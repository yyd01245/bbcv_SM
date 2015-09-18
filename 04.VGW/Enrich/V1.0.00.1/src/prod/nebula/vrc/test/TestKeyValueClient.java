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
public class TestKeyValueClient {

	private static final Logger logger = LoggerFactory
			.getLogger(TestKeyValueClient.class);

	/**
	 * 服务器主机IP
	 */
//	private static String HOST = "10.0.86.233";
//	192.168.122.1|9000
//	private static String HOST = "192.168.122.1";
//	private static String HOST = "127.0.0.1";
	private static String HOST = "10.0.86.134";
//	private static String HOST = "10.0.138.8";
	/**
	 * 服务器设置的端口
	 */
	private static int PORT = 9000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
//			XXBBVOD_CTRL_VOD_PLAY_REQ|user_id|region_id|rtspAddr|serviceid|UDPport|serial_noXXEE
//			String sendMessage = "XXBBVOD_CTRL_VOD_PLAY_REQ|123456|0x601|rtsp://218.108.85.235:555/123456.ts|1111|8888|54321XXEE";
			String sendMessage = "XXBBAPP_ENV_CTL_KEY_ACC_REQ|41|1|2XXEE";
//			XXBBVOD_CA_VOD_QUIT_REQ|command|serial_noXXEE
//			String sendMessage = "XXBBVOD_CA_VOD_QUIT_REQ|quit|654321XXEE";
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
