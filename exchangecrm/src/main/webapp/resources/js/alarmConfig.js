function getHostName() {
	var hostName = window.location.hostname;
	alert(hostName);
	return hostName;
}

function delAlarmConfig(obj) {
	var r = confirm("确定要删除该信息？");
	if (r != true) {
		return;
	}
	var tds = $(obj).parent().parent().find('td');
	var alarmId = tds.eq(0).text();
	var delAlarmConfigUrl = "alarm/delAlarmConfig";
	location.href = delAlarmConfigUrl + '?alarmId=' + alarmId;
}

function updateAlarmConfig(obj) {
	var tds = $(obj).parent().parent().find('td');
	var alarmId = tds.eq(0).text();

	$
			.ajax({
				url : "alarm/updateAlarmConfig",
				type : "post",
				async : false,
				dataType : "JSON",
				data : {
					alarmId : alarmId,
				},
				success : function(data) {
					if (data.retCode == 1) {
						$('#updateAlarmModal').modal('show');
						$("#updateAlarmId").val(data.crmAlarm.alarmId);

						if (data.crmAlarm.alarmType == null
								|| data.crmAlarm.alarmType == undefined) {
							$("#updateAlarmType").val(0);
						} else {
							$("#updateAlarmType").val(data.crmAlarm.alarmType);
						}

						if (data.crmAlarm.alarmType == 3
								|| data.crmAlarm.alarmType == 4
								|| data.crmAlarm.alarmType == 5
								|| data.crmAlarm.alarmType == 6) {
							$("#updateLimitConfig").hide();
							$('#updateLimitConfig :input').attr('disabled',
									true);
							if (data.crmAlarm.alarmType == 4) {
								$("#updatelimitPercent").show();
								console.log(data.crmAlarm.lowerLimit)
								$("#updatePercentageWarning").val(
										data.crmAlarm.lowerLimit);
							}
						} else {
							$("#updateLowerLimit")
									.val(data.crmAlarm.lowerLimit);
							$("#updateUpperLimit")
									.val(data.crmAlarm.upperLimit);
						}
						// 拼接html

						var optionStr = "";
						if (data.crmAlarm.alarmMode == 1) {
							optionStr = '<option value="1" selected>短信</option><option value="2" >邮件</option><option value="3" >短信+邮件</option>';
						} else if (data.crmAlarm.alarmMode == 2) {
							optionStr = '<option value="1">短信</option><option value="2"  selected>邮件</option><option value="3" >短信+邮件</option>';
						} else {
							optionStr = '<option value="1">短信</option><option value="2">邮件</option><option value="3" selected>短信+邮件</option>';
						}

						$("#updateAlarmMode").html(optionStr);

						var supervisorGroup = '';
						var alarmSupervisor = JSON
								.parse(data.crmAlarm.supervisorIdArr);
						// console.log(alarmSupervisor);
						for (var i = 0; i < data.supervisorList.length; i++) {
							var supervisor = data.supervisorList[i];
							if ($.inArray(supervisor.supervisorId,
									alarmSupervisor) != -1) {
								supervisorGroup += '<div class="col-lg-4"><input class="panel-group" name="supervisorId" type="checkbox" value='
										+ supervisor.supervisorId
										+ ' checked/>'
										+ supervisor.supervisorName + '</div>';
							} else {
								supervisorGroup += '<div class="col-lg-4"><input class="panel-group" name="supervisorId" type="checkbox" value='
										+ supervisor.supervisorId
										+ ' />'
										+ supervisor.supervisorName + '</div>';
							}
						}

						$("#updateSupervisorGroup").html(supervisorGroup);

					}
				}
			});
}

function updateAlarmAvailable(obj, alarmAvailable) {

	if (alarmAvailable == 0) {
		var r = confirm("确定要将该预警状态置为不可用状态么？");
		if (r != true) {
			return;
		}
	} else {
		var r = confirm("确定要将该预警状态置为可用状态么？");
		if (r != true) {
			return;
		}
	}
	var tds = $(obj).parent().parent().find('td');
	var alarmId = tds.eq(0).text();

	var updateAlarmAvailableUrl = "alarm/updateAlarmAvailable";
	location.href = updateAlarmAvailableUrl + '?alarmId=' + alarmId
			+ '&alarmAvailable=' + alarmAvailable;
}

$("#addAlarmConfigBtn")
		.click(
				function() {
					if ($("#alarmType").val().trim() != 3
							&& $("#alarmType").val().trim() != 4
							&& $("#alarmType").val().trim() != 5&& $("#alarmType").val().trim() != 6) {
						var lowerLimit = $("#criticalThresholdLowerLimit")
								.val().trim();
						var upperLimit = $("#criticalThresholdUpperLimit")
								.val().trim();
						if (checkNotBlank(lowerLimit)
								&& checkNotBlank(upperLimit)) {
							if (parseInt(lowerLimit) > parseInt(upperLimit)) {
								alert("下限不能大于上限！");
								return;
							}
						} else {
							alert("有未填写完整的信息，请填写完善后再提交！");
							return;
						}
					} else if ($("#alarmType").val().trim() == 4) {
						var percentageWarning = $("#percentageWarning").val()
								.trim();
						if (!checkNotBlank(percentageWarning)) {
							alert("有未填写完整的信息，请填写完善后再提交！！");
							return;
						}
					}
					$("#addAlarmConfig").submit();
				});

$("#updateAlarmConfigBtn").click(
		function() {
			if ($("#updateAlarmType").val().trim() != 3
					&& $("#updateAlarmType").val().trim() != 4
					&& $("#updateAlarmType").val().trim() != 5&& $("#updateAlarmType").val().trim() != 6) {
				var lowerLimit = $("#updateLowerLimit").val().trim();
				var upperLimit = $("#updateUpperLimit").val().trim();
				if (checkNotBlank(lowerLimit) && checkNotBlank(upperLimit)) {
					if (parseInt(lowerLimit) > parseInt(upperLimit)) {
						alert("下限不能大于上限！");
						return;
					}
				} else {
					alert("有未填写完整的信息，请填写完善后再提交！");
					return;
				}
			} else if ($("#updateAlarmType").val().trim() == 4) {
				var updatePercentageWarning = $("#updatePercentageWarning")
						.val().trim();
				if (!checkNotBlank(updatePercentageWarning)) {
					alert("有未填写完整的信息，请填写完善后再提交！！");
					return;
				}
			}
			$("#updateAlarmConfig").submit();
		});

function checkNotBlank(param) {
	if (param == null || (param == undefined || param == '')) {
		return false;
	}
	return true;
}

function isNumeric(obj) {
	return !isNaN(parseFloat(obj)) && isFinite(obj);
}

function addLargeTransConfig(alarmType) {
	$("#addAlarmConfigModal").modal('show');
	$("#alarmType").val(alarmType);
	if (alarmType == 3 || alarmType == 5 || alarmType == 6) {
		$("#limitConfig").hide();
	}

	if (alarmType == 4) {
		$("#limitConfig").hide();
		$('#limitConfig :input').attr('disabled', true);
		$("#limitPercent").show();
	}

}