package com.yuyutechnology.exchange.utils.page;

import java.util.List;

/** 
* @ClassName: PageBean 
* @Description: 分页返回
* @author Niklaus.Chi
* @date 2016年6月28日 下午4:35:46 
*  
*/
public class PageBean {
	
	private long total;			//数据条数
	private int currentPage;	//当前页码
	private int pageSize;		//单页数据量
	private int pageTotal;		//页数

	private List<?> rows;
	
	public PageBean() {
		super();
	}

	public PageBean(long total, int currentPage, int pageSize, int pageTotal, List<?> rows) {
		super();
		this.total = total;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.pageTotal = pageTotal;
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

}
