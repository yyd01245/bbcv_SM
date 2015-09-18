package prod.nebula.mcs.core.uti.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UdpClient implements Client {
	public final Logger logger = LoggerFactory.getLogger(getClass());
	
	public String sendStr(String ip, int port, int timeoutseconds, IoHandlerAdapter ioHandler,
			String sendStr,MinaTextLineCodecFactory codecFactory) {
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
		 logger.info("\r\n[CORE] [UTIL] tcp client reviceStr:"+returnStr+"\r\n");
		 return returnStr;
	}

}
