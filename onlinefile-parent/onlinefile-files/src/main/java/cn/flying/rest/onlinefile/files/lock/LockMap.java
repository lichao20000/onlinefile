package cn.flying.rest.onlinefile.files.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * lujixiang 20151215 封装通用的获取信号量和删除信号量Map,获取、添加、删除信号量需要互斥
 */
public class LockMap {
	
	private Semaphore methodLock ;	// getFileLock和removeLock互斥
	private Map<String, Semaphore> lockMap ;
	
	public LockMap(){
		
		methodLock = new Semaphore(1) ;
		lockMap = new HashMap<String, Semaphore>() ;
	}
	
	/** 获取信号量 **/
	public Semaphore getLock(String key) throws InterruptedException{
		
		methodLock.acquire() ;
		
		Semaphore lock = lockMap.get(key) ;
		if (null == lock ) {
			lock = new Semaphore(1) ;
			lockMap.put(key, lock);
		}
		
		methodLock.release() ;
		
		return lock ;
	}
	
	/** 释放信号量 **/
	public void removeLock(String key) throws InterruptedException{
		
		methodLock.acquire() ; 
		
		Semaphore lock = lockMap.get(key) ;
		
		if(null != lock && false == lock.hasQueuedThreads()){
			lockMap.remove(key) ;
		}
		if(null != lock){
			lock.release();
		}
		methodLock.release() ;
	}
	
	/** 添加信号量 
	 * @throws InterruptedException **/
	public void addLock(String key, Semaphore lock) throws InterruptedException{
		
		methodLock.acquire() ;
		
		lockMap.put(key, lock);
		
		methodLock.release() ;
	}
	

}
