package org.eredlab.g4.cabs.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eredlab.g4.bmf.base.BaseServiceImpl;
import org.eredlab.g4.cabs.service.BatchUpdateService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.Constants;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.TCPSocketCodec;
import org.eredlab.g4.common.util.client.Client;
import org.eredlab.g4.common.util.client.TcpClient;

public class BatchUpdateServiceImpl extends BaseServiceImpl implements BatchUpdateService{

	//批量升级
	public Dto batchUpdateItems(Dto pDto) {
		Dto dto = new BaseDto();
		Calendar calendar = Calendar.getInstance();
		Date createdate = calendar.getTime();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			Dto inDto = new BaseDto();
			inDto.put("vncm_id", arrChecked[i]);
			Dto outDto =(Dto) crsmDao.queryForObject("ServerMonitor.getServerByKey", inDto);
			Integer event_id = (Integer) crsmDao.queryForObject("UpdateStrategy.queryEventMaxIndex");
			if(event_id==null){
				event_id =0;
			}
			outDto.put("event_id", event_id+1);
			outDto.put("oper_status", 4);
			outDto.put("create_time", createdate);
			crsmDao.insert("UpdateStrategy.saveEvent", outDto);
			dto.put("success", new Boolean(true));
		}
		return dto;
	}
	
	
	//批量回滚
	public Dto batchRollBlockItems(Dto pDto) {
		Dto dto = new BaseDto();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			dto.put("term_record_id", arrChecked[i]);
			rollBlock(dto);
		}
		dto = new BaseDto("success",new Boolean(true));
		return dto;
	}
	
	//批量下线
	public Dto batchOfflineItems(Dto pDto) {
		Dto dto = new BaseDto();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			dto.put("term_record_id", arrChecked[i]);
			offLine(dto);
		}
		dto = new BaseDto("success",new Boolean(true));
		return dto;
	}
	

	public Dto batchStartItems(Dto pDto) {
		Dto dto = new BaseDto();
		Calendar cal=Calendar.getInstance(); 
		Date createdate=cal.getTime();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			Dto inDto = new BaseDto();
			inDto.put("vncm_id", arrChecked[i]);
			Dto outDto =(Dto) crsmDao.queryForObject("ServerMonitor.getServerByKey", inDto);
			Integer event_id = (Integer) crsmDao.queryForObject("UpdateStrategy.queryEventMaxIndex");
			if(event_id==null){
				event_id =0;
			}
			outDto.put("event_id", event_id+1);
			outDto.put("oper_status", 1);
			outDto.put("create_time", createdate);
			crsmDao.insert("UpdateStrategy.saveEvent", outDto);
			dto.put("success", new Boolean(true));
		}
		return dto;
	}


	public Dto batchStopItems(Dto pDto) {
		Dto dto = new BaseDto();
		Calendar calendar = Calendar.getInstance();
		Date createdate = calendar.getTime();
		String[] arrChecked = pDto.getAsString("strChecked").split(",");
		for (int i = 0; i < arrChecked.length; i++) {
			Dto inDto = new BaseDto();
			inDto.put("vncm_id", arrChecked[i]);
			Dto outDto =(Dto) crsmDao.queryForObject("ServerMonitor.getServerByKey", inDto);
			Integer event_id = (Integer) crsmDao.queryForObject("UpdateStrategy.queryEventMaxIndex");
			if(event_id==null){
				event_id =0;
			}
			outDto.put("event_id", event_id+1);
			outDto.put("oper_status", 2);
			outDto.put("create_time", createdate);
			crsmDao.insert("UpdateStrategy.saveEvent", outDto);
			dto.put("success", new Boolean(true));
			
		}
		return dto;
	}
	
	//单台回滚
	private Dto rollBlock(Dto pDto) {
		BaseDto outDto = (BaseDto) crsmDao.queryForObject(
				"BatchUpdate.getServerByKey", pDto);
		String ip = outDto.getAsString("term_ip");
		String strPort = outDto.getAsString("term_port");
		int port = Integer.parseInt(strPort);
		Map<String,Object> sendMessage = new HashMap<String,Object>();
		sendMessage.put("command", CommandSupport.COMMAND.PROGRAM_CTRL_REQ.toString());
		sendMessage.put(Constants.PCID,outDto.getAsString("term_id"));
		sendMessage.put(Constants.PCTYPE, outDto.getAsString("term_type"));
		sendMessage.put(Constants.OPERATORTYPE, "2");

		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		
		TCPSocketCodec codec = new TCPSocketCodec();
		String sendStr = codec.encoder(sendMessage);
		Client client = new TcpClient();
		String revStr = client.sendStr(ip, port, 1000, null, sendStr, codecFactory);
		if(!revStr.equals("")){
			if (revStr.indexOf(CommandSupport.HEADER) >= 0) {
				Map<String, Object> revMessage = codec.decoder(revStr);
					String retMsg = (String)revMessage.get(Constants.RETURNCODE);
			}
		}
		return null;
	}

	//单台下线
	private Dto offLine(Dto pDto) {
		BaseDto outDto = (BaseDto) crsmDao.queryForObject(
				"BatchUpdate.getServerByKey", pDto);
		String ip = outDto.getAsString("term_ip");
		String strPort = outDto.getAsString("term_port");
		int port = Integer.parseInt(strPort);
		Map<String,Object> sendMessage = new HashMap<String,Object>();
		sendMessage.put("command", CommandSupport.COMMAND.PROGRAM_CTRL_REQ.toString());
		sendMessage.put(Constants.PCID,outDto.getAsString("term_id"));
		sendMessage.put(Constants.PCTYPE, outDto.getAsString("term_type"));
		sendMessage.put(Constants.OPERATORTYPE, "3");

		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		
		TCPSocketCodec codec = new TCPSocketCodec();
		String sendStr = codec.encoder(sendMessage);
		Client client = new TcpClient();
		String revStr = client.sendStr(ip, port, 1000, null, sendStr, codecFactory);
		if(!revStr.equals("")){
			if (revStr.indexOf(CommandSupport.HEADER) >= 0) {
				Map<String, Object> revMessage = codec.decoder(revStr);
					String retMsg = (String)revMessage.get(Constants.RETURNCODE);
			}
		}
		return null;
	}
	
	//单台升级
	private Dto updateServer(Dto pDto) {
		pDto.put("status", 4);
		Calendar cal=Calendar.getInstance();
		Date updatedate=cal.getTime();
		pDto.put("update_date",updatedate);
		crsmDao.update("BatchUpdate.updateServerItem", pDto);
		return null;
	}
	//单台启动
	private Dto startServer(Dto pDto) {
		Dto outputDto = new BaseDto();
		BaseDto outDto = (BaseDto) crsmDao.queryForObject(
				"BatchUpdate.getServerByKey", pDto);
		String ip = outDto.getAsString("term_ip");
		String strPort = outDto.getAsString("term_port");
		int port = Integer.parseInt(strPort);
		Map<String,Object> sendMessage = new HashMap<String,Object>();
		sendMessage.put("command", CommandSupport.COMMAND.PROGRAM_CTRL_REQ.toString());
		sendMessage.put(Constants.PCID,outDto.getAsString("term_id"));
		sendMessage.put(Constants.PCTYPE, outDto.getAsString("term_type"));
		sendMessage.put(Constants.OPERATORTYPE, "0");

		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		
		TCPSocketCodec codec = new TCPSocketCodec();
		String sendStr = codec.encoder(sendMessage);
		Client client = new TcpClient();
		String revStr = client.sendStr(ip, port, 5000, null, sendStr, codecFactory);
		System.out.println("==============="+revStr);
		if(revStr==null||"".equals(revStr)){									//无返回
			outputDto.put("success", new Boolean(false));
		}else{
			if (revStr.indexOf(CommandSupport.HEADER) >= 0) {
				Map<String, Object> revMessage = codec.decoder(revStr);
					String retMsg = (String)revMessage.get(Constants.RETURNCODE);
					int retcode = Integer.valueOf(retMsg);
					System.out.println("======================================="+retMsg);
					if(retcode>=0){												//返回成功
						outputDto.put("success", new Boolean(true));
					}else{														//返回失败
						outputDto.put("success", new Boolean(false));
					}
			}else{
				outputDto.put("success", new Boolean(false));					//返回报文不对
			}
		}
		return outputDto;
	
	}

	//单台停止
	public Dto stopServer(Dto pDto) {
		Dto outputDto = new BaseDto();
		BaseDto outDto = (BaseDto) crsmDao.queryForObject(
				"BatchUpdate.getServerByKey", pDto);
		String ip = outDto.getAsString("term_ip");
		String strPort = outDto.getAsString("term_port");
		int port = Integer.parseInt(strPort);
		Map<String,Object> sendMessage = new HashMap<String,Object>();
		sendMessage.put("command", CommandSupport.COMMAND.PROGRAM_CTRL_REQ.toString());
		sendMessage.put(Constants.PCID,outDto.getAsString("term_id"));
		sendMessage.put(Constants.PCTYPE, outDto.getAsString("term_type"));
		sendMessage.put(Constants.OPERATORTYPE, "1");

		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		
		TCPSocketCodec codec = new TCPSocketCodec();
		String sendStr = codec.encoder(sendMessage);
		Client client = new TcpClient();
		String revStr = client.sendStr(ip, port, 5000, null, sendStr, codecFactory);
		if(revStr==null||"".equals(revStr)){									//无返回
			outputDto.put("success", new Boolean(false));
		}else{
			if (revStr.indexOf(CommandSupport.HEADER) >= 0) {
				Map<String, Object> revMessage = codec.decoder(revStr);
					String retMsg = (String)revMessage.get(Constants.RETURNCODE);
					int retcode = Integer.valueOf(retMsg);
					System.out.println("======================================="+retMsg);
					if(retcode>=0){												//返回成功
						outputDto.put("success", new Boolean(true));
					}else{														//返回失败
						outputDto.put("success", new Boolean(false));
					}
			}else{
				outputDto.put("success", new Boolean(false));					//返回报文不对
			}
		}		
		
		return outputDto;
	}


	public Dto updateLocation(Dto pDto) {
		Dto outDto = new BaseDto();
		Calendar cal=Calendar.getInstance();
		Date updatedate=cal.getTime();
		pDto.put("update_date",updatedate);
		crsmDao.update("BatchUpdate.updateLocationItem", pDto);
		outDto.put("success", new Boolean(true));
		return outDto;
	}
}
