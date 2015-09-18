/** 
 * Project: msi-dto
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
package prod.nebula.dto.response.base;

import com.alibaba.fastjson.JSONObject;

/** 
 * TODO Comment of ReponseUtil 
 * 
 * @author PengSong 
 */
public class ReponseUtil {
	public static <T extends Response> T parseJsonString(String objStr,Class<T> cls){
		return JSONObject.parseObject(objStr.trim(), cls);
	}
}
