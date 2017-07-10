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
			<!-- Button trigger modal -->
			<button type="button" class="btn btn-primary pull-right"
				data-toggle="modal" data-target="#addCampaignModal">新增活动</button>
		</div>

		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="campaign">
				<thead>
					<tr>
						<th>活动ID</th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th>活动状态</th>
						<th>预算</th>
						<th>剩余</th>
						<th>邀请人奖励金</th>
						<th>被邀请人奖励金</th>
						<th>更新时间（GMT+8）</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>

		<!-- 模态框（Modal） 新增活动 -->
		<div class="modal fade" id="addCampaignModal" tabindex="-1"
			role="dialog" aria-labelledby="addCampaignModalLabel"
			aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="addCampaignModalLabel">新增活动</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal" role="form" id="addCampaign">
							<div class="form-group">
								<label for="startTime " class="col-sm-4 control-label">开始时间</label>
								<div class="col-sm-7">
									<input id="start" type="datetime" class="form-control"
										name="startTime" />
								</div>
							</div>
							<div class="form-group">
								<label for="endTime" class="col-sm-4 control-label ">结束时间</label>
								<div class="col-sm-7 ">
									<input id="end" type="datetime" class="form-control"
										name="endTime" class="laydate-icon" />
								</div>
							</div>
							<div class="form-group">
								<label for="campaignBudget" class="col-sm-4 control-label ">活动预算（GDQ）</label>
								<div class="col-sm-7">
									<input type="number" min="0" class="form-control"
										name="campaignBudget" />
								</div>
							</div>
							<div class="form-group">
								<label for="inviterBonus" class="col-sm-4 control-label ">邀请人奖励金</label>
								<div class="col-sm-7">
									<input type="number" min="0" class="form-control"
										name="inviterBonus" />
								</div>
							</div>
							<div class="form-group">
								<label for="inviteeBonus" class="col-sm-4 control-label ">被邀请人奖励金</label>
								<div class="col-sm-7">
									<input type="number" min="0" class="form-control"
										name="inviteeBonus" />
								</div>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary"
							onclick="addCampaign()">确认</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script>
		$(function() {
			var start = {
				elem : '#start',
				format : 'YYYY-MM-DD hh:mm:ss',
				min : laydate.now(), //设定最小日期为当前日期
				max : '2099-06-16 23:59:59', //最大日期
				istime : true,
				istoday : false,
				choose : function(datas) {
					end.min = datas; //开始日选好后，重置结束日的最小日期
					end.start = datas //将结束日的初始值设定为开始日
				}
			};
			var end = {
				elem : '#end',
				format : 'YYYY-MM-DD hh:mm:ss',
				min : laydate.now(),
				max : '2099-06-16 23:59:59',
				istime : true,
				istoday : false,
				choose : function(datas) {
					start.max = datas; //结束日选好后，重置开始日的最大日期
				}
			};
			laydate(start);
			laydate(end);

			initCampaign();
		});

		/* 	页面初始化 */
		function initCampaign() {

		}
		/* 	新增活动 */
		function addCampaign() {
			form = document.getElementById("addCampaign");
			var data = {
				startTime : form.startTime.value,
				endTime : form.endTime.value,
				campaignBudget : form.campaignBudget.value,
				inviterBonus : form.inviterBonus.value,
				inviteeBonus : form.inviteeBonus.value
			};
			$.ajax({
				type : "post",
				url : "/crm/addCampaign",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						console.log("success");
						$('#addCampaign').modal('hide')
						alert("添加成功！");
						initCampaign();
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else if (data.retCode == "00003") {
						console.log("parameter is empty");
						alert("请填写完整！");
					} else {
						console.log("addCampaign" + data.message);
						alert(data.message);
					}
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
					console.log(xhr);
					alert("未知错误!");
				},
				async : false
			});
		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>