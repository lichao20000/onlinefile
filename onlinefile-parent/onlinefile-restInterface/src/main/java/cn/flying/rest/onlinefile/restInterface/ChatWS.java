package cn.flying.rest.onlinefile.restInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import cn.flying.rest.platform.utils.MediaTypeEx;

/**
 * @author xiaoxiong 20150227 
 * 聊天REST服务接口
 */
public interface ChatWS {
	
	/**
	 * 根据企业id获取当前用户所在公司的所有用户列表-聊天室获取用户列表专用
	 * @param request
	 * @param response
	 */
	@GET
	@Path("getCompanyUsers")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getCompanyUsers(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 根据企业id获取当前用户所在公司的所有用户列表-聊天室获取用户列表专用
	 * @param requestonlinefile-androidInter
	 * @param response
	 */
	@GET
	@Path("getCompanyUsersList")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getCompanyUsersList(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 根据企业id获取当前用户所在公司的所有用户列表-群组创建+编辑专用
	 * @param companyId
	 * @param username
	 * @param request
	 * @param response
	 */
	@POST
    @Path("getCompanyUsersForGroupSet")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map getCompanyUsersForGroupSet(Map<String, String> params);
	
	/**
	 * 根据企业id获取当前用户所在公司的所有用户列表-群组创建+编辑专用
	 * @param companyId
	 * @param username
	 * @param request
	 * @param response
	 */
	@GET
	@Path("getCompanyUsersForGroupSet/{companyId}/{username}")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getCompanyUsersForGroupSet(@PathParam("companyId") String companyId, @PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response);
	
	
	
	/**
	 * 根据企业id获取当前用户所在公司的所有用户列表-群组创建+编辑专用
	 * @param companyId
	 * @param username
	 * @param request
	 * @param response
	 */
	@GET
	@Path("getCompanyUsersForGroupSetAndNotJoin/{companyId}/{username}")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getCompanyUsersForGroupSetAndNotJoin(@PathParam("companyId") String companyId, @PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response);
	/**
	 * 根据分组id获取当前用户所在组的所有用户
	 * @param companyId
	 * @param username
	 * @param request
	 * @param response
	 */
	@POST
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List<String> getGroupUsersByGroupId(String groupid);
	/**
	 * 根据分组id获取当前用户所在组的所有用户
	 * @param companyId
	 * @param username
	 * @param request
	 * @param response
	 */
	
	@GET
	@Path("getGroupUserByGroupId")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getGroupUserByGroupId(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 根据username获取当前用户所属于的群聊组列表
	 * @param companyId
	 * @param username
	 * @param request
	 * @param response
	 */
	
	@GET
	@Path("getGroupsByUsername/{companyId}/{username}")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getGroupsByUsername(@PathParam("companyId") String companyId, @PathParam("username") String username, @Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 保存用户在线，但是没有看的消息，为了之后可能正常查看
	 * @param companyId
	 * @param username
	 * @param request
	 * @param response
	 */
	@POST
    @Path("saveNotSeeMessage")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public Long saveNotSeeMessage(HashMap<String,String> params, @Context HttpServletRequest request);
	
	/**
	 * wangwenshuo 20150824 删除未读消息 
	 * @param companyId
	 * @param username
	 * @param request
	 * @param response
	 */
	@GET
	@Path("dropNotSeeMeesage")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void dropNotSeeMeesage(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 获取一个发送者给自己发送的，且自己还没有看的消息集合
	 * @param request
	 * @param response
	 */
	@GET
	@Path("getOldNotSeeMessage")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getOldNotSeeMessage(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/***
	 * 创建新群组
	 * @param request
	 * @param response
	 */
	@GET
	@Path("createGroup")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void createGroup(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/***
	 * 删除群组
	 * @param request
	 * @param response
	 */
	@POST
    @Path("deleteGroup")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map deleteGroup(Map<String,String> params);
	/***
	 * 删除群组
	 * @param request
	 * @param response
	 */
	@GET
	@Path("deleteGroup")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void deleteGroup(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/***
	 * 重新设置群组,同时也是成员邀请，请出接口
	 * @param request
	 * @param response
	 */
	@GET
	@Path("resetGroup")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void resetGroup(@Context HttpServletRequest request, @Context HttpServletResponse response);
	/***
	 * 重新设置群组,同时也是成员邀请，请出接口
	 * @param request
	 * @param response
	 */
	@POST
    @Path("resetGroup")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map resetGroup(Map<String,String> params);
	/***
	 * 重新设置群组,同时也是成员邀请，别人申请 然后点击同意时触发
	 * @param request
	 * @param response
	 */
	@GET
	@Path("resetGroupByApply")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void resetGroupByApply(@Context HttpServletRequest request, @Context HttpServletResponse response);
	/***
	 * 与resetGroupByApply方法配套使用  这个为拒绝时修改聊天记录
	 * @param request
	 * @param response
	 */
	@GET
	@Path("modifyChatLog")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void modifyChatLog(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/***
	 * 自己主动退出群组
	 * @param request
	 * @param response
	 */
	@POST
    @Path("outGroup")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map outGroup(Map<String,String> params);
	/***
	 * 自己主动退出群组
	 * @param request
	 * @param response
	 */
	@GET
	@Path("outGroup")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void outGroup(@Context HttpServletRequest request, @Context HttpServletResponse response);

	/**
	 * 保存历史消息
	 * @param request
	 * @param response
	 */
	@POST
    @Path("saveHistoryMessage")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public Boolean saveHistoryMessage(HashMap<String,String> params, @Context HttpServletRequest request);
	
	/***
	 * 保存历史消息返回mongodb的id
	 * @param request
	 * @param response
	 */
	/** lujixiang 20151113     注释,将get方式改为post方式,修复乱码
	@GET
	@Path("saveHistoryMessageReturnID")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void saveHistoryMessageReturnID(@Context HttpServletRequest request, @Context HttpServletResponse response);
	**/
	
	
	/***
	 * lujixiang 20151113     将get方式改为post方式 ,修复乱码 (保存历史消息返回mongodb的id)
	 * @param request
	 * @param response
	 */
	@POST
	@Path("saveHistoryMessageReturnID")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map saveHistoryMessageReturnID(HashMap<String,String> params, @Context HttpServletRequest request);

	/**
	 * 获取最后的几条历史数据
	 * @param request
	 * @param response
	 */
	@GET
	@Path("getHistoryMessage")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getHistoryMessage(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	
	/**
	 * 获取获取企业用户及分类、群组头像
	 */
	@POST
	@Path("getImgMap")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,Object> getImgMap(Map<String,String> map);
	
	/**
	 * 获取一个群组的所有成员
	 * @param request
	 * @param response
	 */
	@GET
	@Path("getOneGroupUsers")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getOneGroupUsers(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 保存群组中被点名的用户信息
	 * @param request
	 * @param response
	 */
	@GET
	@Path("saveGroupcallOver")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void saveGroupcallOver(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
	/**
	 * 获取在线用户列表
	 * @param request
	 * @param response
	 * @return
	 */
	@GET
	@Path("getCompanySessions")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getCompanySessions(@Context HttpServletRequest request, @Context HttpServletResponse response) ;
	
	/**
	 * 强制关闭一个连接
	 * @param request
	 * @param response
	 * @return
	 */
	@GET
	@Path("closeSession")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void closeSession(@Context HttpServletRequest request, @Context HttpServletResponse response) ;
	
	/**
     * 获取某个文件最后的几条历史数据(群组)
     * @param request
     * @param response
     */
    @GET
    @Path("getHistoryMessage4File")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public void getHistoryMessage4File(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    @POST
    @Path("getHistoryMessage4File")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public String initMongodbMsg(Map<String,String> map);
    /**
     * 邮件激活邀请人加入分组
     * @param map
     * @return
     */
    @POST
    @Path("saveUserToGroupFromEamil")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map saveUserToGroupFromEamil(Map<String,String> map);
    
	/**
     * 获取某个群组管理员的信息(申请加入分组时，给管理员发送消息 )
     * @param request
     * @param response
     */
    @POST
    @Path("getGroupUserIsAdminInfo")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map getGroupUserIsAdminInfo(Map<String,String> map);
    /**
     * 分组移交
     * @param request
     * @param response
     */
    @POST
    @Path("changeGroupAdmin")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map changeGroupAdmin(Map<String,String> map);

    /**
     * 分享/取消分享
     * @param companyId 
     * @param request
     * @param response
     */
	public Map<String, Object> UpdateFileToShareOrNoShare(String msgid,
			String button, String companyId);
	
	/**
     * 发表文件评论
     */
    public boolean newFileComment(Map<String, String> params);
    
    /**
     * 删除文件评论
     */
    public boolean deleteComment(Map<String, String> params);
    
    /**
     * 获取文件的评论 从mongodb里
     */
    public List<Map<String, String>> getFileCommentList(Map<String, String> params);

    /**
     * 将公司管理员加入到被禁用用户创建的群组中
     * @param params
     * @return
     */
    @POST
    @Path("addAdminIntoGroups")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public List<Map<String, String>> addAdminIntoGroups(Map<String, String> params);
    
	
    public boolean addUserIntoGroup(Map<String, String> params);

    /**
	 * 获取文件列表 根据多个id
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> getFileCommentListByVersions(
			Map<String, String> data);
	/**
	 * 同意加入此公司分组
	 * @param userId
	 * @param companyId
	 * @param string
	 * @return
	 */
	public boolean inviteUserIntoGroup(String userId, String companyId,
			String string);
	
	/** 获取分组内部的头像 */
	public HashMap<String, String> getImage(String companyId) ;
	
	/** 新增的方法  用于产生组合头像 */
	public void doImageMath(String companyId , String groupId , List<HashMap<String, String>> groups);
	
	/** 新增的方法  用于产生组合头像 */
	public void doImageMath(String companyId , String username , String groupId , List<HashMap<String, String>> groups);
	
	/** 新增的方法  用于接收依据传递的值所产生的结果集 以供产生组合头像 */
	public List<HashMap<String, String>> getAnOrAnyGroup(String username ,String groupId ,String companyId);
	
	public List<Map<String, String>>doGroupImageMath(String companyId, String username , String groupId,
			List<Map<String, String>> groups);
	
	/**
     * xiewenda 修改前台文件检索的请求
     * @param params
     * @return
     */
    @POST
    @Path("getHistoryMessage")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,Object> getHistoryMessage(Map<String, String> params);
}
