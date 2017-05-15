package cn.flying.rest.onlinefile.company.service.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.flying.rest.admin.restInterface.IUserRegistrationServer;
import cn.flying.rest.admin.restInterface.SyncConfigWS;
import cn.flying.rest.onlinefile.company.driver.CompanyDao;
import cn.flying.rest.onlinefile.company.driver.EmailSendDao;
import cn.flying.rest.onlinefile.restInterface.ChatWS;
import cn.flying.rest.onlinefile.restInterface.CompanyRegistWS;
import cn.flying.rest.onlinefile.restInterface.LuceneWS;
import cn.flying.rest.onlinefile.restInterface.UserWS;
import cn.flying.rest.onlinefile.utils.BaseWS;
import cn.flying.rest.onlinefile.utils.Blowfish;
import cn.flying.rest.onlinefile.utils.BroadcastUtils;
import cn.flying.rest.onlinefile.utils.CacheUtils;
import cn.flying.rest.onlinefile.utils.LogUtils;

import com.alibaba.fastjson.JSONObject;

@Path("/companyregist")
@Service
public class CompanyRegistWSImpl extends BaseWS implements CompanyRegistWS{
	
	private static final int String = 0;
	private IUserRegistrationServer userRegistrationServer;
	private UserWS userWS;
	private CompanyDao companyDao;
	private EmailSendDao emailSendDao;
	private SyncConfigWS syncConfigWS ;
	private ChatWS chatWS ;
	private LuceneWS lucene;
	private LogUtils logutils;
	
	private void output(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		response.setContentType("text/javascript;charset=UTF-8");
		StringBuffer output = new StringBuffer(100);
		if(request.getParameter("callback")!=null && "androidInter".equals(request.getParameter("callback"))){
			output.append(json.toJSONString()) ;
		}else{
			output.append(request.getParameter("callback")).append("(") ;
			output.append(json.toJSONString()).append(");") ;
		}
		try {
			response.getWriter().println(output);
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Resource
	public void setEmailSendDao(EmailSendDao emailSendDao) {
		this.emailSendDao = emailSendDao;
	}
	@Resource
	public void setCompanyDao(CompanyDao companyDao) {
		this.companyDao = companyDao;
	}

	public ChatWS getChatWS() {
	    if(chatWS == null){
	    	chatWS = this.getService(ChatWS.class);
	    }
	    return chatWS;
	  }
	
	public SyncConfigWS getSyncConfigWS() {
	    if(syncConfigWS == null){
	      syncConfigWS = this.getService(SyncConfigWS.class);
	    }
	    return syncConfigWS;
	  }

	public IUserRegistrationServer getUserRegistrationServer(){
		if(null == this.userRegistrationServer){
			this.userRegistrationServer = this.getService(IUserRegistrationServer.class);
		}
		return this.userRegistrationServer ;
	}
	public UserWS getUserWS(){
		if(null == this.userWS){
			this.userWS = this.getService(UserWS.class);
		}
		return this.userWS ;
	}

	public LuceneWS getLuceneWS(){
		if(lucene==null){
			this.lucene = this.getService(LuceneWS.class);
		}
		return lucene;
	}
	
	/**获取所有公司信息**/
	@SuppressWarnings("unchecked")
	public Map<String,Map<String,String>>  getAllCompanyInfo(){
		Object obj = CacheUtils.get(getCompLocator(), "companyInfo");
		 Map<String,Map<String,String>> companyInfo = null;
		if(null != obj){
			companyInfo = (Map<String,Map<String,String>>)obj;
		}else{
			companyInfo = companyDao.getAllCompanyInfo();
			CacheUtils.set(compLocator, "companyInfo", companyInfo);
		}
		return companyInfo;
	}
	
	public Boolean sendEmail(Map<String, String> temp) {
		Blowfish blowfish = new Blowfish("vPaAjlpumvgC0n7");//加密用的工具
		
		String companyName = temp.get("companyName");
		String adminName = temp.get("adminName");
		String email = temp.get("email");
		String maxUsers = temp.get("maxUsers");
		System.out.println(companyName + "    " + adminName + "    " + email);
		//插入数据库  返回CODE 返回公司id
		Map<String,String> onlinefileUser = new HashMap<String, String>();
		onlinefileUser.put("COMPANYID","-1");
		onlinefileUser.put("USERNAME", null);
		onlinefileUser.put("FULLNAME", adminName);
		onlinefileUser.put("PASSWORD", null);
		onlinefileUser.put("PORTRAIT", null);
		onlinefileUser.put("TELEOHONE", null);
		onlinefileUser.put("MOBILEPHONE", null);
		onlinefileUser.put("FAX", null);
		onlinefileUser.put("STATUS", "1");
		onlinefileUser.put("ENABLED", "1");
		onlinefileUser.put("ISADMIN", "1");
		onlinefileUser.put("EMAIL", email);
		
		Map<String,String> reutrnMap =getUserWS().saveOrUpdate(onlinefileUser);
		String userId = reutrnMap.get("userId");//当前管理员的id
		String code = reutrnMap.get("code");//当前管理员的code
		//插入数据库  返回CODE 返回公司id
		String params = "companyName="+companyName+"&adminName="+adminName+"&email="+email+"&userId="+userId+"&code="+code;
		String encodeParams = blowfish.encrypt(params);
		String activateURL = "http://127.0.0.1:8080/flyingoauth/toactivate?"+encodeParams;
		System.out.println(activateURL);
		int status = 1;
		String toEmailAddress = email;
				
		String subject="";
		String content="";
		if (status == 1) {
			subject="注册成功";
			content="<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
					+"<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
					+"<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
					+"<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"+adminName+"</strong>欢迎您注册Onlinefile，点击下面的绿色按钮建立<strong>"+companyName+"</strong>团队:</p>"
					+"<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
					+"<a href=\""+activateURL+"\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">立即激活</a>"
					+"</p>"
					+"<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
					+"<p style=\"font-size: 18px; line-height: 24px;\"><strong>使用Onlinefile为您的团队工作</strong> – 您可以在线分享和收藏，日常交流.</p>"
//					+"<p style=\"font-size: 18px;text-align: center; line-height: 24px;\">您的飞扬用户名是 <strong>"+username+"</strong>!"
					+"</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
					+"<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
					+"祝好运!<br>"
					+"--"+"东方飞扬"
					+"<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
					+"</div>"
					+"</p>"
					+"</div></td></tr></tbody></table>";
		} else {
			subject="注册失败";
			content="尊敬的用户：遗憾的通知您，没有通过申请，感谢您对北京东方飞扬软件股份有限公司的支持！";
		}

		return emailSendDao.sendHtmlMail(toEmailAddress, subject, content, true);
	}

	public Boolean test() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String saveCompany(Map<String, String> params) {
		String companyName = params.get("companyName");
		String username = params.get("username");
		String fullname = params.get("fullname");
		String realpassword = params.get("password");
		String password = realpassword;
		String email = params.get("email");
//		System.out.println(companyName + "    " + username + "    " + password + "    " + email + "    " );
		//2、存储onlinefile 中的 company 表
		Map<String,String> companyMap = new HashMap<String, String>();
		companyMap.put("NAME", companyName);
		companyMap.put("ADDRESSES", "");
		companyMap.put("PHONE", "");
		companyMap.put("FAX", "");
		companyMap.put("POSTCODE", "");
		companyMap.put("URL", "");
		companyMap.put("SERVER_ID", "1");
		companyMap.put("appDate", params.get("appDate"));
		int companyId = companyDao.saveCompany(companyMap);
		if (companyId > 0) {
		    Object obj = CacheUtils.get(getCompLocator(), "companyInfo");
		    Map<String,Map<String,String>> companyInfo = null;
		    if(null != obj){
		        companyInfo = (Map<String,Map<String,String>>)obj;
		    }else{
		        companyInfo = new HashMap<String,Map<String,String>>();
		    }
		    Map<String,String> cacheMap = new HashMap<String,String>() ;
		    cacheMap.put("ID", companyId+"");
		    cacheMap.put("NAME", companyName);
		    cacheMap.put("ADDRESSES", "");
		    cacheMap.put("PHONE", ""); 
		    cacheMap.put("FAX", "");
		    cacheMap.put("POSTCODE", "");
		    cacheMap.put("SERVER_ID", "1");
		    cacheMap.put("URL", "");
		    cacheMap.put("IMGURL", "");
		    companyInfo.put(companyId+"", cacheMap) ;
		    CacheUtils.set(compLocator, "companyInfo", companyInfo);
		    
		    //3、修改onlinefile 中的 users 表
		    Map<String,String> onlinefileUser = new HashMap<String, String>();
		    onlinefileUser.put("companyid", companyId+"");
		    onlinefileUser.put("username", username);
		    onlinefileUser.put("fullname", fullname);
		    Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		    password = md5.encodePassword(password, params.get("username"));
		    onlinefileUser.put("password", password);
		    onlinefileUser.put("email", email);
		    
		    Map<String,String> data = new HashMap<String,String>();
		    data.put("USERID", params.get("username"));
		    String FIRSTNAME = "" ;
		    String LASTNAME = "" ;
		    if(null != fullname && !"".equals(fullname)){
		        LASTNAME = fullname.substring(0, 1) ;
		        if(fullname.length()>1)FIRSTNAME = fullname.substring(1) ;
		    }
		    data.put("FIRSTNAME", FIRSTNAME);
		    data.put("LASTNAME", LASTNAME);
		    data.put("PASSWORD", password);
		    data.put("USERSTATUS", "1");
		    data.put("SAASID", companyId+"");//之前是公司id  现在改为0
		    data.put("ISUPDATE", "insert");
		    Map<String,Object> msg = getSyncConfigWS().synchronizeUserToPlatform(data);
		    if(Boolean.valueOf(msg.get("success").toString())){
		        boolean flag = getUserWS().saveUser(onlinefileUser);
		        if(flag){
		            companyDao.saveCompanyReferrer(companyId, params.get("referrerName")) ;
		            String arg = null;
		            try {
//        			arg = "secret=flyingsoft&type=add&username="+username+"&password="+realpassword+"&name="+URLEncoder.encode(fullname, "UTF-8");
		                arg = "secret=flyingsoft&type=add&username="+username+"&password=Fy_Documents_Of&name="+URLEncoder.encode(fullname, "UTF-8");
		            } catch (UnsupportedEncodingException e) {
		                e.printStackTrace();
		            }
		            String returnStr = post(arg) ;
		            System.out.println(returnStr);
		            arg = "secret=flyingsoft&type=add_group&groups=company"+companyId+"&username="+username.replace("@", "\\40") ;
		            returnStr = post(arg) ;
		            try{
		                this.createTables(companyId) ;
		            } catch (Exception e){
		                e.printStackTrace(); 
		            }
		            System.out.println(returnStr);
		        }
		        return flag+"";
		    } else {
		        return msg.get("msg").toString();
		    }
		} else {
		    return "false";
		}
	}
	
	/**
	 * 创建系统正常运行的默认表
	 * @param companyId
	 */
	private void createTables(int companyId) {
		companyDao.createTables(companyId) ;
		companyDao.insertIntoFilesN("files_"+companyId);
		//向mongodb的两张表中插入数据，这样建立索引库的时候 首次查询就不会报错了
		Map<String,String> map = new HashMap<String, String>();
		map.put("isGroup", "0");
		map.put("companyId", companyId+"");
		map.put("GROUPFLAG", "indexDbinit") ;
		map.put("FROMCNNAME", "系统初始化") ;
		map.put("FILEID", "0"); // 记录该条消息关联的文件
	    map.put("CHATFLAG", "0_0") ;
	    map.put("FROMUSER", "0") ;
	    map.put("CONTENT", "系统初始化") ;
	    map.put("DATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date())) ;
	    map.put("TIME", new SimpleDateFormat("HH:mm:ss").format(new Date())) ;
	    map.put("STYLE", "undefined") ;
	    getChatWS().initMongodbMsg(map);
	    map.put("isGroup", "1");
	    getChatWS().initMongodbMsg(map);
	    //在这初始化索引库
	    getLuceneWS().createNewIndex(companyId+"");
	}
	
	private String post(String arg){
		String mag = null ;
		try {
			URL oUrl = new URL("http://"+BroadcastUtils.openfireIp+":"+BroadcastUtils.openfireServerPort+"/plugins/userService/userservice");
			// 打开和URL之间的连接
			URLConnection conn = oUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("ContentType","text/xml;charset=utf-8"); 
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
			BufferedReader oIn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			if(null!=oIn){
				mag = oIn.readLine();
				oIn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return mag ;
	}
	
//	public static void main(String[] args){
//		String mag = null ;
//		try {
//			URL oUrl = new URL("http://im.flying.cn:9090/plugins/userService/userservice");
//			// 打开和URL之间的连接
//			URLConnection conn = oUrl.openConnection();
//			// 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//			// 发送POST请求必须设置如下两行
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			conn.setConnectTimeout(30000);  
//			conn.setReadTimeout(30000); 
//			// 获取URLConnection对象对应的输出流
//			PrintWriter out = new PrintWriter(conn.getOutputStream());
//			// 发送请求参数 name1=value1&name2=value2 
//			out.print("secret=flyingsoft&type=add&username=ljbxx1@163.com&password=11&name=肖雄");
//			// flush输出流的缓冲
//			out.flush();
//			BufferedReader oIn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			if(null!=oIn){
//				mag = oIn.readLine();
//				oIn.close();
//			}
//			System.out.println(mag);
//		} catch (Exception e) {
//			e.printStackTrace(); 
//		}
//	}
	
	/**根据公司ID获取公司信息**/
	public Map<String, String> getCampanyInfoById(Map<String, String> params) {
		Map<String,Map<String,String>> companyInfo = getAllCompanyInfo();
		String companyId = params.get("companyId");
		Map<String, String> returnmap = null;
		if(companyInfo != null && !companyInfo.isEmpty()){
			returnmap =  companyInfo.get(companyId);
		}
		return returnmap;
	}
	
	/**liuwei 20150518**/
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> editCampanyInfo(Map<String, String> params) {
		String companyId = params.get("companyid").trim();
		String companyName = params.get("companyname").trim();
		/** 20151028 xiayongcai 加入企业名称验证*/
		boolean flag=true;
		Map<String,Object> reutrnMap = new HashMap<String, Object>();
			//根据企业名称获取企业
			List<Map<String,String>> compayList=companyDao.getCompayByCompayName(params.get("companyname").trim());
			if(compayList!=null && compayList.size() > 0 ){
				for(Map<String,String> compay:compayList){
					if(compay.get("companyId")!=null && !companyId.equals(compay.get("companyId"))){
						if(compay.get("companyName") !=null && companyName.equals(compay.get("companyName"))){
							flag=false;
						}
					}
				}
			}
			if(flag){
				flag = companyDao.editCampanyInfo(params);
				Object obj  = CacheUtils.get(this.compLocator,"companyInfo");
				Map<String,Map<String,String>> companyInfo = null;
				if(flag && null != obj){
					companyInfo = (Map<String,Map<String,String>>)obj;
					if(companyInfo.get(companyId)!= null){
						Map<String,String> map = companyInfo.get(companyId);
						map.put("NAME",params.get("companyname"));
						map.put("ADDRESSES",params.get("addresses"));
						map.put("PHONE",params.get("companyphone"));
						map.put("FAX",params.get("companyfax"));
						map.put("POSTCODE",params.get("postcode"));
						CacheUtils.set(getCompLocator(), "companyInfo", companyInfo);
					}
				}else{
					companyInfo = this.getAllCompanyInfo();
					CacheUtils.set(getCompLocator(), "companyInfo", companyInfo);
				}
			}
		reutrnMap.put("success", flag);
		return reutrnMap;
	}
	
	/**liuwei 20150518**/
	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public Map<String, String> saveHeadImage(Map<String, String> params) {
		Map<String,String> reutrnMap = new HashMap<String, String>();
		String companyId = params.get("companyid");
		if(companyDao.saveHeadImage(params)){
			reutrnMap.put("success", "true");
			Object obj  = CacheUtils.get(this.compLocator,"companyInfo");
			Map<String,Map<String,String>> companyInfo = null;
			if(null != obj){
				companyInfo = (Map<String,Map<String,String>>)obj;
				if(companyInfo.get(companyId)!= null){
					Map<String,String> map  = companyInfo.get(companyId);
					map.put("IMGURL", params.get("path"));
				}
				CacheUtils.set(getCompLocator(), "companyInfo", companyInfo);
			}else{
				companyInfo = this.getAllCompanyInfo();
				CacheUtils.set(getCompLocator(), "companyInfo", companyInfo);
			}	
		}else{
			reutrnMap.put("success", "false");
		}
		return reutrnMap;
	}
	
	@Override
	public Boolean validateUrl(String start) {
		return companyDao.validateUrl(start);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void getCompanyReferrer(HttpServletRequest request,HttpServletResponse response) {
		Map<String,String> map = new HashMap<String, String>();
		int start = (Integer.parseInt(request.getParameter("start"))-1)*Integer.parseInt(request.getParameter("limit"));
		map.put("start",start+"");
		map.put("limit", request.getParameter("limit"));
		String referrer = null;
		try {
			 referrer = URLDecoder.decode(request.getParameter("username"),"utf-8");
			map.put("referrer", referrer);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Object obj  = CacheUtils.get(this.compLocator,"referrerInfo");
		/*List<Map<String,String>> referrerInfo = null;
		if(null != obj){
			referrerInfo  =(List<Map<String,String>>) CacheUtils.get(this.compLocator,"referrerInfo");
		}else{
			 referrerInfo = companyDao.getCompanyReferrer(map);
			 CacheUtils.set(getCompLocator(), "referrerInfo", referrerInfo);
		}*/
		List<Map<String,String>> referrerInfo = companyDao.getCompanyReferrer(map);
		int totle = companyDao.getCompanyReferrerCount(referrer);
		Integer totles = (totle-1)/Integer.parseInt(request.getParameter("limit"))+1;
		JSONObject json  = new JSONObject();
		json.put("referrerInfo", referrerInfo);
		json.put("totles", totles);
		output(request, response, json);
	}

	@Override
	public Map<String, String> registerNewCampany(Map<String, String> params) {
		Map<String, String> map = new HashMap<String,String>() ;
		String companyName = params.get("companyName");
		String email = params.get("email") ;
		String userid = params.get("userid") ;
		Map<String,String> companyMap = new HashMap<String, String>();
		companyMap.put("NAME", companyName.trim());
		companyMap.put("ADDRESSES", "");
		companyMap.put("PHONE", "");
		companyMap.put("FAX", "");
		companyMap.put("POSTCODE", "");
		companyMap.put("URL", "");
		companyMap.put("SERVER_ID", "1");
		companyMap.put("appDate", params.get("appDate"));
		/**20151015  xiayongcai 注册新企业
		 * 一个用户只能注册一个企业
		 * 企业名称不能相同
		 * 20151105 xiayongcai 一个用户可以注册多个企业. 
		 *  */
		//判断企业是否已经被注册
		if(!companyDao.isCompayNameExist(companyName.trim())){
			//验证该用户是否已经注册过
		//	if(!getUserWS().isUserCompayExist(userid)){
				int companyId = companyDao.saveCompany(companyMap);
				// 判断新建企业时候成功
				if (companyId > 0) { // 成功 
				    Object obj = CacheUtils.get(getCompLocator(), "companyInfo");
				    Map<String,Map<String,String>> companyInfo = null;
				    if(null != obj){
				        companyInfo = (Map<String,Map<String,String>>)obj;
				    }else{
				        companyInfo = new HashMap<String,Map<String,String>>();
				    }
				    Map<String,String> cacheMap = new HashMap<String,String>() ;
				    cacheMap.put("ID", companyId+"");
				    cacheMap.put("NAME", companyName);
				    cacheMap.put("ADDRESSES", "");
				    cacheMap.put("PHONE", ""); 
				    cacheMap.put("FAX", "");
				    cacheMap.put("POSTCODE", "");
				    cacheMap.put("SERVER_ID", "1");
				    cacheMap.put("URL", "");
				    cacheMap.put("IMGURL", "");
				    companyInfo.put(companyId+"", cacheMap) ;
				    CacheUtils.set(compLocator, "companyInfo", companyInfo);
				    
				    //3、修改onlinefile 中的 users 表
				    Map<String,String> user = new HashMap<String, String>();
				    user.put("companyid", companyId+"");
				    user.put("email", email);
				    user.put("userid", userid);
				    boolean flag = getUserWS().saveCompanyUsers(user);
				    if(flag){
				        Map<String, Map<String, String>> companyUserInitInfo = null;
				        obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+companyId) ;
				        if(obj == null){
				            companyUserInitInfo = new HashMap<String, Map<String, String>>() ;
				        }
				        companyUserInitInfo.put(email, getUserWS().getOneUserInfo(userid)) ;
				        CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"+companyId, companyUserInitInfo);
				        
				        String arg = "secret=flyingsoft&type=add_group&groups=company"+companyId+"&username="+email.replace("@", "\\40") ;
				        String returnStr = post(arg) ;
				        try{
				            this.createTables(companyId) ;
				        } catch (Exception e){
				            e.printStackTrace(); 
				        }
				        HashMap<String, List<String>> utc = new HashMap<String, List<String>>();
				        HashMap<String, String> companyAdmin = new HashMap<String, String>() ;
				        obj = CacheUtils.get(this.getCompLocator(), "ofuserCompanys") ;
				        if (null != obj) {
				            utc = (HashMap<String, List<String>>) obj ;
				        }
				        obj = CacheUtils.get(this.getCompLocator(), "ofcompanyAdmin") ;
				        if (null != obj) {
				            companyAdmin = (HashMap<String, String>) obj ;
				        }
				        if(utc.get(userid) == null){
				            utc.put(userid, new ArrayList<String>()) ;
				        }
				        utc.get(userid).add(companyId+"") ;
				        user.put("userCompanySize", utc.get(userid).size()+"") ;
				        companyAdmin.put(companyId+"", userid) ;
				        CacheUtils.set(this.getCompLocator(), "ofuserCompanys", utc);
				        CacheUtils.set(this.getCompLocator(), "ofcompanyAdmin", companyAdmin);
				        
				        HashMap<String, List<HashMap<String, String>>> companyUserIsStatus = new HashMap<String, List<HashMap<String, String>>>() ;
				        HashMap<String, String> companyIsStatus = new HashMap<String, String>() ;
				        obj = CacheUtils.get(this.getCompLocator(), "ofcompanyUserIsStatus") ;
				        if (null != obj) {
				        	companyUserIsStatus = (HashMap<String, List<HashMap<String, String>>>) obj ;
				        }
				        if(companyUserIsStatus.get(userid) == null){
							companyUserIsStatus.put(userid,new ArrayList<HashMap<String, String>>()) ;
						}
				        companyIsStatus.put(companyId+"", "1");
				        companyUserIsStatus.get(userid).add(companyIsStatus) ;
				      //2015.0916.添加公司用户状态
						CacheUtils.set(this.getCompLocator(), "ofcompanyUserIsStatus", companyUserIsStatus);
				    }
				    map.put("ownCompanyId",companyId+"");
				    map.put("msg", "新企业注册成功，在右上角的下拉菜单中可以切换到新的企业");
				    map.put("success","true");
				} else {
					map.put("ownCompanyId","-1");
					map.put("msg", "注册失败");
				    map.put("success", "false");
				}
				/*	}else{
				map.put("ownCompanyId","-1");
				map.put("msg", "一个帐号只能注册一个公司,请使用新帐号注册新企业");
				map.put("success", "false");
			}*/
		}else{
			map.put("ownCompanyId","-1");
			map.put("msg", "您注册的企业名称已被人注册,请重新定义企业名称");
			map.put("success", "false");
		}
    	return map;
	}
	
	@Override
	public Map<String, String> uploadHeadImage(HttpServletRequest request) {
		String companyid="";
		String filePat="";
		StringBuffer sbRealPath=null;
		Date date=new Date();
 		DateFormat format=new SimpleDateFormat("yyyyMMdd");
 		DateFormat format1=new SimpleDateFormat("HH:mm:ss");
		 FileItemFactory factory = new DiskFileItemFactory();
		 ServletFileUpload upload = new ServletFileUpload(factory);
		 upload.setHeaderEncoding("utf-8");
		 File filePath=new File("E:/workspace/ESCLOUDAPP/html/files/logoimage/");
		    try {
		      List<FileItem> items = upload.parseRequest(request);
		      for (FileItem item : items) {
		        if (!item.isFormField()) {
		        	String fileName = item.getName();  
		        	String fileEnd = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();  
		    		long timeStart = format1.parse(format1.format(date)).getTime();
		    		String time=format.format(date);
		             //真实上传路径  
		             sbRealPath = new StringBuffer();  
		             sbRealPath.append(time+timeStart+((int)(Math.random()*900)+100)).append(".").append(fileEnd);
		             filePat="files/logoimage/"+sbRealPath.toString();
		             //写入文件  
		             File file = new File(filePath+"/"+sbRealPath.toString()); 
		             item.write(file); 
		        }else if(item.getFieldName().equals("companyid")){
		        	companyid = item.getString("UTF-8");
		        }
	
		      }
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
			Map<String, String> paMap = new HashMap<String, String>();
			Map<String, String> returnMap = new HashMap<String, String>();
			paMap.put("path", filePat);
			paMap.put("companyid", companyid);
			returnMap=this.saveHeadImage(paMap);
			returnMap.put("imgPath", filePat);
			return returnMap;
	}

  @Override
  public Map<String, Object> getCompanyReferrer(Map<String, String> param) {
    Map<String,String> map = new HashMap<String, String>();
    int start = (Integer.parseInt(param.get("start"))-1)*Integer.parseInt(param.get("limit"));
    map.put("start",start+"");
    map.put("limit", param.get("limit"));
    String referrer = null;
    try {
         referrer = URLDecoder.decode(param.get("username"),"utf-8");
        map.put("referrer", referrer);
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    Object obj  = CacheUtils.get(this.compLocator,"referrerInfo");
    /*List<Map<String,String>> referrerInfo = null;
    if(null != obj){
        referrerInfo  =(List<Map<String,String>>) CacheUtils.get(this.compLocator,"referrerInfo");
    }else{
         referrerInfo = companyDao.getCompanyReferrer(map);
         CacheUtils.set(getCompLocator(), "referrerInfo", referrerInfo);
    }*/
    List<Map<String,String>> referrerInfo = companyDao.getCompanyReferrer(map);
    int totle = companyDao.getCompanyReferrerCount(referrer);
    Integer totles = (totle-1)/Integer.parseInt(param.get("limit"))+1;
    Map<String,Object> json  = new HashMap<String,Object>();
    json.put("referrerInfo", referrerInfo);
    json.put("totles", totles);
    return json;
    
  }

	@Override
	public Boolean isCompayNameExist(String compayName) {
		return companyDao.isCompayNameExist(compayName);
	}

	/**xiayongcai20151020 切换企业*/
	@Override
	public Map<String, String> switchoverCompany(Map<String, String> param) {
		Map<String,String> reMap=new HashMap<String, String>();
		String userId=param.get("userId");
		String companyId=param.get("companyId");
		boolean flg= false;
		Map<String, String> rMap=companyDao.isCompayUserExist(companyId,userId);
		if("0".equals(rMap.get("ISDEL"))){
			if(rMap.get("STATUS")!= null && "1".equals(rMap.get("STATUS"))){
				Map<String, String> dataMap=new HashMap<String, String>();
				dataMap=getUserWS().editUserCompanyId(companyId,userId);
				if(dataMap!=null && dataMap.size()>0 && dataMap.get("success")!=null && "true".equals(dataMap.get("success"))){
					flg=true;
					reMap.put("msg", "正在切换,请稍等...");
				}else{
					reMap.put("msg", "您所在企业未激活,不能进行切换...");
				}
			}else{
				reMap.put("msg", "您所在企业未激活,不能进行切换...");
			}
		}else{
			reMap.put("msg", "该企业已被管理员注销...");
		}
		reMap.put("success", flg+"");
		
		return reMap;
	}
	
	public Map<String, String> cancelCompany(Map<String, String> param){
		Map<String, String> rtnMap = new HashMap<String, String>();
		//进行删除
		if(companyDao.cancelCompany(param)){
			rtnMap.put("success", "true");
		}else{
			rtnMap.put("success", "false");
		}
		return rtnMap;
	}

  @Override
  public Map<String, Object> getCompanyUserForTransfer(Map<String, String> param) {
    String companyId = param.get("companyId");
    String userid = param.get("userId");
    Map<String,Object> userMap = new HashMap<String,Object>();
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
    Iterator<Entry<String, Map<String, String>>> iterator = companyUserInitInfo
            .entrySet().iterator();
    Entry<String, Map<String, String>> entry = null;
    Map<String, String> user = null;
    List<Map<String, String>> users = new ArrayList<Map<String, String>>();
    while (iterator.hasNext()) {
        entry = iterator.next();
        user = entry.getValue();
        if (userid.equals(user.get("ID"))) continue;
        user.put("showname",
                (user.get("FULLNAME").length() > 10 ? user.get("FULLNAME").substring(0, 10) + "...":user.get("FULLNAME")));
         users.add(user);
        }

    userMap.put("inUsers", users);
    return userMap;
  }
  @Override
  public Map<String, Object> transferCompany(Map<String, String> param) {
    String companyId = param.get("companyId");
    String companyName = param.get("companyName");
    String userid = param.get("userId");
    String userName = param.get("userName");
    String touserId = param.get("touserId");
    String touserName = param.get("touserName");
    Map<String,Object> result = new HashMap<String,Object>();
        
    boolean flag = companyDao.transferCompany(param);
    if(flag){
   //更新缓存
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
      HashMap<String, String> companyAdmin = new HashMap<String, String>();
      obj = CacheUtils.get(this.getCompLocator(), "ofcompanyAdmin");
      if (null != obj) {
          companyAdmin = (HashMap<String, String>) obj;
      }
      companyUserInitInfo.get(userName).put("ISADMIN", "0");
      companyUserInitInfo.get(touserName).put("ISADMIN", "1");
      companyAdmin.put(companyId, touserId);
      CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
          + companyId, companyUserInitInfo);
      CacheUtils.set(this.getCompLocator(), "ofcompanyAdmin",companyAdmin);
   // 日志------------------------------------------
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("userid", userid);
      log.put("username", userid);
      log.put("ip", param.get("ip"));
      log.put("module", "移交公司");
      log.put("operate", "移交公司");
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("loginfo", "用户ID为【"
              + userid + "】将公司名称为【" + companyName + "】"+"公司ID为【"+companyId+"】移交给用户名【"+touserName+"】ID为【"+touserId+"】的用户");
      logutils.saveBaseLog(this.getCompLocator(), log);
    }
    result.put("isOk", flag);
    return result;
  }
	
}
	
	
	
