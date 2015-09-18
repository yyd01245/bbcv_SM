package prod.nebula.mcs.module.dto;

public class Header {
	//MES-HEADER
	private String SyncByte	="";		//同步字节
	private String MajorVersion ="";	//主版本号
	private String MinorVersion ="";	//副版本号
	private String ComplVersion ="";	//编译版本
	private String MessageLen ="";		//命令消息长度
	private String Checksum	="";		//消息头校验和
	public String getSyncByte() {
		return SyncByte;
	}
	public void setSyncByte(String syncByte) {
		SyncByte = syncByte;
	}
	public String getMajorVersion() {
		return MajorVersion;
	}
	public void setMajorVersion(String majorVersion) {
		MajorVersion = majorVersion;
	}
	public String getMinorVersion() {
		return MinorVersion;
	}
	public void setMinorVersion(String minorVersion) {
		MinorVersion = minorVersion;
	}
	public String getComplVersion() {
		return ComplVersion;
	}
	public void setComplVersion(String complVersion) {
		ComplVersion = complVersion;
	}
	public String getMessageLen() {
		return MessageLen;
	}
	public void setMessageLen(String messageLen) {
		MessageLen = messageLen;
	}
	public String getChecksum() {
		return Checksum;
	}
	public void setChecksum(String checksum) {
		Checksum = checksum;
	}
	
}
