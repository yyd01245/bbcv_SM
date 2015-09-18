package org.eredlab.g4.cabs.service.impl;

import java.util.Date;
import java.util.Calendar;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.cabs.service.PackageVersionService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;

public class PackageVersionServiceImpl extends BaseServiceImpl implements
PackageVersionService {

	public Dto insertPackage(Dto pDto,String versionaddr) {
		Dto outDto = new BaseDto();
		int count = pDto.getAsInteger("count");
		if(count>0){
			outDto.put("success", new Boolean(false));
		}else{
			Integer recordid = (Integer)crsmDao.queryForObject("PackageVersion.queryVersionMaxIndex");
			if(recordid==null){
				recordid=0;
			}
			Calendar cal=Calendar.getInstance(); 
			Date createdate=cal.getTime(); 
			pDto.put("version_id", recordid+1);
			pDto.put("targetaddr", versionaddr);
			pDto.put("create_date", createdate);
			pDto.put("jar_size", 1024);
			pDto.put("status", 1);
			crsmDao.insert("PackageVersion.savePackageVersionItem", pDto);
			outDto.put("success", new Boolean(true));
		}		
		return outDto;
	}

	public Dto insertPackage(Dto pDto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Dto deletePackageVersionItems(Dto pDto) {
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		Dto outDto= new BaseDto();
		boolean flag = true;
		for (int i = 0; i < arrChecked.length; i++) {
			Dto dto= new BaseDto();
			dto.put("version_id", arrChecked[i]);
			Dto inDto = (Dto)crsmDao.queryForObject("PackageVersion.getJARID", dto);
			if(Integer.parseInt(inDto.getAsString("total"))==0){
				crsmDao.delete("PackageVersion.deletePackageVersionItems", dto);
				outDto.put("success", true);
			}else{
				outDto.put("success", false);
				flag=false;
			}
			if(flag){
				outDto.put("ret_msg", "删除成功!");
			}else{
				outDto.put("ret_msg", "删除失败，升级包正在被使用!");
			}
		}
		return outDto;
	}

	public Dto updatePackage(Dto pDto) {
		Dto outDto = new BaseDto();
		int count = pDto.getAsInteger("count");
		if(count>0){
			outDto.put("success", new Boolean(false));
		}else{
			Date date = new Date(System.currentTimeMillis());
			Calendar cal=Calendar.getInstance(); 
			Date updatedate=cal.getTime(); 
			pDto.put("update_date", updatedate);
			crsmDao.update("PackageVersion.updatePackageItem", pDto);
			outDto.put("success", new Boolean(true));
		}
		return outDto;
	}

	//修改升级包后要修改yynm_term_version_rel
	public Dto updatePackageTermRel(Dto pDto) {
		// TODO Auto-generated method stub
		Calendar cal=Calendar.getInstance(); 
		Date updatedate=cal.getTime(); 
		pDto.put("update_date", updatedate);
		pDto.put("status", "3");
		crsmDao.update("PackageVersion.updateTermVersionRel", pDto);
		return null;
	}

	public Dto updatePackageTerm(Dto inDto) {
		// TODO Auto-generated method stub
		crsmDao.update("PackageVersion.updateTermInfoByKey", inDto);
		return null;
	}

	
}
