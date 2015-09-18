package prod.nebula.service.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.dto.UserManager;
import prod.nebula.service.dto.impl.UserManagerImpl;
import prod.nebula.service.util.PropertiesUtil;

public class SaveVodPageThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(SaveVodPageThread.class);
	
	
	// 手机二次登陆详情页url服务器
	static String vodserverIp = PropertiesUtil
			.readValue("vod.page.mysql.server.ip");
	static String voddatabase = PropertiesUtil
			.readValue("vod.page.mysql.database");

	// 查询的详情页服务器
	static String bindserverIp = PropertiesUtil
			.readValue("bind.mysql.server.ip");
	static String binddatabase = PropertiesUtil
			.readValue("bind.mysql.database");
    String username;
    String streamid;
    String vod_page;
	public SaveVodPageThread(String username, String streamid,String  vod_page){
		
		this.username = username;
		this.streamid = streamid;
		this.vod_page = vod_page;
		
		
	}
	
	
	public void run() {

		UserManager um = new UserManagerImpl();

		boolean flag = um.updateVodPage(username, streamid, vod_page);
		
		logger.debug("【NS-SaveVodPageThread】保存手机详情页到数据库线程执行……");

		logger.debug("【NS-SaveVodPageThread】保存手机详情页到【"+vodserverIp+"/"+voddatabase+"】 user_info表中");
		logger.debug("【NS-SaveVodPageThread】目的：为手机二次登陆访问（详情页）");
		
		if (flag) {
			logger.debug("【NS-SaveVodPageThread】同步的数据：" + vod_page);
			logger.info("【NS-SaveVodPageThread】备注：已将首次手机详情页同步到数据库中，作用为手机二次登陆访问（详情页）使用");
		} else {
        logger.info("【NS-SaveVodPageThread】备注：没有能够把手机详情页地址同步到数据库中，请检查数据库配置及传入的参数");
		//	logger.error("【NS-SaveVodPageThread】备注：没有能够把手机详情页同步到【" + vodserverIp + "】"+voddatabase+"的user_info表中");

		}

	}

}
