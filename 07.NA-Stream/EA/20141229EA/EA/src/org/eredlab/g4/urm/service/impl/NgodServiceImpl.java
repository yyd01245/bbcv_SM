package org.eredlab.g4.urm.service.impl;

import java.sql.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.urm.service.NgodService;

public class NgodServiceImpl extends BaseServiceImpl implements NgodService {
	private static Log log = LogFactory.getLog(NgodServiceImpl.class);
	
	//保存NgodInfo到数据库中
	public Dto saveNgodInfo(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
//			Integer id = (Integer) urmDao.queryForObject("ngod.queryMaxIndexForNgod", pDto);
//			if(id==null){
//				id=0;
//			}
			String id = IDHelper.getCodeID() ;
			pDto.put("ngod_id", id);
			urmDao.insert("ngod.saveNgodInfo", pDto);	
			pDto.put("ipqam_event_id", id);
			pDto.put("oper_status", 11);
			updateMDBForQamRes(pDto);
			
			String busi_id = IDHelper.getCodeID() ;
			Date create_date = new Date(new java.util.Date().getTime());
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code", "B11_201");
			logDto.put("oper_id",pDto.get("oper_id"));
			logDto.put("area_id",pDto.get("dept_id") ) ;
			urmDao.insert("Qam.saveOperLog",logDto) ;
			
			outDto.put("success", TRUE);
		}
		return outDto;
	}

	/**
	 * 更新NGOD信息
	 */
	public Dto updateNgodInfo(Dto inDto) {
		Dto retDto = new BaseDto();
		
		if(inDto.getAsInteger("sum")>0){
			retDto.put("success", FALSE);
			return retDto ;
		}
		inDto.put("ipqam_event_id", inDto.getAsString("ngod_id"));
		int status = inDto.getAsInteger("status");

		String busi_id = IDHelper.getCodeID() ;
		if(status==0){
			inDto.put("oper_status", 12);
			   //删除D6R6信息
			List d6r6List = urmDao.queryForList("ngod.getD6R6Del", inDto);
			if(d6r6List!=null){
				for(Object o : d6r6List){
					Dto d6r6Dto = (Dto)o;
					d6r6Dto.put("busi_id",busi_id);
					urmDao.insert("ngod.savaD6R6His",d6r6Dto);
				}
			}
			urmDao.delete("ngod.delD6R6", inDto);
		}else{
			inDto.put("oper_status", 11);
		}				
		updateMDBForQamRes(inDto);
		inDto.put("status", status);
		urmDao.update("ngod.updateNgodInfo", inDto);
		urmDao.update("ngod.updateRfStatus",inDto);
		
		Dto logDto = new BaseDto() ;
		logDto.put("busi_id", busi_id);
		logDto.put("busi_code", "B11_202");
		logDto.put("oper_id",inDto.get("oper_id"));
		logDto.put("area_id",inDto.get("dept_id")) ;
		urmDao.insert("Qam.saveOperLog",logDto) ;
		
		retDto.put("success", TRUE);
		return retDto;
	}
	/**
	 * 删除NGOD信息
	 */
	
	public Dto deleteNgodInfo(Dto inDto) {
		Dto outputDto = new BaseDto();
		String[] arrChecked = inDto.getAsString("strChecked").split(",");
		String busi_id = IDHelper.getCodeID() ;
		Date create_date = new Date(new java.util.Date().getTime());
		for (int i = 0; i < arrChecked.length; i++) {
			String ngod_id = arrChecked[i];
			inDto.put("ngod_id", ngod_id);
			
		    //删除D6R6信息
			List d6r6List = urmDao.queryForList("ngod.getD6R6Del", inDto);
			if(d6r6List!=null){
				for(Object o : d6r6List){
					Dto d6r6Dto = (Dto)o;
					d6r6Dto.put("busi_id",busi_id);
					urmDao.insert("ngod.savaD6R6His",d6r6Dto);
				}
			}
			urmDao.delete("ngod.delD6R6", inDto);
			
			//删除NGOD频点
			List rfList = urmDao.queryForList("ngod.getDelNgodRf", inDto) ;
			if(rfList!=null){
				for(Object o : rfList){
					Dto rfDto = (Dto)o;
					rfDto.put("busi_id",busi_id);
					urmDao.insert("ngod.saveNgodRfInfoHis",rfDto);
				}
			}
			urmDao.delete("ngod.deleteRfById",inDto);
			
			Dto delDto = (Dto)urmDao.queryForObject("ngod.getDelNgod",inDto);
			delDto.put("busi_id",busi_id );
			urmDao.insert("ngod.saveNgodInfoHis",delDto );
			
			urmDao.delete("ngod.deleteNgodInfo",inDto);
			inDto.put("ipqam_event_id", ngod_id);
			inDto.put("oper_status", 12);
			updateMDBForQamRes(inDto);			
			outputDto.put("success", TRUE);
		}
		
		Dto logDto = new BaseDto() ;
		logDto.put("busi_id", busi_id);
		logDto.put("busi_code", "B11_203");
		logDto.put("oper_id",inDto.get("oper_id"));
		logDto.put("area_id",inDto.get("dept_id") ) ;
		urmDao.insert("Qam.saveOperLog",logDto) ;
		
		
		return outputDto;
	}
	
	//保存NgodInfo到数据库中
	public Dto saveNgodRfInfo(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
//			Integer id = (Integer) urmDao.queryForObject("ngod.queryMaxIndexForNgodRf", pDto);
//			if(id==null){
//				id=0;
//			}
			String id = IDHelper.getCodeID() ;
			pDto.put("info_id", id);
			urmDao.insert("ngod.saveNgodRfInfo", pDto);	
			pDto.put("ipqam_event_id", id);
			pDto.put("oper_status", 11);
			updateMDBForQamRes1(pDto);
			
			String busi_id = IDHelper.getCodeID() ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code", "B11_211");
			logDto.put("oper_id",pDto.get("oper_id"));
			logDto.put("area_id",pDto.get("dept_id") ) ;
			urmDao.insert("Qam.saveOperLog",logDto) ;
			
			outDto.put("success", TRUE);
		}
		return outDto;
	}

	/**
	 * 更新NGOD频点信息
	 */
	public Dto updateNgodRfInfo(Dto inDto) {
		Dto retDto = new BaseDto();
		int sum = inDto.getAsInteger("sum");
		if(sum>0){
			retDto.put("false", TRUE);
			return retDto ;
		}
		inDto.put("ipqam_event_id", inDto.getAsString("info_id"));
		int status = inDto.getAsInteger("status");
		String busi_id = IDHelper.getCodeID() ;
		if(status==0){
			inDto.put("oper_status", 12);
			 //删除D6R6信息
			List d6r6List = urmDao.queryForList("ngod.getD6R6Del", inDto);
			if(d6r6List!=null){
				for(Object o : d6r6List){
					Dto d6r6Dto = (Dto)o;
					d6r6Dto.put("busi_id",busi_id);
					urmDao.insert("ngod.savaD6R6His",d6r6Dto);
				}
			}
			urmDao.delete("ngod.delD6R6", inDto);
		}else{
			inDto.put("oper_status", 11);
		}
		urmDao.update("ngod.updateNgodRfInfo", inDto);
		updateMDBForQamRes1(inDto);
		
		Dto logDto = new BaseDto() ;
		logDto.put("busi_id", busi_id);
		logDto.put("busi_code", "B11_212");
		logDto.put("oper_id",inDto.get("oper_id"));
		logDto.put("area_id",inDto.get("dept_id") ) ;
		urmDao.insert("Qam.saveOperLog",logDto) ;
		
		retDto.put("success", TRUE);
		return retDto;

	}
	/**
	 * 删除NGOD频点信息
	 */
	
	public Dto deleteNgodRfInfo(Dto inDto) {
		Dto outputDto = new BaseDto();
		String[] arrChecked = inDto.getAsString("strChecked").split(",");
		String busi_id = IDHelper.getCodeID() ;
		Date create_date = new Date(new java.util.Date().getTime());
		for (int i = 0; i < arrChecked.length; i++) {
			String info_id = arrChecked[i];
			inDto.put("info_id", info_id);
			
			 //删除D6R6信息
			List d6r6List = urmDao.queryForList("ngod.getD6R6Del", inDto);
			if(d6r6List!=null){
				for(Object o : d6r6List){
					Dto d6r6Dto = (Dto)o;
					d6r6Dto.put("busi_id",busi_id);
					urmDao.insert("ngod.savaD6R6His",d6r6Dto);
				}
			}
			urmDao.delete("ngod.delD6R6", inDto);
			
			Dto delDto = (Dto)urmDao.queryForObject("ngod.getDelNgodRfById",inDto);
			delDto.put("busi_id",busi_id );
			urmDao.insert("ngod.saveNgodRfInfoHis",delDto );
			
			inDto.put("ipqam_event_id", info_id);
			inDto.put("oper_status", 12);
		
			urmDao.delete("ngod.deleteNgodRfInfo",inDto);
			updateMDBForQamRes1(inDto);
			outputDto.put("success", TRUE);
		}
		
		Dto logDto = new BaseDto() ;
		logDto.put("busi_id", busi_id);
		logDto.put("busi_code", "B11_213");
		logDto.put("oper_id",inDto.get("oper_id"));
		logDto.put("area_id",inDto.get("dept_id") ) ;
		urmDao.insert("Qam.saveOperLog",logDto) ;
		
		return outputDto;
	}
	
	/**
	 * 添加缓存事件
	 * @param pDto
	 * @return
	 */
	private Dto updateMDBForQamRes(Dto pDto){
		Date create_date = new Date(new java.util.Date().getTime());
		pDto.put("type",20);
		pDto.put("id", IDHelper.getYunAppID());
		pDto.put("status",1);
		pDto.put("create_date",create_date);
		urmDao.insert("Qam.dynamicLoadMDB",pDto);
		return null;
	}
	
	private Dto updateMDBForQamRes1(Dto pDto){
		Date create_date = new Date(new java.util.Date().getTime());
		pDto.put("type",21);
		pDto.put("id", IDHelper.getYunAppID());
		pDto.put("status",1);
		pDto.put("create_date",create_date);
		urmDao.insert("Qam.dynamicLoadMDB",pDto);
		return null;
	}

	public Dto saveQAMDevice(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
			String id = IDHelper.getCodeID() ;
			pDto.put("conf_id", id);
			urmDao.insert("ngod.saveQamDevice", pDto);	
			pDto.put("ipqam_event_id", id);
			pDto.put("oper_status", 11);
			updateMDBForQamDec(pDto);
			
			String busi_id = IDHelper.getCodeID() ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code", "B11_71");
			logDto.put("oper_id",pDto.get("oper_id"));
			logDto.put("area_id",pDto.get("dept_id") ) ;
			urmDao.insert("Qam.saveOperLog",logDto) ;
			
			outDto.put("success", TRUE);
		}
		return outDto;
	}

	public Dto updateQAMDevice(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
			String status = "11" ;
			if(pDto.getAsInteger("status") == 0)
				status = "12" ;
			String id = pDto.getAsString("conf_id") ;
			urmDao.update("ngod.updateQamDevice", pDto);	
			pDto.put("ipqam_event_id", id);
			pDto.put("oper_status", status);
			updateMDBForQamDec(pDto);
			
			String busi_id = IDHelper.getCodeID() ;
			Dto logDto = new BaseDto() ;
			logDto.put("busi_id", busi_id);
			logDto.put("busi_code", "B11_72");
			logDto.put("oper_id",pDto.get("oper_id"));
			logDto.put("area_id",pDto.get("dept_id") ) ;
			urmDao.insert("Qam.saveOperLog",logDto) ;
			
			outDto.put("success", TRUE);
		}
		return outDto;
	}
	
	private Dto updateMDBForQamDec(Dto pDto){
		Date create_date = new Date(new java.util.Date().getTime());
		pDto.put("type",7);
		pDto.put("id", IDHelper.getYunAppID());
		pDto.put("status",1);
		pDto.put("create_date",create_date);
		urmDao.insert("Qam.dynamicLoadMDB",pDto);
		return null;
	}
	
}
