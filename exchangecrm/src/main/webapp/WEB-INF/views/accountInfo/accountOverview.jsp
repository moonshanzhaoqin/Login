<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>用户账户信息列表</title>
		
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-table.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-paginator.min.css" />' />
		<link rel="stylesheet" href="<c:url value="/resources/css/common.css" />" />
	
	</head>
	
	<body>
		
		<div class="container">
			
			<!-- -->
			<div class="row well">
				well
			</div><!--row 结束-->
			
			
			<!-- -->
			<div class="row">

        		<div class="" style="float: right;">
        			<form id="searchForm" action="<c:url value='/account/getTotalAssetsInfoByPage' />" method="POST">
        				<ul class="formbar">
        					<li>
        						<input type="text" class="form-control" placeholder="userPhone" name="userPhone" size="8">
        					</li>
        					<li>
        						<input type="text" class="form-control" placeholder="userName" name="userName" size="8">
        					</li>
        					<li>
        						<select name="isFrozen" class="form-control">
        							<option value=3>账号状态</option>
        							<option value=0>已冻结</option>
        							<option value=1>未冻结</option>
        						</select>
        					</li>
        					<li>
        						<input type="text" class="form-control" placeholder="upperLimit" name="upperLimit" size="8">
        					</li>
        					<li>
        						<input type="text" class="form-control" placeholder="lowerLimit" name="lowerLimit" size="8">
        					</li>

        					<li>
		        				<div style="display:none">
									<input name="pageBean.pageSize" value="${model.pageBean.pageSize}">
									<input class="currentPage" name="pageBean.currentPage" value="${model.pageBean.currentPage}">
									<input name="pageBean.pageTotal" value="${model.pageBean.pageTotal}">
								</div>
        					</li>
        					
        					<li >
        						<button type="submit" class="btn btn-default">搜索</button>
        					</li>
        				</ul>
        			</form>
        		</div>
			</div><!--row 结束-->
			
			<!-- -->
			<div class="row">
				<table class = "table table-striped table-bordered">
					<thead>
						<tr>
							<th>编号</th>
							<th>手机号</th>
							<th>用户名</th>
							<th>用户类型</th>
							<th>是否冻结</th>
							<th>总资产</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty model.pageBean.rows }">
							<c:forEach var="userInfo" items="${model.pageBean.rows}">
								<tr>
									<td>${userInfo.userId }</td>
									<td>${userInfo.userPhone }</td>
									<td>${userInfo.userName }</td>
									<td>${userInfo.userType }</td>
									<td>${userInfo.userAvailable }</td>
									<td>${userInfo.userTotalAssets }</td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
				<!--分页插件-->
				<div id="paginator"></div>
			</div><!--row 结束-->
		</div>
		
		
		<!--引入Js文件-->
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap-table.js" />" ></script>
		<script type="text/javascript" src="<c:url value="/resources/bootstrap/js/bootstrap-paginator.min.js" />" ></script>
		
		
		<script type="text/javascript">
			$(function(){
				var currentPage = parseInt('${model.pageBean.currentPage}');
				var totalPage = parseInt('${model.pageBean.pageTotal}');
				var pageSize = parseInt('${model.pageBean.pageSize}');
				
				var options = {
			        currentPage: currentPage,
			        totalPages: totalPage,
			        size:'normal',
			        alignment:'right',
			        numberOfPages:pageSize,
			        itemTexts: function (type, page, current) {
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
			                return ""+page;
			            }
			        },
			        itemContainerClass: function (type, page, current) {
			            return (page === current) ? "active" : "pointer-cursor";
			        },
			        onPageClicked:function(event, originalEvent, type,page){
			        	 $(".currentPage").val(page);
			        	 $("#searchForm").submit();
			        }
			    }
				//分页控件
				$('#paginator').bootstrapPaginator(options);	
			});
		</script>
	</body>
</html>
