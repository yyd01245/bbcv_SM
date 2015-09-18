package prod.nebula.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ReadData {

	public static void main(String[] args) {
		
		 File file = new File("F:/a.txt");
		 
		 try {
			FileInputStream fis = new FileInputStream(file);
			
			byte[] b = new byte[1024];
			
			int a =0;
			
			while( (a=fis.read(b))!=-1){
				//System.out.println(new String(b));
			}
			
			
			FileReader fr  = new FileReader(file);
			
			char c [] = new char[1024];
			StringBuffer s1 =new StringBuffer();
			
			while( (a=fr.read(c))!=-1){
				 s1.append(new String(c,0,a));
		
			}
			
			String a1 = 	s1.toString().substring(9);
		//	System.out.println(a1);	
			
			
			
		JSONArray ja = new JSONArray();
		String s [] = {"id:gyc"};
		ja.fromObject(s);
	//	System.out.println(ja.toString());
		//	System.out.println(c);
			JSONObject jb = new JSONObject();
			jb.fromObject(a1);
			
			System.out.println(jb);
	//		String s = "";
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 

	}

}
