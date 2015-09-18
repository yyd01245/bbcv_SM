/**  
 * 类名称：ShowVodInfo 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-15 下午08:21:48 
 */
package prod.nebula.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.thread.DeletApplicationSetValueThread;
import prod.nebula.service.util.PropertiesUtil;

/**
 * 类名称：ShowVodInfo 类描述： 控制详情页展示 创建人：PengFei 创建时间：2014-10-15 下午08:21:48 备注：
 * TV详情页与等待页及用户点击首页栏目时，之间状态查询，然后实现页面的跳转
 * 
 * @version
 * 
 */
public class ShowVodInfo extends HttpServlet {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory
			.getLogger(SkipServlet.class);

	/**
	 * Constructor of the object.
	 */
	public ShowVodInfo() {

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

		String flag = request.getParameter("flag");

		if ("getnow".equals(flag)) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setCharacterEncoding("UTF-8");

			logger.debug("【NS-ShowVodInfo】getnow实时查询：");

			String username = request.getParameter("username");
			String streamid = request.getParameter("streamid");
			String resolution = request.getParameter("resolution");

			String chickStateOldKey = username + "chickStateOld";
			String chickStateNowKey = username + "chickStateNow";

			logger.debug("【NS-ShowVodInfo】getnow参数username=" + username);
			logger.debug("【NS-ShowVodInfo】getnow参数streamid=" + streamid);
			logger.debug("【NS-ShowVodInfo】getnow参数resolution=" + resolution);

			logger.debug("【NS-ShowVodInfo】getnow参数chickStateOldKey="
					+ chickStateOldKey);
			logger.debug("【NS-ShowVodInfo】getnow参数chickStateNowKey="
					+ chickStateNowKey);

			String key = username + "vodid";

			ServletContext application = this.getServletContext();
			String vodid = (String) application.getAttribute(key);

			// 得到之前的状态
			String chickStateOldValue = (String) application
					.getAttribute(chickStateOldKey);

			logger.debug("【NS-ShowVodInfo】之前chickStateOldValue="
					+ chickStateOldKey);
			// String chickStateOldValue = jedis.get(chickStateOldKey);
			// 得到当前的状态
			String chickStateNowValue = (String) application
					.getAttribute(chickStateNowKey);
			logger.debug("【NS-ShowVodInfo】之后chickStateOldValue="
					+ chickStateOldKey);

			logger.debug("【NS-ShowVodInfo】getnow中chickStateOldValue="
					+ chickStateOldValue);
			logger.debug("【NS-ShowVodInfo】getnow中chickStateNowValue="
					+ chickStateNowValue);
			PrintWriter out = response.getWriter();

			if (chickStateOldValue == null || chickStateNowValue == null) {
				logger
						.debug("chickStateOldValue=null,chickStateNowValue=null,即两者没有在AppServlet中初始化设");
				chickStateOldValue = "1";
				chickStateNowValue = "1";
			}
			// logger.info("ShowVodInfo《getnow》方法中---------》两者不等吗：="+(!chickStateOldValue.equals(chickStateNowValue)));
			// 如果两者不一致，则说明该用户触发了影片信息
			if (!chickStateOldValue.equals(chickStateNowValue)) {
				// 返回新的影片信息，并让TV详情页 显示

				// 让两者的值恢复一致，即状态一致。

				application.setAttribute(chickStateOldKey, chickStateNowValue);

				String tv_detail = PropertiesUtil.readValue("tv.detail.url")
						+ "?assigntypeid=5&aid=" + vodid + "&username="
						+ username + "&" + "streamid=" + streamid + "&vodid="
						+ vodid + "&resolution=" + resolution;
				out.println(tv_detail);

				out.flush();
				out.close();
			} else {
				// 不做处理

				out.println("error");
				out.flush();
				out.close();

			}

		}

		if ("get".equals(flag)) {
			response.setHeader("Access-Control-Allow-Origin", "*");
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			// System.out.println("get 请求中……");
			logger.debug("【NS-ShowVodInfo】get实时查询：");
			String username = request.getParameter("username");
			String streamid = request.getParameter("streamid");
			String resolution = request.getParameter("resolution");

			String chickStateOldKey = username + "chickStateOld";
			String chickStateNowKey = username + "chickStateNow";

			logger.debug("【NS-ShowVodInfo】get参数username=" + username);
			logger.debug("【NS-ShowVodInfo】getnow参数streamid=" + streamid);
			logger.debug("【NS-ShowVodInfo】get参数resolution=" + resolution);

			logger.debug("【NS-ShowVodInfo】get参数chickStateOldKey="
					+ chickStateOldKey);
			logger.debug("【NS-ShowVodInfo】get参数chickStateNowKey="
					+ chickStateNowKey);

			String key = username + "vodid";

			ServletContext application = this.getServletContext();
			// 设置Application属性

			String vodid = (String) application.getAttribute(key);

			// logger.info("ShowVodInfo《get》方法中---------》vodid="+vodid);
			// 获取该用户的点击状态，是否点击，如果点击了就跳转

			// 得到之前的状态值

			String chickStateOldValue = (String) application
					.getAttribute(chickStateOldKey);
			// 得到当前的状态值
			String chickStateNowValue = (String) application
					.getAttribute(chickStateNowKey);

			PrintWriter out = response.getWriter();

			if (chickStateOldValue == null || chickStateNowValue == null) {
				logger.info("【NS-ShowVodInfo】TV详情页跳转预检测，有值为null  故不会跳转");
				chickStateOldValue = "1";
				chickStateNowValue = "1";
			}
			// logger.info("ShowVodInfo《get》方法中---------》两者不等吗：="+(!chickStateOldValue.equals(chickStateNowValue)));
			// 如果两者不一致，则说明该用户触发了影片信息
			if (!chickStateOldValue.equals(chickStateNowValue)) {

				// 让两者的值恢复一致，即状态一致。
				application.setAttribute(chickStateOldKey, chickStateNowValue);
				// System.out.println("ShowVodInfo《get》方法中---------页面将从等待也转向tv详情页");

				// 获取该资源后，跳转到详情页面。

				String tv_detail = PropertiesUtil.readValue("tv.detail.url")
						+ "?assigntypeid=5&aid=" + vodid + "&username="
						+ username + "&" + "streamid=" + streamid + "&vodid="
						+ vodid + "&resolution=" + resolution;
				out.println(tv_detail);
				out.flush();
				out.close();
			} else {
				// 不做处理
				out.println("error");
				out.flush();
				out.close();

			}

		}
		if ("set".equals(flag)) {
			logger.info("【NS-ShowVodInfo】用户在手机首页点击栏目，将要显示资源详情");

			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			String username = request.getParameter("username");
			String streamid = request.getParameter("streamid");
			String resolution = request.getParameter("resolution");
			String vodid = request.getParameter("vodid");
			String bannername = request.getParameter("bannername");
			String chickStateNowKey = username + "chickStateNow";
			String key = username + "vodid";

			logger.info("【NS-ShowVodInfo】getnow参数username=" + username);
			logger.info("【NS-ShowVodInfo】getnow参数streamid=" + streamid);
			logger.info("【NS-ShowVodInfo】getnow参数resolution=" + resolution);

			logger.info("【NS-ShowVodInfo】getnow参数vodid=" + vodid);
			logger.info("【NS-ShowVodInfo】getnow参数bannername=" + bannername);
			logger.info("【NS-ShowVodInfo】getnow参数chickStateNowKey="
					+ chickStateNowKey);
			logger.info("【NS-ShowVodInfo】getnow参数key=" + key);

			ServletContext application = this.getServletContext();
			// 设置Application属性

			application.setAttribute(key, vodid);

			// 用户点击后……
			// 更新key=chickStateNowKey的值，更新后chickStateNowKey！=chickStateOldKey的值，则标示用户点击事件。
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");// 年月日时分秒毫秒
			String state = sdf.format(new Date());

			// jedis.set(chickStateNowKey, state);
			application.setAttribute(chickStateNowKey, state);
			// 通过value 查找资源。
			// 根据id.查询用户扫描的资源的具体信息

			DeletApplicationSetValueThread da = new DeletApplicationSetValueThread();
			da.setApplication(application);
			da.setKey(chickStateNowKey);

			new Thread(da).start();

			request.setAttribute("bannername", bannername);

			String mobile_detail = PropertiesUtil
					.readValue("mobile.detail.url")
					+ "?aid="
					+ vodid
					+ "&assigntypeid=9&username="
					+ username
					+ "&streamid=" + streamid + "&resolution=" + resolution;

			logger.info("【NS-ShowVodInfo】手机详情定位地址:" + mobile_detail);

			response.sendRedirect(mobile_detail);

		}

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
