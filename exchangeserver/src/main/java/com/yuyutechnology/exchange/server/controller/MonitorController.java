/**
 * 
 */
package com.yuyutechnology.exchange.server.controller;

import java.sql.Timestamp;
import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyutechnology.exchange.util.JsonBinder;

/**
 * @author silent.sun
 *
 */
@Controller
public class MonitorController {
	
	@Resource
	HibernateTemplate hibernateTemplate;
	
	@Resource
	DataSource dataSource;
	
	@Resource
	RedisTemplate<String, String> commonRedisTemplate;
	
	public static Logger logger = LogManager.getLogger(PayPalTransController.class);
	
	@RequestMapping(method = RequestMethod.POST, value = "/getServerInfo")
	public @ResponseBody String getServerInfo(){
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.setServerTime(new Date().toString());
		try {
			hibernateTemplate.executeWithNativeSession(new HibernateCallback<Timestamp>() {
				@Override
				public Timestamp doInHibernate(Session session) throws HibernateException {
					Query query = session.createSQLQuery("select now()");
					return (Timestamp) query.list().get(0);
				}
			});
		} catch (Exception e) {
			serverInfo.setDb(e.getMessage());
		}
		try {
			commonRedisTemplate.opsForValue().set("monitorValue", "1");
			commonRedisTemplate.opsForValue().get("monitorValue");
		} catch (Exception e) {
			serverInfo.setDb(e.getMessage());
		}
		return JsonBinder.getInstance().toJson(serverInfo);
	}
	
    public class ServerInfo {
    	private String serverTime;
        private String db = "ok";
        private String redis = "ok";
		public String getServerTime() {
			return serverTime;
		}
		public void setServerTime(String serverTime) {
			this.serverTime = serverTime;
		}
		public String getDb() {
			return db;
		}
		public void setDb(String db) {
			this.db = db;
		}
		public String getRedis() {
			return redis;
		}
		public void setRedis(String redis) {
			this.redis = redis;
		}
    }
}


