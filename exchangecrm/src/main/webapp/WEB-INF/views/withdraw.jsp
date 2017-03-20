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
	href='<c:url value="/resources/bootstrap/css/bootstrap-paginator.min.css" />' />
<link rel="stylesheet"
	href="<c:url value="/resources/css/common.css" />" />
</head>
<body>
	<%@ include file="common/header.jsp"%>
	<div class="container">
		<div class="row">

			<form class="form-inline" id="searchWithdraw">
				<div class="form-group">
					<label class="sr-only" for="userPhone">userPhone</label> <input
						type="text" class="form-control" name="userPhone" placeholder="手机号">
				</div>
				<div class="form-group">
					<label class="sr-only" for="reviewStatus">reviewStatus</label> <select
						class="form-control" name="reviewStatus">
						<option value="">审批状态</option>
						<option value="0">未审批</option>
						<option value="1">未通过</option>
						<option value="2">通过</option>
					</select>
				</div>
				<div class="form-group">
					<label class="sr-only" for="goldpayRemit">goldpayRemit</label> <select
						class="form-control" name="goldpayRemit">
						<option value="">Goldpay划账结果</option>
						<option value="0">未执行</option>
						<option value="1">失败</option>
						<option value="2">成功</option>
					</select>
				</div>
				<button type="button" class="btn btn-primary"
					onclick="searchWithdraw(1)">搜索</button>
			</form>
		</div>
		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="withdraw">
				<thead>
					<tr>
						<th>用户ID</th>
						<th>手机号</th>
						<th>交易号</th>
						<th>审批状态</th>
						<th>Goldpay划账结果</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
		</div>
	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">用户信息：</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label class="col-sm-3 control-label">用户ID：</label>
							<div class="col-sm-9" id="userId"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">用户名：</label>
							<div class="col-sm-9" id="userName"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">手机号：</label>
							<div class="col-sm-9" id="userPhone"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">Goldpay账号：</label>
							<div class="col-sm-9" id="goldpayAcount"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">Goldpay用户名：</label>
							<div class="col-sm-9" id="goldpayName"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">交易ID：</label>
							<div class="col-sm-9" id="transferId"></div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">交易数量：</label>
							<div class="col-sm-9" id="transferAmount"></div>
						</div>

						<div class="form-group">
							<label class="col-sm-3 control-label">交易创建时间：</label>
							<div class="col-sm-9" id="createTime"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">审核状态：</label>
							<div class="col-sm-9" id="reviewStatus"></div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">Goldpay划账：</label>
							<div class="col-sm-9" id="goldpayRemit"></div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
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
			//页面初始化，加载数据
			getWithdrawList(1, "", "", "");
			$('#myModal').on('show.bs.modal', function(e) {
				// do something...
				var tr = $(e.relatedTarget) // Button that triggered the modal
				var withdrawId = tr.data('whatever') // Extract info from data-* attributes
				console.log(withdrawId);
				initModel(withdrawId);
			})
			$('#myModal').on('hidden.bs.modal', function(e) {
				// 				console.log($("#paginator .active a").html());
				// 				console.log($("#paginator").data("currentPage"));
				searchWithdraw($("#paginator .active a").html());
			})
		});

		function searchWithdraw(page) {
			console.log("searchWithdraw:page=" + page);
			form = document.getElementById("searchWithdraw");
			getWithdrawList(page, form.userId.value, form.reviewStatus.value,
					form.goldpayRemit.value);
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
					searchWithdraw(page)
				}
			}
			//分页控件
			$('#paginator').bootstrapPaginator(options);
		}

		// 模态框初始化
		function initModel(withdrawId) {
			data = {
				withdrawId : withdrawId
			};
			$
					.ajax({
						type : "post",
						url : "/crm/getWithdrawDetail",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(data),
						success : function(data) {
							$('#userId').html(
									'<p class="form-control-static">'
											+ data.userId + '</p>');
							$('#userName').html(
									'<p class="form-control-static">'
											+ data.userName + '</p>');
							$('#userPhone').html(
									'<p class="form-control-static">'
											+ data.areaCode + data.userPhone
											+ '</p>');
							$('#goldpayAcount').html(
									'<p class="form-control-static">'
											+ data.goldpayAcount + '</p>');
							$('#goldpayName').html(
									'<p class="form-control-static">'
											+ data.goldpayName + '</p>');
							$('#transferId').html(
									'<p class="form-control-static">'
											+ data.transferId + '</p>');
							$('#transferAmount').html(
									'<p class="form-control-static">'
											+ data.transferAmount + '</p>');
							$('#createTime').html(
									'<p class="form-control-static">'
											+ new Date(Number(data.createTime))
											+ '</p>');
							if (data.reviewStatus == 0) {
								$('#reviewStatus')
										.html(
												'<p class="form-control-static" style="color: blue">未审批</p>'
														+ '<button type="button" class="btn btn-primary"onclick="withdrawReview('
														+ withdrawId
														+ ')">审批</button>');
							} else if (data.reviewStatus == 1) {
								$('#reviewStatus')
										.html(
												'<p class="form-control-static" style="color: red">未通过</p>'
														+ '<button type="button" class="btn btn-primary"onclick="withdrawReview('
														+ withdrawId
														+ ')">审批</button>');
							} else {
								$('#reviewStatus')
										.html(
												'<p class="form-control-static" style="color: green">通过</p>');
							}

							if (data.goldpayRemit == 0) {
								if (data.reviewStatus == 2) {
									$('#goldpayRemit')
											.html(
													'<p class="form-control-static" style="color: green">未执行</p>'
															+ '<button type="button" class="btn btn-primary"onclick="goldpayRemit('
															+ withdrawId
															+ ')">执行</button>');
								} else {
									$('#goldpayRemit')
											.html(
													'<p class="form-control-static" style="color: green">未执行</p>');
								}
							} else if (data.goldpayRemit == 1) {
								$('#goldpayRemit')
										.html(
												'<p class="form-control-static" style="color: red">失败</p>'
														+ '<button type="button" class="btn btn-primary"onclick="goldpayRemit('
														+ withdrawId
														+ ')">执行</button>');
							} else {
								$('#goldpayRemit')
										.html(
												'<p class="form-control-static" style="color: green">成功</p>');
							}
						}
					})
		}

		function getWithdrawList(currentPage, userId, reviewStatus,
				goldpayRemit) {
			console.log("getWithdrawList-->currentPage=" + currentPage
					+ ", userId=" + userId + ", reviewStatus=" + reviewStatus
					+ ", goldpayRemit=" + goldpayRemit)

			data = {
				currentPage : currentPage,
				userId : userId,
				reviewStatus : reviewStatus,
				goldpayRemit : goldpayRemit
			};

			$
					.ajax({
						type : "post",
						url : "/crm/getWithdrawList",
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
									html += '<tr>'
											+ '<td>'
											+ data.rows[i].userId
											+ '</td>'
											+ '<td>'
											+ data.rows[i].areaCode+data.rows[i].userPhone
											+ '</td>'
											+ '<td>'
											+ data.rows[i].transferId
											+ '</td>'
											+ (data.rows[i].reviewStatus == 0 ? '<td style="color:blue">未审批</td>'
													: (data.rows[i].reviewStatus == 1 ? '<td style="color:red">未通过</td>'
															: '<td style="color:green">通过</td>'))
											+ (data.rows[i].goldpayRemit == 0 ? '<td style="color:blue">未执行</td>'
													: (data.rows[i].goldpayRemit == 1 ? '<td style="color:red">失败</td>'
															: '<td style="color:green">成功</td>'))
											+ '<td><a href="javascript:void(0)" data-toggle="modal" data-target="#myModal" data-whatever='
											+ "'" + data.rows[i].withdrawId
											+ "'" + '>操作</a></td>' + '</tr>'
								}
								$('#withdraw tbody').html(html);
								paginator(data.currentPage, data.pageTotal);
							}
						},
						error : function(xhr, err) {
							console.log("error");
							console.log(err);
						},
						async : false
					});

		}

		//审批
		function withdrawReview(withdrawId) {
			data = {
				withdrawId : withdrawId
			};
			$.ajax({
				type : "post",
				url : "/crm/withdrawReview",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						console.log("success");
						initModel(withdrawId)
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("withdrawReview" + data.message);
						alert(data.message);
						initModel(withdrawId)
					}
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
					alert("something is wrong!");
				},
				async : false
			});

		}
		//划账
		function goldpayRemit(withdrawId) {
			data = {
				withdrawId : withdrawId
			};
			$.ajax({
				type : "post",
				url : "/crm/goldpayRemit",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						console.log("success");
						initModel(withdrawId)
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						console.log("goldpayRemit" + data.message);
						alert(data.message);
						initModel(withdrawId)
					}
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
					alert("something is wrong!");
				},
				async : false
			});
			initModel(withdrawId)
		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</body>
</html>