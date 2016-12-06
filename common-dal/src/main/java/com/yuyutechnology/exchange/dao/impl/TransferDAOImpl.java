package com.yuyutechnology.exchange.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.TransferDAO;

@Repository
public class TransferDAOImpl implements TransferDAO {
	
	@Resource
	RedisTemplate<String, String> commonRedisTemplate;
	
	private final String anytime_exechange_assign_transid = "anytimeExechangeAssignTransid";

	@Override
	public String createTransId(int transferType) {
		Long id = commonRedisTemplate.opsForValue().increment(anytime_exechange_assign_transid, 1);
		StringBuilder sb = new StringBuilder();
		sb.append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append(transferType).append("T");
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

}
