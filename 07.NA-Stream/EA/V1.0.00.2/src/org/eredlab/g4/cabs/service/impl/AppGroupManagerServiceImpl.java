package org.eredlab.g4.cabs.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.cabs.service.AppGroupManagerService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;


import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

public class AppGroupManagerServiceImpl extends BaseServiceImpl implements AppGroupManagerService{
	private static Log log = LogFactory.getLog(AppGroupManagerServiceImpl.class);
	PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	
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
	

}
