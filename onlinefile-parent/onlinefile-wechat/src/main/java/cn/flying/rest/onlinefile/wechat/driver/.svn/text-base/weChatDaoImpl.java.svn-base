package cn.flying.rest.onlinefile.wechat.driver;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;
import cn.flying.rest.onlinefile.utils.JdbcUtil;
import cn.flying.rest.onlinefile.utils.MongoManager;
import cn.flying.rest.onlinefile.utils.StringUtil;
import cn.flying.rest.onlinefile.wechat.dao.weChatDao;
@Repository
@SuppressWarnings("rawtypes")
public class weChatDaoImpl extends BaseDaoHibernate implements weChatDao {
	
	
	/**    20151013 liuhezeng添加 log4j日志管理    **/
	private static final Logger logger = Logger.getLogger(weChatDaoImpl.class);
	

	@SuppressWarnings("unchecked")
	public weChatDaoImpl() {
		super(weChatDaoImpl.class);
	}
	
	@Resource(name = "mongoManager")
	MongoManager mongoManager ;

	public List<HashMap<String, String>> getCommnunityMessage(int page,int limit) {
		List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		String maxcount="0";
		HashMap<String, String> item = null ;
		try {
			session = getSession() ;
			conn = session.connection();
			String sql="SELECT count(1) FROM community_msg INNER JOIN users on user_name=USERNAME";
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if(rs.next()){
				maxcount=rs.getString("count(1)");
			}
			//sql = "SELECT user_id,user_name,fullname,c_title,publish_date,portrait,publish_type,total,last_reply_time,last_reply_name FROM community_msg INNER JOIN users on user_name=USERNAME ORDER BY publish_date DESC LIMIT ?,?";
			//2015.09.10修改sql 获取用户点赞总数以及点赞用户集
			sql = "SELECT user_id,user_name,fullname,c_title,publish_date,portrait,publish_type,total,last_reply_time,last_reply_name,d.praisecount,d.fullnames,d.userids FROM community_msg INNER JOIN users on user_name=USERNAME LEFT JOIN  (SELECT cp.CARDID,COUNT(u.FULLNAME) AS praisecount, GROUP_CONCAT(u.FULLNAME ORDER BY cp.ID DESC) AS fullnames, GROUP_CONCAT(u.ID)  AS userids  FROM community_praise AS cp LEFT JOIN users AS u ON u.ID=cp.USERID  GROUP BY cp.CARDID) AS d ON d.CARDID=user_id ORDER BY publish_date DESC LIMIT ?,?";
			pst = conn.prepareStatement(sql) ;
			pst.setInt(1,page);
			pst.setInt(2,limit);
			rs = pst.executeQuery() ;
			while(rs.next()){
				item = new HashMap<String, String>();
				item.put("userid", rs.getString("user_id"));
				item.put("user_name", rs.getString("user_name"));
				item.put("username", rs.getString("fullname"));
				item.put("title", rs.getString("c_title"));
				item.put("date", rs.getString("publish_date"));
				item.put("imgurl", rs.getString("portrait")!=null && rs.getString("portrait")!=""?rs.getString("portrait"):"apps/onlinefile/templates/ESDefault/images/profle.png");
				item.put("pubtype",rs.getString("publish_type"));
				item.put("total",String.valueOf(rs.getInt("total")));
				item.put("maxcount", maxcount);
				item.put("maxpldate", rs.getString("last_reply_time"));
				item.put("lastReplyName",rs.getString("last_reply_name"));
				item.put("praisecount",rs.getString("praisecount")!=null?rs.getString("praisecount"):"");
				item.put("fullnames",rs.getString("fullnames")!=null?rs.getString("fullnames"):"");
				item.put("userids",rs.getString("userids")!=null?rs.getString("userids"):"");
				users.add(item) ;
			}
		} catch (SQLException e) {
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return users;
	}
	
	public List<HashMap<String, String>> getCommnunityTypeMessage(String type,int page,String username,int limit) {
		List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		String maxcount="0";
		String sql = null;
		
		try {
			session = getSession();
			conn = session.connection();
			if(username != null){
				sql="select COUNT(1) from community_msg where user_name = ?";
			}else{
				 sql="SELECT count(1) FROM community_msg where publish_type=?";
			}
			pst = conn.prepareStatement(sql);
			
			if(username != null){
				pst.setString(1,username);
			}else{
				pst.setString(1, type);
			}
			rs = pst.executeQuery();
			if(rs.next()){
				maxcount=rs.getString("count(1)");
			}
			if(username != null){
				//sql = "SELECT c.c_title,c.total,c.user_name,c.publish_date,c.user_id,c.publish_type,last_reply_time,last_reply_name,u.PORTRAIT, u.FULLNAME ,ci.info from community_msg c INNER JOIN users u on c.user_name = u.USERNAME LEFT JOIN commnuity_info ci ON c.user_id=ci.item_id  WHERE user_name=? ORDER BY publish_date DESC limit ?,?";
				//2015.09.10
				sql = "SELECT c.c_title,c.total,c.user_name,c.publish_date,c.user_id,c.publish_type,last_reply_time,last_reply_name,u.PORTRAIT, u.FULLNAME ,ci.info ,d.praisecount,d.fullnames,d.userids from community_msg c INNER JOIN users u on c.user_name = u.USERNAME LEFT JOIN commnuity_info ci ON c.user_id=ci.item_id LEFT JOIN  (SELECT cp.CARDID,COUNT(u.FULLNAME) AS praisecount,GROUP_CONCAT(u.FULLNAME ORDER BY cp.ID DESC) AS fullnames, GROUP_CONCAT(u.ID)  AS userids  FROM community_praise AS cp LEFT JOIN users AS u ON u.ID=cp.USERID  GROUP BY cp.CARDID) AS d ON d.CARDID=c.user_id  WHERE user_name=? ORDER BY publish_date DESC limit ?,?";
			}else{
				sql = "SELECT user_id,user_name,fullname,c_title,publish_date,portrait,publish_type,total,last_reply_time,last_reply_name,info,d.praisecount,d.fullnames,d.userids FROM community_msg INNER JOIN users on user_name=USERNAME LEFT JOIN commnuity_info ON user_id=item_id LEFT JOIN  (SELECT cp.CARDID,COUNT(u.FULLNAME) AS praisecount,GROUP_CONCAT(u.FULLNAME ORDER BY cp.ID DESC) AS fullnames, GROUP_CONCAT(u.ID)  AS userids  FROM community_praise AS cp LEFT JOIN users AS u ON u.ID=cp.USERID  GROUP BY cp.CARDID) AS d ON d.CARDID=user_id where publish_type=? ORDER BY publish_date DESC LIMIT ?,?";
			}
			pst = conn.prepareStatement(sql) ;
			if(username != null){
				pst.setString(1, username);
				pst.setInt(2, page);
				pst.setInt(3, limit);
			}else{
				pst.setString(1,type); 
				pst.setInt(2,page);
				pst.setInt(3,limit);
			}
			rs = pst.executeQuery() ;
			HashMap<String, String> item = null ;
			while(rs.next()){
				item = new HashMap<String, String>();
				item.put("userid", rs.getString("user_id")) ;
				item.put("user_name", rs.getString("user_name")) ;
				item.put("username", rs.getString("fullname")) ;
				item.put("title", rs.getString("c_title")) ;
				item.put("date", rs.getString("publish_date")) ;
				item.put("imgurl", rs.getString("portrait")!=null && rs.getString("portrait")!=""?rs.getString("portrait"):"apps/onlinefile/templates/ESDefault/images/profle.png");
				item.put("maxpldate", rs.getString("last_reply_time"));
				item.put("lastReplyName", rs.getString("last_reply_name"));
				item.put("total", rs.getString("total"));
				item.put("info", rs.getString("info"));
				item.put("praisecount", rs.getString("praisecount")!=null?rs.getString("praisecount"):"");
				item.put("fullnames", rs.getString("fullnames")!=null?rs.getString("fullnames"):"");
				item.put("maxcount", maxcount);
				item.put("userids", rs.getString("userids")!=null?rs.getString("userids"):"");
				users.add(item) ;
			}
		} catch (Exception e) {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return users;
	}
	
	public boolean publisCommnunity(String username, String time,String title,String userinfo,String realtitle,String pubtype) {
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		ResultSet rs = null ;
		boolean thiskey=true;
		int maxid =0;
		try {
			if(thiskey){
			thiskey=false;
			session = getOpenSession() ;
			conn = session.connection();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO community_msg(user_name,c_title,publish_date,publish_type) values (?,?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.setString(2,title);
			pst.setString(3,time);
			pst.setString(4,pubtype);
			pst.executeUpdate();
			
			sql = "select max(user_id) from community_msg";
			pst = conn.prepareStatement(sql);
			rs=pst.executeQuery();
			while(rs.next()){
				maxid=rs.getInt("max(user_id)");
			}
			sql="INSERT into commnuity_info(item_id,title,info) values (?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, maxid);
			pst.setString(2,realtitle);
			pst.setString(3,userinfo);
			pst.executeUpdate();
			conn.commit();
			thiskey=true;
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
			}
			JdbcUtil.close(rs, pst, conn,session) ;
		}
		return true;
	}
		public Map<String,String> getCommunityArticle(String user){
		HashMap<String, String> users = new HashMap<String, String>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			//String sql = "SELECT user_id,user_name,PORTRAIT,publish_date,title,info,total,fullname,publish_type,praisecount FROM commnuity_info,community_msg INNER JOIN users on USERNAME=user_name WHERE user_id=item_id and item_id=?";
			//2015.09.10修改sql
			String sql = "SELECT user_id,user_name,PORTRAIT,publish_date,title,info,total,fullname,publish_type,d.praisecount,d.fullnames,d.userids FROM commnuity_info,community_msg INNER JOIN users on USERNAME=user_name LEFT JOIN (SELECT cp.CARDID,COUNT(u.FULLNAME) AS praisecount,GROUP_CONCAT(u.FULLNAME ORDER BY cp.ID DESC) AS fullnames, GROUP_CONCAT(u.ID)  AS userids  FROM community_praise AS cp LEFT JOIN users AS u ON u.ID=cp.USERID  GROUP BY cp.CARDID) AS d ON d.CARDID=user_id WHERE user_id=item_id and item_id=?";
			
			pst = conn.prepareStatement(sql) ;
			pst.setString(1,user);
			rs = pst.executeQuery() ;
			while(rs.next()){
				users.put("user_name",rs.getString("user_name"));
				users.put("publish_date",rs.getString("publish_date"));
				users.put("title",rs.getString("title"));
				users.put("info",rs.getString("info"));
				users.put("real_name", rs.getString("fullname"));
				users.put("pubtype", rs.getString("publish_type"));
				users.put("imgurl", rs.getString("PORTRAIT")!=null && rs.getString("portrait")!=""?rs.getString("PORTRAIT"):"apps/onlinefile/templates/ESDefault/images/profle.png");
				users.put("count", rs.getString("total"));
				users.put("cardId", rs.getString("user_id"));
				users.put("praisecount", rs.getString("praisecount")!=null?rs.getString("praisecount"):"");
				users.put("fullnames", rs.getString("fullnames")!=null?rs.getString("fullnames"):"");
				users.put("userids", rs.getString("userids")!=null?rs.getString("userids"):"");
			}
		} catch (SQLException e) {
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return users;
		
	}
	public List<HashMap<String, String>> getReplylist(String pl_context_id,int page ,int limit){
		List<HashMap<String, String>> reply = new ArrayList<HashMap<String, String>>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
		//	String sql = "SELECT pl_id,pl_userid,pl_context_id,pl_name,pl_date,pl_info,pl_imgurl,pl_callback FROM community_callback where pl_context_id=?";
			String sql = "SELECT pl_id,pl_username,pl_context_id,pl_name,pl_date,pl_info,pl_imgurl,PORTRAIT FROM community_callback ca, users us WHERE ca.pl_username = us.username and pl_context_id=?";
			if(limit != 0){
				sql = "SELECT pl_id,pl_username,pl_context_id,pl_name,pl_date,pl_info,pl_imgurl,PORTRAIT FROM community_callback ca, users us WHERE ca.pl_username = us.username and pl_context_id=? LIMIT ?,?";
			}
		    pst = conn.prepareStatement(sql) ;
			pst.setString(1, pl_context_id);
			if(limit != 0){
				pst.setInt(2, page);
				pst.setInt(3, limit);
			}
			rs = pst.executeQuery() ;
			HashMap<String, String> item = null ;
			while(rs.next()){
				item = new HashMap<String, String>();
				item.put("pl_id",rs.getString("pl_id"));
				item.put("pl_username",rs.getString("pl_username"));
				item.put("pl_context_id", rs.getString("pl_context_id")) ;
				item.put("pl_name", rs.getString("pl_name")) ;
				item.put("pl_date", rs.getString("pl_date")) ;
				item.put("pl_info", rs.getString("pl_info")) ;
				item.put("pl_imgurl", rs.getString("PORTRAIT")!=null?rs.getString("PORTRAIT"):"apps/onlinefile/templates/ESDefault/images/profle.png") ;
				reply.add(item) ;
			}
		} catch (SQLException e) {
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return reply;
	}
	
	//根据帖子id获取发帖人id
	public String getUserIdByCardId(String cardId){
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		String id = null;
		try{
		session = getSession() ;
		conn = session.connection() ;
		String sql = "select user_name from community_msg where user_id = ?";
		pst = conn.prepareStatement(sql) ;
		pst.setString(1, cardId);
		rs = pst.executeQuery();
		if(rs.next()){
			id = rs.getString("user_name");
		}
		
		}catch(SQLException e){
			logger.error(e.getMessage());
		}finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return id;
		
		
	}
	
	public boolean showReplylist(String pl_context_id,String pl_name,String pl_info,String replyUserName){
		boolean isOK = false ;
		PreparedStatement pst = null ;
		Connection conn = null ;
		Session session = null ;
		ResultSet rs = null ;
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(date);
		String realname ="noname";
		try {
			session = getOpenSession() ;
			conn = session.connection() ;
			conn.setAutoCommit(false);
			String sql = "select * from users where USERNAME=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1,pl_name);
			rs=pst.executeQuery();
			while(rs.next()){
				realname=rs.getString("fullname");
			}
			sql = "INSERT INTO community_callback(pl_context_id,pl_name,pl_date,pl_info,pl_username,pl_replyname) values (?,?,?,?,?,?) ";
			pst = conn.prepareStatement(sql);
			pst.setString(1, pl_context_id);
			pst.setString(2,realname);
			pst.setString(3,time);
			pst.setString(4, pl_info);
			pst.setString(5, pl_name);
			pst.setString(6, replyUserName);
			isOK = pst.executeUpdate()>0 ;
			
			sql = "UPDATE community_msg set last_reply_time = ? ,last_reply_name = ? WHERE user_id = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1,time);
			pst.setString(2,pl_name);
			pst.setInt(3,Integer.parseInt(pl_context_id));
			isOK = pst.executeUpdate()>0;
			
		//	sql = "update community_msg SET total = total+1  where user_id = ?";
			sql="UPDATE community_msg SET total = IFNULL(total+1, 0) WHERE user_id = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, Integer.parseInt(pl_context_id));
			isOK = pst.executeUpdate()>0;
			conn.commit();
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
			}
			JdbcUtil.close(rs, pst, conn,session) ;
		}
		return isOK ;
		
	}
/*	public List<HashMap<String, String>> getCommnunityuserlist(String username) {
		List<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		try {
			session = getSession() ;
			conn = session.connection() ;
			String sql = "SELECT * FROM community_msg where user_name=? ORDER BY publish_date DESC";
			pst = conn.prepareStatement(sql) ;
			pst.setString(1,username);
			rs = pst.executeQuery() ;
			HashMap<String, String> item = null ;
		//	boolean nonecheck = true;
			while(rs.next()){
			//	nonecheck=false;
				item = new HashMap<String, String>();
				item.put("userid", rs.getString("user_id")) ;
				item.put("username", rs.getString("user_name")) ;
				item.put("title", rs.getString("c_title")) ;
				item.put("date", rs.getString("publish_date")) ;
				users.add(item) ;
			}
			if(nonecheck){
				item = new HashMap<String, String>();
				item.put("userid","-") ;
				item.put("username","-") ;
				item.put("title", "没有此类信息") ;
				item.put("date","-") ;
				users.add(item) ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs, pst, conn) ;
		}
		return users;
	}*/
	public boolean deleteCommunityuserinfo(String userid) {
		HashMap<String, String> users = new HashMap<String, String>() ;
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		boolean t1 = false;
		boolean t2 = false;
		try {
			session = getOpenSession() ;
			conn = session.connection() ;
			conn.setAutoCommit(false);
			String sql = "delete from community_msg where user_id=?";
			pst = conn.prepareStatement(sql) ;
			pst.setString(1,userid);
			t1=pst.executeUpdate()==1;
			if(t1){
				sql = "delete from commnuity_info where item_id=?";
				pst = conn.prepareStatement(sql) ;
				pst.setString(1,userid);
				t2=pst.executeUpdate()==1;
				if(t2){
					sql = "delete from community_callback where pl_context_id=?";
					pst = conn.prepareStatement(sql) ;
					pst.setString(1,userid);
					pst.executeUpdate();
				}else{
					conn.rollback();
					return false;
				}
				conn.commit();
				return true;
			}else{
				conn.rollback();
				return false;
			}
		} catch (SQLException e) {
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
			}
			JdbcUtil.close(rs, pst, conn,session) ;
		}
		return false;
	}
	
	public boolean deleteComment(String pl_id,String username,int contextid) {
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		boolean flag = false;
		try {
			session = getOpenSession();
			conn = session.connection();
			conn.setAutoCommit(false);
//			String sql = "DELETE FROM community_callback WHERE pl_id=? and pl_userid=?"; 
			String sql = "DELETE FROM community_callback WHERE pl_id=?"; 
			pst = conn.prepareStatement(sql);
			pst.setString(1, pl_id);
		//	pst.setInt(2, pl_userid);
			flag = pst.executeUpdate() > 0;
			
			if(flag){
			//	sql = "update community_msg SET total = total-1,last_reply_name = ?,last_reply_time = ? where user_id = ?";
				sql = "update community_msg SET total = total-1 where user_id = ?";
				pst = conn.prepareStatement(sql);
			//	pst.setString(1, null);
			//	pst.setString(2, null);
				pst.setInt(1, contextid);
				flag = pst.executeUpdate()>0;
			}else{
				conn.rollback();
			}
			conn.commit();
		} catch (Exception e) {
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		}finally{
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
			}
			JdbcUtil.close(rs, pst, conn,session) ;
		}
		return flag;
	}
	/*public List<HashMap<String, String>> getMyCommunity(String username,int pageNo) {
		
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		List<HashMap<String, String>> title = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = null;
		Integer count = 0;
		try {
			session = getSession();
			conn = session.connection();
			String sql="select COUNT(1) from community_msg where user_name = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1,username);
			rs = pst.executeQuery();
			if(rs.next()){
				count = rs.getInt(1);
			}
			sql = "SELECT c.c_title,c.total,c.publish_date,c.user_id,c.publish_type,last_reply_name,last_reply_time,u.PORTRAIT, u.FULLNAME from community_msg c INNER JOIN users u on c.user_name = u.USERNAME  WHERE user_name=? ORDER BY publish_date,last_reply_time DESC limit ?,8";
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.setInt(2, pageNo);
			rs = pst.executeQuery();
			while (rs.next()) {
				map = new HashMap<String, String>();
				map.put("ctitle", rs.getString("c_title"));
				map.put("pubdate", rs.getString("publish_date"));
				map.put("headimg", rs.getString("PORTRAIT"));
				map.put("fullname", rs.getString("FULLNAME"));
				map.put("lastReplyName",rs.getString("last_reply_name"));
				map.put("userid", String.valueOf(rs.getInt("user_id")));
				map.put("total", String.valueOf(rs.getInt("total")));
				map.put("pubtype", rs.getString("publish_type"));
				map.put("count", count.toString());
				map.put("maxpldate",rs.getString("last_reply_time"));
				title.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JdbcUtil.close(rs, pst, conn) ;
		}
		return title;
	}*/

	public List<HashMap<String, String>> editComunityContext(int userid) {
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		List<HashMap<String, String>> contextList = new ArrayList<HashMap<String,String>>();
		try {
			session = getSession();
			conn = session.connection();
			String sql = "select title, info from commnuity_info where item_id = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1,userid);
			rs= pst.executeQuery();
			while(rs.next()){
				HashMap<String, String> context = new HashMap<String, String>();
				context.put("info", rs.getString("info"));
				context.put("title", rs.getString("title"));
				contextList.add(context);
			}
			
		} catch (Exception e) {
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		}finally{
			JdbcUtil.close(rs, pst, conn) ;
		}
		return contextList;
	}
	
	/**更新帖子**/
	public boolean updateCard(String cardId,String title,String userinfo) {
		
		boolean flag = false;
		 String sql = "update commnuity_info set title =?,info=? where item_id = ?";
	        Session session = null ;
	        Connection conn = null ;
	        PreparedStatement pst = null ;
	        try {
	            session = getSession();
	            conn = session.connection();
	            pst = conn.prepareStatement(sql);
	            pst.setString(1, title);
	            pst.setString(2, userinfo);
	            pst.setString(3, cardId);
	            int num = pst.executeUpdate();
	            flag = (num > 0);
	        } catch (SQLException e) {
	        	/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
	        } finally {
	            JdbcUtil.close(pst, conn);
	        }
		return flag;
	}
	
	/**更新贴子详细信息**/
	public boolean updateCommunity(String cardId,String title, String time,String publishType) {
			boolean flag = false;
			String sql = "update community_msg set c_title = ?, publish_date = ?,publish_type=? where user_id = ?";
	        Session session = null ;
	        Connection conn = null ;
	        PreparedStatement pst = null ;
	        try {
	            session = getSession();
	            conn = session.connection();
	            pst = conn.prepareStatement(sql);
	            pst.setString(1, title);
	            pst.setString(2,time);
	            pst.setString(3,publishType);
	            pst.setString(4, cardId);
	            int num = pst.executeUpdate();
	            flag = (num > 0);
	        } catch (SQLException e) {
	        	/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
	        } finally {
	            JdbcUtil.close(pst, conn);
	        }
		return flag;
	}
	

	public List<Map<String, String>> getLastReplyName(List<String> userids) {
		PreparedStatement pst = null ;
		ResultSet rs = null ;
		Connection conn = null ;
		Session session = null ;
		HashMap<String,String> info = null;
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(!userids.isEmpty()&& userids.size()>0){
			try {
				session = getSession();
				conn = session.connection();
				String paeram = StringUtil.list2String2(userids,"'",",");
				StringBuilder builder = new StringBuilder();
				builder.append("select FULLNAME,USERNAME from users where username in("+paeram+")");
				pst = conn.prepareStatement(builder.toString());
				rs = pst.executeQuery();
				while(rs.next()){
					info = new HashMap<String, String>();
					info.put("fullname",rs.getString("fullname"));
					info.put("username",rs.getString("username"));
					list.add(info);
				}
			} catch (Exception e) {
				/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
			}finally{
				JdbcUtil.close(rs, pst, conn) ;
			}
			
		}
		return list;
	}

	/**点赞/取消赞**/
	@Override
	public boolean praiseCard(String cardId, String userId, boolean status) {
		
		 	boolean flag = false;
	        String sql = "";
	        String sqlInsert = "insert into community_praise (cardid,userid) values(?,?)";
	        String sqlDelete = "delete from community_praise where cardid=? and userid=?";
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
	            pst.setString(1, cardId);
	            pst.setString(2, userId);
	            int num = pst.executeUpdate();
	            flag = (num > 0);
	        } catch (SQLException e) {
	        	/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
	        } finally {
	            JdbcUtil.close(pst, conn);
	        }
	        return flag;
	}
	/**更新赞数**/
	@Override
	public boolean praiseCountUpdate(String cardId, String userId,boolean status) {
		boolean flag = false;
        String sql = "update community_msg set praisecount = IFNULL(praisecount,0)+"+(status?1:-1)+" where user_id=?";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, cardId);
            int num = pst.executeUpdate();
            flag = (num > 0);
        } catch (SQLException e) {
        	/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
	}
	
	/**获取用户是否点赞**/
	public boolean getUserIsPraise(String user, String plId){
		boolean flag = false;
		String sql = "select id from community_praise where CARDID = ? and USERID = ?";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, plId);
            rs = pst.executeQuery();
            flag = rs.next();
        } catch (SQLException e) {
        	/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
	}
	

	@Override
	public boolean deleteCommunityPraise(String user) {
		boolean flag = false;
        String sql = "delete from community_praise where cardid  = ?";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, user);
            int num = pst.executeUpdate();
            flag = (num > 0);
        } catch (SQLException e) {
        	/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return flag;
	}
	
	//获取用回复评论条数
	@Override
	public int getCountReplyInfo(String username) {
	//	String sql  = "SELECT count(pl_state) FROM community_callback INNER JOIN community_msg ON user_id = pl_context_id WHERE pl_state = 0 AND pl_username !=? AND user_name=? and pl_replyname = ?";
		String sql = "select count(pl_state) from community_callback c where c.pl_replyname= ? and pl_state=0 and pl_username != ?";
		Session session = null ;
	     Connection conn = null ;
	     PreparedStatement pst = null ;
	     ResultSet rs = null;
	     int count = 0;
	     try {
	            session = getSession();
	            conn = session.connection();
	            pst = conn.prepareStatement(sql);
	            pst.setString(1, username);
	            pst.setString(2, username);
	            rs = pst.executeQuery();
	            if(rs.next()){
	            	count = rs.getInt(1);
	            }
	        } catch (SQLException e) {
	        	/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
	        } finally {
	            JdbcUtil.close(pst, conn);
	        }
	     
		return count+getcommentCount(username);
	}
	
	//获取用户评论条数
	public int getcommentCount(String userName){
		String sql="SELECT count(c.pl_state) FROM community_callback c RIGHT JOIN community_msg m ON user_id = pl_context_id and c.pl_username != ? WHERE m.user_name = ? AND c.pl_replyname ='' AND pl_state = 0";
		Session session = null ;
	     Connection conn = null ;
	     PreparedStatement pst = null ;
	     ResultSet rs = null;
	     int count = 0;
	     try {
	            session = getSession();
	            conn = session.connection();
	            pst = conn.prepareStatement(sql);
	            pst.setString(1, userName);
	            pst.setString(2, userName);
	            rs = pst.executeQuery();
	            if(rs.next()){
	            	count = rs.getInt(1);
	            }
	        } catch (SQLException e) {
	        	/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
	        } finally {
	            JdbcUtil.close(pst, conn);
	        }
		return count;
	}
	
	
	//获取用户社区，当前用户帖子的帖子回复列表
	public List<Map<String, String>> getCallBackNewMessage(int page,int limit,String userName){
		 //String sql = "SELECT c_title,user_id,publish_type,pl_date,pl_name,pl_id FROM community_callback INNER JOIN community_msg ON user_id = pl_context_id WHERE pl_state = 0 AND pl_username != ? and pl_replyname =? ORDER BY pl_date desc LIMIT ?,?";
		// String sql = "SELECT m.c_title,m.user_id,m.publish_type,c.pl_date,c.pl_name,c.pl_id FROM community_callback c LEFT JOIN community_msg m ON c.pl_context_id = m.user_id WHERE pl_state = 0 AND c.pl_username = ? AND c.pl_replyname = '' AND m.user_name != ? ORDER BY pl_date desc LIMIT ?,?";
		String sql = "select m.c_title,m.user_id,m.publish_type,c.pl_date,c.pl_name,c.pl_id from community_callback c LEFT JOIN community_msg m on c.pl_context_id = m.user_id where c.pl_state=0 and ((c.pl_username!=m.user_name and m.user_name= ? and c.pl_replyname='') or c.pl_replyname= ?) ORDER BY c.pl_date desc LIMIT ?,?"; 
		Session session = null ;
	     Connection conn = null ;
	     PreparedStatement pst = null ;
	     ResultSet rs = null;
	 	 
	 	 List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
	     try {
	            session = getSession();
	            conn = session.connection();
	            pst = conn.prepareStatement(sql);
	            pst.setString(1, userName);
	            pst.setString(2, userName);
	            pst.setInt(3, page);
	            pst.setInt(4, limit);
	            rs = pst.executeQuery();
	            while(rs.next()){
	            	Map<String,String> maps = new HashMap<String, String>();
	            	maps.put("title",rs.getString("c_title"));
	            	maps.put("pl_date",rs.getString("pl_date"));
	            	maps.put("pl_name",rs.getString("pl_name"));
	            	maps.put("pubtype",rs.getString("publish_type"));
	            	maps.put("userid",rs.getString("user_id"));
	            	maps.put("plId",rs.getString("pl_id"));
	            	datas.add(maps);
	            }
	        } catch (SQLException e) {
	        	/**   20151013 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
	        } finally {
	            JdbcUtil.close(pst, conn);
	        }
		return datas;
	}
	/**更新消息状态**/
	public void updateCommunityMessageState(String user,String plId,String userName){
		
		String sql = "select c.pl_id from community_callback c LEFT JOIN community_msg m on c.pl_context_id = m.user_id where c.pl_state=0 and ((c.pl_username!=m.user_name and m.user_name= ? and c.pl_replyname='') or c.pl_replyname= ?)";
		Session session = null ;
	    Connection conn = null ;
	    ResultSet rs = null;
	    PreparedStatement pst = null ;
	    Integer id = null;
	    StringBuilder param = new StringBuilder();
	    try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, userName);
            pst.setString(2, userName);
            rs = pst.executeQuery();
            if(rs.next()){
            	id = rs.getInt(1);
            }
            sql = "update community_callback c set pl_state = 1 where pl_id = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1,id);
            pst.executeUpdate();
        } catch (SQLException e) {
        	//**   20151013 liuhezeng 添加log4j日志管理    **//*
			logger.error(e.getMessage());
        } finally {
            JdbcUtil.close(pst, conn);
        }
		
	    
	    /*StringBuilder sql = new StringBuilder("update community_callback set pl_state = 1 where pl_username = ? ");
		if(null != user)
			sql.append(" and  pl_context_id = ? ");
		if(null != plId)
			sql.append(" and pl_id = ?");
		 Session session = null ;
	     Connection conn = null ;
	     PreparedStatement pst = null ;
	     try {
	            session = getSession();
	            conn = session.connection();
	            pst = conn.prepareStatement(sql.toString());
	            pst.setString(1, userName);
	            if(null != user)
	            	pst.setString(2, user);
	            if(null != plId)
	            	pst.setString(3,plId);
	            pst.executeUpdate();
	        } catch (SQLException e) {
	        	*//**   20151013 liuhezeng 添加log4j日志管理    **//*
				logger.error(e.getMessage());
	        } finally {
	            JdbcUtil.close(pst, conn);
	        }*/
	}
	/**获取评论总数**/
	public String getCommentTotal(String cardId) {
        String count = null;
		String sql = "select total from community_msg where user_id = ?";
        Session session = null ;
        Connection conn = null ;
        PreparedStatement pst = null ;
        ResultSet rs = null;
        try {
            session = getSession();
            conn = session.connection();
            pst = conn.prepareStatement(sql);
            pst.setString(1, cardId);
            rs = pst.executeQuery();
            if(rs.next())
            	count = String.valueOf(rs.getInt("total"));
        } catch (SQLException e) {
        	/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
        } finally {
            JdbcUtil.close(pst, conn);
        }
        return count;
	}
	
}
