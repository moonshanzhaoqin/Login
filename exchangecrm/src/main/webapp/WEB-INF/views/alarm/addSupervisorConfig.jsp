<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>预警设置</title>
		
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
	
	</head>
	
	<body>
	
		<%@ include file="../header.jsp"%>
	
		<div class="container">
			</br>
			</br>
			</br>
			<div>
				<form id="saveSupervisor" action="<c:url value='/alarm/saveSupervisor' />" method="POST">
					<div class="row">
						<div class="col-lg-4 text-right">
							<h4>姓名：</h4>
						</div>
						
						<div class="col-lg-4">
							<input class="form-control" name="supervisorName" id="name"/>
						</div>
					</div></br>
					
					<div class="row">
						<div class="col-lg-4 text-right">
							<h4>手机：</h4>
						</div>
						
						<div class="col-lg-4">
							<input class="form-control" name="supervisorMobile" id="mobile"/>
						</div>
					</div></br>
					
					<div class="row">
						<div class="col-lg-4 text-right">
							<h4>邮箱：</h4>
						</div>
						
						<div class="col-lg-4">
							<input class="form-control" name="supervisorEmail" id="email"/>
						</div>
					</div></br>
				</form>
				
				<div class="row">
					<div class="col-lg-4 text-right">
					</div>
					
					<div class="col-lg-4">
						<button id="saveSupervisorBtn" class="btn btn-primary btn-lg btn-block">确认</button>
					</div>
				</div></br>

			</div>
			
		</div>
		
		
		
		<!--引入Js文件-->
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" ></script>
		
		<script type="text/javascript">
			
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
		
		<%@ include file="../footer.jsp"%>
		
	</body>
</html>
