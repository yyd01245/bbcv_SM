package prod.nebula.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.util.PropertiesUtil;

/**
 * 程序启动入口
 * @author PF
 */
public class Server {
	
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	/**
	 * Server 服务启动主函数
	 */
	
	public static void main(String[] args) {
		
		logger.info("服务器开始启动 ");
		ServerSocket serverSocket = null;
		try {
			
			String strPort = PropertiesUtil.readValue("server.port");
			
			serverSocket = new ServerSocket(Integer.parseInt(strPort));
			
			
			
	           while(true){		
	                // 接收客户连接,只要客户进行了连接,就会触发accept()从而建立连接
	                Socket socket = serverSocket.accept();
	               
	             ServerThread st = new ServerThread();
	             st.setSocket(socket);
	             
	             new Thread(st).start();

	        }
		} catch (IOException e) {
			
			logger.error("服务器端出错！"+e.getMessage());
	
		}
			
	 
		    }
	}

	  