/**
 * @(#)AGLPRedisSerializer.java ,Created on May 18, 2015
 * 
 * Copyright (c) aspectgaming
 * 
 * MODIFY MEMO:
 * silent.sun/modify time/modify reason
 * 
 */
package com.yuyutechnology.exchange.redis;

import java.io.UnsupportedEncodingException;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.ServerException;

/**
 * Class description goes here.
 *
 *@version   May 18, 2015 6:25:25 PM
 *@author    silent.sun
 *@since     JDK1.6
 */
@Component
public class StringRedisSerializer implements RedisSerializer<String>
{
	public static final String CHARSET = "iso-8859-1";
	/* (non-Javadoc)
	 * @see org.springframework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(String t) throws SerializationException
	{
		try
		{
			if (t == null)
			{
				throw new ServerException("value sent to redis cannot be null");
			}
			return t.getBytes(CHARSET);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new ServerException(e);
		}
	}
	/* (non-Javadoc)
	 * @see org.springframework.data.redis.serializer.RedisSerializer#deserialize(byte[])
	 */
	@Override
	public String deserialize(byte[] bytes) throws SerializationException
	{
		try
		{
			if (bytes == null) {
				return null;
			}
			return new String(bytes, CHARSET);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new ServerException(e);
		}
	}
}
