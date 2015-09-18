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

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import prod.nebula.commons.enums.msi.MsiCommandEnum;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.dto.response.msi.KeySendResponse;
import prod.nebula.exception.msi.BbcvMsiException;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.model.msi.UserInfo;
import prod.nebula.msi.web.action.base.BaseAction;
import prod.nebula.service.socket.mgw.MgwKeyControlServiceImpl;
import prod.nebula.service.socket.mgw.VgwserviceImpl;

/** 
 * TODO Comment of KeySendReqAction 
 * 
 * @author PengSong 
 */
public class KeySendReqAction extends BaseAction {

	private static final long serialVersionUID = -3766412656535187063L;

	private static final Logger logger=Logger.getLogger(KeySendReqAction.class);
	
	private static final String cmd = MsiCommandEnum.KEY_SEND.getCmd();
			
	private String key_type;
	
	private String key_value;
	
	private String username;
	
	private String token;
	
	public void setToken(String token) {
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Resource(name="mgwKeyControlService",type=MgwKeyControlServiceImpl.class)
	private MgwKeyControlServiceImpl mgwKeyControlService;
	@Resource(name="vgwService",type=VgwserviceImpl.class)
	private VgwserviceImpl vgwservice;
	@Override
	public void validParams() {
		if(StringUtils.isBlank(username)) addParamError("username", "username is not blank!");
		if(StringUtils.isBlank(token)) addParamError("token", "token is not blank!");
		if(StringUtils.isBlank(key_type)) addParamError("key_type", "key_type is not blank!");
		if(StringUtils.isBlank(key_value)) addParamError("key_value", "key_value is not blank!");
	}
	
	@Override
	public void action() {
		KeySendResponse resp = new KeySendResponse(cmd, sequence);
		BaseResponseError error = null;
		if(!isHttpPost()) {
			error = new BaseResponseError(cmd, sequence,MsiErrorCodeEnum.IS_NOT_HTTPPOST);
			out(error, resp);
			return;
		}
		validParams();
		if(hasParamErrors()){
			error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.PARAMS_ERROR,paramErrorsToString());
		} else {
			try {
				if(!userInfoService.validUserToken(username, token)){//token是否失效
					if("weixin".equals(type)||"weixin"==type){
						error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.WEC_TOKEN_IS_ERROR);
					}else{
						error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.TOKEN_IS_ERROR);
					}
				}else{
					UserInfo userinfo = userInfoService.getUserInfo(username);
					Integer userstatus = userinfo.getUser_status();
					resp.setStatus(String.valueOf(userstatus));
					logger.info("【UCMS】当前用户状态：======"+userstatus);
					if(userstatus==2||userstatus.equals(2)){//是否处于VOD状态
						mgwKeyControlService.keyControl(username, key_value,"2",userinfo.getStream_id(),userinfo.getNickname());
					}else if(userstatus==3||userstatus.equals(3)){
						//键值发送到VGW
						mgwKeyControlService.keyControl(username, key_value,"3",userinfo.getStream_id(),userinfo.getNickname());
						JSONObject getTimeJson = new JSONObject();
						getTimeJson.put("cmd", "gettime");
						getTimeJson.put("sessionid", userinfo.getStream_id());
						String retString = vgwservice.send(getTimeJson.toString());
						if(retString!=null&&!"".equals(retString)){
							retString = retString.replace("XXEE", "");
							JSONObject retJson = JSONObject.fromObject(retString);
							resp.setCurrent_time(retJson.getString("currenttime"));
							resp.setTotal_time(retJson.getString("totaltime"));
						}
					}else{
						if("weixin".equals(type)||"weixin"==type){
							error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.WEC_USER_NOT_INVOD);
						}else{
							error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.USER_NOT_INVOD);
						}
					}
					
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (BbcvMsiException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		out(error, resp);
	}

	public void setKey_type(String key_type) {
		this.key_type = key_type;
	}

	public void setKey_value(String key_value) {
		this.key_value = key_value;
	}

}
