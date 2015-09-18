package org.eredlab.g4.urm.service.impl;

import java.sql.Date;
import java.util.Random;

import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.urm.service.AppAccessService;

public class AppAccessServiceImpl extends BaseServiceImpl implements AppAccessService{

	public Dto deleteAppAccessItems(Dto pDto) {
		Dto dto = new BaseDto();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			dto.put("login_id", arrChecked[i]);		
			Date create_date = new Date(new java.util.Date().getTime());
			String busi_id = IDHelper.getCodeID() ;
			
			Dto delDto = (Dto)urmDao.queryForObject("AppAccess.getAppAccessByKey", dto);
			delDto.put("busi_id", busi_id);
			urmDao.insert("AppAccess.saveAppAccessItemHis",delDto);
			urmDao.delete("AppAccess.deleteAppAccessItems", dto);
			
			dto.put("type",6);
			dto.put("ipqam_event_id", arrChecked[i]);
			dto.put("oper_status", 12);
			dto.put("id", IDHelper.getYunAppID());
			dto.put("status",1);
			dto.put("create_date",create_date);
			urmDao.insert("Qam.dynamicLoadMDB",dto);
			
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code", "B11_63") ;
			logDto.put("oper_id",pDto.get("oper_id"));
			logDto.put("area_id", pDto.get("dept_id"));
			urmDao.insert("Qam.saveOperLog",logDto) ;
		}
		return null;
	}

	public Dto saveAppAccessItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
			String loginid = IDHelper.getAppAccessID();
			//获取十六位随机字符码
			String license = this.getRandomString(16);
			pDto.put("login_id", loginid);
			pDto.put("app_id", loginid);
			pDto.put("license", license);
			pDto.put("ipqam_event_id", loginid);
			pDto.put("oper_status", 11);		
			urmDao.insert("AppAccess.saveAppAccessItem", pDto);
			updateMDBForQamRes(pDto);
			
			String busi_id = IDHelper.getCodeID() ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code", "B11_61") ;
			logDto.put("oper_id",pDto.get("oper_id"));
			logDto.put("area_id", pDto.get("dept_id"));
			urmDao.insert("Qam.saveOperLog",logDto) ;
			outDto.put("success", TRUE);
		}
		return outDto;
	}

	public Dto updateAppAccessItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
			urmDao.update("AppAccess.updateAppAccessItem", pDto);
			pDto.put("ipqam_event_id", pDto.getAsString("login_id"));
			pDto.put("oper_status", 11);
			updateMDBForQamRes(pDto);
			
			String busi_id = IDHelper.getCodeID() ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code", "B11_62") ;
			logDto.put("oper_id",pDto.get("oper_id"));
			logDto.put("area_id", pDto.get("dept_id"));
			urmDao.insert("Qam.saveOperLog",logDto) ;
			
			outDto.put("success", TRUE);
		}
		
		return outDto;
	}
	
	/**
	 * 获取十六位随机字符码
	 * @param length
	 * @return
	 */
	public String getRandomString(int length) { 
	    StringBuffer buffer = new StringBuffer("ABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
	    StringBuffer sb = new StringBuffer(); 
	    Random r = new Random(); 
	    int range = buffer.length(); 
	    for (int i = 0; i < length; i ++) { 
	        sb.append(buffer.charAt(r.nextInt(range))); 
	    } 
	    return sb.toString(); 
	}
	private Dto updateMDBForQamRes(Dto pDto){
		Date create_date = new Date(new java.util.Date().getTime());
		pDto.put("type",6);
		pDto.put("id", IDHelper.getYunAppID());
		pDto.put("status",1);
		pDto.put("create_date",create_date);
		urmDao.insert("Qam.dynamicLoadMDB",pDto);
		return null;
	}
	
}
