/** 
 * Project: msi-dao
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
package prod.nebula.dao.msi;

import java.sql.SQLException;
import java.util.List;

import prod.nebula.commons.page.Page;
import prod.nebula.model.msi.Licence;

/** 
 * TODO Comment of LicenceDao 
 * 
 * @author PengSong 
 */
public interface LicenceDao {
	List<Licence> listLicence(Licence licence, Page page, String orderBy) throws SQLException;
	
	Integer listLicenceCount(Licence licence) throws SQLException;
	
	/**
	 * @param licence
	 * @return int The number of rows affected by the insert.
	 * @throws SQLException
	 */
	Integer addLicence(Licence licence) throws SQLException;
	
	/**
	 * update licence info by id
	 * @param licence
	 * @param id 
	 * @return int The number of rows affected by the update.
	 * @throws SQLException
	 */
	Integer updateLicenceById(Licence licence,Integer id) throws SQLException;
	
	Integer deleteLicenceById(Integer id) throws SQLException;
	
}
