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
import prod.nebula.model.msi.UserInfo;

/** 
 * TODO Comment of UserInfoDao 
 * 
 * @author PengSong 
 */
public interface UserInfoDao {
	List<UserInfo> listUserInfo(UserInfo userInfo, Page page, String orderBy) throws SQLException;
	
	Integer listUserInfoCount(UserInfo userInfo) throws SQLException;
	
	/**
	 * @param userInfo
	 * @return int The number of rows affected by the insert.
	 * @throws SQLException
	 */
	Integer addUserInfo(UserInfo userInfo) throws SQLException;
	
	/**
	 * update user info by id
	 * @param userInfo
	 * @param user_id 
	 * @return int The number of rows affected by the update.
	 * @throws SQLException
	 */
	Integer updateUserInfoById(UserInfo userInfo,Integer id) throws SQLException;
	
	/**
	 * update user info by term_id
	 * @param userInfo
	 * @param term_id
	 * @return int The number of rows affected by the update.
	 * @throws SQLException
	 */
	Integer updateUserInfoByTermId(UserInfo userInfo,String term_id) throws SQLException;
	
	Integer deleteUserInfoById(Integer id) throws SQLException;
	
	Integer deleteUserInfoByTermId(String term_id) throws SQLException;
	
	public Integer updateUserInfoByUsername(UserInfo userInfo, String username) throws SQLException;
}
