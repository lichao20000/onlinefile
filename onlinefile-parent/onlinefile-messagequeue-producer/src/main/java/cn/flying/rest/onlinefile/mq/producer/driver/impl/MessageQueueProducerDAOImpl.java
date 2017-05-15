package cn.flying.rest.onlinefile.mq.producer.driver.impl;

import cn.flying.rest.onlinefile.mq.producer.driver.MessageQueueProducerDAO;
import cn.flying.rest.onlinefile.utils.BaseDaoHibernate;

@SuppressWarnings("rawtypes")
public class MessageQueueProducerDAOImpl extends BaseDaoHibernate implements MessageQueueProducerDAO {
	
	@SuppressWarnings("unchecked")
	public MessageQueueProducerDAOImpl() {
		super(MessageQueueProducerDAOImpl.class);
	}
	
	/**
	 * @author LIuKang 
	 * 由于暂时用不上ORM层  暂时作为预留
	 * */

}
