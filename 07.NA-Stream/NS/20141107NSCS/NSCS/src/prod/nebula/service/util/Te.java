package prod.nebula.service.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import prod.nebula.service.dto.VodInfo;

public class Te {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
         FileReader reader = new FileReader("F:/a.txt");
         BufferedReader br = new BufferedReader(reader);
         String str = null;
        List<VodInfo> list = new ArrayList<VodInfo>();
         while((str = br.readLine()) != null) {

               VodInfo vi = null;
              String param = str.trim();
              if(param.startsWith("id:")){
            	  vi=  new VodInfo(); 
            	  vi.setNameId(str);
              }             
              if(vi!=null){
                  if(param.startsWith("name")){
                	  vi.setName(str);
                  }  
                  if(param.startsWith("movieintro")){
                	  vi.setMovieintro(str);
                  } 
                  if(param.startsWith("videosrc")){
                	  vi.setVideosrc(str);
                  } 
                  if(param.startsWith("director")){
                	  vi.setDirector(str);
                  } 
                  if(param.startsWith("actor")){
                	  vi.setActor(str);
                  } 
                  if(param.startsWith("type")){
                	  vi.setType(str);
                  } 
                  if(param.startsWith("ondata")){
                	  vi.setOndata(str);
                  } 
                  if(param.startsWith("rstp")){
                	  vi.setRstp(str);
                  } 
                  if(param.startsWith("other")){
                	  vi.setOther(str);
                  } 
                  list.add(vi);
              }                                  

	}
         
         System.out.println("list.size ="+list.size()); 
       
	}
}
