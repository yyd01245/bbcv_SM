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

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import prod.nebula.commons.config.UcmsConfig;
import prod.nebula.commons.enums.msi.MsiCommandEnum;
import prod.nebula.commons.string.OidUtils;
import prod.nebula.dto.response.msi.AuthCodeResponse;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.dto.response.msi.UserChoosetimeResponse;
import prod.nebula.dto.response.msi.UserVodPlayResponse;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.model.msi.UserInfo;
import prod.nebula.msi.web.action.base.BaseAction;
import prod.nebula.service.socket.center.CenterVodPlayReqImpl;
import prod.nebula.service.socket.mgw.VgwserviceImpl;

/** 
 * VOD 点播
 * 
 * 用户名，rtsp地址
 * 
 * @author zhangdj 
 */
public class UserChoosetimeReqAction extends BaseAction {
	
	@Resource(name="vgwService",type=VgwserviceImpl.class)
	private VgwserviceImpl vgwservice;
	
	private static final long serialVersionUID = 5116943721550801750L;
	
	private static final Logger logger=Logger.getLogger(UserChoosetimeReqAction.class);
	
	private static final String cmd = MsiCommandEnum.USER_CHOOSETIME.getCmd();
	
	private String username;
	private String token;
	private String begintime;
	private String stream_id;
	
	
	
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}


	public void setStream_id(String stream_id) {
		this.stream_id = stream_id;
	}


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
		if(StringUtils.isBlank(begintime)) addParamError("begintime", "begintime is not blank!");
		if(StringUtils.isBlank(stream_id)) addParamError("stream_id", "stream_id is not blank!");
	}
	
	@Override
	public void action() {
		UserChoosetimeResponse resp = new UserChoosetimeResponse(cmd, sequence);
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
					UserInfo userInfo = userInfoService.getUserInfo(username);
					resp.setStatus(String.valueOf(userInfo.getUser_status()));
					JSONObject getTimeJson = new JSONObject();
					getTimeJson.put("cmd", "choosetime");
					getTimeJson.put("sessionid", stream_id);
					getTimeJson.put("begintime", begintime);
					getTimeJson.put("serialno", OidUtils.newSerialno(16));
					String retString = vgwservice.send(getTimeJson.toString());
					if(retString==null||"".equals(retString)){
						if("weixin".equals(type)||"weixin"==type){
							error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.WEC_USER_CHOOSETIME_FAIL);
						}else{
							error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.USER_CHOOSETIME_FAIL);
						}
					}
				}else{
					if("weixin".equals(type)||"weixin"==type){
						error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.WEC_TOKEN_IS_ERROR);
					}else{
						error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.TOKEN_IS_ERROR);
					}
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
