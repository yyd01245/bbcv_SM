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
package prod.nebula.service.msi.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import prod.nebula.commons.date.DateUtil;
import prod.nebula.commons.enums.msi.UserStatusEnum;
import prod.nebula.commons.page.Page;
import prod.nebula.commons.string.MD5;
import prod.nebula.dao.msi.UserInfoDao;
import prod.nebula.exception.msi.BbcvMsiException;
import prod.nebula.model.msi.Licence;
import prod.nebula.model.msi.UserInfo;
import prod.nebula.service.msi.UserInfoService;


/** 
 * TODO Comment of UserInfoServiceImpl 
 * 
 * @author PengSong 
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
	
	@Resource(name="userInfoDao",type=UserInfoDao.class)
	private UserInfoDao userInfoDao;
	private static final Logger logger=Logger.getLogger(UserInfoServiceImpl.class);
	
	public UserInfo getUserInfo(Integer id) throws SQLException {
		UserInfo userInfo = new UserInfo();
		userInfo.setUser_id(id);
		List<UserInfo> ls = listUserInfo(userInfo, null, null);
		if(ls != null && ls.size() == 1) {
			return ls.get(0);
		} else {
			return null;
		}
	}

	public UserInfo getUserInfo(String term_id) throws SQLException {
		UserInfo userInfo = new UserInfo(term_id);
		userInfo.setUsername(term_id);
		List<UserInfo> ls = listUserInfo(userInfo, null, null);
		if(ls != null && ls.size() == 1) {
			return ls.get(0);
		} else {
			return null;
		}
	}
	
	public boolean checkUserInfo(String term_id) throws SQLException {
		UserInfo userInfo = new UserInfo(term_id);
		userInfo.setUsername(term_id);
		List<UserInfo> ls = listUserInfo(userInfo, null, null);
		return (ls != null && ls.size() > 0);
	}
	
	public List<UserInfo> listUserInfo(UserInfo userInfo, Page page, String orderBy) throws SQLException {
		return userInfoDao.listUserInfo(userInfo, page, orderBy);
	}

	public Integer listUserInfoCount(UserInfo userInfo) throws SQLException {
		return userInfoDao.listUserInfoCount(userInfo);
	}
	
	public Integer addUserInfo(String username,String passwd) throws SQLException {
		if(!checkUserInfo(username)) {
			logger.info("用户名有效，且数据库中没有！");
			UserInfo userInfo = new UserInfo(username);
			userInfo.setUsername(username);
			userInfo.setPasswd(passwd);
			userInfo.setUser_status(1);
			return userInfoDao.addUserInfo(userInfo);
		}
		return null;
	}
	
	/**
	 * 基于spring aop声明式事务,方法名的开头定义，异常回滚，事务传播类型在配置文件中全局定义
	 * @throws BbcvMsiException 
	 */
	public void addTrasactionTest(UserInfo userInfo) throws SQLException {
		UserInfo info = getUserInfo(userInfo.getUsername());
		if(info == null) {
			userInfoDao.addUserInfo(userInfo);
		} else {
			userInfoDao.deleteUserInfoByTermId(info.getUsername());
			throw new SQLException("userInfo exists.");
		}
	}
	
	/**
	 * 基于spring注解的声明式事务
	 * @param userInfo
	 * @throws SQLException
	 * @throws BbcvMsiException 
	 */
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public void trasactionTest(UserInfo userInfo) throws SQLException {
		UserInfo info = getUserInfo(userInfo.getUsername());
		if(info == null) {
			userInfoDao.addUserInfo(userInfo);
		} else {
			userInfoDao.deleteUserInfoById(info.getUser_id());
			throw new SQLException("userInfo exists.");
		}
	}
	
	public boolean validUser(String app_name, String passwd) throws SQLException {
		UserInfo user = getUserInfo(app_name);
//		if(null != user && MD5.getMD5Casestring(user.getPasswd()).equalsIgnoreCase(passwd)) {
//			return true;
//		}
		if(null != user &&user.getPasswd().equalsIgnoreCase(passwd)) {
			return true;
		}
		return false;
	}
	
	public Integer updateUserToken(String username,String token) throws SQLException {
			UserInfo userInfo = new UserInfo(username);
			userInfo.setUsername(username);
			userInfo.setToken(token);
			return userInfoDao.updateUserInfoByUsername(userInfo,username);
	}
	
	public boolean validUserToken(String username, String token) throws SQLException {
		UserInfo user = getUserInfo(username);
//		if(null != user && MD5.getMD5Casestring(user.getPasswd()).equalsIgnoreCase(passwd)) {
//			return true;
//		}
		if(null != user &&user.getToken().equalsIgnoreCase(token)) {
			return true;
		}
		return false;
	}

	@Override
	public Integer updateUserNickname(String username, String nickname)
			throws SQLException {
		UserInfo userInfo = new UserInfo(username);
		userInfo.setUsername(username);
		userInfo.setNickname(nickname);
		return userInfoDao.updateUserInfoByUsername(userInfo,username);
	}
	
}
