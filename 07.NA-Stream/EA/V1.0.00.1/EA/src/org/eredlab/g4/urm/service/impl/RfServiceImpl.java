package org.eredlab.g4.urm.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.urm.service.RfService;

public class RfServiceImpl extends BaseServiceImpl implements RfService{

	public Dto deleteRfItems(Dto pDto) {
		Dto dto = new BaseDto();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		String busi_id = IDHelper.getCodeID() ;
		Date create_date = new Date(new java.util.Date().getTime());
		for (int i = 0; i < arrChecked.length; i++) {
			dto.put("rf_code", arrChecked[i]);
			
			//删除频点下的QAM资源
			List li = urmDao.queryForList("QamResource.getIDForPageByKey", dto);
			if(li!=null){
				for(Object o : li){
					Dto delres = (Dto)o;
					delres.put("busi_id", busi_id);
					urmDao.insert("QamResource.saveQamResItemHis", delres);
				}
			}
			urmDao.delete("QamResource.deleteQamResItemsByRfId", dto);
			
			Dto temDto = (Dto)urmDao.queryForObject("qamDy.selectHisRf", dto);
			if(temDto!=null){
				int lines = urmDao.delete("qamDy.deleteTepRf",temDto);
				temDto.put("value", -lines);
				urmDao.update("qamDy.updateQamTemp1",temDto);
				
				temDto.put("res_status", -1);
				urmDao.update("qamDy.updateDyRes",temDto);
				urmDao.update("qamDy.updateStaticRes",temDto);
				temDto.put("oper_type", "DEL");
				urmDao.update("qamDy.handlerPrompt",temDto);
			}
			
			//删除频点
			Dto delDto = (Dto)urmDao.queryForObject("RfResource.getDelByCode", dto);
			delDto.put("busi_id", busi_id) ;
			urmDao.insert("RfResource.saveRfItemHis",delDto);
			urmDao.delete("RfResource.deleteRfItems", dto);	
			
			Dto inDto = new BaseDto();
			inDto.put("ipqam_event_id", arrChecked[i]);
			inDto.put("oper_status", 12);
			updateMDBForQamRes(inDto);
			
		}
		Dto logDto = new BaseDto() ;
		logDto.put("busi_id",busi_id );
		logDto.put("busi_code", "B11_53");
		logDto.put("oper_id", pDto.getAsString("oper_id"));
		logDto.put("area_id", pDto.getAsString("dept_id")) ;
		urmDao.insert("Qam.saveOperLog",logDto);
	
		Dto outDto = new BaseDto() ;
		outDto.put("success", TRUE);
		
		return outDto;
	}

	public Dto saveRfItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
			String rf_code = IDHelper.getRfCodeID();
			pDto.put("rf_code", rf_code);
			urmDao.insert("RfResource.saveRfItem", pDto);
		
			Dto inDto = new BaseDto();
			String ipqam_event_id = IDHelper.getMonitorID();
			inDto.put("ipqam_event_id", rf_code);
			inDto.put("oper_status", 11);
			updateMDBForQamRes(inDto);
			
			String busi_id = IDHelper.getCodeID() ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id",busi_id );
			logDto.put("busi_code", "B11_51");
			logDto.put("oper_id", pDto.getAsString("oper_id"));
			logDto.put("area_id", pDto.getAsString("dept_id")) ;
			urmDao.insert("Qam.saveOperLog",logDto);
			
			outDto.put("success", TRUE);
		}
		
		return outDto;
	}

	public Dto updateRfItem(Dto pDto) {
		Dto outPutDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outPutDto.put("false", TRUE);
		}else{
			urmDao.update("RfResource.updateRfItem", pDto);
			Dto inDto = new BaseDto();
			inDto.put("ipqam_event_id", pDto.getAsString("rf_code"));
			inDto.put("oper_status", 11);
			Date create_date = new Date(new java.util.Date().getTime());
			inDto.put("type",5);
			inDto.put("id", IDHelper.getYunAppID());
			inDto.put("status",1);
			inDto.put("create_date",create_date);
			urmDao.insert("Qam.dynamicLoadMDB",inDto);
			
			String busi_id = IDHelper.getCodeID() ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id",busi_id );
			logDto.put("busi_code", "B11_52");
			logDto.put("oper_id", pDto.getAsString("oper_id"));
			logDto.put("area_id", pDto.getAsString("dept_id")) ;
			urmDao.insert("Qam.saveOperLog",logDto);
			
			outPutDto.put("success", TRUE);
		}
		
		return outPutDto;
	}

	public Dto batchRfImport(Dto inDto) {
		Dto outDto = new BaseDto();
		List list = inDto.getAsList("list");
		if(list.size()==0){
			outDto.put("false", TRUE);
		}else{
			List newList = new ArrayList();
			List eventList = new ArrayList();
			for(int i=0;i<list.size();i++){
				Dto pDto =(Dto) list.get(i);
				String rf_code = IDHelper.getRfCodeID();
				pDto.put("rf_code", rf_code);
				pDto.put("network_code", inDto.getAsInteger("network_code"));
				newList.add(pDto);
				Dto eventDto = new BaseDto();
				Date create_date = new Date(new java.util.Date().getTime());
				eventDto.put("type",5);
				eventDto.put("id", IDHelper.getYunAppID());
				eventDto.put("status",1);
				eventDto.put("create_date",create_date);
				eventDto.put("ipqam_event_id",rf_code);
				eventDto.put("oper_status",11);
				eventList.add(eventDto);
			}
			try {
				urmDao.batchInsert("RfResource.saveRfItem", newList);
				urmDao.batchInsert("Qam.dynamicLoadMDB", eventList);
				
				String busi_id = IDHelper.getCodeID() ;
				Date create_date = new Date(new java.util.Date().getTime());
				Dto logDto = new BaseDto() ;
				logDto.put("busi_id",busi_id );
				logDto.put("busi_code", "B11_51");
				logDto.put("oper_id", inDto.getAsString("oper_id"));
				logDto.put("area_id", inDto.getAsString("dept_id")) ;
				urmDao.insert("Qam.saveOperLog",logDto);
				
				outDto.put("success",TRUE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}		
		return outDto;
	}


	private Dto updateMDBForQamRes(Dto pDto){
		Date create_date = new Date(new java.util.Date().getTime());
		pDto.put("type",5);
		pDto.put("id", IDHelper.getYunAppID());
		pDto.put("status",1);
		pDto.put("create_date",create_date);
		urmDao.insert("Qam.dynamicLoadMDB",pDto);
		return null;
	}

	public Dto deleteQamItemsByrf_id(Dto inDto) {
		String[] arrChecked = inDto.getAsString("strChecked").split(",");
		List list = inDto.getAsList("list");
		for (int i = 0; i < arrChecked.length; i++) {
			inDto.put("rf_code", arrChecked[i]);
			urmDao.delete("RfResource.deleteQamByrf_id", inDto);
		}
		if(list.size()>0){
			for(int i=0;i<list.size();i++){
				Date create_date = new Date(new java.util.Date().getTime());
				Dto dto = (Dto) list.get(i);
				inDto.put("type",5);
				inDto.put("id", IDHelper.getYunAppID());
				inDto.put("status",1);
				inDto.put("create_date",create_date);
				inDto.put("ipqam_event_id", dto.getAsString("ipqam_res_id"));
				inDto.put("oper_status", 12);
				urmDao.insert("Qam.dynamicLoadMDB",inDto);
			}
		}
		return null;
		
	}
}
