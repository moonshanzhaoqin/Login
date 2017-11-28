<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Exanytime</title>
<link rel='icon' href='<c:url value="/resources/img/ex_28x28.ico" />'
	type='image/x-ico' />
<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap-table.css" />' />
<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap-paginator.min.css" />' />
<link rel="stylesheet"
	href="<c:url value="/resources/css/common.css" />" />

</head>

<body>
	<%@ include file="../common/header.jsp"%>

	<div class="container">
		<div class="row">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th>instrument</th>
						<th>bid</th>
						<th>ask</th>
						<th>time(UTC)</th>
					</tr>
				</thead>
				<tbody>

					<c:if test="${not empty priceInfos }">
						<c:forEach var="priceInfo" items="${priceInfos}">
							<tr>
								<td>${priceInfo.instrument }</td>
								<td>${priceInfo.bid }</td>
								<td>${priceInfo.ask }</td>
								<td>${priceInfo.time }</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>

			</table>
		</div>

	</div>


	<!--引入Js文件-->
	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap-table.js" />"></script>
	<script>
		
	</script>
	<%@ include file="../common/footer.jsp"%>
</body>
</html>
