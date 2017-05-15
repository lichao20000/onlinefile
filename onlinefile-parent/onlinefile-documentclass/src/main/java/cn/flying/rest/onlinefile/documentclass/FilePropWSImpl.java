package cn.flying.rest.onlinefile.documentclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.flying.rest.admin.restInterface.BaseCacheWS;
import cn.flying.rest.onlinefile.documentclass.driver.FilePropDao;
import cn.flying.rest.onlinefile.restInterface.FilePropertiesWS;
import cn.flying.rest.onlinefile.utils.BaseWS;
import cn.flying.rest.onlinefile.utils.CacheUtils;
import cn.flying.rest.onlinefile.utils.LogUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
@Path("/onlinefile_filePropws")
@Controller
public class FilePropWSImpl extends BaseWS implements FilePropertiesWS{
	private LogUtils logutils;
	@Autowired
	private FilePropDao fpDao;
	private Gson gson = new Gson();
	private BaseCacheWS baseCacheWS;
	public static final String filePropCacheKey = "FilePropCacheKey_";
	public BaseCacheWS getBaseCacheWS(){
		if(baseCacheWS==null){
		      baseCacheWS = this.getService(BaseCacheWS.class);
		}
		return baseCacheWS;
	}

	public Map<String,Object> getFilePropLst(Map<String, String> paras) {
		Map<String,Object> rtnMap = new HashMap<String, Object>();
		String userId = paras.get("userId");
		String queryStr = paras.get("queryKeyWord");
		int start = Integer.parseInt(paras.get("start"));
		int limit = Integer.parseInt(paras.get("limit"));
		if(userId==null || userId.length()==0){
			rtnMap.put("success", "false");
			rtnMap.put("msg", "nouserid");
			return rtnMap;
		}
		Map<String,Object> lstMap = fpDao.getFilePropLst(userId,start,limit,queryStr);
		if(lstMap.size()>0){
			rtnMap.put("success", "true");
			rtnMap.put("total", lstMap.get("total"));
			rtnMap.put("Lst", lstMap.get("lst"));
			// 日志------------------------------------------
			 Map<String, Map<String, String>> companyUserInitInfo = null;
				Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+paras.get("companyId")) ;
				if(obj != null){
					companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
				}
			String companyName = companyUserInitInfo.get(paras.get("username")).get("COMPANYNAME");
			 HashMap<String, String> log = new HashMap<String, String>();
			 log.put("userid",paras.get("username"));
			 log.put("username",paras.get("username"));
			 log.put("ip", paras.get("ip"));
			 log.put("module","获得文件属性列表");
			 log.put("companyName", companyName != null?companyName:paras.get("companyId"));
			// login 用户登录 access 功能访问 operation 功能操作
			 log.put("type", "access");
			 log.put("loginfo", "获得文件属性列表,用户名名为【"+paras.get("username")+"】");
			 logutils.saveBaseLog(this.getCompLocator(), log);
			// 日志------------------------------------------
		}else{
			rtnMap.put("success", "false");
			rtnMap.put("msg", "selectError");
		}
		return rtnMap;
	}
	
	public List<Map<String,String>> getFilePropLstByCompany(String companyId){
		String jsonStr;
		List<Map<String,String>> lst;
		if(getBaseCacheWS().get(filePropCacheKey+companyId)!=null && getBaseCacheWS().get(filePropCacheKey+companyId).trim().length()>0){
			jsonStr =  getBaseCacheWS().get(filePropCacheKey+companyId);
			lst = gson.fromJson(jsonStr, new TypeToken<List<Map<String,String>>>(){}.getType());
		}else{
			lst = fpDao.getFilePropLstByCompany(companyId);
			jsonStr = gson.toJson(lst);
			getBaseCacheWS().set(filePropCacheKey+companyId,jsonStr);
		}
		return lst;
	}
	
	public Map<String, String> addFileProp(Map<String, String> filePropBeanMap) {
		Map<String,String> rtnMap = new HashMap<String, String>();
		int count = fpDao.getFilePropCountByTitle(filePropBeanMap.get("TITLE"),filePropBeanMap.get("COMPANYID"));
		if(count>0){
			rtnMap.put("success", "false");
			rtnMap.put("msg", "ishave");
			return rtnMap;
		}
		filePropBeanMap.put("ISSYSTEM", "0");//1是0否
		rtnMap = fpDao.addFileProp(filePropBeanMap);
		if("true".equals(rtnMap.get("success"))){
			getBaseCacheWS().set(filePropCacheKey+filePropBeanMap.get("COMPANYID"),"");
			StringBuffer sql = new StringBuffer();
			sql.append("ALTER TABLE files_").append(filePropBeanMap.get("COMPANYID")).append(" ADD COLUMN ").append("C").append(rtnMap.get("id"));
			if("文本".equals(filePropBeanMap.get("TYPE"))){
				sql.append(" VARCHAR(").append(filePropBeanMap.get("LENGTH")).append(" ) NULL");
			}else if("数值".equals(filePropBeanMap.get("TYPE"))){
				sql.append(" INT(").append(filePropBeanMap.get("LENGTH")).append(" ) NULL");
			}else if("日期".equals(filePropBeanMap.get("TYPE"))){
				sql.append(" DATE NULL");
			}else if("浮点".equals(filePropBeanMap.get("TYPE"))){
				sql.append(" DECIMAL(").append(filePropBeanMap.get("LENGTH")).append(",").append(filePropBeanMap.get("DOTLENGTH")).append(") NULL");
			}else if("时间".equals(filePropBeanMap.get("TYPE"))){
				sql.append(" TIME NULL");
			}else if("布尔".equals(filePropBeanMap.get("TYPE"))){
				sql.append(" TINYINT(1) NULL");
			}
			boolean addbol = fpDao.addColumn(sql.toString());
			// 日志------------------------------------------
			 Map<String, Map<String, String>> companyUserInitInfo = null;
				Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+filePropBeanMap.get("COMPANYID")) ;
				if(obj != null){
					companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
				}
			String companyName = companyUserInitInfo.get(filePropBeanMap.get("username")).get("COMPANYNAME");
			 HashMap<String, String> log = new HashMap<String, String>();
			 log.put("userid",filePropBeanMap.get("username"));
			 log.put("username",filePropBeanMap.get("username"));
			 log.put("ip", filePropBeanMap.get("ip"));
			 log.put("module","添加文件自定义属性");
			 log.put("companyName", companyName != null?companyName:filePropBeanMap.get("COMPANYID"));
			// login 用户登录 access 功能访问 operation 功能操作
			 log.put("type", "operation");
			 log.put("loginfo", "添加文件自定义属性,属性名为【"+filePropBeanMap.get("TITLE")+"】");
			 logutils.saveBaseLog(this.getCompLocator(), log);
			// 日志------------------------------------------
			if(!addbol){
				Map<String,String> delMap = new HashMap<String, String>();
				delMap.put("companyId", filePropBeanMap.get("COMPANYID"));
				delMap.put("fileId", rtnMap.get("id"));
				this.delFileProp(delMap);
				rtnMap.put("success", "false");
				rtnMap.put("msg", "添加字段出错");
			}
		}
		return rtnMap;
	}

	public Map<String, String> delFileProp(Map<String, String> filePropBeanMap) {
		String companyId = filePropBeanMap.get("companyId");
		boolean isDrop = fpDao.delColumn("files_"+companyId, "c"+ filePropBeanMap.get("fileId"));
		boolean bol = false;
		if(isDrop){
			getBaseCacheWS().set(filePropCacheKey+companyId,"");
			bol = fpDao.delFileProp(filePropBeanMap);
			// 日志------------------------------------------
			 Map<String, Map<String, String>> companyUserInitInfo = null;
				Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+filePropBeanMap.get("companyId")) ;
				if(obj != null){
					companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
				}
			String companyName = companyUserInitInfo.get(filePropBeanMap.get("username")).get("COMPANYNAME");
			 HashMap<String, String> log = new HashMap<String, String>();
			 log.put("userid",filePropBeanMap.get("username"));
			 log.put("username",filePropBeanMap.get("username"));
			 log.put("ip", filePropBeanMap.get("ip"));
			 log.put("module","删除自定义属性");
			// login 用户登录 access 功能访问 operation 功能操作
			 log.put("type", "operation");
			 log.put("companyName", companyName != null?companyName:filePropBeanMap.get("companyId"));
			 log.put("loginfo", "删除自定义属性,属性名为【"+filePropBeanMap.get("title")+"】");
			 logutils.saveBaseLog(this.getCompLocator(), log);
			// 日志------------------------------------------
		}
		Map<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("success", String.valueOf(bol));
		return rtnMap;
	}
	
	public Map<String,String> getFilePropById(Map<String,String> paras){
		return this.getFilePropById(paras.get("id"));
	}
	
	public Map<String,String> getFilePropById(String id){
		Map<String,String> rtnMap = fpDao.getFilePropById(id);
		if(rtnMap.size()==0){
			rtnMap.put("mapsize", "1");
		}else{
			rtnMap.put("mapsize", "0");
		}
		return rtnMap;
	}
	
	public Map<String,String> updateFilePropById(Map<String,String> paras){
		Map<String,String> rtnMap = new HashMap<String,String>();
		Map<String,String> oldData = this.getFilePropById(paras.get("ID"));
		if(!paras.get("TITLE").equals(oldData.get("TITLE"))){
			int count = fpDao.getFilePropCountByTitle(paras.get("TITLE"),paras.get("companyId"));
			if(count>0){
				rtnMap.put("success", "false");
				rtnMap.put("msg", "ishave");
				return rtnMap;
			}
		}
		boolean bol = fpDao.updateFilePropById(paras);
		rtnMap.put("success", String.valueOf(bol));
		if(!bol){
			rtnMap.put("msg", "error");
		}else{
			StringBuffer sql = new StringBuffer();
			sql.append("ALTER TABLE files_").append(paras.get("companyId")).append(" MODIFY COLUMN ").append("C").append(paras.get("ID"));
			if("文本".equals(paras.get("TYPE"))){
				sql.append(" VARCHAR(").append(paras.get("LENGTH")).append(" ) NULL");
			}else if("数值".equals(paras.get("TYPE"))){
				sql.append(" INT(").append(paras.get("LENGTH")).append(" ) NULL");
			}else if("浮点".equals(paras.get("TYPE"))){
				sql.append(" DECIMAL(").append(paras.get("LENGTH")).append(",").append(paras.get("DOTLENGTH")).append(") NULL");
			}
			fpDao.addColumn(sql.toString());
			// 日志------------------------------------------
			 Map<String, Map<String, String>> companyUserInitInfo = null;
				Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+paras.get("companyId")) ;
				if(obj != null){
					companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
				}
			String companyName = companyUserInitInfo.get(paras.get("username")).get("COMPANYNAME");
			 HashMap<String, String> log = new HashMap<String, String>();
			 log.put("userid",paras.get("username"));
			 log.put("username",paras.get("username"));
			 log.put("ip", paras.get("ip"));
			 log.put("module","修改自定义属性");
			 log.put("companyName", companyName != null?companyName:paras.get("companyId"));
			// login 用户登录 access 功能访问 operation 功能操作
			 log.put("type", "operation");
			 log.put("loginfo", "修改自定义属性,属性名为【"+paras.get("TITLE")+"】");
			 logutils.saveBaseLog(this.getCompLocator(), log);
			// 日志------------------------------------------
		}
		return rtnMap;
	}
	
	
}
