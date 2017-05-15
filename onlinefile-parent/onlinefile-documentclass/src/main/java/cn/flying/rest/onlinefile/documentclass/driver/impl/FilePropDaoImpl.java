package cn.flying.rest.onlinefile.documentclass.driver.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.flying.rest.onlinefile.documentclass.driver.FilePropDao;
import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;
import cn.flying.rest.onlinefile.utils.JdbcUtil;
@Repository("filePropDao")
public class FilePropDaoImpl extends BaseDaoHibernate implements FilePropDao{

	@SuppressWarnings("unchecked")
	public FilePropDaoImpl() {
		super(FilePropDaoImpl.class);
	}

	public Map<String,String> addFileProp(Map<String, String> map) {
		 Map<String,String> rtnMap = new HashMap<String, String>();
		 Session session =this.getSession(false);
		 @SuppressWarnings("deprecation")
		 Connection conn = session.connection();
		 PreparedStatement st = null;
		 ResultSet rs = null;
		try {
			String sql ="INSERT INTO USERFILEPROPTYPE(USERID,TITLE,TYPE,COMPANYID,ISNULL,LENGTH,DOTLENGTH,DESCRIPTION,ISSYSTEM)"
					+ " VALUES(?,?,?,?,?,?,?,?,?)";
			st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, Integer.parseInt(map.get("USERID")));
			st.setString(2, map.get("TITLE"));
			st.setString(3, map.get("TYPE"));
			st.setInt(4, Integer.parseInt(map.get("COMPANYID")));
			st.setString(5, map.get("ISNULL"));
			st.setInt(6, Integer.parseInt(map.get("LENGTH")));
			st.setInt(7, Integer.parseInt(map.get("DOTLENGTH")));
			st.setString(8, map.get("DESCRIPTION"));
			st.setString(9, map.get("ISSYSTEM"));
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if(rs.next()){
				rtnMap.put("id", rs.getInt(1)+"");
		        rtnMap.put("title", map.get("TITLE"));
		        rtnMap.put("success", "true");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			rtnMap.put("success", "false");
			rtnMap.put("msg", "SQLException");
		} finally {
			JdbcUtil.close(st, conn);
		}
		return rtnMap;
	}
	
	public boolean delFileProp(Map<String, String> filePropBeanMap) {
		Session session =this.getSession(false);
		@SuppressWarnings("deprecation")
		Connection conn = session.connection();
		PreparedStatement st = null;
		try {
			String sql ="DELETE FROM USERFILEPROPTYPE WHERE ID=?";
			st = conn.prepareStatement(sql);
			st.setInt(1, Integer.parseInt(filePropBeanMap.get("fileId")));
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			JdbcUtil.close(st, conn);
		}
		return true;
	}


	@SuppressWarnings("deprecation")
	public Map<String, Object> getFilePropLst(String userId,int start,int limit,String queryStr) {
		Map<String,Object> rtnMap = new HashMap<String, Object>();
		Session session = null;
	    Connection conn = null;
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    try {
	    	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	        Map<String,String> map = null;
	        String total = "0";
	        session = this.getSession(false);
	        conn = session.connection();
	        StringBuilder sql = new StringBuilder();
	        sql.append("select SQL_CALC_FOUND_ROWS ID,TYPE,COMPANYID,ISNULL,LENGTH,DOTLENGTH,DESCRIPTION,ISSYSTEM,TITLE from userfileproptype WHERE USERID=?");
	        if(queryStr!=null && queryStr.trim().length()>0){
	        	sql.append(" AND (TITLE LIKE ? OR DESCRIPTION LIKE ?) ");
	        }
	        sql.append(" LIMIT ?,? ");
	        st = conn.prepareStatement(sql.toString());
	        st.setInt(1, Integer.parseInt(userId));
	        int para = 0;
	        if(queryStr!=null && queryStr.trim().length()>0){
	        	para = 2;
	        	st.setString(2, "%"+queryStr+"%");
	        	st.setString(3, "%"+queryStr+"%");
	        }
	        st.setInt(2+para, start);
	        st.setInt(3+para, limit);
	        rs = st.executeQuery();
	        while(rs.next()){
	          map = new HashMap<String,String>();
	          map.put("ID", rs.getString("ID"));
	          map.put("TYPE", rs.getString("TYPE"));
	          map.put("COMPANYID",rs.getString("COMPANYID"));
	          map.put("ISNULL", rs.getString("ISNULL"));
	          map.put("LENGTH", rs.getString("LENGTH"));
	          map.put("DOTLENGTH", rs.getString("DOTLENGTH"));
	          map.put("DESCRIPTION", rs.getString("DESCRIPTION"));
	          map.put("ISSYSTEM", rs.getString("ISSYSTEM").equals("1")?"是":"否");
	          map.put("TITLE", rs.getString("TITLE"));
	          list.add(map);
	        }
	        rs = st.executeQuery("SELECT FOUND_ROWS()");
	        if(rs.next()){
	          total = rs.getString(1);
	        }
	        rtnMap.put("lst", list);
	        rtnMap.put("total", total);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JdbcUtil.close(rs, st, conn);
	    }
	    return rtnMap;
	}

	public Map<String,String> getFilePropById(String id){
		Map<String,String> rtnMap = new HashMap<String, String>();
		Session session = null;
	    Connection conn = null;
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    try {
	        session = this.getSession(false);
	        conn = session.connection();
	        StringBuilder sql = new StringBuilder();
	        sql.append("select ID,TYPE,COMPANYID,ISNULL,LENGTH,DOTLENGTH,DESCRIPTION,ISSYSTEM,TITLE from userfileproptype WHERE ID=?");
	        st = conn.prepareStatement(sql.toString());
	        st.setInt(1, Integer.parseInt(id));
	        rs = st.executeQuery();
	        if(rs.next()){
	        	rtnMap.put("ID", rs.getString("ID"));
	        	rtnMap.put("TYPE", rs.getString("TYPE"));
	        	rtnMap.put("COMPANYID",rs.getString("COMPANYID"));
	        	rtnMap.put("ISNULL", rs.getString("ISNULL"));
	        	rtnMap.put("LENGTH", rs.getString("LENGTH"));
	        	rtnMap.put("DOTLENGTH", rs.getString("DOTLENGTH"));
	        	rtnMap.put("DESCRIPTION", rs.getString("DESCRIPTION"));
	        	rtnMap.put("ISSYSTEM", rs.getString("ISSYSTEM").equals("1")?"是":"否");
	        	rtnMap.put("TITLE", rs.getString("TITLE"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JdbcUtil.close(rs, st, conn);
	    }
	    return rtnMap;
	}
	
	public boolean updateFilePropById(Map<String,String> paras){
		 Session session =this.getSession(false);
		 @SuppressWarnings("deprecation")
		 Connection conn = session.connection();
		 PreparedStatement st = null;
		try {
			String sql ="UPDATE USERFILEPROPTYPE SET TITLE=?,TYPE=?,ISNULL=?,LENGTH=?,DOTLENGTH=?,DESCRIPTION=? WHERE ID=?";
			st = conn.prepareStatement(sql);
			st.setString(1, paras.get("TITLE"));
			st.setString(2, paras.get("TYPE"));
			st.setString(3, paras.get("ISNULL"));
			st.setInt(4, Integer.parseInt(paras.get("LENGTH")));
			st.setInt(5, Integer.parseInt(paras.get("DOTLENGTH")));
			st.setString(6, paras.get("DESCRIPTION"));
			st.setInt(7, Integer.parseInt(paras.get("ID")));
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			JdbcUtil.close(st, conn);
		}
		return true;
	
	}
	
	@SuppressWarnings("deprecation")
	public int getFilePropCountByTitle(String title,String companyId){
		Session session = null;
	    Connection conn = null;
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    int count = 0;
	    try {
	        session = this.getSession(false);
	        conn = session.connection();
	        StringBuilder sql = new StringBuilder();
	        sql.append("SELECT COUNT(1) FROM USERFILEPROPTYPE WHERE TITLE=? AND COMPANYID = ?");
	        st = conn.prepareStatement(sql.toString());
	        st.setString(1, title);
	        st.setString(2, companyId);
	        rs = st.executeQuery();
	        if(rs.next()){
	        	count = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JdbcUtil.close(rs, st, conn);
	    }
	    return count;
	}
	
	public boolean delColumn(String tableName,String colName){

		Session session = null;
	    Connection conn = null;
	    PreparedStatement st = null;
	    try {
	        session = this.getSession(false);
	        conn = session.connection();
	        StringBuilder sql = new StringBuilder();
	        sql.append("ALTER TABLE ").append(tableName).append(" DROP COLUMN ").append(colName);
	        st = conn.prepareStatement(sql.toString());
	        st.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        if(e.toString().contains("check that column/key exists")){
	        	return true;
	        }
	        return false;
	    } finally {
	        JdbcUtil.close(null, st, conn);
	    }
	    return true;
	
	}
	
	public boolean addColumn(String sql){
		Session session = null;
	    Connection conn = null;
	    PreparedStatement st = null;
	    try {
	        session = this.getSession(false);
	        conn = session.connection();
	        st = conn.prepareStatement(sql.toString());
	        st.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        JdbcUtil.close(null, st, conn);
	    }
	    return true;
	}
	
	@SuppressWarnings("deprecation")
	public List<Map<String,String>> getFilePropLstByCompany(String companyId){

		Session session = null;
	    Connection conn = null;
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	    try {
	        session = this.getSession(false);
	        conn = session.connection();
	        StringBuilder sql = new StringBuilder();
	        sql.append("select  ID,TYPE,COMPANYID,ISNULL,LENGTH,DOTLENGTH,DESCRIPTION,ISSYSTEM,TITLE from userfileproptype WHERE COMPANYID=?");
	        st = conn.prepareStatement(sql.toString());
	        st.setString(1, companyId);
	        rs = st.executeQuery();
	        while(rs.next()){
	          Map<String,String> map = new HashMap<String,String>();
	          map.put("ID", rs.getString("ID"));
	          map.put("TYPE", rs.getString("TYPE"));
	          map.put("COMPANYID",rs.getString("COMPANYID"));
	          map.put("ISNULL", rs.getString("ISNULL"));
	          map.put("LENGTH", rs.getString("LENGTH"));
	          map.put("DOTLENGTH", rs.getString("DOTLENGTH"));
	          map.put("DESCRIPTION", rs.getString("DESCRIPTION"));
	          map.put("ISSYSTEM", rs.getString("ISSYSTEM").equals("1")?"是":"否");
	          map.put("TITLE", rs.getString("TITLE"));
	          list.add(map);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JdbcUtil.close(rs, st, conn);
	    }
	    return list;
	
	}
	
}
