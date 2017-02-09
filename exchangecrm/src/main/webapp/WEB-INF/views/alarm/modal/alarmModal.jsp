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
					
						<input type="hidden" value="0" name="alarmType" id="alarmType" />
					
						<div class="form-group">
							<label class="col-sm-3 control-label">预警范围</label>
							<div class="col-sm-3 ">
								<div class="input-group">
									<input type="text" class="form-control"
										name="criticalThresholdLowerLimit"
										id="criticalThresholdLowerLimit" /> <span
										class="input-group-addon">%</span>
								</div>
							</div>
							<div class="col-sm-1"
								style="padding: 15px 0; text-align: center; line-height: 50%; display: table;">
								<Strong>~</Strong>
							</div>
							<div class="col-sm-3 ">
							<div class="input-group">
								<input class="form-control" name="criticalThresholdUpperLimit"
									id="criticalThresholdUpperLimit" /> <span
									class="input-group-addon">%</span>
							</div></div>
						</div>


						<div class="form-group">
							<label class="col-sm-3 control-label">预警方式</label>
							<div class="col-sm-7">
								<select class="form-control" name="alarmMode" id="alarmMode">
									<option value="1">短信</option>
									<option value="2">邮件</option>
									<option value="3">短信+邮件</option>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">预警人添加</label>
							<div class="col-sm-7 checkbox">
								<c:if test="${not empty supervisorList }">
									<c:forEach var="supervisor" items="${supervisorList}">
										<div class="col-sm-4">
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