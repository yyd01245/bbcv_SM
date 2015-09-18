package org.eredlab.g4.urm.service;

import java.util.List;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 应用接入平台应用地址管理接口
 * 
 * @author RobinLau
 * @since 2012-04-18
 */
public interface QamResourceService extends BaseService {

	/**
	 * 保存应用地址
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto saveQamResItem(Dto pDto);

	/**
	 * 删除应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto deleteQamResItems(Dto pDto);

	/**
	 * 修改应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto updateQamResItem(Dto pDto);
	
	/**
	 * 从Excel批量导入到数据库
	 * 
	 */
	public Dto batchResImport(Dto inDto);

}
