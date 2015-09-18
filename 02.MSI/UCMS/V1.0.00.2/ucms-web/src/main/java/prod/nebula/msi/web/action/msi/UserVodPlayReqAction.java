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
	private String vodname;
	private String posterurl;
	private String offer;
	private String duration;
	private String user_type;
	private String network;
	private String run_time;
	private String enc_type;
	private String biz_type;
	private String sess_gw;
	private String svc_gw;
	private String svc;
	private String svc_type;
	private String retUrl;

	public void setPosterurl(String posterurl) {
		this.posterurl = posterurl;
	}


	public void setVodname(String vodname) {
		this.vodname = vodname;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	
	public void setToken(String token) {
		this.token = token;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public void setMsVodPlayReqService(CenterVodPlayReqImpl msVodPlayReqService) {
		this.msVodPlayReqService = msVodPlayReqService;
	}


	public void setOffer(String offer) {
		this.offer = offer;
	}


	public void setDuration(String duration) {
		this.duration = duration;
	}


	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}


	public void setNetwork(String network) {
		this.network = network;
	}


	public void setRun_time(String run_time) {
		this.run_time = run_time;
	}


	public void setEnc_type(String enc_type) {
		this.enc_type = enc_type;
	}


	public void setBiz_type(String biz_type) {
		this.biz_type = biz_type;
	}


	public void setSess_gw(String sess_gw) {
		this.sess_gw = sess_gw;
	}


	public void setSvc_gw(String svc_gw) {
		this.svc_gw = svc_gw;
	}


	public void setSvc(String svc) {
		this.svc = svc;
	}


	public void setSvc_type(String svc_type) {
		this.svc_type = svc_type;
	}


	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}


	@Override
	public void validParams() {
		if(StringUtils.isBlank(username)) addParamError("username", "username is not blank!");
		if(StringUtils.isBlank(token)) addParamError("token", "token is not blank!");
		if(StringUtils.isBlank(url)) addParamError("url", "url is not blank!");
//		if(StringUtils.isBlank(vodname)) addParamError("vodname", "vodname is not blank!");
//		if(StringUtils.isBlank(posterurl)) addParamError("posterurl", "posterurl is not blank!");
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
					StringBuffer stringbuffer = new StringBuffer();
					if(url.contains("starvod")&&!url.contains("&")){
						stringbuffer.append(url);
						stringbuffer.append("&offer=");
						stringbuffer.append(offer);
						stringbuffer.append("&duration=");
						stringbuffer.append(duration);
						stringbuffer.append("&user_type=0");
						stringbuffer.append("&network=");
						stringbuffer.append(network);
						stringbuffer.append("&run_time=");
						stringbuffer.append(run_time);
						stringbuffer.append("&enc_type=");
						stringbuffer.append(enc_type);
						stringbuffer.append("&enc_info=");
						stringbuffer.append(enc_type);
						stringbuffer.append("&biz_type=");
						stringbuffer.append(biz_type);
						stringbuffer.append("&sess_gw=");
						stringbuffer.append(sess_gw);
						stringbuffer.append("&svc_gw=");
						stringbuffer.append(svc_gw);
						stringbuffer.append("&svc=");
						stringbuffer.append(svc);
						stringbuffer.append("&svc_type=");
						stringbuffer.append(svc_type);
						stringbuffer.append("&user_type=0");
						url=stringbuffer.toString();
					}
					
					//调用MS VOD点播接口
					if(userInfo.getUser_status()==2||userInfo.getUser_status().equals(2)||userInfo.getUser_status()==3||userInfo.getUser_status().equals(3)){
						if(msVodPlayReqService.userVodPlay(username, userInfo.getStream_id(), url,vodname,posterurl,userInfo.getNickname())){
							userInfoService.updateUserToken(username,new_token);//更新数据库
							userInfoService.updateUserStatus(username,UcmsConfig.play_status);
							if("weixin".equals(type)||"weixin"==type){
								resp.setMessage("play vod sucessful,please goback contrl page");
							}else{
								resp.setMessage("点播VOD成功，请跳转到播控界面。。。");
							}
							resp.setStatus("3");
						}else{
							if("weixin".equals(type)||"weixin"==type){
								error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.WEC_VOD_PLAY_FAIL);
							}else{
								error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.VOD_PLAY_FAIL);
							}
						}
					}else{
						if("weixin".equals(type)||"weixin"==type){
							error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.WEC_USER_UNBAND);
						}else{
							error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.USER_UNBAND);
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
