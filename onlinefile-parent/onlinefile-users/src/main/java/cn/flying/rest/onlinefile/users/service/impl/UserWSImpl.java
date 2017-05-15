package cn.flying.rest.onlinefile.users.service.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.hibernate.envers.tools.StringTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.flying.rest.admin.restInterface.IUserRegistrationServer;
import cn.flying.rest.admin.restInterface.SyncConfigWS;
import cn.flying.rest.onlinefile.entity.Header;
import cn.flying.rest.onlinefile.entity.ImportInfo;
import cn.flying.rest.onlinefile.restInterface.ChatWS;
import cn.flying.rest.onlinefile.restInterface.CompanyRegistWS;
import cn.flying.rest.onlinefile.restInterface.DocumentClassWS;
import cn.flying.rest.onlinefile.restInterface.FilesWS;
import cn.flying.rest.onlinefile.restInterface.LuceneWS;
import cn.flying.rest.onlinefile.restInterface.MessageQueueProducerWS;
import cn.flying.rest.onlinefile.restInterface.UserWS;
import cn.flying.rest.onlinefile.users.driver.UserDao;
import cn.flying.rest.onlinefile.utils.BaseWS;
import cn.flying.rest.onlinefile.utils.Blowfish;
import cn.flying.rest.onlinefile.utils.BroadcastUtils;
import cn.flying.rest.onlinefile.utils.CacheUtils;
import cn.flying.rest.onlinefile.utils.DataTable;
import cn.flying.rest.onlinefile.utils.LogUtils;
import cn.flying.rest.onlinefile.utils.ParseUtil;
import cn.flying.rest.onlinefile.utils.StringUtil;
import cn.flying.rest.onlinefile.utils.ThreadPoolManager;
import cn.flying.rest.onlinefile.utils.email.MyAuthenticator;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

@Path("/onlinefileuser")
@Service
public class UserWSImpl extends BaseWS implements UserWS {
	
	@Value("${onlinefile.messagequeue.factoryUtil.ImageLinePath}")
	private String activeMQPath ;
	@Value("${flyingSoft.activateHost}")
	private String activateHost ;
	@Value("${flyingSoft.activateHostUrl}")
	private String activateHostUrl ;
	@Value("${android.up.img.url}")
	private String androidUpImgUrl ;

    private LogUtils logutils;
    private String mailServerHost;

    private String mailServerPort;
    // 邮件发送者的地址

    private String fromAddress;
    // 登陆邮件发送服务器的用户名和密码

    private String userName;
    private String password;

    private UserDao userDao;
    private IUserRegistrationServer userRegistrationServer;

    private CompanyRegistWS companyRegistWS;

    private MessageQueueProducerWS producerWS;

    private LuceneWS lucene;
    
    private static  Logger logger = Logger.getLogger(UserWSImpl.class);

    public MessageQueueProducerWS getProducerWS() {
        if (producerWS == null) {
//          synchronized (MessageQueueProducerWS.class) {
//              if (producerWS == null) {
                    producerWS = this.getService(MessageQueueProducerWS.class);
//              }
//          }
        }
        return producerWS;
    }

    public CompanyRegistWS getCompanyRegistWS() {
        if (null == this.companyRegistWS) {
            this.companyRegistWS = this.getService(CompanyRegistWS.class);
        }
        return companyRegistWS;
    }

	public LuceneWS getLucene(){
		if(lucene==null){
			this.lucene = this.getService(LuceneWS.class);
		}
		return lucene;
	}
    
    // 导入功能记录用户，与导入信息
    private static Map<String, ImportInfo> imap = new HashMap<String, ImportInfo>();

    private SyncConfigWS syncConfigWS;

    public IUserRegistrationServer getUserRegistrationServer() {
        if (null == this.userRegistrationServer) {
            this.userRegistrationServer = this
                    .getService(IUserRegistrationServer.class);
        }
        return this.userRegistrationServer;
    }

    public SyncConfigWS getSyncConfigWS() {
        if (syncConfigWS == null) {
            syncConfigWS = this.getService(SyncConfigWS.class);
        }
        return syncConfigWS;
    }

    @Resource
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private DocumentClassWS documentClassWS;

    public DocumentClassWS getDocumentClassWS() {
        if (null == this.documentClassWS) {
            this.documentClassWS = this.getService(DocumentClassWS.class);
        }
        return this.documentClassWS;
    }

    private ChatWS chatWS;

    public ChatWS getChatWS() {
        if (null == this.chatWS) {
            this.chatWS = this.getService(ChatWS.class);
        }
        return this.chatWS;
    }

    // 用户被禁用或者启用后 其创建的文件交由管理员管理或者恢复本身管理
    private FilesWS filesWS;

    public FilesWS getFilesWS() {
        if (null == this.filesWS) {
            this.filesWS = this.getService(FilesWS.class);
        }
        return this.filesWS;
    }
    
   /* private WechatWS wechatWS;
    
    public WechatWS getWechatWS(){
    	
    	 if (null == this.wechatWS) {
             this.wechatWS = this.getService(WechatWS.class);
         }
         return this.wechatWS;
    }
    */
   

    public Map<String, String> saveOrUpdate(Map<String, String> params) {
        Map<String, String> reutrnMap = new HashMap<String, String>();
        params.put("CODE", MD5(params.get("FULLNAME")));
        int userId = userDao.saveUser(params);
        reutrnMap.put("userId", userId + "");
        reutrnMap.put("code", MD5(params.get("FULLNAME")));
        return reutrnMap;
    }

    public Map<String, String> getOrgInfo(String companyId) {
        return userDao.getOrgInfo(companyId);
    }

    public Map<String, Map<String, String>> companyUserInitInfo(String companyId) {
        return userDao.getUserInitInfo(companyId);
    }

    /**
     * 根据用户的登陆名称获取用户表的id，缓存优化
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserInitInfo(String companyId, String userName) {
    	
    	//所有公司都被禁用的用户
    	if(companyId.equals("-1")){
    		 Map<String, Object> returnMap = new HashMap<String, Object>();
    		 Map<String, Object> ret = new HashMap<String, Object>();
    		 ret= userDao.getUserInfoByUserName(userName);
    		 Blowfish blowfish = new Blowfish("vPaAjlpumvgC0n7");// 加密用的工具
    		 ret.put("usernamemd5code", blowfish.encrypt(userName));
    		 userDao.getUserInitInfo("","");
    		 List<Map<String, String>> userCompanys =userDao.getCompanysByUserNameList(userName);
    		 ret.put("userCompanySize", userCompanys.size());
    	     returnMap.put("userCompanys", userCompanys);
    		 returnMap.put("user", ret);
    		 return returnMap;
    	}
    		
    	
        if (CacheUtils.get(getCompLocator(), "ofuserCompanys") == null) {
            userDao.cacheUserToCompanys(this.getCompLocator());
        }
        // 用户是否已启用
        /*
         * //公司下的用户状态 HashMap<String, List<HashMap<String, String>>>
         * companyUserIsStatus = null; Object obj =
         * CacheUtils.get(this.getCompLocator(), "ofcompanyUserIsStatus") ;
         * if(obj != null){ companyUserIsStatus = (HashMap<String,
         * List<HashMap<String, String>>>) obj ; } //缓存 List<HashMap<String,
         * String>> companyUserIsStatuslist=companyUserIsStatus.get(userids);
         * for(HashMap<String, String> companyUserIs:companyUserIsStatuslist){
         * for(String key :companyUserIs.keySet()){ if(key.equals(companyId)){
         * companyUserIs.put(key, userstatus) ; } }
         * CacheUtils.set(this.getCompLocator(), "ofcompanyUserIsStatus",
         * companyUserIsStatus) ; }
         */

        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(),
                "companyUserInitInfo" + companyId);
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        }
        if (null == companyUserInitInfo || companyUserInitInfo.isEmpty()) {
            companyUserInitInfo = userDao.getUserInitInfo(companyId);
            CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                    + companyId, companyUserInitInfo);
        }
        terminateSession(companyId, userName, companyUserInitInfo);
        obj = CacheUtils.get(this.getCompLocator(), "companyOrgInfo"
                + companyId);
        if (null == obj) {
            Map<String, String> companyOrgInfo = userDao.getOrgInfo(companyId);
            CacheUtils.set(this.getCompLocator(), "companyOrgInfo" + companyId,
                    companyOrgInfo);
        }
        Map<String, String> user = companyUserInitInfo.get(userName);
        if (user == null || user.isEmpty()) {
            user = userDao.getUserInitInfo(companyId, userName);
            companyUserInitInfo.put(userName, user);
            CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                    + companyId, companyUserInitInfo);
        }
        if (user != null && "1".equals(user.get("FIR_LOGIN"))) {
            userDao.updateFirstLoginFlag(user.get("ID"));
            Map<String, String> newUser = new HashMap<String, String>();
            Iterator<Entry<String, String>> iterator = user.entrySet()
                    .iterator();
            Entry<String, String> entry = null;
            while (iterator.hasNext()) {
                entry = iterator.next();
                newUser.put(entry.getKey(), entry.getValue());
            }
            newUser.put("FIR_LOGIN", "0");
            companyUserInitInfo.put(userName, newUser);
            CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                    + companyId, companyUserInitInfo);
        }
        Blowfish blowfish = new Blowfish("vPaAjlpumvgC0n7");// 加密用的工具
        user.put("usernamemd5code", blowfish.encrypt(userName));
        Map<String, Map<String, String>> companyInfo = getCompanyRegistWS()
                .getAllCompanyInfo();

        if (companyInfo != null && !companyInfo.isEmpty()) {
            if (companyInfo.get(companyId) != null
                    && !companyInfo.get(companyId).isEmpty()) {
                if (companyInfo.get(companyId).get("IMGURL") != null
                        && companyInfo.get(companyId).get("IMGURL").trim() != "") {
                    user.put("IMGURL", companyInfo.get(companyId).get("IMGURL"));
                } else {
                    user.put("IMGURL", "");
                }
                if (companyInfo.get(companyId).get("NAME") != null
                        && companyInfo.get(companyId).get("NAME").trim() != "") {
                    user.put("COMPANYNAME",
                            companyInfo.get(companyId).get("NAME"));
                } else {
                    user.put("COMPANYNAME", "");
                }
            } else {
                user.put("IMGURL", "");
                user.put("COMPANYNAME", "");
            }
        }

		//检测用户自己的文件表等是否存在  不存在就创建 wangwenshuo 20151119
	    userDao.createUserTables(user.get("ID"));
	    //索引是否存在 不存在就创建
		getLucene().createNewIndex("user_"+user.get("ID"));
        
        // 获取企业用户所有用户的个性设置
        Map<String, Map<String, String>> companyUserSingleSet = null;
        obj = CacheUtils.get(this.getCompLocator(), "companyUserSingleSet"+ companyId);// 缓存不存在的时候
        if (null == obj) {
            companyUserSingleSet = userDao.getUserSingleSet(companyId);
            CacheUtils.set(this.getCompLocator(), "companyUserSingleSet"+ companyId, companyUserSingleSet);
        } else {
            companyUserSingleSet = (Map<String, Map<String, String>>) obj;
        }
        if (!companyUserSingleSet.isEmpty() && companyUserSingleSet.get(user.get("ID")) != null) {
            Map<String, String> temp = companyUserSingleSet.get(user.get("ID"));
            user.put("setid", user.get("ID"));
            user.put("ISUPREMIND", temp.get("ISUPREMIND"));
            user.put("ISDOWREMIND", temp.get("ISDOWREMIND"));
            user.put("isOpenSpace", temp.get("isOpenSpace"));
            user.put("isOpenGroup", temp.get("isOpenGroup"));
            user.put("isEnterSend", temp.get("isEnterSend"));
        } else {
            user.put("setid", "0");
            user.put("ISUPREMIND", "1");
            user.put("ISDOWREMIND", "1");
            user.put("isOpenSpace", "1");
            user.put("isOpenGroup", "1");
            user.put("isEnterSend", "1");
        }

        HashMap<String, List<String>> utc = new HashMap<String, List<String>>();
        HashMap<String, String> companyAdmin = new HashMap<String, String>();
        HashMap<String, Set<String>> userClassId = new HashMap<String, Set<String>>();
        HashMap<String, List<HashMap<String, String>>> companyUserIsStatus = new HashMap<String, List<HashMap<String, String>>>();
        obj = CacheUtils.get(this.getCompLocator(), "ofuserCompanys");
        if (null != obj) {
            utc = (HashMap<String, List<String>>) obj;
        }
       
        obj = CacheUtils.get(this.getCompLocator(), "ofcompanyAdmin");
        if (null != obj) {
            companyAdmin = (HashMap<String, String>) obj;
        }
        obj = CacheUtils.get(this.getCompLocator(), "ofuserClassId");
        if (null != obj) {
            userClassId = (HashMap<String, Set<String>>) obj;
        }
        obj = CacheUtils.get(this.getCompLocator(), "ofcompanyUserIsStatus");
        
        if(null != obj){
        	companyUserIsStatus = ( HashMap<String, List<HashMap<String, String>>>)obj;
        }
        
        if (user.get("ID").equals(companyAdmin.get(companyId))) {
            user.put("ISADMIN", "1");
        } else {
            user.put("ISADMIN", "0");  
        }
        user.put("userCompanySize", utc.get(user.get("ID")).size() + "");
       
        List<HashMap<String, String>> userCompanys = new ArrayList<HashMap<String, String>>();
        String ID = user.get("ID");
        for (String cid : utc.get(ID)) {
        	HashMap<String, String> item = new HashMap<String, String>();
            item.put("companyid", cid);
            item.put("companyName", companyInfo.get(cid).get("NAME"));
            Set<String> ids = userClassId.get(cid);
            if(null != ids && ids.size()>0){
	            if(ids.contains(ID)){
//	            	if(id.contains(user.get("ID"))){
	            		item.put("companyClassId", ID == "" ? "0": ID);
//	            	}
	            }
            }
            userCompanys.add(item);
        }
        
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("user", user);
        returnMap.put("userCompanys", userCompanys);
        return returnMap;
    }

    /** 将当前用户之前的系统登出 **/
    private void terminateSession(String companyId, String username,
            Map<String, Map<String, String>> companyUserInitInfo) {
        String tempusername = username.replace("@", "\\40");
        try {
            BroadcastUtils.broadcast(tempusername, tempusername + "@"
                    + BroadcastUtils.openfireServerName, "broadcast-offline:"
                    + username,
                    cn.flying.rest.onlinefile.utils.Message.Type.GROUP_CHAT,
                    companyUserInitInfo.get(username).get("FULLNAME"));
            BroadcastUtils.broadcast(username, "company" + companyId
                    + "@broadcast." + BroadcastUtils.openfireServerName,
                    "broadcast-online:" + username,
                    cn.flying.rest.onlinefile.utils.Message.Type.GROUP_CHAT,
                    companyUserInitInfo.get(username).get("FULLNAME"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public void setValidateCode(HttpServletRequest request,
            HttpServletResponse response) {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        Map<String, Object> result = new HashMap<String, Object>();
        // 先验证有没有这个用户，没有就不发邮件了，直接告诉
        Map<String, Object> checkMap = new HashMap<String, Object>();
        checkMap.put("username", username);
        checkMap.put("email", email);
        Map<String, Object> checkUser = userDao
                .checkUserExistByEmailAndUserName(checkMap);
        if (checkUser.isEmpty()) {
            // 没有用户 返回
            result.put("success", "false");
            result.put("msg", "1");

        } else {

            String subject = "欢迎来到东方飞扬在线分享系统";
            String checkCode = "";
            while (checkCode.length() < 6) {
                checkCode += (int) (Math.random() * 10);
            }
            // 存到对应用户中
            if (userDao.saveCheckCode(checkUser.get("id").toString(),
                    System.currentTimeMillis() + "", checkCode)) {
                String content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
                        + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
                        + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
                        + "<p style=\"font-size: 18px; line-height: 24px;\">用户名为：<strong>"
                        + username
                        + "</strong> 的用户本次找回密码的验证码为:</p>"
                        + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
                        + "<span style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">"
                        + checkCode
                        + "</span>"
                        + "</p>"
                        + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
                        + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
                        + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
                        + "祝好运!<br/>"
                        + "飞扬运维团队<br/>"
                        + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
                        + "</div>"
                        + "</p>"
                        + "</div></td></tr></tbody></table>";

                sendHtmlMail(email, subject, content, true);
                result.put("success", "true");
            } else {
                result.put("success", "false");
                result.put("msg", "2");
            }
        }

        String callback = request.getParameter("callback");
        writeJson(response, callback, new Gson().toJson(result));

    }

    public Map<String, String> resetPassword(Map<String, String> map) {
        String username = map.get("username");
        String email = map.get("email");
        String checkCode = map.get("checkCode");
        String newpassword = map.get("newpassword");
        long validatetime = System.currentTimeMillis();
        Map<String, Object> checkMap = new HashMap<String, Object>();
        checkMap.put("username", username);
        checkMap.put("email", email);
        Map<String, Object> checkUser = userDao
                .checkUserExistByEmailAndUserName(checkMap);
        Map<String, String> result = new HashMap<String, String>();
        if (checkUser.isEmpty()) {
            result.put("success", "false");
            result.put("msg", "1");
        } else {
            String checkTime = checkUser.get("checkTime").toString();
            String dbcheckCode = checkUser.get("checkCode").toString();
            if (checkCode.equals(dbcheckCode)) {
                long usedtime = Math.abs(validatetime
                        - Long.parseLong(checkTime));
                if (usedtime < 180000) {// 三分钟之内
                    // 更新数据库
                    Md5PasswordEncoder md5 = new Md5PasswordEncoder();
                    String password = md5.encodePassword(newpassword, username);
                    if (userDao.modifyPassword(password, username)) {
                        result.put("success", "true");
                    } else {
                        result.put("success", "false");
                        result.put("msg", "4");
                    }
                } else {
                    result.put("success", "false");
                    result.put("msg", "3");
                }
            } else {
                result.put("success", "false");
                result.put("msg", "2");
            }
        }
        return result;
    }
    
    /**
     * liuwei 20160119 分类邀请逻辑修改为现在的逻辑
     */
    public Map<String, Object> addUser(Map<String, String> params) {
    	Map<String,Object> datas = new HashMap<>();
        int divNumStr = Integer.parseInt(params.get("divNum"));
        String message = params.get("message");
        String companyId = params.get("companyid");
        int iCompanyId = Integer.parseInt(companyId);
        String groupId = params.get("groupId");
        String classId = params.get("classId");
        String groupflag = params.get("groupflag");
        String userFlag_ ="";
        String subject = "欢迎来到东方飞扬在线分享系统";
        // 新用户
        List<String> newUsers = new ArrayList<String>();
        // 系统用户,但不是当前团队用户
        List<Map<String, Object>> systemUsers = new ArrayList<Map<String, Object>>(); 
        // 团队用户,但不是当前分类用户
        List<Map<String, String>> companyUsers = new ArrayList<Map<String, String>>(); 
        // 当前分类用户
        List<Map<String, String>> classUsers = new ArrayList<Map<String, String>>();
        
        if (message == null)
            message = "";
        if ("".equals(message.trim())) {
        	message = "Onlinefile是一款简单好用的组织知识分享系统，专门为组织内部及时沟通分享而打造，使用它可以方便的进行组织内部文档分享交流，提高团队工作效率，团队成员之间无缝沟通，大家赶快加入吧。";
        }
       
        // 查到当前用户属于哪个公司的id和名字
        String companyFullName = userDao.getCompanyNameById(companyId);
        HashMap<Integer, String> emailcontents = new HashMap<Integer, String>();
        HashMap<Integer, String> emails = new HashMap<Integer, String>();
       
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Set<String> userIds = new HashSet<>();
        Object obj = CacheUtils.get(this.getCompLocator(),"companyUserInitInfo" + companyId);
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        } else {
            companyUserInitInfo = userDao.getUserInitInfo(companyId);
        }
        
        List<String> groupHasdUsers = new ArrayList<String>();
        // groupId为空 即是用户管理邀请用户 不为空是分类成员用户邀请
        if (null != groupId) { 
            groupHasdUsers = getChatWS().getGroupUsersByGroupId(groupId);
        }
        
        for (int i = 1; i <= divNumStr; i++) {
        	String emailParam = params.get("email" + i);
            if(null == emailParam || "".equals (emailParam)){
            	continue;
            }
            Map<String, String> param = new HashMap<String, String>();
            param.put("email", emailParam);
            param.put("code", MD5(emailParam));
            param.put("companyid", companyId);
            param.put("classId", classId);
            //xiewenda 根据邮箱判断用户的状态
            //HashMap<String, Object> retMap = userDao.verifyMailboxForClass(companyId, emailParam, classId);
            HashMap<String, Object> retMap = userDao.verifyMailbox(companyId, emailParam);
            String inviteState = retMap.get("state")+"";
            Map<String, Object> sysUser = userDao.getUserInfoByUserName(emailParam);
            Integer sysUserState = null;
            if(sysUser.size()>0){
               sysUserState=Integer.parseInt(sysUser.get("STATUS")+"");
            }
            //获取用户信息
        	//Map<String, Object> map = userDao.getUserInfoByUserName(emailParam);
        	
        	if(sysUserState!=null){
            	//已经存在用户的用户id
            	String userId = (String) sysUser.get("ID");
            	/*//获取company_users表中的所有信息根据用户id和公司id
            	Map<String,String> maps = userDao.getCompanyUserInfoByUserIdAndCompanyId(userId,companyId);*/
            	//说明用户还没有激活此时不能算是系统用户继续发送邮件
            	if(sysUserState<0){
            	  if("newCompanyInvite".equals(inviteState)){
            	      userDao.saveCompanyUserTableForClass(companyId, classId, userId);
            	  // 不同公司之间邀请 往invitedetail(邀请详细表)中插入邀请详细信息
            	  }
            	  if (!userDao.checkInvitedetailIsHave(companyId, userId,groupId, classId, groupflag)) {
            	      userDao.saveInviteDetail(companyId, userId,groupId, classId, groupflag);
            	  }
            	 newUsers.add(emailParam);
       			 Map<String, String> classMap = getDocumentClassWS().getCateById(params);
           			 String activateURL = "";
                        String content = "";
                        try {
                            activateURL = BroadcastUtils.http_schema
                                    + "//"
                                    + params.get("ipandport")
                                    + "/onlinefile/0/default/ESUserInfo/toActive?id="
                                    + userId + "&companyid=" + companyId
                                    + "&code=" + MD5(emailParam) + "&email=" + emailParam
                                    + "&sendUserName="+params.get("username")
                                    + "&sendTime="+new Date().getTime();
                            if (classId != null && !"".equals(classId)) {
                                activateURL += "&className="
                                        + URLEncoder.encode(classMap.get("CLASSNAME"), "UTF-8")
                                        + "&classId=" + classId;
                            } else {
                                activateURL += "&className=" + "" + "&classId="
                                        + "";
                            }
                            content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
                                    + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
                                    + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
                                    + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
                                    + ""
                                    + "</strong>邀请您使用Onlinefile，点击下面的绿色按钮加入<strong>"
                                    + (classMap.get("CLASSNAME") != null
                                            && !"".equals(classId) ? classMap
                                            .get("CLASSNAME") : companyFullName)
                                    + "</strong>团队:</p>"
                                    + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
                                    + "<a href=\""
                                    + activateURL
                                    + "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">立即激活</a>"
                                    + "</p>"
                                    + "<p style=\"line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; word-break: break-word;\">如果按钮无法点击，请复制链接至浏览器打开:<a>"
                                    + activateURL
                                    + "</a></p>"
                                    + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
                                    + "<p style=\"font-size: 18px; line-height: 24px;text-align: center;\"><strong>"
                                    + message
                                    + "</p>"
                                    + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
                                    + "<p style=\"font-size: 0.9rem; line-height: 20px; color: rgb(113, 113, 113); text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">-请在收取邮件后7天内激活-</p>"
                                    + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
                                    + "祝好运!<br>"
                                    + "--"
                                    + companyUserInitInfo.get(params.get("ID"))
                                            .get("FULLNAME")
                                    + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
                                    + "</div>"
                                    + "</p>"
                                    + "</div></td></tr></tbody></table>";
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            logger.error(e.getMessage());
                        }
                        emails.put(Integer.parseInt(userId), emailParam);
                        emailcontents.put(Integer.parseInt(userId), content);
	             //不是公司用户 或公司内未激活
            	}else if("newCompanyInvite".equals(inviteState)||"nonactive".equals(inviteState)){
            	          if("newCompanyInvite".equals(inviteState)) userDao.saveCompanyUserTableForClass(companyId, classId, userId);
            	           // 不同公司之间邀请 往invitedetail(邀请详细表)中插入邀请详细信息
                            if (!userDao.checkInvitedetailIsHave(companyId, userId,groupId, classId, groupflag)) {
                                userDao.saveInviteDetail(companyId, userId,groupId, classId, groupflag);
                            }
            			// 添加用户分类缓冲
                        Map<String,Set<String>> companyUserClassId = null;
                        Object obj1 = CacheUtils.get(this.getCompLocator(), "ofuserClassId");
                        if (obj1 != null) {
                            userIds.add(userId);
                            companyUserClassId = (Map<String, Set<String>>) obj1;
                            companyUserClassId.put(companyId,userIds);
                            CacheUtils.set(this.getCompLocator(), "ofuserClassId", companyUserClassId);
                        }
                        // 添加用户公司缓冲
                        obj1 = CacheUtils.get(this.getCompLocator(),"ofuserCompanys");
                        HashMap<String, List<String>> utc = new HashMap<String, List<String>>();
                        if (null != obj1) {
                            utc = (HashMap<String, List<String>>) obj1;
                            /** lujixiang 20151104  添加重复公司id判断   **/
                            if (null == utc.get(userId) ) {
                                utc.put(userId, new ArrayList<String>());
                            }
                            if (!utc.get(userId).contains(companyId)) {
                            	 utc.get(userId).add(companyId);
                                CacheUtils.set(this.getCompLocator(), "ofuserCompanys", utc);
                            }
                        }
                       
                        systemUsers.add(sysUser);
                		 // 查出邀请人发出的邀请分类
                		Map<String, String> classMap = getDocumentClassWS().getCateById(params);
                        String activateURL = "";
                        String content = "";
                        activateURL = BroadcastUtils.http_schema
           				        + "//"
           				        + params.get("ipandport");
           				
           				content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
           				        + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
           				        + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
           				        + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
           				        + ""
           				        + "</strong>您有新的来自 "
           				        + companyUserInitInfo.get(params.get("ID"))
           		                .get("FULLNAME")
           		                		+ " 的邀请，请点击下面的绿色按钮进入登录页</p>"
           				        + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
           				        + "<a href=\""
           				        + activateURL
           				        + "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">进入登录页</a>"
           				        + "</p>"
           				        + "<p style=\"line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; word-break: break-word;\">如果按钮无法点击，请复制链接至浏览器打开:<a>"
           				        + activateURL
           				        + "</a></p>"
           				        + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
           				        + "<p style=\"font-size: 18px; line-height: 24px;text-align: center;\"><strong>"
           				        + "</p>" 
           				        + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
           				        + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
           				        + "祝好运!<br>"
           				        + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
           				        + "</div>"
           				        + "</p>"
           				        + "</div></td></tr></tbody></table>";
           				  userFlag_ = "haveTheUser";
                          emails.put(Integer.parseInt(userId), emailParam);
                          emailcontents.put(Integer.parseInt(userId), content);	
            		
            	}else {
              		  Map<String, String> toAddUser = companyUserInitInfo.get(emailParam);
              		  if (toAddUser != null && !toAddUser.isEmpty()) { // 在公司(团队)内
                            if (groupHasdUsers.contains(toAddUser.get("ID"))) { // 分类内成员
                                classUsers.add(toAddUser);
                            } else {
                                // 添加分类外成员到分类内
                                Map<String, String> data = new HashMap<String, String>();
                                data.put("userId", toAddUser.get("ID"));
                                data.put("groupId", groupId);
                                data.put("companyId", companyId);
                                data.put("groupflag", params.get("groupflag"));
                                data.put("username", params.get("username"));
                                data.put("fullname", params.get("fullname"));
                                getChatWS().addUserIntoGroup(data);
                                companyUsers.add(toAddUser);
                            }
                        }
              		  
              		 // 查出邀请人发出的邀请分类
                      Map<String, String> classMap = getDocumentClassWS().getCateById(params);
                      String activateURL = "";
                      String content = "";
                      activateURL = BroadcastUtils.http_schema
                              + "//"
                              + params.get("ipandport");
                      
                      content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
                              + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
                              + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
                              + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
                              + ""
                              + "</strong>您有新的来自 "
                              + companyUserInitInfo.get(params.get("ID"))
                              .get("FULLNAME")
                                      + " 的邀请，请点击下面的绿色按钮进入登录页</p>"
                              + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
                              + "<a href=\""
                              + activateURL
                              + "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">进入登录页</a>"
                              + "</p>"
                              + "<p style=\"line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; word-break: break-word;\">如果按钮无法点击，请复制链接至浏览器打开:<a>"
                              + activateURL
                              + "</a></p>"
                              + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
                              + "<p style=\"font-size: 18px; line-height: 24px;text-align: center;\"><strong>"
                              + "</p>" 
                              + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
                              + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
                              + "祝好运!<br>"
                              + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
                              + "</div>"
                              + "</p>"
                              + "</div></td></tr></tbody></table>";
                        userFlag_ = "haveTheUser";
                        emails.put(Integer.parseInt(userId), emailParam);
                        emailcontents.put(Integer.parseInt(userId), content);
            //公司下邀请或分类下邀请未激活  		  
        	}
        }else{
        	   //不是系统用户
        	String userid = String.valueOf(userDao.addClassUser(param));
        	if(userDao.saveCompanyUserTableForClass(companyId, classId, userid)){
    			newUsers.add(emailParam);
    			 Map<String, String> classMap = getDocumentClassWS().getCateById(params);
        			 String activateURL = "";
                     String content = "";
                     try {
                         activateURL = BroadcastUtils.http_schema
                                 + "//"
                                 + params.get("ipandport")
                                 + "/onlinefile/0/default/ESUserInfo/toActive?id="
                                 + userid + "&companyid=" + iCompanyId
                                 + "&code=" + MD5(emailParam) + "&email=" + emailParam
                                 + "&sendUserName="+params.get("username")
                                 + "&sendTime="+new Date().getTime();
                         if (classId != null && !"".equals(classId)) {
                             activateURL += "&className="
                                     + URLEncoder.encode(classMap.get("CLASSNAME"), "UTF-8")
                                     + "&classId=" + classId;
                         } else {
                             activateURL += "&className=" + "" + "&classId="
                                     + "";
                         }
                         content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
                                 + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
                                 + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
                                 + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
                                 + ""
                                 + "</strong>邀请您使用Onlinefile，点击下面的绿色按钮加入<strong>"
                                 + (classMap.get("CLASSNAME") != null
                                         && !"".equals(classId) ? classMap
                                         .get("CLASSNAME") : companyFullName)
                                 + "</strong>团队:</p>"
                                 + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
                                 + "<a href=\""
                                 + activateURL
                                 + "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">立即激活</a>"
                                 + "</p>"
                                 + "<p style=\"line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; word-break: break-word;\">如果按钮无法点击，请复制链接至浏览器打开:<a>"
                                 + activateURL
                                 + "</a></p>"
                                 + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
                                 + "<p style=\"font-size: 18px; line-height: 24px;text-align: center;\"><strong>"
                                 + message
                                 + "</p>"
                                 + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
                                 + "<p style=\"font-size: 0.9rem; line-height: 20px; color: rgb(113, 113, 113); text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">-请在收取邮件后7天内激活-</p>"
                                 + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
                                 + "祝好运!<br>"
                                 + "--"
                                 + companyUserInitInfo.get(params.get("ID"))
                                         .get("FULLNAME")
                                 + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
                                 + "</div>"
                                 + "</p>"
                                 + "</div></td></tr></tbody></table>";
                     } catch (UnsupportedEncodingException e) {
                         e.printStackTrace();
                         logger.error(e.getMessage());
                     }
                     if (!userDao.checkInvitedetailIsHave(companyId, userid,groupId, classId, groupflag)) {
                          userDao.saveInviteDetail(companyId, userid,groupId, classId, groupflag);
                     }
                     emails.put(Integer.parseInt(userid), emailParam);
                     emailcontents.put(Integer.parseInt(userid), content);
                     // 日志------------------------------------------
                     HashMap<String, String> log = new HashMap<String, String>();
                     log.put("userid", params.get("ID"));
                     log.put("username", params.get("ID"));
                     log.put("ip", params.get("ip"));
                     log.put("module", "添加用户");
                     // login 用户登录 access 功能访问 operation 功能操作
                     log.put("type", "operation");
                     log.put("operate", "添加用户");
                     log.put("companyName", companyFullName);
                     log.put("loginfo", "添加用户,邮箱为【" + emailParam + "】");
                     logutils.saveBaseLog(this.getCompLocator(), log);
                     // 日志------------------------------------------
        	}
        }
     }
        
        
       if (!emails.isEmpty()) {
          /** 调用线程进行邮件发送 **/
          ThreadPoolManager m = ThreadPoolManager.getInstance();
          try {
              m.runTaskforFree(new sendMailCallable(companyId, emails,
                      emailcontents, subject, true,!"haveTheUser".equals(userFlag_)));
          } catch (InterruptedException e) {
              e.printStackTrace();
              logger.error(e.getMessage());
          } catch (ExecutionException e) {
              e.printStackTrace();
              logger.error(e.getMessage());
         }
      }
      if (!newUsers.isEmpty()) {
          datas.put("newUsers", newUsers);
      }
      if (!systemUsers.isEmpty()) {
    	  datas.put("systemUsers", systemUsers);
      }
      if (!companyUsers.isEmpty()) {
    	  datas.put("companyUsers", companyUsers);
      }
      if (!classUsers.isEmpty()) {
    	  datas.put("classUsers", classUsers);
      }
    		return datas;
    }
    
    //liuwei20160123用户管理下邀请逻辑之前的逻辑注释掉了
    public Map<String, Object> inviteAddUser(Map<String, String> params) {
        String divNumStr = params.get("divNum");
        String message = params.get("message");
        String companyId = params.get("companyid");
        Map<String, Object> temp = new HashMap<String, Object>();
        List<String> nonactiveUsers = new ArrayList<String>();
        List<String> informUsers = new ArrayList<String>();// 发送通知消息
        // 拿到每个动态增加的用户的后戳
        Integer divNum = Integer.valueOf(divNumStr);
        if (message == null)
            message = "";
        if ("".equals(message.trim())) {
            message = "Onlinefile是一款简单好用的组织知识分享系统，专门为组织内部及时沟通分享而打造，使用它可以方便的进行组织内部文档分享交流，提高团队工作效率，团队成员之间无缝沟通，大家赶快加入吧。";
        }
        // 通过ID查询当前企业名称
        String companyName = userDao.getCompanyNameById(companyId);
        String subject = "欢迎来到东方飞扬在线分享系统";
        HashMap<Integer, String> emailcontents = new HashMap<Integer, String>();
        HashMap<Integer, String> emails = new HashMap<Integer, String>();
        Set<String> userIds = new HashSet<>();
        for (int i = 1; i <= divNum; i++) {
        	String emailParam = params.get("email" + i);
            String isSendFlg = "";
            String divid = String.valueOf(i);
            if (null == emailParam || "".equals(emailParam)){
                continue;
            }
            Map<String, String> param = new HashMap<String, String>();
            String code = MD5(emailParam);
            param.put("email", emailParam);
            param.put("code", code);
            param.put("companyid", companyId);
            //xiewenda 根据邮箱判断用户的状态
            HashMap<String, Object> retMap = userDao.verifyMailbox(companyId,emailParam);
            String inviteState = retMap.get("state")+"";
            Map<String, Object> sysUser = userDao.getUserInfoByUserName(emailParam);
            Integer sysUserState = null;
            if(sysUser.size()>0){
               sysUserState=Integer.parseInt(sysUser.get("STATUS")+"");
            }
            //判断用户是否存在于系统中
            if(sysUserState!=null&&sysUserState>-1){
              String userId = (String) sysUser.get("ID");
                if("newCompanyInvite".equals(inviteState)||"nonactive".equals(inviteState)){
//            	//查询该公司是否存在
//            	Map<String, String> companyUserTable = userDao.existCompanyForCompanyUserTable(userId,companyId);
//              if(companyUserTable.isEmpty()){
                  if("newCompanyInvite".equals(inviteState)){
            		userDao.saveCompanyUserTableForCompany(companyId, userId);
                    }
            		isSendFlg = "sendMessage";
                    informUsers.add(emailParam);
                    Map<String, Set<String>> companyUserClassId = null;
                    Object obj = CacheUtils.get(this.getCompLocator(), "ofuserClassId");
                    if (obj != null) {
                    	userIds.add(userId);
                        companyUserClassId = (Map<String, Set<String>>) obj;
                        companyUserClassId.put(companyId,userIds);
                        CacheUtils.set(this.getCompLocator(),"ofuserClassId",companyUserClassId);
                    }
                    obj = CacheUtils.get(this.getCompLocator(), "ofuserCompanys");
                    HashMap<String, List<String>> utc = new HashMap<String, List<String>>();
                    if (null != obj) {
                        utc = (HashMap<String, List<String>>) obj;
                        if ( null == utc.get(userId)) {
                            utc.put(userId + "", new ArrayList<String>());
                        }
                        /** lujixiang 20151104 添加重复数据判断  **/
                        if (!utc.get(userId).contains(companyId)) {
                        	utc.get(userId).add(companyId);
                            CacheUtils.set(this.getCompLocator(), "ofuserCompanys", utc);
						}
                    }
               
//            	}
               String activateURL = "";
               String content = "";
               activateURL = BroadcastUtils.http_schema
                       + "//"
                       + params.get("ipandport");
               
               content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
                       + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
                       + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
                       + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
                       + ""
                       + "</strong>您有新的来自 "
                       + companyName
                        + " 的邀请，请点击下面的绿色按钮进入登录页</p>"
                       + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
                       + "<a href=\""
                       + activateURL
                       + "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">进入登录页</a>"
                       + "</p>"
                       + "<p style=\"line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; word-break: break-word;\">如果按钮无法点击，请复制链接至浏览器打开:<a>"
                       + activateURL
                       + "</a></p>"
                       + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
                       + "<p style=\"font-size: 18px; line-height: 24px;text-align: center;\"><strong>"
                       + "</p>" 
                       + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
                       + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
                       + "祝好运!<br>"
                       + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
                       + "</div>"
                       + "</p>"
                       + "</div></td></tr></tbody></table>";
                 //userFlag_ = "haveTheUser";
                 emails.put(Integer.parseInt(userId), emailParam);
                 emailcontents.put(Integer.parseInt(userId), content); 
                }
            }else { //不是系统用户
                    String userId = sysUser.get("ID")+"";
                    if(sysUserState==null){
	            	   userId = String.valueOf(userDao.addCompanyUser(param));
	            	   userDao.saveCompanyUserTableForCompany(companyId, userId);
                    }
                    if("newCompanyInvite".equals(inviteState)){
                      userDao.saveCompanyUserTableForCompany(companyId, userId);
                    }else{
                      nonactiveUsers.add(emailParam);
                    }
                    
		            	String activateURL = "";
		                String content = "";
		                isSendFlg = "sendEmail";
		                activateURL = BroadcastUtils.http_schema + "//"
		                        + params.get("ipandport")
		                        + "/onlinefile/0/default/ESUserInfo/toActive?id="
		                        + userId + "&companyid=" + companyId + "&code=" + code
		                        + "&email=" + emailParam
		                        + "&sendUserName="+""
		                        + "&sendTime="+new Date().getTime();
		                activateURL += "&className=" + "" + "&classId=" + "";
		                content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
		                        + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
		                        + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
		                        + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
		                        + "" + "</strong>邀请您使用Onlinefile，点击下面的绿色按钮加入<strong>"
		                        + companyName
		                        + "</strong>团队:</p>"
		                        + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
		                        + "<a href=\""
		                        + activateURL
		                        + "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">立即激活</a>"
		                        + "</p>"
		                        + "<p style=\"line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; word-break: break-word;\">如果按钮无法点击，请复制链接至浏览器打开:<a>"
		                        + activateURL
		                        + "</a></p>"
		                        + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
		                        + "<p style=\"font-size: 18px; line-height: 24px;text-align: center;\"><strong>"
		                        + message
		                        + "</p>"
		                        + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
		                        + "<p style=\"font-size: 0.9rem; line-height: 20px; color: rgb(113, 113, 113); text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">-请在收取邮件后7天内激活-</p>"
		                        + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
		                        + "祝好运!<br>"
		                        + "--"
		                        + companyName
		                        + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
		                        + "</div>"
		                        + "</p>"
		                        + "</div></td></tr></tbody></table>";
		                emails.put(Integer.parseInt(userId), emailParam);
		                emailcontents.put(Integer.parseInt(userId), content);
            }
          
            if (!"".equals(isSendFlg)) {
                // 日志------------------------------------------
                HashMap<String, String> log = new HashMap<String, String>();
                log.put("userid", params.get("ID"));
                log.put("username", params.get("ID"));
                log.put("ip", params.get("ip"));
                log.put("module", "添加用户");
                log.put("type", "operation");
                log.put("operate", "添加用户");
                log.put("companyName", companyName);
                log.put("loginfo", "添加用户,邮箱为【" + emailParam + "】");
                logutils.saveBaseLog(this.getCompLocator(), log);
                // 日志------------------------------------------
            }
        }
        if (!emails.isEmpty()) {
            /** 调用线程进行邮件发送 **/
            ThreadPoolManager m = ThreadPoolManager.getInstance();
            try {
                m.runTaskforFree(new sendMailCallable(companyId, emails, emailcontents, subject, true, true));
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        if (!nonactiveUsers.isEmpty()) {
            temp.put("nonactiveUsers", nonactiveUsers);
        }
        if (!informUsers.isEmpty()) {
            temp.put("informUsers", informUsers);
        }
        return temp;
    }
   

    class sendMailCallable implements Callable<Boolean> {
        HashMap<Integer, String> emails;
        HashMap<Integer, String> emailcontents;
        String companyId; // 添加企业id，区分该次邮件邀请的企业, 可为null或字符串
        String subject;
        boolean flag;
        boolean type;// 是否修改状态

        /**
         * 发送邮件
         * 
         * @param companyId
         *            修改状态type为true才起效
         * @param emails
         * @param emailcontents
         * @param subject
         * @param flag
         * @param type
         *            是否修改状态
         */
        sendMailCallable(String companyId, HashMap<Integer, String> emails,
                HashMap<Integer, String> emailcontents, String subject,
                boolean flag, boolean type) {
            this.companyId = companyId;
            this.emails = emails;
            this.emailcontents = emailcontents;
            this.subject = subject;
            this.flag = flag;
            this.type = type;
        }

        public Boolean call() throws Exception {
            Iterator<Entry<Integer, String>> iterator = emails.entrySet()
                    .iterator();
            Entry<Integer, String> entry = null;
            List<Integer> okids = new ArrayList<Integer>();
            List<Integer> errorids = new ArrayList<Integer>();
            while (iterator.hasNext()) {
                entry = iterator.next();
                boolean isOk = sendHtmlMail(entry.getValue(), subject,
                        emailcontents.get(entry.getKey()), true);
                if (isOk) {
                    okids.add(entry.getKey());
                } else {
                    errorids.add(entry.getKey());
                }
            }
//            if (type) {
//                if (!okids.isEmpty()) {
//                    //userDao.updateStatus(okids, -1);
//                    if (companyId != null && !"".equals(companyId)) {
//                        userDao.updateCompanyUserStatus(companyId, okids, -1);
//                    }
//                }
//                if (!errorids.isEmpty()) {
//                   // userDao.updateStatus(errorids, -2);
//                    if (companyId != null && !"".equals(companyId)) {
//                        userDao.updateCompanyUserStatus(companyId, errorids, -2);
//                    }
//                }
//            }
            return true;
        }
    }

    /*------------------------邮件--------------------------------*/
    public String getMailServerHost() {
        return mailServerHost;
    }

    @Value("${flyingSoft.email.serverhost}")
    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    @Value("${flyingSoft.email.serverport}")
    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    @Value("${flyingSoft.email.address}")
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getUserName() {
        return userName;
    }

    @Value("${flyingSoft.email.username}")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    @Value("${flyingSoft.email.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    /** */
    /**
     * 获得邮件会话属性
     */
    public Properties getProperties(boolean validate) {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.getMailServerHost());
        p.put("mail.smtp.port", this.getMailServerPort());
        p.put("mail.smtp.auth", validate ? "true" : "false");
        // longjunhao 20150129 add
        p.put("mail.smtp.starttls.enable", "true");
        return p;
    }

    /**
     * 以文本格式发送邮件
     * 
     * @param mailInfo
     *            待发送的邮件的信息
     */
    public boolean sendTextMail(String toAddress, String subject,
            String content, boolean validate) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = this.getProperties(validate);
        if (validate) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(this.getUserName(),
                    this.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session
                .getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(this.fromAddress);
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(toAddress);
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(subject);
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = content;
            mailMessage.setText(mailContent);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }
        return false;
    }

    /** */
    /**
     * 以HTML格式发送邮件
     * 
     * @param mailInfo
     *            待发送的邮件信息
     */
    public boolean sendHtmlMail(String toAddress, String subject,
            String content, boolean validate) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = this.getProperties(validate);
        // 如果需要身份认证，则创建一个密码验证器
        if (validate) {
            authenticator = new MyAuthenticator(this.userName, this.password);
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session
                .getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(this.fromAddress);
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(toAddress);
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(content, "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return false;
    }

    /*------------------------邮件--------------------------------*/
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> singleSetUser(Map<String, String> params) {
       // Map<String, Object> reutrnMap = new HashMap<String, Object>();
        //String isupdate = params.get("isupdate");
        Map<String, String> objMap = new HashMap<String, String>();
        boolean isOk = false;
        Map<String, String> obMap = userDao.getSingleSet(params);
        // if(objMap.size() == 0){
        // if("0".equals(isupdate)){//默认用户的个性设置都设置为1.
        if (obMap != null && obMap.size() > 0) {// 默认用户的个性设置都设置为1.
            isOk = userDao.updateSingleSet(params);
        } else {
            isOk = userDao.addSingleSet(params);
        }
        
        objMap.put("success", isOk+"");
        Object obj =null;
        if(isOk){
	        objMap.put("ISUPREMIND", params.get("isUpRemind"));
	        objMap.put("ISDOWREMIND", params.get("isDownRemind"));
	        objMap.put("isOpenSpace", params.get("isOpenSpace")!=null?params.get("isOpenSpace"):"1");
	        objMap.put("isOpenGroup", params.get("isOpenGroup")!=null?params.get("isOpenGroup"):"1");
	        objMap.put("isEnterSend", params.get("isEnterSend")!=null?params.get("isEnterSend"):"1");
	        Map<String, Map<String, String>> companyUserSingleSet = null;
	        obj = CacheUtils.get(this.getCompLocator(),"companyUserSingleSet" + params.get("companyId"));
	        if (obj != null && obj.toString() != "") {
	            companyUserSingleSet = (Map<String, Map<String, String>>) obj;
	            companyUserSingleSet.put(params.get("userid"), objMap);
	            CacheUtils.set(this.getCompLocator(), "companyUserSingleSet"+ params.get("companyId"), companyUserSingleSet);
	        } else {
	            companyUserSingleSet = new HashMap<String, Map<String, String>>();
	            companyUserSingleSet.put(params.get("userid"), objMap);
	            CacheUtils.set(this.getCompLocator(), "companyUserSingleSet"+ params.get("companyId"), companyUserSingleSet);
	        }
	        //reutrnMap.put("isupremind",companyUserSingleSet.get(params.get("userid")).get("ISUPREMIND"));
	        //reutrnMap.put("isdownremind",companyUserSingleSet.get(params.get("userid")).get("ISDOWREMIND"));
        }
	        // 日志------------------------------------------
	        Map<String, Map<String, String>> companyUserInitInfo = null;
	        obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+ params.get("companyId"));
	        if (obj != null && obj.toString() != "") {
	            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
	        } else {
	            companyUserInitInfo = userDao.getUserInitInfo(params.get("companyId"));
	        }
	        HashMap<String, String> log = new HashMap<String, String>();
	        log.put("userid", params.get("id"));
	        log.put("username", params.get("id"));
	        log.put("ip", params.get("ip"));
	        log.put("module", "个性设置");
	        // login 用户登录 access 功能访问 operation 功能操作
	        log.put("type", "operation");
	        log.put("operate", "个性设置");
	        log.put("companyName",companyUserInitInfo.get(params.get("id")).get("COMPANYNAME"));
	        log.put("loginfo", "个性设置,用户名为【" + params.get("id") + "】");
	        logutils.saveBaseLog(this.getCompLocator(), log);
        return objMap;
    }
    
    /**
     * 文件评论回车设置
     * 
     */
    public Map<String,String> commentEnterSet(Map<String, String> params){
    	String companyId = params.get("companyId");
    	String userId = params.get("userid");
    	String isEnterSend = params.get("isEnterSend");
    	Map<String, String> obMap = userDao.getSingleSet(params);
    	boolean isSucess = false;
    	if(null != obMap && obMap.size()>0){
    		 isSucess = userDao.updateCommentEnterSet(params);
    	}else{
    		isSucess = userDao.addCommentEnterSet(params);
    	}
    	Map<String,String> maps = new HashMap<String,String>();
    	maps.put("isEnterSend", isEnterSend);
    	if(isSucess){
    		 Map<String, Map<String, String>> companyUserSingleSet = null;
    		 Object obj = null;
 	        obj = CacheUtils.get(this.getCompLocator(),"companyUserSingleSet" + companyId);
 	        if (obj != null) {
 	            companyUserSingleSet = (Map<String, Map<String, String>>) obj;
 	            companyUserSingleSet.put(params.get("userid"), maps);
 	            CacheUtils.set(this.getCompLocator(), "companyUserSingleSet"+ companyId, companyUserSingleSet);
 	        } else {
 	            companyUserSingleSet = new HashMap<String, Map<String, String>>();
 	            companyUserSingleSet.put(params.get("userid"), maps);
 	            CacheUtils.set(this.getCompLocator(), "companyUserSingleSet"+ companyId, companyUserSingleSet);
 	        }
    	}
    	maps.put("isSucess", String.valueOf(isSucess));
    	return maps;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getSingleSet(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        // Map<String, String> objMap=userDao.getSingleSet(params);
        // if(objMap.size() == 0){
        // userDao.addSingleSet(params);
        // objMap.put("ISUPREMIND", "0");
        // objMap.put("ISUPREMIND", "0");
        // }
        Map<String, Map<String, String>> companyUserSingleSet = null;
        Object obj = CacheUtils.get(this.getCompLocator(),"companyUserSingleSet" + params.get("companyId"));
        if (obj != null) {
            companyUserSingleSet = (Map<String, Map<String, String>>) obj;
            if (companyUserSingleSet.get(params.get("id")) == null) {
                companyUserSingleSet = userDao.getUserSingleSet(params.get("companyId"));
            }
        } else {
            companyUserSingleSet = userDao.getUserSingleSet(params.get("companyId"));
        }
        reutrnMap.put("isupremind", companyUserSingleSet.get(params.get("id")).get("ISUPREMIND"));
        reutrnMap.put("isdownremind", companyUserSingleSet.get(params.get("id")).get("ISDOWREMIND"));
        reutrnMap.put("isOpenSpace", companyUserSingleSet.get(params.get("id")).get("isOpenSpace"));
        reutrnMap.put("isOpenGroup", companyUserSingleSet.get(params.get("id")).get("isOpenGroup"));
        reutrnMap.put("isEnterSend", companyUserSingleSet.get(params.get("id")).get("isEnterSend"));
        // 日志------------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+ params.get("companyId"));
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        } else {
            companyUserInitInfo = userDao.getUserInitInfo(params.get("companyId"));
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("userid", params.get("id"));
        log.put("username", params.get("id"));
        log.put("ip", params.get("ip"));
        log.put("module", "个性设置");
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "operation");
        log.put("operate", "个性设置");
        log.put("companyName",
                companyUserInitInfo.get(params.get("id")).get("COMPANYNAME"));
        log.put("loginfo", "个性设置,用户名为【" + params.get("id") + "】");
        logutils.saveBaseLog(this.getCompLocator(), log);
        return reutrnMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> editUserInfo(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        String fullname = params.get("fullname");
        String FIRSTNAME = "";
        String LASTNAME = "";
        if (null != fullname && !"".equals(fullname)) {
            LASTNAME = fullname.substring(0, 1);
            if (fullname.length() > 1)
                FIRSTNAME = fullname.substring(1);
        }
        // 现在由于是登陆的soa的用户 所以用户id是admin
        if (params.get("id") != null) {

            if (userDao.editUserInfo(params)) {
            	 //20151120 xyc -1 表示企业不存在的用户进行修改，将给予默认企业名称：东方飞扬云平台
            	// 禁止用户不进行平台同步以下信息
            	if(params.get("companyId")!=null && !params.get("companyId").equals("-1")){
	                // 同步到平台
	                Map<String, Object> param = new HashMap<String, Object>();
	                Map<String, String> data = new HashMap<String, String>();
	                param.put("userId", params.get("username"));
	                param.put("instanceId", params.get("companyId"));
	                param.put("remoteAddr", params.get("ip"));
	                data.put("ID", params.get("id")); // 暂时写这个id 因为不知道对不对
	                data.put("USERID", params.get("username"));
	                data.put("FIRSTNAME", FIRSTNAME);
	                data.put("LASTNAME", LASTNAME);
	                data.put("MOBTEL", params.get("mobilephone"));
	                param.put("data", data);
	                Map<String, String> returnmap = getUserRegistrationServer()
	                        .editUserInfo(param);
	                if (!Boolean.parseBoolean(returnmap.get("success"))) {
	                    reutrnMap.put("success", "false");
	                    return reutrnMap;
	                }
	                // 同步到平台
	               
	                if (params.get("signature") != null) {
	                    Map<String, Map<String, String>> companyUserInitInfo = null;
	                    Object obj = CacheUtils.get(this.getCompLocator(),
	                            "companyUserInitInfo" + params.get("companyId"));
	                    if (obj != null) {
	                        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
	                        if (companyUserInitInfo.get(params.get("username")) != null) {
	                            companyUserInitInfo.get(params.get("username"))
	                                    .put("SIGNATURE", params.get("signature"));
	                            CacheUtils.set(
	                                    this.getCompLocator(),
	                                    "companyUserInitInfo"
	                                            + params.get("companyId"),
	                                    companyUserInitInfo);
	                        }
	                    }
	                    BroadcastUtils
	                            .broadcast(
	                                    params.get("username"),
	                                    "company" + params.get("companyId")
	                                            + "@broadcast."
	                                            + BroadcastUtils.openfireServerName,
	                                    params.get("username") + "broadcast-status"
	                                            + params.get("signature"),
	                                    cn.flying.rest.onlinefile.utils.Message.Type.GROUP_CHAT,
	                                    params.get("fullname"));
	                }
	                // 机构同步到openfire----------------------------------------
	                if (params.get("org") != null) {
	                    Map<String, Map<String, String>> companyUserInitInfo = null;
	                    Object obj = CacheUtils.get(this.getCompLocator(),
	                            "companyUserInitInfo" + params.get("companyId"));
	                    if (obj != null) {
	                        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
	                        if (companyUserInitInfo.get(params.get("username")) != null) {
	                            companyUserInitInfo.get(params.get("username"))
	                                    .put("ORGID", params.get("org"));
	                            CacheUtils.set(
	                                    this.getCompLocator(),
	                                    "companyUserInitInfo"
	                                            + params.get("companyId"),
	                                    companyUserInitInfo);
	                        }
	                    }
	                    BroadcastUtils
	                            .broadcast(
	                                    params.get("username"),
	                                    "company" + params.get("companyId")
	                                            + "@broadcast."
	                                            + BroadcastUtils.openfireServerName,
	                                    params.get("username") + "broadcast-status"
	                                            + params.get("org"),
	                                    cn.flying.rest.onlinefile.utils.Message.Type.GROUP_CHAT,
	                                    params.get("fullname"));
	                }
            	}
                // 机构同步到openfire----------------------------------------
                // 放入缓存----------------------------------
                Map<String, Map<String, String>> companyUserInitInfo = null;
                if(params.get("companyId")!=null && !params.get("companyId").equals("-1")){
	                Object obj = CacheUtils.get(this.getCompLocator(),
	                        "companyUserInitInfo" + params.get("companyId"));
	                if (obj != null) {
	                    companyUserInitInfo = (Map<String, Map<String, String>>) obj;
	                    if (companyUserInitInfo.get(params.get("username")) != null) {
	                        companyUserInitInfo.remove(params.get("username"));
	                    }
	                    companyUserInitInfo.put(params.get("username"),
	                            userDao.getOneUserInfo(params.get("id")));
	                    CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
	                            + params.get("companyId"), companyUserInitInfo);
	                } else {
	                    companyUserInitInfo = userDao.getUserInitInfo(params
	                            .get("companyId"));
	                    CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
	                            + params.get("companyId"), companyUserInitInfo);
	                }
                }
                // 放入缓存----------------------------------

                // 日志------------------------------------------
                HashMap<String, String> log = new HashMap<String, String>();
                log.put("userid", params.get("username"));
                log.put("username", params.get("username"));
                log.put("ip", params.get("ip"));
                log.put("module", "个人编辑");
                // login 用户登录 access 功能访问 operation 功能操作
                log.put("type", "operation");
                log.put("operate", "个人编辑");
                log.put("companyName",companyUserInitInfo!=null?companyUserInitInfo.get(params.get("username")).get("COMPANYNAME"):"东方飞扬云平台");
                log.put("loginfo", "个人编辑,用户名名为【" + params.get("username") + "】");
                logutils.saveBaseLog(this.getCompLocator(), log);
                // 日志------------------------------------------
                reutrnMap.put("success", "true");
            } else {
                reutrnMap.put("success", "false");
            }
        }
        return reutrnMap;
    }

    @Override
    public Map<String, Object> modifyPassword(Map<String, String> params) {
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        String oldPasswordFromInput = md5.encodePassword(
                params.get("oldPassword"), params.get("id"));
        String newPassword = md5.encodePassword(params.get("newPassword"),
                params.get("id"));
        // ----------------------------------------------------------------
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        String oldPasswordFromDatabase = userDao.getPassWordById(params
                .get("id"));
        if (params.get("id") != null) {
            if (oldPasswordFromInput != null) {
                if (oldPasswordFromDatabase != null) {
                    // 查询之前的密码是否正确
                    if (oldPasswordFromInput.equals(oldPasswordFromDatabase)) {
                        // 两个密码相同再进行修改密码操作
                        if (userDao.modifyPassword(newPassword,
                                params.get("id"))) {
                            // 同步-----------------------------
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("userId", params.get("id"));
                            param.put("password", params.get("newPassword"));
                            Map<String, String> map = getUserRegistrationServer()
                                    .resetUsersPasswordOnlineFile(param);
                            if (map.get("success").endsWith("true")) {
                                reutrnMap.put("isModifySuccess", "1"); // 1代表成功
                                reutrnMap.put("isPasswordValid", "true");
                            } else {
                                reutrnMap.put("isModifySuccess", "0");
                                return reutrnMap;
                            }

                            // 日志------------------------------------------
                            Map<String, Map<String, String>> companyUserInitInfo = null;
                            //20151120 xyc -1 表示企业不存在的用户进行修改，将给予默认企业名称：东方飞扬云平台
                            if(params.get("companyId")!=null && !params.get("companyId").equals("-1")){
	                            Object obj = CacheUtils.get(
	                                    this.getCompLocator(),
	                                    "companyUserInitInfo"
	                                            + params.get("companyId"));
	                            if (obj != null) {
	                                companyUserInitInfo = (Map<String, Map<String, String>>) obj;
	                            } else {
	                                companyUserInitInfo = userDao.getUserInitInfo(params.get("companyId"));
	                            }
                            }
                            HashMap<String, String> log = new HashMap<String, String>();
                            log.put("userid", params.get("id"));
                            log.put("username", params.get("id"));
                            log.put("ip", params.get("ip"));
                            log.put("module", "修改密码");
                            // login 用户登录 access 功能访问 operation 功能操作
                            log.put("type", "operation");
                            log.put("operate", "修改密码");
                            log.put("companyName",companyUserInitInfo!=null?companyUserInitInfo.get(params.get("id")).get("COMPANYNAME"):"东方飞扬云平台");
                            log.put("loginfo", "修改密码,用户名为【" + params.get("id")+ "】");
                            logutils.saveBaseLog(this.getCompLocator(), log);
                            // 日志------------------------------------------
                        } else {
                            reutrnMap.put("isModifySuccess", "0");
                        }
                    } else {
                        reutrnMap.put("isPasswordValid", "false"); // 密码不正确
                    }
                }

            }

        }
        return reutrnMap;
    }

    /*------------------------------md5加密------------------------------------*/
    public String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }

    public List<HashMap<String, Object>> getOrgByUserId(
            Map<String, String> params) {
        List<HashMap<String, Object>> retList = new ArrayList<HashMap<String, Object>>();
        retList = userDao.getOrgByUserId(params);
        // 日志------------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(),
                "companyUserInitInfo" + params.get("companyId"));
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        } else {
            companyUserInitInfo = userDao.getUserInitInfo(params
                    .get("companyId"));
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("userid", params.get("username"));
        log.put("username", params.get("username"));
        log.put("ip", params.get("ip"));
        log.put("module", "获得机构列表");
        log.put("companyName", companyUserInitInfo.get(params.get("username"))
                .get("COMPANYNAME"));
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "access");
        log.put("operate", "获得机构列表");
        log.put("loginfo", "获得机构列表,用户名为【" + params.get("username") + "】");
        logutils.saveBaseLog(this.getCompLocator(), log);
        // 日志------------------------------------------
        return retList;
    }

    public Map<String, Object> openUrl(Map<String, String> map) { // {classId=283,
    	//20151118 xiayongcai 通过url复制链接邀请用户携带发送人用户
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        // 插入users表
        // String code = MD5(map.get("username"));
        // map.put("code", code);
        // int id = userDao.saveUserJustCode(map);
        map.put("companyid", map.get("companyId"));
        // map.put("id", id+"");
        Map<String, String> classMap = getDocumentClassWS().getCateById(map);
        // 插入分类表 插入分类表
        String url = null;
        String urle = "https://" + map.get("ipandport")
                + "/onlinefile/0/default/ESUserInfo/toActiveByLink?";
        try {
            // String lasturl = URLEncoder.encode(last, "UTF-8");
            String last = "companyid="
                    + URLEncoder.encode(map.get("companyId"), "UTF-8")
                    + "&classId="
                    + URLEncoder.encode(map.get("classId"), "UTF-8")
                    + "&className="
                    + URLEncoder.encode(classMap.get("CLASSNAME"), "UTF-8")
                    + "&sendUserName="
                    + URLEncoder.encode(map.get("username"), "UTF-8");
            url = urle + last;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        reutrnMap.put("success", true);
        reutrnMap.put("url", url);

        return reutrnMap;
    }
    
    @Override
    public Map<String, Object> showActivate(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>(); 
        String code = params.get("code");
        String id = params.get("id");
        String time =params.get("sendTime");
        if (time != null&&!"".equals(time)) {
        // 查看该用户是否超过邀请期
        try {
                long l = System.currentTimeMillis() - Long.parseLong(time);
                int days = new Long(l / (1000 * 60 * 60 * 24)).intValue();
                if (days >= 7) {// 7天内激活有效
                    reutrnMap.put("timeout", true);
                    reutrnMap.put("issuccess", true);
                    return reutrnMap;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
       
        String userCode = userDao.getCodeById(id);
        if (code.equals(userCode)) { // 根据用户名获得该用户的code码
            // 日志------------------------------------------
            HashMap<String, String> log = new HashMap<String, String>();
            log.put("userid", params.get("username"));
            log.put("username", params.get("username"));
            log.put("ip", params.get("ip"));
            log.put("module", "激活页面获取数据");
            log.put("operate", "激活页面获取数据");
            log.put("companyName", params.get("companyName"));
            // login 用户登录 access 功能访问 operation 功能操作
            log.put("type", "operation");
            log.put("loginfo", "激活页面,用户名为【" + params.get("username") + "】");
            logutils.saveBaseLog(this.getCompLocator(), log);
            // 日志------------------------------------------
            // 根据公司id查出公司名称 返回
            reutrnMap.put("issuccess", true);
        } else {
            reutrnMap.put("issuccess", false);
            if (userCode == null) { // 已被删除、失效
                reutrnMap.put("disabled", true);
            }
        }
        }else{
          reutrnMap.put("disabled", true);
        }
        return reutrnMap;
    }

    @Override
    public Map<String, Object> userIsExist(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        if (userDao.getUserIdByUserName(params.get("USERNAME")) != -1) { // 表示存在说
            reutrnMap.put("isexist", true);

        } else {
            reutrnMap.put("isexist", false);
        }
        return reutrnMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> activateAccount(Map<String, String> params,
            HttpServletRequest request) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        Map<String, String> data = new HashMap<String, String>();
        data.put("USERID", params.get("username"));
        String fullname = params.get("fullname");
        String companyId = params.get("companyid");
        String userId = params.get("id");
        String FIRSTNAME = "";
        String LASTNAME = "";
        if (null != fullname && !"".equals(fullname)) {
            LASTNAME = fullname.substring(0, 1);
            if (fullname.length() > 1)
                FIRSTNAME = fullname.substring(1);
        }
        data.put("FIRSTNAME", FIRSTNAME);
        data.put("LASTNAME", LASTNAME);
        String password = params.get("password");
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        password = md5.encodePassword(password, params.get("username"));
        data.put("PASSWORD", password);
        data.put("USERSTATUS", "1");
        data.put("SAASID", params.get("companyid"));
        data.put("ISUPDATE", "insert");
        Map<String, Object> msg = getSyncConfigWS().synchronizeUserToPlatform(data);
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(Integer.valueOf(userId));
        if (Boolean.valueOf(msg.get("success").toString())) {
            boolean flag = userDao.activateAccount(params);
            if (flag) {
                // 添加公司用户companyId, List<Integer> ids, int status
                userDao.updateCompanyUserStatus(params.get("companyid"), ids, 1);
                /** 20151016 xiayongcai 修改公司状态，并将class重置为0，这儿的class不等于0的情况下，界面显示的是有小红点的，表示邀请*/
                //userDao.agreenInSideCompany(params.get("companyid"), params.get("id"));
               Map<String, Map<String, String>> companyUserInitInfo = null;
                Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
                companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                
                if(companyUserInitInfo==null || companyUserInitInfo.isEmpty()){
                  companyUserInitInfo = userDao.getUserInitInfo(companyId);
                  CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                          + companyId, companyUserInitInfo);
                }
                if (companyUserInitInfo != null) {
                    if (companyUserInitInfo.get(params.get("username")) != null) {
                        companyUserInitInfo.remove(params.get("username"));
                    }
                    companyUserInitInfo.put(params.get("username"),userDao.getOneUserInfo(params.get("id")));
                    CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"+companyId, companyUserInitInfo);
                    // 刷新utc这个缓存
                    userDao.cacheUserToCompanys(this.getCompLocator());
                }
                // 判断是否需要-插入分组
                Map<String, String> grupInfo = null;
                if (params.get("className") != null
                        && !"".equals(params.get("className"))
                        && params.get("classId") != null
                        && !"".equals(params.get("classId"))) {
                    params.put("groupname", params.get("className"));
                    grupInfo = getChatWS().saveUserToGroupFromEamil(params);
                    
                }
                //用户激活 删除邀请表里的分类邀请记录
                userDao.deleteInvite(companyId, userId);
                // 日志------------------------------------------
                HashMap<String, String> log = new HashMap<String, String>();
                log.put("userid", params.get("username"));
                log.put("username", params.get("username"));
                log.put("ip", params.get("ip"));
                log.put("module", "激活用户");
                log.put("companyName",
                        companyUserInitInfo.get(params.get("username")).get(
                                "COMPANYNAME"));
                // login 用户登录 access 功能访问 operation 功能操作
                log.put("type", "operation");
                log.put("operate", "激活用户");
                log.put("loginfo", "激活用户,用户名为【" + params.get("username") + "】");
                logutils.saveBaseLog(this.getCompLocator(), log);
                // 日志------------------------------------------
                reutrnMap.put("issuccess", flag);
                if (grupInfo != null) {
                    reutrnMap.put("groupflag", grupInfo.get("groupflag"));
                }
              //20151114 xiayongcai 注册成功直接登陆系统; 
                reutrnMap.put("logUrl",activateHost+"/flyingoauth/j_spring_security_check?username="+params.get("username")+"&password="+params.get("password")+"&success="+activateHostUrl+"/user/token&error="+activateHostUrl+"/Default?error");
            }
        } else {
            reutrnMap.put("issuccess", false);
            reutrnMap.put("msg", msg.get("msg"));
        }
        return reutrnMap;
    }

    @Override
    public Map<String, Object> activateAccountByLink(
            Map<String, String> params, HttpServletRequest request) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        Map<String, String> data = new HashMap<String, String>();
        data.put("USERID", params.get("username"));
        String fullname = params.get("fullname");
        String FIRSTNAME = "";
        String LASTNAME = "";
        if (null != fullname && !"".equals(fullname)) {
            LASTNAME = fullname.substring(0, 1);
            if (fullname.length() > 1)
                FIRSTNAME = fullname.substring(1);
        }
        data.put("FIRSTNAME", FIRSTNAME);
        data.put("LASTNAME", LASTNAME);
        String password = params.get("password");
        String loginPassword = params.get("password");
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        password = md5.encodePassword(password, params.get("username"));
        data.put("PASSWORD", password);
        data.put("USERSTATUS", "1");
        data.put("SAASID", params.get("companyid"));
        data.put("ISUPDATE", "insert");
        Map<String, Object> msg = getSyncConfigWS().synchronizeUserToPlatform(
                data);
        if (Boolean.valueOf(msg.get("success").toString())) {
            params.put("password", password);
            params.put("isadmin", "0");
            int id = userDao.saveRealUser(params);
            params.put("id", id + "");
            reutrnMap.put("issuccess", id);
            if (id > 0) {
                reutrnMap.put("issuccess", true);
                Map<String, Map<String, String>> companyUserInitInfo = null;
                Object obj = CacheUtils.get(this.getCompLocator(),
                        "companyUserInitInfo" + params.get("companyid"));
                if (obj != null) {
                    companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                    if (companyUserInitInfo.get(params.get("username")) != null) {
                        companyUserInitInfo.remove(params.get("username"));
                    }
                    companyUserInitInfo.put(params.get("username"),
                            userDao.getOneUserInfo(params.get("id")));
                    CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                            + params.get("companyid"), companyUserInitInfo);
                    // 刷新utc这个缓存
                    userDao.cacheUserToCompanys(this.getCompLocator());
                }
                // 插入分组

                params.put("groupname", params.get("className"));
                Map<String, String> grupInfo = getChatWS()
                        .saveUserToGroupFromEamil(params);
                reutrnMap.put("groupflag", grupInfo.get("groupflag"));

                // 日志------------------------------------------
                HashMap<String, String> log = new HashMap<String, String>();
                log.put("userid", params.get("username"));
                log.put("username", params.get("username"));
                log.put("ip", params.get("ip"));
                log.put("module", "激活用户");
                log.put("companyName",
                        companyUserInitInfo.get(params.get("username")).get(
                                "COMPANYNAME"));
                // login 用户登录 access 功能访问 operation 功能操作
                log.put("type", "operation");
                log.put("operate", "激活用户");
                log.put("loginfo", "激活用户,用户名为【" + params.get("username") + "】");
                logutils.saveBaseLog(this.getCompLocator(), log);
                // 日志------------------------------------------
                //20151114 xiayongcai 注册成功直接登陆系统; 
                reutrnMap.put("logUrl",activateHost+"/flyingoauth/j_spring_security_check?username="+params.get("username")+"&password="+loginPassword+"&success="+activateHostUrl+"/user/token&error="+activateHostUrl+"/Default?error");
            }
        } else {
            reutrnMap.put("issuccess", false);
            reutrnMap.put("msg", msg.get("msg"));
        }
        return reutrnMap;

    }

    @Override
    public Map<String, String> getUserInfoByUserName(Map<String, String> params) {
        String username = params.get("username");
        // 日志------------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(),
                "companyUserInitInfo" + params.get("companyId"));
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        } else {
            companyUserInitInfo = userDao.getUserInitInfo(params
                    .get("companyId"));
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("userid", params.get("username"));
        log.put("username", params.get("username"));
        log.put("ip", params.get("ip"));
        log.put("module", "获得用户个人信息");
        log.put("operate", "获得用户个人信息");
        log.put("companyName", companyUserInitInfo.get(params.get("username"))
                .get("COMPANYNAME"));
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "access");
        log.put("loginfo", "获得用户个人信息,用户名为【" + params.get("username") + "】");
        logutils.saveBaseLog(this.getCompLocator(), log);
        // 日志------------------------------------------
        return companyUserInitInfo.get(username);
    }

    @Override
    public void getUserInfoByUserName(HttpServletRequest request,
            HttpServletResponse response) {
        String username = request.getParameter("username");
        String companyId = request.getParameter("companyId");
        String groupid = request.getParameter("groupid");
        String userId = request.getParameter("userId");
        // 日志------------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(),
                "companyUserInitInfo" + companyId);
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        } else {
            companyUserInitInfo = userDao.getUserInitInfo(companyId);
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("userid", username);
        log.put("username", username);
        log.put("ip", request.getRemoteAddr());
        log.put("module", "获得用户个人信息");
        log.put("operate", "获得用户个人信息");
        log.put("companyName",
                companyUserInitInfo.get(username).get("COMPANYNAME"));
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "access");
        log.put("loginfo", "获得用户个人信息,用户名为【" + username + "】");
        logutils.saveBaseLog(this.getCompLocator(), log);
        // 日志------------------------------------------
        Map<String, String> map = companyUserInitInfo.get(username);
        List<String> groupHasdUsers = getChatWS().getGroupUsersByGroupId(
                groupid);
        if (groupHasdUsers.contains(userId)) {
            map.put("isgroupuser", "true");
        } else {
            map.put("isgroupuser", "false");
        }
        JSONObject json = new JSONObject();
        json.put("isOk", true);
        json.put("user", map);
        output(request, response, json);
    }

    @Override
    public List<HashMap<String, Object>> userList(HashMap<String, String> params) {
        List<HashMap<String, Object>> rtnList = new ArrayList<HashMap<String, Object>>();
        int stratNo = Integer.parseInt(params.get("startNo"));
        if (stratNo < 0)
            stratNo = 0;
        int limit = Integer.parseInt(params.get("limit"));
        int companyId = Integer.parseInt(params.get("companyId"));
        rtnList = userDao.getUserListByCompanyId(companyId, stratNo, limit);
        // 日志------------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(),
                "companyUserInitInfo" + params.get("companyId"));
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        } else {
            companyUserInitInfo = userDao.getUserInitInfo(params
                    .get("companyId"));
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("userid", params.get("username"));
        log.put("username", params.get("username"));
        log.put("ip", params.get("ip"));
        log.put("module", "获得用户列表");
        log.put("operate", "获得用户列表");
        log.put("companyName", companyUserInitInfo.get(params.get("username"))
                .get("COMPANYNAME"));
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "access");
        log.put("loginfo", "获得用户列表,用户名为【" + params.get("username") + "】");
        logutils.saveBaseLog(this.getCompLocator(), log);
        // 日志------------------------------------------
        return rtnList;
    }

    @Override
    public List<HashMap<String, Object>> userListByUserName(
            HashMap<String, String> params) {
        List<HashMap<String, Object>> rtnList = new ArrayList<HashMap<String, Object>>();
        int stratNo = Integer.parseInt(params.get("startNo"));
        if (stratNo < 0)
            stratNo = 0;
        String searchUserName = params.get("searchusername");
        int limit = Integer.parseInt(params.get("limit"));
        int companyId = Integer.parseInt(params.get("companyId"));
        rtnList = userDao.getUserListByCompanyIdAndUserName(companyId,
                searchUserName, stratNo, limit);
        // 日志------------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(),
                "companyUserInitInfo" + params.get("companyId"));
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        } else {
            companyUserInitInfo = userDao.getUserInitInfo(params
                    .get("companyId"));
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("userid", params.get("username"));
        log.put("username", params.get("username"));
        log.put("ip", params.get("ip"));
        log.put("module", "获得用户列表");
        log.put("operate", "获得用户列表");
        log.put("companyName", companyUserInitInfo.get(params.get("username"))
                .get("COMPANYNAME"));
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "access");
        log.put("loginfo", "获得用户列表,用户名为【" + params.get("username") + "】");
        logutils.saveBaseLog(this.getCompLocator(), log);
        // 日志------------------------------------------
        return rtnList;
    }

    @Override
    public int getCountAll(Map<String, String> params) {
        int count = userDao.getCountAllByCompanyId(params);
        return count;
    }

    @Override
    public Map<String, Object> deleteUserList(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        String usernamesStr = params.get("usernames").trim();// 多个用逗号隔开的username
        String idss = params.get("ids");// 多个用逗号隔开的username
        if (usernamesStr != null && !"".equals(usernamesStr) || idss != null
                && !"".equals(idss)) {
            String[] usernames = usernamesStr.split(",");
            Map<String, String> delSubScribeMaps = new HashMap<String, String>();
            delSubScribeMaps.put("usernames", usernamesStr);
            delSubScribeMaps.put("userId", params.get("userId"));
            /** 删除用户同时删除订阅信息 **/
            userDao.deleteUserSubScribersByUserNames(delSubScribeMaps);
            String[] ids = idss.split(",");
            if (ids.length > 0) { // 按照id删除 先留着 LIUMINGCHAO
                if (userDao.deleteUserById(ids)) {
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("ISUPDATE", "delete");
                    usernamesStr = usernamesStr.replaceAll(",", "','");
                    data.put("IDS", "'" + usernamesStr + "'");
                    this.getSyncConfigWS().synchronizeUserToPlatform(data);
                    reutrnMap.put("issuccess", true);
                    // 日志------------------------------------------
                    Map<String, Map<String, String>> companyUserInitInfo = null;
                    Object obj = CacheUtils.get(this.getCompLocator(),
                            "companyUserInitInfo" + params.get("companyId"));
                    if (obj != null) {
                        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                    } else {
                        companyUserInitInfo = userDao.getUserInitInfo(params
                                .get("companyId"));
                    }
                    HashMap<String, String> log = new HashMap<String, String>();
                    log.put("userid", params.get("username"));
                    log.put("username", params.get("username"));
                    log.put("ip", params.get("ip"));
                    log.put("module", "删除用户");
                    log.put("operate", "删除用户");
                    log.put("companyName",
                            companyUserInitInfo.get(params.get("username"))
                                    .get("COMPANYNAME"));
                    // login 用户登录 access 功能访问 operation 功能操作
                    log.put("type", "operation");
                    log.put("loginfo", "删除用户名为【" + params.get("usernames")
                            + "】" + "表ID为【" + params.get("ids") + "】的用户");
                    logutils.saveBaseLog(this.getCompLocator(), log);
                    /** xiaoxiong 20150515 删除用户时，将用户从缓存中去掉 **/
                    for (String username : usernames) {
                        companyUserInitInfo.remove(username);
                    }
                    CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                            + params.get("companyId"), companyUserInitInfo);
                } else {
                    reutrnMap.put("issuccess", false);
                }
            }
        } else {
            reutrnMap.put("issuccess", false);
        }
        return reutrnMap;
    }

    @Override
    public boolean updateUser(Map<String, String> onlinefileUser) {
        boolean flag = false;
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        if (userDao.getCodeById(onlinefileUser.get("userId")).equals(
                onlinefileUser.get("CODE"))) {
            Md5PasswordEncoder md5 = new Md5PasswordEncoder();
            String password = md5.encodePassword(
                    onlinefileUser.get("PASSWORD"),
                    onlinefileUser.get("USERNAME"));
            onlinefileUser.put("PASSWORD", password);
            if (userDao.updateUser(onlinefileUser)) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public HashMap<String, String> getUserById(String id) {
        HashMap<String, String> user = userDao.getUserById(id);
        return user;
    }

    @Override
    public boolean saveUser(Map<String, String> params) {
        String password = params.get("password");
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        password = md5.encodePassword(password, params.get("username"));
        params.put(password, password);
        int id = userDao.saveRealUser(params);
        if (id > 0) {
            Map<String, Map<String, String>> companyUserInitInfo = null;
            Object obj = CacheUtils.get(this.getCompLocator(),
                    "companyUserInitInfo" + params.get("companyid"));
            if (obj == null) {
                companyUserInitInfo = new HashMap<String, Map<String, String>>();
            } else {
                companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                if (companyUserInitInfo.get(params.get("username")) != null) {
                    companyUserInitInfo.remove(params.get("username"));
                }
            }
            companyUserInitInfo.put(params.get("username"),
                    userDao.getOneUserInfo(id + ""));
            CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                    + params.get("companyid"), companyUserInitInfo);

            HashMap<String, List<String>> utc = new HashMap<String, List<String>>();
            HashMap<String, String> companyAdmin = new HashMap<String, String>();
            obj = CacheUtils.get(this.getCompLocator(), "ofuserCompanys");
            if (null != obj) {
                utc = (HashMap<String, List<String>>) obj;
            } else {
                utc = new HashMap<String, List<String>>();
            }
            obj = CacheUtils.get(this.getCompLocator(), "ofcompanyAdmin");
            if (null != obj) {
                companyAdmin = (HashMap<String, String>) obj;
            } else {
                companyAdmin = new HashMap<String, String>();
            }
            if (utc.get(id + "") == null) {
                utc.put(id + "", new ArrayList<String>());
            }
            utc.get(id + "").add(params.get("companyid"));
            companyAdmin.put(params.get("companyid"), id + "");
            
            // longjunhao 20151013 add 用户第一次注册是的时候，也要把状态添加到缓存ofcompanyUserIsStatus中
            HashMap<String, List<HashMap<String, String>>> companyUserIsStatus = new HashMap<String, List<HashMap<String, String>>>() ;
            obj = CacheUtils.get(this.getCompLocator(), "ofcompanyUserIsStatus") ;
            if (null != obj) {
                companyUserIsStatus = (HashMap<String, List<HashMap<String, String>>>) obj ;
            }
            if(companyUserIsStatus.get(id+"") == null){
                companyUserIsStatus.put(id+"",new ArrayList<HashMap<String, String>>()) ;
            }
            HashMap<String, String> companyIsStatus = new HashMap<String, String>() ;
            companyIsStatus.put(params.get("companyid"), "1");
            companyUserIsStatus.get(id+"").add(companyIsStatus) ;
            CacheUtils.set(this.getCompLocator(), "ofcompanyUserIsStatus", companyUserIsStatus);
            
            CacheUtils.set(this.getCompLocator(), "ofuserCompanys", utc);
            CacheUtils.set(this.getCompLocator(), "ofcompanyAdmin",
                    companyAdmin);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<String, String> saveHeadImage(Map<String, String> params) {
        Map<String,String> reutrnMap = new HashMap<String, String>();
        if(userDao.saveHeadImage(params)){
        	//20151120 xyc -1 表示企业不存在的用户进行修改，将给予默认企业名称：东方飞扬云平台 
        	//禁止的用户修改头像先绕开和企业关联的数据
        	Map<String, Map<String, String>> companyUserInitInfo = null;
        	if(params.get("companyid")!=null && !params.get("companyid").equals("-1")){
        		getFilesWS().updateFileCommentsUserHeadImg(params);
                //Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+params.get("companyid")) ;
                List<Map<String,String>> companys = userDao.getCompanysByUserName(params.get("username"));
                //插入缓存中 
                if(companys.size()>0){
                    //companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
                    /*if(companyUserInitInfo.get(params.get("username"))!=null){
                        companyUserInitInfo.remove(params.get("username")) ;
                    }*/
                    //companyUserInitInfo.put(params.get("username"), userDao.getUserInitInfo(params.get("companyid"), params.get("username"))) ;
                    //companyUserInitInfo = userDao.getUserInitInfo(params.get("companyid"));
                    for (Map<String, String> map : companys) {

                         //CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"+map.get("COMPANYID"), companyUserInitInfo);

                         Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+map.get("COMPANYID")) ;
                         if(obj!=null){ 
                             companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
                             companyUserInitInfo.put(params.get("username"),userDao.getUserInitInfo(map.get("COMPANYID"), params.get("username"))) ;
                             CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"+map.get("COMPANYID"), companyUserInitInfo);
                         }
                    }
                    getProducerWS().doSender(activeMQPath, params);
                }else{
                    companyUserInitInfo = userDao.getUserInitInfo(params.get("companyid"));
                    CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"+params.get("companyid"), companyUserInitInfo);
                }
        	}
        	 HashMap<String, String> log = new HashMap<String, String>();
             log.put("userid",params.get("username"));
             log.put("username",params.get("username"));
             log.put("ip", params.get("ip"));
             log.put("module","保存头像");
             
             log.put("operate", "保存头像");
             log.put("companyName", companyUserInitInfo!=null?companyUserInitInfo.get(params.get("username")).get("COMPANYNAME"):"东方飞扬云平台");
            // login 用户登录 access 功能访问 operation 功能操作
             log.put("type", "operation");
             log.put("loginfo", "保存头像,用户名为【"+params.get("username")+"】");
             logutils.saveBaseLog(this.getCompLocator(), log);
            // 日志------------------------------------------
            reutrnMap.put("success", "true");
        }else{
            reutrnMap.put("success", "false");
        }
        return reutrnMap;
    }

    @Override
    public Map<String, String> getImagePath(Map<String, String> params) {
        Map<String, String> reutrnMap = new HashMap<String, String>();
        reutrnMap = userDao.getImagePath(params);
        return reutrnMap;
    }

    // 启用禁用
    @Override
    public Map<String, Object> changeIsEnableStatus(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        String userIdStr = params.get("userIds");// 多个用逗号隔开的userIds
        String userNameStr = params.get("userNames");// 多个用逗号隔开的userNames
        String emailStr = params.get("emails");// 多个用逗号隔开的emails
        String userstatus = params.get("userstatus");// 改变后的状态，1为启用，0为禁用，启用时需要发送邮件通知
        String fullNameStr = params.get("fullNames");// 多个用逗号隔开的fullNames
        String loginUser = params.get("loginUser");// 当前登录人
        String companyId = params.get("companyId");// 企业ID
        String userId = params.get("userId");
        HashMap<Integer, String> emailcontents = new HashMap<Integer, String>();
        HashMap<Integer, String> emails = new HashMap<Integer, String>();
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(),
                "companyUserInitInfo" + companyId);
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        }

        if (companyUserInitInfo.get(params.get("username")) == null
                || ((companyUserInitInfo.get(params.get("username")) != null) && companyUserInitInfo
                        .get(params.get("username")).equals(""))) {
            this.getUserInitInfo(companyId, loginUser);
            obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"
                    + companyId);
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        }
        // 公司下的用户状态
        HashMap<String, List<HashMap<String, String>>> companyUserIsStatus = null;
        obj = CacheUtils.get(this.getCompLocator(), "ofcompanyUserIsStatus");
        if (obj != null) {
            companyUserIsStatus = (HashMap<String, List<HashMap<String, String>>>) obj;
        }

        if (userIdStr != null && !"".equals(userIdStr)) {
            String[] userIds = userIdStr.split(",");
            String[] fullNames = fullNameStr.split(",");
            String[] emailsUse = emailStr.split(",");
            Map<String, String> data = new HashMap<String, String>();
            data.put("userNameStr", userNameStr);
            data.put("status", userstatus);
            // 不进行同步、ESS_USER的状态
            // Map<String,Object> msg =
            // getSyncConfigWS().synchronizeUserStatusToPlatform(data);//同步状态
            // if(Boolean.valueOf(msg.get("success").toString())){
            if (userIds != null && userIds.length > 0) {
                List<Integer> ids = new LinkedList<Integer>();
                for (int i = 0; i < userIds.length; i++) {
                    ids.add(Integer.parseInt(userIds[i]));
                }
                // 修改用户状态、公司下的用户状态
                // userDao.updateStatus(ids,
                // Integer.parseInt(userstatus));//修改状态
                userDao.updateCompanyUserStatus(companyId, ids,
                        Integer.parseInt(userstatus));
                if (null != userstatus && userstatus.equals("0")) {
                    userDao.updateUserSubScribeStatus(ids);
                }
                // userDao.updateUserSubScribeStatus(ids);
                if ("0".equals(userstatus)) { // 禁用
                    List<Map<String, String>> groups = getChatWS()
                            .addAdminIntoGroups(params);
                    reutrnMap.put("groups", groups);
                } else if ("1".equals(userstatus)) { // 启用
                    List<Map<String, String>> groups = getFilesWS()
                            .changeGroupRelationIsAdmin(params); // 修改grouprelation表的isadmin字段
                    reutrnMap.put("groups", groups);
                }
                getFilesWS().changeFileOwner(params); // 修改文件的拥有者
                reutrnMap.put("issuccess", true);
                // 日志------------------------------------------
                HashMap<String, String> log = new HashMap<String, String>();
                log.put("userid", params.get("username"));
                log.put("username", params.get("username"));
                log.put("ip", params.get("ip"));
                log.put("module", "改变用户状态");
                log.put("operate", "改变用户状态");
                log.put("companyName",
                        companyUserInitInfo.get(params.get("username")).get(
                                "COMPANYNAME"));
                // login 用户登录 access 功能访问 operation 功能操作
                log.put("type", "operation");
                String status = userstatus.equals("1") ? "启用" : "禁用";
                log.put("loginfo", "改变用户状态,用户名为【" + params.get("userNames")
                        + "】的用户状态更改为【" + status + "】");
                logutils.saveBaseLog(this.getCompLocator(), log);
                // 日志------------------------------------------
                for (int i = 0; i < userIds.length; i++) {
                    if ("".equals(emailsUse[i].trim()))
                        continue; // 如果邮箱是空的 就不发送邮件
                    String isEnable = userstatus.equals("1") ? "启用" : "禁用";
                    String content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
                            + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
                            + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
                            + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
                            + fullNames[i]
                            + ":</strong>  您所在"
                            + companyUserInitInfo.get(params.get("username"))
                                    .get("COMPANYNAME")
                            + "的账号   "
                            + userNameStr
                            + "   已经被"
                            + isEnable
                            + "！</p>"
                            + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
                            + "<p style=\"font-size: 18px; line-height: 24px;\"><strong>使用飞扬为您的团队工作</strong> – 您可以在线分享和收藏，日常交流.</p>"
                            + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
                            + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
                            + "祝好运!<br>"
                            // +"--"+fullNames[i]
                            + "-- 东方飞扬"
                            + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
                            + "</div>"
                            + "</p>"
                            + "</div></td></tr></tbody></table>";
                    emails.put(Integer.parseInt(userIds[i]), emailsUse[i]);
                    emailcontents.put(Integer.parseInt(userIds[i]), content);
                }
                if (!emails.isEmpty()) {
                    String subject = "东方飞扬在线分享系统-通知:您的帐号被"
                            + (userstatus.equals("1") ? "启用" : "禁用");
                    /** 调用线程进行邮件发送 **/
                    ThreadPoolManager m = ThreadPoolManager.getInstance();
                    try {
                        m.runTaskforFree(new sendMailCallable(null, emails,
                                emailcontents, subject, true, false));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                }
                String[] userNameArray = userNameStr.split(",");
                String[] userIdArray = userIdStr.split(",");
                if (userstatus.equals("0")) {
                    /** xiaoxiong 20150518 在禁用用户时，remove缓存信息 **/
                    for (String username : userNameArray) {
                        String tempusername = username.replace("@", "\\40");
                        BroadcastUtils.broadcast(tempusername,tempusername+ "@"+ BroadcastUtils.openfireServerName,"broadcast-offline:" + username,cn.flying.rest.onlinefile.utils.Message.Type.GROUP_CHAT,companyUserInitInfo.get(username).get("FULLNAME"));
                        companyUserInitInfo.remove(username);
                        CacheUtils.set(this.getCompLocator(),"companyUserInitInfo" + companyId,companyUserInitInfo);
                    }
                } else if (userstatus.equals("1")) {
                    /** xiaoxiong 20150518 在启用用户时，add缓存信息 **/
                    for (String username : userNameArray) {
                        Map<String, String> user = userDao.getUserInitInfo(companyId, username);
                        companyUserInitInfo.put(username, user);
                        CacheUtils.set(this.getCompLocator(),"companyUserInitInfo" + companyId,companyUserInitInfo);
                    }
                }
                // 缓存
                for (String userids : userIdArray) {
                    List<HashMap<String, String>> companyUserIsStatuslist = companyUserIsStatus
                            .get(userids);
                    for (HashMap<String, String> companyUserIs : companyUserIsStatuslist) {
                        for (String key : companyUserIs.keySet()) {
                            if (key.equals(companyId)) {
                                companyUserIs.put(key, userstatus);
                            }
                        }
                    }
                    CacheUtils.set(this.getCompLocator(),"ofcompanyUserIsStatus", companyUserIsStatus);
                }
                
                //禁止用户将用户T出企业，将不会接受到企业信息
                String arg = "secret=flyingsoft&type=reset_group&groups=company"+params.get("companyId");
                if(userstatus.equals("0")){//禁用，
                	arg +="&deletegroupusernames="+ userNameStr.replace("@", "\\40");
                }else if(userstatus.equals("1")){//启用
                //启用用户将用户+进企业，将会继续接受企业信息
                	arg +="&addgroupusernames="+ userNameStr.replace("@", "\\40");
                }
                
              post(arg);
                
            } else {
                reutrnMap.put("issuccess", false);
                // reutrnMap.put("msg", msg.get("msg"));
            }
        } else {
            reutrnMap.put("issuccess", false);
        }
        return reutrnMap;
    }

    @Override
    public Map<String, Object> reSendMail(Map<String, String> params) {
        Map<String, Object> param = new HashMap<String, Object>();
        String userId = "";
        String companyId = params.get("companyId");
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        if (null != params.get("userId") && !"".equals(params.get("userId"))) {
            userId = params.get("userId");
        } else {
            if (null != params.get("emails")
                    && !"".equals(params.get("emails"))) {
                // 通过邮件查询ID
                String email[] = params.get("emails").split(",");
                for (int i = 0; i < email.length; i++) {
                    if (null != email[i] && !"".equals(email[i])) {
                        // 用过Email查找id
                        userId += userDao.getUserId(email[i]) + ",";
                    }
                }
            }
        }
        if (null != userId && !"".equals(userId)) {
            String userids[] = userId.split(",");
            for (int i = 0; i < userids.length; i++) {
                param.put("editUserid", userids[i]);// ID
                param.put("STATUS", "-1");// Status
                Map<String, Object> map = userDao.getUserInfoByUserId(Integer
                        .parseInt(userids[i]));
                HashMap<Integer, String> emailcontents = new HashMap<Integer, String>();
                HashMap<Integer, String> emails = new HashMap<Integer, String>();
                String message = "Onlinefile是一款简单好用的组织知识分享系统，专门为组织内部及时沟通分享而打造，使用它可以方便的进行组织内部文档分享交流，提高团队工作效率，团队成员之间无缝沟通，大家赶快加入吧。";
                if (null != map && map.size() > 0) {
                    // 重新发送邮件并且修改字段值
                    userDao.editUser(param);
                    // 修改创建时间 就是七天的时间
                    if (!userDao.changeCreateTime(params)) {
                        reutrnMap.put("issuccess", false);
                        return reutrnMap;
                    }
                    reutrnMap.put("issuccess", true);
                    String fullName = map.get("FULLNAME") != null ? map.get(
                            "FULLNAME").toString() : "";
                    String email = map.get("EMAIL").toString();
                    String compid = map.get("COMPID").toString();
                    String compname = map.get("COMPNAME").toString();
                    // String code = MD5(fullName);
                    String code = MD5(email);
                    String activateURL = null;
                    String content = "";
                    Map<String, Map<String, String>> companyUserInitInfo = null;
                    try {
                        Object obj = CacheUtils
                                .get(this.getCompLocator(),
                                        "companyUserInitInfo"
                                                + params.get("companyId"));
                        if (obj != null) {
                            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                        } else {
                            companyUserInitInfo = userDao
                                    .getUserInitInfo(params.get("companyId"));
                        }
                        activateURL = BroadcastUtils.http_schema
                                + "//"
                                + params.get("ipandport")
                                + "/onlinefile/0/default/ESUserInfo/toActive?id="
                                + userids[i] + "&fullname="
                                + URLEncoder.encode(fullName, "UTF-8")
                                + "&companyid=" + compid + "&code=" + code
                                + "&email=" + email + "&companyName="
                                + URLEncoder.encode(compname, "UTF-8")
                                + "&className=" + "" + "&classId=" + "";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                    content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
                            + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
                            + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
                            + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
                            + "" + "</strong>邀请您使用Onlinefile，点击下面的绿色按钮加入<strong>"
                            + compname
                            + "</strong>团队:</p>"
                            + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
                            + "<a href=\""
                            + activateURL
                            + "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">立即激活</a>"
                            + "</p>"
                            + "<p style=\"line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; word-break: break-word;\">如果按钮无法点击，请复制链接至浏览器打开:<a>"
                            + activateURL
                            + "</a></p>"
                            + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
                            + "<p style=\"font-size: 18px; line-height: 24px;text-align: center;\"><strong>"
                            + message
                            + "</p>"
                            + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
                            + "<p style=\"font-size: 0.9rem; line-height: 20px; color: rgb(113, 113, 113); text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">-请在收取邮件后7天内激活-</p>"
                            + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
                            + "祝好运!<br>"
                            + "--"
                            + companyUserInitInfo.get(params.get("username"))
                                    .get("FULLNAME")
                            + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
                            + "</div>"
                            + "</p>"
                            + "</div></td></tr></tbody></table>";
                    emails.put(Integer.parseInt(userids[i]), email);
                    emailcontents.put(Integer.parseInt(userids[i]), content);
                    // 日志------------------------------------------
                    HashMap<String, String> log = new HashMap<String, String>();
                    log.put("userid", params.get("username"));
                    log.put("username", params.get("username"));
                    log.put("ip", params.get("ip"));
                    log.put("module", "重新发送邮件");
                    log.put("operate", "重新发送邮件");
                    log.put("companyName",
                            companyUserInitInfo.get(params.get("username"))
                                    .get("COMPANYNAME"));
                    // login 用户登录 access 功能访问 operation 功能操作
                    log.put("type", "operation");
                    log.put("loginfo", "重新发送邮件,用户中文名为【" + fullName + "】邮箱为【"
                            + email + "】");
                    logutils.saveBaseLog(this.getCompLocator(), log);
                    // 日志------------------------------------------
                    if (!emails.isEmpty()) {
                        String subject = "欢迎来到东方飞扬在线分享系统";
                        /** 调用线程进行邮件发送 **/
                        ThreadPoolManager m = ThreadPoolManager.getInstance();
                        try {
                            m.runTaskforFree(new sendMailCallable(companyId,
                                    emails, emailcontents, subject, true, true));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            logger.error(e.getMessage());
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            logger.error(e.getMessage());
                        }
                    }
                } else {
                    reutrnMap.put("issuccess", false);
                }
            }
        } else {
            reutrnMap.put("issuccess", false);
        }
        return reutrnMap;
    }

    @Override
    public Map<String, Object> getUserInfoByUserId(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        String userId = params.get("userId");
        reutrnMap = userDao.getUserInfoByUserId(Integer.parseInt(userId));
        
        Map<String, Map<String, String>> companyUserInitInfo = null;
      //20151120 xyc -1 表示企业不存在的用户进行修改，将给予默认企业名称：东方飞扬云平台
        if(params.get("companyId")!=null && !params.get("companyId").equals("-1")){
        	 // 日志------------------------------------------
            Object obj = CacheUtils.get(this.getCompLocator(),
                    "companyUserInitInfo" + params.get("companyId"));
            if (obj != null) {
                companyUserInitInfo = (Map<String, Map<String, String>>) obj;
            } else {
                companyUserInitInfo = userDao.getUserInitInfo(params
                        .get("companyId"));
            }
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("userid", params.get("username"));
        log.put("username", params.get("username"));
        log.put("ip", params.get("ip"));
        log.put("operate", "获得用户个人信息");
        log.put("module", "获得用户个人信息");
        log.put("companyName", companyUserInitInfo!=null?companyUserInitInfo.get(params.get("username")).get("COMPANYNAME"):"东方飞扬云平台");
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "access");
        log.put("loginfo", "获得用户个人信息,用户名为【" + userId + "】");
        logutils.saveBaseLog(this.getCompLocator(), log);
        // 日志------------------------------------------
        return reutrnMap;
    }

    @Override
    public Map<String, Object> editUser(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        String editFullname = params.get("editFullname");
        String editMobilephone = params.get("editMobilephone");
        String editTelephone = params.get("editTelephone");
        String editEmail = params.get("editEmail");
        String editPosition = params.get("editPosition");
        String editFax = params.get("editFax");
        int orgId = Integer.parseInt(params.get("org"));
        String editOrgname = params.get("editOrgname");
        String editUsername = params.get("editUsername");
        int editUserid = Integer.parseInt(params.get("editUserid"));
        int editCompid = Integer.parseInt(params.get("editCompid"));
        String editCode = MD5(editFullname);
        if (orgId == -1) { // -1说明是新添加的机构
            orgId = userDao
                    .saveOrgByOrgNameReturnOrgId(editOrgname, editCompid);
            // org = period;
        } else if (userDao.getOrgByOrgId(orgId) != editOrgname) {
            // 修改org
            if (!userDao.modifyOrg(orgId + "", editOrgname)) {
            }
        }

        if (StringTools.isEmpty(editUsername)) {// 用户名为空，不同步
            // 放进集合进行操作
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("editFullname", editFullname);
            param.put("editMobilephone", editMobilephone);
            param.put("editTelephone", editTelephone);
            param.put("editEmail", editEmail);
            param.put("editPosition", editPosition);
            param.put("editFax", editFax);
            param.put("editCode", editCode);
            param.put("editUserid", editUserid);
            param.put("editOrgid", orgId);
            userDao.editUser(param);
            reutrnMap.put("issuccess", true);
            // 日志------------------------------------------
            Map<String, Map<String, String>> companyUserInitInfo = null;
            Object obj = CacheUtils.get(this.getCompLocator(),
                    "companyUserInitInfo" + params.get("companyId"));
            if (obj != null) {
                companyUserInitInfo = (Map<String, Map<String, String>>) obj;
            } else {
                companyUserInitInfo = userDao.getUserInitInfo(params
                        .get("companyId"));
            }
            HashMap<String, String> log = new HashMap<String, String>();
            log.put("userid", params.get("username"));
            log.put("username", params.get("username"));
            log.put("ip", params.get("ip"));
            log.put("module", "管理员修改信息");
            log.put("operate", "管理员修改信息");
            log.put("companyName",
                    companyUserInitInfo.get(params.get("username")).get(
                            "COMPANYNAME"));
            // login 用户登录 access 功能访问 operation 功能操作
            log.put("type", "operation");
            log.put("loginfo", "管理员修改信息,用户中文名为【" + editFullname + "】ID为【"
                    + editUserid + "】邮箱为【" + editEmail + "】");
            logutils.saveBaseLog(this.getCompLocator(), log);
            // 日志------------------------------------------
        } else {
            Map<String, String> data = new HashMap<String, String>();
            data.put("ISUPDATE", "true");
            String FIRSTNAME = "";
            String LASTNAME = "";
            if (null != editFullname && !"".equals(editFullname)) {
                LASTNAME = editFullname.substring(0, 1);
                if (editFullname.length() > 1)
                    FIRSTNAME = editFullname.substring(1);
            }
            data.put("FIRSTNAME", FIRSTNAME);
            data.put("LASTNAME", LASTNAME);
            data.put("MOBTEL", editMobilephone);
            data.put("USERID", editUsername);
            Map<String, Object> msg = this.getSyncConfigWS()
                    .synchronizeUserToPlatform(data);
            if (Boolean.valueOf(msg.get("success").toString())) {
                // 放进集合进行操作
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("editFullname", editFullname);
                param.put("editMobilephone", editMobilephone);
                param.put("editTelephone", editTelephone);
                param.put("editEmail", editEmail);
                param.put("editPosition", editPosition);
                param.put("editFax", editFax);
                param.put("editCode", editCode);
                param.put("editUserid", editUserid);
                param.put("editOrgid", orgId);
                userDao.editUser(param);

                // 机构同步到openfire----------------------------------------
                if (orgId > 0) {
                    Map<String, Map<String, String>> companyUserInitInfo = null;
                    Object obj = CacheUtils.get(this.getCompLocator(),
                            "companyUserInitInfo" + editCompid);
                    if (obj != null) {
                        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                        if (companyUserInitInfo.get(editUsername) != null) {
                            companyUserInitInfo.get(editUsername).put("ORGID",
                                    orgId + "");
                            CacheUtils.set(this.getCompLocator(),
                                    "companyUserInitInfo" + editCompid,
                                    companyUserInitInfo);
                        }
                    }
                    BroadcastUtils
                            .broadcast(
                                    editUsername,
                                    "company" + editCompid + "@broadcast."
                                            + BroadcastUtils.openfireServerName,
                                    editUsername + "broadcast-status" + orgId,
                                    cn.flying.rest.onlinefile.utils.Message.Type.GROUP_CHAT,
                                    editFullname);
                }
                // 机构同步到openfire----------------------------------------
                // 放入缓存----------------------------------
                Map<String, Map<String, String>> companyUserInitInfo = null;
                Object obj = CacheUtils.get(this.getCompLocator(),
                        "companyUserInitInfo" + editCompid);
                if (obj != null) {
                    companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                    if (companyUserInitInfo.get(editUsername) != null) {
                        companyUserInitInfo.remove(editUsername);
                    }
                    companyUserInitInfo.put(editUsername,
                            userDao.getOneUserInfo(editUserid + ""));
                    CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                            + editCompid, companyUserInitInfo);
                } else {
                    companyUserInitInfo = userDao.getUserInitInfo(editCompid
                            + "");
                    CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                            + editCompid, companyUserInitInfo);
                }
                // 放入缓存----------------------------------
                // 日志------------------------------------------
                HashMap<String, String> log = new HashMap<String, String>();
                log.put("userid", params.get("username"));
                log.put("username", params.get("username"));
                log.put("ip", params.get("ip"));
                log.put("module", "管理员修改信息");
                log.put("operate", "管理员修改信息");
                log.put("companyName",
                        companyUserInitInfo.get(params.get("username")).get(
                                "COMPANYNAME"));
                // login 用户登录 access 功能访问 operation 功能操作
                log.put("type", "operation");
                log.put("loginfo", "管理员修改信息,用户中文名为【" + editFullname + "】ID为【"
                        + editUserid + "】邮箱为【" + editEmail + "】");
                logutils.saveBaseLog(this.getCompLocator(), log);
                // 日志------------------------------------------
                reutrnMap.put("issuccess", true);
            } else {
                reutrnMap.put("issuccess", false);
                reutrnMap.put("msg", msg.get("msg"));
            }
        }

        return reutrnMap;
    }

    @Override
    public String validateAndParseExcel(HttpServletRequest request,
            HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(2048);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        String userId = null;
        List<FileItem> fileItemList = new ArrayList<FileItem>();
        List<FileItem> items;
        try {
            items = upload.parseRequest(request);
            for (FileItem item : items) {
                if (item.getFieldName().equals("userId")) {
                    userId = item.getString("UTF-8");
                } else if (!item.isFormField()) {
                    fileItemList.add(item);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        imap.remove(userId);
        String msg = veryfyFileItem(fileItemList, "excel");// 校验
        if (msg.length() == 0) {
            msg = parseExcel(fileItemList, userId);// 解析文件
            if (msg.length() == 0) {
                msg = "success";
            }
        }
        return msg;
    }

    /**
     * 导入上传文件验证
     * 
     * @param fileItemList
     * @return
     */
    private String veryfyFileItem(List<FileItem> fileItemList, String type) {
        String msg = "";
        if (fileItemList.size() == 0) {
            msg = "未找到所上传文件，请确认已选择文件！";
            return msg;
        }
        boolean flag = false;
        for (FileItem item : fileItemList) {
            String fileName = item.getName();
            if ("".equals(fileName)) {
                continue;
            }
            flag = true;
            if (item.getSize() <= 0) {
                msg = "文件\"" + fileName + "\"文件内容为空！";// 文件 的文件内容为空
                break;
            }
            if ("excel".equals(type)) {
                if (!ParseUtil.verifyExcel2007(fileName)) {
                    msg = "文件\"" + fileName + "\"不是2007版EXCEL文件，请先下载导入模板！";
                    break;
                }
            } else {
                msg = "文件\"" + fileName + "\"不支持的文件格式！";
                break;
            }
        }
        if (!flag) {
            msg = "未找到所上传文件，请确认已选择文件！";
        }
        return msg;
    }

    /**
     * 上传excel到服务器
     * 
     * @param fileItemList
     * @return 错误信息
     */
    private String parseExcel(List<FileItem> fileItemList, String userId) {
        String msg = "";
        Map<String, String> filePaths = new LinkedHashMap<String, String>();
        msg = preParseExcel(fileItemList, filePaths, userId);// 上传临时数据文件到服务器
        if (msg.length() > 0) {
            return msg;
        }
        for (String excelFile : filePaths.keySet()) {
            ImportInfo info = new ImportInfo();
            if (null != imap.get(userId)) {
                info = imap.get(userId);
            }
            info.setFilePath(excelFile);
            info.setUserId(userId);
            imap.put(userId, info);
        }
        return msg;
    }

    /**
     * 上传文件.
     * 
     * @param fileItemList
     * @return
     */
    private String preParseExcel(List<FileItem> fileItemList,
            Map<String, String> filePaths, String userId) {
        String msg = "";
        String tempPath = getTempPath();
        for (FileItem item : fileItemList) {
            if (item.getName() == null || "".equals(item.getName())) {
                continue;
            }
            String fileFullPath = _upLoadFile(item, tempPath, userId);
            filePaths.put(fileFullPath, item.getFieldName());
        }
        return msg;
    }

    /**
     * 上传文件，以userid+文件名重新命名文件
     * 
     * @param file
     * @param exportFilePath
     * @return
     */
    private String _upLoadFile(FileItem file, String exportFilePath,
            String userId) {
        String fileName = file.getName();
        if (fileName.lastIndexOf("\\") > 0) {
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        }
        fileName = userId + fileName;
        String fileFullPath = exportFilePath + fileName;
        try {
            File dir = new File(exportFilePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            InputStream fileIn = null;
            FileOutputStream fileOut = null;
            fileIn = file.getInputStream();
            fileOut = new FileOutputStream(fileFullPath);
            int len = 0;
            byte[] buffer = new byte[8192];
            while ((len = fileIn.read(buffer, 0, 8192)) != -1) {
                fileOut.write(buffer, 0, len);
            }
            fileOut.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return fileFullPath;
    }

    /**
     * 临时数据存放的位置
     * 
     * @return
     */
    private String getTempPath() {
        String classPath = this.getClass().getProtectionDomain()
                .getCodeSource().getLocation().getPath();
        int pos = classPath.indexOf("WEB-INF");
        String web_infPath = classPath.substring(0, pos); // this.getParentPath(ClassLoader.getSystemResource("").getPath())
        return web_infPath.toString() + "data/";
    }

    @Override
    public Map<String, Object> initImportStep2ByPage(
            Map<String, String> params, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = params.get("userid");
        Integer nowPage = Integer.parseInt(params.get("nowPageStep2"));

        ImportInfo info = new ImportInfo();
        if (null != imap.get(userId)) {
            info = imap.get(userId);
        }
        String excelPath = info.getFilePath();
        DataTable dataTable = new DataTable();
        HashMap<String, Object> pageMap = new HashMap<String, Object>();
        dataTable.initByXlsxExcel(excelPath, session);
        if (info.getTotal() <= 0) {
            info.setPageCount(DataTable.pageCount);
            info.setTotal(dataTable.getRecords().size());
            info.setPageNum((info.getTotal() % info.getPageCount()) == 0 ? (info
                    .getTotal() / info.getPageCount()) : (info.getTotal()
                    / info.getPageCount() + 1));
        }
        pageMap.put("startNo", (nowPage - 1) * DataTable.pageCount + 1);
        pageMap.put("endNo", nowPage * DataTable.pageCount > dataTable
                .getRecords().size() ? dataTable.getRecords().size() : nowPage
                * DataTable.pageCount);
        pageMap.put("total", dataTable.getRecords().size());
        pageMap.put("pageCount", DataTable.pageCount);
        Map<String, String> map = info.getImportMap();
        if (null != map && null != map.get(nowPage.toString())
                && map.get(nowPage.toString()).equals("true")) {
            pageMap.put("isImport", "true");
        } else {
            pageMap.put("isImport", "false");
        }
        List<Map<String, String>> dataMapList = dataTable.getDataMapListStep2(
                (nowPage - 1) * DataTable.pageCount + 1, nowPage
                        * DataTable.pageCount);
        pageMap.put("list", dataMapList);
        List<HashMap<String, String>> columnMapList = new ArrayList<HashMap<String, String>>();
        List<Header> list = dataTable.getHeaders();
        for (Header header : list) {
            HashMap<String, String> columnMap = new HashMap<String, String>();
            if (null == header.getName() || "".equals(header.getName().trim())) {
                continue;
            }
            columnMap.put("name", header.getName());
            columnMapList.add(columnMap);
        }
        // 日志------------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(),
                "companyUserInitInfo" + params.get("companyId"));
        if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        } else {
            companyUserInitInfo = userDao.getUserInitInfo(params
                    .get("companyId"));
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("userid", params.get("userid"));
        log.put("username", params.get("userid"));
        log.put("ip", params.get("ip"));
        log.put("module", "解析excel");
        log.put("operate", "解析excel");
        log.put("companyName", companyUserInitInfo.get(params.get("userid"))
                .get("COMPANYNAME"));
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "access");
        log.put("loginfo", "解析excel中的用户");
        logutils.saveBaseLog(this.getCompLocator(), log);
        // 日志------------------------------------------
        pageMap.put("headerList", columnMapList);
        return pageMap;
    }

    @Override
    public Map<String, Object> realImport(Map<String, Object> params) {
        List<Map<String, String>> list = (List<Map<String, String>>) params
                .get("list");
        Map<String, Object> rtMap = new HashMap<String, Object>();
        String fromfullname = params.get("fromfullname").toString();
        String nowPageStep2 = params.get("nowPageStep2").toString();
        int companyid = Integer.parseInt(params.get("companyid").toString());
        String userid = params.get("userid").toString();
        String companyName = userDao.getCompanyNameById(companyid + "");
        String subject = "欢迎来到东方飞扬在线分享系统";
        HashMap<Integer, String> emailcontents = new HashMap<Integer, String>();
        HashMap<Integer, String> emails = new HashMap<Integer, String>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            String fullNameImport = map.get("fullNameImport");
            String emailImport = map.get("emailImport");
            String mobilephoneImport = map.get("mobilephoneImport");
            String teleohoneImport = map.get("teleohoneImport");
            String faxImport = map.get("faxImport");
            String positionImport = map.get("positionImport");
            String code = MD5(fullNameImport);
            // 放进集合进行操作
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("fullNameImport", fullNameImport);
            param.put("emailImport", emailImport);
            param.put("mobilephoneImport", mobilephoneImport);
            param.put("teleohoneImport", teleohoneImport);
            param.put("faxImport", faxImport);
            param.put("positionImport", positionImport);
            param.put("codeImport", code);
            param.put("companyidImport", companyid);
            int id = userDao.importUser(param);

            // 保存用户成功后发送邮件进行激活
            if (id != -1) {
                String activateURL = null;
                try {
                    activateURL = BroadcastUtils.http_schema + "//"
                            + params.get("ipandport").toString()
                            + "/onlinefile/0/default/ESUserInfo/toActive?id="
                            + id + "&fullname="
                            + URLEncoder.encode(fullNameImport, "UTF-8")
                            + "&companyid=" + companyid + "&code=" + code
                            + "&email=" + emailImport + "&companyName="
                            + URLEncoder.encode(companyName, "UTF-8")
                            + "&className=" + "" + "&classId=" + "";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
                String content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
                        + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
                        + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
                        + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
                        + fromfullname
                        + "</strong>邀请您使用Onlinefile，点击下面的绿色按钮加入<strong>"
                        + companyName
                        + "</strong>团队:</p>"
                        + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
                        + "<a href=\""
                        + activateURL
                        + "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">立即激活</a>"
                        + "</p>"
                        + "<p style=\"line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; word-break: break-word;\">如果按钮无法点击，请复制链接至浏览器打开:<a>"
                        + activateURL
                        + "</a></p>"
                        + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
                        + "<p style=\"font-size: 18px; line-height: 24px;\"><strong>使用Onlinefile为您的团队工作</strong> – 您可以在线分享和收藏，日常交流.</p>"
                        // +"<p style=\"font-size: 18px;text-align: center; line-height: 24px;\">您的飞扬用户名是 <strong>"+username+"</strong>!"
                        + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
                        + "<p style=\"font-size: 0.9rem; line-height: 20px; color: rgb(113, 113, 113); text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">-请在收取邮件后7天内激活-</p>"
                        + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
                        + "祝好运!<br>"
                        + "--"
                        + fromfullname
                        + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
                        + "</div>"
                        + "</p>"
                        + "</div></td></tr></tbody></table>";
                emails.put(id, emailImport);
                emailcontents.put(id, content);
                // 日志------------------------------------------
                Map<String, Map<String, String>> companyUserInitInfo = null;
                Object obj = CacheUtils.get(this.getCompLocator(),
                        "companyUserInitInfo"
                                + params.get("companyid").toString());
                if (obj != null) {
                    companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                } else {
                    companyUserInitInfo = userDao.getUserInitInfo(params.get(
                            "companyid").toString());
                }
                HashMap<String, String> log = new HashMap<String, String>();
                log.put("userid", params.get("username").toString());
                log.put("username", params.get("username").toString());
                log.put("ip", params.get("ip").toString());
                log.put("module", "excel导入的用户发送邮件");
                log.put("operate", "excel导入的用户发送邮件");
                log.put("companyName",
                        companyUserInitInfo.get(params.get("username")).get(
                                "COMPANYNAME"));
                // login 用户登录 access 功能访问 operation 功能操作
                log.put("type", "access");
                log.put("loginfo", "excel导入的用户发送邮件给【" + fullNameImport
                        + "】邮箱为【" + emailImport + "】");
                logutils.saveBaseLog(this.getCompLocator(), log);
                // 日志------------------------------------------
            }
        }
        if (!emails.isEmpty()) {
            /** 调用线程进行邮件发送 **/
            ThreadPoolManager m = ThreadPoolManager.getInstance();
            try {
                m.runTask(new sendMailCallable(companyid + "", emails,
                        emailcontents, subject, true, true));
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        rtMap.put("success", true);
        ImportInfo info = imap.get(userid);
        Map<String, String> importMap = new HashMap<String, String>();
        if (null != info.getImportMap()) {
            importMap = info.getImportMap();
        }
        importMap.put(nowPageStep2, "true");
        info.setImportMap(importMap);
        if (info.getImportMap().size() == info.getPageNum()) {
            info.setImportOk(true);
        }
        rtMap.put("importOk", info.isImportOk());// 全部导入
        if (info.isImportOk()) {
            imap.remove(userid);
        }
        return rtMap;
    }

    @Override
    public Map<String, Object> closeImport(Map<String, Object> map) {
        Map<String, Object> rtMap = new HashMap<String, Object>();
        String userid = map.get("userid").toString();
        imap.remove(userid);
        rtMap.put("success", true);
        return rtMap;
    }

    public String getUserNameById(String userID) {
        if ("0".equals(userID)) {
            return "fyBot";
        }
        Object username = CacheUtils.get(this.getCompLocator(),
                "userIdByNameKey_" + userID);
        if (username == null) {
            Map<String, String> map = userDao.getUserById(userID);
            username = map.get("USERNAME");
            CacheUtils.set(getCompLocator(), "userIdByNameKey_" + userID,
                    username.toString());
        }
        return username.toString();
    }

    // public static void main(String[] args) {
    // // Md5PasswordEncoder md = new Md5PasswordEncoder();
    // // System.out.println(md.encodePassword("admin", "admin"));
    // SimpleDateFormat format = null ;
    // String createtime = null;
    // format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    // createtime = format.format(System.currentTimeMillis());
    // //System.out.println(i);
    // try {
    // Date d = format.parse(createtime);
    // System.out.println(d.getTime());
    // } catch (ParseException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    @Override
    public Map<String, Object> forgetpassword(Map<String, Object> params) {
        Map<String, Object> rtMap = new HashMap<String, Object>();
        HashMap<Integer, String> emailcontents = new HashMap<Integer, String>();
        HashMap<Integer, String> emails = new HashMap<Integer, String>();
        Map<String, Object> map = userDao
                .checkUserExistByEmailAndUserName(params);
        // 根据用户名和邮箱检验该用户是否存在 存在的话就发送邮件不存在的话就返回 并提示
        if (!Boolean.valueOf(map.get("success").toString())) {
            rtMap.put("success", false);
            return rtMap;
        } else {// 存在就发送邮件
            String username = params.get("username").toString();
            String email = params.get("email").toString();
            String fullname = map.get("fullname").toString();
            String subject = "飞扬在线分享系统-忘记密码/找回";
            try {
                String activateURL = BroadcastUtils.http_schema
                        + "//"
                        + params.get("ipandport")
                        + "/onlinefile/0/default/ESUserInfo/toforgetpassword?id="
                        + map.get("id") + "&fullname="
                        + URLEncoder.encode(fullname, "UTF-8") + "&email="
                        + email + "&companyid="
                        + map.get("companyId").toString() + "&username="
                        + username;
                String content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
                        + "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
                        + "<h2 style=\"color: #2ab27b; margin: 0 0 12px; line-height: 30px;\">您好!</h2>"
                        + "<p style=\"font-size: 18px; line-height: 24px;\">您好<strong>"
                        + fullname
                        + "</strong> 您在<strong>"
                        + map.get("createtime")
                        + "</strong>提交了重置密码的请求，点击下面按钮进行重置:</p>"
                        + "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
                        + "<a href=\""
                        + activateURL
                        + "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">立即重置</a>"
                        + "</p>"
                        + "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
                        + "<p style=\"font-size: 18px; line-height: 24px;\"><strong>使用飞扬为您的团队工作</strong> – 您可以在线分享和收藏，日常交流.</p>"
                        // +"<p style=\"font-size: 18px;text-align: center; line-height: 24px;\">您的飞扬用户名是 <strong>"+username+"</strong>!"
                        + "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
                        + "<p style=\"font-size: 0.9rem; line-height: 20px; color: rgb(113, 113, 113); text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">-(该邮件48小时内有效)-</p>"
                        + "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
                        + "祝好运!<br>"
                        + "--"
                        + "东方飞扬"
                        + "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
                        + "</div>"
                        + "</p>"
                        + "</div></td></tr></tbody></table>";
                emailcontents.put(Integer.parseInt(map.get("id").toString()),
                        content);
                emails.put(Integer.parseInt(map.get("id").toString()), email);
                rtMap.put("success", true);
                // 日志------------------------------------------
                Map<String, Map<String, String>> companyUserInitInfo = null;
                Object obj = CacheUtils
                        .get(this.getCompLocator(), "companyUserInitInfo"
                                + map.get("companyId").toString());
                if (obj != null) {
                    companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                } else {
                    companyUserInitInfo = userDao.getUserInitInfo(map.get(
                            "companyId").toString());
                }
                HashMap<String, String> log = new HashMap<String, String>();
                log.put("userid", params.get("username").toString());
                log.put("username", params.get("username").toString());
                log.put("ip", params.get("ip").toString());
                log.put("module", "忘记密码");
                log.put("companyName",
                        companyUserInitInfo.get(params.get("username")).get(
                                "COMPANYNAME") == "" ? map.get("companyId")
                                .toString() : companyUserInitInfo.get(
                                params.get("username")).get("COMPANYNAME"));
                // login 用户登录 access 功能访问 operation 功能操作
                log.put("type", "operation");
                log.put("operate", "忘记密码");
                log.put("loginfo", "该用户忘记密码申请重置,用户名为【" + params.get("username")
                        + "】");
                logutils.saveBaseLog(this.getCompLocator(), log);
                // 日志------------------------------------------

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            if (!emails.isEmpty()) {
                /** 调用线程进行邮件发送 **/
                ThreadPoolManager m = ThreadPoolManager.getInstance();
                try {
                    m.runTaskforFree(new sendMailCallable(null, emails,
                            emailcontents, subject, true, false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }

        }
        return rtMap;
    }

    @Override
    public Map<String, Object> toforgetpassword(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        String createtime = userDao.getCreateTime(params);
        if (createtime != null) {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String nowtime = format.format(System.currentTimeMillis());
            try {
                Long createLong = format.parse(createtime).getTime();
                Long nowLong = format.parse(nowtime).getTime();
                Long pars = (nowLong - createLong) / 1000 * 60 * 60;// (1000 *
                                                                    // 60 * 60)
                if (pars > 86400000) {// 暂定24小时
                    reutrnMap.put("timeout", true);
                    reutrnMap.put("issuccess", true);
                    return reutrnMap;
                } else {
                    reutrnMap.put("timeout", false);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            // 日志------------------------------------------
            Map<String, Map<String, String>> companyUserInitInfo = null;
            Object obj = CacheUtils.get(this.getCompLocator(),
                    "companyUserInitInfo" + params.get("companyid").toString());
            if (obj != null) {
                companyUserInitInfo = (Map<String, Map<String, String>>) obj;
            } else {
                companyUserInitInfo = userDao.getUserInitInfo(params.get(
                        "companyid").toString());
            }
            HashMap<String, String> log = new HashMap<String, String>();
            log.put("userid", params.get("username").toString());
            log.put("username", params.get("username").toString());
            log.put("ip", params.get("ip").toString());
            log.put("module", "忘记密码");
            log.put("companyName",
                    companyUserInitInfo.get(params.get("username")).get(
                            "COMPANYNAME") == "" ? params.get("companyId")
                            .toString() : companyUserInitInfo.get(
                            params.get("username")).get("COMPANYNAME"));
            // login 用户登录 access 功能访问 operation 功能操作
            log.put("type", "operation");
            log.put("operate", "忘记密码");
            log.put("loginfo",
                    "验证该邮件是否超过规定的修改时间,用户名为【" + params.get("username") + "】");
            logutils.saveBaseLog(this.getCompLocator(), log);
            // 日志------------------------------------------
        }
        return reutrnMap;
    }

    @Override
    public Map<String, Object> fromforgetpasswordtochange(
            Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        if (params.get("password") != null && params.get("password") != "") {
            Md5PasswordEncoder md5 = new Md5PasswordEncoder();
            String newPassword = md5.encodePassword(params.get("password"),
                    params.get("username"));
            if (userDao.modifyPassword(newPassword, params.get("id"))) {
                // 同步-----------------------------
                Map<String, String> param = new HashMap<String, String>();
                param.put("ids", "1");
                param.put("userId", params.get("id"));
                param.put("userids", params.get("id"));
                param.put("password", params.get("password"));
                param.put("remoteAddr", params.get("ip"));
                getUserRegistrationServer().resetUsersPassword(param);
                reutrnMap.put("success", true); // 1代表成功
                reutrnMap.put("isPasswordValid", "true");
                // 日志------------------------------------------
                Map<String, Map<String, String>> companyUserInitInfo = null;
                Object obj = CacheUtils.get(this.getCompLocator(),
                        "companyUserInitInfo" + params.get("companyid"));
                if (obj != null) {
                    companyUserInitInfo = (Map<String, Map<String, String>>) obj;
                } else {
                    companyUserInitInfo = userDao.getUserInitInfo(params
                            .get("companyid"));
                }
                HashMap<String, String> log = new HashMap<String, String>();
                log.put("userid", params.get("id"));
                log.put("username", params.get("username"));
                log.put("ip", params.get("ip"));
                log.put("module", "重置密码");
                // login 用户登录 access 功能访问 operation 功能操作
                log.put("type", "operation");
                log.put("operate", "重置密码");
                log.put("companyName",
                        companyUserInitInfo.get(params.get("username")).get(
                                "COMPANYNAME"));
                log.put("loginfo", "重置密码,用户名为【" + params.get("username") + "】");
                logutils.saveBaseLog(this.getCompLocator(), log);
                // 日志------------------------------------------
            } else {
                reutrnMap.put("success", false);
            }
        }
        return reutrnMap;
    }

    public void getLoginLog(HttpServletRequest request,
            HttpServletResponse response) {// (start-1)*limit
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", request.getParameter("userid"));
        int start = (Integer.parseInt(request.getParameter("start")) - 1)
                * Integer.parseInt(request.getParameter("limit"));
        map.put("start", start + "");
        map.put("limit", request.getParameter("limit"));
        List<Map<String, String>> lst = logutils.getLoginLog(
                this.getCompLocator(), map);
        String callback = request.getParameter("callback");
        Map<String, Object> result = new HashMap<String, Object>();
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("userid", request.getParameter("userid"));
        map1.put("start", request.getParameter("start"));
        map1.put("limit", request.getParameter("limit"));
        String count = logutils.getLoginLogCount(this.getCompLocator(), map1);
        result.put("lst", lst);
        Integer counts = (Integer.parseInt(count) - 1)
                / Integer.parseInt(request.getParameter("limit")) + 1;
        result.put("totle", counts + "");// ((allCount-1)/pageSize)+1
        writeJson(response, callback, new Gson().toJson(result));
    }

    // 查询所用用户的操作记录
    public void getSafetyLog(HttpServletRequest request,
            HttpServletResponse response) {// (start-1)*limit
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", request.getParameter("userid"));
        int start = (Integer.parseInt(request.getParameter("start")) - 1)
                * Integer.parseInt(request.getParameter("limit"));
        map.put("start", start + "");
        map.put("limit", request.getParameter("limit"));
        List<Map<String, String>> lst = logutils.getSafetyLog(
                this.getCompLocator(), map);
        String callback = request.getParameter("callback");
        Map<String, Object> result = new HashMap<String, Object>();
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("userid", request.getParameter("userid"));
        map1.put("start", request.getParameter("start"));
        map1.put("limit", request.getParameter("limit"));
        String count = logutils.getsafetyLogCount(this.getCompLocator(), map1);
        result.put("lst", lst);
        Integer counts = (Integer.parseInt(count) - 1)
                / Integer.parseInt(request.getParameter("limit")) + 1;
        result.put("totle", counts + "");// ((allCount-1)/pageSize)+1
        writeJson(response, callback, new Gson().toJson(result));
    }

    private void writeJson(HttpServletResponse response, String callback,
            String json) {
    	
    	
        response.setContentType("text/javascript;charset=UTF-8");
        String data ="";
        if (callback != null && "androidInter".equals(callback)) {
        	data =json;
        }else{
        	data = super.jsonpCallbackWithString(callback, json);
        }
        try {
            response.getWriter().println(data);
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    private void output(HttpServletRequest request,
            HttpServletResponse response, JSONObject json) {
        response.setContentType("text/javascript;charset=UTF-8");
        StringBuffer output = new StringBuffer(100);
        output.append(request.getParameter("callback")).append("(");
        output.append(json.toJSONString()).append(");");
        try {
            response.getWriter().println(output);
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    class sendOneMailCallable implements Callable<Boolean> {
        String email;
        String emailcontent;
        String subject;

        sendOneMailCallable(String email, String emailcontent, String subject) {
            this.email = email;
            this.emailcontent = emailcontent;
            this.subject = subject;
        }

        public Boolean call() throws Exception {
            boolean isOk = sendHtmlMail(email, subject, emailcontent, true);
            return true;
        }
    }

    @Override
    public Map<String, Object> verifyMailbox(Map<String, String> params) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        String emailStr = params.get("EMAIL");
        String companyId = params.get("companyId");
        HashMap<String, Object> retMap = userDao.verifyMailbox(companyId,
                emailStr);
        reutrnMap.put("verifyMailbox", retMap.get("state"));
        return reutrnMap;
    }

    /**
     * longjunhao 20150819 修改逻辑</br> 验证多个邮箱在某个company中的状态
     * 
     * @return 返回对应的状态的邮箱列表 resendList newCompanyInviteList firstInviteList
     */
    @Override
    public HashMap<String, Object> verifyMailboxS(Map<String, String> params) {
        String isVerify = ""; // 记录已激活的邮箱
        String resendList = ""; // 需要重新发送
        String newCompanyInviteList = ""; // 新的企业邀请
        String firstInviteList = ""; // 第一次邀请
        boolean existActive = false; // 是否存在已激活的邮箱
        HashMap<String, Object> reutrnMap = new HashMap<String, Object>();
        String emailStrs = params.get("EMAIL").trim();
        String companyId = params.get("companyId");
        String[] emailStr = emailStrs.split(",");
        for (int i = 0; i < emailStr.length; i++) {
            // 首先判断邮箱是否存在
            HashMap<String, Object> retMap = userDao.verifyMailbox(companyId,
                    emailStr[i].trim());// 验证邮件是否已激活
            String state = (String) retMap.get("state");
            if ("active".equals(state)) {// 已激活用户
                isVerify += emailStr[i].trim() + ",";
                existActive = true;
            } else if ("nonactive".equals(state)) {// 存在但未激活用户
                resendList += emailStr[i].trim() + ",";
            } else if ("firstInvite".equals(state)) { // 不存在用户
                firstInviteList += emailStr[i].trim() + ",";
            } else if ("newCompanyInvite".equals(state)) { // 存在用户但是不在companyId中
                newCompanyInviteList += emailStr[i].trim() + ",";
            }
        }
        if (!"".equals(isVerify)) {
            isVerify = isVerify.substring(0, isVerify.length() - 1);
        }
        if (!"".equals(resendList)) {
            resendList = resendList.substring(0, resendList.length() - 1);
        }
        if (!"".equals(firstInviteList)) {
            firstInviteList = firstInviteList.substring(0,
                    firstInviteList.length() - 1);
        }
        if (!"".equals(newCompanyInviteList)) {
            newCompanyInviteList = newCompanyInviteList.substring(0,
                    newCompanyInviteList.length() - 1);
        }
        reutrnMap.put("verifyMailbox", existActive);// 标识是否存在已激活的邮箱
        reutrnMap.put("isVerify", isVerify);// 标识行，记录已激活过的邮箱列表
        reutrnMap.put("resendList", resendList); // 是否重新发送
        reutrnMap.put("newCompanyInviteList", newCompanyInviteList); // true:
                                                                        // 新的企业邀请
        reutrnMap.put("firstInviteList", firstInviteList); // true: 第一次邀请
        return reutrnMap;
    }


	public Boolean invitation(Map<String, String> map) {
		String subject = map.get("subject");
		String appendEmail = map.get("email");
		String[] emailArray = appendEmail.split("-");
		String emailcontent = map.get("emailcontent");
		String recommend_url = map.get("recommend_url");
		// String message =
		// "Onlinefile是一个简单好用的团队协作工具，我们团队使用它来跟踪任务进展情况，讨论问题，共享文件和文档，安排团队的日程，大家赶快加入吧。";
		String content = "<table id=\"body\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tbody><tr><td valign=\"top\"><div style=\"max-width: 600px; margin: 0 auto; padding: 0 12px;\">"
				+ "<div class=\"card\" style=\"background: white; border-radius: 0.5rem; padding: 2rem; margin-bottom: 1rem;\">"
				+ "<p style=\"font-size: 18px; line-height: 24px;\">"
				+ emailcontent
				+ ""
				+ "<p style=\"text-align: center; margin: 2rem 0 1rem;\">"
				+ "<a href=\""
				+ recommend_url
				+ "\" style=\"display: inline-block; padding: 14px 32px; background: #2ab27b; border-radius: 4px; font-weight: normal; letter-spacing: 1px; font-size: 20px; line-height: 26px; color: white; text-shadow: 0 1px 1px black; text-shadow: 0 1px 1px rgba(0,0,0,0.25); text-decoration: none;\">立即注册</a>"
				+ "</p>"
				+ "<p style=\"line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; word-break: break-word;\">如果按钮无法点击，请复制链接至浏览器打开:<a>"
				+ recommend_url
				+ "</a></p>"
				+ "<p style=\"font-size: 0.9rem; line-height: 20px; color: #AAA; text-align: center; margin: 0 auto 1rem; max-width: 320px; word-break: break-word;\">一直专注于在文档信息资源管理领域帮助用户获得成功，是中国领先的内容管理解决方案提供商.</p>"
				// +"<p style=\"font-size: 18px; line-height: 24px;text-align: center;\"><strong>"+message+"</p>"
				+ "</p><p style=\"font-size: 18px;text-align: center; line-height: 24px;\">更多飞扬产品请查看飞扬<a href=\"http://www.flyingsoft.cn\" style=\"font-weight: bold; color: #439fe0;\">官网</a></p>"
				+ "<p style=\"font-size: 18px;text-align: right; line-height: 24px;\">"
				+ "<img img=1\" width=\"0\" height=\"0\" style=\"width: 0; height: 0; position: absolute;\" alt=\"\">"
				+ "</div>" + "</p>" + "</div></td></tr></tbody></table>";
		
		// 调用线程进行邮件发送
		ThreadPoolManager m = ThreadPoolManager.getInstance();
		for (int i = 0; i < emailArray.length; i++) {
			String email = emailArray[i];
			try {
				m.runTaskforFree(new sendOneMailCallable(email, content,
						subject));
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return false;
			} catch (ExecutionException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				return false;
			}
		}
		return true;
	}


    public Map<String, String> getUserByUserId(String userId) {
        Map<String, String> reutrnMap = new HashMap<String, String>();
        reutrnMap = userDao.getUserByUserId(userId);
        reutrnMap.put("username", reutrnMap.get("username"));
        return reutrnMap;
    }

    public Map checkPasswordIsRight(Map<String, String> map) {
        Map<String, Object> reutrnMap = new HashMap<String, Object>();
        String companyId = map.get("companyId");
        String username = map.get("username");
        String userid = map.get("userid");
        String password = map.get("password");
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        String md5password = md5.encodePassword(password, username);
        boolean isOk = userDao.checkPasswordIsRight(username, userid,
                md5password);
        reutrnMap.put("isOk", isOk);
        return reutrnMap;
    }

    @Override
    public boolean saveCompanyUsers(Map<String, String> user) {
        return userDao.saveCompanyUsers(user);
    }

    public Map<String, String> getOneUserInfo(String id) {
        return userDao.getOneUserInfo(id);
    }

    @Override
    public List<HashMap<String, String>> getUserCompanys(String userid) {
        HashMap<String, List<String>> utc = new HashMap<String, List<String>>();
        HashMap<String, String> companyAdmin = new HashMap<String, String>();
        Object obj = CacheUtils.get(this.getCompLocator(), "ofuserCompanys");
        if (null != obj) {
            utc = (HashMap<String, List<String>>) obj;
        }
        obj = CacheUtils.get(this.getCompLocator(), "ofcompanyAdmin");
        if (null != obj) {
            companyAdmin = (HashMap<String, String>) obj;
        }
        Map<String, Set<String>> companyUserClassId = new HashMap<String, Set<String>>();
        obj = CacheUtils.get(this.getCompLocator(), "ofuserClassId");
        if (obj != null) {
            companyUserClassId = (Map<String, Set<String>>) obj;
        }
        HashMap<String, List<HashMap<String, String>>> companyUserIsStatus = new HashMap<String, List<HashMap<String, String>>>();
        obj = CacheUtils.get(this.getCompLocator(), "ofcompanyUserIsStatus");
        if (obj != null) {
            companyUserIsStatus = (HashMap<String, List<HashMap<String, String>>>) obj;
        }
        List<HashMap<String, String>> userCompanys = new ArrayList<HashMap<String, String>>();
        Map<String, Map<String, String>> companyInfo = getCompanyRegistWS()
                .getAllCompanyInfo();// 所有公司
        if(utc.get(userid) != null) {
        	for (String cid : utc.get(userid)) {
                
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("companyid", cid);
                item.put("companyName", companyInfo.get(cid).get("NAME"));
                item.put("companyImgurl", companyInfo.get(cid).get("IMGURL"));
                item.put("isAdmin", companyAdmin.get(cid)!=null && userid.equals(companyAdmin.get(cid))?"1":"0");
                Set<String> ids = companyUserClassId.get(cid);
                if(null != ids && ids.size()>0){
                	  for(String id : ids){
                      	if(id.contains(userid))
                      	 item.put("companyClassId", id);
                      }
                }
               
                List<HashMap<String, String>> companyUserIsStatuslist = companyUserIsStatus.get(userid);
                if(companyUserIsStatuslist!=null){
	                for (HashMap<String, String> companyUserIs : companyUserIsStatuslist) {
	                    for (String key : companyUserIs.keySet()) {
	                        if (key.equals(cid)) {
	                            item.put("companyUserIsStatus", companyUserIs.get(cid));
	                        }
	                    }
	                }
                }
                userCompanys.add(item);
           }
        }
        //  return userCompanys;
        return StringUtil.listMapSortByKeys(userCompanys, "isAdmin");
    }

    // 根据用户，密码获取用户详细信息
        @Override
        public Map<String, Object> loginGetUserParticular(Map<String, String> map) {
        	String ownCompanyId="";
        	String companyId="";
        	String activationCompanyId="";
        	String memoryCompanyId="";
            String userName = map.get("userName");
            String userPwd = map.get("userPwd");
            Map<String, String> userMap = new HashMap<String, String>();
            Map<String, Object> returnMap = new HashMap<String, Object>();
            //1.验证空
            if (userName != null && userPwd != null && ("".equals(userName.trim()) || "".equals(userPwd.trim()))) {
            	 returnMap.put("userinfo", "");
                 returnMap.put("message", "帐号或密码不能空!");
                 returnMap.put("success", "flase");
                 return returnMap;
            }
            	
            Md5PasswordEncoder md5 = new Md5PasswordEncoder();
            String md5password ="";
            if(map.get("isLogin")!=null && "1".equals(map.get("isLogin"))){
            	md5password = userPwd;
            }else{
            	md5password=md5.encodePassword(userPwd.trim(),userName.trim());
            }
            //2.验证密码是否正确
            String oldPasswordFromDatabase = userDao.getPassWordById(userName);
           
            if(null == oldPasswordFromDatabase || "".equals(oldPasswordFromDatabase)){
            	returnMap.put("userinfo", "");
                returnMap.put("message", "用户不存在!");
                returnMap.put("success", "flase");
                return returnMap;
            }
           
            if (oldPasswordFromDatabase != null && !oldPasswordFromDatabase.equals(md5password)) {
            	 returnMap.put("userinfo", "");
                 returnMap.put("message", "密码错误!");
                 returnMap.put("success", "flase");
                 return returnMap;
            }
            Map<String,String> parMap=this.getUserCompanyId(userName);
    		if(parMap!=null && parMap.get("ownCompanyId")!=null && !"".equals(parMap.get("ownCompanyId"))){
    			ownCompanyId=parMap.get("ownCompanyId");
    			companyId=ownCompanyId;
    		}
    		if(parMap!=null && parMap.get("memoryCompanyId")!=null && !"".equals(parMap.get("memoryCompanyId"))){
    			memoryCompanyId=parMap.get("memoryCompanyId");
    			companyId=memoryCompanyId;
    		}
    		if("".equals(companyId) && parMap!=null && parMap.get("activationCompanyId")!=null && !"".equals(parMap.get("activationCompanyId"))){
    			activationCompanyId=parMap.get("activationCompanyId");
    			companyId=activationCompanyId;
    		}
            if("".equals(companyId)){
            	 returnMap.put("userinfo", "");
                 returnMap.put("message", "帐号被禁用");
                 returnMap.put("success", "flase");
                 return returnMap;
            }
            map.put("userPwd", md5password);
            map.put("companyId", companyId);
            //5.获取用户信息
            userMap = userDao.LoginGetUserParticular(map);
            Blowfish blowfish = new Blowfish("vPaAjlpumvgC0n7");// 加密用的工具
            returnMap.put("usernamemd5code", blowfish.encrypt(userName));
            Map<String, String> singleflg = new HashMap<String, String>();
            singleflg.put("userid", userMap.get("id"));
            singleflg.put("companyId", userMap.get("companyId"));
            Map<String, String> obMap = userDao.getSingleSet(singleflg);
            returnMap.put("isupremind", obMap.get("isUpRemind")!=null?obMap.get("isUpRemind"):"1");
            returnMap.put("isdownremind", obMap.get("isDowRemind")!=null?obMap.get("isDowRemind"):"1");
            returnMap.put("isOpenSpace", obMap.get("isOpenSpace")!=null?obMap.get("isOpenSpace"):"1");
            returnMap.put("isOpenGroup", obMap.get("isOpenGroup")!=null?obMap.get("isOpenGroup"):"1");
            returnMap.put("isEnterSend", obMap.get("isEnterSend")!=null?obMap.get("isEnterSend"):"1");
            
           // this.getUserInitInfo(companyId,userName);
            returnMap.put("userinfo",userMap);
            returnMap.put("message", "登录成功!");
            returnMap.put("success", "true");
            return returnMap;
        }

    public void agreenInSideCompany(HttpServletRequest request,
            HttpServletResponse response) {
        String companyId = request.getParameter("companyId");
        String userId = request.getParameter("userId");
        String username = request.getParameter("username");
        String callback = request.getParameter("callback");
        Map<String,String> userInfo = new HashMap<String,String>();
        boolean isOk = false;
        if (userDao.agreenInSideCompany(companyId, userId)) {
            
            List<Integer> ids = new ArrayList<Integer>();
            ids.add(Integer.parseInt(userId));
            userDao.updateCompanyUserStatus(companyId, ids, 1);
            isOk = true;
            Map<String, Set<String>> companyUserClassId = null;
            Object obj = CacheUtils.get(this.getCompLocator(), "ofuserClassId");
            if (obj != null) {
                companyUserClassId = (Map<String, Set<String>>) obj;
                Set<String> userIds = companyUserClassId.get(companyId);
                for(Iterator<String> it=userIds.iterator();it.hasNext();){
                	if(it.next().equals(userId)){
                		it.remove();
                		companyUserClassId.put(companyId, userIds);
            		}
                }
                CacheUtils.set(this.getCompLocator(), "ofuserClassId",companyUserClassId);
            }
            Map<String, Map<String, String>> companyUserInitInfo = null;
            Object obj1 = CacheUtils.get(this.getCompLocator(),
                    "companyUserInitInfo" + companyId);
            if (obj1 != null) {
                companyUserInitInfo = (Map<String, Map<String, String>>) obj1;
                if (companyUserInitInfo.get(username) != null) {
                    companyUserInitInfo.remove(username);
                }
                userInfo = userDao.getOneUserInfo(userId);
                companyUserInitInfo.put(username,userInfo);
                CacheUtils.set(this.getCompLocator(), "companyUserInitInfo"
                        + companyId, companyUserInitInfo);
            }
            /**20151016 xiayongcai 加入企业用户激活缓存*/
            HashMap<String, List<HashMap<String, String>>> companyUserIsStatus = new HashMap<String, List<HashMap<String, String>>>() ;
            HashMap<String, String> companyIsStatus = new HashMap<String, String>() ;
            obj = CacheUtils.get(this.getCompLocator(), "ofcompanyUserIsStatus") ;
            if (null != obj) {
                companyUserIsStatus = (HashMap<String, List<HashMap<String, String>>>) obj ;
            }
            if(companyUserIsStatus.get(userId) == null){
                companyUserIsStatus.put(userId,new ArrayList<HashMap<String, String>>()) ;
                companyIsStatus.put(companyId, "1");
                companyUserIsStatus.get(userId).add(companyIsStatus) ;
            }else{
                for(HashMap<String, String> companyIsSdtatusd : companyUserIsStatus.get(userId)){
                    companyIsSdtatusd.put(companyId, "1");
                }
            }
            CacheUtils.set(this.getCompLocator(), "ofcompanyUserIsStatus", companyUserIsStatus);
        }
        
       
        //getChatWS().addUserIntoGroup(data);
        List<HashMap<String, String>> inviteList = userDao.getInviteDetail( userId, companyId);
        if (inviteList.size() > 0) {
            for (HashMap<String, String> invite : inviteList) {
                String returnStr = null;
                String arg = null;
                /*Map<String, String> data = new HashMap<String, String>();
                data.put("userId", userId);
                data.put("groupId", invite.get("groupid"));
                data.put("companyId", companyId);
                data.put("groupflag", invite.get("flag"));
                data.put("username", userInfo.get("USERNAME"));
                data.put("fullname", userInfo.get("FULLNAME"));*/
                // 插入分组
                if (/*getChatWS().addUserIntoGroup(data)*/getChatWS().inviteUserIntoGroup(userId, companyId,
                        invite.get("groupid"))) {
                    // 插入openfire
                    arg = "secret=flyingsoft&type=reset_group&groups="
                            + invite.get("flag") + "&addgroupusernames="
                            + username.replace("@", "\\40");
                    returnStr = post(arg);
                }
                // 删除invite表中的已经处理的字段
                userDao.deleteInvite(companyId, userId);

            }
        }
        JSONObject json = new JSONObject();
        json.put("isOk", isOk);
     // output(request, response, json);
        writeJson(response, callback, new Gson().toJson(json));
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
            logger.error(e.getMessage());
        }
        return mag ;
    }

    public void noAgreenInSideCompany(HttpServletRequest request,
            HttpServletResponse response) {
        String companyId = request.getParameter("companyId");
        String userId = request.getParameter("userId");
        String username = request.getParameter("username");
        String callback = request.getParameter("callback");
        boolean isOk = false;
        if (userDao.noAgreenInSideCompany(companyId, userId)) {
            isOk = true;
            Map<String, Set<String>> companyUserClassId = null;
            Object obj = CacheUtils.get(this.getCompLocator(), "ofuserClassId");
            if (obj != null) {
                companyUserClassId = (Map<String, Set<String>>) obj;
                
                Set<String> userIds = companyUserClassId.get(companyId);
                if(null != userIds){
                	  for(Iterator<String> it=userIds.iterator();it.hasNext();){
                      	if(it.next().contains(userId)){
                  			it.remove();
                      		companyUserClassId.put(companyId, userIds);
                  		}
                      }
                }
                CacheUtils.set(this.getCompLocator(), "ofuserClassId",companyUserClassId);
            }
            obj = CacheUtils.get(this.getCompLocator(), "ofuserCompanys");
            HashMap<String, List<String>> utc = new HashMap<String, List<String>>();
            List<String> removelist = new ArrayList<String>();
            if (null != obj) {
                utc = (HashMap<String, List<String>>) obj;
                for (String uc : utc.get(userId)) {
                    if (uc.equals(companyId)) {

                        removelist.add(uc);
                    }
                }
                utc.get(userId).removeAll(removelist);
                CacheUtils.set(this.getCompLocator(), "ofuserCompanys", utc);
            }
            // 拒绝之后删除invite表中的邀请
            userDao.deleteInvite(companyId, userId);

        }
        
        
        JSONObject json = new JSONObject();
        json.put("isOk", isOk);
       // output(request, response, json);
        writeJson(response, callback, new Gson().toJson(json));
    }

    // 修改头像-上传图片
    @Override
    public Map<String, String> uploadImg(HttpServletRequest request) {
        String username = "";
        String companyid = "";
        String filePat = "";
        String ip = "";
        String oldImg = "";
        StringBuffer sbRealPath = null;
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        DateFormat format1 = new SimpleDateFormat("HH:mm:ss");
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("utf-8");
        File filePath = new File(androidUpImgUrl);
        try {
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    String fileEnd = fileName.substring(
                            fileName.lastIndexOf(".") + 1).toLowerCase();
                    long timeStart = format1.parse(format1.format(date))
                            .getTime();
                    String time = format.format(date);
                    // 创建文件唯一名称
                    // uuid = UUID.randomUUID().toString().replace("-", "");
                    // 真实上传路径
                    sbRealPath = new StringBuffer();
                    sbRealPath.append(time + timeStart + ((int) (Math.random() * 900) + 100)).append(".").append(fileEnd);
                    filePat = "files/headimage/" + sbRealPath.toString();
                    // 写入文件
                    File file = new File(filePath + "\\" + sbRealPath.toString());
                    item.write(file);
                    File file1 = new File(filePath + "\\"+MD5(username).toString()+".jpeg");
                    int     bytesum     =     0;    
                    int     byteread     =     0;    
                    InputStream inStream  =new FileInputStream(file);     
                    FileOutputStream fs=new FileOutputStream(file1);    
                    byte[]     buffer =new byte[1444];    
                    while ((byteread=inStream.read(buffer))!=-1){    
                            bytesum+=byteread;        
                            fs.write(buffer,0,byteread);    
                    }    
                    inStream.close();    
                  //  item.write(file1);
                } else if (item.getFieldName().equals("username")) {
                    username = item.getString("UTF-8");
                } else if (item.getFieldName().equals("companyid")) {
                    companyid = item.getString("UTF-8");
                } else if (item.getFieldName().equals("ip")) {
                    ip = item.getString("UTF-8");
                }else if (item.getFieldName().equals("oldImg")) {
                	oldImg = item.getString("UTF-8");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
       
        //删除本地图片
        File file = new File(filePath + "\\" + oldImg.replace("files/headimage/",""));
        file.delete();
        Map<String, String> paMap = new HashMap<String, String>();
        Map<String, String> returnMap = new HashMap<String, String>();
        paMap.put("path", filePat);
        paMap.put("username", username);
        paMap.put("companyid", companyid);
        paMap.put("ip", ip);
        returnMap = this.saveHeadImage(paMap);
        returnMap.put("imgPath", filePat);
        return returnMap;
    }

    public void getLoginLogAndroid(HttpServletRequest request,
            HttpServletResponse response) {// (start-1)*limit
        JSONObject json = new JSONObject();
        if (request.getParameter("username") != null) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("userid", request.getParameter("username"));
            int start = (Integer.parseInt(request.getParameter("start")) - 1)
                    * Integer.parseInt(request.getParameter("limit"));
            map.put("start", start + "");
            map.put("limit", request.getParameter("limit"));
            List<Map<String, String>> lst = logutils.getLoginLog(
                    this.getCompLocator(), map);
            // String callback = request.getParameter("callback");
            // Map<String,Object> result = new HashMap<String, Object>();
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("userid", request.getParameter("username"));
            map1.put("start", request.getParameter("start"));
            map1.put("limit", request.getParameter("limit"));
            String count = logutils.getLoginLogCount(this.getCompLocator(),
                    map1);
            // result.put("lst", lst);
            // Integer counts =
            // (Integer.parseInt(count)-1)/Integer.parseInt(request.getParameter("limit"))+1;
            // result.put("totle", count+"");//((allCount-1)/pageSize)+1
            json.put("success", "true");
            json.put("lst", lst);
            json.put("countMun", count);
        } else {
            json.put("success", "false");
        }
        try {
            response.setContentType("text/javascript;charset=UTF-8");
            response.getWriter().println(json);
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    @Override
    public List<String> getUsersByFullName(Map<String, Object> param) {
        List<Map<String, String>> userList = userDao.getUsersByFullName(param);
        List<String> ids = new ArrayList<String>();
        for (Map<String, String> map : userList) {
            ids.add(map.get("ID"));
        }
        return ids;
    }

    @Override
    public Map<String, Object> getLoginLog(Map<String, Object> param) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", param.get("userid") + "");
        int start = (Integer.parseInt(param.get("start") + "") - 1)* Integer.parseInt(param.get("limit") + "");
        int limit = Integer.parseInt(param.get("limit")+"");
        map.put("start", start + "");
        map.put("limit", limit+"");
        List<Map<String, String>> lst = logutils.getLoginLog(
                this.getCompLocator(), map);
        Map<String, Object> result = new HashMap<String, Object>();
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("userid", param.get("userid") + "");
        map1.put("start", param.get("start") + "");
        map1.put("limit", param.get("limit") + "");
        String count = logutils.getLoginLogCount(this.getCompLocator(), map1);
        result.put("lst", lst);
        Integer counts = (Integer.parseInt(count) - 1)
                / Integer.parseInt(param.get("limit") + "")+1;
        result.put("totle", counts + "");// ((allCount-1)/pageSize)+1
        return result;
    }
    
    @Override
    public boolean isUserCompayExist(String userId){
        return userDao.isUserCompayExist(userId);
    }

    @Override
    public String getUserImagePathByUserId(String userId) {
        return userDao.getUserImagePathByUserId(userId);
    }
    
        @Override
        public Map<String, String> editUserCompanyId(String companyId,String userId){
            Map<String, String> reMap =  new HashMap<String, String>();
            Map<String, Object> dataMap =  new HashMap<String, Object>();
            dataMap.put("editCompanyId", companyId);
            dataMap.put("editUserid", userId);
            boolean flg=userDao.editUser(dataMap);
            reMap.put("success", flg+"");
            return reMap;
        }
         /** 20151021 xiayongcai
          * @param params 唯一username
          * 如果记忆企业被禁用
          * --那么跳转到自己创建的企业
          * 如果该用户没有自己企业
          * --将跳转到激活的企业
          * 如果该用户没有激活的企业
          * --那么将显示该用户被彻底禁用
          * @return 返回默认企业Id
          * @return 返回自己注册企业的Id
          * memoryCompanyId 
          * ownCompanyId
          * 
          * */
        @Override
        public Map<String, String> getUserCompanyId(String username){
            Map<String, String> retMap=new HashMap<String, String>();
            List<Map<String, String>> ListUser=null;
            if(username!=null && !"".equals(username.trim())){
                //注册过用户。返回USERID,COMPANYID,STATUS,ISADMIN,memoryCompanyId
                ListUser=userDao.getCompanysByUserName(username);
                if(ListUser!=null && ListUser.size()>0){
                    for(Map<String, String> userObjMap :ListUser){
                        //返回常用企业ID
                        if(userObjMap.get("memoryCompanyId")!=null && userObjMap.get("COMPANYID")!=null && userObjMap.get("STATUS")!=null && userObjMap.get("memoryCompanyId").equals((userObjMap.get("COMPANYID"))) && "1".equals(userObjMap.get("STATUS"))){
                            retMap.put("memoryCompanyId", userObjMap.get("memoryCompanyId"));
                        }
                        //class：0 表示用户为真正激活，不然不算激活，是邀请的状态。
                        if(userObjMap.get("CLASSID")!=null && "0".equals(userObjMap.get("CLASSID")) && userObjMap.get("ISADMIN")!=null && "1".equals(userObjMap.get("ISADMIN"))){
                            //返回自己注册的企业ID默认目前一个用户只能注册一个企业
                            retMap.put("ownCompanyId",userObjMap.get("COMPANYID"));
                        }
                        //返回没有被禁用的企业id
                        if(userObjMap.get("CLASSID")!=null && "0".equals(userObjMap.get("CLASSID")) && userObjMap.get("STATUS")!=null && "1".equals(userObjMap.get("STATUS"))){
                            retMap.put("activationCompanyId",userObjMap.get("COMPANYID"));
                        }
                    }
                    
                }
            }
            return retMap;
        }



        /**
         * 安卓端获取个性设置
         */
        public Map<String,String> getSingleSets(Map<String, String> params){
        	Map<String,String> userSetParams = new HashMap<>();
        	if(params.size() > 0){
        		userSetParams =  userDao.getSingleSet(params);
        		if(userSetParams.size() ==  0){
        			userSetParams.put("isUpRemind","1");
        			userSetParams.put("isDowRemind","1");
        			userSetParams.put("isOpenSpace", "1");
        			userSetParams.put("isOpenGroup", "1");
        		}
        	}
        	return userSetParams;
        }
        
      public Map<String,String> userLogin(Map<String,String> param){
                Map<String,String> map = new HashMap<String,String>();
                boolean result = userDao.userLogin(param);
                map.put("result", result+"");
             return map;
      }
      
      public Map<String,String> registerUser(String userName,String passWord){
         Map<String,String> param = new HashMap<String,String>();
          param.put("userName", userName);
          param.put("passWord", passWord);
          String result = "";
          if(!userDao.isExitsUserName(param)){
            result = userDao.registerUser(param)+"";
          }else{
            result = "userName isExits";
          }
          Map<String,String> map = new HashMap<String,String>();
          map.put("result", result);
        return map;
      }
}
