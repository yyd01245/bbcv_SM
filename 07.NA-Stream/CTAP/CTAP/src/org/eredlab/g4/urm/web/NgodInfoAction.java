package org.eredlab.g4.urm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
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
import org.eredlab.g4.common.util.RedisUseable;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.NgodService;

import prod.nebula.framework.mdb.impl.MdbDataImpl;

public class NgodInfoAction extends BaseAction{
	private static Log log = LogFactory.getLog(NgodInfoAction.class);

	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	private NgodService ngodService = (NgodService)super.getService("ngodService");
	private OrganizationService organizationService =  (OrganizationService) super.getService("organizationService");
	private String urm_redis_passwd = ph.getValue("urm.redisserver.password");
	private MdbDataImpl mdb = (MdbDataImpl) super.getService("urmmdbImpl") ;
	/**
	 * NGOD页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward ngodPageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("ngodPageInit");
	}
	
	/**
	 * NGOD rf页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward ngodRFPageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("ngodRFPageInit");
	}
	
	/**
	 * D6R6页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward D6R6PageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("D6R6PageInit");
	}
	
	/**
	 * 查询Ngod数据队列
	 * @param
	 * @return
	 */
	public ActionForward queryNgodInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("ngod.getNgodInfoList", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("ngod.getNgodInfoListForCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, G4Constants.FORMAT_Date);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 保存NGOD信息
	 * @param
	 * @return
	 */
	public ActionForward saveNgno(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer totalCount = (Integer) urmReader.queryForObject("ngod.getCountByName", inDto);//重名校验
		Integer totalCount1 = (Integer) urmReader.queryForObject("ngod.getCountByQAMIP", inDto);//QAMIP校验
		totalCount+=totalCount1;
		inDto.put("sum", totalCount);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = ngodService.saveNgodInfo(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	/**
	 * 修改NGOD信息
	 * @param
	 * @return
	 */
	public ActionForward updateNgodInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		
		Integer count1 = (Integer)urmReader.queryForObject("ngod.getCountUpByName",inDto);
		Integer count2 = (Integer)urmReader.queryForObject("ngod.getCountUpByQAMIP", inDto);
		Integer sum = 0 ;
		if(count1!=null)
			sum += count1 ;
		if(count2!=null)
			sum += count2 ;
		
		inDto.put("sum", sum);
		Dto outDto = ngodService.updateNgodInfo(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 删除NGOD信息
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteNgodInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = ngodService.deleteNgodInfo(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询Ngod频点数据队列
	 * @param
	 * @return
	 */
	public ActionForward queryNgodRfInfoList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("ngod.getNgodRfInfoList", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("ngod.getNgodRfInfoListForCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, G4Constants.FORMAT_Date);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 保存NGOD频点信息
	 * @param
	 * @return
	 */
	public ActionForward saveNgodRfInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Integer totalCount = (Integer) urmReader.queryForObject("ngod.getCountByKey", inDto);//信息重复匹配校验
		inDto.put("sum", totalCount);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = ngodService.saveNgodRfInfo(inDto);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		return mapping.findForward(null);
	}
	/**
	 * 修改NGOD信息
	 * @param
	 * @return
	 */
	public ActionForward updateNgodRfInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		
		Integer totalCount = (Integer) urmReader.queryForObject("ngod.getCountUpByKey", inDto);//信息重复匹配校验
		inDto.put("sum", totalCount);
		Dto outDto = ngodService.updateNgodRfInfo(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 删除NGOD信息
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteNgodRfInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		
		String account = WebUtils.getSessionContainer(request).getUserInfo().getAccount();
		Dto dto = new BaseDto() ;
		dto.put("account", account);
		Dto userDto = (Dto)organizationService.getUserInfo(dto);
		UserInfoVo user = (UserInfoVo)userDto.get("userInfo");
		inDto.put("oper_id", user.getUserid()) ;
		inDto.put("dept_id", user.getDeptid());
		
		Dto outDto = ngodService.deleteNgodRfInfo(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询应用名下拉队列
	 * @param
	 * @return
	 */
	public ActionForward queryAppNameList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List appNameList = urmReader.queryForList("ngod.getAppInfoDownList", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(appNameList,appNameList.size(), null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询Ngod频点下拉队列
	 * @param
	 * @return
	 */
	public ActionForward queryNgodList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List ngodList = urmReader.queryForList("ngod.getNgodInfoDownList", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(ngodList, ngodList.size(), null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}	
	
	public ActionForward queryResByNetwork (ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String key = inDto.getAsString("key");
		Dto outDto = new BaseDto();
		String ip = ph.getValue("urm.redisserver.ip");
		Integer port = Integer.valueOf(ph.getValue("urm.redisserver.port"));
		try{
			RedisUseable res = new RedisUseable() ;
			if(res.jedisUseable(ip, port, urm_redis_passwd)){
				String[] checked = key.split(",");
				Boolean flag = FALSE ;
				String appname = ph.getValue("urm.auth.appname") ;
				for(String checkKey : checked){
					List ngod_rf = (List) mdb.getObject("URM_RF_"+appname+checkKey);
					if(ngod_rf!=null){
						for(int i = 0; i < ngod_rf.size();i++){
							String rfKey = "URM_RF_PER_"+checkKey+"_"+ngod_rf.get(i);
							String rfPer = (String) mdb.getString(rfKey);
							if(rfPer != null && !"0".equals(rfPer)){
								flag = TRUE ;
								break ;
							}
						}
					}
					if(flag)
						break;
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
	
	public ActionForward queryResourceByCode (ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			CommonActionForm aForm = (CommonActionForm) form;
			Dto inDto = aForm.getParamAsDto(request);
			String strChecked = inDto.getAsString("strChecked");
			
			Dto outDto = new BaseDto();
	
			String ip = ph.getValue("urm.redisserver.ip") ;
			Integer port =  Integer.valueOf(ph.getValue("urm.redisserver.port"));
			try{
				RedisUseable usable = new RedisUseable() ;
				if(usable.jedisUseable(ip, port, urm_redis_passwd)){
					String[] checkli = strChecked.split(",");
					Boolean flag = FALSE ;
					for(int i=0;i< checkli.length ;i++){
						Dto in = new BaseDto();
						in.put("info_id", checkli[i]);
						Dto dto = (Dto)urmReader.queryForObject("ngod.getAreaById",in) ;
						String key = "URM_RF_PER_"+dto.getAsString("area_id")+"_"+dto.getAsString("region_id")+"_"+dto.getAsString("rf_id");
						String rfPer = (String) mdb.getString(key);
						if(rfPer != null && !"0".equals(rfPer)){
							flag = TRUE ;
							break ;
						}
					}
					outDto.put("success", flag);
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
	
	public ActionForward queryD6R6(ActionMapping map , ActionForm form ,HttpServletRequest request ,
			HttpServletResponse response)throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List d6r6List = urmReader.queryForPage("ngod.getD6R6", inDto);
		Integer d6r6Count = (Integer)urmReader.queryForObject("ngod.getD6R6Count", inDto) ;
		String jsonStrList = JsonHelper.encodeList2PageJson(d6r6List,d6r6Count, null);
		write(jsonStrList, response);
		return map.findForward(null);
	}
	
	public ActionForward queryNgodById(ActionMapping map ,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		String str = inDto.getAsString("strChecked");
		int sum = 0 ;
		String[] strs = str.split(",");
		for(int i = 0 ; i < strs.length ; i++){
			Dto dto = new BaseDto() ;
			dto.put("ngod_id", strs[i]) ;
			Integer count = (Integer) urmReader.queryForObject("ngod.getNgodCount",dto);
			sum += count ;
		}
		Dto outDto = new BaseDto() ;
		if(sum>0){
			outDto.put("success", TRUE);
		}else{
			outDto.put("success", FALSE);
		}
		String stObj = JsonHelper.encodeObject2Json(outDto);
		write(stObj,response);
		return map.findForward(null);
	}
	
}
