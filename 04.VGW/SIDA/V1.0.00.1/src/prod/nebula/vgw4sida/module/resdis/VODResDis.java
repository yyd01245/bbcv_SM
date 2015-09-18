/**
 * 
 */
package prod.nebula.vgw4sida.module.resdis;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw4sida.config.JSONConstants;
import prod.nebula.vgw4sida.config.MsgConstants;
import prod.nebula.vgw4sida.config.VODConst;
import prod.nebula.vgw4sida.exception.VODException;
import prod.nebula.vgw4sida.handler.RTSPClient;
import prod.nebula.vgw4sida.module.resctrl.Controller;
import prod.nebula.vgw4sida.module.resctrl.VODResCtrl;
import prod.nebula.vgw4sida.module.resdis.dao.VCAResDisDao;
import prod.nebula.vgw4sida.module.resdis.dto.VODResDisReqBean;
import prod.nebula.vgw4sida.service.TCPServer;
import prod.nebula.vgw4sida.util.Commons;
import prod.nebula.vgw4sida.util.DBConnection;
import prod.nebula.vgw4sida.util.IpUtil;
import prod.nebula.vgw4sida.util.ParamUtils;
import prod.nebula.vgw4sida.util.SQLCommand;

/**
 * VOD控制代理--VOD资源调度
 * 
 * @author 严东军
 * 
 */
public class VODResDis {
	public static final Logger logger = LoggerFactory
			.getLogger(VODResDis.class);
	/**
	 * 远程CDN服务器地址
	 */
	private String remoteAddrIp;

	/**
	 * 远程CDN服务器端口
	 */
	private int remoteAddrPort;

	private String localIpAddress;

	private static VODResDis vodResDis = new VODResDis();

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
	public void login(IoSession session, Map<String, Object> message) {
		VODResDisReqBean request = null;
		try {
			// 1解析报文
			logger.info("【VOD网关】开始解析传入消息");
			request = VCAResDisDao.getInstance().getReqBean(message);
			logger.info("【VOD网关】结束解析传入消息");
			
			// int listenPort = TCPServer.beginPort + TCPServer.seq++;
			
			// 保证只有唯一登录
			logout(request.getSessionId());
			
			// 2播放视频
			logger.info("【VOD网关】开始播放视频操作");
			startPlayer(request, session);
			logger.info("【VOD网关】完成播放视频操作");
		} catch (VODException e) {
			logger.error("【VOD网关】", e);
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
			logger.debug("开始获得可用UDP端口");
			int listenPort = IpUtil.getAvailablePort();
			logger.debug("获得可用UDP端口：" + listenPort);
			localIpAddress = TCPServer.getConfig().getLocalIpAddress();
			String stbid = bean.getUserId();
			String rtspUrl = TCPServer.getConfig().getRtspAddr ();
			String regionId = TCPServer.getConfig().getRegionId();
			int length = 0;
			
			if (null != regionId && "".equals(regionId)) {
				regionId = bean.getRegionId();
			} else {
				length = regionId.split(",").length;
				regionId = regionId.split(",")[TCPServer.VRC_CHANGE];
				if (null != regionId) {
					regionId = regionId.trim();
				}
			}
			
			if (null != rtspUrl && "".equals(rtspUrl)) {
				rtspUrl = bean.getRtspAddr();
			} else {
				rtspUrl = rtspUrl.split(",")[TCPServer.VRC_CHANGE];
				if (null != rtspUrl) {
					rtspUrl = rtspUrl.trim();
				}
			}
			
			if (regionId.split(",").length > 0) {
				TCPServer.VRC_CHANGE++;
				if (TCPServer.VRC_CHANGE >= length) {
					TCPServer.VRC_CHANGE = 0;
				}
			}
			
			String serialNo = bean.getSerialNo();
			String doneDate = bean.getDoneDate();
			String cmd = bean.getCmd();
			String sessionId = bean.getSessionId();
			String appUserId = bean.getAppUserId();
			String spId = bean.getSpId();
			String totalTime = bean.getTotalTime();
			String purchaseToken = bean.getPurchaseToken();
			String ipqamIP = bean.getIpqamIP();
			String ipqamPort = bean.getIpqamPort();
			String vodname = bean.getVodname();
			String posterurl = bean.getPosterurl();
			
			
			logger.debug("【VOD网关】appUserId=" + appUserId + " sessionId="
					+ sessionId + " regionId=" + regionId
					+ " rtsp=" + rtspUrl+" localIpAddress="+localIpAddress);
			
			rtspUrl = rtspUrl.substring(rtspUrl.indexOf("rtsp://"));
			// String playUrl = rtspUrl.substring(0,
			// rtspUrl.lastIndexOf(".ts") + 3);
			String playUrl = rtspUrl;
			rtspUrl = rtspUrl.substring(7);
			remoteAddrIp = rtspUrl.substring(0, rtspUrl.indexOf(":"));
			remoteAddrPort = Integer.valueOf(rtspUrl.substring(
					rtspUrl.indexOf(":") + 1, rtspUrl.length()));
			logger.info("远端地址："+remoteAddrIp+",远端端口："+remoteAddrPort);
			SQLCommand sqlCommand = new SQLCommand();
			String sql = "select stbid,stbcard,stbip,stbport,qamname from teamInfo where stbid= '"+appUserId+"'";
			String result = sqlCommand.getFirstRecord(sql);
			ArrayList resultList = sqlCommand.getRecord(sql);
			String stbcard="";
//			String stbip ="";
//			String stbport ="";
			String qamname ="";
			if(resultList != null && resultList.size() >0){
				Object[] rsObjArr = (Object[])resultList.get(0);
				stbcard = (String) rsObjArr[1];
//				stbip = (String) rsObjArr[2];
//				stbport = (String) rsObjArr[3];
				qamname = (String) rsObjArr[4];
			}
//			String stbcard="3452788";
//			String stbip ="192.168.100.11";
//			String stbport ="6000";
//			String qamname ="3000";
			
			logger.info("查询机顶盒信息：stbcard="+stbcard+",stbip="+ipqamIP+",stbport="+ipqamPort+",qamname="+qamname);
			sender = new Controller(listenPort);
			sender.setRuntime(System.currentTimeMillis() / 1000);
			client = new RTSPClient(new InetSocketAddress(remoteAddrIp,
					remoteAddrPort), new InetSocketAddress(localIpAddress, 0),
					playUrl, sender, session);
			client.setStbid(appUserId);
			client.setRegionId(regionId);
			client.setSerialNo(serialNo);
			client.setListenIp(localIpAddress);
			client.setListenPort(listenPort);
			client.setCmd(cmd);
			client.setDoneDate(doneDate);
			client.setSessionid(sessionId);
			client.setSpId(spId);
			client.setPurchaseToken(purchaseToken);
			client.setClient(stbcard);
			client.setClient_port(ipqamPort);
			client.setQam_name(qamname);
			client.setDestination(ipqamIP);
			sender.setStbid(stbid);
			sender.setClient(client);
			sender.setRegionId(regionId);
			sender.setSerialNo(serialNo);
			sender.setSessionId(sessionId);
			sender.setAppUserId(appUserId);
			sender.setVodname(vodname);
			sender.setPosterurl(posterurl);
			sender.setCurrentTime("0");
			sender.setTotalTime(totalTime);
			if (TCPServer.putController(sender)) {
				client.start();
				sender.start();
			}
		} catch (Exception e) {
			VODResCtrl.getInstance().logout(sender);
			if (sender != null) {
				logger.debug("正在停止守护线程");
				sender.getAcceptor().dispose(false);
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
		VODResCtrl.getInstance().returnMessage(session, retObj.toString());

		try {
			logger.debug("start logout");
			String sessionId = ParamUtils.getParameter(message,
					MsgConstants.APPUSERID, "");
			logger.info("sessionId=" + sessionId);
			logout(sessionId);
		} catch (Exception e) {
			logger.error("【VOD网关】销毁播放视频线程失败");
		}

	}

	public static void logout(String sessionId) {
		if (!Commons.isNullorEmptyString(sessionId)) {
			TCPServer.getControllerList().containsKey(sessionId);
			Object object = TCPServer.getControllerList().get(sessionId);
			if (null != object && object instanceof Controller) {
				Controller temp = (Controller) object;
				VODResCtrl.getInstance().logout(temp);
				if (temp != null) {
					logger.debug("正在停止守护线程");
					temp.getAcceptor().dispose(false);
					temp.setRuntimeThread(false);
				}
				TCPServer.getControllerList().remove(sessionId);
				temp = null;
			}
		}
	}
}
