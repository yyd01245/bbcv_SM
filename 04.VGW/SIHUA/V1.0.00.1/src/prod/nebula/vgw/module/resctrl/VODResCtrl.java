/**
 * 
 */
package prod.nebula.vgw.module.resctrl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw.config.Command;
import prod.nebula.vgw.config.JSONConstants;
import prod.nebula.vgw.config.VODConst;
import prod.nebula.vgw.module.resctrl.dto.VODResCtrlReqBean;
import prod.nebula.vgw.service.TCPServer;
import prod.nebula.vgw.util.Commons;
import prod.nebula.vgw.util.StringUtil;
import prod.nebula.vgw.util.client.IOSocketClient;
import prod.nebula.vgw.util.client.TcpClient;

/**
 * VOD控制代理--VOD资源控制类
 * 
 * @author 严东军
 * 
 */
public class VODResCtrl {
	public static final Logger logger = LoggerFactory
			.getLogger(VODResCtrl.class);

	private Controller ctrl;

	private static VODResCtrl vodResCtrl = new VODResCtrl();
	private static int forwardTime = TCPServer.getConfig().getForwardTime();
	private VODResCtrl() {

	}

	/**
	 * 获取VOD资源控制管理类实例
	 * 
	 * @return VODResCtrl VOD资源控制管理类实例
	 */
	public static VODResCtrl getInstance() {
		if (null == vodResCtrl) {
			return vodResCtrl = new VODResCtrl();
		}

		return vodResCtrl;
	}

	/**
	 * VOD资源控制管理方法,对应按键处理
	 * 
	 * @param session
	 *            连接信息
	 * @param message
	 *            接收到的数据
	 */
	public void VODresDisManage(IoSession session, String keyValue,
			Controller ctrl) {

		try {
			this.ctrl = ctrl;
			this.ctrl.setRuntime(System.currentTimeMillis() / 1000);
			logger.debug("【VOD网关】当前状态:" + ctrl.getStatus());
			logger.debug("【VOD网关】当前接受键值:"+keyValue);
//			if ("11".equals(keyValue)) {
//				sendCTAS(VODConst.KEYVALUE_LEFT, ctrl);
//			} else if ("6".equals(keyValue)) {
//				sendCTAS(VODConst.KEYVALUE_RIGHT, ctrl);
//			}
			switch (ctrl.getStatus()) {
			case init:
				break;
			case options:
				break;
			case describe:
				break;
			case setup:
				break;
			case scale:
				// 1.快进状态下按快进
				if (ctrl.getClient().getScale() > 0
						&& keyValue.equals(VODConst.KEYVALUE_FAST)) {
					if (ctrl.getClient().getScale() < VODConst.SCALE_FAST_TIMES) {
						ctrl.getClient().setScale(
								ctrl.getClient().getScale() + 2);
						doScale(ctrl);
					} else {
						doPlay(ctrl);
					}
				}
				// 2.快退状态下按快退
				if (ctrl.getClient().getScale() < 0
						&& keyValue.equals(VODConst.KEYVALUE_SLOW)) {
					if (ctrl.getClient().getScale() > VODConst.SCALE_SLOW_TIMES) {
						ctrl.getClient().setScale(
								ctrl.getClient().getScale() - 2);
						doScale(ctrl);
					} else {
						doPlay(ctrl);
					}

				}

				// 3.快退状态下点快进
				if ( keyValue.equals(VODConst.KEYVALUE_FAST)) {
					doPlay(ctrl);
				}

				// 4.快进状态下点快退
				if ( keyValue.equals(VODConst.KEYVALUE_SLOW)) {
					doPlay(ctrl);
				}

				// 5任何状态下点播放
				if (keyValue.equals(VODConst.KEYVALUE_PLAY)) {
					doPlay(ctrl);
				}

				// 6任何状态下点退出、返回
				if (keyValue.equals(VODConst.KEYVALUE_EXIT)
						|| keyValue.equals(VODConst.KEYVALUE_BACK)) {
					back();
				}
				break;
			case play:
				// 当前状态为播放，按确认暂停
				if (keyValue.equals(VODConst.KEYVALUE_PLAY)) {
					doPause(ctrl);
					
					//通知SM暂停指令
					TcpClient tcpClient = new TcpClient();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("cmd", "vod_pause");
					String username = ctrl.getStbid();
					String usernamegbk = new String(username.getBytes(),"gbk");
					String usernameutf = URLEncoder.encode(usernamegbk, "utf-8");
					jsonObject.put("username", usernameutf);
					jsonObject.put("stream_id", ctrl.getAppUserId());
					String vodname = ctrl.getVodname();
					String vodnamegbk = new String(vodname.getBytes(),"gbk");
					String vodnameutf = URLEncoder.encode(vodnamegbk, "utf-8");
					jsonObject.put("vodname", vodnameutf);
					jsonObject.put("posterurl", ctrl.getPosterurl());
					jsonObject.put("serialno", ctrl.getSerialNo());
					String sendMsg = jsonObject.toString();
					tcpClient.sendStr(TCPServer.getConfig()
							.getCSCGAddress(), TCPServer.getConfig().getCSCGPort(), 1000,
							null, sendMsg);
				}

				// 当前状态为播放，按返回/退出(停止) 停止
				if (keyValue.equals(VODConst.KEYVALUE_EXIT)
						|| keyValue.equals(VODConst.KEYVALUE_BACK)) {
					back();
				}

				// 播放状态 快进
				if (keyValue.equals(VODConst.KEYVALUE_FAST)) {
//					logger.debug("当前请求为背进，倍率为2.。。。。。");
//					ctrl.getClient().setScale(2);
//					doScale(ctrl);
					String currentTime = ctrl.getCurrentTime().trim();
					String[] currentTimes = currentTime.split("\\.");
					int currenttime = Integer.valueOf(currentTimes[0])+forwardTime;//向后移动1分钟
					String totalTime = ctrl.getTotalTime().trim();
					String[] totalTimes = totalTime.split("\\.");
					int totaltime = Integer.valueOf(totalTimes[0]);
					int curtime = (currenttime>totaltime)?totaltime-5:currenttime;//判断是否超出总时长
					ctrl.setCurrentTime(String.valueOf(curtime));
					doChooseTime(ctrl);
					
				}

				// 播放状态 快退
				if (keyValue.equals(VODConst.KEYVALUE_SLOW)) {
//					ctrl.getClient().setScale(-2);
//					doScale(ctrl);
					String currentTime = ctrl.getCurrentTime().trim();
					String[] currentTimes = currentTime.split("\\.");
					int currenttime = Integer.valueOf(currentTimes[0])-forwardTime;//向前移动1分钟
					int curtime = (currenttime>0)?currenttime:0;//判断是否小于开始时间
					ctrl.setCurrentTime(String.valueOf(curtime));
					doChooseTime(ctrl);
				}
				
				break;
			case pause:
				// 当前状态为暂停，按确认播放
				if (keyValue.equals(VODConst.KEYVALUE_PLAY)) {
					doPlay(ctrl);
					//通知SM暂停恢复指令
					TcpClient tcpClient = new TcpClient();
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("cmd", "vod_resume");
					String username = ctrl.getStbid();
					String usernamegbk = new String(username.getBytes(),"gbk");
					String usernameutf = URLEncoder.encode(usernamegbk, "utf-8");
					jsonObject.put("username", usernameutf);
					jsonObject.put("stream_id", ctrl.getAppUserId());
					jsonObject.put("serialno", ctrl.getSerialNo());
					String sendMsg = jsonObject.toString();
					tcpClient.sendStr(TCPServer.getConfig()
							.getCSCGAddress(), TCPServer.getConfig().getCSCGPort(), 1000,
							null, sendMsg);
				}

				// 当前状态为暂停，按返回/退出(停止) 停止
				if (keyValue.equals(VODConst.KEYVALUE_EXIT)
						|| keyValue.equals(VODConst.KEYVALUE_BACK)) {
					back();
				}
				break;
			case teardown:
				break;
			default:
				break;
			}

		} catch (Exception e) {
			logger.error("【VOD网关】操作VOD视频", e);
		}

	}

	/**
	 * 向CTAS发送音量控制键值
	 * 
	 * @param keyvalueLeft
	 */
	private void sendCTAS(int keyvalue, Controller ctrl) {
//		String ip = TCPServer.getConfig().getCtasIp();
//		int port = TCPServer.getConfig().getCtasPort();
//		logger.info("【VGW】发送音量控制键  [" + ip + ":" + port + "]:" + keyvalue);
//		send(keyvalue + "", ctrl, "2", ip, port);
//		send(keyvalue + "", ctrl, "3", ip, port);
	}

	private void send(String keyvalue, Controller ctrl, String keystatus,
			String ip, int port) {
		IOSocketClient client = new IOSocketClient();

		JSONObject json = new JSONObject();
		json.put("cmd", "KeyDown");
		json.put("termid", "");
		json.put("outputid", ctrl.getSessionId());
		json.put("keydev", "1001");
		json.put("key", keyvalue);
		json.put("keystatus", keystatus);
		json.put("dvbinfo", ctrl.getAreaid() + ":0:0");
		client.sendStr(ip, port, 2000, null, json.toString() + "XXEE", null);
	}

	/**
	 * VOD资源控制管理方法，对应HTTP请求
	 * 
	 * @param keyValue
	 *            操作类型
	 * @param ctrl
	 *            当前操作类
	 */
	public void VODOperate(String keyValue, Controller ctrl) {
		try {
			this.ctrl = ctrl;
			this.ctrl.setRuntime(System.currentTimeMillis() / 1000);
			logger.debug("【VOD网关】当前状态:" + ctrl.getStatus());
			switch (ctrl.getStatus()) {
			case init:
				break;
			case options:
				break;
			case describe:
				break;
			case setup:
				break;
			case scale:
				// 1.快进状态下按快进
				if (ctrl.getClient().getScale() > 0
						&& keyValue.equals(VODConst.KEYVALUE_FAST)) {
					if (ctrl.getClient().getScale() < VODConst.SCALE_FAST_TIMES) {
						ctrl.getClient().setScale(
								ctrl.getClient().getScale() + 2);
						doScale(ctrl);
					} else {
						doPlay(ctrl);
					}
				}
				// 2.快退状态下按快退
				if (ctrl.getClient().getScale() < 0
						&& keyValue.equals(VODConst.KEYVALUE_SLOW)) {
					if (ctrl.getClient().getScale() > VODConst.SCALE_SLOW_TIMES) {
						ctrl.getClient().setScale(
								ctrl.getClient().getScale() - 2);
						doScale(ctrl);
					} else {
						doPlay(ctrl);
					}

				}

				// 3.快退状态下点快进
				if (ctrl.getClient().getScale() < 0
						&& keyValue.equals(VODConst.KEYVALUE_FAST)) {
					doPlay(ctrl);
				}
				
				// 4.快进状态下点快退
				if (ctrl.getClient().getScale() > 0
						&& keyValue.equals(VODConst.KEYVALUE_SLOW)) {
					doPlay(ctrl);
				}

				// 5任何状态下点播放
				if (keyValue.equals(VODConst.KEYVALUE_PLAY)) {
					doPlay(ctrl);
				}

				// 6任何状态下点退出、返回
				if (keyValue.equals(VODConst.KEYVALUE_EXIT)
						|| keyValue.equals(VODConst.KEYVALUE_BACK)) {
					back();
				}
				break;
			case play:
				// 当前为播放状态 选时间播放
				if (keyValue.equals(VODConst.KEYVALUE_CHOOSETIME)) {
					doChooseTime(ctrl);
				}

				// 当前状态为播放，按暂停
				if (keyValue.equals(VODConst.KEYVALUE_PAUSE)) {
					doPause(ctrl);
				}

				// 当前状态为播放，按返回/退出(停止) 停止
				if (keyValue.equals(VODConst.KEYVALUE_EXIT)
						|| keyValue.equals(VODConst.KEYVALUE_BACK)) {
					back();
				}

				// 播放状态 快进
				if (keyValue.equals(VODConst.KEYVALUE_FAST)) {
					ctrl.getClient().setScale(2);
					doScale(ctrl);
				}

				// 播放状态 快退
				if (keyValue.equals(VODConst.KEYVALUE_SLOW)) {
					ctrl.getClient().setScale(-2);
					doScale(ctrl);
				}
				break;
			case pause:
				// 当前状态为暂停，按播放
				if (keyValue.equals(VODConst.KEYVALUE_PLAY)) {
					doPlay(ctrl);
				}

				// 当前状态为暂停，继续播放
				if (keyValue.equals(VODConst.KEYVALUE_RESUME)) {
					doResume(ctrl);
				}

				// 当前为暂停状态 选时间播放
				if (keyValue.equals(VODConst.KEYVALUE_CHOOSETIME)) {
					doChooseTime(ctrl);
				}
				
				// 当前状态为暂停，按返回/退出(停止) 停止
				if (keyValue.equals(VODConst.KEYVALUE_EXIT)
						|| keyValue.equals(VODConst.KEYVALUE_BACK)) {
					back();
				}
				break;
			case teardown:
				break;
			default:
				break;
			}

		} catch (Exception e) {
			logger.error("【VOD网关】操作VOD视频", e);
		}

	}

	/**
	 * 返回应用首页
	 */
	@SuppressWarnings("unused")
	public void backToApp() {
		// 临时将代码结构调整为释放在前，发消息在后。
		try {
			tearDownClient(ctrl);
		} catch (Exception e1) {
			logger.error("【VOD网关】[关闭视频播放接收操作失败]", e1);
		}

		TcpClient tcpClient = new TcpClient();
		String sendMsg = getBackMsgToERM();
		logger.debug("send:" + TCPServer.getConfig().getCSCGAddress() + ":"
				+ TCPServer.getConfig().getCSCGPort() + "---:" + sendMsg);
		String revMsg = tcpClient.sendStr(TCPServer.getConfig()
				.getCSCGAddress(), TCPServer.getConfig().getCSCGPort(), 1000,
				null, sendMsg);
	}

	/**
	 * 发送给CSCG的返回信息
	 * 
	 * @param stbid
	 * @param regionId
	 * @return
	 */
	private String getBackMsgToERM() {
		String retMsg = getGoBackResp("", "", ctrl.getSerialNo(),
				ctrl.getSessionId(), "1001", "",ctrl.getStbid());
		return retMsg;
	}

	/**
	 * 获取请求模型数据
	 * 
	 * @param revStr
	 * @return VODResCtrlReqBean 返回请求的数据的模型
	 */
	@SuppressWarnings("unused")
	private VODResCtrlReqBean getBean(String revStr) {

		String head = TCPServer.getConfig().getDatagramHeader();
		String end = TCPServer.getConfig().getDatagramEnd();
		VODResCtrlReqBean bean = null;

		// 请求头和尾判断
		if (revStr.indexOf(head) >= 0 && revStr.indexOf(end) >= 0) {

			revStr = revStr.substring(revStr.indexOf(head) + head.length(),
					revStr.indexOf(end));
			String[] revArr = revStr.split("\\|");

			// 如果是VOD资源控制请求则可返回具体值
			if (!Commons.isNullorEmptyString(revArr[0])
					&& !Commons.isNullorEmptyString(revArr[1])) {
				bean = new VODResCtrlReqBean();
				bean.setHead(head);
				bean.setReqHead(revArr[0]);
				bean.setKeyValue(revArr[1]);
				bean.setEnd(end);
			}
		}

		return bean;
	}

	/**
	 * 获得CDN操作类
	 * 
	 * @param sessionId
	 * @return
	 */
	public Controller getCtrl(String sessionId) {
		if (StringUtil.assertNotNull(sessionId)) {
			Map<String, Controller> ctrls = TCPServer.getControllerList();
			if (null != ctrls && ctrls.size() > 0) {
				for (Entry<String, Controller> entry : ctrls.entrySet()) {
					if (entry.getKey().equals(sessionId)) {
						return entry.getValue();
					}
				}
			}
		}

		return null;

	}

	/**
	 * 关闭监听线程
	 * 
	 * @param ctrl
	 */
	private void tearDownClient(Controller ctrl) {
		ctrl.getClient().doTeardown();
		ctrl.setStatus(Commons.Status.teardown);
		ctrl.getClient().shutdown.set(true);
		ctrl.getClient().shutdown();
	}

	/**
	 * 暂停
	 * 
	 * @param ctrl
	 */
	private void doPause(Controller ctrl) {
		ctrl.getClient().doPause();
		ctrl.setStatus(Commons.Status.pause);
	}

	/**
	 * 播放
	 * 
	 * @param ctrl
	 */
	private void doPlay(Controller ctrl) {
		ctrl.getClient().doPlay();
		ctrl.setStatus(Commons.Status.play);
	}

	/**
	 * 快/慢放
	 * 
	 * @param ctrl
	 */
	private void doScale(Controller ctrl) {
		ctrl.getClient().doScale();
		ctrl.setStatus(Commons.Status.scale);
	}

	/**
	 * 选择时间播放
	 * 
	 * @param ctrl
	 */
	private void doChooseTime(Controller ctrl) {
		ctrl.getClient().doChooseTime(ctrl.getCurrentTime());
		ctrl.setStatus(Commons.Status.play);
	}

	/**
	 * 继续播放
	 * 
	 * @param ctrl
	 */
	private void doResume(Controller ctrl) {
		ctrl.getClient().doResume();
		ctrl.setStatus(Commons.Status.play);
	}

	/**
	 * 返回
	 */
	private void back() {
		try {
			backToApp();
		} catch (Exception e) {
			logger.error("【VOD网关】back", e);
		}
	}

	/**
	 * 登出
	 */
	public void logout(Controller ctrl) {
		try {
			tearDownClient(ctrl);
		} catch (Exception e1) {
			logger.error("【VOD网关】[关闭视频播放接收线程失败]", e1);
		}
	}

	/**
	 * 返回信息给业务调度
	 * 
	 * @param session
	 * @param retMsg
	 */
	public void returnMessage(IoSession session, String retMsg) {
		try {
			StringBuffer sf = new StringBuffer();
			sf.append(Commons.getStrValue(retMsg));
			IoBuffer buffer = IoBuffer.allocate(2048, true);// 分清楚direct和heap方式的缓冲区别
			buffer.setAutoExpand(true);// 自动扩张
			buffer.setAutoShrink(true);// 自动收缩
			buffer.putString(sf.toString(), Charset.forName(("gbk"))
					.newEncoder());
			buffer.flip();
			buffer.free();
			logger.info("【VOD代理工具】发送消息:" + retMsg);
			session.write(buffer);
			logger.info("【VOD网关】消息发送完毕");
		} catch (Exception e) {
			logger.error("【VOD网关】send message to CSCG fail", e);
		} finally {
			try {
				session.close(false);
			} catch (Exception e) {
				logger.error("【VOD网关】 TCP CLOSE Error: " + e);
			}
		}
	}

	public String getGoBackResp(String cmd, String doneDate, String serialNo,
			String sessionId, String spId, String messageInfo,String username) {

		if (ctrl != null) {
			logger.debug("正在停止守护线程");
			ctrl.getAcceptor().dispose(false);
			ctrl.setRuntimeThread(false);
		}
		TCPServer.getControllerList().remove(sessionId);

		JSONObject sendJson = new JSONObject();
		sendJson.put(JSONConstants.CMD, Command.GOBACK.value());
		sendJson.put(JSONConstants.SESSIONID, sessionId);
		// sendJson.put(JSONConstants.SPID, spId);
		sendJson.put(JSONConstants.SERIALNO, serialNo);
		sendJson.put(JSONConstants.MSG, messageInfo);
		try {
			String usernamegbk = new String(username.getBytes(),"gbk");
			String usernameutf = URLEncoder.encode(usernamegbk, "utf-8");
			sendJson.put("username", usernameutf);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sendJson.toString() + "XXEE";
	}

	/**
	 * 获得成功信息
	 * 
	 * @param revArr
	 * @return
	 */
	public String getResp(String cmd, String doneDate, String serialNo,
			String listenIp, String sessionId, int listenPort,
			String messageInfo, int errorCode, String totalTime) {
		JSONObject retObj = new JSONObject();
		retObj.put(JSONConstants.CMD, cmd);
		retObj.put(JSONConstants.RETCODE, String.valueOf(errorCode));
		retObj.put(JSONConstants.SESSIONID, sessionId);
		if (errorCode == 0) {
			retObj.put(JSONConstants.CIP, listenIp);
			retObj.put(JSONConstants.CPORT, String.valueOf(listenPort));
			retObj.put(JSONConstants.MESSAGEINFO, messageInfo);
			retObj.put(JSONConstants.TOTALTIME, totalTime);
			retObj.put(JSONConstants.SERIALNO, serialNo);
		} else if (errorCode < 0) {
			if (ctrl != null) {
				logger.debug("正在停止守护线程");
				ctrl.getAcceptor().dispose(false);
				ctrl.setRuntimeThread(false);
			}
			TCPServer.getControllerList().remove(sessionId);
		}
		return retObj.toString();
	}
}
