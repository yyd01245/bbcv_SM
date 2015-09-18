package prod.nebula.vgw4sida.module.resctrl.dto;

public class VODRecvKeyMsgBean {

	/**
	 * 键值设备类型
	 */
	private int devType;
	/**
	 * 序列号
	 */
	private int seqNum;
	/**
	 * 键值
	 */
	private int keyValue;
	/**
	 * 键值状态
	 */
	private int keyStatus;

	public VODRecvKeyMsgBean() {
	}

	public VODRecvKeyMsgBean(int devType, int seqNum, int keyValue,
			int keyStatus) {
		super();
		this.devType = devType;
		this.seqNum = seqNum;
		this.keyValue = keyValue;
		this.keyStatus = keyStatus;
	}

	public void setDevType(int devType) {
		this.devType = devType;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}

	public void setKeyStatus(int keyStatus) {
		this.keyStatus = keyStatus;
	}

	public int getDevType() {
		return devType;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public int getKeyValue() {
		return keyValue;
	}

	public int getKeyStatus() {
		return keyStatus;
	}

	@Override
	public String toString() {
		return "VODRecvKeyMsgBean [devType=" + devType + ", seqNum=" + seqNum
				+ ", keyValue=" + keyValue + ", keyStatus=" + keyStatus + "]";
	}

}
