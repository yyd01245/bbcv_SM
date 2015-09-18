package prod.nebula.mcs.module.dto;

import java.io.Serializable;

public class Task implements Serializable {
	//任务信息节点
	public int id=0;					//任务标识
	public String hi_delay ="";			//高低延时输出：yes，no
	public String execute ="";			//任务添加后是否执行产生输出：yes，no
	//输入流信息节点
		//参数内容节点
	public String inputUrl ="";			//输入流的url为udp的单组播地址，如udp://225.14.14.168:5501
	public String inputLocal_ip ="";	//本机需要绑定的IP地址
	//任务点播文件节点
		//参数内容节点
	public String file ="";				//点播文件名称（当前只能为CBR的TS流文件）
	public String sys_bitrate ="";		//点播文件的系统码率 单位bps（此字段需要准确）
	//任务输出流信息节点
		//参数内容节点
	public String outoutUrl ="";		//输出流的url为udp的单组播地址，如udp://225.14.14.168:5501
	public String outputLocal_ip ="";	//本机需要绑定的IP地址
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHi_delay() {
		return hi_delay;
	}
	public void setHi_delay(String hi_delay) {
		this.hi_delay = hi_delay;
	}
	public String getExecute() {
		return execute;
	}
	public void setExecute(String execute) {
		this.execute = execute;
	}
	public String getInputUrl() {
		return inputUrl;
	}
	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}
	public String getInputLocal_ip() {
		return inputLocal_ip;
	}
	public void setInputLocal_ip(String inputLocal_ip) {
		this.inputLocal_ip = inputLocal_ip;
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
	public String getOutoutUrl() {
		return outoutUrl;
	}
	public void setOutoutUrl(String outoutUrl) {
		this.outoutUrl = outoutUrl;
	}
	public String getOutputLocal_ip() {
		return outputLocal_ip;
	}
	public void setOutputLocal_ip(String outputLocal_ip) {
		this.outputLocal_ip = outputLocal_ip;
	}
	
	
}
