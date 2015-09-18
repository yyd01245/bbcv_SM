/**  
 * 类名称：DeletFileData 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-9 下午12:30:35 
 */
package prod.nebula.service.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.servlet.TransitionServlet;

/**    
 * 类名称：DeletFileData   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-10-9 下午12:30:35    
 * 备注：   
 * @version    
 *    
 */
public class DeletFileDataThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(DeletFileDataThread.class);
	/**
	/* (非 Javadoc)
	 * <p>Title: run</p>
	 * <p>Description: </p>
	 * @see java.lang.Runnable#run()
	 */
	
	
	
	public void run() {
		// TODO Auto-generated method stub

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  logger.info("线程中 ……清空文件内容");
		
	 	 String path =  TransitionServlet.class.getResource("").getPath();
	 	 File f = new File(path+"state.txt");
	 	 
	 	 FileWriter fw;
		try {
			fw = new FileWriter(f);
			
		 	if(f.exists()){

		 		  logger.info("要清空的文件存在！写\"\"");
		 		fw.write("");
		        FileReader fr = new FileReader(f);
				 
				 BufferedReader br = new BufferedReader(fr);
				 String s;
				StringBuffer sb = new StringBuffer();
				 while ((s = br.readLine()) != null) {
				 // System.out.println(s);		 
		           sb.append(s);
				 }
				 logger.info("清空后文件 内容是："+sb.toString()+"长度是："+sb.toString().length());
		 		fw.flush();
		 		fw.close();

		 	}else{
		 		  logger.info("要清空的文件不存在！");
		 	
		 	}
		 	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	//System.out.println(path);
	 
	 		
		
	 	 
	  
	}

}
