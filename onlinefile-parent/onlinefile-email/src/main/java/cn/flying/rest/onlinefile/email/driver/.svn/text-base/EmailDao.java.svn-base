package cn.flying.rest.onlinefile.email.driver;

import java.util.List;
import java.util.Map;

import cn.flying.rest.onlinefile.entity.EmailAttachment;
import cn.flying.rest.onlinefile.entity.EmailEntity;

public interface EmailDao {
	/**
	 * 添加邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, String> addEmail(Map<String, String> map);
	
	/**
	 * 手动添加邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, String> addEmailManual(Map<String, String> map);

	/**
	 * 删除邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, String> deleteEmail(Map<String, String> map);
	
	/**
	 * 清空邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, String> deleteAllEmail(Map<String, String> map);

	/**
	 * 修改邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, String> updateEmail(Map<String, String> map);

	/**
	 * 获取所有邮箱地址
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public List<Map<String,String>> getEmailList(Map<String, String> map);
	
	/**
	 * 查询邮箱附件
	 * 
	 * @author liuhezeng 20150403
	 * @param 参数
	 * @return
	 */
	public Map<String, Object> searchEmailAttachment(Map<String, String> map);


	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, Object> getAttachmentByEmail(Map<String, String> map);
	
	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, Object> getDefaultAttachmentByEmail(Map<String, String> map);
	
	
	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public List<Map<String, String>> getAllAttachments(Map<String, String> map);
	
	
	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, String> getEmailSettingByEmail(Map<String, String> map);
	
	
	/**
	 * 保存邮箱设置
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, String> saveEmailSetting(Map<String, String> map);
	
	/**
	 * 保存默认邮箱
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public Map<String, String> setAsDefaultEmail(Map<String, String> map);
	
	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public List<Map<String, String>> cacheAllEmailAttachments(Map<String, String> map) throws Exception ;
	
	
	/**
	 * 根据邮箱地址获取邮箱附件
	 * 
	 * @author liuhezeng 20150304
	 * @param 参数
	 * @return
	 */
	public List<Map<String, String>> cacheDefaultAllEmailAttachments(Map<String, String> map);
	
	
	/**
	 * 是否同步邮箱
	 * 
	 * @author liuhezeng 20150414
	 * @param 参数
	 * @return
	 */
	
	public Map<String, String> isSynEmailAttachMent(Map<String, String> map);
	
	/**
	 * 根据邮箱获取该邮箱的邮件内容
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmailAttachMentInfoByEmail(Map<String, String> map);
	
	/**
	 * 根据邮箱获取该邮箱的邮件附件
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmailAttachMentsByEmail(Map<String, String> map);

	
	/**
	 * 根据邮箱获取该邮箱的内部的缓存
	 * @param map
	 * @return
	 */
	public String getDefaultEmail(Map<String, String> map);
	

	/**
	 * 附件下载到服务器
	 * @param map
	 * @return 返回下载结果
	 */
	public List<String> downloadAttachMent(Map<String, String> map);
	
}
