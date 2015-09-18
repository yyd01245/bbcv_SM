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

import java.net.InetAddress;
import java.sql.SQLException;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import prod.nebula.commons.config.UcmsConfig;
import prod.nebula.commons.enums.msi.MsiCommandEnum;
import prod.nebula.commons.string.OidUtils;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.dto.response.msi.UserUnBindResponse;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.model.msi.UserInfo;
import prod.nebula.msi.web.action.base.BaseAction;
import prod.nebula.service.socket.center.CenterStreamBindReqServiceImpl;

/** 
 * 频道绑定
 * 
 * vod_page，用户名，token，流ID
 * 
 * @author zhangdj 
 */
public class UserUnBindReqAction extends BaseAction {
	
	@Resource(name="msStreamBindReqService",type=CenterStreamBindReqServiceImpl.class)
	private CenterStreamBindReqServiceImpl msStreamBindReqService;
//	@Resource(name="mdbredis",type=MdbRedis.class)
//	private MdbRedis mdbredis;
	
	private static final long serialVersionUID = 5116943721550801750L;
	
	private static final Logger logger=Logger.getLogger(UserUnBindReqAction.class);
	
	private static final String cmd = MsiCommandEnum.USER_UNBADN_STREAM.getCmd();
	
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
		UserUnBindResponse resp = new UserUnBindResponse(cmd, sequence);
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
				String ip = InetAddress.getLocalHost().getHostAddress();
				String recall_addr = ip+":"+UcmsConfig.local_server_port;//TODO 本地端口需要设置
				if(userInfoService.validUserToken(username, token)){
					UserInfo userInfo = userInfoService.getUserInfo(username);
					String retString = msStreamBindReqService.userUnBindStream(username, userInfo.getStream_id());
					if(retString!=null&&!"".equals(retString)){
						JSONObject json = JSONObject.fromObject(retString);
						String ret_code = json.getString("ret_code");
						if(ret_code=="0"||"0".equals(ret_code)){
							//与调度中心交互成功
							//检验token，如果正确，重新生成token，后续使用新的token
							String new_token = OidUtils.newId();
							resp.setNew_token(new_token);
							userInfoService.updateUserToken(username,new_token);//更新数据库
							//将vod_page、steam_id、channel_id存入数据库
							userInfoService.updateUserVodPage(username,"","","",1);//更新数据库
							resp.setStatus("1");
						}else{
							if("weixin".equals(type)||"weixin"==type){
								error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.WEC_USER_BAND_FAIL);
							}else{
								error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.USER_BAND_FAIL);
							}
						}
					}
				}else{
					if("weixin".equals(type)||"weixin"==type){
						error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.WEC_TOKEN_IS_ERROR);
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
