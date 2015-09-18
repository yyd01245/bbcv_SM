package prod.nebula.mcs.module.dao;

import org.apache.log4j.Logger;

import prod.nebula.mcs.core.common.CommConstants;
import prod.nebula.mcs.module.dto.Task;
import prod.nebula.mcs.module.dto.TaskModel;

public class CreateMessage {
	private static Logger logger = Logger.getLogger(CreateMessage.class);
	/**
	 * 组装查询所有网卡信息报文
	 * @return
	 */
	
	public static String CreateQueryAllNetCard(int id){
		String retString="<message id='"+id+"' cmd='get_dev_netcard_info'>"
			+"<target></target>"
			+"<body>"
			+"</body>"
			+"</message>";
		return retString;
	}
	
	/**
	 * 组装查询所有任务信息报文
	 * @return
	 */
	
	public static String CreateQueryAllTask(int id){
		String retString="<message id='"+id+"' cmd='get_all_task_info'>"
			+"<target></target>"
			+"<body>"
			+"</body>"
			+"</message>";
		return retString;
	}
	/**
	 * 组装添加任务信息报文
	 * @param messageID
	 * @param task
	 * @return
	 */
	public static String CreateAddTaskMessage(int messageID,Task task){
		String retString="<message id='"+messageID+"' cmd='add_switch_task'>"
			+"<target></target>"
			+"<body>"
			+"<task id='"+task.id+"' hi-delay='"+task.hi_delay+"' execute='"+task.execute+"'>"
			+"<input>"
			+"<params url='"+task.inputUrl+"' local-ip='"+task.inputLocal_ip+"'/>"
			+"</input>"
			+"<backup>"
			+"<params file='"+task.file+"' sys-bitrate='"+task.sys_bitrate+"'/>"
			+"</backup>"
			+"<output>"
			+"<params url='"+task.outoutUrl+"' local-ip='"+task.outputLocal_ip+"'/>"
			+"</output>"
			+"</task>"
			+"</body>"
			+"</message>";
		return retString;
	}
	/**
	 * 组装查询当前任务信息报文
	 * @param messageID
	 * @param taskID
	 * @return
	 */
	public static String CreateQueryCurrentTask(int messageID,int taskID){
		String retString ="<message id='"+messageID+"' cmd='query_task_input'>"
						+"<target><task>"+taskID+"</task></target>"
						+"<body/>"
						+"</message>";
		return retString;
	}
	/**
	 * 组装任务切换信息报文
	 * @param messageID
	 * @param taskID
	 * @param taskModel
	 * @return
	 */
	public static String CreateSwitchTaskModelMessage(int messageID,int taskID,TaskModel taskModel){
		String Messagehead ="<message id='"+messageID+"' cmd='execute_switch'>"
						+"<target><task>"+taskID+"</task></target>"
						+"<body>";
		String MessageEnd="</body></message>";
		String mode = taskModel.getMode();
		String switchBody ="";
		if(mode=="A-B"||"A-B".equals(mode)){// (A流切换到B流)
			switchBody="<switch mode='A-B' hi-delay='no' idle-time='"+taskModel.idle_time+"' >"
			+"<new-input>"
			+"<params url='"+taskModel.new_input_url+"' local-ip='"+taskModel.new_input_ip+"'/>"
			+"</new-input>"
			+"</switch>";
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(Messagehead).append(switchBody).append(MessageEnd);
			return stringBuffer.toString();
		}else if(mode=="A-F-B"||"A-F-B".equals(mode)){//(A流到广告片再切换到B流)
			switchBody="<switch mode='A-F-B' hi-delay='"+taskModel.hi_delay+"' idle-time='"+taskModel.idle_time+"' >"
			+"<new-input>"
			+"<params url='"+taskModel.new_input_url+"' local-ip='"+taskModel.new_input_ip+"'/>"
			+"</new-input>"
			+"<ads>"
			+"<params file='"+taskModel.file+"' sys-bitrate='"+taskModel.sys_bitrate+"'/>"
			+"</ads>"
			+"</switch>";
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(Messagehead).append(switchBody).append(MessageEnd);
			return stringBuffer.toString();
			
		}else if(mode=="A-AB-B"||"A-AB-B".equals(mode)){//(A流带卷帘效果切换到B流)
			switchBody="<switch mode='A-AB-B' hi-delay='"+taskModel.hi_delay+"' idle-time='"+taskModel.idle_time+"' >"
			+"<new-input>"
			+"<params url='"+taskModel.new_input_url+"' local-ip='"+taskModel.new_input_ip+"'/>"
			+"</new-input>"
			+"<roll-input>"
			+"<params url='"+taskModel.roll_input_url+"' local-ip='"+taskModel.roll_input_ip+"'/>"
			+"</roll-input>"
			+"</switch>";
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(Messagehead).append(switchBody).append(MessageEnd);
			return stringBuffer.toString();
		}else if(mode=="A-AF-B"||"A-AF-B".equals(mode)){//(A流带A与广告片卷帘效果切换到B流)
			switchBody="<switch mode='A-AF-B' hi-delay='"+taskModel.hi_delay+"' idle-time='"+taskModel.idle_time+"' >"
			+"<new-input>"
			+"<params url='"+taskModel.new_input_url+"' local-ip='"+taskModel.new_input_ip+"'/>"
			+"</new-input>"
			+"<roll-input>"
			+"<params url='"+taskModel.roll_input_url+"' local-ip='"+taskModel.roll_input_ip+"'/>"
			+"</roll-input>"
			+"</switch>";
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(Messagehead).append(switchBody).append(MessageEnd);
			return stringBuffer.toString();
		}else{
			logger.error(CommConstants.HEAD+"任务切换方式"+mode+"不在合法范围内，请检查！");
		}
		return null;
	}
	/**
	 * 组装删除任务信息报文
	 * @param messageID
	 * @param taskID
	 * @return
	 */
	public static String CreateDeleteTaskMessage(int messageID,int taskID){
		String retString="<message id='"+messageID+"' cmd='del_switch_task'>"
			+"<target><task>"+taskID+"</task></target>"
			+"<body/>"
			+"</message>";
		return retString;
	}
	/**
	 * 设备初始化报文组装
	 * @param messageID
	 * @return
	 */
	public static String CreateResetDevice(int messageID){
		String message = "<message id='"+messageID+"' cmd='reset-device'>"
			+"<target></target>"
			+"<body>"
			+"</body>"
			+"</message>";
		return message;
	}
}
