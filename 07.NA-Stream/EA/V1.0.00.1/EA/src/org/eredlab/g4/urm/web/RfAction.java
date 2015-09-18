package org.eredlab.g4.urm.web;

import java.util.ArrayList;
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
import org.eredlab.g4.urm.service.RfService;
import org.springframework.context.ApplicationContext;

import prod.nebula.framework.mdb.impl.MdbDataImpl;

import redis.clients.jedis.JedisPool;


public class RfAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(RfAction.class);
	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);

	private RfService rfService = (RfService)super.getService("rfService");
	private OrganizationService organizationService = (OrganizationService) super.getService("organizationService");
	private String urm_redis_passwd = ph.getValue("urm.redisserver.password");
	private MdbDataImpl mdb = (MdbDataImpl) super.getService("urmmdbImpl");
	
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
	 * 根据QAM编号查询频点资源数据
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryrfItemsByQamID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("RfResource.getListWithQamID", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("RfResource.getListWithQamIDCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryRfByIPqam(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String strChecked = request.getParameter("ipqam_id");
		String[] arrChecked = strChecked.split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			inDto.put("ipqam_id", arrChecked[i]);
			List qamResList = urmReader.queryForList("RfResource.getRfIDByIPqam", inDto);
			Integer totalCount = (Integer) urmReader.queryForObject("RfResource.getListWithQamIDCount", inDto);
			String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
			write(jsonStrList, response);
		}	
		return mapping.findForward(null);
	}
	/**
	 * 查询频点资源数据
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForList("rfResource.getListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("rfResource.getListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryItemsByNetworkCode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("RfResource.queryItemsByNetworkCode", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("RfResource.queryItemsByNetworkCodeCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryItemsByType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String area = inDto.getAsString("area");
		if("c".equals(area)){
			inDto.put("area", "99999");
		}else if("d".equals(area)){
			inDto.put("area", "33") ;
		}else if("b".equals(area)){
			inDto.put("area", "33");
			inDto.put("sg_id", "11");
		}
		List qamResList = urmReader.queryForPage("RfResource.queryItemsByType"+area, inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("RfResource.queryItemsByTypeCount"+area, inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	/**
	 * 保存频点资源数据
	 * 
	 * @param
	 * @return
	 */
	public ActionForward saveRf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);

		Integer totalCount = (Integer) urmReader.queryForObject("RfResource.getListByRfID", inDto);
		inDto.put("sum", totalCount);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account",account) ;
		
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid());
		Dto outDto = rfService.saveRfItem(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 删除频点资源
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteRf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		List list = new ArrayList();
		String[] arrChecked = inDto.getAsString("strChecked").split(",");

		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account",account) ;
		
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid());
		Dto outDto = rfService.deleteRfItems(inDto);
		write(JsonHelper.encodeObject2Json(outDto),response);
		setOkTipMsg("频点数据删除成功", response);
		return mapping.findForward(null);
	}
	public ActionForward deleterfManagerByIPqam(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		
		rfService.deleteQamItemsByrf_id(inDto);
		setOkTipMsg("频点数据删除成功", response);
		return mapping.findForward(null);
	}
	/**
	 * 修改频点资源
	 * 
	 * @param
	 * @return
	 */
	public ActionForward updateRf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);

		Integer totalCount = (Integer) urmReader.queryForObject("RfResource.getListByRfIDs", inDto);
		inDto.put("sum", totalCount);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account",account) ;
		
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid());
		Dto outDto = rfService.updateRfItem(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		setOkTipMsg("频点数据修改成功", response);
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
		String metaData = "rf_id,order_port,rf_bandwidth,switch_type";
		ExcelReader excelReader = new ExcelReader(metaData, theFile.getInputStream());
		List list = excelReader.read(1);
		String msg = "";
		if(list.size()==0){
			msg = "导入的文件格式不正确或是文件中没有数据！" ;
		}
		String network_code = inDto.getAsString("network_code");
	
		log.info("返回Action");
		log.info("Excel中有"+list.size()+"条记录");
//		for(int i=0;i<list.size();i++){
//			System.out.println(list.get(i));
//		}
		log.info("往数据库导入......");
		List rfList = new ArrayList();
		for(int i=0;i<list.size();i++){
			Dto dto = (Dto) list.get(i);
			dto.put("network_code", network_code);
			Integer totalCount = (Integer) urmReader.queryForObject("RfResource.getListByRfID", dto);
			if(totalCount==0){
				rfList.add(dto);
			}
		}
		inDto.put("list", rfList);
		System.out.println(inDto);
		if("".equals(msg)&&rfList.size()==0){
			msg = "导入的数据已存在！" ;
		}
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account",account) ;
		
		Dto userDto = organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = rfService.batchRfImport(inDto);
		outDto.put("msg", msg);
		System.out.println("往数据库导入成功!");
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	public ActionForward queryResourceByCode (ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			CommonActionForm aForm = (CommonActionForm) form;
			Dto inDto = aForm.getParamAsDto(request);
			String strChecked = inDto.getAsString("strChecked");
			
			Dto outDto = new BaseDto();

			String ip = ph.getValue("urm.redisserver.ip");
			Integer port =  Integer.valueOf(ph.getValue("urm.redisserver.port"));

			try{
				RedisUseable usable = new RedisUseable() ;
			
				if(usable.jedisUseable(ip, port, urm_redis_passwd)){
					String[] checkli = strChecked.split(",");
					Boolean flag = FALSE ;
					for(int i=0;i< checkli.length ;i++){
						Dto in = new BaseDto();
						in.put("rf_code", checkli[i]);
						Dto dto = (Dto)urmReader.queryForObject("RfResource.getRfArea",in) ;
						String key = "URM_RF_PER_"+dto.getAsString("area_id")+"_"+dto.getAsString("sg_id")+"_"+dto.getAsString("rf_id");
						String rfPer = mdb.getString(key);
						if(rfPer!=null && !"0".equals(rfPer)){
							flag = TRUE ;
							break ;
						}
					}
					outDto.put("success", flag) ;
				}else{
					log.info("【错误】：URM的缓存密码配置错误！");
					outDto.put("msg", "缓存密码错误，操作失败！") ;
					outDto.put("success", FALSE) ;	
				}
				String json = JsonHelper.encodeObject2Json(outDto);
				write(json,response);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				
			}
			return mapping.findForward(null);
		}
}
