/** 
 * Project: msi-commons
 * author : PengSong
 * File Created at 2013-12-3 
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
 * MSI常量定义类
 * 
 * @author PengSong 
 */
public class MsiConstans {
	
	public static final int oneDaySeconds = 24 * 3600;
	
	/**
	 * 双向盒子
	 */
	public static final String stbTermTypeDouble = "1001";
	
	public static final String keyStatusDown = "2";
	
	public static final String keyStatusUp = "3";
	
	/**
	 * 是1
	 */
	public static final int YES = 1;
	
	/**
	 * 否2
	 */
	public static final int NO = 2;
	
	/**
	 * 绑定类型,手机号码 1
	 */
	public static final int BIND_TYPE_MOBILE = 1;
	
	/**
	 * 绑定类型,机顶盒号码 2
	 */
	public static final int BIND_TYPE_STB = 2;
	
	/**
	 * 机顶盒类型,单向1
	 */
	public static final String STB_TYPE_SINGLE = "1";
	
	/**
	 * 机顶盒类型,双向2
	 */
	public static final String STB_TYPE_DOUBLE = "2";
	
	/**
	 * 机顶盒类型,一体机3
	 */
	public static final String STB_TYPE_WHOLE = "3";
	
	/**
	 * 机顶盒能力,高清1
	 */
	public static final String STB_ABILITY_HDMI = "1";
	
	/**
	 * 机顶盒能力,标清2
	 */
	public static final String STB_ABILITY_SDI = "2";
	
	/**
	 * 指令类型,DVB直播切台指令1
	 */
	public static final int COMMAND_TYPE_DVB = 1;
	
	/**
	 * 指令类型,通过CDN服务器切VOD2
	 */
	public static final int COMMAND_TYPE_VOD_CDN = 2;
	
	/**
	 * 指令类型,通过ipanel切换VOD4
	 */
	public static final int COMMAND_TYPE_VOD_IPANEL = 4;
}
