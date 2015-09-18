package prod.nebula.mcs.module.handle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.framework.mina.command.json.CommandResult;
import prod.nebula.framework.mina.command.json.JsonCommandHandler;
import prod.nebula.mcs.core.CoreLoader;
import prod.nebula.mcs.core.common.CommConstants;
import prod.nebula.mcs.core.common.Constants;
import prod.nebula.mcs.core.common.ResConstants;
import prod.nebula.mcs.core.uti.client.TcpClient;
import prod.nebula.mcs.module.dto.Task;
import prod.nebula.mcs.module.dto.TaskModel;
import prod.nebula.mcs.module.enums.CommandSupport;
import prod.nebula.mcs.module.exception.CtrlModuleException;
import prod.nebula.mcs.util.ApplicationContextHelper;


public class TcpPersistentHandler implements JsonCommandHandler {

	private static final long serialVersionUID = 1L;
	public final Logger logger = LoggerFactory.getLogger(getClass());
	public CoreLoader core =(CoreLoader) ApplicationContextHelper.getBean(ResConstants.CORE_MODULE_NAME);
	private MsiAgentCtrlImpl ctrl;
	private Random random = new Random();
	List list = new ArrayList();
	public MsiAgentCtrlImpl getCtrl() {
		return ctrl;
	}

	public void setCtrl(MsiAgentCtrlImpl ctrl) {
		this.ctrl = ctrl;
	}

	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		try {
			if (session != null) {
				String ip = session.getRemoteAddress().toString();
				ip = ip.replace("/", "");
				logger.error(ip + "连接异常断开");
			} else {
				logger.error("iosession异常：" + cause);
			}
		} catch (Exception e) {

		}
	}

	public void sessionClosed(IoSession session) throws Exception {
		session.close(true);
	}

	public void messageSent(IoSession session, Object object) throws Exception {
		session.close(true);
		logger.info("mina mtp client message = " + object.toString());
	}






	public static String getSerialNo() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
	
	public String getAction(String message) throws CtrlModuleException {
		String ret_mess = null;
		if (message.indexOf(Constants.COM_PREFIX) >= 0) {
			message = message.substring(Constants.COM_PREFIX.length());
			String[] revArr = message.split("\\|");
			ret_mess = revArr[0];
		}
		return ret_mess;
	}

	@Override
	public CommandResult handle(JSONObject message) {
		logger.info("receive message=" + message);
		String revStr = message.toString();
		JSONObject json = JSONObject.fromObject(revStr);
		String returnMessage ="";
		long startTime = System.currentTimeMillis();
		String action = json.getString("cmd").toString();
		CommandSupport.MSIAGENTCOMMAND cc = CommandSupport.MSIAGENTCOMMAND.valueOf(action);
		switch (cc) {
		case cloud_switch_task://云门户登录创建任务
			Task cloudtask = new Task();
			cloudtask.setId(random.nextInt(900)+100);
			cloudtask.setHi_delay("no");
			cloudtask.setExecute("yes");
			cloudtask.setInputLocal_ip(core.getProperties().getProperty("matrix.tcpserver.ip"));
			cloudtask.setInputUrl(json.getString("input_url"));
			cloudtask.setFile("");
			cloudtask.setSys_bitrate("");
			cloudtask.setOutoutUrl(json.getString("output_url"));
			cloudtask.setOutputLocal_ip(core.getProperties().getProperty("matrix.tcpserver.ip"));
			returnMessage = ctrl.AddCloudTask(random.nextInt(900)+100, cloudtask, json);
			core.getCscsmdbInterface().addValue("excuteOK_"+cloudtask.getId(), "true");	//任务是否执行完成
			TaskExcuteThread taskThread = new TaskExcuteThread(String.valueOf(cloudtask.getId()));
			new Thread(taskThread).start();
			break;
		case get_all_task_info:
			returnMessage = ctrl.getAllTask(random.nextInt(900)+100,message);
			break;
		case get_dev_netcard_info:
			returnMessage = ctrl.getNetCardMap(random.nextInt(900)+100,message);
			break;
		case add_switch_task:
			Task task = new Task();
			task.setId(random.nextInt(900)+100);
			task.setHi_delay(json.getString("hi_delay"));
			task.setExecute(json.getString("execute"));
			task.setInputLocal_ip(core.getProperties().getProperty("matrix.tcpserver.ip"));
			task.setInputUrl(json.getString("input_url"));
			task.setFile(json.getString("file"));
			task.setSys_bitrate(json.getString("sys_bitrate"));
			task.setOutputLocal_ip(core.getProperties().getProperty("matrix.tcpserver.ip"));
			task.setOutoutUrl(json.getString("output_url"));
			returnMessage = ctrl.AddTask(random.nextInt(900)+100, task,message);
			core.getCscsmdbInterface().addValue("excuteOK_"+task.getId(), "true");//任务是否执行完成
			core.getCscsmdbInterface().addValue("currentTask_"+task.getId(),"");
			break;
		case execute_switch:
			int taskid  = Integer.valueOf(json.getString("taskID"));
			String input_url = json.getString("newInput_url");			//任务输入源
			if(input_url==""||"".equals(input_url)||input_url.indexOf("null")>=0){
				//输入源有问题
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.execute_switch.toString());
				jsonObject.put(Constants.RETURNCODE, CommConstants.SUCCESS);
				jsonObject.put(Constants.MSG, CommConstants.SUCCESS_MSG);
				jsonObject.put(Constants.SERIALNO, json.getString("serialno"));
				returnMessage = jsonObject.toString();
				logger.error("当前输入源无效，直接跳过。。。"+input_url);
			}else{
				core.getCscsmdbInterface().addValue("currentTask_"+taskid,input_url);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.COMMAND,CommandSupport.MSIAGENTCOMMAND.execute_switch.toString());
			jsonObject.put(Constants.RETURNCODE, CommConstants.SUCCESS);
			jsonObject.put(Constants.MSG, CommConstants.SUCCESS_MSG);
			jsonObject.put(Constants.SERIALNO, json.getString("serialno"));
			returnMessage = jsonObject.toString();
			break;
		case query_task_input:
			int taskID = json.getInt("taskID");
			returnMessage = ctrl.queryCurrentTask(random.nextInt(900)+100, taskID,message);
			break;
		case del_switch_task:
			int taskId = json.getInt("taskID");
			returnMessage = ctrl.DeleteTask(random.nextInt(900)+100, taskId,message);
			core.getCscsmdbInterface().delValue("currentTask_"+taskId);
			logger.info("CSCS对任务"+taskId+"进行删除，此处对任务队列进行清除");
			list.remove(new TaskExcuteThread(String.valueOf(taskId)));
			break;
		case reset_device:
			returnMessage = ctrl.ResetDevice(random.nextInt(900)+100, message);
			list.clear();
			break;
		case add_ads_stream:
			Task adstask = new Task();
			adstask.setId(random.nextInt(900)+100);
			adstask.setHi_delay("no");
			adstask.setExecute("yes");
			adstask.setInputLocal_ip(core.getProperties().getProperty("matrix.tcpserver.ip"));
			adstask.setInputUrl(json.getString("input_url"));
			adstask.setFile("");
			adstask.setSys_bitrate("");
			adstask.setOutputLocal_ip(core.getProperties().getProperty("matrix.tcpserver.ip"));
			adstask.setOutoutUrl(json.getString("output_url"));
			returnMessage = ctrl.AddAdsTask(random.nextInt(900)+100, adstask,message);
			break;
		case del_ads_stream:
			int task_id = json.getInt("task_id");
			returnMessage = ctrl.DeleteAdsTask(random.nextInt(900)+100, task_id,message);
			break;
		case check_session:
			int task_ads_id = json.getInt("task_id");
			returnMessage = ctrl.queryAdsTask(random.nextInt(900)+100, task_ads_id,message);
			break;
		}
		long endTime = System.currentTimeMillis();
		logger.info(CommConstants.HEAD +"-整体耗时:"+ ( endTime -startTime )+"ms" );
		return new CommandResult(returnMessage);
	}
}
