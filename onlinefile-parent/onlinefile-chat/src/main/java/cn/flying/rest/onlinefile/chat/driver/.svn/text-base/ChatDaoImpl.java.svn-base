package cn.flying.rest.onlinefile.chat.driver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import cn.flying.rest.onlinefile.chat.dao.ChatDao;
import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;
import cn.flying.rest.onlinefile.utils.JdbcUtil;
import cn.flying.rest.onlinefile.utils.MongoManager;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Repository("chatDaoImpl")
@SuppressWarnings("rawtypes")
public class ChatDaoImpl extends BaseDaoHibernate implements ChatDao {
		
	@SuppressWarnings("unchecked")
	public ChatDaoImpl() {
		super(ChatDaoImpl.class);
	}
	
	@Resource(name = "mongoManager")
	MongoManager mongoManager ;
	
//	private SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@SuppressWarnings("deprecation")
	public LinkedHashMap<String, List<HashMap<String, String>>> getOneCompanyUsers(String companyId) {
		LinkedHashMap<String, List<HashMap<String, String>>> companyUsers = new LinkedHashMap<String, List<HashMap<String, String>>>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
//			String sql = "SELECT u.ID,u.USERNAME,u.FULLNAME,u.PORTRAIT,u.SIGNATURE,o.ORGNAME FROM USERS u LEFT JOIN user_org uo ON u.ID=uo.userid LEFT JOIN ORG o ON uo.ORGID=o.id WHERE u.COMPANYID=? AND u.STATUS=1 ORDER BY u.ID" ;
			String sql = "SELECT u.ID,u.USERNAME,u.FULLNAME,u.PORTRAIT,u.SIGNATURE,o.ORGNAME FROM USERS u LEFT JOIN ORG o ON u.ORGID = o.ID where u.COMPANYID=? AND u.STATUS=1 ORDER BY u.ID" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(companyId));
			rs = pst.executeQuery() ;
			HashMap<String, String> item = null ;
			while(rs.next()){
				String ORGNAME = rs.getString("ORGNAME") ;
				if(companyUsers.get(ORGNAME)==null){
					companyUsers.put(ORGNAME, new ArrayList<HashMap<String, String>>()) ;
				}
				item = new HashMap<String, String>();
				item.put("ID", rs.getString("ID")) ;
				item.put("USERNAME", rs.getString("USERNAME")) ;
				item.put("FULLNAME", rs.getString("FULLNAME")) ;
				String PORTRAIT = rs.getString("PORTRAIT") ;
				PORTRAIT = PORTRAIT==null?"":PORTRAIT ;
				item.put("PORTRAIT", PORTRAIT) ;
				item.put("SIGNATURE", rs.getString("SIGNATURE")) ;
				companyUsers.get(ORGNAME).add(item) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return companyUsers;
	}
	
	
	/**
	 * 新增方法  通过分组ID与用户名字加载一个单独的分组
	 * */
	@SuppressWarnings("deprecation")
	public List<HashMap<String,String>> getGroupByGroupId(String userName ,  String groupId , String companyId) {
		List<HashMap<String, String>> groups = new ArrayList<HashMap<String, String>>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT g.ID,g.GROUPNAME,g.REMARK,g.CREATETIME,g.FLAG,gur.ISADMIN,gur.JOINTIME FROM GROUPS g LEFT JOIN groupusersrelation gur ON g.ID=gur.GROUPID LEFT JOIN users u on gur.USERID=u.ID WHERE u.USERNAME=? and g.ID=? and g.COMPANYID=?  and g.classid=0" ;
			pst = conn.prepareStatement(sql) ;
			pst.setString(1, userName);
			pst.setString(2, groupId);
			pst.setString(3, companyId);
			rs = pst.executeQuery() ;
			HashMap<String, String> item = null ;
			while(rs.next()){
				item = new HashMap<String, String>();
				item.put("ID", rs.getString("ID")) ;
				item.put("GROUPNAME", rs.getString("GROUPNAME")) ;
				item.put("REMARK", rs.getString("REMARK")) ;
				item.put("CREATETIME", rs.getString("CREATETIME")) ;
				item.put("FLAG", rs.getString("FLAG")) ;
				item.put("PORTRAIT", "") ;
				item.put("ISADMIN", rs.getString("ISADMIN")) ;
				String JOINTIME = rs.getString("JOINTIME") ;
				String[] array = JOINTIME.split(" ") ;
				item.put("JOINDATE", array[0]) ;
				item.put("JOINTIME", array[1]) ;
				groups.add(item) ;
			}
			if(groups.size()>0){
				HashMap<String, String> map = this.getNotSeeMessageCount(groupId, companyId);
				HashMap<String, String> callselfmap = this.getCallSelfNotSeeMessageCount(groupId, companyId);
				for(HashMap<String, String> group:groups){
					if(map.get(group.get("FLAG"))==null){
						group.put("newmessagecount", "0") ;
						group.put("callselfcount", "0") ;
					} else {
						group.put("newmessagecount", map.get(group.get("FLAG"))) ;
						group.put("callselfcount", callselfmap.get(group.get("FLAG"))==null?"0":callselfmap.get(group.get("FLAG"))) ;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return groups;		
	};
	

	@SuppressWarnings("deprecation")
	public List<HashMap<String, String>> getGroupsByUsername(String companyId, String username) {
		List<HashMap<String, String>> groups = new ArrayList<HashMap<String, String>>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT g.ID,g.GROUPNAME,g.REMARK,g.CREATETIME,g.FLAG,gur.ISADMIN,gur.JOINTIME FROM GROUPS g LEFT JOIN groupusersrelation gur ON g.ID=gur.GROUPID LEFT JOIN users u on gur.USERID=u.ID WHERE u.USERNAME=? and g.COMPANYID=? and g.classid=0 ORDER BY g.ID" ;
			pst = conn.prepareStatement(sql) ;
			pst.setString(1, username);
			pst.setString(2, companyId);
			rs = pst.executeQuery() ;
			HashMap<String, String> item = null ;
			while(rs.next()){
				item = new HashMap<String, String>();
				item.put("ID", rs.getString("ID")) ;
				item.put("GROUPNAME", rs.getString("GROUPNAME")) ;
				item.put("REMARK", rs.getString("REMARK")) ;
				item.put("CREATETIME", rs.getString("CREATETIME")) ;
				item.put("FLAG", rs.getString("FLAG")) ;
				item.put("PORTRAIT", "") ;
				item.put("ISADMIN", rs.getString("ISADMIN")) ;
				String JOINTIME = rs.getString("JOINTIME") ;
				String[] array = JOINTIME.split(" ") ;
				item.put("JOINDATE", array[0]) ;
				item.put("JOINTIME", array[1]) ;
				groups.add(item) ;
			}
			if(groups.size()>0){
				HashMap<String, String> map = this.getNotSeeMessageCount(companyId, username);
				HashMap<String, String> callselfmap = this.getCallSelfNotSeeMessageCount(companyId, username);
				for(HashMap<String, String> group:groups){
					if(map.get(group.get("FLAG"))==null){
						group.put("newmessagecount", "0") ;
						group.put("callselfcount", "0") ;
					} else {
						group.put("newmessagecount", map.get(group.get("FLAG"))) ;
						group.put("callselfcount", callselfmap.get(group.get("FLAG"))==null?"0":callselfmap.get(group.get("FLAG"))) ;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return groups;
	}
	@SuppressWarnings("deprecation")
	public List<HashMap<String, String>> getGroupsAndClassByUsername(String companyId, String username) {
		List<HashMap<String, String>> groups = new ArrayList<HashMap<String, String>>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT g.ID,g.GROUPNAME,g.classid,g.REMARK,g.CREATETIME,g.FLAG,gur.ISADMIN,gur.JOINTIME FROM GROUPS g LEFT JOIN groupusersrelation gur ON g.ID=gur.GROUPID LEFT JOIN users u on gur.USERID=u.ID WHERE u.USERNAME=? and g.COMPANYID=? ORDER BY g.ID" ;
			pst = conn.prepareStatement(sql) ;
			pst.setString(1, username);
			pst.setString(2, companyId);
			rs = pst.executeQuery() ;
			HashMap<String, String> item = null ;
			while(rs.next()){
				item = new HashMap<String, String>();
				item.put("ID", rs.getString("ID")) ;
				item.put("GROUPNAME", rs.getString("GROUPNAME")) ;
				item.put("REMARK", rs.getString("REMARK")) ;
				item.put("CREATETIME", rs.getString("CREATETIME")) ;
				item.put("FLAG", rs.getString("FLAG")) ;
				item.put("PORTRAIT", "") ;
				item.put("ISADMIN", rs.getString("ISADMIN")) ;
				item.put("classid", rs.getString("classid")) ;
				String JOINTIME = rs.getString("JOINTIME") ;
				String[] array = JOINTIME.split(" ") ;
				item.put("JOINDATE", array[0]) ;
				item.put("JOINTIME", array[1]) ;
				groups.add(item) ;
			}
			if(groups.size()>0){
				HashMap<String, String> map = this.getNotSeeMessageCount(companyId, username);
				HashMap<String, String> callselfmap = this.getCallSelfNotSeeMessageCount(companyId, username);
				for(HashMap<String, String> group:groups){
					if(map.get(group.get("FLAG"))==null){
						group.put("newmessagecount", "0") ;
						group.put("callselfcount", "0") ;
					} else {
						group.put("newmessagecount", map.get(group.get("FLAG"))) ;
						group.put("callselfcount", callselfmap.get(group.get("FLAG"))==null?"0":callselfmap.get(group.get("FLAG"))) ;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return groups;
	}
	//对群组进行分组并获取所有群组包含的图片
	@SuppressWarnings("deprecation")
	public HashMap<String, String> getGroupsByCompanyIdsImgMap(String companyId) {
		HashMap<String, String> item = new HashMap<String, String>();
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT g.ID,GROUP_CONCAT(u.PORTRAIT) AS PORTRAIT FROM GROUPS g LEFT JOIN groupusersrelation gur ON g.ID=gur.GROUPID LEFT JOIN users u on gur.USERID=u.ID WHERE g.classid=0 AND g.COMPANYID=? GROUP BY g.ID ORDER BY g.ID" ;
			pst = conn.prepareStatement(sql) ;
			pst.setString(1, companyId);
			rs = pst.executeQuery() ;
			while(rs.next()){
				String PORTRAIT = rs.getString("PORTRAIT") ;
				PORTRAIT = PORTRAIT == null?"":PORTRAIT;
				item.put(rs.getString("ID"), rs.getString("PORTRAIT")) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return item;
	}

	private HashMap<String, String> getCallSelfNotSeeMessageCount(
			String companyId, String username) {
		HashMap<String, String> map = new HashMap<String, String>() ;
		DB db = mongoManager.getDB("company_"+companyId) ;
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
		DB db = mongoManager.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("newMessageCount");
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
     *  模拟序列 
     * @param tableName
     * @return int
     */
	private long getSequence(DB db, String tableName, DBCollection collection){
	      DBCollection sequence = db.getCollection("sequence");
	      BasicDBObject query = new BasicDBObject();
	      query.put("_id", tableName);
	      BasicDBObject newDocument =new BasicDBObject();
	      newDocument.put("$inc", new BasicDBObject().append("cnt", 1));
	      BasicDBObject ret = (BasicDBObject)sequence.findAndModify(query, newDocument);
	      if (ret == null){
	        return this.initTable(sequence, tableName, collection, query, newDocument);
	      } else {
	        return ret.getLong("cnt") + 1;
	      }
	}
	
	/**
	 * 当表不存在时，初始化表 - 此时需要排队
	 * @param sequence
	 * @param tableName
	 * @param collection
	 * @return
	 */
	private synchronized long initTable(DBCollection sequence, String tableName, DBCollection collection, BasicDBObject query, BasicDBObject newDocument){
		BasicDBObject ret = (BasicDBObject)sequence.findAndModify(query, newDocument);
	    if (ret == null){
	    	BasicDBObject object = new BasicDBObject() ;
	    	object.put("_id", tableName) ;
	    	object.put("cnt", 1) ;
	    	sequence.save(object) ;
	    	this.createIndexs(collection, tableName);
	    	return 1 ;
	    } else {
	        return ret.getLong("cnt") + 1;
	    }
	}
	
	/**
	 * 创建索引
	 * @param db
	 * @param tableName
	 */
	private void createIndexs(DBCollection collection, String tableName){
		if("newMessageCount".equals(tableName)){
			BasicDBObject indexs = new BasicDBObject();
			indexs.put("user", 1) ;
			indexs.put("from", 1) ;
			collection.ensureIndex(indexs);
		} else if("groupNotSeeMessage".equals(tableName)){
			BasicDBObject indexs = new BasicDBObject();
			indexs.put("GROUPFLAG", 1) ;
			indexs.put("USERNAME", 1) ;
			collection.ensureIndex(indexs);
		} else if("personNotSeeMessage".equals(tableName)){
			BasicDBObject indexs = new BasicDBObject();
			indexs.put("USERNAME", 1) ;
			indexs.put("FROMUSER", 1) ;
			collection.ensureIndex(indexs);
		} else if("chatrecords0".equals(tableName)){
			collection.ensureIndex("CHATFLAG");
		} else if("chatrecords1".equals(tableName)){
		    // longjunhao 20150703 修改
//		    BasicDBObject indexs = new BasicDBObject();
//            indexs.put("GROUPFLAG", 1) ;
//            indexs.put("FILEFLAG", 1) ;
//			collection.ensureIndex(indexs);
		    collection.ensureIndex("GROUPFLAG");
		} else if("groupCallOver".equals(tableName)){
			BasicDBObject indexs = new BasicDBObject();
			indexs.put("user", 1) ;
			indexs.put("from", 1) ;
			collection.ensureIndex(indexs);
		} else if ("fileComment".equals(tableName)) {
		    collection.ensureIndex("FILEFLAG");
		}
	}
	
	/**
	 * 保存未看消息计数信息
	 * @param db
	 * @param username
	 * @param from
	 */
	private void saveNewMessageCount(DB db, String username, String from){
	    DBCollection newMessageCount = db.getCollection("newMessageCount");
	    BasicDBObject query = new BasicDBObject();
	    query.put("user", username);
	    query.put("from", from);
	    DBCursor cursor = null;
	    cursor = newMessageCount.find(query);
	    int messageCounts = 0 ;
	    while(cursor.hasNext()){
	    	BasicDBObject obj = (BasicDBObject)cursor.next() ;
	    	messageCounts = obj.getInt("c");
	    }
	    // if(messageCounts<99){
		    BasicDBObject newDocument =new BasicDBObject();
		    newDocument.put("$inc", new BasicDBObject().append("c", 1));
		    BasicDBObject ret = (BasicDBObject)newMessageCount.findAndModify(query, newDocument);
		    if (ret == null){
		    	BasicDBObject object = new BasicDBObject() ;
		    	object.put("_id", getSequence(db, "newMessageCount", newMessageCount));
		    	object.put("user", username) ;
		    	object.put("from", from) ;
		    	object.put("c", 1) ;
		    	newMessageCount.save(object) ;
		    }
	   // }
	}
	
	/**
	 * 保存未看消息
	 */
	public Long saveNotSeeMessage(String companyId, String username, String content, String date, String time, String from, String isGroup, String groupFlag, String fromCnName) {
		DB db = mongoManager.getDB("company_"+companyId) ;
		if("1".equals(isGroup)){
			/** 群聊 **/
			return this.saveGroupNotSeeMessage(db, companyId, username, content, date, time, from, groupFlag, fromCnName) ;
		} else {
			/** 私聊 **/
			return this.savePersonNotSeeMessage(db, companyId, username, content, date, time, from, fromCnName) ;
		}
	}
	
	/**
	 * 删除保存未看消息
	 */
	public boolean dropNotSeeMeesage(String companyId, String from, String username, long notSeeMsgId) {
		DB db = mongoManager.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("personNotSeeMessage");
		BasicDBObject object = new BasicDBObject() ;
		object.put("_id", notSeeMsgId) ;
		boolean isOk = coll.remove(object).getN()>0;
		if(isOk){
			DBCollection newMCColl = db.getCollection("newMessageCount");
			BasicDBObject query = new BasicDBObject();
			query.put("user", username);
		    query.put("from", from);
		    
		    BasicDBObject newObj = new BasicDBObject().append("$inc",new BasicDBObject().append("c", -1));
		    newMCColl.update(query, newObj);
		}
		return isOk;
	}

	/**
	 * 保存群聊未看消息
	 * @param db
	 * @param companyId
	 * @param username
	 * @param content
	 * @param date
	 * @param time
	 * @param from
	 * @param groupFlag
	 * @param fromCnName 
	 * @return
	 */
	private long saveGroupNotSeeMessage(DB db, String companyId, String username,
			String content, String date, String time, String from,
			String groupFlag, String fromCnName) {
		DBCollection coll = db.getCollection("groupNotSeeMessage");
		BasicDBObject object = new BasicDBObject() ;
		object.put("FROMUSER", from) ;
		object.put("FROMCNNAME", fromCnName) ;
		object.put("USERNAME", username) ;
		object.put("CONTENT", content) ;
		object.put("DATE", date) ;
		object.put("TIME", time) ;
		object.put("GROUPFLAG", groupFlag) ;
		long notSeeMsgId = getSequence(db, "groupNotSeeMessage", coll);
		object.put("_id", notSeeMsgId) ;
		if(coll.save(object).getN()>0){
			this.saveNewMessageCount(db, username, groupFlag);
			return notSeeMsgId ;
		}
	    return 0L ;
	}

	/**
	 * 保存两人间直接通信未看信息
	 * @param db
	 * @param companyId
	 * @param username
	 * @param content
	 * @param date
	 * @param time
	 * @param from
	 * @param fromCnName 
	 * @return
	 */
	private Long savePersonNotSeeMessage(DB db, String companyId, String username,
			String content, String date, String time, String from, String fromCnName) {
		DBCollection coll = db.getCollection("personNotSeeMessage");
		BasicDBObject object = new BasicDBObject() ;
		object.put("FROMUSER", from) ;
		object.put("USERNAME", username) ;
		object.put("CONTENT", content) ;
		object.put("DATE", date) ;
		object.put("TIME", time) ;
		long notSeeId = getSequence(db, "personNotSeeMessage", coll);
		object.put("_id", notSeeId) ;
		if(coll.save(object).getN()>0){
			this.saveNewMessageCount(db, username, from);
			return notSeeId ;
		}
		return 0L ;
	}

	/**
	 * 获取未看信息
	 */
	public List<HashMap<String, String>> getOldNotSeeMessage(String companyId, String receiver, String username, String isGroup){
		List<HashMap<String, String>> msgs = new ArrayList<HashMap<String, String>>() ;
		try {
			DB db = mongoManager.getDB("company_"+companyId) ;
			if("1".equals(isGroup)){
				/** 代表为群聊消息 **/
				DBCollection coll = db.getCollection("groupNotSeeMessage");
				BasicDBObject cond = new BasicDBObject();
		        BasicDBList values = new BasicDBList();  
		        values.add(new BasicDBObject("GROUPFLAG", receiver));
		        values.add(new BasicDBObject("USERNAME", username));
		        cond.put("$and", values); 
		        DBCursor cursor = coll.find(cond).sort(new BasicDBObject("_id", 1));
				HashMap<String, String> map = null ;
				BasicDBObject rs = null ;
				while(cursor.hasNext()){
					rs = (BasicDBObject)cursor.next() ;
					map = new HashMap<String, String>() ;
					String CONTENT = rs.getString("CONTENT") ;
					CONTENT = CONTENT.replaceAll(" ", "%20");
					map.put("CONTENT", URLEncoder.encode(CONTENT, "utf-8")) ;
					map.put("DATE", rs.getString("DATE")) ;
					map.put("TIME", rs.getString("TIME")) ;
					map.put("FROMUSER", rs.getString("FROMUSER")) ;
					map.put("FROMCNNAME", rs.getString("FROMCNNAME")) ;
					msgs.add(map) ;
				}
			} else {
				/** 代表为私聊消息 **/
				DBCollection coll = db.getCollection("personNotSeeMessage");
				BasicDBObject cond = new BasicDBObject();
		        BasicDBList values = new BasicDBList();  
		        values.add(new BasicDBObject("USERNAME", username));
		        values.add(new BasicDBObject("FROMUSER", receiver));
		        cond.put("$and", values); 
		        DBCursor cursor = coll.find(cond).sort(new BasicDBObject("_id", 1));
				HashMap<String, String> map = null ;
				BasicDBObject rs = null ;
				while(cursor.hasNext()){
					rs = (BasicDBObject)cursor.next() ;
					map = new HashMap<String, String>() ;
					String CONTENT = rs.getString("CONTENT") ;
					CONTENT = CONTENT.replaceAll(" ", "%20");
					map.put("CONTENT", URLEncoder.encode(CONTENT, "utf-8")) ;
					map.put("DATE", rs.getString("DATE")) ;
					map.put("TIME", rs.getString("TIME")) ;
					msgs.add(map) ;
				}
			}
			dropOldNotSeeMessage(companyId, receiver, username, isGroup) ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return msgs ;
	}
	
	/**
	 * 在获取完之前未看的消息后将数据删除
	 * @param companyId
	 * @param receiver
	 * @param username
	 * @param isGroup 
	 * @return
	 */
	private boolean dropOldNotSeeMessage(String companyId, String receiver, String username, String isGroup){
		boolean isOK = false ;
		DB db = mongoManager.getDB("company_"+companyId) ;
		if("1".equals(isGroup)){
			DBCollection coll = db.getCollection("groupNotSeeMessage");
			BasicDBObject cond = new BasicDBObject();
	        BasicDBList values = new BasicDBList();  
	        values.add(new BasicDBObject("GROUPFLAG", receiver));
	        values.add(new BasicDBObject("USERNAME", username));
	        cond.put("$and", values); 
	        isOK = coll.remove(cond).getN()>0 ;
		} else {
			DBCollection coll = db.getCollection("personNotSeeMessage");
			BasicDBObject cond = new BasicDBObject();
	        BasicDBList values = new BasicDBList();  
	        values.add(new BasicDBObject("USERNAME", username));
	        values.add(new BasicDBObject("FROMUSER", receiver));
	        cond.put("$and", values); 
	        isOK = coll.remove(cond).getN()>0 ;
		}
		if(isOK){
			DBCollection coll = db.getCollection("newMessageCount");
			BasicDBObject query = new BasicDBObject();
		    query.put("user", username);
		    query.put("from", receiver);
		    isOK = coll.remove(query).getN()>0 ;
		    coll = db.getCollection("groupCallOver");
	        isOK = coll.remove(query).getN()>0 ;
		}
		return isOK ;
	}
	
	/**
	 * 将Boolean类型转换成了String 返回值是一个拼接的值 
	 * 分为两个部分组成
	 * ① 插入新值后返回的ID
	 * ② 返回的成功与否的状态
	 * 例子：78_true
	 * */
	@SuppressWarnings("deprecation")
	public String createGroup(String companyId, String username,
			String groupuserids, String manageruserid, String groupname,
			String groupremark, String groupflag, String time) {
		boolean isOK = false ;
		/** 用于接收新插入数据的ID */
		int groupId = 0 ;
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
		return groupId+","+isOK;
	}
	
	/**
	 * 保存历史消息
	 */
	public boolean saveHistoryMessage(String companyId, String username, String content, String date, String time, String from, String isGroup, String fromCnName, String styleTpl, String fileFlag,Map<String, Map<String, String>> companyUserInitInfo) {
		DB db = mongoManager.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("chatrecords"+isGroup);
		BasicDBObject object = new BasicDBObject() ;
		if("1".equals(isGroup)){
	    	object.put("GROUPFLAG", username) ;
	    	object.put("FROMCNNAME", fromCnName) ;
	    	if (fileFlag == null) {
	    	    fileFlag = "";
	    	}
	    	object.put("FILEFLAG", fileFlag); // 记录该条消息关联的文件
	    	long timestamp = Long.parseLong(date.replaceAll("-", "")+time.replaceAll(":", "")) ;
	    	object.put("TIMESTAMP", timestamp) ;
		} else {
			
			String CHATFLAG = this.getTwoPersonChatFlag(Integer.parseInt(username), Integer.parseInt(from)) ;
	    	object.put("CHATFLAG", CHATFLAG) ;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		if(pattern.matcher(from).matches()){
			object.put("FROMUSER", from) ;
		}else{
			if(companyUserInitInfo==null){
				return false;
			}else{
				if(!"fyBot".equals(username)){
					String fromId = companyUserInitInfo.get(from).get("ID");
					if(fromId!=null && fromId.length()>0){
						object.put("FROMUSER", fromId) ;
					}else{
						return false;
					}
				}
			}
		}
	    object.put("CONTENT", content.toLowerCase()) ;
	    object.put("CONTENT_IK", content) ;
	    object.put("DATE", date) ;
	    object.put("TIME", time) ;
	    long id = getSequence(db, "chatrecords"+isGroup, coll);
	    object.put("ORDERID", id) ;
	    if(!"".equals(styleTpl)){
	    	object.put("STYLE", styleTpl) ;
	    }
	    object.put("_id", id) ;
	    return coll.save(object).getN()>0 ;
	}
	public String saveHistoryMessageReturnID(String companyId, String username, String content, String date, String time, String from, String isGroup, String fromCnName, String styleTpl, String fileFlag,Map<String, Map<String, String>> companyUserInitInfo) {
		DB db = mongoManager.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("chatrecords"+isGroup);
		BasicDBObject object = new BasicDBObject() ;
		if("1".equals(isGroup)){
			object.put("GROUPFLAG", username) ;
			object.put("FROMCNNAME", fromCnName) ;
			if (fileFlag == null) {
				fileFlag = "";
			}
			object.put("FILEFLAG", fileFlag); // 记录该条消息关联的文件
			long timestamp = Long.parseLong(date.replaceAll("-", "")+time.replaceAll(":", "")) ;
			object.put("TIMESTAMP", timestamp) ;
		} else {
			
			String CHATFLAG = this.getTwoPersonChatFlag(Integer.parseInt(username), Integer.parseInt(from)) ;
			object.put("CHATFLAG", CHATFLAG) ;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		if(pattern.matcher(from).matches()){
			object.put("FROMUSER", from) ;
		}else{
			if(companyUserInitInfo==null){
				return "";
			}else{
				if(!"fyBot".equals(username)){
					String fromId = companyUserInitInfo.get(from).get("ID");
					if(fromId!=null && fromId.length()>0){
						object.put("FROMUSER", fromId) ;
					}else{
						return "";
					}
				}
			}
		}
		object.put("CONTENT", content.toLowerCase()) ;
		object.put("CONTENT_IK", content) ;
		object.put("DATE", date) ;
		object.put("TIME", time) ;
		long id = getSequence(db, "chatrecords"+isGroup, coll);
		object.put("ORDERID", id) ;
		if(!"".equals(styleTpl)){
			object.put("STYLE", styleTpl) ;
		}
		object.put("_id", id) ;
		coll.save(object).getN() ;
		return id+"";
	}
	
	public boolean initMessageTable(Map<String,String> map) {
		DB db = mongoManager.getDB("company_"+map.get("companyId")) ;
		DBCollection coll = db.getCollection("chatrecords"+map.get("isGroup"));
		BasicDBObject object = new BasicDBObject() ;
		if("1".equals(map.get("isGroup"))){
	    	object.put("GROUPFLAG", map.get("GROUPFLAG")) ;
	    	object.put("FROMCNNAME", map.get("FROMCNNAME")) ;
	    	object.put("FILEFLAG", ""); // 记录该条消息关联的文件
		} else {
	    	object.put("CHATFLAG", map.get("CHATFLAG")) ;
		}
	    object.put("FROMUSER", map.get("FROMUSER")) ;
	    object.put("CONTENT", map.get("CONTENT").toLowerCase()) ;
	    //预留相同内容字段 因为索引数据是river插件将mongdb数据同步的 索引只能建立重复内容字段 满足相同内容不同映射的索引
	    object.put("CONTENT_IK", map.get("CONTENT")) ;
	    object.put("DATE", map.get("DATE")) ;
	    object.put("TIME", map.get("TIME")) ;
	    long id = getSequence(db, "chatrecords"+map.get("isGroup"), coll);
	    object.put("ORDERID", id) ;
	    object.put("STYLE", map.get("STYLE")) ;
	    object.put("_id", id) ;
	    return coll.save(object).getN()>0 ;
	}
	
	/**
	 * 获取历史消息
	 */
	public List<HashMap<String, String>> getHistoryMessage(String companyId,
			String receiver, String username, String isGroup, String limit, String page, String skip, String keyword) {
		List<HashMap<String, String>> msg = new ArrayList<HashMap<String, String>>() ;
		DB db = mongoManager.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("chatrecords"+isGroup);
		HashMap<String, HashMap<String, String>> mapObj = new HashMap<String, HashMap<String, String>>() ;
		List<String> ids = new ArrayList<String>() ;
		try{
			int p = Integer.parseInt(page) ;
			int l = Integer.parseInt(limit) ;
			if("1".equals(isGroup)){
				/** 代表为群聊消息 **/
				BasicDBObject cond = new BasicDBObject();
		        if("".equals(keyword)){
		        	cond.append("GROUPFLAG", receiver);
		        } else {
		        	BasicDBList values = new BasicDBList();  
					values.add(new BasicDBObject("GROUPFLAG", receiver)) ;
					Pattern pattern = Pattern.compile("^"+keyword+"|"+keyword); 
					values.add(new BasicDBObject("CONTENT", pattern)) ;
					cond.put("$and", values) ;
		        }
		        DBCursor cursor = null;
		        if(p == 1){
		        	int k = Integer.parseInt(skip) ;
		        	if(k == 0){
		        		cursor = coll.find(cond).sort(new BasicDBObject("_id", -1)).limit(l) ;
		        	} else {
		        		cursor = coll.find(cond).sort(new BasicDBObject("_id", -1)).skip(k).limit(l) ;
		        	}
		        } else {
		        	cursor = coll.find(cond).sort(new BasicDBObject("_id", -1)).skip((p-1)*l).limit(l) ;
		        }
				HashMap<String, String> map = null ;
				BasicDBObject rs = null ;
				while(cursor.hasNext()){
					rs = (BasicDBObject)cursor.next() ;
					String id = rs.getString("_id") ;
					map = new HashMap<String, String>() ;
					String CONTENT = rs.getString("CONTENT") ;
					CONTENT = CONTENT.replaceAll(" ", "%20");
					String STYLE = rs.getString("STYLE") ;
					if(STYLE!=null){
						CONTENT = "<span%20style='"+STYLE+"'>"+CONTENT+"</span>" ;
					}
					map.put("CONTENT", URLEncoder.encode(CONTENT, "utf-8")) ;
					map.put("DATE", rs.getString("DATE")) ;
					map.put("TIME", rs.getString("TIME")) ;
					map.put("FROMUSER", rs.getString("FROMUSER")) ;
					map.put("FROMCNNAME", rs.getString("FROMCNNAME")) ;
					mapObj.put(id, map) ;
					ids.add(id) ;
				}
			} else {
				/** 代表为私聊消息 **/
				BasicDBObject query = new BasicDBObject();
				String CHATFLAG = this.getTwoPersonChatFlag(Integer.parseInt(username), Integer.parseInt(receiver)) ;
		        if("".equals(keyword)){
		        	query.append("CHATFLAG", CHATFLAG);
		        } else {
		        	BasicDBList values = new BasicDBList();  
					values.add(new BasicDBObject("CHATFLAG", CHATFLAG)) ;
					Pattern pattern = Pattern.compile("^"+keyword+"|"+keyword); 
					values.add(new BasicDBObject("CONTENT", pattern)) ;
					query.put("$and", values) ;
		        }
				DBCursor cursor = null;
				if(p == 1){
					int k = Integer.parseInt(skip) ;
		        	if(k == 0){
		        		cursor = coll.find(query).sort(new BasicDBObject("_id", -1)).limit(l) ;
		        	} else {
		        		cursor = coll.find(query).sort(new BasicDBObject("_id", -1)).skip(k).limit(l) ;
		        	}
		        } else {
		        	cursor = coll.find(query).sort(new BasicDBObject("_id", -1)).skip((p-1)*l).limit(l) ;
		        }
		        HashMap<String, String> map = null ;
				BasicDBObject rs = null ;
				while(cursor.hasNext()){
					rs = (BasicDBObject)cursor.next() ;
					String id = rs.getString("_id") ;
					map = new HashMap<String, String>() ;
					String CONTENT = rs.getString("CONTENT") ;
					CONTENT = CONTENT.replaceAll(" ", "%20");
					String STYLE = rs.getString("STYLE") ;
					if(STYLE!=null){
						CONTENT = "<span%20style='"+STYLE+"'>"+CONTENT+"</span>" ;
					}
					map.put("CONTENT", URLEncoder.encode(CONTENT, "utf-8")) ;
					map.put("DATE", rs.getString("DATE")) ;
					map.put("TIME", rs.getString("TIME")) ;
					map.put("FROMUSER", rs.getString("FROMUSER")) ;
					mapObj.put(id, map) ;
					ids.add(id) ;
				}
			}
			if(ids.size()>0){
				for(int i=ids.size()-1; i>=0 ;i--){
					msg.add(mapObj.get(ids.get(i))) ;
				}
			}
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return msg ;
	}
	
	private String getTwoPersonChatFlag(int userId, int receiverId){
		String CHATFLAG = null ;
		if(userId>receiverId){
			CHATFLAG = userId+"_"+receiverId ;
		} else {
			CHATFLAG = receiverId+"_"+userId ;
		}
		return CHATFLAG ;
	}
	
	/**
     * 获取文件相关的历史消息(群组)
     */
    public List<HashMap<String, String>> getHistoryMessage4File(String companyId,
            String receiver, String fileFlag, String limit, String page, String skip) {
        List<HashMap<String, String>> msg = new ArrayList<HashMap<String, String>>() ;
        DB db = mongoManager.getDB("company_"+companyId) ;
        DBCollection coll = db.getCollection("chatrecords1");
        HashMap<String, HashMap<String, String>> mapObj = new HashMap<String, HashMap<String, String>>() ;
        List<String> ids = new ArrayList<String>() ;
        try{
            int p = Integer.parseInt(page) ;
            int l = Integer.parseInt(limit) ;
            if (fileFlag == null) {
                fileFlag = "";
            }
            
            /** 群聊消息 **/
            Pattern fileFlagPattern = Pattern.compile(fileFlag, Pattern.CASE_INSENSITIVE);
            BasicDBObject cond = new BasicDBObject();
            cond.append("GROUPFLAG", receiver);
            cond.append("FILEFLAG", fileFlagPattern);
            DBCursor cursor = null;
            if(p == 1){
                int k = Integer.parseInt(skip) ;
                if(k == 0){
                    cursor = coll.find(cond).sort(new BasicDBObject("_id", -1)).limit(l) ;
                } else {
                    cursor = coll.find(cond).sort(new BasicDBObject("_id", -1)).skip(k).limit(l) ;
                }
            } else {
                cursor = coll.find(cond).sort(new BasicDBObject("_id", -1)).skip((p-1)*l).limit(l) ;
            }
            HashMap<String, String> map = null ;
            BasicDBObject rs = null ;
            while(cursor.hasNext()){
                rs = (BasicDBObject)cursor.next() ;
                String id = rs.getString("_id") ;
                map = new HashMap<String, String>() ;
                String CONTENT = rs.getString("CONTENT") ;
                CONTENT = CONTENT.replaceAll(" ", "%20");
                String STYLE = rs.getString("STYLE") ;
                if(STYLE!=null){
                    CONTENT = "<span%20style='"+STYLE+"'>"+CONTENT+"</span>" ;
                }
                map.put("CONTENT", URLEncoder.encode(CONTENT, "utf-8")) ;
                map.put("DATE", rs.getString("DATE")) ;
                map.put("TIME", rs.getString("TIME")) ;
                map.put("FROMUSER", rs.getString("FROMUSER")) ;
                map.put("FROMCNNAME", rs.getString("FROMCNNAME")) ;
                map.put("FILEFLAG", rs.getString("FILEFLAG"));
//                mapObj.put(id, map) ;
//                ids.add(id) ;
                msg.add(map);
            }
            // 注释掉，消息记录应该要asc
//            if(ids.size()>0){
//                for(int i=ids.size()-1; i>=0 ;i--){
//                    msg.add(mapObj.get(ids.get(i))) ;
//                }
//            }
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return msg ;
    }
	
	@Override
	public boolean saveGroupcallOver(String companyId, String groupflag, String users){
		DB db = mongoManager.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("groupCallOver");
		String[] array = users.split(",") ;
		for(String user:array){
			BasicDBObject query = new BasicDBObject();
			query.put("user", user);
			query.put("from", groupflag);
			BasicDBObject newDocument =new BasicDBObject();
			newDocument.put("$inc", new BasicDBObject().append("c", 1));
			BasicDBObject ret = (BasicDBObject)coll.findAndModify(query, newDocument);
			if (ret == null){
				BasicDBObject object = new BasicDBObject() ;
				object.put("_id", getSequence(db, "groupCallOver", coll));
				object.put("user", user) ;
				object.put("from", groupflag) ;
				object.put("c", 1) ;
				coll.save(object) ;
			}
		}
		return true ;
	}
	
	public boolean UpdateFileToShareOrNoShare(String msgid, String button,String companyId){
		DB db = mongoManager.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("chatrecords0");
		BasicDBObject object = new BasicDBObject() ;
		object.put("_id",  Integer.parseInt(msgid));
		DBCursor dbObject = coll.find(object) ;
		BasicDBObject object1 = null;
		BasicDBObject newDocument = new BasicDBObject();  
		if(dbObject.hasNext()){
			object1 = (BasicDBObject) dbObject.next();
			newDocument.put("CHATFLAG", object1.getString("CHATFLAG"));
			newDocument.put("FROMUSER", object1.getString("FROMUSER"));
			newDocument.put("DATE", object1.getString("DATE"));
			newDocument.put("TIME", object1.getString("TIME"));  
			newDocument.put("ORDERID", object1.getInt("ORDERID"));  
		}
		BasicDBObject query = new BasicDBObject();  
        query.put("_id", Integer.parseInt(msgid));  

        newDocument.put("CONTENT", button.toLowerCase());  
        newDocument.put("CONTENT_IK", button);  
        
        WriteResult result = coll.update(query, newDocument);
       // System.out.println(result);  
		
		return true ;
		
	}
	
	@SuppressWarnings("deprecation")
	public List<String> getOneGroupUserIds(String companyId, String username, String groupFlag) {
		List<String> userids = new ArrayList<String>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT r.USERID FROM groups g LEFT JOIN groupusersrelation r ON g.id=r.groupid WHERE g.COMPANYID=? AND g.FLAG=?" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(companyId));
			pst.setString(2, groupFlag);
			rs = pst.executeQuery() ;
			while(rs.next()){
				userids.add(rs.getString("USERID")) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return userids;
	}
	@SuppressWarnings("deprecation")
	public List<Map<String, String>> getOneGroupUser(String companyId, String groupFlag) {
		List<Map<String, String>> userids = new ArrayList<Map<String, String>>();
		Map<String, String> useridMap =null;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT g.ID,.g.FLAG,g.GROUPNAME,g.REMARK,g.CREATETIME,r.USERID,r.ISADMIN FROM groups g LEFT JOIN groupusersrelation r ON g.id=r.groupid WHERE g.COMPANYID=? AND g.FLAG=?" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(companyId));
			pst.setString(2, groupFlag);
			rs = pst.executeQuery() ;
			while(rs.next()){
				useridMap=new HashMap<String, String>();
				useridMap.put("id", rs.getString("ID"));
				useridMap.put("flag", rs.getString("FLAG"));
				useridMap.put("groupName", rs.getString("GROUPNAME"));
				useridMap.put("remark", rs.getString("REMARK"));
				useridMap.put("createTime", rs.getString("CREATETIME"));
				useridMap.put("userId", rs.getString("USERID"));
				useridMap.put("isAdmin", rs.getString("ISADMIN"));
				userids.add(useridMap) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return userids;
	}
	
	public Map<String, String> getGroupDetail(String companyId,String groupFlag) {
		Map<String, String> group = new HashMap<String,String>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT g.ID,.g.FLAG,g.GROUPNAME,g.REMARK,g.CREATETIME FROM  groups g WHERE g.COMPANYID =? AND g.FLAG =?" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(companyId));
			pst.setString(2, groupFlag);
			rs = pst.executeQuery() ;
			while(rs.next()){
				group.put("GROUPID", rs.getString("ID"));
				group.put("GROUPFLAG", rs.getString("FLAG"));
				group.put("GROUPNAME", rs.getString("GROUPNAME"));
				group.put("REMARK", rs.getString("REMARK"));
				group.put("CREATETIME", rs.getString("CREATETIME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return group;
	}

	@SuppressWarnings("deprecation")
	public List<HashMap<String, String>> getOneGroupUsers(String companyId, String username, String groupFlag) {
		List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT u.ID,u.USERNAME,u.FULLNAME,u.PORTRAIT FROM USERS u LEFT JOIN groupusersrelation r ON u.id=r.userid LEFT JOIN groups g ON r.groupid=g.id  WHERE u.COMPANYID=? AND u.ENABLED=1 AND g.FLAG=?" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(companyId));
			pst.setString(2, groupFlag);
			rs = pst.executeQuery() ;
			HashMap<String, String> item = null ;
			while(rs.next()){
				item = new HashMap<String, String>();
				item.put("ID", rs.getString("ID")) ;
				item.put("USERNAME", rs.getString("USERNAME")) ;
				item.put("FULLNAME", rs.getString("FULLNAME")) ;
				String PORTRAIT = rs.getString("PORTRAIT") ;
				PORTRAIT = PORTRAIT==null?"":PORTRAIT ;
				item.put("PORTRAIT", PORTRAIT) ;
				users.add(item) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return users;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean deleteGroup(String companyId, String groupflag, String groupid) {
		boolean isOK = false ;
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		ResultSet rs = null ;
		try {
			session = getOpenSession() ;
			conn = session.connection() ;
			conn.setAutoCommit(false);
			/** 1、删除用户与群组的关联数据 **/
			String sql = "DELETE FROM groupusersrelation WHERE GROUPID=?" ;
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.parseInt(groupid));
			isOK = pst.executeUpdate()>0 ;
			if(isOK){
				JdbcUtil.close(rs, pst) ;
				/** 2、删除群组基本信息 **/
				sql = "DELETE FROM groups WHERE ID=?" ;
				pst = conn.prepareStatement(sql) ;
				pst.setInt(1, Integer.parseInt(groupid));
				isOK = pst.executeUpdate()>0 ;
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
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean outGroup(String companyId, String groupid, String userid) {
		boolean isOK = false ;
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		ResultSet rs = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			/** 1、删除用户与群组的关联数据 **/
			String sql = "DELETE FROM groupusersrelation WHERE GROUPID=? AND USERID=?" ;
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.parseInt(groupid));
			pst.setInt(2, Integer.parseInt(userid));
			isOK = pst.executeUpdate()>0 ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return isOK ;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public List<String> getGroupUsersByGroupId(String groupid) {
		List<String> users = new ArrayList<String>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT USERID FROM groupusersrelation WHERE GROUPID=?" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(groupid));
			rs = pst.executeQuery() ;
			while(rs.next()){
				users.add(rs.getString("USERID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return users;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean resetGroup(String companyId, String username,
			String addgroupuserids, String deletegroupuserids, 
			String manageruserid, String groupname, String groupremark,
			String groupflag, String time, String groupid, String changeusers,
			String changeitems) {
		boolean isOK = true ;
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		ResultSet rs = null ;
		try {
			session = getOpenSession() ;
			conn = session.connection() ;
			conn.setAutoCommit(false);
			int id = Integer.parseInt(groupid) ;
			String sql = null ;
			/** 1、插入群组基本信息 **/
			if(Boolean.valueOf(changeitems)){
				sql = "UPDATE GROUPS SET GROUPNAME=?, REMARK=? WHERE ID=?" ;
				pst = conn.prepareStatement(sql);
				pst.setString(1, groupname);
				pst.setString(2, groupremark);
				pst.setInt(3, id);
				isOK = pst.executeUpdate()>0 ;
			}
			if(isOK && Boolean.valueOf(changeusers)){
				JdbcUtil.close(rs, pst) ;
				/** 2、删除提出的用户 **/
				if(deletegroupuserids.length()>0){
					String[] dids = deletegroupuserids.split(",") ;
					sql = "DELETE FROM groupusersrelation WHERE GROUPID=? AND USERID=?" ;
					pst = conn.prepareStatement(sql) ;
					for(String did:dids){
						pst.setInt(1, id);
						pst.setInt(2, Integer.parseInt(did));
						pst.addBatch();
					}
					isOK = pst.executeBatch().length==dids.length ;
				}
				if(isOK){
					JdbcUtil.close(rs, pst) ;
					/** 3、插入群组与用户关联信息 **/
					if(addgroupuserids.length()>0){
						sql = "INSERT INTO groupusersrelation(GROUPID, USERID, JOINTIME, ISADMIN) VALUES(?,?,?,?)" ;
						pst = conn.prepareStatement(sql) ;
						String[] userids = addgroupuserids.split(",") ;
						for(String userid:userids){
							pst.setInt(1, id) ;
							pst.setInt(2, Integer.parseInt(userid)) ;
							pst.setString(3, time) ;
							pst.setInt(4, manageruserid.equals(userid)?1:0) ;
							pst.addBatch();
						}
						//liuwei 此处有问题总返回false修改为一下方式如若出现问题将我写的注释掉将原来的代码放开即可
						//isOK = pst.executeBatch().length==userids.length;
						
						int [] counts = pst.executeBatch();
						isOK = counts.length > 0;
						
					}
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
	
	@SuppressWarnings("deprecation")
	@Override
	public String getFullNamesByIds(String ids) {
		StringBuffer sb = new StringBuffer() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT FULLNAME FROM USERS WHERE ID IN ("+ids+")" ;
			pst = conn.prepareStatement(sql) ;
			rs = pst.executeQuery() ;
			while(rs.next()){
				sb.append(rs.getString("FULLNAME")).append("、") ;
			}
			if(sb.length()>0)sb.deleteCharAt(sb.length()-1) ;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return sb.toString();
	}
	/**luiwei20150916**/
	public List<String> getUserNameByIds(String ids){
		List<String> usernames = new ArrayList<String>(); 
		StringBuffer sb = new StringBuffer() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT USERNAME  FROM USERS WHERE ID IN ("+ids+")" ;
			pst = conn.prepareStatement(sql) ;
			rs = pst.executeQuery() ;
			while(rs.next()){
				usernames.add(rs.getString("USERNAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return usernames;
	}
	
	public Map<String, String> getGroupFlagByClassId(String classId,String companyId){
		Map<String, String> rtnMap = new HashMap<String, String>();
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT ID,FLAG FROM GROUPS WHERE CLASSID=? and COMPANYID=?" ;
			pst = conn.prepareStatement(sql) ;
			pst.setString(1, classId);
			pst.setString(2, companyId);
			rs = pst.executeQuery() ;
			if(rs.next()){
				rtnMap.put("groupid", rs.getString(1));
				rtnMap.put("groupflag", rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return rtnMap;
		
	}
	
	public List<Map<String, String>> getGroupUsersByNotJoin(String companyId, String classId){
		List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
//			String sql = "SELECT ID,EMAIL FROM USERS WHERE CLASSID=? and COMPANYID=? and STATUS=-1" ;
			//liuwei20160111
			String sql="select u.EMAIL , u.ID from company_users cu INNER JOIN users u on cu.USERID  = u.ID and cu.COMPANYID = ? and  cu.CLASSID= ?";
			pst = conn.prepareStatement(sql) ;
			pst.setString(1, companyId);
			pst.setString(2, classId);
			rs = pst.executeQuery() ;
			while(rs.next()){
				Map<String, String> rtnMap = new HashMap<String, String>();
				rtnMap.put("EMAIL", rs.getString(1));
				rtnMap.put("ID", rs.getString(2));
				listMap.add(rtnMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return listMap;
		
	}
	
	public Map<String, Object> getGroupUserIsAdminInfo(String groupid, String groupflag, String companyId){
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;																						//wangwenshuo add排序   最新插入的为群组管理员
			String sql = "SELECT r.USERID FROM groups g LEFT JOIN groupusersrelation r ON g.id=r.groupid WHERE g.COMPANYID=? AND g.FLAG=? and r.ISADMIN=1 order by r.id DESC" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(companyId));
			pst.setString(2, groupflag);
			rs = pst.executeQuery() ;
			if(rs.next()){
				rtnMap.put("userId", rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return rtnMap;
	}
	
	public boolean changeGroupAdmin(String companyId, String username,
			String userid, String tousername, String touserid,String groupid){
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		int index = -1;
		int index1 = -1;
		boolean flag = false;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "update groupusersrelation set ISADMIN=0 where userid=? and GROUPID=?" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(userid));
			pst.setInt(2, Integer.parseInt(groupid));
			index = pst.executeUpdate() ;
			if(index>0){
				String sqltext ="update groupusersrelation set ISADMIN=1 where userid=? and GROUPID=?" ;
				pst = conn.prepareStatement(sqltext) ;
				pst.setInt(1, Integer.parseInt(touserid));
				pst.setInt(2, Integer.parseInt(groupid));
				index1 = pst.executeUpdate() ;
				if(index1>0){
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(null, pst, conn) ;
		}
		return flag;
	}
	
	/**
     * 发表文件的评论
     * @param data  ( companyId/fileFlag/version/userId/content/userName/remoteAddr )
     * @return
     */
    public boolean newFileComment(Map<String, String> data) {
        String companyId = data.get("companyId");
        String fileFlag = data.get("fileFlag");
        String version = data.get("version");
        String content = data.get("content");
        String portrait = data.get("portrait");
        String fullName = data.get("fullName");
        String userId = data.get("userId");
        String userName = data.get("userName");
        String remoteAddr = data.get("remoteAddr");
        String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        
        DB db = mongoManager.getDB("company_"+companyId) ;
        DBCollection coll = db.getCollection("fileComment");
        BasicDBObject object = new BasicDBObject() ;
        object.put("FILEFLAG", fileFlag);
        object.put("VERSION", version);
        object.put("CONTENT", content);
        object.put("PORTRAIT", portrait);
        object.put("FULLNAME", fullName);
        object.put("USERID", userId);
        object.put("USERNAME", userName);
        object.put("CREATEDATE", createDate);
        object.put("REMOTEADDR", remoteAddr);
        long id = getSequence(db, "fileComment", coll);
        object.put("_id", id) ;
        return coll.save(object).getN()>0 ;
    }
    
    public boolean deleteComment(Map<String, String> data) {
        String companyId = data.get("companyId");
        String commentId = data.get("commentId");
        DB db = mongoManager.getDB("company_"+companyId) ;
        DBCollection coll = db.getCollection("fileComment");
        BasicDBObject object = new BasicDBObject() ;
        object.put("_id", Integer.parseInt(commentId));
        return coll.remove(object).getN() > 0;
    }
    
    /**
     * 获取文件评论列表
     */
    public List<Map<String, String>> getFileCommentList(String companyId, String fileFlag, String version, String limit, String page, String skip) {
        List<Map<String, String>> msg = new ArrayList<Map<String, String>>() ;
        DB db = mongoManager.getDB("company_"+companyId) ;
        DBCollection coll = db.getCollection("fileComment");
//        HashMap<String, HashMap<String, String>> mapObj = new HashMap<String, HashMap<String, String>>() ;
        List<String> ids = new ArrayList<String>() ;
        try{
            int p = Integer.parseInt(page);
            int l = Integer.parseInt(limit);
            BasicDBObject cond = new BasicDBObject();
            cond.append("FILEFLAG", fileFlag);
            if (!"".equals(version)) {
                cond.append("VERSION", version);
            }
            DBCursor cursor = null;
            if(p == 1){
                int k = Integer.parseInt(skip) ;
                if(k == 0){
                    cursor = coll.find(cond).sort(new BasicDBObject("_id", 1)).limit(l) ;
                } else {
                    cursor = coll.find(cond).sort(new BasicDBObject("_id", 1)).skip(k).limit(l) ;
                }
            } else {
                cursor = coll.find(cond).sort(new BasicDBObject("_id", 1)).skip((p-1)*l).limit(l) ;
            }
            HashMap<String, String> map = null ;
            BasicDBObject rs = null ;
            while(cursor.hasNext()){
                rs = (BasicDBObject)cursor.next() ;
                String id = rs.getString("_id") ;
                map = new HashMap<String, String>() ;
                map.put("_id", id);
                String CONTENT = rs.getString("CONTENT");
//                CONTENT = CONTENT.replaceAll(" ", "%20");
                map.put("FILEFLAG", rs.getString("FILEFLAG"));
                map.put("VERSION", rs.getString("VERSION"));
//                map.put("CONTENT", URLEncoder.encode(CONTENT, "utf-8")) ;
                map.put("CONTENT", CONTENT) ;
                map.put("CREATEDATE", rs.getString("CREATEDATE")) ;
                map.put("PORTRAIT", rs.getString("PORTRAIT"));
                map.put("FULLNAME", rs.getString("FULLNAME")) ;
                map.put("USERID", rs.getString("USERID")) ;
                map.put("USERNAME", rs.getString("USERNAME")) ;
                map.put("REMOTEADDR", rs.getString("REMOTEADDR")) ;
//                mapObj.put(id, map) ;
//                ids.add(id) ;
                msg.add(map);
            }
            // 注释掉，消息记录应该要asc
//            if(ids.size()>0){
//                for(int i=ids.size()-1; i>=0 ;i--){
//                    msg.add(mapObj.get(ids.get(i))) ;
//                }
//            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return msg ;
    }
    public List<Map<String, String>> getFileCommentListByVersions(
			String companyId, String fileFlag, String version, String limit,
			String page, String skip){
		List<Map<String, String>> msg = new ArrayList<Map<String, String>>() ;
    	DB db = mongoManager.getDB("company_"+companyId) ;
    	DBCollection coll = db.getCollection("fileComment");
    	List<String> ids = new ArrayList<String>() ;
    	try{
    		int p = Integer.parseInt(page);
    		int l = Integer.parseInt(limit);
    		BasicDBObject cond = new BasicDBObject();
    		//cond.append("FILEFLAG", fileFlag);
//    		if (!"".equals(version)) {
//    			cond.append("VERSION", version);
//    		}
    		BasicDBList values = new BasicDBList();
    		for (int i = 0; i < fileFlag.split(",").length; i++) {
    			values.add(fileFlag.split(",")[i]);
    		}
    		cond.put("FILEFLAG", new BasicDBObject("$in", values));
    		DBCursor cursor = null;
    		if(p == 1){
    			int k = Integer.parseInt(skip) ;
    			if(k == 0){
    				cursor = coll.find(cond).sort(new BasicDBObject("_id", 1)).limit(l) ;
    			} else {
    				cursor = coll.find(cond).sort(new BasicDBObject("_id", 1)).skip(k).limit(l) ;
    			}
    		} else {
    			cursor = coll.find(cond).sort(new BasicDBObject("_id", 1)).skip((p-1)*l).limit(l) ;
    		}
    		HashMap<String, String> map = null ;
    		BasicDBObject rs = null ;
    		while(cursor.hasNext()){
    			rs = (BasicDBObject)cursor.next() ;
    			String id = rs.getString("_id") ;
    			map = new HashMap<String, String>() ;
    			map.put("_id", id);
    			String CONTENT = rs.getString("CONTENT");
    			map.put("FILEFLAG", rs.getString("FILEFLAG"));
    			map.put("VERSION", rs.getString("VERSION"));
    			map.put("CONTENT", CONTENT) ;
    			map.put("CREATEDATE", rs.getString("CREATEDATE")) ;
    			map.put("PORTRAIT", rs.getString("PORTRAIT"));
    			map.put("FULLNAME", rs.getString("FULLNAME")) ;
    			map.put("USERID", rs.getString("USERID")) ;
    			map.put("USERNAME", rs.getString("USERNAME")) ;
    			map.put("REMOTEADDR", rs.getString("REMOTEADDR")) ;
    			msg.add(map);
    		}
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	return msg ;
    }

    /**
     * 将公司管理员加入到被禁用用户创建的群组中
     */
	public List<Map<String, String>> addAdminIntoGroups(String userId, String userIdStr, String companyId ) {
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		ResultSet rs = null ;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			session = getOpenSession() ;
			conn = session.connection() ;
			conn.setAutoCommit(false);
			//1.获取所有被禁用户创建的群组
			String sql = "SELECT g.ID groupId,FLAG FROM groups g LEFT JOIN groupusersrelation r ON g.id=r.groupid WHERE r.ISADMIN=1 and g.COMPANYID=? and r.USERID in ("+userIdStr+")" ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(companyId));
			rs = pst.executeQuery() ;
			
			HashMap<String, String> map = null ;
			while(rs.next()){
				map = new HashMap<String, String>();
				map.put("groupId", rs.getString("groupId"));
				map.put("flag", rs.getString("FLAG"));
				list.add(map);
			}
			
			//2.将admin加入到群组中
			sql = "INSERT INTO groupusersrelation(GROUPID, USERID, JOINTIME, ISADMIN) VALUES(?,?,?,?)" ;
	        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	        JdbcUtil.close(rs, pst) ;
			pst = conn.prepareStatement(sql) ;
			for(Map<String, String> m : list){
				pst.setInt(1, Integer.parseInt(m.get("groupId"))) ;
				pst.setInt(2, Integer.parseInt(userId)) ;
				pst.setString(3, time) ;
				pst.setInt(4, 1) ;
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
	 * 添加用户到群组中
	 * @param userId  被添加的用户
	 * @param groupId  群组
	 * @return
	 */
	public boolean addUserIntoGroup(String userId, String groupId) {
		
		/** lujixiang 20151104   添加重复数据判断 **/
		if (checkExistUserIntoGroup(userId, groupId)) {
			return true;
		}
		
		boolean isOK = false ;
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		ResultSet rs = null ;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "INSERT INTO groupusersrelation(GROUPID, USERID, JOINTIME, ISADMIN) VALUES(?,?,?,?)" ;
	        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(groupId)) ;
			pst.setInt(2, Integer.parseInt(userId)) ;
			pst.setString(3, time) ;
			pst.setInt(4, 0) ;
			isOK = pst.executeUpdate()>0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return isOK;
	}


	@Override
	public boolean dropKickoutUserMessage(String companyId, String groupflag,List<String> usernames) {
		
		boolean isOK = false ;
		DB db = mongoManager.getDB("company_"+companyId) ;
		DBCollection coll = db.getCollection("groupNotSeeMessage");
		BasicDBObject cond = new BasicDBObject();
        for(String username: usernames){
        	BasicDBList values = new BasicDBList();
        	values.add(new BasicDBObject("GROUPFLAG", groupflag));
	        values.add(new BasicDBObject("USERNAME", username));
	        cond.put("$and", values); 
	        isOK = coll.remove(cond).getN()>0 ;
	        if(isOK){
				DBCollection dbcoll = db.getCollection("newMessageCount");
				BasicDBObject query = new BasicDBObject();
				    query.put("user", username);
				    query.put("from", groupflag);
				    isOK = dbcoll.remove(query).getN()>0 ;
				    /*dbcoll = db.getCollection("groupCallOver");
			        isOK = dbcoll.remove(query).getN()>0 ;*/
			}
		}
		
	   return isOK ;
	}
	
	
	/**
	 * lujixiang 20151104   检查用户是否已在分类下
	 * @param userId 用户id
	 * @param groupId 分类id
	 */
	@Override
	public boolean checkExistUserIntoGroup(String userId, String groupId){
		
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		
		boolean isExist = false;
		
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT COUNT(id) from groupusersrelation where USERID = ?  AND GROUPID = ? " ;
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1, Integer.parseInt(userId));
			pst.setInt(2, Integer.parseInt(groupId));
			rs = pst.executeQuery() ;
			if(rs.next() && 0 < rs.getInt(1) ){
				
				isExist = true ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return isExist;
	}
	/**
	 * @param CompanyId 用户id
	 */
	@Override
	public List<Map<String, String>> getUserImgByCompanyId(String companyId){
		List<Map<String, String>> retList=new ArrayList<Map<String,String>>();
		Map<String, String> rtnMap = null;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT u.USERNAME,IFNULL(u.PORTRAIT,'apps/onlinefile/templates/ESDefault/images/profle.png') PORTRAIT FROM company_users AS cu LEFT JOIN users AS u ON cu.`USERID`=u.`ID` WHERE  cu.COMPANYID=? AND cu.`STATUS` ='1'" ;
			pst = conn.prepareStatement(sql) ;
			pst.setString(1, companyId);
			rs = pst.executeQuery() ;
			while(rs.next()){
				rtnMap =new HashMap<String,String>();;
				rtnMap.put("USERNAME", rs.getString("USERNAME"));
				rtnMap.put("PORTRAIT", rs.getString("PORTRAIT"));
				retList.add(rtnMap);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return retList;
	}
	
}
