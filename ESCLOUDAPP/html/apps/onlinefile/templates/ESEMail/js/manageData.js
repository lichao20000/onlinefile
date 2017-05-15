
var emailManage = {
		/**  基础URL  **/
		baseUrl: window.onlinefilePath,
		getEmailList:function(userid) {
			// jQuery跨域获取
			var url = this.baseUrl+'/rest/onlinefile_emailws/getEmailList?callback=?';
			var data = {"userid":userid,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				$('#emailListsContainerForScro').html(template('email_list_item_template', json));
				$('#leftSection').scrollbarCreate();
				refreshEmailSetting();
				
				if(json.emails==""){  //禁用右侧保存按钮 设置默认邮箱按钮
					$("#setAsDefaultEmail").attr("disabled", true); 
					//清除邮箱设置内容
    				$("#emailId").val("");
    				$("#emailAddressInput_set").val("");
    				$("#emailPasswordInput_set").val("");
    				$("#receiveEmailServer_set").val("");
    				$("#receiveEmailServerPort_set").val("");
    				$("#sendEmailServer_set").val("");
    				$("#sendEmailServerPort_set").val("");
				}else{
					$("#setAsDefaultEmail").attr("disabled", false); 
				}
			});
		},
		getDefaultAttachmentByEmail:function(userid,pageIndex) {
			var url = emailAction.baseUrl+'/rest/onlinefile_emailws/getDefaultAttachmentByEmail?callback=?';
			var data = {"userid":userid,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,pageIndex:pageIndex,pageLimit:"20"};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				$('#titleForEmailEmpty').css("display","none");
				$('#attachMentListUl').html(template('emailAttachMents_list_item_template', json));
				/**  绑定当前的hover事件  **/
				$(".contaier li").hover(function() {
						 var topOff = $(this).offset().top+35;
						 $(this).find(".titleForLi").show();
						 $(this).find(".titleForLi").css("top",topOff);
						 $(this).find(".arrowRightIdCls").css("top",topOff+45).show();
						 }, function() {
						  $(this).find(".titleForLi").hide();
						  $(this).find(".arrowRightIdCls").hide();
						 });
				$('#attachmentList_scroller').scrollbarCreate();
				/**  此处处理分页条  **/
				$('#email_page_items').html(template('emailAttachMents_page_template', json));
				$('#emailPageingTotalInput').attr("value",json.emailAttechMentPages);
			});
			
		},
		cacheDefaultAllEmailAttachments:function(userid,email){
			var url = window.onlinefilePath+'/rest/onlinefile_emailws/cacheDefaultAllEmailAttachments?callback=?';
			var data = {"userid":userid,"email":email};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				
			});
		},
		getDefaultEmailList:function(userid){
			var url = window.onlinefilePath+'/rest/onlinefile_emailws/getDefaultEmail?callback=?';
			var data = {"userid":userid,'username':window.userName,'companyName':encodeURI(window.companyName, "utf-8")};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
					var emailAddress = json.defaultEmail;
					if(emailAddress == ""){
					 	$("#myemailBtn").html("我的邮箱<i class='iconfa-caret-down' style='margin-left: 5px;'></i>").attr("title",$(this).text().trim()).css({"width":80,"padding-right":"10px"});
					 	$("#dropdown").css("width",80);
					}else{
						if(emailAddress.length>18){
					 		emailAddress = emailAddress.substring(0,18);
					 	}
					 	$("#dropdown").css("width",150);
					 	$("#myemailBtn").html(emailAddress+"<i class='iconfa-caret-down' style='margin-left: 5px;'></i>").attr("title",$(this).text().trim()).css({"width":150,"padding-right":"0px"});
					}
			});
		}
}

$('.username-label, .password-label').animate({
	opacity : "0.4"
}).click(function() {
	var thisFor = $(this).attr('for');
	$('.' + thisFor).focus();
});

$('.username').focus(function() {
	
}).blur(function() {

	if ($(this).val() == "") {
		$(this).val() == "username";
		$('.username-label').animate({
			opacity : "0.4"
		}, "fast");
	}
});

$('.password').focus(function() {

}).blur(function() {

	if ($(this).val() == "") {
		$(this).val() == "password";
		$('.password-label').animate({
			opacity : "0.4"
		}, "fast");
	}
});



$("#addEmailBtn").click(function() {
	autoAddEmailAddress();
});


$("#delEmailBtn").click(function() {
	var emailSelected = $('#leftSection .emailList_selected').text();
	if(emailSelected==""){
		showMsg("请选择要删除的邮箱",'2');
		return;
	}
	$.dialog({
		title : "删除文件",
		content: "您确定要删除该邮箱吗？",
    	ok: function() {
    		var delEmailurl = $.appClient.generateUrl({ESEMail : 'deleteEmail'}, 'x');
    		$.post(delEmailurl,{email:emailSelected,userid:window.userId,companyName:window.companyName,ip:window.ip,username:window.userName}, function(result){
    			var jsonDX = eval('(' + result + ')');
    			if(jsonDX.success == "true"){
    				showMsg(jsonDX.msg);
    				refreshEmaiList();
    				refreshEmailAttachMents();
    			}else{
    				showMsg(jsonDX.msg,'2');
    			}
    		});
    		return true;
    	},
    	lock: true,
    	cancelVal:'取消',
    	cancel: true
    });
});


/**   清空邮箱  **/

$("#delAllEmailBtn").click(function() {
	var emailSelected = $('#leftSection .emailList_selected').text();
	if(emailSelected==""){
		showMsg("没有需要清空的邮箱",'2');
		return;
	}
	$.dialog({
		title : "清空邮箱",
		content: "您确定要清空所有邮箱吗？",
    	ok: function() {
    		var delAllEmailurl = $.appClient.generateUrl({ESEMail : 'deleteAllEmail'}, 'x');
    		$.post(delAllEmailurl,{userid:window.userId,companyName:window.companyName,ip:window.ip,username:window.userName}, function(result){
    			var jsonDX = eval('(' + result + ')');
    			if(jsonDX.success == "true"){
    				showMsg(jsonDX.msg);
    				refreshEmaiList();
    				refreshEmailAttachMents();
    				
    				//清除邮箱设置内容
    				$("#emailId").val("");
    				$("#emailAddressInput_set").val("");
    				$("#emailPasswordInput_set").val("");
    				$("#receiveEmailServer_set").val("");
    				$("#receiveEmailServerPort_set").val("");
    				$("#sendEmailServer_set").val("");
    				$("#sendEmailServerPort_set").val("");
    			}else{
    				showMsg(jsonDX.msg,'2');
    			}
    		});
    		return true;
    	},
    	lock: true,
    	cancelVal:'取消',
    	cancel: true
    });
});

function autoAddEmailAddress(email, password){
	$.ajax({
		url : $.appClient.generateUrl({
			ESEMail : 'addEmailAddress'
		}, 'x'),
		type : 'post',
		success : function(data) {
			var emailAddDialog = $.dialog({
				title : '添加邮箱地址',
				fixed : false,
				resize : false,
				width : 150,
				height : 80,
				lock : true,
				opacity : 0.1,
				content : data,
				padding : 0,
				id :'addEmailAutoWindow',
				okVal : '创建',
				ok : function() {
					
					var formEmail=$('#addEmailAutoForm');
					if (!formEmail.validate()) {
						return false;
					}
					var email = $("#emailAutoInput").val();
					var password = $("#password").val();
    				var addEmailurl = $.appClient.generateUrl({ESEMail : 'addEmail'}, 'x');
    				$.post(addEmailurl,{email:email,password:password,userid:window.userId,companyName:window.companyName,ip:window.ip,username:window.userName}, function(result){
    					var jsonDX = eval('(' + result + ')');
    					if(jsonDX.success == "true"){
    						showMsg(jsonDX.msg);
    						emailAddDialog.close();
    						emailManage.getEmailList(window.userId);
    						refreshEmailAttachMents();
    					}else{
    						showMsg(jsonDX.msg,'2');
    					}
    					
    				});
    				return false ;
				},
				cancelVal : '关闭',
				cancel : true,
				button : [ {
					id : 'imageFile',
					name : '手动添加',
					callback : function() {
						var email = $("#emailAutoInput").val();
						var password = $("#password").val();
						manualAddEmailAddress(email,password);
					}
				} ],
				init:function() {  /** 初始化地址和密码 **/
					$("#emailAutoInput").val(email);
					$("#password").val(password);
				}
			});
		}
	});
}
/**  该function 服务于手动添加邮箱  **/
function manualAddEmailAddress(email, password) {
	$.ajax({
		url : $.appClient.generateUrl({
			ESEMail : 'addEmailAddressManual'
		}, 'x'),
		type : 'post',
		success : function(data) {
			/** 跳转到手动设置页面 * */
			var emailManualAddDialog = $.dialog({
				title : '添加邮箱地址',
				fixed : false,
				resize : false,
				width : 450,
				height : 80,
				lock : true,
				opacity : 0.1,
				content : data,
				padding : 0,
				okVal : '创建',
				ok : function() {
					var formEmail=$('#addEmailManualForm');
					if (!formEmail.validate()) {
						return false;
					}
					var email = $("#emailManualInput").val();
					var password = $("#passwordManualInput").val();
					var popServerInput = $("#popServerInput").val();
					var popSSLPortInput = $("#popSSLPortInput").val();
					var smtpServerInput =  $("#smtpServerInput").val();
					var smtpSSLPortInput =  $("#smtpSSLPortInput").val();
					
    				var addEmailurl = $.appClient.generateUrl({ESEMail : 'addEmailManual'}, 'x');
    				$.post(addEmailurl,{email:email,password:password,userid:window.userId,companyName:window.companyName,username:window.userName,ip:window.ip,popServerInput:popServerInput,popSSLPortInput:popSSLPortInput,smtpServerInput:smtpServerInput,smtpSSLPortInput:smtpSSLPortInput}, function(result){
    					var jsonDX = eval('(' + result + ')');
    					if(jsonDX.success == "true"){
    						$.dialog.notice({
        						icon : 'succeed',
        						content : jsonDX.msg,
        						title : '3秒后自动关闭',
        						time : 3
        					});
    						emailManualAddDialog.close();	
    						emailManage.getEmailList(window.userId);
    						refreshEmailAttachMents();
    					}else{
    						$.dialog.notice({
        						icon : 'error',
        						content : jsonDX.msg,
        						title : '3秒后自动关闭',
        						time : 3
        					});
    					}
    					
    				});
    				return false ;
				
				},
				cancelVal : '关闭',
				cancel : true,
				button : [ {
					id : 'imageFile',
					name : '返回',
					callback : function() {
						var email = $("#emailManualInput").val();
						var password = $("#passwordManualInput").val();
						autoAddEmailAddress(email, password);
					}
				} ],
				init:function() {  /** 初始化地址和密码 **/
					$("#emailManualInput").val(email);
					$("#passwordManualInput").val(password);
				}
			});
		}
	});
}

$("#saveEmailBaseSetting").click(function() {
	
	var formEmail=$('#saveEmailSettingForm');
	if (!formEmail.validate()) {
		return false;
	}
	var id = $("#emailId").val();
	var email = $("#emailAddressInput_set").val();
	var password = $("#emailPasswordInput_set").val();
	var receiverserver = $("#receiveEmailServer_set").val();
	var receiveserverport = $("#receiveEmailServerPort_set").val();
	var sendserver =  $("#sendEmailServer_set").val();
	var sendserverport =  $("#sendEmailServerPort_set").val();
	if(id!=""){
		var saveEmailSettingUrl = $.appClient.generateUrl({ESEMail : 'saveEmailSetting'}, 'x');
		$.post(saveEmailSettingUrl,{id:id,email:email,password:password,receiverserver:receiverserver,receiveserverport:receiveserverport,sendserver:sendserver,sendserverport:sendserverport,userid:window.userId,companyName:window.companyName,ip:window.ip,username:window.userName}, function(result){
			var jsonDX = eval('(' + result + ')');
			if(jsonDX.success == "true"){
				showMsg(jsonDX.msg,'1');
			}else{
				showMsg(jsonDX.msg,'2');
			}
		});
	}else{
		var addEmailurl = $.appClient.generateUrl({ESEMail : 'addEmailManual'}, 'x');
		$.post(addEmailurl,{email:email,password:password,userid:window.userId,companyName:window.companyName,username:window.userName,ip:window.ip,popServerInput:receiverserver,popSSLPortInput:receiveserverport,smtpServerInput:sendserver,smtpSSLPortInput:sendserverport}, function(result){
			var jsonDX = eval('(' + result + ')');
			if(jsonDX.success == "true"){
				showMsg(jsonDX.msg,'1');
				emailManage.getEmailList(window.userId);
				refreshEmailAttachMents();
			}else{
				showMsg(jsonDX.msg,'2');
			}
			
		});
	}
	
});


$("#setAsDefaultEmail").click(function() {
	var email = $("#emailAddressInput_set").val();
	var setAsDefaultEmail = $.appClient.generateUrl({ESEMail : 'setAsDefaultEmail'}, 'x');
	$.post(setAsDefaultEmail,{email:email,userid:window.userId,companyName:window.companyName,username:window.userName,ip:window.ip}, function(result){
		var jsonDX = eval('(' + result + ')');
		if(jsonDX.success == "true"){
			showMsg(jsonDX.msg,'1');
			refreshEmailAttachMents();
		}else{
			showMsg(jsonDX.msg,'2');
		}
	});
});

$("#settingSectionBtn").click(function() {
	$("#serverSetting").css("display", "none");
	$("#settingSection").css("display", "");
});

$('#emailListsContainerForScro').on('click', 'div',function() {
	$("#emailListsContainerForScro div").removeClass("emailList_selected");
	$("#emailListsContainerForScro div").addClass("emailList");
	$(this).addClass("emailList_selected");
	/**    点击本次div时候从后台获取数据，然后渲染给右侧form表单中   **/
	getEmailSettingByEmailAddress($(this).text());
});



function getEmailSettingByEmailAddress(email){
	var getEmailSettigngUrl = $.appClient.generateUrl({ESEMail : 'getEmailSettingByEmail'}, 'x');
	$.post(getEmailSettigngUrl,{email:email,userid:window.userId,companyName:window.companyName,ip:window.ip,username:window.userName}, function(result){
		var jsonDX = eval('(' + result + ')');
		if(jsonDX.success == "true"){
			$("#emailId").val(jsonDX.id);
			$("#emailAddressInput_set").val(jsonDX.email);
			$("#emailPasswordInput_set").val(jsonDX.password);
			$("#receiveEmailServer_set").val(jsonDX.receiverserver);
			$("#receiveEmailServerPort_set").val(jsonDX.receiveserverport);
			$("#sendEmailServer_set").val(jsonDX.sendserver);
			$("#sendEmailServerPort_set").val(jsonDX.sendserverport);
		}else{
			showMsg(jsonDX.msg);
		}
	});
}





$(function(){
	refreshEmaiList();
});

function refreshEmaiList(){
	emailManage.getEmailList(window.userId);
}

function refreshEmailAttachMents(){
	emailManage.getDefaultAttachmentByEmail(window.userId,1);
	emailManage.cacheDefaultAllEmailAttachments(window.userId);
	emailManage.getDefaultEmailList(window.userId);
}

function refreshEmailSetting(){
	if($(".emailList_selected").get(0) != undefined){
		getEmailSettingByEmailAddress($(".emailList_selected").html());
	}
}
