package com.yuyutechnology.exchange.server.session;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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

	@Resource
	RedisTemplate<String, String> sessionRedisTemplate;
	public static String SESSION_DATA_KEY = "session_data[sessionid]";
	// public static String REPEAT_LOGIN_SESSION_DATA_KEY =
	// "repeat_login_session_data[:sessionid]";
	public static String SESSION_DATA_KEY_USERID = "session_data[userid]";
	public static String LOGIN_TOKEN_USERID_KEY = "loginTokenUserId[:userid]";
	public static String LOGIN_TOKEN_TOKEN_KEY = "loginToken[:token]";

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
		sessionRedisTemplate.opsForValue().set(key, json);
		sessionRedisTemplate.expire(key, ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l).intValue(),
				TimeUnit.MINUTES);
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
		sessionRedisTemplate.expire(key, ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l).intValue(),
				TimeUnit.MINUTES);
	}

	/**
	 * 
	 * @param sessionData
	 */
	public void saveSessionDataToUserId(SessionData sessionData) {
		String json = JsonBinder.getInstance().toJson(sessionData);
		String useridkey = StringUtils.replace(SESSION_DATA_KEY_USERID, "userid", sessionData.getUserId().toString());
		sessionRedisTemplate.opsForValue().set(useridkey, json);
		sessionRedisTemplate.expire(useridkey,
				ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l).intValue(), TimeUnit.MINUTES);
	}

	/**
	 * 
	 * @param sessionId
	 * @return
	 */
	public SessionData get(String sessionId) {
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionId);
		String jsonContent = sessionRedisTemplate.opsForValue().get(key);
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
		sessionRedisTemplate.delete(key);
	}

	/**
	 * @param userId
	 */
	public void repeatLogin(int userId) {
		SessionData session = getByUserid(userId);
		if (session != null && StringUtils.isNotEmpty(session.getSessionId())) {
			String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", session.getSessionId());
			sessionRedisTemplate.delete(key);
		}
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public SessionData getByUserid(int userId) {
		String key = StringUtils.replace(SESSION_DATA_KEY_USERID, "userid", userId + "");
		String jsonContent = sessionRedisTemplate.opsForValue().get(key);
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
		sessionRedisTemplate.opsForValue().set(userIdKey, loginToken);
		sessionRedisTemplate.opsForValue().set(key, userId + "");
		sessionRedisTemplate.expire(userIdKey, ResourceUtils.getBundleValue4Long("login.token.timeout.day", 7l),
				TimeUnit.DAYS);
		sessionRedisTemplate.expire(key, ResourceUtils.getBundleValue4Long("login.token.timeout.day", 7l),
				TimeUnit.DAYS);
		return loginToken;
	}

	public int validateLoginToken(String loginToken) {
		int userId = 0;
		String tokenKey = StringUtils.replace(LOGIN_TOKEN_TOKEN_KEY, ":token", loginToken);
		String userIdString = sessionRedisTemplate.opsForValue().get(tokenKey);
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
			String token = sessionRedisTemplate.opsForValue().get(userIdKey);
			if (StringUtils.isNotBlank(token)) {
				sessionRedisTemplate.delete(userIdKey);
				String tokenKey = StringUtils.replace(LOGIN_TOKEN_TOKEN_KEY, ":token", token);
				sessionRedisTemplate.delete(tokenKey);
			}
		}
	}

	public String createCheckToken(Integer userId, String purpose) {
		String checkToken = DigestUtils.md5Hex(UidUtils.genUid());
		sessionRedisTemplate.opsForValue().set(purpose + userId, checkToken);
		return checkToken;
	}

	public boolean validateCheckToken(Integer userId, String purpose, String checkToken) {
		if (checkToken.equals(sessionRedisTemplate.opsForValue().get(purpose + userId))) {
			return true;
		}
		return false;
	}

	public void delCheckToken(Integer userId, String purpose) {
		sessionRedisTemplate.delete(purpose + userId);
	}
}