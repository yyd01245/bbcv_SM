package prod.nebula.mcs.core.uti.client;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlTcpClient implements Client{
	public final Logger logger = LoggerFactory.getLogger(getClass());

	public String sendStr(String ip, int port, int timeoutseconds, IoHandlerAdapter ioHandler,String sendStr,MinaTextLineCodecFactory codecFactory) {
		String revStr = "";
		try{			 
			 NioSocketConnector connector = new NioSocketConnector();
			 connector.setConnectTimeoutMillis(timeoutseconds);
			 connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MinaTextLineCodecFactory("UTF-8","</msgreq>","</msgresp>")));
			 SocketSessionConfig cfg = connector.getSessionConfig();
			 cfg.setUseReadOperation(true);
			 logger.debug("\r\n[CORE] [UTIL] tcp client sendStr ["+ip+":"+port+"]:"+sendStr+"\r\n");
			 IoSession session = connector.connect(new InetSocketAddress(ip,port)).awaitUninterruptibly().getSession();
			 try {
				// 发送
				if (sendStr.lastIndexOf("</msgreq>") > 0)
					sendStr = sendStr.substring(0,
							sendStr.lastIndexOf("</msgreq>"));			 
				 session.write(sendStr).awaitUninterruptibly();
				 // 接收
				 ReadFuture readFuture = session.read();
				 if (readFuture.awaitUninterruptibly(10L, TimeUnit.SECONDS)) {
					revStr = (String)readFuture.getMessage();
					revStr = revStr.trim();
					revStr = revStr.replaceAll("\t", "");
					if(revStr.lastIndexOf("</msgresp>")<=0)
						revStr = revStr+"</msgresp>";	
					logger.debug("\r\n[CORE] [UTIL] tcp client reviceStr ["+ip+":"+port+"]:"+revStr+"\r\n");
				 } else {
					// 读超时
					revStr = "";
					logger.info("\r\n[CORE] [UTIL] tcp client revice timeout ["+ip+":"+port+"]"+"\r\n");
				 }
			 }finally {
	           // 断开
				session.close(true);
				session.getService().dispose();
			 }
		}catch(Exception e){
			revStr = "";
		}
		return revStr;
	}
}
