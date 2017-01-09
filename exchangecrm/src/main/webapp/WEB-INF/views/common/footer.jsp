<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>
	<script
		src="//cdn.bootcss.com/bootstrap-validator/0.5.3/js/bootstrapValidator.min.js"></script>
	<script>
		$(function() {
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
							$('#myModal').modal('hide')
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
	</script>
</body>
</html>