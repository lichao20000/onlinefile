package cn.flying.rest.onlinefile.documentclass.driver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DocumentClassDao {

	public List<Map<String,String>> getCateList(Map<String,String> params);
	
	public Map<String,String> addCate(Map<String,String> params);
	
	public Map<String,String> delCate(Map<String,String> params);
	
	public Map<String,String> editCate(Map<String,String> params);
	
	public Map<String,String> getCateById(Map<String,String> params);
	
	public Integer getCateCount();

	/**
	 * 新建分类
	 * @param params
	 * @return
	 */
	public Map<String, String> addClassByName(Map<String, String> params);
	/**
	 * 删除分类
	 * @param params
	 * @return
	 */
	public Map<String, String> deleteClassById(String companyId, String classId);

	/**
	 * 根据分类id修改分类名
	 * @param params
	 * @return
	 */
	public Map<String, String> reClassNameById(Map<String, String> params);
	/**
	 * 获得所有分类
	 * @param params
	 * @return
	 */
	public List<HashMap<String, Object>> gerClassList(Map<String, String> params);

	/**
	 * 让groups表与file_n表产生关系
	 * @param params
	 * @return
	 */
	public Map<String, String> editClassId(Map<String, String> params);
	/**
	 * 判断该分类下是否有文件
	 * @param companyId 
	 * @param params
	 * @return
	 */
	public boolean hasFilesByClassId(String classId, String companyId);

	/**
	 * 创建分组操作
	 * @param companyId 
	 * @param params
	 * @return
	 */
	public boolean createGroup(String companyId, String username,
			String groupuserids, String manageruserid, String groupname,
			String groupremark, String groupflag, String time);

	/**
	 * 查看是否存在该分类名
	 * @param companyId 
	 * @param params
	 * @return
	 */
	public boolean getClassByClassNameAndCompanyId(Map<String, String> params);
	/**
	 * 获得当前分类的创建人
	 * @param companyId 
	 * @param params
	 * @return
	 */
	public String getCreatorByClassId(Map<String, String> params);

	/**
	 * 获得当前分类/分组的详细信息
	 * @param companyId 
	 * @param params
	 * @return
	 */
	public Map<String, String> getClassInfo(Map<String, String> params);
	/**
	 * 更新当前分类/分组的详细信息
	 * @param companyId 
	 * @param params
	 * @return
	 */
	public Map<String, String> editClassInfo(Map<String, String> params);
	/**
     * 分组移交
     * @param request
     * @param response
     */
	public Map<String, Object> changeGroupAdmin(Map<String, String> params);
	
	/**
     * 获取分类群组的FLAG
     * @param classId  分类群组id
     * @return
     */
    public Map<String, String> getFlagWithClassId(String companyId, String classId);
    /**
     * 查出改文件夹下所有文件的id
     * @param classId  分类群组id
     * @return
     */
	public String getChildByClassId(String companyId, String folderId);
	/**
     * 匹配seq删除
	 * @param companyId 
	 * @param classId 
     * @param 
     * @return
     */
	public boolean deleteClassByIdSeq(String idsql, String companyId, String classId);
	/**
     * 查看这个文件夹名字是否存在
	 * @param  
     * @param 
     * @return
     */
	public boolean getCheckClassNameIsIn(Map<String, String> params);
	
	public List<String> getIdsByIdSeq(String idseq,String companyId);
	
	public boolean updateDelById(List<String> ids,String companyId,String isDel);
	/**
     * 删除回收站中的数据 
	 * @param  
     * @param 
     * @return
     */
	public boolean deleteHuiShouZhan(String companyId, String userId,
			String userName, List<String> ids);
	
	/**
	 * 判断是否包含除自己以外成员
	 * */
	public boolean hasUsersByClassId(String classId,String companyId,String userId);
	
}
