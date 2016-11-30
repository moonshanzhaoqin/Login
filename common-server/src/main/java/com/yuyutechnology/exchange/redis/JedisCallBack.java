/**
 * @(#)JedisCallBack.java ,Created on Nov 15, 2012
 * 
 * Copyright (c) aspectgaming
 * 
 * MODIFY MEMO:
 * kevin.sun/modify time/modify reason
 * 
 */
package com.yuyutechnology.exchange.redis;
import org.springframework.dao.DataAccessException;
import redis.clients.jedis.Jedis;
/**
 * Class description goes here.
 * 
 *@version Nov 15, 2012 5:28:33 PM
 *@author kevin.sun
 * @param <T>
 *@since JDK1.6
 */
public interface JedisCallBack<T>
{
	T doInRedis(Jedis jedisClient) throws DataAccessException;
}
