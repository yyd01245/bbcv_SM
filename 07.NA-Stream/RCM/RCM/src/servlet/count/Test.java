/**  
 * ����ƣ�Test 
 * �������� 
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-10-13 ����07:46:49 
 */
package servlet.count;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**   
 * ����ƣ�Test   
 * ��������   
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-10-13 ����07:46:49   
 * ��ע��   
 * @version    
 *    
 */
public class Test extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Test() {
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
this.doPost(request, response);
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
    
		//this.doGet(request, response);
		
		  String path1 = System.getProperty("user.dir")+"\\data\\data.js";
	       
	     String path3 =Test.class.getClassLoader().getResource("/").getPath() ;
	       System.out.println("path1 = "+path1);
	       System.out.println("path3 = "+path3);
	       File f = new File(path3);
	     
	       File f1 = new File(f.getParent());
	    String s =    f.getParent();
	    
	    System.out.println("s= "+s);  
	    
	    String s1 =    f1.getParent();
	    System.out.println("s1= "+s1);  
	    
	    
	    File f3 = new File(s1+"/data/data.js");
	    
	 
	    
	       if(f3.exists()){
	    	   
	    	   System.out.println("�ļ����� "+f.getAbsolutePath());
	       }else{
	    	   System.out.println("�ļ�������");
	       }
	       try {
			FileReader fr = new FileReader(f3);
			int i =0;
			BufferedReader br = new BufferedReader(fr);
			String s5;
			while((s5=br.readLine())!=null){
				
				System.out.println(s5);
				
			}
		
	   	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
	       
	       //System.out.println(f.getParent());
	 
		
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
