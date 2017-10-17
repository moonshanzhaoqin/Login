/**
 * 
 */
package com.yuyutechnology.exchange.dao;

import com.yuyutechnology.exchange.pojo.Collect;

/**
 * @author suzan.wu
 *
 */
public interface CollectDAO {



	/**
	 * @param collectId
	 * @return
	 */
	Collect getCollect(Integer collectId);

	/**
	 * @param collect
	 */
	void updateCollect(Collect collect);

	/**
	 * 查找单条数据
	 * @param hql
	 * @param values
	 * @return
	 */
	Collect findHQL(String hql, Object[] values);

}
