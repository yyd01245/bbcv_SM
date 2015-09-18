/** 
 * Project: bbcvision3-socket
 * author : PengSong
 * File Created at 2013-11-28 
 *
 * Copyright bbcvision Corporation Limited. 
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * bbcvision company. ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with bbcvision.com. 
 */
package prod.nebula.socket.session;

import java.io.IOException;

/** 
 * TODO Comment of SocketSession 
 * 
 * @author PengSong 
 */
public interface SocketSession {
	String sendStr(String sendStr) throws IOException;
	
	String sendSynMsg(byte[] datas) throws IOException;
}
