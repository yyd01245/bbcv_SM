/** 
 * Project: bbcvision3-dto
 * author : PengSong
 * File Created at 2013-11-5 
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
package prod.nebula.dto.response.msi;

import prod.nebula.exception.ErrorCodeEnum;



/** 
 * TODO Comment of BaseResponseError 
 * 
 * @author PengSong 
 */
public class BaseResponseError extends BaseMsiResponse {
	private static final long serialVersionUID = 8204121617633612149L;

	private String msg;
	
	public BaseResponseError() {
	}
	
	public BaseResponseError(String cmd,String sequence,ErrorCodeEnum errorCode) {
		super.setCmd(cmd);
		super.setSequence(sequence);
		super.setReturn_code(errorCode.getCode());
		this.setMsg(errorCode.getMessage());
	}
	
	public BaseResponseError(String cmd,String sequence,ErrorCodeEnum errorCode,String msg) {
		super.setCmd(cmd);
		super.setSequence(sequence);
		super.setReturn_code(errorCode.getCode());
		this.setMsg(errorCode.getMessage()+"  "+msg);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
