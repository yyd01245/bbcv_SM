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
public class KeySendResponse extends BaseMsiResponse {

	private static final long serialVersionUID = -5516878435710377480L;
	
	private String total_time="";
	private String current_time="";
	private String status = "";
	

	public String getTotal_time() {
		return total_time;
	}

	public void setTotal_time(String total_time) {
		this.total_time = total_time;
	}

	public String getCurrent_time() {
		return current_time;
	}

	public void setCurrent_time(String current_time) {
		this.current_time = current_time;
	}

	public KeySendResponse() {
	}
	
	public KeySendResponse(String cmd,String sequence) {
		super.setCmd(cmd);
		super.setSequence(sequence);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
