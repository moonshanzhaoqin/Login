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
					</tr>
				</thead>
				<tbody>
					<tr data-toggle="modal" data-target="#myModal">
						<td>2</td>
						<td>2017011606450T000073</td>
						<td style="color: blue">待审批</td>
						<td style="color: blue">未执行</td>
					</tr>
					<tr>
						<td>2</td>
						<td>2017011606450T000073</td>
						<td style="color: red">未通过</td>
						<td style="color: blue">未执行</td>
					</tr>
					<tr>
						<td>2</td>
						<td>2017011606450T000073</td>
						<td style="color: green">通过</td>
						<td style="color: blue">未执行</td>
					</tr>
					<tr>
						<td>2</td>
						<td>2017011606450T000073</td>
						<td style="color: green">通过</td>
						<td style="color: red">失败</td>
					</tr>
					<tr>
						<td>2</td>
						<td>2017011606450T000073</td>
						<td style="color: green">通过</td>
						<td style="color: green">成功</td>
					</tr>

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
						<h4 class="modal-title" id="myModalLabel">用户信息</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form" id="updateCurrency">
							<div class="form-group">
								<label for="currency" class="col-sm-2 control-label">货币代码</label>
								<div class="col-sm-5">
									<input type="text" name="currency" class="form-control"
										readonly>
								</div>
							</div>
							<div class="form-group">
								<label for="nameCn" class="col-sm-2 control-label">简体名称</label>
								<div class="col-sm-5">
									<input type="text" name="nameCn" class="form-control">
								</div>
							</div>
							<div class="form-group">
								<label for="nameEn" class="col-sm-2 control-label">英文名称</label>
								<div class="col-sm-5">
									<input type="text" name="nameEn" class="form-control">
								</div>
							</div>
							<div class="form-group">
								<label for="nameHk" class="col-sm-2 control-label">繁体名字</label>
								<div class="col-sm-5">
									<input type="text" name="nameHk" class="form-control">
								</div>
							</div>
							<div class="form-group">
								<label for="currencyUnit" class="col-sm-2 control-label">货币单位</label>
								<div class="col-sm-5">
									<input type="text" name="currencyUnit" class="form-control">
								</div>
							</div>
							<div class="form-group">
								<label for="currency" class="col-sm-2 control-label">状态</label>
								<div class="col-sm-5">
									<input type="radio" name="currencyStatus" value="1">上架
									<input type="radio" name="currencyStatus" value="0">下架
								</div>
							</div>
							<div class="form-group">
								<label for="currencyOrder" class="col-sm-2 control-label">顺序</label>
								<div class="col-sm-5">
									<input type="text" name="currencyOrder" class="form-control">
								</div>
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
			// 			init();

			//
			$('#myModal').on('show.bs.modal', function(e) {
				// do something...
				var tr = $(e.relatedTarget) // Button that triggered the modal
				var currency = tr.data('whatever') // Extract info from data-* attributes
				console.log(currency);
				form = document.getElementById("updateCurrency");
				form.currency.value = currency.currency;
				form.currencyOrder.value = currency.currencyOrder;
				form.currencyStatus.value = currency.currencyStatus;
				form.currencyUnit.value = currency.currencyUnit;
				form.nameCn.value = currency.nameCn;
				form.nameEn.value = currency.nameEn;
				form.nameHk.value = currency.nameHk;
				if (currency.currency == 'GDQ') {
					form.currencyOrder.readOnly = true;
				} else {
					form.currencyOrder.readOnly = false;
				}
			})
		});

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
						html += '<tr>' + '<td>' + data[i].userId + '</td>'
								+ '<td>' + data[i].transferId + '</td>'
								+ '<td>' + data[i].reviewStatus + '</td>'
								+ '<td>' + data[i].goldpayRemit + '</td>'
								+ '</tr>'
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
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</body>
</html>