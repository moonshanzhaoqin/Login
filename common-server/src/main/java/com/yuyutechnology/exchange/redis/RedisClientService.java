/**
 * @(#)RedisClientManager.java ,Created on Nov 13, 2012
 * 
 * Copyright (c) aspectgaming
 * 
 * MODIFY MEMO:
 * kevin.sun/modify time/modify reason
 * 
 */
package com.yuyutechnology.exchange.redis;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
/**
 * Manager Redis Client for sharding access.
 * 
 * @version Nov 13, 2012 2:37:26 PM
 * @author kevin.sun
 * @since JDK1.6
 */
public class RedisClientService
{
	public static Logger logger = LoggerFactory
			.getLogger(RedisClientService.class);
	
	private String host;
	private int port;
	private int minIdle;
	private int maxIdle;
	private int maxWait;
	
	public enum JedisClient
	{
		global
	}
	/** global redis pool */
	JedisPool globalJedisPool;
	/**
	 * 
	 */
	@PostConstruct
	public void init() throws Exception
	{
		// Construct JedisPoolConfig and Set some pool parameters to it;
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWait);
		// Initialize global RedisClient
		//设置global超时 30秒
		globalJedisPool = new JedisPool(jedisPoolConfig,host,
				port, 30000);
	}
	/**
	 * 
	 * @param <T>
	 * @param action
	 * @return
	 */
	public <T> T execute(JedisCallBack<T> action, JedisClient client)
			throws Exception
	{
		Jedis jedis = null;
		JedisPool jedisPool = null;
		try
		{
			jedisPool = getJedisPool(client);
			jedis = jedisPool.getResource();
			return action.doInRedis(jedis);
		}
		catch (Exception e)
		{
			//logger.error("error RedisClientManager.execute", e);
			if (e instanceof JedisConnectionException)
			{
				jedisPool.returnBrokenResource(jedis);
			}
			throw new Exception(e);
		}
		finally
		{
			jedisPool.returnResource(jedis);
		}
	}
	/**
	 * 
	 * @param client
	 * @return
	 */
	public JedisPool getJedisPool(JedisClient client)
	{
		JedisPool jedisPool = null;
		switch (client)
		{
		case global:
			jedisPool = globalJedisPool;
			break;
		default:
			break;
		}
		return jedisPool;
	}
	public JedisPool getGlobalJedisPool()
	{
		return globalJedisPool;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getMinIdle() {
		return minIdle;
	}
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	public int getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	public int getMaxWait() {
		return maxWait;
	}
	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}
	
}
