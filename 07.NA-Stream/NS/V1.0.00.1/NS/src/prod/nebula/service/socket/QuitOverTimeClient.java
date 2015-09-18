package prod.nebula.service.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.util.PropertiesUtil;

public class QuitOverTimeClient {
	
	private static final Logger logger =  LoggerFactory.getLogger(QuitOverTimeClient.class);
	private String  serverIp   = PropertiesUtil.readValue("z.server.ip");
	private int     serverPort = Integer.parseInt(PropertiesUtil.readValue("z.server.port"));
	
	private Socket socket = null;
	
	
	public void send(String username){
		try {
			socket = new Socket(serverIp, serverPort);
			
			
			OutputStream os = socket.getOutputStream();

			Map<String, String> map = new TreeMap<String, String>();
			map.put("cmd", "quit_timeover");
			map.put("username", username);
			
			SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String serialno = sd.format(new Date());
			map.put("serialno", serialno);

			JSONObject json = JSONObject.fromObject(map);
			String s = json.toString() + "XXEE";

			os.write(s.getBytes());
			 logger.info("【NS-QuitOverTimeClient】向UCMS服务器【"+serverIp+":"+serverPort+"】发送的报文:"+s);
			 
		    os.flush();//写完后要记得flush  
		    os.close();  
		    socket.close();
		    logger.info("【NS-QuitOverTimeClient】向UCMS服务器【"+serverIp+":"+serverPort+"】发送成功 ");
		
		} catch (UnknownHostException e) {	
		    logger.error("【NS-QuitOverTimeClient】[UnknownHostException],与UCMS服务器【"+serverIp+":"+serverPort+"】连接失败");
			
		} catch (IOException e) {
			logger.error("【NS-QuitOverTimeClient】[IOException],与UCMS服务器【"+serverIp+":"+serverPort+"】连接失败");
		}
		
		
		
	}
	
	


}
