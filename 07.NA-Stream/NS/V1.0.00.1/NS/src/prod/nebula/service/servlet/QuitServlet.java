/**  
 * 类名称：QuitServlet 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-23 下午03:10:06 
 */
package prod.nebula.service.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.socket.QuitOverTimeClient;
import prod.nebula.service.util.PropertiesUtil;

/**    
 * 类名称：QuitServlet   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-10-23 下午03:10:06    
 * 备注：   
 * @version    
 *    
 */
public class QuitServlet extends HttpServlet {

	private static final Logger logger =  LoggerFactory.getLogger(QuitServlet.class);
	private static final long serialVersionUID = 1L;
	/**
	 * Constructor of the object.
	 */
	public QuitServlet() {
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
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		 logger.info("【NS-QuitServlet】退出超时：");
	 //   String streamid = request.getParameter("streamid");
	    String username = request.getParameter("username");
		 String  serverIp   = PropertiesUtil.readValue("z.server.ip");
		 int     serverPort = Integer.parseInt(PropertiesUtil.readValue("z.server.port"));
		
		
		 
		if(username!=null&&!"".equals(username)){
			
			QuitOverTimeClient c = new QuitOverTimeClient();
			  c.send(username);
	           }else{
	        	   
	       // 	 logger.info("streamid="+streamid);
	  
	  			 logger.info("【NS-QuitServlet】由于必要参数username=【"+username+"】不合法，故不能向【UCMS】服务器发送退出超时报文");
	  			 logger.info("【NS-QuitServlet】注：当前UCMS服务器地址为：【"+serverIp+":"+serverPort+"】");
	  			
	  			 
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
