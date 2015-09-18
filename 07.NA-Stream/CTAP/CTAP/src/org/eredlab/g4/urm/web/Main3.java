package org.eredlab.g4.urm.web;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
 
public class Main3 {
 
    public static void main(String[] args) throws IOException {
    	
    
        Process pro = Runtime.getRuntime().exec("tasklist /v");
        in2st(pro.getInputStream());
       // System.out.println(in2st(pro.getInputStream()));
    }
 
    private static String in2st(InputStream stream) throws IOException {
      
    	BufferedInputStream in = new BufferedInputStream(stream);
   	 BufferedReader inBr = new BufferedReader(new InputStreamReader(in, "GBK"));
        
        String count = null;
       StringBuffer sb = new StringBuffer();
        while ((count = inBr.readLine()) != null) {
          System.out.println(count);
           sb.append(count);
        }
        return sb.toString();
    }
}