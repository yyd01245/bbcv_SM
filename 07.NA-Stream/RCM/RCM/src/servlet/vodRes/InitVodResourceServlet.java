/**  
 * 类名称：InitVodResourceServlet 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-10-30 下午05:55:40 
 */
package servlet.vodRes;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.BannerInfo;
import pojo.Page;
import pojo.VodResourceInfo;
import dto.BannerConfigService;
import dto.InitVodResourceService;

/**   
 * 类名称：InitVodResourceServlet   
 * 类描述：   
 * 创建人：PengFei   
 * 创建时间：2014-10-30 下午05:55:40   
 * 备注：   
 * @version    
 *    
 */
public class InitVodResourceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(InitVodResourceServlet.class);

	public InitVodResourceServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String returnPage = request.getParameter("page");
	    String currentPage = request.getParameter("currentPage");
	    String resolution = request.getParameter("resolution");
	    String onchange = request.getParameter("onchange");
	    String bannerId = request.getParameter("bannerId");
	    
	    System.out.println("onchange------------->"+onchange);
	    
	    
	    if("tv_navigate_config_add_update.jsp".equals(returnPage)){
	    	  int currentPageInt = 1;
			  if(currentPage!=null&&!currentPage.equals("")){
			     currentPageInt=   Integer.parseInt(currentPage);			
			   }
			   
	    	   listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null,returnPage,resolution);
	    }
			 
   	   
	    if("banner_config_add_update.jsp".equals(returnPage)){
	  	  int currentPageInt = 1;
		  if(currentPage!=null&&!currentPage.equals("")){
		     currentPageInt=   Integer.parseInt(currentPage);			
		   }
		  
		  
		   BannerConfigService mcs = new BannerConfigService();
	    	List<BannerInfo> list = mcs.findBI();
	    	BannerInfo bannerInfo = new BannerInfo();
	    	
	    	if(null==onchange){
	        	if(list.size()>0){
		    		
		    		bannerInfo.setBannerName(list.get(0).getBannerName());
		    		bannerInfo.setBannerId(list.get(0).getBannerId());
		    		bannerInfo.setItemNum(list.get(0).getItemNum());
		    		 request.setAttribute("bannerid", list.get(0).getBannerId());
		    		 if(resolution==null||"".equals(resolution)){
		    			 resolution="0";
		    		 }
		    		 
		    		 request.setAttribute("resolution", resolution);
		    		request.setAttribute("list", list);
		 	    	bannerListAndPage(request,response,currentPageInt, "id"  ,"desc" ,null,returnPage,resolution,bannerInfo);
		 	    	
		 	    	
		 	    	
		    	}else{
		    		 request.setAttribute("list", list);
		    		 request.getRequestDispatcher(returnPage).forward(request, response); 
		    	}
		    	
	    	}
	    	else if("c".equals(onchange)){
	    		
	    		
	    		System.out.println("提交表单了   resolution = "+resolution);
	    		bannerInfo.setBannerId(Integer.parseInt(bannerId));
	    		 request.setAttribute("list", list);
	    		 request.setAttribute("resolution", resolution);
	    		 request.setAttribute("bannerid", bannerId);
	    		bannerListAndPage(request,response,currentPageInt, "id"  ,"desc" ,null,returnPage,resolution,bannerInfo);
	    		
	    	}
	
	    	else{
	    		
	    		 request.setAttribute("list", list);
	    		 request.getRequestDispatcher(returnPage).forward(request, response); 
	    	}
	    	
	    	
	    }
	    
	    
	}

	public void listAndPage(HttpServletRequest request, HttpServletResponse response,int  currentPage,String ascendName  ,String ascending ,String urlKey,String returnPage,String resolution){
		 Page page = new Page();	
		 page.setCurrentPage(currentPage);		
		 InitVodResourceService tns = new InitVodResourceService();
	     List<VodResourceInfo> list =	tns.findTNList(page, ascendName  , ascending , urlKey,resolution);
	     
	     
	     
	     int  totalRecofds = tns.getTvNavigetTotal();	   
	      logger.info("TotalRecords="+totalRecofds);
	      if(list!=null&&list.size()!=0){
		     page.setTotalRecords(totalRecofds);
		     int  totalpages = totalRecofds%page.getPerPageRecords()==0?totalRecofds/page.getPerPageRecords():totalRecofds/page.getPerPageRecords()+1;	
		     logger.info("totalpages="+totalpages);		  
		     page.setTotalPages(totalpages);
		    
	    }else{	    	
	    	   page.setCurrentPage(0);
			    page.setTotalRecords(0);
			    page.setTotalPages(0);	    	
	    }

		
	     request.setAttribute("page", page);
		 request.setAttribute("list", list);

		try {
			request.getRequestDispatcher(returnPage).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} 		
	}
	public void bannerListAndPage(HttpServletRequest request, HttpServletResponse response,int  currentPage,String ascendName  ,String ascending ,String urlKey,String returnPage,String resolution,BannerInfo bannerInfo){
		 Page page = new Page();	
		 page.setCurrentPage(currentPage);		
		 InitVodResourceService tns = new InitVodResourceService();
	     List<VodResourceInfo> list =	tns.findBCList(page, ascendName  , ascending , urlKey,resolution,bannerInfo);
	     int  totalRecofds = tns.getTvNavigetTotal();	   
	      logger.info("TotalRecords="+totalRecofds);
	      if(list!=null&&list.size()!=0){
		     page.setTotalRecords(totalRecofds);
		     int  totalpages = totalRecofds%page.getPerPageRecords()==0?totalRecofds/page.getPerPageRecords():totalRecofds/page.getPerPageRecords()+1;	
		     logger.info("totalpages="+totalpages);		  
		     page.setTotalPages(totalpages);
		    
	    }else{	    	
	    	   page.setCurrentPage(0);
			    page.setTotalRecords(0);
			    page.setTotalPages(0);	    	
	    }

	     request.setAttribute("page", page);
		 request.setAttribute("vodlist", list);
		
		try {
			request.getRequestDispatcher(returnPage).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} 		
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void init() throws ServletException {
	
	}

}
