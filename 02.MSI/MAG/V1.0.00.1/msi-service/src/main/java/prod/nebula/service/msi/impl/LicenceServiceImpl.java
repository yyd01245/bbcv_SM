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
package prod.nebula.service.msi.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import prod.nebula.commons.string.MD5;
import prod.nebula.dao.msi.LicenceDao;
import prod.nebula.model.msi.Licence;
import prod.nebula.service.msi.LicenceService;

/** 
 * TODO Comment of LicenceServiceImpl 
 * 
 * @author PengSong 
 */
@Service("licenceService")
public class LicenceServiceImpl implements LicenceService {
	
	@Resource(name="licenceDao",type=LicenceDao.class)
	private LicenceDao licenceDao;
	
	public boolean validLicence(String app_name, String licence) throws SQLException {
		Licence li = getLicenceByAppName(app_name);
//		if(null != li && MD5.getMD5Casestring(li.getLicence()).equalsIgnoreCase(licence)) {
//			return true;
//		}
		if(null != li && li.getLicence().equalsIgnoreCase(licence)) {
			return true;
		}
		return false;
	}
	
	public Licence getLicenceByAppName(String app_name) throws SQLException {
		Licence licence = new Licence();
		licence.setApp_name(app_name);
		List<Licence> ls = licenceDao.listLicence(licence, null, null);
		if(ls != null && ls.size() == 1) {
			return ls.get(0);
		} else {
			return null;
		}
	}

}
