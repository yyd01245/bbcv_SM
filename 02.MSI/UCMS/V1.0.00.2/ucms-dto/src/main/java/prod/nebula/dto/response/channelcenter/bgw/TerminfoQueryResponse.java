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
package prod.nebula.dto.response.channelcenter.bgw;

import prod.nebula.dto.response.channelcenter.BaseChannelCenterResponse;

/** 
 * TODO Comment of SwitchKeyAppResponse 
 * 
 * @author PengSong 
 */
public class TerminfoQueryResponse extends BaseChannelCenterResponse {

	private static final long serialVersionUID = 2279814272894163730L;
	
	private String online;
	
	private String onlineinfo;
	
	public TerminfoQueryResponse() {
	}
	
	public TerminfoQueryResponse(String cmd,String serialno) {
		super.setCmd(cmd);
		super.setSerialno(serialno);
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getOnlineinfo() {
		return onlineinfo;
	}

	public void setOnlineinfo(String onlineinfo) {
		this.onlineinfo = onlineinfo;
	}
}
