package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.BadAccountDAO;
import com.yuyutechnology.exchange.dao.WalletDAO;
import com.yuyutechnology.exchange.dao.WalletSeqDAO;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.WalletManager;
import com.yuyutechnology.exchange.pojo.BadAccount;
import com.yuyutechnology.exchange.pojo.Wallet;
import com.yuyutechnology.exchange.util.ResourceUtils;
import com.yuyutechnology.exchange.util.page.PageBean;

@Service
public class WalletManagerImpl implements WalletManager {

	@Autowired
	WalletDAO walletDAO;
	@Autowired
	WalletSeqDAO walletSeqDAO;
	@Autowired
	BadAccountDAO badAccountDAO;

	@Autowired
	OandaRatesManager oandaRatesManager;

	public static Logger logger = LogManager.getLogger(WalletManagerImpl.class);

	@Override
	public HashMap<String, BigDecimal> getTotalAmoutGold(int userId) {

		BigDecimal totalUSDAmoumt = BigDecimal.ZERO;

		List<Wallet> list = walletDAO.getWalletsByUserId(userId);

		if (list.isEmpty()) {
			return null;
		}

		for (Wallet wallet : list) {

			if (!wallet.getCurrency().getCurrency().equals(ServerConsts.CURRENCY_OF_USD)) {

				BigDecimal num = oandaRatesManager
						.getDefaultCurrencyAmount(wallet.getCurrency().getCurrency(), wallet.getBalance())
						.setScale(4, BigDecimal.ROUND_DOWN);

				logger.info("{} {} to {} USD", wallet.getBalance(), wallet.getCurrency().getCurrency(), num);

				totalUSDAmoumt = totalUSDAmoumt.add(num);

			} else {
				totalUSDAmoumt = totalUSDAmoumt.add(wallet.getBalance());
			}
		}

		BigDecimal goldpayAmount = oandaRatesManager.getExchangedAmount(ServerConsts.CURRENCY_OF_USD, totalUSDAmoumt,
				ServerConsts.CURRENCY_OF_GOLDPAY);
		logger.info("user id : {} ,Currently has total goldpay : {}", userId, goldpayAmount);
		final BigDecimal goldAmount = goldpayAmount.divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_FLOOR);
		logger.info("user id : {} ,Currently has total gold : {}", userId, goldAmount);
		String oz4g = ResourceUtils.getBundleValue4String("exchange.oz4g");
		logger.info("oz4g : {}", oz4g);
		final BigDecimal goldAmountOz = goldpayAmount.divide(new BigDecimal("10000").multiply(new BigDecimal(oz4g)), 2,
				BigDecimal.ROUND_FLOOR);
		logger.info("user id : {} ,Currently has total gold oz : {}", userId, goldAmountOz);
		@SuppressWarnings("serial")
		HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>() {
			{
				put("goldAmount", goldAmount);
				put("goldAmountOz", goldAmountOz);
			}
		};
		return map;
	}

	@Override
	public void getUserTotalBalance(int systemUserId) {
		walletDAO.getUserAccountTotalAssets(systemUserId);
	}

	@Override
	public PageBean getBadAccountByPage(int currentPage) {
		return badAccountDAO.getBadAccountByPage(currentPage, 10);
	}

	@Override
	public List<?> getDetailSeq(Integer badAccountId) {
		BadAccount badAccount = badAccountDAO.getBadAccount(badAccountId);
		// logger.info("badAccount:{}", badAccount.getBadAccountId());
		return walletSeqDAO.getWalletSeq(badAccount.getUserId(), badAccount.getCurrency(), badAccount.getStartSeqId(),
				badAccount.getEndSeqId());
	}

	@Override
	public List<?> getDetailSeqByTransferId(String transferId) {
		BadAccount badAccount = badAccountDAO.getBadAccountByTransferId(transferId);
		logger.info("badAccount:{}", badAccount == null ? "null" : badAccount.getBadAccountId());
		return badAccount == null ? new ArrayList<>()
				: walletSeqDAO.getWalletSeq2(badAccount.getUserId(), badAccount.getStartSeqId(),
						badAccount.getEndSeqId());

	}

}
