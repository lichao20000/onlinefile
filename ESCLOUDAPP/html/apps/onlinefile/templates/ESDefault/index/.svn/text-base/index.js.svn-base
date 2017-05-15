!function(e){
	var fun = {
		navcount:6,
		imgCodeRandom:'',
		loginImgCodeRandom:'',
		init : function(){
			$(".item").css("height", $("#maindiv").height()) ;
			$(".itemdesc").find("div").css("height", $("#maindiv").height()) ;
			$(".welcome").css("height", $("#maindiv").height()) ;
			$(".formdiv").css("margin-top", ($("#maindiv").height()-533)/2) ;
			var errorno = (window.location.href).indexOf("error") ;
			if(errorno>-1){
				var errortype = (window.location.href).substring(errorno+6) ;
				$("#fyusername").focus();
				if(errortype == "1"){
					$(".userlogin").show();
					$("#fyusername").focus();
					fun.loginErrorMsg("用户不存在！");
				} else if(errortype == "2"){
					$(".userlogin").show();
					var oldusername = (window.location.href).substring((window.location.href).indexOf("username")+9, errorno-1) ;
					$("#fyusername").val(oldusername) ;
					$("#fypassword").focus();
					fun.loginErrorMsg("登录密码错误！");
				} else if(errortype == "3"){
					$(".userlogin").show();
					fun.loginErrorMsg("用户被禁用！");
				}else if(errortype == "4"){
					fun.loginErrorMsgPassive("账户验证失败! ");
				}else if(errortype == "5"){
					fun.loginErrorMsgPassive("您的账号在其他地方登陆,请注意及时修改密码!!..");
				}
			} else if((window.location.href).indexOf("?")>-1){
//				fun.changePage($("#nav"+fun.navcount));
				var user = (window.location.href).substring((window.location.href).indexOf("?")+1) ;
				$("#referrerName").val(user) ;
			}
			fun.loginImgCodeRandom = Math.random() ;
			$("#loginImgCode").attr("src", "captcha.php?random="+fun.loginImgCodeRandom) ;
			setTimeout(function(){fun.imgCodeRandom = Math.random() ;$("#imgCode").attr("src", "captcha.php?register=1&random="+fun.imgCodeRandom) ;}, 2000);
		},
		bindEvent : function(){
			$(".item-nav").on("click", "li", function (){
				fun.changePage($(this));
		    });
			$(".pages").bind("mousewheel DOMMouseScroll", function (e) {
				fun.scrollFun(e);
		    });
			$(".register").on("click", function(){
				fun.changePage($("#nav"+fun.navcount));
			});
			$("#heardregister").on("click", function(){
				fun.changePage($("#nav"+fun.navcount));
			});
			$(".startView").on("click", function(){
				fun.changePage($("#nav2"));
			});
			fun.loginEvent() ;
			fun.companyRegisterEvent() ;
			$("#submitBtn").on("click", function(){
				fun.companySubmitBtnFun() ;
			});
			$("#loginImgCode").on("click", function(){
				fun.loginImgCodeRandom = Math.random() ;
				$(this).attr("src", "captcha.php?random="+fun.loginImgCodeRandom) ;
			});
			$("#imgCode").on("click", function(){
				fun.imgCodeRandom = Math.random() ;
				$(this).attr("src", "captcha.php?register=1&random="+fun.imgCodeRandom) ;
			});
			$("#fyAgreement").on("click", function(){
				window.open('flyingsoft_service_agreement.html');
			});
			$("#doMailValidate").off().on("click",function(){
				var url=$(this).val();
				window.open("http://"+url);
			});
			$("#jumpMailValidate").off().on("click",function(){
				$("#createnewcompanyclose").trigger("click");
			});
		},
		lastScrollTime : 0,
		currentZIndex : 10,
		scrollFun : function (e) {
            var t = (new Date).getTime();
            if (t - fun.scrollTime < 1400) {
                return!1;
            }
            fun.scrollTime = t ;
            var n = parseInt(e.originalEvent.wheelDelta ||- e.originalEvent.detail);
            var outObj = $(".item-nav").find(".checkeds") ;
            var outId = outObj.attr("for")*1;
            if (n > 0) {
            	if(outId>1){
            		fun.changePage($("#nav"+(outId-1)));
            	}
            } else if (0 > n) {
            	if(outId<fun.navcount){
            		fun.changePage($("#nav"+(outId+1)));
            	}
            }
        },
		changePage : function(obj){
			var outObj = $(".item-nav").find(".checkeds") ;
			var outId = outObj.attr("for")*1;
			var inId = obj.attr("for")*1;
			if(inId == outId){
				return ;
			}
			outObj.removeClass("checkeds");
			obj.addClass("checkeds");
			var inObj = $("#item"+inId) ;
			fun.currentZIndex++;
			var flag = outId>inId?0:1 ;
			if(flag){
				inObj.css({"top": $("#maindiv").height(), "z-index":fun.currentZIndex}) ;
			} else {
				inObj.css({"top": -$("#maindiv").height(), "z-index":fun.currentZIndex}) ;
			}
			fun.outAnimate($("#item"+outId), flag) ;
			fun.inAnimate(inObj) ;
		},
		inAnimate : function(obj){
			obj.animate({"top":0}, 500) ;
		},
		outAnimate : function(obj, up){
			if(up){
				obj.animate({"top":-obj.height()}, 500) ;
			} else {
				obj.animate({"top":obj.height()}, 500) ;
			}
		},
		loginEvent : function() {
			fun.loginImgCodeRandom = Math.random() ;
			$("#loginImgCode").attr("src", "captcha.php?random="+fun.loginImgCodeRandom) ;
				
			$(".dd-nav-login").on("mouseover", function(){
				$(".userlogin").show();
			});
//			
//			$(".dd-nav-login").on("click", function(){
//				$(".userlogin").show();
//				//return false;
//			});
			
			$(document).on("click", function(e){
				var currentObj = $(e.target).closest(".dd-nav-login") ;
				//var currentValue = currentObj.attr("for")*1;
				if(currentObj.length == 0){
					$(".userlogin").hide();
					return false;
				}
			});
			$(".loginbutton").on("click", function(){
				fun.login() ;
			});
			$("#fyusername").on("blur", function(){
				var username = $("#fyusername").val() ;
				if($.trim(username) == ""){
					fun.loginErrorMsg("用户名不能为空！");
				}
			})
			/*
			.on("keydown", function(event){
				var username = $("#fyusername").val() ;
				var e = event || window.event;
				var keyCode = e.which || e.keyCode;
				if (keyCode == 13) {
					if($.trim(username) != ""){
						$("#fypassword").focus() ;
					}
					return false;
				}
				
			});
			*/
			$("#fypassword").on("blur", function(){
				var password = $("#fypassword").val() ;
				if($.trim(password) == ""){
					fun.loginErrorMsg("密码不能为空！");
				}
			}).on("keydown", function(event){
				var password = $("#fypassword").val() ;
				var e = event || window.event;
				var keyCode = e.which || e.keyCode;
				if (keyCode == 13) {
					if($.trim(password) != ""){
						$("#fycode").focus() ;
					}
					return false;
				}
			});
			$("#fycode").on("blur", function(){
				var fycode = $("#fycode").val() ;
				if($.trim(fycode) == ""){
					fun.loginErrorMsg("验证码不能为空！");
				}
			}).on("keydown", function(event){
				var e = event || window.event;
				var keyCode = e.which || e.keyCode;
				if (keyCode == 13) {
					fun.login() ;
					return false;
				}
			});
		},
		login : function(){
//			var maindiv = $("#maindiv") ;
//			var baseUrl = maindiv.attr("indexUrl") ;
//			var indexUrl = baseUrl+"/user/token" ;
//			var errorUrl = baseUrl+"/Default?error" ;
			var username = $("#fyusername").val() ;
			var password = $("#fypassword").val() ;
			var fycode = $("#fycode").val() ;
			if($.trim(username) == ""){
				fun.loginErrorMsg("用户名不能为空！");
				return ;
			}
			if($.trim(password) == ""){
				fun.loginErrorMsg("密码不能为空！");
				return ;
			}
			if($.trim(fycode) == ""){
				fun.loginErrorMsg("验证码不能为空！");
				return ;
			}
			
			if(password.indexOf("##-##")>0){
				var pass = md5TO(password);
				$("#fypassword").val(pass);
			}
			
			$.ajax({
				url:"captcha.php",
				type:'POST',
				data:{'code':fycode,'random':fun.loginImgCodeRandom},
				success:function(data){
					if(data=="true"){
						$("#loginForm").submit();
						//document.formx1.submit();
//						window.location.href = maindiv.attr("loginUrl")+"/flyingoauth/j_spring_security_check?username="+username+"&password="+password+"&success="+indexUrl+"&error="+errorUrl
					} else {
						if(data=="false"){
						   fun.loginErrorMsg("验证码不正确！");
						} else {
						   fun.loginErrorMsg("请刷新验证码！");
						} 
					}
				}
			});
			
		},
		loginErrorMsg : function(msg){
			var loginerror = $(".loginerror") ;
			loginerror.html(msg);
			loginerror.show();
			loginerror.delay(5000).fadeOut("fast");
		},
		loginErrorMsgPassive : function(msg){
			var loginerrorMsg = $(".passiveLogoutLoginerror") ;
			var loginerror = $("#passivMsg") ;
			loginerror.html(msg);
			loginerrorMsg.show();
			loginerrorMsg.delay(5000).fadeOut("fast");
		},
		companyRegisterEvent : function() {
			$("#campanyName").on("blur", function(){
				fun.companyNameEvent() ;
			}).on("keydown", function(event){
				var e = event || window.event;
				var keyCode = e.which || e.keyCode;
				if (keyCode == 13 || keyCode == 9) {//9标识Tab
					if(fun.companyNameEvent()){
						$("#registeremail").focus() ;
					}
				}
			});
			$("#closePassiv").on("click", function(){
				$(".passiveLogoutLoginerror").hide();
			});
			
			$("#registeremail").on("blur", function(){
				fun.companyEmailEvent() ;
			}).on("keydown", function(event){
				var e = event || window.event;
				var keyCode = e.which || e.keyCode;
				if (keyCode == 13) {
					if(fun.companyEmailEvent()){
						$("#registercode").focus() ;
					}
				}
			});
			$("#registercode").on("blur", function(){
				fun.companyCodeEvent() ;
			}).on("keydown", function(event){
				var e = event || window.event;
				var keyCode = e.which || e.keyCode;
				if (keyCode == 13) {
					if(fun.companyCodeEvent()){
						$("#registerinfo").focus() ;
					}
				}
			});
			$("#registerinfo").on("change", function(){
				if(document.getElementById("registerinfo").checked){
					fun.companySubmitBtnEvent() ; 
				} else {
					$("#submitBtn").addClass("dd-no-submit") ;
				}
			}) ;
		},
		emailaddressZZ : /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/, 
		companyNameZZ : /^[a-zA-Z0-9()\u4E00-\u9FA5_\[\] ]+$/, 
		companyNameIsOk : false, 
		companyEmailIsOk : false, 
		companyCodeIsOk : false, 
		companyNameEvent : function() {
			var value = $.trim($("#campanyName").val()) ;
			$("#campanyName").parent().find(".dd-glyphicon").hide() ;
			if(value == ""){
				$("#campanyIsNull").show() ;
				fun.companyNameIsOk = false ;
				$("#submitBtn").addClass("dd-no-submit") ;
				return false ;
			}
			if(value.replace("/[^/x00-/xFF]/g",'**').length > 50 
				|| 4 > value.replace("/[^/x00-/xFF]/g",'**').length){
				
				$("#campanyLength").show() ;
				fun.companyNameIsOk = false ;
				$("#submitBtn").addClass("dd-no-submit") ;
				return false ;
			}
			if(fun.companyNameZZ.test(value)==false){
				$("#campanyValidate").show() ;
				fun.companyNameIsOk = false ;
				$("#submitBtn").addClass("dd-no-submit") ;
				return false ;
			}
			var maindiv = $("#maindiv") ;
			/**20151015 xiayongcai 验证企业名称是否被注册*/
			var data = "companyName="+value ;
			$(".formdiv").showLoading();
			$.post(maindiv.attr("loginUrl")+"/flyingoauth/validateemail",{'companyName':value},
				    function(data) {
						var json = $.parseJSON(data)
						$(".formdiv").hideLoading(); 
						if(json){
							$("#campanyHased").show() ;
							fun.companyNameIsOk = false ;
							$("#submitBtn").addClass("dd-no-submit") ;
							return false;
						} else {
							$("#campanyIsOk").show() ;
							fun.companyNameIsOk = true ;
							fun.companySubmitBtnEvent() ; 
							return true  ;
						}
				});
		},
		companyEmailEvent : function() {
			var value = $.trim($("#registeremail").val()) ;
			$("#registeremail").parent().find(".dd-glyphicon").hide() ;
			if(value == ""){
				$("#emailIsNull").show() ;
				fun.companyEmailIsOk = false ;
				$("#submitBtn").addClass("dd-no-submit") ;
				return false;
			}
			if(fun.emailaddressZZ.test(value)==false){
				$("#emailFormatError").show() ;
				fun.companyEmailIsOk = false ;
				$("#submitBtn").addClass("dd-no-submit") ;
				return false;
			}
			var maindiv = $("#maindiv") ;
			var data = "email="+value ;
			$(".formdiv").showLoading();
//			jQuery.getJSON($("#maindiv").attr("loginUrl")+'/flyingoauth/validateemail?callback=?',{'email':value},
			$.post(maindiv.attr("loginUrl")+"/flyingoauth/validateemail",{'email':value},
			    function(data) {
					var json = $.parseJSON(data)
					$(".formdiv").hideLoading(); 
					if(!json){
						$("#emailHased").show() ;
						fun.companyEmailIsOk = false ;
						$("#submitBtn").addClass("dd-no-submit") ;
						return false;
					} else {
						$("#emailIsOk").show() ;
						fun.companyEmailIsOk = true ;
						fun.companySubmitBtnEvent() ; 
						return true  ;
					}
			});
		},
		companyCodeEvent : function() {
			var value = $.trim($("#registercode").val()) ;
			$("#registercode").parent().find(".dd-glyphicon").hide() ;
			if(value == ""){
				$("#codeIsNull").show() ;
				fun.companyCodeIsOk = false ;
				$("#submitBtn").addClass("dd-no-submit") ;
				return false;
			}
			$.ajax({
				url:"captcha.php",
				type:'POST',
				data:{'register':1, 'code':value, 'random':fun.imgCodeRandom},
				success:function(data){
					if(data=="true"){
						$("#codeIsOk").show() ;
						fun.companyCodeIsOk = true ;
						fun.companySubmitBtnEvent() ; 
						return true  ;
					} else {
						$("#codeError").show() ;
						fun.companyCodeIsOk = false ;
						$("#submitBtn").addClass("dd-no-submit") ;
						return false ;
					}
				}
			});
		},
		companySubmitBtnEvent : function() {
			if(document.getElementById("registerinfo").checked && fun.companyCodeIsOk && fun.companyEmailIsOk && fun.companyNameIsOk){
				$("#submitBtn").removeClass("dd-no-submit") ;
			} else {
				$("#submitBtn").addClass("dd-no-submit") ;
			}
		},
		companySubmitBtnFun : function() {
			if($("#submitBtn").attr("class") == "dd-submit-btn"){
				var maindiv = $("#maindiv") ;
				var data = {"companyName":$.trim($("#campanyName").val()),"email":$.trim($("#registeremail").val()),"referrerName":$.trim($("#referrerName").val()),"successurl":$("#maindiv").attr("indexUrl")};
				$(".formdiv").showLoading();
				jQuery.getJSON(maindiv.attr("loginUrl")+"/flyingoauth/regist?callback=?",data, function(json) {
					if(json){
//						$("#registIsOk").show();
//						var tempInterval = setInterval(intervalFun, 1000);
//						var value = 4;
//				        function intervalFun() {
//				            $("#registIsOk").find("span").html("注册成功！&nbsp;"+value+"秒钟后会自动跳转到系统首页。");
////				            $("#registIsOk").find("span").html("注册成功！&nbsp;"+value+"秒钟后会自动跳转到邮箱的登陆界面。");
//				            if (value == 0) {
//				                clearInterval(tempInterval);
//				                window.location.href = maindiv.attr("indexUrl");
////				                var registeremail = $.trim($("#registeremail").val()) ;
////				                registeremail = "mail."+registeremail.substring(registeremail.indexOf("@")+1);
////				                window.location.href = "http://"+registeremail;
//				            }
//				            value--;
//				        }
						var mail = $.trim($("#registeremail").val());
						var mailHost ="mail."+ mail.substring(mail.indexOf("@")+1);
						$(".formdiv").hideLoading();
						$("#findPassbg").show();
						$("#emailText").html($.trim($("#registeremail").val()));
						$("#doMailValidate").val(mailHost);
						$('#bingotocreate').show();
					} else {
						$(".formdiv").hideLoading();
						$("#registError").show();
						$("#registError").delay(5000).fadeOut("fast");
					}
				});
			}
		}
	};
	fun.init();
	fun.bindEvent();
	/**
	 * 注册完成弹窗关闭按钮
	 */
	$('#createnewcompanyclose').on('click',function(){
		$("#registeremail").val("");
		$("#referrerName").val("");
		$("#campanyName").val("");
		$("#registercode").val("");
		$("#findPassbg").hide();
		$('#bingotocreate').hide();
		
		/** lujixiang 20150728 将验证信息置为false并去除验证样式, 刷新验证码 **/
		fun.companyNameIsOk = false ;
		fun.companyEmailIsOk = false ;
		fun.companyCodeIsOk = false ;
		
		$("#campanyIsOk").hide();
		$("#emailIsOk").hide() ;
		$("#codeIsOk").hide() ;
		
		$("#imgCode").click();
		$("#registerinfo").removeAttr("checked");
		$("#submitBtn").addClass("dd-no-submit") ;
	});
	/**
	 * 查找密码点击事件
	 */		
	$("#getpasswordDivId").on("click",function(){
		$("#findPasswordDivId").css("left",($(window).width()-600)/2);
		$("#findPassbg").show();
		$("#findPasswordDivId").slideDown();
	});
	
	/**
	 * 查找密码框关闭事件
	 */
	$("#findPassCloseId").on("click",function(){
		$("#findPassbg").hide();
		$("#findPasswordDivId").hide();
		resetFindPassForm();
		$("#findPassErrorDivId").hide();
	});

	/**
	 * 获取验证码方法
	 */
	$('#getValidateCodeBtnId').click(function () {
	    	if($('#getValidateCodeBtnId').hasClass("bgcolccc")){//灰色状态下直接返回，不触发下面事件
	    		return;
	    	}
	    	
	    	//先验证用户名是否填写了 
	    	if ($("#findPassusernameId").val().trim().length==0 ){
	    		showFindPassMsg("请填写用户名。",$("#findPassusernameId"));
		    	return ;
	    	} 
	    	var reg =  /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/; 
	    	if (!reg.test($("#findPassCheckEmailId").val())){
	    		showFindPassMsg("请填写用户名,用户名格式错误,应为邮箱格式。",$("#findPassCheckEmailId"));
		    	return ;
	    	} 
	    	//验证下用户名是否存在
	    	jQuery.getJSON($("#maindiv").attr("loginUrl")+'/onlinefile/rest/onlinefileuser/setValidateCode?callback=?',
				{"username":$("#findPassusernameId").val(), "email":$("#findPassCheckEmailId").val() },
				function(json) {
					if(json.success==false || json.success=="false"){
						if(json.msg=="1"){
							$("#findPassErrorDivId span").text("此邮箱验证下的用户不存在。");
							$("#findPassErrorDivId").show();
						}else if(json.msg=="2"){
							$("#findPassErrorDivId span").text("验证码发送失败。");
							$("#findPassErrorDivId").show();
						}				
					}
			});
	        var count = 30;
		    $('#getValidateCodeBtnId').addClass("bgcolccc");
		    $("#findPassErrorDivId span").text("验证码已发送至您的邮箱，请查收后填写。");
		    $("#findPassErrorDivId").show();
	        var countdown = setInterval(CountDown, 1000);
	        function CountDown() {
		        $('#getValidateCodeBtnId').addClass("bgcolccc");
	            $("#getValidateCodeBtnId").text(count + " 秒后获取");
	            if (count == 0) {
		       		$('#getValidateCodeBtnId').removeClass("bgcolccc");
		            $("#getValidateCodeBtnId").text("获取验证码");
	                clearInterval(countdown);
	            }
	            count--;
	        }
	    });
	
		/**
		 * input框键盘按下事件
		 */
	    $("#findPasswordDivId input").on("keyup",function(){
	    	$(this).removeClass("bordercolred");
	    });
	    
	    /**
	     * 表单验证
	     */
	    function validateFindPassForm(){
	    	//验证用户名 
	    	if ($("#findPassusernameId").val().trim().length==0 ){
	    		showFindPassMsg("请填写用户名。",$("#findPassusernameId"));
		    	return false;
	    	} 
	    	//验证绑定邮箱 
	    	var emailreg =  /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/; 
	    	if (!emailreg.test($("#findPassCheckEmailId").val())){
	    		showFindPassMsg("邮箱格式错误。",$("#findPassCheckEmailId"));
		    	return false;
	    	} 
	    	//验证验证码 findPassCheckCodeId  \d{6}
	    	var checkCodeReg = /^\d{6}$/;
	    	if (!checkCodeReg.test($("#findPassCheckCodeId").val())){
	    		showFindPassMsg("验证码错误。",$("#findPassCheckCodeId"));
		    	return false;
	    	} 
	    	//新密码 findPassNewPassId
	    	if($("#findPassNewPassId").val().trim().length==0){
	    		showFindPassMsg("请填写密码。",$("#findPassNewPassId"));
		    	return false;
	    	}
	    	if($("#findPassNewPassId").val().trim().length<6 || $("#findPassNewPassId").val().trim().length>20){
	    		showFindPassMsg("密码只能由字母，数字，下划线组成，长度应在6-20位之间。",$("#findPassNewPassId"),"25");
		    	return false;
	    	}
	    	var passwordreg =  /^\w+$/;
	    	if (!passwordreg.test($("#findPassNewPassId").val())){
	    		showFindPassMsg("密码只能由字母，数字，下划线组成，长度应在6-20位之间。",$("#findPassNewPassId"),"25");
		    	return false;
	    	} 
	    	//确认密码 findPassAgainPassId
	    	if($("#findPassNewPassId").val()!=$("#findPassAgainPassId").val()){
	    		showFindPassMsg("确认密码与新密码不一致。",$("#findPassAgainPassId"));
		    	return false;
	    	}
	    	return true;
	    }
	    
	    /**
	     * 显示错误信息
	     */
	    function showFindPassMsg(msg,obj,lineHeight){
	    	obj.addClass("bordercolred");
			$("#findPassErrorDivId span").text(msg);
			if(lineHeight=="25"){
				$("#findPassErrorDivId span").css("line-height","25px");
			}else{
				$("#findPassErrorDivId span").css("line-height","49px");
			}
			$("#findPassErrorDivId").show();
	    }
	    
	    /**
	     * 提交按钮事件
	     */
	    $("#findPassSaveBtnId").on("click",function(){
	    	if($("#findPassSaveBtnId").hasClass("bgcolccc")){
	    		return;
	    	}
	    	if(validateFindPassForm()){
	    		$("#findPassSaveBtnId").addClass("bgcolccc");
	    		$("#findPassErrorDivId span").text("正在修改，请稍后。");
	    		var data = {"username":$("#findPassusernameId").val(), "email":$("#findPassCheckEmailId").val(),"checkCode":$("#findPassCheckCodeId").val(),"newpassword": $("#findPassNewPassId").val()} ;
	    		jQuery.getJSON($("#maindiv").attr("loginUrl")+"/flyingoauth/resetpass?callback=?",data, function(json) {
//	    			json = JSON.parse(json); 
	    			if(json.success==false || json.success=="false"){
						$("#findPassSaveBtnId").removeClass("bgcolccc");
						if(json.msg=="1"){
							$("#findPassErrorDivId span").text("此邮箱验证下的用户不存在。");
							$("#findPassErrorDivId").show();
						}else if(json.msg=="2"){
							$("#findPassErrorDivId span").text("验证码错误 。");
							$("#findPassErrorDivId").show();
						}else if(json.msg=="3"){
							$("#findPassErrorDivId span").text("验证码超时，验证码三分钟内有效 。");
							$("#findPassErrorDivId").show();
						}else if(json.msg=="4"){
							$("#findPassErrorDivId span").text("修改失败。");
							$("#findPassErrorDivId").show();
						}								
					}else{
//						var arg = 'type=resetpass&username='+$("#findPassusernameId").val()+'&password='+$("#findPassNewPassId").val()+'&secret=flyingsoft';
						resetFindPassForm();
						$("#findPassSaveBtnId").addClass("bgcolccc");
						$("#findPassErrorDivId span").text("密码修改成功！");
						$("#findPassErrorDivId").show();
						setTimeout('$("#findPassErrorDivId").hide();$("#findPassSaveBtnId").removeClass("bgcolccc");$("#findPassbg").hide();$("#findPasswordDivId").hide();', 3000);
//						ofuserservice($("#maindiv").attr("openfireurl"), arg, false) ;
					}
				});
	    	}
	    });
	    
	    /**
	     * 重置表单
	     */
	    function resetFindPassForm(){
	    	$("#findPassusernameId").val("");
	    	$("#findPassCheckEmailId").val("");
	    	$("#findPassCheckCodeId").val("");
	    	$("#findPassNewPassId").val("");
	    	$("#findPassAgainPassId").val("");
	    	$("#findPassErrorDivId").hide();
	    	$("#findPasswordDivId input").removeClass("bordercolred");
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
		
}();


//用户名组件获得焦点时，如果有值，则选中值。
function usernameFocusFun(el) {
	if (el && el.value) {
		el.select();
	}
}

//用户名发生改变时处理密码值，勾选记住密码时，从cookie中读取对应的密码，否则置为空
function cleanPwd4nameChange() {
	var user = $("#fyusername").val();
	var pass = $("#fypassword").val();
	var cookieuser=Util.Cookie.get("fyusername");
	var cookiepass=Util.Cookie.get("uuid");
	if(user != cookieuser){
		$("#fypassword").val('');
	}else if($("#remPWD").prop("checked")){
		$("#fypassword").val(cookiepass);
	}
}
/**
 * 设置cookie
 */
var Util = {
    Cookie: {
        set: function (name, value, expire) {
            var exp = new Date();
            exp.setTime(exp.getTime() + expire * 24 * 60 * 60 * 1000);
            document.cookie = name + "=" + encodeURIComponent(value,"utf-8") + ";expires=" + exp.toGMTString() + ";domain=;path=/";
        },
        get: function (key) {
            var cookies = document.cookie ? document.cookie.split('; ') : [];
            for (var i = 0, l = cookies.length; i < l; i++) {
                var parts = cookies[i].split('=');
                var name = parts.shift();
                var cookie = parts.join('=');
                if (key && key === name) {
                    return decodeURIComponent(cookie);
                }
            }
        },
        setALCookie: function () {
            if ($("input[name='chkRememberMe']").prop("checked")) {
                var Days = 3 * 30;
                var password = $("#fypassword").val();
                var uid = getUuid();
            	if(password.indexOf("##-##")<1){
            		password = toMD5(uid,password);
            	}
                this.set("fyusername", $("#fyusername").val(), Days);
                this.set("uuid", password, Days);
            } else {
                var v = this.get("uuid");
                if (v) {
                    this.set("uuid", "", -100);
                }
            }
        }
    }
};

function saveUsername(){
	Util.Cookie.setALCookie();
}
/**
 * 生成唯一编号
 */
function getUuid() {
    var s = [];
    var hexDigits = "0123456789abcdefghijklmnopqrstuvwxyz";
    for (var i = 0; i < 50; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    var uuid = s.join("");
    return uuid;
}
/**
 * 密码加密
 */
function toMD5(str1,str2){
	var num = 0;
	var pass="";
	for (var i = 0; i < str1.length; i++ ) {  
		if(i == 49){
			pass+=str1.charAt(i)+"##-##"+str2.length;
		}else{
			if(i%3==0){
				if(num<str2.length && num<15){
					pass+=str2[num];
					num++;
				}else{
					pass+=str1.charAt(i);
				};
			}else{
				pass+=str1.charAt(i);
			}
		}
	}
	if(str2.length>15){
		pass+="##-##"+str2.substring(15); 
	}
	return pass;
}


function md5TO(mdpass){
	var pass="";
	var num=0;
	var str = mdpass.split("##-##");
	var length = str[1];
	var md = str[0];
	for (var i = 0; i < md.length; i++) {
		if(i%3==0){
			pass+=md[i];
			num++;
		}
		if(num>=length || num>=15){
			break;
		}
	}
	if(length>15){
		pass+=str[2]; 
	}
	return pass;
}
	
$(document).ready(function() {
	
	var username = Util.Cookie.get("fyusername");
	var userpass = Util.Cookie.get("uuid");
	$("#fyusername").val(username);
	if(typeof userpass != 'undefined' ){
		$("#fypassword").val(userpass);
		$("#remPWD").prop("checked", true);
		$(".msg-wrap .msg-warn").show();
	}else{
		$("#remPWD").prop("checked", false);
	}
});

$("#remPWD").on("click",function(){
	if($("#remPWD").prop("checked") == true){
		$(".msg-wrap .msg-warn").show();
	}else{
		$(".msg-wrap .msg-warn").hide();
	}
});

//liuwei解决IE浏览器问题
function isPlaceholder(){  
    var input = document.createElement('input');  
    return 'placeholder' in input;  
}  
  
if (!isPlaceholder()) {
    $(document).ready(function() {  
        if(!isPlaceholder()){  
            $("input").not("input[type='password']").each(//把input绑定事件 排除password框  
                function(){  
                    if($(this).val()=="" && $(this).attr("placeholder")!=""){  
                        $(this).val($(this).attr("placeholder"));  
                        $(this).focus(function(){  
                            if($(this).val()==$(this).attr("placeholder")) $(this).val("");  
                        });  
                        $(this).blur(function(){  
                            if($(this).val()=="") $(this).val($(this).attr("placeholder"));  
                        });  
                    }  
            });  
            //对password框的特殊处理1.创建一个text框 2获取焦点和失去焦点的时候切换  
            var pwdField = $("input[type=password]");  
            var pwdVal = pwdField.attr('placeholder');  
            pwdField.after('<input id="pwdPlaceholder" type="text" class="form-control userlogininput" placeholder="密码" value='+pwdVal+' autocomplete="off" />');  
            var pwdPlaceholder = $('#pwdPlaceholder');  
            pwdPlaceholder.show();  
            pwdField.hide();  
              
            pwdPlaceholder.focus(function(){  
                pwdPlaceholder.hide();  
                pwdField.show();  
                pwdField.focus();  
            });  
              
            pwdField.blur(function(){  
                if(pwdField.val() == '') {  
                    pwdPlaceholder.show();  
                    pwdField.hide();  
                }  
            });  
              
        }  
    });  
      
}  
















