/**
 * 
 */
package prod.nebula.vrc.core.common.constants;

import java.io.Serializable;

/**
 * 通用报文字段定义
 */
public class ResConstants implements Serializable {
	private static final long serialVersionUID = 2493690826973813015L;
	/** 模块名称 http */
	public final static String HTTP_MODULE_NAME = "http";
	
	
	/**
	 * params
	 */
	public final static String sessionId = "sessionId";
	
	public final static String beginTime = "begin";
	
	public final static String serialNo = "serialNo";
	
	public final static String VERSION = "1.0.00.1";
	
	public final static String TYPE = "ENRICH";
	
	/** errorcode */
	public final static String INTERNAL_ERROR_MSG = "-1301";
}
