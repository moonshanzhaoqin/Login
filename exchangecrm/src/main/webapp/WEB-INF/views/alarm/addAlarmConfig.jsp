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
				<form id="saveConfig" action="<c:url value='/alarm/saveAlarmConfig' />" method="POST">
					<div class="row">
						<div class="col-lg-4 text-right">
							<h4>报警等级：</h4>
						</div>
						
						<div class="col-lg-4">
							<input class="form-control" name="alarmGrade"/>
						</div>
					</div></br>
					
					<div class="row">
						<div class="col-lg-4 text-right">
							<h4>临界阈值下限：</h4>
						</div>
						
						<div class="col-lg-4">
							<input class="form-control" name="criticalThresholdLowerLimit"/>
						</div>
					</div></br>
					
					<div class="row">
						<div class="col-lg-4 text-right">
							<h4>临界阈值下限：</h4>
						</div>
						
						<div class="col-lg-4">
							<input class="form-control" name="criticalThresholdUpperLimit"/>
						</div>
					</div></br>
					
					<div class="row">
						<div class="col-lg-4 text-right">
							<h4>报警方式：</h4>
						</div>
						
						<div class="col-lg-4">
							<select class="form-control" name="alarmMode">
								<option value="1" >短信</option>
								<option value="2" >邮件</option>
								<option value="3" >短信+邮件</option>
							</select>
						</div>
					</div></br>
					
					<div class="row">
						<div class="col-lg-4 text-right">
							<h4>警报接收人员：</h4>
						</div>
						
						<div class="col-lg-4 control-group">
							<c:if test="${not empty list }">
								<c:forEach var="supervisor" items="${list}">
									<div class="col-lg-4">
										<input class="panel-group" name="supervisorId" type="checkbox" value="${supervisor.supervisorId}" /> 
										${supervisor.supervisorName }
									</div>
								</c:forEach>
							</c:if>
						</div>
					</div></br>
					
					<div class="row">
						<div class="col-lg-4 text-right">
						</div>
						
						<div class="col-lg-4">
							<input class="btn btn-primary btn-lg btn-block" type="submit" value="确认"/>
<!-- 							<button id="saveConfigBtn" class="btn btn-primary btn-lg btn-block">确认</button> -->
						</div>
					</div></br>
				</form>
									

			</div>
			
		</div>
		
		
		
		<!--引入Js文件-->
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" ></script>
		<%@ include file="../footer.jsp"%>
	</body>
</html>
