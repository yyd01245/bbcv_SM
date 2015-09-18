/**  
 * 类名称：DeletFileData 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-9 下午12:30:35 
 */
package prod.nebula.service.thread;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**    
 * 类名称：DeletApplicationSetValueThread   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-10-9 下午12:30:35    
 * 备注：   
 * @version    
 *    
 */
public class DeletApplicationSetValueThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(DeletApplicationSetValueThread.class);

	 ServletContext application ;
	 String key;
	 
	 
	 
	 
	 
	
	public String getKey() {
		return key;
	}




	public void setKey(String key) {
		this.key = key;
	}




	public ServletContext getApplication() {
		return application;
	}




	public void setApplication(ServletContext application) {
		this.application = application;
	}




	public void run() {
		// TODO Auto-generated method stub

		try {
			Thread.sleep(4000);
			
			application.removeAttribute(key);
			logger.info("【NS-DeletApplicationSetValueThread】：4秒已过，已经把用户按键全局变量删除，不会再跳到TV详情页了");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	 		
		
	 	 
	  
	}

}
