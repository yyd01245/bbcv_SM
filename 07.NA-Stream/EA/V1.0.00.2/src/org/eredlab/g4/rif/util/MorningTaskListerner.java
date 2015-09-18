package org.eredlab.g4.rif.util;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eredlab.g4.ccl.properties.PropertiesFactory;
import org.eredlab.g4.ccl.properties.PropertiesFile;
import org.eredlab.g4.ccl.properties.PropertiesHelper;

public class MorningTaskListerner implements ServletContextListener {
	
	private Timer timer = null;
	private PropertiesHelper ph = PropertiesFactory.getPropertiesHelper(PropertiesFile.APP);
	 
	private static Log log = LogFactory.getLog(MorningTaskListerner.class);
	public void contextDestroyed(ServletContextEvent event) {
		if(timer!=null)
			timer.cancel();
	//	event.getServletContext().log("定时器已销毁");
		log.info("定时器已销毁");
	}

	public void contextInitialized(ServletContextEvent event) {
		Integer switchV = Integer.valueOf(ph.getValue("cums.timing.switch"));
		if(switchV == null || switchV == 0)
			return ;
		else if(switchV == 1){
			timer = new Timer(true);
		//	event.getServletContext().log("添加CUMS数据全量同步定时器已启动");
			log.info("添加CUMS数据全量同步定时器已启动");
			///timer.schedule(new myTask(event.getServletContext()), 0,60*60*1000);
		//	event.getServletContext().log("已经添加任务调度");
			log.info("已经添加任务调度");
		}
	}
	
//	public static class myTask extends TimerTask {
//		private static final int C_SCHEDULE_HOUR=0;
//		private static boolean isRunning = false;
//		private ServletContext context = null;
//		public myTask(ServletContext context){
//			this.context=context;
//		}		
//		public void run() {
//			Calendar cal = Calendar.getInstance();
//			System.out.println("当前小时："+cal.get(Calendar.HOUR_OF_DAY));
//			if(!isRunning){
//				if(C_SCHEDULE_HOUR==cal.get(Calendar.HOUR_OF_DAY)){
//					isRunning=true;
////					context.log("开始添加CUMS缓存全量同步事件");
//					log.info("开始添加CUMS缓存全量同步事件");
//					SQLCommand sqlCommand = new SQLCommand();
//					String event_id = IDHelper.getCodeID();
//					String event_idd = IDHelper.getAppAccessID();
//					String insertAllUserTask = "INSERT INTO CSS_USER_EVENT (event_id,cloud_user_id,oper_status,create_date)"
//						+"VALUES ("+event_id+",1,4,NOW())";
//					String insertCommonInfo = "INSERT INTO CSS_SERV_ADDRESSING_EVENT (event_id,addr_record_id,oper_status,create_date)"
//						+"VALUES ("+event_idd+",1,4,NOW())";
//					try {
//						sqlCommand.execInsert(insertAllUserTask);
//						sqlCommand.execInsert(insertCommonInfo);
//						isRunning=false;
////						context.log("CUMS缓存全量同步事件添加结束");
//						log.info("CUMS缓存全量同步事件添加结束");
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//			}else{
////				context.log("上一次任务执行还未结束");
//				log.info("上一次任务执行还未结束");
//			}
//		}
	//}
}
