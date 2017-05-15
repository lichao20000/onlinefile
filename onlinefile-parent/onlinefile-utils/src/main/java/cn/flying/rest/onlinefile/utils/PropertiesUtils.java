package cn.flying.rest.onlinefile.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 读取配置文件
 * @author longjunhao
 *
 */
public class PropertiesUtils {
	
	private static PropertiesUtils propertiesUtils = new PropertiesUtils();
	
	private PropertiesUtils() {
		
	}
	
	public static PropertiesUtils getInstance() {
		return propertiesUtils;
	}
	
	private Map<String, Object> propertiesMap = new HashMap<String, Object>();
	
	/**
	 * 获取配置文件的数据
	 * @author longjunhao 20151010
	 * @return
	 */
	public Map<String, Object> getPropertiesMap() {
		// 配置文件改动后需要重启服务，所以只取一次
		if (propertiesMap.isEmpty()) {
			StringBuffer path = new StringBuffer(getWebInfPath());
			path.append(File.separator);
			path.append("conf");
			path.append(File.separator);
			path.append("config.properties");
			Properties props = new Properties();
			Resource r = new FileSystemResource(path.toString());
			try {
				props = PropertiesLoaderUtils.loadProperties(r);
				Set<Object> keySet = props.keySet();
				if (keySet != null) {
					for (Object object : keySet) {
						propertiesMap.put(String.valueOf(object), props.get(object));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return propertiesMap;
	}
	
	private String getWebInfPath() {
	    String classPath =
	    		new PropertiesUtils().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	    int endPosition = classPath.indexOf("WEB-INF");
	    if (System.getProperty("os.name").contains("Linux") || System.getProperty("os.name").contains("Mac")) {
	      return classPath.substring(0, endPosition + 8);
	    }
	    return classPath.substring(1, endPosition + 8);
	}

}
