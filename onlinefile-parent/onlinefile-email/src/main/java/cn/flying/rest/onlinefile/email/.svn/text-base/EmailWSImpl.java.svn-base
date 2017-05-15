package cn.flying.rest.onlinefile.email;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.flying.rest.onlinefile.email.driver.EmailDao;
import cn.flying.rest.onlinefile.restInterface.EmailWS;
import cn.flying.rest.onlinefile.restInterface.FilesWS;
import cn.flying.rest.onlinefile.utils.BaseWS;
import cn.flying.rest.onlinefile.utils.EmailUtil;
import cn.flying.rest.onlinefile.utils.LogUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Path("/onlinefile_emailws")
@Component
public class EmailWSImpl extends BaseWS implements EmailWS{
	
	@Resource(name="emailServerDao")
	private EmailDao emailDao;
	
	private Gson gson = new Gson();
	
	/**    20151011 liuhezeng添加 log4j日志管理    **/
	private static final Logger logger = Logger.getLogger(EmailWSImpl.class);
	
	private FilesWS filesWS;
	
	private FilesWS getFilesWS(){
        if(filesWS == null){
            filesWS = this.getService(FilesWS.class);
        }
        return filesWS;
    }
	
	@Value("${flyinfsoft.email.upload.url}")
	public void setEmailUploadUrl(String emailUploadUrl) {
		EmailUtil.emailUploadUrl = emailUploadUrl;
	}
	
	@Value("${fileserver.uploadURI}")
	public void setFileUploadUrl(String fileUploadUrl) {
		EmailUtil.fileUploadUrl = fileUploadUrl;
	}
	
	public Map<String, String> addEmailManual(Map<String, String> map) {
		
		Map<String, String> resultMaps = new HashMap<String, String>();
		String emailAddress = map.get("email").toString();
		/** 此处检测是否为163邮箱！后续邮箱会陆续开放 **/
		if (EmailUtil.checkEmailServerForManul(emailAddress,map.get("popServerInput"))) {
			/** 验证邮箱有效性**/
			boolean flag = false;
			try {
				flag = EmailUtil.checkEmail(emailAddress, map.get("password"),map.get("popServerInput"),map.get("popSSLPortInput"));
			} catch (Exception e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage()); 
			}
			if(!flag){
				resultMaps.put("success", "false");
				resultMaps.put("msg", "邮箱登录验证失败");
				resultMaps.put("imgUrl", EmailUtil.getEmailType(emailAddress)+"_Guide.png");
				return resultMaps;
			}else{
				
				//日志--start
				HashMap<String, String> log = new HashMap<String, String>();
				log.put("ip", map.get("ip"));
				log.put("userid", map.get("username"));
				log.put("loginfo", "手动添加邮箱【"+emailAddress+"】");
				log.put("module", "邮箱设置");
				log.put("username",map.get("username"));
				log.put("type", "operation");
				log.put("operate", "邮箱设置");
				log.put("companyName", map.get("companyName"));
				
				LogUtils.saveBaseLog(compLocator, log);
				//日志--end
				
				return emailDao.addEmailManual(map);
			}
		} else {
			resultMaps.put("success", "false");
			resultMaps.put("msg", "亲,我们暂时支持163,263,新浪,搜狐,189邮箱,后续邮箱的支持我们会陆续开放！");
			return resultMaps;
		}
	}
	
	public Map<String, String> addEmail(Map<String, String> map) {
		Map<String, String> resultMaps = new HashMap<String, String>();
		String emailAddress = map.get("email").toString();
		/** 此处检测是否为163邮箱！后续邮箱会陆续开放 **/
		if (EmailUtil.checkEmailServer(emailAddress)) {
			/** 验证邮箱有效性， 暂时验证163邮箱**/
			boolean flag = false;
			try {
				Map<String,String> emailProperties = EmailUtil.getEmailServerPropertitesByAddress(emailAddress);
				String host = emailProperties.get("receiveAddress");
				String popPort = emailProperties.get("receivePort");
				
				flag = EmailUtil.checkEmail(emailAddress, map.get("password").toString(),host,popPort);
			} catch (Exception e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage());

			}
			if(!flag){
				resultMaps.put("success", "false");
				resultMaps.put("msg", "邮箱登录验证失败");
				resultMaps.put("imgUrl", EmailUtil.getEmailType(emailAddress)+"_Guide.png");
				return resultMaps;
			}else{
				
				//日志--
				HashMap<String, String> log = new HashMap<String, String>();
				log.put("ip", map.get("ip"));
				log.put("userid", map.get("username"));
				log.put("loginfo", "添加邮箱【"+emailAddress+"】");
				log.put("module", "邮箱设置");
				log.put("username",map.get("username"));
				log.put("type", "operation");
				log.put("operate", "邮箱设置");
				log.put("companyName", map.get("companyName"));
				LogUtils.saveBaseLog(compLocator, log);
				
				return emailDao.addEmail(map);
			}
		} else {
			resultMaps.put("success", "false");
			resultMaps.put("msg", "亲,我们暂时支持163,263,新浪,搜狐,189邮箱,后续邮箱的支持我们会陆续开放！");
			return resultMaps;
		}
	}

	public Map<String, String> deleteEmail(Map<String, String> map) {
		//日志--
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", map.get("ip"));
		log.put("userid", map.get("username"));
		log.put("loginfo", "删除邮箱【"+map.get("email")+"】");
		log.put("module", "邮箱设置");
		log.put("username",map.get("username"));
		log.put("type", "operation");
		log.put("operate", "邮箱设置");
		log.put("companyName", map.get("companyName"));
		LogUtils.saveBaseLog(compLocator, log);
		
		return emailDao.deleteEmail(map);
	}

	public Map<String, String> updateEmail(Map<String, String> map) {
		//日志--
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", map.get("ip"));
		log.put("userid", map.get("username"));
		log.put("loginfo", "修改邮箱【"+map.get("email")+"】");
		log.put("module", "邮箱设置");
		log.put("username",map.get("username"));
		log.put("type", "operation");
		log.put("operate", "邮箱设置");
		log.put("companyName", map.get("companyName"));
		LogUtils.saveBaseLog(compLocator, log);
		
		return emailDao.updateEmail(map);
	}

	/**
     * 输出json结果
     * @param response
     * @param json
     */
    private void writeJson(HttpServletResponse response, String callback, String json) {
        response.setContentType("text/javascript;charset=UTF-8");
        String data="";
        if(callback !=null && "androidInter".equals(callback)){
        	 data=json;
        }else{
        	 data = super.jsonpCallbackWithString(callback, json);
        }
        
        try {
            response.getWriter().println(data);
            response.getWriter().close();
        } catch (IOException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());       
        }
    }
	
	@SuppressWarnings("unchecked")
	public void getEmailList(HttpServletRequest request,HttpServletResponse response) {
		
		String userid = request.getParameter("userid");
		String callback = request.getParameter("callback");
		//日志--
		try {
			String username = request.getParameter("username");
			String companyName = request.getParameter("companyName");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getParameter("ip"));
			log.put("userid", username);
			log.put("loginfo", "获取邮箱列表");
			log.put("module", "邮件浏览");
			log.put("username",username);
			log.put("type", "operation");
			log.put("operate", "邮件浏览");
			log.put("companyName", URLDecoder.decode(companyName, "utf-8"));
			LogUtils.saveBaseLog(compLocator, log);
		} catch (Exception e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());       
        }
        
        Map<String,String> map = new HashMap<String, String>();
        map.put("userid", userid);
        //String callback = request.getParameter("callback");
        List<Map<String,String>> list = emailDao.getEmailList(map);
        Map result = new HashMap();
        result.put("emails", list);
        writeJson(response, callback, gson.toJson(result));
//        return result;
       // writeJson(response, callback, gson.toJson(result));
	}
	

	@Override
	public Map cacheAllEmailAttachments( Map<String,String> mapParams) {
		String userid = mapParams.get("userid");
		String email = mapParams.get("email");
		
        Map<String,String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("email", email);
        Map result = new HashMap();
        try {
			result.put("emailAttechMents", emailDao.cacheAllEmailAttachments(map));
			result.put("success", "true");
		} catch (Exception e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage());       
		}
        return result;
	}
	
	@Override
	public void cacheDefaultAllEmailAttachments(HttpServletRequest request,
			HttpServletResponse response) {
		String userid = request.getParameter("userid");
		String email = request.getParameter("email");
        
        Map<String,String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("email", email);
        String callback = request.getParameter("callback");
        Map result = new HashMap();
        result.put("emailAttechMents", emailDao.cacheDefaultAllEmailAttachments(map));
        writeJson(response, callback, gson.toJson(result));
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> searchEmailAttachment(Map<String,String> mapParams) {
		
		String userid = mapParams.get("userid");
		String searchKeyWord = mapParams.get("searchKeyWord");
		String email = mapParams.get("email");
		String pageIndex = mapParams.get("pageIndex");
		String pageLimit = mapParams.get("pageLimit");
		String windowWidth = mapParams.get("windowWidth");
		//日志--
		try {
			String username = mapParams.get("username");
			String companyName = mapParams.get("companyName");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", mapParams.get("ip"));
			log.put("userid", username);
			log.put("loginfo", "搜索邮箱【"+email+"】附件,关键词【"+searchKeyWord+"】");
			log.put("module", "邮件浏览");
			log.put("username",username);
			log.put("type", "operation");
			log.put("operate", "邮件浏览");
			log.put("companyName", URLDecoder.decode(companyName, "utf-8"));
			LogUtils.saveBaseLog(compLocator, log);
		} catch (Exception e) {
		}
		
        Map<String,String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("email", email);
        map.put("pageIndex", pageIndex);
        map.put("pageLimit", pageLimit);
        map.put("searchKeyWord", searchKeyWord);
        map.put("windowWidth", windowWidth);
        Map result = new HashMap();
		Map<String,Object> emailAttechMentsLists = emailDao.searchEmailAttachment(map);
        result.put("emailAttechMents", emailAttechMentsLists.get("emailAttechMents"));
        result.put("emailAttechMentPages", emailAttechMentsLists.get("emailAttechMentPages"));
        return result;
        
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> getAttachmentByEmail(Map<String,String> mapParams) {
		String userid = mapParams.get("userid");
		String emailAddress = mapParams.get("emailAddress");
		String pageIndex = mapParams.get("pageIndex");
		String pageLimit = mapParams.get("pageLimit");
		String windowWidth = mapParams.get("windowWidth");
		//日志--
		try {
			String username = mapParams.get("username");
			String companyName = mapParams.get("companyName");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", mapParams.get("ip"));
			log.put("userid", username);
			log.put("loginfo", "获取邮箱【"+emailAddress+"】附件");
			log.put("module", "邮件浏览");
			log.put("username",username);
			log.put("type", "operation");
			log.put("operate", "邮件浏览");
			log.put("companyName", URLDecoder.decode(companyName, "utf-8"));
			LogUtils.saveBaseLog(compLocator, log);
		} catch (Exception e) {
		}
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("email", emailAddress);
		map.put("pageIndex", pageIndex);
		map.put("pageLimit", pageLimit);
		map.put("windowWidth", windowWidth);
		@SuppressWarnings("rawtypes")
		Map result = new HashMap();
		Map<String,Object> emailAttechMentsLists = emailDao.getAttachmentByEmail(map);
		result.put("emailAttechMents", emailAttechMentsLists.get("emailAttechMents"));
        result.put("emailAttechMentPages", emailAttechMentsLists.get("emailAttechMentPages"));
        return result;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getDefaultAttachmentByEmail(Map<String,String> mapParams) {
		String userid = mapParams.get("userid");
		String emailAddress = mapParams.get("emailAddress");
		String pageIndex = mapParams.get("pageIndex");
		String pageLimit = mapParams.get("pageLimit");
		String windowWidth = mapParams.get("windowWidth");
		//日志--
		try {
			String username = mapParams.get("username");
			String companyName = mapParams.get("companyName");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", mapParams.get("ip"));
			log.put("userid", username);
			log.put("loginfo", "获取默认邮箱附件");
			log.put("module", "邮件浏览");
			log.put("username",username);
			log.put("type", "operation");
			log.put("operate", "邮件浏览");
			log.put("companyName", URLDecoder.decode(companyName, "utf-8"));
			LogUtils.saveBaseLog(compLocator, log);
		} catch (Exception e) {
		}
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("email", emailAddress);
		map.put("pageIndex", pageIndex);
		map.put("pageLimit", pageLimit);
		map.put("windowWidth", windowWidth);
		@SuppressWarnings("rawtypes")
		Map result = new HashMap();
		Map<String,Object> emailAttechMentsLists = emailDao.getDefaultAttachmentByEmail(map);
		result.put("emailAttechMents", emailAttechMentsLists.get("emailAttechMents"));
        result.put("emailAttechMentPages", emailAttechMentsLists.get("emailAttechMentPages"));
        return result;
	}

	@Override
	public Map<String, String> getEmailSettingByEmail(Map<String, String> map) {
		
		//日志--
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", map.get("ip"));
		log.put("userid", map.get("username"));
		log.put("loginfo", "获取邮箱【"+map.get("email")+"】设置");
		log.put("module", "邮箱设置");
		log.put("username",map.get("username"));
		log.put("type", "operation");
		log.put("operate", "邮箱设置");
		log.put("companyName", map.get("companyName"));
		LogUtils.saveBaseLog(compLocator, log);
		return emailDao.getEmailSettingByEmail(map);
	}

	@Override
	public List<Map<String, String>> getAllAttachments(Map<String, String> map) {
		//日志--
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", map.get("ip"));
		log.put("userid", map.get("username"));
		log.put("loginfo", "获取邮箱【"+map.get("email")+"】所有附件");
		log.put("module", "邮件浏览");
		log.put("username",map.get("username"));
		log.put("type", "operation");
		log.put("operate", "邮件浏览");
		log.put("companyName", map.get("companyName"));
		LogUtils.saveBaseLog(compLocator, log);
		return emailDao.getAllAttachments(map);
	}

	@Override
	public Map<String, String> saveEmailSetting(Map<String, String> map) {
		Map<String, String> resultMaps = new HashMap<String, String>();
		String emailAddress = map.get("email");
		/** 此处检测是否为163邮箱！后续邮箱会陆续开放 **/
		if (EmailUtil.checkEmailServer(emailAddress)) {
			boolean flag = false;
			try {
				flag = EmailUtil.checkEmail(emailAddress, map.get("password").toString(),map.get("receiverserver"),map.get("receiveserverport"));
			} catch (Exception e) {
				/**   20151011 liuhezeng 添加log4j日志管理    **/
				logger.error(e.getMessage()); 
			}
			if(!flag){
				resultMaps.put("success", "false");
				resultMaps.put("msg", "邮箱登录验证失败,保存失败");
				return resultMaps;
			}else{
				
				//日志
				HashMap<String, String> log = new HashMap<String, String>();
				log.put("ip", map.get("ip"));
				log.put("userid", map.get("username"));
				log.put("loginfo", "修改邮箱【"+map.get("email")+"】");
				log.put("module", "邮箱设置");
				log.put("username",map.get("username"));
				log.put("type", "operation");
				log.put("operate", "邮箱设置");
				log.put("companyName", map.get("companyName"));
				LogUtils.saveBaseLog(compLocator, log);
				return emailDao.saveEmailSetting(map);
			}
		} else {
			resultMaps.put("success", "false");
			resultMaps.put("msg", "亲,我们暂时支持163,263,新浪,搜狐,189邮箱,后续邮箱的支持我们会陆续开放！");
			return resultMaps;
		}
	}

	@Override
	public Map<String, String> setAsDefaultEmail(Map<String, String> map) {
		
		//日志--
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", map.get("ip"));
		log.put("userid", map.get("username"));
		log.put("loginfo", "设置为默认邮箱【"+map.get("email")+"】");
		log.put("module", "邮箱设置");
		log.put("username",map.get("username"));
		log.put("type", "operation");
		log.put("operate", "邮箱设置");
		log.put("companyName", map.get("companyName"));
		LogUtils.saveBaseLog(compLocator, log);
		// TODO Auto-generated method stub
		return emailDao.setAsDefaultEmail(map);
	}

	@Override
	public Map<String,String> isSynEmailAttachMent(Map<String,String> mapParams) {
		String userid = mapParams.get("userid");
		String emailAddress = mapParams.get("email");
		
		//日志--
		try {
			String username = mapParams.get("username");
			String companyName = mapParams.get("companyName");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", mapParams.get("ip"));
			log.put("userid", username);
			log.put("loginfo", "同步邮箱【"+emailAddress+"】附件");
			log.put("module", "邮箱浏览");
			log.put("username",username);
			log.put("type", "operation");
			log.put("operate", "邮箱浏览");
			log.put("companyName", URLDecoder.decode(companyName, "utf-8"));
			LogUtils.saveBaseLog(compLocator, log);
		} catch (UnsupportedEncodingException e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage()); 
		}
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("email", emailAddress);
		Map<String,String> emailAttechMentsLists = emailDao.isSynEmailAttachMent(map);
		return emailAttechMentsLists;
	}

	@Override
	public void getEmailAttachMentInfoByEmail(HttpServletRequest request,
			HttpServletResponse response) {
		String userid = request.getParameter("userid");
		String emailAddress = request.getParameter("emailAddress");
		String callback = request.getParameter("callback");
		String emailIndex = request.getParameter("emailIndex");
		
		//日志--
		try {
			String username = request.getParameter("username");
			String companyName = request.getParameter("companyName");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "获取邮箱【"+emailAddress+"】内容");
			log.put("module", "邮箱浏览");
			log.put("username",username);
			log.put("type", "operation");
			log.put("operate", "邮箱浏览");
			log.put("companyName", URLDecoder.decode(companyName, "utf-8"));
			LogUtils.saveBaseLog(compLocator, log);
		} catch (Exception e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage()); 
		}
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("email", emailAddress);
		map.put("emailIndex", emailIndex);
		writeJson(response, callback, gson.toJson(emailDao.getEmailAttachMentInfoByEmail(map)));
	
	}
	
	@Override
	public void getEmailAttachMentsByEmail(HttpServletRequest request,
			HttpServletResponse response) {
		String userid = request.getParameter("userid");
		String emailAddress = request.getParameter("emailAddress");
		String pageIndex = request.getParameter("pageIndex");
		String pageLimit = request.getParameter("pageLimit");
		String callback = request.getParameter("callback");
		String emailIndex = request.getParameter("emailIndex");
		String partCount = request.getParameter("partCount");
		
		//日志--
		try {
			String username = request.getParameter("username");
			String companyName = request.getParameter("companyName");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "获取邮箱【"+emailAddress+"】附件");
			log.put("module", "邮箱浏览");
			log.put("username",username);
			log.put("type", "operation");
			log.put("operate", "邮箱浏览");
			log.put("companyName", URLDecoder.decode(companyName, "utf-8"));
			LogUtils.saveBaseLog(compLocator, log);
		} catch (Exception e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage()); 
		}
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("email", emailAddress);
		map.put("pageIndex", pageIndex);
		map.put("pageLimit", pageLimit);
		map.put("emailIndex", emailIndex);
		map.put("partCount", partCount);
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,Object> emailAttechMentsLists = emailDao.getEmailAttachMentsByEmail(map);
		result.put("emailAttechMents", emailAttechMentsLists.get("emailAttechMents"));
        result.put("emailAttechMentPages", emailAttechMentsLists.get("emailAttechMentPages"));
        result.put("success", emailAttechMentsLists.get("success"));
        result.put("msg", emailAttechMentsLists.get("msg"));
		writeJson(response, callback, gson.toJson(result));
	}
	
	@Override
	public void getDefaultEmail(HttpServletRequest request,
			HttpServletResponse response) {
		String userid = request.getParameter("userid");
		
		//日志--
		try {
			String username = request.getParameter("username");
			String companyName = request.getParameter("companyName");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", request.getRemoteAddr());
			log.put("userid", username);
			log.put("loginfo", "获取默认邮箱");
			log.put("module", "邮箱设置");
			log.put("username",username);
			log.put("type", "operation");
			log.put("operate", "邮箱设置");
			log.put("companyName", URLDecoder.decode(companyName, "utf-8"));
			LogUtils.saveBaseLog(compLocator, log);
		} catch (Exception e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage()); 
		}
		
        Map<String,String> map = new HashMap<String, String>();
        map.put("userid", userid);
        String callback = request.getParameter("callback");
        Map result = new HashMap();
        result.put("defaultEmail",emailDao.getDefaultEmail(map) );
        result.put("emails", emailDao.getEmailList(map));
        writeJson(response, callback, gson.toJson(result));
		
	}

	@Override
	public void downloadAttachMent(HttpServletRequest request,
			HttpServletResponse response) {
		String userid = request.getParameter("userid");
		String email = request.getParameter("email");
		String emailIndex = request.getParameter("emailIndex");
		String attachMentIndex = request.getParameter("attachMentIndex");
        String callback = request.getParameter("callback");
        String emailUploadUrl = request.getParameter("emailUploadUrl");
        String classId = request.getParameter("classId");
        String companyId = request.getParameter("companyId");
        String openlevel = request.getParameter("openlevel");
        String ip = request.getRemoteAddr();
        emailUploadUrl = emailUploadUrl.replace(EmailUtil.fileUploadUrl, EmailUtil.emailUploadUrl);
        
        //日志--
		try {
			String username = request.getParameter("username");
			String companyName = request.getParameter("companyName");
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("ip", ip);
			log.put("userid", username);
			log.put("loginfo", "下载邮箱【"+email+"】附件");
			log.put("module", "邮箱浏览");
			log.put("username",username);
			log.put("type", "operation");
			log.put("operate", "邮箱浏览");
			log.put("companyName", URLDecoder.decode(companyName, "utf-8"));
			LogUtils.saveBaseLog(compLocator, log);
		} catch (Exception e) {
			/**   20151011 liuhezeng 添加log4j日志管理    **/
			logger.error(e.getMessage()); 
		}
        
		Map<String,String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("email", email);
		map.put("emailIndex", emailIndex);
		map.put("attachMentIndex", attachMentIndex);
		map.put("emailUploadUrl", emailUploadUrl);
		map.put("classId", classId);
		map.put("companyId", companyId);
		map.put("openlevel", openlevel);
		Map<String, String> result = new HashMap<String, String>();
		List<String> attachList = emailDao.downloadAttachMent(map);
		if (attachList.size()>0) {
		    for (String attach : attachList) {
		        Map<String, String> temp = gson.fromJson(attach, new TypeToken<Map<String, String>>(){}.getType());
		        temp.put("companyId",companyId);
		        temp.put("userId",userid);
		        temp.put("remoteAddr",ip);
		        temp.put("md5", temp.get("contentMD5"));
		        temp.put("fileName", temp.get("filename"));
		        temp.put("size", temp.get("fileSize"));
		        // 插入db
		        getFilesWS().insertNewFileAfterUpload(temp);
		    }
		    result.put("success","true");
		    result.put("msg", "保存成功");
		} else {
		    result.put("success","false");
            result.put("msg", "保存失败");
		}
		
		 writeJson(response, callback, gson.toJson(result));
	}

	@Override
	public Map<String, String> deleteAllEmail(Map<String, String> map) {
		//日志--
		HashMap<String, String> log = new HashMap<String, String>();
		log.put("ip", map.get("ip"));
		log.put("userid", map.get("username"));
		log.put("loginfo", "清空邮箱");
		log.put("module", "邮箱设置");
		log.put("username",map.get("username"));
		log.put("type", "operation");
		log.put("operate", "邮箱设置");
		log.put("companyName", map.get("companyName"));
		LogUtils.saveBaseLog(compLocator, log);
		
		return emailDao.deleteAllEmail(map);
	}


}
