<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>

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
							<label class="col-sm-3 control-label">发款人ID:</label>
							<div class="col-sm-9" id=fromId></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">发款人手机号:</label>
							<div class="col-sm-9" id=fromPhone></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">收款人ID:</label>
							<div class="col-sm-9" id=toId></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">收款人手机号:</label>
							<div class="col-sm-9" id=toPhone></div>
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
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
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

	<script type="text/javascript">
		function getDetailSeq(badAccountId) {
			$("tr").removeClass("success")
			$("#" + badAccountId).addClass("success")

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
											+ new Number(data[i].amount)
													.toFixed(4)
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
							alert("未知错误");
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
								$('#fromId').html(
										'<p class="form-control-static">'
												+ data[0].userFrom + '</p>');
								$('#fromPhone').html(
										'<p class="form-control-static">'
												+ data[1].areaCode
												+ data[1].userPhone + '</p>');
								$('#toId').html(
										'<p class="form-control-static">'
												+ data[0].userTo + '</p>');
								$('#toPhone').html(
										'<p class="form-control-static">'
												+ data[2].areaCode
												+ data[2].userPhone + '</p>');

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
							alert("未知错误");
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
				return "金沛提现退款";
			}
		}
		function transferStatus(transferStatus) {
			switch (transferStatus) {
			case 0:
				return "交易初始化";
			case 1:
				return " 交易进行中/待审核";
			case 2:
				return "交易已完成";
			case 3:
				return "交易退回";
			case 4:
				return "待支付";
			case 5:
				return "审核失败";
			case 8:
				return "支付失败";
			}
		}
	</script>
</body>
</html>