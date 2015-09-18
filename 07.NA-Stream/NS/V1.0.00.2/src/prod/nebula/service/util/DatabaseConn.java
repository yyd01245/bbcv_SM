package prod.nebula.service.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**    
 * 类名称：ConnectionUtil   
 * 类描述：  工具类，数据库连接
 * 创建人：PengFei   
 * 备注：   
 * @version       
 */

public class DatabaseConn {
	
	private static final Logger logger = LoggerFactory.getLogger(DatabaseConn.class);
	
	static String driver = "com.mysql.jdbc.Driver";  
	// MySQL服务器地址
	static String serverIp = PropertiesUtil.readValue("rcm.mysql.server.ip");
	// MySQL数据库名称
	static String database = PropertiesUtil.readValue("rcm.mysql.database");
	// MySQL配置时的端口
	static String port = PropertiesUtil.readValue("rcm.mysql.server.port");
	// MySQL配置时的用户名
	static String user = PropertiesUtil.readValue("rcm.mysql.database.user");
	// Java连接MySQL配置时的密码
	static String password = PropertiesUtil.readValue("rcm.mysql.database.password");

	
	static String url = "jdbc:mysql://"+serverIp+":"+port+"/"+database;
	


	  public static Connection getConn(){
			// 加载驱动程序
		  Connection conn = null;
		
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);			
		
			} catch (ClassNotFoundException e) {
				logger.error("数据库"+url+" 连接失败 ClassNotFoundException！ ",e);
				//e.printStackTrace();
			}
			catch (SQLException e) {
				logger.error("数据库"+url+" 连接失败！SQLException",e);
				//	e.printStackTrace();
				}
		
		return conn;
	  }


}
