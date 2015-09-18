package prod.nebula.service.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import prod.nebula.service.socket.PauseClient;

public class PauseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Constructor of the object.
	 */
	public PauseServlet() {
		super();
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


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	    String streamid = request.getParameter("streamid");
	    String username = request.getParameter("username");
		System.out.println("streamid="+streamid);
		System.out.println("username="+username);
		
		if(streamid!=null&&!"".equals(streamid)&&username!=null&&!"".equals(username)){
			  PauseClient c = new PauseClient();
			  c.send(streamid,username);
			//  System.out.println("发送完毕，处理暂停超时页面");
			//  request.getRequestDispatcher("subinter2.jsp").forward(request, response);   
			
		}
	
	//	
	}

}
