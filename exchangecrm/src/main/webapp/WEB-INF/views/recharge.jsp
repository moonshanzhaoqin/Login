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
			<form class="form-inline  pull-right" id="searchRecharge">
				<div class="form-group">
					<label class="sr-only" for="userPhone">userPhone</label> <input
						type="text" class="form-control" name="userPhone"
						placeholder="手机号">
				</div>
				<div class="form-group">
					<label class="sr-only" for="lowerAmount">userPhone</label> <input
						type="text" class="form-control" name="lowerAmount"
						placeholder="交易数量>=">
				</div>
				<div class="form-group">
					<label class="sr-only" for="upperAmount">userPhone</label> <input
						type="text" class="form-control" name="upperAmount"
						placeholder="交易数量<=">
				</div>
				<div class="form-group">
					<label class="sr-only" for="startTime">startTime</label> <input
						id="start" class="form-control" name="startTime"
						placeholder="开始时间" />
				</div>
				<div class="form-group">
					<label class="sr-only" for="endTime">endTime</label> <input
						id="end" class="form-control" name="endTime" placeholder="结束时间" />
				</div>
				<div class="form-group">
					<label class="sr-only" for="transferType">transferType</label> <select
						class="form-control" name="transferType">
<!-- 						<option value="5">GoldPay</option> -->
						<option value="7">PayPal</option>
					</select>
				</div>
				<button type="button" class="btn btn-primary"
					onclick="searchRecharge(1)">搜索</button>
			</form>
		</div>

		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="recharge">
				<thead>
					<tr>
						<th>用户ID</th>
						<th>国家码</th>
						<th>手机号</th>
						<th>交易号</th>
						<th>交易数量(GDQ)</th>
						<th>手续费(GDQ)</th>
						<th>支付币种</th>
						<th>支付金额</th>
						<th>交易方式</th>
						<th>交易时间（GMT+8）</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
			<div id="total" class="pull-right"></div>
		</div>

	</div>
	<script src="<c:url value="/resources/js/recharge.js" />"></script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>