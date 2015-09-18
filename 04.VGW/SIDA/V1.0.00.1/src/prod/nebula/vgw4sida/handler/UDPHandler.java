package prod.nebula.vgw4sida.handler;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw4sida.module.resctrl.Controller;
import prod.nebula.vgw4sida.module.resctrl.VODResCtrl;
import prod.nebula.vgw4sida.module.resctrl.dto.VODRecvKeyMsgBean;
import prod.nebula.vgw4sida.util.KeyEnum;

public class UDPHandler extends IoHandlerAdapter {
	public static final Logger logger = LoggerFactory
			.getLogger(UDPHandler.class);

	private Controller ctrl;

	public UDPHandler(Controller ctrl) {
		super();
		this.ctrl = ctrl;
	}

	/**
	 * 当前播放倍速
	 */
	private static int scale = 0;

	/**
	 * @return the scale
	 */
	public static int getScale() {
		return scale;
	}

	/**
	 * @param scale
	 *            the scale to set
	 */
	public static void setScale(int scale) {
		UDPHandler.scale = scale;
	}

	// messageSent是Server响应给Clinet成功后触发的事件
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	// 抛出异常触发的事件
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		session.close(true);
	}

	// Server接收到UDP请求触发的事件
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		IoBuffer buffer = (IoBuffer) message;
		byte[] recvBytes = buffer.array();
		buffer.free();

		VODRecvKeyMsgBean bean = null;

		int devType = getDevType(recvBytes);
		// 判断是否为遥控器设备
		if (devType == KeyEnum.KeyDevType.IrrControl.getDevType()) {
			int seqNum = getInt(recvBytes, 4);
			int keyValue = getInt(recvBytes, 8);
			int keyStatus = getInt(recvBytes, 12);
			// 判断按键是否释放
			if (!isKeyRelease(keyStatus)) {
				
				return;
			}
			bean = new VODRecvKeyMsgBean(devType, seqNum, keyValue, keyStatus);
			logger.info("收到键值：" + keyValue);
		} else {
			return;
		}

		if (bean != null) {
			VODResCtrl.getInstance().VODresDisManage(session,
					String.valueOf(bean.getKeyValue()), ctrl);
		}
	}

	private boolean isKeyRelease(int keyStatus) {
		return keyStatus == KeyEnum.IrrKeyStat.IrrStatUp.getStatus();
	}

	private int getDevType(byte[] bytes) {
		return getInt(bytes, 0);
	}

	private int getInt(byte[] bytes, int startLoc) {
		byte[] buf = new byte[4];
		System.arraycopy(bytes, startLoc, buf, 0, 4);
		return byte2Int(buf);
	}

	private int byte2Int(byte[] buf) {
		int sum = 0;
		for (int i = 0; i < 4; i++) {
			int temp = ((int) buf[i]) & 0xff;

			temp <<= i * 8;
			sum = temp + sum;

		}
		return sum;
	}

	// 连接关闭触发的事件
	@Override
	public void sessionClosed(IoSession session) throws Exception {
	}

	// 建立连接触发的事件
	@Override
	public void sessionCreated(IoSession session) throws Exception {
	}

	// 会话空闲
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	}

	// 打开连接触发的事件，它与sessionCreated的区别在于，一个连接地址（A）第一次请求Server会建立一个Session默认超时时间为1分钟，此时若未达到超时时间这个连接地址（A）再一次向Server发送请求即是sessionOpened（连接地址（A）第一次向Server发送请求或者连接超时后向Server发送请求时会同时触发sessionCreated和sessionOpened两个事件）
	@Override
	public void sessionOpened(IoSession session) throws Exception {
	}
}
