<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored ="false" %>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Exanytime</title>
		<link rel='icon'  href='<c:url value="/resources/img/ex_28x28.ico" />' type='image/x-ico' />
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
        						<input type="text" value="${model.userPhone }" class="form-control" placeholder="手机号" name="userPhone" size="12">
        					</li>
        					<li>
        						<input type="text" value="${model.userName }" class="form-control" placeholder="用户名" name="userName" size="12">
        					</li>
        					<li>
        						<select name="userAvailable" class="form-control">
									<c:if test="${model.userAvailable == '0'}">
										<option value=3>账号状态</option>
	        							<option value=0 selected>已冻结</option>
	        							<option value=1>未冻结</option>
									</c:if>
									<c:if test="${model.userAvailable == '1'}">
										<option value=3>账号状态</option>
	        							<option value=0>已冻结</option>
	        							<option value=1 selected>未冻结</option>
									</c:if>
									<c:if test="${(model.userAvailable == '3') || (model.userAvailable != '0' && model.userAvailable != '1')}">
										<option value=3 selected>账号状态</option>
	        							<option value=0>已冻结</option>
	        							<option value=1>未冻结</option>
									</c:if>
        						</select>
        					</li>
        					<li>
        						<select name="loginAvailable" class="form-control">
									<c:if test="${model.loginAvailable == '0'}">
										<option value=3>登录冻结</option>
	        							<option value=0 selected>已冻结</option>
	        							<option value=1>未冻结</option>
									</c:if>
									<c:if test="${model.loginAvailable == '1'}">
										<option value=3>登录冻结</option>
	        							<option value=0>已冻结</option>
	        							<option value=1 selected>未冻结</option>
									</c:if>
									<c:if test="${(model.loginAvailable == '3') || (model.loginAvailable != '0' && model.loginAvailable != '1')}">
										<option value=3 selected>登录冻结</option>
	        							<option value=0>已冻结</option>
	        							<option value=1>未冻结</option>
									</c:if>
        						</select>
        					</li>
        					<li>
        						<select name="payAvailable" class="form-control">
									<c:if test="${model.payAvailable == '0'}">
										<option value=3>支付冻结</option>
	        							<option value=0 selected>已冻结</option>
	        							<option value=1>未冻结</option>
									</c:if>
									<c:if test="${model.payAvailable == '1'}">
										<option value=3>支付冻结</option>
	        							<option value=0>已冻结</option>
	        							<option value=1 selected>未冻结</option>
									</c:if>
									<c:if test="${(model.payAvailable == '3') || (model.payAvailable != '0' && model.payAvailable != '1')}">
										<option value=3 selected>支付冻结</option>
	        							<option value=0>已冻结</option>
	        							<option value=1>未冻结</option>
									</c:if>
        						</select>
        					</li>
        					<li>
        						<input type="text" value="${model.lowerLimit }" class="form-control" placeholder="总资产>=(USD)" name="lowerLimit" size="12">
        					</li>
        					<li>
        						<input type="text" value="${model.upperLimit }" class="form-control" placeholder="总资产<=(USD)" name="upperLimit" size="12">
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
        					
        					<li >
        						<c:if test="${updateFlag == 0 }">
        							<button type="button" class="btn btn-primary" id="updateImmediately">立即更新</button>
        						</c:if>
        						<c:if test="${updateFlag == 1 }">
        							<button type="button" class="btn btn-primary" disabled>更新中...</button>
        						</c:if>
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
							<th>国家码</th>
							<th>手机号</th>
							<th>用户名</th>
							<th>用户类型</th>
							<th>账户冻结</th>
							<th>登录冻结</th>
							<th>支付冻结</th>
							<th>总资产（USD）</th>
							<th>更新时间（UTC）</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty model.pageBean.rows }">
							<c:forEach var="userInfo" items="${model.pageBean.rows}">
								<tr id=${userInfo.userId }>
									<td>${userInfo.areaCode }</td>
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
									<td>
										<c:if test="${userInfo.loginAvailable eq 1}">未冻结</c:if>
						        		<c:if test="${userInfo.loginAvailable eq 0}">已冻结</c:if>
									</td>
									<td>
										<c:if test="${userInfo.payAvailable eq 1}">未冻结</c:if>
						        		<c:if test="${userInfo.payAvailable eq 0}">已冻结</c:if>
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

				var tds=$(obj).parent().parent();
				var userId = tds.attr('id');
				
				var userFreezeUrl = "<c:url value='/account/userFreeze' />";
				location.href=userFreezeUrl+'?userId='+userId+'&operate='+operate;
			}
			
			$("#updateImmediately").click(function(){
// 				alert("点击立即更新按钮");
				$("#updateImmediately").attr("disabled", true); 
				$("#updateImmediately").text("更新中...");
				
				location.href="<c:url value='/account/updateImmediately' />";
				
			});
			
			
		</script>
		<%@ include file="../common/footer.jsp"%>
	</body>
</html>
