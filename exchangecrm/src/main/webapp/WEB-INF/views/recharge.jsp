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
			<form class="form-inline  pull-right" id="searchRecharge">
				<div class="form-group">
					<label class="sr-only" for="startTime">startTime</label> <input
						type="date" class="form-control" name="startTime"
						placeholder="开始时间" />
				</div>
				<div class="form-group">
					<label class="sr-only" for="endTime">endTime</label> <input
						type="date" class="form-control" name="endTime" placeholder="结束时间" />
				</div>
				<div class="form-group">
					<label class="sr-only" for="transferType">transferType</label> <select
						class="form-control" name="transferType">
						<option value="5">GoldPay</option>
						<option value="7">PayPal</option>
					</select>
				</div>
				<button type="button" class="btn btn-primary"
					onclick="searchRecharge(1)">搜索</button>
			</form>
		</div>

		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="recharge">
				<thead>
					<tr>
						<th>用户ID</th>
						<th>国家码</th>
						<th>手机号</th>
						<th>交易号</th>
						<th>交易数量(GDQ)</th>
						<th>支付币种</th>
                        <th>支付金额</th>
						<th>交易方式</th>
						<th>交易时间（GMT+8）</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
		</div>

	</div>
	<script>
		$(function() {
			var startTime = '', endTime = '', transferType;
			//页面初始化，加载数据
			searchRecharge(1);
		});
		function searchRecharge(page) {
			console.log("searchRecharge:page=" + page);
			form = document.getElementById("searchRecharge");
			startTime = form.startTime.value;
			endTime = form.endTime.value;
			transferType = form.transferType.value;
			console.log("startTime=" + startTime + ",endTime=" + endTime
					+ ",transferType=" + transferType);
			getRechargeList(page, startTime, endTime, transferType);
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
					getRechargeList(page, startTime, endTime, transferType);
				}
			}
			//分页控件
			$('#paginator').bootstrapPaginator(options);
		}

		function getRechargeList(currentPage, startTime, endTime, transferType) {
			form = document.getElementById("searchRecharge");
			form.startTime.value = startTime;
			form.endTime.value = endTime;
			form.transferType.value = transferType;
			var data = {
				currentPage : currentPage,
				startTime : startTime,
				endTime : endTime,
				transferType : transferType
			};

			$.ajax({
						type : "post",
						url : "/crm/getRechargeList",
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
									html += '<tr>' + '<td>'
											+ data.rows[i][0].userTo
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
											+ '<td>'
                                            + data.rows[i][0].paypalCurrency
                                            + '</td>'
                                            + '<td>'
                                            + data.rows[i][0].paypalExchange
                                            + '</td>'
											+ '<td>'
											+ (data.rows[i][0].transferType == 5 ? "GoldPay"
													: "PayPal")
											+ '</td>'
											+ '<td>'
											+ timeDate(data.rows[i][0].finishTime)
											+ '</td>' + '</tr>'

								}
								$('#recharge tbody').html(html);
								if (data.currentPage == 1) {
									paginator(data.currentPage, data.pageTotal);
								}
							}
						},
						error : function(xhr, err) {
							alert("未知错误");
							console.log(err);
						}
					})

		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>