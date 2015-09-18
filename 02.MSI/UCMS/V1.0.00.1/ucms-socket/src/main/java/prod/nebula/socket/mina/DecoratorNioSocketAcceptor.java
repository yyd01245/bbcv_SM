/** 
 * Project: bbcvision3-socket
 * author : PengSong
 * File Created at 2013-11-5 
 *
 * Copyright bbcvision Corporation Limited. 
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * bbcvision company. ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with bbcvision.com. 
 */
package prod.nebula.socket.mina;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/** 
 * TODO Comment of DecoratorNioSocketAcceptor 
 * 
 * @author PengSong 
 */
public class DecoratorNioSocketAcceptor {
	//关闭Nagle算法.立即发包  
	private boolean tcpNoDelay = true;
	//设置socket发包缓冲为32k；  
	private int sendBufferSize = 32*1024;
	
	private int writeTimeout = 100;
	
	private NioSocketAcceptor nioSocketAcceptor;
	
	public void init(){
		//关闭Nagle算法.立即发包  
		this.nioSocketAcceptor.getSessionConfig().setTcpNoDelay(tcpNoDelay);
	    //设置socket发包缓冲为32k；  
		this.nioSocketAcceptor.getSessionConfig().setSendBufferSize(sendBufferSize);
		this.nioSocketAcceptor.getSessionConfig().setWriteTimeout(writeTimeout);
	}
	
	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}

	public void setWriteTimeout(int writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public void setNioSocketAcceptor(NioSocketAcceptor nioSocketAcceptor) {
		this.nioSocketAcceptor = nioSocketAcceptor;
	}
}
