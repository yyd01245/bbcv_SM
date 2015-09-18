package prod.nebula.vrc.test;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestClietnHandler extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(TestClietnHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
	    IoBuffer buffer = (IoBuffer) message; 
	    byte[] bb = buffer.array();
	    String revStr = new String(bb,"GBK");
		logger.info("客户端接收到的返回信息为：" + revStr);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("客户端发生异常...", cause);
	}
}
