package org.eredlab.g4.urm.service;


import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 应用接入平台应用地址管理接口
 * 
 * @author RobinLau
 * @since 2012-04-18
 */
public interface KeyConfigService extends BaseService {

	Dto deleteKeyConfigs(Dto inDto);
	
	public Dto saveKeyConfig(Dto pDto);
	
	Dto updateKeyConfig(Dto pDto) ;
	
	Dto saveCacheContor(Dto pDto) ;

}
