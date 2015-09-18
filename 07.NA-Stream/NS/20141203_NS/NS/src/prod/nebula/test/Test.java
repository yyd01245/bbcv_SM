package prod.nebula.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private static final Logger logger = LoggerFactory.getLogger("");
	private int i = 0;
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
            StringBuffer sb = new StringBuffer();
            
            sb.append("2,");
            
            
            System.out.println(sb.toString());
            System.out.println(sb.toString().length());
            System.out.println(sb.toString().substring(0, sb.toString().length()-1));
            
            
            
            
            
            
            
            
            
            
            
            List<String> list1 = new ArrayList<String>();
            list1.add("a");
            list1.add("b");
            list1.add("c");
            list1.add("d");
            
            
            
            List<String> list2 = new ArrayList<String>();
            
            
            list2.add("a");
            list2.add("b");
            list2.add("c");
            list2.add("e");
            
       //     list2.retainAll(list1);
            System.out.println(list2);
            list2.removeAll(list1);
            System.out.println(list2);
            System.out.println(list1);
            
            
            
            
            
            
            
            
            
            
            
            String s1 = "营盘镇警事";
            
            
            String vodname = new String(s1.getBytes("UTF-8"),"ISO-8859-1");
            System.out.println("vodname="+vodname);
            
            String vodname1 = new String(vodname.getBytes("ISO-8859-1"),"UTF-8");
            
            
            System.out.println("vodname1="+vodname1);
            
            
            
            
            
            
            
            System.out.println(new String(s1.getBytes("UTF-8"),"GBK"));
            
           // System.out.println(new String(s1.getBytes("UTF-8"),"gb18080"));
            
            System.out.println(new String(s1.getBytes("UTF-8"),"ISO8859-1"));
            
            System.out.println(new String(s1.getBytes("gb2312"),"UTF-8"));
            
        
      System.out.println(new String(new String(s1.getBytes("UTF-8"),"GBK").getBytes("GBK"),"UTF-8"));
            System.out.println(new String(s1.getBytes("ISO8859-1"),"UTF-8"));
            System.out.println(new String(s1.getBytes("UTF-8"),"GBK"));
            System.out.println(new String(s1.getBytes("UTF-8"),"GBK")); 
            
            
            
            
            File f = new File("d:\\2.mp4");
            
            if(!f.exists()){
            	
            	 try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }else{            	System.out.println("文件已存在");
            }
            
           
            
            
            
            
	}

}
