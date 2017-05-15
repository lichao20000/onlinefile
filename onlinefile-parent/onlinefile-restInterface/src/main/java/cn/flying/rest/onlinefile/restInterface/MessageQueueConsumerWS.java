package cn.flying.rest.onlinefile.restInterface;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import cn.flying.rest.platform.utils.MediaTypeEx;

/**
 * @author LiuKang   2015/9/15
 * @since FlyingSoft
 * @version 1.0
 * Apache MQ 消费者者服务接口
 * */
public interface MessageQueueConsumerWS {
	
	//用于后台不经过REST进行直接调用
	public void doReceiver(String getQueue);
	
	/**
	 * 消费者方法的接口 主要用于执行操作
	 * */
	@GET
	@Path("doReceiver/{getQueue}")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public boolean doUrlReceiver(@PathParam("getQueue")String getQueue);
	
	public void doIndexReceiver(String getQueue);

}
