package prod.nebula.vrc.test;

import java.net.InetSocketAddress;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


public class TCPClient extends IoHandlerAdapter {

	static String host = null;
	static String port = null;
//	XXBBVOD_CTRL_VOD_PLAY_REQ|user_id|region_id|rtspAddr|serial_noXXEE
//	String sendMessage = "XXBBVOD_CTRL_VOD_PLAY_REQ|123456|0x601|12345|rtsp://218.108.85.235:555/123456.ts|654321XXEE";
//	String sendMessage = "XXBBVOD_CTRL_VOD_PLAY_REQ|123456|0x601|12345|rtsp://218.108.85.235:555/2012.ts|654321XXEE";
//	String sendMessage = "XXBBVOD_CTRL_VOD_PLAY_REQ|123456|12345|rtsp://218.108.85.235:555/123456.ts|654321XXEE";
//	XXBBVOD_CTRL_VOD_PLAY_CTRL_REQ|keyvalueXXEE
//	String sendMessage = "XXBBAPP_ENV_CTL_KEY_ACC_REQ|40XXEE";
//	XXBBVOD_CA_VOD_QUIT_REQ|command|serial_noXXEE
//	String sendMessage = "XXBBVOD_CA_VOD_QUIT_REQ|quit|654321XXEE";
//	String sendMessage = "XXBBVOD_CTRL_VOD_PLAY_REQ|123456|0x601|12345|rtsp://10.0.138.6:6010/123456.ts|654321XXEE";
//	String sendMessage = "XXBBVOD_CTRL_VOD_PLAY_REQ|123456|0x601|12345|rtsp://10.0.138.6:8843/yjy_ipqam/0808/congldafangong.ts|654321XXEE";
	String sendMessage = "XXBBVOD_CTRL_VOD_PLAY_REQ|123456|0x601|12345|rtsp://10.0.138.6:8843/yjy_ipqam/0808/sls.ts|654321XXEE";

	public TCPClient() {
		System.out.println("Starting Mina TCPClient v2.0...");
		IoBuffer.setUseDirectBuffer(false);
		IoBuffer.setAllocator(new SimpleBufferAllocator());

		SocketConnector connector = new NioSocketConnector();
		connector.getSessionConfig().setTcpNoDelay(true);
		connector.getSessionConfig().setKeepAlive(true);
		connector.getSessionConfig().setReuseAddress(true);
		connector.setConnectTimeoutMillis(2000L);

		/* 建立线程池 */
		Executor threadPool = Executors.newCachedThreadPool();
		connector.getFilterChain().addLast("exector",
				new ExecutorFilter(threadPool));

		connector.setHandler(new TCPHandler());
		try {
			InetSocketAddress lsa = new InetSocketAddress(host,
					Integer.parseInt(port));
			ConnectFuture cf = connector.connect(lsa);
			cf.awaitUninterruptibly();
			if (cf.isConnected()) {
				IoSession session = cf.getSession();
				this.sendMemory(session, sendMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void sendMemory(final IoSession session, String message) {
		try {
			StringBuffer sf = new StringBuffer();
			sf.append(message);
			IoBuffer buffer = IoBuffer.allocate(20480, true);// 分清楚direct和heap方式的缓冲区别
			buffer.setAutoExpand(true);// 自动扩张
			buffer.setAutoShrink(true);// 自动收缩
			buffer.putString(sf.toString(), Charset.forName(("gbk"))
					.newEncoder());
			buffer.flip();
			buffer.free();
			session.write(buffer);
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		host = "127.0.0.1";
//		host = "10.0.138.8";
//		host = "10.0.138.19";
		port = "8571";
		new TCPClient();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("messageSent:" + message.toString());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("sessionClosed:" + session.toString());
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("sessionCreated:" + session.toString());
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("sessionOpened:" + session.toString());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		System.out.println("exceptionCaught:" + cause.toString());
		session.close(true);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("sessionIdle:" + session.toString());
	}

}
