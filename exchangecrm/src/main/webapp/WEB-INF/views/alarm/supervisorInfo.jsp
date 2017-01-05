<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>监督人员信息列表</title>
		
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-table.css" />' />
	</head>
	
	<body>
		
		<%@ include file="../header.jsp"%>
		<div class="container">
			
			<div class="row">
				<h4 class="text-left">监督人员信息列表</h4>	
			</div>
		
			
			<!-- -->
			<div class="row">
				<table class = "table table-striped table-bordered">
					<thead>
						<tr>
							<th>编号</th>
							<th>姓名</th>
							<th>手机</th>
							<th>邮箱</th>
							<th>更新时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty list }">
							<c:forEach var="supervisor" items="${list}">
								<tr>
									<td>${supervisor.supervisorId }</td>
									<td>${supervisor.supervisorName }</td>
									<td>${supervisor.supervisorMobile }</td>
									<td>${supervisor.supervisorEmail }</td>
									<td>${supervisor.updateAt }</td>
									<td>
										<a href="#" onclick="delSupervisor(this)">删除</a>
									</td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div><!--row 结束-->
		</div>
		
		
		<!--引入Js文件-->
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap-table.js" />" ></script>
		<script type="text/javascript">
			function delSupervisor(obj){
				
				var r = confirm("确定要删除该信息？");
				
				if(r != true){
					return ;
				}
				
				var tds=$(obj).parent().parent().find('td');
				var alarmId = tds.eq(0).text();
				
				var delAlarmConfigUrl = "<c:url value='/alarm/delAlarmConfig' />";
				location.href=delAlarmConfigUrl+'?alarmId='+alarmId;
			}
		</script>
		<%@ include file="../footer.jsp"%>
	</body>
</html>
