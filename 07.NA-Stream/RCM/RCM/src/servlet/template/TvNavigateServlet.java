/**  
 * ����ƣ�TvGuideServlet 
 * �������� 
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-25 ����03:32:53 
 */
package servlet.template;

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
import data.SynchroData;
import dto.TvNavigateService;

/**   
 * ����ƣ�TvNavigateServlet   
 * ��������   tv����ģ�崦�� servlet
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-25 ����03:32:53   
 * ��ע��   
 * @version    
 *    
 */
public class TvNavigateServlet extends HttpServlet {

	/**
	 *  @Fields serialVersionUID : TODO(��һ�仰�������������ʾʲô)
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TvNavigateServlet.class);
	/**
	 * Constructor of the object.
	 */
	public TvNavigateServlet() {
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

	    	TvNavigateService ts = new TvNavigateService();
	    	TvNavigate tvNavigate= ts.getTvNavigate(id);
	    	
	    	 request.setAttribute("tvNavigate", tvNavigate);
	    	request.getRequestDispatcher("tv_navigate_add_update.jsp").forward(request, response);  
	    	   
	       } 
		   
		   
		   if("updateUseState".equals(method)){
	    	   logger.info("���²������û�ʹ��ģ�� id = "+id);
	    	   TvNavigate tn = new TvNavigate();
	    	   tn.setId(Integer.parseInt(id));
	    	   tn.setUseState("1");
	    	TvNavigateService ts = new TvNavigateService();
	    	ts.updateUseState(tn);
	   	 SynchroData sd = new SynchroData();	 
		 sd.writeTvNavigateToDatabase();
		 logger.info("����192.168.70.240�� stream_resource��strNav_url�ֶ���");
	    	
	    	//�б?��ҳ
	    	  listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null);
	    	   
	       }
		   if("updateState".equals(method)){
	    	   logger.info("αɾ����� id = "+id);
	    	   TvNavigate tn = new TvNavigate();
	    	   tn.setId(Integer.parseInt(id));
	    	
	    	TvNavigateService ts = new TvNavigateService();
	    	ts.updateState(tn);
	    	
	    	//�б?��ҳ
	    	  listAndPage(request,response,currentPageInt, "id"  ,"desc" ,null);
	    	   
	       }
		
		   
 
	}
	
	//�б?��ҳ	
	public void listAndPage(HttpServletRequest request, HttpServletResponse response,int  currentPage,String ascendName  ,String ascending ,String urlKey){
		 Page page = new Page();
	
		 page.setCurrentPage(currentPage);
		long start = System.currentTimeMillis();
		TvNavigateService tns = new TvNavigateService();

	    List<TvNavigate> list =	tns.findTNList(page, ascendName  , ascending , urlKey);
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
			request.getRequestDispatcher("tv_navigate_page.jsp").forward(request, response);
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
    	   
    	   String ascendName = request.getParameter("ascendName");
    	   String ascending = request.getParameter("ascending");
    	   String url = request.getParameter("url");
    	   String urlkey = request.getParameter("urlkey");
		   
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
	    	   logger.info("��Ӳ���   url ="+url);
	    	   TvNavigate tn = new TvNavigate();
	    	   tn.setState("0");
	    	   tn.setUrl(url);
	    	   tn.setUseState("0");
	    	   TvNavigateService ts = new TvNavigateService();
	    	   ts.add(tn);
	    	    
	       }
	       if("updateUrl".equals(method)){
	    	   logger.info("���²���  ����url ="+url);
	    	   TvNavigate tn = new TvNavigate();
	    	   tn.setId(Integer.parseInt(id));
	    	   tn.setUrl(url);
	    	TvNavigateService ts = new TvNavigateService();
	    	ts.update(tn);
	  
	    	   
	       }

	       
	       
	       listAndPage(request,response,currentPageInt, ascendName  ,ascending ,urlkey);
	      
	       
	       
	       
	       
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
