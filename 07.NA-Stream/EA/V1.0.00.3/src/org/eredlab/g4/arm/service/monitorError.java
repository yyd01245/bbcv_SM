package org.eredlab.g4.arm.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 用户管理与授权业务接口
 * 
 * @author XiongChun
 * @since 2010-01-13
 */
public interface monitorError extends BaseService {

	public Dto executeProcedure(Dto inDto);
	
}
