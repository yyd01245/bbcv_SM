package prod.nebula.vgw4sida.config;

import prod.nebula.vgw4sida.core.common.ResConstants;

public class ServerConfiguration {
	private int poolSize;
	private int port;
	private int readBufferSize;
	private int receiveBufferSize;
	private int sendBufferSize;
	private int clientBeginUDPPort;
	private int clientPortSize;
	private int clientPoolSize;
	private String DatagramHeader;
	private String DatagramEnd;
	private int source;
	private String CSCGAddress;
	private int CSCGPort;
	private String localIpAddress;
	private String regionId;
	private String rtspAddr;

	private int dooptionTime;
	private long vodTimeout;
	
	private String SMserverIP;
	private int SMserverPORT;


	private String Version;
	
	private int forwardTime;
	
	private int threadTime;
	
	private boolean reportType;
	
	private String DBdrivename;
	private String DBurl;
	private String DBuser;
	private String DBpasswd;
	private int DBconnecttype;
	private int DBminpoolsize;
	private int DBmaxpoolsize;
	private int DBcquireincrement;
	private int DBmaxstatements;
	private int DBcheckouttimeout;
	private int DBmaxidletime;
	public int getDooptionTime() {
		return dooptionTime;
	}

	public void setDooptionTime(int dooptionTime) {
		this.dooptionTime = dooptionTime;
	}

	public long getVodTimeout() {
		return vodTimeout;
	}

	public void setVodTimeout(long vodTimeout) {
		this.vodTimeout = vodTimeout;
	}


	public String getCSCGAddress() {
		return CSCGAddress;
	}

	public void setCSCGAddress(String address) {
		CSCGAddress = address;
	}

	public int getCSCGPort() {
		return CSCGPort;
	}

	public void setCSCGPort(int port) {
		CSCGPort = port;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getReadBufferSize() {
		return readBufferSize;
	}

	public void setReadBufferSize(int readBufferSize) {
		this.readBufferSize = readBufferSize;
	}

	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}

	public int getSendBufferSize() {
		return sendBufferSize;
	}

	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}

	public int getClientBeginUDPPort() {
		return clientBeginUDPPort;
	}

	public void setClientBeginUDPPort(int clientBeginUDPPort) {
		this.clientBeginUDPPort = clientBeginUDPPort;
	}

	public int getClientPortSize() {
		return clientPortSize;
	}

	public void setClientPortSize(int clientPortSize) {
		this.clientPortSize = clientPortSize;
	}

	public int getClientPoolSize() {
		return clientPoolSize;
	}

	public void setClientPoolSize(int clientPoolSize) {
		this.clientPoolSize = clientPoolSize;
	}

	public String getDatagramHeader() {
		return DatagramHeader;
	}

	public void setDatagramHeader(String datagramHeader) {
		DatagramHeader = datagramHeader;
	}

	public String getDatagramEnd() {
		return DatagramEnd;
	}

	public void setDatagramEnd(String datagramEnd) {
		DatagramEnd = datagramEnd;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getLocalIpAddress() {
		return localIpAddress;
	}

	public void setLocalIpAddress(String localIpAddress) {
		this.localIpAddress = localIpAddress;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRtspAddr() {
		return rtspAddr;
	}

	public void setRtspAddr(String rtspAddr) {
		this.rtspAddr = rtspAddr;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n");
		sb.append("----------The server configuration----------");
		sb.append("\r\n");
		sb.append("程序版本【Version】 = ");
		sb.append(ResConstants.VERSION);
		sb.append("\r\n");
		sb.append("VRC port: ");
		sb.append(this.getPort());
		sb.append("\r\n");
		sb.append("VRC client begin UDP port: ");
		sb.append(this.getClientBeginUDPPort());
		sb.append("\r\n");
		sb.append("VRC local ip: ");
		sb.append(this.getLocalIpAddress());
		sb.append("\r\n");
		sb.append("RegionId: ");
		sb.append(this.getRegionId());
		sb.append("\r\n");
		sb.append("RtspAddr: ");
		sb.append(this.getRtspAddr());
		sb.append("\r\n");
		sb.append("----------The server configuration----------");
		return sb.toString();
	}

	public int getForwardTime() {
		return forwardTime;
	}

	public void setForwardTime(int forwardTime) {
		this.forwardTime = forwardTime;
	}

	public int getThreadTime() {
		return threadTime;
	}

	public void setThreadTime(int threadTime) {
		this.threadTime = threadTime;
	}

	public boolean isReportType() {
		return reportType;
	}

	public void setReportType(boolean reportType) {
		this.reportType = reportType;
	}

	public String getSMserverIP() {
		return SMserverIP;
	}

	public void setSMserverIP(String sMserverIP) {
		SMserverIP = sMserverIP;
	}

	public int getSMserverPORT() {
		return SMserverPORT;
	}

	public void setSMserverPORT(int sMserverPORT) {
		SMserverPORT = sMserverPORT;
	}

	public String getDBdrivename() {
		return DBdrivename;
	}

	public void setDBdrivename(String dBdrivename) {
		DBdrivename = dBdrivename;
	}

	public String getDBurl() {
		return DBurl;
	}

	public void setDBurl(String dBurl) {
		DBurl = dBurl;
	}

	public String getDBuser() {
		return DBuser;
	}

	public void setDBuser(String dBuser) {
		DBuser = dBuser;
	}

	public String getDBpasswd() {
		return DBpasswd;
	}

	public void setDBpasswd(String dBpasswd) {
		DBpasswd = dBpasswd;
	}

	public int getDBconnecttype() {
		return DBconnecttype;
	}

	public void setDBconnecttype(int dBconnecttype) {
		DBconnecttype = dBconnecttype;
	}

	public int getDBminpoolsize() {
		return DBminpoolsize;
	}

	public void setDBminpoolsize(int dBminpoolsize) {
		DBminpoolsize = dBminpoolsize;
	}

	public int getDBmaxpoolsize() {
		return DBmaxpoolsize;
	}

	public void setDBmaxpoolsize(int dBmaxpoolsize) {
		DBmaxpoolsize = dBmaxpoolsize;
	}

	public int getDBcquireincrement() {
		return DBcquireincrement;
	}

	public void setDBcquireincrement(int dBcquireincrement) {
		DBcquireincrement = dBcquireincrement;
	}

	public int getDBmaxstatements() {
		return DBmaxstatements;
	}

	public void setDBmaxstatements(int dBmaxstatements) {
		DBmaxstatements = dBmaxstatements;
	}

	public int getDBcheckouttimeout() {
		return DBcheckouttimeout;
	}

	public void setDBcheckouttimeout(int dBcheckouttimeout) {
		DBcheckouttimeout = dBcheckouttimeout;
	}

	public int getDBmaxidletime() {
		return DBmaxidletime;
	}

	public void setDBmaxidletime(int dBmaxidletime) {
		DBmaxidletime = dBmaxidletime;
	}
	
	
	
}
