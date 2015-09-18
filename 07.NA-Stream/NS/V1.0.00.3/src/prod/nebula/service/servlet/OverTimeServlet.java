package prod.nebula.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.socket.OverTimeClient;
import prod.nebula.service.util.PropertiesUtil;

public class OverTimeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger =  LoggerFactory.getLogger(OverTimeServlet.class);

	/**
	 * Constructor of the object.
	 */
	public OverTimeServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
           this.doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		 logger.info("【NS-OverTimeServlet】等待超时：");
		
		PrintWriter out = response.getWriter();	
		String streamid = request.getParameter("streamid");
		 String  serverIp   = PropertiesUtil.readValue("sm.server.ip");
		 int     serverPort = Integer.parseInt(PropertiesUtil.readValue("sm.server.port"));
		
		if(isNumeric(streamid)&&!"".equals(streamid)){
			  OverTimeClient c = new OverTimeClient();
			  c.send(streamid);	    	
			  out.println("");
			  out.close();			
		}else{
			 logger.info("【NS-OverTimeServlet】由于必要参数streamid=【"+streamid+"】是非法值，故不能向SM服务器发送等待超时报文");
			 
			 logger.info("【NS-OverTimeServlet】注：当前SM服务器地址为：【"+serverIp+":"+serverPort+"】");
			 
		}
	}

	public static boolean isNumeric(String str){
		
		if(str==null){
			return false;
		}
		  for (int i = str.length();--i>=0;){   
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		  }
		  return true;
 }
	public void init() throws ServletException {
	}

}
