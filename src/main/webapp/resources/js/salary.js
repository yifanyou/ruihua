$(document).ready(function(){

})

change = function(val) {
	var strs = val.split("\\");
	var pos = strs.length - 1;
	var fileName = strs[pos];
	var filestrs = fileName.split(".");
	var typepos = filestrs.length - 1;
	var type = "";
	if (filestrs.length > 1) {
		type = filestrs[typepos];
	}
	
//	if (type != "csv") {
//		alert("只能上传csv格式");
//		return false;
//	}

	$("#upload_file").text("重新上传");
	$("#importBtn").removeClass("disabled");
};

ajaxFileUpload = function(e) {

	var surl = getContextPath() + "/salary?importExcel";

	// 执行上传文件操作的函数
	$.ajaxFileUpload({
		// 处理文件上传操作的服务器端地址(可以传参数,已亲测可用)
		url : surl,
		type : 'post',
		secureuri : false, // 是否启用安全提交,默认为false
		fileElementId : "inputFile1", // 文件选择框的id属性
		dataType : 'text', // 服务器返回的格式,可以是json或xml等
		success : function(data, status) { // 服务器响应成功时的处理函数
			if (status == "success") {
				alert("导入成功");
				var uploadForm = document.getElementById("uploadFile");
				uploadForm.reset();
				$("#upload_file").text("上传文件");
				$("#importBtn").addClass("disabled");
				
				$("#salarys").html(data);
			} else {
				alert("导入失败");
			}
		},
		error : function(data, status) { // 服务器响应失败时的处理函数
			alert("导入异常");
		}
	});
}

importCancel = function() {
	var uploadForm = document.getElementById("uploadFile");
	uploadForm.reset();
	$("#upload_file").text("上传文件");
	$("#importBtn").addClass("disabled");
}