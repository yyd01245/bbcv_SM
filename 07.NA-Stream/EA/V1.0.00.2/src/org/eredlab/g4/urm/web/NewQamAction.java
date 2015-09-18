package org.eredlab.g4.urm.web;

import java.io.IOException;
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
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.NewQamService;


public class NewQamAction extends BaseAction{
	
	private static Log logger = LogFactory.getLog(NewQamAction.class) ;
	
	private NewQamService service = (NewQamService) super.getService("newQamService") ;
	private OrganizationService organizationService = (OrganizationService) super.getService("organizationService");
	
	/**
	 * 页面初始化
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward pageInit(ActionMapping map , ActionForm form ,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		return map.findForward("initView") ;
	}
	
	public ActionForward pageInit1(ActionMapping map , ActionForm form ,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		return map.findForward("initView1") ;
	}
	
	
	public ActionForward queryItemsByQamIp(ActionMapping map ,ActionForm form ,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		CommonActionForm com = (CommonActionForm) form ;
		Dto inDto = com.getParamAsDto(request) ;
		List queryList = urmReader.queryForPage("NewQam.getQamAreaByIp", inDto) ;
		Integer totalCount = (Integer)urmReader.queryForObject("NewQam.getQamAreaByIpCount",inDto) ;
		String jsonString = JsonHelper.encodeList2PageJson(queryList, totalCount, null) ;
		write(jsonString, response) ;
		return map.findForward(null);
	}
	
	/**
	 * 批量数据同步
	 * @param map
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward batchQAMSychronous(ActionMapping map , ActionForm form , HttpServletRequest request ,
			HttpServletResponse response) throws Exception{
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		if(strChecked == null || "".equals(strChecked)){
//			setOkTipMsg("ipqam资源编号为空，不能添加，请先选择一个ipqam资源", response);
//			System.out.println("ipqam资源编号为空，不能添加，请先选择一个ipqam资源");
//			return map.findForward(null);
//		}
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto();
		inDto.put("account", account);
		dto.put("account", account);
		
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo userInfo = (UserInfoVo) userDto.get("userInfo");
		inDto.put("oper_id", userInfo.getUserid());
		inDto.put("dept_id", userInfo.getDeptid());
		service.saveEvent(inDto);
		setOkTipMsg("IPQAM同步成功", response);
		return map.findForward(null) ;
	}
	
	/**
	 * urm的操作处理查询
	 */
	public ActionForward queryKeyHandler(ActionMapping map,ActionForm form ,HttpServletRequest request,
			HttpServletResponse response ) throws Exception{
		CommonActionForm com = (CommonActionForm)form ;
		Dto inDto = com.getParamAsDto(request);
		List outList = urmReader.queryForPage("NewQam.getMdbData", inDto) ;
		Integer total = (Integer)urmReader.queryForObject("NewQam.getMdbDataCount", inDto) ;
		logger.info("urm操作处理查询查到数据："+total);
		String result = JsonHelper.encodeList2PageJson(outList, total, G4Constants.FORMAT_DateTime);
		write(result,response);
		return map.findForward(null);
	}
}
