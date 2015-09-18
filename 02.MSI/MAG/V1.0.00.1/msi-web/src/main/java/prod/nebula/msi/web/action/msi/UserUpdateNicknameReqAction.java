/** 
 * Project: mtp-web
 * author : PengSong
 * File Created at 2013-11-27 
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
package prod.nebula.msi.web.action.msi;

import java.sql.SQLException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import prod.nebula.commons.enums.msi.MsiCommandEnum;
import prod.nebula.dto.response.msi.AuthCodeResponse;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.msi.web.action.base.BaseAction;

/** 
 * APP注册
 * 
 * 用户名，密码
 * 
 * @author PengSong 
 */
public class UserUpdateNicknameReqAction extends BaseAction {
	private static final long serialVersionUID = 5116943721550801750L;
	
	private static final Logger logger=Logger.getLogger(UserUpdateNicknameReqAction.class);
	
	private static final String cmd = MsiCommandEnum.USER_UPDATE_NICKNAME.getCmd();
	
	private String username;
	
	private String passwd;
	
	private String nickname;
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

		
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public void validParams() {
		if(StringUtils.isBlank(username)) addParamError("username", "username is not blank!");
		if(StringUtils.isBlank(passwd)) addParamError("passwd", "passwd is not blank!");
		if(StringUtils.isBlank(nickname)) addParamError("nickname", "nickname is not blank!");
	}
	
	@Override
	public void action() {
		AuthCodeResponse resp = new AuthCodeResponse(cmd, sequence);
		BaseResponseError error = null;
		JSONObject jsonObject = new JSONObject();
		if(!isHttpPost()) {
			error = new BaseResponseError(cmd, sequence,MsiErrorCodeEnum.IS_NOT_HTTPPOST);
			out(error, resp);
			return;
		}
		this.validParams();
		if(hasParamErrors()){
			error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.PARAMS_ERROR,paramErrorsToString());
		} else {
			try {
				if(userInfoService.validUser(username, passwd)){
					//生成操作授权验证码放入缓存
					userInfoService.updateUserNickname(username,nickname);
					jsonObject.put("action_result", 0);
					jsonObject.put("msg", "user update nickname success");
				}else{
					error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.MOBILE_NOT_EXISTS);
					logger.error("user error");
					jsonObject.put("action_result", 1);
					jsonObject.put("msg", "user update nickname fail");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		out(error, resp);
	}
}
