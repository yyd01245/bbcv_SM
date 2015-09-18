package org.eredlab.g4.common.util.client;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;

public class TcpClient implements Client {
	public static final Log logger = LogFactory.getLog(TcpClient.class);
	
	public String sendStr(String ip, int port, int timeoutseconds, IoHandlerAdapter ioHandler,
			String sendStr,MinaTextLineCodecFactory codecFactory) {
		String revStr = "";
		NioSocketConnector connector = null;
		try{			 
			 connector = new NioSocketConnector();
			 connector.setConnectTimeoutMillis(timeoutseconds);
			 connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(codecFactory));
			 SocketSessionConfig cfg = connector.getSessionConfig();
			 cfg.setUseReadOperation(true);
			 logger.debug("\r\n[CORE] [UTIL] tcp client sendStr ["+ip+":"+port+"]:"+sendStr+"\r\n");
			 IoSession session = connector.connect(new InetSocketAddress(ip,port)).awaitUninterruptibly().getSession();
			 try {
				 if(sendStr.lastIndexOf(codecFactory.getEncodingDelimiter())>0)
					 sendStr = sendStr.substring(0,sendStr.lastIndexOf(codecFactory.getEncodingDelimiter()));				 
				 session.write(sendStr).awaitUninterruptibly();
				 SocketAddress socket = new Socket(ip, port).getLocalSocketAddress();
				 System.out.println(session.write(sendStr).awaitUninterruptibly());
				 ReadFuture readFuture = session.read();				 
				 if (readFuture.awaitUninterruptibly(3L, TimeUnit.SECONDS)) {
					revStr = (String)readFuture.getMessage();
					revStr = revStr.trim();
					revStr = revStr.replaceAll("\t", "");
					if(revStr.lastIndexOf(codecFactory.getDecodingDelimiter())<=0)
						revStr = revStr+codecFactory.getDecodingDelimiter();
					logger.debug("\r\n[CORE] [UTIL] tcp client reviceStr ["+ip+":"+port+"]:"+revStr+"\r\n");
				 } else {
					revStr = "";
					logger.info("\r\n[CORE] [UTIL] tcp client revice timeout ["+ip+":"+port+"]"+"\r\n");
				 }
			 }finally {
				session.close(true);
				session.getService().dispose();
			 }
		}catch(Exception e){
			connector.dispose();			
			revStr = "";
			logger.error("\r\n[CORE] [UTIL] tcp client error! the server ["+ip+":"+port+"]"+"\r\n");
			//e.printStackTrace();
		}
		return revStr;
	}

}
