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
<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap-datetimepicker.css" />' />
<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap-paginator.min.css" />' />

<style type="text/css">
.formbar li {
	list-style: none;
	display: inline-block;
}
</style>

</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="container">
		<form class="form-horizontal" role="form">
			<div class="form-group">
				<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="inputEmail3"
						placeholder="Email">
				</div>
			</div>
			<div class="form-group">
				<label for="inputPassword3" class="col-lg-2 control-label">Password</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" id="inputPassword3"
						placeholder="Password">
				</div>
			</div>
		</form>
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
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal"
						onclick="updateConfig()">提交更改</button>
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
			initConfig();

			$('#myModal').on('show.bs.modal', function(e) {
				// do something...
				// 				var tr = $(e.relatedTarget) // Button that triggered the modal
				// 				var config = tr.data('whatever') // Extract info from data-* attributes
				// 				console.log(config);
				// 				form = document.getElementById("configValue");
				// 				form.configKey.value = config.configKey;
				// 				form.configValue.value = config.configValue;
			})

		});

		function initConfig() {
			console.log("initConfig");
			$
					.ajax({
						type : "post",
						url : "/crm/getConfigList",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : {},
						success : function(data) {
							console.log("getConfigList success");
							var html = "";
							for (var i = 0; i < data.length; i++) {
								html += '<tr data-toggle="modal" data-target="#myModal" data-whatever='
										+ JSON.stringify(data[i])
										+ '>'
										+ '<td>'
										+ data[i].configKey
										+ '</td>'
										+ '<td>'
										+ data[i].configValue
										+ '</td>' + '</tr>'
							}
							$('#config tbody').html(html);

						},
						error : function(xhr, err) {
							console.log("getConfigList error");
							console.log(err);
						},
						async : false
					});

		}

		function configValue() {
			form = document.getElementById("updateConfig");
			data = {
				configKey : form.configKey.value,
				configValue : form.configValue.value,
			}
			$.ajax({
				type : "post",
				url : "/crm/updateConfig",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					console.log("success");
					initConfig();
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
</body>
</body>
</html>