/**
 * 
 */
package prod.nebula.mcs.core.common;

import java.io.Serializable;

/**
 * 通用报文字段定义
 */
public class ResConstants implements Serializable {
	private static final long serialVersionUID = 2493690826973813015L;
	
	/** 模块名称 core */
	public final static String CORE_MODULE_NAME = "core";
	/** 模块名称 ctrl */
	public final static String MTP_MODULE_NAME = "mtp";
	/** 模块名称 xmem */
	public final static String XMEM_MODULE_NAME = "xmem";
	/** 模块名称 mdb */
	public final static String MDB_MODULE_NAME = "mdb";
	
	/** 报文头 */
	public final static String COM_PREFIX = "XXBB";
	
	/** 报文尾 */
	public final static String COM_SUFFIX= "XXEE";
	
	/**程序版本*/
	public final static String VERSION = "1.0.00.1"; 
	
}
