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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import prod.nebula.commons.config.UcmsConfig;
import prod.nebula.commons.enums.msi.MsiCommandEnum;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.dto.response.msi.UserSessionQueryResponse;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.model.msi.UserInfo;
import prod.nebula.msi.web.action.base.BaseAction;

/** 
 * VOD 点播
 * 
 * 用户名，rtsp地址
 * 
 * @author zhangdj 
 */
public class UserSessionQueryReqAction extends BaseAction {
	
	
	private static final long serialVersionUID = 5116943721550801750L;
	
	private static final Logger logger=Logger.getLogger(UserSessionQueryReqAction.class);
	
	private static final String cmd = MsiCommandEnum.USER_SESSIONQUERY.getCmd();
	
	private String username;
	private String token;
	
	public void setUsername(String username) {
		this.username = username;
	}

	
	public void setToken(String token) {
		this.token = token;
	}



	@Override
	public void validParams() {
		if(StringUtils.isBlank(username)) addParamError("username", "username is not blank!");
		if(StringUtils.isBlank(token)) addParamError("token", "token is not blank!");
	}
	
	@Override
	public void action() {
		UserSessionQueryResponse resp = new UserSessionQueryResponse(cmd, sequence);
		BaseResponseError error = null;
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
				UserInfo userinfo = userInfoService.getUserInfo(username);
				//检验token，如果正确，重新生成token，后续使用新的token
				if(!userInfoService.validUserToken(username, token)){
					error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.TOKEN_IS_ERROR);
					resp.setMessage("token失效，请跳转到登陆页面...");
				}
				int user_status = userinfo.getUser_status();
				resp.setStatus(String.valueOf(user_status));
				switch(user_status){//根据用户状态返回提示，并返回对应的门户地址
				case 1:
					resp.setMessage(UcmsConfig.unbanding_message);
					break;
				case 2:
					resp.setMessage(UcmsConfig.banding_message+userinfo.getChannel_id());
					break;
				case 3:
					resp.setMessage(UcmsConfig.play_message+userinfo.getChannel_id());
					break;
				case 0:
					resp.setMessage(UcmsConfig.user_unable_message);
					break;
				}
			} catch (SQLException e) {
				error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.SQLEXCEPTION_ERROR);
				logger.error(e.getMessage(),e);
			} catch (Exception e) {
				error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.SYSTEM_ERROR);
				logger.error(e.getMessage(),e);
			}
		}
		out(error, resp);
	}
}
