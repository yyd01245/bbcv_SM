package prod.nebula.service.servlet;

import java.io.IOException;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.task.MyTask;

public class UpdateStrNavUrlServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(UpdateStrNavUrlServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public UpdateStrNavUrlServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void init() throws ServletException {
      /** 
	       * 定时器到指定的时间时,执行某个操作(如某个类,或方法) 
	       */  
	      //后边最后一个参数代表监视器的监视周期,现在为一小时  
		//   Timer timer = new Timer();  
	     //  timer.schedule(new MyTask(), 0, 5000*60*60); 
	    //  logger.info("同步结束");
	}

}
