/** 
 * Project: mtp
 * author : PengSong
 * File Created at 2013-10-30 
 *
 * Copyright bbcvision Corporation Limited. 
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * bbcvision company. ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with bbcvision.com. 
 */
package prod.nebula.socket.session.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpClient {
	public static final Logger logger = LoggerFactory.getLogger(UdpClient.class);
	
	public static String sendStr(String ip, int port, int timeoutseconds,String sendStr) {
		String returnStr = "";
		logger.info("\r\n[CORE] [UTIL] udp client sendStr to "+ip+":"+port+":"+sendStr+"\r\n");
		try {  
		    InetAddress ia = InetAddress.getByName(ip);  
		    DatagramSocket socket = new DatagramSocket(0);  
		    socket.connect(ia, port);  
		    byte[] buffer = new byte[1024];  
		    
		    buffer = (sendStr).getBytes();  
		    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);  
		    DatagramPacket dp1 = new DatagramPacket(new byte[1024], 1024);  
		    socket.send(dp);  
	    socket.receive(dp1);  
	    byte[] bb = dp1.getData();
		    returnStr = new String(bb);		    
		 }catch (Exception e) {  
		    e.printStackTrace();  
		 }
		 logger.info("\r\n[CORE] [UTIL] udp client reviceStr:"+returnStr+"\r\n");
		 return returnStr;
	}
}
