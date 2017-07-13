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
						<th>操作</th>
						<th>修改奖励金</th>
						<th>追加预算</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
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

	<!-- 模态框（Modal） 修改奖励金 -->
	<div class="modal fade" id="changeBounsModal" tabindex="-1"
		role="dialog" aria-labelledby="changeBounsModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="changeBounsModalLabel">修改奖励金</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="changeBouns">
						<div class="form-group">
							<label for="campaignId" class="col-sm-4 control-label ">活动ID</label>
							<div class="col-sm-7">
								<input type="text" class="form-control" name="campaignId"
									readonly />
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
						onclick="changeBouns()">确认</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 模态框（Modal）追加预算 -->
	<div class="modal fade" id="addBudgetModal" tabindex="-1"
		role="dialog" aria-labelledby="addBudgetModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="addBudgetModalLabel">修改奖励金</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="addBudget">
						<div class="form-group">
							<label for="campaignId" class="col-sm-4 control-label ">活动ID</label>
							<div class="col-sm-7">
								<input type="text" class="form-control" name="campaignId"
									readonly />
							</div>
						</div>
						<div class="form-group">
							<label for="campaignBudget" class="col-sm-4 control-label ">活动预算（GDQ）</label>
							<div class="col-sm-7">
								<input type="number" class="form-control"
									name="campaignBudget" readonly/>
							</div>
						</div>
						<div class="form-group">
							<label for="additionalBudget" class="col-sm-4 control-label ">追加预算（GDQ）</label>
							<div class="col-sm-7">
								<input type="number" min="0" class="form-control"
									name="additionalBudget" />
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="addBudget()">确认</button>
				</div>
			</div>
		</div>
	</div>

	<script src="<c:url value="/resources/js/campaign.js" />"></script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>