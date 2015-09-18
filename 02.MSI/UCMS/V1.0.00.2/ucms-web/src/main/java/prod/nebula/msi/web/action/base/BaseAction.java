/** 
 * Project: mtp-web
 * author : PengSong
 * File Created at 2013-11-21 
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
package prod.nebula.msi.web.action.base;

import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.InitializingBean;

import prod.nebula.commons.constans.MsiConstans;
import prod.nebula.commons.constans.MsiRedisKey;
import prod.nebula.dto.response.base.Response;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.service.msi.LicenceService;
import prod.nebula.service.msi.UserInfoService;
import prod.nebula.service.socket.SocketService;
import prod.nebula.service.socket.channelcenter.ChannelCenterBgwServiceImpl;

import com.alibaba.fastjson.JSONObject;
import com.bbcv.mdb.redis.MDBInterface;
import com.bbcv.mdb.redis.impl.MDBDataImpl;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * TODO Comment of BaseAction 
 * 
 * @author PengSong 
 */
public abstract class BaseAction extends ActionSupport implements  ServletRequestAware, ServletResponseAware,InitializingBean{
	private static final long serialVersionUID = 5870834915980739018L;
	private static final Logger logger=Logger.getLogger(BaseAction.class);
	
	protected Map<String, String> paramErrors = new HashMap<String, String>();
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private String login_stb_id = null;
	
	@Resource(name="userInfoService",type=UserInfoService.class)
	protected UserInfoService userInfoService;
	
	@Resource(name="licenceService",type=LicenceService.class)
	protected LicenceService licenceService;
	
	@Resource(name="channelCenterBgwService",type=ChannelCenterBgwServiceImpl.class)
	protected SocketService channelCenterBgwService;
	
	@Resource(name="mdbImpl",type=MDBDataImpl.class)
	protected MDBInterface mdbImpl;
	
	/**
	 * 公共输入参数
	 * 本次通讯唯一编号
	 */
	protected String sequence;
	
	/**
	 * 公共输入参数
	 * 移动终端id
	 */
	protected String mobile_id;
	
	/**
	 * 公共输入参数
	 * 验证码
	 */
	protected String auth_code;
	
	protected String username;
	
	protected String passwd;
	
	protected String appname;
	
	protected String licence;
	
	protected String version;
	
	protected String type;
	
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 公共输入参数
	 * 机顶盒ID
	 */
	protected String stb_id;
	
	/**
	 * 业务方法
	 */
	abstract public void action();
	
	protected void out(String str) {
		PrintWriter out = null;
		try {  
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			//response.setDateHeader("Expires", 0);  
            out = response.getWriter();  
            out.println(str);  
        } catch (IOException e) {
            e.printStackTrace();  
        } finally {
        	if(out != null) {
        		out.flush();  
        		out.close(); 
        	}
        }
	}
	
	protected void out(BaseResponseError error,Response resp) {
		String result = null != error?JSONObject.toJSONString(error):JSONObject.toJSONString(resp);
		logger.debug("response out :"+result);
		out(result);
	}
	
	/**
	 * 根据输入参数term_id,auth_code验证auth_code是否正确
	 * @return
	 */
	protected boolean validAuthCode(){
		if(StringUtils.isNotBlank(mobile_id) && StringUtils.isNotBlank(auth_code)) {
			String cacheAuthCode = mdbImpl.getString(MsiRedisKey.getAuthcodeKey(mobile_id));
			if(null != cacheAuthCode) {
				return cacheAuthCode.equals(auth_code);
			}
		}
		return false;
	}
	
	/**
	 * 将paramErrors map对象值组合成string，用于错误输出
	 * @param fieldErrors
	 * @return
	 */
	protected String paramErrorsToString(){
		StringBuffer sb = new StringBuffer("");
		if(!paramErrors.isEmpty()) {
			Iterator<String> vs = paramErrors.values().iterator();
			while(vs.hasNext()) {
				String str = vs.next();
				sb.append(str).append("  ");
			}
		}
		return sb.toString();
	}
	
	public void validParams() {
		if(StringUtils.isBlank(sequence)) addParamError("sequence", "sequence is not blank!");
		if(StringUtils.isBlank(mobile_id)) addParamError("mobile_id", "mobile_id is not blank!");
		if(StringUtils.isBlank(auth_code)) addParamError("auth_code", "auth_code is not blank!");
	}
	/**
	 * 验证是否post方式提交数据
	 * @return
	 */
	public boolean isHttpPost(){
		String method = getRequest().getMethod();
		return "POST".equalsIgnoreCase(method);
	}
	
	protected boolean hasParamErrors() {
		return !paramErrors.isEmpty();
	}
	
	protected void addParamError(String key,String value){
		paramErrors.put(key, value);
	}
	
	public void afterPropertiesSet() throws Exception {
		notNull(this.mdbImpl, "Property 'mdbImpl' are required");
	}
	
	public HttpSession getSession(){
		HttpSession session = getRequest().getSession();
		if(null != session){
			return session;
		}else{
			session = getRequest().getSession(true);
			return session;
		}
	}
	
	public String getLogin_stb_id() {
		return mdbImpl.getString(MsiRedisKey.getLoginStbIdKey(mobile_id));
	}

	public void setLogin_stb_id(String login_stb_id) {
		mdbImpl.putString(MsiRedisKey.getLoginStbIdKey(mobile_id),MsiConstans.oneDaySeconds, login_stb_id);
	}
	
	public void delLogin_stb_id() {
		mdbImpl.removeString(MsiRedisKey.getLoginStbIdKey(mobile_id));
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}


	public void setServletRequest(HttpServletRequest request) {
		this.request = request;		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;		
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public void setStb_id(String stb_id) {
		this.stb_id = stb_id;
	}

	public void setUserInfoService(UserInfoService userInfoService) {
		this.userInfoService = userInfoService;
	}

	public void setMobile_id(String mobile_id) {
		this.mobile_id = mobile_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
