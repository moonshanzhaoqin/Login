package com.yuyutechnology.exchange.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.UnregisteredDAO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.manager.TaskManager;
import com.yuyutechnology.exchange.manager.TransferManager;
import com.yuyutechnology.exchange.manager.UserManager;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;

public class AutoTaskTest extends BaseSpringJunit4 {
	
	@Autowired
	UnregisteredDAO unregisteredDAO;
	@Autowired
	TaskManager taskManager;
	@Autowired
	UserManager userManager;
	@Autowired
	ConfigManager configManager;
	@Autowired
	TransferManager transferManager;
	@Autowired
	OandaRatesManager oandaRatesManager;

	@Test
	public void test () {
		
		List<Unregistered> list = unregisteredDAO.getAllUnfinishedTransaction();
		if (list.isEmpty()) {
			return;
		}
		
		for (Unregistered unregistered : list) {
			
			User user = userManager.getUserByPhone(unregistered.getAreaCode(), unregistered.getUserPhone());
			
			if(user != null ){

				System.out.println("user phone: {} has been registed"+user.getUserName());
				HashMap<String, Object> reuslt = taskManager.crtTransByUnregistered(user,unregistered);
				if(ServerConsts.GOLDPAY_RETURN_SUCCESS == ((int)reuslt.get("retCode"))){
					taskManager.transAndConfirm(((String)reuslt.get("transferId")), user.getUserId(),
							unregistered, ((User)reuslt.get("payer")), ((String)reuslt.get("comment")));
				}
			}else{
				// 判断是否超过期限
				long deadline = configManager.getConfigLongValue(ConfigKeyEnum.REFUNTIME, 3l) * 24 * 60 * 60 * 1000;
				if (new Date().getTime() - unregistered.getCreateTime().getTime() >= deadline) {

					
					String transferId = transferManager.systemRefundStep1(unregistered);
					if(StringUtils.isNotBlank(transferId)){
						transferManager.systemRefundStep2(transferId,unregistered);
					}
				}
			}

		}
		
		
	}
	
}
