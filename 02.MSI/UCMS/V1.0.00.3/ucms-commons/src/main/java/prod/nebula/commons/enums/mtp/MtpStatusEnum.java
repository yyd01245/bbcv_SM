package prod.nebula.commons.enums.mtp;

public enum MtpStatusEnum {

	//user:bind
	NORMAL(0),
	FREEZE(-1),
	DELETE(-2),
	
	//dvb 
	Recommend(1),
	
	//comment_type
	DVB(1),
	VOD(2);
	
	private Integer status;
	
	private MtpStatusEnum(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}
}