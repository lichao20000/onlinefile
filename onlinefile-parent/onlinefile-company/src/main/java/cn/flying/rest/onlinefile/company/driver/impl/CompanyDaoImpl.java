package cn.flying.rest.onlinefile.company.driver.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.flying.rest.onlinefile.company.driver.CompanyDao;
import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;
import cn.flying.rest.onlinefile.utils.JdbcUtil;
import cn.flying.rest.onlinefile.utils.MongoManager;
import cn.flying.rest.onlinefile.utils.StringUtil;

@Repository("companyDao")
public class CompanyDaoImpl extends BaseDaoHibernate implements CompanyDao {

	public CompanyDaoImpl() {
		super(CompanyDaoImpl.class);
	}
	
	@Autowired
    private MongoManager mongo;

	@SuppressWarnings("deprecation")
	@Override
	public Integer saveCompany(Map<String, String> params) {
		int id = -1;
		Session session = null ;
		Connection conn = null ;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "insert into company(NAME,ADDRESSES,PHONE,FAX,POSTCODE,URL,SERVER_ID,appDate) values(?,?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1, params.get("NAME"));
			pst.setString(2, params.get("ADDRESSES"));
			pst.setString(3, params.get("PHONE"));
			pst.setString(4, params.get("FAX"));
			pst.setString(5, params.get("POSTCODE"));
			pst.setString(6, params.get("URL"));
			pst.setString(7, params.get("SERVER_ID"));
			pst.setString(8, params.get("appDate"));
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if(rs.next()){
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
	@Override
	public void createTables(int companyId) {
		Session session = null ;
		Connection conn = null ;
		PreparedStatement pst = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			//xyc 20151211 添加字段3个字段，
			//UPDATETIME,更新时间是为了生成序列编号的排序，由于删除回来的文件需要排序到文件后面
			//SERIALNUMBER,序号编号格式(因为序号会变，所以要确定更改名字的文件必须有一个唯一编号)：0001、
			//SOLENUMBER,唯一编号格式：上传的同一个文件的唯一ID
			String sql = "CREATE TABLE `files_"+companyId+"` (`ID` INT (8) NOT NULL AUTO_INCREMENT,`CLASSID` INT (8) NOT NULL,`FILENAME` VARCHAR (2000)  NOT NULL,`CREATOR` INT (8)  NOT NULL,`OWNER` INT (8)  NOT NULL,`CREATETIME` VARCHAR (30),`UPDATETIME` VARCHAR (30),SERIALNUMBER VARCHAR (30),SOLENUMBER int (200),`SIZE` BIGINT (30) DEFAULT 0,`MD5` VARCHAR (32) ,`TYPE` VARCHAR (20) DEFAULT '',`PRAISECOUNT` INT (8) DEFAULT '0' NOT NULL,`COLLECTCOUNT` INT (8) DEFAULT '0' NOT NULL,`ISDELETE` enum('1','0') NOT NULL DEFAULT '0',`FILEID` VARCHAR (50) ,`IDSEQ` VARCHAR (250)  NOT NULL,`ISFILE` enum('1','0') NOT NULL DEFAULT '1',`VERSION` INT (11) DEFAULT '1' NOT NULL,`ISLAST` enum('1','0') NOT NULL DEFAULT '1', `OPENLEVEL`  enum('3','2','1','0') NULL DEFAULT '0',PRIMARY KEY (`ID`),INDEX `CLASSID` (`CLASSID`, `ISDELETE`, `ISLAST`) ,INDEX `ISFILE` (`ISFILE`),KEY `CREATOR` (`CREATOR`)) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8";
			pst = conn.prepareStatement(sql) ;
			pst.execute() ;
			JdbcUtil.close(pst);
			sql = "CREATE TABLE `files_trash_"+companyId+"` (`ID` INT (8) NOT NULL AUTO_INCREMENT,`FROMID` INT (8) NOT NULL,`FILENAME` VARCHAR (2000)  NOT NULL,`TYPE` VARCHAR (20) DEFAULT '',`CREATOR` INT (8) NOT NULL ,`OWNER` INT (8) NOT NULL,`DELETEUSERID` INT (8),`DELETETIME` VARCHAR (30),`DESTROYUSERID` INT (8),`DESTROYTIME` VARCHAR (30),`ISDELETE` enum('1','0') NOT NULL DEFAULT '0',`IDSEQ` VARCHAR (250)  NOT NULL,`PATHNAME` VARCHAR (1024),`ISFILE` enum('1','0') NOT NULL DEFAULT '1', `OPENLEVEL`  enum('3','2','1','0') NULL DEFAULT '0',PRIMARY KEY (`ID`) ,INDEX `ISFILE` (`ISFILE`),KEY `CREATOR` (`CREATOR`)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8";
            pst = conn.prepareStatement(sql) ;
            pst.execute() ;
            JdbcUtil.close(pst);
            /** 添加文件评论表  **/
			sql = "CREATE TABLE `filecomment_"+companyId+"` (`ID` int(8) NOT NULL AUTO_INCREMENT,`FILEID` int(8) DEFAULT NULL,`PARENTID` int(8) DEFAULT NULL,`VERSION` int(3) DEFAULT NULL,`MORE` int(1) DEFAULT NULL,`CONTENT` varchar(2000) DEFAULT NULL,`USERID` int(8) DEFAULT NULL,`USERNAME` varchar(100) DEFAULT NULL,`FULLNAME` varchar(100) DEFAULT NULL,`PORTRAIT` varchar(100) DEFAULT NULL,`CREATEDATE` varchar(200) DEFAULT NULL,PRIMARY KEY (`ID`)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8";
            pst = conn.prepareStatement(sql) ;
            pst.execute() ;
            JdbcUtil.close(pst);
			sql = "CREATE TABLE `filepraise_"+companyId+"` (`ID` int(8) NOT NULL AUTO_INCREMENT,`FILEID` int(8) NOT NULL,`USERID` int(8) NOT NULL,PRIMARY KEY (`ID`),KEY `FILEID` (`FILEID`),KEY `USERID` (`USERID`)) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8";
			pst = conn.prepareStatement(sql) ;
			pst.execute() ;
			mongo.getDB("company_"+companyId);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
	}
	
	
	/**获取公司详细信息**/
	@Override
	public Map<String, Map<String, String>> getAllCompanyInfo() {
		Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>();
	    Session session = null;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    try {
	        session = getSession();
	        conn = session.connection();
	        String sql = "select c.ID, c.NAME,c.ADDRESSES, c.PHONE,c.FAX,c.POSTCODE,c.SERVER_ID,c.URL,c.IMG_URL from company c";
	        pst = conn.prepareCall(sql);
	        rs = pst.executeQuery();
	        HashMap<String, String> map = null ;
	        while (rs.next()) {
	        	map = new HashMap<String, String>();
	            map.put("ID", rs.getString("ID"));
	            map.put("NAME", rs.getString("NAME"));
	            map.put("ADDRESSES", StringUtil.string(rs.getString("ADDRESSES")));
	            map.put("PHONE", StringUtil.string(rs.getString("PHONE"))); 
	            map.put("FAX", StringUtil.string(rs.getString("FAX")));
	            map.put("POSTCODE", StringUtil.string(rs.getString("POSTCODE")));
	            map.put("SERVER_ID", StringUtil.string(rs.getString("SERVER_ID")));
	            map.put("URL", StringUtil.string(rs.getString("URL")));
	            map.put("IMGURL", StringUtil.string(rs.getString("IMG_URL")));
	            maps.put(rs.getString("ID"), map) ;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
	    return maps;
	}

	@Override
	public HashMap<String, Object> getCampanyInfoById(Integer id) {
		
		HashMap<String,Object> reutrnMap = new HashMap<String, Object>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "select c.ID, c.NAME,c.ADDRESSES, c.PHONE,c.FAX,c.POSTCODE,c.SERVER_ID,c.URL,c.IMG_URL from company c where c.ID=?";
		try {
			session = this.getSession(false);
			conn = session.connection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if(rs.next()){
				reutrnMap.put("ID", rs.getInt("ID"));
				reutrnMap.put("NAME", rs.getString("NAME"));
				reutrnMap.put("ADDRESSES", rs.getString("ADDRESSES")); 
				reutrnMap.put("PHONE", rs.getString("PHONE"));
				reutrnMap.put("FAX", rs.getString("FAX"));
				reutrnMap.put("POSTCODE", rs.getString("POSTCODE"));
				reutrnMap.put("SERVER_ID", rs.getInt("SERVER_ID"));
				reutrnMap.put("URL", rs.getString("URL"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
	        JdbcUtil.close(rs, pst, conn);
	    }
		
		return reutrnMap;
	}

	@Override
	public boolean editCampanyInfo(Map<String, String> params) {
		Session session = null ;
		Connection conn = null ;
		PreparedStatement st = null;
		boolean flag = false;
		StringBuilder sqlbuilder = new StringBuilder(100);
		List<String> values = new ArrayList<String>() ;
		sqlbuilder.append("update company set");
		if(params.get("companyname")!=null){
			sqlbuilder.append(" NAME=?,");
			values.add(params.get("companyname")) ;
		}
		if(params.get("addresses")!=null){
			sqlbuilder.append(" ADDRESSES=?,");
			values.add(params.get("addresses")) ;
		}
		if(params.get("companyphone")!=null){
			sqlbuilder.append(" PHONE=?,");
			values.add(params.get("companyphone")) ;
		}
		if(params.get("companyfax")!=null){
			sqlbuilder.append(" FAX=?,");
			values.add(params.get("companyfax")) ;
		} 
		if(params.get("postcode")!=null){
			sqlbuilder.append(" POSTCODE=?");
			values.add(params.get("postcode")) ;
		}else {
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
				st.setInt(i, Integer.parseInt(params.get("companyid"))) ;
				flag = st.executeUpdate()>0 ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	        JdbcUtil.close( st,conn);
	    }
		return flag;
	}
	
	/*@Override
	public Map<String, String> getImagePath(Map<String, String> params) {
		Map<String, String> rtnMap = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null ;
		int temp = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "SELECT IMG_URL FROM company WHERE ID=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("companyid"));
			rs = pst.executeQuery();
			if(rs.next()){
				rtnMap.put("path", rs.getString("IMG_URL"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return rtnMap ;
	}*/

	@Override
	public boolean saveHeadImage(Map<String, String> params) {
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		int temp = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "UPDATE company SET IMG_URL=? WHERE ID=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("path"));
			pst.setString(2, params.get("companyid"));
			temp = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return temp ==-1?false:true ;
	}
	

	@SuppressWarnings("deprecation")
	@Override
	public void saveCompanyReferrer(int companyId, String referrer) {
		Session session = null ;
		Connection conn = null ;
		PreparedStatement pst = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "insert into companyReferrer(companyId, referrer) values(?,?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, companyId);
			pst.setString(2, referrer);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
	}
	
	@Override
	public Boolean validateUrl(String start) {
		boolean isOK = false ;
		Session session = null ;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null ;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String orgRegistersql = "SELECT id FROM company WHERE appDate=?";
			pst = conn.prepareStatement(orgRegistersql);
			pst.setString(1, start);
			rs = pst.executeQuery() ;
			if(rs.next()){
				isOK = true ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return isOK;
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return isOK;
	}
	public boolean insertIntoFilesN(String filesN){
		 Session session =this.getSession(false);
		 @SuppressWarnings("deprecation")
		 Connection conn = session.connection();
		 PreparedStatement st = null;
		try {
			String sql ="INSERT INTO "+filesN+"(ID,CLASSID,FILENAME,CREATOR,OWNER,CREATETIME,SIZE,MD5,TYPE,PRAISECOUNT,COLLECTCOUNT,ISDELETE,FILEID,IDSEQ,ISFILE,VERSION,ISLAST,OPENLEVEL) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			st = conn.prepareStatement(sql);
			st.setInt(1, 1);
			st.setInt(2, 0);
			st.setString(3, "root");
			st.setInt(4, 1);
			st.setInt(5, 1);
			st.setString(6, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			st.setInt(7, 0);
			st.setString(8, "");
			st.setString(9,"");
			st.setInt(10, 0);
			st.setInt(11, 0);
			st.setString(12, "0");
			st.setString(13, "");
			st.setString(14,"");
			st.setString(15,"0");
			st.setInt(16,1);
			st.setString(17,"1");
			st.setString(18,"0");
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			JdbcUtil.close(null, st, conn);
		}
		return true;
	}

	@Override
	public List<Map<String,String>> getCompanyReferrer(Map<String,String> params) {
		String referrer = params.get("referrer");
		int start = Integer.parseInt(params.get("start"));
		int limit = Integer.parseInt(params.get("limit"));
		List<Map<String,String>> lst = new ArrayList<Map<String,String>>();
	    Session session = null;
	    Connection conn = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    session = getSession();
	    conn = session.connection();
	    HashMap<String, String> map = null;
	    String sql = "select co.id, co.NAME ,co.IMG_URL from companyreferrer c,company co where c.companyId = co.ID and c.referrer = ? order by co.id limit ?,?";
	    try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, referrer);
			pst.setInt(2, start);
			pst.setInt(3, limit);
			rs = pst.executeQuery();
			while(rs.next()){
				map = new HashMap<String, String>();
				map.put("companyName", rs.getString("name"));
				map.put("imgurl", rs.getString("IMG_URL"));
				map.put("id",rs.getString("id"));
				lst.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JdbcUtil.close(rs, pst, conn);
		}
		return lst;
	}
	@Override
	public int getCompanyReferrerCount(String refferrer) {
		 	
			Session session = null;
		    Connection conn = null;
		    PreparedStatement pst = null;
		    ResultSet rs = null;
		    session = getSession();
		    conn = session.connection();
		    int  totel = -1;
		    String sql = "SELECT COUNT(1) FROM companyreferrer c INNER JOIN company co on c.companyId = co.ID and c.referrer = ? ";
		    try {
		    	pst = conn.prepareStatement(sql);
		    	pst.setString(1, refferrer);
		    	rs = pst.executeQuery();
		    	if(rs.next()){
		    		 totel = rs.getInt(1);
		    	}
		    } catch (SQLException e) {
				e.printStackTrace();
			}finally{
				JdbcUtil.close(rs, pst, conn);
			}
		
		return totel;
	}

	@Override
	public boolean isCompayNameExist(String compayName) {
		boolean isOK = false ;
		Session session = null ;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null ;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String orgRegistersql = "SELECT id FROM company WHERE NAME=?";
			pst = conn.prepareStatement(orgRegistersql);
			pst.setString(1, compayName);
			rs = pst.executeQuery() ;
			if(rs.next()){
				isOK = true ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return isOK;
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return isOK;
	}
	
	@Override
	public  Map<String, String> isCompayUserExist(String compayId,String userId){
		Map<String, String> reMap=new HashMap<String, String>();
		Session session = null ;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null ;
		try {
			session = this.getSession(false);
			conn = session.connection();
			//String sql = "SELECT USERID,STATUS,ISDEL FROM company_users WHERE COMPANYID=? and USERID=? and CLASSID=0";
			String sql = "SELECT USERID,STATUS,ISDEL FROM company_users WHERE COMPANYID=? and USERID=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, compayId);
			pst.setString(2, userId);
			rs = pst.executeQuery() ;
			if(rs.next()){
				reMap.put("USERID", rs.getString("USERID"));
				reMap.put("STATUS", rs.getString("STATUS"));
				reMap.put("ISDEL", rs.getString("ISDEL"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return reMap;
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return reMap;
	}
	public boolean cancelCompany(Map<String, String> param){
		Session session = null ;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null ;
		int i = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "update company_users set isdel = 1 where userid=? and companyid=? and isadmin=1";
			pst = conn.prepareStatement(sql);
			pst.setString(1, param.get("userId"));
			pst.setString(2, param.get("companyId"));
			i = pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return i>0;
		
	}

	public List<Map<String,String>> getCompayByCompayName(String compayName){
		List<Map<String,String>> listMaps= new ArrayList<Map<String,String>>();
		Map<String, String> reMap=new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null ;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String orgRegistersql = "SELECT id,name FROM company WHERE NAME=?";
			pst = conn.prepareStatement(orgRegistersql);
			pst.setString(1, compayName);
			rs = pst.executeQuery() ;
			if(rs.next()){
				reMap.put("companyId", rs.getString("id"));
				reMap.put("companyName", rs.getString("name"));
				listMaps.add(reMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return listMaps;
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return listMaps;
	}
	 
	 public boolean transferCompany(Map<String, String> param){
	    String companyId = param.get("companyId");
	    String userid = param.get("userId");
	    String touserId = param.get("touserId");
	    
	    Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            session = this.getSession(false);
            conn = session.connection();
            conn.setAutoCommit(false);
            String sql = "update company_users set ISADMIN=? where USERID = ? and COMPANYID = ? and ISADMIN=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, 1);
            pst.setInt(2,Integer.parseInt(touserId));
            pst.setInt(3, Integer.parseInt(companyId));
            pst.setInt(4, 0);
            int num = pst.executeUpdate();
            pst.close();
            String sql1 = "update company_users set ISADMIN=? where USERID = ? and COMPANYID = ? and ISADMIN=?";
            pst = conn.prepareStatement(sql1);
            pst.setInt(1, 0);
            pst.setInt(2,Integer.parseInt(userid));
            pst.setInt(3, Integer.parseInt(companyId));
            pst.setInt(4, 1);
            int num1 =  pst.executeUpdate();
            boolean flag = false;
            if(num+num1>1){
               flag = true;
               conn.commit();
            }else{
              throw new SQLException("更新不同步");
            }
            return flag;
        } catch (SQLException e) {
          try {
            conn.rollback();
          } catch (SQLException e1) {
            e1.printStackTrace();
          }
            e.printStackTrace();
          return false;
        } finally {
            JdbcUtil.close(null, pst, conn) ;
        }
	 }
	
}
