/**  
 * TvGuideServlet 
 * 资源管理Servlet
 * PengFei   
 * 2014-9-25 
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

import pojo.Page;
import pojo.TvNavigate;
import pojo.VodResourceInfo;
import data.SynchroData;
import dto.TvNavigateService;
import dto.VodResService;

/**   
 *  VodResourceServlet   
 *  资源管理
 *  
 *  
 *  
 *    
 */
public class VodResourceServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(VodResourceServlet.class);
	/**
	 * Constructor of the object.
	 */
	public VodResourceServlet() {
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
        
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		   String method = request.getParameter("method");
		   String id = request.getParameter("id");
		   String currentPage = request.getParameter("currentPage");
		   
		   String resolution = request.getParameter("resolution");
		  
			  int currentPageInt = 1;
			   if(currentPage!=null&&!currentPage.equals("")){
				currentPageInt=   Integer.parseInt(currentPage);
				
			   }
		
		   if("list".equals(method)){
	    	 
	    	 
	    	   listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null);
	    	
	    	   
	       } 
		   if("preUpdateUrl".equals(method)){
	    	   logger.info("预更新操作  id="+id);

	    	VodResService ts = new VodResService();
	    	VodResourceInfo tvNavigate= ts.getTvNavigate(id);
	    	
	    	 request.setAttribute("tvNavigate", tvNavigate);
	    	request.getRequestDispatcher("vod_resource_add_update.jsp").forward(request, response);  
	    	   
	       } 
		   if("vodDetail".equals(method)){
	    	   logger.info("预更新操作  id="+id);

	    	VodResService ts = new VodResService();
	    	VodResourceInfo tvNavigate= ts.getTvNavigate(id);
	    	
	    	 request.setAttribute("tvNavigate", tvNavigate);
	    	request.getRequestDispatcher("vod_resource_detail.jsp").forward(request, response);  
	    	   
	       } 
		   
		   
		   if("preUpdateChange".equals(method)){
	    	   logger.info("预更新转化操作  id="+id);

	    	VodResService ts = new VodResService();
	    	VodResourceInfo tvNavigate= ts.getTvNavigate(id);
	    	
	    	 request.setAttribute("tvNavigate", tvNavigate);
	    	request.getRequestDispatcher("vod_resource_resolution_change.jsp").forward(request, response);  
	    	   
	       } 
		   
		   
	
		   if("updateState".equals(method)){
	    	   logger.info("资源删除操作  id = "+id);
	    	   VodResourceInfo tn = new VodResourceInfo();
	    	   tn.setId(Integer.parseInt(id));
	    	
	    	   VodResService ts = new VodResService();
	    	 ts.updateState(tn);
	    	
	 	       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
		       SynchroData sd = new SynchroData();	            
		       sd.writeTvDataToFile(filePath,resolution);	
		       sd.writeMbDataToFile(filePath, resolution);	       
	           sd.writeSliderDataToFile(filePath, resolution);
		       
	    	  listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null);
	    	   
	       }
		
		   
 
	}

	public void listAndPage(HttpServletRequest request, HttpServletResponse response,int  currentPage,String ascendName  ,String ascending ,String urlKey){
		 Page page = new Page();
	
		 page.setCurrentPage(currentPage);
		
		VodResService tns = new VodResService();

	    List<VodResourceInfo> list =	tns.findTNList(page, ascendName  , ascending , urlKey);
	   int  totalRecofds = tns.getTvNavigetTotal();
	   
	   logger.info("TotalRecords="+totalRecofds);
	    if(list!=null&&list.size()!=0){
		
		//    page.setCurrentPage(1);
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
			request.getRequestDispatcher("vod_resource.jsp").forward(request, response);
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

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		   String method = request.getParameter("method");
		   String currentPage = request.getParameter("currentPage");
    	   
    	   String ascendName = request.getParameter("ascendName");
    	   String ascending = request.getParameter("ascending");
    	   String url = request.getParameter("url");

    	   
    	      String name= request.getParameter("name");
    	      Integer grade= Integer.parseInt(request.getParameter("grade"));
    	      String director= request.getParameter("director");
    	      String actor= request.getParameter("actor");
    	      String years= request.getParameter("years");
    	      String runtime= request.getParameter("runtime");
    	      String type= request.getParameter("type");
    	      String area= request.getParameter("area");
    	      String description= request.getParameter("description");
    	      String path= request.getParameter("path");
    	      String bigPosterPath= request.getParameter("bigPosterPath");
    	      String littlePosterPath= request.getParameter("littlePosterPath");
    	      String rtspUrl= request.getParameter("rtspUrl");
    	      String other= request.getParameter("other");
    	   //  Integer modelId= Integer.parseInt(request.getParameter("modelId"));
    	    
    	      String resolution= request.getParameter("resolution");
    	     
    
		   Page page = new Page();
		  int currentPageInt = 1;
		   if(currentPage!=null&&!currentPage.equals("")){
			currentPageInt=   Integer.parseInt(currentPage);
		   }
		   page.setCurrentPage(currentPageInt);
		 
		   String id = request.getParameter("id");
		   
		   
	       System.out.println(method);
	       System.out.println(url);
	       
	       System.out.println("other="+other);
	       
	       
	       if("add".equals(method)){
	    	   logger.info("资源新增操作");
	    	    logger.info("name="+name+" years"+years+ " description"+description+" path"+path);
	    	   VodResourceInfo tn = new VodResourceInfo();
	    	   tn.setActor(actor);
	    	   tn.setBigPosterPath(bigPosterPath);
	    	   tn.setDescription(description);
	    	   tn.setDirector(director);
	    	   tn.setGrade(grade);
	    	   tn.setType(type);
	    	   tn.setArea(area);
	    	   tn.setLittlePosterPath(littlePosterPath);
	    	//   tn.setModelId(modelId);
	    	   tn.setName(name);
	    	   tn.setPath(path);
	    	   tn.setRtspUrl(rtspUrl);
	    	   tn.setRuntime(runtime);
	    	  tn.setYears(years);
	    	  tn.setOther(other);
	    	  tn.setState("0");
	    	  tn.setResolution(resolution);
	        VodResService vrs = new VodResService();
	            vrs.add(tn);
	            
//	            
//		 	       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
//			       logger.info("栏目配置更新操作，路径："+filePath);		    	   
//			       SynchroData sd = new SynchroData();	            
//			       sd.writeTvDataToFile(filePath,resolution);
//			       sd.writeMbDataToFile(filePath, resolution);
		            
	    	    
	       }
	       
	       
	       
	       
	       if("resolutionChange".equals(method)){
	    	   logger.info("资源更新操作  ");
	    	    logger.info("name="+name+" years:"+years+" path:"+path+" resolution="+resolution);
	    	   VodResourceInfo tn = new VodResourceInfo();
	      
	    	   tn.setActor(actor);
	    	   tn.setBigPosterPath(bigPosterPath);
	    	   tn.setDescription(description);
	    	   tn.setDirector(director);
	    	   tn.setGrade(grade);
	    	   tn.setType(type);
	    	   tn.setArea(area);
	    	   tn.setLittlePosterPath(littlePosterPath);
	    	//   tn.setModelId(modelId);
	    	   tn.setName(name);
	    	   tn.setPath(path);
	    	   tn.setRtspUrl(rtspUrl);
	    	   tn.setRuntime(runtime);
	    	  tn.setYears(years);
	    	  tn.setOther(other);
	    	  tn.setState("0");
	    	  
	    	   if("1".equals(resolution)){
	    		
	    		   tn.setResolution("0");
	    	   }
	    	   
	    	   if("0".equals(resolution)){
	    		   tn.setResolution("1");
	    	   }
	    	  
	    	
	        VodResService vrs = new VodResService();
	         
	        vrs.add(tn);
	        

	       }

	       if("updateUrl".equals(method)){
	    	   logger.info("资源更新操作  ");
	    	    logger.info("name="+name+" years:"+years+ " description:"+description+" path:"+path);
	    	   VodResourceInfo tn = new VodResourceInfo();
	    	   tn.setId(Integer.parseInt(id));
	    	   tn.setActor(actor);
	    	   tn.setBigPosterPath(bigPosterPath);
	    	   tn.setDescription(description);
	    	   tn.setDirector(director);
	   	       tn.setGrade(grade);
	    	   tn.setLittlePosterPath(littlePosterPath);
	    	//   tn.setModelId(modelId);
	    	   tn.setName(name);
	    	   tn.setType(type);
	    	   tn.setArea(area);
	    	   tn.setPath(path);
	    	   tn.setRtspUrl(rtspUrl);
	    	   tn.setRuntime(runtime);
	    	   tn.setYears(years);
	    	   tn.setOther(other);
	    	   tn.setResolution(resolution);
	        VodResService vrs = new VodResService();
	         
	        vrs.update(tn);
	        
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
