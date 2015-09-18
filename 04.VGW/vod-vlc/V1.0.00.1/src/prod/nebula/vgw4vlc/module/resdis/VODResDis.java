/**
 * 
 */
package prod.nebula.vgw4vlc.module.resdis;

import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw4vlc.config.JSONConstants;
import prod.nebula.vgw4vlc.config.MsgConstants;
import prod.nebula.vgw4vlc.config.TelnetClientServer;
import prod.nebula.vgw4vlc.config.VODConst;
import prod.nebula.vgw4vlc.exception.VODException;
import prod.nebula.vgw4vlc.handler.RTSPClient;
import prod.nebula.vgw4vlc.module.resctrl.Controller;
import prod.nebula.vgw4vlc.module.resctrl.VODResCtrl;
import prod.nebula.vgw4vlc.module.resdis.dao.VCAResDisDao;
import prod.nebula.vgw4vlc.module.resdis.dto.VODResDisReqBean;
import prod.nebula.vgw4vlc.service.TCPServer;
import prod.nebula.vgw4vlc.util.Commons;
import prod.nebula.vgw4vlc.util.IpUtil;
import prod.nebula.vgw4vlc.util.ParamUtils;

/**
 * VOD控制代理--VOD资源调度
 * 
 * @author 严东军
 * 
 */
public class VODResDis {
	public static final Logger logger = LoggerFactory
			.getLogger(VODResDis.class);

	private String localIpAddress;

	private static VODResDis vodResDis = new VODResDis();
	
//	private static TelnetClientServer telnet = TCPServer.getTelnet();

	private VODResDis() {

	}

	public static VODResDis getInstance() {
		if (null == vodResDis) {
			return vodResDis = new VODResDis();
		}

		return vodResDis;
	}

	/**
	 * VOD资源调度管理
	 * 
	 * @param revArr
	 * @return
	 */
	public void login(IoSession session, Map<String, Object> message) throws InterruptedException {
		VODResDisReqBean request = null;
		try {
			// 1解析报文
			logger.info("【VOD-VLC】开始解析传入消息");
			request = VCAResDisDao.getInstance().getReqBean(message);
			logger.info("【VOD-VLC】结束解析传入消息");

			// 保证只有唯一登录
//			if(TCPServer.getControllerList().containsKey(request.getSessionId())){
//				Controller ctrl = TCPServer.getControllerList().get(request.getSessionId());
//				VODResCtrl.getInstance().tearDownClient(ctrl);
//				Thread.sleep(2000);
//			}
//			logout(request.getSessionId());
//			Thread.sleep(2000);
			// 2播放视频
			logger.info("【VOD-VLC】开始播放视频操作");
			startPlayer(request, session);
			logger.info("【VOD-VLC】完成播放视频操作");
//			VODResCtrl.getInstance().returnMessage(
//					session,
//					VODResCtrl.getInstance().getResp("login",
//							request.getDoneDate(), request.getSerialNo(), "",
//							request.getSessionId(), 0,
//							"", VODConst.VOD_OK, ""));
		} catch (VODException e) {
			logger.error("【VOD-VLC】", e);
			int errorCode = e.getErrorCode();
			// String errorMessage = e.getMessage();
			VODResCtrl.getInstance().returnMessage(
					session,
					VODResCtrl.getInstance().getResp("login", "", "", "", "",
							-1, "", errorCode, ""));
		}

	}

	/**
	 * 开始播放视频流程
	 * 
	 * @param bean
	 *            VOD资源调度请求模型
	 */
	public void startPlayer(VODResDisReqBean bean, IoSession session)
			throws VODException {
		Controller sender = null;
		RTSPClient client = null;
		try {
			String localIP = TCPServer.getConfig().getLocalIpAddress();
			String stbid = bean.getUserId();
			String serialNo = bean.getSerialNo();
			String doneDate = bean.getDoneDate();
			String cmd = bean.getCmd();
			String sessionId = bean.getSessionId();
			String appUserId = bean.getAppUserId();
			String spId = bean.getSpId();
			String input = bean.getInput();
			String output = bean.getOutput();
			String vodname = bean.getVodname();
			String posterurl = bean.getPosterurl();
			
			
			/********************S 设置每次快进快退比例开始    s****************************/
			String totalTime = bean.getTotalTime();
			double forwardTime = TCPServer.getConfig().getForwardTime();
			double totaltime = Double.parseDouble(totalTime);
			double seek = new Double(forwardTime/totaltime);
			seek = seek*100;
			NumberFormat numberFormater = new DecimalFormat("0.0000");
			String forwardSeek = numberFormater.format(seek);		
			/********************E 设置每次快进快退比例结束    E****************************/
			
			logger.debug("【VOD-VLC】appUserId=" + appUserId + ",sessionId="
					+ sessionId +",vlc input= "+input+",vlc output="+output +",vodname="+vodname+",posterurl="+posterurl+",totalTime="+totalTime);
			
			int vlc_id = IpUtil.getAvailablePort();				//初始化一个VLC流标识，主键
			sender = new Controller(vlc_id);
			sender.setRuntime(System.currentTimeMillis() / 1000);
			client = new RTSPClient(sender, session);
			client.setStbid(appUserId);
			client.setSerialNo(serialNo);
			client.setCmd(cmd);
			client.setDoneDate(doneDate);
			client.setSessionid(sessionId);
			client.setListenIp(localIP);
			client.setListenPort(vlc_id);
			client.setSpId(spId);
			client.setInput(input);
			client.setOutput(output);
			sender.setStbid(stbid);
			sender.setClient(client);
			sender.setSerialNo(serialNo);
			sender.setSessionId(sessionId);
			sender.setAppUserId(appUserId);
			sender.setCurrentTime("0");
			sender.setTotalTime(totalTime);
			sender.setSeek(forwardSeek);
			sender.setVodname(vodname);
			sender.setPosterurl(posterurl);
			sender.setInput(input);
			sender.setOutput(output);
			if (TCPServer.putController(sender)) {
				client.start();
				sender.start();
//				VODResCtrl.getInstance().returnMessage(
//						session,
//						VODResCtrl.getInstance().getResp("login",
//								doneDate, serialNo, localIP,
//								sessionId, vlc_id,
//								"", VODConst.VOD_OK, totalTime));
			}
		} catch (Exception e) {
			VODResCtrl.getInstance().logout(sender);
//			telnet.sendCommand("del "+sender.getId());
			if (sender != null) {
				logger.debug("正在停止守护线程");
//				sender.getAcceptor().dispose(false);
				sender.setRuntimeThread(false);
			}
			TCPServer.getControllerList().remove(sender.getSessionId());
			client = null;
			sender = null;
			throw new VODException(VODConst.PLAYVOD_ERROR, "播放VOD主流程异常");
		}
	}

	/**
	 * 登出流程
	 * 
	 * @param msgreq
	 */
	public void logout(IoSession session, Map<String, Object> message) {

		JSONObject retObj = new JSONObject();
		retObj.put(JSONConstants.CMD,
				ParamUtils.getParameter(message, MsgConstants.COMMAND, ""));
		retObj.put(JSONConstants.RETCODE, "0");
		retObj.put(
				JSONConstants.SERIALNO,
				ParamUtils.getParameter(message, MsgConstants.SERIALNO,
						Commons.getSerialNo()));
		

		try {
			logger.debug("start logout");
			String sessionId = ParamUtils.getParameter(message,
					JSONConstants.SESSIONID, "");
			logger.info("sessionId=" + sessionId);
			logout(sessionId);
			VODResCtrl.getInstance().returnMessage(session, retObj.toString());
		} catch (Exception e) {
			logger.error("【VOD-VLC】销毁播放视频线程失败");
		}

	}

	public static void logout(String sessionId) {
		if (!Commons.isNullorEmptyString(sessionId)) {
			TCPServer.getControllerList().containsKey(sessionId);
			Object object = TCPServer.getControllerList().get(sessionId);
			if (null != object && object instanceof Controller) {
				Controller temp = (Controller) object;
				logger.info("要退出的标识：============"+temp.getId());
				VODResCtrl.getInstance().logout(temp);
//				telnet.sendCommand("del "+temp.getId());
//				temp.getClient().shutdown.set(true);
				if (temp != null) {
					logger.debug("正在停止守护线程");
//					temp.getAcceptor().dispose(false);
					temp.setRuntimeThread(false);
				}
				TCPServer.getControllerList().remove(sessionId);
				temp = null;
			}
		}
	}
}
