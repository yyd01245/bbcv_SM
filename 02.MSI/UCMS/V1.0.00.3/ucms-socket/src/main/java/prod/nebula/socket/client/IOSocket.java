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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * TODO Comment of IOSocket
 * 
 * @author PengSong
 */
public class IOSocket {
	private Socket socket;
	private String ip;
	private int port;
	private int timeout = 2000;

	public IOSocket() {
		socket = new Socket();
	}

	public IOSocket(String ip, int port) throws IOException {
		socket = new Socket(ip, port);
		socket.setSoTimeout(timeout);
		socket.setReuseAddress(true);
		this.ip = ip;
		this.port = port;
	}

	public IOSocket(String ip, int port, int timeoutseconds) throws IOException {
		this.timeout = timeoutseconds;
		this.ip = ip;
		this.port = port;

		socket = new Socket();
		SocketAddress add = new InetSocketAddress(ip, port);
		try {
			socket.connect(add, 250);
		} catch (Exception e) {
			throw new ConnectException(e.getMessage());
		}
		socket.setSoTimeout(timeout);
		socket.setReuseAddress(false);
	}

	public void connect() throws IOException {
		socket.connect(new InetSocketAddress(ip, port), timeout);
	}

	public void connect(String ip, int port) throws IOException {
		socket.connect(new InetSocketAddress(ip, port), timeout);
	}

	public void connect(String ip, int port, int timeout) throws IOException {
		socket.connect(new InetSocketAddress(ip, port), timeout);
	}

	public int sendAndreceive(byte[] sendBuffer, byte[] receiveBuffer) throws IOException {
		send(sendBuffer);
		return receive(receiveBuffer);
	}

	private void send(byte[] sendBuffer) throws IOException {
		OutputStream output = socket.getOutputStream();
		output.write(sendBuffer);
	}

	private int receive(byte[] receiveBuffer) throws IOException {
		InputStream input = socket.getInputStream();
		DataInputStream dis = new DataInputStream(input);
		try {
			return dis.read(receiveBuffer);
		} finally {
			dis.close();
			input.close();
		}
	}

	public void close() throws IOException {
		socket.close();
	}

	public boolean isClose() {
		if (socket == null)
			return false;
		return socket.isClosed();
	}

	public static void main(String[] args) {
		try {
			IOSocket socket = new IOSocket("192.168.1.194", 8889);
			byte[] receiveBuffer = new byte[12];
			socket.sendAndreceive("      327001 1 112                  1102".getBytes(), receiveBuffer);
			System.out.println(new String(receiveBuffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
