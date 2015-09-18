/**  
 * ����ƣ�FilePath 
 * �������� 
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-10-13 ����08:21:42 
 */
package data;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.PropertiesUtil;

/**   
 * FilePath   
 * 
 *PengFei   
 * 2014-10-1308:21:42   
 * 
 * @version    
 *    
 */
public class FileManager {
	private static final Logger logger = LoggerFactory.getLogger(FileManager.class);
	
	private String contentsFirst = PropertiesUtil.readValue("server.data.file.contents.first"); //数据文件一级目录
	private String contentsSecond = PropertiesUtil.readValue("server.data.file.contents.second");//数据文件二级目录
	
	private String contents = contentsFirst+File.separator+contentsSecond+File.separator;
	
	private String  SdTvDataFile   =contents+PropertiesUtil.readValue("server.sd.tv.data.file.name"); // 目录下标清Tv导航页面中使用的数据文件名
	private String  SdTvDataFile1   =contents+PropertiesUtil.readValue("server.sd.tv.data.file1.name"); // 目录下标清Tv导航页面中使用的数据文件名(模板vod111.jsp)
	private String  HdTvDataFile   = contents+PropertiesUtil.readValue("server.hd.tv.data.file.name"); // 目录下高清Tv导航页面中使用的数据文件名
	private String  HdTvDataFile1   = contents+PropertiesUtil.readValue("server.hd.tv.data.file1.name"); // 目录下高清Tv导航页面中使用的数据文件名(模板vod111.jsp)

	
	
	
	private String  SdMbDataFile   = contents+PropertiesUtil.readValue("server.sd.mb.data.file.name"); // 目录下标清手机首页页面中栏目使用的数据文件名
	private String  HdMbDataFile   = contents+PropertiesUtil.readValue("server.hd.mb.data.file.name"); // 目录下高清手机首页页面中栏目使用的数据文件名
	
	
	private String  SdMbSpDataFile   = contents+PropertiesUtil.readValue("server.sd.mb.sp.data.file.name"); // 目录下标清手机首页页面中跑马灯（流动海报）使用的数据文件名
	private String  HdMbSpDataFile   = contents+PropertiesUtil.readValue("server.hd.mb.sp.data.file.name"); // 目录下高清手机首页页面中跑马灯（流动海报）使用的数据文件名
	
	
	
	
	
	
	/**

	* Tv导航数据文件

	* @param path 表示操作文件的路径

	* @param resolution 表示操作对应的高标清文件

	* @return 返回数据文件

	*/
	public File getTvDataFile(String path,String resolution){
			
		String str=null;
		
		if("1".equals(resolution)){
		
			 logger.info("TV resolution 标清");
			 
			 str = path+File.separator+SdTvDataFile;
		}
		else if("0".equals(resolution)){
			
			logger.info("TV resolution 高清");
			
			 str = path+File.separator+HdTvDataFile;
		}
		else{
			logger.info("TV resolution 未知分辨率");
		}
	
	    logger.info("Tv 路径是："+str);	  
	    
	    
	    File file = new File(str);

		    if (!file.exists()) {   
	            try {
	            	file.createNewFile();
				} catch (IOException e) {
					
					logger.info("【未找到文件，创建文件失败】 文件："+str);
					e.printStackTrace();
				}   
		    }

	    
	    return 	file;
	}

	
	public File getTvDataFile1(String path,String resolution){
		
		//resolution="0";
		
		String str=null;
		
		if("1".equals(resolution)){
		
			 logger.info("TV resolution 标清");
			 
			 str = path+File.separator+SdTvDataFile1;
		}
		else if("0".equals(resolution)){
			
			logger.info("TV resolution 高清");
			
			 str = path+File.separator+HdTvDataFile1;
		}
		else{
			logger.info("TV resolution 未知分辨率");
		}
	
	    logger.info("Tv 路径是："+str);	  
	    
	    
	    File file = new File(str);

		    if (!file.exists()) {   
	            try {
	            	file.createNewFile();
				} catch (IOException e) {
					
					logger.info("【未找到文件，创建文件失败】 文件："+str);
					e.printStackTrace();
				}   
		    }

	    
	    return 	file;
	}

	/**

	* 手机首页栏目数据文件

	* @param path 表示操作文件的路径

	* @param resolution 表示操作对应的高标清文件

	* @return 返回数据文件

	*/
	public File getMbDataFile(String path,String resolution){
	
		String str=null;
		
		if("1".equals(resolution)){
		
			 logger.info("Mb resolution 标清");
			 
			 str = path+File.separator+SdMbDataFile;
		}
		else if("0".equals(resolution)){
			
			logger.info("Mb resolution 高清");
			
			 str = path+File.separator+HdMbDataFile;
		}
		else{
			logger.info("Mb resolution 未知分辨率");
		}
	
	    logger.info("Mb 路径是："+str);	
	    File file = new File(str);

	    if (!file.exists()) {   
            try {
            	file.createNewFile();
			} catch (IOException e) {
				
				logger.info("【未找到文件，创建文件失败】 文件："+str);
				e.printStackTrace();
			}   
	    }

    
    return 	file;
	
	
	}
	
	
	/**

	* 手机首页跑马灯（流动海报）数据文件

	* @param path 表示操作文件的路径

	* @param resolution 表示操作对应的高标清文件

	* @return 返回数据文件

	*/
	public File getMbSpDataFile(String path,String resolution){
		
		String str=null;
		
		if("1".equals(resolution)){
		
			 logger.info("Mb resolution 标清");
			 
			 str = path+File.separator+SdMbSpDataFile;
		}
		else if("0".equals(resolution)){
			
			logger.info("Mb resolution 高清");
			
			 str = path+File.separator+HdMbSpDataFile;
		}
		else{
			logger.info("Mb resolution 未知分辨率");
		}
	
	    logger.info("Mb 路径是："+str);	
	    File file = new File(str);

	    if (!file.exists()) {   
            try {
            	file.createNewFile();
			} catch (IOException e) {
				
				logger.info("【未找到文件，创建文件失败】 文件："+str);
				e.printStackTrace();
			}   
	    }

    
    return 	file;
	
	}
  

}
