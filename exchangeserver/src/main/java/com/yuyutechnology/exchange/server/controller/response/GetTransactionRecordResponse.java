package com.yuyutechnology.exchange.server.controller.response;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.yuyutechnology.exchange.dto.TransferDTO;

@ApiModel(value="retCode=00000,03007")
public class GetTransactionRecordResponse extends BaseResponse {

	private int currentPage;
	private int pageSize;
	private int total;
	private int pageTotal;
	private List<TransferDTO> list;

	@ApiModelProperty(value = "当前页",required=true)
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	@ApiModelProperty(value = "每页条目数量",required=true)
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	@ApiModelProperty(value = "条目总数",required=true)
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	@ApiModelProperty(value = "总页数",required=true)
	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	@ApiModelProperty(value = "",required=true)
	public List<TransferDTO> getList() {
		return list;
	}

	public void setList(List<TransferDTO> list) {
		this.list = list;
	}

}
