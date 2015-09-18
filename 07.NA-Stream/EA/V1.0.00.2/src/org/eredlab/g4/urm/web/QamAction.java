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
import org.eredlab.g4.urm.service.QamService;

import prod.nebula.framework.mdb.impl.MdbDataImpl;

import redis.clients.jedis.JedisPool;
import com.sun.net.httpserver.Authenticator.Success;

public class QamAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(QamAction.class);
	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	
	private QamService qamService = (QamService) super.getService("qamService");
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
	public ActionForward pageInittoTh(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("initPage1");
	}
	/**
	 * 查询QAM
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryQamItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamList = urmReader.queryForList("Qam.getQamListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Qam.getQamListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryItemsByNetworkCode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamList = urmReader.queryForPage("Qam.getQamListByKey", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Qam.getQamListByKeyCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamList, totalCount, null);
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
		List qamList = urmReader.queryForPage("Qam.getQamListByType"+area, inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Qam.getQamListByTypeCount"+area, inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	
		
	public ActionForward queryIPQAMListBykey(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamList = urmReader.queryForList("Qam.queryIPQAMListBykey", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Qam.queryIPQAMListBykeyCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public ActionForward queryIPQAMResListBykey(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamList = urmReader.queryForList("Qam.queryIPQAMResListBykey", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Qam.queryIPQAMResListBykey", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 保存
	 * 
	 * @param
	 * @return
	 */
	public ActionForward saveQamItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		Integer totalCount = (Integer) urmReader.queryForObject("Qam.queryCountByQAMIP", inDto);
//		Integer totalCount1 = (Integer) urmReader.queryForObject("Qam.queryNameByQAMIP", inDto);
//		totalCount+=totalCount1;
		
		inDto.put("sum", 0);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account) ;
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo") ;
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid()) ;
		
		Dto outDto= qamService.saveQamItem(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}

	/**
	 * 删除QAM
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteQamItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
	
		inDto.put("strChecked", strChecked);
		qamService.deleteQamItems(inDto);
		setOkTipMsg("QAM数据删除成功", response);
		return mapping.findForward(null);
	}

	/**
	 * 修改QAM
	 * 
	 * @param
	 * @return
	 */
	public ActionForward updateQamItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
//		Integer totalCount = (Integer) urmReader.queryForObject("Qam.queryCountByQAMIPUpdate", inDto);
//		Integer totalCount1 = (Integer) urmReader.queryForObject("Qam.queryNameByQAMIPUpdate", inDto);
//		totalCount+=totalCount1;
		inDto.put("sum", 0);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account) ;
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo") ;
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid()) ;
		
		Dto outDto = qamService.updateQamItem(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
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
		String metaData = "ipqam_name,ipqam_ip,ipqam_port,ipqam_type,ipqam_level";
		ExcelReader excelReader = new ExcelReader(metaData, theFile.getInputStream());
		List list = excelReader.read(1);
		String msg = "";
		if(list.size()==0){
			msg = "导入的文件格式不正确或是文件中没有数据！" ;
		}
		System.out.println("返回Action");
		System.out.println("Excel中有"+list.size()+"条记录");

		System.out.println("往数据库导入......");
		String network_code = inDto.getAsString("network_code");
		List qamList = new ArrayList();
		for(int i=0;i<list.size();i++){
			Dto dto = (Dto) list.get(i);
			dto.put("network_code", network_code);
			dto.put("band_width", 37500);
			dto.put("status", 1);
			Integer totalCount = (Integer) urmReader.queryForObject("Qam.queryCountByQAMIP", dto);
			if(totalCount==0){
				qamList.add(dto);
			}
		}
		if("".equals(msg)&&qamList.size()==0){
			msg = "导入的数据已存在！" ;
		}
		inDto.put("list", qamList);
//		inDto.put("list", list);
		System.out.println(inDto);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account) ;
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo") ;
		inDto.put("oper_id", user.getUserid());
		inDto.put("dept_id", user.getDeptid()) ;
		
		Dto outDto = qamService.batchResImport(inDto);
		outDto.put("msg", msg);
		System.out.println("往数据库导入成功!");
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	
//新增缓存同步加载控制方式
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
		inDto.put("operpassword", userInfo.getOperpassword());
		inDto.put("oper_id", userInfo.getUserid());
		inDto.put("dept_id", userInfo.getDeptid()) ;
		Dto outDto= qamService.saveCacheContor(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
//查询缓存同步事件
	public ActionForward queryCacheContor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamList = urmReader.queryForList("Qam.queryCacheContor", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("Qam.queryCacheContorForPageCount", inDto);
//		List cumsList = cssReader.queryForList("UserManage.queryCacheContor", inDto);
//		Integer Count = (Integer) cssReader.queryForObject("UserManage.queryCacheContorForPageCount", inDto);
		List list = new ArrayList();
//		if(cumsList.size()>0){
//			Dto dto = (Dto) cumsList.get(0);
//			dto.put("status", 99);
//			dto.put("type", 1);
//			list.add(dto);
//		}
		if(qamList.size()>0){
			for(int i=0;i<qamList.size();i++){
				Dto dto = (Dto) qamList.get(i);
				dto.put("type", 2);
				list.add(dto);
			}
		}
//		totalCount+=Count;
		String jsonStrList = JsonHelper.encodeList2PageJson(list, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	public static void main(String args[]){
		Dto dto = new BaseDto();
		dto.put("t", "111");
		String s = JsonHelper.encodeObject2Json(dto);
		System.out.print(s);
	}
	
	/**
	 * QAM下架
	 * 
	 * @param
	 * @return
	 */
	public ActionForward downloadQamItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		
		qamService.downloadQamItems(inDto);
		setOkTipMsg("QAM数据删除成功", response);
		return mapping.findForward(null);
	}
	/**
	 * QAM预下架
	 * 
	 * @param
	 * @return
	 */
	public ActionForward redownloadQamItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		
		qamService.redownloadQamItems(inDto);
		setOkTipMsg("QAM数据删除成功", response);
		return mapping.findForward(null);
	}
	/**
	 * QAM上架
	 * 
	 * @param
	 * @return
	 */
	public ActionForward uploadQamItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		
		qamService.uploadQamItems(inDto);
		setOkTipMsg("QAM数据删除成功", response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryItemByQamID (ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
//		
//		CommonActionForm aForm = (CommonActionForm) form;
//		Dto inDto = aForm.getParamAsDto(request);
//		String strChecked = inDto.getAsString("strChecked");
//		
//		Dto outDto = new BaseDto();
//		
//		String ip = ph.getValue("urm.redisserver.ip");
//		Integer port =  Integer.valueOf(ph.getValue("urm.redisserver.port"));
//		
//		try{
//			RedisUseable usable = new RedisUseable() ;
//			if(usable.jedisUseable(ip, port, urm_redis_passwd)){
//				String[] checkli = strChecked.split(",");
//				Boolean flag = FALSE ;
//				for(int i=0;i< checkli.length ;i++){
//					Dto in = new BaseDto();
//					in.put("ipqam_id", checkli[i]);
//					List li = urmReader.queryForList("QamResource.getResList",in) ;
//					if(li!=null){
//						for(Object o : li){
//							Dto dto = (Dto)o;
//							String key = "RES"+dto.getAsString("ipqam_res_id") ;
//							String rfPer = (String) mdb.getString(key);
//							if(rfPer!=null && !"0".equals(rfPer)){
//								flag = TRUE ;
//								break ;
//							}
//						}
//						if(flag)
//							break ;
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
//		
	}
	
	
}