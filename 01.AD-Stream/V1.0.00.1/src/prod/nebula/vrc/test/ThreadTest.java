/**
 * 
 */
package prod.nebula.vrc.test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import prod.nebula.vrc.exception.IEvent;
import prod.nebula.vrc.exception.VODException;

/**
 * @author Administrator
 *
 */
public class ThreadTest implements IEvent{

	/**
	 * @param args
	 * @throws VODException 
	 * @throws SocketException 
	 */

	public static void main(String[] args) throws VODException{
		String xml = "mysql serilno=";
		if(xml.indexOf("serilno")>0){
			xml = xml.substring(0,xml.indexOf("serilno")).trim();;
			
		}
		System.out.println("xml="+xml);
/*		String rtspAddr="erwerwe";
		if (rtspAddr.indexOf("rtsp://") == -1) {

			throw new VODException("RTSP Addr is Wrong!");
		} else {
			rtspAddr = rtspAddr.substring(rtspAddr.indexOf("rtsp://"));
		}
		*/
		System.out.println("System="+System.currentTimeMillis()/1000);
		AtomicBoolean shutdown = new AtomicBoolean();
		shutdown.set(true);
		String rtspUrl="rtsp://21.254.47.82:5541/data/contents/dianpian.ts";
		rtspUrl = rtspUrl.substring(7);
		String ss = rtspUrl.substring(
				rtspUrl.indexOf(":") + 1, rtspUrl.indexOf("/"));
		System.out.println("ss="+ss);
/*		int port=7572;
		boolean flag = false;
		try {
			DatagramSocket socket = null;
			socket = new DatagramSocket(port);
			flag = true;
			socket.close();
		} catch (SocketException e) {
		}
		System.out.println("flag="+flag);*/
/*		try {
			DatagramSocket	sockeqqqt = new DatagramSocket(4000);
			sockeqqqt.close();
		} catch (SocketException e) {
			e.printStackTrace();
		}*/
		Properties p = new Properties();
	/*	String regionId = Commons.getStringPropertyValue(p, "VRC_RTSP_ADDR",
				"");*/
		String regionId = null ;
		if(null != regionId && "".equals(regionId)){
			System.out.println("commons=");
		}else{
			System.out.println("sdfsfd=");
		}
		
		
		System.out.println("getAvailablePort="+getAvailablePort());
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("1111","aaa");
		map.put("2222","bbb");
		map.put("3333","ccc");
		if(map.containsKey("1111")){
			System.out.println("WWWWWWWWWWWWWWW");
		}
	}
	
	private final static int startPort = 4000;
	private final static int endPort = 50000;
	private static int port = startPort;

	public static int getAvailablePort() {
	
		while (true) {
		if(port >= endPort){
			port = startPort;
		}
		if(isUserPorting(port)){
			int retPort = port++;
			return retPort;
		}
		
		 port++;
		}
	}

	public static boolean isUserPorting(int port){
		boolean flag = false;
		try {
			DatagramSocket socket = null;
			socket = new DatagramSocket(port);
		
			flag = true;
			System.out.println("userport="+port+",flag="+flag);
			socket.close();
		} catch (SocketException e) {
		}

		return flag;
		
		
		
	}

	@Override
	public void connect(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void read(SelectionKey key) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(Exception e) {
		// TODO Auto-generated method stub
		
	}
}
