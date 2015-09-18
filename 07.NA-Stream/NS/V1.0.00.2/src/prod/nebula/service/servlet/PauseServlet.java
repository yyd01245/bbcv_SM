package prod.nebula.service.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.socket.PauseClient;
import prod.nebula.service.util.PropertiesUtil;

public class PauseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger =  LoggerFactory.getLogger(PauseServlet.class);

	/**
	 * Constructor of the object.
	 */
	public PauseServlet() {
		super();
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


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		 logger.info("【NS-PauseServlet】暂停超时：");
	    String streamid = request.getParameter("streamid");
	    String username = request.getParameter("username");
	
		 String  serverIp   = PropertiesUtil.readValue("sm.server.ip");
		 int     serverPort = Integer.parseInt(PropertiesUtil.readValue("sm.server.port"));
		
		
		
		if(isNumeric(streamid)&&username!=null&&!"".equals(username)){
			  PauseClient c = new PauseClient();
			  c.send(streamid,username);
			//  System.out.println("发送完毕，处理暂停超时页面");
			//  request.getRequestDispatcher("subinter2.jsp").forward(request, response);   
			
		}else{
			 logger.info("【NS-PauseServlet】由于必要参数streamid=【"+streamid+"】是非法值，故不能向SM服务器发送暂停超时报文");
			 
			 logger.info("【NS-PauseServlet】注：当前SM服务器地址为：【"+serverIp+":"+serverPort+"】");
			 
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
}
