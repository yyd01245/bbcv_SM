package org.eredlab.g4.cabs.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.cabs.service.DevManagerService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;

public class DevManagerServiceImpl extends BaseServiceImpl implements DevManagerService {

	/**
	 * 
	 * 保存设备
	 */
	public Dto saveDevManagerItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int count = pDto.getAsInteger("count");
		if(count>0){
			outDto.put("success", new Boolean(false));
		}else{
			Integer id =(Integer) crsmDao.queryForObject("DeviceManager.queryMaxIndex");
			if(id==null){
				id =0;
			}
			Calendar cal=Calendar.getInstance(); 
			Date createdate=cal.getTime(); 
			Date updatedate = createdate;
			pDto.put("dev_id", id+1);
			pDto.put("status", 1);
			pDto.put("create_time", createdate);
			crsmDao.insert("DeviceManager.saveDevManagerItem", pDto);
			outDto.put("success", new Boolean(true));
		}		
		return outDto;
	}
	/**
	 * 
	 * 更新设备
	 */
	public Dto updateDevManagerItem(Dto pDto) {
		Dto outDto = new BaseDto();
		int count = pDto.getAsInteger("count");
		if(count>0){
			outDto.put("success", new Boolean(false));
		}else{
			Calendar cal=Calendar.getInstance(); 
			Date updatedate=cal.getTime();
			pDto.put("update_date", updatedate);
			crsmDao.update("DeviceManager.updateDevManagerItem", pDto);
			outDto.put("success", new Boolean(true));
		}
		return outDto;
	}
	public Dto deleteDevManagerItems(Dto inDto) {
		Dto outDto = new BaseDto();
		boolean flag =true;
		String[] arrChecked = inDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			Dto dto = new BaseDto();
			dto.put("dev_id", arrChecked[i]);
			Dto devDto = (Dto)crsmDao.queryForObject("DeviceManager.queryTypeByDevID", dto);
			Integer vncmCount = (Integer)crsmDao.queryForObject("DeviceManager.queryVncmByDevID", devDto);
			int count = vncmCount;
			if(count>0){
				flag=false;				
			}else{
				crsmDao.delete("DeviceManager.deleteDevManagerItems", dto);
			}			
		}		
		if(flag){
			outDto.put("success", new Boolean(true));
		}else{
			outDto.put("success", new Boolean(false));
		}
		return outDto;
	}

}
