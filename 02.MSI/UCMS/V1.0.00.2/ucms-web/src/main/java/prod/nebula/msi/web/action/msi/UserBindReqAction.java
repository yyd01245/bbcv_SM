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
import prod.nebula.dto.response.msi.UserBindResponse;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.msi.web.action.base.BaseAction;
import prod.nebula.service.socket.center.CenterStreamBindReqServiceImpl;

/** 
 * 频道绑定
 * 
 * vod_page，用户名，token，流ID
 * 
 * @author zhangdj 
 */
public class UserBindReqAction extends BaseAction {
	
	@Resource(name="msStreamBindReqService",type=CenterStreamBindReqServiceImpl.class)
	private CenterStreamBindReqServiceImpl msStreamBindReqService;
//	@Resource(name="mdbredis",type=MdbRedis.class)
//	private MdbRedis mdbredis;
	
	private static final long serialVersionUID = 5116943721550801750L;
	
	private static final Logger logger=Logger.getLogger(UserBindReqAction.class);
	
	private static final String cmd = MsiCommandEnum.USER_BADN_STREAM.getCmd();
	
	private String username;
	private String token;
	private String vod_page;
	private String stream_id;
	
	public void setVod_page(String vod_page) {
		this.vod_page = vod_page;
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
		if(StringUtils.isBlank(vod_page)) addParamError("vod_page", "vod_page is not blank!");
		if(StringUtils.isBlank(stream_id)) addParamError("stream_id", "stream_id is not blank!");
	}
	
	@Override
	public void action() {
		UserBindResponse resp = new UserBindResponse(cmd, sequence);
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
					String retString = msStreamBindReqService.userBindStream(username, token, stream_id, recall_addr);
					if(retString!=null&&!"".equals(retString)){
						JSONObject json = JSONObject.fromObject(retString);
						String ret_code = json.getString("ret_code");
						String channel_id = json.getString("ChannelInfo");
						/********将门户地址以用户名为主键存入缓存*******/
						String cloudIP = json.getString("strNavIP");	
						String cloudPort =json.getString("iNavPort");	
						mdbImpl.putString("CLOUD_KEY_SEND_IP_"+username, cloudIP);
						mdbImpl.putString("CLOUD_KEY_SEND_PORT_"+username, cloudPort);
						/***************/
						if(ret_code=="0"||"0".equals(ret_code)){
							//与调度中心交互成功
							//检验token，如果正确，重新生成token，后续使用新的token
							String new_token = OidUtils.newId();
							resp.setNew_token(new_token);
							resp.setChannel_id(channel_id);
							userInfoService.updateUserToken(username,new_token);//更新数据库
							//将vod_page、steam_id、channel_id存入数据库
							userInfoService.updateUserVodPage(username,vod_page,stream_id,channel_id,2);//更新数据库
							resp.setStatus("2");
						}else{
							if("weixin".equals(type)||"weixin"==type){
								error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.WEC_USER_BAND_FAIL);
							}else{
								error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.USER_BAND_FAIL);
							}
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
