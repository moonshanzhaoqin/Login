/**
 * 
 */
$(function() {
	var start = {
		elem : '#start',
		format : 'YYYY-MM-DD',
		min : '1970-01-01', // 设定最小日期
		max :  laydate.now(), // 最大日期为当前日期
//		istime : true,
		istoday : false,
		choose : function(datas) {
			end.min = datas; // 开始日选好后，重置结束日的最小日期
//			end.start = datas // 将结束日的初始值设定为开始日
		}
	};
	var end = {
		elem : '#end',
		format : 'YYYY-MM-DD',
		min : '1970-01-01', // 设定最小日期
		max :  laydate.now(), // 最大日期为当前日期
//		istime : true,
		istoday : false,
		choose : function(datas) {
			start.max = datas; // 结束日选好后，重置开始日的最大日期
		}
	};
	laydate(start);
	laydate(end);

	var userPhone, userName, startTime = '', endTime = '';
	searchUserInfo(1);
});

function searchUserInfo(page) {
	console.log("searchUserInfo:page=" + page);
	form = document.getElementById("searchUserInfo");
	userPhone = form.userPhone.value;
	console.log("userPhone=" + userPhone);
	userName = form.userName.value;
	startTime = form.startTime.value;
	endTime = form.endTime.value;
	getUserInfoByPage(page, userPhone, userName, startTime, endTime);
}

function getUserInfoByPage(currentPage, userPhone, userName) {
	form = document.getElementById("searchUserInfo");
	form.userPhone.value = userPhone;
	form.userName.value = userName;
	form.startTime.value = startTime;
	form.endTime.value = endTime;
	var data = {
		currentPage : currentPage,
		userPhone : userPhone,
		userName : userName,
		startTime : startTime,
		endTime : endTime
	};

	$.ajax({
		type : "POST",
		url : "/crm/getUserInfoByPage",
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		data : JSON.stringify(data),
		success : function(data) {
			console.log(data);
			if (data.retCode == "00002") {
				location.href = loginUrl;
			} else {
				console.log("success");
				var html = "";
				for ( var i in data.rows) {
					html += '<tr id="' + data.rows[i].userId + '">' + '<td>'
							+ data.rows[i].areaCode + '</td>' + '<td>'
							+ data.rows[i].userPhone + '</td>' + '<td>'
							+ data.rows[i].userName + '</td>' + '<td>'
							+ timeDate(data.rows[i].createTime) + '</td>'
							+ '<td>' + timeDate(data.rows[i].loginTime)
							+ '</td>' + '</tr>'
				}
				$('#userInfo tbody').html(html);
				if (data.currentPage == 1) {
					paginator(data.currentPage, data.pageTotal);
				}
				$('#total').html(data.total);
			}
		},
		error : function(xhr, err) {
			alert("未知错误");
			console.log(err);
		}
	});
}
/* 立即更新 */
function updateUserInfo() {
	$("#updateImmediately").attr("disabled", true);
	$("#updateImmediately").text("更新中...");
	$.ajax({
		type : "GET",
		url : "/crm/updateUserInfo",
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			$("#updateImmediately").attr("disabled", false);
			$("#updateImmediately").text("立即更新");
			getUserInfoByPage(1, "", "")
		},
		error : function(xhr, err) {
			alert("未知错误");
			console.log(err);
			$("#updateImmediately").attr("disabled", false);
			$("#updateImmediately").text("立即更新");
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
			getUserInfoByPage(page, userPhone, userName);
		}
	}
	// 分页控件
	$('#paginator').bootstrapPaginator(options);
}