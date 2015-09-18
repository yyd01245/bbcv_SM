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
public class UserBindResponse extends BaseMsiResponse {

	private static final long serialVersionUID = 6353681133439203202L;

	private String new_token = "";
	

	public String getNew_token() {
		return new_token;
	}

	public void setNew_token(String new_token) {
		this.new_token = new_token;
	}

	public UserBindResponse() {
	}
	
	public UserBindResponse(String cmd,String sequence) {
		super.setCmd(cmd);
		super.setSequence(sequence);
	}



}
