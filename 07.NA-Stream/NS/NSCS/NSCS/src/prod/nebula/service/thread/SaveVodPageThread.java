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
		
		logger.info("线程同步执行……");
		logger.info("从【"+bindserverIp+"/"+binddatabase+"】 vodpage 表 中读取 vod_page_url同步到【"+vodserverIp+"/"+voddatabase+"】 user_info表中");
		logger.info("目的：为手机二次登陆访问（详情页）");
		
		if (flag) {

			logger.info("同步执行成功" + "同步的vod_page_url= " + vod_page);

		} else {
			logger.info("线程执行异常");
			logger.info(" 没有把vod_page_url同步到【" + vodserverIp + "】user_info表中");
		}

	}

}
