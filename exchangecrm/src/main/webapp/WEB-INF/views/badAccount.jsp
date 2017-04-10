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

	<div class="container" style="height: 100%;">

		<div class="row" style="height: 300px; overflow: auto;">
			<table class="table table-bordered table-hover table-striped"
				id="badAccount">
				<thead>
					<tr>
						<th>用户ID</th>
						<th>手机号</th>
						<th>货币</th>
						<th>sumAmount</th>
						<th>balanceBefore</th>
						<th>balanceNow</th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th>badAccountStatus</th>
						<th>操作</th>

					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
		<hr />
		<div id="detail" class="row" style="height: 450px; overflow: auto;"
			hidden>
			<table class="table table-bordered table-hover table-striped"
				id="walletSeq">
				<thead>
					<tr>
						<th>流水号</th>
						<th>货币</th>
						<th>数量</th>
						<th>交易类型</th>
						<th>交易ID</th>
						<th>时间</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">详情</h4>
				</div>
				<div class="modal-body" style="overflow: auto;">
					<!-- 					<table class="table table-bordered table-hover table-striped" -->
					<!-- 						id="transfer"> -->
					<!-- 						<thead> -->
					<!-- 							<tr> -->
					<!-- 								<th></th> -->
					<!-- 							</tr> -->
					<!-- 						</thead> -->
					<!-- 						<tbody></tbody> -->
					<!-- 					</table> -->
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap-paginator.min.js" />"></script>
	<script type="text/javascript">
		$(function() {
			getBadAccountByPage(1);

			$('#myModal').on('show.bs.modal', function(e) {
				// do something...
				var tr = $(e.relatedTarget) // Button that triggered the modal
				var badAccountId = tr.data('whatever') // Extract info from data-* attributes
				console.log(badAccountId);
				getDetailSeq(badAccountId)
			})

		})
		function getBadAccountByPage(currentPage) {
			data = {
				currentPage : currentPage
			};
			$
					.ajax({
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
									html += '<tr>' + '<td>'
											+ data.rows[i][1].userId
											+ '</td>'
											+ '<td>'
											+ data.rows[i][1].areaCode
											+ data.rows[i][1].userPhone
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].currency
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].sumAmount
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].balanceBefore
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].balanceNow
											+ '</td>'
											+ '<td>'
											+ timeDate(data.rows[i][0].startTime)
											+ '</td>'
											+ '<td>'
											+ timeDate(data.rows[i][0].endTime)
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].badAccountStatus
											+ '</td>'
											+ '<td>'
											+ '<a href="javascript:void(0)" onclick="getDetailSeq('
											+ data.rows[i][0].badAccountId
											+ ')">详情</a>' + '</td>' + '</tr>'
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

		function getDetailSeq(badAccountId) {
			data = {
				badAccountId : badAccountId
			};
			$
					.ajax({
						type : "post",
						url : "/crm/getDetailSeq",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : JSON.stringify(data),
						success : function(data) {
							if (data.retCode == "00002") {
								location.href = loginUrl;
							} else {
								console.log("success");
								var html = "";
								for ( var i in data) {
									html += '<tr>'
											+ '<td>'
											+ data[i].seqId
											+ '</td>'
											+ '<td>'
											+ data[i].currency
											+ '</td>'
											+ '<td>'
											+ data[i].amount
											+ '</td>'
											+ '<td>'
											+ transferType(data[i].transferType)
											+ '</td>'
											+ '<td>'
											+ ((data[i].transferType == "1") ? data[i].transactionId
													: ('<a href="" data-toggle="modal" data-target="#myModal">onclick='
															+ data[i].transactionId + '</a>'))
											+ '</td>' + '<td>'
											+ timeDate(data[i].createTime)
											+ '</td>' + '</tr>'
								}
								$('#walletSeq tbody').html(html);
								$('#detail').show();
							}
						},
						error : function(xhr, err) {
							console.log("error");
							console.log(err);
						},
						async : false
					});
		}

function getTransfer(transferId){
	
}


		

		function transferType(transferType) {
			switch (transferType) {
			case 0:
				return "交易";
			case 1:
				return "兑换";
			case 2:
				return "邀请";
			case 3:
				return "系统退款";
			case 4:
				return "金沛提现";
			case 5:
				return "金沛充值";
			case 6:
				return "金沛充值退款";
			}
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