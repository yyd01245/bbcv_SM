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


/** 
 * TODO Comment of UserLoginResponse 
 * 
 * @author PengSong 
 */
public class UserSessionQueryResponse extends BaseMsiResponse {

	private static final long serialVersionUID = 6353681133439203202L;

	private String status = "";
	private String message = "";
	
	
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

	public UserSessionQueryResponse() {
	}
	
	public UserSessionQueryResponse(String cmd,String sequence) {
		super.setCmd(cmd);
		super.setSequence(sequence);
	}



}
