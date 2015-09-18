package prod.nebula.mcs.config;

public class ServerConfiguration {
	private int CtrlServerProt; 	//监听服务端口
	private String version;			//当前版本号
	private String MatrixDevicesIP;	//矩阵设备地址IP
	private int MatrixDevicesPort;	//矩阵设备端口
	private String MtpRedisIP;		//MTP缓存地址
	private int MtpRedisPORT;		//MTP缓存端口
	private String MtpRedisPass;	//MTP缓存密码
	private String CscsRedisIP;		//CSCS缓存地址
	private int CscsRedisPORT;		//CSCS缓存端口
	private String CscsRedisPass;	//CSCS缓存密码
	private String CscsIP;			//CSCS服务地址
	public String getCscsRedisIP() {
		return CscsRedisIP;
	}

	public void setCscsRedisIP(String cscsRedisIP) {
		CscsRedisIP = cscsRedisIP;
	}

	public int getCscsRedisPORT() {
		return CscsRedisPORT;
	}

	public void setCscsRedisPORT(int cscsRedisPORT) {
		CscsRedisPORT = cscsRedisPORT;
	}

	public String getCscsIP() {
		return CscsIP;
	}

	public void setCscsIP(String cscsIP) {
		CscsIP = cscsIP;
	}

	public String getMtpRedisIP() {
		return MtpRedisIP;
	}

	public void setMtpRedisIP(String mtpRedisIP) {
		MtpRedisIP = mtpRedisIP;
	}

	public int getMtpRedisPORT() {
		return MtpRedisPORT;
	}

	public void setMtpRedisPORT(int mtpRedisPORT) {
		MtpRedisPORT = mtpRedisPORT;
	}

	public String getMtpRedisPass() {
		return MtpRedisPass;
	}

	public String getCscsRedisPass() {
		return CscsRedisPass;
	}

	public void setCscsRedisPass(String cscsRedisPass) {
		CscsRedisPass = cscsRedisPass;
	}

	public void setMtpRedisPass(String mtpRedisPass) {
		MtpRedisPass = mtpRedisPass;
	}

	public String getMatrixDevicesIP() {
		return MatrixDevicesIP;
	}

	public void setMatrixDevicesIP(String matrixDevicesIP) {
		MatrixDevicesIP = matrixDevicesIP;
	}

	public int getMatrixDevicesPort() {
		return MatrixDevicesPort;
	}

	public void setMatrixDevicesPort(int matrixDevicesPort) {
		MatrixDevicesPort = matrixDevicesPort;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getCtrlServerProt() {
		return CtrlServerProt;
	}

	public void setCtrlServerProt(int ctrlServerProt) {
		CtrlServerProt = ctrlServerProt;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n");
		sb.append("---------- Begin loading the config parameters ----------");
		sb.append("\r\n");
		sb.append("程序版本【Version】 = ");
		sb.append(this.version);
		sb.append("\r\n");
		sb.append("服务端口【CtrlServerProt】 = ");
		sb.append(this.CtrlServerProt);
		sb.append("---------- End loading-----------------------------------");
		return sb.toString();
	}
}
