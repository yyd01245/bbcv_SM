package org.eredlab.g4.cabs.service.impl;

import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.cabs.service.ServerMonitorService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;

import java.util.Calendar;
import java.util.Date;

public class ServerMonitorServiceImpl extends BaseServiceImpl implements
		ServerMonitorService {

	public Dto startServer(Dto pDto) {
		Dto outputDto = new BaseDto();
		Calendar calendar = Calendar.getInstance();
		Date createdate = calendar.getTime();
		Dto outDto =(Dto) crsmDao.queryForObject("ServerMonitor.getServerByKey", pDto);
		Integer event_id = (Integer) crsmDao.queryForObject("UpdateStrategy.queryEventMaxIndex");
		if(event_id==null){
			event_id =0;
		}
		outDto.put("event_id", event_id+1);
		outDto.put("oper_status", 1);
		outDto.put("create_time", createdate);
		crsmDao.insert("UpdateStrategy.saveEvent", outDto);
		outputDto.put("success", new Boolean(true));

		return outputDto;
	
	}

	public Dto stopServer(Dto pDto) {
		Dto outputDto = new BaseDto();
		Calendar calendar = Calendar.getInstance();
		Date createdate = calendar.getTime();
		Dto outDto =(Dto) crsmDao.queryForObject("ServerMonitor.getServerByKey", pDto);
		Integer event_id = (Integer) crsmDao.queryForObject("UpdateStrategy.queryEventMaxIndex");
		if(event_id==null){
			event_id =0;
		}
		outDto.put("event_id", event_id+1);
		outDto.put("oper_status", 2);
		outDto.put("create_time", createdate);
		crsmDao.insert("UpdateStrategy.saveEvent", outDto);
		outputDto.put("success", new Boolean(true));

		
		return outputDto;
	}

	public Dto updateServer(Dto pDto) {
		Dto outputDto = new BaseDto();
		Calendar calendar = Calendar.getInstance();
		Date createdate = calendar.getTime();
		Dto outDto =(Dto) crsmDao.queryForObject("ServerMonitor.getServerByKey", pDto);
		Integer event_id = (Integer) crsmDao.queryForObject("UpdateStrategy.queryEventMaxIndex");
		if(event_id==null){
			event_id =0;
		}
		outDto.put("event_id", event_id+1);
		outDto.put("oper_status", 4);
		outDto.put("create_time", createdate);
		crsmDao.insert("UpdateStrategy.saveEvent", outDto);
		outputDto.put("success", new Boolean(true));

		return outputDto;
	}
}
