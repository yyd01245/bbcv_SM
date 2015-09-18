/** 
 * Project: msi-service
 * author : PengSong
 * File Created at 2013-12-5 
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
package prod.nebula.service.socket.channelcenter;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import prod.nebula.dto.request.base.Request;
import prod.nebula.dto.response.base.ReponseUtil;
import prod.nebula.dto.response.base.Response;
import prod.nebula.exception.msi.BbcvMsiException;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.service.socket.SocketService;
import prod.nebula.socket.session.SocketSession;

/** 
 * TODO Comment of SwitchKeyAppServiceImpl 
 * 
 * @author PengSong 
 */
@Service("channelCenterBgwService")
public class ChannelCenterBgwServiceImpl implements SocketService {
	private static final Logger logger=Logger.getLogger(ChannelCenterBgwServiceImpl.class);
	
	private static SocketSession socketSession;
	
	public <T extends Response> T send(Request req, Class<T> cls) throws IOException, BbcvMsiException {
		if(null != socketSession) {
			String retStr = socketSession.sendStr(req.toJsonString());
			if(StringUtils.isNotBlank(retStr)) {
				return ReponseUtil.parseJsonString(retStr,cls);
			}
			return null;
		} else {
			logger.error("[msi-service] channel center bgw tcp service is not available!");
			throw new BbcvMsiException(MsiErrorCodeEnum.SOCKE_NOT_READY);
		}
	}
	
	public static void setSocketSession(SocketSession ss) {
		socketSession = ss;
	}
}
