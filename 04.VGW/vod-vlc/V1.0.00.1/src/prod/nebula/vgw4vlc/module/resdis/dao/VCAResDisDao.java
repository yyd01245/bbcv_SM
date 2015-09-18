package prod.nebula.vgw4vlc.module.resdis.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vgw4vlc.config.JSONConstants;
import prod.nebula.vgw4vlc.config.MsgConstants;
import prod.nebula.vgw4vlc.config.VODConst;
import prod.nebula.vgw4vlc.exception.VODException;
import prod.nebula.vgw4vlc.module.resdis.dto.VODResDisReqBean;
import prod.nebula.vgw4vlc.service.TCPServer;
import prod.nebula.vgw4vlc.util.ParamUtils;

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
			VODResDisReqBean bean;
			String appUserId = ParamUtils.getParameter(message,
					MsgConstants.APPUSERID, "");
			String spId = ParamUtils.getParameter(message, MsgConstants.SPID,
					"");
			String vodname = ParamUtils.getParameter(message,
					JSONConstants.VODNAME, "");
			
			String posterurl = ParamUtils.getParameter(message,
					JSONConstants.POSTERURL, "");
			String doneDate = ParamUtils.getParameter(message,
					MsgConstants.DONEDATE, "");
			String cmd = ParamUtils.getParameter(message, MsgConstants.COMMAND,
					"");
			String doneCode = ParamUtils.getParameter(message,
					MsgConstants.SERIALNO, "");
			String sessionId = ParamUtils.getParameter(message,
					MsgConstants.APPUSERID, "");
			String spuserid = ParamUtils.getParameter(message, MsgConstants.SPUSERID,"");
			
			String input = ParamUtils.getParameter(message, MsgConstants.INPUT,"");
			String output = ParamUtils.getParameter(message, MsgConstants.OUTPUT,"");
			String totalTime = ParamUtils.getParameter(message, MsgConstants.TOTALTIME, "");
			String gatewayIp = ParamUtils.getParameter(message,
					MsgConstants.GATEWAYIP, "");
			String gatewayPort = ParamUtils.getParameter(message,
					MsgConstants.GATEWAYPORT, "");
			
			bean = new VODResDisReqBean();
			bean.setUserId(spuserid);
			bean.setDoneDate(doneDate);
			bean.setSerialNo(doneCode);
			bean.setCmd(cmd);
			bean.setSessionId(sessionId);
			bean.setAppUserId(appUserId);
			bean.setSpId(spId);
			bean.setVodname(vodname);
			bean.setPosterurl(posterurl);
			bean.setInput(input);
			bean.setOutput(output);
			bean.setTotalTime(totalTime);
			TCPServer.getConfig().setCSCGAddress(gatewayIp);
			TCPServer.getConfig().setCSCGPort(Integer.parseInt(gatewayPort));
			return bean;
		} catch (Exception e) {
			logger.error("【VOD网关】解析传入报文异常", e);
			throw new VODException(VODConst.REQMESSAGE_ERROR, "【VOD网关】解析传入报文异常");
		}

	}
}
