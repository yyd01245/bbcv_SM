package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**    
 * ����ƣ�ConnectionUtil   
 * ��������  �����࣬��ݿ�����
 * �����ˣ�PengFei   
 * ��ע��   
 * @version       
 */

public class ConnectionUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);
	static String driver = "com.mysql.jdbc.Driver";

	// URLָ��Ҫ���ʵ���ݿ���scutcs
	
	static String url = "jdbc:mysql://"+PropertiesUtil.readValue("mysql.url")+":3306/"+PropertiesUtil.readValue("mysql.database");

	// MySQL����ʱ���û���

	static String user = "bbcv";

	// Java����MySQL����ʱ������

	static String password = "bbcv";

	  public static Connection getConn(){
			// ���������
		  Connection conn = null;
		
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);			
				
				if(!conn.isClosed()){
					logger.error("��ݿ����ӳɹ���");	
				}
			} catch (ClassNotFoundException e) {
				logger.error("��ݿ�"+url+" ����ʧ�� ClassNotFoundException�� ",e);
				//e.printStackTrace();
			}
			catch (SQLException e) {
				logger.error("��ݿ�"+url+" ����ʧ�ܣ�SQLException",e);
				//	e.printStackTrace();
				}
		
		return conn;
	  }


}
