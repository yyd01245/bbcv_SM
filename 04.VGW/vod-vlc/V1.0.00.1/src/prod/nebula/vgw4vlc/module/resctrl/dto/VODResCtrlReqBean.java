/**
 * 
 */
package prod.nebula.vgw4vlc.module.resctrl.dto;

import prod.nebula.vgw4vlc.config.VODConst;

/**
 * VOD资源控制请求封装类
 * @author 严东军
 *
 */
public class VODResCtrlReqBean {
	/**
	 * VOD资源控制-请求头信息
	 */
	private String head;
	
	/**
	 * VOD资源控制-请求类型
	 */
	private String reqHead;
	
	/**
	 * VOD资源控制-键值信息
	 */
	private String keyValue;
	
	/**
	 * VOD资源控制-请求尾信息
	 */
	private String end;
	
	public VODResCtrlReqBean(){
		
	}

	/**
	 * @return the head
	 */
	public String getHead() {
		return head;
	}

	/**
	 * @param head the head to set
	 */
	public void setHead(String head) {
		this.head = head;
	}

	/**
	 * @return the reqHead
	 */
	public String getReqHead() {
		return reqHead;
	}

	/**
	 * @param reqHead the reqHead to set
	 */
	public void setReqHead(String reqHead) {
		this.reqHead = reqHead;
	}

	/**
	 * @return the keyValue
	 */
	public String getKeyValue() {
		return keyValue;
	}

	/**
	 * @param keyValue the keyValue to set
	 */
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * @return the end
	 */
	public String getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		sb.append(this.getHead());
		sb.append(VODConst.SEPARATOR);
		sb.append(this.getReqHead());
		sb.append(VODConst.SEPARATOR);
		sb.append(this.getKeyValue());
		sb.append(this.getEnd());
		return sb.toString();
	}
	
}
