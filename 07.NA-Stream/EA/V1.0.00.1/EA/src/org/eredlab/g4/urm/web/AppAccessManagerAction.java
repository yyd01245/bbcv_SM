package org.eredlab.g4.urm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.arm.service.OrganizationService;
import org.eredlab.g4.arm.vo.UserInfoVo;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.AppAccessService;
import org.eredlab.g4.urm.service.QamService;

public class AppAccessManagerAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(AppAccessManagerAction.class);
	
	private AppAccessService appAccessService = (AppAccessService) super.getService("appAccessService");
	private OrganizationService organizationService =  (OrganizationService) super.getService("organizationService");
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
	//跳转到缓存加载控制页面
	public ActionForward control(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("control");
	}
	/**
	 * 查询代码表
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryAppAccessItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List appAccessList = urmReader.queryForPage("AppAccess.getAppAccessListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("AppAccess.getAppAccessListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(appAccessList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	/**
	 * 保存
	 * 
	 * @param
	 * @return
	 */
	public ActionForward saveAppAccessItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer totalCount = (Integer) urmReader.queryForObject("AppAccess.getCountByKey", inDto);
		inDto.put("sum", totalCount);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = appAccessService.saveAppAccessItem(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}

	/**
	 * 删除用户
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteAppAccessItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
		
		appAccessService.deleteAppAccessItems(inDto);
		setOkTipMsg("外部应用接入数据删除成功", response);
		return mapping.findForward(null);
	}

	/**
	 * 修改用户
	 * 
	 * @param
	 * @return
	 */
	public ActionForward updateAppAccessItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer totalCount = (Integer) urmReader.queryForObject("AppAccess.getCountByKey", inDto);
		inDto.put("sum", totalCount);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = appAccessService.updateAppAccessItem(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		setOkTipMsg("外部应用接入数据数据修改成功", response);
		return mapping.findForward(null);
	}

}
