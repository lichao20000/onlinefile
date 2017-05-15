var onlineUploadArray = [];
var onlineErrorFile = 0;
/* This is an example of how to cancel all the files queued up.  It's made somewhat generic.  Just pass your SWFUpload
object in to this method and it loops through cancelling the uploads. */
function cancelQueue(instance) {
	document.getElementById(instance.customSettings.cancelButtonId).disabled = true;
	var startBtn = document.getElementById(instance.customSettings.startButtonId);
	startBtn.disabled = true;
	startBtn.className = "";
	instance.stopUpload();
	var stats;
	
	do {
		stats = instance.getStats();
		instance.cancelUpload();
	} while (stats.files_queued !== 0);
	return false;
}

function startQueue(instance) {
	try {
		instance.startUpload();
	} catch (ex) {
		instance.debug(ex);
	}
	return false;
}

/* **********************
   Event Handlers
   These are my custom event handlers to make my
   web application behave the way I went when SWFUpload
   completes different tasks.  These aren't part of the SWFUpload
   package.  They are part of my application.  Without these none
   of the actions SWFUpload makes will show up in my application.
   ********************** */
function swfuploadLoaded()
{
	// swfupload加载后执行
}
function fileDialogStart() {
	onlineUploadArray = [];
	onlineErrorFile = 0;
	/* I don't need to do anything here */
}
function fileQueued(file) {
	try {
		// You might include code here that prevents the form from being submitted while the upload is in
		// progress.  Then you'll want to put code in the Queue Complete handler to "unblock" the form
		onlineUploadArray.push(file);
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setStatus("等待上传...");
		progress.toggleCancel(true, this);
//		fsUploadProgressShow();
	} catch (ex) {
		this.debug(ex);
	}
}

function fileQueueError(file, errorCode, message) {
	var timeout = onlineErrorFile==0?100:onlineErrorFile*1000;
	onlineErrorFile ++ ;
	try {
		if (errorCode === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
			// wangwenshuo 20150908 使用统一的消息提示
			//$.dialog.notice({width: 150,content: "不能选择太多的文件。\n" + (message == 0 ? "已经达到上传文件限制。" : "最多可以添加   " + message + " 个文件。"),icon: 'error',time: 3});
			showBottomMsg("不能选择太多的文件。" + (message == 0 ? "已经达到上传文件限制。" : "最多可以添加   " + message + " 个文件。"),"3");
			return;
		}
		$("#fsUploadProgress").css('display','block');
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setError();
		progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			progress.setStatus("文件太大。文件大小限制" + this.settings.file_size_limit/1024 + "MB");
			setTimeout("showBottomMsg('文件("+file.name+")太大。大小限制"+this.settings.file_size_limit/1024+"MB','3')", timeout);
			this.debug("错误: 文件太大, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			progress.setStatus("文件大小不能为 0 KB。");
			setTimeout("showBottomMsg('文件("+file.name+")大小不能为 0 KB。','3')", timeout);
			this.debug("错误: 0尺寸文件, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			progress.setStatus("文件格式不可用。");
			this.debug("错误: 文件格式不可用, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			//alert("选择的文件太多.  " +  (message > 1 ? "最多可以添加 " +  message + " 个文件" : "不能再添加文件了."));
			// wangwenshuo 20150908 使用统一的消息提示
			//$.dialog.notice({width: 150,content: "选择的文件太多.  " +  (message > 1 ? "最多可以添加 " +  message + " 个文件" : "不能再添加文件了."),icon: 'error',time: 3});
			showBottomMsg("不能选择太多的文件。" + (message == 0 ? "已经达到上传文件限制。" : "最多可以添加   " + message + " 个文件。"),"3");
			break;
		default:
			if (file !== null) {
				progress.setStatus("未知错误。");
			}
			this.debug("错误代码: " + errorCode + ", 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
	setTimeout(function(){
		$("#fsUploadProgress").css('display','none');
	},2000);
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
	/** 我的文档文件上传 不需要指定其他参数 直接上传 */
	if($("#myDocument").hasClass("active")){
		var folderId = $("#file-breadcrumbs li.last").attr("data-folder-id");
		for(var i=0;i<onlineUploadArray.length;i++){
			var onefile = onlineUploadArray[i];
			onefile.folderId = folderId;
			onefile.openlevel = "1";
			onlineUploadArray[i] = onefile;
		}
		$("#btnStart").trigger("click");
		return;
	}else{
		try {
			if (this.getStats().files_queued > 0) {
				document.getElementById(this.customSettings.cancelButtonId).disabled = false;
				var startBtn = document.getElementById(this.customSettings.startButtonId);
				startBtn.disabled = false;
				startBtn.className = "aui_state_highlight";
				$("#uploadFileNumId").val("0");
				$("#uploadBtnId").text("上传");
				var folderId = $("#file-breadcrumbs li.last").attr("data-folder-id");
				var fileNames = new Array();
				for(var i=0;i<onlineUploadArray.length;i++){
					var onefile = onlineUploadArray[i];
					onefile.folderId = folderId;
					onefile.openlevel = "2";
					onlineUploadArray[i] = onefile;
					fileNames.push(onlineUploadArray[i].name);
				}
				var fileNamesStr = fileNames.join("|");  //使用|分割文件名   后台使用split("\\|")
				//jQuery.getJSON(window.onlinefilePath+'/rest/onlinefile_filesws/checkFileNamesExist?callback=?',
				//xyc 替换接口
				var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkClassFileNamesIsExist?callback=?';
				var data = {"companyId":g_companyId, "classId":folderId, "fileNames":encodeURI($.trim(fileNamesStr), "utf-8")};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
							
						if(json.isExist){//如果存在同名文件，用户选择上传还是取消
							
							var confict = "1";	
							for(var i in json.files){
								if(json.files[i].extentions=="false"){
									confict = "2"
									break;
								}else if(json.files[i].openLevel=="3" && json.files[i].owner!=g_userId){
									confict = "3"
									break;
								}
							}
							var msg = "存在同名文件，上传并更新版本号？";
							if(confict=="2"){
								msg = "存在无后缀名文件，请重新选择！";
							}else if(confict=="3"){
								msg = "存在他人私密文件，请重新选择上传文件！";
							}
							
							var html = template('file_upload_version_template', json);
							$.dialog({
								title:msg,
								content : html,
								width: 400,
								okValue : '确定',
								cancelValue : '取消',
								cancel : function(){
									cancelQueue($uploadObj);
								},
								ok : confict!="1" ? false : function() {
									$("#btnStart").trigger("click");
								}
							}).showModal();
							// 文件很多时显示滚动条
							var height = document.documentElement.clientHeight-150;
							if(height<400) height=400;
							if($("#file_upload_version_confict").height()>height){
								$("#file_upload_version_confict").height(height)
								$("#file_upload_version_confict").perfectScrollbar();
							};
						}else{ //如果不存在同名文件，直接上传
							$("#btnStart").trigger("click");
						}
				});
			}
		} catch (ex)  {
	        this.debug(ex);
		}
	}
	/*
	try {
		if (this.getStats().files_queued > 0) {
			document.getElementById(this.customSettings.cancelButtonId).disabled = false;
			var startBtn = document.getElementById(this.customSettings.startButtonId);
			startBtn.disabled = false;
			startBtn.className = "aui_state_highlight";
			$("#uploadFileNumId").val("0");
			
			if($("#myDocument").hasClass("active")){//我的文档下上传的
				$("#uploadShareMainDivId").show();
				$("#uploadOpenlevelUlId li.privateDocLi").show();
				$("#uploadKeepPId").text("对剩下未设置的同一批次上传(点击上传按钮后)的文件均使用以上的设置，分享位置以及公开级别。");
				$("#uploadNoKeepPId").text("对每个文件都单独进行设置分享位置或公开级别。");
			}else{//分类下上传的
				$("#uploadShareMainDivId").hide();
				$("#uploadOpenlevelUlId li.privateDocLi").hide();
				$("#uploadKeepPId").text("对剩下未设置的同一批次上传(点击上传按钮后)的文件均使用以上设置。");
				$("#uploadNoKeepPId").text("对每个文件都单独进行设置公开级别。");
			}
			$("#showtitleSpanId").text(onlineUploadArray[0].name);
			$("#showtitleSpanId").attr("title",onlineUploadArray[0].name);
			$("#action-content-panel-upload").hide();
			$("#uploadBtnId").text("上传");
			$("#uploadbg").hide();
			$("#uploadChooseFolderDivId").hide();
			$("#action-content-panel-upload").show();
			$("#uploadbg").show();
			
			//如果只选择一个文件 就不显示取消按钮
			if(onlineUploadArray.length==1){
				$("#uploadSkipBtnId").hide();
			}else{
				$("#uploadSkipBtnId").show();
			}
			
			// 判断文件名是否重复
			checkFileNameExist(onlineUploadArray[0].name, 0);
//			$.each(onlineUploadArray, function(i,file){      
//			      alert(i);   
//			      alert(file.id);
//			      alert(file.name);
//			  });  
//			$("#btnStart").trigger("click");
		}
	} catch (ex)  {
        this.debug(ex);
	}*/
}

function uploadStart(file) {
	try {
		/* I don't want to do any file validation or anything,  I'll just update the UI and return true to indicate that the upload should start */
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setStatus("正在上传...");
		progress.toggleCancel(true, this);
	}
	catch (ex) {
	}
	
	return true;
}
function uploadStartOfprop(file) {
	changeUploadBtn("0");//一旦上传，那么上传过程中，上传按钮不可用
	$("#fileupload-content").css("z-index","1500");
//	if("OK"==file.a){
		try {
			var classId = "";
			var openlevel = "";
			var iscancel = "";
			$.each(onlineUploadArray, function(i,newfile){      
				if(newfile.id==file.id){
					classId = newfile.folderId;
					openlevel = newfile.openlevel;
					iscancel = newfile.iscancel;
					file.name = newfile.name;
					return false;
				}
			  });  
			if("1"==iscancel){
				this.cancelUpload();  //这个可以取消上传
			}else{
				/* I don't want to do any file validation or anything,  I'll just update the UI and return true to indicate that the upload should start */
				var progress = new FileProgress(file, this.customSettings.progressTarget);
				progress.setStatus("正在上传...");
				$(".progressWrapper").css("display","none");
				progress.toggleCancel(true, this);
				this.setPostParams({   
					'userId':g_userId,'classId':classId,'companyId':g_companyId,"openlevel":openlevel
				});  
			}
		}
		catch (ex) {
			
		}
//	}
//	if("cancel"==file.a){
//		this.cancelUpload();  //这个可以取消上传
//	}
//	if("cancelAll"==file.a){
//		$("#btnCancel").trigger("click");
//	}
	return true;
}

function uploadProgress(file, bytesLoaded, bytesTotal) {

	try {
		var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);

		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setProgress(percent);
		progress.setStatus("正在上传...");
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadSuccess(file, serverData) {
	try {
		if(this.customSettings.progressTarget){
			var progress = new FileProgress(file, this.customSettings.progressTarget);
			progress.setComplete();
			progress.setStatus("上传完毕.");
			progress.toggleCancel(false);
		}
		if(typeof this.customSettings.uploadSuccess === "function"){
			this.customSettings.uploadSuccess(file, serverData, this.getStats().files_queued);
		}
	} catch (ex) {
		this.debug(ex);
	}
}

function uploadComplete(file) {
	try {
		/*  I want the next upload to continue automatically so I'll call startUpload here */
		if (this.getStats().files_queued === 0) {
			changeUploadBtn("1");//全部上传完毕后，再把上传按钮启用
			document.getElementById(this.customSettings.cancelButtonId).disabled = true;
			var startBtn = document.getElementById(this.customSettings.startButtonId);
			startBtn.disabled = true;
			startBtn.className = "";
			
			//wangwenshuo 20150909 上传后重置数量
			var stats = this.getStats();
			stats.successful_uploads = 0;
			this.setStats(stats);
			
			setTimeout(function(){
				$("#fsUploadProgress").css('display','none');
			},1000);
		} else {	
			this.startUpload();
		}
	} catch (ex) {
		this.debug(ex);
	}

}

function uploadError(file, errorCode, message) {
	showBottomMsg("文件("+file.name+")上传失败","3");
	try {
		var progress = new FileProgress(file, this.customSettings.progressTarget);
		progress.setError();
		progress.toggleCancel(false);

		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
			progress.setStatus("上传出错: " + message);
			this.debug("错误: HTTP错误, 文件名: " + file.name + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
			progress.setStatus("配置错误");
			this.debug("错误: No backend file, 文件名: " + file.name + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
			progress.setStatus("上传失败.");
			this.debug("错误: 上传失败, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.IO_ERROR:
			progress.setStatus("服务器 (IO) 错误");
			this.debug("错误: IO错误, 文件名: " + file.name + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
			progress.setStatus("安全问题");
			this.debug("错误: 安全问题, 文件名: " + file.name + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
			progress.setStatus("上传超出限制.");
			this.debug("错误: 上传超出限制, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
			progress.setStatus("文件未找到.");
			this.debug("错误: 文件未找到, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
			progress.setStatus("验证失败.  跳过上传.");
			this.debug("错误: 文件验证失败, 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
			if (this.getStats().files_queued === 0) {
				document.getElementById(this.customSettings.cancelButtonId).disabled = true;
				var startBtn = document.getElementById(this.customSettings.startButtonId);
//				alert(this.customSettings.startButtonFlag);
				//guolanrui 20140830 增加一个按钮标识属性值的判断，如果按钮是来自编辑报表的话，不将保存按钮置灰
				if(this.customSettings.startButtonFlag == 'editReport'){
					startBtn.disabled = false;
				}else{
					startBtn.disabled = true;
				}
				startBtn.className = "";
			}
			progress.setStatus("取消上传");
			progress.setCancelled();
			break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
			progress.setStatus("停止上传");
			break;
		default:
			progress.setStatus("未知错误: " + error_code);
			this.debug("错误: " + errorCode + ", 文件名: " + file.name + ", 文件大小: " + file.size + ", 信息: " + message);
			break;
		}
	} catch (ex) {
        this.debug(ex);
    }
}
//上传图片即时显示
function imageDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		this.startUpload();
	} catch (ex) {
		this.debug(ex);
	}
}

function settingUploadFilePara(iscancel){
	var isMyDocument = true;
	isMyDocument = $("#myDocument").hasClass("active");
//	验证值是否通过
	if("1"!=iscancel && isMyDocument){
		if("3"!=$("#uploadOpenlevelId").attr("openlevel")){
			if($("#chooseUploadFolderBtnId").attr("folderId")=="0"){
				showBottomMsg("请选择分享位置!","3");
				return;
			}
		}
	}
//	通过的话将设置记录在数组对应的file中
	var num = $("#uploadFileNumId").val();
	num = num*1;
	var file = onlineUploadArray[num];
	var folderId = $("#chooseUploadFolderBtnId").attr("folderId");
	var openlevel =  $("#uploadOpenlevelId").attr("openlevel");
	if("1"==iscancel){
		file.iscancel = "1";
	}else{
		if(isMyDocument){
			file.folderId = folderId;
		}else{
			// 这最后回去当前分类的文件夹
			folderId = $("#file-breadcrumbs li.last").attr("data-folder-id");
			file.folderId = folderId;
		}
		file.openlevel = openlevel;
	}
	onlineUploadArray[num] = file;
//	判断当前的是否为最后一个文件
	if((num+1)<onlineUploadArray.length){
		//先判断是不是以后的文件均保持设置了，如果是，那么就循环赋值，然后直接上传
		if("1"==$("#uploadkeepSettingbtnId").attr("keepvalue") && "1"!=iscancel){
			for(var i=num+1;i<onlineUploadArray.length;i++){
				var onefile = onlineUploadArray[i];
				onefile.folderId = folderId;
				onefile.openlevel = openlevel;
				onlineUploadArray[i] = onefile;
			} 
			
			uploadForm();
		}else{
			//	如果不是，就继续展现页面，并且将数组下标+1后赋值给隐藏数据
			$("#uploadFileNumId").val(num+1);
			//根据隐藏的ID，将标题展现在页面上
			$("#showtitleSpanId").text(onlineUploadArray[num+1].name);
			$("#showtitleSpanId").attr("title",onlineUploadArray[num+1].name);
			$("#action-content-panel-upload").hide();
			$("#uploadbg").hide();
			$("#action-content-panel-upload").show();
			$("#uploadbg").show();
			
			// 判断文件名是否重复
			checkFileNameExist(onlineUploadArray[num+1].name, (num+1));
		}
		
	}else{
		//是最后一个，就直接上传,然后恢复原值
		uploadForm();
	}
	
}

function uploadForm(){
	$("#chooseFolderShowSpanId").text("请选择");
	$('#chooseFolderCrumbs').html("");
	$('#upload_folder_list').html("");
	$("#chooseUploadFolderBtnId").attr("folderId","0");
	$("#openLeverTitleShowSpanId").text("分类内公开");
	$("#uploadOpenlevelId").attr("openlevel","2");
	$("#keepSettingTitleShowSpanId").text("其他文件均保持此设置");
	$("#uploadkeepSettingbtnId").attr("keepvalue","1");
	$("#action-content-panel-upload").hide();
	$("#uploadbg").hide();
	$("#btnStart").trigger("click");
	$("div.fileNameExist").addClass("hidden");
}

$("#uploadBtnId").on("click",function(){
	/**  此处用来区别是否是邮件附件上传还是我的文档的上传，如果==0说明不是邮件上传，如果为1那么为邮件附件上传，由于修改前端代码太冗余，所以在此处修改 **/
	if($("#emailSyncBtn").length=="0"){
		settingUploadFilePara("0");
	}else{
		if($("#chooseUploadFolderBtnId").attr("folderId")=="0"){
				showBottomMsg("请选择分享位置!","3");
				return;
			}
		var folderId = $("#chooseUploadFolderBtnId").attr("folderId");
		var openlevel =  $("#uploadOpenlevelId").attr("openlevel");
		var tmpIndex = $(this).attr("value");
		if(tmpIndex.length>8){
			showBottomMsg("您选择的附件比较多,我们正在拼命为您下载!",1);
		}else{
			showBottomMsg("邮箱附件开始下载",1);
		}
		
		var url = window.siteUrl+'/fileStoreMainServer/rest/mainFileServer/dispatchFileUploadUrl?callback=?';
		var data = {};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			var emailUploadUrl = json.url;
			if (emailUploadUrl != "") {
				
				var url = window.onlinefilePath+'/rest/onlinefile_emailws/downloadAttachMent?callback=?';
				var data = {"userid":window.userId,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,"email":$("#dropdownMenu_email").attr("title"),"emailIndex":tmpIndex,"attachMentIndex":tmpIndex,"emailUploadUrl":emailUploadUrl,"classId":folderId,"companyId":g_companyId,"openlevel":openlevel};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json){
					showBottomMsg("邮箱附件下载成功",1);
				});
			} else {
				dialog({
					title: '遇到问题',
					content: '文件服务器找不到了，我们正在努力解决...',
					ok: true,
					okValue:'确定'
				}).showModal();
			}
		});
		
		$(".keepSettingSpan,.keepSettingDropDownDiv").show();
		$(".fileShareBtn").css("margin-top",20);
		$("#uploadBtnId").text("上传");
		$("#uploadSkipBtnId").show();
		uploadForm();
	
	}
});
$("#uploadSkipBtnId").on("click",function(){
	/**  此处用来区别是否是邮件附件上传还是我的文档的上传，如果==0说明不是邮件上传，如果为1那么为邮件附件上传，由于修改前端代码太冗余，所以在此处修改 **/
	if($("#emailSyncBtn").length=="0"){
		settingUploadFilePara("1");
	}else{
		uploadForm();
		$(".keepSettingSpan,.keepSettingDropDownDiv").show();
		$("#uploadBtnId").text("上传");
		$(".fileShareBtn").css("margin-top",20);
	}
});

$("#uploadCancelAId,#uploadAllCancelBtn").on("click",function(){
	$("#action-content-panel-upload").hide();
	$("#uploadbg").hide();
	$("#uploadSkipBtnId").show();
	cancelQueue($uploadObj);  //wangwenshuo 20150918 清空上传队列
});

/**
 * 检查当前路径下，文件名是否存在
 */
function checkFileNameExist(fileName, index) {
	var isMyDocument = true;
	isMyDocument = $("#myDocument").hasClass("active");
	var folderId = 0;
	if(isMyDocument){
		folderId = $("#chooseUploadFolderBtnId").attr("folderId");
	}else{
		// 这最后回去当前分类的文件夹
		folderId =  $("#file-breadcrumbs li.last").attr("data-folder-id");
	}
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileNameExist?callback=?';
	var data = {"companyId":g_companyId, "classId":folderId, "fileName":fileName};
	var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
			function(json) {
				$("#uploadBtnId").removeClass("disabled");
				var newFileName = json.newFileName;
//				if (newFileName == "") {
//					$("div.fileNameExist").addClass("hidden");
//				} else {
//					$("div.fileNameExist").removeClass("hidden");
//					$("div.fileNameExist .newFileName").html(newFileName);
//					onlineUploadArray[index].name = newFileName;
//				}
	});
}
