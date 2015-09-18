package prod.nebula.vgw4sida.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import prod.nebula.vgw4sida.config.ServerConfiguration;
import prod.nebula.vgw4sida.core.CoreLoader;
import prod.nebula.vgw4sida.core.common.ResConstants;
import prod.nebula.vgw4sida.handler.ControllerThread;
import prod.nebula.vgw4sida.handler.reportType;
import prod.nebula.vgw4sida.module.resctrl.Constants;
import prod.nebula.vgw4sida.module.resctrl.Controller;
import prod.nebula.vgw4sida.module.resctrl.VGWEnum;
import prod.nebula.vgw4sida.module.resctrl.VODResCtrl;
import prod.nebula.vgw4sida.util.ApplicationContextHelper;
import prod.nebula.vgw4sida.util.Commons;
import prod.nebula.vgw4sida.util.IpUtil;
import prod.nebula.vgw4sida.util.client.Client;
import prod.nebula.vgw4sida.util.client.UdpClient;

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
	
	
	//内存数据，记录用户播放状态
	
	
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
			
			String rtspUrl = TCPServer.getConfig().getRegionId();
			if (null != rtspUrl && !"".equals(rtspUrl)) {
				logger.info("regionid="+rtspUrl);
			}
			
			//发送日志上报
//			reportBoot();
			// server.start();
			int threadTime = TCPServer.getConfig().getThreadTime();
			ControllerThread conThread = new ControllerThread(threadTime);
			conThread.start();
			if(!TCPServer.getConfig().isReportType()){
				reportType reportThread = new reportType(30);
				reportThread.start();
			}
		} catch (Exception e) {
			logger.error("【VOD网关】初始化异常" + e.getMessage());
			System.exit(1);
		}
		logger.info("【VOD网关】初始化完毕,绑定端口:" + TCPServer.config.getPort());
	}

	/**
	 * 
	 * @throws IOException
	 * 
	 *             public void start() throws Exception { try {
	 *             logger.info("【VOD网关】主进程开始加载"); SocketAcceptor acceptor = new
	 *             NioSocketAcceptor();// 创建一个TCP的接收器
	 *             IoBuffer.setUseDirectBuffer(false); IoBuffer.setAllocator(new
	 *             SimpleBufferAllocator());
	 * 
	 *             acceptor.setReuseAddress(true);
	 *             acceptor.getSessionConfig().setReuseAddress(true);
	 *             acceptor.getSessionConfig().setKeepAlive(true);
	 *             acceptor.getSessionConfig().setTcpNoDelay(true);
	 *             acceptor.getSessionConfig().setReceiveBufferSize(4096);
	 *             acceptor.getSessionConfig().setMaxReadBufferSize(4096);
	 * 
	 *             // 设置接收器的处理程序 acceptor.setHandler(new ServerHander());
	 *             MinaTextLineCodecFactory lineCodec = new
	 *             MinaTextLineCodecFactory( "UTF-8", "XXEE", "XXEE");
	 *             lineCodec.setDecoderMaxLineLength(4096);
	 *             lineCodec.setEncoderMaxLineLength(4096);
	 * 
	 *             // 建立线程池 Executor threadPool =
	 *             Executors.newFixedThreadPool(config .getPoolSize());
	 *             acceptor.getFilterChain().addLast("exector", new
	 *             ExecutorFilter(threadPool));
	 *             acceptor.getFilterChain().addLast("codec", new
	 *             ProtocolCodecFilter(lineCodec));
	 *             acceptor.getFilterChain().addLast("logger", new
	 *             LoggingFilter());
	 * 
	 *             InetSocketAddress isa = new
	 *             InetSocketAddress(config.getPort()); acceptor.bind(isa);
	 *             TCPServer.beginPort = config.getClientBeginUDPPort();
	 *             TCPServer.portArray = new int[config.getClientPortSize()];
	 *             for (int i = 0; i < TCPServer.portArray.length; i++) {
	 *             TCPServer.portArray[i] = beginPort + i; }
	 *             logger.info("【VOD网关】主进程加载完毕"); } catch (Exception e) { throw
	 *             new Exception("【VOD网关】主进程加载失败！", e); }
	 * 
	 *             }
	 */
	/**
	 * 设置启动服务参数
	 * 
	 * @param args
	 *            启动命令行参数
	 * @throws Exception
	 *             如果读取应用配置文件时发生异常
	 */
	private void init(String[] args) throws Exception {
		TCPServer.beginPort = config.getClientBeginUDPPort();
		TCPServer.portArray = new int[config.getClientPortSize()];
		for (int i = 0; i < TCPServer.portArray.length; i++) {
			TCPServer.portArray[i] = beginPort + i;
		}
		try {
			initLog4j();
			
			Properties p = loadConfigFile();
			populate(p);
			logger.info(TCPServer.config.toString());
		} catch (Exception e) {
			throw new Exception("加载配置文件异常", e);
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
		config.setRegionId(Commons
				.getStringPropertyValue(p, "VRC_REGIONID", ""));
		config.setRtspAddr(Commons.getStringPropertyValue(p, "VRC_RTSP_ADDR",
				""));
		
		config.setVodTimeout(Commons.getIntPropertyValue(p, "vod.timeout",
				86400));
		config.setDooptionTime(Commons.getIntPropertyValue(p, "dooption.time",
				20000));
		
		config.setSource(source);
		config.setForwardTime(Commons.getIntPropertyValue(p, "vod.forward.time", 60));
		config.setThreadTime(Commons.getIntPropertyValue(p, "vod.thread.time", 5));
		
		config.setReportType(false);
		
		config.setSMserverIP(Commons.getStringPropertyValue(p, "sm.server.ip", "192.168.100.11"));
		config.setSMserverPORT(Commons.getIntPropertyValue(p, "sm.server.port", 20909));
		
		
		config.setDBdrivename(Commons.getStringPropertyValue(p, "vgw.mysqlserver.drivename",""));
		config.setDBurl(Commons.getStringPropertyValue(p, "vgw.mysqlserver.url",""));
		config.setDBuser(Commons.getStringPropertyValue(p, "vgw.mysqlserver.username",""));
		config.setDBpasswd(Commons.getStringPropertyValue(p, "vgw.mysqlserver.password",""));
		config.setDBconnecttype(Commons.getIntPropertyValue(p, "vgw.mysqlconn.type",2));
		config.setDBmaxpoolsize(Commons.getIntPropertyValue(p, "vgw.mysqlconn.maxpoolsize",200));
		config.setDBminpoolsize(Commons.getIntPropertyValue(p, "vgw.mysqlconn.minpoolsize",50));
		config.setDBcheckouttimeout(Commons.getIntPropertyValue(p, "vgw.mysqlconn.checkouttimeout",0));
		config.setDBcquireincrement(Commons.getIntPropertyValue(p, "vgw.mysqlconn.acquireincrement",20));
		config.setDBmaxidletime(Commons.getIntPropertyValue(p, "vgw.mysqlconn.maxidletime=",0));
		config.setDBmaxstatements(Commons.getIntPropertyValue(p, "vgw.mysqlconn.maxstatements",0));
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
					logger.info("移除原来的控制元素。。。。。。");
				}
				controllerList.put(sessionId, ctrl);
				logger.info("添加新的控制元素。。。。。。。");
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
