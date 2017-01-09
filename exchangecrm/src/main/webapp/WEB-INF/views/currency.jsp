<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Anytime Exchange</title>

<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
<link rel="stylesheet"
	href="<c:url value="/resources/css/common.css" />" />
</head>
<body>
	<%@ include file="common/header.jsp"%>

	<div class="container">
		<div class="row ">

			<ul class="formbar pull-right">
				<li><select class="form-control"></select></li>
				<li>
					<button type="button" class="btn btn-default"
						onclick="addCurrency()">
						<span class="glyphicon glyphicon-plus"></span> 添加新币种
					</button>
				</li>
			</ul>
		</div>
		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="currency">
				<thead>
					<tr>
						<th>货币代码</th>
						<th>简体</th>
						<th>英文</th>
						<th>繁体</th>
						<th>单位</th>
						<th>状态</th>
						<th>顺序</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>

	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">编辑币种</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="updateCurrency">
						<div class="form-group">
							<label for="currency" class="col-sm-2 control-label">货币代码</label>
							<div class="col-sm-5">
								<input type="text" name="currency" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="nameCn" class="col-sm-2 control-label">中文名字</label>
							<div class="col-sm-5">
								<input type="text" name="nameCn" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="nameEn" class="col-sm-2 control-label">英文名字</label>
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
							<label for="currencyUnit" class="col-sm-2 control-label">单位</label>
							<div class="col-sm-5">
								<input type="text" name="currencyUnit" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="currency" class="col-sm-2 control-label">状态</label>
							<div class="col-sm-5">
								<input type="radio" name="currencyStatus" value="1">可用 <input
									type="radio" name="currencyStatus" value="0">不可用
							</div>
						</div>
						<div class="form-group">
							<label for="currencyOrder" class="col-sm-2 control-label">排序</label>
							<div class="col-sm-5">
								<input type="text" name="currencyOrder" class="form-control">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="updateCurrency()">提交更改</button>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript">
		$(function() {
			initCurrency();

			$('#myModal').on('show.bs.modal', function(e) {
				// do something...
				var tr = $(e.relatedTarget) // Button that triggered the modal
				var currency = tr.data('whatever') // Extract info from data-* attributes
				console.log(currency);
				form = document.getElementById("updateCurrency");
				form.currency.value = currency.currency
				form.currencyOrder.value = currency.currencyOrder
				form.currencyStatus.value = currency.currencyStatus
				form.currencyUnit.value = currency.currencyUnit
				form.nameCn.value = currency.nameCn
				form.nameEn.value = currency.nameEn
				form.nameHk.value = currency.nameHk
			})

		});

		function initCurrency() {
			$
					.ajax({
						type : "post",
						url : "/crm/getCurrencyList",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : {},
						success : function(data) {
							console.log("success");
							var html = "";
							for (var i = 0; i < data.length; i++) {
								html += '<tr data-toggle="modal" data-target="#myModal" data-whatever='
										+ JSON.stringify(data[i])
										+ '>'
										+ '<td style="font-weight:bold;">'
										+ data[i].currency
										+ '</td>'
										+ '<td>'
										+ data[i].nameCn
										+ '</td>'
										+ '<td>'
										+ data[i].nameEn
										+ '</td>'
										+ '<td>'
										+ data[i].nameHk
										+ '</td>'
										+ '<td>'
										+ data[i].currencyUnit
										+ '</td>'
										+ (data[i].currencyStatus == 0 ? '<td style="color:red">UNAVAILABLE</td>'
												: '<td style="color:green">AVAILABLE</td>')
										+ '<td>'
										+ data[i].currencyOrder
										+ '</td>' + '</tr>'
							}
							$('#currency tbody').html(html);

						},
						error : function(xhr, err) {
							console.log("error");
							console.log(err);
						},
						async : false
					});
			$.ajax({
				type : "post",
				url : "/crm/getAddingCurrencyList",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : {},
				success : function(data) {
					console.log("success");
					var html = "";
					for (var i = 0; i < data.length; i++) {
						html += '<option value="'+data[i]+'">' + data[i]
								+ '</option>'
					}
					$('select').html(html);

				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
				},
				async : false
			});
		}
		function updateCurrency() {
			form = document.getElementById("updateCurrency");
			data = {
				currency : form.currency.value,
				currencyOrder : form.currencyOrder.value,
				currencyStatus : form.currencyStatus.value,
				currencyUnit : form.currencyUnit.value,
				nameCn : form.nameCn.value,
				nameEn : form.nameEn.value,
				nameHk : form.nameHk.value
			}
			$.ajax({
				type : "post",
				url : "/crm/updateCurrency",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						console.log("success");
						$('#myModal').modal('hide')
						alert("修改成功！");
						initCurrency();
					} else {
						console.log("updateCurrency" + data.message);
						alert(data.message);
					}
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
				},
				async : false
			});
		}
		function addCurrency() {
			data = {
				currency : $('select').val()
			}
			$.ajax({
				type : "post",
				url : "/crm/addCurrency",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						console.log("success");
						alert("添加成功！");
						initCurrency();

					} else {
						alert("币种已存在")
					}
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
				},
				async : false
			});

		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>