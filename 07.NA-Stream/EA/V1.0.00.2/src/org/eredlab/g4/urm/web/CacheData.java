package org.eredlab.g4.urm.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.common.util.RedisUseable;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;

import prod.nebula.framework.mdb.impl.MdbDataImpl;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class CacheData extends BaseAction {
	private static Log log = LogFactory.getLog(CacheData.class);
	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	private String ip = ph.getValue("urm.redisserver.ip") ;
	private Integer port =Integer.valueOf(ph.getValue("urm.redisserver.port")) ;
	private String urm_redis_passwd = ph.getValue("urm.redisserver.password");
	private MdbDataImpl  mdb = (MdbDataImpl) super.getService("urmmdbImpl");

	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward pageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("initView");
	}
	public ActionForward queryData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		RedisUseable usable = new RedisUseable() ;
		if(!usable.jedisUseable(ip, port, urm_redis_passwd)){
			log.info("【错误】：URM的缓存密码配置错误！");
			String str = "缓存密码错误，查询失败！";
			write(str, response);
			return mapping.findForward(null);
		}
		try{
			String newkey = inDto.getAsString("key");
			String key =newkey.replace(":", "");
			String jsonStr = "";
				if(key.contains("URM_THRD_RUNOUT")){  				//URM_THRD_RUNOUTareaId_sgID    String
					Object MemValue = mdb.getString(key);	
					if(MemValue!=null){
						String a = MemValue.toString();
						if(a=="0"||"0".equals(a)){
							jsonStr="该网络区域阈值未使用完！";
						}else{
							jsonStr="该网络区域阈值已使用完！";
						}
					}else{
						jsonStr="该网络区域未设置阈值或者该网络区域不存在！";
					}				
				}else if(key.contains("URM_THRD")){   				//URM_THRDareaID_sgID     String
					Object MemValue = mdb.getString(key);	
					if(MemValue!=null){
						jsonStr = "该网络区域的阈值大小为"+MemValue.toString()+"%";
					}else{
						jsonStr="该网络区域未设置阈值或者该网络区域不存在！";
					}	
				}else if(key.contains("URM_RF_PER")){ 				//URM_RF_PERareaID_sgID_rfID   String
					Object MemValue = mdb.getString(key);	
					if(MemValue!=null){
						jsonStr = "频点对应的已使用的带宽大小"+MemValue.toString();
					}else{
						jsonStr="URM_RF_PER没有数据！";
					}	
				}else if(key.contains("URM_RES_RF")){				//URM_RF_PFareaID_sgID_rfID   List<String>
					List<String> urm_res_rf = new ArrayList<String>();
					urm_res_rf = (List) mdb.getObject(key);
					int linesNum = 0 ;
					
					if(urm_res_rf.size()>0){
						for(int j=0;j<urm_res_rf.size();j++){
							String appuserid = (String) mdb.getString("RES"+urm_res_rf.get(j));
				    		String qamInfo = (String) mdb.getString("URM_QAM"+urm_res_rf.get(j));
				    		if(qamInfo!=null && !"".equals(qamInfo)){
					    		String[] strArray = qamInfo.split("\\|");
					    		String ip = strArray[0];
					    		String port = strArray[1];
					    		String rfid=  strArray[4];
					    		linesNum ++ ;
					    		jsonStr+="【" + linesNum + "】：QAMIP="+ip+",PORT="+port+",占用的AAPUSER="+appuserid+",资源ID="+urm_res_rf.get(j)+",频点="+rfid+"<br>";
					    	}
						}
					}else{
						jsonStr="URM_RES_RF没有数据！";
					}
					
				}else if(key.contains("URM_RF_RUNOUT")){ 			//URM_RF_RUNOUTareaID_sgID_rfID    String
					Object MemValue = mdb.getString(key);	
					if(MemValue!=null){
						jsonStr = "当前频点使用情况："+MemValue.toString();
					}else{
						jsonStr="URM_RF_RUNOUT没有数据！";
					}	
				}else if(key.contains("URM_RF")){                   //URM_RFareaID_sgID    List<String>
					List urm_rf = new ArrayList();
				    List urm_res = new ArrayList();
				    urm_rf = (List) mdb.getObject(key);
				    int pos = newkey.indexOf(":");
				    String network = key.substring(pos, key.length());
				    
				    int linesNum = 0 ; 
				   
				    if(urm_rf!=null){//没有URM_RF对应的key值
				    	 if(urm_rf.size()>0){//有key值，但是内容为空
						    	for(int i=0;i<urm_rf.size();i++){
//						    		String rf_info = (String) urm_rf.get(i);
//						    		rf_info = rf_info.substring(0, rf_info.lastIndexOf("_"));
//						    		urm_res = (List) mdbDataImpl.getObject("URM_RES_RF_"+network+"_"+rf_info);
						    	  urm_res = (List) mdb.getObject("URM_RES_RF_"+network+"_"+urm_rf.get(i));
						    	  for(int j=0;j<urm_res.size();j++){
						    		   String appuserid = (String) mdb.getString("RES"+urm_res.get(j));
						    		   String qamInfo = (String) mdb.getString("URM_QAM"+urm_res.get(j));
						    		   if(qamInfo!=null && !"".equals(qamInfo)){
							    		   String[] strArray = qamInfo.split("\\|");
							    		   String ip = strArray[0];
							    		   String port = strArray[1];
							    		   String rfid=  strArray[4];
							    		   linesNum ++ ;
							    		   jsonStr+="【" + linesNum + "】：QAMIP="+ip+",PORT="+port+",占用的AAPUSER="+appuserid+",资源ID="+urm_res.get(j)+",频点="+rfid;
							    		   if(strArray.length>=8){
							    			   jsonStr += ",切台方式="+strArray[7];
							    		   }
							    		   jsonStr += "<br>";
						    		   }
						    	  }
						    	}
					        }else{
					        	jsonStr="没有找到资源！";
					        }
				    }else{
				    	jsonStr="没有找到资源！";
				    }
				   
				}else if(key.contains("URM_RESID_RF")){			//URM_RESID_RFareaID_sgID_rfID    List<String>
					Object MemValue = mdb.getString(key);
					if(MemValue!=null){
						jsonStr ="占用当前resid的频点是："+ MemValue.toString();
					}else{
						jsonStr="URM_RESID_RF没有数据！";
					}
					 
				}else if(key.contains("RESOURCE_TYPE")){		//RESOURCE_TYPEresid               String
					Object MemValue = mdb.getString(key);	
					if(MemValue!=null){
						String a = MemValue.toString();
						if(a=="1"||"1".equals(a)){
							jsonStr="该资源被标清占用！";
						}else{
							jsonStr="该资源被高清占用！";
						}
					}else{
						jsonStr="RESOURCE_TYPE没有数据！";
					}	
				}else if(key.contains("RES_APPUSERID")){		//RES_APPUSERID					  List<String>
					 List res_appuserid = new ArrayList();
					 res_appuserid = (List) mdb.getObject(key);
					 if(res_appuserid.size()>0){
						 jsonStr+="资源使用情况：<br>";
						 for(int i=0;i<res_appuserid.size();i++){
							 jsonStr+=res_appuserid.get(i)+"<br>";
						 }
					 }else{
						 jsonStr="RES_APPUSERID没有数据！";
					 }
					 
				}else if(key.contains("URM_QAM")){				//URM_QAMresid						String
					Object MemValue = mdb.getString(key);
					if(MemValue!=null){
						String memValue = MemValue.toString();
						String[] memValues = memValue.split("\\|");
						jsonStr = "该资源详细信息如下：<br>QAMIP:"+memValues[0]+"<br>IPQAM端口:"+memValues[1]
						+"<br>serverid："+memValues[2]+"<br>ptmid:"+memValues[3]+"<br>频点编号："+memValues[4]
						+"<br>数据输出端口："+memValues[5]+"<br>IPQAM资源类型："+memValues[6];                                              
		    		    if(memValues.length>=8){
		    			   jsonStr += "<br>切台方式:"+memValues[7];
		    		    }
					}else{
						jsonStr="URM_QAM没有数据！";
					}
				}else if(key.contains("URM_CHECK")){			//URM_CHECKappname 					String
					Object MemValue = mdb.getString(key);	
					if(MemValue!=null){
						jsonStr = MemValue.toString();
					}else{
						jsonStr="URM_CHECK没有数据！";
					}	
				}else if(key.contains("RES")){					//RES_resid							String
					Object MemValue = mdb.getString(key);	
					if(MemValue!=null){
						jsonStr = "使用当前资源的用户是："+MemValue.toString();
					}else{
						jsonStr="RES没有数据！";
					}	
				}else{
					jsonStr="键值填写错误，请确认后再填！";
				}
				 write(jsonStr, response);
		}catch (Exception ex) {       
            ex.printStackTrace();  
        }finally{
        } 
		
		return mapping.findForward(null);
	}
	public ActionForward resSynchronization(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String appName= "";
		String resType = "";
		String bandWidth = "";
		Dto outDto = new BaseDto();
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String key =inDto.getAsString("key");
		String ipqam_res_id = key.substring(7, key.length());
//		String ipPort = inDto.getAsString("ipPort");
//		String[] ipports = ipPort.split(":");
		String sqlstr = "select b.sg_id,a.ipqam_ip,c.exp_port,c.ipqam_res_id,c.app_name,c.res_type,c.band_width,c.pid_id,d.rf_id,c.server_id,d.order_port,a.ipqam_id,b.area_id " +
					" from urm_ipqam_info a,urm_network_area b,urm_ipqam_resource c,urm_ipqam_frequency d where b.network_code=d.network_code and d.rf_code=c.rf_code and c.ipqam_id=a.ipqam_id and c.res_status>=0 and a.status>=0 and c.ipqam_res_id="+ipqam_res_id;
		List rsList = getList(sqlstr);
		String[] sqls = new String[rsList.size()];
		if(rsList != null && rsList.size() >0){
			for(int k =0;k<rsList.size();k++){
				Object[] IpqamObjArr = (Object[])rsList.get(k);
				String sg_id = IpqamObjArr[0]!=null?IpqamObjArr[0].toString():"";
				String ipqamIp = IpqamObjArr[1].toString();
				String ipqamPort = IpqamObjArr[2].toString();
				String resId = IpqamObjArr[3].toString();
				appName = IpqamObjArr[4]!=null?IpqamObjArr[4].toString():"";
				resType = IpqamObjArr[5].toString();
				bandWidth = IpqamObjArr[6].toString();
				String ptmId = IpqamObjArr[7].toString();
				String rfId = IpqamObjArr[8].toString();
				String serverId = IpqamObjArr[9].toString();
				String orderPort = IpqamObjArr[10].toString();
				String ipqam_id = IpqamObjArr[11].toString();
				String area_id = IpqamObjArr[12].toString();
				
				log.info("area_id="+area_id+",sg_id="+sg_id+",rfId="+rfId+",resource_id="+resId);
				/*每个频点对应的最大使用带宽 38M*/
				/**判断查询时配置文件是否有密码开始*/
				RedisUseable usable = new RedisUseable();
				 if(!usable.jedisUseable(ip, port,urm_redis_passwd)){
					 log.info("【错误】：URM的缓存密码配置错误！");
					 outDto.put("msg", "缓存密码错误，操作失败！") ;
					 outDto.put("success", FALSE);
					 String jsonStr = JsonHelper.encodeObject2Json(outDto);
					 write(jsonStr, response);
					 return mapping.findForward(null);
				 }
					mdb.removeString("URM_RF_PER_"+area_id+"_"+sg_id+"_"+rfId);
					mdb.putString("URM_RF_PER_"+area_id+"_"+sg_id+"_"+rfId, "0");
					/*sgId下对应的频点集合*/
					List<String> rfList = new ArrayList<String>();
					rfList = (List<String>) mdb.getObject("URM_RF_"+area_id+"_"+sg_id);
					if(null == rfList){
						List<String> rfNewList = new ArrayList<String>();
						rfNewList.add(rfId);
						mdb.putObject("URM_RF_"+area_id+"_"+sg_id, rfNewList);
					}else{
						if(!rfList.contains(rfId)){
							rfList.add(rfId);
							mdb.putObject("URM_RF_"+area_id+"_"+sg_id, rfList);
						}
					}
					
					/*频点对应resId的集合，区域和中心资源对应的sg_id为空*/
					List<String> resList = new ArrayList<String>();
					resList = (List<String>) mdb.getObject("URM_RES_RF_"+area_id+"_"+sg_id+"_"+rfId);
					if(null == resList){
						List<String> resNewList = new ArrayList<String>();
						resNewList.add(resId);
						mdb.putObject("URM_RES_RF_"+area_id+"_"+sg_id+"_"+rfId, resNewList);
					}else{
						resList.add(resId);
						mdb.putObject("URM_RES_RF_"+area_id+"_"+sg_id+"_"+rfId, resList);
					}
					mdb.putString("RES"+resId, "0");
					mdb.putString("URM_QAM"+resId, ipqamIp+"|"+ipqamPort+"|"+serverId+"|"+ptmId+"|"+rfId+"|"+orderPort+"|"+resType);
			 }
				log.info("******【XMEM】缓存数据加载结束******【成功】");
			outDto.put("success", TRUE);
		}else{
			outDto.put("success", FALSE);
		}	
		String jsonStr = JsonHelper.encodeObject2Json(outDto);
		write(jsonStr, response);
		return mapping.findForward(null);
	}
	public List getList(String sql){
		PropertiesHelper pht = PropertiesFactory.getPropertiesHelper(PropertiesFile.JDBC);
		String DBUrl =pht.getValue("urm.mysqlserver.url");
		String DBUser = pht.getValue("urm.mysqlserver.username");
		String password = pht.getValue("urm.mysqlserver.password");
		List rsList = new ArrayList();
		ComboPooledDataSource cpds = null;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
				Class.forName(pht.getValue("urm.mysqlserver.drivename"));
				conn = DriverManager.getConnection(DBUrl,DBUser,password);
				conn.setAutoCommit(false);
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs == null){
					return rsList;
				}
				ResultSetMetaData rsmd = rs.getMetaData();
				while (rs.next()) {
					Object[] rsArr = new Object[rsmd.getColumnCount()];
					for (int i = 0; i < rsmd.getColumnCount(); i++) {
						rsArr[i]=rs.getObject(i+1);
					}
					rsList.add(rsArr);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return rsList;
	}
}
