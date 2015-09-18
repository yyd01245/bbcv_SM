package org.eredlab.g4.cabs.web;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.cabs.service.AppGroupManagerService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.SerialnoUtil;
import org.eredlab.g4.common.util.client.IOSocketClient;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;

public class AppGroupManagerAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(AppGroupManagerAction.class);
	
	private AppGroupManagerService appGroupManagerService = (AppGroupManagerService) super.getService("appGroupManagerService");
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward appGroupManagerInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("initView");
	}

	/**
	 *  
	 * @param 跳转承载预下线页面
	 * @return
	 */
	public ActionForward toAimSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("toAimSession");
	}
	
	/**
	 *  
	 * @param 跳转电视上网承载页面
	 * @return
	 */
	public ActionForward toTvManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("toTvManage");
	}
	
	/**
	 *  
	 * @param 跳转RSM配置参数修改页面
	 * @return
	 */
	public ActionForward toRsmConf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("toRsmConf");
	}
	/**
	 * 查询
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryAppGroupManagerItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForPage("AppGroupManager.getAppGroupManagerListForPage", inDto);
		Integer totalCount = (Integer) crsmReader.queryForObject("AppGroupManager.getAppGroupManagerListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	/**
	 * 查询
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryValidGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForList("AppGroupManager.getAppGroupManagerValidList", inDto);
		Integer totalCount = codeList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 保存
	 * 
	 * @param
	 * @return
	 */
	public ActionForward saveAppGroupManagerItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer count = (Integer)crsmReader.queryForObject("AppGroupManager.IsNamed", inDto);//重名校验
		inDto.put("count", count);
		Dto outDto = appGroupManagerService.saveAppGroupManagerItem(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	

	/**
	 * 删除
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteAppGroupManagerItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		Dto outDto = appGroupManagerService.deleteAppGroupManagerItems(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
//		setOkTipMsg("应用组管理数据数据删除成功", response);
		return mapping.findForward(null);
	}

	/**
	 * 修改
	 * 
	 * @param
	 * @return
	 */
	public ActionForward updateAppGroupManagerItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer count = (Integer)crsmReader.queryForObject("AppGroupManager.IsUpdateNamed", inDto);
		inDto.put("count", count);
		Dto outDto =appGroupManagerService.updateAppGroupManagerItem(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
//		setOkTipMsg("应用组管理数据修改成功", response);
		return mapping.findForward(null);
	}
	
	/**
	 * 获取RSM配置文件参数
	 */
	public ActionForward queryRsmConf(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String vncw_ip = inDto.getAsString("vncw_ip");
		SerialnoUtil serial = new SerialnoUtil();
		String busi_code = (String) g4Reader.queryForObject("RsmConfBusi.queryMaxCode");
		if(busi_code==null||"".equals(busi_code)){
			busi_code="0";
		}
		int busi_code_id = Integer.parseInt(busi_code); //用户一次操作的记录标识
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();//操作员
		Date create_date = new Date(new java.util.Date().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Dto saveDto = new BaseDto();
		saveDto.put("busi_code", busi_code_id+1);
		saveDto.put("operater", account);
		saveDto.put("oper_time", sdf.format(create_date));
		Dto outDto = appGroupManagerService.saveBusiToDB(saveDto); //实现数据库记录存储
		/****************S  查询RSM配置文件参数开始    S*****************/
		Dto queryDto = new BaseDto();
		Dto retDto = new BaseDto() ;
		queryDto.put("cmd", "info_log");
		queryDto.put("type", "2");
		queryDto.put("serialno", serial.getRandomString(8));
		String queryStr = JsonHelper.encodeObject2Json(queryDto);
		String queryString = queryStr + "XXEE";
		log.info("【ADMIN】查询RSM配置数据发送报文===>" + queryString);
		
		
		
		IOSocketClient client = new IOSocketClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		String revStr = client.sendStr(vncw_ip, 28002, 5000, null, queryString,
				codecFactory, null);
		
		
		
		
//		String revStr ="{\"cmd\":\"info_log\",\"retcode\":\"0\",\"status\":\"running\",\"resourcemag.config\":[{\"RSM.AREA_ID\":\"0571\",\"RSM.C_TYPE\":\"1\",\"RSM.NETNAME\":\"eth0\",\"RSM.AIM_LISTENTPORT\":\"25000\",\"RSM.VNC_LISTENTPORT\":\"30000\",\"AIM.IP\":\"192.168.200.23\",\"AIM.PORT\":\"26000\",\"VNCW.IP\":\"192.168.200.23\",\"VNCW.PORT\":\"28000\",\"RSM.VID.QUOTA\":\"all_1280p\",\"RSM.NUM.HD\":\"20000\",\"RSM.NUM.SD\":\"0\",\"RSM.AUDIO\":\"1\",\"RSM.GOP.SIZE\":\"5\",\"RSM.VID.RATE\":\"7\",\"VNC.PORT\":\"48000\",\"RSM.VNCSERVER.PORT\":\"5900\",\"RSM.KEYLISTEN.PORT\":\"46000\",\"RSM.PA.AMOUNT\":\"18\",\"RSM.UID\":\"1000\",\"RSM.LOG_FILE\":\"sourcemag.log\",\"RSM.LOG_LEVEL\":\"3\",\"RSM.PROCESS_THREADS_AIM\":\"91\",\"RSM.PROCESS_THREADS_VNC\":\"99\"}],\"vncconfig.ini\":[{\"area_id\":\"0571\",\"maxcount\":\"100\",\"vncname\":\"/opt/xencoder/tools/Xvnc\",\"sd_width\":\"720\",\"sd_height\":\"576\",\"hd_width\":\"1280\",\"hd_height\":\"720\",\"vncserverport\":\"5900\",\"recordadd\":\"0\",\"pa_amount\":\"8\",\"uid\":\"1000\",\"chrome_type\":\"0\",\"TVnet\":\"0\",\"key_ignore\":\"0\",\"proxy\":\"218.108.0.82:6080\",\"listenport\":\"48000\",\"keytimeoutport\":\"30000\",\"keylistenport\":\"46000\",\"timeoutforecast\":\"10\",\"log_file_path\":\"./vncms_log\",\"log_file\":\"vnc.log\",\"log_level\":\"4\",\"process_threads_aim\":\"1\"}],\"serialno\":\"123456789\"}";
		log.info("【ADMIN】查询RSM配置数据接收报文===>" + revStr);
		revStr = revStr.replace("XXEE", "");
		if (!(revStr == null || "".equals(revStr))) {
			String JsonStr = JsonHelper.encodeObject2Json(revStr);
			Dto dto = JsonHelper.parseSingleJson2Dto(JsonStr);
			if(dto.getAsInteger("retcode")<0){
				retDto.put("success", new Boolean(false));
			}else{
				List rsmConf = dto.getAsList("resourcemag.config");
				if(rsmConf.size()>0){
					Object rsmConfDir = rsmConf.get(0);
					String rsm = JsonHelper.encodeObject2Json(rsmConfDir);
					Dto rsmDto = JsonHelper.parseSingleJson2Dto(rsm);
					dto.putAll(rsmDto);
				}
				List vncConf = dto.getAsList("vncconfig.ini");
				if(vncConf.size()>0){
					Object vncConfDir = vncConf.get(0);
					String vnc = JsonHelper.encodeObject2Json(vncConfDir);
					Dto vncDto = JsonHelper.parseSingleJson2Dto(vnc);
					dto.putAll(vncDto);
				}
				dto.remove("resourcemag.config");
				dto.remove("vncconfig.ini");
				retDto.putAll(dto);
				retDto.put("success", new Boolean(true));
				retDto.put("busi_code", busi_code_id+1);
				retDto.put("busi_type", "查询");
				retDto.put("RSM_AREA_ID", retDto.get("RSM.AREA_ID"));
				retDto.put("RSM_C_TYPE", retDto.get("RSM.C_TYPE"));
				retDto.put("RSM_NETNAME", retDto.get("RSM.NETNAME"));
				retDto.put("RSM_AIM_LISTENTPORT", retDto.get("RSM.AIM_LISTENTPORT"));
				retDto.put("RSM_VNC_LISTENTPORT", retDto.get("RSM.VNC_LISTENTPORT"));
				retDto.put("AIM_IP", retDto.get("AIM.IP"));
				retDto.put("AIM_PORT", retDto.get("AIM.PORT"));
				retDto.put("VNCW_IP", retDto.get("VNCW.IP"));
				retDto.put("VNCW_PORT", retDto.get("VNCW.PORT"));
				retDto.put("RSM_VID_QUOTA", retDto.get("RSM.VID.QUOTA"));
				retDto.put("RSM_NUM_HD", retDto.get("RSM.NUM.HD"));
				retDto.put("RSM_NUM_SD", retDto.get("RSM.NUM.SD"));
				retDto.put("RSM_AUDIO", retDto.get("RSM.AUDIO"));
				retDto.put("RSM_GOP_SIZE", retDto.get("RSM.GOP.SIZE"));
				retDto.put("RSM_VID_RATE", retDto.get("RSM.VID.RATE"));
				retDto.put("VNC_PORT", retDto.get("VNC.PORT"));
				retDto.put("RSM_VNCSERVER_PORT", retDto.get("RSM.VNCSERVER.PORT"));
				retDto.put("RSM_KEYLISTEN_PORT", retDto.get("RSM.KEYLISTEN.PORT"));
				retDto.put("RSM_PA_AMOUNT", retDto.get("RSM.PA.AMOUNT"));
				retDto.put("RSM_UID", retDto.get("RSM.UID"));
				retDto.put("RSM_LOG_FILE", retDto.get("RSM.LOG_FILE"));
				retDto.put("RSM_LOG_LEVEL", retDto.get("RSM.LOG_LEVEL"));
				retDto.put("RSM_LOG_FILE_PATH", retDto.get("RSM.LOG_FILE_PATH"));
				retDto.put("RSM_PROCESS_THREADS_AIM", retDto.get("RSM.PROCESS_THREADS_AIM"));
				retDto.put("RSM_PROCESS_THREADS_VNC", retDto.get("RSM.PROCESS_THREADS_VNC"));
				Dto outtDto = appGroupManagerService.saveBusiDetailToDB(retDto);// 实现事务详情数据存储
			}
		}else{
			retDto.put("success", new Boolean(false));
		}
		/****************E  查询RSM配置文件参数结束    E*****************/
		write(JsonHelper.encodeDto2FormLoadJson(retDto, G4Constants.FORMAT_Date), response);
		return mapping.findForward(null);
	}
	
	/**
	 * 修改RSM配置文件参数
	 */
	public ActionForward saveRsmConf(ActionMapping mapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		inDto.put("RSM_AREA_ID", inDto.get("RSM.AREA_ID"));
		inDto.put("RSM_C_TYPE", inDto.get("RSM.C_TYPE"));
		inDto.put("RSM_NETNAME", inDto.get("RSM.NETNAME"));
		inDto.put("RSM_AIM_LISTENTPORT", inDto.get("RSM.AIM_LISTENTPORT"));
		inDto.put("RSM_VNC_LISTENTPORT", inDto.get("RSM.VNC_LISTENTPORT"));
		inDto.put("AIM_IP", inDto.get("AIM.IP"));
		inDto.put("AIM_PORT", inDto.get("AIM.PORT"));
		inDto.put("VNCW_IP", inDto.get("VNCW.IP"));
		inDto.put("VNCW_PORT", inDto.get("VNCW.PORT"));
		inDto.put("RSM_VID_QUOTA", inDto.get("RSM.VID.QUOTA"));
		inDto.put("RSM_NUM_HD", inDto.get("RSM.NUM.HD"));
		inDto.put("RSM_NUM_SD", inDto.get("RSM.NUM.SD"));
		inDto.put("RSM_AUDIO", inDto.get("RSM.AUDIO"));
		inDto.put("RSM_GOP_SIZE", inDto.get("RSM.GOP.SIZE"));
		inDto.put("RSM_VID_RATE", inDto.get("RSM.VID.RATE"));
		inDto.put("VNC_PORT", inDto.get("VNC.PORT"));
		inDto.put("RSM_VNCSERVER_PORT", inDto.get("RSM.VNCSERVER.PORT"));
		inDto.put("RSM_KEYLISTEN_PORT", inDto.get("RSM.KEYLISTEN.PORT"));
		inDto.put("RSM_PA_AMOUNT", inDto.get("RSM.PA.AMOUNT"));
		inDto.put("RSM_UID", inDto.get("RSM.UID"));
		inDto.put("RSM_LOG_FILE", inDto.get("RSM.LOG_FILE"));
		inDto.put("RSM_LOG_LEVEL", inDto.get("RSM.LOG_LEVEL"));
		inDto.put("RSM_LOG_FILE_PATH", inDto.get("RSM.LOG_FILE_PATH"));
		inDto.put("RSM_PROCESS_THREADS_AIM", inDto.get("RSM.PROCESS_THREADS_AIM"));
		inDto.put("RSM_PROCESS_THREADS_VNC", inDto.get("RSM.PROCESS_THREADS_VNC"));
		inDto.put("busi_type", "修改");
		Dto outtDto = appGroupManagerService.saveBusiDetailToDB(inDto);// 实现事务修改详情数据存储
		
		String vncw_ip = inDto.getAsString("vncw_ip");
		SerialnoUtil serial = new SerialnoUtil();
		Dto queryDto = new BaseDto();
		Dto retDto = new BaseDto() ;
		Dto resDto = new BaseDto();
		resDto.put("RSM.AREA_ID", inDto.get("RSM.AREA_ID"));
		resDto.put("RSM.C_TYPE", inDto.get("RSM.C_TYPE"));
		resDto.put("RSM.NETNAME", inDto.get("RSM.NETNAME"));
		resDto.put("RSM.AIM_LISTENTPORT", inDto.get("RSM.AIM_LISTENTPORT"));
		resDto.put("RSM.VNC_LISTENTPORT", inDto.get("RSM.VNC_LISTENTPORT"));
		resDto.put("AIM.IP", inDto.get("AIM.IP"));
		resDto.put("AIM.PORT", inDto.get("AIM.PORT"));
		resDto.put("VNCW.IP", inDto.get("VNCW.IP"));
		resDto.put("VNCW.PORT", inDto.get("VNCW.PORT"));
		resDto.put("RSM.VID.QUOTA", inDto.get("RSM.VID.QUOTA"));
		resDto.put("RSM.NUM.HD", inDto.get("RSM.NUM.HD"));
		resDto.put("RSM.NUM.SD", inDto.get("RSM.NUM.SD"));
		resDto.put("RSM.AUDIO", inDto.get("RSM.AUDIO"));
		resDto.put("RSM.GOP.SIZE", inDto.get("RSM.GOP.SIZE"));
		resDto.put("RSM.VID.RATE", inDto.get("RSM.VID.RATE"));
		resDto.put("VNC.PORT", inDto.get("VNC.PORT"));
		resDto.put("RSM.VNCSERVER.PORT", inDto.get("RSM.VNCSERVER.PORT"));
		resDto.put("RSM.KEYLISTEN.PORT", inDto.get("RSM.KEYLISTEN.PORT"));
		resDto.put("RSM.PA.AMOUNT", inDto.get("RSM.PA.AMOUNT"));
		resDto.put("RSM.UID", inDto.get("RSM.UID"));
		resDto.put("RSM.LOG_FILE", inDto.get("RSM.LOG_FILE"));
		resDto.put("RSM.LOG_LEVEL", inDto.get("RSM.LOG_LEVEL"));
		resDto.put("RSM.LOG_FILE_PATH", inDto.get("RSM.LOG_FILE_PATH"));
		resDto.put("RSM.PROCESS_THREADS_AIM", inDto.get("RSM.PROCESS_THREADS_AIM"));
		resDto.put("RSM.PROCESS_THREADS_VNC", inDto.get("RSM.PROCESS_THREADS_VNC"));
		String resConfRet = JsonHelper.encodeObject2Json(resDto);
		String resConfRets[] = new String[1];
		resConfRets[0] = resConfRet;
		Dto vncDto = new BaseDto();
		vncDto.put("area_id", inDto.getAsString("area_id"));
		vncDto.put("maxcount", inDto.getAsString("maxcount"));
		vncDto.put("vncname", inDto.getAsString("vncname"));
		vncDto.put("sd_width", inDto.getAsString("sd_width"));
		vncDto.put("sd_height", inDto.getAsString("sd_height"));
		vncDto.put("hd_width", inDto.getAsString("hd_width"));
		vncDto.put("hd_height", inDto.getAsString("hd_height"));
		vncDto.put("recordadd", inDto.getAsString("recordadd"));
		vncDto.put("vncserverport", inDto.getAsString("vncserverport"));
		vncDto.put("pa_amount", inDto.getAsString("pa_amount"));
		vncDto.put("uid", inDto.getAsString("uid"));
		vncDto.put("chrome_type", inDto.getAsString("chrome_type"));
		vncDto.put("TVnet", inDto.getAsString("TVnet"));
		vncDto.put("key_ignore", inDto.getAsString("key_ignore"));
		vncDto.put("proxy", inDto.getAsString("proxy"));
		vncDto.put("listenport", inDto.getAsString("listenport"));
		vncDto.put("keytimeoutport", inDto.getAsString("keytimeoutport"));
		vncDto.put("keylistenport", inDto.getAsString("keylistenport"));
		vncDto.put("timeoutforecast", inDto.getAsString("timeoutforecast"));
		vncDto.put("log_file_path", inDto.getAsString("log_file_path"));
		vncDto.put("log_file", inDto.getAsString("log_file"));
		vncDto.put("log_level", inDto.getAsString("log_level"));
		vncDto.put("process_threads_aim", inDto.getAsString("process_threads_aim"));
		String vncConfRet = JsonHelper.encodeObject2Json(vncDto);
		String vncConfRets[] = new String[1];
		vncConfRets[0] = vncConfRet;
		queryDto.put("cmd", "modify_log");
		queryDto.put("type", "2");
		queryDto.put("resourcemag.config", resConfRets);
		queryDto.put("vncconfig.ini", vncConfRets);
		queryDto.put("serialno", serial.getRandomString(8));
		String saveStr = JsonHelper.encodeObject2Json(queryDto);
		String saveString = saveStr + "XXEE";
		log.info("【ADMIN】修改RSM配置数据发送报文===>" + saveString);
		IOSocketClient client = new IOSocketClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		String revStr = client.sendStr(vncw_ip, 28002, 5000, null, saveString,
				codecFactory, null);
//		String revStr ="{\"cmd\":\"modify_log\",\"retcode\":\"0\",\"msg\":\"RSM wil reboot\",\"serialno\":\"12345678\"}XXEE";
		log.info("【ADMIN】修改RSM配置数据接收报文===>" + revStr);
		revStr = revStr.replace("XXEE", "");
		if (!(revStr == null || "".equals(revStr))) {
			String JsonStr = JsonHelper.encodeObject2Json(revStr);
			Dto dto = JsonHelper.parseSingleJson2Dto(JsonStr);
			if(dto.getAsInteger("retcode")<0){
				retDto.put("success", new Boolean(false));
			}else{
				retDto.put("success", new Boolean(true));
			}
		}else{
			retDto.put("success", new Boolean(false));
		}
		write(JsonHelper.encodeObject2Json(retDto), response);
		return mapping.findForward(null);
	}
}
