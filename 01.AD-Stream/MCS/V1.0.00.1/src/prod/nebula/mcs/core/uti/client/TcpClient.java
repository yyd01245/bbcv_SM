package prod.nebula.mcs.core.uti.client;

import java.net.ConnectException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.cums.module.cscs.common.MinaTextLineCodecFactory;
import prod.nebula.cums.module.cscs.common.StringUtil;



public class TcpClient{
	public final Logger logger = LoggerFactory.getLogger(getClass());
	
	public String sendStr(String ip, int port, int timeoutseconds,
			IoHandlerAdapter ioHandler, String sendStr,
			MinaTextLineCodecFactory codecFactory, String serialNo) {
		IOSocket socket = null;
		String retStr = "";
		try{
			try{
				logger.debug("[CORE] [UTIL] tcp client send ["+ip+":"+port+"] :" + sendStr + serialNo);
				socket = new IOSocket(ip, port,timeoutseconds);
				byte[] receiveBuffer = new byte[1024];
				socket.sendAndreceive(sendStr.getBytes("UTF-8"), receiveBuffer);
				retStr = new String(receiveBuffer);
				if(StringUtil.assertNotNull(retStr))
					retStr = retStr.trim();
				logger.debug("[CORE] [UTIL] tcp client receive ["+ip+":"+port+"] :"+retStr + serialNo);
			}catch(ConnectException e){
				logger.error("[CORE] [UTIL] tcp client connect timeout ["+ip+":"+port+"]" + serialNo);
			}catch(Exception ex){
				logger.error("[CORE] [UTIL] tcp client exception:" + serialNo +ex);
				if(socket!=null)
					socket.close();
			}finally{
				if(socket!=null)
					socket.close();
			}
		}catch(Exception e){			
			logger.error("[CORE] [UTIL] tcp client error! the server ["+ip+":"+port+"]" + serialNo +e);
		}
		return retStr;
	}

}
