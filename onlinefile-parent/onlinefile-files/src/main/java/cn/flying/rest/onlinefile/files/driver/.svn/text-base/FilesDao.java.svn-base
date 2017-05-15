package cn.flying.rest.onlinefile.files.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文档数据库操作接口
 * @author longjunhao
 *
 */
public interface FilesDao {
    
    /**
     * 获取分类列表
     * @param companyId
     * @return
     */
    public List<Map<String, String>> getClassList(String companyId);
    
    /**
     * 获取分类列表
     * @param companyId
     * @param ids
     * @return
     */
    public List<Map<String, String>> getClassList(String companyId, List<String> ids);
    
    /**
     * 获取文件id的列表
     * @param companyId 公司ID
     * @param classId 分类ID
     * @param userId 用户id
     * @param start
     * @param limit
     * @return
     */
    public List<String> getFileIdList(String companyId, String classId, String userId, int start, int limit,String orderField,String orderType);
    
    /**
     * 获取我的文件id的列表
     * @param companyId 公司ID
     * @param userId 用户id
     * @param start
     * @param limit
     * @return
     */
    public List<String> getMyFileIdList(String companyId, String userId, int start, int limit,String orderType,String orderField);
    
    /**
     * 获取文件列表
     * @param companyId
     * @param ids
     * @return
     */
    public ArrayList<Map<String, String>> getFileListByIds(String companyId, List<String> idList);
    
    /**
     * 获取文件列表总count
     * @param companyId
     * @param classId
     * @param userId
     * @return
     */
    public int getFileListCount(String companyId, String classId, String userId);
    
    /**
     * 获取文件总count  为了显示文件和文件夹的数量
     * @param companyId
     * @param classId
     * @param userId
     * @return
     */
    public int getFileCount(String companyId, String classId, String userId);
    
    /**
     * 获取我的文件列表总count
     * @param companyId
     * @param userId
     * @return
     */
    public int getMyFileListCount(String companyId, String userId);
    
    /**
     * liqiubo 20140407 edit
     * filedLst 传入的是自定义的属性列表，会把这些自定义的值返回。
     * 获取文件信息
     * @param companyId
     * @param fileId 文件id
     * @return
     */
    public Map<String, String> getFileInfo(String companyId, String fileId,List<Map<String,String>> filedLst);
    
    /**
     * 获取公司的成员列表
     * @param companyId
     * @return
     */
    public List<Map<String, String>> getMembers(String companyId);
    
    /**
     * 获取用户信息
     * @param companyId
     * @param userId
     * @return
     */
    public Map<String, String> getMemberById(String companyId, String userId);
    
    /**
     * 获取用户拥有权限的文件id列表
     * @param companyId
     * @param userId
     * @return
     */
    public Map<String,Map<String,String>> getFileUserRelationWithUserId(String companyId, String userId, List<String> groupIdList);
    
    /**
     * 是否有权限查看文件
     * @param fileId 文件id
     * @param userId 用户id
     * @return
     */
    public boolean hasRightToReadFile(String fileId, String userId);
    
    /**
     * 分享一个文件给某个用户或联系人群组（允许其查看和下载文件）
     * @param companyId
     * @param fileId
     * @param toUserId
     * @return
     */
    public boolean shareFileToUserOrGroup(String companyId, String fileId, String targetId, boolean isGroup, String fromUserName,String isDownload,String isLook);
    /**
     * 更新或新增文件给某个用户或联系人群组（允许其查看或下载文件）
     * @param companyId
     * @param fileId
     * @param toUserId
     * @param isApplyLabel 标示：更新、插入
     * @param forAllVersion	wangwenshuo 20160112 添加 是否是所有版本权限
     * @return
     */
    public boolean updateOrShareFileToUserOrGroup(String companyId, String fileId, String targetId, boolean isGroup, String fromUserName,boolean isApplyLabel,String applyLabel,String isDownload,String isLook, boolean forAllVersion);
    /**
     * 分享一个文件给某个用户或联系人群组（允许其查看和下载文件）
     * @param companyId
     * @param fileId
     * @param toUserId
     * @return
     */
    public boolean unShareFileToUser(String companyId, String fileId, String targetId);
    
    /**
     * 分享多个个文件给某个用户（允许其查看和下载文件）
     * @param companyId
     * @param fileIdList
     * @param toUserId
     * @return
     */
    public boolean shareFilesToUser(String companyId, List<String> fileIdList, String toUserId, String fromUserName);
    
    /**
     * 判断文件是否已经分享给某用户
     * @param companyId
     * @param fileId
     * @param targetId
     * @param isGroup
     * @return
     */
    public boolean hasShareFileToUserOrGroup(String companyId, String fileId, String targetId, boolean isGroup);
    /**
     * 判断文件是否已经分享
     * @param companyId
     * @param fileId
     * @param targetId
     * @param forAllVersion wangwenshuo 20160119 add 是否是根据soleNumber查权限（如果是，全部版本文件都有权限   否则是单个文件）
     * @return
     */
    public Map<String, String> getHasShareFileToUserOrGroup(String companyId, String fileId,String targetId, boolean isGroup, boolean forAllVersion);
    
    /**
     * 从fileIds中找出已经有权限的fileIds
     * @param companyId
     * @param fileIds
     * @param toUserId
     * @return
     */
    public List<String> findFileIdsForHasRight(String companyId, String fileIds, String toUserId);
    
    /**
     * 获取某个用户所有已经点赞的文件id列表
     * @param companyId
     * @param userId
     * @return
     */
    public List<String> getPraiseFileIdsWithUserId(String companyId, String userId);

    /**
     * 点赞/取消赞文件
     * @param fileId 文件id
     * @param userId 用户id
     * @param status true：赞， false：取消赞
     * @return
     */
    public boolean praiseFile(String companyId, String fileId, String userId, boolean status);
    
    /**
     * 更新赞数量
     * @param fileId
     * @param status true：赞， false：取消赞
     * @return
     */
    public boolean praiseCountUpdate(String companyId, String fileId, boolean status);
    
    /**
     * 收藏/取消收藏文件
     * @param fileId 文件id
     * @param userId 用户id
     * @param status true：收藏， false：取消收藏
     * @return
     */
    public boolean collectFile(String fileId, String userId, boolean status);
    
    /**
     * 逻辑删除文件，修改isDelete状态值为0
     * @param fileId 文件id
     * @return
     */
    public boolean deleteFile(String companyId, String fileId);
    
    /**
     * 取消一个文件的分享
     * @param fileId
     * @return
     */
    public boolean cancelShareFile(String fileId);
    
    /**
     * 设为私密：撤销本文件所有分享
     * @param fileId
     * @return
     */
    public boolean setPrivacyAnnulShare(String fileId);
    
    /**
     * 向file_N表插入数据
     * @param map
     * @return 返回0那么标识插入到file_n表中失败
     */
    public int insertIntoFilesN(Map<String,String> map);
    
    /**
     * 获取文件的id路径
     * @param id
     * @return
     */
    public String getIdSeqWithId(String companyId, String id);
    
    /**
     * 获取文件的所有版本
     * @param companyId
     * @param classId
     * @param fileName
     * @return
     */
    public List<Map<String, String>> getFileList4Version(String companyId, String classId, String fileName, String fileType);
    
    /**
     * 获取指定分类的分组信息列表
     * @param companyId
     * @param classIdList 分类id列表
     * @return
     */
    public List<Map<String, String>> getClassGroups(String companyId, List<String> classIdList);
    
    /**
     * 获取某个用户关联的分组id列表
     * @param userId
     * @return
     */
    public List<String> getGroupIdsWithUserId(String userId);
    
    public boolean editFileOtherProp(String companyId,String fileId,List<String> columName,List<String> columVal);
    
    /**
     * 获取某个企业下，所有文件夹id,filename
     * @param companyId
     * @return
     */
    public List<Map<String, String>> getCompanyFolders(String companyId);
    
    /**
     * 获取文件夹信息（分类信息）
     * @param companyId
     * @param folderId
     * @return
     */
    public List<Map<String,String>> getFolderById(String companyId,String folderId);
    
    /**
     * 获取公司的分组信息
     * @param companyId
     * @param userId 
     * @return
     */
    public Map<String, Map<String, String>> getCompanyGroups(String companyId, String username, String userId);
    
    /**
     * 是否属于某个群组分类成员
     * @param groupId
     * @param userId
     * @return
     */
    public boolean isGroupMember(String groupId, String userId);
    
    /**
     * 获取分组id
     * @param classId 分类id
     * @return
     */
    public String getGroupIdByClassId(String classId,String companyId);
    
    /**
     * 获取某用户加入的分类id列表
     * @param companyId
     * @param userId
     * @return
     */
    public List<String> getUserClassIds(String companyId, String userId);
    
    /**
     * 获取用户的订阅信息
     * @param request
     * @param response
     */
    public List<Map<String,String>> getSubScribeMsgByUserId(Map<String,String> map);
    
    /**
     * 获取订阅信息总数
     * @param request
     * @param response
     */
    public int getSubScribeMsgCount(Map<String,String> map);


    /**
     * 添加用户订阅信息
     * @param params
     * @return
     */
	public Map<String, String> addSubScribeMsgByUserId(Map<String, String> params);
	
	/**
     * 添加用户订阅信息
     * @param params
     * @return
     */
	public Map<String, String> addSubScribeMsgByDocs(Map<String, String> params);


	/**
     * 删除用户订阅信息
     * @param params
     * @return
     */
	public Map<String, String> delSubScribeMsgByUserId(Map<String, String> params);
	
    /**
     * 获取用户的订阅人信息
     * @param request
     * @param response
     */
    public Map<String,Object> getSubScribersByUserId(Map<String,String> map);

	
	public List<Map<String,String>> getClassByIdseq(String idseq,String companyId);
	
	/**
	 * 通过文件的id获取文件信息
	 * @param fileId（文件的ID）
	 * @return 返回文件的map
	 */
	public Map<String,String> getFileInfoById(String companyId,String fileId);
	
	/**
	 * 取消分享
	 * @param companyId
	 * @param fileId
	 * @param userId
	 * @return
	 */
	public boolean unshareFile(String companyId, String fileId, String userId,int maxVersion, String isExist, String unshareType, String newFileName);
	
	 /**
     * @param userId 用户ID
     * @param pageIndex 起始查询值
     * @param pageLimit 查询分页
     * @param keyWord 关键词
	 * 通过用户ID查询出所有的用户的订约人的信息
	 */
	public List<Map<String,String>> searchSubScribeMsgByUserId(Map<String,String> map);
	
	 /**
     * @param userId 用户ID
     * @param pageIndex 起始查询值
     * @param pageLimit 查询分页
     * @param keyWord 关键词
	 * 通过用户ID查询出所有的用户的订约人的信息总数
	 */
	public int searchSubScribeMsgByUserIdCount(Map<String,String> map);
	
	/**
	 * 判断是否订阅了某个用户
	 * @param userid  当前用户
	 * @param rssUserName  被订阅人username
	 * @return
	 */
	public boolean hasSubScribeUser(String userid, String rssUserName);
	
	/**
	 * 根据用户名获取id
	 * @param companyId
	 * @param userName
	 * @return
	 */
	public String getUserIdWithName(String companyId, String userName);
	
	/**
	 * 获取分组信息
	 * @param companyId
	 * @param Flag
	 * @return
	 */
	public Map<String,String> getGroupInfoByFlag(String companyId, String Flag);
	
	/**
	 * 查询idseq的中文名称
	 * @param idSeq
	 * @return
	 */
	public Map<String, String> getFolderPathNameWithIdSeq(String companyId, String idSeq);
	
	public Boolean checkFileDelete(String fileId,String companyId);
	
	/**
	 * 文件分享到某个路径下
	 * @param companyId
	 * @param fileId
	 * @param folderId
	 * @param idSeq 文件的idseq
	 * @param openlevel
	 * @return
	 */
	public boolean shareToFolderPath(String companyId, String fileId, String folderId, String idSeq, String openlevel);
	
	/**
	 *  wangwenshuo 20151020 复制到 
	 * @param companyId
	 * @param fileId
	 * @param folderId
	 * @param idSeq 文件的idseq
	 * @param openlevel
	 * @return
	 */
	public boolean copyToFolderPath(String companyId, List<Map<String, String>> files,String fileName , String folderId, String idSeq, String openlevel);
	
    /**
     *  wangwenshuo 20151117 我的文档文件分享到分类dao
     * @param companyId
     * @param fileId
     * @param folderId
     * @param idSeq 文件的idseq
     * @param openlevel
     * @return
     */
    public int fileShareToClass(String fromCompanyId, String toCompanyId, Map<String, String> file, String fileName, String folderId, String idSeq, String openlevel, String userId);
   
    /**
     *  wangwenshuo 20151117 我的文档文件分享到分类log
     * @param companyId
     * @param fileId
     * @param folderId
     * @param idSeq 文件的idseq
     * @param openlevel
     * @return
     */
    public boolean fileShareToClassLog(String fromCompanyId, String toCompanyId, Map<String, String> file, String fileName, String folderId, String idSeq, String openlevel);
	/**
	 * 获取分类群组的FLAG
	 * @param classId  分类群组id
	 * @return
	 */
	public Map<String, String> getFlagWithClassId(String companyId, String classId);
	
	
	/**
	 * 通过公司ID获取用户的姓名
	 * @param companyId 公司ID
	 * @return 返回用户姓名
	 */
	public Map<String,String> getUserFullNameByCompanyId(String companyId);
	
	/**
	 * 将文件添加到回收站
	 * @param fileId
	 * @return
	 */
	public boolean addFileTrashById(String companyId, String fileId, String userId, String pathName);
	
	/**
     * 获取回收站文件id的列表
     * @param companyId 公司ID
     * @param userId 用户id
     * @param start
     * @param limit
     * @return
     */
    public List<Map<String,String>> getTrashFileList(String companyId, String userId, int start, int limit,String orderType,String orderField,String keyWord);
    
    /**
     * 获取回收站文件列表总count
     * @param companyId
     * @param userId
     * @return
     */
    public int getTrashFileListCount(String companyId, String userId,String keyWord);
    
    /**
     * 回收站彻底删除文件
     * 就是把isDelete改为1
     * @param companyId
     * @param fileId
     * @return
     */
    public boolean fileDestroy(String companyId, String fileId, String userId);
    
    /**
     * 清空回收站
     * 就是把isDelete改为1
     * @param companyId
     * @return
     */
    public boolean cleanRecycleBin(String companyId, String userId);
    
    /**
     * 回收站恢复文件
     * 就是把isDelete改为0
     * @param companyId
     * @param fileId
     * @param fileIdSeq
     * @return
     */
    public Map<String, String> fileRestore(String trashIds, String companyId, String userId);
    
    /**
     * 删除回收站表的数据
     * @param companyId
     * @param fileId
     * @return
     */
    public boolean deleteTrashFile(String companyId, String fileId);
    
    /**
     * 获取用户的常用分类id列表
     * @param userId
     * @return
     */
    public List<String> getClassStarIds(String userId);
    
    /**
     * 标记/取消标记常用分类
     * @param userId
     * @param companyId
     * @param classId
     * @param status true:标记
     * @return
     */
    public boolean starClass(String userId, String companyId, String classId, boolean status);
    
    /**
     * 修改文件是否为isLast
     * @param fileId 文件id
     * @param isLast '1' '0'
     * @return
     */
    public boolean updateFileIsLast(String companyId, String fileId, String isLast);
    
    /**
     * 修改文件的所有版本为非last
     * @param companyId
     * @param classId
     * @param fileName
     * @param fileType
     * @return
     */
    public void updateFilesIsNotLast(String companyId, String classId, String fileName, String fileType);
    	
    /**
     * 获取下一个版本号
     * @param companyId
     * @param classId
     * @param fileName
     * @param fileType
     * @return
     */
    public int getNextVersion(String companyId, String classId, String fileName, String fileType);
    /**
     * 获取当前文件last版本的信息
     * @param companyId
     * @param classId
     * @param fileName
     * @param fileType
     * @return
     */
    public Map<String, String> getCurrentLastFile(String companyId, String classId, String fileName, String fileType);
    
    /**
     * 撤销一个文件给某个用户或联系人群组的分享
     * @param companyId
     * @param fileId
     * @return
     */
    public boolean backoutFile(String companyId, String fileId, String targetId, boolean isGroup);

    /**
     * 修改文件的拥有者
     * @param userIdStr 多个用逗号隔开的userIds
     * @param userstatus 改变后的状态，1为启用，0为禁用
     * @return
     */
	public String changeFileOwner(String userIdStr, String userstatus, String companyAdminId, String companyId);
	
	 /**
     * 向file_N表插入数据
     * @return 返回0那么标识插入到file_n表中失败
     */
    public Map<String, String> insertIntoFilesN(String companyId, String classId, String fileName, String type, String size, String md5, String openLevel, String fileId, String userId);

    /**
     * 向分类信息中添加额外的群组信息  包括 未读消息数目、@次数、joindate、jointime
     * @param classList
     */
	public void getClassExtraInfo(List<Map<String, String>> classList, String companyId, String username, String userId);

	 /**
     * 修改grouprelation表的isadmin字段
     * @param userIdStr 多个用逗号隔开的userIds
     * @param userstatus 改变后的状态，1为启用，0为禁用
     * @return
     */
	public List<Map<String, String>> changeGroupRelationIsAdmin(String userIdStr,
			String userstatus, String userId, String companyId);

	/** lujixiang 20150804 判断用户是否拥有某一文档权限(忽略版本号)
     * 获取用户拥有权限的文件id列表
     * @return
     */
    public boolean hasRightIgnoreVersion(int companyId, int userId, int classid, String fileName, String type, List<String> groupIdList) ;

    /**
     * wangwenshuo  判断ids的文件夹是否存在于回收站中  如果存先得到最上层的路径
     * @param companyId
     * @param ids  in参数  1,2,3
     * @return
     */
	public String getTrashFileByFromIds(String companyId, String ids);

	
	/**
	 * lujixiang 20150806 根据文件夹获取所欲文件详情 
	 * @param companyId
	 * @param folderId
	 */
    public List<Map<String,String>> getFilesByFoldId(String companyId,String folderId) ;
    
    /**lujixiang 20150810   批量添加文件到回收站
     * @param fileId
     * @return
     */
    public boolean addFilesTrashById(String companyId, List<String> fileIds, String userId, String pathName);
    
    
    /** lujixiang 20150810  批量删除文件,支持回滚(建议将删除文件和放入回收站做事物处理)
     * @param fileId 文件id
     * @return
     */
    public boolean deleteFiles(String companyId, List<String> fileIds);
    
    /**
     * 获取回收站文件列表
     * @author longjunhao 20150817
     * @param companyId
     * @param ids
     * @return
     */
    public List<Map<String, String>> getTrashFileListByIds(String companyId, List<String> idList);

    /**
     *  判断当前用户是否已经点赞
     * @param companyId
     * @param userId
     * @param fileId
     * @return
     */
	public boolean getUserIsPraise(String companyId, String userId,
			String fileId);

	/**
	 * wangwenshuo 20150828  根据idseq获取所有的子文件
	 * @param idSeq
	 * @return
	 */
	public List<Map<String, String>> getSubFilesByIdseq(String companyId, String idSeq);
   /**
    * 根据用户id和公司id查询常用分类
    * @param userId
    * @param companyId
    * @return
    */
   public List<String> getClassStarIds(String userId, String companyId);
   /**
    * 根据企业ID获取企业所有分类图片
    * @param userId
    * @param companyId
    * @return
    */
   public Map<String, String> getClassImg(String CompanyId);
   
   /**
    * 判断文件名称是否已经存在（同一文件夹下）
    * @author longjunhao 20150910
    * @param companyId
    * @param classId
    * @param fileName
    * @param fileType
    * @return true：存在
    */
   public boolean checkFileNameExist(String companyId, String classId, String fileName, String fileType);
   
	/**
	 * 设为私密时用
	 * @param companyId
	 * @param classId
	 * @param fileId
	 * @return
	 */
   public Map<String, String> getFileById(String companyId, String classId,
		String fileId);
   
   /**
    * 取消私密
    * @param companyId
    * @param fileId
    * @param openlevel
    * @return
    */
   public boolean setFileOpenlevel(String companyId, String fileId, String openlevel,String rename);
   
   /**
	 * 发送评论
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, String> onSendComment(Map<String, String> params);

	/**
	 * 回复评论
	 * 
	 * @param params
	 * @return
	 */
	public Map<String, Object> onReplyComment(Map<String, String> params);
	
	
	/**
	 * 获取评论
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> getFileCommentsListByFileId(Map<String, String> params);
	
	
	/**
	 * 获取更多评论
	 * 
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> getMoreReplyComments(Map<String, String> params);
  /**
   * 获得同名文件
   * @param param
   * @return
   */
  public Map<String, String> getRenameFile(Map<String, String> param);
	
	/**
	 * 用户上传头像时候更改此处的头像地址，实时跟用户保持同步
	 * @param params
	 * @return
	 */
	public Map<String, String>  updateFileCommentsUserHeadImg(Map<String,String> params);
	
	/**
	 * 获得最新版本的文件
	 * @param companyId
	 * @param classId
	 * @param fileName
	 * @param fileType
	 * @return
	 */
	public List<Map<String,String>> getFileOfIsLast(String companyId, String classId, String fileName, String fileType);
	
    /**
     * 获取@用户信息
     * @param request
     * @param response
     */
    public Map<String,Object> getCommentMembersByUserId(Map<String,String> map);
    
    /**
     * 根据用户输入参数检索
     * @param params
     * @return 
     */
	public Map<String, Object> queryMembersBykeyWord(Map<String, String> params);
    
    /**
     * 
     * @param companyId
     * @param classId
     * @param fileName
     * @param fileType
     * @return
     */
    public Map<String, String> getFileFirstVersion(String companyId, String classId, String fileName,String fileType, String soleNumber);
     /**
     * 获取文件的分享权限
     * @return
     */
    public List<Map<String, String>> getFileUserRelationByFileId(String companyId, String userId,String fileId ,String groupid);

    /**
     * wangwenshuo add 20160113
     * 	根据序列号获取分享权限
     */
    public List<Map<String, String>> getFileUserRelationBySoleNumber(String companyId, String userId,String soleNumber,String groupId );
    
    /**
     * 获取左侧公司的分组信息
     * @param companyId
     * @return
     */
    public Map<String, Map<String, String>> getLeftCompanyGroups(String companyId, String username, String userId) ;
    
    /**
     * 将文件拖拽到文件夹里面
     * @param params
     * @return
     */
    public Map<String,String> dragFileToDocumnet(Map<String, String> params);
    
    /**
     * xyc将文件拖拽到文件夹里面
     * @param params
     * @return
     */
    public Map<String,String> dragFileToDocumnet2(Map<String, String> params,List<String> ids);
    
    /**
     * xyc 返回单文件存在分类的集合Id
     * @param params
     * @return
     */
    public List<String> getOneFileExistList(String tableName,String classId,String soleNumber);
    
    /**
     * xyc 返回分类文件或文件夹集合
     * @param params
     * @return
     */
    public List<Map<String, String>> getFileOrFolderVersionList(String tableName,String classId,String isDesc,String fileType);
    /**
     * xyc 获取当前分类最大SERIALNUMBER
     * @param params
     * @return
     */
    public String getClassHighestSerialNumber(String tableName,String classId,String isFile);
    
    /**
     * xyc 更新当前分类的序号
     * @param 根据文件名，当前class，文件或文件夹
     * @return true,false
     */
    public String updateClassFileOrFolderSerialnumber(String tableName,String classId,String isFile);
    
    /**
	 * lujixiang 20151210   新增文件删除,并更新版本号
     * @param companyId
     * @param fileId
     * @return 是否删除成功
     */
    public boolean deleteFileAndUpdateVersion(String companyId, String fileId, String userId);
    
    /**
     * xyc 20151216 
     * 
     * */
    public Map<String, String> getFileHighestVersionMap(String tableName,String classId,String filename,String fileType,String updateTime);
    
    /**
     * xyc 20151218
     * 通过指定文件，指定文件ID，获取详细文件
     * */
    public Map<String, String> getFileById(String tableName,String fileId);
    
    /**
     * xiayongcai
     * 更新文件和文件夹拥有者
     * @param 
     * groupsOwnerId：拥有分类的id
     * tableName：表名
     * userIds：用户
     * idSeq：所在分类路径
     */
    public boolean updateGroupsFileAndFolderOwner(String tableName,String groupsOwnerId,List<String> userids,String idSeq);
    /**
     * 获取文件所有字段信息 建索引
     * @param companyId
     * @param fileInsertId
     * @return
     */
    public Map<String, String> getFileForIndex(String companyId, String fileInsertId);
}
