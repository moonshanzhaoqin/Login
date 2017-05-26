/**
 * 
 */
package com.yuyutechnology.exchange.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.dao.CrmLogDAO;
import com.yuyutechnology.exchange.manager.CrmLogManager;
import com.yuyutechnology.exchange.pojo.CrmLog;

/**
 * @author suzan.wu
 *
 */
@Service
public class CrmLogManagerImpl implements CrmLogManager {
	@Autowired
	CrmLogDAO crmLogDAO;

	@Override
	public void saveCrmLog(CrmLog crmLog) {
		crmLogDAO.saveCrmLog(crmLog);

	}

}
