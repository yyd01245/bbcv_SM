package org.eredlab.g4.urm.service.impl;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.util.G4Utils;
import org.eredlab.g4.urm.service.StreamResourceService;
import org.eredlab.g4.urm.util.Contents;
import org.eredlab.g4.urm.util.SendMessageClient;

public class StreamResourceServiceImpl extends BaseServiceImpl implements	StreamResourceService {
	
	private static Log log = LogFactory.getLog(StreamResourceServiceImpl.class);
	
	public Dto saveStreamResource(Dto pDto){
		Dto outDto = new BaseDto();
		
		int sum = pDto.getAsInteger("sum");
		boolean flag = pDto.getAsBoolean("flag");
		
		Integer ipqamInfoId = pDto.getAsInteger("ipqamInfoId");
		Integer strNetRegionNum = pDto.getAsInteger("strNetRegionNum");
		String strNav_url = pDto.getAsString("strNav_url");
		String iChannel_id = pDto.getAsString("iChannel_id");
		String strWhether_HD = pDto.getAsString("strWhether_HD");
//		
//		log.info("新增操作，参数：---------------------------");
//		
//		log.info("新增操作，参数：ipqamInfoId="+ipqamInfoId);
//		log.info("新增操作，参数：strNetRegionNum="+strNetRegionNum);
//		log.info("新增操作，参数：strNav_url="+strNav_url);
//		log.info("新增操作，参数：iChannel_id="+iChannel_id);
//		log.info("新增操作，参数：strWhether_HD="+strWhether_HD);
		if(flag){
			if(sum>0){
				log.info("频道号重复。。。。");
				outDto.put("false", TRUE);
			}else{
				pDto.put("ipqamInfoId", ipqamInfoId);
				pDto.put("strNetRegionNum", strNetRegionNum);
				pDto.put("strNav_url", strNav_url);
				pDto.put("iChannel_id", iChannel_id);
				pDto.put("strWhether_HD", strWhether_HD);
				urmDao.insert("StreamResource.saveStreamResource", pDto);	
	    		SendMessageClient s = new SendMessageClient(Contents.STREAMRESOURCE,Contents.INSERT,null,"new");  
				s.send();
				outDto.put("success", TRUE);
			}
		}else{
			log.info("当前IPQAM下的流化路数已经将资源用完，没有多余资源。。。");
			outDto.put("false", TRUE);
		}	
		return outDto;
	}
	
	public Dto deleteStreamResource(Dto inDto) {
		Dto dto = new BaseDto();
		Dto outputDto = new BaseDto();
		String[] arrChecked = inDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			String iStreamID = arrChecked[i];
			dto.put("iStreamID", iStreamID);
			String bindStatus = (String)urmDao.queryForObject("StreamResource.queryStatusByID", dto);
			String bindUsername = (String)urmDao.queryForObject("StreamResource.queryBindUserById", dto);
			if("E".equals(bindStatus)){
				//资源被占用用作点播，不能删除
				outputDto.put("false", new Boolean(true));
			}else{
				//资源被占，不是点播，可删除，通知SM，做进一步处理
				urmDao.delete("StreamResource.deleteStreamResource", dto);
				outputDto.put("success", new Boolean(true));
				SendMessageClient s = new SendMessageClient(Contents.STREAMRESOURCE,Contents.DELETE,bindUsername,iStreamID);  
				s.send();
			}
		}
		return outputDto;
	}

	public Dto updateStreamResource(Dto inDto) {
		Dto retDto = new BaseDto();
		int sum = inDto.getAsInteger("sum");

		if(sum>0){
			retDto.put("success", FALSE);
		}else{
			String iStreamID = inDto.getAsString("istreamid");
			Integer ipqamInfoId = inDto.getAsInteger("ipqaminfoid");
			Integer strNetRegionNum = inDto.getAsInteger("strnetregionnum");
			String strNav_url = inDto.getAsString("strnav_url");
			String iChannel_id = inDto.getAsString("ichannel_id");
			String strWhether_HD = inDto.getAsString("strwhether_hd");
			inDto.put("iStreamID", iStreamID);
			inDto.put("ipqamInfoId", ipqamInfoId);
			inDto.put("strNetRegionNum", strNetRegionNum);		
			inDto.put("strNav_url", strNav_url);		
			inDto.put("iChannel_id", iChannel_id);			
			inDto.put("strWhether_HD", strWhether_HD);
			String bindUsername = (String)urmDao.queryForObject("StreamResource.queryBindUserById", inDto);
			String oldurl = (String)urmDao.queryForObject("StreamResource.queryTypeByID", inDto); 
			urmDao.update("StreamResource.updateStreamResource", inDto);	
			if(!(oldurl==strWhether_HD||strWhether_HD.equals(oldurl))){
				SendMessageClient s = new SendMessageClient(Contents.STREAMRESOURCE,Contents.UPDATE,bindUsername,iStreamID);  
				s.send();
			}
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
