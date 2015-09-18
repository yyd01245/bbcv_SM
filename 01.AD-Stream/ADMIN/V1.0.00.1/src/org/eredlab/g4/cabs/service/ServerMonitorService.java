package org.eredlab.g4.cabs.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 云应用承载平台监控操作接口
 * 
 * @author RobinLau
 * @since 2012-04-18
 */
public interface ServerMonitorService extends BaseService {

	/**
	 * 启动
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto startServer(Dto pDto);

	/**
	 * 停止
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto stopServer(Dto pDto);
	
	/**
	 * 升级
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto updateServer(Dto pDto);

}
