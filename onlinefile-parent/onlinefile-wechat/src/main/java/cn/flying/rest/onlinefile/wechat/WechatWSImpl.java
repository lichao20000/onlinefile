package cn.flying.rest.onlinefile.wechat;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.flying.rest.onlinefile.restInterface.WechatWS;
import cn.flying.rest.onlinefile.utils.BaseWS;
import cn.flying.rest.onlinefile.utils.StringUtil;
import cn.flying.rest.onlinefile.wechat.dao.weChatDao;

import com.alibaba.fastjson.JSONObject;

@Path("/wechat")
@Service
public class WechatWSImpl extends BaseWS implements WechatWS{
	
	/**    20151013 liuhezeng添加 log4j日志管理    **/
	private static final Logger logger = Logger.getLogger(WechatWSImpl.class);
	
	
	@Autowired
	private weChatDao chatDao ;
	private void output(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		response.setContentType("text/javascript;charset=UTF-8");
		StringBuffer output = new StringBuffer(100);
		if(null != request.getParameter("callback") && "androidInter".equals(request.getParameter("callback"))){
			output.append(json.toJSONString());
		}else{
			output.append(request.getParameter("callback")).append("(") ;
			output.append(json.toJSONString()).append(");") ;
		}
		try {
			response.getWriter().println(output);
			response.getWriter().close();
		} catch (IOException e) {
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void getCommunitylist(HttpServletRequest request, HttpServletResponse response){
	//	String pageid = request.getParameter("pageid");
		int pageid=Integer.parseInt(request.getParameter("pageid")==null || "".equals(request.getParameter("pageid"))?"0":request.getParameter("pageid"));
		int limitPage=Integer.parseInt(request.getParameter("limitPage")==null || "".equals(request.getParameter("limitPage"))?"8":request.getParameter("limitPage"));
		if(pageid < 0){
			pageid=0;
		}
	//	int page= Integer.parseInt(pageid);
		List<HashMap<String, String>> mesglist = chatDao. getCommnunityMessage(pageid,limitPage);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!mesglist.isEmpty() && mesglist.size()>0){
			for(HashMap<String, String> list : mesglist){
					if( null != list.get("maxpldate") && "" != list.get("maxpldate")){
						String newdate = list.get("maxpldate");
						String systemTime = df.format(new Date());
						String subDate = newdate.substring(0,newdate.indexOf(" "));
						systemTime = systemTime.substring(0,systemTime.indexOf(" "));
						String time = newdate.substring(newdate.indexOf(" "),newdate.lastIndexOf(":"));
						if(subDate.equals(systemTime)){
							String maxpldate = "今天" + time;
						    list.put("maxpldate", maxpldate);
						}
					}
					if(null != list.get("date") && "" != list.get("date")){
						String systemDate = df.format(new Date()).substring(0,11);
						String date = list.get("date").substring(0,11);
						String time = list.get("date").substring(11,16);
						String pubdate = "今天   "+time;
						if(systemDate.equals(date)){
							list.put("date", pubdate);
						}
					}
					
			}
		}
		List<String> userids = new ArrayList<String>();
		if(!mesglist.isEmpty() && mesglist.size()>0){
			for (HashMap<String, String> hashMap : mesglist) {
				if(!hashMap.isEmpty()&& hashMap.size() > 0){
					if(null != hashMap.get("lastReplyName") && "" != hashMap.get("lastReplyName")){
						userids.add(hashMap.get("lastReplyName"));
					}
				}
			}
		}
		List<Map<String, String>>infos = chatDao.getLastReplyName(userids);
		if(!mesglist.isEmpty() && mesglist.size()>0){
			for (HashMap<String, String> hashMap : mesglist) {
				if(null != hashMap.get("lastReplyName") && "" != hashMap.get("lastReplyName")){
					if(null != infos && infos.size() > 0){
						for(Map<String,String> map : infos){
							if(!map.isEmpty() && map.size() > 0){
								if(hashMap.get("lastReplyName").equals(map.get("username"))){
									hashMap.put("fullName", map.get("fullname"));
								}
							}
						}
					}
				}
			}
		}
		JSONObject json  = new JSONObject();
		if(mesglist!=null){
			json.put("alllist", mesglist) ;
			output(request, response, json);
		}else{
			output(request, response, json);
		}
	}
	/*@SuppressWarnings("unchecked")
	public void publishCommunitylist(HttpServletRequest request, HttpServletResponse response){
		String username="";
		String title="";
		String userinfo="";
		String realtitle="";
		String pubtype="";
		try {
			username = new String(request.getParameter("username").getBytes("iso8859-1"),"utf-8");
			title =  new String(request.getParameter("usertitle").getBytes("iso8859-1"),"utf-8");
			userinfo =  new String(request.getParameter("userinfo").getBytes("iso8859-1"),"utf-8");
			realtitle = new String(request.getParameter("realtitle").getBytes("iso8859-1"),"utf-8");
			pubtype = new String(request.getParameter("thtype").getBytes("iso8859-1"),"utf-8");
			
			
			username = URLDecoder.decode(request.getParameter("username"),"utf-8");
			title = URLDecoder.decode(request.getParameter("usertitle"),"utf-8");
			userinfo = URLDecoder.decode(request.getParameter("userinfo"),"utf-8");
			realtitle = URLDecoder.decode(request.getParameter("realtitle"),"utf-8");
			pubtype = URLDecoder.decode(request.getParameter("thtype"),"utf-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(date);
		if(chatDao.publisCommnunity(username, time, title,userinfo,realtitle,pubtype)){
			JSONObject json  = new JSONObject();
			json.put("alllist","OK");
			output(request, response, json);
		}
	}*/
	
	public Map<String, String> publishCommunitylist(Map<String, String> params) {
		Map<String,String> returnInfo = new HashMap<String, String>();
		String username="";
		String title="";
		String userinfo="";
		String realtitle="";
		String pubtype="";
		String cardId = params.get("cardId");
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(date);
		if(!params.isEmpty() && params.size() > 0){
			username = params.get("username");
			title = params.get("usertitle");
			userinfo = params.get("userinfo");
			realtitle = params.get("realtitle");
			pubtype = params.get("thtype");
		}
		if(userinfo!=""){
			userinfo=StringUtil.getGifImg("apps/onlinefile/templates/ESUserCommunity/ckeditor/plugins/smiley/appGifImg/", userinfo);
		}
		if(!cardId.equals("")){
			if(chatDao.updateCard(cardId,title,userinfo)){
				if(chatDao.updateCommunity(cardId,title,time,pubtype)){
					returnInfo.put("alllist","OK");
				}
			}
		}else{
			if(chatDao.publisCommnunity(username, time, title,userinfo,realtitle,pubtype)){
				returnInfo.put("alllist","OK");
			}
		}
		return returnInfo;
	}
	
	@SuppressWarnings("unchecked")
	public void getCommunityArticle(HttpServletRequest request, HttpServletResponse response){
		JSONObject json  = new JSONObject();
		String user = request.getParameter("userid");
		String flag  = request.getParameter("flag");
		String plId  = request.getParameter("plId");
		String userName  = request.getParameter("userName");
		int count = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String , String> map = chatDao.getCommunityArticle(user);
		
		if("1".equals(flag)){
			chatDao.updateCommunityMessageState(user,null,userName);
			count = chatDao.getCountReplyInfo(userName);
			json.put("count",count);
		}
		
		if(!map.isEmpty() && map.size() > 0){
			if(null != map.get("publish_date") && "" != map.get("publish_date")){
				String systemDate = df.format(new Date()).substring(0,11);
				String date = map.get("publish_date").substring(0,11);
				String time = map.get("publish_date").substring(11,16);
				String pubdate = "今天   "+time;
				if(systemDate.equals(date)){
					map.put("publish_date", pubdate);
				}
			}
			
			if( null !=map.get("pubtype") && "" !=map.get("pubtype")){
				String type = map.get("pubtype");
				switch (type) {
				case "newuser":
					map.put("type", "新手上路");
					break;
				case "userreply":
					map.put("type", "用户反馈");
					break;
				case "usertec":
					map.put("type", "使用技巧");
					break;
				case "guanfang":
					map.put("type", "产品公告");
					break;
				}
			}
		}
		
		//获取用户是否已经点赞
		if(chatDao.getUserIsPraise(user,plId)){
        	map.put("ispraise", "true");
        }else{
        	map.put("ispraise", "false");
        }
		if(null != request.getParameter("callback") && "androidInter".equals(request.getParameter("callback"))){
			int page=Integer.parseInt(request.getParameter("page")==null || "".equals(request.getParameter("page"))?"0":request.getParameter("page"));
			int limitPage=Integer.parseInt(request.getParameter("limitPage")==null || "".equals(request.getParameter("limitPage"))?"0":request.getParameter("limitPage"));
			if(page < 0){
				page=0;
			}
			List<HashMap<String, String>> replylist=getReplylistInherit(user,page,limitPage);
				json.put("replylist", replylist!=null ? replylist:"") ;
		}
		json.put("alllist",map);
		output(request, response, json);
	}
	@SuppressWarnings("unchecked")
	public void getCommunityTypelist(HttpServletRequest request,
			HttpServletResponse response) {
		//String pageid = request.getParameter("pageid");
		//int page= Integer.parseInt(pageid);
		int pageid=Integer.parseInt(request.getParameter("pageid")==null || "".equals(request.getParameter("pageid"))?"0":request.getParameter("pageid"));
		int limitPage=Integer.parseInt(request.getParameter("limitPage")==null || "".equals(request.getParameter("limitPage"))?"8":request.getParameter("limitPage"));
		if(pageid < 0){
			pageid=0;
		}
		String pubtype="";
		try {
			pubtype = new String(request.getParameter("ctype").getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			/**   20151013 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());
		}
		List<HashMap<String, String>> mesglist = chatDao.getCommnunityTypeMessage(pubtype,pageid,null,limitPage);
		List<String> userids = new ArrayList<String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!mesglist.isEmpty() && mesglist.size()>0){
			for(HashMap<String, String> list : mesglist){
					if( null != list.get("maxpldate") && "" != list.get("maxpldate")){
						String date = list.get("maxpldate");
						String maxpldate = date.substring(5,date.length());
						list.put("maxpldate", maxpldate);
					}
					if(null != list.get("date") && "" != list.get("date")){
						String systemDate = df.format(new Date()).substring(0,11);
						String date = list.get("date").substring(0,11);
						String time = list.get("date").substring(11,16);
						String pubdate = "今天   "+time;
						if(systemDate.equals(date)){
							list.put("date", pubdate);
						}
					}
					
			}
		}
		if(!mesglist.isEmpty() && mesglist.size() > 0){
			for (HashMap<String, String> hashMap : mesglist) {
				if(!hashMap.isEmpty() && hashMap.size() > 0){
					if(null != hashMap.get("lastReplyName") && "" != hashMap.get("lastReplyName")){
						userids.add(hashMap.get("lastReplyName"));
					}
				}
			}
		}
		List<Map<String, String>>infos = chatDao.getLastReplyName(userids);
		if(!mesglist.isEmpty() && mesglist.size()>0){
			for (HashMap<String, String> hashMap : mesglist) {
				if(null != hashMap.get("lastReplyName") && "" != hashMap.get("lastReplyName")){
					if(null != infos && infos.size() > 0){
						for(Map<String,String> map : infos){
							if(!map.isEmpty() && map.size() > 0){
								if(hashMap.get("lastReplyName").equals(map.get("username"))){
									hashMap.put("fullName", map.get("fullname"));
								}
							}
						}
					}
				}
			}
		}
		JSONObject json  = new JSONObject();
		json.put("alllist", mesglist) ;
		output(request, response, json);
	}
	
/*	@SuppressWarnings({ "unchecked", "deprecation" })
	public void showReplylist(HttpServletRequest request,
			HttpServletResponse response) {
		String pl_context_id="";
		String pl_name="";
		String pl_info="";
		Integer pl_userid = null;
		try {
			pl_context_id = new String(request.getParameter("pl_context_id").getBytes("iso8859-1"),"utf-8");
		//	pl_name = new String(request.getParameter("pl_name").getBytes("iso8859-1"),"utf-8");
		//	pl_info = new String(request.getParameter("pl_info").getBytes("iso8859-1"),"utf-8");
			pl_name = URLDecoder.decode(request.getParameter("pl_name"),"utf-8");
			pl_info = URLDecoder.decode(request.getParameter("pl_info"),"utf-8");
			pl_userid = Integer.parseInt(request.getParameter("pl_userid"));
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(chatDao.showReplylist(pl_context_id, pl_name, pl_info,pl_userid)) {
			
			JSONObject json  = new JSONObject();
			json.put("replycontext","OK");
			output(request, response, json);
		}
	}
*/
	public Map<String,String> showReplylist(Map<String, String> params){
		String pl_context_id =null;
		String pl_name=null;
		String pl_info = null;
		String replyUserName = null;
		Map<String,String> returnInfo = new HashMap<String, String>();
		if(!params.isEmpty() && params.size() > 0){
			 pl_context_id = params.get("pl_context_id");
			 pl_name = params.get("pl_name");
			 pl_info = params.get("pl_info");
			 replyUserName = params.get("replyUserName");
		}
		
		if(pl_info!=""){
			pl_info=StringUtil.getGifImg("apps/onlinefile/templates/ESUserCommunity/ckeditor/plugins/smiley/appGifImg/", pl_info);
		}
		if(chatDao.showReplylist(pl_context_id, pl_name, pl_info,replyUserName)) {
			returnInfo.put("sucess", "OK");
		}
		return returnInfo;
	}
	
	@SuppressWarnings("unchecked")
	public void getReplylist(HttpServletRequest request,
			HttpServletResponse response) {
		int pageid=Integer.parseInt(request.getParameter("page")==null || "".equals(request.getParameter("page"))?"0":request.getParameter("page"));
		int limitPage=Integer.parseInt(request.getParameter("limitPage")==null || "".equals(request.getParameter("limitPage"))?"0":request.getParameter("limitPage"));
		String cardId = request.getParameter("pl_context_id");
		if(pageid < 0){
			pageid=0;
		}
		
		//根据帖子id获取发帖人
		String userName = chatDao.getUserIdByCardId(cardId);
		
		List<HashMap<String, String>> mesglist = chatDao.getReplylist(cardId,pageid,limitPage);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!mesglist.isEmpty() && mesglist.size() > 0){
			for(HashMap<String, String> mesg : mesglist){
				mesg.put("userName", userName);
				if("" != mesg.get("pl_date")){
					String date = mesg.get("pl_date").substring(0,11);
					String systemDate = df.format(new Date()).substring(0,11);
					if(date.equals(systemDate)){
						String time = mesg.get("pl_date").substring(11,16);
						String newDate = "今天  " + time;
						mesg.put("pl_date", newDate);
					}else{
						String newDate = mesg.get("pl_date").substring(5,16);
						mesg.put("pl_date", newDate);
					}
				}
			}
		}
		JSONObject json  = new JSONObject();
		if(mesglist!=null){
			json.put("replylist", mesglist) ;
			output(request, response, json);
		}else{
			output(request, response, json);
		}
	}
	
	/**获取用户评论总数**/
	public String getCommentTotal (Map<String, String> params){
		String cardId = params.get("cardId");
		String total = null;
		if(null!= cardId && !"".equals(cardId)){
			total =  chatDao.getCommentTotal(cardId);
		}
		return total;
		
	}
	
	/**获取用户评论*/
	public List<HashMap<String, String>> getReplylistInherit(String pl_context_id,int page,int limet) {
		List<HashMap<String, String>> mesglist = chatDao.getReplylist(pl_context_id,page,limet);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!mesglist.isEmpty() && mesglist.size() > 0){
			for(HashMap<String, String> mesg : mesglist){
				if("" != mesg.get("pl_date")){
					String date = mesg.get("pl_date").substring(0,11);
					String systemDate = df.format(new Date()).substring(0,11);
					if(date.equals(systemDate)){
						String time = mesg.get("pl_date").substring(11,16);
						String newDate = "今天  " + time;
						mesg.put("pl_date", newDate);
					}else{
						String newDate = mesg.get("pl_date").substring(5,16);
						mesg.put("pl_date", newDate);
					}
				}
			}
		}
		return mesglist;
	}
	
	
	
/*	public void getCommunityUserlist(HttpServletRequest request,
			HttpServletResponse response) {
		String username="";
		try {
			username = new String(request.getParameter("username").getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<HashMap<String, String>> mesglist = chatDao.getCommnunityuserlist(username);
		JSONObject json  = new JSONObject();
		json.put("alllist", mesglist);
		output(request, response, json);
		
	}
*/
	public String getinfocommunity(Map<String,String> map) {
		return "123";
	}
	
	public void deleteCommunitytie(HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject json  = new JSONObject();
		String user = request.getParameter("userid");
		boolean flag = chatDao.deleteCommunityuserinfo(user);
		if(flag){
			chatDao.deleteCommunityPraise(user);
			json.put("msg", "删除成功!");
		}else{
			json.put("msg","删除失败");
		}
		json.put("sucess", flag);
		output(request, response, json);
	}

	public void deleteComment(HttpServletRequest request,
			HttpServletResponse response) {
		String userName = request.getParameter("pl_username");
		String pl_id = new String(request.getParameter("pl_id"));
		int contextId = Integer.parseInt(request.getParameter("contextid"));
		JSONObject json = new JSONObject();
		boolean flag = true;
		flag =  chatDao.deleteComment(pl_id,userName,contextId);
		if(flag){
			json.put("msg","删除成功!");
		}else{
			json.put("msg", "删除失败!");
		}
		json.put("sucess",flag);
		output(request, response, json);
	}

	public void getMyCommunity(HttpServletRequest request,
			HttpServletResponse response) {
		String username = request.getParameter("username");
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		int limitPage=Integer.parseInt(request.getParameter("limitPage")==null || "".equals(request.getParameter("limitPage"))?"8":request.getParameter("limitPage"));
		List<HashMap<String, String>> communtiyMessage = chatDao.getCommnunityTypeMessage(null, pageNo, username,limitPage);
		List<String> userids = new ArrayList<String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!communtiyMessage.isEmpty() && communtiyMessage.size()>0){
			for(HashMap<String, String> list : communtiyMessage){
				if( null != list.get("maxpldate") && "" != list.get("maxpldate")){
					String date = list.get("maxpldate");
					String maxpldate = date.substring(5,date.length());
					list.put("maxpldate", maxpldate);
				}
				if(null != list.get("date") && "" != list.get("date")){
					String systemDate = df.format(new Date()).substring(0,11);
					String date = list.get("date").substring(0,11);
					String time = list.get("date").substring(11,16);
					String pubdate = "今天   "+time;
					if(systemDate.equals(date)){
						list.put("date", pubdate);
					}
				}
					
			}
		}
		if(!communtiyMessage.isEmpty() && communtiyMessage.size() > 0){
			for (HashMap<String, String> hashMap : communtiyMessage) {
				if(!hashMap.isEmpty() && hashMap.size() > 0){
					if(null != hashMap.get("lastReplyName") && "" != hashMap.get("lastReplyName")){
						userids.add(hashMap.get("lastReplyName"));
					}
				}
			}
		}
		List<Map<String, String>>infos = chatDao.getLastReplyName(userids);
		if(!communtiyMessage.isEmpty() && communtiyMessage.size()>0){
			for (HashMap<String, String> hashMap : communtiyMessage) {
				if(null != hashMap.get("lastReplyName") && "" != hashMap.get("lastReplyName")){
					if(null != infos && infos.size() > 0){
						for(Map<String,String> map : infos){
							if(!map.isEmpty() && map.size() > 0){
								if(hashMap.get("lastReplyName").equals(map.get("username"))){
									hashMap.put("fullName", map.get("fullname"));
								}
							}
						}
					}
				}
			}
		}
		JSONObject json = new JSONObject();
		json.put("data",communtiyMessage);
		output(request, response, json);
	}

	public void editComunityContext(HttpServletRequest request,
			HttpServletResponse response) {
		int  userid = Integer.parseInt(request.getParameter("userid"));
		List<HashMap<String, String>> cardContext =  chatDao.editComunityContext(userid);
		JSONObject json = new JSONObject();
		json.put("data",cardContext);
		output(request, response, json);
	}

	
	/**点赞/取消赞*/
	@Override
	public String praiseCard(Map<String, String> params) {
		boolean flag = false;
		String cardId = params.get("cardId");
		String userId = params.get("userId");
        String statusStr = params.get("status");
        boolean status = Boolean.parseBoolean(statusStr);
        if(chatDao.praiseCard(cardId,userId,status)){
        	 chatDao.praiseCountUpdate(cardId,userId,status);
        }
		return "";
	}
	
	/**统计最新消息条数**/
	public void  getCountReplyInfo (HttpServletRequest request,HttpServletResponse response){
		String username=request.getParameter("userName");
		int count = chatDao.getCountReplyInfo(username);
		JSONObject json = new JSONObject();
		json.put("count", count);
		output(request, response, json);
	}

	/**获取新信息**/
	public void getCallBackNewMessage(HttpServletRequest request,
			HttpServletResponse response) {
		String pageNo = request.getParameter("page");
		//当前登录的用户
		String userName = request.getParameter("userName");
		int page = Integer.parseInt((pageNo==null || "".equals(pageNo))?"0":pageNo);
		int limitPage=Integer.parseInt(request.getParameter("limitPage")==null || "".equals(request.getParameter("limitPage"))?"10":request.getParameter("limitPage"));
		List<Map<String,String>> datas = chatDao.getCallBackNewMessage(page,limitPage,userName);
		int count = chatDao.getCountReplyInfo(userName);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!datas.isEmpty() && datas.size() > 0){
			for(Map<String, String> mesg : datas){
				if("" != mesg.get("pl_date")){
					String date = mesg.get("pl_date").substring(0,11);
					String systemDate = df.format(new Date()).substring(0,11);
					if(date.equals(systemDate)){
						String time = mesg.get("pl_date").substring(11,16);
						String newDate = "今天  " + time;
						mesg.put("pl_date", newDate);
					}else{
						String newDate = mesg.get("pl_date").substring(5,16);
						mesg.put("pl_date", newDate);
					}
				}
			}
		}
		JSONObject json = new JSONObject();
		json.put("datas", datas);
		json.put("count", count);
		output(request, response, json);
		
	}

	/**更新消息状态**/
	public void updateMessageState(HttpServletRequest request,
			HttpServletResponse response) {
		String cardId = request.getParameter("cardId");
		String plId = request.getParameter("plId");
		String userName = request.getParameter("userName");
		chatDao.updateCommunityMessageState(cardId,plId,userName);
		int count = chatDao.getCountReplyInfo(userName);
		JSONObject json = new JSONObject();
		json.put("count", count);
		output(request, response, json);
		
	}

	/**更新全部消息状态**/
	public void updateMessageAllState(HttpServletRequest request,
			HttpServletResponse response) {
			String userid = request.getParameter("userid");
			String userName = request.getParameter("userName");
			chatDao.updateCommunityMessageState(null,null,userName);
			int count = chatDao.getCountReplyInfo(userName);
			JSONObject json = new JSONObject();
			json.put("count", count);
			output(request, response, json);
	}
}
