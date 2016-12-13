package com.yuyutechnology.exchange.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.ExchangeDAO;
import com.yuyutechnology.exchange.pojo.Exchange;

@Repository
public class ExchangeDAOImpl implements ExchangeDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;
	@Resource
	RedisTemplate<String, String> commonRedisTemplate;
	
	private final String anytime_exechange_assign_exchangeid = "anytimeExechangeAssignExchangeid";

	@Override
	public String createExchangeId(int transferType) {
		Long id = commonRedisTemplate.opsForValue().increment(anytime_exechange_assign_exchangeid, 1);
		StringBuilder sb = new StringBuilder();
		sb.append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append(transferType).append("E");
		String idStr = String.valueOf(id);
		if (idStr.length() < 6)
		{
			for (int i = 0; i < 6 - idStr.length(); i++)
			{
				sb.append("0");
			}
		}
		sb.append(idStr);
		return sb.toString();
	}

	@Override
	public void addExchange(Exchange exchange) {
		hibernateTemplate.save(exchange);
	}
}
