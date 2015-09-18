package org.eredlab.g4.cabs.service;

import org.eredlab.g4.bmf.base.BaseService;
import org.eredlab.g4.ccl.datastructure.Dto;

/**
 * 云应用承载平台监控操作接口
 * 
 * @author RobinLau
 * @since 2012-04-18
 */
public interface PackageVersionService extends BaseService {

	/**
	 * 插入一条最新升级包数据
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto insertPackage(Dto pDto);
	
	public Dto updatePackage(Dto pDto);
	
	public Dto insertPackage(Dto pDto, String fileName);
	
	public Dto updatePackageTermRel(Dto pDto);
	
	/**
	 * 删除升级包
	 * 
	 * @param pDto
	 * @return
	 */
	public Dto deletePackageVersionItems(Dto pDto);

	public Dto updatePackageTerm(Dto inDto);


}
