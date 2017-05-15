package cn.flying.rest.onlinefile.apk.driver;

import java.util.Map;


/**
 * 数据层接口
 * @author Administrator
 *
 */
public interface AppVersionDao {
	/**
	 * 保存更新信息
	 * @param appUpdateInfo
	 * @return 
	 */
	public boolean saveUpdateInfo();
	/**
	 * 获取最近更新数据
	 * @return 
	 * 
	 */
	public Map<String, Object> getAppLatestUpdateInfo();
	/**
	 * 更新数据
	 * @return
	 */
	public boolean updateAppInfo();
	
	

}
