/** 
 * Project: msi-commons
 * author : PengSong
 * File Created at 2013-12-20 
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
package prod.nebula.commons.enums.msi;

/** 
 * TODO Comment of MgwEnum 
 * 
 * @author PengSong 
 */
public enum MgwEnum {
	CMD("CMD"),
	
	MTAP_AUTHCODE_REQ("MTAP_AUTHCODE_REQ"),
	
	MTAP_SWITCH_REQ("MTAP_SWITCH_REQ"),
	
	MTAP_LOGIN_REQ("MTAP_LOGIN_REQ"),
	
	MTAP_KEYCTRL_REQ("MTAP_KEYCTRL_REQ"),
	
	MTAP_SHOWOSD_REQ("MTAP_SHOWOSD_REQ"),
	
	APP_PLATFORM_EXIST_REQ("APP_PLATFORM_EXIST_REQ"),
	
	Delimiter("|"),
	
	PREFIX("XXBB"),
	
	SUFFIX("XXEE"),
	
	SEQUENCE("sequence"),

	APPNAME("appname"),
	
	LICENCE("licence"),
	
	AUTHCODE("authcode"),
	
	REGION_ID("region_id"),

	STB_ID("stb_id"),
	
	SWITCH_ID("switch_id"),
	
	SWITCH_TYPE("switch_type"),

	ACCT_ID("acct_id"),
	
	KEY_TYPE("key_type"),
	
	KEY_VALUE("key_value"),
	
	OSD_MESSAGE("osd_message"),
	
	MGW_IP("mgw_ip"),
	
	MGW_PORT("mgw_port"),
	;

	private String desc;
	
	private MgwEnum(String desc) {
		this.desc = desc;
	}


	public String getCode() {
		return null;
	}


	public String getDesc() {
		return this.desc;
	}
}
