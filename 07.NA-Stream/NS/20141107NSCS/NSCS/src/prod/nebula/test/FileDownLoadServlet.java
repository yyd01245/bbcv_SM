package prod.nebula.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class FileDownLoadServlet extends HttpServlet{
	private static final Logger logger = LoggerFactory.getLogger(FileDownLoadServlet.class);
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path=request.getParameter("filename");
        path = "apk";
        path=new String(path.getBytes("ISO-8859-1"),"utf-8");
        download(path,request,response);
    }
    
    public HttpServletResponse download(String path,HttpServletRequest request, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
          //  File file = new File(request.getRealPath("/")+"/"+path);
        	 String sys=request.getParameter("phoneSystem");
        	 
        	  logger.info("手机扫描，手机系统为："+sys);
        	  
        	 File file =null;
        	 if("ios".equals(sys)){
        		    logger.info("手机系统为："+sys+" 下载   D://key.app");
        			 file = new File("D://key.app");
        	 }
        	 if("android".equals(sys)){
        		    logger.info("手机系统为："+sys+" 下载   D://Android.apk");
        			 file = new File("D://Android.apk");
        	 }
        	 
        
        	
        	
        
        	
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            //String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("utf-8"),"ISO-8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }
}