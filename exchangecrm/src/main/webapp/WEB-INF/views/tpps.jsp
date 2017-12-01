<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>
<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<%@ include file="common/header.jsp"%>
<head>
<style type="text/css">
td {
	overflow: hidden
}
</style>
</head>
<body>
	<div class="container">
		<div class="row ">
			<ul class="formbar pull-right">
				<li>
					<button type="button" class="btn btn-primary " data-toggle="modal"
						data-target="#addGoldqPayClientModal">新增商户</button>
				</li>
			</ul>
		</div>
		<div class="row table-responsive">
			<table class="table table-bordered table-hover table-striped"
				id="goldqPayClient">
				<thead>
					<tr>
						<th>商户ID</th>
						<th>商户名称</th>
						<th>操作</th>
						<th>手续费模板</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
			<div id="total" class="pull-right"></div>
		</div>
		
		<hr class="divide" />
		
		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="goldqPayFee">
				<thead>
					<tr>
						<th>商户ID</th>
						<th>模板类型</th>
						<th>免手续费额度(Q)</th>
						<th>手续费率(%)</th>
						<th>最小手续费(Q)</th>
						<th>最大手续费(Q)</th>
						<th>手续费承担者</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>

	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="addGoldqPayClientModal" tabindex="-1"
		role="dialog" aria-labelledby="addGoldqPayClientModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="addGoldqPayClientModalLabel">新增商户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="addGoldqPayClient">

						<div class="form-group">
							<label for="areaCode" class="col-sm-4 control-label">国家码(e.g.'+86')</label>
							<div class="col-sm-5">
								<input type="text" name="areaCode" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="userPhone" class="col-sm-4 control-label">手机号</label>
							<div class="col-sm-5">
								<input type="text" name="userPhone" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="userPayToken" class="col-sm-4 control-label">支付密钥</label>
							<div class="col-sm-5">
								<input type="text" name="userPayToken" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-sm-4 control-label">商户名称(必填)</label>
							<div class="col-sm-5">
								<input type="text" name="name" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="redirectUrl" class="col-sm-4 control-label">跳转URL</label>
							<div class="col-sm-5">
								<input type="text" name="redirectUrl" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="customDomain" class="col-sm-4 control-label">自定义域名</label>
							<div class="col-sm-5">
								<input type="text" name="customDomain" class="form-control">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="addGoldqPayClient()">添加</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="updategoldqPayClientModal" tabindex="-1"
		role="dialog" aria-labelledby="updategoldqPayClientModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="updategoldqPayClientModalLabel">修改商户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="updateGoldqPayClient">

						<div class="form-group">
							<label for="areaCode" class="col-sm-3 control-label">Goldpay国家码</label>
							<div class="col-sm-8">
								<input type="text" name="areaCode" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="userPhone" class="col-sm-3 control-label">Goldpay手机号</label>
							<div class="col-sm-8">
								<input type="text" name="userPhone" class="form-control"
									readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="userName" class="col-sm-3 control-label">Goldpay用户名</label>
							<div class="col-sm-8">
								<input type="text" name="userName" class="form-control" readonly>
							</div>
						</div>
						<input type="hidden" name="exId" class="form-control" >
						<div class="form-group">
							<label for="clientId" class="col-sm-3 control-label">商户ID</label>
							<div class="col-sm-8">
								<input type="text" name="clientId" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="secretKey" class="col-sm-3 control-label">API密钥</label>
							<div class="col-sm-8">
								<input type="text" name="secretKey" class="form-control" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="userPayToken" class="col-sm-3 control-label">支付密钥</label>
							<div class="col-sm-8">
								<input type="text" name="userPayToken" class="form-control" >
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-sm-3 control-label">商户名称</label>
							<div class="col-sm-8">
								<input type="text" name="name" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="redirectUrl" class="col-sm-3 control-label">跳转URL</label>
							<div class="col-sm-8">
								<input type="text" name="redirectUrl" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="customDomain" class="col-sm-3 control-label">自定义域名</label>
							<div class="col-sm-8">
								<input type="text" name="customDomain" class="form-control">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="updateGoldqPayClient()">保存</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="updategoldqPayFeeModal" tabindex="-1"
		role="dialog" aria-labelledby="updategoldqPayFeeModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="updategoldqPayFeeModalLabel">修改手续费模板</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="updateGoldqPayFee">
						<input type="hidden" name="feeId" class="form-control">
						<div class="form-group">
							<label for="clientId" class="col-sm-4 control-label">商户ID</label>
							<div class="col-sm-5">
								<input type="text" name="clientId" class="form-control" readonly>
							</div>
						</div>

						<div class="form-group">
							<label for="payRole" class="col-sm-4 control-label">转账类型</label>
							<div class="col-sm-5">
								<select name="payRole" class="form-control" disabled="disabled">
									<option value=1>转入</option>
									<option value=2>转出</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="exemptAmount" class="col-sm-4 control-label">免手续费额度(Q)</label>
							<div class="col-sm-5">
								<input type="number" min="0" onkeydown="onlyNum();"
									style="ime-mode: Disabled" name="exemptAmount"
									class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="feePercent" class="col-sm-4 control-label">手续费率(%)</label>
							<div class="col-sm-5">
								<input type="text" name="feePercent" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="minFee" class="col-sm-4 control-label">最小手续费(Q)</label>
							<div class="col-sm-5">
								<input type="number" min="0" onkeydown="onlyNum();"
									style="ime-mode: Disabled" name="minFee" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="maxFee" class="col-sm-4 control-label">最大手续费(Q)</label>
							<div class="col-sm-5">
								<input type="number" min="0" onkeydown="onlyNum();"
									style="ime-mode: Disabled" name="maxFee" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="feePayer" class="col-sm-4 control-label">手续费承担者</label>
							<div class="col-sm-5">
								<select name="feePayer" class="form-control">
									<option value=1>收款方</option>
									<option value=2>付款方</option>
								</select>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="updateGoldqPayFee()">保存</button>
				</div>
			</div>
		</div>
	</div>

	<script src="<c:url value="/resources/js/tpps.js" />"></script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>