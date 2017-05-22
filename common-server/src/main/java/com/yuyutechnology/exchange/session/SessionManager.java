package com.yuyutechnology.exchange.session;

import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yuyutechnology.exchange.dao.RedisDAO;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;
import com.yuyutechnology.exchange.util.UidUtils;

/**
 * 
 * @author silent.sun
 * 
 */
/**
 * @author suzan.wu
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
		// logger.info("saveData : {}={}", key,json);
		redisDAO.saveData(key, json, ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l),
				TimeUnit.MINUTES);
		if (sessionData.getUserId() != null) {
			if (!allowRepeatLogin) {
				logger.info("repeatLogin");
				repeatLogin(sessionData.getUserId());
			}
			saveSessionDataToUserId(sessionData);
		}
	}

	/**
	 * 
	 * @param sessionData
	 */
	public void refreshSessionDataExpireTime(SessionData sessionData) {
		String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", sessionData.getSessionId());
		// logger.info("expireData : {}",key);
		redisDAO.expireData(key, ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l), TimeUnit.MINUTES);
		String userkey = StringUtils.replace(SESSION_DATA_KEY_USERID, "userid", sessionData.getUserId().toString());
		// logger.info("expireData : {}",userkey);
		redisDAO.expireData(userkey, ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l),
				TimeUnit.MINUTES);
	}

	/**
	 * 
	 * @param sessionData
	 */
	public void saveSessionDataToUserId(SessionData sessionData) {
		String json = JsonBinder.getInstance().toJson(sessionData);
		String useridkey = StringUtils.replace(SESSION_DATA_KEY_USERID, "userid", sessionData.getUserId().toString());
		// logger.info("saveData : {}={}",useridkey,json);
		redisDAO.saveData(useridkey, json, ResourceUtils.getBundleValue4Long("session.timeout.minuate", 15l),
				TimeUnit.MINUTES);
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
		// logger.info("deleteData : {}",key);
		redisDAO.deleteData(key);
	}

	/**
	 * @param userId
	 */
	public void repeatLogin(int userId) {
		SessionData session = getByUserid(userId);
		if (session != null && StringUtils.isNotEmpty(session.getSessionId())) {
			String key = StringUtils.replace(SESSION_DATA_KEY, "sessionid", session.getSessionId());
			// logger.info("deleteData : {}",key);
			redisDAO.deleteData(key);
		}
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	/**
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

	/**
	 * @param userId
	 * @return
	 */
	public String createLoginToken(int userId) {
		delLoginToken(userId);
		String userIdKey = StringUtils.replace(LOGIN_TOKEN_USERID_KEY, ":userid", userId + "");
		String loginToken = DigestUtils.md5Hex(UidUtils.genUid());
		String key = StringUtils.replace(LOGIN_TOKEN_TOKEN_KEY, ":token", loginToken);
		redisDAO.saveData(userIdKey, loginToken, ResourceUtils.getBundleValue4Long("login.token.timeout.day", 7l),
				TimeUnit.DAYS);
		redisDAO.saveData(key, userId + "", ResourceUtils.getBundleValue4Long("login.token.timeout.day", 7l),
				TimeUnit.DAYS);
		return loginToken;
	}

	/**
	 * @param loginToken
	 * @return
	 */
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

	/**
	 * @param userId
	 */
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

	/**
	 * @param userId
	 * @param purpose
	 * @return
	 */
	public String createCheckToken(Integer userId, String purpose) {
		String checkToken = DigestUtils.md5Hex(UidUtils.genUid());
		String key = CHECK_TOKEN_KEY.replace(":userid", userId.toString()).replace(":purpose", purpose);
		redisDAO.saveData(key, checkToken, ResourceUtils.getBundleValue4Long("check.token.timeout.minutes", 5l),
				TimeUnit.MINUTES);
		return checkToken;
	}

	/**
	 * 验证checkToken
	 * 
	 * @param userId
	 * @param purpose
	 * @param checkToken
	 * @return
	 */
	public boolean validateCheckToken(Integer userId, String purpose, String checkToken) {
		String key = CHECK_TOKEN_KEY.replace(":userid", userId.toString()).replace(":purpose", purpose);
		if (checkToken.equals(redisDAO.getValueByKey(key))) {
			return true;
		}
		return false;
	}

	/**
	 * @param userId
	 * @param purpose
	 */
	public void delCheckToken(Integer userId, String purpose) {
		String key = CHECK_TOKEN_KEY.replace(":userid", userId.toString()).replace(":purpose", purpose);
		redisDAO.deleteData(key);
	}
}