package cn.flying.rest.onlinefile.utils;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Map工具类
 * 
 * @author liuhezeng 20151022
 * 
 */
public class MapUtil {

  /**
   * 从map里获取Object数据
   * 
   * @param map
   * @param key
   * @return
   */
  public static Object getObject(Map<String, Object> map, String key) {
    return map.get(key);
  }

  /**
   * 从map里获取int数据
   * 
   * @param map
   * @param key
   * @return
   */
  public static Integer getInt(Map<String, Object> map, String key) {
    String object = getString(map, key);
    if (object != null) {
      return Integer.parseInt(object);
    } else {
      return 0;
    }
  }

  /**
   * 从map里获取long数据
   * 
   * @param map
   * @param key
   * @return
   */
  public static Long getLong(Map<String, Object> map, String key) {
    String object = getString(map, key);
    if (object != null) {
      return Long.parseLong(object);
    } else {
      return 0L;
    }
  }

  /**
   * 从map里获取字符串
   * 
   * @param map
   * @param key
   * @return
   */
  public static String getString(Map<String, Object> map, String key) {
    Object object = getObject(map, key);
    if (object != null) {
      return object.toString();
    } else {
      return null;
    }
  }

  public static Map<String, String> parseData(String data) {
    GsonBuilder gb = new GsonBuilder();
    Gson g = gb.create();
    Map<String, String> map = g.fromJson(data, new TypeToken<Map<String, String>>() {}.getType());
    return map;
  }

  public static <T> String mapToJson(Map<String, T> map) {
    Gson gson = new Gson();
    String jsonStr = gson.toJson(map);
    return jsonStr;
  }
}
