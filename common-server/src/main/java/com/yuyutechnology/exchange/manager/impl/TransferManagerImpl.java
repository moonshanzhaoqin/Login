package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.ConfigDAO;
import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dao.UserDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.exchangerate.ExchangeRate;

@Service
public class TransferManagerImpl implements TransferManager{
	
	@Autowired
	UserDAO userDAO;
	@Autowired
	RedisDAO redisDAO;
	@Autowired
	ConfigDAO configDAO;
	@Autowired
	WalletDAO walletDAO;
	@Autowired
	TransferDAO transferDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;
	@Autowired
	UnregisteredDAO unregisteredDAO;
	
	public static Logger logger = LoggerFactory.getLogger(TransferManagerImpl.class);

	@Override
	public String transferInitiate(int userId,String areaCode,String userPhone, String currency, 
			BigDecimal amount, String transferComment) {
		//判断余额是否足够支付
		Wallet wallet = walletDAO.getWalletByUserIdAndCurrency(userId, currency);
		if(wallet == null || wallet.getBalance().compareTo(amount) == -1){
			logger.warn("Current balance is insufficient");
			return ServerConsts.TRANSFER_CURRENT_BALANCE_INSUFFICIENT;
		}
		//当日累加金额
		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount(userId+"");
		//当日最大金额===================================================================
		BigDecimal dayMaxAmount =  new BigDecimal(20000);
		//判断是否超过当日累加金额
		if(accumulatedAmount.add(amount).compareTo(dayMaxAmount) == 1){
			logger.warn("Exceeded the day's transaction limit");
			return ServerConsts.TRANSFER_EXCEEDED_TRANSACTION_LIMIT;
		}
		
		User receiver = userDAO.getUserByUserPhone(areaCode, userPhone);
		
		//生成TransId
		String transferId = transferDAO.createTransId(ServerConsts.TRANSFER_TYPE_OF_TRANSACTION);
		
		Transfer transfer = new Transfer();
		transfer.setTransferId(transferId);
		transfer.setCreateTime(new Date());
		transfer.setCurrency(currency);
		transfer.setTransferAmount(amount);
		transfer.setTransferComment(transferComment);
		transfer.setTransferStatus(ServerConsts.TRANSFER_STATUS_OF_INITIALIZATION);
		transfer.setTransferType(ServerConsts.TRANSFER_TYPE_OF_TRANSACTION);
		transfer.setUserFrom(userId);
		//判断接收人是否是已注册账号
		if(receiver != null){
			transfer.setUserTo(receiver.getUserId());
		}else{
			transfer.setUserTo(0);
		}
		transfer.setUserToPhone(areaCode+userPhone);
		
		//保存
		transferDAO.addTransfer(transfer);
		
		return transferId;
	}

	@Override
	public String payPwdConfirm(int userId, String transferId, String userPayPwd) {
		
		User user = userDAO.getUser(userId);
		Transfer transfer = transferDAO.getTransferById(transferId);
		
		if(!userPayPwd.equals(user.getUserPayPwd())){
			return ServerConsts.TRANSFER_PAYMENTPWD_INCORRECT;
		}

		//总账大于设置安全基数，弹出需要短信验证框===============================================
		BigDecimal totalBalance =  new BigDecimal(20000);
		BigDecimal totalBalanceMax =  new BigDecimal(30000);
		//当天累计转出总金额大于设置安全基数，弹出需要短信验证框======================================
		BigDecimal accumulatedAmount =  transferDAO.getAccumulatedAmount(userId+"");
		BigDecimal accumulatedAmountMax =  new BigDecimal(30000);
		//单笔转出金额大于设置安全基数，弹出需要短信验证框==========================================
		BigDecimal AmountofSingleTransfer =  new BigDecimal(30000);
		
		if(totalBalance.compareTo(totalBalanceMax) == 1 || 
				( accumulatedAmount.compareTo(accumulatedAmountMax) == 1 || 
				transfer.getTransferAmount().compareTo(AmountofSingleTransfer) == 1)){
			logger.warn("The transaction amount exceeds the limit");
			//发送pinCode///////////////////////////////////////////////////////////////
			
			
			
			return ServerConsts.TRANSFER_REQUIRES_PHONE_VERIFICATION;
			
		}else{
			transferConfirm(transferId);
		}
		
		return ServerConsts.RET_CODE_SUCCESS;
	}

	@Override
	public void transferConfirm(String transferId) {
		Transfer transfer = transferDAO.getTransferById(transferId);
		
		if(transfer.getUserTo() == 0){  	//交易对象没有注册账号
			
			//获取系统账号
			User systemUser = userDAO.getSystemUser();
			//扣款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-");
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(systemUser.getUserId(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "+");
			//添加gift记录
			Unregistered unregistered = new Unregistered();
			//手机号有问题
			unregistered.setAreaCode(transfer.getUserToPhone().substring(3));
			unregistered.setUserPhone(transfer.getUserToPhone().
					substring(3, transfer.getUserToPhone().length()));
			unregistered.setCurrency(transfer.getCurrency());
			unregistered.setAmount(transfer.getTransferAmount());
			unregistered.setCreateTime(new Date());
			unregistered.setUnregisteredStatus(ServerConsts.TRANSFER_STATUS_OF_PROCESSING);
			
			unregisteredDAO.addUnregistered(unregistered);
			//更改用户的当日累计金额
//			transferDAO.updateAccumulatedAmount(transfer.getUserFrom()+"", exchangeResult);
			//增加seq记录
			walletSeqDAO.addWalletSeq4Transaction(transfer.getUserFrom(), systemUser.getUserId(), 
					ServerConsts.TRANSFER_TYPE_OF_TRANSACTION, transfer.getTransferId(), 
					transfer.getCurrency(), transfer.getTransferAmount());
			
		}else{								//交易对象注册账号,交易正常进行，无需经过系统账户
			
			//扣款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserFrom(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "-");
			//加款
			walletDAO.updateWalletByUserIdAndCurrency(transfer.getUserTo(), 
					transfer.getCurrency(), transfer.getTransferAmount(), "+");
			//更改Transfer状态
			transferDAO.updateTransferStatus(transferId, ServerConsts.TRANSFER_STATUS_OF_COMPLETED);
			
			//更改用户的当日累计金额
//			transferDAO.updateAccumulatedAmount(transfer.getUserFrom()+"", exchangeResult);
			
			//添加seq记录
			walletSeqDAO.addWalletSeq4Transaction(transfer.getUserFrom(), transfer.getUserTo(), 
					ServerConsts.TRANSFER_TYPE_OF_TRANSACTION, transfer.getTransferId(), 
					transfer.getCurrency(), transfer.getTransferAmount());		
		}
		
		//转换金额
		BigDecimal exchangeResult = getExchangeResult(transfer.getCurrency(),transfer.getTransferAmount());
		transferDAO.updateAccumulatedAmount(transfer.getUserFrom()+"", exchangeResult);
	}
	
	
	
	///////////////////////////////////////////方法内部调用//////////////////////////////////////////////
	/**
	 * @Descrition : 将交易金额兑换为默认币种
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 下午5:24:08
	 * @param transCurrency 交易币种
	 * @param transAmount   默认币种
	 * @return
	 */
	private BigDecimal getExchangeResult(String transCurrency,BigDecimal transAmount){
		BigDecimal result = null;
		//默认币种
		String standardCurrency = configDAO.getConfigValue(ServerConsts.STANDARD_CURRENCY);
		if(transCurrency.equals(standardCurrency)){
			result = transAmount;
		}else{
			double exchangeRate = getExchangeRate(transCurrency, standardCurrency);
			result = transAmount.multiply(new BigDecimal(exchangeRate));
		}
		return result;
	}
	
	/**
	 * @Descrition : 获取汇率
	 * @author : nicholas.chi
	 * @time : 2016年12月7日 下午5:29:14
	 * @param base
	 * @param outCurrency
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private double getExchangeRate(String base, String outCurrency) {
		double out = 0;
		HashMap<String, String> map = new HashMap<String, String>();
		String result = redisDAO.getValueByKey("redis_exchangeRate");
		if(StringUtils.isNotBlank(result)){
//			logger.info("result : {}",result);
			map = JsonBinder.getInstance().fromJson(result, HashMap.class);
			String value = map.get(base);
			logger.info("value : {}",value);
			ExchangeRate exchangeRate = JsonBinder.getInstanceNonNull().fromJson(value, ExchangeRate.class);
			
			if(base.equals(ServerConsts.CURRENCY_OF_GOLDPAY) || outCurrency.equals(ServerConsts.CURRENCY_OF_GOLDPAY)){
				//当兑换中有goldpay时，需要特殊处理//////////////////////////////////////////////
			}else{
				out = exchangeRate.getRates().get(outCurrency);
			}
		}
		
		logger.info("base : {},out : {}",base,out);
		
		return out;
	}
}
