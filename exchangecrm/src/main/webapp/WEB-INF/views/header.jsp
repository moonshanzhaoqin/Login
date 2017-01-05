<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
</head>
<body>
	<!--
        	作者：1119805031@qq.com
        	时间：2015-12-25
        	描述：导航栏
        -->
	<nav class="navbar navbar-default .navbar-static-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#"><img alt="Anytime Exchange"
					src='<c:url value="/resources/img/ae_logo.png"/>' height="32px"></a>
			</div>

			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-left">
					<li><a href="<c:url value='/account/getTotalAssetsInfo' />">首页</a>
					</li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">预警 <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="<c:url value='/alarm/getAlarmConfigList' />">预警信息</a>
							</li>
							<li><a href="<c:url value='/alarm/addAlarmConfig' />">预警设置</a>
							</li>
							<li class="divider"></li>
							<li><a href="<c:url value='/alarm/getSupervisorList' />">监督人信息</a>
							</li>
							<li><a href="<c:url value='/alarm/addSupervisor' />">添加监督人</a>
							</li>
						</ul></li>
					<li><a href="<c:url value='/currency' />">币种</a></li>
					<li><a href="<c:url value='/config' />">配置</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><span class="glyphicon glyphicon-user"></span>${sessionScope.adminName }
							<span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#" data-toggle="modal" data-target="#passwordModal">修改密码</a></li>
						</ul></li>
					<li><a href="<c:url value='/exit' />">退出</a></li>
				</ul>
			</div>

		</div>
	</nav>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="passwordModal" tabindex="-1" role="dialog"
		aria-labelledby="passwordModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="passwordModalLabel">修改密码</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="modifyPassword">
						<div class="form-group">
							<label for="oldPassword" class="col-sm-2 control-label">原密码</label>
							<div class="col-sm-10">
								<input type="password" class="form-control" name="oldPassword"
									placeholder="Old Password">
							</div>
						</div>
						<div class="form-group">
							<label for="newPassword" class="col-sm-2 control-label ">新密码</label>
							<div class="col-sm-10">
								<input type="password" class="form-control " name="newPassword"
									placeholder="New Password">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="modifyPassword()">提交更改</button>
				</div>
			</div>
		</div>
	</div>
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