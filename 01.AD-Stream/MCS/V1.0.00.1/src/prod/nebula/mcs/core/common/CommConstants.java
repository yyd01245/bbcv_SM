package prod.nebula.mcs.core.common;

public class CommConstants {
	//**成功*//*
	public final static  int SUCCESS = 0;
	public final static  String SUCCESS_MSG = "SUCCESS";
	
	//**内部处理异常*//*
	public final static int INTERNAL_ERROR = -1000;
	public final static String INTERNAL_ERROR_MSG = "INTERNAL_ERROR";
	public final static String REDIS_MODULE_NAME = "redis";
	
	//错误码
	
	//任务切换mode错误
	public static String SWITCH_TASK_ERROR = "-1811";
	public static String SWITCH_TASK_ERROR_MSG = "任务切换mode错误";
	public static String RECEIVE_MESSAGE_NULL ="-1800";
	public static String RECEIVE_MESSAGE_NULL_MSG ="接收矩阵设备消息为空或连接超时";
	
	public static String TASK_EXCUTE_NOTOK ="-1802";
	public static String TASK_EXCUTE_NOTOK_MSG ="当前任务前一条任务执行还未完成";
	
	public static String TASK_INPUT_ERROR ="-1803";
	public static String TASK_INPUT_ERROR_MSG ="输入源为包含null，或本身为空";
	
	//程序日志头
	public final static String HEAD ="【MSIAGENT】";
	
}
