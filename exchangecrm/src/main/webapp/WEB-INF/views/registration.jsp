<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp"%>
<body>

	<div class="container">
		<div class="pull-left" style="width: 50%">
			<h3>24小时注册量：</h3>
			<h1 id="24HRegistration" style="text-indent: 20%">12</h1>
		</div>
		<div class="pull-right" style="width: 50%">
			<div class="row">
				<form class="form-inline pull-right" id="getRegistration">
					<div class="form-group">
						<label class="sr-only" for="startTime">startTime</label> <input
							id="start" type="datetime" class="form-control" name="startTime" placeholder="开始时间"/>
					</div>
					<div class="form-group">
						<label class="sr-only" for="endTime">endTime</label> <input
							id="end" type="datetime" class="form-control" name="endTime"
							class="laydate-icon" placeholder="结束时间" />
					</div>
					<button type="button" class="btn btn-primary " onclick="getRegistration()">搜索</button>

				</form>
			</div>
			<div class="row">
				<h1 id="Registration"style="text-indent: 40%"></h1>
			</div>
		</div>
	</div>
	<script src="<c:url value="/resources/js/registration.js" />"></script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>