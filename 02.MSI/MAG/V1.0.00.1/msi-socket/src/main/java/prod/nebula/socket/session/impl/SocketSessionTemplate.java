/** 
 * Project: bbcvision3-socket
 * author : PengSong
 * File Created at 2013-11-28 
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
package prod.nebula.socket.session.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import prod.nebula.socket.client.IOSocket;
import prod.nebula.socket.session.SocketSession;


/** 
 * TODO Comment of SocketSessionTemplate 
 * 
 * @author PengSong 
 */
public class SocketSessionTemplate implements SocketSession {
	private static final Logger logger=Logger.getLogger(SocketSessionTemplate.class);
	
	private String ip;
	
	private Integer port;
	
	private Integer timeout;
	
	private String suffix;
	
	public SocketSessionTemplate() {
	}
	
	public SocketSessionTemplate(String ip,Integer port,Integer timeout,String suffix) {
		this.ip = ip;
		this.port = port;
		this.timeout = timeout;
		this.suffix =suffix;
	}
	
	public String sendStr(String sendStr) throws IOException {
		IOSocket socket = null;
		String retStr = null;
		try {
			if(suffix != null) sendStr = sendStr +suffix;
			logger.debug("[msi-socket] tcp send [" + ip + ":" + port + "] string:" + sendStr + "");
			socket = new IOSocket(ip, port, timeout);
			byte[] receiveBuffer = new byte[102400];
			socket.sendAndreceive(sendStr.getBytes(), receiveBuffer);
			retStr = new String(receiveBuffer);
			logger.debug("[msi-socket] tcp recvice [" + ip + ":" + port + "] recvice:" + retStr + "");
		} finally {
			if(null != socket) socket.close();
		}
		return retStr;
	}

	public String sendSynMsg(byte[] datas) throws IOException {
		InetSocketAddress endpoint = new InetSocketAddress(ip, port);

		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			socket = new Socket();
			// 设置发送逗留时间2秒
			socket.setSoLinger(true, 2);
			// 设置InputStream上调用 read()阻塞超时时间2秒
			socket.setSoTimeout(timeout);
			// 设置socket发包缓冲为32k；
			socket.setSendBufferSize(32 * 1024);
			// 设置socket底层接收缓冲为32k
			socket.setReceiveBufferSize(32 * 1024);
			// 关闭Nagle算法.立即发包
			socket.setTcpNoDelay(true);
			// 连接服务器
			socket.connect(endpoint,250);
			// 获取输出输入流
			out = socket.getOutputStream();
			in = socket.getInputStream();
			// 输出请求
			out.write(datas);
			out.flush();
			// 接收应答
			BufferedReader br = new BufferedReader(new InputStreamReader(in), 4096);
			StringWriter received = new StringWriter(4096);
			char[] charBuf = new char[4096];
			int size = 0;
			char lastChar = 0;
			do {
				size = br.read(charBuf, 0, 4096);
				lastChar = charBuf[size - 1];
				if (lastChar == 0) {
					received.write(charBuf, 0, size - 1);
				}
			} while (lastChar != 0);
			return received.toString();

		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (socket != null) {
				socket.close();
			}
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
