/**  
 * 类名称：AdAction 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-12-1 上午09:38:57 
 */
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
import org.eredlab.g4.arm.service.OrganizationService;
import org.eredlab.g4.arm.vo.UserInfoVo;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.StreamResourceService;

/**   
 * 类名称：AdAction   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-12-1 上午09:38:57   
 * 备注：   流资源管理Action
 * @version    
 *    
 */
public class StreamResourceAction extends BaseAction{
	


	
	private static Log log = LogFactory.getLog(StreamResourceAction.class);

	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	
	private StreamResourceService streamResourceService = (StreamResourceService)super.getService("streamResourceService");
	
	private OrganizationService organizationService = (OrganizationService) super.getService("organizationService");

	
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
	
	public ActionForward queryAreas(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		//ipqamInfoId
		
		System.out.println("ipqamInfoId---------------------------->"+inDto.getAsString("ipqamInfoId"));
		inDto.put("ipqamInfoId", inDto.getAsString("ipqamInfoId"));
//		dto.put("areacode", areacode);
//		dto.put("length", areacode.length() + 2);
		List list = null;
//		if (areacode.equals("00")) {
//			list = g4Reader.queryForList("StreamResource.queryAreas4Tree4FirstLevel", dto);
//		}else {
			list = urmReader.queryForList("StreamResource.getQamInfo", inDto);
//		}
		for (int i = 0; i < list.size(); i++) {
			Dto node = (BaseDto)list.get(i);
		
				node.put("leaf", new Boolean(true));
			
		}
		System.out.println(list);
		String jsonString = JsonHelper.encodeObject2Json(list);
		super.write(jsonString, response);
		return mapping.findForward(null);
	}
	
	
	
	
	
	/**
	 * 查询网络区域
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryStreamResource(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("StreamResource.getStreamResourceListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("StreamResource.getStreamResourceListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryStreamResources(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("StreamResource.getStreamResourceLists", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("StreamResource.getStreamResourceListsCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryStreamResourceList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForList("StreamResource.getStreamResourceListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("StreamResource.getStreamResourceListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryResourceByStreamResource(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		String strChecked = request.getParameter("strChecked");
		String[] strCheckeds = strChecked.split(",");
		List list = new ArrayList();
		int sum=0;
		for(int i=0;i<strCheckeds.length;i++){
			Dto inDto = aForm.getParamAsDto(request);
			inDto.put("network_code", strCheckeds[i]);
			List qamResList = urmReader.queryForList("Network.queryResourceByNetwork", inDto);
			Integer totalCount = (Integer) urmReader.queryForObject("Network.queryResourceByNetworkCount", inDto);
			List rfResList = urmReader.queryForList("Network.queryRfResourceByNetwork", inDto);
			Integer total = (Integer) urmReader.queryForObject("Network.queryRfResourceByNetworkCount", inDto);
			list.addAll(qamResList);
			list.addAll(rfResList);
			sum+=totalCount;
			sum+=total;
		}
		
		String jsonStrList = JsonHelper.encodeList2PageJson(list, sum, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/*
	 * V1.0.00.14
	 */
	public ActionForward saveStreamResource(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		Integer totalCount = (Integer) urmReader.queryForObject("Network.getNetworkListByKey", inDto);
//		Integer totalCount1 = (Integer) urmReader.queryForObject("Network.getNameByKey", inDto);
//		totalCount+=totalCount1;
		inDto.put("sum", 0);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto();
		dto.put("account", account);
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo) userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid());
		Dto outDto = streamResourceService.saveStreamResource(inDto);
		
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
		
	/**
	 * 删除网络区域
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteStreamResource(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto();
		dto.put("account", account);
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo) userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid());
		Dto outDto = streamResourceService.deleteStreamResource(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
//		setOkTipMsg("网络区域删除成功", response);
		return mapping.findForward(null);
	}	
	
	/**
	 * 修改
	 * V1.0.00.14
	 * @param
	 * @return
	 */
	public ActionForward updateStreamResource(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		Integer totalCount = (Integer) urmReader.queryForObject("Network.getNetworkListByUpdate", inDto);
//		Integer totalCount1 = (Integer) urmReader.queryForObject("Network.getNameByUpdate", inDto);
//		totalCount+=totalCount1;
		inDto.put("sum", 0);
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto();
		dto.put("account", account);
		
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo) userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = streamResourceService.updateStreamResource(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		setOkTipMsg("修改成功", response);
		return mapping.findForward(null);
	}
	
	
	/**
	 * 
	 * 
	 * 获取所有区域的area_id和region_id组合
	 */
	public ActionForward queryURMkey(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForList("Network.getAreaRegion", inDto);
		List list = new ArrayList();
		if(qamResList.size()>0){
			for(int i=0;i<qamResList.size();i++){
				Dto outDto = (Dto)qamResList.get(i);
				String area_id = outDto.getAsString("area_id");
				String sg_id = outDto.getAsString("sg_id");
				String area_sg = area_id+"_"+sg_id;
				Dto dto = new BaseDto();
				dto.put("area_sg", area_sg);
				list.add(dto);
			}
		}
		String jsonStrList = JsonHelper.encodeList2PageJson(list, list.size(), null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/***
	 * 查看是否有缓存事件已添加
	 * 
	 */
	public ActionForward querySynEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto outDto = new BaseDto();
		outDto.put("oper_status", 98);
		Integer count = (Integer)urmReader.queryForObject("Network.queryCountSynEvent",outDto);
		if(count>0){
			outDto.put("success", TRUE);
		}else{
			outDto.put("success", FALSE);
		}
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 
	 * 新增阈值全量同步
	 */
	public ActionForward saveCacheContor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		String account = request.getParameter("account");
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto();
		inDto.put("account", account);
		dto.put("account", account);
		
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo userInfo = (UserInfoVo) userDto.get("userInfo");
		inDto.put("oper_id", userInfo.getUserid());
		inDto.put("dept_id", userInfo.getDeptid());
		inDto.put("operpassword", userInfo.getOperpassword());
		
		Dto outDto= streamResourceService.saveCacheContor(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	

}
