package prod.nebula.vrc.module.resdis.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vrc.config.MsgConstants;
import prod.nebula.vrc.config.VODConst;
import prod.nebula.vrc.exception.VODException;
import prod.nebula.vrc.module.resdis.dto.VODResDisReqBean;
import prod.nebula.vrc.service.TCPServer;
import prod.nebula.vrc.util.Base64Util;
import prod.nebula.vrc.util.Commons;
import prod.nebula.vrc.util.ParamUtils;

/**
 * VOD控制代理-资源调度
 * 
 * @author 严东军
 * 
 */
public class VCAResDisDao {
	public final Logger logger = LoggerFactory.getLogger(getClass());

	private static VCAResDisDao vcaResDisDao = new VCAResDisDao();

	private VCAResDisDao() {

	}

	/**
	 * VOD控制代理-资源调度类获取
	 * 
	 * @return VCAResDisDao VOD控制代理-资源调度类
	 */
	public static VCAResDisDao getInstance() {
		if (null == vcaResDisDao) {
			return vcaResDisDao = new VCAResDisDao();
		}

		return vcaResDisDao;
	}

	/**
	 * 获取请求模型数据
	 * 
	 * @param revStr
	 * @return VODResDisReqBean 返回请求的数据的模型
	 * @throws Exception
	 */
	public VODResDisReqBean getReqBean(Map<String, Object> message)
			throws VODException {
		try {
			VODResDisReqBean bean = new VODResDisReqBean();
			String appUserId = ParamUtils.getParameter(message,
					MsgConstants.APPUSERID, "");
			String spId = ParamUtils.getParameter(message, MsgConstants.SPID,
					"");
			String messageInfo = ParamUtils.getParameter(message,
					MsgConstants.MESSAGEINFO, "");
			
			String regionId = messageInfo.split("\\|")[1];
			String rtspAddr = Base64Util
					.getFromBASE64(messageInfo.split("\\|")[0]);
			if (rtspAddr.indexOf("rtsp://") == -1) {
				logger.error("RTSP Addr is Wrong!");
				throw new VODException("RTSP Addr is Wrong!");
			} else {
				rtspAddr = rtspAddr.substring(rtspAddr.indexOf("rtsp://"));
			}
			if (Commons.isNullorEmptyString(regionId)
					|| Commons.isNullorEmptyString(rtspAddr)) {
				logger.error("reginId or rtspAddr is null or empty");
				throw new VODException("reginId or rtspAddr is null or empty");
			}

			// Support SD VOD.
			int index = rtspAddr.indexOf("hdds_ip//");
			if (index != -1) {
				if ("5542".equals(rtspAddr.substring(index - 5, index - 1))) {
					rtspAddr = rtspAddr.substring(0, index - 2) + "1"
							+ rtspAddr.substring(index - 1, rtspAddr.length());
				}
				index = rtspAddr.indexOf("hdds_ip");
				rtspAddr = rtspAddr.substring(0, index) + "hdds_ipqam4"
						+ rtspAddr.substring(index + 7, rtspAddr.length());
				index = rtspAddr.indexOf("isIpqam");
				rtspAddr = rtspAddr.substring(0, index + 8) + "1"
						+ rtspAddr.substring(index + 9, rtspAddr.length());
			}
			
			String doneDate = ParamUtils.getParameter(message,
					MsgConstants.DONEDATE, "");
			String cmd = ParamUtils.getParameter(message, MsgConstants.COMMAND,
					"");
			String doneCode = ParamUtils.getParameter(message,
					MsgConstants.SERIALNO, "");
			String sessionId = ParamUtils.getParameter(message,
					MsgConstants.APPUSERID, "");
			String gatewayIp = ParamUtils.getParameter(message,
					MsgConstants.GATEWAYIP, "");
			String gatewayPort = ParamUtils.getParameter(message,
					MsgConstants.GATEWAYPORT, "");
			String areaid = ParamUtils.getParameter(message,
					MsgConstants.AREAID, "");
			String spuserid = ParamUtils.getParameter(message, MsgConstants.SPUSERID,"");
			
			String ipqamIp = ParamUtils.getParameter(message, MsgConstants.IPQAMIP,"");
			String ipqamPort = ParamUtils.getParameter(message, MsgConstants.IPQAMPORT,"");
			String frequency = ParamUtils.getParameter(message, MsgConstants.FREQUENCY,"");
			String pid = ParamUtils.getParameter(message, MsgConstants.PID,"");

			bean = new VODResDisReqBean();
			bean.setAreaid(areaid);
			bean.setUserId(spuserid);
			bean.setRegionId(regionId);
			bean.setRtspAddr(rtspAddr);
			bean.setDoneDate(doneDate);
			bean.setSerialNo(doneCode);
			bean.setCmd(cmd);
			bean.setSessionId(sessionId);
			bean.setAppUserId(appUserId);
			bean.setSpId(spId);
			bean.setKgIp(gatewayIp);
			bean.setKgPort(gatewayPort);
			bean.setIpqamIP(ipqamIp);
			bean.setIpqamPort(ipqamPort);
			bean.setFrequency(frequency);
			bean.setPid(pid);
			TCPServer.getConfig().setCSCGAddress(gatewayIp);
			TCPServer.getConfig().setCSCGPort(Integer.parseInt(gatewayPort));
			
			return bean;
		} catch (Exception e) {
			logger.error("【VOD网关】解析传入报文异常", e);
			throw new VODException(VODConst.REQMESSAGE_ERROR, "【VOD网关】解析传入报文异常");
		}

	}
}
