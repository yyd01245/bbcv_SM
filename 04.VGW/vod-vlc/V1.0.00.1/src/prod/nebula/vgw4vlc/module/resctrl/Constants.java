/**
 * 
 */
package prod.nebula.vgw4vlc.module.resctrl;

import java.io.Serializable;

/**
 * 通用报文字段定义
 */
public class Constants implements Serializable {
	private static final long serialVersionUID = 2493690826973813015L;
	public static final String DATE_FORMAT_COMPACT = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT_STANDARD = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_DETAIL = "yyyy-MM-dd HH:mm:ss";
	
	/** 模块名称vgw */
	
	/** 报文头 */
	public final static String COM_PREFIX = "XXBB";
	
	/** 报文尾 */
	public final static String COM_SUFFIX= "XXEE";
	
	public final static String USERREPORT_IP = "userreport.ip";
	
	public final static String USERREPORT_PORT = "userreport.port";

	public final static String VGW_PORT = "vgw.port";
}
