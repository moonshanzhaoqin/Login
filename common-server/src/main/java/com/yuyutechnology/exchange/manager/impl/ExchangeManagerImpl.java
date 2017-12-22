package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.ExchangeDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.goldpay.msg.GoldpayUserDTO;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.Currency;
import com.yuyutechnology.exchange.pojo.Exchange;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.util.DateFormatUtils;

@Service
public class ExchangeManagerImpl implements ExchangeManager {

	@Autowired
	UserDAO userDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	ExchangeDAO exchangeDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	CrmAlarmDAO crmAlarmDAO;
	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	UserManager userManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	CrmAlarmManager crmAlarmManager;

	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;

	public static Logger logger = LogManager.getLogger(ExchangeManagerImpl.class);

	@Override
	public HashMap<String, String> exchangeCalculation(int userId, String currencyOut, String currencyIn,
			BigDecimal amountOut) {

		HashMap<String, String> map = new HashMap<String, String>();
		if (!commonManager.verifyCurrency(currencyOut) || !commonManager.verifyCurrency(currencyIn)) {
			logger.info("This currency is not a tradable currency");
			map.put("retCode", RetCodeConsts.EXCHANGE_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			map.put("msg", "This currency is not a tradable currency");
			return map;
		}

		Currency unit = commonManager.getCurreny("USD");

		// 每次兑换金额限制
		BigDecimal exchangeLimitPerPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITPERPAY, 100000d));
		logger.info("exchangeLimitPerPay : {}", exchangeLimitPerPay.toString());
		if ((oandaRatesManager.getDefaultCurrencyAmount(currencyOut, amountOut)).compareTo(exchangeLimitPerPay) == 1) {
			logger.info("Exceeds the maximum amount of each exchange");
			map.put("retCode", RetCodeConsts.EXCHANGE_LIMIT_EACH_TIME);
			map.put("msg", exchangeLimitPerPay.setScale(2).toString());
			map.put("thawTime", DateFormatUtils.getIntervalDay(new Date(), 1).getTime() + "");
			map.put("unit", unit.getCurrencyUnit());
			return map;
		}
		// 每天累计兑换金额限制
		BigDecimal exchangeLimitDailyPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITDAILYPAY, 100000d));
		logger.info("exchangeLimitDailyPay : {}", exchangeLimitDailyPay.toString());
		BigDecimal accumulatedAmount = transferDAO.getAccumulatedAmount("exchange_" + userId);
		if ((accumulatedAmount.add(oandaRatesManager.getDefaultCurrencyAmount(currencyOut, amountOut)))
				.compareTo(exchangeLimitDailyPay) == 1) {
			logger.info("More than the maximum daily exchange limit");
			map.put("retCode", RetCodeConsts.EXCHANGE_LIMIT_DAILY_PAY);
			map.put("msg", exchangeLimitDailyPay.setScale(2).toString());
			map.put("thawTime", DateFormatUtils.getIntervalDay(new Date(), 1).getTime() + "");
			map.put("unit", unit.getCurrencyUnit());
			return map;
		}
		// 每天累计兑换次数限制
		Double exchangeLimitNumOfPayPerDay = configManager
				.getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITNUMBEROFPAYPERDAY, 100000d);
		logger.info("exchangeLimitNumOfPayPerDay : {}", exchangeLimitNumOfPayPerDay.toString());
		Integer totalNumOfDailyExchange = transferDAO.getCumulativeNumofTimes("exchange_" + userId);
		if (exchangeLimitNumOfPayPerDay <= new Double(totalNumOfDailyExchange)) {
			logger.info("Exceeds the maximum number of exchange per day");
			map.put("retCode", RetCodeConsts.EXCHANGE_LIMIT_NUM_OF_PAY_PER_DAY);
			map.put("msg", exchangeLimitNumOfPayPerDay.intValue() + "");
			map.put("thawTime", DateFormatUtils.getIntervalDay(new Date(), 1).getTime() + "");
			return map;
		}

		// add by niklaus.chi at 2017-10-16
		if (ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyOut)) {
			//
			GoldpayUserDTO dto = goldpayTrans4MergeManager.getGoldpayUserAccount(userId);
			if (dto == null || amountOut.compareTo(new BigDecimal(dto.getBalance() + "")) == 1) {
				map.put("retCode", RetCodeConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE);
				map.put("msg", "The output amount is greater than the balance");
				return map;
			}
		} else {
			Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currencyOut);
			if (wallet == null) {
				map.put("retCode", RetCodeConsts.EXCHANGE_WALLET_CAN_NOT_BE_QUERIED);
				map.put("msg", "The user's information can not be queried");
				return map;
			}
			// 首先判断输入金额是否超过余额
			if (amountOut.compareTo(wallet.getBalance()) == 1) {
				map.put("retCode", RetCodeConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE);
				map.put("msg", "The output amount is greater than the balance");
				return map;
			}
		}

		// 然后判断换算后金额是否超过最小限额
		BigDecimal result = oandaRatesManager.getExchangedAmount(currencyOut, amountOut, currencyIn);

		if (currencyIn.equals(ServerConsts.CURRENCY_OF_GOLDPAY) && result.compareTo(new BigDecimal(1)) == 1) {

		} else if (!currencyIn.equals(ServerConsts.CURRENCY_OF_GOLDPAY)
				&& result.compareTo(new BigDecimal("0.0001")) == 1) {

		} else {
			map.put("retCode", RetCodeConsts.EXCHANGE_AMOUNT_LESS_THAN_MINIMUM_TRANSACTION_AMOUNT);
			map.put("msg", "The amount of the conversion is less than the minimum transaction amount");
			return map;
		}

		HashMap<String, BigDecimal> map2 = exchangeCalculation(currencyOut, currencyIn, amountOut);
		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("out", map2.get("out").toString());
		map.put("in", map2.get("in").toString());
		map.put("fee", map2.get("fee").toString());
		map.put("rate", map2.get("rate").toString());
		map.put("perThousand", map2.get("perThousand").toString());
		return map;
	}

	// @Override
	// public HashMap<String, String> exchangeConfirm(int userId, String
	// currencyOut, String currencyIn,
	// BigDecimal amountOut) {
	// HashMap<String, String> result = exchangeCalculation(userId, currencyOut,
	// currencyIn, amountOut);
	// if (result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
	// // 用户账户
	// // 扣款
	// String exchangeId =
	// exchangeDAO.createExchangeId(ServerConsts.TRANSFER_TYPE_EXCHANGE);
	//
	// int updateCount = walletDAO.updateWalletByUserIdAndCurrency(userId,
	// currencyOut,
	// new BigDecimal(result.get("out")), "-", ServerConsts.TRANSFER_TYPE_EXCHANGE,
	// exchangeId);
	// if (updateCount == 0) {// 余额不足
	// result.put("retCode",
	// RetCodeConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE);
	// result.put("msg", "Insufficient balance");
	// return result;
	// }
	// // 加款
	// walletDAO.updateWalletByUserIdAndCurrency(userId, currencyIn, new
	// BigDecimal(result.get("in")), "+",
	// ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
	//
	// // 系统账户
	// int systemUserId = userDAO.getSystemUser().getUserId();
	// // 加款
	// walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyOut, new
	// BigDecimal(result.get("out")), "+",
	// ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
	// // 扣款
	// walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyIn, new
	// BigDecimal(result.get("in")), "-",
	// ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
	//
	// // 添加Exchange记录
	// Exchange exchange = new Exchange();
	// exchange.setExchangeId(exchangeId);
	// exchange.setUserId(userId);
	// exchange.setCurrencyOut(currencyOut);
	// exchange.setAmountOut(new BigDecimal(result.get("out")));
	// exchange.setCurrencyIn(currencyIn);
	// exchange.setAmountIn(new BigDecimal(result.get("in")));
	// exchange.setCreateTime(new Date());
	// exchange.setFinishTime(new Date());
	// exchange.setExchangeRate(new BigDecimal(result.get("rate")));
	// exchange.setExchangeFeePerThousand(new
	// BigDecimal(result.get("perThousand")));
	// exchange.setExchangeFeeAmount(new BigDecimal(result.get("fee")));
	//
	// exchangeDAO.addExchange(exchange);
	//
	// // 添加seq记录
	// // walletSeqDAO.addWalletSeq4Exchange(userId,
	// // ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId, currencyOut,
	// // new BigDecimal(result.get("out")), currencyIn, new
	// // BigDecimal(result.get("in")));
	// // walletSeqDAO.addWalletSeq4Exchange(systemUserId,
	// // ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId,
	// // currencyIn, new BigDecimal(result.get("in")), currencyOut, new
	// // BigDecimal(result.get("out")));
	//
	// // 添加累计金额
	// BigDecimal exchangeResult =
	// oandaRatesManager.getDefaultCurrencyAmount(exchange.getCurrencyOut(),
	// exchange.getAmountOut());
	// transferDAO.updateAccumulatedAmount("exchange_" + userId,
	// exchangeResult.setScale(2, BigDecimal.ROUND_FLOOR));
	// // 更改累计次数
	// transferDAO.updateCumulativeNumofTimes("exchange_" + userId, new
	// BigDecimal("1"));
	//
	// // 预警
	// largeExchangeWarn(exchange);
	//
	// }
	//
	// return result;
	// }

	@Override
	public HashMap<String, String> exchangeConfirm(int userId, String currencyOut, String currencyIn,
			BigDecimal amountOut) {
		HashMap<String, String> result = exchangeCalculation(userId, currencyOut, currencyIn, amountOut);
		if (result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {

			String exchangeId = exchangeDAO.createExchangeId(ServerConsts.TRANSFER_TYPE_EXCHANGE);
			// 系统账户
			int systemUserId = userDAO.getSystemUser().getUserId();

			if (!ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyOut)) {
				int updateCount = walletDAO.updateWalletByUserIdAndCurrency(userId, currencyOut,
						new BigDecimal(result.get("out")), "-", ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
				if (updateCount == 0) {// 余额不足
					result.put("retCode", RetCodeConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE);
					result.put("msg", "Insufficient balance");
					return result;
				}
				// 加款
				walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyOut, new BigDecimal(result.get("out")),
						"+", ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
			}

			if (!ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyIn)) {
				walletDAO.updateWalletByUserIdAndCurrency(userId, currencyIn, new BigDecimal(result.get("in")), "+",
						ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
				// 扣款
				walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyIn, new BigDecimal(result.get("in")),
						"-", ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
			}

			// if(!ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyIn)
			// && !ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyOut)){
			//
			// int updateCount = walletDAO.updateWalletByUserIdAndCurrency(userId,
			// currencyOut,
			// new BigDecimal(result.get("out")), "-", ServerConsts.TRANSFER_TYPE_EXCHANGE,
			// exchangeId);
			// if (updateCount == 0) {// 余额不足
			// result.put("retCode",
			// RetCodeConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE);
			// result.put("msg", "Insufficient balance");
			// return result;
			// }
			// walletDAO.updateWalletByUserIdAndCurrency(userId, currencyIn, new
			// BigDecimal(result.get("in")), "+",
			// ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
			//
			// // 加款
			// walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyOut, new
			// BigDecimal(result.get("out")), "+",
			// ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
			// // 扣款
			// walletDAO.updateWalletByUserIdAndCurrency(systemUserId, currencyIn, new
			// BigDecimal(result.get("in")), "-",
			// ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId);
			// }

			String goldpayOrderId = null;
			if (ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyOut)
					|| ServerConsts.CURRENCY_OF_GOLDPAY.equals(currencyIn)) {
				goldpayOrderId = goldpayTrans4MergeManager.getGoldpayOrderId();
				if(!StringUtils.isNotBlank(goldpayOrderId)){
					result.put("retCode", RetCodeConsts.TRANSFER_GOLDPAYTRANS_ORDERID_NOT_EXIST);
					result.put("msg", "Not generated goldpayId");
					return result;
				}
			}

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
			exchange.setExchangeRate(new BigDecimal(result.get("rate")));
			exchange.setExchangeFeePerThousand(new BigDecimal(result.get("perThousand")));
			exchange.setExchangeFeeAmount(new BigDecimal(result.get("fee")));
			exchange.setGoldpayOrderId(goldpayOrderId);
			exchange.setExchangeStatus(ServerConsts.EXCHANGE_STATUS_OF_PROCESS);
			exchangeDAO.addExchange(exchange);

			goldpayTrans4MergeManager.updateWallet4GoldpayExchange(exchangeId, systemUserId);

			// 添加累计金额
			BigDecimal exchangeResult = oandaRatesManager.getDefaultCurrencyAmount(exchange.getCurrencyOut(),
					exchange.getAmountOut());
			transferDAO.updateAccumulatedAmount("exchange_" + userId,
					exchangeResult.setScale(2, BigDecimal.ROUND_FLOOR));
			// 更改累计次数
			transferDAO.updateCumulativeNumofTimes("exchange_" + userId, new BigDecimal("1"));

			// 预警
			largeExchangeWarn(exchange);

		}

		return result;
	}

	@Override
	public HashMap<String, Object> getExchangeRecordsByPage(int userId, String period, int currentPage, int pageSize) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder sb = new StringBuilder("from Exchange where userId = ? and exchangeStatus = ? ");

		List<Object> values = new ArrayList<Object>();
		values.add(userId);
		values.add(ServerConsts.EXCHANGE_STATUS_OF_COMPLETED);

		if (!period.equals("all")) {
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
		}

		sb.append(" order by createTime desc, exchangeId desc");

		HashMap<String, Object> map = exchangeDAO.getExchangeRecordsByPage(sb.toString(), values, currentPage,
				pageSize);

		return map;
	}

	@Override
	public HashMap<String, BigDecimal> exchangeCalculation(String currencyOut, String currencyIn,
			BigDecimal outAmount) {
		// 取余位数
		int bitsOut = 4;
		int bitsIn = 4;

		// 计算最小单位对应的数值
		if (currencyIn.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) {
			bitsIn = 0;
		}
		if (currencyOut.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) {
			bitsOut = 0;
		}

		String exchangeFeePerThousand = configManager.getConfigStringValue(ConfigKeyEnum.EXCHANGEFEE, "1.5");

		BigDecimal rate = oandaRatesManager.getSingleExchangeRate(currencyOut, currencyIn);

		BigDecimal in = (outAmount.multiply(rate)).setScale(bitsIn, BigDecimal.ROUND_DOWN);

		// BigDecimal in = (oandaRatesManager.getExchangedAmount(currencyOut, outAmount,
		// currencyIn))
		// .setScale(bitsIn, BigDecimal.ROUND_DOWN);

		BigDecimal fee = in.multiply(new BigDecimal(((Double.parseDouble(exchangeFeePerThousand)) / 1000) + ""))
				.setScale(bitsIn, BigDecimal.ROUND_DOWN);

		// BigDecimal out = (oandaRatesManager.getInputValue(currencyOut, in,
		// currencyIn))
		// .setScale(bitsOut, BigDecimal.ROUND_UP);

		BigDecimal out = in.divide(rate, bitsOut, BigDecimal.ROUND_UP);

		HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();

		logger.info("{} to {} ,  out : {}, in : {} ,rate : {}, fee : {} ,per thousand : {}  ", currencyOut, currencyIn,
				out, in.subtract(fee), rate, fee, exchangeFeePerThousand);

		map.put("out", out);
		map.put("in", in.subtract(fee));
		map.put("fee", fee);
		map.put("perThousand", new BigDecimal(exchangeFeePerThousand));
		map.put("rate", rate);
		return map;

	}

	@SuppressWarnings("serial")
	private void largeExchangeWarn(final Exchange exchange) {
		BigDecimal exchangeLimitPerPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITPERPAY, 100000d));
		BigDecimal percentage = (oandaRatesManager.getDefaultCurrencyAmount(exchange.getCurrencyOut(),
				exchange.getAmountOut())).divide(exchangeLimitPerPay, 5, RoundingMode.DOWN)
						.multiply(new BigDecimal("100"));

		logger.info("exchangeLimitPerPay : {},percentage : {}", exchangeLimitPerPay.toString(), percentage.toString());

		final User user = userDAO.getUser(exchange.getUserId());

		List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(2, 1);

		if (list != null && !list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				CrmAlarm crmAlarm = list.get(i);

				if ((crmAlarm.getLowerLimit().compareTo(percentage) == 0
						|| crmAlarm.getLowerLimit().compareTo(percentage) == -1)
						&& crmAlarm.getUpperLimit().compareTo(percentage) == 1) {

					crmAlarmManager.alarmNotice(crmAlarm.getSupervisorIdArr(), "largeExchangeWarning",
							crmAlarm.getAlarmMode(), new HashMap<String, Object>() {
								{
									put("payerMobile", user.getAreaCode() + user.getUserPhone());
									put("amountOut", exchange.getAmountOut());
									put("currencyOut", exchange.getCurrencyOut());
									put("amountIn", exchange.getAmountIn());
									put("currencyIn", exchange.getCurrencyIn());
								}
							});

				}

			}
		}
	}

	@Override
	public Exchange getExchangeById(String exchangeId) {
		return exchangeDAO.getExchangeById(exchangeId);
	}

	@Override
	public Object getExchange(String exchangeId) {
		return exchangeDAO.getExchangeByIdJoinUser(exchangeId);
	}

}
