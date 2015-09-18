package org.eredlab.g4.common.util.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;


public interface Client {
	
	public String sendStr(String ip,int port, int timeoutseconds, IoHandlerAdapter ioHandler,String sendStr,MinaTextLineCodecFactory codecFactory);
}
