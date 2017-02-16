package com.yuyutechnology.exchange.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.dao.ExchangeDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.pojo.Exchange;
import com.yuyutechnology.exchange.utils.page.PageUtils;

@Repository
public class ExchangeDAOImpl implements ExchangeDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;
	@Autowired
	RedisDAO redisDAO;
	
	private final String ANYTIME_EXECHANGE_ASSIGN_EXCHANGEID = "anytimeExechangeAssignExchangeid";

	@Override
	public String createExchangeId(int transferType) {
		Long id = redisDAO.incrementValue(ANYTIME_EXECHANGE_ASSIGN_EXCHANGEID, 1);
		StringBuilder sb = new StringBuilder();
		sb.append(DateFormatUtils.format(new Date(), "yyyyMMddHHmm")).append(transferType).append("E");
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

	@Override
	public HashMap<String, Object> getExchangeRecordsByPage(String sql, List<Object> values, int currentPage,
			int pageSize) {
		int firstResult = (currentPage -1)*pageSize;
		int masResult = pageSize;
		
		List<?> list = PageUtils.getListByPage(hibernateTemplate, sql, values, firstResult, masResult);
		
		long total = PageUtils.getTotal(hibernateTemplate, sql,values);
		int pageTotal = PageUtils.getPageTotal(total, pageSize);
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("currentPage",currentPage);
		map.put("pageSize",pageSize);
		map.put("total",total);
		map.put("pageTotal",pageTotal);
		map.put("list",list);
		
		return map;
	}

	@Override
	public Integer getTotalNumOfDailyExchange() {
		Integer sum = hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(" select COUNT(*) "
						+ "FROM `e_exchange` "
						+ "where TO_DAYS(`finish_time`) = TO_DAYS(NOW())");
				List list = query.list();
				if((list != null && !list.isEmpty())&& list.get(0)!=null){
					return Integer.parseInt((list.get(0).toString()));
				}
				
				return new Integer(-1);
			}
		});

		return sum;
	}
}
