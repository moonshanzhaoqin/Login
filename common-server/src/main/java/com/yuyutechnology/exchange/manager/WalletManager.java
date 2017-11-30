package com.yuyutechnology.exchange.manager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.yuyutechnology.exchange.dto.WalletInfo;
import com.yuyutechnology.exchange.util.page.PageBean;

public interface WalletManager {

	/**
	 * @Descrition : 根据用户id获取账号资产对应的黄金总量
	 * @author : nicholas.chi
	 * @time : 2016年12月17日 上午10:36:38
	 * @param userId
	 * @return
	 */
	HashMap<String, BigDecimal> getTotalAmoutGold(int userId);

	/**
	 * @Descrition : 根据UserId获取用户的wallets
	 * @author : nicholas.chi
	 * @time : 2016年12月5日 下午4:17:36
	 * @param userId
	 * @return
	 */
	List<WalletInfo> getWalletsByUserId(int userId);

	/**
	 * 获取用户总账
	 * 
	 * @param systemUserId
	 */
	void getUserTotalBalance(int systemUserId);

	/**
	 * 分页获取坏账记录
	 * 
	 * @param parseInt
	 * @return
	 */
	PageBean getBadAccountByPage(int parseInt);

	/**
	 * 根据badAccountId获取坏账流水
	 * 
	 * @param badAccountId
	 * @return
	 */
	List<?> getDetailSeq(Integer badAccountId);

	/**
	 * 根据transferId获取坏账流水
	 * 
	 * @param transferId
	 * @return
	 */
	List<?> getDetailSeqByTransferId(String transferId);

}
