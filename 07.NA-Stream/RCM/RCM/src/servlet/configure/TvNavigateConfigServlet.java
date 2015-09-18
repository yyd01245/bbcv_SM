
package servlet.configure;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.Ad;
import pojo.NamodelRole;
import pojo.Page;
import pojo.dao.AdAndAdDistribution;
import pojo.dao.VodRoleView;
import data.SynchroData;
import dto.TvNavigateConfigService;

/**   
 * TV导航配置管理
 * @version    
 *    
 */
public class TvNavigateConfigServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TvNavigateConfigServlet.class);
	/**
	 * Constructor of the object.
	 */
	public TvNavigateConfigServlet() {
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
	
		   String resolution = request.getParameter("resolution");
		   
		   
		   
			  int currentPageInt = 1;
			   if(currentPage!=null&&!currentPage.equals("")){
				currentPageInt=   Integer.parseInt(currentPage);
				
			   }
	
			   
		   if("list".equals(method)){
			   
			   System.out.println("list 页中 resolution="+ resolution);
	    	
			   request.setAttribute("resolution", resolution);
			    
	    	   listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null,resolution);
	    	  
	    	
	    	   
	       } 
		   
		   if("adList".equals(method)){
			   
			   System.out.println("adList 页中");
	    	
	        listAdPage(request,response,currentPageInt, "id"  ,"desc" ,null,resolution);
	    	   
	       } 
		   
		   
//		   if("preAdd".equals(method)){
//	    	
//			   
//			   MbHomeConfigService mcs = new MbHomeConfigService();
//	    	List<BannerInfo> list = mcs.findBI();
//	
//	    	
//	    	 request.setAttribute("list", list);
//	    	request.getRequestDispatcher("mb_home_config_add_update.jsp?flag=add").forward(request, response);  
//	    	   
//	       } 
		   
		   if("preUpdateUrl".equals(method)){
	    	  

	    	   TvNavigateConfigService ts = new TvNavigateConfigService();
	    	   VodRoleView tvNavigate= ts.getVodRoleView(id);
	    	  request.setAttribute("resolution", resolution);
	    	 request.setAttribute("tvNavigate", tvNavigate);
	    	request.getRequestDispatcher("tv_navigate_config_add_update.jsp").forward(request, response);  
	    	   
	       } 
		   if("preUpdateAd".equals(method)){
		    	  

	    	TvNavigateConfigService ts = new TvNavigateConfigService();
	    	Ad tvNavigate= ts.getAd(id);
	    	request.setAttribute("resolution", resolution);
	    	request.setAttribute("tvNavigate", tvNavigate);
	    	request.getRequestDispatcher("tv_ad_add_update.jsp?flag=update").forward(request, response);  
	    	   
	       } 
		    
	
		   if("updateState".equals(method)){
	    	   logger.info("id = "+id);
	    	   NamodelRole tn = new NamodelRole();
	    	   tn.setId(Integer.parseInt(id));
	    	
	    	   TvNavigateConfigService ts = new TvNavigateConfigService();
	    	   ts.updateState(tn);
		       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
		       SynchroData sd = new SynchroData();	            
		       sd.writeTvDataToFile(filePath,"0");
		       sd.writeTvDataToFile(filePath,"1");
	    	
	    	  listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null,resolution);
	    	   
	       }
		   if("changeState".equals(method)){
	    	   logger.info("id = "+id);
	    	   Ad tn = new Ad();
	    	   tn.setId(Integer.parseInt(id));
	    	
	    	   TvNavigateConfigService ts = new TvNavigateConfigService();
	    	   ts.changeState(tn);
		       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
		
	    	
	    	  listAdPage(request,response,currentPageInt, "id"  ,"desc" ,null,resolution);
	    	   
	       }
		   
 
	}
	public void listAdPage(HttpServletRequest request, HttpServletResponse response,int  currentPage,String ascendName  ,String ascending ,String urlKey,String resolution){
		 Page page = new Page();
	
		 page.setCurrentPage(currentPage);
		
		TvNavigateConfigService tns = new TvNavigateConfigService();
	    List<Ad> list =tns.findAdList(page, ascendName  , ascending , urlKey);	     
	   int  totalRecofds = tns.getTvAdTotal();	   
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
			request.getRequestDispatcher("tv_ad_config.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
		
	public void listAndPage(HttpServletRequest request, HttpServletResponse response,int  currentPage,String ascendName  ,String ascending ,String urlKey,String resolution){
		 Page page = new Page();
	
		 page.setCurrentPage(currentPage);
		
		TvNavigateConfigService tns = new TvNavigateConfigService();
	    List<VodRoleView> list =tns.findTNList(page, ascendName  , ascending , urlKey,resolution);	     
	   int  totalRecofds = tns.getTvNavigetTotal(resolution);	   
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
	    request.setAttribute("resolution", resolution);
		 request.setAttribute("list", list);
		
		try {
			request.getRequestDispatcher("tv_navigate_config.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

	
		//  response.setCharacterEncoding("UTF-8");
	 
		   String method = request.getParameter("method");
		   String currentPage = request.getParameter("currentPage");
    	   
    	   String ascendName = request.getParameter("ascendName");
    	   String ascending = request.getParameter("ascending");
    	   String url = request.getParameter("url");
    	   String resolution = request.getParameter("resolution");

    	   Integer pageDwellTime= Integer.parseInt(request.getParameter("pageDwellTime"));
    	   Integer vodId= Integer.parseInt(request.getParameter("vodId"));
    	   String orderId= request.getParameter("orderId");


    	   
		   logger.info("��ǰҳ��currentPage="+currentPage);
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
	    	   logger.info("TV导航配置新增操作");
	    	    logger.info("规则时间："+pageDwellTime+" vodid="+vodId+" resolution="+resolution);
	    	    NamodelRole bvm = new NamodelRole();
	    	   bvm.setVodId(vodId);
	    	   bvm.setOrderId(Integer.parseInt(orderId));
	    	   bvm.setState("0");
	    	   bvm.setPageDwellTime(pageDwellTime);
	    	   bvm.setResolution(resolution);  
	            TvNavigateConfigService vrs = new TvNavigateConfigService();
	            vrs.add(bvm);
	            
		 	       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
			       logger.info("栏目配置更新操作，路径："+filePath);		    	   
			       SynchroData sd = new SynchroData();	            
			       sd.writeTvDataToFile(filePath,resolution);
			       
			       
			       
	       }
	       if("updateUrl".equals(method)){
	    	   logger.info("TV导航配置更新操作");
	    	   logger.info("规则"+pageDwellTime+" vodid"+vodId);
	    	   NamodelRole bvm = new NamodelRole();
	    	 
	    	   bvm.setVodId(vodId);
	    	   bvm.setPageDwellTime(pageDwellTime);
	    	   bvm.setId(Integer.parseInt(id));	 
	    	   bvm.setOrderId(Integer.parseInt(orderId));
	    	   bvm.setResolution(resolution);
	    	   TvNavigateConfigService vrs = new TvNavigateConfigService();
		       vrs.update(bvm);	 
		       
	 	       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
		       SynchroData sd = new SynchroData();	            
		       sd.writeTvDataToFile(filePath,resolution);
	       }
	       
	       String adVideoUrl = request.getParameter("adVideoUrl");
	       String name = request.getParameter("name");
	       String upData = request.getParameter("upData");

	       
	       if("updateAd".equals(method)){
	    	   logger.info("TV广告配置更新操作");
	    	   logger.info("规则"+pageDwellTime+" vodid"+vodId);
	    	   Ad bvm = new Ad();
	    	    bvm.setName(name);
	    	   bvm.setAdVideoUrl(adVideoUrl);
	    	//   bvm.setPageDwellTime(pageDwellTime);
	    	   bvm.setId(Integer.parseInt(id));	 
	    	//   bvm.setOrderId(Integer.parseInt(orderId));
	    	//   bvm.setResolution(resolution);
	    	   bvm.setUpData(upData);
	    	   TvNavigateConfigService vrs = new TvNavigateConfigService();
		      vrs.updateAd(bvm);	 
		       
//	 	       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
//		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
//		       SynchroData sd = new SynchroData();	            
//		       sd.writeTvDataToFile(filePath,resolution);
	       }
	       
	       
	       
	       listAndPage(request,response,currentPageInt, ascendName  ,ascending ,url,resolution);
	      
	       
	       
	       
	       
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
