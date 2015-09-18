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
package prod.nebula.service.socket;

import java.io.IOException;

import prod.nebula.dto.request.base.Request;
import prod.nebula.dto.response.base.Response;
import prod.nebula.exception.msi.BbcvMsiException;

/** 
 * TODO Comment of SocketService 
 * 
 * @author PengSong 
 */
public interface SocketService {
	<T extends Response> T send(Request req,Class<T> cls) throws IOException, BbcvMsiException;
}
