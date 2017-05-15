package cn.flying.rest.onlinefile.restInterface;

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
 * 文件操作主要接口
 * @author longjunhao
 *
 */
public interface FilesWS {
    
    /**
     * 查询文件
     * @author longjunhao 20150326
     */
    @GET
    @Path("searchFile")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void searchFile(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取分类列表
     * @author longjunhao 20150504
     * @param params
     * @return
     */
    @GET
    @Path("getClassList")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getClassList(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取文件列表
     * @author longjunhao 20150317
     * @param params
     * @return
     */
    @GET
    @Path("getFileList")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFileList(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取我的文件列表
     * @author longjunhao 20150505
     * @param params
     * @return
     */
    @GET
    @Path("getMyFileList")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getMyFileList(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取文件信息
     * @author longjunhao 20150305
     * @param params
     * @return
     */
    @GET
    @Path("getFileInfo")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFileInfo(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /** 
    	lujixiang 20150806  根据用户选择的文件夹和文件返回（过滤）不可以删除的文件夹和文件
    **/
    @GET
    @Path("getFilesAndFoldsCanNotDelete")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFilesAndFoldsCanNotDelete(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 查询历史版本
     * @param request
     * @param response
     */
    @GET
    @Path("getFileList4Version")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFileList4Version(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取成员列表
     * @author longjunhao 20150323
     */
    @GET
    @Path("queryMembers")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void queryMembers(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 分享一个文件给某个用户（允许其查看和下载文件）
     */
    @POST
    @Path("shareFileToUser")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String shareFileToUser(Map<String, String> params);
    /**
     * 取消分享一个文件给某个用户（允许其查看和下载文件）
     */
    @POST
    @Path("unShareFileToUser")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String unShareFileToUser(Map<String, String> params);
    /**
     * 拒绝分享一个文件给某个用户
     */
    @GET
    @Path("doNotShareFileToUser")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void doNotShareFileToUser(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 分享多个文件给某个用户（允许其查看和下载文件）
     */
    @POST
    @Path("shareFilesToUser")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String shareFilesToUser(Map<String, String> params);
    
    /**
     * 分享一个文件给某个用户或联系人群组（允许其查看和下载文件）
     */
    @POST
    @Path("shareFileToGroupOrUser")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String shareFileToGroupOrUser(Map<String, String> params);
    
    /**
     * 取消分享
     * @param params
     * @return
     */
    @POST
    @Path("unshareFile")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String unshareFile(Map<String, String> params);

    /**
     * 点赞/取消赞文件
     */
    @POST
    @Path("praiseFile")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String praiseFile(Map<String, String> params);
    
    /**
     * 收藏/取消收藏文件
     */
    @POST
    @Path("collectFile")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String collectFile(Map<String, String> params);
    
    /**
     * 逻辑删除文件，修改isDelete状态值为0
     */
    @POST
    @Path("deleteFile")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String deleteFile(Map<String, String> params);
    /**
     * 逻辑删除文件，修改isDelete状态值为0
     */
    @POST
    @Path("deleteFileExtend")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String deleteFileExtend(Map<String, String> params);
    
    /**
     * 取消一个文件的分享
     */
    @POST
    @Path("cancelShareFile")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String cancelShareFile(Map<String, String> params); 
    
    @POST
    @Path("saveUploadFile")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,String> saveUploadFile(Map<String,String> params);
    
    @GET
    @Path("getFileOtherProp/{companyId}/{fileId}/{ip}/{username}")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    public String getFileOtherProp(@PathParam("companyId")String companyId,@PathParam("fileId")String fileId,@PathParam("ip")String ip,@PathParam("username")String username);
    
    @POST
    @Path("editFileOtherProp")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public boolean editFileOtherProp(Map<String,Object> params);
    
    /**
     * 更新企业文件夹列表缓存
     * @param companyId
     */
    @GET
    @Path("updateCompanyFoldersCache/{companyId}")
    public void updateCompanyFoldersCache(@PathParam("companyId")String companyId);
    
    /**
     * 更新企业左侧分类列表缓存
     */
    @GET
    @Path("updateCompanyClassesCache/{companyId}/{userId}/{userName}")
    public void updateCompanyClassesCache(@PathParam("companyId")String companyId, @PathParam("userId")String userId, @PathParam("userName")String userName);
    
    @GET
    @Path("getFolderById")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFolderById(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取用户的订阅信息
     * @param request
     * @param response
     */
    @GET
    @Path("getSubScribeMsgByUserId")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getSubScribeMsgByUserId(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 添加用户订阅信息
     * @param params
     * @return
     */
    @POST
    @Path("addSubScribeMsgByUserId")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,String> addSubScribeMsgByUserId(Map<String,String> params);
    
    /**
     * 添加用户订阅信息
     * @param params
     * @return
     */
    @POST
    @Path("addSubScribeMsgByDocs")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,String> addSubScribeMsgByDocs(Map<String,String> params);
    
    
    /**
     * 删除用户订阅信息
     * @param params
     * @return
     */
    @POST
    @Path("delSubScribeMsgByUserId")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,String> delSubScribeMsgByUserId(Map<String,String> params);
    
    /**
     * 获取用户的订阅人信息
     * @param request
     * @param response
     */
    @GET
    @Path("getSubScribersByUserId")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getSubScribersByUserId(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
	 /**
     * @param userId 用户ID
     * @param pageIndex 起始查询值
     * @param pageLimit 查询分页
     * @param keyWord 关键词
	 * 通过用户ID查询出所有的用户的订约人的信息
	 */
    @GET
    @Path("searchSubScribeMsgByUserId")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void searchSubScribeMsgByUserId(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 验证文件是否被删除
     * @param request
     * @param response
     */
    @GET
    @Path("checkFileDelete")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void checkFileDelete(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    @GET
    @Path("getFileStatus")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFileStatus(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 分享文件到某个路径下
     * @param params
     * @return
     */
    @POST
    @Path("shareToFolderPath")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String shareToFolderPath(Map<String,String> params);
    
    /**
     * wangwenshuo 20151020 复制到 
     * @param params
     * @return
     */
    @POST
    @Path("copyToFolderPath")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,String> copyToFolderPath(Map<String,String> params);
    
    /**
     * 获取文件夹路径 
     */
    @GET
    @Path("getFolderPath")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFolderPath(@Context HttpServletRequest request, @Context HttpServletResponse response);
    /**
     * 每隔一段时间才能继续申请
     */
    @GET
    @Path("applyFileDown")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void applyFileDown(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取回收站的文件列表
     * @author longjunhao 20150609
     * @param params
     * @return
     */
    @GET
    @Path("getTrashFileList")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getTrashFileList(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 将文件添加到回收站
     * @param fileId
     * @return
     */
    @POST
    @Path("addFileTrashById")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public boolean addFileTrashById(String companyId, String fileId, String userId);
    
    /**
     * 回收站文件恢复
     */
    @POST
    @Path("fileRestore")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String fileRestore(Map<String,String> params);
    
    /**
     * 回收站文件彻底删除
     */
    @POST
    @Path("fileDestroy")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String fileDestroy(Map<String,String> params);
    
    /**
     * 清空回收站 wangwenshuo 20150906
     */
    @POST
    @Path("cleanRecycleBin")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String cleanRecycleBin(Map<String,String> params);
    
    /**
     * 获取用户的常用分类列表
     * @param request
     * @param response
     */
    @GET
    @Path("getClassStarList")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getClassStarList(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取单文件权限
     * @param request
     * @param response
     */
    @GET
    @Path("getFileAuthority")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFileAuthority(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 标记/取消标记为常用分类
     */
    @POST
    @Path("starClass")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String starClass(Map<String,String> params);
    
    /**
     * 撤销文件分享
     */
    @POST
    @Path("backoutFile")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String backoutFile(Map<String, String> params);
    
    /**
     * 发表文件评论
     */
    @POST
    @Path("newFileComment")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String newFileComment(Map<String, String> params);
    
    /**
     * 删除文件评论
     */
    @POST
    @Path("deleteComment")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String deleteComment(Map<String, String> params);
    
    /**
     * 获取文件的评论列表
     */
    @GET
    @Path("getFileCommentList")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFileCommentList(@Context HttpServletRequest request, @Context HttpServletResponse response);

    /**
     * 修改文件的拥有者
     */
    @GET
    @Path("changeFileOwner")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	public String changeFileOwner(Map<String, String> params);
 
    /**
     * 上传文件成功后调用，往db里插入file数据
     */
    @POST
    @Path("insertNewFileAfterUpload")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String insertNewFileAfterUpload(Map<String, String> params);

    /**
     * 修改grouprelation表里的isadmin字段
     */
    @POST
	@Path("getAttachmentByEmail")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public List<Map<String, String>> changeGroupRelationIsAdmin(Map<String, String> params);
	
	/** lujixiang 20150810  批量将文件添加到回收站
     * @param fileId
     * @return
     */
    public boolean addFilesTrashById(String companyId, List<String> fileId, String userId);
    
    
    /** lujixiang 20150810  批量将文件添加到回收站
     * @param fileId
     * @return
     */
    public boolean deleteFiles(String companyId, List<String> fileIds);
   
    /**
     * 安卓(获取文件相关信息)
     * @param request
     * @param response
     */
    @GET
    @Path("getFileRelevantInfo")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public void getFileRelevantInfo(@Context HttpServletRequest request, @Context HttpServletResponse response);

    /**
     * 检测文件名是否已经存在（同一文件夹下）
     * @author longjunhao 20150910
     */
    @GET
    @Path("checkFileNameExist")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void checkFileNameExist(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取常用分类
     */
    @POST
    @Path("getClassStarList")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,Object> getClassStarList(Map<String,Object> param);
    /**
     * 获取分类
     */
    @POST
    @Path("getClassList")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,Object> getClassList(Map<String,Object> param);
    /**
     * 获取评论列表
     */
    @POST
    @Path("getFileCommentList")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,Object> getFileCommentList(Map<String,String> param);
    /**
     * 获得历史版本
     */
    @POST
    @Path("getFileList4Version")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,Object> getFileList4Version(Map<String,String> param);
    
    /**
     * 登录时，初始化左侧分类
     * @author longjunhao 20150928
     * @param param
     * @return
     */
    @POST
    @Path("initClassList")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,Object> initClassList(Map<String, String> param);
        
    
    @POST
    @Path("setFileOpenlevel")
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public Map<String,Object> setFileOpenlevel(Map<String, String> param);
    
    /**     文件评论 Start    **/
    
    
    
	/**
	 * 发送评论
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("onSendComment")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> onSendComment(Map<String, String> params);

	
	/**
	 * 回复评论
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("onReplyComment")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> onReplyComment(Map<String, String> params);
    
	
	/**
	 * 回复评论
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("getFileCommentsListByFileId")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> getFileCommentsListByFileId(Map<String, String> params);
	
	
	/**
	 * 回复评论
	 * 
	 * @param params
	 * @return
	 */
	@POST
	@Path("getMoreReplyComments")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, Object> getMoreReplyComments(Map<String, String> params);
	
	
	/**
	 * 用户上传头像时候，更新用户头像
	 * @param params
	 * @return
	 */
	@POST
	@Path("updateFileCommentsUserHeadImg")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String, String> updateFileCommentsUserHeadImg(Map<String, String> params);
	
	
    /**
     * 通过文件ID获取文件信息
     * @param request
     * @param response
     */
    @GET
    @Path("getFileInfoById")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getFileInfoById(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 获取用户@信息
     * @param request
     * @param response
     */
    @GET
    @Path("getCommentMembersByUserId")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void getCommentMembersByUserId(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    /**
     * 根据用户输入的参数进行检索
     * @param request
     * @param response
     */
    @GET
    @Path("queryMembersByParams")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void queryMembersBykeyWord(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    
    
    
    
    /**     文件评论 End    **/

    
	
	
    /** 20151023 xiayongcai 验证文件是否删除或未公开
     * 返回
     * success :true |false
     * msg
     * */
	@POST
	@Path("checkFileDeleteAndOpenLevel")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> checkFileDeleteAndOpenLevel(Map<String, String> params);

	/**
	 * wangwenshuo 20151117 我的文档文件分享到分类下
	 * @param params
	 * @return
	 */
	@POST
	@Path("fileShareToClass")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> fileShareToClass(Map<String, String> params);
	
	
	/**
	 * xiewenda 修改前台文件检索的请求
	 * @param params
	 * @return
	 */
	@POST
	@Path("searchFile")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,Object> searchFile(Map<String, String> params);
	
	/**
	 * 将文件拖拽到文件夹里面
	 * @param params
	 * @return
	 */
	@POST
	@Path("dragFileToDocumnet")
	@Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
	@Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
	public Map<String,String> dragFileToDocumnet(Map<String, String> params);
	
	
	/** lujixiang 20151210 新增文件删除,并更新版本号 **/
    @POST
    @Path("deleteFileAndUpdateVersion")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public String deleteFileAndUpdateVersion(Map<String, String> params);
    
    /**
     * wangwenshuo 20151222 批量检测某一文件夹下文件名是否存在
     * @param request
     * @param response
     */
    @GET
    @Path("checkFileNamesExist")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void checkFileNamesExist(@Context HttpServletRequest request, @Context HttpServletResponse response);
	
    /**
     * xyc 20151230 批量检测某一文件夹下文件名是否存在
     * @param request
     * @param response
     */
    @GET
    @Path("checkClassFileNamesIsExist")
    @Produces(MediaTypeEx.APPLICATION_JSON_UTF8)
    public void checkClassFileNamesIsExist(@Context HttpServletRequest request, @Context HttpServletResponse response);
    
    
    /**
     * xyc 获取当前分类最大SERIALNUMBER
     * @param params
     * @return
     */
    public String getClassHighestSerialNumber(String tableName,String classId,String isFile);
    /**
     * xyc 获取文件详情
     * @param params
     * @return
     * DocumentClass在用
     */
    public Map<String, String> getFileById(String tableName,String fileId);
    /**
     * xyc 更新当前分类的序号
     * @param 根据文件名，当前class，文件或文件夹
     * @return true,false
     */
    public String updateClassFileOrFolderSerialnumber(String tableName,String classId,String isFile);
    
    /**
     * xiayongcai
     * 更新文件和文件夹拥有者
     * @param 
     * groupsOwnerId：拥有分类的id
     * data_idseq：所在分类的idseq
     * userIds：用户
     * companyId：企业id
     */
    @POST
    @Path("updateGroupsFileAndFolder")
    @Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
    @Consumes(MediaTypeEx.APPLICATION_JSON_UTF8)
    public boolean updateGroupsFileAndFolderOwner(Map<String,String> params);
    
    /**
     * 获取文件详情 添加索引
     * @param companyId
     * @param fileId
     * @return
     */
    public Map<String,String> getFileForIndex(String companyId,String fileId);
}
