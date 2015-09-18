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
package prod.nebula.commons.enums.msi;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/** 
 * TODO Comment of ActionTypeEnum 
 * 
 * @author PengSong 
 */
public enum MsiCommandEnum {
	AUTH_CODE("auth_code_req"),
	STB_LOGIN("stb_login_req"),
	BIND_STB("bind_stb_req"),
	UNBIND_STB("unbind_stb_req"),
	QUERY_BIND_STB("querybind_stb_req"),
	COMMAND_SEND("command_send_req"),
	KEY_SEND("key_send_req"),
	STB_LOGOUT("stb_logout_req"),
	QR_REQ("qr_req"),
	QR_QUERY_REQ("qr_query_req"),
	RENAME_STB_REQ("rename_stb_req"),
	GENERATE_REGION("gen_region_req"),
	USER_REGIST("user_regist_req"),
	USER_ACCESS("user_access_req"),
	USER_LOGIN("user_login_req"),
	USER_BADN_STREAM("user_bind_req"),
	USER_UNBADN_STREAM("user_unbind_req"),
	USER_VODPLAY("user_vodplay_req"),
	USER_SESSIONQUERY("user_sessionquery_req"),
	REGION_REQ("region_req");
	
	private String cmd;
	
	private MsiCommandEnum(String cmd) {
		this.cmd = cmd;
	}

	private static Map<String, MsiCommandEnum> actionMap = new HashMap<String, MsiCommandEnum>();

	static {
		MsiCommandEnum[] cmds = MsiCommandEnum.values();
		for (MsiCommandEnum cmd : cmds) {
			actionMap.put(cmd.getCmd(), cmd);
		}
	}

	public static MsiCommandEnum getActionTypeByName(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		return actionMap.get(name);
	}

	public String getCmd() {
		return this.cmd;
	}
}
