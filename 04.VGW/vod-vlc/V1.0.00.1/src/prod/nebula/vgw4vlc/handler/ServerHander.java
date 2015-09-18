package prod.nebula.vgw4vlc.handler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw4vlc.config.Command;
import prod.nebula.vgw4vlc.config.JSONConstants;
import prod.nebula.vgw4vlc.config.MsgConstants;
import prod.nebula.vgw4vlc.config.VODConst;
import prod.nebula.vgw4vlc.core.common.ResConstants;
import prod.nebula.vgw4vlc.module.resctrl.Controller;
import prod.nebula.vgw4vlc.module.resctrl.VODResCtrl;
import prod.nebula.vgw4vlc.module.resctrl.dto.VodCtrlReq;
import prod.nebula.vgw4vlc.module.resdis.VODResDis;
import prod.nebula.vgw4vlc.service.TCPServer;
import prod.nebula.vgw4vlc.util.Commons;
import prod.nebula.vgw4vlc.util.StringUtil;

/**
 * VODM请求处理
 * 
 * @author hj
 * 
 */
public class ServerHander extends IoHandlerAdapter {

	public final Logger logger = LoggerFactory.getLogger(getClass());
	private final int timeLong = TCPServer.getConfig().getThreadTime();

	// 抛出异常触发的事件

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.warn("【VOD-VLC】接收消息处理异常", cause);
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
			logger.info("【VOD-VLC代理工具】收到消息:" + revStr + serialNo);

			String cmd = StringUtil.ToBeString(revObj.get(JSONConstants.CMD));
			message.put(MsgConstants.COMMAND, cmd);
			message.put(MsgConstants.SERIALNO, serialNo);
			message.put(MsgConstants.DONEDATE, StringUtil.getNowDate());
			message.put(MsgConstants.APPUSERID,
					StringUtil.ToBeString(revObj.get(JSONConstants.APPUSERID)));
			message.put(MsgConstants.SPUSERID,
					StringUtil.ToBeString(revObj.get(JSONConstants.SPUSERID)));
			Command command = Command.type(cmd);
			switch (command) {
			case LOGIN: {
				
				message.put(MsgConstants.GATEWAYIP,
						StringUtil.ToBeString(revObj.get(JSONConstants.SIP)));
				message.put(MsgConstants.GATEWAYPORT,
						StringUtil.ToBeString(revObj.get(JSONConstants.SPORT)));
				
				message.put(MsgConstants.INPUT,
						StringUtil.ToBeString(revObj.get(JSONConstants.INPUT)));
				message.put(MsgConstants.OUTPUT,
						StringUtil.ToBeString(revObj.get(JSONConstants.OUTPUT)));
				
				message.put(JSONConstants.VODNAME,
						StringUtil.ToBeString(revObj.get(JSONConstants.VODNAME)));
				
				message.put(JSONConstants.POSTERURL,
						StringUtil.ToBeString(revObj.get(JSONConstants.POSTERURL)));
				
				message.put(MsgConstants.TOTALTIME,
						StringUtil.ToBeString(revObj.get(MsgConstants.TOTALTIME)));
				
				logger.info("【VOD-VLC】登陆流程开始");
				VODResDis.getInstance().login(session, message);
				logger.info("【VOD-VLC】登陆流程结束");
			}
				break;
			case LOGOUT: {
				logger.info("【VOD-VLC】退出流程开始");
				message.put(JSONConstants.SESSIONID, revObj.get(JSONConstants.SESSIONID));
				VODResDis.getInstance().logout(session, message);
//				String sessionid = StringUtil.ToBeString(revObj
//						.get(JSONConstants.SESSIONID));
//				Controller cdnCtrl = VODResCtrl.getInstance()
//						.getCtrl(sessionid);
//				if(cdnCtrl!=null&&!"".equals(cdnCtrl)){
//					cdnCtrl.setStatus(Commons.Status.teardown);
//					VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_PLAY,
//							cdnCtrl);
//				}
				logger.info("【VOD-VLC】退出流程结束");
			}
				break;
			case PLAY: {
				String sessionid = StringUtil.ToBeString(revObj
						.get(JSONConstants.SESSIONID));
				Controller cdnCtrl = VODResCtrl.getInstance()
						.getCtrl(sessionid);
				VODResCtrl.getInstance().VODOperate(VODConst.KEYVALUE_PLAY,
						cdnCtrl);
				session.write(retRespose("play",TCPServer.getControllerList().get(
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
				session.write(retRespose("resume",TCPServer.getControllerList().get(
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
				session.write(retRespose("pause",TCPServer.getControllerList().get(
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
				session.write(retRespose("forward",TCPServer.getControllerList().get(
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
				session.write(retRespose("backward",TCPServer.getControllerList().get(
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
				String beginTime = StringUtil.ToBeString(revObj
						.get(JSONConstants.BEGINTIME));
				double begintime = Double.parseDouble(beginTime);
				String totalTime = cdnCtrl.getTotalTime();
				double totaltime = Double.parseDouble(totalTime);
				double seek = new Double(begintime/totaltime);
				seek = seek*100;
				NumberFormat numberFormater = new DecimalFormat("0.0000");
				String currentSeek = numberFormater.format(seek);	
				cdnCtrl.setCurrentTime(beginTime);
				cdnCtrl.setSeek(currentSeek);
				VODResCtrl.getInstance().VODOperate(
						VODConst.KEYVALUE_CHOOSETIME, cdnCtrl);
				session.write(retRespose("choosetime",TCPServer.getControllerList().get(
						sessionid)));
				session.close(false);
			}
				break;
			case GETTIME: {
				String sessionid = StringUtil.ToBeString(revObj
						.get(JSONConstants.SESSIONID));
				Controller cdnCtrl = VODResCtrl.getInstance()
						.getCtrl(sessionid);
				if(cdnCtrl!=null&&!"".equals(cdnCtrl)){
					long currentTime = System.currentTimeMillis()/1000;
					long createTime = cdnCtrl.getRuntime();
					if(currentTime-createTime>timeLong){
						cdnCtrl.getClient().doOption2();
					}else{
						logger.info("刚起来，稍等片刻，先返回初始化信息。。。。。");
					}
				}
				session.write(retRespose("gettime",TCPServer.getControllerList().get(
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
			case QUERY_TYPE:{
				session.write("{\"cmd\":\"query_type\",\"ret_code\":\"0\",\"vgw_type\":\""+ResConstants.TYPE+"\"}");
				session.close(false);
				break;
			}
			}
		} catch (Exception e) {
			logger.error("【VOD-VLC】接收并处理消息异常", e);
		}
	}

	private String retRespose(String cmd,Controller cdnCtrl) {
		JSONObject retObj = new JSONObject();
		retObj.put("cmd", cmd);
		if(cdnCtrl!=null&&!"".equals(cdnCtrl)){
			retObj.put(JSONConstants.CURRENTTIME, cdnCtrl.getCurrentTime());
			retObj.put(JSONConstants.TOTALTIME, cdnCtrl.getTotalTime());
		}else{
			retObj.put(JSONConstants.CURRENTTIME, "");
			retObj.put(JSONConstants.TOTALTIME, "");
		}
		logger.info("【VOD-VLC】:"+cmd+"返回报文："+retObj.toString());
		return retObj.toString();
	}

}
