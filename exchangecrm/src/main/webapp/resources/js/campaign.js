/**
 * 
 */
$(function() {
	var start = {
		elem : '#start',
		format : 'YYYY-MM-DD hh:mm:ss',
		min : laydate.now(), // 设定最小日期为当前日期
		max : '2099-06-16 23:59:59', // 最大日期
		istime : true,
		istoday : false,
		choose : function(datas) {
			end.min = datas; // 开始日选好后，重置结束日的最小日期
			end.start = datas // 将结束日的初始值设定为开始日
		}
	};
	var end = {
		elem : '#end',
		format : 'YYYY-MM-DD hh:mm:ss',
		min : laydate.now(),
		max : '2099-06-16 23:59:59',
		istime : true,
		istoday : false,
		choose : function(datas) {
			start.max = datas; // 结束日选好后，重置开始日的最大日期
		}
	};
	laydate(start);
	laydate(end);

	initCampaign();

	$('#changeBounsModal').on('show.bs.modal', function(e) {
		// do something...
		var tr = $(e.relatedTarget) // Button that triggered the modal
		var campaign = tr.data('whatever') // Extract info from data-*
		// attributes
		console.log(campaign);
		form = document.getElementById("changeBouns");
		form.campaignId.value = campaign.campaignId;
		form.inviterBonus.value = campaign.inviterBonus;
		form.inviteeBonus.value = campaign.inviteeBonus;
	})
	$('#addBudgetModal').on('show.bs.modal', function(e) {
		// do something...
		var tr = $(e.relatedTarget) // Button that triggered the modal
		var campaign = tr.data('whatever') // Extract info from data-*
		// attributes
		console.log(campaign);
		form = document.getElementById("addBudget");
		form.campaignId.value = campaign.campaignId;
		form.campaignBudget.value = campaign.campaignBudget;
	})

});

/* 页面初始化 */
function initCampaign() {
	$
			.ajax({
				type : "post",
				url : "/crm/getCampaignList",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : {},
				success : function(data) {
					var html = "";
					for (var i = 0; i < data.length; i++) {
						html += '<tr>' + '<td>'
								+ data[i].campaignId
								+ '</td>'
								+ '<td>'
								+ timeDate(data[i].startTime)
								+ '</td>'
								+ '<td>'
								+ timeDate(data[i].endTime)
								+ '</td>'
								+ '<td>'
								+ data[i].campaignStatus
								+ '</td>'
								+ '<td>'
								+ data[i].campaignBudget
								+ '</td>'
								+ '<td>'
								+ data[i].budgetSurplus
								+ '</td>'
								+ '<td>'
								+ data[i].inviterBonus
								+ '</td>'
								+ '<td>'
								+ data[i].inviteeBonus
								+ '</td>'
								+ '<td>'
								+ timeDate(data[i].updateTime)
								+ '</td>'
								+ '<td>'
								+ '<a href="" onclick=" changeCurrencyStatus(this,1)">开启</a>'
								+ '</td>'
								+ '<td>'
								+ '<a  data-toggle="modal" data-target="#changeBounsModal" data-whatever='
								+ "'"
								+ JSON.stringify(data[i])
								+ "'"
								+ '>修改</a>'
								+ '</td>'
								+ '<td>'
								+ '<a  data-toggle="modal" data-target="#addBudgetModal" data-whatever='
								+ "'" + JSON.stringify(data[i]) + "'"
								+ '>追加</a>' + '</td>'

								+ '</tr>'
					}
					$('#campaign tbody').html(html);

				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
					alert("未知错误!");
				},
				async : false
			});
}
/* 新增活动 */
function addCampaign() {
	form = document.getElementById("addCampaign");
	var data = {
		startTime : form.startTime.value,
		endTime : form.endTime.value,
		campaignBudget : form.campaignBudget.value,
		inviterBonus : form.inviterBonus.value,
		inviteeBonus : form.inviteeBonus.value
	};
	$.ajax({
		type : "post",
		url : "/crm/addCampaign",
		contentType : "application/json; charset=utf-8",
		dataType : 'json',
		data : JSON.stringify(data),
		success : function(data) {
			if (data.retCode == "00000") {
				console.log("success");
				$('#addCampaign').modal('hide')
				alert("添加成功！");
				initCampaign();
			} else if (data.retCode == "00002") {
				location.href = loginUrl;
			} else if (data.retCode == "00003") {
				console.log("parameter is empty");
				alert("请填写完整！");
			} else {
				console.log("addCampaign" + data.message);
				alert(data.message);
			}
		},
		error : function(xhr, err) {
			console.log("error");
			console.log(err);
			console.log(xhr);
			alert("未知错误!");
		},
		async : false
	});
}
/* 修改奖励金 */
function changeBouns() {
	form = document.getElementById("changeBouns");
	data = {
		campaignId : form.campaignId.value,
		inviterBonus : form.inviterBonus.value,
		inviteeBonus : form.inviteeBonus.value,
	}
	$.ajax({
		type : "post",
		url : "/crm/changeBouns",
		contentType : "application/json; charset=utf-8",
		dataType : 'json',
		data : JSON.stringify(data),
		success : function(data) {
			if (data.retCode == "00000") {
				console.log("changeBouns success");
				$('#changeBounsModal').modal('hide')
				alert("修改成功！");
				initCampaign();
			} else if (data.retCode == "00002") {
				location.href = loginUrl;
			} else {
				console.log("changeBouns" + data.message);
				alert(data.message);
			}
		},
		error : function(xhr, err) {
			console.log("error");
			console.log(err);
			console.log(xhr);
			alert("something is wrong!");
		},
		async : false
	});
}
/* 追加预算 */
function addBudget() {
	form = document.getElementById("addBudget");
	data = {
		campaignId : form.campaignId.value,
		additionalBudget : form.additionalBudget.value,
	}
	$.ajax({
		type : "post",
		url : "/crm/addBudget",
		contentType : "application/json; charset=utf-8",
		dataType : 'json',
		data : JSON.stringify(data),
		success : function(data) {
			if (data.retCode == "00000") {
				console.log("addBudget success");
				$('#addBudgetModal').modal('hide')
				alert("修改成功！");
				initCampaign();
			} else if (data.retCode == "00002") {
				location.href = loginUrl;
			} else {
				console.log("addBudget" + data.message);
				alert(data.message);
			}
		},
		error : function(xhr, err) {
			console.log("error");
			console.log(err);
			console.log(xhr);
			alert("something is wrong!");
		},
		async : false
	});
}
/* 开启或关闭活动,活动开关 */
function campaignSwitch(campaignId, campaignStatus) {

}