package org.eredlab.g4.urm.service;

import java.util.List;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 网关配置Service
 * 
 * @author PengFei
 * @since 2012-04-18
 */
public interface AccessGatewayConfigService extends BaseService {


	public Dto savaGatewayConfig(Dto inDto);
	
	
	public Dto updateGatewayConfigById(Dto inDto);
	
	
	
	public Dto authGatewayConfigById(Dto inDto);




}
