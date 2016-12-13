package com.yuyutechnology.exchange.session;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.UidUtils;

/**
 * 
 * @author sunwei
 * 
 */
@Component
public class SessionManager {
	@Resource
	RedisTemplate<String, String> sessionRedisTemplate;
	public static String SESSION_DATA_KEY = "session_data[sessionid]";
	public static String SESSION_DATA_KEY_USERID = "session_data[userid]";
	public static String LOGIN_TOKEN_USERID_KEY = "loginTokenUserId[:userid]";
	public static String LOGIN_TOKEN_TOKEN_KEY = "loginToken[:token]";

	/**
	 * 
	 * @param sessionData
	 */
	public void saveSessionData(SessionData sessionData) {
		String json = JsonBinder.getInstance().toJson(sessionData);
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionData.getSessionId());
		sessionRedisTemplate.opsForValue().set(key, json);
		sessionRedisTemplate.expire(key, 30, TimeUnit.MINUTES);
		if (sessionData.getUserId() != null) {
			saveSessionDataToUserId(sessionData);
		}
	}
	
	/**
	 * 
	 * @param sessionData
	 */
	public void refreshSessionDataExpireTime(String sessionId) {
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionId);
		sessionRedisTemplate.expire(key, 30, TimeUnit.MINUTES);
	}

	/**
	 * 
	 * @param sessionData
	 */
	public void saveSessionDataToUserId(SessionData sessionData) {
		String json = JsonBinder.getInstance().toJson(sessionData);
		String useridkey = StringUtils.replace(SESSION_DATA_KEY_USERID, "userid", sessionData.getUserId().toString());
		sessionRedisTemplate.opsForValue().set(useridkey, json);
		sessionRedisTemplate.expire(useridkey, 30, TimeUnit.MINUTES);
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
	public void logout(String sessionId) {
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionId);
		sessionRedisTemplate.delete(key);
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public SessionData getByUserid(String userId) {
		String key = StringUtils.replace(SESSION_DATA_KEY_USERID, "userid", userId);
		String jsonContent = sessionRedisTemplate.opsForValue().get(key);
		if (StringUtils.isEmpty(jsonContent)) {
			return null;
		} else {
			return JsonBinder.getInstance().fromJson(jsonContent, SessionData.class);
		}
	}

	public String createLoginToken(int userId) {
		String userIdKey = StringUtils.replace(LOGIN_TOKEN_USERID_KEY, ":userid", userId+"");
		String loginToken = DigestUtils.md5Hex(UidUtils.genUid());
		String key = StringUtils.replace(LOGIN_TOKEN_TOKEN_KEY, ":token", loginToken);
		sessionRedisTemplate.opsForValue().set(userIdKey, loginToken);
		sessionRedisTemplate.opsForValue().set(key, userId+"");
		sessionRedisTemplate.expire(userIdKey, 7, TimeUnit.DAYS);
		sessionRedisTemplate.expire(key, 7, TimeUnit.DAYS);
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
		String userIdKey = StringUtils.replace(LOGIN_TOKEN_USERID_KEY, ":userid", userId+"");
		String token = sessionRedisTemplate.opsForValue().get(userIdKey);
		if (StringUtils.isNotBlank(token)) {
			sessionRedisTemplate.delete(userIdKey);
			String tokenKey = StringUtils.replace(LOGIN_TOKEN_TOKEN_KEY, ":token", token);
			sessionRedisTemplate.delete(tokenKey);
		}
	}
}