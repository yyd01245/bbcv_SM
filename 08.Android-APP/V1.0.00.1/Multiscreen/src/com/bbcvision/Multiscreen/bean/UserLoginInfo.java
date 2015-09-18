package com.bbcvision.Multiscreen.bean;

import java.io.Serializable;

public class UserLoginInfo implements Serializable{
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 接入地址
	 */
	private String service_url;
	
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getService_url() {
		return service_url;
	}

	public void setService_url(String service_url) {
		this.service_url = service_url;
	}

	/**
	 * 请求响应指令字(user_login_req)
	 */
	private String cmd;
	/**
	 * message对应status分别描述提示
	 */
	private String message;
	/**
	 * 新的认证码
	 */
	private String new_token;
	/**
	 * 返回是否成功，值>=0表示成功代码，值<0表示错误代码
	 */
	private String return_code;
	/**
	 * 本次通讯唯一标识
	 */
	private String sequence;
	/**
	 * status: 1未绑定 2已绑定未点播 3已绑定已点播
	 */
	private String status;
	/**
	 * 手机显示的门户地址
	 */
	private String url;
	
	private String stream_id;
	
	private String vodname;
	
	
	public String getVodname() {
		return vodname;
	}

	public void setVodname(String vodname) {
		this.vodname = vodname;
	}

	public String getStream_id() {
		return stream_id;
	}

	public void setStream_id(String stream_id) {
		this.stream_id = stream_id;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
	}

	private String channel_id;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNew_token() {
		return new_token;
	}

	public void setNew_token(String new_token) {
		this.new_token = new_token;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
