$(function() {
	getGoldqPayClientByPage(1);
	$('#addGoldqPayClientModal').on('show.bs.modal', function(e) {
		form = document.getElementById("addGoldqPayClient");
		form.areaCode.value = '';
		form.userPhone.value = '';
	})

	$('#updategoldqPayClientModal').on('show.bs.modal', function(e) {
		// do something...
		var tr = $(e.relatedTarget) // Button that triggered the modal
		var goldqPayClient = tr.data('whatever') // Extract info from data-*
		// attributes
		console.log(goldqPayClient);
		form = document.getElementById("updateGoldqPayClient");
		form.id.value = goldqPayClient.id;
		form.exId.value = goldqPayClient.exId;
		form.clientId.value = goldqPayClient.clientId;
		form.secretKey.value = goldqPayClient.secretKey;
		form.name.value = goldqPayClient.name;
		form.redirectUrl.value = goldqPayClient.redirectUrl;
		form.customDomain.value = goldqPayClient.customDomain;
	})

	$('#updategoldqPayFeeModal').on('show.bs.modal', function(e) {
		// do something...
		var tr = $(e.relatedTarget) // Button that triggered the modal
		var goldqPayFee = tr.data('whatever') // Extract info from data-*
		// attributes
		console.log(goldqPayFee);
		form = document.getElementById("updateGoldqPayFee");
		form.feeId.value = goldqPayFee.feeId;
		form.clientId.value = goldqPayFee.clientId;
		form.payRole.value = goldqPayFee.payRole;
		form.exemptAmount.value = goldqPayFee.exemptAmount;
		form.feePercent.value = goldqPayFee.feePercent;
		form.minFee.value = goldqPayFee.minFee;
		form.maxFee.value = goldqPayFee.maxFee;
		form.feePayer.value = goldqPayFee.feePayer;
	})
});

function getGoldqPayClientByPage(currentPage) {
	// form = document.getElementById("searchGoldqPayClient");
	// form.userPhone.value = userPhone;
	// form.userName.value = userName;
	// form.startTime.value = startTime;
	// form.endTime.value = endTime;
	var data = {
		currentPage : currentPage
	// userPhone : userPhone,
	// userName : userName,
	// startTime : startTime,
	// endTime : endTime
	};

	$
			.ajax({
				type : "POST",
				url : "/crm/getGoldqPayClientByPage",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					// console.log(data);
					if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("success");
						var html = "";
						for ( var i in data.rows) {
							html += '<tr id="'
									+ data.rows[i].clientId
									+ '">'
									+ '<td>'
									+ data.rows[i].exId
									+ '</td>'
									+ '<td>'
									+ data.rows[i].clientId
									+ '</td>'
									+ '<td>'
									+ data.rows[i].secretKey
									+ '</td>'
									+ '<td>'
									+ (data.rows[i].name == null ? ""
											: data.rows[i].name)
									+ '</td>'
									+ '<td>'
									+ (data.rows[i].redirectUrl == null ? ""
											: data.rows[i].redirectUrl)
									+ '</td>'
									+ '<td>'
									+ (data.rows[i].customDomain == null ? ""
											: data.rows[i].customDomain)
									+ '</td>'
									+ '<td>'
									+ '<a  data-toggle="modal" data-target="#updategoldqPayClientModal" data-whatever='
									+ "'"
									+ JSON.stringify(data.rows[i])
									+ "'"
									+ '>修改</a> '
									+ '<a href="javascript:void(0)" onclick="getGoldqPayFee('
									+ "'" + data.rows[i].clientId + "'"
									+ ')">手续费</a> ' + '</td>' + '</tr>'
						}
						$('#goldqPayClient tbody').html(html);
						if (data.currentPage == 1) {
							paginator(data.currentPage, data.pageTotal);
						}
						$('#total').html(data.total);
						page = data.currentPage;
					}
				},
				error : function(xhr, err) {
					alert("未知错误");
					console.log(err);
				}
			});
}

function updateGoldqPayClient() {
	var form = document.getElementById("updateGoldqPayClient");

	var data = {
		id : form.id.value,
		exId : form.exId.value,
		clientId : form.clientId.value,
		secretKey : form.secretKey.value,
		name : form.name.value,
		redirectUrl : form.redirectUrl.value,
		customDomain : form.customDomain.value,
	}
	$.ajax({
		type : "post",
		url : "/crm/updateGoldqPayClient",
		contentType : "application/json; charset=utf-8",
		dataType : 'json',
		data : JSON.stringify(data),
		success : function(data) {
			if (data.retCode == "00000") {
				console.log("updateGoldqPayClient success");
				$('#updategoldqPayClientModal').modal('hide')
				alert("修改成功！");
				getGoldqPayClientByPage(page);
			} else if (data.retCode == "00002") {
				location.href = loginUrl;
			} else {
				console.log("updateGoldqPayClient" + data.message);
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

function getGoldqPayFee(clientId) {
	$("tr").removeClass("success")
	$("#" + clientId).addClass("success")
	var data = {
		clientId : clientId,
	}
	$
			.ajax({
				type : "post",
				url : "/crm/getGoldqPayFee",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					console.log("success");
					var html = "";
					for (var i = 0; i < data.length; i++) {
						html += '<tr>' + '<td>'
								+ data[i].clientId
								+ '</td>'
								+ '<td>'
								+ showPayRole(data[i].payRole)
								+ '</td>'
								+ '<td>'
								+ data[i].exemptAmount
								+ '</td>'
								+ '<td>'
								+ data[i].feePercent
								+ '</td>'
								+ '<td>'
								+ data[i].minFee
								+ '</td>'
								+ '<td>'
								+ data[i].maxFee
								+ '</td>'
								+ '<td>'
								+ showPayRole(data[i].feePayer)
								+ '</td>'
								+ '<td>'
								+ '<a  data-toggle="modal" data-target="#updategoldqPayFeeModal" data-whatever='
								+ "'" + JSON.stringify(data[i]) + "'"
								+ '>修改</a>' + '</td>' + '</tr>'
					}
					$('#goldqPayFee tbody').html(html);
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
				},
				async : false
			});
}

function updateGoldqPayFee() {
	form = document.getElementById("updateGoldqPayFee");
	if (parseInt(form.exemptAmount.value) == form.exemptAmount.value
			&& parseInt(form.minFee.value) == form.minFee.value
			&& parseInt(form.maxFee.value) == form.maxFee.value) {
		data = {
			feeId : form.feeId.value,
			clientId : form.clientId.value,
			payRole : form.payRole.value,
			exemptAmount : form.exemptAmount.value,
			feePercent : form.feePercent.value,
			minFee : form.minFee.value,
			maxFee : form.maxFee.value,
			feePayer : form.feePayer.value
		}
		$.ajax({
			type : "post",
			url : "/crm/updateGoldqPayFee",
			contentType : "application/json; charset=utf-8",
			dataType : 'json',
			data : JSON.stringify(data),
			success : function(data) {
				if (data.retCode == "00000") {
					console.log("updateGoldqPayFee success");
					$('#updategoldqPayFeeModal').modal('hide')
					alert("修改成功！");
					getGoldqPayFee(clientId);
				} else if (data.retCode == "00002") {
					location.href = loginUrl;
				} else {
					console.log("updateGoldqPayFee" + data.message);
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

function addGoldqPayClient() {
	form = document.getElementById("addGoldqPayClient");
	data = {
		areaCode : form.areaCode.value,
		userPhone : form.userPhone.value,
	}
	$.ajax({
		type : "post",
		url : "/crm/addGoldqPayClient",
		contentType : "application/json; charset=utf-8",
		dataType : 'json',
		data : JSON.stringify(data),
		success : function(data) {
			if (data.retCode == "00000") {
				console.log("addGoldqPayClient success");
				$('#addGoldqPayClientModal').modal('hide')
				alert("添加成功！");
				getGoldqPayClientByPage(1);
			} else if (data.retCode == "00002") {
				location.href = loginUrl;
			} else {
				console.log("updateGoldqPayFee" + data.message);
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

function showPayRole(payRole) {
	switch (payRole) {
	case 1:
		return "接收方";
	case 2:
		return "付款方";
	default:
		return "未知，出错！"
	}

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
			getGoldqPayClientByPage(page);
		}
	}
	// 分页控件
	$('#paginator').bootstrapPaginator(options);
}
