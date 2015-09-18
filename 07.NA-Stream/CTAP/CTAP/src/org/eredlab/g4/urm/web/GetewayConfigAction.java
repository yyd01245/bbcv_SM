package org.eredlab.g4.urm.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.AccessGatewayConfigService;

public class GetewayConfigAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(GetewayConfigAction.class);
	
	
	private AccessGatewayConfigService accessGatewayConfigService = (AccessGatewayConfigService)super.getService("accessGatewayConfigService");
	private DataSynchronizateAction data = new DataSynchronizateAction();
	
	/**
	 * 页面初始化
	 */

	public ActionForward toManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("---------------同步配置文件");
		List list = new ArrayList();
		/*************CAG列表查询***************/
		List cagList = urmReader.queryForList("DateSyn.getCagList");
		if(cagList!=null){
			for (Object object : cagList) {
				Dto dto = (Dto)object;
				dto.put("app_name", "cag");
				list.add(dto);
			}
		}
		/*************CKG列表查询***************/
		List ckgList = urmReader.queryForList("DateSyn.getCkgList");
		if(cagList!=null){
			for (Object object : ckgList) {
				Dto dto = (Dto)object;
				dto.put("app_name", "ckg");
				list.add(dto);
			}
		}
		/*************CSG列表查询***************/
		List csgList = urmReader.queryForList("DateSyn.getCsgList");
		if(cagList!=null){
			for (Object object : csgList) {
				Dto dto = (Dto)object;
				dto.put("app_name", "csg");
				list.add(dto);
			}
		}
		if(list!=null){
			for (Object object : list) {
				Dto dto = (Dto)object;
				request.setAttribute("ip", dto.getAsString("ip"));
				request.setAttribute("port", dto.getAsString("port"));
				request.setAttribute("app_name", dto.getAsString("app_name"));
				data.queryConfigByIPAndUpdateDB(mapping, form, request, response);
			}
		}
		log.info("---------------网关配置首页初始化方法（toManage）");
		return mapping.findForward("toManage");
	}
	

	/**
	 *  
	 * 查询接入网关列表
	 * 
	 */
	public ActionForward queryCagGatewayInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			
		log.info("查询接入列表方法（queryGatewayConfigList）");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		
		List objectList = urmReader.queryForList("GatewayConfig.getCagGatewayConfigList", inDto);
		
		System.out.println("接入网关objectList.size "+objectList.size());
//		List list = new ArrayList();
//		if(objectList.size()>0){
//			for(int i=0;i<objectList.size();i++){
//				Dto outDto = (Dto)objectList.get(i);
//				String server_ip = outDto.getAsString("cag_ip");
//				String server_port = outDto.getAsString("cag_port");
//				String cag_id = outDto.getAsString("cag_id");
//			//	String server_id=server_ip+":"+server_port;
//				outDto.put("server_ip", server_ip);
//				outDto.put("server_port", server_port);
//				outDto.put("cag_id", cag_id);
//				list.add(outDto);
//			}
//		}
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(objectList, totalCount, null);
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
			
		log.info("查询键值网关列表方法（queryCkgGatewayConfigList）");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		System.out.println("cag_id="+inDto.getAsString("cag_id"));
		List objectList = urmReader.queryForList("GatewayConfig.getCkgGatewayConfigList", inDto);
		
		System.out.println("键值网关objectList.size "+objectList.size());
		List list = new ArrayList();
//		if(objectList.size()>0){
//			for(int i=0;i<objectList.size();i++){
//				Dto outDto = (Dto)objectList.get(i);
//				String server_ip = outDto.getAsString("ckg_ip");
//				String server_port = outDto.getAsString("ckg_port");
//				String cag_id = outDto.getAsString("cag_id");
//			//	String server_id=server_ip+":"+server_port;
//				outDto.put("server_ip", server_ip);
//				outDto.put("ckg_id", cag_id);
//				outDto.put("server_port", server_port);
//				list.add(outDto);
//			}
//		}
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(objectList, totalCount, null);
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
			
		log.info("查询信令网关列表方法（queryCsgGatewayConfigList）");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		System.out.println("cag_id="+inDto.getAsString("cag_id"));
		List objectList = urmReader.queryForList("GatewayConfig.getCsgGatewayConfigList", inDto);
		
		System.out.println("信令网关objectList.size "+objectList.size());
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
//		
		
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(objectList, totalCount, null);
		write(jsonStrList, response);
	
		
//		Integer totalCount = objectList.size();
//		String jsonStrList = JsonHelper.encodeList2PageJson(list,totalCount,null);
//		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	
	/**
	 *  
	 * 查询网关配置列表
	 * 
	 */
	public ActionForward queryGatewayConfigFileList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			
		log.info("查询网关配置列表（queryGatewayConfigList）");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		
		log.info("查找对应配置文件的参数：config_ip="+inDto.getAsString("config_ip")+" config_port="+inDto.getAsString("config_port")+" config_name="+inDto.getAsString("config_name"));
	
		List objectList = urmReader.queryForList("GatewayConfig.getGatewayConfigFileList", inDto);
		
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
		
		
		List objectList = urmReader.queryForList("GatewayConfig.queryGatewayConfig", inDto);
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
