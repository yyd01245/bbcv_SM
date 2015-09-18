package org.eredlab.g4.urm.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.urm.service.SignalingGatewayConfigService;

public class SignalingGatewayConfigServiceImpl extends BaseServiceImpl implements
SignalingGatewayConfigService {
	
	
	
	Log logger = LogFactory.getLog(SignalingGatewayConfigServiceImpl.class);

	
	
	
	public Dto savaGatewayConfig(Dto inDto) {
		
		Dto outDto = new BaseDto();	
		

		
		
		urmDao.insert("SignalingGatewayConfig.saveAccessGatewayConfig", inDto);	
		outDto.put("success", TRUE);		
		return outDto;
		
	}
	public Dto updateGatewayConfigById(Dto inDto) {
		
		Dto outDto = new BaseDto();	
		String id = inDto.getAsString("id");
		String field1 = inDto.getAsString("field1");
		String field2 = inDto.getAsString("field2");
		String field3 = inDto.getAsString("field3");
		String field4 = inDto.getAsString("field4");
		String field5 = inDto.getAsString("field5");
		String field6 = inDto.getAsString("field6");
		String field7 = inDto.getAsString("field7");
		String field8 = inDto.getAsString("field8");
		String field9 = inDto.getAsString("field9");
		String field10 = inDto.getAsString("field10");
	
		inDto.put("id", id);
		inDto.put("field1", field1);
		inDto.put("field2", field2);
		inDto.put("field3", field3);
		inDto.put("field4", field4);
		inDto.put("field5", field5);
		inDto.put("field6", field6);
		inDto.put("field7", field7);
		
		inDto.put("field8", field8);
		inDto.put("field9", field9);
		inDto.put("field10", field10);
		
		urmDao.update("SignalingGatewayConfig.updateAccessGatewayConfigById", inDto);	
		outDto.put("success", TRUE);		
		return outDto;
		
		
	}

	public Dto authGatewayConfigById(Dto inDto) {
		Dto outDto = new BaseDto();	
		String field1 = inDto.getAsString("field1");
		String field2 = inDto.getAsString("field2");
		
		
		inDto.put("field1", field1);
		inDto.put("field2", field2);
		
		urmDao.update("SignalingGatewayConfig.authAccessGatewayConfigById", inDto);	
		outDto.put("success", TRUE);	
		return null;
	}
	

}
