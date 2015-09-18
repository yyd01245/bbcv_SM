package org.eredlab.g4.urm.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.util.G4Utils;
import org.eredlab.g4.urm.service.NetworkService;
import org.eredlab.g4.urm.util.Contents;
import org.eredlab.g4.urm.util.SendMessageClient;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class NetworkServiceImpl extends BaseServiceImpl implements	NetworkService {
	
	private static Log log = LogFactory.getLog(NetworkServiceImpl.class);
	
	public Dto saveNetworks(Dto pDto){
		Dto outDto = new BaseDto();
		
		int sum = pDto.getAsInteger("sum");
		String strNetRegionName = pDto.getAsString("strNetRegionName");
		String iNavgationStreamNum = pDto.getAsString("iNavgationStreamNum");
		String sdiNavgationStreamNum = pDto.getAsString("sdiNavgationStreamNum");
		String iAdvertisementStreamNum = pDto.getAsString("iAdvertisementStreamNum");
		String sdiAdvertisementStreamNum = pDto.getAsString("sdiAdvertisementStreamNum");
		String strNetworkComment = pDto.getAsString("strNetworkComment");
		
		
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
		//	String network_code = IDHelper.getnetworkID();
			//pDto.put("network_code", network_code);
			pDto.put("strNetRegionName", strNetRegionName);
			pDto.put("iNavgationStreamNum", iNavgationStreamNum);
			pDto.put("sdiNavgationStreamNum", sdiNavgationStreamNum);
			pDto.put("iAdvertisementStreamNum", iAdvertisementStreamNum);
			pDto.put("sdiAdvertisementStreamNum", sdiAdvertisementStreamNum);
			pDto.put("strNetworkComment", strNetworkComment);
			pDto.put("state", 0);
			
			urmDao.insert("Network.saveNetwork", pDto);	
			
			
			  SendMessageClient s = new SendMessageClient(Contents.NETWORK,Contents.INSERT);  
			  s.send();
//			//添加事件
//			pDto.put("ipqam_event_id", network_code);
//			pDto.put("oper_status", 11);
//			updateMDBForQamRes(pDto);
//			Date create_date = new Date(new java.util.Date().getTime());
//			pDto.put("ipqam_event_id", network_code);
//			pDto.put("oper_status",11);
//			pDto.put("type",4);
//			pDto.put("id", IDHelper.getYunAppID());
//			pDto.put("status",1);
//			pDto.put("create_date",create_date);
//			urmDao.insert("Qam.dynamicLoadMDB",pDto);
//
//			//添加操作记录
//			String busi_id = IDHelper.getCodeID() ;
//			Dto logDto = new BaseDto() ;
//			logDto.put("busi_id",busi_id);
//			logDto.put("busi_code", "B11_31");
//			logDto.put("oper_id", pDto.get("oper_id"));
//			logDto.put("area_id", pDto.getAsString("dept_id"));
//			urmDao.insert("Qam.saveOperLog",logDto);
			outDto.put("success", TRUE);
		}
		
		return outDto;
	}
	
	public Dto deleteNetwork(Dto inDto) {
		Dto dto = new BaseDto();
		Dto outputDto = new BaseDto();
		String[] arrChecked = inDto.getAsString("strChecked").split(",");
//		String busi_id = IDHelper.getCodeID();
//		Date create_date = new Date(new java.util.Date().getTime());
		for (int i = 0; i < arrChecked.length; i++) {
			String strNetRegionNum = arrChecked[i];
			dto.put("strNetRegionNum", strNetRegionNum);
			
			
			
			
//			Dto outDto = (Dto)urmDao.queryForObject("Network.queryyNetwork",dto);
//			String area_id = outDto.getAsString("area_id");
//			if(area_id=="99999"||"99999".equals(area_id)){
//				dto.put("network_code", "-1");
//				outputDto.put("success", FALSE);
//				return outputDto;
//			}else{
//				outputDto.put("success", TRUE);
//			}
//			
			// 网络区域删除数据插入到历史表中
//			outDto.put("busi_id", busi_id);
//			urmDao.insert("Network.saveNetworkHis",outDto);
			
			//删除原表中的数据
			urmDao.delete("Network.deleteNetwork", dto);
			
			  SendMessageClient s = new SendMessageClient(Contents.NETWORK,Contents.DELETE);  
			  s.send();
			//添加网路区域删除事件
//			inDto.put("ipqam_event_id", network_code);
//			inDto.put("oper_status", 12);
//			inDto.put("type",3);
//			inDto.put("id", IDHelper.getYunAppID());
//			inDto.put("status",1);
//			inDto.put("create_date",create_date);
//			urmDao.insert("Qam.dynamicLoadMDB",inDto);
//			
//			List qamList = urmDao.queryForList("Qam.getQamListByKey",dto);
//			if(qamList!=null){
//				for(int k = 0 ; k < qamList.size() ; k++){
//					Dto d = (Dto) qamList.get(k);
//					
//					//删除qam上报数据
//					List tempDtoli = urmDao.queryForList("qamDy.getHisQam",d);
//					if(tempDtoli!=null&&tempDtoli.size()>0){
//						for(int j=0;j<tempDtoli.size() ; j++){
//							Dto tempDto = (Dto)tempDtoli.get(j);
//							
//							urmDao.update("qamDy.deleteDyQam",tempDto);
//							urmDao.delete("qamDy.deleteDyRf",tempDto);
//							tempDto.put("res_status", -1);
//							urmDao.update("qamDy.updateDyRes",tempDto);
//							urmDao.update("qamDy.updateStaticRes",tempDto);
//							tempDto.put("oper_type", "DEL");
//							urmDao.update("qamDy.handlerPrompt",tempDto);
//							
//						}
//					}
//				
//							
//					//移QAM信息、QAM资源到历史表
//					d.put("busi_id",busi_id);
//					urmDao.insert("Qam.saveQamItemHis",d);
//					List resList = urmDao.queryForList("QamResource.getIDByKey", d);
//					if(resList!=null){
//						for(Object o : resList){
//							Dto resDto = (Dto) o ;
//							resDto.put("busi_id",busi_id);
//							urmDao.insert("QamResource.saveQamResItemHis",resDto);
//						}
//					}
//					urmDao.delete("QamResource.deleteResManagerByIPqam",d);
//				}
//			}
//			urmDao.delete("Qam.deleteItemsByCode",dto);
//			
//			//移频点数据到历史表
//			List rfList = urmDao.queryForList("Network.queryRfIDByNetwork", dto);
//			if(rfList!=null){
//				for(int k = 0 ; k < rfList.size() ; k ++){
//					Dto rfDto = (Dto) rfList.get(k);
//					rfDto.put("busi_id",busi_id);
//					urmDao.insert("RfResource.saveRfItemHis",rfDto);
//				}
//			}
//			urmDao.delete("RfResource.deleteItemsByCode",dto);
			
//			//添加阈值删除的事件
//			Dto pDto = new BaseDto();
//			pDto.put("ipqam_event_id", network_code);
//			pDto.put("oper_status",12);
//			pDto.put("type",4);
//			pDto.put("id", IDHelper.getYunAppID());
//			pDto.put("status",1);
//			pDto.put("create_date",create_date);
//			urmDao.insert("Qam.dynamicLoadMDB",pDto);
		}
		
//		//记录网络区域删除的操作
//		Dto logDto = new BaseDto() ;
//		logDto.put("busi_id", busi_id);
//		logDto.put("busi_code","B11_33");
//		logDto.put("oper_id", inDto.getAsString("oper_id"));
//		logDto.put("area_id", inDto.getAsString("dept_id"));
//		urmDao.insert("Qam.saveOperLog",logDto);
		
		return outputDto;
	}

	public Dto updateNetworks(Dto inDto) {
		Dto retDto = new BaseDto();
		int sum = inDto.getAsInteger("sum");
	
		if(sum>0){
			retDto.put("success", FALSE);
		}else{
			String strNetRegionName = inDto.getAsString("strnetregionname");
			String iNavgationStreamNum = inDto.getAsString("inavgationstreamnum");
			String sdiNavgationStreamNum = inDto.getAsString("sdinavgationstreamnum");
			String iAdvertisementStreamNum = inDto.getAsString("iadvertisementstreamnum");
			String sdiAdvertisementStreamNum = inDto.getAsString("sdiadvertisementstreamnum");
			String strNetworkComment = inDto.getAsString("strnetworkcomment");
			Integer strNetRegionNum = inDto.getAsInteger("strnetregionnum");
		
			//	String network_code = IDHelper.getnetworkID();
				//pDto.put("network_code", network_code);
			inDto.put("strNetRegionName", strNetRegionName);
			inDto.put("iNavgationStreamNum", iNavgationStreamNum);
			inDto.put("sdiNavgationStreamNum", sdiNavgationStreamNum);
			inDto.put("iAdvertisementStreamNum", iAdvertisementStreamNum);
			inDto.put("sdiAdvertisementStreamNum", sdiAdvertisementStreamNum);
			inDto.put("strNetworkComment", strNetworkComment);
			inDto.put("strNetRegionNum", strNetRegionNum);
//			Date create_date = new Date(new java.util.Date().getTime());
//			inDto.put("ipqam_event_id", inDto.getAsString("network_code"));
//			List networkList = urmDao.queryForList("Network.queryyNetwork",inDto);
//			if(networkList.size()>0){
//				Dto outDto = (Dto)networkList.get(0);
//				int statusDB = outDto.getAsInteger("status");
//				if(statusDB!=status){
//					if(status==0){
//						inDto.put("oper_status", 12);
//					}else{
//						inDto.put("oper_status", 11);
//					}
//					inDto.put("ipqam_event_id", inDto.getAsString("network_code"));
//					inDto.put("type",4);
//					inDto.put("status",1);
//					inDto.put("id", IDHelper.getYunAppID());
//					inDto.put("create_date",create_date);
//					urmDao.insert("Qam.dynamicLoadMDB",inDto);
//				}else{
//					if(status==1){
//						inDto.put("ipqam_event_id", inDto.getAsString("network_code"));
//						inDto.put("type",4);
//						inDto.put("status",1);
//						inDto.put("oper_status", 11);
//						inDto.put("id", IDHelper.getYunAppID());
//						inDto.put("create_date",create_date);
//						urmDao.insert("Qam.dynamicLoadMDB",inDto);
//					}
//				}
//			}
		
		//	inDto.put("status",status);
			urmDao.update("Network.updateNetwork", inDto);	
			
			  SendMessageClient s = new SendMessageClient(Contents.NETWORK,Contents.UPDATE);  
			  s.send();
//			//添加操作记录
//			String busi_id = IDHelper.getCodeID() ;
//			Dto logDto = new BaseDto() ;
//			logDto.put("busi_id",busi_id);
//			logDto.put("busi_code", "B11_32");
//			logDto.put("oper_id", inDto.get("oper_id"));
//			logDto.put("area_id", inDto.getAsString("dept_id"));
//			urmDao.insert("Qam.saveOperLog",logDto);
			
			retDto.put("success", TRUE);
		}		
		return retDto;
	}
	
	private Dto updateMDBForQamRes(Dto pDto){
		Date create_date = new Date(new java.util.Date().getTime());
		pDto.put("type",3);
		pDto.put("id", IDHelper.getYunAppID());
		pDto.put("status",1);
		pDto.put("create_date",create_date);
		urmDao.insert("Qam.dynamicLoadMDB",pDto);
		return null;
	}

	
	//全量同步服务寻址到缓存
	public Dto saveCacheContor(Dto pDto) {
		String operater = pDto.getAsString("account");
		String password = pDto.getAsString("password");
		password = G4Utils.encryptBasedDes(password);
		String operpassword = pDto.getAsString("operpassword");
		Dto outDto = new BaseDto();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");//设置日期格式
		String oper_time = df.format(new Date(new java.util.Date().getTime()));
		if(password==operpassword||operpassword.equals(password)){
			outDto.put("success", TRUE);			
			log.info("操作员"+operater+"在" + oper_time + "进行了阈值全量同步操作!");
					
			pDto.put("status",1);
			pDto.put("id", IDHelper.getYunAppID());
			pDto.put("ipqam_event_id", IDHelper.getYunAppID());
			pDto.put("type", 10);
			Date create_date = new Date(new java.util.Date().getTime());
			pDto.put("create_date", create_date);
			pDto.put("oper_status", "98");
			
			urmDao.insert("Qam.dynamicLoadMDB",pDto);
			
			//添加操作记录
			String busi_id = IDHelper.getCodeID() ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id",busi_id);
			logDto.put("busi_code", "B11_44");
			logDto.put("oper_id", pDto.get("oper_id"));
			logDto.put("area_id", pDto.getAsString("dept_id"));
			urmDao.insert("Qam.saveOperLog",logDto);
			
			outDto.put("success", TRUE);
		}else{
			outDto.put("false", TRUE);
			log.error("操作员"+operater+"在"+oper_time+"进行了阈值全量同步操作，但是密码输入有误！属于非法操作");
		}
		
		return outDto;
	}

}
