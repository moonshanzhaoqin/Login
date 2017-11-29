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
					<label class="sr-only" for="userPhone">userPhone</label> <input
						type="text" class="form-control" name="userPhone"
						placeholder="手机号">
				</div>
				<div class="form-group">
					<label class="sr-only" for="lowerAmount">userPhone</label> <input
						type="text" class="form-control" name="lowerAmount"
						placeholder="交易数量>=">
				</div>
				<div class="form-group">
					<label class="sr-only" for="upperAmount">userPhone</label> <input
						type="text" class="form-control" name="upperAmount"
						placeholder="交易数量<=">
				</div>
				<div class="form-group">
					<label class="sr-only" for="startTime">startTime</label> <input
						id="start" class="form-control" name="startTime"
						placeholder="开始时间" />
				</div>
				<div class="form-group">
					<label class="sr-only" for="endTime">endTime</label> <input
						id="end" class="form-control" name="endTime" placeholder="结束时间" />
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
			<div id="total" class="pull-right"></div>
		</div>

	</div>
	<script>
		$(function() {
			var start = {
			        elem : '#start',
			        format : 'YYYY-MM-DD',
			        min : '1970-01-01', // 设定最小日期
			        max :  laydate.now(), // 最大日期为当前日期
//			      istime : true,
			        istoday : false,
			        choose : function(datas) {
			            end.min = datas; // 开始日选好后，重置结束日的最小日期
//			          end.start = datas // 将结束日的初始值设定为开始日
			        }
			    };
			    var end = {
			        elem : '#end',
			        format : 'YYYY-MM-DD',
			        min : '1970-01-01', // 设定最小日期
			        max :  laydate.now(), // 最大日期为当前日期
//			      istime : true,
			        istoday : false,
			        choose : function(datas) {
			            start.max = datas; // 结束日选好后，重置开始日的最大日期
			        }
			    };
			    laydate(start);
			    laydate(end);
			
			
			var userPhone = '', lowerAmount = '', upperAmount = '', startTime = '', endTime = '', transferType;
			//页面初始化，加载数据
			searchRecharge(1);
		});
		function searchRecharge(page) {
			console.log("searchRecharge:page=" + page);
			form = document.getElementById("searchRecharge");

			userPhone = form.userPhone.value;
			lowerAmount = form.lowerAmount.value;
			upperAmount = form.upperAmount.value;
			startTime = form.startTime.value;
			endTime = form.endTime.value;
			transferType = form.transferType.value;

			console.log("userPhone=" + userPhone + "lowerAmount=" + lowerAmount
					+ "upperAmount=" + upperAmount + "startTime=" + startTime
					+ ",endTime=" + endTime + ",transferType=" + transferType);

			getRechargeList(page, userPhone, lowerAmount, upperAmount,
					startTime, endTime, transferType);
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
					getRechargeList(page, userPhone, lowerAmount, upperAmount,
							startTime, endTime, transferType);
				}
			}
			//分页控件
			$('#paginator').bootstrapPaginator(options);
		}

		function getRechargeList(currentPage, userPhone, lowerAmount,
				upperAmount, startTime, endTime, transferType) {
			form = document.getElementById("searchRecharge");

			form.userPhone.value = userPhone;
			form.lowerAmount.value = lowerAmount;
			form.upperAmount.value = upperAmount;
			form.startTime.value = startTime;
			form.endTime.value = endTime;
			form.transferType.value = transferType;

			var data = {
				currentPage : currentPage,
				userPhone : userPhone,
				lowerAmount : lowerAmount,
				upperAmount : upperAmount,
				startTime : startTime,
				endTime : endTime,
				transferType : transferType
			};

			$
					.ajax({
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
											+ (!data.rows[i][0].paypalCurrency ? ""
													: data.rows[i][0].paypalCurrency)
											+ '</td>'
											+ '<td>'
											+ (!data.rows[i][0].paypalExchange ? ""
													: data.rows[i][0].paypalExchange)
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
								$('#total').html("共 "+data.total+" 条记录");
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