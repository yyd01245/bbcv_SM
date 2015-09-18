package org.eredlab.g4.urm.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 外部应用接入平台应用地址管理接口
 * 
 * @author Sun Qiang 
 * @since 2012-05-25
 */
public interface AppAccessService extends BaseService {

	/**
	 * 保存应用地址
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto saveAppAccessItem(Dto pDto);

	/**
	 * 删除应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto deleteAppAccessItems(Dto pDto);

	/**
	 * 修改应用
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto updateAppAccessItem(Dto pDto);

}
