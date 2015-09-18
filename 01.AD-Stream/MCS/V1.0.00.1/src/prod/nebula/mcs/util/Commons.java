package prod.nebula.mcs.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Commons {
	public final static Logger logger = LoggerFactory.getLogger(Commons.class);
	
	public static boolean assertSuccess(List<String> retList){
		boolean ret = false;
		if(retList.size()>0&&Integer.valueOf(retList.get(0))>=0){
			ret = true;
		}
		return ret;
	}
	
	public static String getSerialNo(){
		//String str = "20110908111";
		UUID uuid = UUID.randomUUID();
//		try{
//			Date d=new Date(); 
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss"); 
//			str=sdf.format(d); 
//			String haomiao=String.valueOf(System.nanoTime()); 
//			str=str + haomiao.substring(haomiao.length()-6,haomiao.length()); 
//			str=str + IpUtil.getMACAddress().replaceAll("-", "");
//			str = str.replaceAll(":", "");
//		}catch(Exception e){
//			str="000000000000";
//		}
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
		logger.info("\r\n[CORE] load properties file, the name is: " + file+"\r\n");
		InputStream in = Commons.class.getClassLoader().getResourceAsStream(
				file);
		Properties p = new Properties();
		p.load(in);
		return p;
	}

	public static Properties loadFile(String file) throws IOException{
		try {
			FileInputStream fin  = new FileInputStream(file);
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
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullorEmptyString(String str) {
		return (str == null || str.equals("")) ? true : false;
	}

	public static String getThisDayStr(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHH");
		return sdf.format(cal.getTime());
	}
	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String getLocalIp() throws SocketException {
		String localip=null;//本地IP，如果没有配置外网IP则返回它
        String netip=null;//外网IP
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded=false;//是否找到外网IP
            while(netInterfaces.hasMoreElements() && !finded){
                NetworkInterface ni=netInterfaces.nextElement();
                Enumeration<InetAddress> address=ni.getInetAddresses();
                while(address.hasMoreElements()){
                    ip=address.nextElement();
                    if( !ip.isSiteLocalAddress() && !ip.isLoopbackAddress()&& ip.getHostAddress().indexOf(":")==-1){//外网IP
                        netip=ip.getHostAddress();
                        finded=true;
                        break;
                    }else if(ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){//内网IP
                        localip=ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if(netip!=null && !"".equals(netip)){
            return netip;
        }else{
            return localip;
        }
	}
}
