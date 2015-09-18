/** 
 * Project: mtp-model
 * author : PengSong
 * File Created at 2013-11-3 
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
package prod.nebula.dto.response.msi;

import java.util.List;

/** 
 * TODO Comment of UserLoginResponse 
 * 
 * @author PengSong 
 */
public class UserAccessReAction extends BaseMsiResponse {

	private static final long serialVersionUID = 6353681133439203202L;

	private String token = "";

	private String service_url="";
	
	private String username="";
	
	private String passwd="";
	
	private String appname="";
	
	private String licence="";
	
	private String version ="";
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	
	
	public UserAccessReAction() {
	}
	
	public UserAccessReAction(String cmd,String sequence) {
		super.setCmd(cmd);
		super.setSequence(sequence);
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setService_url(String service_url) {
		this.service_url = service_url;
	}

	public String getService_url() {
		return service_url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
