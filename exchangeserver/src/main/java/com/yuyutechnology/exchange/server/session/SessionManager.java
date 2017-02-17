package com.yuyutechnology.exchange.server.session;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	public static Logger logger = LoggerFactory.getLogger(SessionManager.class);
	
	@Autowired
	RedisDAO redisDAO;

	public static String SESSION_DATA_KEY = "session_data[sessionid]";
	public static String SESSION_DATA_KEY_USERID = "session_data[userid]";
	public static String LOGIN_TOKEN_USERID_KEY = "loginTokenUserId[:userid]";
	public static String LOGIN_TOKEN_TOKEN_KEY = "loginToken[:token]";
	public static String CHECK_TOKEN_KEY = "checkToken[:purpose][:userid]";

	/**
	 * 
	 * @param sessionData
	 */
	public void saveSessionData(SessionData sessionData) {
		saveSessionData(sessionData, false);
	}

	/**
	 * 
	 * @param sessionData
	 */
	public void saveSessionData(SessionData sessionData, boolean allowRepeatLogin) {
		String json = JsonBinder.getInstance().toJson(sessionData);
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionData.getSessionId());
		redisDAO.saveData(key, json, ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l), TimeUnit.MINUTES);
		if (sessionData.getUserId() != null) {
			if (!allowRepeatLogin) {
				repeatLogin(sessionData.getUserId());
			}
			saveSessionDataToUserId(sessionData);
		}
	}

	/**
	 * 
	 * @param sessionData
	 */
	public void refreshSessionDataExpireTime(String sessionId) {
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionId);
		redisDAO.expireData(key, ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l), TimeUnit.MINUTES);
	}

	/**
	 * 
	 * @param sessionData
	 */
	public void saveSessionDataToUserId(SessionData sessionData) {
		String json = JsonBinder.getInstance().toJson(sessionData);
		String useridkey = StringUtils.replace(SESSION_DATA_KEY_USERID, "userid", sessionData.getUserId().toString());
		redisDAO.saveData(useridkey, json, ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l), TimeUnit.MINUTES);
	}

	/**
	 * 
	 * @param sessionId
	 * @return
	 */
	public SessionData get(String sessionId) {
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionId);
		String jsonContent = redisDAO.getValueByKey(key);
		if (StringUtils.isEmpty(jsonContent)) {
			return null;
		} else {
			return JsonBinder.getInstance().fromJson(jsonContent, SessionData.class);
		}
	}

	/**
	 * 
	 * @param sessionId
	 */
	public void cleanSession(String sessionId) {
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionId);
		redisDAO.deleteData(key);
	}

	/**
	 * @param userId
	 */
	public void repeatLogin(int userId) {
		SessionData session = getByUserid(userId);
		if (session != null && StringUtils.isNotEmpty(session.getSessionId())) {
			String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", session.getSessionId());
			redisDAO.deleteData(key);
		}
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

	public String createLoginToken(int userId) {
		delLoginToken(userId);
		String userIdKey = StringUtils.replace(LOGIN_TOKEN_USERID_KEY, ":userid", userId + "");
		String loginToken = DigestUtils.md5Hex(UidUtils.genUid());
		String key = StringUtils.replace(LOGIN_TOKEN_TOKEN_KEY, ":token", loginToken);
		redisDAO.saveData(userIdKey, loginToken, ResourceUtils.getBundleValue4Long("login.token.timeout.day", 7l), TimeUnit.DAYS);
		redisDAO.saveData(key, userId + "", ResourceUtils.getBundleValue4Long("login.token.timeout.day", 7l), TimeUnit.DAYS);
		return loginToken;
	}

	public int validateLoginToken(String loginToken) {
		int userId = 0;
		String tokenKey = StringUtils.replace(LOGIN_TOKEN_TOKEN_KEY, ":token", loginToken);
		String userIdString = redisDAO.getValueByKey(tokenKey);
		try {
			userId = Integer.valueOf(userIdString);
			delLoginToken(userId);
		} catch (NumberFormatException e) {
		}
		return userId;
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

	public String createCheckToken(Integer userId, String purpose) {
		String checkToken = DigestUtils.md5Hex(UidUtils.genUid());
		String key = StringUtils.replace(CHECK_TOKEN_KEY, ":userid", userId + "");
		key = StringUtils.replace(key, ":purpose", purpose);
		redisDAO.saveData(key, checkToken, ResourceUtils.getBundleValue4Long("check.token.timeout.minutes", 5l), TimeUnit.MINUTES);
		return checkToken;
	}

	public boolean validateCheckToken(Integer userId, String purpose, String checkToken) {
		String key = StringUtils.replace(CHECK_TOKEN_KEY, ":userid", userId + "");
		key = StringUtils.replace(key, ":purpose", purpose);
		if (checkToken.equals(redisDAO.getValueByKey(key))) {
			return true;
		}
		return false;
	}

	public void delCheckToken(Integer userId, String purpose) {
		String key = StringUtils.replace(CHECK_TOKEN_KEY, ":userid", userId + "");
		key = StringUtils.replace(key, ":purpose", purpose);
		redisDAO.deleteData(key);
	}
}