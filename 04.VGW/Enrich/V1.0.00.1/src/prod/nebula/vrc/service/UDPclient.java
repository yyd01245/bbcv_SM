package prod.nebula.vrc.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UDPclient {
	public final Logger logger = LoggerFactory.getLogger(getClass());

	public void send(String host, int port, String str) {
		try {
			InetAddress ia = InetAddress.getByName(host);
			DatagramSocket socket = new DatagramSocket(0);
			socket.connect(ia, port);
			byte[] buffer = new byte[1024];

			buffer = (str).getBytes();
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			logger.info("host:" + host + ";" + "port:" + port + ";str:" + str);
//			DatagramPacket dp1 = new DatagramPacket(new byte[1024], 1024);
			socket.send(dp);
//			byte[] bb = dp1.getData();
//			for (int i = 0; i < dp1.getLength(); i++) {
//				logger.info("" + (char) bb[i]);
//			}
		} catch (Exception e) {
			logger.error("¡¾UDPclient·¢ËÍUDPÇëÇóÊ§°Ü¡¿", e);
		}
	}
}
