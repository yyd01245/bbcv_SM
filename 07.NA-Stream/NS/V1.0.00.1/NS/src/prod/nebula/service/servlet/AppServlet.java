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

import prod.nebula.service.thread.SaveVodPageThread;
import prod.nebula.service.util.PropertiesUtil;

/**
 * 类名称：AppServlet <br>
 * 类描述： 处理二维码扫描 Servlet<br>
 * 创建人：PengFei 创建时间：2014-10-23 下午03:10:06<br>
 * 备注：<br>
 * 
 * 此Servlet功能（作用）说明： <br>
 * 一 ，用户使用客户端扫描二维码后，访问此Servlet<br>
 *     1，在application中设置该用户扫描二维码所获取到的资源id即vodid;<br>
 *        然后TV导航页ajax实时查询会获得此id,然后页面就会跳转到TV详情页。<br>
 *     2，在application中设置两个不同的key,初始值相同。即：<br>
 *          String chickStateOldKey = username+"chickStateOld";<br>
 *          String chickStateNowKey =username+"chickStateNow"; <br>
 *      作用就是为了TV详情页的展示，每当用户在手机首页点击栏目时，<br>
 *      就改变了chickStateOldKey中的值，然后 TV详情页、TV等待页实时查询 会比较以上两个 key中的值，<br>
 *      如果不相同则说明用户发生了点击事件，如果此时电视正在显示TV详情页， 或等待页，那么就会跳转了<br> 
 * 二，用户使用非客户端扫描二维码后，则将跳转到下载客户端页面。<br>
 * 
 */
public class AppServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(AppServlet.class);
	private static final long serialVersionUID = 1L;

	public AppServlet() {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		/*
		 * 参数说明 username:用户名 id:流id vp:资源id(扫描获取的影片id) r:高标清标识
		 */
		String username = request.getParameter("username");
		String streamid = request.getParameter("id");
		String vp = request.getParameter("vp");
		String resolution = request.getParameter("r");

		logger.info("【NS-AppServlet】--------二维码Servlet开始执行--------");

		if (username != null) {
			logger.info("【NS-AppServlet】用户使用宽云客户端扫描二维码");
			logger.info("【NS-AppServlet】获取二维码中的参数信息：");
			logger.info("【NS-AppServlet】用户名(username)=" + username);
			logger.info("【NS-AppServlet】流ID(streamid)=" + streamid);
			logger.info("【NS-AppServlet】扫描的片花视频ID(vp)=" + vp);

			if ("0".equals(resolution)) {
				logger.info("【NS-AppServlet】频道分辨率(r)：高清");
			}
			if ("1".equals(resolution)) {
				logger.info("【NS-AppServlet】频道分辨率(r)：标清");
			}

			String vodidkey = username + "vodidkey";

			ServletContext application = this.getServletContext();

			String mobileDetailPage = PropertiesUtil
					.readValue("mobile.detail.url");

			try {

				if (mobileDetailPage != null && !"".equals(mobileDetailPage)) {

					application.setAttribute(vodidkey, vp);

					logger
							.info("【NS-AppServlet】在Application中设置用户扫描二维码资源id.key="
									+ vodidkey + ",id=" + vp);

					logger
							.debug("【NS-AppServlet】备注：在Application中设置用户扫描二维码资源是为了，导航页面跳转到TV详情页(此时需要知道要展示的资源信息的id)");

					// 设置用户的点击状态(取时间 代替)，两时间值value相同则说明无点击，否则，就是用户点击了
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMddHHmmssSSS");// 年月日时分秒毫秒
					String state = sdf.format(new Date());

					String chickStateOldKey = username + "chickStateOld";
					String chickStateNowKey = username + "chickStateNow";

					application.setAttribute(chickStateOldKey, state);
					application.setAttribute(chickStateNowKey, state);

					logger.debug("【NS-AppServlet】中chickStateOldKey="
							+ chickStateOldKey);
					logger.debug("【NS-AppServlet】中chickStateOldKey对应key值="
							+ state);
					logger.debug("【NS-AppServlet】中chickStateNowKey="
							+ chickStateNowKey);
					logger.debug("【NS-AppServlet】中chickStateNowKey对应key值=="
							+ state);

					String page = mobileDetailPage + "?assigntypeid=9&aid="
							+ vp + "&username=" + username + "&streamid="
							+ streamid + "&resolution=" + resolution;
					
			


					logger.info("【NS-AppServlet】手机跳转到手机详情页面：");
					if(page.indexOf("?")!=-1){
						String [] pages = page.split("\\?");
						logger.info("【NS-AppServlet】手机详情页地址：【" + pages[0] + "?");
						logger.info("【NS-AppServlet】"+ pages[1] + "】");
					}
					
			
					response.sendRedirect(page);
					
					
					
					SaveVodPageThread st = new SaveVodPageThread(username,streamid, page);
					new Thread(st).start();

				} else {
					logger.info("【NS-AppServlet】异常信息：");
					logger.info("【NS-AppServlet】 原因手机详情页没有获取到");
					String mobile_home = PropertiesUtil
							.readValue("mobile.home.url");
					logger.info("【NS-AppServlet】将强制手机页面转到默认页：" + mobile_home);
					response.sendRedirect(mobile_home);

				}

			} catch (Exception e) {
				logger.error("【NS-AppServlet】 执行出错", e.getMessage());
				PrintWriter out = response.getWriter();

				out.println("【NS-AppServlet】 error[" + e
						+ "]， Maybe , AppServlet 执行出错");
				out.flush();
				out.close();
			}

		} else {
			logger.info("【NS-AppServlet】非客户端扫描二维码");
			logger.info("【NS-AppServlet】非客户端扫描，将进入下载客户端页面");
			String url = PropertiesUtil.readValue("app.download.url");
			response.sendRedirect(url);
			logger.info("【NS-AppServlet】下载客户端页面地址：" + url);

			// request.getRequestDispatcher(url).forward(request, response);
			// logger.info("下载页面 ："+url);
			// PrintWriter out = response.getWriter();
			// out.println(url);http://www.pgyer.com/T2CY
			// logger.info("【NS-AppServlet】下载(IOS)页地址是：【NS-http://www.pgyer.com/T2CY】");
			// response.sendRedirect("http://pgyer.com/MsHG");
			// response.sendRedirect("http://www.pgyer.com/T2CY");
		}

	}

}
