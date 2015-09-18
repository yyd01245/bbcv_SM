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

import prod.nebula.service.dto.DatabaseQuary;

/*
 * 处理二维码扫描   Servlet
 * 
 * 
 */
public class SkipServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(SkipServlet.class);
	private static final long serialVersionUID = 1L;
	
	public SkipServlet() {
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	      this.doPost(request, response);	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	
		PrintWriter out = response.getWriter();	
		
		String streamid = request.getParameter("streamid");
		
		String resolution = request.getParameter("resolution");
		
		String pageName = request.getParameter("pageName");
		
	//	String vodid = request.getParameter("vodid");
		
	
		
	try {
		String username =  DatabaseQuary.queryStatus(streamid);
		
	
		  //  Jedis jedis = new Jedis("127.0.0.1",6379);
		//   	 jedis.auth("1234");//验证密码	

		
		 // 在缓存中设置该用户扫描所获取到的vodid;
		 String vodidkey = username+"vodidkey";
	
		 
		 
		 // 在缓存中设置扫描的频道是高标清（0高清，1标清）;
	//	 String resolutionkey = username+"resolutionkey";
	//	 HttpSession session = request.getSession(true);
		
		 ServletContext application=this.getServletContext();   
	        //设置Application属性   
	        
		 
		String v= (String)application.getAttribute(vodidkey);

		if(username!=null&&!"".equals(username)&&null!=v){
			  
			//在缓存中获取该用户扫描的资源id 即vodid.
			
		//	String vodid =  jedis.get(vodidkey);
			String vodid = v;
		//	String resolution = jedis.get(resolutionkey);
			
		//	request.getSession().removeAttribute(vodidkey);
		//	jedis.del(vodidkey);
			
			application.removeAttribute(vodidkey);
			
	//		jedis.del(resolutionkey);在Application设置扫描资源id.key="+vodidkey+",id.value"+vp
			
		    logger.info("【SkipServlet】已删除扫描资源id.key="+vodidkey+",id="+v);
	   		
	//	    logger.info("【SkipServlet】中缓存设置resolutionkey="+resolutionkey+" 所对应的值："+resolution);


			//获取该资源后，跳转到详情页面。
			
//			
//			OutputStream ops = response.getOutputStream();
//			
//			ops.write(("tvvoddetail.jsp?username="+username+"&"+"streamid="+streamid+"&"+"vod="+vod+"&vodid="+vodid).getBytes());
			//ops.println("tvvoddetail.jsp?username="+username+"&"+"streamid="+streamid+"&"+"vod="+vod+"&vodid="+vodid,"GBK");
//			ops.flush();
			
			
			
			if("1".equals(resolution)){				
				 logger.info("【SkipServlet】TV导航将跳转到TV详情页（标清）");
			//	out.println("tvvoddetail1.jsp?username="+username+"&"+"streamid="+streamid+"&vodid="+vodid+"&pageName="+pageName+"&times=first");
			}
			
			
			//是高清，则跳转到高清的详情页等
			if("0".equals(resolution)){
				  logger.info("【SkipServlet】TV导航将跳转到TV详情页（高清）");				
				//	out.println("tvvoddetail1.jsp?username="+username+"&"+"streamid="+streamid+"&vodid="+vodid+"&pageName="+pageName+"&times=first");	
			}
			
			
			out.println("tvvoddetail1.jsp?username="+username+"&"+"streamid="+streamid+"&vodid="+vodid+"&pageName="+pageName+"&times=first");
			
			
			
		//	logger.info("【SkipServlet】用户扫描二维码，绑定成功   【用户名username ="+username+"， 流streamid ="+streamid+"，扫描资源id="+vodid+"】");
      
			// 根据id.查询用户扫描的资源的具体信息
	
			

	//	out.println("test.jsp?username="+username+"&"+"streamid="+streamid+"&"+"vod="+vod);
	//	out.println("subinter1.jsp?username="+username+"&"+"streamid="+streamid+"&"+"vod="+vod);
	//	logger.info("【SkipServlet】绑定成功后页面跳转到等待点播页面：subinter1.jsp?username="+username+"&"+"streamid="+streamid);
	//	out.println("http://218.108.50.246/tianjing/apps/index.html");
	//	logger.info("绑定成功后页面跳转到：http://218.108.50.246/tianjing/apps/index.html");
		
			out.close();
		}
	} catch (Exception e) 
	{	
		logger.info("【SkipServlet】出错 "+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort());
		e.printStackTrace();
		out.println("error");
		out.close();// TODO: handle exception
	}
		
		//System.out.println(flag);	
			//	request.getRequestDispatcher(url).forward(request, response); 
   
	}

	
}
