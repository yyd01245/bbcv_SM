/** 
 * Project: mtp
 * author : PengSong
 * File Created at 2013-10-29 
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
package prod.nebula.socket.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Comment of IOSocketClient
 * 
 * @author PengSong
 */
public class IOSocketClient {
	public final Logger logger = LoggerFactory.getLogger(IOSocketClient.class);

	private String ip;

	private int port;

	private int timeoutseconds;

	public IOSocketClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public IOSocketClient(String ip, int port, int timeoutseconds) {
		this.ip = ip;
		this.port = port;
		this.timeoutseconds = timeoutseconds;
	}

	public String sendStr(String sendStr) {
		IOSocket socket = null;
		String retStr = "";
		try {
			try {
				logger.info("[CORE] [UTIL] tcp client send [" + ip + ":" + port + "] string:" + sendStr + "");
				socket = new IOSocket(ip, port, timeoutseconds);
				byte[] receiveBuffer = new byte[102400];
				socket.sendAndreceive(sendStr.getBytes(), receiveBuffer);
				retStr = new String(receiveBuffer);
				retStr = retStr.trim();
				logger.info("[CORE] [UTIL] tcp client recvice [" + ip + ":" + port + "] recvice:" + retStr + "");
			} catch (ConnectException e) {
				logger.info("[CORE] [UTIL] tcp client connect timeout [" + ip + ":" + port + "]" + "");
				// e.printStackTrace();
			} catch (Exception ex) {
				logger.error("[CORE] [UTIL] tcp client exception:" + ex);
				if (socket != null)
					socket.close();
			} finally {
				if (socket != null)
					socket.close();
			}
		} catch (Exception e) {
			logger.error("[CORE] [UTIL] tcp client error! the server [" + ip + ":" + port + "]" + "" + e);
		}
		return retStr;
	}

	public String sendSynMsg(byte[] datas) throws Exception {
		InetSocketAddress endpoint = new InetSocketAddress(ip, port);

		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			socket = new Socket();
			// 设置发送逗留时间2秒
			socket.setSoLinger(true, 2);
			// 设置InputStream上调用 read()阻塞超时时间2秒
			socket.setSoTimeout(2000);
			// 设置socket发包缓冲为32k；
			socket.setSendBufferSize(32 * 1024);
			// 设置socket底层接收缓冲为32k
			socket.setReceiveBufferSize(32 * 1024);
			// 关闭Nagle算法.立即发包
			socket.setTcpNoDelay(true);
			// 连接服务器
			socket.connect(endpoint);
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
				try {
					out.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
