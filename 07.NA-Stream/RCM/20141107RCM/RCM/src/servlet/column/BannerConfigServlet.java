/**  
 * ����ƣ�TvGuideServlet 
 * �������� 
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-25 ����03:32:53 
 */
package servlet.column;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.BannerInfo;
import pojo.BannerVodMapping;
import pojo.Page;
import pojo.dao.BannerVodView;
import data.SynchroData;
import dto.BannerConfigService;

/**   
 * VodResourceServlet   
 * 手机栏目配置 servlet
 * PengFei   
 * 2014-9-25   
 */
public class BannerConfigServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(BannerConfigServlet.class);
	/**
	 * Constructor of the object.
	 */
	public BannerConfigServlet() {
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
    
		   String method = request.getParameter("method");
		   String id = request.getParameter("id");
		   String currentPage = request.getParameter("currentPage");
		 
		   
		   String sliderPoster = request.getParameter("sliderPoster");
		   String resolution = request.getParameter("resolution");
		   
		   
			  int currentPageInt = 1;
			   if(currentPage!=null&&!currentPage.equals("")){
				currentPageInt=   Integer.parseInt(currentPage);
				
			   }
	
			   
		   if("updateSliderPoster".equals(method)){  	  
			    	
	    	   logger.info("更新操作，是否在首页流动海报中 显示   id = "+id+" sliderPoster(0否，1是):"+sliderPoster);
	    	   BannerVodMapping tn = new BannerVodMapping();
	    	   tn.setId(Integer.parseInt(id));
	    	   tn.setSliderPoster(sliderPoster);
	    	   BannerConfigService ts = new BannerConfigService();
	           ts.updateSliderPoster(tn);	    	
	    	
	           SynchroData sd = new SynchroData();
	           
	           String filePath=this.getServletConfig().getServletContext().getRealPath("/");
	           sd.writeSliderDataToFile(filePath, resolution);
	           
	           
	    	  listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null);
		    		    	   
		     } 
			   
		   if("list".equals(method)){  	  
	    	
	    	   listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null);
	    		    	   
	       } 
		   if("preAdd".equals(method)){
	    	
			   
			   BannerConfigService mcs = new BannerConfigService();
	    	List<BannerInfo> list = mcs.findBI();
	
	    	
	    	 request.setAttribute("list", list);
	    	request.getRequestDispatcher("banner_config_add_update.jsp?flag=add").forward(request, response);  
	    	   
	       } 
		   
		   if("preUpdateUrl".equals(method)){
	    	 

	    	BannerConfigService ts = new BannerConfigService();
	    	BannerVodView tvNavigate= ts.getTvNavigate(id);
	    	List<BannerInfo> list = ts.findBI();
	    	
	    	
	    	 request.setAttribute("list", list);
	    	 request.setAttribute("tvNavigate", tvNavigate);
	    	request.getRequestDispatcher("banner_config_add_update.jsp").forward(request, response);  
	    	   
	       } 
		   
		   

		   if("updateState".equals(method)){
	    	
	    	   BannerVodMapping tn = new BannerVodMapping();
	    	   tn.setId(Integer.parseInt(id));
	    	
	    	   BannerConfigService ts = new BannerConfigService();
	           ts.updateState(tn);
	    	
	           
	           
	           SynchroData sd = new SynchroData();
	           String filePath=this.getServletConfig().getServletContext().getRealPath("/");
		       sd.writeTvDataToFile(filePath,resolution);	
		       sd.writeMbDataToFile(filePath, resolution);	          
	           sd.writeSliderDataToFile(filePath, resolution);
	           
	           
	    	  listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null);
	    	   
	       }
		
		   
 
	}

	public void listAndPage(HttpServletRequest request, HttpServletResponse response,int  currentPage,String ascendName  ,String ascending ,String urlKey){
		 Page page = new Page();
	
		 page.setCurrentPage(currentPage);
		
		BannerConfigService tns = new BannerConfigService();

	    List<BannerVodView> list =	tns.findTNList(page, ascendName  , ascending , urlKey);
	   int  totalRecofds = tns.getTvNavigetTotal();
	   
	   logger.info("TotalRecords="+totalRecofds);
	    if(list!=null&&list.size()!=0){
		

		    page.setTotalRecords(totalRecofds);
		  int  totalpages = totalRecofds%page.getPerPageRecords()==0?totalRecofds/page.getPerPageRecords():totalRecofds/page.getPerPageRecords()+1;
	
		  
		    page.setTotalPages(totalpages);
		    
	    }else{
	    	
	    	   page.setCurrentPage(0);
			    page.setTotalRecords(0);
			    page.setTotalPages(0);
	    	
	    }
	    request.setAttribute("page", page);

		 request.setAttribute("list", list);

		try {
			request.getRequestDispatcher("banner_config.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
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

	       
		   String method = request.getParameter("method");
		   String currentPage = request.getParameter("currentPage");
		   
		   
		   String resolution = request.getParameter("resolution");
    	   
    	   String ascendName = request.getParameter("ascendName");
    	   String ascending = request.getParameter("ascending");
    	   String url = request.getParameter("url");

    	   Integer bannerId= Integer.parseInt(request.getParameter("bannerId"));
    	   Integer vodId= Integer.parseInt(request.getParameter("vodId"));
    	   Integer orderId= Integer.parseInt(request.getParameter("orderId"));
    	   String sliderPoster = request.getParameter("sliderPoster");
    	   
		  
		   Page page = new Page();
		  int currentPageInt = 1;
		   if(currentPage!=null&&!currentPage.equals("")){
			currentPageInt=   Integer.parseInt(currentPage);
		   }
		   page.setCurrentPage(currentPageInt);
		 
		   String id = request.getParameter("id");
		   
		   
	       System.out.println(method);
	       System.out.println(url);
	       
	       
	       
	       
	       if("add".equals(method)){
	    	  
	    	   BannerVodMapping bvm = new BannerVodMapping();
	    	   bvm.setBannerId(bannerId);
	    	   bvm.setVodId(vodId);
	    	   bvm.setOrderId(orderId);
	    	   bvm.setSliderPoster("0");
	    	   bvm.setState("0");
	    	   bvm.setResolution(resolution);    	  
	    	   BannerConfigService vrs = new BannerConfigService();
	           vrs.add(bvm);	 
	           
	           
		       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
		       SynchroData sd = new SynchroData();	            

		       
		       sd.writeTvDataToFile(filePath,resolution);	
		       sd.writeMbDataToFile(filePath, resolution);	          
	           sd.writeSliderDataToFile(filePath, resolution);
		
	            
	       }
	       if("updateUrl".equals(method)){
	    
	    	   BannerVodMapping bvm = new BannerVodMapping();
	    	   bvm.setBannerId(bannerId);
	    	   bvm.setVodId(vodId);
	    	   bvm.setOrderId(orderId);
	    	   bvm.setId(Integer.parseInt(id));	
	    	   bvm.setResolution(resolution);  
	    
	    	   BannerConfigService vrs = new BannerConfigService();
		       vrs.update(bvm);	
		       
		       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
		       SynchroData sd = new SynchroData();	            

		       
		       sd.writeTvDataToFile(filePath,resolution);	
		       sd.writeMbDataToFile(filePath, resolution);	          
	           sd.writeSliderDataToFile(filePath, resolution);
	       }

	       
	       
	       listAndPage(request,response,currentPageInt, ascendName  ,ascending ,url);
	      
	       
	       
	       
	       
	     //  response.sendRedirect(null);
 	//request.getRequestDispatcher("dwz-ria1/index.jsp").forward(request, response); 

 	
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
