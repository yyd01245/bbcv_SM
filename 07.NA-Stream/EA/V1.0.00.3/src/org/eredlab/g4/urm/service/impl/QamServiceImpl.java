package org.eredlab.g4.urm.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.util.G4Utils;
import org.eredlab.g4.urm.service.QamService;
import org.eredlab.g4.urm.util.Contents;
import org.eredlab.g4.urm.util.SendMessageClient;

public class QamServiceImpl extends BaseServiceImpl implements QamService {
	private static Log log = LogFactory.getLog(QamServiceImpl.class);
	public Dto deleteQamItems(Dto pDto) {
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int  i = 0 ; i < arrChecked.length; i++) {
			Dto dto = new BaseDto();
			dto.put("ipqamInfoId", arrChecked[i]);
			urmDao.delete("Qam.deleteResouceItemsByQamID", dto);
			urmDao.delete("Qam.deleteQamItems", dto);
			urmDao.delete("StreamResource.deleteStreamResourceByIPQAMID", dto);
		}
		return null;
	}

	public Dto saveQamItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum =pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
			
		//	ipqamInfoId
		String	ipqamInfoName   = pDto.getAsString("ipqamInfoName");
		String	ipqamIp         = pDto.getAsString("ipqamIp");
		Integer	ipqamPort       = pDto.getAsInteger("ipqamPort");
		Integer	ipqamFrequency  = pDto.getAsInteger("ipqamFrequency");			
		Integer	network_area_id = pDto.getAsInteger("network_area_id");			
	//	String	state           = pDto.getAsString("state");
		String	remark          = pDto.getAsString("remark");
		
		pDto.put("ipqamInfoName", ipqamInfoName);
		pDto.put("ipqamIp", ipqamIp);
		pDto.put("ipqamPort", ipqamPort);
		pDto.put("ipqamFrequency", ipqamFrequency);
		pDto.put("network_area_id", network_area_id);
		pDto.put("remark", remark);
		pDto.put("state", 0);
			urmDao.insert("Qam.saveQamItem", pDto);
			
			
//			 SendMessageClient s = new SendMessageClient(Contents.IPQAMINFO,Contents.INSERT,"","new");  
//			  s.send();
			outDto.put("success", TRUE);
		}		
		return outDto;
	}
	public Dto updateQamItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("success", FALSE);
		}else{
			Integer	ipqamInfoId  = pDto.getAsInteger("ipqaminfoid");	
			
			String	ipqamInfoName   = pDto.getAsString("ipqaminfoname");
			String	ipqamIp         = pDto.getAsString("ipqamip");
			Integer	ipqamPort       = pDto.getAsInteger("ipqamport");
			Integer	ipqamFrequency  = pDto.getAsInteger("ipqamfrequency");			
			Integer	network_area_id = pDto.getAsInteger("network_area_id");			
		//	String	state           = pDto.getAsString("state");
			String	remark          = pDto.getAsString("remark");
			pDto.put("ipqamInfoId", ipqamInfoId);
			pDto.put("ipqamInfoName", ipqamInfoName);
			pDto.put("ipqamIp", ipqamIp);
			pDto.put("ipqamPort", ipqamPort);
			pDto.put("ipqamFrequency", ipqamFrequency);
			pDto.put("network_area_id", network_area_id);
			pDto.put("remark", remark);
			pDto.put("state", 0);
			
			

		
			urmDao.update("Qam.updateQamItem", pDto);
			
			
//			 SendMessageClient s = new SendMessageClient(Contents.IPQAMINFO,Contents.UPDATE,null,String.valueOf(ipqamInfoId));  
//			  s.send();
//			Dto inDto = new BaseDto();
//			inDto.put("ipqam_event_id", pDto.getAsString("ipqam_id"));
//			inDto.put("oper_status",11);
//			updateMDBForQam(inDto);
//			String busi_id = IDHelper.getCodeID();
//			
//			Date create_date = new Date(new java.util.Date().getTime());
//			Dto logDto = new BaseDto() ;
//			logDto.put("busi_id", busi_id);
//			logDto.put("busi_code","B11_12") ;
//			logDto.put("oper_id", pDto.get("oper_id"));
//			logDto.put("area_id", pDto.get("dept_id"));
//			urmDao.insert("Qam.saveOperLog",logDto);
			
			outDto.put("success", TRUE);
		}		
		return outDto;
	}
	
	private Dto updateMDBForQam(Dto pDto){
		Date create_date = new Date(new java.util.Date().getTime());
		pDto.put("type",1);
		pDto.put("id", IDHelper.getYunAppID());
		pDto.put("status",1);
		pDto.put("create_date",create_date);
		urmDao.insert("Qam.dynamicLoadMDB",pDto);
		return null;
	}

	public Dto batchResImport(Dto inDto) {
		Dto outDto = new BaseDto();
		List list = inDto.getAsList("list");
		if(list.size()==0){
			outDto.put("false", TRUE);
		}else{
			List newList = new ArrayList();
			List eventList = new ArrayList();
			Date create_date = new Date(new java.util.Date().getTime());
			for(int i=0;i<list.size();i++){
				Dto pDto =(Dto) list.get(i);
				String ipqamid = IDHelper.getIPQAMID();
				pDto.put("ipqam_id", ipqamid);
				pDto.put("network_code", inDto.getAsInteger("network_code"));
				newList.add(pDto);
				pDto.put("ipqam_event_id",ipqamid);
				pDto.put("oper_status",11);
				pDto.put("type",4);
				pDto.put("id", IDHelper.getYunAppID());
				pDto.put("status",1);
				pDto.put("create_date",create_date);
				urmDao.insert("Qam.dynamicLoadMDB",pDto);			
			}
			
			String busi_id = IDHelper.getCodeID();
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code","B11_11") ;
			logDto.put("oper_id", inDto.get("oper_id"));
			logDto.put("area_id", inDto.get("dept_id"));
			urmDao.insert("Qam.saveOperLog",logDto);
			
			try {
				urmDao.batchInsert("Qam.saveQamItem", newList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			outDto.put("success", TRUE);
		}
		return outDto;
	}

	public Dto saveCacheContor(Dto pDto) {
		String operater = pDto.getAsString("account");
		String password = pDto.getAsString("password");
		password = G4Utils.encryptBasedDes(password);
		String operpassword = pDto.getAsString("operpassword");
		String is_flush_all = pDto.getAsString("is_flush_all");
		Dto outDto = new BaseDto();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");//设置日期格式
		String oper_time = df.format(new Date(new java.util.Date().getTime()));
		if(password==operpassword||operpassword.equals(password)){
			outDto.put("success", TRUE);			
			log.info("操作员"+operater+"在"+oper_time+"进行了资源全量同步操作，操作类型为"+is_flush_all);
			String id = IDHelper.getYunAppID();
			Date create_date = new Date(new java.util.Date().getTime());
			pDto.put("create_date", create_date);
			pDto.put("id", id);
			pDto.put("type", is_flush_all);
			urmDao.insert("Qam.saveCacheContor", pDto);
			
			String busiCode = "B11_"+is_flush_all+"4";
			String busi_id = IDHelper.getCodeID();
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code",busiCode) ;
			logDto.put("oper_id", pDto.get("oper_id"));
			logDto.put("area_id", pDto.get("dept_id"));
			urmDao.insert("Qam.saveOperLog",logDto);
			
			outDto.put("success", TRUE);
		}else{
			outDto.put("false", TRUE);
			log.error("操作员"+operater+"在"+oper_time+"进行了资源全量同步操作，操作类型为"+is_flush_all+",但是密码输入有误！属于非法操作");
		}
		
		return outDto;
	}

	public Dto downloadQamItems(Dto pDto) {
		Dto dto = new BaseDto();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			dto.put("ipqam_id", arrChecked[i]);
			urmDao.update("Qam.updateQamItemsById", dto);
			Dto inDto = new BaseDto();
			inDto.put("ipqam_event_id", arrChecked[i]);
			inDto.put("oper_status", 13);
			updateMDBForQam(inDto);
		}
		
		Date create_date = new Date(new java.util.Date().getTime());
		String busi_id = IDHelper.getCodeID();
		Dto logDto = new BaseDto() ;
		logDto.put("busi_id", busi_id);
		logDto.put("busi_code","B11_12") ;
		logDto.put("oper_id", pDto.get("oper_id"));
		logDto.put("area_id", pDto.get("dept_id"));
		urmDao.insert("Qam.saveOperLog",logDto);
		
		return null;
	}

	public Dto redownloadQamItems(Dto pDto) {
		Dto dto = new BaseDto();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			dto.put("ipqam_id", arrChecked[i]);
			urmDao.update("Qam.updateQamItemsById", dto);
			Dto inDto = new BaseDto();
			inDto.put("ipqam_event_id", arrChecked[i]);
			inDto.put("oper_status", 12);
			updateMDBForQam(inDto);
			
		}
		
		Date create_date = new Date(new java.util.Date().getTime());
		String busi_id = IDHelper.getCodeID();
		Dto logDto = new BaseDto() ;
		logDto.put("busi_id", busi_id);
		logDto.put("busi_code","B11_12") ;
		logDto.put("oper_id", pDto.get("oper_id"));
		logDto.put("area_id", pDto.get("dept_id"));
		urmDao.insert("Qam.saveOperLog",logDto);
		
		return null;
	}

	public Dto uploadQamItems(Dto pDto) {
		Dto dto = new BaseDto();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			dto.put("ipqam_id", arrChecked[i]);
			urmDao.update("Qam.updateQamItemById", dto);
			Dto inDto = new BaseDto();
			inDto.put("ipqam_event_id", arrChecked[i]);
			inDto.put("oper_status", 11);
			updateMDBForQam(inDto);
		}
		
		Date create_date = new Date(new java.util.Date().getTime());
		String busi_id = IDHelper.getCodeID();
		Dto logDto = new BaseDto() ;
		logDto.put("busi_id", busi_id);
		logDto.put("busi_code","B11_12") ;
		logDto.put("oper_id", pDto.get("oper_id"));
		logDto.put("area_id", pDto.get("dept_id"));
		urmDao.insert("Qam.saveOperLog",logDto);
		
		return null;
	}
}
