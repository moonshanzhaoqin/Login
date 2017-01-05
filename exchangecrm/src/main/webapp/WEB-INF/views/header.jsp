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
							<li><a href="<c:url value='/setting' />">设置</a></li>
						</ul></li>
					<li><a href="<c:url value='/exit' />">退出</a></li>
				</ul>
			</div>

		</div>
	</nav>
</body>
</html>