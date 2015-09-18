package org.eredlab.g4.common.util.client;

import java.net.ConnectException;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.StringUtil;


public class IOSocketClient  {
	public static final Log logger = LogFactory.getLog(IOSocketClient.class);

	public String sendStr(String ip, int port, int timeoutseconds,
			IoHandlerAdapter ioHandler, String sendStr,
			MinaTextLineCodecFactory codecFactory, Map<String, Object> message) {
		String serialNo = StringUtil.ToBeString(this.getRandomString(16));
		IOSocket socket = null;
		String retStr = "";
		try {
			try {
				logger.info("[CORE] [UTIL] tcp client send [" + ip + ":" + port
						+ "] string:" + sendStr + " serialNo = " + serialNo);
				socket = new IOSocket(ip, port);
//				socket.connect(ip, port, timeoutseconds);
				byte[] receiveBuffer = new byte[4096];
				socket.sendAndreceive(sendStr.getBytes(), receiveBuffer);
				retStr = new String(receiveBuffer);
				if (StringUtil.assertNotNull(retStr))
					retStr = retStr.trim();
				logger.info("[CORE] [UTIL] tcp client recvice [" + ip + ":"
						+ port + "] recvice:" + retStr + " serialNo = "
						+ serialNo);
			} catch (ConnectException e) {
				logger.info("[CORE] [UTIL] tcp client connect timeout [" + ip
						+ ":" + port + "]" + " serialNo = " + serialNo);
			} catch (Exception ex) {
				logger.error("[CORE] [UTIL] tcp client exception:" + ex
						+ " serialNo = " + serialNo);
				if (socket != null)
					socket.close();
			} finally {
				if (socket != null)
					socket.close();
			}
		} catch (Exception e) {
			logger.error("[CORE] [UTIL] tcp client error! the server [" + ip
					+ ":" + port + "]" + "" + e + " serialNo = " + serialNo);
		}
		return retStr;
	}
	public String getRandomString(int length) { 
	    StringBuffer buffer = new StringBuffer("1234567890"); 
	    StringBuffer sb = new StringBuffer(); 
	    Random r = new Random(); 
	    int range = buffer.length(); 
	    for (int i = 0; i < length; i ++) { 
	        sb.append(buffer.charAt(r.nextInt(range))); 
	    } 
	    return sb.toString(); 
	}
}
