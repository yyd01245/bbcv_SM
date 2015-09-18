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
package prod.nebula.dto.response.channelcenter;


/** 
 * TODO Comment of RegisterResponse 
 * 
 * @author PengSong 
 */
public class RegisterResponse extends BaseChannelCenterResponse {
	
	private static final long serialVersionUID = -2073551838360664490L;
	
	private String cbgwip;
	
	private String cbgwport;
	
	private String ipsgwip;
	
	private String ipsgwport;
	
	private String dvbsgwip;
	
	private String dvbsgwport;
	
	private String xmppsgwip;
	
	private String xmppsgwport;
	
	private String ilist;
	
	public RegisterResponse() {
	}
	
	public RegisterResponse(String cmd,String serialno) {
		super.setCmd(cmd);
		super.setSerialno(serialno);
	}

	public String getCbgwip() {
		return cbgwip;
	}

	public void setCbgwip(String cbgwip) {
		this.cbgwip = cbgwip;
	}

	public String getCbgwport() {
		return cbgwport;
	}

	public void setCbgwport(String cbgwport) {
		this.cbgwport = cbgwport;
	}

	public String getIpsgwip() {
		return ipsgwip;
	}

	public void setIpsgwip(String ipsgwip) {
		this.ipsgwip = ipsgwip;
	}

	public String getIpsgwport() {
		return ipsgwport;
	}

	public void setIpsgwport(String ipsgwport) {
		this.ipsgwport = ipsgwport;
	}

	public String getDvbsgwip() {
		return dvbsgwip;
	}

	public void setDvbsgwip(String dvbsgwip) {
		this.dvbsgwip = dvbsgwip;
	}

	public String getDvbsgwport() {
		return dvbsgwport;
	}

	public void setDvbsgwport(String dvbsgwport) {
		this.dvbsgwport = dvbsgwport;
	}

	public String getXmppsgwip() {
		return xmppsgwip;
	}

	public void setXmppsgwip(String xmppsgwip) {
		this.xmppsgwip = xmppsgwip;
	}

	public String getXmppsgwport() {
		return xmppsgwport;
	}

	public void setXmppsgwport(String xmppsgwport) {
		this.xmppsgwport = xmppsgwport;
	}

	public String getIlist() {
		return ilist;
	}

	public void setIlist(String ilist) {
		this.ilist = ilist;
	}

}
