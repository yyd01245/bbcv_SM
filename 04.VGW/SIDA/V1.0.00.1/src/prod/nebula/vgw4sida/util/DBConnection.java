package prod.nebula.vgw4sida.util;

import java.sql.Connection;
import java.sql.DriverManager;

import prod.nebula.vgw4sida.service.TCPServer;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBConnection {
	

	private static ComboPooledDataSource cpds = null;
	public static Connection getConnection(){
		Connection conn = null;
		try {			
			if(TCPServer.getConfig().getDBconnecttype()==2){
				if(cpds==null){
					cpds = new ComboPooledDataSource();
					cpds.setJdbcUrl(TCPServer.getConfig().getDBurl());
					cpds.setUser(TCPServer.getConfig().getDBuser());
					cpds.setPassword(TCPServer.getConfig().getDBpasswd());
					cpds.setMaxStatements(TCPServer.getConfig().getDBmaxstatements());
					cpds.setMaxIdleTime(TCPServer.getConfig().getDBmaxidletime());
					cpds.setMaxPoolSize(TCPServer.getConfig().getDBmaxpoolsize());
					cpds.setMinPoolSize(TCPServer.getConfig().getDBminpoolsize());
					cpds.setTestConnectionOnCheckin(true);
					cpds.setTestConnectionOnCheckout(false);
					cpds.setIdleConnectionTestPeriod(18000);
				}
				conn = cpds.getConnection();
				conn.setAutoCommit(false);
			}else{
				Class.forName(TCPServer.getConfig().getDBdrivename());
				conn = DriverManager.getConnection(
						TCPServer.getConfig().getDBurl(),
						TCPServer.getConfig().getDBuser(), 
						TCPServer.getConfig().getDBpasswd());
				conn.setAutoCommit(false);
			}
		} catch (Exception e) {
			return null;
		}
		return conn;
	}
}
