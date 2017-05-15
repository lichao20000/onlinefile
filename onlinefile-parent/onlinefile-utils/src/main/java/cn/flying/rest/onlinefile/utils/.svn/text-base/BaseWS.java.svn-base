package cn.flying.rest.onlinefile.utils;

import org.springframework.stereotype.Component;

import cn.flying.rest.platform.impl.BasePlatformService;

@Component
public class BaseWS extends BasePlatformService {
    
    /**
     * 使用jquery的getJson进行跨域读取时，数据的返回方法
     * @author longjunhao 20150317
     * @param callback 客户端注册的callback，从request中获取
     * @param jsonStr 返回客户端的JSON格式字符串
     * @return
     */
    protected String jsonpCallbackWithString(String callback, String jsonStr) {
        StringBuilder sb = new StringBuilder();
        sb.append(callback).append("(").append(jsonStr).append(")");
        return sb.toString();
    }
    
}
