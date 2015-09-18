package prod.nebula.mcs.module.handle;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.mcs.core.CoreLoader;
import prod.nebula.mcs.core.common.ResConstants;
import prod.nebula.mcs.module.dto.TaskModel;
import prod.nebula.mcs.util.ApplicationContextHelper;

public class TaskExcuteThread implements Runnable {
	
	public final Logger logger = LoggerFactory.getLogger(getClass());
	public static CoreLoader core = (CoreLoader) ApplicationContextHelper.getBean(ResConstants.CORE_MODULE_NAME);
	public String taskId;
	public String taskInfo;
	
	private Random random = new Random();
	private MsiAgentCtrlImpl ctrl;
	
	public MsiAgentCtrlImpl getCtrl() {
		return ctrl;
	}

	public void setCtrl(MsiAgentCtrlImpl ctrl) {
		this.ctrl = ctrl;
	}
	public TaskExcuteThread(String taskId) {
		this.taskId = taskId;
	}
	
	@Override
	public void run() {
//		logger.info("任务执行后台任务开启。。。。。。。");
		logger.info(taskId);
		while(true){
//			if(taskInfo!=null&&!"".equals(taskInfo)){	//指定任务切换任务存在，执行任务切换
//				if(core.getCscsmdbInterface().getValue("excuteOK_"+taskId)=="true"||"true".equals(core.getCscsmdbInterface().getValue("excuteOK_"+taskId))){
//					String input_ip;
//					if((taskInfo.indexOf("192.168")>0)||(taskInfo.indexOf("21.200")>0)){//判断切换输入源为单播还是组播
//						input_ip = core.getProperties().getProperty("matrix.tcpserver.ip");
//					}else{
//						input_ip = core.getProperties().getProperty("matrix.zubo.tcpserver.ip");
//					}
//					TaskModel taskModel = new TaskModel();
//					taskModel.setMode("A-B");
//					taskModel.setIdle_time("0");
//					taskModel.setNew_input_url(taskInfo);
//					taskModel.setNew_input_ip(input_ip);
//					core.getCscsmdbInterface().addValue("excuteOK_"+taskId,"false");
//					ctrl.ExcuteTask(random.nextInt(900)+100, Integer.valueOf(taskId), taskModel,null);
//					if(taskInfo.equals(core.getCscsmdbInterface().getValue("currentTask_"+taskId))||taskInfo==core.getCscsmdbInterface().getValue("currentTask_"+taskId)){//如果当前执行任务与队列中任务一致，删除队列
//						core.getCscsmdbInterface().delValue("currentTask_"+taskId);
//						logger.info("任务队列中只有当前任务，删除任务队列。。。");
//					}
//				}
//			}
			if(core.getCscsmdbInterface().getValue("excuteOK_"+taskId)=="true"||"true".equals(core.getCscsmdbInterface().getValue("excuteOK_"+taskId))){
				String taskInfo = core.getCscsmdbInterface().getValue("currentTask_"+taskId);		//指定任务切换任务列表；
				if(taskInfo!=null&&!"".equals(taskInfo)){
					core.getCscsmdbInterface().addValue("excuteOK_"+taskId,"false");
					String input_ip;
					if((taskInfo.indexOf("192.168")>0)||(taskInfo.indexOf("21.200")>0)){//判断切换输入源为单播还是组播
						input_ip = core.getProperties().getProperty("matrix.tcpserver.ip");
					}else{
						input_ip = core.getProperties().getProperty("matrix.zubo.tcpserver.ip");
					}
					TaskModel taskModel = new TaskModel();
					taskModel.setMode("A-B");
					taskModel.setIdle_time("0");
					taskModel.setNew_input_url(taskInfo);
					taskModel.setNew_input_ip(input_ip);
					ctrl.ExcuteTask(random.nextInt(900)+100, Integer.valueOf(taskId), taskModel,null);
					if(taskInfo.equals(core.getCscsmdbInterface().getValue("currentTask_"+taskId))||taskInfo==core.getCscsmdbInterface().getValue("currentTask_"+taskId)){//如果当前执行任务与队列中任务一致，删除队列
						core.getCscsmdbInterface().delValue("currentTask_"+taskId);
						logger.info("任务队列中只有当前任务，删除任务队列。。。");
					}
				}
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
