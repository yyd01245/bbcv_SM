/** 
 * Project: bbcvision3-commons
 * author : PengSong
 * File Created at 2013-10-31 
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
package prod.nebula.commons.date;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * TODO Comment of DateUtils 
 * 
 * @author PengSong 
 */
public class DateUtil {
	public static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	/**
	 * date format yyyy-MM-dd HH:mm:ss
	 */
	public static final String dateFormatStr = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * date format yyyy-MM-dd
	 */
	public static final String dateFormatyyyyMMdd = "yyyy-MM-dd";
	
	/**
	 * date format yyyyMMdd
	 */
	public static final String dateFormatyyyyMMdd2 = "yyyyMMdd";
	
	/**
	 * date format yyyyMMddHHmmssSSS
	 */
	public static final String dateFormatyyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
	
	/**
	 * date format HH:mm
	 */
	public static final String dateFormatHHmm ="HH:mm";
	
	/**
	 * 获得当期的时间 String
	 * @return String 当前日期 yyyy-MM-dd HH:MM:ss
	 **/
	public static String getNowString(){
		return DateFormatUtils.format(new Date(), dateFormatStr);
	}
	
	/**
	 * 格式化日期为yyyy-MM-dd HH:mm:ss
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr){
		try {
			return DateUtils.parseDate(dateStr, new String[]{dateFormatStr});
		} catch (ParseException e) {
			logger.error("parseDate error!"+e.getMessage(),e);
		} 
		return null;
	}
	
	/**
	 * 格式化时间
	 * @param Date 时间
	 * @param format 格式化字符串 例如 "yyyy-MM-dd HH:MM:ss"
	 * @return String 格式化好的字符串 例如"2010-12-18 17:12:33"
	 * */
	public static String formatDate(Date date,String format){		
		return DateFormatUtils.format(date,format);
	}
	
	/**
	 * 得到格式化后的日期yyyy-MM-dd HH:MM:ss
	 * @return
	 */
	public static Date getFormatDate(){
		return parseDate(getNowString());
	}
	
	/**
	 * 获取日期目录，如: 2013/03/18/
	 * @return
	 */
	public static String getStringFolder(){
    	Calendar c = Calendar.getInstance();
    	StringBuilder sb = new StringBuilder("");
    	sb.append(c.get(Calendar.YEAR)).append("/");
    	sb.append(c.get(Calendar.MONTH) + 1).append("/");
    	sb.append(c.get(Calendar.DATE)).append("/");
    	return sb.toString();
	}
}
