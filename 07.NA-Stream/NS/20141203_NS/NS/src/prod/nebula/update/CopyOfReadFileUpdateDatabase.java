/**  
 * 类名称：ReadFileUpdateDatabase 
 * 类描述： 
 * 创建人：PengFei   
 * 创建时间：2014-9-23 下午03:49:02 
 */
package prod.nebula.update;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.dto.VodInfo;
import prod.nebula.service.util.PropertiesUtil;

/**
 * 类名称：ReadFileUpdateDatabase 类描述： 读取文件数据更新到数据库中 创建人：PengFei 创建时间：2014-9-23
 * 下午03:49:02 备注：
 * 
 * @version
 * 
 */
public class CopyOfReadFileUpdateDatabase {
	private static final Logger logger = LoggerFactory
			.getLogger(CopyOfReadFileUpdateDatabase.class);

	public static String toDatabase() throws IOException, InterruptedException {

		String path = PropertiesUtil.readValue("file.data.url");

		try {
			URL url = new URL(path);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.getInputStream();
			DataInputStream input = new DataInputStream(conn.getInputStream());

			InputStreamReader isr = new InputStreamReader(conn.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String str = null;
			List<VodInfo> list = new ArrayList<VodInfo>();
			while ((str = br.readLine()) != null) {

				VodInfo vi = null;
				String param = str.trim();
				if (param.startsWith("id:")) {
					vi = new VodInfo();
					vi.setNameId(str);
				}
				if (vi != null) {
					if (param.startsWith("name")) {
						vi.setName(str);
					}
					if (param.startsWith("movieintro")) {
						vi.setMovieintro(str);
					}
					if (param.startsWith("videosrc")) {
						vi.setVideosrc(str);
					}
					if (param.startsWith("director")) {
						vi.setDirector(str);
					}
					if (param.startsWith("actor")) {
						vi.setActor(str);
					}
					if (param.startsWith("type")) {
						vi.setType(str);
					}
					if (param.startsWith("ondata")) {
						vi.setOndata(str);
					}
					if (param.startsWith("rstp")) {
						vi.setRstp(str);
					}
					if (param.startsWith("other")) {
						vi.setOther(str);
					}
					list.add(vi);
				}

			}

			System.out.println("list.size =" + list.size());


		

		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}

	//	 logger.info("素材文件存在，素材地址为："+path);


	

		return null;
	}

	public static void main(String[] args) throws InterruptedException {
		try {
			CopyOfReadFileUpdateDatabase.toDatabase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

   
	  

}
