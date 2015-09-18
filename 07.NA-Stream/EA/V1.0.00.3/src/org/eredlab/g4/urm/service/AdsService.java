package org.eredlab.g4.urm.service;


import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 应用接入平台应用地址管理接口
 * 
 * @author RobinLau
 * @since 2012-04-18
 */
public interface AdsService extends BaseService {

	Dto deleteAds(Dto inDto);
	
	public Dto saveAds(Dto pDto);
	
	Dto updateAds(Dto pDto) ;
	
	Dto saveCacheContor(Dto pDto) ;

}
