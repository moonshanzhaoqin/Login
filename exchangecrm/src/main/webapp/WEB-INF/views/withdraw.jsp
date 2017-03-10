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
	href="<c:url value="/resources/css/common.css" />" />
</head>
<body>
	<%@ include file="common/header.jsp"%>
	<div class="container">
		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="withdraw">
				<thead>
					<tr>
						<th>用户ID</th>
						<th>交易号</th>
						<th>审批状态</th>
						<th>Goldpay划账结果</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>

		<!-- 模态框（Modal） -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">用户信息：</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<label class="col-sm-3 control-label">用户ID：</label>
								<div class="col-sm-9" id="userId"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">用户名：</label>
								<div class="col-sm-9" id="userName"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Goldpay账号：</label>
								<div class="col-sm-9" id="goldpayAcount"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Goldpay用户名：</label>
								<div class="col-sm-9" id="goldpayName"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">交易ID：</label>
								<div class="col-sm-9" id="transferId"></div>
							</div>

							<div class="form-group">
								<label class="col-sm-3 control-label">交易数量：</label>
								<div class="col-sm-9" id="transferAmount"></div>
							</div>

							<div class="form-group">
								<label class="col-sm-3 control-label">交易创建时间：</label>
								<div class="col-sm-9" id="createTime"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">审核状态：</label>
								<div class="col-sm-9" id="reviewStatus"></div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Goldpay划账：</label>
								<div class="col-sm-9" id="goldpayRemit"></div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					</div>
				</div>
			</div>
		</div>

	</div>

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script>
		$(function() {
			//页面初始化，加载数据
			init();

			$('#myModal').on('show.bs.modal', function(e) {
				// do something...
				var tr = $(e.relatedTarget) // Button that triggered the modal
				var withdrawId = tr.data('whatever') // Extract info from data-* attributes
				console.log(withdrawId);
				initModel(withdrawId);
			})
			$('#myModal').on('hidden.bs.modal', function (e) {
				init();
			})
		});
		function initModel(withdrawId) {
			data = {withdrawId : withdrawId};
			$.ajax({
				type : "post",
				url : "/crm/getWithdrawDetail",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					$('#userId').html('<p class="form-control-static">' + data.userId + '</p>');
					$('#userName').html('<p class="form-control-static">' + data.userName + '</p>');
					$('#goldpayAcount').html('<p class="form-control-static">' + data.goldpayAcount + '</p>');
					$('#goldpayName').html('<p class="form-control-static">' + data.goldpayName + '</p>');
					$('#transferId').html('<p class="form-control-static">' + data.transferId + '</p>');
					$('#transferAmount').html('<p class="form-control-static">' + data.transferAmount + '</p>');
					$('#createTime').html('<p class="form-control-static">'+ new Date(Number(data.createTime)) + '</p>');
					if (data.reviewStatus == 0) {
						$('#reviewStatus').html('<p class="form-control-static" style="color: blue">未审批</p>'
									+ '<button type="button" class="btn btn-primary"onclick="withdrawReview('+ withdrawId+ ')">审批</button>');
					} else if (data.reviewStatus == 1) {
						$('#reviewStatus').html('<p class="form-control-static" style="color: red">未通过</p>'
								+ '<button type="button" class="btn btn-primary"onclick="withdrawReview('+ withdrawId+ ')">审批</button>');
					} else {
						$('#reviewStatus').html('<p class="form-control-static" style="color: green">通过</p>');
					}

					if (data.goldpayRemit == 0) {
						if (data.reviewStatus == 2) {
							$('#goldpayRemit').html('<p class="form-control-static" style="color: green">未执行</p>'
									+ '<button type="button" class="btn btn-primary"onclick="goldpayRemit('+ withdrawId+ ')">执行</button>');
						} else {
							$('#goldpayRemit').html('<p class="form-control-static" style="color: green">未执行</p>');
						}
					} else if (data.goldpayRemit == 1) {
						$('#goldpayRemit').html('<p class="form-control-static" style="color: red">失败</p>'
								+ '<button type="button" class="btn btn-primary"onclick="goldpayRemit('+ withdrawId+ ')">执行</button>');
					} else {
						$('#goldpayRemit').html('<p class="form-control-static" style="color: green">成功</p>');
					}
				}
			})
		}

		function init() {
			$.ajax({
				type : "post",
				url : "/crm/getWithdrawList",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : {},
				success : function(data) {
					console.log("success");
					var html = "";
					for (var i = 0; i < data.length; i++) {
						html += '<tr>'
								+ '<td>'
								+ data[i].userId
								+ '</td>'
								+ '<td>'
								+ data[i].transferId
								+ '</td>'
								+ (data[i].reviewStatus == 0 ? '<td style="color:blue">未审批</td>'
										: (data[i].reviewStatus == 1 ? '<td style="color:red">未通过</td>'
												: '<td style="color:green">通过</td>'))
								+ (data[i].goldpayRemit == 0 ? '<td style="color:blue">未执行</td>'
										: (data[i].goldpayRemit == 1 ? '<td style="color:red">失败</td>'
												: '<td style="color:green">成功</td>'))
								+ '<td><a href="javascript:void(0)" data-toggle="modal" data-target="#myModal" data-whatever='
								+ "'" + data[i].withdrawId + "'"
								+ '>操作</a></td>' + '</tr>'
					}
					$('#withdraw tbody').html(html);
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
				},
				async : false
			});
		}

		//审批
		function withdrawReview(withdrawId) {
			data = {
				withdrawId : withdrawId
			};
			$.ajax({
				type : "post",
				url : "/crm/withdrawReview",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						console.log("success");
						initModel(withdrawId)
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("withdrawReview" + data.message);
						alert(data.message);
						initModel(withdrawId)
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
		function goldpayRemit(withdrawId) {
			data = {
				withdrawId : withdrawId
			};
			$.ajax({
				type : "post",
				url : "/crm/goldpayRemit",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						console.log("success");
						initModel(withdrawId)
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("goldpayRemit" + data.message);
						alert(data.message);
						initModel(withdrawId)
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
			initModel(withdrawId)
		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</body>
</html>