<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Exanytime</title>
<link rel='icon' href='<c:url value="/resources/img/ex_28x28.ico" />'
	type='image/x-ico' />
<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap-paginator.min.css" />' />
<link rel="stylesheet"
	href="<c:url value="/resources/css/common.css" />" />
</head>
<body>
	<%@ include file="common/header.jsp"%>
	<div class="container">
		<div class="row">

			<form class="form-inline" id="searchWithdraw">
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
				<div class="form-group">
					<!-- 					<label class="checkbox-inline"> <input type="checkbox" -->
					<!-- 						name="transferStatus" value="">all -->
					<!-- 					</label>  -->
					<label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="1" checked>未审核
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="2">已完成
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="3">已退回
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="4" checked>一审成功，待支付
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="5" checked>一审失败，待二审
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="6" checked>二审成功 ，待支付
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="7" checked>二审失败，待退回
					</label> <label class="checkbox-inline"> <input type="checkbox"
						name="transferStatus" value="8" checked>支付失败，待退回，待支付
					</label>

					<!-- 					<label class="sr-only" for="transferStatus">transferStatus</label> -->
					<!-- 					<select class="form-control selectpicker" name="transferStatus" multiple> -->
					<!-- 						<option value="">审批状态</option> -->
					<!-- 						<option value="1">未审核</option> -->
					<!-- 						<option value="2">已完成</option> -->
					<!-- 						<option value="3">已退回</option> -->
					<!-- 						<option value="4">一审成功，待支付</option> -->
					<!-- 						<option value="5">一审失败，待二审</option> -->
					<!-- 						<option value="6">二审成功 ，待支付</option> -->
					<!-- 						<option value="7">二审失败，待退回</option> -->
					<!-- 						<option value="8">支付失败，待退回，待支付</option> -->
					<!-- 					</select> -->
				</div>
				<button type="button" class="btn btn-primary"
					onclick="searchWithdraw(1)">搜索</button>
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
						<th>最新更新时间</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
		</div>
	</div>
	<!-- 模态框（Modal） -->
	<!-- 	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" -->
	<!-- 		aria-labelledby="myModalLabel" aria-hidden="true"> -->
	<!-- 		<div class="modal-dialog"> -->
	<!-- 			<div class="modal-content"> -->
	<!-- 				<div class="modal-header"> -->
	<!-- 					<button type="button" class="close" data-dismiss="modal" -->
	<!-- 						aria-hidden="true">&times;</button> -->
	<!-- 					<h4 class="modal-title" id="myModalLabel">用户信息：</h4> -->
	<!-- 				</div> -->
	<!-- 				<div class="modal-body"> -->
	<!-- 					<form class="form-horizontal" role="form"> -->
	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">用户ID：</label> -->
	<!-- 							<div class="col-sm-9" id="userId"></div> -->
	<!-- 						</div> -->
	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">用户名：</label> -->
	<!-- 							<div class="col-sm-9" id="userName"></div> -->
	<!-- 						</div> -->
	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">手机号：</label> -->
	<!-- 							<div class="col-sm-9" id="userPhone"></div> -->
	<!-- 						</div> -->
	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">Goldpay账号：</label> -->
	<!-- 							<div class="col-sm-9" id="goldpayAcount"></div> -->
	<!-- 						</div> -->
	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">Goldpay用户名：</label> -->
	<!-- 							<div class="col-sm-9" id="goldpayName"></div> -->
	<!-- 						</div> -->
	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">交易ID：</label> -->
	<!-- 							<div class="col-sm-9" id="transferId"></div> -->
	<!-- 						</div> -->

	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">交易数量：</label> -->
	<!-- 							<div class="col-sm-9" id="transferAmount"></div> -->
	<!-- 						</div> -->

	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">交易创建时间：</label> -->
	<!-- 							<div class="col-sm-9" id="createTime"></div> -->
	<!-- 						</div> -->
	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">审核状态：</label> -->
	<!-- 							<div class="col-sm-9" id="reviewStatus"></div> -->
	<!-- 						</div> -->
	<!-- 						<div class="form-group"> -->
	<!-- 							<label class="col-sm-3 control-label">Goldpay划账：</label> -->
	<!-- 							<div class="col-sm-9" id="goldpayRemit"></div> -->
	<!-- 						</div> -->
	<!-- 					</form> -->
	<!-- 				</div> -->
	<!-- 				<div class="modal-footer"> -->
	<!-- 					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button> -->
	<!-- 				</div> -->
	<!-- 			</div> -->
	<!-- 		</div> -->
	<!-- 	</div> -->


	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap-paginator.min.js" />"></script>
	<script>
		$(function() {
			var userPhone, transferId, transferStatus = [];
			//页面初始化，加载数据
			searchWithdraw(1);
			// 			$('#myModal').on('show.bs.modal', function(e) {
			// 				// do something...
			// 				var tr = $(e.relatedTarget) // Button that triggered the modal
			// 				var withdrawId = tr.data('whatever') // Extract info from data-* attributes
			// 				console.log(withdrawId);
			// 				initModel(withdrawId);
			// 			})

			// 			//模态框关闭
			// 			$('#myModal').on('hidden.bs.modal', function(e) {
			// 				// 				console.log($("#paginator .active a").html());
			// 				// 				console.log($("#paginator").data("currentPage"));
			// 				searchWithdraw($("#paginator .active a").html());
			// 			})
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

		// 		// 模态框初始化
		// 		function initModel(withdrawId) {
		// 			data = {
		// 				withdrawId : withdrawId
		// 			};
		// 			$
		// 					.ajax({
		// 						type : "post",
		// 						url : "/crm/getWithdrawDetail",
		// 						dataType : 'json',
		// 						contentType : "application/json; charset=utf-8",
		// 						data : JSON.stringify(data),
		// 						success : function(data) {
		// 							$('#userId').html(
		// 									'<p class="form-control-static">'
		// 											+ data.userId + '</p>');
		// 							$('#userName').html(
		// 									'<p class="form-control-static">'
		// 											+ data.userName + '</p>');
		// 							$('#userPhone').html(
		// 									'<p class="form-control-static">'
		// 											+ data.areaCode + data.userPhone
		// 											+ '</p>');
		// 							$('#goldpayAcount').html(
		// 									'<p class="form-control-static">'
		// 											+ data.goldpayAcount + '</p>');
		// 							$('#goldpayName').html(
		// 									'<p class="form-control-static">'
		// 											+ data.goldpayName + '</p>');
		// 							$('#transferId').html(
		// 									'<p class="form-control-static">'
		// 											+ data.transferId + '</p>');
		// 							$('#transferAmount').html(
		// 									'<p class="form-control-static">'
		// 											+ data.transferAmount + '</p>');
		// 							$('#createTime').html(
		// 									'<p class="form-control-static">'
		// 											+ new Date(Number(data.createTime))
		// 											+ '</p>');
		// 							if (data.reviewStatus == 0) {
		// 								$('#reviewStatus')
		// 										.html(
		// 												'<p class="form-control-static" style="color: blue">未审批</p>'
		// 														+ '<button type="button" class="btn btn-primary"onclick="withdrawReview('
		// 														+ withdrawId
		// 														+ ')">审批</button>');
		// 							} else if (data.reviewStatus == 1) {
		// 								$('#reviewStatus')
		// 										.html(
		// 												'<p class="form-control-static" style="color: red">未通过</p>'
		// 														+ '<button type="button" class="btn btn-primary"onclick="withdrawReview('
		// 														+ withdrawId
		// 														+ ')">审批</button>');
		// 							} else {
		// 								$('#reviewStatus')
		// 										.html(
		// 												'<p class="form-control-static" style="color: green">通过</p>');
		// 							}

		// 							if (data.goldpayRemit == 0) {
		// 								if (data.reviewStatus == 2) {
		// 									$('#goldpayRemit')
		// 											.html(
		// 													'<p class="form-control-static" style="color: green">未执行</p>'
		// 															+ '<button type="button" class="btn btn-primary"onclick="goldpayRemit('
		// 															+ withdrawId
		// 															+ ')">执行</button>');
		// 								} else {
		// 									$('#goldpayRemit')
		// 											.html(
		// 													'<p class="form-control-static" style="color: green">未执行</p>');
		// 								}
		// 							} else if (data.goldpayRemit == 1) {
		// 								$('#goldpayRemit')
		// 										.html(
		// 												'<p class="form-control-static" style="color: red">失败</p>'
		// 														+ '<button type="button" class="btn btn-primary"onclick="goldpayRemit('
		// 														+ withdrawId
		// 														+ ')">执行</button>');
		// 							} else {
		// 								$('#goldpayRemit')
		// 										.html(
		// 												'<p class="form-control-static" style="color: green">成功</p>');
		// 							}
		// 						}
		// 					})
		// 		}

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

			$.ajax({
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
							html += '<tr>'
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
									+ status(data.rows[i][0].transferId,
											data.rows[i][0].transferStatus)
									+ '<td>'
									+ timeDate(data.rows[i][0].finishTime)
									+ '</td>' + '</tr>'
						}
						$('#withdraw tbody').html(html);
						if (data.currentPage == 1) {
							paginator(data.currentPage, data.pageTotal);
						}
					}
					;
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
				},
				async : false
			});
		}
		//时间戳变格式化
		function timeDate(time) {
			var date = new Date();
			date.setTime(time);
			return date.toLocaleString();
		}

		//审批
		function withdrawReview(transferId) {
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
						alert("success");
						searchWithdraw($("#paginator .active a").html());
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("withdrawReview" + data.message);
						alert(data.message);
						searchWithdraw($("#paginator .active a").html());
					}
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
					alert("something is wrong!");
				},
				async : false
			});

		}
		//划账
		function goldpayRemit(transferId) {
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
						alert("success");
						searchWithdraw($("#paginator .active a").html());
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("goldpayRemit" + data.message);
						alert(data.message);
						searchWithdraw($("#paginator .active a").html());
					}
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
					alert("something is wrong!");
				},
				async : false
			});
		}
		//退回
		function refund(transferId) {
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
						alert("success");
						searchWithdraw($("#paginator .active a").html());
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("refund" + data.message);
						alert(data.message);
						searchWithdraw($("#paginator .active a").html());
					}
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
					alert("something is wrong!");
				},
				async : false
			});

		}

		function status(transferId, transferStatus) {
			switch (transferStatus) {
			case 0:
				return '<td>初始化</td><td></td>';
			case 1:
				return '<td>未审核</td><td><button type="button" class="btn btn-info" onclick="withdrawReview(\''
						+ transferId
						+ '\')">审批</button><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			case 2:
				return '<td>已完成</td><td></td>';
			case 3:
				return '<td>已退回</td><td></td>';
			case 4:
				return '<td>一审成功，待支付</td><td><button type="button" class="btn btn-primary" onclick="goldpayRemit(\''
						+ transferId
						+ '\')">划账</button><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			case 5:
				return '<td>一审失败，待二审</td><td><button type="button" class="btn btn-info" onclick="withdrawReview(\''
						+ transferId
						+ '\')">审批</button><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			case 6:
				return '<td>二审成功 ，待支付</td><td><button type="button" class="btn btn-primary" onclick="goldpayRemit(\''
						+ transferId
						+ '\')">划账</button><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			case 7:
				return '<td>二审失败，待退回</td><td><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			case 8:
				return '<td>支付失败，待退回，待支付</td><td><button type="button" class="btn btn-primary" onclick="goldpayRemit(\''
						+ transferId
						+ '\')">划账</button><button type="button" class="btn btn-warning" onclick="refund(\''
						+ transferId + '\')">退回</button></td>';
			}
		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</body>
</html>