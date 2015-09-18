package prod.nebula.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 程序启动入口
 * 
 * @author PF
 */
public class ServerThread implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(ServerThread.class);
	/**
	 * Server 服务启动主函数
	 */
	private Socket socket;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		InputStream is = null;
		
		OutputStream os = null;
		try {
			is = socket.getInputStream();
			byte b[] = new byte[1024];
			int a = 0;
			int i =0;
			while ((a = is.read(b)) != -1) {
				@SuppressWarnings("unused")
				String s = new String(b, 0, a);
                i++;
                
           String   url =  (String) this.getUrl().get(i);
           
           
           os = socket.getOutputStream();
           
           os.write(url.getBytes());
           if(i>=3){
        	   i=0;
           }
           
           System.out.println("返回请求: "+url);
				//readRequest(s);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();

				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	
	
	public static String readRequest(String str){
		
		System.out.println(str);
		JSONObject json = JSONObject.fromObject(str);
		
		@SuppressWarnings("unused")
		String cmd =(String) json.get("cmd");
		String page =(String) json.get("url");
		
		String url = "http://192.168.30.237:8080/Service1.0/"+page;
		logger.info(" url ="+ url );
		return url;
	}
	
	public List<String> getUrl(){
		
		List<String> list = new  ArrayList<String>();


		   list.add("http://192.168.30.237:8080/Service1.0/vod1.jsp");
		   list.add("http://192.168.30.237:8080/Service1.0/vod11.jsp");
		   list.add("http://192.168.30.237:8080/Service1.0/vod2.jsp");
		   list.add("http://192.168.30.237:8080/Service1.0/vod22.jsp");
	   
	        
	        return list;
	}
	
}
