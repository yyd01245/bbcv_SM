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
 * TODO Comment of SwitchKeyAppRequest 
 * 
 * @author PengSong 
 */
public class SwitchKeyAppRequest extends BaseChannelCenterRequest {

	private static final long serialVersionUID = -5975999738520306544L;
	
	private String termid;
	
	private String termtype;
	
	private String appaddr;
	
	private String switchtype;
	
	private String switchinfo;
	
	private String url;
	
	private String service;
	
	private String list;
	
	private String ret_url;
	
	private String dvbinfo;

	public SwitchKeyAppRequest() {
	}
	
	public SwitchKeyAppRequest(String cmd) {
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

	public String getAppaddr() {
		return appaddr;
	}

	public void setAppaddr(String appaddr) {
		this.appaddr = appaddr;
	}

	public String getSwitchtype() {
		return switchtype;
	}

	public void setSwitchtype(String switchtype) {
		this.switchtype = switchtype;
	}

	public String getSwitchinfo() {
		return switchinfo;
	}

	public void setSwitchinfo(String switchinfo) {
		this.switchinfo = switchinfo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getRet_url() {
		return ret_url;
	}

	public void setRet_url(String ret_url) {
		this.ret_url = ret_url;
	}

	public String getDvbinfo() {
		return dvbinfo;
	}

	public void setDvbinfo(String dvbinfo) {
		this.dvbinfo = dvbinfo;
	}
}
