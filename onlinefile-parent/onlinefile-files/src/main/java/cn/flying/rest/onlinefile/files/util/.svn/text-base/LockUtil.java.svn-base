package cn.flying.rest.onlinefile.files.util;

import java.util.concurrent.Semaphore;

import cn.flying.rest.onlinefile.files.lock.LockMap;

/**
 * lujixiang 20151216 请求锁资源工具类
 * @author Administrator
 *
 */
public class LockUtil {
	
	private static LockMap lockMap ;
	
	static{
		lockMap = new LockMap();
	}
	
	/** 获取锁资源 **/
	public static void acquire(String key) throws InterruptedException{
		
		Semaphore lock = lockMap.getLock(key);		
		lock.acquire();
	}
	
	public static void release(String key) throws InterruptedException {
		lockMap.removeLock(key);
	}

}
