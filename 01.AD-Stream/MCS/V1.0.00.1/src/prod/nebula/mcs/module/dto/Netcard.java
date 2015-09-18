package prod.nebula.mcs.module.dto;

import java.io.Serializable;

public class Netcard implements Serializable{
	//网卡信息节点
	public String name =""; 	//网卡的别名
	public String mac ="";  	//mac地址
	public String connect ="";	//网卡是否连接：true、false
	//IPV4信息节点
	public String ip ="";		//ip地址信息
	public String mask ="";		//掩码地址
	public String gateway ="";	//网关地址

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getConnect() {
		return connect;
	}
	public void setConnect(String connect) {
		this.connect = connect;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String mask) {
		this.mask = mask;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	

}
