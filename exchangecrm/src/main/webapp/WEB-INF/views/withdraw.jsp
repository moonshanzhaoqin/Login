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
			<form class="form-inline pull-right" id="searchWithdraw">
				<div class="form-group">
					<label class="sr-only" for="userPhone">userPhone</label> <input
						type="text" class="form-control" name="userPhone"
						placeholder="手机号">
				</div>
				<div class="form-group">
					<label class="sr-only" for="userName">userName</label> <input
						type="text" class="form-control" name="userName" placeholder="用户名">
				</div>
				<div class="form-group">
					<label class="sr-only" for="startTime">startTime</label> <input
						id="start" class="form-control" name="startTime"
						placeholder="开始时间(申请)" />
				</div>
				<div class="form-group">
					<label class="sr-only" for="endTime">endTime</label> <input
						id="end" class="form-control" name="endTime"
						placeholder="结束时间(申请)" />
				</div>
				<div class="form-group">
					<label class="sr-only" for="handleResult">endTime</label> <select
						class="form-control" name="handleResult">
						<option value=""> 提取结果</option>
						<option value="0">处理中</option>
						<option value="1">申请成功</option>
						<option value="2">申请失败</option>
						<option value="3">交易完成</option>
						<option value="4">交易取消</option>
					</select>


				</div>
				<button type="button" class="btn btn-primary "
					onclick="searchWithdraw(1)">搜索</button>
			</form>
		</div>
		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="withdraw">
				<thead>
					<tr>
						<!-- 						<th>提现编号</th> -->
						<!-- 						<th>用户ID</th> -->
						<th>国家码</th>
						<th>手机号</th>
						<th>用户名</th>
						<!-- 						<th>联系邮箱</th> -->
						<th>金条数量(根)</th>
						<th>对应的Goldpay(GDQ)</th>
						<th>手续费(GDQ)</th>
						<th>申请时间（GMT+8）</th>
						<th>提取结果</th>
						<th>详情</th>
						<!-- 						<th>处理者</th> -->
						<!-- 						<th>处理时间</th> -->
						<!-- 						<th>金条交易ID(申请)</th> -->
						<!-- 						<th>手续费交易ID(申请)</th> -->
						<!-- 						<th>金条交易ID(失败/成功)</th> -->
						<!-- 						<th>手续费交易ID(失败/成功)</th> -->
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
			<div id="total" class="pull-right"></div>
		</div>

	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="withdrawDetailModal" tabindex="-1"
		role="dialog" aria-labelledby="withdrawDetailModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="withdrawDetailModalLabel">提取详情</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="form-group">
							<label for="withdrawId" class="col-sm-4 control-label">提现编号：</label>
							<div class="col-sm-5" id="withdrawId"></div>
						</div>
						<div class="form-group">
							<label for="userId" class="col-sm-4 control-label">用户ID：</label>
							<div class="col-sm-5" id="userId"></div>
						</div>
						<div class="form-group">
							<label for="userPhone" class="col-sm-4 control-label">手机号：</label>
							<div class="col-sm-5" id="userPhone"></div>
						</div>
						<div class="form-group">
							<label for="userName" class="col-sm-4 control-label">用户名：</label>
							<div class="col-sm-5" id="userName"></div>
						</div>
						<div class="form-group">
							<label for="userEmail" class="col-sm-4 control-label">联系邮箱：</label>
							<div class="col-sm-5" id="userEmail"></div>
						</div>
						<div class="form-group">
							<label for="quantity" class="col-sm-4 control-label">金条数量(根)：</label>
							<div class="col-sm-5" id="quantity"></div>
						</div>
						<div class="form-group">
							<label for="goldpay" class="col-sm-4 control-label">对应的Goldpay(GDQ)：</label>
							<div class="col-sm-5" id="goldpay"></div>
						</div>
						<div class="form-group">
							<label for="fee" class="col-sm-4 control-label">手续费(GDQ)：</label>
							<div class="col-sm-5" id="fee"></div>
						</div>
						<div class="form-group">
							<label for="applyTime" class="col-sm-4 control-label">申请时间（GMT+8）：</label>
							<div class="col-sm-5" id="applyTime"></div>
						</div>
						<div class="form-group">
							<label for="handleResult" class="col-sm-4 control-label">提取结果：</label>
							<div class="col-sm-5" id="handleResult"></div>
						</div>
						<div class="form-group">
							<label for="handleAdmin" class="col-sm-4 control-label">处理者：</label>
							<div class="col-sm-5" id="handleAdmin"></div>
						</div>
						<div class="form-group">
							<label for="handleTime" class="col-sm-4 control-label">处理时间（GMT+8）：</label>
							<div class="col-sm-5" id="handleTime"></div>
						</div>
						<div class="form-group">
							<label for="operation" class="col-sm-4 control-label">操作：</label>
							<div class="col-sm-5" id="operation"></div>
						</div>
<!-- 						<div class="form-group"> -->
<!-- 							<label for="goldTransferA" class="col-sm-4 control-label">金条交易ID(申请)</label> -->
<!-- 							<div class="col-sm-5" id="goldTransferA"></div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group"> -->
<!-- 							<label for="feeTransferA" class="col-sm-4 control-label">手续费交易ID(申请)</label> -->
<!-- 							<div class="col-sm-5" id="feeTransferA"></div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group"> -->
<!-- 							<label for="goldTransferB" class="col-sm-4 control-label">金条交易ID(完成/取消)</label> -->
<!-- 							<div class="col-sm-5" id="goldTransferB"></div> -->
<!-- 						</div> -->
<!-- 						<div class="form-group"> -->
<!-- 							<label for="feeTransferB" class="col-sm-4 control-label">手续费交易ID(完成/取消)</label> -->
<!-- 							<div class="col-sm-5" id="feeTransferB"></div> -->
<!-- 						</div> -->
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<script src="<c:url value="/resources/js/withdraw.js" />"></script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>