package cn.flying.rest.onlinefile.mq.file.consumer.driver.impl;

import cn.flying.rest.onlinefile.mq.file.consumer.driver.MessageQueueConsumerFileDAO;
import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;

@SuppressWarnings("rawtypes")
public class MessageQueueConsumerFileDAOImpl extends BaseDaoHibernate implements MessageQueueConsumerFileDAO {
	
	@SuppressWarnings("unchecked")
	public MessageQueueConsumerFileDAOImpl() {
		super(MessageQueueConsumerFileDAOImpl.class);
	}
	
	/**
	 * @author LIuKang 
	 * 由于暂时用不上ORM层  暂时作为预留
	 * */

}
