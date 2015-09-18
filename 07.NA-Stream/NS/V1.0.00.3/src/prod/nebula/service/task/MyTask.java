package prod.nebula.service.task;

import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.dto.DatabaseQuary;

/**
 * 继承 定时器任务类
 * 
 */
public class MyTask extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(MyTask.class);

	public MyTask() {
		super();
	}

	/** 这个代表3点钟的时候执行任务 */
	//private static final int C_SCHEDULE_HOUR = 3;

	private static boolean isRunning = false;

	//private ServletContext context = null;

	public MyTask(ServletContext context) {
	//	this.context = context;
	}

	public void run() {
		
	
//		Calendar cal = Calendar.getInstance();
		if (!isRunning) {
		
		     Long start = System.currentTimeMillis();
		     DatabaseQuary dq = new DatabaseQuary();
		     dq.quaryAndupdate();
		     Long end = System.currentTimeMillis();
		     
		     System.out.println("查询更新 strNav_url   耗时 " +(end-start)+" ms");
		     
//		     
//			if (C_SCHEDULE_HOUR == cal.get(Calendar.HOUR_OF_DAY)) {
//				isRunning = true;
//				logger.info("开始执行指定任务");
//
//				/**
//				 * 此处写执行任务代码
//				 */
//			
//				// new YouCode().changeState();
//
//				isRunning = false;
				logger.info("指定任务执行结束");
		//	}
		} else {
			logger.info("上一次任务执行还未结束");
		}
	}
//  public static void main(String[] args) {
//	
//	  
//    Timer timer = new Timer();  
//      
//  
//
//      /** 
//       * 定时器到指定的时间时,执行某个操作(如某个类,或方法) 
//       */  
//      //后边最后一个参数代表监视器的监视周期,现在为一小时  
//      timer.schedule(new MyTask(), 0, 2000);  
//	  
//	  
//	  
//}
}