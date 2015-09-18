package prod.nebula.vgw.util.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw.util.Commons;

public class TcpClient implements Client {
	public final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String sendStr(String ip, int port, int timeoutseconds, IoHandlerAdapter ioHandler,
			String sendStr) {
		String revStr = "";
		NioSocketConnector connector = null;
		try{			 
			 connector = new NioSocketConnector();
			 connector.setConnectTimeoutMillis(timeoutseconds);
			 TextLineCodecFactory codecFactory = new TextLineCodecFactory(Charset.forName("UTF-8"), "XXEE", "XXEE");
			 connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(codecFactory));
			 SocketSessionConfig cfg = connector.getSessionConfig();
			 cfg.setUseReadOperation(true);
			 logger.debug("【VOD网关】tcp client sendStr to "+ip+":"+port+":"+sendStr);
			 IoSession session = connector.connect(new InetSocketAddress(ip,port)).awaitUninterruptibly().getSession();
			 try {
				 session.write(sendStr).awaitUninterruptibly();
				 ReadFuture readFuture = session.read();
				 if (readFuture.awaitUninterruptibly(2L, TimeUnit.SECONDS)) {
					 revStr = (String)readFuture.getMessage();
					if (!Commons.isNullorEmptyString(revStr)){
						revStr = new String(revStr.getBytes(), "UTF-8");
						revStr = revStr.trim();
						logger.debug("【VOD网关】tcp client reviceStr:"+revStr);
						revStr = revStr.replaceAll("\t", "");
					} else {
						revStr = "";
						logger.debug("【VOD网关】tcp client revice msg is null");
					}
					
				 } else {
					revStr = "";
					logger.debug("【VOD网关】tcp client revice timeout");
				 }
			 }finally {
				session.close(true);
				session.getService().dispose();
			 }
		}catch(Exception e){
			connector.dispose();			
			revStr = "";
			logger.error("【VOD网关】tcp client can not connect to server("+ip+":"+port+")");
		}
		return revStr;
	}
}
