package org.eredlab.g4.urm.service.impl;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.arm.util.idgenerator.IDHelper;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.urm.service.QamDyService;

import prod.nebula.eabg.util.StringUtil;

public class QamDyServiceImpl extends BaseServiceImpl implements QamDyService {
	private static Log log = LogFactory.getLog(QamDyServiceImpl.class);

	public Dto saveQAMDevice(Dto pDto) {
		Dto outDto = new BaseDto();
		int sum = pDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("false", TRUE);
		}else{
			String id = IDHelper.getCodeID() ;
			pDto.put("conf_id", id);
			urmDao.insert("qamDy.saveQamDevice", pDto);	
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
			urmDao.update("qamDy.updateQamDevice", pDto);	
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

	public Dto deleteDyQam(Dto inDto) {
		String checked = inDto.getAsString("checked");
		String[] arr = checked.split(",");
		Dto dto = new BaseDto() ;
		for(int i = 0 ; i < arr.length ; i ++ ){
			dto.put("ipqam_id", arr[i]);
			urmDao.delete("qamDy.deleteDyQam1",dto);
			urmDao.delete("qamDy.deleteDyRf1",dto);
			urmDao.delete("qamDy.deleteBizip",dto);
		}
		Dto outDto = new BaseDto() ;
		outDto.put("success", TRUE);
		return outDto ;
	}


	public Dto updateRfStatus(Dto inDto) {
		urmDao.insert("qamDy.updateRfStatus",inDto);
		Dto outDto = new BaseDto() ;
		outDto.put("success",TRUE);
		return outDto;
	}

	public Dto saveMidRf(Dto inDto) {
		
		Dto qamCount = (Dto)urmDao.queryForObject("qamDy.getMidQamCount",inDto);
		Dto outDto = new BaseDto() ;
		String ipqam_id = null ;
		if(qamCount.getAsInteger("count")==0){
			List list = urmDao.queryForList("qamDy.getDyQam",inDto);
			if(list!=null && list.size()>0){
				Dto dto = (Dto)list.get(0);
				ipqam_id = IDHelper.getIPQAMID() ;
				dto.put("base_ipqam_id", dto.get("ipqam_id"));
				dto.put("ipqam_id", ipqam_id);
				urmDao.insert("qamDy.insertQamMid",dto);
			}
		}else{
			ipqam_id = qamCount.getAsString("ipqam_id");
		}
		
		urmDao.update("qamDy.updateQamTemp",inDto);
		String rf_code ;
		
		inDto.put("base_ipqam_id", inDto.get("ipqam_id"));
		inDto.put("ipqam_id", ipqam_id);
		inDto.put("base_rf_code", inDto.get("rf_code")) ;
		urmDao.update("qamDy.updateQamMid",inDto);
		
		Dto firmDto = (Dto)urmDao.queryForObject("qamDy.queryQamDec",inDto);
		if(firmDto!=null)		inDto.put("r6_firm", firmDto.get("r6_firm"));
		
		
		Dto dtoRf = (Dto)urmDao.queryForObject("qamDy.queryRfCount",inDto);
		if(dtoRf.getAsInteger("count") == 0){
		
			rf_code = IDHelper.getRfCodeID() ;
			inDto.put("rf_code", rf_code);
			
			//处理频点
			urmDao.insert("qamDy.insertRfMid",inDto) ;
			
		}else{
			
			rf_code = dtoRf.getAsString("rf_code");
			inDto.put("rf_code",rf_code );
			
			urmDao.update("qamDy.updateMidRf",inDto);
		}
		
		outDto.put("device_firm_id", inDto.get("device_firm_id"));
		outDto.put("device_firm", inDto.get("device_firm"));
		outDto.put("rf_code", rf_code);
		outDto.put("success", true);
		return outDto;
	}

	public Dto saveNormalData(Dto inDto) {
		Dto firmDto = (Dto) urmDao.queryForObject("qamDy.queryQamDec", inDto);
		Dto resDto = new BaseDto() ;
		List list = urmDao.queryForList("qamDy.getMidRf", inDto) ;
		if(list !=null && list.size()>0){
			Dto dto = (Dto)list.get(0);
			List li = urmDao.queryForList("qamDy.getMidQam", dto) ;
			if(li!=null && li.size()>0){
				Dto qamD = (Dto)li.get(0) ;
				
				String min_rf = dto.getAsString("rf_code");
				resDto.put("rf_code", dto.get("base_rf_code"));
				resDto.put("status", 1);
				
				String mid_qamid = qamD.getAsString("ipqam_id");
				
				List networkList = urmDao.queryForList("Qam.getNerworkById", qamD);
				if(networkList.size()>0){
					Dto leveDto = (Dto) networkList.get(0);
					String area_id = leveDto.getAsString("area_id");
					String region_id = leveDto.getAsString("sg_id");
					String ipqam_level;
					if(area_id=="99999"||"99999".equals(area_id)){
						ipqam_level="3";
					}else if(region_id ==""||"".equals(region_id)){
						ipqam_level = "2";
					}else{
						ipqam_level="1";
					}
					qamD.put("ipqam_level", ipqam_level);
				}
				
				String ipqam_id = qamD.getAsString("normal_ipqam_id");
				Integer count = (Integer)urmDao.queryForObject("qamDy.getQamCount",qamD);
				if(ipqam_id==null || count==0){
					ipqam_id = IDHelper.getIPQAMID() ;		
					qamD.put("ipqam_id", ipqam_id) ;
					urmDao.insert("qamDy.saveDyQam",qamD);	
				}else{
					qamD.put("ipqam_id", ipqam_id);
					urmDao.update("Qam.updateQamItem",qamD);
				}
				
				dto.put("admin_ip", inDto.get("admin_ip"));
				String rf_id = IDHelper.getRfCodeID() ;
			
				Dto rfd = (Dto)urmDao.queryForObject("qamDy.rfCount",dto);
				if(rfd.getAsInteger("count")>0){
					rf_id =  rfd.getAsString("rf_code");
					dto.put("ipqam_id", mid_qamid) ;
					dto.put("normal_ipqam_id",ipqam_id ) ;
					dto.put("normal_rf_code", rf_id);
					urmDao.update("qamDy.updateMidRf",dto);
					urmDao.update("qamDy.updateQamMid",dto);
					dto.put("rf_code",rf_id);
					urmDao.update("RfResource.updateRfItem",dto);
				}else{
					dto.put("ipqam_id", mid_qamid) ;
					dto.put("normal_ipqam_id",ipqam_id ) ;
					dto.put("normal_rf_code", rf_id);
					urmDao.update("qamDy.updateMidRf",dto);
					urmDao.update("qamDy.updateQamMid",dto);
					dto.put("rf_code", rf_id);
					urmDao.insert("qamDy.saveDyRf",dto);
				}				
				
				String baseRf = dto.getAsString("base_rf_code");
				resDto.put("res_status", 1);
				resDto.put("oper_type", "ADD");
				urmDao.update("qamDy.handlerPrompt",resDto);
				
				String pid = firmDto.getAsString("pid_rule") ;
				String res_type = "MP2T_UNICAST";
				Integer minPort = inDto.getAsInteger("min_port");
				
				pid = pid.replaceAll("serverid", "server_id");
				resDto.put("pid_rule", pid);
				
				/***********************   开始处理静态资源  *********************************************/
			
				int[] sData = new int[4];
				if(inDto.getAsBoolean("saveStatic")){
					urmDao.insert("qamDy.svaeResMid1", resDto) ;
					urmDao.update("qamDy.updateStaticRes",resDto);
					List staticList = urmDao.queryForList("qamDy.getStaticRf",inDto);
					
					if(!StringUtil.isEmpty(staticList)){
						Dto dtoSta = (Dto)staticList.get(0) ;
						sData[0] = dtoSta.getAsInteger("start_port") ;
						sData[2] = dtoSta.getAsInteger("end_port");
						sData[1] = dtoSta.getAsInteger("start_service") ;
						sData[3] = dtoSta.getAsInteger("end_service") ;
					}
					
				}
				
				/**************************  开始处理动态资源  ********************************************/
				if(inDto.getAsBoolean("dyStatic")){
					List dyList = urmDao.queryForList("qamDy.getDyRes", resDto) ;
					if(dyList!=null && dyList.size()>0){
						Dto dDto = (Dto)dyList.get(0);
						Integer resCount = dDto.getAsInteger("res_count");
						Integer startServer = dDto.getAsInteger("start_serviceid");
						Integer startPort =dDto.getAsInteger("start_port");
						Integer serverStep = firmDto.getAsInteger("service_step");
						Integer portStep = firmDto.getAsInteger("port_step");
						
						int endServer = startServer + (resCount - 1)*serverStep ;
						int endPort = startPort + (resCount - 1)*startPort ;
						
						String sql = "" ;
						
						int[] dData = new int[]{startPort,startServer,endPort,endServer};
						if(!inDto.getAsBoolean("saveStatic")||compareValue(sData, dData)){
							sql = "qamDy.svaeResMid" ;
							
						}else{
							sql = "qamDy.saveMidRes" ;
						}

						int server = 0;
						int port = 0 ;
						String ipqam_res_id ;
						for(int i = 0 ; i < resCount ; i ++){
							ipqam_res_id = IDHelper.getIPQAMID();
							server = startServer + i*serverStep ;
							port = startPort + i*portStep ;
							pid = firmDto.getAsString("pid_rule") ;
							String pid_rule = pid.replaceAll("serverid", server+"");
							Dto d = new BaseDto();
							d.put("pid_id", pid_rule);
							d = ((Dto)urmDao.queryForObject("qamDy.mathPid",d));
							String pid_id = d.getAsString("pid_id");
						
							dDto.put("res_type",res_type );
							dDto.put("pid_id", pid_id);
							dDto.put("server_id", server);
							dDto.put("exp_port", port);
							dDto.put("band_width", 37500000);
							dDto.put("ipqam_res_id", ipqam_res_id);
							
							urmDao.insert(sql,dDto);
						}
						
					}
					urmDao.update("qamDy.updateDyRes",resDto);
				}
				
				resDto.put("min_port", minPort);
				resDto.put("res_type", "DATA");
				urmDao.update("qamDy.updateData",resDto);
				
				resDto.put("mid_rf_code", min_rf);
				resDto.put("rf_code", rf_id);
				resDto.put("ipqam_id", ipqam_id);
				
			}
		}
		return resDto;
	}
	
	private boolean compareValue(int[] sData ,int[] dData){
		if(sData[0] > dData[2]){
			if(sData[1] > dData[3] || sData[3] < dData[1])
				return true ;
		}else if(sData[2] < dData[0]){
			if(sData[1] > dData[3] || sData[3] < dData[1])
				return true ;
		}
		return false ;
	}
	
	public Dto saveResData(Dto inDto){
		List midList = urmDao.queryForList("qamDy.getMidHis", inDto);
		if(midList!=null && midList.size()>0){
			for(int i = 0 ; i < midList.size();i++){
				Dto dto1 = (Dto)midList.get(i);
				urmDao.insert("qamDy.svaeResNor",dto1);
			}
		}
		
		Dto d = new BaseDto();
		d.put("rf_code", inDto.get("rf_code"));
		
		inDto.put("rf_code", inDto.get("mid_rf_code"));
		List list = urmDao.queryForList("qamDy.getMidRf", inDto) ;
		Dto resDto = new BaseDto();
		if(list !=null && list.size()>0){
			Dto dto = (Dto)list.get(0);
			inDto.put("ipqam_id", dto.get("base_ipqam_id")) ;
			inDto.put("value", 1) ;
			inDto.put("qstatus", 1);
			log.info("==================>更新QAM:"+JsonHelper.encodeObject2Json(inDto));
			urmDao.update("qamDy.updateQamTemp",inDto);
			resDto.put("rf_code", dto.get("base_rf_code"));
			resDto.put("status", 1);
			urmDao.update("qamDy.updateRfStatus",resDto);
			log.info("==================>更新频点"+JsonHelper.encodeObject2Json(resDto));
		}
		
		Integer count = (Integer)urmDao.queryForObject("qamDy.rfSuccess", d);
		log.info("**************频点审核数目****************"+count + "*********************"+JsonHelper.encodeObject2Json(d));
	
		Dto baseDto = new BaseDto() ;
		if(count>0){
			Dto reDto = new BaseDto();
			reDto.put("ipqam_event_id", d.get("rf_code"));
			reDto.put("oper_status", 11);
			updateMDBForQamRes(reDto);
			baseDto.put("success", TRUE);
		}else{
			baseDto.put("false", TRUE);
		}
		return baseDto ;
	}
	
	public Dto saveAllToNormal(Dto inDto){
		long current = Calendar.getInstance().getTimeInMillis();
		Dto out = (Dto) saveNormalData(inDto) ;
		Dto outDto = new BaseDto() ;
		if(!StringUtil.isEmpty(out))
			outDto = saveResData(out);
		else
			outDto.put("false", TRUE);
		
		long currentTime = Calendar.getInstance().getTimeInMillis() ;
		log.info("=====================》频点信息确认结束，用时：" + StringUtil.ToBeString(currentTime - current)) ;
		return outDto  ;
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

	public Dto batchPrompt(Dto inDto) {
		Dto outDto = new BaseDto();
		String strChecked = inDto.getAsString("strChecked");
		if(strChecked==null || "".equals(strChecked)){
			outDto.put("success" ,FALSE);
		}else{
			String[] ids = strChecked.split(",");
			for(int i = 0 ; i < ids.length ; i ++){
				Dto dto = new BaseDto();
				dto.put("prompt_id",ids[i] );
				urmDao.update("qamDy.batchData",dto);
			}
			outDto.put("success", TRUE);
		}
		return outDto;
	}

	
}
