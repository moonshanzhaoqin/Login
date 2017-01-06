<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>用户账户信息列表</title>
		
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-table.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-paginator.min.css" />' />
		<link rel="stylesheet" href="<c:url value="/resources/css/common.css" />" />
	
	</head>
	
	<body>
		<%@ include file="../header.jsp"%>
		
		<div class="container">
		
			<div class="row">
				<table class = "table table-striped table-bordered">
					<thead>
						<tr>
							<th>币种</th>
							<th>系统账户信息</th>
							<th>用户账户信息</th>
						</tr>
					</thead>
					<tbody>
					
						<c:if test="${not empty systemTotalAssets }">
							<c:forEach var="systemWallet" items="${systemTotalAssets }">
								<c:if test="${systemWallet.key != 'totalAssets' }">
									<tr>
										<td>${systemWallet.key}</td>
										<td>${systemWallet.value}</td>
										<c:if test="${not empty userTotalAssets }">
											<c:forEach var="userWallet" items="${userTotalAssets }">
												<c:if test="${userWallet.key == systemWallet.key }">
													<td>${userWallet.value}</td>
												</c:if>
											</c:forEach>
										</c:if>
									</tr>
								</c:if>
							</c:forEach>
						</c:if>
						
						<tr>
							<td>总金额（USD）</td>
							<c:if test="${not empty systemTotalAssets }">
								<c:forEach var="wallet" items="${systemTotalAssets }">
									<c:if test="${wallet.key == 'totalAssets' }">
										<td>${wallet.value}</td>
									</c:if>
								</c:forEach>	
							</c:if>
							<c:if test="${not empty userTotalAssets }">
								<c:forEach var="wallet" items="${userTotalAssets }">
									<c:if test="${wallet.key == 'totalAssets' }">
										<td>${wallet.value}</td>
									</c:if>
								</c:forEach>	
							</c:if>
						</tr>
					</tbody>
				
				</table>
			</div>
			
		</div>

		
		<!--引入Js文件-->
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap-table.js" />" ></script>
		
		<%@ include file="../footer.jsp"%>
	</body>
</html>
