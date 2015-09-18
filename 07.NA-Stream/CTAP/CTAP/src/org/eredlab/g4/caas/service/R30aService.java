package org.eredlab.g4.caas.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 应用接入平台应用地址管理接口
 * 
 * @author RobinLau
 * @since 2012-04-18
 */
public interface R30aService extends BaseService {

	/**
	 * 保存应用地址
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto saveItem(Dto pDto);

	/**
	 * 删除应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto deleteItems(Dto pDto);

	/**
	 * 修改应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto updateItem(Dto pDto);
	/**
	 * 查询应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto queryItems(Dto pDto);

}
