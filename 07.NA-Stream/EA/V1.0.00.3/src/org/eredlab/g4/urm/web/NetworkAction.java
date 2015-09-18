package org.eredlab.g4.urm.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.eredlab.g4.arm.service.OrganizationService;
import org.eredlab.g4.arm.vo.UserInfoVo;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.common.util.RedisUseable;
import org.eredlab.g4.rif.report.excel.ExcelReader;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.NetworkService;
import org.eredlab.g4.urm.service.QamService;
import org.eredlab.g4.urm.service.RfService;

import prod.nebula.framework.mdb.impl.MdbDataImpl;

import redis.clients.jedis.JedisPool;

public class NetworkAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(NetworkAction.class);

	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	
	private NetworkService networkService = (NetworkService)super.getService("networkService");

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
	
	/**
	 * 查询网络区域
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryNetwork(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("Network.getNetworkListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Network.getNetworkListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryNetworks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("Network.getNetworkLists", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Network.getNetworkListsCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryNetworkList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForList("Network.getNetworkListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Network.getNetworkListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryResourceByNetwork(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
	public ActionForward saveNetworks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		Dto outDto = networkService.saveNetworks(inDto);
		
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
	public ActionForward deleteNetwork(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		Dto outDto = networkService.deleteNetwork(inDto);
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
	public ActionForward updateNetworks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		Integer totalCount = (Integer) urmReader.queryForObject("Network.getNetworkListByUpdate", inDto);
		List list = (List)urmReader.queryForList("Network.getItemInfoByNetID", inDto);//获取当前网络区域在数据库中的信息
		Integer hnavCount = (Integer) urmReader.queryForObject("Navigate.gethNavCountByType", inDto);
		Integer snavCount = (Integer) urmReader.queryForObject("Navigate.getsNavCountByType", inDto);
		Dto netDto = (Dto) list.get(0);
		int oldhnavnum = netDto.getAsInteger("inavgationstreamnum");
		int oldsnavnum = netDto.getAsInteger("sdinavgationstreamnum");
		int oldhadsnum = netDto.getAsInteger("iadvertisementstreamnum");
		int oldsadsnum = netDto.getAsInteger("sdiadvertisementstreamnum");
		boolean changeflag = false;
//		Integer totalCount = (Integer) urmReader.queryForObject("Network.getCountByName", inDto);
		Integer hstreamNum = (Integer) urmReader.queryForObject("Network.getHStreamCountByNetID", inDto);
		Integer sstreamNum = (Integer) urmReader.queryForObject("Network.getSStreamCountByNetID", inDto);
		int hnavNum = inDto.getAsInteger("inavgationstreamnum");
		int snavNum = inDto.getAsInteger("sdinavgationstreamnum");
		int hadsNum = inDto.getAsInteger("iadvertisementstreamnum");
		int sadsNum = inDto.getAsInteger("sdiadvertisementstreamnum");
		if(oldhadsnum!=hadsNum||oldhnavnum!=hnavNum||oldsadsnum!=sadsNum||oldsnavnum!=snavNum){
			//比对是否修改了导航流数目
			changeflag=true;
		}
		inDto.put("flag", true);
		
		if((hnavNum+hadsNum)>hstreamNum||(snavNum+sadsNum)>sstreamNum){
			//比对初始化数目是否合理
			inDto.put("flag", false);
		}
		
		if((hnavNum>0&&hnavCount==0)||(snavNum>0&&snavCount==0)){
			//导航个数是否合理
			inDto.put("flag", false);
		}
		
		inDto.put("changeflag", changeflag);
		Dto outDto = networkService.updateNetworks(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		setOkTipMsg("网络区域修改成功", response);
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
		
		Dto outDto= networkService.saveCacheContor(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	
}
