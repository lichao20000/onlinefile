package cn.flying.rest.onlinefile.quartz.util;

import org.springframework.stereotype.Component;

import cn.flying.rest.onlinefile.restInterface.MessageQueueConsumerWS;
import cn.flying.rest.onlinefile.utils.BaseWS;

@Component
public class doConsumerSomething extends BaseWS implements Runnable {

	public String ActiveMQPath;
	
	public doConsumerSomething(String ActiveMQPath){
		this.ActiveMQPath=ActiveMQPath;
	}
	
	/** 声明消费者服务 */
	private MessageQueueConsumerWS consumerWS;

	public MessageQueueConsumerWS getconsumerWS() {
		if (consumerWS == null) {
			synchronized (MessageQueueConsumerWS.class) {
				if (consumerWS == null) {
					consumerWS = this.getService(MessageQueueConsumerWS.class);
				}
			}
		}
		return consumerWS;
	}
	
	@Override
	public void run() {
		getconsumerWS().doReceiver(ActiveMQPath);
	}

}
