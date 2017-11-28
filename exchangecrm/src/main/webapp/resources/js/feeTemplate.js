$(function() {
	initFeeTemplate();
	$('#feeTemplateModal').on('show.bs.modal', function(e) {
		// do something...
		var tr = $(e.relatedTarget) // Button that triggered the modal
		var feeTemplate = tr.data('whatever') // Extract info from data-*
		// attributes
		console.log(feeTemplate);
		form = document.getElementById("updateFeeTemplate");
		form.feePurpose.value = feeTemplate.feePurpose;
		form.feeName.value = feeTemplate.feeName;
		form.exemptAmount.value = feeTemplate.exemptAmount;
		form.feePercent.value = feeTemplate.feePercent;
		form.minFee.value = feeTemplate.minFee;
		form.maxFee.value = feeTemplate.maxFee;
	})
});

function initFeeTemplate() {
	$
			.ajax({
				type : "post",
				url : "/crm/listFeeTemplate",
				dataType : 'json',
				contentType : "application/json; charset=utf-8",
				data : {},
				success : function(data) {
					console.log("success");
					var html = "";
					for (var i = 0; i < data.length; i++) {
						html += '<tr>' + '<td>'
								+ data[i].feeName
								+ '</td>'
								+ '<td>'
								+ data[i].exemptAmount
								+ '</td>'
								+ '<td>'
								+ data[i].feePercent
								+ '</td>'
								+ '<td>'
								+ data[i].minFee
								+ '</td>'
								+ '<td>'
								+ data[i].maxFee
								+ '</td>'
								+ '<td>'
								+ '<a href="" data-toggle="modal" data-target="#feeTemplateModal" data-whatever='
								+ "'" + JSON.stringify(data[i]) + "'"
								+ '>修改</a>' + '</td>' + '</tr>'
					}
					$('#feeTemplate tbody').html(html);
				},
				error : function(xhr, err) {
					console.log("error");
					console.log(err);
				},
				async : false
			});
}

function updateFeeTemplate() {
	form = document.getElementById("updateFeeTemplate");
	if (parseInt(form.exemptAmount.value) == form.exemptAmount.value
			&& parseInt(form.minFee.value) == form.minFee.value
			&& parseInt(form.maxFee.value) == form.maxFee.value) {
		data = {
			feeName : form.feeName.value,
			feePurpose : form.feePurpose.value,
			exemptAmount : form.exemptAmount.value,
			feePercent : form.feePercent.value,
			minFee : form.minFee.value,
			maxFee : form.maxFee.value
		}
		$.ajax({
			type : "post",
			url : "/crm/updateFeeTemplate",
			contentType : "application/json; charset=utf-8",
			dataType : 'json',
			data : JSON.stringify(data),
			success : function(data) {
				if (data.retCode == "00000") {
					console.log("updateFeeTemplate success");
					$('#feeTemplateModal').modal('hide')
					alert("修改成功！");
					initCampaign(1);
				} else if (data.retCode == "00002") {
					location.href = loginUrl;
				} else {
					console.log("changeBonus" + data.message);
					alert(data.message);
				}
			},
			error : function(xhr, err) {
				console.log("error");
				console.log(err);
				console.log(xhr);
				alert("未知错误！");
			},
			async : false
		});
	} else {
		alert("GDQ需为整数");
	}
}
