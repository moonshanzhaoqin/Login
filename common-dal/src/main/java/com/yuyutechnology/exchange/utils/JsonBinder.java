/**
 * @(#)JsonBinder.java ,Created on Dec 20, 2012
 * 
 * Copyright (c) aspectgaming
 * 
 * MODIFY MEMO:
 * mestry.ma/modify time/modify reason
 * 
 */
package com.yuyutechnology.exchange.utils;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuyutechnology.exchange.pojo.Bind;

/**
 * Class description goes here.
 * 
 * @version Dec 20, 2012 3:35:12 PM
 * @author mestry.ma
 * @since JDK1.6
 */
public class JsonBinder
{
	private static Logger logger = LogManager.getLogger(JsonBinder.class);
	private static Object lock = new Object();
	private static JsonBinder jsonBinder;
	private static JsonBinder jsonBinderNonNull;
	private ObjectMapper mapper;
	private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 
	 * @return
	 */
	public static JsonBinder getInstance()
	{
		if (jsonBinder == null)
		{
			synchronized (lock)
			{
				if (jsonBinder == null)
				{
					jsonBinder = new JsonBinder(Include.NON_EMPTY);
				}
			}
		}
		return jsonBinder;
	}
	/**
	 * 
	 * @param inclusion
	 * @return
	 */
	public static JsonBinder getInstanceNonNull()
	{
		if (jsonBinderNonNull == null)
		{
			synchronized (lock)
			{
				if (jsonBinderNonNull == null)
				{
					jsonBinderNonNull = new JsonBinder(Include.NON_NULL);
				}
			}
		}
		return jsonBinderNonNull;
	}
	
	protected JsonBinder(Include inclusion)
	{
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(inclusion);
//		mapper.getDeserializationConfig().set(
//				Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDateFormat(df);
	}
	
	public <T> T fromJson(String jsonString, Class<T> clazz)
	{
		if (StringUtils.isEmpty(jsonString))
		{
			return null;
		}
		try
		{
			return mapper.readValue(jsonString, clazz);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}
	
	public Object fromJsonToList(String jsonString, Class clazz)
	{
		if (StringUtils.isEmpty(jsonString))
		{
			return null;
		}
		try
		{
			JavaType type = mapper.getTypeFactory().constructCollectionType(
					List.class, clazz);
			return mapper.readValue(jsonString, type);
		}
		catch (IOException e)
		{
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}
	
	/**
	 * 如果对象为Null,返回"null". 如果集合为空集合,返回"[]".
	 */
	public String toJson(Object object)
	{
		try
		{
			return mapper.writeValueAsString(object);
		}
		catch (JsonProcessingException e)
		{
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	
	public static void main(String[] args) {
		Map<String, Bind> map = new HashMap<String, Bind>();
		map.put("aa", new Bind(1));
		System.out.println(JsonBinder.getInstance().getInstance().toJson(map));
		
	}
}
