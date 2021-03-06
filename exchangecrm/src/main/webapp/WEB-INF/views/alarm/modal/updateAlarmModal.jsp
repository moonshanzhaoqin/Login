<%@page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title></title>
	</head>
	<body>
		<div class="modal fade" id="updateAlarmModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
				
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">预警信息编辑</h4>
					</div>
					
					<form id="updateAlarmConfig" method="POST" action="<c:url value="/alarm/updateAlarmConfigInfo" />">
					
						<input class="form-control" name="alarmId" type="hidden" id="updateAlarmId"/>
						
						<input class="form-control" name="alarmType" type="hidden" id="updateAlarmType"/>
					
					
						<div class="modal-body">
						
							<div class="row" id="updateLimitConfig">
								<div class="col-md-4">
			            			<h4 class="text-right">预警范围 :</h4>	
			            		</div>
								<div class="col-md-3">
									<div class="input-group">
										<input type="text" class="form-control" name="criticalThresholdLowerLimit" id="updateLowerLimit" /> 
										<span class="input-group-addon">%</span>
									</div>
								</div>
								<div class="col-md-1"
									style="padding: 15px 0; text-align: center; line-height: 50%; display: table; width: 8px">
									<Strong>~</Strong>
								</div>
								<div class="col-md-3">
									<div class="input-group">
										<input type="text" class="form-control" name="criticalThresholdUpperLimit" id="updateUpperLimit" /> 
										<span class="input-group-addon">%</span>
									</div>	
										
								</div>
							</div>
							
							<div class="row" id="updatelimitPercent" style="display: none;">
								<div class="col-md-4">
			            			<h4 class="text-right">预警百分比 :</h4>	
			            		</div>
								<div class="col-md-4">
									<div class="input-group">
										<input type="text" class="form-control" name="criticalThresholdLowerLimit" id="updatePercentageWarning" /> 
										<span class="input-group-addon">%</span>
									</div>
								</div>
							</div>

							<div class="row">
								<div class="col-md-4">
			            			<h4 class="text-right">预警方式:</h4>	
			            		</div>
			            		<div class="col-md-4">
			            			<select class="form-control" name="alarmMode" id="updateAlarmMode">
										
									</select>
			            		</div>
							</div>
							
							<div class="row">
								<div class="row">
									<div class="col-md-4">
				            			<h4 class="text-right">预警人添加:</h4>	
				            		</div>
				            		<div class="col-md-8" id="updateSupervisorGroup">
				            			
				            		</div>
								</div>
							</div>
						</div>
						
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							<button type="button" class="btn btn-primary submit" id="updateAlarmConfigBtn">确认</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</body>
</html>