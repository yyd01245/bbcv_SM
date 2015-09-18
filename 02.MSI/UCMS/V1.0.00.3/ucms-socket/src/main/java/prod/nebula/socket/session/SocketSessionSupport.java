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

import static org.springframework.util.Assert.notNull;

import org.springframework.beans.factory.InitializingBean;

/** 
 * TODO Comment of SocketSessionSupport 
 * 
 * @author PengSong 
 */
public abstract class SocketSessionSupport implements InitializingBean {

	private SocketSession socketSession;
	
	public SocketSession getSocketSession() {
		return socketSession;
	}

	public void setSocketSession(SocketSession socketSession) {
		this.socketSession = socketSession;
	}
	
	public void afterPropertiesSet() throws Exception {
		notNull(this.socketSession, "Property 'socketSession' are required");
	}
}
