package prod.nebula.vgw4vlc.util.client;

import org.apache.mina.core.service.IoHandlerAdapter;

public interface Client {
	public String sendStr(String ip,int port,int timeoutSeconds,IoHandlerAdapter ioHandler,String sendStr);
}
