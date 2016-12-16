package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.ExchangeDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.dto.WalletInfo;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Exchange;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.utils.DateFormatUtils;

@Service
public class ExchangeManagerImpl implements ExchangeManager {

	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	CurrencyDAO currencyDAO;
	@Autowired
	ExchangeDAO exchangeDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;

	@Autowired
	ExchangeRateManager exchangeRateManager;

	public static Logger logger = LoggerFactory.getLogger(ExchangeManagerImpl.class);

	@Override
	public List<WalletInfo> getWalletsByUserId(int userId) {
		List<WalletInfo> list = new ArrayList<>();
		List<Wallet> wallets = walletDAO.getWalletsByUserId(userId);
		for (Wallet wallet : wallets) {
			if (wallet.getCurrency().getCurrencyStatus() == ServerConsts.CURRENCY_AVAILABLE
					|| wallet.getBalance().compareTo(BigDecimal.ZERO)!=0 ) {
				list.add(new WalletInfo(wallet.getCurrency().getCurrency(),
						wallet.getCurrency().getNameEn(), wallet.getCurrency().getNameCn(),
						wallet.getCurrency().getNameHk(), wallet.getCurrency().getCurrencyStatus(),
						wallet.getBalance()));
			}
		}
		return list;
	}

	@Override
	public HashMap<String, String> exchangeCalculation(int userId, String currencyOut, String currencyIn,
			BigDecimal amountOut) {

		HashMap<String, String> map = new HashMap<String, String>();

		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currencyOut);
		if (wallet == null) {
			map.put("retCode", ServerConsts.EXCHANGE_WALLET_CAN_NOT_BE_QUERIED);
			map.put("msg", "The user's information can not be queried");
			return map;
		}
		// 首先判断输入金额是否超过余额
		if (amountOut.compareTo(wallet.getBalance()) == 1) {
			map.put("retCode", ServerConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE);
			map.put("msg", "The output amount is greater than the balance");
			return map;
		}
		// 然后判断换算后金额是否超过最小限额
		double exchangeRate = exchangeRateManager.getExchangeRate(currencyOut, currencyIn);
		BigDecimal result = amountOut.multiply(new BigDecimal(exchangeRate));
		logger.info("out : " + amountOut + " exchangeRate : " + exchangeRate + "result : " + result);
		if (currencyOut.equals(ServerConsts.CURRENCY_OF_GOLDPAY) && result.compareTo(new BigDecimal(1)) == 1) {

		} else if (!currencyOut.equals(ServerConsts.CURRENCY_OF_GOLDPAY)
				&& result.compareTo(new BigDecimal(0.01)) == 1) {

		} else {
			map.put("retCode", ServerConsts.EXCHANGE_AMOUNT_LESS_THAN_MINIMUM_TRANSACTION_AMOUNT);
			map.put("msg", "The amount of the conversion is less than the minimum transaction amount");
			return map;
		}

		HashMap<String, BigDecimal> map2 = exchangeCalculation(currencyOut, currencyIn, amountOut, 0);
		map.put("retCode", ServerConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("out", map2.get("out").toString());
		map.put("in", map2.get("in").toString());
		return map;
	}

	@Override
	public HashMap<String, String> exchangeConfirm(int userId, String currencyOut, String currencyIn, BigDecimal amountOut,
			BigDecimal amountIn) {
		HashMap<String, String> result = exchangeCalculation(userId, currencyOut, currencyIn, amountOut);
		if (result.get("retCode").equals(ServerConsts.RET_CODE_SUCCESS)) {
			// 用户账户
			// 扣款
			int updateCount = walletDAO.updateWalletByUserIdAndCurrency(userId, currencyOut, new BigDecimal(result.get("out")), "-");
			if(updateCount == 0){//余额不足
				result.put("retCode", ServerConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE);
				result.put("msg", "Insufficient balance");
				return result;
			}
			// 加款
			walletDAO.updateWalletByUserIdAndCurrency(userId, currencyIn, new BigDecimal(result.get("in")), "+");

			// 系统账户
			int systemUserId = userDAO.getSystemUser().getUserId();
			// 加款
			walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyOut, new BigDecimal(result.get("out")),
					"+");
			// 扣款
			walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyIn, new BigDecimal(result.get("in")), "-");
			


			String exchangeId = exchangeDAO.createExchangeId(ServerConsts.TRANSFER_TYPE_OF_EXCHANGE);
			// 添加Exchange记录
			Exchange exchange = new Exchange();
			exchange.setExchangeId(exchangeId);
			exchange.setUserId(userId);
			exchange.setCurrencyOut(currencyOut);
			exchange.setAmountOut(new BigDecimal(result.get("out")));
			exchange.setCurrencyIn(currencyIn);
			exchange.setAmountIn(new BigDecimal(result.get("in")));
			exchange.setCreateTime(new Date());
			exchange.setFinishTime(new Date());
			exchange.setExchangeRate(new BigDecimal(exchangeRateManager.getExchangeRate(currencyOut, currencyIn)));

			exchangeDAO.addExchange(exchange);

			// 添加seq记录
			walletSeqDAO.addWalletSeq4Exchange(userId, ServerConsts.TRANSFER_TYPE_OF_EXCHANGE, exchangeId, currencyOut,
					amountOut, currencyIn, amountIn);
			walletSeqDAO.addWalletSeq4Exchange(systemUserId, ServerConsts.TRANSFER_TYPE_OF_EXCHANGE, exchangeId,
					currencyIn, amountIn, currencyOut, amountOut);
		}

		return result;
	}
	
	@Override
	public HashMap<String, Object> getExchangeRecordsByPage(int userId, String period, int currentPage, int pageSize) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder sb = new StringBuilder("from Exchange where userId = ? and exchangeStatus = ? ");
		
		List<Object> values = new ArrayList<Object>();
		values.add(userId);
		/////////////////////////////////////////////////////////////
		values.add(0);
		
		switch (period) {
			case "today":
				sb.append("and createTime > ?");
				values.add(DateFormatUtils.getStartTime(sdf.format(new Date())));
				break;
				
			case "lastMonth":
				sb.append("and createTime > ?");
				Date date = DateFormatUtils.getpreDays(-30);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;
			case "last3Month":
				sb.append("and createTime > ?");
				date = DateFormatUtils.getpreDays(-90);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));		
				break;
			case "lastYear":
				sb.append("and createTime > ?");
				date = DateFormatUtils.getpreDays(-365);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;
			case "aYearAgo":
				sb.append("and createTime  < ?");
				date = DateFormatUtils.getpreDays(-365);
				values.add(DateFormatUtils.getStartTime(sdf.format(date)));
				break;
	
			default:
				break;
		}
		
		sb.append(" order by createTime desc");
		
//		logger.info("生成SQL:{}",sb.toString());

		HashMap<String, Object> map = exchangeDAO.getExchangeRecordsByPage(sb.toString(), values, currentPage, pageSize);
		
		return map;
	}

	@Override
	public HashMap<String, BigDecimal> exchangeCalculation(String currencyOut, String currencyIn, BigDecimal outAmount,
			int capitalFlows) {

		logger.info("currencyOut : {},currencyIn : {},outAmount:{}",
				new String[] { currencyOut, currencyIn, outAmount.toString() });
		// 取余位数
		int bitsOut = 2;
		int bitsIn = 2;

		// 获取汇率
		double exchangeRate = exchangeRateManager.getExchangeRate(currencyIn, currencyOut);
		// 计算最小单位对应的数值
		double minimumValue = 0.01;
		if (currencyIn.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) {
			minimumValue = 1;
			bitsIn = 0;
		}
		if (currencyOut.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) {
			bitsOut = 0;
		}

		double rounding = Math.floor(outAmount.doubleValue() / (exchangeRate * minimumValue));

		logger.info("rounding : {}", rounding);

		BigDecimal out = new BigDecimal(exchangeRate * rounding * minimumValue).setScale(bitsOut,
				BigDecimal.ROUND_CEILING);
		BigDecimal in = new BigDecimal(minimumValue * rounding).setScale(bitsIn, BigDecimal.ROUND_FLOOR);

		HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();

		logger.info("currencyOut : {},currencyIn : {}, out:{}, in:{}",
				new String[] { currencyOut, currencyIn, out.toString(), in.toString() });

		map.put("out", out);
		map.put("in", in);

		return map;

	}

}
