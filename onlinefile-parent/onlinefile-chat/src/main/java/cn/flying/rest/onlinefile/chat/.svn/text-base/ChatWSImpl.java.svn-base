package cn.flying.rest.onlinefile.chat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.flying.rest.onlinefile.chat.dao.ChatDao;
import cn.flying.rest.onlinefile.restInterface.ChatWS;
import cn.flying.rest.onlinefile.restInterface.DocumentClassWS;
import cn.flying.rest.onlinefile.restInterface.FilesWS;
import cn.flying.rest.onlinefile.restInterface.LuceneWS;
import cn.flying.rest.onlinefile.restInterface.MessageQueueConsumerWS;
import cn.flying.rest.onlinefile.restInterface.MessageQueueProducerWS;
import cn.flying.rest.onlinefile.restInterface.UserWS;
import cn.flying.rest.onlinefile.utils.BaseWS;
import cn.flying.rest.onlinefile.utils.BroadcastUtils;
import cn.flying.rest.onlinefile.utils.CacheUtils;
import cn.flying.rest.onlinefile.utils.ImageUtil;
import cn.flying.rest.onlinefile.utils.LogUtils;
import cn.flying.rest.onlinefile.utils.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Path("/chat")
@Component
public class ChatWSImpl extends BaseWS implements ChatWS {

	@Value("${onlinefile.messagequeue.factoryUtil.ImageLinePath}")
	private String activeMQPath;

	@Value("${onlinefile.chatWSImpl.urlPath}")
	private String urlPath;

	@Value("${onlinefile.logoImg}")
	private String logoImg;
	
	@Autowired
	private ImageUtil imageUtil;

	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private UserWS userWS;
	
	private FilesWS filesWS;

	private LuceneWS lucene;

	private DocumentClassWS documentClassWS;

	private MessageQueueProducerWS producerWS;
	private static Logger logger = Logger.getLogger(ChatWSImpl.class);

	public MessageQueueProducerWS getProducerWS() {
		if (producerWS == null) {
			synchronized (MessageQueueProducerWS.class) {
				if (producerWS == null) {
					producerWS = this.getService(MessageQueueProducerWS.class);
				}
			}
		}
		return producerWS;
	}

	private MessageQueueConsumerWS consumerWS;

	public MessageQueueConsumerWS getconsumerWS() {
		if (consumerWS == null) {
			synchronized (MessageQueueConsumerWS.class) {
				if (consumerWS == null) {
					consumerWS = this.getService(MessageQueueConsumerWS.class);
				}
			}
		}
		return consumerWS;
	}

	private DocumentClassWS getDocumentClassWS() {
		if (documentClassWS == null) {
			synchronized (DocumentClassWS.class) {
				if (documentClassWS == null) {
					documentClassWS = this.getService(DocumentClassWS.class);
				}
			}
		}
		return documentClassWS;
	}

	private UserWS getUserWS() {
		if (userWS == null) {
			synchronized (UserWS.class) {
				if (userWS == null) {
					userWS = this.getService(UserWS.class);
				}
			}
		}
		return userWS;
	}
	private FilesWS getfilesWS() {
		if (filesWS == null) {
			synchronized (FilesWS.class) {
				if (filesWS == null) {
					filesWS = this.getService(FilesWS.class);
				}
			}
		}
		return filesWS;
	}

	private LuceneWS getLuceneWS() {
		if (lucene == null) {
			synchronized (LuceneWS.class) {
				if (lucene == null) {
					lucene = this.getService(LuceneWS.class);
				}
			}
		}
		return lucene;
	}

	@Resource(name = "chatDaoImpl")
	private ChatDao chatDao;

	@Value("${openfire.ip}")
	public void setOpenfireIp(String openfireIp) {
		BroadcastUtils.openfireIp = openfireIp;
	}

	@Value("${openfire.port}")
	public void setOpenfirePort(String openfirePort) {
		BroadcastUtils.openfirePort = openfirePort;
	}

	@Value("${openfire.servername}")
	public void setOpenfireServerName(String openfireServerName) {
		BroadcastUtils.openfireServerName = openfireServerName;
	}

	@Value("${openfire.serverPort}")
	public void setOpenfireServerPort(String openfireServerPort) {
		BroadcastUtils.openfireServerPort = openfireServerPort;
	}

	@Value("${http.schema}")
	public void setHttpSchema(String httpSchema) {
		BroadcastUtils.http_schema = httpSchema;
	}

	@Override
	public void doStart() {
		/** 匿名登录openfire **/
		BroadcastUtils.login();
	}

	// 获取公司下的全部激活联系人已经未读消息
	public void getCompanyUsersList(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String username = request.getParameter("username");
		// System.out.println("获取公司下的全部激活联系人已经未读消息:companyId:"+companyId+",username:"+username);
		JSONObject json = new JSONObject();
		Set<String> onlineingusers = this.getOnLineUsers(companyId);
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		if (null == companyUserInitInfo || companyUserInitInfo.isEmpty()) {
			companyUserInitInfo = getUserWS().companyUserInitInfo(companyId);
			CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
					+ companyId, companyUserInitInfo);
		}
		HashMap<String, String> map = chatDao.getNotSeeMessageCount(companyId,
				username);
		List<String> types = new ArrayList<String>();
		HashMap<String, List<Map<String, String>>> onlineusers = new HashMap<String, List<Map<String, String>>>();
		HashMap<String, List<Map<String, String>>> offlineusers = new HashMap<String, List<Map<String, String>>>();
		Iterator<Entry<String, Map<String, String>>> iterator = companyUserInitInfo
				.entrySet().iterator();
		Entry<String, Map<String, String>> entry = null;
		Map<String, String> user = null;
		Map<String, String> self = null;
		long allMessageCount = 0;
		HashMap<String, Long> typesCount = new HashMap<String, Long>();
		String type = "公司所有成员";
		while (iterator.hasNext()) {
			entry = iterator.next();
			user = entry.getValue();
			user.remove("FIR_LOGIN");
			user.remove("ISADMIN");
			user.remove("ORGID");
			user.put(
					"newmessagecount",
					map.get(user.get("USERNAME")) == null ? "0" : map.get(user
							.get("USERNAME")));
			user.put(
					"newmessageStyle",
					map.get(user.get("USERNAME")) == null ? "display:none;"
							: ("display:block;width:"
									+ (6 + user.get("newmessagecount").length() * 6) + "px;"));
			user.put(
					"status",
					username.equals(user.get("USERNAME")) ? "在线"
							: (onlineingusers.contains(user.get("USERNAME")
									.replace("@", "\\40")) ? "在线" : "离线"));
			if ("在线".equals(user.get("status"))) {
				user.put("useritempicStyle", "user-online");
			} else {
				user.put("useritempicStyle", "user-offline");
			}
			user.put("showname",
					(user.get("FULLNAME").length() > 10 ? user.get("FULLNAME")
							.substring(0, 10) + "..." : user.get("FULLNAME"))
							+ (username.equals(user.get("USERNAME")) ? "(自己)"
									: ""));
			if (!types.contains(type))
				types.add(type);
			if (username.equals(user.get("USERNAME"))) {
				self = user;
				continue;
			}
			if ("在线".equals(user.get("status"))) {
				if (onlineusers.get(type) == null) {
					onlineusers.put(type, new ArrayList<Map<String, String>>());
				}
				onlineusers.get(type).add(user);
			} else {
				if (offlineusers.get(type) == null) {
					offlineusers
							.put(type, new ArrayList<Map<String, String>>());
				}
				offlineusers.get(type).add(user);
			}
			long temp = Long.parseLong(user.get("newmessagecount"));
			allMessageCount += temp;
			if (typesCount.get(type) == null) {
				typesCount.put(type, temp);
			} else {
				if (temp > 0)
					typesCount.put(type, typesCount.get(type) + temp);
			}
		}
		if (self != null) {
			if (onlineusers.get(type) == null) {
				onlineusers.put(type, new ArrayList<Map<String, String>>());
			}
			onlineusers.get(type).add(self);
		}

		List<String> lastOnlineUser = new ArrayList<String>();
		List<String> itemonlinecount = new ArrayList<String>();
		List<String> itemallcount = new ArrayList<String>();
		List<Map<String, String>> users = new ArrayList<Map<String, String>>();
		if (onlineusers.get(type) == null) {
			lastOnlineUser.add("");
			itemonlinecount.add("0");
		} else {
			lastOnlineUser.add(onlineusers.get(type)
					.get(onlineusers.get(type).size() - 1).get("USERNAME"));
			itemonlinecount.add(onlineusers.get(type).size() + "");
		}
		if (offlineusers.get(type) != null) {
			if (onlineusers.get(type) == null) {
				onlineusers.put(type, new ArrayList<Map<String, String>>());
			}
			onlineusers.get(type).addAll(offlineusers.get(type));
		}
		users = onlineusers.get(type);
		itemallcount.add(onlineusers.get(type).size() + "");
		if (typesCount.get(type) == null) {
			typesCount.put(type, 0L);
		}
		json.put("username", username);
		json.put("types", type);
		json.put("users", users);
		json.put("lastOnlineUser", lastOnlineUser);
		json.put("itemonlinecount", itemonlinecount);
		json.put("itemallcount", itemallcount);
		json.put("allMessageCount", allMessageCount);
		json.put("typesMessageCount", typesCount);
		output(request, response, json);
	}

	public void getCompanyUsers(HttpServletRequest request,HttpServletResponse response) {
		String companyId = request.getParameter("companyid");
		String username = request.getParameter("username");
		String groupFlag = request.getParameter("groupflag");
		String groupName = request.getParameter("groupname");
		try {
			groupName = URLDecoder.decode(groupName, "utf-8");
			if (groupName.length() > 10) {
				groupName = "<a title='" + groupName + "'>"+ groupName.substring(0, 9) + "...</a>";
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		List<String> userids = null;
		if (!"".equals(groupFlag)) {
			userids = chatDao.getOneGroupUserIds(companyId, username, groupFlag);
		}
		JSONObject json = new JSONObject();
		Set<String> onlineingusers = this.getOnLineUsers(companyId);
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		} else {
			getUserWS().getUserInitInfo(companyId, username);
			obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		HashMap<String, String> map = chatDao.getNotSeeMessageCount(companyId,username);
		List<String> types = new ArrayList<String>();
		HashMap<String, List<Map<String, String>>> onlineusers = new HashMap<String, List<Map<String, String>>>();
		HashMap<String, List<Map<String, String>>> offlineusers = new HashMap<String, List<Map<String, String>>>();
		Iterator<Entry<String, Map<String, String>>> iterator = companyUserInitInfo.entrySet().iterator();
		Entry<String, Map<String, String>> entry = null;
		Map<String, String> user = null;
		Map<String, String> self = null;
		long allMessageCount = 0;
		HashMap<String, Long> typesCount = new HashMap<String, Long>();
		while (iterator.hasNext()) {
			entry = iterator.next();
			user = entry.getValue();
			user.remove("FIR_LOGIN");
			user.remove("ISADMIN");
			user.remove("ORGID");
			user.put(
					"newmessagecount",
					map.get(user.get("USERNAME")) == null ? "0" : map.get(user
							.get("USERNAME")));
			user.put(
					"newmessageStyle",
					map.get(user.get("USERNAME")) == null ? "display:none;"
							: ("display:block;width:"
									+ (6 + user.get("newmessagecount").length() * 6) + "px;"));
			user.put(
					"status",
					username.equals(user.get("USERNAME")) ? "在线"
							: (onlineingusers.contains(user.get("USERNAME")
									.replace("@", "\\40")) ? "在线" : "离线"));
			if ("在线".equals(user.get("status"))) {
				user.put("useritempicStyle", "user-online");
			} else {
				user.put("useritempicStyle", "user-offline");
			}

			if (null != user.get("FULLNAME")) {
				user.put(
						"showname",
						(user.get("FULLNAME").length() > 10 ? user.get(
								"FULLNAME").substring(0, 10)
								+ "..." : user.get("FULLNAME"))
								+ (username.equals(user.get("USERNAME")) ? "(自己)"
										: ""));
			} else {
				user.put(
						"showname",
						(user.get("USERNAME").length() > 10 ? user.get(
								"USERNAME").substring(0, 10)
								+ "..." : user.get("USERNAME"))
								+ (username.equals(user.get("USERNAME")) ? "(自己)"
										: ""));
			}

			String type = "";
			if (userids == null) {
				type = "公司所有成员";
			} else {
				if (userids.contains(user.get("ID"))) {
					type = "组内成员[" + groupName + "]";
				} else {
					type = "组外成员";
				}
			}
			if (!types.contains(type))
				types.add(type);
			if (username.equals(user.get("USERNAME"))) {
				self = user;
				continue;
			}
			if ("在线".equals(user.get("status"))) {
				if (onlineusers.get(type) == null) {
					onlineusers.put(type, new ArrayList<Map<String, String>>());
				}
				onlineusers.get(type).add(user);
			} else {
				if (offlineusers.get(type) == null) {
					offlineusers.put(type, new ArrayList<Map<String, String>>());
				}
				offlineusers.get(type).add(user);
			}
			long temp = Long.parseLong(user.get("newmessagecount"));
			allMessageCount += temp;
			if (typesCount.get(type) == null) {
				typesCount.put(type, temp);
			} else {
				if (temp > 0)
					typesCount.put(type, typesCount.get(type) + temp);
			}
		}
		if (self != null) {
			String type = "";
			if (userids == null) {
				type = "公司所有成员";
			} else {
				if (userids.contains(self.get("ID"))) {
					type = "组内成员[" + groupName + "]";
				} else {
					type = "组外成员";
				}
			}
			if (onlineusers.get(type) == null) {
				onlineusers.put(type, new ArrayList<Map<String, String>>());
			}
			onlineusers.get(type).add(self);
		}
		List<String> lastOnlineUser = new ArrayList<String>();
		List<String> itemonlinecount = new ArrayList<String>();
		List<String> itemallcount = new ArrayList<String>();
		List<List<Map<String, String>>> users = new ArrayList<List<Map<String, String>>>();
		if (types.contains("组内成员[" + groupName + "]")) {
			types.remove("组内成员[" + groupName + "]");
			types.add(0, "组内成员[" + groupName + "]");
		}
		for (String type : types) {
			if (onlineusers.get(type) == null) {
				lastOnlineUser.add("");
				itemonlinecount.add("0");
			} else {
				lastOnlineUser.add(onlineusers.get(type)
						.get(onlineusers.get(type).size() - 1).get("USERNAME"));
				itemonlinecount.add(onlineusers.get(type).size() + "");
			}
			if (offlineusers.get(type) != null) {
				if (onlineusers.get(type) == null) {
					onlineusers.put(type, new ArrayList<Map<String, String>>());
				}
				onlineusers.get(type).addAll(offlineusers.get(type));
			}
			//liuwei2160323在线用户新消息前置
			Collections.sort(onlineusers.get(type), new Comparator<Map<String,String>>(){  
				@Override
				public int compare(Map<String, String> o1,Map<String, String> o2) {
					Integer s1 = Integer.parseInt(o1.get("newmessagecount"));  
		            Integer s2 = Integer.parseInt(o2.get("newmessagecount")); 
		            if(s1<=s2) {  
		             return 1;  
		            }else {  
		             return -1;  
		            }  
				}  
	        }); 
			users.add(onlineusers.get(type));
			itemallcount.add(onlineusers.get(type).size() + "");
			if (typesCount.get(type) == null) {
				typesCount.put(type, 0L);
			}
		}
		json.put("username", username);
		json.put("types", types);
		json.put("users", users);
		json.put("lastOnlineUser", lastOnlineUser);
		json.put("itemonlinecount", itemonlinecount);
		json.put("itemallcount", itemallcount);
		json.put("allMessageCount", allMessageCount);
		json.put("typesMessageCount", typesCount);
		output(request, response, json);
	}

	@SuppressWarnings("unchecked")
	public Map getCompanyUsersForGroupSet(Map<String, String> params) {
		String companyId = params.get("companyId");
		String username = params.get("username");
		String flag = params.get("flag");
		Map<String, Object> json = new HashMap<String, Object>();
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		//此处是用于移交分类删除自己的信息 liuwei
		for(String map: companyUserInitInfo.keySet()){
			if("transfer".equals(flag)&& username.equals(map)){
				companyUserInitInfo.remove(username);
				break;
			}
		}
		// Map<String, String> orgInfo = null ;
		// obj = CacheUtils.get(this.getCompLocator(),
		// "companyOrgInfo"+companyId) ;
		// if(obj != null){
		// orgInfo = (Map<String, String>) obj ;
		// }
		String groupid = params.get("groupid");

		// //日志---
		// String userid = request.getParameter("userid");
		// HashMap<String, String> log = new HashMap<String, String>();
		// log.put("ip", request.getRemoteAddr());
		// log.put("userid", username);
		// log.put("loginfo", "获取用户列表，公司ID【"+companyId+"】");
		// log.put("module", "联系人/群组");
		// log.put("username",username);
		// log.put("type", "operation");
		// log.put("operate", "联系人/群组");
		// String companyName =
		// companyUserInitInfo.get(username).get("COMPANYNAME");
		// log.put("companyName", companyName != null?companyName:companyId);
		// LogUtils.saveBaseLog(compLocator, log);
		// //日志--end

		List<String> groupHasdUsers = null;
		if (StringUtils.isBlank(groupid)) {// 加入空验证
			groupid = null;
		}
		if (groupid != null) {
			groupHasdUsers = chatDao.getGroupUsersByGroupId(groupid);
		}
		Iterator<Entry<String, Map<String, String>>> iterator = companyUserInitInfo
				.entrySet().iterator();
		Entry<String, Map<String, String>> entry = null;
		Map<String, String> user = null;
		List<Map<String, String>> outUsers = new ArrayList<Map<String, String>>();
		List<Map<String, String>> inUsers = new ArrayList<Map<String, String>>();
		while (iterator.hasNext()) {
			entry = iterator.next();
			user = entry.getValue();
			user.remove("FIR_LOGIN");
			user.remove("ISADMIN");
			user.remove("ORGID");
			if (groupid != null) {
				if (groupHasdUsers.contains(user.get("ID"))) {
					inUsers.add(user);
				} else {
					outUsers.add(user);
				}
			} else {
				if (username.equals(user.get("USERNAME"))) {
					inUsers.add(user);
				} else {
					outUsers.add(user);
				}
			}
		}
		if (groupid != null)
			json.put(
					"oldUsers",
					groupHasdUsers
							.toString()
							.substring(1,
									groupHasdUsers.toString().length() - 1)
							.replaceAll(", ", ","));
		json.put("outUsers", outUsers);
		json.put("inUsers", inUsers);
		json.put("username", username);
		// output(request, response, json);
		return json;
	}

	@SuppressWarnings("unchecked")
	public void getCompanyUsersForGroupSet(String companyId, String username,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		String groupid = request.getParameter("groupid");
		List<String> groupHasdUsers = null;
		if (StringUtils.isBlank(groupid)) {// 加入空验证
			groupid = null;
		}
		if (groupid != null) {
			groupHasdUsers = chatDao.getGroupUsersByGroupId(groupid);
		}
		Iterator<Entry<String, Map<String, String>>> iterator = companyUserInitInfo
				.entrySet().iterator();
		Entry<String, Map<String, String>> entry = null;
		Map<String, String> user = null;
		List<Map<String, String>> outUsers = new ArrayList<Map<String, String>>();
		List<Map<String, String>> inUsers = new ArrayList<Map<String, String>>();
		while (iterator.hasNext()) {
			entry = iterator.next();
			user = entry.getValue();
			user.remove("FIR_LOGIN");
			user.remove("ISADMIN");
			user.remove("ORGID");
			if (groupid != null) {
				if (groupHasdUsers.contains(user.get("ID"))) {
					inUsers.add(user);
				} else {
					outUsers.add(user);
				}
			} else {
				if (username.equals(user.get("USERNAME"))) {
					inUsers.add(user);
				} else {
					outUsers.add(user);
				}
			}
		}
		if (groupid != null)
			json.put(
					"oldUsers",
					groupHasdUsers
							.toString()
							.substring(1,
									groupHasdUsers.toString().length() - 1)
							.replaceAll(", ", ","));
		json.put("outUsers", outUsers);
		json.put("inUsers", inUsers);
		json.put("username", username);
		output(request, response, json);
	}

	@SuppressWarnings("unchecked")
	public void getCompanyUsersForGroupSetAndNotJoin(String companyId,
			String username, HttpServletRequest request,
			HttpServletResponse response) {
		
		//===============================================================
		
		String userid = request.getParameter("userid");
		JSONObject json = new JSONObject();
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		if (null == companyUserInitInfo || companyUserInitInfo.isEmpty()) {
			companyUserInitInfo = getUserWS().companyUserInitInfo(companyId);
			CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
					+ companyId, companyUserInitInfo);
		}
		String groupid = request.getParameter("groupid");
		String classId = request.getParameter("classId");
		String flag = request.getParameter("flag");
		List<String> groupHasdUsers = null;
		if (StringUtils.isBlank(groupid)) {// 加入空验证
			groupid = null;
		}
		if (groupid != null) {
			groupHasdUsers = chatDao.getGroupUsersByGroupId(groupid);
		}
		Iterator<Entry<String, Map<String, String>>> iterator = companyUserInitInfo
				.entrySet().iterator();
		Entry<String, Map<String, String>> entry = null;
		Map<String, String> user = null;
		List<Map<String, String>> outUsers = new ArrayList<Map<String, String>>();
		List<Map<String, String>> inUsers = new ArrayList<Map<String, String>>();
		List<Map<String, String>> noJoinUsers = new ArrayList<Map<String, String>>();
		// 查出当前未激活的用户
		noJoinUsers = chatDao.getGroupUsersByNotJoin(companyId, classId);
		//List<Map<String, String>> noJoinUsers = chatDao.getGroupUsersByNotJoin(companyId, classId);
		while (iterator.hasNext()) {
			entry = iterator.next();
			user = entry.getValue();
			user.remove("FIR_LOGIN");
			user.remove("ISADMIN");
			user.remove("ORGID");
			if (groupid != null) {
				if (groupHasdUsers.contains(user.get("ID"))) {
					inUsers.add(user);
				} else {
					outUsers.add(user);
				}
			} else {
				if (username.equals(user.get("USERNAME"))) {
					inUsers.add(user);
				} else {
					outUsers.add(user);
				}
			}
		}
		if (groupid != null)
			json.put(
					"oldUsers",
					groupHasdUsers
							.toString()
							.substring(1,
									groupHasdUsers.toString().length() - 1)
							.replaceAll(", ", ","));
		json.put("outUsers", outUsers);
		json.put("inUsers", inUsers);
		json.put("noJoinUsers", noJoinUsers);
		json.put("username", username);
		Map<String, Object> rtnMap = chatDao.getGroupUserIsAdminInfo(groupid,
				flag, companyId);// 查出admin
		json.put("userid", rtnMap.get("userId"));
		if (request.getParameter("callback") != null
				&& "androidInter".equals(request.getParameter("callback"))) {
			try {
				response.setContentType("text/javascript;charset=UTF-8");
				response.getWriter().println(json);
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			output(request, response, json);
		}
	}

	@SuppressWarnings("unused")
	private String getOrgName(Map<String, String> orgInfo, String orgId,
			String companyId) {
		if (orgInfo.get(orgId) == null) {
			orgInfo = getUserWS().getOrgInfo(companyId);
			CacheUtils.set(this.getCompLocator(), "companyOrgInfo" + companyId,
					orgInfo);
		}
		return orgInfo.get(orgId);
	}

	/**
	 * 判断openfire用户的状态 返回值 : online - 用户在线; offline - 用户离线 说明 ：必须要求 openfire加载
	 * presence 插件，同时设置任何人都可以访问
	 */
	@SuppressWarnings("unchecked")
	private Set<String> getOnLineUsers(String companyId) {
		Set<String> users = null;
		String mag = post("type=getonlineusernames&secret=flyingsoft&resource=onlinefile"
				+ companyId);// 获取openfire资源
		if (mag != null) {
			users = (Set<String>) JSON.parse(mag);
		}
		return users;
	}

	@SuppressWarnings("unchecked")
	public void getGroupsByUsername(String companyId, String username,
			HttpServletRequest request, HttpServletResponse response) {
		
		String cache_username = (String) CacheUtils.get(this.getCompLocator(),
				"username_"+username);
		List<HashMap<String, String>> groups=new ArrayList<HashMap<String,String>>();
		
		if(request.getParameter("callback")!=null && "androidInter".equals(request.getParameter("callback"))){
			groups = chatDao.getGroupsAndClassByUsername(companyId, username);
		}else{
			/** 通过公司编号、用户名来获取分组信息 */
			groups = chatDao.getGroupsByUsername(companyId, username);
		}
		
		/** 定义变量接收缓存中的分组头像信息 */
		HashMap<String, String> oldGroupImages = (HashMap<String, String>) CacheUtils
				.get(this.getCompLocator(), "company_id_username_" + companyId + "_" + username);
		
//		logger.error("getGroupsByUsername方法————》缓存中的分组头像数量"+oldGroupImages.size());

		// 如果缓存中不是空值的话
		if (oldGroupImages != null) {
			
			List<String> strs = new ArrayList<String>();
			// 拿到缓存中的数据数量
			for (String string : oldGroupImages.keySet()) {
				strs.add(string);
			}
			
			if (cache_username==null) {
				
				doImageMath(companyId, username , null, groups);
				/** 如果数据不一致 重新加载一遍 */
				oldGroupImages = (HashMap<String, String>) CacheUtils.get(
						this.getCompLocator(), "company_id_username_" + companyId + "_" + username);
				
				CacheUtils.set(this.getCompLocator(), "username_"+username, username);
					
			}
		}

		try {
			/**
			 * @author LiuKang 如果所加载的公司的群分组的数量不是空的 那么判断缓存中的数据是不是空的
			 * */
			if (null != groups && groups.size() > 0) {

				/**
				 * 如果缓存中的数据是空的 说明之前没有加载过此公司的信息 那么进行全新的加载
				 * */
				if (oldGroupImages == null || oldGroupImages.size()!=groups.size()) {
					
					doImageMath(companyId, username , null, groups);
					CacheUtils.set(this.getCompLocator(), "username_"+username, username);
				} else {
					for (HashMap<String, String> hashMap : groups) {
						// 拿到缓存中的地址
						String cachePath = oldGroupImages
								.get(hashMap.get("ID"));
						
						// 截取字符串
						cachePath = cachePath.substring(cachePath
								.indexOf("files"));
						
						// 替换原地址
						hashMap.put("PORTRAITS", cachePath);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//liuwei2160323在线用户新消息前置
		Collections.sort(groups, new Comparator<Map<String,String>>(){  
			@Override
			public int compare(Map<String, String> o1,Map<String, String> o2) {
				Integer s1 = Integer.parseInt(o1.get("newmessagecount"));  
	            Integer s2 = Integer.parseInt(o2.get("newmessagecount")); 
	            if(s1<=s2) {  
	             return 1;  
	            }else {  
	             return -1;  
	            }  
			}  
        }); 
		
		JSONObject json = new JSONObject();
		if (groups.isEmpty()) {
			json.put("size", "0");
		} else {
			json.put("size", groups.size());
			json.put("groups", groups);
		}
		output(request, response, json);
	}

	public HashMap<String, String> getImage(String conpanyId) {

		HashMap<String, String> groupsImgMap = chatDao
				.getGroupsByCompanyIdsImgMap(conpanyId);

		return groupsImgMap;

	}

	@SuppressWarnings("unchecked")
	public void doImageMath(String companyId, String username , String groupId,
			List<HashMap<String, String>> groups) {

		/** 用来记录分组数 */
		int flag = 0;
		/** 保存图片的来源 */
		URL url;
		/** 缓冲区 */
		byte[] data;
		/** 网路地址 */
		HttpURLConnection conn;
		/** 创建输入流 */
		InputStream inStream;
		/** 创建单个文件 */
		File imageFile;
		/** 创建输出流 */
		FileOutputStream outStream;
		/** 创建存储组内成员图片的集合 */
		HashMap<String, String[]> groupUsersImages = new HashMap<String, String[]>();

		/** 用来存储已经生成好的组合图片 */
		HashMap<String, String> group_id_image = new HashMap<String, String>();

		/** 获取组内包含图片 */
		HashMap<String, String> groupsImgMap = chatDao
				.getGroupsByCompanyIdsImgMap(companyId);
		
		/**
		 * 使用Foreach循环从分组信息中，以Map的形式拿到当前分组的单个消息
		 * */
		for (HashMap<String, String> dHashMaps : groups) {
			/** 定义集合存储拆产生的多个文件 */
			List<File> files = new ArrayList<File>();
			/**
			 * 拿到当前分组ID并写入默认路径的图片 注意：这里拿到的是一个图片的全部拼接路径
			 * xxxx.jpeg,xxxx,jpeg,xxxx.jpeg
			 * */
			dHashMaps.put("PORTRAITS", groupsImgMap.get(dHashMaps.get("ID")));

			/**
			 * 将路径保存成字符串 拆分成字符串数组
			 * */
			String s = dHashMaps.get("PORTRAITS");
			if ("".equals(s) || s == null) {
				s = "files/headimage/profle.png";
			}
			String[] strs = s.split(",");

			groupUsersImages.put(dHashMaps.get("ID"), strs);

			try {
				for (int i = 0; i < strs.length; i++) {

					File file = new File(urlPath + strs[i]);
					if (file.exists()) {
						data = readInputStream(new FileInputStream(urlPath + strs[i]));
						
						// new涓�釜鏂囦欢瀵硅薄鐢ㄦ潵淇濆瓨鍥剧墖锛岄粯璁や繚瀛樺綋鍓嶅伐绋嬫牴鐩綍
						imageFile = new File("thisGroupsImage_" + companyId +"_" + username + "_"+ i);
						// 鍒涘缓杈撳嚭娴�
						outStream = new FileOutputStream(imageFile);
						// 鍐欏叆鏁版嵁
						outStream.write(data);
						// 鍏抽棴杈撳嚭娴�
						outStream.close();
						// 灏嗗緱鍒扮殑鍥剧墖瀛樺埌闆嗗悎涓�
						files.add(imageFile);
					}
				}

				// 拿到当前分组的ID
				String group_id = groups.get(flag++).get("ID");

				if (groupId == null || "".equals(groupId)) {
					// 将改分组组装好的图片存到集合中
					group_id_image.put(group_id,
							imageUtil.imageManager(files, companyId, group_id));
				} else {
					group_id_image = (HashMap<String, String>) CacheUtils.get(
							this.getCompLocator(), "company_id_username_" + companyId + "_" + username);
					if (group_id_image == null) {
						group_id_image = new HashMap<String, String>();
					} else {
						group_id_image.put(groupId, imageUtil.imageManager(
								files, companyId, group_id));
					}

				}

				// 将该分组的生成结果里的图片以刚生成的新图片替换掉
				dHashMaps.put("PORTRAITS", "files" + File.separator +"groupImages" + File.separator + companyId
						+ "_" + group_id + ".jpeg");

			} catch (Exception e) {
				// 捕获异常
				e.printStackTrace();
			}

		}// The Foreach Was END

		CacheUtils.set(this.getCompLocator(), "company_id_groups_" + companyId,
				groupUsersImages);

		CacheUtils.set(this.getCompLocator(), "company_id_username_" + companyId + "_" + username,
				group_id_image);

	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> doGroupImageMath(String companyId, String username , String groupId,
			List<Map<String, String>> groups) {

		/** 用来记录分组数 */
		int flag = 0;
		/** 保存图片的来源 */
		URL url;
		/** 缓冲区 */
		byte[] data;
		/** 网路地址 */
		HttpURLConnection conn;
		/** 创建输入流 */
		InputStream inStream;
		/** 创建单个文件 */
		File imageFile;
		/** 创建输出流 */
		FileOutputStream outStream;
		/** 创建存储组内成员图片的集合 */
		HashMap<String, String[]> groupUsersImages = new HashMap<String, String[]>();

		/** 用来存储已经生成好的组合图片 */
		HashMap<String, String> group_id_image = new HashMap<String, String>();

		/**
		 * 使用Foreach循环从分组信息中，以Map的形式拿到当前分组的单个消息
		 * */
		for (Map<String, String> dHashMaps : groups) {
			/** 定义集合存储拆产生的多个文件 */
			List<File> files = new ArrayList<File>();
			/**
			 * 拿到当前分组ID并写入默认路径的图片 注意：这里拿到的是一个图片的全部拼接路径
			 * xxxx.jpeg,xxxx,jpeg,xxxx.jpeg
			 * */
//			dHashMaps.put("PORTRAITS", groupsImgMap.get(dHashMaps.get("ID")));

			/**
			 * 将路径保存成字符串 拆分成字符串数组
			 * */
			String s = dHashMaps.get("PORTRAITS");
			String flag_ = dHashMaps.get("FLAG");
			if ("".equals(s) || s == null) {
				s = "files/headimage/profle.png";
			}
			String[] strs = s.split(",");

			try {
				for (int i = 0; i < strs.length; i++) {
					if (logoImg.equals(strs[i])) {
						continue;
					}
					File file = new File(urlPath + strs[i]);
					if (file.exists()) {
						data = readInputStream(new FileInputStream(urlPath + strs[i]));
						
						// new涓�釜鏂囦欢瀵硅薄鐢ㄦ潵淇濆瓨鍥剧墖锛岄粯璁や繚瀛樺綋鍓嶅伐绋嬫牴鐩綍
						imageFile = new File("thisGroupsImage_" + companyId +"_" + username + "_"+ i);
						// 鍒涘缓杈撳嚭娴�
						outStream = new FileOutputStream(imageFile);
						// 鍐欏叆鏁版嵁
						outStream.write(data);
						// 鍏抽棴杈撳嚭娴�
						outStream.close();
						// 灏嗗緱鍒扮殑鍥剧墖瀛樺埌闆嗗悎涓�
						files.add(imageFile);
					}

				}
				
				if (files.size()<1) {
					File file = new File(urlPath + logoImg);
					data = readInputStream(new FileInputStream(urlPath + logoImg));
					imageFile = new File("thisGroupsImage_" + companyId +"_" + username + "_"+ "logoImg");
					outStream = new FileOutputStream(imageFile);
					outStream.write(data);
					outStream.close();
					files.add(file);
				}



				if (groupId == null || "".equals(groupId)) {
					// 将改分组组装好的图片存到集合中
					group_id_image.put(flag_,
							imageUtil.groupImageManager(files, companyId, flag_));
				} else {
					group_id_image = (HashMap<String, String>) CacheUtils.get(
							this.getCompLocator(), "company_id_class_username_" + companyId + "_" + flag_);
					if (group_id_image == null) {
						group_id_image = new HashMap<String, String>();
					} else {
						group_id_image.put(groupId, imageUtil.groupImageManager(
								files, companyId, flag_));
					}

				}

				// 将该分组的生成结果里的图片以刚生成的新图片替换掉
				dHashMaps.put("PORTRAITS", "files/groupClassImages/" + companyId
						+ "_" + flag_ + ".jpeg");

			} catch (Exception e) {
				// 捕获异常
				e.printStackTrace();
			}

		}// The Foreach Was END

		CacheUtils.set(this.getCompLocator(), "company_id_class_groups_" + companyId,
				groupUsersImages);

		CacheUtils.set(this.getCompLocator(), "company_id_class_username_" + companyId + "_" + username,
				group_id_image);
		return groups;
	}
	

	public byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}

	public Long saveNotSeeMessage(HashMap<String, String> params,
			HttpServletRequest request) {
		String companyId = params.get("companyId");
		String username = params.get("username");
		String from = params.get("from");
		String fromCnName = params.get("fromCnName");
		String groupFlag = params.get("groupFlag");
		String isGroup = params.get("isGroup");
		String content = params.get("content");
		String date = params.get("date");
		String time = params.get("time");
		Long notSeeMsgId = chatDao.saveNotSeeMessage(companyId, username,
				content, date, time, from, isGroup, groupFlag, fromCnName);

		// 日志---
		if (notSeeMsgId > 0) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "保存未读消息【" + from + "】");
			log.put("module", "消息");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			}
			String companyName = null;
			try {
				companyName = companyUserInitInfo.get(username).get(
						"COMPANYNAME");
			} catch (Exception e) {
			}
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
		}
		// 日志--end
		return notSeeMsgId;
	}

	public void dropNotSeeMeesage(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String username = request.getParameter("username");
		String from = request.getParameter("from");
		String notSeeMsgIdStr = request.getParameter("notSeeMsgId");

		long notSeeMsgId = 0l;
		try {
			notSeeMsgId = Long.parseLong(notSeeMsgIdStr);
		} catch (Exception e1) {
			JSONObject json = new JSONObject();
			json.put("isOk", "false");
			output(request, response, json);
		}

		boolean isOk = chatDao.dropNotSeeMeesage(companyId, from, username,
				notSeeMsgId);

		// 日志---
		if (isOk) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "删除未读消息【" + from + "】");
			log.put("module", "消息");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			}
			String companyName = null;
			try {
				companyName = companyUserInitInfo.get(username).get(
						"COMPANYNAME");
			} catch (Exception e) {
			}
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
		}
		// 日志--end
		JSONObject json = new JSONObject();
		json.put("isOk", isOk + "");
		output(request, response, json);
	}

	public void getOldNotSeeMessage(HttpServletRequest request,
			HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String receiver = request.getParameter("receiver");
		String username = request.getParameter("username");
		String isGroup = request.getParameter("isGroup");

		// 日志---
		String userid = request.getParameter("userid");
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", request.getRemoteAddr());
		log.put("userid", username);
		log.put("loginfo", "保存未读消息，公司ID【" + companyId + "】");
		log.put("module", "消息");
		log.put("username", username);
		log.put("type", "operation");
		log.put("operate", "联系人/群组");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		String companyName = companyUserInitInfo.get(username).get(
				"COMPANYNAME");
		log.put("companyName", companyName != null ? companyName : companyId);
		LogUtils.saveBaseLog(compLocator, log);
		// 日志--end

		List<HashMap<String, String>> msgs = chatDao.getOldNotSeeMessage(
				companyId, receiver, username, isGroup);
		JSONObject json = new JSONObject();
		json.put("msgs", msgs);
		output(request, response, json);
	}

	public void createGroup(HttpServletRequest request,
			HttpServletResponse response) {
		// 用于保存返回的分组ID 用于后续的生成分组图
		String groupId;
		// 用于标识是否插入数据成功！
		boolean isOk;
		String companyId = request.getParameter("companyId");
		String username = request.getParameter("username");
		String groupuserids = request.getParameter("groupuserids");
		String manageruserid = request.getParameter("manageruserid");
		String groupname = request.getParameter("groupname");
		String groupremark = request.getParameter("groupremark");
		try {
			groupname = URLDecoder.decode(groupname, "utf-8");
			groupremark = URLDecoder.decode(groupremark, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String groupflag = "c" + companyId + "g" + System.currentTimeMillis();
		Date dt = new Date();
		String time = df.format(dt);

		// 调用DAO方法获取ID和状态组合的字符串
		String groupId_isOk = chatDao.createGroup(companyId, username,
				groupuserids, manageruserid, groupname, groupremark, groupflag,
				time);

		// 将其拆分
		String[] strs = groupId_isOk.split(",");
		groupId = strs[0];
		isOk = Boolean.valueOf(strs[1]).booleanValue();
		JSONObject json = new JSONObject();
		json.put("isOk", isOk);
		if (isOk) {
			json.put("GROUPNAME", groupname);
			json.put("REMARK", groupremark);
			json.put("CREATETIME", time);
			json.put("FLAG", groupflag);
			// BroadcastUtils.broadcast(username,
			// groupflag+"@broadcast."+BroadcastUtils.openfireServerName,
			// "broadcast-creategroup:"+groupflag, Message.Type.CHAT);
			// 日志---
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "创建群组【" + groupname + "】");
			log.put("module", "联系人/群组");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			}
			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
			// =-=-=

			// 拿到一个或多个分组的信息
			List<HashMap<String, String>> anOrAnyGroup = getAnOrAnyGroup(
					username, groupId, companyId);

			// 执行组装图片的方法
			doImageMath(companyId, username , groupId, anOrAnyGroup);
			String groupusernames = request.getParameter("groupusernames");
			// 20151110 创建群组安卓
			if (request.getParameter("callback") != null
					&& request.getParameter("callback").equals("androidInter")) {
				String arg = "secret=flyingsoft&type=add_group&groups="
						+ groupflag + "&username="
						+ groupusernames.replace("@", "\\40");
				post(arg);
			}
		}
		output(request, response, json);
	}

	/** 获取一个或多个分组的详情 (取决于传递的值) */
	public List<HashMap<String, String>> getAnOrAnyGroup(String username,
			String groupId, String companyId) {
		/** 定义集合保存数据 */
		List<HashMap<String, String>> anOrAnyGroup = null;
		/** 如果传来的分组id是空的 就意味着是加载所有的操作 */
		if (groupId == null) {
			anOrAnyGroup = chatDao.getGroupsByUsername(companyId, username);

			/** 否则不是空的 就意味着操作的是某一个分组 */
		} else {
			anOrAnyGroup = chatDao.getGroupByGroupId(username, groupId,
					companyId);
		}
		/** 返回方法的结果 */
		return anOrAnyGroup;
	}

	public Boolean saveHistoryMessage(HashMap<String, String> params,
			HttpServletRequest request) {
		String companyId = params.get("companyId");
		String username = params.get("username");
		String from = params.get("from");
		String fromCnName = params.get("fromCnName");
		String isGroup = params.get("isGroup");
		String content = params.get("content");
		String styleTpl = params.get("styleTpl");
		String fileFlag = params.get("fileFlag");
		String date = params.get("date");
		String time = params.get("time");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		String userId = "0";
		if ("1".equals(isGroup)) {
			userId = username;
		} else {
			if (!"fyBot".equals(username)) {
				userId = companyUserInitInfo.get(username).get("ID");
			}
		}
		String fromId = "0";
		if (!"fyBot".equals(from)) {
			fromId = companyUserInitInfo.get(from).get("ID");
		}
		boolean isOK = chatDao.saveHistoryMessage(companyId, userId, content,
				date, time, fromId, isGroup, fromCnName, styleTpl, fileFlag,
				companyUserInitInfo);
		// 日志---
		if (!"fyBot".equals(username) && isOK) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", from);
			log.put("loginfo", "保存历史消息to【" + username + "】");
			log.put("module", "消息");
			log.put("username", from);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			String companyName = null;
			if (!"1".equals(isGroup)) {
				companyName = companyUserInitInfo.get(username).get(
						"COMPANYNAME");
			}
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}
		return isOK;
	}
	
	

	/**  lujixiang 20151113     注释,将get方式改为post方式,修复乱码
	public void saveHistoryMessageReturnID(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String username = request.getParameter("username");
		String from = request.getParameter("from");
		String fromCnName = request.getParameter("fromCnName");
		String isGroup = request.getParameter("isGroup");
		String content = request.getParameter("content");
		String styleTpl = request.getParameter("styleTpl");
		String fileFlag = request.getParameter("fileFlag");
		String date = request.getParameter("date");
		String time = request.getParameter("time");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		String userId = "0";
		if ("1".equals(isGroup)) {
			userId = username;
		} else {
			if (!"fyBot".equals(username)) {
				userId = companyUserInitInfo.get(username).get("ID");
			}
		}
		String fromId = "0";
		if (!"fyBot".equals(from)) {
			fromId = companyUserInitInfo.get(from).get("ID");
		}
		String msgid = chatDao.saveHistoryMessageReturnID(companyId, userId,
				content, date, time, fromId, isGroup, fromCnName, styleTpl,
				fileFlag, companyUserInitInfo);

		// 日志---
		if (!"fyBot".equals(username) && Integer.parseInt(msgid) > 0) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", from);
			log.put("loginfo", "保存历史消息to【" + username + "】");
			log.put("module", "消息");
			log.put("username", from);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			String companyName = null;
			if (!"1".equals(isGroup)) {
				companyName = companyUserInitInfo.get(username).get(
						"COMPANYNAME");
			}
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}
		JSONObject json = new JSONObject();
		json.put("isOK", Integer.parseInt(msgid) > 0 ? true : false);
		json.put("msgid", msgid);
		json.put("username", username);
		json.put("fromCnName", fromCnName);
		output(request, response, json);
	}
	**/
	
	
	public Map saveHistoryMessageReturnID(HashMap<String, String> params,
											HttpServletRequest request) {
		
		String companyId = params.get("companyId");
		String username = params.get("username");
		String from = params.get("from");
		String fromCnName = params.get("fromCnName");
		String isGroup = params.get("isGroup");
		String content = params.get("content");
		String styleTpl = params.get("styleTpl");
		String fileFlag = params.get("fileFlag");
		String date = params.get("date");
		String time = params.get("time");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		String userId = "0";
		if ("1".equals(isGroup)) {
			userId = username;
		} else {
			if (!"fyBot".equals(username)) {
				userId = companyUserInitInfo.get(username).get("ID");
			}
		}
		String fromId = "0";
		if (!"fyBot".equals(from)) {
			fromId = companyUserInitInfo.get(from).get("ID");
		}
		String msgid = chatDao.saveHistoryMessageReturnID(companyId, userId,
				content, date, time, fromId, isGroup, fromCnName, styleTpl,
				fileFlag, companyUserInitInfo);

		// 日志---
		if (!"fyBot".equals(username) && Integer.parseInt(msgid) > 0) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", from);
			log.put("loginfo", "保存历史消息to【" + username + "】");
			log.put("module", "消息");
			log.put("username", from);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			String companyName = null;
			if (!"1".equals(isGroup)) {
				companyName = companyUserInitInfo.get(username).get(
						"COMPANYNAME");
			}
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("isOK", Integer.parseInt(msgid) > 0 ? "true" : "false");
		rtnMap.put("msgid", msgid);
		rtnMap.put("username", username);
		rtnMap.put("fromCnName", fromCnName);
		rtnMap.put("toUserFullName", companyUserInitInfo.get(username)!=null ?companyUserInitInfo.get(username).get("FULLNAME"):"");
		return rtnMap;
	}
	
	

	public void getHistoryMessage(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String receiver = request.getParameter("receiver");
		String username = request.getParameter("username");
		String isGroup = request.getParameter("isGroup");
		String limit = request.getParameter("limit");
		String page = request.getParameter("page");
		String skip = request.getParameter("skip");
		String keyword = request.getParameter("keyword");
		String joindate = request.getParameter("joindate");
		String jointime = request.getParameter("jointime");

		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", request.getRemoteAddr());
		log.put("userid", username);
		log.put("loginfo", "获取历史消息to【" + receiver + "】");
		log.put("module", "消息");
		log.put("username", username);
		log.put("type", "operation");
		log.put("operate", "联系人/群组");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		String companyName = companyUserInitInfo.get(username).get(
				"COMPANYNAME");
		log.put("companyName", companyName != null ? companyName : companyId);
		LogUtils.saveBaseLog(compLocator, log);
		// 日志--end

		try {
			if (!"".equals(keyword))
				keyword = URLDecoder.decode(keyword, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// List<HashMap<String, String>> msgs =
		// chatDao.getHistoryMessage(companyId, receiver, username, isGroup,
		// limit, page, skip, keyword);
		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put("keyWord", keyword);
		searchMap.put("companyId", companyId);
		int start = 0;
		if (Integer.parseInt(page) == 1) {
			if (!StringUtils.isBlank(skip) && Integer.parseInt(skip) != 0) {
				start = Integer.parseInt(skip);
			}
		} else {
			start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
		}
		String userId = companyUserInitInfo.get(username).get("ID");
		String receiverId = "0";
		if ("1".equals(isGroup)) {
			userId = username;
			receiverId = receiver;
		} else {
			if (!"fyBot".equals(receiver)) {
				receiverId = companyUserInitInfo.get(receiver).get("ID");
			}
		}
		searchMap.put("start", start + "");
		searchMap.put("limit", limit);
		searchMap.put("idSeq", "");
		searchMap.put("createrId", "");

		searchMap.put("searchType", "2");// 1 是文件搜索，2是消息
		searchMap.put("receiver", receiverId);
		searchMap.put("username", userId);
		searchMap.put("isGroup", isGroup);// 非1代表 chad0
		searchMap.put("joindate", joindate);
		searchMap.put("jointime", jointime);
		List<Map<String, String>> msgs = getLuceneWS().search(searchMap);
		Collections.reverse(msgs);
		JSONObject json = new JSONObject();
		json.put("msgs", msgs);
		output(request, response, json);
	}

	@SuppressWarnings("unchecked")
	public void getOneGroupUsers(HttpServletRequest request,
			HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String username = request.getParameter("username");
		String groupFlag = request.getParameter("groupFlag");

		// 日志--start
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", request.getRemoteAddr());
		log.put("userid", username);
		log.put("loginfo", "获取群组用户,公司ID【" + companyId + "】");
		log.put("module", "联系人/群组");
		log.put("username", username);
		log.put("type", "operation");
		log.put("operate", "联系人/群组");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		if (null == companyUserInitInfo || companyUserInitInfo.isEmpty()) {
			companyUserInitInfo = getUserWS().companyUserInitInfo(companyId);
			CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
					+ companyId, companyUserInitInfo);
		}
		String companyName = companyUserInitInfo.get(username).get(
				"COMPANYNAME");
		String userId = companyUserInitInfo.get(username).get("ID");
		log.put("companyName", companyName != null ? companyName : companyId);
		LogUtils.saveBaseLog(compLocator, log);
		// 日志--end

		// List<String> userids = chatDao.getOneGroupUserIds(companyId,
		// username, groupFlag);
		List<Map<String, String>> useridlists = chatDao.getOneGroupUser(
				companyId, groupFlag);
		List<String> userids = new ArrayList<String>();
		String isgroupAdmin = "0";
		for (Map<String, String> str : useridlists) {
			userids.add(str.get("userId"));
			if (str.get("userId").equals(userId)) {
				isgroupAdmin = str.get("isAdmin");
			}
		}
		// 获取分组信息
		// Map<String, String> group = chatDao.getGroupDetail(companyId,
		// groupFlag);
		JSONObject json = new JSONObject();
		int online = 0;
		String status = null;
		Set<String> onlineingusers = this.getOnLineUsers(companyId);
		List<Map<String, String>> onlines = new ArrayList<Map<String, String>>();
		List<Map<String, String>> offlines = new ArrayList<Map<String, String>>();
		Map<String, String> self = null;
		Iterator<Entry<String, Map<String, String>>> iterator = companyUserInitInfo
				.entrySet().iterator();
		Entry<String, Map<String, String>> entry = null;
		Map<String, String> user = null;
		while (iterator.hasNext()) {
			entry = iterator.next();
			user = entry.getValue();
			if (!userids.contains(user.get("ID"))) {
				continue;
			}
			status = username.equals(user.get("USERNAME")) ? "online"
					: (onlineingusers.contains(user.get("USERNAME").replace(
							"@", "\\40")) ? "online" : "offline");
			user.put("status", "online".equals(status) ? "在线" : "离线");
			if ("online".equals(status)) {
				user.put("useritempicStyle", "user-online");
			} else {
				user.put("useritempicStyle", "user-offline");
			}
			if (username.equals(user.get("USERNAME"))) {
				self = user;
				online++;
				continue;
			}
			if ("online".equals(status)) {
				onlines.add(user);
				online++;
			} else {
				offlines.add(user);
			}
		}
		if (self != null) {
			onlines.add(self);
		}
		onlines.addAll(offlines);
		// json.put("groupId", group.get("GROUPID")) ;
		// json.put("groupFlag", group.get("GROUPFLAG")) ;
		// json.put("groupname", group.get("GROUPNAME")) ;
		// json.put("groupremark", group.get("REMARK")) ;
		// json.put("createtime", group.get("CREATETIME")) ;
		json.put("groupId", useridlists.get(0).get("id"));
		json.put("groupFlag", useridlists.get(0).get("flag"));
		json.put("groupname", useridlists.get(0).get("groupName"));
		json.put("groupremark", useridlists.get(0).get("remark"));
		json.put("createtime", useridlists.get(0).get("createTime"));
		json.put("isgroupAdmin", isgroupAdmin);
		json.put("groupImg", "");
		json.put("users", onlines);
		json.put("all", onlines.size());
		json.put("username", username);
		json.put("online", online);
		output(request, response, json);
	}

	// public static void main(String[] args){
	// // ChatWSImpl dd = new ChatWSImpl();
	// // dd.broadcastMyLogin("admin", "company0@broadcast.im.flying.cn",
	// "broadcast-online:admin");
	// try {
	// String ip = "127.0.0.1" ;
	//
	// int port = 5222 ;
	// ConnectionConfiguration config = new ConnectionConfiguration(ip, port);
	// XMPPConnection con = new XMPPConnection(config);
	// con.loginAnonymously();//匿名登錄。
	// Message m = new Message();
	// m.setBody("broadcast-online:admin");
	// m.setTo("company0@broadcast.im.flying.cn");//all@broadcast.im.flying.cn
	// 說明一下只需要改後面的yyp-pc改成 相應的域名。 我這裏是自己機器的名字。
	// con.sendPacket(m);
	// } catch (XMPPException e) {
	// e.printStackTrace();
	// }
	// }
	@Override
	public Map deleteGroup(Map<String, String> params) {
		String companyId = params.get("companyId");
		String groupid = params.get("groupid");
		String groupflag = params.get("groupflag");

		boolean isOk = chatDao.deleteGroup(companyId, groupflag, groupid);
		Map<String, Object> json = new HashMap<String, Object>();

		// 日志---
		if (isOk) {
			String username = params.get("username");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", params.get("ip"));
			log.put("userid", username);
			log.put("loginfo", "删除群组ID【" + groupid + "】");
			log.put("module", "联系人/群组");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			}
			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
		}
		// 日志--end

		json.put("isOk", isOk);
		// output(request, response, json);
		return json;
	}

	@Override
	public void deleteGroup(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String groupid = request.getParameter("groupid");
		String groupflag = request.getParameter("groupflag");

		boolean isOk = chatDao.deleteGroup(companyId, groupflag, groupid);
		JSONObject json = new JSONObject();

		// 日志---
		if (isOk) {
			String username = request.getParameter("username");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "删除群组ID【" + groupid + "】");
			log.put("module", "联系人/群组");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			}

			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
		}
		// 日志--end
		// 20151111 安卓解散群组
		if (request.getParameter("callback") != null
				&& request.getParameter("callback").equals("androidInter")) {
			String arg = "secret=flyingsoft&type=delete_group&groups="
					+ groupflag;
			System.out.println(post(arg));
		}
		json.put("isOk", isOk);
		output(request, response, json);
	}

	@Override
	public void resetGroup(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String username = request.getParameter("username");
		String olduserName = username;
		String changeusers = request.getParameter("changeusers");
		String changeitems = request.getParameter("changeitems");
		String addgroupuserids = request.getParameter("addgroupuserids");
		String deletegroupuserids = request.getParameter("deletegroupuserids");
		String manageruserid = request.getParameter("manageruserid");
		String groupname = request.getParameter("groupname");
		String groupremark = request.getParameter("groupremark");
		String fullname = request.getParameter("fullname");
		String isApplied = request.getParameter("isApplied"); // wangwenshuo add
																// 表示用户自己申请后的邀请用户，这种情况不发送“邀请xx加入群组”通知

		try {
			groupname = URLDecoder.decode(groupname, "utf-8");
			groupremark = URLDecoder.decode(groupremark, "utf-8");
			fullname = URLDecoder.decode(fullname, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String groupid = request.getParameter("groupid");
		String groupflag = request.getParameter("groupflag");
		Date dt = new Date();
		String time = df.format(dt);
		boolean isOk = chatDao.resetGroup(companyId, username, addgroupuserids,
				deletegroupuserids, manageruserid, groupname, groupremark,
				groupflag, time, groupid, changeusers, changeitems);

		// 日志---
		if (isOk) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "修改群组【" + groupname + "】");
			log.put("module", "联系人/群组");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			}
			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}

		/** 保存邀请与踢出消息 **/
		String[] attay = time.split(" ");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		if (!"fyBot".equals(username)) {
			username = companyUserInitInfo.get(username).get("ID");
		}
		if (addgroupuserids.length() > 0) {
			String fullnames = chatDao.getFullNamesByIds(addgroupuserids);
			if (fullnames.length() == 0) {
				chatDao.saveHistoryMessage(companyId, groupflag,
						("true".equals(isApplied) ? "同意 " : "邀请 ")
								+ addgroupuserids.replaceAll(",", "、")
								+ " 加入分类！", attay[0], attay[1], username, "1",
						fullname, "", "", companyUserInitInfo);
			} else {
				chatDao.saveHistoryMessage(companyId, groupflag,
						("true".equals(isApplied) ? "同意 " : "邀请 ") + fullnames
								+ " 加入分类！", attay[0], attay[1], username, "1",
						fullname, "", "", companyUserInitInfo);
			}
		}
		if (deletegroupuserids.length() > 0) {
			String fullnames = chatDao.getFullNamesByIds(deletegroupuserids);
			if (fullnames.length() == 0) {
				chatDao.saveHistoryMessage(companyId, groupflag,
						deletegroupuserids.replaceAll(",", "、") + " 被请出分类！",
						attay[0], attay[1], username, "1", fullname, "", "",
						companyUserInitInfo);
			} else {
				chatDao.saveHistoryMessage(companyId, groupflag, "将 "
						+ fullnames + " 请出分类！", attay[0], attay[1], username,
						"1", fullname, "", "", companyUserInitInfo);
			}
		}
		// 创建Map集合用于保存从前台接收到的值
		Map<String, String> editGroup = new HashMap<String, String>();

		// 将数据写入
		editGroup.put("companyid", companyId);
		editGroup.put("username", olduserName);
		editGroup.put("groupid", groupid);
		// =-=-=-=

		// 执行发送者方法将数据发送到MQ上
		if ("true".equals(changeusers)) {
			getProducerWS().doSender(activeMQPath, editGroup);
		}
		String addgroupusernames = request.getParameter("addgroupusernames");
		String deletegroupusernames = request
				.getParameter("deletegroupusernames");
		// 20151110 创建群组安卓
		if (request.getParameter("callback") != null
				&& request.getParameter("callback").equals("androidInter")) {
			String arg = "secret=flyingsoft&type=reset_group&groups="
					+ groupflag + "&addgroupusernames="
					+ addgroupusernames.replace("@", "\\40")
					+ "&deletegroupusernames="
					+ deletegroupusernames.replace("@", "\\40");
			System.out.println(post(arg));
		}
		JSONObject json = new JSONObject();
		json.put("isOk", isOk);
		output(request, response, json);
	}

	public Map resetGroup(Map<String, String> params) {
		String companyId = params.get("companyId");
		String username = params.get("username");
		String changeusers = params.get("changeusers");
		String changeitems = params.get("changeitems");
		String addgroupuserids = params.get("addgroupuserids");
		String deletegroupuserids = params.get("deletegroupuserids");
		String manageruserid = params.get("manageruserid");
		String groupname = params.get("groupname");
		String groupremark = params.get("groupremark");
		String fullname = params.get("fullname");
		String isApplied = params.get("isApplied"); // wangwenshuo add
		Map<String,String> updateOwner = new HashMap<String, String>();
		//xiayongcai 20151230 当用户被退出分类，该分类下以前上传过的所有文件都归分类创建者  OWNER
		if(deletegroupuserids!=null && deletegroupuserids.length()>0 && params.get("data_idseq")!=null && !"".equals(params.get("data_idseq"))){
			updateOwner.put("groupsOwnerId", manageruserid);
			updateOwner.put("data_idseq", params.get("data_idseq"));
			updateOwner.put("userIds", deletegroupuserids);
			updateOwner.put("companyId", companyId);
			getfilesWS().updateGroupsFileAndFolderOwner(updateOwner);
		}
		try {
			groupname = URLDecoder.decode(groupname, "utf-8");
			groupremark = URLDecoder.decode(groupremark, "utf-8");
			fullname = URLDecoder.decode(fullname, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String groupid = params.get("groupid");
		String groupflag = params.get("groupflag");
		Date dt = new Date();
		String time = df.format(dt);
		boolean isOk = chatDao.resetGroup(companyId, username, addgroupuserids,
				deletegroupuserids, manageruserid, groupname, groupremark,
				groupflag, time, groupid, changeusers, changeitems);

		// 日志---
		if (isOk) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", params.get("ip"));
			log.put("userid", username);
			log.put("loginfo", "修改群组【" + groupname + "】");
			log.put("module", "联系人/群组");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			}
			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}

		/** 保存邀请与踢出消息 **/
		String[] attay = time.split(" ");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		if (!"fyBot".equals(username)) {
			username = companyUserInitInfo.get(username).get("ID");
		}
		if (addgroupuserids.length() > 0) {
			String fullnames = chatDao.getFullNamesByIds(addgroupuserids);
			if (fullnames.length() == 0) {
				chatDao.saveHistoryMessage(companyId, groupflag,
						("true".equals(isApplied) ? "同意 " : "邀请 ")
								+ addgroupuserids.replaceAll(",", "、")
								+ " 加入分类！", attay[0], attay[1], username, "1",
						fullname, "", "", companyUserInitInfo);
			} else {
				chatDao.saveHistoryMessage(companyId, groupflag,
						("true".equals(isApplied) ? "同意 " : "邀请 ") + fullnames
								+ " 加入分类！", attay[0], attay[1], username, "1",
						fullname, "", "", companyUserInitInfo);
			}
		}
		if (deletegroupuserids.length() > 0) {
			String fullnames = chatDao.getFullNamesByIds(deletegroupuserids);
			List<String> usernames = chatDao
					.getUserNameByIds(deletegroupuserids);
			if (fullnames.length() == 0) {
				chatDao.saveHistoryMessage(companyId, groupflag,
						deletegroupuserids.replaceAll(",", "、") + " 被请出分类！",
						attay[0], attay[1], username, "1", fullname, "", "",
						companyUserInitInfo);
			} else {
				chatDao.saveHistoryMessage(companyId, groupflag, "将 "
						+ fullnames + " 请出分类！", attay[0], attay[1], username,
						"1", fullname, "", "", companyUserInitInfo);
			}

			chatDao.dropKickoutUserMessage(companyId, groupflag, usernames);

		}
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("isOk", isOk);
		return json;
		// output(request, response, json);

	}

	@Override
	public Map outGroup(Map<String, String> params) {
		String companyId = params.get("companyId");
		String groupid = params.get("groupid");
		String userid = params.get("userid");
		/** 保存退出消息 **/
		String groupflag = params.get("groupflag");
		String username = params.get("username");
		String fullname = params.get("fullname");
		boolean isOk = chatDao.outGroup(companyId, groupid, userid);
		Map<String,String> updateOwner = new HashMap<String, String>();
		//xiayongcai 20151230 当用户被退出分类，该分类下以前上传过的所有文件都归分类创建者  OWNER
		if(userid!=null && userid.length()>0 && params.get("data_idseq")!=null && !"".equals(params.get("data_idseq"))){
			updateOwner.put("groupsOwnerId", params.get("groupsOwnerId"));
			updateOwner.put("data_idseq", params.get("data_idseq"));
			updateOwner.put("userIds", userid);
			updateOwner.put("companyId", companyId);
			getfilesWS().updateGroupsFileAndFolderOwner(updateOwner);
		}
		// 日志---
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		if (isOk) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", params.get("ip"));
			log.put("userid", username);
			log.put("loginfo", "退出群组ID【" + groupid + "】");
			log.put("module", "联系人/群组");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}

		try {
			fullname = URLDecoder.decode(fullname, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Date dt = new Date();
		String d = df.format(dt);
		String[] attay = d.split(" ");
		chatDao.saveHistoryMessage(companyId, groupflag, fullname + "退出群组！",
				attay[0], attay[1], username, "1", fullname, "", "",
				companyUserInitInfo);
		Map<String, Object> json = new HashMap<String, Object>();
		// 20151111 安卓退出
		if (params.get("callback") != null
				&& params.get("callback").equals("androidInter")) {
			String arg = "secret=flyingsoft&type=out_group&groups=" + groupflag
					+ "&username=" + username.replace("@", "\\40");
			System.out.println(post(arg));
		}
		json.put("isOk", isOk);
		// output(request, response, json);
		return json;
	}

	@Override
	public void outGroup(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String groupid = request.getParameter("groupid");
		String userid = request.getParameter("userid");
		/** 保存退出消息 **/
		String groupflag = request.getParameter("groupflag");
		String username = request.getParameter("username");
		String fullname = request.getParameter("fullname");
		boolean isOk = chatDao.outGroup(companyId, groupid, userid);

		// 日志---
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		if (isOk) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "退出群组ID【" + groupid + "】");
			log.put("module", "联系人/群组");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}

		try {
			fullname = URLDecoder.decode(fullname, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Date dt = new Date();
		String d = df.format(dt);
		String[] attay = d.split(" ");
		chatDao.saveHistoryMessage(companyId, groupflag, fullname + "退出群组！",
				attay[0], attay[1], username, "1", fullname, "", "",
				companyUserInitInfo);
		JSONObject json = new JSONObject();
		json.put("isOk", isOk);
		output(request, response, json);
	}

	@Override
	public void saveGroupcallOver(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String groupflag = request.getParameter("groupflag");
		String users = request.getParameter("users");
		boolean isOk = chatDao.saveGroupcallOver(companyId, groupflag, users);

		// 日志---
		if (isOk) {
			String username = request.getParameter("username");
			String userid = request.getParameter("userid");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "保存被点名的用户信息【" + users + "】");
			log.put("module", "联系人/群组");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			}
			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}

		JSONObject json = new JSONObject();
		json.put("isOk", isOk);
		output(request, response, json);
	}

	private void output(HttpServletRequest request,
			HttpServletResponse response, JSONObject json) {
		response.setContentType("text/javascript;charset=UTF-8");
		StringBuffer output = new StringBuffer(100);
		if (request.getParameter("callback") != null
				&& "androidInter".equals(request.getParameter("callback"))) {
			output.append(json.toString());
		} else {
			output.append(request.getParameter("callback")).append("(");
			output.append(json.toJSONString()).append(");");
		}
		try {
			response.getWriter().println(output);
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void getCompanySessions(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String stratNo = request.getParameter("startNo");
		String limit = request.getParameter("limit");

		// 日志---
		String username = request.getParameter("username");
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", request.getRemoteAddr());
		log.put("userid", username);
		log.put("loginfo", "获取在线用户,公司ID【" + companyId + "】");
		log.put("module", "在线用户查看");
		log.put("username", username);
		log.put("type", "operation");
		log.put("operate", "在线用户查看");
		Map<String, Map<String, String>> companyUserInitInfo1 = null;
		Object obj1 = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj1 != null) {
			companyUserInitInfo1 = (Map<String, Map<String, String>>) obj1;
		}
		String companyName = companyUserInitInfo1.get(username).get(
				"COMPANYNAME");
		log.put("companyName", companyName != null ? companyName : companyId);
		LogUtils.saveBaseLog(compLocator, log);
		// 日志--end

		HashMap<String, String> fullnames = new HashMap<String, String>();
		String arg = "secret=flyingsoft&type=getCompanySessions&resource=onlinefile"
				+ companyId + "&start=" + stratNo + "&limit=" + limit;
		String data = post(arg);
		JSONObject json = new JSONObject();
		if (data != null) {
			json = (JSONObject) JSON.parse(data);
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				Map<String, Map<String, String>> companyUserInitInfo = (Map<String, Map<String, String>>) obj;
				List<HashMap<String, String>> sessions = (List<HashMap<String, String>>) json
						.get("rows");
				if (sessions != null) {
					for (HashMap<String, String> session : sessions) {
						fullnames.put(
								session.get("userName"),
								companyUserInitInfo.get(
										session.get("userName").replace("\\40",
												"@")).get("FULLNAME"));
					}
				}
			}
		}
		json.put("fullnames", fullnames);
		output(request, response, json);
	}

	@SuppressWarnings("unchecked")
	public void closeSession(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String username = request.getParameter("username");

		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		JSONObject json = new JSONObject();
		if (obj == null) {
			json.put("isOk", false);
		} else {
			Map<String, Map<String, String>> companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			String realusername = username.replace("\\40", "@");
			BroadcastUtils.broadcast(username, username + "@"
					+ BroadcastUtils.openfireServerName, "broadcast-offline:"
					+ realusername, Message.Type.CHAT,
					companyUserInitInfo.get(realusername).get("FULLNAME"));
			json.put("isOk", true);

			// 日志---
			String userid = request.getParameter("userid");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", userid);
			log.put("loginfo", "强制退出【" + username + "】");
			log.put("module", "在线用户查看");
			log.put("username", userid);
			log.put("type", "operation");
			log.put("operate", "在线用户查看");
			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}
		output(request, response, json);
	}

	private String post(String arg) {
		String mag = null;
		try {
			URL oUrl = new URL("http://" + BroadcastUtils.openfireIp + ":"
					+ BroadcastUtils.openfireServerPort
					+ "/plugins/userService/userservice");
			// 打开和URL之间的连接
			URLConnection conn = oUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			// 获取URLConnection对象对应的输出流
			PrintWriter out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数 name1=value1&name2=value2
			out.print(arg);
			// flush输出流的缓冲
			out.flush();
			BufferedReader oIn = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			if (null != oIn) {
				mag = oIn.readLine();
				oIn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mag;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String ip = "119.254.102.238";
		String mag = null;
		try {
			URL oUrl = new URL("http://ip.taobao.com/service/getIpInfo.php");
			// 打开和URL之间的连接
			URLConnection conn = oUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			// 获取URLConnection对象对应的输出流
			PrintWriter out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数 name1=value1&name2=value2
			out.print("ip=" + ip);
			// flush输出流的缓冲
			out.flush();
			BufferedReader oIn = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			if (null != oIn) {
				mag = oIn.readLine();
				oIn.close();
			}
			System.out.println(mag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.print(System.currentTimeMillis() - start);
	}

	/**
	 * 获取某个文件最后的几条历史数据(群组)
	 */
	public void getHistoryMessage4File(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String receiver = request.getParameter("receiver");
		String fileFlag = request.getParameter("fileFlag");
		String limit = request.getParameter("limit");
		String page = request.getParameter("page");
		String skip = request.getParameter("skip");

		// 日志---
		String username = request.getParameter("username");
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", request.getRemoteAddr());
		log.put("userid", username);
		log.put("loginfo", "获取文件【" + fileFlag + "】最后的几条历史数据(群组)");
		log.put("module", "联系人/群组");
		log.put("username", username);
		log.put("type", "operation");
		log.put("operate", "联系人/群组");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		String companyName = companyUserInitInfo.get(username).get(
				"COMPANYNAME");
		log.put("companyName", companyName != null ? companyName : companyId);
		LogUtils.saveBaseLog(compLocator, log);
		// 日志--end

		List<HashMap<String, String>> msgs = chatDao.getHistoryMessage4File(
				companyId, receiver, fileFlag, limit, page, skip);

		// Map<String,String> searchMap = new HashMap<String, String>();
		// searchMap.put("keyWord","");
		// searchMap.put("companyId",companyId);
		// int start = 0;
		// if(Integer.parseInt(page)==1){
		// if(Integer.parseInt(skip)!=0){
		// start = Integer.parseInt(skip);
		// }
		// }else{
		// start = (Integer.parseInt(page)-1)*Integer.parseInt(limit);
		// }
		// searchMap.put("start",start+"");
		// searchMap.put("limit",limit);
		// searchMap.put("idSeq","");
		// searchMap.put("createrId","");
		//
		// searchMap.put("searchType","2");//1 是文件搜索，2是消息
		// searchMap.put("receiver",receiver);
		// searchMap.put("username",username);
		// searchMap.put("fileFlag",fileFlag);
		// searchMap.put("isGroup","1");//非1代表 chad0/*
		// List<Map<String, String>> msgs = getLuceneWS().search(searchMap);
		Collections.reverse(msgs);

		JSONObject json = new JSONObject();
		json.put("msgs", msgs);
		output(request, response, json);
	}

	public String initMongodbMsg(Map<String, String> map) {
		boolean isOK = chatDao.initMessageTable(map);
		return Boolean.toString(isOK);
	}

	/**
	 * 邮件增加
	 */
	public Map saveUserToGroupFromEamil(Map<String, String> parems) {
		String companyId = parems.get("companyid");
		String username = parems.get("username");
		String changeusers = "true";
		String changeitems = "false";
		String addgroupuserids = parems.get("id");
		String deletegroupuserids = "";
		String groupname = parems.get("groupname");
		String fullname = parems.get("fullname");
		String classId = parems.get("classId");
		//20151118 xiayongcai 通过获取发送者的ID获取发送者的姓名
		//目前针对 用户成员分类下邀请进行的修改：包括（链接邀请、右键发送邀请）未发现其他。。
		String sendUserName = parems.get("sendUserName");
		//通过缓存获取ID
		String sendUserId="";
		String sendFullName="";

		try {
			groupname = URLDecoder.decode(groupname, "utf-8");
			fullname = URLDecoder.decode(fullname, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Map<String, String> groupInfo = chatDao.getGroupFlagByClassId(classId,companyId);
		String groupid = groupInfo.get("groupid");
		String groupflag = groupInfo.get("groupflag");
		Date dt = new Date();
		String time = df.format(dt);
		boolean isOk = chatDao.resetGroup(companyId, username, addgroupuserids,
				deletegroupuserids, "", groupname, "", "", time, groupid,
				changeusers, changeitems);
	
		/** 保存邀请与踢出消息 **/
		String[] attay = time.split(" ");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		
		if (!"fyBot".equals(username)) {
			sendFullName=companyUserInitInfo.get(sendUserName).get("FULLNAME");
			sendUserId=companyUserInitInfo.get(sendUserName).get("ID");
			username = companyUserInitInfo.get(username).get("ID");
		}
		if (addgroupuserids.length() > 0) {
			String fullnames = chatDao.getFullNamesByIds(addgroupuserids);
			if (fullnames.length() == 0) {
				chatDao.saveHistoryMessage(companyId, groupflag, "邀请 "
						+ addgroupuserids.replaceAll(",", "、") + " 加入分类！",
						attay[0], attay[1], sendUserId, "1", sendFullName, "", "",
						companyUserInitInfo);
			} else {
				chatDao.saveHistoryMessage(companyId, groupflag, "邀请 "
						+ fullnames + " 加入分类！", attay[0], attay[1], sendUserId,
						"1", sendFullName, "", "", companyUserInitInfo);
			}
		}
		/*if (deletegroupuserids.length() > 0) {
			String fullnames = chatDao.getFullNamesByIds(deletegroupuserids);
			if (fullnames.length() == 0) {
				chatDao.saveHistoryMessage(companyId, groupflag,
						deletegroupuserids.replaceAll(",", "、") + " 被请出分类！",
						attay[0], attay[1], username, "1", fullname, "", "",
						companyUserInitInfo);
			} else {
				chatDao.saveHistoryMessage(companyId, groupflag, "将 "
						+ fullnames + " 请出分类！", attay[0], attay[1], username,
						"1", fullname, "", "", companyUserInitInfo);
			}
		}*/
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("isOk", isOk);
		rtnMap.put("groupflag", groupflag);
		return rtnMap;
	}

	public List<String> getGroupUsersByGroupId(String groupid) {

		return chatDao.getGroupUsersByGroupId(groupid);
	}

	public void getGroupUserByGroupId(HttpServletRequest request,
			HttpServletResponse response) {
		String groupid = request.getParameter("groupid");
		String companyId = request.getParameter("companyId");
		String username = request.getParameter("username");
		String groupFlag = request.getParameter("groupFlag");
		String applyuserid = request.getParameter("applyuserid");
		List<String> list = chatDao.getOneGroupUserIds(companyId, username,
				groupFlag);
		boolean isOk = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(applyuserid)) {
				isOk = true;
			}
		}
		JSONObject json = new JSONObject();
		json.put("users", list);
		json.put("isOk", isOk);
		json.put("all", list.size());
		output(request, response, json);
	}

	public Map getGroupUserIsAdminInfo(Map<String, String> params) {
		String groupid = params.get("groupid");
		String groupflag = params.get("groupflag");
		String companyId = params.get("companyId");
		String username = params.get("username");
		Map<String, Object> rtnMap = chatDao.getGroupUserIsAdminInfo(groupid,
				groupflag, companyId);
		Map<String, String> map = getUserWS().getUserByUserId(
				rtnMap.get("userId").toString());
		String timeOutKey = "applyTimeOut" + companyId;
		String applyKey = username + "_" + groupflag;
		Map<String, Object> json = new HashMap<String, Object>();
		// 放入缓存----------------------------------
		Map<String, String> applyTimeOut = null;
		Object obj = CacheUtils.get(this.getCompLocator(), timeOutKey);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (obj != null) {
			applyTimeOut = (Map<String, String>) obj;
			if (applyTimeOut.get(applyKey) != null) {
				String createtime = applyTimeOut.get(applyKey);
				if (createtime != null) {
					String nowtime = format.format(System.currentTimeMillis());
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					try {
						cal1.setTime(format.parse(nowtime));
						cal2.setTime(format.parse(createtime));
						long l = cal1.getTimeInMillis()
								- cal2.getTimeInMillis();
						int days = new Long(l / (1000 * 60)).intValue();
						if (days >= 3) {// 3分钟只能发送一次
							json.put("isOk", true);
							applyTimeOut.put(applyKey, nowtime);
							CacheUtils.set(this.getCompLocator(), timeOutKey,
									applyTimeOut);
						} else {
							json.put("afterMin", (3 - days));
							json.put("isOk", false);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					json.put("isOk", true);
					String now = format.format(System.currentTimeMillis());
					applyTimeOut.put(applyKey, now);
					CacheUtils.set(this.getCompLocator(), timeOutKey,
							applyTimeOut);
				}
			} else {
				json.put("isOk", true);
				String now = format.format(System.currentTimeMillis());
				applyTimeOut.put(applyKey, now);
			}
			CacheUtils.set(this.getCompLocator(), timeOutKey, applyTimeOut);
		} else {
			json.put("isOk", true);

			applyTimeOut = new HashMap<String, String>();
			String now = format.format(System.currentTimeMillis());
			applyTimeOut.put(applyKey, now);
			CacheUtils.set(this.getCompLocator(), timeOutKey, applyTimeOut);
		}
		json.put("username", map.get("username"));
		return json;
	}

	public Map changeGroupAdmin(Map<String, String> map) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String companyId = map.get("companyId");
		String username = map.get("username");
		String userid = map.get("userid");
		String tousername = map.get("tousername");
		String touserid = map.get("touserid");
		String groupid = map.get("groupid");
		String classId = map.get("classId");
		boolean isOk = false;
		// 替换角色
		if (chatDao.changeGroupAdmin(companyId, username, userid, tousername,
				touserid, groupid)) {
			// 调用document的东西要改掉创建者
			Map<String, String> params = new HashMap<String, String>();
			params.put("companyId", companyId);
			params.put("classId", classId);
			params.put("touserid", touserid);
			Map<String, Object> rtnMap = getDocumentClassWS().changeGroupAdmin(
					params);
			if (Boolean.parseBoolean(rtnMap.get("isOk").toString())) {
				isOk = true;
			}
		}
		returnMap.put("isOk", isOk);
		return returnMap;
	}

	public Map<String, Object> UpdateFileToShareOrNoShare(String msgid,
			String button, String companyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (chatDao.UpdateFileToShareOrNoShare(msgid, button, companyId)) {
			params.put("success", true);
		} else {
			params.put("success", false);
		}
		return params;

	}

	/**
	 * 发表文件评论
	 */
	public boolean newFileComment(Map<String, String> params) {
		return chatDao.newFileComment(params);
	}

	/**
	 * 删除文件评论
	 */
	public boolean deleteComment(Map<String, String> params) {
		return chatDao.deleteComment(params);
	}

	/**
	 * 获取文件的评论
	 */
	public List<Map<String, String>> getFileCommentList(
			Map<String, String> params) {
		String companyId = params.get("companyId");
		String fileFlag = params.get("fileFlag");
		String version = params.get("version");
		String limit = params.get("limit");
		String page = params.get("page");
		String skip = params.get("skip");
		return chatDao.getFileCommentList(companyId, fileFlag, version, limit,
				page, skip);
	}

	public List<Map<String, String>> getFileCommentListByVersions(
			Map<String, String> params) {
		String companyId = params.get("companyId");
		String fileFlag = params.get("fileFlag");
		String version = params.get("version");
		String limit = params.get("limit");
		String page = params.get("page");
		String skip = params.get("skip");
		return chatDao.getFileCommentListByVersions(companyId, fileFlag,
				version, limit, page, skip);
	}

	/**
	 * 将公司管理员加入到被禁用用户创建的群组中
	 */
	public List<Map<String, String>> addAdminIntoGroups(
			Map<String, String> params) {
		String userId = params.get("userId");
		String userIdStr = params.get("userIds");// 多个用逗号隔开的userIds
		String companyId = params.get("companyId");
		return chatDao.addAdminIntoGroups(userId, userIdStr, companyId);
	}

	@Override
	public void resetGroupByApply(HttpServletRequest request,
			HttpServletResponse response) {
		String companyId = request.getParameter("companyId");
		String username = request.getParameter("username");
		String changeusers = request.getParameter("changeusers");
		String changeitems = request.getParameter("changeitems");
		String addgroupuserids = request.getParameter("addgroupuserids");
		String deletegroupuserids = request.getParameter("deletegroupuserids");
		String manageruserid = request.getParameter("manageruserid");
		String groupname = request.getParameter("groupname");
		String groupremark = request.getParameter("groupremark");
		String fullname = request.getParameter("fullname");
		String isApplied = request.getParameter("isApplied"); // wangwenshuo add
																// 表示用户自己申请后的邀请用户，这种情况不发送“邀请xx加入群组”通知
		String msgid = request.getParameter("msgid");
		String button = request.getParameter("button");
		try {
			groupname = URLDecoder.decode(groupname, "utf-8");
			groupremark = URLDecoder.decode(groupremark, "utf-8");
			fullname = URLDecoder.decode(fullname, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String groupid = request.getParameter("groupid");
		String groupflag = request.getParameter("groupflag");
		Date dt = new Date();
		String time = df.format(dt);
		boolean isOk = chatDao.resetGroup(companyId, username, addgroupuserids,
				deletegroupuserids, manageruserid, groupname, groupremark,
				groupflag, time, groupid, changeusers, changeitems);

		// 修改聊天记录
		boolean flag = chatDao.UpdateFileToShareOrNoShare(msgid, button,
				companyId);
		// 日志---
		if (isOk) {
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "修改群组【" + groupname + "】");
			log.put("module", "联系人/群组");
			log.put("username", username);
			log.put("type", "operation");
			log.put("operate", "联系人/群组");
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(),
					"companyUserInitInfo" + companyId);
			if (obj != null) {
				companyUserInitInfo = (Map<String, Map<String, String>>) obj;
			}
			String companyName = companyUserInitInfo.get(username).get(
					"COMPANYNAME");
			log.put("companyName", companyName != null ? companyName
					: companyId);
			LogUtils.saveBaseLog(compLocator, log);
			// 日志--end
		}

		/** 保存邀请与踢出消息 **/
		String[] attay = time.split(" ");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		if (!"fyBot".equals(username)) {
			username = companyUserInitInfo.get(username).get("ID");
		}
		if (addgroupuserids.length() > 0) {
			String fullnames = chatDao.getFullNamesByIds(addgroupuserids);
			if (fullnames.length() == 0) {
				chatDao.saveHistoryMessage(companyId, groupflag,
						("true".equals(isApplied) ? "同意 " : "邀请 ")
								+ addgroupuserids.replaceAll(",", "、")
								+ " 加入分类！", attay[0], attay[1], username, "1",
						fullname, "", "", companyUserInitInfo);
			} else {
				chatDao.saveHistoryMessage(companyId, groupflag,
						("true".equals(isApplied) ? "同意 " : "邀请 ") + fullnames
								+ " 加入分类！", attay[0], attay[1], username, "1",
						fullname, "", "", companyUserInitInfo);
			}
		}
		if (deletegroupuserids.length() > 0) {
			String fullnames = chatDao.getFullNamesByIds(deletegroupuserids);
			if (fullnames.length() == 0) {
				chatDao.saveHistoryMessage(companyId, groupflag,
						deletegroupuserids.replaceAll(",", "、") + " 被请出分类！",
						attay[0], attay[1], username, "1", fullname, "", "",
						companyUserInitInfo);
			} else {
				chatDao.saveHistoryMessage(companyId, groupflag, "将 "
						+ fullnames + " 请出分类！", attay[0], attay[1], username,
						"1", fullname, "", "", companyUserInitInfo);
			}
		}
		JSONObject json = new JSONObject();
		json.put("isOk", isOk);
		output(request, response, json);
	}

	public void modifyChatLog(HttpServletRequest request,
			HttpServletResponse response) {
		String msgid = request.getParameter("msgid");
		String button = request.getParameter("button");
		String companyId = request.getParameter("companyId");
		boolean isOk = chatDao.UpdateFileToShareOrNoShare(msgid, button,
				companyId);
		JSONObject json = new JSONObject();
		json.put("isOk", isOk);
		output(request, response, json);
	}

	/**
	 * 添加用户到群组中
	 */
	public boolean addUserIntoGroup(Map<String, String> params) {
		String userId = params.get("userId");
		String groupId = params.get("groupId");
		String companyId = params.get("companyId");
		String groupflag = params.get("groupflag");
		String username = params.get("username");
		String fullname = params.get("fullname");
		try {
			fullname = URLDecoder.decode(fullname, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		boolean flag =chatDao.addUserIntoGroup(userId, groupId);
		if(flag){
		Date dt = new Date();
		String time = df.format(dt);
		/** 保存邀请消息 **/
		String[] attay = time.split(" ");
		Map<String, Map<String, String>> companyUserInitInfo = null;
		Object obj = CacheUtils.get(this.getCompLocator(),
				"companyUserInitInfo" + companyId);
		if (obj != null) {
			companyUserInitInfo = (Map<String, Map<String, String>>) obj;
		}
		if (!"fyBot".equals(username)) {
			username = companyUserInitInfo.get(username).get("ID");
		}
		String fullnames = chatDao.getFullNamesByIds(userId);
		if (fullnames.length() == 0) {
			chatDao.saveHistoryMessage(companyId, groupflag, ("邀请  ") + userId
					+ " 加入分类！", attay[0], attay[1], username, "1", fullname,
					"", "", companyUserInitInfo);
		} else {
			chatDao.saveHistoryMessage(companyId, groupflag, ("邀请  ")
					+ fullnames + " 加入分类！", attay[0], attay[1], username, "1",
					fullname, "", "", companyUserInitInfo);
		}
		}
		return flag;
	}

	public boolean inviteUserIntoGroup(String userId, String companyId,
			String groupId) {
		return chatDao.addUserIntoGroup(userId, groupId);
	}

	@Override
	public void doImageMath(String companyId, String groupId,
			List<HashMap<String, String>> groups) {
		// TODO Auto-generated method stub
	}

  @Override
  public Map<String, Object> getHistoryMessage(Map<String, String> params) {
    String companyId = params.get("companyId");
    String receiver = params.get("receiver");
    String username = params.get("username");
    String isGroup = params.get("isGroup");
    String limit = params.get("limit");
    String page = params.get("page");
    String skip = params.get("skip");
    String keyword = params.get("keyword");
    String joindate = params.get("joindate");
    String jointime = params.get("jointime");

    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip",params.get("remoteAddr"));
    log.put("userid", username);
    log.put("loginfo", "获取历史消息to【" + receiver + "】");
    log.put("module", "消息");
    log.put("username", username);
    log.put("type", "operation");
    log.put("operate", "联系人/群组");
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(),
            "companyUserInitInfo" + companyId);
    if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
    }
    String companyName = companyUserInitInfo.get(username).get(
            "COMPANYNAME");
    log.put("companyName", companyName != null ? companyName : companyId);
    LogUtils.saveBaseLog(compLocator, log);
    // 日志--end

    // List<HashMap<String, String>> msgs =
    // chatDao.getHistoryMessage(companyId, receiver, username, isGroup,
    // limit, page, skip, keyword);
    Map<String, String> searchMap = new HashMap<String, String>();
    searchMap.put("keyWord", keyword);
    searchMap.put("companyId", companyId);
    int start = 0;
    if (Integer.parseInt(page) == 1) {
        if (!StringUtils.isBlank(skip) && Integer.parseInt(skip) != 0) {
            start = Integer.parseInt(skip);
        }
    } else {
        start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
    }
    String userId = companyUserInitInfo.get(username).get("ID");
    String receiverId = "0";
    if ("1".equals(isGroup)) {
        userId = username;
        receiverId = receiver;
    } else {
        if (!"fyBot".equals(receiver)) {
            receiverId = companyUserInitInfo.get(receiver).get("ID");
        }
    }
    searchMap.put("start", start + "");
    searchMap.put("limit", limit);
    searchMap.put("idSeq", "");
    searchMap.put("createrId", "");

    searchMap.put("searchType", "2");// 1 是文件搜索，2是消息
    searchMap.put("receiver", receiverId);
    searchMap.put("username", userId);
    searchMap.put("isGroup", isGroup);// 非1代表 chad0
    searchMap.put("joindate", joindate);
    searchMap.put("jointime", jointime);
    List<Map<String, String>> msgs = getLuceneWS().search(searchMap);
    /** lujixiang 20151217 倒序显示记录 **/
    Collections.reverse(msgs);
    Map<String,Object> result = new HashMap<String,Object>();
    result.put("msgs", msgs);
    return result;
  }
  
  
  public Map<String, Object> getImgMap(Map<String, String> map){
	  Map<String, Object> retMap= new HashMap<String, Object>();
	  String companyId= map.get("companyId");
	  String username=map.get("username");
	  String cache_username = (String) CacheUtils.get(this.getCompLocator(),"username_"+username);
	  List<Map<String, String>> retugroups = new ArrayList<Map<String,String>>();
	  List<HashMap<String, String>> groups = chatDao.getGroupsAndClassByUsername(companyId,username);
	  /** 定义变量接收缓存中的分组头像信息 */
		HashMap<String, String> oldGroupImages = (HashMap<String, String>) CacheUtils.get(this.getCompLocator(), "company_id_username_" + companyId + "_" + username);
		// 如果缓存中不是空值的话
				if (oldGroupImages != null) {
					List<String> strs = new ArrayList<String>();
					// 拿到缓存中的数据数量
					for (String string : oldGroupImages.keySet()) {
						strs.add(string);
					}
					if (cache_username==null) {
						doImageMath(companyId, username , null, groups);
						/** 如果数据不一致 重新加载一遍 */
						oldGroupImages = (HashMap<String, String>) CacheUtils.get(this.getCompLocator(), "company_id_username_" + companyId + "_" + username);
						CacheUtils.set(this.getCompLocator(), "username_"+username, username);
					}
				}
				try {
					if (null != groups && groups.size() > 0) {
						if (oldGroupImages == null || oldGroupImages.size()!=groups.size()) {
							doImageMath(companyId, username , null, groups);
							CacheUtils.set(this.getCompLocator(), "username_"+username, username);
						} else {
							for (HashMap<String, String> hashMap : groups) {
								String cachePath = oldGroupImages.get(hashMap.get("ID"));
								// 截取字符串
								cachePath = cachePath.substring(cachePath.indexOf("files"));
								// 替换原地址
								hashMap.put("PORTRAITS", cachePath);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Map<String, String> retugroupsMap=new HashMap<String, String>();
				if(groups !=null && groups.size()>0){
					for(HashMap<String, String> hashMap : groups){
						retugroupsMap=new HashMap<String, String>();
						retugroupsMap.put("FLAG", hashMap.get("FLAG"));
						retugroupsMap.put("PORTRAIT", hashMap.get("PORTRAITS"));
						retugroups.add(retugroupsMap);
					}
				}
				//获取联系人图片
				retMap.put("groups", retugroups);
				retMap.put("users", chatDao.getUserImgByCompanyId(companyId));
				return retMap;
  }
  
  
  
  
}
