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

/** 
 * TODO MTP配置文件类,属性值通过spring加载
 * 
 * @author PengSong 
 */
public class MtpConfig {
	/**
	 * 数据库连接地址
	 */
	public static String dbSourceUrl;
	
	/**
	 * tcp socket 监听端口 默认值7577
	 */
	public static String tcpServerPort = "7577";
	
	/**
	 * udp socket 监听端口 默认值7578
	 */
	public static String udpServerPort = "7578";
	
	public static boolean useSSL;
	
	/**
	 * redis 缓存服务器ip
	 */
	public static String redisServerHost;
	
	/**
	 * redis 缓存服务器端口
	 */
	public static String redisServerPort;
	
	/**
	 * MGW应用名称
	 */
	public static String mgwAppname;
	
	/**
	 * MGW licence
	 */
	public static String mgwLicence;
	
	/**
	 * activeMq tcp url
	 */
	public static String activeMqTcpUrl;

	public void setDbSourceUrl(String dbSourceUrl) {
		MtpConfig.dbSourceUrl = dbSourceUrl;
	}
	
	public void setTcpServerPort(String tcpServerPort) {
		MtpConfig.tcpServerPort = tcpServerPort;
	}

	public void setUdpServerPort(String udpServerPort) {
		MtpConfig.udpServerPort = udpServerPort;
	}

	public void setUseSSL(boolean useSSL) {
		MtpConfig.useSSL = useSSL;
	}

	public void setRedisServerHost(String redisServerHost) {
		MtpConfig.redisServerHost = redisServerHost;
	}

	public void setRedisServerPort(String redisServerPort) {
		MtpConfig.redisServerPort = redisServerPort;
	}

	public void setMgwAppname(String mgwAppname) {
		MtpConfig.mgwAppname = mgwAppname;
	}

	public void setMgwLicence(String mgwLicence) {
		MtpConfig.mgwLicence = mgwLicence;
	}

	public void setActiveMqTcpUrl(String activeMqTcpUrl) {
		MtpConfig.activeMqTcpUrl = activeMqTcpUrl;
	}
}
