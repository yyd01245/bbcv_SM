package prod.nebula.commons.config;

/**
 * 系统固定参数配置
 * @author zhangdj
 *
 */
public class UcmsConfig {
	//用户未绑定频道状态
	public static int unbanding_status =1;
	public static String unbanding_message ="用户还未绑定频道...";
	public static String wec_unbanding_message ="user do not bind channel...";
	//用户已绑定频道但未点播
	public static int banding_status =2;
	public static String banding_message ="用户已绑定频道，频道号";
	public static String wec_banding_message ="user in bind status,the channel number is";
	//用户绑定频道且已点播
	public static int play_status =3;
	public static String play_message ="正在观看VOD内容,绑定本地频道号";
	public static String wec_play_message ="watching vod,the channel number is";
	//用户无效
	public static int user_unable_status =0;
	public static String user_unable_message ="用户无效...";
	public static String wec_user_unable_message ="user unable use";
	
	//首次登陆，默认手机门户页面
	public static String mainPage;
	
	//调度中心地址
	public static String sm_service_ip;
	
	//调度中心端口
	public static int sm_service_port;
	
	//调度中心端口
	public static int sm_service_udpport;
	
	//调度中心端口
	public static int sm_service_time;
	
	//VOD键值上传IP
	public static String VOD_KEY_IP;
	
	//本地监听端口
	public static String local_server_port;
	
	//VGW键值上传地址
	public static String VGW_KEY_SEND;
	
	//VOD键值上传PORT
	public static String VOD_KEY_PORT;
	
	//VGW服务地址
	public static String vgw_tcpserver_ip;
	//VGW服务端口
	public static int vgw_tcpserver_port;
	

	public static void setVgw_tcpserver_ip(String vgw_tcpserver_ip) {
		UcmsConfig.vgw_tcpserver_ip = vgw_tcpserver_ip;
	}

	public static void setVgw_tcpserver_port(int vgw_tcpserver_port) {
		UcmsConfig.vgw_tcpserver_port = vgw_tcpserver_port;
	}

	public static void setLocal_server_port(String local_server_port) {
		UcmsConfig.local_server_port = local_server_port;
	}

	public static String getVOD_KEY_IP() {
		return VOD_KEY_IP;
	}

	public static void setVOD_KEY_IP(String vOD_KEY_IP) {
		VOD_KEY_IP = vOD_KEY_IP;
	}
	
	public static String getVOD_KEY_PORT() {
		return VOD_KEY_PORT;
	}

	public static void setVOD_KEY_PORT(String vOD_KEY_PORT) {
		VOD_KEY_PORT = vOD_KEY_PORT;
	}

	public static void setSm_service_time(int sm_service_time) {
		UcmsConfig.sm_service_time = sm_service_time;
	}

	public static void setSm_service_ip(String sm_service_ip) {
		UcmsConfig.sm_service_ip = sm_service_ip;
	}

	public static void setSm_service_port(int sm_service_port) {
		UcmsConfig.sm_service_port = sm_service_port;
	}

	public static void setMainPage(String mainPage) {
		UcmsConfig.mainPage = mainPage;
	}
	
	public static void setSm_service_udpport(int sm_service_udpport) {
		UcmsConfig.sm_service_udpport = sm_service_udpport;
	}
	
	
	public enum COMMAND{
		get_session,stream_unbind,vod_over,quit_timeover
	}
}
