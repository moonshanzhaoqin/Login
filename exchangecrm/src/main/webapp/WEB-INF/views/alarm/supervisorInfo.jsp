<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Exanytime</title>
		
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-table.css" />' />
	</head>
	
	<body>
		
		<%@ include file="../common/header.jsp"%>
		<div class="container">
		
		
			<div class="row">
				<h4 class="text-left">预警人添加</h4>	
			</div>
		
			<div class="row well">
				<form id="saveSupervisor" action="<c:url value='/alarm/saveSupervisor' />" method="POST">
				
					<div class="col-lg-3 text-right">
						<input class="form-control" name="supervisorName" id="name" placeholder="请填写姓名"/>
					</div>
					
					<div class="col-lg-3 text-right">
						<input class="form-control" name="supervisorMobile" id="mobile" placeholder="请填写手机号(+86)"/>
					</div>
					
					<div class="col-lg-3 text-right">
						<input class="form-control" name="supervisorEmail" id="email" placeholder="请填写邮箱地址"/>
					</div>
				</form>
									
					<div class="col-lg-3 text-center">
						<button id="saveSupervisorBtn" class="btn btn-primary">确认</button>
					</div>
			</div>
		
			<div class="row">
				<h4 class="text-left">预警人员信息列表</h4>	
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
									<td><fmt:formatDate value="${supervisor.updateAt }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
				var supervisorId = tds.eq(0).text();
				
				var delSupervisorUrl = "<c:url value='/alarm/delSupervisor' />";
				location.href=delSupervisorUrl+'?supervisorId='+supervisorId;
			}
			
			$("#saveSupervisorBtn").click(function(){
				
				var name = $("#name").val().trim();
				var mobile = $("#mobile").val().trim();
				var email = $("#email").val().trim();
				
				if(checkNotBlank(name) && checkNotBlank(mobile) && checkNotBlank(email)){
					
					if(checkEmail(email)){
						$("#saveSupervisor").submit();
					}else{
						alert("邮箱格式不正确，请重新填写！");
						return ;
					}
				}else{
					alert("有未填写完整的信息，请填写完善后再提交！");
					return ;
				}
			});
			
			
			function checkNotBlank(param){
				if(param == null ||(param == undefined || param == '')){
					return false;
				}
				return true;
			}
			
			function checkEmail(email){
				var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
				return reg.test(email);
			}
			
		</script>
		<%@ include file="../common/footer.jsp"%>
	</body>
</html>
