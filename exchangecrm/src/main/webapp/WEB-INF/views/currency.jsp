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
	href='<c:url value="/resources/bootstrap/css/bootstrap-table.css" />' />

</head>
<body>
	<%@ include file="header.jsp"%>

	<div class="container">
		<div class="row">
			<div class="col-md-2"></div>
			<table class="table table-bordered table-hover col-md-8"
				id="currency">
				<thead>
					<tr>
						<th>货币符号</th>
						<th>简体</th>
						<th>英文</th>
						<th>繁体</th>
						<th>单位</th>
						<th>状态</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
		<div class="col-md-2"></div>
		<!-- 模态框（Modal） -->
<!-- 		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" -->
<!-- 			aria-labelledby="myModalLabel" aria-hidden="true"> -->
<!-- 			<div class="modal-dialog"> -->
<!-- 				<div class="modal-content"> -->
<!-- 					<div class="modal-header"> -->
<!-- 						<button type="button" class="close" data-dismiss="modal" -->
<!-- 							aria-hidden="true">&times;</button> -->
<!-- 						<h4 class="modal-title" id="myModalLabel">编辑币种</h4> -->
<!-- 					</div> -->
<!-- 					<div class="modal-body">在这里添加一些文本</div> -->
<!-- 					<div class="modal-footer"> -->
<!-- 						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button> -->
<!-- 						<button type="button" class="btn btn-primary" onclick="updateCurrency()">提交更改</button> -->
<!-- 					</div> -->
<!-- 				</div> -->
<!-- 				/.modal-content -->
<!-- 			</div> -->
<!-- 			<!-- /.modal --> -->

		</div>


		<script type="text/javascript"
			src="<c:url value="/resources/js/jquery.min.js" />"></script>
		<script type="text/javascript"
			src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
		<script type="text/javascript">
			$(function() {
				$.ajax({
					type : "post",
					url : "/crm/getCurrencyList",
					dataType : 'json',
					contentType : "application/json; charset=utf-8",
					data : {},
					success : function(data) {
						console.log("success");
						console.log(data);
						initCurrency(data);

					},
					error : function(xhr, err) {
						console.log("error");
						console.log(err);
					},
					async : false
				});

				function initCurrency(data) {
					var html = "";
					for (var i = 0; i < data.length; i++) {
						html += '<tr>' + '<td>' + data[i].currency + '</td>'
								+ '<td>' + data[i].nameCn + '</td>' + '<td>'
								+ data[i].nameEn + '</td>' + '<td>'
								+ data[i].nameHk + '</td>' + '<td>'
								+ data[i].currencyUnit + '</td>' + '<td>'
								+ data[i].currencyStatus == 0 ? 'unavailable'
								: 'available' + '</td>' + '</tr>'
					}
					$('#currency tbody').html(html);

				}

			});
		</script>
</body>
</html>