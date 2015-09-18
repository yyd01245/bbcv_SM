
package servlet.vodRes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.Ad;

import dto.VodResService;

/**   
 * ResUpDownLoad   
 * 文件上传下载 
 * PengFei   
 * 2014-10-16 
 *
 * @version    
 *    
 */
public class ResUpDownLoad extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(ResUpDownLoad.class);

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ResUpDownLoad() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	   this.doPost(request, response);
	}

	/**
	 * 
	 * @author Administrator
	 * 文件上传
	 * 具体步骤：
	 * 1）获得磁盘文件条目工厂 DiskFileItemFactory 要导包
	 * 2） 利用 request 获取 真实路径 ，供临时文件存储，和 最终文件存储 ，这两个存储位置可不同，也可相同
	 * 3）对 DiskFileItemFactory 对象设置一些 属性
	 * 4）高水平的API文件上传处理  ServletFileUpload upload = new ServletFileUpload(factory);
	 * 目的是调用 parseRequest（request）方法  获得 FileItem 集合list ，
	 *     
	 * 5）在 FileItem 对象中 获取信息，   遍历， 判断 表单提交过来的信息 是否是 普通文本信息  另做处理
	 * 6）
	 *    第一种. 用第三方 提供的  item.write( new File(path,filename) );  直接写到磁盘上
	 *    第二种. 手动处理  
	 *
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	     String flag =  request.getParameter("flag");
	     String restype =  request.getParameter("restype");
	     
	     
	     logger.info("flag="+flag);
	     logger.info("restype="+restype);
	     
	     
	     if("upload".equals(flag)){
	    	 
	    	 request.setCharacterEncoding("utf-8");	//设置编码
	 		
	 		//获得磁盘文件条目工厂
	 		DiskFileItemFactory factory = new DiskFileItemFactory();
	 		//获取文件需要上传到的路径
	 		String path = request.getRealPath("/res");
	 		System.out.println("path="+path);
	 		 logger.info("path="+path);

	 		//如果没以下两行设置的话，上传大的 文件 会占用 很多内存，
	 		//设置暂时存放的 存储室 , 这个存储室，可以和 最终存储文件 的目录不同
	 		/**
	 		 * 原理 它是先存到 暂时存储室，然后在真正写到 对应目录的硬盘上， 
	 		 * 按理来说 当上传一个文件时，其实是上传了两份，第一个是以 .tem 格式的 
	 		 * 然后再将其真正写到 对应目录的硬盘上
	 		 */
	 		factory.setRepository(new File(path));
	 		//设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
	 		factory.setSizeThreshold(1024*1024) ;
	 		
	 		//高水平的API文件上传处理
	 		ServletFileUpload upload = new ServletFileUpload(factory);
	 		
	 		
	 		try {
	 			//可以上传多个文件
	 			List<FileItem> list = (List<FileItem>)upload.parseRequest(request);
	 			
	 			for(FileItem item : list)
	 			{
	 				//获取表单的属性名字
	 				String name = item.getFieldName();
	 				
	 				
	 				if(item.isFormField())
	 				{					
	 					//如果获取的 表单信息是普通的 文本 信息
	 					String value = item.getString() ;
	 					
	 					request.setAttribute(name, value);
	 				}
	 				//对传入的非 简单的字符串进行处理 ，比如说二进制的 图片，电影这些
	 				else
	 				{
	 					/**
						 * 以下三步，主要获取 上传文件的名字
						 */
						//获取路径名
						String value = item.getName() ;
						//索引到最后一个反斜杠
						int start = value.lastIndexOf("\\");
						//截取 上传文件的 字符串名字，加1是 去掉反斜杠，
	 					String filename = value.substring(start+1);
	 					String filename1 = value.substring(start+1);
	 					System.out.println("filename="+filename);
	 					logger.info("filename="+filename);
	 					String type = filename.split("\\.")[filename.split("\\.").length-1];
	 					String realPath ="";
	 					logger.info("type="+type);
	 					
	 			        if("adres".equals(restype)) {
	 			        	
	 			        	if("mp4".equals(type)||"MP4".equals(type)){
		 						   realPath = path+File.separator+"ads";
		 					}
	 			        	  
	 			        	
	 			        }
	                   if("vodres".equals(restype)) {
	                	   
		 					if("mp4".equals(type)||"MP4".equals(type)){
		 						   realPath = path+File.separator+"video";
		 					}
		 					else	if("jpg".equals(type)||"jpeg".equals(type)||"JPG".equals(type)||"JPEG".equals(type)||"png".equals(type)||"PNG".equals(type)||"gif".equals(type)||"GIF".equals(type)){
		 						 realPath = path+File.separator+"images";
		 					}
		 					
		 					else{
		 						 realPath = path+File.separator+"other";
		 					}
		 					
		 					request.setAttribute(name, filename);
		 					
		 				
		 				
	 			        	
	 			        	
	 			        }
	 			        

	                   File dir = new File(realPath);
	                   if (!dir.exists()) {
 						   dir.mkdir();
 						  }
	               	
	 					File f = new File(realPath+File.separator+filename);
	 					
	 					
	 					
	 					if(f.exists()){
	 						
	 						 
	 				String name1=	  filename.replace("."+type,"(副本)."+type);
	 				logger.info("name1="+name1);
	 				
	 			      filename =name1;
	 					}
	 					
	 				
	 					OutputStream out = new FileOutputStream(new File(realPath,filename));
	 					
	 					InputStream in = item.getInputStream() ;
	 					
	 					int length = 0 ;
	 					byte [] buf = new byte[1024] ;
	 			
	 					logger.info("上传文件大小"+item.getSize());
	 				
	 					while( (length = in.read(buf) ) != -1)
	 					{
	 					
	 						out.write(buf, 0, length);
	 					    
	 					}
	 					
	 					in.close();
	 					out.close();
	 					
	 			 		VodResService vrs = new VodResService();
	 			 		Ad ad = new Ad();
	 			 		
	 			 		ad.setName(filename1.split("\\.")[0]);
	 			 		
	 			 		String d = "";
	 					if("mp4".equals(type)||"MP4".equals(type)){
	 						   d = "res/video";
	 					}
	 					else	if("jpg".equals(type)||"jpeg".equals(type)||"JPG".equals(type)||"JPEG".equals(type)||"png".equals(type)||"PNG".equals(type)||"gif".equals(type)||"GIF".equals(type)){
	 						 d = "res/images";
	 					}
	 					
	 					else{
	 						 d = "res/other";
	 					}
	 			 		
	 					
	 			 		
	 			 		String p = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	 			 		ad.setAdVideoUrl(p+d+"/"+filename);
	 			 		
	 			 		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 			 		ad.setUpData(sd.format(new Date()));
	 			 	//	ad.setPauseUsed("0");
	 			 	//	ad.setWaitUsed("0");
	 			 		ad.setState("0");
	 			 	//	ad.setResolution("3");
	 			 		
	 			 		vrs.add(ad);
	 				}
	 			}
	 			
	 			
	 			
	 		} catch (FileUploadException e) {
	 			logger.info("文件上传出错");
	 			// TODO Auto-generated catch block
	 			request.getRequestDispatcher("/apps/vod_res_upload.jsp?info=error").forward(request, response);
	 			e.printStackTrace();
	 		}
	 		catch (Exception e) {
	 			logger.info("文件上传出错");
	 			// TODO Auto-generated catch block
	 		
	 			request.getRequestDispatcher("/apps/vod_res_upload.jsp?info=error").forward(request, response);
	 			e.printStackTrace();
	 		}
	 		

	 		
	 		
	 		request.getRequestDispatcher("/apps/vod_res_upload.jsp?info=success").forward(request, response);
	 		
         
	 	}

	  if("download".equals(flag)){   
		  
		  
		  String filePath = java.net.URLDecoder.decode(request.getParameter("fileName"),"UTF-8");//后台转编码一次成中文
		  
		  String path = this.getServletConfig().getServletContext().getRealPath("/");
		  String type = filePath.split("\\.")[filePath.split("\\.").length-1];
		  
		 
		  System.out.println("filePath="+filePath);
		  System.out.println("path="+path);
		  System.out.println("type="+type);
		  System.out.println("File.separator="+File.separator);
		  System.out.println("File.pathSeparator="+File.pathSeparator);
		  String fileName = filePath.split("/")[filePath.split("/").length-1];
		  
		  
		  String realPath = "";
			if("mp4".equals(type)||"MP4".equals(type)){
				   realPath = path+"res"+File.separator+"video";
			}
			else	if("jpg".equals(type)||"jpeg".equals(type)||"JPG".equals(type)||"JPEG".equals(type)||"png".equals(type)||"PNG".equals(type)||"gif".equals(type)||"GIF".equals(type)){
				 realPath = path+"res"+File.separator+"images";
			}
			
			else{
				 realPath = path+"res"+File.separator+"other";
			}
		  
			
			File file = new File(realPath+File.separator+ fileName);
			response.setContentType("application/x-msdownload");
			response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859_1"));//保证另存为文件名为中文
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			byte[] b = new byte[1024];
			long k = 0;

			OutputStream out = response.getOutputStream();
			while (k < file.length()) {
				int j = bis.read(b, 0, 1024);
				k += j;
				out.write(b, 0, j);
			}
			out.close();
			bis.close();
			fis.close();
	  }
	      
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
