package org.eredlab.g4.crsm.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.common.util.CacheTempData;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.SerialnoUtil;
import org.eredlab.g4.common.util.client.IOSocketClient;
import org.eredlab.g4.crsm.service.CrsmServerMonitorService;

import prod.nebula.crsm.query.core.QueryManager;
import prod.nebula.crsm.query.entity.RsmAttribute;
import prod.nebula.crsm.query.entity.RsmResource;
import prod.nebula.crsm.query.service.CRSMUtil;

public class CrsmServerMonitorServiceImpl extends BaseServiceImpl implements
		CrsmServerMonitorService {
	Log logger = LogFactory.getLog(CrsmServerMonitorServiceImpl.class);
	PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	String authname = ph.getValue("crsm.auth.authname");
	String authcode = ph.getValue("crsm.auth.authcode");
	String ip = ph.getValue("crsm.tcpserver.ip");
	int port = Integer.valueOf(ph.getValue("crsm.tcpserver.port"));
	SerialnoUtil serialnoUtil = new SerialnoUtil();

	public Dto savaConfigById(Dto inDto) {
		Dto outDto = new BaseDto();
		Dto cfgDto = new BaseDto();//修改配置文件报文
		cfgDto.put("cmd", "chgcfg");
		cfgDto.put("cfgname", inDto.getAsString("maxnum"));
		cfgDto.put("cfgvalue", inDto.getAsString("max_line"));
		cfgDto.put("serialno", serialnoUtil.getRandomString(8));
		String server_ip = inDto.getAsString("server_ip");
		Dto crsmDto = new BaseDto();//与CRSM交互
		crsmDto.put("cmd", "manage");
		crsmDto.put("vncid", server_ip);
		crsmDto.put("authname", authname);
		crsmDto.put("authcode", authcode);
		crsmDto.put("msg", cfgDto);
		crsmDto.put("serialno", serialnoUtil.getRandomString(8));
		String queryStr = JsonHelper.encodeObject2Json(crsmDto);
		String queryString = queryStr + "XXEE";
		logger.info("【ADMIN】服务器修改配置发送报文===>" + queryString);
		IOSocketClient client = new IOSocketClient();
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		String revStr = client.sendStr(ip, port, 5000, null, queryString,
				codecFactory, null);
		logger.info("【ADMIN】服务器修改配置接收报文===>" + revStr);
		revStr = revStr.replace("XXEE", "");
		if(revStr==null||"".equals(revStr)){
			outDto.put("success", new Boolean(false));
		}else{
			Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
			int retcode = dto.getAsInteger("retcode");
			Calendar calendar = Calendar.getInstance();
			Date createdate = calendar.getTime();
			Dto operDto = new BaseDto();
			operDto.put("operater", inDto.getAsString("account"));
			operDto.put("server_id", server_ip);
			operDto.put("operate_time", createdate);
			operDto.put("operate_type", "managecfg");
			String retString;
			if (retcode < 0) {
				outDto.put("success", new Boolean(false));
				operDto.put("operate_res", "failure");
			} else {
				String msg = JsonHelper.encodeObject2Json(dto.get("msg"));
				Dto cfgRetDto = JsonHelper.parseSingleJson2Dto(msg);
				int cfgRetCode = cfgRetDto.getAsInteger("retcode");
				if(cfgRetCode<0){
					outDto.put("success", new Boolean(false));
					operDto.put("operate_res", "failure");
				}else{
					outDto.put("success", new Boolean(true));
					operDto.put("operate_res", "success");
				}
			}
			crsmDao.insert("CrsmServerMonitor.insertEvent",operDto);
		}
		return outDto;
	}

	public Dto saveCabinet(Dto inDto) {
		Dto outDto = new BaseDto();
		int sum = inDto.getAsInteger("sum");
		if(sum>0){
			outDto.put("success", new Boolean(false));
		}else{
			int card_num = Integer.valueOf(inDto.getAsString("card_num"));
			List list = new ArrayList();
			for(int i=1;i<=card_num;i++){
				Dto dto = new BaseDto();
				dto.putAll(inDto);
				dto.put("cabinet_card", i);
				list.add(dto);
			}
			try {
				crsmDao.batchInsert("CrsmServerMonitor.saveCabinet", list);
				outDto.put("success", new Boolean(true));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return outDto;
	}

	public Dto savaBanding(Dto inDto) {
		String server_id = inDto.getAsString("server_id");
		String[] server_ips = server_id.split(":");
		inDto.put("server_ip", server_ips[0]);
		inDto.put("server_port", server_ips[1]);
		Dto outDto = new BaseDto();
		crsmDao.update("CrsmServerMonitor.updateServerInfo", inDto);//将cabinet_id保存到承载表中
		crsmDao.update("CrsmServerMonitor.updateCabinetInfo", inDto);//将cabinet_id对应isbangding改为1
		outDto.put("success", new Boolean(true));
		return outDto;
	}

	public Dto serverOnlineOperate(Dto inDto) {
		Dto outDto = new BaseDto();
		String server_IPs = inDto.getAsString("server_ip");
		String[] server_ips = server_IPs.split(",");
		boolean flag = true;
		for(int i =0;i<server_ips.length;i++){
			Dto queryDto = new BaseDto();
			queryDto.put("cmd", "onshelf");
			queryDto.put("vncid", server_ips[i]);
			queryDto.put("authname", authname);
			queryDto.put("authcode", authcode);
			queryDto.put("msg", "11111");
			queryDto.put("serialno", serialnoUtil.getRandomString(8));
			String queryStr = JsonHelper.encodeObject2Json(queryDto);
			String queryString = queryStr + "XXEE";
			logger.info("【ADMIN】服务器上线发送报文===>" + queryString);
			System.out.println("【ADMIN】服务器上线发送报文===>" + queryString);
			IOSocketClient client = new IOSocketClient();
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
					CommandSupport.CHARSET, CommandSupport.TAIL,
					CommandSupport.TAIL);
			String revStr = client.sendStr(ip, port, 5000, null, queryString,
					codecFactory, null);
			logger.info("【ADMIN】服务器上线接收报文===>" + revStr);
			System.out.println("【ADMIN】服务器上线接收报文===>" + revStr);
			revStr = revStr.replace("XXEE", "");
			if(revStr==null||"".equals(revStr)){
				flag=false;
			}else{
				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
				int retcode = dto.getAsInteger("retcode");
				Calendar calendar = Calendar.getInstance();
				Date createdate = calendar.getTime();
				Dto operDto = new BaseDto();
				operDto.put("operater", inDto.getAsString("account"));
				operDto.put("server_id", server_ips[i]);
				operDto.put("operate_time", createdate);
				operDto.put("operate_type", "onshelf");
				String retString;
				if (retcode < 0) {
					flag=false;
					operDto.put("operate_res", "failure");
				} else {
					operDto.put("operate_res", "success");
					String[] server_ids = server_ips[i].split(":");
					Dto statusDto = new BaseDto();
					statusDto.put("server_ip", server_ids[0]);
					statusDto.put("server_port", server_ids[1]);
					statusDto.put("vstatus", "normal");
					List list = new ArrayList();
					list.add(statusDto);
					updateServerMore(list);
					
				}
				crsmDao.insert("CrsmServerMonitor.insertEvent",operDto);
			}
		}
		outDto.put("success", flag);
		return outDto;
	}

	public Dto serverOfflineOperate(Dto inDto) {
		Dto outDto = new BaseDto();
		String server_IPs = inDto.getAsString("server_ip");
		String[] server_ips = server_IPs.split(",");
		boolean flag = true;
		for(int i=0;i<server_ips.length;i++){
			Dto queryDto = new BaseDto();
			queryDto.put("cmd", "offshelf");
			queryDto.put("vncid", server_ips[i]);
			queryDto.put("authname", authname);
			queryDto.put("authcode", authcode);
			queryDto.put("msg", "11111");
			queryDto.put("serialno", serialnoUtil.getRandomString(8));
			String queryStr = JsonHelper.encodeObject2Json(queryDto);
			String queryString = queryStr + "XXEE";
			logger.info("【ADMIN】服务器下线发送报文===>" + queryString);
			System.out.println("【ADMIN】服务器下线发送报文===>" + queryString);
			IOSocketClient client = new IOSocketClient();
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
					CommandSupport.CHARSET, CommandSupport.TAIL,
					CommandSupport.TAIL);
			String revStr = client.sendStr(ip, port, 5000, null, queryString,
					codecFactory, null);
			logger.info("【ADMIN】服务器下线接收报文===>" + revStr);
			System.out.println("【ADMIN】服务器下线接收报文===>" + revStr);
			revStr = revStr.replace("XXEE", "");
			if(revStr==null||"".equals(revStr)){
				flag=false;
			}else{
				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
				int retcode = dto.getAsInteger("retcode");
				Calendar calendar = Calendar.getInstance();
				Date createdate = calendar.getTime();
				Dto operDto = new BaseDto();
				operDto.put("operater", inDto.getAsString("account"));
				operDto.put("server_id", server_ips[i]);
				operDto.put("operate_time", createdate);
				operDto.put("operate_type", "offshelf");
				String retString;
				if (retcode < 0) {
					operDto.put("operate_res", "failure");
					flag=false;
				} else {
					operDto.put("operate_res", "success");
					String[] server_ids = server_ips[i].split(":");
					Dto statusDto = new BaseDto();
					statusDto.put("server_ip", server_ids[0]);
					statusDto.put("server_port", server_ids[1]);
					statusDto.put("vstatus", "unused");
					List list = new ArrayList();
					list.add(statusDto);
					updateServerMore(list);
					
				}
				crsmDao.insert("CrsmServerMonitor.insertEvent",operDto);
			}
		}
		outDto.put("success", flag);
		return outDto;
	}

	public String querySesionDetail(Dto inDto) {
		String token = CacheTempData.AUTHTOKEN.get("token");
		QueryManager crsmQuery = CRSMUtil.getQueryManager(token);
		Map<String, RsmResource> crsmMap = crsmQuery.getUserResourcesByRsm(inDto.getAsString("server_ip"));
		List list = new ArrayList();
		Dto outDto = new BaseDto();
		if(!crsmMap.isEmpty()){
			for(Map.Entry entry : crsmMap.entrySet()){
				RsmResource rsmResource = (RsmResource)entry.getValue();
				String jsonString = JsonHelper.encodeObject2Json(rsmResource);
				Dto dto = JsonHelper.parseSingleJson2Dto(jsonString);
				list.add(dto);
			}
		}
		return JsonHelper.encodeList2PageJson(list, list.size(), null);
	}

	public Dto queryServerSingle(Dto inDto) {
		String token = CacheTempData.AUTHTOKEN.get("token");
		QueryManager crsmQuery = CRSMUtil.getQueryManager(token);
		String server_id = inDto.getAsString("nodeName");
		String rsmDetail = crsmQuery.getRsmDetailInfo(server_id);
		Dto rsmDto = JsonHelper.parseSingleJson2Dto(rsmDetail);
		RsmAttribute rsm = crsmQuery.getResource(server_id);
		logger.info("单个查询，当前承载信息=========="+rsm);
		if(rsm!=null){
			long online_num = rsm.getMaxnum()-rsm.getFreenum();
			rsmDto.put("online_num", online_num);
			rsmDto.put("max_num", rsm.getMaxnum());
			rsmDto.put("liuhua_num", rsm.getStreamnum());
			rsmDto.put("rate", rsm.getRate());
			rsmDto.put("security", rsm.getSecurity());
			String vstatus = rsm.getStatus();
			if(vstatus=="unused"||"unused".equals(vstatus)){
				Map<String, RsmResource> map = crsmQuery.getUserResourcesByRsm(server_id);
				logger.info("单个查询，当前承载没有用户在用："+map.isEmpty());
				if(!map.isEmpty()){
					vstatus="perunused";
				}
			}
			logger.info("单个查询，承载当前状态："+vstatus);
			rsmDto.put("vstatus", vstatus);
		}else{
			String vstatus = "perunused";
			Map<String, RsmResource> map = crsmQuery.getUserResourcesByRsm(server_id);
			logger.info("承载信息为空，查询当前承载没有用户在用："+map.isEmpty());
			if(map.isEmpty()){
				vstatus="unused";
			}
			rsmDto.put("vstatus", vstatus);
		}
		rsmDto.put("server_ip", inDto.getAsString("server_ip"));
		rsmDto.put("server_port", inDto.getAsString("server_port"));
		Dto outDto = new BaseDto();
		int i =crsmDao.update("CrsmServerMonitor.updateServerMoreInfo",rsmDto);
		if(i>0){
			outDto.put("success", new Boolean(true));
		}else{
			outDto.put("success", new Boolean(false));
		}
		return outDto;
	}

	public Dto killSession(Dto inDto) {
		Dto outDto = new BaseDto();
		String resID = inDto.getAsString("strChecked1");
		String[] terms = resID.split(",");
		for(String resid : terms){

			Dto queryDto = new BaseDto();
			queryDto.put("cmd", "resfree");
			queryDto.put("resid", resid);
			queryDto.put("authname", authname);
			queryDto.put("authcode", authcode);
			queryDto.put("msg", "111111");
			queryDto.put("serialno", serialnoUtil.getRandomString(8));
			String queryStr = JsonHelper.encodeObject2Json(queryDto);
			String queryString = queryStr + "XXEE";
			logger.info("【ADMIN】释放在线资源发送报文===>" + queryStr);
			System.out.println("【ADMIN】释放在线资源发送报文===>" + queryStr);
			// Client tcpClient = new TcpClient();
			IOSocketClient client = new IOSocketClient();
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
					CommandSupport.CHARSET, CommandSupport.TAIL,
					CommandSupport.TAIL);
			String revStr = client.sendStr(ip, port, 5000, null, queryString,
					codecFactory, null);
			logger.info("【ADMIN】释放在线资源接收报文===>" + revStr);
			System.out.println("【ADMIN】释放在线资源接收报文===>" + revStr);
			revStr = revStr.replace("XXEE", "");
			if(revStr!=null && !"".equals(revStr)){
				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
				int retcode = dto.getAsInteger("retcode");
				if (retcode < 0) {
					outDto.put("success", new Boolean(false));
				} else {
					outDto.put("success", new Boolean(true));
				}
			}else{
				outDto.put("success", new Boolean(false));
			}
		}	
		return outDto;
	}

	public Dto deleteCabinet(Dto inDto) {
		Dto outDto = new BaseDto();
		crsmDao.delete("CrsmServerMonitor.deleteCabinetItems", inDto);
		outDto.put("success", new Boolean(true));
		return outDto;
	}

	public void saveServerMore(List insertList) {
		try {
			crsmDao.batchInsert("CrsmServerMonitor.saveServer", insertList);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}		
	}

	public void updateServerMore(List updateList) {
		if(updateList.size()>0){
			for (Object object : updateList) {
				Dto dto = (Dto)object;
				crsmDao.update("CrsmServerMonitor.updateServerMoreInfo", dto);
			}
		}
		
	}

	public Dto serverAuthOperate(Dto inDto) {
		Dto outDto = new BaseDto();
		String server_IPs = inDto.getAsString("server_ip");
		String[] server_ips = server_IPs.split(",");
		boolean flag = true;
		for(int i=0;i<server_ips.length;i++){
			Dto queryDto = new BaseDto();
			queryDto.put("cmd", "activate");
			queryDto.put("vncid", server_ips[i]);
			queryDto.put("authname", authname);
			queryDto.put("authcode", authcode);
			queryDto.put("msg", "11111");
			queryDto.put("serialno", serialnoUtil.getRandomString(8));
			String queryStr = JsonHelper.encodeObject2Json(queryDto);
			String queryString = queryStr + "XXEE";
			logger.info("【ADMIN】服务器激活发送报文===>" + queryString);
			System.out.println("【ADMIN】服务器激活发送报文===>" + queryString);
			IOSocketClient client = new IOSocketClient();
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
					CommandSupport.CHARSET, CommandSupport.TAIL,
					CommandSupport.TAIL);
			String revStr = client.sendStr(ip, port, 5000, null, queryString,
					codecFactory, null);
			logger.info("【ADMIN】服务器激活接收报文===>" + revStr);
			System.out.println("【ADMIN】服务器激活接收报文===>" + revStr);
			revStr = revStr.replace("XXEE", "");
			if(revStr==null||"".equals(revStr)){
				flag=false;
			}else{
				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
				int retcode = dto.getAsInteger("retcode");
				Calendar calendar = Calendar.getInstance();
				Date createdate = calendar.getTime();
				Dto operDto = new BaseDto();
				operDto.put("operater", inDto.getAsString("account"));
				operDto.put("server_id", server_ips[i]);
				operDto.put("operate_time", createdate);
				operDto.put("operate_type", "activate");
				String retString;
				if (retcode < 0) {
					flag=false;
					operDto.put("operate_res", "failure");
				} else {
					operDto.put("operate_res", "success");
					String[] server_ids = server_ips[i].split(":");
					Dto statusDto = new BaseDto();
					statusDto.put("server_ip", server_ids[0]);
					statusDto.put("server_port", server_ids[1]);
					statusDto.put("vstatus", "normal");
					List list = new ArrayList();
					list.add(statusDto);
					updateServerMore(list);
				}
				crsmDao.insert("CrsmServerMonitor.insertEvent",operDto);
			}
		}
		outDto.put("success", flag);
		return outDto;
	}

	public Dto serverPerOfflineOperate(Dto inDto) {
		Dto outDto = new BaseDto();
		String server_IPs = inDto.getAsString("server_ip");
		String[] server_ips = server_IPs.split(",");
		boolean flag = true;
		for(int i=0;i<server_ips.length;i++){
			Dto queryDto = new BaseDto();
			queryDto.put("cmd", "preoffshelf");
			queryDto.put("vncid", server_ips[i]);
			queryDto.put("authname", authname);
			queryDto.put("authcode", authcode);
			queryDto.put("msg", "11111");
			queryDto.put("serialno", serialnoUtil.getRandomString(8));
			String queryStr = JsonHelper.encodeObject2Json(queryDto);
			String queryString = queryStr + "XXEE";
			logger.info("【ADMIN】服务器预下线发送报文===>" + queryString);
			System.out.println("【ADMIN】服务器预下线发送报文===>" + queryString);
			IOSocketClient client = new IOSocketClient();
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
					CommandSupport.CHARSET, CommandSupport.TAIL,
					CommandSupport.TAIL);
			String revStr = client.sendStr(ip, port, 5000, null, queryString,
					codecFactory, null);
			logger.info("【ADMIN】服务器预下线接收报文===>" + revStr);
			System.out.println("【ADMIN】服务器预下线接收报文===>" + revStr);
			revStr = revStr.replace("XXEE", "");
			if(revStr==null||"".equals(revStr)){
				flag = false;
			}else{
				Dto dto = JsonHelper.parseSingleJson2Dto(revStr);
				int retcode = dto.getAsInteger("retcode");
				Calendar calendar = Calendar.getInstance();
				Date createdate = calendar.getTime();
				Dto operDto = new BaseDto();
				operDto.put("operater", inDto.getAsString("account"));
				operDto.put("server_id", server_ips[i]);
				operDto.put("operate_time", createdate);
				operDto.put("operate_type", "preoffshelf");
				String retString;
				if (retcode < 0) {
					flag = false;
					operDto.put("operate_res", "failure");
				} else {
					operDto.put("operate_res", "success");
					String vstatus = "perunused";
					String[] server_ids = server_ips[i].split(":");
					QueryManager queryManager = CRSMUtil.getQueryManager(CacheTempData.AUTHTOKEN.get("token"));
					Map<String, RsmResource> map = queryManager.getUserResourcesByRsm(server_ips[i]);
					logger.info("预下线，当前承载没有用户在用："+map.isEmpty());
					if(map.isEmpty()){
						vstatus="unused";
					}
					logger.info("预下线，当前承载状态："+vstatus);
					Dto statusDto = new BaseDto();
					statusDto.put("server_ip", server_ids[0]);
					statusDto.put("server_port", server_ids[1]);
					statusDto.put("vstatus", vstatus);
					List list = new ArrayList();
					list.add(statusDto);
					updateServerMore(list);
				}
				crsmDao.insert("CrsmServerMonitor.insertEvent",operDto);
			}
		}
		outDto.put("success", flag);
		return outDto;
	}

	public Dto serverDeleteCabinet(Dto inDto) {
		Dto outDto = new BaseDto();
		crsmDao.update("CrsmServerMonitor.updateServerByIp", inDto);
		crsmDao.update("CrsmServerMonitor.updateCabinetByID",inDto);
		outDto.put("success", new Boolean(true));
		return outDto;
	}

	public void updateServerStatus(Dto inDto) {
		crsmDao.update("CrsmServerMonitor.updateServer", inDto);		
	}

}
