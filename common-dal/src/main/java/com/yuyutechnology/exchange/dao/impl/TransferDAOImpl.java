package com.yuyutechnology.exchange.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.util.page.PageBean;
import com.yuyutechnology.exchange.util.page.PageUtils;

@Repository
public class TransferDAOImpl implements TransferDAO {

	@Resource
	HibernateTemplate hibernateTemplate;
	@Autowired
	RedisDAO redisDAO;

	private final String ANYTIME_EXECHANGE_ASSIGN_TRANSID = "anytimeExechangeAssignTransid";
	// 用户累加交易金额
	private final String ACCUMULATED_AMOUNT_KEY = "accumulated_amount_[key]";

	private final String ACCUMULATED_TIMES_KEY = "accumulated_times_[key]";
	
	public static Logger logger = LogManager.getLogger(TransferDAOImpl.class);

	@Override
	public String createTransId(int transferType) {
		Long id = redisDAO.incrementValue(ANYTIME_EXECHANGE_ASSIGN_TRANSID, 1);
		StringBuilder sb = new StringBuilder();
		sb.append(DateFormatUtils.format(new Date(), "yyyyMMddHHmm")).append(transferType).append("T");
		String idStr = String.valueOf(id);
		if (idStr.length() < 6) {
			for (int i = 0; i < 6 - idStr.length(); i++) {
				sb.append("0");
			}
		}
		sb.append(idStr);
		return sb.toString();
	}

	@Override
	public void addTransfer(Transfer transfer) {
		hibernateTemplate.save(transfer);
		logger.info("addTransfer {}",transfer.getTransferId());
	}
	

	@Override
	public void updateTransfer(Transfer transfer) {
		hibernateTemplate.saveOrUpdate(transfer);
	}

	@Override
	public Transfer getTransferById(String transferId) {
		Transfer transfer = hibernateTemplate.get(Transfer.class, transferId);
		return transfer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transfer> findTransferByStatusAndTimeBefore(int transferStatus, int transferType, Date date) {
		List<?> list = hibernateTemplate.find(
				"from Transfer where transferStatus = ? and transferType = ? and createTime <= ? ", transferStatus,
				transferType, date);
		return (List<Transfer>) list;
	}

	@Override
	public Transfer getTranByIdAndStatus(String transferId, int transferStatus) {
		List<?> list = hibernateTemplate.find("from Transfer where transferId = ? " + "and transferStatus = ?",
				transferId, transferStatus);

		if (!list.isEmpty()) {
			return (Transfer) list.get(0);
		}

		return null;
	}

	@Override
	public void updateTransferStatus(String transferId, int transferStatus) {
		Transfer transfer = hibernateTemplate.get(Transfer.class, transferId);
		transfer.setTransferStatus(transferStatus);
		transfer.setFinishTime(new Date());
		hibernateTemplate.saveOrUpdate(transfer);
	}

	@Override
	public void updateTransferStatusAndUserTo(String transferId, int transferStatus, Integer userTo) {
		Transfer transfer = hibernateTemplate.get(Transfer.class, transferId);
		transfer.setTransferStatus(transferStatus);
		transfer.setUserTo(userTo);
		if (transferStatus == ServerConsts.TRANSFER_STATUS_OF_COMPLETED) {
			transfer.setFinishTime(new Date());
		}
		hibernateTemplate.saveOrUpdate(transfer);
	}

	@Override
	public void updateAccumulatedAmount(String key, BigDecimal amoumt) {
		redisDAO.incrementValue(ACCUMULATED_AMOUNT_KEY.replace("[key]", key), amoumt.doubleValue());
		redisDAO.expireAtData(ACCUMULATED_AMOUNT_KEY.replace("[key]", key),
				com.yuyutechnology.exchange.util.DateFormatUtils.getIntervalDay(new Date(), 1));
	}

	@Override
	public void updateCumulativeNumofTimes(String key, BigDecimal amoumt) {
		redisDAO.incrementValue(ACCUMULATED_TIMES_KEY.replace("[key]", key), amoumt.doubleValue());
		redisDAO.expireAtData(ACCUMULATED_TIMES_KEY.replace("[key]", key),
				com.yuyutechnology.exchange.util.DateFormatUtils.getIntervalDay(new Date(), 1));
	}

	@Override
	public BigDecimal getAccumulatedAmount(String key) {
		String result = redisDAO.getValueByKey(ACCUMULATED_AMOUNT_KEY.replace("[key]", key));
		if (StringUtils.isEmpty(result)) {
			return new BigDecimal(0);
		} else {
			return new BigDecimal(result);
		}
	}

	@Override
	public int getCumulativeNumofTimes(String key) {
		String result = redisDAO.getValueByKey(ACCUMULATED_TIMES_KEY.replace("[key]", key));
		if (StringUtils.isEmpty(result)) {
			return 0;
		} else {
			return Integer.parseInt(result);
		}
	}

	@Override
	public HashMap<String, Object> getTransactionRecordByPage(String sql, String countSql, List<Object> values,
			int currentPage, int pageSize) {

		int firstResult = (currentPage - 1) * pageSize;
		int masResult = pageSize;

		List<?> list = PageUtils.getListByPage4MySql(hibernateTemplate, sql, values, firstResult, masResult);

		long total = PageUtils.getTotal4MySql(hibernateTemplate, countSql, values);
		int pageTotal = PageUtils.getPageTotal(total, pageSize);

		HashMap<String, Object> map = new HashMap<>();

		map.put("currentPage", currentPage);
		map.put("pageSize", pageSize);
		map.put("total", total);
		map.put("pageTotal", pageTotal);
		map.put("list", list);

		return map;

	}

	@Override
	public BigDecimal sumGoldpayTransAmount(final int transferType) {

		BigDecimal sum = hibernateTemplate.executeWithNativeSession(new HibernateCallback<BigDecimal>() {
			@Override
			public BigDecimal doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("SELECT sum(transfer_amount) "
						+ "FROM `e_transfer` where transfer_type = ? and transfer_status = 2");
				query.setInteger(0, transferType);

				@SuppressWarnings("unchecked")
				List<BigDecimal> list = query.list();

				if ((list != null && !list.isEmpty()) && list.get(0) != null) {
					return new BigDecimal(list.get(0).toString());
				}

				return new BigDecimal("0");
			}
		});

		return sum;
	}

	@Override
	public Integer getDayTradubgVolume(final int transferType) {
		Integer sum = hibernateTemplate.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("SELECT COUNT(*) FROM `e_transfer` " + "where transfer_type = ? "
						+ "and transfer_status = 2 " + "and k(`finish_time`) = TO_DAYS(NOW());");
				query.setInteger(0, transferType);

				List<?> list = query.list();

				if ((list != null && !list.isEmpty()) && list.get(0) != null) {
					return Integer.parseInt((list.get(0).toString()));
				}

				return new Integer(-1);
			}
		});

		return sum;
	}


	@Override
	public PageBean searchTransfersByPage(String hql, List<Object> values, int currentPage, int pageSize) {
		return PageUtils.getPageContent(hibernateTemplate, hql, values, currentPage, pageSize);

	}

	@Override
	public Object getTransferByIdJoinUser(String transferId) {
		List<?> list = hibernateTemplate.find(
				"from Transfer t,User u1,User u2 where t.transferId = ? and t.userFrom = u1.userId and t.userTo = u2.userId ",
				transferId);
		return list.isEmpty() ? null : list.get(0);
	}

	@Override
	public BigDecimal getTotalPaypalExchange(final Date finishTime, final int transferType, final int transferStatus) {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<BigDecimal>() {
			@Override
			public BigDecimal doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery("select sum(transfer_amount) from e_transfer "
						+ "where transfer_status = ? and transfer_type = ? and finish_time < ?");

				query.setInteger(0, transferStatus);
				query.setInteger(1, transferType);
				query.setDate(2, finishTime);
				@SuppressWarnings("unchecked")
				List<BigDecimal> list = query.list();
				if (list == null || list.isEmpty() || list.get(0) == null) {
					return new BigDecimal("0");
				}

				return new BigDecimal(list.get(0).toString());
			}
		});
	}
	
	@Override
	public Transfer getFeeTransfer(String transferId){
		List<?> list = hibernateTemplate.find("from Transfer where transferComment = ? "
				+ "and transferStatus = ? and transferType = ?", transferId,
				ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION,ServerConsts.TRANSFER_TYPE_IN_FEE);
		return (Transfer) (list.isEmpty() ? null : list.get(0));
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Transfer> getTransferListByTime(int transferStatus, int transferType,Date time){
		List<?> list = hibernateTemplate.find("from Transfer where transferStatus = ? and transferType = ? and createTime < ?", 
				transferStatus,transferType,time);
		return (List<Transfer>) list;
	}

}
