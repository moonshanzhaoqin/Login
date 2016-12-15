package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.pojo.Transfer;

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
	public Transfer getTransferById(String transferId) {
		Transfer transfer = hibernateTemplate.get(Transfer.class, transferId);
		return transfer;
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
	public void getTransactionRecordByPage(String period, int status) {
		// TODO Auto-generated method stub
		
		String hql = "select t1.amount,t1.currency  from WalletSeq t1,Transfer t2 where t1.transactionId = t2.transferId and t2.userFrom = ? or t2.userTo=?";
		
		
		
	}

}
