
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
import pojo.Page;
import data.SynchroData;
import dto.BannerService;

/**   
 * 栏目管理
 * PengFei   
 * 2014-9-25 03:32:53   
 *    
 * @version    
 *    
 */
public class BannerServlet extends HttpServlet {

	/**
	 *  @Fields serialVersionUID : TODO()
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(BannerServlet.class);
	/**
	 * Constructor of the object.
	 */
	public BannerServlet() {
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
		   logger.info("��ǰҳ��currentPage="+currentPage);
			  int currentPageInt = 1;
			   if(currentPage!=null&&!currentPage.equals("")){
				currentPageInt=   Integer.parseInt(currentPage);
				
			   }
		
		   if("list".equals(method)){
	    	   logger.info("Tv ����ģ����ҳ currentPageInt="+currentPageInt);
	    	 //�б?��ҳ
	    	   listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null);
	    	
	    	   
	       } 
		   if("preUpdateUrl".equals(method)){
	    	   logger.info("Ԥ���²������������  id="+id);

	    	   BannerService ts = new BannerService();
	    	BannerInfo tvNavigate= ts.getTvNavigate(id);
	    	
	    	 request.setAttribute("tvNavigate", tvNavigate);
	    	request.getRequestDispatcher("banner_add_update.jsp").forward(request, response);  
	    	   
	       } 
		   
		   
		
		   if("updateState".equals(method)){
	    	   logger.info("αɾ����� id = "+id);
	    	   BannerInfo tn = new BannerInfo();
	    	   tn.setBannerId(Integer.parseInt(id));
	    	
	    	   BannerService ts = new BannerService();
	        	ts.updateState(tn);
	    	
		 	       String filePath=this.getServletConfig().getServletContext().getRealPath("/");	    		  
			       logger.info("栏目配置更新操作，路径："+filePath);		    	   
			       SynchroData sd = new SynchroData();	 
			       
			       
			       sd.writeTvDataToFile(filePath,"0");
			       sd.writeTvDataToFile(filePath,"1");
			       sd.writeMbDataToFile(filePath, "0");	
			       sd.writeMbDataToFile(filePath, "1");
		           sd.writeSliderDataToFile(filePath, "0");
		           sd.writeSliderDataToFile(filePath, "1");
	    	
	    	  listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null);
	    	   
	       }
		
		   
 
	}
	
	//�б?��ҳ	
	public void listAndPage(HttpServletRequest request, HttpServletResponse response,int  currentPage,String ascendName  ,String ascending ,String urlKey){
		 Page page = new Page();
	
		 page.setCurrentPage(currentPage);
		long start = System.currentTimeMillis();
		BannerService tns = new BannerService();

	    List<BannerInfo> list =	tns.findTNList(page, ascendName  , ascending , urlKey);
	   int  totalRecofds = tns.getTvNavigetTotal();
	   
	   logger.info("TotalRecords="+totalRecofds);
	    if(list!=null&&list.size()!=0){
		
		//    page.setCurrentPage(1);
		    page.setTotalRecords(totalRecofds);
		  int  totalpages = totalRecofds%page.getPerPageRecords()==0?totalRecofds/page.getPerPageRecords():totalRecofds/page.getPerPageRecords()+1;
		  logger.info("ÿҳ������¼�� = "+page.getPerPageRecords());
		  logger.info("totalpages="+totalpages);
		  
		    page.setTotalPages(totalpages);
		    
	    }else{
	    	
	    	   page.setCurrentPage(0);
			    page.setTotalRecords(0);
			    page.setTotalPages(0);
	    	
	    }
	    request.setAttribute("page", page);

		 request.setAttribute("list", list);
		 long end = System.currentTimeMillis();
		 logger.info("��ѯ���һ����ʱ��"+(end-start)+"ms"); 
		try {
			request.getRequestDispatcher("banner_page.jsp").forward(request, response);
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

		   request.setCharacterEncoding("UTF-8");
		  response.setCharacterEncoding("UTF-8");
		   String method = request.getParameter("method");
		   String currentPage = request.getParameter("currentPage");
    	   
    	   String ascendName = request.getParameter("ascendName");
    	   String ascending = request.getParameter("ascending");
    	   
    	   String bannerId = request.getParameter("bannerId");
    	   String bannerName = request.getParameter("bannerName");
    	   String itemNum = request.getParameter("itemNum");
		   
		   logger.info("当前页currentPage="+currentPage);
		   Page page = new Page();
		  int currentPageInt = 1;
		   if(currentPage!=null&&!currentPage.equals("")){
			currentPageInt=   Integer.parseInt(currentPage);
		   }
		   page.setCurrentPage(currentPageInt);
		 
		   String id = request.getParameter("id");
		   
		   
	       System.out.println(method);
	       System.out.println(bannerName);
	       
	       
	       
	       
	       if("add".equals(method)){
	    	   logger.info("栏目新增操作   bannerName ="+bannerName);
	    	   BannerInfo tn = new BannerInfo();
	    	   tn.setState("0");
	    	   tn.setBannerName(bannerName);
	    	   tn.setItemNum(Integer.parseInt(itemNum));
	    	   BannerService ts = new BannerService();
	    	   ts.add(tn);
	    	    
	       }
	       if("updateUrl".equals(method)){
	    	   logger.info("栏目更新操作      bannerName ="+bannerName);
	    	   BannerInfo tn = new BannerInfo();
	    	   tn.setBannerId(Integer.parseInt(bannerId));
	    	   tn.setBannerName(bannerName);
	    	   tn.setItemNum(Integer.parseInt(itemNum));
	    	   BannerService ts = new BannerService();
	    	   ts.update(tn);
	  
	    	   String filePath=this.getServletConfig().getServletContext().getRealPath("/");
	  
	    	   logger.info("栏目修改操作，路径："+filePath);
	    	   
	            SynchroData sd = new SynchroData();	            
	            sd.writeMbDataToFile(filePath,"0");
	            sd.writeMbDataToFile(filePath,"1");
	       }

	       
	       
	       listAndPage(request,response,currentPageInt, ascendName  ,ascending ,null);
	      
	       
	       
	       
	       
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
