package prod.nebula.mcs.module.dto;

public class TaskModel {
	//切换方式
	public String mode =""; 	//切换方式A-B (A流切换到B流)
								//A-B (A流切换到B流)
								//A-F-B(A流到广告片再切换到B流)
								//A-F-B(A流到广告片再切换到B流)
								//A-AB-B(A流带卷帘效果切换到B流)
								//A-AF-B(A流带A与广告片卷帘效果切换到B流) 
	public String hi_delay =""; //切换后是否高延时输出:yes,no
	public String idle_time ="";	//切换时断流时间 单位ms
	//切换目标流相关信息
	public String new_input ="";	//需要切换到的B流信息
	public String new_input_url ="";	//输入流的url为udp的单组播地址，如udp://225.14.14.168:5501
	public String new_input_ip ="";	//本机接收需要绑定的IP地址
	//过度广告相关信息
	public String file ="";			//广告文件名称（当前只能是CBR的TS文件）
	public String sys_bitrate ="";	//广告文件的系统码率 单位bps（此字段需要准确）
	//卷帘中间流相关信息
	public String roll_input_url ="";	//输出流的url为udp的单组播地址，如udp://225.14.14.168:5501
	
	public int taskId;					//任务ID
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getHi_delay() {
		return hi_delay;
	}
	public void setHi_delay(String hi_delay) {
		this.hi_delay = hi_delay;
	}
	public String getIdle_time() {
		return idle_time;
	}
	public void setIdle_time(String idle_time) {
		this.idle_time = idle_time;
	}
	public String getNew_input() {
		return new_input;
	}
	public void setNew_input(String new_input) {
		this.new_input = new_input;
	}
	public String getNew_input_url() {
		return new_input_url;
	}
	public void setNew_input_url(String new_input_url) {
		this.new_input_url = new_input_url;
	}
	public String getNew_input_ip() {
		return new_input_ip;
	}
	public void setNew_input_ip(String new_input_ip) {
		this.new_input_ip = new_input_ip;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getSys_bitrate() {
		return sys_bitrate;
	}
	public void setSys_bitrate(String sys_bitrate) {
		this.sys_bitrate = sys_bitrate;
	}
	public String getRoll_input_url() {
		return roll_input_url;
	}
	public void setRoll_input_url(String roll_input_url) {
		this.roll_input_url = roll_input_url;
	}
	public String getRoll_input_ip() {
		return roll_input_ip;
	}
	public void setRoll_input_ip(String roll_input_ip) {
		this.roll_input_ip = roll_input_ip;
	}
	public String roll_input_ip ="";	//本机输出需要绑定的IP地址
}
