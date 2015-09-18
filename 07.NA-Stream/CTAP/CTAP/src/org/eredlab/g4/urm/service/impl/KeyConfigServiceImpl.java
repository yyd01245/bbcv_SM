package org.eredlab.g4.urm.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.util.G4Utils;
import org.eredlab.g4.urm.service.KeyConfigService;

public class KeyConfigServiceImpl extends BaseServiceImpl implements	KeyConfigService {
	
	private static Log log = LogFactory.getLog(KeyConfigServiceImpl.class);
	
	public Dto saveKeyConfig(Dto pDto){
		Dto outDto = new BaseDto();
		
	//	int sum = pDto.getAsInteger("sum");
		int sum = 0;
	//	String id = pDto.getAsString("id");
		String key_mean = pDto.getAsString("key_mean");
		String key_name = pDto.getAsString("key_name");
		String key_value = pDto.getAsString("key_value");
		String remark = pDto.getAsString("remark");
		String state = pDto.getAsString("state");		
		
//		log.info("新增键值，参数：");
//		
//		log.info("新增键值，参数key_mean="+key_mean);
//		log.info("新增键值，参数key_name="+key_name);
//		log.info("新增键值，参数key_value="+key_value);
//		log.info("新增键值，参数remark="+remark);		
//		log.info("新增键值，参数state="+state);
		
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
	
			pDto.put("key_mean", key_mean);
			pDto.put("key_name", key_name);
			pDto.put("key_value", key_value);
			pDto.put("remark", remark);
			pDto.put("state", "0");
			
			urmDao.insert("KeyConfig.saveKeyConfig", pDto);	
								
		  
			

			outDto.put("success", TRUE);
		}
		
		return outDto;
	}
	
	public Dto deleteKeyConfigs(Dto inDto) {
		Dto dto = new BaseDto();
		Dto outputDto = new BaseDto();
		String[] arrChecked = inDto.getAsString("strChecked").split(",");

		for (int i = 0; i < arrChecked.length; i++) {
			String id = arrChecked[i];
			dto.put("id", id);
			
			
		
			//删除原表中的数据
			urmDao.delete("KeyConfig.deleteKeyConfigs", dto);
			

		}
		

		
		return outputDto;
	}

	public Dto updateKeyConfig(Dto inDto) {
		Dto retDto = new BaseDto();
		int sum = 0;
		
		if(sum>0){
			retDto.put("success", FALSE);
		}else{
			String id = inDto.getAsString("id");
			String key_mean = inDto.getAsString("key_mean");
			String key_name = inDto.getAsString("key_name");
			String key_value = inDto.getAsString("key_value");
			String remark = inDto.getAsString("remark");
			String state = inDto.getAsString("state");

			
			
//			
//			log.info("更新键值操作，参数：");
//			
//			log.info("新增键值，参数id="+id);
//			log.info("新增键值，参数key_mean="+key_mean);
//			log.info("新增键值，参数key_name="+key_name);
//			log.info("新增键值，参数key_value="+key_value);
//			log.info("新增键值，参数remark="+remark);		
//			log.info("新增键值，参数state="+state);
			
			
			inDto.put("id", id);
			inDto.put("key_mean", key_mean);
			inDto.put("key_name", key_name);
			inDto.put("key_value", key_value);
		
			inDto.put("remark", remark);
			inDto.put("state", state);
			

	


			urmDao.update("KeyConfig.updateKeyConfig", inDto);	
			

			
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
