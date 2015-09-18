/** 
 * Project: msi-dto
 * author : PengSong
 * File Created at 2013-12-5 
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
package prod.nebula.dto.request.channelcenter.bgw;

import prod.nebula.dto.request.channelcenter.BaseChannelCenterRequest;

/** 
 * TODO Comment of SwitchDvbRequest 
 * 
 * @author PengSong 
 */
public class SwitchDvbRequest extends BaseChannelCenterRequest {

	private static final long serialVersionUID = -5000790112062654823L;
	
	private String termid;
	
	private String termtype;
	
	private String switchtype;
	
	private String freq;
	
	private String serviceid;
	
	private String pid;
	
	private String dvbinfo;

	public SwitchDvbRequest() {
	}
	
	public SwitchDvbRequest(String cmd) {
		super(cmd);
	}

	public String getTermid() {
		return termid;
	}

	public void setTermid(String termid) {
		this.termid = termid;
	}

	public String getTermtype() {
		return termtype;
	}

	public void setTermtype(String termtype) {
		this.termtype = termtype;
	}

	public String getSwitchtype() {
		return switchtype;
	}

	public void setSwitchtype(String switchtype) {
		this.switchtype = switchtype;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getDvbinfo() {
		return dvbinfo;
	}

	public void setDvbinfo(String dvbinfo) {
		this.dvbinfo = dvbinfo;
	}
}
