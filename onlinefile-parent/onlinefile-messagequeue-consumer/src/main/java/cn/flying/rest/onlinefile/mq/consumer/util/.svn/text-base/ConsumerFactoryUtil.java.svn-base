package cn.flying.rest.onlinefile.mq.consumer.util;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsumerFactoryUtil {

	@Value("${onlinefile.messagequeue.factoryUtil.ipPath}")	
	public String ipPath;
	@Value("${onlinefile.messagequeue.factoryUtil.macPath}")
	public String macPath;

	/** 根据参数获取结果的方法 */
	public ConnectionFactory getFactory() {
		return new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://"+ipPath+":"+macPath);
	}

}
