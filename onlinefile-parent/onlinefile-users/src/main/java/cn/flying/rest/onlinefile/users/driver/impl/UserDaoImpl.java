package cn.flying.rest.onlinefile.users.driver.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Repository;

import cn.flying.rest.onlinefile.users.driver.UserDao;
import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;
import cn.flying.rest.onlinefile.utils.CacheUtils;
import cn.flying.rest.onlinefile.utils.JdbcUtil;
import cn.flying.rest.onlinefile.utils.StringUtil;
import cn.flying.rest.platform.IServiceProvider;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoHibernate implements UserDao {

	public UserDaoImpl() {
		super(UserDaoImpl.class);
	}

	public Integer saveUser(Map<String, String> params) {
		int id = 0;
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "insert into users(COMPANYID,USERNAME,FULLNAME,PASSWORD,PORTRAIT,TELEOHONE,MOBILEPHONE,FAX,STATUS,ENABLED,CODE,EMAIL,ISADMIN) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1, params.get("COMPANYID"));
			pst.setString(2, params.get("USERNAME"));
			pst.setString(3, params.get("FULLNAME"));
			pst.setString(4, params.get("PASSWORD"));
			pst.setString(5, params.get("PORTRAIT"));
			pst.setString(6, params.get("TELEOHONE"));
			pst.setString(7, params.get("MOBILEPHONE"));
			pst.setString(8, params.get("FAX"));
			pst.setString(9, params.get("STATUS"));
			pst.setString(10, params.get("ENABLED"));
			pst.setString(11, params.get("CODE"));
			pst.setString(12, params.get("EMAIL"));
			pst.setString(13, params.get("ISADMIN"));
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return id;
	}


	@SuppressWarnings("deprecation")
	public boolean updateFirstLoginFlag(String id) {
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "UPDATE USERS SET FIR_LOGIN =0 WHERE ID = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.parseInt(id));
			return pst.executeUpdate()>0;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return false;
	}

	public HashMap<String, List<String>> getNotFirLoginWithCompany() {
		HashMap<String, List<String>> rtnMap = new HashMap<String, List<String>>();
		Session session = null;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "SELECT USERNAME,COMPANYID FROM USERS WHERE FIR_LOGIN = 0";
		try {
			session = this.getSession(false);
			conn = session.connection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String companyid = String.valueOf(rs.getInt("COMPANYID"));
				String username = rs.getString("USERNAME");
				if (!rtnMap.containsKey(companyid)) {
					rtnMap.put(companyid, new ArrayList<String>());
				}
				rtnMap.get(companyid).add(username);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
		    JdbcUtil.close(rs, st, conn);
		}
		return rtnMap;
	}
	
	public Map<String, String> getCompanyUserStatus(String companyId,String EMAIL){
		Map<String, String> maps = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
		//	String sql = "SELECT cu.USERID, cu.STATUS FROM USERS U, company_users cu WHERE u.id=cu.userid and (U.EMAIL=? or u.username=?) and cu.CLASSID=0 and cu.COMPANYID=?";
			String sql = "SELECT cu.USERID, cu.STATUS FROM USERS U, company_users cu WHERE u.id=cu.userid and (U.EMAIL=? or u.username=?) and cu.COMPANYID=?";
			pst = conn.prepareCall(sql);
			pst.setString(1, EMAIL);
			pst.setString(2, EMAIL);
			pst.setString(3, companyId);
			rs = pst.executeQuery();
			while (rs.next()) {
				maps.put("USERID", rs.getString("USERID")) ;
				maps.put("STATUS", rs.getString("STATUS")) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return maps;
	}
	
	public Map<String,String> existCompanyForCompanyUserTable(String userId, String companyId){
		Map<String,String> map = new HashMap<>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			String sql = "select status from company_users where companyid = ? and userid= ?";
			pst = conn.prepareCall(sql);
			pst.setString(1, companyId);
			pst.setString(2, userId);
			rs = pst.executeQuery();
			if(rs.next()){
				map.put("status", rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return map;
	}
	
	public Map<String, Map<String, String>> getUserInitInfo(String companyId) {
		Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
	    Session session = null;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    try {
	        session = getSession();
	        conn = session.connection();
	        String sql = "select u.ID, u.USERNAME, u.FULLNAME, u.FIR_LOGIN, u.ISADMIN,u.PORTRAIT,u.SIGNATURE,u.ORGID,c.NAME,cu.STATUS from users u left join company_users cu on u.ID=cu.USERID left join company c on cu.companyid=c.id where c.ID=? AND cu.STATUS=1";// 增加公司名日志用 liumingchao 
	        pst = conn.prepareCall(sql);
	        pst.setString(1, companyId);
	        rs = pst.executeQuery();
	        HashMap<String, String> map = null ;
	        while (rs.next()) {
	        	map = new HashMap<String, String>();
	            map.put("ID", rs.getString("ID"));
	            map.put("USERNAME", rs.getString("USERNAME"));
	            map.put("FULLNAME", rs.getString("FULLNAME"));
	            map.put("FIR_LOGIN", rs.getString("FIR_LOGIN")); 
	            map.put("ISADMIN", rs.getString("ISADMIN"));
	            map.put("PORTRAIT", rs.getString("PORTRAIT"));
	            map.put("SIGNATURE", rs.getString("SIGNATURE"));
	            map.put("ORGID", rs.getString("ORGID"));
	            map.put("COMPANYNAME", rs.getString("NAME"));
	            map.put("COMPANYSTATUS", rs.getString("STATUS"));
	            maps.put(rs.getString("USERNAME"), map) ;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
	    return maps;
	}
	
	@SuppressWarnings("deprecation")
	public Map<String, String> getOrgInfo(String companyId) {
		Map<String, String> maps = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			String sql = "select ID, ORGNAME from ORG where COMPANYID=?";
			pst = conn.prepareCall(sql);
			pst.setString(1, companyId);
			rs = pst.executeQuery();
			while (rs.next()) {
				maps.put(rs.getString("ID"), rs.getString("ORGNAME")) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return maps;
	}
	
	@SuppressWarnings("deprecation")
	public Map<String, String> getOneUserInfo(String id) {
		HashMap<String, String> map = new HashMap<String, String>();
		Session session = null;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    try {
	        session = getSession();
	        conn = session.connection();
	        String sql = "select u.ID,u.USERNAME, u.FULLNAME, u.FIR_LOGIN, cu.ISADMIN,u.PORTRAIT,u.SIGNATURE,u.ORGID,c.NAME from users u left join company_users cu on u.ID=cu.USERID left join company c on cu.companyid=c.id where u.ID=? or u.username=?";
	        pst = conn.prepareCall(sql);
	        pst.setString(1, id);
	        pst.setString(2, id);
	        rs = pst.executeQuery();
	        while (rs.next()) {
	            map.put("ID", rs.getString("ID"));
	            map.put("USERNAME", rs.getString("USERNAME"));
	            map.put("FULLNAME", rs.getString("FULLNAME"));
	            map.put("FIR_LOGIN", rs.getString("FIR_LOGIN"));
	            map.put("ISADMIN", rs.getString("ISADMIN"));
	            map.put("PORTRAIT", rs.getString("PORTRAIT"));
	            map.put("SIGNATURE", rs.getString("SIGNATURE"));
	            map.put("ORGID", rs.getString("ORGID"));
	            map.put("COMPANYNAME", rs.getString("NAME"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
	    return map ;
	}

	@SuppressWarnings("deprecation")
	public Map<String, Map<String, String>> getUserSingleSet(String companyId) {
		Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
	    Session session = null;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    try {
	        session = getSession();
	        conn = session.connection();
	        String sql = "SELECT s.ISDOWREMIND,s.ISUPREMIND,s.ISOPENSPACE,s.ISOPENGROUP,s.ISENTERSEND,s.UID FROM user_singleset s where s.companyid=?";
	        pst = conn.prepareCall(sql);
	        pst.setString(1, companyId);
	        rs = pst.executeQuery();
	        HashMap<String, String> map = null ;
	        String uid = null ;
	        while (rs.next()) {
	        	map = new HashMap<String, String>();
	        	uid = rs.getString("UID") ;
	            map.put("ID", uid);
	            map.put("ISDOWREMIND", rs.getString("ISDOWREMIND")==null?"1":rs.getString("ISDOWREMIND"));
	            map.put("ISUPREMIND", rs.getString("ISUPREMIND")==null?"1":rs.getString("ISUPREMIND"));
	            map.put("isOpenSpace", rs.getString("ISOPENSPACE")==null?"1":rs.getString("ISOPENSPACE"));
	            map.put("isOpenGroup", rs.getString("ISOPENGROUP")==null?"1":rs.getString("ISOPENGROUP"));
	            map.put("isEnterSend", rs.getString("ISENTERSEND")==null?"1":rs.getString("ISENTERSEND"));
	            maps.put(uid, map) ;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
	    return maps;
	}
	public Map<String, String> getUserInitInfo(String companyId, String username) {
		HashMap<String, String> map = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
//			String sql = "select ID, USERNAME, FULLNAME, FIR_LOGIN, ISADMIN,PORTRAIT from users where COMPANYID=? AND USERNAME=?";
//			String sql = "select u.ID, u.USERNAME, u.FULLNAME, u.FIR_LOGIN, u.ISADMIN,u.PORTRAIT,u.SIGNATURE,u.ORGID,c.name from users u LEFT JOIN company c ON u.COMPANYID=c.id where u.COMPANYID=? AND USERNAME=?";//增加公司名日志用 liumingchao
			String sql = "SELECT u.ID, u.USERNAME, u.FULLNAME, u.FIR_LOGIN, u.ISADMIN,u.PORTRAIT,u.SIGNATURE,u.ORGID,c.name FROM users u  LEFT JOIN company_users AS cu ON u.ID=cu.USERID   LEFT JOIN company c ON cu.COMPANYID=c.id   WHERE cu.COMPANYID=? AND USERNAME=?";
			pst = conn.prepareCall(sql);
			pst.setString(1, companyId);
			pst.setString(2, username);
			rs = pst.executeQuery();
			while (rs.next()) {
				map.put("ID", rs.getString("ID"));
				map.put("USERNAME", rs.getString("USERNAME"));
				map.put("FULLNAME", rs.getString("FULLNAME"));
				map.put("FIR_LOGIN", rs.getString("FIR_LOGIN"));
				map.put("ISADMIN", rs.getString("ISADMIN"));
				map.put("PORTRAIT", rs.getString("PORTRAIT"));
				map.put("COMPANYNAME", rs.getString("NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return map;
	}
	
	public List<Map<String, String>> getCompanyMembers(String companyId) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select ID, USERNAME, FULLNAME from users where COMPANYID=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", rs.getString("ID"));
                map.put("userName", rs.getString("USERNAME"));
                map.put("fullName", rs.getString("FULLNAME"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
	
	//分类下添加用户
	public int  addClassUser(Map<String, String> param){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		SimpleDateFormat format = null ;
		String createtime = null;
		int num = -1;
		try {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			createtime = format.format(System.currentTimeMillis());
			session = this.getSession(false);
			conn = session.connection();
			String sql = "insert into users (USERNAME,EMAIL,COMPANYID,CODE,STATUS,CREATETIME,CLASSID) values(?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, param.get("email"));
			pst.setString(2, param.get("email"));
			pst.setString(3, param.get("companyid"));
			pst.setString(4, param.get("code"));
			pst.setString(5, "-1");
			pst.setString(6,createtime);
			pst.setString(7,param.get("classId"));
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				num = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return num;
	}
	
	public int addCompanyUser(Map<String, String> param){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		SimpleDateFormat format = null ;
		String createtime = null;
		int num = -1;
		try {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			createtime = format.format(System.currentTimeMillis());
			session = this.getSession(false);
			conn = session.connection();
			String sql = "insert into users (USERNAME,EMAIL,COMPANYID,CODE,STATUS,CREATETIME)values(?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, param.get("email"));
			pst.setString(2, param.get("email"));
			pst.setString(3, param.get("companyid"));
			pst.setString(4, param.get("code"));
			pst.setString(5, "-1");
			pst.setString(6,createtime);
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				num = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return num;
	}
	
	

//	public int adduser(Map<String, Object> param) {
//		Map<String, Object> rtnMap = new HashMap<String, Object>();
//		Session session = null;
//		Connection conn = null;
//		PreparedStatement pst = null;
//		ResultSet rs = null;
//		SimpleDateFormat format = null ;
//		String createtime = null;
//		int num = -1;
//		try {
//			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//			createtime = format.format(System.currentTimeMillis());
//			session = this.getSession(false);
//			conn = session.connection();
//			//user表里插入
//			String sql = "insert into users (EMAIL,FULLNAME,COMPANYID,CODE,STATUS,CREATETIME) values('"+param.get("email")+"','"+param.get("username")+"','"+param.get("companyid")+"','"+param.get("code")+"','-3','"+createtime+"')";
//			pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
//			pst.executeUpdate();
//			rs = pst.getGeneratedKeys();
//			if(rs.next()){
//				num = rs.getInt(1);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//	        JdbcUtil.close(rs, pst, conn);
//	    }
//		return num;
//	}
	public int adduser(Map<String, Object> param) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		SimpleDateFormat format = null ;
		String createtime = null;
		int num = -1;
		try {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			createtime = format.format(System.currentTimeMillis());
			session = this.getSession(false);
			conn = session.connection();
			//user表里插入
			String sql="";
			if(param.get("classId")!=null && !param.get("classId").equals("")){
				 sql = "insert into users (USERNAME,EMAIL,COMPANYID,CODE,STATUS,CREATETIME,CLASSID) values('"+param.get("email")+"','"+param.get("email")+"','"+param.get("companyid")+"','"+param.get("code")+"','-3','"+createtime+"','"+param.get("classId")+"')";
			}else{
				 sql = "insert into users (USERNAME,EMAIL,COMPANYID,CODE,STATUS,CREATETIME) values('"+param.get("email")+"','"+param.get("email")+"','"+param.get("companyid")+"','"+param.get("code")+"','-3','"+createtime+"')";
			}
			pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				num = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return num;
	}
	
	public int saveUserJustCode(Map<String, String> param){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		SimpleDateFormat format = null ;
		String createtime = null;
		int num = -1;
		try {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			createtime = format.format(System.currentTimeMillis());
			session = this.getSession(false);
			conn = session.connection();
			//user表里插入
			String sql = "insert into users (COMPANYID,CODE,STATUS,CREATETIME) values('"+param.get("companyId")+"','"+param.get("code")+"','-3','"+createtime+"')";
			pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				num = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return num;
	}
	
	public boolean insertIntoGroupUsersRalationTable(Map<String, String> params){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst= null;
		int rtn = -1;
		String sql = "insert into groupusersrelation (GROUPID,USERID,ISADMIN) values(?,?,0)";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("groupid"));
			pst.setString(2, params.get("id"));
			rtn = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(null, pst, conn);
	    }
		return rtn == -1?false:true;
	}
	
	public int  getOrgIdByOrgName(String orgName){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst= null;
		ResultSet rs = null;
		int rtn = -1;
		String sql = "select ID from ORG where ORGNAME=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, orgName);
			rs = pst.executeQuery();
			if(rs.next()){
				 rtn= rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return rtn;
	}
	public int  getUserIdByUserName(String userName){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int rtn = -1;
		String sql = "select ID from users where USERNAME=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, userName);
			rs = pst.executeQuery();
			if(rs.next()){
				 rtn=rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return rtn;
	}
	public boolean editUserInfo(Map<String, String> params){
		Session session = null ;
		Connection conn = null ;
		PreparedStatement st = null;
		boolean flag = false;
		StringBuilder sqlbuilder = new StringBuilder(100);
		List<String> values = new ArrayList<String>() ;
		sqlbuilder.append("update users set");
		if(params.get("fullname")!=null){
			sqlbuilder.append(" FULLNAME=?,");
			values.add(params.get("fullname")) ;
		}
		if(params.get("signature")!=null){
			sqlbuilder.append(" SIGNATURE=?,");
			values.add(params.get("signature")) ;
		}
		if(params.get("mobilephone")!=null){
			sqlbuilder.append(" MOBILEPHONE=?,");
			values.add(params.get("mobilephone")) ;
		}
		if(params.get("telephone")!=null){
			sqlbuilder.append(" TELEOHONE=?,");
			values.add(params.get("telephone")) ;
		}
		if(params.get("email")!=null){
			sqlbuilder.append(" EMAIL=?,");
			values.add(params.get("email")) ;
		}
		if(params.get("position")!=null){
			sqlbuilder.append(" POSITION=?,");
			values.add(params.get("position")) ;
		}
		if(params.get("org")!=null){
			sqlbuilder.append(" ORGID=?,");
			values.add(params.get("org")) ;
		}
		if(params.get("fax")!=null){
			sqlbuilder.append(" FAX=?");
			values.add(params.get("fax")) ;
		} else {
			sqlbuilder.deleteCharAt(sqlbuilder.length()-1) ;
		}
		sqlbuilder.append(" where ID=?");
		try {
			if(!values.isEmpty()){
				session = this.getSession(false);
				conn = session.connection();
				st = conn.prepareStatement(sqlbuilder.toString());
				int i=1 ;
				for(String value:values){
					st.setString(i, value);
					i++ ;
				}
				st.setInt(i, Integer.parseInt(params.get("id"))) ;
				flag = st.executeUpdate()>0 ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close( st,conn);
	    }
		return flag;
	}
	
	public String getPassWordById(String id){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String pas = null;
		String sql = "select PASSWORD from users where USERNAME=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			rs = pst.executeQuery();
			if(rs.next()){
				pas = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return pas;
	}
	
	public boolean modifyPassword(String newPassword, String id){
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		int rtn = -1;
		String sql = "update users set PASSWORD = ? where USERNAME=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, newPassword);
			pst.setString(2, id);
			rtn = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(pst, conn);
	    }
		
		return rtn ==-1?false:true;
	}
	
	public List<HashMap<String, Object>> getOrgByUserId(Map<String, String> params){
		List<HashMap<String, Object>> retList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> retMap = null;
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String companyid = params.get("companyId");
		try {
			String sql = "select ID,ORGNAME from org where COMPANYID=?";
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.parseInt(companyid));
			rs = pst.executeQuery();
			while(rs.next()){
				retMap = new HashMap<String, Object>();
				retMap.put("id", rs.getString("ID"));
				retMap.put("orgname", rs.getString("ORGNAME"));
				retList.add(retMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		
		return retList;
	}
	
	public int getcompanyIdByUserName(String id){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int rtn = -1;
		String sql = "select COMPANYID from users where USERNAME=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			rs = pst.executeQuery();
			if(rs.next()){
				 rtn= rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return rtn;
	}
	
	public boolean saveUserIdAndOrgId(int orgId,int userId){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int rtn = -1;
//		String sql = "insert into user_org (USERID,ORGID) values(?,?)";
		String sql = "update users set ORGID=? WHERE ID=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, orgId);
			pst.setInt(2, userId);
			rtn = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return rtn ==1?true:false;
	}
	
	@SuppressWarnings("deprecation")
	public String getCodeById(String id){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String rtn = null;
		String sql = "select CODE from users where ID=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.parseInt(id));
			rs = pst.executeQuery();
			if(rs.next()){
				 rtn= rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return rtn;
	}
	
	public String getCompanyNameById(String companyid){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String rtn = null;
		String sql = "select NAME from company where ID=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, companyid);
			rs = pst.executeQuery();
			if(rs.next()){
				 rtn= rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		
		return rtn;
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean activateAccount(Map<String, String> params){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		int rtn = -1;
		Md5PasswordEncoder md5 = new Md5PasswordEncoder(); 
		String password = md5.encodePassword(params.get("password"), params.get("username")) ;
		String sql = "update users set FULLNAME=?,USERNAME=?,PASSWORD=?,CODE=?,STATUS=1,ENABLED=1 where ID=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("fullname"));
			pst.setString(2, params.get("username"));
			pst.setString(3, password);
			pst.setString(4, null);
			pst.setInt(5, Integer.parseInt(params.get("id")));
			rtn = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return rtn==-1?false:true;
	}
	
	public Map<String, Object> getUserInfoByUserName(String username){
		Map<String,Object> reutrnMap = new HashMap<String, Object>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
//		String sql = "select ID,FULLNAME,TELEOHONE,MOBILEPHONE,FAX,EMAIL,SIGNATURE,POSITION,PORTRAIT,ORGID from users where USERNAME=?";
		String sql = "select U.ID,U.FULLNAME,U.TELEOHONE,U.MOBILEPHONE,U.FAX,U.EMAIL,U.SIGNATURE,U.POSITION,U.PORTRAIT,U.ISADMIN,U.STATUS,U.USERNAME, U.COMPANYID from USERS U where U.USERNAME =?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			rs = pst.executeQuery();
			if(rs.next()){
				reutrnMap.put("ID", rs.getString(1));
				reutrnMap.put("FULLNAME", rs.getString(2));
				reutrnMap.put("TELEOHONE", rs.getString(3));
				reutrnMap.put("MOBILEPHONE", rs.getString(4));
				reutrnMap.put("FAX", rs.getString(5));
				reutrnMap.put("EMAIL", rs.getString(6));
				reutrnMap.put("SIGNATURE", rs.getString(7));
				reutrnMap.put("POSITION", rs.getString(8));
				reutrnMap.put("PORTRAIT", rs.getString(9));
				reutrnMap.put("ISADMIN", rs.getString(10));
				reutrnMap.put("STATUS", rs.getString(11));
				reutrnMap.put("USERNAME", rs.getString(12));
				reutrnMap.put("COMPANYID", rs.getString("COMPANYID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		
		return reutrnMap;
	}
	
	public Map<String, String> getCompanyUserInfoByUserIdAndCompanyId(String userId, String companyId){
		Map<String,String> reutrnMap = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select u.USERID,u.COMPANYID,u.`STATUS`, u.ISADMIN from company_users u where u.USERID = ? and u.COMPANYID = ?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, userId);
			pst.setString(2, companyId);
			rs = pst.executeQuery();
			if(rs.next()){
				reutrnMap.put("USERID", rs.getString(1));
				reutrnMap.put("COMPANYID", rs.getString(2));
				reutrnMap.put("STATUS", rs.getString(3));
				reutrnMap.put("ISADMIN", rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return reutrnMap;
	}
	
	
	
	public Map<String, String> getDifferentClassForCompany(String userId,String companyId){
		Map<String,String> maps = new HashMap<>();
		Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select classid,status from company_users where userid = ? and companyid = ?";
        try {
            session = this.getSession(false);
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setString(2, companyId);
            rs = pst.executeQuery();
            if(rs.next()){
              maps.put("classid", rs.getString("classid"));
              maps.put("status", rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return maps;
	}
	
	   public Map<String, Object> getUserByUserNameAndCompanId(String username,int companyId){
	        Map<String,Object> reutrnMap = new HashMap<String, Object>();
	        Session session = null;
	        Connection conn = null;
	        PreparedStatement pst = null;
	        ResultSet rs = null;
	        String sql = "select U.ID ,U.FULLNAME,U.TELEOHONE,U.MOBILEPHONE,U.FAX,U.EMAIL,U.SIGNATURE,U.POSITION,U.PORTRAIT,U.ISADMIN,U.STATUS,U.USERNAME from USERS U where U.USERNAME =? and u.COMPANYID =?";
	        try {
	            session = this.getSession(false);
	            conn = session.connection();
	            pst = conn.prepareStatement(sql);
	            pst.setString(1, username);
	            pst.setInt(2, companyId);
	            rs = pst.executeQuery();
	            if(rs.next()){
	                reutrnMap.put("ID", rs.getString(1));
	                reutrnMap.put("FULLNAME", rs.getString(2));
	                reutrnMap.put("TELEOHONE", rs.getString(3));
	                reutrnMap.put("MOBILEPHONE", rs.getString(4));
	                reutrnMap.put("FAX", rs.getString(5));
	                reutrnMap.put("EMAIL", rs.getString(6));
	                reutrnMap.put("SIGNATURE", rs.getString(7));
	                reutrnMap.put("POSITION", rs.getString(8));
	                reutrnMap.put("PORTRAIT", rs.getString(9));
	                reutrnMap.put("ISADMIN", rs.getString(10));
	                reutrnMap.put("STATUS", rs.getString(11));
	                reutrnMap.put("USERNAME", rs.getString(12));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }finally {
	            JdbcUtil.close(rs, pst, conn);
	        }
	        
	        return reutrnMap;
	    }
	public List<HashMap<String, Object>> getUserListByCompanyId(int companyId,int stratNo, int limit){
		List<HashMap<String,Object>> rtnList = new ArrayList<HashMap<String,Object>>();
		
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
//		String sql = "select ID,USERNAME,FULLNAME,TELEOHONE,MOBILEPHONE,FAX,EMAIL,SIGNATURE,POSITION,STATUS from users where COMPANYID=? limit ?,?";
//		String sql = "select U.ID,U.USERNAME,U.FULLNAME,U.TELEOHONE,U.MOBILEPHONE,U.FAX,U.EMAIL,U.SIGNATURE,U.POSITION,cu.STATUS from company_users cu LEFT JOIN users U ON U.ID=cu.USERID where cu.COMPANYID=? AND u.ID IS NOT NULL AND cu.CLASSID=0 limit ?,?";
		String sql = "select U.ID,U.USERNAME,U.FULLNAME,U.TELEOHONE,U.MOBILEPHONE,U.FAX,U.EMAIL,U.SIGNATURE,U.POSITION,cu.STATUS from company_users cu LEFT JOIN users U ON U.ID=cu.USERID where cu.COMPANYID=? AND u.ID IS NOT NULL limit ?,?";
//		String sql="select U.ID,U.USERNAME,U.FULLNAME,U.TELEOHONE,U.MOBILEPHONE,U.FAX,U.EMAIL,U.SIGNATURE,U.POSITION,cu.STATUS from company_users cu LEFT JOIN users U ON U.ID=cu.USERID and u.COMPANYID = cu.COMPANYID where u.COMPANYID= ? limit ?,?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, companyId);
			pst.setInt(2, stratNo);
			pst.setInt(3, limit);
			rs = pst.executeQuery();
			while(rs.next()){
				HashMap<String,Object> reutrnMap = new HashMap<String, Object>();
				reutrnMap.put("ID", rs.getString(1));
				reutrnMap.put("USERNAME", rs.getString(2));
				reutrnMap.put("FULLNAME", rs.getString(3));
				reutrnMap.put("TELEOHONE", rs.getString(4));
				reutrnMap.put("MOBILEPHONE", rs.getString(5));
				reutrnMap.put("FAX", rs.getString(6));
				reutrnMap.put("EMAIL", rs.getString(7));
				reutrnMap.put("SIGNATURE", rs.getString(8));
				reutrnMap.put("POSITION", rs.getString(9));
				String sta=rs.getString(10);
				reutrnMap.put("STATUS", sta==null?"0":sta);
				rtnList.add(reutrnMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		
		
		return rtnList;
	}
	public List<HashMap<String, Object>> getUserListByCompanyIdAndUserName(int companyId,String userName,int stratNo, int limit){
		List<HashMap<String,Object>> rtnList = new ArrayList<HashMap<String,Object>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		//String sql ="select U.ID,U.USERNAME,U.FULLNAME,U.TELEOHONE,U.MOBILEPHONE,U.FAX,U.EMAIL,U.SIGNATURE,U.POSITION,cu.STATUS from company_users cu LEFT JOIN users U ON U.ID=cu.USERID where cu.COMPANYID=? AND cu.classid=0 AND u.id IS NOT NULL";
		String sql ="select U.ID,U.USERNAME,U.FULLNAME,U.TELEOHONE,U.MOBILEPHONE,U.FAX,U.EMAIL,U.SIGNATURE,U.POSITION,cu.STATUS from company_users cu LEFT JOIN users U ON U.ID=cu.USERID where cu.COMPANYID=? AND u.ID IS NOT NULL";
		if(userName != null && !userName.trim().equals("")){
			userName="%"+userName.trim()+"%";
			sql +=" and (U.USERNAME like '"+userName+"' or U.FULLNAME like '"+userName+"' or U.POSITION like '"+ userName+"' or U.MOBILEPHONE like '"+userName+"' ";
			userName=userName.replace("%", "");
			//启用1、禁用0、未激活-1、邀请邮件发送失败-2、邀请邮件发送中-3、未知
			if("邀请邮件发送".contains(userName)){
				sql +="or cu.STATUS IN (-2,-3) )";
			}else if("用".equals(userName)){
				sql +="or cu.STATUS IN (0,1) )";
			}else if("未".equals(userName)){
				sql +="or cu.STATUS NOT IN (1,0,-2,-3) )";
			}else if("启用".contains(userName)){
				sql +="or cu.STATUS = 1  )";
			}else if("禁用".contains(userName)){
				sql +="or cu.STATUS = 0  )";
			}else if("未激活".contains(userName)){
				sql +="or cu.STATUS = -1 )";
			}else if("邀请邮件发送失败".contains(userName)){
				sql +="or cu.STATUS = -2 )";
			}else if("邀请邮件发送中".contains(userName)){
				sql +="or cu.STATUS = -3 )";
			}else if("未知".contains(userName)){
				sql +="or cu.STATUS NOT IN (1,0,-1,-2,-3) )";
			}else{
				sql +=" )";
			}
		}
		sql +=" limit ?,?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, companyId);
			pst.setInt(2, stratNo);
			pst.setInt(3, limit);
			rs = pst.executeQuery();
			while(rs.next()){
				HashMap<String,Object> reutrnMap = new HashMap<String, Object>();
				reutrnMap.put("ID", rs.getString(1));
				reutrnMap.put("USERNAME", rs.getString(2));
				reutrnMap.put("FULLNAME", rs.getString(3));
				reutrnMap.put("TELEOHONE", rs.getString(4));
				reutrnMap.put("MOBILEPHONE", rs.getString(5));
				reutrnMap.put("FAX", rs.getString(6));
				reutrnMap.put("EMAIL", rs.getString(7));
				reutrnMap.put("SIGNATURE", rs.getString(8));
				reutrnMap.put("POSITION", rs.getString(9));
				reutrnMap.put("STATUS", rs.getString(10));
				rtnList.add(reutrnMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return rtnList;
	}
	
	public int getCountAllByCompanyId(Map<String, String> params){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		int rtn = -1;
		ResultSet rs = null;
		//String sql = "select count(FULLNAME) from users where COMPANYID=?";
		String sql = "";
		if(params.get("searchusername") != null && !params.get("searchusername").trim().equals("")){
			String userName=params.get("searchusername").trim();
			userName ="%"+userName+"%";
			sql ="SELECT count(1) FROM company_users cu LEFT JOIN users U ON U.ID=cu.USERID where cu.COMPANYID=? and (U.USERNAME like '"+userName+"' or U.FULLNAME like '"+userName+"' or U.POSITION like '"+ userName+"' or U.MOBILEPHONE like '"+userName+"' ";
			userName=userName.replace("%", "");
				//启用1、禁用0、未激活-1、邀请邮件发送失败-2、邀请邮件发送中-3、未知
				if("邀请邮件发送".contains(userName)){
					sql +="or cu.STATUS IN (-2,-3) )";
				}else if("用".equals(userName)){
					sql +="or cu.STATUS IN (0,1) )";
				}else if("未".equals(userName)){
					sql +="or cu.STATUS NOT IN (1,0,-2,-3) )";
				}else if("启用".contains(userName)){
					sql +="or cu.STATUS = 1  )";
				}else if("禁用".contains(userName)){
					sql +="or cu.STATUS = 0  )";
				}else if("未激活".contains(userName)){
					sql +="or cu.STATUS = -1 )";
				}else if("邀请邮件发送失败".contains(userName)){
					sql +="or cu.STATUS = -2 )";
				}else if("邀请邮件发送中".contains(userName)){
					sql +="or cu.STATUS = -3 )";
				}else if("未知".contains(userName)){
					sql +="or cu.STATUS NOT IN (1,0,-1,-2,-3) )";
				}else{
					sql +="   AND u.id IS NOT NULL )";
				}
		}else{
			sql="SELECT count(1) FROM company_users cu LEFT JOIN users U ON U.ID=cu.USERID where cu.COMPANYID=?  AND u.id IS NOT NULL";
		}
		
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("companyId"));
			rs = pst.executeQuery();
			if(rs.next()){
				rtn = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		
		return rtn;
		
	}
	public boolean deleteUserByUserId(String[] usernames){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		int rtn = -1;
		String sql = "delete from users where USERNAME=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			for(String username:usernames){
				pst.setString(1, username);
				pst.addBatch();
			}
			rtn = pst.executeBatch().length;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(pst, conn);
	    }
		return rtn==usernames.length?true:false;
		
	}
	public boolean deleteUserById(String[] ids){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		int rtn = -1;
		String sql = "delete from users where ID=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			for(String id:ids){
				pst.setString(1, id);
				pst.addBatch();
			}
			rtn = pst.executeBatch().length;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(pst, conn);
	    }
		return rtn==ids.length?true:false;
		
	}
	public int saveOrgByOrgNameReturnOrgId(String period, int companyid){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int num = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			//user表里插入
			String sql = "insert into org(COMPANYID,ORGNAME) values(?,?) ";
			pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, companyid);
			pst.setString(2,period);
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				num = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return num;
	}
	
	public String getOrgByOrgId(int newOrgId){
		Map<String,Object> reutrnMap = new HashMap<String, Object>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String rtn = null;
		String sql = "select ORGNAME from org where ID=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, newOrgId);
			rs = pst.executeQuery();
			if(rs.next()){
				rtn = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		
		return rtn;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean updateStatus(List<Integer> ids, int status) {
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		Boolean flag = true;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "UPDATE users SET STATUS=? WHERE ID=?";
			pst = conn.prepareStatement(sql);
			for(Integer id:ids){
				pst.setInt(1, status);
				pst.setInt(2, id);
				pst.addBatch();
			}
			flag = pst.executeBatch().length == ids.size();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return flag ;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public HashMap<String, String> getUserById(String id) {
		HashMap<String, String> user = new HashMap<String, String>();
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
//			String sql = "SELECT u.ID,u.USERNAME,u.FULLNAME,u.PORTRAIT,u.SIGNATURE,o.ORGNAME FROM USERS u LEFT JOIN user_org uo ON u.ID=uo.userid LEFT JOIN ORG o ON uo.ORGID=o.id WHERE u.ID=?" ;
			String sql = "select u.ID,u.USERNAME,u.FULLNAME,u.PORTRAIT,u.SIGNATURE from USERS u where u.ID=?" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(id));
			rs = pst.executeQuery() ;
			if(rs.next()){
				user.put("ID", rs.getString("ID")) ;
				user.put("USERNAME", rs.getString("USERNAME")) ;
				user.put("FULLNAME", rs.getString("FULLNAME")) ;
				String PORTRAIT = rs.getString("PORTRAIT") ;
				PORTRAIT = PORTRAIT==null?"":PORTRAIT ;
				user.put("PORTRAIT", PORTRAIT) ;
				user.put("SIGNATURE", rs.getString("SIGNATURE")) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return user ;
	}

	public boolean updateUser(Map<String, String> onlinefileUser){
		String COMPANYID = onlinefileUser.get("COMPANYID");
		String userId = onlinefileUser.get("userId");
		String USERNAME = onlinefileUser.get("USERNAME");
//		String FULLNAME = onlinefileUser.get("FULLNAME");
		String PASSWORD = onlinefileUser.get("PASSWORD");
//		String CODE = onlinefileUser.get("CODE");
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		int index = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "UPDATE users SET USERNAME = ? ,PASSWORD = ? ,CODE = ? WHERE ID=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, USERNAME);
			pst.setString(2, PASSWORD);
			pst.setString(3, null);
			pst.setString(4, userId);
			index = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return index >0?true:false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int saveRealUser(Map<String, String> params) {
		int id = 0;
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "insert into users(USERNAME,FULLNAME,PASSWORD,STATUS,ENABLED,EMAIL,COMPANYID) values(?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1, params.get("username"));
			pst.setString(2, params.get("fullname"));
			pst.setString(3, params.get("password"));
			pst.setString(4, "1");
			pst.setString(5, "1");
			pst.setString(6, params.get("email"));
			pst.setInt(7, Integer.parseInt(params.get("companyid")));
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			JdbcUtil.close(pst);
			//pst = conn.prepareStatement("INSERT INTO company_users(COMPANYID,USERID,ISADMIN,) VALUES(?,?,?)");
			pst = conn.prepareStatement("INSERT INTO company_users(COMPANYID,USERID,ISADMIN,STATUS) VALUES(?,?,?,1)");
			pst.setInt(1, Integer.parseInt(params.get("companyid")));
			pst.setInt(2, id);
			pst.setInt(3, params.get("isadmin")!=null?Integer.parseInt(params.get("isadmin")):1);
			pst.executeUpdate() ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return id;
	}
	public boolean saveHeadImage(Map<String, String> params){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		int temp = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "UPDATE users SET PORTRAIT=? WHERE USERNAME=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("path"));
			pst.setString(2, params.get("username"));
			temp = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return temp ==-1?false:true ;
	}
	
	public Map<String, String> getImagePath(Map<String, String> params){
		Map<String, String> rtnMap = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null ;
		int temp = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "SELECT PORTRAIT FROM users WHERE USERNAME=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("username"));
			rs = pst.executeQuery();
			if(rs.next()){
				rtnMap.put("path", rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return rtnMap ;
	}
	
	/**
	 * 根据userid获得详细信息#
	 * @param param
	 * @return 
	 */
	public HashMap<String, Object> getUserInfoByUserId(Integer id){
		HashMap<String,Object> reutrnMap = new HashMap<String, Object>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
//		String sql = "SELECT U.ID ,U.USERNAME,U.FULLNAME,U.TELEOHONE,U.MOBILEPHONE,U.FAX,U.EMAIL,U.SIGNATURE,U.POSITION,U.STATUS,C.ID,C.NAME,O.ID,O.ORGNAME FROM  USERS U LEFT JOIN  COMPANY C ON U.COMPANYID=C.ID LEFT JOIN USER_ORG UO ON U.ID = UO.USERID LEFT JOIN ORG O ON UO.ORGID = O.ID WHERE U.ID=?";
		String sql = "select U.ID ,U.USERNAME,U.FULLNAME,U.TELEOHONE,U.MOBILEPHONE,U.FAX,U.EMAIL,U.SIGNATURE,U.POSITION,U.STATUS,C.ID,C.NAME from company_users cu LEFT JOIN users U ON U.ID=cu.USERID LEFT JOIN company c on cu.COMPANYID = c.ID where U.ID =?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if(rs.next()){
				reutrnMap.put("ID", rs.getString(1));
				reutrnMap.put("USERNAME", rs.getString(2));
				reutrnMap.put("FULLNAME", rs.getString(3));
				reutrnMap.put("TELEPHONE", rs.getString(4));
				reutrnMap.put("MOBILEPHONE", rs.getString(5));
				reutrnMap.put("FAX", rs.getString(6));
				reutrnMap.put("EMAIL", rs.getString(7));
				reutrnMap.put("SIGNATURE", rs.getString(8));
				reutrnMap.put("POSITION", rs.getString(9));
				reutrnMap.put("STATUS", rs.getString(10));
				reutrnMap.put("COMPID", rs.getString(11));
				reutrnMap.put("COMPNAME", rs.getString(12));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		
		return reutrnMap;
	}
	/**
	 * 修改用户信息
	 * @param param
	 * @return 
	 */
	public boolean editUser(Map<String, Object> param){
		Session session = null ;
		Connection conn = null ;
		PreparedStatement st = null;
		boolean flag = false;
		StringBuilder sqlbuilder = new StringBuilder(100);
		List<Object> values = new ArrayList<Object>() ;
		sqlbuilder.append("UPDATE USERS SET");
		if(param.get("editFullname")!=null){
			sqlbuilder.append(" FULLNAME=?,");
			values.add(param.get("editFullname")) ;
		}
		if(param.get("editCompanyId")!=null){
			sqlbuilder.append(" COMPANYID=?,");
			values.add(param.get("editCompanyId")) ;
		}
		if(param.get("editMobilephone")!=null){
			sqlbuilder.append(" MOBILEPHONE=?,");
			values.add(param.get("editMobilephone")) ;
		}
		if(param.get("editTelephone")!=null){
			sqlbuilder.append(" TELEOHONE=?,");
			values.add(param.get("editTelephone")) ;
		}
		if(param.get("editEmail")!=null){
			sqlbuilder.append(" EMAIL=?,");
			values.add(param.get("editEmail")) ;
		}
		if(param.get("editPosition")!=null){
			sqlbuilder.append(" POSITION=?,");
			values.add(param.get("editPosition")) ;
		}
		if(param.get("editFax")!=null){
			sqlbuilder.append(" FAX=?,");
			values.add(param.get("editFax")) ;
		} 
		if(param.get("editUsername")!=null){
			sqlbuilder.append(" USERNAME=?,");
			values.add(param.get("editUsername")) ;
		} 
		if(param.get("editOrgid")!=null){
			sqlbuilder.append(" ORGID=?,");
			values.add(param.get("editOrgid")) ;
		}
		if(param.get("STATUS")!=null){
			sqlbuilder.append(" STATUS=?,");
			values.add(param.get("STATUS")) ;
		}
		if(param.get("editCode")!=null){
			sqlbuilder.append(" CODE=?");
			values.add(param.get("editCode")) ;
		} else {
			sqlbuilder.deleteCharAt(sqlbuilder.length()-1) ;
		}
		sqlbuilder.append(" WHERE ID=?");
		try {
			if(!values.isEmpty()){
				session = this.getSession(false);
				conn = session.connection();
				st = conn.prepareStatement(sqlbuilder.toString());
				int i=1 ;
				for(Object value:values){
					st.setObject(i, value);
					i++ ;
				}
				st.setInt(i, Integer.parseInt(param.get("editUserid").toString())) ;
				flag = st.executeUpdate() == 1 ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close( st,conn);
	    }
		return flag;
	}
	
	public int importUser(Map<String, Object> param) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int num = -1;
		SimpleDateFormat format = null ;
		String createtime = null;
		try {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			createtime = format.format(System.currentTimeMillis());
			session = this.getSession(false);
			conn = session.connection();
			//user表里插入
			String sql = "INSERT INTO USERS (COMPANYID,FULLNAME,TELEOHONE,MOBILEPHONE,FAX,STATUS,CODE,EMAIL,POSITION,CREATETIME) VALUES ('"+param.get("companyidImport")+"','"+param.get("fullNameImport")+"','"+param.get("teleohoneImport")+"','"+param.get("mobilephoneImport")+"','"+param.get("faxImport")+"','-3','"+param.get("codeImport")+"','"+param.get("emailImport")+"','"+param.get("positionImport")+"','"+createtime+"')";
			pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
				num = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return num;
	}
	public boolean modifyOrg(String orgid, String orgname){
		Map<String, String> rtnMap = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		int index = -1 ;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "update org set ORGNAME=? WHERE ID=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, orgname);
			pst.setString(2, orgid);
			index = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return index == -1?false:true ;
		
	}
	public String getCreateTime(Map<String, String> params){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		String index = null ;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "select CREATETIME from users where ID=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("id"));
//			pst.setString(2, params.get("code"));
			rs = pst.executeQuery();
			if(rs.next()){
				index = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return index;
	}
	
	public boolean changeCreateTime(Map<String, String> params){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		boolean flag = false ;
		int index = -1;
		SimpleDateFormat format = null ;
		String createtime = null;
		try {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			createtime = format.format(System.currentTimeMillis());
			session = this.getSession(false);
			conn = session.connection();
			String sql = "update users set CREATETIME=? where ID=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1,createtime);
			pst.setString(2, params.get("userId"));
			index = pst.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return index==-1?false:true;
	}
	
	public Map<String, Object> checkUserExistByEmailAndUserName(Map<String, Object> params){
		Map<String, Object> rtMap = new HashMap<String, Object>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		String index = null;
		int temp = -1;
		ResultSet rs = null;
		SimpleDateFormat format = null ;
		String createtime = null;
		try {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			createtime = format.format(System.currentTimeMillis());
			session = this.getSession(false);
			conn = session.connection();
			String sql = "select ID,FULLNAME,CHECKTIME,CHECKCODE from users where USERNAME=? AND EMAIL=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1,params.get("username").toString());
			pst.setString(2, params.get("email").toString());
			rs = pst.executeQuery();
			if(rs.next()){
				rtMap.put("id", rs.getString(1));
				rtMap.put("fullname", rs.getString(2));
				rtMap.put("checkTime", rs.getString(3));
				rtMap.put("checkCode", rs.getString(4));
				rtMap.put("createtime", createtime);
			}
			if(rtMap.get("id") != null && rtMap.get("id") !=""){
				String sqltext = "update users set CREATETIME=? where USERNAME=?";
				pst = conn.prepareStatement(sqltext);
				pst.setString(1, createtime);
				pst.setString(2, params.get("username").toString());;
				temp = pst.executeUpdate();
				if(temp!=-1){
					rtMap.put("success", true);
				}
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return rtMap;
	}
	
	public boolean saveCheckCode(String id,String checktime,String checkcode){

		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "UPDATE USERS SET checktime =?,checkcode=? WHERE ID = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, checktime);
			pst.setString(2, checkcode);
			pst.setInt(3, Integer.parseInt(id));
			return pst.executeUpdate()>0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
	
	}

	/**
	 * 验证邮箱是否激活</br>longjunhao 20150818 修改逻辑, 验证用户-企业是否激活
	 * 
	 * @param companyId 企业id
	 * @param emailStr 邮箱
	 * 
	 * @return <state,xxx>
	 *     firstInvite: users表里没有记录，第一次被邀请使用；</br>
	 *     active：     用户已激活</br>
	 *     nonactive:   用户未激活</br>
	 *     newCompanyInvite:   用户已经在某个company中，但不在参数companyId对应的company中。
	 */
	public HashMap<String, Object> verifyMailbox(String companyId, String emailStr) {
	    /**
	     * 用户的状态描述：
	     * 1、firstInvite:  sql查询没有记录，表示users表没有该邮件用户的记录，是第一次被邀请使用的用户    
	     * （users表里没有用户记录）
	     * 
	     * 2、active:       status >= 0 用户已激活。 不要重复发送邀请邮件 
	     * 3、nonactive:    status < 0 用户未激活。 可以重复发送邀请邮件  
	     * 4、newCompanyInvite:    sql能查询出来记录，但是记录中不包含companId的，表示有新的company邀请   
	     * （users表里已经有用户记录）
	     * 
	     * STATUS: -5 异常  -4不存在  -3邮件发送中  -2 邮件发送失败  -1 未激活   0禁用  1启用
	     */
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "SELECT cu.companyid, cu.STATUS FROM USERS U, company_users cu WHERE u.id=cu.userid and (U.EMAIL=? or u.username=?)";
//		boolean flg = false;
//		boolean isAnewSend = false;
		String state = "";
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, emailStr);
			pst.setString(2, emailStr);
			rs = pst.executeQuery();
			while (rs.next()) {
			    map.put(rs.getString("companyid"), rs.getInt("status"));
			}
			if (map.size() == 0) { // 邮箱没用过 firstInvite
			    state = "firstInvite";
			}else {  // 2、用户已经存在users表中
			    if (map.keySet().contains(companyId)) {
			        if (map.get(companyId) >= 0) { // 用户已经激活 active
			            state = "active";
			        } else{ // 用户没有激活    nonactive
			            state = "nonactive";
			        }
			    } else {
			        // 有新的企业邀请 newInvite
			        state = "newCompanyInvite";
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
//		retMap.put("flg",flg);//激活用户
//		retMap.put("isAnewSend",isAnewSend);//判断是否重新发送
		retMap.put("state", state);
		return retMap;
	}	
	public Map<String, String> getUserByUserId(String userId){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String, String> rtn = new HashMap<String, String>();
		String sql = "select USERNAME from users where ID=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.parseInt(userId));
			rs = pst.executeQuery();
			if(rs.next()){
				rtn.put("username", rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return rtn;
	}
	
	public boolean checkPasswordIsRight(String username, String userid,
			String md5password){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag= false;
		String sql = "select ID from users where ID=? and PASSWORD=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.parseInt(userid));
			pst.setString(2, md5password);
			rs = pst.executeQuery();
			if(rs.next()){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return flag;
		
	}
	
	public Map<String, String> deleteUserSubScribersByUserNames(Map<String,String> map){
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String[] chekedUsers = map.get("usernames").split(",");
        String sql = "delete from usersubscribe where subscribename =? ";
        Map<String, String> result = new HashMap<String, String>();
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            for (String chekedUser : chekedUsers) {
                pst.setString(1, chekedUser);
                pst.addBatch();
            }
           pst.executeBatch();
           conn.commit();
           result.put("success", "true");
           result.put("msg", "成功删除当前订阅人信息");
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("msg", "删除当前订阅人信息失败");
        } finally {
            JdbcUtil.close(pst, conn);
        }
    
		return result;
	}
	
	public Map<String,String> getUserStatusByEmail(String companyId, String email){

		Map<String,String> map = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "SELECT u.ID, cu.STATUS FROM USERS U, company_users cu WHERE u.id=cu.userid and U.EMAIL=? and cu.companyid=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, email);
			pst.setString(2, companyId);
			rs = pst.executeQuery();
			if(rs.next()){
				map.put("ID", rs.getString(1));
				map.put("STATUS", rs.getString(2));
			}else{
				map.put("ID", "-1");
				map.put("STATUS", "-4");; //不存在
			}
		} catch (SQLException e) {
			e.printStackTrace();
			map.put("ID", "-1");
			map.put("STATUS", "-5");  //异常
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return map;
	}

	
	

	@Override
	public Map<String, String> getSingleSet(Map<String, String> param) {
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String, String> rtn = new HashMap<String, String>();
		String sql = "select ISUPREMIND,ISDOWREMIND,ISOPENSPACE,ISOPENGROUP from user_singleset where UID=? and COMPANYID = ?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1,param.get("userid"));
			pst.setString(2,param.get("companyId"));
			rs = pst.executeQuery();
			if(rs.next()){
				rtn.put("isUpRemind", rs.getString(1));
				rtn.put("isDowRemind", rs.getString(2));
				rtn.put("isOpenSpace", rs.getString(3));
				rtn.put("isOpenGroup", rs.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return rtn;
	}

	@Override
	public boolean updateSingleSet(Map<String, String> params) {
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null; 
		boolean flg=false;
		String sql = "UPDATE user_singleset SET ISUPREMIND =?,ISDOWREMIND=?,ISOPENSPACE=?,ISOPENGROUP=?WHERE UID = ? and COMPANYID = ?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1,params.get("isUpRemind"));
			pst.setString(2,params.get("isDownRemind"));
			pst.setString(3,params.get("isOpenSpace"));
			pst.setString(4,params.get("isOpenGroup"));
			pst.setString(5,params.get("userid"));
			pst.setString(6,params.get("companyId"));
			if(pst.executeUpdate()>0){
				 flg=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return flg;
	}
	
	/**
	 * 更新评论回车发送
	 */
	public boolean updateCommentEnterSet(Map<String, String> params){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null; 
		boolean isSucess=false;
		String userId = params.get("userid");
		String isEnterSend = params.get("isEnterSend");
		String companyId = params.get("companyId");
		String sql = "UPDATE user_singleset SET ISENTERSEND=? WHERE UID = ? and COMPANYID = ?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, isEnterSend);
			pst.setString(2, userId);
			pst.setString(3, companyId);
			if(pst.executeUpdate()>0){
				 isSucess=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return isSucess;
	}
	
	public boolean addCommentEnterSet(Map<String, String> params){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean isSucess=false;
		String sql = "insert into user_singleset (UID,ISENTERSEND,companyid) values(?,?,?)";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("userid"));
			pst.setString(2, params.get("isEnterSend"));
			pst.setString(3,params.get("companyId"));
			if(pst.executeUpdate()>0)
				isSucess=true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return isSucess;
	}

	@Override
	public boolean addSingleSet(Map<String, String> params) {
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flg=false;
		String sql = "insert into user_singleset (UID,ISUPREMIND,ISDOWREMIND,ISOPENSPACE,ISOPENGROUP,companyid) values(?,?,?,?,?,?)";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("userid"));
			pst.setString(2, params.get("isUpRemind"));
			pst.setString(3, params.get("isDownRemind"));
			pst.setString(4, params.get("isOpenSpace"));
			pst.setString(5, params.get("isOpenGroup"));
			pst.setInt(6, Integer.parseInt(params.get("companyId")));
			if(pst.executeUpdate()>0)
				 flg=true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		return flg;
	}

	@Override
	public boolean saveCompanyUsers(Map<String, String> user) {
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement("INSERT INTO company_users(COMPANYID,USERID,ISADMIN,STATUS) VALUES(?,?,?,?)");
			pst.setInt(1, Integer.parseInt(user.get("companyid")));
			pst.setInt(2, Integer.parseInt(user.get("userid")));
			//pst.setInt(3, 1);
			pst.setInt(3, (user.get("isadmin")!=null && !"".equals(user.get("isadmin")))?Integer.parseInt(user.get("isadmin")):1);
			pst.setInt(4, (user.get("status")!=null && !"".equals(user.get("status")))?Integer.parseInt(user.get("status")):1);
			return pst.executeUpdate()>0 ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
		return false;
	}
	
	@Override
	public void cacheUserToCompanys(IServiceProvider compLocator) {
		HashMap<String, List<String>> utc = new HashMap<String, List<String>>();
		HashMap<String, String> companyAdmin = new HashMap<String, String>() ;
		HashMap<String, Set<String>> userClassId = new HashMap<String, Set<String>>() ;
		HashMap<String, List<HashMap<String, String>>> companyUserIsStatus = new HashMap<String, List<HashMap<String, String>>>() ;
		Set<String> userIds = new HashSet<>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null ;
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement("SELECT USERID,COMPANYID,ISADMIN,CLASSID,STATUS FROM company_users ORDER BY USERID, ISADMIN DESC,COMPANYID");
			rs = pst.executeQuery() ;
			while(rs.next()){
				HashMap<String, String> companyIsStatus = new HashMap<String, String>() ;
				String userId = rs.getString("USERID") ;
				String companyId = rs.getString("COMPANYID") ;
				String classId = rs.getString("CLASSID") ;
				String status = rs.getString("STATUS") ;
				companyIsStatus.put(companyId, status);
				if(utc.get(userId) == null){
					utc.put(userId, new ArrayList<String>()) ;
				}
				if(companyUserIsStatus.get(userId) == null){
					companyUserIsStatus.put(userId,new ArrayList<HashMap<String, String>>()) ;
				}
				
				if(!utc.get(userId).contains(companyId)){
					utc.get(userId).add(companyId) ;
				}
				
				companyUserIsStatus.get(userId).add(companyIsStatus);
				if(1==rs.getInt("ISADMIN"))companyAdmin.put(companyId, userId) ;
				//Set<String> userIds = new HashSet<>();
				if(-1 == rs.getInt("status")){
				    userIds = userClassId.get(companyId);
				    if(userIds==null) userIds = new HashSet<String>();
				    userIds.add(userId);
                    userClassId.put(companyId, userIds) ;
				}
				
				/*if(0==rs.getInt("classId") && -1==rs.getInt("status")){
					userIds.add(userId);
					userClassId.put(companyId, userIds) ;
				}*/
				/*if(0!=rs.getInt("classId")){
					userIds.add(userId);
					userClassId.put(companyId, userIds) ;
				}*/
				
			}
			CacheUtils.set(compLocator, "ofuserCompanys", utc);
			CacheUtils.set(compLocator, "ofcompanyAdmin", companyAdmin);
			CacheUtils.set(compLocator, "ofuserClassId", userClassId);
			//2015.0916.添加公司用户状态
			CacheUtils.set(compLocator, "ofcompanyUserIsStatus", companyUserIsStatus);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
	}
	
	
	//直接查询相关数据
		@Override
		public Map<String, String> LoginGetUserParticular(Map<String, String> params) {
			 HashMap<String, String> map = new HashMap<String, String>();
			Session session = null;
		    Connection conn = null;
		    PreparedStatement pst = null;
		    ResultSet rs = null;
		    try {
		        session = getSession();
		        conn = session.connection();
		        String sql = "SELECT u.ID,u.COMPANYID,u.TELEOHONE,u.FAX,u.USERNAME, u.FULLNAME,u.PASSWORD,u.PORTRAIT,u.MOBILEPHONE, u.EMAIL,u.SIGNATURE,u.POSITION,u.ORGID,u.CREATETIME,c.NAME,cu.ISADMIN,cu.STATUS FROM users AS u"
		        		+ " LEFT JOIN company_users AS cu ON u.ID=cu.USERID"
		        		+ " LEFT JOIN company AS c ON cu.COMPANYID=c.ID "
		        		+ " where u.USERNAME=? and u.PASSWORD=? and c.ID=?"; 
		        pst = conn.prepareCall(sql);
		        pst.setString(1, params.get("userName"));
		        pst.setString(2, params.get("userPwd"));
		        pst.setString(3, params.get("companyId"));
		        rs = pst.executeQuery();
		        while (rs.next()) {
		            map.put("id", rs.getString("ID"));
		            map.put("companyid", rs.getString("COMPANYID"));
		            map.put("username", rs.getString("USERNAME"));
		            map.put("fullname", rs.getString("FULLNAME"));
		            map.put("password", rs.getString("PASSWORD"));
		            map.put("portrait", rs.getString("PORTRAIT"));
		            map.put("mobilephone", rs.getString("MOBILEPHONE"));
		            map.put("status", rs.getString("STATUS"));
		            map.put("teleohone", rs.getString("TELEOHONE"));
		            map.put("fax", rs.getString("FAX"));
		            map.put("email", rs.getString("EMAIL"));
		            map.put("signature", rs.getString("SIGNATURE"));
		            map.put("position", rs.getString("POSITION"));
		            map.put("isadmin", rs.getString("ISADMIN"));
		            map.put("companyname", rs.getString("NAME"));
		            map.put("createtime", rs.getString("CREATETIME"));
		            map.put("orgid", rs.getString("ORGID"));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        JdbcUtil.close(rs, pst, conn);
		    }
		    return map;
		}
		public boolean checkCompany_UserIsIn(int companyid, String userDeId,
				String userDeId2){
			Session session = null;
			Connection conn = null;
			ResultSet rs = null;
			boolean flag = false;
			PreparedStatement pst = null;
			try {
				session = this.getSession(false);
				conn = session.connection();
				pst = conn.prepareStatement("select count(COMPANYID) from company_users where COMPANYID=? and USERID=? and CLASSID=?");
				pst.setInt(1, companyid);
				pst.setInt(2, Integer.parseInt(userDeId));
				pst.setInt(3, Integer.parseInt(userDeId));
				rs = pst.executeQuery();
				if(rs.next()){
					flag = rs.getInt(1)>0?true:false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs,pst, conn);
			}
			return flag;
		}
	
	//luiwei 检查数据是否存在	
	public Map<String,String> isexistNotes(String companyid, String classId,String userid){
		Map<String, String> map = new HashMap<>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql ="select STATUS  from company_users where COMPANYID = ? and CLASSID = ? and USERID= ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, companyid);
			pst.setString(2, classId);
			pst.setString(3, userid);
			rs = pst.executeQuery();
			if(rs.next()){
				map.put("status", rs.getString("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs,pst, conn);
		}
		return map;
	}
	
	public boolean saveCompanyUserTableForClass(String companyId, String classId, String userid){
		
		  Session session = null ;
	      Connection conn = null ;
	      PreparedStatement pst = null ;
	      boolean flag = false;
	      try {
	    	  session = getSession();
	          conn = session.connection();
	          String sql = "INSERT INTO company_users(COMPANYID,USERID,`CLASSID`,ISADMIN,status)VALUES(?,?,?,?,?)";
	          pst = conn.prepareStatement(sql);
				pst.setString(1, companyId);
				pst.setString(2, userid);
				pst.setString(3, classId);
				pst.setString(4, "0");
				pst.setString(5, "-1");
				flag = pst.executeUpdate()>0;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			 JdbcUtil.close(pst, conn);
		}
		return flag;
		
		/*Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		boolean flag = false;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "INSERT INTO company_users(COMPANYID,USERID,ISADMIN,status,classid)VALUES(?,?,?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, companyId);
			pst.setString(2, userid);
			pst.setString(3, "0");
			pst.setString(4, "-1");
			pst.setString(5, classId);
			flag = pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
		return flag^true;*/
	}
	
	public boolean saveCompanyUserTableForCompany(String companyid,String userDeId){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			// status=-1 : 所有邀请的用户默认显示
			pst = conn.prepareStatement("INSERT INTO company_users(COMPANYID,USERID,ISADMIN,status) VALUES(?,?,?,?)");
			pst.setString(1, companyid);
			pst.setString(2, userDeId);
			pst.setInt(3, 0);
			pst.setString(4, "-1");
			return pst.executeUpdate()>0 ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
		return false;
	}
	
	public boolean agreenInSideCompany(String companyid, String userId){
		Session session = null ;
		Connection conn = null;
		PreparedStatement pst = null;
		int index = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
//			String sql = "update company_users set CLASSID=0,STATUS=1 where COMPANYID=? and USERID=?";
			String sql = "update company_users set STATUS = ?,CLASSID = ? where COMPANYID=? and USERID=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, "1");
			pst.setString(2, "0");
			pst.setString(3, companyid);
			pst.setString(4, userId);
			index = pst.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn);
		}
		return index >0?true:false;
	}
	public boolean noAgreenInSideCompany(String companyId, String userId){
		Session session = null ;
		Connection conn = null;
		PreparedStatement pst = null;
		int index = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "delete from company_users where COMPANYID=? and USERID=?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.parseInt(companyId));
			pst.setInt(2, Integer.parseInt(userId));
			index = pst.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn);
		}
		return index >0?true:false;
	}
	//通过用户查询关联的用户class
		public List<Map<String, String>> getClassList(String companyId) {
			        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			        Session session = null ;
			        Connection conn = null ;
			        PreparedStatement pst = null ;
			        ResultSet rs = null ;
			        try {
			            session = getSession();
			            conn = session.connection();
			            String tableName = "files_" + companyId;
			            StringBuilder sql = new StringBuilder();
			            sql.append("select id, filename, creator, owner, createtime, idseq from ").append(tableName)
			                .append(" where CLASSID=1 and ISDELETE='0' and ISFILE='0' ")
			                .append(" order BY id");
			            pst = conn.prepareStatement(sql.toString());
			            rs = pst.executeQuery();
			            while (rs.next()) {
			                Map<String, String> classObj = new HashMap<String, String>();
			                String id = rs.getString("id");
			                classObj.put("id", id); // 当前数据id
			                classObj.put("className", StringUtil.string(rs.getString("fileName")));
			                classObj.put("userId", StringUtil.string(rs.getString("creator")));
			                classObj.put("owner", StringUtil.string(rs.getString("owner")));
			                classObj.put("createTime", StringUtil.string(rs.getString("createTime")));
			                classObj.put("idSeq", StringUtil.string(rs.getString("idSeq")) + id + ".");
			                list.add(classObj);
			            }
			        } catch (SQLException e) {
			            e.printStackTrace();
			        } finally {
			            JdbcUtil.close(rs, pst, conn);
			        }
			        return list;
			    }
		/**通过邮件查询ID是否存在*/
		@Override
		public String getUserId(String email) {
			String userId="";
			Session session = null;
			Connection conn = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			String sql = "SELECT id FROM users WHERE EMAIL =?";
			try {
				session = this.getSession(false);
				conn = session.connection();
				pst = conn.prepareStatement(sql);
				pst.setString(1, email);
				rs = pst.executeQuery();
				if (rs.next()) {
					userId = rs.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs, pst, conn);
			}
			return userId;
		}
		
		public boolean checkUserIsInCompany_User(int companyid, String userId){
			Session session = null;
			Connection conn = null;
			PreparedStatement pst = null;
			ResultSet rs = null;
			int index = -1;
			String sql = "SELECT count(*) FROM company_users WHERE companyid=? and userid=?";
			try {
				session = this.getSession(false);
				conn = session.connection();
				pst = conn.prepareStatement(sql);
				pst.setInt(1, companyid);
				pst.setInt(2, Integer.parseInt(userId));
				rs = pst.executeQuery();
				if (rs.next()) {
					index = rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				JdbcUtil.close(rs, pst, conn);
			}
			return index>0?true:false;
			
		}

    @Override
    public List<Map<String,String>> getUsersByFullName(Map<String, Object> param) {
      List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
      Session session = null;
      Connection conn = null;
      PreparedStatement pst = null;
      ResultSet rs = null;
      String companyId = param.get("companyid")+"";
      String fullname = param.get("fullname")+"";
      try {
          session = getSession();
          conn = session.connection();
          String sql = "select * from users where FULLNAME=?"; 
          pst = conn.prepareCall(sql);
          pst.setString(1, fullname);
          //pst.setInt(2, Integer.parseInt(companyId));
          rs = pst.executeQuery();
          HashMap<String, String> map = null ;
          while (rs.next()) {
              map = new HashMap<String, String>();
              map.put("ID", rs.getString("ID"));
              map.put("COMPANYID", rs.getString("COMPANYID"));
              map.put("USERNAME", rs.getString("USERNAME"));
              map.put("FULLNAME", rs.getString("FULLNAME"));
              map.put("FIR_LOGIN", rs.getString("FIR_LOGIN")); 
              map.put("ISADMIN", rs.getString("ISADMIN"));
              map.put("PORTRAIT", rs.getString("PORTRAIT"));
              map.put("SIGNATURE", rs.getString("SIGNATURE"));
              map.put("ORGID", rs.getString("ORGID"));
              userList.add(map) ;
          }
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          JdbcUtil.close(rs, pst, conn);
      }
      return userList;
    }
    
    private String list2String(List<Integer> stringList, String separator) {
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (Integer tmp_int : stringList) {
            if (flag) {
                result.append(separator);
            }else {
                flag=true;
            }
            result.append(tmp_int);
        }
        return result.toString();
    }
    
    private List<String> getUserNameByIds(List<Integer> ids){
    	
    	Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String sql = "select username from  users where id in (?)";
        List<String> results = new ArrayList<String>();
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, (list2String(ids,",")));
            rs = pst.executeQuery();
            while (rs.next()) {
            	results.add(rs.getString("username"));
            }
       
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs,pst, conn);
        }
        return results;
    
    }
    
    public boolean updateUserSubScribeStatus(List<Integer> ids){
    	Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String sql = "delete from  usersubscribe where subscribename = ?";
        int num = 0;
        try {
            session = getOpenSession();
            conn = session.connection();
            conn.setAutoCommit(false);
            pst = conn.prepareStatement(sql);
            List<String> userNames = getUserNameByIds(ids);
            for (String username : userNames) {
                pst.setString(1, username);
                pst.addBatch();
            }
            pst.executeBatch();
            conn.commit();
        } catch (SQLException e) {
        	try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            e.printStackTrace();
        } finally {
        	try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            JdbcUtil.close(null,pst, conn,session);
        }
        return num > 0;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean updateCompanyUserStatus(String companyId, List<Integer> ids, int status) {
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        Boolean flag = true;
        try {
            session = this.getSession(false);
            conn = session.connection();
//            String sql = "UPDATE company_users SET STATUS=? WHERE userID=? and companyid=?";
            String sql ="UPDATE company_users SET STATUS=?,CLASSID=? WHERE userID=? and companyid=?";
            pst = conn.prepareStatement(sql);
            for(Integer id:ids){
                pst.setInt(1, status);
                pst.setInt(2, 0);
                pst.setInt(3, id);
                //pst.setInt(3, Integer.parseInt(companyId));
                pst.setString(4,companyId);
                pst.addBatch();
            }
            flag = pst.executeBatch().length == ids.size();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(null, pst, conn) ;
        }
        return flag ;
    }
     public List<Map<String,String>> getCompanysByUserName(String username){
       Session session = null;
       Connection conn = null;
       PreparedStatement pst = null;
       ResultSet rs = null;
       String rtn = null;
       List<Map<String,String>> companyList = new ArrayList<Map<String,String>>();
       try {
    	   /** 20151021 xiayongcai 增加返回值 用户表的企业id,企业管理员，目前做为记忆企业id，记忆企业id表示：用户切换并退出之后的企业ID*/
//       String sql = "select c.* from users u LEFT JOIN company_users c on u.ID = c.USERID where u.USERNAME = ?";
       String sql = "select c.*,u.companyId AS memoryCompanyId from users u LEFT JOIN company_users c on u.ID = c.USERID where u.USERNAME = ? and c.isdel=0";
           session = this.getSession(false);
           conn = session.connection();
           pst = conn.prepareStatement(sql);
           pst.setString(1, username);
           rs = pst.executeQuery();
           Map<String, String> map = null ;
           while (rs.next()) {
               map = new HashMap<String, String>();
               map.put("USERID", rs.getString("USERID"));
               map.put("COMPANYID", rs.getString("COMPANYID"));
               map.put("STATUS", rs.getString("STATUS"));
               map.put("CLASSID", rs.getString("CLASSID"));
               map.put("ISADMIN", rs.getString("ISADMIN"));
               map.put("memoryCompanyId", rs.getString("memoryCompanyId"));
               companyList.add(map) ;
           }
       } catch (Exception e) {
           e.printStackTrace();
       }finally {
           JdbcUtil.close(rs, pst, conn);
       }
       return companyList;
    }
     public List<Map<String,String>> getCompanysByUserNameList(String username){
    	 Session session = null;
    	 Connection conn = null;
    	 PreparedStatement pst = null;
    	 ResultSet rs = null;
    	 List<Map<String,String>> companyList = new ArrayList<Map<String,String>>();
    	 try {
    		 StringBuffer sql = new StringBuffer();
    		 sql.append(" SELECT p.ID,p.NAME,cy.STATUS,cy.CLASSID FROM users AS u ");
    		 sql.append("  LEFT JOIN company_users AS cy  ON u.ID=cy.USERID ");
    		 sql.append("  LEFT JOIN company AS p  ON cy.COMPANYID=p.ID ");
    		 sql.append("  WHERE u.USERNAME=? ");
    		 session = this.getSession(false);
    		 conn = session.connection();
    		 pst = conn.prepareStatement(sql.toString());
    		 pst.setString(1, username);
    		 rs = pst.executeQuery();
    		 Map<String, String> map = null ;
    		 while (rs.next()) {
    			 map = new HashMap<String, String>();
    			 map.put("companyid", rs.getString("ID"));
    			 map.put("companyName", rs.getString("NAME"));
    			 map.put("STATUS", rs.getString("STATUS"));
    			 map.put("companyClassId", rs.getString("CLASSID"));
    			 companyList.add(map) ;
    		 }
    	 } catch (Exception e) {
    		 e.printStackTrace();
    	 }finally {
    		 JdbcUtil.close(rs, pst, conn);
    	 }
    	 return companyList;
     }
     
     public void saveInviteDetail(String companyId, String userId,
 			String groupId, String classId, String groupflag){
    	 Session session = null;
         Connection conn = null;
         PreparedStatement pst = null;
         String rtn = null;
         try {
         String sql = "insert into invitedetail values(?,?,?,?,?)";
             session = this.getSession(false);
             conn = session.connection();
             pst = conn.prepareStatement(sql);
             pst.setString(1, companyId);
             pst.setString(2, userId);
             pst.setString(3, groupId);
             pst.setString(4, groupflag);
             pst.setString(5, classId);
             pst.executeUpdate();
         } catch (Exception e) {
             e.printStackTrace();
         }finally {
             JdbcUtil.close(pst, conn);
         }
     }
     public boolean checkInvitedetailIsHave(String companyId, String userId,
 			String groupId, String classId, String groupflag){
 			Session session = null;
         Connection conn = null;
         PreparedStatement pst = null;
         int flag = -1;
         ResultSet rs = null;
         try {
         String sql = "select count(*) from invitedetail where companyid=? and userid=? and groupid=? and flag=? and classid=?";
             session = this.getSession(false);
             conn = session.connection();
             pst = conn.prepareStatement(sql);
             pst.setString(1, companyId);
             pst.setString(2, userId);
             pst.setString(3, groupId);
             pst.setString(4, groupflag);
             pst.setString(5, classId);
             rs = pst.executeQuery();
             if(rs.next()){
            	 flag = rs.getInt(1);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }finally {
             JdbcUtil.close(rs,pst, conn);
         }
         return flag >0?true:false ;
     }
     public List<HashMap<String, String>> getInviteDetail(String userid,
 			String companid){
    	 Session session = null;
         Connection conn = null;
         PreparedStatement pst = null;
         ResultSet rs = null;
         String rtn = null;
         List<HashMap<String,String>> companyList = new ArrayList<HashMap<String,String>>();
         try {
         String sql = "select groupid,flag,classid from invitedetail where companyid=? and userid=?";
             session = this.getSession(false);
             conn = session.connection();
             pst = conn.prepareStatement(sql);
             pst.setString(1, companid);
             pst.setString(2, userid);
             rs = pst.executeQuery();
             HashMap<String, String> map = null ;
             while (rs.next()) {
                 map = new HashMap<String, String>();
                 map.put("groupid", rs.getString(1));
                 map.put("flag", rs.getString(2));
                 map.put("classid", rs.getString(3));
                 companyList.add(map) ;
             }
         } catch (Exception e) {
             e.printStackTrace();
         }finally {
             JdbcUtil.close(rs, pst, conn);
         }
         return companyList;
    	 
     }
     
     public void deleteInvite(String companyId, String userId){
    	 Session session = null;
         Connection conn = null;
         PreparedStatement pst = null;
         String rtn = null;
         try {
         String sql = "delete from invitedetail where companyid=? and userid=?";
             session = this.getSession(false);
             conn = session.connection();
             pst = conn.prepareStatement(sql);
             pst.setString(1, companyId);
             pst.setString(2, userId);
             pst.executeUpdate();
         } catch (Exception e) {
             e.printStackTrace();
         }finally {
             JdbcUtil.close(pst, conn);
         }
    	 
     }
     public List<Map<String, String>> getUserByCompayList(String userId){
	       Session session = null;
	       Connection conn = null;
	       PreparedStatement pst = null;
	       ResultSet rs = null;
	       String rtn = null;
	       List<Map<String,String>> companyList = new ArrayList<Map<String,String>>();
	       try {
	       String sql = "select c.* from users u LEFT JOIN company_users c on u.ID = c.USERID where u.USERNAME = ?";
	           session = this.getSession(false);
	           conn = session.connection();
	           pst = conn.prepareStatement(sql);
	           pst.setString(1, userId);
	           rs = pst.executeQuery();
	           Map<String, String> map = null ;
	           while (rs.next()) {
	               map = new HashMap<String, String>();
	               map.put("USERID", rs.getString("USERID"));
	               map.put("COMPANYID", rs.getString("COMPANYID"));
	               map.put("STATUS", rs.getString("STATUS"));
	               companyList.add(map) ;
	           }
	       } catch (Exception e) {
	           e.printStackTrace();
	       }finally {
	           JdbcUtil.close(rs, pst, conn);
	       }
	       return companyList;
     }
     
     @Override
     public boolean isUserCompayExist(String userId){
    	 Session session = null;
 		Connection conn = null;
 		PreparedStatement pst = null;
 		ResultSet rs = null;
 		boolean flag= false;
 		String sql = "SELECT USERID FROM company_users WHERE USERID=? and ISADMIN=1";//ISADMIN企业管理员代表是注册企业人
 		try {
 			session = this.getSession(false);
 			conn = session.connection();
 			pst = conn.prepareStatement(sql);
 			pst.setInt(1, Integer.parseInt(userId));
 			rs = pst.executeQuery();
 			if(rs.next()){
 				flag = true;
 			}
 		} catch (SQLException e) {
 			e.printStackTrace();
 		} finally {
 	        JdbcUtil.close(rs, pst, conn);
 	    }
 		return flag;
     }
     
     @Override
     public String getUserImagePathByUserId(String userId){

    	String userImagePath = "";
 		Session session = null;
 		Connection conn = null;
 		PreparedStatement pst = null;
 		ResultSet rs = null ;
 		int temp = -1;
 		try {
 			session = this.getSession(false);
 			conn = session.connection();
 			String sql = "SELECT PORTRAIT FROM users WHERE ID=?";
 			pst = conn.prepareStatement(sql);
 			pst.setString(1, userId);
 			rs = pst.executeQuery();
 			if(rs.next()){
 				userImagePath = rs.getString(1);
 			}
 		} catch (SQLException e) {
 			e.printStackTrace();
 		} finally {
 			JdbcUtil.close(null, pst, conn) ;
 		}
 		return userImagePath ;
 	
     }
     
     /**
 	 *  wangwenshuo 20151119 用户登录后检测是否有用户表（files_user_N，files_trash_user_N等），没有就创建 
 	 */
 	public boolean createUserTables(String userId) {
 		Session session = null;
 		Connection conn = null;
 		PreparedStatement pst = null;
 		ResultSet rs = null;
 		boolean flag = false;
 		try {
 			session = this.getSession(false);
 			conn = session.connection();
			String tableName = "files_user_"+userId;
			rs = conn.getMetaData().getTables(null, null, tableName, null);
			if(rs.next()){
				//存在
			}else{  //不存在
				JdbcUtil.closeConn(rs, null, null);
				//xyc 20151211 添加字段3个字段，
				//UPDATETIME,更新时间是为了生成序列编号的排序，由于删除回来的文件需要排序到文件后面
				//SERIALNUMBER,序号编号格式(因为序号会变，所以要确定更改名字的文件必须有一个唯一编号)：0001、
				//SOLENUMBER,唯一编号格式：上传的同一个文件的唯一ID
				String sql = "CREATE TABLE "+tableName+" (`ID` INT (8) NOT NULL AUTO_INCREMENT,`CLASSID` INT (8) NOT NULL,`FILENAME` VARCHAR (2000)  NOT NULL,`CREATOR` INT (8)  NOT NULL,`OWNER` INT (8)  NOT NULL,`CREATETIME` VARCHAR (30),`UPDATETIME` VARCHAR (30),SERIALNUMBER VARCHAR (30),SOLENUMBER int (200),`SIZE` BIGINT (30) DEFAULT 0,`MD5` VARCHAR (32) ,`TYPE` VARCHAR (20) DEFAULT '',`PRAISECOUNT` INT (8) DEFAULT '0' NOT NULL,`COLLECTCOUNT` INT (8) DEFAULT '0' NOT NULL,`ISDELETE` enum('1','0') NOT NULL DEFAULT '0',`FILEID` VARCHAR (50) ,`IDSEQ` VARCHAR (250)  NOT NULL,`ISFILE` enum('1','0') NOT NULL DEFAULT '1',`VERSION` INT (11) DEFAULT '1' NOT NULL,`ISLAST` enum('1','0') NOT NULL DEFAULT '1', `OPENLEVEL`  enum('3','2','1','0') NULL DEFAULT '0',PRIMARY KEY (`ID`),INDEX `CLASSID` (`CLASSID`, `ISDELETE`, `ISLAST`) ,INDEX `ISFILE` (`ISFILE`),KEY `CREATOR` (`CREATOR`)) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8";
				pst = conn.prepareStatement(sql) ;
				pst.execute() ;
				
				JdbcUtil.closeConn(rs, pst, null);
				sql = "CREATE TABLE `files_trash_user_"+userId+"` (`ID` INT (8) NOT NULL AUTO_INCREMENT,`FROMID` INT (8) NOT NULL,`FILENAME` VARCHAR (2000)  NOT NULL,`TYPE` VARCHAR (20) DEFAULT '',`CREATOR` INT (8) NOT NULL ,`OWNER` INT (8) NOT NULL,`DELETEUSERID` INT (8),`DELETETIME` VARCHAR (30),`DESTROYUSERID` INT (8),`DESTROYTIME` VARCHAR (30),`ISDELETE` enum('1','0') NOT NULL DEFAULT '0',`IDSEQ` VARCHAR (250)  NOT NULL,`PATHNAME` VARCHAR (1024),`ISFILE` enum('1','0') NOT NULL DEFAULT '1', `OPENLEVEL`  enum('3','2','1','0') NULL DEFAULT '0',PRIMARY KEY (`ID`) ,INDEX `ISFILE` (`ISFILE`),KEY `CREATOR` (`CREATOR`)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8";
	            pst = conn.prepareStatement(sql) ;
	            pst.execute() ;
	            
	            JdbcUtil.closeConn(rs, pst, null);
	            sql = "CREATE TABLE `files_share_log_user_"+userId+"` ( `ID` int(8) NOT NULL AUTO_INCREMENT, `FILEID` int(11) NOT NULL, `FILENAME` varchar(200) NOT NULL DEFAULT '0', `TYPE` varchar(20) NOT NULL DEFAULT '', `COMPANYID` int(8) NOT NULL, `CLASSID` varchar(2000) NOT NULL, `VERSION` int(8) NOT NULL,`OPENLEVEL` enum('3','2','1','0') NOT NULL DEFAULT '0',`CREATETIME` varchar(30) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8";
	            pst = conn.prepareStatement(sql) ;
	            pst.execute() ;
	            
	            JdbcUtil.closeConn(rs, pst, null);
	            sql = "INSERT INTO `files_user_"+userId+"` (`ID`, `CLASSID`, `FILENAME`, `CREATOR`, `OWNER`, `CREATETIME`, `SIZE`, `MD5`, `TYPE`, `PRAISECOUNT`, `COLLECTCOUNT`, `ISDELETE`, `FILEID`, `IDSEQ`, `ISFILE`, `VERSION`, `ISLAST`, `OPENLEVEL`) VALUES ('1', '0', 'root', '1', '1', '2015-11-09 11:46:06', '0', '', '', '0', '0', '0', '', '', '0', '1', '1', '0')";
	            pst = conn.prepareStatement(sql) ;
	            pst.executeUpdate();
	            
	            JdbcUtil.closeConn(rs, pst, null);
	            sql = "INSERT INTO `files_user_"+userId+"` (`ID`, `CLASSID`, `FILENAME`, `CREATOR`, `OWNER`, `CREATETIME`, `SIZE`, `MD5`, `TYPE`, `PRAISECOUNT`, `COLLECTCOUNT`, `ISDELETE`, `FILEID`, `IDSEQ`, `ISFILE`, `VERSION`, `ISLAST`, `OPENLEVEL`) VALUES ('2', '1', '我的文档', '1', '1', '2015-11-09 11:46:06', '0', '', '', '0', '0', '0', '', '1.', '0', '1', '1', '0')";
	            pst = conn.prepareStatement(sql) ;
	            pst.executeUpdate();
	            
	            flag = true;
			}
 			
 		} catch (SQLException e) {
 		} finally {
 			JdbcUtil.close(rs, pst, conn) ;
 		}
 		return flag;
 	}


    @Override
    public HashMap<String, Object> verifyMailboxForClass(String companyId, String emailParam,
        String classId) {

      HashMap<String, Object> retMap = new HashMap<String, Object>();
      Session session = null;
      Connection conn = null;
      PreparedStatement pst = null;
      ResultSet rs = null;
      String sql = "SELECT cu.companyid,cu.classId,cu.status FROM USERS U, company_users cu WHERE u.id=cu.userid and u.username=?";
      String state = "";
      Map<String, String> map = new HashMap<String, String>();
      try {
          session = this.getSession(false);
          conn = session.connection();
          pst = conn.prepareStatement(sql);
          pst.setString(1, emailParam);
          rs = pst.executeQuery();
          String k = companyId+","+classId;
          while (rs.next()) {
               String key = rs.getString("companyid")+","+rs.getString("classId");
               map.put(key, rs.getString("status"));
          }
          if (map.size() == 0) { // 1、用户不存在users表中,firstInvite
                   state = "firstInvite";
          } else {
            
            String status = map.get(k);
            if(status!=null){
                 if(Integer.parseInt(status)>-1){
                   state = "classactive";//在此分类下已激活
                 }else{
                   state = "classnoactive";//在此分类邀请过没有激活
                 }
            }else{
                 String st = map.get(companyId+",0");
                 //是否是此公司下激活用户
                 if(null!=st){
                   if(Integer.parseInt(st)>-1){
                         state = "companyactive";
                   }else{
                         state = "companynoactive";
                   }
                 }else{
                         state = "nocompanyuser";
             }
            }            
          }
      } catch (SQLException e) {
          e.printStackTrace();
      } finally {
          JdbcUtil.close(rs, pst, conn);
      }

      retMap.put("state", state);
      return retMap;
    }
    
    public Map<String,String> getUserInfoByEmail(String emailParam){
      Session session = null;
      Connection conn = null;
      PreparedStatement pst = null;
      ResultSet rs = null;
      Map<String,String> maps = new HashMap<>();
      String sql = "select * from users WHERE USERNAME = ?";
      try {
          session = this.getSession(false);
          conn = session.connection();
          pst = conn.prepareStatement(sql);
          pst.setString(1, emailParam);
          rs = pst.executeQuery();
          if(rs.next()){
            maps.put("id", rs.getString("id"));
            maps.put("companyid", rs.getString("companyid"));
            maps.put("status", rs.getString("status"));
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }finally {
          JdbcUtil.close(rs, pst, conn);
      }
      return maps;
  }
    public boolean registerUser(Map<String, String> param){
      int num = 0;
      Session session = null;
      Connection conn = null;
      PreparedStatement pst = null;
      ResultSet rs = null;
      try {
          session = this.getSession(false);
          conn = session.connection();
          String sql = "insert into ios_user(id,USERNAME,PASSWORD) values(null,?,?)";
          pst = conn.prepareStatement(sql,
                  PreparedStatement.RETURN_GENERATED_KEYS);
          pst.setString(1, param.get("userName"));
          pst.setString(2, param.get("passWord"));
          num =  pst.executeUpdate();
         // rs = pst.getGeneratedKeys();
         return num>0;
          
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      } finally {
          JdbcUtil.close(rs, pst, conn);
      }
    }

    public boolean userLogin(Map<String, String> param){
      Session session = null;
      Connection conn = null;
      PreparedStatement pst = null;
      ResultSet rs = null;
      Map<String,String> maps = new HashMap<>();
      String sql = "select count(*) from ios_user WHERE USERNAME = ? and PASSWORD = ?";
      int num = 0;
      try {
          session = this.getSession(false);
          conn = session.connection();
          pst = conn.prepareStatement(sql);
          pst.setString(1, param.get("userName"));
          pst.setString(2, param.get("passWord"));
          rs = pst.executeQuery();
          if(rs.next()){
            num = rs.getInt(1);
          }
          
          return num>0;
      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }finally {
          JdbcUtil.close(rs, pst, conn);
      }
    }
   public boolean isExitsUserName(Map<String, String> param){
     Session session = null;
     Connection conn = null;
     PreparedStatement pst = null;
     ResultSet rs = null;
     Map<String,String> maps = new HashMap<>();
     String sql = "select count(*) from ios_user WHERE USERNAME = ?";
     int num = 0;
     try {
         session = this.getSession(false);
         conn = session.connection();
         pst = conn.prepareStatement(sql);
         pst.setString(1, param.get("userName"));
         rs = pst.executeQuery();
         if(rs.next()){
           num = rs.getInt(1);
         }
         
         return num>0;
     } catch (SQLException e) {
         e.printStackTrace();
         return false;
     }finally {
         JdbcUtil.close(rs, pst, conn);
     }
   }
}
