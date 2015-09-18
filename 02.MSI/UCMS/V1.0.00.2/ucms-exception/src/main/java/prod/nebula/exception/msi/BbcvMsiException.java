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

import prod.nebula.exception.BbcvException;

/** 
 * TODO Comment of BbcvMsiException 
 * 
 * @author PengSong 
 */
public class BbcvMsiException extends BbcvException {
	private static final long serialVersionUID = 702054401914436052L;
	
	private MsiErrorCodeEnum errorCode;
	
	public BbcvMsiException(MsiErrorCodeEnum errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public MsiErrorCodeEnum getErrorCode() {
		return errorCode;
	}
	
	
}
