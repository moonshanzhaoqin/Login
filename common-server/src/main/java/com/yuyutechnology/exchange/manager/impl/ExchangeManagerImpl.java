package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.CurrencyDAO;
import com.yuyutechnology.exchange.dao.ExchangeDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.dto.WalletInfo;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.CommonManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.ExchangeManager;
import com.yuyutechnology.exchange.manager.ExchangeRateManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
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
	TransferDAO transferDAO;
	@Autowired
	CrmAlarmDAO crmAlarmDAO;
	
	@Autowired
	ExchangeRateManager exchangeRateManager;
	@Autowired
	UserManager userManager;
	@Autowired
	CommonManager commonManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	CrmAlarmManager crmAlarmManager;
	
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
						wallet.getCurrency().getNameHk(), wallet.getCurrency().getCurrencyStatus(), wallet.getCurrency().getCurrencyUnit(),
						wallet.getBalance()));
			}
		}
		return list;
	}

	@Override
	public HashMap<String, String> exchangeCalculation(int userId, String currencyOut, String currencyIn,
			BigDecimal amountOut) {

		HashMap<String, String> map = new HashMap<String, String>();
		
		
		//每次兑换金额限制
		BigDecimal exchangeLimitPerPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITPERPAY, 100000d));
		logger.info("exchangeLimitPerPay : {}",exchangeLimitPerPay.toString());
		if((exchangeRateManager.getExchangeResult(currencyOut, amountOut)).compareTo(exchangeLimitPerPay) == 1){
			logger.warn("Exceeds the maximum amount of each exchange");
			map.put("retCode", RetCodeConsts.EXCHANGE_LIMIT_EACH_TIME);
			map.put("msg", exchangeLimitPerPay.toString());
			return map;
		}
		//每天累计兑换金额限制
		BigDecimal exchangeLimitDailyPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITDAILYPAY, 100000d));
		logger.info("exchangeLimitDailyPay : {}",exchangeLimitDailyPay.toString());
		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount("exchange"+userId);
		if((accumulatedAmount.add(exchangeRateManager.getExchangeResult(currencyOut, amountOut))).compareTo(exchangeLimitDailyPay) == 1){
			logger.warn("More than the maximum daily exchange limit");
			map.put("retCode", RetCodeConsts.EXCHANGE_LIMIT_DAILY_PAY);
			map.put("msg", exchangeLimitDailyPay.toString());
			return map;
		}
		//每天累计兑换次数限制
		Double exchangeLimitNumOfPayPerDay =  configManager.
				getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITNUMBEROFPAYPERDAY, 100000d);
		logger.info("exchangeLimitNumOfPayPerDay : {}",exchangeLimitNumOfPayPerDay.toString());
		Integer totalNumOfDailyExchange = exchangeDAO.getTotalNumOfDailyExchange();
		if(exchangeLimitNumOfPayPerDay <= new Double(totalNumOfDailyExchange)){
			logger.warn("Exceeds the maximum number of exchange per day");
			map.put("retCode", RetCodeConsts.EXCHANGE_LIMIT_NUM_OF_PAY_PER_DAY);
			map.put("msg", exchangeLimitNumOfPayPerDay.toString());
			return map;
		}

		if(!commonManager.verifyCurrency(currencyOut) || !commonManager.verifyCurrency(currencyIn)){
			logger.warn("This currency is not a tradable currency");
			map.put("retCode", RetCodeConsts.EXCHANGE_CURRENCY_IS_NOT_A_TRADABLE_CURRENCY);
			map.put("msg", "This currency is not a tradable currency");
			return map;
		}
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
		// 然后判断换算后金额是否超过最小限额
		double exchangeRate = exchangeRateManager.getExchangeRate(currencyOut, currencyIn);
		BigDecimal result = amountOut.multiply(new BigDecimal(Double.toString(exchangeRate)));
		logger.info("out : " + amountOut + " exchangeRate : " + exchangeRate + "result : " + result);
		if (currencyIn.equals(ServerConsts.CURRENCY_OF_GOLDPAY) && result.compareTo(new BigDecimal(1)) == 1) {

		} else if (!currencyIn.equals(ServerConsts.CURRENCY_OF_GOLDPAY)
				&& result.compareTo(new BigDecimal("0.0001")) == 1) {

		} else {
			map.put("retCode", RetCodeConsts.EXCHANGE_AMOUNT_LESS_THAN_MINIMUM_TRANSACTION_AMOUNT);
			map.put("msg", "The amount of the conversion is less than the minimum transaction amount");
			return map;
		}

		HashMap<String, BigDecimal> map2 = exchangeCalculation(currencyOut, currencyIn, amountOut, 0);
		map.put("retCode", RetCodeConsts.RET_CODE_SUCCESS);
		map.put("msg", "ok");
		map.put("out", map2.get("out").toString());
		map.put("in", map2.get("in").toString());
		return map;
	}

	@Override
	public HashMap<String, String> exchangeConfirm(int userId, String currencyOut, String currencyIn, BigDecimal amountOut,
			BigDecimal amountIn) {
		HashMap<String, String> result = exchangeCalculation(userId, currencyOut, currencyIn, amountOut);
		if (result.get("retCode").equals(RetCodeConsts.RET_CODE_SUCCESS)) {
			// 用户账户
			// 扣款
			int updateCount = walletDAO.updateWalletByUserIdAndCurrency(userId, currencyOut, new BigDecimal(result.get("out")), "-");
			if(updateCount == 0){//余额不足
				result.put("retCode", RetCodeConsts.EXCHANGE_OUTPUTAMOUNT_BIGGER_THAN_BALANCE);
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
			


			String exchangeId = exchangeDAO.createExchangeId(ServerConsts.TRANSFER_TYPE_EXCHANGE);
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
			exchange.setExchangeRate(new BigDecimal(Double.toString(exchangeRateManager.getExchangeRate(currencyOut, currencyIn))));

			exchangeDAO.addExchange(exchange);

			// 添加seq记录
			walletSeqDAO.addWalletSeq4Exchange(userId, ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId, currencyOut,
					new BigDecimal(result.get("out")), currencyIn, new BigDecimal(result.get("in")));
			walletSeqDAO.addWalletSeq4Exchange(systemUserId, ServerConsts.TRANSFER_TYPE_EXCHANGE, exchangeId,
					currencyIn, new BigDecimal(result.get("in")), currencyOut, new BigDecimal(result.get("out")));
			
			//添加累计金额
			BigDecimal exchangeResult = exchangeRateManager.getExchangeResult(currencyOut,amountOut);
			transferDAO.updateAccumulatedAmount("exchange"+userId, exchangeResult.setScale(2, BigDecimal.ROUND_FLOOR));
			
			//预警
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
		values.add(0);
		
		if(!period.equals("all")){
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
		
		sb.append(" order by createTime desc");
		
		HashMap<String, Object> map = exchangeDAO.getExchangeRecordsByPage(sb.toString(), values, currentPage, pageSize);
		
		return map;
	}

	@Override
	public HashMap<String, BigDecimal> exchangeCalculation(String currencyOut, String currencyIn, BigDecimal outAmount,
			int capitalFlows) {

		logger.info("currencyOut : {},currencyIn : {},outAmount:{}",
				new String[] { currencyOut, currencyIn, outAmount.toString() });
		// 取余位数
		int bitsOut = 4;
		int bitsIn = 4;

		// 获取汇率
		double exchangeRate = exchangeRateManager.getExchangeRate(currencyOut,currencyIn);
		// 计算最小单位对应的数值
		if (currencyIn.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) {
			bitsIn = 0;
		}
		if (currencyOut.equals(ServerConsts.CURRENCY_OF_GOLDPAY)) {
			bitsOut = 0;
		}
		
		BigDecimal in = (outAmount.multiply(new BigDecimal(Double.toString(exchangeRate)))).setScale(bitsIn, BigDecimal.ROUND_FLOOR);
		
		BigDecimal out = (in.divide(new BigDecimal(Double.toString(exchangeRate)),bitsOut,BigDecimal.ROUND_CEILING));

		HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();

		logger.info("currencyOut : {},currencyIn : {}, out:{}, in:{}",
				new String[] { currencyOut, currencyIn, out.toString(), in.toString() });

		map.put("out", out);
		map.put("in", in);

		return map;

	}
	
	
	@SuppressWarnings("serial")
//	@Async
	private void largeExchangeWarn(final Exchange exchange){
		BigDecimal exchangeLimitPerPay =  BigDecimal.valueOf(configManager.
				getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITPERPAY, 100000d));
		BigDecimal percentage = (exchangeRateManager.getExchangeResult(exchange.getCurrencyOut(), exchange.getAmountOut()))
				.divide(exchangeLimitPerPay,2,RoundingMode.DOWN).multiply(new BigDecimal("100"));
		
		logger.info("exchangeLimitPerPay : {},percentage : {}",exchangeLimitPerPay.toString(),percentage.toString());
		
		List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(2, 1);
		
		if(list != null && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				CrmAlarm crmAlarm = list.get(i);
				
				if((crmAlarm.getLowerLimit().compareTo(percentage)==0 || 
						crmAlarm.getLowerLimit().compareTo(percentage)==-1) && 
						crmAlarm.getUpperLimit().compareTo(percentage) == 1){

					crmAlarmManager.alarmNotice(crmAlarm.getSupervisorIdArr(), "largeExchangeWarning", crmAlarm.getAlarmMode(),new HashMap<String,Object>(){
						{
							put("payerMobile", exchange.getUserId());
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

}
