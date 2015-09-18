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

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.dto.RcmManager;
import prod.nebula.service.dto.VodResourceInfo;
import prod.nebula.service.redis.RedisClient;
import redis.clients.jedis.Jedis;

/**    
 * 类名称：ShowVodInfo   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-10-15 下午08:21:48    
 * 备注：   
 * @version    
 *    
 */
public class ShowVodInfo extends HttpServlet {
	/**
	 *  @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(SkipServlet.class);
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
		
		  String flag = request.getParameter("flag");
 
	
		   
			if("getnow".equals(flag)){
				response.setCharacterEncoding("UTF-8"); 
				//System.out.println("getnow 请求中……");
				  String username = request.getParameter("username");
				  String streamid = request.getParameter("streamid");
				  String resolution = request.getParameter("resolution");
				//  String vodid = request.getParameter("vodid");
				
			        String chickStateOldKey = username+"chickStateOld";
			        String chickStateNowKey = username+"chickStateNow";
				  
//			        logger.info("ShowVodInfo《getnow》方法中---------》username="+username);
//			        logger.info("ShowVodInfo《getnow》方法中---------》streamid="+streamid);
//			     
//			        
//			        logger.info("ShowVodInfo《getnow》方法中---------》chickStateOldKey="+chickStateOldKey);
//				    logger.info("ShowVodInfo《getnow》方法中---------》chickStateNowKey="+chickStateNowKey);
					
					 
				//	 Jedis jedis = new Jedis("127.0.0.1",6379);
				//   	 jedis.auth("1234");//验证密码
					 
					 
					 String key = username+"vodid";
					 
					 ServletContext application=this.getServletContext();   
				        //设置Application属性   
				        
					 
					String vodid= (String)application.getAttribute(key);
					 
				//	 String vodid=  jedis.get(key);

				//	jedis.del(key);//TV显示完后，删除掉。此时已无用。
//					 logger.info("ShowVodInfo《getnow》方法中---------》vodid="+vodid);
				//获取该用户的点击状态，是否点击，如果点击了就跳转
				
			
				 
				 //得到之前的状态
					String chickStateOldValue= (String)application.getAttribute(chickStateOldKey);
			//	String chickStateOldValue =  jedis.get(chickStateOldKey);
				//得到当前的状态
				String chickStateNowValue= (String)application.getAttribute(chickStateNowKey);
			//	String chickStateNowValue =  jedis.get(chickStateNowKey);
				
//		        logger.info("ShowVodInfo《getnow》方法中---------》chickStateOldValue="+chickStateOldValue);
//			    logger.info("ShowVodInfo《getnow》方法中---------》chickStateNowValue="+chickStateNowValue);
			    PrintWriter out = response.getWriter();
			   
		         if(chickStateOldValue==null||chickStateNowValue==null){
//		        	logger.info("chickStateOldValue=null,chickStateNowValue=null,即两者没有在AppServlet中初始化设");
		        	 chickStateOldValue="1";
		        	 chickStateNowValue="1";
		         }
//		         logger.info("ShowVodInfo《getnow》方法中---------》两者不等吗：="+(!chickStateOldValue.equals(chickStateNowValue)));
				//如果两者不一致，则说明该用户触发了影片信息
				if(!chickStateOldValue.equals(chickStateNowValue)){
					//返回新的影片信息，并让TV详情页 显示
				//	RcmManager rm = new RcmManager();
					
				//	VodResourceInfo vod = rm.getVod(vodid);
					
					
					
					//  JSONArray  jsonObject =   JSONArray.fromObject(vod);
					
				//	jedis.set(chickStateOldKey, chickStateNowValue); //让两者的值恢复一致，即状态一致。
	
					application.setAttribute(chickStateOldKey, chickStateNowValue);
					//System.out.println("ShowVodInfo《getnow》方法中---------》vod="+jsonObject.toString());		
				//	System.out.println("ShowVodInfo《getnow》方法中---------》vod.length="+vod.toString().length());		
					//获取该资源后，跳转到详情页面。	
					//out.println("tvvoddetail1.jsp?username="+username+"&"+"streamid="+streamid+"&"+"vod="+jsonObject+"&vodid="+vodid);
					
					
					if("0".equals(resolution)){
						out.println("tvvoddetail1.jsp?username="+username+"&"+"streamid="+streamid+"&vodid="+vodid+"&resolution="+resolution);
					}
					if("1".equals(resolution)){
						
						// 标清地址
						
						out.println("tvvoddetail1.jsp?username="+username+"&"+"streamid="+streamid+"&vodid="+vodid+"&resolution="+resolution);
					}
					
				//	tvvoddetail.jsp?username="+username+"&"+"streamid="+streamid+"&"+"vod="+vod+"&vodid="+vodid
				//	out.println(vod.toString());
				
					out.flush();
	              	out.close();
				}else{
					//不做处理
					
					out.println("error");
					
					out.flush();
	              	out.close();
					
				}
			     
				
				
			}
		  
		  
		  
		if("get".equals(flag)){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
		//	System.out.println("get 请求中……");
			  String username = request.getParameter("username");
			  String streamid = request.getParameter("streamid");
			  String resolution = request.getParameter("resolution");
			//  String vodid = request.getParameter("vodid");
			
		        String chickStateOldKey = username+"chickStateOld";
		        String chickStateNowKey = username+"chickStateNow";
			  
//		        logger.info("ShowVodInfo《get》方法中---------》username="+username);
//		        logger.info("ShowVodInfo《get》方法中---------》streamid="+streamid);
//		        logger.info("ShowVodInfo《get》方法中---------》resolution="+resolution);
//		     
//		        
//		        logger.info("ShowVodInfo《get》方法中---------》chickStateOldKey="+chickStateOldKey);
//			    logger.info("ShowVodInfo《get》方法中---------》chickStateNowKey="+chickStateNowKey);
		      //  Jedis jedis = new Jedis("127.0.0.1",6379);
			   	// jedis.auth("1234");//验证密码
				 String key = username+"vodid";
			//	String vodid=  jedis.get(key);
				 ServletContext application=this.getServletContext();   
			        //设置Application属性   
			        
				 
				String vodid= (String)application.getAttribute(key);
				
//				 logger.info("ShowVodInfo《get》方法中---------》vodid="+vodid);
			//获取该用户的点击状态，是否点击，如果点击了就跳转
			
		
			 
			 //得到之前的状态
		//	String chickStateOldValue =  jedis.get(chickStateOldKey);
			String chickStateOldValue= (String)application.getAttribute(chickStateOldKey);
			//得到当前的状态
			String chickStateNowValue= (String)application.getAttribute(chickStateNowKey);
		//	String chickStateNowValue =  jedis.get(chickStateNowKey);
			
//	        logger.info("ShowVodInfo《get》方法中---------》chickStateOldValue="+chickStateOldValue);
//		    logger.info("ShowVodInfo《get》方法中---------》chickStateNowValue="+chickStateNowValue);
		    PrintWriter out = response.getWriter();
		   
	         if(chickStateOldValue==null||chickStateNowValue==null){
//	        	logger.info("chickStateOldValue=null,chickStateNowValue=null,即两者没有在AppServlet中初始化设");
	        	 chickStateOldValue="1";
	        	 chickStateNowValue="1";
	         }
//	         logger.info("ShowVodInfo《get》方法中---------》两者不等吗：="+(!chickStateOldValue.equals(chickStateNowValue)));
			//如果两者不一致，则说明该用户触发了影片信息
			if(!chickStateOldValue.equals(chickStateNowValue)){
				//返回新的影片信息，并让TV详情页 显示
				
			//	RcmManager rm = new RcmManager();
				
			 //	VodResourceInfo vod = rm.getVod(vodid);
			
			//	  JSONArray  jsonObject =   JSONArray.fromObject(vod);
				
				
			//	jedis.set(chickStateOldKey, chickStateNowValue); //让两者的值恢复一致，即状态一致。			
				application.setAttribute(chickStateOldKey, chickStateNowValue);
		//		System.out.println("ShowVodInfo《get》方法中---------》vod="+jsonObject.toString());	
				
				
				//获取该资源后，跳转到详情页面。
				
				if("0".equals(resolution)){
					out.println("tvvoddetail1.jsp?username="+username+"&"+"streamid="+streamid+"&vodid="+vodid+"&resolution="+resolution);
				}
				if("1".equals(resolution)){
					
					out.println("tvvoddetail1.jsp?username="+username+"&"+"streamid="+streamid+"&vodid="+vodid+"&resolution="+resolution);
					// 标清地址
				}
			
			
				out.flush();
              	out.close();
			}else{
				//不做处理
				
				out.println("error");
				
				out.flush();
              	out.close();
				
			}
		     
			
			
		}
		if("set".equals(flag)){
			System.out.println("set 请求中……");
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8"); 
			 //获取streamid,usernames,vodid		
			  String username = request.getParameter("username");
			  String streamid = request.getParameter("streamid");
			  String resolution = request.getParameter("resolution");
			  String vodid = request.getParameter("vodid");
			  String beforepage = request.getParameter("beforepage");
			  String bannername = request.getParameter("bannername");
//			  System.out.println("bannername----------------------------------------。"+bannername);
		//        String chickStateOldKey = username+"chickStateOld";
		        String chickStateNowKey = username+"chickStateNow";
		    //    Jedis jedis = new Jedis("127.0.0.1",6379);
			  // 	 jedis.auth("1234");//验证密码
				 String key = username+"vodid";
				 
				 
				 ServletContext application=this.getServletContext();   
			        //设置Application属性   
			        
				 application.setAttribute(key, vodid);
			//	String vodid= (String)application.getAttribute(key);
			//	 jedis.set(key, vodid);
				
//		        logger.info("ShowVodInfo<set>方法中---------》username="+username);
//		        logger.info("ShowVodInfo<set>方法中---------》streamid="+streamid);
//		        logger.info("ShowVodInfo<set>方法中---------》vodid="+vodid);
//		        logger.info("ShowVodInfo<set>方法中---------》resolution="+resolution);
//		        
//		        logger.info("ShowVodInfo<set>方法中---------》chickStateOldKey="+chickStateOldKey);
//			    logger.info("ShowVodInfo<set>方法中---------》chickStateNowKey="+chickStateNowKey);
//			  
			    
			    
//		--------------------------------	    

			    
			    
			    
			    
			    
//				--------------------------------			    
			
			//用户点击后……  更新key=chickStateNowKey的值，更新后chickStateNowKey！=chickStateOldKey的值，则标示用户点击事件。
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");//年月日时分秒毫秒
	        String state =   sdf.format(new Date());        
	    
	    //    jedis.set(chickStateNowKey, state);
	        application.setAttribute(chickStateNowKey, state);
			 //通过value 查找资源。
			 //根据id.查询用户扫描的资源的具体信息
			
		//	RcmManager rm = new RcmManager();
		//	VodResourceInfo vod = rm.getVod(vodid);		
			
	
			
			//  JSONArray  jsonObject =   JSONArray.fromObject(vod);
			  
			  
			 //   String filePath=this.getServletConfig().getServletContext().getRealPath("/")+"voddetail"+File.separator+"js"+File.separator+username+"movie.js";
           //     CreateData cd = new CreateData();
            //    cd.createMoviceData(filePath, jsonObject.toString());
			  
			// 把获取的资源  返回到页面中
			//out.println("tvvoddetail.jsp?username="+username+"&"+"streamid="+streamid+"&"+"vod="+vod);
//			  logger.info("ShowVodInfoServlet 将要跳转到手机详情页面");
			 
			  request.setAttribute("bannername", bannername);
			  
			  String  url = null;
//				if("0".equals(resolution)){
					   url = "voddetail.jsp?username="+username+"&"+"streamid="+streamid+"&"+"vodid="+vodid+"&name="+vodid+"&beforepage="+beforepage+"&resolution="+resolution;
					
//				}
//				if("1".equals(resolution)){
//					
//					// 标清地址
//				}
			 
//			  logger.info("-------->手机详情页面 URL="+url);
			  request.getRequestDispatcher(url).forward(request, response);
	    //    response.sendRedirect(url); 
		}
		

		
		//System.out.println("-----------------");
	
		
		
		
		
		
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
