package cn.flying.rest.onlinefile.lucene.dao.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.bson.types.ObjectId;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.flying.rest.admin.entity.AuditLog;
import cn.flying.rest.onlinefile.lucene.dao.LuceneDao;
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

@Repository
public class LuceneDaoImpl extends BaseDaoHibernate implements LuceneDao {

	public LuceneDaoImpl() {
    super(LuceneDaoImpl.class);
    }

    @Autowired
	private MongoManager mongo;
	
	public boolean insert(Map<String, String> fileInfo) {
		//存  文件名，自定义属性的信息，文件的位置，公司的id
		try{
			DB db = mongo.getDB("indexFile");
			DBCollection coll = db.getCollection("files");
			BasicDBObject data = new BasicDBObject();
			 data.append("ID", fileInfo.get("ID"))
			 //.append("FILEID", fileInfo.get("FILEID"))
			 .append("COMPANYID", fileInfo.get("COMPANYID"));
			 coll.insert(data);
			 return true;
		}catch(Exception e){
			return false;
		}
	}

	public boolean delete(String id) {
		try{
			DB db = mongo.getDB("indexFile");
			DBCollection coll = db.getCollection("files");
			BasicDBObject data = new BasicDBObject();
			data.put("_id", new ObjectId(id));
			DBObject dbObj = coll.findOne(data);
			coll.remove(dbObj);
			return true;

		}catch(Exception e){
			return false;
		}
	}

	public List<Map<String, String>> findAll() {
		DB db = mongo.getDB("indexFile");
		DBCollection coll = db.getCollection("files");
		DBCursor cursor = coll.find();
		List<Map<String,String>> lst = new ArrayList<Map<String,String>>();
		BasicDBObject object = null;
		while(cursor.hasNext()){
		  Map<String,String> mapData = new HashMap<String, String>();
          object = (BasicDBObject)cursor.next() ;
          mapData.put("_id", object.getObjectId("_id").toString());
          mapData.put("ID", object.getString("ID"));
          //mapData.put("FILEID", object.getString("FILEID"));
          mapData.put("COMPANYID", object.getString("COMPANYID"));

          lst.add(mapData) ;
	    }
		return lst;
	}
	
	public boolean insertKeyWord(String keyWord) {
		try{
			DB db = mongo.getDB("KEYWORD");
			DBCollection coll = db.getCollection("NOSEARCHKEYWORD");
			BasicDBObject data = new BasicDBObject();
			data.append("keyword", keyWord);
			coll.insert(data);
			return true;
		}catch(Exception e){
			return false;
		}
	}

  @Override
  public List<Map<String, String>> getFilesByCondition(Map<String, String> map) {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    Session session = null ;
    Connection conn = null ;
    PreparedStatement pst = null ;
    ResultSet rs = null ;
    PreparedStatement pst2 = null ;
    ResultSet rs2 = null ;

    try {
        session = getSession();
        conn = session.connection();
        String keyWord = map.get("keyWord");
        String companyId = map.get("companyId");
        String start = map.get("start");
        String limit = map.get("limit");
        String idSeq = map.get("idSeq");
        String createrId = map.get("createrId");
        
        String searchType = map.get("searchType");//1 是文件搜索，2是消息
        String receiver = map.get("receiver");
        String username = map.get("username");
        String isGroup = map.get("isGroup");
        String joindate = map.get("joindate");
        String jointime = map.get("jointime");
        String loginUserId = map.get("loginUserId");
        String orderField = map.get("orderField");
        String orderType = map.get("orderType");
        String type = map.get("type");
        if("1".equals(orderField)){
               orderField = "fileName";
           //  orderField = "pinyin";
        }else if("2".equals(orderField)){
            orderField = "size";
        }else if("3".equals(orderField)){
            orderField = "createtime";
        }else{
            orderField = "createtime";
            orderType = "desc";
        }
        String table  = "files_"+companyId;
        if("myDocument".equals(type)){
               table = "files_user_"+loginUserId;
        }
        StringBuilder sbQuery = new StringBuilder("select * from (select * from ");
        StringBuilder sbCount = new StringBuilder("select count(1) from ");
        StringBuilder sb = new StringBuilder();
        sb.append(" (select *, CONCAT(fileName,'.',type) as fullFileName from "+table+") as s ");
        sb.append(" where ");
        sb.append("isDelete='0' and isLast=1 ");
        if("myDocument".equals(type)){
            sb.append("and isFile=1 ");
        }else if(loginUserId != null && loginUserId.trim().length()>0){
          sb.append("and (creator="+loginUserId+" ");
          sb.append("or openLevel!=3) ");
        }
        if(idSeq !=null && idSeq.trim().length()>0){
            sb.append("and idSeq like '"+idSeq+"%' ");
        }
        if(keyWord!=null && !"".equals(keyWord)){
          keyWord = URLDecoder.decode(keyWord.trim(), "utf-8");
          sb.append("and s.fullFileName like '%"+keyWord+"%' ");
         // sb.append(" and fileVALUE like %"+keyWord+"% ");
        }
        pst2=conn.prepareStatement(sbCount.append(sb).toString());
        sb.append("order by isFile desc,createtime desc ");
        if(start!=null && limit!=null){
            sb.append("limit "+Integer.parseInt(start)+","+Integer.parseInt(limit)+" ");
        }
        //为了防止分页后点击排序 整个搜索全排 所以排序放在分页后
        sb.append(") a order by isFile desc, "+orderField+" "+orderType+" ");
       // System.out.println(sb.toString());
        
        rs2 = pst2.executeQuery();
        if(rs2.next()){
          Map<String, String> firstMap = new HashMap<String, String>();
          firstMap.put("total",rs2.getString(1));
          list.add(firstMap);
        }
        pst = conn.prepareStatement(sbQuery.append(sb).toString());
        rs = pst.executeQuery();
        
        while (rs.next()) {
            Map<String, String> classObj = new HashMap<String, String>();
            String id = rs.getString("id");
            String idseq= StringUtil.string(rs.getString("idSeq"));
            classObj.put("mysqlId", id); // 当前数据id
            classObj.put("id", id); // 当前数据id
            classObj.put("fileId", StringUtil.string(rs.getString("fileId")));
            classObj.put("fileName", StringUtil.string(rs.getString("fileName")));
            classObj.put("userId", StringUtil.string(rs.getString("creator")));
            classObj.put("owner", StringUtil.string(rs.getString("owner")));
            classObj.put("createTime", StringUtil.string(rs.getString("createTime")));
            classObj.put("idSeq", idseq+id+".");
            classObj.put("idSeqSrc", idseq);
            classObj.put("fileVALUE", "");
            classObj.put("type",StringUtil.string(rs.getString("type")));
            classObj.put("version",StringUtil.string(rs.getString("version")));
            classObj.put("classId",StringUtil.string(rs.getString("classId")));
            classObj.put("openlevel",StringUtil.string(rs.getString("openlevel")));
            classObj.put("filesize",StringUtil.string(rs.getString("size")));
            classObj.put("isFile",StringUtil.string(rs.getString("isFile")));
            String rootIdseq = "1.";
            if(!"1.".equals(idseq)){
                rootIdseq = (idseq==null||idseq.length()==0)?"":idseq.substring(0, idseq.indexOf(".", 2))+".";
            }
            classObj.put("rootIdseq", rootIdseq);
            list.add(classObj);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        JdbcUtil.close(rs, pst, conn);
        JdbcUtil.close(rs2, pst2, conn);
    } 
    
    return list;
  }


  @Override
  public List<Map<String, String>> getMsgByCondition(Map<String, String> map) {
    String keyWord = map.get("keyWord");
    String companyId = map.get("companyId");
    String receiver = map.get("receiver");
    String username = map.get("username");
    String isGroup = map.get("isGroup");
    String joindate = map.get("joindate");
    String fileFlag = map.get("fileFlag");
    String jointime = map.get("jointime");

    
    DB db = mongo.getDB("company_"+companyId);
    DBCollection coll = db.getCollection("chatrecords"+isGroup);
    BasicDBObject query = new BasicDBObject();
    if("1".equals(isGroup)){
      if (joindate == null || "".equals(joindate)) {
        query.put("GROUPFLAG", receiver.toLowerCase());
      } else {
          /** 添加时间戳 **/
          long timestamp = Long.parseLong(joindate.replaceAll("-", "")+jointime.replaceAll(":", "")) ;
          query.put("GROUPFLAG", receiver.toLowerCase());
          query.put("TIMESTAMP",  new BasicDBObject("$gte", timestamp));
      }
    }else{
        String CHATFLAG = this.getTwoPersonChatFlag(username, receiver) ;
        query.put("CHATFLAG", CHATFLAG.toLowerCase().replace("@", "%40"));
    }
    if(fileFlag!=null && !"".equals(fileFlag.trim())){
        query.put("FILEFLAG", fileFlag);
    }
  
    BasicDBList conOr = new BasicDBList();
    if(keyWord!=null && !"".equals(keyWord.trim())){
        keyWord = keyWord.replaceAll("\\.", "\\\\.");
        //所有内容相关的搜索
        Pattern pattern = Pattern.compile(keyWord.toLowerCase());
        Pattern pattern2 = Pattern.compile(keyWord);
        conOr.add( new BasicDBObject("CONTENT", pattern));
        conOr.add( new BasicDBObject("DATE", pattern2));
        conOr.add( new BasicDBObject("FROMCNNAME", keyWord));
    }
    
    int start = Integer.valueOf(map.get("start") + "");
    int limit = Integer.valueOf(map.get("limit") + "");
    if (!conOr.isEmpty()) {
        query.put("$or", conOr);
    }
    /** 分页 **/
    DBCursor cursor = null;
    /** 将获取日志数据按照id排序**/
    if (start == -1) {
        cursor = coll.find(query).sort(new BasicDBObject("_id", -1));
    } else {
        if (start == 0) {
            cursor = coll.find(query).sort(new BasicDBObject("_id", -1))
                    .limit(limit);
        } else {
            cursor = coll.find(query).sort(new BasicDBObject("_id", -1))
                    .skip(start).limit(limit);
        }
    }
    BasicDBObject object = null;
    List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    try {
    while (cursor.hasNext()) {
        object = (BasicDBObject) cursor.next();
        Map<String,String> map1 = new HashMap<String,String>();
        String CONTENT = object.getString("CONTENT_IK")+"";
        if( object.getString("CONTENT_IK")==null){
          CONTENT =  object.getString("CONTENT")+"" ;
        }
        map1.put("CONTENT", CONTENT);
        map1.put("STYLE",  object.getString("STYLE"));
        map1.put("DATE",  object.getString("DATE")) ;
        map1.put("TIME",  object.getString("TIME")) ;
        map1.put("ID",    object.getString("_id")) ;
        map1.put("FROMUSER",  object.getString("FROMUSER")) ;
        if("1".equals(isGroup)){
            map1.put("FROMCNNAME",object.getString("FROMCNNAME")) ;
        }
        list.add(map1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }
  private String getTwoPersonChatFlag(String userId, String receiverId){
    String CHATFLAG = null ;
    if(Integer.parseInt(userId)>Integer.parseInt(receiverId)){
        CHATFLAG = userId+"_"+receiverId ;
    } else {
        CHATFLAG = receiverId+"_"+userId ;
    }
    return CHATFLAG ;
}

}
