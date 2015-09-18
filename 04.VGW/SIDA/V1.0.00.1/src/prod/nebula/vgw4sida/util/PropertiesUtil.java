package prod.nebula.vgw4sida.util;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw4sida.service.TCPServer;

public class PropertiesUtil {
	public final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 对应于属性文件的文件对象变量
	 */
	// private File file = null;

	/**
	 * 属性文件的最后修改日期
	 */
	// private long lastModifiedTime = 0;

	/**
	 * 属性文件所对应的属性对象变量
	 */
	private Properties properties = null;

	/**
	 * 文件分割符
	 */
	private static String fileSeperator = "/";

	/**
	 * 本类可能存在的惟一的一个实例
	 */
	private static PropertiesUtil instance = new PropertiesUtil();

	/**
	 * 私有的构造子，用以保证外界无法直接实例化
	 */
	private PropertiesUtil() {
	}

	/**
	 * 静态工厂方法
	 * 
	 * @return 返还PropertiesUtil 类的单一实例
	 */
	synchronized public static PropertiesUtil getInstance() {
		if (null == instance) {
			return instance = new PropertiesUtil();
		}

		return instance;
	}

	/**
	 * 获取class类在工程目录中的地址
	 * 
	 * @return
	 */
	public String getWebClassesPath() {
		String path = getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath();
		return path;

	}

	/**
	 * 获取工程目录中classes目录的地址
	 * 
	 * @return
	 * @throws IllegalAccessException
	 */
	public String getClassesPath() throws IllegalAccessException {
		String path = getWebClassesPath();
		if (path.indexOf("classes") > 0) {
			path = path.substring(0, path.indexOf("classes") + 8);
		} else {
			throw new IllegalAccessException(
					"[ReadConfigation]exception:getClassesPath error!");
		}
		return path;
	}

	/**
	 * 获取配置文件信息
	 * 
	 * @param moduleName
	 *            文件地址 格式 moduleName
	 * @return
	 * @throws IllegalAccessException
	 */
	public Properties getPropertiesWithPath(String path)
			throws IllegalAccessException {
		properties = new Properties();

		try {
			String configFilePath;
			StringBuffer sb = new StringBuffer("");
			configFilePath = System.getenv("VGW_CONFIG_FILE");
			if (configFilePath != null && !configFilePath.equals("")) {
				sb.append(configFilePath);
				properties = Commons.loadFile(sb.toString());
				return properties;
			}
			configFilePath = System.getProperty("VGW_CONFIG_FILE");
			if (configFilePath != null && !configFilePath.equals("")) {
				sb.append(configFilePath);
				properties = Commons.loadFile(sb.toString());
				return properties;
			}
			properties = loadConfigFile(path);
		} catch (Exception e) {
			logger.error("[PropertiesUtil]exception:", e.getMessage());
			throw new IllegalAccessException("[PropertiesUtil]exception:"
					+ e.getMessage());
		}

		return properties;
	}

	/**
	 * 获取配置文件信息
	 * 
	 * @param moduleName
	 *            文件地址 格式 moduleName
	 * @return
	 * @throws IllegalAccessException
	 */
	public Properties getProperties(String moduleName)
			throws IllegalAccessException {
		StringBuffer sb = new StringBuffer("");
		// sb.append(getClassesPath());

		// 目录自工程创建之后不可修改
		// 即： resource/module/modulename/modulename.properties
		sb.append("module");
		sb.append(fileSeperator);
		sb.append(moduleName);
		sb.append(fileSeperator);
		sb.append(moduleName);
		sb.append(".properties");

		properties = new Properties();

		try {
			properties = loadConfigFile(sb.toString());
		} catch (Exception e) {
			logger.error("[PropertiesUtil]exception:", e.getMessage());
			throw new IllegalAccessException("[PropertiesUtil]exception:"
					+ e.getMessage());
		}

		return properties;
	}

	public StringBuffer getModelXmlPath(String moduleName)
			throws IllegalAccessException {
		StringBuffer sb = new StringBuffer("");
		StringBuffer retSb = new StringBuffer("");
		sb.append("module");
		sb.append(fileSeperator);
		sb.append(moduleName);
		sb.append(fileSeperator);
		sb.append(moduleName);
		sb.append(".xml");

		int source = 0;
		source = TCPServer.APP;
		logger.debug("loadfile from " + source);
		return retSb.append(this.getClass().getClassLoader()
				.getResource(sb.toString()).getFile());
	}

	private Properties loadConfigFile(String path) throws IOException {
		int source = 0;
		source = TCPServer.APP;
		logger.debug("loadfile from " + source);
		return Commons.loadPropertiesFile(path);
	}
}
