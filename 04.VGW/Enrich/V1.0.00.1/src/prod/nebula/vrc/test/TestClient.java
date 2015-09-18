/**
 * 
 */
package prod.nebula.vrc.test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 * 
 */
public class TestClient {

	private static final Logger logger = LoggerFactory
			.getLogger(TestClient.class);

	/**
	 * 服务器主机IP
	 */
//	private static String HOST = "127.0.0.1";
	private static String HOST = "218.108.0.92";

	/**
	 * 服务器设置的端口
	 */
	private static int PORT = 10571;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
//			XXBBVOD_CTRL_VOD_PLAY_REQ|user_id|region_id|rtspAddr|serviceid|UDPport|serial_noXXEE
			String sendMessage = "XXBBVOD_CTRL_VOD_PLAY_REQ|123456|0x601|rtsp://218.108.85.235:555/123456.ts|654321XXEE";
//			String sendMessage = "XXBBVOD_CTRL_VOD_PLAY_CTRL_REQ|123456|rtsp://218.108.85.235:555/123456.tsXXEE";
			InetAddress ia = InetAddress.getByName(HOST);
			DatagramSocket socket = new DatagramSocket(0);
			socket.connect(ia, PORT);
			byte[] buffer = new byte[1024];

			buffer = (sendMessage).getBytes();
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			socket.send(dp);
		} catch (Exception e) {
			logger.error("【TestClient发送UDP请求失败】", e);
		}
	}

}
