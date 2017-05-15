$(function(){
	var browser=navigator.appName 
	var b_version=navigator.appVersion 
	var version=b_version.split(";"); 
	if(version[1]){
	var trim_Version=version[1].replace(/[ ]/g,""); 
	if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE6.0") 
	{ 
		 $("#mask").css("height",$(document).height());  
	     $("#mask").css("width",$(document).width());  
	     $("#mask").show();  
	     $("#browser_ie").show();  
	} 
	else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE7.0") 
	{ 
		 $("#mask").css("height",$(document).height());  
	     $("#mask").css("width",$(document).width());  
	     $("#mask").show();  
	     $("#browser_ie").show();  
	} 
	else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0") 
	{ 
//	alert("IE 8.0"); 
		 $("#mask").css("height",$(document).height());  
	     $("#mask").css("width",$(document).width());  
	     $("#mask").show();  
	     $("#browser_ie").show();  
	}
	}
	/**
	 * XmlHttp factory
	 * @private
	 */
	function XmlHttp() {}

	/**
	 * creates a cross browser compliant XmlHttpRequest object
	 */
	XmlHttp.create = function () {
	  try {
	    if (window.XMLHttpRequest) {
	      var req = new XMLHttpRequest();
	     
	      // some versions of Moz do not support the readyState property
	      // and the onreadystate event so we patch it!
	      if (req.readyState == null) {
		req.readyState = 1;
		req.addEventListener("load", function () {
				       req.readyState = 4;
				       if (typeof req.onreadystatechange == "function")
					 req.onreadystatechange();
				     }, false);
	      }
	     
	      return req;
	    }
	    if (window.ActiveXObject) {
	      return new ActiveXObject(XmlHttp.getPrefix() + ".XmlHttp");
	    }
	  }
	  catch (ex) {}
	  // fell through
	  throw new Error("Your browser does not support XmlHttp objects");
	};

	/**
	 * used to find the Automation server name
	 * @private
	 */
	XmlHttp.getPrefix = function() {
	  if (XmlHttp.prefix) // I know what you did last summer
	    return XmlHttp.prefix;
	 
	  var prefixes = ["MSXML2", "Microsoft", "MSXML", "MSXML3"];
	  var o;
	  for (var i = 0; i < prefixes.length; i++) {
	    try {
	      // try to create the objects
	      o = new ActiveXObject(prefixes[i] + ".XmlHttp");
	      return XmlHttp.prefix = prefixes[i];
	    }
	    catch (ex) {};
	  }
	 
	  throw new Error("Could not find an installed XML parser");
	};
	XmlHttp.bind = function(fn, obj, optArg) {
	    return function(arg) {
	      return fn.apply(obj, [arg, optArg]);
	    };
	  }
	
	function ofuserservice(ofbaseurl, arg, async) {
		// setupRequest must be done after rid is created but before first use in reqstr
		r = XmlHttp.create();
		r.open("POST", ofbaseurl+'/plugins/userService/userservice', async==null?true:async);
	    r.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  //用POST的时候一定要有这句  
		r.onreadystatechange = 
			XmlHttp.bind(function() {
				if (r.readyState == 4) {
				}
			}, this);
		
		if (r.onerror != 'undefined') {
			r.onerror = 
				XmlHttp.bind(function(e) {
					return false;
				}, this);
		}
		r.send(arg);
	}
	//获取链接上的值
	var url = window.location;
	/*-------------------------页面展现并查询数据---------------------------------*/
	var url = $.appClient.generateUrl({ESUserInfo:'showActivate'},'x');
//	var sendTime = $("#sendTime").val();
//	alert(sendTime);
	$.ajax({
		url:url,
		type:'POST',
		data:{id:$("#userid").val(), code:$("#code").val(),companyId:$("#companyid").val(),className:$("#className").val(),classId:$("#classId").val(),sendTime:$("#sendTime").val()},
		success:function(data){
			var success = $.parseJSON(data);
			if(!success.issuccess){
				if(success.disabled){
					$("#alerterror").append("当前激活失效！");
				}else{
					$("#alerterror").append("当前用户已激活！");
				}
				$("#alerterror").css('display','block');
				$("#alerterror").attr('isAccounted','true');
				$("#activateAccount").css("background-color", "#E8E8E8");
				$("#activateAccount").css("cursor", "default;");
			}
			if(success.timeout){
				$("#alerterror").append("已超出激活时间！");
				$("#alerterror").css('display','block');
				$("#alerterror").attr('isAccounted','true');
				$("#activateAccount").css("background-color", "#E8E8E8");
				$("#activateAccount").css("cursor", "default;");
			}
		}
	});
	/*-----------------------表单验证及登陆--------------------------------*/
	var passwordTest = /^\w{6,20}$/;
	var emailaddressZZ = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var nameZZ= /^[\u4e00-\u9fa5a-zA-Z]+$/;
	$("#activateAccount").on('click',function(){
		if($("#alerterror").attr('isAccounted') == "true"){
			return ;
		}
		var username = $("#username").val();
		var fullname = $("#fullname").val();
		var password = $("#password").val();
		var repassword = $("#repassword").val();
		var isOk = true ;
		if($.trim(username)==''){
			$("#usernamealerterror").html("用户名不能为空.");
			$("#usernamealerterror").css('display','block');
			isOk = false ;
		}else if(emailaddressZZ.test(username)==false){
			$("#usernamealerterror").html("用户名必须是符合邮箱格式的字符串,例如admin@flyingsoft.com");
			$("#usernamealerterror").css('display','block');
			isOk = false ;
		} else {
			$("#usernamealerterror").css('display','none');
		}
		if($.trim(fullname)==''){
			$("#fullnamealerterror").html("姓名不能为空.");
			$("#fullnamealerterror").css('display','block');
			isOk = false ;
		}else if(nameZZ.test(fullname)==false){
			$("#fullnamealerterror").html("姓名必须是汉字或字母.");
			$("#fullnamealerterror").css('display','block');
			isOk = false ;
		} else {
			$("#fullnamealerterror").css('display','none');
		}
		if(passwordTest.test($.trim(password))==false){
			$("#passwordalerterror").html("密码只能由字母，数字，下划线组成，长度应在6-20位之间!");
			$("#passwordalerterror").css('display','block');
			isOk = false ;
		}else if($.trim(password).length<6){
			$("#passwordalerterror").html("密码不能小于6位!");
			$("#passwordalerterror").css('display','block');
			isOk = false ;
		} else {
			$("#passwordalerterror").css('display','none');
		}
		if($.trim(repassword)==''){
			$("#repasswordalerterror").html("确认密码不能为空!");
			$("#repasswordalerterror").css('display','block');
			isOk = false ;
		} else {
			$("#repasswordalerterror").css('display','none');
		}
		if($.trim(password)!='' && $.trim(repassword)!=''){
			if($.trim(password)!=$.trim(repassword)){
				$("#repasswordalerterror").html("您两次输入的密码不一致，请重新输入!");
				$("#repasswordalerterror").css('display','block');
				isOk = false ;
			} else {
				$("#repasswordalerterror").css('display','none');
			}
		}
		if(!isOk){
			return ;
		}
		var url = $.appClient.generateUrl({ESUserInfo:'activateAccount'},'x');
		var fullname = $("#fullname").val() ;
		var companyid = $("#companyid").val() ;
		var companyName = $("#companyName").val() ;
		var className = $("#className").val();
		var classId = $("#classId").val();
		//xiayongcai 20151118 通过谁来激活的,也就是发送者
		var sendUserName = $("#sendUserName").val();
		var sendTime = $("#sendTime").val();
		$.ajax({
			url:url,
			type:'POST',
			data:{id:$("#userid").val(), username:username,password:password,companyid:companyid,fullname:fullname,companyName:companyName,className:className,classId:classId,sendUserName:sendUserName,sendTime:sendTime},
			success:function(data){
				var success = $.parseJSON(data);
				if(success.issuccess==true){
					var openfirebaseurl = $("#activateAccount").attr("openfirebaseurl") ;
//					var arg = 'type=add&username='+username+'&password='+password+'&name='+fullname+'&secret=flyingsoft';
					var arg = 'type=add&username='+username+'&password=Fy_Documents_Of&name='+fullname+'&secret=flyingsoft';
					ofuserservice(openfirebaseurl, arg, false) ;
					var arg = 'type=reset_group&groups=company'+companyid+'&addgroupusernames='+username.replace("@", "\\40")+'&secret=flyingsoft';
					ofuserservice(openfirebaseurl, arg, false) ;
					var arg = 'type=reset_group&groups='+success.groupflag+'&addgroupusernames='+username.replace("@", "\\40")+'&secret=flyingsoft';
					ofuserservice(openfirebaseurl, arg, false) ;
					//window.open($("#activateAccount").attr("logouturl"), "_self");
					//20151114 xiayongcai 注册成功直接登陆系统; 
					window.location=success.logUrl;
				} else if(success.msg){
					showMsg(success.msg);
				} else {
					showMsg("出现未知的异常，请联系管理员！");
				}
			}
		});
	});
	
	$('#username').on("blur", function(){
		if($("#alerterror").attr('isAccounted') == "true"){
			return ;
		}
	 	var email = $(this).val();
	 	email=email.replace(/[ ]/g,"");
	 	email = $.trim(email);
	 	if(email==''){
	 		$("#usernamealerterror").html("用户名不能为空.");
	 		$("#usernamealerterror").css('display','block');
	 	} else if(emailaddressZZ.test(email)==false){
	 		$("#usernamealerterror").html("用户名必须是符合邮箱格式的字符串,例如admin@flyingsoft.com");
	 		$("#usernamealerterror").css('display','block');
	 	} else {
	 		$("#usernamealerterror").css('display','none');
	 	}
	});
	$('#fullname').on("blur", function(){
		if($("#alerterror").attr('isAccounted') == "true"){
			return ;
		}
		var fullname = $(this).val();
		if($.trim(fullname)==''){
			$("#fullnamealerterror").html("姓名不能为空.");
			$("#fullnamealerterror").css('display','block');
		} else if(nameZZ.test(fullname)==false){
			$("#fullnamealerterror").html("姓名必须是中文或字母.");
			$("#fullnamealerterror").css('display','block');
		} else {
			$("#fullnamealerterror").css('display','none');
		}
	});
	
	$('#password').on("blur", function(){
		if($("#alerterror").attr('isAccounted') == "true"){
			return ;
		}
		var password = $(this).val();
		if(passwordTest.test($.trim(password))==false){
			$("#passwordalerterror").html("密码只能由字母，数字，下划线组成，长度应在6-20位之间!");
			$("#passwordalerterror").css('display','block');
		} else if($.trim($("#repassword").val())==$.trim(password)){
			$("#repasswordalerterror").css('display','none');
			$("#passwordalerterror").css('display','none');
		} else {
			$("#passwordalerterror").css('display','none');
		}
	});
	
	$('#repassword').on("blur", function(){
		if($("#alerterror").attr('isAccounted') == "true"){
			return ;
		}
		var repassword = $(this).val();
		if($.trim(repassword)==''){
			$("#repasswordalerterror").html("确认密码不能为空!");
			$("#repasswordalerterror").css('display','block');
		} else if($.trim($("#password").val())!=$.trim(repassword)){
			$("#repasswordalerterror").html("您两次输入的密码不一致，请重新输入!");
			$("#repasswordalerterror").css('display','block');
		} else {
			$("#repasswordalerterror").css('display','none');
		}
	});
	
	function showMsg(msg){
		$("#rtnMsg").html(msg);
		$("#rtnMsg").css('display','block');
		setTimeout(function(){
			$("#rtnMsg").css('display','none');
		}, 2000);
	}
});