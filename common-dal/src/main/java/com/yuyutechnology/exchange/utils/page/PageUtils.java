package com.yuyutechnology.exchange.utils.page;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

/** 
* @ClassName: PageUtils 
* @Description: 分页工具类
* @author Niklaus.Chi
* @date 2016年6月28日 下午4:59:23 
*  
*/
public class PageUtils {

	private static final Logger log = LogManager.getLogger(PageUtils.class);
	/** 
	* @Title: getBulletinList 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param hibernateTemplate
	* @param hql
	* @param values
	* @param currentPage
	* @param pageSize
	* @return    设定文件 
	* PageBean    返回类型 
	* @throws 
	*/
	public static PageBean getPageContent(HibernateTemplate hibernateTemplate,final String hql,
			final List<?> values,int currentPage,int pageSize) {
		
		int firstResult = (currentPage -1)*pageSize;
		int masResult = pageSize;
		List<?> list = PageUtils.getListByPage(hibernateTemplate, hql, values, firstResult, masResult);
		long total = PageUtils.getTotal(hibernateTemplate, hql, values);
		int pageTotal = PageUtils.getPageTotal(total, pageSize);
		
		PageBean pageBean = new PageBean(total, currentPage, pageSize, pageTotal, list);
		return pageBean;
		
	}
	
	/** 
	* @Title: getListByPage 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param  hibernateTemplate
	* @param  hql
	* @param  values
	* @param  firstResult
	* @param  masResult 
	* @return List<?>    返回类型 
	* @throws 
	*/
	public static List<?> getListByPage(HibernateTemplate hibernateTemplate,final String hql, 
			final List<?> values,final int firstResult, final int masResult) {
		
		List<?> list = hibernateTemplate.execute(new HibernateCallback<List<?>>() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				//设置参数
				if(values != null && !values.isEmpty()){
					for (int i = 0; i < values.size(); i++)
					{
						query.setParameter(i, values.get(i));
					}
				}
								//设置起点
				query.setFirstResult(firstResult);
				//设置每页显示多少个
				query.setMaxResults(masResult);
				
				return query.list();
			}
		});
		return list;
	}
	
	public static List<?> getListByPage4MySql(HibernateTemplate hibernateTemplate,final String mySql, 
			final List<?> values,final int firstResult, final int masResult) {
		
		List<?> list = new ArrayList<>();
		list = hibernateTemplate.executeWithNativeSession(new HibernateCallback<List<?>>() {

			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(mySql);
				//设置参数
				if(values != null && !values.isEmpty()){
					for (int i = 0; i < values.size(); i++)
					{
						query.setParameter(i, values.get(i));
					}
				}
				//设置起点
				query.setFirstResult(firstResult);
				//设置每页显示多少个
				query.setMaxResults(masResult);
				
				return query.list();
			}
		});

		return list;
	}
	
	/** 
	* @Title: getTotal 
	* @Description: 返回数据总条数 
	* @param hibernateTemplate
	* @param hql
	* @param values
	* @return    设定文件 
	* long    返回类型 
	* @throws 
	*/
	public static long getTotal(HibernateTemplate hibernateTemplate,final String hql, final List<?> values){
		//生成count sql
		StringBuilder totalHqlSb = new StringBuilder();
		totalHqlSb.append("select count(*) ").append(hql);
		final String totalhql = totalHqlSb.toString(); 
		log.info("生成Count语句 ： {} ",totalhql);
		
		long total = hibernateTemplate.execute(new HibernateCallback<Long>(){
					@Override
					public Long doInHibernate(Session session)
							throws HibernateException
					{
						Query query = session.createQuery(totalhql);
						
						if(values != null && !values.isEmpty()){
							for (int i = 0; i < values.size(); i++)
							{
								query.setParameter(i, values.get(i));
							}
						}
						List<?> list = query.list();
						if (list.isEmpty())
						{
							return 0L;
						}
						Long count = (Long) list.get(0);
						return count;
					}
				});
		
		log.info("符合条件的数据总数  ： {} ",total);
		
		return total;
	}
	
	
	public static long getTotal4MySql(HibernateTemplate hibernateTemplate,final String hql, final List<?> values){
		//生成count sql
		StringBuilder totalHqlSb = new StringBuilder();
		totalHqlSb.append("select count(*) ").append(hql);
		final String totalhql = totalHqlSb.toString(); 
		log.info("生成Count语句 ： {} ",totalhql);
		
		Long total = hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>(){
					@Override
					public Long doInHibernate(Session session)
							throws HibernateException
					{
						Query query = session.createSQLQuery(totalhql);
						
						if(values != null && !values.isEmpty()){
							for (int i = 0; i < values.size(); i++)
							{
								query.setParameter(i, values.get(i));
							}
						}
						BigInteger count=(BigInteger) query.uniqueResult();
						return count.longValue();
					}
				});
		
		log.info("符合条件的数据总数  ： {} ",total);
		
		return total;
	}
	
	
	
	/** 
	* @Title: getPageTotal 
	* @Description: 计算总页数
	* @param total
	* @param pageSize
	* @return  
	* int    返回类型 
	* @throws 
	*/
	public static int getPageTotal(long total,int pageSize){
		int pageTotal = (int) Math.ceil(total/(double)pageSize);
		return pageTotal>=1?pageTotal:1;
	}
	
	public static void main(String[] args){
		int retCode = getPageTotal(0, 10);
		log.info("retCode = {}",retCode);
	}
}
