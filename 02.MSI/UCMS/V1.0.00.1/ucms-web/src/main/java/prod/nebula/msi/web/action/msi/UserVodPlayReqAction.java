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

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import prod.nebula.commons.config.UcmsConfig;
import prod.nebula.commons.enums.msi.MsiCommandEnum;
import prod.nebula.commons.string.OidUtils;
import prod.nebula.dto.response.msi.AuthCodeResponse;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.dto.response.msi.UserVodPlayResponse;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.model.msi.UserInfo;
import prod.nebula.msi.web.action.base.BaseAction;
import prod.nebula.service.socket.center.CenterVodPlayReqImpl;

/** 
 * VOD 点播
 * 
 * 用户名，rtsp地址
 * 
 * @author zhangdj 
 */
public class UserVodPlayReqAction extends BaseAction {
	
	@Resource(name="msVodPlayReqService",type=CenterVodPlayReqImpl.class)
	private CenterVodPlayReqImpl msVodPlayReqService;
	
	private static final long serialVersionUID = 5116943721550801750L;
	
	private static final Logger logger=Logger.getLogger(UserVodPlayReqAction.class);
	
	private static final String cmd = MsiCommandEnum.USER_VODPLAY.getCmd();
	
	private String username;
	private String token;
	private String url;
	
	public void setUsername(String username) {
		this.username = username;
	}

	
	public void setToken(String token) {
		this.token = token;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void validParams() {
		if(StringUtils.isBlank(username)) addParamError("username", "username is not blank!");
		if(StringUtils.isBlank(token)) addParamError("token", "token is not blank!");
		if(StringUtils.isBlank(url)) addParamError("url", "url is not blank!");
	}
	
	@Override
	public void action() {
		UserVodPlayResponse resp = new UserVodPlayResponse(cmd, sequence);
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
				//检验token，如果正确，重新生成token，后续使用新的token
				if(userInfoService.validUserToken(username, token)){
					String new_token = OidUtils.newId();
					resp.setNew_token(new_token);
					
					UserInfo userInfo = userInfoService.getUserInfo(username);
					resp.setStatus(String.valueOf(userInfo.getUser_status()));
					//调用MS VOD点播接口
					if(userInfo.getUser_status()==2||userInfo.getUser_status().equals(2)||userInfo.getUser_status()==3||userInfo.getUser_status().equals(3)){
						if(msVodPlayReqService.userVodPlay(username, userInfo.getStream_id(), url)){
							userInfoService.updateUserToken(username,new_token);//更新数据库
							userInfoService.updateUserStatus(username,UcmsConfig.play_status);
							resp.setMessage("点播VOD成功，请跳转到播控界面。。。");
							resp.setStatus("3");
						}else{
							error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.VOD_PLAY_FAIL);
							resp.setMessage("VOD播放请求失败，请重新点播...");
						}
					}else{
						error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.USER_UNBAND);
						resp.setMessage("token失效，请跳转到登陆页面...");
					}
				}else{
					error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.TOKEN_IS_ERROR);
					resp.setMessage("token失效，请跳转到登陆页面...");
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
