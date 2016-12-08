package com.yuyutechnology.exchange.session;

import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.utils.JsonBinder;

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
}