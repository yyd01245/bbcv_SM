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
import org.eredlab.g4.common.util.RedisUseable;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.AdsService;
import org.eredlab.g4.urm.service.NavigateService;
import org.eredlab.g4.urm.service.QamService;

import prod.nebula.framework.mdb.impl.MdbDataImpl;

/**   
 * 类名称：AdAction   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-12-1 上午09:38:57   
 * 备注：   广告资源管理Action
 * @version    
 *    
 */
public class NavigateAction extends BaseAction{
	


	
	private static Log log = LogFactory.getLog(NavigateAction.class);

	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	
	private NavigateService navigateService = (NavigateService)super.getService("navigateService");
//	private QamService qamService = (QamService) super.getService("qamService");
	private OrganizationService organizationService = (OrganizationService) super.getService("organizationService");
//	private String urm_ip = ph.getValue("urm.redisserver.ip");
//	private Integer urm_port = Integer.valueOf(ph.getValue("urm.redisserver.port"));
//	private String urm_redis_passwd = ph.getValue("urm.redisserver.password");
//	private MdbDataImpl mdb = (MdbDataImpl) super.getService("urmmdbImpl");
	
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
	 * 查询网络区域
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryNavigate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("Navigate.getNavigateListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Navigate.getNavigateListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryNavigates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("Navigate.getNavigateLists", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Navigate.getNavigateListsCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryNavigateList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForList("Navigate.getNavigateListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Navigate.getNavigateListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryResourceByNavigate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
	public ActionForward saveNavigate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		Dto outDto = navigateService.saveNavigate(inDto);
		
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
	public ActionForward deleteNavigate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		Dto outDto = navigateService.deleteNavigate(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
//		setOkTipMsg("网络区域删除成功", response);
		return mapping.findForward(null);
	}	
	
	/**
	 * 修改网络区域
	 * V1.0.00.14
	 * @param
	 * @return
	 */
	public ActionForward updateNavigate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		Integer totalCount = (Integer) urmReader.queryForObject("Network.getNavigateListByUpdate", inDto);
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
		
		Dto outDto = navigateService.updateNavigate(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		setOkTipMsg("导航修改成功", response);
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
		
		Dto outDto= navigateService.saveCacheContor(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryResByNetwork (ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String key = inDto.getAsString("key");
		Dto outDto = new BaseDto();
		
		/**判断查询时配置文件是否有密码开始*/

//		try{
//			RedisUseable usable = new RedisUseable() ;
//			if(usable.jedisUseable(urm_ip, urm_port, urm_redis_passwd)){
//				
//				String[] checked = key.split(",");
//				Boolean flag = FALSE ;
//				for(String checkKey : checked){
//					List urm_rf = (List) mdb.getObject("URM_RF_"+checkKey);
//					if(urm_rf!=null){
//						for(int i = 0; i < urm_rf.size();i++){
//							String rfKey = "URM_RF_PER_"+checkKey+"_"+urm_rf.get(i);
//							String rfPer = (String) mdb.getString(rfKey);
//							if(rfPer!=null && !"0".equals(rfPer)){
//								flag = TRUE ;
//								break ;
//							}
//						}
//					}
//					if(flag)
//						break;
//				}
//				outDto.put("success", flag) ;
//			}else{
//				log.info("【错误】：URM的缓存密码配置错误！");
//				outDto.put("msg", "缓存密码错误，操作失败！") ;
//				outDto.put("success", FALSE) ;	
//			}
//		
//			String json = JsonHelper.encodeObject2Json(outDto);
//			write(json,response);
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			
//		}
		return mapping.findForward(null);
	}


}
