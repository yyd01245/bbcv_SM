package prod.nebula.service.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**    
 * 类名称：PropertiesUtil   
 * 类描述：  工具类，取出配置参数
 * 创建人：PengFei   
 * 备注：   
 * @version       
 */
public class PropertiesUtil {
    
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

		 // 根据key读取value
		 public static String readValue(String key) {
			 
		   Properties prop = new Properties();

			try {
				
				prop.load(PropertiesUtil.class.getResourceAsStream("../../../../config.properties"));
			} catch (IOException e) {
				logger.error("参数读取错误，IOException");
				e.printStackTrace();
			}
		
		    String value = prop.getProperty (key);
		       
		    return value;

		 }

    public static void main(String[] args) {
		System.out.println(PropertiesUtil.readValue("welcome.page"));
	}
}