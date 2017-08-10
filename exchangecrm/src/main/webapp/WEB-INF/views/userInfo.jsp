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
			<form class="form-inline pull-right" id="searchUserInfo">
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
						placeholder="开始时间(注册)" />
				</div>
				<div class="form-group">
					<label class="sr-only" for="endTime">endTime</label> <input
						id="end" class="form-control" name="endTime"
						placeholder="结束时间(注册)" />
				</div>
				<button type="button" class="btn btn-primary "
					onclick="searchUserInfo(1)">搜索</button>
				<button type="button" class="btn btn-primary" id="updateImmediately"
					onclick="updateUserInfo()">立即更新</button>
			</form>
		</div>

		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="userInfo">
				<thead>
					<tr>
						<th>国家码</th>
						<th>手机号</th>
						<th>用户名</th>
						<th>注册时间（GMT+8）</th>
						<th>登录时间（GMT+8）</th>
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

	<script src="<c:url value="/resources/js/userInfo.js" />"></script>
	<%@ include file="common/footer.jsp"%>
</body>
</body>
</html>