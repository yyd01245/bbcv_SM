package prod.nebula.vgw4vlc.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import prod.nebula.vgw4vlc.config.ServerConfiguration;
import prod.nebula.vgw4vlc.config.TelnetClientServer;
import prod.nebula.vgw4vlc.core.CoreLoader;
import prod.nebula.vgw4vlc.core.common.ResConstants;
import prod.nebula.vgw4vlc.handler.ControllerThread;
import prod.nebula.vgw4vlc.handler.reportType;
import prod.nebula.vgw4vlc.module.resctrl.Constants;
import prod.nebula.vgw4vlc.module.resctrl.Controller;
import prod.nebula.vgw4vlc.module.resctrl.VGWEnum;
import prod.nebula.vgw4vlc.module.resctrl.VODResCtrl;
import prod.nebula.vgw4vlc.util.ApplicationContextHelper;
import prod.nebula.vgw4vlc.util.Commons;
import prod.nebula.vgw4vlc.util.IpUtil;
import prod.nebula.vgw4vlc.util.client.Client;
import prod.nebula.vgw4vlc.util.client.UdpClient;

public class TCPServer {
	public static final Logger logger = LoggerFactory
			.getLogger(TCPServer.class);
	/** UDP接收最大字节 */
	public static final int VRC_READ_BUFFER_SIZE = 4096;

	/** UDP输入缓冲区的大小 */
	public static final int VRC_RECEIVE_BUFFER_SIZE = 1024;

	/** UDP输出缓冲区的大小 */
	public static final int VRC_SEND_BUFFER_SIZE = 1024;

	/** 服务端口 */
	public static final int VRC_PORT = 8571;

	/** 线程池大小 */
	public static final int VRC_POOL_SIZE = 100;

	/** 客户端UDP起始端口 */
	public static final int VRC_CLIENT_BEGIN_UDP_PORT = 10155;

	/** 客户端线程池大小 */
	public static final int VRC_CLIENT_POOL_SIZE = 10;

	/** 可分配端口大小 */
	public static final int VRC_CLIENT_PORT_SIZE = 1000;

	/** 报头 */
	public static final String VRC_DATAGRAM_HEADER = "XXBB";

	/** 报尾 */
	public static final String VRC_DATAGRAM_END = "XXEE";

	/**
	 * 本地IP地址
	 */
	public static final String VRC_LOCALIPADDRESS = "10.0.138.8";

	public static int VRC_CHANGE = 0;

	/** Server应用配置文件名 */
	private final String VRC_CONFIG_FILE = "config.properties";

	private static ServerConfiguration config = new ServerConfiguration();

	// 内存数据,记录用户状态
	private static Map<String, Controller> controllerList = new HashMap<String, Controller>();
	
	//telnet 模拟客户端
	private static TelnetClientServer telnet = new TelnetClientServer();

	
	
	public static TelnetClientServer getTelnet() {
		return telnet;
	}

//	public static void setTelnet(TelnetClientServer telnet) {
//		TCPServer.telnet = telnet;
//	}

	public static ServerConfiguration getConfig() {
		return config;
	}

	public static Map<String, Controller> getControllerList() {
		return controllerList;
	}

	private int source;

	public final static int ENV = 1;

	public final static int OPT = 2;

	public final static int APP = 4;

	public static int beginPort;
	public static int[] portArray;
	public static int seq = 0;
	
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
			server.reset(args);
			if(!TCPServer.getConfig().isReportType()){
				reportType reportThread = new reportType(30);
				reportThread.start();
			}
			int threadTime = TCPServer.getConfig().getThreadTime();
			ControllerThread cThread = new ControllerThread(threadTime);
			cThread.start();
		} catch (Exception e) {
			logger.error("【VOD网关】初始化异常" + e.getMessage());
			System.exit(1);
		}
		logger.info("【VOD网关】初始化完毕,绑定端口:" + TCPServer.config.getPort());
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
			
			String telnetIP = config.getTelnetip();
			int telnetPort = config.getTelnetport();
			String telnetpassword = config.getTelnetpasswd();
			telnet.setIp(telnetIP);
			telnet.setPort(telnetPort);
			telnet.Connect();
			while(!telnet.isConnect()){
				telnet.Connect();
//				logger.info("连接成功?=============="+telnet.isConnect());
			}
			if(telnet.isConnect()){
				logger.info("连接VLC服务器成功,发送认证命令。。。。。。");
				telnet.sendCommand(telnetpassword);
			}
		} catch (Exception e) {
			throw new Exception("加载配置文件异常", e);
		}
	}
	
	private void reset(String[] args) throws Exception {
		Map controllerMap = TCPServer.getControllerList();
		if(!controllerMap.isEmpty()){
			Set controllerSet = controllerMap.keySet();
			Iterator controllerIterator = controllerSet.iterator();
			while (controllerIterator.hasNext()) {
				Controller ctrl = (Controller) controllerMap.get(controllerIterator.next());
				telnet.sendCommand("del "+ctrl.getId());
				TCPServer.getControllerList().remove(ctrl.getSessionId());
			}
		}
	}
	
	 public void initLog4j() throws IOException {
		String configFilePath;
		Properties p = null;
		configFilePath = System.getenv("VGW_LOG_PATH");
		if (configFilePath != null && !configFilePath.equals("")) {
			p = Commons.loadFile(configFilePath);
			if (p != null) {
				source = TCPServer.ENV;
				PropertyConfigurator.configure(configFilePath);
				return;
			}
		}
		
		configFilePath = System.getProperty("VGW_LOG_PATH");
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
		configFilePath = System.getenv("VGW_CONFIG_FILE");
		logger.debug("System getenv VGW_CONFIG_FILE=" + configFilePath);
		if (configFilePath != null && !configFilePath.equals("")) {
			p = Commons.loadFile(configFilePath);
			if (p != null) {
				source = TCPServer.ENV;
				return p;
			}
		}

		configFilePath = System.getProperty("VGW_CONFIG_FILE");
		logger.debug("System getProperty VGW_CONFIG_FILE=" + configFilePath);
		if (configFilePath != null && !configFilePath.equals("")) {
			p = Commons.loadFile(configFilePath);
			if (p != null) {
				source = TCPServer.OPT;
				return p;
			}
		}
		source = TCPServer.APP;
		return Commons.loadPropertiesFile(VRC_CONFIG_FILE);
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private ServerConfiguration populate(Properties p) throws IOException {
		config.setPort(Commons.getIntPropertyValue(p, "vgw.port", VRC_PORT));
		config.setPoolSize(Commons.getIntPropertyValue(p, "vgw.pool.size",
				VRC_POOL_SIZE));
		config.setReadBufferSize(Commons.getIntPropertyValue(p,
				"VRC_READ_BUFFER_SIZE", VRC_READ_BUFFER_SIZE));
		config.setReceiveBufferSize(Commons.getIntPropertyValue(p,
				"VRC_RECEIVE_BUFFER_SIZE", VRC_RECEIVE_BUFFER_SIZE));
		config.setSendBufferSize(Commons.getIntPropertyValue(p,
				"VRC_SEND_BUFFER_SIZE", VRC_SEND_BUFFER_SIZE));
		config.setClientBeginUDPPort(Commons.getIntPropertyValue(p,
				"VRC_CLIENT_BEGIN_UDP_PORT", VRC_CLIENT_BEGIN_UDP_PORT));
		config.setClientPoolSize(Commons.getIntPropertyValue(p,
				"VRC_CLIENT_POOL_SIZE", VRC_CLIENT_POOL_SIZE));
		config.setClientPortSize(Commons.getIntPropertyValue(p,
				"VRC_CLIENT_PORT_SIZE", VRC_CLIENT_PORT_SIZE));
		config.setDatagramHeader(Commons.getStringPropertyValue(p,
				"VRC_DATAGRAM_HEADER", VRC_DATAGRAM_HEADER));
		config.setDatagramEnd(Commons.getStringPropertyValue(p,
				"VRC_DATAGRAM_END", VRC_DATAGRAM_END));
		config.setLocalIpAddress(Commons.getStringPropertyValue(p,
				"vgw.localipaddress", VRC_LOCALIPADDRESS));
//		config.setRegionId(Commons
//				.getStringPropertyValue(p, "VRC_REGIONID", ""));
//		config.setRtspAddr(Commons.getStringPropertyValue(p, "VRC_RTSP_ADDR",
//				""));
//		
//		config.setBwIp(Commons.getStringPropertyValue(p, "bgw.ip", ""));
//		config.setBwPort(Commons.getIntPropertyValue(p, "bgw.port", 0));
//		config.setInterval(Commons.getIntPropertyValue(p,
//				"bgw.heartbeat.interval", 6000));
		config.setVodTimeout(Commons.getIntPropertyValue(p, "vod.timeout",
				86400));
//		config.setDooptionTime(Commons.getIntPropertyValue(p, "dooption.time",
//				20000));
		
		config.setCtasIp(Commons.getStringPropertyValue(p, "ctas.ip",
				"127.0.0.1"));
		config.setCtasPort(Commons.getIntPropertyValue(p, "ctas.port", 20000));
		config.setSource(source);
		config.setForwardTime(Commons.getIntPropertyValue(p, "vod.forward.time", 60));
		config.setThreadTime(Commons.getIntPropertyValue(p, "vod.thread.time", 5));
		config.setTelnetip(Commons.getStringPropertyValue(p, "vlc.telnet.ip", "192.168.100.56"));
		config.setTelnetport(Commons.getIntPropertyValue(p, "vlc.telnet.port", 5000));
		config.setTelnetpasswd(Commons.getStringPropertyValue(p, "vlc.telnet.password", "bbcv"));
		
		config.setReportType(false);
		
		config.setSMserverIP(Commons.getStringPropertyValue(p, "sm.server.ip", "192.168.100.11"));
		config.setSMserverPORT(Commons.getIntPropertyValue(p, "sm.server.port", 20909));
		
		
		return config;
	}

	/**
	 * 向池中重置视频播放控制类
	 * 
	 * @return
	 */
	public static synchronized boolean resetController(Controller ctrl) {
		try {
			String sessionId = ctrl.getSessionId();
			if (!Commons.isNullorEmptyString(sessionId)) {
				controllerList.put(sessionId, ctrl);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 向池中添加视频播放控制类
	 * 
	 * @return
	 */
	public static synchronized boolean putController(Controller ctrl) {
		try {
			String sessionId = ctrl.getSessionId();
			if (!Commons.isNullorEmptyString(sessionId)) {
				Object object = controllerList.get(sessionId);
				if (null != object && object instanceof Controller) {
					Controller temp = (Controller) object;
					VODResCtrl.getInstance().logout(temp);
					temp = null;
//					logger.info("移除原来的控制元素。。。。。。");
				}
				controllerList.put(sessionId, ctrl);
//				logger.info("添加新的控制元素。。。。。。。");
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	 public static void reportBoot(){
		    /*
			 * XXXBB{"cmd":"bootlog","appname":"CSCS","version":"0.9.1","hostip":"218.108.0.1","url":"3305","boottime":"2012-12-12 12:11:00.789"}XXEE
			 */
	        CoreLoader core = (CoreLoader)ApplicationContextHelper.getBean("core");
			String ReportIp = core.getProperties().getProperty(Constants.USERREPORT_IP,
					"127.0.0.1");
		    int ReportPort = Integer.parseInt(core.getProperties().getProperty(Constants.USERREPORT_PORT,
				     "36016"));
		    Client udpClient = new UdpClient();

		    int VGWPort = Integer.parseInt(core.getProperties().getProperty(Constants.VGW_PORT,
				     "8577"));
		    
			Date d = new Date();
			SimpleDateFormat sdf=new SimpleDateFormat(Constants.DATE_FORMAT_DETAIL);
			String currdate=sdf.format(d);
			
		    JSONObject json = new JSONObject();
		    json.put(VGWEnum.CMD.getDesc(), "bootlog");
		    json.put(VGWEnum.APPNAME.getDesc(), "VGW");
		    json.put(VGWEnum.VERSION.getDesc(), ResConstants.VERSION);
		    json.put(VGWEnum.HOSTIP.getDesc(), IpUtil.getIp());
		    json.put(VGWEnum.URL.getDesc(),"VGW访问端口:"+VGWPort);
		    json.put(VGWEnum.BOOTTIME.getDesc(), currdate);
		    udpClient.sendStr(ReportIp, ReportPort, 4000, null, Constants.COM_PREFIX+json.toString()+Constants.COM_SUFFIX);
		}
}
