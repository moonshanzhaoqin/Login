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
			<div class="alert alert-info alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<strong>注意!</strong> 所有操作将会在10分钟后起效。
			</div>
		</div>

		<div class="row ">
			<ul class="formbar pull-right">
				<li><select class="form-control"></select></li>
				<li>
					<button type="button" class="btn btn-primary"
						onclick="addCurrency()">添加新币种</button>
				</li>
			</ul>
		</div>

		<div class="row">
			<table class="table table-bordered table-hover table-striped"
				id="currency">
				<thead>
					<tr>
						<th>货币代码</th>
						<th>简体名称</th>
						<th>英文名称</th>
						<th>繁体名称</th>
						<th>货币单位</th>
						<th>状态</th>
						<th>顺序</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
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
					<h4 class="modal-title" id="myModalLabel">编辑币种</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="updateCurrency">
						<div class="form-group">
							<label for="currency" class="col-sm-2 control-label">货币代码</label>
							<div class="col-sm-5">
								<input type="text" name="currency" class="form-control" readonly>
							</div>
						</div>
						<div class="form-group">
							<label for="nameCn" class="col-sm-2 control-label">简体名称</label>
							<div class="col-sm-5">
								<input type="text" name="nameCn" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="nameEn" class="col-sm-2 control-label">英文名称</label>
							<div class="col-sm-5">
								<input type="text" name="nameEn" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="nameHk" class="col-sm-2 control-label">繁体名字</label>
							<div class="col-sm-5">
								<input type="text" name="nameHk" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="currencyUnit" class="col-sm-2 control-label">货币单位</label>
							<div class="col-sm-5">
								<input type="text" name="currencyUnit" class="form-control">
							</div>
						</div>
						<div class="form-group">
							<label for="currency" class="col-sm-2 control-label">状态</label>
							<div class="col-sm-5">
								<input type="radio" name="currencyStatus" value="1">上架 <input
									type="radio" name="currencyStatus" value="0">下架
							</div>
						</div>
						<div class="form-group">
							<label for="currencyOrder" class="col-sm-2 control-label">顺序</label>
							<div class="col-sm-5">
								<input type="text" name="currencyOrder" class="form-control">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="updateCurrency()">保存</button>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript"
		src="<c:url value="/resources/js/jquery.min.js" />"></script>
	<script type="text/javascript"
		src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>
	<script type="text/javascript">
		$(function() {
			initCurrency();

			$('#myModal').on('show.bs.modal', function(e) {
				// do something...
				var tr = $(e.relatedTarget) // Button that triggered the modal
				var currency = tr.data('whatever') // Extract info from data-* attributes
				console.log(currency);
				form = document.getElementById("updateCurrency");
				form.currency.value = currency.currency;
				form.currencyOrder.value = currency.currencyOrder;
				form.currencyStatus.value = currency.currencyStatus;
				form.currencyUnit.value = currency.currencyUnit;
				form.nameCn.value = currency.nameCn;
				form.nameEn.value = currency.nameEn;
				form.nameHk.value = currency.nameHk;
				if (currency.currency == 'GDQ') {
					form.currencyOrder.readOnly = true;
				} else {
					form.currencyOrder.readOnly = false;
				}
			})

			$('#updateCurrency').bootstrapValidator({
				message : 'This value is not valid',
				feedbackIcons : {/*输入框不同状态，显示图片的样式*/
					valid : 'glyphicon glyphicon-ok',
					invalid : 'glyphicon glyphicon-remove',
					validating : 'glyphicon glyphicon-refresh'
				},
				fields : {/*验证*/
					nameEn : {/*键名username和input name值对应*/
						message : '无效',
						validators : {
							notEmpty : {/*非空提示*/
								message : '名称不能为空'
							}
						/*最后一个没有逗号*/
						}
					},
					nameCn : {
						message : '无效',
						validators : {
							notEmpty : {
								message : '名称不能为空'
							}
						}
					},
					nameHk : {
						message : '无效',
						validators : {
							notEmpty : {
								message : '名称不能为空'
							}
						}
					},
					currencyUnit : {
						message : '无效',
						validators : {
							notEmpty : {
								message : '单位不能为空'
							}
						}
					},
					currencyOrder : {
						message : '无效',
						validators : {
							notEmpty : {
								message : '顺序不能为空'
							},
							regexp : {/* 只需加此键值对，包含正则表达式，和提示 */
								regexp : /^[1-9]/,
								message : '只能是大于1的数字'
							},
							stringLength : {
								max : 6,
								message : '数字过大'
							}
						}
					}
				}
			});
		});

		function initCurrency() {
			$
					.ajax({
						type : "post",
						url : "/crm/getCurrencyList",
						dataType : 'json',
						contentType : "application/json; charset=utf-8",
						data : {},
						success : function(data) {
							console.log("success");
							var html = "";
							for (var i = 0; i < data.length; i++) {
								html += '<tr>'
										+ '<td style="font-weight:bold;">'
										+ data[i].currency
										+ '</td>'
										+ '<td>'
										+ data[i].nameCn
										+ '</td>'
										+ '<td>'
										+ data[i].nameEn
										+ '</td>'
										+ '<td>'
										+ data[i].nameHk
										+ '</td>'
										+ '<td>'
										+ data[i].currencyUnit
										+ '</td>'
										+ (data[i].currencyStatus == 0 ? '<td style="color:red">未上架</td>'
												: '<td style="color:green">已上架</td>')
										+ '<td>'
										+ data[i].currencyOrder
										+ '</td>'
										+ '<td><a href="javascript:void(0)" data-backdrop="static" data-toggle="modal" data-target="#myModal" data-whatever='
										+ "'"
										+ JSON.stringify(data[i])
										+ "'"
										+ '>编辑</a> '
										+ (data[i].currencyStatus == 0 ? '<a href="javascript:void(0)" onclick=" changeCurrencyStatus(this,1)">上架</a>'
												: '<a href="javascript:void(0)" style="color:red" onclick=" changeCurrencyStatus(this,0)">下架</td>')
										+ '</td>' + '</tr>'
							}
							$('#currency tbody').html(html);

						},
						error : function(xhr, err) {
							console.log("error");
							console.log(err);
						},
						async : false
					});
			$.ajax({
				type : "post",
				url : "/crm/getAddingCurrencyList",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : {},
				success : function(data) {
					console.log("success");
					var html = "";
					for (var i = 0; i < data.length; i++) {
						html += '<option value="'+data[i]+'">' + data[i]
								+ '</option>'
					}
					$('select').html(html);
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
				},
				async : false
			});
		}
		function updateCurrency() {
			var updateCurrencyValidator = $('#updateCurrency').data(
					'bootstrapValidator');
			updateCurrencyValidator.validate();
			console.log(updateCurrencyValidator.isValid());
			if (updateCurrencyValidator.isValid()) {
				form = document.getElementById("updateCurrency");
				data = {
					currency : form.currency.value,
					currencyOrder : form.currencyOrder.value,
					currencyStatus : form.currencyStatus.value,
					currencyUnit : form.currencyUnit.value,
					nameCn : form.nameCn.value,
					nameEn : form.nameEn.value,
					nameHk : form.nameHk.value
				}
				$.ajax({
					type : "post",
					url : "/crm/updateCurrency",
					contentType : "application/json; charset=utf-8",
					dataType : 'json',
					data : JSON.stringify(data),
					success : function(data) {
						if (data.retCode == "00000") {
							console.log("success");
							$('#myModal').modal('hide')
							alert("修改成功！");
							initCurrency();
						} else if (data.retCode == "00002") {
							location.href = loginUrl;
						} else {
							console.log("updateCurrency" + data.message);
							alert(data.message);
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
			} else {
				alert("请正确填写后，再提交！")
			}
		}

		function changeCurrencyStatus(obj, status) {
			if (status == 0) {
				var r = confirm("确定该下架币种该么？");
				if (r != true) {
					return;
				}
			} else {
				var r = confirm("确定要上架该币种么？");
				if (r != true) {
					return;
				}
			}

			var tds = $(obj).parent().parent().find('td');
			var currency = tds.eq(0).text();
			var data = {
				currency : currency,
				status : status
			}

			$.ajax({
				type : "post",
				url : "/crm/changeCurrencyStatus",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						initCurrency();
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else {
						alert("something is wrong!");
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

		function addCurrency() {
			var data = {
				currency : $('select').val()
			}
			$.ajax({
				type : "post",
				url : "/crm/addCurrency",
				contentType : "application/json; charset=utf-8",
				dataType : 'json',
				data : JSON.stringify(data),
				success : function(data) {
					if (data.retCode == "00000") {
						console.log("success");
						alert("添加成功！");
						initCurrency();
					} else if (data.retCode == "00002") {
						location.href = loginUrl;
					} else if (data.retCode == "05001") {
						alert("币种已存在")
					} else {
						alert("Something is wrong!");
					}

				},
				error : function(xhr, err) {
					console.log(err);
					console.log(xhr);
					alert("Something is wrong!");
				},
				async : false
			});

		}
	</script>
	<%@ include file="common/footer.jsp"%>
</body>
</html>