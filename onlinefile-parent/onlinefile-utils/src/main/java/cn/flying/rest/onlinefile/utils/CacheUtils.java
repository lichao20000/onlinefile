package cn.flying.rest.onlinefile.utils;

import java.util.List;
import java.util.Map;

import cn.flying.rest.admin.restInterface.BaseCacheWS;
import cn.flying.rest.platform.IServiceProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class CacheUtils {

	private static BaseCacheWS cacheWS ;
	
	private static BaseCacheWS getCacheWS(IServiceProvider compLocator) {
		if (cacheWS == null) {
			synchronized (BaseCacheWS.class) {
				if (cacheWS == null) {
					cacheWS = compLocator.findService(BaseCacheWS.class);
				}
			}
		}
		return cacheWS;
	}
	
	/***
	 * 获取缓存对象
	 * @param compLocator
	 * @param key
	 * @return
	 */
	public static Object get(IServiceProvider compLocator, String key){
		String value = getCacheWS(compLocator).get(key) ;
		if(value != null && !"".equals(value)){
			return JSON.parse(value) ;
		}
		return null ;
	}
	
	/**
	 * 获取缓存对象String
	 * @author longjunhao 20150415
	 * @param compLocator
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static List<Map<String,String>> getList(IServiceProvider compLocator, String key){
        String value = getCacheWS(compLocator).get(key) ;
        if(value != null && !"".equals(value)){
            return JSON.parseObject(value, new TypeReference<List<Map<String,String>>>(){});
        }
        return null ;
    }
	
	/**
	 * 保存缓存对象
	 * @param compLocator
	 * @param key
	 * @param obj
	 */
	public static void set(IServiceProvider compLocator, String key, Object obj){
		getCacheWS(compLocator).set(key, JSON.toJSONString(obj, SerializerFeature.WriteClassName)) ;
	}
	
	/**
	 * 删除缓存对象
	 * @param compLocator
	 * @param key
	 */
	public static void delete(IServiceProvider compLocator, String key){
		getCacheWS(compLocator).delete(key) ;
	}
}
