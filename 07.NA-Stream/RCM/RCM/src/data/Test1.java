/**  
 * ����ƣ�Test 
 * �������� 
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-10-13 ����06:48:01 
 */
package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Test;

/**   
 * ����ƣ�Test   
 * ��������   
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-10-13 ����06:48:01   
 * ��ע��   
 * @version    
 *    
 */
public class Test1 {
  

	/**
	 * @throws IOException 
	 * @Title: main
	 * @Description: TODO(������һ�仰�����������������)
	 * @param @param args    �趨�ļ�
	 * @return void    ��������
	 * @throws
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Test t = new Test();
	String path =Thread.currentThread().getContextClassLoader().getResource(".").getPath(); 
		System.out.println(""+path);
    
       String path1 = System.getProperty("user.dir")+"\\data\\data.js";
       
       String path3 =Test.class.getClassLoader().getResource("").getPath() ;
       System.out.println("path1 = "+path1);
       File f = new File("path3 = "+path3);
       
       System.out.println(f.getParent());
  System.out.println(System.getProperty("user.dir"));
  
  
  
  
  
       if(f.exists()){
    	   
    	   System.out.println("�ļ����� "+f.getAbsolutePath());
       }else{
    	   System.out.println("�ļ�������");
       }
       try {
		FileReader fr = new FileReader(f);
		int i =0;
		while((i=fr.read())!=-1){
			
			System.out.println(i);
			
		}
		
		
		
		
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       
       
	}

}
