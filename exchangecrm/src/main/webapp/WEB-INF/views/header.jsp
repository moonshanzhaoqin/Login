<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
<style type="text/css">
.navbar-nav {
	font-size: larger;
	font-weight: bolder;
}
</style>
</head>
<body>
	<!--
        	作者：1119805031@qq.com
        	时间：2015-12-25
        	描述：导航栏
        -->
	<nav class="navbar navbar-default" role="navigation">
		<div class="container">
			<div class="navbar-header">
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
						data-toggle="dropdown">${sessionScope.adminName } <span
							class="caret"></span></a>
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