package prod.nebula.service.dto;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.util.PropertiesUtil;
import prod.nebula.update.Info;

public class DatabaseQuary {
    
	private static final Logger logger = LoggerFactory.getLogger(DatabaseQuary.class);
	
	

	static String driver = "com.mysql.jdbc.Driver";  
	// MySQL服务器地址
	static String serverIp = PropertiesUtil.readValue("bind.mysql.server.ip");
	// MySQL数据库名称
	static String database = PropertiesUtil.readValue("bind.mysql.database");
	// MySQL配置时的端口
	static String port = PropertiesUtil.readValue("bind.mysql.server.port");
	// MySQL配置时的用户名
	static String user = PropertiesUtil.readValue("bind.mysql.database.user");
	// Java连接MySQL配置时的密码
	static String password = PropertiesUtil.readValue("bind.mysql.database.password");

	
	static String url = "jdbc:mysql://"+serverIp+":"+port+"/"+database+"?characterEncoding=utf8";


	  public static Connection getConn(){
			// 加载驱动程序
		  Connection conn = null;
		
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);			

			
		} catch (ClassNotFoundException e) {
			logger.error("数据库"+url+" 连接失败 ClassNotFoundException！ ",e.getMessage());
			//e.printStackTrace();
		}
		catch (SQLException e) {
			logger.error("数据库"+url+" 连接失败！SQLException",e.getMessage());
			//	e.printStackTrace();
			}
		
		return conn;
	  }

      
   public void quaryAndupdate(){
	   
	   String sql = "select iStreamID  from  ucs_stream_resource " ; 
	   
	   PreparedStatement ps  = null ;
	
	   Connection  conn = DatabaseQuary.getConn();
	try {
		ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		
		while(rs.next()){	
			
		   String	iStreamId  = rs.getString(1);
	    	 String page =  PropertiesUtil.readValue("navigation.page");
	    	 String pageIndex =  PropertiesUtil.readValue("navigation.page.index");
	    	 String ip= PropertiesUtil.readValue("ns.server.ip");
	    	 //vod1.jsp  gyc
	    		//	http://192.168.100.11:8181/NS/vod1.jsp?name=gyc&id=3 
	    	 String url = "http://"+ip+"/NS/"+page+"?name="+pageIndex+"&id="+iStreamId;
	    	 String updateSql = "update ucs_stream_resource u set  u.strNav_url= '"+url+"' WHERE u.iStreamID ="+iStreamId+"" ;
		   
		     ps.addBatch(updateSql);
		}

	   rs.close();
       ps.executeBatch();
		
	} catch (SQLException e) {
		logger.error("数据库更新 str_url 时 错误");
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
        	   logger.error("数据库关闭时出错");
           }
	 
	}
	   
	   
   }
   
   
   
     //查询用户是否绑定
    public static String queryStatus(String streamid) throws SQLException{ 
    	
       Connection conn = DatabaseQuary.getConn();
       
 	   String sql = "select strBind_userID  from  stream_session s where s.istreamID="+streamid+" and ( strStreamType = 'D' or strStreamType = 'E' )" ; 
 	   
 	   PreparedStatement ps  = null ;
 	   
 	  ResultSet rs = null;
 	 String s = null;
 		try {
			ps = conn.prepareStatement(sql);
			rs =ps.executeQuery();
	 		
	 		
	 		while(rs.next()){
	 			s = rs.getString(1);
	 		
	 		}
		} catch (Exception e) {
		  //    logger.info("【SkipServlet】--------开始执行--------");	
		//	  logger.error("数据库连接时出错    请检查网络 ");
		}
 	 	try {
 	 		
 	 		
 
 	
	if(s!=null&&!"".equals(s)&&!"NULL".equals(s)){
		logger.debug("【NS-SkipServlet】实时查询，用户：【"+s+"】已绑定到流id=【"+streamid+"】的频道");	
	
    		return s;
    	}
 	}	finally {
        try{
        	   if(rs != null) {
                   rs.close();
                  }
            if(ps != null) {
             ps.close();
           
            }
            if(conn != null) {
             conn.close();
            }
           }catch(SQLException e) {
        		logger.error("数据库关闭时出错");
           }
	 
	}
    	return null;
    }
     
     
    public String getVodPageUrl(String streamid){
    	
 	   Connection conn = DatabaseQuary.getConn();
 	   String sql = "select vod_page_url  from  ucs_stream_resource s where s.istreamID="+streamid ; 
 	   
 	   PreparedStatement ps  = null ;
 	   
 	  ResultSet rs = null;
 	try {
 		ps = conn.prepareStatement(sql);
 		rs =ps.executeQuery();
 		
 		String s = null;
 		while(rs.next()){
 			s = rs.getString(1);
 		
 		}
 	
	if(s!=null&&!"".equals(s)){
		logger.info("vod_page_url = "+s)	;
    		return s;
    	}
 	} catch (SQLException e) {
 		  logger.error("sql ="+sql+" 语句执行时出错"+e.getMessage());
 	//	e.printStackTrace();
 	}	finally {
        try{
        	   if(rs != null) {
                   rs.close();
                  }
            if(ps != null) {
             ps.close();
           
            }
            if(conn != null) {
             conn.close();
            }
           }catch(SQLException e) {
        		logger.error("数据库关闭时出错");
           }
	 
	}
    	return null;
    }
     
    public boolean vodExist(String name_id){
    	
     Connection conn = DatabaseQuary.getConn();
     
      String sql = "select id  from  vodpage where name_id ='"+name_id+"'" ; 
    	 
  	   PreparedStatement ps  = null ;
  	   
   	  ResultSet rs = null;
   	try {
   		ps = conn.prepareStatement(sql);
   		rs =ps.executeQuery();
   	  return rs.next();	
   	} catch (SQLException e) {
   		  logger.error("sql ="+sql+" 语句执行时出错"+e.getMessage());
  
   	}	finally {
          try{
          	   if(rs != null) {
                     rs.close();
                    }
              if(ps != null) {
               ps.close();
             
              }
              if(conn != null) {
               conn.close();
              }
             }catch(SQLException e) {
          		logger.error("数据库关闭时出错");
             }
  	 
  	}
   	return false;
    }
    
  
    
    public int[] addVod(List<Info> list) throws UnsupportedEncodingException{
    	
    	
    	  Connection  conn = DatabaseQuary.getConn();
   	   PreparedStatement ps= null;
    	   
    	
       	String   insert_sql = "INSERT INTO vodpage (name_id,vod_page_url,info) VALUES (?,?,?)"; 
        int[] i ={}; 
    
    	try {
    	
    	    ps= conn.prepareStatement(insert_sql);
        	for (Info info : list) {
        	System.out.println(info.getName_id()+" "+info.getVod_page_url()+" "+info.getInfo());	
        	ps.setString(1, info.getName_id()); 
        	ps.setString(2, info.getVod_page_url()); 
        	ps.setString(3,info.getInfo());         
        
           ps.addBatch();     		
       	System.out.println("insert_sql = "+insert_sql+" ps "+ps.toString());
    		   
    		}

    	 
         i =   ps.executeBatch();
    		
    	} catch (SQLException e) {
    		logger.error("数据库更新 vodpage 时 错误");
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
            	   logger.error("数据库关闭时出错");
               }
    	 
    	}
 	  return i;
    }
    public String getVodPage(String name){
    	
  	   Connection conn = DatabaseQuary.getConn();
  	   String sql = "select vod_page_url  from  vodpage where name_id='"+name.trim()+"'" ; 
  	   
  	   PreparedStatement ps  = null ;
  	   
  	  ResultSet rs = null;
  	try {
  		ps = conn.prepareStatement(sql);
  		rs =ps.executeQuery();
  		
  		String s = null;
  		while(rs.next()){
  			s = rs.getString(1);
  		
  		}
  	
 	if(s!=null&&!"".equals(s)){
 		logger.info("vod_page_url = "+s)	;
     		return s;
     	}
  	} catch (SQLException e) {
  		  logger.error("sql ="+sql+" 语句执行时出错"+e.getMessage());
  	//	e.printStackTrace();
  	}	finally {
         try{
         	   if(rs != null) {
                    rs.close();
                   }
             if(ps != null) {
              ps.close();
            
             }
             if(conn != null) {
              conn.close();
             }
            }catch(SQLException e) {
         		logger.error("数据库关闭时出错");
            }
 	 
 	}
     	return null;
     }
     
   public static void main(String[] args) {
	   DatabaseQuary d = new DatabaseQuary();
	   
	boolean  s =    d.vodExist("22");
	System.out.println(s);
   }
}
