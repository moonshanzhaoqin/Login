<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<script
		src="//cdn.bootcss.com/bootstrap-validator/0.5.3/js/bootstrapValidator.min.js"></script>
	<script src="<c:url value="/resources/js/dateFormat.min.js" />"></script>
	<script>
		$(function() {
			$('.dropdown-toggle').dropdown();
			$('#modifyPassword').bootstrapValidator({
				message : 'This value is not valid',
				feedbackIcons : {/*输入框不同状态，显示图片的样式*/
					valid : 'glyphicon glyphicon-ok',
					invalid : 'glyphicon glyphicon-remove',
					validating : 'glyphicon glyphicon-refresh'
				},
				fields : {/*验证*/
					oldPassword : {/*键名username和input name值对应*/
						message : '密码无效',
						validators : {
							notEmpty : {/*非空提示*/
								message : '密码不能为空'
							},
							stringLength : {/*长度提示*/
								min : 6,
								max : 30,
								message : '密码必须在6到30之间'
							}
						/*最后一个没有逗号*/
						}
					},
					newPassword : {
						message : '密码无效',
						validators : {
							notEmpty : {
								message : '密码不能为空'
							},
							stringLength : {
								min : 6,
								max : 30,
								message : '密码长度必须在6到30之间'
							}
						}
					},
				}
			});
		})

		function modifyPassword() {
			var bootstrapValidator = $('#modifyPassword').data(
					'bootstrapValidator');
			bootstrapValidator.validate();
			console.log(bootstrapValidator.isValid());
			if (bootstrapValidator.isValid()) {
				form = document.getElementById("modifyPassword");
				data = {
					oldPassword : form.oldPassword.value,
					newPassword : form.newPassword.value
				}
				$.ajax({
					type : "post",
					url : "/crm/modifyPassword",
					contentType : "application/json; charset=utf-8",
					dataType : 'json',
					data : JSON.stringify(data),
					success : function(data) {
						if (data.retCode == "00000") {
							console.log("success");
							alert("修改成功");
							$('#myModal').modal('hide');
							location.href = "<c:url value='/login' />"

						} else {
							console.log(data.message);
							alert(data.message);
						}
					},
					error : function(xhr, err) {
						console.log("error");
						console.log(err);
						console.log(xhr);
					},
					async : false
				});
			}
		}
		//时间戳变格式化
		function timeDate(time) {
			var date = new Date();
			date.setTime(time+8*60*60*1000);
			return date.UTCFormat("yyyy-MM-dd hh:mm:ss");
		}

		// 对Date的扩展，将 Date 转化为指定格式的String   
		// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，   
		// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)   
		// 例子：   
		// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423   
		// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18   
		Date.prototype.UTCFormat = function(fmt)   
		{ //author: meizz   
		  var o = {   
		    "M+" : this.getUTCMonth()+1,                 //月份   
		    "d+" : this.getUTCDate(),                    //日   
		    "h+" : this.getUTCHours(),                   //小时   
		    "m+" : this.getUTCMinutes(),                 //分   
		    "s+" : this.getUTCSeconds(),                 //秒   
		    "S"  : this.getUTCMilliseconds()             //毫秒   
		  };   
		  if(/(y+)/.test(fmt))   
		    fmt=fmt.replace(RegExp.$1, (this.getUTCFullYear()+"").substr(4 - RegExp.$1.length));   
		  for(var k in o)   
		    if(new RegExp("("+ k +")").test(fmt))   
		  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
		  return fmt;   
		}  
		
		
		function onlyNum() {
			if (!(event.keyCode == 46) && !(event.keyCode == 8)
					&& !(event.keyCode == 37) && !(event.keyCode == 39))
				if (!((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105)))
					event.returnValue = false;
		}
		
	</script>
</body>
</html>