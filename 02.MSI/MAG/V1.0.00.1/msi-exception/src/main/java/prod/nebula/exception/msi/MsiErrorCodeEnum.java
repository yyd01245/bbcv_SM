/** 
 * Project: msi-exception
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
package prod.nebula.exception.msi;

import prod.nebula.exception.ErrorCodeEnum;


/** 
 * TODO Comment of MsiErrorCodeEnum 
 * 
 * @author PengSong 
 */
public enum MsiErrorCodeEnum implements ErrorCodeEnum {
	TOKEN_IS_ERROR(-2,"token is error!"),
	VOD_PLAY_FAIL(-3,"vod play fail!"),
	USER_NOT_INVOD(-4,"user not in vodplay status!"),
	USER_UNBAND(-5,"user not in banding or vodplay status,cannot play vod!"),
	SYSTEM_ERROR(-1000,"system error"),
	COMMANDCODE_NOT_EXIST(-1001,"command code not exist"),
	PARAMS_ERROR(-1002,"params error"),
	SQLEXCEPTION_ERROR(-1003,"sql exception error"),
	UNSUPPORT_TYPE(-1004,"unsupport type error"),
	SOCKE_NOT_READY(-1005,"socket is not available"),
	IS_NOT_HTTPPOST(-1006,"http request must be use post method."),
	USER_ALREADY_EXIST(-1007,"user is already exist"),
	AUTH_CODE_ERROR(-2000,"auth code error"),
	LICENCE_ERROR(-2001, "licence error"),
	QUERY_NULL(-2002, "query result is null"),
	QUERY_MORE_RESULT(-2003, "query more than one result"),
	QR_CODE_GEN_ERROR(-2004, "generate qr code error"),
	QR_CODE_QUERY_ERROR(-2005, "query qr code error"),
	MGW_AUTHCODE_ERROR(-2006, "get mgw auth code error"),
	MGW_LOGIN_ERROR(-2007, "mgw login request error"),
	MGW_PLAY_ERROR(-2008,"mgw play error"),
	KEY_SEND_ERROR(-2009,"mgw key send error"),
	MOBILE_NOT_EXISTS(-2010,"user not exists!"),
	RESOLVE_LOCATE_ERROR(-2011,"resolve location error!"),
	STB_IS_NOT_BIND(-2012,"stb is not bind!"),
	STB_NOT_START(-2013,"stb is not start!"),
	STB_IS_NOT_ONLINE(-2014,"stb is not online!"),
	STB_IS_NOT_EXISTS(-2015,"stb is not exists!"),
	
	MS_BIND_ERROR(-2017,"ms socket bind false!"),
	
	;
	
	private Integer code;
	private String message;
	
	private MsiErrorCodeEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
