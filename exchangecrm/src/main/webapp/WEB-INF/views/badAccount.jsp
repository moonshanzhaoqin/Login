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

	<div class="container" style="height: 100%;">
		<div class="row" id="task" style="margin-bottom: 20px;">
			<!-- 			<button type="button" class="btn btn-primary pull-right" onclick="setGoldpayRemitTaskStatus(false)">开启</button> -->
		</div>
		<div class="row" >
			<table class="table table-bordered table-hover table-striped"
				id="badAccount">
				<thead>
					<tr>
						<th>用户ID</th>
						<th>手机号</th>
						<th>货币</th>
						<th>核账前</th>
						<th>核账中</th>
						<th>核账后</th>
						<th>开始时间(UTC)</th>
						<th>结束时间(UTC)</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody style="height: 300px; overflow: auto;"></tbody>
			</table>
		</div>
		<hr style="background-color: grey; height: 1px;" />
		<div id="detail" class="row" style="height: 450px; overflow: auto;"
			hidden>
			<table class="table table-bordered table-hover table-striped"
				id="walletSeq">
				<thead>
					<tr>
						<th>流水号</th>
						<th>货币</th>
						<th>数量</th>
						<th>交易类型</th>
						<th>交易ID</th>
						<th>时间(UTC)</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">详情</h4>
				</div>
				<div class="modal-body" style="overflow: auto;">
					<form class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-3 control-label">交易ID:</label>
							<div class="col-sm-9" id="transferId"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">交易类型:</label>
							<div class="col-sm-9" id="transferType"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">FROM:</label>
							<div class="col-sm-9" id=from></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">TO:</label>
							<div class="col-sm-9" id=to></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">币种:</label>
							<div class="col-sm-9" id=currency></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">数量:</label>
							<div class="col-sm-9" id=transferAmount></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">创建时间:</label>
							<div class="col-sm-9" id=createTime></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">最新更新时间:</label>
							<div class="col-sm-9" id=finishTime></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">交易状态:</label>
							<div class="col-sm-9" id=transferStatus></div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap-paginator.min.js" />"></script>
	<script type="text/javascript">
		$(function() {
			getGoldpayRemitTaskStatus()
			getBadAccountByPage(1);

			// 			$('#myModal').on('show.bs.modal', function(e) {
			// 				// do something...
			// 				var tr = $(e.relatedTarget) // Button that triggered the modal
			// 				var badAccountId = tr.data('whatever') // Extract info from data-* attributes
			// 				console.log(badAccountId);
			// 				getDetailSeq(badAccountId)
			// 			})

		})

		function getGoldpayRemitTaskStatus() {
			$
					.ajax({
						type : "post",
						url : "/crm/getGoldpayRemitTaskStatus",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : {},
						success : function(data) {
							if (data.retCode == "00002") {
								location.href = loginUrl;
							} else {
								console.log("success");
								console.log(data);
								if (data == true) {
									$("#task")
											.html(
													'<button type="button" class="btn btn-primary pull-right" onclick="setGoldpayRemitTaskStatus(false)">开启</button>');
								} else {
									$("#task")
											.html(
													'<button type="button" class="btn btn-danger pull-right" onclick="setGoldpayRemitTaskStatus(true)">关闭</button>');
								}
							}

						}

					})
		}
		function setGoldpayRemitTaskStatus(status) {
			var data = {
				status : status
			};
			$.ajax({
				type : "post",
				url : "/crm/setGoldpayRemitTaskStatus",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						getGoldpayRemitTaskStatus();
					}

				}

			})
		}
		function getBadAccountByPage(currentPage) {
			var data = {
				currentPage : currentPage
			};
			$
					.ajax({
						type : "post",
						url : "/crm/getBadAccountByPage",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(data),
						success : function(data) {
							if (data.retCode == "00002") {
								location.href = loginUrl;
							} else {
								console.log("success");
								var html = "";
								for ( var i in data.rows) {
									html += '<tr>' + '<td>'
											+ data.rows[i][1].userId
											+ '</td>'
											+ '<td>'
											+ data.rows[i][1].areaCode
											+ data.rows[i][1].userPhone
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].currency
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].balanceBefore
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].sumAmount
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].balanceNow
											+ '</td>'
											+ '<td>'
											+ timeDate(data.rows[i][0].startTime)
											+ '</td>'
											+ '<td>'
											+ timeDate(data.rows[i][0].endTime)
											+ '</td>'
											+ '<td>'
											+ '<a href="javascript:void(0)" onclick="getDetailSeq('
											+ data.rows[i][0].badAccountId
											+ ')">详情</a>' + '</td>' + '</tr>'
								}
								$('#badAccount tbody').html(html);
								if (data.currentPage == 1) {
									paginator(data.currentPage, data.pageTotal);
								}
							}
						},
						error : function(xhr, err) {
							console.log("error");
							console.log(err);
						},
						async : false
					});
		}

		function getDetailSeq(badAccountId) {
			var data = {
				badAccountId : badAccountId
			};
			$
					.ajax({
						type : "post",
						url : "/crm/getDetailSeq",
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
											+ data[i].amount
											+ '</td>'
											+ '<td>'
											+ transferType(data[i].transferType)
											+ '</td>'
											+ '<td>'
											+ ((data[i].transferType == "1") ? data[i].transactionId
													: ('<a href="" data-toggle="modal" data-target="#myModal" onclick= "getTransfer(\''
															+ data[i].transactionId
															+ '\')">	'
															+ data[i].transactionId + '</a>'))
											+ '</td>' + '<td>'
											+ timeDate(data[i].createTime)
											+ '</td>' + '</tr>'
								}
								$('#walletSeq tbody').html(html);
								$('#detail').show();
							}
						},
						error : function(xhr, err) {
							console.log("error");
							console.log(err);
						},
						async : false
					});
		}

		function getTransfer(transferId) {
			var data = {
				transferId : transferId
			};
			$
					.ajax({
						type : "post",
						url : "/crm/getTransfer",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(data),
						success : function(data) {
							if (data.retCode == "00002") {
								location.href = loginUrl;
							} else {
								$('#transferId').html(
										'<p class="form-control-static">'
												+ data[0].transferId + '</p>');
								$('#from')
										.html(
												'<p class="form-control-static">'
														+ data[0].userFrom
														+ '</p><p class="form-control-static">'
														+ data[1].areaCode
														+ data[1].userPhone
														+ '</p>');
								$('#to')
										.html(
												'<p class="form-control-static">'
														+ data[0].userTo
														+ '</p><p class="form-control-static">'
														+ data[2].areaCode
														+ data[2].userPhone
														+ '</p>');

								$('#currency').html(
										'<p class="form-control-static">'
												+ data[0].currency + '</p>');
								$('#transferAmount').html(
										'<p class="form-control-static">'
												+ data[0].transferAmount
												+ '</p>');
								$('#createTime').html(
										'<p class="form-control-static">'
												+ timeDate(data[0].createTime)
												+ '</p>');
								$('#finishTime').html(
										'<p class="form-control-static">'
												+ timeDate(data[0].finishTime)
												+ '</p>');
								$('#transferStatus')
										.html(
												'<p class="form-control-static">'
														+ transferStatus(data[0].transferStatus)
														+ '</p>');
								$('#transferType')
										.html(
												'<p class="form-control-static">'
														+ transferType(data[0].transferType)
														+ '</p>');
							}
						},
						error : function(xhr, err) {
							console.log("error");
							console.log(err);
						},
						async : false
					});
		}

		function transferType(transferType) {
			switch (transferType) {
			case 0:
				return "交易";
			case 1:
				return "兑换";
			case 2:
				return "邀请";
			case 3:
				return "系统退款";
			case 4:
				return "金沛提现";
			case 5:
				return "金沛充值";
			case 6:
				return "金沛充值退款";
			}
		}
		function transferStatus(transferStatus) {
			switch (transferStatus) {
			case 0:
				return "交易初始化";
			case 1:
				return " 交易进行中";
			case 2:
				return "交易已完成";
			case 3:
				return "交易退回";
			case 4:
				return "提现一审成功，待支付";
			case 5:
				return "提现一审失败，待二审";
			case 6:
				return "提现二审成功 ，待支付";
			case 7:
				return "提现二审失败，待退回";
			case 8:
				return "支付失败，待退回，待支付";
			}
		}

		//分页
		function paginator(currentPage, pageTotal) {
			console.log("currentPage=" + currentPage + ",pageTotal="
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
				itemContainerClass : function(type, page, current) {
					return (page === current) ? "active" : "pointer-cursor";
				},
				onPageClicked : function(event, originalEvent, type, page) {
					getBadAccountByPage(page)
				}
			}
			//分页控件
			$('#paginator').bootstrapPaginator(options);
		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>