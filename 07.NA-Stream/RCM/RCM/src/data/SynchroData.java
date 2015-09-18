/**  
 * SynchroData 
 * 资源同步
 * PengFei   
 * 2014-10-10 
 */
package data;

import hibernate.HibernateSessionFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pojo.TvNavigate;
import pojo.dao.BannerVodView;
import pojo.dao.MainBannerView;
import pojo.dao.VodRoleView;


/**
 * 操作配置中的数据文件data.js <br>
 * 作者：PengFei    <br>
 * 创建日期 2014-10-10 08:41:13 
 * @version
 */
public class SynchroData {

	private static final Logger logger = LoggerFactory
			.getLogger(SynchroData.class);
	

	
	
	

	
	
//	
//	/**
//
//	*配置默认的手机首页跑马灯数据
//
//	* @param path 表示操作文件的路径
//
//	* @param resolution 表示操作对应的高标清文件
//
//	* @return 返回 默认数据
//
//	*/
//	
//	
//	public String getDefaultSliderData() {
//
//		
//		
//		String data = "var sliderdata=[{id:\"1\",name:\"关云长\",href:\"voddetail.html?name=001\",sliderposter:\"images/gyc-big.jpg\"}]; \r\n";
//
//	
//		return data;
//	}

	
	
	

	/**

	*从数据库中读取跑马灯数据，并组装模板

	* @param path 表示操作文件的路径

	* @param resolution 表示操作对应的高标清文件

	* @return 返回 读取数据

	*/
	@SuppressWarnings("unchecked")
	public String readSliderDataFromDatabase(String resolution) {
         
		
	    Session session = HibernateSessionFactory.getSession();
		
       
		
		String hql = " from BannerVodView where sliderposter=1 and bannerResolution="+resolution+" order by orderId asc";
		Query query = session.createQuery(hql);
		
		List<BannerVodView> list = query.list();

		StringBuffer sb = new StringBuffer("var sliderdata=[\r\n ");
		for (int i = 0; i < list.size(); i++) {
		
			sb.append("    {id:\""+list.get(i).getVodId()+"\","+"name:\""+list.get(i).getVodName()+
					"\","+"href:\"voddetail.jsp?name="+list.get(i).getVodId()+"\",sliderposter:\""+list.get(i).getBigPosterPath()+"\"},\r\n");

		}
		sb.append("];");
		return sb.toString();
		
	}

	
	/**

	*  从数据库中读取手机首页栏目配置信息 并组装成模板

	* @param path 表示操作文件的路径

	* @param resolution 表示操作对应的高标清文件

	* @return 返回读取数据值

	*/
	@SuppressWarnings("unchecked")
	public String readMbDataFromDatabase(String resolution) {
		
		Session session = HibernateSessionFactory.getSession();
		
       
		
		String hql = " from MainBannerView where state =0 and resolution="+resolution+" order by orderId asc";
		Query query = session.createQuery(hql);
		
		List<MainBannerView> list = query.list();
		
		
		
		
		StringBuffer sb = new StringBuffer("var data={ \r\n");
		for (int i = 0; i < list.size(); i++) {
		
			sb.append("   " + i + ":{class:\"" + list.get(i).getBannerName()
					+ "\",morelink:\"vod.jsp?class=\"+encodeurl(\""
					+ list.get(i).getBannerName() + "\"),resource:[" + "\r\n");

		
			String str = "  from BannerVodView where bannerId="
					+ list.get(i).getModelId() + " and  bannerResolution="+resolution+"order by orderId asc";
			Query query2 = session.createQuery(str);
			Transaction ts = 	session.beginTransaction();
			List<BannerVodView> list2 = query2.list();
			ts.commit();
			for (BannerVodView bannerVodView : list2) {
				// System.out.println(bannerVodView.getVodName());
				sb.append("      {      " + bannerVodView + "      },");
			}
			sb.append("]\r\n   }," + "\r\n");
		}
		sb.append("};" + "\r\n");
		
		
		
		
		return sb.toString();
		
	}
	
	
	
	
	
	public  void writeSliderDataToFile(String path,String resolution){
	
		 FileManager fp = new FileManager();
	     String data = this.readSliderDataFromDatabase(resolution);	    	   
		try {
		    File f = fp.getMbSpDataFile(path,resolution);	    
	  
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");   
	        BufferedWriter writer=new BufferedWriter(write);     
	        writer.write(data);   
	        writer.close(); 
	      		
		} catch (IOException e) {
			logger.error("操作文件失败");
			e.printStackTrace();
		}
	
	
	
	
    }
	
	
	
	/**

	* writeMbDataToFile: 根据数据库配置生成标清或高清的手机首页页面

	* @param path 表示操作文件的路径

	* @param resolution 表示操作对应的高标清文件

	* @return 没有返回值

	*/
	public  void writeMbDataToFile(String path,String resolution){
		
		 FileManager fp = new FileManager();
	     String data = this.readMbDataFromDatabase(resolution);	    	   
		try {
		    File f = fp.getMbDataFile(path,resolution);	    
		    if (!f.exists()) {   
	            f.createNewFile();   
	        }   
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");   
	        BufferedWriter writer=new BufferedWriter(write);     
	        writer.write(data);   
	        writer.close(); 
	      		
		} catch (IOException e) {
			logger.error("操作文件失败");
			e.printStackTrace();
		}
	
		
    }
	
	
	/**

	* 从数据库中读取TV导航配置信息，并按格式把数据返回

	* @param path 表示操作文件的路径

	* @param resolution 表示操作对应的高、标清文件

	* @return 按格式把数据返回

	*/
	
	@SuppressWarnings("unchecked")
	public String readTvRoleDate(String resolution){
		
		
       Session session = HibernateSessionFactory.getSession();
			
		String hql = " from VodRoleView where resolution="+resolution+" order by orderId asc";
		Query query = session.createQuery(hql);
		
		List<VodRoleView> list = query.list();
		
				
		StringBuffer sb = new StringBuffer("var data={ \r\n");
		for (int i = 0; i < list.size(); i++) {
		
			sb.append("   " + i + ":{"+list.get(i).toString()+"   },\r\n");
			
		}
		sb.append("}");
		
		System.out.println(sb.toString());
		return sb.toString();
	
	}


	@SuppressWarnings("unchecked")
	public String readTvRoleDate1(String resolution){
		
		
       Session session = HibernateSessionFactory.getSession();
			
		String hql = " from VodRoleView where resolution="+resolution+" order by orderId asc";
		Query query = session.createQuery(hql);
		
		List<VodRoleView> list = query.list();
		
				
		StringBuffer sb = new StringBuffer("var info={ \r\n");
		sb.append("   'length':"+list.size()+",\r\n");
		for (int i = 0; i < list.size(); i++) {
		
			sb.append("   'movie" + i + "':{"+list.get(i).toString()+"   },\r\n");
			
		}
		sb.append("}");
		
		System.out.println(sb.toString());
		return sb.toString();
	
	}
	/**

	* writeMbDataToFile: 生成TV导航数据文件

	* @param path 表示操作文件的路径

	* @param resolution 表示操作对应的高标清文件

	* @return 没有返回值

	*/
   public void writeTvDataToFile(String path,String resolution){
	   
		 FileManager fp = new FileManager(); 	    
	    String data = this.readTvRoleDate(resolution);	
	    String data1 = this.readTvRoleDate1(resolution);	
		try {
		    File f =  fp.getTvDataFile(path,resolution);
		    File f1 =  fp.getTvDataFile1(path,resolution);
		  
		    if (!f.exists()) {   
	            f.createNewFile();   
	        }   
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");   
	        BufferedWriter writer=new BufferedWriter(write);     
	        writer.write(data);   
	        writer.close(); 

	        OutputStreamWriter write1 = new OutputStreamWriter(new FileOutputStream(f1),"UTF-8");   
	        BufferedWriter writer1=new BufferedWriter(write1);     
	        writer1.write(data1);   
	        writer1.close(); 

		} catch (IOException e) {
			logger.error("文件操作失败");
			e.printStackTrace();
		}

   }
	/**

	* 把TV导航模板地址同步到ucs_stream_resource表中的 u.strNav_url 字段中

	* @param path 表示操作文件的路径

	* @param resolution 表示操作对应的高标清文件

	* @return 没有返回值
	* 
	* @备注   没有填写 分辨率参数，及没有标示高清还是标清

	*/
   
  
  public void writeTvNavigateToDatabase(){
	  
	  
	   Session session = HibernateSessionFactory.getSession();
		
      
		
		String hql = " from TvNavigate  where  useState=1 ";
		Query query = session.createQuery(hql);
		
		TvNavigate   tv=   (TvNavigate) query.uniqueResult();
		
		String tvUrl = tv.getUrl();
		
		System.out.println(tvUrl);
		
		String sql = "select iStreamID  from  ucs_stream_resource " ; 
		   
		   PreparedStatement ps  = null ;
		
		   Connection  conn = DatabaseConn.getConn();
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			
			while(rs.next()){	
				
			   String	iStreamId  = rs.getString(1);

		    	 String updateSql = "update ucs_stream_resource u set  u.strNav_url= '"+tvUrl+"?id="+iStreamId+"' WHERE u.iStreamID ="+iStreamId+"" ;
			   
			     ps.addBatch(updateSql);
			}

		   rs.close();
	       ps.executeBatch();
			
		
		}catch (SQLException e) {
		
			e.printStackTrace();
		}finally {
	        try{
	         
	            if(ps != null) {
	             ps.close();
	           
	            }
	            if(conn != null) {
	             conn.close();
	            }
	           }catch(SQLException e) {
	        	 
	           }
		 
		}
  }
	
   public static void main(String[] args) {
	   
	
	 
	 SynchroData sd = new SynchroData();	 
	 sd.writeTvNavigateToDatabase();
	
}
}
