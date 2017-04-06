<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Exanytime</title>
<link rel='icon' href='<c:url value="/resources/img/ex_28x28.ico" />'
	type='image/x-ico' />
<link rel="stylesheet"
	href='<c:url value="/resources/bootstrap/css/bootstrap.min.css" />' />
<link rel="stylesheet"
	href="<c:url value="/resources/css/common.css" />" />
</head>
<body>
	<%@ include file="common/header.jsp"%>

	<div class="container">

		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="badAccount">
				<thead>
					<tr>
						<th>用户ID</th>
						<th>货币</th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>

	</div>

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript">
		$(function() {
			getBadAccountByPage(1);
		})
		function getBadAccountByPage(currentPage) {
			data = {
				currentPage : currentPage
			};
			$.ajax({
				type : "post",
				url : "/crm/getBadAccountByPage",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("success");
						var html = "";
						for ( var i in data.rows) {
							html += '<tr>' + '<td>' + data.rows[i].userId
									+ '</td>' + '<td>' + data.rows[i].currency
									+ '</td>' + '<td>' + timeDate(data.rows[i].startTime)
									+ '</td>' + '<td>' + timeDate(data.rows[i].endTime)
									+ '</td>' + '<td>'
									+ '<a href="javascript:void(0)">详情</a>'
									+ '</td>' + '</tr>'
						}
						$('#badAccount tbody').html(html);
						if (data.currentPage == 1) {
							paginator(data.currentPage, data.pageTotal);
						}
					}
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
				},
				async : false
			});

		}
		//分页
		function paginator(currentPage, pageTotal) {
			console.log("currentPage=" + currentPage + ",pageTotal="
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
				itemContainerClass : function(type, page, current) {
					return (page === current) ? "active" : "pointer-cursor";
				},
				onPageClicked : function(event, originalEvent, type, page) {
					getBadAccountByPage(page)
				}
			}
			//分页控件
			$('#paginator').bootstrapPaginator(options);
		}

		//时间戳变格式化
		function timeDate(time) {
			var date = new Date();
			date.setTime(time);
			return date.toLocaleString();
		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>