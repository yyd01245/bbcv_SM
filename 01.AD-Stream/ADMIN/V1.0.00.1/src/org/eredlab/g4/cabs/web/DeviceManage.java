package org.eredlab.g4.cabs.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.cabs.service.DevManagerService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;

public class DeviceManage  extends BaseAction{
	private static Log log = LogFactory.getLog(DeviceManage.class);	
	private DevManagerService devManagerService = (DevManagerService) super.getService("devManagerService");

	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward devManagerInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("initView");
	}
	
	/**
	 * 查询设备
	 * 
	 * @param
	 * @return
	 */
	public ActionForward querydevManagerItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForList("DeviceManager.getDevManagerListForPage", inDto);
		Integer totalCount = (Integer) crsmReader.queryForObject("DeviceManager.getDevManagerListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	/**
	 * 新增设备
	 * 
	 * @param
	 * @return
	 */
	public ActionForward saveDevManagerItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer count = (Integer)crsmReader.queryForObject("DeviceManager.IsNamed", inDto);//重名校验
		inDto.put("count", count);
		Dto outDto = devManagerService.saveDevManagerItem(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 修改设备
	 * 
	 * @param
	 * @return
	 */
	public ActionForward updateDevManagerItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer count = (Integer)crsmReader.queryForObject("DeviceManager.IsUpdateNamed", inDto);
		inDto.put("count", count);
		Dto outDto =devManagerService.updateDevManagerItem(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 删除设备
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteDevManagerItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		Dto outDto = devManagerService.deleteDevManagerItems(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
}
