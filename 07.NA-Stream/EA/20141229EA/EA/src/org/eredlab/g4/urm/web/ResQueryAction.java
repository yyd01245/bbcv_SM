package org.eredlab.g4.urm.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eredlab.g4.bmf.util.SpringBeanLoader;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.ccl.util.G4Constants;
import org.eredlab.g4.common.util.SerialnoUtil;
import org.eredlab.g4.common.util.StringUtil;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.csmp.rm.dto.req.rm.QueryresourceRequest;
import prod.nebula.csmp.rm.dto.resp.rm.QueryresourceResult;
import prod.nebula.csmp.rm.service.impl.ResourceManagerServiceImpl;
import prod.nebula.framework.utils.socket.IoSocketClient;
import prod.nebula.framework.utils.spring.ApplicationContextHelper;

public class ResQueryAction extends BaseAction  {
	
	private final Logger  logger = LoggerFactory.getLogger(getClass());
	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	private String ip = ph.getValue("rm.tcpserver.ip") ;
	private Integer port =Integer.valueOf(ph.getValue("rm.tcpserver.port")) ;
	private ResourceManagerServiceImpl resourceManagerService = (ResourceManagerServiceImpl)
			SpringBeanLoader.getSpringBean("resourceManagerService");
	
	public ActionForward toResQuery(ActionMapping map,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		return map.findForward("resQuery");
	}
	
	public ActionForward queryResInfo(ActionMapping map,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		CommonActionForm cform = (CommonActionForm)form ;
		Dto inDto = cform.getParamAsDto(request);
		QueryresourceRequest req = new QueryresourceRequest() ;
		req.setCmd("queryresource");
		req.setRtype(inDto.getAsString("rtype")) ;
		req.setSessionid(inDto.getAsString("sessionid")) ;
		req.setType(inDto.getAsString("type")) ;
		req.setVstbid(inDto.getAsString("vstbid")) ;
		req.setStreamip(inDto.getAsString("serverip")) ;
		List<QueryresourceResult> list= resourceManagerService.queryresource(req);
		if(list == null)
			list = new ArrayList() ;
		int total = list.size() ;
		List<Dto> outlist = new ArrayList<Dto>() ;
		for(int i = inDto.getAsInteger("start"); list.size()>i&&i<inDto.getAsInteger("start")+inDto.getAsInteger("limit"); i++){
			 Dto out = new BaseDto();
             out.put("vstbid", list.get(i).getVstbid());
             out.put("sessionid", list.get(i).getSessionid());
             if(StringUtil.assertNotNull(list.get(i).getIpqamip()))
              out.put("ipqamaddr", list.get(i).getIpqamip()+":"+list.get(i).getIpqamport());
             if(StringUtil.assertNotNull(list.get(i).getKeyip()))
            	 out.put("keyaddr", list.get(i).getKeyip()+":"+ list.get(i).getKeyport());
             out.put("operid", list.get(i).getOperid());
             out.put("resid", list.get(i).getResid());
             out.put("rfid", list.get(i).getRfid());
             out.put("sid", list.get(i).getSid());
             out.put("rtype",list.get(i).getType());
             if(StringUtil.assertNotNull(list.get(i).getSip()))
            	 out.put("saddr", list.get(i).getSip()+":"+list.get(i).getSport());
             if(StringUtil.assertNotNull(list.get(i).getStreamip()))
            	 out.put("streamaddr", list.get(i).getStreamip()+":"+list.get(i).getStreamport());
             out.put("type", list.get(i).getType());
             out.put("createtime", list.get(0).getCreatetime());
             outlist.add(out) ;
			
		}
        
        String result = encodeList2PageJson(outlist, total, G4Constants.FORMAT_DateTime);
	    write(result, response);
		return map.findForward(null);
	}
	
	
	public ActionForward deleteExpRes(ActionMapping map,ActionForm form,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		CommonActionForm cform = (CommonActionForm)form ;
		Dto inDto = cform.getParamAsDto(request);
		Dto outDto = new BaseDto() ;
		String str1 = inDto.getAsString("strChecked1") ;
		String str2 = inDto.getAsString("strChecked");
		boolean flag = true ;
		String[] sessionids = str1.split(",");
		String[] vstbids = str2.split(",") ;
		
		IoSocketClient client = new IoSocketClient(ip,port,2000);
        for(int i = 0 ; i < sessionids.length ; i++){
        	JSONObject dto = new JSONObject();
        	dto.put("cmd", "freeresource");
        	dto.put("vstbid", vstbids[i]) ;
        	dto.put("sessionid", sessionids[i]) ;
        	dto.put("serialno", SerialnoUtil.getRandomString(16)) ;
        	String result = client.sendStr(dto.toString()+"XXEE") ;
        	if(result!=null && !"".equals(result)){
        		Dto dtoRet = JsonHelper.parseSingleJson2Dto(result);
        		if(dtoRet.getAsInteger("retcode")==0)
        			flag &= true;
        		else
        			flag &= false ;
        	}else
        		flag &= false ;
        		
        }
        outDto.put("success", flag) ;
        String result = encodeObjectJson(outDto);
	    write(result, response);
		return map.findForward(null);
	}

}
