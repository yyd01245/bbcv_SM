package org.eredlab.g4.rif.util;

import java.util.Map;

import org.eredlab.g4.arm.vo.UserInfoVo;

public class UserInfoUtil {
	
	public static UserInfoVo genUserInfoVo(Map<String,String> map){
		
		if(map!=null && !map.isEmpty()){
			UserInfoVo user = new UserInfoVo();
			user.setAccount(map.get("account"));
			user.setCustomId(map.get("customId"));
			user.setDeptid(map.get("deptid"));
			user.setExplorer(map.get("explorer"));
			user.setLock(map.get("lock"));
			user.setLoginIP(map.get("loginIP"));
			user.setPassword(map.get("password"));
			user.setSessionCreatedTime(map.get("sessionCreatedTime"));
			user.setSessionID(map.get("sessionID"));
			user.setSex(map.get("sex"));
			user.setTheme(map.get("theme"));
			user.setUserid(map.get("userid"));
			user.setUsername(map.get("username"));
			return user;
		}else
			return null;
		
	}

}
