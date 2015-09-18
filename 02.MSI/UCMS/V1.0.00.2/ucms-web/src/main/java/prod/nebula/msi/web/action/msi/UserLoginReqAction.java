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

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import prod.nebula.commons.config.UcmsConfig;
import prod.nebula.commons.enums.msi.MsiCommandEnum;
import prod.nebula.commons.string.OidUtils;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.dto.response.msi.LoginCodeResponse;
import prod.nebula.exception.msi.BbcvMsiException;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.model.msi.UserInfo;
import prod.nebula.msi.web.action.base.BaseAction;
import prod.nebula.service.socket.center.CenterServiceImpl;

/** 
 * 用户登陆
 * 
 * 用户名，TOKEN
 * 
 * @author PengSong 
 */
public class UserLoginReqAction extends BaseAction {
//	@Resource(name="centerService",type=CenterServiceImpl.class)
//	private CenterServiceImpl centerService;
	private static final long serialVersionUID = 5116943721550801750L;
	
	private static final Logger logger=Logger.getLogger(UserLoginReqAction.class);
	
	private static final String cmd = MsiCommandEnum.USER_LOGIN.getCmd();
	
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
		JSONObject jsonObject = new JSONObject();
		LoginCodeResponse resp = new LoginCodeResponse(cmd, sequence);
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
					jsonObject.put("cmd", "user_action");
					jsonObject.put("action_type", "A");
					jsonObject.put("username", username);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar nowTime = Calendar.getInstance();
					jsonObject.put("action_date", sdf.format(nowTime.getTime()));
					jsonObject.put("cmd", "user_action");
					//检验token，如果正确，重新生成token，后续使用新的token
					if(userInfoService.validUserToken(username, token)){
						String new_token = OidUtils.newId();
						resp.setNew_token(new_token);
						userInfoService.updateUserToken(username,new_token);//更新数据库
						jsonObject.put("action_result", 0);
						jsonObject.put("msg", "user login success");
					}else{
						error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.TOKEN_IS_ERROR);
						resp.setMessage("token失效，请跳转到登陆页面...");
						jsonObject.put("action_result", 1);
						jsonObject.put("msg", "user login fail");
					}
					//判断用户状态，并根据状态返回url；
					UserInfo userinfo = userInfoService.getUserInfo(username);
					jsonObject.put("stream_id", userinfo.getStream_id());
					int user_status = userinfo.getUser_status();
					resp.setStatus(String.valueOf(user_status));
					resp.setChannel_id(userinfo.getChannel_id());
					switch(user_status){//根据用户状态返回提示，并返回对应的门户地址
					case 1:
						resp.setMessage(UcmsConfig.unbanding_message);
						resp.setUrl(UcmsConfig.mainPage);
						break;
					case 2:
						resp.setMessage(UcmsConfig.banding_message+userinfo.getChannel_id());
						resp.setUrl(userinfo.getVod_page());
						break;
					case 3:
						resp.setMessage(UcmsConfig.play_message+userinfo.getChannel_id());
						resp.setUrl(userinfo.getVod_page());
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
		try {
//			centerService.send(jsonObject.toString());//用户上报数据
			InetAddress ia = InetAddress.getByName(UcmsConfig.sm_service_ip);
			DatagramSocket socket = new DatagramSocket(0);
			socket.connect(ia, UcmsConfig.sm_service_udpport);
			String sendString = jsonObject.toString()+"XXEE";
			DatagramPacket dp = new DatagramPacket(sendString.getBytes(),
					sendString.getBytes().length);
			socket.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
