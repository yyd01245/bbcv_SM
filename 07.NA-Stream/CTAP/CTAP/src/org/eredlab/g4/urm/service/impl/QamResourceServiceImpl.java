package org.eredlab.g4.urm.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.urm.service.QamResourceService;
import org.eredlab.g4.urm.util.Contents;
import org.eredlab.g4.urm.util.SendMessageClient;

public class QamResourceServiceImpl extends BaseServiceImpl implements QamResourceService{

	public Dto deleteQamResItems(Dto pDto) {
		Dto dto = new BaseDto();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
	//	String busi_id = IDHelper.getCodeID() ;
	//	Date create_date = new Date(new java.util.Date().getTime()) ;
		for (int i = 0; i < arrChecked.length; i++) {
			dto.put("ipqamresid", arrChecked[i]);
			
//			urmDao.delete("qamDy.deleteStaticRes",dto);
//			urmDao.delete("qamDy.delteDyRes",dto);
//			//删除QAM资源
//			Dto delDto = (Dto)urmDao.queryForObject("QamResource.getQamResByKey",dto) ;
//			
//			delDto.put("busi_id", busi_id);
//			urmDao.insert("QamResource.saveQamResItemHis",delDto) ;
			urmDao.delete("QamResource.deleteQamResItems", dto);
			
			
			  SendMessageClient s = new SendMessageClient(Contents.IPQAMRESOURCE,Contents.DELETE);  
			  s.send();
//			//添加删除事件
//			Dto inDto = new BaseDto();
//			inDto.put("ipqam_event_id", arrChecked[i]);
//			inDto.put("oper_status", 12);
//			updateMDBForQamRes(inDto);
		}
		
//		//添加 删除操作记录
//		Dto logDto = new BaseDto() ;
//		logDto.put("busi_id", busi_id);
//		logDto.put("busi_code", "B11_23");
//		logDto.put("oper_id", pDto.get("oper_id"));
//		logDto.put("area_id", pDto.get("dept_id")) ;
//		urmDao.insert("Qam.saveOperLog", logDto) ;
		
		return null;
	}

	public Dto saveQamResItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
		//	String ipqam_res_id = IDHelper.getIPQAMID();
			
			
		// Integer  ipqamResId =pDto.getAsInteger("ipqamResId");
		 String	  ipqamResPort=pDto.getAsString("ipqamResPort");
		 Integer  ipqamResPid=pDto.getAsInteger("ipqamResPid");
		 Integer  ipqamInfoId=pDto.getAsInteger("ipqamInfoId");
		 
	
			
			
		 String	  state=pDto.getAsString("state");
		 String	  remark=pDto.getAsString("remark");
		 
	 	pDto.put("ipqamResPort", ipqamResPort);
	 	pDto.put("ipqamResPid", ipqamResPid);
	 	pDto.put("ipqamInfoId", ipqamInfoId);
	 	pDto.put("state", "0"); 
	 	pDto.put("remark", remark);

			urmDao.insert("QamResource.saveQamResItem", pDto);
			
			
			  SendMessageClient s = new SendMessageClient(Contents.IPQAMRESOURCE,Contents.INSERT);  
			  s.send();
//			Dto inDto = new BaseDto();
//			inDto.put("ipqam_event_id", ipqam_res_id);
//			inDto.put("oper_status", 11);
//			updateMDBForQamRes(inDto);
//			
//			String busi_id = IDHelper.getCodeID() ;
//			Date create_date = new Date(new java.util.Date().getTime()) ;
//			Dto logDto = new BaseDto() ;
//			logDto.put("busi_id", busi_id);
//			logDto.put("busi_code", "B11_21");
//			logDto.put("oper_id", pDto.get("oper_id"));
//			logDto.put("area_id", pDto.get("dept_id")) ;
//			urmDao.insert("Qam.saveOperLog", logDto) ;
			
			outDto.put("success", TRUE);
		}
		return outDto;
	}

	public Dto updateQamResItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("success", FALSE);
		}else{
			
			
			 Integer  ipqamResId =pDto.getAsInteger("ipqamresid");
			 String	  ipqamResPort=pDto.getAsString("ipqamresport");
			 Integer  ipqamResPid=pDto.getAsInteger("ipqamrespid");
			 Integer  ipqamInfoId=pDto.getAsInteger("ipqaminfoid");
			 String	  state=pDto.getAsString("state");
			 String	  remark=pDto.getAsString("remark");
			 
			 
			pDto.put("ipqamResId", ipqamResId);
		 	pDto.put("ipqamResPort", ipqamResPort);
		 	pDto.put("ipqamResPid", ipqamResPid);
		 	pDto.put("ipqamInfoId", ipqamInfoId);
		 	pDto.put("state", "0"); 
		 	pDto.put("remark", remark);
			

//			if(res_status==1){
				
				//删除QAM资源
		//		Dto delDto = (Dto)urmDao.queryForObject("QamResource.getQamResByKey",pDto) ;
			//	String rfCode = delDto.getAsString("rf_code");
				
				urmDao.update("QamResource.updateQamResItem", pDto);
				
				
				  SendMessageClient s = new SendMessageClient(Contents.IPQAMRESOURCE,Contents.UPDATE);  
				  s.send();
//				if(rf_code!=null && !rfCode.equals(rf_code)){
//					delDto.put("busi_id", busi_id);
//					urmDao.insert("QamResource.saveQamResItemHis",delDto) ;
//					urmDao.delete("QamResource.deleteQamResItems", pDto);
//				
//					Dto inDto = new BaseDto();
//					inDto.put("ipqam_event_id",ipqam_res_id);
//				
//					inDto.put("oper_status", 12);
//					updateMDBForQamRes(inDto);
//					ipqam_res_id = IDHelper.getIPQAMID();
//					pDto.put("ipqam_res_id", ipqam_res_id);
//					urmDao.insert("QamResource.saveQamResItem", pDto);
//				}
//				inDto1.put("oper_status", 11);
//			}else{
//				urmDao.update("QamResource.updateQamResItem", pDto);
//				inDto1.put("oper_status", 12);
//			}		
//			inDto1.put("ipqam_event_id", ipqam_res_id);
//			updateMDBForQamRes(inDto1);		
//			
//			Dto logDto = new BaseDto() ;
//			logDto.put("busi_id", busi_id);
//			logDto.put("busi_code", "B11_22");
//			logDto.put("oper_id", pDto.get("oper_id"));
//			logDto.put("area_id", pDto.get("dept_id")) ;
//			urmDao.insert("Qam.saveOperLog", logDto) ;
			
			outDto.put("success",TRUE);
		}		
		return outDto;
	}

	public Dto batchResImport(Dto inDto) {
		Dto outDto = new BaseDto();
		List list = inDto.getAsList("list");
		if(list.size()==0){
			outDto.put("false", TRUE);
		}else{
			List newList = new ArrayList();
			List eventList = new ArrayList();
			for(int i=0;i<list.size();i++){
				Dto pDto =(Dto) list.get(i);
				int rf_id = pDto.getAsInteger("rf_id");
				String ipqam_res_id = IDHelper.getIPQAMID();
				pDto.put("ipqam_res_id", ipqam_res_id);
				pDto.put("res_status", 1);
				pDto.put("ipqam_id", inDto.getAsInteger("ipqam_id"));
				
				Dto eventDto = new BaseDto();
				Date create_date = new Date(new java.util.Date().getTime());
				eventDto.put("type",2);
				eventDto.put("id", IDHelper.getYunAppID());
				eventDto.put("status",1);
				eventDto.put("create_date",create_date);
				eventDto.put("ipqam_event_id",ipqam_res_id);
				eventDto.put("oper_status",11);
				newList.add(pDto);
				eventList.add(eventDto);
			}
			
			String busi_id = IDHelper.getCodeID() ;
			Date create_date = new Date(new java.util.Date().getTime()) ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code", "B11_21");
			logDto.put("oper_id", inDto.get("oper_id"));
			logDto.put("area_id", inDto.get("dept_id")) ;
			urmDao.insert("Qam.saveOperLog", logDto) ;
			
			try {
				urmDao.batchInsert("QamResource.saveQamResItem", newList);
				urmDao.batchInsert("Qam.dynamicLoadMDB", eventList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			outDto.put("success", TRUE);
		}		
		return outDto;
	}


	private Dto updateMDBForQamRes(Dto pDto){
		Date create_date = new Date(new java.util.Date().getTime());
		pDto.put("type",2);
		pDto.put("id", IDHelper.getYunAppID());
		pDto.put("status",1);
		pDto.put("create_date",create_date);
		urmDao.insert("Qam.dynamicLoadMDB",pDto);
		return null;
	}
}
