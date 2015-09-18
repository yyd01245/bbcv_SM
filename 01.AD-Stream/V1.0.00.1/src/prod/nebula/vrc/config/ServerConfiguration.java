package prod.nebula.vrc.config;

import prod.nebula.vrc.core.common.constants.ResConstants;

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

	private String bwIp;
	private int bwPort;
	private int interval;
	private int dooptionTime;
	private long vodTimeout;

	private String ctasIp;
	private int ctasPort;

	private String Version;
	
	public String getCtasIp() {
		return ctasIp;
	}

	public void setCtasIp(String ctasIp) {
		this.ctasIp = ctasIp;
	}

	public int getCtasPort() {
		return ctasPort;
	}

	public void setCtasPort(int ctasPort) {
		this.ctasPort = ctasPort;
	}

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

	public String getBwIp() {
		return bwIp;
	}

	public void setBwIp(String bwIp) {
		this.bwIp = bwIp;
	}

	public int getBwPort() {
		return bwPort;
	}

	public void setBwPort(int bwPort) {
		this.bwPort = bwPort;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
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
		sb.append("³ÌÐò°æ±¾¡¾Version¡¿ = ");
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
}
