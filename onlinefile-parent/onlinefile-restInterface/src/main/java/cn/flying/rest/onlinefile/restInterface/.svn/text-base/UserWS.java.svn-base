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

import org.hibernate.annotations.Parameter;

import cn.flying.rest.platform.utils.MediaTypeEx;

public interface UserWS {

	@POST
	@Path("saveOrUpdate")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> saveOrUpdate(Map<String, String> params);

	/**
	 * 根据用户的登陆名称获取用户一些基本信息
	 * @author longjunhao 20150324
	 * @param companyId
	 * @param userName
	 * @return
	 */
	@GET
    @Path("getUserInitInfo/{companyId}/{userName}")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> getUserInitInfo(@PathParam("companyId")String companyId, @PathParam("userName")String userName);
	
	/** 
	 * 管理员添加用户
	 * @param params
	 * @return
	 */
	@POST
	@Path("addUser")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> addUser(Map<String, String> params);
	
	/** 
	 * 管理员添加用户（2）
	 * @param params
	 * @return
	 */
	@POST
	@Path("inviteAddUser")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> inviteAddUser(Map<String, String> params);
	
	/** 
	 * 完善/修改用户信息
	 * @param params
	 * @return
	 */
	@POST
	@Path("editUserInfo")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> editUserInfo(Map<String, String> params);
	/** 
	 * 设置用户个性信息
	 * @param params
	 * @return
	 */
	@POST
	@Path("singleSetUser")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> singleSetUser(Map<String, String> params);
	
	/**
	 * 文件评论回车设置
	 * @param params
	 * @return
	 */
	@POST
	@Path("commentEnterSet")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> commentEnterSet(Map<String, String> params);
	
	
	/** 
	 * 根据id获取当前用户的个性设置
	 * @param params
	 * @return
	 */
	@POST
	@Path("getSingleSet")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> getSingleSet(Map<String, String> params);
	
	/**
	 * 安卓端获取个性设置
	 * @param params
	 * @return
	 */
	@POST
	@Path("getSingleSets")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> getSingleSets(Map<String, String> params);
	
	
	/** 
	 * 验证邮件是否已经激活
	 * @param params
	 * @return
	 */
	@POST
	@Path("verifyMailbox")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> verifyMailbox(Map<String, String> params);
	/** 
	 * 验证邮件是否已经激活
	 * @param params
	 * @return
	 */
	@POST
	@Path("verifyMailboxS")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> verifyMailboxS(Map<String, String> params);
	
	/** 
	 * 修改密码
	 * @param params
	 * @return
	 */
	@POST
	@Path("modifyPassword")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> modifyPassword(Map<String, String> params);
	/** 
	 * 根据用户id获得机构id
	 * @param params
	 * @return
	 */
	@POST
	@Path("getOrgByUserId")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List<HashMap<String, Object>> getOrgByUserId(Map<String, String> params);

	/** 
	 * 展现激活页面的信息
	 * @param params
	 * @return
	 */
	@POST
	@Path("showActivate")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> showActivate(Map<String, String> params);
	
	/** 
	 * 校验用户是否存在
	 * @param params
	 * @return
	 */
	@POST
	@Path("userIsExist")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> userIsExist(Map<String, String> params);
	
	/** 
	 * 真正激活用户
	 * @param params
	 * @return
	 */
	@POST
	@Path("activateAccount")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> activateAccount(Map<String, String> params, @Context HttpServletRequest request);
	/** 
	 * 真正激活用户
	 * @param params
	 * @return
	 */
	@POST
	@Path("activateAccountByLink")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> activateAccountByLink(Map<String, String> params, @Context HttpServletRequest request);
	
	/** 
	 * 根据username获得用户详细信息
	 * @param params
	 * @return
	 */
	@POST
	@Path("getUserInfoByUserName")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> getUserInfoByUserName(Map<String, String> params);
	/** 
	 * 根据username 模板请求
	 * @param params
	 * @return
	 */
	@GET
	@Path("getUserInfoByUserName")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void getUserInfoByUserName(@Context HttpServletRequest request, @Context HttpServletResponse response);
	/** 
	 * 管理员查看用户列表
	 * @param params
	 * @return
	 */
	@POST
	@Path("userList")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List<HashMap<String, Object>> userList(HashMap<String, String> params);
	/** 
	 * 管理员检索用户列表
	 * @param params
	 * @return
	 */
	@POST
	@Path("userListByUserName")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List<HashMap<String, Object>> userListByUserName(HashMap<String, String> params);
	/** 
	 * 当前公司下员工总数
	 * @param params
	 * @return
	 */
	@POST
	@Path("getCountAll")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public int getCountAll(Map<String, String> params);
	/** 
	 * 删除用户
	 * @param params
	 * @return
	 */
	@POST
	@Path("deleteUserList")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> deleteUserList(Map<String, String> params);

	/** 
	 * 修改管理员用户(企业注册)
	 * @param params
	 * @return
	 * 20151015 xiayongcai 修改符合rest
	 */
	@GET
	@Path("updateUser")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public boolean updateUser(Map<String, String> onlinefileUser);
	
	/** 
	 * 根据用户id获得用户
	 * @param params
	 * @return
	 *20151015 xiayongcai 修改符合rest
	 */
	@GET
	@Path("getUserById")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public HashMap<String, String> getUserById(String id);

	@POST
	@Path("saveUser")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.TEXT_PLAIN)
	public boolean saveUser(Map<String, String> onlinefileUser);
	
	@POST
	@Path("saveHeadImage")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> saveHeadImage(Map<String, String> params);
	/*
	 * 根据用户username获得他的头像路径
	 * @param params
	 * @return
	 */
	@POST
	@Path("getImagePath")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> getImagePath(Map<String, String> params);
	
	/** 
	 * 更改用户启用禁用状态
	 * @param params
	 * @return
	 */
	@POST
	@Path("changeIsEnableStatus")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> changeIsEnableStatus(Map<String, String> params);
	/** 
	 * 重新发送邮件
	 * @param params
	 * @return
	 */
	@POST
	@Path("reSendMail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> reSendMail(Map<String, String> params);
	/** 
	 * 根据userid获得用户详细信息
	 * @param params
	 * @return
	 */
	@POST
	@Path("getUserInfoByUserId")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> getUserInfoByUserId(Map<String, String> params);
	/** 
	 * 修改用户信息
	 * @param params
	 * @return
	 */
	@POST
	@Path("editUser")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> editUser(Map<String, String> params);
	/** 
	 * 修改用户企业ID信息
	 * @param params
	 * @return
	 */
	@POST
	@Path("editUserCompanyId")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.TEXT_PLAIN)
	public Map<String, String> editUserCompanyId(String companyId,String userId);
	/**
	 * 校验并上传文件
	 * @param request request请求.
	 */
	@POST
	@Path("validateAndParseExcel")
	@Consumes(MediaTypeEx.MULTIPART_FORM_DATA)
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public String validateAndParseExcel(@Context HttpServletRequest request,@Context HttpServletResponse response);
	
	/**
	 * 获取上传文件的结构信息
	 * @param request request请求.
	 */
	@POST
	@Path("initImportStep2ByPage")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> initImportStep2ByPage(Map<String, String> params,@Context HttpServletRequest request);
	/**
	 * 向数据库导入数据
	 * @return
	 */
	@POST
    @Path("realImport")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> realImport(Map<String , Object> map);
	/**
	 * 向数据库导入数据
	 * @return
	 */
	@POST
    @Path("closeImport")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> closeImport(Map<String , Object> map);
	
	@POST
    @Path("getOrgInfo/{companyId}")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> getOrgInfo(@PathParam("companyId") String companyId) ;
	
	@POST
	@Path("companyUserInitInfo/{companyId}")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Map<String, String>> companyUserInitInfo(@PathParam("companyId") String companyId) ;
	
	@GET
    @Path("getUserNameById/{userID}")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public String getUserNameById(@PathParam("userID")String userID);
	
	@POST
	@Path("forgetpassword")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> forgetpassword(Map<String , Object> map) ;
	/**
	 * 验证时间（修改密码的邮件是否过期）
	 * @return
	 */
	@POST
	@Path("toforgetpassword")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> toforgetpassword(Map<String , String> map) ;
	/**
	 * 忘记密码功能  之 修改密码
	 * @return
	 */
	@POST
	@Path("fromforgetpasswordtochange")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> fromforgetpasswordtochange(Map<String , String> map) ;
	
	/**
	 * 获取登陆日志
	 * @param request
	 * @param response
	 */
	@GET
    @Path("getLoginLog")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void getLoginLog(@Context HttpServletRequest request,@Context HttpServletResponse response);
	/**
	 * 获取全部安全日志
	 * @param request
	 * @param response
	 */
	@GET
	@Path("getSafetyLog")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void getSafetyLog(@Context HttpServletRequest request,@Context HttpServletResponse response);
	/**
	 * 获取邀请链接
	 * @param request
	 * @param response
	 */
	@POST
	@Path("openUrl")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> openUrl(Map<String , String> map);
	
	/**
	 * 获取验证码(密码找回)
	 * @param request
	 * @param response
	 */
	@GET
	@Path("setValidateCode")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public void setValidateCode(@Context HttpServletRequest request, @Context HttpServletResponse response);
	/**
	 * 检验密码是否正确，用来移交管理员的
	 * @param request
	 * @param response
	 */
	@POST
	@Path("checkPasswordIsRight")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map checkPasswordIsRight(Map<String,String> map);
	
	/**
	 * 重置密码
	 * @param request
	 * @param response
	 */
	@POST
	@Path("resetPassword")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> resetPassword(Map<String,String> map);
	
	@POST
	@Path("invitation")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public Boolean invitation(Map<String , String> map);

	/**
     * 获取某个群组管理员的信息(申请加入分组时，给管理员发送消息 )
     * @param request
     * @param response
     * 20151015 xiayongcai 修改符合rest
     */
	@GET
	@Path("getUserByUserId")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> getUserByUserId(String userId);

	/**
	 * 保存企业与用户的关系
	 * @param user
	 * @return
	 */
	@POST
	@Path("saveCompanyUsers")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public boolean saveCompanyUsers(Map<String, String> user);
	
	/**
	 * 根据用户ID获取用户信息
	 * @param id
	 * @return
	 */
	@POST
	@Path("getOneUserInfo/{id}")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> getOneUserInfo(@PathParam("id") String id) ;
	
	/**
	 * 获取当前用户所属的企业列表
	 * @param userid
	 * @return
	 */
	@GET
	@Path("getUserCompanys/{userid}")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List<HashMap<String, String>> getUserCompanys(@PathParam("userid") String userid) ;
	
	/**
	 * 根据用户名及密码判断是否登录成功
	 * @param user(id),password
	 * @param 个人用户的详细信息及相关参数
	 */
	@POST
	@Path("loginGetUserParticular")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> loginGetUserParticular(Map<String,String> map);
	
	/**
	 * 同意加入团队，把classid变0就行
	 * @param request
	 * @param response
	 */
	@GET
	@Path("agreenInSideCompany")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void agreenInSideCompany(@Context HttpServletRequest request,@Context HttpServletResponse response);
	/**
	 * 拒绝加入团队，删除字段
	 * @param request
	 * @param response
	 */
	@GET
	@Path("noAgreenInSideCompany")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void noAgreenInSideCompany(@Context HttpServletRequest request,@Context HttpServletResponse response);

	/**
	 * 安卓修改头像上传图片
	 * @param 
	 * @param 
	 */
	@POST
	@Path("uploadImg")
	@Consumes(MediaTypeEx.MULTIPART_FORM_DATA)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> uploadImg(@Context HttpServletRequest request);
	/**
	 * 安卓获取登陆日志
	 * @param request
	 * @param response
	 */
	@GET
    @Path("getLoginLogAndroid")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void getLoginLogAndroid(@Context HttpServletRequest request,@Context HttpServletResponse response);
	

	/**
     * 根据fullname和companyId获得所用户id
     * @param 
     * @param 
     */
    @POST
    @Path("getUsersByFullName")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public List<String> getUsersByFullName(Map<String,Object> param);
    
    /**

     */
    @POST
    @Path("getLoginLog")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,Object> getLoginLog(Map<String,Object> param );
    
    
    /**
     * xiyongcai
     * @param userid // username
     * 根据userid // username 
     * 验证用户是否已经注册过企业
     */
    @POST
    @Path("isUserCompayExist")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public boolean isUserCompayExist(String userId);

    /**
	 * 根据用户Id获得他的头像路径
	 * @param userId
	 * @return userImgUrl
	 */
	@POST
	@Path("getUserImagePathByUserId")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public String getUserImagePathByUserId(String userId);
    
    
	 /** 20151021 xiayongcai
	  * @param params 唯一username
	  * 如果企业记忆企业被禁用
	  * --那么跳转到自己创建的企业
	  * 如果该用户没有自己企业
	  * --将跳转到激活的企业
	  * 如果该用户没有激活的企业
	  * --那么将显示该用户被彻底禁用
	  *	@return 返回默认企业Id
	  * @return 返回自己注册企业的Id
	  * defaultCompanyId
	  * ownCompanyId
	  * 
	  * */
	@GET
	@Path("getUserCompanyId/{username}")
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> getUserCompanyId(@PathParam("username")String username);
	
	
	
	@POST
    @Path("userLogin")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,String> userLogin(Map<String,String> param);
	
	@GET
    @Path("registerUser/{userName}/{passWord}")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,String> registerUser(@PathParam("userName")String userName,@PathParam("passWord")String passWord);
    
}
