package prod.nebula.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import prod.nebula.service.socket.OverTimeClient;

public class OverTimeServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Constructor of the object.
	 */
	public OverTimeServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
           this.doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();	
		String streamid = request.getParameter("streamid");
		
		System.out.println("streamid="+streamid);
		
		if(streamid!=null&&!"".equals(streamid)){
			  OverTimeClient c = new OverTimeClient();
			  c.send(streamid);
			  System.out.println("发送完毕，处理绑定超时页面");
		//	  request.getRequestDispatcher("subinter2.jsp").forward(request, response);   
		    	
				out.println("");

				out.close();
			
		}
	
	//	

	}


	public void init() throws ServletException {
	}

}
