package com.yuyutechnology.exchange.crm.session;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.yuyutechnology.exchange.util.ResourceUtils;

/**
 * @author silent.sun
 *
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=1200)
public class SessionConfig {
	
    @Bean  
    public JedisConnectionFactory connectionFactory()  
    {  
        JedisConnectionFactory connection = new JedisConnectionFactory();  
        connection.setHostName(ResourceUtils.getBundleValue4String("session.redis.host"));  
        connection.setPort(ResourceUtils.getBundleValue4Long("session.redis.port").intValue());  
        connection.setDatabase(ResourceUtils.getBundleValue4Long("session.redis.index", 0l).intValue());
        return connection;  
    }
    
    @Bean 
    public static ConfigureRedisAction configureRedisAction() {     
    	return ConfigureRedisAction.NO_OP;
    }
}
