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
 * Apache MQ 文件消费者者服务接口
 * */
public interface MessageQueueConsumerFileWS {
	
	//用于后台直接调度，不经过REST接口
	public void doReceiver(String getQueue);
	
	/**
	 * 文件消费者方法的接口 主要用于文件执行操作
	 * */
	@GET
	@Path("doFileReceiver/{getQueue}")
	@Produces(MediaTypeEx.TEXT_PLAIN_UTF8)
	public boolean doUrlReceiver(@PathParam("getQueue")String getQueue);
	
	public boolean doIndexReceiver(String getQueue);

}
