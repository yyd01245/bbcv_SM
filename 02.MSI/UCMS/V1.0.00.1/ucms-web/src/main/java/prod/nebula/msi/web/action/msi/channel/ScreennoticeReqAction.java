/** 
 * Project: msi-web
 * author : PengSong
 * File Created at 2013-11-27 
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
package prod.nebula.msi.web.action.msi.channel;

import java.io.IOException;

import org.apache.log4j.Logger;

import prod.nebula.commons.enums.msi.ChannelCommandEnum;
import prod.nebula.dto.request.channelcenter.bgw.NoticeoverRequest;
import prod.nebula.dto.request.channelcenter.bgw.ScreennoticeRequest;
import prod.nebula.dto.response.channelcenter.bgw.NoticeoverResponse;
import prod.nebula.dto.response.channelcenter.bgw.ScreennoticeResponse;
import prod.nebula.dto.response.msi.BaseMsiResponse;
import prod.nebula.dto.response.msi.BaseResponseError;
import prod.nebula.exception.msi.BbcvMsiException;
import prod.nebula.exception.msi.MsiErrorCodeEnum;
import prod.nebula.msi.web.action.base.BaseAction;

import com.alibaba.fastjson.JSONObject;

/** 
 * TODO Comment of CommonReqAction 
 * 
 * @author PengSong 
 */
public class ScreennoticeReqAction extends BaseAction {

	private static final long serialVersionUID = -3766412656535187063L;

	private static final Logger logger=Logger.getLogger(ScreennoticeReqAction.class);
	
	private static final String cmd = ChannelCommandEnum.SCREENNOTICE.getCmd();
			
	@Override
	public void action() {
		BaseMsiResponse resp = new BaseMsiResponse(cmd, sequence);
		BaseResponseError error = null;
		
		if (validAuthCode()) {
			try {
				ScreennoticeRequest req = new ScreennoticeRequest(cmd);
				req.setTermid(stb_id);
				req.setTermtype("1001");
				req.setNoticemsg("notice msg");
				ScreennoticeResponse keydownResp = channelCenterBgwService.send(req, ScreennoticeResponse.class);
				resp.setReturn_code(Integer.valueOf(keydownResp.getRetcode()));
			} catch (IOException e) {
				error = new BaseResponseError(cmd, sequence, MsiErrorCodeEnum.SYSTEM_ERROR);
				logger.error(e.getMessage(), e);
			} catch (BbcvMsiException e) {
				error = new BaseResponseError(cmd, sequence, e.getErrorCode());
				logger.error(e.getMessage(), e);
			}
		} else {
			error = new BaseResponseError(cmd,sequence,MsiErrorCodeEnum.AUTH_CODE_ERROR);
			logger.error(cmd + " auth_code error!");
		}
		String result = null != error?JSONObject.toJSONString(error):JSONObject.toJSONString(resp);
		out(result);
	}
}
