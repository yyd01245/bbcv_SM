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

/*
 * 处理二维码扫描   Servlet
 * 
 * 
 */
public class AppServlet extends HttpServlet {
	
	private static final Logger logger = LoggerFactory.getLogger(AppServlet.class);
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
		 * 参数说明
		 * username:用户名
		 * id:流id
		 * vp:资源id(扫描获取的影片id)
		 * r:高标清标识
		 */
		String username	= request.getParameter("username");		
		String streamid = request.getParameter("id");
		String vp = request.getParameter("vp");
		String resolution = request.getParameter("r"); 
		
	   logger.info("【NS-AppServlet】--------开始执行--------");

	 
		if(username!=null){
			   logger.info("【NS-AppServlet】用户使用宽云客户端扫描二维码");
			   logger.info("【NS-AppServlet】获取相关信息：");
			   logger.info("【NS-AppServlet】用户名(username) =" + username);  
			   logger.info("【NS-AppServlet】流ID(streamid) =" + streamid); 
			   logger.info("【NS-AppServlet】扫描的视频ID(vp) =" + vp);
			   
			   if("0".equals(resolution)){
				   logger.info("【NS-AppServlet】频道分辨率(r) ：高清"); 
			   }
			   if("1".equals(resolution)){
				   logger.info("【NS-AppServlet】频道分辨率(r) ：标清"); 
			   }
			    
			 
			 // 在缓存中设置该用户扫描所获取到的vodid;
			 String vodidkey = username+"vodidkey";
			 
		
			 ServletContext application=this.getServletContext();   
		        //设置Application属性   
		
			 
			 //http://218.108.50.246/bbcvcms/uploads/plus/view.php?aid=29&assigntypeid=9 
			 
			//  RcmManager rm = new RcmManager();
			  
			  
			//  String mobileDetailPage = rm.getMbDetailPage().getUrl();
			  
			 String mobileDetailPage ="http://218.108.50.246/bbcvcms/uploads/plus/view.php";
			 
			  try {
				  
					if(mobileDetailPage!=null&&!"".equals(mobileDetailPage)){

				        application.setAttribute(vodidkey, vp);  
		 

				    logger.info("【NS-AppServlet】在Application设置扫描资源id.key="+vodidkey+",id="+vp);			   		

					 
					//设置用户的点击状态(取时间 代替)，两时间值value相同则说明无点击，否则，就是用户点击了				    
				    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");//年月日时分秒毫秒
			        String state =   sdf.format(new Date());
				    
			        String chickStateOldKey = username+"chickStateOld";
			        String chickStateNowKey = username+"chickStateNow";
			        
			        application.setAttribute(chickStateOldKey, state);  
			        application.setAttribute(chickStateNowKey, state);  
			        
//				    logger.info("【NS-AppServlet】中---------》chickStateOldKey="+chickStateOldKey);
//				    logger.info("【NS-AppServlet】中---------》getchickStateOldKey="+getchickStateOldKey);				    
//				    logger.info("【NS-AppServlet】中---------》chickStateNowKey="+chickStateNowKey);
//				    logger.info("【NS-AppServlet】中---------》getchickStateNowKey="+getchickStateNowKey);

						
						
						  
				    String page = mobileDetailPage+"?name="+vp+"&username="+username+"&streamid="+streamid+"&resolution="+resolution;
				    
				    logger.info("【NS-AppServlet】手机跳转到vod详情页面：");
				    logger.info("【NS-AppServlet】页面："+page);
				    
				//    VodResourceInfo vod =   rm.getVod(vp);				    
				 //   request.getSession().setAttribute("vod", vod);
				    
				    
					//	request.getRequestDispatcher(page).forward(request, response);
					//	  response.sendRedirect("http://192.168.100.11/multiscreen/mobile/vodDetail/voddetail.html?name=gyc");	
				     	  response.sendRedirect(page);
						  SaveVodPageThread st = new SaveVodPageThread(username, streamid,page);
						  new Thread(st).start();
						
					}else{
						 logger.info("【NS-AppServlet】异常信息：");
						 logger.info("【NS-AppServlet】数据库中获取 手机视频详情页面为空");
						 logger.info("【NS-AppServlet】则给手机指定默认跳转地址：【NS-http://192.168.100.11:8882/NSCS/mobile/index.jsp】");
						 logger.info("【NS-AppServlet】重要提醒：请到RCM中配置手机详情页。");
						 response.sendRedirect("http://192.168.100.11:8882/NSCS/mobile/index.jsp?resolution="+resolution);	
					}

			
			} catch (Exception e) {
				logger.error("【NS-AppServlet】 执行出错", e.getMessage());
				PrintWriter out = response.getWriter();
		
				out.println("error["+e+"]， Maybe , AppServlet 执行出错");
				out.flush();
              	out.close();
			}
		
		}else{		
			    logger.info("【NS-AppServlet】非客户端扫描二维码");
			    logger.info("【NS-AppServlet】 非客户端扫描，将进入下载客户端页面");						 
				String url = PropertiesUtil.readValue("app.download.url");	
				request.getRequestDispatcher(url).forward(request, response); 
				
			 //	logger.info("下载页面 ："+url);				
			 //	PrintWriter out = response.getWriter();					
			 //	out.println(url);http://www.pgyer.com/T2CY
			 // logger.info("【NS-AppServlet】下载(IOS)页地址是：【NS-http://www.pgyer.com/T2CY】");			
			 // response.sendRedirect("http://pgyer.com/MsHG");
			 //  response.sendRedirect("http://www.pgyer.com/T2CY");
		 }			
			
		}

	
}
