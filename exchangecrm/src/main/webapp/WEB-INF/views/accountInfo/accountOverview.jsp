<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Exanytime</title>
		
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-table.css" />' />
		<link rel="stylesheet" href='<c:url value="/resources/bootstrap/css/bootstrap-paginator.min.css" />' />
		<link rel="stylesheet" href="<c:url value="/resources/css/common.css" />" />
	
	</head>
	
	<body>
		<%@ include file="../common/header.jsp"%>
		
		<div class="container">
			<!-- -->
			<div class="row">

        		<div class="" style="float: right;">
        			<form id="searchForm" action="<c:url value='/account/getTotalAssetsInfoByPage' />" method="POST">
        				<ul class="formbar">
        					<li>
        						<input type="text" value="${model.userPhone }" class="form-control" placeholder="手机号" name="userPhone" size="8">
        					</li>
        					<li>
        						<input type="text" value="${model.userName }" class="form-control" placeholder="用户名" name="userName" size="8">
        					</li>
        					<li>
        						<select name="isFrozen" class="form-control">
        							<option value=3>账号状态</option>
        							<option value=0>已冻结</option>
        							<option value=1>未冻结</option>
        						</select>
        					</li>
        					<li>
        						<input type="text" value="${model.lowerLimit }" class="form-control" placeholder="下限" name="lowerLimit" size="8">
        					</li>
        					<li>
        						<input type="text" value="${model.upperLimit }" class="form-control" placeholder="上限" name="upperLimit" size="8">
        					</li>

        					<li>
		        				<div style="display:none">
									<input name="pageBean.pageSize" value="${model.pageBean.pageSize}">
									<input class="currentPage" name="pageBean.currentPage" value="${model.pageBean.currentPage}">
									<input name="pageBean.pageTotal" value="${model.pageBean.pageTotal}">
								</div>
        					</li>
        					
        					<li >
        						<button type="submit" class="btn btn-primary">搜索</button>
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
							<th>用户编号</th>
							<th>手机号</th>
							<th>用户名</th>
							<th>用户类型</th>
							<th>是否冻结</th>
							<th>总资产（USD）</th>
							<th>更新时间（UTC）</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty model.pageBean.rows }">
							<c:forEach var="userInfo" items="${model.pageBean.rows}">
								<tr>
									<td>${userInfo.userId }</td>
									<td>${userInfo.userPhone }</td>
									<td>${userInfo.userName }</td>
									<td>
										<c:if test="${userInfo.userType eq 1}"><font color="red">系统用户</font></c:if>
						        		<c:if test="${userInfo.userType eq 0}">普通用户</c:if>
									</td>
									<td>
										<c:if test="${userInfo.userAvailable eq 1}">未冻结</c:if>
						        		<c:if test="${userInfo.userAvailable eq 0}">已冻结</c:if>
									</td>
									<td>${userInfo.userTotalAssets }</td>
									<td><fmt:formatDate value="${userInfo.updateAt }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
									<td>
										<c:if test="${userInfo.userAvailable eq 1}">
											<a href="#" onclick="userFreeze(this,0)">冻结用户</a>
										</c:if>
										<c:if test="${userInfo.userAvailable eq 0}">
											<a href="#" onclick="userFreeze(this,1)"><font color="red">解冻用户</font></a>
										</c:if>
									</td>
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
			
			function userFreeze(obj,operate){
				
				if(operate == 0 ){
					var r = confirm("确定冻结该用户么？");
					if(r != true){
						return ;
					}
				}else{
					var r = confirm("确定要解除冻结该用户么？");
					if(r != true){
						return ;
					}
				}

				var tds=$(obj).parent().parent().find('td');
				var userId = tds.eq(0).text();
				
				var userFreezeUrl = "<c:url value='/account/userFreeze' />";
				location.href=userFreezeUrl+'?userId='+userId+'&operate='+operate;
			}
			
		</script>
		<%@ include file="../common/footer.jsp"%>
	</body>
</html>
