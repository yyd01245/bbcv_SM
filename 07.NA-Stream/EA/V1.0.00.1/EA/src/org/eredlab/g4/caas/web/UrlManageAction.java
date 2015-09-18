package org.eredlab.g4.caas.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.caas.service.R30aService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.client.IOSocketClient;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;


public class UrlManageAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(UrlManageAction.class);
	
	private R30aService r30aService = (R30aService) super.getService("r30aService");
	PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward yunAppInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("initView");
	}
	
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward r30aInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward("r30aView");
	}
	/**
	 * 分页查询
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryYunAppItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto queryDto = new BaseDto();
		queryDto.put("cmd", "s2qquery");
		String serialno = getRandomString(16);
		queryDto.put("serialno", serialno);
		String queryStr = JsonHelper.encodeObject2Json(queryDto);
		queryStr+="XXEE";
		log.info("【ADMIN】查询S2Q发送报文===>" + queryStr);
		IOSocketClient client = new IOSocketClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		String retString = client.sendStr(ph.getValue("aim.tcpserver.ip"), Integer.valueOf(ph.getValue("aim.tcpserver.port")), 5000, null, queryStr, codecFactory, null);
//		String retString ="{\"cmd\":\"s2qquery\",\"retcode\":\"0\",\"s2qpool\":[\"http://21.254.40.254:8080\",\"http://21.254.42.254:8080\"],\"serialno\":\"db974e9d8f424d2da87b246831442867\"}XXEE";
		retString = retString.replace("XXEE", "");
		log.info("【ADMIN】查询S2Q接收报文===>" + retString);
		List list = new ArrayList();
		if(!(retString==null||"".equals(retString))){
			String jsonString = JsonHelper.encodeObject2Json(retString);
			Dto outDto = (Dto) JsonHelper.parseSingleJson2Dto(jsonString);
			List s2qList = (List)outDto.getAsList("s2qpool");
			if(s2qList.size()>0){
				for(int i=0;i<s2qList.size();i++){
					String s2qID = (String)s2qList.get(i);
					Dto dto = new BaseDto();
					dto.put("id", s2qID);
					list.add(dto);
				}
			}
		}
		String retStr = JsonHelper.encodeList2PageJson(list, list.size(), null);
		write(retStr, response);
		return mapping.findForward(null);
	}
	



	/**
	 * 分页查询R30A
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		r30aService.queryItems(inDto);
		return mapping.findForward(null);
	}
	
	/**
	 * 保存R30A
	 * 
	 * @param
	 * @return
	 */
	public ActionForward saveItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		Dto outDto = r30aService.saveItem(inDto);
		String result = outDto.getAsString("result");
		System.out.println("======================"+result+"======================");
		setOkTipMsg("R30A添加成功", response);
		return mapping.findForward(null);
	}

	/**
	 * 删除R30A
	 * 
	 * @param
	 * @return
	 */
	public ActionForward deleteItems(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		r30aService.deleteItems(inDto);
		setOkTipMsg("R30A删除成功", response);
		return mapping.findForward(null);
	}

	/**
	 * 修改R30A
	 * 
	 * @param
	 * @return
	 */
	public ActionForward updateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		r30aService.updateItem(inDto);
		setOkTipMsg("R30A修改成功", response);
		return mapping.findForward(null);
	}
	//随机获取n位随机数
	public String getRandomString(int length) { 
	    StringBuffer buffer = new StringBuffer("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"); 
	    StringBuffer sb = new StringBuffer(); 
	    Random r = new Random(); 
	    int range = buffer.length(); 
	    for (int i = 0; i < length; i ++) { 
	        sb.append(buffer.charAt(r.nextInt(range))); 
	    } 
	    return sb.toString(); 
	}
	

}
