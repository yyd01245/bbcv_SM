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
 * 通道中心-操作命令指令字符
 * 
 * @author PengSong 
 */
public enum ChannelCommandEnum {
	/**
	 * 上/下行通用请求接口
	 */
	COMMON("common"),
	
	/**
	 * noticeover接口请求(退出)
	 */
	NOTICEOVER("noticeover"),
	
	/**
	 * screennotice接口请求(终端屏显接口)
	 */
	SCREENNOTICE("screennotice"),
	
	/**
	 * 通道中心注册
	 */
	REGISTER("register"),
	/**
	 * 通道中心解除注册
	 */
	UNREGISTER("unregister"),
	/**
	 * 状态上报
	 */
	STATUS("status"),
	
	/**
	 * vod切换
	 */
	SWITCHKEYAPP("switchkeyapp"),
	/**
	 * DVB切台
	 */
	SWITCHDVB("switchdvb"),
	/**
	 * 键值下发
	 */
	KEYDOWN("keydown"),
	
	TERMINFOQUERY("terminfoquery");
	
	private String cmd;
	
	private ChannelCommandEnum(String cmd) {
		this.cmd = cmd;
	}

	private static Map<String, ChannelCommandEnum> actionMap = new HashMap<String, ChannelCommandEnum>();

	static {
		ChannelCommandEnum[] cmds = ChannelCommandEnum.values();
		for (ChannelCommandEnum cmd : cmds) {
			actionMap.put(cmd.getCmd(), cmd);
		}
	}

	public static ChannelCommandEnum getActionTypeByName(String name) {
		if (StringUtils.isBlank(name)) {
			return null;
		}
		return actionMap.get(name);
	}

	public String getCmd() {
		return this.cmd;
	}
}
