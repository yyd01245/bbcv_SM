package util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**    
 * ����ƣ�PropertiesUtil   
 * ��������  �����࣬ȡ�����ò���
 * �����ˣ�PengFei   
 * ��ע��   
 * @version       
 */
public class PropertiesUtil {
    
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

		 // ���key��ȡvalue
		 public static String readValue(String key) {
			 
		   Properties prop = new Properties();

			try {
				
				prop.load(PropertiesUtil.class.getResourceAsStream("../config.properties"));
			} catch (IOException e) {
				logger.error("�����ȡ����IOException");
				e.printStackTrace();
			}
		
		    String value = prop.getProperty (key);
		       
		    return value;

		 }

    public static void main(String[] args) {
		System.out.println(PropertiesUtil.readValue("welcome.page"));
	}
}