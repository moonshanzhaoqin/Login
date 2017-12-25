package com.yuyutechnology.exchange.server.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.dto.FriendDTO;
import com.yuyutechnology.exchange.manager.GoldpayTrans4MergeManager;
import com.yuyutechnology.exchange.manager.UserManager;

public class UserManagerTest extends BaseSpringJunit4 {

	@Autowired
	UserManager userManager;
	@Autowired
	RedisDAO redisDAO;
	
	@Autowired
	GoldpayTrans4MergeManager goldpayTrans4MergeManager;
	
	@Test
	public void testUpdate(){
		
		List<FriendDTO> fs = userManager.searchFriend(4, "1");
		for (FriendDTO friendDTO : fs) {
			System.out.println(friendDTO.getName());
		}
//		userManager.updateUser(2, "", "1", "");
//		userManager.updateHappyLivesVIP("123", 5);
//		System.out.println(userManager.isHappyLivesVIP(5));
//		GoldpayAccount account = goldpayTrans4MergeManager.getGoldpayUserAccount(13);
//		System.out.println(account.getAccountNum() + " : " + account.getBalance());
//		redisDAO.saveData("test", "asdcb123", DateFormatUtils.getIntervalMinute(new Date(), 2));
//		System.out.println(redisDAO.getValueByKey("test"));
	}
}
