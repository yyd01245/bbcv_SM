package org.eredlab.g4.cabs.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.cabs.service.ServerMonitorService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;

public class ServerMonitorAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(ServerMonitorAction.class);
	
	private ServerMonitorService serverMonitorService = (ServerMonitorService) super.getService("serverMonitorService");
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward pageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("initView");
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
			List serverList = crsmReader.queryForList("ServerMonitor.getServerMonitorList", groupDto);
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
		List codeList = crsmReader.queryForList("ServerMonitor.getServerMonitorList", inDto);
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
		BaseDto object = (BaseDto)crsmReader.queryForObject("ServerMonitor.getServerByKey", inDto);
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
		List objectList = crsmReader.queryForList("ServerMonitor.getServerStatusList", inDto);
		
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(objectList,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryServerStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List objectList = crsmReader.queryForList("ServerMonitor.getServerStatusDetail", inDto);
		
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(objectList,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryServerStatusDetailList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List objectList = crsmReader.queryForList("ServerMonitor.getServerStatusDetailList", inDto);
		List list = new ArrayList();
		if(objectList.size()>0){
			for(int i=0;i<objectList.size();i++){
				Dto outDto = (Dto)objectList.get(i);
				String version = outDto.getAsString("version");
				String update_version = outDto.getAsString("update_version");
				String status=outDto.getAsString("status");
				if(!((version==update_version||version.equals(update_version))||(update_version==""||"".equals(update_version))||(status=="2"||"2".equals(status)))){
					outDto.put("status", 4);
				}
				list.add(outDto);
			}
		}
		Integer totalCount = objectList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(list,totalCount,null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	
	/**
	 * 启动服务器
	 * 
	 * @param
	 * @return
	 */
	public ActionForward startServer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		//TODO
		Dto outDto =serverMonitorService.startServer(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);		
		write(retString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 升级服务器
	 * 
	 * @param
	 * @return
	 */
	public ActionForward updateServer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		//TODO
		serverMonitorService.updateServer(inDto);
		write("1", response);
		return mapping.findForward(null);
	}
	
	/**
	 * 停止服务器
	 * 
	 * @param
	 * @return
	 */
	public ActionForward stopServer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = serverMonitorService.stopServer(inDto);		
		String retString = JsonHelper.encodeObject2Json(outDto);		
		write(retString, response);
		
		return mapping.findForward(null);
	}
}
