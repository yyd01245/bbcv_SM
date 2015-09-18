/** 
 * Project: msi-exception
 * author : PengSong
 * File Created at 2013-11-14 
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
 * TODO Comment of ErrorCodeEnum 
 * 
 * @author PengSong 
 */
public interface ErrorCodeEnum {
	Integer getCode();
	
	String getMessage();
}
