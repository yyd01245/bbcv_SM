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
package prod.nebula.commons.constans;

/** 
 * TODO Comment of MsiRedisKey 
 * 
 * @author PengSong 
 */
public class MsiRedisKey {
	public static final String KEY_PRE = "msi:redis:";
	
	public static String getMybatisKey(int hashcode){
		return KEY_PRE + hashcode +":mybatis";
	}
	
	public static String getAuthcodeKey(String id) {
		return KEY_PRE + id +":AuthCode";
	}
	
	public static String getMgwAuthcodeKey(String id) {
		return KEY_PRE + id +":MgwAuthCode";
	}
	
	public static String getMgwAreaIdKey(String id) {
		return KEY_PRE + id +":MgwAreaId";
	}
	
	public static String getQrCodeXmlKey(int code){
		return KEY_PRE + code +":QrCodeXml";
	}
	
	public static String getQrCodeStbIdKey(int code){
		return KEY_PRE + code +":QrCodeStbId";
	}
	
	public static String getQrCodeRegionIdKey(int code){
		return KEY_PRE + code +":RegionId";
	}
	
	public static String getQrCodeStbAbilityKey(int code){
		return KEY_PRE + code +":StbAbility";
	}
	
	public static String getRegionCacheKey(String code){
		return KEY_PRE + code + ":region:mybatis";
	}
	
	public static String getLoginStbIdKey(String mobile_id){
		return KEY_PRE + mobile_id + ":loginStb";
	}
}