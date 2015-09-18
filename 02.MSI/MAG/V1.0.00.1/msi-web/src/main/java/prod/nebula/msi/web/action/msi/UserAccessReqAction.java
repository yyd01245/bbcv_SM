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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import prod.nebula.commons.config.MsiConfig;
import prod.nebula.commons.enums.msi.MsiCommandEnum;
import prod.nebula.commons.string.OidUtils;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.dto.response.msi.UserAccessReAction;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.msi.web.action.base.BaseAction;

/** 
 * 用户接入
 * 
 * 用户名、密码，返回token
 * 
 * @author PengSong 
 */
public class UserAccessReqAction extends BaseAction {
	private static final long serialVersionUID = 5116943721550801750L;
	
	private static final Logger logger=Logger.getLogger(UserAccessReqAction.class);
	
	private static final String cmd = MsiCommandEnum.USER_ACCESS.getCmd();
	
	private String username;
	
	private String passwd;
	
	private String version;
	
	private String appname;
	private String licence;
	
	
	@Override
	public void validParams() {
		if(StringUtils.isBlank(username)) addParamError("username", "username is not blank!");
		if(StringUtils.isBlank(passwd)) addParamError("passwd", "passwd is not blank!");
		if(StringUtils.isBlank(version)) addParamError("version", "version is not blank!");
		if(StringUtils.isBlank(appname)) addParamError("appname", "appname is not blank!");
		if(StringUtils.isBlank(licence)) addParamError("licence", "licence is not blank!");
	}
	
	@Override
	public void action() {
		UserAccessReAction resp = new UserAccessReAction(cmd, sequence);
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
				jsonObject.put("cmd", "user_action");
				jsonObject.put("action_type", "G");
				jsonObject.put("username", username);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar nowTime = Calendar.getInstance();
				jsonObject.put("action_date", sdf.format(nowTime.getTime()));
				jsonObject.put("cmd", "user_action");
				jsonObject.put("stream_id", "");
				if(licenceService.validLicence(appname, licence)) {
					if(userInfoService.validUser(username, passwd)){
						//生成操作授权验证码放入缓存
						String auth_code = OidUtils.newId();
						resp.setToken(auth_code);
						resp.setService_url(MsiConfig.serviceIP+":"+MsiConfig.servicePort);
						userInfoService.updateUserToken(username,auth_code);
						jsonObject.put("action_result", 0);
						jsonObject.put("msg", "user access success");
					}else{
						error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.MOBILE_NOT_EXISTS);
						logger.error("user error");
						jsonObject.put("action_result", 1);
						jsonObject.put("msg", "user access fail");
					}
					
				} else {
					error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.LICENCE_ERROR);
					logger.error("licence error");
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
		try {
			InetAddress ia = InetAddress.getByName(MsiConfig.smServiceIP);
			DatagramSocket socket = new DatagramSocket(0);
			socket.connect(ia, Integer.valueOf(MsiConfig.smServicePort));
			String sendString = jsonObject.toString()+"XXEE";
			DatagramPacket dp = new DatagramPacket(sendString.getBytes(),
					sendString.getBytes().length);
			socket.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}
}
