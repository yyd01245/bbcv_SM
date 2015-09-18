package prod.nebula.service.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import prod.nebula.service.util.PropertiesUtil;

import net.sf.json.JSONObject;

public class OverTimeClient {
	
	private static final Logger logger =  LoggerFactory.getLogger(OverTimeClient.class);
	private String  serverIp   = PropertiesUtil.readValue("sm.server.ip");
	private int     serverPort = Integer.parseInt(PropertiesUtil.readValue("sm.server.port"));
	
	private Socket socket = null;
	
	
	public void send(String id){
		try {
			socket = new Socket(serverIp, serverPort);
			
			
			OutputStream os = socket.getOutputStream();

			Map<String, String> map = new HashMap<String, String>();
			map.put("cmd", "overtime");
			map.put("streamid", id);

			JSONObject json = JSONObject.fromObject(map);
			String s = json.toString() + "XXEE";

			os.write(s.getBytes());
			 logger.info("【NS-OverTimeClient】向SM服务器【"+serverIp+":"+serverPort+"】发送的报文:"+s);
			 
		    os.flush();//写完后要记得flush  
		    os.close();  
		    socket.close();
		    logger.info("向SM服务器【"+serverIp+":"+serverPort+"】发送成功 ");
		
		} catch (UnknownHostException e) {	
		    logger.info("UnknownHostException   OverTimeClient与 服务器 【 serverIp="+serverIp+" serverPort="+serverPort+" 】连接失败");
			
		} catch (IOException e) {
			 logger.info("IOException   OverTimeClient与 服务器 【serverIp="+serverIp+" serverPort="+serverPort+" 】连接失败");
		}
		
		
		
	}
	
	


}
