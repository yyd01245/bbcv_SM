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
import org.eredlab.g4.rif.report.fcf.FcfDataMapper;
import org.eredlab.g4.rif.report.fcf.GraphConfig;
import org.eredlab.g4.rif.report.fcf.Set;
import org.eredlab.g4.rif.util.WebUtils;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.AdsService;

/**   
 * 类名称：KeyGatewayDistributionAction   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-12-24 上午10:13:57   
 * 备注：   
 * @version    
 *    
 */
public class MonitorHomePageAction extends BaseAction{
	


	
	private static Log log = LogFactory.getLog(MonitorHomePageAction.class);

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
		  
		
		log.info("概览页面初始化");

		String productName = "接入网关";
		CommonActionForm aForm = (CommonActionForm) form;
		Dto dto1= aForm.getParamAsDto(request);
		
		
		List list1 = g4Reader.queryForList("Demo.getFcfDataCountByProduct", dto1);
		
		for(int i=0 ;i<list1.size();i++){
			
			Dto dto = (BaseDto)list1.get(i);
			
		     String product = 	dto.getAsString("product");
			
			 if(product.equals("1")){
		    	   productName ="接入网关";	    	 
		    	   Integer joinCountOnline =dto.getAsInteger("count");
		    		request.setAttribute("joinCountOnline",joinCountOnline+"");
		       }
		       if(product.equals("2")){
		    	   productName ="接入网关";	    	
		    	   Integer joinCountOffline = dto.getAsInteger("count");
		    	   request.setAttribute("joinCountOffline",joinCountOffline+"");
		       }
		       if(product.equals("3")){
		    	   productName ="键值网关";	    	 
		    	   Integer KeyCountOnline =dto.getAsInteger("count");
		    	   request.setAttribute("KeyCountOnline",KeyCountOnline+"");
		       }
		       if(product.equals("4")){
		    	   productName ="键值网关";	    	 
		    	   Integer KeyCountOffline =dto.getAsInteger("count");
		    	   request.setAttribute("KeyCountOffline",KeyCountOffline+"");
		       }
		       if(product.equals("5")){
		    	   productName ="键值网关";	    	 
		    	   Integer KeyCountNoAuth =dto.getAsInteger("count");
		    	   request.setAttribute("KeyCountNoAuth",KeyCountNoAuth+"");
		       }
		       if(product.equals("6")){
		    	   productName ="信令网关";	    	 
		    	   Integer signalingCountOnline =dto.getAsInteger("count");
		    	   request.setAttribute("signalingCountOnline",signalingCountOnline+"");
		       }
		       if(product.equals("7")){
		    	   productName ="信令网关";	    	 
		    	   Integer signalingCountOffline =dto.getAsInteger("count");
		    	   request.setAttribute("signalingCountOffline",signalingCountOffline+"");
		       }
		       if(product.equals("8")){
		    	   productName ="信令网关";	    	 
		    	   Integer signalingCountNoAuth =dto.getAsInteger("count");
		    	   request.setAttribute("signalingCountNoAuth",signalingCountNoAuth+"");
		       }
		     

		}
	      
		
			GraphConfig graphConfig = new GraphConfig();
			//主标题
			
			graphConfig.setCaption("接入网关负载量统计图");
			//X坐标轴名称
			graphConfig.setXAxisName("注：红色表示警告");
			//数字值前缀
		//	graphConfig.setNumberPrefix("$");
			//使用这种方式可以加入框架没有封装的原生报表属性,原生属可以参考《G4Studio开发指南》的相关章节
			//graphConfig.put("propertyName", "value");
	        Dto qDto = new BaseDto();		
	        qDto.put("product", "1");
	        //查询原始数据
			List list = g4Reader.queryForList("Demo.getFcfDataList", qDto);
			List dataList = new ArrayList();
			//将原始数据对象转换为框架封装的Set报表数据对象
			for (int i = 0; i < list.size(); i++) {
				Dto dto = (BaseDto)list.get(i);
				//实例化一个图表元数据对象
				Set set = new Set();
				String name = dto.getAsString("name");
				set.setName(name); //名称
				set.setValue(dto.getAsString("value")); //数据值
				set.setColor(dto.getAsString("color")); //柱状图颜色				
				set.setJsFunction("showGatewayInfo(\"1\")"); //调用的JS函数
				
				dataList.add(set);
			}
			//将图表数据转为Flash能解析的XML资料格式
			String xmlString = FcfDataMapper.toFcfXmlData(dataList, graphConfig);
			//此Key对应的<flashReport />标签的datavar属性
			request.setAttribute("xmlString", xmlString);
		
			
			
			
			
			GraphConfig graphConfig1 = new GraphConfig();
			//主标题
			graphConfig1.setCaption("报警统计图");
			//X坐标轴名称
			graphConfig.setXAxisName("注：红色表示警告");
			//数字值前缀
		//	graphConfig.setNumberPrefix("$");
			//使用这种方式可以加入框架没有封装的原生报表属性,原生属可以参考《G4Studio开发指南》的相关章节
			//graphConfig.put("propertyName", "value");
	     
	        //查询原始数据
		//	List list1 = g4Reader.queryForList("Demo.getFcfDataList", qDto);
		     List dataList1 = new ArrayList();
			//将原始数据对象转换为框架封装的Set报表数据对象
			List list2 = urmReader.queryForList("KeyGatewayDistribution.getAdsReportChartCount", qDto);
			
			log.info("查询警告总数list.size()="+list2.size());
			log.info("查询警告总数list="+list2);
		
			Dto dto = (BaseDto)list2.get(0);
			Set set1 = new Set();
			set1.setName("一级告警"); //名称
			
			String f =  dto.getAsString("firstem");

			set1.setValue(f);
			set1.setColor("FF0000"); //柱状图颜色
			set1.setJsFunction("fnMyJs1(\"xiongchun\")"); //调用的JS函数
			dataList1.add(set1);
			
			
		
			
			Set set2 = new Set();
			set2.setName("二级告警"); //名称
			
			String s =  dto.getAsString("secondem");
		
			set2.setValue(s); //数据值
		
			set2.setColor("FFCC66"); //柱状图颜色
			set2.setJsFunction("fnMyJs2(\"xiongchun\")"); //调用的JS函数
			dataList1.add(set2);
			
			Set set3 = new Set();
			set3.setName("三级告警"); //名称
			
			String t =  dto.getAsString("thirdem");
		
			set3.setValue(t); //数据值
			set3.setColor("FFFFCC"); //柱状图颜色
			set3.setJsFunction("fnMyJs3(\"xiongchun\")"); //调用的JS函数
			dataList1.add(set3);
			
			
			
			String xmlString1 = FcfDataMapper.toFcfXmlData(dataList1, graphConfig1);
			
			request.setAttribute("xmlString1", xmlString1);
		//	return mapping.findForward("integrateFlashReport1View");
			
			
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
		List qamResList = urmReader.queryForPage("KeyGatewayDistribution.getAdsListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("KeyGatewayDistribution.getAdsListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	
	/**
	 * 查询数据报表XML格式串
	 * 点击台数，显示负载量
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryReportXmlDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("点击查看几台，将更新网关负载量柱状图");
		
	  	CommonActionForm aForm = (CommonActionForm) form;
		Dto dto= aForm.getParamAsDto(request);
		
		String product = dto.getAsString("product");
		String productName = "接入网关";
		
	       if(product.equals("1")){
	    	   log.info("查看接入网关在线总台数");
	    	   productName ="接入网关";
	       }
	       if(product.equals("2")){
	    	   productName ="接入网关";
	    	   log.info("查看接入网关离线总台数");
	       }
	       if(product.equals("3")){
	    	   productName ="键值网关";
	    	   log.info("查看键值网关在线总台数");
	       }
	       if(product.equals("4")){
	    	   productName ="键值网关";
	    	   log.info("查看键值网关离线总台数");
	       }
	       if(product.equals("5")){
	    	   productName ="键值网关";
	    	   log.info("查看信令网关未认证总台数");
	       }
	       if(product.equals("6")){
	    	   productName ="信令网关";
	    	   log.info("查看信令网关在线总台数");
	       }
	       if(product.equals("7")){
	    	   productName ="信令网关";
	    	   log.info("查看信令网关离线线总台数");
	       }
	       if(product.equals("8")){
	    	   productName ="信令网关";
	    	   log.info("查看信令网关未认证总台数");
	       }
			GraphConfig graphConfig = new GraphConfig();
			//主标题
			graphConfig.setCaption(productName+"负载量统计图");
			//X坐标轴名称
			graphConfig.setXAxisName("注：红色表示警告");
			//数字值前缀
		List list = g4Reader.queryForList("Demo.getFcfDataList", dto);
		//实例化一个图形配置对象
	//	GraphConfig graphConfig = new GraphConfig();
		//主标题
	//	graphConfig.setCaption("Google软件2010年月度销售业绩图表" + product);
		//X坐标轴名称
	//	graphConfig.setXAxisName("月度");
		//数字值前缀
	//	graphConfig.setNumberPrefix("$");
		//使用这种方式可以加入框架没有封装的原生报表属性,原生属可以参考《G4Studio开发指南》的相关章节
		//graphConfig.put("propertyName", "value");
		List dataList = new ArrayList();
		//将原始数据对象转换为框架封装的Set报表数据对象
		for (int i = 0; i < list.size(); i++) {
			Dto dto1 = (BaseDto)list.get(i);
			//实例化一个图表元数据对象
			Set set = new Set();
			set.setName(dto1.getAsString("name")); //名称
			set.setValue(dto1.getAsString("value")); //数据值
			set.setColor(dto1.getAsString("color")); //柱状图颜色
			set.setJsFunction("showGatewayInfo(\"1\")"); //调用的JS函数
			dataList.add(set);
		}
		//将图表数据转为Flash能解析的XML资料格式
		String xmlString = FcfDataMapper.toFcfXmlData(dataList, graphConfig);
		Dto outDto = new BaseDto();
		outDto.put("success", new Boolean(true));
		request.setAttribute("countNum", list.size()+"");
		outDto.put("xmlstring", xmlString);
		outDto.put("xmlstring1", xmlString);
		write(JsonHelper.encodeObject2Json(outDto), response);
		return mapping.findForward(null);
	}
	
	
	
	public ActionForward queryAdss(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForPage("KeyGatewayDistribution.getAdsLists", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("KeyGatewayDistribution.getAdsListsCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(qamResList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryAdsList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List qamResList = urmReader.queryForList("KeyGatewayDistribution.getAdsListForPage", inDto);
		Integer totalCount = (Integer) urmReader.queryForObject("KeyGatewayDistribution.getAdsListForPageCount", inDto);
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
		Integer totalCount = (Integer) urmReader.queryForObject("KeyGatewayDistribution.getNameByKey", inDto);

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
		Integer totalCount = (Integer) urmReader.queryForObject("KeyGatewayDistribution.getNameByUpdate", inDto);

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
