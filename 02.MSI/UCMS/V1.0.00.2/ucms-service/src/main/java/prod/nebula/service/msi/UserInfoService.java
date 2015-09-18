/** 
 * Project: bbcvision3-service
 * author : PengSong
 * File Created at 2013-11-5 
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
import java.util.List;

import prod.nebula.commons.page.Page;
import prod.nebula.model.msi.UserInfo;


/** 
 * TODO Comment of UserInfoService 
 * 
 * @author PengSong 
 */
public interface UserInfoService {
	/**
	 * 根据id获取用户信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	UserInfo getUserInfo(Integer id) throws SQLException;
	
	/**
	 * 根据终端id获取用户信息
	 * @param term_id
	 * @return
	 * @throws SQLException
	 */
	UserInfo getUserInfo(String term_id) throws SQLException;
	
	/**
	 * 分页查询
	 * @param userInfo
	 * @param page
	 * @param orderBy
	 * @return
	 * @throws SQLException
	 */
	List<UserInfo> listUserInfo(UserInfo userInfo,Page page,String orderBy) throws SQLException;
	
	/**
	 * 总数查询
	 * @param userInfo
	 * @return
	 * @throws SQLException
	 */
	Integer listUserInfoCount(UserInfo userInfo) throws SQLException;
	
	/**
	 * 根据终端id，添加用户
	 * @param term_id
	 * @return int The number of rows affected by the insert.
	 * @throws SQLException
	 */
	Integer addUserInfo(String username,String passwd) throws SQLException;
	
	/**
	 * 根据终端Id检查用户是否存在,存在返回true，不存在返回false
	 * @param term_id
	 * @return
	 * @throws SQLException
	 */
	boolean checkUserInfo(String term_id) throws SQLException;
	
	void addTrasactionTest(UserInfo userInfo) throws SQLException;
	
	void trasactionTest(UserInfo userInfo) throws SQLException;
	public boolean validUser(String app_name, String passwd) throws SQLException;
	
	public Integer updateUserToken(String username,String token) throws SQLException;
	
	public boolean validUserToken(String username, String token) throws SQLException;
	public Integer updateUserVodPage(String username,String vod_page,String stream_id,String channel_id,int status) throws SQLException;
	public Integer unBindUser(String username) throws SQLException;
	public Integer updateUserStatus(String username,int status) throws SQLException;
}
