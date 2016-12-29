<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>Anytime Exchange</title>
		
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
		
		<style type="text/css">
			/* enable absolute positioning */
			.inner-addon { 
			    position: relative; 
			}
			/* style icon */
			.inner-addon .glyphicon {
			  position: absolute;
			  padding: 10px;
			  pointer-events: none;
			  font-size: 20px;
			}
			
			/* align icon */
			.left-addon .glyphicon  { left:  0px;}
			.right-addon .glyphicon { right: 0px;}
			
			/* add padding  */
			.left-addon input  { padding-left:  40px; }
			.right-addon input { padding-right: 30px; }	
			
			/**/
			.formbar li{
				list-style:none ;
				display:inline-block;
			}
			
		</style>
	</head>
	
	<body>
		<div class="container">
			<div class="row" style="height: 250px;width: 600px;">
				<img  src="<c:url value="/resources/bootstrap/img/ex.png" />" style="width:200px;height:100px"/>
			</div>
			
			<div class="row">
				<div class="col-md-8">
					<img src="<c:url value="/resources/bootstrap/img/Image 1.jpg" />" />
				</div>
				
				<form class="form-signin form" role="form" action="<c:url value='/dologin' />" method="post">
					<div class="col-md-12" style="height: 400px;width: 320px;background-color: #999;border-radius: 10px;">
						
						<!--标题-->
						<div class="row" style="height:65px;background-color: #C8C8C8;border-top-left-radius: 10px;border-top-right-radius: 10px;">
							<h2 class="form-signin-heading text-center">登录</h2>
						</div>
						
						<!--输入框-->
						<div class="row center-block" style="height:175px;width:100%;margin-top: 15px;">
							<div class="inner-addon left-addon ">
								<span class="glyphicon glyphicon-envelope"></span>
								<input name="adminName" type="text" class="form-control adminName" placeholder="Admin Name" style="border:0px;height: 40px;"/>
							</div>
							<br />
							<div class="inner-addon left-addon">
								<span class="glyphicon glyphicon-lock"></span>
								<input name="adminPassword" type="password" class="form-control adminPassword" style="border:0px;height: 40px;" value="123456"/>
							</div>
						</div>
						
						<!--登录按钮-->
						<div class="row" style="height:145px;background-color: #D4D4D4;border-bottom-left-radius: 10px;border-bottom-right-radius: 10px;">
							<button class="btn btn-lg btn-default btn-block login" type="button" style="width: 280px;text-align: center;margin: auto;margin-top: 30px;">确认</button>
							<div class="row" style="margin-top:20px">
<!-- 								<a href="#" style="margin-left: 35px;">忘记密码？</a> -->
<%-- 								<a href="<c:url value="/regist" />" style="margin-left: 150px;">立即注册</a> --%>
							</div>
						</div>
						
					</div>	
				</form>
				<div class="col-md-1"></div>
			</div>
			<div class="row">
				<input id="retCode"  name="retCode" value="${model.retCode }" type="hidden">
				<input id="retMessage"  name="retMessage" value="${message}" type="hidden">
			</div>
		</div><!--end of container-->
		
		<div class="footer navbar-fixed-bottom" style="margin-left: -60px;">
<%--        	<%@ include file="../views/common/footer.jsp"%>     --%>
		</div>
		
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" ></script>
		<script type="text/javascript">
			$(function(){
				var retCode = $('#retCode').val();
				var retMessage = $('#retMessage').val();
					
				if(retMessage!=null || retMessage!=""){
				alert(retMessage);
				}
				
				$('#retCode').val("");
			});
			//登录提交 
			$('.login').click(function(){
// 				var email = $('.userEmail').val().trim();
// 				var password = $('.userPassword').val().trim();
// 				//检验账号密码
// 				if(!checkEmail(email)){
// 					alert("邮箱格式不正确")
// 					return;
// 				}else{
					$(".form").submit();
// 				}
				
			});
			
			//检查邮箱格式
// 			function checkEmail(email){
// 				var reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
// 				return reg.test(email);
// 			}
			
			
		</script>
		
	</body>
</html>