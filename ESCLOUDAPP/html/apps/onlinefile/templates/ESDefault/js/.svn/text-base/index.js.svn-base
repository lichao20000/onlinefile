/*
 * 控制台系统首页
 * 
 */
 
function GuideFirst(){
	if($("#isFirLogin").val()*1==1){
		//begin  首层登录功能提示   
		var steplist = $.WebPageGuide();
		steplist.newGuidStep(prompt_ids[0],prompt_content[0],0);
		steplist.startGuide();
		//end
	}
}
$(function(){
	setTimeout(GuideFirst,"300");
	
     $(window).resize(function(){
    	 $("#online_left_bottom").css("height",$('#mainContentContainer').height()-73);
    	 $('#online_center').css("width", ($('#mainContentContainer').width()-550)+"px") ;
    	 $(".receiveMessageDiv").css("height", $('#mainContentContainer').height()-197);
    	 resetFileListHeight();
    	 resetEmailListHeight();
    	 $('#filelist_scroller').scrollbarUpdate() ;
    	 $('#attachmentList_scroller').scrollbarUpdate() ;
     });
});
/** 年月日的生成 * */
function startTime() {
	var today = new Date();
	var h = today.getHours();
	var m = today.getMinutes();
	var s = today.getSeconds();
	var day = today.getDate();
	var month = today.getMonth() + 1;
	var week = today.getDay();
	m = checkTime(m);
	s = checkTime(s);
	$("#daySection").text(day);
	$("#monthSection").text(month + "月");
	$("#weekSection").text("星期" + week);

	// IE8下报错，以后再处理吧，longjunhao 20150303
//	document.getElementById('hourSection').innerHTML = h + ":" + m + ":" + s;
	t = setTimeout('startTime()', 500);
}

function checkTime(i) {
	if (i < 10) {
		i = "0" + i
	}
	return i
}

/** liuhezeng  控制滚动条横向滚动 **/
var Browser = function(){
	var d_ = document,n_ = navigator,t_ = this,s_= screen;
	var b = n_.appName;
	var ua = n_.userAgent.toLowerCase();
	t_.name = "Unknow";
	 
	t_.safari = ua.indexOf("safari")>-1;  // always check for safari & opera
	t_.opera = ua.indexOf("opera")>-1;    // before ns or ie
	t_.firefox = !t_.safari && ua.indexOf('firefox')>-1; // check for gecko engine 
	t_.ns = !t_.firefox && !t_.opera && !t_.safari && (b=="Netscape");
	t_.ie = !t_.opera && (b=="Microsoft Internet Explorer");
	t_.name = (t_.ie ? "IE" : (t_.firefox ? "Firefox" : (t_.ns ? "Netscape" : (t_.opera ? "Opera" : (t_.safari ? "Safari" : "Unknow")))));
}
var brw = new Browser();
var apDiv1 = document.getElementById("mainContentContainer");
var perWidth = apDiv1.clientWidth / 2;
var mouse_wheel = function(e){
	var evt = window.event || e;
	if(evt.detail > 0 || evt.wheelDelta < 0)
		apDiv1.scrollLeft += perWidth;
	else
		apDiv1.scrollLeft -= perWidth;
}
var mouse_wheel_opera = function(e){
	var obj = e.srcElement;
	if(obj == apDiv1){
		mouse_wheel(e);
	}
}
	 
switch(brw.name){
	case "IE":
	case "Safari":
		apDiv1.onmousewheel = mouse_wheel;
		break;
	case "Firefox":
		apDiv1.addEventListener("DOMMouseScroll", mouse_wheel, false);
		break;
	case "Opera":
		document.onmousewheel = mouse_wheel_opera;
		break;
}

$(".centerFunc div").click(function() {
	if($(this).attr("controller") != null || $(this).attr("controller") != undefined){
		var url = {};
		var controller = $(this).attr("controller");
		var action = $(this).attr("action");
		if(typeof controller === "string" && controller.length > 0){
			url[$(this).attr("controller")] = $(this).attr("action");
			window.open($.appClient.generateUrl(url), "_self");
		}
	}
});
	
$("#ControlConsole").height($(window).height()-90);
$( ".subFuncs" ).click(function() {
	var urlLocation = window.location.href+"";
	urlLocation = urlLocation.substring(0,urlLocation.indexOf("order")+14)+"/";
	var srcLocation = urlLocation + $(this).attr("rel");
	$("#ControlConsole").attr("src",srcLocation);
});


$.post(
		$.appClient.generateUrl({ESEMail:'index'},'x'),
		function (data){
			$('#online_rig_top').html(data);
		},
		false
	);
function showFileProPage(){
	var url=$.appClient.generateUrl({ESFileProperties:'filePropMain'},'x');
	$.post(url,{userId:g_userId},function(html){
		$.dialog({
	    	title:'文件属性',
	    	padding:'0',
		    content:html
	    }).show();
	});

}

$("#addEmailAddressBtn").click(function() {
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
			opacity : 0.1,
			content : data,
			padding : 0
			
		}).showModal();
	}
});
});

/**
 * 设置右侧邮件附件列表的高度
 */
function resetEmailListHeight() {
	var h = $('#online_right').height() - 113;
	$('#attachmentList_scroller').height(h);
}

/** xiaoxiong 20150408 用户状态更改相关处理 start **/
$("#connection_status").on("click", function(){
	$(this).css("display", "none") ;
	$("#connection_status_input").css("display", "block") ;
	$("#connection_status_input").select();
});

function updateUserSignature() {
	$("#connection_status_input").css("display", "none") ;
	var SIGNATURE = $.trim($("#connection_status_input").val());
	var oldvalue = $("#connection_status_input").attr("oldvalue") ;
	if(oldvalue == SIGNATURE){
		$("#connection_status").css("display", "block") ;
		return ;
	}
	var spantitle = "";
	if(SIGNATURE.length>14){
		spantitle = SIGNATURE ;
		SIGNATURE = SIGNATURE.substring(0, 14)+"..." ;
	}
	$("#connection_status").html(SIGNATURE==""?"编辑个性签名":SIGNATURE);
	$("#connection_status").attr("title", spantitle);
	$("#connection_status").css("display", "block") ;
	var value = $.trim($("#connection_status_input").val()) ;
	$("#connection_status_input").attr("oldvalue", value);
	$.ajax({
		url:$.appClient.generateUrl({ESUserInfo:'editUserInfo'},'x'),
		type:'POST',
		data:{param:"signature="+value},
		success:function(data){
			var obj = $("#itemuser-"+hex_md5($("#flyingchat").attr("username"))) ;
			obj.attr("itemsignature", value==""?"&nbsp;":value) ;
			obj.find(".userstatus").html(SIGNATURE==""?"&nbsp;":SIGNATURE);
			obj.find(".userstatus").attr("title", spantitle);
		}
	});
}

$("#connection_status_input").on("blur", function(){
	updateUserSignature();
});

$("#connection_status_input").on("keydown", function(){
	var e = event || window.event;
	var keyCode = e.which || e.keyCode;
	if (keyCode == 13) {
		updateUserSignature();
		return false;
	}
});
/** xiaoxiong 20150408 用户状态更改相关处理 end **/

$("#onlineusers").on("click", function(){
	$.ajax({
	    url : $.appClient.generateUrl({ESUserInfo : 'onlineUsers'},'x'),
	    success:function(data){
    		$.dialog({
		    	title:'在线用户查看',
	    	   	fixed:false,
	    	    resize: false,
				opacity : 0.1,
				padding:0,
				width: 700,
				height: 400,
			    content:data
		    }).showModal();
		  },
	      cache:false
	});
});
