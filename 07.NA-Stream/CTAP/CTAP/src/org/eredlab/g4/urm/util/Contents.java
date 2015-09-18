/**  
 * 类名称：Contents 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-12-15 下午05:22:57 
 */
package org.eredlab.g4.urm.util;

/**   
 * 类名称：Contents   
 * 类描述：  报文内容 
 * 创建人：PengFei   
 * 创建时间：2014-12-15 下午05:22:57   
 * 备注：   数据库表对应的报文类别
 * @version    
 *    
 */
public interface Contents {
  
	
	
	/**
	 * 操作表类型（新增，修改，删除）
	 */
	public static final String INSERT = "insert"; 
	
	
	/**
	 * 操作表类型（新增，修改，删除）
	 */
	public static final String UPDATE = "update"; 
	
	
	
	/**
	 * 操作表类型（新增，修改，删除）
	 */
	public static final String DELETE = "delete"; 
	
	
	/**
	 * 广告表 
	 */
	public static final String AD = "ucs_adv_info"; 
	
	
	/**
	 * 导航表
	 */
	public static final String NAVIGATE = "ucs_navigate"; 
	
	
	/**
	 * 流资源表
	 */
	public static final String STREAMRESOURCE = "ucs_stream_resource"; 
	
	
	/**
	 * 网络区域表
	 */
	public static final String NETWORK = "ucs_network_area"; 
	
	
	/**
	 * Ipqam信息表 
	 */
	public static final String IPQAMINFO = "ipqam_info"; 
	
	/**
	 * Ipqam资源表 
	 */
	public static final String IPQAMRESOURCE = "ipqam_resource"; 
	
}
