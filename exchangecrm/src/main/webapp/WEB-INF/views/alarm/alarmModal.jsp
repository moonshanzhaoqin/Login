<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>
	<div class="modal fade" id="addAlarmConfigModal" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">预警信息配置</h4>
				</div>

				<form class="form-horizontal" id="addAlarmConfig" method="POST"
					action="<c:url value="/alarm/saveAlarmConfig" />">
					<div class="modal-body">
						<div class="form-group">
							<lable class="col-md-4 control-label"> 预警范围 </lable>
							<div class="col-md-3">
								<input class="form-control" name="criticalThresholdLowerLimit"
									id="criticalThresholdLowerLimit" />
							</div>
							<div class="col-md-1"
								style="padding: 15px 0; text-align: center; line-height: 50%; display: table; width: 8px">
								<Strong>~</Strong>
							</div>
							<div class="col-md-3">
								<input class="form-control" name="criticalThresholdUpperLimit"
									id="criticalThresholdUpperLimit" />
							</div>
						</div>


						<div class="form-group">
							<lable class="col-md-4 control-label"> 预警方式 </lable>
							<div class="col-md-6">
								<select class="form-control" name="alarmMode" id="alarmMode">
									<option value="1">短信</option>
									<option value="2">邮件</option>
									<option value="3">短信+邮件</option>
								</select>
							</div>
						</div>

						<div class="form-group">
							<lable class="col-md-4 control-label"> 预警人添加 </lable>
							<div class="col-md-6 checkbox">
								<c:if test="${not empty supervisorList }">
									<c:forEach var="supervisor" items="${supervisorList}">
										<div class="col-lg-4">
											<input class="panel-group" name="supervisorId"
												type="checkbox" value="${supervisor.supervisorId}" />
											${supervisor.supervisorName }
										</div>
									</c:forEach>
								</c:if>
							</div>
						</div>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-primary submit"
							id="addAlarmConfigBtn">确认</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>