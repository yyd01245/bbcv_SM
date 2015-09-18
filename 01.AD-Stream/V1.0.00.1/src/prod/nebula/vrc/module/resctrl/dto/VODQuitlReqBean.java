/**
 * 
 */
package prod.nebula.vrc.module.resctrl.dto;


/**
 * VOD资源控制 VOD资源释放请求封装类
 * 
 * @author 严东军
 * 
 */
public class VODQuitlReqBean {

	/**
	 * VOD资源控制-操作命令
	 */
	private String command;

	/**
	 * VOD资源控制-流水号
	 */
	private String serialNo;

	public VODQuitlReqBean() {

	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
}
