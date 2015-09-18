package org.eredlab.g4.urm.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.client.TcpClient;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.eredlab.g4.urm.service.KeyConfigService;


/**   
 * 类名称：KeyConfigAction   
 * 类描述：键值定义配置   
 * 创建人：PengFei   
 * 创建时间：2015-1-6 上午11:55:41   
 * 备注：   游戏键值配置和系统按键配置
 * @version    
 *    
 */
public class KeyConfigAction extends BaseAction {
	private static Log log = LogFactory.getLog(AdsAction.class);


	
	private KeyConfigService keyConfigService = (KeyConfigService)super.getService("keyConfigService");
	PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	String watchDogIp = ph.getValue("watchdog.server.ip");
	String watchDogPort = ph.getValue("watchdog.server.port");
	
	
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward pageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		  
		log.info("键值配置页面初始化");
		return mapping.findForward("initView");
	}
	
	
	
	
	public ActionForward querySfxmDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto dto = aForm.getParamAsDto(request);
		List list = g4Reader.queryForPage("Demo.queryCatalogsForGridDemo", dto);
		Integer countInteger = (Integer) g4Reader.queryForObject("Demo.countCatalogsForGridDemo", dto);
		String jsonString = JsonHelper.encodeList2PageJson(list, countInteger, G4Constants.FORMAT_Date);
		super.write(jsonString, response);
		return mapping.findForward(null);
	}
	
	
	
	
	public ActionForward queryKeys(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		log.info("查询键值列表");
		request.setCharacterEncoding("gbk");
		response.setCharacterEncoding("gbk");
		CommonActionForm aForm = (CommonActionForm) form;
		Dto dto = aForm.getParamAsDto(request);
		
		String key_mean = dto.getAsString("key_mean");
		log.info("查询参数：key_mean="+key_mean);
		log.info("查询参数：config_ip="+dto.getAsString("config_ip"));
		log.info("查询参数：config_name="+dto.getAsString("config_name"));
		log.info("查询参数：config_port="+dto.getAsString("config_port"));
		
	
		List list = urmReader.queryForPage("KeyConfig.getKeysListForPage", dto);
		Integer countInteger = (Integer) urmReader.queryForObject("KeyConfig.getKeyConfigCount", dto);
		String jsonString = JsonHelper.encodeList2PageJson(list, countInteger, G4Constants.FORMAT_Date);
		super.write(jsonString, response);
		return mapping.findForward(null);
	}
	
	
	
	/**
	 * 删除网络区域
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteKeyConfigs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		log.info("删除键值");
		
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);

		Dto dto = new BaseDto();
	
		Dto outDto = keyConfigService.deleteKeyConfigs(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
//		setOkTipMsg("网络区域删除成功", response);
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
		
		log.info("键值配置保存或新增");
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List list  = aForm.getGridDirtyData(request);
		int saveTotal = 0;
		int updateTotal = 0;
		
      log.info("list="+list);
		
		
		JSONObject obj1 = new JSONObject();
		
		
		obj1.put("cmd", "configupdate");
		obj1.put("appname", inDto.getAsString("appname"));
		obj1.put("addr", inDto.getAsString("addr"));
		
		
		
		
		
		JSONObject obj2 = new JSONObject();
		
	
		JSONArray array1 = new JSONArray();
		
		
		for (int i = 0; i < list.size(); i++) {
			Dto dto = (BaseDto)list.get(i);
			
			Integer id = dto.getAsInteger("id");		
			String key_mean = dto.getAsString("key_mean");
			String key_name = dto.getAsString("key_name");
			String key_value = dto.getAsString("key_value");	
			
			JSONObject obj3 = new JSONObject();
			obj3.put("key", key_name);
			obj3.put("value", key_value);
			obj3.put("name", key_mean);
			obj3.put("property", "0");
			
			array1.add(obj3);
			log.info("前台填写的参数，：");		
			if(id!=null&&id>0){				
				log.info("更新键值，参数id="+id);
				log.info("更新键值，参数key_mean="+key_mean);
				log.info("更新键值，参数key_name="+key_name);
				log.info("更新键值，参数key_value="+key_value);			
				Dto outDto = keyConfigService.updateKeyConfig(dto);
				++updateTotal;
			}else{					
				log.info("新增键值，参数id="+id);
				log.info("新增键值，参数key_mean="+key_mean);
				log.info("新增键值，参数key_name="+key_name);
				log.info("新增键值，参数key_value="+key_value);
			
				Dto outDto = keyConfigService.saveKeyConfig(dto);
				++saveTotal;
			}
		
			
//			obj2.put("appconfig", array1);
//			
//			obj1.put("configinfo", obj2);
			obj1.put("configinfo", array1);
			

		}
		String str =obj1.toString();
		  
		String sendstr =str+"XXEE";
		
		
	
		
		log.info("sendstr发送的报文："+sendstr);
		TcpClient client = new TcpClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		String returnStr = client.sendStr(watchDogIp, Integer.parseInt(watchDogPort), 5000, null, sendstr,codecFactory);
		log.info("接收到的报文："+returnStr);
		
	

	    String	revStr = returnStr.replace("XXEE", "");
		Dto retDto = new BaseDto();
	     if(returnStr==null||"".equals(returnStr)){
	    	 log.info("看门狗服务器没有返回值，是否挂了？端口Ip地址难道配置有错吗"+returnStr);
			retDto.put("success", false);
		}
		if (!(revStr == null || "".equals(revStr))) {
			String JsonStr = JsonHelper.encodeObject2Json(revStr);
			Dto dto = JsonHelper.parseSingleJson2Dto(JsonStr);
			if(dto.getAsInteger("retcode")<0){
				retDto.put("success", false);
				//retDto.put("msg", "保存失败");
			}else{
				retDto.put("success", true);
				//retDto.put("msg", "保存成功，一共新增"+saveTotal+"条，一个更新"+updateTotal+"条");
			}
		}else{
			retDto.put("success", false);
		//	retDto.put("msg", "保存失败");
		}
			
		String jsonString = JsonHelper.encodeObject2Json(retDto);
		
		super.write(jsonString, response);
		return mapping.findForward(null);
	}
}
