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

	initCampaign(1);

	$('#changeBonusModal').on('show.bs.modal', function(e) {
		// do something...
		var tr = $(e.relatedTarget) // Button that triggered the modal
		var campaign = tr.data('whatever') // Extract info from data-*
		// attributes
		console.log(campaign);
		form = document.getElementById("changeBonus");
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
		form.additionalBudget.value = '';
	})
	$('#addCampaignModal').on('show.bs.modal', function(e) {
		form = document.getElementById("addCampaign");
		form.startTime.value = '';
		form.endTime.value = '';
		form.campaignBudget.value = '';
		form.inviterBonus.value = '';
		form.inviteeBonus.value = '';

	})

});

/* 页面初始化 */
function initCampaign(currentPage) {
	var data = {
		currentPage : currentPage
	}

	$
			.ajax({
				type : "post",
				url : "/crm/getCampaignList",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					var html = "";
					for ( var i in data.rows) {
						html += '<tr>' + '<td>'
								+ data.rows[i].campaignId
								+ '</td>'
								+ '<td>'
								+ timeDate(data.rows[i].startTime)
								+ ' ~ '
								+ timeDate(data.rows[i].endTime)
								+ '</td>'
								// + '<td>'
								// + data[i].campaignStatus
								// + '</td>'
								+ '<td>'
								+ data.rows[i].campaignBudget
								+ '</td>'
								+ '<td>'
								+ (data.rows[i].campaignBudget - data.rows[i].budgetSurplus)
								+ '</td>'
								+ '<td>'
								+ data.rows[i].budgetSurplus
								+ '</td>'
								+ '<td>'
								+ data.rows[i].inviterBonus
								+ '</td>'
								+ '<td>'
								+ data.rows[i].inviteeBonus
								+ '</td>'
								// + '<td>'
								// + timeDate(data.rows[i].updateTime)
								// + '</td>'
								+ '<td>'
								+ (data.rows[i].campaignStatus == 0 ? ('<a href="" onclick="openCampaign('
										+ data.rows[i].campaignId + ')">开启</a>')
										: ('<a href="" onclick="closeCampaign('
												+ data.rows[i].campaignId + ')">关闭</a>'))
								+ '</td>'
								+ '<td>'
								+ '<a  data-toggle="modal" data-target="#changeBonusModal" data-whatever='
								+ "'"
								+ JSON.stringify(data.rows[i])
								+ "'"
								+ '>修改</a>'
								+ '</td>'
								+ '<td>'
								+ '<a  data-toggle="modal" data-target="#addBudgetModal" data-whatever='
								+ "'" + JSON.stringify(data.rows[i]) + "'"
								+ '>追加</a>' + '</td>' + '</tr>'
					}
					$('#campaign tbody').html(html);
					if (data.currentPage == 1) {
						paginator(data.currentPage, data.pageTotal);
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
/* 新增活动 */
function addCampaign() {
	form = document.getElementById("addCampaign");
	if (parseInt(form.campaignBudget.value) == form.campaignBudget.value
			&& parseInt(form.inviterBonus.value) == form.inviterBonus.value
			&& parseInt(form.inviteeBonus.value) == form.inviteeBonus.value) {

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
					$('#addCampaignModal').modal('hide')
					alert("添加成功！");
					initCampaign(1);
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
	} else {
		alert("GDQ需为整数");
	}
}
/* 修改奖励金 */
function changeBonus() {
	form = document.getElementById("changeBonus");
	if (parseInt(form.inviterBonus.value) == form.inviterBonus.value
			&& parseInt(form.inviteeBonus.value) == form.inviteeBonus.value) {
		data = {
			campaignId : form.campaignId.value,
			inviterBonus : form.inviterBonus.value,
			inviteeBonus : form.inviteeBonus.value,
		}
		$.ajax({
			type : "post",
			url : "/crm/changeBonus",
			contentType : "application/json; charset=utf-8",
			dataType : 'json',
			data : JSON.stringify(data),
			success : function(data) {
				if (data.retCode == "00000") {
					console.log("changeBonus success");
					$('#changeBonusModal').modal('hide')
					alert("修改成功！");
					initCampaign(1);
				} else if (data.retCode == "00002") {
					location.href = loginUrl;
				} else {
					console.log("changeBonus" + data.message);
					alert(data.message);
				}
			},
			error : function(xhr, err) {
				console.log("error");
				console.log(err);
				console.log(xhr);
				alert("未知错误！");
			},
			async : false
		});
	} else {
		alert("GDQ需为整数");
	}
}
/* 追加预算 */
function addBudget() {
	form = document.getElementById("addBudget");
	if (parseInt(form.additionalBudget.value) == form.additionalBudget.value) {

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
					initCampaign(1);
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
				alert("未知错误！");
			},
			async : false
		});
	} else {
		alert("GDQ需为整数");
	}
}
/* 开启活动 */
function openCampaign(campaignId) {
	data = {
		campaignId : campaignId,
	}
	$.ajax({
		type : "post",
		url : "/crm/openCampaign",
		contentType : "application/json; charset=utf-8",
		dataType : 'json',
		data : JSON.stringify(data),
		success : function(data) {
			if (data.retCode == "00000") {
				console.log("openCampaign success");
				alert("开启成功！");
				initCampaign(1);
			} else if (data.retCode == "00002") {
				location.href = loginUrl;
			} else {
				console.log("openCampaign" + data.message);
				alert("活动 " + data.message + " 在开启状态，请关闭后再试");
			}
		},
		error : function(xhr, err) {
			console.log("error");
			console.log(err);
			console.log(xhr);
			alert("未知错误！");
		},
		async : false
	});
}
/* 关闭活动 */
function closeCampaign(campaignId) {
	data = {
		campaignId : campaignId,
	}
	$.ajax({
		type : "post",
		url : "/crm/closeCampaign",
		contentType : "application/json; charset=utf-8",
		dataType : 'json',
		data : JSON.stringify(data),
		success : function(data) {
			if (data.retCode == "00000") {
				console.log("closeCampaign success");
				alert("关闭成功！");
				initCampaign(1);
			} else if (data.retCode == "00002") {
				location.href = loginUrl;
			} else {
				console.log("closeCampaign" + data.message);
				alert(data.message);
			}
		},
		error : function(xhr, err) {
			console.log("error");
			console.log(err);
			console.log(xhr);
			alert("未知错误！");
		},
		async : false
	});
}


// 分页
function paginator(currentPage, pageTotal) {
	console.log("paginator:currentPage=" + currentPage + ",pageTotal="
			+ pageTotal);
	var options = {
		currentPage : currentPage,// 当前页
		totalPages : pageTotal,// 总页数
		size : 'normal',
		alignment : 'right',
		numberOfPages : 10,// 显示页数
		itemTexts : function(type, page, current) {
			switch (type) {
			case "first":
				return "<<";
			case "prev":
				return "<";
			case "next":
				return ">";
			case "last":
				return ">>";
			case "page":
				return "" + page;
			}
		},
		onPageClicked : function(event, originalEvent, type, page) {
			initCampaign(page);
		}
	}
	// 分页控件
	$('#paginator').bootstrapPaginator(options);
}
