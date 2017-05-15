package cn.flying.rest.onlinefile.chat.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.flying.rest.onlinefile.utils.BaseDao;

@SuppressWarnings("rawtypes")
public interface ChatDao extends BaseDao {

	public LinkedHashMap<String, List<HashMap<String, String>>> getOneCompanyUsers(String companyId) ;

	public List<HashMap<String, String>> getGroupByGroupId(String userName ,String GroupName, String companyId);
	
	public List<HashMap<String, String>> getGroupsByUsername(String companyId, String username);
	//获取分类、群组头像
	public List<HashMap<String, String>> getGroupsAndClassByUsername(String companyId, String username);
	//根据公司ID，获取公司分组内的所有用户头像
	public HashMap<String, String> getGroupsByCompanyIdsImgMap(String companyId);
	//获取群组信息
	public Map<String, String> getGroupDetail(String companyId,String groupFlag);

	public Long saveNotSeeMessage(String companyId, String username, String content, String date, String time, String from, String isGroup, String groupFlag, String fromCnName);
	
	public boolean dropNotSeeMeesage(String companyId, String from, String username, long notSeeMsgId);

	public HashMap<String, String> getNotSeeMessageCount(String companyId, String username);

	public List<HashMap<String, String>> getOldNotSeeMessage(String companyId, String receiver, String username, String isGroup);

	/** 这里进行更改  将Boolean类型转换成String类型 (这里的字符串包含两部分 ①返回的一个ID ②状态[true/false]) */
	public String createGroup(String companyId, String username, String groupuserids, String manageruserid, String groupname, String groupremark, String groupflag, String time);

	public boolean saveHistoryMessage(String companyId, String username, String content, String date, String time, String from, String isGroup, String fromCnName, String styleTpl, String fileFlag,Map<String, Map<String, String>> companyUserInitInfo);
	
	public String saveHistoryMessageReturnID(String companyId, String username, String content, String date, String time, String from, String isGroup, String fromCnName, String styleTpl, String fileFlag,Map<String, Map<String, String>> companyUserInitInfo);

	public boolean initMessageTable(Map<String,String> map);
	
	public List<HashMap<String, String>> getHistoryMessage(String companyId, String receiver, String username, String isGroup, String limit, String page, String skip, String keyword);
	
	public List<HashMap<String, String>> getHistoryMessage4File(String companyId, String receiver, String fileFlag, String limit, String page, String skip);

	public List<String> getOneGroupUserIds(String companyId, String username, String groupFlag);
	
	//20151111查看单个群组用户
	public List<Map<String, String>> getOneGroupUser(String companyId,String groupFlag);
	
	public List<HashMap<String, String>> getOneGroupUsers(String companyId, String username, String groupFlag);

	public boolean deleteGroup(String companyId, String groupflag, String groupid);

	public boolean outGroup(String companyId, String groupid, String userid);

	public List<String> getGroupUsersByGroupId(String groupid);

	public boolean resetGroup(String companyId, String username,
			String addgroupuserids, String deletegroupuserids, 
			String manageruserid, String groupname, String groupremark,
			String groupflag, String time, String groupid, String changeusers,
			String changeitems);

	public boolean saveGroupcallOver(String companyId, String groupflag,
			String users);

	public String getFullNamesByIds(String addgroupuserids);
	
	public List<String> getUserNameByIds(String deletegroupuserids);

	/**
	 * 获得flag  
	 * @param classId
	 * @param companyId 
	 * @return
	 */
	public Map<String, String> getGroupFlagByClassId(String classId, String companyId);
	/**
	 * 获得当前分类下，已被邀请未激活的用户
	 * @param companyId
	 * @param classId
	 * @return 
	 */
	public List<Map<String, String>> getGroupUsersByNotJoin(String companyId, String classId);
	/**
     * 获取某个群组管理员的信息(申请加入分组时，给管理员发送消息 )
	 * @param companyId 
     * @param request
     * @param response
	 * @return 
     */
	public Map<String, Object> getGroupUserIsAdminInfo(String groupid, String groupflag, String companyId);
	/**
     * 分组移交
	 * @param groupid 
     * @param request
     * @param response
     */
	public boolean changeGroupAdmin(String companyId, String username,
			String userid, String tousername, String touserid, String groupid);
	/**
     * 分享/取消分享
	 * @param groupid 
     * @param request
     * @param response
     */
	public boolean UpdateFileToShareOrNoShare(String msgid, String button,String companyId);
	
	/**
     * 发表文件的评论
     * @param data  ( companyId/fileFlag/version/userId/content/userName/remoteAddr )
     * @return
     */
    public boolean newFileComment(Map<String, String> data);
    
    /**
     * 删除文件的评论
     * @return
     */
    public boolean deleteComment(Map<String, String> data);
    
    /**
     * 获取文件评论列表
     */
    public List<Map<String, String>> getFileCommentList(String companyId, String fileFlag, String version, String limit, String page, String skip);

    /**
     * 将公司管理员加入到被禁用用户创建的群组中
     * @param userId 管理员ID
     * @param userIdStr 被禁用的用户ids，格式为:1,2,3
     * @return
     */
	public List<Map<String, String>> addAdminIntoGroups(String userId, String userIdStr, String companyId);
	
	/**
	 * 添加用户到群组中
	 * @param userId  被添加的用户
	 * @param groupId  群组
	 * @return
	 */
	public boolean addUserIntoGroup(String userId, String groupId);

	/**
	 * 获取文件评论列表根据多个id
	 * @param 
	 * @param 
	 * @return
	 */
	public List<Map<String, String>> getFileCommentListByVersions(
			String companyId, String fileFlag, String version, String limit,
			String page, String skip);
	/**
	 * 删除被踢用户的消息
	 * @param companyId
	 * @param groupflag
	 * @param usernames
	 * @param usernames2 
	 */
	public boolean dropKickoutUserMessage(String companyId, String groupflag,List<String> usernames);
	
	/**
	 * lujixiang 20151104   检查用户是否已在分类下
	 * @param userId 用户id
	 * @param groupId 分类id
	 */
	public boolean checkExistUserIntoGroup(String userId, String groupId);
	
	
	/**
	 * @param companyId
	 * 获取企业所有人员
	 */
	public List<Map<String, String>> getUserImgByCompanyId(String companyId);
	
	
}
