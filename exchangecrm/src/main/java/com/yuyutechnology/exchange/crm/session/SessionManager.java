package com.yuyutechnology.exchange.crm.session;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;
import com.yuyutechnology.exchange.utils.UidUtils;

/**
 * 
 * @author sunwei
 * 
 */
@Component
public class SessionManager {
	public static Logger logger = LogManager.getLogger(SessionManager.class);

	@Autowired
	RedisDAO redisDAO;

	public static String SESSION_DATA_KEY = "session_data[sessionid]";
	public static String SESSION_DATA_KEY_USERID = "session_data[userid]";
	public static String LOGIN_TOKEN_USERID_KEY = "loginTokenUserId[:userid]";
	public static String LOGIN_TOKEN_TOKEN_KEY = "loginToken[:token]";

	/**
	 * 
	 * @param sessionId
	 */
	public void cleanSession(String sessionId) {
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionId);
		redisDAO.deleteData(key);
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public SessionData getByUserid(int userId) {
		String key = StringUtils.replace(SESSION_DATA_KEY_USERID, "userid", userId + "");
		String jsonContent = redisDAO.getValueByKey(key);
		if (StringUtils.isEmpty(jsonContent)) {
			return null;
		} else {
			return JsonBinder.getInstance().fromJson(jsonContent, SessionData.class);
		}
	}

	public void delLoginToken(int userId) {
		if (userId != 0) {
			String userIdKey = StringUtils.replace(LOGIN_TOKEN_USERID_KEY, ":userid", userId + "");
			String token = redisDAO.getValueByKey(userIdKey);
			if (StringUtils.isNotBlank(token)) {
				redisDAO.deleteData(userIdKey);
				String tokenKey = StringUtils.replace(LOGIN_TOKEN_TOKEN_KEY, ":token", token);
				redisDAO.deleteData(tokenKey);
			}
		}
	}

}