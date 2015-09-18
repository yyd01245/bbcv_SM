package prod.nebula.vgw4sida.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


public class SQLCommand {
	private int colCount;

	public String[] column = null;
	private int totalCount;

	public SQLCommand() {
		totalCount = 0;
	}
	
	/**
	 * 获取第一行第一例 
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
			conn = DBConnection.getConnection();
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
	
    
	public ArrayList getRecord(String sql) throws SQLException {
 		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList rsList = new ArrayList();
		try {
			conn = DBConnection.getConnection();
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
	 * 获取多条数据库记录
	 * @author shenggl
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List getMoreRecord(String sql) throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List strs = new ArrayList();
		try {
			conn = DBConnection.getConnection();
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
	public Map getRsData(String sql) throws SQLException {
		Map map = new HashMap();
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			conn = DBConnection.getConnection();
			st = conn.createStatement();
			// 受理定义
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
	 * 获取序列号值
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
			conn = DBConnection.getConnection();
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
	public ArrayList execQuery(String sql, int offset, int length, String type) throws SQLException {
		Connection conn = null;
		try{
			conn = DBConnection.getConnection();
			return execQuery(conn, sql, offset, length, type);
		} catch (java.sql.SQLException e) {
			throw e;			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return new ArrayList();
		
	}
	public ArrayList execQuery(Connection conn, String sql, int offset, 
			int length, String type) throws SQLException {
		ArrayList list = new ArrayList();
		ResultSet rs = null;
		Statement st = null;
		try {
			st = conn.createStatement(1004, 1007);
			if ((length > 0 || offset > 0)) {// 需要分页
				String sqlcount = "";
				if (sql.toLowerCase().indexOf("group by") == -1) {
					sqlcount = "select /*+ First_rows */ count(*) " + sql.substring(sql.toLowerCase().indexOf(" from "));

					if (sqlcount.toLowerCase().indexOf("order by") != -1) {

						if (sqlcount.toLowerCase().indexOf("desc)") != -1
								&& sqlcount.toLowerCase().indexOf("from(select") != -1) {
							sqlcount = sqlcount.substring(0, sqlcount.toLowerCase().indexOf("order by"));
							sqlcount += ")";

						} else {
							sqlcount = sqlcount.substring(0, sqlcount.toLowerCase().indexOf("order by"));

						}
					}
				} else {
					sqlcount = "select /*+ First_rows */ count(*) from (" + sql + ")";
				}
				rs = st.executeQuery(sqlcount);
				rs.next();
				setTotalCount(rs.getInt(1));
				//rs.getString(1);
				rs.close();
				if (offset > 5000) {
					rs = st.executeQuery("select * from (" + sql + ") where rownum<" + (offset + length) + " minus "
							+ "select * from (" + sql + ") where rownum<" + (offset));
					offset = 0;
					length = 0;
				} else {
					rs = st.executeQuery(sql);
				}

			} else {
				rs = st.executeQuery(sql);
			}
			list = setRsToList(rs, type, offset, length);
		} catch (java.sql.SQLException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
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
		}
		return list;
	}

	private ArrayList setRsToList(ResultSet rs, String type, int offset, int length) throws SQLException, Exception {
		ArrayList list = new ArrayList();
		ArrayList colList = getRsData(rs);
		if (offset == 0 && length == 0) {
			for (; rs.next(); list.add(setToObj(rs, type, colList))) {
				;
			}
		} else {
			if (offset > 0) {
				rs.absolute(offset);
			}
			if (totalCount > offset + length) {
				for (int i = 0; i < length; i++) {
					rs.next();
					list.add(setToObj(rs, type, colList));
				}

			} else {
				for (; rs.next(); list.add(setToObj(rs, type, colList))) {
					;
				}
			}
		}

		return list;
	}
	public ArrayList getRsData(ResultSet rs) throws SQLException {
		ArrayList list = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		this.setColCount(cols);
		this.column = new String[cols];
		for (int i = 1; i <= cols; i++) {
			column[i - 1] = rsmd.getColumnName(i);
			Rsdata rd = new Rsdata();
			rd.setName(rsmd.getColumnName(i));
			rd.setType(rsmd.getColumnTypeName(i));
			list.add(rd);
		}
		return list;
	}
	private Object setToObj(ResultSet rs, String type, ArrayList colList) throws SQLException,Exception {
		Object obj;
		obj = Class.forName(type).newInstance();
		if (type.equals("prod.biluochun.ims.util.Record")) {
			for (int i = 1; i <= colList.size(); i++) {
				if (rs.getString(i) != null) {
					BeanUtils.setProperty(obj, "col" + i, rs.getString(i));
				}
			}
			return obj;
		}
		for (int i = 0; i < colList.size(); i++) {
			Rsdata rd = (Rsdata) colList.get(i);
			if (rd.getType() == "DATE") {
				if (rs.getDate(rd.getName()) != null) {
					java.sql.Date date = rs.getDate(rd.getName());
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
					BeanUtils.setProperty(obj, rd.getName().toLowerCase(), sdf.format(date));
				}

			} else {
				if (rs.getString(rd.getName()) != null) {
					BeanUtils.setProperty(obj, rd.getName().toLowerCase(), rs.getString(rd.getName()).trim());
				}
			}
		}

		return obj;
	}

	public int execUpdate(String sql) throws SQLException, Exception {
		Connection conn = null;
		int rownum=0;
		try {
			conn = DBConnection.getConnection();
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
			conn = DBConnection.getConnection();
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
	
	public int[] executeBatch(String[] sqls) throws SQLException {
		int[] rtnInt=null;
		Connection conn = null;
		Statement st = null;
		ArrayList rsList = new ArrayList();
		try {
			conn = DBConnection.getConnection();
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
		this.colCount = colCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
