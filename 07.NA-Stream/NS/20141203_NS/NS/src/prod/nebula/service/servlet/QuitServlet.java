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

import prod.nebula.service.socket.QuitOverTimeClient;

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


	    String streamid = request.getParameter("streamid");
	    String username = request.getParameter("username");
		System.out.println("streamid="+streamid);
		System.out.println("username="+username);
		
		if(streamid!=null&&!"".equals(streamid)&&username!=null&&!"".equals(username)){
			QuitOverTimeClient c = new QuitOverTimeClient();
			  c.send(username);
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
