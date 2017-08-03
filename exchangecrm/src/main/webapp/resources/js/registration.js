/**
 * 
 */
$(function() {
	var start = {
		elem : '#start',
		format : 'YYYY-MM-DD hh:mm:ss',
		min : '1970-01-01 00:00:00', // 设定最小日期
		max :  laydate.now(), // 最大日期为当前日期
		istime : true,
		istoday : false,
		choose : function(datas) {
			end.min = datas; // 开始日选好后，重置结束日的最小日期
//			end.start = datas // 将结束日的初始值设定为开始日
		}
	};
	var end = {
		elem : '#end',
		format : 'YYYY-MM-DD hh:mm:ss',
		min : '1970-01-01 00:00:00', // 设定最小日期
		max :  laydate.now(), // 最大日期为当前日期
		istime : true,
		istoday : false,
		choose : function(datas) {
			start.max = datas; // 结束日选好后，重置开始日的最大日期
		}
	};
	laydate(start);
	laydate(end);

	init();

});

/* 页面初始化 */
function init() {

	$.ajax({
		type : "post",
		url : "/crm/get24HRegistration",
		contentType : "application/json; charset=utf-8",
		dataType : 'json',
		data : {},
		success : function(data) {

			$('#24HRegistration').text(data);

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

/* 某个时间段的注册量 */
function getRegistration() {
	form = document.getElementById("getRegistration");
	var data = {
		startTime : form.startTime.value,
		endTime : form.endTime.value
	};
	$.ajax({
		type : "post",
		url : "/crm/getRegistration",
		contentType : "application/json; charset=utf-8",
		dataType : 'json',
		data : JSON.stringify(data),
		success : function(data) {

			$('#Registration').text(data);

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