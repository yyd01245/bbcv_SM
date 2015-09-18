/** 
 * Project: bbcvision3-commons
 * author : PengSong
 * File Created at 2013-11-5 
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
package prod.nebula.commons.enums.mtp;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/** 
 * TODO Comment of ActionTypeEnum 
 * 
 * @author PengSong 
 */
public enum MtpCommandEnum {
	USERLOGIN("userlogin"),
	USERREG("userreg"),
	BINDCODE("bindcode"),
	BINDOK("bindok"),
	BINDQUERY("bindquery"),
	RECDVB("rec_dvb"),
	DVBCHANNELS("dvb_channels"),
	CHANNELITEMS("channel_items"),
	RECVOD("rec_vod"),
	VODSQUERY("vodsquery"),
	COMMENTS("comments"),
	ADDCOMMENT("add_comment"),
	SEARCH("search"),
	PLAY("play"),
	KEYCONTROL("keyControl"),
	VERSION("version"),
	DEFAULTAREAID("defaultAreaid"),
	GETAREAS("getAreas"),
	REGISTERMGW("registermgw"),
	MQTHREAD("mqThread"),
	TRANSACTION_TEST("transTest"),
	SOCKET_TEST("socketTest"),
	WEB_TEST("webtest");
	
	private String cmd;
	
	private MtpCommandEnum(String cmd) {
		this.cmd = cmd;
	}

	private static Map<String, MtpCommandEnum> actionMap = new HashMap<String, MtpCommandEnum>();

	static {
		MtpCommandEnum[] cmds = MtpCommandEnum.values();
		for (MtpCommandEnum cmd : cmds) {
			actionMap.put(cmd.getCmd(), cmd);
		}
	}

	public static MtpCommandEnum getActionTypeByName(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		return actionMap.get(name);
	}

	public String getCmd() {
		return this.cmd;
	}
}
