var attachMentListCurPage = "";
//邮箱功能区只有一个进度条
var spinner;
var emailAction = {
		/**  基础URL  **/
		baseUrl: window.onlinefilePath,
		getAttachmentByEmail:function(userid,emailAddress,pageIndex) {
			if(spinner) spinner.stop();
			
			//隐藏关键字搜索框内的内容
			$("#searchEmailAttachment").val("");
			$("#emailsearchClear").addClass("hidden");
			
			spinner = new Spinner({color:'#58a2d8',top:'30%',msgText:'读取邮件...',msgTop:'34%',msgLeft:'40%'}).spin(document.getElementById('attachmentList_scroller'));
			var url = emailAction.baseUrl+'/rest/onlinefile_emailws/getAttachmentByEmail?callback=?';
			var data = {"userid":userid,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,"emailAddress":emailAddress,pageIndex:pageIndex,pageLimit:"20"};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				spinner.stop();
				if($("#titleForEmailAttachMentEmpty").css("display") == "block" || $("#logoForEmailEmpty").css("display") == "block" || $("#emailContentText").css("display") == "block" ){
					$("#titleForEmailAttachMentEmpty,#logoForEmailEmpty,#emailContentText").hide();
					$("#attachmentList_scroller").css("margin-top",0);
				}
				if(json.emailAttechMents == ""){
					$("#titleForEmailListEmpty").css({"margin-top":$("#attachmentList_scroller").height()/2}).show();
					$('#attachMentListUl').html("");
					return;
				}else{
					$('#attachMentListUl').html("");
					$("#titleForEmailListEmpty").hide();
				}
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
		getDefaultAttachmentByEmail:function(userid,pageIndex) {
			if(spinner) spinner.stop();
			spinner = new Spinner({color:'#58a2d8',top:'30%',msgText:'读取邮件...',msgTop:'34%',msgLeft:'40%'}).spin(document.getElementById('attachmentList_scroller'));
			
			var url = emailAction.baseUrl+'/rest/onlinefile_emailws/getDefaultAttachmentByEmail?callback=?';
			var data = {"userid":userid,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,pageIndex:pageIndex,pageLimit:"20"};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				spinner.stop();
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
		getEmailList:function(userid){
			if(spinner) spinner.stop();
			spinner = new Spinner({top:'50%',left:'-6%',radius:2,length:2,lines:6,width:2}).spin(document.getElementById('myemailBtn'));
			
			var url = window.onlinefilePath+'/rest/onlinefile_emailws/getEmailList?callback=?';
			var data = {"userid":userid,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				spinner.stop();
				if(json.emails == ""){
					showMsg("没有可用的邮箱,您可以在左下角的邮箱管理功能里面添加一个邮箱!",3);
				}else{
					var menu_flexpane_items_html = template('emailShow_list_item_template', json);
					$('#email_menu_custom #email_menu_items').html(menu_flexpane_items_html);
					$('#email_menu_custom #email_menu_items_scroller').scrollbarCreate();
					
					// 定位
					var tmpH = $('#myemailBtn').height();
				    var tmpW = - $('#email_menu_custom').width() + $('#myemailBtn').width();
				    var offset = $('#myemailBtn').offset();  
				    $("#email_menu_custom").css({ top: offset.top + tmpH + "px", left: offset.left + tmpW });
				}
			});
		},
		getDefaultEmailList:function(userid){
			var url = window.onlinefilePath+'/rest/onlinefile_emailws/getDefaultEmail?callback=?';
			var data = {"userid":userid,'username':window.userName,'companyName':encodeURI(window.companyName, "utf-8")};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				if(json.emails == ""){
					//showMsg("没有可用的邮箱,您可以在左下角的邮箱管理功能里面添加邮箱!",3);
					$("#titleForEmailEmpty").css("margin-top",$("#attachmentList_scroller").height()/2).show();
					$("#titleForEmailEmpty").click(function(){
						$.ajax({
							url : $.appClient.generateUrl({
								ESEMail : 'manageEmailAddress'
							}, 'x'),
							type : 'post',
							success : function(data) {
								$.dialog({
									title : '管理邮箱地址',
									fixed : false,
									resize : false,
									width:660,
									height:500,
									lock : true,
									opacity : 0.1,
									content : data,
									padding : 0
									
								});
							}
						});
					});
				}else{
					var emailAddress = json.defaultEmail;
				 	if(emailAddress.length>18){
				 		emailAddress = emailAddress.substring(0,18);
				 	}
				 	$("#dropdown").css("width",150);
				 	$("#myemailBtn").html(emailAddress+"<i class='iconfa-caret-down' style='margin-left: 5px;'></i>").attr("title",json.defaultEmail).css({"width":150,"padding-right":"0px"});

				}
			});
		},
		cacheAllEmailAttachments:function(userid,email){
			var url = window.onlinefilePath+'/rest/onlinefile_emailws/cacheAllEmailAttachments?callback=?';
			var data = {"userid":userid,"email":email};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				
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
		isSynEmailAttachMent:function(userid,email){
			if(spinner) spinner.stop();
			spinner = new Spinner({color:'#58a2d8',top:'30%',msgText:'正在努力同步邮箱......',msgTop:'34%',msgLeft:'70px'}).spin(document.getElementById('attachmentList_scroller'));
			var url = window.onlinefilePath+'/rest/onlinefile_emailws/isSynEmailAttachMent?callback=?';
			var data = {"userid":userid,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,"email":email};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				spinner.stop();	
				if(json.success == "true"){
					showMsg(json.msg,1);
					emailAction.getAttachmentByEmail(userid,email,1);
				}else{
					showMsg(json.msg,3);
				}
			});
		},
		getEmailAttachMentInfoByEmail:function(userid,email,emailIndex,pageIndex,pageLimit){
			if(spinner) spinner.stop();
			spinner = new Spinner({color:'#58a2d8',top:'30%',msgText:'正在努力读取邮件内容...',msgTop:'34%',msgLeft:'70px'}).spin(document.getElementById('attachmentList_scroller'));
			
			var url = window.onlinefilePath+'/rest/onlinefile_emailws/getEmailAttachMentInfoByEmail?callback=?';
			var data = {"userid":userid,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,"emailAddress":email,emailIndex:emailIndex,pageIndex:pageIndex,pageLimit:pageLimit};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				spinner.stop();
				if(json.emailAttechMents == ""){
					$("#titleForEmailAttachMentEmpty").css("margin-top",$("#attachmentList_scroller").height()/2+200).show();
					$("#logoForEmailEmpty,#emailContentText").show();
					if(json.emailText == "<html><body><P>&nbsp;</P></body></html>"){
						json.emailText = "<div style = 'margin-top:120px;'><font color='#58a2d8' style = 'margin-left:10px;' >发件人太懒了,什么邮箱内容都没给您留下!</font></div>";
					}
					$("#emailContentTextContainer").html(json.emailText).css("height",$("#emailContentTextContainer").height());
					$('#emailContentText').scrollbarCreate();
					$('#attachMentListUl').html("");
				}else{
					$('#attachMentListUl').html(template('emailSingleAttachMents_list_item_template', json));
					/**  绑定当前的hover事件  **/
					$(".emaiAttatchmentsBorder").hover(function() {
							 var topOff = $(this).offset().top+35;
							 $(this).find(".titleForLi").show();
							 $(this).find(".titleForLi").css("top",topOff);
							 $(this).find(".arrowRightIdCls").css("top",topOff+45).show();
							 }, function() {
							  $(this).find(".titleForLi").hide();
							  $(this).find(".arrowRightIdCls").hide();
							 });
					$('#attachmentList_scroller').css({"width":250,"margin-top":370,height:$(window).height()-442}).scrollbarCreate();
					/**  此处处理分页条  **/
					//$('#email_page_items').html(template('emailAttachMents_page_template', json));
					$("#logoForEmailEmpty,#emailContentText").show();
					$("#emailContentTextContainer").html(json.emailText).css("height",$("#emailContentTextContainer").height());
					$('#emailContentText').scrollbarCreate();
				}
			});
		},
		searchEmialAttachsByKeyWord:function(userid,emailAddress,searchKeyWord,pageIndex){
			if(spinner) spinner.stop();
			spinner = new Spinner({color:'#58a2d8',top:'30%',msgText:'搜索邮件...',msgTop:'34%',msgLeft:'40%'}).spin(document.getElementById('attachmentList_scroller'));
			
			var url = emailAction.baseUrl+'/rest/onlinefile_emailws/searchEmailAttachment?callback=?';
			var data = {"userid":window.userId,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,"searchKeyWord":searchKeyWord,"email":emailAddress,
	    			"pageIndex":pageIndex,"pageLimit":20};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				spinner.stop();
				if($("#titleForEmailAttachMentEmpty").css("display") == "block" || $("#logoForEmailEmpty").css("display") == "block" || $("#emailContentText").css("display") == "block" ){
					$("#titleForEmailAttachMentEmpty,#logoForEmailEmpty,#emailContentText").hide();
					$("#attachmentList_scroller").css("margin-top",0);
				}
				$('#attachMentListUl').html(template('emailAttachMents_list_item_template', json));
				/**  绑定当前的hover事件  **/
				$(".contaier li").hover(function() {
						 var topOff = $(this).offset().top+55;
						 $(this).find(".titleForLi").show();
						 $(this).find(".titleForLi").css("top",topOff);
						 $(this).find(".arrowRightIdCls").css("top",topOff+25).show();
						 }, function() {
						  $(this).find(".titleForLi").hide();
						  $(this).find(".arrowRightIdCls").hide();
						 });
				$('#attachmentList_scroller').scrollbarCreate();
				/**  此处留给处理分页条  **/
				$('#email_page_items').html(template('emailAttachMents_page_template', json));
				$('#emailPageingTotalInput').attr("value",json.emailAttechMentPages);
			});
		},
		downloadAttachMent:function(email,emailIndex,attachMentIndex,emailUploadUrl,classId,companyId){
			if(spinner) spinner.stop();
			spinner = new Spinner({color:'#58a2d8',top:'30%',msgText:'正在努力下载附件中...',msgTop:'36%',msgLeft:'70px'}).spin(document.getElementById('emailContentText'));
			var url = emailAction.baseUrl+'/rest/onlinefile_emailws/downloadAttachMent?callback=?';
			var data = {"userid":window.userId,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,"email":email,"emailIndex":emailIndex,"attachMentIndex":attachMentIndex,"emailUploadUrl":emailUploadUrl,"classId":classId,"companyId":companyId};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
				function(json){
				spinner.stop();
				if(json.success == "true"){
					showMsg("亲,我们已经将该文档同步到了您的左侧的文档中了!",1);
					documentCenter.getFileList(classId,"0");
				}else{
					showMsg("亲,文件服务器似乎出了点小问题!",3);
				}
			});
		}
}
/**  是否显示邮箱控制 **/
$("#showAndClosrBtn").click(function(){
	$("#flyingchat").animate({width:$(window).width()-300},'slow',function(){
		$("#emailAllSection").css("display","none");
		$("#emailDivShow").animate({width:30},'slow');
		setCookie("isShowEMailSection","false");
	}) ;
		
});
/**  下拉菜单控制 **/
$("#emailDivShow").click(function(){
	$("#emailAllSection").css("display","");
	$("#flyingchat").animate({width:$(window).width()-550},'slow',function(){
		$("#emailDivShow").animate({width:0},'slow');
		setCookie("isShowEMailSection","true");
	}) ;
});
/**  查询  **/
function searchEmialAttachFun(){
    		var searchKeyWord = $('#searchEmailAttachment').val();
    		var emailAddress = $('#myemailBtn').attr("title");
    		var pageIndex = $("#pageIndex_email").text().replace("第","").replace("页","").trim();
    		emailAction.searchEmialAttachsByKeyWord(window.userId,emailAddress,searchKeyWord,pageIndex);
}

/**   查询当前用户的email的文档  **/
$('#searchEmailAttachment').bind('keypress',function(event){
	if(event.keyCode == "13")    
    {
    	var id=$("input:focus").attr("id");
    	if(id == "searchEmailAttachment"){
    		$("#pageIndex_email").html("第1页<i class='iconfa-caret-down' style = 'margin-left:6px;'></i>");
    		searchEmialAttachFun();
    	}
    }
});
/** 还是查询  **/
$('#emailSearchLogo').click(function(){
	searchEmialAttachFun();
});

//控制清除搜索关键字按钮的显示
$("#searchEmailAttachment").keyup(function(){
	if ($(this).val().length > 0) {
		$("#emailsearchClear").removeClass("hidden");
	} else {
		$("#emailsearchClear").addClass("hidden");
	}
	return false;
});

$("#emailsearchClear").click(function(){
	var emailAddress = $('#myemailBtn').attr("title");
 	emailAction.getAttachmentByEmail(window.userId,emailAddress,1);
 	emailAction.cacheAllEmailAttachments(window.userId,emailAddress);
});


// 展示邮箱列表
$('#myemailBtn').click(function() {
	if ($('#email_menu_custom').length == 0) {
		var menu_html = template('email_menu_template', {});
		$('#file_maincontent').append(menu_html);
		emailAction.getEmailList(g_userId);
	} else {
		$('#email_menu_custom').remove();
	}
    return false;
});

/** 右上角邮箱选择事件  **/
$('#file_maincontent').on('click','#email_menu_items li',function(){
	
	var emailAddress = $(this).text().trim();
 	emailAction.getAttachmentByEmail(window.userId,emailAddress,1);
 	emailAction.cacheAllEmailAttachments(window.userId,emailAddress);
 	if(emailAddress.length>18){
 		emailAddress = emailAddress.substring(0,18);
 	}
 	$("#dropdown").css("width",150);
 	$("#myemailBtn").html(emailAddress+"<i class='iconfa-caret-down' style='margin-left: 5px;'></i>").attr("title",$(this).text().trim()).css({"width":150,"padding-right":"0px"});
});

/**  下载到本地  **/
$("#attachMentListUl").on('click','.downloadEmailAttach',function(){
	var classId = $('#selectFileClassId').val();
	//跟节点提示选择分类
	if(classId==1){
		showMsg("请在左侧选择文档分类!",2);
	}
	if($("#cantUpload").css("display")!="none"){
		showMsg("对不起,当前左侧文档分类下无权限上传!",2);
	}else{
		var offsetTop = $(this).offset().top;
		var offsetRight = $(this).offset().right+80;
		var right_Doc = $(window).width()-150;
		$(".emailAnimateCls").css({right:offsetRight,top:offsetTop}).show();
		$(".emailAnimateCls").animate({right:right_Doc,top:"200px"},1000,function(){
			$(this).css({position:"absolute",right:0,top:0,"display":"none"});
		});
		var emailAddress = $('#myemailBtn').attr("title");
		var emailUploadUrl = window.uploadUrl;
		if(emailUploadUrl=='noFileStoreServer'){
			showMsg("亲,您的文件服务器好像不工作了呢!",2);
			return ;
		}
		var tmpIndex = $(this).parent().attr("value");
		//showMsg("正在努力为您下载!",1);
		emailAction.downloadAttachMent(emailAddress,tmpIndex.split("|")[0],tmpIndex.split("|")[1],emailUploadUrl,classId,g_companyId);
	}
	
	
});

/**  分享到组  **/
$("#attachMentListUl").on('click','.shareToGroup',function(){ 
	$.ajax({
		url : $.appClient.generateUrl({
			ESEMail : 'shareToGroup'
		}, 'x'),
		type : 'post',
		success : function(data) {
			$.dialog({
				title : '分享到组',
				fixed : false,
				resize : false,
				width:250,
				height:400,
				lock : true,
				opacity : 0.1,
				content : data,
				padding : 0,
				okVal : '分享',
				ok : function() {
					$.dialog.notice({icon : 'succeed',content : '分享成功',title : '3秒后自动关闭',time : 3});
				},
				cancelVal: '关闭',
			    cancel: true
				
			});
		}
	});
});

/** 底部分页条事件     **/

$('#pages-menu-attachMent').on('click','ul li a', function() {
	var emailAddress = $('#myemailBtn').attr("title");
	$("#pageIndex_email").html($(this).text()+"<i class='iconfa-caret-down' style = 'margin-left:6px;'></i>");
	var pageIndex = $(this).text().replace("第","").replace("页","").trim();
	emailAction.getAttachmentByEmail(window.userId,emailAddress,pageIndex);
});

/**  上一页 下一页事件  **/
$('#previousBtnEmail,#nextBtnEmail').on('click', function() {
	var curPage = $("#pageIndex_email").text().replace("第","").replace("页","").trim();
	var searchKeyWord = $('#searchEmailAttachment').val();
	curPage = parseInt(curPage);
	var total = $('#emailPageingTotalInput').attr("value");
	var emailAddress = $('#myemailBtn').attr("title");
	if ($(this).hasClass('previous')) { // 上一页
		if (curPage == 1) { return; }
		curPage-=1;
	} else { // 下一页
		if (curPage == total) { return; }
		curPage+=1;
	}
	if(curPage>9){
		$("#nextBtnEmail").css("margin-left","15px");
	}else{
		$("#nextBtnEmail").css("margin-left","20px");
	}
	$("#pageIndex_email").html("第"+curPage+"页"+"<i class='iconfa-caret-down' style = 'margin-left:6px;'></i>");
	
	var pageIndex = $("#pageIndex_email").text().replace("第","").replace("页","").trim();
	emailAction.searchEmialAttachsByKeyWord(window.userId,emailAddress,searchKeyWord,pageIndex);
	//emailAction.getAttachmentByEmail(window.userId,emailAddress,curPage);
});


/**   获取邮件的附件功能  **/
$('#attachMentListUl').on('click','li',function(){
	var emailAddress = $('#myemailBtn').attr("title");
	var emailIndex = $(this).attr("value");
	var pageIndex = $("#pageIndex_email").text().replace("第","").replace("页","").trim();
	attachMentListCurPage = pageIndex;
	emailAction.getEmailAttachMentInfoByEmail(window.userId,emailAddress,emailIndex,1,20);
});

/**  返回邮箱列表功能 **/
$("#logoForEmailEmpty").click(function(){
	var emailAddress = $('#myemailBtn').attr("title");
	var searchKeyWord = $('#searchEmailAttachment').val();
	$("#titleForEmailAttachMentEmpty").hide();
	$("#logoForEmailEmpty,#emailContentText").hide();
	$("#attachmentList_scroller").css({"margin-top":0,"height":$(window).height() - 113});
	emailAction.searchEmialAttachsByKeyWord(window.userId,emailAddress,searchKeyWord,attachMentListCurPage);

});

/**  分页条弹出事件 **/
$('.pagingBarForAttachMent .pages').on('click', function() {
	var pageMenuAttatchMent = $(this).find('#pages-menu-attachMent');
	if(pageMenuAttatchMent.css("display")=="none") {
	    var tmpH = -pageMenuAttatchMent.height() - 10;
	    var tmpW = -pageMenuAttatchMent.width() * 0.25;
	    //设置弹出层位置，获取相对位置
	    var offset = $(this).position();
	    pageMenuAttatchMent.css({ top: offset.top + tmpH + "px", left: offset.left + tmpW }); 
	    //动画显示  
	    pageMenuAttatchMent.show(0);
	    $('#pages-menu-attachMent').scrollbarCreate();
	}else{
		$(this).find('#pages-menu-attachMent').hide(0);
	}
    return false;
});

/** 是否隐藏邮箱事件  **/
$('#emailtitle').mouseover(function () {
	$("#showAndClosrBtn").css("display","block");
}).mouseout(function () {
	$("#showAndClosrBtn").css("display","none");
});

/**  同步邮箱事件  **/
$("#syncEmailListBtn").click(function(){
	var emailAddress = $('#myemailBtn').attr("title");
	if(emailAddress.indexOf("我的邮箱")>-1){
		showMsg("亲,没有可以同步的邮箱,您不是还没添加呢?",3);
		return ;
	}
	$.dialog({
		title : "同步邮箱",
		content: "您确定要同步该邮箱吗？可能会占用您几分钟时间！",
    	ok: function() {
    		emailAction.isSynEmailAttachMent(window.userId,emailAddress);
    	},
    	lock: true,
    	cancelVal:'取消',
    	cancel: true
    });
	
});
/**   将是否显示邮件保存在cookies中，默认隐藏 **/
if(getCookie("isShowEMailSection")==null || getCookie("isShowEMailSection")=="false"){
	$("#flyingchat").width($(window).width()-300);
	$("#emailAllSection").css("display","none");
	$("#emailDivShow").animate({width:30},'slow');
}

//保存 Cookie 
function setCookie ( name, value ) 
{ 
	expires = new Date(); 
	expires.setTime(expires.getTime() + (1000 * 86400 * 365)); 
	document.cookie = name + "=" + escape(value) + "; expires=" + expires.toGMTString() + "; path=/"; 
} 

// 获取 Cookie 
function getCookie ( name ) 
{ 
	cookie_name = name + "="; 
	cookie_length = document.cookie.length; 
	cookie_begin = 0; 
	while (cookie_begin < cookie_length) 
	{ 
	value_begin = cookie_begin + cookie_name.length; 
	if (document.cookie.substring(cookie_begin, value_begin) == cookie_name) 
	{ 
	var value_end = document.cookie.indexOf ( ";", value_begin); 
	if (value_end == -1) 
	{ 
	value_end = cookie_length; 
	} 
	return unescape(document.cookie.substring(value_begin, value_end)); 
	} 
	cookie_begin = document.cookie.indexOf ( " ", cookie_begin) + 1; 
	if (cookie_begin == 0) 
	{ 
	break; 
	} 
	} 
	return null; 
} 

// 清除 Cookie 
function delCookie ( name ) 
{ 
	var expireNow = new Date(); 
	document.cookie = name + "=" + "; expires=Thu, 01-Jan-70 00:00:01 GMT" + "; path=/"; 
} 




