package prod.nebula.mcs.core.uti.client;

import org.apache.mina.core.service.IoHandlerAdapter;


public interface Client {
	public String sendStr(String ip,int port, int timeoutseconds, IoHandlerAdapter ioHandler, String sendStr,MinaTextLineCodecFactory codecFactory);
}
