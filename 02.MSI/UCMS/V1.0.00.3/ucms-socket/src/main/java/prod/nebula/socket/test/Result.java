/** 
 * Project: mtp-commons
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
package prod.nebula.socket.test;

/** 
 * TODO Comment of Result 
 * 
 * @author PengSong 
 */
public class Result {
	private long time;
	
	private boolean error;
	
	private boolean isNotEqual;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isNotEqual() {
		return isNotEqual;
	}

	public void setNotEqual(boolean isNotEqual) {
		this.isNotEqual = isNotEqual;
	}
}
