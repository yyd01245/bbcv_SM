/**  
 * 类名称：CreateData 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-21 下午12:57:53 
 */
package prod.nebula.service.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**    
 * 类名称：CreateData   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-10-21 下午12:57:53    
 * 备注：   
 * @version    
 *    
 */
public class CreateData {
   
	private static final Logger logger = LoggerFactory.getLogger(CreateData.class);
	     public static void createMoviceData1(String path,String content) throws IOException{
	    	 
	    	 
	    	 File f = new File(path); 
	    	  
	         if (!f.exists()) {
	        	 logger.info("文件不存在，已创建文件："+path);
		            f.createNewFile();   
		        }   
		        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");   
		        BufferedWriter writer=new BufferedWriter(write);     
		        writer.write(content);   
		        writer.close(); 
	    	 
	    	 
	    	 
	     }
	
}
