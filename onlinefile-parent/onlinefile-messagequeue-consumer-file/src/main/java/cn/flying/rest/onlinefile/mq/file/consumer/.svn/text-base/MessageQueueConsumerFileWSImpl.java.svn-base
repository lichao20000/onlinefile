package cn.flying.rest.onlinefile.mq.file.consumer;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.flying.rest.onlinefile.mq.file.consumer.util.ConsumerFileFactoryUtil;
import cn.flying.rest.onlinefile.restInterface.MessageQueueConsumerFileWS;
import cn.flying.rest.onlinefile.utils.BaseWS;

@Path("/onlinefile_messagequeue_consumer_file")
@Component
public class MessageQueueConsumerFileWSImpl extends BaseWS implements
	MessageQueueConsumerFileWS {

	/**
	 * 将Consumer对象工厂注入进来
	 * */
	@Autowired
	ConsumerFileFactoryUtil factoryUtil;

	
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
			connection = factoryUtil.getFileFactory().createConnection();
			/** 启动 */
			connection.start();
			/** 获取操作连接 */
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			/** 获取session注意参数值 */
			destination = session.createQueue(getQueue);

			/** 创建消费者实例*/
			consumer = session.createConsumer(destination);
			
			//listener 方式 
		    consumer.setMessageListener(new MessageListener() { 
	
				public void onMessage(Message msg) {
					MapMessage message = (MapMessage) msg; 
		            try {
						System.out.println(" 收到消息：" + message.getString("path"));
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}  
				} 
				
		    }); 
		    
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
