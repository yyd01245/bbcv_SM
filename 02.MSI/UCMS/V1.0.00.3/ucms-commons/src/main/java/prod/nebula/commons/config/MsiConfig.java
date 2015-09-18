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
package prod.nebula.commons.config;

import java.util.Map;

/** 
 * TODO Msi多屏互动，移动业务平台配置文件类,属性值通过spring加载
 * 
 * @author PengSong 
 */
public class MsiConfig {
	/**
	 * 数据库连接地址
	 */
	public static String dbSourceUrl;
	
	/**
	 * redis 缓存服务器ip
	 */
	public static String redisServerHost;
	
	/**
	 * redis 缓存服务器端口
	 */
	public static String redisServerPort;
	
	public void setDbSourceUrl(String dbSourceUrl) {
		MsiConfig.dbSourceUrl = dbSourceUrl;
	}

	public void setRedisServerHost(String redisServerHost) {
		MsiConfig.redisServerHost = redisServerHost;
	}

	public void setRedisServerPort(String redisServerPort) {
		MsiConfig.redisServerPort = redisServerPort;
	}

}
