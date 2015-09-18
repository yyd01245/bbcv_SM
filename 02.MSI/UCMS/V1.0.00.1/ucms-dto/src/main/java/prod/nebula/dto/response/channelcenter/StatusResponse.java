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
public class StatusResponse extends BaseChannelCenterResponse {
	
	private static final long serialVersionUID = -2073551838360664490L;
	
	private String cbgwip;
	
	private Integer cbgwport;
	
	private String ipsgwip;
	
	private Integer ipsgwport;
	
	private String dvbsgwip;
	
	private Integer dvbsgwport;
	
	private String xmppsgwip;
	
	private Integer xmppsgwport;
	
	public StatusResponse() {
	}
	
	public StatusResponse(String cmd,String serialno) {
		super.setCmd(cmd);
		super.setSerialno(serialno);
	}

	public String getCbgwip() {
		return cbgwip;
	}

	public void setCbgwip(String cbgwip) {
		this.cbgwip = cbgwip;
	}

	public Integer getCbgwport() {
		return cbgwport;
	}

	public void setCbgwport(Integer cbgwport) {
		this.cbgwport = cbgwport;
	}

	public String getIpsgwip() {
		return ipsgwip;
	}

	public void setIpsgwip(String ipsgwip) {
		this.ipsgwip = ipsgwip;
	}

	public Integer getIpsgwport() {
		return ipsgwport;
	}

	public void setIpsgwport(Integer ipsgwport) {
		this.ipsgwport = ipsgwport;
	}

	public String getDvbsgwip() {
		return dvbsgwip;
	}

	public void setDvbsgwip(String dvbsgwip) {
		this.dvbsgwip = dvbsgwip;
	}

	public Integer getDvbsgwport() {
		return dvbsgwport;
	}

	public void setDvbsgwport(Integer dvbsgwport) {
		this.dvbsgwport = dvbsgwport;
	}

	public String getXmppsgwip() {
		return xmppsgwip;
	}

	public void setXmppsgwip(String xmppsgwip) {
		this.xmppsgwip = xmppsgwip;
	}

	public Integer getXmppsgwport() {
		return xmppsgwport;
	}

	public void setXmppsgwport(Integer xmppsgwport) {
		this.xmppsgwport = xmppsgwport;
	}
}
