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
package prod.nebula.dto.response.mtp;


/** 
 * TODO Comment of UserRegResponse 
 * 
 * @author PengSong 
 */
public class UserRegResponse extends BaseMtpResponse {

	private static final long serialVersionUID = -481977144582980310L;
	
	private Long userid;
	
	public UserRegResponse() {
	}
	
	public UserRegResponse(String cmd,String sequence) {
		super.setCmd(cmd);
		super.setSequence(sequence);
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}
}
