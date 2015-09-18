/** 
 * Project: bbcvision3-dto
 * author : PengSong
 * File Created at 2013-11-5 
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
package prod.nebula.dto.request.mtp;

import com.alibaba.fastjson.JSONObject;

import prod.nebula.dto.request.base.Request;

/** 
 * TODO Comment of BaseMtpRequest 
 * 
 * @author PengSong 
 */
public abstract class BaseMtpRequest implements Request {

	private static final long serialVersionUID = 6185758274932792294L;
	
	/**
	 * 请求指令字
	 */
	private String cmd;
	
	/**
	 * 本次通讯唯一编号
	 */
	private String sequence;
	
	public BaseMtpRequest() {
	}
	
	public BaseMtpRequest(String cmd,String sequence) {
		this.cmd = cmd;
		this.sequence = sequence;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}
}
