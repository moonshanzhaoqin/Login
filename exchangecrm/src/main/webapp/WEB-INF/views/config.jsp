<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Anytime Exchange</title>
<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
</head>
<body>
	<%@ include file="header.jsp"%>
	<div class="container">
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
								html += '<div class="form-group" id="' + data[i].configKey + '">'
										+ '<label  class="col-lg-2 control-label">'
										+ data[i].configName
										+ '</label>'
										+ '<div class="col-lg-8"><input type="text" class="form-control" value="' + data[i].configValue + '"></div>'
										+ '<div class="col-lg-2"><button type="button" class="btn btn-default" onclick="updateConfig()"><span class="glyphicon glyphicon-ok"></span>&nbsp;保存</button></div>'
										+ '</div>'
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
	<%@ include file="footer.jsp"%>
</body>
</body>
</html>