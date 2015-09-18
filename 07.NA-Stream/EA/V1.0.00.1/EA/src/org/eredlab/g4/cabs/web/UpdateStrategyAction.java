package org.eredlab.g4.cabs.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.cabs.service.UpdateStrategyService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;

public class UpdateStrategyAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(UpdateStrategyAction.class);
	
	private UpdateStrategyService updateStrategyService = (UpdateStrategyService) super.getService("updateStrategyService");
	
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward updateStrategyInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("initView");
	}
	/**
	 * 分页查询
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryStrategyItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForPage("UpdateStrategy.getUpdateStrategyListForPage", inDto);
		Integer totalCount = (Integer) crsmReader.queryForObject("UpdateStrategy.getUpdateStrategyListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询所有
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryStrategyAll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForList("UpdateStrategy.getUpdateStrategyListForPage", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, new Integer(codeList.size()), null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 通过组名获取相应的升级包编号
	 * 
	 */
	public ActionForward getRecordIdByName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForList("UpdateStrategy.getJarRecordIdByGroupId", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, new Integer(codeList.size()), null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 保存应用组编号和对应升级包编号关系
	 * 
	 * @param
	 * @return
	 */
	public ActionForward addStrategyItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		Integer count = (Integer) crsmReader.queryForObject("UpdateStrategy.queryPackageGroupRel", inDto);
//		inDto.put("count", count);
		Dto outDto = updateStrategyService.insertStrategyItem(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 删除策略
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteStrategyItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		Dto outDto = updateStrategyService.deleteStrategyItems(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	

}
