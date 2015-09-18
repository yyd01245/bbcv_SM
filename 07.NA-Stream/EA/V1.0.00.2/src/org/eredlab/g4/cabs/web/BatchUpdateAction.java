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
import org.eredlab.g4.cabs.service.BatchUpdateService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;

public class BatchUpdateAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(AppGroupManagerAction.class);
	
	private BatchUpdateService batchUpdateService = (BatchUpdateService) super.getService("batchUpdateService");
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
	 * 条件查询主机信息
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryServerWithCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String status = inDto.getAsString("status");
		List list = new ArrayList();
		Integer totalCount;
		if(status=="4"||"4".equals(status)){
			inDto.put("status", 1);
			List serverList = crsmReader.queryForPage("BatchUpdate.getBatchUpdateListForPage", inDto);
			if(serverList.size()>0){
				for(int i=0;i<serverList.size();i++){
					Dto vncmDto = (Dto) serverList.get(i);
					String version = vncmDto.getAsString("version");
					String update_version = vncmDto.getAsString("update_version");
					if(!((version==update_version||version.equals(update_version))||(update_version==""||"".equals(update_version)))){
						vncmDto.put("status", 4);
						list.add(vncmDto);
					}
				}
			}
//			totalCount = (Integer) crsmReader.queryForObject("BatchUpdate.getBatchServerForPageCount", inDto);
		}else if(status=="1"||"1".equals(status)){
			List serverList = crsmReader.queryForPage("BatchUpdate.getBatchUpdateListForPage", inDto);
			if(serverList.size()>0){
				for(int i=0;i<serverList.size();i++){
					Dto vncmDto = (Dto) serverList.get(i);
					String version = vncmDto.getAsString("version");
					String update_version = vncmDto.getAsString("update_version");
					if((version==update_version||version.equals(update_version))||(update_version==""||"".equals(update_version))){
						vncmDto.put("status", 1);
						list.add(vncmDto);
					}
				}
			}
		}else if(status=="2"||"2".equals(status)){
			List serverList = crsmReader.queryForPage("BatchUpdate.getBatchUpdateListForPage", inDto);
			list=serverList;
		}else{
			List serverList = crsmReader.queryForPage("BatchUpdate.getBatchUpdateListForPage", inDto);
			if(serverList.size()>0){
				for(int i=0;i<serverList.size();i++){
					Dto vncmDto = (Dto) serverList.get(i);
					String version = vncmDto.getAsString("version");
					String update_version = vncmDto.getAsString("update_version");
					String vstatus=vncmDto.getAsString("status");
					if(!((version==update_version||version.equals(update_version))||(update_version==""||"".equals(update_version))||(vstatus=="2"||"2".equals(vstatus)))){
						vncmDto.put("status", 4);
						list.add(vncmDto);
					}else if((((version==update_version||version.equals(update_version))||(update_version==""||"".equals(update_version)))&&(vstatus=="1"||"1".equals(vstatus)))){
						vncmDto.put("status", 1);
						list.add(vncmDto);
					}else{
						list.add(vncmDto);
					}
				}
			}
		}
		//查询所有主机
//		if(!inDto.getAsString("status").equals("6")){
//			List serverList = crsmReader.queryForPage("BatchUpdate.getBatchUpdateListForPage", inDto);
//			Integer totalCount = (Integer) crsmReader.queryForObject("BatchUpdate.getBatchServerForPageCount", inDto);
//			String jsonStrList = JsonHelper.encodeList2PageJson(serverList, totalCount, null);
//			write(jsonStrList, response);
//		
//			//升级主机
//		}else if(inDto.getAsString("status").equals("6")){
//			List codeList = crsmReader.queryForPage("BatchUpdate.getBatchUpdateValidList", inDto);
//			Integer totalCount = (Integer) crsmReader.queryForObject("BatchUpdate.getBatchUpdateValidPageCount", inDto);
//			Integer versionTotal = (Integer) crsmReader.queryForObject("BatchUpdate.getVersionPageCount", inDto);
//			totalCount = totalCount/versionTotal;
//			String jsonStrList = JsonHelper.encodeList2PageJson(codeList, totalCount, null);
//			write(jsonStrList, response);
//		}
		
		String jsonStrList = JsonHelper.encodeList2PageJson(list, list.size(), null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	public ActionForward queryServerStauts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		//查询所有主机
		
//		Dto serverDto = (Dto) crsmReader.queryForObject("BatchUpdate.getBatchUpdateDStatusForPage", inDto);
//		String jsonString = null;
//		if(!serverDto.isEmpty()){
//			jsonString = serverDto.getAsString("status");
//		}
		String jsonString = "1";
		List serverList = crsmReader.queryForList("BatchUpdate.getBatchUpdateDStatusForPage", inDto);
		if(serverList.size()>0){
			Dto serverDto = (Dto) serverList.get(0);
			jsonString = serverDto.getAsString("status");
		}
		write(jsonString, response);
		return mapping.findForward(null);
	}
	

	/**
	 * 批量升级
	 * 
	 * @param
	 * @return
	 */
	public ActionForward batchUpdateItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		batchUpdateService.batchUpdateItems(inDto);
		setOkTipMsg("批量升级成功", response);
		return mapping.findForward(null);
	}
	
	/**
	 * 批量回滚
	 * 
	 * @param
	 * @return
	 */
	public ActionForward batchRollBlockItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		batchUpdateService.batchRollBlockItems(inDto);
		setOkTipMsg("批量升级成功", response);
		return mapping.findForward(null);
	}
	/**
	 * 批量启动
	 * 
	 * @param
	 * @return
	 */
	public ActionForward batchStartItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		Dto outDto = batchUpdateService.batchStartItems(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
//		setOkTipMsg("批量下线成功", response);
		return mapping.findForward(null);
	}
	/**
	 * 批量停止
	 * 
	 * @param
	 * @return
	 */
	public ActionForward batchStopItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		Dto outDto = batchUpdateService.batchStopItems(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
//		setOkTipMsg("批量下线成功", response);
		return mapping.findForward(null);
	}
	
	/**
	 * 批量下线
	 * 
	 * @param
	 * @return
	 */
	public ActionForward batchOfflineItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		batchUpdateService.batchOfflineItems(inDto);
		setOkTipMsg("批量下线成功", response);
		return mapping.findForward(null);
	}
	
	/**
	 * 修改机柜位置
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward updateLocation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		System.out.println("==================机柜位置："+inDto.getAsString("term_desc")+"=====================");
		batchUpdateService.updateLocation(inDto);
		setOkTipMsg("修改位置成功", response);
		return mapping.findForward(null);
	}
	
	/**
	 * 保存表格脏数据
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward saveDirtyDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto outDto = new BaseDto();
		List list  = aForm.getGridDirtyData(request);
		for (int i = 0; i < list.size(); i++) {
			Dto dto = (BaseDto)list.get(i);
			System.out.println("脏数据:\n" + dto);
			//todo anything what u want
			outDto = batchUpdateService.updateLocation(dto);
		}
		
//		outDto.put("success", new Boolean(true));
//		outDto.put("msg", "数据已提交到后台,但演示程序没有将其持久化到数据库.<br>" + request.getParameter("dirtydata"));
		String retString =JsonHelper.encodeObject2Json(outDto);
		System.out.println(retString);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 获取设备队列
	 * 
	 */
	public ActionForward queryDeviceList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List deviceList = crsmReader.queryForList("UpdateStrategy.queryDeviceList", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(deviceList, new Integer(deviceList.size()), null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	/**
	 * 获取组队列
	 * 
	 */
	public ActionForward queryGroupList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List groupList = crsmReader.queryForList("UpdateStrategy.queryGroupList", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(groupList, new Integer(groupList.size()), null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
}

