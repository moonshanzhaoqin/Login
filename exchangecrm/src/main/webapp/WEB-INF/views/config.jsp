<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp"%>
<body>
	<div class="container">
		<div class="row">
			<div class="alert alert-info alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<strong>注意!</strong> 所有操作将会在10分钟后起效。
			</div>
		</div>
		<form class="form-horizontal" id="config"></form>
	</div>

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script>
		$(function() {
			initConfig();
		});

		function initConfig() {
			$
					.ajax({
						type : "post",
						url : "/crm/getConfigList",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : {},
						success : function(data) {
							console.log("success");
							html = "";
							for (var i = 0; i < data.length; i++) {
								if (data[i].configCanChange == "1") {
									html += '<div class="form-group" id="' + data[i].configKey + '">'
											+ '<label  class="col-sm-4 control-label">'
											+ data[i].configName
											+ '</label>'
											+ '<div class="col-sm-4"><input type="text" class="form-control" value="' + data[i].configValue + '"></div>'
											+ '<div class="col-sm-2"><button type="button" class="btn btn-primary" onclick="updateConfig()">保存</button></div>'
											+ '</div>'
								} else {
									html += '<div class="form-group" id="' + data[i].configKey + '">'
											+ '<label  class="col-sm-4 control-label">'
											+ data[i].configName
											+ '</label>'
											+ '<div class="col-sm-4"><input type="text" class="form-control" value="' + data[i].configValue + '" disabled></div>'
											+ '<div class="col-sm-2"><button type="button" class="btn btn-primary" onclick="updateConfig()" disabled>保存</button></div>'
											+ '</div>'
								}

							}
							$('#config').html(html);
						},
						error : function(xhr, err) {
							console.log("error");
							console.log(err);
						},
						async : false
					});
		}

		function updateConfig() {
			var target = event.target || event.currentTarget;
			var id = $(target).parent().parent().attr('id');
			var value = $(target).parent().parent().find('input').val();
			if (id == "reserve_funds" && parseInt(value) != value) {
				alert("请输入整数")
				return;
			}
			data = {
				configKey : id,
				configValue : value
			}
			$.ajax({
				type : "post",
				url : "/crm/updateConfig",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						console.log("success");
						alert("修改成功！");
						initConfig();
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						alert("Something is wrong!")
					}

				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
					alert("Something is wrong!")
				},
				async : false
			});

		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</body>
</html>