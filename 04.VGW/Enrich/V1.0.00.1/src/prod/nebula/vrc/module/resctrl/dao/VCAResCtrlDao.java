/**
 * 
 */
package prod.nebula.vrc.module.resctrl.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.vrc.config.VODConst;
import prod.nebula.vrc.module.resctrl.dto.VODQuitlReqBean;
import prod.nebula.vrc.service.TCPServer;
import prod.nebula.vrc.util.Commons;

/**
 * VOD控制代理-资源控制
 * 
 * @author 严东军
 * 
 */
public class VCAResCtrlDao {
	public final Logger logger = LoggerFactory.getLogger(getClass());

	private static String head = TCPServer.getConfig().getDatagramHeader();
	private static String end = TCPServer.getConfig().getDatagramEnd();

	private static VCAResCtrlDao vcaResCtrlDao = new VCAResCtrlDao();

	private VCAResCtrlDao() {

	}

	/**
	 * VOD控制代理-资源控制类获取
	 * 
	 * @return VCAResCtrlDao VOD控制代理-资源控制类
	 */
	public static VCAResCtrlDao getInstance() {
		if (null == vcaResCtrlDao) {
			return vcaResCtrlDao = new VCAResCtrlDao();
		}

		return vcaResCtrlDao;
	}

	/**
	 * 
	 * 
	 * @param recvMsg
	 * @return
	 */
	public boolean checkParamForVODQuit(String recvMsg) {
		// 请求头和尾判断
		if (!Commons.isNullorEmptyString(recvMsg) && recvMsg.indexOf(head) >= 0
				&& recvMsg.indexOf(end) >= 0) {
			recvMsg = recvMsg.substring(recvMsg.indexOf(head) + head.length(),
					recvMsg.indexOf(end));
			String[] revArr = recvMsg.split("\\|");

			if (revArr.length == 3 && !Commons.isNullorEmptyString(revArr[0])
					&& !Commons.isNullorEmptyString(revArr[1])
					&& !Commons.isNullorEmptyString(revArr[2])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取请求模型数据
	 * 
	 * @param revStr
	 * @return VODQuitlReqBean 返回请求的数据的模型
	 */
	public VODQuitlReqBean getVODQuitBean(String recvMsg) {
		VODQuitlReqBean bean = new VODQuitlReqBean();
		recvMsg = recvMsg.substring(recvMsg.indexOf(head) + head.length(),
				recvMsg.indexOf(end));
		String[] revArr = recvMsg.split("\\|");
		bean.setCommand(revArr[1]);
		bean.setSerialNo(revArr[2]);
		return bean;
	}

	/**
	 * 返回成功信息
	 * 
	 * @return String 成功信息
	 */
	public String getOKMsgForVodQuit(String serialNo) {
		// VOD_CA_VOD_QUIT_REQP：VOD资源管理VOD退出应答指令字
		// return_code：返回是否成功，值>=0表示成功代码，值<0表示错误代码
		// serial_no：流水号
		StringBuffer retMsg = new StringBuffer("");
		retMsg.append(head);
		retMsg.append(VODConst.VOD_CA_VOD_PLAY_RESP);
		retMsg.append(VODConst.SEPARATOR);
		retMsg.append(VODConst.VOD_QUITE_OK);
		retMsg.append(VODConst.SEPARATOR);
		retMsg.append(serialNo);
		retMsg.append(end);

		return retMsg.toString();
	}

	/**
	 * 获得错误信息
	 * 
	 * @param revArr
	 * @return
	 */
	public String getWrongMsgForVodQuit() {
		// VOD_CA_VOD_QUIT_REQP：VOD资源管理VOD退出应答指令字
		// return_code：返回是否成功，值>=0表示成功代码，值<0表示错误代码
		// serial_no：流水号
		StringBuffer retMsg = new StringBuffer("");
		retMsg.append(head);
		retMsg.append(VODConst.VOD_CA_VOD_PLAY_RESP);
		retMsg.append(VODConst.SEPARATOR);
		retMsg.append(VODConst.VOD_QUITE_FAIL);
		retMsg.append(VODConst.SEPARATOR);
		retMsg.append("");
		retMsg.append(end);

		return retMsg.toString();
	}

}
