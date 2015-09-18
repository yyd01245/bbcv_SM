package org.eredlab.g4.crsm.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.SerialnoUtil;
import org.eredlab.g4.common.util.client.IOSocketClient;
import org.eredlab.g4.crsm.service.CrsmServerMonitorService;

public class CrsmServerMonitorServiceImpl extends BaseServiceImpl implements
		CrsmServerMonitorService {
	Log logger = LogFactory.getLog(CrsmServerMonitorServiceImpl.class);
	PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	String authname = ph.getValue("crsm.auth.authname");
	String authcode = ph.getValue("crsm.auth.authcode");
	String ip = ph.getValue("crsm.tcpserver.ip");
	int port = Integer.valueOf(ph.getValue("crsm.tcpserver.port"));
	SerialnoUtil serialnoUtil = new SerialnoUtil();
	IOSocketClient client = new IOSocketClient();
	MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
			CommandSupport.CHARSET, CommandSupport.TAIL,
			CommandSupport.TAIL);

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
		String vncid = inDto.getAsString("server_ip");
		List list = querySession(vncid);
		return JsonHelper.encodeList2PageJson(list, list.size(), null);
	}

	public Dto queryServerSingle(Dto inDto) {
		Dto queryDetailDto = new BaseDto();
		String server_id = inDto.getAsString("nodeName");
		queryDetailDto.put("cmd", "vncdetail");
		queryDetailDto.put("authname", authname);
		queryDetailDto.put("authcode", authcode);
		queryDetailDto.put("vncid", server_id);
		queryDetailDto.put("serialno", serialnoUtil.getRandomString(16));
		String queryDetailStr = JsonHelper.encodeObject2Json(queryDetailDto)+"XXEE";
		String queryDetailRet = client.sendStr(ip, port, 4000, null, queryDetailStr, codecFactory, null);
//		String queryDetailRet ="{\"cmd\":\"vncdetail\",\"authname\":\"1\",\"authcode\":\"12345678\",\"vncid\":\"192.168.70.249:16000\",\"serialno\":\"db974e9d8f424d2da87b246831442867\"}XXEE";
		Dto detailDto= new BaseDto();
		logger.info("查询当前承载信息返回报文："+queryDetailRet);
		if(queryDetailRet!=null&&!"".equals(queryDetailRet)){
			queryDetailRet = queryDetailRet.replace("XXEE", "");
			Dto retDto = JsonHelper.parseSingleJson2Dto(queryDetailRet);
			if("0".equals(retDto.getAsString("retcode"))){
				String detail = JsonHelper.encodeObject2Json(retDto.get("detail"));
				if(detail!=null&&!"".equals(detail)){
					detailDto = JsonHelper.parseSingleJson2Dto(detail);
				}
				long maxnum = retDto.getAsInteger("totalnum");
				long freenum = retDto.getAsInteger("freenum");
				long online_num = maxnum-freenum;
				long streamnum = retDto.getAsInteger("streamnum");
				detailDto.put("rate", retDto.getAsString("rate"));
				detailDto.put("online_num", online_num);
				detailDto.put("max_num", maxnum);
				detailDto.put("liuhua_num", streamnum);
				detailDto.put("vendor", retDto.getAsString("vendor"));
				detailDto.put("vtype", retDto.getAsString("videotype"));
				detailDto.put("server_ip", retDto.getAsString("vncip"));
				detailDto.put("server_port", retDto.getAsString("vncport"));
				detailDto.put("vability", retDto.getAsString("vnctype"));
				String vstatus = retDto.getAsString("status");
				if(vstatus=="unused"||"unused".equals(vstatus)){
					List sessionList = querySession(server_id);
					logger.info("单个查询，当前承载没有用户在用："+sessionList.isEmpty());
					if(!sessionList.isEmpty()){
						vstatus="perunused";
					}
				}
				logger.info("单个查询，承载当前状态："+vstatus);
				detailDto.put("vstatus", vstatus);
			}else{
				String vstatus = "perunused";
				List sessionList = querySession(server_id);
				logger.info("承载信息为空，查询当前承载没有用户在用："+sessionList.isEmpty());
				if(sessionList.isEmpty()){
					vstatus="unused";
				}
				detailDto.put("vstatus", vstatus);
			}
			
		}
//		RsmAttribute rsm = crsmQuery.getResource(server_id);
//		logger.info("单个查询，当前承载信息=========="+rsm);
//		if(rsm!=null){
//			long online_num = rsm.getMaxnum()-rsm.getFreenum();
//			rsmDto.put("online_num", online_num);
//			rsmDto.put("max_num", rsm.getMaxnum());
//			rsmDto.put("liuhua_num", rsm.getStreamnum());
//			rsmDto.put("rate", rsm.getRate());
//			rsmDto.put("security", rsm.getSecurity());
//			String vstatus = rsm.getStatus();
//			if(vstatus=="unused"||"unused".equals(vstatus)){
//				Map<String, RsmResource> map = crsmQuery.getUserResourcesByRsm(server_id);
//				logger.info("单个查询，当前承载没有用户在用："+map.isEmpty());
//				if(!map.isEmpty()){
//					vstatus="perunused";
//				}
//			}
//			logger.info("单个查询，承载当前状态："+vstatus);
//			rsmDto.put("vstatus", vstatus);
//		}else{
//			String vstatus = "perunused";
//			Map<String, RsmResource> map = crsmQuery.getUserResourcesByRsm(server_id);
//			logger.info("承载信息为空，查询当前承载没有用户在用："+map.isEmpty());
//			if(map.isEmpty()){
//				vstatus="unused";
//			}
//			rsmDto.put("vstatus", vstatus);
//		}
		detailDto.put("server_ip", inDto.getAsString("server_ip"));
		detailDto.put("server_port", inDto.getAsString("server_port"));
		Dto outDto = new BaseDto();
		int i =crsmDao.update("CrsmServerMonitor.updateServerMoreInfo",detailDto);
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
			// Client client = new TcpClient();
		
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
					List sessionlist = querySession(server_ips[i]);
					logger.info("预下线，当前承载没有用户在用："+sessionlist.isEmpty());
					if(sessionlist.isEmpty()){
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

	public void selectListAndInsertDB(Dto inDto) {
		try {
			Dto queryDto = new BaseDto();
			queryDto.put("cmd", "vnclist");
			queryDto.put("authname", authname);
			queryDto.put("authcode", authcode);
			queryDto.put("serialno", serialnoUtil.getRandomString(16));
			String queryString = JsonHelper.encodeObject2Json(queryDto)+"XXEE";
			String revStr = client.sendStr(ip, port, 4000, null, queryString, codecFactory,null);
//			String revStr="{\"cmd\":\"vnclist\",\"retcode\":\"0\",\"vncpool\":[{\"vncid\":\"192.168.100.31:25000\",\"status\":\"unuser\",\"videotype\":\"HD\",\"vnctype\":\"PORTAL\",\"vendor\":\"BLUELINK\",\"rate\":\"4097\",\"vncip\":\"192.168.100.31\",\"vncport\":\"25000\",\"mgmtport\":\"25001\",\"totalnum\":\"100\",\"freenum\":\"89\",\"streamnum\":\"0\"},{\"vncid\":\"192.168.100.32:25000\",\"status\":\"register\",\"videotype\":\"HD\",\"vnctype\":\"PORTAL\",\"vendor\":\"BLUELINK\",\"rate\":\"4096\",\"vncip\":\"192.168.100.32\",\"vncport\":\"25000\",\"mgmtport\":\"25001\",\"totalnum\":\"100\",\"freenum\":\"100\",\"streamnum\":\"0\"}],\"serialno\":\"e22aff4bb8da4ed991d9f7c043614d68\"}XXEE";
			Dto dto = null;
			List insertList = new ArrayList();//批量插入List
			List updateList = new ArrayList();//批量插入List
			if(revStr!=null&&!"".equals(revStr)){
				revStr = revStr.replace("XXEE", "");
				Dto revDto = JsonHelper.parseSingleJson2Dto(revStr);
				if("0".equals(revDto.getAsString("retcode"))){
					List vncPool = revDto.getAsList("vncpool");
					for (Object object : vncPool) {
						Dto vncPoolDto = JsonHelper.parseSingleJson2Dto(JsonHelper.encodeObject2Json(object));
						String vncid = vncPoolDto.getAsString("vncid");
						String vstatus = vncPoolDto.getAsString("status");
						if(vstatus=="unused"||"unused".equals(vstatus)){
							List sessionList = querySession(vncid);
							logger.info("全量同步，承载"+vncid+"没有用户在线："+sessionList.isEmpty());
							if(!sessionList.isEmpty())
								vstatus="perunused";
						}
						long maxnum = vncPoolDto.getAsInteger("totalnum");
						long freenum = vncPoolDto.getAsInteger("freenum");
						long streamnum = vncPoolDto.getAsInteger("streamnum");
						long online_num = maxnum-freenum;
						logger.info("全量同步，"+vncid+"状态为:"+vstatus);
						dto = new BaseDto();
						dto.put("vstatus",vstatus);
						dto.put("online_num",online_num );
						dto.put("max_num", maxnum);
						dto.put("liuhua_num", streamnum);
						dto.put("rate", vncPoolDto.getAsString("rate"));//码率
//						dto.put("security", vncPoolDto.getAsString(""));//安全等级
						dto.put("vendor", vncPoolDto.getAsString("vendor"));
						dto.put("vtype", vncPoolDto.getAsString("videotype"));
						dto.put("server_ip", vncPoolDto.getAsString("vncip"));
						dto.put("server_port", vncPoolDto.getAsString("vncport"));
						dto.put("vability", vncPoolDto.getAsString("vnctype"));
						logger.info("定时同步RSM资源到数据库："+JsonHelper.encodeObject2Json(dto));
						
						Integer sum =(Integer) crsmDao.queryForObject("CrsmServerMonitor.queryServerCountByID", dto);;//查看当前承载是否已在数据库
						if(sum==0){
							insertList.add(dto);
						}else{
							updateList.add(dto);
						}
					}
					this.updateServerStatus(inDto);
					Thread.sleep(100);
					this.saveServerMore(insertList);
					Thread.sleep(100);
					this.updateServerMore(updateList);
				}
			}
		} catch (Exception e) {
			logger.equals(e.getMessage());
		}
		
	}
	
	
	public List querySession(String vncid){
		Dto querySessionDto = new BaseDto();
		querySessionDto.put("cmd", "vncresources");
		querySessionDto.put("authname", authname);
		querySessionDto.put("authcode", authcode);
		querySessionDto.put("vncid", vncid);
		querySessionDto.put("serialno", serialnoUtil.getRandomString(16));
		String queryStr = JsonHelper.encodeObject2Json(querySessionDto)+"XXEE";
		String queryRet = client.sendStr(ip, port, 4000, null, queryStr, codecFactory, null);
//		String queryRet ="{\"cmd\":\"vncresources\",\"retcode\":\"0\",\"respool\":[{\"resid\":\"4750437d175d40a38eba6535bd0ddfbe\",\"sessionid\":\"1234567\",\"sid\":\"1001\",\"operid\":\"0\",\"videotype\":\"HD\",\"sip\":\"192.168.70.249\",\"sport\":\"6000\",\"vncip\":\"192.168.70.249\",\"vncport\":\"6000\",\"keyport\":\"60002\",\"iip\":\"192.168.70.249\",\"iport\":\"10000\",\"rate\":\"4096\",\"url\":\"http://192.168.100.11:8882/NSCS/vod_jingdian.jsp?id=4\",\"iserror\":\"false\"},{\"resid\":\"4750437d175d40a38eba6535bd0ddfbb\",\"sessionid\":\"1234567\",\"sid\":\"1001\",\"operid\":\"0\",\"videotype\":\"HD\",\"sip\":\"192.168.70.249\",\"sport\":\"6000\",\"vncip\":\"192.168.70.249\",\"vncport\":\"6000\",\"keyport\":\"60002\",\"iip\":\"192.168.70.249\",\"iport\":\"10000\",\"rate\":\"4096\",\"url\":\"http://192.168.100.11:8882/NSCS/vod_jingdian.jsp?id=4\",\"iserror\":\"false\"}],\"serialno\":\"db974e9d8f424d2da87b246831442867\"}XXEE";
		if(queryRet!=null&&!"".equals(queryRet)){
			queryRet = queryRet.replace("XXEE", "");
			Dto queryRetDto = JsonHelper.parseSingleJson2Dto(queryRet);
			if("0".equals(queryRetDto.getAsString("retcode"))){
				return queryRetDto.getAsList("respool");
			}
		}
		return null;
	}
}
