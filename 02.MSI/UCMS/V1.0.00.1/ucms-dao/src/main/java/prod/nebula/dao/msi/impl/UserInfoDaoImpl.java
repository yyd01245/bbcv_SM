/** 
 * Project: bbcvision3-dao
 * author : PengSong
 * File Created at 2013-11-12 
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
import prod.nebula.dao.msi.UserInfoDao;
import prod.nebula.model.msi.UserInfo;


/** 
 * TODO Comment of UserInfoDaoMybatisImpl2 
 * 
 * @author PengSong 
 */
@Repository("userInfoDao")
public class UserInfoDaoImpl extends SqlSessionDaoSupport implements UserInfoDao {
	
//	public List<UserInfo> listUserInfo(UserInfo userInfo,Page page,String orderBy) throws SQLException {
//		Map map = new HashMap();
//		userInfo=(userInfo == null)?new UserInfo():userInfo;
//		map.put("userInfo", userInfo);
//		if(null != page){
//			page.setTotalCount(listUserInfoCount(userInfo));
//			map.put("page", page);
//		}
//		if(null != orderBy && !"".equals(orderBy)) map.put("orderBy", orderBy);
//		return getSqlSession().selectList("msi.userInfo.listUserInfo", map);
//	}

	public List<UserInfo> listUserInfo(UserInfo userInfo,Page page,String orderBy) throws SQLException {
		Map map = new HashMap();
		if(userInfo==null){
			return null;
		}else{
			map.put("userInfo", userInfo);
			if(null != page){
				page.setTotalCount(listUserInfoCount(userInfo));
				map.put("page", page);
			}
			if(null != orderBy && !"".equals(orderBy)) map.put("orderBy", orderBy);
			return getSqlSession().selectList("msi.userInfo.listUserInfo", map);
		}
	}
	public Integer listUserInfoCount(UserInfo userInfo) throws SQLException {
		Integer result = (Integer)getSqlSession().selectOne("msi.userInfo.listUserInfoCount", userInfo);
		return (result != null && result > 0) ? result : 0;
	}
	
	public Integer addUserInfo(UserInfo userInfo) throws SQLException {
		return getSqlSession().insert("msi.userInfo.addUserInfo",userInfo);
	}
	
	public Integer updateUserInfoById(UserInfo userInfo, Integer id) throws SQLException {
		if(null != id) {
			userInfo.setUser_id(id);
			return getSqlSession().update("msi.userInfo.updateUserInfo", userInfo);
		} else {
			throw new SQLException("updateUserInfoById parameter id is required.");
		}
	}
	
	public Integer updateUserInfoByTermId(UserInfo userInfo, String username) throws SQLException {
		if(null != username) {
			userInfo.setUsername(username);
			return getSqlSession().update("msi.userInfo.updateUserInfo", userInfo);
		} else {
			throw new SQLException("updateUserInfoByTermId parameter term_id is required.");
		}
	}
	
	public Integer deleteUserInfoById(Integer id) throws SQLException {
		if(null != id) {
			return getSqlSession().delete("msi.userInfo.deleteUserInfoByUserId", id);
		} else {
			throw new SQLException("deleteUserInfoById parameter id is required.");
		}
	}
	
	public Integer deleteUserInfoByTermId(String username) throws SQLException {
		if(null != username) {
			return getSqlSession().delete("msi.userInfo.deleteUserInfoByUserNum", username);
		} else {
			throw new SQLException("deleteUserInfoByTermId parameter username is required.");
		}
	}
	
	public Integer updateUserInfoByUsername(UserInfo userInfo, String username) throws SQLException {
		if(null != username) {
			userInfo.setUsername(username);
			return getSqlSession().update("msi.userInfo.updateUserInfoByUsername", userInfo);
		} else {
			throw new SQLException("updateUserInfoByTermId parameter username is required.");
		}
	}
}
