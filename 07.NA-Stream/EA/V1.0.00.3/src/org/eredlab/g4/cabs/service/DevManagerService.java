package org.eredlab.g4.cabs.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;
/**
 * 设备管理操作接口
 * 
 * @author Administrator
 *
 */
public interface DevManagerService extends BaseService{

	Dto saveDevManagerItem(Dto inDto);

	Dto updateDevManagerItem(Dto inDto);

	Dto deleteDevManagerItems(Dto inDto);

}
