function getHostName(){
	var hostName = window.location.hostname;
	alert(hostName);
	return hostName;
}


function delAlarmConfig(obj){
	var r = confirm("确定要删除该信息？");
	if(r != true){
		return ;
	}
	var tds=$(obj).parent().parent().find('td');
	var alarmId = tds.eq(0).text();
	var delAlarmConfigUrl ="/crm/alarm/delAlarmConfig";
	location.href=delAlarmConfigUrl+'?alarmId='+alarmId;
}


function updateAlarmConfig(obj){
	var tds=$(obj).parent().parent().find('td');
	var alarmId = tds.eq(0).text();
	
	$.ajax({
			url:"/crm/alarm/updateAlarmConfig",
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
					
					if(data.crmAlarm.alarmType== null ||data.crmAlarm.alarmType == undefined ){
						$("#updateAlarmType").val(0);
					}else{
						$("#updateAlarmType").val(data.crmAlarm.alarmType);
					}
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
					for(var i = 0; i<data.supervisorList.length;i++){
						var supervisor = data.supervisorList[i];
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
	
	var updateAlarmAvailableUrl = "/crm/alarm/updateAlarmAvailable";
	location.href=updateAlarmAvailableUrl+'?alarmId='+alarmId+'&alarmAvailable='+alarmAvailable;
}


$("#addAlarmConfigBtn").click(function(){			
	var lowerLimit = $("#criticalThresholdLowerLimit").val().trim();
	var upperLimit = $("#criticalThresholdUpperLimit").val().trim();
	
	if( checkNotBlank(lowerLimit) && checkNotBlank(upperLimit)){
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
	var lowerLimit = $("#updateLowerLimit").val().trim();
	var upperLimit = $("#updateUpperLimit").val().trim();
	
	if(checkNotBlank(lowerLimit) && checkNotBlank(upperLimit)){
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

function isNumeric(obj) {
    return !isNaN(parseFloat(obj)) && isFinite(obj);
}

function addLargeTransConfig(alarmType){
	$("#addAlarmConfigModal").modal('show');
	$("#alarmType").val(alarmType);
}