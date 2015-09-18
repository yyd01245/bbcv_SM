package prod.nebula.mcs.core.uti.client;

import java.net.ConnectException;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;

import prod.nebula.mcs.core.common.CommConstants;
import prod.nebula.mcs.module.handle.CommunicationStandard;
import prod.nebula.mcs.util.Base64Util;


public class IOSocketClient implements Client {
	public final Logger logger = Logger.getLogger(IOSocketClient.class);
	CommunicationStandard ccs = new CommunicationStandard();
	public String sendStr(String ip, int port, int timeoutseconds,
			IoHandlerAdapter ioHandler, byte[] b, String sendStr,
			MinaTextLineCodecFactory codecFactory) {
		IOSocket socket = null;
		String retStr = "";
		try {
			try {
				logger.info(CommConstants.HEAD+"tcp client send [" + ip + ":" + port
						+ "] message:" + sendStr + "");
				socket = new IOSocket(ip, port,timeoutseconds);
				byte[] sendBuffer = sendStr.getBytes("UTF-8");
				byte[] receiveBuffer = new byte[4096];
				byte[] bs =Base64Util.addBytes(b, sendBuffer);
				socket.sendAndreceive(bs, receiveBuffer);
				int i =ccs.parsePacketHeader(receiveBuffer);
				System.out.println(CommConstants.HEAD+"接收报文字节数："+i);
				retStr = new String(receiveBuffer, "UTF-8");
				if(retStr.indexOf("<response")>0){
					retStr = retStr.substring(retStr.indexOf("<response"), retStr.lastIndexOf("</response>")+11);
				}else if(retStr.indexOf("<alert")>0){
					retStr = retStr.substring(retStr.indexOf("<alert"), retStr.lastIndexOf("</alert>")+8);
				}
				retStr = retStr.trim();
				logger.info(CommConstants.HEAD+"tcp client recvice [" + ip + ":"
						+ port + "] message:" + retStr + "");
			} catch (ConnectException e) {
				logger.info(CommConstants.HEAD+"tcp client connect timeout [" + ip
						+ ":" + port + "]" + "");
			} catch (Exception ex) {
				logger.error(CommConstants.HEAD+"tcp client exception:" + ex);
				if (socket != null)
					socket.close();
			} finally {
				if (socket != null)
					socket.close();
			}
		} catch (Exception e) {
			logger.error(CommConstants.HEAD+"tcp client error! the server [" + ip
					+ ":" + port + "]" + "" + e);
		}
		return retStr;
	}

	@Override
	public String sendStr(String ip, int port, int timeoutseconds,
			IoHandlerAdapter ioHandler, String sendStr,
			MinaTextLineCodecFactory codecFactory) {
		return null;
	}

}
