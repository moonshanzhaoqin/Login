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
			<form class="form-inline pull-right" id="searchWithdraw">
				<div class="form-group">
					<label class="sr-only" for="userPhone">userPhone</label> <input
						type="text" class="form-control" name="userPhone"
						placeholder="手机号">
				</div>
				<div class="form-group">
					<label class="sr-only" for="userName">userName</label> <input
						type="text" class="form-control" name="userName" placeholder="用户名">
				</div>
				<div class="form-group">
					<label class="sr-only" for="startTime">startTime</label> <input
						id="start" class="form-control" name="startTime"
						placeholder="开始时间(申请)" />
				</div>
				<div class="form-group">
					<label class="sr-only" for="endTime">endTime</label> <input
						id="end" class="form-control" name="endTime"
						placeholder="结束时间(申请)" />
				</div>
				<button type="button" class="btn btn-primary "
					onclick="searchWithdraw(1)">搜索</button>
			</form>
		</div>
		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="withdraw">
				<thead>
					<tr>
						<th>提现编号</th>
<!-- 						<th>用户ID</th> -->
						<th>国家码</th>
						<th>手机号</th>
						<th>用户名</th>
						<th>联系邮箱</th>
						<th>金条数量</th>
						<th>对应的goldpay(GDQ)</th>
						<th>手续费(GDQ)</th>
						<th>申请时间</th>
						<th>处理结果</th>
						<th>处理者</th>
						<th>处理时间</th>
<!-- 						<th>金条交易ID(申请)</th> -->
<!-- 						<th>手续费交易ID(申请)</th> -->
<!-- 						<th>金条交易ID(失败/成功)</th> -->
<!-- 						<th>手续费交易ID(失败/成功)</th> -->
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
	
	<script src="<c:url value="/resources/js/withdraw.js" />"></script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>