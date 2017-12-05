$(function() {
	$('#withdrawDetailModal')
			.on(
					'show.bs.modal',
					function(e) {
						// do something...
						var tr = $(e.relatedTarget) // Button that triggered the
						// modal
						var data = tr.data('whatever') // Extract info from
						// data-*
						// attributes
						console.log(data);

						$('#withdrawId').html(
								'<p class="form-control-static">'
										+ data[0].withdrawId + '</p>');
						$('#userId').html(
								'<p class="form-control-static">'
										+ data[0].userId + '</p>');
						$('#userPhone').html(
								'<p class="form-control-static">'
										+ data[1].areaCode + data[1].userPhone
										+ '</p>');
						$('#userName').html(
								'<p class="form-control-static">'
										+ data[1].userName + '</p>');
						$('#userEmail').html(
								'<p class="form-control-static">'
										+ data[0].userEmail + '</p>');
						$('#quantity').html(
								'<p class="form-control-static">'
										+ data[0].quantity + '</p>');
						$('#goldpay').html(
								'<p class="form-control-static">'
										+ data[0].goldpay + '</p>');
						$('#fee').html(
								'<p class="form-control-static">' + data[0].fee
										+ '</p>');
						$('#applyTime').html(
								'<p class="form-control-static">'
										+ timeDate(data[0].applyTime) + '</p>');
						$('#handleResult')
								.html(
										'<p class="form-control-static">'
												+ showHandleResult(data[0].handleResult)
												+ '</p>');
						$('#operation')
								.html(
										'<p class="form-control-static">'
												+ (data[0].handleResult == 1 ? ('<button type="button" class="btn btn-success" onclick="finishWithdraw('
														+ "'"
														+ data[0].withdrawId
														+ "'"
														+ ')">完成</button>'
														+ ' '
														+ '<button type="button" class="btn btn-warning" onclick="cancelWithdraw('
														+ "'"
														+ data[0].withdrawId
														+ "'" + ')">取消</button>')
														: '') + '</p>');
						$('#handleAdmin')
								.html(
										'<p class="form-control-static">'
												+ (data[0].handleAdmin == null ? ''
														: data[0].handleAdmin)
												+ '</p>');
						$('#handleTime').html(
								'<p class="form-control-static">'
										+ (data[0].handleTime == null ? ''
												: timeDate(data[0].handleTime))
										+ '</p>');
//						$('#goldTransferA').html(
//								'<p class="form-control-static">'
//										+ (data[0].goldTransferA == null ? ''
//												: data[0].goldTransferA)
//										+ '</p>');
//						$('#feeTransferA').html(
//								'<p class="form-control-static">'
//										+ (data[0].feeTransferA == null ? ''
//												: data[0].feeTransferA)
//										+ '</p>');
//						$('#goldTransferB').html(
//								'<p class="form-control-static">'
//										+ (data[0].goldTransferB == null ? ''
//												: data[0].goldTransferB)
//										+ '</p>');
//						$('#feeTransferB').html(
//								'<p class="form-control-static">'
//										+ (data[0].feeTransferB == null ? ''
//												: data[0].feeTransferB)
//										+ '</p>');

					})

	var start = {
		elem : '#start',
		format : 'YYYY-MM-DD',
		min : '1970-01-01', // 设定最小日期
		max : laydate.now(), // 最大日期为当前日期
		// istime : true,
		istoday : false,
		choose : function(datas) {
			end.min = datas; // 开始日选好后，重置结束日的最小日期
			// end.start = datas // 将结束日的初始值设定为开始日
		}
	};
	var end = {
		elem : '#end',
		format : 'YYYY-MM-DD',
		min : '1970-01-01', // 设定最小日期
		max : laydate.now(), // 最大日期为当前日期
		// istime : true,
		istoday : false,
		choose : function(datas) {
			start.max = datas; // 结束日选好后，重置开始日的最大日期
		}
	};
	laydate(start);
	laydate(end);

	var userPhone, userName, startTime = '', endTime = '', handleResult = '';
	searchWithdraw(1);

});

function searchWithdraw(page) {
	console.log("searchWithdraw:page=" + page);
	form = document.getElementById("searchWithdraw");
	userPhone = form.userPhone.value;
	console.log("userPhone=" + userPhone);
	userName = form.userName.value;
	startTime = form.startTime.value;
	endTime = form.endTime.value;
	handleResult = form.handleResult.value;
	getWithdrawByPage(page, userPhone, userName, startTime, endTime,
			handleResult);
}

function getWithdrawByPage(currentPage, userPhone, userName, startTime,
		endTime, handleResult) {
	form = document.getElementById("searchWithdraw");
	form.userPhone.value = userPhone;
	form.userName.value = userName;
	form.startTime.value = startTime;
	form.endTime.value = endTime;
	form.handleResult.value = handleResult;
	var data = {
		currentPage : currentPage,
		userPhone : userPhone,
		userName : userName,
		startTime : startTime,
		endTime : endTime,
		handleResult : handleResult
	};

	$
			.ajax({
				type : "POST",
				url : "/crm/getWithdrawByPage",
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
							html += '<tr>'
							// + '<td>'
							// + data.rows[i][0].withdrawId
							// + '</td>'
							// + '<td>'
							// + data.rows[i][0].userId
							// + '</td>'
							+ '<td>'
									+ data.rows[i][1].areaCode
									+ '</td>'
									+ '<td>'
									+ data.rows[i][1].userPhone
									+ '</td>'
									+ '<td>'
									+ data.rows[i][1].userName
									+ '</td>'
									// + '<td>'
									// + data.rows[i][0].userEmail
									// + '</td>'
									+ '<td>'
									+ data.rows[i][0].quantity
									+ '</td>'
									+ '<td>'
									+ data.rows[i][0].goldpay
									+ '</td>'
									+ '<td>'
									+ data.rows[i][0].fee
									+ '</td>'
									+ '<td>'
									+ timeDate(data.rows[i][0].applyTime)
									+ '</td>'
									+ '<td>'
									+ showHandleResult(data.rows[i][0].handleResult)
									+ '</td>'
									+ '<td>'
									+ '<a href="" data-backdrop="static" data-toggle="modal" data-target="#withdrawDetailModal" data-whatever='
									 + JSON.stringify(data.rows[i])
									+ '>详情</a> ' + '</td>'

									// + '<td>'
									// + (data.rows[i][0].handleAdmin == null ?
									// ""
									// : data.rows[i][0].handleAdmin)
									// + '</td>'
									// + '<td>'
									// + (data.rows[i][0].handleTime == null ?
									// ""
									// : timeDate(data.rows[i][0].handleTime))
									// + '</td>'
									// + '<td>'
									// + (data.rows[i][0].goldTransferA == null
									// ? ""
									// : data.rows[i][0].goldTransferA)
									// + '</td>'
									// + '<td>'
									// + (data.rows[i][0].feeTransferA == null ?
									// ""
									// : data.rows[i][0].feeTransferA)
									// + '</td>'
									// + '<td>'
									// + (data.rows[i][0].goldTransferB == null
									// ? ""
									// : data.rows[i][0].goldTransferB)
									// + '</td>'
									// + '<td>'
									// + (data.rows[i][0].feeTransferB == null ?
									// ""
									// : data.rows[i][0].feeTransferB) + '</td>'
									+ '</tr>'
						}
						$('#withdraw tbody').html(html);
						if (data.currentPage == 1) {
							paginator(data.currentPage, data.pageTotal);
						}
						$('#total').html("共 " + data.total + " 条记录");
						page = data.currentPage;
					}
				},
				error : function(xhr, err) {
					alert("未知错误");
					console.log(err);
				}
			});
}

function showHandleResult(handleResult) {
	switch (handleResult) {
	case WITHDRAW_RESULT_DEFAULT:
		return "申请处理中";
	case WITHDRAW_RESULT_APPLY_SUCCESS:
		return "申请成功";
	case WITHDRAW_RESULT_APPLY_FAIL:
		return "申请失败";
	case WITHDRAW_RESULT_FINISHT:
		return "交易完成";
	case WITHDRAW_RESULT_CANCEL:
		return "交易取消";
	}
}

function finishWithdraw(withdrawId) {
	var data = {
		withdrawId : withdrawId
	};
	$.ajax({
		type : "POST",
		url : "/crm/finishWithdraw",
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(data),
		success : function(data) {
			console.log(data);
			if (data.retCode == "00002") {
				location.href = loginUrl;
			} else if (data.retCode == "00000") {
				console.log("success");
				alert("操作成功");
				searchWithdraw(page);
			} else {
				alert("操作失败");
				searchWithdraw(page);
			}
		},
		error : function(xhr, err) {
			alert("未知错误");
			console.log(err);
		}
	});
}

function cancelWithdraw(withdrawId) {
	var data = {
		withdrawId : withdrawId
	};
	$.ajax({
		type : "POST",
		url : "/crm/cancelWithdraw",
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(data),
		success : function(data) {
			console.log(data);
			if (data.retCode == "00002") {
				location.href = loginUrl;
			} else if (data.retCode == "00000") {
				console.log("success");
				alert("操作成功");
				searchWithdraw(page);
			} else {
				alert("操作失败");
				searchWithdraw(page);
			}
		},
		error : function(xhr, err) {
			alert("未知错误");
			console.log(err);
		}
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
			searchWithdraw(page);
		}
	}
	// 分页控件
	$('#paginator').bootstrapPaginator(options);
}

var WITHDRAW_RESULT_DEFAULT = 0;
var WITHDRAW_RESULT_APPLY_SUCCESS = 1;
var WITHDRAW_RESULT_APPLY_FAIL = 2;
var WITHDRAW_RESULT_FINISHT = 3;
var WITHDRAW_RESULT_CANCEL = 4;
