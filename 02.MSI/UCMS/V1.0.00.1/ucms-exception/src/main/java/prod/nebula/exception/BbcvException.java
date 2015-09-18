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
package prod.nebula.exception;

/** 
 * TODO Comment of BbcvException 
 * 
 * @author PengSong 
 */
public class BbcvException extends Exception {
	private static final long serialVersionUID = 702054401914436052L;
	
	private ErrorCodeEnum errorCode;
	
	public BbcvException(String message) {
		super(message);
	}
	
	public BbcvException(ErrorCodeEnum errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCodeEnum getErrorCode() {
		return errorCode;
	}
	
	
}
