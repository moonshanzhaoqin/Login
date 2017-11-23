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
			<table class="table table-bordered table-hover table-striped"
				id="goldqPayClient">
				<thead>
					<tr>
						<th>exId</th>
						<th>clientId</th>
						<th>secretKey</th>
						<th>name</th>
						<th>redirectUrl</th>
						<th>customDomain</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			<!--分页插件-->
			<div id="paginator"></div>
			<div id="total" class="pull-right"></div>
		</div>


		<hr style="background-color: grey; height: 1px;" />

		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="goldqPayFee">
				<thead>
					<tr>
						<th>clientId</th>
						<th>商户角色</th>
						<th>免手续费额度</th>
						<th>手续费率</th>
						<th>最小手续费</th>
						<th>最大手续费</th>
						<th>手续费承担者</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>

	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="goldqPayClientModal" tabindex="-1"
		role="dialog" aria-labelledby="goldqPayClientModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="goldqPayClientModalLabel">修改模板</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="updateGoldqPayClient">
						<input type="hidden" name="id" class="form-control" >
						
						<div class="form-group">
							<label for="exId" class="col-sm-4 control-label">exId</label>
							<div class="col-sm-5">
								<input type="text" name="exId" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="clientId" class="col-sm-4 control-label">clientId</label>
							<div class="col-sm-5">
								<input type="text" name="clientId" class="form-control" readonly>
							</div>
						</div>

						<div class="form-group">
							<label for="secretKey" class="col-sm-4 control-label">secretKey</label>
							<div class="col-sm-5">
								<input type="text" name="secretKey" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-sm-4 control-label">name</label>
							<div class="col-sm-5">
								<input type="text" name="name" class="form-control" >
							</div>
						</div>
						<div class="form-group">
							<label for="redirectUrl" class="col-sm-4 control-label">redirectUrl</label>
							<div class="col-sm-5">
								<input type="text" name="redirectUrl" class="form-control">
							</div>
						</div>

						<div class="form-group">
							<label for="customDomain" class="col-sm-4 control-label">customDomain</label>
							<div class="col-sm-5">
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

	<div class="modal fade" id="goldqPayFeeModal" tabindex="-1"
		role="dialog" aria-labelledby="goldqPayFeeModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="goldqPayFeeModalLabel">修改模板</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="updateGoldqPayFee">
<input type="hidden" name="feeId" class="form-control" >
						<div class="form-group">
							<label for="clientId" class="col-sm-4 control-label">clientId</label>
							<div class="col-sm-5">
								<input type="text" name="clientId" class="form-control" readonly>
							</div>
						</div>

						<div class="form-group">
							<label for="payRole" class="col-sm-4 control-label">商户角色</label>
							<div class="col-sm-5">
								<input type="text" name="payRole" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="exemptAmount" class="col-sm-4 control-label">免手续费额度</label>
							<div class="col-sm-5">
								<input type="number" min="0" onkeydown="onlyNum();"
									style="ime-mode: Disabled" name="exemptAmount"
									class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="feePercent" class="col-sm-4 control-label">手续费率</label>
							<div class="col-sm-5">
								<input type="text" name="feePercent" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="minFee" class="col-sm-4 control-label">最小手续费</label>
							<div class="col-sm-5">
								<input type="number" min="0" onkeydown="onlyNum();"
									style="ime-mode: Disabled" name="minFee" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="maxFee" class="col-sm-4 control-label">最大手续费</label>
							<div class="col-sm-5">
								<input type="number" min="0" onkeydown="onlyNum();"
									style="ime-mode: Disabled" name="maxFee" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="feePayer" class="col-sm-4 control-label">手续费承担者</label>
							<div class="col-sm-5">
								<input type="text" name="feePayer" class="form-control" >
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