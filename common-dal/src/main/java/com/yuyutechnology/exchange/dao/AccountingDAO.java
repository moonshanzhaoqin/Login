/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import java.util.Date;

/**
 * @author silent.sun
 *
 */
public interface AccountingDAO {

	public Integer snapshotWalletToNow(final Date stareDate, final Date endDate);

	public Integer accountingWalletSeq(final long seqIdStart, final long seqIdEnd, final Date startDate,
			final Date endDate);

	public Integer snapshotWalletNowToHistory();

	public void cleanSnapshotWalletNow();

	public Long getMAXSeqId4WalletNow();

	public Long getMAXSeqId4WalletBefore();
	
	public Long getMAXSeqId4WalletBeforeByUserId(final int userId);
	
	public Long getMAXSeqId4WalletUserId(final int userId);
	
	public Integer calculatorWalletSeqByUserId(final long seqIdStart, final long seqIdEnd, final Date startDate,
			final Date endDate, final int userId);
	
	public Date getLastAccountingTime();
	
	public void saveLastAccountingTime(Date time);

}
