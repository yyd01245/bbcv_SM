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
package prod.nebula.dto.response.channelcenter;

import prod.nebula.dto.response.base.Response;

/** 
 * TODO Comment of BaseMtpResponse 
 * 
 * @author PengSong 
 */
public class BaseChannelCenterResponse implements Response {

	private static final long serialVersionUID = 4818049182906228511L;
	
	private String cmd;
	
	private Integer retcode = 0;
	
	private String serialno;
	
	public BaseChannelCenterResponse() {
	}
	
	public BaseChannelCenterResponse(String cmd,String serialno) {
		this.cmd = cmd;
		this.serialno = serialno;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Integer getRetcode() {
		return retcode;
	}

	public void setRetcode(Integer retcode) {
		this.retcode = retcode;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
}
