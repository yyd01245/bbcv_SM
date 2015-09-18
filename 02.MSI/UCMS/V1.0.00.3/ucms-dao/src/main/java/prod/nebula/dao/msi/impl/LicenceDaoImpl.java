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
package prod.nebula.dao.msi.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import prod.nebula.commons.page.Page;
import prod.nebula.dao.msi.LicenceDao;
import prod.nebula.model.msi.Licence;

/** 
 * TODO Comment of LicenceDaoImpl 
 * 
 * @author PengSong 
 */
@Repository("licenceDao")
public class LicenceDaoImpl extends SqlSessionDaoSupport implements LicenceDao {

	public List<Licence> listLicence(Licence licence, Page page, String orderBy) throws SQLException {
		Map map = new HashMap();
		licence=(licence == null)?new Licence():licence;
		map.put("licence", licence);
		if(null != page){
			page.setTotalCount(listLicenceCount(licence));
			map.put("page", page);
		}
		if(null != orderBy && !"".equals(orderBy)) map.put("orderBy", orderBy);
		return getSqlSession().selectList("msi.licence.listLicence", map);
	}

	public Integer listLicenceCount(Licence licence) throws SQLException {
		Integer res = (Integer)getSqlSession().selectOne("msi.licence.listLicenceCount", licence);
		return (res != null && res > 0) ? res : 0;
	}

	public Integer addLicence(Licence licence) throws SQLException {
		return getSqlSession().insert("msi.licence.addLicence",licence);
	}

	public Integer updateLicenceById(Licence licence, Integer id) throws SQLException {
		if(null != id) {
			licence.setId(id);
			return getSqlSession().update("msi.licence.updateLicenceById", licence);
		} else {
			throw new SQLException("updateLicenceById parameter id is required.");
		}
	}

	public Integer deleteLicenceById(Integer id) throws SQLException {
		if(null != id) {
			return getSqlSession().delete("msi.licence.deleteLicenceById", id);
		} else {
			throw new SQLException("deleteLicenceById parameter id is required.");
		}
	}

}
