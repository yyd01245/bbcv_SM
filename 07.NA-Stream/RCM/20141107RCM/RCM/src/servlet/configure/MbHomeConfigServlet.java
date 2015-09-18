
package servlet.configure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.BannerInfo;
import pojo.MainpageInfo;
import pojo.Page;
import pojo.dao.MainBannerView;
import data.SynchroData;
import dto.BannerService;
import dto.MbHomeConfigService;

/**   
 * VodResourceServlet   
 * PengFei   
 * 2014-9-25 
 * @version    
 *    
 */
public class MbHomeConfigServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(MbHomeConfigServlet.class);
	/**
	 * Constructor of the object.
	 */
	public MbHomeConfigServlet() {
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
		   logger.info("当前页：currentPage="+currentPage);
		   
		   String resolution = request.getParameter("resolution");  //0高清，1标清
		   
		   
		   
			  int currentPageInt = 1;
			  
			   if(currentPage!=null&&!currentPage.equals("")){
				currentPageInt=   Integer.parseInt(currentPage);
				
			   }
	
			   
		   if("list".equals(method)){
			   request.setAttribute("resolution", resolution);
			    
	    	   listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null,resolution);
	    	
	    	   
	       } 
		   if("preAdd".equals(method)){
	    	
			   
			   MbHomeConfigService mcs = new MbHomeConfigService();
			   List<BannerInfo> list =null;
			  try {
				  request.setAttribute("resolution", resolution);
				    
				  list = mcs.findBI(resolution);	
				  request.setAttribute("list", list);
			    	request.getRequestDispatcher("mb_home_config_add_update.jsp?flag=add").forward(request, response); 
			} catch (Exception e) {
				e.printStackTrace();
				// request.setAttribute("list", list);
			    	request.getRequestDispatcher("mb_home_config_add_update.jsp?flag=error").forward(request, response);  
				System.out.println("����Ŀ�����@��");
			} 
	    	
	
	    	
	    	
	    	
	    	
	    	   
	       } 
		   
		   if("preUpdateUrl".equals(method)){
	    	   logger.info("Ԥ���²������������  id="+id);

	    	   MbHomeConfigService mcs = new MbHomeConfigService();
	    	   MainpageInfo tvNavigate= mcs.getTvNavigate(id);
	    	
	    	   
	    	   BannerService bs = new BannerService();
	    	   List<BannerInfo> list = new ArrayList<BannerInfo>();
	    	   
	    	   request.setAttribute("resolution", resolution);
	    	   list.add(bs.getTvNavigate(String.valueOf(tvNavigate.getModelId())));
	    		 request.setAttribute("list", list);
	    	   
	    	 request.setAttribute("tvNavigate", tvNavigate);
	    	request.getRequestDispatcher("mb_home_config_add_update.jsp").forward(request, response);  
	    	   
	       } 

		   if("updateState".equals(method)){
	    	   logger.info(" id = "+id);
	    	   MainpageInfo tn = new MainpageInfo();
	    	   tn.setMainPage(Integer.parseInt(id));
	    	
	    	MbHomeConfigService mcs = new MbHomeConfigService();
	    	mcs.updateState(tn);
	    	
		       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
		       SynchroData sd = new SynchroData();	            
		       sd.writeMbDataToFile(filePath,"0");
		       sd.writeMbDataToFile(filePath,"1");
	    	
	    	  listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null,resolution);
	    	   
	       }
		
		   
 
	}
	
	//	
	public void listAndPage(HttpServletRequest request, HttpServletResponse response,int  currentPage,String ascendName  ,String ascending ,String urlKey,String resolution){
		 Page page = new Page();
	
		 page.setCurrentPage(currentPage);
	
		MbHomeConfigService tns = new MbHomeConfigService();

	    List<MainBannerView> list =	tns.findTNList(page, ascendName  , ascending , urlKey,resolution);
	   int  totalRecofds = tns.getTvNavigetTotal(resolution);
	   
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
	    request.setAttribute("resolution", resolution);
		 request.setAttribute("list", list);
	
		try {
			request.getRequestDispatcher("mb_home_config.jsp").forward(request, response);
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

		//response.setCharacterEncoding("GBK");
		   String method = request.getParameter("method");
		   String currentPage = request.getParameter("currentPage");
		   String resolution = request.getParameter("resolution");
		   
    	   String ascendName = request.getParameter("ascendName");
    	   String ascending = request.getParameter("ascending");
    	   String url = request.getParameter("url");
    	  
  
    	   Integer modelId= Integer.parseInt(request.getParameter("modelId"));
    	   Integer orderId= Integer.parseInt(request.getParameter("orderId"));
    	   
    	   String mainPageStr=request.getParameter("mainPage");
    	   Integer mainPage =null ;
    	   System.out.println("mainPageStr= "+mainPageStr);
    	   if(mainPageStr!=null){
    		    mainPage= Integer.parseInt(mainPageStr);
    	   }
    	 
  
    	   
		 
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
	    	   logger.info("手机首页添加操作");
	    	    logger.info("排序："+orderId);
	    	    MainpageInfo bvm = new MainpageInfo();
	             bvm.setMainpageUrl("");
	             bvm.setResolution(resolution);
	             bvm.setModelId(modelId);
	             bvm.setOrderId(orderId);
	             bvm.setState("0");	  
	        MbHomeConfigService vrs = new MbHomeConfigService();
	            vrs.add(bvm);	
	            
	 	       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
		       SynchroData sd = new SynchroData();	            
		       sd.writeMbDataToFile(filePath,resolution);
		    
	            
	            
	       }
	       if("updateUrl".equals(method)){
	    	   logger.info("手机首页配置更新操作  ");
	    	   logger.info(" 排列顺序"+orderId);
	    	   MainpageInfo bvm = new MainpageInfo();
	        
	           bvm.setModelId(modelId);
	           bvm.setOrderId(orderId);
	    	   bvm.setMainPage(mainPage);
	    	   bvm.setResolution(resolution);
	    	   MbHomeConfigService vrs = new MbHomeConfigService();
		       vrs.update(bvm);	  
		       
	 	       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
		       logger.info("栏目配置更新操作，路径："+filePath);		    	   
		       SynchroData sd = new SynchroData();	            
		       sd.writeMbDataToFile(filePath,resolution);
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
