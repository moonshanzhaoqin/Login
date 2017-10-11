/**
 * 
 */
package com.yuyutechnology.exchange.merge;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Service;

/**
 * @author silent.sun
 *
 */
@Service
public class ExanytimeMergeManager {
	
	public static Logger logger = LogManager.getLogger(ExanytimeMergeManager.class);
	
	@Resource
	HibernateTemplate hibernateTemplate;
	
	public void mergeExUserGoldpayToGoldpayServer(Integer userId) {
		//TODO:
	}
}
