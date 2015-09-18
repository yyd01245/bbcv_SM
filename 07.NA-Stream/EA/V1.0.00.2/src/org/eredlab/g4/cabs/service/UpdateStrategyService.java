package org.eredlab.g4.cabs.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 升级策略管理接口
 * 
 * @author Sun Qiang
 * @since 2012-05-18
 */
public interface UpdateStrategyService extends BaseService {

	Dto insertStrategyItem(Dto inDto);

	Dto deleteStrategyItems(Dto inDto);

}
