package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.utils.page.PageUtils;

@Repository
public class TransferDAOImpl implements TransferDAO {
	
	@Resource
	HibernateTemplate hibernateTemplate;
	@Resource
	RedisTemplate<String, String> commonRedisTemplate;
	
	private final String anytime_exechange_assign_transid = "anytimeExechangeAssignTransid";
	//用户累加交易金额
	private final String accumulated_amount_key = "accumulated_amount_[key]";

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

	@Override
	public void addTransfer(Transfer transfer) {
		hibernateTemplate.save(transfer);
	}
	
	@Override
	public void updateTransfer(Transfer transfer){
		hibernateTemplate.saveOrUpdate(transfer);
	}
	

	@Override
	public Transfer getTransferById(String transferId) {
		Transfer transfer = hibernateTemplate.get(Transfer.class, transferId);
		return transfer;
	}
	

	@Override
	public Transfer getTranByIdAndStatus(String transferId, int transferStatus) {
		List<?> list = hibernateTemplate.find("from Transfer where transferId = ? "
				+ "and transferStatus != ?", transferId,transferStatus);
		
		if(!list.isEmpty()){
			return (Transfer) list.get(0);
		}
		
		return null;
	}

	
	@Override
	public void updateTransferStatus(String transferId, int transferStatus) {
		Transfer transfer = hibernateTemplate.get(Transfer.class, transferId);
		transfer.setTransferStatus(transferStatus);
		if(transferStatus == ServerConsts.TRANSFER_STATUS_OF_COMPLETED){
			transfer.setFinishTime(new Date());
		}
		hibernateTemplate.saveOrUpdate(transfer);
	}

	@Override
	public void updateTransferStatusAndUserTo(String transferId, int transferStatus, Integer userTo) {
		Transfer transfer = hibernateTemplate.get(Transfer.class, transferId);
		transfer.setTransferStatus(transferStatus);
		transfer.setUserTo(userTo);
		if(transferStatus == ServerConsts.TRANSFER_STATUS_OF_COMPLETED){
			transfer.setFinishTime(new Date());
		}
		hibernateTemplate.saveOrUpdate(transfer);
	}

	@Override
	public void updateAccumulatedAmount(String key,BigDecimal amoumt) {
		commonRedisTemplate.opsForValue().increment(accumulated_amount_key.replace("[key]", key),amoumt.longValue() );
	}

	@Override
	public BigDecimal getAccumulatedAmount(String key) {
		String result = commonRedisTemplate.opsForValue().get(accumulated_amount_key.replace("[key]", key));
		if (StringUtils.isEmpty(result)){
			return new BigDecimal(0);
		}else{
			return new BigDecimal(result);
		}
	}

	@Override
	public HashMap<String, Object> getTransactionRecordByPage(String sql,String countSql,List<Object> values,int currentPage, int pageSize) {
		
		int firstResult = (currentPage -1)*pageSize;
		int masResult = pageSize;
		
		List<?> list = PageUtils.getListByPage4MySql(hibernateTemplate, sql, values, firstResult, masResult);
		
		long total = PageUtils.getTotal4MySql(hibernateTemplate, countSql, values);
		int pageTotal = PageUtils.getPageTotal(total, pageSize);
		
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("currentPage",currentPage);
		map.put("pageSize",pageSize);
		map.put("total",total);
		map.put("pageTotal",pageTotal);
		map.put("list",list);
		
		return map;
		
	}

}
