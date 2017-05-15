package cn.flying.rest.onlinefile.company.driver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CompanyDao {

	public Integer saveCompany(Map<String,String> params);

	public void createTables(int companyId);

	public void saveCompanyReferrer(int companyId, String string);

	public Boolean validateUrl(String start);
	
	/**
	 * 获取推荐信息
	 * @param params
	 * @return
	 */
	public List<Map<String,String>> getCompanyReferrer(Map<String,String> params);
	
	/**
	 * 获取推荐信息总数
	 * @param params
	 * @return
	 */
	public int getCompanyReferrerCount(String refferrer);
	
	
	/**
	 * luiwei 20150518
	 * 根據公司id獲取公司詳細信息
	 * @param id 公司id
	 * @return 
	 */
	public HashMap<String, Object> getCampanyInfoById(Integer id);
	/**
	 * 获取所有公司信息
	 * @return
	 */
	public Map<String, Map<String, String>> getAllCompanyInfo();
	
	/**luiwei 20150518
	 * 修改公司信息
	 * @param param
	 * @return 
	 */
	public boolean editCampanyInfo(Map<String, String> params);
	
	/**
	 * liuwei20150518
	 * 查询用户的头像path
	 * @param id
	 * @return
	 */
//	public Map<String, String> getImagePath(Map<String, String> params);
	
	/**
	 * liuwei20150518
	 * 保存头像地址
	 * @param id
	 * @return
	 */
	public boolean saveHeadImage(Map<String, String> params);
	
	/**
	 * 初始化根数据
	 * @param filesN
	 * @return
	 */
	public boolean insertIntoFilesN(String filesN);
	
	/**
	 * xiayongcai
	 * 判断企业是否存在
	 * @param compayName
	 * */
	public boolean isCompayNameExist(String compayName);
	/**
	 * 20151020
	 * xiayongcai
	 * 判断企业用户是否存在
	 * @param companyId,userId
	 * */
	public Map<String, String> isCompayUserExist(String compayId,String userId);
	/**
	 * 20151027 注销公司
	 * @param param
	 * @return
	 */
	public boolean cancelCompany(Map<String, String> param);
	
	/**
	 * 20151028
	 * xiayongcai
	 * 根据企业名称获取企业信息
	 * */
	public List<Map<String,String>> getCompayByCompayName(String compayName);
  /*
   * 移交公司
   */
  public boolean transferCompany(Map<String, String> param);
	
	
}
