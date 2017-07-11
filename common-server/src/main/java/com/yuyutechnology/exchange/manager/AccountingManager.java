/**
 * 
 */
package com.yuyutechnology.exchange.manager;

import java.util.Date;

/**
 * @author silent.sun
 *
 */
public interface AccountingManager {

	public int accountingAll();

	public boolean accountingUser(int userId, String transferId);

	public int accounting(Date startDate, Date endDate);

	public void freezeUsers();

	public void snapshotToBefore(int userId);

}
