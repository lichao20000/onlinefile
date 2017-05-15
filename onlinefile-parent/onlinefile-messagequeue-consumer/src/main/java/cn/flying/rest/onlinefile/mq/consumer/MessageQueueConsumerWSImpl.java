package cn.flying.rest.onlinefile.mq.consumer;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.flying.rest.onlinefile.mq.consumer.util.ConsumerFactoryUtil;
import cn.flying.rest.onlinefile.restInterface.ChatWS;
import cn.flying.rest.onlinefile.restInterface.LuceneWS;
import cn.flying.rest.onlinefile.restInterface.MessageQueueConsumerWS;
import cn.flying.rest.onlinefile.restInterface.MessageQueueProducerWS;
import cn.flying.rest.onlinefile.utils.BaseWS;

@Path("/onlinefile_messagequeue_consumer")
@Component
public class MessageQueueConsumerWSImpl extends BaseWS implements
		MessageQueueConsumerWS {

	
	@Value("${onlinefile.imageutil.groupImagePath}")
	private String imageAfterPath ;
	
	/**
	 * 创建chat服务的实例
	 * */
	private ChatWS chatWS;
	
	public ChatWS getChatWS() {
		if (chatWS == null) {
			synchronized (MessageQueueProducerWS.class) {
				if (chatWS == null) {
					chatWS = this.getService(ChatWS.class);
				}
			}
		}
		return chatWS;
	}
	private LuceneWS lucenews;
    private LuceneWS getLuceneWS(){
        if(lucenews == null){
            lucenews = this.getService(LuceneWS.class);
        }
        return lucenews;
    }
	/**
	 * 将Consumer对象工厂注入进来
	 * */
	@Autowired
	ConsumerFactoryUtil factoryUtil;
	
	/**
	 * @author LiuKang 消费者方法
	 * 
	 *         方法参数：
	 * 
	 *         getQueue : 用于获取存储在localhost:8161上的存储通道 如 : getQueue = FristQueue
	 *         那么: 消费者方法[doReceiver()]将从名字为FristQueue的通道上拿数据
	 * */
	public void doReceiver(String getQueue) {

		/** Connection ：JMS 客户端到JMS Provider 的连接 */
		Connection connection = null;
		/** Session： 一个发送或接收消息的线程 */
		Session session = null;
		/** Destination ：消息的目的地;消息发送给谁. */
		Destination destination;
		/** 消费者，消息接收者 */
		MessageConsumer consumer;

		try {
			/** 构造从工厂得到连接对象 */
			connection = factoryUtil.getFactory().createConnection();
			/** 启动 */
			connection.start();
			/** 获取操作连接 */
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			/** 获取session注意参数值 */
			destination = session.createQueue(getQueue);

			/** 创建消费者实例*/
			consumer = session.createConsumer(destination);
			
			/** 从MQ通道中拿到数据*/
			MapMessage groupInfoMath = (MapMessage) consumer.receive(100);

			/**
			 * 数据拆分属性
			 * @groupid 用于保存单个分组的id
			 * @companyid 用于保存目前公司的id
			 * @username 用于保存用户的姓名
			 * */
			
			String groupid ;
			String companyid ;
			String username ;
			
			/** 如果拿到的数据不是空的话*/
			while (groupInfoMath != null) {
				
				/** 赋值*/
				groupid = groupInfoMath.getString("groupid") ;
				companyid = groupInfoMath.getString("companyid") ;
				username = groupInfoMath.getString("username") ;
				
				/** 调用产生分组图片的方法，将得到的数据传到该方法中*/
				List<HashMap<String, String>> anOrAnyGroup = 
						getChatWS().getAnOrAnyGroup(username, groupid, companyid);
				
				HashMap<String, String> groupImage = chatWS.getImage(companyid);
				
				try {
					for (HashMap<String, String> hashMap : anOrAnyGroup) {
						
						String anPicPath = groupImage.get(hashMap.get("ID"));
						String[] paths = anPicPath.split(",");
						for (int i = 0; i < paths.length; i++) {
							String headImagePath = imageAfterPath
									.substring(0,imageAfterPath.indexOf("files/"))+paths[i];
							File file = new File(headImagePath);
							if (!file.exists()) {
								System.out.println("缺乏资源文件："+headImagePath);
							}
						}
					}						
					/** 传递已经生成的参数 其中包括上一步拿到的集合 */
					getChatWS().doImageMath(companyid ,username , groupid, anOrAnyGroup);
					/** 再次从MQ通道中取值，并覆盖groupInfoMath用于下一次的判断*/
					groupInfoMath = (MapMessage) consumer.receive(100);
				} catch (Exception e) {
					/** 再次从MQ通道中取值，并覆盖groupInfoMath用于下一次的判断*/
					groupInfoMath = (MapMessage) consumer.receive(100);
				}
			}
			username = null ;
			companyid = null ;
			groupid = null ;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				/** 关闭通道*/
				destination = null;
				/** 关闭Session*/
				session.close();
				if (null != connection)
					/** 关闭工厂*/
					connection.close();
			} catch (Throwable ignore) {
			}
		}
	}
	/**
	 * xiewenda 创建索引时的消息队列
	 */
	public void doIndexReceiver(String getQueue) {
	  System.out.println("starting beging execute doIndexReceiver method..............");
	  /** Connection ：JMS 客户端到JMS Provider 的连接 */
	  Connection connection = null;
	  /** Session： 一个发送或接收消息的线程 */
	  Session session = null;
	  /** Destination ：消息的目的地;消息发送给谁. */
	  Destination destination;
	  /** 消费者，消息接收者 */
	  MessageConsumer consumer;
	  
	  try {
	    /** 构造从工厂得到连接对象 */
	    connection = factoryUtil.getFactory().createConnection();
	    /** 启动 */
	    connection.start();
	    /** 获取操作连接 */
	    session = connection.createSession(Boolean.FALSE,
	        Session.CLIENT_ACKNOWLEDGE);
	    /*  AUTO_ACKNOWLEDGE = 1    自动确认 
        CLIENT_ACKNOWLEDGE = 2    客户端手动确认   
        DUPS_OK_ACKNOWLEDGE = 3    自动批量确认 
        SESSION_TRANSACTED = 0    事务提交并确认 */
	    /** 获取session注意参数值 */
	    destination = session.createQueue(getQueue);
	    
	    /** 创建消费者实例*/
	    consumer = session.createConsumer(destination);
	    
	    /** 从MQ通道中拿到数据*/
	    MapMessage indexMap = (MapMessage) consumer.receive(100);
	    
	    /**
	     * 数据拆分属性
	     * @groupid 用于保存单个分组的id
	     * @companyid 用于保存目前公司的id
	     * @username 用于保存用户的姓名
	     * */
	    
	    String companyid = null;
	    String id = null;
	    
	    /** 如果拿到的数据不是空的话*/
	    while (indexMap != null) {
	      Map<String,String> map = new HashMap<String,String>();
	      try {
	      /** 赋值*/
	        map.put("id", indexMap.getString("id"));
	        map.put("companyId", indexMap.getString("companyId") );
	        map.put("fileType", indexMap.getString("fileType"));
	        map.put("fileId", indexMap.getString("fileId"));
	        map.put("onlinefile", indexMap.getString("onlinefile"));
	        boolean flag = getLuceneWS().mqUpdateIndexfileContent(map);
	        if(flag) indexMap.acknowledge();
	        indexMap = (MapMessage) consumer.receive(100);
	        map=null;
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	    companyid = null ;
	    id = null ;
	    
	  } catch (Exception e) {
	    e.printStackTrace();
	  } finally {
	    try {
	      /** 关闭通道*/
	      destination = null;
	      /** 关闭Session*/
	      session.close();
	      if (null != connection)
	        /** 关闭工厂*/
	        connection.close();
	    } catch (Throwable ignore) {
	    }
	  }
	}


	@Override
	public boolean doUrlReceiver(String getQueue) {
		// TODO Auto-generated method stub
		boolean flag = false ;
		try {
			doReceiver(getQueue);
			flag = true ;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return flag ;
	}
}
