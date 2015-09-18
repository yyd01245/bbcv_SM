package com.bbcvision.Multiscreen.configs;
/**
 * 
 * @Title: 网络配置信息
 *		
 * @Description: 
 *		
 * @author Nestor bbcvision.com 
 *		
 * @date 2014-10-10 下午3:54:52
 *		
 * @version V1.0
 *
 */
public class NetConfig {

	public static String NC_POST = "18080";
	public static String NC_HOST = "218.108.50.254";
    //public static String NC_HOST = "192.168.100.12";
    //public static String NC_HOST = "10.169.8.29";
    public static String RESOLUTIONURL ="http://218.108.50.246/bbcvcms/uploads/plus/list.php?tid=9";
    //public static String RESOLUTIONURL ="http://192.168.100.11:8882/NSCS/mobile/index.jsp?resolution=";
    //public static String RESOLUTIONURL ="http:/"+NC_HOST+":8888/NSCS/mobile/index.jsp?resolution=";

    // 注册1
	public static String NC_REG_URL = "/msi/user_regist_req.do";
	
	// 登入
	public static String NC_ACCESS_URL = "/msi/user_access_req.do";
	public static String NC_LOGIN_URL = "/msi/user_login_req.do";
	
	// 绑定
	public static String NC_BIND_URL = "/msi/user_bind_req.do";
	
	// 点播
	public static String NC_VODPLAY_URL = "/msi/user_vodplay_req.do";
	
	// 解绑
	public static String NC_UNBIND_URL = "/msi/user_unbind_req.do";
	
	// 键值下发
	public static String NC_KEYSEND_URL = "/msi/key_send_req.do";
	
	// 选时播放
	public static String NC_CHOOSETIME_URL = "/msi/user_choosetime_req.do";
	
	// 状态查询
	public static String SESSIONQUERY_URL = "/msi/user_sessionquery_req.do";
	
}
