<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
	<meta charset="utf-8" />
	<title>Insert title here</title>
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
					<a class="navbar-brand" href="#">Anytime Exchange</a>
				</div>
				
				<div>
					<ul class="nav navbar-nav navbar-left">
						<li >
							<a href="<c:url value='/account/getTotalAssetsInfo' />">首页</a>
						</li>
						
						<li >   
							<a href="<c:url value='/userAssets' />">用户资产</a>
						</li>
<li >   
							<a href="<c:url value='/alarm/addAlarmConfig' />">预警</a>
						</li>
						<li >   
							<a href="<c:url value='/currency' />">币种</a>
						</li>
						<li >   
							<a href="<c:url value='/config' />">配置</a>
						</li>
						<li>
							<a href="<c:url value='/setting' />">设置</a>
						</li>
						
					</ul>
				</div>
				
				<div>
					<ul class="nav navbar-nav navbar-right">
						<li>
							<a href="#">${sessionScope.user.userName }</a>
						</li>

						<li>
							<a href="<c:url value='/exit' />">退出</a>
						</li>
					</ul>
				</div>
				
			</div>
		</nav>
	</body>
</html>