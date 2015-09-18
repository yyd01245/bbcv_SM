/**  
 * 类名称：ClearVodidServlet 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-11-6 上午11:10:53 
 */
package prod.nebula.service.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名称：ClearVodidServlet 类描述： 清除无用的kye 创建人：PengFei 创建时间：2014-11-6 上午11:10:53 备注：
 * 
 * @version
 * 
 */
public class ClearVodidServlet extends HttpServlet {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory
			.getLogger(ClearVodidServlet.class);

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

		// 用户彻底退出（主动解绑或被挤掉）：清除application中无用的key

		logger.info("【NS-ClearVodidServlet】用户彻底退出（主动解绑或被挤掉）：");

		String username = request.getParameter("username");

		logger.info("【NS-ClearVodidServlet】删除有关【" + username+ "】的application中的key值");

		String chickStateOldKey = username + "chickStateOld";
		String chickStateNowKey = username + "chickStateNow";
		String vodidkey = username + "vodidkey";
		String isplaykey = username + "isplay";

		ServletContext application = this.getServletContext();
		application.removeAttribute(vodidkey);
		application.removeAttribute(chickStateOldKey);
		application.removeAttribute(chickStateNowKey);
		application.removeAttribute(isplaykey);

		logger.info("【NS-ClearVodidServlet】清除完毕");

	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
