package org.eredlab.g4.cabs.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.cabs.service.AppGroupManagerService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.client.IOSocketClient;

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

public class AppGroupManagerServiceImpl extends BaseServiceImpl implements AppGroupManagerService{
	private static Log log = LogFactory.getLog(AppGroupManagerServiceImpl.class);
	PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	String ip = ph.getValue("aim.tcpserver.ip");
	int port = Integer.parseInt(ph.getValue("aim.tcpserver.port"));
	
	public Dto deleteAppGroupManagerItems(Dto pDto) {
		Dto outDto = new BaseDto();
		boolean flag =true;
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			Dto dto = new BaseDto();
			dto.put("group_id", arrChecked[i]);
			Integer relCount = (Integer)crsmDao.queryForObject("AppGroupManager.queryRelByGroupID", dto);
			Integer vncmCount = (Integer)crsmDao.queryForObject("AppGroupManager.queryVncmByGroupID", dto);
			int count = relCount+vncmCount;
			if(count>0){
				flag=false;				
			}else{
				crsmDao.delete("AppGroupManager.deleteAppGroupManagerItems", dto);
			}			
		}		
		if(flag){
			outDto.put("success", new Boolean(true));
		}else{
			outDto.put("success", new Boolean(false));
		}
		return outDto;
	}

	public Dto saveAppGroupManagerItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int count = pDto.getAsInteger("count");
		if(count>0){
			outDto.put("success", new Boolean(false));
		}else{
			Integer id =(Integer) crsmDao.queryForObject("AppGroupManager.queryMaxIndex");
			if(id==null){
				id =0;
			}
			Calendar cal=Calendar.getInstance(); 
			Date createdate=cal.getTime(); 
			Date updatedate = createdate;
			pDto.put("group_id", id+1);
			pDto.put("status", 1);
			pDto.put("create_time", createdate);
			crsmDao.insert("AppGroupManager.saveAppGroupManagerItem", pDto);
			outDto.put("success", new Boolean(true));
		}		
		return outDto;
	}

	public Dto updateAppGroupManagerItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int count = pDto.getAsInteger("count");
		if(count>0){
			outDto.put("success", new Boolean(false));
		}else{
			Calendar cal=Calendar.getInstance(); 
			Date updatedate=cal.getTime();
			pDto.put("update_date", updatedate);
			crsmDao.update("AppGroupManager.updateAppGroupManagerItem", pDto);
			outDto.put("success", new Boolean(true));
		}
		return outDto;
	}


	/**
	 * 保存RSM配置文件修改记录
	 */
	public Dto saveBusiToDB(Dto saveDto) {
		Dto outDto = new BaseDto();
		g4Dao.insert("RsmConfBusi.saveBusi", saveDto);
		outDto.put("success", new Boolean(true));
		return outDto;
	}

	public Dto saveBusiDetailToDB(Dto retDto) {
		Dto outDto = new BaseDto();
		g4Dao.insert("RsmConfBusi.saveBusiDetail", retDto);
		outDto.put("success", new Boolean(true));
		return outDto;
	}
	
	/**
	 * 承载服务器预下线
	 */
	public Dto vncShutdown(Dto inDto) {
		Dto outDto = new BaseDto();
		String[] ids = inDto.getAsString("strChecked1").split(",");
		String[] vTypes = inDto.getAsString("strChecked2").split(",");
		String[] ltypes = inDto.getAsString("strChecked3").split(",");
	
		for(int i=0;i<ids.length;i++){
			String ltype = ltypes[i];
			if(ltype=="1"||"1".equals(ltype)){
				outDto.put("success", new Boolean(false));				
			}else{
				Dto queryDto = new BaseDto();
				queryDto.put("cmd", "preoffshelf");
				queryDto.put("id", ids[i]);
				 queryDto.put("vtype", vTypes[i]);
				 queryDto.put("serialno", "");

				String queryStr = JsonHelper.encodeObject2Json(queryDto);
				String queryString = queryStr + "XXEE";
				log.info("【ADMIN】承载服务器预下线发送报文===>" + queryStr);
				// Client client = new TcpClient();
				IOSocketClient client = new IOSocketClient();
				MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
						CommandSupport.CHARSET, CommandSupport.TAIL,
						CommandSupport.TAIL);
				String revStr = client.sendStr(ip, port, 5000, null, queryString,codecFactory, null);
				revStr = revStr.replace("XXEE", "");
				//String revStr = "{\"cmd\":\"preoffshelf\",\"retcode\":\"0\",\"serialno\":\"e22aff4bb8da4ed991d9f7c043614d68\"}";
				log.info("【ADMIN】承载服务器预下线接收报文===>" + revStr);
				if(revStr!=null && !"".equals(revStr)){
					Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
					int retcode = dto.getAsInteger("retcode");
					String retString;
					if (retcode < 0) {
						retString = "承载服务器预下线失败！";
						outDto.put("success", new Boolean(false));
					} else {
						retString = "承载服务器预下线成功！";
						outDto.put("success", new Boolean(true));
					}
				}
			}
				
			
		}
		
		return outDto;
	}

	public String vncQuery(Dto inDto) {
		List outPutList = new ArrayList();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		String server = inDto.getAsString("server");
		if(!(server==null||"".equals(server))){
			String  vtype = inDto.getAsString("vtype");
			String  qtype = inDto.getAsString("qtype");
			Dto outPutDto = new BaseDto();
			outPutDto.put("cmd", "vncresources");
			outPutDto.put("vncid", server);
			outPutDto.put("vtype", vtype);
			outPutDto.put("serialno", "");
			String queryResStr = JsonHelper.encodeObject2Json(outPutDto);
			String queryResString = queryResStr + "XXEE";
			log.info("【ADMIN】查询承载服务器资源发送报文===>" + queryResString);
			IOSocketClient clientRes = new IOSocketClient();
			String retStr = clientRes.sendStr(ip, port, 5000, null, queryResString,codecFactory, null);
//			//String retStr="{\"cmd\":\"vncresources\",\"retcode\":\"0\",\"vncip\":\"192.168.100.31\",\"vncport\":\"14117\",\"vtype\":\"1\",\"maxnum\":\"100\",\"freenum\":\"80\",\"ltype\":\"2\",\"qtype\":\"1\",\"resources\":[\"28cab81a4eed48aba20b03bc2e997206\",\"032971323a52498c8a3b9f0ed8416600\"],\"errorresources\":[\"28cab81a4eed48aba20b03bc2e997206\",\"032971323a52498c8a3b9f0ed8416600\"],\"serialno\":\"db974e9d8f424d2da87b246831442867\"}XXEE";
			retStr = retStr.replace("XXEE", "");
			log.info("【ADMIN】查询承载服务器资源接收报文===>" + retStr);
			if(retStr!=null && !"".equals(retStr)){
				String JsonRetStr = JsonHelper.encodeObject2Json(retStr);
				Dto retDto = JsonHelper.parseSingleJson2Dto(JsonRetStr);
				List resourcesList = retDto.getAsList("resources");
				List errorresourcesList = retDto.getAsList("errorresources");
				String resserialno = retDto.getAsString("serialno");
				String ltype = retDto.getAsString("ltype");
				outPutDto.put("ltype", ltype);
				List list = new ArrayList();
				list.addAll(errorresourcesList);
				list.addAll(resourcesList);
				if(list.size()>0){
					for(int k=0;k<list.size();k++){
						String resid = (String) list.get(k); 
						Dto sourceDto = new BaseDto();
						sourceDto.put("cmd", "vncdetail");
						sourceDto.put("resid", resid);
						sourceDto.put("serialno", resserialno);
						String queryResdetailStr = JsonHelper.encodeObject2Json(sourceDto);
						String queryResdetailString = queryResdetailStr + "XXEE";
						log.info("【ADMIN】查询结点会话发送报文===>" + queryResdetailString);
						IOSocketClient clientvnc = new IOSocketClient();
						String detailRetStr = clientvnc.sendStr(ip, port, 5000, null, queryResdetailString,codecFactory, null);
	//					String detailRetStr = "{\"cmd\":\"vncdetail\",\"retcode\":\"0\",\"sessionid\":\"01001823109160123456781234\",\"sid\":\"1001\",\"operid\":\"0\",\"vtype\":\"1\",\"sip\":\"192.168.70.249\",\"sport\":\"6000\",\"vncip\":\"192.168.70.249\",\"vncport\":\"6000\",\"keyport\":\"60002\",\"iip\":\"192.168.70.249\",\"iport\":\"10000\",\"serialno\":\"db974e9d8f424d2da87b246831442867\"}XXEE";
						detailRetStr = detailRetStr.replace("XXEE", "");
						log.info("【ADMIN】查询结点会话接收报文===>" + detailRetStr);
						String JsonRetDetailStr = JsonHelper.encodeObject2Json(detailRetStr);
						Dto retDetailDto = JsonHelper.parseSingleJson2Dto(JsonRetDetailStr);
						String sessionid = retDetailDto.getAsString("sessionid");
						if(!(sessionid==null||"".equals(sessionid))){
							Dto outDto = new BaseDto();
							outDto.put("ltype", ltype);
							outDto.put("sessionid", retDetailDto.getAsString("sessionid"));
							outDto.put("sid", retDetailDto.getAsString("sid"));
							outDto.put("operid", retDetailDto.getAsString("operid"));
							outDto.put("vtype", retDetailDto.getAsString("vtype"));
							outDto.put("sip", retDetailDto.getAsString("sip"));
							outDto.put("sport", retDetailDto.getAsString("sport"));
							outDto.put("vncip", retDetailDto.getAsString("vncip"));
							outDto.put("vncport", retDetailDto.getAsString("vncport"));
							outDto.put("keyport", retDetailDto.getAsString("keyport"));
							outDto.put("iip", retDetailDto.getAsString("iip"));
							outDto.put("iport", retDetailDto.getAsString("iport"));
							outPutList.add(outDto);
						}									
					}
				}
			}
			
//			outPutList.add(outPutDto);
		}else{
			String  vtype = inDto.getAsString("vtype");
			String  qtype = inDto.getAsString("qtype");
		
			Dto queryDto = new BaseDto();
			queryDto.put("cmd", "vncquery");
			queryDto.put("vtype", vtype);
			queryDto.put("qtype", qtype);
			queryDto.put("ltype", "0");
			queryDto.put("serialno", "");
			String queryStr = JsonHelper.encodeObject2Json(queryDto);
			String queryString = queryStr + "XXEE";
			log.info("【ADMIN】查询承载服务器发送报文===>" + queryString);
			IOSocketClient client = new IOSocketClient();
			
			String revStr = client.sendStr(ip, port, 5000, null, queryString,codecFactory, null);
//			String revStr="{\"cmd\":\"vncquery\",\"vncpool\":[\"192.168.70.249:25001\",\"192.168.70.249:25000\"],\"retcode\":\"0\",\"vtype\":\"1\",\"qtype\":\"0\",\"serialno\":\"e22aff4bb8da4ed991d9f7c043614d68\"}XXEE";
			revStr = revStr.replace("XXEE", "");
//			revStr = "{\"cmd\":\"vncresources\",\"vncip\":\"\",\"vncport\":\"\",\"vtype\":\"2\",\"maxnum\":\"\",\"freenum\":\"\",\"ltype\":\"\",\"qtype\":\"\",\"resources\":\"\",\"errorresources\":\"\",\"retcode\":\"-1211\",\"serialno\":\"\"}";
			
			log.info("【ADMIN】查询承载服务器接收报文===>" + revStr);
			if (!(revStr == null || "".equals(revStr))) {
				String JsonStr = JsonHelper.encodeObject2Json(revStr);
				Dto dto = JsonHelper.parseSingleJson2Dto(JsonStr);
				List vncpoolList = dto.getAsList("vncpool");
				String rvtype = dto.getAsString("vtype");
				String rqtype = dto.getAsString("qtype");
				String serialno = dto.getAsString("serialno");
				String retcode = dto.getAsString("retcode");
				if(vncpoolList!=null && vncpoolList.size()>0){
					for(int i=0; i<vncpoolList.size();i++){
						Dto outPutDto = new BaseDto();
						String vncidport = (String) vncpoolList.get(i);
						outPutDto.put("cmd", "vncresources");
						outPutDto.put("vncid", vncidport);
						outPutDto.put("vtype", rvtype);
						outPutDto.put("serialno", serialno);
						String queryResStr = JsonHelper.encodeObject2Json(outPutDto);
						String queryResString = queryResStr + "XXEE";
						log.info("【ADMIN】查询承载服务器资源发送报文===>" + queryResString);
						IOSocketClient clientRes = new IOSocketClient();
						String retStr = clientRes.sendStr(ip, port, 5000, null, queryResString,codecFactory, null);
//						String retStr="{\"cmd\":\"vncresources\",\"retcode\":\"0\",\"vncip\":\"192.168.100.31\",\"vncport\":\"14117\",\"vtype\":\"1\",\"maxnum\":\"100\",\"freenum\":\"80\",\"ltype\":\"2\",\"qtype\":\"1\",\"resources\":[\"28cab81a4eed48aba20b03bc2e997206\",\"032971323a52498c8a3b9f0ed8416600\"],\"errorresources\":[\"28cab81a4eed48aba20b03bc2e997206\",\"032971323a52498c8a3b9f0ed8416600\"],\"serialno\":\"db974e9d8f424d2da87b246831442867\"}XXEE";
						retStr = retStr.replace("XXEE", "");
						log.info("【ADMIN】查询承载服务器资源接收报文===>" + retStr);
						if(retStr!=null && !"".equals(retStr)){
							String JsonRetStr = JsonHelper.encodeObject2Json(retStr);
							Dto retDto = JsonHelper.parseSingleJson2Dto(JsonRetStr);
							List resourcesList = retDto.getAsList("resources");
							List errorresourcesList = retDto.getAsList("errorresources");
							String resserialno = retDto.getAsString("serialno");
							String ltype = retDto.getAsString("ltype");
							outPutDto.put("ltype", ltype);
							List list = new ArrayList();
							list.addAll(errorresourcesList);
							list.addAll(resourcesList);
							if(list.size()>0){
								for(int k=0;k<list.size();k++){
									String resid = (String) list.get(k);
									Dto sourceDto = new BaseDto();
									sourceDto.put("cmd", "vncdetail");
									sourceDto.put("resid", resid);
									sourceDto.put("serialno", resserialno);
									String queryResdetailStr = JsonHelper.encodeObject2Json(sourceDto);
									String queryResdetailString = queryResdetailStr + "XXEE";
									log.info("【ADMIN】查询结点会话发送报文===>" + queryResdetailString);
									IOSocketClient clientvnc = new IOSocketClient();
									String detailRetStr = clientvnc.sendStr(ip, port, 5000, null, queryResdetailString,codecFactory, null);
	//								String detailRetStr = "{\"cmd\":\"vncdetail\",\"retcode\":\"0\",\"sessionid\":\"01001823109160123456781234\",\"sid\":\"1001\",\"operid\":\"0\",\"vtype\":\"1\",\"sip\":\"192.168.70.249\",\"sport\":\"6000\",\"vncip\":\"192.168.70.249\",\"vncport\":\"6000\",\"keyport\":\"60002\",\"iip\":\"192.168.70.249\",\"iport\":\"10000\",\"serialno\":\"db974e9d8f424d2da87b246831442867\"}XXEE";
									detailRetStr = detailRetStr.replace("XXEE", "");
									log.info("【ADMIN】查询结点会话接收报文===>" + detailRetStr);
									String JsonRetDetailStr = JsonHelper.encodeObject2Json(detailRetStr);
									Dto retDetailDto = JsonHelper.parseSingleJson2Dto(JsonRetDetailStr);
									String sessionid = retDetailDto.getAsString("sessionid");
									if(!(sessionid==null||"".equals(sessionid))){
										Dto outDto = new BaseDto();
										outDto.put("ltype", ltype);
										outDto.put("sessionid", retDetailDto.getAsString("sessionid"));
										outDto.put("sid", retDetailDto.getAsString("sid"));
										outDto.put("operid", retDetailDto.getAsString("operid"));
										outDto.put("vtype", retDetailDto.getAsString("vtype"));
										outDto.put("sip", retDetailDto.getAsString("sip"));
										outDto.put("sport", retDetailDto.getAsString("sport"));
										outDto.put("vncip", retDetailDto.getAsString("vncip"));
										outDto.put("vncport", retDetailDto.getAsString("vncport"));
										outDto.put("keyport", retDetailDto.getAsString("keyport"));
										outDto.put("iip", retDetailDto.getAsString("iip"));
										outDto.put("iport", retDetailDto.getAsString("iport"));
										outPutList.add(outDto);
									}									
								}
							}
						}
						
//						outPutList.add(outPutDto);
					}
				}
				
			}
		}
		
		return JsonHelper.encodeList2PageJson(outPutList, outPutList.size(),
				null);
	}

	public Dto vncUpload(Dto inDto) {
		Dto outDto = new BaseDto();
		String[] ids = inDto.getAsString("strChecked1").split(",");
		String[] vTypes = inDto.getAsString("strChecked2").split(",");
		String[] ltypes = inDto.getAsString("strChecked3").split(",");
	
		for(int i=0;i<ids.length;i++){
			String ltype = ltypes[i];
			if(ltype=="2"||"2".equals(ltype)){
				outDto.put("success", new Boolean(false));				
			}else{
				Dto queryDto = new BaseDto();
				queryDto.put("cmd", "onshelf");
				queryDto.put("id", ids[i]);
				queryDto.put("vtype", vTypes[i]);
				queryDto.put("serialno", "");

				String queryStr = JsonHelper.encodeObject2Json(queryDto);
				String queryString = queryStr + "XXEE";
				log.info("【ADMIN】承载服务器上线发送报文===>" + queryStr);
				// Client client = new TcpClient();
				IOSocketClient client = new IOSocketClient();
				MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
						CommandSupport.CHARSET, CommandSupport.TAIL,
						CommandSupport.TAIL);
				String revStr = client.sendStr(ip, port, 5000, null, queryString,codecFactory, null);
				revStr = revStr.replace("XXEE", "");
				//String revStr = "{\"cmd\":\"preoffshelf\",\"retcode\":\"0\",\"serialno\":\"e22aff4bb8da4ed991d9f7c043614d68\"}";
				log.info("【ADMIN】承载服务器上线接收报文===>" + revStr);
				if(revStr!=null && !"".equals(revStr)){
					Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
					int retcode = dto.getAsInteger("retcode");
					String retString;
					if (retcode < 0) {
						retString = "承载服务器上线失败！";
						outDto.put("success", new Boolean(false));
					} else {
						retString = "承载服务器上线成功！";
						outDto.put("success", new Boolean(true));
					}
				}
			}	
		}
		
		return outDto;
	}

	/**
	 * 承载服务器查询
	 */
	public String aimQuery(Dto inDto) {
		Dto out = new BaseDto();
		String  vtype = inDto.getAsString("vtype");
		String  qtype = inDto.getAsString("qtype");
		String  ltype = inDto.getAsString("ltype");
		List outPutList = new ArrayList();
		Dto queryDto = new BaseDto();
		queryDto.put("cmd", "vncquery");
		queryDto.put("vtype", vtype);
		queryDto.put("qtype", qtype);
		queryDto.put("ltype", ltype);
		queryDto.put("serialno", "");
		String queryStr = JsonHelper.encodeObject2Json(queryDto);
		String queryString = queryStr + "XXEE";
		log.info("【ADMIN】查询承载服务器发送报文===>" + queryString);
		IOSocketClient client = new IOSocketClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		String revStr = client.sendStr(ip, port, 5000, null, queryString,codecFactory, null);
		revStr = revStr.replace("XXEE", "");
	//	String revStr = "{\"cmd\":\"vncquery\",\"vncpool\":[\"192.168.70.249:25001\",\"192.168.70.249:25000\"],\"retcode\":\"0\",\"vtype\":\"1\",\"qtype\":\"0\",\"serialno\":\"e22aff4bb8da4ed991d9f7c043614d68\"}";
		log.info("【ADMIN】查询承载服务器接收报文===>" + revStr);
		//{"cmd":"vncquery","vncpool":["192.168.70.249:25001","192.168.70.249:25000"],"retcode":"0","vtype":"1","qtype":"0","serialno":"e22aff4bb8da4ed991d9f7c043614d68"}XXEE
		if (!(revStr == null || "".equals(revStr))) {
			String JsonStr = JsonHelper.encodeObject2Json(revStr);
			Dto dto = JsonHelper.parseSingleJson2Dto(JsonStr);
			List vncpoolList = dto.getAsList("vncpool");
			String rvtype = dto.getAsString("vtype");
			String rqtype = dto.getAsString("qtype");
			String serialno = dto.getAsString("serialno");
			String retcode = dto.getAsString("retcode");
			if(vncpoolList.size()>0){
				for(int i=0; i<vncpoolList.size();i++){
					Dto outPutDto = new BaseDto();
					String vncidport = (String) vncpoolList.get(i);
					outPutDto.put("cmd", "vncresources");
					outPutDto.put("vncid", vncidport);
					outPutDto.put("vtype", rvtype);
					outPutDto.put("serialno", serialno);
					String queryResStr = JsonHelper.encodeObject2Json(outPutDto);
					String queryResString = queryResStr + "XXEE";
					log.info("【ADMIN】查询承载服务器资源发送报文===>" + queryResString);
					IOSocketClient clientRes = new IOSocketClient();
					String retStr = clientRes.sendStr(ip, port, 5000, null, queryResString,codecFactory, null);
					retStr = retStr.replace("XXEE", "");
					log.info("【ADMIN】查询承载服务器资源接收报文===>" + retStr);
					if(retStr!=null && !"".equals(retStr)){
						String JsonRetStr = JsonHelper.encodeObject2Json(retStr);
						Dto retDto = JsonHelper.parseSingleJson2Dto(JsonRetStr);
						String rltype = retDto.getAsString("ltype");
						outPutDto.put("ltype", rltype);
						outPutDto.put("qtype", rqtype);
						outPutDto.put("retcode", retcode);
						outPutList.add(outPutDto);
					}
				}
			}
		}
		return JsonHelper.encodeList2PageJson(outPutList, outPutList.size(),
				null);
	}
	

}
