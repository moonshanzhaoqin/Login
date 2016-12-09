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
	public static String LOGIN_TOKEN_KEY = "loginToken[userid]";

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

	/**
	 * 
	 * @param userId
	 */
	
	public void logoutUserid(String userId) {
		String key = StringUtils.replace(SESSION_DATA_KEY_USERID, "userid", userId);
		sessionRedisTemplate.delete(key);
	}
	
	public String createLoginToken(int userId) {
		String key = StringUtils.replace(LOGIN_TOKEN_KEY, "userid", userId+"");
		String loginToken = UidUtils.genUid();
		sessionRedisTemplate.opsForValue().set(key, DigestUtils.md5Hex(loginToken));
		sessionRedisTemplate.expire(key, 7, TimeUnit.DAYS);
		return loginToken;
	}
	
	public boolean validateLoginToken(int userId, String loginToken) {
		String key = StringUtils.replace(LOGIN_TOKEN_KEY, "userid", userId+"");
		String tokenMD5 = sessionRedisTemplate.opsForValue().get(key);
		if (StringUtils.isNotBlank(loginToken) && StringUtils.isNotBlank(tokenMD5) && DigestUtils.md5Hex(loginToken).equals(tokenMD5)) {
			return true;
		}
		return false;
	}
	
	public void delLoginToken(int userId) {
		String key = StringUtils.replace(LOGIN_TOKEN_KEY, "userid", userId+"");
		sessionRedisTemplate.delete(key);
	}
}