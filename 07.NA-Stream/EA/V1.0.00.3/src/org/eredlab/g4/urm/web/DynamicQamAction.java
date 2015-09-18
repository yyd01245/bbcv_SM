package org.eredlab.g4.urm.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JsonConfig;

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
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.QamDyService;
import org.eredlab.g4.urm.service.impl.QamDyServiceImpl;

import prod.nebula.eabg.util.StringUtil;
import prod.nebula.framework.mdb.impl.MdbDataImpl;

public class DynamicQamAction extends BaseAction {
	
	private static Log log = LogFactory.getLog(DynamicQamAction.class);

	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	private QamDyService qamDyService = (QamDyService)super.getService("qamDyService");
	private OrganizationService organizationService =  (OrganizationService) super.getService("organizationService");
	private String urm_redis_passwd = ph.getValue("urm.redisserver.password");
	private MdbDataImpl mdb = (MdbDataImpl) super.getService("urmmdbImpl") ;
	
	/********************************************S QAM 厂商 S************************************************************/
	/**
	 * QAM管理页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward QAMPageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("QAMPageInit");
	}
	
	/**
	 * 保存QAMDEVICE信息
	 * @param
	 * @return
	 */
	public ActionForward saveQAMDevice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer totalCount = (Integer) urmReader.queryForObject("qamDy.queryInCount", inDto);//信息重复匹配校验
		inDto.put("sum", totalCount);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = qamDyService.saveQAMDevice(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	/**
	 * 修改QAM信息
	 * @param
	 * @return
	 */
	public ActionForward updateQAMDevice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
		
		Integer totalCount = (Integer) urmReader.queryForObject("qamDy.queryUpCount", inDto);//信息重复匹配校验
		inDto.put("sum", totalCount);
		Dto outDto = qamDyService.updateQAMDevice(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryQamDevice(ActionMapping map , ActionForm form ,HttpServletRequest request ,
			HttpServletResponse response)throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List d6r6List = urmReader.queryForPage("qamDy.queryQamDec", inDto);
		Integer d6r6Count = (Integer)urmReader.queryForObject("qamDy.queryQamDecCount", inDto) ;
		String jsonStrList = JsonHelper.encodeList2PageJson(d6r6List,d6r6Count, null);
		write(jsonStrList, response);
		return map.findForward(null);
	}
	
	
	
	/********************************************E QAM 厂商 E************************************************************/

	
	public ActionForward QAMDyPageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("QamDyPageInit");
	}
	
	/*******************************************  第一步   ***************************************************************/
	public ActionForward queryDyQam(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		
		Integer totalCount = (Integer) urmReader.queryForObject("qamDy.getDyQamCount", inDto) ;
		List list = new ArrayList() ;
		if(totalCount>0){
			list = urmReader.queryForPage("qamDy.getDyQam", inDto) ;
			for(Object d : list){
				Dto dto = (Dto) d;
				Integer port = (Integer)urmReader.queryForObject("qamDy.getMinPort",dto);
				Integer port1 = (Integer)urmReader.queryForObject("qamDy.getMinPort1",dto);
				if(port==null)
					port = port1;
				
				if(port1!=null){
					port = (port>port1)? port1 : port ;
				}
				dto.put("ipqam_port", port);
			}
		}
		String result = JsonHelper.encodeList2PageJson(list, totalCount, null) ;
		write(result,response) ;
		log.info("查询到动态上报QAM数量============================>"+ totalCount) ;
		
		return mapping.findForward(null);
	}
	public ActionForward deleteQam(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		Dto outDto = qamDyService.deleteDyQam(inDto) ;
		String result = JsonHelper.encodeObject2Json(outDto) ;
		write(result,response) ;
		log.info("删除动态上报QAM结果============================>"+ result) ;
		return mapping.findForward(null);
	}
	
	/********************************************  第二步   ***************************************************************/
	public ActionForward getRfList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		inDto.put("ipqam_id", inDto.getAsString("checked"));
		List list = urmReader.queryForList("qamDy.getRfList", inDto) ;
		String result = JsonHelper.encodeList2PageJson(list,list.size(),null) ;
		write(result,response) ;
		log.info("该IPQAM下的频点数============================>"+ list.size()) ;
		return mapping.findForward(null);
	}
	public ActionForward getRfDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		String[] rfs = inDto.getAsString("strChecked").trim().split(",");

		Dto dto = new BaseDto() ;
		List ayyList = new ArrayList();
		for(String rf:rfs){
			if("".equals(rf))
				continue ;
			dto.put("rf_code",rf );
			dto.put("status",2) ;
			qamDyService.updateRfStatus(dto);
			inDto.put("rf_code", rf);
			List list = urmReader.queryForList("qamDy.getRfDetail", inDto) ;
			ayyList.addAll(list);
			
		}
		
		String result = JsonHelper.encodeList2PageJson(ayyList,ayyList.size(),null) ;
		write(result,response) ;
		log.info("选中频点数============================>"+ rfs.length) ;
		return mapping.findForward(null);
	}
	
	public ActionForward getQamList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		List list = urmReader.queryForList("qamDy.getQamList",inDto);
		String result = JsonHelper.encodeList2PageJson(list, list.size(), null) ;
		write(result,response) ;
		log.info("设备厂商数目============================>"+ list.size()) ;
		return mapping.findForward(null);
	}
	
	
	public ActionForward getBizList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		List list = urmReader.queryForList("qamDy.getBizList", inDto) ;
		String result = JsonHelper.encodeList2PageJson(list,list.size(),null) ;
		write(result,response) ;
		return mapping.findForward(null);
	}
	
	/**
	 * 频点审核
	 */
	
	public ActionForward rfAudit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		
		Dto outDto = qamDyService.saveMidRf(inDto) ;
		String result = JsonHelper.encodeObject2Json(outDto) ;
		write(result,response) ;
		return mapping.findForward(null);
	}
	
	public ActionForward migrateDate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		Dto outDto =qamDyService.saveAllToNormal(inDto) ;
		String result = JsonHelper.encodeObject2Json(outDto) ;
		write(result,response) ;
		return mapping.findForward(null);
	}
	
	/**************************************     第三步      ****************************************************************/
	public ActionForward getSumData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		List list = urmReader.queryForList("qamDy.getSumRf",inDto);
		String result = JsonHelper.encodeList2PageJson(list, list.size(), null);
		write(result,response) ;
		return mapping.findForward(null);
	}
	public ActionForward getStaticData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		List list = urmReader.queryForList("qamDy.getStaticRf",inDto);
		String result = JsonHelper.encodeList2PageJson(list, list.size(), null);
		write(result,response) ;
		return mapping.findForward(null);
	}
	public ActionForward getDyData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		List list = urmReader.queryForList("qamDy.getDyRf",inDto);
		String result = JsonHelper.encodeList2PageJson(list, list.size(), null);
		write(result,response) ;
		return mapping.findForward(null);
	}
	
	public ActionForward getNetList(ActionMapping mapping,ActionForm form , HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		List list = urmReader.queryForList("qamDy.getNetList");
		String result = JsonHelper.encodeList2PageJson(list, list.size(), null);
		write(result,response) ;
		return mapping.findForward(null);
	}
	
	public ActionForward d6r6PromtInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("d6r6PageInit");
	}
	
	public ActionForward queryD6R6Prompt(ActionMapping mapping,ActionForm form , HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		List list = urmReader.queryForPage("qamDy.selectD6R6Pro",inDto);
		Integer count = (Integer)urmReader.queryForObject("qamDy.selectD6R6ProCount",inDto);
		String result = JsonHelper.encodeList2PageJson(list, count, G4Constants.FORMAT_DateTime);
		write(result,response) ;
		return mapping.findForward(null);
	}
	
	public ActionForward getD6R6Promt(ActionMapping mapping,ActionForm form , HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		List list = urmReader.queryForList("qamDy.selectD6R6Pro",inDto);
		Dto outDto = new BaseDto();
		if(list!=null &&list.size()>0){
			outDto.put("success", TRUE);
		}else{
			outDto.put("success", FALSE);
		}
		String result = JsonHelper.encodeObject2Json(outDto);
		write(result,response) ;
		return mapping.findForward(null);
	}
	
	public ActionForward batchPromot(ActionMapping mapping,ActionForm form , HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request) ;
		Dto outDto = qamDyService.batchPrompt(inDto);
		String result = JsonHelper.encodeObject2Json(outDto);
		write(result,response) ;
		return mapping.findForward(null);
	}
	

	
}
