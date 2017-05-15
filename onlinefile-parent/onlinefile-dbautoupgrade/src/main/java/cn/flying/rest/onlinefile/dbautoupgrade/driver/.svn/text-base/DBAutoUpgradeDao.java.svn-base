package cn.flying.rest.onlinefile.dbautoupgrade.driver;



public interface DBAutoUpgradeDao{

	/**
	 * 获得数据库的版本号
	 * @return
	 */
	
	public int getVersion();

	/**
	 * 从xml里遍历来的sql插入到数据库
	 * @return
	 */
	public StringBuffer dostart(String sql,String type, int version,String logPath);

	/**
	 * 修改数据库的版本号
	 * @return
	 */
	public void updateVersion(String editionResult);
	/**
	 * 判断是否存在appconfig  不存在就创建
	 * @return
	 */
	public void checkApp_Config();
}
