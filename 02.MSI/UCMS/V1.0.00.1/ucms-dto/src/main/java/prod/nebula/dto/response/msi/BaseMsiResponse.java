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
package prod.nebula.dto.response.msi;

import prod.nebula.dto.response.base.Response;

/** 
 * TODO Comment of BaseMtpResponse 
 * 
 * @author PengSong 
 */
public class BaseMsiResponse implements Response {

	private static final long serialVersionUID = 4818049182906228511L;
	
	private String cmd;
	
	private Integer ret_code = 0;
	
	private String sequence;
	
	public BaseMsiResponse() {
	}
	
	public BaseMsiResponse(String cmd,String sequence) {
		this.cmd = cmd;
		this.sequence = sequence;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Integer getReturn_code() {
		return ret_code;
	}

	public void setReturn_code(Integer return_code) {
		this.ret_code = return_code;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
}
