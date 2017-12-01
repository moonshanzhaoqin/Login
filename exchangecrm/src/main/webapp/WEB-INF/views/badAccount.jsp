<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp"%>
<body>
	<div class="container" style="height: 100%;">

		<!-- 		<div class="row" id="task" style="margin-bottom: 20px;"></div> -->

		<div class="row">
			<table class="table table-bordered table-hover" id="badAccount">
				<thead>
					<tr>
						<th>手机号</th>
						<th>币种</th>
						<th>时间区间（GMT+8）</th>
						<th>交易前金额</th>
						<th>交易累计金额</th>
						<th>交易后期望金额</th>
						<th>交易后实际金额</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody style="height: 250px; overflow: auto;"></tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
		</div>

		<hr style="background-color: grey; height: 1px;" />

		<div id="detail" class="row" style="height: 400px; overflow: auto;">
			<table class="table table-bordered table-hover table-striped"
				id="walletSeq">
				<thead>
					<tr>
						<th>流水号</th>
						<th>币种</th>
						<th>数量</th>
						<th>交易类型</th>
						<th>交易ID</th>
						<th>创建时间（GMT+8）</th>
					</tr>
				</thead>
				<tbody
					style="display: table-row-group; height: 400px; overflow: auto;"></tbody>
			</table>
		</div>

	</div>

	<%@ include file="badAccountDetail.jsp"%>

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap-paginator.min.js" />"></script>
	<script type="text/javascript">
		$(function() {
// 			getGoldpayRemitTaskStatus()
			getBadAccountByPage(1);
// 			$("tr").removeClass("active")
		})

// 		function getGoldpayRemitTaskStatus() {
// 			$
// 					.ajax({
// 						type : "post",
// 						url : "/crm/getGoldpayRemitTaskStatus",
// 						dataType : 'json',
// 						contentType : "application/json; charset=utf-8",
// 						data : {},
// 						success : function(data) {
// 							if (data.retCode == "00002") {
// 								location.href = loginUrl;
// 							} else {
// 								console.log("success");
// 								console.log(data);
// 								if (data == true) {
// 									$("#task")
// 											.html(
// 													'<button type="button" class="btn btn-primary pull-right" onclick="setGoldpayRemitTaskStatus(false)">开启核帐功能</button>');
// 								} else {
// 									$("#task")
// 											.html(
// 													'<button type="button" class="btn btn-danger pull-right" onclick="setGoldpayRemitTaskStatus(true)">关闭核帐功能</button>');
// 								}
// 							}

// 						},
// 						error : function(xhr, err) {
// 							alert("未知错误");
// 							console.log(err);
// 						}

// 					})
// 		}
// 		function setGoldpayRemitTaskStatus(status) {
// 			if (confirm("确认吗？")) {
// 				var data = {
// 					status : status
// 				};
// 				$.ajax({
// 					type : "post",
// 					url : "/crm/setGoldpayRemitTaskStatus",
// 					dataType : 'json',
// 					contentType : "application/json; charset=utf-8",
// 					data : JSON.stringify(data),
// 					success : function(data) {
// 						if (data.retCode == "00002") {
// 							location.href = loginUrl;
// 						} else {
// 							getGoldpayRemitTaskStatus();
// 						}

// 					},
// 					error : function(xhr, err) {
// 						alert("未知错误");
// 						console.log(err);
// 					},
// 					async : false

// 				})
// 			}
// 		}
		function getBadAccountByPage(currentPage) {
			var data = {
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
									html += '<tr id="'+data.rows[i][0].badAccountId+'">' + '<td>'
											+ data.rows[i][1].areaCode
											+ data.rows[i][1].userPhone
											+ '</td>'
											+ '<td>'
											+ data.rows[i][0].currency
											+ '</td>'
											+ '<td>'
											+ timeDate(data.rows[i][0].startTime)
											+ ' ~ '
											+ timeDate(data.rows[i][0].endTime)
											+ '</td>'
											+ '<td>'
											+ new Number(
													data.rows[i][0].balanceBefore)
													.toFixed(4)
											+ '</td>'
											+ '<td>'
											+ new Number(
													data.rows[i][0].sumAmount)
													.toFixed(4)
											+ '</td>'
											+ '<td>'
											+ (new Number(
													data.rows[i][0].balanceBefore
															+ data.rows[i][0].sumAmount)
													.toFixed(4))
											+ '</td>'
											+ '<td>'
											+ new Number(
													data.rows[i][0].balanceNow)
													.toFixed(4)
											+ '</td>'
											+ '<td>'
											+ '<a href="javascript:void(0)" onclick="getDetailSeq('
											+ data.rows[i][0].badAccountId
											+ ')">详情</a>' + '</td>' + '</tr>'
								}
								$('#badAccount tbody').html(html);
								if (data.currentPage == 1 && data.pageTotal>1) {
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

		function getDetailSeq(badAccountId) {
            $("tr").removeClass("success")
            $("#" + badAccountId).addClass("success")

            var data = {
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
                                            + new Number(data[i].amount)
                                                    .toFixed(4)
                                            + '</td>'
                                            + '<td>'
                                            + transferTypeName(data[i].transferType)
                                            + '</td>'
                                            + '<td>'
                                            + ((data[i].transferType == "1") ? ('<a href="" data-backdrop="static" data-toggle="modal" data-target="#exchange" onclick= "getExchange(\''
                                                    + data[i].transactionId
                                                    + '\')">    '
                                                    + data[i].transactionId + '</a>')
                                                    : ('<a href="" data-backdrop="static" data-toggle="modal" data-target="#transfer" onclick= "getTransfer(\''
                                                            + data[i].transactionId
                                                            + '\')">    '
                                                            + data[i].transactionId + '</a>'))
                                            + '</td>' + '<td>'
                                            + timeDate(data[i].createTime)
                                            + '</td>' + '</tr>'
                                }
                                if (html == "") {
                                    alert("没有坏账")
                                }
                                $('#walletSeq tbody').html(html);
                                $('#detail').show();
                            }
                        },
                        error : function(xhr, err) {
                            alert("未知错误");
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
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>