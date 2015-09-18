/** 
 * Project: bbcvision3-service
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
package prod.nebula.service.socket.channelcenter;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import prod.nebula.dto.request.base.Request;
import prod.nebula.dto.response.base.ReponseUtil;
import prod.nebula.dto.response.base.Response;
import prod.nebula.exception.msi.BbcvMsiException;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.service.socket.SocketService;
import prod.nebula.socket.session.SocketSessionSupport;


/** 
 * TODO Comment of RegisterSocket 
 * 
 * @author PengSong 
 */
@Repository("channelCenterService")
public class ChannelCenterServiceImpl extends SocketSessionSupport implements SocketService {
	
	private static final Logger logger=Logger.getLogger(ChannelCenterServiceImpl.class);

	public <T extends Response> T send(Request req,Class<T> cls) throws IOException, BbcvMsiException {
		if(getSocketSession() != null) {
			String retStr = getSocketSession().sendStr(req.toJsonString());
			if(StringUtils.isNotBlank(retStr)) {
				return ReponseUtil.parseJsonString(retStr,cls);
			}
			return null;
		} else {
			logger.error("[msi-service] channel center tcp service is not available!");
			throw new BbcvMsiException(MsiErrorCodeEnum.SOCKE_NOT_READY);
		}
	}
}
