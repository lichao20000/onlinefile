package cn.flying.rest.onlinefile.files.driver.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.flying.rest.onlinefile.files.driver.FilesDao;
import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;
import cn.flying.rest.onlinefile.utils.JdbcUtil;
import cn.flying.rest.onlinefile.utils.MongoManager;
import cn.flying.rest.onlinefile.utils.StringUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;

/**
 * 文档数据库操作实现类
 * @author longjunhao
 *
 */
@Repository
public class FilesDaoImpl extends BaseDaoHibernate implements FilesDao {

    @SuppressWarnings("unchecked")
    public FilesDaoImpl() {
        super(FilesDaoImpl.class);
    }
    
    @Autowired
    private MongoManager mongo;
    
    /**
     * 获取分类列表
     * @param companyId
     * @return
     */
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
    
    /**
     * 获取分类列表
     * @param companyId
     * @param ids
     * @return
     */
    public List<Map<String, String>> getClassList(String companyId, List<String> idList) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (idList.isEmpty()) {
            return list;
        }
        String ids = StringUtil.list2String(idList, ",");
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
                .append(" where CLASSID=1 and ISDELETE='0' and ISFILE='0' and id in (").append(ids).append(") ")
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
    
    
    /**
     * 获取文件id的列表
     * @param companyId
     * @param classId
     * @param userId
     * @param start
     * @param limit
     * @return
     */
    public List<String> getFileIdList(String companyId, String classId, String userId, int start, int limit,String orderField,String orderType) {
    	/** orderField
    	 * 几种排序
    	 * 1.默认排序
    	 * 2.文件名排序
    	 * 3.大小排序
    	 * 4.上传人排序   wangwenshuo modify 2015076 上传人改为  拥有人
    	 * 5.上传日期排序（上传日期其实也就是默认排序）
    	 * 6.按类型排序 liuwei  20160106
    	 */
    	/**
    	 * orderType
    	 * 排序类型
    	 * 升序 降序
    	 * 默认降序
    	 */
    	if(orderType==null || orderType.trim().length()==0){
    		orderType = " asc ";//desc
    	}
    	if(orderField==null || orderField.trim().length()==0){
    		orderField = "1";
    	}
        boolean isFilterUser = false;
        if (null != userId && !"".equals(userId) && !"0".equals(userId)) {
            isFilterUser = true;
        }
        List<String> list = new ArrayList<String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_" + companyId;
            StringBuilder sql = new StringBuilder();
            if("4".equals(orderField)){
            	sql.append("select f.ID from ").append(tableName).append(" f,users u WHERE f.owner = u.id and f.CLASSID = ? AND f.isDelete = '0' AND f.isLast = '1' ");
            	if (isFilterUser) {
            		sql.append(" and (OWNER=? or OWNER=0) ");
            	}
            	sql.append(" order by f.isFile desc, CONVERT (u.fullname USING gb2312) "+orderType+",f.ID DESC LIMIT ?, ? ");
            }else{
            	sql.append("select ID from ").append(tableName).append(" WHERE CLASSID=? and isDelete='0' and isLast='1' ");
            	if (isFilterUser) {
            		sql.append(" and (OWNER=? or OWNER=0) ");
            	}
            	if("2".equals(orderField)){
            		sql.append(" order by isFile desc, convert(filename using gb2312) "+orderType+",ID desc LIMIT ?, ? ");
            	}else if("3".equals(orderField)){
            		sql.append(" order by isFile desc, size "+orderType+",ID desc LIMIT ?, ? ");
            	}else if("6".equals(orderField)){
            		sql.append(" order by isFile desc, OPENLEVEL "+orderType+",ID desc LIMIT ?, ? ");
            	}else if("7".equals(orderField)){   //liuwei 2016/01/06
            		sql.append(" order by isFile desc, type "+orderType+",ID desc LIMIT ?, ? ");
            	}else{
            		sql.append(" order by isFile desc, serialNumber "+orderType+",ID desc LIMIT ?, ? ");
            	}
            }
            pst = conn.prepareStatement(sql.toString());
            if (isFilterUser) {
                pst.setString(1, classId);
                pst.setString(2, userId);
                pst.setInt(3, start);
                pst.setInt(4, limit);
            } else {
                pst.setString(1, classId);
                pst.setInt(2, start);
                pst.setInt(3, limit);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    /**
     * 获取我的文件id的列表
     * @param companyId 公司ID
     * @param userId 用户id
     * @param start
     * @param limit
     * @return
     */
    public List<String> getMyFileIdList(String companyId, String userId, int start, int limit,String orderType,String orderField) {
    	/** orderField
    	 * 几种排序
    	 * 1.默认排序
    	 * 2.文件名排序
    	 * 3.大小排序
    	 * 4.上传人排序
    	 * 5.上传日期排序（上传日期其实也就是默认排序）
    	 */
    	/**
    	 * orderType
    	 * 排序类型
    	 * 升序 降序
    	 * 默认降序
    	 */
    	if(orderType==null || orderType.trim().length()==0){
    		orderType = " desc ";
    	}
    	if(orderField==null || orderField.trim().length()==0){
    		orderField = "1";
    	}
        List<String> list = new ArrayList<String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_" + companyId;
            StringBuilder sql = new StringBuilder();
        	if("2".equals(orderField)){
        		sql.append("select ID from ").append(tableName).append(" WHERE isDelete='0' and isLast='1' and openlevel in('1', '2', '3') and OWNER=? ORDER BY convert(filename using gb2312) "+orderType+",ID DESC LIMIT ?, ? ");
        	}else if("3".equals(orderField)){
        		sql.append("select ID from ").append(tableName).append(" WHERE isDelete='0' and isLast='1' and openlevel in('1', '2', '3') and OWNER=? ORDER BY SIZE "+orderType+",ID DESC LIMIT ?, ? ");
        	}else if("6".equals(orderField)){
        		sql.append("select ID from ").append(tableName).append(" WHERE isDelete='0' and isLast='1' and openlevel in('1', '2', '3') and OWNER=? ORDER BY openlevel "+orderType+",ID DESC LIMIT ?, ? ");
        	}else{
        		sql.append("select ID from ").append(tableName).append(" WHERE isDelete='0' and isLast='1' and openlevel in('1', '2', '3') and OWNER=? ORDER BY ID DESC LIMIT ?, ? ");
        	}
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, userId);
            pst.setInt(2, start);
            pst.setInt(3, limit);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    /**
     * 获取文件列表
     * @param companyId
     * @param ids
     * @return
     */
    public ArrayList<Map<String, String>> getFileListByIds(String companyId, List<String> idList) {
        String ids = StringUtil.list2String(idList, ",");
        if (idList == null || idList.isEmpty()) {
        	return new ArrayList<Map<String, String>>();
        }
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for(int i=0;i<idList.size();i++){
        	list.add(null);
        }
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_" + companyId;
            StringBuilder sql = new StringBuilder();
            sql.append(" select * from ")
               .append(tableName)
               .append(" where id in(").append(ids).append(")");
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> file = new HashMap<String, String>();
                String id = rs.getString("id");
                file.put("id", id); // 当前数据id
                file.put("classId", rs.getString("classId")); // 所属分类id
                file.put("fileName", StringUtil.string(rs.getString("fileName")));
                file.put("userId", StringUtil.string(rs.getString("creator")));
                file.put("owner", StringUtil.string(rs.getString("owner")));
                //file.put("owner_v1", StringUtil.string(rs.getString("OWNER_V1")));
                file.put("createTime", StringUtil.string(rs.getString("createTime")));
                file.put("size", StringUtil.string(rs.getString("size")));
                file.put("md5", StringUtil.string(rs.getString("md5")));
                file.put("type", StringUtil.string(rs.getString("type")));
                file.put("praiseCount", StringUtil.string(rs.getString("praiseCount")));
                file.put("isDelete", StringUtil.string(rs.getString("isDelete")));
                file.put("isFile", StringUtil.string(rs.getString("isFile")));
                String idSeqSrc = StringUtil.string(rs.getString("idSeq"));
                String idSeq = idSeqSrc + id + ".";
                file.put("idSeqSrc", idSeqSrc);
                file.put("idSeq", idSeq);
                file.put("version", StringUtil.string(rs.getString("version")));
                file.put("fileId", StringUtil.string(rs.getString("fileId")));
                file.put("openLevel", rs.getString("openlevel"));
                file.put("serialNumber", rs.getString("SERIALNUMBER"));
                file.put("soleNumber", rs.getString("SOLENUMBER"));
                int index = idList.indexOf(id);
                list.set(index,file);
            }
            
            //去掉null
            for(int i=0;i<list.size();i++){
            	if(list.get(i)==null){
            		list.remove(i);
            	}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }

    /**
     * 获取文件列表总count
     * @param companyId
     * @param classId
     * @param userId
     * @return
     */
    public int getFileListCount(String companyId, String classId, String userId) {
        boolean isFilterUser = false;
        if (null != userId && !"".equals(userId) && !"0".equals(userId)) {
            isFilterUser = true;
        }
        int count = 0;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_"+companyId;
            StringBuffer sql = new StringBuffer();
            sql.append("select count(id) from ").append(tableName).append(" WHERE CLASSID=? and isDelete='0' and isLast='1'");
            if (isFilterUser) {
                sql.append(" and (OWNER=? or OWNER=0)");
            }
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, classId);
            if (isFilterUser) {
                pst.setString(2, userId);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return count;
    }
    
    /**
     * 获取文件总count 为了显示文件和文件夹的数量
     * @param companyId
     * @param classId
     * @param userId
     * @return
     */
    public int getFileCount(String companyId, String classId, String userId) {
    	boolean isFilterUser = false;
    	if (null != userId && !"".equals(userId) && !"0".equals(userId)) {
    		isFilterUser = true;
    	}
    	int count = 0;
    	Session session = null ;
    	Connection conn = null ;
    	PreparedStatement pst = null ;
    	ResultSet rs = null ;
    	try {
    		session = getSession();
    		conn = session.connection();
    		String tableName = "files_"+companyId;
    		StringBuffer sql = new StringBuffer();
    		sql.append("select count(id) from ").append(tableName).append(" WHERE CLASSID=? and isDelete='0' and isLast='1' and isFile='1'");
    		if (isFilterUser) {
    			sql.append(" and (OWNER=? or OWNER=0)");
    		}
    		pst = conn.prepareStatement(sql.toString());
    		pst.setString(1, classId);
    		if (isFilterUser) {
    			pst.setString(2, userId);
    		}
    		rs = pst.executeQuery();
    		if (rs.next()) {
    			count = rs.getInt(1);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		JdbcUtil.close(rs, pst, conn);
    	}
    	return count;
    }
    
    /**
     * 获取我的文件列表总count
     * @param companyId
     * @param userId
     * @return
     */
    public int getMyFileListCount(String companyId, String userId) {
        int count = 0;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_"+companyId;
            StringBuffer sql = new StringBuffer(); 
            sql.append("select count(id) from ").append(tableName).append(" WHERE isDelete='0' and isLast='1' and openlevel in('1', '2', '3') and OWNER=?");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, userId);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return count;
    }
    
    /**
     * 获取文件信息
     * @param fileId 文件id
     * @return
     */
    public Map<String, String> getFileInfo(String companyId, String fileId, List<Map<String,String>> filedLst) {
        Map<String, String> file = new HashMap<String, String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select "
            		+ "id, "
            		+ "CLASSID, "
            		+ "fileName, "
            		+ "creator, "
            		+ "isFile,"
            		+ "owner, "
            		+ "createTime, "
            		+ "size, "
            		+ "type, "
            		+ "praiseCount, "
            		+ "(SELECT SUM(PRAISECOUNT) FROM files_"+companyId+" t where t.FILENAME = f.FILENAME) sum_praiseCount , "
            		+ "idSeq, "
            		+ "version, "
            		+ "fileId, "
            		+ "md5, "
            		+ "isDelete, "
            		+ "isLast, "
            		+ "openlevel, "
            		+ "soleNumber  from files_"+companyId+" f where ID = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, fileId);
            rs = pst.executeQuery();
            if (rs.next()) {
                String id = rs.getString("id");
                String isFile = rs.getString("isFile");
                file.put("isFile", isFile);
                file.put("id", id);
                file.put("fileName", StringUtil.string(rs.getString("fileName")));
                file.put("userId", StringUtil.string(rs.getString("creator")));
                file.put("owner", StringUtil.string(rs.getString("owner")));
               // file.put("owner_v1", StringUtil.string(rs.getString("owner_v1")));//此为第一版文件的所有者,此所有者决定所有版本的权限
                file.put("createTime", StringUtil.string(rs.getString("createTime")));
                file.put("sizeNum", StringUtil.string(rs.getString("size")));
                String str1 = StringUtil.string(rs.getString("size"));
                str1 = new StringBuilder(str1).reverse().toString();     //先将字符串颠倒顺序  
                String str2 = "";  
                for(int i=0;i<str1.length();i++){  
                    if(i*3+3>str1.length()){  
                        str2 += str1.substring(i*3, str1.length());  
                        break;  
                    }
                    str2 += str1.substring(i*3, i*3+3)+",";  
                }  
                if(str2.endsWith(",")){  
                    str2 = str2.substring(0, str2.length()-1);  
                }  
                //最后再将顺序反转过来  
               // System.err.println(new StringBuilder(str2).reverse().toString()); 
                file.put("size", new StringBuilder(str2).reverse().toString());
                file.put("md5", StringUtil.string(rs.getString("md5")));
                file.put("type", StringUtil.string(rs.getString("type")));
                file.put("praiseCount", StringUtil.string(rs.getString("praiseCount")));
                file.put("sum_praiseCount", StringUtil.string(rs.getString("sum_praiseCount")));
                file.put("isDelete", StringUtil.string(rs.getString("isDelete")));
                //liqiubo 20150612 为了支持分类也能全文检索后跳转，故在此拼了下参数，如果有影响，请更改
                String idSeqSrc = StringUtil.string(rs.getString("idSeq"));
                String idSeq = idSeqSrc + id + ".";
                file.put("classId", "1.".equals(idSeqSrc)?id:StringUtil.string(rs.getString("classId")));
                file.put("idSeqSrc", "1.".equals(idSeqSrc)?"1."+id+".":idSeqSrc);
                file.put("idSeq", idSeq);
                file.put("rootIdseq", !"1.".equals(idSeqSrc)?((idSeqSrc==null||idSeqSrc.length()==0)?"":idSeqSrc.substring(0, idSeqSrc.indexOf(".", 2))+"."):idSeq);
                file.put("version", StringUtil.string(rs.getString("version")));
                file.put("fileId", StringUtil.string(rs.getString("fileId")));
                file.put("openLevel", rs.getString("openlevel"));
                file.put("isLast", rs.getString("isLast"));
                file.put("soleNumber", rs.getString("soleNumber"));
                //liqiubo 20140407 把自定义的字段也查出来
//                if(filedLst!=null && filedLst.size()>0){
//                	for(Map<String,String> field:filedLst){
//                		file.put("C"+field.get("ID"),StringUtil.string(rs.getString("C"+field.get("ID"))));
//                	}
//                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return file;
    }
    
    /**
	 * lujixiang 20150806 根据文件夹获取所欲文件详情 
	 * @param companyId
	 * @param folderId
	 */
    public List<Map<String,String>> getFilesByFoldId(String companyId,String folderId){

        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "FILES_" + companyId;
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ").append(tableName).append(" WHERE CLASSID=? AND ISDELETE='0' AND ISLAST='1' AND ISFILE='0' ");
            if(!"1".equals(folderId)){
            	sql.append(" ORDER BY ID DESC ");
            }
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, folderId);
            rs = pst.executeQuery();
            while (rs.next()) {
            	Map<String,String> file = new HashMap<String, String>();
            	
            	 String id = rs.getString("id");
                 String isFile = rs.getString("isFile");
                 file.put("isFile", isFile);
                 file.put("id", id);
                 file.put("fileName", StringUtil.string(rs.getString("fileName")));
                 file.put("userId", StringUtil.string(rs.getString("creator")));
                 file.put("owner", StringUtil.string(rs.getString("owner")));
                 file.put("createTime", StringUtil.string(rs.getString("createTime")));
                 file.put("size", StringUtil.string(rs.getString("size")));
                 file.put("md5", StringUtil.string(rs.getString("md5")));
                 file.put("type", StringUtil.string(rs.getString("type")));
                 file.put("praiseCount", StringUtil.string(rs.getString("praiseCount")));
                 file.put("isDelete", StringUtil.string(rs.getString("isDelete")));
                 //liqiubo 20150612 为了支持分类也能全文检索后跳转，故在此拼了下参数，如果有影响，请更改
                 String idSeqSrc = StringUtil.string(rs.getString("idSeq"));
                 String idSeq = idSeqSrc + id + ".";
                 file.put("classId", "1.".equals(idSeqSrc)?id:StringUtil.string(rs.getString("classId")));
                 file.put("idSeqSrc", "1.".equals(idSeqSrc)?"1."+id+".":idSeqSrc);
                 file.put("idSeq", idSeq);
                 file.put("rootIdseq", !"1.".equals(idSeqSrc)?((idSeqSrc==null||idSeqSrc.length()==0)?"":idSeqSrc.substring(0, idSeqSrc.indexOf(".", 2))+"."):idSeq);
                 file.put("version", StringUtil.string(rs.getString("version")));
                 file.put("fileId", StringUtil.string(rs.getString("fileId")));
                 file.put("openLevel", rs.getString("openlevel"));
                 file.put("isLast", rs.getString("isLast"));
            	
                list.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    
    }
    
    /**
     * 获取用户拥有权限的文件id列表
     * @param companyId
     * @param userId
     * @return  wangwenshuo 20160113 modify 修改返回值  key为fileId_forAllVersion 
     * 										此时只有WS层setupUserReadRight方法调用，修改此方法时注意
     * @return
     */
    public Map<String,Map<String,String>> getFileUserRelationWithUserId(String companyId, String userId, List<String> groupIdList) {
        String groupIds = StringUtil.list2String(groupIdList, ",");
        Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select FILEID,isDownload,isLook,forAllVersion from fileuserrelation WHERE COMPANYID=? ";
            if (groupIdList != null && !groupIdList.isEmpty()) {
                sql += "and (USERID=? or groupid in ("+groupIds+"))";
            } else {
                sql += "and USERID=?"; 
            }
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            pst.setString(2, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
              //修改返回值  key为fileId_forAllVersion
              String fileId = rs.getString("FILEID")+"_"+rs.getString("forAllVersion");
              String isDownload = rs.getString("isDownload");
              String isLook = rs.getString("isLook");
              Map<String,String> map = new HashMap<String,String>();
              if(result.keySet().contains(fileId)){
                if("1".equals(isDownload))
                result.get(fileId).put("isDownload",isDownload);
                if("1".equals(isLook))
                result.get(fileId).put("isLook", isLook);
              }else{
                map.put("isDownload", isDownload);
                map.put("isLook", isLook);
                result.put(fileId, map);
              }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return result;
    }
    
    /**
     * 获取公司的成员列表
     * @param companyId
     * @return
     */
    public List<Map<String, String>> getMembers(String companyId) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select id, username, fullname, portrait from users where enabled=1 and companyid=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> user = new HashMap<String, String>();
                user.put("id", StringUtil.string(rs.getString("id")));
                user.put("userName", StringUtil.string(rs.getString("userName")));
                user.put("fullName", StringUtil.string(rs.getString("fullName")));
                user.put("portrait", StringUtil.string(rs.getString("portrait")));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    /**
     * 获取用户信息
     * @param companyId
     * @param userId
     * @return
     */
    public Map<String, String> getMemberById(String companyId, String userId) {
        Map<String, String> user = new HashMap<String, String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select u.id, u.username, u.fullname, u.portrait from company_users cu left join users u on cu.USERID=u.ID where enabled=1 and cu.COMPANYID=? and u.id=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            pst.setString(2, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                user.put("id", StringUtil.string(rs.getString("id")));
                user.put("userName", StringUtil.string(rs.getString("userName")));
                user.put("fullName", StringUtil.string(rs.getString("fullName")));
                user.put("portrait", StringUtil.string(rs.getString("portrait")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return user;
    }

    /**
     * 是否有权限查看文件
     * @param fileId 文件id
     * @param userId 用户id
     * @return
     */
    public boolean hasRightToReadFile(String fileId, String userId) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 分享一个文件给某个用户（允许其查看和下载文件）
     * @param companyId
     * @param fileId
     * @param toUserId
     * @return
     */
    public boolean shareFileToUserOrGroup(String companyId, String fileId, String targetId, boolean isGroup, String fromUserName,String isDownload,String isLook) {
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook) values(?,?,?,0,?,?,?)";
        if (isGroup) {
            sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook) values(?,?,0,?,?,?,?)";
        }
        int num = 0;
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            pst.setString(2, fileId);
            pst.setString(3, targetId);
            pst.setString(4, fromUserName);
            pst.setString(5, isDownload);
            pst.setString(6, isLook);
            num = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return num > 0;
    }
    /**
     * 更新一个文件给某个用户（允许其查看或下载文件）
     * @param companyId
     * @param fileId
     * @param toUserId
     * @param forAllVersion 是否所有版本拥有权限    wangwenshuo 20160112add 没时间重构  就先加参数了
     * @return
     */
    public boolean updateOrShareFileToUserOrGroup(String companyId, String fileId, String targetId, boolean isGroup, String fromUserName,boolean isApplyLabel,String applyLabel,String isDownload,String isLook, boolean forAllVersion){
    	Session session = null ;
    	Connection conn = null ;
    	PreparedStatement pst = null ;
    	String sql="";
    	
    	if(forAllVersion){
    		if(isApplyLabel){//更新下载、更新浏览
    			//wangwenshuo 分享文件  撤销分享权限修改
    			if("申请".equals(applyLabel)){
    				sql="update fileuserrelation set isDownload="+isDownload+",isLook="+isLook+" where companyid=? and fileId=? and USERID=? and groupid=0 and APPROVEUSERNAME=? and forallversion='1'";
    			}else{
    				if("isDownload".equals(applyLabel)){
    					sql="update fileuserrelation set isDownload="+isDownload+",isLook="+isLook+" where companyid=? and fileId=? and USERID=? and groupid=0 and APPROVEUSERNAME=? and forallversion='1'";
    					if (isGroup) {
    						sql="update fileuserrelation set isDownload="+isDownload+",isLook="+isLook+" where companyid=? and fileId=? and userid=0 and groupid=? and APPROVEUSERNAME=? and forallversion='1'";
    					}
    				}else{
    					sql="update fileuserrelation set isLook="+isLook+" where companyid=? and fileId=? and USERID=? and groupid=0 and APPROVEUSERNAME=? and forallversion='1'";
    					if (isGroup) {
    						sql="update fileuserrelation set isLook="+isLook+" where companyid=? and fileId=? and userid=0 and groupid=? and APPROVEUSERNAME=? and forallversion='1'";
    					}
    				}
    			}
    		}else{//插入下载、插入浏览
    			if("申请".equals(applyLabel)){
    				sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,forallversion) values(?,?,?,0,?,'1')";
    			}else{
    				if("isDownload".equals(applyLabel)){
    					sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook,forallversion) values(?,?,?,0,?,1,1,'1')";
    					if (isGroup) {
    						sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook,forallversion) values(?,?,0,?,?,1,1,'1')";
    					}
    				}else{
    					sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook,forallversion) values(?,?,?,0,?,0,1,'1')";
    					if (isGroup) {
    						sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook,forallversion) values(?,?,0,?,?,0,1,'1')";
    					}
    				}
    			}
    		}
    	}else{
    		if(isApplyLabel){//更新下载、更新浏览
    			//wangwenshuo 分享文件  撤销分享权限修改
    			if("isShareUpdateRight".equals(applyLabel)){
    				sql="update fileuserrelation set isDownload="+isDownload+",isLook="+isLook+" where companyid=? and fileId=? and userid=? and groupid=0 and APPROVEUSERNAME=? and forallversion='0'";
    				if (isGroup) {
    					sql="update fileuserrelation set isDownload="+isDownload+",isLook="+isLook+" where companyid=? and fileId=? and userid=0 and groupid=? and APPROVEUSERNAME=? and forallversion='0'";
    				}
    			}else if("申请".equals(applyLabel)){
    				sql="update fileuserrelation set isDownload="+isDownload+",isLook="+isLook+" where companyid=? and fileId=? and USERID=? and groupid=0 and APPROVEUSERNAME=? and forallversion='0'";
    			}else{
    				if("isDownload".equals(applyLabel)){
    					sql="update fileuserrelation set isDownload="+isDownload+",isLook="+isLook+" where companyid=? and fileId=? and USERID=? and groupid=0 and APPROVEUSERNAME=? and forallversion='0'";
    					if (isGroup) {
    						sql="update fileuserrelation set isDownload="+isDownload+",isLook="+isLook+" where companyid=? and fileId=? and userid=0 and groupid=? and APPROVEUSERNAME=? and forallversion='0'";
    					}
    				}else{
    					sql="update fileuserrelation set isLook="+isLook+" where companyid=? and fileId=? and USERID=? and groupid=0 and APPROVEUSERNAME=? and forallversion='0'";
    					if (isGroup) {
    						sql="update fileuserrelation set isLook="+isLook+" where companyid=? and fileId=? and userid=0 and groupid=? and APPROVEUSERNAME=? and forallversion='0'";
    					}
    				}
    			}
    		}else{//插入下载、插入浏览
    			if("申请".equals(applyLabel)){
    				sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,forallversion) values(?,?,?,0,?,'0')";
    			}else{
    				if("isDownload".equals(applyLabel)){
    					sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook,forallversion) values(?,?,?,0,?,1,1,'0')";
    					if (isGroup) {
    						sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook,forallversion) values(?,?,0,?,?,1,1,'0')";
    					}
    				}else{
    					sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook,forallversion) values(?,?,?,0,?,0,1,'0')";
    					if (isGroup) {
    						sql = "insert into fileuserrelation(companyid,fileid,userid,groupid,APPROVEUSERNAME,isDownload,isLook,forallversion) values(?,?,0,?,?,0,1,'0')";
    					}
    				}
    			}
    		}
    	}
    	int num = 0;
    	try {
    		session = getSession();
    		conn = session.connection();
    		pst = conn.prepareStatement(sql);
    		pst.setString(1, companyId);
    		pst.setString(2, fileId);
    		pst.setString(3, targetId);
    		pst.setString(4, fromUserName);
    		num = pst.executeUpdate();	
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		JdbcUtil.close(pst, conn);
    	}
    	return num > 0;
    }
    /**
     * 取消分享一个文件给某个用户（允许其查看和下载文件）
     * @param companyId
     * @param fileId
     * @param toUserId
     * @return
     */
    public boolean unShareFileToUser(String companyId, String fileId, String targetId) {
    	Session session = null ;
    	Connection conn = null ;
    	PreparedStatement pst = null ;
    	String sql = "delete from fileuserrelation where companyid=? and fileid=? and userid=? and forallversion='1'";
    	int num = 0;
    	try {
    		session = getSession();
    		conn = session.connection();
    		pst = conn.prepareStatement(sql);
    		pst.setString(1, companyId);
    		pst.setString(2, fileId);
    		pst.setString(3, targetId);
    		num = pst.executeUpdate();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		JdbcUtil.close(pst, conn);
    	}
    	return num > 0;
    }
    
    /**
     * 分享多个个文件给某个用户（允许其查看和下载文件）
     * @param companyId
     * @param fileIdList
     * @param toUserId
     * @return
     */
    public boolean shareFilesToUser(String companyId, List<String> fileIdList, String toUserId, String fromUserName) {
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String sql = "insert into fileuserrelation(companyid,fileid,userid,APPROVEUSERNAME) values(?,?,?,?)";
        int num = 0;
        try {
            session = getOpenSession();
            conn = session.connection();
            conn.setAutoCommit(false);
            pst = conn.prepareStatement(sql);
            for (String fileId : fileIdList) {
                pst.setString(1, companyId);
                pst.setString(2, fileId);
                pst.setString(3, toUserId);
                pst.setString(4, fromUserName);
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
    
    /**
     * 判断文件是否已经分享给某用户
     * @param companyId
     * @param fileId
     * @param targetId
     * @param isGroup
     * @return
     */
    public boolean hasShareFileToUserOrGroup(String companyId, String fileId, String targetId, boolean isGroup) {
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        String sql = "select count(id) from fileuserrelation where companyid=? and fileid=? and forallversion='0'";
        if (isGroup) {
            sql += " and groupid=?";
        } else {
            sql += " and userid=?";
        }
        int num = 0;
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            pst.setString(2, fileId);
            pst.setString(3, targetId);
            rs = pst.executeQuery();
            if (rs.next()) {
                num = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return num > 0;
    }
    /**
     * 判断文件是否已经分享给某用户
     * @param companyId
     * @param fileId
     * @param targetId   
     * @param isGroup
     * @return
     * 分享鍀个人，
     * 分享鍀分类
     * 以上都是唯一一条数据。
     */
    public Map<String, String> getHasShareFileToUserOrGroup(String companyId,String fileId,String targetId, boolean isGroup, boolean forAllVersion){
    	Map<String, String> retMap=new HashMap<String, String>();
    	Session session = null ;
    	Connection conn = null ;
    	PreparedStatement pst = null ;
    	ResultSet rs = null;
    	String sql = "select isDownload,isLook from fileuserrelation where companyid=? and fileid=? ";
		if (isGroup) {
			sql += " and groupid=?";
		} else {
			sql += " and userid=?";
		}
		if (forAllVersion) {
			sql += " and forallversion='1'";
		} else {
			sql += " and forallversion='0'";
		}
    	try {
    		session = getSession();
    		conn = session.connection();
    		pst = conn.prepareStatement(sql);
    		pst.setString(1, companyId);
    		pst.setString(2, fileId);
    		pst.setString(3, targetId);
    		rs = pst.executeQuery();
    		if (rs.next()) {
    			retMap.put("isDownload", rs.getString("isDownload"));
    			retMap.put("isLook", rs.getString("isLook"));
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		JdbcUtil.close(pst, conn);
    	}
    	return retMap;
    }
    
    /**
     * 从fileIds中找出已经有权限的fileIds
     * @param companyId
     * @param fileIds
     * @param toUserId
     * @return
     */
    public List<String> findFileIdsForHasRight(String companyId, String fileIds, String toUserId) {
        List<String> list = new ArrayList<String>();
        if (fileIds != "" && (fileIds.lastIndexOf(",") == fileIds.length() - 1)) {
            fileIds = fileIds.substring(0, fileIds.length() - 1);
        }
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        String sql = "select FILEID from fileuserrelation where COMPANYID=? and USERID=? and forallversion='0' and FILEID in("+fileIds+")";
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            pst.setString(2, toUserId);
            rs = pst.executeQuery();
            if (rs.next()) {
                list.add(rs.getString(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    /**
     * 获取某个用户所有已经点赞的文件id列表
     * @param companyId
     * @param userId
     * @return
     */
    public List<String> getPraiseFileIdsWithUserId(String companyId, String userId) {
        List<String> list = new ArrayList<String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        String sql = "select fileId from filepraise_" + companyId + " where userId=?";
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }

    /**
     * 点赞/取消赞文件
     * @param fileId 文件id
     * @param userId 用户id
     * @param status true：赞， false：取消赞
     * @return
     */
    public boolean praiseFile(String companyId, String fileId, String userId, boolean status) {
        boolean flag = false;
        String sql = "";
        String sqlInsert = "insert into filepraise_" + companyId + "(fileId, userId) values(?,?)";
        String sqlDelete = "delete from filepraise_" + companyId + " where fileId=? and userId=?";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            session = getSession();
            conn = session.connection();
            if (status) {
                sql = sqlInsert;
            } else {
                sql = sqlDelete;
            }
            pst = conn.prepareStatement(sql);
            pst.setString(1, fileId);
            pst.setString(2, userId);
            int num = pst.executeUpdate();
            flag = (num > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
    }
    
    /**
     * 更新赞数量
     * @param fileId
     * @param status true：赞， false：取消赞
     * @return
     */
    public boolean praiseCountUpdate(String companyId, String fileId, boolean status) {
        boolean flag = false;
        String sql = "update files_" + companyId + " set praisecount=ifnull(praisecount,0)+"+(status?1:-1)+" where id=?";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, fileId);
            int num = pst.executeUpdate();
            flag = (num > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
    }

    /**
     * 收藏/取消收藏文件
     * @param fileId 文件id
     * @param userId 用户id
     * @param status true：收藏， false：取消收藏
     * @return
     */
    public boolean collectFile(String fileId, String userId, boolean status) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 逻辑删除文件，修改isDelete状态值为0
     * @param fileId 文件id
     * @return
     */
    public boolean deleteFile(String companyId, String fileId) {
        boolean flag = false;
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "update files_" + companyId + " set isDelete='1' where id=? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, fileId);
            int num = pst.executeUpdate();
            flag = (num > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
    }

    /**
     * 取消一个文件的分享
     * @param fileId
     * @return
     */
    public boolean cancelShareFile(String fileId) {
        // TODO Auto-generated method stub
        return false;
    }
    /**
     * 设为私密：撤销本文件所有分享
     * @param fileId
     * @return
     */
    public boolean setPrivacyAnnulShare(String fileId){
    	 boolean flag = false;
         Session session = null;
         Connection conn = null;
         PreparedStatement pst = null;
         try {
             session = getSession();
             conn = session.connection();
             String sql = "delete from fileuserrelation where fileid=? and forallversion='0'";
             pst = conn.prepareStatement(sql);
             pst.setString(1, fileId);
             int num = pst.executeUpdate();
             flag = (num > 0);
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             JdbcUtil.close(pst, conn);
         }
         return flag;
    }
    
    public int insertIntoFilesN(Map<String,String> map){
		 Session session =this.getSession(false);
		 Connection conn = null;
		 PreparedStatement st = null;
		 ResultSet rs = null;
		 int resultId = 0;
		try {
//			String mongoid = map.get("classId")+"_onlinefile_"+map.get("filename")+map.get("type").hashCode();
//			int version = this.getVersion(map.get("companyId"),mongoid );
		    String companyId = map.get("companyId"); 
		    String classId = map.get("classId"); 
		    String fileName = map.get("filename"); 
		    String fileType = map.get("type");
		    this.updateFilesIsNotLast(companyId, classId, fileName, fileType);
			int version = this.getNextVersion(companyId, classId, fileName, fileType);
			map.put("version", version+"");
			String idSeq = "";
			String sql = "select CONCAT(IDseq  ,ID,'.') FROM FILES_"+map.get("companyId")+" tmp WHERE ID="+map.get("classId");
			conn = session.connection();
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			if(rs.next()){
				idSeq = rs.getString(1);
			}
			JdbcUtil.close(rs, st);
			map.put("idSeq", idSeq);
			sql ="INSERT INTO "+map.get("filesN")+"(CLASSID,FILENAME,CREATOR,OWNER,CREATETIME,SIZE,MD5,TYPE,ISDELETE,FILEID,IDSEQ,VERSION,ISLAST,OPENLEVEL) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, Integer.parseInt(map.get("classId")));
			st.setString(2, map.get("filename"));
			st.setInt(3, Integer.parseInt(map.get("userId")));
			st.setInt(4, Integer.parseInt(map.get("userId")));
			st.setString(5, map.get("createtime"));
			st.setString(6, map.get("length"));
			st.setString(7, map.get("md5"));
			st.setString(8, map.get("type"));
			st.setString(9,"0");
			st.setString(10, map.get("id"));
			st.setString(11, idSeq);
			st.setString(12, version+"");
			st.setString(13, "1");
			st.setString(14,map.get("openlevel"));
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			String id = "";
			if(rs.next()){
				resultId = rs.getInt(1);
				id = resultId + "";
				map.put("insertId", id);
			}
			//存完了 把ID插入到mongo里
//			String idLst = this.getVersionIdLst(map.get("companyId"),mongoid);
//			String insertIdLst = (idLst==null?"":idLst)+id+",";
//			this.insertVersionIdLst(map.get("companyId"),mongoid, insertIdLst);
//			if(version>1 && idLst!=null && idLst.trim().length()>0){
//				this.updateIsLast(map.get("companyId"), idLst);
//				//TODO 更新其他字段
//			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			JdbcUtil.close(rs, st, conn);
		}
		return resultId;
	}
    
    /**
     * 更新id集合的islast为0
     * @param companyId
     * @param idLst
     * @return
     */
    private boolean updateIsLast(String companyId,String idLst){
    	 Session session = null;
         Connection conn = null;
         PreparedStatement pst = null;
         StringBuffer sql = new StringBuffer();
         sql.append("UPDATE FILES_"+companyId+" SET ISLAST='0' ");
         sql.append(" WHERE ID in("+idLst+"0)");
         try {
             session = getSession();
             conn = session.connection();
             pst = conn.prepareStatement(sql.toString());
             pst.executeUpdate();
             return true;
         } catch (SQLException e) {
         	e.printStackTrace();
         	return false;
         } finally {
             JdbcUtil.close(null, pst, conn);
         }
    }
    
    /**
     * 获取mongo中，文件的下一个版本号
     * @param companyId
     * @param id  classid + _onlinefile_ + 文件名 + type.hashcode
     * @return
     */
    private int getVersion(String companyId,String id){
		DB db = mongo.getDB("company_"+companyId);
		DBCollection sequence = db.getCollection("files");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        BasicDBObject newDocument =new BasicDBObject();
        newDocument.put("$inc", new BasicDBObject().append("version", 1));
        BasicDBObject ret = (BasicDBObject)sequence.findAndModify(query, newDocument);
        if (ret == null){
          BasicDBObject object = new BasicDBObject() ;
          object.put("_id", id) ;
          object.put("version", 1) ;
          sequence.save(object) ;
          return 1;
        }else{
          return (int) (ret.getLong("version") + 1);
        }
    }
    
    /**
     * 获取mongo中，文件的所有版本的id，用逗号隔开
     * @param companyId
     * @param id classid + _onlinefile_ + 文件名 + type.hashcode
     * @return
     */
    private String getVersionIdLst(String companyId,String id){
    	DB db = mongo.getDB("company_"+companyId);
		DBCollection sequence = db.getCollection("files");
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        DBCursor cursor = sequence.find(query);
        String str = "";
        if(cursor.hasNext()){
        	BasicDBObject obj = (BasicDBObject)cursor.next() ;
        	str = obj.getString("idlst");
        }
        return str;
    }
    
    /**
     * 更新mongo中，文件的所有版本的id
     * @param companyId
     * @param id classid + _onlinefile_ + 文件名 + type.hashcode
     * @param idlst
     * @return
     */
    private boolean insertVersionIdLst(String companyId,String id,String idlst){
    	try{
    		DB db = mongo.getDB("company_"+companyId);
    		DBCollection sequence = db.getCollection("files");
            BasicDBObject query = new BasicDBObject();
            query.put("_id", id);
            
            DBCursor cursor = sequence.find(query);
            if (cursor.hasNext()) { 
               DBObject updateDocument = cursor.next();
               updateDocument.put("idlst",idlst);
               sequence.update(new BasicDBObject("_id", id),updateDocument); 
            }
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    
    public String getIdSeqWithId(String companyId, String id) {
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String idSeq = "";
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select idseq from files_" + companyId + " where id=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                idSeq = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return idSeq == null ? "" : idSeq;
    }
    
    /**
     * 获取文件的所有版本
     * @param companyId
     * @param classId
     * @param fileName
     * @return
     */
    public List<Map<String, String>> getFileList4Version(String companyId, String classId, String fileName, String fileType) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            StringBuffer sql = new StringBuffer();
            String tableName = "files_" + companyId;
            sql.append("select id, CLASSID, fileName, creator, owner, createTime, size, type, praiseCount, idSeq, version, fileId, md5, isDelete, isLast, openlevel, soleNumber from ")
                    .append(tableName).append(" WHERE CLASSID=? AND FILENAME=? AND TYPE=? AND ISDELETE='0' AND ISFILE='1' ORDER BY VERSION DESC");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, classId);
            pst.setString(2, fileName);
            pst.setString(3, fileType);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> file = new HashMap<String, String>();
                String id = rs.getString("id");
                file.put("id", id);
                file.put("classId", StringUtil.string(rs.getString("classId")));
                file.put("fileName", StringUtil.string(rs.getString("fileName")));
                file.put("userId", StringUtil.string(rs.getString("creator")));
                file.put("owner", StringUtil.string(rs.getString("owner")));
                file.put("createTime", StringUtil.string(rs.getString("createTime")));
                file.put("size", StringUtil.string(rs.getString("size")));
                file.put("type", StringUtil.string(rs.getString("type")));
                file.put("praiseCount", StringUtil.string(rs.getString("praiseCount")));
                file.put("isFile", "1");
                file.put("md5", rs.getString("md5"));
                file.put("isDel", rs.getString("isDelete"));
                file.put("isLast", rs.getString("isLast"));
                file.put("idSeq", StringUtil.string(rs.getString("idSeq")) + id + ".");
                file.put("idSeqSrc", StringUtil.string(rs.getString("idSeq")));  //wangwenshuo 20151021  添加IdSeqStr 用于复制的文件创建索引
                file.put("version", StringUtil.string(rs.getString("version")));
                file.put("fileId", StringUtil.string(rs.getString("fileId")));
                file.put("openLevel", rs.getString("openlevel"));
                file.put("soleNumber", rs.getString("soleNumber"));
                list.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    /**
     * 获取指定分类的分组信息列表
     * @param companyId
     * @param classIdList 分类id列表
     * @return
     */
    public List<Map<String, String>> getClassGroups(String companyId, List<String> classIdList) {
        String ids = StringUtil.list2String(classIdList, ",");
        if ("".equals(ids)) {
            ids = "-1";
        }
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select g.id, g.classid, g.groupname, g.FLAG from groups g INNER JOIN files_"+companyId+" f ON g.classId=f.ID and f.ID in ("+ids+")";
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> classGroup = new HashMap<String, String>();
                classGroup.put("id", StringUtil.string(rs.getString("id")));
                classGroup.put("classId", StringUtil.string(rs.getString("classId")));
                classGroup.put("groupName", StringUtil.string(rs.getString("groupName")));
                classGroup.put("flag", StringUtil.string(rs.getString("flag")));
                list.add(classGroup);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    /**
     * 获取某个用户关联的分组id列表
     * @param userId
     * @return
     */
    public List<String> getGroupIdsWithUserId(String userId) {
        List<String> list = new ArrayList<String>();
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select groupid from groupusersrelation where USERID=?";
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    @SuppressWarnings("deprecation")
	public boolean editFileOtherProp(String companyId,String fileId,List<String> columName,List<String> columVal){
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE FILES_"+companyId+" SET ");
        for(int i=0;i<columName.size();i++){
        	if(columVal.get(i)!=null && columVal.get(i).trim().length()>0){
        		sql.append(columName.get(i)+"='"+columVal.get(i)+"'");
        	}else{
        		sql.append(columName.get(i)+"=null ");
        	}
        	if(i+1<columName.size()){
        		sql.append(",");
        	}
        }
        sql.append(" WHERE ID="+fileId);
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql.toString());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
        	e.printStackTrace();
        	return false;
        } finally {
            JdbcUtil.close(null, pst, conn);
        }
    
    }
    
    /**
     * 获取某个企业下，所有文件夹的id，filename
     * @param companyId
     * @return
     */
    public List<Map<String, String>> getCompanyFolders(String companyId) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select id, filename from files_"+companyId+" where isfile='0'";
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                map = new HashMap<String, String>();
                map.put("ID", rs.getString("id"));
                map.put("FILENAME", rs.getString("filename"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    public List<Map<String,String>> getFolderById(String companyId,String folderId){

        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "FILES_" + companyId;
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ID,FILENAME,idSeq,SERIALNUMBER FROM ").append(tableName).append(" WHERE CLASSID=? AND ISDELETE='0' AND ISLAST='1' AND ISFILE='0' ");
            if(!"1".equals(folderId)){
            	sql.append(" ORDER BY ID ASC ");
            }
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, folderId);
            rs = pst.executeQuery();
            while (rs.next()) {
            	Map<String,String> map = new HashMap<String, String>();
            	String id = rs.getString("ID");
            	map.put("ID", id);
            	map.put("FILENAME", rs.getString("FILENAME"));
            	map.put("IDSEQ", rs.getString("IDSEQ")+id+".");
            	map.put("SERIALNUMBER", rs.getString("SERIALNUMBER"));
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    
    }
    
    /**
     * 获取公司的分组信息
     * @param companyId
     * @return
     */
    public Map<String, Map<String, String>> getCompanyGroups(String companyId, String username, String userId) {
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>() ;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ID, flag, classid,remark FROM groups WHERE companyid=? and classid > 0");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, companyId);
            rs = pst.executeQuery();
            while (rs.next()) {
                int classId = rs.getInt("classid");
                Map<String,String> map = new HashMap<String, String>();
                map.put("id", rs.getString("ID"));
                map.put("flag", rs.getString("flag"));
                map.put("classId", classId+"");
                map.put("remark", rs.getString("remark"));
                maps.put(classId+"", map) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return maps;
    }
    
    /**
     * 获取左侧公司的分组信息
     * @param companyId
     * @return
     */
    public Map<String, Map<String, String>> getLeftCompanyGroups(String companyId, String username, String userId) {
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Map<String, Map<String, String>> maps = new HashMap<String, Map<String, String>>() ;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT g.`ID`,g.`FLAG`,g.`CLASSID`,g.`GROUPNAME`,g.`REMARK`,u.`USERNAME`,u.`FULLNAME`,u.`PORTRAIT` FROM groups AS g LEFT JOIN groupusersrelation AS gu ON g.`ID`=gu.`GROUPID` LEFT JOIN users AS u ON gu.`USERID`=u.`ID`  WHERE g.`CLASSID` <> 0 and g.COMPANYID = ?;");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, companyId);
            rs = pst.executeQuery();
            while (rs.next()) {
                int classId = rs.getInt("classid");
                Map<String,String> map = new HashMap<String, String>();
                map.put("id", rs.getString("ID"));
                map.put("flag", rs.getString("flag"));
                map.put("classId", classId+"");
                map.put("remark", rs.getString("remark"));
                map.put("username", rs.getString("USERNAME"));
                map.put("fullname", rs.getString("FULLNAME"));                
                map.put("portrait", rs.getString("PORTRAIT"));
                maps.put(classId+"", map) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return maps;
    }
    
    /**
     * 获取@的数量
     * @param companyId
     * @param username
     * @return
     */
	private HashMap<String, String> getCallSelfNotSeeMessageCount(
			String companyId, String username) {
		HashMap<String, String> map = new HashMap<String, String>() ;
		DB db = mongo.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("groupCallOver");
		BasicDBObject cond = new BasicDBObject();
		cond.put("user", username) ;
		DBCursor cursor = coll.find(cond) ;
		BasicDBObject rs = null ;
		while(cursor.hasNext()){
			rs = (BasicDBObject)cursor.next() ;
        	map.put(rs.getString("from"), rs.getInt("c")+"") ;
        }
		return map;
	}

	/**
	 * 获取未看消息总数
	 */
	public HashMap<String, String> getNotSeeMessageCount(String companyId, String username) {
		HashMap<String, String> map = new HashMap<String, String>() ;
		DB db = mongo.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("newMessageCount");
		BasicDBObject cond = new BasicDBObject();
		cond.put("user", username) ;
		DBCursor cursor = coll.find(cond) ;
		BasicDBObject rs = null ;
		while(cursor.hasNext()){
			rs = (BasicDBObject)cursor.next() ;
			if(rs.getInt("c")!=0)
				map.put(rs.getString("from"), rs.getInt("c")+"") ;
        }
		return map;
	}

    /**
     * 是否属于某个群组分类成员
     * @param groupId
     * @param userId
     * @return
     */
    public boolean isGroupMember(String groupId, String userId) {
        boolean flag = false;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT count(id) FROM groupusersrelation WHERE groupid=? and userid=? ");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, groupId);
            pst.setString(2, userId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    flag = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return flag;
    }
    
    /**
     * 获取分组id
     * @param classId 分类id
     * @return
     */
    public String getGroupIdByClassId(String classId,String companyId) {
        String groupId = "-1";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT id FROM groups WHERE classid=? and companyid=?");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, classId);
            pst.setString(2, companyId);
            rs = pst.executeQuery();
            if (rs.next()) {
                groupId = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return groupId;
    }
    
    /**
     * 获取某用户加入的分类id列表
     * @param companyId
     * @param userId
     * @return
     */
    public List<String> getUserClassIds(String companyId, String userId) {
        List<String> list = new ArrayList<String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("select g.classId from groups g, groupusersrelation gr where g.ID=gr.GROUPID and g.companyid=? and gr.USERID=?");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, companyId);
            pst.setString(2, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                int classId = rs.getInt("classid");
                if (classId > 0) {
                    list.add(classId + "");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }


	public Object save(Object model) {
		// TODO Auto-generated method stub
		return null;
	}


	public void saveOrUpdate(Object model) {
		// TODO Auto-generated method stub
		
	}


	public void update(Object model) {
		// TODO Auto-generated method stub
		
	}


	public void merge(Object model) {
		// TODO Auto-generated method stub
		
	}


	public void delete(Object id) {
		// TODO Auto-generated method stub
		
	}


	public void deleteObject(Object model) {
		// TODO Auto-generated method stub
		
	}


	public Object get(Object id) {
		// TODO Auto-generated method stub
		return null;
	}


	public Object getObject(Object id) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean exists(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	/**
     * 通过用户ID查询用户关注的人
     * @param companyId
     * @param userId
     * @return
     */
    public List<Map<String,String>> getSubScribeUsersByUserId(String userId) {
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        Map<String,String> tmpUsers = null;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("select subscribename,subscribedate,subscribetime from usersubscribe where USERID=?");
            pst = conn.prepareStatement(sql.toString());
            pst.setInt(1, userId== null ? 0 : Integer.valueOf(userId));
            rs = pst.executeQuery();
            while (rs.next()) {
                tmpUsers = new HashMap<String, String>();
                tmpUsers.put("subscribename", rs.getString("subscribename"));
                tmpUsers.put("subscribetime", rs.getString("subscribetime"));
                tmpUsers.put("subscribedate", rs.getString("subscribedate"));
                list.add(tmpUsers);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
	

    /**
     * 获取当前公司的禁用的用户
     * @param companyId
     * @return
     */
    public List<String> getUnUsedUsersLists(String companyId){
    	List<String> unUsedUserLists = new ArrayList<String>();
    	Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("select username from users where companyId=? and status = 0");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, companyId);
            rs = pst.executeQuery();
            while (rs.next()) {
            	unUsedUserLists.add(rs.getString("username")) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
    	return unUsedUserLists;
    }
    
    public Map<String,String> getUserFullNameByCompanyId(String companyId){
    	Map<String,String> userFullNames = new HashMap<String, String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            //sql.append("select username,fullname from users where companyId=? and status = 1");
            sql.append("select username,fullname,portrait from users where id in (select userid from company_users where companyId=? and status = 1) ");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, companyId);
            rs = pst.executeQuery();
            while (rs.next()) {
            	userFullNames.put(rs.getString("username"),rs.getString("fullname")) ;
            	userFullNames.put(rs.getString("fullname"),rs.getString("portrait")) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
    
    	return userFullNames;
    }
    
    /**
     * 通过公司ID获取文件的版本号
     * @see cn.flying.rest.onlinefile.files.driver.FilesDao#getUserFullNameByCompanyId(java.lang.String)
     */
    public String getFileVersionByCompanyId(String companyId,String fileId){
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        String fileVersion = "1";
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("select VERSION from files_"+companyId+" where ID=?");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, fileId);
            rs = pst.executeQuery();
            while (rs.next()) {
            	fileVersion = rs.getString("VERSION");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
    
    	return fileVersion;
    }
    
	/**
	 * 通过用户ID查询出所有的用户的订约人的信息
	 * @see cn.flying.rest.onlinefile.files.driver.FilesDao#getSubScribeMsgByUserId(java.util.Map)
	 */
	public List<Map<String,String>> getSubScribeMsgByUserId(Map<String,String> map) {
		Date d=new Date();    
		String userId = map.get("userId");
		String companyId = map.get("companyId");
		int pageIndex = Integer.valueOf(map.get("pageIndex"));
		int pageLimit = Integer.valueOf(map.get("pageLimit"));
		pageIndex = (pageIndex-1)*pageLimit;
		List<Map<String,String>> tmpSubSctibe = new ArrayList<Map<String,String>>();
		Map<String,String> tmpSubSctibeMsg = null;
		/**  查询该用户的所有关注人  **/
		List<Map<String,String>> subScribers = getSubScribeUsersByUserId(userId);
		if(subScribers.size() == 0){
			return tmpSubSctibe;
		}
		Map<String,String> userFullNames = getUserFullNameByCompanyId(companyId);
		DB db = mongo.getDB("mongodbdata");
		DBCollection sequence = db.getCollection("auditlog");
		BasicDBObject query = new BasicDBObject();
		query.put("fileId", new BasicDBObject("$ne",""));
	    query.put("fileSubscribMsg", new BasicDBObject("$exists",true));
	    //xiewenda 要显示必须是关注人相同公司下的 动态消息
	    query.put("companyId", companyId);
		BasicDBList cond = new BasicDBList();
		
		//查询时间date 
        String finddate="";
        String finddate1="";
        String findtime="";
        Long findDateTime = 0L;
        Long findDateTime1 = 0L;
        if(map.get("findDateflg")!=null && map.get("findDateflg").equals("1")){//表示今天
          findDateTime=d.getTime();
          finddate = StringUtil.dateLongFormatString(findDateTime,"yyyy-MM-dd");
          //findtime = "00:00:00";
        }else if(map.get("findDateflg")!=null && map.get("findDateflg").equals("2")){//2表示一周
          findDateTime=d.getTime() - 7 * 24 * 60 * 60 * 1000;
          findDateTime1=d.getTime() - 6 * 24 * 60 * 60 * 1000;
          finddate = StringUtil.dateLongFormatString(findDateTime,"yyyy-MM-dd");
          finddate1 = StringUtil.dateLongFormatString(findDateTime1,"yyyy-MM-dd");
          findtime = StringUtil.dateLongFormatString(findDateTime,"HH:mm:ss");
        }
		//List<String> unUsedUsersLists = getUnUsedUsersLists(companyId);
		for(Map<String,String> tmpIn : subScribers){
//			if(!unUsedUsersLists.contains(tmpIn.get("subscribename"))){
		  String subscribedate = tmpIn.get("subscribedate");
          String subscribetime = tmpIn.get("subscribetime");
          String subscribename = tmpIn.get("subscribename");
          String dateTime=subscribedate+" "+subscribetime;
          //获取关注此人的时间
          Long time1 = StringUtil.dateStringFormatLong(dateTime, "yyyy-MM-dd HH:mm:ss");
          Long time2 =time1+24 * 60 * 60 * 1000;
          String subscribedate1= StringUtil.dateLongFormatString(time2,"yyyy-MM-dd");
          //如果查询的时间大于关注此人的时间(也就是说查询时间在关注此人时间之后)直接用查询时间   否则就从关注此人的时间开始查询动态信息
          //query.put("userid", tmpIn.get("subscribename"));
			if(map.get("findDateflg")!=null && findDateTime > time1){
			  if("1".equals(map.get("findDateflg"))){
			    cond.add(new BasicDBObject("userid", subscribename).append("log_date", finddate));
			  }else if("2".equals(map.get("findDateflg"))){
			    
			    cond.add(new BasicDBObject().append(QueryOperators.AND,  
	                  new BasicDBObject[] {new BasicDBObject("userid", subscribename),   
	                  new BasicDBObject(QueryOperators.OR, new BasicDBObject[]{new BasicDBObject("log_date", finddate).append("log_time", new BasicDBObject("$gte",findtime)),new BasicDBObject("log_date", new BasicDBObject("$gte",finddate1))})}));
			    /* mong 很灵活 上边也可以写成这样
			     * cond.add(new BasicDBObject().append(QueryOperators.AND,  
                            new BasicDBObject[] {new BasicDBObject("userid", subscribename),   
                            new BasicDBObject("log_date", finddate),new BasicDBObject("log_time", new BasicDBObject("$gte",findtime))}));
                  cond.add(new BasicDBObject().append(QueryOperators.AND,  
                            new BasicDBObject[] {new BasicDBObject("userid", subscribename),new BasicDBObject( "log_date",new BasicDBObject( "$gte", finddate1))}));*/
			  }
					
			}else{  
			 /**
			  *  xiewenda 20151215 修改我的关注查询逻辑
			  * 
			  * [ { "$and" : [ { "userid" : "106161@qq.com"} , { "$or" : [ { "log_date" : "2015-11-27" , "log_time" : { "$gte" : "10:09:42"}} , { "log_date" : { "$gte" : "2015-11-28"}}]}]} , 
			  *   { "$and" : [ { "userid" : "846201@qq.com"} , { "$or" : [ { "log_date" : "2015-12-03" , "log_time" : { "$gte" : "15:53:26"}} , { "log_date" : { "$gte" : "2015-12-04"}}]}]}
			  * ]
			  * 第一个and 查询从2015-11-27 10:09:42 开始  到现在的记录 (因为没有存时间戳 所以只能这样查)
			  * 类似于 and (userid="106161@qq.com" and ((log_date="2015-11-27" and log_time >= "10:09:42") or (log_date >= "2015-11-28"))) 
			  * 
			  */
			  
			  cond.add(new BasicDBObject().append(QueryOperators.AND,  
                  new BasicDBObject[] {new BasicDBObject("userid", subscribename),   
                  new BasicDBObject(QueryOperators.OR, new BasicDBObject[]{new BasicDBObject("log_date", subscribedate).append("log_time", new BasicDBObject("$gte",subscribetime)),new BasicDBObject("log_date", new BasicDBObject("$gte",subscribedate1))})}));
			}
//			}  
		}
		//System.out.println(cond.toString());
		if(cond.size()>0){
			query.put("$or", cond);
		}
		
	    
	    DBCursor cursor = null;
	    /** 分页 **/
		if (pageIndex == 0) {
			cursor = sequence.find(query).sort(new BasicDBObject("_id",-1)).limit(pageLimit);
		} else {
			cursor = sequence.find(query).sort(new BasicDBObject("_id",-1)).skip(pageIndex).limit(pageLimit);
		}
		//tmpSubSctibe
		Map<String,String> mapCount = new HashMap<String,String>();
		mapCount.put("size", cursor.count()+"");
		tmpSubSctibe.add(mapCount);
	    while(cursor.hasNext()){
	        	BasicDBObject obj = (BasicDBObject)cursor.next() ;
	        	tmpSubSctibeMsg = new HashMap<String, String>();
	        	tmpSubSctibeMsg.put("userid", obj.getString("userid"));
	        	tmpSubSctibeMsg.put("opreateTime", obj.getString("log_date")+" "+obj.getString("log_time"));
	        	tmpSubSctibeMsg.put("fileId", obj.getString("fileId"));
	        	tmpSubSctibeMsg.put("folderId", obj.getString("folderId"));
	        	tmpSubSctibeMsg.put("fileVersion", getFileVersionByCompanyId(companyId,obj.getString("fileId")));
	        	tmpSubSctibeMsg.put("userFullName",userFullNames.get(obj.getString("userid")));
	        	tmpSubSctibeMsg.put("portrait",userFullNames.get(userFullNames.get(obj.getString("userid")))!=null? userFullNames.get(userFullNames.get(obj.getString("userid"))):"");
	        	tmpSubSctibeMsg.put("fileSubscribMsg", obj.getString("fileSubscribMsg"));
	        	
	        	/** lujixiang 20150907 添加字段，用于满足关注信息中对分享文件的特殊显示要求    --start**/
	        	tmpSubSctibeMsg.put("actionEn", null == obj.getString("actionEn") ? "" : obj.getString("actionEn"));		// 分享
	        	tmpSubSctibeMsg.put("toUser", null == obj.getString("toUser") ? "" : obj.getString("toUser"));		// 被分享的用户名
	        	tmpSubSctibeMsg.put("fileName", null == obj.getString("fileName") ? "" : obj.getString("fileName"));	// 分享的文件名称
	        	tmpSubSctibeMsg.put("toUserFullName", null == obj.getString("toUserFullName") ? "" : obj.getString("toUserFullName"));	// 分享的文件名称
				/** lujixiang 20150907 添加字段，用于满足关注信息中对分享文件的特殊显示要求    --end**/
	        	
	        	tmpSubSctibe.add(tmpSubSctibeMsg);
	    }
        
        return tmpSubSctibe;
		
	}


	/**
	 * 添加关注人
	 * @see cn.flying.rest.onlinefile.files.driver.FilesDao#addSubScribeMsgByUserId(java.util.Map)
	 */
	public Map<String, String> addSubScribeMsgByUserId(
			Map<String, String> params) {

        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String[] chekedUsers = params.get("chekedUsers").split(",");
        String userId = params.get("userId");
        String sql = "insert into usersubscribe(userid,subscribename,subscribedate,subscribetime) values(?,?,?,?)";
        Map<String, String> result = new HashMap<String, String>();
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            for (String chekedUser : chekedUsers) {
                pst.setInt(1, userId== null ? 0 : Integer.valueOf(userId));
                pst.setString(2, chekedUser);
                pst.setString(3, dfDate.format(new Date()));
                pst.setString(4, dfTime.format(new Date()));
                pst.addBatch();
            }
           pst.executeBatch();
           conn.commit();
           result.put("success", "true");
           result.put("msg", "关注成功");
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("msg", "关注失败");
        } finally {
            JdbcUtil.close(pst, conn);
        }
    
		return result;
	}


	/**
	 * 删除关注人
	 * @see cn.flying.rest.onlinefile.files.driver.FilesDao#delSubScribeMsgByUserId(java.util.Map)
	 */
	public Map<String, String> delSubScribeMsgByUserId(
			Map<String, String> params) {
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String[] chekedUsers = params.get("chekedUsers").split(",");
        String userId = params.get("userId");
        String sql = "delete from usersubscribe where userid = ? and subscribename =? ";
        Map<String, String> result = new HashMap<String, String>();
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            for (String chekedUser : chekedUsers) {
            	pst.setInt(1, userId== null ? 0 : Integer.valueOf(userId));
                pst.setString(2, chekedUser);
                pst.addBatch();
            }
           pst.executeBatch();
           conn.commit();
           result.put("success", "true");
           result.put("msg", "取消关注成功");
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("msg", "取消关注失败");
        } finally {
            JdbcUtil.close(pst, conn);
        }
    
		return result;
	}

	/**
     * 通过用户ID查询用户关注的人
     * @param companyId
     * @return
     */
    public Map<String, Map<String,String>> getAllUsersByComPanyIdId(String companyId) {
        Map<String, Map<String,String>> list = new HashMap<String, Map<String,String>>();
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select ID, USERNAME, FULLNAME ,TELEOHONE,PORTRAIT,MOBILEPHONE,EMAIL,FAX,POSITION from users where id in (select userid from company_users where companyId=? and status = 1)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<String, String>();
                if(null != rs.getString("FULLNAME") && !rs.getString("FULLNAME").toString().equals("")){
                	map.put("id", rs.getString("ID"));
	                map.put("userName", rs.getString("USERNAME"));
	                map.put("fullName", rs.getString("FULLNAME"));
	                map.put("portRait", rs.getString("PORTRAIT"));
	                map.put("teleohone", rs.getString("TELEOHONE"));
	                map.put("mobilephone", rs.getString("MOBILEPHONE"));
	                map.put("email", rs.getString("EMAIL"));
	                map.put("fax", rs.getString("FAX"));
	                map.put("position", rs.getString("POSITION"));
	                list.put(rs.getString("USERNAME"), map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }

	public Map<String, Object> getSubScribersByUserId(Map<String, String> map) {
		String userId = map.get("userId");
		String userName = map.get("userName");
		String companyId = map.get("companyId");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String,String>> tmpUserLists = new ArrayList<Map<String,String>>();
		Map<String,String> tmpUserMaps = null;
		/** 获取到了公司所有成员   **/
		Map<String, Map<String,String>> allCompanyUsers = getAllUsersByComPanyIdId(companyId);
		allCompanyUsers.remove(userName);
		/** 获取已经关注的成员 **/
		List<Map<String,String>> subScribers = getSubScribeUsersByUserId(userId);
		
		List<String> unUsedUsersLists = getUnUsedUsersLists(companyId);
		/** 装载已经关注人信息 **/
		for(Map<String,String> subScriber:subScribers){
			if(null!= allCompanyUsers.get(subScriber.get("subscribename")) && !unUsedUsersLists.contains(subScriber.get("subscribename"))){
				/**  把每个用户信息放入到里面 **/
				tmpUserMaps = allCompanyUsers.get(subScriber.get("subscribename"));
				tmpUserLists.add(tmpUserMaps);
				allCompanyUsers.remove(subScriber.get("subscribename"));
			}else{
				allCompanyUsers.remove(subScriber.get("subscribename"));
			}
		}
		resultMap.put("subScribedUsers", tmpUserLists);
		
		tmpUserLists = new ArrayList<Map<String,String>>();
		/** 装载未关注人的信息  **/
		for(Entry<String, Map<String, String>> entry:allCompanyUsers.entrySet()){
			 tmpUserLists.add(entry.getValue());
		}  
		resultMap.put("subScriberAvaliable", tmpUserLists);
		
		return resultMap;
	}
	
	public List<Map<String,String>> getClassByIdseq(String idseq,String companyId){
		//{"id":folderId, "name":folderName, "idSeq":idSeq};

        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ID,FILENAME,IDSEQ FROM FILES_"+companyId+" WHERE ID IN (1, "+idseq.substring(2, idseq.length()-1).replace(".", ",")+") and ISFILE = '0' order by idseq asc ");
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String,String> map = new HashMap<String, String>();
                String id = rs.getString("ID");
                map.put("id", id);
                map.put("name", rs.getString("FILENAME"));
                String idSeq = rs.getString("IDSEQ");
                if (idSeq == null || "".equals(idSeq)) {
                    idSeq = "";
                } else {
                    idSeq += id+".";
                }
                map.put("idSeq", idSeq);
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
	}


	public Map<String, String> getFileInfoById(String companyId,String fileId) {

        Map<String,String> fileMap = new HashMap<String,String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            /**20151023 xiayongcai 增加查询字段*/
            sql.append("SELECT FILENAME,TYPE,OPENLEVEL,CLASSID,IDSEQ,isFile,islast,version,ISDELETE, CREATOR,OWNER, CREATETIME, MD5,SOLENUMBER,SERIALNUMBER FROM FILES_"+companyId+" WHERE ID = ? ");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, fileId);
            rs = pst.executeQuery();
            while (rs.next()) {
                fileMap.put("fileName", rs.getString("FILENAME"));
                fileMap.put("fileType", rs.getString("TYPE"));
                fileMap.put("openLevel", rs.getString("OPENLEVEL"));
                fileMap.put("folderId", rs.getString("CLASSID"));
                fileMap.put("fileIdSeq", rs.getString("IDSEQ"));
                fileMap.put("isLast", rs.getString("islast"));
                fileMap.put("isFile", rs.getString("isFile"));
                fileMap.put("version", rs.getString("version"));
                fileMap.put("isDelete", rs.getString("ISDELETE"));
                fileMap.put("id", fileId);
                fileMap.put("creator", rs.getString("CREATOR"));
                fileMap.put("owner", rs.getString("OWNER"));
                fileMap.put("createTime", rs.getString("CREATETIME"));
                fileMap.put("idseq", rs.getString("IDSEQ"));
                fileMap.put("md5", rs.getString("MD5"));
                //SOLENUMBER,SERIALNUMBER
                fileMap.put("classId", rs.getString("CLASSID"));
                fileMap.put("serialNumber", rs.getString("SERIALNUMBER"));
                fileMap.put("soleNumber", rs.getString("SOLENUMBER"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return fileMap;
    
	}


	public int getSubScribeMsgCount(
			Map<String, String> map) {
		//20151112 xiayongcai 查询个人关注信息,默认为全部,2天时间,一周时间(一周为7天)
		//查询时间date
		Date d=new Date();   
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");   
		SimpleDateFormat df1=new SimpleDateFormat("HH:mm:ss");   
		//查询时间date 
		String subscribedate="";
		String subscribetime=df1.format(d.getTime());
		if(map.get("findDateflg")!=null && map.get("findDateflg").equals("1")){//1表示一天（一天就指代今天）
			subscribedate=df.format(d.getTime());
		}else if(map.get("findDateflg")!=null && map.get("findDateflg").equals("2")){//2表示一周
			subscribedate=df.format(new Date(d.getTime() - 7 * 24 * 60 * 60 * 1000));
		}
		
		String userId = map.get("userId");
		String companyId = map.get("companyId");
		
		DB db = mongo.getDB("mongodbdata");
		DBCollection sequence = db.getCollection("auditlog");
		BasicDBObject query = new BasicDBObject();
//	    query.put("userid", new BasicDBObject("$in",values));
	    query.put("fileId", new BasicDBObject("$ne",""));
	    query.put("fileSubscribMsg", new BasicDBObject("$exists",true));
	    query.put("companyId", companyId);
		/**  查询该用户的所有关注人  **/
		List<Map<String,String>> subScribers = getSubScribeUsersByUserId(userId);
		
		if(subScribers.size() == 0){
			return 1;
		}
		
		BasicDBList values = new BasicDBList();
		List<String> unUsedUsersLists = getUnUsedUsersLists(companyId);
		for(Map<String,String> tmpIn : subScribers){
			if(!unUsedUsersLists.contains(tmpIn.get("subscribename"))){
				if(!subscribedate.equals("")){
					//不等于空的情况下，要判断时间大小选着值
					String newDateTime=subscribedate+" "+tmpIn.get("subscribetime");
					String dateTime=tmpIn.get("subscribedate")+" "+tmpIn.get("subscribetime");
					//try {
					//Date d1 = df.parse(newDateTime);
					//Date d2 = df.parse(dateTime);
					java.util.Calendar c1 = java.util.Calendar.getInstance();
					java.util.Calendar c2 = java.util.Calendar.getInstance();
					try {
						c1.setTime(df.parse(newDateTime));
						c2.setTime(df.parse(dateTime));
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
				int result = c1.compareTo(c2);
					if (result > 0){
						values.add(new BasicDBObject().append(QueryOperators.AND,  
			                new BasicDBObject[] { new BasicDBObject("userid", tmpIn.get("subscribename")),  
		                    new BasicDBObject("log_date", new BasicDBObject("$gte",subscribedate)),new BasicDBObject("log_time", new BasicDBObject("$gte",tmpIn.get("subscribetime")))}));
					}else if (result <= 0){
						values.add(new BasicDBObject().append(QueryOperators.AND,  
								//new BasicDBObject[] { new BasicDBObject("userid", tmpIn.get("subscribename")), new BasicDBObject("companyId", companyId), 
								new BasicDBObject[] { new BasicDBObject("userid", tmpIn.get("subscribename")),
								new BasicDBObject("log_date", new BasicDBObject("$gte",tmpIn.get("subscribedate"))),new BasicDBObject("log_time", new BasicDBObject("$gte",tmpIn.get("subscribetime")))}));
					}
				}else{
					values.add(new BasicDBObject().append(QueryOperators.AND,  
			                new BasicDBObject[] { new BasicDBObject("userid", tmpIn.get("subscribename")),  
		                    new BasicDBObject("log_date", new BasicDBObject("$gte",tmpIn.get("subscribedate"))),new BasicDBObject("log_time", new BasicDBObject("$gte",tmpIn.get("subscribetime")))}));
				}
				
			}
		}
		if(values.size()>0){
			query.put("$or", values);
		}
		
    	
	    DBCursor cursor = sequence.find(query);
	    
        return cursor.count();
    
		
	}
	
	/**
     * 取消分享
     * @param companyId
     * @param fileId
     * @param userId
     * @return
     */
    public boolean unshareFile(String companyId, String fileId, String userId,int maxVersion, String isExist, String unshareType, String newFileName) {
        boolean flag = false;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            String tableName = "files_" + companyId;
            //以前设置私密的修改业务
            //sql.append("update ").append(tableName).append(" set classid=0, openlevel='3', idseq=''");
            //当前的业务
            sql.append("update ").append(tableName).append(" set openlevel='3' ");
            if("true".equals(isExist) && "0".equals(unshareType)){  //更新版本号
            	sql.append(",version = version+").append(maxVersion);
            }else if("true".equals(isExist) && "1".equals(unshareType)){  //重命名
            	sql.append(",filename = ?");
            }
            sql.append(" where id=? and owner=?");
            pst = conn.prepareStatement(sql.toString());
            int i = 1;
            if("true".equals(isExist) && "1".equals(unshareType)){
            	pst.setString(i++, newFileName.substring(0, newFileName.lastIndexOf(".")));
            }
            pst.setString(i++, fileId);
            pst.setString(i++, userId);
            int num = pst.executeUpdate();
            if (num == 1) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
    }
    
    public Map<String, String> getFileById(String companyId, String classId,
    		String fileId){
    	Map<String, String> rtMap = new HashMap<String, String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_" + companyId;
            String sql ="select FILENAME,TYPE,CLASSID from "+tableName+" where ID=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, fileId);
            rs = pst.executeQuery();
            if(rs.next()){
            	rtMap.put("filename", rs.getString(1));
            	rtMap.put("type", rs.getString(2));
            	rtMap.put("classid", rs.getString(3)); //wangwenshuo 20151022 添加字段  用于复制功能
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs,pst, conn);
        }
        return rtMap;
    	
    }
    /**
     * @param userId 用户ID
     * @param pageIndex 起始查询值
     * @param pageLimit 查询分页
     * @param keyWord 关键词
	 * 通过用户ID查询出所有的用户的订约人的信息
	 */
	public List<Map<String,String>> searchSubScribeMsgByUserId(Map<String,String> map) {
		String userId = map.get("userId");
		String companyId = map.get("companyId");
		int pageIndex = Integer.valueOf(map.get("pageIndex"));
		int pageLimit = Integer.valueOf(map.get("pageLimit"));
		String keyWord = map.get("keyWord");
		pageIndex = (pageIndex-1)*pageLimit;
		List<Map<String,String>> tmpSubSctibe = new ArrayList<Map<String,String>>();
		Map<String,String> tmpSubSctibeMsg = null;
		Map<String,String> userFullNames = getUserFullNameByCompanyId(companyId);
		/**  查询该用户的所有关注人  **/
		List<Map<String,String>> subScribers = getSubScribeUsersByUserId(userId);
		
		if(subScribers.size() == 0 ){
			return tmpSubSctibe;
		}
		
		DB db = mongo.getDB("mongodbdata");
		DBCollection sequence = db.getCollection("auditlog");
		BasicDBObject query = new BasicDBObject();
		query.put("fileId", new BasicDBObject("$ne",""));
	    query.put("fileSubscribMsg", new BasicDBObject("$exists",true));
//		BasicDBList values_Search = new BasicDBList();
		Pattern pattern = Pattern.compile("^" + keyWord + "|" + keyWord);
		
		BasicDBList values = new BasicDBList();
		
		List<String> unUsedUsersLists = getUnUsedUsersLists(companyId);

		for(Map<String,String> tmpIn : subScribers){
			if(!unUsedUsersLists.contains(tmpIn.get("subscribename"))){
				BasicDBList conAnd = new BasicDBList();
				BasicDBObject queryAdd = new BasicDBObject();
				conAnd.add(new BasicDBObject("log_date", new BasicDBObject("$gte", tmpIn.get("subscribedate"))));
				conAnd.add(new BasicDBObject("log_time", new BasicDBObject("$gte", tmpIn.get("subscribetime"))));
				conAnd.add(new BasicDBObject("fileSubscribMsg", pattern));
				conAnd.add(new BasicDBObject("userid", tmpIn.get("subscribename")));
				conAnd.add(new BasicDBObject("companyId", companyId));
				queryAdd.put("$and", conAnd);
				values.add(queryAdd) ;
			}
		}
		if(values.size()>0){
			query.put("$or", values);
		}
	    DBCursor cursor = null;
	    /** 分页 **/
		if (pageIndex == 0) {
			cursor = sequence.find(query).sort(new BasicDBObject("_id",-1)).limit(pageLimit);
		} else {
			cursor = sequence.find(query).sort(new BasicDBObject("_id",-1)).skip(pageIndex).limit(pageLimit);
		}
		Map<String,String> count = new HashMap<String,String>();
		count.put("count", cursor.count()+"");
		tmpSubSctibe.add(count);
	    while(cursor.hasNext()){
	        	BasicDBObject obj = (BasicDBObject)cursor.next() ;
	        	tmpSubSctibeMsg = new HashMap<String, String>();
	        	tmpSubSctibeMsg.put("userid", obj.getString("userid"));
	        	tmpSubSctibeMsg.put("opreateTime", obj.getString("log_date")+" "+obj.getString("log_time"));
	        	tmpSubSctibeMsg.put("fileId", obj.getString("fileId"));
	        	tmpSubSctibeMsg.put("userFullName",userFullNames.get(obj.getString("userid")));
	        	tmpSubSctibeMsg.put("fileVersion", getFileVersionByCompanyId(companyId,obj.getString("fileId")));
	        	tmpSubSctibeMsg.put("fileSubscribMsg", obj.getString("fileSubscribMsg"));
	        	tmpSubSctibe.add(tmpSubSctibeMsg);
	    }
	    
        return tmpSubSctibe;
		
	}


	public int searchSubScribeMsgByUserIdCount(Map<String, String> map) {
		String userId = map.get("userId");
		String keyWord = map.get("keyWord");
		String companyId = map.get("companyId");
		
		DB db = mongo.getDB("mongodbdata");
		DBCollection sequence = db.getCollection("auditlog");
		BasicDBObject query = new BasicDBObject();
		/**  查询该用户的所有关注人  **/
		List<Map<String,String>> subScribers = getSubScribeUsersByUserId(userId);
		
		if(subScribers.size() == 0){
			return 1;
		}
		
		query.put("fileId", new BasicDBObject("$ne",""));
		query.put("fileSubscribMsg", new BasicDBObject("$exists",true));
		Pattern pattern = Pattern.compile("^" + keyWord + "|" + keyWord);
		BasicDBList values = new BasicDBList();
		List<String> unUsedUsersLists = getUnUsedUsersLists(companyId);
		for(Map<String,String> tmpIn : subScribers){
			if(!unUsedUsersLists.contains(tmpIn.get("subscribename"))){
				BasicDBList conAnd = new BasicDBList();
				BasicDBList conOr = new BasicDBList();
				BasicDBObject queryAdd = new BasicDBObject();
				BasicDBObject queryOr = new BasicDBObject();
				conAnd.add(new BasicDBObject("log_date", new BasicDBObject("$gte", tmpIn.get("subscribedate"))));
				conAnd.add(new BasicDBObject("log_time", new BasicDBObject("$gte", tmpIn.get("subscribetime"))));
				conOr.add(new BasicDBObject("fileSubscribMsg", pattern));
				conOr.add(new BasicDBObject("userid", pattern));
				queryOr.put("$or", conOr);
				conAnd.add(queryOr);
				queryAdd.put("$and", conAnd) ;
				values.add(queryAdd) ;
			}
		}
		if(values.size()>0){
			query.put("$or", values);
		}
	    DBCursor cursor = sequence.find(query);
        return cursor.count();
		
	}


	public Map<String, String> addSubScribeMsgByDocs(Map<String, String> params) {

        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        String chekedUser = params.get("chekedUsers");
        String userId = params.get("userId");
        String sql = "insert into usersubscribe(userid,subscribename,subscribedate,subscribetime) values(?,?,?,?)";
        Map<String, String> result = new HashMap<String, String>();
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        int count = 0;
        try {
            session = getSession();
            conn = session.connection();
            
            /**  优先检测是否存在该关注人信息  **/
            String sql_select = "select count(*) from usersubscribe where userid = ? and subscribename = ?";
            pst = conn.prepareStatement(sql_select);
            pst.setInt(1, userId== null ? 0 : Integer.valueOf(userId));
            pst.setString(2, chekedUser);
            rs = pst.executeQuery();
            while(rs.next()){
            	count = rs.getInt(1);
            }
            
            if(count > 0){
            	result.put("success", "false");
                result.put("msg", "关注失败,您已经关注了该用户");
                return result;
            }
            
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setString(2, chekedUser);
            pst.setString(3, dfDate.format(new Date()));
            pst.setString(4, dfTime.format(new Date()));
            pst.executeUpdate();
            conn.commit();
            result.put("success", "true");
            result.put("msg", "关注成功");
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", "false");
            result.put("msg", "关注失败");
        } finally {
            JdbcUtil.close(pst, conn);
        }
    
		return result;
	}
	
	/**
     * 判断是否关注了某个用户
     * @param userid  当前用户
     * @param rssUserName  被关注人username
     * @return
     */
    public boolean hasSubScribeUser(String userId, String rssUserName) {
        boolean flag = false;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select count(id) from usersubscribe where userid=? and subscribename=? ";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, userId== null ? 0 : Integer.valueOf(userId));
            pst.setString(2, rssUserName);
            rs = pst.executeQuery();
            if (rs.next()) {
                if(rs.getInt(1) > 0) {
                    flag = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return flag;
    }
	
	/**
     * 根据用户名获取id
     * @param companyId
     * @param userName
     * @return
     */
    public String getUserIdWithName(String companyId, String userName) {
        String userId = "";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select id from users where enabled=1 and companyid=? and username=? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            pst.setString(2, userName);
            rs = pst.executeQuery();
            if (rs.next()) {
                userId = rs.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return userId;
    }
    
    /**
     * 获取分组信息
     * @param companyId
     * @param Flag
     * @return
     */
    public Map<String,String> getGroupInfoByFlag(String companyId, String Flag) {
    	Map<String,String> map = new HashMap<String,String>();
        String groupName = "";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select id,groupname,classid from groups where companyid=? and flag=? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            pst.setString(2, Flag);
            rs = pst.executeQuery();
            if (rs.next()) {
            	map.put("id", rs.getString("id"));
            	map.put("groupName", rs.getString("groupname"));
            	map.put("classId", rs.getString("classid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return map;
    }
    
    /**
     * 查询idseq的中文名称
     * @param idSeq
     * @return
     */
    public Map<String, String> getFolderPathNameWithIdSeq(String companyId, String idSeq) {
        Map<String, String> map = new HashMap<String, String>();
        idSeq = idSeq.substring(0, idSeq.length());
        List<String> stringList = Arrays.asList(idSeq.split("\\."));
        String ids = StringUtil.list2String(stringList, ",");
        if ("".equals(ids)) {
            return map;
        }
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select id, filename from files_"+companyId+" where id in("+ids+") ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("id"), rs.getString("filename"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return map;
    }
    
    public Boolean checkFileDelete(String fileId,String companyId){
         Session session = null ;
         Connection conn = null ;
         PreparedStatement pst = null ;
         ResultSet rs = null ;
         String isdel = "1";
         try {
             session = getSession();
             conn = session.connection();
             String sql = "SELECT ISDELETE FROM FILES_"+companyId+" WHERE ID=? ";
             pst = conn.prepareStatement(sql);
             pst.setInt(1, Integer.parseInt(fileId));
             rs = pst.executeQuery();
             if (rs.next()) {
            	 isdel = rs.getString("ISDELETE");
             }
         } catch (SQLException e) {
             e.printStackTrace();
             return false;
         } finally {
             JdbcUtil.close(rs, pst, conn);
         }
         return "1".equals(isdel);
    }
    
    /**
     * 文件分享到某个路径下
     * @param companyId
     * @param fileId
     * @param folderId
     * @param idSeq 文件的idseq
     * @param openlevel
     * @return
     */
    public boolean shareToFolderPath(String companyId, String fileId, String folderId, String idSeq, String openlevel) {
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        int num = 0;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "update files_"+companyId+" set classid=?, idseq=?, openlevel=? where id=? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, folderId);
            pst.setString(2, idSeq);
            pst.setString(3, openlevel);
            pst.setString(4, fileId);
            num = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return num > 0 ? true : false;
    }
    
    /**
     *  wangwenshuo 20151020 复制到 
     * @param companyId
     * @param fileId
     * @param folderId
     * @param idSeq 文件的idseq
     * @param openlevel
     * @return
     */
    public boolean copyToFolderPath(String companyId, List<Map<String, String>> files, String fileName, String folderId, String idSeq, String openlevel) {
    	Session session = null ;
    	Connection conn = null ;
    	PreparedStatement pst = null ;
    	ResultSet rs = null ;
    	int num = 0;
    	try {
    		session = getOpenSession();
    		conn = session.connection();
    		conn.setAutoCommit(false);
    		String sql = "Insert into files_"+companyId 
    				+ "(CLASSID, FILENAME, CREATOR, OWNER, CREATETIME, SIZE, MD5, TYPE, PRAISECOUNT,"+
    				"COLLECTCOUNT, ISDELETE, FILEID, IDSEQ, ISFILE, VERSION, ISLAST, OPENLEVEL)"+
    			  " select ?, ?, CREATOR, OWNER, CREATETIME, SIZE, MD5, TYPE, PRAISECOUNT, "+
    				"COLLECTCOUNT, ISDELETE, FILEID, ?, ISFILE, VERSION, ISLAST, ? from files_"+companyId+" where id=?";
    		pst = conn.prepareStatement(sql);
    		for(Map<String,String> file:files){
    			pst.setString(1, folderId);
    			pst.setString(2, fileName);
    			pst.setString(3, idSeq);
    			pst.setString(4, openlevel);
    			pst.setString(5, file.get("id"));
    			pst.addBatch();
    		}
    		num = pst.executeBatch().length;
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
    			e.printStackTrace();
    		}
    		JdbcUtil.close(null,pst, conn,session);
    	}
    	return num > 0 ? true : false;
    }
    
    /**
     *  wangwenshuo 20151117 我的文档文件分享到分类dao
     * @param file Map
     * @return
     */
    public int fileShareToClass(String fromCompanyId, String toCompanyId, Map<String, String> file, String fileName, String folderId, String idSeq, String openlevel, String userId) {
    	Session session = null ;
    	Connection conn = null ;
    	PreparedStatement pst = null ;
    	ResultSet rs = null ;
    	int rstId = 0;
    	try {
    		String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    		//xyc 添加更新字段，唯一编号，序号编号
    		Map<String, String> newFileMap=this.getFileHighestVersionMap("files_"+toCompanyId,folderId,fileName, file.get("type"),createTime);
    		
    		session = getOpenSession();
    		conn = session.connection();
    		conn.setAutoCommit(false);
    		String sql = "Insert into files_"+toCompanyId 
    				+ "(CLASSID, FILENAME, CREATOR, OWNER, CREATETIME, SIZE, MD5, TYPE, PRAISECOUNT,"+
    				"COLLECTCOUNT, ISDELETE, FILEID, IDSEQ, ISFILE, VERSION, ISLAST, OPENLEVEL,UPDATETIME,SOLENUMBER,SERIALNUMBER)"+
    			  " select ?, ?, ?, ?, ?, SIZE, MD5, TYPE, PRAISECOUNT, "+
    				"COLLECTCOUNT, ISDELETE, FILEID, ?, ISFILE, ?, 1, ?,?,?,"+"LPAD("+newFileMap.get("SERIALNUMBER")+",4,0)"+" from files_"+fromCompanyId+" where id=?";
    		pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, folderId);
			pst.setString(2, fileName);
			pst.setString(3, userId);
			pst.setString(4, userId);
			pst.setString(5, createTime);
			pst.setString(6, idSeq);
			pst.setString(7, newFileMap.get("VERSION"));//file.get("version")==null?"1":file.get("version")
			pst.setString(8, openlevel);
			pst.setString(9, newFileMap.get("UPDATETIME"));
			pst.setString(10,newFileMap.get("SOLENUMBER"));
			pst.setString(11,file.get("id"));
    		rstId = pst.executeUpdate();
    		rs = pst.getGeneratedKeys();
            if(rs.next()){
            	rstId = rs.getInt(1);
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
    			e.printStackTrace();
    		}
    		JdbcUtil.close(null,pst, conn,session);
    	}
    	return rstId;
    }
    
    /**
     *  wangwenshuo 20151117 我的文档文件分享到分类dao
     * @param file Map
     * @return
     */
    public boolean fileShareToClassLog(String fromCompanyId, String toCompanyId, Map<String, String> file, String fileName, String folderId, String idSeq, String openlevel) {
    	Session session = null ;
    	Connection conn = null ;
    	PreparedStatement pst = null ;
    	ResultSet rs = null ;
    	int rstId = 0;
    	try {
    		session = getOpenSession();
    		conn = session.connection();
    		String sql = "INSERT INTO files_share_log_"+fromCompanyId+
    				" (FILEID, FILENAME, TYPE, COMPANYID, CLASSID, VERSION, OPENLEVEL, CREATETIME)"
    				+ " VALUES (?,?,?,?,?,?,?,?)";
    		pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, file.get("id"));
			pst.setString(2, file.get("filename"));
			pst.setString(3, file.get("type"));
			pst.setString(4, toCompanyId);
			pst.setString(5, folderId);
			pst.setString(6, file.get("version")==null?"1":file.get("version"));
			pst.setString(7, openlevel);
			pst.setString(8, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    		rstId = pst.executeUpdate();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		JdbcUtil.close(null,pst, conn,session);
    	}
    	return rstId>0;
    }
    
    /**
     * 获取分类群组的FLAG
     * @param classId  分类群组id
     * @return
     */
    public  Map<String, String> getFlagWithClassId(String companyId, String classId) {
    	Map<String, String> retMap=new HashMap<String, String>();
        String FLAG = "";
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
                //FLAG = rs.getString("FLAG");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return retMap;
        //return FLAG;
    }
    
    /**
     * 将文件添加到回收站
     * @param fileId
     * @return
     */
    public boolean addFileTrashById(String companyId, String fileId, String userId, String pathName) {
        boolean FLAG = true;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        String filesTable = "files_" + companyId;
        String trashTable = "files_trash_" + companyId;
        try {
            session = getSession();
            conn = session.connection();
        //    String sql = "insert into " + trashTable + "(fromid,filename,type,creator,idseq,isfile) select id as fromid,filename,type,creator,idseq,isfile from " + filesTable + " where id=?";
            String sql = "insert into " + trashTable + "(fromid,filename,type,creator,owner,idseq,isfile,deleteuserid,openlevel) select id as fromid,filename,type,creator,owner,idseq,isfile,id as deleteuserid,openlevel from " + filesTable + " where id=?";
            pst = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, fileId);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            int resultId = 0;
            if(rs.next()){
                resultId = rs.getInt(1);
            }
            if (resultId > 0) {
                pst.close();
                rs.close();
                sql = "update " + trashTable + " set deleteuserid=?, deletetime=?,pathname=? where id=?";
                String deleteTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
                pst = conn.prepareStatement(sql);
                pst.setString(1, userId);
                pst.setString(2, deleteTime);
                pst.setString(3, pathName);
                pst.setInt(4, resultId);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            FLAG = false;
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return FLAG;
    }
    
    /**
     * 获取回收站文件列表
     * @param companyId 公司ID
     * @param userId 用户id
     * @param start
     * @param limit
     * @return
     */
    public List<Map<String,String>> getTrashFileList(String companyId, String userId, int start, int limit,String orderType,String orderField,String keyWord) {
        /** orderField
         * 几种排序
         * 1.默认排序
         * 2.文件名排序
         * 4.原位置排序
         * 5.删除日期排序
         * 6.公开级别排序
         */
        /**
         * orderType
         * 排序类型
         * 升序 降序
         * 默认降序
         */
        if(orderType==null || orderType.trim().length()==0){
            orderType = " desc ";
        }
        if(orderField==null || orderField.trim().length()==0){
            orderField = "1";
        }
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            
            StringBuilder whereSb = new StringBuilder();
            whereSb.append(" WHERE t.isDelete='0' and t.DELETEUSERID="+userId+" ");
			if (keyWord != null && "".equals("")) {
				whereSb.append("and (");
				whereSb.append("t.FILENAME LIKE '%" + keyWord + "%'");
				whereSb.append("or t.DELETETIME LIKE '%" + keyWord + "%'");
				whereSb.append("or t.PATHNAME LIKE '%" + keyWord + "%'");
				whereSb.append(") ");
			}
			whereSb.append("and t.idseq not in('1.')");
            
			//source 标识数据来源  c是公司  u是用户我的文档
			/** 1.查询公司删除文档 */
			StringBuilder sqlC = new StringBuilder();
			sqlC.append("SELECT ")
				.append(" t.id trashId, t.isFile, t.idSeq, t.pathName, t.fileName, t.type, t.openLevel, t.deleteTime, t.creator, t.owner,t.deleteUserid,")
				.append(" f.version, f.id, f.classId, f.fileid,")
				.append(" '' as myDocument ")
				.append(" FROM files_trash_"+companyId+" t ")
				.append(" LEFT JOIN files_"+companyId+" f ON t.fromId = f.id ")
				.append(whereSb)
				.append(" UNION ALL ");
			
			/** 2. 查询我的文档删除文档 */
			//LEFT(t.pathName,5)  说明：我的文档根节点两条数据idseq错误，导致之后的pathName多了“/我的文档 ” 在此删除  
			StringBuilder sqlU = new StringBuilder();
			sqlU.append("SELECT ")
				.append(" t.id trashId, t.isFile, t.idSeq, t.pathName, t.fileName, t.type, '' as openLevel, t.deleteTime, t.creator, t.owner,t.deleteUserid,")
				.append(" f.version, f.id, f.classId, f.fileid,")
				.append(" 'myDocument' as myDocument ")
				.append(" FROM files_trash_user_"+userId+" t ")
				.append(" LEFT JOIN files_user_"+userId+" f ON t.fromId = f.id ")
				.append(whereSb);
			
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT * FROM (");
            if(!"-1".equals(companyId)){ //-1代表用户没有企业  只有个人空间数据
            	sql.append(sqlC);
            }
            sql.append(sqlU)
               .append(")")
               .append(" as cu")
               .append(" ORDER BY cu.isFile ASC ");
            
            if("2".equals(orderField)){
                sql.append(", convert(filename using gb2312) "+orderType+" ");
            }else if("4".equals(orderField)){
            	sql.append(", convert(pathname using gb2312) "+orderType+" ");
            }else if("6".equals(orderField)){
            	sql.append(", openlevel "+orderType+" ");
            }
            
            if("5".equals(orderField)){
            	sql.append(", deletetime "+orderType+" ");
            }else{
            	sql.append(", deletetime DESC");
            }
            sql.append(" LIMIT ?, ? ");
            
            pst = conn.prepareStatement(sql.toString());
            pst.setInt(1, start);
            pst.setInt(2, limit);
            rs = pst.executeQuery();
            while (rs.next()) {
//            	id isFile classId trashId fileName idSeq fileId ileName type  
//            	userId owner version pathName deleteTime openLevel
            	Map<String,String> m = new HashMap<String, String>();
            	m.put("id", rs.getString("id"));
            	m.put("isFile", rs.getString("isFile"));
            	m.put("classId", rs.getString("classId"));
            	m.put("trashId", rs.getString("trashId"));
            	m.put("fileName", rs.getString("fileName"));
            	m.put("idSeq", rs.getString("idSeq"));
            	m.put("fileId", rs.getString("fileId"));
            	m.put("fileName", rs.getString("fileName"));
            	m.put("type", rs.getString("type"));
            	m.put("userId", rs.getString("creator"));
            	m.put("owner", rs.getString("owner"));
            	m.put("version", rs.getString("version"));
            	m.put("pathName", rs.getString("pathName"));
            	m.put("deleteTime", rs.getString("deleteTime"));
            	m.put("openLevel", rs.getString("openLevel"));
            	m.put("myDocument", rs.getString("myDocument"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    /**
     * 获取回收站文件列表总count
     * @param companyId
     * @param userId
     * @return
     */
    public int getTrashFileListCount(String companyId, String userId,String keyWord) {
        int count = 0;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            
            StringBuilder whereSb = new StringBuilder();
            whereSb.append(" WHERE t.isDelete='0' and t.DELETEUSERID="+userId+" ");
			if (keyWord != null && "".equals("")) {
				whereSb.append("and (");
				whereSb.append("t.FILENAME LIKE '%" + keyWord + "%'");
				whereSb.append("or t.DELETETIME LIKE '%" + keyWord + "%'");
				whereSb.append("or t.PATHNAME LIKE '%" + keyWord + "%'");
				whereSb.append(") ");
			}
			whereSb.append("and t.idseq not in('1.')");
            
			//source 标识数据来源  c是公司  u是用户我的文档
			/** 1.查询公司删除文档 */
			StringBuilder sqlC = new StringBuilder();
			sqlC.append("SELECT ")
				.append(" t.id trashId, t.fromId, 'c' as source ")
				.append(" FROM files_trash_"+companyId+" t ")
				.append(whereSb)
				.append(" UNION ALL ");
			
			/** 2. 查询我的文档删除文档 */
			//LEFT(t.pathName,5)  说明：我的文档根节点两条数据idseq错误，导致之后的pathName多了“/我的文档 ” 在此删除  
			StringBuilder sqlU = new StringBuilder();
			sqlU.append("SELECT ")
				.append(" t.id trashId, t.fromId, 'u' as source ")
				.append(" FROM files_trash_user_"+userId+" t ")
				.append(whereSb);
			
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT count(*) FROM (");
            if(!"-1".equals(companyId)){ //-1代表用户没有企业  只有个人空间数据
            	sql.append(sqlC);
            }
            sql.append(sqlU).append(")").append(" as cu");
            
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return count;
    }
    
    /**
     * 回收站彻底删除文件 就是把isDelete改为1
     * @param companyId
     * @param fileId
     * @return
     */
    public boolean fileDestroy(String companyId, String fileId, String userId) {
        boolean flag = true;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
       
        try {
            String destroyTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
            session = getSession();
            conn = session.connection();
            conn.setAutoCommit(false);
            String tableName1 = "files_trash_"+companyId;
           // String tableName2 = "files_"+companyId;
            StringBuffer sql1 = new StringBuffer(); 
           // StringBuffer sql2 = new StringBuffer(); 
            sql1.append("update ").append(tableName1).append(" set isdelete='1',destroytime=?,destroyuserid=? WHERE fromid in (").append(fileId).append(")");
           // sql2.append("update ").append(tableName2).append(" set classId=-1, idseq='' WHERE id in ("+fileId+") ");
            pst = conn.prepareStatement(sql1.toString());
            pst.setString(1, destroyTime);
            pst.setString(2, userId);
            pst.executeUpdate();
           // pst.addBatch();
          //  pst.addBatch(sql2.toString());
          //  pst.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            flag = false;
            try {
              conn.rollback();
            } catch (SQLException e1) {
              e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
    }
    
    /**
     * 清空回收站
     * @param companyId
     * @return
     */
    public boolean cleanRecycleBin(String companyId, String userId) {
    	boolean flag = true;
    	Session session = null ;
    	Connection conn = null ;
    	PreparedStatement pst = null ;
    	PreparedStatement pst1 = null ;
    	try {
    		String destroyTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
    		session = getSession();
    		conn = session.connection();
    		conn.setAutoCommit(false);
    		
    		/** 1.删除公司分类下回收站的内容 */
    		String tableName1 = "files_trash_"+companyId;
    		String tableName2 = "files_"+companyId;
    		StringBuffer sql1 = new StringBuffer(); 
    		//StringBuffer sql2 = new StringBuffer(); 
    		sql1.append("update ").append(tableName1).append(" set isdelete='1',destroytime=?,destroyuserid=? where deleteuserid=?");
    		//sql2.append("update ").append(tableName2).append(" set classId=-1, idseq='' WHERE id in (select FROMID from "+tableName1+" where ISDELETE = '0' and owner="+userId+" ) ");
    		pst = conn.prepareStatement(sql1.toString());
    		pst.setString(1, destroyTime);
    		pst.setString(2, userId);
    		pst.setString(3, userId);
    		pst.executeUpdate();
    		//pst.addBatch(sql2.toString());
    		//pst.addBatch();
    	   // pst.executeBatch();
    	    
    	    //JdbcUtil.close(pst);
    	    /** 2.删除我的文档下下回收站的内容 */
    	    tableName1 = "files_trash_user_"+userId;
    		tableName2 = "files_user_"+userId;
    		sql1 = new StringBuffer(); 
    		//sql2 = new StringBuffer(); 
    		sql1.append("update ").append(tableName1).append(" set isdelete='1',destroytime=?,destroyuserid=?");
    		//sql2.append("update ").append(tableName2).append(" set classId=-1, idseq='' WHERE ISDELETE = '1'");
    		pst1 = conn.prepareStatement(sql1.toString());
    		pst1.setString(1, destroyTime);
    		pst1.setString(2, userId);
    		pst1.executeUpdate();
    		//pst.addBatch(sql2.toString());
    		//pst.addBatch();
    	   // pst.executeBatch();
    		conn.commit();
    	} catch (SQLException e) {
    		flag = false;
    		try {
                  conn.rollback();
                } catch (SQLException e1) {
                  e1.printStackTrace();
                }
    		e.printStackTrace();
    	} finally {
    		JdbcUtil.close(pst, conn);
    		JdbcUtil.close(pst1, conn);
    	}
    	return flag;
    }
    
    /**
     * 删除回收站表的数据
     * @param companyId
     * @param fileId
     * @return
     */
    public boolean deleteTrashFile(String companyId, String fileId) {
        boolean flag = true;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_trash_"+companyId;
            StringBuffer sql = new StringBuffer(); 
            sql.append("delete from ").append(tableName).append(" WHERE fromid=?");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, fileId);
            pst.executeUpdate();
        } catch (SQLException e) {
            flag = false;
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
    }
    
    /**
     * 回收站恢复文件
     * 就是把isDelete改为0
     * @param companyId
     * @param idSeq
     * @return
     */
    public Map<String, String> fileRestore(String trashIds, String companyId, String userId) {
    	Map<String, String> map = new HashMap<String, String>();
        boolean flag = true;
        Session session = null ;
        Connection conn = null ;
        CallableStatement cst = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_"+companyId;
            /**
             *  trashIds : 回收站文件id集合，以逗号分割
			 *	companyId ： 公司id
			 *	userId : 用户id
			 *	indexIds ： 新建的文件夹（需要建立索引，以逗号分割）
			 *	failIds ： 返回失败的回收站文件id集合（以逗号分割）
             */
            cst = conn.prepareCall("{ call proc_batch_restore_file_folder(?,?,?,?,?,?) }");
            cst.setString(1, trashIds);
            cst.setString(2, companyId);
            cst.setString(3, userId);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.registerOutParameter(6, java.sql.Types.VARCHAR);
            cst.execute();
            map.put("indexIds", cst.getString(4));
            map.put("failIds", cst.getString(5));
            map.put("fileIds", cst.getString(6));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(cst, conn);
        }
        return map;
    }
    
    /**
     * 获取用户的常用分类id列表
     * @param userId
     * @return
     */
    public List<String> getClassStarIds(String userId) {
        List<String> list = new ArrayList<String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            StringBuffer sql = new StringBuffer(); 
            sql.append("select classid from user_class_star WHERE userid=?");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, userId);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("classid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    
    /**
     * 标记/取消标记常用分类
     * @param userId
     * @param companyId
     * @param classId
     * @param status true:标记
     * @return
     */
    public boolean starClass(String userId, String companyId, String classId, boolean status) {
        boolean flag = true;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            String sql = "insert into user_class_star(userid,companyid,classid) values(?,?,?)";
            if (!status) {
                sql = "delete from user_class_star where userid=? and companyid=? and classid=? ";
            }
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, userId);
            pst.setString(2, companyId);
            pst.setString(3, classId);
            pst.executeUpdate();
        } catch (SQLException e) {
            flag = false;
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
    }
    
    /**
     * 修改文件是否为isLast
     * @param fileId 文件id
     * @param isLast '1' '0'
     * @return
     */
    public boolean updateFileIsLast(String companyId, String fileId, String isLast) {
        boolean flag = false;
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "update files_" + companyId + " set isLast=? where id=? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, isLast);
            pst.setString(2, fileId);
            int num = pst.executeUpdate();
            flag = (num > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
    }
    
    /**
     * 修改文件的所有版本为非last
     * @param companyId
     * @param classId
     * @param fileName
     * @param fileType
     * @return
     */
    public void updateFilesIsNotLast(String companyId, String classId, String fileName, String fileType) {
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "update files_" + companyId + " set isLast='0' where CLASSID=? and FILENAME=? and type=? ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, classId);
            pst.setString(2, fileName);
            pst.setString(3, fileType);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
    }
    
    /**
     * 获取下一个版本号
     * @param companyId
     * @param classId
     * @param fileName
     * @param fileType
     * @return
     */
    public int getNextVersion(String companyId, String classId, String fileName, String fileType) {
        int nextVersion = 1;
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select ifnull(max(version),0) from files_" + companyId + " where classid=? and fileName=? and type=? and isdelete='0' ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, classId);
            pst.setString(2, fileName);
            pst.setString(3, fileType);
            rs = pst.executeQuery();
            if (rs.next()) {
                nextVersion = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return nextVersion;
    }
    
    /**
     * 获取当前文件last版本的信息
     * @param companyId
     * @param classId
     * @param fileName
     * @param fileType
     * @return
     */
    public Map<String, String> getCurrentLastFile(String companyId, String classId, String fileName, String fileType) {
        Map<String,String> fileMap = new HashMap<String,String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT id, FILENAME,TYPE,CLASSID,IDSEQ,islast,version FROM FILES_"+companyId+" WHERE classid=? and filename=? and type=? and isDelete='0' and islast='1' ");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, classId);
            pst.setString(2, fileName);
            pst.setString(3, fileType);
            rs = pst.executeQuery();
            if (rs.next()) {
                fileMap.put("id", rs.getString("id"));
                fileMap.put("fileName", rs.getString("FILENAME"));
                fileMap.put("fileType", rs.getString("TYPE"));
                fileMap.put("folderId", rs.getString("CLASSID"));
                fileMap.put("fileIdSeq", rs.getString("IDSEQ"));
                fileMap.put("isLast", rs.getString("islast"));
                fileMap.put("version", rs.getString("version"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return fileMap;
    }
    
    /**
     * 撤销一个文件给某个用户或联系人群组的分享
     * @param companyId
     * @param fileId
     * @return
     */
    public boolean backoutFile(String companyId, String fileId, String targetId, boolean isGroup) {
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String sql = "delete from fileuserrelation where companyid=? and fileid=? and userid=? and forallversion='0'";
        if (isGroup) {
            sql = "delete from fileuserrelation where companyid=? and fileid=? and groupid=? and forallversion='0'";
        }
        int num = 0;
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, companyId);
            pst.setString(2, fileId);
            pst.setString(3, targetId);
            num = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return num > 0;
    }
    
    /**
     * 修改文件的拥有者
     * @param userIdStr 多个用逗号隔开的userIds
     * @param userstatus 改变后的状态，1为启用，0为禁用
     * @return
     */
	public String changeFileOwner(String userIdStr, String userstatus, String companyAdminId, String companyId){
		String msg = "sucsess";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            session = getSession();
            conn = session.connection();
            StringBuilder sql = new StringBuilder();
            String tableName = "files_" + companyId;
            if("1".equals(userstatus)){ //启用用户
                sql.append("update ").append(tableName).append(" set OWNER=CREATOR where CREATOR in (").append(userIdStr).append(")");
            }else{   	//禁用用户
            	sql.append("update ").append(tableName).append(" set OWNER=? where CREATOR in (").append(userIdStr).append(")");
            }
            pst = conn.prepareStatement(sql.toString());
            if(!"1".equals(userstatus))
            	pst.setString(1, companyAdminId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            msg = "";
        } finally {
            JdbcUtil.close(pst, conn);
        }
		return msg;
	}
	public List<Map<String, String>> changeGroupRelationIsAdmin(String userIdStr,
			String userstatus, String userId, String companyId){
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		ResultSet rs = null ;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			session = getOpenSession() ;
			conn = session.connection() ;
			conn.setAutoCommit(false);
			String filesTable = "files_"+companyId;
			//1.获取所有被禁/启用户创建的群组
			String sql = "SELECT g.ID,FLAG,f.CREATOR FROM groups g LEFT JOIN "+filesTable+" f ON g.CLASSID=f.id WHERE g.COMPANYID=? and f.CREATOR in("+userIdStr+")" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(companyId));
			rs = pst.executeQuery() ;
			
			HashMap<String, String> map = null ;
			while(rs.next()){
				map = new HashMap<String, String>();
				map.put("groupId", rs.getInt(1)+"");
				map.put("flag", rs.getString(2));
				map.put("userid", rs.getInt(3)+"");
				list.add(map);
			}
			
			//2.
			sql = "delete from groupusersrelation where groupid=? and userid !=? and isadmin=1" ;
	        JdbcUtil.close(rs, pst) ;
			pst = conn.prepareStatement(sql) ;
			for(Map<String, String> m : list){
				pst.setInt(1, Integer.parseInt(m.get("groupId"))) ;
				pst.setInt(2, Integer.parseInt(m.get("userid"))) ;
				pst.addBatch();
			}
			pst.executeBatch();
			
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
		return list;
	}
	
	 /**
     * 向file_N表插入数据
     * @return 返回0那么标识插入到file_n表中失败
     */
    public Map<String, String> insertIntoFilesN(String companyId, String classId, String fileName, String fileType, String size, String md5, String openLevel, String fileId, String userId) {
        String filesN = "files_" + companyId;
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Session session =this.getSession(false);
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        int resultId = 0;
        Map<String, String> rst = new HashMap<String, String>();
       try {
           
         //获取不到获取最高编号
           Map<String, String> newFileMap=this.getFileHighestVersionMap(filesN, classId,fileName, fileType,createTime);
           
           this.updateFilesIsNotLast(companyId, classId, fileName, fileType);
           // int version = this.getNextVersion(companyId, classId, fileName, fileType);
           
           String idSeq = "";
           String sql = "select CONCAT(IDseq  ,ID,'.') FROM FILES_"+companyId+" tmp WHERE ID="+classId;
           conn = session.connection();
           st = conn.prepareStatement(sql);
           rs = st.executeQuery();
           if(rs.next()){
               idSeq = rs.getString(1);
           }
           JdbcUtil.close(rs, st);
           sql ="INSERT INTO "+filesN+"(CLASSID,FILENAME,CREATOR,OWNER,CREATETIME,SIZE,MD5,TYPE,ISDELETE,FILEID,IDSEQ,VERSION,ISLAST,OPENLEVEL,UPDATETIME,SOLENUMBER,SERIALNUMBER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"+"LPAD("+newFileMap.get("SERIALNUMBER")+",4,0)"+")";
           st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
           st.setInt(1, Integer.parseInt(classId));
           st.setString(2, fileName);
           st.setInt(3, Integer.parseInt(userId));
           st.setInt(4, Integer.parseInt(userId));
           st.setString(5, createTime);
           st.setString(6, size);
           st.setString(7, md5);
           st.setString(8, fileType);
           st.setString(9,"0");
           st.setString(10, fileId);
           st.setString(11, idSeq);
           st.setString(12, newFileMap.get("VERSION"));
           st.setString(13, "1");
           st.setString(14, openLevel);
           st.setString(15, newFileMap.get("UPDATETIME"));//更新时间为创建时间
           st.setString(16, newFileMap.get("SOLENUMBER"));
           st.executeUpdate();
           rs = st.getGeneratedKeys();
           if(rs.next()){
               resultId = rs.getInt(1);
           }
           rst.put("id", resultId+"");
           rst.put("idSeq", idSeq);
           rst.put("version", newFileMap.get("VERSION"));
           rst.put("createTime", createTime);
       } catch (SQLException e) {
           e.printStackTrace();
           return rst;
       } finally {
           JdbcUtil.close(rs, st, conn);
       }
       return rst;
   }

    /**
     * 向分类信息中添加额外的群组信息  包括 未读消息数目、@次数、joindate、jointime
     * @param classList
     */
	public void getClassExtraInfo(List<Map<String, String>> classList, String companyId, String username, String userId) {
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            if(classList.size()>0){
            	StringBuilder sql = new StringBuilder();
            	sql.append("select GROUPID,JOINTIME from groupusersrelation where USERID=?");
            	pst = conn.prepareStatement(sql.toString());
            	pst.setString(1, userId);
            	rs = pst.executeQuery();
            	HashMap<String, String> jointime = new HashMap<String, String>(); 
            	while (rs.next()) {
	                  jointime.put(rs.getString("GROUPID"), rs.getString("JOINTIME")) ;
            	}
				HashMap<String, String> map = this.getNotSeeMessageCount(companyId, username);
				HashMap<String, String> callselfmap = this.getCallSelfNotSeeMessageCount(companyId, username);
				for(Map<String, String> group:classList){
					if(map.get(group.get("flag"))==null || (null != group.get("isStar") && group.get("isStar").equals("true"))){
						group.put("newmessagecount", "0") ;
						group.put("callselfcount", "0") ;
					} else {
						/**  消息超过100条后用99显示  **/
						if(map.get(group.get("flag")).equals("99")){
							group.put("newmessagecount", "99+") ;
						}else{
							group.put("newmessagecount", map.get(group.get("flag"))) ;
						}
						group.put("callselfcount", callselfmap.get(group.get("flag"))==null?"0":callselfmap.get(group.get("flag"))) ;
					}
					
					String JOINTIME = jointime.get(group.get("groupId")) ;
					if(JOINTIME==null){
						group.put("joindate", "") ;
						group.put("jointime", "") ;
					} else {
						String[] array = JOINTIME.split(" ") ;
						group.put("joindate", array[0]) ;
						group.put("jointime", array[1]) ;
					}
				}
			}
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
	}

	/** lujixiang 20150804 判断用户是否拥有某一文档权限(忽略版本号)
     * 获取用户拥有权限的文件id列表
     * @return
     */
    public boolean hasRightIgnoreVersion(int companyId, int userId, int classid, String fileName, String type, List<String> groupIdList) {
        String groupIds = StringUtil.list2String(groupIdList, ",");
        ArrayList<String> list = new ArrayList<String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select FILEID from fileuserrelation WHERE COMPANYID=? ";
            if (groupIdList != null && !groupIdList.isEmpty()) {
                sql += "and (USERID=? or groupid in("+groupIds+"))";
            } else {
                sql += "and USERID=?"; 
            }
            sql += " and FILEID in ( select ID from files_" + companyId + " where CLASSID=? AND FILENAME=? AND TYPE=? AND ISDELETE='0' AND ISFILE='1' )" ;
            pst = conn.prepareStatement(sql);
            pst.setInt(1, companyId);
            pst.setInt(2, userId);
            pst.setInt(3, classid);
            pst.setString(4, fileName);
            pst.setString(5, type);
            rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return false;
    }
    
    /**
     * wangwenshuo  判断ids的文件夹是否存在于回收站中  如果存在先得到上级的父文件夹的路径
     * @param companyId
     * @param ids  in参数  1,2,3
     * @return
     */
	public String getTrashFileByFromIds(String companyId, String ids){
		Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        String pathName = null;
        try {
            session = getSession();
            conn = session.connection();
            String trashTableName = "files_trash_"+companyId;
            Map<String,String> idMap = new HashMap<String, String>();
            StringBuffer sql = new StringBuffer(); 
            // 查出原有的删除记录 
            sql.append("select PATHNAME,FILENAME from  ").append(trashTableName).append(" where FROMID in ("+ids+")");
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while(rs.next()){
            	String fullName = rs.getString(1)+rs.getString(2);
            	//获取最短的路径   就是最上层的路径
            	if(pathName!=null){
            		if(pathName.length()<fullName.length());
            			pathName = fullName;
            	}else{
            		pathName = fullName;
            	}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return pathName;
	}
	
	
	
	/**lujixiang 20150810   批量添加文件到回收站, 暂不支持回滚（单纯的回滚无意义，建议将删除文件和将文件放到回收站做事务处理）
     * @param fileId
     * @return
     */
    public boolean addFilesTrashById(String companyId, List<String> fileIds, String userId, String pathName) {
        boolean FLAG = true;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        String filesTable = "files_" + companyId;
        String trashTable = "files_trash_" + companyId;
        try {
            session = getSession();
            conn = session.connection();
            StringBuffer sql = new StringBuffer() ;
            String deleteTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
            sql.append("insert into " + trashTable + "(fromid, filename, type, creator, owner, idseq, isfile, deleteuserid, openlevel, deletetime, pathname ) ")
            	.append(" 	select id as fromid,filename,type,creator,owner,idseq,isfile," +userId+ " as deleteuserid,openlevel, '" + deleteTime + "' as deletetime, '" + pathName + "' as pathname  from " + filesTable + " where id=?");
            pst = conn.prepareStatement(sql.toString());
            
            for (String fileId : fileIds) {
            	if (null == fileId || "".equals(fileId))  continue;
            	pst.setString(1, fileId);
            	pst.addBatch();
			}
            pst.executeBatch();
            
        } catch (SQLException e) {
            FLAG = false;
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return FLAG;
    }
    
    
    /** lujixiang 20150810  批量删除文件,支持回滚(建议将删除文件和放入回收站做事物处理)
     * @param fileId 文件id
     * @return
     */
    public boolean deleteFiles(String companyId, List<String> fileIds) {
        boolean flag = false;
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            session = getOpenSession();
            conn = session.connection();
            conn.setAutoCommit(false);
            String sql = "update files_" + companyId + " set isDelete='1' where id=? ";
            pst = conn.prepareStatement(sql);
            for (String tempFileId : fileIds) {
            	pst.setString(1, tempFileId);
            	pst.addBatch();
			}
            
            int[] updateCountArr = pst.executeBatch();
            boolean success = true;
            for (int updateCount : updateCountArr) {
				if (updateCount < 1) {
					conn.rollback();
					success = false;
					break ;
				}
			}
            
            if (success) {
            	conn.commit();
			}
            flag = true;
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
            JdbcUtil.close(pst, conn);
        }
        return flag;
    }
    
    /**
     * 获取回收站文件列表
     * @author longjunhao 20150817
     * @param companyId
     * @param ids
     * @return
     */
    public List<Map<String, String>> getTrashFileListByIds(String companyId, List<String> idList) {
        String ids = StringUtil.list2String(idList, ",");
        if (idList == null || idList.isEmpty()) {
            return new ArrayList<Map<String, String>>();
        }
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_trash_" + companyId;
            StringBuilder sql = new StringBuilder();
            sql.append("select id,fromid, filename, deleteuserid, creator, owner, deletetime, pathname, type, isdelete, isfile from ").append(tableName)
                .append(" where fromid in(").append(ids).append(")");
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> file = new HashMap<String, String>();
                file.put("trashId", rs.getString("id")); 
                file.put("fileId", rs.getString("fromid")); 
                file.put("fileName", StringUtil.string(rs.getString("fileName")));
                file.put("deleteUserId", StringUtil.string(rs.getString("deleteuserid")));
                file.put("userId", StringUtil.string(rs.getString("creator")));
                file.put("owner", StringUtil.string(rs.getString("owner")));
                file.put("deleteTime", StringUtil.string(rs.getString("deletetime")));
                file.put("pathName", StringUtil.string(rs.getString("pathname")));
                file.put("type", StringUtil.string(rs.getString("type")));
                file.put("isDelete", StringUtil.string(rs.getString("isDelete")));
                file.put("isFile", StringUtil.string(rs.getString("isFile")));
                list.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    public boolean getUserIsPraise(String companyId, String userId,
			String fileId){
    	 ResultSet rs = null;
    	 Session session = null ;
         Connection conn = null ;
         PreparedStatement pst = null ;
         boolean flag = false;
         String sql = "select ID from filepraise_"+companyId+" where userid=? and fileid=?";
         try {
             session = getSession();
             conn = session.connection();
             pst = conn.prepareStatement(sql);
             pst.setString(1, userId);
             pst.setString(2, fileId);
             rs = pst.executeQuery();
             if(rs.next()){
            	 flag = true;
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             JdbcUtil.close(rs,pst, conn);
         }
         return flag;
    }

    /**
	 * wangwenshuo 20150828  根据idseq获取所有的子孙文件(文件夹)
	 * @param idSeq
	 * @return
	 */
	public List<Map<String, String>> getSubFilesByIdseq(String companyId, String idSeqS) {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null ;
        try {
            session = getSession();
            conn = session.connection();
            String tableName = "files_" + companyId;
            StringBuilder sql = new StringBuilder();
            sql.append("select id, classid, filename, creator, owner, createtime, size, md5, type, praisecount, isdelete, isfile, idseq, version, fileid, openlevel from ").append(tableName)
                .append(" where isDelete = '0' AND IDSEQ like '").append(idSeqS).append("%'");
            pst = conn.prepareStatement(sql.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                Map<String, String> file = new HashMap<String, String>();
                String id = rs.getString("id");
                file.put("id", id); // 当前数据id
                file.put("classId", rs.getString("classId")); // 所属分类id
                file.put("fileName", StringUtil.string(rs.getString("fileName")));
                file.put("userId", StringUtil.string(rs.getString("creator")));
                file.put("owner", StringUtil.string(rs.getString("owner")));
                file.put("createTime", StringUtil.string(rs.getString("createTime")));
                file.put("size", StringUtil.string(rs.getString("size")));
                file.put("md5", StringUtil.string(rs.getString("md5")));
                file.put("type", StringUtil.string(rs.getString("type")));
                file.put("praiseCount", StringUtil.string(rs.getString("praiseCount")));
                file.put("isDelete", StringUtil.string(rs.getString("isDelete")));
                file.put("isFile", StringUtil.string(rs.getString("isFile")));
                file.put("idSeq", rs.getString("idSeq"));
                file.put("version", StringUtil.string(rs.getString("version")));
                file.put("fileId", StringUtil.string(rs.getString("fileId")));
                file.put("openLevel", rs.getString("openlevel"));
                list.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
	}
	
    /**
     * 获取用户的常用分类id列表
     * @param userId companyId
     * @return
     */
    public List<String> getClassStarIds(String userId,String companyId) {
        List<String> list = new ArrayList<String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            StringBuffer sql = new StringBuffer(); 
            sql.append("select classid from user_class_star WHERE userid=? and companyid=?");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, userId);
            pst.setString(2, companyId);
            rs = pst.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("classid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return list;
    }
    /**
     * 获取用户的常用分类id列表
     * @param userId companyId
     * @return
     */
    public Map<String, String> getClassImg(String companyId) {
    	
    	Map<String, String> retuMap=new HashMap<String, String>();
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            StringBuffer sql = new StringBuffer(); 
            sql.append("SELECT sl.FLAG,GROUP_CONCAT(sl.po) as PORTRAITS FROM (");
            sql.append("SELECT DISTINCT g.FLAG,g.GROUPNAME,u.USERNAME,IFNULL(u.PORTRAIT,'apps/onlinefile/templates/ESDefault/images/profle.png')");
            sql.append(" AS po FROM groups AS g  LEFT JOIN groupusersrelation AS gu ");
            sql.append(" ON g.ID=gu.GROUPID  LEFT JOIN users AS u ON gu.USERID=u.ID ");
            sql.append(" WHERE g.CLASSID <> 0 AND g.COMPANYID= ?  and u.`STATUS` <> '-1' ");
            sql.append(") AS sl GROUP BY sl.FLAG");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, companyId);
            rs = pst.executeQuery();
            while (rs.next()) {
            	retuMap.put(rs.getString("FLAG"), rs.getString("PORTRAITS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return retuMap;
    }
    
    
    /**
     * 判断文件名称是否已经存在（同一文件夹下）
     * @author longjunhao 20150910
     * @param companyId
     * @param classId
     * @param fileName
     * @param fileType
     * @return  true：存在
     */
    public boolean checkFileNameExist(String companyId, String classId, String fileName, String fileType) {
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        int num = 0;
        try {
            session = getSession();
            conn = session.connection();
            StringBuffer sql = new StringBuffer(); 
            sql.append("select count(*) from files_"+companyId+" WHERE classId=? and filename=? and type=?");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, classId);
            pst.setString(2, fileName);
            pst.setString(3, fileType);
            rs = pst.executeQuery();
            if (rs.next()) {
                num = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return num > 0 ? true : false;
    }

    @Override
    public boolean setFileOpenlevel(String companyId, String fileId, String openlevel,String rename) {
      boolean flag = false;
      Session session = null ;
      Connection conn = null ;
      PreparedStatement pst = null ;
      try {
          session = getOpenSession();
          conn = session.connection();
          conn.setAutoCommit(false);
          StringBuilder sql = new StringBuilder();
          String tableName = "files_" + companyId;
          if(rename!=null){
            sql.append("update ").append(tableName).append(" set openlevel=?,filename=? where id=? "); 
          }else{
          sql.append("update ").append(tableName).append(" set openlevel=? where id=? ");
          }
          pst = conn.prepareStatement(sql.toString());
          pst.setString(1, openlevel);
          pst.setString(2, fileId);
          if(rename!=null){
            pst.setString(3, rename);
          }
          int num = pst.executeUpdate();
          if (num == 1) {
              flag = true;
          }
          conn.commit();
      } catch (SQLException e) {
          e.printStackTrace();
          try {
            conn.rollback();
          } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
      } finally {
          try {
            conn.setAutoCommit(true);
          } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          JdbcUtil.close(pst, conn);
      }
      return flag;
    }
    

	/**  文件评论  start   **/

	public Map<String, String> onSendComment(Map<String, String> params) {
		String userId = params.get("userId");
		String content = params.get("content");
		String authorName = params.get("authorName");
		String fileId = params.get("fileId");
		String companyId = params.get("companyId");
		String portrait = params.get("portrait");
		String fullName = params.get("fullName");
		String tableName = "fileComment_"+companyId;
        String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        Map<String , String > result = new HashMap<String, String>();
        
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String sql = "insert into "+tableName+"(FILEID,PARENTID,VERSION,MORE,CONTENT,USERID,USERNAME,PORTRAIT,CREATEDATE,FULLNAME) values(?,?,?,?,?,?,?,?,?,?)";
        int num = 0;
        ResultSet rs = null;
        String maxId = "0";
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(fileId));
            pst.setInt(2, 0);
            pst.setInt(3,0 );
            //0代表没有更多回复，1代表存在更多回复
            pst.setInt(4,0);
            pst.setString(5, content);
            pst.setString(6,userId );
            pst.setString(7,authorName );
            pst.setString(8,portrait);
            pst.setString(9,createDate );
            pst.setString(10,fullName );
            num = pst.executeUpdate();
            
            String sql_count = "select MAX(id) from "+tableName;
            pst = conn.prepareStatement(sql_count);
            rs = pst.executeQuery();
            while(rs.next()){
            	maxId = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        if( num > 0 ){
        	result.put("success", "true");
        	result.put("msg", "评论成功");
        }else{
        	result.put("success", "false");
        	result.put("success", "评论失败");
        }
        result.put("maxId", maxId);
		return result;
	}

	public Map<String, Object> onReplyComment(Map<String, String> params) {

		String userId = params.get("userId");
		String content = params.get("content");
		String authorName = params.get("authorName");
		String fileId = params.get("fileId");
		String companyId = params.get("companyId");
		String portrait = params.get("portrait");
		String fullName = params.get("fullName");
		String parentId = params.get("parentId");
		String tableName = "fileComment_"+companyId;
        String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        Map<String , Object > result = new HashMap<String, Object>();
        
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        String sql = "insert into "+tableName+"(FILEID,PARENTID,VERSION,MORE,CONTENT,USERID,USERNAME,PORTRAIT,CREATEDATE,FULLNAME) values(?,?,?,?,?,?,?,?,?,?)";
        int num = 0;
        ResultSet rs = null;

        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(fileId));
            pst.setInt(2, Integer.parseInt(parentId));
            pst.setInt(3,0);
            //0代表没有更多回复，1代表存在更多回复
            pst.setInt(4,1);
            pst.setString(5, content);
            pst.setString(6,userId );
            pst.setString(7,authorName );
            pst.setString(8,portrait);
            pst.setString(9,createDate );
            pst.setString(10,fullName );
            num = pst.executeUpdate();
            
            /**   返回更多回复  **/
            String sql_update = "select ID,USERID,FULLNAME,CREATEDATE,CONTENT from "+tableName+" where parentId =  ? order by ID DESC";
            pst = conn.prepareStatement(sql_update);
            pst.setInt(1, Integer.parseInt(parentId));
            rs = pst.executeQuery();
            List<Map<String,String>> commentRelyLists = new ArrayList<Map<String,String>>();
            Map<String,String> resultMap = null;
            while(rs.next()){
            	resultMap = new HashMap<String, String>();
            	resultMap.put("ID", rs.getString("ID"));
            	resultMap.put("userId", rs.getString("USERID"));
            	resultMap.put("fullName", rs.getString("FULLNAME"));
            	resultMap.put("createDate", rs.getString("CREATEDATE"));
            	resultMap.put("content", rs.getString("CONTENT"));
            	resultMap.put("parentId", parentId);
            	commentRelyLists.add(resultMap);
            }
            result.put("commentMoreReplyLists",commentRelyLists );

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        if( num > 0 ){
        	result.put("success", "true");
        	result.put("msg", "评论成功");
        }else{
        	result.put("success", "false");
        	result.put("success", "评论失败");
        }
        
		return result;
	
	}
	
	public List<Map<String, String>> getFileCommentsListByFileId(Map<String, String> params) {

		String fileId = params.get("fileId");
		String companyId = params.get("companyId");
		String tableName = "fileComment_"+companyId;
		List<Map<String, String>> resultLists = new ArrayList<Map<String,String>>();
        Map<String , String > resultMap = null;
        
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        String sql = "select ID,USERID,FULLNAME,CREATEDATE,PORTRAIT,CONTENT from "+tableName+" where fileId in (select id from files_"+companyId+" where filename = (SELECT filename from files_"+companyId+" where id = ? ) and idseq = (SELECT IDSEQ from files_"+companyId+" where id = ?) ) and parentId = 0 order by ID DESC";
         try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(fileId));
            pst.setInt(2, Integer.parseInt(fileId));
            rs = pst.executeQuery();
            while(rs.next()){
            	resultMap = new HashMap<String, String>();
            	resultMap.put("ID", rs.getString("ID"));
            	resultMap.put("userId", rs.getString("USERID"));
            	resultMap.put("fullName", rs.getString("FULLNAME"));
            	resultMap.put("createDate", rs.getString("CREATEDATE"));
            	resultMap.put("content", rs.getString("CONTENT"));
            	if(isContainMoreReplyCommnets(rs.getString("ID"), companyId)){
            		resultMap.put("more", "1");	
            	}else{
            		resultMap.put("more", "0");	
            	}
            	if(null == rs.getString("PORTRAIT") || rs.getString("PORTRAIT").equals("")){
            		resultMap.put("portRait", "apps/onlinefile/templates/ESDefault/images/profle.png");
            	}else{
            		resultMap.put("portRait", rs.getString("PORTRAIT"));
            	}
            	resultLists.add(resultMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        
		return resultLists;
	
	}
	
	/**
	 * 判断当前评论条目是否有更多回复
	 * @param commetId
	 * @param companyId
	 * @return
	 */
	private boolean isContainMoreReplyCommnets(String commetId,String companyId){

		String tableName = "fileComment_"+companyId;
		List<Map<String, String>> resultLists = new ArrayList<Map<String,String>>();
        Map<String , String > resultMap = null;
        
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        String sql = "select count(*) from "+tableName+" where PARENTID = ?";
        boolean result = false;
        
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(commetId));
            rs = pst.executeQuery();
            while(rs.next()){
            	if(rs.getInt(1)>0){
            		result = true;
            	}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
		return result;
	
	}
	
	public List<Map<String, String>> getMoreReplyComments(Map<String, String> params) {

		String parentId = params.get("parentId");
		String companyId = params.get("companyId");
		String tableName = "fileComment_"+companyId;
		List<Map<String, String>> resultLists = new ArrayList<Map<String,String>>();
        Map<String , String > resultMap = null;
        
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        String sql = "select ID,USERID,FULLNAME,CREATEDATE,PORTRAIT,CONTENT from "+tableName+" where parentId =  ? order by ID DESC";
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(parentId));
            rs = pst.executeQuery();
            while(rs.next()){
            	resultMap = new HashMap<String, String>();
            	resultMap.put("ID", rs.getString("ID"));
            	resultMap.put("userId", rs.getString("USERID"));
            	resultMap.put("fullName", rs.getString("FULLNAME"));
            	resultMap.put("createDate", rs.getString("CREATEDATE"));
            	resultMap.put("content", rs.getString("CONTENT"));
            	if(null == rs.getString("PORTRAIT") || rs.getString("PORTRAIT").equals("")){
            		resultMap.put("portRait", "apps/onlinefile/templates/ESDefault/images/profle.png");
            	}else{
            		resultMap.put("portRait", rs.getString("PORTRAIT"));
            	}
            	resultMap.put("parentId", parentId);
            	resultLists.add(resultMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pst, conn);
        }
        
		return resultLists;
	
	}

  @Override
  public Map<String, String> getRenameFile(Map<String, String> param) {
    String companyId = param.get("companyId");
    String classId = param.get("classId");
    String userId = param.get("userId");
    String fileName = param.get("fileName");
    String fileType = param.get("fileType");
    String openlevel = param.get("openlevel");
    Map<String , String > resultMap = null;
    Session session = null ;
    Connection conn = null ;
    PreparedStatement pst = null ;
    ResultSet rs = null;
    String tableName = "files_"+companyId;
    StringBuilder sb = new StringBuilder();
    sb.append("select * from "+tableName+" where ")
      .append("CLASSID = ? and TYPE = ? and FILENAME =? ");
     // if(openlevel=="3") sb.append("and OWNER = '"+userId+"' ");
    try {
        session = getSession();
        conn = session.connection();
        int num = 1;
        //循环执行sql语句找到不重复的文件名
        do{
          if(rs!=null){
            fileName = fileName+"("+num+")";
          }
          pst = conn.prepareStatement(sb.toString());
          pst.setString(1, classId);
          pst.setString(2, fileType);
          pst.setString(3, fileName);
          rs = pst.executeQuery();
          num++;
        }while(rs.next());
        
        resultMap.put("fileName", fileName);

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        JdbcUtil.close(pst, conn);
    }
    
    return resultMap;
  }
	
	public Map<String, String>  updateFileCommentsUserHeadImg(Map<String,String> params){
	      boolean flag = false;
	      Session session = null ;
	      Connection conn = null ;
	      PreparedStatement pst = null ;
	      String imgPath = params.get("path");
	      String userName = params.get("username");
	      String companyId = params.get("companyid");
	      Map<String, String> result = new HashMap<String, String>();
	      try {
	          session = getSession();
	          conn = session.connection();
	          StringBuilder sql = new StringBuilder();
	          String tableName = "fileComment_" + companyId;
	          sql.append("update ").append(tableName).append(" set portrait=? where username=? ");
	          pst = conn.prepareStatement(sql.toString());
	          pst.setString(1, imgPath);
	          pst.setString(2, userName);
	          int num = pst.executeUpdate();
	          if (num == 1) {
	              flag = true;
	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	      } finally {
	          JdbcUtil.close(pst, conn);
	      }
	      if(flag){
	    	  result.put("success", "true");
	      }else{
	    	  result.put("success", "false");
	      }
	      return result;
	    }
  
  /**
   * 获得最新版本的文件
   */
  @Override
  public List<Map<String, String>> getFileOfIsLast(String companyId, String classId,
      String fileName, String fileType) {
        Session session = null;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<Map<String, String>> files= new ArrayList<Map<String,String>>();
        try {
            session = getSession();
            conn = session.connection();
            String sql = "select * from files_" + companyId + " where classid=? and fileName=? and type=? and islast=? and isDelete='0' ";
            pst = conn.prepareStatement(sql);
            pst.setString(1, classId);
            pst.setString(2, fileName);
            pst.setString(3, fileType);
            pst.setString(4, "1");
            rs = pst.executeQuery();
            if (rs.next()) {
               Map<String,String> file = new HashMap<String,String>();
               file.put("id", rs.getString("id"));
               file.put("classId", StringUtil.string(rs.getString("classId")));
               file.put("fileName", StringUtil.string(rs.getString("fileName")));
               file.put("userId", StringUtil.string(rs.getString("creator")));
               file.put("owner", StringUtil.string(rs.getString("owner")));
               file.put("createTime", StringUtil.string(rs.getString("createTime")));
               file.put("size", StringUtil.string(rs.getString("size")));
               file.put("type", StringUtil.string(rs.getString("type")));
               file.put("praiseCount", StringUtil.string(rs.getString("praiseCount")));
               file.put("isFile", rs.getString("isFile"));
               file.put("openlevel", StringUtil.string(rs.getString("openlevel")));
               file.put("fileId", StringUtil.string(rs.getString("fileId")));
               files.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs, pst, conn);
        }
        return files;

  }

  @Override
  public Map<String, String> getFileFirstVersion(String companyId, String classId, String fileName,
      String fileType, String soleNumber) {
    Session session = null;
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    Map<String,String> file = new HashMap<String,String>();
    try {
        session = getSession();
        conn = session.connection();
        																			//wangwenshuo 20151229  修改为根据时间排序  取最早的那个
        String sql = "select * from files_" + companyId + " where classid=? and fileName=? and type=? and soleNumber=? order by createTime limit 1";
        pst = conn.prepareStatement(sql);
        pst.setString(1, classId);
        pst.setString(2, fileName);
        pst.setString(3, fileType);
        pst.setString(4, soleNumber);  //添加唯一标识，获取本组文件第一个版本信息
        rs = pst.executeQuery();
        if (rs.next()) {
           
           file.put("id", rs.getString("id"));
           file.put("classId", StringUtil.string(rs.getString("classId")));
           file.put("fileName", StringUtil.string(rs.getString("fileName")));
           file.put("userId", StringUtil.string(rs.getString("creator")));
           file.put("owner", StringUtil.string(rs.getString("owner")));
           file.put("createTime", StringUtil.string(rs.getString("createTime")));
           file.put("size", StringUtil.string(rs.getString("size")));
           file.put("type", StringUtil.string(rs.getString("type")));
           file.put("praiseCount", StringUtil.string(rs.getString("praiseCount")));
           file.put("isFile", rs.getString("isFile"));
           file.put("fileId", StringUtil.string(rs.getString("fileId")));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        JdbcUtil.close(rs, pst, conn);
    }
    return file;
  }

  @Override
  public List<Map<String, String>> getFileUserRelationByFileId(String companyId, String userId,String fileId,String groupId ) {
    Session session = null ;
    Connection conn = null ;
    PreparedStatement pst = null ;
    ResultSet rs = null ;
    List<Map<String, String>> list = new ArrayList<Map<String,String>>();
    try {
        session = getSession();
        conn = session.connection();
        String sql = "select * from fileuserrelation WHERE COMPANYID=? AND FILEID=? and forallversion='0' and (USERID=? or GROUPID=? )";
      
        pst = conn.prepareStatement(sql);
        pst.setString(1, companyId);
        pst.setString(2, fileId);
        pst.setString(3, userId);
        pst.setString(4, groupId);
        rs = pst.executeQuery();
        while (rs.next()) {
          Map<String, String> Map = new HashMap<String,String>();
          Map.put("groupId", rs.getString("GROUPID"));
          Map.put("isDownload", rs.getString("isDownload"));
          Map.put("isLook", rs.getString("isLook"));
          list.add(Map);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        JdbcUtil.close(rs, pst, conn);
    }
    return list;
  }
  
  /**
   * wangwenshuo add 20160113
   * 	根据序列号获取分享权限
   */
  public List<Map<String, String>> getFileUserRelationBySoleNumber(String companyId, String userId,String soleNumber,String groupId ) {
    Session session = null ;
    Connection conn = null ;
    PreparedStatement pst = null ;
    ResultSet rs = null ;
    List<Map<String, String>> list = new ArrayList<Map<String,String>>();
    try {
        session = getSession();
        conn = session.connection();
        String sql = "select * from fileuserrelation WHERE COMPANYID=? AND FILEID=? and forallversion='1' and (USERID=? or GROUPID=?)";
      
        pst = conn.prepareStatement(sql);
        pst.setString(1, companyId);
        pst.setString(2, soleNumber);
        pst.setString(3, userId);
        pst.setString(4, groupId);
        rs = pst.executeQuery();
        while (rs.next()) {
          Map<String, String> Map = new HashMap<String,String>();
          Map.put("groupId", rs.getString("GROUPID"));
          Map.put("isDownload", rs.getString("isDownload"));
          Map.put("isLook", rs.getString("isLook"));
          list.add(Map);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        JdbcUtil.close(rs, pst, conn);
    }
    return list;
  }

  public Map<String,Object> getCommentMembersByUserId(Map<String,String> map){
		String userId = map.get("userId");
		String userName = map.get("userName");
		String companyId = map.get("companyId");
		String groupId = map.get("groupId");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String,String>> tmpUserLists = new ArrayList<Map<String,String>>();
		Map<String,String> tmpUserMaps = null;
		/** 获取到了公司所有成员   **/
		Map<String, Map<String,String>> allCompanyUsers = getAllUsersByComPanyIdId(companyId);
		allCompanyUsers.remove(userName);
		
		List<String> unUsedUsersLists = getUnUsedUsersLists(companyId);
		List<String> groupUserLists = getGroupUsersByGroupId(groupId);
		
		for (Map.Entry<String, Map<String,String>> entry : allCompanyUsers.entrySet()) {
			if(!unUsedUsersLists.contains(entry.getKey()) && groupUserLists.contains(entry.getKey())){
				tmpUserLists.add(entry.getValue());
			}
		}
		resultMap.put("commmentUsers", tmpUserLists);
		
		return resultMap;
	}
  
  	/**
  	 * 通过ID获取群组内成员
  	 * @param groupid
  	 * @return 返回用户的USERNAME集合
  	 */
  public List<String> getGroupUsersByGroupId(String groupid) {
		List<String> users = new ArrayList<String>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT USERNAME FROM USERS WHERE ID IN ( SELECT USERID FROM groupusersrelation WHERE GROUPID=? )" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(groupid));
			rs = pst.executeQuery() ;
			while(rs.next()){
				users.add(rs.getString("USERNAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return users;
	}
  
  /**
   * 根据用户输入参数检索
   * @param params
 * @return 
   */
	public Map<String, Object> queryMembersBykeyWord(Map<String, String> params){
		String userId = params.get("userId");
		String userName = params.get("userName");
		String companyId = params.get("companyId");
		String param = params.get("keyWord");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String,String>> tmpUserLists = new ArrayList<Map<String,String>>();
		Map<String,String> tmpUserMaps = null;
		/** 获取到了公司所有成员   **/
		Map<String, Map<String,String>> usersMap = getUserByComPanyIdAndParam(companyId,param);
		List<String> unUsedUsersLists = getUnUsedUsersLists(companyId);
		for (Map.Entry<String, Map<String,String>> entry : usersMap.entrySet()) {
			if(!unUsedUsersLists.contains(entry.getKey())){
				tmpUserLists.add(entry.getValue());
			}
		}
		resultMap.put("commmentUsers", tmpUserLists);
		return resultMap;
	}
	
	private Map<String, Map<String, String>> getUserByComPanyIdAndParam(String companyId,String param) {
		 Map<String, Map<String,String>> list = new HashMap<String, Map<String,String>>();
	        Session session = null;
	        Connection conn = null;
	        PreparedStatement pst = null;
	        ResultSet rs = null;
	        StringBuilder builder = new StringBuilder("%").append(param).append("%");
	        try {
	            session = getSession();
	            conn = session.connection();
	            String sql = "select ID, USERNAME, FULLNAME ,TELEOHONE,PORTRAIT,MOBILEPHONE,EMAIL,FAX,POSITION from users where COMPANYID = ? and  FULLNAME like ?  and status = 1";
	            pst = conn.prepareStatement(sql);
	            pst.setString(1, companyId);
	            pst.setString(2, builder.toString());
	            rs = pst.executeQuery();
	            while (rs.next()) {
	                Map<String, String> map = new HashMap<String, String>();
	                if(null != rs.getString("FULLNAME") && !rs.getString("FULLNAME").toString().equals("")){
	                	map.put("id", rs.getString("ID"));
		                map.put("userName", rs.getString("USERNAME"));
		                map.put("fullName", rs.getString("FULLNAME"));
		                map.put("portRait", rs.getString("PORTRAIT"));
		                map.put("teleohone", rs.getString("TELEOHONE"));
		                map.put("mobilephone", rs.getString("MOBILEPHONE"));
		                map.put("email", rs.getString("EMAIL"));
		                map.put("fax", rs.getString("FAX"));
		                map.put("position", rs.getString("POSITION"));
		                list.put(rs.getString("USERNAME"), map);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            JdbcUtil.close(rs, pst, conn);
	        }
	        return list;
	}
	
	public Map<String,String> dragFileToDocumnet(Map<String, String> params){
		
		Map<String ,String> result = new HashMap<String, String>();
		String companyId = params.get("companyId");
		String userId = params.get("userId");
		String sourceFileId = params.get("sourceFileId");
		String sourceFileTitle = params.get("sourceFileTitle");
		String targetDocIdSeq = params.get("targetDocIdSeq");
		String targetDocId = params.get("targetDocId");
		
		String tableName = "files_"+companyId;
		if(null != params.get("isMyDocmentFlag") && params.get("isMyDocmentFlag").equals("true")){
			tableName = "files_user_"+userId;
		}
        boolean flag = false;
        String sql_queryIds = "select id from "+tableName+" where filename = (SELECT filename from "+tableName+" where id = ? ) and idseq = (SELECT IDSEQ from "+tableName+" where id = ?)";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        List<String> ids = new ArrayList<String>();
        int fileVersionCounter = 0;
        try {
            session = getSession();
            conn = session.connection();
            
            /**  先查出来需要update的字段id  **/
            
            pst = conn.prepareStatement(sql_queryIds);
            pst.setString(1, sourceFileId);
            pst.setString(2, sourceFileId);
            rs = pst.executeQuery();
            while(rs.next()){
            	ids.add(rs.getString(1));
            }
            pst.close();
            /**  查询是否存在同名文件，如果存在那么直接更新到最新版本号       **/
            String sql_QueryExist = "select Max(version) from "+tableName +" where idseq = ? and filename = ? and isdelete = '0'" ;
            pst = conn.prepareStatement(sql_QueryExist);
            pst.setString(1, targetDocIdSeq);
            pst.setString(2, sourceFileTitle.substring(0, sourceFileTitle.indexOf(".")));
            rs = pst.executeQuery();
            while(rs.next()){
            	fileVersionCounter = rs.getInt(1) +1;
            }
            pst.close();
            /**  如果大于1那么应该存在相同版本 ，如果不存在默认的为直接改变路径就行了  **/
            if(fileVersionCounter>1){
            	/**   先更新一下当前文件夹下面的islast字段 **/
                String sql_updateLast = "update " + tableName + " set ISLAST='0' where CLASSID=? and FILENAME=? and type=? ";
                pst = conn.prepareStatement(sql_updateLast);
                pst.setInt(1, Integer.valueOf(targetDocId));
                pst.setString(2, sourceFileTitle.substring(0, sourceFileTitle.indexOf(".")));
                pst.setString(3, sourceFileTitle.substring(sourceFileTitle.indexOf(".")+1, sourceFileTitle.length()));
                pst.executeUpdate();
                pst.close();
                
                String sql = "update "+tableName+" set IDSEQ=?,CLASSID=?,VERSION=?,ISLAST='1' where id in ("+StringUtil.list2String(ids, ",")+")";
                pst = conn.prepareStatement(sql);
                pst.setString(1, targetDocIdSeq);
                pst.setString(2, targetDocId);
                pst.setInt(3, fileVersionCounter);
                pst.executeUpdate();
                result.put("success", "true");
            }else{
            	 String sql = "update "+tableName+" set IDSEQ=?,CLASSID=? where id in ("+StringUtil.list2String(ids, ",")+")";
                 pst = conn.prepareStatement(sql);
                 pst.setString(1, targetDocIdSeq);
                 pst.setString(2, targetDocId);
                 pst.execute();
                 result.put("success", "true");
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", "false");
        } finally {
            JdbcUtil.close(pst, conn);
        }
		
		return result;
	}
	
	
	
	/**
	 * lujixiang 20151210   新增文件删除,并更新版本号
     * @param companyId
     * @param fileId
     * @return 是否删除成功
     */
    public boolean deleteFileAndUpdateVersion(String companyId, String fileId, String userId) {
        boolean flag = false;
        
        Session session = null ;
        Connection conn = null ;
        CallableStatement cst = null ;
        
        try {
            session = getSession();
            conn = session.connection();
            cst = conn.prepareCall("{ call proc_delete_file(?,?,?,?) }");
            cst.setString(1, companyId);
            cst.setString(2, fileId);
            cst.setString(3, userId);
            cst.registerOutParameter(4, java.sql.Types.INTEGER);
            cst.execute();
            if(1 == cst.getInt(4) ){
            	flag = true;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(cst, conn);
        }
        return flag;
    }
//====================================
    /**拖拽，保持当前原有版本，拖拽过来的文件未最新版本   */
	public Map<String,String> dragFileToDocumnet2(Map<String, String> params,List<String> ids){
		String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String ,String> result = new HashMap<String, String>();
		String companyId = params.get("companyId");
		String userId = params.get("userId");
		String sourceFileId = params.get("sourceFileId");
		String sourceFileTitle = params.get("sourceFileTitle");
		String fileName=params.get("FILENAME");
		String fileType=params.get("TYPE");
		String targetDocIdSeq = params.get("targetDocIdSeq");
		String targetDocId = params.get("targetDocId");
		String tableName = params.get("tableName");
		boolean flag = false;
		
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        session = getSession();
        conn = session.connection();
        try {
            if(Integer.valueOf(params.get("VERSION"))>1){//当大于1的情况下
            	/**   先更新一下当前文件夹下面的islast字段 **/
                String sql_updateLast = "update " + tableName + " set ISLAST='0' where SOLENUMBER=? ";
                pst = conn.prepareStatement(sql_updateLast);
                pst.setInt(1, Integer.valueOf(params.get("SOLENUMBER")));
                pst.executeUpdate();
                pst.close();
                //需要更新原来的分类的序号
            	String sql = "update "+tableName+" set CREATETIME=?,UPDATETIME=?,IDSEQ=?,CLASSID=?,VERSION=?,ISLAST=?,SERIALNUMBER=?,SOLENUMBER=? where id =?";
            	conn.setAutoCommit(false);
            	pst = conn.prepareStatement(sql);   
                if(ids!=null && ids.size()>0){
                	 for(int x = 0; x < ids.size(); x++){   
                		 pst.setString(1, createTime);
                         pst.setString(2, params.get("UPDATETIME"));
                         pst.setString(3, targetDocIdSeq);
                         pst.setString(4, targetDocId);
                         pst.setInt(5, (Integer.valueOf(params.get("VERSION"))+x));
                         pst.setString(6, (ids.size()-1) == x?"1":"0");
                         pst.setString(7,  params.get("SERIALNUMBER"));
                         pst.setString(8,  params.get("SOLENUMBER"));//目前属于覆盖情况，所以需要更新到当前分类的唯一编号
                         pst.setString(9, ids.get(x));
                         pst.addBatch();   
                      }   
                }
                pst.executeBatch();
                conn.commit();
                result.put("success", "true");
            }else{
            	 String sql = "update "+tableName+" set CREATETIME=?,UPDATETIME=?,IDSEQ=?,CLASSID=?,SERIALNUMBER="+"LPAD("+params.get("SERIALNUMBER")+",4,0)"+" where id in ("+StringUtil.list2String(ids, ",")+")";
                 pst = conn.prepareStatement(sql);
                 pst.setString(1, createTime);//拖过去之后时间为当前时间
                 pst.setString(2, params.get("UPDATETIME"));
                 pst.setString(3, targetDocIdSeq);
                 pst.setString(4, targetDocId);
                 pst.execute();
                 result.put("success", "true");
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", "false");
        } finally {
            JdbcUtil.close(pst, conn);
        }
		
		return result;
	}

	/**
	 * 
	 * 根据：表名、classID、SERIALNUMBER
	 * 获取：当前分类的集合
	 * -- 目前拖拽是将整个拖拽走，排除删除的，彻底删除的。
	 * 返回：当前存在分类的单文件版本集合,
	 * */
	public List<String> getOneFileExistList(String tableName,String classId,String soleNumber) {
		List<String> strIdList=new ArrayList<String>();
		Map<String, String> fileMap= new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			String solenNumberSql = "SELECT ID FROM "+tableName+" WHERE classId = ? AND solenumber=? AND classId <> '-1' AND ISDELETE ='0'  ORDER BY createtime,ID ASC";
			pst = conn.prepareStatement(solenNumberSql);
			pst.setString(1, classId);
			pst.setString(2, soleNumber);
			rs = pst.executeQuery();
			while (rs.next()) {
				strIdList.add(rs.getString(1));
			}
			JdbcUtil.close(rs, pst);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return strIdList;
	}

	/**
	 * xyc
	 * 根据：表名、分类标示、排序标示、唯一编号分组排序
	 * 获取：当前分类下的所有文件未被删除的最高版本List DESC
	 * 获取：当前分类下的所有文件未被删除的最低版本List  ASC
	 * -- 根据更新时间来排序，由于恢复的时候，版本号的更新是根据上传时间的，
	 * 返回：FILENAME、TYPE、UPDATETIME、SERIALNUMBER、SOLENUMBER、VERSION
	 * */
	public List<Map<String, String>> getFileOrFolderVersionList(String tableName,String classId,String isDesc,String isFile) {
		 List<Map<String, String>> fileList= new ArrayList<Map<String,String>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			String solenNumberSql = "select * from (SELECT * FROM "+tableName+" WHERE classId = ? AND ISFILE=? AND classId <> '-1' AND ISDELETE ='0' and islast='1' ORDER BY updatetime "+isDesc+" ,ID "+isDesc+") as c   GROUP BY c.classId,c.solenumber";//
			pst = conn.prepareStatement(solenNumberSql);
			pst.setString(1, classId);
			pst.setString(2, isFile);
			rs = pst.executeQuery();
			while(rs.next()) {
				Map<String, String> fileMap= new HashMap<String, String>();
				fileMap.put("FILENAME", rs.getString("FILENAME"));
				fileMap.put("TYPE", rs.getString("TYPE"));
				fileMap.put("UPDATETIME", rs.getString("UPDATETIME"));
				fileMap.put("SERIALNUMBER", rs.getString("SERIALNUMBER"));
				fileMap.put("SOLENUMBER", rs.getString("SOLENUMBER"));
				fileMap.put("VERSION", rs.getString("VERSION"));
				fileMap.put("CREATOR", rs.getString("CREATOR"));
				fileMap.put("OWNER", rs.getString("OWNER"));
				fileMap.put("OPENLEVEL", rs.getString("OPENLEVEL"));
				fileList.add(fileMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return fileList;
	}

	/**
	 * xyc
	 * 根据：表名、分类标示、文件标示（是否文件夹）isFile,排除删除，彻底删除文件或文件夹
	 * 获取：当前分类下最大的SerialNumber
	 * 返回：SerialNumber
	 * */
	public String getClassHighestSerialNumber(String tableName,String classId,String isFile){
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String SerialNumber="0";
		try {
			session = getSession();
			conn = session.connection();
			String	solenNumberSql = "SELECT IFNULL(MAX(SerialNumber),0) FROM "+tableName +" WHERE classId = ? and ISFILE =? AND isdelete='0' AND classId <> '-1'";
			pst = conn.prepareStatement(solenNumberSql);
			pst.setString(1, classId);
			pst.setString(2, isFile);
			rs = pst.executeQuery();
			while(rs.next()) {
				SerialNumber=rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return SerialNumber;
	}

	/**
	 * 更新编号
	 * 根据：表名、分类标示、"是否文件夹"SOLENUMBER、当前分类最低版本集合（*）
	 * 批量更新当前分类序号：0001
	 * SERIALNUMBER
	 * 返回：标示
	 * */
	public String updateClassFileOrFolderSerialnumber(String tableName,String classId,String isFile) {
		List<Map<String, String>> classFileList=this.getFileOrFolderVersionList(tableName,classId,"ASC",isFile);
		String msg = "true";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            session = getSession();
            conn = session.connection();
            conn.setAutoCommit(false);
            String sql = "update "+tableName+" set SERIALNUMBER=LPAD(?,4,0) where classid=? and SOLENUMBER=? and ISFILE=?";   
            pst = conn.prepareStatement(sql);   
            if(classFileList!=null && classFileList.size()>0){
            	 for(int x = 0; x < classFileList.size(); x++){   
            		 pst.setInt(1, x+1);   
            		 pst.setString(2, classId);   
            		 pst.setString(3, classFileList.get(x).get("SOLENUMBER"));   
            		 pst.setString(4, isFile);   
                     pst.addBatch();   
                  }   
            }
            pst.executeBatch();   
            conn.commit();   //wangwenshuo 20160224 添加 提交与回滚
        } catch (SQLException e) {
        	try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
            e.printStackTrace();
            msg = "false";
        } finally {
            JdbcUtil.close(pst, conn);
        }
		return msg;
	}

	public Map<String, String> getFileHighestVersionMap(String tableName,String classId,String filename,String fileType,String updateTime){
		//String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String, String> retMap=new HashMap<String, String>();
		boolean exist=false;
		//集合返回FILENAME、TYPE、UPDATETIME、SERIALNUMBER、SOLENUMBER、VERSION
		List<Map<String, String>> fileHighestList =this.getFileOrFolderVersionList(tableName,classId,"DESC","1");
		if(fileHighestList!=null && fileHighestList.size()>0){
			for(Map<String, String> fileMap : fileHighestList){
				if(filename!=null && filename.trim().equals(fileMap.get("FILENAME").trim()) && fileType!=null && fileType.trim().equals(fileMap.get("TYPE").trim())){
					fileMap.put("VERSION", (Integer.valueOf(fileMap.get("VERSION"))+1)+"");
					retMap=fileMap;
					exist=true;
					break;
				}else{
					exist=false;
				}
			}
		}
		if(!exist){
			//查询本表文件和文件夹唯一的最高编号
			String  solenumber=this.getFileAndFolderMaxSolenumber(tableName);//, classId."<>"
			//查询分类序号的最高编号
			String  serialnumber=this.getClassFileOrFolderMaxSerialnumber(tableName, classId, "1");
			retMap.put("FILENAME", filename);
			retMap.put("TYPE", fileType);
			retMap.put("UPDATETIME", updateTime);
			retMap.put("SERIALNUMBER", serialnumber);
			retMap.put("SOLENUMBER",solenumber);
			retMap.put("VERSION", "1");
		}
		return retMap;
	}
	
	
	/**
	 * 根据：表名，文件ID
	 * 获取：详细文件详情
	 * */
	public Map<String, String> getFileById(String tableName,String fileId){
		Map<String, String> retMap =new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			String	solenNumberSql = "SELECT FILENAME,TYPE,CLASSID,ISFILE,SERIALNUMBER,SOLENUMBER FROM "+tableName+" WHERE id=?";
				pst = conn.prepareStatement(solenNumberSql);
				pst.setString(1,fileId);
				rs = pst.executeQuery();
				while(rs.next()){
					retMap.put("fileName", rs.getString("FILENAME"));
					retMap.put("fileType", rs.getString("TYPE"));
					retMap.put("classId", rs.getString("CLASSID"));
					retMap.put("isFile", rs.getString("ISFILE"));
					retMap.put("serialNumber", rs.getString("SERIALNUMBER"));
					retMap.put("soleNumber", rs.getString("SOLENUMBER"));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return retMap;
	}
	
		
	/**
	 * 根据：表名  *由于是唯一编号 							 
	 * 获取：当前表下最高唯一编号>包括被删除、彻底删除占用的。
	 * 返回：SOLENUMBER+1
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
	
	/**
	 * 根据：表名、分类标示、"是否文件夹folder<> ="
	 * 获取：当前分类下最高唯一编号>排除删除的与彻底删除的
	 * 返回：serialnumber+1
	 * */
	public String getClassFileOrFolderMaxSerialnumber(String tableName,String classId,String isFile) {
		int maxSerial=0;
		Session session = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			String	serialnumberSql = "SELECT IFNULL(MAX(SERIALNUMBER),0) FROM "+tableName+" WHERE classId =? AND isdelete='0' AND classId <> '-1' AND ISFILE=? ";
			pst = conn.prepareStatement(serialnumberSql);
			pst.setString(1, classId);
			pst.setString(2, isFile);
			rs = pst.executeQuery();
			if (rs.next()) {
				String str1=rs.getString(1);
				maxSerial = Integer.valueOf((str1!=null?str1:"0"))+1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn);
		}
		return maxSerial+"";
	}
	
    /**
     * xiayongcai
     * 更新文件和文件夹拥有者
     * @param 
     * groupsOwnerId：拥有分类的id
     * tableName：表名
     * userIds：用户
     * idSeq：所在分类路径
     */
	@Override
	public boolean updateGroupsFileAndFolderOwner(String tableName,String groupsOwnerId,List<String> userids,String idSeq) {
		boolean msg = true;
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            session = getSession();
            conn = session.connection();
            conn.setAutoCommit(false);
            String sql = "update "+tableName+" set OWNER=? where classid<>'-1' and ISDELETE='0' and OWNER=? and IDSEQ like '"+idSeq+"%'"; //1.classid.%  
            pst = conn.prepareStatement(sql);   
            if(userids!=null && userids.size()>0){
            	 for(int x = 0; x < userids.size(); x++){   
            		 pst.setString(2, userids.get(x));   
            		 pst.setString(1, groupsOwnerId);   
                     pst.addBatch();   
                  }   
            }
            pst.executeBatch();   
        } catch (SQLException e) {
            e.printStackTrace();
            msg= false;
        } finally {
            JdbcUtil.close(pst, conn);
        }
		return msg;
	}
	
	public Map<String, String> getFileForIndex(String companyId,String fileId) {

      Map<String,String> fileMap = new HashMap<String,String>();
      Session session = null ;
      Connection conn = null ;
      PreparedStatement pst = null ;
      ResultSet rs = null ;
      try {
          session = getSession();
          conn = session.connection();
          StringBuilder sql = new StringBuilder();
          sql.append("SELECT * FROM FILES_"+companyId+" WHERE ID = ? ");
          pst = conn.prepareStatement(sql.toString());
          pst.setString(1, fileId);
          rs = pst.executeQuery();

          while (rs.next()) {
              fileMap.put("ID", rs.getString("ID"));
              fileMap.put("FILENAME", rs.getString("FILENAME"));
              fileMap.put("TYPE", rs.getString("TYPE"));
              fileMap.put("CLASSID", rs.getString("CLASSID"));
              fileMap.put("IDSEQ", rs.getString("IDSEQ"));
              fileMap.put("ISLAST", rs.getString("ISLAST"));
              fileMap.put("ISFILE", rs.getString("ISFILE"));
              fileMap.put("VERSION", rs.getString("VERSION"));
              fileMap.put("ISDELETE", rs.getString("ISDELETE"));
              fileMap.put("CREATOR", rs.getString("CREATOR"));
              fileMap.put("OWNER", rs.getString("OWNER"));
              fileMap.put("SIZE", rs.getString("SIZE"));
              fileMap.put("FILEID", rs.getString("FILEID"));
              fileMap.put("OPENLEVEL", rs.getString("OPENLEVEL"));
              fileMap.put("PRAISECOUNT", rs.getString("PRAISECOUNT"));
              fileMap.put("COLLECTCOUNT", rs.getString("COLLECTCOUNT"));
              fileMap.put("CREATETIME", rs.getString("CREATETIME"));
              fileMap.put("UPDATETIME", rs.getString("UPDATETIME"));
              fileMap.put("MD5", rs.getString("MD5"));
              fileMap.put("SERIALNUMBER", rs.getString("SERIALNUMBER"));
              fileMap.put("SOLENUMBER", rs.getString("SOLENUMBER"));
          }
      } catch (SQLException e) {
          e.printStackTrace();
      } finally {
          JdbcUtil.close(rs, pst, conn);
      }
      return fileMap;
  
  }
}
