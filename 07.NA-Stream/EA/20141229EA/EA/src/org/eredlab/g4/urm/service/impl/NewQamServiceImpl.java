package org.eredlab.g4.urm.service.impl;

import java.sql.Date;

import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.urm.service.NewQamService;

public class NewQamServiceImpl extends  BaseServiceImpl implements NewQamService {

	public Dto saveEvent(Dto pDto) {
		String strChecked = pDto.getAsString("strChecked");
		String strChecked1 = pDto.getAsString("strChecked1");
		String[] arrChecked = strChecked.split(",");
		String[] arrChecked1 = strChecked1.split(",");
		for (int i = 0; i < arrChecked.length; i++) {
				if(arrChecked1[i]=="1"||"1".equals(arrChecked1[i])){
					Dto inDto = new BaseDto();
					inDto.put("ipqam_event_id",arrChecked[i]);
					inDto.put("oper_status", 11);
					updateMDBForQamRes(inDto);
				}				
		}
		
		String busi_id = IDHelper.getCodeID() ;
		Date create_date = new Date(new java.util.Date().getTime()) ;
		Dto logDto = new BaseDto() ;
		logDto.put("busi_id", busi_id);
		logDto.put("busi_code", "B11_24");
		logDto.put("oper_id", pDto.get("oper_id"));
		logDto.put("area_id", pDto.get("dept_id")) ;
		urmDao.insert("Qam.saveOperLog", logDto) ;
		
		return null;
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
