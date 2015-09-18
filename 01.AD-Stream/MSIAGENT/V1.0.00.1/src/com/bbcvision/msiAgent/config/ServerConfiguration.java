package com.bbcvision.msiAgent.config;


public class ServerConfiguration {
	private String mag_server_ip;
	private int mag_server_port;
	private String ucms_server_ip;
	private int ucms_server_port;
	private String wec_server_ip;
	private int wec_server_port;
	private int source;
	private String area;
	private String mainPage;
	private String detailPage;
	private String authPage;
	
	

	public String getMag_server_ip() {
		return mag_server_ip;
	}



	public void setMag_server_ip(String mag_server_ip) {
		this.mag_server_ip = mag_server_ip;
	}



	public int getMag_server_port() {
		return mag_server_port;
	}



	public void setMag_server_port(int mag_server_port) {
		this.mag_server_port = mag_server_port;
	}



	public String getUcms_server_ip() {
		return ucms_server_ip;
	}



	public void setUcms_server_ip(String ucms_server_ip) {
		this.ucms_server_ip = ucms_server_ip;
	}



	public int getUcms_server_port() {
		return ucms_server_port;
	}



	public void setUcms_server_port(int ucms_server_port) {
		this.ucms_server_port = ucms_server_port;
	}



	public String getWec_server_ip() {
		return wec_server_ip;
	}



	public void setWec_server_ip(String wec_server_ip) {
		this.wec_server_ip = wec_server_ip;
	}



	public int getWec_server_port() {
		return wec_server_port;
	}



	public void setWec_server_port(int wec_server_port) {
		this.wec_server_port = wec_server_port;
	}



	public int getSource() {
		return source;
	}



	public void setSource(int source) {
		this.source = source;
	}



	public String getArea() {
		return area;
	}



	public void setArea(String area) {
		this.area = area;
	}

	
	


	public String getMainPage() {
		return mainPage;
	}



	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}



	public String getDetailPage() {
		return detailPage;
	}



	public void setDetailPage(String detailPage) {
		this.detailPage = detailPage;
	}



	public String getAuthPage() {
		return authPage;
	}



	public void setAuthPage(String authPage) {
		this.authPage = authPage;
	}



	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n");
		sb.append("----------The server configuration----------");
		sb.append("\r\n");
		sb.append("MAG IP: ");
		sb.append(this.getMag_server_ip());
		sb.append("\r\n");
		sb.append("MAG port: ");
		sb.append(this.getMag_server_port());
		sb.append("\r\n");
		sb.append("微信后台服务地址: ");
		sb.append(this.getWec_server_ip());
		sb.append("\r\n");
		sb.append("微信后台服务端口: ");
		sb.append(this.getWec_server_port());
		sb.append("\r\n");
		sb.append("连接方式: ");
		sb.append(this.getArea());
		sb.append("\r\n");		
		sb.append("----------The server configuration----------");
		return sb.toString();
	}

	
	
	
}
