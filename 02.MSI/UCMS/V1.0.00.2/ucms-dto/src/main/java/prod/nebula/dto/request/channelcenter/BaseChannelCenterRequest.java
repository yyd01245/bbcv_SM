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
package prod.nebula.dto.request.channelcenter;

import java.util.UUID;

import com.alibaba.fastjson.JSONObject;

import prod.nebula.commons.config.MsiConfig;
import prod.nebula.dto.request.base.Request;


/** 
 * TODO Comment of BaseMtpRequest 
 * 
 * @author PengSong 
 */
public abstract class BaseChannelCenterRequest implements Request {

	private static final long serialVersionUID = 6185758274932792294L;
	
	/**
	 * 操作命令
	 */
	private String cmd;
	
	/**
	 * 操作序列号
	 */
	private String serialno = UUID.randomUUID().toString();
	
	/**
	 * 操作平台鉴权码(由业务网关统一分配的鉴权码，用于认证系统合法性)，终端类型可以不带此参数
	 */
	
	public BaseChannelCenterRequest() {
	}
	
	public BaseChannelCenterRequest(String cmd) {
		this.cmd = cmd;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getSerialno() {
		return serialno;
	}

	
	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}
}
