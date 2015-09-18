/**
 * 
 */
package prod.nebula.vrc.config;

/**
 * VOD控制代理参数类
 * 
 * @author 严东军
 * 
 */
public class VODConst {

	/**
	 * VOD资源调度--播放VOD接入请求头信息
	 */
	public static final String VOD_CTRL_VOD_PLAY_REQ = "VOD_CTRL_VOD_PLAY_REQ";

	/**
	 * VOD资源调度--播放VOD接入响应头信息
	 */
	public static final String VOD_CTRL_VOD_PLAY_RESP = "VOD_CTRL_VOD_PLAY_RESP";

	/**
	 * VOD资源控制--VOD播放控制请求头信息
	 */
	public static final String VOD_CTRL_VOD_PLAY_CTRL_REQ = "VOD_CTRL_VOD_PLAY_CTRL_REQ";

	/**
	 * VOD资源控制--VOD播放控制响应头信息
	 */
	public static final String VOD_CTRL_VOD_PLAY_CTRL_RESP = "VOD_CTRL_VOD_PLAY_CTRL_RESP";

	/**
	 * VOD资源调度--播放结果返回请求头信息
	 */
	public static final String VOD_CTRL_VOD_RESULT_GET_REQ = "VOD_CTRL_VOD_RESULT_GET_REQ";

	/**
	 * VOD退出请求头
	 */
	public static final String BUSI_CTRL_EXIT_VOD_REQ = "BUSI_CTRL_EXIT_VOD_REQ";

	/**
	 * 业务调度VOD退出请求头信息
	 */
	public static final String VOD_CA_VOD_QUIT_REQ = "VOD_CA_VOD_QUIT_REQ";

	public static final String APP_ENV_CTL_KEY_ACC_REQ = "APP_ENV_CTL_KEY_ACC_REQ";

	/**
	 * 业务调度VOD退出响应头信息
	 */
	public static final String VOD_CA_VOD_PLAY_RESP = "VOD_CA_VOD_PLAY_RESP";

	/**
	 * 业务调度请求VOD退出-指令
	 */
	public static final String CSCG_COMMAND = "quit";

	/**
	 * VOD退出成功码
	 */
	public static final int VOD_QUITE_OK = 0;

	/**
	 * VOD退出失败码
	 */
	public static final int VOD_QUITE_FAIL = -1;

	/**
	 * 通知更新视频播放地址接口请求头
	 */
	public static final String VEDIO_ROUTE_UPDATE_REQ = "VEDIO_ROUTE_UPDATE_REQ";

	/**
	 * 通知更新视频播放地址接口响应头
	 */
	public static final String VEDIO_ROUTE_UPDATE_RESP = "VEDIO_ROUTE_UPDATE_RESP";

	/**
	 * 通知移除视频播放地址接口请求头
	 */
	public static final String VEDIO_ROUTE_FREE_REQ = "VEDIO_ROUTE_FREE_REQ";

	/**
	 * 通知移除视频播放地址接口响应头
	 */
	public static final String VEDIO_ROUTE_FREE_RESP = "VEDIO_ROUTE_UPDATE_RESP";

	/**
	 * 分割符
	 */
	public static final String SEPARATOR = "|";

	/**
	 * VOD资源调度--返回成功码
	 */
	public static final int RETURNCODE_OK = 0;

	/**
	 * VOD资源调度-返回错误码
	 */
	public static final int RETURNCODE_ERROR = -1;

	/****************** HID标准键值转换成10进制信息 *********************/
	/**
	 * 播放
	 */
	public static final String KEYVALUE_PLAY = "40";

	/**
	 * 暂停后继续播放
	 */
	public static final String KEYVALUE_RESUME = "43";

	/**
	 * 暂停
	 */
	public static final String KEYVALUE_PAUSE = "pause";

	/**
	 * 返回
	 */
	public static final String KEYVALUE_BACK = "158";

	/**
	 * 退出/停止
	 */
	public static final String KEYVALUE_EXIT = "41";

	/**
	 * 快进
	 */
	public static final String KEYVALUE_FAST = "8";

	/**
	 * 快退
	 */
	public static final String KEYVALUE_SLOW = "4";

	/**
	 * 选时间
	 */
	public static final String KEYVALUE_CHOOSETIME = "80";

	/**
	 *音量减
	 */
	public static final int KEYVALUE_LEFT = 0x0b;
	
	/**
	 * 音量加
	 */
	public static final int KEYVALUE_RIGHT = 0x06;

	/****************** HID标准键值转换成10进制信息 *********************/

	/**
	 * VOD播放接入后返回给业务调度的播放结果--播放成功
	 */
	public static final int PLAYTYPE_OK = 0;

	/**
	 * VOD播放接入后返回给业务调度的播放结果--播放失败
	 */
	public static final int PLAYTYPE_FAIL = -1;

	/**
	 * VOD资源类型
	 */
	public static final int RESTYPE_VOD = 3;

	/**
	 * VOD退出命令
	 */
	public static final String VOD_QUITE_COMMOND = "exit vod";

	/**
	 * 机顶盒类型
	 */
	public static final int STBTYPE = 1;

	/**
	 * 最大快进倍速
	 */
	public static final int SCALE_FAST_TIMES = 8;

	/**
	 * 最大快退倍速
	 */
	public static final int SCALE_SLOW_TIMES = -8;

	// ----------------异常码定义 start----------------
	// 正常
	public static final int VOD_OK = 0;

	// 内部错误
	public static final int INTERNAL_ERROR = -2201;

	// 请求参数异常
	public static final int REQMESSAGE_ERROR = -2202;

	// VOD通信模块交互异常
	public static final int VODC_ERROR = -2203;

	// VOD播放主流程异常
	public static final int PLAYVOD_ERROR = -2204;

	// ----------------异常码定义 end ----------------

}
