/** 
 * Project: msi-dto
 * author : PengSong
 * File Created at 2013-12-5 
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
package prod.nebula.dto.request.channelcenter.bgw;

import com.alibaba.fastjson.JSONObject;

import prod.nebula.dto.request.channelcenter.BaseChannelCenterRequest;

/** 
 * TODO Comment of SwitchDvbRequest 
 * 
 * @author PengSong 
 */
public class CommonRequest extends BaseChannelCenterRequest {

	private static final long serialVersionUID = -5000790112062654823L;
	
	private JSONObject params;
	
	public CommonRequest() {
	}
	
	public CommonRequest(String cmd) {
		super(cmd);
	}

	public JSONObject getParams() {
		return params;
	}

	public void setParams(JSONObject params) {
		this.params = params;
	}
}
