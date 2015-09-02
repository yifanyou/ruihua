var isUpload = false;
var isConnect = false;
var isBatchSend = false;

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
	
	if (type!="xls"&&type!="xlsx") {
		alert("请上传Excel文件！");
		return false;
	}

	$("#upload_file").text("重新上传");
	$("#importBtn").removeClass("disabled");
	$("#importCancel").removeClass("disabled");
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
				//alert("导入成功");
				var uploadForm = document.getElementById("uploadFile");
				uploadForm.reset();
				$("#upload_file").text("上传文件");
				$("#importBtn").addClass("disabled");
				$("#salarys").html(data);
				
				isUpload = true;
			} else {
				isUpload = false;
				alert("导入失败！");
			}
		},
		error : function(data, status) { // 服务器响应失败时的处理函数
			isUpload = false;
			alert("导入异常！");
		}
	});
}

importCancel = function() {
	var uploadForm = document.getElementById("uploadFile");
	uploadForm.reset();
	$("#upload_file").text("上传文件");
	$("#importBtn").addClass("disabled");
}

sendEmail = function(e) {
	var pos = $(e).parent().prev().prev().text();
	var emailUrl = getContextPath() + "/salary?sendEmail&pos=" + pos;
	
    $.ajax({
        type:'GET',
        contentType:'application/json',
        url:emailUrl,
        dataType:'text',
        success:function(data){
            if(data!=null) {
            	alert("发送邮件成功");
            }
        },
        error:function(data){
            alert(data);
        }
    });
}

sendManyEmails = function(e){
	alert("sendManyEmail");
}

tectConnect = function() {
	var form = document.getElementById("emailForm");
	
	//校验
	var emailCheck = document.emailForm.email.validity;
	var passwordCheck = document.emailForm.password.validity;
	//boolean status = true;
	if(emailCheck.valueMissing){
		alert("邮箱不能为空！");
		return false;
	}else if(emailCheck.typeMismatch){
		alert("邮箱格式不正确！");
		return false;
	}
	
	if(passwordCheck.valueMissing){
		alert("密码不能为空！");
		return false;
	}

	var email = document.emailForm.email.value;
	var password = document.emailForm.password.value;
	$("#loading").attr("src", getContextPath() + "/resources/images/loading.gif");

	var testUrl = getContextPath() + "/salary?testEmail";
    $.ajax({
        url:testUrl,
        type:'POST',
        contentType:'application/json; charset=utf-8',
        dataType:'text',
        data:JSON.stringify($("#emailForm").serializeObject()),
        success:function(data){
            if(data=="success") {
            	$("#currentEmail").html("&nbsp;&nbsp;&nbsp;&nbsp;当前邮箱：" + email);
            	isConnect = true;
            	$("#loading").attr("src", "");
            	alert("测试连接邮箱成功！");
            }else if(data=="error"){
            	alert("测试连接邮箱失败！");
            }
        },
        error:function(data){
            isConnect = false;
        	alert("测试连接邮箱失败！");
        }
    });
}

resetConnect = function() {
	$("#currentEmail").html("&nbsp;&nbsp;&nbsp;&nbsp;当前邮箱：");
	isConnect = false;
}

batchSendEmail = function(){
	if(isBatchSend){
		alert("批量发送邮件成功，不能再次发送！");
		return false;
	}
	
	$("#status").text("正在发送中...");
	
	var batchUrl = getContextPath() + "/salary?batchSendEmail";
    $.ajax({
        url:batchUrl,
        type:'Get',
        contentType:'application/json; charset=utf-8',
        dataType:'text',
        success:function(data){
            if(data=="success") {
            	$("#status").text("发送完毕！");
            	isBatchSend = true;
            }else {
            	if(data=="0")
            		isBatchSend = false;
            	else
            		isBatchSend = true;
            	
            	$("#status").text("发送中断，发送到Serial NO:"+ data + "！");
            }
        },
        error:function(data){
        	isBatchSend = false;
        	alert("发送失败！");
        }
    });
}