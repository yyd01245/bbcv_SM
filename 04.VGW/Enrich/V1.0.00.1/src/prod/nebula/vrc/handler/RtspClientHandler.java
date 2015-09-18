package prod.nebula.vrc.handler;

import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import prod.nebula.vrc.module.resctrl.Controller;

public class RtspClientHandler extends IoHandlerAdapter {
	private Controller ctrl;	
	
	public Controller getCtrl() {
		return ctrl;
	}

	public void setCtrl(Controller ctrl) {
		this.ctrl = ctrl;
	}
	public RtspClientHandler(Controller ctrl) {
		super();
		this.ctrl = ctrl;
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String str = message.toString();
		if( str.trim().equalsIgnoreCase("pause") ) { 
			ctrl.getClient().doPause(); 
		}
		if( str.trim().equalsIgnoreCase("quit") ) { 
			session.close(true);;//结束会话 
			return; 
		}
		//ctrl.setCommand(str);
		Date date = new Date(); 
		session.write("message:"+message + "   " +  date.toString() );//返回当前时间的字符串 
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		session.close(true);
	}
}
