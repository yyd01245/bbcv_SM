package com.bbcvision.Multiscreen.bean;

import java.io.Serializable;

public class UserLoginInfo implements Serializable{
	
	private String username;
	
	
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

	
	private String cmd;
	
	private String message;
	
	private String new_token;
	
	private String return_code;
	
	private String sequence;
	
	private String status;
	
	private String url;
	
	private String stream_id;
	
	
	public String getStream_id() {
		return stream_id;
	}

	public void setStream_id(String stream_id) {
		this.stream_id = stream_id;
	}

	public String getChannel_id() {
		return Channel_id;
	}

	public void setChannel_id(String channel_id) {
		Channel_id = channel_id;
	}

	private String Channel_id;

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
