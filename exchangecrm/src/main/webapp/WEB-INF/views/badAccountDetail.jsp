<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>

	<!-- Modal -->
	<div class="modal fade" id="transfer" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">交易详情</h4>
				</div>
				<div class="modal-body" style="overflow: auto;">
					<form class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-5 control-label">交易ID:</label>
							<div class="col-sm-7" id="transferId"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">交易类型:</label>
							<div class="col-sm-7" id="transferType"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">发款人ID:</label>
							<div class="col-sm-7" id="fromId"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">发款人手机号:</label>
							<div class="col-sm-7" id="fromPhone"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">收款人ID:</label>
							<div class="col-sm-7" id="toId"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">收款人手机号:</label>
							<div class="col-sm-7" id="toPhone"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">币种:</label>
							<div class="col-sm-7" id="currency"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">数量:</label>
							<div class="col-sm-7" id="transferAmount"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">创建时间（GMT+8）:</label>
							<div class="col-sm-7" id="createTime"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">最新更新时间（GMT+8）:</label>
							<div class="col-sm-7" id="finishTime"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">交易状态:</label>
							<div class="col-sm-7" id="transferStatus"></div>
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


	<!-- Modal -->
	<div class="modal fade" id="exchange" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">兑换详情</h4>
				</div>
				<div class="modal-body" style="overflow: auto;">
					<form class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-5 control-label">兑换ID:</label>
							<div class="col-sm-7" id="exchangeId"></div>
						</div>

						<div class="form-group">
							<label class="col-sm-5 control-label">用户ID:</label>
							<div class="col-sm-7" id="userId"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">手机号:</label>
							<div class="col-sm-7" id="userPhone"></div>
						</div>
						<!--                        <div class="form-group"> -->
						<!--                            <label class="col-sm-3 control-label">币种:</label> -->
						<!--                            <div class="col-sm-9" id=currency></div> -->
						<!--                        </div> -->
						<div class="form-group">
							<label class="col-sm-5 control-label">数量:</label>
							<div class="col-sm-7" id="exchangeAmount"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">汇率:</label>
							<div class="col-sm-7" id="exchangeRate"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">创建时间（GMT+8）:</label>
							<div class="col-sm-7" id="EcreateTime"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-5 control-label">最新更新时间（GMT+8）:</label>
							<div class="col-sm-7" id="EfinishTime"></div>
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


	<script type="text/javascript">
		function getTransfer(transferId) {
			var data = {
				transferId : transferId
			};
			$
					.ajax({
						type : "post",
						url : "getTransfer",
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
														+ transferStatusName(data[0].transferStatus)
														+ '</p>');
								$('#transferType')
										.html(
												'<p class="form-control-static">'
														+ transferTypeName(data[0].transferType)
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
		function getExchange(exchangeId) {
			var data = {
				exchangeId : exchangeId
			};
			$.ajax({
				type : "post",
				url : "getExchange",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						$('#exchangeId').html(
								'<p class="form-control-static">'
										+ data[0].exchangeId + '</p>');
						$('#userId').html(
								'<p class="form-control-static">'
										+ data[0].userId + '</p>');
						$('#userPhone').html(
								'<p class="form-control-static">'
										+ data[1].areaCode + data[1].userPhone
										+ '</p>');

						// 								$('#currency').html(
						// 										'<p class="form-control-static">'
						// 												+ data[0].currency + '</p>');
						$('#exchangeAmount').html(
								'<p class="form-control-static">'
										+ data[0].amountOut + "  "
										+ data[0].currencyOut + " ——> "
										+ data[0].amountIn + " "
										+ data[0].currencyIn + '</p>');
						$('#exchangeRate').html(
								'<p class="form-control-static">'
										+ data[0].exchangeRate + '</p>');
						$('#EcreateTime')
								.html(
										'<p class="form-control-static">'
												+ timeDate(data[0].createTime)
												+ '</p>');
						$('#EfinishTime')
								.html(
										'<p class="form-control-static">'
												+ timeDate(data[0].finishTime)
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

		function transferTypeName(transferType) {
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
				return "金本提现";
			case 5:
				return "金本充值";
			case 6:
				return "金本提现退款";
			}
		}
		function transferStatusName(transferStatus) {
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