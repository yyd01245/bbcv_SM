package org.eredlab.g4.rpm.handler;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.eredlab.g4.bmf.util.SpringBeanLoader;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.StringUtil;
import org.eredlab.g4.rpm.common.RPMConstants;
import org.eredlab.g4.urm.service.DateSynchronizateService;


public class HostAppMonitorHandler extends IoHandlerAdapter  {
	
	private static Log logger = LogFactory.getLog(HostAppMonitorHandler.class);
	
	/**
	 * 从服务容器中获取服务组件
	 * 
	 * @param pBeanName
	 *            :BeanID
	 * @return Object
	 */
	private Object getService(String pBeanId) {
		Object springBean = SpringBeanLoader.getSpringBean(pBeanId);
		return springBean;
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error("[HostAppMonitorHandler]exception:", cause);
	}

	@Override
	public void messageReceived(IoSession ioSession, Object message) {
		String revStr = message.toString();
		logger.info("【RPM】:revice string :" + revStr);
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject revObj = JSONObject.fromObject(revStr);
			String cmd = StringUtil.ToBeString(revObj.get(RPMConstants.COMMAND));
				CommandSupport.COMMAND cc = CommandSupport.COMMAND
						.valueOf(cmd);
				DateSynchronizateService dateSyn = (DateSynchronizateService)getService("dateSynchronizateService");
				switch (cc) {
				case host_info:
					//服务器信息更新
					dateSyn.saveHost(revStr);
					break;
				case cag_info:
					//服务器信息保存
					dateSyn.saveApp(revStr);
					break;
				case app_info:
					//应用信息更新
					dateSyn.updateApp(revStr);
					break;
				case check:
					//服务器信息保存
					dateSyn.saveApp(revStr);
					break;
				}
			
		} catch (Exception e) {
			logger.error("【RPM】:ERROR,"+e.getMessage());
		} finally{
	    	ioSession.close(true);
	    }
	}
}

