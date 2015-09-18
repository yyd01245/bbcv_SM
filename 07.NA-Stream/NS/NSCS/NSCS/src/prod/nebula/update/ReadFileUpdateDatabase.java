/**  
 * 类名称：ReadFileUpdateDatabase 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-9-23 下午03:49:02 
 */
package prod.nebula.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.dto.DatabaseQuary;
import prod.nebula.service.util.PropertiesUtil;

/**
 * 类名称：ReadFileUpdateDatabase 类描述： 读取文件数据更新到数据库中 创建人：PengFei 创建时间：2014-9-23
 * 下午03:49:02 备注：
 * 
 * @version
 * 
 */
public class ReadFileUpdateDatabase {
	private static final Logger logger = LoggerFactory
			.getLogger(ReadFileUpdateDatabase.class);

	public static String toDatabase() throws IOException, InterruptedException {

		String path = PropertiesUtil.readValue("file.data.url");

		try {
			URL url = new URL(path);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(conn.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String str = null;
			List<Info> list = new ArrayList<Info>();
			
			
			DatabaseQuary dq = new DatabaseQuary();
			
			boolean flag = false;
			
			 StringBuffer sb =null; 
			 Info vi = null;
			 
			while ((str = br.readLine()) != null) {

				
			
				
					 if(!flag){
					    vi = null;
					    sb = null;
					 }
				
				String param = str.trim();
				System.out.println(param);
				if (param.startsWith("id:")) {
				String name_id  = 	param.substring(4, param.length()-2);
				
				System.out.println("name_id ="+name_id);
				
					if(!dq.vodExist(name_id)){
						System.out.println("数据库中不存在");
			
					sb = new StringBuffer();	
				     vi = new Info();
					vi.setName_id(name_id);
					flag =true;
				}
				 
				
				}
				  if(flag){
					   System.out.println(param);
					   sb.append(param);  
				   }
					

					if(flag&&param.startsWith("}")){
						
						System.out.println("读取结束");
						vi.setInfo(sb.toString());
						flag =false;
						list.add(vi);
					}
					
			}

			System.out.println("list.size =" + list.size());
		int[] i =	dq.addVod(list);

	System.out.println(i.length);

		

		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}

	//	 logger.info("素材文件存在，素材地址为："+path);


	

		return null;
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			ReadFileUpdateDatabase.toDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

   
	  

}
