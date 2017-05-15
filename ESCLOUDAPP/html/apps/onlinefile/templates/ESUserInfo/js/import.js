	function importuser(){
		var url=$.appClient.generateUrl({ESUserInfo:'importStep1'},'x');
		$.ajax({
		    url:url,
		    success:function(data){
		    	$.dialog({
		    		id:'importDialog',
		    		title:'数据导入（第一步）',
		    		width: '500px',
		    	   	height: '120px',
		    	    fixed:true,
		    	    resize: false,
		    		content:data,
		    		cancelVal: '关闭',
		    		cancel: true,
		    		padding: '10px',
		    		button: [
		        		{id:'btnNext', name: '导入', callback:importSetting}
		    		],
		    		cancel:function(){
		    			this.close();
		    		}
		    	});	 
		    }
		});
	}
	function importSetting(){
		$.ajax({
			async : false,
			url:$.appClient.generateUrl({ESUserInfo:'validateAndParseExcel'},'x'),
			success:function(url){
				$('#importStep1').ajaxSubmit({
					url:url,
					dataType:"text",
					success:function(data){
						if(data == "success"){
							importSettingSuccess();
							art.dialog.list["importDialog"].close();
						}else{
					    	//$.dialog.notice({icon:'warning',content:data,time:2});
							showBottomMsg(data,2);
						}
					},
					error:function(){
						//$.dialog.notice({icon:'error',content:"系统错误，请联系管理员"});
						showBottomMsg('系统错误，请联系管理员',3);
					}
				});
			}
		});
		return false;
	}
	function importSettingSuccess()	{
		$.ajax({
			async : false,
			url:$.appClient.generateUrl({ESUserInfo:'importStep2'},'x'),
			success:function(data){
				$.dialog({
					id:'importStep2',
					title:'数据导入（第二步）',
					width: '860px',
					height: '560px',
					fixed:true,
					resize: false,
					content:data,
					cancelVal: '关闭',
					cancel: true,
					padding: '0',
					button: [
					    {id:'btnImport', name: '导入',callback:realImport}
					],
					cancel:function(){
						closeWin();
		    		}
				});	 
			}
		});
	}
	function realImport(){
		if($("input[name='fullNameImport']").length == 0){
			//$.dialog.notice({icon:'warning',content:"没有数据无法导入！",time:2});
			showBottomMsg('没有数据无法导入！',2);
			return false;
		}
		if($("#isImport").val()=="true"){
			//$.dialog.notice({icon:'warning',content:"当前页已经导入，请选择其他页！",time:2});
			showBottomMsg('当前页已经导入，请选择其他页！',2);
			return false;
		}
		var flag = true;
		$("input[name='fullNameImport']").each(function(i){
		    if($.trim($(this).val())==''){
		    	$("#name_"+i).showTooltips("姓名不能为空！", 5000) ;
		 		flag = false;
		 	} else if(nameZZ.test($(this).val())==false){
		 		$("#name_"+i).showTooltips("只能包含中英文字符!", 5000) ;
		 		flag = false;
		 	}
		});	
		$("input[name='emailImport']").each(function(i){
			if($.trim($(this).val())==''){
		    	$("#email_"+i).showTooltips("邮箱不能为空!", 5000) ;
		    	flag = false;
		 	} else if(emailaddressZZ.test($(this).val())==false){
		 		$("#email_"+i).showTooltips("邮箱格式不正确!", 5000) ;
		 		flag = false;
		 	}
		});	
		//选择框的input
		if(!flag){
			return flag;
		}
		var list = new Array();
		$("input[name='fullNameImport']").each(function(i){
			var fullNameImport = $.trim($(this).val());
			var emailImport = $.trim($("input[name='emailImport']")[i].value);
			var mobilephoneImport = $.trim($("input[name='mobilephoneImport']")[i].value);
			var teleohoneImport = $.trim($("input[name='teleohoneImport']")[i].value);
			var faxImport = $.trim($("input[name='faxImport']")[i].value);
			var positionImport = $.trim($("input[name='positionImport']")[i].value);
		    list.push({"fullNameImport":fullNameImport,"emailImport":emailImport,"mobilephoneImport":mobilephoneImport,"teleohoneImport":teleohoneImport,"faxImport":faxImport,"positionImport":positionImport});
		});	
		$('#btnImport').attr('disabled',"true");
		$.ajax({
			url:$.appClient.generateUrl({ESUserInfo:'realImport'},'x'),
			data:{list:list,fromfullname:$("#current_user_name").html(),companyid:$("#flyingchat").attr("companyId"),nowPageStep2:$('#nowPageStep2').html()},
			type:"post",
			dataType:'json',
			success:function(data){
				if(data.success){
					if(data.importOk){
						//$.dialog.notice({icon:'warning',content:"已经全部导入,飞扬小强正在快速的奔跑，逐一发送邀请邮件，请稍等！",time:5});
						showBottomMsg('已经全部导入,飞扬小强正在快速的奔跑，逐一发送邀请邮件，请稍等！',1);
						reloadUsers(1) ;
						art.dialog.list["importStep2"].close();
					}else{
						$("#titleshow").text("当前页已成功导入！");
						//$.dialog.notice({icon:'warning',content:"当前页已经导入,飞扬小强正在快速的奔跑，逐一发送邀请邮件，请稍等！",time:5});
						showBottomMsg('当前页已经导入,飞扬小强正在快速的奔跑，逐一发送邀请邮件，请稍等！',1);
					}
					$("#isImport").val("true");
					$('#btnImport').removeAttr("disabled"); 
				}
			}
		});
		return false;
	}
	function closeWin(){
		$.ajax({
			url:$.appClient.generateUrl({ESUserInfo:'closeImport'},'x'),
			type:"post",
			dataType:'json',
			success:function(data){
				if(data.success){
					reloadUsers(1) ;
				}
			}
		});
		return false;
	}