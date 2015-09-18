package prod.nebula.vgw4sida.module.resdis.dao;

import java.util.Map;

import org.apache.mina.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw4sida.config.JSONConstants;
import prod.nebula.vgw4sida.config.MsgConstants;
import prod.nebula.vgw4sida.config.VODConst;
import prod.nebula.vgw4sida.exception.VODException;
import prod.nebula.vgw4sida.module.resdis.dto.VODResDisReqBean;
import prod.nebula.vgw4sida.service.TCPServer;
import prod.nebula.vgw4sida.util.Commons;
import prod.nebula.vgw4sida.util.ParamUtils;

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
			String ipqamIP = ParamUtils.getParameter(message, MsgConstants.IPQAMIP, "");
			String ipqamPort = ParamUtils.getParameter(message, MsgConstants.IPQAMPORT, "");
			
			String vodname = ParamUtils.getParameter(message, JSONConstants.VODNAME, "");
			String posterurl = ParamUtils.getParameter(message, JSONConstants.POSTERURL, "");
			
			String regionId = messageInfo.split("\\|")[1];
			String rtspAddr = new String(Base64.decodeBase64(messageInfo.split("\\|")[0].getBytes()));
			if (rtspAddr.indexOf("starvod://") == -1) {
				logger.error("RTSP Addr is Wrong!");
				throw new VODException("RTSP Addr is Wrong!");
			} else {
				rtspAddr = rtspAddr.substring(rtspAddr.indexOf("starvod://"));
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
			//starvod://srm=10.255.201.12:554&offer=138993&duration=321_23140&user_type=0&network=1&run_time=6613&enc_type=0&enc_info=the.program.is.encrypted.not.supported&biz_type=2&sess_gw=10.255.201.12&svc_gw=BMSsvcgateway&svc=huashu-service&svc_type=mod&retUrl=&user_type=0
			//user_type和svc_type
			/******************S   获取rtsp、purchaseToken、totalTime  S***********************/
			String a = rtspAddr.substring(rtspAddr.indexOf("starvod://")+10);
			String[] rtspAddrs = null ;
			if(a.contains("$")){
				 rtspAddrs = a.split("\\$");
			}else{
				rtspAddrs = a.split("&");
			}
			String setupRtspAddr = rtspAddrs[0].replace("srm=", "rtsp://");
			String offer = rtspAddrs[1].split("=")[1];
			int user_type = Integer.valueOf(rtspAddrs[3].split("=")[1]);
			String svc_type = rtspAddrs[12].split("=")[1];
			String totalTime = rtspAddrs[5].split("=")[1];
			int offertype = 0;
			if(user_type == 1 )
				offertype = 0x06;
			else if(user_type == 2 )
				offertype = 0x03;
			else if(user_type == 8 )
				offertype = 0x04;
			else if(user_type == 9 )
				offertype = 0x05;
			else if(user_type == 0 )
			{
				if(svc_type.compareToIgnoreCase("mod") == 0)
					offertype = 0x01;
				else if(svc_type.compareToIgnoreCase("svod") == 0)
					offertype = 0x02;
			}
			String purchaseToken = offer+"-"+offertype;
			/******************S   获取rtsp、purchaseToken、totalTime  S***********************/
			
			
			
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
			String spuserid = ParamUtils.getParameter(message, MsgConstants.SPUSERID,"");

			bean = new VODResDisReqBean();
			bean.setUserId(spuserid);
			bean.setRegionId(regionId);
			bean.setRtspAddr(setupRtspAddr);
			bean.setDoneDate(doneDate);
			bean.setSerialNo(doneCode);
			bean.setCmd(cmd);
			bean.setSessionId(sessionId);
			bean.setAppUserId(appUserId);
			bean.setSpId(spId);
			bean.setKgIp(gatewayIp);
			bean.setKgPort(gatewayPort);
			
			bean.setTotalTime(totalTime);
			bean.setPurchaseToken(purchaseToken);
			
			bean.setIpqamIP(ipqamIP);
			bean.setIpqamPort(ipqamPort);
			
			bean.setVodname(vodname);
			bean.setPosterurl(posterurl);
			
			TCPServer.getConfig().setCSCGAddress(gatewayIp);
			TCPServer.getConfig().setCSCGPort(Integer.parseInt(gatewayPort));
			
			return bean;
		} catch (Exception e) {
			logger.error("【VOD网关】解析传入报文异常", e);
			throw new VODException(VODConst.REQMESSAGE_ERROR, "【VOD网关】解析传入报文异常");
		}

	}
}
