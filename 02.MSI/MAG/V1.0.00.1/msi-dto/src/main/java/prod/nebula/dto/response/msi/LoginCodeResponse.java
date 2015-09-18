/** 
 * Project: mtp-model
 * author : PengSong
 * File Created at 2013-11-3 
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

import java.util.List;

/** 
 * TODO Comment of UserLoginResponse 
 * 
 * @author PengSong 
 */
public class LoginCodeResponse extends BaseMsiResponse {

	private static final long serialVersionUID = 6353681133439203202L;

	private String status = "";
	
	private String message = "";
	
	private String url = "";
	
	private String new_token = "";
	


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNew_token() {
		return new_token;
	}

	public void setNew_token(String new_token) {
		this.new_token = new_token;
	}

	public LoginCodeResponse() {
	}
	
	public LoginCodeResponse(String cmd,String sequence) {
		super.setCmd(cmd);
		super.setSequence(sequence);
	}



}
