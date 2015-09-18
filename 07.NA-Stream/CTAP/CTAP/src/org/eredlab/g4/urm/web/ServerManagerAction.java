package org.eredlab.g4.urm.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.client.TcpClient;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.AccessGatewayConfigService;

public class ServerManagerAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(ServerManagerAction.class);
	
	
	private AccessGatewayConfigService accessGatewayConfigService = (AccessGatewayConfigService)super.getService("accessGatewayConfigService");
	
	PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	String watchDogIp = ph.getValue("watchdog.server.ip");
	String watchDogPort = ph.getValue("watchdog.server.port");
	
	
	
	/**
	 * 页面初始化
	 */

	public ActionForward toManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("服务器管理首页初始化方法（toManage）");
		return mapping.findForward("toManage");
	}
	

	/**
	 *  
	 * 查询接入网关列表
	 * 
	 */
	public ActionForward queryCagGatewayInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			
		log.info("查询所有接入网关（queryGatewayConfigList）");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		
		List objectList = urmReader.queryForList("ServerManager.getCagGatewayConfigList", inDto);
		
		log.info("查询出接入网关总数："+objectList.size());
		
		List list = new ArrayList();
		if(objectList.size()>0){
			for(int i=0;i<objectList.size();i++){
				Dto outDto = (Dto)objectList.get(i);
				String server_ip = outDto.getAsString("cag_ip");
				String server_port = outDto.getAsString("cag_port");
				String cag_id = outDto.getAsString("cag_id");
			//	String server_id=server_ip+":"+server_port;
				outDto.put("server_ip", server_ip);
				outDto.put("server_port", server_port);
				outDto.put("cag_id", cag_id);
				list.add(outDto);
			}
		}
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(list,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	
	
	/**
	 *  
	 * 查询键值网关列表
	 * 
	 */
	public ActionForward queryCkgGatewayInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			
		
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		log.info("查询接入网关cag_id=【"+inDto.getAsString("cag_id")+"】下所有键值网关（queryCkgGatewayConfigList）");
	
		List objectList = urmReader.queryForList("ServerManager.getCkgGatewayConfigList", inDto);
		
		log.info("查询出键值网关总数： "+objectList.size());
		List list = new ArrayList();
		if(objectList.size()>0){
			for(int i=0;i<objectList.size();i++){
				Dto outDto = (Dto)objectList.get(i);
				String server_ip = outDto.getAsString("ckg_ip");
				String server_port = outDto.getAsString("ckg_port");
				String cag_id = outDto.getAsString("cag_id");
			//	String server_id=server_ip+":"+server_port;
				outDto.put("server_ip", server_ip);
				outDto.put("ckg_id", cag_id);
				outDto.put("server_port", server_port);
				list.add(outDto);
			}
		}
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(list,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	
	
	
	
	/**
	 *  
	 * 查询信令网关列表
	 * 
	 */
	public ActionForward queryCsgGatewayInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			

		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		log.info("查询接入网关cag_id=【"+inDto.getAsString("cag_id")+"】下所有信令网关（queryCsgGatewayConfigList），");
		List objectList = urmReader.queryForList("ServerManager.getCsgGatewayConfigList", inDto);
		
		log.info("查询出信令网关总数： "+objectList.size());
		
		List list = new ArrayList();
		if(objectList.size()>0){
			for(int i=0;i<objectList.size();i++){
				Dto outDto = (Dto)objectList.get(i);
				String server_ip = outDto.getAsString("csg_ip");
				String server_port = outDto.getAsString("csg_port");
				String cag_id = outDto.getAsString("cag_id");
			//	String server_id=server_ip+":"+server_port;
				outDto.put("server_ip", server_ip);
				outDto.put("server_port", server_port);
				outDto.put("cag_id", cag_id);
				
				list.add(outDto);
			}
		}
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(list,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	
	/**
	 *  
	 * 查询网关配置文件列表
	 * 
	 */
	public ActionForward queryGatewayConfigFileList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			
		log.info("查询网关配置列表（queryGatewayConfigList）");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		
		log.info("查找对应配置文件的参数：config_ip="+inDto.getAsString("config_ip")+" config_port="+inDto.getAsString("config_port")+" config_name="+inDto.getAsString("config_name"));
	
		List objectList = urmReader.queryForList("ServerManager.getGatewayConfigFileList", inDto);
		
		System.out.println("查找出该网关配置项共  objectList.size 【"+objectList.size()+"】条");
//		List list = new ArrayList();
//		if(objectList.size()>0){
//			for(int i=0;i<objectList.size();i++){
//				Dto outDto = (Dto)objectList.get(i);
//				String server_ip = outDto.getAsString("csg_ip");
//				String server_port = outDto.getAsString("csg_port");
//				String cag_id = outDto.getAsString("cag_id");
//			//	String server_id=server_ip+":"+server_port;
//				outDto.put("server_ip", server_ip);
//				outDto.put("server_port", server_port);
//				outDto.put("cag_id", cag_id);
//				
//				list.add(outDto);
//			}
//		}
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(objectList,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	
	
	
	
	/**
	 *  
	 * 查询单个键值网关服务器信息
	 * 
	 */
	public ActionForward queryCkgGatewayInfoByIpPort(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
				
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		log.info("查询键值网关【"+inDto.getAsString("ckg_ip")+":"+inDto.getAsString("ckg_port")+"】的信息（queryCsgGatewayConfigList），");
		List objectList = urmReader.queryForList("ServerManager.getCkgGatewayInfoByIpPort", inDto);
		
		log.info("查询出键值网关总数： "+objectList.size());
		
		
		String defort = "未获取到信息"; 
		String  id = defort;
		String cag_id = defort;
		String  ip = defort;
		String  port = defort;		
		String  cpu_info =defort; 
		String  mem_info =defort;
		String  gpu_info = defort;
		String  status = defort; 
		String  capacity_size = defort; 
		String version = defort; 
		String thread_id = defort;
		
		if(objectList.size()>0){
			
			Dto outDto = (Dto)objectList.get(0);
			
			   id = outDto.getAsString("ckg_id");
			  cag_id = outDto.getAsString("cag_id");
			   ip = outDto.getAsString("ckg_ip");
			   port = outDto.getAsString("ckg_port");		
			   cpu_info = outDto.getAsString("ckg_cpu_info");
			   mem_info = outDto.getAsString("ckg_mem_info");
			   gpu_info = outDto.getAsString("ckg_gpu_info");
			   status = outDto.getAsString("ckg_status");
			   capacity_size = outDto.getAsString("ckg_capacity_size");
			  version = outDto.getAsString("version");
			  thread_id = outDto.getAsString("thread_id");
		}
		Dto retDto =new BaseDto();
		retDto.put("id",  id);
		retDto.put("cag_id",cag_id );
		retDto.put("ip",  ip);
		retDto.put("port",  port);
		retDto.put("cpu_info", cpu_info );
		retDto.put("mem_info",  mem_info);
		retDto.put("gpu_info", gpu_info );
		
		if(status.equals("1")){
			status = "正常";
		}else{
			status = "非正常";
		}
		retDto.put("status", status );
		retDto.put("capacity_size",  capacity_size+"G");
		retDto.put("version", version);
		
		retDto.put("version", version);
		retDto.put("thread_id", thread_id);
		
		Integer totalCount = objectList.size();
		retDto.put("success", true);
		String retString = JsonHelper.encodeDto2FormLoadJson(retDto,G4Constants.FORMAT_Date); 
		
		log.info("retString="+retString);
		
		write(retString, response);
		return mapping.findForward(null);
	}
	
	
	
	
	
	
	
	
	
	/**
	 *  
	 * 查询单个信令网关服务器信息
	 * 
	 */
	public ActionForward queryCsgGatewayInfoByIpPort(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
				
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		log.info("查询信令网关【"+inDto.getAsString("csg_ip")+":"+inDto.getAsString("csg_port")+"】的信息（queryCsgGatewayConfigList），");
		List objectList = urmReader.queryForList("ServerManager.getCsgGatewayInfoByIpPort", inDto);
		
		log.info("查询出信令网关总数： "+objectList.size());
		
		
		String defort = "未获取到信息"; 
		String id = defort;
		String cag_id = defort;
		String ip = defort;
		String port = defort;		
		String cpu_info =defort; 
		String mem_info =defort;
		String gpu_info = defort;
		String status = defort; 
		String capacity_size = defort; 
		String version = defort; 
		String thread_id = defort;
		
		if(objectList.size()>0){
			
			Dto outDto = (Dto)objectList.get(0);
			
			  id = outDto.getAsString("csg_id");
			  cag_id = outDto.getAsString("cag_id");
			  ip = outDto.getAsString("csg_ip");
			  port = outDto.getAsString("csg_port");		
			  cpu_info = outDto.getAsString("csg_cpu_info");
			  mem_info = outDto.getAsString("csg_mem_info");
			  gpu_info = outDto.getAsString("csg_gpu_info");
			  status = outDto.getAsString("csg_status");
			  capacity_size = outDto.getAsString("csg_capacity_size");
			  version = outDto.getAsString("version");
			  thread_id = outDto.getAsString("thread_id");
		}
	     Dto	retDto = new BaseDto();
		retDto.put("id", id);
		retDto.put("cag_id",cag_id );
		retDto.put("ip", ip);
		retDto.put("port", port);
		retDto.put("cpu_info",cpu_info);
		retDto.put("mem_info", mem_info);
		retDto.put("gpu_info",gpu_info );
		if(status.equals("1")){
			status = "正常";
		}else{
			status = "非正常";
		}
		retDto.put("status",status );
		retDto.put("capacity_size", capacity_size);
		retDto.put("version", version);
		
		retDto.put("version", version);
		retDto.put("thread_id", thread_id);
		
		Integer totalCount = objectList.size();
		retDto.put("success", true);
		String retString = JsonHelper.encodeDto2FormLoadJson(retDto,G4Constants.FORMAT_Date); 
		write(retString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 不可用网关  ----认证处理
	 * 
	 */
	
	
	
	
	
	public ActionForward authGateway(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("认证操作");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String gatewayName =  inDto.getAsString("gatewayName");
		String gatewayIp =  inDto.getAsString("gatewayIp");
		String gatewayPort =  inDto.getAsString("gatewayPort");

		JSONObject obj1 = new JSONObject();

		obj1.put("cmd", "auth");
		obj1.put("appname", gatewayName);
		obj1.put("addr", gatewayIp+":"+gatewayPort);

		
		String str =obj1.toString();
		  
		String sendstr =str+"XXEE";
		
		log.info("sendstr发送的报文："+sendstr);
		TcpClient client = new TcpClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		String returnStr = client.sendStr(watchDogIp, Integer.parseInt(watchDogPort), 5000, null, sendstr,codecFactory);
		System.out.println("接收到的报文："+returnStr);
		
	    String	revStr = returnStr.replace("XXEE", "");
		Dto retDto = new BaseDto();
		if (!(revStr == null || "".equals(revStr))) {
			String JsonStr = JsonHelper.encodeObject2Json(revStr);
			Dto dto = JsonHelper.parseSingleJson2Dto(JsonStr);
			if(dto.getAsInteger("retcode")<0){
				retDto.put("success", false);
			}else{
				retDto.put("success", true);
			}
		}else{
			retDto.put("failure", false);
		
		}
			
		String jsonString = JsonHelper.encodeObject2Json(retDto);
		
		super.write(jsonString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 网关参数详情
	 * 
	 */
	public ActionForward queryGatewayConfig(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		log.info("获取单个网关参数详情方法（queryGatewayConfig）");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String server_id = inDto.getAsString("nodeName");
		String[] server_ids = server_id.split(":");
		inDto.put("field1", server_ids[0]);
		inDto.put("field2", server_ids[1]);
	
		Dto retDto = new BaseDto();
		log.info("server_id="+server_id);
		
		
		List objectList = urmReader.queryForList("ServerManager.queryGatewayConfig", inDto);
		if(objectList.size()>0){
			retDto=(Dto)objectList.get(0);
			retDto.put("success", new Boolean(true));
		}else{
			retDto.put("success", new Boolean(false));
		}
		

		String retString = JsonHelper.encodeDto2FormLoadJson(retDto,G4Constants.FORMAT_Date);
		write(retString, response);
		return mapping.findForward(null);
	}

	/**
	 * 保存网关
	 * 
	 */	
	

	
	public ActionForward saveAccessGatewayConfig(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		log.info("新增网关方法（saveAccessGatewayConfig）");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		Dto outDto = accessGatewayConfigService.savaGatewayConfig(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	
	

	/**
	 * 更新
	 * 
	 */	
	public ActionForward updateAccessGatewayConfig(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("更新网关方法（updateAccessGatewayConfig）");
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		log.info("参数："+inDto.getAsString("id"));
		log.info("参数："+inDto.getAsString("field1"));
		log.info("参数："+inDto.getAsString("field2"));
		log.info("参数："+inDto.getAsString("field3"));
		log.info("参数："+inDto.getAsString("field4"));
		log.info("参数："+inDto.getAsString("field5"));
		
		Dto outDto = accessGatewayConfigService.updateGatewayConfigById(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}

	
	/**
	 * 认证
	 * 
	 */	
	public ActionForward authAccessGatewayConfig(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("认证网关方法（authAccessGatewayConfig）");
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		String server_id = inDto.getAsString("server_ip");
		String[] server_ids = server_id.split(":");
		inDto.put("field1", server_ids[0]);
		inDto.put("field2", server_ids[1]);
		
	
		log.info("参数："+inDto.getAsString("field1"));
		log.info("参数："+inDto.getAsString("field2"));
	
		
		Dto outDto = accessGatewayConfigService.authGatewayConfigById(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
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
		List qamResList = urmReader.queryForPage("CrsmServerMonitor.getAdsListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("CrsmServerMonitor.getAdsListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
		
//		CommonActionForm aForm = (CommonActionForm) form;
//		Dto inDto = aForm.getParamAsDto(request);
//		String retString = serverMonitorService.querySesionDetail(inDto);
//		write(retString, response);
//		return mapping.findForward(null);
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


}
