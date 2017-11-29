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
				id="feeTemplate">
				<thead>
					<tr>
						<th>模板名称</th>
						<th>免手续费额度(Q)</th>
						<th>手续费率(%)</th>
						<th>最小手续费(Q)</th>
						<th>最大手续费(Q)</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>

	</div>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="feeTemplateModal" tabindex="-1"
		role="dialog" aria-labelledby="feeTemplateModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="feeTemplateModalLabel">修改模板</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="updateFeeTemplate">
						<input type="hidden" name="feePurpose" class="form-control"
									readonly>
						<div class="form-group">
							<label for="feePurpose" class="col-sm-4 control-label">模板名称</label>
							<div class="col-sm-5">
								<input type="text" name="feeName" class="form-control"
									readonly>
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
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="updateFeeTemplate()">保存</button>
				</div>
			</div>
		</div>
	</div>

	<script src="<c:url value="/resources/js/feeTemplate.js" />"></script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>