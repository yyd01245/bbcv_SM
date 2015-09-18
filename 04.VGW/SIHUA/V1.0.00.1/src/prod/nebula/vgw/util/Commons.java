package prod.nebula.vgw.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参数工具类
 * 
 * @author 严东军
 * 
 */
public class Commons {
	public enum Status {
		init, options, describe, setup, play, pause, scale, teardown,move,choosetime,resume
	}

	private static final Logger logger = LoggerFactory.getLogger(Commons.class);
	
	public static String getSerialNo(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", ""); 
	}

	/**
	 * 获取需要的属性文件并转换成Properties对象。该方法使用Commons类加载器加载属性文件。
	 * 
	 * @param file
	 *            读取的属性文件名称
	 * @return 属性文件对象
	 * @throws IOException
	 *             读取属性文件时发生的IO异常
	 */
	public static Properties loadPropertiesFile(String file) throws IOException {
		logger.info("【VOD网关】开始加载 " + file);
		InputStream in = Commons.class.getClassLoader().getResourceAsStream(
				file);
		Properties p = new Properties();
		p.load(in);
		logger.info("【VOD网关】加载  " + file + " 完毕");
		return p;
	}

	public static Properties loadFile(String file) throws IOException {
		try {
			FileInputStream fin = new FileInputStream(file);
			Properties p = new Properties();
			p.load(fin);
			return p;
		} catch (FileNotFoundException e) {
			return null;
		}

	}

	public static int getIntPropertyValue(Properties p, String name, int def) {
		String value = Commons.getStringPropertyValue(p, name);
		return Commons.isNullorEmptyString(value) ? def : Integer
				.parseInt(value);
	}

	public static String getStringPropertyValue(Properties p, String name) {
		return Commons.getStringPropertyValue(p, name, "");
	}

	public static String getStringPropertyValue(Properties p, String name,
			String def) {
		String value = p.getProperty(name);
		if (Commons.isNullorEmptyString(value)) {
			return Commons.isNullorEmptyString(def) ? "" : def;
		}
		return value;
	}

	/**
	 * 解析消息和关系中以","为间隔的字符串。
	 * 
	 * @param str
	 * @return
	 */
	public static String[] splitString(String str) {
		return splitString(",", 0);
	}

	public static String[] splitString(String str, int limit) {
		return str.split(",", limit);
	}

	/**
	 * 如果为空返回TRUE
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullorEmptyString(String str) {
		return (str == null || str.equals("")) ? true : false;
	}

	public static String getThisDayStr() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		return sdf.format(cal.getTime());
	}

	/**
	 * 转换接收的数据为GBK编码
	 * 
	 * @param message
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getGBKCharactor(Object message)
			throws UnsupportedEncodingException {
		if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
			byte[] bb = buffer.array();
			String revStr = new String(bb, "UTF-8");
			return revStr;
		}

		return "";
	}

	/**
	 * 返回非空字符串
	 * 
	 * @param str
	 * @return String 返回一个非空的字符串
	 */
	public static String getStrValue(String str) {
		return (null == str ? "" : str);
	}
}
