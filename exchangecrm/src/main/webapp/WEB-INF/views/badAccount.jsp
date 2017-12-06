<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp"%>
<body>
	<div class="container" style="height: 100%;">

		<!-- 		<div class="row" id="task" style="margin-bottom: 20px;"></div> -->

		<div class="row">
			<table class="table table-bordered table-hover" id="badAccount">
				<thead>
					<tr>
						<th>手机号</th>
						<th>币种</th>
						<th>时间区间（GMT+8）</th>
						<th>交易前金额</th>
						<th>交易累计金额</th>
						<th>交易后期望金额</th>
						<th>交易后实际金额</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody style="height: 250px; overflow: auto;"></tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
		</div>

		<hr style="background-color: grey; height: 1px;" />

		<div id="detail" class="row" style="height: 400px; overflow: auto;">
			<table class="table table-bordered table-hover table-striped"
				id="walletSeq">
				<thead>
					<tr>
						<th>流水号</th>
						<th>币种</th>
						<th>数量</th>
						<th>交易类型</th>
						<th>交易ID</th>
						<th>创建时间（GMT+8）</th>
					</tr>
				</thead>
				<tbody
					style="display: table-row-group; height: 400px; overflow: auto;"></tbody>
			</table>
		</div>

	</div>
	<%@ include file="badAccountDetail.jsp"%>

	<script src="<c:url value="/resources/js/badAccount.js" />"></script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>