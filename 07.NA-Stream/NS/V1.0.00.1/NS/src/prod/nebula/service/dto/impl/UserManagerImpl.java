package prod.nebula.service.dto.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prod.nebula.service.dto.UserInfo;
import prod.nebula.service.dto.UserManager;
import prod.nebula.service.util.ConnectionUtil;
import prod.nebula.service.util.SQLCommand;

public class UserManagerImpl implements UserManager {
	private static final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);
	private Connection conn = null;
	
	public UserInfo getUser(String keyword) {		
		conn = ConnectionUtil.getConn();
		String sql =" select user_id ,username,passwd ,vod_page,token ,user_status from user_info u where u.vod_page = "+keyword;
		PreparedStatement ps = null;
		System.out.println(sql);
		ResultSet rs = null;
		UserInfo u =new UserInfo();
	try {
		 ps =conn.prepareStatement(sql);
		
		rs = ps.executeQuery();

		while(rs.next()){
			
			u.setUserId(rs.getInt("user_id"));
			u.setUsername(rs.getString("username"));
			u.setPasswd(rs.getString("passwd"));
			u.setVodPage(rs.getString("vod_page"));
			u.setToken(rs.getString("token"));
			u.setUserStatus(rs.getInt("user_status"));
	
		}
		
	} catch (SQLException e) {
	//	logger.error("数据库查询出错！");
		e.printStackTrace();
	} finally {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			logger.error("【NS-UserManagerImpl】数据库查询后关闭资源错误");
		}
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			logger.error("【NS-UserManagerImpl】数据库查询后关闭资源错误");
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			logger.error("【NS-UserManagerImpl】数据库查询后关闭资源错误");
		}
	}
		
		return u;
	
	}
        
	
    @SuppressWarnings("unchecked")
	public List<Integer> getUserSatus(String keyword){
    	
    	SQLCommand sqlc = new SQLCommand();
    	
    	String sql = "select user_status from user_info u where u.vod_page = "+keyword;
    	List<Integer> list =	null;
    	try {
		 list =	sqlc.getMoreRecord(sql);
		} catch (SQLException e) {
			logger.error("【NS-UserManagerImpl】数据库查询出错！");
			e.printStackTrace();
		}
        return list;	
    }
	
    
    
	public List<UserInfo> getUserList(String keyword) {
		
		conn = ConnectionUtil.getConn();
		String sql =" select user_id ,username,passwd ,vod_page,token ,user_status from user_info u where u.vod_page = "+keyword;
		PreparedStatement ps = null;
		System.out.println(sql);
		ResultSet rs = null;
		List<UserInfo> list = null;
	try {
		 ps =conn.prepareStatement(sql);
		
		rs = ps.executeQuery();
      list = new ArrayList<UserInfo>();
		while(rs.next()){
			UserInfo u =new UserInfo();
			u.setUserId(rs.getInt("user_id"));
			u.setUsername(rs.getString("username"));
			u.setPasswd(rs.getString("passwd"));
			u.setVodPage(rs.getString("vod_page"));
			u.setToken(rs.getString("token"));
			u.setUserStatus(rs.getInt("user_status"));
			list.add(u);
		}
		
	} catch (SQLException e) {
		logger.error("【NS-UserManagerImpl】数据库查询出错！");
		e.printStackTrace();
	}finally {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			
		}
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			logger.error("【NS-UserManagerImpl】数据库查询后关闭资源错误");
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			logger.error("【NS-UserManagerImpl】数据库查询后关闭资源错误");
		}
	}

		return list;
		
		
		
	}
    
	/*
	 * 
	 * 从stream_resource 表 中读取 vod_page_url 同步到   user_info表中 的 vod_page
	 * 目的：为手机二次登陆访问（详情页）
	 */
	
	public boolean updateVodPage(String username,String streamid,String vodpage){
		
		conn = ConnectionUtil.getConn();
		 String sql = "update user_info u set  u.vod_page= '"+vodpage+"' WHERE user_status=2 and u.stream_id ="+streamid+" and u.username='"+username+"'" ;
		
		PreparedStatement ps = null;
		  logger.debug("【NS-UserManagerImpl】SQL语句："+sql);
		
	
	try {
		 ps =conn.prepareStatement(sql);
		
	  int total=    ps.executeUpdate();

	  if(total==1){
		  return true;
	  }else{
		  logger.debug("【NS-UserManagerImpl】数据库中没有对应的用户名或流id,故不能同步");
	  }
		
	} catch (SQLException e) {
		
		logger.error("【NS-UserManagerImpl】更新语句有错误，请检查您的SQL语句");
		logger.error("【NS-UserManagerImpl】SQL语句："+sql);

	}finally {
	
		}
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			logger.error("【NS-UserManagerImpl】数据库查询后关闭资源错误");
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			logger.error("【NS-UserManagerImpl】数据库查询后关闭资源错误");
		}
		return false;
		
	}
	
	
	 
}
