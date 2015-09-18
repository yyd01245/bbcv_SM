/**  
 * 类名称：SendMessageClient 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-12-15 下午05:16:39 
 */
package org.eredlab.g4.urm.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**   
 * 类名称：SendMessageClient   
 * 类描述： 修改数据库时，通知相关服务器
 * 创建人：PengFei   
 * 创建时间：2014-12-15 下午05:16:40   
 * 备注：   
 * @version    
 *    
 */
public class SendMessageClient {
    

	
	private static final Logger logger =  LoggerFactory.getLogger(SendMessageClient.class);
	
	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	private String  serverIp   = ph.getValue("sm.server.ip");
	private int     serverPort = Integer.parseInt(ph.getValue("sm.server.port"));
	
	private Socket socket = null;
	
	private String changeTable ;
	
	
	
	private String changeType;
	
	
	
	public SendMessageClient(){
			
	}

	public SendMessageClient(String changeTable,String changeType){
		
		this.changeTable=changeTable;
		this.changeType=changeType;
	}


	public String getChangeTable() {
		return changeTable;
	}




	public void setChangeTable(String changeTable) {
		this.changeTable = changeTable;
	}




	public String getChangeType() {
		return changeType;
	}




	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}




	public void send(){
		try {
			socket = new Socket(serverIp, serverPort);
			
			
			OutputStream os = socket.getOutputStream();

			Map<String, String> map = new HashMap<String, String>();
			map.put("changeTable", changeTable);
			map.put("changeType", changeType);
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			
			
			map.put("changeTime", sdf.format(new Date()));

			JSONObject json = JSONObject.fromObject(map);
			String s = json.toString() + "XXEE";

			os.write(s.getBytes());
			 logger.info("【GO-SendMessageClient】向SM服务器【"+serverIp+":"+serverPort+"】发送的报文:"+s);
			 
		    os.flush();//写完后要记得flush  
		    os.close();  
		    socket.close();
		    logger.info("【GO-SendMessageClient】向SM服务器【"+serverIp+":"+serverPort+"】发送成功 ");
		
		} catch (UnknownHostException e) {	
		    logger.info("【GO-SendMessageClient】   与 服务器 【 serverIp="+serverIp+" serverPort="+serverPort+" 】连接失败");
			
		} catch (IOException e) {
			 logger.info("【GO-SendMessageClient】与 服务器 【serverIp="+serverIp+" serverPort="+serverPort+" 】连接失败");
		}
		
		
		
	}
	
	

      public static void main(String[] args) {
		
    	  SendMessageClient s = new SendMessageClient(Contents.AD,Contents.DELETE);  
    	  s.send();
	}

	
	   
}
