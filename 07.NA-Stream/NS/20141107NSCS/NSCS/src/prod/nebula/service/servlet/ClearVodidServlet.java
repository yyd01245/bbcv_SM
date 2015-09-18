/**  
 * 类名称：ClearVodidServlet 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-11-6 上午11:10:53 
 */
package prod.nebula.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**    
 * 类名称：ClearVodidServlet   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-11-6 上午11:10:53    
 * 备注：   
 * @version    
 *    
 */
public class ClearVodidServlet extends HttpServlet {

	/**
	 *  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ClearVodidServlet() {
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

		
		String username	= request.getParameter("username");	
		
		 String vodidkey = username+"vodidkey";
	     
		    
	        String chickStateOldKey = username+"chickStateOld";
	        String chickStateNowKey = username+"chickStateNow";
	        
	        String key = username+"vodid";

		 ServletContext application=this.getServletContext();   
	     
	     application.removeAttribute(vodidkey);
	     application.removeAttribute(chickStateOldKey);
	     application.removeAttribute(chickStateNowKey);
	     application.removeAttribute(key);
	     System.out.println("执行完毕");
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
