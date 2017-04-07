<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<title></title>
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
					src='<c:url value="/resources/img/ex_page_logo_small.png"/>'
					height="30px"></a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-left">
					<li><a href="<c:url value='/account/getTotalAssetsDetails' />">账户汇总</a>
					<li><a href="<c:url value='/account/accountOverview' />">用户管理</a>
					<li><a href="<c:url value='/badAccount' />">坏账管理</a>
					<li><a href="<c:url value='/withdraw' />">提现管理</a>
					</li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">预警管理 <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="<c:url value='/alarm/getAlarmConfigList' />">预警设置</a>
							</li>
							<li><a href="<c:url value='/alarm/getLargeTransAlarmConfigList' />">大额预警设置</a>
							</li>
							<li><a href="<c:url value='/alarm/getBadAccountAlarmConfigList' />">坏账预警设置</a>
							</li>
							<li class="divider"></li>
							<li><a href="<c:url value='/alarm/getSupervisorList' />">预警人设置</a>
							</li>
						</ul></li>
					<li><a href="<c:url value='/currency' />">币种管理</a></li>
					<li><a href="<c:url value='/exchangeRate/getAllExchangeRates' />">Oanda汇率</a>
					<li><a href="<c:url value='/config' />">系统配置管理</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><span class="glyphicon glyphicon-user"></span>
							${sessionScope.adminName } <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#passwordModal" data-toggle="modal">修改密码</a></li>
							<li><a href="<c:url value='/exit' />"><span
									class="glyphicon glyphicon-log-out"></span> 退出</a></li>
						</ul></li>
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
	<script>
		var loginUrl = "<c:url value='/login' />";
	</script>
</body>
</html>