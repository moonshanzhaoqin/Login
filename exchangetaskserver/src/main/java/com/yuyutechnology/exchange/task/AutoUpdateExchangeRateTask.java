package com.yuyutechnology.exchange.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.dto.UserDTO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.TransDetailsManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Transfer;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;

@Component
public class AutoUpdateExchangeRateTask {

	@Autowired
	UnregisteredDAO unregisteredDAO;
	
	
	
	@Autowired
	UserManager userManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	TransferManager transferManager;
	@Autowired
	OandaRatesManager oandaRatesManager;
	@Autowired
	TransDetailsManager transDetailsManager;
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;

	
	public static Logger logger = LogManager.getLogger(AutoUpdateExchangeRateTask.class);
	
	public void autoUpdateExchangeRateTask(){
		logger.info("=============autoUpdateExchangeRateTask Start=================={}",new SimpleDateFormat("HH:mm:ss").format(new Date()));
		oandaRatesManager.updateExchangeRates();
		logger.info("=============End at {}==================",new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}
	
	public void autoSystemRefundBatch(){
		logger.info("=============autoSystemRefundBatch Start=============={}",new SimpleDateFormat("HH:mm:ss").format(new Date()) );
//		transferManager.systemRefundBatch();
		// 获取所有未完成的订单
		List<Unregistered> list = unregisteredDAO.getAllUnfinishedTransaction();
		if (list.isEmpty()) {
			return;
		}
		for (Unregistered unregistered : list) {
			
			// 判断是否超过期限
			long deadline = configManager.getConfigLongValue(ConfigKeyEnum.REFUNTIME, 3l) * 24 * 60 * 60 * 1000;
			if (new Date().getTime() - unregistered.getCreateTime().getTime() >= deadline) {

				logger.info("deadline : {},Difference : {}", deadline,
						new Date().getTime() - unregistered.getCreateTime().getTime());

				logger.info(
						"Invitation ID: {}, The invitee has not registered for the due "
								+ "date and the system is being refunded ,{}",
						unregistered.getUnregisteredId(), new SimpleDateFormat("HH:mm:ss").format(new Date()));

				String transferId = transferManager.systemRefundStep1(unregistered);
				if(StringUtils.isNotBlank(transferId)){
					transferManager.systemRefundStep2(transferId,unregistered);
				}
			}
		}
		logger.info("=============End at {}==================",new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}
	
}
