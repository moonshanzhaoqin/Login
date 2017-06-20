<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp"%>
<body>
	<div class="container">
		<div class="row">
			<form class="form-inline " id="searchWithdraw">
				<div class="form-group">
					<label class="sr-only" for="userPhone">userPhone</label> <input
						type="text" class="form-control" name="userPhone"
						placeholder="手机号">
				</div>
				<div class="form-group">
					<label class="sr-only" for="transferId">transferId</label> <input
						type="text" class="form-control" name="transferId"
						placeholder="交易号">
				</div>
				<button type="button" class="btn btn-primary pull-right"
					onclick="searchWithdraw(1)">搜索</button>
				<div class="form-group">
					<label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="1" checked>待审核
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="2">已完成
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="3">已退回
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="4" checked>待支付
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="5" checked>审核失败
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="8" checked>支付失败
					</label>
				</div>
			</form>
		</div>

		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="withdraw">
				<thead>
					<tr>
						<th>用户ID</th>
						<th>国家码</th>
						<th>手机号</th>
						<th>交易号</th>
						<th>交易数量(GDQ)</th>
						<th>交易状态</th>
						<th>操作</th>
						<th>创建时间（GMT+8）</th>
						<th>最新更新时间（GMT+8）</th>
						<th>坏账详情</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
		</div>
		<hr style="background-color: grey; height: 1px;" />
		<div id="detail" class="row"
			style="height: 400px; overflow: auto; width: 100%;">
			<table class="table table-bordered table-hover table-striped"
				id="walletSeq">
				<thead>
					<tr>
						<th>流水号</th>
						<th>币种</th>
						<th>数量</th>
						<th>交易类型</th>
						<th>交易ID</th>
						<th>创建时间（GMT+8）</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>

	<%@ include file="badAccountDetail.jsp"%>

	<script>
		$(function() {
			var userPhone, transferId, transferStatus = [];
			//页面初始化，加载数据
			searchWithdraw(1);
		});

		function searchWithdraw(page) {
			console.log("searchWithdraw:page=" + page);
			form = document.getElementById("searchWithdraw");
			userPhone = form.userPhone.value;
			transferId = form.transferId.value;
			obj = form.transferStatus;
			transferStatus = [];
			for (k in obj) {
				if (obj[k].checked)
					transferStatus.push(obj[k].value);
			}
			console.log(transferStatus);
			getWithdrawList(page, userPhone, transferId, transferStatus);
		}

		//分页
		function paginator(currentPage, pageTotal) {
			console.log("paginator:currentPage=" + currentPage + ",pageTotal="
					+ pageTotal);
			var options = {
				currentPage : currentPage,//当前页
				totalPages : pageTotal,//总页数
				size : 'normal',
				alignment : 'right',
				numberOfPages : 10,//显示页数
				itemTexts : function(type, page, current) {
					switch (type) {
					case "first":
						return "<<";
					case "prev":
						return "<";
			            case "next":
			                return ">";
					case "last":
						return ">>";
					case "page":
						return "" + page;
					}
				},
				onPageClicked : function(event, originalEvent, type, page) {
					getWithdrawList(page, userPhone, transferId, transferStatus);
				}
			}
			//分页控件
			$('#paginator').bootstrapPaginator(options);
		}

		function getWithdrawList(currentPage, userPhone, transferId,
				transferStatus) {
			form = document.getElementById("searchWithdraw");
			form.userPhone.value = userPhone;
			form.transferId.value = transferId;
			$('input:checkbox').each(function() {
				$(this).attr("checked", false);
			});
			for (i in transferStatus) {
				$("input:checkbox[value='" + transferStatus[i] + "']").prop(
						"checked", true);
			}
			var data = {
				currentPage : currentPage,
				userPhone : userPhone,
				transferId : transferId,
				transferStatus : transferStatus
			};

			$
					.ajax({
						type : "post",
						url : "/crm/getWithdrawList",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(data),
						success : function(data) {
							console.log(data);
							if (data.retCode == "00002") {
								location.href = loginUrl;
							} else {
								console.log("success");
								var html = "";
								for ( var i in data.rows) {
									html += '<tr id="'+data.rows[i][0].transferId+'">'
											+ '<td>'
											+ data.rows[i][0].userFrom
											+ '</td>'
											+ '<td>'
											+ data.rows[i][1].areaCode
											+ '</td>'
											+ '<td>'
											+ data.rows[i][1].userPhone
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].transferId
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].transferAmount
											+ '</td>'
											+ status(
													data.rows[i][0].transferId,
													data.rows[i][0].transferStatus)
											+ '<td>'
											+ timeDate(data.rows[i][0].createTime)
											+ '</td>'
											+ '<td>'
											+ timeDate(data.rows[i][0].finishTime)
											+ '</td>'
											+ '<td>'
											+ (data.rows[i][0].transferStatus == 5 ? ('<a href="javascript:void(0)" onclick="getDetailSeqByTransferId(\''
											+ data.rows[i][0].transferId + '\')">详情</a>')
											: '' )+ '</td>' + '</tr>'
								}
								$('#withdraw tbody').html(html);
								if (data.currentPage == 1) {
									paginator(data.currentPage, data.pageTotal);
								}
							}
						},
						error : function(xhr, err) {
							alert("未知错误");
							console.log(err);
						}
					});
		}
		function getDetailSeqByTransferId(transferId) {
			$("tr").removeClass("success");
			$("#" + transferId).addClass("success");

			var data = {
				transferId : transferId
			};
			$
					.ajax({
						type : "post",
						url : "/crm/getDetailSeqByTransferId",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(data),
						success : function(data) {
							if (data.retCode == "00002") {
								location.href = loginUrl;
							} else {
								console.log("success");
								var html = "";
								for ( var i in data) {
									html += '<tr>'
											+ '<td>'
											+ data[i].seqId
											+ '</td>'
											+ '<td>'
											+ data[i].currency
											+ '</td>'
											+ '<td>'
											+ new Number(data[i].amount)
													.toFixed(4)
											+ '</td>'
											+ '<td>'
											+ transferTypeName(data[i].transferType)
											+ '</td>'
											+ '<td>'
											+ ((data[i].transferType == "1") ? ('<a href="" data-toggle="modal" data-target="#exchange" onclick= "getExchange(\''
													+ data[i].transactionId
													+ '\')">    '
													+ data[i].transactionId + '</a>')
													: ('<a href="" data-toggle="modal" data-target="#transfer" onclick= "getTransfer(\''
															+ data[i].transactionId
															+ '\')">    '
															+ data[i].transactionId + '</a>'))
											+ '</td>' + '<td>'
											+ timeDate(data[i].createTime)
											+ '</td>' + '</tr>'
								}
								if (html == "") {
									alert("没有坏账")
								}
								$('#walletSeq tbody').html(html);
								$('#detail').show();
							}
						},
						error : function(xhr, err) {
							alert("未知错误");
							console.log(err);
						},
						async : false
					});
		}

		//审批
		function withdrawReview(transferId) {
			if (confirm("确认重新审核吗？")) {
				console.log("withdrawReview" + transferId);
				data = {
					transferId : transferId
				};
				$.ajax({
					type : "post",
					url : "/crm/withdrawReview",
					dataType : 'json',
					contentType : "application/json; charset=utf-8",
					data : JSON.stringify(data),
					success : function(data) {
						if (data.retCode == "00000") {
							alert("操作成功");
							searchWithdraw($("#paginator .active a").html());
						} else if (data.retCode == "00002") {
							location.href = loginUrl;
						} else {
							console.log("withdrawReview" + data.message);
							alert("操作失败");
							searchWithdraw($("#paginator .active a").html());
						}
					},
					error : function(xhr, err) {
						console.log("error");
						console.log(err);
						console.log(xhr);
						alert("未知错误");
					},
					async : false
				});
			}
		}
		//划账
		function goldpayRemit(transferId) {
			if (confirm("确认重新支付吗？")) {
				data = {
					transferId : transferId
				};
				$.ajax({
					type : "post",
					url : "/crm/goldpayRemit",
					dataType : 'json',
					contentType : "application/json; charset=utf-8",
					data : JSON.stringify(data),
					success : function(data) {
						if (data.retCode == "00000") {
							alert("操作成功");
							searchWithdraw($("#paginator .active a").html());
						} else if (data.retCode == "00002") {
							location.href = loginUrl;
						} else {
							console.log("goldpayRemit" + data.message);
							alert("操作失败");
							searchWithdraw($("#paginator .active a").html());
						}
					},
					error : function(xhr, err) {
						console.log("error");
						console.log(err);
						console.log(xhr);
						alert("未知错误");
					},
					async : false
				});
			}
		}
		//退回
		function refund(transferId) {
			if (confirm("确认退回吗？")) {
				data = {
					transferId : transferId
				};
				$.ajax({
					type : "post",
					url : "/crm/refund",
					dataType : 'json',
					contentType : "application/json; charset=utf-8",
					data : JSON.stringify(data),
					success : function(data) {
						if (data.retCode == "00000") {
							alert("操作成功");
							searchWithdraw($("#paginator .active a").html());
						} else if (data.retCode == "00002") {
							location.href = loginUrl;
						} else {
							console.log("refund" + data.message);
							alert("操作失败");
							searchWithdraw($("#paginator .active a").html());
						}
					},
					error : function(xhr, err) {
						console.log("error");
						console.log(err);
						console.log(xhr);
						alert("未知错误");
					},
					async : false
				});
			}
		}

		function status(transferId, transferStatus) {
			switch (transferStatus) {
			case 0:
				return '<td>初始化</td><td></td>';
			case 1:
				return '<td>待审核</td><td><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			case 2:
				return '<td>已完成</td><td></td>';
			case 3:
				return '<td>已退回</td><td></td>';
			case 4:
				return '<td>待支付</td><td><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			case 5:
				return '<td>审核失败</td><td><button type="button" class="btn btn-info" onclick="withdrawReview(\''
						+ transferId
						+ '\')">重新审核</button><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			case 8:
				return '<td>支付失败</td><td><button type="button" class="btn btn-primary" onclick="goldpayRemit(\''
						+ transferId
						+ '\')">重新支付</button><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			}
		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</body>
</html>