package cn.flying.rest.onlinefile.documentclass.driver.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import cn.flying.rest.onlinefile.documentclass.driver.DocumentClassDao;
import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;
import cn.flying.rest.onlinefile.utils.JdbcUtil;

@Repository("documentClassDao")
public class DocumentClassDaoImpl extends BaseDaoHibernate implements
		DocumentClassDao {

	public DocumentClassDaoImpl() {
		super(DocumentClassDaoImpl.class);
	}

	public List<Map<String,String>> getCateList(Map<String,String> params){
		String companyId = params.get("companyId");
		String startNo = params.get("startNo");
		String limit = params.get("limit");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Session session =this.getSession(false);
		Connection conn = session.connection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			StringBuffer totalSql = new StringBuffer();
			totalSql.append("SELECT COUNT(*) FROM documentclass");
			pst = conn.prepareStatement(totalSql.toString());
			rs = pst.executeQuery();
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM documentclass WHERE COMPANYID = ? ORDER BY ORDER_COL ");
			if(null != startNo && null != limit && !"".equals(startNo) && !"".equals(limit) ){
				sql.append(" LIMIT ").append(startNo).append(",").append(limit);
			}
			pst = conn.prepareStatement(sql.toString());
			pst.setString(1, companyId);
			
			rs = pst.executeQuery();
			Map<String,String> tempMap = null;
			while(rs.next()){
				tempMap = new HashMap<String, String>();
				tempMap.put("id", rs.getString("ID"));
				tempMap.put("companyId", rs.getString("COMPANYID"));
				tempMap.put("className", rs.getString("CLASSNAME"));
				list.add(tempMap);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return list;
	}
	
	public Integer getCateCount(){
		int count = 0;
		Session session =this.getSession(false);
		Connection conn = session.connection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			StringBuffer totalSql = new StringBuffer();
			totalSql.append("SELECT COUNT(*) FROM documentclass");
			pst = conn.prepareStatement(totalSql.toString());
			rs = pst.executeQuery();
			if(rs.next()){
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return count;
	}

	public Map<String, String> addCate(Map<String, String> params) {
		String companyId = params.get("companyId");
		String className = params.get("className");
		int order = 0;
		boolean flag = false;
		Map<String,String> tempMap = new HashMap<String, String>();
		Session session =this.getSession(false);
		Connection conn = session.connection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			StringBuffer orderSql = new StringBuffer();
			orderSql.append("SELECT MAX(order_col)+1 FROM documentclass WHERE COMPANYID = ?");
			pst = conn.prepareStatement(orderSql.toString());
			pst.setString(1, companyId);
			rs = pst.executeQuery();
			if(rs.next()){
				order = rs.getInt(1);
			}
			JdbcUtil.close(rs, pst);
			
			StringBuffer insertSql = new StringBuffer();
			insertSql.append(" INSERT INTO documentclass(companyid,classname,order_col) VALUES (?, ?, ?) ");
			pst = conn.prepareStatement(insertSql.toString());
			pst.setString(1, companyId);
			pst.setString(2, className);
			pst.setString(3, order+"");
			pst.execute();
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		tempMap.put("success", Boolean.toString(flag));
		return tempMap;
	}

	public Map<String, String> delCate(Map<String, String> params) {
		Map<String,String> tempMap = new HashMap<String, String>();

		String id = params.get("id");
		boolean flag = false;
		Session session =this.getSession(false);
		Connection conn = session.connection();
		PreparedStatement pst = null;
		try {
			StringBuffer updateSql = new StringBuffer();
			updateSql.append("DELETE FROM documentclass WHERE ID = ?");
			pst = conn.prepareStatement(updateSql.toString());
			pst.setString(1, id);
			pst.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
		tempMap.put("success", Boolean.toString(flag));
		return tempMap;
	}

	public Map<String, String> editCate(Map<String, String> params) {
		String id = params.get("id");
		String className = params.get("className");
		Map<String,String> tempMap = new HashMap<String, String>();
		boolean flag = false;
		
		Session session =this.getSession(false);
		Connection conn = session.connection();
		PreparedStatement pst = null;
		try {
			StringBuffer updateSql = new StringBuffer();
			updateSql.append("UPDATE documentclass SET CLASSNAME = ? where ID = ?");
			pst = conn.prepareStatement(updateSql.toString());
			pst.setString(1, className);
			pst.setString(2, id);
			pst.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
		tempMap.put("success", Boolean.toString(flag));
		return tempMap;
	}

	public Map<String, String> getCateById(Map<String, String> params) {
		Map<String,String> tempMap = new HashMap<String, String>();
		String tableName = "files_"+params.get("companyid");
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session =this.getSession(false);
			conn = session.connection();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT FILENAME,CLASSID FROM "+tableName+" WHERE ID = ? ");
			pst = conn.prepareStatement(sql.toString());
			pst.setString(1, params.get("classId"));
			rs = pst.executeQuery();
			if(rs.next()){
				tempMap.put("CLASSNAME", rs.getString(1));
				tempMap.put("CLASSID", rs.getString(2));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return tempMap;
	}
	
//	public Map<String, String> addClassByName(Map<String, String> params){
//		String companyId = params.get("companyId");
//		String className = params.get("className");
//		String classId = params.get("classId");
//		String userId = params.get("userId");
//		String tableName = "files_"+companyId;
//		int order = 0;
//		boolean flag = false;
//		Map<String,String> tempMap = new HashMap<String, String>();
//		Session session = null;
//		Connection conn = null;
//		PreparedStatement pst = null;
//		ResultSet rs = null;
//		String temp = null;
//		int index = -1;
//		 SimpleDateFormat format = null ;
//		  String createtime = null;
//		try {
//			format = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
//			createtime = format.format(System.currentTimeMillis());
//			session = this.getSession(false);
//			conn = session.connection();
//			String sql = null;
//			if(params.get("classId").equals("1")){
//				//说明是父节点  直接插入
//				sql = "insert into "+tableName+" (classid,filename,isdelete,idseq,isfile,creator,CREATETIME) values(?,?,?,?,?,?,?)";
//				pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
//				pst.setString(1, classId);
//				pst.setString(2, className);
//				pst.setString(3, "0");
//				pst.setString(4, "1.");
//				pst.setString(5, "0");
//				pst.setInt(6, Integer.parseInt(userId));
//				pst.setString(7, createtime);
//				index = pst.executeUpdate();
//				rs = pst.getGeneratedKeys();
//				if(rs.next()){
//					index = rs.getInt(1);
//				}
//			}else{
//				sql = "select idseq from "+tableName+" where id=?";
//				pst = conn.prepareStatement(sql);
//				pst.setString(1, classId);
//				rs = pst.executeQuery();
//				
//				if(rs.next()){
//					temp = rs.getString(1);
//				}
//				String sqlt = "insert into "+tableName+" (classid,filename,isdelete,idseq,isfile,creator,CREATETIME) values(?,?,?,?,?,?,?)";
//				pst = conn.prepareStatement(sqlt,Statement.RETURN_GENERATED_KEYS);
//				pst.setString(1, classId);
//				pst.setString(2, className);
//				pst.setString(3, "0");
//				pst.setString(4, temp+classId+".");
//				pst.setString(5, "0");
//				pst.setInt(6, Integer.parseInt(userId));
//				pst.setString(7, createtime);
//				index = pst.executeUpdate();
//				rs = pst.getGeneratedKeys();
//				if(rs.next()){
//					index = rs.getInt(1);
//				}
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			JdbcUtil.close(rs, pst, conn);
//		}
//		if(index !=-1){
//			flag = true;
//		}
//		tempMap.put("success", Boolean.toString(flag));
//		tempMap.put("id", index+"");
//		return tempMap;
//	}
	public Map<String, String> addClassByName(Map<String, String> params){
		String companyId = params.get("companyId");
		String className = params.get("className");
		String classId = params.get("classId");
		String userId = params.get("userId");
		String tableName = "files_"+companyId;
		int order = 0;
		boolean flag = false;
		Map<String,String> tempMap = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String temp = null;
		int index = -1;
		 SimpleDateFormat format = null ;
		  String createtime = null;
		try {
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			createtime = format.format(System.currentTimeMillis());
			session = this.getSession(false);
			conn = session.connection();
			String sql = null;
			if(params.get("classId").equals("1")){
				//说明是父节点  直接插入
				sql = "insert into "+tableName+" (classid,filename,isdelete,idseq,isfile,creator,owner,CREATETIME) values(?,?,?,?,?,?,?,?)";
				pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, classId);
				pst.setString(2, className);
				pst.setString(3, "0");
				pst.setString(4, "1.");
				pst.setString(5, "0");
				pst.setInt(6, Integer.parseInt(userId));
				pst.setInt(7, Integer.parseInt(userId));
				pst.setString(8, createtime);
				index = pst.executeUpdate();
				rs = pst.getGeneratedKeys();
				if(rs.next()){
					index = rs.getInt(1);
				}
			}else{
				sql = "select idseq from "+tableName+" where id=?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, classId);
				rs = pst.executeQuery();
				
				if(rs.next()){
					temp = rs.getString(1);
				}
				JdbcUtil.close(rs, pst);
				int nextSerialNumber=this.getNextSerialNumber(tableName,classId);
				String nextSolenumber=this.getFileAndFolderMaxSolenumber(tableName);
				conn = session.connection();
				String sqlt = "insert into "+tableName+" (classid,filename,isdelete,idseq,isfile,creator,owner,CREATETIME,type,UPDATETIME,SERIALNUMBER,SOLENUMBER) values(?,?,?,?,?,?,?,?,?,?,"+"LPAD("+nextSerialNumber+",4,0)"+",?)";
				pst = conn.prepareStatement(sqlt,Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, classId);
				pst.setString(2, className);
				pst.setString(3, "0");
				pst.setString(4, temp+classId+".");
				pst.setString(5, "0");
				pst.setInt(6, Integer.parseInt(userId));
				pst.setInt(7, Integer.parseInt(userId));
				pst.setString(8, createtime);
				pst.setString(9, "folder");
				pst.setString(10, createtime);
				pst.setString(11, nextSolenumber);
//				pst.setString(11, "LPAD("+nextSerialNumber+",4,0)");
				index = pst.executeUpdate();
				rs = pst.getGeneratedKeys();
				if(rs.next()){
					index = rs.getInt(1);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		if(index !=-1){
			flag = true;
		}
		tempMap.put("success", Boolean.toString(flag));
		tempMap.put("id", index+"");
		return tempMap;
	}
  /**
	 * xyc
     * 获取文件夹的下一个序列号
     * @param tableName
     * @param classId
     * @return SERIALNUMBER
     */
	public int getNextSerialNumber(String tableName, String classId) {
        String serialNumber = "0";
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            String serialnumberSql = "SELECT IFNULL(MAX(SERIALNUMBER),0) FROM "+tableName+" WHERE classId =? AND TYPE='folder' AND ISDELETE ='0'";
            pst = conn.prepareStatement(serialnumberSql);
            pst.setString(1, classId);
            rs = pst.executeQuery();
            if (rs.next()) {
            	serialNumber = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return Integer.valueOf((serialNumber!=null?serialNumber:"0"))+1;
    }
	
	/**
	 * xyc
	 * 文件、文件夹都给唯一编号
	 * */
	public String getFileAndFolderMaxSolenumber(String tableName) {
		int maxSole=0;
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			String	solenNumberSql = "SELECT IFNULL(MAX(SOLENUMBER),0) FROM "+tableName;
				pst = conn.prepareStatement(solenNumberSql);
				rs = pst.executeQuery();
				if (rs.next()) {
					maxSole = rs.getInt(1)+1;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return maxSole+"";
	}

	public Map<String, String> deleteClassById(String companyId, String classId){
		String tableName = "files_"+companyId;
		Map<String,String> tempMap = new HashMap<String, String>();
		boolean flag = false;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		int rtn = 0;
		try {
			session = this.getSession(false);
			conn = session.connection();
			StringBuffer updateSql = new StringBuffer();
//			updateSql.append("DELETE FROM "+tableName+" WHERE ID = ?");
			updateSql.append("update "+tableName+" set ISDELETE = '1' WHERE ID = ?");
			pst = conn.prepareStatement(updateSql.toString());
			pst.setString(1, classId);
			rtn = pst.executeUpdate();
			flag = rtn==0?false:true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
		tempMap.put("success", Boolean.toString(flag));
		return tempMap;
	}
	
	public String getCreatorByClassId(Map<String, String> params){
		String classId = params.get("classId");
		String companyId = params.get("companyId");
		String tableName = "files_"+companyId;
		int creator = -1;
		Session session =null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pst = null;
		int rtn = 0;
		try {
			session = this.getSession(false);
			conn = session.connection();
			StringBuffer updateSql = new StringBuffer();
			updateSql.append("select creator FROM "+tableName+" WHERE ID = ?");
			pst = conn.prepareStatement(updateSql.toString());
			pst.setString(1, classId);
			rs = pst.executeQuery();
			if(rs.next()){
				creator = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
		return creator == -1? null:creator+"";
	}
	
	public Map<String, String> reClassNameById(Map<String, String> params){
		String id = params.get("classId");
		String className = params.get("className");
		String companyId = params.get("companyId");
		String tableName = "files_"+companyId;
		Map<String,String> tempMap = new HashMap<String, String>();
		boolean flag = false;
		int rtn = 0;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			StringBuffer updateSql = new StringBuffer();
			updateSql.append("UPDATE "+tableName+" SET filename = ? where ID = ?");
			pst = conn.prepareStatement(updateSql.toString());
			pst.setString(1, className);
			pst.setString(2, id);
			rtn = pst.executeUpdate();
			flag = rtn==0?false:true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
		tempMap.put("success", Boolean.toString(flag));
		return tempMap;
	}
	
	public List<HashMap<String, Object>> gerClassList(Map<String, String> params){
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		//暂时没加条件
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "SELECT ID,filename  from documentclass where COMPANYID=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, params.get("companyId"));
			rs = pst.executeQuery();
			HashMap<String,Object> tempMap = null;
			while(rs.next()){
				tempMap = new HashMap<String, Object>();
				tempMap.put("id", rs.getString("ID"));
				//tempMap.put("companyId", rs.getString("COMPANYID"));
				tempMap.put("className", rs.getString("CLASSNAME"));
				list.add(tempMap);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return list;
	}
	
	public Map<String, String> editClassId(Map<String, String> params){
		String flag = params.get("flag");
		String id = params.get("id");
		Map<String,String> tempMap = new HashMap<String, String>();
		boolean flas = false;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		int rtn = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "update groups set CLASSID=? where FLAG=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			pst.setString(2, flag);
			rtn = pst.executeUpdate();
			flas = rtn == -1?false:true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pst, conn);
		}
		tempMap.put("success", Boolean.toString(flas));
		return tempMap;
	}
	public boolean hasFilesByClassId(String classId,String companyId){
		String tableName = "files_"+companyId;
		boolean flas = false;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "select COUNT(ID) from "+tableName+" where CLASSID=? AND isdelete='0' AND isLast=1"; //wangwenshuo add AND isLast=1"
			pst = conn.prepareStatement(sql);
			pst.setString(1, classId);
			rs = pst.executeQuery();
			if (rs.next()){
				flas =rs.getInt(1)>0?true:false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs,pst, conn);
		}
		return flas;
	}
	public boolean hasUsersByClassId(String classId,String companyId,String userId){
		boolean flas = false;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = "select COUNT(r.USERID) FROM groups g LEFT JOIN groupusersrelation r ON g.id = r.groupid WHERE g.classid=? AND g.companyid=? AND r.USERID !=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, classId);
			pst.setString(2, companyId);
			pst.setString(3, userId);
			rs = pst.executeQuery();
			if (rs.next()){
				flas =rs.getInt(1)>0?true:false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs,pst, conn);
		}
		return flas;
	}
	
	public boolean createGroup(String companyId, String username,
			String groupuserids, String manageruserid, String groupname,
			String groupremark, String groupflag, String time){
		boolean isOK = false ;
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		ResultSet rs = null ;
		try {
			session = getOpenSession() ;
			conn = session.connection() ;
			conn.setAutoCommit(false);
			/** 1、插入群组基本信息 **/
			String sql = "INSERT INTO GROUPS(COMPANYID, GROUPNAME, REMARK, CREATETIME, FLAG) VALUES(?,?,?,?,?)" ;
			pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pst.setInt(1, Integer.parseInt(companyId));
			pst.setString(2, groupname);
			pst.setString(3, groupremark);
			pst.setString(4, time);
			pst.setString(5, groupflag);
			isOK = pst.executeUpdate()>0 ;
			if(isOK){
				rs = pst.getGeneratedKeys();
				int groupId = 0 ;
				if(rs.next()){
					groupId = rs.getInt(1) ;
				}
				if(groupId > 0){
					JdbcUtil.close(rs, pst) ;
					/** 2、插入群组与用户关联信息 **/
					sql = "INSERT INTO groupusersrelation(GROUPID, USERID, JOINTIME, ISADMIN) VALUES(?,?,?,?)" ;
					pst = conn.prepareStatement(sql) ;
					String[] userids = groupuserids.split(",") ;
					for(String userid:userids){
						pst.setInt(1, groupId) ;
						pst.setInt(2, Integer.parseInt(userid)) ;
						pst.setString(3, time) ;
						pst.setInt(4, manageruserid.equals(userid)?1:0) ;
						pst.addBatch();
					}
					isOK = pst.executeBatch().length==userids.length;
				}
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
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
			JdbcUtil.close(rs, pst, conn,session) ;
		}
		return isOK ;
	}
	public boolean getClassByClassNameAndCompanyId(Map<String, String> params){
		String companyId = params.get("companyId");
		String className = params.get("className");
		String classId = params.get("classId");
		String fatherClassId = params.get("fatherClassId");
		String tableName = "files_"+companyId;
		ResultSet rs = null ;
		boolean flag = false;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			StringBuffer updateSql = new StringBuffer();
			updateSql.append("select id  from "+tableName+"  where filename =? and id !=?  and classid=? and isDelete!='1' ");
			pst = conn.prepareStatement(updateSql.toString());
			pst.setString(1, className);
			pst.setString(2, classId);
//			pst.setString(3, fatherClassId);
			pst.setInt(3, Integer.parseInt(fatherClassId));
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
	
	public boolean getCheckClassNameIsIn(Map<String, String> params){
		String companyId = params.get("companyId");
		String className = params.get("className");
		String classId = params.get("classId");
		String fatherClassId = params.get("fatherClassId");
		String tableName = "files_"+companyId;
		ResultSet rs = null ;
		boolean flag = false;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			StringBuffer updateSql = new StringBuffer();
			updateSql.append("select id  from "+tableName+"  where filename =? and classid=? and isdelete='0'");
			pst = conn.prepareStatement(updateSql.toString());
			pst.setString(1, className);
			pst.setInt(2, Integer.parseInt(classId));
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
	
	public Map<String, String> getClassInfo(Map<String, String> params){
		Map<String, String> rtMap = new HashMap<String, String>();
		String companyId = params.get("companyId");
		//String groupName = params.get("groupName");
		String groupid = params.get("groupid");
		String flag = params.get("flag");
		String userId = params.get("userId");
		String tableName = "files_"+companyId;
		ResultSet rs = null ;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = ("select g.id,g.groupname,g.remark,g.createtime,f.creator,f.owner  from groups g,"+tableName+" f  where g.classid=f.id and  g.flag = ?");
			pst = conn.prepareStatement(sql);
			pst.setString(1, flag);
			rs = pst.executeQuery();
			if(rs.next()){
				rtMap.put("id", rs.getString(1));
				rtMap.put("groupname", rs.getString(2));
				rtMap.put("groupremark", rs.getString(3));
				rtMap.put("createtime", rs.getString(4));
				rtMap.put("creator", rs.getString(5));
				rtMap.put("owner", rs.getString(6));
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs,pst, conn);
		}
		return rtMap;
	}
	
	public Map<String, String> editClassInfo(Map<String, String> params){
		Map<String, String> rtMap = new HashMap<String, String>();
		String companyId = params.get("companyId");
		String groupName = params.get("groupName");
		String groupid = params.get("groupid");
		String flag = params.get("flag");
		String userId = params.get("userId");
		String mark = params.get("mark");
		int index = -1;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			session = this.getSession(false);
			conn = session.connection();
			String sql = ("update groups set groupname=?,remark=? where flag = ?");
			pst = conn.prepareStatement(sql);
			pst.setString(1, groupName);
			pst.setString(2, mark);
			pst.setString(3, flag);
			index = pst.executeUpdate();
			if(index>0){
				rtMap.put("success", "true");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null,pst, conn);
		}
		return rtMap;
	}
	
	public Map<String, Object> changeGroupAdmin(Map<String, String> params){
		Map<String, Object>  rtnMap = new HashMap<String, Object>();
		String tableName = "files_"+params.get("companyId");
		boolean flag = false;
		Session session =null;
		Connection conn = null;
		PreparedStatement pst = null;
		int index = -1;
		try {
			session = this.getSession(false);
			conn = session.connection();
			StringBuffer updateSql = new StringBuffer();
			updateSql.append("update "+tableName+" set CREATOR=?,OWNER=?  where ID=?");
			pst = conn.prepareStatement(updateSql.toString());
			pst.setInt(1, Integer.parseInt(params.get("touserid")));
			pst.setInt(2, Integer.parseInt(params.get("touserid")));
			pst.setInt(3, Integer.parseInt(params.get("classId")));
			index = pst.executeUpdate();
			if(index>0){
				flag = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null,pst, conn);
		}
		rtnMap.put("isOk", flag);
		return rtnMap;
		
	}
	
	/**
     * 获取分类群组的FLAG
     * @param classId  分类群组id
     * @return
     */
    public Map<String, String> getFlagWithClassId(String companyId, String classId) {
    	Map<String, String> retMap=new HashMap<String, String>();
       // String FLAG = "";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select GROUPNAME,FLAG from groups where classid=? and companyid=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, classId);
            pst.setString(2, companyId);
            rs = pst.executeQuery();
            if (rs.next()) {
            	retMap.put("GROUPNAME", rs.getString("GROUPNAME"));
            	retMap.put("FLAG", rs.getString("FLAG"));
              //  FLAG = rs.getString("FLAG");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
       // return FLAG;
        return retMap;
    }
    
    public String getChildByClassId(String companyId, String folderId){
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        String idsql = "";
        String tableName = "files_"+companyId;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select IDSEQ from "+tableName+" where ID=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, folderId);
            rs = pst.executeQuery();
            if (rs.next()) {
            	idsql = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return idsql;
    }
    public boolean deleteClassByIdSeq(String idsql,String companyId,String classId){
    	Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        int index = -1;
        int temp = -1;
        String tableName = "files_"+companyId;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "update "+tableName+" set ISDELETE=1 where IDSEQ like ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, idsql+"%");
            index = pst.executeUpdate();
            if(index!=-1){
            	 String sqltest = "update "+tableName+" set ISDELETE=1 where ID = ?";
            	 pst = conn.prepareStatement(sqltest);
            	 pst.setInt(1, Integer.parseInt(classId));
            	 temp = pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(null, pst, conn);
        }
        return temp>0?true:false;
    }
    
    public List<String> getIdsByIdSeq(String idseq,String companyId){
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        String tableName = "FILES_"+companyId;
        List<String> ids = new ArrayList<String>();
        try {
            session = getSession();
            conn = session.connection();
            String sql = "SELECT ID FROM "+tableName+" WHERE IDSEQ like ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, idseq+"%");
            rs = pst.executeQuery();
            while(rs.next()){
            	ids.add(rs.getString("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return ids;
    }
    
    public boolean updateDelById(List<String> ids,String companyId,String isDel){
    	Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String tableName = "FILES_"+companyId;
        try {
            session = getOpenSession();
            conn = session.connection();
            conn.setAutoCommit(false);
            String sql = "UPDATE "+tableName+" SET ISDELETE = ? WHERE ID = ?";
            pst = conn.prepareStatement(sql);
            for(String id:ids){
            	pst.setString(1, isDel);
            	pst.setString(2, id);
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
            return false;
        } finally {
        	   try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            JdbcUtil.close(null, pst, conn,session);
        }
        return true;
    }
    public boolean deleteHuiShouZhan(String companyId, String userId,
			String userName, List<String> ids){
    	Session session = null ;
    	Connection conn = null ;
    	PreparedStatement pst = null ;
    	String tableName = "files_trash_"+companyId;
    	try {
    		String destroyTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
    		session = getOpenSession();
    		conn = session.connection();
    		conn.setAutoCommit(false);
    		StringBuffer sql = new StringBuffer(); 
            sql.append("update ").append(tableName).append(" set isdelete='1',destroytime=?,destroyuserid=? WHERE fromid=?");
            pst = conn.prepareStatement(sql.toString());
    		for(String id:ids){
    			pst.setString(1, destroyTime);
    			pst.setString(2, userId);
    			pst.setString(3, id);
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
    		return false;
    	} finally {
    		try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		JdbcUtil.close(null, pst, conn,session);
    	}
    	return true;
    }
}
