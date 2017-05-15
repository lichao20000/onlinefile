package cn.flying.rest.onlinefile.quartz.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.flying.rest.onlinefile.restInterface.MessageQueueConsumerWS;
import cn.flying.rest.onlinefile.utils.BaseWS;

@Component
public class DataConversionTask extends BaseWS implements Runnable {
	
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
	
	/**
	 * 从prop配置文件中读取值
	 * */
	@Value("${onlinefile.messagecursumer.rollPath}")
	String rolls = null;

	/** 定义Run方法 以处理后续的定时任务 */
	public void run(){
			System.out.println("服务器开启处理ActiveMQ数据模式......");
			System.out.println("Plaese Wait....... ");
			long startTime=System.currentTimeMillis();   //获取开始时间
			String[] ArrayRolls = rolls.split(",");
			for (int i = 0; i < ArrayRolls.length; i++) {
				getconsumerWS().doReceiver(ArrayRolls[i]);
			}
			long endTime=System.currentTimeMillis(); //获取结束时间
			System.out.println("维护结束.......");
			System.out.println("维护所运行时间： "+(endTime-startTime)+"ms");
		
//		System.out.println("服务器开启处理ActiveMQ数据模式......");
//		System.out.println("Plaese Wait....... ");
//		long startTime=System.currentTimeMillis();   //获取开始时间
//		doConsumerSomething d = null ;
//		String[] ArrayRolls = rolls.split(",");
//		Thread[] threads = new Thread[2000];
//		for (int i = 0; i < ArrayRolls.length; i++) {
//			d = new doConsumerSomething(ArrayRolls[i]);
//			for (int j = 0; j < 4; i++) {
//				threads[j] = new Thread(d);
//				threads[j].start();
//			}
//		}
//		long endTime=System.currentTimeMillis(); //获取结束时间
//		System.out.println("维护结束.......");
//		System.out.println("维护所运行时间： "+(endTime-startTime)+"ms");
	}
}
