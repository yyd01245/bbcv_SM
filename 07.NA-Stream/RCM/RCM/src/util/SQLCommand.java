package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SQLCommand {
	public String[] column = null;
	public SQLCommand() {
	}
	
	/**
	 * ��ȡ��һ�е�һ��  
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public String getFirstRecord(String sql) throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String resStr=null;
		try {
			conn = ConnectionUtil.getConn();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs == null){
				return resStr;
			}
			
			while (rs.next()) {
				resStr=String.valueOf(rs.getObject(1));
			}
		} catch (SQLException e) {
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return resStr;
	}
	
    
	@SuppressWarnings("unchecked")
	public ArrayList getRecord(String sql) throws SQLException {
 		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList rsList = new ArrayList();
		try {
			conn = ConnectionUtil.getConn();
			if(null!=conn){
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(rs == null){
					return rsList;
				}
				ResultSetMetaData rsmd = rs.getMetaData();
				while (rs.next()) {
					Object[] rsArr = new Object[rsmd.getColumnCount()];
					for (int i = 0; i < rsmd.getColumnCount(); i++) {
						rsArr[i]=rs.getObject(i+1);
					}
					rsList.add(rsArr);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null){
					rs.close();
					rs=null;
				}
			} catch (Exception e) {
			}
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
 		}
		return rsList;
	}
	
	/**
	 * ��ȡ������ݿ��¼
	 * @author shenggl
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List getMoreRecord(String sql) throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List strs = new ArrayList();
		try {
			conn = ConnectionUtil.getConn();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				strs.add(rs.getString(1));
			}
		} catch (SQLException e) {
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return strs;
	}
	@SuppressWarnings("unchecked")
	public Map getRsData(String sql) throws SQLException {
		Map map = new HashMap();
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			conn = ConnectionUtil.getConn();
			st = conn.createStatement();
			// ���?��
			rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			for (int i = 1; i <= cols; i++) {
				map.put(rsmd.getColumnName(i), rsmd.getColumnTypeName(i));
			}
		}catch (SQLException e) {
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return map;
	}
	/**
	 * ��ȡ���к�ֵ
	 * @author shenggl
	 * @param seqName
	 * @return
	 * @throws SQLException
	 */
	public int getSeqInit(String seqName) throws SQLException{
		int  seq_id = 0;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try{ 
			//String sql = "select " + seqName + ".NEXTVAL from dual";
			String sql = "select nextval('"+seqName+"') from dual";
			conn = ConnectionUtil.getConn();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				seq_id =  rs.getInt(1);
			}
			conn.commit();
		} catch (java.sql.SQLException e){
			conn.rollback();
			} catch (Exception e) {
				e.printStackTrace();
		}finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return seq_id;
	}


	
	public int execUpdate(String sql) throws SQLException, Exception {
		Connection conn = null;
		int rownum=0;
		try {
			conn = ConnectionUtil.getConn();
			rownum = execUpdate(conn, sql);
			conn.commit();
		} catch (java.sql.SQLException e) {
			conn.rollback();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return rownum;
	}
	public int execUpdate(Connection conn, String sql) throws SQLException {
		Statement st = null;
		int rownum=0;		
		try {
			st = conn.createStatement();
			rownum = st.executeUpdate(sql);
		} catch (java.sql.SQLException e) {
			conn.rollback();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
		}
		return rownum;
	}
	
	public boolean execInsert(Connection conn, String sql) throws SQLException {
		Statement st = null;
		boolean flag=true;		
		try {
			st = conn.createStatement();
			flag = st.execute(sql);
		} catch (java.sql.SQLException e) {
			conn.rollback();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
		}
		return flag;
	}
	public boolean execInsert(String sql) throws SQLException {
		Connection conn = null;
		Statement st = null;
		boolean flag=true;		
		try {
			conn = ConnectionUtil.getConn();
			st = conn.createStatement();
			flag = st.execute(sql);
		} catch (java.sql.SQLException e) {
			conn.rollback();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
		}
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	public int[] executeBatch(String[] sqls) throws SQLException {
		int[] rtnInt=null;
		Connection conn = null;
		Statement st = null;
		@SuppressWarnings("unused")
		ArrayList rsList = new ArrayList();
		try {
			conn = ConnectionUtil.getConn();
			st = conn.createStatement();
			if(sqls!=null && sqls.length>0){
				for(int i =0;i<sqls.length;i++){
					st.addBatch(sqls[i]);
				}		
			}
			rtnInt =st.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return rtnInt;
	}
	public void setColCount(int colCount) {
	}
	public void setTotalCount(int totalCount) {
	}
}
