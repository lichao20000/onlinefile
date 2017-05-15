package cn.flying.rest.onlinefile.utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;

/**
 * @see JDBC连接关闭的工具类
 * @author yanggaofei 20121023
 *
 */
public class JdbcUtil {
	public static void close(ResultSet rs){
		try {
			if(rs!=null){
				rs.close();
				rs = null ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement st){
		try {
			if(st!=null){
				st.close();
				st = null ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection con){
		try {
			if(con!=null){
				con.close();
				con = null ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs, Statement st){
		try {
			if(rs!=null){
				rs.close();
				rs = null;
			}
			if(st!=null){
				st.close();
				st = null ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement st, Connection con){
		try {
			if(st!=null){
				st.close();
				st = null ;
			}
			if(con!=null){
				con.close();
				con = null ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs, Statement st, Connection con){
		try {
			if(rs!=null){
				rs.close();
				rs = null;
			}
			if(st!=null){
				st.close();
				st = null ;
			}
			if(con!=null){
				con.close();
				con = null ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 这个session千万不要乱关啊！
	 * 只有opensession的方式拿session的时候 才要关session的
	 * @param rs
	 * @param st
	 * @param con
	 * @param session
	 */
	@Deprecated
	public static void close(ResultSet rs, Statement st, Connection con,Session session){
		try {
			if(rs!=null){
				rs.close();
				rs = null;
			}
			if(st!=null){
				st.close();
				st = null ;
			}
			if(con!=null){
				con.close();
				con = null ;
			}
			if(session!=null){
				session.close();
				session = null ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * jiangyuntao 20121116 add 增加获取ID方法，通过最大ID加1方式。
	 * @param conn
	 * @param tableName
	 * @param pkColumn
	 * @return
	 */
	public static long getPrimaryKey(Connection conn,String tableName,String pkColumn){
		String sql = "select max("+pkColumn+")+1 from "+tableName;
		PreparedStatement  pstm = null;
		ResultSet rs = null;
		long id = -1 ;
		try {
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();
			if (rs.next()) {
				id = rs.getLong(1);
			}
			return id ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pstm);
		}
		return id ;
	}
	
	public static void closeConn(ResultSet rs, Statement st,Session session){
      try{
          if(rs != null){
              rs.close();
              rs = null;
          }
          if(st != null){
              st.close();
              st = null;
          }
          if(session != null && session.isOpen()){
              session.close();
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }
	
}
