package prod.nebula.vgw4sida.util;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取环境IP工具类
 * 
 * @author 严东军
 * 
 */
public class IpUtil {
	public static String getLocalIpAddress(String ethName) {
		String retIp = "";
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces
						.nextElement();
				if ((ni.getName()).toLowerCase().equals(ethName.toLowerCase())) {
					ip = (InetAddress) ni.getInetAddresses().nextElement();
					retIp = ip.getHostAddress();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retIp;
	}

	
	 /**
     * 根据网卡取本机配置的IP
     * 如果是双网卡的，则取出外网IP
     * @return
     */
    public static String getIp(){
        String localip=null;//本地IP，如果没有配置外网IP则返回它
        String netip=null;//外网IP
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded=false;//是否找到外网IP
            while(netInterfaces.hasMoreElements() && !finded){
                NetworkInterface ni=netInterfaces.nextElement();
                Enumeration<InetAddress> address=ni.getInetAddresses();
                while(address.hasMoreElements()){
                    ip=address.nextElement();
                    if( !ip.isSiteLocalAddress() && !ip.isLoopbackAddress()&& ip.getHostAddress().indexOf(":")==-1){//外网IP
                        netip=ip.getHostAddress();
                        finded=true;
                        break;
                    }else if(ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){//内网IP
                        localip=ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if(netip!=null && !"".equals(netip)){
            return netip;
        }else{
            return localip;
        }
    }
	/**
	 * 判断当前操作是否Windows.
	 * 
	 * @return true---是Windows操作系统
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	private final static int startPort = 4000;
	private final static int endPort = 50000;
	private static int port = startPort;

	public static int getAvailablePort() {
		while (true) {
			if (port > endPort) {
				port = startPort;
			}

			if (isPortUsing(port)) {
				int retPort = port++;
				return retPort;
			}
			
			port++;
		}
	}

	public static boolean isPortUsing(int port) {
		boolean flag = false;
		try {
			DatagramSocket socket = null;
			socket = new DatagramSocket(port);
			flag = true;
			socket.close();
		} catch (SocketException e) {
		}

		return flag;
	}
}
