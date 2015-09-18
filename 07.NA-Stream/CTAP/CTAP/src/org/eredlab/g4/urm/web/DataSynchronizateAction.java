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
import org.eredlab.g4.urm.service.DateSynchronizateService;

public class DataSynchronizateAction extends BaseAction{
	private static Log log = LogFactory.getLog(DataSynchronizateAction.class);
	private DateSynchronizateService dateSynchronizateService = (DateSynchronizateService) super.getService("dateSynchronizateService");
	PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	String ip = ph.getValue("watchdog.server.ip");
	int port = Integer.valueOf(ph.getValue("watchdog.server.port"));
	public ActionForward queryCagListAndInsertDB(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
//		CommonActionForm cFrom = (CommonActionForm)form;
//		Dto inDto = cFrom.getParamAsDto(request);
		Dto queryDto = new BaseDto();
		queryDto.put("cmd", "caglistquery");
		String queryStr = JsonHelper.encodeObject2Json(queryDto)+"XXEE";
		IOSocketClient client = new IOSocketClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
//		String retString = client.sendStr(ip, port, 4000, null, queryStr, codecFactory,null);
		String retString ="{\"cmd\":\"caglistquery\",\"retcode\":\"0\",\"cagpool\":[{\"cagip\":\"192.168.100.11\",\"cagport\":\"12345\",\"status\":\"0\",\"cpu\":\"33\",\"mem\":\"67\",\"gpu\":\"24\",\"version\":\"1.1.00.1\",\"threadid\":\"567893\",\"kgwpool\":[{\"kgwip\":\"192.168.100.12\",\"kgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"13\",\"mem\":\"50\",\"gpu\":\"4\",\"version\":\"1.1.00.1\",\"threadid\":\"565673\",\"capacitysize\":\"2000\"},{\"kgwip\":\"192.168.100.19\",\"kgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"13\",\"mem\":\"50\",\"gpu\":\"4\",\"version\":\"1.1.00.1\",\"threadid\":\"565673\",\"capacitysize\":\"2000\"}],\"sgwpool\":[{\"sgwip\":\"192.168.100.13\",\"sgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"43\",\"mem\":\"60\",\"gpu\":\"24\",\"version\":\"1.1.00.3\",\"threadid\":\"565673\",\"capacitysize\":\"2000\"},{\"sgwip\":\"192.168.100.14\",\"sgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"13\",\"mem\":\"50\",\"gpu\":\"4\",\"version\":\"1.1.00.1\",\"threadid\":\"565673\",\"capacitysize\":\"2000\"}]},{\"cagip\":\"192.168.100.15\",\"cagport\":\"12345\",\"status\":\"0\",\"cpu\":\"33\",\"mem\":\"80\",\"gpu\":\"24\",\"version\":\"1.1.00.1\",\"threadid\":\"567893\",\"kgwpool\":[{\"kgwip\":\"192.168.100.16\",\"kgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"13\",\"mem\":\"50\",\"gpu\":\"4\",\"version\":\"1.1.00.1\",\"threadid\":\"565673\",\"capacitysize\":\"2000\"},{\"kgwip\":\"192.168.100.20\",\"kgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"13\",\"mem\":\"50\",\"gpu\":\"4\",\"version\":\"1.1.00.1\",\"threadid\":\"565673\",\"capacitysize\":\"2000\"}],\"sgwpool\":[{\"sgwip\":\"192.168.100.17\",\"sgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"43\",\"mem\":\"60\",\"gpu\":\"24\",\"version\":\"1.1.00.3\",\"threadid\":\"565673\",\"capacitysize\":\"2000\"},{\"sgwip\":\"192.168.100.18\",\"sgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"13\",\"mem\":\"50\",\"gpu\":\"4\",\"version\":\"1.1.00.1\",\"threadid\":\"565673\",\"capacitysize\":\"2000\"}]}],\"serialno\":\"e22aff4bb8da4ed991d9f7c043614d68\"}XXEE";
		if(!(retString==null||"".equals(retString))){
			retString=retString.replace("XXEE", "");
			Dto retDto = JsonHelper.parseSingleJson2Dto(retString);
			String retcode = retDto.getAsString("retcode");
			List cagInsertList = new ArrayList();	//cag新增列表
			List cagUpdateList = new ArrayList();	//cag修改列表
			List csgInsertList = new ArrayList();	//csg新增列表
			List csgUpdateList = new ArrayList();	//csg修改列表
			List ckgInsertList = new ArrayList();	//ckg新增列表
			List ckgUpdateList = new ArrayList();	//ckg修改列表
			
			if(retcode=="0"||"0".equals(retcode)){
				List capList = retDto.getAsList("cagpool");
				for (Object object : capList) {
					//处理cag信息
					Dto capDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object));
					/*********解析sgw列表*********/
					List csgList = capDto.getAsList("sgwpool");
					List ckgList = capDto.getAsList("kgwpool");
					String cap_id = (String)urmReader.queryForObject("DateSyn.queryCagIDbyIP", capDto);
					if(cap_id==null||"".equals(cap_id)){
						//数据库中没有记录，新增
						cagInsertList.add(capDto);
					}else{
						//数据库中有记录，修改
						capDto.put("cag_id", cap_id);
						cagUpdateList.add(capDto);
						//统计csg列表
						for (Object object2 : csgList) {
							Dto csgDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object2));
							csgDto.put("cag_id", cap_id);
							String csg_id = (String)urmReader.queryForObject("DateSyn.queryCsgIDbyKey", csgDto);
							if(csg_id==null||"".equals(csg_id)){
								csgInsertList.add(csgDto);
							}else{
								csgDto.put("csg_id", csg_id);
								csgUpdateList.add(csgDto);
							}
						}
						//统计ckg列表
						for (Object object3 : ckgList) {
							Dto ckgDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object3));
							ckgDto.put("cag_id", cap_id);
							String ckg_id = (String)urmReader.queryForObject("DateSyn.queryCkgIDbyKey", ckgDto);
							if(ckg_id==null||"".equals(ckg_id)){
								ckgInsertList.add(ckgDto);
							}else{
								ckgDto.put("ckg_id", ckg_id);
								ckgUpdateList.add(ckgDto);
							}
						}
					}
					
				}
				dateSynchronizateService.savecagInfo(cagInsertList);
				Thread.sleep(100);
				dateSynchronizateService.savecsgInfo(csgInsertList);
				Thread.sleep(100);
				dateSynchronizateService.saveckgInfo(ckgInsertList);
				Thread.sleep(100);
				dateSynchronizateService.updatecagInfo(cagUpdateList);
				Thread.sleep(100);
				dateSynchronizateService.updatecsgInfo(csgUpdateList);
				Thread.sleep(100);
				dateSynchronizateService.updateckgInfo(ckgUpdateList);
			}
		}
		return mapping.findForward(null);
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryCagInfoByIPAndUpdateDB(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cFrom = (CommonActionForm)form;
		Dto inDto = cFrom.getParamAsDto(request);
		String queryIP = inDto.getAsString("cag_ip");
		String queryPort = inDto.getAsString("cag_port");
		Dto queryDto = new BaseDto();
		queryDto.put("cmd", "check");
		queryDto.put("ip", queryIP);
		queryDto.put("port", queryPort);
		String queryStr = JsonHelper.encodeObject2Json(queryDto)+"XXEE";
		IOSocketClient client = new IOSocketClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
//		String retString = client.sendStr(ip, port, 4000, null, queryStr, codecFactory,null);
		String retString = client.sendStr(queryIP, port, 4000, null, queryStr, codecFactory, null);
//		String retString = "{\"cmd\":\"cagquery\",\"retcode\":\"0\",\"cagip\":\"192.168.100.11\",\"cagport\":\"12345\",\"status\":\"0\",\"cpu\":\"43\",\"mem\":\"80\",\"gpu\":\"54\",\"version\":\"1.1.00.1\",\"threadid\":\"534393\",\"kgwpool\":[{\"kgwip\":\"192.168.100.12\",\"kgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"53\",\"mem\":\"80\",\"gpu\":\"41\",\"version\":\"1.1.00.1\",\"threadid\":\"705673\",\"capacitysize\":\"2000\"},{\"kgwip\":\"192.168.100.19\",\"kgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"33\",\"mem\":\"40\",\"gpu\":\"24\",\"version\":\"1.1.00.1\",\"threadid\":\"565673\",\"capacitysize\":\"2300\"}],\"sgwpool\":[{\"sgwip\":\"192.168.100.13\",\"sgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"53\",\"mem\":\"70\",\"gpu\":\"54\",\"version\":\"1.1.00.3\",\"threadid\":\"534223\",\"capacitysize\":\"5000\"},{\"sgwip\":\"192.168.100.14\",\"sgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"63\",\"mem\":\"40\",\"gpu\":\"54\",\"version\":\"1.1.00.1\",\"threadid\":\"535673\",\"capacitysize\":\"2700\"}],\"serialno\":\"e22aff4bb8da4ed991d9f7c043614d68\"}XXEE";
		if(!(retString==null||"".equals(retString))){
			retString=retString.replace("XXEE", "");
			Dto retDto = JsonHelper.parseSingleJson2Dto(retString);
			String retcode = retDto.getAsString("retcode");
			List cagUpdateList = new ArrayList();	//cag修改列表
			List csgInsertList = new ArrayList();	//csg新增列表
			List csgUpdateList = new ArrayList();	//csg修改列表
			List ckgInsertList = new ArrayList();	//ckg新增列表
			List ckgUpdateList = new ArrayList();	//ckg修改列表
			
			if(retcode=="0"||"0".equals(retcode)){
				List csgList = retDto.getAsList("sgwpool");
				List ckgList = retDto.getAsList("kgwpool");
				String cap_id = (String)urmReader.queryForObject("DateSyn.queryCagIDbyIP", retDto);
				retDto.put("cag_id", cap_id);
				cagUpdateList.add(retDto);
				//统计csg列表
				for (Object object2 : csgList) {
					Dto csgDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object2));
					csgDto.put("cag_id", cap_id);
					String csg_id = (String)urmReader.queryForObject("DateSyn.queryCsgIDbyKey", csgDto);
					if(csg_id==null||"".equals(csg_id)){
						csgInsertList.add(csgDto);
					}else{
						csgDto.put("csg_id", csg_id);
						csgUpdateList.add(csgDto);
					}
				}
				//统计ckg列表
				for (Object object3 : ckgList) {
					Dto ckgDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object3));
					ckgDto.put("cag_id", cap_id);
					String ckg_id = (String)urmReader.queryForObject("DateSyn.queryCkgIDbyKey", ckgDto);
					if(ckg_id==null||"".equals(ckg_id)){
						ckgInsertList.add(ckgDto);
					}else{
						ckgDto.put("ckg_id", ckg_id);
						ckgUpdateList.add(ckgDto);
					}
				}
				dateSynchronizateService.savecsgInfo(csgInsertList);
				Thread.sleep(100);
				dateSynchronizateService.saveckgInfo(ckgInsertList);
				Thread.sleep(100);
				dateSynchronizateService.updatecagInfo(cagUpdateList);
				Thread.sleep(100);
				dateSynchronizateService.updatecsgInfo(csgUpdateList);
				Thread.sleep(100);
				dateSynchronizateService.updateckgInfo(ckgUpdateList);
			}
		}
		return mapping.findForward(null);
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward queryConfigByIPAndUpdateDB(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cFrom = (CommonActionForm)form;
		Dto inDto = cFrom.getParamAsDto(request);
		String queryIp = inDto.getAsString("ip");
		String queryPort = inDto.getAsString("port");
		String appname = inDto.getAsString("app_name");
		String qip = (String)request.getAttribute("ip");
		String qport = (String)request.getAttribute("port");
		String qname = (String)request.getAttribute("app_name");
		String queryip = (queryIp==null||"".equals(queryIp))?qip:queryIp;
		String queryport = (queryPort==null||"".equals(queryPort))?qport:queryPort;
		String queryname = (appname==null||"".equals(appname))?qname:appname;
		String addr = queryip+":"+queryport;
		Dto queryDto = new BaseDto();
		queryDto.put("cmd", "configquery");
		queryDto.put("appname", queryname);
		queryDto.put("addr", addr);
		IOSocketClient client = new IOSocketClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		String queryStr = JsonHelper.encodeObject2Json(queryDto)+"XXEE";
		String retString = client.sendStr(queryip, port, 4000, null, queryStr, codecFactory,null);
//		String retString = client.sendStr(ip, port, 4000, null, queryStr, codecFactory, null);
//		String retString = "{\"cmd\":\"cagquery\",\"retcode\":\"0\",\"cagip\":\"192.168.100.11\",\"cagport\":\"12345\",\"status\":\"0\",\"cpu\":\"43\",\"mem\":\"80\",\"gpu\":\"54\",\"version\":\"1.1.00.1\",\"threadid\":\"534393\",\"kgwpool\":[{\"kgwip\":\"192.168.100.12\",\"kgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"53\",\"mem\":\"80\",\"gpu\":\"41\",\"version\":\"1.1.00.1\",\"threadid\":\"705673\",\"capacitysize\":\"2000\"},{\"kgwip\":\"192.168.100.19\",\"kgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"33\",\"mem\":\"40\",\"gpu\":\"24\",\"version\":\"1.1.00.1\",\"threadid\":\"565673\",\"capacitysize\":\"2300\"}],\"sgwpool\":[{\"sgwip\":\"192.168.100.13\",\"sgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"53\",\"mem\":\"70\",\"gpu\":\"54\",\"version\":\"1.1.00.3\",\"threadid\":\"534223\",\"capacitysize\":\"5000\"},{\"sgwip\":\"192.168.100.14\",\"sgwport\":\"12345\",\"status\":\"0\",\"cpu\":\"63\",\"mem\":\"40\",\"gpu\":\"54\",\"version\":\"1.1.00.1\",\"threadid\":\"535673\",\"capacitysize\":\"2700\"}],\"serialno\":\"e22aff4bb8da4ed991d9f7c043614d68\"}XXEE";
//		String retString ="{\"cmd\": \"configquery\",\"appname\": \"cag\",\"ip\":\""+queryip+"\",\"port\":\""+queryport+"\",\"configinfo\":[{\"key\": \"key1\",\"value\": \"value1\",\"name\":\"属性1\",\"property\":\"0\"},{\"key\": \"key2\",\"value\": \"value2\",\"name\":\"属性2\",\"property\":\"0\"}]}XXEE";
		if(!(retString==null||"".equals(retString))){
			retString=retString.replace("XXEE", "");
			Dto retDto = JsonHelper.parseSingleJson2Dto(retString);
			List list = new ArrayList();
			String app_name = retDto.getAsString("appname");
			String app_ip = retDto.getAsString("ip");
			String app_port = retDto.getAsString("port");
//			String configinfo = JsonHelper.encodeObject2Json(retDto.get("configinfo"));
//			Dto configDto = JsonHelper.parseSingleJson2Dto(configinfo);
			List configList = retDto.getAsList("configinfo");
			for (int i = 0; i < configList.size(); i++) {
				Dto itemDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(configList.get(i)));
				itemDto.put("config_ip", app_ip);
				itemDto.put("config_port", app_port);
				itemDto.put("config_name", app_name);
				list.add(itemDto);
			}
			Dto dto = new BaseDto();
			dto.put("app_name", app_name);
			dto.put("ip", app_ip);
			dto.put("port", app_port);
			dateSynchronizateService.delConfigInfo(dto);
			Thread.sleep(300);
			dateSynchronizateService.saveConfigInfo(list);
		}
		return mapping.findForward(null);
	}
	
	
	
	public ActionForward queryAllCagInfoAndUpdateDB(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cFrom = (CommonActionForm)form;
		Dto inDto = cFrom.getParamAsDto(request);
		List list = urmReader.queryForList("DateSyn.getCagList");
		
		if(list!=null){
			IOSocketClient client = new IOSocketClient();
			for (Object object : list) {
				Dto dto = (Dto)object;
				String queryIp = dto.getAsString("ip");
				String queryPort = dto.getAsString("port");
				log.info("【CTAP-ADMIN】全量同步CAG信息：=====同步ip:"+queryIp+",======同步端口:"+queryPort);
				Dto queryDto = new BaseDto();
				queryDto.put("cmd", "check");
				queryDto.put("ip", queryIp);
				queryDto.put("port", queryPort);
				String queryStr = JsonHelper.encodeObject2Json(queryDto)+"XXEE";
				MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
						CommandSupport.CHARSET, CommandSupport.TAIL,
						CommandSupport.TAIL);
//				String retString = client.sendStr(ip, port, 4000, null, queryStr, codecFactory,null);
				String retString = client.sendStr(queryIp, port, 4000, null, queryStr, codecFactory, null);
				dateSynchronizateService.saveApp(retString);
			}
		}
		return mapping.findForward(null);
	}
	
	
}
