<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
</head>

<body>

	<%@ include file="../common/header.jsp"%>

	<div class="container">
		<!-- -->
		<div class="row well">
			<table class="table">
				<thead>
					<tr>
						<th>用户总账(USD)</th>
						<th>公司入账 (USD)</th>
						<th>预备金(USD)</th>
						<th>可用预备金(USD)</th>
						<th>准备率</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>${userHoldingTotalAssets }</td>
						<td>${exHoldingTotalAssets }</td>
						<td>${reserveFunds }</td>
						<td>${remaining }</td>
						<td><c:if test="${reserveAvailability < 20}">
								<font color="red">${reserveAvailability }%</font>
							</c:if> <c:if
								test="${reserveAvailability >= 20 &&  reserveAvailability<50}">
								<font color="#EC971F">${reserveAvailability }%</font>
							</c:if> <c:if test="${reserveAvailability >= 50}">
								<font color="green">${reserveAvailability }%</font>
							</c:if></td>
					</tr>
				</tbody>
			</table>
		</div>
		<!--row 结束-->

		<div class="row">
			<div class="col-sm-6">
				<h4 class="text-left">预警信息详情</h4>
			</div>
			<div class="col-sm-6 text-right">
				<button class="btn btn-primary " data-backdrop="static" data-toggle="modal"
					data-target="#addAlarmConfigModal">添加预警信息</button>
			</div>
		</div>

		<!-- -->
		<div class="row">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th style="display: none">编号</th>
						<th>下限</th>
						<th>上限</th>
						<th>预警方式</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${not empty list }">
						<c:forEach var="alarmConfig" items="${list}">
							<c:if test="${alarmConfig.alarmType eq 0 }">
								<tr>
									<td style="display: none">${alarmConfig.alarmId }</td>
									<%-- 									<td>${alarmConfig.alarmGrade }</td> --%>
									<td>${alarmConfig.lowerLimit }%</td>
									<td>${alarmConfig.upperLimit }%</td>
									<td><c:if test="${alarmConfig.alarmMode eq 1}">短信</c:if> <c:if
											test="${alarmConfig.alarmMode eq 2}">邮件</c:if> <c:if
											test="${alarmConfig.alarmMode eq 3}">短信+邮件</c:if></td>
									<td><c:if test="${alarmConfig.alarmAvailable eq 0}">
											<font color="red">不可用</font>
										</c:if> <c:if test="${alarmConfig.alarmAvailable eq 1}">可用</c:if></td>
									<td><a href="#" onclick="updateAlarmConfig(this)">编辑</a> <a
										href="#" onclick="delAlarmConfig(this)">删除</a> <c:if
											test="${alarmConfig.alarmAvailable eq 0}">
											<a href="#" onclick="updateAlarmAvailable(this,1)">可用</a>
										</c:if> <c:if test="${alarmConfig.alarmAvailable eq 1}">
											<a href="#" onclick="updateAlarmAvailable(this,0)">不可用</a>
										</c:if></td>
								</tr>
							</c:if>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
		<!--row 结束-->


		<%@ include file="modal/alarmModal.jsp"%>
		<%@ include file="modal/updateAlarmModal.jsp"%>

	</div>


	<!--引入Js文件-->
	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap-table.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/js/alarm/alarmConfig.js" />"></script>

	<%@ include file="../common/footer.jsp"%>
</body>
</html>
