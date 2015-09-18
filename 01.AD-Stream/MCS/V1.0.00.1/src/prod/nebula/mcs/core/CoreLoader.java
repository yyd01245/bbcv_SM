package prod.nebula.mcs.core;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.mcs.config.ServerConfiguration;
import prod.nebula.mcs.core.common.ResConstants;
import prod.nebula.mcs.core.executor.CTRLInterface;
import prod.nebula.mcs.core.executor.RedisInterface;
import prod.nebula.mcs.core.executor.XMEMInterface;
import prod.nebula.mcs.core.executor.impl.DefaultCTRLImpl;
import prod.nebula.mcs.core.executor.impl.DefaultXMEMImpl;
import prod.nebula.mcs.util.Commons;
import prod.nebula.mcs.util.PropertiesUtil;

public class CoreLoader {
	private static final int DEFAULT_CTRLSERVERPROT = 18178;	//服务监听端口
	private static final String MatrixDevicesIP	= "127.0.0.1";	//矩阵设备地址IP
	private static final int MatrixDevicesPort	=8800;			//矩阵设备端口
	private static final String MtpRedisIP	="127.0.0.1";		//MTP缓存地址
	private static final int MtpRedisPORT	=16172;				//MTP缓存端口
	private static final String CSCSRedisIP	="127.0.0.1";		//CSCS缓存地址
	private static final int CSCSRedisPORT	=12172;				//CSCS缓存端口
	private static final String CSCSIP = "127.0.0.1";			//CSCS服务地址

	public final Logger logger = LoggerFactory.getLogger(getClass());

	private Properties properties;
	private CTRLInterface ctrl;
	private XMEMInterface xmemInterface;

	private XMEMInterface cumsxmemInterface;
	
	private RedisInterface mtpmdbInterface;
	
	private RedisInterface cscsmdbInterface;

	public static ServerConfiguration config = new ServerConfiguration();

	public static ServerConfiguration getConfig() {
		return config;
	}

	public Properties getProperties() {
		return properties;
	}

	public CTRLInterface getCtrl() {
		return ctrl;
	}

	public void setCtrl(CTRLInterface ctrl) {
		this.ctrl = ctrl;
	}

	/**
	 * @return the xmemInterface
	 */
	public XMEMInterface getXmemInterface() {
		return xmemInterface;
	}

	/**
	 * @param xmemInterface
	 *            the xmemInterface to set
	 */
	public void setXmemInterface(XMEMInterface xmemInterface) {
		this.xmemInterface = xmemInterface;
	}

	public XMEMInterface getCumsxmemInterface() {
		return cumsxmemInterface;
	}

	public void setCumsxmemInterface(XMEMInterface cumsxmemInterface) {
		this.cumsxmemInterface = cumsxmemInterface;
	}

	public void setMtpmdbInterface(RedisInterface mtpmdbInterface) {
		this.mtpmdbInterface = mtpmdbInterface;
	}

	public RedisInterface getMtpmdbInterface() {
		return mtpmdbInterface;
	}
	
	public CoreLoader() {
		try {
			// 初始化配置参数
			this.properties = this.loadContantsFromClassPath("core.properties");
			this.properties.putAll(this.loadContants("config.properties"));

			// 初始化注入接口
			this.ctrl = new DefaultCTRLImpl();
			this.xmemInterface = new DefaultXMEMImpl();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("\r\n[CORE] [CoreLoader]: load failed , process exit",
					e.getMessage());
			System.exit(0);
		}
	}

	public Properties loadContants(String path) {
		Properties properties = null;
		try {
			properties = PropertiesUtil.getInstance().getPropertiesWithPath(
					path);

			this.populate(properties);

		} catch (Exception e) {
			logger.error("\r\n[CORE] [CoreLoader]loadContants", e.getMessage());
		}
		return properties;
	}

	public Properties loadContantsFromClassPath(String path) {
		Properties properties = null;
		try {
			properties = Commons.loadPropertiesFile(path);
		} catch (Exception e) {
			logger.error("\r\n[CORE] [CoreLoader]loadContants", e.getMessage());
		}
		return properties;
	}


	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private ServerConfiguration populate(Properties p) throws IOException {
		config.setCtrlServerProt(Commons.getIntPropertyValue(p,
				"mcs.tcpserver.port", DEFAULT_CTRLSERVERPROT));
		config.setVersion(ResConstants.VERSION);
		config.setMatrixDevicesIP(Commons.getStringPropertyValue(p, "matrix.tcpserver.ip", MatrixDevicesIP));
		config.setMatrixDevicesPort(Commons.getIntPropertyValue(p, "matrix.tcpserver.port", MatrixDevicesPort));
		config.setMtpRedisIP(Commons.getStringPropertyValue(p, "mtp.redis.server.host",MtpRedisIP));
		config.setMtpRedisPORT(Commons.getIntPropertyValue(p, "mtp.redis.server.port", MtpRedisPORT));
		config.setMtpRedisPass(Commons.getStringPropertyValue(p, "mtp.redis.server.password", ""));
		config.setCscsRedisIP(Commons.getStringPropertyValue(p, "cscs.redis.server.host", CSCSRedisIP));
		config.setCscsRedisPORT(Commons.getIntPropertyValue(p, "cscs.redis.server.port", CSCSRedisPORT));
		config.setCscsRedisPass(Commons.getStringPropertyValue(p, "cscs.redis.server.password", ""));
		config.setCscsIP(Commons.getStringPropertyValue(p, "cscs.tcpserver.ip", CSCSIP));
		logger.info(config.toString());
		return config;
	}

	public void setCscsmdbInterface(RedisInterface cscsmdbInterface) {
		this.cscsmdbInterface = cscsmdbInterface;
	}

	public RedisInterface getCscsmdbInterface() {
		return cscsmdbInterface;
	}

}
