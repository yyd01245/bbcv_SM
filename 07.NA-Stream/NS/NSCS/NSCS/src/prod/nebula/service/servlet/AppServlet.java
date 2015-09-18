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
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.dto.RcmManager;
import prod.nebula.service.dto.VodResourceInfo;
import prod.nebula.service.thread.SaveVodPageThread;
import prod.nebula.service.util.PropertiesUtil;
import redis.clients.jedis.Jedis;

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
		
		String username	= request.getParameter("username");		
		String streamid = request.getParameter("id");
		String vp = request.getParameter("vp");
		String resolution = request.getParameter("r"); 
		
		   logger.info("【AppServlet】用户访问------------AppServlet------------：");
		   logger.info("【AppServlet】username(即用户名) =" + username);  
		   logger.info("【AppServlet】streamid(即流id ) =" + streamid); 
		   logger.info("【AppServlet】vp(即视频id) =" + vp);
		   logger.info("【AppServlet】r(即高标清) =" + resolution);
		   
		//   Jedis jedis = new Jedis("127.0.0.1",6379);
		//   	 jedis.auth("1234");//验证密码	
		   	 
		if(username!=null){
			  logger.info("【AppServlet】此用户使用宽云客户端扫描");
			  
			    
			 
			 // 在缓存中设置该用户扫描所获取到的vodid;
			 String vodidkey = username+"vodidkey";
			 
			// jedis.del(vodidkey);
			 ServletContext application=this.getServletContext();   
		        //设置Application属性   
		
			 
			 
			  RcmManager rm = new RcmManager();
			  
			  
			  String mobileDetailPage = rm.getMbDetailPage().getUrl();
			  
			  
			  try {
				  
					if(mobileDetailPage!=null&&!"".equals(mobileDetailPage)){
						
					
					// 查询出vp对应 的资源信息。	
	
				
					 
				//	 jedis.set(vodidkey, vp);
					
					 
					  
				        //设置Application属性   
				        application.setAttribute(vodidkey, vp);  
				        
					 // 在缓存中设置扫描的频道是高标清（0高清，1标清）;
				//	 String resolutionkey = username+"resolutionkey";
				//	 jedis.set(resolutionkey, resolution);
					 
					 

				    logger.info("【AppServlet】中Application设置vodidkey="+vodidkey+" 所对应的值："+vp);			   		
				//    logger.info("【AppServlet】中缓存设置resolutionkey="+resolutionkey+" 所对应的值："+resolution);
					 
					//设置用户的点击状态(取时间 代替)，两时间值value相同则说明无点击，否则，就是用户点击了				    
				    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");//年月日时分秒毫秒
			        String state =   sdf.format(new Date());
				    
			        String chickStateOldKey = username+"chickStateOld";
			        String chickStateNowKey = username+"chickStateNow";
			        
			        application.setAttribute(chickStateOldKey, state);  
			        application.setAttribute(chickStateNowKey, state);  
			        
			     //   jedis.set(chickStateOldKey, state);
			     //   jedis.set(chickStateNowKey, state);
			  //      String getchickStateOldKey =  jedis.get(chickStateOldKey);
			   //     String getchickStateNowKey =  jedis.get(chickStateNowKey);
			        
//				    logger.info("【AppServlet】中---------》chickStateOldKey="+chickStateOldKey);
//				    logger.info("【AppServlet】中---------》getchickStateOldKey="+getchickStateOldKey);				    
//				    logger.info("【AppServlet】中---------》chickStateNowKey="+chickStateNowKey);
//				    logger.info("【AppServlet】中---------》getchickStateNowKey="+getchickStateNowKey);
				    
				    
		
				//	 logger.info("【AppServlet】AppServlet中key-->value="+jedis.get(vodidkey));
						 //得到之前的状态
					
						
						
						
						  
				    String page = mobileDetailPage+"?name="+vp+"&username="+username+"&streamid="+streamid+"&resolution="+resolution;
				    logger.info("【AppServlet】手机进入视频详情页面："+page);
				    
				//    request.setAttribute("username", username);
				//    request.setAttribute("streamid", streamid);
				//    request.setAttribute("resolution", resolution);				    
				    VodResourceInfo vod =   rm.getVod(vp);				    
				    request.getSession().setAttribute("vod", vod);
				    
				    
					//	request.getRequestDispatcher(page).forward(request, response);
					//	  response.sendRedirect("http://192.168.100.11/multiscreen/mobile/vodDetail/voddetail.html?name=gyc");	
				     	response.sendRedirect(page);
						  SaveVodPageThread st = new SaveVodPageThread(username, streamid,page);
						  new Thread(st).start();
						
					}else{
						 logger.info("【AppServlet】因从数据库中获取 手机视频详情页面为空，则给手机指定默认跳转地址：【http://192.168.100.11:8181/NS/new/voddetail.jsp?name=1】");	
						 response.sendRedirect("http://192.168.100.11:8181/NS/new/voddetail.jsp?name=1");	
					}

			
			} catch (Exception e) {
				logger.error("【AppServlet】 执行出错", e.getMessage());
				PrintWriter out = response.getWriter();
		
				out.println("error["+e+"]， Maybe , AppServlet 执行出错");
				out.flush();
              	out.close();
			}
		
		}else{		
			   logger.info("【AppServlet】此用户非客户端扫描，将进入下载客户端页面");			
			   logger.info("【AppServlet】下载(IOS)页地址是：【http://www.pgyer.com/T2CY】");
				String url = PropertiesUtil.readValue("app.download.url");	
				
			//	logger.info("下载页面 ："+url);
				
			//	PrintWriter out = response.getWriter();	
				
			//	out.println(url);http://www.pgyer.com/T2CY
			
			request.getRequestDispatcher(url).forward(request, response); 
			
			// response.sendRedirect("http://pgyer.com/MsHG");
			 //  response.sendRedirect("http://www.pgyer.com/T2CY");
		 }			
			
		}

	
}
