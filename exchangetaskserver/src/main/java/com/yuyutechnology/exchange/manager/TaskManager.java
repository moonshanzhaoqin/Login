package com.yuyutechnology.exchange.manager;

import java.util.HashMap;

import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;


public interface TaskManager {

	void transAndConfirm(String transferId, Integer userId, Unregistered unregistered, User payer, String comment);

	HashMap<String, Object> crtTransByUnregistered(User user, Unregistered unregistered);

}
