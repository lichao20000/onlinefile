package cn.flying.rest.onlinefile.restInterface;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import cn.flying.rest.platform.utils.MediaTypeEx;

/**
 * @author LiuKang   2015/9/15
 * @since FlyingSoft
 * @version 1.0
 * Apache MQ 生产者服务接口
 * */
public interface MessageQueueProducerWS {
	
	@GET
	@Path("doSender/{setQueue}/{json2Map}")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public boolean doSender(@PathParam("setQueue")String setQueue , @PathParam("json2Map")String json2Map);

	//仅供后台使用，不经过REST
	public void doSender(String setQueue, Map<String, String> params);
	
	public boolean doIndexSender(String setQueue, Map<String, Object> params);
	
}
