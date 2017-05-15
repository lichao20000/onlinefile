package cn.flying.rest.onlinefile.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.flying.rest.admin.restInterface.BaseCacheWS;
import cn.flying.rest.onlinefile.entity.UserInfo;
import cn.flying.rest.onlinefile.files.driver.FilesDao;
import cn.flying.rest.onlinefile.files.util.LockUtil;
import cn.flying.rest.onlinefile.restInterface.ChatWS;
import cn.flying.rest.onlinefile.restInterface.FilePropertiesWS;
import cn.flying.rest.onlinefile.restInterface.FilesWS;
import cn.flying.rest.onlinefile.restInterface.LuceneWS;
import cn.flying.rest.onlinefile.restInterface.UserWS;
import cn.flying.rest.onlinefile.utils.BaseWS;
import cn.flying.rest.onlinefile.utils.BroadcastUtils;
import cn.flying.rest.onlinefile.utils.CacheUtils;
import cn.flying.rest.onlinefile.utils.EmojiUtil;
import cn.flying.rest.onlinefile.utils.LogUtils;
import cn.flying.rest.onlinefile.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

/**
 * 文档操作主要实现类
 * 
 * @author longjunhao
 * 
 */
@Path("/onlinefile_filesws")
@Component
@SuppressWarnings({"rawtypes", "unchecked"})
public class FilesWSImpl extends BaseWS implements FilesWS {

  @Autowired
  private FilesDao filesDao;

  private Gson gson = new Gson();

  private FilePropertiesWS fpws;

  private LuceneWS lucenews;

  private LuceneWS getLuceneWS() {
    if (lucenews == null) {
      lucenews = this.getService(LuceneWS.class);
    }
    return lucenews;
  }

  private FilePropertiesWS getFilePropertiesWS() {
    if (fpws == null) {
      fpws = this.getService(FilePropertiesWS.class);
    }
    return fpws;
  }

  private BaseCacheWS cacheWS;

  public BaseCacheWS getCacheWS() {
    if (cacheWS == null) {
      synchronized (BaseCacheWS.class) {
        if (cacheWS == null) {
          cacheWS = this.getService(BaseCacheWS.class);
        }
      }
    }
    return cacheWS;
  }

  private UserWS userWS;

  public UserWS getUserWS() {
    if (userWS == null) {
      synchronized (UserWS.class) {
        if (userWS == null) {
          userWS = this.getService(UserWS.class);
        }
      }
    }
    return userWS;
  }

  private ChatWS chatWS;

  public ChatWS getChatWS() {
    if (null == this.chatWS) {
      this.chatWS = this.getService(ChatWS.class);
    }
    return this.chatWS;
  }

  /**
   * 获取用户id
   * 
   * @param companyId
   * @param userName
   */
  private String getUserIdByName(String companyId, String userName) {
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
    }
    Map<String, String> user = companyUserInitInfo.get(userName);
    return user.get("ID");
  }

  /**
   * 从缓存获取当前企业的所有文件夹列表
   * 
   * @param companyId
   * @return
   */
  private List<Map<String, String>> getCompanyFolders(String companyId) {
    List<Map<String, String>> companyFolders =
        CacheUtils.getList(this.getCompLocator(), "companyFolders" + companyId);
    if (companyFolders == null || companyFolders.isEmpty()) {
      companyFolders = filesDao.getCompanyFolders(companyId);
      CacheUtils.set(this.getCompLocator(), "companyFolders" + companyId, companyFolders);
    }
    return companyFolders;
  }

  /**
   * 更新企业文件夹列表缓存
   * 
   * @param companyId
   */
  public void updateCompanyFoldersCache(String companyId) {
    List<Map<String, String>> companyFolders = filesDao.getCompanyFolders(companyId);
    CacheUtils.set(this.getCompLocator(), "companyFolders" + companyId, companyFolders);
  }

  /**
   * 输出json结果
   * 
   * @param response
   * @param json
   */
  private void writeJson(HttpServletResponse response, String callback, String json) {
    response.setContentType("text/javascript;charset=UTF-8");
    String data = "";
    if (callback != null && "androidInter".equals(callback)) {
      data = json;
    } else {
      data = super.jsonpCallbackWithString(callback, json);
    }
    try {
      response.getWriter().println(data);
      response.getWriter().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 查询文件,检索当前分类下的所有分类和文件
   */
  public void searchFile(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String loginUserId = request.getParameter("loginUserId");
    String createrId = request.getParameter("createrId");
    String idSeq = request.getParameter("idSeq");
    // String classId = request.getParameter("classId");
    String query = request.getParameter("query");
    String pageStr = request.getParameter("page");
    String pageSizeStr = request.getParameter("pageSize");
    String callback = request.getParameter("callback");
    String userName = request.getParameter("userName");
    String className = request.getParameter("className");
    String orderField = request.getParameter("orderField");
    String orderType = request.getParameter("orderType");
    if (orderField == null || orderField.trim().length() == 0) {
      orderType = null;
    }

    int page = Integer.parseInt(pageStr); // 1 2 3 4..
    int limit = Integer.parseInt(pageSizeStr);
    int start = (page - 1) * limit;

    // String idSeq = filesDao.getIdSeqWithId(companyId, classId);
    // idSeq += classId + ".";
    try {
      if (!"".equals(query))
        query = URLDecoder.decode(query, "utf-8");
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }
    Map<String, String> searchMap = new HashMap<String, String>();
    searchMap.put("keyWord", query);
    searchMap.put("companyId", companyId);
    searchMap.put("start", start + "");
    searchMap.put("limit", limit + "");
    searchMap.put("createrId", createrId);
    searchMap.put("idSeq", idSeq);
    searchMap.put("searchType", "1");// 1表示搜索文件
    searchMap.put("loginUserId", loginUserId);
    searchMap.put("orderField", orderField);
    searchMap.put("orderType", orderType);
    List<Map<String, String>> files = getLuceneWS().search(searchMap);
    int count = 0;
    if (files.size() > 1) {

      count = Integer.parseInt(files.get(0).get("total"));
      files = files.subList(1, files.size());
    }
    if (count > 0) {
      setupParentClassName(companyId, files);
      setupFilesCreatorName(companyId, files);
      // 赋予浏览权限
      setupUserReadRight(companyId, loginUserId, files, false);
      setupPraise(companyId, loginUserId, files);
      setupClassGroupInfo(companyId, loginUserId, files);
    } else {
      files.remove(0);
    }
    int totalPage = (count + limit - 1) / limit;
    Map result = new HashMap();
    result.put("files", files);
    result.put("size", files.size());
    result.put("total", totalPage);
    result.put("page", page);
    result.put("count", count);
    result.put("hidePageMenu", "true");
    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", request.getRemoteAddr());
    log.put("userid", userName);
    log.put("loginfo", "检索【" + className + "】分类下的所有分类和文件,关键字为【" + query + "】");
    log.put("module", "文件检索");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "文档搜索");
    LogUtils.saveBaseLog(compLocator, log);
    // 日志------------------------------------
    writeJson(response, callback, gson.toJson(result));
  }

  /**
   * 更新企业左侧分类列表缓存
   */
  public void updateCompanyClassesCache(String companyId, String userId, String userName) {


    List<Map<String, String>> classList = filesDao.getClassList(companyId);
    Map<String, Map<String, String>> groupList =
        filesDao.getCompanyGroups(companyId, userName, userId);
    Map<String, String> map = null;
    for (Map<String, String> classMap : classList) {
      map = groupList.get(classMap.get("id"));
      if (map != null) {
        classMap.put("groupId", map.get("id"));
        classMap.put("flag", map.get("flag"));
        classMap.put("remark", map.get("remark"));
      }
    }
    CacheUtils.set(this.getCompLocator(), "companyClasses" + companyId, classList);
  }

  /**
   * 获取分类列表
   * 
   * @author longjunhao 20150504
   * @param params
   * @return
   */
  public void getClassList(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String userId = request.getParameter("userId");
    String username = request.getParameter("username");
    String callback = request.getParameter("callback");
    // 从缓存获取
    List<Map<String, String>> classList =
        CacheUtils.getList(this.getCompLocator(), "companyClasses" + companyId);
    if (classList == null || classList.isEmpty()) {
      this.updateCompanyClassesCache(companyId, userId, username);
      classList = CacheUtils.getList(this.getCompLocator(), "companyClasses" + companyId);
    }

    List<String> starIds = filesDao.getClassStarIds(userId, companyId);
    for (Map<String, String> tmpMap : classList) {
      if (null != tmpMap.get("id") && starIds.contains(tmpMap.get("id"))) {
        if (null != tmpMap.get("flag")) {
          tmpMap.put("isStar", "true");
        }
      } else {
        tmpMap.put("isStar", "false");
      }
    }
    // 20151119 查询每组分类图片
    Map<String, String> classImg = filesDao.getClassImg(companyId);
    Map<String, String> maps = null;
    List<Map<String, String>> groups = new ArrayList<Map<String, String>>();

    for (String map1 : classImg.keySet()) {
      maps = new HashMap<String, String>();
      maps.put("FLAG", map1);
      maps.put("PORTRAITS", classImg.get(map1));
      groups.add(maps);
    }
    List<Map<String, String>> groupss =
        getChatWS().doGroupImageMath(companyId, username, "", groups);
    if (groupss != null)
      for (Map<String, String> strMap : groupss) {
        classImg.put(strMap.get("FLAG"), strMap.get("PORTRAITS"));
      }

    // 添加当前用户所属分类群组的 消息数目、时间等信息
    this.filesDao.getClassExtraInfo(classList, companyId, username, userId);

    // 当前用户关联的分组id列表
    List<String> groupIds = filesDao.getGroupIdsWithUserId(userId);
    for (Map<String, String> classMap : classList) {
      if (groupIds.contains(classMap.get("groupId"))) {
        classMap.put("isMember", "true");
        classMap.put("ordermsgcount", classMap.get("newmessagecount"));
      } else {
        classMap.put("isMember", "false");
        classMap.put("ordermsgcount", "-1"); // 没有权限的排在最后
      }
      classMap.put("PORTRAITS",
          classImg.get(classMap.get("flag")) != null ? classImg.get(classMap.get("flag")) : "");
    }
    JSONObject json = new JSONObject();
    /** 对分类进行排序 消息数量多的在前面 **/
    Collections.sort(classList, new ClassComparator("ordermsgcount"));
    json.put("classes", classList);
    writeJson(response, callback, json.toJSONString());
  }

  /**
   * 获取文件列表
   * 
   * @author longjunhao 20150317
   * @param params
   * @return
   */
  public void getFileList(HttpServletRequest request, HttpServletResponse response) {
    String userName = request.getParameter("userName");
    String companyId = request.getParameter("companyId");
    String loginUserId = request.getParameter("loginUserId");
    String groupId = request.getParameter("groupId");
    String userId = request.getParameter("userId");
    String classId = request.getParameter("classId");
    String pageStr = request.getParameter("page");
    String pageSizeStr = request.getParameter("pageSize");
    String callback = request.getParameter("callback");
    String idseq = request.getParameter("idseq");
    int page = Integer.parseInt(pageStr);
    int limit = Integer.parseInt(pageSizeStr);
    int start = (page - 1) * limit;
    String orderField = request.getParameter("orderField");
    String orderType = request.getParameter("orderType");
    if (orderField == null || orderField.trim().length() == 0) {
      orderType = null;
    }

    // 如果传过来的companyId包含user_字符串 说明是我的文档部分
    boolean isMyDocument = companyId.contains("user_");

    // 判断用户时候属于某个分组###
    boolean isMember = filesDao.isGroupMember(groupId, loginUserId);

    List<String> fileIds =
        filesDao.getFileIdList(companyId, classId, userId, start, limit, orderField, orderType);
    // 获取文件列表
    ArrayList<Map<String, String>> files = filesDao.getFileListByIds(companyId, fileIds);
    // 获取文件（文件和文件夹）总条数
    int count = filesDao.getFileListCount(companyId, classId, userId);
    // 获取文件总数目
    int fileCount = filesDao.getFileCount(companyId, classId, userId);

    // 设置父级的分类名称
    setupParentClassName(companyId, files);
    // 文件路径
    setupFolderPathName(companyId, files);
    if (!isMyDocument) {
      // 文件权限拥有者（第一版上传人） wangwenshuo 20151229 add
      setupRightOwner(companyId, files);
      // 设置文件上传者的username
      setupFilesCreatorName(companyId, files);
      // 赋予浏览权限
      setupUserReadRight(companyId, loginUserId, files, isMember);
      // 赞
      setupPraise(companyId, loginUserId, files);
      // 设置分类的分组信息
      setupClassGroupInfo(companyId, loginUserId, files);
    } else {
      isMember = true;
    }
    // 计算总页数
    int total = (count + limit - 1) / limit;
    Map result = new HashMap();
    result.put("isMember", isMember);
    result.put("files", files);
    result.put("size", files.size());
    result.put("total", total);
    result.put("page", page);
    result.put("fileDirMsg", "共" + (count - fileCount) + "个文件夹，" + fileCount + "个文件");
    if (idseq != null && idseq.trim().length() > 0) {// 如果传来idseq 说明需要全部导航信息，返回之
      result.put("folderObjsJson", filesDao.getClassByIdseq(idseq, companyId));
    }
    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", request.getRemoteAddr());
    log.put("userid", userName);
    String className = files.size() > 0 ? files.get(0).get("className") : "";
    log.put("loginfo", "获得ID为【" + classId + "】名称为【" + className + "】分类下所有文件列表");
    log.put("module", "文件列表");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "access");
    log.put("operate", "文档");
    LogUtils.saveBaseLog(compLocator, log);
    // 日志------------------------------------
    writeJson(response, callback, gson.toJson(result));
  }



  /**
   * 我的获取文件列表
   * 
   * @author longjunhao 20150505
   * @param params
   * @return
   */
  public void getMyFileList(@Context HttpServletRequest request,
      @Context HttpServletResponse response) {
    String userName = request.getParameter("userName");
    String companyId = request.getParameter("companyId");
    String loginUserId = request.getParameter("loginUserId");
    String pageStr = request.getParameter("page");
    String pageSizeStr = request.getParameter("pageSize");
    String callback = request.getParameter("callback");

    int page = Integer.parseInt(pageStr); // 1 2 3 4..
    int limit = Integer.parseInt(pageSizeStr);
    int start = (page - 1) * limit;
    String orderField = request.getParameter("orderField");
    String orderType = request.getParameter("orderType");
    if (orderField == null || orderField.trim().length() == 0) {
      orderType = null;
    }
    List<String> fileIds =
        filesDao.getMyFileIdList(companyId, loginUserId, start, limit, orderType, orderField);
    // 获取文件列表
    ArrayList<Map<String, String>> files = filesDao.getFileListByIds(companyId, fileIds);
    // 获取文件总条数
    int count = filesDao.getMyFileListCount(companyId, loginUserId);
    // 设置文件上传者的username
    setupFilesCreatorName(companyId, files);
    setupParentClassName(companyId, files);
    setupFolderPathName(companyId, files);
    // 赋予浏览权限
    setupUserReadRight(companyId, loginUserId, files, false);
    // 赞
    setupPraise(companyId, loginUserId, files);

    // 计算总页数
    int total = (count + limit - 1) / limit;
    Map result = new HashMap();
    result.put("files", files);
    result.put("size", files.size());
    result.put("total", total);
    result.put("page", page);
    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", request.getRemoteAddr());
    log.put("userid", userName);
    log.put("loginfo", "获得自己所有文件列表");
    log.put("module", "文件列表");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "access");
    log.put("operate", "我的文档");
    LogUtils.saveBaseLog(compLocator, log);
    // 日志------------------------------------
    writeJson(response, callback, gson.toJson(result));
  }

  /**
   * 设置文件上传者的username
   * 
   * @param companyId
   * @param files
   */
  private void setupFilesCreatorName(String companyId, List<Map<String, String>> files) {
    List<Map<String, String>> members = filesDao.getMembers(companyId);
    for (Map<String, String> file : files) {
      String creatorId = file.get("userId");
      String owner = file.get("owner");
      String owner_v1 = file.get("owner_v1");
      if (!"0".equals(creatorId)) {
        for (Map<String, String> member : members) {
          if (creatorId.equals(member.get("id"))) {
            file.put("userName", member.get("userName"));
            file.put("fullName", member.get("fullName"));
          }
          if (owner != null && owner.equals(member.get("id"))) {
            file.put("ownerUserName", member.get("userName"));
            file.put("ownerFullName", member.get("fullName"));
          }

          // wangwenshuo 20151229 add 文件权限拥有者
          if (owner_v1 != null && owner_v1.equals(member.get("id"))) {
            file.put("ownerV1UserName", member.get("userName"));
            file.put("ownerV1FullName", member.get("fullName"));
          }
        }
      }
    }
  }

  /**
   * 设置文件删除者的username
   * 
   * @param companyId
   * @param files
   */
  private void setupFilesDeleteUserName(String companyId, List<Map<String, String>> trashFiles) {
    List<Map<String, String>> members = filesDao.getMembers(companyId);
    for (Map<String, String> file : trashFiles) {
      String deleteUserId = file.get("deleteUserId");
      String owner = file.get("owner");
      if (!"0".equals(deleteUserId)) {
        for (Map<String, String> member : members) {
          if (deleteUserId.equals(member.get("id"))) {
            file.put("deleteUserName", member.get("userName"));
            file.put("deleteFullName", member.get("fullName"));
          }
          if (owner != null && owner.equals(member.get("id"))) {
            file.put("ownerUserName", member.get("userName"));
            file.put("ownerFullName", member.get("fullName"));
          }
        }
      }
    }
  }

  /**
   * 设置父级的分类名称
   * 
   * @param companyId
   * @param files
   */
  private void setupParentClassName(String companyId, List<Map<String, String>> files) {
    List<Map<String, String>> companyClasses = this.getCompanyFolders(companyId);
    for (Map<String, String> file : files) {
      for (Map<String, String> companyClass : companyClasses) {
        if (companyClass.get("ID").equals(file.get("classId"))) {
          file.put("className", companyClass.get("FILENAME"));
        }
      }
    }
  }

  /**
   * 设置浏览权限
   * 
   * @param companyId 企业id
   * @param userId 当前用户id
   * @param files 目标文件列表
   * @param isMember 是否分类的成员
   * @return
   */
  private List<Map<String, String>> setupUserReadRight(String companyId, String userId,
      List<Map<String, String>> files, boolean isMember) {
    // 获取用户所在的联系人群组id集合
    // List<String> groupIds = filesDao.getGroupIdsWithUserId(userId);
    List<String> groupIds = new ArrayList<String>();
    if (isMember) {
      String classId = "-1";
      if (files.size() > 0)
        classId = files.get(0).get("idSeq").split("\\.")[1];
      String groupId = filesDao.getGroupIdByClassId(classId, companyId);
      groupIds.add(groupId);
    }
    // 获取该用户有权限的fileid 如果文件分享给了聊天或者用户都 那么组内成员和单个用户都有权限了
    Map<String, Map<String, String>> fileIds =
        filesDao.getFileUserRelationWithUserId(companyId, userId, groupIds);
    for (Map<String, String> file : files) {
      file.put("isMember", isMember + "");
      String openLevel = file.get("openLevel");
      // 判断文件的公开权限
      // 1.版本创建者有权限 2.非私密文件成员拥有权限 3.文件分享者有权限
      if (userId.equals(file.get("owner_v1"))) {
        file.put("hasRight", "1");
        file.put("isDownload", "1");
        file.put("isLook", "1");
      } else if ("2".equals(openLevel) && isMember) {
        file.put("hasRight", "1");
        file.put("isDownload", "1");
        file.put("isLook", "1");
      } else if ("1".equals(openLevel)) {
        file.put("hasRight", "1");
        file.put("isDownload", "1");
        file.put("isLook", "1");
      } else if (!fileIds.isEmpty()
          && (fileIds.get(file.get("id") + "_0") != null || fileIds.get(file.get("soleNumber")
              + "_1") != null)) {
        file.put("hasRight", "1");
        if (fileIds.get(file.get("id") + "_0") != null) {
          file.put("isDownload", fileIds.get(file.get("id") + "_0").get("isDownload"));
          file.put("isLook", fileIds.get(file.get("id") + "_0").get("isLook"));
        }
        if (fileIds.get(file.get("soleNumber") + "_1") != null) {
          if (file.get("isDownload") == null || file.get("isDownload").equals("0")) // 判断的原因是，如果已经有权限了就没必要在设置权限
            file.put("isDownload", fileIds.get(file.get("soleNumber") + "_1").get("isDownload"));
          if (file.get("isLook") == null || file.get("isLook").equals("0"))
            file.put("isLook", fileIds.get(file.get("soleNumber") + "_1").get("isLook"));
        }
      } else {
        file.put("hasRight", "0");
        file.put("isDownload", "0");
        file.put("isLook", "0");
      }

      // wangwenshuo 删除权限控制 私密文件只有创建者可以删除 非私密文件只有拥有者可以删除
      String openlevel = file.get("openLevel");
      if (("3".equals(openlevel) && userId.equals(file.get("owner_v1")))
          || (!"3".equals(openlevel) && userId.equals(file.get("owner")))) {
        file.put("hasDeleteRight", "true");
      } else {
        file.put("hasDeleteRight", "false");
      }

      /*
       * if ("2".equals(openLevel)) { // 分类内公开 if (userId.equals(file.get("owner"))) { // 是否是文件拥有者
       * file.put("hasRight", "1"); } else { file.put("hasRight", (isMember?"1":"0")); } } else if
       * ("1".equals(openLevel)) { // 公司内公开 file.put("hasRight", "1"); } else if
       * (fileIds.contains(file.get("id"))) { // 被分享 file.put("hasRight", "1"); } else {
       * file.put("hasRight", "0"); }
       */
    }
    return files;
  }

  /**
   * xyc 判断文件是否具有下载，浏览权限
   * 
   * @param companyId 企业id
   * @param userId 当前用户id
   * @param files 目标文件Map wangwenshuo 20151215 添加groupFlag 用于分享到聊天窗口 权限判断
   * @return
   */
  private Map<String, String> hasUserIsDownloadAndLook(String companyId, String userId,
      Map<String, String> file, String groupFlag) {
    Map<String, String> returnMap = new HashMap<String, String>();
    boolean hasRight = false;
    int isDownload = 1;
    int isLook = 1;
    String classId = "";
    String idSeq = file.get("idSeq");
    if (idSeq != null && idSeq.indexOf(".") > 0) {
      classId = file.get("idSeq").split("\\.")[1];
    } else {
      returnMap.put("hasRight", hasRight + "");
      return returnMap;
    }
    // 分类下点击文件是的groupId;
    String groupId = filesDao.getGroupIdByClassId(classId, companyId);
    boolean isMember = filesDao.isGroupMember(groupId, userId);
    List<Map<String, String>> shareList = new ArrayList<Map<String, String>>();
    List<Map<String, String>> soleNumShareList = null;
    if (groupFlag != null && !"".equals(groupFlag)) {
    	// 分享目标群组id
    	String targetId = filesDao.getGroupInfoByFlag(companyId, groupFlag).get("id");
      	// 获取文件是否别分享的权限
      	shareList = filesDao.getFileUserRelationByFileId(companyId, userId, file.get("id"), targetId);
      	// 获取根据唯一标识分享的权限
      	soleNumShareList = filesDao.getFileUserRelationBySoleNumber(companyId, userId, file.get("soleNumber"), targetId);
    } else {
    	/* 如果不是当前群组成员 群组的权限不能给当前用户  */
    	if(!isMember) groupId = "-1";
    	shareList = filesDao.getFileUserRelationByFileId(companyId, userId, file.get("id"), groupId);
    	soleNumShareList = filesDao.getFileUserRelationBySoleNumber(companyId, userId, file.get("soleNumber"), groupId);
    }

    String openLevel = file.get("openLevel");
    file.put("isMember", isMember + "");
    if (userId.equals(file.get("owner_v1"))) {
      hasRight = true;
    } else if ("1".equals(openLevel)) { // 公司级公开
      hasRight = true;
    } else if ("2".equals(openLevel) && isMember) { // 分类级公开
      hasRight = true;
    } else if ((shareList != null && shareList.size() > 0) || (soleNumShareList != null && soleNumShareList.size() > 0)) {
      hasRight = true;
      isDownload = 0;
      isLook = 0;
      // 分享到分类下，分享到个人，都需要做权限鍀情况下，分享鍀个人权限大于分类
      // xiewenda 进行权限叠加操作
      for (Map<String, String> s : shareList) {
        isDownload = isDownload | Integer.parseInt(s.get("isDownload"));
        isLook = isLook | Integer.parseInt(s.get("isLook"));
      }

      // 根据唯一标识获取权限
      for (Map<String, String> s : soleNumShareList) {
        isDownload = isDownload | Integer.parseInt(s.get("isDownload"));
        isLook = isLook | Integer.parseInt(s.get("isLook"));
      }
    } else {
      hasRight = false;
      isDownload = 0;
      isLook = 0;
    }
    returnMap.put("hasRight", hasRight + "");
    returnMap.put("isDownload", isDownload + "");
    returnMap.put("isLook", isLook + "");
    return returnMap;
  }


  /**
   * 是否有浏览权限
   * 
   * @param companyId 企业id
   * @param userId 当前用户id
   * @param files 目标文件Map
   * @return
   */
  private boolean hasUserReadRight(String companyId, String userId, Map<String, String> file) {
    boolean hasRight = false;
    // String classId = file.get("classId");;
    String classId = "";
    String idSeq = file.get("idSeq");
    if (idSeq != null && idSeq.indexOf(".") > 0) {
      classId = file.get("idSeq").split("\\.")[1];
    } else {
      return false;
    }
    String groupId = filesDao.getGroupIdByClassId(classId, companyId);
    // 获取文件是否别分享的权限
    List<Map<String, String>> shareList =
        filesDao.getFileUserRelationByFileId(companyId, userId, file.get("id"), groupId);
    List<Map<String, String>> soleNumShareList =
        filesDao
            .getFileUserRelationBySoleNumber(companyId, userId, file.get("soleNumber"), groupId);
    // 文件的公开级别 
    //1.版本创建者有权限 2.非私密文件成员拥有权限 3.文件被分享者有权限
    String openLevel = file.get("openLevel");
    if (userId.equals(file.get("owner_v1"))) {
      hasRight = true;
    } else if ("1".equals(openLevel)) { // 公司级公开
      hasRight = true;
    } else if ("2".equals(openLevel) && filesDao.isGroupMember(groupId, userId)) { // 分类级公开
      hasRight = true;
    } else if (shareList.size() > 0 || soleNumShareList.size() > 0) {
      hasRight = true;
    } else {
      hasRight = false;
    }
    /*
     * xiewenda 注释掉 影响请打开 // 获取用户所在的联系人群组id集合 lujixiang 20150804 获取文档权限时,忽略文档版本号 --end List<String>
     * groupIds = filesDao.getGroupIdsWithUserId(userId);
     * 
     * hasRight = filesDao.hasRightIgnoreVersion(Integer.parseInt(companyId),
     * Integer.parseInt(userId), Integer.parseInt(file.get("classId")), file.get("fileName"),
     * file.get("type"), groupIds) ;
     */
    return hasRight;
  }
  /**
   * 是否有浏览权限
   * 
   * @param companyId 企业id
   * @param userId 当前用户id
   * @param files 目标文件Map
   * @return
   */
  private boolean hasUserDeleteRight(String companyId, String userId, Map<String, String> file) {
    boolean hasRight = false;
    // 文件的公开级别 
    //1.分类创建者 2.私密文件第一版本拥有者有权限3.非私密文件文件拥有者有权限
    String openLevel = file.get("openLevel");
    
    if ("3".equals(openLevel)) {
      Map<String, String> fileV1 =
          filesDao.getFileFirstVersion(companyId, file.get("classId"),file.get("fileName") ,file.get("type"),file.get("soleNumber"));
      if(userId.equals(fileV1.get("owner_v1")))
         hasRight = true;
     
    } else{ // 公司级公开
      if(userId.equals(file.get("owner")))
      hasRight = true;
      
    }
    return hasRight;
  }

  /**
   * 是否有浏览权限 我的文档分线到聊天窗口 对面浏览 查看，下载等权限检查
   * 
   * @param companyId 企业id
   * @param userId 当前用户id
   * @param files 目标文件Map
   * @return
   */
  private Map<String, String> hasUserReadRight4MyDocShare(String companyId, String toCompanyId,
      String groupFlag, String userId, Map<String, String> file) {
    Map<String, String> m = new HashMap<String, String>();
    if (userId.equals(file.get("userId"))) {
      m.put("isDownload", "1");
      m.put("isLook", "1");
      return m;
    }
    String groupId = filesDao.getGroupInfoByFlag(toCompanyId, groupFlag).get("id");
    // 获取文件是否别分享的权限
    List<Map<String, String>> shareList =
        filesDao.getFileUserRelationByFileId(companyId, userId, file.get("id"), groupId);

    // 群组或者个人 有一个有权限 就有权限
    for (Map<String, String> s : shareList) {
      if ("1".equals(s.get("isDownload"))) {
        m.put("isDownload", s.get("isDownload"));
      }
      if ("1".equals(s.get("isLook"))) {
        m.put("isLook", s.get("isLook"));
      }
    }
    return m;
  }

  /**
   * 设置赞
   * 
   * @param companyId 企业id
   * @param userId 当前用户id
   * @param files 目标文件列表
   * @return
   */
  private List<Map<String, String>> setupPraise(String companyId, String userId,
      List<Map<String, String>> files) {
    // 获取该用户有权限的fileid
    List<String> fileIds = filesDao.getPraiseFileIdsWithUserId(companyId, userId);
    for (Map<String, String> file : files) {
      if (fileIds.contains(file.get("id"))) {
        file.put("hasUp", "1");
      } else {
        file.put("hasUp", "0");
      }
    }
    return files;
  }

  /**
   * 设置分类的分组信息
   * 
   * @author longjunhao 20150403
   * @param companyId
   * @param userId
   * @param files
   * @return
   */
  private List<Map<String, String>> setupClassGroupInfo(String companyId, String userId,
      List<Map<String, String>> files) {
    // 记录分类id列表
    List<String> classIdList = new ArrayList<String>();
    for (Map<String, String> file : files) {
      String classId = file.get("idSeq").split("\\.")[1];
      if ("0".equals(file.get("isFile"))) {
        if (!classIdList.contains(classId))
          classIdList.add(classId);
      }
    }
    // 获取用户所属的分类组list
    List<Map<String, String>> classGroups = new ArrayList<Map<String, String>>();
    if (classIdList.size() > 0) {
      classGroups = filesDao.getClassGroups(companyId, classIdList);
      // 找出对应的分类信息，并写入分组数据
      for (Map<String, String> file : files) {
        String classId = file.get("idSeq").split("\\.")[1];
        if ("0".equals(file.get("isFile"))) {
          for (Map<String, String> classGroup : classGroups) {
            if (classGroup.get("classId").equals(classId)) {
              file.put("groupId", classGroup.get("id"));
              file.put("groupName", classGroup.get("groupName"));
              file.put("flag", classGroup.get("flag"));
            }
          }
        }
      }
    }
    return files;
  }

  /**
   * 设置历史版本的文件上传者是否被关注
   * 
   * @author wangwenshuo 20160112
   * @param userId
   * @param files
   * @return
   */
  private List<Map<String, String>> setupHasSubScribeUser(String userId,
      List<Map<String, String>> files) {
    // 使用map缓存一下结果 已经查询过的 就不再去数据库查询了 如果后续有优化可以考虑删除
    Map<String, String> subScribeUserMap = new HashMap<String, String>();
    for (Map<String, String> file : files) {
      if (userId.equals(file.get("userId"))) {
        file.put("hasSubScribeUser", "false");
      } else {
        String userName = file.get("userName");
        if (subScribeUserMap.containsKey(userName)) {
        } else {
          boolean hasSubScribeUser = filesDao.hasSubScribeUser(userId, file.get("userName"));
          subScribeUserMap.put(userName, hasSubScribeUser + "");
        }
        file.put("hasSubScribeUser", subScribeUserMap.get(userName));
      }
    }
    return files;
  }

  public void getFileInfo(HttpServletRequest request, HttpServletResponse response) {
    String userName = request.getParameter("userName");
    String companyId = request.getParameter("companyId");

    String fileId = request.getParameter("fileId");
    String userId = request.getParameter("userId"); // 当前登录用户
    String callback = request.getParameter("callback");
    String groupFlag = request.getParameter("groupFlag");
    List<Map<String, String>> fpLst = null;
    // 如果传过来的companyId包含user_字符串 说明是我的文档部分
    boolean isMyDocument = companyId.contains("user_");
    // 暂时不用，先注释掉
    // List<Map<String,String>> fpLst = getFilePropertiesWS().getFilePropLstByCompany(companyId);
    Map<String, String> file = filesDao.getFileInfo(companyId, fileId, fpLst);

    Map<String, Object> res = new HashMap<String, Object>(); // 返回数据
    if (isMyDocument) {
      // 我的文档分享出去的文件 对方下载或者查看的时候进行权限检查用 companyId实际为user_N toCompanyId为当前公司Id
      String toCompanyId = request.getParameter("toCompanyId");
      this.setupFolderPathName(companyId, file);
      String fileName = file.get("fileName");
      List<Map<String, String>> files = new ArrayList<Map<String, String>>();
      files.add(file);
      this.setupParentClassName(companyId, files);
      res.put("file", file);
      Map<String, String> userById = this.getUserWS().getUserById(file.get("userId"));
      Map<String, String> creator = new HashMap<String, String>();
      creator.put("id", userById.get("ID"));
      creator.put("userName", userById.get("USERNAME"));
      creator.put("fullName", userById.get("FULLNAME"));
      creator.put("portrait", userById.get("PORTRAIT"));
      res.put("creator", creator);
      res.put("isOwner", userId.equals(file.get("userId")));
      res.put("isV1Owner", userId.equals(file.get("userId")));
      res.put("owner", creator);
      res.put("ownerV1", creator);
      Map<String, String> rigthsMap =
          this.hasUserReadRight4MyDocShare(companyId, toCompanyId, groupFlag, userId, file);
      res.put("hasRight", !rigthsMap.isEmpty() + "");
      res.put("isDownload", rigthsMap.get("isDownload"));
      res.put("isLook", rigthsMap.get("isLook"));
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", request.getRemoteAddr());
      log.put("userid", userName);
      log.put("loginfo", "获得文件【" + fileName + "】ID为【" + fileId + "】的信息");
      log.put("module", "文件列表");
      log.put("username", userName);
      log.put("companyName", (companyName != null || "".equals(companyName)) ? companyName
          : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "access");
      log.put("operate", "所有文档/我的文档");
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    } else {
      // String classId = file.get("classId"); // 文件所在的文件夹id

      String[] classIds = file.get("idSeq").split("\\.");
      String classId = classIds[1];
      if ("1".equals(file.get("version"))) {
        file.put("owner_v1", file.get("owner"));
      } else {
        String fileName = file.get("fileName");
        String fileType = file.get("type");
        String soleNumber = file.get("soleNumber");
        String folderId = file.get("classId");
        Map<String, String> fileV1 =
            filesDao.getFileFirstVersion(companyId, folderId, fileName, fileType, soleNumber);
        file.put("owner_v1", fileV1.get("owner"));
      }
      if (!"0".equals(classId)) {
        Map<String, String> groupMap = filesDao.getFlagWithClassId(companyId, classId);
        file.put("groupFlag", groupMap.get("FLAG"));
        this.setupFolderPathName(companyId, file);
      }
      // 赞
      String fileName = file.get("fileName");
      List<Map<String, String>> files = new ArrayList<Map<String, String>>();
      files.add(file);
      this.setupParentClassName(companyId, files);
      this.setupPraise(companyId, userId, files);
      Map<String, String> creator = filesDao.getMemberById(companyId, file.get("userId"));
      Map<String, String> ownerV1 = filesDao.getMemberById(companyId, file.get("owner_v1"));
      Map<String, String> owner = new HashMap<String, String>();
      owner = filesDao.getMemberById(companyId, file.get("owner"));
      // 判断当前用户是否已经点赞
      if (filesDao.getUserIsPraise(companyId, userId, fileId)) {
        file.put("ispraise", "true");
      } else {
        file.put("ispraise", "false");
      }
      // 判断当前用户时候有订阅该文件的上传者
      boolean hasSubScribeUser = filesDao.hasSubScribeUser(userId, creator.get("userName"));
      res.put("hasSubScribeUser", hasSubScribeUser);
      // 判断当前用户时候有权限
      // if(this.hasUserReadRight(companyId, userId, file)) {
      Map<String, String> ishasUserMap =
          this.hasUserIsDownloadAndLook(companyId, userId, file, groupFlag);
      if ("true".equals(ishasUserMap.get("hasRight"))) {
        file.put("hasRight", "1");
        res.put("isOwner", userId.equals(file.get("owner")));
        res.put("isV1Owner", userId.equals(file.get("owner_v1")));
        res.put("file", file);
        res.put("creator", creator);
        res.put("owner", owner);
        res.put("ownerV1", ownerV1);
        res.put("hasRight", "true");
        res.put("isDownload", ishasUserMap.get("isDownload"));
        res.put("isLook", ishasUserMap.get("isLook"));
        // 日志------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
        String companyName = "";
        if (obj != null) {
          companyUserInitInfo = (Map<String, Map<String, String>>) obj;
          companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("ip", request.getRemoteAddr());
        log.put("userid", userName);
        log.put("loginfo", "获得文件【" + fileName + "】ID为【" + fileId + "】的信息");
        log.put("module", "文件列表");
        log.put("username", userName);
        log.put("companyName", (companyName != null || "".equals(companyName)) ? companyName
            : companyId);
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "access");
        log.put("operate", "所有文档/我的文档");
        LogUtils.saveBaseLog(compLocator, log);
        // 日志------------------------------------
      } else {
        res.put("isOwner", false);
        res.put("file", file);
        res.put("creator", creator);
        res.put("owner", owner);
        res.put("ownerV1", ownerV1);
        res.put("hasRight", "false");
        res.put("isDownload", "0");
        res.put("isLook", "0");

      }
    }
    writeJson(response, callback, gson.toJson(res));
  }

  /**
   * lujixiang 20150806 根据用户选择的文件夹和文件返回（过滤）不可以删除的文件夹和文件
   **/
  public void getFilesAndFoldsCanNotDelete(HttpServletRequest request, HttpServletResponse response) {

    String userName = request.getParameter("userName");
    String companyId = request.getParameter("companyId");
    String foldIds = request.getParameter("foldIds"); // 选中的文件夹
    String fileIds = request.getParameter("fileIds"); // 选中的文件
    String userId = request.getParameter("userId"); // 当前登录用户
    String callback = request.getParameter("callback");
    String groupFlag = "";
    /** 返回结果 **/
    Map<String, Object> res = new HashMap<String, Object>();

    String hasDeleteFoldsId = ""; // 无法删除的文件夹（该文件夹已经被删除）
    String hasNoRightFoldsId = ""; // 无法删除的文件夹（该用户不是分类成员）
    String hasDeleteFilesId = ""; // 无法删除的文件（该文件已经被删除）
    String hasNoRightFilesId = ""; // 无法删除的文件（没有权限）
    String hasMoreVersion = ""; // 存在多个版本的文件(没有权限)liuwei



    /** 过滤选中的文件夹 **/
    if (null != foldIds && !"".equals(foldIds)) {

      String[] foldsIdArr = foldIds.split(",");

      /** 判断是否为分类成员 **/
      Map<String, String> firstFold = filesDao.getFileInfo(companyId, foldsIdArr[0], null);
      String idSeq = firstFold.get("idSeq");
      // 截取文件所属的分类id
      String classId = idSeq.split("\\.")[1];
      String groupId = filesDao.getGroupIdByClassId(classId, companyId);
      boolean isMember = filesDao.isGroupMember(groupId, userId);

      for (String tempFoldId : foldsIdArr) {

        if (null == tempFoldId || "".equals(tempFoldId))
          continue;

        // 判断是否已经被删除
        Map<String, String> tempFold = filesDao.getFileInfo(companyId, tempFoldId, null);

        /** 获取分组 **/
        if ("".equals(groupFlag) && !"0".equals(tempFold.get("classId"))) {
          String firstClassId = tempFold.get("idSeq").split("\\.")[1];
          // groupFlag = filesDao.getFlagWithClassId(companyId, firstClassId);
          Map<String, String> groupMap = filesDao.getFlagWithClassId(companyId, firstClassId);
          res.put("groupFlag", groupMap.get("FLAG"));
        }

        if (null == tempFold || 0 == tempFold.size() || "1".equals(tempFold.get("isDelete"))) {
          hasDeleteFoldsId = hasDeleteFoldsId + tempFoldId + ",";
          continue;
        }

        // 判断是否拥有该文件夹的删除权限（即是否为分类成员）
        if (!isMember) {
          hasNoRightFoldsId = hasNoRightFoldsId + tempFoldId + ",";
          continue;
        }
      }
    }



    /** 过滤选中的文件 **/
    if (null != fileIds && !"".equals(fileIds)) {

      String[] filesIdArr = fileIds.split(",");
      for (String tempFileId : filesIdArr) {
        if (null == tempFileId || "".equals(tempFileId))
          continue;
        // 获取文件
        Map<String, String> tempFile = filesDao.getFileInfo(companyId, tempFileId, null);

        /** 获取分组 **/
        if ("".equals(groupFlag) && !"0".equals(tempFile.get("classId"))) {
          String firstClassId = tempFile.get("idSeq").split("\\.")[1];
          // groupFlag = filesDao.getFlagWithClassId(companyId, firstClassId);
          Map<String, String> groupMap = filesDao.getFlagWithClassId(companyId, firstClassId);
          res.put("groupFlag", groupMap.get("FLAG"));
          // res.put("groupFlag", groupFlag);
        }

        // 判断是否已经被删除
        if (null == tempFile || 0 == tempFile.size() || "1".equals(tempFile.get("isDelete"))) {
          hasDeleteFilesId = hasDeleteFilesId + tempFileId + ",";
          continue;
        }
        // liuwei 判断是否存在多个版本
        if ("2".equals(tempFile.get("version"))) {
          hasMoreVersion = hasMoreVersion + tempFileId + ",";
          continue;
        }

       
        // 判断是否拥有删除权限
        if (!this.hasUserDeleteRight(companyId, userId, tempFile)) {
          hasNoRightFilesId = hasNoRightFilesId + tempFileId + ",";
          continue;
        }

      }
    }


    hasDeleteFoldsId =
        "".equals(hasDeleteFoldsId) ? "" : hasDeleteFoldsId.substring(0,
            hasDeleteFoldsId.length() - 1);
    hasNoRightFoldsId =
        "".equals(hasNoRightFoldsId) ? "" : hasNoRightFoldsId.substring(0,
            hasNoRightFoldsId.length() - 1);
    hasDeleteFilesId =
        "".equals(hasDeleteFilesId) ? "" : hasDeleteFilesId.substring(0,
            hasDeleteFilesId.length() - 1);
    hasNoRightFilesId =
        "".equals(hasNoRightFilesId) ? "" : hasNoRightFilesId.substring(0,
            hasNoRightFilesId.length() - 1);
    hasMoreVersion =
        "".equals(hasMoreVersion) ? "" : hasMoreVersion.substring(0, hasMoreVersion.length() - 1);

    res.put("hasDeleteFoldsId", hasDeleteFoldsId);
    res.put("hasNoRightFoldsId", hasNoRightFoldsId);
    res.put("hasDeleteFilesId", hasDeleteFilesId);
    res.put("hasNoRightFilesId", hasNoRightFilesId);
    res.put("hasMoreVersion", hasMoreVersion);
    writeJson(response, callback, gson.toJson(res));

  }

  /**
   * 设置文件的文件夹路径
   */
  private void setupFolderPathName(String companyId, List<Map<String, String>> files) {
    for (Map<String, String> file : files) {
      setupFolderPathName(companyId, file);
    }
  }

  /**
   * 设置文件的文件夹路径
   */
  private void setupFolderPathName(String companyId, Map<String, String> file) {
    String idSeqSrc = file.get("idSeqSrc");
    String folderPathName = getFolderPathName(companyId, idSeqSrc);
    file.put("folderPathName", folderPathName);
  }

  /**
   * 获取文件夹的路径
   * 
   * @param companyId
   * @param idSeqSrc
   * @return
   */
  private String getFolderPathName(String companyId, String idSeqSrc) {
    String folderPathName = "";
    if (idSeqSrc != null && !"".equals(idSeqSrc)) {
      folderPathName = "/";
      idSeqSrc = idSeqSrc.substring(2);
      Map<String, String> folderPathNameMap =
          filesDao.getFolderPathNameWithIdSeq(companyId, idSeqSrc);
      for (String id : idSeqSrc.split("\\.")) {
        if (!"".equals(id)) {
          folderPathName += folderPathNameMap.get(id) + "/";
        }
      }
    }
    return folderPathName;
  }

  /**
   * wangwenshuo 20151229 文件权限拥有者（第一版上传人）
   */
  private void setupRightOwner(String companyId, List<Map<String, String>> files) {
    for (Map<String, String> file : files) {
      String classId = file.get("classId");
      String fileName = file.get("fileName");
      String fileType = file.get("type");
      String soleNumber = file.get("soleNumber");
      Map<String, String> fileV1 =
          filesDao.getFileFirstVersion(companyId, classId, fileName, fileType, soleNumber);
      file.put("owner_v1", fileV1.get("owner"));
    }
  }

  /**
   * 查询历史版本
   * 
   * @param request
   * @param response
   */
  public void getFileList4Version(HttpServletRequest request, HttpServletResponse response) {
    String userName = request.getParameter("userName");
    String companyId = request.getParameter("companyId");
    String classId = request.getParameter("classId");
    String fileId = request.getParameter("fileId");
    String fileName = request.getParameter("fileName");
    String fileType = request.getParameter("fileType");
    String userId = request.getParameter("userId"); // 当前登录用户
    String callback = request.getParameter("callback");

    Map<String, Object> res = new HashMap<String, Object>();
    // 获取文件版本列表
    List<Map<String, String>> versionList =
        filesDao.getFileList4Version(companyId, classId, fileName, fileType);
    if (versionList != null && versionList.size() > 1) {
      this.setupFilesCreatorName(companyId, versionList);
      this.setupParentClassName(companyId, versionList);
      // 赋予浏览权限
      setupUserReadRight(companyId, userId, versionList, true);
      // 赞
      setupPraise(companyId, userId, versionList);
      setupClassGroupInfo(companyId, userId, versionList);
      res.put("size", versionList.size());
      res.put("list", versionList);
      res.put("lastFileId", fileId);
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", request.getRemoteAddr());
      log.put("userid", userName);
      log.put("loginfo", "获得文件【" + fileName + "】ID为【" + fileId + "】历史版本");
      log.put("module", "文件列表");
      log.put("username", userName);
      log.put("companyName", (companyName != null || "".equals(companyName)) ? companyName
          : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "access");
      log.put("operate", "所有文档/我的文档");
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    } else {
      res.put("size", 0);
    }
    writeJson(response, callback, gson.toJson(res));
  }

  public Map<String, Object> getFileList4Version(Map<String, String> param) {
    String userName = param.get("userName");
    String companyId = param.get("companyId");
    String classId = param.get("classId");
    String fileId = param.get("fileId");
    String fileName = param.get("fileName");
    String fileType = param.get("fileType");
    String userId = param.get("userId"); // 当前登录用户


    Map<String, Object> res = new HashMap<String, Object>();
    // 获取文件版本列表
    List<Map<String, String>> versionList =
        filesDao.getFileList4Version(companyId, classId, fileName, fileType);
    if (versionList != null && versionList.size() > 1) {
      // 如果传过来的companyId包含user_字符串 说明是我的文档部分
      boolean isMyDocument = companyId.contains("user_");
      if (isMyDocument) {
        for (Map<String, String> file : versionList) {
          file.put("hasRight", "1");
          file.put("userName", userName);
          file.put("fullName", "");
          file.put("ownerUserName", userName);
          file.put("ownerFullName", "");
          file.put("hasDeleteRight", "true");
          file.put("ownerV1UserName", userName);
          file.put("ownerV1FullName", "");
          file.put("hasRight", "1");
          file.put("isDownload", "1");
          file.put("isLook", "1");
          file.put("owner_v1", file.get("owner"));
        }
      } else {
        String rootClass = versionList.get(0).get("idSeq").split("\\.")[1];
        String groupId = filesDao.getGroupIdByClassId(rootClass, companyId);
        boolean isMember = filesDao.isGroupMember(groupId, userId);
        // 文件权限拥有者（第一版上传人） wangwenshuo 20151229 add
        this.setupRightOwner(companyId, versionList);
        this.setupFilesCreatorName(companyId, versionList);
        // 赋予浏览权限
        setupUserReadRight(companyId, userId, versionList, isMember);
        // 赞
        setupPraise(companyId, userId, versionList);
        setupClassGroupInfo(companyId, userId, versionList);
        this.setupHasSubScribeUser(userId, versionList);
      }
      this.setupParentClassName(companyId, versionList);
      res.put("size", versionList.size());
      res.put("list", versionList);
      res.put("lastFileId", fileId);
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", param.get("remoteAddr"));
      log.put("userid", userName);
      log.put("loginfo", "获得文件【" + fileName + "】ID为【" + fileId + "】历史版本");
      log.put("module", "文件列表");
      log.put("username", userName);
      log.put("companyName", (companyName != null || "".equals(companyName)) ? companyName
          : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "access");
      log.put("operate", "所有文档/我的文档");
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    } else {
      res.put("size", 0);
      res.put("list", versionList);
    }
    // writeJson(response, callback, gson.toJson(res));
    return res;
  }

  // private void setupUserReadRightForVersion(String companyId, String userId,
  // List<Map<String, String>> versionList, String groupFlag) {
  // List<String> groupIds = new ArrayList<String>();
  //
  // // 获取该用户有权限的fileid 如果文件分享给了聊天或者用户都 那么组内成员和单个用户都有权限了
  // for (Map<String, String> file : versionList) {
  // if(groupFlag!=null && "".equals(groupFlag)){
  // String classId = file.get("idSeq").split("\\.")[1];
  // file.put("isMember", "false");
  // String groupId = filesDao.getGroupIdByClassId(classId,companyId);
  // groupIds.add(groupId);
  // }
  // Map<String,Map<String,String>> fileIds = filesDao.getFileUserRelationWithUserId(companyId,
  // userId, groupIds);
  // String openLevel = file.get("openLevel");
  // //判断文件的公开权限
  // //1.版本创建者有权限 2.非私密文件成员拥有权限 3.文件分享者有权限
  // if(userId.equals(file.get("owner_v1"))){
  // file.put("hasRight", "1");
  // file.put("isDownload", "1");
  // file.put("isLook", "1");
  // }else if("2".equals(openLevel) && isMember){
  // file.put("hasRight", "1");
  // file.put("isDownload", "1");
  // file.put("isLook", "1");
  // }else if("1".equals(openLevel)){
  // file.put("hasRight", "1");
  // file.put("isDownload", "1");
  // file.put("isLook", "1");
  // }else if(!fileIds.isEmpty() && (fileIds.get(file.get("id")+"_0")!=null ||
  // fileIds.get(file.get("soleNumber")+"_1")!=null)){
  // file.put("hasRight", "1");
  // if(fileIds.get(file.get("id")+"_0")!=null){
  // file.put("isDownload", fileIds.get(file.get("id")+"_0").get("isDownload"));
  // file.put("isLook", fileIds.get(file.get("id")+"_0").get("isLook"));
  // }
  // if(fileIds.get(file.get("soleNumber")+"_1")!=null){
  // if(file.get("isDownload")==null || file.get("isDownload").equals("0"))
  // //判断的原因是，如果已经有权限了就没必要在设置权限
  // file.put("isDownload", fileIds.get(file.get("soleNumber")+"_1").get("isDownload"));
  // if(file.get("isLook")==null || file.get("isLook").equals("0"))
  // file.put("isLook", fileIds.get(file.get("soleNumber")+"_1").get("isLook"));
  // }
  // }else{
  // file.put("hasRight", "0");
  // file.put("isDownload", "0");
  // file.put("isLook", "0");
  // }
  //
  // // wangwenshuo 删除权限控制 私密文件只有创建者可以删除 非私密文件只有拥有者可以删除
  // String openlevel = file.get("openLevel");
  // if(("3".equals(openlevel) && userId.equals(file.get("owner_v1")))
  // || (!"3".equals(openlevel) && userId.equals(file.get("owner")))){
  // file.put("hasDeleteRight", "true");
  // }else{
  // file.put("hasDeleteRight", "false");
  // }
  //
  // }
  // return files;
  //
  // }

  /**
   * 计算文件的actions
   * 
   * @param userId
   * @param file
   */
  private void setupFileActions(String userId, Map<String, String> file) {
    Map<String, String> actionMap = new HashMap<String, String>();
    // 是否创建人
    if (userId.equals(file.get("userId"))) {
      actionMap.put("share", "1");
      actionMap.put("edit", "1");
      actionMap.put("download", "1");
      actionMap.put("del", "1");
    } else {
      actionMap.put("share", "0");
      actionMap.put("edit", "0");
      actionMap.put("download", "1");
      actionMap.put("del", "0");
    }
    file.put("actions", gson.toJson(actionMap));
  }

  public void queryMembers(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String callback = request.getParameter("callback");
    List<Map<String, String>> members = filesDao.getMembers(companyId);
    Map result = new HashMap();
    result.put("members", members);
    writeJson(response, callback, gson.toJson(result));
  }

  /**
   * 分享一个文件给某个用户（允许其查看和下载文件）
   */
  public String shareFileToUser(Map<String, String> params) {
    String companyId = params.get("companyId");
    String fileId = params.get("fileId");
    String toUserId = params.get("toUserId");
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");
    String button = params.get("button");
    String isDownload = "1";// 申请默认权限都为1
    String isLook = "1";
    boolean isApplyLabel = false;
    String applyLabel = "申请";
    if (button != null && button.contains("浏览")) {
      applyLabel = "isLook";
      isDownload = "0";
    } else if (button != null && button.contains("下载")) {
      applyLabel = "isDownload";
      // isLook="0";
    } else if (button != null && button.contains("申请")) {
      applyLabel = "申请";
    }
    String receiver = params.get("receiver").replace("\\40", "@");
    String rst = "hasShare";
    String approveUserName = params.get("fromUserName");
    approveUserName = approveUserName.replace("\\40", "@");

    Map<String, String> fileMap = filesDao.getFileById("files_" + companyId, fileId);
    Map<String, String> retMap =
        filesDao.getHasShareFileToUserOrGroup(companyId, fileMap.get("soleNumber"), toUserId,
            false, true);
    if (retMap != null && retMap.size() > 0) {
      if ((("申请").equals(applyLabel) && "1".equals(retMap.get("isDownload")) && "1".equals(retMap
          .get("isLook")))
          || ("isDownload".equals(applyLabel) && "1".equals(retMap.get("isDownload")))
          || ("isLook".equals(applyLabel) && "1".equals(retMap.get("isLook")))) {
        return rst;// 已经具备条件了
      }
      isApplyLabel = true;// 标示更新
    } else {
      isApplyLabel = false;// 标示插入
    }
    // boolean flag=filesDao.shareFileToUserOrGroup(companyId, fileId, toUserId, false,
    // approveUserName);
    boolean flag =
        filesDao.updateOrShareFileToUserOrGroup(companyId, fileMap.get("soleNumber"), toUserId,
            false, approveUserName, isApplyLabel, applyLabel, isDownload, isLook, true);
    if (flag) {
      rst = "success";
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", remoteAddr);
      log.put("userid", userName);
      log.put("loginfo", "分享文件【" + fileId + "】给用户【" + toUserId + "】");
      log.put("module", "文件列表");
      log.put("username", userName);
      log.put("companyName", (companyName != null || "".equals(companyName)) ? companyName
          : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "所有文档/我的文档");
      Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
      if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")) {
        log.put("fileId", fileId + "");
        log.put("folderId", felFileMap.get("folderId") + "");
        Map<String, String> userFullNameMaps =
            filesDao.getUserFullNameByCompanyId(params.get("companyId"));
        log.put("fileSubscribMsg",
            "分享文件【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>给用户 【"
                + receiver + "】");
      }

      /** lujixiang 20150907 添加字段，用于满足关注信息中对分享文件的特殊显示要求 --start **/
      log.put("actionEn", "share"); // 分享
      log.put("toUser", receiver); // 被分享的用户名
      log.put("fileName", felFileMap.get("fileName") + "." + felFileMap.get("fileType")); // 分享的文件名称
      log.put("toUserFullName", params.get("toUserFullName"));
      /** lujixiang 20150907 添加字段，用于满足关注信息中对分享文件的特殊显示要求 --end **/

      log.put("companyId", companyId);

      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
      String msgid = params.get("msgid");
      // 用来判断是什么已经分享的还是未分享的
      // String test = params.get("test");
      Map<String, Object> map = getChatWS().UpdateFileToShareOrNoShare(msgid, button, companyId);
    } else {
      rst = "failure";
    }
    return rst;
  }

  /**
   * 取消分享一个文件给某个用户 wangwenshuo 20160120 此时前台只有取消申请的权限时使用，所以forAllVersion为true
   */
  public String unShareFileToUser(Map<String, String> params) {
    String companyId = params.get("companyId");
    String fileId = params.get("fileId");
    String toUserId = params.get("toUserId");
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");
    String receiver = params.get("receiver").replace("\\40", "@");
    String fromUserName = params.get("fromUserName").replace("\\40", "@");
    String button = params.get("button");
    String rst = "hasShare";
    // if (!filesDao.hasShareFileToUserOrGroup(companyId, fileId, toUserId, false)) {
    // boolean flag = filesDao.unShareFileToUser(companyId, fileId, toUserId);
    // 取消分享
    String applyLabel = "申请";
    if (button != null && button.contains("浏览")) {
      applyLabel = "isLook";
    } else if (button != null && button.contains("下载")) {
      applyLabel = "isDownload";
    } else if (button != null && button.contains("申请")) {
      applyLabel = "申请";
    }

    // 获取实际用于权限检验的soleNumber
    Map<String, String> fileMap = filesDao.getFileById("files_" + companyId, fileId);
    String soleNumber = fileMap.get("soleNumber");

    Map<String, String> retMap =
        filesDao.getHasShareFileToUserOrGroup(companyId, soleNumber, toUserId, false, true);
    if (retMap != null && retMap.size() > 0) {
      if ("申请".equals(applyLabel) && "0".equals(retMap.get("isDownload"))) {
        // 删除
        filesDao.unShareFileToUser(companyId, soleNumber, toUserId);
        return rst;// 已经具备条件了
      }
      if (("isLook".equals(applyLabel) && "0".equals(retMap.get("isLook")))
          || ("isDownload".equals(applyLabel) && "0".equals(retMap.get("isDownload")))) {
        return rst;// 已经具备条件了
      }
    } else {
      return rst;
    }
    boolean flag =
        filesDao.updateOrShareFileToUserOrGroup(companyId, soleNumber, toUserId, false,
            fromUserName, true, applyLabel, "0", "0", true);
    if (flag) {
      retMap = filesDao.getHasShareFileToUserOrGroup(companyId, soleNumber, toUserId, false, true);
      if (retMap != null && retMap.size() > 0) {
        if ("0".equals(retMap.get("isDownload")) && "0".equals(retMap.get("isLook"))) {
          filesDao.unShareFileToUser(companyId, soleNumber, toUserId);
        }
      }
      rst = "success";
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", remoteAddr);
      log.put("userid", userName);
      log.put("loginfo", "取消分享分享文件【" + soleNumber + "】对用户【" + toUserId + "】");
      log.put("module", "文件列表");
      log.put("username", userName);
      log.put("companyName", (companyName != null || "".equals(companyName)) ? companyName
          : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "所有文档/我的文档");
      Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
      if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")) {
        log.put("fileId", fileId + "");
        log.put("folderId", felFileMap.get("folderId") + "");
        Map<String, String> userFullNameMaps =
            filesDao.getUserFullNameByCompanyId(params.get("companyId"));
        log.put("fileSubscribMsg",
            "取消分享文件【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>给用户 【"
                + receiver + "】");
      }

      log.put("companyId", companyId);

      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
      String msgid = params.get("msgid");
      // 用来判断是什么已经分享的还是未分享的
      // String test = params.get("test");
      Map<String, Object> map = getChatWS().UpdateFileToShareOrNoShare(msgid, button, companyId);
    }
    return rst;
  }

  public void doNotShareFileToUser(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String fileId = request.getParameter("fileId");
    String toUserId = request.getParameter("toUserId");
    String toUserName = request.getParameter("toUserName").replace("\\40", "@");
    String msgid = request.getParameter("msgid");
    String button = request.getParameter("button");
    Map<String, Object> map = getChatWS().UpdateFileToShareOrNoShare(msgid, button, companyId);
    boolean isOk = false;
    if (Boolean.parseBoolean(map.get("success").toString())) {
      isOk = true;
    }

    String applyKey = toUserName + "_" + fileId;
    CacheUtils.delete(this.getCompLocator(), applyKey);

    JSONObject json = new JSONObject();
    json.put("isOk", isOk);
    writeJson(response, request.getParameter("callback"), gson.toJson(json));
  }

  /**
   * 分享多个文件给某个用户（允许其查看和下载文件）
   */
  public String shareFilesToUser(Map<String, String> params) {
    String companyId = params.get("companyId");
    String fileIds = params.get("fileIds");
    String toUserName = params.get("toUserName");
    String remoteAddr = params.get("remoteAddr");
    String userName = params.get("userName");
    String toUserId = getUserIdByName(companyId, toUserName);
    List<String> existList = filesDao.findFileIdsForHasRight(companyId, fileIds, toUserId);
    List<String> list = Arrays.asList(fileIds.split(","));
    List<String> fileIdList = new ArrayList<String>();
    // 找出需要新插入的fileid列表
    for (String fileId : list) {
      if (!existList.contains(fileId)) {
        fileIdList.add(fileId);
      }
    }
    String approveUserName = params.get("fromUserName");
    approveUserName = approveUserName.replace("\\40", "@");
    filesDao.shareFilesToUser(companyId, fileIdList, toUserId, approveUserName);
    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", remoteAddr);
    log.put("userid", userName);
    log.put("loginfo", "分享文件【" + fileIds + "】给多个用户【" + toUserName + "】");
    log.put("module", "文件列表");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "所有文档/我的文档");
    LogUtils.saveBaseLog(compLocator, log);
    // 日志------------------------------------
    return "success";
  }

  /**
   * 分享一个文件给某个用户或联系人群组（允许其查看和下载文件）
   */
  public String shareFileToGroupOrUser(Map<String, String> params) {
    String companyId = params.get("companyId");
    String fromCompanyId = params.get("fromCompanyId"); // 我的文档分享出来的 user_N
    String fileId = params.get("fileId");
    String receiver = params.get("receiver");
    String isGroupStr = params.get("isGroup");
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");
    String accessRight = params.get("accessRight"); // 1浏览 2下载 3浏览+下载
    boolean isGroup = "1".equals(isGroupStr) ? true : false;
    String targetId = "";
    String receiverName = "";
    String classId = "";
    if (isGroup) { // 联系人群组 c0g1428484244718
      // 获取联系人群组id
      Map<String, String> groupMap = filesDao.getGroupInfoByFlag(companyId, receiver);
      targetId = groupMap.get("id");
      receiverName = groupMap.get("groupName");
      classId = groupMap.get("classId"); // 区分发送到分类还是群组
    } else { // 联系人 long2
      // 获取联系人id
      targetId = filesDao.getUserIdWithName(companyId, receiver);
      receiverName = receiver;
    }

    String rst = "hasShare";
    String approveUserName = params.get("fromUserName");
    approveUserName = approveUserName.replace("\\40", "@");

    // 我的文档分享出来的文件 插入的时候companyId为user_N
    if (null != fromCompanyId && fromCompanyId.contains("user_"))
      companyId = fromCompanyId;
    // if (!filesDao.hasShareFileToUserOrGroup(companyId, fileId, targetId, isGroup)) {
    int hasRight = 0; // 已经拥有的权限
    Map<String, String> retMap =
        filesDao.getHasShareFileToUserOrGroup(companyId, fileId, targetId, isGroup, false);
    if ("1".equals(retMap.get("isDownload")))
      hasRight += 2;
    if ("1".equals(retMap.get("isLook")))
      hasRight += 1;

    if (!"3".equals(String.valueOf(hasRight)) && !accessRight.equals(String.valueOf(hasRight))) {
      boolean flag = false;
      if (retMap.isEmpty()) {
        flag =
            filesDao.shareFileToUserOrGroup(companyId, fileId, targetId, isGroup, approveUserName,
                "2".equals(accessRight) || "3".equals(accessRight) ? "1" : "0",
                "1".equals(accessRight) || "3".equals(accessRight) ? "1" : "0");
      } else {
        flag =
            filesDao
                .updateOrShareFileToUserOrGroup(
                    companyId,
                    fileId,
                    targetId,
                    isGroup,
                    approveUserName,
                    true,
                    "isShareUpdateRight",
                    "2".equals(accessRight) || "3".equals(accessRight) ? "1" : retMap
                        .get("isDownload"),
                    "1".equals(accessRight) || "3".equals(accessRight) ? "1" : retMap.get("isLook"),
                    false);
      }
      if (flag) {
        rst = "success";
        // 日志------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
        String companyName = "";
        if (obj != null) {
          companyUserInitInfo = (Map<String, Map<String, String>>) obj;
          companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("ip", remoteAddr);
        log.put("userid", userName);
        log.put("loginfo", "分享文件【" + fileId + "】给用户【" + receiver + "】");
        log.put("module", "文件列表");
        log.put("username", userName);
        log.put("companyName", !"".equals(companyName) ? companyName : companyId);
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "operation");
        log.put("operate", "所有文档/我的文档");
        Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
        if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")) {
          log.put("fileId", fileId + "");
          log.put("folderId", felFileMap.get("folderId") + "");
          Map<String, String> userFullNameMaps =
              filesDao.getUserFullNameByCompanyId(params.get("companyId"));
          if (isGroup) {
            log.put(
                "fileSubscribMsg",
                "分享文件【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                    + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>到"
                    + ("0".equals(classId) ? "群组" : "分类") + "【" + receiverName + "】");
          } else {
            /** lujixiang 20150908 添加字段，用于满足关注信息中对分享文件的特殊显示要求 --start **/
            log.put("actionEn", "share"); // 分享
            log.put("toUser", receiver); // 被分享的用户名
            log.put("fileName", felFileMap.get("fileName") + "." + felFileMap.get("fileType")); // 分享的文件名称
            /** lujixiang 20150908 添加字段，用于满足关注信息中对分享文件的特殊显示要求 --end **/
            log.put("fileSubscribMsg",
                "分享文件【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                    + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>给用户【"
                    + receiverName + "】");
            log.put("toUserFullName", params.get("toUserFullName"));
          }
        }
        log.put("companyId", companyId);
        LogUtils.saveBaseLog(compLocator, log);
        // 日志------------------------------------
      } else {
        rst = "failure";
      }
    }
    return rst;
  }

  /**
   * 取消分享
   */
  public String unshareFile(Map<String, String> params) {
    String companyId = params.get("companyId");
    String classId = params.get("classId");
    String fileId = params.get("fileId");
    String userId = params.get("userId");
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");
    String isExist = params.get("isExist"); // 是否已有同名文件 true时unshareType有效
    String unshareType = params.get("unshareType"); // 类型 0更新版本号 1重命名 1时newFileName有效
    String newFileName = params.get("newFileName"); // 重命名的文件名
    // 设为私密时 把所有历史版本设为私密
    Map<String, String> fileInfo = filesDao.getFileById(companyId, classId, fileId);

    /** 同名私密文件设置islast为false 并且得到最大版本号 */
    int maxVersion = 0;
    if ("true".equals(isExist) && "0".equals(unshareType)) {
      filesDao.updateFilesIsNotLast(companyId, "0", fileInfo.get("filename"), fileInfo.get("type"));
      maxVersion =
          filesDao.getNextVersion(companyId, "0", fileInfo.get("filename"), fileInfo.get("type")) - 1;
    }
    // 获得所有版本
    boolean success = false;
    List<Map<String, String>> versionList =
        filesDao.getFileList4Version(companyId, classId, fileInfo.get("filename"),
            fileInfo.get("type"));
    if (versionList.size() > 0) {
      for (int i = 0; i < versionList.size(); i++) {
        Map<String, String> m = versionList.get(i);
        if (filesDao.unshareFile(companyId, m.get("id"), userId, maxVersion, isExist, unshareType,
            newFileName)) {
          success = true;
        } else {
          success = false;
          break;
        }

        // 跟新设置私密后改变的版本号 或文件名
        if ("true".equals(isExist)) {
          if ("0".equals(unshareType)) {
            m.put("version", String.valueOf(Integer.parseInt(m.get("version")) + maxVersion));
          } else {
            m.put("fileName", newFileName.substring(0, newFileName.lastIndexOf(".")));
          }
        }
        // 设为私密后以下值修改 用于后续更新索引
        // m.put("idSeq", "");
        m.put("openlevel", "3");
        // m.put("classId", "0");
        m.put("companyId", companyId);
      }
    } else {
      success =
          filesDao.unshareFile(companyId, fileId, userId, maxVersion, isExist, unshareType,
              newFileName);
    }
    // String groupFlag = filesDao.getFlagWithClassId(companyId, classId);
    Map<String, String> groupMap = filesDao.getFlagWithClassId(companyId, classId);
    if (success) {
      getLuceneWS().updateIndex4Unshare(versionList);
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", remoteAddr);
      log.put("userid", userName);
      log.put("loginfo", "删除文件【" + fileId + "】");
      log.put("module", "文件");
      log.put("username", userName);
      log.put("companyName", !"".equals(companyName) ? companyName : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "所有文档/我的文档");
      Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
      if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")) {
        log.put("fileId", fileId + "");
        log.put("folderId", felFileMap.get("folderId") + "");
        Map<String, String> userFullNameMaps =
            filesDao.getUserFullNameByCompanyId(params.get("companyId"));
        log.put("fileSubscribMsg",
            "取消了【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>的分享");
      }
      log.put("companyId", companyId);
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    }
    Map<String, String> rst = new HashMap<String, String>();
    rst.put("groupFlag", groupMap.get("FLAG"));
    rst.put("GROUPNAME", groupMap.get("GROUPNAME"));
    rst.put("success", success + "");
    return gson.toJson(rst);
  }

  /**
   * 点赞/取消赞文件
   */
  public String praiseFile(Map<String, String> params) {
    String companyId = params.get("companyId");
    String fileId = params.get("fileId");
    String userId = params.get("userId");
    String statusStr = params.get("status");
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");
    boolean status = Boolean.parseBoolean(statusStr);
    boolean paraiseTag = true;
    // 赞/取消赞
    if (filesDao.praiseFile(companyId, fileId, userId, status)) {
      // 计算文件的赞数量
      paraiseTag = filesDao.praiseCountUpdate(companyId, fileId, status);
    }
    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", remoteAddr);
    log.put("userid", userName);
    log.put("loginfo", statusStr.equals("true") ? "" + "文件【" + fileId + "】" + "点赞" : "" + "文件【"
        + fileId + "】" + "取消赞");
    log.put("module", "文件");
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    log.put("username", userName);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "所有文档/我的文档");
    log.put("fileId", fileId);
    Map<String, String> fileInfoMap = filesDao.getFileInfoById(companyId, fileId);
    if (null != fileInfoMap.get("openLevel") && !fileInfoMap.get("openLevel").equals("3")) {
      if (paraiseTag) {
        log.put("fileSubscribMsg",
            "赞了【" + fileInfoMap.get("fileName") + "." + fileInfoMap.get("fileType")
                + "】<span class ='version'>第" + fileInfoMap.get("version") + "版</span>");
      } else {
        log.put("fileSubscribMsg",
            "取消了【" + fileInfoMap.get("fileName") + "." + fileInfoMap.get("fileType")
                + "】<span class ='version'>第" + fileInfoMap.get("version") + "版</span>的赞");
      }
      log.put("fileId", fileId + "");
      log.put("folderId", fileInfoMap.get("folderId") + "");
    }
    log.put("companyId", companyId);
    LogUtils.saveBaseLog(compLocator, log);
    // 日志------------------------------------
    return "";
  }

  /**
   * 收藏/取消收藏文件
   */
  public String collectFile(Map<String, String> params) {
    // TODO Auto-generated method stub
    return null;
  }


  /** lujixiang 20151210 此文件删除函数弃用,改用deleteFileAndUpdateVersion **/
  /**
   * wangwenshuo 20160105 右键删除（特别是我的文档）使用的是该删除方法 逻辑删除文件，修改isDelete状态值为0
   */
  public String deleteFile(Map<String, String> params) {
    String companyId = params.get("companyId");
    String userId = params.get("userId");
    String fileId = params.get("fileId");
    String remoteAddr = params.get("remoteAddr");
    String userName = params.get("userName");
    String isShow = params.get("isShow");

    Map<String, String> file = filesDao.getFileInfoById(companyId, fileId);
    String fileName = file.get("fileName");
    String fileType = file.get("fileType");
    String folderId = file.get("folderId");
    String idSeqSrc = file.get("fileIdSeq");
    String version = file.get("version");
    String isLast = file.get("isLast");
    String folderPathName = getFolderPathName(companyId, idSeqSrc);
    boolean bol = filesDao.deleteFile(companyId, fileId);
    String newLastId = "";
    if (bol) {
      try {
        // xyc查询当前分类下文件最大的序号
        String maxSerialNumber =
            filesDao.getClassHighestSerialNumber("FILES_" + companyId, file.get("classId"),
                file.get("isFile"));
        if (!maxSerialNumber.equals(file.get("serialNumber"))) {
          // 删除的不是当前最新的，那么更新序号
          filesDao.updateClassFileOrFolderSerialnumber("FILES_" + companyId, file.get("classId"),
              file.get("isFile"));
        }
        /** lujixiang 20150812 先修改为当删除文档时，整个文件不可见，历史版本也不可见 **/
        /** liumingchao 20150825 在列表下删除文件时是包括历史记录一并删除，在详细页面删除历史版本时只是将个别版本删除 **/
        if (Boolean.parseBoolean(isShow)) {
          // 将刚删除的文件的islast值0
          filesDao.updateFileIsLast(companyId, fileId, "0");
          // 将文件的前一版本替换为最新版本
          if (!"1".equals(version) && "1".equals(isLast)) {
            List<Map<String, String>> fileList =
                filesDao.getFileList4Version(companyId, folderId, fileName, fileType);
            if (fileList.size() > 0) {
              newLastId = fileList.get(0).get("id");
              filesDao.updateFileIsLast(companyId, newLastId, "1");
            }
          }
        }
        /** 我的文档右键删除 删除所有版本文件 wangwenshuo2151224 start */
        if (companyId.contains("user_")) {
          List<Map<String, String>> fileList =
              filesDao.getFileList4Version(companyId, folderId, fileName, fileType);
          if (fileList.size() > 0) {
            List<String> fileIds = new ArrayList<String>();
            for (Map<String, String> fileMap : fileList) {
              fileIds.add(fileMap.get("id"));
            }
            filesDao.deleteFiles(companyId, fileIds);
            this.addFilesTrashById(companyId, fileIds, userId);
          }
        }
        /** 我的文档右键删除 删除所有版本文件 wangwenshuo2151224 end */
        // 添加到回收站
        filesDao.addFileTrashById(companyId, fileId, userId, folderPathName);
        // getLuceneWS().deleteIndex(companyId, fileId);
        // 索引库不直接删了，更改一下状态。
        List<String> ids = new ArrayList<String>();
        ids.add(fileId);
        Map<String, Object> lucenemap = new HashMap<String, Object>();
        lucenemap.put("isDel", "1");
        lucenemap.put("companyId", companyId);
        lucenemap.put("ids", ids);
        getLuceneWS().updateIndexOfDel(lucenemap);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", remoteAddr);
    log.put("userid", userName);
    log.put("loginfo", "删除文件【" + fileId + "】");
    log.put("module", "文件");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "所有文档/我的文档");
    Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
    if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")) {
      log.put("fileId", fileId + "");
      log.put("folderId", felFileMap.get("folderId") + "");
      Map<String, String> userFullNameMaps =
          filesDao.getUserFullNameByCompanyId(params.get("companyId"));
      log.put("fileSubscribMsg",
          "删除了【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
              + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>");
    }
    log.put("companyId", companyId);
    LogUtils.saveBaseLog(compLocator, log);
    // 日志------------------------------------
    Map<String, String> rstMap = new HashMap<String, String>();
    rstMap.put("lastFileId", newLastId);
    rstMap.put("success", "true");
    return gson.toJson(rstMap);
  }

  /**
   * 逻辑删除文件，修改isDelete状态值为0
   */
  public String deleteFileExtend(Map<String, String> params) {
    String companyId = params.get("companyId");
    String userId = params.get("userId");
    String fileId = params.get("fileId");
    String remoteAddr = params.get("remoteAddr");
    String userName = params.get("userName");
    Map<String, String> retrunMap = new HashMap<String, String>();
    if (companyId != null && userId != null && fileId != null && remoteAddr != null
        && userName != null) {
      Map<String, String> file = filesDao.getFileInfoById(companyId, fileId);
      String fileName = file.get("fileName");
      String fileType = file.get("fileType");
      String folderId = file.get("folderId");
      String idSeqSrc = file.get("fileIdSeq");
      String version = file.get("version");
      String isLast = file.get("isLast");
      String folderPathName = getFolderPathName(companyId, idSeqSrc);
      boolean bol = filesDao.deleteFile(companyId, fileId);
      if (bol) {
        try {
          // 将刚删除的文件的islast值0
          filesDao.updateFileIsLast(companyId, fileId, "0");
          // 将文件的前一版本替换为最新版本
          if (!"1".equals(version) && "1".equals(isLast)) {
            List<Map<String, String>> fileList =
                filesDao.getFileList4Version(companyId, folderId, fileName, fileType);
            if (fileList.size() > 0) {
              String newLastId = fileList.get(0).get("id");
              filesDao.updateFileIsLast(companyId, newLastId, "1");
            }
          }
          // 添加到回收站
          filesDao.addFileTrashById(companyId, fileId, userId, folderPathName);
          // getLuceneWS().deleteIndex(companyId, fileId);
          // 索引库不直接删了，更改一下状态。
          List<String> ids = new ArrayList<String>();
          ids.add(fileId);
          Map<String, Object> lucenemap = new HashMap<String, Object>();
          lucenemap.put("isDel", "1");
          lucenemap.put("companyId", companyId);
          lucenemap.put("ids", ids);
          getLuceneWS().updateIndexOfDel(lucenemap);
          retrunMap.put("success", "true");
          retrunMap.put("version", version);
          // 日志------------------------------------
          Map<String, Map<String, String>> companyUserInitInfo = null;
          Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
          String companyName = "";
          if (obj != null) {
            companyUserInitInfo = (Map<String, Map<String, String>>) obj;
            companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
          }
          HashMap<String, String> log = new HashMap<String, String>();
          log.put("ip", remoteAddr);
          log.put("userid", userName);
          log.put("loginfo", "删除文件【" + fileId + "】");
          log.put("module", "文件");
          log.put("username", userName);
          log.put("companyName", !"".equals(companyName) ? companyName : companyId);
          // login 用户登录 access 功能访问 operation 功能操作
          log.put("type", "operation");
          log.put("operate", "所有文档/我的文档");
          Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
          if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")) {
            log.put("fileId", fileId + "");
            log.put("folderId", felFileMap.get("folderId") + "");
            Map<String, String> userFullNameMaps =
                filesDao.getUserFullNameByCompanyId(params.get("companyId"));
            log.put("fileSubscribMsg",
                "删除了【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                    + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>");
          }
          log.put("companyId", companyId);
          LogUtils.saveBaseLog(compLocator, log);
          // 日志------------------------------------
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        retrunMap.put("success", "false");
      }
    } else {
      retrunMap.put("success", "false");
    }
    return gson.toJson(retrunMap);
  }

  /**
   * 取消一个文件的分享
   */
  public String cancelShareFile(Map<String, String> params) {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<String, String> saveUploadFile(Map<String, String> params) {
    // 修改索引库的不同版本的isLast字段
    // 插入新版本钱 获取最新文件版本
    String companyId = params.get("companyId");
    String classId = params.get("classId");
    String fileName = params.get("filename");
    String fileType = params.get("type");
    List<Map<String, String>> files =
        filesDao.getFileOfIsLast(companyId, classId, fileName, fileType);

    int fileInsertId = filesDao.insertIntoFilesN(params);
    if (fileInsertId > 0) {
      if (files.size() > 0) {
        getLuceneWS().updateIndexOfisLast(files, companyId);
      }
      Map<String, String> map = new HashMap<String, String>();
      // map.put("isfile", "true");
      map.put("ID", fileInsertId + "");
      map.put("COMPANYID", params.get("companyId"));
      /*
       * map.put("fileName", params.get("filename")); map.put("classId", params.get("classId"));
       * map.put("fileType", params.get("type")); map.put("version", params.get("version"));
       * map.put("insertId", params.get("insertId")); map.put("idSeq", params.get("idSeq"));
       * map.put("createrId", params.get("userId")); map.put("openlevel", params.get("openlevel"));
       * map.put("size", params.get("length")); map.put("createtime", params.get("createtime"));
       * map.put("isFile", "1"); map.put("isLast", "1"); map.put("isDel", "0"); map.put("md5",
       * params.get("md5"));
       */
      getLuceneWS().createIndex(map);
      // 日志------------------------------------
      // 获得登陆名
      String userName = getUserWS().getUserNameById(params.get("userId"));
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj =
          CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + params.get("companyId"));
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", params.get("ip"));
      log.put("userid", userName);
      log.put("loginfo", "上传文件【" + params.get("filename") + "】在ID为【" + params.get("classId") + "】下");
      log.put("module", "文件");
      log.put("username", userName);
      log.put("companyName", !"".equals(companyName) ? companyName : params.get("companyId"));
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "所有文档/我的文档");
      if (null != params.get("openlevel") && !params.get("openlevel").equals("3")) {
        log.put("fileId", fileInsertId + "");
        log.put("folderId", params.get("classId"));
        Map<String, String> userFullNameMaps =
            filesDao.getUserFullNameByCompanyId(params.get("companyId"));
        log.put("fileSubscribMsg", "上传了【" + params.get("filename") + "." + params.get("type")
            + "】<span class ='version'>第" + params.get("version") + "版</span>");
      }
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    }
    Map<String, String> map = new HashMap<String, String>();

    map.put("success", fileInsertId > 0 ? "true" : "false");
    map.put("fileId", fileInsertId + "");
    return map;
  }

  public String getFileOtherProp(String companyId, String fileId, String ip, String username) {
    List<Map<String, String>> fpLst = getFilePropertiesWS().getFilePropLstByCompany(companyId);
    Map<String, String> file = filesDao.getFileInfo(companyId, fileId, fpLst);
    // liqiubo 20140407 把自定义属性信息传过去
    StringBuffer showDetailStr = new StringBuffer();
    if (fpLst != null && fpLst.size() > 0) {
      for (Map<String, String> oneFileProp : fpLst) {
        showDetailStr.append("<div class='editFileInfoHeight25px'>");
        showDetailStr.append("<div class='editFileInfoTitle'>");
        showDetailStr.append(oneFileProp.get("TITLE"));
        showDetailStr.append("</div>");
        showDetailStr.append("<div class='editFileInfoValue'>");
        if ("文本".equals(oneFileProp.get("TYPE"))) {
          showDetailStr.append("<input fileType='text' maxlength='" + oneFileProp.get("LENGTH")
              + "' name='" + "C" + oneFileProp.get("ID") + "' type='text' value='"
              + file.get("C" + oneFileProp.get("ID")) + "'/>");
        } else if ("数值".equals(oneFileProp.get("TYPE"))) {
          showDetailStr.append("<input fileType='number' maxlength='" + oneFileProp.get("LENGTH")
              + "' name='" + "C" + oneFileProp.get("ID") + "' type='text' value='"
              + file.get("C" + oneFileProp.get("ID")) + "'/>");
        } else if ("浮点".equals(oneFileProp.get("TYPE"))) {
          showDetailStr.append("<input fileType='float' floatlength='" + oneFileProp.get("LENGTH")
              + "." + oneFileProp.get("DOTLENGTH") + "' name='" + "C" + oneFileProp.get("ID")
              + "' type='text' value='" + file.get("C" + oneFileProp.get("ID")) + "'/>");
        } else if ("日期".equals(oneFileProp.get("TYPE"))) {
          showDetailStr
              .append("<input fileType='date' name='"
                  + "C"
                  + oneFileProp.get("ID")
                  + "' type='text' class=\"Wdate\" readOnly onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\" value='"
                  + file.get("C" + oneFileProp.get("ID")) + "'/>");
        } else if ("时间".equals(oneFileProp.get("TYPE"))) {
          showDetailStr
              .append("<input fileType='time' name='"
                  + "C"
                  + oneFileProp.get("ID")
                  + "' type='text' class=\"Wdate\" readOnly onClick=\"WdatePicker({dateFmt:'HH:mm:ss'})\" value='"
                  + file.get("C" + oneFileProp.get("ID")) + "'/>");
        } else if ("布尔".equals(oneFileProp.get("TYPE"))) {
          showDetailStr
              .append("<select fileType='bol' name='" + "C" + oneFileProp.get("ID") + "'>");
          if ("1".equals(file.get("C" + oneFileProp.get("ID")))) {
            showDetailStr
                .append("<option value='1' selected>是</option><option value='0'>否</option>");
          } else {
            showDetailStr
                .append("<option value='1'>是</option><option selected value='0'>否</option>");
          }
          showDetailStr.append("</select>");
        }
        showDetailStr.append("</div>");
        showDetailStr.append("</div>");
        // 日志------------------------------------
        Map<String, Map<String, String>> companyUserInitInfo = null;
        Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
        String companyName = "";
        if (obj != null) {
          companyUserInitInfo = (Map<String, Map<String, String>>) obj;
          companyName = companyUserInitInfo.get(username).get("COMPANYNAME");
        }
        HashMap<String, String> log = new HashMap<String, String>();
        log.put("ip", ip);
        log.put("userid", username);
        log.put("loginfo", "获得文件ID为【" + fileId + "】的可自定义属性");
        log.put("module", "文件");
        log.put("username", username);
        log.put("companyName", !"".equals(companyName) ? companyName : companyId);
        // login 用户登录 access 功能访问 operation 功能操作
        log.put("type", "access");
        log.put("operate", "所有文档/我的文档");
        LogUtils.saveBaseLog(compLocator, log);
        // 日志------------------------------------
      }
    }
    return showDetailStr.toString().length() > 0 ? showDetailStr.toString() : "暂无可编辑的属性信息。";
  }

  public boolean editFileOtherProp(Map<String, Object> params) {
    String companyId = (String) params.get("companyId");
    String fileId = (String) params.get("fileId");
    String remoteAddr = (String) params.get("remoteAddr");
    String userName = (String) params.get("userName");
    List<String> columName = (List<String>) params.get("KeyLst");
    List<String> columVal = (List<String>) params.get("valueLst");
    boolean bol = filesDao.editFileOtherProp(companyId, fileId, columName, columVal);
    if (bol) {
      Map<String, String> map = new HashMap<String, String>();
      map.put("companyId", companyId);
      map.put("ID", fileId);
      String colStr = "";
      if (columName != null && columName.size() > 0) {
        for (String s : columVal) {
          colStr += " ";
          colStr += s;
        }
      }
      map.put("userDefinedProp", colStr);

      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", remoteAddr);
      log.put("userid", userName);
      log.put("loginfo", "编辑文件属性【" + columVal + "】");
      log.put("module", "文件");
      log.put("username", userName);
      log.put("companyName", !"".equals(companyName) ? companyName : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "所有文档/我的文档");
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
      // 修改时同事传参并且记录日志
      map.put("ip", remoteAddr);
      map.put("username", userName);
      // getLuceneWS().updateIndex(map);
    }
    return bol;
  }

  public void getFolderById(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String folderId = request.getParameter("folderId");
    String userId = request.getParameter("userId");
    List<Map<String, String>> lst = filesDao.getFolderById(companyId, folderId);
    // 过滤没有上传权限的分类，文件夹不需要过滤
    if ("1".equals(folderId)) {
      // 查询用户属于哪些群组分类
      List<String> userClassIds = filesDao.getUserClassIds(companyId, userId);
      Iterator<Map<String, String>> it = lst.iterator();
      while (it.hasNext()) {
        if (!userClassIds.contains(it.next().get("ID"))) {
          it.remove();
        }
      }
    }
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("files", lst);
    result.put("size", lst.size());
    writeJson(response, request.getParameter("callback"), gson.toJson(result));
  }

  public void getSubScribeMsgByUserId(HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("userId", request.getParameter("userId"));
    map.put("companyId", request.getParameter("companyId"));
    map.put("pageIndex", request.getParameter("pageIndex"));
    map.put("pageLimit", request.getParameter("pageLimit"));
    // 20151112 xiayongcai 新增时间段查询标示
    map.put("findDateflg", request.getParameter("findDateflg"));
    Map<String, Object> result = new HashMap<String, Object>();
    List<Map<String, String>> subScribes = filesDao.getSubScribeMsgByUserId(map);
    int size = 0;
    if (subScribes.size() > 0) {
      size = Integer.parseInt(subScribes.get(0).get("size"));
      subScribes.remove(0);
    }
    result.put("subScribes", subScribes);
    result.put("subScribeSize", size);
    writeJson(response, request.getParameter("callback"), gson.toJson(result));
  }

  public Map<String, String> addSubScribeMsgByUserId(Map<String, String> params) {
    return filesDao.addSubScribeMsgByUserId(params);
  }

  public Map<String, String> addSubScribeMsgByDocs(Map<String, String> params) {
    return filesDao.addSubScribeMsgByDocs(params);
  }

  public Map<String, String> delSubScribeMsgByUserId(Map<String, String> params) {
    return filesDao.delSubScribeMsgByUserId(params);
  }

  public void getSubScribersByUserId(HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("userId", request.getParameter("userId"));
    map.put("userName", request.getParameter("userName"));
    map.put("companyId", request.getParameter("companyId"));
    Map<String, Object> result = filesDao.getSubScribersByUserId(map);
    writeJson(response, request.getParameter("callback"), gson.toJson(result));

  }

  /**
   * @param userId 用户ID
   * @param pageIndex 起始查询值
   * @param pageLimit 查询分页
   * @param keyWord 关键词 通过用户ID查询出所有的用户的订约人的信息
   */
  public void searchSubScribeMsgByUserId(HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("userId", request.getParameter("userId"));
    map.put("pageIndex", request.getParameter("pageIndex"));
    map.put("pageLimit", request.getParameter("pageLimit"));
    map.put("companyId", request.getParameter("companyId"));
    map.put("keyWord", request.getParameter("keyWord"));
    Map<String, Object> result = new HashMap<String, Object>();
    List<Map<String, String>> subScribes = filesDao.searchSubScribeMsgByUserId(map);
    int size = Integer.parseInt(subScribes.get(0).get("count"));
    result.put("subScribeSize", size);
    subScribes.remove(0);
    result.put("subScribes", subScribes);
    writeJson(response, request.getParameter("callback"), gson.toJson(result));
  }

  public void checkFileDelete(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Boolean> result = new HashMap<String, Boolean>();
    result
        .put(
            "success",
            filesDao.checkFileDelete(request.getParameter("fileId"),
                request.getParameter("companyId")));
    writeJson(response, request.getParameter("callback"), gson.toJson(result));
  }

  public void getFileStatus(HttpServletRequest request, HttpServletResponse response) {
    String fileId = request.getParameter("fileId");
    String companyId = request.getParameter("companyId");
    Map<String, String> fileInfo = filesDao.getFileInfo(companyId, fileId, null);
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("fileInfo", fileInfo);
    writeJson(response, request.getParameter("callback"), gson.toJson(result));
  }

  /**
   * 分享某个文件到路径下
   */
  public String shareToFolderPath(Map<String, String> params) {
    String companyId = params.get("companyId");
    String fileId = params.get("fileId");
    String folderId = params.get("folderId");
    String folderName = params.get("folderName");
    String openlevel = params.get("openlevel");
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");

    // 获取folder的idseq
    String folderIdSeq = filesDao.getIdSeqWithId(companyId, folderId);
    String fileIdSeq = folderIdSeq + folderId + ".";
    //
    // 设为私密时 把所有历史版本设为私密
    Map<String, String> fileInfo = filesDao.getFileById(companyId, folderId, fileId);
    // 获得所有版本
    boolean flag = false;
    List<Map<String, String>> versionList =
        filesDao.getFileList4Version(companyId, 0 + "", fileInfo.get("filename"),
            fileInfo.get("type"));
    if (versionList.size() > 0) {
      for (int i = 0; i < versionList.size(); i++) {
        if (filesDao.shareToFolderPath(companyId, versionList.get(i).get("id"), folderId,
            fileIdSeq, openlevel)) {
          flag = true;
        } else {
          flag = false;
          break;
        }
      }
    } else {
      flag = filesDao.shareToFolderPath(companyId, fileId, folderId, fileIdSeq, openlevel);
    }

    //
    // boolean flag = filesDao.shareToFolderPath(companyId, fileId, folderId, fileIdSeq, openlevel);
    String rst = "failure";
    if (flag) {
      rst = "success";
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", remoteAddr);
      log.put("userid", userName);
      log.put("loginfo", "分享文件【" + fileId + "】到【" + folderId + "】");
      log.put("module", "文件信息");
      log.put("username", userName);
      log.put("companyName", !"".equals(companyName) ? companyName : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "文件信息");
      Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
      if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")) {
        log.put("fileId", fileId + "");
        log.put("folderId", felFileMap.get("folderId") + "");
        Map<String, String> userFullNameMaps =
            filesDao.getUserFullNameByCompanyId(params.get("companyId"));
        log.put("fileSubscribMsg",
            "分享文件【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>到分类【 "
                + folderName + "】");
      }
      log.put("companyId", companyId);
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    }
    return rst;
  }

  /**
   * wangwenshuo 20151020 复制到
   */
  public Map<String, String> copyToFolderPath(Map<String, String> params) {
    String companyId = params.get("companyId");
    String fileId = params.get("fileId");
    String folderId = params.get("folderId");
    String folderName = params.get("folderName");
    String openlevel = params.get("openlevel");
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");

    // 获取folder的idseq
    String folderIdSeq = filesDao.getIdSeqWithId(companyId, folderId);
    String fileIdSeq = folderIdSeq + folderId + ".";
    //
    // 设为私密时 把所有历史版本设为私密
    Map<String, String> fileInfo = filesDao.getFileById(companyId, folderId, fileId);

    // 获得所有版本
    boolean flag = false;
    List<Map<String, String>> versionList =
        filesDao.getFileList4Version(companyId, fileInfo.get("classid"), fileInfo.get("filename"),
            fileInfo.get("type"));

    // 判断目标路径是否有同名文件，如果有同名文件，重命名当前复制文件
    String newFileName = fileInfo.get("filename");
    int index = 0;
    boolean hasExist =
        filesDao.checkFileNameExist(companyId, folderId, fileInfo.get("filename"),
            fileInfo.get("type"));
    while (hasExist) {
      index++;
      newFileName = fileInfo.get("filename") + "(" + index + ")";
      hasExist =
          filesDao.checkFileNameExist(companyId, folderId, newFileName, fileInfo.get("type"));
    }

    // 复制文件
    flag =
        filesDao.copyToFolderPath(companyId, versionList, newFileName, folderId, fileIdSeq,
            openlevel);

    Map<String, String> map = new HashMap<String, String>();
    map.put("success", "false");
    if (flag) {
      // 为复制的文件建立索引
      List<Map<String, String>> files =
          filesDao.getFileList4Version(companyId, folderId, fileInfo.get("filename"),
              fileInfo.get("type"));
      for (Map<String, String> fileMap : files) {
        Map<String, String> createLuceneIndexParams = new HashMap<String, String>();
        createLuceneIndexParams.put("ID", fileMap.get("id"));
        createLuceneIndexParams.put("COMPANYID", companyId);
        /*
         * createLuceneIndexParams.put("fileId", fileMap.get("fileId"));
         * createLuceneIndexParams.put("classId", fileMap.get("classId"));
         * createLuceneIndexParams.put("fileName",newFileName );
         * createLuceneIndexParams.put("fileType", fileMap.get("type"));
         * createLuceneIndexParams.put("version", fileMap.get("version"));
         * createLuceneIndexParams.put("insertId", fileMap.get("id"));
         * createLuceneIndexParams.put("idSeq", fileMap.get("idSeqSrc") );
         * createLuceneIndexParams.put("openlevel", openlevel); createLuceneIndexParams.put("size",
         * fileMap.get("size")); createLuceneIndexParams.put("createtime",
         * fileMap.get("createTime")); createLuceneIndexParams.put("createrId",
         * fileMap.get("userId") ); createLuceneIndexParams.put("ip", remoteAddr);
         * createLuceneIndexParams.put("userid", fileMap.get("userId") );
         * createLuceneIndexParams.put("username", userName );
         * createLuceneIndexParams.put("companyName", "");//记log用的，暂时先不传了
         * createLuceneIndexParams.put("isFile", fileMap.get("isFile"));
         * createLuceneIndexParams.put("isDel", fileMap.get("isDel"));
         * createLuceneIndexParams.put("isLast", fileMap.get("isLast"));
         * createLuceneIndexParams.put("md5", fileMap.get("md5"));
         */
        getLuceneWS().createIndex(createLuceneIndexParams);
      }

      map.put("success", "true");
      map.put("fileName", newFileName);
      map.put("isRename", !newFileName.equals(fileInfo.get("filename")) + "");
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", remoteAddr);
      log.put("userid", userName);
      log.put("loginfo", "复制文件【" + fileId + "】到【" + folderId + "】");
      log.put("module", "文件信息");
      log.put("username", userName);
      log.put("companyName", !"".equals(companyName) ? companyName : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "文件信息");
      Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
      if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")) {
        log.put("fileId", fileId + "");
        log.put("folderId", felFileMap.get("folderId") + "");
        Map<String, String> userFullNameMaps =
            filesDao.getUserFullNameByCompanyId(params.get("companyId"));
        log.put("fileSubscribMsg",
            "复制文件【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>到分类【 "
                + folderName + "】");
      }
      log.put("companyId", companyId);
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    }
    return map;
  }

  /**
   * 获取文件夹路径
   */
  public void getFolderPath(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    // 20151122 xyc 禁止用户没有企业概念，等确定清楚后再加
    if (companyId != null && "-1".equals(companyId)) {
      writeJson(response, request.getParameter("callback"), gson.toJson(""));
      return;
    }
    String folderId = request.getParameter("folderId");
    String idSeq = filesDao.getIdSeqWithId(companyId, folderId);
    List<Map<String, String>> list = filesDao.getClassByIdseq(idSeq + folderId + ".", companyId);
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("path", list);
    writeJson(response, request.getParameter("callback"), gson.toJson(result));
  }

  /**
   * 申请文件下载前，时间间隔的判断
   */
  public void applyFileDown(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String username = request.getParameter("username");
    String fileId = request.getParameter("fileId");
    String fileTitle = request.getParameter("fileTitle");
    String applyType = request.getParameter("applyType");
    String timeOutKey = "applyFileDownTimeOut" + companyId;
    String applyKey = username + "_" + fileId + "_" + applyType;
    JSONObject json = new JSONObject();
    // 放入缓存----------------------------------
    Map<String, String> applyTimeOut = null;
    Object obj = CacheUtils.get(this.getCompLocator(), applyKey);
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
            long l = cal1.getTimeInMillis() - cal2.getTimeInMillis();
            int days = new Long(l / (1000 * 60)).intValue();
            if (days >= 3) {// 3分钟只能发送一次
              json.put("isOk", true);
              applyTimeOut.put(applyKey, nowtime);
              CacheUtils.set(this.getCompLocator(), applyKey, applyTimeOut);
            } else {
              json.put("isOk", false);
            }
          } catch (ParseException e) {
            e.printStackTrace();
          }
        } else {
          json.put("isOk", true);
          String now = format.format(System.currentTimeMillis());
          applyTimeOut.put(applyKey, now);
          CacheUtils.set(this.getCompLocator(), applyKey, applyTimeOut);
        }
      }
      CacheUtils.set(this.getCompLocator(), applyKey, applyTimeOut);
    } else {
      json.put("isOk", true);
      applyTimeOut = new HashMap<String, String>();
      String now = format.format(System.currentTimeMillis());
      applyTimeOut.put(applyKey, now);
      CacheUtils.set(this.getCompLocator(), applyKey, applyTimeOut);
    }
    writeJson(response, request.getParameter("callback"), gson.toJson(json));
  }

  /**
   * 获取回收站的文件列表
   * 
   * @author longjunhao 20150609
   * @param params
   * @return
   */
  public void getTrashFileList(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String callback = request.getParameter("callback");
    // 20151122 xyc 回收站。按照企业查询，禁止用户没有企业概念，等独立用户回收站有独立的表需要加上
    // if(companyId!=null && "-1".equals(companyId)){
    // writeJson(response, callback, gson.toJson(""));
    // return;
    // }
    String userName = request.getParameter("userName");
    String loginUserId = request.getParameter("loginUserId");
    String pageStr = request.getParameter("page");
    String pageSizeStr = request.getParameter("pageSize");
    String keyWord = request.getParameter("keyWord");

    int page = Integer.parseInt(pageStr); // 1 2 3 4..
    int limit = Integer.parseInt(pageSizeStr);
    int start = (page - 1) * limit;
    String orderField = request.getParameter("orderField");
    String orderType = request.getParameter("orderType");
    if (orderField == null || orderField.trim().length() == 0) {
      orderType = null;
    }
    List<Map<String, String>> files =
        filesDao.getTrashFileList(companyId, loginUserId, start, limit, orderType, orderField,
            keyWord);
    // 计算总页数
    int count = filesDao.getTrashFileListCount(companyId, loginUserId, keyWord);

    /** wangwenshuo 20151202 使用关联查询代替注释代码的分别请求查询 */
    /*
     * // 获取文件列表 List<Map<String, String>> files = filesDao.getFileListByIds(companyId, fileIds);
     * List<Map<String, String>> trashFiles = filesDao.getTrashFileListByIds(companyId, fileIds); //
     * 获取文件总条数 // 设置文件上传者的username setupFilesCreatorName(companyId, files);
     * setupFilesDeleteUserName(companyId, trashFiles); setupParentClassName(companyId, files); //
     * setupFolderPathName(companyId, files); setupTrashFilePathName(files, trashFiles);
     */
    int total = (count + limit - 1) / limit;
    Map result = new HashMap();
    result.put("files", files);
    result.put("size", files.size());
    result.put("total", total);
    result.put("page", page);
    result.put("count", count);

    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", request.getRemoteAddr());
    log.put("userid", userName);
    log.put("loginfo", "获得自己回收站文件列表");
    log.put("module", "文件列表");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "回收站");
    LogUtils.saveBaseLog(compLocator, log);
    // 日志------------------------------------
    writeJson(response, callback, gson.toJson(result));
  }

  /**
   * 设置回收站文件的原路径,和删除时间
   * 
   * @author longjunhao 20150817
   * @param companyId
   * @param files
   */
  private void setupTrashFilePathName(List<Map<String, String>> files,
      List<Map<String, String>> trashFiles) {
    for (Map<String, String> file : files) {
      for (Map<String, String> trashFile : trashFiles) {
        if (file.get("id").equals(trashFile.get("fileId"))) {
          file.put("pathName", trashFile.get("pathName"));
          file.put("deleteTime", trashFile.get("deleteTime"));
          file.put("trashId", trashFile.get("trashId"));
        }
      }
    }
  }

  /**
   * 将文件添加到回收站
   * 
   * @param fileId
   * @return
   */
  public boolean addFileTrashById(String companyId, String fileId, String userId) {
    String idSeqSrc = filesDao.getIdSeqWithId(companyId, fileId);
    String folderPathName = getFolderPathName(companyId, idSeqSrc);
    return filesDao.addFileTrashById(companyId, fileId, userId, folderPathName);
  }

  /**
   * 回收站文件恢复
   */
  public String fileRestore(Map<String, String> params) {
    String userName = params.get("userName");
    String fileId = params.get("fileId");
    String trashId = params.get("trashId");
    String companyId = params.get("companyId");
    String userId = params.get("userId");

    Map<String, String> map = filesDao.fileRestore(trashId, companyId, userId);
    if (map.isEmpty()) {
      return "false";
    }
    // **************xyc 等恢复加入 查询重新排序后，去掉查询排序
    String indexIds = map.get("indexIds");
    String failIds = map.get("failIds");
    String fileIds = map.get("fileIds");
    // 新创建的文件夹时要建立索引的
    if (!"".equals(indexIds) && null != indexIds) {
      if (indexIds.endsWith(","))
        indexIds = indexIds.substring(0, indexIds.length() - 1);
      List<Map<String, String>> files =
          filesDao.getFileListByIds(companyId, Arrays.asList(indexIds.split(",")));
      for (Map<String, String> dirMap : files) {
        Map<String, String> createLuceneIndexParams = new HashMap<String, String>();
        createLuceneIndexParams.put("COMPANYID", companyId);
        createLuceneIndexParams.put("ID", dirMap.get("id"));
        /*
         * createLuceneIndexParams.put("classId", dirMap.get("classId") );
         * createLuceneIndexParams.put("fileId", "0-0-0");
         * createLuceneIndexParams.put("fileName",dirMap.get("fileName") );
         * createLuceneIndexParams.put("fileType", ""); createLuceneIndexParams.put("insertId",
         * dirMap.get("id")); createLuceneIndexParams.put("idSeq", dirMap.get("idSeqSrc") );
         * createLuceneIndexParams.put("openlevel", "1"); createLuceneIndexParams.put("size", "0");
         * createLuceneIndexParams.put("createtime", new
         * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
         * createLuceneIndexParams.put("createrId", dirMap.get("userId") );
         * createLuceneIndexParams.put("ip", params.get("remoteAddr"));
         * createLuceneIndexParams.put("userid", dirMap.get("userId") );
         * createLuceneIndexParams.put("username", userName );
         * createLuceneIndexParams.put("companyName", "");//记log用的，暂时先不传了
         * createLuceneIndexParams.put("isFile", "0"); createLuceneIndexParams.put("isDel", "0");
         * createLuceneIndexParams.put("isLast", "1"); createLuceneIndexParams.put("md5", "");
         */
        getLuceneWS().createIndex(createLuceneIndexParams);
      }
    }

    // 获取恢复失败的文件（文件夹）名称 用于前端提示
    String failFileNames = ""; // 恢复失败文件名
    if (!"".equals(failIds) && null != failIds) {
      if (failIds.endsWith(","))
        failIds = failIds.substring(0, failIds.length() - 1);
      String[] fails = failIds.split(",");
      for (String id : fails) {
        Map<String, String> dirMap = filesDao.getFileInfo(companyId, id, null);
        failFileNames += dirMap.get("fileName") + ",";
      }
      failFileNames = failFileNames.substring(0, failFileNames.length() - 1);
    }

    // 恢复的文件夹或文件 文件的话就要恢复文件本身的索引 文件夹还要恢复子的索引 这块非常耗性能 如果有好的办法 要优化一下
    Map<String, Map> m = new HashMap<String, Map>(); // 使用map的原因是能去重
    if (!"".equals(fileIds) && null != fileIds) {
      if (fileIds.endsWith(","))
        fileIds = fileIds.substring(0, fileIds.length() - 1);
      List<Map<String, String>> files =
          filesDao.getFileListByIds(companyId, Arrays.asList(fileIds.split(",")));
      for (Map<String, String> file : files) {
        if ("0".equals(file.get("isFile"))) {
          List<Map<String, String>> subFiles =
              filesDao.getSubFilesByIdseq(companyId, file.get("idSeq"));
          for (Map<String, String> subM : subFiles) {
            subM.put("companyId", companyId);
            m.put(subM.get("id"), subM);
          }
        } else {
          file.put("companyId", companyId);
          file.put("idSeq", file.get("idSeqSrc"));
          m.put(file.get("id"), file);
        }
      }
    }
    // 更新索引
    if (!m.isEmpty()) {
      getLuceneWS().updateIndexForList(new ArrayList(m.values()));
    }
    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", params.get("remoteAddr"));
    log.put("userid", userName);
    log.put("loginfo", "回收站恢复文件,id:" + fileId);
    log.put("module", "回收站");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "回收站");
    LogUtils.saveBaseLog(compLocator, log);

    return "".equals(failFileNames) ? "true" : failFileNames;
  }

  /**
   * 回收站文件彻底删除
   */
  public String fileDestroy(Map<String, String> params) {
    String userName = params.get("userName");
    String fileId = params.get("fileId");
    String companyId = params.get("companyId");
    String userId = params.get("userId");
    // 将files_trash_n表的改文件的isDelete改为1
    boolean flag = filesDao.fileDestroy(companyId, fileId, userId);

    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", params.get("remoteAddr"));
    log.put("userid", userName);
    log.put("loginfo", "回收站彻底删除文件,id:" + fileId);
    log.put("module", "回收站");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "回收站");
    LogUtils.saveBaseLog(compLocator, log);

    return flag ? "success" : "";
  }

  /**
   * 清空回收站 wangwenshuo 20150806
   */
  public String cleanRecycleBin(Map<String, String> params) {
    // //20151122 xyc 回收站。按照企业查询，禁止用户没有企业概念，等独立用户回收站有独立的表需要加上
    String companyId = params.get("companyId");
    if (companyId != null && "-1".equals(companyId)) {
      return "success";
    }

    String userName = params.get("userName");
    String userId = params.get("userId");
    // 将files_trash_n表的改文件的isDelete改为1
    boolean flag = filesDao.cleanRecycleBin(companyId, userId);

    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", params.get("remoteAddr"));
    log.put("userid", userName);
    log.put("loginfo", "清空回收站");
    log.put("module", "回收站");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "回收站");
    LogUtils.saveBaseLog(compLocator, log);

    return flag ? "success" : "";
  }

  /**
   * 获取用户的常用分类列表
   * 
   * @param request
   * @param response
   */
  public void getClassStarList(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String userId = request.getParameter("userId");
    String username = request.getParameter("username");
    String callback = request.getParameter("callback");
    // xiewenda 查询常用分类是加入公司id
    // List<String> starIds = filesDao.getClassStarIds(userId);
    List<String> starIds = filesDao.getClassStarIds(userId, companyId);
    List<Map<String, String>> classList = filesDao.getClassList(companyId, starIds);

    Map<String, Map<String, String>> groupList =
        filesDao.getCompanyGroups(companyId, username, userId);
    Map<String, String> map = null;
    for (Map<String, String> classMap : classList) {
      map = groupList.get(classMap.get("id"));
      if (map != null) {
        classMap.put("groupId", map.get("id"));
        classMap.put("flag", map.get("flag"));
        classMap.put("remark", map.get("remark"));
      }
    }

    // 20151119 查询每组分类图片
    Map<String, String> classImg = filesDao.getClassImg(companyId);
    Map<String, String> maps = null;
    List<Map<String, String>> groups = new ArrayList<Map<String, String>>();

    for (String map1 : classImg.keySet()) {
      maps = new HashMap<String, String>();
      maps.put("FLAG", map1);
      maps.put("PORTRAITS", classImg.get(map1));
      groups.add(maps);
    }
    List<Map<String, String>> groupss =
        getChatWS().doGroupImageMath(companyId, username, "", groups);
    if (groupss != null)
      for (Map<String, String> strMap : groupss) {
        classImg.put(strMap.get("FLAG"), strMap.get("PORTRAITS"));
      }



    // 添加当前用户所属分类群组的 消息数目、时间等信息
    this.filesDao.getClassExtraInfo(classList, companyId, username, userId);

    // 当前用户关联的分组id列表
    List<String> groupIds = filesDao.getGroupIdsWithUserId(userId);
    for (Map<String, String> classMap : classList) {
      if (groupIds.contains(classMap.get("groupId"))) {
        classMap.put("isMember", "true");
        classMap.put("ordermsgcount", classMap.get("newmessagecount"));
      } else {
        classMap.put("isMember", "false");
        classMap.put("ordermsgcount", "-1"); // 没有权限的排在最后
      }
      if (starIds.contains(classMap.get("id"))) {
        classMap.put("isStar", "true");
      } else {
        classMap.put("isStar", "false");
      }
      classMap.put("PORTRAITS",
          classImg.get(classMap.get("flag")) != null ? classImg.get(classMap.get("flag"))
              : "apps/onlinefile/templates/ESDefault/images/profle.png");
    }
    JSONObject json = new JSONObject();
    /** 对分类进行排序 消息数量多的在前面 **/
    Collections.sort(classList, new ClassComparator("ordermsgcount"));
    json.put("classes", classList);
    writeJson(response, callback, json.toJSONString());
  }

  /**
   * 获取单文件权限
   * 
   * @param request
   * @param response
   */
  public void getFileAuthority(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String userId = request.getParameter("userId");
    String username = request.getParameter("username");
    String callback = request.getParameter("callback");
    // xiewenda 查询常用分类是加入公司id
    // List<String> starIds = filesDao.getClassStarIds(userId);

    JSONObject json = new JSONObject();
    writeJson(response, callback, json.toJSONString());
  }

  /**
   * 标记/取消标记为常用分类
   */
  public String starClass(Map<String, String> params) {
    String userName = params.get("userName");
    String classId = params.get("classId");
    String companyId = params.get("companyId");
    String userId = params.get("userId");
    String status = params.get("status");
    boolean flag = filesDao.starClass(userId, companyId, classId, Boolean.parseBoolean(status));
    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", params.get("remoteAddr"));
    log.put("userid", userName);
    log.put("loginfo", "true".equals(status) ? "标记" : "取消标记" + "分类id" + classId + "为常用分类");
    log.put("module", "常用分类");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "常用分类");
    LogUtils.saveBaseLog(compLocator, log);

    return flag ? "success" : "";

  }

  /**
   * 撤销文件分享
   */
  public String backoutFile(Map<String, String> params) {
    String companyId = params.get("companyId");
    String fromCompanyId = params.get("fromCompanyId"); // 我的文档分享出来的 user_N
    String fileId = params.get("fileId");
    String receiver = params.get("receiver");
    String isGroupStr = params.get("isGroup");
    String userName = params.get("userName");
    String accessRight = params.get("accessRight");
    String remoteAddr = params.get("remoteAddr");
    boolean isGroup = "1".equals(isGroupStr) ? true : false;
    String targetId = "";
    String receiverName = "";
    if (isGroup) { // 联系人群组 c0g1428484244718
      // 获取联系人群组id
      Map<String, String> groupMap = filesDao.getGroupInfoByFlag(companyId, receiver);
      targetId = groupMap.get("id");
      receiverName = "".equals(groupMap.get("classId")) ? "群组" : "分类" + groupMap.get("groupName");
    } else { // 联系人 long2
      // 获取联系人id
      targetId = filesDao.getUserIdWithName(companyId, receiver);
    }

    String rst = "failure";
    if (null != fromCompanyId && fromCompanyId.contains("user_"))
      companyId = fromCompanyId;

    boolean flag = false;
    /** wangwenshuo 20151215 做兼容 历史数据没有 accessRight这个参数 */
    if (accessRight == null || "".equals(accessRight) || "undefined".equals(accessRight)) {
      // 判断当前是否依然是分享状态
      if (filesDao.hasShareFileToUserOrGroup(companyId, fileId, targetId, isGroup)) {
        flag = filesDao.backoutFile(companyId, fileId, targetId, isGroup);
      } else {
        rst = "haveBacked";
      }
    } else {
      Map<String, String> retMap =
          filesDao.getHasShareFileToUserOrGroup(companyId, fileId, targetId, isGroup, false);
      // 分享过
      if (!retMap.isEmpty()) {
        // 仅有浏览权限 撤销浏览权限
        if (!"1".equals(retMap.get("isDownload")) && "1".equals(retMap.get("isLook"))
            && "1".equals(accessRight)) {
          flag = filesDao.backoutFile(companyId, fileId, targetId, isGroup);
          // 仅是下载权限
        } else if ("1".equals(retMap.get("isDownload")) && !"1".equals(retMap.get("isLook"))
            && "2".equals(accessRight)) {
          flag = filesDao.backoutFile(companyId, fileId, targetId, isGroup);
          // 浏览下载权限都有 都去掉
        } else if ("3".equals(accessRight)) {
          flag = filesDao.backoutFile(companyId, fileId, targetId, isGroup);
          // 有下载和浏览权限 去掉一个
        } else {
          flag =
              filesDao.updateOrShareFileToUserOrGroup(companyId, fileId, targetId, isGroup,
                  userName.replace("\\40", "@"), true, "isShareUpdateRight",
                  "2".equals(accessRight) ? "0" : "1", "1".equals(accessRight) ? "0" : "1", false);
        }
      } else {
        rst = "haveBacked";
      }
    }
    if (flag) {
      rst = "success";
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", remoteAddr);
      log.put("userid", userName);
      log.put("loginfo", "撤销文件【" + fileId + "】给用户【" + receiver + "】的分享");
      log.put("module", "文件列表");
      log.put("username", userName);
      log.put("companyName", !"".equals(companyName) ? companyName : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "所有文档/我的文档");
      Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
      if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")
          && isGroup) {
        log.put("fileId", fileId + "");
        log.put("folderId", felFileMap.get("folderId") + "");
        Map<String, String> userFullNameMaps =
            filesDao.getUserFullNameByCompanyId(params.get("companyId"));
        log.put("fileSubscribMsg",
            "撤销了文件【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>到 "
                + receiverName + "的分享");
      }
      log.put("companyId", companyId);
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    }
    return rst;
  }

  /**
   * 发表文件评论
   */
  public String newFileComment(Map<String, String> params) {
    String companyId = params.get("companyId");
    String fileFlag = params.get("fileFlag");
    String version = params.get("version");
    String content = params.get("content");
    String portrait = params.get("portrait");
    String fullName = params.get("fullName");
    String userId = params.get("userId");
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");
    Map<String, String> data = new HashMap<String, String>();
    data.put("companyId", companyId);
    data.put("fileFlag", fileFlag);
    data.put("version", version);
    data.put("content", content);
    data.put("portrait", portrait);
    data.put("userId", userId);
    data.put("userName", userName);
    data.put("fullName", fullName);
    data.put("remoteAddr", remoteAddr);
    getChatWS().newFileComment(data);
    return "";
  }

  /**
   * 删除文件评论
   */
  public String deleteComment(Map<String, String> params) {
    getChatWS().deleteComment(params);
    return "";
  }

  /**
   * 获取文件的评论列表
   */
  public void getFileCommentList(HttpServletRequest request, HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String userId = request.getParameter("userId");
    String userName = request.getParameter("userName");
    String fileFlag = request.getParameter("fileFlag");
    String version = request.getParameter("version");
    String pageStr = request.getParameter("page");
    String pageSizeStr = request.getParameter("pageSize");
    String callback = request.getParameter("callback");
    String versions = request.getParameter("versions");
    int page = Integer.parseInt(pageStr);
    int limit = Integer.parseInt(pageSizeStr);
    int start = (page - 1) * limit;
    // String[] version_shuzu = versions.split(",");
    Map result = new HashMap();
    if (versions == null || versions == "") {
      Map<String, String> data = new HashMap<String, String>();
      data.put("companyId", companyId);
      data.put("fileFlag", fileFlag);
      data.put("version", version);
      data.put("limit", limit + "");
      data.put("page", page + "");
      data.put("skip", start + "");
      List<Map<String, String>> commentList = getChatWS().getFileCommentList(data);
      result.put("comments", commentList);
      result.put("page", page);
    } else {
      Map<String, String> data = new HashMap<String, String>();
      data.put("companyId", companyId);
      data.put("fileFlag", versions);
      data.put("version", version);
      data.put("limit", limit + "");
      data.put("page", page + "");
      data.put("skip", start + "");
      List<Map<String, String>> commentList = getChatWS().getFileCommentListByVersions(data);
      result.put("comments", commentList);
      result.put("page", page);
    }

    // 日志------------------------------------
    writeJson(response, callback, gson.toJson(result));
  }

  /**
   * 获得文件评论之post提交
   */
  public Map<String, Object> getFileCommentList(Map<String, String> param) {
    String companyId = param.get("companyId");
    String userId = param.get("userId");
    String userName = param.get("userName");
    String fileFlag = param.get("fileFlag");
    String version = param.get("version");
    String pageStr = param.get("page");
    String pageSizeStr = param.get("pageSize");
    String callback = param.get("callback");
    String versions = param.get("versions");
    int page = Integer.parseInt(pageStr);
    int limit = Integer.parseInt(pageSizeStr);
    int start = (page - 1) * limit;
    // String[] version_shuzu = versions.split(",");
    Map result = new HashMap();
    if (versions == null || versions == "") {
      Map<String, String> data = new HashMap<String, String>();
      data.put("companyId", companyId);
      data.put("fileFlag", fileFlag);
      data.put("version", version);
      data.put("limit", limit + "");
      data.put("page", page + "");
      data.put("skip", start + "");
      List<Map<String, String>> commentList = getChatWS().getFileCommentList(data);
      result.put("comments", commentList);
      result.put("page", page);
    } else {
      Map<String, String> data = new HashMap<String, String>();
      data.put("companyId", companyId);
      data.put("fileFlag", versions);
      data.put("version", version);
      data.put("limit", limit + "");
      data.put("page", page + "");
      data.put("skip", start + "");
      List<Map<String, String>> commentList = getChatWS().getFileCommentListByVersions(data);
      result.put("comments", commentList);
      result.put("page", page);
    }

    return result;
  }


  /**
   * 修改文件的拥有者
   */
  public String changeFileOwner(Map<String, String> params) {
    String userstatus = params.get("userstatus");// 改变后的状态，1为启用，0为禁用
    String userIdStr = params.get("userIds");// 多个用逗号隔开的userIds
    String companyId = params.get("companyId");
    String username = params.get("loginUser");
    String userId = params.get("userId"); // 当前操作用户即为管理员
    // 更改文件拥有者
    String msg = filesDao.changeFileOwner(userIdStr, userstatus, userId, companyId);
    this.updateCompanyClassesCache(companyId, userId, username);
    return msg;
  }

  /**
	 * 
	 */
  public List<Map<String, String>> changeGroupRelationIsAdmin(Map<String, String> params) {
    String userstatus = params.get("userstatus");// 改变后的状态，1为启用，0为禁用
    String userIdStr = params.get("userIds");// 多个用逗号隔开的userIds
    String companyId = params.get("companyId");
    String username = params.get("loginUser");
    String userId = params.get("userId"); // 当前操作用户即为管理员
    // 更改文件拥有者
    List<Map<String, String>> list =
        filesDao.changeGroupRelationIsAdmin(userIdStr, userstatus, userId, companyId);
    this.updateCompanyClassesCache(companyId, userId, username);
    return list;
  }

  /**
   * 上传文件成功后调用，往db里插入file数据
   */
  public String insertNewFileAfterUpload(Map<String, String> params) {
    String userId = params.get("userId");
    String companyId = params.get("companyId");
    // String username = params.get("username");
    String classId = params.get("classId");
    String fileName = params.get("fileName");
    String size = params.get("size");
    String md5 = params.get("md5");
    String type = params.get("type");
    String openLevel = params.get("openLevel");
    String fileId = params.get("fileId");
    String ip = params.get("remoteAddr");
    System.out.println("params:" + params.toString());
    // 修改索引库的不同版本的isLast字段
    // 插入新版本前 获取当前最新文件版本###
    List<Map<String, String>> files = filesDao.getFileOfIsLast(companyId, classId, fileName, type);
    // 如果存在同名文件，所有的公开级别，都与上个版本相同，包括分类级公开与私密。。。
    if (files != null && files.size() > 0) {
      openLevel = files.get(0).get("openlevel");
    }
    Map<String, String> rstMap =
        filesDao.insertIntoFilesN(companyId, classId, fileName, type, size, md5, openLevel, fileId,
            userId);
    String fileInsertId = "";
    if (rstMap.size() > 0) {
      if (files.size() > 0) {
        getLuceneWS().updateIndexOfisLast(files, companyId);
      }
      fileInsertId = rstMap.get("id");
      String version = rstMap.get("version");
      Map<String, String> indexFile = new HashMap<String, String>();
      // Map<String,String> indexFile = filesDao.getFileForIndex(companyId,fileInsertId);
      indexFile.put("ID", fileInsertId);
      indexFile.put("COMPANYID", companyId);
      getLuceneWS().createIndex(indexFile);
      // 日志------------------------------------
      // 获得登陆名
      String userName = getUserWS().getUserNameById(userId);
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", ip);
      log.put("userid", userName);
      log.put("loginfo", "上传文件【" + fileName + "】在ID为【" + classId + "】下");
      log.put("module", "文件");
      log.put("username", userName);
      log.put("companyName", !"".equals(companyName) ? companyName : companyId);
      log.put("companyId", companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "所有文档/我的文档");
      if (null != openLevel && !openLevel.equals("3")) {
        log.put("fileId", fileInsertId + "");
        log.put("folderId", classId);
        Map<String, String> userFullNameMaps = filesDao.getUserFullNameByCompanyId(companyId);
        log.put("fileSubscribMsg", "上传了【" + fileName + "." + type + "】<span class ='version'>第"
            + version + "版</span>");
      }
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    }

    return fileInsertId;
  }


  /**
   * lujixiang 20150810 批量将文件添加到回收站
   * 
   * @param fileId
   * @return
   */
  public boolean addFilesTrashById(String companyId, List<String> fileId, String userId) {
    String idSeqSrc = filesDao.getIdSeqWithId(companyId, fileId.get(0));
    String folderPathName = getFolderPathName(companyId, idSeqSrc);
    return filesDao.addFilesTrashById(companyId, fileId, userId, folderPathName);
  }

  /**
   * lujixiang 20150810 批量将文件添加到回收站
   * 
   * @param fileId
   * @return
   */
  public boolean deleteFiles(String companyId, List<String> fileIds) {
    return filesDao.deleteFiles(companyId, fileIds);
  }

  /**
   * 安卓接口 getFileRelevantInfo(获取文件相关信息) 整合 checkFileDelete(验证文件) getFileInfo(文件详情)
   * getFileList4Version(版本号)
   * */
  public void getFileRelevantInfo(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> returnMap = new HashMap<String, Object>();// 返回最终数据
    String fileId = request.getParameter("fileId");// fileId:
                                                   // "1-37-afd3779032be1033938d00e04c38b7f4"###id
    String companyId = request.getParameter("companyId");// 公司Id
    String callback = request.getParameter("callback");// 安卓
    String userId = request.getParameter("userId"); // 当前登录用户
    boolean isMyDocument = companyId.contains("user_");
    String userName = "";
    List<Map<String, String>> fpLst = null;
    Map<String, String> file = filesDao.getFileInfo(companyId, fileId, fpLst);
    if (isMyDocument) {
      if (file != null && "1".equals(file.get("isDelete"))) {// 被删除
        returnMap.put("checkFileDelete", "true");
        writeJson(response, callback, gson.toJson(returnMap));
        return;
      }
      returnMap.put("checkFileDelete", "false");
      this.setupFolderPathName(companyId, file);
      List<Map<String, String>> files = new ArrayList<Map<String, String>>();
      files.add(file);
      this.setupParentClassName(companyId, files);
      returnMap.put("file", file);
      Map<String, String> userById = this.getUserWS().getUserById(userId);
      Map<String, String> creator = new HashMap<String, String>();
      creator.put("id", userById.get("ID"));
      userName = userById.get("USERNAME");
      creator.put("userName", userName);
      creator.put("fullName", userById.get("FULLNAME"));
      creator.put("portrait", userById.get("PORTRAIT"));
      returnMap.put("creator", creator);
      returnMap.put("hasRight", "true");
      returnMap.put("isOwner", "true");


      List<Map<String, String>> versionList =
          filesDao.getFileList4Version(companyId, file.get("classId"), file.get("fileName"),
              file.get("type"));
      if (versionList != null && versionList.size() > 1) {
        for (Map<String, String> file1 : versionList) {
          file1.put("hasRight", "1");
          file1.put("userName", userName);
          file1.put("fullName", "");
          file1.put("ownerUserName", userName);
          file1.put("ownerFullName", "");
        }
        this.setupParentClassName(companyId, versionList);
        returnMap.put("size", versionList.size());
        returnMap.put("list", versionList);
        returnMap.put("lastFileId", fileId);

      } else {
        returnMap.put("size", 0);
      }
    } else {
      // 判断文件是否被删除 true 删除
      if (file != null && file.size() > 0) {
        if (file != null && "1".equals(file.get("isDelete"))) {// 被删除
          returnMap.put("checkFileDelete", "true");
          writeJson(response, callback, gson.toJson(returnMap));
          return;
        }
        returnMap.put("checkFileDelete", "false");
        if (!"0".equals(file.get("classId"))) {
          Map<String, String> groupMap =
              filesDao.getFlagWithClassId(companyId, file.get("classId"));
          file.put("groupFlag", groupMap.get("FLAG"));
          this.setupFolderPathName(companyId, file);
        }
        String fileName = file.get("fileName");
        List<Map<String, String>> files = new ArrayList<Map<String, String>>();
        files.add(file);
        this.setupParentClassName(companyId, files);
        this.setupPraise(companyId, userId, files);
        file.put("hasRight", "0");
        Map<String, String> creator = new HashMap<String, String>();
        creator = filesDao.getMemberById(companyId, file.get("userId"));
        Map<String, String> owner = new HashMap<String, String>();
        owner = filesDao.getMemberById(companyId, file.get("owner"));
        // 判断当前用户是否已经点赞
        if (filesDao.getUserIsPraise(companyId, userId, fileId)) {
          file.put("ispraise", "true");
        } else {
          file.put("ispraise", "false");
        }
        // 判断当前用户时候有订阅该文件的上传者
        boolean hasSubScribeUser = filesDao.hasSubScribeUser(userId, creator.get("userName"));
        returnMap.put("hasSubScribeUser", hasSubScribeUser);
        // 判断当前用户时候有权限
        if (this.hasUserReadRight(companyId, userId, file)) {
          file.put("hasRight", "1");
          returnMap.put("isOwner", userId.equals(file.get("owner_v1")));
          returnMap.put("file", file);
          returnMap.put("creator", creator);
          returnMap.put("owner", owner);
          // 如果是私密文件 当前用户不是此分类的拥有者 也没有权限
          if ("3".equals(file.get("openLevel")) && !userId.equals(file.get("owner_v1"))) {
            returnMap.put("hasRight", "false");
          } else {
            returnMap.put("hasRight", "true");
          }
        } else {
          returnMap.put("isOwner", false);
          returnMap.put("file", file);
          returnMap.put("creator", creator);
          returnMap.put("owner", owner);
          returnMap.put("hasRight", "false");
        }
        List<Map<String, String>> versionList =
            filesDao
                .getFileList4Version(companyId, file.get("classId"), fileName, file.get("type"));
        if (versionList != null && versionList.size() > 1) {
          this.setupFilesCreatorName(companyId, versionList);
          this.setupParentClassName(companyId, versionList);
          // 赋予浏览权限
          setupUserReadRight(companyId, userId, versionList, true);
          // 赞
          setupPraise(companyId, userId, versionList);
          setupClassGroupInfo(companyId, userId, versionList);
          returnMap.put("size", versionList.size());
          returnMap.put("list", versionList);
          returnMap.put("lastFileId", fileId);
        } else {
          returnMap.put("size", 0);
        }
      } else {
        returnMap.put("checkFileDelete", "true");
      }
    }
    writeJson(response, callback, gson.toJson(returnMap));
  }

  /**
   * 检测文件名是否已经存在（同一文件夹下）
   * 
   * @author longjunhao 20150910
   */
  public void checkFileNameExist(HttpServletRequest request, HttpServletResponse response) {
    System.err.println("检查文件是否相同参数：" + StringUtil.getRequerstVal(request));
    String companyId = request.getParameter("companyId");
    String classId = request.getParameter("classId");
    String fileName = request.getParameter("fileName");
    String callback = request.getParameter("callback");
    // 截取文件名和文件后缀
    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
    fileName = fileName.substring(0, fileName.lastIndexOf("."));

    boolean hasExist = filesDao.checkFileNameExist(companyId, classId, fileName, fileType);
    String newFileName = "";
    String name = "";
    int index = 0;
    // 获取新的文件名
    while (hasExist) {
      index++;
      name = fileName + "(" + index + ")";
      hasExist = filesDao.checkFileNameExist(companyId, classId, name, fileType);
    }
    if (!"".equals(name)) {
      newFileName = name + "." + fileType;
    }
    JSONObject json = new JSONObject();
    json.put("newFileName", newFileName);
    json.put("isExist", newFileName.equals("") ? false : true);
    writeJson(response, callback, json.toJSONString());
  }

  public Map<String, Object> getClassStarList(Map<String, Object> param) {
    String companyId = param.get("companyId") + "";
    String userId = param.get("userId") + "";
    String username = param.get("username") + "";
    // xiewenda 查询常用分类是加入公司id
    // List<String> starIds = filesDao.getClassStarIds(userId);
    List<String> starIds = filesDao.getClassStarIds(userId, companyId);
    List<Map<String, String>> classList = filesDao.getClassList(companyId, starIds);

    Map<String, Map<String, String>> groupList =
        filesDao.getCompanyGroups(companyId, username, userId);
    Map<String, String> map = null;
    for (Map<String, String> classMap : classList) {
      map = groupList.get(classMap.get("id"));
      if (map != null) {
        classMap.put("groupId", map.get("id"));
        classMap.put("flag", map.get("flag"));
        classMap.put("remark", map.get("remark"));
      }
    }


    // 20151119 查询每组分类图片
    Map<String, String> classImg = filesDao.getClassImg(companyId);
    Map<String, String> maps = null;
    List<Map<String, String>> groups = new ArrayList<Map<String, String>>();

    for (String map1 : classImg.keySet()) {
      maps = new HashMap<String, String>();
      maps.put("FLAG", map1);
      maps.put("PORTRAITS", classImg.get(map1));
      groups.add(maps);
    }
    List<Map<String, String>> groupss =
        getChatWS().doGroupImageMath(companyId, username, "", groups);
    if (groupss != null)
      for (Map<String, String> strMap : groupss) {
        classImg.put(strMap.get("FLAG"), strMap.get("PORTRAITS"));
      }

    // 添加当前用户所属分类群组的 消息数目、时间等信息
    this.filesDao.getClassExtraInfo(classList, companyId, username, userId);

    // 当前用户关联的分组id列表
    List<String> groupIds = filesDao.getGroupIdsWithUserId(userId);
    for (Map<String, String> classMap : classList) {
      if (groupIds.contains(classMap.get("groupId"))) {
        classMap.put("isMember", "true");
        classMap.put("ordermsgcount", classMap.get("newmessagecount"));
      } else {
        classMap.put("isMember", "false");
        classMap.put("ordermsgcount", "-1"); // 没有权限的排在最后
      }
      if (starIds.contains(classMap.get("id"))) {
        classMap.put("isStar", "true");
      } else {
        classMap.put("isStar", "false");
      }
      classMap.put("PORTRAITS",
          classImg.get(classMap.get("flag")) != null ? classImg.get(classMap.get("flag"))
              : "apps/onlinefile/templates/ESDefault/images/profle.png");
    }
    Map<String, Object> resultMap = new HashMap<String, Object>();
    /** 对分类进行排序 消息数量多的在前面 **/
    Collections.sort(classList, new ClassComparator("ordermsgcount"));
    resultMap.put("classes", classList);
    return resultMap;
  }

  public Map<String, Object> getClassList(Map<String, Object> param) {
    String companyId = param.get("companyId") + "";
    String userId = param.get("userId") + "";
    String username = param.get("username") + "";
    // 从缓存获取
    List<Map<String, String>> classList =
        CacheUtils.getList(this.getCompLocator(), "companyClasses" + companyId);
    if (classList == null || classList.isEmpty()) {
      this.updateCompanyClassesCache(companyId, userId, username);
      classList = CacheUtils.getList(this.getCompLocator(), "companyClasses" + companyId);
    }

    // 20151119 查询每组分类图片
    Map<String, String> classImg = filesDao.getClassImg(companyId);
    Map<String, String> maps = null;
    List<Map<String, String>> groups = new ArrayList<Map<String, String>>();

    for (String map : classImg.keySet()) {
      maps = new HashMap<String, String>();
      maps.put("FLAG", map);
      maps.put("PORTRAITS", classImg.get(map));
      groups.add(maps);
    }
    List<Map<String, String>> groupss =
        getChatWS().doGroupImageMath(companyId, username, "", groups);
    if (groupss != null)
      for (Map<String, String> strMap : groupss) {
        classImg.put(strMap.get("FLAG"), strMap.get("PORTRAITS"));
      }



    List<String> starIds = filesDao.getClassStarIds(userId, companyId);
    for (Map<String, String> tmpMap : classList) {
      if (null != tmpMap.get("id") && starIds.contains(tmpMap.get("id"))) {
        if (null != tmpMap.get("flag")) {
          tmpMap.put("isStar", "true");
        }
      } else {
        tmpMap.put("isStar", "false");
      }
    }

    // 添加当前用户所属分类群组的 消息数目、时间等信息
    this.filesDao.getClassExtraInfo(classList, companyId, username, userId);

    // 当前用户关联的分组id列表
    List<String> groupIds = filesDao.getGroupIdsWithUserId(userId);
    for (Map<String, String> classMap : classList) {
      if (groupIds.contains(classMap.get("groupId"))) {
        classMap.put("isMember", "true");
        classMap.put("ordermsgcount", classMap.get("newmessagecount"));
      } else {
        classMap.put("isMember", "false");
        classMap.put("ordermsgcount", "-1"); // 没有权限的排在最后
      }
      classMap.put("PORTRAITS",
          classImg.get(classMap.get("flag")) != null ? classImg.get(classMap.get("flag"))
              : "apps/onlinefile/templates/ESDefault/images/profle.png");
    }
    Map<String, Object> resultMap = new HashMap<String, Object>();
    /** 对分类进行排序 消息数量多的在前面 **/
    Collections.sort(classList, new ClassComparator("ordermsgcount"));
    resultMap.put("classes", classList);
    return resultMap;
  }

  /**
   * 登录时，初始化左侧分类（常用和普通分类）
   * 
   * @author longjunhao 20150928
   * @param param
   * @return
   */
  public Map<String, Object> initClassList(Map<String, String> param) {
    String companyId = param.get("companyId") + "";
    String userId = param.get("userId") + "";
    String username = param.get("username") + "";

    Map<String, Map<String, String>> groupList =
        filesDao.getCompanyGroups(companyId, username, userId);
    // 当前用户关联的分组id列表
    List<String> groupIds = filesDao.getGroupIdsWithUserId(userId);
    List<String> starIds = filesDao.getClassStarIds(userId, companyId);
    List<Map<String, String>> classStarList = filesDao.getClassList(companyId, starIds);
    List<Map<String, String>> groupss = new ArrayList<Map<String, String>>();
    List<Map<String, String>> cacheGroups =
        CacheUtils.getList(this.getCompLocator(), "classGroupImage");
    Map<String, String> classImg = new HashMap<String, String>();

    if (cacheGroups == null
        || (cacheGroups.size() != groupList.size() || !cacheGroups.contains(groupList))) {

      // 20151119 查询每组分类图片
      classImg = filesDao.getClassImg(companyId);
      Map<String, String> maps = null;
      List<Map<String, String>> groups = new ArrayList<Map<String, String>>();

      for (String map : classImg.keySet()) {
        maps = new HashMap<String, String>();
        maps.put("FLAG", map);
        maps.put("PORTRAITS", classImg.get(map));
        groups.add(maps);
      }
      groupss = getChatWS().doGroupImageMath(companyId, username, "", groups);
      CacheUtils.set(this.getCompLocator(), "classGroupImage", groupss);

    } else {
      groupss = CacheUtils.getList(this.getCompLocator(), "classGroupImage");
    }

    if (groupss != null)
      for (Map<String, String> strMap : groupss) {
        classImg.put(strMap.get("FLAG"), strMap.get("PORTRAITS"));
      }

    // wangwenshuo 20151030获取openfire中的分类离线消息数量 获取失败打印错误，排序功能无效
    Map<String, String> msgCountMap = new HashMap<String, String>();
    try {
      msgCountMap = this.getUserMsgCount(username.replace("@", "\\40"));
    } catch (Exception e) {
      System.out.println("从openfire获取未读消息数量失败");
    }

    /**
     * 1、查询用户常用的分类列表
     */
    Map<String, String> map = null;
    for (Map<String, String> classMap : classStarList) {
      map = groupList.get(classMap.get("id"));
      if (map != null) {
        classMap.put("groupId", map.get("id"));
        classMap.put("flag", map.get("flag"));
        classMap.put("remark", map.get("remark"));
      }
    }

    // 添加当前用户所属分类群组的 消息数目、时间等信息
    this.filesDao.getClassExtraInfo(classStarList, companyId, username, userId);

    for (Map<String, String> classMap : classStarList) {
      if (groupIds.contains(classMap.get("groupId"))) {
        classMap.put("isMember", "true");

        // wangwenshuo 20151030 分类排序
        if (classMap.get("flag") != null) {
          String msgCount = String.valueOf(msgCountMap.get(classMap.get("flag")));
          int mc = "null".equals(msgCount) ? 0 : Integer.parseInt(msgCount);
          int nmc =
              classMap.get("newmessagecount") == null ? 0 : (classMap.get("newmessagecount")
                  .contains("+") ? 99 : Integer.parseInt(classMap.get("newmessagecount")));
          classMap.put("ordermsgcount", String.valueOf(nmc + mc));
        }
      } else {
        classMap.put("isMember", "false");
        classMap.put("ordermsgcount", "-1"); // 没有权限的排在最后
      }
      if (starIds.contains(classMap.get("id"))) {
        classMap.put("isStar", "true");
      } else {
        classMap.put("isStar", "false");
      }
      // 20151119 xiayongcai放置分类图片
      classMap.put("PORTRAITS",
          classImg.get(classMap.get("flag")) != null ? classImg.get(classMap.get("flag"))
              : "apps/onlinefile/templates/ESDefault/images/profle.png");
    }


    /**
     * 2、查询用户分类列表
     */
    // 从缓存获取
    List<Map<String, String>> classList =
        CacheUtils.getList(this.getCompLocator(), "companyClasses" + companyId);
    if (classList == null || classList.isEmpty()) {
      this.updateCompanyClassesCache(companyId, userId, username);
      classList = CacheUtils.getList(this.getCompLocator(), "companyClasses" + companyId);
    }

    for (Map<String, String> tmpMap : classList) {
      if (null != tmpMap.get("id") && starIds.contains(tmpMap.get("id"))) {
        if (null != tmpMap.get("flag")) {
          tmpMap.put("isStar", "true");
        }
      } else {
        tmpMap.put("isStar", "false");
      }
    }

    // 添加当前用户所属分类群组的 消息数目、时间等信息
    this.filesDao.getClassExtraInfo(classList, companyId, username, userId);

    for (Map<String, String> classMap : classList) {
      if (groupIds.contains(classMap.get("groupId"))) {
        classMap.put("isMember", "true");

        // wangwenshuo 20151030 分类排序
        if (classMap.get("flag") != null) {
          String msgCount = String.valueOf(msgCountMap.get(classMap.get("flag")));
          int mc = "null".equals(msgCount) ? 0 : Integer.parseInt(msgCount);
          int nmc =
              classMap.get("newmessagecount") == null ? 0 : (classMap.get("newmessagecount")
                  .contains("+") ? 99 : Integer.parseInt(classMap.get("newmessagecount")));
          classMap.put("ordermsgcount", String.valueOf(nmc + mc));
        }
      } else {
        classMap.put("isMember", "false");
        classMap.put("ordermsgcount", "-1"); // 没有权限的排在最后
      }
      // 20151119 xiayongcai放置分类图片
      classMap.put("PORTRAITS",
          classImg.get(classMap.get("flag")) != null ? classImg.get(classMap.get("flag"))
              : "apps/onlinefile/templates/ESDefault/images/profle.png");
    }

    Map<String, Object> resultMap = new HashMap<String, Object>();
    /** 对分类进行排序 消息数量多的在前面 **/
    Collections.sort(classList, new ClassComparator("ordermsgcount"));
    resultMap.put("classes", classList);
    Collections.sort(classStarList, new ClassComparator("ordermsgcount"));
    resultMap.put("starClasses", classStarList);
    return resultMap;
  }

  /** 文件评论 Start **/



  public Map<String, Object> onSendComment(Map<String, String> params) {
    params.put("portrait", getUserWS().getUserImagePathByUserId(params.get("userId")));
    Map<String, Object> result = new HashMap<String, Object>();
    String adroidFlag = params.get("callback");
    Map<String, Object> userInfoMap = null;
    if(null != adroidFlag && "androidInter".equals(adroidFlag)){
      userInfoMap = initReplyContentMessage(params.get("content"),adroidFlag);
    }else{
      userInfoMap = initReplyContentMessage(params.get("content"),null);
    }
    params.put("content", userInfoMap.get("content").toString());
    result.put("commentLists", this.filesDao.onSendComment(params));
    result.put("toUsersLists", userInfoMap.get("toUsersLists"));
    result.put("replyContent", userInfoMap.get("replyContent"));

    return result;
  }

  public Map<String, Object> onReplyComment(Map<String, String> params) {
    String callback = params.get("callback");
    /** 此处需要解析一下content内容,因为内容是Inputdiv标签组成的 **/
    Map<String, Object> userInfoMap = new HashMap<String, Object>();
    if (callback != null && "androidInter".equals(callback)) {// 手机针对单用户回复
      userInfoMap.put("content", params.get("content"));// 格式@姓名 内容
      Map<String, Object> userMap = new HashMap<String, Object>();
      userMap.put(params.get("toUserId"), getUserWS().getUserNameById(params.get("toUserId")) + ","
          + params.get("toUserFullName"));
      userInfoMap.put("toUsersLists", userMap);
      userInfoMap.put("replyContent", params.get("replyContent"));
      userInfoMap.put("userFullName", params.get("userFullName"));
    } else {
      userInfoMap = initReplyContentMessage(params.get("content"),null);
    }

    Map<String, Object> result = new HashMap<String, Object>();
    params.put("content", userInfoMap.get("content").toString());
    Map<String, Object> replyResultMap = this.filesDao.onReplyComment(params);
    result.put("commentMoreReplyLists", replyResultMap.get("commentMoreReplyLists"));
    result.put("success", replyResultMap.get("success"));
    result.put("toUsersLists", userInfoMap.get("toUsersLists"));
    // result.put("receiverId", userInfoMap.get("toUsersLists"));
    result.put("replyContent", userInfoMap.get("replyContent"));
    result.put("userFullName", userInfoMap.get("userFullName"));
    /** 这里保存了原来的新的评论之后需要刷新所有的改评论的记录，所以要从这获取一次评论回复内容 **/

    return result;
  }

  /**
   * 解析content内容
   * 
   * @param content
   * @return
   */
  private Map<String, Object> initReplyContentMessage(String content,String adroidFlag) {
	    StringBuffer newStr = new StringBuffer();
	    StringBuffer replyContent = new StringBuffer();
	    StringBuffer userFullName = new StringBuffer();
	    String useStr = "";
	    String userId = "";
	    String showName = "";
	    Map<String, Object> result = new HashMap<String, Object>();
	    Map<String, String> toUsersList = new HashMap<String, String>();
	    Set<String> userSet = new LinkedHashSet<String >();
	    while (content.trim().length() > 0) {
	      if (content.trim().startsWith("<input")) {
	        useStr = content.substring(content.indexOf("<input"), content.indexOf("_inputEnd\"") + 10);
	        //newStr.append("@");
	        UserInfo entity = new Gson().fromJson( useStr.substring(useStr.indexOf("attr=\"inputStart_") + 17,useStr.indexOf("_inputEnd\"")), UserInfo.class);
	        userId = entity.getUserId();
	        showName = entity.getShowName();
	        //newStr.append(showName);
	        //newStr.append(" ");
	        userSet.add(showName);
	        userFullName.append(showName);
	        content = content.substring(content.indexOf(">") + 1);
	        toUsersList.put(userId, getUserWS().getUserNameById(userId) + "," + showName);
	      } else if (content.trim().startsWith("<img")) {
	        useStr = content.substring(content.indexOf("f_static_") + 9, content.indexOf(".png"));
	        EmojiUtil eu = EmojiUtil.getInstance();
	        newStr.append("[img###_f_static_" + useStr + ".png_###img]");
	        newStr.append(" ");
	        replyContent.append(eu.getMapKey(useStr) + "");
	        content = content.substring(content.indexOf(">") + 1);
	      }else if(content.trim().indexOf("[")> -1 && null != adroidFlag){  //liuwei 修改安卓端不显示表情
	    	  String subStr = content.substring(content.indexOf("["),content.indexOf("]")+1);
	    	  EmojiUtil eu = EmojiUtil.getInstance();
	    	  useStr = eu.getFaceId(subStr);
	    	  if(null != useStr && !"".equals(useStr)){
	    		  newStr.append(content.substring(0, content.indexOf("[")));
	    		  newStr.append("[img###_f_static_" + useStr + ".png_###img]");
	    		  replyContent.append(eu.getMapKey(useStr) + "");
	    	  }
	    	  newStr.append(" ");
	    	  content = content.substring(content.indexOf("]") + 1);
	      } else {
	        if (content.trim().indexOf("<input") > -1) {
	          useStr =content.substring(content.indexOf("<input"), content.indexOf("_inputEnd\"") + 10);
	          newStr.append(content.substring(0, content.indexOf("<input")));
	          UserInfo entity = new Gson().fromJson(useStr.substring(useStr.indexOf("attr=\"inputStart_") + 17,useStr.indexOf("_inputEnd\"")), UserInfo.class);
	          userId = entity.getUserId();
	          showName = entity.getShowName();
	          //newStr.append(showName);
	          //newStr.append(" ");
	          userSet.add(showName);
	          userFullName.append(showName);
	          content = content.substring(content.indexOf(">") + 1);
	          toUsersList.put(userId, getUserWS().getUserNameById(userId) + "," + showName);
	        } else if (content.trim().indexOf("<img") > -1) {
	          useStr = content.substring(content.indexOf("f_static_") + 9, content.indexOf(".png"));
	          EmojiUtil eu = EmojiUtil.getInstance();
	          newStr.append(content.substring(0, content.indexOf("<img")));
	          newStr.append("[img###_f_static_" + useStr + ".png_###img]");
	          newStr.append(" ");
	          replyContent.append(eu.getMapKey(useStr) + "");
	          content = content.substring(content.indexOf(">") + 1);
	        }else {
	          newStr.append(content);
	          replyContent.append(content);
	          content = "";
	        }
	      }
	    }
	    for(String  user : userSet){
	    	newStr.append("@").append(user);
	    }
	    result.put("content", newStr.toString());
	    result.put("toUsersLists", toUsersList);
	    result.put("replyContent", replyContent.toString());
	    result.put("userFullName", userFullName.toString());
	    return result;
	 }
  

  public Map<String, Object> getFileCommentsListByFileId(Map<String, String> params) {
    Map<String, Object> result = new HashMap<String, Object>();
    List<Map<String, String>> commentMoreReplyLists =
        this.filesDao.getFileCommentsListByFileId(params);
    if (params != null && "androidInter".equals(params.get("callback"))) {
      commentMoreReplyLists = this.returnReplyLists(commentMoreReplyLists);
    }
    result.put("commentLists", commentMoreReplyLists);
    return result;
  }

  /**
   * 安卓返回
   * 
   * 解析content 
   * 
   * @param content
   * @return
   * */
  public List<Map<String, String>> returnReplyLists(List<Map<String, String>> commentMoreReplyLists) {
    String content = "";
    StringBuilder newContent = new StringBuilder();
    String tempContent = "";
    if(!commentMoreReplyLists.isEmpty()){
    	for(Map<String,String> map : commentMoreReplyLists){
    		if(!map.isEmpty()){
    			content = StringUtils.trim(map.get("content"));
    			while(content.length() > 0 && content.indexOf("[img###_") > -1){
    					tempContent = content.substring(0,content.indexOf("[img###_"));
    					String newStr = content.substring(content.indexOf("[img###_") + 8, content.indexOf("_###img]"));
    					newContent.append(tempContent)
    							  .append("<img height='26' width='26' src='templates/onlinefile/images/expression/")
    							  .append(newStr)
    							  .append("'>")
    							  .append(" ");
    					content = content.substring(content.indexOf("_###img]") + 8);
    				}
    			if(content.indexOf("[img###_") < 0){
    				newContent.append(content);
    			}
    		}
    		
    		map.put("content", newContent.toString());
    		newContent.setLength(0);
    	}
    }
    
    /*if(!commentMoreReplyLists.isEmpty()){
    	 for (Map<String, String> map : commentMoreReplyLists) {
    	      if (map != null && map.get("content").length() > 0) {
    	        String data = map.get("content").trim();
    	        while (data.length() > 0) {
    	          if (data.indexOf("[img###_") < 0) {// 如果只有文字，没有表情图片
    	            newDate = data;
    	            break;
    	          } else {
    	            newDate = newDate + data.substring(0, data.indexOf("[img###_"));
    	            dateUse = data.substring(data.indexOf("[img###_") + 8, data.indexOf("_###img]"));
    	            newDate =
    	                newDate
    	                    + "<img height='26' width='26' src='templates/onlinefile/images/expression/"
    	                    + dateUse + "'>";
    	            data = data.substring(data.indexOf("_###img]") + 8);
    	          }
    	        }
    	        map.put("content", newDate);
    	      }
    	  }
    }*/
    return commentMoreReplyLists;
  }

  public Map<String, Object> getMoreReplyComments(Map<String, String> params) {
    Map<String, Object> result = new HashMap<String, Object>();
    List<Map<String, String>> commentMoreReplyLists = this.filesDao.getMoreReplyComments(params);
    String callback = params.get("callback");
    if (callback != null && callback.equals("androidInter")) {
      commentMoreReplyLists = this.returnReplyLists(commentMoreReplyLists);
    }
    // result.put("commentMoreReplyLists", this.filesDao.getMoreReplyComments(params));
    result.put("commentMoreReplyLists", commentMoreReplyLists);
    return result;
  }

  @Override
  public Map<String, String> updateFileCommentsUserHeadImg(Map<String, String> params) {
    return this.filesDao.updateFileCommentsUserHeadImg(params);
  }


  public void getFileInfoById(HttpServletRequest request, HttpServletResponse response) {
    String fileId = request.getParameter("fileId");
    String callback = request.getParameter("callback");
    String companyId = request.getParameter("companyId");
    String fromUserFullName = request.getParameter("fromUserFullName");
    try {
    	fromUserFullName = URLDecoder.decode(fromUserFullName, "utf-8");
	} catch (UnsupportedEncodingException e1) {
		e1.printStackTrace();
	}
    String fromUserId = request.getParameter("fromUserId");
    String parentId = request.getParameter("parentId");
    Map<String, String> fileInfoMap = this.filesDao.getFileInfoById(companyId, fileId);
    Map<String, Object> res = new HashMap<String, Object>();
    fileInfoMap.put("commentUserId", fromUserId);
    fileInfoMap.put("commentUserFullName", fromUserFullName);
    fileInfoMap.put("commentParentId", parentId);
    res.put("file", fileInfoMap);
    writeJson(response, callback, gson.toJson(res));
  }

  public void getCommentMembersByUserId(HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("userId", request.getParameter("userId"));
    map.put("userName", request.getParameter("userName"));
    map.put("companyId", request.getParameter("companyId"));
    map.put("groupId", request.getParameter("groupId"));
    Map<String, Object> result = filesDao.getCommentMembersByUserId(map);
    writeJson(response, request.getParameter("callback"), gson.toJson(result));

  }

  public void queryMembersBykeyWord(HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> params = new HashMap<String,String>();
    params.put("userId", request.getParameter("userId"));
    params.put("userName", request.getParameter("userName"));
    params.put("companyId", request.getParameter("companyId"));
    params.put("keyWord", request.getParameter("keyWord"));
    Map<String, Object> result = filesDao.queryMembersBykeyWord(params);
    writeJson(response, request.getParameter("callback"), gson.toJson(result));
  }


  /** 文件评论 End **/

  /**
   * wangwenshuo 20151019 分类排序
   */
  private class ClassComparator implements Comparator<Map<String, String>> {
    private String orderField = "newmessagecount"; // 默认根据消息数量排序

    public ClassComparator() {}

    /*
     * 传入排序的字段 值为整型
     */
    public ClassComparator(String orderField) { // 登录后 消息数量来源于openfire与mongodb 使用自定义字段排序
      this.orderField = orderField;
    }

    @Override
    public int compare(Map<String, String> m1, Map<String, String> m2) {
      String mc1 = m1.get(orderField);
      String mc2 = m2.get(orderField);
      int c1 = Integer.parseInt(mc1 == null ? "0" : (mc1.contains("+") ? "99" : mc1));
      int c2 = Integer.parseInt(mc2 == null ? "0" : (mc2.contains("+") ? "99" : mc2));
      return c1 > c2 ? -1 : (c1 < c2 ? 1 : 0);
    }
  }

  @Override
  public Map<String, Object> setFileOpenlevel(Map<String, String> param) {
    String companyId = param.get("companyId");
    String classId = param.get("classId");
    String fileId = param.get("fileId");
    String userId = param.get("userId");
    String userName = param.get("username");
    String fileName = param.get("fileName");
    String fileType = param.get("fileType");
    String remoteAddr = param.get("remoteAddr");
    String openlevel = param.get("openlevel");
    String rename = null;
    /*
     * if("3".equals(openlevel)){ //查找是否存在同名的文件 Map<String,String> renamefile =
     * filesDao.getRenameFile(param); rename = renamefile.get("fileName"); if(rename !=null &&
     * rename.equals(fileName)){ rename = null; } }
     */
    /**
     * xyc 设置私密后，以前分享鍀文件都清除包括（分类、个人，关系到本文件）。 那么私密后，用户再分享鍀文件，没有办法控制，依旧还是可以下载、查看。
     * */
    if (openlevel != null && "3".equals(openlevel)) {
      filesDao.setPrivacyAnnulShare(fileId);
    }
    // 设为私密时 把所有历史版本设为私密
    Map<String, String> fileInfo = filesDao.getFileById(companyId, classId, fileId);
    // 获得所有版本
    List<Map<String, String>> versionList =
        filesDao.getFileList4Version(companyId, classId, fileInfo.get("filename"),
            fileInfo.get("type"));
    boolean success = false;
    if (versionList.size() > 0) {
      for (int i = 0; i < versionList.size(); i++) {
        Map<String, String> m = versionList.get(i);
        if (filesDao.setFileOpenlevel(companyId, m.get("id"), openlevel, rename)) {
          success = true;
        } else {
          success = false;
          break;
        }
        // 设为私密后以下值修改 用于后续更新索引
        // if(rename!=null) m.put("reName", rename);
        m.put("openlevel", openlevel);
        m.put("companyId", companyId);
      }
    }
    if (success) {
      getLuceneWS().updateIndex4Unshare(versionList);
      // 日志------------------------------------
      Map<String, Map<String, String>> companyUserInitInfo = null;
      Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
      String companyName = "";
      if (obj != null) {
        companyUserInitInfo = (Map<String, Map<String, String>>) obj;
        companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
      }
      HashMap<String, String> log = new HashMap<String, String>();
      log.put("ip", remoteAddr);
      log.put("userid", userName);
      log.put("loginfo", "设置公开级别【" + fileId + "】");
      log.put("module", "文件");
      log.put("username", userName);
      log.put("companyName", !"".equals(companyName) ? companyName : companyId);
      // login 用户登录 access 功能访问 operation 功能操作
      log.put("type", "operation");
      log.put("operate", "所有文档/我的文档");
      Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
      if (null != felFileMap.get("openLevel")) {
        log.put("fileId", fileId + "");
        log.put("folderId", felFileMap.get("folderId") + "");
        log.put(
            "fileSubscribMsg",
            "设置文件【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
                + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>公开级别为【"
                + ("3".equals(openlevel) ? "私密" : ("1".equals(openlevel) ? "公司公开" : "分类公开")) + "】");
      }
      log.put("companyId", companyId);
      LogUtils.saveBaseLog(compLocator, log);
      // 日志------------------------------------
    }
    Map<String, String> groupMap = filesDao.getFlagWithClassId(companyId, classId);
    Map<String, Object> rst = new HashMap<String, Object>();
    rst.put("groupFlag", groupMap.get("FLAG"));
    rst.put("GROUPNAME", groupMap.get("GROUPNAME"));
    rst.put("success", success);
    return rst;
  }

  /**
   * 20151023 xiayongcai 验证文件是否删除或未公开 返回 success :true |false msg
   * */
  public Map<String, String> checkFileDeleteAndOpenLevel(Map<String, String> params) {
    Map<String, String> result = null;
    Map<String, String> reMap = new HashMap<String, String>();
    String success = "false";
    String msg = "";
    result = filesDao.getFileInfoById(params.get("companyId"), params.get("fileId"));
    // 标识文件被删除
    if (result != null && "1".equals(result.get("isDelete"))) {
      success = "true";
      msg = "对不起，文件已被删除，无法查看！";
    }
    if (result != null && "3".equals(result.get("openLevel"))
        && !"1".equals(result.get("isDelete"))) {
      success = "true";
      msg = "对不起，文件已取消分享，无法查看！";
    }
    // 如果验证成功
    reMap.put("success", success);
    reMap.put("msg", msg);
    return reMap;
  }

  /**
   * wangwenshuo 20151030获取分类消息数量
   * 
   * @param username 替换掉@的username
   */
  private Map<String, String> getUserMsgCount(String username) {
    Map<String, String> map = new HashMap<String, String>();
    String mag = post("type=getusermsgcount&secret=flyingsoft&username=" + username);
    if (mag != null) {
      map = (Map<String, String>) JSON.parse(mag);
    }
    return map;
  }

  /**
   * wangwenshuo 20151030 连接openfire userservice接口
   */
  private String post(String arg) {
    String mag = null;
    try {
      URL oUrl =
          new URL("http://" + BroadcastUtils.openfireIp + ":" + BroadcastUtils.openfireServerPort
              + "/plugins/userService/userservice");
      // 打开和URL之间的连接
      URLConnection conn = oUrl.openConnection();
      // 设置通用的请求属性
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent",
          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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
      if (null != oIn) {
        mag = oIn.readLine();
        oIn.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mag;
  }

  /**
   * wangwenshuo 201511117 我的文档文件分享 20151130 我的文档分享到聊天窗口 分享目标用户保存到我的文档同样使用本方法
   */
  public Map<String, String> fileShareToClass(Map<String, String> params) {
    Map<String, String> map = new HashMap<String, String>();
    String fromCompanyId = params.get("fromCompanyId");
    String fromFileId = params.get("fromFileId");
    String toCompanyId = params.get("toCompanyId");
    String toFolderIds = params.get("toFolderIds");
    String toFolderNames = params.get("toFolderNames");
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");
    String userId = params.get("userId");
    String[] toFolderIdArray = toFolderIds.split(",");
    String[] toFolderNameArray = toFolderNames.split(",");
    String fileIds = "";
    for (int i = 0; i < toFolderIdArray.length; i++) {
      String toFolderId = toFolderIdArray[i];
      String toFolderName = toFolderNameArray[i];
      // 获取folder的idseq
      String folderIdSeq = filesDao.getIdSeqWithId(toCompanyId, toFolderId);
      String fileIdSeq = folderIdSeq + toFolderId + ".";

      // 获取源文件的文件信息
      Map<String, String> fileInfo = filesDao.getFileById(fromCompanyId, null, fromFileId);
      fileInfo.put("id", fromFileId);
      int rstId = 0; // 返回的ID
      // 插入新版本前 获取当前最新文件版本
      List<Map<String, String>> files =
          filesDao.getFileList4Version(toCompanyId, toFolderId, fileInfo.get("filename"),
              fileInfo.get("type"));
      try {
        if (files.size() > 0) {
          filesDao.updateFileIsLast(toCompanyId, files.get(0).get("id"), "0");
          getLuceneWS().updateIndexOfisLast(files, toCompanyId);
          fileInfo
              .put("version", String.valueOf(Integer.parseInt(files.get(0).get("version")) + 1)); // 版本号+1
        }

        // openlevel 1公司级 2分类级 3私密级
        rstId =
            filesDao.fileShareToClass(fromCompanyId, toCompanyId, fileInfo,
                fileInfo.get("filename"), toFolderId, fileIdSeq, "2", userId);

        boolean logRst = true;
        // 目标公司是user_N 说明是保存到我的文档操作 不记录日志
        if (!toCompanyId.contains("user_")) {
          logRst =
              filesDao.fileShareToClassLog(fromCompanyId, toCompanyId, fileInfo,
                  fileInfo.get("filename"), toFolderId, fileIdSeq, "2");
        }

        if (rstId > 0 && logRst) {
          // 为复制的文件建立索引
          Map<String, String> fileMap =
              filesDao.getFileInfo(toCompanyId, String.valueOf(rstId), null);
          Map<String, String> createLuceneIndexParams = new HashMap<String, String>();
          createLuceneIndexParams.put("ID", fileMap.get("id"));
          createLuceneIndexParams.put("COMPANYID", toCompanyId);
          /*
           * createLuceneIndexParams.put("fileId", fileMap.get("fileId"));
           * createLuceneIndexParams.put("classId", fileMap.get("classId"));
           * createLuceneIndexParams.put("fileName",fileInfo.get("filename") );
           * createLuceneIndexParams.put("fileType", fileMap.get("type"));
           * createLuceneIndexParams.put("version", fileMap.get("version"));
           * createLuceneIndexParams.put("insertId", fileMap.get("id"));
           * createLuceneIndexParams.put("idSeq", fileMap.get("idSeqSrc") );
           * createLuceneIndexParams.put("openlevel", "2"); createLuceneIndexParams.put("size",
           * fileMap.get("sizeNum")); createLuceneIndexParams.put("createtime",
           * fileMap.get("createTime")); createLuceneIndexParams.put("createrId",
           * fileMap.get("userId") ); createLuceneIndexParams.put("ip", remoteAddr);
           * createLuceneIndexParams.put("userid", fileMap.get("userId") );
           * createLuceneIndexParams.put("username", userName );
           * createLuceneIndexParams.put("companyName", "");//记log用的，暂时先不传了
           * createLuceneIndexParams.put("isFile", fileMap.get("isFile"));
           * createLuceneIndexParams.put("isDel", fileMap.get("isDelete"));
           * createLuceneIndexParams.put("isLast", fileMap.get("isLast"));
           * createLuceneIndexParams.put("md5", fileMap.get("md5"));
           */
          getLuceneWS().createIndex(createLuceneIndexParams);

          fileIds = fileIds + rstId + ",";
          map.put("haveSuccess", "true");
          if (null == map.get("successFolderNames")) {
            map.put("successFolderNames", toFolderName);
          } else {
            map.put("successFolderNames", map.get("successFolderNames") + "," + toFolderName);
          }
        } else {
          map.put("haveFail", "true");
          if (null == map.get("failFolderNames")) {
            map.put("failFolderNames", toFolderName);
          } else {
            map.put("failFolderNames", map.get("failFolderNames") + "," + toFolderName);
          }
        }
      } catch (Exception e) {
        map.put("haveFail", "true");
        if (null == map.get("failFolderNames")) {
          map.put("failFolderNames", toFolderName);
        } else {
          map.put("failFolderNames", map.get("failFolderNames") + "," + toFolderName);
        }
      }
    }
    // 分享成功的fileid
    map.put("fileIds", fileIds.length() > 0 ? fileIds.substring(0, fileIds.length() - 1) : "");
    return map;
  }

  @Override
  public Map<String, Object> searchFile(Map<String, String> params) {
    String companyId = params.get("companyId");
    String loginUserId = params.get("loginUserId");
    String idSeq = params.get("idSeq");
    String query = params.get("query");
    String pageStr = params.get("page");
    String pageSizeStr = params.get("pageSize");
    String userName = params.get("userName");
    String className = params.get("className");
    String orderField = params.get("orderField");
    String orderType = params.get("orderType");
    String type = params.get("type");
    if (orderField == null || orderField.trim().length() == 0) {
      orderType = null;
    }
    String searchCompanyId = companyId;
    if("myDocument".equals(type)){
       searchCompanyId = "user_"+loginUserId;
    }
    int page = Integer.parseInt(pageStr); // 1 2 3 4..
    int limit = Integer.parseInt(pageSizeStr);
    int start = (page - 1) * limit;

    Map<String, String> searchMap = new HashMap<String, String>();
    searchMap.put("keyWord", query);
    searchMap.put("companyId", searchCompanyId);
    searchMap.put("start", start + "");
    searchMap.put("limit", limit + "");
    searchMap.put("idSeq", idSeq);
    searchMap.put("searchType", "1");// 1表示搜索文件
    searchMap.put("loginUserId", loginUserId);
    searchMap.put("orderField", orderField);
    searchMap.put("orderType", orderType);
    searchMap.put("type", type);
    List<Map<String, String>> files = getLuceneWS().search(searchMap);
    int count = 0;
    if (files.size() > 0) {
      count = Integer.parseInt(files.get(0).get("total"));
      files.remove(0);
    }
    if (count > 0) {
      setFilePath(searchCompanyId, files);

      // setupParentClassName(companyId, files);
      // setupFilesCreatorName(companyId, files);
      // 赋予浏览权限
      // setupUserReadRight(companyId, loginUserId, files, false);
      // setupPraise(companyId, loginUserId, files);
      // setupClassGroupInfo(companyId, loginUserId, files);
    }
    int totalPage = (count + limit - 1) / limit;
    Map result = new HashMap();
    result.put("files", files);
    result.put("size", files.size());
    result.put("total", totalPage);
    result.put("page", page);
    result.put("count", count);
    result.put("hidePageMenu", "true");
    // 日志------------------------------------
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", params.get("remoteAddr"));
    log.put("userid", loginUserId);
    log.put("loginfo", "检索【" + className + "】分类下的文件,关键字为【" + query + "】");
    log.put("module", "文件检索");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "文档搜索");
    LogUtils.saveBaseLog(compLocator, log);
    // 日志------------------------------------
    return result;
  }

  private void setFilePath(String companyId, List<Map<String, String>> files) {
    for (Map<String, String> file : files) {
      String idseq = file.get("idSeqSrc");
      List<Map<String, String>> filePaths = filesDao.getClassByIdseq(idseq, companyId);
      String folderPath = "";
      for (Map<String, String> path : filePaths) {
        if ("1".equals(path.get("id")))
          continue;
        folderPath += path.get("name") + "/";
      }
      file.put("folderPath", folderPath);
    }
  }

  public Map<String, String> dragFileToDocumnet(Map<String, String> params) {
    Map<String, String> mapStr = new HashMap<String, String>();
    // 查询当前分类最大编号
    String tableName = "files_" + params.get("companyId");
    String sourceFileTitle = params.get("sourceFileTitle");
    // String fileName=sourceFileTitle.substring(0, sourceFileTitle.indexOf("."));
    // String fileType=sourceFileTitle.substring(sourceFileTitle.indexOf(".")+1,
    // sourceFileTitle.length());
    // String sourcesoleNumberIsType=(params.get("sourcesoleNumber")!=null &&
    // !"".equals(params.get("sourcesoleNumber").trim()))?"<>":"=";


    // xyc 拖拽需要修改文件的更新时间
    String updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    if (null != params.get("isMyDocmentFlag") && params.get("isMyDocmentFlag").equals("true")) {
      tableName = "files_user_" + params.get("userId");
    }
    Map<String, String> fileMap = filesDao.getFileById(tableName, params.get("sourceFileId"));

    if (fileMap != null && fileMap.size() > 0) {// 文件不存在
      Map<String, String> newFileMap =
          filesDao.getFileHighestVersionMap(tableName, params.get("targetDocId"),
              fileMap.get("fileName"), fileMap.get("fileType"), updateTime);
      // 存在的情况下
      params.put("FILENAME", newFileMap.get("FILENAME"));
      params.put("TYPE", newFileMap.get("TYPE"));
      params.put("UPDATETIME", newFileMap.get("UPDATETIME"));
      params.put("SERIALNUMBER", newFileMap.get("SERIALNUMBER"));
      params.put("SOLENUMBER", newFileMap.get("SOLENUMBER"));
      params.put("VERSION", newFileMap.get("VERSION"));
      params.put("tableName", tableName);
      List<String> ids = filesDao.getOneFileExistList(tableName, fileMap.get("classId"), fileMap.get("soleNumber"));
      mapStr = filesDao.dragFileToDocumnet2(params, ids);

      String classSerialNumber =
          filesDao.getClassHighestSerialNumber(tableName, fileMap.get("classId"),
              fileMap.get("isFile"));
      if (!classSerialNumber.equals(params.get("sourceserialNumber"))) {// 拖动的不是最高值,
        // 进行更新序号,根据编号大小
        filesDao.updateClassFileOrFolderSerialnumber(tableName, fileMap.get("classId"),
            fileMap.get("isFile"));
      }
    }



    return mapStr;
  }


  /** lujixiang 20151210 新增文件删除,并更新版本号 **/
  public String deleteFileAndUpdateVersion(Map<String, String> params) {

    String companyId = params.get("companyId");
    String fileId = params.get("fileId");
    String userId = params.get("userId");

    Map<String, String> rstMap = new HashMap<String, String>();

    if (companyId == null || "".equals(companyId) || fileId == null || "".equals(fileId)) {

      rstMap.put("success", "false");
      rstMap.put("msg", "参数为空,无法删除");
      return gson.toJson(rstMap);
    }

    /** 检测删除的权限 ： 私密文件，V1Owner能够删除 非私密文件 Owner能够删除 **/
    Map<String, String> file = filesDao.getFileInfoById(companyId, fileId);

    String openlevel = file.get("openLevel");
    Map<String, String> fileV1 =
        filesDao.getFileFirstVersion(companyId, file.get("classId"), file.get("fileName"),
            file.get("fileType"), file.get("soleNumber"));
    if (!("3".equals(openlevel) && userId.equals(fileV1.get("owner")))
        && !(!"3".equals(openlevel) && userId.equals(file.get("owner")))) {
      rstMap.put("success", "false");
      rstMap.put("msg", "非拥有者无权限删除");
      return gson.toJson(rstMap);
    }
    /** 检测删除的权限 ： 私密文件，V1Owner能够删除 非私密文件 Owner能够删除 **/

    boolean flag = false; // 调用存储过程是否成功

    /** 针对某一文档添加同步锁 **/
    String lockKey = companyId + file.get("idseq") + file.get("md5");
    try {

      LockUtil.acquire(lockKey);
      /** 调用存储过程删除文件,并更新版本号 **/
      flag = filesDao.deleteFileAndUpdateVersion(companyId, fileId, userId);
      LockUtil.release(lockKey);

    } catch (InterruptedException e) {
      rstMap.put("success", "false");
      rstMap.put("msg", "获取同步锁异常");
      return gson.toJson(rstMap);
    }

    if (!flag) {
      rstMap.put("success", "false");
      rstMap.put("msg", "调用存储过程异常,无法删除文件");
      return gson.toJson(rstMap);
    }

    // 日志------------------------------------
    String userName = params.get("userName");
    String remoteAddr = params.get("remoteAddr");
    Map<String, Map<String, String>> companyUserInitInfo = null;
    Object obj = CacheUtils.get(this.getCompLocator(), "companyUserInitInfo" + companyId);
    String companyName = "";
    if (obj != null) {
      companyUserInitInfo = (Map<String, Map<String, String>>) obj;
      companyName = companyUserInitInfo.get(userName).get("COMPANYNAME");
    }
    HashMap<String, String> log = new HashMap<String, String>();
    log.put("ip", remoteAddr);
    log.put("userid", userName);
    log.put("loginfo", "删除文件【" + fileId + "】");
    log.put("module", "文件");
    log.put("username", userName);
    log.put("companyName", !"".equals(companyName) ? companyName : companyId);
    // login 用户登录 access 功能访问 operation 功能操作
    log.put("type", "operation");
    log.put("operate", "所有文档/我的文档");
    Map<String, String> felFileMap = filesDao.getFileInfoById(companyId, fileId);
    if (null != felFileMap.get("openLevel") && !felFileMap.get("openLevel").equals("3")) {
      log.put("fileId", fileId + "");
      log.put("folderId", felFileMap.get("folderId") + "");
      Map<String, String> userFullNameMaps =
          filesDao.getUserFullNameByCompanyId(params.get("companyId"));
      log.put("fileSubscribMsg",
          "删除了【" + felFileMap.get("fileName") + "." + felFileMap.get("fileType")
              + "】<span class ='version'>第" + felFileMap.get("version") + "版</span>");
    }
    log.put("companyId", companyId);
    LogUtils.saveBaseLog(compLocator, log);
    // 日志------------------------------------

    rstMap.put("success", "true");
    rstMap.put("msg", "文件删除成功");
    rstMap.put("isLast", file.get("isLast"));
    return gson.toJson(rstMap);

  }


  /**
   * wangwenshuo 20151222 批量检测某一文件夹下文件名是否存在
   * 
   * @param request
   * @param response
   */
  public void checkFileNamesExist(@Context HttpServletRequest request,
      @Context HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String classId = request.getParameter("classId");
    String fileNamesStr = request.getParameter("fileNames");
    String callback = request.getParameter("callback");
    try {
      fileNamesStr = URLDecoder.decode(fileNamesStr, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    String[] fileNames = fileNamesStr.split("\\|");
    Map<String, Object> map = new HashMap<String, Object>();
    List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
    for (String fileName : fileNames) {
      // 截取文件名和文件后缀
      String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
      String fn = fileName.substring(0, fileName.lastIndexOf("."));
      boolean isExist = filesDao.checkFileNameExist(companyId, classId, fn, fileType);
      Map<String, String> fileMap = new HashMap<String, String>();
      fileMap.put("fileName", fileName);
      fileMap.put("isExist", isExist + "");
      if (isExist) {
        map.put("isExist", true);
      }
      fileList.add(fileMap);
    }
    map.put("files", fileList);
    writeJson(response, callback, gson.toJson(map));
  }


  /**
   * xiayongcai 20151224 批量检测某一文件夹下文件名是否存在 如果存在的情况下， 就根据已经存在的文件分类级别展开设置公开级别， 私密文件只能通过修改名称才能上传。
   * 
   * @param request
   * @param response -- 修改上面的接口
   */
  public void checkClassFileNamesIsExist(@Context HttpServletRequest request,
      @Context HttpServletResponse response) {
    String companyId = request.getParameter("companyId");
    String classId = request.getParameter("classId");
    String fileNamesStr = request.getParameter("fileNames");
    String callback = request.getParameter("callback");
    try {
      fileNamesStr = URLDecoder.decode(fileNamesStr, "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    String[] fileNames = fileNamesStr.split("\\|");
    Map<String, Object> map = new HashMap<String, Object>();
    List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
    // 查询当前分类下最高版本好的所有文件
    List<Map<String, String>> returnFilesList =
        filesDao.getFileOrFolderVersionList("files_" + companyId, classId, "asc", "1");
    boolean isExist = false;
	for (String fileName : fileNames) {
		Map<String, String> fileMap = new HashMap<String, String>();
		if(fileName.lastIndexOf(".")==-1){  //上传的文件没有后缀名 不能上传
			isExist = true;
			fileMap.put("extentions", "false");  //无后缀名
		}else{
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
			String fn = fileName.substring(0, fileName.lastIndexOf("."));
			if (returnFilesList != null && returnFilesList.size() > 0 && fileNames != null) {
				for (Map<String, String> filesMap : returnFilesList) {
					// 截取文件名和文件后缀
					if (fn.equals(filesMap.get("FILENAME")) && fileType.toLowerCase().equals(filesMap.get("TYPE"))) {
						fileMap.put("openLevel", filesMap.get("OPENLEVEL"));// 文件的公开级别
						fileMap.put("owner", filesMap.get("OWNER"));// 文件拥有者
						isExist = true;
						break;
					} else {
						isExist = false;
					}
				}
			}
		}
		fileMap.put("fileName", fileName);
		fileMap.put("isExist", isExist + "");
		if (isExist) {
			map.put("isExist", true);
		}
		fileList.add(fileMap);
	}
    map.put("files", fileList);
    writeJson(response, callback, gson.toJson(map));
  }



  /**
   * xyc 获取当前分类最大SERIALNUMBER
   * 
   * @param params
   * @return DocumentClass在用
   */
  public String getClassHighestSerialNumber(String tableName, String classId, String isFile) {
    return filesDao.getClassHighestSerialNumber(tableName, classId, isFile);
  }


  /**
   * xyc 获取文件详情
   * 
   * @param params
   * @return DocumentClass在用
   */
  public Map<String, String> getFileById(String tableName, String fileId) {
    return filesDao.getFileById(tableName, fileId);
  }

  /**
   * xyc 更新当前分类的序号
   * 
   * @param 根据文件名，当前class，文件或文件夹
   * @return DocumentClass在用
   */
  public String updateClassFileOrFolderSerialnumber(String tableName, String classId, String isFile) {
    return filesDao.updateClassFileOrFolderSerialnumber(tableName, classId, isFile);
  }

  /**
   * xiayongcai 更新文件和文件夹拥有者
   * 
   * @param groupsOwnerId：拥有分类的id data_idseq：所在分类的idseq userIds：用户 companyId：企业id
   */
  public boolean updateGroupsFileAndFolderOwner(Map<String, String> params) {
    // data-idseq 所在分类data_idseq
    // owner 分类拥有者。groupsOwnerId
    String groupsOwnerId = params.get("groupsOwnerId");
    String idSeq = params.get("data_idseq");
    String userids = params.get("userIds");
    String tableName = "files_" + params.get("companyId");
    List<String> useridList = new ArrayList<String>();
    if (userids == null) {
      return true;
    }
    if (userids != null && userids.length() > 0) {
      String[] userid = userids.split(",");
      for (String id : userid) {
        useridList.add(id);
      }
    }
    return filesDao.updateGroupsFileAndFolderOwner(tableName, groupsOwnerId, useridList, idSeq);
  }

  @Override
  public Map<String, String> getFileForIndex(String companyId, String fileId) {
    return filesDao.getFileForIndex(companyId, fileId);
  }

}
