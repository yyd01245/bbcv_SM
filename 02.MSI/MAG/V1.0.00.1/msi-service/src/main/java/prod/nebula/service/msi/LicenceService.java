/** 
 * Project: msi-service
 * author : PengSong
 * File Created at 2013-12-2 
 *
 * Copyright bbcvision Corporation Limited. 
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * bbcvision company. ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with bbcvision.com. 
 */
package prod.nebula.service.msi;

import java.sql.SQLException;

import prod.nebula.exception.msi.BbcvMsiException;
import prod.nebula.model.msi.Licence;

/** 
 * TODO Comment of LicenceService 
 * 
 * @author PengSong 
 */
public interface LicenceService {
	/**
	 * 根据app_name验证licence
	 * @param app_name 名称
	 * @param licence MD5加密后的licence
	 * @return
	 */
	boolean validLicence(String app_name,String licence) throws SQLException;
	
	/**
	 * 根据app_name获取licence信息,如果没有获取到或者获取到多条记录时返回null
	 * @param app_name
	 * @return Licence
	 * @throws SQLException
	 * @throws BbcvMsiException
	 */
	Licence getLicenceByAppName(String app_name) throws SQLException;
}
