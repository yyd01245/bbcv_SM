package prod.nebula.vrc.module.pant.client;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
import prod.nebula.vrc.service.TCPServer;
import prod.nebula.vrc.util.client.IOSocketClient;


public class RegBwClient  extends TimerTask{
	
	public static final Logger logger = LoggerFactory
	.getLogger(TCPServer.class);
	
	private String ip;
	private int port;
	
	public RegBwClient(String ip,int port)
	{
		this.ip=ip;
		this.port=port;
	}
	
	public void sendBwReg()
	{
		IOSocketClient client = new IOSocketClient();
		String bwIp = TCPServer.getConfig().getBwIp();
		int bwPort = TCPServer.getConfig().getBwPort();
		
		JSONObject retObj = new JSONObject();
		retObj.put("cmd", "VOD_REG");
		retObj.put("ip", ip);
		retObj.put("port",port);
		retObj.put("number", TCPServer.getControllerList().size());
		client.sendStr(
				bwIp,
				bwPort,
				2000,
				null,
				retObj.toString()+"XXEE", null);
	}
	
	@Override
	public void run() {
		sendBwReg();
	}

}
