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
package prod.nebula.dto.response.mtp;

import prod.nebula.dto.response.base.Response;
import prod.nebula.exception.ErrorCodeEnum;


/** 
 * TODO Comment of BaseResponseError 
 * 
 * @author PengSong 
 */
public class BaseResponseError implements Response {
	private static final long serialVersionUID = 8204121617633612149L;

	private Integer return_code;
	
	private String msg;
	
	public BaseResponseError() {
	}
	
	public BaseResponseError(ErrorCodeEnum errorCode) {
		this.return_code = errorCode.getCode();
		this.msg = errorCode.getMessage();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getReturn_code() {
		return return_code;
	}

	public void setReturn_code(Integer return_code) {
		this.return_code = return_code;
	}
}
