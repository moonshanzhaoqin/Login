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
						<th>币种</th>
						<th>公司总账</th>
						<th>用户总账</th>
					</tr>
				</thead>
				<tbody>

					<c:if test="${not empty totalAssets }">
						<c:forEach var="totalAssets" items="${totalAssets}">
							<tr>
								<td style="font-weight: bold;">${totalAssets.currency.currency}
									（${totalAssets.currency.nameCn}）</td>
								<td>${totalAssets.systemTotalAsset}</td>
								<td>${totalAssets.userTotalAsset}</td>
							</tr>
						</c:forEach>
					</c:if>

					<tr style="font-weight: bold;">
						<td>总金额（USD）</td>
						<c:if test="${not empty systemAmount }">
							<td>${systemAmount}</td>
						</c:if>
						<c:if test="${not empty usermAmount }">
							<td>${usermAmount}</td>
						</c:if>
					</tr>
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
