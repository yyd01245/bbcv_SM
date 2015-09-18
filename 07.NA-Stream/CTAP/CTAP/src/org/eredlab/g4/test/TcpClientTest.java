/**
 * 
 */
package org.eredlab.g4.test;

import java.text.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.common.util.CommandSupport;
import org.eredlab.g4.common.util.MinaTextLineCodecFactory;
import org.eredlab.g4.common.util.client.Client;
import org.eredlab.g4.common.util.client.TcpClient;

/**
 * @author hj
 *
 */
public class TcpClientTest {

	private static Log logger = LogFactory.getLog(TcpClientTest.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		MinaTextLineCodecFactory codecFactory = new MinaTextLineCodecFactory(
				CommandSupport.CHARSET, CommandSupport.TAIL,
				CommandSupport.TAIL);
		for(int i=0;i<100;i++){
			Client c = new TcpClient();
			String sendStr = "{\"cmd\":\"auth\",\"termid\":\"11040010100052544C1A009D\",\"termtype\":\"1001\"}XXEE";
			String recStr = c.sendStr("192.168.100.108", 18090, 100, null, sendStr, codecFactory);
			logger.info("recStr===>"+recStr);
		}
	}

}
