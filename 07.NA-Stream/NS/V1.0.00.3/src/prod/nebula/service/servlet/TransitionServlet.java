/**  
 * 类名称：TransitionServlet 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-8 下午08:10:37 
 */
package prod.nebula.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名称：TransitionServlet 类描述： 创建人：PengFei 创建时间：2014-10-8 下午08:10:37 备注：
 * 
 * @version
 * 
 */
public class TransitionServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory
			.getLogger(TransitionServlet.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public TransitionServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		System.out.println("destroy()");
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				
		PrintWriter out = response.getWriter();
		String thisState = request.getParameter("thisState");
		String username = request.getParameter("username");
		String read = request.getParameter("method");
	
		if ("2".equals(read)) {
	

		    response.setHeader("Access-Control-Allow-Origin","*");
			String key = username+"isplay";
			
			ServletContext application=this.getServletContext();   
	        //设置Application属性   		  
		
			
			String value = (String) application.getAttribute(key);
		//	logger.info(path + "state.txt 中内容是：" + sb.toString() + "长度是："
		//			+ sb.toString().length());

			if (value!=null&&!"".equals(value)) {
	           logger.debug("【NS-TransitionServlet】 2页面实时查找方法，因为value!=null， 页面将显示：‘即将播放’ ");
				application.removeAttribute(key);
				logger.debug("【NS-TransitionServlet】 application.removeAttribute(key) 已执行 ，所以页面不会再显示‘即将播放’");
				out.println("success");
				out.close();
			}else{
		//		logger.info("【TransitionServlet】 2页面实时查找方法， 查找key时已不存在，页面不会再显示‘即将播放’");
				out.println("error"); 
				out.close();
			}

		}

		if ("3".equals(read)) {
			 response.setHeader("Access-Control-Allow-Origin","*");
	//		logger.info("清空文件内容");
			ServletContext application=this.getServletContext(); 
			String key = username+"isplay";
			application.removeAttribute(key);

			String value = (String) application.getAttribute(key);
			if (value!=null&&!"".equals(value)) {

			logger.debug("【NS-TransitionServlet】3清除方法， 清除key，但没有清除成功，故页面还会出现‘即将播放’");
				
			} else {
				logger.debug("【NS-TransitionServlet】3清除方法， 清除key，清除成功，故页面不会出现‘即将播放’");

			}

		}

		if ("1".equals(thisState)) {
			 response.setHeader("Access-Control-Allow-Origin","*");
			String vodname = request.getParameter("vodname");
			String rstp = request.getParameter("rstp");
			//logger.info("【TransitionServlet】1设置方法，用户点击了播放按钮    在session中设置 key="+username+"isplay，用于让页面显示：’即将播放‘");
			logger.info("【NS-TransitionServlet】用户点击播放按钮，播放影片《"+vodname+"》，rstp="+rstp+"");
			ServletContext application=this.getServletContext();   
	        //设置Application属性   		  
		
			String key = username+"isplay";
			 application.setAttribute(key, key);
			 
			 try {
				Thread.sleep(5000);
				application.removeAttribute(key);
				logger.debug("【NS-TransitionServlet】5秒后自动 清除key，清除成功，故页面不会出现‘即将播放’");

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			out.close();

		}

	}

}
