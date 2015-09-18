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
import org.eredlab.g4.urm.service.QamResourceService;

import prod.nebula.framework.mdb.impl.MdbDataImpl;

public class QamResourceAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(QamResourceAction.class);
	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	
	private QamResourceService qamResourceService = (QamResourceService)super.getService("qamResourceService");
	private OrganizationService organizationService = (OrganizationService) super.getService("organizationService");
//	private String urm_redis_passwd = ph.getValue("urm.redisserver.password");
//	private MdbDataImpl mdb = (MdbDataImpl) super.getService("urmmdbImpl") ;
	
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
	 * 根据QAM编号查询QAM资源数据
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryItemsByQamID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("QamResource.getListWithQamID", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("QamResource.getListWithQamIDCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}

	public ActionForward getQamInfoByName(ActionMapping mapping,ActionForm form,HttpServletRequest request, 
			HttpServletResponse response) throws Exception{
		CommonActionForm cform = (CommonActionForm) form ;
		Dto inDto = cform.getParamAsDto(request);
		List list = urmReader.queryForList("Qam.getQamInfoByName");
		String jsonStr = JsonHelper.encodeList2PageJson(list, list.size(), null) ;
		write(jsonStr,response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryItemsByType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String code = inDto.getAsString("network_code");
		List qamResList ;
		Integer totalCount;
		if(code!= null && !"".equals(code)){
			qamResList= urmReader.queryForPage("QamResource.getListByNetWork", inDto);
			totalCount = (Integer) urmReader.queryForObject("QamResource.getListByNetWorkCount", inDto);
		}else{
			String area = inDto.getAsString("area");
			if("c".equals(area)){
				inDto.put("area", "99999");
			}else if("d".equals(area)){
				inDto.put("area", "33") ;
			}else if("b".equals(area)){
				inDto.put("area", "33");
				inDto.put("sg_id", "11");
			}
			qamResList = urmReader.queryForPage("QamResource.getListWithType"+area, inDto);
			totalCount = (Integer) urmReader.queryForObject("QamResource.getListWithTypeCount"+area, inDto);
		}
			String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryItemsByQamIDs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForList("QamResource.getListWithQamID", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("QamResource.getListWithQamIDCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}

	public ActionForward queryItemByDelQamRes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String str = inDto.getAsString("strChecked");
		String[] strs = str.split(",");
		int sum = 0 ;
		Dto outDto = new BaseDto() ;
		for(int i = 0 ; i < strs.length ; i++){
				Dto dto = new BaseDto() ;
				dto.put("ipqam_res_id", strs[i]) ;
				Integer totalCount = (Integer) urmReader.queryForObject("QamResource.getQamResCount", dto);
				sum += totalCount ;
		}
		if(sum>0)
			outDto.put("success", new Boolean(true));
		else
			outDto.put("success", new Boolean(false)) ;
		String jsonStrList = JsonHelper.encodeObject2Json(outDto) ;
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	public ActionForward queryResourceByRf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String strChecked = request.getParameter("rf_code");
		String[] arrChecked = strChecked.split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			inDto.put("rf_code", arrChecked[i]);
			List qamResList = urmReader.queryForList("QamResource.getIpqamResIdByRf", inDto);
			Integer totalCount = (Integer) urmReader.queryForObject("QamResource.getResourceByRfCount", inDto);
			String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
			write(jsonStrList, response);
		}		
		return mapping.findForward(null);
	}
	
	/**
	 * 查询QAM资源数据
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForList("QamResource.getListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("QamResource.getListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 保存QAM资源数据
	 * 
	 * @param
	 * @return
	 */
	public ActionForward saveQamRes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		String rf_id =	 inDto.getAsString("rf_code");
//		if(rf_id==""){
//			setOkTipMsg("频点信息为空，不能添加，请先选择一个频点", response);
//			System.out.println("频点信息为空，不能添加，请先选择一个频点");
//			return mapping.findForward(null);
//		}
//		Integer totalCount = (Integer) urmReader.queryForObject("QamResource.getCountByIPQAMkey", inDto);
		inDto.put("sum", 0);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account) ;
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo") ;
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid()) ;
		
		
	
		Dto outDto = qamResourceService.saveQamResItem(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 删除QAM资源
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteQamRes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account) ;
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo") ;
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid()) ;
		
		Dto outDto = qamResourceService.deleteQamResItems(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString,response);
		setOkTipMsg("QAM数据删除成功", response);
		return mapping.findForward(null);
	}

	/**
	 * 修改QAM资源
	 * 
	 * @param
	 * @return
	 */
	public ActionForward updateQamRes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
	    Dto inDto = aForm.getParamAsDto(request);
//		Integer totalCount = (Integer) urmReader.queryForObject("QamResource.getCountByIPQAMkeyChange", inDto);
		inDto.put("sum", 0);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account) ;
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo") ;
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid()) ;
		
		Dto outDto = qamResourceService.updateQamResItem(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString,response);
		setOkTipMsg("QAM数据修改成功", response);
		return mapping.findForward(null);
	}
	
	/**
	 * 导入Excel
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward importExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm actionForm = (CommonActionForm) form;
		Dto inDto = actionForm.getParamAsDto(request);
		FormFile theFile = actionForm.getTheFile();
		String metaData = "exp_port,res_type,server_id,pid_id,rf_id,app_name,band_width";
		ExcelReader excelReader = new ExcelReader(metaData, theFile.getInputStream());
		List list = excelReader.read(1);
		String msg = "";
		if(list.size()==0){
			msg = "导入的文件格式不正确或是文件中没有数据！" ;
		}
		System.out.println("返回Action");
		log.info("Excel中有"+list.size()+"条记录");
		log.info("往数据库导入......");
		int ipqam_id = inDto.getAsInteger("ipqam_id");
	
		List dataList = new ArrayList();
 		for(int i=0;i<list.size();i++){
			Dto DateDto = (Dto) list.get(i);
			Integer rf_id = DateDto.getAsInteger("rf_id");
			Dto ppDto = new BaseDto();
			ppDto.put("ipqam_id", ipqam_id);
			ppDto.put("rf_id", rf_id);
			Integer totalCount = (Integer) urmReader.queryForObject("RfResource.getCountByRfId", ppDto);
			List RfCodeList = urmReader.queryForList("RfResource.getRfCodeByRfId", ppDto);
			if(totalCount==0){
				list.remove(i);
			}else{
				Dto  dto = (Dto) RfCodeList.get(0);
				int rf_code = dto.getAsInteger("rf_code");
				DateDto.put("rf_code", rf_code);
				dataList.add(DateDto);
			}
		}
 		
 		List resList = new ArrayList() ;
 		for(int i=0;i<dataList.size();i++){
			Dto dto = (Dto) list.get(i);
			dto.put("ipqam_id", ipqam_id);
			if(dto.getAsInteger("band_width")==null)
				dto.put("band_width", 37500000);
			Integer totalCount = (Integer) urmReader.queryForObject("QamResource.getCountByIPQAMkey", dto);
			if(totalCount==0){
				resList.add(dto);
			}
		}
		inDto.put("list", resList);
		if("".equals(msg)&&resList.size()==0){
			msg = "没有合适的数据可导入！" ;
		}
		System.out.println(inDto);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account) ;
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo") ;
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid()) ;
		
		Dto outDto = qamResourceService.batchResImport(inDto);
		outDto.put("msg", msg);
		log.info("往数据库导入成功!");
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	//根据ipqam_id查询rf_id
	public ActionForward queryRfIdByIpqamId(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto dto = aForm.getParamAsDto(request);
		List rfIdList = urmReader.queryForList("QamResource.getRfIdByIpqamId", dto);
		String jsonString = JsonHelper.encodeList2PageJson(rfIdList, rfIdList.size(), null);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	//根据Code_id查询rf_id
	public ActionForward queryRfIdByCodeId(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto dto = aForm.getParamAsDto(request);
		List rfIdList = urmReader.queryForList("QamResource.getRfIdByCodeId", dto);
		String jsonString = JsonHelper.encodeList2PageJson(rfIdList, rfIdList.size(), null);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryItemByQamRes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String strChecked = inDto.getAsString("strChecked");
		
		Dto outDto = new BaseDto();

		String ip = ph.getValue("urm.redisserver.ip");
		Integer port = Integer.valueOf(ph.getValue("urm.redisserver.port"));

//		try{
//			RedisUseable usable = new RedisUseable() ;
//			if(usable.jedisUseable(ip, port, urm_redis_passwd)){
//				String[] checkli = strChecked.split(",");
//				Boolean flag = FALSE ;
//				for(int i=0;i< checkli.length ;i++){
//					Dto in = new BaseDto();
//					String key = "RES"+checkli[i];
//					String rfPer = (String) mdb.getString(key);
//					if(rfPer!=null && !"0".equals(rfPer)){
//						flag = TRUE ;
//						break ;
//					}
//				}
//				outDto.put("success", flag) ;
//			}else{
//				log.info("【错误】：URM的缓存密码配置错误！");
//				outDto.put("msg", "缓存密码错误，操作失败！") ;
//				outDto.put("success", FALSE) ;	
//			}
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
