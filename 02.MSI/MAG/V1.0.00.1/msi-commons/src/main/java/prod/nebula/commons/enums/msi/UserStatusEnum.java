/** 
 * Project: msi-commons
 * author : PengSong
 * File Created at 2013-12-2 
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
package prod.nebula.commons.enums.msi;

/** 
 * 用户状态枚举类
 * 
 * @author PengSong 
 */
public enum UserStatusEnum {
	NORMAL(1,"正常");
	
	private Integer status;
	
	private String desc;
	
	private UserStatusEnum(Integer status,String desc) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	
	public String getDesc() {
		return this.desc;
	}
}
