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
import org.eredlab.g4.urm.service.AdsService;

/**   
 * 类名称：KeyLoadNumConfigAction  
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-12-24 上午10:13:57   
 * 备注：   
 * @version    
 *    
 */
public class KeyLoadNumConfigAction extends BaseAction{
	


	
	private static Log log = LogFactory.getLog(KeyLoadNumConfigAction.class);

	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	
	private AdsService adsService = (AdsService)super.getService("adsService");

	private OrganizationService organizationService = (OrganizationService) super.getService("organizationService");

	
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward pageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		   System.out.println("KeyLoadNumConfigAction-------------------------------"); 
		
//		  log.info("AdsAction 首页初始化");
//		  File[] roots = File.listRoots();//获取磁盘分区列表
//	      for (File file : roots) {
//	    	  log.info(file.getPath()+"信息如下:");
//	    	  log.info("空闲未使用 = " + file.getFreeSpace()/1024/1024/1024+"G");//空闲空间
//	    	  log.info("已经使用 = " + file.getUsableSpace()/1024/1024/1024+"G");//可用空间
//	    	  log.info("总容量 = " + file.getTotalSpace()/1024/1024/1024+"G");//总空间
//	         
//	      }	
//		
//	      DiskSpace d = new DiskSpace();
//       d.main1();
//       
//       log.info("-----------------------------");
//       
//       
//       CpuUsage.getInstance().get();
//       IoUsage.getInstance().get();
//       MemUsage.getInstance().get();
//       
//       NetUsage.getInstance().get();
       
		return mapping.findForward("initView");
	}
	
	/**
	 * 查询网络区域
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryAds(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("KeyLoadNumConfig.getAdsListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("KeyLoadNumConfig.getAdsListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryAdss(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("KeyLoadNumConfig.getAdsLists", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("KeyLoadNumConfig.getAdsListsCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryAdsList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForList("KeyLoadNumConfig.getAdsListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("KeyLoadNumConfig.getAdsListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryResourceByAds(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
	public ActionForward saveAds(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("新增广告操作");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		log.info("查询此广告是否存在");
		Integer totalCount = (Integer) urmReader.queryForObject("KeyLoadNumConfig.getNameByKey", inDto);

		log.info("数据库中存在该广告的条数为："+totalCount);
		inDto.put("sum", totalCount);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto();
		dto.put("account", account);
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo) userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid());
		Dto outDto = adsService.saveAds(inDto);
		
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
	public ActionForward deleteAds(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		Dto outDto = adsService.deleteAds(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
//		setOkTipMsg("网络区域删除成功", response);
		return mapping.findForward(null);
	}	
	
	/**
	 * 修改广告
	 * V1.0.00.14
	 * @param
	 * @return
	 */
	public ActionForward updateAds(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("更新广告操作");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		String id = inDto.getAsString("id");
		log.info("查询此广告是否存在 id ="+id);
		Integer totalCount = (Integer) urmReader.queryForObject("KeyLoadNumConfig.getNameByUpdate", inDto);

		log.info("数据库中存在该广告的条数为："+totalCount);
		inDto.put("sum", totalCount);
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto();
		dto.put("account", account);
		
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo) userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = adsService.updateAds(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		setOkTipMsg("广告修改成功", response);
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
		
		Dto outDto= adsService.saveCacheContor(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	

}
