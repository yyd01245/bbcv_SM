package prod.nebula.vrc.util.client;

import java.net.ConnectException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vrc.util.MinaTextLineCodecFactory;
import prod.nebula.vrc.util.StringUtil;

public class IOSocketClient {
	public final Logger logger = LoggerFactory.getLogger(getClass());

	public String sendStr(String ip, int port, int timeoutseconds,
			IoHandlerAdapter ioHandler, String sendStr,
			MinaTextLineCodecFactory codecFactory) {
		IOSocket socket = null;
		String retStr = "";
		try{
			try{
				logger.debug("[CORE] [UTIL] tcp client send ["+ip+":"+port+"] :"+sendStr+"\r\n");
				socket = new IOSocket(ip, port,timeoutseconds);
				byte[] receiveBuffer = new byte[1024];
				socket.sendAndreceive(sendStr.getBytes(), receiveBuffer);
				retStr = new String(receiveBuffer);
				if(StringUtil.assertNotNull(retStr))
					retStr = retStr.trim();
				logger.debug("[CORE] [UTIL] tcp client receive ["+ip+":"+port+"] :"+retStr+"\r\n");
			}catch(ConnectException e){
				logger.info("[CORE] [UTIL] tcp client connect timeout ["+ip+":"+port+"]"+"\r\n");
			}catch(Exception ex){
				logger.error("[CORE] [UTIL] tcp client exception:"+ex);
				if(socket!=null)
					socket.close();
			}finally{
				if(socket!=null)
					socket.close();
			}
		}catch(Exception e){			
			logger.error("[CORE] [UTIL] tcp client error! the server ["+ip+":"+port+"]"+"\r\n"+e);
		}
		return retStr;
	}

}
