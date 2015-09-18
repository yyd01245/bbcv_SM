package prod.nebula.vrc.handler;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vrc.config.Command;
import prod.nebula.vrc.config.JSONConstants;
import prod.nebula.vrc.config.MsgConstants;
import prod.nebula.vrc.config.VODConst;
import prod.nebula.vrc.module.resctrl.Controller;
import prod.nebula.vrc.module.resctrl.VODResCtrl;
import prod.nebula.vrc.module.resctrl.dto.VodCtrlReq;
import prod.nebula.vrc.module.resdis.VODResDis;
import prod.nebula.vrc.service.TCPServer;
import prod.nebula.vrc.util.Commons;
import prod.nebula.vrc.util.StringUtil;

/**
 * VODM请求处理
 * 
 * @author hj
 * 
 */
public class ServerHander extends IoHandlerAdapter {

	public final Logger logger = LoggerFactory.getLogger(getClass());

	// 抛出异常触发的事件

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.warn("【VOD网关】接收消息处理异常", cause);
		session.close(true);
	}

	// Server接收到TCP请求触发的事件
	@Override
	public void messageReceived(IoSession session, Object msg) throws Exception {
		String revStr = msg.toString();
		Map<String, Object> message = new HashMap<String, Object>();
		
		try {
			JSONObject revObj = JSONObject.fromObject(revStr);
			String serialNo = StringUtil.ToBeString(revObj
					.get(JSONConstants.SERIALNO));
			if (!StringUtil.assertNotNull(serialNo)) {
				serialNo = Commons.getSerialNo();
			}
			logger.info("【VOD代理工具】收到消息:" + revStr + serialNo);

			String cmd = StringUtil.ToBeString(revObj.get(JSONConstants.CMD));
			message.put(MsgConstants.COMMAND, cmd);
			message.put(MsgConstants.SERIALNO, serialNo);
			message.put(MsgConstants.DONEDATE, StringUtil.getNowDate());
			message.put(MsgConstants.APPUSERID,
					StringUtil.ToBeString(revObj.get(JSONConstants.APPUSERID)));
			message.put(MsgConstants.SPUSERID,
					StringUtil.ToBeString(revObj.get(JSONConstants.SPUSERID)));
			message.put(MsgConstants.AREAID,
					StringUtil.ToBeString(revObj.get(JSONConstants.AREAID)));
			Command command = Command.type(cmd);
			switch (command) {
			case LOGIN: {
				message.put(MsgConstants.SPID,
						StringUtil.ToBeString(revObj.get(JSONConstants.SID)));

				message.put(MsgConstants.IP,
						StringUtil.ToBeString(revObj.get(JSONConstants.IIP)));
				message.put(MsgConstants.PORT,
						StringUtil.ToBeString(revObj.get(JSONConstants.IPORT)));

				message.put(MsgConstants.GATEWAYIP,
						StringUtil.ToBeString(revObj.get(JSONConstants.SIP)));
				message.put(MsgConstants.GATEWAYPORT,
						StringUtil.ToBeString(revObj.get(JSONConstants.SPORT)));
				
				message.put(MsgConstants.MESSAGEINFO,
						StringUtil.ToBeString(revObj.get(JSONConstants.MSG)));
				/*
				 * VODResCtrl.getInstance().returnMessage( session,
				 * VODResCtrl.getInstance().getResp(cmd, "", "", "", "", 0, "",
				 * VODConst.VOD_OK));
				 */
				logger.info("【VOD网关】登陆流程开始");
				VODResDis.getInstance().login(session, message);
				logger.info("【VOD网关】登陆流程结束");
			}
				break;
			case LOGOUT: {
				logger.info("【VOD网关】退出流程开始");
				VODResDis.getInstance().logout(session, message);
				logger.info("【VOD网关】退出流程结束");
			}
				break;
			case PLAY: {
				String sessionid = StringUtil.ToBeString(revObj
						.get(JSONConstants.SESSIONID));
				Controller cdnCtrl = VODResCtrl.getInstance()
						.getCtrl(sessionid);
				VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_PLAY,
						cdnCtrl);
				session.write(retRespose(TCPServer.getControllerList().get(
						sessionid)));
				session.close(false);
			}
				break;
			case RESUME: {
				String sessionid = StringUtil.ToBeString(revObj
						.get(JSONConstants.SESSIONID));
				Controller cdnCtrl = VODResCtrl.getInstance()
						.getCtrl(sessionid);
				VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_RESUME,
						cdnCtrl);
				session.write(retRespose(TCPServer.getControllerList().get(
						sessionid)));
				session.close(false);
			}
				break;
			case PAUSE: {
				String sessionid = StringUtil.ToBeString(revObj
						.get(JSONConstants.SESSIONID));
				Controller cdnCtrl = VODResCtrl.getInstance()
						.getCtrl(sessionid);
				VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_PAUSE,
						cdnCtrl);
				session.write(retRespose(TCPServer.getControllerList().get(
						sessionid)));
				session.close(false);
			}
				break;
			case FORWARD: {
				String sessionid = StringUtil.ToBeString(revObj
						.get(JSONConstants.SESSIONID));
				
				Controller cdnCtrl = VODResCtrl.getInstance()
						.getCtrl(sessionid);
				VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_FAST,
						cdnCtrl);
				session.write(retRespose(TCPServer.getControllerList().get(
						sessionid)));
				session.close(false);
			}
				break;
			case BACKWARD: {
				String sessionid = StringUtil.ToBeString(revObj
						.get(JSONConstants.SESSIONID));
				Controller cdnCtrl = VODResCtrl.getInstance()
						.getCtrl(sessionid);
				VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_SLOW,
						cdnCtrl);
				session.write(retRespose(TCPServer.getControllerList().get(
						sessionid)));
				session.close(false);
			}
				break;
			case CHOOSETIME: {
				VodCtrlReq vodctrl = (VodCtrlReq) JSONObject.toBean(revObj,
						VodCtrlReq.class);
				String sessionid = StringUtil.ToBeString(revObj
						.get(JSONConstants.SESSIONID));
				Controller cdnCtrl = VODResCtrl.getInstance()
						.getCtrl(sessionid);
				cdnCtrl.setBeginTime(vodctrl.getBegintime());
				VODResCtrl.getInstance().VODOperate(
						VODConst.KEYVALUE_CHOOSETIME, cdnCtrl);
				session.write(retRespose(TCPServer.getControllerList().get(
						sessionid)));
				session.close(false);
			}
				break;
			case GETTIME: {
				String sessionid = StringUtil.ToBeString(revObj
						.get(JSONConstants.SESSIONID));
				session.write(retRespose(TCPServer.getControllerList().get(
						sessionid)));
				session.close(false);
			}
				break;
			case APP_PLATFORM_EXIST_REQ: {
				session.write("{'retcode':'0'}");
				session.close(false);
			}
				break;
			case SESSION_CHECK: {
				String sessionid = StringUtil.ToBeString(message
						.get(MsgConstants.APPUSERID));
				String code = "-1";
				// 会话检查
				if (TCPServer.getControllerList().get(sessionid) != null) {
					code = "0";
				}
				session.write("{'retcode':'" + code + "'}");
				session.close(false);
			}
				break;
			}
		} catch (Exception e) {
			logger.error("【VOD网关】接收并处理消息异常", e);
		}
	}

	private String retRespose(Controller cdnCtrl) {
		JSONObject retObj = new JSONObject();
		retObj.put(JSONConstants.CURRENTTIME, cdnCtrl.getCurrentTime());
		retObj.put(JSONConstants.TOTALTIME, cdnCtrl.getTotalTime());
		return retObj.toString();
	}

}
