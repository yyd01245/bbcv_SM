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
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.ccl.util.G4Utils;
import org.eredlab.g4.rif.report.fcf.FcfDataMapper;
import org.eredlab.g4.rif.report.fcf.GraphConfig;
import org.eredlab.g4.rif.report.fcf.Set;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.AdsService;
import org.eredlab.g4.urm.service.KeyConfigService;



/**
 * 表格标准范例暨教程Action
 * 
 * @author XiongChun
 * @since 2010-10-23
 * @see BaseAction
 */
public class ChartDetailAction extends BaseAction {
	private static Log log = LogFactory.getLog(AdsAction.class);

	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	
	private KeyConfigService keyConfigService = (KeyConfigService)super.getService("keyConfigService");

	private OrganizationService organizationService = (OrganizationService) super.getService("organizationService");
	
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward pageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		  
       System.out.println("KeyConfigAction---------------------------------------");
       
		//实例化一个图形配置对象
		GraphConfig graphConfig = new GraphConfig();
		//主标题
		graphConfig.setCaption("负载量监控图");
	
		
		graphConfig.setCanvasBorderColor("D64646");
		graphConfig.setCanvasBorderThickness(TRUE);
		//使用这种方式可以加入框架没有封装的原生报表属性,原生属可以参考《开发指南》的相关章节
		//graphConfig.put("propertyName", "value");
		//设置水平分割线的颜色
		graphConfig.put("divLineColor", "008ED6");
		//设置水平分割线的透明度
		graphConfig.put("divLineAlpha", "60");
		//设置对水平分割区域使用斑马纹
		graphConfig.put("showAlternateHGridColor", "1");
		//设置斑马纹颜色
		graphConfig.put("AlternateHGridColor", "BFFFFF");
		//设置斑马纹的透明度
		graphConfig.put("alternateHGridAlpha", "10");
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
			set.setName(dto.getAsString("name")); //名称
			set.setValue(dto.getAsString("value")); //数据值
			set.setColor(dto.getAsString("color")); //柱状图颜色
			dataList.add(set);
		}
		//将图表数据转为Flash能解析的XML资料格式
		String xmlString = FcfDataMapper.toFcfXmlData(dataList, graphConfig);
		//此Key对应的<flashReport />标签的datavar属性
		request.setAttribute("xmlString", xmlString);
       
       
		return mapping.findForward("initView");
	}
	
	
	
	/**
	 * 表格演示三页面初始化
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward gridDemo3Init(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("gridDemo3View");
	}
	

	/**
	 * 查询医院收费项目数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryKeys(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		System.out.println("queryKeys-----------------------------------------------------");
		CommonActionForm aForm = (CommonActionForm) form;
		Dto dto = aForm.getParamAsDto(request);
		
		List list = urmReader.queryForPage("KeyConfig.getKeysListForPage", dto);
		Integer countInteger = (Integer) urmReader.queryForObject("KeyConfig.getKeyConfigCount", dto);
		String jsonString = JsonHelper.encodeList2PageJson(list, countInteger, G4Constants.FORMAT_Date);
		super.write(jsonString, response);
		return mapping.findForward(null);
	}
	
	
	
	
	
	/**
	 * 查询医院结算数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryBalanceInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		System.out.println("queryBalanceInfo-----------------------------------------------------");
		CommonActionForm aForm = (CommonActionForm) form;
		Dto dto = aForm.getParamAsDto(request);
		List list = urmReader.queryForPage("KeyConfig.queryBalanceInfo", dto);
		Integer countInteger = (Integer) urmReader.queryForObject("KeyConfig.countBalanceInfo", dto);
		super.setSessionAttribute(request, "GRIDACTION_QUERYBALANCEINFO_DTO", dto);
		String jsonString = encodeList2PageJson(list, countInteger, G4Constants.FORMAT_Date);
		super.write(jsonString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 汇总医院结算数据
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward sumBalanceInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		System.out.println("sumBalanceInfo-----------------------------------------------------");
		Dto dto = (BaseDto)super.getSessionAttribute(request, "GRIDACTION_QUERYBALANCEINFO_DTO");
		Dto sumDto = sumDto = (BaseDto)urmReader.queryForObject("KeyConfig.sumBalanceInfo", dto);
		if (G4Utils.isNotEmpty(sumDto)) {
			sumDto.put("sxh", "共" + sumDto.getAsString("sxh") + "人次");
		}
		sumDto.put("success", new Boolean(true));
		String jsonString = JsonHelper.encodeObject2Json(sumDto);
		super.write(jsonString, response);
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
	public ActionForward saveOrUpadateKey(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("saveOrUpadateKey-----------------------------------------------------");
		CommonActionForm aForm = (CommonActionForm) form;
		
		
		
		
//		Dto dto = aForm.getParamAsDto(request);
//		
//		BaseActionForm aForm = (BaseActionForm) form;
		List list  = aForm.getGridDirtyData(request);
		int saveTotal = 0;
		int updateTotal = 0;
		
		for (int i = 0; i < list.size(); i++) {
			Dto dto = (BaseDto)list.get(i);
			
			Integer id = dto.getAsInteger("id");
			
			String key_mean = dto.getAsString("key_mean");
			String key_name = dto.getAsString("key_name");
			String key_value = dto.getAsString("key_value");
			String remark = dto.getAsString("remark");
			String state = dto.getAsString("state");		
			
			log.info("前台填写的参数，：");
			
			log.info("新增键值，参数id="+id);
			log.info("新增键值，参数key_mean="+key_mean);
			log.info("新增键值，参数key_name="+key_name);
			log.info("新增键值，参数key_value="+key_value);
			log.info("新增键值，参数remark="+remark);		
			log.info("新增键值，参数state="+state);
			
			if(id!=null&&id>0){
				Dto outDto = keyConfigService.updateKeyConfig(dto);
				++updateTotal;
			}else{
				Dto outDto = keyConfigService.saveKeyConfig(dto);
				++saveTotal;
			}
		
			//Dto outDto = keyConfigService.saveKeyConfig(dto);
		//	System.out.println("脏数据:\n" + dto);
			//todo anything what u want   request.getParameter("dirtydata")
		}
		Dto outDto = new BaseDto();
		outDto.put("success", new Boolean(true));
		outDto.put("msg", "一共新增"+saveTotal+"条，一个更新"+updateTotal+"条");
		super.write(outDto.toJson(), response);
		return mapping.findForward(null);
	}
}
