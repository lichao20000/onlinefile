package cn.flying.rest.onlinefile.documentclass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.flying.rest.onlinefile.documentclass.driver.DocumentClassDao;
import cn.flying.rest.onlinefile.restInterface.ChatWS;
import cn.flying.rest.onlinefile.restInterface.DocumentClassWS;
import cn.flying.rest.onlinefile.restInterface.FilesWS;
import cn.flying.rest.onlinefile.restInterface.LuceneWS;
import cn.flying.rest.onlinefile.utils.BaseWS;
import cn.flying.rest.onlinefile.utils.BroadcastUtils;
import cn.flying.rest.onlinefile.utils.CacheUtils;
import cn.flying.rest.onlinefile.utils.LogUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;


/**
 * 
 * @author guolanrui 20150309 
 * 文件分类实现
 *
 */
@Path("/onlinefile_documentclass")
@Controller
public class DocumentClassWSImpl extends BaseWS implements DocumentClassWS{
	
	
	private DocumentClassDao documentClassDao;
	private LogUtils logutils;
	private LuceneWS lucenews;
    
    private LuceneWS getLuceneWS(){
    	if(lucenews == null){
    		lucenews = this.getService(LuceneWS.class);
    	}
    	return lucenews;
    }
	@Resource
	public DocumentClassDao getDocumentClassDao(DocumentClassDao documentClassDao){
		return this.documentClassDao = documentClassDao;
	}
	
	private FilesWS filesWS;
	
	private FilesWS getFilesWS(){
        if(filesWS == null){
            filesWS = this.getService(FilesWS.class);
        }
        return filesWS;
    }
	private ChatWS chatWS;
	public ChatWS getChatWS(){
		if(null == this.chatWS){
			this.chatWS = this.getService(ChatWS.class);
		}
		return this.chatWS ;
	}
	
	public Map getCateList(Map<String,String> params){
		String limit = params.get("limit");
		String startNo = params.get("startNo");
		String keyWord = params.get("keyWord");
		String companyId = params.get("companyId");
		String userIp = params.get("userIp");
		String userId = params.get("userId");
		List<Map<String,String>> list = documentClassDao.getCateList(params);
		int count = documentClassDao.getCateCount();
		Map<String,Object> returnMap = new HashMap<String, Object>();
		returnMap.put("data", list);
		returnMap.put("count", count);
		
		return returnMap;
	}
	
	public Map addCate(Map<String,String> params){
		Map returnMap = documentClassDao.addCate(params);
		
		return returnMap;
	}
	
	public Map delCate(Map<String,String> params){
		Map returnMap = documentClassDao.delCate(params);
		
		return returnMap;
	}
	
	public Map editCate(Map<String,String> params){
		Map returnMap = documentClassDao.editCate(params);
		
		return returnMap;
	}
	
	public Map getCateById(Map<String,String> params){
		Map returnMap = documentClassDao.getCateById(params);
		
		return returnMap;
	}
	/**----------------------------------------------------------------*/

	public List getUserListClass(Map<String, String> params) {
		List<HashMap<String, Object>> rtnList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> rtnMap = new HashMap<String, Object>();
		String username = params.get("username");
		//String className = params.get("className");
		
		return null;
	}

	/**
	 * 添加文件夹
	 */
	public Map addClassByName(Map<String, String> params) { 
		Map<String, String> rtnmap = new HashMap<String, String>();
		String companyId = params.get("companyId");
		params.put("companyid", companyId);
		Map<String, String> map = documentClassDao.getCateById(params);//fatherClassId
		params.put("fatherClassId", params.get("classId"));
		if(!documentClassDao.getCheckClassNameIsIn(params)){
			rtnmap = documentClassDao.addClassByName(params);
			rtnmap.put("ishave", "fasle");
			if(!"1".equals(params.get("iscreateGroup"))){
				Map<String,String> createLuceneIndexParams = new HashMap<String, String>();
				createLuceneIndexParams.put("ID",rtnmap.get("id"));
				createLuceneIndexParams.put("COMPANYID",params.get("companyId"));
/*				createLuceneIndexParams.put("fileId", "0-0-0");
				createLuceneIndexParams.put("classId", params.get("classId") );
				createLuceneIndexParams.put("fileName",params.get("className") );
				createLuceneIndexParams.put("fileType", "");
				createLuceneIndexParams.put("insertId", rtnmap.get("id"));
				createLuceneIndexParams.put("idSeq", params.get("idSeq") );
				createLuceneIndexParams.put("openlevel", "1");
				createLuceneIndexParams.put("size", "0");
				createLuceneIndexParams.put("createtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				createLuceneIndexParams.put("createrId", params.get("userId") );
				createLuceneIndexParams.put("ip", params.get("ip"));
				createLuceneIndexParams.put("userid", params.get("userId") );
				createLuceneIndexParams.put("username", params.get("username") );
				createLuceneIndexParams.put("companyName", "");//记log用的，暂时先不传了
				createLuceneIndexParams.put("isFile", "0");
                createLuceneIndexParams.put("isDel", "0");
                createLuceneIndexParams.put("isLast", "1");
                createLuceneIndexParams.put("md5", "");*/
				getLuceneWS().createIndex(createLuceneIndexParams);
			}
			
			// 更新分类缓存
            getFilesWS().updateCompanyFoldersCache(companyId);
			// 日志------------------------------------------
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+params.get("companyId")) ;
			String companyName="";
			if(obj != null){
				companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
				companyName = companyUserInitInfo.get(params.get("username")).get("COMPANYNAME");
			}
			 HashMap<String, String> log = new HashMap<String, String>();
			 log.put("userid",params.get("username"));
			 log.put("username",params.get("username"));
			 log.put("ip", params.get("ip"));
			 log.put("module","添加分类");
			 log.put("companyName", !"".equals(companyName)?companyName:companyId);
			// login 用户登录 access 功能访问 operation 功能操作
			 log.put("type", "operation");
			 log.put("loginfo", "添加分类,分类名为【"+params.get("className")+"】");
			 logutils.saveBaseLog(this.getCompLocator(), log);
			// 日志------------------------------------------
		}else{
			rtnmap.put("ishave", "true");
			rtnmap.put("success", "false");
		}
		
		return rtnmap;
	}

	public Map deleteFileClassById(Map<String, String> params) {
	    String classId = params.get("classId");
	    String companyId = params.get("companyId");
	    String userId = params.get("userId");
	    String userName = params.get("username");
		Map<String, String> rtnmap = new HashMap<String, String>();
		//查出改文件夹下的idsql
		String idsql = documentClassDao.getChildByClassId(companyId,classId);
		String newidsql = idsql+classId;
		List<String> ids = documentClassDao.getIdsByIdSeq(newidsql, companyId);
		ids.add(classId);
		/**20151029 xiayongcai 加入删除分类判断，删除分类需要判断分类成员(除自己外)是否存在,存在将不给予删除，要请出成员方可删除*/
		if(!documentClassDao.hasUsersByClassId(classId, companyId, userId)){
			if(!documentClassDao.hasFilesByClassId(classId, companyId)){
				//idsql来进行匹配删除
				if(ids!=null && ids.size()>0){//documentClassDao.deleteClassByIdSeq(newidsql,companyId,folderId)
					if(documentClassDao.updateDelById(ids, companyId, "1")){//注意  底层是batch方式更新的数据库，一次batch500条左右最优，如果大于500条，请分批次执行，预估暂且不会多余500条
						//回收站删除
						if(documentClassDao.deleteHuiShouZhan(companyId,userId,userName,ids)){
							//修改索引库记录,20个文件调一次
							if(ids.size()<21){
								Map<String,Object> lucenemap = new HashMap<String, Object>();
								lucenemap.put("isDel", "1");
								lucenemap.put("companyId", companyId);
								lucenemap.put("ids", ids);
								getLuceneWS().updateIndexOfDel(lucenemap);
							}else{
								int j = ids.size()/20;
								if(ids.size()>20*j){
									j=j+1;
								}
								for(int i=0;i<j;i++){
									List<String> executeIds = null;
									if(i==j-1){
										executeIds = ids.subList(i*20,ids.size());
									}else{
										executeIds = ids.subList(i*20,i*20+20);
									}
									Map<String,Object> lucenemap = new HashMap<String, Object>();
									lucenemap.put("isDel", "1");
									lucenemap.put("companyId", companyId);
									lucenemap.put("ids", executeIds);
									getLuceneWS().updateIndexOfDel(lucenemap);
								}
							}
							
							//String groupFlag = documentClassDao.getFlagWithClassId(companyId, classId); 
							//rtnmap.put("groupFlag", groupFlag);
							Map<String, String> groupObjeMap = documentClassDao.getFlagWithClassId(companyId, classId); 
							rtnmap.put("groupFlag", groupObjeMap.get("FLAG"));
							rtnmap.put("GROUPNAME", groupObjeMap.get("GROUPNAME"));
							rtnmap.put("success", "true");
							getFilesWS().updateCompanyClassesCache(companyId, userId, userName);
							getFilesWS().updateCompanyFoldersCache(companyId);
						}
						// 日志------------------------------------------
						Map<String, Map<String, String>> companyUserInitInfo = null;
						Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+companyId) ;
						String companyName="";
						if(obj != null){
							companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
							companyName = companyUserInitInfo.get(params.get("username")).get("COMPANYNAME");
						}
						HashMap<String, String> log = new HashMap<String, String>();
						log.put("userid",params.get("username"));
						log.put("username",params.get("username"));
						log.put("ip", params.get("ip"));
						log.put("module","删除分类");
						log.put("companyName", !"".equals(companyName)?companyName:companyId);
						// login 用户登录 access 功能访问 operation 功能操作
						log.put("type", "operation");
						log.put("loginfo", "删除分类,分类id为【"+classId+"】");
						logutils.saveBaseLog(this.getCompLocator(), log);
						// 日志------------------------------------------
					}else{
						rtnmap.put("success", "false");
					}
				}else{
					rtnmap.put("success", "false");
				}
			}else{
				rtnmap.put("success", "havechildren");
			}
		}else{
			rtnmap.put("success", "haveuser");
		}
		//只要有一个出现删除不了 就停止不再删除
		//当所有分类删除，当前的文件夹也要删除
		return rtnmap;
	}
	public Map deleteClassById(Map<String, String> params) {
		String classId = params.get("classId");
		String folderId = params.get("folderId");
		String companyId = params.get("companyId");
		String userId = params.get("userId");
		String userName = params.get("username");
		Map<String, String> rtnmap = new HashMap<String, String>();
		//通过文件夹ID 获取文件夹详情
		Map<String, String> fileMap=getFilesWS().getFileById("FILES_"+companyId, folderId);
		//查出改文件夹下的idsql
		String idsql = documentClassDao.getChildByClassId(companyId,folderId);
		String newidsql = idsql+folderId;
		List<String> ids = documentClassDao.getIdsByIdSeq(newidsql, companyId);
		ids.add(folderId);
		//idsql来进行匹配删除
		if(ids!=null && ids.size()>0){//documentClassDao.deleteClassByIdSeq(newidsql,companyId,folderId)
			if(documentClassDao.updateDelById(ids, companyId, "1")){//注意  底层是batch方式更新的数据库，一次batch500条左右最优，如果大于500条，请分批次执行，预估暂且不会多余500条
				//xyc查询当前分类下文件最大的序号
                String maxSerialNumber=getFilesWS().getClassHighestSerialNumber("FILES_"+companyId,classId,"0");
                if(!maxSerialNumber.equals(fileMap.get("serialNumber"))){
                	//删除的不是当前最新的，那么更新序号
                	getFilesWS().updateClassFileOrFolderSerialnumber("FILES_"+companyId, classId, "0");
                }
				
				//加入回收站
				getFilesWS().addFileTrashById(companyId, folderId, userId);
				//修改索引库记录,20个文件调一次
				if(ids.size()<21){
					Map<String,Object> lucenemap = new HashMap<String, Object>();
					lucenemap.put("isDel", "1");
					lucenemap.put("companyId", companyId);
					lucenemap.put("ids", ids);
					getLuceneWS().updateIndexOfDel(lucenemap);
				}else{
					int j = ids.size()/20;
					if(ids.size()>20*j){
						j=j+1;
					}
					for(int i=0;i<j;i++){
						List<String> executeIds = null;
						if(i==j-1){
							executeIds = ids.subList(i*20,ids.size());
						}else{
							executeIds = ids.subList(i*20,i*20+20);
						}
						Map<String,Object> lucenemap = new HashMap<String, Object>();
						lucenemap.put("isDel", "1");
						lucenemap.put("companyId", companyId);
						lucenemap.put("ids", executeIds);
						getLuceneWS().updateIndexOfDel(lucenemap);
					}
				}
				
				//String groupFlag = documentClassDao.getFlagWithClassId(companyId, classId); 
				Map<String, String> groupObjeMap = documentClassDao.getFlagWithClassId(companyId, classId); 
				rtnmap.put("groupFlag", groupObjeMap.get("FLAG"));
				rtnmap.put("GROUPNAME", groupObjeMap.get("GROUPNAME"));
				rtnmap.put("success", "true");
				getFilesWS().updateCompanyClassesCache(companyId, userId, userName);
	            getFilesWS().updateCompanyFoldersCache(companyId);
				// 日志------------------------------------------
				Map<String, Map<String, String>> companyUserInitInfo = null;
				Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+companyId) ;
				String companyName="";
				if(obj != null){
					companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
					companyName = companyUserInitInfo.get(params.get("username")).get("COMPANYNAME");
				}
				HashMap<String, String> log = new HashMap<String, String>();
				log.put("userid",params.get("username"));
				log.put("username",params.get("username"));
				log.put("ip", params.get("ip"));
				log.put("module","删除文件夹");
				log.put("companyName", !"".equals(companyName)?companyName:companyId);
				// login 用户登录 access 功能访问 operation 功能操作
				log.put("type", "operation");
				log.put("loginfo", "删除文件夹,文件夹id为【"+classId+"】");
				logutils.saveBaseLog(this.getCompLocator(), log);
				// 日志------------------------------------------
			}else{
				rtnmap.put("success", "false");
			}
		}else{
			rtnmap.put("success", "false");
		}
		//只要有一个出现删除不了 就停止不再删除
		//当所有文件删除完，当前的文件夹也要删除
		return rtnmap;
	}

	public Map reClassNameById(Map<String, String> params) {
		Map<String, String> rtnmap = new HashMap<String, String>();
		String itemId = params.get("itemId");
		params.put("classId", itemId);
		params.put("companyId", params.get("companyId"));
		params.put("fatherClassId", params.get("fatherClassId"));
		params.put("className", params.get("className"));
		if(!documentClassDao.getClassByClassNameAndCompanyId(params)){
			rtnmap = documentClassDao.reClassNameById(params);
			String companyId = params.get("companyId");
			String className = params.get("className");
			String id = params.get("classId");
	        Map<String,List<Map<String,String>>> indexmap = new HashMap<String,List<Map<String,String>>>();
	        List<Map<String,String>> datalist = new ArrayList<Map<String,String>>();
	        Map<String,String> map = new HashMap<String,String>();
	            map.put("id", id);
	            map.put("fullFileName", className.toLowerCase());
	            map.put("fileName", className);
	            map.put("fullFileName_IK", className);
	            datalist.add(map);
	        indexmap.put(companyId, datalist);
	        //修改索引中关于文件名的字段信息
	        getLuceneWS().updateIndexComment(indexmap);
			// 日志------------------------------------------
			Map<String, Map<String, String>> companyUserInitInfo = null;
			Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+params.get("companyId")) ;
			String companyName="";
			if(obj != null){
				companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
				companyName = companyUserInitInfo.get(params.get("username")).get("COMPANYNAME");
			}
			HashMap<String, String> log = new HashMap<String, String>();
			log.put("userid",params.get("username"));
			log.put("username",params.get("username"));
			log.put("ip", params.get("ip"));
			log.put("module","分类重命名");
			log.put("companyName", !"".equals(companyName)?companyName:companyId);
			// login 用户登录 access 功能访问 operation 功能操作
			log.put("type", "operation");
			log.put("loginfo", "分类ID为【"+itemId+"】分类名为【"+params.get("oldCLassName")+"】的分类重命名为【"+params.get("className")+"】");
			logutils.saveBaseLog(this.getCompLocator(), log);
			// 日志------------------------------------------
			// 更新分类缓存
			getFilesWS().updateCompanyFoldersCache(companyId);
			rtnmap.put("ishave", "false");
			rtnmap.put("success", "true");
		}else{
			rtnmap.put("ishave", "true");
			rtnmap.put("success", "false");
		}
		return rtnmap;
	}

	public List gerClassList(Map<String, String> params) {
		List<HashMap<String, Object>> rtnList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> rtnMap = new HashMap<String, Object>();
		rtnList = documentClassDao.gerClassList(params);
		return rtnList;
	}

	@Override
	public Map editClassId(Map<String, String> params) {
		Map<String, String> rtnmap = new HashMap<String, String>();
		rtnmap = documentClassDao.editClassId(params);
		return rtnmap;
	}

	public Map addClassByNameAndCreateGroup(Map<String, String> params) {
	    String userId = params.get("userId");
	    String userName = params.get("username");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> json = new HashMap<String, String>();
		//1.创建分类先
		Map<String, String> rtnmap = new HashMap<String, String>();
		params.put("iscreateGroup", "1");
		rtnmap =this.addClassByName(params);
		if(!Boolean.valueOf(rtnmap.get("success").toString())){
			json.put("success", "false");
			json.put("ishave", "true");
			return json;
		}
		
		Map<String,String> createLuceneIndexParams = new HashMap<String, String>();
		createLuceneIndexParams.put("ID",rtnmap.get("id"));
		createLuceneIndexParams.put("COMPANYID",params.get("companyId"));
/*		createLuceneIndexParams.put("fileId", "0-0-0");
		createLuceneIndexParams.put("classId", params.get("classId") );
		createLuceneIndexParams.put("fileName",params.get("groupname") );
		createLuceneIndexParams.put("fileType", "");
		createLuceneIndexParams.put("insertId", rtnmap.get("id"));
		createLuceneIndexParams.put("idSeq", "1.");//由于这个方法就是创建分类的，故idseq默认1. 以后改动的时候需要注意这个地方
		createLuceneIndexParams.put("openlevel", "1");
		createLuceneIndexParams.put("size", "0");
		createLuceneIndexParams.put("createtime", df.format(new Date()));
		createLuceneIndexParams.put("createrId", params.get("userId") );
		createLuceneIndexParams.put("ip", params.get("ip"));
		createLuceneIndexParams.put("userid", params.get("userId") );
		createLuceneIndexParams.put("username", params.get("username") );
		createLuceneIndexParams.put("companyName", "");//记log用的，暂时先不传了
		createLuceneIndexParams.put("isFile", "0");
        createLuceneIndexParams.put("isDel", "0");
        createLuceneIndexParams.put("isLast", "1");
        createLuceneIndexParams.put("md5", "");*/
		
		getLuceneWS().createIndex(createLuceneIndexParams);
		
		if(!Boolean.valueOf(rtnmap.get("ishave").toString())){
			// 当前这个id是file_x表中的id字段  用来与group表的classId字段产生关系
			String classId = rtnmap.get("id");
			//2.创建分组
			String companyId = params.get("companyId");
			String username = params.get("username");
			String groupuserids = params.get("groupuserids");
			String manageruserid = params.get("manageruserid");
			String groupname = params.get("groupname");
			String groupremark = params.get("groupremark");
			try { //添加对%和+的处理
				groupname = groupname.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
				groupname = groupname.replaceAll("\\+", "%2B");
				groupname = URLDecoder.decode(groupname, "utf-8") ;
				
				groupremark = groupremark.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
				groupremark = groupremark.replaceAll("\\+", "%2B");
				groupremark = URLDecoder.decode(groupremark, "utf-8") ;
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String groupflag = "c"+companyId+"g"+System.currentTimeMillis() ;
			Date dt = new Date();
	        String time = df.format(dt);
			boolean isOk = documentClassDao.createGroup(companyId, username, groupuserids, manageruserid, groupname, groupremark, groupflag, time);
			
			if(isOk){
				json.put("GROUPNAME", groupname) ;
				json.put("REMARK", groupremark) ;
				json.put("CREATETIME", time) ;
				json.put("FLAG", groupflag) ;
				json.put("flag", groupflag) ;
				json.put("id", classId) ;
				//让groups表与file_n表产生关系------------
				Map<String, String> reMap = documentClassDao.editClassId(json);
				if(Boolean.valueOf(reMap.get("success").toString())){
				    
				    // 更新分类缓存
	                getFilesWS().updateCompanyClassesCache(companyId, userId, userName);
	                getFilesWS().updateCompanyFoldersCache(companyId);
				    
					// 日志------------------------------------------
					Map<String, Map<String, String>> companyUserInitInfo = null;
					Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo"+params.get("companyId")) ;
					String companyName="";
					if(obj != null){
						companyUserInitInfo = (Map<String, Map<String, String>>) obj ;
						companyName = companyUserInitInfo.get(params.get("username")).get("COMPANYNAME");
					}
					 HashMap<String, String> log = new HashMap<String, String>();
					 log.put("userid",params.get("username"));
					 log.put("username",params.get("username"));
					 log.put("ip", params.get("ip"));
					 log.put("module","添加分类/分组");
					 log.put("companyName", !"".equals(companyName)?companyName:companyId);
					// login 用户登录 access 功能访问 operation 功能操作
					 log.put("type", "operation");
					 log.put("loginfo", "添加分类/分组,添加分类/分组名为【"+params.get("className")+"】");
					 logutils.saveBaseLog(this.getCompLocator(), log);
					// 日志------------------------------------------
					json.put("success", "true");
					//20151110 创建群组安卓
					if(params.get("callback")!=null && params.get("callback").equals("androidInter")){
						 String arg = "secret=flyingsoft&type=add_group&groups="+groupflag+"&username="+username.replace("@", "\\40") ;
				         post(arg);
					}
				}else{
					json.put("success", "false");
				}
			} else {
			    json.put("success", "false");
			}
		}else{
			json.put("success", "have");
		}
		
		return json;
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
	
	
	//新
	@Override
	public Map getClassInfo(Map<String, String> param) {
		Map<String, String> rtnmap = new HashMap<String, String>();
		String companyId = param.get("companyId");
		String groupName = param.get("groupname");
		String groupid = param.get("groupid");
		String flag = param.get("flag");
		String userId = param.get("userId");
		Map<String, String> params = new HashMap<String, String>();
		params.put("companyId", companyId);
		params.put("groupName", groupName);
		params.put("groupid", groupid);
		params.put("flag", flag);
		params.put("userId", userId);
		rtnmap = documentClassDao.getClassInfo(params);
		List<String> groupHasdUsers = getChatWS().getGroupUsersByGroupId(groupid);
		if(groupHasdUsers.contains(userId)){
			rtnmap.put("isgroupuser", "true");
		}else{
			rtnmap.put("isgroupuser", "false");
		}
		Map<String, Object> json  = new HashMap<String, Object>();
		json.put("isOk", true);
		json.put("info", rtnmap);
//		output(request, response, json);
		return json;
	}
	
	@Override
	public void getClassInfo(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> rtnmap = new HashMap<String, String>();
		String companyId = request.getParameter("companyId");
		String groupName = request.getParameter("groupname");
		String groupid = request.getParameter("groupid");
		String flag = request.getParameter("flag");
		String userId = request.getParameter("userId");
		Map<String, String> params = new HashMap<String, String>();
		params.put("companyId", companyId);
		params.put("groupName", groupName);
		params.put("groupid", groupid);
		params.put("flag", flag);
		params.put("userId", userId);
		rtnmap = documentClassDao.getClassInfo(params);
		List<String> groupHasdUsers = getChatWS().getGroupUsersByGroupId(groupid);
		if(groupHasdUsers.contains(userId)){
			rtnmap.put("isgroupuser", "true");
		}else{
			rtnmap.put("isgroupuser", "false");
		}
		JSONObject json  = new JSONObject();
		json.put("isOk", true);
		json.put("info", rtnmap);
		output(request, response, json);
	}
	

	@Override
	public Map editClassInfo(Map<String, String> params) {
	    String companyId = params.get("companyId");
	    String userId = params.get("userId");
	    String userName = params.get("username");
		Map<String, String> rtnmap = new HashMap<String, String>();
		params.put("companyid", companyId);
		rtnmap = documentClassDao.getCateById(params);
		params.put("className", params.get("groupName"));
		params.put("fatherClassId", rtnmap.get("CLASSID"));
//		Map<String, String> map = documentClassDao.reClassNameById(params);
		if(!documentClassDao.getClassByClassNameAndCompanyId(params)){
			rtnmap = documentClassDao.editClassInfo(params);
			Map<String, String> map = documentClassDao.reClassNameById(params);
			if(Boolean.parseBoolean(map.get("success"))){
				rtnmap.put("success", "true");
			}else{
				rtnmap.put("success", "false");
			}
		}else{
			rtnmap.put("success", "have");
		}
		getFilesWS().updateCompanyClassesCache(companyId,userId,userName);
		return rtnmap;
	}

	
	private void output(HttpServletRequest request, HttpServletResponse response, JSONObject json){
		response.setContentType("text/javascript;charset=UTF-8");
		StringBuffer output = new StringBuffer(100);
		output.append(request.getParameter("callback")).append("(") ;
		output.append(json.toJSONString()).append(");") ;
		try {
			response.getWriter().println(output);
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, Object> changeGroupAdmin(Map<String, String> params){
	    String companyId = params.get("companyId");
	    String userId = params.get("userId");
	    String userName = params.get("username");
		Map<String, Object> map = documentClassDao.changeGroupAdmin(params);
		// 更新分类缓存
	    getFilesWS().updateCompanyClassesCache(companyId, userId, userName);
		return map;
	}
	
	/**
	 * lujixiang 20150810  批量删除文件夹和文件
	 */
	public void deleteFilesAndFolders(HttpServletRequest request, HttpServletResponse response){
		
		String userName = request.getParameter("username");
		String userId = request.getParameter("userId");
		String companyId = request.getParameter("companyId");
		String classId = request.getParameter("classId");			// 当前文件夹
		String folderIds = request.getParameter("foldIds");		// 选中的文件夹
		String fileIds = request.getParameter("fileIds");			// 选中的文件
		String callback = request.getParameter("callback");
		
		
		List<String> deleteIds = new ArrayList<String>();	// 需要删除所有文件夹和文件
		List<String> trashIds = new ArrayList<String>(); // 需要放入回收站的文件夹
		Map<String, Object> rtnmap = new HashMap<String, Object>();	// 返回删除结果
		Gson gson = new Gson();
		
		/*********************** 删除文件夹 --start **********************/
		
		if (null != folderIds && !"".equals(folderIds)) {
		
			
			String[] folderIdArr = folderIds.split(",");
			
			for (String tempFolderId : folderIdArr) {
				
				if (null == tempFolderId || "".equals(tempFolderId)) 	continue;
				
				deleteIds.add(tempFolderId);
				trashIds.add(tempFolderId);
				
				String idsql = documentClassDao.getChildByClassId(companyId, tempFolderId);		// idsql
				String newidsql = idsql+tempFolderId;
				List<String> tempIds = documentClassDao.getIdsByIdSeq(newidsql, companyId);		// 需要删除的删除文件和其下的所有文件夹和文件id
				if (null != tempIds && 0 < tempIds.size())  deleteIds.addAll(tempIds);
			}
		}
		
		/*********************** 删除文件夹 --end **********************/
		
		
		/*********************** 删除文件 --start **********************/
		if (null != fileIds && !"".equals(fileIds)) {
			
			String[] fileIdArr = fileIds.split(",") ;
			
			for (String tempFileId : fileIdArr) {
				
				if (null == tempFileId || "".equals(tempFileId))	continue ;
				deleteIds.add(tempFileId);
				trashIds.add(tempFileId);
			}
		}
		
		/*********************** 删除文件 --end **********************/
		
		
		/**********************  执行真正的删除  --start ****************/
		if (0 == deleteIds.size() || 0 == trashIds.size()) {
			
			rtnmap.put("success", false);
			writeJson(response, callback, gson.toJson(rtnmap));
			return;
		}
		
		// 删除文件夹和文件
		boolean isDelete = documentClassDao.updateDelById(deleteIds, companyId, "1");
		if (!isDelete) {
			rtnmap.put("success", false);
			writeJson(response, callback, gson.toJson(rtnmap));
			return;
		}
		
		//wangwenshuo 20160224  重排编号
		if(null != folderIds && !"".equals(folderIds))
			getFilesWS().updateClassFileOrFolderSerialnumber("files_"+companyId, classId, "0");
		if(null != fileIds && !"".equals(fileIds))
			getFilesWS().updateClassFileOrFolderSerialnumber("files_"+companyId, classId, "1");
		
		// 将文件夹放入回收站
		getFilesWS().addFilesTrashById(companyId, trashIds, userId);
					
		// 更新缓存
		getFilesWS().updateCompanyClassesCache(companyId, userId, userName);
		getFilesWS().updateCompanyFoldersCache(companyId);
					
		// 修改索引库记录
		//修改索引库记录,20个文件调一次
		if(deleteIds.size()<21){
			Map<String,Object> lucenemap = new HashMap<String, Object>();
			lucenemap.put("isDel", "1");
			lucenemap.put("companyId", companyId);
			lucenemap.put("ids", deleteIds);
			getLuceneWS().updateIndexOfDel(lucenemap);
		}else{
			int j = deleteIds.size()/20;
			if(deleteIds.size()>20*j){
				j=j+1;
			}
			for(int i=0;i<j;i++){
				List<String> executeIds = null;
				if(i==j-1){
					executeIds = deleteIds.subList(i*20,deleteIds.size());
				}else{
					executeIds = deleteIds.subList(i*20,i*20+20);
				}
				Map<String,Object> lucenemap = new HashMap<String, Object>();
				lucenemap.put("isDel", "1");
				lucenemap.put("companyId", companyId);
				lucenemap.put("ids", executeIds);
				getLuceneWS().updateIndexOfDel(lucenemap);
			}
		}		
		
		/**********************  执行真正的删除  --end ****************/
		
		rtnmap.put("success", true);
		writeJson(response, callback, gson.toJson(rtnmap));
		
	}
	/**
     * 输出json结果
     * @param response
     * @param json
     */
    private void writeJson(HttpServletResponse response, String callback, String json) {
        response.setContentType("text/javascript;charset=UTF-8");
        String data="";
        if(callback!=null && "androidInter".equals(callback)){
        	data = json;
        }else{
        	data = super.jsonpCallbackWithString(callback, json);
        }
        try {
            response.getWriter().println(data);
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
