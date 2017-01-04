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
		<div class="panel panel-primary ">
			<div class="panel-heading">
				<h3 class="panel-title">修改密码</h3>
			</div>
			<div class="panel-body">
				<form class="form-horizontal" role="form" id="modifyPassword">
					<div class="form-group">
						<label for="oldPassword" class="col-sm-2 control-label input-lg">原密码</label>
						<div class="col-sm-10">
							<input type="password" class="form-control input-lg"
								name="oldPassword" placeholder="Old Password">
						</div>
					</div>
					<div class="form-group">
						<label for="newPassword" class="col-sm-2 control-label input-lg">新密码</label>
						<div class="col-sm-10">
							<input type="password" class="form-control input-lg"
								name="newPassword" placeholder="New Password">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="button" class="btn btn-primary btn-lg"
								onclick="modifyPassword()">确认修改</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script>
		function modifyPassword() {
			form = document.getElementById("modifyPassword");
			data = {
				oldPassword : form.oldPassword.value,
				newPassword : form.newPassword.value
			}
			$.ajax({
				type : "post",
				url : "/crm/modifyPassword",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					console.log("success");
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
</html>