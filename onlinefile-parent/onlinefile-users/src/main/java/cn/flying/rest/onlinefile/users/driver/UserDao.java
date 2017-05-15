package cn.flying.rest.onlinefile.users.driver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.flying.rest.platform.IServiceProvider;

public interface UserDao {
	
	public Integer saveUser(Map<String,String> params);
	
	public boolean updateFirstLoginFlag(String id);
	
	public HashMap<String,List<String>> getNotFirLoginWithCompany();
	
	/**
	 * 获取企业用户（id, userName, fullName）
	 * @param companyId
	 */
	public Map<String, Map<String, String>> getUserInitInfo(String companyId);
	
	public Map<String, Map<String, String>> getUserSingleSet(String companyId);
	
	public Map<String, String> getUserInitInfo(String companyId, String userName);
	
	
	public List<Map<String, String>> getCompanyMembers(String companyId);
	/**
	 * 管理员添加用户
	 * @param param
	 * @return 
	 */
	public int adduser(Map<String, Object> param);
	/**
	* 根据Userid查询个性设置
	 * @param param
	 * @return 
	 */
	public Map<String, String> getSingleSet(Map<String, String> param);
	/** 
	 * 设置用户个性信息
	 * @param params
	 * @return
	 */
	public boolean updateSingleSet(Map<String, String> params);
	/** 
	 * 新增个性设置
	 * @param params
	 * @return
	 */
	public boolean addSingleSet(Map<String, String> params);
	/**
	 * 完善/修改信息
	 * @param param
	 * @return 
	 */
	public boolean editUserInfo(Map<String, String> params);
	/**
	 * 根据id查密码
	 * @param param
	 * @return 
	 */
	public String getPassWordById(String string);
	/**
	 * 根据id修改密码
	 * @param param
	 * @return 
	 */
	public boolean modifyPassword(String newPassword, String string);
	/**
	 * 根据用户id查询当前公司下机构的集合
	 * @param param
	 * @return 
	 */
	public List<HashMap<String, Object>> getOrgByUserId(Map<String, String> params);
	/**
	 * 根据机构名字获得机构id
	 * @param param
	 * @return 
	 */
	public int getOrgIdByOrgName(String orgName);
	/**
	 * 根据username获得user的id
	 * @param param
	 * @return 
	 */
	public int getUserIdByUserName(String userName);
	/**
	 * 根据username获得所在公司的id
	 * @param param
	 * @return 
	 */
	public int getcompanyIdByUserName(String id);
	/**
	 * 保存用户id和机构id在关联表
	 * @param param
	 * @return 
	 */
	public boolean saveUserIdAndOrgId(int userId,int orgId);
	/**
	 * 根据用户名查询该用户名的code
	 * @param param
	 * @return 
	 */
	public String getCodeById(String id);
	/**
	 * 根据公司id查询公司名称
	 * @param param
	 * @return 
	 */
	public String getCompanyNameById(String companyid);
	/**
	 * 真正激活用户
	 * @param param
	 * @return 
	 */
	public boolean activateAccount(Map<String, String> params);
	
	/**
	 * 根据username获得详细信息
	 * @param param
	 * @return 
	 */
	public Map<String, Object> getUserInfoByUserName(String username);
	
	/**
	 * 根据公司id获得当前公司下所有的
	 * @param limit 
	 * @param stratNo 
	 * @param param
	 * @return 
	 */
	public List<HashMap<String, Object>> getUserListByCompanyId(int companyId, int stratNo, int limit);
	/**
	 * 根据公司id和用户名获得当前公司下所有的用户(模糊或者精确查询)
	 * @param limit 
	 * @param selectUserFlg 
	 * @param userName 
	 * @param stratNo 
	 * @param param
	 * @return 
	 */
	public List<HashMap<String, Object>> getUserListByCompanyIdAndUserName(int companyId,String userName,int stratNo, int limit);
	/**
	 * 根据公司id获得当前公司下所有的人的数量
	 * @param param
	 * @return 
	 */
	public int getCountAllByCompanyId(Map<String, String> params);
	/**
	 * 根据username删除用户
	 * @param param
	 * @return 
	 */
	public boolean deleteUserByUserId(String[] usernames);
	/**
	 * 如果是管理员输入的机构 那么保存这个名字并且返回这个的id
	 * @param companyid 
	 * @param period 
	 * @param param
	 * @return 
	 */
	public int saveOrgByOrgNameReturnOrgId(String period, int companyid);
	/**
	 * 根据orgid获得org详情
	 * @param companyid 
	 * @param period 
	 * @param param
	 * @return 
	 */
	public String getOrgByOrgId(int newOrgId);
	
	/**
	 * xiaoxiong 20150402 
	 * 修改用户状态 
	 * 		-3:正在发送中
	 * 		-2:邮件发送失败
	 * 		-1:邮件已发送，还未激活
	 * 		0:禁用
	 * 		1:启用
	 * @param ids 
	 * @param int
	 */
	public boolean updateStatus(List<Integer> ids, int status);
	
	/**
	 * 更新一下用户的订阅信息状态
	 * @param ids
	 * @return 返回更新结果
	 */
	public boolean updateUserSubScribeStatus(List<Integer> ids);

	/**
	 * xiaoxiong 20150408
	 * 根据用户ID获取用户信息
	 * @param id
	 * @return
	 */
	public HashMap<String, String> getUserById(String id);
	/**
	 * liumingchao
	 * 修改管理员字段(企业注册)
	 * @param id
	 * @return
	 */
	public boolean updateUser(Map<String, String> onlinefileUser);

	public int saveRealUser(Map<String, String> params);
	/**
	 * liumingchao
	 * 保存头像地址
	 * @param id
	 * @return
	 */
	public boolean saveHeadImage(Map<String, String> params);
	/**
	 * liumingchao
	 * 查询用户的头像path
	 * @param id
	 * @return
	 */
	public Map<String, String> getImagePath(Map<String, String> params);
	
	/**
	 * 根据userid获得详细信息
	 * @param param
	 * @return 
	 */
	public HashMap<String, Object> getUserInfoByUserId(Integer id);
	/**
	 * 修改用户信息
	 * @param param
	 * @return 
	 */
	public boolean editUser(Map<String, Object> param);
	
	/**
     * 验证邮箱是否激活</br>longjunhao 20150818 修改逻辑, 验证用户-企业是否激活
     * 
     * @param companyId 企业id
     * @param emailStr 邮箱
     * 
     * @return 
     *     firstInvite: users表里没有记录，第一次被邀请使用；</br>
     *     active：     用户已激活</br>
     *     nonactive:   用户未激活</br>
     *     newInvite:   用户已经在某个company中，但不在参数companyId对应的company中。
     */
	public HashMap<String, Object> verifyMailbox(String companyId, String emailStr);
	/**
	 * 通过邮箱查询userID
	 * @param param
	 * @return 
	 */
	public String getUserId(String email);
	
	/**
	 * 导入用户
	 * @param param
	 * @return 
	 */
	public int importUser(Map<String, Object> param);

	/**
	 * 根据用户的表id删除用户
	 * @param param
	 * @return 
	 */
	public boolean deleteUserById(String[] ids);

	/**
	 * 获取 一个企业的所有部门
	 * @param companyId
	 * @return
	 */
	public Map<String, String> getOrgInfo(String companyId);
	/**
	 * 根据公司ID、用户EMAIL
	 * @param companyId，EMAIL
	 * @return 公司信息
	 */
	public Map<String, String> getCompanyUserStatus(String companyId,String EMAIL);

	/**
	 * 根据用户ID获取一个用户的信息
	 * @param id
	 * @return
	 */
	public Map<String, String> getOneUserInfo(String id);
	/**
	 * 根据orgid修改orgname
	 * @param id
	 * @return
	 */
	public boolean modifyOrg(String orgid, String orgname);
	/**
	 * 根据username获取该用户的被邀请时间
	 * @param id
	 * @return
	 */
	public String getCreateTime(Map<String, String> params);
	/**
	 * 修改激活时间  
	 * @param id
	 * @return
	 */
	public boolean changeCreateTime(Map<String, String> params);
	/**
	 * 根据用户名和邮箱检验用户是否存在  （忘记密码操作中用到）
	 * @param id
	 * @return
	 */
	public Map<String, Object> checkUserExistByEmailAndUserName(Map<String, Object> params);
	/**
	 * 获得链接/插入code
	 * @param id
	 * @return
	 */
	public int saveUserJustCode(Map<String, String> map);
	/**
	 * 插入GroupUsersRalation表
	 * @param map 
	 * @param id
	 * @return
	 */
	public boolean insertIntoGroupUsersRalationTable(Map<String, String> map);
	
	/**
	 * 保存修改密码获取的验证码
	 * @param id
	 * @param checktime
	 * @param checkcode
	 * @return
	 */
	public boolean saveCheckCode(String id,String checktime,String checkcode);
	/**
     * 获取某个群组管理员的信息(申请加入分组时，给管理员发送消息 )
     * @param request
     * @param response
     */
	public Map<String, String> getUserByUserId(String userId);
	/**
	 * 检验密码是否正确，用来移交管理员的
	 * @param request
	 * @param response
	 */
	public boolean checkPasswordIsRight(String username, String userid,
			String md5password);
	
	/**
	 * 通过用户账号删除该用户的信息订阅
	 * @param map
	 * @return 返回删除结果
	 */
	public Map<String, String> deleteUserSubScribersByUserNames(Map<String,String> map);
	
	/**
	 * 获取企业-用户的状态
	 * @param companyId
	 * @param email
	 * @return
	 */
	public Map<String,String> getUserStatusByEmail(String companyId, String email);

	public boolean saveCompanyUsers(Map<String, String> user);

	/**
	 * 缓存一个用户属于的企业列表
	 * @return
	 */
	public void cacheUserToCompanys(IServiceProvider compLocator);
	
	
	/**
	 * 通过帐号跟密码登录获取相关数据
	 *  @param name(id),password
	 *  @return 获取当前用户的详细信息
	 *  20151012 xiayongcai 并获取所属公司信息
	 * */
	public Map<String, String> LoginGetUserParticular(Map<String,String> map);
	/**
	 * 插入company_user表来自于分类邀请
	 *  @param 
	 *  @return 
	 * */
	public boolean saveCompanyUserTableForClass(String companyid, String classId,String userDeId);
	/**
	 * 插入company_user表来自于分类邀请来自公司邀请
	 * @param companyid
	 * @param classId
	 * @param userDeId
	 * @return
	 */
	public boolean saveCompanyUserTableForCompany(String companyid,String userDeId);
	
	
	/**
	 * 同意加入团队，把classid变0就行
	 * @param request
	 * @param response
	 */
	public boolean agreenInSideCompany(String companyid, String userId);
	/**
	 * 拒绝加入团队，删除字段
	 * @param request
	 * @param response
	 */
	public boolean noAgreenInSideCompany(String companyId, String userId);
	
	/**
	 * 分类下邀请判断compnany_users表中是否已存在相同的数据
	 * @param companyId
	 * @param classId
	 * @param userId 
	 * @return 存在 : true 不存在false;
	 */
	public Map<String, String> isexistNotes(String companyId, String classId, String userId);
	
	/**
	 * 插入company_user表前看看是否已经有了
	 *  @param 
	 *  @return 
	 * */
	public boolean checkCompany_UserIsIn(int companyid, String userDeId,
			String userDeId2);

	/**
	 * 登录时获取相对应的class
	 *  @param 
	 *  @return 
	 * */
	public List<Map<String, String>> getClassList(String string);
	/**
	 * 查找user是否已经被邀请
	 *  @param 
	 *  @return 
	 * */
	public boolean checkUserIsInCompany_User(int companyid, String userId);
    /**
     * 获得所有users
     * @param param
     * @return
     */
    public List<Map<String,String>> getUsersByFullName(Map<String, Object> param);
    
    /**
     * 修改企业用户状态 
     *      -3:正在发送中
     *      -2:邮件发送失败
     *      -1:邮件已发送，还未激活
     *      0:禁用
     *      1:启用
     * @author longjunhao 20150818
     * @param ids 
     * @param int
     */
    public boolean updateCompanyUserStatus(String companyId, List<Integer> ids, int status);
    /**
     * xiewenda 
     * 更具用户名和公司id获取用户
     * @param companyId
     * @param email
     * @return
     */
    public Map<String, Object> getUserByUserNameAndCompanId(String email,int companyId);
    /**
     * 根据用户名查找所有公司
     * @param username
     * @return
     */
    public List<Map<String,String>> getCompanysByUserName(String username);
    /**
     * 根据用户名查找所有公司
     * @param username
     * @return 企业id,企业名称，企业状态，企业标示
     */
    public List<Map<String,String>> getCompanysByUserNameList(String username);
	/**
	 * 不同公司之间邀请  往invitedetail(邀请详细表)中插入邀请详细信息
	 * @param companyId
	 * @param userId
	 * @param groupId
	 * @param classId
	 * @param groupflag
	 */
	public void saveInviteDetail(String companyId, String userId,
			String groupId, String classId, String groupflag);
	/**
	 * 查看是否已经发送过了
	 * @param companyId
	 * @param userId
	 * @param groupId
	 * @param classId
	 * @param groupflag
	 * @return
	 */
	public boolean checkInvitedetailIsHave(String companyId, String userId,
			String groupId, String classId, String groupflag);
	/**
	 * 获得所有邀请明细
	 * @param userid
	 * @param companid
	 * @return
	 */
	public List<HashMap<String, String>> getInviteDetail(String userid,
			String companid);
	/**
	 * 删除已经操作过的邀请
	 * @param companyId
	 * @param userId
	 * @param string
	 */
	public void deleteInvite(String companyId, String userId);
	
	/**
	 * 20151015 xiayongcai
	 * 根据userId 
	 * 验证用户是否已经注册过企业
	 * */
	public boolean isUserCompayExist(String userId);
	
	/**
	 * liuhezeng
	 * 查询用户的头像path
	 * @param id
	 * @return
	 */
	public String getUserImagePathByUserId(String userId);

	/**
	 * wangwenshuo 20151119 用户登录后检测是否有用户表（files_user_N，files_trash_user_N等），没有就创建
	 * @return  不存在 创建表 返回true   存在返回false
	 */
	public boolean createUserTables(String userId);
	/**
	 * 更新回车发送
	 * @param params
	 */
	public boolean updateCommentEnterSet(Map<String, String> params);
	/**
	 * 天添加回车发送
	 * @param params
	 * @return
	 */
	public boolean addCommentEnterSet(Map<String, String> params);
	/**
	 * 分类下邀请添加用户
	 * @param param
	 * @return
	 */
	public int  addClassUser(Map<String, String> param);
	/**
	 * 用户管理下邀请公司用户
	 * @param param
	 * @return
	 */
	public int addCompanyUser(Map<String, String> param);

	/**
	 * 获取company_users表中是否已经存在该公司
	 * @param userId
	 * @param companyId
	 */
	public Map<String,String> existCompanyForCompanyUserTable(String userId, String companyId);
	/**
	 * 查询同同个公司不同分类下邀请是否存在成员
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public Map<String, String> getDifferentClassForCompany(String userId,String companyId);
	
	/**
	 * 获取compnay_users表中的所有信息
	 * @param userId
	 * @param companyId
	 * @return
	 */
	public Map<String, String> getCompanyUserInfoByUserIdAndCompanyId(String userId, String companyId);
    /**
     * 判断邀请的邮箱在分类下的状态
     * @param companyId
     * @param emailParam
     * @param classId
     * @return
     */
    public HashMap<String, Object> verifyMailboxForClass(String companyId, String emailParam,
      String classId);
    
    public Map<String,String> getUserInfoByEmail(String emailParam);

    public boolean registerUser(Map<String, String> param);

    public boolean userLogin(Map<String, String> param);

    public boolean isExitsUserName(Map<String, String> param);
}
