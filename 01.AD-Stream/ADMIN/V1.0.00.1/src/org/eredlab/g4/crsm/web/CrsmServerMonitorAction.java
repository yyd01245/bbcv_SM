package org.eredlab.g4.crsm.web;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.SerialnoUtil;
import org.eredlab.g4.common.util.client.IOSocketClient;
import org.eredlab.g4.crsm.service.CrsmServerMonitorService;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;

public class CrsmServerMonitorAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(CrsmServerMonitorAction.class);
	
	private CrsmServerMonitorService serverMonitorService = (CrsmServerMonitorService) super.getService("crsmServerMonitorService");
	PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	String authname = ph.getValue("crsm.auth.authname");
	String authcode = ph.getValue("crsm.auth.authcode");
	String ip = ph.getValue("crsm.tcpserver.ip");
	int port = Integer.valueOf(ph.getValue("crsm.tcpserver.port"));
	
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	
	public ActionForward toMoreServer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("toMoreServer");
	}
	/**
	 *  
	 * @param 跳转chrome插件升级页面
	 * @return
	 */
	public ActionForward toChrome(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("toChrome");
	}
	/**
	 *  
	 * @param 跳转承载预下线页面
	 * @return
	 */
	public ActionForward toAim(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("toAim");
	}
	/**
	 *  
	 * @param 跳转承载维护监控页面
	 * @return
	 */
	public ActionForward toManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("toManage");
	}
	/**
	 * 查询服务器列表
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryServerItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		List groupList = crsmReader.queryForList("AppGroupManager.getAppGroupManagerValidList", inDto);
		List resp = new ArrayList();
		for(int i=0;i<groupList.size();i++){
			String groupid = ((BaseDto)groupList.get(i)).getAsString("group_id");
			Dto groupDto = new BaseDto();
			groupDto.put("group_id", groupid);
			List serverList = crsmReader.queryForList("CrsmServerMonitor.getServerMonitorList", groupDto);
			Integer totalCount = serverList.size();
			String jsonStrList = JsonHelper.encodeList2PageJson(serverList, totalCount, null);
			resp.add(jsonStrList);
		}
		
		//Integer totalCount = new Integer(codeList.size());
		
		write(resp.toString(), response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryServerByGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForList("CrsmServerMonitor.getServerMonitorList", inDto);
		Integer totalCount = codeList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询服务器具体信息
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryServerDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		BaseDto object = (BaseDto)crsmReader.queryForObject("CrsmServerMonitor.getServerByKey", inDto);
		String jsonStrList = JsonHelper.encodeObject2Json(object);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询status=3的服务器信息
	 * 服务器监控页面展示
	 */
	public ActionForward queryServerStatusDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List objectList = crsmReader.queryForList("CrsmServerMonitor.getServerStatusList", inDto);
		
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(objectList,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryServerStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List objectList = crsmReader.queryForList("CrsmServerMonitor.getServerStatusDetail", inDto);
		
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(objectList,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryServerStatusDetailList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List objectList = crsmReader.queryForList("CrsmServerMonitor.getServerStatusDetailList", inDto);
		List list = new ArrayList();
		if(objectList.size()>0){
			for(int i=0;i<objectList.size();i++){
				Dto outDto = (Dto)objectList.get(i);
				String server_ip = outDto.getAsString("server_ip");
				String server_port = outDto.getAsString("server_port");
				String server_id=server_ip+":"+server_port;
				outDto.put("server_id", server_id);
				list.add(outDto);
			}
		}
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(list,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	/**
	 * 调用crsm接口，查询单个承载详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryServerSingle(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String server_id = inDto.getAsString("nodeName");
		String[] server_ids = server_id.split(":");
		inDto.put("server_ip", server_ids[0]);
		inDto.put("server_port", server_ids[1]);
		Dto outDto = serverMonitorService.queryServerSingle(inDto);
		Dto retDto = new BaseDto();
		if(outDto.getAsBoolean("success")){
			List objectList = crsmReader.queryForList("CrsmServerMonitor.queryServerSingle", inDto);
			if(objectList.size()>0){
				retDto=(Dto)objectList.get(0);
				retDto.put("success", new Boolean(true));
			}else{
				retDto.put("success", new Boolean(false));
			}
		}else{
			List objectList = crsmReader.queryForList("CrsmServerMonitor.queryServerSingle", inDto);
			if(objectList.size()>0){
				retDto=(Dto)objectList.get(0);
				retDto.put("success", new Boolean(true));
			}else{
				retDto.put("success", new Boolean(false));
			}
		}
		String retString = JsonHelper.encodeDto2FormLoadJson(retDto,G4Constants.FORMAT_Date);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 根据承载IP查询在线资源详情
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward querySesionDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String retString = serverMonitorService.querySesionDetail(inDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 保存远程配置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward savaConfigById(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = serverMonitorService.savaConfigById(inDto);	
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 查询机柜绑定信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward querycabinet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List cabinetList= crsmReader.queryForList("CrsmServerMonitor.querycabinetList", inDto);	
		String retString = JsonHelper.encodeList2PageJson(cabinetList, cabinetList.size(), null);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 查询机柜编号信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward querycard(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String cabinet_address = URLDecoder.decode(inDto.getAsString("cabinet_address"), "utf-8");
		inDto.put("cabinet_address", cabinet_address);
		List cardList= crsmReader.queryForList("CrsmServerMonitor.querycardList", inDto);	
		String retString = JsonHelper.encodeList2PageJson(cardList, cardList.size(), null);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 保存机柜信息到数据库
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward savaCabinet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer sum =(Integer) crsmReader.queryForObject("CrsmServerMonitor.queryCabinetAddress",inDto);//判断机柜地址是否已存在
		inDto.put("sum", sum);
		Dto outDto = serverMonitorService.saveCabinet(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 绑定机柜
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward savaBanding(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = serverMonitorService.savaBanding(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 承载预下线操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward serverPerOfflineOperate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		inDto.put("account", account);
		Dto outDto = serverMonitorService.serverPerOfflineOperate(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 承载服务器下架
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward serverOfflineOperate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		inDto.put("account", account);
		Dto outDto = serverMonitorService.serverOfflineOperate(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 承载上线操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward serverOnlineOperate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		inDto.put("account", account);
		Dto outDto = serverMonitorService.serverOnlineOperate(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 承载认证操作
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward serverAuthOperate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		inDto.put("account", account);
		Dto outDto = serverMonitorService.serverAuthOperate(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 *释放在线资源
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward killSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = serverMonitorService.killSession(inDto);
		if(outDto.getAsBoolean("success")){
		   queryServerByApiAndInsertDB(mapping,form,request,response);
		}
		String respStr = JsonHelper.encodeObject2Json(outDto);
		write(respStr, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询机柜信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward querycabinetList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List cabinetList= crsmReader.queryForList("CrsmServerMonitor.querycabinetAllList", inDto);	
		List list = new ArrayList();
		if(cabinetList.size()>0){
			for(int i = 0;i<cabinetList.size();i++){
				Dto cabinetDto = (Dto) cabinetList.get(i);
				List serverList= crsmReader.queryForList("CrsmServerMonitor.queryServerListByCabinetAddress", cabinetDto);
				cabinetDto.put("list", serverList);
				cabinetDto.put("listsize", serverList.size());
				list.add(cabinetDto);
			}
		}
		String retString = JsonHelper.encodeList2PageJson(list, list.size(), null);
//		String retString = JsonHelper.encodeList2PageJson(cabinetList, cabinetList.size(), null);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询机柜下服务器信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryServerList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List serverList= crsmReader.queryForList("CrsmServerMonitor.queryServerListByCabinetAddress", inDto);	
		String retString = JsonHelper.encodeList2PageJson(serverList, serverList.size(), null);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 删除机柜
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward deleteCabinetItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = serverMonitorService.deleteCabinet(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 修改机柜信息，（先删除再添加）
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateCabinetInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = serverMonitorService.deleteCabinet(inDto);
		String success = outDto.getAsString("success");
		if(success=="true"){
			inDto.put("sum", 0);
			outDto = serverMonitorService.saveCabinet(inDto);
		}
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 根据状态查询承载信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryServerByStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List serverList= crsmReader.queryForList("CrsmServerMonitor.queryServerByStatus", inDto);	
		List list = new ArrayList();
		if(serverList.size()>0){
			for (Object object : serverList) {
				Dto outDto = (Dto)object;
				String server_ip = outDto.getAsString("server_ip");
				String server_port = outDto.getAsString("server_port");
				String server_id = server_ip+":"+server_port;
				outDto.put("server_id", server_id);
				list.add(outDto);
			}
		}
		String retString = JsonHelper.encodeList2PageJson(list, list.size(), null);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 调用API查询承载列表并存入数据库
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryServerByApiAndInsertDB(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cFrom = (CommonActionForm)form;
		Dto inDto = cFrom.getParamAsDto(request);
		serverMonitorService.selectListAndInsertDB(inDto);
//		String token = CacheTempData.AUTHTOKEN.get("token");
//		List insertList = new ArrayList();//批量插入List
//		List updateList = new ArrayList();//批量插入List
//		if(token!=null&&!"".equals(token)){
//			QueryManager crsmQuery=CRSMUtil.getQueryManager(token);//token需要通过tcp连接去获取
//			Map<String,RsmAttribute> crsmMap = crsmQuery.getResourcesWithAttributes();
//			System.out.println("服务器列表查询："+crsmMap.toString());
//			for(Map.Entry entry : crsmMap.entrySet()){
//				Dto dto = new BaseDto();
//				RsmAttribute rsmEntry = (RsmAttribute)entry.getValue(); //承载详情
//				long free_num = rsmEntry.getFreenum();
//				long max_num = rsmEntry.getMaxnum();
//				long online_num = max_num - free_num;
//				String vncid = rsmEntry.getVncip()+":"+rsmEntry.getVncport();
//				log.info("全量同步，承载"+vncid+"详情："+rsmEntry);
//				String vstatus = rsmEntry.getStatus();
//				if(vstatus=="unused"||"unused".equals(vstatus)){
//					Map<String, RsmResource> map = crsmQuery.getUserResourcesByRsm(vncid);
//					log.info("全量同步，承载"+vncid+"没有用户在线："+map.isEmpty());
//					if(!map.isEmpty())
//						vstatus="perunused";
//				}
//				log.info("全量同步，"+vncid+"状态为:"+vstatus);
//				dto.put("vstatus",vstatus);
//				dto.put("online_num",online_num );
//				dto.put("max_num", rsmEntry.getMaxnum());
//				dto.put("liuhua_num", rsmEntry.getStreamnum());
//				dto.put("rate", rsmEntry.getRate());//码率
//				dto.put("security", rsmEntry.getSecurity());//安全等级
//				dto.put("vendor", rsmEntry.getVendor());
//				dto.put("vtype", rsmEntry.getVideotype());
//				dto.put("server_ip", rsmEntry.getVncip());
//				dto.put("server_port", rsmEntry.getVncport());
//				dto.put("vability", rsmEntry.getVnctype());
//				log.info("定时同步RSM资源到数据库："+JsonHelper.encodeObject2Json(dto));
//				Integer sum =(Integer) crsmReader.queryForObject("CrsmServerMonitor.queryServerCountByID", dto);//查看当前承载是否已在数据库
//				if(sum==0){
//					insertList.add(dto);
//				}else{
//					updateList.add(dto);
//				}
//			}
//			serverMonitorService.updateServerStatus(inDto);
//			Thread.sleep(100);
//			serverMonitorService.saveServerMore(insertList);
//			Thread.sleep(100);
//			serverMonitorService.updateServerMore(updateList);
//		}else{
//			Dto queryDto = new BaseDto();
//			SerialnoUtil serialnoUtil = new SerialnoUtil();
//			String serialno = serialnoUtil.getRandomString(8);
//			queryDto.put("cmd", "auth");
//			queryDto.put("authname", authname);
//			queryDto.put("authcode", authcode);
//			queryDto.put("msg", "");
//			queryDto.put("serialno", serialno);
//			String queryStr = JsonHelper.encodeObject2Json(queryDto);
//			String queryString = queryStr + "XXEE";
//			IOSocketClient client = new IOSocketClient();
//			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
//					CommandSupport.CHARSET, CommandSupport.TAIL,
//					CommandSupport.TAIL);
//			String revStr = client.sendStr(ip, port, 5000, null, queryString,
//					codecFactory, null);
//			revStr = revStr.replace("XXEE", "");
//			if(revStr!=null && !"".equals(revStr)){
//				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
//				int retcode = dto.getAsInteger("retcode");
//				if (retcode < 0) {
//					log.error("【ERROR】与CRSM交互获取token失败，请查看二者配置文件中认证参数是否一致！！！！");
//				} else {
//					CacheTempData.AUTHTOKEN.put("token", dto.getAsString("token"));
//				}
//			}else{
//				log.error("【ERROR】与CRSM交互获取token失败，请确认CRSM地址和端口是否正确！！！！");
//			}
//		}
		return mapping.findForward(null);
	}
	/**
	 * 上传chrome升级包
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateChromePlush(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cForm = (CommonActionForm) form;
		Dto inDto = cForm.getParamAsDto(request);
		String server_ips = inDto.getAsString("server_ip");
		String[] server_id = server_ips.split(",");
		Dto outDto = new BaseDto();
		FormFile myFile = cForm.getFile1();
		String contextPath = getServlet().getServletContext().getRealPath("/");
		String context = request.getContextPath();
		String savePath = contextPath.substring(0, contextPath.indexOf(context.substring(1)))  + "ROOT/upload/";
		// 检查路径是否存在,如果不存在则创建之
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		// 文件真实文件名
		String fileName = myFile.getFileName();
		// 我们一般会根据某种命名规则对其进行重命名
		File fileToCreate = new File(savePath, fileName);
		// 检查同名文件是否存在,不存在则将文件流写入文件磁盘系统
		if (!fileToCreate.exists()) {
			FileOutputStream os = new FileOutputStream(fileToCreate);
			os.write(myFile.getFileData());
			os.flush();
			os.close();
		} else {
			// 此路径下已存在同名文件,是否要覆盖或给客户端提示信息由你自己决定
			fileToCreate.delete();
			FileOutputStream os = new FileOutputStream(new File(savePath,fileName));
			os.write(myFile.getFileData());
			os.flush();
			os.close();
		}
		// 我们通常还会把这个文件的相关信息持久化到数据库
		String result="";
		String url = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+"/upload/";
		for(int i=0;i<server_id.length;i++){
			SerialnoUtil serialnoUtil = new SerialnoUtil();
			Dto updateDto = new BaseDto();//chrome插件升级报文
			updateDto.put("cmd", "chrplugin");
			updateDto.put("filename", fileName);
			updateDto.put("url", url);
			updateDto.put("serialno", serialnoUtil.getRandomString(8));
//			String ipdatemsg = JsonHelper.encodeObject2Json(updateDto);
//			Dto rsmwDto = new BaseDto();//CRSM透传给RSMW
//			rsmwDto.put("cmd", "manage");
//			rsmwDto.put("msg", updateDto);
//			rsmwDto.put("serialno", serialnoUtil.getRandomString(8));
//			String rsmwmsg = JsonHelper.encodeObject2Json(rsmwDto);
			Dto crsmDto = new BaseDto();//与CRSM交互
			crsmDto.put("cmd", "manage");
			crsmDto.put("vncid", server_id[i]);
			crsmDto.put("authname", authname);
			crsmDto.put("authcode", authcode);
			crsmDto.put("msg", updateDto);
			crsmDto.put("serialno", serialnoUtil.getRandomString(8));
			String queryStr = JsonHelper.encodeObject2Json(crsmDto);
			String queryString = queryStr + "XXEE";
			log.info("【ADMIN】chrome插件升级发送报文===>" + queryString);
			IOSocketClient client = new IOSocketClient();
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
					CommandSupport.CHARSET, CommandSupport.TAIL,
					CommandSupport.TAIL);
			String revStr = client.sendStr(ip, port, 5000, null, queryString,
					codecFactory, null);
			log.info("【ADMIN】chrome插件升级接收报文===>" + revStr);
			revStr = revStr.replace("XXEE", "");
			if(revStr==null||"".equals(revStr)){
				outDto.put("success", new Boolean(false));
				result+="服务器："+server_id[i]+"chrome插件升级事件触发失败！<br>";
			}else{
				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
				int retcode = dto.getAsInteger("retcode");
				Calendar calendar = Calendar.getInstance();
				if (retcode < 0) {
					outDto.put("success", new Boolean(false));
					result+="服务器："+server_id[i]+"chrome插件升级事件触发失败！<br>";
				} else {
					String msg = JsonHelper.encodeObject2Json(dto.get("msg"));
					Dto cfgRetDto = JsonHelper.parseSingleJson2Dto(msg);
					int cfgRetCode = cfgRetDto.getAsInteger("retcode");
					if(cfgRetCode<0){
						outDto.put("success", new Boolean(false));
						result+="服务器："+server_id[i]+"chrome插件升级事件触发失败！<br>";
					}else{
						outDto.put("success", new Boolean(true));
						result+="服务器："+server_id[i]+"chrome插件升级事件触发成功！<br>";
					}
				}
			}
		}
		outDto.put("result", result);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	/**
	 * 无状态获取所有承载
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryServerListNoStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List serverList= crsmReader.queryForList("CrsmServerMonitor.queryServerListNoStatus", inDto);	
		List list = new ArrayList();
		if(serverList.size()>0){
			for (Object object : serverList) {
				Dto outDto = (Dto)object;
				String server_ip = outDto.getAsString("server_ip");
				String server_port = outDto.getAsString("server_port");
				String server_id = server_ip+":"+server_port;
				outDto.put("server_id", server_id);
				list.add(outDto);
			}
		}
		String retString = JsonHelper.encodeList2PageJson(list, list.size(), null);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 解绑机柜
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward serverDeleteCabinet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = serverMonitorService.serverDeleteCabinet(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 获取单台承载信息BYID
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryServerSingleByID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto listDto = (Dto)crsmReader.queryForObject("CrsmServerMonitor.queryServerSingle", inDto);
		String retString = JsonHelper.encodeObject2Json(listDto);
		write(retString, response);
		return mapping.findForward(null);
	}
}
