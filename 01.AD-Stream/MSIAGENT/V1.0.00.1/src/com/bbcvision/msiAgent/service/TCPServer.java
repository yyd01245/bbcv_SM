package com.bbcvision.msiAgent.service;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bbcvision.msiAgent.config.ServerConfiguration;
import com.bbcvision.msiAgent.module.socket.SocketClient;
import com.bbcvision.msiAgent.util.Commons;

public class TCPServer {
	public static final Logger logger = LoggerFactory
			.getLogger(TCPServer.class);

	private static ServerConfiguration config = new ServerConfiguration();

	/** Server应用配置文件名 */
	private final String MSIGW_CONFIG_FILE = "config.properties";
	
	private int source;

	public final static int ENV = 1;

	public final static int OPT = 2;

	public final static int APP = 4;
	
	
	

	public static ServerConfiguration getConfig() {
		return config;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("【VOD网关】开始初始化");
		TCPServer server = new TCPServer();
		try {
			new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		} catch (Exception e) {
			logger.info("【VOD网关】模块加载异常", e);
		}
		logger.info("【VOD网关】各模块加载完毕");

		try {
			server.init(args);

		} catch (Exception e) {
			logger.error("【VOD网关】初始化异常" + e.getMessage());
			System.exit(1);
		}

	}

	/**
	 * 设置启动服务参数
	 * 
	 * @param args
	 *            启动命令行参数
	 * @throws Exception
	 *             如果读取应用配置文件时发生异常
	 */
	private void init(String[] args) throws Exception {
		try {
			initLog4j();
			Properties p = loadConfigFile();
			populate(p);
			logger.info(TCPServer.config.toString());
			SocketClient socketClient = new SocketClient();
			socketClient.start();
		} catch (Exception e) {
			throw new Exception("加载配置文件异常", e);
		}
	}

	public void initLog4j() throws IOException {
		String configFilePath;
		Properties p = null;
		configFilePath = System.getenv("MSIGW_LOG_PATH");
		if (configFilePath != null && !configFilePath.equals("")) {
			p = Commons.loadFile(configFilePath);
			if (p != null) {
				source = TCPServer.ENV;
				PropertyConfigurator.configure(configFilePath);
				return;
			}
		}

		configFilePath = System.getProperty("MSIGW_LOG_PATH");
		if (configFilePath != null && !configFilePath.equals("")) {
			p = Commons.loadFile(configFilePath);
			if (p != null) {
				source = TCPServer.OPT;
				PropertyConfigurator.configure(configFilePath);
				return;
			}
		}
	}

	private Properties loadConfigFile() throws IOException {
		Properties p = null;
		String configFilePath;
		configFilePath = System.getenv("MSIGW_CONFIG_FILE");
		logger.debug("System getenv VGW_CONFIG_FILE=" + configFilePath);
		if (configFilePath != null && !configFilePath.equals("")) {
			p = Commons.loadFile(configFilePath);
			if (p != null) {
				source = TCPServer.ENV;
				return p;
			}
		}

		configFilePath = System.getProperty("MSIGW_CONFIG_FILE");
		logger.debug("System getProperty VGW_CONFIG_FILE=" + configFilePath);
		if (configFilePath != null && !configFilePath.equals("")) {
			p = Commons.loadFile(configFilePath);
			if (p != null) {
				source = TCPServer.OPT;
				return p;
			}
		}
		source = TCPServer.APP;
		return Commons.loadPropertiesFile(MSIGW_CONFIG_FILE);
	}

	private ServerConfiguration populate(Properties p) throws IOException {
		config.setMag_server_ip(Commons.getStringPropertyValue(p, "mag.server.ip", "192.168.100.11"));
		config.setMag_server_port(Commons.getIntPropertyValue(p, "mag.server.port", 18080));
		config.setWec_server_ip(Commons.getStringPropertyValue(p, "wec.server.ip","192.168.100.56"));
		config.setWec_server_port(Commons.getIntPropertyValue(p, "wec.server.port", 20208));
		config.setArea(Commons.getStringPropertyValue(p, "tcp.server.area","home"));
		config.setAuthPage(Commons.getStringPropertyValue(p, "mcs.authpage", "http://218.108.50.246/bbcvcms/uploads/plus/list.php?tid=24"));
		config.setDetailPage(Commons.getStringPropertyValue(p, "mcs.detailpage", "http://218.108.50.246/bbcvcms/uploads/plus/view.php?aid=29&assigntypeid=9"));
		config.setMainPage(Commons.getStringPropertyValue(p, "mcs.mainpage", "http://218.108.50.246/bbcvcms/uploads/plus/list.php?tid=9"));
		config.setSource(source);
		return config;
	}
	
}
