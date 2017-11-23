package com.yuyutechnology.exchange.manager;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dto.UserDTO;
import com.yuyutechnology.exchange.pojo.Unregistered;
import com.yuyutechnology.exchange.pojo.User;

@Service
public interface TaskManager {

	void transAndConfirm(String transferId, Integer userId, Unregistered unregistered, User payer, String comment);

	HashMap<String, Object> crtTransByUnregistered(UserDTO user, Unregistered unregistered);

}
