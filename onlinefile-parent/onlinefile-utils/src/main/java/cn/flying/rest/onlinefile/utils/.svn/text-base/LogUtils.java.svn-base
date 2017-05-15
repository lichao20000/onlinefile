package cn.flying.rest.onlinefile.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.flying.rest.admin.entity.AuditLog;
import cn.flying.rest.admin.restInterface.BaseCacheWS;
import cn.flying.rest.admin.restInterface.BaseLogService;
import cn.flying.rest.platform.IServiceProvider;

public class LogUtils {
	private static BaseLogService logWS;

	private static BaseLogService getLogWS(IServiceProvider compLocator) {
		if (logWS == null) {
			synchronized (BaseCacheWS.class) {
				if (logWS == null) {
					logWS = compLocator.findService(BaseLogService.class);
				}
			}
		}
		return logWS;
	}

	/**
	 * 添加登录日志
	 * 
	 * @param log
	 * @return
	 */
	public static Boolean saveAuditLog(IServiceProvider compLocator, HashMap map) {
		return getLogWS(compLocator).saveAuditLog(map);
	}

	/**
	 * 添加模块日志
	 * 
	 * @param map
	 * @return
	 */
	public static Boolean saveBaseLog(IServiceProvider compLocator,
			HashMap<String, String> map) {
		return getLogWS(compLocator).saveBaseLog(map);
	}

	/**
	 * 获取日志总数
	 * 
	 * @param map
	 * @return
	 */
	public static String getAuditLogCount(IServiceProvider compLocator, HashMap map) {
		return getLogWS(compLocator).getAuditLogCount(map);
	}

	/**
	 * 通过ids获取日志s
	 * 
	 * @param ids
	 * @return
	 */
	public static List<AuditLog> getAuditLogByIds(IServiceProvider compLocator,
			String ids) {
		return getLogWS(compLocator).getAuditLogByIds(ids);
	}

	/**
	 * 得到日志列表
	 * 
	 * @param start
	 *            开始值
	 * @param limit
	 *            多少数据
	 * @param keyWord
	 *            关键字
	 * @param sort
	 *            排序
	 * @param type
	 *            获取那些日志（用户登录-1、功能访问-2、功能操作-3、任务调度-4、数据接口-5 ）
	 * @return
	 */
	public static List<AuditLog> getAuditLogs(IServiceProvider compLocator, HashMap map) {
		return getLogWS(compLocator).getAuditLogs(map);
	}

	/**
	 * 通过id获取日志信息
	 * 
	 * @param id
	 * @return
	 */
	public static AuditLog getAuditLog(IServiceProvider compLocator, int id) {
		return getLogWS(compLocator).getAuditLog(id);
	}

	/**
	 * 通过条件获取日志信息
	 * 
	 * @param start
	 * @param limit
	 * @param condition
	 *            ★■◆● ●◆■★ 分割（★■◆●"loginfo"●◆■★"="●◆■★"3"★■◆●）
	 * @param type
	 * @return
	 */
	public static List<AuditLog> getAuditLogByCondition(IServiceProvider compLocator,
			HashMap map) {
		return getLogWS(compLocator).getAuditLogByCondition(map);
	}

	/**
	 * 删除日志
	 * 
	 * @param ids
	 * @return
	 */
	public static Boolean deleteAuditLogByIds(IServiceProvider compLocator, String ids) {
		return getLogWS(compLocator).deleteAuditLogByIds(ids);

	}

	/**
	 * 通过条件删除日志（如果没有条件，则删除全部日志）
	 * 
	 * @param condition
	 * @return
	 */
	public static Boolean deleteAuditLogByCondition(IServiceProvider compLocator,
			HashMap map) {
		return getLogWS(compLocator).deleteAuditLogByCondition(map);
	}

	/**
	 * 得到统计的总数 map -> type condition keyword
	 * 
	 * @param map
	 * @return
	 */
	public static String getStatisticData(IServiceProvider compLocator,
			Map<String, Object> map) {
		return getLogWS(compLocator).getStatisticData(map);
	}
	
	/**
	 * 获取登陆日志
	 * @param compLocator
	 * @param map
	 * @return
	 */
	public static List<Map<String,String>> getLoginLog(IServiceProvider compLocator,Map<String,String> map) {
		return getLogWS(compLocator).getLoginLogs(map);
	}
	/**
	 * 获取登陆日志总数
	 * @param compLocator
	 * @param map
	 * @return
	 */
	public static String getLoginLogCount(IServiceProvider compLocator,Map<String,String> map) {
		return getLogWS(compLocator).getLoginLogCount(map);
	}
	/**
	 * 获取全部安全日志
	 * @param compLocator
	 * @param map
	 * @return
	 */
	public static List<Map<String,String>> getSafetyLog(IServiceProvider compLocator,Map<String,String> map) {
		return getLogWS(compLocator).getSafetyLogs(map);
	}
	
	/**
	 * 获取安全日志总数
	 * @param compLocator
	 * @param map
	 * @return
	 */
	public static String getsafetyLogCount(IServiceProvider compLocator,Map<String,String> map) {
		return getLogWS(compLocator).getSafetyLogCount(map);
	}

}
