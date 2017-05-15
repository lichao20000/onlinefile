$(function(){
	$("#user_perfect").on('click',function(){
		 user_perfect()
	});
	function user_perfect()
	{
		$.ajax({
		    url : $.appClient.generateUrl({
		    	ESUserInfo : 'user_perfect'},'x'),
		    success:function(data){
		    	$.dialog({
		    		id: 'user_perfectDialog',
			    	title:'个人信息完善',
		    	   	fixed:false,
		    	    resize: false,
		    	    zIndex:200,
		    	    lock : true,
					opacity : 0.1,
			    	okVal:'保存',
				    ok:true,
				    cancelVal: '关闭',
				    cancel: true,
				    content:data,
				    okId : 'saveBtn',
				    ok : function() {
				    	$('#signature').val($('input[name=signatureInput]').val());
				    	/*-----------------签名字段------------------------*/
				    	var fullname = $("#fullname").val();
				    	var mobilephone = $("#mobilephone").val();
				    	var telephone = $("#telephone").val();
				    	var email = $("#email").val();
				    	var position = $("#email").val();
				    	var fax = $("#fax").val();
//				    	if(trim(fullname)==""||trim(mobilephone)==""||trim(telephone)==""||trim(email)==""||trim(position)==""||trim(fax)==""){
//				    		if(trim(fullname)==""){
//				    			showTooltips('fullname_input','中文名不能为空');
//				    		}
//				    		if(trim(mobilephone)==""){
//				    			showTooltips('mobilephone_input','手机号码不能为空');
//				    		}
//				    		if(trim(telephone)==""){
//				    			showTooltips('telephone_input','公司电话不能为空!');
//				    		}
//				    		if(trim(email)==""){
//				    			showTooltips('email_input','邮箱不能为空!');
//				    		}
//				    		if(trim(position)==""){
//				    			showTooltips('position_input','职位');
//				    		}
//				    		if(trim(fax)==""){
//				    			showTooltips('fax_input','传真不能为空!');
//				    		}
//				    		return false;
//				    	}else if(nameZZ.test(fullname)==false){
//				    			showTooltips('fullname_input','中文名只能是中文或字母!');
//				    		return false;
//				    	}else if(emailaddressZZ.test(email)==false){
//				    		showTooltips('email_input','邮箱格式不正确!');
//				    		return false;
//				    	}else if(mobtelZZ.test(mobilephone)==false){
//				    		showTooltips('mobilephone_input','手机号码格式不正确!');
//				    		return false;
//				    	}else if(teleZZ.test(telephone)==false){
//				    		showTooltips('telephone_input','公司电话格式不正确!');
//				    		return false;
//				    	}else if(fexZZ.test(fax)==false){
//				    		showTooltips('fax_input','传真格式不正确!');
//				    		return false;
//				    	}
				    	//理论上都可不填写 但是填写就要验证
				    	if(trim(fullname)==""){
				    		showTooltips('fullname_input','中文名不能为空');
				    		return false;
				    	}else if(nameZZ.test(fullname)==false){
				    		showTooltips('fullname_input','中文名只能是中文或字母!');
				    		return false;
				    	}
				    	if(trim(email) != "" && emailaddressZZ.test(email)==false){
				    		showTooltips('email_input','邮箱格式不正确!');
				    		return false;
				    	}
				    	if(trim(mobilephone) != "" && mobtelZZ.test(mobilephone)==false){
				    		showTooltips('mobilephone_input','手机号码格式不正确!');
				    		return false;
				    	}
				    	if(trim(telephone) != "" && teleZZ.test(telephone)==false){
				    		showTooltips('telephone_input','公司电话格式不正确!');
				    		return false;
				    	}
				    	if(trim(fax) != "" && fexZZ.test(fax)==false){
				    		showTooltips('fax_input','传真格式不正确!');
				    		return false;
				    	}
				    	//url =  $esaction->generateUrl(Array('ESUserInfo'=>'do_edit'),'x');
				    	url =  $.appClient.generateUrl({ESUserInfo : 'editUserInfo'},'x');
				    	var form=$('#form');
				    	var data=form.serialize();
				    	$.ajax({
				    		url:url,
				    		type: "POST",
				    		data:{
				    			param:data
				    		},
				    		dataType:"json",
				    		error:function(){
				    			$.dialog.notice({icon:'error',content:'编辑个人信息失败，请重试！',time:3});
				    		},
				    		success:function(datas){	
				    			if(datas!=null){
				    				showMsg("编辑成功！", "1");
				     				$("#user_menu #current_user_name").html(fullname);
				    			}else{
				    				showMsg("编辑失败！", "2");
				    			}
				    			
				    		}
				    	});
					}
			    });
		    	
			    },
			    cache:false
		});
	}
	//点击头像触发
//	$("#touxiang").live("click",function(){
//		$.dialog({
//			id:'changeiconDialog',
//			title:'上传文件',
//		    fixed:true,
//		    resize: false,
//		    padding:'0px 0px',
//			content:"<div class='fieldset flash' id='fsUploadProgress'></div>",
//			cancelVal: '关闭',
//			cancel: function (){
//			},
//			button: [
//	    		{id:'btnAdd', name: '添加文件'},
//	            {id:'btnCancel', name: '删除文件', disabled: true},
//	            {id:'btnStart', name: '开始上传', disabled: true, callback: function(){return false;}}
//			],
//			init:createSWFUpload
//		});	    	
//	
//	});
	$("#touxiang").on("click",function(){
		$.ajax({
		    url : $.appClient.generateUrl({ESUserInfo : 'headPicture'},'x'),
		    success:function(data){
	    		$.dialog({
	    			id:'uploadhead',
			    	title:'头像上传',
		    	   	fixed:false,
		    	    resize: false,
		    	    zIndex:510,
		    	    lock : true,
					opacity : 0.1,
					padding:0,
					width: 100,
					height: 100,
				    content:data,
				    okVal : '确定',
					ok : true,
					cancelVal : '关闭',
					cancel : true,
					ok : function() {
						$("#imageform").ajaxSubmit({
							url: $.appClient.generateUrl({ESUserInfo : 'uploadImage1'},'x'),
							type:"post",
							dataType:"text",
							success:function(responseText){
								var success = $.parseJSON(responseText);
								if(success.success ==true){
									//showMsg(success.msg, "1");
									$("#touxiang").attr('src',success.touxiang);
									$("#indexheadimage").attr('src',success.touxiang);  //修改完头像替换掉首页的头像
//									$('#smallimage').css('background-image','url('+success.touxiang+')');
//									alert($('#smallimage').attr('background-image'));  //下拉框中的人员
									art.dialog.list['uploadhead'].close();
								}else if(success.success ==false){
									showMsg(success.msg, "2");
								}
							},
							error:function(){
								showMsg("系统错误!", "2");
							}
						});return false;
					}
			    });
			  },
		      cache:false
		});
	
	});
	
	//点击修改密码触发
	$("#changepassword").on("click",function(){
		$.ajax({
			url : $.appClient.generateUrl({ESUserInfo : 'changepassword'},'x'),
			type : 'post',
			success:function(resultform){
	    		$.dialog({
	    			id:'modifyPasswordForm',
	    			title:'修改密码',
	    			content : resultform,
	    			fixed:false,
		    	    resize: false,
		    	    lock : true,
	    			opacity : 0.1,
	    			padding : 0,
	    			okVal : '确定',
	    			ok : true,
	    			cancelVal : '关闭',
	    			cancel : true,
	    			ok : function() {
	    				var oldPassword = $("#oldPassword").val();
	    				var newPassword = $("#newPassword").val();
	    				var repetPassword = $("#rePassword").val();
	    				if(oldPassword=='' || newPassword==''){
	    					showTooltips('oldPassword_input','密码不能为空!');
	    					showTooltips('newPassword_input','新密码不能为空!');
	    					return false;
	    				}else{
	    					hideTooltips('oldPassword_input');
	    					hideTooltips('newPassword_input');
	    				}
	    				if(newPassword != repetPassword){
	    					showTooltips('rePassword_input','两次密码不一致');
	    					return false;
	    				}else{
	    					hideTooltips('newPassword_input');
	    				
	    				}
	    				
//	    				alert(newPassword);
	    				var modifyurl = $.appClient.generateUrl({ESUserInfo : 'do_changepassword'}, 'x');
	    				$.post(modifyurl,{oldPassword:oldPassword,newPassword:newPassword}, function(result){
	    					var isPasswordValid = result.isPasswordValid;//密码是否正确
	    					var isModifySuccess = result.isModifySuccess;//是否重置成功
	    					if(isPasswordValid=='true'){
	    						if(isModifySuccess=='1'){
	    							showMsg("修改成功!", "1");;
	    							art.dialog.list['modifyPasswordForm'].close();
	    						}else{
	    							showMsg("修改失败!", "2");
	    							art.dialog.list['modifyPasswordForm'].close();
	    						}
	    					}else{
	    						//$.dialog.notice({icon : 'warning',content : '您输入的密码不正确！',title : '3秒后自动关闭',time : 3});
	    						showTooltips('oldPassword_input','密码错误!');
	    						//$("#modifyPasswordDIV input[name='oldPassword']").addClass("warnning");
	    					}
	    					
			    		},'json');
	    				return false;
	    			}
	    		});
			}
		});
		return false;
		
	});
	
	
	//正则
	var nameZZ= /^[\u4e00-\u9fa5a-zA-Z]+$/;
	var emailaddressZZ = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	/*-------------------------------------------点击online_left_top弹出用户菜单-----------------------------------------------------------*/
	$("#user_menu_click").click(function(){
		if($('#user_info_tan_menu').css("display")=="none"){
			$("#user_info_tan_menu").show();
			$("#user_info_tan_menu .perfect-scroller").perfectScrollbar('update');
		}else{
			$("#user_info_tan_menu").hide();
		}
		return false;
	});
	
	/*------------------------------------------------忘记密码----------------------------------------------------------*/
	$("#forgetpassword").on("click",function(){
//		var username = $("#username").val();
//		var email = $("#email").val();
		var email = "mingchao52189914@163.com";
		var username = "33333@163.com";
		if($.trim(username) == "" && emailaddressZZ.test($.trim(username))){
			showMsg("用户名格式不正确！", "2");//提示信息暂时用这个  以后改
			return false;
		}
		if($.trim(email) == "" && emailaddressZZ.test($.trim(email))){
			showMsg("邮箱格式不正确格式不正确！", "2");//提示信息暂时用这个  以后改
			return false;
		}
		
		$.ajax({
			url : $.appClient.generateUrl({ESUserInfo : 'forgetpassword'},'x'),
    		type: "POST",
    		data:{username:username,email:email},
    		dataType:"json",
    		error:function(){
    		},
    		success:function(datas){	
    			var success = $.parseJSON(datas);
//    			alert(success.success);
    			if(success.success){
    				showMsg("重置密码已发送至您的邮箱,请注意查收！", "1");
    			}else{
    				showMsg("邮箱发送失败，请联系管理员！", "2");
    			}
    			
    		}
    	});
		
	});
	
	/*--------------------------------------------离线在线-----------------------------------------------------------*/
	//改变在线状态
	$("#changeOnline").click(function(){
		var onlineValue = $("#connection_status").attr("value");
		if(onlineValue=='1'){
			$("#connection_status").attr("value","0");
			$("#connection_status").html("离线");
			$("#changeOnline a").html('<i class="fa fa-random"></i>在线');
			$("#connection_icon").attr("src","data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAFb0lEQVR42u2Xe0xTVxzH2ZZlWfxvW2YW98f+2OaiidmmZlaglEefvBRE2RwjguJijKJxDpzipjiGL0Kckc2Z4OJmKW15WoRKsSqi+CA+shUqMnUOfJRy21IotOe7323aIGmujG2JW+Iv+eTe3nPu+X1/v3N+556GPbX/hQF4JnB9jVhNHCF+w5iNEBbiIJFATAm+9285foUoIVx4jDHGELBuIod48W8LAfBs4BpJdAd9/MF1otlagp86srC/TYxv2yLwfXsc9NfW4mzPIXBD9/CIGYjXg+NNKmrevF5vFguE1ee8wSqvbEKxORy7T0tQ0hqF0lYx9rX58d/vPSNGCV2NXfsw6LGzQFbuABAJixBOezLhZfCi446RFZtl+KpFjCJzNHaaJSQiKuAwivA79z/beSoa200iHGjLwN2BzuCc9BDTA+M+J+g82Mh3DryES7eNLN/4LgqaJSQgBjtaJPjmpIQcRWEXQU4DjiUoJmFfU1vhyRhsNUXQbzm73W8Bbz741ABeGsuEYOR9UwDsJXDLZsEXRhHLaxJjy4lYbG2OxjZTNApbyJGZhDxCUdA5tVEfEhCLvCYR9rVmM8ewDbx5vZ60CaP3eDwzALjdoxx+OJ/Hcg3zaKBYEhKLzSdi/INrO3ag514n7JwbtgEHHtj78cvv7TjcvoEyNYd37hecb4zGuob3YLZqAIwSaHa5XFPHshA6788TmwlY+i5jZf0stq5Bgs8bZdjYyEcWh3M3jOAcw3g4wPmdj2cAx68fJsHTsckoRX6TlASEY1NTHOMCWRgdHY0J+gwpOZfr3lRe5ZDXhUPnC/Bp3TwaQIrPjiuQa5iGpusaOJwe2EKd++kfcKLX1oefL+7BGsM0Eq6k96Ox6thcdrq7Cj6MgKzQarW+EBQxLv3cMPcWQAF67KR6AcupC8d6gwor6mZgf2s+bvZ1UdoHeWeCDDiGcOFmC1VCKomIxPoGuT+Q8gvbMewdBFmjw+F4ORj4OAEOj+MdAMw+ZENG1Wzf8tpIrDUkIrMmDLrL5RT9cMCRMP2cC7fv9+BA6xZk176JdYZ4rKgVociUjcERB8jOOp3OV8cJCN6MjAzOBcD63TZ8qJ/ty66Jwqp6FVZSBhquaUIECMH3O9JehvSqMH8Ay2pEKGyepIDFutm+zGoxOVdhee1MGK5OTsCP58uQqgvD6mNJyKyej20nQgQITwEvIEU7x7e0Soyc2iQka8NQcbEczr8gwE5TcIumoPRUAZbop9P8J2Cpfj6+NGbDJSAgZBFyw3asqU9hS3SRyK6JR4puFnafzEd3b+eEi5CjRdhmNWFDQyoyqqOQVaOkbM5HWes2oUUYWobuUSdKTxdgQWU4PtbL8UmVCmm6N1B3pRLOx5Uh56Iy7MXBs3tI9NvIqo6n6OMomyJmtOjhZaFlKLgRXb17CfKK91maNpYGUSJdJ0VOjQxmi5HmWHgj0l46TBHPpCpS+knVRmFZtZQqS2AjEtqKXSMcdrXksYSKCCzWykkAjwyZeinKzhSi666Faj64FdvR0XMOxaYNlKkIEqzARzo5vSeFquID1F9Twye8FQt/jG7ct2CRVsySNbE0mAJpWhmWBCEx6eOhZ2PtfP8kjRgbG7KY3S34MZr4c2y2NjGZeh6SNTKkVCqwsFKOVGJRpYwEjbGIoGd8G/VTIpFE09phfBC8+XwCn+OJDiSUOhLRyNK1KigrpEiqUJAYORYIooBcHUNVlIHuB4IHkskfyXoedrEiUz4U6jio1ArEq+VIeIREQkUspEyVnysF5+4XOJL9s0MppfRXHGzbjdz6FeRQCdlRmg5NIrY05uLoxe/wwNEreCh9osfyJ/fH5Kn91+1P0yiqwz6mfpkAAAAASUVORK5CYII=");
		} else {
			$("#connection_status").attr("value","1");
			$("#connection_status").html("在线");
			$("#changeOnline a").html('<i class="fa fa-random"></i>离线');
			$("#connection_icon").attr("src","data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAMAAABEpIrGAAABmFBMVEUAAAD////////////////////////////////////2+/LR5bKw1Hmfy1KUxz2VyD2izVKz1nnS5rP////A3JuOw0qKwkCNxD+QxT6Sxj6Txz6SxUnC3Jv1+fGXx2GDvkCGwECIwUCLwj+PxD6PxT+JwUCFwECZyGD2+vGSxWF9vEGAvkGDv0CMwz+Wx2GPw2F4ukJ7u0J+vUGBvkGHwUB8u0KSxGG31pp0uEN3uUJ5u0KFv0CCv0B6u0K415p5uU1yt0N/vUF1uEN8u0zG3bFttURwtkR5ukLH3rGWxnlqtERutUR2uUOZx3l6uVZos0VvtkRxt0Nzt0N8ulVisUVlskVns0VzuENmskVfsEVps0VztlZer0VhsEVjsUVstER1t1aOwXhcrkZdr0VgsEaQwnm/2a9YrUZbrka/2rDz+PFhr09XrEZksE6pzplUq0ZVrEZarUaqzpl0tWJRq0dWrEZ1tmJztWJOqUdSq0dxtGJMqEdNqUdQqkdytWKmzJhXrFBKqEdZrU+716+GvXhjr1dIp0hkr1dYtVOVAAAAFHRSTlMAV8/v/wCH+x/n////////////9kvBHZAAAAG7SURBVHgBvdOxjtNAEIDhGe/MZO3sxVaiIJkiSNdQUPJOeQlqXoCCIg/EU9BQHRKg5CT7ErzrHTa+aBOqaxC/tdLK+2kbj+H/hoWhlCmQr0HeyYxyM8mvkWHKoAfBS6cBWEeYugAzf4QGp1SV8DvU/ZjBdN7iud6hdnOTdl+TuALyrUPEwfdu3nc1ipr9AwdIFZPysJylRDfa6cZL2rfgMd9QjO8R0Y+/u7sa4LHZz4wN/MXEyw1hbK1VZdV7PZ1OyufzktsxXADCW5EkXq06Paan02Uoo3kHmAEzJ8HBN6v5qlkqaxTmCdAzQK8Noi6rXwCrJyutepUMAARnXS++3cvm2xvftR0PzAyQAXtwdNChifvFHppBdR003IDCIg6JDOse4DX8WIdo1TwfpaUgqWC9c4eqqg5HF20QZdAMmDlasdHWkrKR03J0A4iIXRTrpba29laiY8YMyOyMKYkXroyROZZuwVTyztAFJPmZKBGq+FxFVBr5BHr7ubd3GICfAM+88qDHHYe/BmbbIAaGKU/Fz10emDxyHxBhgJTg+DGP3O3QbltMBkd92F2H9sWxB772wo9z2z8FfwDHWbdKLDfq1AAAAABJRU5ErkJggg==");
		}
	});
	/*---------------------------------------------管理用户页面(添加/删除/列表)-----------------------------------------------------------*/
	$("#adminadduser").on('click',function(){
		$.ajax({
		    url : $.appClient.generateUrl({ESUserInfo : 'toUserList'},'x'),
		    success:function(data){
	    		$.dialog({
			    	title:'普通用户管理',
		    	   	fixed:false,
		    	    resize: false,
		    	    lock : true,
					opacity : 0.1,
					padding:0,
					width: 700,
					height: 400,
				    content:data
			    });
			  },
		      cache:false
		});
	});
	
	/*--------------------------------------------跳转到激活页面----------------------------------------------------------*/
	$("#toActiveHtml").click(function(){
		var url = {};
		url['ESUserInfo'] = 'toActive';
		window.open($.appClient.generateUrl(url), "_self");
	});
	
	
})
