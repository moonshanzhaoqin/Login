/**
 * 
 */
package com.yuyutechnology.exchange.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.yuyutechnology.exchange.dao.RedisDAO;

/**
 * @author silent.sun
 *
 */
public class RedisMessageListener implements MessageListener {
	
	@Autowired
	RedisDAO redisDAO;
	
	private static final Logger logger= LogManager.getLogger(RedisMessageListener.class);

	/* (non-Javadoc)
	 * @see org.springframework.data.redis.connection.MessageListener#onMessage(org.springframework.data.redis.connection.Message, byte[])
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
		logger.info("onMessage ============================= message body: {}, message channel : {}, pattern : {}", new Object[]{new String(message.getBody()),new String(message.getChannel()), new String(pattern)});
	}

}
