<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Anytime Exchange</title>
		
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-table.css" />' />
	</head>
	
	<body>
		
		<%@ include file="../common/header.jsp"%>
		
		<div class="container">
			<!-- -->
			<div class="row well">
				<table class = "table"> 
					<thead>
						<tr>
							<th>系统总资产（USD）</th>
							<th>用户总资产（USD）</th>
							<th>差额（USD）</th>
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
			
				<div class="col-sm-6">
					<h4 class="text-left">预警信息详情</h4>	
				</div>
				<div class="col-sm-6 text-right" >
					<button class="btn btn-primary " data-toggle="modal" data-target="#addAlarmConfigModal">添加预警信息</button>
				</div>	
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
							<th>预警方式</th>
							<th>状态</th>
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
										<c:if test="${alarmConfig.alarmAvailable eq 0}">
											<font color="red">不可用</font>
										</c:if>
						        		<c:if test="${alarmConfig.alarmAvailable eq 1}">可用</c:if>
									</td>
									<td>
										<a href="#" onclick="updateAlarmConfig(this)">编辑</a>
										<a href="#" onclick="delAlarmConfig(this)">删除</a>
										
										<c:if test="${alarmConfig.alarmAvailable eq 0}">
											<a href="#" onclick="updateAlarmAvailable(this,1)">可用</a>
										</c:if>
										<c:if test="${alarmConfig.alarmAvailable eq 1}">
											<a href="#" onclick="updateAlarmAvailable(this,0)">不可用</a>
										</c:if>
										
									</td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div><!--row 结束-->
			
			
			<%@ include file="alarmModal.jsp"%>
			<%@ include file="updateAlarmModal.jsp"%>

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
			}
			
			
			function updateAlarmConfig(obj){
				var tds=$(obj).parent().parent().find('td');
				var alarmId = tds.eq(0).text();
				
				$.ajax({
						url:"<c:url value='/alarm/updateAlarmConfig' />",
						type:"post",
						async:false,
						dataType:"JSON",
						data:{
							alarmId:alarmId,
						},
						success:function(data){
							if(data.retCode == 1){
								$('#updateAlarmModal').modal('show');
								$("#updateAlarmId").val(data.crmAlarm.alarmId);
								$("#updateAlarmGrade").val(data.crmAlarm.alarmGrade);
								$("#updateLowerLimit").val(data.crmAlarm.lowerLimit);
								$("#updateUpperLimit").val(data.crmAlarm.upperLimit);
								
								//拼接html
								
								var optionStr="";
								if(data.crmAlarm.alarmMode == 1){
									optionStr='<option value="1" selected>短信</option><option value="2" >邮件</option><option value="3" >短信+邮件</option>';
								}else if(data.crmAlarm.alarmMode == 2){
									optionStr='<option value="1">短信</option><option value="2"  selected>邮件</option><option value="3" >短信+邮件</option>';
								}else{
									optionStr='<option value="1">短信</option><option value="2">邮件</option><option value="3" selected>短信+邮件</option>';
								}
								
								$("#updateAlarmMode").html(optionStr);
								
								var supervisorGroup = '';
								for(var i = 0; i<data.list.length;i++){
									var supervisor = data.list[i];
									if((data.crmAlarm.supervisorIdArr).indexOf(supervisor.supervisorId) != -1){
										supervisorGroup +='<div class="col-lg-4"><input class="panel-group" name="supervisorId" type="checkbox" value='+supervisor.supervisorId+' checked/>'+supervisor.supervisorName+'</div>';
									}else{
										supervisorGroup +='<div class="col-lg-4"><input class="panel-group" name="supervisorId" type="checkbox" value='+supervisor.supervisorId+' />'+supervisor.supervisorName+'</div>';
									}
								}
								
								$("#updateSupervisorGroup").html(supervisorGroup);
								
							}
						}
					});
			}
			
			function updateAlarmAvailable(obj,alarmAvailable){
				
				if(alarmAvailable == 0){
					var r = confirm("确定要将该预警状态置为不可用状态么？");
					if(r != true){
						return ;
					}
				}else{
					var r = confirm("确定要将该预警状态置为可用状态么？");
					if(r != true){
						return ;
					}
				}
				var tds=$(obj).parent().parent().find('td');
				var alarmId = tds.eq(0).text();
				
				var updateAlarmAvailableUrl = "<c:url value='/alarm/updateAlarmAvailable' />";
				location.href=updateAlarmAvailableUrl+'?alarmId='+alarmId+'&alarmAvailable='+alarmAvailable;
			}
			
			
			$("#addAlarmConfigBtn").click(function(){				
				var alarmGrade = $("#alarmGrade").val().trim();
				var lowerLimit = $("#criticalThresholdLowerLimit").val().trim();
				var upperLimit = $("#criticalThresholdUpperLimit").val().trim();
				
				if( checkNotBlank(alarmGrade) && checkNotBlank(lowerLimit) && checkNotBlank(upperLimit)){
					if(parseInt(lowerLimit) > parseInt(upperLimit)){
						alert("下限不能大于上限！");
						return ;
					}
				}else{
					alert("有未填写完整的信息，请填写完善后再提交！");
					return ;
				}
				$("#addAlarmConfig").submit();
			});
			
			
			$("#updateAlarmConfigBtn").click(function(){		
				var alarmGrade = $("#updateAlarmGrade").val().trim();
				var lowerLimit = $("#updateLowerLimit").val().trim();
				var upperLimit = $("#updateUpperLimit").val().trim();
				
				if( checkNotBlank(alarmGrade) && checkNotBlank(lowerLimit) && checkNotBlank(upperLimit)){
					if(parseInt(lowerLimit) > parseInt(upperLimit)){
						alert("下限不能大于上限！");
						return ;
					}
				}else{
					alert("有未填写完整的信息，请填写完善后再提交！");
					return ;
				}
				$("#updateAlarmConfig").submit();
			});

			function checkNotBlank(param){
				if(param == null ||(param == undefined || param == '')){
					return false;
				}
				return true;
			}
			
			
		</script>
		<%@ include file="../common/footer.jsp"%>
	</body>
</html>
