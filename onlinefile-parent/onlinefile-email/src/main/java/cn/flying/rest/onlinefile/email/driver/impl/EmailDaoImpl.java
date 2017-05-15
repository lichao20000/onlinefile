package cn.flying.rest.onlinefile.email.driver.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.AuthenticationFailedException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.flying.rest.onlinefile.email.driver.EmailDao;
import cn.flying.rest.onlinefile.utils.EmailUtil;
import cn.flying.rest.onlinefile.utils.JdbcUtil;
import cn.flying.rest.onlinefile.utils.MD5Util;

@Repository("emailServerDao")
public class EmailDaoImpl implements EmailDao {

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**    20151011 liuhezeng添加 log4j日志管理    **/
	private static final Logger logger = Logger.getLogger(EmailDaoImpl.class);
	
	/**
	 * 获取session
	 */
	private Session getSession() {
		return sessionFactory.openSession();
	}

	public Map<String, String> addEmail(Map<String, String> map) {
		Map<String, String> resultMaps = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String emailAddress = map.get("email").toString();

		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);

			int userId = Integer.valueOf(map.get("userid"));

			/** 优先该账户下是否绑定了该邮箱 **/
			sql.append("select count(id) from EMAILSET where USERID = ? and EMAIL = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, userId);
			st.setString(2, emailAddress);
			rs = st.executeQuery();
			while (rs.next()) {
				int result_Counter = rs.getInt(1);
				if (result_Counter > 0) {
					resultMaps.put("success", "false");
					resultMaps.put("msg", "已存在相同邮箱");
					return resultMaps;
				}
			}
			
			StringBuffer sql_insert = new StringBuffer();
			sql_insert.append("INSERT INTO EMAILSET(USERID, RECEIVESERVER,RECEIVESERVERPORT, SENDSERVER,SENDSERVERPORT,EMAIL,PASSWORD) VALUES(?,?,?,?,?,?,?)");
			st = conn.prepareStatement(sql_insert.toString());
			st.setInt(1, userId);
			
			Map<String,String> emailPropertites = EmailUtil.getEmailServerPropertitesByAddress(emailAddress);
			
			st.setString(2, emailPropertites.get("receiveAddress"));
			st.setString(3, emailPropertites.get("receivePort"));
			st.setString(4, emailPropertites.get("sendAddress"));
			st.setString(5, emailPropertites.get("sendPort"));
			st.setString(6, emailAddress);
			st.setString(7, MD5Util.string2MD5(map.get("password").toString()));
			st.executeUpdate();
			resultMaps.put("success", "true");
			resultMaps.put("msg", "添加成功");

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			resultMaps.put("success", "false");
			resultMaps.put("msg", "添加失败");
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		
		return resultMaps;
	}

	public Map<String, String> addEmailManual(Map<String, String> map) {
		Map<String, String> resultMaps = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
	
			int userId = Integer.valueOf(map.get("userid"));
			String emailAddress = map.get("email").toString();
			/** 优先该账户下是否绑定了该邮箱 **/
			sql.append("select count(id) from EMAILSET where USERID = ? and EMAIL = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, userId);
			st.setString(2, emailAddress);
			rs = st.executeQuery();
			while (rs.next()) {
				int result_Counter = rs.getInt(1);
				if (result_Counter > 0) {
					resultMaps.put("success", "false");
					resultMaps.put("msg", "已存在相同邮箱");
					return resultMaps;
				}
			}
			StringBuffer sql_insert = new StringBuffer();
			sql_insert.append("INSERT INTO EMAILSET(USERID, RECEIVESERVER,RECEIVESERVERPORT,SENDSERVER,SENDSERVERPORT,EMAIL,PASSWORD) VALUES(?,?,?,?,?,?,?)");
			st = conn.prepareStatement(sql_insert.toString());
			st.setInt(1, userId);
			st.setString(2, map.get("popServerInput").toString());
			st.setString(3, map.get("popSSLPortInput").toString());
			st.setString(4, map.get("smtpServerInput").toString());
			st.setString(5, map.get("smtpSSLPortInput").toString());
			st.setString(6, emailAddress);
			st.setString(7, MD5Util.string2MD5(map.get("password").toString()));
			st.executeUpdate();
			resultMaps.put("success", "true");
			resultMaps.put("msg", "添加成功");
	
		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			resultMaps.put("success", "false");
			resultMaps.put("msg", "添加失败");
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return resultMaps;
	}

	public Map<String, String> deleteEmail(Map<String, String> map) {
		Map<String, String> resultMaps = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String delEmail = map.get("email");
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("DELETE FROM EMAILSET WHERE USERID = ? and EMAIL = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(map.get("userid")));
			st.setString(2, delEmail);
			st.executeUpdate();
			
			/** 获取默认邮箱   如果删的是默认邮箱  就清空对应的默认邮箱跟该邮箱缓存在数据库中的附件**/
			String defaultEmail = "";
			sql = new StringBuffer(50);
			sql.append("select email from EMAILDEFAULT where USERID = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(map.get("userid")));
			rs = st.executeQuery();
			while (rs.next()) {
				defaultEmail = rs.getString("email");
			}
			if(delEmail.equals(defaultEmail)){
				/** 清空对应的默认邮箱跟该邮箱缓存在数据库中的附件     **/
				delDefaultEmail(map);
			}
			delEmailAttachMents(map);
			resultMaps.put("success", "true");
			resultMaps.put("msg", "删除成功");

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			resultMaps.put("success", "false");
			resultMaps.put("msg", "删除失败");
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return resultMaps;
	}
	public Map<String, String> updateEmail(Map<String, String> map) {
		Map<String, String> resultMaps = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("update EMAILSET set EMAIL = ?,PASSWORD=? where ID = ? ");
			st = conn.prepareStatement(sql.toString());
			st.setString(1, map.get("email".toString()));
			st.setString(2, MD5Util.string2MD5(map.get("password".toString())));
			st.setInt(3, Integer.valueOf(map.get("id")));
			st.executeUpdate();
			resultMaps.put("success", "true");
			resultMaps.put("msg", "更新成功");

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			resultMaps.put("success", "false");
			resultMaps.put("msg", "更新失败");
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return resultMaps;
	}

	public List<Map<String, String>> getEmailList(Map<String, String> map) {
		List<Map<String, String>> emailEntities = new ArrayList<Map<String, String>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Map<String, String> tmpMap = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("select EMAIL from EMAILSET where USERID = ? ");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(map.get("userid").toString()));
			rs = st.executeQuery();

			while (rs.next()) {
				tmpMap = new HashMap<String, String>();
				tmpMap.put("email", rs.getString("EMAIL"));
				emailEntities.add(tmpMap);
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return emailEntities;
	}

	/**
	 * 此方法连接邮箱，并返回邮箱附件列表
	 */
	public Map<String, Object> getDefaultAttachmentByEmail(
			Map<String, String> map) {
		String userid = map.get("userid").toString();
		String windowWidth = map.get("windowWidth").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> emailEntities = new ArrayList<Map<String, String>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String password = null;
		String host = null;
		int attachmentsCount = 0;
		String port = "";
		Map<String, String> attachMentMap = null;
		int pageIndex = Integer.valueOf(map.get("pageIndex"));
		int pageLimit = Integer.valueOf(map.get("pageLimit"));
		String defaultEmail = getDefaultEmail(map);
		if (defaultEmail == null && defaultEmail.equals("")) {
			defaultEmail = map.get("email").toString();
		}
		try {
			session = getSession();
			conn = session.connection();
			/** 在获取emial时候先查询一下当前的表里面是否包含当前的邮箱的缓存数据 **/

			StringBuffer sqlIsCache = new StringBuffer(50);
			sqlIsCache.append("select count(*) from EMAILATTACHMENTS where userid = ? and email = ?");
			st = conn.prepareStatement(sqlIsCache.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, defaultEmail);
			rs = st.executeQuery();
			while (rs.next()) {
				attachmentsCount = rs.getInt(1);
			}
			if (attachmentsCount == 0) {
				StringBuffer sql = new StringBuffer(50);
				sql.append("select PASSWORD,RECEIVESERVER,RECEIVESERVERPORT from EMAILSET where USERID = ? AND EMAIL = ? ");
				st = conn.prepareStatement(sql.toString());
				st.setInt(1, Integer.valueOf(userid));
				st.setString(2, defaultEmail);
				rs = st.executeQuery();

				while (rs.next()) {
					password = MD5Util.md52String(rs.getString("PASSWORD"));
					host = rs.getString("RECEIVESERVER");
					port = rs.getString("RECEIVESERVERPORT");

				}

				try {
					if (host != null && !host.equals("")) {
						emailEntities = EmailUtil.receiveAll(host,defaultEmail, password,port);
						saveEmailAttachMents(emailEntities, userid, defaultEmail);
						emailEntities = new ArrayList<Map<String,String>>();
//						emailEntities = EmailUtil.receive(host, defaultEmail,
//								password, pageIndex, pageLimit);
//						attachmentsCount = EmailUtil.getCountAll(host,
//								defaultEmail, password);

						StringBuffer sql_Select = new StringBuffer(50);
						sql_Select.append("select * from EMAILATTACHMENTS where USERID = ? AND EMAIL = ? order by EMAILINDEX DESC LIMIT ?,?");
						st = conn.prepareStatement(sql_Select.toString());
						st.setInt(1, Integer.valueOf(userid));
						st.setString(2, defaultEmail);
						st.setInt(3, (pageIndex - 1) * pageLimit);
						st.setInt(4, pageLimit);
						rs = st.executeQuery();
						while (rs.next()) {
							attachMentMap = new HashMap<String, String>();
							attachMentMap.put("emailIndex", rs.getString("EMAILINDEX"));
							attachMentMap.put("subject",
									getSubJectForShow(rs.getString("SUBJECTTITLE"),windowWidth));
							attachMentMap.put("subjectTitle",rs.getString("SUBJECTTITLE"));
							attachMentMap.put("sender", getSenderForShow(rs.getString("SENDER"),windowWidth));
							attachMentMap.put("senderTitle", rs.getString("SENDER"));
							attachMentMap.put("sendTime", getSendDateForShow(rs.getString("SENDTIME"), windowWidth));
							attachMentMap.put("containAttachMent", rs.getString("CONTAINATTACHMENT"));
							attachMentMap.put("partCount", rs.getString("PARTCOUNT"));
							attachMentMap.put("attachmentCount", rs.getString("ATTACHMENTCOUNT"));
							emailEntities.add(attachMentMap);
						}
						
						StringBuffer sqlCounter = new StringBuffer(50);
						sqlCounter.append("select count(*) from EMAILATTACHMENTS where userid = ? and email = ?");
						st = conn.prepareStatement(sqlCounter.toString());
						st.setInt(1, Integer.valueOf(userid));
						st.setString(2, defaultEmail);
						rs = st.executeQuery();
						while (rs.next()) {
							attachmentsCount = rs.getInt(1);
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					/**   20151011 liuhezeng 添加log4j日志管理    **/
					logger.error(e.getMessage());
				}
			} else {
				StringBuffer sql = new StringBuffer(50);
				sql.append("select * from EMAILATTACHMENTS where USERID = ? AND EMAIL = ? order by EMAILINDEX DESC LIMIT ?,?");
				st = conn.prepareStatement(sql.toString());
				st.setInt(1, Integer.valueOf(userid));
				st.setString(2, defaultEmail);
				st.setInt(3, (pageIndex - 1) * pageLimit);
				st.setInt(4, pageLimit);
				rs = st.executeQuery();
				while (rs.next()) {
					attachMentMap = new HashMap<String, String>();
					attachMentMap.put("emailIndex", rs.getString("EMAILINDEX"));
					attachMentMap.put("subject",
							getSubJectForShow(rs.getString("SUBJECTTITLE"),windowWidth));
					attachMentMap.put("subjectTitle",rs.getString("SUBJECTTITLE"));
					attachMentMap.put("sender", getSenderForShow(rs.getString("SENDER"),windowWidth));
					attachMentMap.put("senderTitle", rs.getString("SENDER"));
					attachMentMap.put("sendTime", getSendDateForShow(rs.getString("SENDTIME"), windowWidth));
					attachMentMap.put("containAttachMent", rs.getString("CONTAINATTACHMENT"));
					attachMentMap.put("partCount", rs.getString("PARTCOUNT"));
					attachMentMap.put("attachmentCount", rs.getString("ATTACHMENTCOUNT"));
					emailEntities.add(attachMentMap);
				}

			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}

		if (attachmentsCount > 1000) {
			attachmentsCount = 1000;
		}

		result.put("emailAttechMents", emailEntities);
		if(attachmentsCount%20==0){
			result.put("emailAttechMentPages", attachmentsCount / 20);
		}else{
			result.put("emailAttechMentPages", attachmentsCount / 20 + 1);
		}
		return result;
	}

	/**
	 * 此方法连接邮箱，并返回邮箱附件列表
	 */
	public Map<String, Object> getAttachmentByEmail(Map<String, String> map) {
		String userid = map.get("userid").toString();
		String windowWidth = map.get("windowWidth").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> emailEntities = new ArrayList<Map<String, String>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String password = null;
		String host = null;
		String port = null;
		int attachmentsCount = 0;
		Map<String, String> attachMentMap = null;
		int pageIndex = Integer.valueOf(map.get("pageIndex"));
		int pageLimit = Integer.valueOf(map.get("pageLimit"));
		String defaultEmail = map.get("email").toString();
		try {
			session = getSession();
			conn = session.connection();
			/** 在获取emial时候先查询一下当前的表里面是否包含当前的邮箱的缓存数据 **/

			StringBuffer sqlIsCache = new StringBuffer(50);
			sqlIsCache.append("select count(*) from EMAILATTACHMENTS where userid = ? and email = ?");
			st = conn.prepareStatement(sqlIsCache.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, defaultEmail);
			rs = st.executeQuery();
			while (rs.next()) {
				attachmentsCount = rs.getInt(1);
			}
			if (attachmentsCount == 0) {
				StringBuffer sql = new StringBuffer(50);
				sql.append("select PASSWORD,RECEIVESERVER,RECEIVESERVERPORT from EMAILSET where USERID = ? AND EMAIL = ? ");
				st = conn.prepareStatement(sql.toString());
				st.setInt(1, Integer.valueOf(userid));
				st.setString(2, defaultEmail);
				rs = st.executeQuery();

				while (rs.next()) {
					password = MD5Util.md52String(rs.getString("PASSWORD"));
					host = rs.getString("RECEIVESERVER");
					port = rs.getString("RECEIVESERVERPORT");

				}

				try {
					if (host != null && !host.equals("")) {
						emailEntities = EmailUtil.receive(host, defaultEmail, password, pageIndex, pageLimit,port);
						attachmentsCount = EmailUtil.getCountAll(host, defaultEmail, password,port);
					}

				} catch (Exception e) {
					/**   20151011 liuhezeng 添加log4j日志管理    **/
					logger.error(e.getMessage());
				}
			} else {
				StringBuffer sql = new StringBuffer(50);
				sql.append("select * from EMAILATTACHMENTS where USERID = ? AND EMAIL = ? order by EMAILINDEX DESC LIMIT ?,?");
				st = conn.prepareStatement(sql.toString());
				st.setInt(1, Integer.valueOf(userid));
				st.setString(2, defaultEmail);
				st.setInt(3, (pageIndex - 1) * pageLimit);
				st.setInt(4, pageLimit);
				rs = st.executeQuery();
				while (rs.next()) {
					attachMentMap = new HashMap<String, String>();
					attachMentMap.put("emailIndex", rs.getString("EMAILINDEX"));
					attachMentMap.put("subject", getSubJectForShow(rs.getString("SUBJECTTITLE"),windowWidth));
					attachMentMap.put("subjectTitle",rs.getString("SUBJECTTITLE"));					attachMentMap.put("sender", rs.getString("SENDER"));
					attachMentMap.put("sender", getSenderForShow(rs.getString("SENDER"),windowWidth));
					attachMentMap.put("senderTitle", rs.getString("SENDER"));
					attachMentMap.put("sendTime", getSendDateForShow(rs.getString("SENDTIME"), windowWidth));
					attachMentMap.put("containAttachMent", rs.getString("CONTAINATTACHMENT"));
					attachMentMap.put("partCount", rs.getString("partCount"));
					attachMentMap.put("attachmentCount", rs.getString("ATTACHMENTCOUNT"));
					emailEntities.add(attachMentMap);
				}

			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}

		if (attachmentsCount > 1000) {
			attachmentsCount = 1000;
		}

		result.put("emailAttechMents", emailEntities);
		if(attachmentsCount%20==0){
			result.put("emailAttechMentPages", attachmentsCount / 20 );
		}else{
			result.put("emailAttechMentPages", attachmentsCount / 20 + 1);
		}
		return result;
	}

	/**
	 * 获得邮件主题
	 * 
	 * @param msg
	 *            邮件内容
	 * @return 解码后的邮件主题
	 */
	public String getSubJectForShow(String msg,String windowWidth) {
		String tmpMsg = "";
		int windowWidthInt = Integer.parseInt(windowWidth);
		/**  default is 13 **/
		if (String_length(msg) > windowWidthInt/35 ) {
				tmpMsg = subStringByByte(msg,windowWidthInt/35)+"...";
		} else {
				tmpMsg = msg;
		}
		
		return tmpMsg;
	}
	
	public String getSenderForShow(String msg,String windowWidth) {
		String tmpMsg = "";
		int windowWidthInt = Integer.parseInt(windowWidth);
		/**  default is 13 **/
		if (String_length(msg) > windowWidthInt/45 ) {
				tmpMsg = subStringByByte(msg,windowWidthInt/45)+"...";
		} else {
				tmpMsg = msg;
		}
//		if(String_length(msg)>5){
//			tmpMsg = subStringByByte(msg,8)+"...";
//		}
		return tmpMsg;
	}
	
	public String getSendDateForShow(String msg,String windowWidth) {
		String tmpMsg = "";
		int windowWidthInt = Integer.parseInt(windowWidth);
		/**  default is 13 **/
		if (String_length(msg) > windowWidthInt/65 ) {
				tmpMsg = subStringByByte(msg,windowWidthInt/65)+"...";
		} else {
				tmpMsg = msg;
		}
//		if(String_length(msg)>5){
//			tmpMsg = subStringByByte(msg,8)+"...";
//		}
		return msg;
	}
	
	private static String subStringByByte(String str, int len) {
	        String result = null;
	        if (str != null) {
	            byte[] a = str.getBytes();
	            if (a.length <= len) {
	                result = str;
	            } else if (len > 0) {
	                result = new String(a, 0, len);
	                int length = result.length();
	                if (str.charAt(length - 1) != result.charAt(length - 1)) {
	                    if (length < 2) {
	                        result = null;
	                    } else {
	                        result = result.substring(0, length - 1);
	                    }
	                }
	            }
	        }
	        return result;
	    }

	/**
	 * 判断字符串长度
	 * 
	 * @param value
	 *            字符串
	 * @return 返回字符串长度
	 */
	public static int String_length(String value) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}

	@Override
	public Map<String, String> getEmailSettingByEmail(Map<String, String> map) {
		Map<String, String> emailSettings = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("select * from EMAILSET where USERID = ? AND EMAIL = ? ");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(map.get("userid").toString()));
			st.setString(2, map.get("email").toString());
			rs = st.executeQuery();

			while (rs.next()) {
				emailSettings.put("success", "true");
				emailSettings.put("id", rs.getString("ID"));
				emailSettings.put("type", rs.getString("TYPE"));
				emailSettings.put("receiverserver",
						rs.getString("RECEIVESERVER"));
				emailSettings.put("sendserver", rs.getString("SENDSERVER"));
				emailSettings.put("receiveserverport",
						rs.getString("RECEIVESERVERPORT"));
				emailSettings.put("sendserverport",
						rs.getString("SENDSERVERPORT"));
				emailSettings.put("email", rs.getString("EMAIL"));
				emailSettings.put("password", null);
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			emailSettings.put("success", "false");
			emailSettings.put("msg", "获取邮箱配置信息错误");
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return emailSettings;
	}

	@Override
	public List<Map<String, String>> getAllAttachments(Map<String, String> map) {

		List<Map<String, String>> emailEntities = new ArrayList<Map<String, String>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String password = null;
		String host = null;
		String port = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("select PASSWORD,RECEIVESERVER,RECEIVESERVERPORT from EMAILSET where USERID = ? AND EMAIL = ? ");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(map.get("userid").toString()));
			st.setString(2, map.get("email").toString());
			rs = st.executeQuery();

			while (rs.next()) {
				password = MD5Util.md52String(rs.getString("PASSWORD"));
				// emailEntity.setUsername(rs.getString("EMAIL"));
				// emailEntities.add(emailEntity);
				host = rs.getString("RECEIVESERVER");
				port = rs.getString("RECEIVESERVERPORT");
			}
			int pageIndex = Integer.valueOf(map.get("pageIndex"));
			int pageLimit = Integer.valueOf(map.get("pageLimit"));
			try {
				EmailUtil.receive(host, map.get("email").toString(),
						password, pageIndex, pageLimit,port);
			} catch (Exception e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return emailEntities;

	}

	/**
	 * @author Administrator 该方法用于从邮箱取出附件之后，把附件信息保存到数据库的表里面，方便以后存取方便，每次去后台抓取数据太慢
	 * @param attachmentLists
	 * @param userid
	 * @return 返回插入结果
	 */
	public boolean saveEmailAttachMents(
			List<Map<String, String>> attachmentLists, String userid,
			String email) {
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean isOK = false;
		try {
			session = getSession();
			conn = session.connection();
			/** 此处为了确保邮箱同步，目前先清空一下当前的数据，然后再插入 **/
			StringBuffer sqlDel = new StringBuffer(50);
			sqlDel.append("delete from EMAILATTACHMENTS where userid = ? and email = ?");
			st = conn.prepareStatement(sqlDel.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, email);
			st.executeUpdate();
			
			StringBuffer sql = new StringBuffer(50);
			sql.append("insert into EMAILATTACHMENTS(USERID,EMAIL,EMAILINDEX,SUBJECTTITLE,SENDER,SENDTIME,CONTAINATTACHMENT,MAILTEXT,PARTCOUNT,ATTACHMENTCOUNT,SIZE) values(?,?,?,?,?,?,?,?,?,?,?)");
			st = conn.prepareStatement(sql.toString());
			for (Map<String, String> attachMent : attachmentLists) {
				st.setString(1, userid);
				st.setString(2, email);
				st.setString(3, attachMent.get("emailIndex"));
				st.setString(4, attachMent.get("subjectTitle"));
				st.setString(5, attachMent.get("sender"));
				st.setString(6, attachMent.get("sendTime"));
				st.setString(7, attachMent.get("containAttachMent"));
				if(attachMent.get("mailText").length()>4000){
					st.setString(8, attachMent.get("mailText").substring(0,3999));
				}else{
					st.setString(8, attachMent.get("mailText"));
				}
				st.setString(9, attachMent.get("partCount"));
				st.setString(10, attachMent.get("attachmentCount"));
				st.setString(11, attachMent.get("size"));
				st.addBatch();
			}
			isOK = (st.executeBatch().length == attachmentLists.size());

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return true;

	}

	/**
	 * @author Administrator 
	 * 删除邮箱附件
	 * @param attachmentLists
	 * @param userid
	 * @return 返回插入结果
	 */
	public boolean delEmailAttachMents(Map<String, String> map) {

		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("delete from EMAILATTACHMENTS where userid = ? and email = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(map.get("userid").toString()));
			st.setString(2, map.get("email").toString());
			st.executeUpdate();
			return true;

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			return false;
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
	}

	
	/**
	 * @author Administrator 
	 * 清空当前用户的所有的附件
	 * @param attachmentLists
	 * @param userid
	 * @return 返回插入结果
	 */
	public boolean delAllEmailAttachMents(Map<String, String> map) {

		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("delete from EMAILATTACHMENTS where userid = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(map.get("userid").toString()));
			st.executeUpdate();
			return true;

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			return false;
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
	}

	/**
	 * @author Administrator 该方法用于从邮箱取出附件之后，把附件信息保存到数据库的表里面，方便以后存取方便，每次去后台抓取数据太慢
	 * @param attachmentLists
	 * @param userid
	 * @return 返回插入结果
	 */
	public boolean updateEmailAttachMents(Map<String, String> map) {

		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("insert into PASSWORD values()");
			st = conn.prepareStatement(sql.toString());
			rs = st.executeQuery();

			while (rs.next()) {

			}
			try {

			} catch (Exception e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return true;
	}

	@Override
	public Map<String, String> saveEmailSetting(Map<String, String> map) {
		Map<String, String> saveResult = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("update EMAILSET SET EMAIL = ?,PASSWORD = ?,RECEIVESERVER=?,RECEIVESERVERPORT=?,SENDSERVER=?,SENDSERVERPORT=? WHERE ID = ? ");
			st = conn.prepareStatement(sql.toString());
			st.setString(1, map.get("email"));
			st.setString(2, MD5Util.string2MD5(map.get("password")));
			st.setString(3, map.get("receiverserver"));
			st.setString(4, map.get("receiveserverport"));
			st.setString(5, map.get("sendserver"));
			st.setString(6, map.get("sendserverport"));
			st.setString(7, map.get("id"));
			st.executeUpdate();
			saveResult.put("success", "true");
			saveResult.put("msg", "保存成功");

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			saveResult.put("success", "false");
			saveResult.put("msg", "保存失败");
		} finally {
			JdbcUtil.closeConn(null, st, session);
		}
		return saveResult;
	}

	@Override
	public Map<String, Object> searchEmailAttachment(Map<String, String> map) {
		String userid = map.get("userid").toString();
		String searchKeyWord = map.get("searchKeyWord");
		String windowWidth = map.get("windowWidth");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, String>> emailEntities = new ArrayList<Map<String, String>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int pageIndex = Integer.valueOf(map.get("pageIndex"));
		int pageLimit = Integer.valueOf(map.get("pageLimit"));
		Map<String, String> attachMentMap = null;
		int countAll = 0;
		try {
			session = getSession();
			conn = session.connection();

			StringBuffer sqlCount = new StringBuffer(50);
			sqlCount.append("select count(*) from EMAILATTACHMENTS where (USERID = ? AND EMAIL = ?) AND (SUBJECTTITLE like '%"
					+ searchKeyWord
					+ "%' OR SENDER like '%"
					+ searchKeyWord
					+ "%' OR SENDTIME like '%" + searchKeyWord + "%' OR MAILTEXT like '%" + searchKeyWord +"%')");
			st = conn.prepareStatement(sqlCount.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, map.get("email").toString());
			rs = st.executeQuery();
			while (rs.next()) {
				countAll = rs.getInt(1);
			}

			StringBuffer sql = new StringBuffer(50);
			sql.append("select EMAILINDEX,SUBJECTTITLE,SENDER,SENDTIME,CONTAINATTACHMENT,PARTCOUNT,ATTACHMENTCOUNT from EMAILATTACHMENTS where (USERID = ? AND EMAIL = ?) AND (SUBJECTTITLE like '%"
					+ searchKeyWord
					+ "%' OR SENDER like '%"
					+ searchKeyWord
					+ "%' OR SENDTIME like '%"
					+ searchKeyWord
					+ "%'  OR MAILTEXT like '%" + searchKeyWord +"%')   LIMIT ?,?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, map.get("email").toString());
			st.setInt(3, (pageIndex - 1) * pageLimit);
			st.setInt(4, pageLimit);
			rs = st.executeQuery();
			while (rs.next()) {
				attachMentMap = new HashMap<String, String>();
				attachMentMap.put("emailIndex", rs.getString("EMAILINDEX"));
				attachMentMap.put("subject",
						getSubJectForShow(rs.getString("SUBJECTTITLE"),windowWidth));
				attachMentMap.put("subjectTitle",rs.getString("SUBJECTTITLE"));				
				attachMentMap.put("sender", getSenderForShow(rs.getString("SENDER"),windowWidth));
				attachMentMap.put("senderTitle", rs.getString("SENDER"));
				attachMentMap.put("sendTime", getSendDateForShow(rs.getString("SENDTIME"), windowWidth));
				attachMentMap.put("containAttachMent", rs.getString("CONTAINATTACHMENT"));
				attachMentMap.put("partCount", rs.getString("partCount"));
				attachMentMap.put("attachmentCount", rs.getString("ATTACHMENTCOUNT"));
				emailEntities.add(attachMentMap);
			}
		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}

		resultMap.put("emailAttechMents", emailEntities);
		if(countAll%20 == 0){
			resultMap.put("emailAttechMentPages", countAll / 20 );
		}else{
			resultMap.put("emailAttechMentPages", countAll / 20 + 1);
		}

		return resultMap;
	}

	@Override
	public List<Map<String, String>> cacheAllEmailAttachments(
			Map<String, String> map) throws Exception {
		String userid = map.get("userid").toString();
		List<Map<String, String>> emailEntities = new ArrayList<Map<String, String>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String password = null;
		String host = null;
		String port = null;
		int attachmentsCount = 0;
		try {
			session = getSession();
			conn = session.connection();

			StringBuffer sqlIsCache = new StringBuffer(50);
			sqlIsCache
					.append("select count(*) from EMAILATTACHMENTS where userid = ? and email = ?");
			st = conn.prepareStatement(sqlIsCache.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, map.get("email").toString());
			rs = st.executeQuery();
			while (rs.next()) {
				attachmentsCount = rs.getInt(1);
			}

			if (attachmentsCount == 0) {
				StringBuffer sql = new StringBuffer(50);
				sql.append("select PASSWORD,RECEIVESERVER,RECEIVESERVERPORT from EMAILSET where USERID = ? AND EMAIL = ? ");
				st = conn.prepareStatement(sql.toString());
				st.setInt(1, Integer.valueOf(userid));
				st.setString(2, map.get("email").toString());
				rs = st.executeQuery();

				while (rs.next()) {
					password = MD5Util.md52String(rs.getString("PASSWORD"));
					host = rs.getString("RECEIVESERVER");
					port = rs.getString("RECEIVESERVERPORT");

				}
				if (host != null && !host.equals("")) {
					emailEntities = EmailUtil.receiveAll(host,map.get("email").toString(), password,port);
				}
				saveEmailAttachMents(emailEntities, userid, map.get("email").toString());
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return emailEntities;
	}

	@Override
	public List<Map<String, String>> cacheDefaultAllEmailAttachments(
			Map<String, String> map) {
		String userid = map.get("userid").toString();
		List<Map<String, String>> emailEntities = new ArrayList<Map<String, String>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String password = null;
		String host = null;
		String port = null;
		int attachmentsCount = 0;
		String defaultEmail = getDefaultEmail(map);
		try {
			session = getSession();
			conn = session.connection();

			StringBuffer sqlIsCache = new StringBuffer(50);
			sqlIsCache
					.append("select count(*) from EMAILATTACHMENTS where userid = ? and email = ?");
			st = conn.prepareStatement(sqlIsCache.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, defaultEmail);
			rs = st.executeQuery();
			while (rs.next()) {
				attachmentsCount = rs.getInt(1);
			}

			if (attachmentsCount == 0) {
				StringBuffer sql = new StringBuffer(50);
				sql.append("select PASSWORD,RECEIVESERVER,RECEIVESERVERPORT from EMAILSET where USERID = ? AND EMAIL = ? ");
				st = conn.prepareStatement(sql.toString());
				st.setInt(1, Integer.valueOf(userid));
				st.setString(2, defaultEmail);
				rs = st.executeQuery();

				while (rs.next()) {
					password = MD5Util.md52String(rs.getString("PASSWORD"));
					host = rs.getString("RECEIVESERVER");
					port = rs.getString("RECEIVESERVERPORT");

				}
				try {
					if (host != null && !host.equals("")) {
						emailEntities = EmailUtil.receiveAll(host,
								defaultEmail, password,port);
					}
					saveEmailAttachMents(emailEntities, userid, defaultEmail);
				} catch (Exception e) {
					/**   20151011 liuhezeng 添加log4j日志管理    **/
					logger.error(e.getMessage());
				}
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return emailEntities;
	}

	@Override
	public Map<String, String> setAsDefaultEmail(Map<String, String> map) {
		Map<String, String> resultMaps = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			
			int userId = Integer.valueOf(map.get("userid"));
			String emailAddress = map.get("email").toString();
			
			/** 未被保存的邮箱不能设为默认邮箱 **/
			StringBuffer sql_query = new StringBuffer(50);
			sql_query.append("select count(id) from EMAILSET where USERID = ? and EMAIL = ?");
			st = conn.prepareStatement(sql_query.toString());
			st.setInt(1, userId);
			st.setString(2, emailAddress);
			rs = st.executeQuery();
			while (rs.next()) {
				int result_Counter = rs.getInt(1);
				if (result_Counter == 0) {
					resultMaps.put("success", "false");
					resultMaps.put("msg", "您尚未创建(保存)该邮箱，请创建后设置");
					return resultMaps;
				}
			}
			
			/** 优先该账户下是否绑定了该邮箱 **/
			StringBuffer sql = new StringBuffer(50);
			sql.append("select count(id) from EMAILDEFAULT where USERID = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, userId);
			rs = st.executeQuery();
			while (rs.next()) {
				int result_Counter = rs.getInt(1);
				if (result_Counter > 0) {
					/** 此处为邮箱的更新操作 **/
					StringBuffer sql_insert = new StringBuffer();
					sql_insert.append("UPDATE EMAILDEFAULT SET EMAIL = ? where userid = ?");
					st = conn.prepareStatement(sql_insert.toString());
					st.setString(1, map.get("email").toString());
					st.setInt(2, userId);
					st.executeUpdate();
					resultMaps.put("success", "true");
					resultMaps.put("msg", "设置成功");
					return resultMaps;
				}
			}
			StringBuffer sql_insert = new StringBuffer();
			sql_insert.append("INSERT INTO EMAILDEFAULT(USERID,EMAIL) VALUES(?,?)");
			st = conn.prepareStatement(sql_insert.toString());
			st.setInt(1, userId);
			st.setString(2, map.get("email").toString());
			st.executeUpdate();
			resultMaps.put("success", "true");
			resultMaps.put("msg", "设置成功");

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			resultMaps.put("success", "false");
			resultMaps.put("msg", "设置失败");
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return resultMaps;
	}

	/**
	 * 获取默认邮箱，如果为空，那么默认取第一个邮箱
	 * 
	 * @param map
	 * @return
	 */
	public String getDefaultEmail(Map<String, String> map) {
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String defaultEmail = "";
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			int userId = Integer.valueOf(map.get("userid"));
			/** 优先该账户下是否绑定了该邮箱 **/
			sql.append("select email from EMAILDEFAULT where USERID = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, userId);
			rs = st.executeQuery();
			while (rs.next()) {
				defaultEmail = rs.getString("email");
			}
			if (defaultEmail == null || defaultEmail.equals("")) {
				StringBuffer sqlSelect = new StringBuffer(50);
				sqlSelect.append("select email from EMAILSET where USERID = ?");
				st = conn.prepareStatement(sqlSelect.toString());
				st.setInt(1, userId);
				rs = st.executeQuery();
				while (rs.next()) {
					defaultEmail = rs.getString("email");
					break;
				}
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return defaultEmail;
	}

	public Map<String, String> delDefaultEmail(Map<String, String> map) {
		Map<String, String> resultMaps = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			int userId = Integer.valueOf(map.get("userid"));
			/** 优先该账户下是否绑定了该邮箱 **/
			sql.append("delete from EMAILDEFAULT where USERID = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, userId);
			st.executeUpdate();
			resultMaps.put("success", "true");
			resultMaps.put("msg", "删除成功");

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			resultMaps.put("success", "false");
			resultMaps.put("msg", "删除失败");
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return resultMaps;
	}

	
	public void checkEmailAddress(Map<String, String>  map)throws Exception{

		String userid = map.get("userid").toString();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String password = null;
		String host = null;
		String port = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("select PASSWORD,RECEIVESERVER,RECEIVESERVERPORT from EMAILSET where USERID = ? AND EMAIL = ? ");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, map.get("email").toString());
			rs = st.executeQuery();
			while (rs.next()) {
				password = MD5Util.md52String(rs.getString("PASSWORD"));
				host = rs.getString("RECEIVESERVER");
				port = rs.getString("RECEIVESERVERPORT");
			}
			if (host != null && !host.equals("")) {
				EmailUtil.getCountAll(host, map.get("email").toString(), password,port);
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
	}

	/**同步邮箱
	 * @see cn.flying.rest.onlinefile.email.driver.EmailDao#isSynEmailAttachMent(java.util.Map)
	 */
	@Override
	public Map<String, String> isSynEmailAttachMent(Map<String, String> map) {
		Map<String,String> resultMap = new HashMap<String, String>();
			try {
				checkEmailAddress(map);
			}catch (AuthenticationFailedException e) {
				resultMap.put("success", "false");
				resultMap.put("msg", "登录邮箱失败，请检查用户名、密码是否正确");
				return resultMap;
			}catch (Exception e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
		}
		if(delEmailAttachMents(map)){
			try {
				cacheAllEmailAttachments(map);
			} catch (AuthenticationFailedException e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
				resultMap.put("success", "false");
				resultMap.put("msg", "登录邮箱失败，请检查用户名、密码是否正确");
				return resultMap;
			}catch (Exception e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
				resultMap.put("success", "false");
				resultMap.put("msg", "同步失败!");
				return resultMap;
			}
			resultMap.put("success", "true");
			resultMap.put("msg", "同步成功!");
		}else{
			resultMap.put("success", "false");
			resultMap.put("msg", "同步失败!");
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getEmailAttachMentInfoByEmail(Map<String, String> map) {
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String userid = map.get("userid").toString();
		String defaultEmail = map.get("email").toString();
		int emailIndex = Integer.valueOf(map.get("emailIndex").toString());
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sb = new StringBuffer(50);
			sb.append("select MAILTEXT,ATTACHMENTCOUNT,SENDER,SENDTIME,SUBJECTTITLE from EMAILATTACHMENTS where USERID = ? AND EMAIL = ? AND EMAILINDEX = ?");
			st = conn.prepareStatement(sb.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, defaultEmail);
			st.setInt(3, emailIndex);
			rs = st.executeQuery();
			if (rs.next()) {
				result.put("emailText", getEmailText(rs.getString("SUBJECTTITLE"),rs.getString("SENDER"),rs.getString("SENDTIME"),rs.getString("MAILTEXT"),rs.getString("ATTACHMENTCOUNT")));
				result.put("attachmentCount", rs.getString("ATTACHMENTCOUNT"));
			}
		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return result;
	}
	
	private String getEmailText(String subjectTitle,String sender,String sendTime,String emailText,String attachMentAccount){
		String subjectTitleOrg = subjectTitle;
		String senderOrg = sender;
		/** 如果存在邮箱附件 **/
		if(null!= attachMentAccount && !attachMentAccount.equals("0")){
//			if (String_length(msg) > windowWidthInt/45 ) {
//				tmpMsg = subStringByByte(msg,windowWidthInt/45)+"...";
//				
			if(String_length(subjectTitle)>10){
				subjectTitle = subStringByByte(subjectTitle,10)+"...";
			}
			if(String_length(sender)>20){
				sender = subStringByByte(sender,20)+"...";
			}
		}else{
			if(String_length(subjectTitle)>15){
				subjectTitle = subStringByByte(subjectTitle,15)+"...";
			}
			if(String_length(sender)>26){
				sender = subStringByByte(sender,26)+"...";
			}
		}
		if(null != emailText && emailText.equals("")){
			emailText = "发件人太懒了,什么邮箱内容都没给您留下!";
		}
		emailText = "<div style = 'margin-top:5px'><div style = 'float:left;color:#999;width:72px;'>主&nbsp;&nbsp;&nbsp;&nbsp;题  :</div><h4 style = 'font-weight:bold;float:left;margin: 0px;margin-left: 8px;cursor:pointer;' title = '"+subjectTitleOrg+"'>"+subjectTitle + "</h4></div><br>"+
				"<div style = 'margin-top:5px;'><div style = 'float:left;color:#999;width:72px;'>发件人&nbsp;</div><div style = '  float: left;margin-left:-24px;'>:</div><h5 style = 'float:left;margin:0px;margin-left: 8px;line-height: 20px;cursor:pointer;' title = '"+senderOrg+"'>"+sender+"</h5></div><br>"+
				"<div style = 'margin-top:5px;'><div style = 'float:left;color:#999;width:72px;'>时&nbsp;&nbsp;&nbsp;&nbsp;间  :</div><h5 style = 'float:left;margin:0px;margin-left: 8px;line-height: 20px;'>"+sendTime+"</h5></div><br>"+
				"<div style = 'margin-top:5px;margin-bottom:10px;'><div style = 'float:left;color:#999;width:72px;'>内&nbsp;&nbsp;&nbsp;&nbsp;容  :</div></div><br>"
				+emailText;
	
		return emailText;
	}
	
	@Override
	public Map<String, Object> getEmailAttachMentsByEmail(Map<String, String> map) {
		String userid = map.get("userid").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> emailEntities = new ArrayList<Map<String, String>>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String password = null;
		String host = null;
		String port = null;
		int attachmentsCount = 0;
		int pageIndex = Integer.valueOf(map.get("pageIndex"));
		int pageLimit = Integer.valueOf(map.get("pageLimit"));
		String defaultEmail = map.get("email").toString();
		int emailIndex = Integer.valueOf(map.get("emailIndex").toString());
		int partCount = Integer.valueOf(map.get("partCount").toString());
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("select PASSWORD,RECEIVESERVER,RECEIVESERVERPORT from EMAILSET where USERID = ? AND EMAIL = ? ");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, defaultEmail);
			rs = st.executeQuery();
			while (rs.next()) {
				password = MD5Util.md52String(rs.getString("PASSWORD"));
				host = rs.getString("RECEIVESERVER");
				port = rs.getString("RECEIVESERVERPORT");
			}
			try {
				if (host != null && !host.equals("")) {
					try{
						emailEntities = EmailUtil.getEmailAttachMentInfoByEmail(host, defaultEmail,
								password, pageIndex, pageLimit,emailIndex,partCount,port);
						attachmentsCount = emailEntities.size();
					} catch (AuthenticationFailedException e) {
						result.put("success", "false");
						result.put("msg", "获取邮箱附件失败，请检查用户名、密码是否正确");
					}
				}
			} catch (Exception e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		if (attachmentsCount > 1000) {
			attachmentsCount = 1000;
		}
		result.put("emailAttechMents", emailEntities);
		if(attachmentsCount % 20 == 0){
			result.put("emailAttechMentPages", attachmentsCount / 20 );
		}else{
			result.put("emailAttechMentPages", attachmentsCount / 20 + 1);
		}
		return result;
	}

	@Override
	public List<String> downloadAttachMent(Map<String, String> map) {
	    List<String> attachList = new ArrayList<String>();
		String userid = map.get("userid").toString();
		Map<String, String> result = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String password = null;
		String host = null;
		String port = null;
		String defaultEmail = map.get("email").toString();
		String emailUploadUrl = map.get("emailUploadUrl").toString();
		String classId = map.get("classId").toString();
		String companyId = map.get("companyId").toString();
		String openlevel = map.get("openlevel").toString();
		/**  支持多个邮箱附件上传  **/
		String[] emailIndexSplit = map.get("emailIndex").split(",");
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("select PASSWORD,RECEIVESERVER,RECEIVESERVERPORT from EMAILSET where USERID = ? AND EMAIL = ? ");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(userid));
			st.setString(2, defaultEmail);
			rs = st.executeQuery();

			while (rs.next()) {
				password = MD5Util.md52String(rs.getString("PASSWORD"));
				host = rs.getString("RECEIVESERVER");
				port = rs.getString("RECEIVESERVERPORT");
			}
			try {
				if (host != null && !host.equals("")) {
					emailUploadUrl +="?classId="+classId+"&companyId="+companyId+"&userId="+userid+"&openlevel="+openlevel;
					for(String tmpIndex:emailIndexSplit){
						String fileInfo = EmailUtil.shareAttachMentToGroup(Integer.valueOf(tmpIndex.split("\\|")[0]),Integer.valueOf(tmpIndex.split("\\|")[1]),host, defaultEmail,
								password,emailUploadUrl,port);
						attachList.add(fileInfo);
					}
				}

			} catch (Exception e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());
			}

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return attachList;
	}

	@Override
	public Map<String, String> deleteAllEmail(Map<String, String> map) {
		Map<String, String> resultMaps = new HashMap<String, String>();
		Session session = null;
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			session = getSession();
			conn = session.connection();
			StringBuffer sql = new StringBuffer(50);
			sql.append("DELETE FROM EMAILSET WHERE USERID = ?");
			st = conn.prepareStatement(sql.toString());
			st.setInt(1, Integer.valueOf(map.get("userid")));
			st.executeUpdate();
			delDefaultEmail(map);
			delAllEmailAttachMents(map);
			resultMaps.put("success", "true");
			resultMaps.put("msg", "删除成功");

		} catch (SQLException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
			resultMaps.put("success", "false");
			resultMaps.put("msg", "删除失败");
		} finally {
			JdbcUtil.closeConn(rs, st, session);
		}
		return resultMaps;
	}
	
	
}
