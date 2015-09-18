package prod.nebula.mcs.module.handle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;

import prod.nebula.mcs.core.CoreLoader;
import prod.nebula.mcs.core.common.CommConstants;
import prod.nebula.mcs.core.common.Constants;
import prod.nebula.mcs.core.common.ResConstants;
import prod.nebula.mcs.core.uti.client.IOSocketClient;
import prod.nebula.mcs.core.uti.client.MinaTextLineCodecFactory;
import prod.nebula.mcs.module.dao.CreateMessage;
import prod.nebula.mcs.module.dto.Netcard;
import prod.nebula.mcs.module.dto.Task;
import prod.nebula.mcs.module.dto.TaskModel;
import prod.nebula.mcs.module.enums.CommandSupport;
import prod.nebula.mcs.module.exception.CtrlModuleException;
import prod.nebula.mcs.util.ApplicationContextHelper;


public class MsiAgentCtrlImpl extends AbstarctMgwCtrl implements MsiAgentCtrl {
	public static Logger logger = Logger.getLogger(MsiAgentCtrlImpl.class);
	public static String ip =CoreLoader.getConfig().getMatrixDevicesIP();
	public static int port =CoreLoader.getConfig().getMatrixDevicesPort();
	public static CoreLoader core =(CoreLoader) ApplicationContextHelper.getBean(ResConstants.CORE_MODULE_NAME);
	public static CommunicationStandard ccs = new CommunicationStandard();
	public String getAction(String message) throws CtrlModuleException {
		String ret_mess = null;
		if (message.indexOf(Constants.COM_PREFIX) >= 0) {
			message = message.substring(Constants.COM_PREFIX.length());
			String[] revArr = message.split("\\|");
			ret_mess = revArr[0];
		}
		return ret_mess;
	}

	public Map<String, Object> decoder(String message, String template) {
		HashMap<String, Object> retObj = new HashMap<String, Object>();
		if (message.indexOf(Constants.COM_PREFIX) >= 0) {
			if (message.lastIndexOf(Constants.COM_SUFFIX) > 0) {
				message = message.substring(0,
						message.lastIndexOf(Constants.COM_SUFFIX));
				message = message.substring(Constants.COM_PREFIX.length());
			}
		}
		String[] revArr = message.split("\\|");
		String[] keyArr = template.split("\\|");
		for (int i = 0; i < revArr.length; i++) {
			retObj.put(keyArr[i], revArr[i]);
		}
		return retObj;
	}

	public String encoder(Map<String, Object> message, String template) {
		StringBuffer sb = new StringBuffer();
		String[] revArr = template.split("\\|");
		if (revArr.length > 0) {
			for (int i = 0; i < message.size(); i++) {
				sb.append(message.get(revArr[i]).toString() + "|");
			}
			int len = sb.length();
			if (len > 0) {
				sb.deleteCharAt(len - 1);
			}
		}
		return Constants.COM_PREFIX + sb.toString() + Constants.COM_SUFFIX;
	}
	
	
	//获取网卡信息
	
	public static String getNetCardMap(int id,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		String msg ="";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.get_dev_netcard_info.toString());
		String serialNo = getSerialNo(json);
		String message = CreateMessage.CreateQueryAllNetCard(id);
		try {
			byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
			IOSocketClient client = new IOSocketClient();
			String sendMsg =message;
			logger.info(CommConstants.HEAD+"发送查询全部网卡信息报文给矩阵设备："+sendMsg);
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
			String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
			if(retString!=null&&!"".equals(retString)){
				List list = new ArrayList();
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成xml
				List<Element> netcardList = (List<Element>) doc.selectNodes("response/body/netcard");//获取报文中netcard节点
				if(netcardList.size()>0){
					for(Element netcardElement:netcardList){
						Netcard netcard = new Netcard();
						String name = netcardElement.attributeValue("name");
						String mac = netcardElement.attributeValue("mac");
						String connect = netcardElement.attributeValue("connect");
						netcard.setName(name);
						netcard.setMac(mac);
						netcard.setConnect(connect);
						Document ipv4Doc = reader.read(new ByteArrayInputStream(netcardElement.asXML().getBytes("UTF-8")));//生成ipv4节点xml
						List<Element> ipv4List = (List<Element>) ipv4Doc.selectNodes("netcard/ipv4");//获取报文中ipv4节点
						if(ipv4List.size()>0){
							for(Element ipv4Element:ipv4List){
								String ip = ipv4Element.attributeValue("ip");
								String mask = ipv4Element.attributeValue("mask");
								String gateway = ipv4Element.attributeValue("gateway");
								netcard.setIp(ip);
								netcard.setMask(mask);
								netcard.setGateway(gateway);
							}
						}
						list.add(JSONObject.fromObject(netcard).toString());
					}
					msg = list.toString();
				}
			}else{
				returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
				logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
			}
		} catch (IOException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} catch (DocumentException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} 
		jsonObject.put(Constants.RETURNCODE, returnCode);
		jsonObject.put(Constants.MSG, msg);
		jsonObject.put(Constants.SERIALNO, serialNo);
		return jsonObject.toString();
	}
	
	//获取所有任务
	
	public static String getAllTask(int id,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		String msg ="";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.get_all_task_info.toString());
		String serialNo = getSerialNo(json);
		String message = CreateMessage.CreateQueryAllTask(id);
		try {
			byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
			IOSocketClient client = new IOSocketClient();
			String sendMsg =message;
			logger.info(CommConstants.HEAD+"发送查询所有任务报文给矩阵设备："+sendMsg);
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
			String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
			if(retString!=null&&!"".equals(retString)){
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
				List<Element> taskList = (List<Element>) doc.selectNodes("response/body/task");//获取报文中task节点
				List list = new ArrayList();
				if(taskList.size()>0){
					for(Element element :taskList){
						Task task = new Task();
						//获取task节点下元素
						int task_id = Integer.valueOf(element.attributeValue("id"));
						String hi_delay = element.attributeValue("hi-delay");
						String execute = element.attributeValue("execute");
						task.setId(task_id);
						task.setHi_delay(hi_delay);
						task.setExecute(execute);
						Document docTask = reader.read(new ByteArrayInputStream(element.asXML().getBytes("UTF-8")));//取task节点下其他节点参数
						//输入流
						List<Element> inputList = (List<Element>) docTask.selectNodes("task/input/params");
						if(inputList.size()>0){
							for(Element elementInput:inputList){
								//获取input节点下参数
								String url = elementInput.attributeValue("url");
								String local_ip = elementInput.attributeValue("local-ip");
								task.setInputLocal_ip(local_ip);
								task.setInputUrl(url);
							}
						}
						//参数
						List<Element> backupList = (List<Element>) docTask.selectNodes("task/backup/params");
						if(backupList.size()>0){
							for(Element elementBackup:backupList){
								//获取backup节点下参数
								String file = elementBackup.attributeValue("file");
								String sys_bitrate = elementBackup.attributeValue("sys-bitrate");
								task.setFile(file);
								task.setSys_bitrate(sys_bitrate);
							}
						}
						//输出流
						List<Element> outputList = (List<Element>) docTask.selectNodes("task/output/params");
						if(outputList.size()>0){
							for(Element elementOutput:outputList){
								//获取output节点下参数
								String url = elementOutput.attributeValue("url");
								String local_ip = elementOutput.attributeValue("local-ip");
								task.setOutputLocal_ip(local_ip);
								task.setOutoutUrl(url);
							}
						}
						list.add(JSONObject.fromObject(task).toString());
					}
					msg = list.toString();
				}
			}else{
				returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
				logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
			}
		} catch (IOException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} catch (DocumentException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} 
		jsonObject.put(Constants.RETURNCODE, returnCode);
		jsonObject.put(Constants.MSG, msg);
		jsonObject.put(Constants.SERIALNO, serialNo);
		return jsonObject.toString();
	}
	/**
	 * 获取当前任务的流信息
	 * @param messageID
	 * @param taskID
	 * @return 
	 */
	public static String queryCurrentTask(int messageID, int taskID,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		String msg ="";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.query_task_input.toString());
		String serialNo = getSerialNo(json);
		String message = CreateMessage.CreateQueryCurrentTask(messageID, taskID);
		try {
			byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
			IOSocketClient client = new IOSocketClient();
			String sendMsg =message;
			logger.info(CommConstants.HEAD+"发送查询当前任务报文给矩阵设备："+sendMsg);
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
			String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
//			String retString = "<response id='201' cmd='query_task_input' ack='0' detail='OK'><target><task>200</task></target><body><input>udp://225.14.14.168:5501</input></body></response>";
			if(retString!=null&&!"".equals(retString)){
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
				List<Element> responseList = (List<Element>) doc.selectNodes("response");//获取报文中response节点
				if(responseList.size()>0){
					for (Element elementResponse:responseList) {
						String ack = elementResponse.attributeValue("ack");
						String detail = elementResponse.attributeValue("detail");
						if(ack=="0"||"0".equals(ack)){
							Document inputDoc = reader.read(new ByteArrayInputStream(elementResponse.asXML().getBytes("UTF-8")));
							List<Element> taskList = (List<Element>) doc.selectNodes("response/body/input");//获取报文中input节点
							if(taskList.size()>0){
								for(Element elementTask:taskList){
									logger.info(CommConstants.HEAD+"当前流信息："+elementTask.getText());
									msg=elementTask.getText();
								}
							}
						}else{
							returnCode ="-"+ack;
							logger.error(CommConstants.HEAD+"查询任务失败，错误信息："+ack+",错误描述："+detail);
						}
					}
				}
			}else{
				returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
				logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} catch (DocumentException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		}
		jsonObject.put(Constants.RETURNCODE, returnCode);
		jsonObject.put(Constants.MSG, msg);
		jsonObject.put(Constants.SERIALNO, serialNo);
		return jsonObject.toString();
	}
	/**
	 * 添加任务
	 * @param messageID
	 * @param task
	 * @return
	 */
	public static String AddDvbTask(int messageID,Task task,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.add_switch_task.toString());
		String serialNo = getSerialNo(json);
		String message = CreateMessage.CreateAddTaskMessage(messageID, task);
		try {
			byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
			IOSocketClient client = new IOSocketClient();
			String sendMsg =message;
			logger.info(CommConstants.HEAD+"发送添加DVB任务报文给矩阵设备："+sendMsg);
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
			String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
			if(retString!=null&&!"".equals(retString)){
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
				List<Element> responseList = (List<Element>) doc.selectNodes("response");//获取报文中response节点
				if(responseList.size()>0){
					for (Element elementResponse:responseList) {
						String ack = elementResponse.attributeValue("ack");
						String detail = elementResponse.attributeValue("detail");
						if(ack=="0"||"0".equals(ack)){
							logger.info(CommConstants.HEAD+"DVB任务添加成功！");
						}else{
							returnCode ="-"+ack;
							logger.error(CommConstants.HEAD+"DVB任务添加失败，错误信息："+ack+",错误描述："+detail);
						}
					}
				}
			}else{
				returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
				logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} catch (DocumentException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		}
		jsonObject.put("taskID", task.getId());
		jsonObject.put(Constants.RETURNCODE, returnCode);
		jsonObject.put(Constants.SERIALNO, serialNo);
		return jsonObject.toString();
	}
	
	/**
	 * 添加任务
	 * @param messageID
	 * @param task
	 * @return
	 */
	public static String AddTask(int messageID,Task task,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.add_switch_task.toString());
		String serialNo = getSerialNo(json);
		String message = CreateMessage.CreateAddTaskMessage(messageID, task);
		try {
			byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
			IOSocketClient client = new IOSocketClient();
			String sendMsg =message;
			logger.info(CommConstants.HEAD+"发送添加任务报文给矩阵设备："+sendMsg);
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
			String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
			if(retString!=null&&!"".equals(retString)){
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
				List<Element> responseList = (List<Element>) doc.selectNodes("response");//获取报文中response节点
				if(responseList.size()>0){
					for (Element elementResponse:responseList) {
						String ack = elementResponse.attributeValue("ack");
						String detail = elementResponse.attributeValue("detail");
						if(ack=="0"||"0".equals(ack)){
							logger.info(CommConstants.HEAD+"任务添加成功！");
						}else{
							returnCode ="-"+ack;
							logger.error(CommConstants.HEAD+"任务添加失败，错误信息："+ack+",错误描述："+detail);
						}
					}
				}
			}else{
				returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
				logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} catch (DocumentException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		}
		jsonObject.put("taskID", task.getId());
		jsonObject.put(Constants.RETURNCODE, returnCode);
		jsonObject.put(Constants.SERIALNO, serialNo);
		return jsonObject.toString();
	}
	
	/**
	 * 添加云任务
	 * @param messageID
	 * @param task
	 * @return
	 */
	public static String AddCloudTask(int messageID,Task task,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.cloud_switch_task.toString());
		String serialNo = getSerialNo(json);
		String message = CreateMessage.CreateAddTaskMessage(messageID, task);
		try {
			byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
			IOSocketClient client = new IOSocketClient();
			String sendMsg =message;
			logger.info(CommConstants.HEAD+"发送添加任务报文给矩阵设备："+sendMsg);
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
			String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
			if(retString!=null&&!"".equals(retString)){
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
				List<Element> responseList = (List<Element>) doc.selectNodes("response");//获取报文中response节点
				if(responseList.size()>0){
					for (Element elementResponse:responseList) {
						String ack = elementResponse.attributeValue("ack");
						String detail = elementResponse.attributeValue("detail");
						if(ack=="0"||"0".equals(ack)){
							logger.info(CommConstants.HEAD+"任务添加成功！");
						}else{
							returnCode ="-"+ack;
							logger.error(CommConstants.HEAD+"任务添加失败，错误信息："+ack+",错误描述："+detail);
						}
					}
				}
			}else{
				returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
				logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} catch (DocumentException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		}
		jsonObject.put("taskID", task.getId());
		jsonObject.put(Constants.RETURNCODE, returnCode);
		jsonObject.put(Constants.SERIALNO, serialNo);
		return jsonObject.toString();
	}
	/**
	 *切换任务
	 * @param messageID
	 * @param taskID
	 * @param taskModel
	 * @param json
	 * @return
	 */
	public static String ExcuteTask(int messageID, int taskID, TaskModel taskModel,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		String msg="";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.execute_switch.toString());
		String serialNo = getSerialNo(json);
		String message = CreateMessage.CreateSwitchTaskModelMessage(messageID, taskID, taskModel);
		if(message!=null&&!"".equals(message)){
			try {
				byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
				IOSocketClient client = new IOSocketClient();
				String sendMsg =message;
				logger.info(CommConstants.HEAD+"发送切换任务报文给矩阵设备："+sendMsg);
				MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
				String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
//				<response id='898' cmd='execute_switch' ack='2004' detail='same source, don't switch'><target><task>384</task></target><body></body></response>
				if(retString!=null&&!"".equals(retString)&&retString.indexOf("<response")>=0){
					logger.info("发送切换任务接收返回："+retString);
					core.getCscsmdbInterface().addValue("excuteOK_"+taskID, "true");
				}else if(retString!=null&&!"".equals(retString)&&retString.indexOf("<alert")>=0){
					logger.info(CommConstants.HEAD+"任务执行完成！");
					core.getCscsmdbInterface().addValue("excuteOK_"+taskID, "true");
					msg = "task excute ok!";
					returnCode="0";
				}else{
					returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
					logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
					core.getCscsmdbInterface().addValue("excuteOK_"+taskID, "true");
					msg = CommConstants.RECEIVE_MESSAGE_NULL_MSG;
				}
			} catch (Exception e) {
				logger.error(CommConstants.HEAD+e.getMessage());
			}
		}else{
			logger.error(CommConstants.HEAD+"【"+CommConstants.SWITCH_TASK_ERROR+"】"+CommConstants.SWITCH_TASK_ERROR_MSG);
		}
		jsonObject.put(Constants.RETURNCODE, returnCode);
		jsonObject.put(Constants.MSG, msg);
		jsonObject.put(Constants.SERIALNO, getSerialNo(json));
		return jsonObject.toString();
	}
	
	/**
	 * 删除任务
	 * @param messageID
	 * @param taskID
	 * @param taskModel
	 * @param json
	 * @return
	 */
	public static String DeleteTask(int messageID, int taskID,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.del_switch_task.toString());
		String serialNo = getSerialNo(json);
		String message = CreateMessage.CreateDeleteTaskMessage(messageID, taskID);
			try {
				byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
				IOSocketClient client = new IOSocketClient();
				String sendMsg =message;
				logger.info(CommConstants.HEAD+"发送删除任务报文给矩阵设备："+sendMsg);
				MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
				String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
				if(retString!=null&&!"".equals(retString)){
					SAXReader reader = new SAXReader();
					Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
					List<Element> responseList = (List<Element>) doc.selectNodes("response");//获取报文中response节点
					if(responseList.size()>0){
						for (Element elementResponse:responseList) {
							String ack = elementResponse.attributeValue("ack");
							String detail = elementResponse.attributeValue("detail");
							if(ack=="0"||"0".equals(ack)){
								logger.info(CommConstants.HEAD+"删除任务成功！");
							}else{
								returnCode ="-"+ack;
								logger.error(CommConstants.HEAD+"删除任务失败，错误信息："+ack+",错误描述："+detail);
							}
						}
					}
				}else{
					returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
					logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
				}
			} catch (UnsupportedEncodingException e) {
				logger.error(CommConstants.HEAD+e.getMessage());
			} catch (DocumentException e) {
				logger.error(CommConstants.HEAD+e.getMessage());
			}
		jsonObject.put(Constants.RETURNCODE, returnCode);
		jsonObject.put(Constants.SERIALNO, serialNo);
		return jsonObject.toString();
	}
	
	public static String ResetDevice(int messageID,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.reset_device.toString());
		String serialNo = getSerialNo(json);
		String message = CreateMessage.CreateResetDevice(messageID);
			try {
				byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
				IOSocketClient client = new IOSocketClient();
				String sendMsg =message;
				logger.info(CommConstants.HEAD+"发送设备初始化报文给矩阵设备："+sendMsg);
				MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
				String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
				if(retString!=null&&!"".equals(retString)){
					SAXReader reader = new SAXReader();
					Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
					List<Element> responseList = (List<Element>) doc.selectNodes("response");//获取报文中response节点
					if(responseList.size()>0){
						for (Element elementResponse:responseList) {
							String ack = elementResponse.attributeValue("ack");
							String detail = elementResponse.attributeValue("detail");
							if(ack=="0"||"0".equals(ack)){
								logger.info(CommConstants.HEAD+"设备初始化成功！");
							}else{
								returnCode ="-"+ack;
								logger.error(CommConstants.HEAD+"设备初始化失败，错误信息："+ack+",错误描述："+detail);
							}
						}
					}
				}else{
					returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
					logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
				}
			} catch (UnsupportedEncodingException e) {
				logger.error(CommConstants.HEAD+e.getMessage());
			} catch (DocumentException e) {
				logger.error(CommConstants.HEAD+e.getMessage());
			}
		jsonObject.put(Constants.RETURNCODE, returnCode);
		jsonObject.put(Constants.SERIALNO, serialNo);
		return jsonObject.toString();
	}
	
	/**
	 * 获取报文中的serialNo
	 * @param json
	 * @return
	 */
	public static String getSerialNo(JSONObject json) {
		UUID uuid = UUID.randomUUID();
		String serialNo = uuid.toString().replaceAll("-", "");
		return serialNo;
	}
	public static void main(String[] args) {
		MsiAgentCtrlImpl msi = new MsiAgentCtrlImpl();
		JSONObject json = new JSONObject();
		json.put(Constants.SERIALNO, "werwsd98989");
		System.out.println(msi.getAllTask(123, json));
	}

	public static String AddAdsTask(int messageID,Task task,JSONObject json) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.add_ads_stream.toString());
		String serialNo = json.getString("serialno");
		String sessionid = json.getString("sessionid");
		String message = CreateMessage.CreateAddTaskMessage(messageID, task);
		try {
			byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
			IOSocketClient client = new IOSocketClient();
			String sendMsg =message;
			logger.info(CommConstants.HEAD+"发送添加任务报文给矩阵设备："+sendMsg);
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
			String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
			if(retString!=null&&!"".equals(retString)){
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
				List<Element> responseList = (List<Element>) doc.selectNodes("response");//获取报文中response节点
				if(responseList.size()>0){
					for (Element elementResponse:responseList) {
						String ack = elementResponse.attributeValue("ack");
						String detail = elementResponse.attributeValue("detail");
						if(ack=="0"||"0".equals(ack)){
							logger.info(CommConstants.HEAD+"任务添加成功！");
						}else{
							returnCode ="-"+ack;
							logger.error(CommConstants.HEAD+"任务添加失败，错误信息："+ack+",错误描述："+detail);
						}
					}
				}
			}else{
				returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
				logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} catch (DocumentException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		}
		jsonObject.put("task_id", task.getId());
		jsonObject.put(Constants.RETCODE, returnCode);
		jsonObject.put(Constants.SERIALNO, serialNo);
		jsonObject.put("sessionid", sessionid);
		return jsonObject.toString();
	}

	public static String DeleteAdsTask(int messageID, int taskID,JSONObject json) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.del_ads_stream.toString());
		String serialNo = json.getString("serialno");
		String sessionid = json.getString("sessionid");
		String message = CreateMessage.CreateDeleteTaskMessage(messageID, taskID);
			try {
				byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
				IOSocketClient client = new IOSocketClient();
				String sendMsg =message;
				logger.info(CommConstants.HEAD+"发送删除任务报文给矩阵设备："+sendMsg);
				MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
				String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
				if(retString!=null&&!"".equals(retString)){
					SAXReader reader = new SAXReader();
					Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
					List<Element> responseList = (List<Element>) doc.selectNodes("response");//获取报文中response节点
					if(responseList.size()>0){
						for (Element elementResponse:responseList) {
							String ack = elementResponse.attributeValue("ack");
							String detail = elementResponse.attributeValue("detail");
							if(ack=="0"||"0".equals(ack)){
								logger.info(CommConstants.HEAD+"删除任务成功！");
							}else{
								returnCode ="-"+ack;
								logger.error(CommConstants.HEAD+"删除任务失败，错误信息："+ack+",错误描述："+detail);
							}
						}
					}
				}else{
					returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
					logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
				}
			} catch (UnsupportedEncodingException e) {
				logger.error(CommConstants.HEAD+e.getMessage());
			} catch (DocumentException e) {
				logger.error(CommConstants.HEAD+e.getMessage());
			}
		jsonObject.put(Constants.RETCODE, returnCode);
		jsonObject.put(Constants.SERIALNO, serialNo);
		jsonObject.put("sessionid", sessionid);
		return jsonObject.toString();
	}
	public static String queryAdsTask(int messageID, int taskID,JSONObject json){
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0";
		String msg ="";
		jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.check_session.toString());
		String serialNo = json.getString("serialno");
		String sessionid = json.getString("sessionid");
		String message = CreateMessage.CreateQueryCurrentTask(messageID, taskID);
		try {
			byte[] messageByte = ccs.initPacketHeader(message);//初始化报文头
			IOSocketClient client = new IOSocketClient();
			String sendMsg =message;
			logger.info(CommConstants.HEAD+"发送查询当前任务报文给矩阵设备："+sendMsg);
			MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory("UTF-8", "XXEE","XXEE");
			String retString = client.sendStr(ip, port, 5000, null,messageByte, sendMsg, codecFactory);
//			String retString = "<response id='201' cmd='query_task_input' ack='0' detail='OK'><target><task>200</task></target><body><input>udp://225.14.14.168:5501</input></body></response>";
			if(retString!=null&&!"".equals(retString)){
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new ByteArrayInputStream(retString.getBytes("UTF-8")));//根据返回报文生成document
				List<Element> responseList = (List<Element>) doc.selectNodes("response");//获取报文中response节点
				if(responseList.size()>0){
					for (Element elementResponse:responseList) {
						String ack = elementResponse.attributeValue("ack");
						String detail = elementResponse.attributeValue("detail");
						if(ack=="0"||"0".equals(ack)){
							Document inputDoc = reader.read(new ByteArrayInputStream(elementResponse.asXML().getBytes("UTF-8")));
							List<Element> taskList = (List<Element>) doc.selectNodes("response/body/input");//获取报文中input节点
							if(taskList.size()>0){
								for(Element elementTask:taskList){
									logger.info(CommConstants.HEAD+"当前流信息："+elementTask.getText());
									msg=elementTask.getText();
								}
							}
						}else{
							returnCode ="-"+ack;
							logger.error(CommConstants.HEAD+"查询任务失败，错误信息："+ack+",错误描述："+detail);
						}
					}
				}
			}else{
				returnCode = CommConstants.RECEIVE_MESSAGE_NULL;
				logger.error(CommConstants.HEAD+"【"+CommConstants.RECEIVE_MESSAGE_NULL+"】"+CommConstants.RECEIVE_MESSAGE_NULL_MSG);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		} catch (DocumentException e) {
			logger.error(CommConstants.HEAD+e.getMessage());
		}
		jsonObject.put(Constants.RETCODE, returnCode);
		jsonObject.put(Constants.SERIALNO, serialNo);
		jsonObject.put("sessionid", sessionid);
		jsonObject.put("stream_info", msg);
		return jsonObject.toString();
	}
}
