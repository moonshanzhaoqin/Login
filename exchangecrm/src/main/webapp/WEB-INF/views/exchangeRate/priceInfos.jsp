<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="../common/header.jsp"%>
<body>
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
	<%@ include file="../common/footer.jsp"%>
</body>
</html>
