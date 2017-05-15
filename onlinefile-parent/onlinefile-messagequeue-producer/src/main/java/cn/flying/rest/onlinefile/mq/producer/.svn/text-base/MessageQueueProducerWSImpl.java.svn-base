package cn.flying.rest.onlinefile.mq.producer;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.ws.rs.Path;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.flying.rest.onlinefile.mq.producer.util.ProducerFactoryUtil;
import cn.flying.rest.onlinefile.restInterface.MessageQueueProducerWS;
import cn.flying.rest.onlinefile.utils.BaseWS;

@SuppressWarnings("serial")
@Path("/onlinefile_messagequeue_producer")
@Component
public class MessageQueueProducerWSImpl extends BaseWS implements
		MessageQueueProducerWS, Serializable {

	/**
	 * 拿到之前注入进去的工厂对象
	 * */
	@Autowired
	private ProducerFactoryUtil factoryUtil;

	/**
	 * @author LiuKang 生产者方法
	 * 
	 *         方法参数：
	 * 
	 *         setQueue : 用于设置存储在localhost:8161上的存储通道 如 : setQueue = FristQueue
	 *         那么: 消费者方法[doReceiver()]将从名字为FristQueue的通道上拿数据
	 * 
	 *         obj: 从前台所传递的数据
	 * */

	@SuppressWarnings("rawtypes")
	public void doSender(String setQueue, Map<String, String> groupsInfoMath) {
		/** Connection ：JMS 客户端到JMS Provider 的连接 */
		Connection connection = null;
		/** Session： 一个发送或接收消息的线程 */
		Session session = null;
		/** Destination ：消息的目的地;消息发送给谁. */
		Destination destination;
		/** MessageProducer：消息发送者 */
		MessageProducer producer;

		try {
			/** 构造从工厂得到连接对象 */
			connection = factoryUtil.getFactory().createConnection();
			/** 启动 */
			connection.start();
			/** 获取操作连接 */
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);

			/** 获取session注意参数值 */
			destination = session.createQueue(setQueue);
			/** 得到消息生成者【发送者】 */
			producer = session.createProducer(destination);
			/** 设置不持久化，此处学习，实际根据项目决定 */
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			/** 由于采用Map类型进行数据交互 所以定义为Map类型的消息 */
			MapMessage mapMessage = session.createMapMessage();

			/** 从Map中拿到所欲键的集合 */
			Set<String> mapsKey = groupsInfoMath.keySet();

			/** 创建迭代器的实例 */
			Iterator iterator = mapsKey.iterator();

			/** 如果迭代器中有值的话 */
			while (iterator.hasNext()) {
				/** 取出下一个有值的单位并赋值 */
				String thisKey = (String) iterator.next();
				/** 向Map中放入接收到的值 */
				mapMessage.setString(thisKey, groupsInfoMath.get(thisKey));
			}

			/** 发送消息至消息通道 */
			producer.send(mapMessage);

			/** 提交Session */
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				/** 通道置空 */
				destination = null;
				/** 关闭Session */
				session.close();
				if (null != connection)
					/** 关闭工厂 */
					connection.close();
			} catch (Throwable ignore) {
			  
		}
		}
	}
	public boolean doIndexSender(String setQueue, Map<String, Object> map) {
	  /** Connection ：JMS 客户端到JMS Provider 的连接 */
	  Connection connection = null;
	  /** Session： 一个发送或接收消息的线程 */
	  Session session = null;
	  /** Destination ：消息的目的地;消息发送给谁. */
	  Destination destination;
	  /** MessageProducer：消息发送者 */
	  MessageProducer producer;
	  
	  try {
	    /** 构造从工厂得到连接对象 */
	    connection = factoryUtil.getFactory().createConnection();
	    /** 启动 */
	    connection.start();
	    /** 获取操作连接 */
	    session = connection.createSession(Boolean.TRUE,
	        Session.AUTO_ACKNOWLEDGE);
	    
	    /** 获取session注意参数值 */
	    destination = session.createQueue(setQueue);
	    /** 得到消息生成者【发送者】 */
	    producer = session.createProducer(destination);
	    /** 设置持久化 */
	    producer.setDeliveryMode(DeliveryMode.PERSISTENT);
	    
	    /** 由于采用Map类型进行数据交互 所以定义为Map类型的消息 */
	    MapMessage mapMessage = session.createMapMessage();
	    
	    /** 如果迭代器中有值的话 */
	  /*  while (f) {
	      *//** 取出下一个有值的单位并赋值 *//*
	      String thisKey = (String) iterator.next();
	      *//** 向Map中放入接收到的值 *//*
	      mapMessage.setString(thisKey, groupsInfoMath.get(thisKey));
	    }*/
	    for ( String key : map.keySet()) {
	          mapMessage.setString(key, map.get(key)+"");
        }
	    
	    /** 发送消息至消息通道 */
        producer.send(mapMessage);
	    
	    /** 提交Session */
	    session.commit();
	    return true;
	  } catch (Exception e) {
	    e.printStackTrace();
	  } finally {
	    try {
	      /** 通道置空 */
	      destination = null;
	      /** 关闭Session */
	      session.close();
	      if (null != connection)
	        /** 关闭工厂 */
	        connection.close();
	    } catch (Throwable ignore) {
	    }
	  }
	  return false;
	}

	public boolean doSender(String setQueue, String json2Map) {
		System.out.println("starting beging execute doSender method..............");
		boolean flag = false;
		try {
			String param = URLDecoder.decode(json2Map,"UTF-8");
			doSender(setQueue, toHashMap(param));
			flag = true ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@SuppressWarnings("rawtypes")
	private HashMap<String, String> toHashMap(String json) {
		HashMap<String, String> data = new HashMap<String, String>();
	
		// 将json字符串转换成jsonObject
		JSONObject jsonObject = JSONObject.fromObject(json);
		Iterator it = jsonObject.keys();
		// 遍历jsonObject数据，添加到Map对象
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			String value = jsonObject.get(key).toString();
			data.put(key, value.replace('*', '/'));
			System.out.println("key------------------>" + key );
			System.out.println("value------------------>" + value );
		}
		return data;
	}
}
