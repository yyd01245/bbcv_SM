package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class DatabaseConn {
    
	private static final Logger logger = LoggerFactory.getLogger(DatabaseConn.class);
	static String driver = "com.mysql.jdbc.Driver";

	
	
	static String url = "jdbc:mysql://192.168.20.230:3306/mydb?characterEncoding=utf8";

	
	static String user = "root";

	
	static String password = "root";

	  public static Connection getConn(){
			
		  Connection conn = null;
		
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);			

			
		} catch (ClassNotFoundException e) {
		
			//e.printStackTrace();
		}
		catch (SQLException e) {
		
			//	e.printStackTrace();
			}
		
		return conn;
	  }

      
  
}
