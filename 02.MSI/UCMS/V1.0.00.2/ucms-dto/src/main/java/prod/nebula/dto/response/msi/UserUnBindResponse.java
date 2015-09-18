/** 
 * Project: bbcvision3-dto
 * author : PengSong
 * File Created at 2013-11-27 
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
 * TODO Comment of BindStbResponse 
 * 
 * @author PengSong 
 */
public class UserUnBindResponse extends BaseMsiResponse {

	private static final long serialVersionUID = -5516878435710377480L;
	
	private String new_token = "";
	
	private String status="";

	public UserUnBindResponse() {
	}
	
	public UserUnBindResponse(String cmd,String sequence) {
		super.setCmd(cmd);
		super.setSequence(sequence);
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
	
	
}
