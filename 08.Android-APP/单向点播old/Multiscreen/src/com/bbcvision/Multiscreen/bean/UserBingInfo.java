package com.bbcvision.Multiscreen.bean;

import java.io.Serializable;

/**
 * 
 * @Title: 用户绑定信息
 *		
 * @Description: 
 *		
 * @author Nestor bbcvision.com 
 *		
 * @date 2014-10-14 下午5:40:47
 *		
 * @version V1.0  
 *
 */
public class UserBingInfo implements Serializable{
	/**
	 * 绑定的频道
	 */
	private String channel_id;
	/**
	 * 新的认证码
	 */
	private String new_token;
	/**
	 * 当前状态:1未绑定,2已绑定未点播,3已绑定已点播
	 */
	private String status;
	
	private String return_code;
	
	private String sequence;
	
	private String cmd;
	
	/**
	 * 错误信息，绑定失败才有
	 */
	private String msg;

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getNew_token() {
		return new_token;
	}

	public void setNew_token(String new_token) {
		this.new_token = new_token;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
