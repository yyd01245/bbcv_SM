package org.eredlab.g4.urm.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 应用接入平台应用地址管理接口
 * 
 * @author RobinLau
 * @since 2012-04-18
 */
public interface QamService extends BaseService {

	/**
	 * 保存应用地址
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto saveQamItem(Dto pDto);

	/**
	 * 删除应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto deleteQamItems(Dto pDto);

	/**
	 * 修改应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto updateQamItem(Dto pDto);

	public Dto batchResImport(Dto inDto);

	public Dto saveCacheContor(Dto inDto);

	public Dto downloadQamItems(Dto inDto);

	public Dto redownloadQamItems(Dto inDto);

	public Dto uploadQamItems(Dto inDto);

}
