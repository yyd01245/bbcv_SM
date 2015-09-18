package prod.nebula.vgw4vlc.config;
/**  * commons-net-2.0.jar是工程依赖包   */  

import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.log4j.Logger;
  
public class TelnetClientServer {  
	private Logger logger = Logger.getLogger(TelnetClientServer.class);
    private TelnetClient telnet = new TelnetClient();  
    private InputStream in;  
    private PrintStream out;  
    private char prompt = '>';      // 普通用户结束   
    private String ip;
    private int port;
    
    public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public TelnetClientServer(){}
    
//    public TelnetClientServer(String ip, int port) {  
//        try {  
//            telnet.connect(ip, port);  
//            in = telnet.getInputStream();  
//            out = new PrintStream(telnet.getOutputStream());  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//    }  
  
    public void Connect(){
    	 try {  
             telnet.connect(ip,port);  
             in = telnet.getInputStream();  
             out = new PrintStream(telnet.getOutputStream());  
         } catch (Exception e) {  
             e.printStackTrace();  
         }  
    }
    public String readUntil(String pattern) {  
        try {  
            char lastChar = pattern.charAt(pattern.length() - 1);  
            StringBuffer sb = new StringBuffer();  
            char ch = (char) in.read();  
            while (true) {  
                sb.append(ch);  
                if (ch == lastChar) {  
                    if (sb.toString().endsWith(pattern)) {  
//                    	logger.info("【VGW-VLC】接收返回："+sb.toString());
                        return sb.toString();  
                    }  
                }  
                ch = (char) in.read();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{
        }  
        return null;  
    }  
  
    /** * 写操作 * * @param value */  
    public void write(String value) {  
        try {  
            out.println(value);  
            out.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** * 向目标发送命令字符串 * * @param command * @return */  
    public String sendCommand(String command) {  
        try {  
        	logger.info("【VGW-VLC】发送指令："+command);
            write(command);  
            return readUntil(prompt + " ");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    
    /** * 关闭连接 */  
    public void disconnect() {  
        try {  
            telnet.disconnect();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    /** * 关闭连接 */  
    public boolean isConnect() {  
       return telnet.isConnected();
    }  
  
    public static void main(String[] args) {  
        try {  
            System.out.println("启动Telnet...");  
            String ip = "192.168.100.56";  
            int port = 5000;  
            TelnetClientServer telnet = new TelnetClientServer();  
            telnet.setIp(ip);
            telnet.setPort(port);
            telnet.Connect();
            Thread.sleep(1000);
            String r = telnet.sendCommand("bbcv");  
            System.out.println(telnet.sendCommand("del ch1"));
            String r1 = telnet.sendCommand("new ch1 broadcast enabled");  
            String r2 = telnet.sendCommand("setup ch1 input /home/x00/yyd/hd.ts ");  
            String r3 = telnet.sendCommand("setup ch1 output udp://192.168.30.165:1234");  
            String r4 = telnet.sendCommand("control ch1 play");
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
} 