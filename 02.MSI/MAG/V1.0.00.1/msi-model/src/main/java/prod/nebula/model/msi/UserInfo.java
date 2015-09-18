/** 
 * Project: msi-model
 * author : PengSong
 * File Created at 2013-12-2 
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
package prod.nebula.model.msi;

import java.util.Date;

import prod.nebula.model.Model;

/** 
 * TODO Comment of UserInfo 
 * 
 * @author zhangdj 
 */
public class UserInfo implements Model {

	private static final long serialVersionUID = -9132353783477213946L;

	private Integer user_id;
	
	private String username;
	
	private String passwd;
	
	private Integer user_status;
	
	private String vod_page;
	
	private String token;
	
	private String stream_id;
	
	private String channel_id;
	
	private String nickname;
	
	public String getStream_id() {
		return stream_id;
	}

	public void setStream_id(String stream_id) {
		this.stream_id = stream_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPasswd() {
		return passwd;
	}




	
	
	public UserInfo() {
	}
	
	public UserInfo(String username) {
		
	}


	public Integer getUser_id() {
		return user_id;
	}


	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}




	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public void setUser_status(Integer user_status) {
		this.user_status = user_status;
	}

	public Integer getUser_status() {
		return user_status;
	}

	public String getVod_page() {
		return vod_page;
	}

	public void setVod_page(String vod_page) {
		this.vod_page = vod_page;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}



	
}
