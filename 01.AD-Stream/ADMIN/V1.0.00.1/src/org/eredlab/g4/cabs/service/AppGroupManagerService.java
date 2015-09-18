package org.eredlab.g4.cabs.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 应用组管理操作接口
 * 
 * @author Sun Qiang
 * @since 2012-05-14
 */
public interface AppGroupManagerService extends BaseService {

	/**
	 * 保存应用地址
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto saveAppGroupManagerItem(Dto pDto);

	/**
	 * 删除应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto deleteAppGroupManagerItems(Dto pDto);

	/**
	 * 修改应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto updateAppGroupManagerItem(Dto pDto);
	
	public Dto saveBusiToDB(Dto saveDto);

	public Dto saveBusiDetailToDB(Dto retDto);
	
	public String aimQuery(Dto inDto);
	
	public Dto vncShutdown(Dto inDto);

	public String vncQuery(Dto inDto);

	public Dto vncUpload(Dto inDto);

}
