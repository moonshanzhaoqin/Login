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

</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="container">
	<div class="row ">
	<div class="col-sm-4"></div>
	<button class="btn btn-default btn-lg col-sm-4 " data-toggle="modal" data-target="#myModal">修改密码</button>
	<div class="col-sm-4"></div>
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
					<h4 class="modal-title" id="myModalLabel">修改密码</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="modifyPassword">
					<div class="form-group">
						<label for="oldPassword" class="col-sm-2 control-label">原密码</label>
						<div class="col-sm-10">
							<input type="password" class="form-control"
								name="oldPassword" placeholder="Old Password">
						</div>
					</div>
					<div class="form-group">
						<label for="newPassword" class="col-sm-2 control-label ">新密码</label>
						<div class="col-sm-10">
							<input type="password" class="form-control "
								name="newPassword" placeholder="New Password">
						</div>
					</div>
				</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal"
						onclick="modifyPassword()">提交更改</button>
				</div>
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
					alert("修改成功")
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