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
	USER_REGIST("user_regist_req"),
	USER_ACCESS("user_access_req"),
	USER_UPDATE_NICKNAME("user_updateNickname_req");
	
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
