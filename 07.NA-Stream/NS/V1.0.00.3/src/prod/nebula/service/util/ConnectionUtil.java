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
 * 备注：   手机详情页保存的数据库
 * @version       
 */

public class ConnectionUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);
	
	
	static String driver = "com.mysql.jdbc.Driver";  
	// MySQL服务器地址
	static String serverIp = PropertiesUtil.readValue("vod.page.mysql.server.ip");
	// MySQL数据库名称
	static String database = PropertiesUtil.readValue("vod.page.mysql.database");
	// MySQL配置时的端口
	static String port = PropertiesUtil.readValue("vod.page.mysql.server.port");
	// MySQL配置时的用户名
	static String user = PropertiesUtil.readValue("vod.page.mysql.database.user");
	// Java连接MySQL配置时的密码
	static String password = PropertiesUtil.readValue("vod.page.mysql.database.password");

	
	static String url = "jdbc:mysql://"+serverIp+":"+port+"/"+database;

	
	

	

	  public static Connection getConn(){
			// 加载驱动程序
		  Connection conn = null;
		
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);			
				
				if(!conn.isClosed()){
					logger.debug("【NS-ConnectionUtil】数据库【"+url+"】连接成功！");	
				}else{
					logger.error("【NS-ConnectionUtil】数据库【"+url+"】 连接失败");
				}
			} catch (ClassNotFoundException e) {
				logger.error("【NS-ConnectionUtil】数据库【"+url+"】 连接失败 ClassNotFoundException",e);
				//e.printStackTrace();
			}
			catch (SQLException e) {
				logger.error("【NS-ConnectionUtil】数据库【"+url+"】 连接失败！SQLException",e);
				//	e.printStackTrace();
				}
		
		return conn;
	  }


}
