<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>预警信息</title>
		
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-table.css" />' />
	</head>
	
	<body>
		
		<%@ include file="../header.jsp"%>
		
		<div class="container">
			<!-- -->
			<div class="row well">
				<table class = "table"> 
					<thead>
						<tr>
							<th>系统总资产</th>
							<th>用户总资产</th>
							<th>差额</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${systemTotalAssets }</td>
							<td>${userTotalAssets }</td>
							<td>${difference }</td>
						</tr>
					</tbody>
				</table>
			</div><!--row 结束-->
			
			<div class="row">
				<h4 class="text-left">预警信息详情</h4>	
			</div>
			
			<!-- -->
			<div class="row">
				<table class = "table table-striped table-bordered">
					<thead>
						<tr>
							<th style="display:none">编号</th>
							<th>等级</th>
							<th>下限</th>
							<th>上限</th>
							<th>报警方式</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty list }">
							<c:forEach var="alarmConfig" items="${list}">
								<tr>
									<td style="display:none">${alarmConfig.alarmId }</td>
									<td>${alarmConfig.alarmGrade }</td>
									<td>${alarmConfig.lowerLimit }</td>
									<td>${alarmConfig.upperLimit }</td>
									<td>
										<c:if test="${alarmConfig.alarmMode eq 1}">短信</c:if>
						        		<c:if test="${alarmConfig.alarmMode eq 2}">邮件</c:if>
						        		<c:if test="${alarmConfig.alarmMode eq 3}">短信+邮件</c:if>
									</td>
									<td>
										<a href="#" onclick="delAlarmConfig(this)">删除</a>
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
			function delAlarmConfig(obj){
				
				var r = confirm("确定要删除该信息？");
				
				if(r != true){
					return ;
				}
				
				var tds=$(obj).parent().parent().find('td');
				var alarmId = tds.eq(0).text();
				
				var delAlarmConfigUrl = "<c:url value='/alarm/delAlarmConfig' />";
				location.href=delAlarmConfigUrl+'?alarmId='+alarmId;
				
// 				alert("alarmId : "+alarmId);
			}
		</script>
		<%@ include file="../footer.jsp"%>
	</body>
</html>
