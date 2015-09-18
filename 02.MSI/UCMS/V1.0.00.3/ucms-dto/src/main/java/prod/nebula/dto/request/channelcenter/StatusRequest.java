/** 
 * Project: bbcvision3-dto
 * author : PengSong
 * File Created at 2013-11-28 
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
package prod.nebula.dto.request.channelcenter;



/** 
 * TODO Comment of RegisterRequest 
 * 
 * @author PengSong 
 */
public class StatusRequest extends BaseChannelCenterRequest {

	private static final long serialVersionUID = 5229861405331201112L;
	
	private String areaid = "";
	
	private String ip ="";
	
	private String port ="";
	
	private String msg = "";
	
	public StatusRequest() {
	}
	
	public StatusRequest(String cmd) {
		super(cmd);
	}
	
	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
