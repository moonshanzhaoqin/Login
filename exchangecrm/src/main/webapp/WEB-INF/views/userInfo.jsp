<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp"%>
<body>
	<div class="container">
		<div class="row">
			<form class="form-inline pull-right" id="searchUserInfo">
				<div class="form-group">
					<label class="sr-only" for="userPhone">userPhone</label> <input
						type="text" class="form-control" name="userPhone"
						placeholder="手机号">
				</div>
				<div class="form-group">
					<label class="sr-only" for="userName">userName</label> <input
						type="text" class="form-control" name="userName" placeholder="用户名">
				</div>
				<button type="button" class="btn btn-primary "
					onclick="searchUserInfo(1)">搜索</button>
				<button type="button" class="btn btn-primary" id="updateImmediately" onclick="updateUserInfo()"> 立即更新</button>
			</form>
		</div>

		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="userInfo">
				<thead>
					<tr>
						<th>国家码</th>
						<th>手机号</th>
						<th>用户名</th>
						<th>登录时间（GMT+8）</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
		</div>
	</div>

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap-paginator.min.js" />"></script>
	<script>
		$(function() {
			var userPhone,userName;
			searchUserInfo(1);
		});

		function searchUserInfo(page) {
			console.log("searchUserInfo:page=" + page);
			form = document.getElementById("searchUserInfo");
			userPhone = form.userPhone.value;
			console.log("userPhone="+userPhone);
			userName = form.userName.value;
			getUserInfoByPage(page, userPhone, userName);
		}

		function getUserInfoByPage(currentPage, userPhone1, userName1) {
			form = document.getElementById("searchUserInfo");
			form.userPhone.value = userPhone1;
			form.userName.value = userName1;
			userPhone=userPhone1;
			userName=userName1;
			var data = {
				currentPage : currentPage,
				userPhone : userPhone,
				userName : userName
			};

			$.ajax({
				type : "POST",
				url : "/crm/getUserInfoByPage",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					console.log(data);
					if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("success");
						var html = "";
						for ( var i in data.rows) {
							html += '<tr id="'+data.rows[i].userId+'">'
									+ '<td>' + data.rows[i].areaCode + '</td>'
									+ '<td>' + data.rows[i].userPhone + '</td>'
									+ '<td>' + data.rows[i].userName + '</td>'
									+ '<td>' + timeDate(data.rows[i].loginTime)+ '</td>' 
									+ '</tr>'
						}
						$('#userInfo tbody').html(html);
						if (data.currentPage == 1) {
							paginator(data.currentPage, data.pageTotal);
						}
					}
				},
				error : function(xhr, err) {
					alert("未知错误");
					console.log(err);
				}
			});
		}
        /* 立即更新 */
		function updateUserInfo() {
			$("#updateImmediately").attr("disabled", true);
			$("#updateImmediately").text("更新中...");
			$.ajax({
				type : "GET",
				url : "/crm/updateUserInfo",
				contentType : "application/json; charset=utf-8",
				success : function(data) {
					$("#updateImmediately").attr("disabled", false);
		            $("#updateImmediately").text("立即更新");
		            getUserInfoByPage(1,"","")
				},
				error : function(xhr, err) {
					alert("未知错误");
					console.log(err);
					$("#updateImmediately").attr("disabled", false);
                    $("#updateImmediately").text("立即更新");
				}
			});

		}

		//分页
		function paginator(currentPage, pageTotal) {
			console.log("paginator:currentPage=" + currentPage + ",pageTotal="
					+ pageTotal);
			var options = {
				currentPage : currentPage,//当前页
				totalPages : pageTotal,//总页数
				size : 'normal',
				alignment : 'right',
				numberOfPages : 10,//显示页数
				itemTexts : function(type, page, current) {
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
						return "" + page;
					}
				},
				onPageClicked : function(event, originalEvent, type, page) {
					getUserInfoByPage(page, userPhone, userName);
				}
			}
			//分页控件
			$('#paginator').bootstrapPaginator(options);
		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</body>
</html>