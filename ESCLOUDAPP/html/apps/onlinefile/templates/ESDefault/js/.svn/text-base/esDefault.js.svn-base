!function(e){
	window.onresize = function(){
		/** 自适应设置 */
		var bodyHeight = document.documentElement.clientHeight; // 可见区域高度
		$("#chatDiv").height(bodyHeight-210);
		$('#receiveMessageDiv').height(bodyHeight-210);
		// longjunhao 20150812 注释掉，解决左侧滚动条问题
//		$("#group-left").height(bodyHeight-68);
		
	   	//wangwenshuo 20151105 分类高度
	   	$("#classesList").css("height",$(window).height()-58);

		if($("#content-footer").attr("class").indexOf("hidden")==-1){ // 如果显示footer
			$("#content-list").height(bodyHeight-220);
		} else {
			$("#content-list").height(bodyHeight-220 + contentFooterHeight);
		}
		$("#content-list4FileInfo").height(bodyHeight-220);
		
		$("#messageValue").width($("#content-list4FileInfo").width()-150).css("margin-left",32);
		$(".sendCommentBtnContainerCls").css("margin-left",$("#content-list4FileInfo").width()-110);
		
		$("div.searchAllPanelBody").height(bodyHeight-210);
		$("#action-content-panel-searchAll").width($("#mainContentLeft > .panel").width()).css({top:113});
		$("#action-content-panel-searchTrash").width($("#mainContentLeft > .panel").width()).css({top:113});
		
		if($("#uploadAllBtnDivId").css("top")!="0px"){
			$("#uploadAllBtnDivId").css("right",$("#flyingchat").width()+82+"px");
			// longjunhao 20151020 注释掉下面代码，暂时没有发现用处。
			/**
			var $canUploadImg = $("#cantuploadimgid").offset();
			var $mainContentRight = $("#mainContentRight").offset();
			var $groupLeft = $("#group-left").offset();
			if($mainContentRight.top<200){
				$("#uploadAllBtnDivId").css("right",$("#flyingchat").width()+82+"px");
			}else{
				if($groupLeft.top<200 && $groupLeft.top>0){
					$("#uploadAllBtnDivId").css("right","120px");
				}else{
					$("#uploadAllBtnDivId").attr("style","position: absolute;top: 0px;width: 79px;height: 35px; ");
				}
			}
			*/
		}
	};
	/** 自适应设置 */
	var bodyHeight = document.documentElement.clientHeight; // 可见区域高度
	var contentFooterHeight = 45; // panel的footer高度
	$("#content-list").height(bodyHeight-220);
	$("#content-list4FileInfo").height(bodyHeight-220+contentFooterHeight);
	$("#chatDiv").height(bodyHeight-210); /** 设置右侧消息框内容的高度 */
	$('#receiveMessageDiv').height($("#chatDiv").height());
	
	$("div.searchAllPanelBody").height(bodyHeight-210);
	$("#action-content-panel-searchAll").width($("#mainContentLeft > .panel").width()).css({top:113});
	$("#action-content-panel-searchTrash").width($("#mainContentLeft > .panel").width()).css({top:113});
	
	template.helper("hex_md5", hex_md5) ;
	template.helper("groupnewmessagestyle", $.WebIM.groupnewmessagestyle) ;
	template.helper("groupnewmessagestyleclass", $.WebIM.groupnewmessagestyleclass) ;
	template.helper("groupnewmessagevalue", $.WebIM.groupnewmessagevalue) ;
	template.helper("groupnewmessageCounter", $.WebIM.groupnewmessageCounter) ;
	template.helper("showText", $.WebIM.showText) ;
	/** lujixiang 20150831 与reloadCompany函数调用重复 **/
//	if(window.userCompanySize*1 > 1){
//		if(window.isAdmin == "1"){
//			$("#userCompanysDiv").css("max-height", (document.documentElement.clientHeight-450)+"px");
//		} else {
//			$("#userCompanysDiv").css("max-height", (document.documentElement.clientHeight-370)+"px");
//		}
//		$.ajax({
//			type:'POST',
//	        url : $.appClient.generateUrl({ESDefault : 'getUserCompanys'},'x'),
//	        datatype:"json",
//	        data:{'reload':1},
//		    success:function(data){
//		    	var json = JSON.parse(data) ;
//		    	var html =  template('userCompanys_template',json) ;
//		    	setTimeout(function(){
//		    		$("#userCompanysDiv").html(html) ;
//		    		var obj = $("#userCompanysDiv") ;
//		    		obj.perfectScrollbar();
//					obj.scrollLeft(300);
//					obj.perfectScrollbar('update');
//					//遍历让红点显示
//					$("#userCompanysDiv .").each(function(){
//						var userid = $(this).attr('companyClassId');
//						if(userid!="" && userid == window.userId){
//							$(this).find('#companynewmessage').css('display','block');
//						}else{
//							$(this).find('#companynewmessage').css('display','none');
//						}
//					});
//					$("#userCompanysDiv").on("mouseover", function(){
//	    				$("#userCompanysDiv").perfectScrollbar('update');
//	    			});
//		    		$("#navbar").on("click",".inSideNewCompanyRegister", function(){
//		    			if($(this).find('#companynewmessage').css('display') =='none'){
//		    				var companyid = $(this).attr("companyid") ;
//			    			var url = window.location.href ;
//			    			url = url.substring(0, url.indexOf("?")) ;
//			    			window.location.href = url + "?companyid=" + companyid
//		    			}else{
//		    				var companyid = $(this).attr("companyid") ;
//			    			var companyName = $(this).attr("companyName") ;
//			    			$.dialog({
//			    				content : '邀请您加入 '+companyName+' 团队！',
//			    				okValue : '加入',
//			    				cancelValue : '拒绝',
//			    				ok : function() {
//			    					jQuery.getJSON(window.onlinefilePath+'/rest/onlinefileuser/agreenInSideCompany?callback=?',{'userId':window.userId,'companyId':companyid,'username':window.userName},
//											function(json) {
//										//正文
//										if(json.isOk){
//											reloadCompany();
//											//跳转
//											var url = window.location.href ;
//							    			url = url.substring(0, url.indexOf("?")) ;
//							    			window.location.href = url + "?companyid=" + companyid
//											showBottomMsg('成功加入'+companyName+'团队！', 1);
//										}else{
//											showBottomMsg('加入'+companyName+'团队失败！', 2);
//										}
//										
//									});
//								},
//								cancel : function(){
//									jQuery.getJSON(window.onlinefilePath+'/rest/onlinefileuser/noAgreenInSideCompany?callback=?',{'userId':window.userId,'companyId':companyid,'username':window.userName},
//											function(json) {
//										//正文
//										if(json.isOk){
//											reloadCompany();
//											showBottomMsg('您已拒绝加入'+companyName+'团队！', 1);
//										}else{
//											showBottomMsg('拒绝加入'+companyName+'团队失败！', 2);
//										}
//									});
//								}
//			    			}).show(); 
//		    			}
//		    			 
//		    		}) ;
//		    		
//		    	}, 500);
//		    },
//		    cache:false
//		});
//	}
	/** 引导页JS,如果用户首次登录系统，那么window.isFirst为1，此时开始显示引导功能 **/
	if(window.isFirst == 1){
		$("#walkthrough-content").removeClass("hidden");
	  // Set up tour
    $('body').pagewalkthrough({
        name: 'introduction',
        steps: [{
           popup: {
               content: '#walkthrough-1',
               type: 'modal'
           }
        }, {
            wrapper: '#newClassId',
            popup: {
                content: '#walkthrough-2',
                type: 'tooltip',
                position: 'right'
            }
        }, {  
            wrapper: '#privateClass',  
            popup: {
                content: '#walkthrough-3',
                type: 'tooltip',
                position: 'right'
            }
        },{  
            wrapper: '#publicClass',  
            popup: {
                content: '#walkthrough-4',
                type: 'tooltip',
                position: 'right'
            }
        }, {
            wrapper: '#userMenusLi',
            popup: {
                content: '#walkthrough-5',
                type: 'tooltip',
                position: 'bottom'
            }
        }, {
            wrapper: '#searchFileInputId',
            popup: {
                content: '#walkthrough-6',
                type: 'tooltip',
                position: 'bottom'
            }
        }]
    });

    $('body').pagewalkthrough('show');
	}
	$("#userMessagesUl").perfectScrollbar();
}();

/** 鼠标是否可以拖拽到目标  **/
function allowDrop(ev,obj)
{
	ev.preventDefault();
	
	if($(obj).attr("id")!= "flyingchat"){
		$(obj).addClass("drag-hover");
		$(".drag-hover").each(function(){
			if($(obj).is($(this))){
				$(this).addClass("drag-hover");
			}else{
				$(this).removeClass("drag-hover");
			}
		});
	}
	
}

/**  鼠标拖拽时候触发  **/
function drag(ev)
{
	ev.dataTransfer.setData("Text",ev.target.id);
}

function dragend(ev,obj){
	$(".drag-hover").each(function(){
		$(this).removeClass("drag-hover");
	});
}

/** 鼠标放下时候触发  **/
function drop(ev,obj)
{
	ev.preventDefault();
	var currentId = ev.dataTransfer.getData('text');
	var currentObj = $("#"+currentId);
	var targetSelected = $(obj);
	
	
	
	/** 文件拖拽开始执行跟后台  **/
	if(undefined!=targetSelected.attr("isfile") && targetSelected.attr("isfile") == 0){
		
		/**   如果退拽的文件为私密文件那么不让拖拽  **/
		
		if(currentObj.attr("openlevel") == 3 && currentObj.attr("ownername") != window.userName){
			showBottomMsg('拖拽失败,该文件是私密文件!', 3);
			return;
		}
		
		/**  拖拽时候判断当前的拖拽是否在自己的所属分类，是不是访客身份访问的  **/
		if($("#publicClassSub").find(".active .glyphicon-lock").css("display")== "block"){
			showBottomMsg('亲,您还不是该分类的成员呢!', 2);
			return ;
		}else{
			var clsName = targetSelected.attr("filename");
			var content = "确定要将文件放到文件夹【"+clsName+"】吗?";
			$.dialog({
				content : content,
				quickClose: true,
				okValue : '确定',
				ok : true,
				cancelValue : '取消',
				cancel : true,
				ok : function() {
					shareFileToFolderByDrag(currentObj,targetSelected);
				}
			}).show();
		}
		/**   将我的文档拖拽到分类里面  **/
	}else if(currentObj.attr("class").indexOf("myDocument")>-1 && targetSelected.attr("id") != "flyingchat"){
		var clsName = targetSelected.attr("groupname");
		var isMember = targetSelected.attr('isMember');
		//文件拥有这位         分类内成员，文件非私密文件                                                                      不是分类内成员，文件为公司级公开
		if(isMember=="false" ){
			showBottomMsg('您不是该分类的成员,不能发送文件到该分类!', 2);
			return false;
		}
		var content = "确定要将文件发送到分类【"+clsName+"】吗?";
		$.dialog({
			content : content,
			quickClose: true,
			okValue : '确定',
			ok : true,
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				shareFileToClassByDrag(currentObj,targetSelected);
			}
		}).show();
		
	}else if(targetSelected.attr("id") == "flyingchat"){
		var $fileItem = currentObj;
		
		/* wangwenshuo 2151221 添加分享权限判断 */
		var owner = $fileItem.attr('owner');
		var owner_v1 = $fileItem.attr('owner_v1');
		var openlevel = $fileItem.attr('openlevel');
		var isOwner = (owner == g_userId) ? true : false;
		var isV1Owner = (owner_v1 == g_userId) ? true : false;
		var isMember = $fileItem.attr('isMember');
		//文件拥有者，文件非私密                                                                           第一版本文件拥有者                                                                分类内成员，文件非私密文件                                                                      不是分类内成员，文件为公司级公开
		//if((isOwner && openlevel != '3') || (isV1Owner && openlevel == '3') || (isMember=='true' && openlevel != '3') || (isMember=="false" && openlevel == '1')){
		if(openlevel == '1' || (openlevel == '2' && isMember=='true') || (isV1Owner && openlevel == '3')){
		
		}else{
			showBottomMsg("您没有该文件的分享权限！","2");
			return;
		}
		
		var isgroup = $('#receiverusername').attr('isgroup');
		var name = $('#receiverusername').attr("groupName");
		if(name=='fyBot'){
			showBottomMsg("机器人无法接收您分享的文档哦，请分享给您的其他小伙伴吧！","3");
			return;
		}
		var content="";
		if(isgroup =='0'){
			content = "确定要将文件发送到用户【 "+name+"】的聊天窗口吗？"
		}else if(isgroup =='1'){
			var groupFlag = $('#receiverusername').attr('groupflag');
			var hexname = hex_md5(groupFlag);
			var obj = $('#newmessage'+hexname) ;
			if(obj.attr("class")=="classnewmessage"){
				content = "确定要将文件发送到分类【"+name+"】的聊天窗口吗？"
			}else if(obj.attr("class")=="newmessage"){
				content = "确定要将文件发送到分组【"+name+"】的聊天窗口吗？"
			}
		}
		
		var html = [
			'<div id="fileAccessRight" class="alert alert-warning" style="margin:10px 0px -5px 0px;">',
			'	<span class="tips"><strong>请配置【'+name+'】拥有的文件权限</strong></span>',
			'	<div class="options">',
			'		<div class="radio">',
			'		    <label>',
			'		      <input type="radio" name="optionsRadios" value="1" checked> 浏览权限',
			'		    </label>',
			'		</div>',
			'		<div class="radio">',
			'		    <label>',
			'		      <input type="radio" name="optionsRadios" value="3"> 浏览、下载权限',
			'		    </label>',
			'		</div>',
			'	</div>',
			'</div>'
		].join('');
		
		$.dialog({
			content :content+html,
			quickClose: true,
			modal:true,
			okValue : '确定',
			ok : true,
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				var accessRight = $("#fileAccessRight input[name='optionsRadios']:checked").val();
				$fileItem.attr("accessright",accessRight);
				shareAction($fileItem);
			}
		}).show();
		
	}
	
}

function shareFileToClassByDrag(currentObj,targetSelected){
	var fromCompanyId = "user_"+window.userId;  //实际为用户我的文档文件
	var targetFolderId = targetSelected.attr("id");
	var targetFolderName = targetSelected.attr("title");
	var sourceFileId = currentObj.attr("fileid");
	documentCenter.fileShareTo(fromCompanyId,sourceFileId,g_companyId,targetFolderId,targetFolderName);
}

function shareFileToFolderByDrag(currentObj,targetSelected){
	var sourceFileId = currentObj.attr("fileid");
	var sourceFileTitle = currentObj.attr("filetitle");
	var targetDocIdSeq = targetSelected.attr("idseq");
	var targetDocId = targetSelected.attr("fileid");
	var isMyDocmentFlag = false;
	if($("#myDocument").attr("class") == "active"){
		isMyDocmentFlag = true;
	}
	$.ajax({
		type:'POST',
        url : $.appClient.generateUrl({ESDocumentCenter : 'dragFileToDocumnet'},'x'),
        data:{"companyId":window.companyid, "sourceFileId":sourceFileId,"targetDocIdSeq":targetDocIdSeq,"targetDocId":targetDocId,"isMyDocmentFlag":isMyDocmentFlag,"userId":window.userId,"sourceFileTitle":sourceFileTitle},
        datatype:"json",
	    success:function(data){
	    	var json = JSON.parse(data);
	    	if(json.success == "true"){
	    		var folderId = $('#file-breadcrumbs .last').attr('data-folder-id');
	    		documentCenter.getFileList(folderId);
	    		showBottomMsg('文件拖拽成功!', 1);
	    	}else{
				//showBottomMsg('亲是不是哪里出问题了,咋拖不进去呢?', 3);
				showBottomMsg('文件拖拽失败,请重新再试!', 3);
	    	}
	    }
	});
}

/**   拖拽后的事件  **/
function afterDragEve(selected,currentObj,targetSelected){ 
    if(selected && (currentObj.attr("fileid")!= targetSelected.attr("fileid")) ){
    	/** 文件拖拽  **/
    	if(undefined!=targetSelected.attr("isfile") && targetSelected.attr("isfile") == 0){
    		
    		/**   如果退拽的文件为私密文件那么不让拖拽  **/
    		
    		if(currentObj.attr("openlevel") == 3 && currentObj.attr("ownername") != window.userName){
    			showBottomMsg('拖拽失败,该文件是私密文件!', 3);
    			return;
    		}
    		
    		/**  拖拽时候判断当前的拖拽是否在自己的所属分类，是不是访客身份访问的  **/
    		if($("#publicClassSub").find(".active .glyphicon-lock").css("display")== "block"){
				showBottomMsg('亲,您还不是该分类的成员呢!', 2);
				return ;
    		}else{
    			var sourceFileId = currentObj.attr("fileid");
    			var sourceFileTitle = currentObj.attr("filetitle");
    			var targetDocIdSeq = targetSelected.attr("idseq");
    			var targetDocId = targetSelected.attr("fileid");
    			var isMyDocmentFlag = false;
    			if($("#myDocument").attr("class") == "active"){
    				isMyDocmentFlag = true;
    			}
    			$.ajax({
    				type:'POST',
    		        url : $.appClient.generateUrl({ESDocumentCenter : 'dragFileToDocumnet'},'x'),
    		        data:{"companyId":window.companyid, "sourceFileId":sourceFileId,"targetDocIdSeq":targetDocIdSeq,"targetDocId":targetDocId,"isMyDocmentFlag":isMyDocmentFlag,"userId":window.userId,"sourceFileTitle":sourceFileTitle},
    		        datatype:"json",
    			    success:function(data){
    			    	var json = JSON.parse(data);
    			    	if(json.success == "true"){
    			    		var folderId = $('#file-breadcrumbs .last').attr('data-folder-id');
    			    		documentCenter.getFileList(folderId);
    			    		showBottomMsg('文件拖拽成功!', 1);
    			    	}else{
	    					showBottomMsg('亲是不是哪里出问题了,咋拖不进去呢?', 3);
    			    	}
    			    }
    			});
    		}
    		/**   将我的文档拖拽到分类里面  **/
    	}else if($("#myDocument").attr("class") == "active" && targetSelected.attr("id") != "receiveMessageDiv"){
    		var fromCompanyId = "user_"+window.userId;  //实际为用户我的文档文件
    		var targetFolderId = targetSelected.attr("id");
    		var targetFolderName = targetSelected.attr("title");
			var sourceFileId = currentObj.attr("fileid");
    		documentCenter.fileShareTo(fromCompanyId,sourceFileId,g_companyId,targetFolderId,targetFolderName);
    	}else if(targetSelected.attr("id") == "receiveMessageDiv"){
    		
    	}
    }else{
    	/**   如果没有放到选取的fun Zone  **/
    	//alert($("#myDocument").attr("class"));
        //alert("没有拖动到指定的区域里哦!");
    	return true;
    }
}

/**
 * wangwenshuo 20160222 拖动文件到文件夹  选择文件夹时控制滚动条位置
 */
function file2FolderDragover(ev){
	ev.preventDefault();
	var currentId = ev.dataTransfer.getData('text');
	//ev.clientY;   返回当事件被触发时，鼠标指针的垂直坐标。
	
	var height = $("#content-list").scrollTop();  //当前滚动条位置
	//如果拖动到上方第一行，如果当前不是第一行就向上滚动
	if(ev.clientY<190 && height>0){
		var increment = (190-ev.clientY)/3; 	//步长   控制滚动跳上滑速度
		height = height-increment;
		$("#content-list").scrollTop(height);
	}
	
	//向下滚动
	if(($(window).height()-ev.clientY)<75){
		var increment = ($(window).height()-ev.clientY-45)/3; 	//步长   控制滚动跳上滑速度
		$("#content-list").scrollTop(height+increment);
	}
}

/**
 * wangwenshuo 20160222 拖动文件到分类  选择分类控制滚动条位置
 */
function file2ClassDragover(ev){
	ev.preventDefault();
	
	var scrollHeight = $("#classesList").scrollTop();  //当前滚动条位置
	//向下滚动
	if(($(window).height()-ev.clientY)<30){
		var increment = ($(window).height()-ev.clientY)/3; 	//步长   控制滚动跳上滑速度
		$("#classesList").scrollTop(scrollHeight+increment);
	}
	
	//向上滚动
	if(ev.clientY<85 && scrollHeight>0){
		var increment = (85-ev.clientY)/3;
		$("#classesList").scrollTop(scrollHeight-increment);
	}
}

function updateAbout(){
	window.open('apps/onlinefile/templates/ESDefault/update_announcement.html'); 
}

function reloadCompany(){
	$("#userCompanysDiv").html("") ;
	/** lujixiang 20151104 修复被邀请时刷新页面无法获取被邀请的公司 **/
	// if(window.userCompanySize*1 > 1){
	
		if(window.isAdmin == "1"){
			$("#userCompanysDiv").css("max-height", (document.documentElement.clientHeight-450)+"px");
		} else {
			$("#userCompanysDiv").css("max-height", (document.documentElement.clientHeight-370)+"px");
		}
	
		$.ajax({
				type:'POST',
		        url : $.appClient.generateUrl({ESDefault : 'getUserCompanys'},'x'),
		        data:{'reload':1},
		        datatype:"json",
			    success:function(data){
			    	var json = JSON.parse(data) ;
			    	var html =  template('userCompanys_template',json) ;
			    	setTimeout(function(){
			    		$("#userCompanysDiv").html(html) ;
			    		if($("#userCompanysDiv .ps-scrollbar-y-rail").length>0){
			    			$("#userCompanysDiv").perfectScrollbar('update');
			    		}else{
			    			var obj = $("#userCompanysDiv") ;
			    			obj.perfectScrollbar();
			    			obj.scrollLeft(300);
			    			obj.perfectScrollbar('update');
			    		}
			    		$("#userCompanysDiv").on("mouseover", function(){
			    			$("#userCompanysDiv").perfectScrollbar('update');
		    			});
			    		//遍历让红点显示
						$("#userCompanysDiv .inSideNewCompanyRegister").each(function(){
							var userid = $(this).attr('companyClassId');
							if(userid!="" && userid == window.userId){
								$(this).find('#companynewmessage').css('display','block');
							}else{
								$(this).find('#companynewmessage').css('display','none');
							}
						});
			    		$("#navbar").off("click",".inSideNewCompanyRegister").on("click",".inSideNewCompanyRegister" ,function(){
			    			if($(this).find('#companynewmessage').css('display') =='none'){
			    				if(window.companyid == $(this).attr("companyid")){
			    					showBottomMsg('目前所处于该企业,无需切换...', 2);
			    				}else{
			    					//判断该用户是否被禁用
				    				var companyUserIsStatus = $(this).attr("companyUserIsStatus") ;
				    				if(companyUserIsStatus == 1){
				    					var companyid = $(this).attr("companyid") ;			    					/**20151020 xiayongcai 当用户切换公司并记录为下次登录的企业*/
				    				   	var param ={"userId":window.userId,"companyId":companyid};
				    			        $.ajax({
				    			            type: 'POST',
				    			            url: $.appClient.generateUrl({ESCompanyRegist: 'switchoverCompany'},'x'),
				    			            data: param,
				    			            dataType : 'json',
				    			            success: function(data) {
				    			            	if(data.success== 'true'){
				    			            			var url = window.location.href ;
				    			    					url = url.substring(0, url.indexOf("?")) ;
				    			    					window.location.href = url + "?companyid=" + companyid
				    			    					showBottomMsg(data.msg, 1);
				    			            	}else{
				    			            		//alert(data.msg);
				    			            		showBottomMsg(data.msg, 2);
				    			            	}
				    			            }
				    			        });
				    				}else{
				    					//alert("该企业已将您禁用");
				    					showBottomMsg('该企业已将您禁用!', 2);
				    				}
			    				}
			    			}else{
			    				var companyid = $(this).attr("companyid") ;
				    			var companyName = $(this).attr("companyName") ;
				    			var obj = $.dialog({
				    				title:"团队邀请",
				    				content : '邀请您加入 '+companyName+' 团队！',
				    				cancelValue : '关闭',
				    				button: [
								        {
								            value: '加入',
								            callback: function () {
								                var url = window.onlinefilePath+'/rest/onlinefileuser/agreenInSideCompany?callback=?';
												var data = {'userId':window.userId,'companyId':companyid,'username':window.userName};
												var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
												jQuery.getJSON(url, ret.data,
														function(json) {
													//正文
													if(json.isOk){
														reloadCompany();
														//加入到openfire
														var arg = 'type=reset_group&groups=company'+companyid+'&addgroupusernames='+window.userName.replace("@", "\\40")+'&secret=flyingsoft';
														remote.jsjac.chat.ofuserservice(arg, false) ;
														//跳转
														var url = window.location.href ;
										    			url = url.substring(0, url.indexOf("?")) ;
										    			window.location.href = url + "?companyid=" + companyid
														showBottomMsg('成功加入'+companyName+'团队！', 1);
													}else{
														showBottomMsg('加入'+companyName+'团队失败！', 2);
													}
													
												});
								            },
								            autofocus: true
								        },
								        {
								            value: '拒绝',
								            callback: function () {
								                var url = window.onlinefilePath+'/rest/onlinefileuser/noAgreenInSideCompany?callback=?';
												var data = {'userId':window.userId,'companyId':companyid,'username':window.userName};
												var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
												jQuery.getJSON(url, ret.data,
														function(json) {
													//正文
													if(json.isOk){
														reloadCompany();
														showBottomMsg('您已拒绝加入'+companyName+'团队！', 1);
													}else{
														showBottomMsg('拒绝加入'+companyName+'团队失败！', 2);
													}
												});
								            }
								        }
								    ]
									
				    			}).show();
			    			}
			    		}) ;
			    	}, 500);
			    },
			    cache:false
			});
	// }
}
$(function(){
	reloadCompany();
	//$("#searchFileInputId").css("margin-left",$("#group-left").width());
	//$("#searchSelectDivId").css("left",$("#group-left").width()+61);
	var userName = window.userName;
	if (userName != null) {
		userName = userName.replace("@", "\\40");
		$.WebIM({
			sender: userName,
			receiver: ''
		});
	}
    $("#chatuserpicicon").on("click", function(){
    	var obj = $("#peguserpic") ;
    	if(obj.attr("class") == "peguserpic-default"){
    		obj.removeClass("peguserpic-default").addClass("peguserpic-archive") ;
    		$(this).attr("title", "解除当前会话锁定");
    		obj.attr("title", "当前处于锁定状态，点击左侧分类不会自动化进入分类的群聊会话。");
    	} else {
    		obj.removeClass("peguserpic-archive").addClass("peguserpic-default") ;
    		$(this).attr("title", "锁定当前会话");
    		obj.attr("title", "当前处于开放状态，点击左侧分类会自动化进入分类的群聊会话。");
    	}
    });
    //进入到用户社区
    $("#userCommnunity").on('click', function(){
		$.ajax({
			type:'GET',
	        url : $.appClient.generateUrl({ESUserCommunity : 'onlineCommnunity'},'x'),
		    success:function(data){
				$.fywindow({
					title:'用户社区',
					width: window.innerWidth- 225,
					height: window.innerHeight-130,
					content:data,
					style:'padding:0px;'
				});
				userCommunity.addAllcommunitylist(0);
				userCommunity.getCountReplyInfo();
		    },
		    cache:true
		});
		
	});
    
    
    $("#aboutMine").on('click', function(){
//    	window.open('apps/onlinefile/templates/ESDefault/update_announcement.html'); 
//    	window.open('apps/onlinefile/templates/ESDefault/testindex.html');
//    	window.open('apps/onlinefile/templates/ESDefault/get_help.html');
    	var data =  template('aboutMine_templete',{'username':window.userName}) ;
			 	setTimeout(function(){
					$.fywindow({
						title:'关于我们',
						width: window.innerWidth- 225,
						height: window.innerHeight-130,
						content:data,
						style:'padding:0px;'
					});
		    	}, 0);		
    });
    
    
   
   
    $("#newCompanyRegister").on('click', function(){
    	var data =  template('newCompanyRegister_template',{'username':window.userName}) ;
    	setTimeout(function(){
    		$.fywindow({
    			title:'注册新企业(团体)',
    			width: 530,
    			height: 300,
    			content:data,
    			style:'padding:0px'
    		});
    		$("#fyAgreement").on("click", function(){
				window.open('/Default/flyingsoft_service_agreement.html');
			});
			
    		$("#crSubmit").on("click", function(){
    			var companynamezz = /^[a-zA-Z0-9()\u4E00-\u9FA5_\[\] ]+$/ ;
				var companyAddressZZ=/^[^#%&*\/|:<>?\"]*$/;
    			var newCampanyName = $.trim($("#newCampanyName").val()) ;
    			var password = $.trim($("#registerpassword").val());
    			var email = window.userName ;
    			/*if(newCampanyName == "" || newCampanyName.length > 50 ){
    				showBottomMsg('企业(团队)名称的长度不能超过50个字符!', 2);
    				return ;
    			}*/
    			if(newCampanyName == ''){
    				showBottomMsg("企业(团队)名称不能为空!",2);
    				return false;
    			}
    			
    			if(companynamezz.test(newCampanyName) == false){
    				showBottomMsg("企业(团队)名称只支持中文、英文、数值、下划线、小括号和中括号。",2);
    				return false;
    			}
    			if(password == ""){
    				showBottomMsg('密码不能为空！', 2);
    				return ;
    			}
    			if(!document.getElementById("registerinfo").checked){
    				showBottomMsg('请阅读《飞扬服务协议》，并同意相关约束，才可以进行注册！', 2);
    				return ;
    			}
				var $btn = $(this).button('loading');
    			$.ajax({
					url:$.appClient.generateUrl({ESUserInfo : 'checkPasswordIsRight'},'x'),
					type:'POST',
					data:{userid:window.userId,password:password},
					datatype:"json",
					success:function(data) {
						json = eval('('+data+')');
	    				if(json.isOk){
	    					$.ajax({
	    			  			url:$.appClient.generateUrl({ESCompanyRegist : 'registerNewCampany'},'x'),
	    			  			type:'POST',
	    			  			data:{'companyName':newCampanyName, 'email':email, 'userid':window.userId},
	    			  			datatype:"json",
	    			  			success:function(data){
	    			  				var rst = JSON.parse(data);
	    			  				if (rst.success == 'true') {
	    			  					$.ajax({
	    			  						type:'POST',
	    			  						url : $.appClient.generateUrl({ESDefault : 'getUserCompanys'},'x'),
	    			  						data:{'reload':1},
	    			  						datatype:"json",
	    			  						success:function(data){
	    			  							var json = JSON.parse(data) ;
	    			  							var html =  template('userCompanys_template',json) ;
	    			  							setTimeout(function(){
	    			  								$("#userCompanysDiv").html(html) ;
	    			  								$("#userCompanysDiv").perfectScrollbar();
	    			  								$("#userCompanysDiv").on("mouseover", function(){
	    			  								$("#userCompanysDiv").perfectScrollbar('update');
	    			  								});
	    			  								//遍历让红点显示
	    			  								$("#userCompanysDiv .inSideNewCompanyRegister").each(function(){
	    			  									var userid = $(this).attr('companyClassId');
	    			  									if(userid!="" && userid == window.userId){
	    			  										$(this).find('#companynewmessage').show();
	    			  									}else{
	    			  										$(this).find('#companynewmessage').hide();
	    			  									}
	    			  								});

	    			  								$(".inSideNewCompanyRegister").on("click", function(){
	    			  									if($(this).find('#companynewmessage').css('display') =='none'){
	    			  					    				if(window.companyName == $(this).attr("companyname")){
	    			  					    					showBottomMsg('目前所处于该企业,无需切换...', 2);
	    			  					    				}else{
	    			  					    					//判断该用户是否被禁用
	    			  						    				var companyUserIsStatus = $(this).attr("companyUserIsStatus") ;
	    			  						    				if(companyUserIsStatus == 1){
	    			  						    					var companyid = $(this).attr("companyid") ;			    					/**20151020 xiayongcai 当用户切换公司并记录为下次登录的企业*/
	    			  						    				   	var param ={"userId":window.userId,"companyId":companyid};
	    			  						    			        $.ajax({
	    			  						    			            type: 'POST',
	    			  						    			            url: $.appClient.generateUrl({ESCompanyRegist: 'switchoverCompany'},'x'),
	    			  						    			            data: param,
	    			  						    			            dataType : 'json',
	    			  						    			            success: function(data) {
	    			  						    			            	if(data.success== 'true'){
	    			  						    			            			var url = window.location.href ;
	    			  						    			    					url = url.substring(0, url.indexOf("?")) ;
	    			  						    			    					window.location.href = url + "?companyid=" + companyid
	    			  						    			    					showBottomMsg(data.msg, 1);
	    			  						    			            	}else{
	    			  						    			            		//alert(data.msg);
	    			  						    			            		showBottomMsg(data.msg, 2);
	    			  						    			            	}
	    			  						    			            }
	    			  						    			        });
	    			  						    				}else{
	    			  						    					//alert("该企业已将您禁用");
	    			  						    					showBottomMsg('该企业已将您禁用!', 2);
	    			  						    				}
	    			  					    				}
	    			  					    			}else{
	    			  										var companyid = $(this).attr("companyid") ;
	    			  										var companyName = $(this).attr("companyName") ;
	    			  										$.dialog({
	    			  											title:"团队邀请",
	    			  											content : '邀请您加入 '+companyName+' 团队！',
	    			  											cancelValue : '关闭',
	    			  											button: [
    			  											         {
    			  											         	value: '加入', 
    			  											         	callback: function(){
	    			  											          	 var url = window.onlinefilePath+'/rest/onlinefileuser/agreenInSideCompany?callback=?';
																			 var data = {'userId':window.userId,'companyId':companyid,'username':window.userName};
																			 var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
																			 jQuery.getJSON(url, ret.data,
	    			  											        			 function(json) {
	    			  											        		 //正文
	    			  											        		 if(json.isOk){
	    			  											        			 reloadCompany();
	    			  											        			//加入到openfire
	    			  																var arg = 'type=reset_group&groups=company'+companyid+'&addgroupusernames='+window.userName.replace("@", "\\40")+'&secret=flyingsoft';
	    			  																remote.jsjac.chat.ofuserservice(arg, false) ;
	    			  											        			 //跳转
	    			  											        			 var url = window.location.href ;
	    			  											        			 url = url.substring(0, url.indexOf("?")) ;
	    			  											        			 window.location.href = url + "?companyid=" + companyid
	    			  											        			 showBottomMsg('成功加入'+companyName+'团队！', 1);
	    			  											        		 }else{
	    			  											        			 showBottomMsg('加入'+companyName+'团队失败！', 2);
	    			  											        		 }
	    			  											        		 
	    			  											        	 });
    			  											         	},
							            								autofocus: true
							            							},
    			  											        {
    			  											        	value: '拒绝', 
    			  											         	callback: function(){
	    			  											            var url = window.onlinefilePath+'/rest/onlinefileuser/noAgreenInSideCompany?callback=?';
																			var data = {'userId':window.userId,'companyId':companyid,'username':window.userName};
																			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
																			jQuery.getJSON(url, ret.data,
	    			  											        		function(json) {
	    			  											        		 //正文
	    			  											        		 if(json.isOk){
	    			  											        			 reloadCompany();
	    			  											        			 showBottomMsg('您已拒绝加入'+companyName+'团队！', 1);
	    			  											        		 }else{
	    			  											        			 showBottomMsg('拒绝加入'+companyName+'团队失败！', 2);
	    			  											        		 }
	    			  											        	});
	    			  											        	this.close();
    			  											         	}}
    			  											         ]
	    			  										}).show(); 
	    			  									}
	    			  								}) ;
	    			  								var url = window.location.href ;
	    			  				    			url = url.substring(0, url.indexOf("?")) ;
	    			  				    			window.location.href = url + "?companyid=" + window.companyid;
	    			  								/*showBottomMsg('新企业注册成功，在右上角的下拉菜单中可以切换到新的企业', 1);
	    			  								$btn.button('reset');
	    			  								closeContentPanel();*/
	    			  							}, 500);
	    			  						},
	    			  						cache:false
	    			  					});
	    			  					//showBottomMsg('新企业注册成功，在右上角的下拉菜单中可以切换到新的企业', 1);
	    			  					//20151015 xiayongcai 修改返回值
	    			  					showBottomMsg(rst.msg, 1);
		  								$btn.button('reset');
		  								closeContentPanel();
	    			  				} else {
	    			  					//20151015 xiayongcai 修改返回值
	    			  					if(rst.msg != null && rst.msg != undefined){
	    			  						showBottomMsg(rst.msg, 2);
	    			  					}else{
	    			  						showBottomMsg('新企业注册失败！', 2);
	    			  					}
	    			  					//showBottomMsg('新企业注册失败！', 2);
	        			  				$btn.button('reset');
	        			  			}
	    			  			}
	    			  		});
	    				} else {
	    					showBottomMsg("对不起，您的密码输入有误！",3);
	    					$btn.button('reset');
	    				}
					}
    			});
    		});
    		$("#crCancle").on("click", function(){
    			closeContentPanel();
    		});
    	}, 100);
    });
    
    
    /*liuwei 20150523*/
    //点击推荐好友
    $("#recommendFriends").on('click',function(){
    	if(window.globalUserStatus == 0){
			showBottomMsg("当前会话已经过期，请<a onclick='gotoIndexPage()'>重新登录</a>。", 3);
			return false ;
		}
    	//https://127.0.0.1/admin
    	var url = window.defaultusrl+"?"+window.usernamemd5code
	   	var html = template('recommendFriends_templete',{"url":url, 'fullname':window.fullName});
    	setTimeout(function(){
    		$.fywindow({
    			title:"推荐给好友",
    			width: 800,
    			height: 600,
    			content:html
    		});
    		var panelH = document.documentElement.clientHeight-115;
    		$("#scrollbardiv").height(panelH);
    		$("#scrollbardiv").perfectScrollbar();
    		copyUrlToFriend();
    		var username = encodeURIComponent(window.userName);
    		/**获取推荐信息**/
    		getCompanyReferrerInfo(username,"1");
    		
    	}, 0);
    });
    
    /**获取推荐信息**/
    function getCompanyReferrerInfo(username,page){
/*    	jQuery.getJSON(window.onlinefilePath+'/rest/companyregist/getCompanyReferrer?callback=?',{"username":username,"start":page, "limit":"10"},
			function(data) {
    			data.startno = (page*1-1)*10+1 ; 
				$("#recommendFriends_contentDivId").html(template('recommendFriends_content_templete',data));
				// 分页
				$("#companymanager #refferrer_pagingDiv").pagination({
					pages: data.totles,
					currentPage: page,
					onPageClick:function(page, event) {
						getCompanyReferrerInfo(username,page);
						return false;
					}
				});
    	});*/
    	//xiewenda
    	var param ={"username":username,"start":page, "limit":"10"};
        $.ajax({
            type: 'POST',
            url: $.appClient.generateUrl({ESCompanyRegist: 'getCompanyReferrer'},'x'),
            data: param,
            dataType : 'json',
            success: function(data) {
    			data.startno = (page*1-1)*10+1 ; 
				$("#recommendFriends_contentDivId").html(template('recommendFriends_content_templete',data));
				// 分页
				$("#companymanager #refferrer_pagingDiv").pagination({
					pages: data.totles,
					currentPage: page,
					onPageClick:function(page, event) {
						getCompanyReferrerInfo(username,page);
						return false;
					}
				})
				},
            cache: false
        });
    }
    
    //点击添加一行
    $("#main-container").on('click','#addone',function () {
    	var html = '<input type="text" class="form-control" placeholder="请输入邮箱地址" name="share_email"><a class="a_style">删除</a>';
    	$("#rowul").append('<ul class="form-inline" style="margin:0; padding:0;margin-top: 10px;margin-bottom: 10px;">'+html+'</ul>');
		//显示滚动条
		$("#scrollbardiv").perfectScrollbar('update');
    });
	
	//删除一行
	$('#main-container').on('click','.a_style',function(){
		$(this).parent().remove();
		//显示滚动条
		$("#scrollbardiv").perfectScrollbar('update');
		return false;
	});

	//点击发送邀请按钮
	$("#main-container").on('click','#sendMail',function(){
		var $array = $("input[name='share_email']");
		var $myreg = /^([a-zA-Z0-9]+[_|/_|/.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|/_|/.]?)*[a-zA-Z0-9]+/;
		var appendEmail='';
		/*for(var i = 0; i < $array.length; i++){
			$email = $array[i].value;
			if($email.trim()=='' && $email.trim().length==0){
	    		showBottomMsg("邮箱地址不能为空！", 2);
	    		return ;
	    	}else if(!$myreg.test($email)){
	    		showBottomMsg("输入的邮件格式不正确！", 2);
	    		return ;
	  	  	}
			appendEmail+=$email+"-";
		}*/
		
		for(var i = 0; i < $array.length; i++){
			$email = $array[i].value;
			if($email.trim()!='' && $email.trim().length > 0){
				if($myreg.test($email)){
					appendEmail+=$email+"-";
				}else {
		    		showBottomMsg("输入的邮箱格式不正确!", 2);
		    		return ;
		  	  	}
	    	}
		}
		if(appendEmail.length == 0){
			showBottomMsg("邮箱地址不能为空!", 2);
			return ;
		}
		$.ajax({
  			url:$.appClient.generateUrl({ESUserInfo : 'invitationCampany'},'x'),
  			type:'POST',
  			data:{'recommend_url':$("#recommend_url").val(), 'email':appendEmail, 'emailcontent':$("#textareaEmail").val(), 'subject':$("#subject").val()},
  			datatype:"json",
  			success:function(data){
  				$("input[name='share_email']").val("");
  				$(".form-inline:gt(0)").remove();
  				showBottomMsg('发送邀请成功，您可以继续发送邀请！', 1);
  			}
  		});
    });
	
    //点击发送邀请按钮
    /*$("#main-container").on('click','#sendMail',function(){
    	var $email = $.trim($("#inputEmail").val());
    	if($email=='' && $email.length==0){
    		showBottomMsg("邮箱地址不能为空！", 2);
    		return ;
    	}
    	var $myreg = /^([a-zA-Z0-9]+[_|/_|/.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|/_|/.]?)*[a-zA-Z0-9]+/;
    	if(!$myreg.test($email)){
    		showBottomMsg("输入的邮件格式不正确！", 2);
    		return false;
  	  	} else {
  	  		$.ajax({
  	  			url:$.appClient.generateUrl({ESUserInfo : 'invitationCampany'},'x'),
  	  			type:'POST',
  	  			data:{'recommend_url':$("#recommend_url").val(), 'email':$email, 'emailcontent':$("#textareaEmail").val(), 'subject':$("#subject").val()},
  	  			datatype:"json",
  	  			success:function(data){
  	  				$("#inputEmail").val("");
  	  				showBottomMsg('发送邀请成功，您可以继续发送邀请！', 1);
  	  			}
  	  		});
  	  	}
    });*/
  
   
 
    //copyUrlToFriend();
//    $('#main-container').on('click','#openUrlToFriend',function(){
//    	copyUrlToFriend();
//    });
    function copyUrlToFriend(){
		var swf = $('#openUrlToFriend').attr('swftext');
		var clip = new ZeroClipboard($("#openUrlToFriend"), {
			moviePath: swf
		});
		
		clip.on('load', function (client) {
			debugstr("Flash movie loaded and ready.");
		});
		
		clip.on('noFlash', function (client) {
			$(".demo-area").hide();
			debugstr("Your browser has no Flash.");
		});
		
		clip.on('wrongFlash', function (client, args) {
			$(".demo-area").hide();
			debugstr("Flash 10.0.0+ is required but you are running Flash " + args.flashVersion.replace(/,/g, "."));
		});
		clip.on('complete', function (client, args) {
			showBottomMsg("已复制到黏贴板","1");
			debugstr("Copied text to clipboard: " + args.text);
		
		});
    }
    
    if (userName != null) {
    	//20151122 xyc 
    	if(window.companyid !='-1'){
    		// 默认初始化数据左侧分类
    		documentCenter.initClassList(window.userId);
    	}
    	
//		$("#mySubscribe").click();
    	// 登陆到openfire服务器
    	remote.jsjac.chat.login(userName, "Fy_Documents_Of", false, window.companyid, window.fullName);
    	$.WebIM.reloadUserList(0);
    	$.WebIM.reloadGroupList(window.onlinefilePath, window.companyid, window.userName);
    }
    $('#receiveMessageDiv').perfectScrollbar();
	$(".loginoutbnt").on("click", function(){
		window.location.href = $(this).attr("url");
	});
	
	$('#user-context-menu').off('click','#group_remove_user').on('click','#group_remove_user',function(){
		var addgroupuserids = "";
		var itemid = $(this).attr("target-id");
		var obj =$('#'+itemid);
		var userid = obj.attr("itemuserid");
		var username = obj.attr("itemuser");
		var userfullname = obj.attr("itemuserfullname");
		var groupname = $('.main-left .active').attr("groupname");
		var flag = $('.main-left .active').attr('flag');
		var groupid = $('.main-left .active').attr('data-group-id');
		var deletegroupuserids=userid;
		var deletegroupuserusernames=username.replace('@', '\\40');
		var deletegroupuserfullnames=userfullname;	
		
		var url = window.onlinefilePath+'/rest/chat/resetGroup?callback=?';
		var data = {'companyId':window.companyid, 'username':window.userName, 'addgroupuserids':addgroupuserids, 'deletegroupuserids':userid,groupremark:"", 'groupname':encodeURI($.trim(groupname), "utf-8"), groupid:groupid, groupflag:flag, manageruserid:window.userId, changeusers:true, changeitems:false, "fullname":encodeURI( window.fullName, "utf-8")};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
		function(json) {
			if(json.isOk){
				var arg = 'type=reset_group&groups='+flag+'&deletegroupusernames='+deletegroupuserusernames;
				remote.jsjac.chat.ofuserservice(arg, false) ;
				if(deletegroupuserusernames.length > 0){
					var deleteArray = deletegroupuserusernames.split(",");
					for(var d=0;d<deleteArray.length;d++){
						var dcontent = flag+"broadcast--removeuser"+$("#current_user_name").html()+"将您从["+groupname+"]分类中请出！" ;
						remote.jsjac.chat.sendMessage(dcontent, deleteArray[d]+"@"+remote.jsjac.domain);
					}
				}
				var content = groupname+"broadcast-deletegroupuser"+flag+";"+deletegroupuserids+";"+deletegroupuserfullnames ;
				remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
				$.WebIM.reloadUserList(1);
				$.WebIM.reloadGroupUsers(flag) ;
			} else {
				showBottomMsg("添加分类成员失败",2);
			}
			
		});
	});
	
	$('#user-context-menu').off('click','#group_add_user').on('click','#group_add_user',function(){
		var deletegroupuserids = "";
		var itemid = $(this).attr("target-id");
		var obj =$('#'+itemid);
		var userid = obj.attr("itemuserid");
		var username = obj.attr("itemuser");
		var userfullname = obj.attr("itemuserfullname");
		var groupname = $('.main-left .active').attr("groupname");
		var flag = $('.main-left .active').attr('flag');
		var groupid = $('.main-left .active').attr('data-group-id');
		var addgroupuserids=userid;
		var addgroupusernames=username.replace('@', '\\40');
		var addgroupfullnames=userfullname ;
		var url = window.onlinefilePath+'/rest/chat/resetGroup?callback=?';
		var data = {'companyId':window.companyid, 'username':window.userName, 'addgroupuserids':addgroupuserids, 'deletegroupuserids':deletegroupuserids,groupremark:"", 'groupname':encodeURI($.trim(groupname), "utf-8"), groupid:groupid, groupflag:flag, manageruserid:window.userId, changeusers:true, changeitems:false, "fullname":encodeURI( window.fullName, "utf-8")};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
		  function(json) {
			if(json.isOk){
//				$('#ClassUserInfo').css("display",'none');
//				$('#groupMembers').trigger('click');
				var arg = 'type=reset_group&groups='+flag+'&addgroupusernames='+addgroupusernames;
				remote.jsjac.chat.ofuserservice(arg, false) ;
				var content = groupname+"broadcast-addgroup"+flag+";"+$("#current_user_name").html()+"邀请您加入分类【"+groupname+"】！"+addgroupuserids+";"+addgroupfullnames ;
				remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
				$.WebIM.reloadUserList(1);
				$.WebIM.reloadGroupUsers(flag);
			} else {
				showBottomMsg("添加分类成员失败",3);
			}
		});
	});
	$('#grouptype-context-menu').off('click','#edit_grouptype').on('click','#edit_grouptype',function(){
		var id = $(this).attr("target-id");
		var groupid = $(this).attr("groupid");
		var flag = $(this).attr("flag");
		//var li =$(".main-left").find("li[data-class-id='"+id+"']");
		var li =$("#publicClassSub").find("li[data-class-id='"+id+"']");
		var name =$(li).attr("groupname");
		var remark =$(li).attr("remark");
	   $("#editClassPanel").empty();
	   $("#editClassPanel").html(template('editGroupType_template',{"name":name,"remark":remark,"flag":flag,"classid":id,"groupid":groupid}));
	    var offset = $("#newClassId").offset();
		$('#editClassPanel').css("top",offset.top+"px");
		$('#editClassPanel').css("left",$("#newClassId").width()+"px");
		$('#editClassPanel').css("z-index","1001");
	    $("#editClassPanel").show();
	    
	    
	    /**20151020 xiayongcai 加入修改分类名称验证 */
	    $("#editClassPanel .createClassName").focus(function (){
			var groupname = $('#editClassPanel .createClassName').val();
			if($.trim(groupname).length < 30){
				$("#editClassPanel .createClassName").removeAttr("style");
			}
		}).keydown(function (){
			var groupname = $('#editClassPanel .createClassName').val();
			if($.trim(groupname).length >= 30){
				$("#editClassPanel .createClassName").css("border-color","red");
				$("#editClassPanel .createClassName").css("box-shadow","inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(247, 75, 121, 0.6)");
			}else{
				$("#editClassPanel .createClassName").removeAttr("style");
			}
		}).blur(function (){
			var groupname = $('#editClassPanel .createClassName').val();
			if($.trim(groupname).length >= 30){
				$("#editClassPanel .createClassName").css("border-color","red");
				$("#editClassPanel .createClassName").css("box-shadow","inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(247, 75, 121, 0.6)");
				showBottomMsg("分类名支持的最大长度小于30！",2);
			}else{
				$("#editClassPanel .createClassName").removeAttr("style");
			}
		});
	});

	$('#grouptype-context-menu').off('click','#transfer_grouptype').on('click','#transfer_grouptype',function(){
		var classId = $(this).attr("target-id");
		var groupid = $(this).attr("groupid");
		var groupname = $(this).attr("groupname");
		var baseurl = $("#flyingchat").attr("baseurl") ; 
		var companyId = window.companyid ;
		var username = window.userName ;
		var flag = "transfer";
		$(".alert-panel").hide();
		
		var offset = $("#newClassId").offset();
		//$('#transferClassPanel').css("top",offset.top+"px");
		$('#transferClassPanel').css("left",$("#newClassId").width()+"px");
		$('#transferClassPanel').css("z-index","1001");
		$.ajax({
			url:$.appClient.generateUrl({ESDefault : 'getCompanyUsersForGroupSet'},'x'),
			type:'POST',
			data:{userid:window.userId,groupid:groupid,'flag':flag},
			datatype:"json",
			success:function(data){
				var json = $.parseJSON(data);
				json["groupname"] = groupname;
				$("#transferClassPanel").empty();
			    $("#transferClassPanel").html(template("groupManeger_template",json));
			    $("#transferClassPanel").show();
			    
				if(json.inUsers.length==1){
					$('#ifNullShow').css('display','block');
				}
				
				$("#transferGroupAlert #xialatiao").perfectScrollbar();
				$("#xialatiao").perfectScrollbar();
				// 检索
				$('#transferGroupAlert').on('keyup','input',function(){
					var $items = $("#xialatiao li.transfer-user-item");
					if ($(this).val().length > 0) {
						$items.removeClass("active").filter(":contains('"+( $(this).val() )+"')").addClass("active");
					} else {
						$items.addClass("active");
					}
					$("#xialatiao").scrollTop(0);
					$("#xialatiao").perfectScrollbar('update');
				});
				//点击关闭时也关闭移交窗口
				$('#findtransferGroupAlertCloseId').on('click',function(){
					$("#transferClassPanel").empty();
					$("#transferClassPanel").hide();
				});
				//点击某个人进行移交
				$('#transferGroupAlert li').on('click',function(){
					//弹出窗口验证密码
					$('#transferGroupAlert').hide();
					$('#quiteChangeManeger').remove();
					var username = $(this).attr('username');
					var userid = $(this).attr('userid');
					var headimg = $(this).attr('headimg');
					
					$('#transferClassPanel').append(template("quiteChangeManeger_template",{"username":window.userName,"userid":window.userId,"headimg":window.PORTRAIT}));
					
					$('#quiteChangeManeger').on('click','#confirm_password',function(){
						var password = $('#checkpassword').val();
						//验证密码
						$.ajax({
							url:$.appClient.generateUrl({ESUserInfo : 'checkPasswordIsRight'},'x'),
							type:'POST',
							data:{userid:window.userId,password:password},
							datatype:"json",
							success:function(data){
								var json = $.parseJSON(data);
								if(json.isOk){
									//说明正确，执行移交
									$.ajax({
										url:$.appClient.generateUrl({ESDefault : 'changeGroupAdmin'},'x'),
										type:'POST',
										data:{companyId:window.companyid,username:window.userName,userid:window.userId,tousername:username,touserid:userid,groupid:groupid,classId:classId},
										datatype:"json",
										success:function(data){
											var json1 = $.parseJSON(data);
											if(json1.isOk){
												// 更改左侧分类的拥有者信息
												var objGroupLeft=$("#group-left li[data-class-id='"+classId+"']").attr("owner", userid);
												objGroupLeft.attr("owner", userid);
												objGroupLeft.attr("creator", userid);
												showBottomMsg("分类移交成功",1);
												$('#quiteChangeManeger').remove();	
												//20151125点击移交分类鍀时候获取用户是否在成员模块，如果是鍀情况下刷新成员页面
												if(undefined != $("#add_user_for_email").attr("title")){
													getMemberList();
												}
											} else {
												showBottomMsg("分类移交失败",3);
											}
										}
									});
								} else {
									showBottomMsg("对不起，您的密码输入有误！",3);
								}
							}
						});
					});
					//取消
					$('#quiteChangeManeger').on('click','#quxiao',function(){
						$('#quiteChangeManeger').remove();	
					});
				});
			}
		});
	
	});
	
	$('#grouptype-context-menu').off('click','#delete_grouptype').on('click','#delete_grouptype',function(){
		var classId = $(this).attr("target-id");
		$.dialog({
			title: '删除分类',
			content : '确定要删除分类吗?分类删除后无法恢复！',
			okValue : '确定',
			ok : true,
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				var li =$(".main-left").find("li[data-class-id='"+classId+"']");
				var groupid =$(li).attr("data-group-id");
				var flag =$(li).attr("flag");
				var groupname =$(li).attr("groupname");
				var url = $.appClient.generateUrl({ESDocumentClass : 'deleteFileClassById'}, 'x');
				$.post(url, {classId:classId,companyId:window.companyid,userId:window.userId}, function(res) {
					var success = $.parseJSON(res);
					if(success.success == 'true'){
						documentCenter.getClassList(window.userId);
						setTimeout(function(){
							//删除分类后 默认显示第一个分类  如果第一个分类不存在   就进入到个人空间
							//liuwei 增加判断当前用户是否是第一个分类下的，如果是就显示第一个分类的聊天窗口否则显示机器人窗口
							if($('#documentClassList li:first').length>0 && $('#documentClassList li:first').attr('ismember') =='true'){
								$('#documentClassList li:first').trigger('click');
							}else{
								$("#mySubscribe").click();
								$.WebIM.gotoUser('fyBot');
							}
						}, 200);
						//删除分组
						var url = window.onlinefilePath+'/rest/chat/deleteGroup?callback=?';
						var data = {'companyId':window.companyid,'userid':window.userId,'username':window.userName, 'groupflag':flag, "groupid":groupid};
						var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
						jQuery.getJSON(url, ret.data,
							function(json) {
							if(json.isOk){
								showBottomMsg("名称为【"+groupname+"】的分类已删除！", 1);
								var content = groupname+"broadcast-dropgroup"+flag ;
								remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
								setTimeout(function(){
									var arg = 'type=delete_group&groups='+flag;
									remote.jsjac.chat.ofuserservice(arg, true) ;
								}, 2000);
							} else {
								showBottomMsg("删除分类失败！", 3);
							}
						});
//						$('#flyingchatgrouplist').perfectScrollbar('update');
						return false;
					}else if(success.success == "havechildren"){
						showBottomMsg("该分类下存在文件或文件夹，不能直接删除！",2);
					}else if(success.success == "nocreator"){
						showBottomMsg("您无权删除此分类！",3);
					}else if(success.success == "haveuser"){
						showBottomMsg("该分类下存在成员，不能直接删除！",2);
					}
				});
			}
		}).showModal();
		
	});
	
	//移交公司
	$('#grouptype-context-menu').off('click','#transfer_company').on('click','#transfer_company',function(){
		var companyid = $(this).attr("companyid");
		var userid = $(this).attr("userid");
		var companyname = $(this).attr("companyname");
		var username = $(this).attr("username");
		$(".alert-panel").hide();
		var offset = $("#newClassId").offset();
		$('#transferClassPanel').css("top",offset.top+"px");
		$('#transferClassPanel').css("left",$("#newClassId").width()+"px");
		$.ajax({
			url:$.appClient.generateUrl({ESCompanyRegist : 'getCompanyUserForTransfer'},'x'),
			type:'POST',
			data:{'userid':userid,'companyid':companyid},
			datatype:"json",
			success:function(data){
				var json = $.parseJSON(data);
				json["companyname"] = companyname;
				$("#transferClassPanel").empty();
			    $("#transferClassPanel").html(template("companyManeger_template",json));
			    $("#transferClassPanel").show();
			    
				if(json.inUsers.length==0){
					$('#ifNullShow').css('display','block');
				}
				
				$("#transferGroupAlert #xialatiao").perfectScrollbar();
				$("#xialatiao").perfectScrollbar();
				// 检索
				$('#transferGroupAlert').on('keyup','input',function(){
					var $items = $("#xialatiao li.transfer-user-item");
					if ($(this).val().length > 0) {
						$items.removeClass("active").filter(":contains('"+( $(this).val() )+"')").addClass("active");
					} else {
						$items.addClass("active");
					}
					$("#xialatiao").scrollTop(0);
					$("#xialatiao").perfectScrollbar('update');
				});
				//点击关闭时也关闭移交窗口
				$('#findtransferGroupAlertCloseId').on('click',function(){
					$("#transferClassPanel").empty();
					$("#transferClassPanel").hide();
				});
				//点击某个人进行移交
				$('#transferGroupAlert li').on('click',function(){
					//弹出窗口验证密码
					$('#transferGroupAlert').hide();
					$('#quiteChangeManeger').remove();
					var tousername = $(this).attr('username');
					var touserid = $(this).attr('userid');
					var headimg = $(this).attr('headimg');
					
					$('#transferClassPanel').append(template("quiteChangeManeger_template",{"username":window.userName,"userid":window.userId,"headimg":window.PORTRAIT}));
					
					$('#quiteChangeManeger').on('click','#confirm_password',function(){
						var password = $('#checkpassword').val();
						//验证密码
						$.ajax({
							url:$.appClient.generateUrl({ESUserInfo : 'checkPasswordIsRight'},'x'),
							type:'POST',
							data:{userid:userid,password:password},
							datatype:"json",
							success:function(data){
								var json = $.parseJSON(data);
								if(json.isOk){
									//说明正确，执行移交
									$.ajax({
										url:$.appClient.generateUrl({ ESCompanyRegist: 'transferCompany'},'x'),
										type:'POST',
										data:{userid:userid,username:username,companyid:companyid,companyname:companyname,tousername:tousername,touserid:touserid},
										datatype:"json",
										success:function(data){
											var json1 = $.parseJSON(data);
											if(json1.isOk){
												// 更改左侧分类的拥有者信息
												window.isAdmin = "0";
												$('#quiteChangeManeger').remove();
												$("#publicClass").attr("isadmin","0");
												$("#campanyinfo").prev().remove();
												$("#campanyinfo").remove();
												$("#usersmanager").remove();
												//重新加载公司
												reloadCompany();
												showBottomMsg("公司移交成功",1);
												//发送给移交人消息
												var postMsg = "我将公司【"+companyname+"】移交给了你，你成为了此公司管理员，重新登录系统后生效！"
												var name = tousername.replace('@', '\\40');
												remote.jsjac.chat.sendMessage(postMsg,name+"@"+remote.jsjac.domain);
												$.WebIM.saveHistorySimpleMsg(username,tousername,postMsg);
											} else {
												showBottomMsg("分类移交失败",3);
											}
										}
									});
								} else {
									showBottomMsg("对不起，您的密码输入有误！",3);
								}
							}
						});
					});
					//取消
					$('#quiteChangeManeger').on('click','#quxiao',function(){
						$('#quiteChangeManeger').remove();	
					});
				});
			
			}
		});
	
	});
	
	$("#editClassPanel").on("click",".editClassBtn",function(){
//		var groupid = $('#documentClassList .active').attr('data-group-id');
//		var classId = $('#documentClassList .active').attr('data-class-id');
		var classId = $(this).attr("classid");
		var groupid = $(this).attr("groupid");
		var flag = $(this).attr('flag');
		var groupname= $.trim($('#editClassPanel [name="className"]').val());
		var oldName= $.trim($('#editClassPanel [name="className"]').attr("oldValue"));
		var mark= $.trim($('#editClassPanel [name="groupremark"]').val());
		var oldmark= $.trim($('#editClassPanel [name="groupremark"]').attr("oldValue"));
        var src= $("#"+classId).find("a img").attr("src");
		if(groupname == oldName && mark == oldmark){
			showBottomMsg("您未做任何修改!", 2);
			return false;
		}else if($.trim(groupname).length>=30){
			showBottomMsg("分类名支持的最大长度小于30!",2);
			return false;
		}else if(classNameTest.test(groupname) ==false){
			showBottomMsg('分类名不能包含\ / : * \\\\ ? \\" < > |等字符', 2);
			return false;
		}
		var url = $.appClient.generateUrl({ESDocumentClass:'editClassInfo'},'x');
		$.ajax({
			url:url,
			type:'POST',
			data:{classId:classId,groupName:groupname,companyId:window.companyid,userId:window.userId,groupid:groupid,flag:flag,mark:mark},
			success:function(data){
				var success = $.parseJSON(data)
				if(success.success == 'true'){
					$('#editClassPanel [name="className"]').attr("oldValue",groupname);
					$('#editClassPanel [name="groupremark"]').attr("oldValue",mark);
					/*
					$(".main-left li[data-class-id='"+classId+"']").attr("remark",mark);
					$(".main-left li[data-class-id='"+classId+"']").attr("groupname",groupname);
					$(".main-left li[data-class-id='"+classId+"']").attr("title",groupname);
					$(".main-left li[data-class-id='"+classId+"']").find("a").html('<img width="32" height="32" class="useritempic" src="'+src+'">  '+groupname);
					*/
					//liuwei 2016215修改修改分类名称修改其我的文档名称
					$("#publicClassSub li[data-class-id='"+classId+"']").attr("remark",mark);
					$("#publicClassSub li[data-class-id='"+classId+"']").attr("groupname",groupname);
					$("#publicClassSub li[data-class-id='"+classId+"']").attr("title",groupname);
					$("#publicClassSub li[data-class-id='"+classId+"']").find("a").html('<img width="32" height="32" class="useritempic" src="'+src+'">  '+groupname);
					
					
					showBottomMsg("修改成功",1);
					/*修改成功后,自动将当前panel隐藏 modify by jiechao*/
					$("#editClassPanel").hide();
					/**/
					$("#ClassNameFromEditClassName").text(groupname); 
		    		if(oldName==$("#flyingchat #receiverusername").text()){
		    			$('#receiverusername').css({"width":"auto"});
		    			$("#flyingchat #receiverusername").text(groupname); 
		    			$("#flyingchat #receiverusername").attr('title',groupname); 
		    			var tempwidth = $("#groupusersspanId").width() ;
						var maxWidth = ($("#mainContentRight").width()-160-tempwidth) ;
						if($('#receiverusername').width()>maxWidth){
							$('#receiverusername').css({"width":maxWidth+"px"});
						} else {
							$('#receiverusername').css({"width":($('#receiverusername').width()+5)+"px"});
						}
		    		}
			    	var $userspan = $("#flyingchatusers #userListspan-0");
			    	if($userspan.attr("text")=="组内成员["+oldName+"]"){
			    		$userspan.text("组内成员["+groupname+"]");
			    		$userspan.attr("text","组内成员["+groupname+"]");
			    	}
					
					$("#mainContentRight .receiveruserstatus").attr('title',mark); 
					$("#mainContentRight .receiveruserstatus").html(mark); 
					$('#editClassPanel [name="className"]').attr("oldValue",groupname);
				}else if(success.success == 'have'){
					showBottomMsg("分类名重复",2);
				}else{
					showBottomMsg("修改失败",2);
				}
			}
		});
		return false;
	});
	$("#editClassPanel").on("click",'.editClassCancleBtn',function(){
		$("#editClassPanel").hide();
		$("#editClassPanel .createClassName").removeAttr("style");
	});
	
	$("#grouptype-context-menu").on("click","#apply_for_group",function(){
		var id = $(this).attr("target-id");
		var li =$(".main-left").find("li[data-class-id='"+id+"']");
		var groupid =$(li).attr("data-group-id");
		var groupflag =$(li).attr("flag");
		applyGroup(groupid,groupflag);
	});
	
	$("#grouptype-context-menu").on("click","#out_for_group",function(){
		var id = $(this).attr("target-id");
		var li =$(".main-left").find("li[data-class-id='"+id+"']");
		var groupid =$(li).attr("data-group-id");
		var groupname =$(li).attr("groupname");
		var groupflag =$(li).attr("flag");
		var data_idseq =$(li).attr("data-idseq");
		var groupOwner =$(li).attr("owner");
		outGroup(groupid,groupname,groupflag,data_idseq,groupOwner);
	});
	
	$("#main-container").on("click","#userGrid .userInfoDiv",function(){
		var checkbox = $(this).parent().find("input[type='checkbox']");
		$(checkbox).prop("checked",!checkbox.is(":checked"));
	});
	
});


function addFoucs(divId) {
	var html = $("#"+divId).html();
	$("#"+divId).html("");
	var obj = $("#"+divId);
	var range, node;
	if (!obj.hasfocus) {
		obj.focus();
	}
	 //liuwei加入判断是否是IE
	 if($.browser.msie) { 
		 document.selection.createRange().pasteHTML(html);
	 }else{
			range = window.getSelection().getRangeAt(0);
			range.collapse(false);
			node = range.createContextualFragment(html);
			var c = node.lastChild;
			range.insertNode(node);
			if (c) {
				range.setStartAfter(c)
				range.setEndAfter(c);
			}
			var j = window.getSelection();
			j.removeAllRanges();
			j.addRange(range);
	}
}



/** 文件评论 start **/

/*
 * 处理过长的字符串，截取并添加省略号
 * 注：半角长度为1，全角长度为2
 * 
 * pStr:字符串
 * pLen:截取长度
 * 
 * return: 截取后的字符串
 */
function autoAddEllipsis(pStr, pLen) {

	var _ret = cutString(pStr, pLen);
	var _cutFlag = _ret.cutflag;
	var _cutStringn = _ret.cutstring;

	if ("1" == _cutFlag) {
		return _cutStringn + "...";
	} else {
		return _cutStringn;
	}
}

/*
 * 取得指定长度的字符串
 * 注：半角长度为1，全角长度为2
 * 
 * pStr:字符串
 * pLen:截取长度
 * 
 * return: 截取后的字符串
 */
function cutString(pStr, pLen) {

	// 原字符串长度
	var _strLen = pStr.length;

	var _tmpCode;

	var _cutString;

	// 默认情况下，返回的字符串是原字符串的一部分
	var _cutFlag = "1";

	var _lenCount = 0;

	var _ret = false;

	if (_strLen <= pLen/2) {
		_cutString = pStr;
		_ret = true;
	}

	if (!_ret) {
		for (var i = 0; i < _strLen ; i++ ) {
			if (isFull(pStr.charAt(i))) {
				_lenCount += 2;
			} else {
				_lenCount += 1;
			}

			if (_lenCount > pLen) {
				_cutString = pStr.substring(0, i);
				_ret = true;
				break;
			} else if (_lenCount == pLen) {
				_cutString = pStr.substring(0, i + 1);
				_ret = true;
				break;
			}
		}
	}
	
	if (!_ret) {
		_cutString = pStr;
		_ret = true;
	}

	if (_cutString.length == _strLen) {
		_cutFlag = "0";
	}

	return {"cutstring":_cutString, "cutflag":_cutFlag};
}

/*
 * 判断是否为全角
 * 
 * pChar:长度为1的字符串
 * return: true:全角
 * 			false:半角
 */
function isFull (pChar) {
	if ((pChar.charCodeAt(0) > 128)) {
		return true;
	} else {
		return false;
	}
}


function strlen(str){
    var len = 0;
    for (var i=0; i<str.length; i++) { 
     var c = str.charCodeAt(i); 
    //单字节加1 
     if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) { 
       len++; 
     } 
     else { 
      len+=2; 
     } 
    } 
    return len;
}

//点击头像@发送评论
function sendMessageNew(authorId, authorName, messageId,evt) {
	if ($("#messageValue").html().indexOf("authorId_" + authorId) <= 0) {
		var length = strlen("@" + authorName) * 8;
		$("#messageValue").html($("#messageValue").html() + "<input type='text' id='authorId_" + authorId + "' placeholder ='@" + authorName + "' readonly='readonly' style='background:transparent;border:0;height:21px;width:"+length+"px' attr='inputStart_{userId:"+authorId+",showName:"+authorName+"}_inputEnd'>");
//		$("#messageValue").html("<input type='text' id='authorId_" + authorId + "' value ='@" + authorName + "' readonly='readonly' style='background:transparent;border:0;height:21px;width:"+length+"px' attr='inputStart_{userId:"+authorId+",showName:"+authorName+"}_inputEnd'>");
		addFoucs("messageValue");
	}
	var e = (evt) ? evt : window.event; //判断浏览器的类型，在基于ie内核的浏览器中的使用cancelBubble;事件冒泡
	if (window.event) {
		e.cancelBubble = true;
	} else {
		e.stopPropagation();
	}
	$("#messageType").val("0");
	$("#messageId").val(messageId);
	$("#parentId").val(0);
}

//点击非头像@发送回复
function replyMessageNew(authorId, authorName, messageId, parentId) {
	
	if (authorId != "-1") {
		if ($("#messageValue").html().indexOf("authorId_" + authorId) <= 0) {
			var length = strlen("@" + authorName) * 8;
			$("#messageValue").html($("#messageValue").html() + "<input type='text' id='authorId_"	+ authorId + "' value='@" + authorName	+ "' readonly='readonly' style='background:transparent;border:0;height:21px;width:"	+length + "px' attr='inputStart_{userId:"+authorId+",showName:"+authorName+"}_inputEnd'>");
//			$("#messageValue").html("<input type='text' id='authorId_"	+ authorId + "' value='@" + authorName	+ "' readonly='readonly' style='background:transparent;border:0;height:21px;width:"	+length + "px' attr='inputStart_{userId:"+authorId+",showName:"+authorName+"}_inputEnd'>");
		}
	}
//	addFoucs("messageValue");
	$("#messageType").val("1");
	$("#messageId").val(parentId);
	$("#parentId").val(parentId);
}

//选择发送
$("#main-container").on("click","#choose-enter-send li",function(){
	var isEnterSend = $(this).attr("isEnterSend");
	url =  $.appClient.generateUrl({ESUserInfo : 'commentEnterSet'},'x');
	$.ajax({
		url:url,
		type: "POST",
		data:{'userid':window.userId,'isEnterSend':isEnterSend},
		dataType:"json",
		success:function(datas){
			if(datas.isSucess=="true"){
				 window.isEnterSend = isEnterSend;
				var html =  template('choose-enter-ok',{'type':isEnterSend});
				 $("#choose-enter-send").html(html);
			}
		}
	});
});

//评论输入框回车
$("#main-container").on("keydown","#messageValue",function(e){
	if(window.isEnterSend == 1){
		if(!e.ctrlKey && e.keyCode == 13){
			doComment();
			e.preventDefault ? e.preventDefault() : (e.returnValue = false);
		}
	}else if(window.isEnterSend == 0 ){
		if(e.ctrlKey && e.keyCode == 13){
			doComment();
			e.preventDefault ? e.preventDefault() : (e.returnValue = false);
		}
	}
});

/** 发送评论与回复  **/
function doComment() {
	var content = $("#messageValue").html();
	var contentUse = content.replace(/&nbsp;+/g, "");
	if (trim(contentUse).length <= 0) {
		showBottomMsg("请填写评论!", 2);
		return false;
	}
	var authorId = window.userId;
	var messageType = $("#messageType").val();
	var fileId = $('#content-list4FileInfo').attr("fileId");
	if(content.indexOf("@")<0){
		messageType = "0";
	}
	var messageId = $("#messageId").val();
	var parentId = $("#parentId").val();
	if(parentId == ""){
		parentId = $("#content-list4FileInfo").find(".fileinfo").attr("fileid");
	}
	/**  0代表文件评论,默认是文件评论  **/
	if (messageType == "0") {
		$.ajax({url : $.appClient.generateUrl({ESDocumentCenter:'onSendComment'}, 'x'),
			type : 'POST',
			data : {'content' : content,'userId' : authorId,'fileId' : fileId,'companyId':window.companyid,'authorName' : window.userName,'fullName':window.fullName},
			datatype : "json",
			success : function(data) {
				var rstJson = $.parseJSON(data);
				if (rstJson.commentLists.success) {
					/**  添加新评论成功 加载页面   **/
					showBottomMsg("评论成功!", 1);
					documentCenter.getFileCommentList(fileId,"","1","",true,"-1","1");
					addFoucs("messageValue");
					$("#messageType").val("0");
					$("#messageId").val("");
					$("#parentId").val("");
					$("#messageValue").html("");
					var toUsersMap = rstJson.toUsersLists;
					/** value []0代表userName  1代表userFullName  **/
					for(var key in toUsersMap){
						$.WebIM.sendReplyFileComment(fileId,toUsersMap[key].split(",")[0],rstJson.replyContent,key,window.userName,window.fullName,toUsersMap[key].split(",")[1],rstJson.commentLists.maxId);  
					} 
				} else {
					showBottomMsg("评论失败!", 3);
					return false;
				}
			}
		});
	} else {//回复
		$.ajax({url : $.appClient.generateUrl({ESDocumentCenter : 'onReplyComment'}, 'x'),
			type : 'POST',
			data : {'content' : content,'userId' : authorId,'fileId' : fileId,'messageId' : messageId,'parentId' : parentId,'authorName' : window.userName,'companyId':window.companyid,'fullName':window.fullName},
			datatype : "json",
			success : function(json) {
				var rstJson = eval('('+json+')');
				if (rstJson.success) {
					$("#messageType").val("0");
					$("#messageId").val("");
					$("#parentId").val("");
					$("#messageValue").html("");
					$("#comment_" + messageId).html(template('file_reply_single_content_template',rstJson));
					showBottomMsg("评论成功!", 1);
					/** 此处解析需要发送的人员  **/
					var toUsersMap = rstJson.toUsersLists;
					/** value []0代表userName  1代表userFullName  **/
					for(var key in toUsersMap){  
						$.WebIM.sendReplyFileComment(fileId,toUsersMap[key].split(",")[0],rstJson.replyContent,key,window.userName,window.fullName,toUsersMap[key].split(",")[1],parentId);  
					}
					
					//$.WebIM.sendReplyFileComment(fileId,rstJson.toUsersLists,rstJson.replyContent,rstJson.receiverId,window.userName,window.fullName,rstJson.userFullName,parentId);
					
					
					if($("#emdDiv_"+parentId).find("#open_"+parentId).html() == undefined ){
						var jsonContent = '{"commentId":"'+parentId+'"}';
						$("#dl_"+parentId).append(template('file_reply_fresh_content_template',jQuery.parseJSON(jsonContent)));
						changeComment(parentId);
					}else if($("#comment_"+parentId).css('display')=="none" || $("#comment_"+parentId).css('display')==undefined){
						changeComment(parentId);
					}
					
				} else {
					showBottomMsg("评论失败!", 3);
					return false;
				}
			}
		});
	}
}
/**  文件评论的回复  **/
$("#receiveMessageDiv").on('click', '.file-reply-comment', function() {
	var $shareFileItem = $(this).closest('div.file_share_item');
	var fileId = $shareFileItem.attr("fileId");
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileDelete?callback=?';
	var data = {"fileId":fileId, "companyId":g_companyId};
	var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
		function(data) {
				if(data.success==true || data.success == "true"){
					showBottomMsg("对不起，文件已被删除，无法查看！","3");
					return false;
				}else{
					var folderId = $shareFileItem.attr("folderId");
					var fileIdSeq = $shareFileItem.attr("idseq");
					var isupload = "0";
					if($("#uploadAllBtnDivId").css("top")!="0px"){
						 isupload = "1";
					}
					changeUploadBtn("0");
					var html = template('file_info_template', {"fileIdSeq": fileIdSeq, "myDocument": folderId=='0'?"myDocument":"","isupload":isupload,"isEnterSend":window.isEnterSend});
					$('#mainContentLeft').hide();
					$('#mainContentLeft4FileInfo').show();
					$('#mainContentLeft4FileInfo').html(html);
					documentCenter.showFileInfo(fileId, folderId);
					var authorId = $shareFileItem.attr("fileCommentUserId");
					var authorName = $shareFileItem.attr("fileCommentUserFullName");
					var parentId = $shareFileItem.attr("fileCommentParentId");
					replyMessageNew(authorId,authorName,parentId,parentId);
				/*	setTimeout(function(){
					},260);*/
					
				/*	if($("#messageValue").length == 0){
						
					}else{
						replyMessageNew(authorId,authorName,parentId,parentId);
					}*/
					return false;
				}
	});
	
});



/** 展开或显示评论信息 **/
function changeComment(messageid){
	if(($("#comment_"+messageid).html() == undefined)){
		
		$.ajax({url : $.appClient.generateUrl({ESDocumentCenter:'getMoreReplyComments'}, 'x'),
			type : 'POST',
			data : {'parentId' : messageid,'companyId':window.companyid},
			datatype : "json",
			success : function(json) {
				var rstJson = eval('('+json+')');
				rstJson["messageid"] = messageid;
				$("#open_"+messageid).after(template('file_reply_content_template',rstJson));
				changeComment_children(messageid);
			}
		});
		
	}else{
		changeComment_children(messageid);
	}

}
function changeComment_children(messageid){
	
	var status = $("#open_"+messageid).children("a").attr("data-id");
	if("open" == status){
		$("#comment_"+messageid).css('display','block'); 
		$("#open_"+messageid).children("a").attr("data-id","close");
		$("#open_"+messageid).children("a").children("strong").text("收起回复列表");
	}
	if("close" == status){
		$("#comment_"+messageid).css('display','none'); 
		$("#open_"+messageid).children("a").attr("data-id","open");
		$("#open_"+messageid).children("a").children("strong").text("展开更多回复");
	}
}

/**  初始化评论信息   **/
function initMessage() {
	var eventTitle = $("#projectName").val();
	var eventid = $("#projectId").val();
	// 设置头部
	var jsonHeading = '{"type":"' + $("#eventMessage").attr('id') + '","title":"' + eventTitle + '"}';
	$("#content-heading").html(template('event_panel_heading_template', jQuery.parseJSON(jsonHeading)));
	$("#messagePageIndex").val("1");
	$("#messagePageTotal").val("0");
	$("#messageType").val("0");
	$("#messageId").val("");
	$("#parentId").val("");
	$('#content-list').scrollTop(0);
	$('#content-list').perfectScrollbar('update');
	// 设置中间  查询获得评论信息数据
	documentCenter.getMessages(eventid, window.coreuserid);
	// 设置底部
	showMainContentLeftFooter(true);
	changeDisplay("EventMessage");
	$("#messageValue").html("");
}

/**  动态加载 **/
$("#main-container").on('scroll','#content-list4FileInfo',function () {
	
    var scrollTop = $(this).scrollTop();
    var scrollHeight = $(document).height();
    var windowHeight = $(this).height();
    if (scrollTop + windowHeight == scrollHeight) {
    	alert(1);
    }
});




/**   表情点击   **/
function changeEmotion(type){
	if(type == "message"){
		$("#emotionType").val("message");
	}else{
		$("#emotionType").val("talk");
	}
}

$("#main-container").on('keyup','#messageValue',function(){
	showEventMember();
});


/** 评论输入内容为@时弹出选择事件  **/
function showEventMember(){
	var val = $.trim($("#messageValue").html());
	if(undefined != val && val.length>0){
		var $obj = $('.main-left li.active');
		var groupid = $obj.attr('data-group-id');
		var value = val.substring(val.length-1,val.length);
		if(value == "@"){
			//var eventId = $('#projectId').val();
			var url = window.onlinefilePath+'/rest/onlinefile_filesws/getCommentMembersByUserId?callback=?';
	        var data = {'userId':window.userId,'companyId':window.companyid,'userName':window.userName,'groupId':groupid};
	        var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
	        jQuery.getJSON(url, ret.data,
					function(json) {
				var commentMembersHtml = $("#myCommentUsers_content").html(template('myCommentUsers_templete',json));
				var da = $.dialog({
			    	id:"myCommentUsers_content",
			    	title:'选择@人员',
					top:300,
					left: 300,
					width:500,
					height:500,
					modal:true,
				    content:commentMembersHtml
			    });
				/**
				 * 已经关注人员勾选事件
				 * **/
				$("#myCommentUsers_content").off("click",".groupMemmbers_commentUsers").on('click','.groupMemmbers_commentUsers',function(){
					var ck = $(this).find("[name = 'commentUsersChkBox']:checkbox");
					if(ck.prop("checked")){
						ck.prop("checked",false);
					}else{
						ck.prop("checked", true);
					}
					return true;
				});
				
				$("#myCommentUsers_content").off("click",".groupMemmbers_commentUsers :checkbox").on('click','.groupMemmbers_commentUsers :checkbox',function(){
					var ck = $(this);
					if(ck.prop("checked")){
						ck.prop("checked",false);
					}else{
						ck.prop("checked", true);
					}
				});
				
				/**  全选事件 **/
				
				$("#myCommentUsers_content").off("click","#chooseAllCommentMembersBtnId").on('click','#chooseAllCommentMembersBtnId',function(){
					$(".groupMemmbers_commentUsers").each(function(){
						var ck = $(this).find("[name = 'commentUsersChkBox']:checkbox");
						ck.prop("checked", true);
					});
				});
				
				/**  反选事件 **/
				$("#myCommentUsers_content").off("click","#chooseUpCommentMembersBtnId").on('click','#chooseUpCommentMembersBtnId',function(){
					$(".groupMemmbers_commentUsers").each(function(){
						var ck = $(this).find("[name = 'commentUsersChkBox']:checkbox");
						if(ck.prop("checked")){
							ck.prop("checked",false);
						}else{
							ck.prop("checked", true);
						}
					});
				});
				/** 确定事件 **/
				$("#myCommentUsers_content").off("click","#checkCommentMembersBtnId").on('click','#checkCommentMembersBtnId',function(){
					var check = $("input:checkbox[name=commentUsersChkBox]:checked");
					if(check.length<=0){
						showBottomMsg("没有选择新的成员", 2);
						return false;
					}else{
						$("#messageValue").html($("#messageValue").html().substring(0,$("#messageValue").html().lastIndexOf("@")));
						check.each(function(i){
							var authorId = $(this).attr("userId");
							var authorName = $(this).attr("userFullName");
							var length = strlen("@" + authorName) * 8;
							if ($("#messageValue").html().indexOf("authorId_" + authorId) <= 0) {
								$("#messageValue").html($("#messageValue").html() + "<input type='text' id='authorId_" + authorId + "' value='@" + authorName + "' readonly='readonly' style='background:transparent;margin-top:-5px;border:0;height:20px;width:"+length+"px' attr='inputStart_{userId:"+authorId+",showName:"+authorName+"}_inputEnd'>");
								addFoucs("messageValue"); 
							}
						});
						da.close();
					}
					//20151113 xiayongcai 
					$("#messageType").val("0");
				});
				
				//绑定关闭事件 20151113 xiayongcai 
				//点击头像属于单条记录回复，而@目前来说是发送消息，
				//那么@选择出来的用户都将以消息形式发送,不记录到个人的回复列表中。
				$(".ui-dialog-close").on('click',function(){
					$("#messageType").val("0");
				});
				
				da.show();
				//点击回车键检索
				$("#myCommentUsers_content").on('keydown',"#query_myComment_members",function(e){
					if(e.keyCode==13){ 
						var $items = $("#myCommentMembersContainer .groupMemmbers_commentUsers")
						var val = $.trim($(this).val());
						if(val.length > 0){
							$("#closeQuery_members").show();
							$("#imgQuery_myComment_members").hide();
							$items.css("display","none").filter(":contains('"+(val)+"')").css("display","block");
						}else{
							$items.css("display","block");
							$("#closeQuery_members").hide();
							$("#imgQuery_myComment_members").show();
						}
					}
					/*if(event.keyCode == 13){ 
						queryMembers();
					}*/
				});
				//点击搜索按钮
				$("#myCommentUsers_content").on('click',"#imgQuery_myComment_members",function(){
					var $items = $("#myCommentMembersContainer .groupMemmbers_commentUsers")
					var val = $.trim($("#query_myComment_members").val());
					if(val.length > 0){
						$("#closeQuery_members").show();
						$("#imgQuery_myComment_members").hide();
						$items.css("display","none").filter(":contains('"+(val)+"')").css("display","block");
					}else{
						$items.css("display","block");
						$("#closeQuery_members").hide();
						$("#imgQuery_myComment_members").show();
					}
				});
				
				//点击取消按钮
				$("#myCommentUsers_content").on('click',"#closeQuery_members",function(){
					$("#query_myComment_members").val('');
					$("#closeQuery_members").hide();
					$("#imgQuery_myComment_members").show();
					var $items = $("#myCommentMembersContainer .groupMemmbers_commentUsers")
					var val = $.trim($(this).val());
					if(val.length > 0){
						$items.css("display","none").filter(":contains('"+(val)+"')").css("display","block");
					}else{
						var $obj = $('.main-left li.active');
						var groupid = $obj.attr('data-group-id');
						var url = window.onlinefilePath+'/rest/onlinefile_filesws/getCommentMembersByUserId?callback=?';
	                    var data = {'userId':window.userId,'companyId':window.companyid,'userName':window.userName,'groupId':groupid};
	                    var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
	                    jQuery.getJSON(url, ret.data,
						function(json) {
					    	$("#myCommentUsers_content").html(template('myCommentUsers_templete',json));
					    });
					}
				});
				
			});
			
		}
	}
}
/**查询成员方法**/
function queryMembers(){
	var keyWord = $.trim($("#query_myComment_members").val());
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/queryMembersByParams?callback=?';
	var data = {'userId':window.userId,'companyId':window.companyid,'userName':window.userName,'keyWord':keyWord};
	var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
	function(json) {
    	$("#myCommentUsers_content").html(template('myCommentUsers_templete',json));
    });
}

/** 文件评论 end  **/

function stickytester(msg){
	var _messageTotalShow = $("#messageTotalShow");
	var _messageTotalShowVal = $("#messageTotalShowVal");
	var _userMessagesUl = $("#userMessagesUl");
	
	var messageTotalShowVal = _messageTotalShowVal.val();
	if(messageTotalShowVal == ""){
		messageTotalShowVal = 0;
	}
	messageTotalShowVal = messageTotalShowVal * 1 + 1;
	_messageTotalShowVal.val(messageTotalShowVal);
	_messageTotalShow.html(messageTotalShowVal > 99 ? "99+" : messageTotalShowVal);
	_messageTotalShow.show();
	
	
	/** lujixiang 20151120  注释  --start**/
	/** 
	var messageCount = _messageTotalShow.html();
	if(messageCount == ""){
		messageCount = 1;
	}else if(messageCount!='99+'){
		messageCount = messageCount*1 +1;
		messageCount = (messageCount > 99 ? '99+' : messageCount) ;
	}
	_messageTotalShowVal.val(_messageTotalShowVal.val() * 1 + 1);
	_messageTotalShow.css("display","").html(messageCount);
	**/
	/** lujixiang 20151120  注释  --end**/
	
	_userMessagesUl.append("<li>"+msg+"</li>");
	_userMessagesUl.perfectScrollbar('update');
	/**   暂时注释掉消息插件  **/
	//$.sticky(msg);
}

/**   如果新消息没有那么默认的不下拉 **/
$("#userMessagesId").click(function(){
	
	/** lujixiang 20151120  注释 --start **/
	/**
	var messageCount = $("#messageTotalShow").html();
	if(messageCount == "" || messageCount == "0"){
		showBottomMsg('您暂时还没收到新消息!',2);
		return false;
	}
	**/
	/** lujixiang 20151120  注释 --end **/
	
	var messageTotalShowVal = $("#messageTotalShowVal").val();
	if(messageTotalShowVal == "" || messageTotalShowVal == "0"){
		showBottomMsg('您暂时还没收到新消息!',2);
		return false;
	}
});

/**  点击新消息跳转到当前聊天对象  **/
$('#userMessagesUl').on('click','.msgGotoUserUrl',function(){
	talkToMessage('msgGotoUserUrl',$(this).attr('userName'));
});

/**  跳转到分组  **/
$('#userMessagesUl').on('click','.msgGotoGroupUrl',function(){
	talkToMessage('msgGotoGroupUrl',$(this).attr('groupflag'));
});

/**  跳转到分类  **/
$('#userMessagesUl').on('click','.msgGotoClassUrl',function(){
	talkToMessage('msgGotoClassUrl',$(this).attr('groupflag'));
});

/**  上下线消息  只提示 点击没有动作 wagnwenshuo 20160127  **/
$('#userMessagesUl').on('click','.msgOnOffLine',function(){
	talkToMessage('msgOnOffLine',$(this).attr('userName'));
});

/**  wangwenshuo 20160119 申请加入分类消息 点击后从新消息中移除**/
$('#userMessagesUl').on('click','#agreeThisApply,#passThisApply',function(){
	$(this).closest("li").remove();
	updateMsgTotalShowVal(-1);
});


function talkToMessage(messageType,messageValue){

	if(messageType == 'msgGotoUserUrl'){
		$.WebIM.gotoUser(messageValue);
	}else if(messageType == 'msgGotoGroupUrl'){
		$.WebIM.gotoGroup(messageValue);
	}else if(messageType == 'msgGotoClassUrl'){
		$.WebIM.gotoGroupForClass(messageValue);
	}
	/**  循环便利出来当前的数据，然后将其剔除掉  **/
	removeObjectByClassName(messageType,messageValue);
	$("#userMessagesUl").perfectScrollbar('update');
}

/**   按照class移出新消息里面内容 **/
function removeObjectByClassName(className,conditionValue){
	var counter = 0;
	$("."+className).each(function(index){
		if($(this).attr('userName')!= undefined){
			if($(this).attr('userName') == conditionValue){
				$(this).parent().remove();
				counter++;
			}
		}else{
			if($(this).attr('groupflag') == conditionValue){
				$(this).parent().remove();
				counter++;
			}
		}
	});

	/** lujixiang 20151120  注释,当条数超过99条更新错误 --start  **/
	/**
	var _messageTotalShow = $("#messageTotalShow").val();
	
	var messageCount = $("#messageTotalShow").html();
	if(messageCount != "" || messageCount != "0"){
		if(messageCount == 1){
			$("#messageTotalShow").hide();
			messageCount = "";
		}else{
			if(messageCount == counter){
				messageCount = "";
				$("#messageTotalShow").hide();
			}else{
				messageCount = messageCount*1 - counter;
			}
		}
	}
	$("#messageTotalShow").html(messageCount);
	**/
	updateMsgTotalShowVal(counter*(-1));
}

/**
 * wangwenshuo 重置未读消息数量 抽出方法公用
 * @param {} counter 减少消息数使用负数
 */
function updateMsgTotalShowVal(counter){
	/** lujixiang 20151120  注释,当条数超过99条更新错误 --end  **/
	var _messageTotalShowVal = $("#messageTotalShowVal");
	var _messageTotalShow = $("#messageTotalShow");
	var messageTotalShowVal = _messageTotalShowVal.val();
	if(messageTotalShowVal == ""){
		messageTotalShowVal = 0;
	}
	messageTotalShowVal = messageTotalShowVal * 1 + counter ;
	
	if(messageTotalShowVal <= 0){
		messageTotalShowVal = 0 ;
		_messageTotalShow.html(messageTotalShowVal);
		_messageTotalShow.hide();
	}else{
		_messageTotalShow.html(messageTotalShowVal > 99 ? '99+' : messageTotalShowVal);
		_messageTotalShow.show();
	}
	_messageTotalShowVal.val(messageTotalShowVal);
}

function showMsg(msg,type){
//	 stickytester(msg);
	if (type == null) {
		type = "1";
	}
	 showBottomMsg(msg,type);
}

function showBottomMsg(msg,errorlevel){
	/** 
	var timestamp=new Date().getTime();
	var jsonHeading = '{"msg":"'+msg+'"';
	if("1"==errorlevel){
		jsonHeading = jsonHeading + ',"level":"alert-success"}';
	}else if("2"==errorlevel){
		jsonHeading = jsonHeading + ',"level":"alert-warning"}';
	}else if("3" == errorlevel){
		jsonHeading = jsonHeading + ',"level":"alert-danger"}';
	}else{
		jsonHeading = jsonHeading + ',"level":"alert-info"}';
		
	}
	$("#messageDivId").alert("close");
	$("#messageMainDivId").css("height","50px");
	$("#messageMainDivId").attr("timestamp",timestamp);
	$("#messageMainDivId").html("");
	$("#messageMainDivId").html(template('message_template', jQuery.parseJSON(jsonHeading)));
	$("#messageMainDivId").css("left",$(window).width()/2-$("#messageDivId").width()/2);
	//setTimeout("hideMsg('"+timestamp+"')", 3000);
	**/
	/**  从新修改了源代码，使用新版提示框   **/
	var d = dialog({
	    content: msg,
	    layout:'top',
	    level:errorlevel
	});
	d.show();
	setTimeout(function () {
	    d.close().remove();
	}, 2000); 
}

function showMessage(msg,errorlevel){
	var d = dialog({
		content:msg,
		layout:'top',
		level:errorlevel,
		button:[
			{
            	value: '关闭'
        	}
		]
	}).show();
	
}




window.hideMsg=function(timestamp){
	if($("#messageMainDivId").attr("timestamp")==(timestamp+"")){
		$("#messageDivId").alert("close");
		$("#messageMainDivId").css("height","0px");
	}
}
var isSubSearchScribeTag = 0,isSubScribeTag = 1;
$(function(){
	$("#action-content-panel-upload").width("600").height(window.innerHeight - 58).css({top:53});
	
	$("#newClassId").on("click",function(){
		if (!$("#newClassPanel").is(":hidden")) {
			$("#newClassPanel").hide();
		} else {
			var offset = $(this).offset();
			$('#newClassPanel').css("top",offset.top+"px");
			$('#newClassPanel').css("left",$(this).width()+"px");
			$('#newClassPanel').css("z-index",'1000');
			$("#newClassPanel").show();
			/** 20151020 xiayongcai 分类创建名称验证提示*/
			$("#newClassPanel .createClassName").focus(function (){
				var groupname = $('.createClassName').val();
				if($.trim(groupname).length < 30){
					$("#newClassPanel .createClassName").removeAttr("style");
				}
			}).keydown(function (){
				var groupname = $('.createClassName').val();
				if($.trim(groupname).length >= 30){
					$("#newClassPanel .createClassName").css("border-color","red");
					$("#newClassPanel .createClassName").css("box-shadow","inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(247, 75, 121, 0.6)");
				}else{
					$("#newClassPanel .createClassName").removeAttr("style");
				}
			}).blur(function (){
				var groupname = $('.createClassName').val();
				if($.trim(groupname).length >= 30){
					$("#newClassPanel .createClassName").css("border-color","red");
					$("#newClassPanel .createClassName").css("box-shadow","inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(247, 75, 121, 0.6)");
					showBottomMsg("分类名支持的最大长度小于30！",2);
				}else{
					$("#newClassPanel .createClassName").removeAttr("style");
				}
			});
		}
	});

	$(document).on("click",function(e){ 	
		var target = $(e.target);
		
		/**  创建分类的点击事件 **/
		if(target.closest("#newClassId").length == 0 && target.closest("#newClassPanel").length == 0 ){ 
			$("#newClassPanel").hide(); 
		}
		if(target.closest("#editClassPanel").length == 0 && target.closest("#editClassPanel").length == 0 ){ 
			$("#editClassPanel").hide(); 
		}
		if(target.closest("#transferClassPanel").length == 0 && target.closest("#transferClassPanel").length == 0 ){ 
			$("#transferClassPanel").empty();
			$("#transferClassPanel").hide(); 
		}
		
		if(target.closest("#flyingchatSearchInputGroup").length == 0 && target.closest("#showflyingchatSearch").length == 0 ){ 
			$("#flyingchatSearchInputGroup").addClass("hidden"); 
		}
		
		/***  右侧滑动窗体的点击事件    ***/
		/***  wangwenshuo 20151020 复制到  添加copy-action-content-panel判断    ***/
		if(target.closest("#main-container div.file-item").length == 0  
		   && target.closest("#action-content-panel").length == 0  
		   && target.closest("ul.explorer-nav").length == 0  
		   && target.closest(".aui_outer").length==0 
		   && target.closest("button").length==0 
		   && target.closest($("#takeNextDiv").next()).length==0  
		   && target.closest($("#takeNextDiv").next().next()).length==0  
		   && target.closest("#deleteicon1").length==0 
		   && target.closest("#copy-action-content-panel").length==0 
		   && target.closest(".cke_dialog_body").length==0
		   && target.closest(".cke_dialog_background_cover").length==0){
			if (target.closest('.ui-dialog').length==0 
				&& !target.hasClass('ui-popup-backdrop') 
				&& target.attr("id") != "deleteRow") {
				closeContentPanel();
				$("#searchFileInputId").css("background-color","white");
			}
		}
		if(!$("#searchSelectDivId").hasClass("searchSelectMainDivHide")){//关闭检索框下拉
					$("#searchSelectDivId").addClass("searchSelectMainDivHide");
		}
		/***  wangwenshuo 20151020 复制到  添加判断    ***/
		if(target.closest("#copy-action-content-panel").length == 0 && target.closest(".file-copy").length == 0){ 
			closeCopyContentPanel();
		}
		
		
		/***  右侧滑动窗体的点击事件    ***/
		if(target.closest(".myEmailGroupCls li").length == 0 && target.closest("#action-emailattachment-content-panel").length == 0 ){
			closeEmailAttachMentContentPanel();
		}
		
		/**  邮箱设置的点击事件 由于拖拽会影响到其他窗体自动关闭，所以先注释掉该处
		if(target.closest("#emailSetting").length == 0 && target.closest("#email-setting-content-panel").length == 0 && target.closest("#email-create-content-panel").length == 0 && target.closest(".ui-dialog").length == 0 && target.closest("#messageMainDivId").length == 0 ){ 
			
			closeCreateEmailPanel();  //先关闭创建窗口
			closeEmailSettingPanel();
			$("#email_panel_maskLayer").hide();
		}**/
		
		/**
		 * 文件菜单
		 */
		if(target.closest(".menu").length == 0 && target.closest(".file-share").length==0){ 
			$("#menu_panel").remove();
		}
	}); 
  $(document).on('mousedown',function(e){
		if(3 == e.which){ // 鼠标右键点击
			var target = $(e.target);
			//** 文件菜单 *//
			if(target.closest("#menu_panel").length == 0){ 
				$("#menu_panel").remove(); 
			}
		}
	});
	
	/**       点击左侧文档分类       **/
	$("div.main-left").on("click", "li", function(){
		if($(this).attr("id")=='newClassId'){ // 20150812 .main-left下的新建分类点击时使其返回
			return false;
		}
		$(".main-left li").removeClass("active");
		$(this).addClass("active");
		if($(this).attr("lievent")=='false'){ // wangwenshuo 20151104 按钮功能不响应
			return;
		}
		closeFileInfo();
		var folderId = $(this).attr("data-class-id");
		var folderName = $(this).attr("groupname");
		var idSeq = $(this).attr("data-idseq");
		var isStar = $(this).attr("isStar");
		var json='{"classId":"'+folderId+'","titleText":"'+$(this).attr("groupname")+'"}';
		createfilejiapower("0");
		$('#bodyContent_Title').html(template('documentClass_menu_templete',jQuery.parseJSON(json)));
		
		var viewType = $.cookie('file-view-type_'+window.userId); 
		if (!viewType) {
			viewType = 'icon';
		}
		var sortType = $("#file-sort-type").val();
		var file_view_filter_btn_html = template("file_view_filter_btn_template", {"viewType":viewType,"sortType":sortType});
		// 设置头部
		var jsonHeading = '{"type":"documentAll"}';
		var json = jQuery.parseJSON(jsonHeading);
		json["file_view_filter_btn_html"] = file_view_filter_btn_html;
		$("#content-heading").html(template('file_panel_heading_template', json));
		$("#contentBody").html("");
		changeUploadBtn("0");
		showMainContentLeftFooter(true);
		// 查询文件列表
		$("#fileOrderFieldId").val("");
		$("#fileOrderTypeId").val("");
		documentCenter.getFirstFileList($(this), $(this).attr("data-class-id"));
		
		star(isStar);
		$("#file-breadcrumbs").jFolderCrumb({data:[{"id":folderId, "name":folderName, "idSeq":idSeq}],clickFunName:"forwardFolder"});
		$("#file-breadcrumbs").hide();
		$('#receiveMessageDiv').perfectScrollbar('update');
		$('#groupUsersList' ).css('display','block');
		
		
		/**  点击左侧分类的时候，如果右上角的新消息有分类消息那么直接remove掉  **/
		removeObjectByClassName('msgGotoClassUrl',$(this).attr("flag"));
		
	});
	
	/**  title标题栏中Tab切换事件  **/
	$("#bodyContent_Title").on("click","ul li",function(){
		$("#bodyContent_Title ul li").removeClass("active");
		$(this).addClass("active");
	});
	
	
	/**  点击文档  **/
	$("#bodyContent_Title").on("click","#documentAll",function(){
		closeFileInfo();
		var viewType = $.cookie('file-view-type_'+window.userId); 
		if (!viewType) {
			viewType = 'icon';
		}
		var sortType = $("#file-sort-type").val();
		var file_view_filter_btn_html = template("file_view_filter_btn_template", {"viewType":viewType,"sortType":sortType});
		// 设置头部
		var jsonHeading = '{"type":"'+$(this).attr('id')+'"}';
		var json = jQuery.parseJSON(jsonHeading);
		json["file_view_filter_btn_html"] = file_view_filter_btn_html;
		$("#content-heading").html(template('file_panel_heading_template', json));
		$("#contentBody").html("");
		changeUploadBtn("1");
		showMainContentLeftFooter(true);
		var $obj = $(".main-left li.active");
		var classId = $obj.attr("data-class-id");
		documentCenter.getFileList(classId);
		var title = $obj.text();
		initFolderCrumbs();
	});
	
	/**
	 * 点击我的文档
	 */
	$("#privateClassSub").on("click","#myDocument",function(){
		$("#searchValHidId").attr("searchtype","myDocument");
		$(".main-left li").removeClass("active");
		$(this).addClass("active");
		$('#bodyContent_Title').html(template('userCenter_menu_templete'));
		closeFileInfo();
		var viewType = $.cookie('myfile-view-type_'+window.userId); 
		if (!viewType) {
			viewType = 'icon';
		}
		var sortType = $("#file-sort-type").val();
		var file_view_filter_btn_html = template("file_view_filter_btn_template", {"viewType":viewType,"sortType":sortType,"documentType":"myDocument"});
		// 设置头部
		var jsonHeading = '{"type":"'+$(this).attr('id')+'"}';
		var json = jQuery.parseJSON(jsonHeading);
		json["file_view_filter_btn_html"] = file_view_filter_btn_html;
		$("#content-heading").html(template('file_panel_heading_template', json));
		$("#contentBody").html("");
		showMainContentLeftFooter(true);
		changeUploadBtn("1");
		$("#fileOrderFieldId").val("");
		$("#fileOrderTypeId").val("");
		/*documentCenter.getMyFileList();*/
		
		documentCenter.getFirstFileList($(this), $(this).attr("data-class-id"));
		$("#file-breadcrumbs").jFolderCrumb({data:[{"id":"2", "name":'我的文档', "idSeq":"1.2."}],clickFunName:"forwardFolder"});
//		$("#file-breadcrumbs").hide();
	});
	  
	 
	/** 我的关注 Start **/
	$("#privateClassSub").on("click","#mySubscribe",function(){
		$('#bodyContent_Title').html(template('userCenter_menu_templete'));
		closeFileInfo();
		changeUploadBtn("0");
		isSubScribeTag= 1;
		// 设置头部
		var jsonHeading = '{"type":"'+$(this).attr('id')+'"}';
		$("#content-heading").html(template('file_panel_heading_template', jQuery.parseJSON(jsonHeading)));
		$("#contentBody").html("");
		$("#content-list").perfectScrollbar();
		getSubScribeMsgsFun(1);
		showMainContentLeftFooter(true);
	});
	
//getSubScribeMsgsFun	
	
	/**  已经订阅成员检索  **/
	$("#main-container").on("click","#imgQuery_subScribersSearchBtn",function(){
		var searchKeyWord  = $("#subScribersSearchInputId").val();
		if(searchKeyWord.trim() != ""){
			$("#mySubScribesContainer .userNameInGroup").parent().css("display","none");
			$("#mySubScribesContainer .userNameInGroup").filter(":contains('"+searchKeyWord+"')").parent().css("display","block");
			$("#closeQuery_subScribersSearchBtn").css('display','block');
			$("#imgQuery_subScribersSearchBtn").css('display','none');
		}else{
			$("#mySubScribesContainer .userNameInGroup").parent().css("display","block");
			$("#closeQuery_subScribersSearchBtn").css('display','none');
			$("#imgQuery_subScribersSearchBtn").css('display','block');
		}
		
		return true;
	});
	
	$("#main-container").on("click","#closeQuery_subScribersSearchBtn",function(){
	   $("#subScribersSearchInputId").val("");
	   $(this).css('display','none');
	   $("#imgQuery_subScribersSearchBtn").css('display','block');
	   $("#mySubScribesContainer .userNameInGroup").parent().css("display","block");
	});
	
	$("#main-container").on("keyup","#subScribersSearchInputId",function(e){
		var searchKeyWord  = $("#subScribersSearchInputId").val();
		//$("#mySubScribesContainer .userNameInGroup").parent().css("display","none");
		if(searchKeyWord.trim()==""){
			$("#mySubScribesContainer .userNameInGroup").parent().css("display","block");
		}else{
			//$("#mySubScribesContainer .userNameInGroup").filter(":contains('"+searchKeyWord+"')").parent().css("display","block");
		}
		$("#closeQuery_subScribersSearchBtn").css('display','none');
		$("#imgQuery_subScribersSearchBtn").css('display','block');
		if(e.keyCode == 13){
			if(searchKeyWord.trim() != ""){
				$("#mySubScribesContainer .userNameInGroup").parent().css("display","none");
				$("#mySubScribesContainer .userNameInGroup").filter(":contains('"+searchKeyWord+"')").parent().css("display","block");
				$("#closeQuery_subScribersSearchBtn").css('display','block');
				$("#imgQuery_subScribersSearchBtn").css('display','none');
			}else{
				$("#mySubScribesContainer .userNameInGroup").parent().css("display","block");
				$("#closeQuery_subScribersSearchBtn").css('display','none');
				$("#imgQuery_subScribersSearchBtn").css('display','block');
			}
			return true;
		}
	});
	
	/**  未订阅成员检索  **/
	$("#main-container").on("click","#imgQuery_subScribersAvaliableSearchBtn",function(){
		var searchKeyWord  = $("#subScribersAvaliableSearchInput").val();
		if(searchKeyWord.trim() != ""){
			$("#mySubScribesAvaliableContainer .userNameInGroup").parent().css("display","none");
			$("#mySubScribesAvaliableContainer .userNameInGroup").filter(":contains('"+searchKeyWord+"')").parent().css("display","block");
			$("#closeQuery_subScribersAvaliableSearchBtn").css('display','block');
			$("#imgQuery_subScribersAvaliableSearchBtn").css('display','none');
		}else{
			$("#mySubScribesAvaliableContainer .userNameInGroup").parent().css("display","block");
			$("#closeQuery_subScribersAvaliableSearchBtn").css('display','none');
			$("#imgQuery_subScribersAvaliableSearchBtn").css('display','block');
		}
		
		return true;
	});
	
	$("#main-container").on("click","#closeQuery_subScribersAvaliableSearchBtn",function(){
		   $("#subScribersAvaliableSearchInput").val("");
		   $(this).css('display','none');
		   $("#imgQuery_subScribersAvaliableSearchBtn").css('display','block');
		   $("#mySubScribesAvaliableContainer .userNameInGroup").parent().css("display","block");
		});
	/**  订阅查询回车事件  **/
	$("#main-container").on("keyup","#subScribersAvaliableSearchInput",function(e){
		var searchKeyWord  = $("#subScribersAvaliableSearchInput").val();
		//$("#mySubScribesAvaliableContainer .userNameInGroup").parent().css("display","none");
		if(searchKeyWord.trim()==""){
			$("#mySubScribesAvaliableContainer .userNameInGroup").parent().css("display","block");
		}else{
			//$("#mySubScribesAvaliableContainer .userNameInGroup").filter(":contains('"+searchKeyWord+"')").parent().css("display","block");
		}
		$("#closeQuery_subScribersAvaliableSearchBtn").css('display','none');
		$("#imgQuery_subScribersAvaliableSearchBtn").css('display','block');
		if(e.keyCode == 13){
			if(searchKeyWord.trim() != ""){
				$("#mySubScribesAvaliableContainer .userNameInGroup").parent().css("display","none");
				$("#mySubScribesAvaliableContainer .userNameInGroup").filter(":contains('"+searchKeyWord+"')").parent().css("display","block");
				$("#closeQuery_subScribersAvaliableSearchBtn").css('display','block');
				$("#imgQuery_subScribersAvaliableSearchBtn").css('display','none');
			}else{
				$("#mySubScribesAvaliableContainer .userNameInGroup").parent().css("display","block");
				$("#closeQuery_subScribersAvaliableSearchBtn").css('display','none');
				$("#imgQuery_subScribersAvaliableSearchBtn").css('display','block');
			}
			return true;
		}
	});
	
	$("#main-container").on("click","div.mySubScribe > a",function(){
		
		$("#contentBody").find("div.mySubScribe > a").removeClass("active");
		var fileId = $(this).attr("value");
		var folderId = $(this).attr("folderId");
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileDelete?callback=?';
		var data = {'companyId':window.companyid,"fileId":fileId};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			if(json.success==true || json.success == "true"){
				showBottomMsg("对不起，文件已被删除，无法查看！","3");
				return false;
			}else{
				var html = template('file_info_template', {"fileIdSeq": "", "myDocument": "mySubScribe","isupload":0});
				$('#mainContentLeft').hide();
				$('#mainContentLeft4FileInfo').show();
				$('#mainContentLeft4FileInfo').html(html);
				documentCenter.showFileInfo(fileId,folderId);
				/*var html = template('content_panel_template', {});
				$('#main-container').append(html);
				var $panel = $("#action-content-panel");
				var h = window.innerHeight - 58;
				$panel.width("600").height(h);
				$panel.css({top:10});
				$panel.show(300);
				documentCenter.showFileInfo(fileId,folderId,'2');*/
				
			}
		});
		return false;
		
	});
	
	
	$('#main-container').on('click','#mySubScribeBackBtn',function(){
		/**  如果为0那么就是返回到正常页面，为1就返回搜索结果页面  **/
		if(isSubSearchScribeTag == 0){
			$('#content-heading').find("#mySubScribesTitle").html(template('mySubScribeTitle_templete'));
			getSubScribeMsgsFun(1);
			showMainContentLeftFooter(true);
		}else{
			showMainContentLeftFooter(true);
			searchSubScribeMsgByKeyWord($("#searchFileInputId").val(),isSubSearchScribeTag);
		}
		
	});
	
	$('#main-container').on('click','#mySubScribeBrand',function(){
		isSubSearchScribeTag = 0;
		$("#content-heading").find(".navbar-brand").html("我的关注");
		getSubScribeMsgsFun(1);
		showMainContentLeftFooter(true);
		$("#subScribeBtn").show();
	});
	
	/**  添加关注人 **/
	$("#mainContentBody").on("click","#subScribeBtn",function(){
		
		/** 获取关注信息  **/
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/getSubScribersByUserId?callback=?';
		var data = {'userId':window.userId,'companyId':window.companyid,'userName':window.userName};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			var html = template('mySubScribeUsers_templete', json);
			$('#main-container').append(html);
			
			/**    添加滚动条     **/
			
			var $panel = $("#action-content-panel");
			var h = window.innerHeight - 58;
			var mySubScribesContainerHeight  = 134;
			var mySubScribesAvaliableContainer  = h-mySubScribesContainerHeight-190;
			$("#mySubScribesContainer").css({"height":mySubScribesContainerHeight,"width":580,"position":"relative"}).perfectScrollbar();
			$("#mySubScribesAvaliableContainer").css({"height":mySubScribesAvaliableContainer,"position":"relative"}).perfectScrollbar();
			$panel.width("600").height(h);
			$panel.css({top:10});
			$panel.show(300);
			$("#art_panel_maskLayer").removeClass("hidden");
		});
		return false;
	});
	
	
	/**
	 * 添加关注人
	 * **/
	$('#main-container').on('click',"#subScribeUsersBtn",function(){
		var chekedUsers = "";
		 $("[name='subScribeCK']:checkbox").each(function(){
			   if($(this).prop("checked"))
			   {
				   chekedUsers += $(this).attr("username")+",";
			   }
		 });
		 if(chekedUsers.length>0){
			 chekedUsers = chekedUsers.substring(0,chekedUsers.length-1);
		 }else{
			 showBottomMsg("请选择关注的成员",2);
			 return true;
		 }
		 
		 
		 var url = $.appClient.generateUrl({ESDocumentCenter:'addSubScribeMsgByUserId'},'x');
			$.ajax({
				url:url,
				type:'POST',
				data:{userId:window.userId,chekedUsers:chekedUsers},
				success:function(data){
					refreshSubScribePanel();
					showBottomMsg("关注成功",1);
					$("#subScribersAvaliableSearchInput").val("");
				}
			});
		 return false;
		 
	});
	
	/**
	 * 删除关注人
	 * **/
	$('#main-container').on('click',"#delSubScribeUsersBtn",function(){
		var chekedUsers = "";
		 $("[name='subScribeCK_del']:checkbox").each(function(){
			   if($(this).prop("checked"))
			   {
				   chekedUsers += $(this).attr("username")+",";
			   }
		 });
		 if(chekedUsers.length>0){
			 chekedUsers = chekedUsers.substring(0,chekedUsers.length-1);
		 }else{
			 showBottomMsg("请选择取消关注的成员",2);
			 return true;
		 }
		 
		 var url = $.appClient.generateUrl({ESDocumentCenter:'delSubScribeMsgByUserId'},'x');
			$.ajax({
				url:url,
				type:'POST',
				data:{userId:window.userId,chekedUsers:chekedUsers},
				success:function(data){
					refreshSubScribePanel();
					showBottomMsg("取消成功",1);
					$("#subScribersSearchInputId").val("");
				}
			});
		 return false;
		 
	});
	
	/**
	 * 未关注人员勾选事件
	 * **/
	$('#main-container').on('click',".groupMemmbers_subScribe",function(){
		var ck = $(this).find("[name = 'subScribeCK']:checkbox");
		if(ck.prop("checked")){
			ck.prop("checked",false);
		}else{
			ck.prop("checked", true);
		}
		return true;
	});
	
	/**
	 * 已经关注人员勾选事件
	 * **/
	$('#main-container').on('click',".groupMemmbers_subScibeDel",function(){
		var ck = $(this).find("[name = 'subScribeCK_del']:checkbox");
		if(ck.prop("checked")){
			ck.prop("checked",false);
		}else{
			ck.prop("checked", true);
		}
		return true;
	});
	
	$('#main-container').on('click',".groupMemmbers_subScibeDel :checkbox,.groupMemmbers_subScribe :checkbox",function(){
		var ck = $(this);
		if(ck.prop("checked")){
			ck.prop("checked",false);
		}else{
			ck.prop("checked", true);
		}
		return true;
	});
	
	function refreshSubScribePanel(){
		/** 获取关注信息  **/
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/getSubScribersByUserId?callback=?';
		var data = {'userId':window.userId,'companyId':window.companyid,'userName':window.userName};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			var mySubScribesContainer_html = template('mySubScribersSelected_templete', json);
			var mySubScribesAvaliableContainer_html = template('mySubScribersAvaliable_templete', json);
			$("#mySubScribesContainer").html(mySubScribesContainer_html);
			$("#mySubScribesAvaliableContainer").html(mySubScribesAvaliableContainer_html);
		});
		
//		closeContentPanel();
		return true;
	}
	
	
	/**  对具体文件所有者进行关注   **/
	$("#main-container").on("click",".rssbtn",function(){
		 var chekedUsers = $(this).attr("target-data-name");
		 if(chekedUsers == window.userName){
			 showBottomMsg("亲,这个文档貌似是您自己上传的,不能关注!",2);
			 return ;
		 }
		 var rssbtn = $(this);
		 var url = $.appClient.generateUrl({ESDocumentCenter:'addSubScribeMsgByDocs'},'x');
			$.ajax({
				url:url,
				type:'POST',
				data:{userId:window.userId,chekedUsers:chekedUsers},
				success:function(data){
					var jsonDX = eval('(' + data + ')');
					if(jsonDX.success == "true"){
						//使用位置查找元素，兼容文件详情和历史版本关注设置
						rssbtn.addClass("hidden");
						rssbtn.parent().find(".cancelrssbtn").removeClass("hidden");
						showBottomMsg(jsonDX.msg,1);
					}else{
						showBottomMsg(jsonDX.msg,3);
					}
					
				}
			});
		 return false;
	});
	
	/**
	 * 文件详细页中取消关注
	 */
	$("#main-container").on("click",".cancelrssbtn",function(){
		var chekedUsers = $(this).attr("target-data-name");
		var rssbtn = $(this);
		$.ajax({
			url: $.appClient.generateUrl({ESDocumentCenter:'delSubScribeMsgByUserId'},'x'),
			type:'POST',
			data:{userId:window.userId,chekedUsers:chekedUsers},
			success:function(data) {
				var jsonDX = eval('(' + data + ')');
				if(jsonDX.success == "true"){
					rssbtn.addClass("hidden");
					rssbtn.parent().find(".rssbtn").removeClass("hidden");
					showBottomMsg(jsonDX.msg,1);
				}else{
					showBottomMsg(jsonDX.msg,3);
				}
			}
		});
		 return false;
	});
	
	/** 我的关注 End **/
	

	/**
	 * 点击设置
	 */
	$("#bodyContent_Title").on("click","#settingUp",function(){
		changeUploadBtn("0");
		closeFileInfo();
		// 设置头部
		var jsonHeading = '{"type":"'+$(this).attr('id')+'"}';
		$("#content-heading").html(template('file_panel_heading_template', jQuery.parseJSON(jsonHeading)));//ClassSetting_templete
		$("#contentBody").html("");
		showMainContentLeftFooter(false);
		//查出数据
		var $obj = $('.main-left li.active');
		var flag = $obj.attr('flag');
		var groupname = $obj.find('a').html();
		var groupid = $obj.attr('data-group-id');
		var classId = $obj.attr('data-class-id');
//		jQuery.getJSON(window.onlinefilePath+'/rest/onlinefile_documentclass/getClassInfo?callback=?', {'companyId':window.companyid,'userId':window.userId,'groupname':groupname, 'flag':flag, "groupid":groupid},
//				function(json) {
//			if(json.isOk){
//				$("#contentBody").html(template("ClassSetting_templete",json));
//				operateClass(json.info.isgroupuser);
//				
//			}
//		});
		
		var url =  $.appClient.generateUrl({ESDocumentClass : 'getClassInfo'},'x');
		$.ajax({
			url:url,
			type: "POST",
			data:{companyId:window.companyid,userId:window.userId,groupname:groupname,flag:flag,groupid:groupid},
			success:function(data){
				var json = $.parseJSON(data);
				if(json.isOk){
					$("#contentBody").html(template("ClassSetting_templete",json));
					operateClass(json.info.isgroupuser);
				
				}
			}
		});
	});
	var emailCurentPage = 1;
	/**  我的邮箱 start  **/
	var emailAction = {
			/**  基础URL  **/
			baseUrl: window.onlinefilePath,
			getDefaultAttachmentByEmail:function(userid,pageIndex) {
				var getDefaultAttachmentByEmailUrl = $.appClient.generateUrl({ESEMail : 'getDefaultAttachmentByEmail'}, 'x');
				$.post(getDefaultAttachmentByEmailUrl,{userid:userid,companyName:window.companyName,username:window.userName,pageIndex:pageIndex,pageLimit:20,windowWidth:($(window).width()-210),ip:window.ip}, function(result){
					
					if(!$("#myEmail").hasClass("active")){ 
						return;
					}
					var json = eval('(' + result + ')');
					if(json.emailAttechMents != ""){
						$("#contentBody").html(template("myEmail_templete",json));
					}else if($("#myEmailListsDropDown").length>0){
						$("#contentBody").html(template("myEmail_templete",json));
					}
					$("#content-list").scrollTop(0);
					if(window.companyid !='-1'){
						$("#content-list").perfectScrollbar("update");
					}
					// 分页
					$("#pagingDiv").pagination({
						pages: json.emailAttechMentPages,
						currentPage: 1,
						onPageClick:function(pageNumber, event) {
							emailAction.searchEmialAttachsByKeyWord(getCurrentEmailAddress(),getCurrentEmailKeyWord(),pageNumber);
							emailCurentPage = pageNumber;
						}
					});
					
				
				});
				
			},
			getEmailList:function(userid){
				var url = window.onlinefilePath+'/rest/onlinefile_emailws/getEmailList?callback=?';
				var data = {userid:window.userId,companyName:window.companyName,username:window.userName,ip:window.ip};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
				function(json) {
					if(!$("#myEmail").hasClass("active")){ 
						return;
					}
					if(json.emails == ""){
						$("#contentBody").html(template('myEmailListsEmpty_templete',{}));
						showMainContentLeftFooter(false);
						//showBottomMsg("没有可用的邮箱,您可以在右上角的邮箱管理功能里面添加一个邮箱!",2);
					}else{
						showMainContentLeftFooter(true);
						$('#contentHeader_right').html(template('myEmailHeader_right_templete', json));
						emailAction.getDefaultEmailList();
					}
				});
				
			},
			searchEmialAttachsByKeyWord:function(emailAddress,searchKeyWord,pageIndex){
				
				var searchEmialAttachsByKeyWordUrl = $.appClient.generateUrl({ESEMail : 'searchEmialAttachsByKeyWord'}, 'x');
				$.post(searchEmialAttachsByKeyWordUrl,{"userid":g_userId,'companyName':window.companyName,'username':window.userName,"searchKeyWord":searchKeyWord,"email":emailAddress,
	    			"pageIndex":pageIndex,"pageLimit":20,windowWidth:($(window).width()-210),ip:window.ip}, function(result){
						 var json = eval('(' + result + ')');
							$("#contentBody").html(template("myEmail_templete",json));
							$("#content-list").scrollTop(0);
							$("#content-list").perfectScrollbar('update');
							// 分页
							$("#pagingDiv").pagination({
								pages: json.emailAttechMentPages,
								currentPage: pageIndex,
								onPageClick:function(pageNumber, event) {
									emailAction.searchEmialAttachsByKeyWord(getCurrentEmailAddress(),getCurrentEmailKeyWord(),pageNumber);
									emailCurentPage = pageNumber;
								}
							});
				});
			},
			isSynEmailAttachMent:function(email){
				var isSynEmailAttachMentUrl = $.appClient.generateUrl({ESEMail : 'isSynEmailAttachMent'}, 'x');
				$.post(isSynEmailAttachMentUrl,{"userid":g_userId,'companyName':window.companyName,'username':window.userName,"email":email,ip:window.ip}, function(result){
						 	var json = eval('(' + result + ')');
							if(json.success == "true"){
								emailAction.getAttachmentByEmail(email,1);
								showBottomMsg(json.msg,1);
							}else{
								showBottomMsg(json.msg,3);
								emailAction.getAttachmentByEmail(getCurrentEmailAddress(),1);
								emailSettingFun(getCurrentEmailAddress());
								$("#email_panel_maskLayer").show();
							}
						
						 
	    		});
				
			},
			getAttachmentByEmail:function(emailAddress,pageIndex) {
				var getAttachmentByEmailUrl = $.appClient.generateUrl({ESEMail : 'getAttachmentByEmail'}, 'x');
				$.post(getAttachmentByEmailUrl,{"userid":g_userId,'companyName':window.companyName,'username':window.userName,"emailAddress":emailAddress,pageIndex:pageIndex,pageLimit:"20",windowWidth:($(window).width()-210),ip:window.ip}, function(result){
					var json = eval('(' + result + ')');
					$("#contentBody").html(template("myEmail_templete",json));
					$("#content-list").scrollTop(0);
					if(window.companyid != '-1'){
						$("#content-list").perfectScrollbar('update');
						$("#dropdownMenu_email").html(emailAddress+"<span class='caret'></span>").attr("title",emailAddress);
					}
					// 分页
					$("#pagingDiv").pagination({
						pages: json.emailAttechMentPages,
						currentPage: pageIndex,
						onPageClick:function(pageNumber, event) {
							emailAction.getAttachmentByEmail(getCurrentEmailAddress(),pageNumber);
							emailCurentPage = pageNumber;
						}
					});
				
						 	
				});	 	
				
			},
			getDefaultEmailList:function(){
				var url = window.onlinefilePath+'/rest/onlinefile_emailws/getDefaultEmail?callback=?';
				var data = {"userid":g_userId,'username':window.userName,'companyName':encodeURI(window.companyName, "utf-8")};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
					if(json.emails == ""){
						
					}else{
						var emailAddress = json.defaultEmail;
					 	if(emailAddress.length>18){
					 		emailAddress = emailAddress.substring(0,18);
					 	}
					 	$("#dropdownMenu_email").html(emailAddress+"<span class='caret'></span>").attr("title",json.defaultEmail);
					}
				});
			},
			getEmailAttachMentInfoByEmail:function(email,emailIndex,partCount,pageIndex,pageLimit){
				var url = window.onlinefilePath+'/rest/onlinefile_emailws/getEmailAttachMentInfoByEmail?callback=?';
				var data = {"userid":g_userId,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,"emailAddress":email,emailIndex:emailIndex};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
					var panelHtml = template('email_attachment_content_panel_template',{});
					$('#main-container').append(panelHtml);
					var $panel1 = $("#action-emailattachment-content-panel");
					var h = 560;
					if(json.emailText == ""){
						$("#emailContentTextContainer").html("发件人太懒了,什么邮箱内容都没给您留下!").css({"height":h-60});
					}else{
						$("#emailContentTextContainer").html(json.emailText).css({"height":$(window).height()-114});
					}
					
					$("#attachMentsListsContainer").height(h-115);
					$("#loadEmaiAttachMentTitle").css("margin-top",h/2-30);
					$("#emailContentTextContainer").perfectScrollbar();
					$panel1.width("600").height($(window).height()-52);
					$panel1.css({top:0});
					$panel1.show(300);
					
					//获取附件
					if(json.attachmentCount>0){
						$("#emailAttachMentsBody").css("width",420).height($(window).height()-104);
						$("#attachMentsListsFooter").css("display","").height($(window).height()-130);
						var url = window.onlinefilePath+'/rest/onlinefile_emailws/getEmailAttachMentsByEmail?callback=?';
						var data = {"userid":g_userId,'companyName':encodeURI(window.companyName, "utf-8"),'username':window.userName,"emailAddress":email,partCount:partCount,emailIndex:emailIndex,pageIndex:pageIndex,pageLimit:pageLimit};
						var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
						jQuery.getJSON(url, ret.data,
								function(json) {
							if(json.success == "false"){
								showBottomMsg(json.msg,3);
								$("#emailEmptyText").html("获取附件失败!");
								return ;
							}
							var attachmentsHtml = template('email_attachments_panel_template',json);
							$("#attachMentsListsContainer").height($(window).height()-170).html(attachmentsHtml);
							$("#attachMentsListsContainer").perfectScrollbar();
						});
					}else{
						$("#emailAttachMentsBody").css("width",600).height($(window).height()-104);
						$("#attachMentsListsFooter").css("display","none");
					}
				});
			}
	}
	
	/**  我的邮箱  **/
	$("#privateClassSub").on("click","#myEmail",function(){
		$('#bodyContent_Title').html(template('userCenter_menu_templete'));
		closeFileInfo();
		myEmailsContent();
		emailAction.getDefaultAttachmentByEmail(g_userId,1);
	});
	
	function myEmailsContent(){
		// 设置头部
		changeUploadBtn("0");//隐藏上传按钮
		var jsonHeading = '{"type":"myEmail"}';
		$("#content-heading").html(template('file_panel_heading_template', jQuery.parseJSON(jsonHeading)));
		myProgressBar("正在加载邮箱信息,请稍后...");
		emailAction.getEmailList(g_userId);
	}
	
	/**  邮箱搜索  **/
	$("#main-container").on("click","#btn_emailSearch",function(){
		emailAction.searchEmialAttachsByKeyWord(getCurrentEmailAddress(),getCurrentEmailKeyWord(),1);
	});
	
	/**   点击邮箱下拉菜单方法  **/
	$("#main-container").on("click","#myEmailListsDropDown ul li",function(){
		var emailAddress = $(this).text();
	 	if(emailAddress.length>18){
	 		emailAddress = emailAddress.substring(0,18);
	 	}
	 	$("#contentBody").html(template('myEmailProgress_templete'));
		$("#dropdownMenu_email").html(emailAddress+"<span class='caret'></span>").attr("title",$(this).text());
		emailAction.getAttachmentByEmail($(this).text(),1);
	});
	
	
	$('#main-container').on('click','#uploadEmailAttachMentBtn',function(){
		
		var chekedAttachMents = "";
		 $("[name='checkEmailAttachMent']:checkbox").each(function(){
			   if($(this).prop("checked"))
			   {
				   chekedAttachMents += $(this).attr("emailIndex")+",";
			   }
		 });
		 if(chekedAttachMents.length>0){
			 chekedAttachMents = chekedAttachMents.substring(0,chekedAttachMents.length-1);
		 }else{
			 showBottomMsg("请至少勾选一个附件上传!",2);
			 return false;
		 }
		
		$("#showtitleSpanId").text("附件归类");
		$("#showtitleSpanId").attr("title",$(this).attr("title"));
		$("#uploadBtnId").attr("value",chekedAttachMents);
		$("#uploadShareMainDivId").css("display","");
		$("#uploadbg").show();
		$("#uploadBtnId").text("归类").removeClass("disabled");
		$("#uploadChooseFolderDivId").hide();
		$("#uploadSkipBtnId").hide();
		$("#uploadOpenlevelUlId li.privateDocLi,.keepSettingSpan,.keepSettingDropDownDiv").hide();
		$(".fileShareBtn").css("margin-top",-20);
		$("#action-content-panel-upload").show();
		return true;
	});
	
	$('#main-container').on('click','#checkAllEmailAttachMents',function(){
		 var checkAllCheckboxObj = $(this);
		 if($(this).prop("checked")){
			 $("[name='checkEmailAttachMent']:checkbox").each(function(){
				 $(this).prop("checked",true);
			 });
		 }else{
			 $("[name='checkEmailAttachMent']:checkbox").each(function(){
				 $(this).prop("checked",false);
			 });
		 }
	});
	
	$('#main-container').on('click','.emaiAttatchmentsBorder',function(){
		var ck = $(this).find(".checkEmailAttachMentCheckboxCls");
		if(ck.prop("checked")){
			ck.prop("checked",false);
		}else{
			ck.prop("checked", true);
		}
		return true;

	});
	
	$('#main-container').on('click','.emaiAttatchmentsBorder input[type="checkbox"]',function(){
		var ck = $(this);
		if(ck.prop("checked")){
			ck.prop("checked",false);
		}else{
			ck.prop("checked", true);
		}
		return true;
	});
	
	/**  返回我的邮箱 **/
	$('#main-container').on('click','#myEmailBrand',function(){
		if($(this).html().indexOf("我的邮箱")>-1){
			return ;
		}
		
		emailAction.getAttachmentByEmail(getCurrentEmailAddress(),emailCurentPage);
		//emailAction.searchEmialAttachsByKeyWord(getCurrentEmailAddress(),"",emailCurentPage);
		$("#content-heading").find(".navbar-brand").html("我的邮件").attr("title","");
		//myEmailsContent();
		return true;
	});
	
	/** 邮箱同步 **/
	$("#main-container").on("click","#emailSyncBtn",function(){
		$.dialog({
			title: '邮箱同步',
			content : '确定同步该邮箱吗？可能会占用您一点时间，我们会为您缓存最近三个月的邮件！',
			width: 300,
			okValue : '同步',
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				myProgressBar("正在同步邮箱信息,请稍后...");
				emailAction.isSynEmailAttachMent(getCurrentEmailAddress());
			}
		}).showModal(); 
	});
	
	/**   获取邮箱内容方法 file-itemEmail**/
	$("#main-container").on("click",".file-itemEmail",function(){
		//$("#contentBody").find(".myEmailGroupCls li").removeClass("active");
		//$(this).addClass("active");
		emailAction.getEmailAttachMentInfoByEmail(getCurrentEmailAddress(),$(this).attr("value"),$(this).attr("partCount"),1,20);
	});
	
	function myProgressBar(contentText){
		var contentText = '{"contentText":"'+contentText+'"}';
		$("#contentBody").html(template('myEmailProgress_templete',jQuery.parseJSON(contentText)));
	}
	
	/**  获取当前邮箱地址 **/
	function getCurrentEmailAddress(){
		return $("#dropdownMenu_email").attr("title");
	}
	
	/**  获取当前邮箱搜索关键词  **/
	function getCurrentEmailKeyWord(){
		return $("#searchFileInputId").val();
	}
	
	/**  点击添加一个邮箱 ***/
	$("#contentBody").on('click','#addEmailAddressBtn',function(){
		emailSettingFun();
		$("#email_panel_maskLayer").show();
		return false;
	});
	
	/**  我的邮箱 end  **/
	
	
	/**  邮箱设置 start  **/
	var emailManage = {
			/**  基础URL  **/
			baseUrl: window.onlinefilePath,
			getEmailList:function(emailAddress) {
				var url = window.onlinefilePath+'/rest/onlinefile_emailws/getEmailList?callback=?';
				var data = {userid:window.userId,companyName:window.companyName,username:window.userName,ip:window.ip};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
//				var getEmailListUrl = $.appClient.generateUrl({ESEMail : 'getEmailList'}, 'x');
//				$.post(getEmailListUrl,{userid:window.userId,companyName:window.companyName,username:window.userName,ip:window.ip}, function(result){
//					var json = eval('(' + result + ')');

					$('#emailListsContainerForScro').html(template('email_list_item_template', json));
					$('#leftSection').scrollbarCreate();
					refreshEmailSetting();
					
					/**  此判断服务于同步邮箱时候当前邮箱的用户名或者密码错误时候，弹出修改密码窗体  **/
					if(emailAddress!= undefined ){
						var emailListDivContainer = $("#emailListsContainerForScro div");
						emailListDivContainer.removeClass("emailList_selected");
						emailListDivContainer.addClass("emailList");
						$("#emailListsContainerForScro div").filter(":contains('"+emailAddress+"')").addClass("emailList_selected");
						getEmailSettingByEmailAddress(emailAddress);
					}
					
					if(json.emails==""){  //禁用右侧保存按钮 设置默认邮箱按钮
						$("#emailSyncBtn").hide();
						$("#dropdownMenu_email").hide();
						$("#setAsDefaultEmail").attr("disabled", true); 
						//清除邮箱设置内容
	    				$("#emailId").val("");
	    				$("#emailAddressInput_set").val("").removeAttr("readonly");
	    				$("#emailPasswordInput_set").val("");
	    				$("#receiveEmailServer_set").val("");
	    				$("#receiveEmailServerPort_set").val("");
	    				$("#sendEmailServer_set").val("");
	    				$("#sendEmailServerPort_set").val("");
					}else{
						$("#emailSyncBtn").show();
						$("#dropdownMenu_email").show();
						$("#emailAddressInput_set").attr("readonly","readonly");
						$("#setAsDefaultEmail").attr("disabled", false); 
					}
				});
				
			},
			/**  新建邮箱完成后  缓存该邮箱邮件  **/
			cacheAllEmailAttachments:function(email){
				var cacheEmailListUrl = $.appClient.generateUrl({ESEMail : 'cacheAllEmailAttachments'}, 'x');
				$.post(cacheEmailListUrl,{userid:window.userId,email:email,ip:window.ip}, function(result){
					var json = eval('(' + result + ')');
					if(json.success == "true"){
						if(null != $("#myEmail").attr("class") && $("#myEmail").attr("class") == "active"){
							myEmailsContent();
							emailAction.getAttachmentByEmail(email,1);
						}
					}
				});
			}
	}
	
	/** 显示邮箱设置信息 **/
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
//				showMsg(jsonDX.msg);
			}
		});
	}
	
	/** 刷新邮箱列表 **/
	function refreshEmaiList(){
		emailManage.getEmailList();
		$("#emailAddressInput_set").attr("readonly","readonly");
		//我的邮件面板存在  刷新邮箱切换列表
		if($("#myEmailListsDropDown").length>0){
			emailAction.getEmailList(g_userId);
		}
	}
	
	/** 刷新邮箱列表 **/
	function refreshSaveEmailList(){
		
		//我的邮件面板存在  刷新邮箱切换列表
		if($("#myEmailListsDropDown").length>0){
			emailAction.getEmailList(g_userId);
		}
		
	}
	
	/** 默认显示第一个邮件设置信息 **/
	function refreshEmailSetting(){
		if($(".emailList_selected").get(0) != undefined){
			getEmailSettingByEmailAddress($(".emailList_selected").html());
		}
	}
	
	/**  点击邮箱设置  **/
	$("#emailSetting").on("click",function(){
		if(window.globalUserStatus == 0){
			showBottomMsg("当前会话已经过期，请<a onclick='gotoIndexPage()'>重新登录</a>。", 3);
			return false ;
		}
		emailSettingFun();
		$("#email_panel_maskLayer").show();
		return true;
	});
	
	
	function emailSettingFun(emailAddress){
		var html = template('email_setting_panel_template', {});
		$('#main-container').append(html);
		var $panel = $("#email-setting-content-panel");
		$panel.width(670).height($(window).height()-52);
		$panel.css({top:10});
		$panel.show(300);
		//刷新邮箱列表
		emailManage.getEmailList(emailAddress);
		
		
		/**  新建邮箱  **/
		$("#addEmailBtn").on("click",function(){
			autoAddEmailAddress("","");
		});
		
		/** 自动创建邮箱执行代码 **/
		function autoAddEmailAddress(email, password){
			var html = template('email_content_panel_template', {});
			$('#main-container').append(html);
			var data = template('auto_add_email_panel',{});
			$("#email-create-content-panel .panel-body").html(data);
			var $panel = $("#email-create-content-panel");
			$("#emailAutoInput").val(email);
			$("#password").val(password);
			$panel.width(375).height(220);
			$panel.css({top:160});
			$panel.show(300);
			
			var emai_errorMsg = "邮箱验证失败!";
			
			$("#emailAutoInput").changeTips({
				divTip:".on_changes"
			}); 
			
			$("#goAddEmailManual").click(function(){
				
				var tmpEmailManual = $("#emailAutoInput").val() ;
				var tmpPassWordManual = $("#password").val() ;
				closeCreateEmailPanel();
				
				
				var html = template('email_content_panel_template', {});
				$('#main-container').append(html);
				var data = template('manual_add_email_panel',{});
				$("#email-create-content-panel .panel-body").html(data);
				var $panel = $("#email-create-content-panel");
				$panel.width(480).height(330);
				$panel.css({top:140,"margin-right":"95px"});
				$("#emailManualInput").val(tmpEmailManual);
				$("#passwordManualInput").val(tmpPassWordManual);
				$panel.show(300);
				
				/** pop3 服务开启指导  **/
				$("#openPop3Server").on("click",function(){
					window.open('files/guideimage/guideImg.png', '_blank');
				});
				
				$("#emailManualInput").changeTips({
					divTip:".on_changes_manul"
				}); 
				
				
				/**  返回自动创建窗口 **/
				$("#goAddEmailAuto").on("click",function(){
					closeCreateEmailPanel();
					autoAddEmailAddress(tmpEmailManual,tmpPassWordManual);
				});
				
				//新建手动邮箱
				$("#doAddEmailManual").click(function(){
					
					var isAvaliable = true;
					$(".emailError").each(function(){ 
							if($(this).css("display") == "block"){
								isAvaliable = false;
								return isAvaliable;
							}
						});
					if(!isAvaliable){
						showBottomMsg(emai_errorMsg,3);
						return true;
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
    						showBottomMsg(jsonDX.msg,1);
    						closeCreateEmailPanel();
    						refreshEmaiList();
    						emailManage.cacheAllEmailAttachments(email);
    					}else{
    						
    						if(jsonDX.msg.indexOf("支持")>-1){
    							$("#passwordManualInput_setError").show().attr("title",jsonDX.msg);
        						$("#emailManualInput_setError").show().attr("title",jsonDX.msg);
    							showBottomMsg(jsonDX.msg,2);
    						}else{
    							$("#passwordManualInput_setError").show().attr("title","邮箱或密码错误或未开启pop3服务");
        						$("#emailManualInput_setError").show().attr("title","邮箱或密码错误或未开启pop3服务");
    							showBottomMsg(emai_errorMsg,3);
    						}
    					}
    					
    				});
    				return false ;
				});
				
				//关闭手动创建邮箱窗口
				$("#closeAddEmialManualPanel").click(function(){
					closeCreateEmailPanel();
				});
			});
			
			//新建自动邮箱
			$("#doAddEmailAuto").click(function(){
				var isAvaliable = true;
				$(".emailAutoError").each(function(){ 
						if($(this).css("display") == "block"){
							isAvaliable = false;
							return isAvaliable;
						}
					});
				if(!isAvaliable){
					showBottomMsg(emai_errorMsg,3);
					return true;
				}
				var email = $("#emailAutoInput").val();
				var password = $("#password").val();
				var addEmailurl = $.appClient.generateUrl({ESEMail : 'addEmail'}, 'x');
				$.post(addEmailurl,{email:email,password:password,userid:window.userId,companyName:window.companyName,ip:window.ip,username:window.userName}, function(result){
					var jsonDX = eval('(' + result + ')');
					if(jsonDX.success == "true"){
						showBottomMsg(jsonDX.msg,1);
						closeCreateEmailPanel();
						refreshEmaiList();
						emailManage.cacheAllEmailAttachments(email);
					}else{
						if(jsonDX.msg.indexOf("支持")>-1){
							$("#emailAutoInput_setError").show().attr("title",jsonDX.msg);
							$("#password_setError").show().attr("title",jsonDX.msg);
							showBottomMsg(jsonDX.msg,2);
						}else{
							$("#emailAutoInput_setError").show().attr("title","邮箱或密码错误或未开启pop3服务");
							$("#password_setError").show().attr("title","邮箱或密码错误或未开启pop3服务");
							showBottomMsg(emai_errorMsg,3);
						}
						
					}
					
				});
				return false ;
			});
			
			//关闭自动创建邮箱窗口
			$("#closeAddEmialAutoPanel").click(function(){
				closeCreateEmailPanel();
			});
			/** pop3 服务开启指导  **/
			$("#openPop3Server").on("click",function(){
				window.open('files/guideimage/guideImg.png', '_blank');
			});
		}
		
		/** 删除邮箱  **/
		$("#delEmailBtn").click(function() {
			var emailSelected = $('#leftSection .emailList_selected').text();
			if(emailSelected==""){
				showBottomMsg("请选择要删除的邮箱",2);
				return;
			}
			dialog({
				title: '删除邮箱',
				content: '确定删除该邮箱吗?',
				width: 200,
				quickClose: true,
				okValue: '删除',
				ok: function () {
		    		var delEmailurl = $.appClient.generateUrl({ESEMail : 'deleteEmail'}, 'x');
		    		$.post(delEmailurl,{email:emailSelected,userid:window.userId,companyName:window.companyName,ip:window.ip,username:window.userName}, function(result){
		    			var jsonDX = eval('(' + result + ')');
		    			if(jsonDX.success == "true"){
		    				showBottomMsg(jsonDX.msg,1);
		    				refreshEmaiList();
		    				
		    				//我的邮件面板存在  刷新邮箱切换列表
		    				if($("#myEmailListsDropDown").length>0){
		    					emailAction.getEmailList(g_userId);
		    					if(emailSelected==$("#dropdownMenu_email").attr("title")){
		    						emailAction.getDefaultAttachmentByEmail(g_userId,1);
		    					}
		    				}
		    			}else{
		    				showBottomMsg(jsonDX.msg,3);
		    			}
		    		});
				},
				cancelValue: '取消',
				cancel: true
			}).show();
    		return true;
		});
		
		/**   清空邮箱  **/
		$("#delAllEmailBtn").click(function() {
			var emailSelected = $('#leftSection .emailList_selected').text();
			if(emailSelected==""){
				showBottomMsg("没有需要清空的邮箱",2);
				return;
			}
			
			dialog({
				title: '清空邮箱',
				content: '确定清空邮箱吗?',
				width: 200,
				quickClose: true,
				okValue: '清空',
				ok: function() {
		    		var delAllEmailurl = $.appClient.generateUrl({ESEMail : 'deleteAllEmail'}, 'x');
		    		$.post(delAllEmailurl,{userid:window.userId,companyName:window.companyName,ip:window.ip,username:window.userName}, function(result){
		    			var jsonDX = eval('(' + result + ')');
		    			if(jsonDX.success == "true"){
		    				showBottomMsg(jsonDX.msg,1);
		    				//清除邮箱设置内容
		    				$("#emailId").val("");
		    				$("#emailAddressInput_set").val("");
		    				$("#emailPasswordInput_set").val("");
		    				$("#receiveEmailServer_set").val("");
		    				$("#receiveEmailServerPort_set").val("");
		    				$("#sendEmailServer_set").val("");
		    				$("#sendEmailServerPort_set").val("");
		    				
		    				emailManage.getEmailList()
		    				//我的邮件面板存在  刷新邮箱切换列表
		    				if($("#myEmailListsDropDown").length>0){
		    					emailAction.getEmailList(g_userId);
		    					emailAction.getDefaultAttachmentByEmail(g_userId,1);
		    				}
		    			}else{
		    				showBottomMsg(jsonDX.msg,3);
		    			}
		    		});
				},
				cancelValue: '取消',
				cancel: true
			}).show();
			return true;
		});
		
		/**  保存邮箱设置  **/
		$("#saveEmailBaseSetting").click(function() {
			var isAvaliable = true;
			$(".emailError").each(function(){ 
					if($(this).css("display") == "block"){
						isAvaliable = false;
						return isAvaliable;
					}
				});
		
			var id = $("#emailId").val();
			var email = $("#emailAddressInput_set").val();
			var password = $("#emailPasswordInput_set").val();
			var receiverserver = $("#receiveEmailServer_set").val();
			var receiveserverport = $("#receiveEmailServerPort_set").val();
			var sendserver =  $("#sendEmailServer_set").val();
			var sendserverport =  $("#sendEmailServerPort_set").val();
			
			if($.trim(email) == ""){
				$("#emailAddressInput_set").addClass("invalidEmailtext").attr("title","此项不能为空或空格");	
				$("#emailAddressInput_set").parent().find("#emailAddressInput_setError").show().attr("title","此项不能为空或空格");
				isAvaliable = false;
			}
			if($.trim(password) == ""){
				$("#emailPasswordInput_set").addClass("invalidEmailtext").attr("title","密码不能为空或空格");
				$("#emailPasswordInput_set").parent().find("#emailPasswordInput_setError").show().attr("title","密码不能为空或空格");
				isAvaliable = false;
			}
			if($.trim(receiverserver) == ""){
				$("#receiveEmailServer_set").addClass("invalidEmailtext").attr("title","POP服务器地址不能为空或空格");
				$("#receiveEmailServer_set").parent().find("#receiveEmailServer_setError").show().attr("title","POP服务器地址不能为空或空格");
				isAvaliable = false;
			}
			if($.trim(receiveserverport) == ""){
				$("#receiveEmailServerPort_set").addClass("invalidEmailtext").attr("title","pop服务器SSL端口不能为空或空格");
				$("#receiveEmailServerPort_set").parent().find("#receiveEmailServer_setError").show().attr("title","pop服务器SSL端口不能为空或空格");
				isAvaliable = false;
			}
			if($.trim(sendserver) == ""){
				$("#sendEmailServer_set").addClass("invalidEmailtext").attr("title","smtp服务器地址不能为空或空格");
				$("#sendEmailServer_set").parent().find("#sendEmailServerPort_setError").show().attr("title","smtp服务器地址不能为空或空格");
				isAvaliable = false;
			}
			if($.trim(sendserverport) == ""){
				$("#sendEmailServerPort_set").addClass("invalidEmailtext").attr("title","smtp服务器SSL端口不能为空或空格");
				$("#sendEmailServerPort_set").parent().find("#sendEmailServerPort_setError").show().attr("title","smtp服务器SSL端口不能为空或空格");
				isAvaliable = false;
			}
			
			if(!isAvaliable){
				showBottomMsg("表单验证失败!",2);
				return true;
			}
				
			if(id!=""){
				var saveEmailSettingUrl = $.appClient.generateUrl({ESEMail : 'saveEmailSetting'}, 'x');
				$.post(saveEmailSettingUrl,{id:id,email:email,password:password,receiverserver:receiverserver,receiveserverport:receiveserverport,sendserver:sendserver,sendserverport:sendserverport,userid:window.userId,companyName:window.companyName,ip:window.ip,username:window.userName}, function(result){
					var jsonDX = eval('(' + result + ')');
					if(jsonDX.success == "true"){
						showBottomMsg(jsonDX.msg,1);
						refreshSaveEmailList();
					}else{
						$("#emailPasswordInput_setError").show().attr("title","邮箱或密码错误");
						showBottomMsg(jsonDX.msg,3);
					}
				});
			}else{
				var addEmailurl = $.appClient.generateUrl({ESEMail : 'addEmailManual'}, 'x');
				$.post(addEmailurl,{email:email,password:password,userid:window.userId,companyName:window.companyName,username:window.userName,ip:window.ip,popServerInput:receiverserver,popSSLPortInput:receiveserverport,smtpServerInput:sendserver,smtpSSLPortInput:sendserverport}, function(result){
					var jsonDX = eval('(' + result + ')');
					if(jsonDX.success == "true"){
						showBottomMsg(jsonDX.msg,1);
						refreshEmaiList();
						emailManage.cacheAllEmailAttachments(email);
					}else{
						$("#emailAddressInput_setError").show().attr("title","邮箱或密码错误");
						$("#emailPasswordInput_setError").show().attr("title","邮箱或密码错误");
						showBottomMsg(jsonDX.msg,3);
					}
				});
			}
			
		});
		
		/**  设置为常用邮箱  **/
		$("#setAsDefaultEmail").click(function() {
			var email = $("#emailAddressInput_set").val();
			var setAsDefaultEmail = $.appClient.generateUrl({ESEMail : 'setAsDefaultEmail'}, 'x');
			$.post(setAsDefaultEmail,{email:email,userid:window.userId,companyName:window.companyName,username:window.userName,ip:window.ip}, function(result){
				var jsonDX = eval('(' + result + ')');
				if(jsonDX.success == "true"){
					showBottomMsg(jsonDX.msg,1);
					//我的邮件面板存在  刷新邮箱切换列表
    				if($("#myEmailListsDropDown").length>0){
    					//emailAction.getDefaultAttachmentByEmail(g_userId,1);
    				}
				}else{
					showBottomMsg(jsonDX.msg,3);
				}
			});
		});
		
		/**  切换当前选中邮箱  **/
		$('#emailListsContainerForScro').on('click', 'div',function() {
			$("#emailListsContainerForScro div").removeClass("emailList_selected");
			$("#emailListsContainerForScro div").addClass("emailList");
			$(this).addClass("emailList_selected");
			/**    点击本次div时候从后台获取数据，然后渲染给右侧form表单中   **/
			getEmailSettingByEmailAddress($(this).text());
		});
		
	}
	
	
	/** 邮箱地址验证 **/
	$("#main-container").on('blur','#emailAddressInput_set',function(){
		var reg_mailType=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
		var reg_result = reg_mailType.test(this.value);
		if(!reg_result ){
			$(this).addClass("invalidEmailtext").attr("title","请输入合法的邮箱");
			$(this).parent().find("#emailAddressInput_setError").show().attr("title","请输入合法的邮箱");
		}else if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","此项不能为空或空格");	
			$(this).parent().find("#emailAddressInput_setError").show().attr("title","此项不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#emailAddressInput_setError").hide().attr("title","");
		}
	});
	
	/**    密码验证    **/
	$("#main-container").on('blur','#emailPasswordInput_set',function(){
		if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","密码不能为空或空格");
			$(this).parent().find("#emailPasswordInput_setError").show().attr("title","密码不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#emailPasswordInput_setError").hide().attr("title","");
		}
	});
	
	/** 接受服务器地址验证  **/
	$("#main-container").on('blur','#receiveEmailServer_set',function(){
		if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","POP服务器地址不能为空或空格");
			$(this).parent().find("#receiveEmailServer_setError").show().attr("title","POP服务器地址不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#receiveEmailServer_setError").hide().attr("title","");
		}
	});
	/** 接受服务器地址端口验证  **/
	$("#main-container").on('blur','#receiveEmailServerPort_set',function(){
		if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","pop服务器SSL端口不能为空或空格");
			$(this).parent().find("#receiveEmailServer_setError").show().attr("title","pop服务器SSL端口不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#receiveEmailServer_setError").hide().attr("title","");
		}
	});
	
	/** 发送服务器地址验证  **/
	$("#main-container").on('blur','#sendEmailServer_set',function(){
		if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","smtp服务器地址不能为空或空格");
			$(this).parent().find("#sendEmailServerPort_setError").show().attr("title","smtp服务器地址不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#sendEmailServerPort_setError").hide().attr("title","");
		}
	});
	
	/** 发送服务器地址端口验证  **/
	$("#main-container").on('blur','#sendEmailServerPort_set',function(){
		if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","smtp服务器SSL端口不能为空或空格");
			$(this).parent().find("#sendEmailServerPort_setError").show().attr("title","smtp服务器SSL端口不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#sendEmailServerPort_setError").hide().attr("title","");
		}
	});
	
	/**  自动创建邮箱验证  **/
	$("#main-container").on('blur','#emailAutoInput',function(){
		$("#sendEmailServerPort_setError").hide().attr("title","");
		if($(".on_changes").css("display") == "none"){
			var reg_mailType=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
			var reg_result = reg_mailType.test(this.value);
			if(!reg_result ){
				$(this).addClass("invalidEmailtext").attr("title","请输入合法的邮箱");
				$(this).parent().parent().find("#emailAutoInput_setError").show().attr("title","请输入合法的邮箱");
			}else if($(this).val() == null || $.trim($(this).val())==''){
				$(this).addClass("invalidEmailtext").attr("title","此项不能为空或空格");	
				$(this).parent().parent().find("#emailAutoInput_setError").show().attr("title","此项不能为空或空格");
			}else{
				$(this).removeClass("invalidEmailtext").attr("title","");
				$(this).parent().parent().find("#emailAutoInput_setError").hide().attr("title","");
			}
		}
	});
	
	
	/**    自动创建邮箱密码验证    **/
	$("#main-container").on('blur','#password',function(){
		$("#password_setError").hide().attr("title","");
		if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","密码不能为空或空格");
			$(this).parent().parent().find("#password_setError").show().attr("title","密码不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().parent().find("#password_setError").hide().attr("title","");
		}
	});
	
	
	/**   手动创建邮箱地址验证  **/
	$("#main-container").on('blur','#emailManualInput',function(){
		if($(".on_changes_manul").css("display") == "none"){
			var reg_mailType=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
			var reg_result = reg_mailType.test(this.value);
			if(!reg_result ){
				$(this).addClass("invalidEmailtext").attr("title","请输入合法的邮箱");
				$(this).parent().parent().find("#emailManualInput_setError").show().attr("title","请输入合法的邮箱");
			}else if($(this).val() == null || $.trim($(this).val())==''){
				$(this).addClass("invalidEmailtext").attr("title","此项不能为空或空格");	
				$(this).parent().parent().find("#emailManualInput_setError").show().attr("title","此项不能为空或空格");
			}else{
				$(this).removeClass("invalidEmailtext").attr("title","");
				$(this).parent().parent().find("#emailManualInput_setError").hide().attr("title","");
			}
		}
	});
	
	/**  手动创建密码   **/
	
	$("#main-container").on('blur','#passwordManualInput',function(){
		if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","密码不能为空或空格");
			$(this).parent().parent().find("#passwordManualInput_setError").show().attr("title","密码不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().parent().find("#passwordManualInput_setError").hide().attr("title","");
		}
	});
	
	
	/** 手动接受服务器地址验证  **/
	$("#main-container").on('blur','#popServerInput',function(){
		if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","POP服务器地址不能为空或空格");
			$(this).parent().find("#popServerInput_setError").show().attr("title","POP服务器地址不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#popServerInput_setError").hide().attr("title","");
		}
	});
	/** 手动接受服务器地址端口验证  **/
	$("#main-container").on('blur','#popSSLPortInput',function(){
		if($(this).val() == null || $.trim($(this).val())==''){
			$(this).addClass("invalidEmailtext").attr("title","pop服务器SSL端口不能为空或空格");
			$(this).parent().find("#popServerInput_setError").show().attr("title","pop服务器SSL端口不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#popServerInput_setError").hide().attr("title","");
		}
	});
	
	/** 手动发送服务器地址验证  **/
	$("#main-container").on('blur','#smtpServerInput',function(){
		if($(this).val() == null || $.trim($(this).val()) == ''){
			$(this).addClass("invalidEmailtext").attr("title","smtp服务器地址不能为空或空格");
			$(this).parent().find("#smtpServerInput_setError").show().attr("title","smtp服务器地址不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#smtpServerInput_setError").hide().attr("title","");
		}
	});
	
	/**手动发送服务器地址端口验证  **/
	$("#main-container").on('blur','#smtpSSLPortInput',function(){
		if($(this).val() == null || $.trim($(this).val()) == ''){
			$(this).addClass("invalidEmailtext").attr("title","smtp服务器SSL端口不能为空或空格");
			$(this).parent().find("#smtpServerInput_setError").show().attr("title","smtp服务器SSL端口不能为空或空格");
		}else{
			$(this).removeClass("invalidEmailtext").attr("title","");
			$(this).parent().find("#smtpServerInput_setError").hide().attr("title","");
		}
	});
	
	/**  邮箱设置 end  **/
	
	/** lujixiang 20150805 添加单击、control、shift组合事件,模拟windows文件夹操作方式 **/
	/**
	$(function () {
		var start = null;
		$("#content-list").on("click", "div.file-item", function(e) {
			e = e || event;
			if(e.shiftKey){
				var si = $(start).index(), ti = $(this).index();
				var fileItems = $("#content-list div.file-item");
				var sel = fileItems.slice(Math.min(si, ti)-1, Math.max(si, ti) );
				sel.addClass("active");
				fileItems.not(sel).removeClass('active');
				return;
			}else{
				if(!e.ctrlKey){
					$('#content-list div.file-item').removeClass('active');
					$(this).addClass('active');
				}else{
					if($(this).hasClass('active')){
						$(this).removeClass('active');
					}else{
						$(this).addClass('active');
					}
				}
				
			}
			start = this;
		});
	});
	
	**/
	
	/**  
	 * 点击文件item
	 */
	$(function () {
		
		var start = null;
		
		//wangwenshuo 点击文件列表空白区域时 清空已选择文件或文件夹
		$("#main-container").on("click", "#content-list", function(e) {
			var target = $(e.target);
			if(target.closest(".file-item").length == 0){ 
				start = null;
				var fileItems = $("#content-list div.file-item");
				fileItems.removeClass('active');
			}
		});
		
		$("#main-container").on("click", "div.file-item", function(e) {
						
			/** lujixiang 20150805 添加单击、control、shift组合事件,模拟windows文件夹操作方式  --start**/
			e = e || event;
			$fileItems =$("#main-container div.file-item");
			if(e.shiftKey){
				if(null == start){
					$(this).addClass('active');
					start = this;
				}else{
					var si = $fileItems.index(start), ti = $fileItems.index(this);
					var fileItems = $("#content-list div.file-item");
					var sel = fileItems.slice(Math.min(si, ti), Math.max(si, ti)+1 );
					sel.addClass("active");
					fileItems.not(sel).removeClass('active');
				}
				return;
			}else if(e.ctrlKey){
				if($(this).hasClass('active')){
					$(this).removeClass('active');
				}else{
					$(this).addClass('active');
				}
				start = this;
				return;
			}
			start = this;
			/** lujixiang 20150805 添加单击、control、shift组合事件,模拟windows文件夹操作方式  --start**/
			
			$('#content-list div.file-item').removeClass('active');
			$(this).addClass('active');
			
			//判断点击的是文件夹还是文件
			var fileId = $(this).attr("fileid");
			var fileName = $(this).attr("filename");
			var folderId = $(this).attr("folderId");
			var idSeq = $(this).attr("idseq");
			var isFile = $(this).attr("isfile");
			var openlevel = $(this).attr("openlevel");
			var isOwner = ($(this).attr("owner") == g_userId) ? true : false;
			/*if(!isOwner && openlevel == '3'){//不是拥有者公开级别是3的私密文件将取消进入
				showBottomMsg("私密文件,无访问权限！","3");
				return false;
			}*/
			closeContentPanel();
			if (!$(this).hasClass("trash")) {	// 非回收站
				
			    /** 如果是我的文档创建文件夹 那么companyId应该为‘user_’+userId */
				var companyId = documentCenter.getCompanyId();
				
				// 判断文件夹或文件是否被删除
				var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileDelete?callback=?';
				var dataInput = {"fileId":fileId, "companyId":companyId};
				var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
					function(data) {
							if(data.success==true || data.success == "true"){
								showBottomMsg((isFile=='0'?"文件夹":"文件")+"已被删除，无法打开！","3");
								return false;
							}else{
								if (isFile == '0') { // 文件夹
									showMainContentLeftFooter(true);
									documentCenter.setupFolderPath(fileId,"file-breadcrumbs","forwardFolder");
									documentCenter.getFileList(fileId);
								} else { // 文件
									var isupload = "0";
									if($("#uploadAllBtnDivId").css("top")!="0px"){
										isupload = "1";
									}
									changeUploadBtn("0");
									var isMyDocument = $('#file_id_'+fileId).hasClass("myDocument");
									var currentPageNum = documentCenter.getCurrentPageNum(); // 当前所在的页码
									var html = template('file_info_template', {"fileIdSeq": idSeq, "myDocument": isMyDocument?"myDocument":"", "currentPageNum":currentPageNum,"isupload":isupload,"isEnterSend":window.isEnterSend});
									$('#mainContentLeft4FileInfo').show();
									$('#mainContentLeft').hide();
									$('#mainContentLeft4FileInfo').html(html);
									documentCenter.showFileInfo(fileId, folderId);
									documentCenter.setupFolderPath(folderId,"zhexiangmuzhenshabi","forwardFolder");
								}
							}
				});
			}
			
		});
	});
	/**
	 * 刷新文件页面
	 */
	$("#main-container").on("click", "#file_preview_head_section .refreshFileInfo", function() {
		//判断点击的是文件夹还是文件
		var fileId = $(this).attr("fileid");
		var folderId = $(this).attr("folderId");
		var idSeq = $(this).attr("idseq");
		if($(this).closest("#filecontent").length>0){
			var searchType = $("#searchValHidId").attr("searchtype");
			documentCenter.showFileInfo(fileId, folderId,searchType);
		}else{
			documentCenter.showFileInfo(fileId, folderId);
			closeContentPanel();
			changeUploadBtn("0");
		}
	});
	//左侧弹出框的文件刷新
	$("#action-content-panel").off("click","#file_preview_head_section .refreshFileInfo").on("click", "#file_preview_head_section .refreshFileInfo", function() {
		//判断点击的是文件夹还是文件
		var fileId = $(this).attr("fileid");
		var folderId = $(this).attr("folderId");
		var idSeq = $(this).attr("idseq");
		changeUploadBtn("0");
		var searchType = $("#searchValHidId").attr("searchtype");
		documentCenter.showFileInfo(fileId, folderId,searchType);
	});
	
	// 鼠标经过文件中心的文件
	/*$("#mainContentLeft").on('mouseover mouseout', 'div.file-item', function(event) {
		if(event.type == "mouseover"){
		  //鼠标悬浮
			$("#content-list a.file_tool_menu").addClass("hidden");
			$(this).find('.file_tool_menu').removeClass("hidden");
		 }else if(event.type == "mouseout"){
		  //鼠标离开
			 $("#content-list a.file_tool_menu").addClass("hidden");
		 }
	});*/
	
	// 从详细页返回到list页
	$("#mainContentLeft4FileInfo").on('click', '#backToList', function() {
		if($(this).hasClass('fromChatNoAction')){ //从聊天窗口打开文件详情，返回时仅仅关闭文件面板
			closeFileInfo();
			return;
		}		
		if($(this).hasClass('mySubScribe')){
			getSubScribeMsgsFun(isSubScribeTag);
		}else{
		    documentCenter.getFileList($("#fileClassId").val(),null,null,$("#filePageNowId").val());
		}
		if($(this).attr("isupload")=="1"){
			changeUploadBtn("1");
		}
		closeFileInfo();
	});
	
	// 文件的更多操作
	/*$("#mainContentLeft").on('click', 'a.file_tool_menu', function(){
		$('#content-list div.file-item').removeClass('active');
		$(this).closest('div.file-item').addClass('active');
		
		var $item = $(this).closest('div.file-item')
		var item_id = $item.attr('id');
		var fileId = $(this).attr('target-id');
		var isFile = $(this).attr('isfile');
		var owner = $item.attr('owner');
		var owner_v1 = $item.attr('owner_v1');
		var folderId = $item.attr('folderId');
		var openlevel = $item.attr('openlevel');
		var isMember = $item.attr('isMember');	
		var isTrash = $item.hasClass("trash");
		var isMyDocument = $item.hasClass("myDocument");
		var version = $item.attr("version");
		var isV1Version = parseInt(version) == 1;  //当前版本就是第一版本（只存在一个版本文件）
		
		var isBatch = false;
		//标示私密文件权限
		var isDownload=$item.attr('isDownload');
		var isLook=$item.attr('isLook');
		var isOwner = (owner == g_userId) ? true : false;
		var isV1Owner = (owner_v1 == g_userId) ? true : false;
		
		var data = template('menu_file_action_items_template', {"isFile":isFile,"targetId":item_id,"isOwner":isOwner,"isV1Owner":isV1Owner,"folderId":folderId,"isMember":isMember,"isTrash":isTrash,'isBatch':isBatch,'openlevel':openlevel,"isMyDocument":isMyDocument,"isDownload":isDownload,"isLook":isLook, "isV1Version":isV1Version});
		var title = isFile=="1"?"文件选项":"文件夹选项";
		$(this).fymenu({"data": data,"header":title});
		return false;
	});*/
	
/*	$("#main-container").on('mouseover', '#file_openlevel', function(){
		var data = template('menu_file_action_openlevel_template');
		var title ="公开级别设置";
		$(this).fymenu({"data": data,"header":title,"menuId":"file_openlevel_menuid"});
		
		return false;
	});*/
	
	/**
	 * 文件下载
	 */
	$('#main-container').on('click', '#file_preview_head_section .file_download_label', function() {
		var $filePreviewContainer = $(this).closest("div.fileinfo");
		// 获取filePath
		var filePath = $filePreviewContainer.attr("filePath");
		var fileName = $filePreviewContainer.attr("fileTitle");
		var fileId = $filePreviewContainer.attr("fileid");
		documentCenter.downloadFile(filePath, fileName,fileId);
	});
	
	/**
	 * 保存到我的文档 wangwenshuo 20151216
	 */
	$('#main-container').on('click', '#file_preview_head_section .file_save_to_mydoc_label', function() {
		var $fileItem = $(this).closest("div.fileinfo");
		documentCenter.fileSaveToMyDoc($fileItem);
	});
	
	/**
	 * 文件item菜单-下载
	 */
	$('#main-container').on('click', '#file_download, #versions_list a.file-download', function() {
		var itemId = $(this).attr('target-id');
		var $fileItem = $("#"+itemId);
		var fileId = $fileItem.attr("fileid");
		var filePath = $fileItem.attr("filepath");
		var fileName = $fileItem.attr("filetitle");
		documentCenter.downloadFile(filePath, fileName,fileId);
	});
	/**
	 * 聊天框的下载操作
	 */
	$("#receiveMessageDiv").on('click', 'div.file_share_item div.file-download', function() {
		var groupFlag = "";
		if($('#receiverusername').attr('isgroup') =='1'){
			groupFlag = $('#receiverusername').attr('groupflag');
		}
		var $shareFileItem = $(this).closest('div.file_share_item');
		var filePath = $shareFileItem.attr("filepath");
		var fileName = $shareFileItem.attr("filetitle");
		var fileId = $shareFileItem.attr("fileid");
		documentCenter.downloadFile(filePath, fileName,fileId,groupFlag);
		return false;
	});
	
	/**消息中的下载操作liuwei20160215**/
	$("#userMessagesUl").on('click','div.file_share_item div.file-download', function(){
		var groupFlag = "";
		/*
		if($('#receiverusername').attr('isgroup') =='1'){
			groupFlag = $('#receiverusername').attr('groupflag');
		}
		*/
		groupFlag = $(this).parent().parent().prev('.msgGotoClassUrl').attr('groupflag');
		var $shareFileItem = $(this).closest('div.file_share_item');
		var filePath = $shareFileItem.attr("filepath");
		var fileName = $shareFileItem.attr("filetitle");
		var fileId = $shareFileItem.attr("fileid");
		documentCenter.downloadFile(filePath, fileName,fileId,groupFlag);
		return false;
	});
	
	/**
	 * 聊天框的浏览操作 我的文档分享出的文件  wangwenshuo 20151126
	 */
	$("#receiveMessageDiv").on('click', 'div.file_share_item div.file-preview', function() {
		var $shareFileItem = $(this).closest('div.file_share_item');
		var fileId = $shareFileItem.attr("fileid");
		var userId = $shareFileItem.attr("userid");
		var groupFlag = "";
		var isgroup = $('#receiverusername').attr('isgroup');
		if(isgroup =='1'){
			groupFlag = $('#receiverusername').attr('groupflag');
		}
		var baseUrl = window.onlinefilePath;
		var fileServerUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
		var companyId = g_companyId;
		if($(this).hasClass("myDocument"))  {
			companyId = "user_"+userId;
		}
		var url = baseUrl+'/rest/onlinefile_filesws/getFileInfo?callback=?';
		var dataInput = {"userName":window.userName,"fileId":fileId, "companyId":companyId,"toCompanyId":window.companyid,"userId":g_userId,"groupFlag":groupFlag};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(data) {
					if(data.file.isDelete=="1"){
						showBottomMsg("对不起，文件已被删除，无法浏览！","3");
						return false;
					}else if(data.isLook != "1"){
						showBottomMsg("对不起，分享已被撤销，您无此文件的浏览权限！","3");
						return false;
					}else{
						send2preview($shareFileItem.attr("id"));
					}
		});
		return false;
	});
	
	/**消息中的浏览操作liuwei20160215**/
	$("#userMessagesUl").on("click","div.file_share_item div.file-preview",function(){
		var $shareFileItem = $(this).closest('div.file_share_item');
		var fileId = $shareFileItem.attr("fileid");
		var userId = $shareFileItem.attr("userid");
		var groupFlag = $(this).parent().parent().prev('.msgGotoClassUrl').attr('groupflag');
		var baseUrl = window.onlinefilePath;
		var fileServerUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
		var companyId = g_companyId;
		if($(this).hasClass("myDocument"))  {
			companyId = "user_"+userId;
		}
		var url = baseUrl+'/rest/onlinefile_filesws/getFileInfo?callback=?';
		var dataInput = {"userName":window.userName,"fileId":fileId, "companyId":companyId,"toCompanyId":window.companyid,"userId":g_userId,"groupFlag":groupFlag};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(data) {
					if(data.file.isDelete=="1"){
						showBottomMsg("对不起，文件已被删除，无法浏览！","3");
						return false;
					}else if(data.isLook != "1"){
						showBottomMsg("对不起，分享已被撤销，您无此文件的浏览权限！","3");
						return false;
					}else{
						send2preview($shareFileItem.attr("id"));
					}
		});
		return false;
	});
	
	
	/**
	 * 聊天框的另存操作 我的文档分享出的文件  wangwenshuo 20151127
	 */
	$("#receiveMessageDiv").on('click', 'div.file_share_item div.file-save-as', function() {
		var $shareFileItem = $(this).closest('div.file_share_item');
		var fileId = $shareFileItem.attr("fileid");
		var fromUserId = $shareFileItem.attr("userid");
		var groupFlag = "";
		var isgroup = $('#receiverusername').attr('isgroup');
		if(isgroup =='1'){
			groupFlag = $('#receiverusername').attr('groupflag');
		}
		var baseUrl = window.onlinefilePath;
		var fileServerUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
		var companyId = "user_"+fromUserId;
		
		var url = baseUrl+'/rest/onlinefile_filesws/getFileInfo?callback=?';
		var dataInput = {"userName":window.userName,"fileId":fileId, "companyId":companyId,"toCompanyId":window.companyid,"userId":g_userId,"groupFlag":groupFlag};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(data) {
					if(data.file.isDelete=="1"){
						showBottomMsg("对不起，文件已被删除，无法保存！","3");
						return false;
					}else if(data.isDownload !="1"){
						showBottomMsg("对不起，分享已被撤销，您无此文件的保存权限！","3");
						return false;
					}else{
						//执行保存操作 默认保存到我的文档
						var fromCompanyId = "user_"+fromUserId;
						var toCompanyId = "user_"+window.userId;
						var toFolderIds = "2";  //默认我的文档根目录下
						$.post($.appClient.generateUrl({ESDocumentCenter:'fileShareToClass'},'x'),
						{fromCompanyId:fromCompanyId,fromFileId:fileId,toCompanyId:toCompanyId,toFolderIds:toFolderIds,toFolderNames:'我的文档',userId:window.userId}, function(json){
							var json = eval('('+json+')');
							if(json.haveSuccess){
								showBottomMsg("保存到我的文档成功！","1");
							}else{
								showBottomMsg("保存到我的文档失败！","3");
							}
						});
					}
		});
		return false;
	});
	// 聊天记录搜索框
	$("#showflyingchatSearch").click(function(){
		var $chatSearch = $("#flyingchatSearchInputGroup");
		$("#chatSearchInput").val("");
		if ($chatSearch.hasClass("hidden")) {
			$chatSearch.removeClass("hidden");
			$("#chatSearchInput").focus();
		} else {
			$chatSearch.addClass("hidden");
		}
		
		$("#chatSearchTotal").html("");
		
		$('#chatSearchInput').keydown(function (event) {
			var e = event || window.event;
			var keyCode = e.which || e.keyCode;
			if (keyCode == 13) {
				if ($(this).val()=="") {
					$("#chatSearchTotal").hide();
					$("#flyingchatSearchBackBtn").click();
				} else {
					$("#chatSearchTotal").show();
				}
				$("#chatSearchTotal").html("");
				flyingchatSearchBtn();
				return false;
			}
		});
		
	});
	
	// longjunhao 20151027 高亮处理
	function addHighLight(content, keyword) {
		keyword = decodeURI($.trim(keyword), "utf-8");
		var checkStrStart = '<div class="file_title"><a class="file_preview_link link" name="fileName">';
		var checkStrEnd = '</a></div>';
		var index = content.indexOf(checkStrStart);
		// 判断是否是文件框
		if (index > -1) {
			// 获取文件框中的文件标题
			var fileTitle = content.substr(index+checkStrStart.length);
			fileTitle = fileTitle.substr(0, fileTitle.indexOf(checkStrEnd));
			newFileTitle = replaceNoChangeMatchWords(fileTitle,keyword,true);
			//if(newFileTitle==fileTitle) return false;
			content = content.replace(checkStrStart+fileTitle+checkStrEnd, checkStrStart+newFileTitle+checkStrEnd);
			
		} else {
			var oldContent = content;
			content = replaceNoChangeMatchWords(content,keyword,true);
			//if(content==oldContent) return false;
		}
		return content;
	}
	function replaceNoChangeMatchWords(replaceBase,replaceWith,ignoreCase){
		if(typeof replaceWith != "string") return replaceBase;  
		var regReplaceWith = new RegExp(replaceWith, (ignoreCase ? "gi" : "g"));
		if(ignoreCase){
			var arr = replaceBase.match(regReplaceWith);
			arr = arr||[];
			var arrNoRepeat = [];
			arr.forEach(function(i){
				if($.inArray(i,arrNoRepeat)==-1){
				 var regi = new RegExp(i, "g"); 
				 replaceBase = replaceBase.replace(regi,'<span class="keywordHighLight">'+i+'</span>');
				 arrNoRepeat.push(i);
			   }
			}); 
			
		}else{
			replaceBase = replaceBase.replace(regReplaceWith,'<span class="keywordHighLight">'+replaceWith+'</span>');
		}
		return replaceBase;
	}
	/**
	 * 聊天搜索的返回
	 */
	$("#flyingchatSearchBackBtn").click(function() {
		$("#chatSearchTotal").html("");
		$("#flyingchatSearchInputGroup").addClass("hidden");
		$(".returnChat").click();
	});
	
	/**
	 * 聊天框的搜索
	 * **/
	function flyingchatSearchBtn(){
//		$("#flyingchatSearchInputGroup").addClass("hidden");
		var doc = $("#chatSearchInput");
		var content = doc.val();
		if($.trim(content) == ""){
//			showBottomMsg("您没有输入检索词，不能进行检索操作！", 2);
			return ;
		}
		var receiver = $('#receiverusername').attr("receiver");
		var isGroup = $('#receiverusername').attr("isGroup");
		var skip  = 0;
		//var keyword = encodeURI($.trim(content), "utf-8");
		var keyword = $.trim(content);
		var page = 1;
		if(keyword == null){
			fyBotKeyword = "" ;
		} else {
			fyBotKeyword = keyword ;
		}
		var joindate = "" ;
		var jointime = "" ;
		if(isGroup==1){
			joindate = $('#receiverusername').attr("joindate");
			jointime = $('#receiverusername').attr("jointime");
		}
		var url =  $.appClient.generateUrl({ESChat : 'getHistoryMessage'},'x');
		$.ajax({
			url:url,
			type: "POST",
			data:{companyId:$('#flyingchat').attr('companyId'), receiver:receiver, username:$('#flyingchat').attr('username'), isGroup:isGroup, limit:20, page:page, skip:skip, keyword:fyBotKeyword, joindate:joindate, jointime:jointime},
			dataType:"json",
			success:function(json){
				var msgs = json.msgs ;
				var count = msgs.length;
				if(count>0){
					var preDate = "" ;
					var messages = "" ;
					var msgCount = msgs.length ;
					for(var i = 0 ; i <msgCount ;i ++){
						// 发送消息的样式
						if(preDate != msgs[i].DATE){
							messages += createDateHr(msgs[i].DATE);
							preDate = msgs[i].DATE ;
						}
						var content = decodeURIComponent(msgs[i].CONTENT, "utf-8") ;
						content = content.replace(/%20/g," ");
						// longjunhao 20151027 高亮处理
						content = addHighLight(content, fyBotKeyword);
						if(!content) continue;
						if(fyBotKeyword == ""){
							if(i==msgCount-1){
								if('fyBot'==msgs[i].FROMUSER){
									if(content==fyBotSets["S0"]){
										content += "<a id=\"fyBotNext_0\" onclick='$.WebIM.messageClickFun(\"skip_0\")'>跳过</a>" ; ;
										$('#receiverusername').attr("fybotstep", "0") ;
									} else if(content==fyBotSets["S1"]){
										content += "<a id=\"fyBotNext_1\" onclick='$.WebIM.messageClickFun(\"skip_1\")'>跳过</a>" ; ;
										$('#receiverusername').attr("fybotstep", "1") ;
									} else if(content==fyBotSets["S2"]){
										content += "<a id=\"fyBotNext_2\" onclick='$.WebIM.messageClickFun(\"skip_2\")'>跳过</a>" ; ;
										$('#receiverusername').attr("fybotstep", "2") ;
									} else if(content==fyBotSets["S3"]){
										content += "<a id=\"fyBotNext_3\" onclick='$.WebIM.messageClickFun(\"skip_3\")'>跳过</a>" ; ;
										$('#receiverusername').attr("fybotstep", "3") ;
									} else if(content==fyBotSets["S4"]){
										$('#receiverusername').attr("fybotstep", "4") ;
									} else {
										$('#receiverusername').attr("fybotstep", "4") ;
									}
								} else {
									$('#receiverusername').attr("fybotstep", "4") ;
								}
							}
						}
						var userpic = msgs[i].FROMUSER == 'fyBot'?defaultPortrait:null ;
						// 向接收信息区域写入发送的数据
						if(msgs[i].FROMUSER == $('#flyingchat').attr('username')){
							messages += receiveMessageTpl(msgs[i].FROMUSER, "", content, true, msgs[i].DATE, msgs[i].TIME, userpic, isGroup==1?msgs[i].FROMCNNAME:null,msgs[i].ID);
						} else {
							messages += receiveMessageTpl(msgs[i].FROMUSER, "", content, false, msgs[i].DATE, msgs[i].TIME, userpic, isGroup==1?msgs[i].FROMCNNAME:$('#receiverusername').attr("groupName"),msgs[i].ID);
						}
					}
					/**
					if(preDate != _opts.getDate()){
						messages += _opts.createDateHr(_opts.getDate());
					}  **/
					if(fyBotKeyword == ""){
						$(messages).insertAfter($("#msgMoreButton"));
						$("#receiveMessageDiv").scrollTop($(".im-chat-list").height());
					} else {
						$("#chatSearchTotal").html("共" + count + "条");
						$(".im-chat-list").html('<div id="msgMoreButton" page="0"><div class="msgMore">更多历史消息...</div></div>');
						$(messages).insertAfter($("#msgMoreButton"));
						$(".im-chat-list").append('<div id="returnChatMain"><div class="returnChat">返回聊天记录界面</div></div>') ;
						$("#receiveMessageDiv").scrollTop(0);
					}
					$('#receiveMessageDiv').perfectScrollbar('update');
				} else {
					if(fyBotKeyword == ""){
						if('fyBot' == $('#receiverusername').attr('receiver'))_opts.fyBotSessionStart() ;
					} else {
						$("#chatSearchTotal").html("共" + count + "条");
					    $(".im-chat-list").html('<div class="noSearchResult">没有找到符合条件的记录</div><div id="returnChatMain"><div class="returnChat">返回聊天记录界面</div></div>') ;

					}
					$("#receiveMessageDiv").scrollTop(0);
					$('#receiveMessageDiv').perfectScrollbar('update');
				}
				$("#msgMoreButton").attr("page", page) ;
				$("#msgMoreButton").attr("keyWord", fyBotKeyword) ;
				if(count<20){
					$("#msgMoreButton").css("display", "none") ;
				}else{
					$("#msgMoreButton").css("display", "block") ;
				}
			
			},
			error:function(){
				showBottomMsg("请重试",2);
			}
		});

	}
	
	
	function createDateHr(date) {
		return "<div class='datediv'><hr role='separator' aria-hidden='true'><div class='day_divider_label' aria-label='February ninth, twenty fifteen'>"+date+"</div></div>" ;
	}
	
	
	function receiveMessageTpl(userName, styleTpl, content, flag, date, time, userpic, fromCnName,ID) {
		var mongoID = ID=="undefined" ? "":ID;
		var userCls = flag ? "im-me" : "im-others";
		userpic = $("#itemuser-"+hex_md5(userName.replace("\\40", "@"))).attr("itemuserportrait") ;
		return [
			'<div class="im-item ', userCls, '"><div class="im-message clearfix"><div class="im-user-area"><img width="32" height="32" class="useritempic',(flag?"":" otherusermsg"),'" itemuser="',userName,'" fullname="',fromCnName,'" src="', userpic, '" style="',(userpic==""?"opacity: 0;":""),'"></div><div class="im-message-detail">',
			'<table class="im-message-table" border="0" cellSpacing="0" cellPadding="0">',
			'<tr><td class="lt"></td><td class="tt"></td><td class="rt"></td></tr>',
			'<tr><td class="lm">', (userCls=='im-me'?'':'<span/>'), '</td><td class="mm">',
			'<div class="im-message-title"><p class="im-message-owner"><span class="im-txt-bold">', (fromCnName==null?$("#current_user_name").html():fromCnName), '</span></p><span class="im-send-time">', (time==null?_opts.getDatetime():time), '</span></div>',
			'<div class="im-message-content" msgid="'+mongoID+'" date="'+(date==null?_opts.getDate():date)+' '+(time==null?_opts.getDatetime():time)+'">', content, '</div>',
			'</td><td class="rm">', (userCls=='im-me'?'<span/>':''), '</td></tr>',
			'<tr><td class="lb"></td><td class="bm"></td><td class="rb"></td></tr>',
			'</table>',
			'</div></div></div>'
		].join("");
	}
	
	/**
	 * 聊天框的撤销文件分享
	 */
	$("#receiveMessageDiv").on('click', 'div.file_share_item div.file-backout', function() {
		var $shareFileItem = $(this).closest('div.file_share_item');
		var msgDateStr = $shareFileItem.parent().attr("date");
		var msgDate = new Date(msgDateStr.replace(/-/g,"/")).getTime();
		var newDate = new Date().getTime();
		if((newDate-msgDate)/(1000*60*60*24)>1){ //超过24小时
			showBottomMsg("分享超过24小时，不能进行撤销操作","2");
			return;
		}
		
		var fileId = $shareFileItem.attr("fileId");
		var userId = $shareFileItem.attr("userid");
		var accessRight = $shareFileItem.attr("accessRight");
		var isMyDocument = $(this).hasClass("myDocument");
		var companyId = g_companyId;
		if(isMyDocument)  {
			companyId = "user_"+userId;
		}
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileDelete?callback=?';
		var dataInput = {"fileId":fileId, "companyId":companyId};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(data) {
					if(data.success==true || data.success == "true"){
						showBottomMsg("对不起，文件已被删除，无法撤销！","3");
						return false;
					}else{
						var $receiverusername = $("#receiverusername");
						var receiverId = $receiverusername.attr("receiver");
						var isGroup = $receiverusername.attr("isGroup");
						if(isMyDocument){
							documentCenter.backoutFile(fileId, receiverId, isGroup, accessRight ,companyId);
						}else{
							documentCenter.backoutFile(fileId, receiverId, isGroup, accessRight);
						}
						return false;
					}
		});
		
	});
	
	/**
	 * 聊天框的查看文件
	 */
	$("#receiveMessageDiv").on('click', 'div.file_share_item div.file-open', function() {
		var $shareFileItem = $(this).closest('div.file_share_item');
		var fileId = $shareFileItem.attr("fileId");
		
		//聊天窗口查看分享出来的文档
		var groupFlag = ""; 
		//判断是否是分类内分享出来的文件 并且分享对象是群组
		if($(this).attr("name")=="sharefromclass" && $('#receiverusername').attr('isgroup') =='1'){
			groupFlag = $('#receiverusername').attr('groupflag');
		}
		
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileDelete?callback=?';
		var dataInput = {"fileId":fileId, "companyId":g_companyId};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(data) {
					if(data.success==true || data.success == "true"){
						showBottomMsg("对不起，文件已被删除，无法查看！","3");
						return false;
					}else{
						var folderId = $shareFileItem.attr("folderId");
						var fileIdSeq = $shareFileItem.attr("idseq");
						var isupload = "0";
						if($("#uploadAllBtnDivId").css("top")!="0px"){
							 isupload = "1";
						}
						changeUploadBtn("0");
						var html = template('file_info_template', {"fileIdSeq": fileIdSeq, "myDocument": folderId=='0'?"myDocument":"","isupload":isupload});
//						$('#mainContentLeft').html(html);
						$('#mainContentLeft').hide();
						$('#mainContentLeft4FileInfo').show();
						$('#mainContentLeft4FileInfo').html(html);
						documentCenter.showFileInfo(fileId, folderId, null,null,groupFlag,false);
						
						//为了返回按钮正常使用
						$("#mainContentLeft4FileInfo #backToList").addClass('fromChatNoAction');
						return false;
					}
		});
		
	});
	
	/**
	 * 聊天框的打开文件夹
	 */
	$("#receiveMessageDiv").on('click', 'div.file_share_item div.file-open-dir', function() {
		var $shareFileItem = $(this).closest('div.file_share_item');
		var fileIdSeq = $shareFileItem.attr("idseq");
		var fileid = $shareFileItem.attr("fileid");
		jumpToFolderByFileIdSeq(fileIdSeq,fileid);
		return false;
	});
	
	/**消息中打开文件夹liuwei20160215**/
	$("#userMessagesUl").on('click', 'div.file_share_item div.file-open-dir', function() {
		var $shareFileItem = $(this).closest('div.file_share_item');
		var fileIdSeq = $shareFileItem.attr("idseq");
		var fileid = $shareFileItem.attr("fileid");
		jumpToFolderByFileIdSeq(fileIdSeq,fileid);
		return false;
	});
	
	/**分享文件消息中查看文件liuwei20160215**/
	$("#userMessagesUl").on('click','div.file_share_item div.file-open', function(){
		var $shareFileItem = $(this).closest('div.file_share_item');
		var fileId = $shareFileItem.attr("fileId");
		//聊天窗口查看分享出来的文档
		var groupFlag = $(this).parent().parent().prev('.msgGotoClassUrl').attr('groupflag');
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileDelete?callback=?';
		var dataInput = {"fileId":fileId, "companyId":g_companyId};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(data) {
					if(data.success==true || data.success == "true"){
						showBottomMsg("对不起，文件已被删除，无法查看！","3");
						return false;
					}else{
						var folderId = $shareFileItem.attr("folderId");
						var fileIdSeq = $shareFileItem.attr("idseq");
						var isupload = "0";
						if($("#uploadAllBtnDivId").css("top")!="0px"){
							 isupload = "1";
						}
						changeUploadBtn("0");
						var html = template('file_info_template', {"fileIdSeq": fileIdSeq, "myDocument": folderId=='0'?"myDocument":"","isupload":isupload});
//						$('#mainContentLeft').html(html);
						$('#mainContentLeft').hide();
						$('#mainContentLeft4FileInfo').show();
						$('#mainContentLeft4FileInfo').html(html);
						documentCenter.showFileInfo(fileId, folderId, null,null,groupFlag,false);
						
						//为了返回按钮正常使用
						$("#mainContentLeft4FileInfo #backToList").addClass('fromChatNoAction');
						return false;
					}
		});
		
	});
	
	/**分享文件消息中下载文件 liuwei20160215**/
	
	
	
	
	/**
	 * 文件item菜单-设为私密
	 */
	$('#main-container').on('click', '#file_unshare', function() {
		var itemId = $(this).attr('target-id');
		var $fileItem = $("#"+itemId);
		var filetitle = $fileItem.attr("filetitle");
		var fileName = $fileItem.attr("fileName");
		var fileType = $fileItem.attr("fileType");
		var version = $fileItem.attr("version");
		var idSeq = $fileItem.attr("idseq");
		
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileNameExist?callback=?';
		var dataInput = {"companyId":g_companyId, "classId":0, "fileName":filetitle};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(rst) {
					var newFileName = rst.newFileName;
					var isExist = rst.isExist;  //存在同名文件true
					var html = template('file_unshare_template', rst);
					$.dialog({
						title : "设为私密",
						content : html,
						width: 400,
						okValue : '确定',
						cancelValue : '取消',
						cancel : true,
						ok : function() {
							var unshareType = $("#fileUnshareRadio input:radio:checked").val();  //0更新版本   1重命名
							var json = {file:{fileName:fileName,type:fileType,version:version,isFile:"1",idSeq:idSeq},creator:{}};
							documentCenter.unshareFile($fileItem.attr("folderid"), $fileItem.attr("fileid"), 
									"fileItem", $fileItem.hasClass("myDocument"), json , isExist, unshareType, newFileName);
						}
					}).showModal();
		});
		
	});
	
	//xiewenda 私密设置
	$('#main-container').on('click', "#file_openlevel_set li", function() {
		var itemId = $(this).attr('target-id');
		var openlevel = $(this).attr("value");
		var $fileItem = $("#"+itemId);
		var filetitle = $fileItem.attr("filetitle");
		var classId = $fileItem.attr("folderid");
		var fileId = $fileItem.attr("fileid");
		var fileName = $fileItem.attr("filename");
		var fileType = $fileItem.attr("filetype");
		var version = $fileItem.attr("version");
		var idSeq = $fileItem.attr("idseq");
		//var openlevel = $fileItem.attr("openlevel");
		var flushType = "";
		if($fileItem.hasClass("myDocument")){
			flushType = "myDocument";
		}else if($fileItem.hasClass("fileinfo")){
			flushType = "fileinfo";
			if($fileItem.closest("#filecontent").length>0){
				flushType = "fileinfosearch";
			}
		}else if($fileItem.hasClass("file-item")){
			flushType = "file-item";
		}
		var json = {file:{fileName:fileName,type:fileType,version:version,isFile:"1",idSeq:idSeq},creator:{}};
		documentCenter.setFileOpenlevel(fileId,filetitle,classId,openlevel,flushType,json);
		menuClose();
		return false;
	});
	/**
	 * 文件预览的设为私密
	 */
	$('#main-container').on('click', '#file_preview_head_section a.file-unshare', function() {
		var $fileItem = $(this).closest("div.fileinfo");
		var filetitle = $fileItem.attr("filetitle");
		var fileName = $fileItem.attr("fileName");
		var fileType = $fileItem.attr("fileType");
		var version = $fileItem.attr("version");
		var idSeq = $fileItem.attr("idseq");
		
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileNameExist?callback=?';
		var dataInput = {"companyId":g_companyId, "classId":0, "fileName":filetitle};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(rst) {
					var newFileName = rst.newFileName;
					var isExist = rst.isExist;  //存在同名文件true
					var html = template('file_unshare_template', rst);
					//$("#mask").show(); 
					$.dialog({
						title : "设为私密",
						content : html,
						width: 400,
						okValue : '确定',
						cancelValue : '取消',
						cancel : true,
						ok : function() {
							var unshareType = $("#fileUnshareRadio input:radio:checked").val();  //0更新版本   1重命名
							var json = {file:{fileName:fileName,type:fileType,version:version,isFile:"1",idSeq:idSeq},creator:{}};
							documentCenter.unshareFile($fileItem.attr("folderid"), $fileItem.attr("fileid"), 
									"fileInfo", $fileItem.hasClass("myDocument"), json , isExist, unshareType, newFileName);
						}
					}).showModal();
		});
	});
	
	/**
	 * 文件预览的分享到
	 */
	$('#main-container').on('click', '#file_preview_head_section .file-share-to', function() {
		var $fileItem = $(this).closest("div.fileinfo");
		var itemId = $fileItem.attr("id");
		var file_share_to_html = template("file_share_to_template", {"itemId":itemId});
		$.fywindow({
	    	title: '分享到分类',
			width: 600,
		    content: file_share_to_html
	    });
		shareToFolderBind();
		return false;
	});
	
	/**
	 * 文件item菜单-分享到
	 */
	$('#main-container').on('click', '#file_share_to', function() {
		var itemId = $(this).attr('target-id');
		var $fileItem = $("#"+itemId);
		var itemId = $fileItem.attr("id");
		var file_share_to_html = template("file_share_to_template", {"itemId":itemId});
		$.fywindow({
	    	title: '分享到分类',
			width: 600,
		    content: file_share_to_html
	    });
		shareToFolderBind() ;
		$("#menu_panel").remove(); //手动关闭右键菜单
		return false ;
	});
	
	/**
	 * 文件item菜单-保存到我的文档
	 */
	$('#main-container').on('click', '#file_save_to_mydoc', function() {
		var itemId = $(this).attr('target-id');
		var $fileItem = $("#"+itemId);
		documentCenter.fileSaveToMyDoc($fileItem);
	});
	
	/**
	 * wangwenshuo 20151020 复制到 点击复制后
	 */
	$('#main-container').on('click', '#file_copy_to', function() {
		var itemId = $(this).attr('target-id');
		var $fileItem = $("#"+itemId);
		copyToFolderAction($fileItem);
		return false;
	});
	
	/**
	 * 文件item菜单-发送到讨论
	 */
	$('#main-container').on('click', '#file_share, #versions_list a.file-share', function() {
		var itemId = $(this).attr('target-id');
		var $fileItem = $("#"+itemId);
		
		//增加发送到讨论的提示 liumingchao
		var isgroup = $('#receiverusername').attr('isgroup');
		var name = $('#receiverusername').attr("groupName");
		if(name=='fyBot'){
			showBottomMsg("机器人无法接收您分享的文档哦，请分享给您的其他小伙伴吧！","3");
			return;
		}
//      xiewenda 将下边三行代码移到isgroup==1时获取
//		var groupFlag = $('#receiverusername').attr('groupflag');
//		var hexname = hex_md5(groupFlag);
//		var obj = $('#newmessage'+hexname) ;
		var content="";
		if(isgroup =='0'){
			content = "确定要将文件发送到用户【 "+name+"】的聊天窗口吗？"
		}else if(isgroup =='1'){
			var groupFlag = $('#receiverusername').attr('groupflag');
			var hexname = hex_md5(groupFlag);
			var obj = $('#newmessage'+hexname) ;
			if(obj.attr("class")=="classnewmessage"){
				content = "确定要将文件发送到分类【"+name+"】的聊天窗口吗？"
			}else if(obj.attr("class")=="newmessage"){
				content = "确定要将文件发送到分组【"+name+"】的聊天窗口吗？"
			}
		}
		
		var html = [
			'<div id="fileAccessRight" class="alert alert-warning" style="margin:10px 0px -5px 0px;">',
			'	<span class="tips"><strong>请配置【'+name+'】拥有的文件权限</strong></span>',
			'	<div class="options">',
			'		<div class="radio">',
			'		    <label>',
			'		      <input type="radio" name="optionsRadios" value="1" checked> 浏览权限',
			'		    </label>',
			'		</div>',
			'		<div class="radio">',
			'		    <label>',
			'		      <input type="radio" name="optionsRadios" value="3"> 浏览、下载权限',
			'		    </label>',
			'		</div>',
			'	</div>',
			'</div>'
		].join('');
		
		$.dialog({
			content :content+html,
			quickClose: true,
			modal:true,
			okValue : '确定',
			ok : true,
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				var accessRight = $("#fileAccessRight input[name='optionsRadios']:checked").val();
				$fileItem.attr("accessright",accessRight);
				shareAction($fileItem);
			}
		}).show();
		
	});
	
	/**
	 * 删除文件的历史版本 
	 *		wangwenshuo 20160105  详情页面最新版本删除同样使用本方法
	 */
	$('#main-container').on('click', '#versions_list a.file-deletehistory,#file_preview_head_section span.file_delete_label', function() {
		var itemId = $(this).attr('target-id');
		$fileItem = $("#"+itemId);
		var fileId = $fileItem.attr('fileid');
		var folderId = $fileItem.attr('folderid');
		var idSeq = $fileItem.attr("idseq");
		/*wangwenshuo  20151228 添加我的文档/分类文档 历史版本 兼容支持*/
		var companyId = documentCenter.getCompanyId();
		var isMyDocument = documentCenter.isMyDocument();
		
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFileInfo?callback=?';
		var dataInput = {"userName":window.userName,"fileId":fileId, "companyId":companyId,"userId":g_userId};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(data) {
					if(data.file.isDelete=="1"){
						showBottomMsg("对不起，文件已被删除！","3");
						return false;
					}else if(data.hasRight == false || data.hasRight == "false"){
						showBottomMsg("对不起，您无此文件的操作权限，请到文件详细信息页进行申请！","3");
						return false;
					}else{
						$.dialog({
							title : "删除文件",
							content: "当前版本文件删除后会放置在回收站，您可以在回收站选择恢复或者彻底删除。",
							okValue: '确定',
					    	ok: function() {
					    		documentCenter.deleteHistoryFile(folderId, fileId);
					    		// 发送群组聊天消息提示
								if(!isMyDocument){
									var fileName = $fileItem.attr("fileName");
									var fileType = $fileItem.attr("fileType");
									var version = $fileItem.attr("version");
									var json = {file:{fileName:fileName,type:fileType,version:version,isFile:"1"},creator:{}};
									$.WebIM.sendFileMsgNotAction(data.file.groupFlag, json, "deleteFile");
								}
					    		return true;
					    	},
					    	cancelValue:'取消',
					    	cancel: true
					    }).show();
					}
		});
		return false;
	});
	
	/**
	 * 文件item菜单-文件夹删除
	 */
	$('#main-container').on('click', '#folder_delete', function() {
		var itemId = $(this).attr('target-id');
		$fileItem = $("#"+itemId);
		var parentFolderId = $fileItem.attr("folderId");
		var folderId = $fileItem.attr("fileId");
		var idSeq = $fileItem.attr("idseq");
		var className=documentCenter.getClassIdByIdSeq("ClassNameFromEditClassName"); 
		var firstClassId = documentCenter.getClassIdByIdSeq(idSeq);
		 /** 如果是我的文档创建文件夹 那么companyId应该为‘user_’+userId */
		var companyId = documentCenter.getCompanyId();
		$.dialog({
			title: '删除文件夹',
			content : '文件夹及其文件删除后会放在回收站，可以在回收站选择恢复或者彻底删除。',
			okValue : '确定',
			cancelValue : '取消',
			cancel : true,
			ok : function() {
					$.post($.appClient.generateUrl({ESDocumentClass : 'deleteClassById'}, 'x'), 
							{companyId:companyId,classId:firstClassId,folderId:folderId,userId:g_userId}, function(res) {
						var json = $.parseJSON(res);
						if(json.success == 'true'){
							showMsg("删除文件夹成功！");
							var page = documentCenter.getCurrentPageNum();
							documentCenter.getFileList(parentFolderId,"","",page);
							
							if(!documentCenter.isMyDocument()){
								// 发送群组聊天消息
								var fileName = $fileItem.attr("fileName");
								var groupFlag = json.groupFlag;
								var GROUPNAME = json.GROUPNAME;
								var json = {file:{fileName:fileName,className:GROUPNAME,isFile:"0"},creator:{}};
								$.WebIM.sendFileMsgNotAction(groupFlag, json,"deleteFolder");
							}
							
						}else if(json.success == "havechildren"){
							showMsg("该分类下存在子文件夹或文件！");
						}else if(json.success == "nocreator"){
							showMsg("您无权删除此子文件夹！", "2");
						}
					});
			}
		}).showModal();
	});
	
	/**
	 * 文件item菜单-文件删除
	 */
	$('#main-container').on('click', '#file_delete', function(){
		var itemId = $(this).attr('target-id');
		$fileItem = $("#"+itemId);
		var fileId = $fileItem.attr('fileid');
		var folderId = $fileItem.attr('folderid');
		var idSeq = $fileItem.attr("idseq");
		 /** 如果是我的文档创建文件夹 那么companyId应该为‘user_’+userId */
		var companyId = documentCenter.getCompanyId();
		var isMyDocument = documentCenter.isMyDocument();
		
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFileInfo?callback=?';
		var dataInput = {"userName":window.userName,"fileId":fileId, "companyId":companyId,"userId":g_userId};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(data) {
					if(data.file.isDelete=="1"){
						showBottomMsg("对不起，文件已被删除！","3");
						return false;
					}else if(data.hasRight == false || data.hasRight == "false"){
						//showBottomMsg("对不起，您无此文件的操作权限，请到文件详细信息页进行申请！","3");
						showMessage("对不起，您无此文件的操作权限，请到文件详细信息页进行申请！","3");
						return false;
					}else{
						$.dialog({
							title : "删除文件",
							content: "文件删除后会放在回收站，可以在回收站选择恢复或者彻底删除。",
							okValue: '确定',
					    	ok: function() {
					    		documentCenter.deleteFile(folderId, fileId);
					    		// 发送群组聊天消息提示
								var fileName = $fileItem.attr("fileName");
								var fileType = $fileItem.attr("fileType");
								var version = $fileItem.attr("version");
								var json = {file:{fileName:fileName,type:fileType,version:version,isFile:"1"},creator:{}};
								if(!isMyDocument){
									$.WebIM.sendFileMsgNotAction(data.file.groupFlag, json, "deleteFile");
								}
					    		return true;
					    	},
					    	cancelValue:'取消',
					    	cancel: true
					    }).showModal();
					}
		});
	});
	
	/**
	 * lujixiang 20150806 批量删除文件夹和文件
	 */
	$('#main-container').on('click', '#folder_file_delete_batch', function(){
		
		var activeItems = $('#content-list div.file-item.active');	// 被选中文件夹和文件
		var foldsId = '' ;	// 被选中的文件夹ids
		var filesId = '' ;  // 被选中的文件ids
		var groupFlag = ''; // 群组
		
		/** 获取选中的文件夹和文件id **/
		activeItems.each(function(){
			var item = $(this);
			if('0' == item.attr('isfile')){
				foldsId = foldsId + item.attr('fileid') + ',';
			}else{
				filesId = filesId + item.attr('fileid') + ',';
			}
			
		});
		
		// 获取当前父文件夹
		var parentFolderId = activeItems.eq(0).attr('folderId');
		
		/** 如果是我的文档创建文件夹 那么companyId应该为‘user_’+userId */
		var companyId = documentCenter.getCompanyId();
		var isMyDocument = documentCenter.isMyDocument();
		
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFilesAndFoldsCanNotDelete?callback=?';
		var dataInput = {"userName":window.userName,"fileIds":filesId,"foldIds":foldsId, "companyId":companyId,"userId":g_userId};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
					   function(data) {
					
					      var hasDeleteFoldsId = data.hasDeleteFoldsId ;	//	无法删除的文件夹（该文件夹已经被删除）
					      var hasNoRightFoldsId =  data.hasNoRightFoldsId ;	// 	无法删除的文件夹（只要文件夹下存在无权限的文档,那么整个文件夹都不能删除）
					      var hasDeleteFilesId = data.hasDeleteFilesId ;	//	无法删除的文件（该文件已经被删除）
					      var hasNoRightFilesId = data.hasNoRightFilesId ;	//	无法删除的文件（没有权限）
					      var hasMoreVersion = data.hasMoreVersion ;	    //	是否存在多个版本（没有权限）
					      groupFlag = data.groupFlag ;
					      
					      var canDelete = true ;	// 是否能删除 
					      
					      /** 获取已经被删除的文件夹 **/
					      var deleteFoldMsg = ''
					      if(null != hasDeleteFoldsId && '' != hasDeleteFoldsId ){
					    	  canDelete = false;
					    	  
					    	  var hasDeleteFoldsArr = hasDeleteFoldsId.split(',');
					    	  for(var tempIdIndex in hasDeleteFoldsArr){
					    		  var tempId = hasDeleteFoldsArr[tempIdIndex] ;
					    		  activeItems.each(function(){
					    			  	var item = $(this);
					    			  	if(tempId == item.attr('fileid')){
					    			  		deleteFoldMsg = ('' == deleteFoldMsg ? '' : deleteFoldMsg + ',') + item.attr('filename');
					    			  		return false ;
					    			  	}
					    			  
					    			});
					    	  }
					      }
					      
					      /** 获取无权限删除的文件夹 **/
					      var noRightFoldMsg = ''
					      if(!isMyDocument && null != hasNoRightFoldsId && '' != hasNoRightFoldsId ){
					    	  canDelete = false;
					    	  
					    	  var hasNoRightFoldsArr = hasNoRightFoldsId.split(',');
					    	  for(var tempIdIndex in hasNoRightFoldsArr){
					    		  var tempId = hasNoRightFoldsArr[tempIdIndex] ;
					    		  activeItems.each(function(){
					    			  	var item = $(this);
					    			  	if(tempId == item.attr('fileid')){
					    			  		noRightFoldMsg = ('' == noRightFoldMsg ? '' : noRightFoldMsg + ',') + item.attr('filename');
					    			  		return false ;
					    			  	}
					    			  
					    			});
					    	  }
					      }
					      
					      /** 获取已经被删除的文件 **/
					      var deleteFileMsg = ''
					      if(null != hasDeleteFilesId && '' != hasDeleteFilesId ){
					    	  canDelete = false;
					    	  
					    	  var hasDeleteFilesArr = hasDeleteFilesId.split(',');
					    	  for(var tempIdIndex in hasDeleteFilesArr){
					    		  var tempId = hasDeleteFilesArr[tempIdIndex] ;
					    		  activeItems.each(function(){
					    			  	var item = $(this);
					    			  	if(tempId == item.attr('fileid')){
					    			  		deleteFileMsg = ('' == deleteFileMsg ? '' : deleteFileMsg + ',') + item.attr('filename');
					    			  		return false ;
					    			  	}
					    			  
					    			});
					    	  }
					      }
					      
					      /** 获取无权限删除的文件 **/
					      var noRightFileMsg = ''
					      if(!isMyDocument && null != hasNoRightFilesId && '' != hasNoRightFilesId ){
					    	  canDelete = false;
					    	  
					    	  var hasNoRightFilesArr = hasNoRightFilesId.split(',');
					    	  for(var tempIdIndex in hasNoRightFilesArr){
					    		  var tempId = hasNoRightFilesArr[tempIdIndex] ;
					    		  activeItems.each(function(){
					    			  	var item = $(this);
					    			  	if(tempId == item.attr('fileid')){
					    			  		noRightFileMsg = ('' == noRightFileMsg ? '' : noRightFileMsg + ',') + item.attr('filename');
					    			  		return false ;
					    			  	}
					    			  
					    			});
					    	  }
					      }
					      /**liuwei 判断是否有多个版本的文件**/
					      	  var hasMoreVersionMsg = '';
						      if(null != hasMoreVersion && '' != hasMoreVersion ){
						    	  canDelete = false;
						    	  var hasMoreVersionArr = hasMoreVersion.split(',');
						    	  for(var tempIdIndex in hasMoreVersionArr){
						    		  var tempId = hasMoreVersionArr[tempIdIndex] ;
						    		  activeItems.each(function(){
						    			  	var item = $(this);
						    			  	if(tempId == item.attr('fileid')){
						    			  		hasMoreVersionMsg = ('' == hasMoreVersionMsg ? '' : hasMoreVersionMsg + ',') + item.attr('filename');
						    			  		return false ;
						    			  	}
						    			  
						    			});
						    	  }
						      }
					      
					      
					      
					      /** 所选文件夹和文件可以被删除 **/
					      if(canDelete){
					          
					     	  $.dialog({
									title : "删除文件",
									content: "文件删除后会放在回收站，可以在回收站选择恢复或者彻底删除。",
									okValue: '确定',
							    	ok: function() {
							    		var url = window.onlinefilePath+'/rest/onlinefile_documentclass/deleteFilesAndFolders?callback=?';
							    		var dataInput = {"classId":parentFolderId,"userName":window.userName,"fileIds":filesId,"foldIds":foldsId, "companyId":companyId,"userId":g_userId};
							    		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
							    		jQuery.getJSON(url, ret.data,
												   function(data) {
													   if(data.success){
														   showBottomMsg("已删除成功","1");
														   var page = documentCenter.getCurrentPageNum();
														   documentCenter.getFileList(parentFolderId,"","",page);
														   
														   /** 将删除信息发送到聊天记录 **/
														   activeItems.each(function(){
																var item = $(this);
																var fileName = item.attr("fileName");
																var fileType = item.attr("fileType");
																var version = item.attr("version");
																var isfile = item.attr("isfile");
																if('0' == isfile){
																	var json = {file:{fileName:fileName,isFile:"0"},creator:{}};
																	$.WebIM.sendFileMsgNotAction(groupFlag, json,"deleteFolder");
																	
																}else{
																	var json = {file:{fileName:fileName,type:fileType,version:version,isFile:"1"},creator:{}};
																	$.WebIM.sendFileMsgNotAction(groupFlag, json, "deleteFile");
																}
																
															});
													   }else{
														   showBottomMsg("删除失败","2");
													   }
										  });
							    	},
							    	cancelValue:'取消',
							    	cancel: true
							    }).showModal();
					    	 
					      }else{
					    	  
					    	  deleteFoldMsg = ('' == deleteFoldMsg ? '' : '文件夹「' + deleteFoldMsg + '」已被删除<br>');
					    	  noRightFoldMsg = ('' == noRightFoldMsg ? '' : '文件夹「' + noRightFoldMsg + '」无权限删除<br>');
					    	  deleteFileMsg = ('' == deleteFileMsg ? '' : '文件「' + deleteFileMsg + '」已被删除<br>');
					    	  noRightFileMsg = ('' == noRightFileMsg ? '' : '文件「' + noRightFileMsg + '」无权限删除<br>');
					    	  hasMoreVersionMsg = ('' == hasMoreVersionMsg ? '' : '文件「' + hasMoreVersionMsg + '」存在多个版本<br>');
					    	  //showBottomMsg(deleteFoldMsg + noRightFoldMsg + deleteFileMsg + noRightFileMsg + hasMoreVersionMsg + '请重新选择',"2");
					    	  showMessage(deleteFoldMsg + noRightFoldMsg + deleteFileMsg + noRightFileMsg + hasMoreVersionMsg + '请重新选择',"2");
					      }
					      
					
		});
	});

	/**
	 * 回收站 文件、文件夹批量恢复 wangwenshuo 20150827
	 */
	$('#main-container').on('click', '#file_restore_batch', function(){
		var activeItems = $('#content-list div.file-item.active');	// 被选中文件夹和文件
		var fileId = '' ;	// 被选中的文件夹ids
		var trashId = '';
		//回收站同时存在我的文档删除的文件（夹） 他们有独有的companyId user_N
		var myDocFileId = ''; //我的文档被选中的ids
		var myDocTrashId = '';
		var isSearch = false;
		var isMydocument = false;
		/** 获取选中的文件夹和文件id **/
		activeItems.each(function(){
			var item = $(this);
			isSearch = item.hasClass("search");
			isMydocument = item.hasClass("myDocument");
			if(isMydocument){
				myDocFileId = myDocFileId + item.attr('fileid') + ',';
				myDocTrashId = myDocTrashId + item.attr('trashId') + ',';
			}else{
				fileId = fileId + item.attr('fileid') + ',';
				trashId = trashId + item.attr('trashId') + ',';
			}
		});
		if(trashId.length>0) trashId = trashId.substring(0, trashId.length-1);
		if(myDocTrashId.length>0) myDocTrashId = myDocTrashId.substring(0, myDocTrashId.length-1);
		
		$.dialog({
			title: '批量恢复文件(文件夹)',
			content : '确定批量恢复文件(文件夹)？恢复完成后，请前往原位置查看！',
			okValue : '确定',
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				if(trashId.length>0) {
					showBottomMsg("开始恢复分类文件（文件夹）","2");
					documentCenter.fileRestore(fileId,trashId,2, isSearch,window.companyid);
					// showBottomMsg("公司文档暂恢复服务暂不能使用","3");
				}
				if(myDocTrashId.length>0){
					//showBottomMsg("开始恢复我的文档文件（文件夹）","2");
					documentCenter.fileRestore(myDocFileId,myDocTrashId,2, isSearch,"user_"+window.userId);
				}
			}
		}).showModal();
		
	});
	
	/**
	 * 回收站 文件、文件夹批量彻底删除 wangwenshuo 20150827
	 */
	$('#main-container').on('click', '#file_destroy_batch', function(){
		var activeItems = $('#content-list div.file-item.active');	// 被选中文件夹和文件
		var fileId = '' ;	// 被选中的文件夹ids
		
		var myDocFileId = ''; //我的文档被选中的ids
		var isSearch = false;
		var isMydocument = false;
		/** 获取选中的文件夹和文件id **/
		activeItems.each(function(){
			var item = $(this);
			isSearch = item.hasClass("search");
			isMydocument = item.hasClass("myDocument");
			if(isMydocument){
				myDocFileId = myDocFileId + item.attr('fileid') + ',';
			}else{
				fileId = fileId + item.attr('fileid') + ',';
			}
		});
		
		$.dialog({
			title: '彻底批量删除文件(文件夹)',
			content : '确定彻底批量删除文件(文件夹)？删除后无法恢复！',
			okValue : '确定',
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				if(fileId.length>0){
					fileId = fileId.substring(0, fileId.length-1);
					documentCenter.fileDestroy(fileId, 2, isSearch,window.companyid);
				}
				if(myDocFileId.length>0) {
					myDocFileId = myDocFileId.substring(0, myDocFileId.length-1);
					documentCenter.fileDestroy(myDocFileId, 2, isSearch,"user_"+window.userId);
				}
			}
		}).showModal();
	});
	
	/**
	 * 文件预览liuwei20150811
	 */
	
	$('#main-container').on('click', '#file_online_preview', function(){
		var itemId = $(this).attr('target-id');
		var $fileItem = $("#"+itemId);
		var fileType = $fileItem.attr('type');
		var fileSize = $fileItem.attr('size');
		var filePath = $fileItem.attr('filepath');
		var maxFileSize = 52428800;
		var swfHttpUrl = window.onlinefilePath.substring(0, window.onlinefilePath.lastIndexOf("/"));
		var url = $.appClient.generateUrl({ESCFileViewer:'getOnlineViewUrl'},'x'); 
		if(fileSize > maxFileSize){
			showBottomMsg("您选择的文件太大,请您下载后浏览!",2);
			 return ;
		}
		if('pdf'== fileType || 'tif'== fileType || 'bmp' == fileType ||
		   'docx' == fileType ||'xls' == fileType || 
		   'doc'==fileType || 'xlsx'==fileType ||
		   'ppt'==fileType||'pptx' == fileType||
		   'txt'== fileType || "html" == fileType || "htm" == fileType ||
		   'jpg'== fileType||'png'== fileType|| 
			'gif'== fileType||'jpeg' == fileType){
			
			$.ajax({
				url:url,
				type:'POST',
				data:{'fileId':filePath,'fileType':fileType},
				success:function(data){
					var pdfUrl = eval('(' + data + ')');
					if('' == pdfUrl){
						showBottomMsg("亲,文件正在获取中请稍后再试!",2);
						return ;
					}else{
						//var param = swfHttpUrl+'/fileStoreMainServer/generic/web/viewer.html?file='+pdfUrl;
						var param = "apps/onlinefile/templates/public/generic/web/viewer.html?file="+pdfUrl;
						window.open(param,'_blank');
					
					}
				}
			});
		
		}else{
			showBottomMsg("您选择的文件格式暂不支持在线浏览,请您下载本地后浏览!",2);
			return ;
		}
	});
	
	
	/**
	 * 文件列表页面-文件申请
	 * applyFileBtn1:浏览
	 * applyFileBtn2:下载
	 * */
	$('#main-container').on('click', '#applyFileBtn1', function(){
		var targetId = $(this).attr("target-id");
		var $fileObj=$("#"+targetId);
		var userName = $fileObj.attr("ownerv1username"); //权限拥有者为第一版上传人
		var fileId = $fileObj.attr("fileid");
		var fileTitle = $fileObj.attr("filetitle");
		
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/applyFileDown?callback=?';
		var dataInput = {'companyId':window.companyid,'username':window.userName,'fileId':fileId,'fileTitle':fileTitle,'applyType':'1'};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			if(json.isOk){
				$.WebIM.applyFileClickFun(userName, fileId, fileTitle, "isLook");
			}else{
				showBottomMsg("申请已发送成功，请等待回复...", 2);
			}
		});
		$("#menu_panel").hide();
		return false;
	});
	$('#main-container').on('click', '#applyFileBtn2', function(){
		var targetId = $(this).attr("target-id");
		var $fileObj=$("#"+targetId);
		var userName = $fileObj.attr("ownerv1username"); //权限拥有者为第一版上传人
		var fileId = $fileObj.attr("fileid");
		var fileTitle = $fileObj.attr("filetitle");
		
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/applyFileDown?callback=?';
		var dataInput = {'companyId':window.companyid,'username':window.userName,'fileId':fileId,'fileTitle':fileTitle,'applyType':'2'};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			if(json.isOk){
				$.WebIM.applyFileClickFun(userName, fileId, fileTitle, "isDownload");
			}else{
				showBottomMsg("申请已发送成功，请等待回复...", 2);
			}
		});
		$("#menu_panel").hide();
		return false;
	});
	
	/**
	 * 文件详细页面-文件申请
	 */
//	$('#main-container').on('click', '#applyFileBtn', function(){
	$('#main-container').on('click', '.btn-primary', function(){
		var applyFileBtn=$(this).attr("flag");
		var applylabel="0";
		if(applyFileBtn == "applyFileBtn1"){//申请浏览
			applylabel="1";
		}
		if(applyFileBtn == "applyFileBtn2"){//申请下载
			applylabel="2";
		}
			
		var $fileInfo = $(this).closest("div.fileinfo");
		//向拥有者发送申请消息（用户没被禁用，文件拥有者就是文件创建者；用户被禁用后，文件拥有者为企业管理员）
		var userName = $fileInfo.attr("ownerv1username"); //权限拥有者为第一版上传人
		var fileId = $fileInfo.attr("fileId");
		var fileTitle = $fileInfo.attr("fileTitle");
		//文件申请加上时间限制3分钟发送一次
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/applyFileDown?callback=?';
		var dataInput = {'companyId':window.companyid,'username':window.userName,'fileId':fileId,'fileTitle':fileTitle,'applyType':applylabel};
		var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			if(json.isOk){
				if(applylabel != undefined  && applylabel == '0'){
					applyAction(userName, fileId, fileTitle);
				}else if(applylabel != undefined && applylabel == '1'){
					$.WebIM.applyFileClickFun(userName, fileId, fileTitle, "isLook");
				}else if(applylabel != undefined  && applylabel == '2'){
					$.WebIM.applyFileClickFun(userName, fileId, fileTitle, "isDownload");
				}
			}else{
				showBottomMsg("申请已发送成功，请等待回复...", 2);
			}
		});
		return false;
	});
	
	/**
	 * 申请文件
	 */
	function applyAction(userName, fileId, fileTitle) {
		$.WebIM.applyFileClickFun(userName, fileId, fileTitle, "");
	}
	
	/**
	 * 关闭右侧的弹出邮箱附件panel
	 */
	function closeEmailAttachMentContentPanel() {
		var $panel = $("#action-emailattachment-content-panel");
		if ($panel.length != 0) {
			$panel.hide(300);
			$panel.remove();
		}
	}
	
	/**
	 * 关闭邮箱设置右侧的弹出panel
	 */
	function closeEmailSettingPanel() {
		var $panel = $("#email-setting-content-panel");
		if ($panel.length != 0) {
			$panel.hide(300);
			$panel.remove();
		}
	}
	
	/**
	 * 关闭新建邮箱右侧的弹出panel
	 */
	function closeCreateEmailPanel() {
		var $panel = $("#email-create-content-panel");
		if ($panel.length != 0) {
			$panel.hide(100);
			$panel.remove();
		}
	}
	
	$("#main-container").on("click", "#panel-close", function() {
		closeContentPanel();
	});
	
	/**
	 * wangwenshuo 20151020 复制到 面板关闭
	 */
	$("#main-container").on("click", "#copy-panel-close", function() {
		closeCopyContentPanel();
	});
	
	$("#main-container").on("click", "#panel-email-close", function() {
		closeEmailAttachMentContentPanel();
	});
	
	
	$("#main-container").on("click", "#email-setting-panel-close", function() {
		$("#email_panel_maskLayer").hide();
		closeCreateEmailPanel();  //先关闭创建窗口
		closeEmailSettingPanel();
	});
	
	$("#main-container").on("click", "#email-create-panel-close", function() {
		closeCreateEmailPanel();
	});
	
	/**
	 * 上传文件，分享到后面的下拉框点击事件
	 */
	$("#chooseUploadFolderBtnId").on("click",function(){
		$("#uploadChooseFolderDivId").toggle();
		$("#chooseFolderCrumbs").jFolderCrumb({data:[{"id":"1", "name":"全部分类", "idSeq":"1."}],clickFunName:"forwardFolderForUpload"});
		getForderLstOfUpload(1);
	});
	
	
	$("#uploadOpenlevelId,#uploadkeepSettingbtnId").on("click",function(){
		chooseFolderAIdClick();
	});
	$("#uploadOpenlevelUlId li a").on("click",function(){
		$("#openLeverTitleShowSpanId").text($(this).find("span").text());
		$("#uploadOpenlevelId").attr("openlevel",$(this).find("span").attr("openlevel"));
	});
	$("#uploadkeepSettingulId a").on("click",function(){
		$("#keepSettingTitleShowSpanId").text($(this).find("span").text());
		$("#uploadkeepSettingbtnId").attr("keepvalue",$(this).find("span").attr("keepvalue"));
	});
	
	
	
	/**
	 * 上传文件，选择好文件夹后的确定事件
	 */
	$("#chooseFolderAId").on("click",function(){
		chooseFolderAIdClick();
	});
	
	/**  上传文件关闭选择文件夹窗口  **/
	$("#closeChooseFolderBtnId").on("click",function(){
		$("#uploadChooseFolderDivId").hide();
	});
	//20151122 xyc 禁用的用户默认显示自己的文档。
	if(window.companyid !="-1"){
		$("#mySubscribe").click();
	}else{
		$("#myDocument").click();
	}
});

function closeFileInfo(){
	$("#mainContentLeft4FileInfo").hide();
	$("#mainContentLeft4FileInfo").html("");
	$("#mainContentLeft").show();
}

/**
 * 关闭右侧的弹出panel
 */
function closeContentPanel() {
	if(window.globalUserStatus == 0){
		return false;
	}
	var $panel = $("#action-content-panel");
	if ($panel.length != 0) {
		$panel.hide(300);
		/** xiaoxiong 20150524 如果是账户设置界面，关闭的时候将临时头像删除 **/
		if($("#imghead").get(0) && $(".loginoutbnt").attr("hastempuserportrait")=="1"){
			jQuery.getJSON($(".loginoutbnt").attr("removetempuserportraiturl")+'?callback=?',
					function(json) {
			});
		}
		$panel.remove();
	}
	hideMaskLayer();
}

/**
 * wangwenshuo 20151020 复制到 面板关闭
 */
function closeCopyContentPanel() {
	if(window.globalUserStatus == 0){
		return false;
	}
	var $panel = $("#copy-action-content-panel");
	if ($panel.length != 0) {
		$panel.hide(300);
		$panel.remove();
	}
	hideMaskLayer();
}

/**
 * 隐藏蒙板层 
 */
function hideMaskLayer() {
	if(!$("#art_panel_maskLayer").hasClass("hidden")) {
		$("#art_panel_maskLayer").addClass("hidden")
	}
}

/**
 * 此方法只是为了在搜索框的下拉框显示的时候给出提示
 */
$("#searchFileInputId").on("keyup",function(e){
	if(e.keyCode == 46 || e.keyCode == 8){//46  delete事件   8 //回格事件
		if($("#searchFileInputId").val().trim().length==0){
			$("#searchSelectDivId").addClass("searchSelectMainDivHide");
			$("#action-content-panel-searchAll").hide();
			$("#searchAllResultID").html("");
			$("#action-content-panel-searchTrash").hide();
			$("#searchResultContent").html("");
			$("#searchFileInputId").removeClass("searching");
			$("#searchAllModalId").hide();
			return;
		}
	}
	if(e.keyCode!=13){
		$("#searchSelectId").removeAttr("style");
		$("#searchAllId").removeAttr("style");
		showsearchSelectDivId();
	}
	if(e.keyCode == 13){
		$("#searchSelectId").css('background-color','#E0E0D8');
	}
	var hoverObj = $("#searchSelectDivId").find("div[name='hover']");
	if(e.keyCode == 40){
		if(hoverObj&&hoverObj.next("div").attr("id")){
			hoverObj.removeAttr("style");
			hoverObj.removeAttr('name');
			hoverObj.next("div").css('background-color','#F9F9F7');
			hoverObj.next("div").attr('name','hover');
		}else{
			$("#searchSelectDivId").find(".searchSelectEtem").first().css('background-color','#F9F9F7');
			$("#searchSelectDivId").find(".searchSelectEtem").first().attr('name','hover');;
		}
	}
	if(e.keyCode == 38){
		if(hoverObj&&hoverObj.prev("div").attr("id")){
			hoverObj.removeAttr("style");
			hoverObj.removeAttr('name');
			hoverObj.prev("div").css('background-color','#F9F9F7');
			hoverObj.prev("div").attr('name','hover');
		}else{
			$("#searchSelectDivId").find(".searchSelectEtem").last().css('background-color','#F9F9F7');
			$("#searchSelectDivId").find(".searchSelectEtem").last().attr('name','hover');
		}
	}
}).on("click",function(){
	if($("#searchFileInputId").val().trim().length>0){
		showsearchSelectDivId();
		return false;
	}
})

/*$("#searchSelectId").hover(function(){
	$("#searchSelectId").removeAttr("style");
	$("#searchAllId").removeAttr("style");
},function(){}).on("click",function(){
	$("#searchSelectId").css('background-color','#E0E0D8');
});
$("#searchAllId").hover(function(){
	$("#searchSelectId").removeAttr("style");
	$("#searchAllId").removeAttr("style");
},function(){}).on("click",function(){
	$("#searchAllId").css('background-color','#E0E0D8');
});*/
$(".searchSelectEtem").hover(function(){
	$(this).css('background-color','#F9F9F7');
	$(this).attr('name','hover');
},function(){
	$(this).removeAttr("style");
	$(this).removeAttr('name');
})

function showsearchSelectDivId(){
	
		if($("#myDocument").hasClass("active")){
			//我的文档
			$("#searchSelectId").text("在 我的文档 下搜索");
		}else if($("#mySubscribe").hasClass("active")){
			//我的关注
			$("#searchSelectId").text("在 我的关注 下搜索");
		}else if($("#myEmail").hasClass("active")){
			//我的邮件
			$("#searchSelectId").text("在 我的邮件 下搜索");
		}else if($("#trash").hasClass("active")){
			//我的回收站
			$("#searchSelectId").text("在 我的回收站 下搜索");
		}else if($(".main-left li.active").attr("data-class-id") != undefined || $("#starClassList li.active").attr("data-class-id") != null){
			//分类下的
//			$("#searchSelectId").text("在 "+$("#documentClassList li.active").attr("groupname")+" 下搜索");
			var documentForderClass = $('#file-breadcrumbs .last');   //最后文件夹路径data-idseq
			var idSeq = documentForderClass.attr("data-idseq");
			if(idSeq == undefined || idSeq == null || idSeq == "undefined" || idSeq.length==0){
				idSeq = $("#searchSelectId").attr("searchIdseq");
			}
			var $obj = $(".main-left li.active");    //分类的data-idseq
			var dataIdseq = $obj.attr('data-idseq');
			if(dataIdseq==idSeq){  //相等说明当前的搜索路径为分类
				$("#searchSelectId").text("在 当前分类 下搜索");
			}else{
				$("#searchSelectId").text("在 文件夹 下搜索");
			}
		}else{
			//只有搜索全部
			return false;
		}
		$("#searchSelectDivId").removeClass("searchSelectMainDivHide");
		return true;
}


function chooseFolderAIdClick(){
	var $folderActiveObj = $("#upload_folder_list .folder-item.active");
	var folderId = $folderActiveObj.attr("data-id");
	var folderIdSeq = $folderActiveObj.attr("data-idseq");
	if(folderId != undefined && folderId!= "undefined"){
		$("#chooseUploadFolderBtnId").attr("folderId",folderId);
		$("#chooseUploadFolderBtnId").attr("idSeq",folderIdSeq);
		$("#chooseFolderShowSpanId").text($folderActiveObj.text());
	}
	$("#uploadChooseFolderDivId").hide();
	$('#upload_folder_list').html("");
}

/**
 * 文件夹但双击事件。
 */
function folderAbindClick(){
	$("#upload_folder_list .folder-item").on("click",function(){
		$("#upload_folder_list").find(".folder-item").removeClass("active");
		$(this).addClass("active");
		
		/** 去掉上传的选择路径的确定按钮 **/
		var $folderActiveObj = $("#upload_folder_list .folder-item.active");
		var folderId = $folderActiveObj.attr("data-id");
		var folderIdSeq = $folderActiveObj.attr("data-idseq");
		if(folderId != undefined && folderId!= "undefined"){
			$("#chooseUploadFolderBtnId").attr("folderId",folderId);
			$("#chooseUploadFolderBtnId").attr("idSeq",folderIdSeq);
			$("#chooseFolderShowSpanId").text($folderActiveObj.text());
		}
		
	}).on("dblclick",function(){
		var id = $(this).attr("data-id");
		var fileName = $(this).attr("data-title");
//		documentCenter.setupFolderPathForShare(id, "chooseFolderCrumbs","forwardFolderForUpload");
		getForderLstOfUpload(id,fileName,1);
	});
}

/**
 * 获取有上传权限的文件夹列表
 */
function getForderLstOfUpload(folderId,fileName,uploadClick){
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFolderById?callback=?';
	var dataInput = {"companyId":g_companyId, "folderId":folderId, "userId":g_userId };
	var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
			function(json) {
				if((folderId==0 || folderId=="0")&&(json.size==0||json.size=="0")){
					$('#upload_folder_list').html("<span style='color:#7E7E7E;'>亲，您还没有有权限的分类供您上传，您可以在“公开级别”选项中选择“私有文档”，待有权限后再分享到分类中。</span>");
				}else{
					if(fileName!=undefined && fileName!=null && fileName!="undefined" && fileName.length>0 ){
						if(json.size!="0"){
							if(uploadClick==1){
								documentCenter.setupFolderPathForShare(folderId, "chooseFolderCrumbs","forwardFolderForUpload");
							}
							$('#upload_folder_list').html(template('upload_choose_folder_template', json)).perfectScrollbar();;
							folderAbindClick();
						}else{
							showBottomMsg("此文件夹没有子文件夹了！", "2");
						}
					}else{
						$('#upload_folder_list').html(template('upload_choose_folder_template', json)).perfectScrollbar();;
						folderAbindClick();
					}
				}
	});
}

/**
 * 检索框回车事件
 */
$('#searchFileInputId').keydown(function(e){
	if(e.keyCode==13){
		$("#searchSelectDivId").addClass("searchSelectMainDivHide");
		if($("#searchFileInputId").val().trim().length==0){
			showBottomMsg("请输入检索词","2");
			return false;
		}
		$("#searchAllModalId").hide();
		$("#searchFileInputId").addClass("searching");
		$("#action-content-panel-searchAll").hide();
		$("#searchAllResultID").html("");
		$("#action-content-panel-searchTrash").hide();
		$("#searchResultContent").html("");
		//默认要搜索全部的
//		changeUploadBtn("0");
		$("#fileOrderFieldId").attr("searchField","");
		$("#fileOrderTypeId").attr("searchType","");
		$("#searchValHidId").val($("#searchFileInputId").val());
		var hoverObj = $("#searchSelectDivId").find("div[name='hover']");
		if(hoverObj.attr("id")=="searchAllId"){
			$("#searchValHidId").attr("searchType","allClass");
			getSearchResult("","全部","","1","");
		}else{
			etemSearch();
		}
	   return false;
	}
});


$(".searchSelectEtem").on("click",function(){
	$("#searchSelectDivId").addClass("searchSelectMainDivHide");
	//空值判断
	if($("#searchFileInputId").val().trim().length==0){
		showBottomMsg("请输入检索词","2");
		return;
	}
	$("#fileOrderFieldId").attr("searchField","");
	$("#fileOrderTypeId").attr("searchType","");
	$("#searchAllModalId").hide();
	$("#searchFileInputId").addClass("searching");
	$("#action-content-panel-searchAll").hide();
	$("#searchAllResultID").html("");
	$("#action-content-panel-searchTrash").hide();
	$("#searchResultContent").html("");
	$("#searchValHidId").val($("#searchFileInputId").val());
	
	if($(this).attr("id")=="searchAllId"){
		$("#searchValHidId").attr("searchType","allClass");
		getSearchResult("","全部","","1","");
	}else{
		etemSearch();
	}
});

function etemSearch(){
	if($("#myDocument").hasClass("active")){
		//我的文档
		$("#searchValHidId").attr("searchType","myDocument");
		getSearchResult("","我的文档","","1","");
	}else if($("#mySubscribe").hasClass("active")){
		//我的关注
		$("#searchValHidId").attr("searchType","mySubscribe");
		searchSubScribeMsgByKeyWord($("#searchFileInputId").val(),1);
	}else if($("#myEmail").hasClass("active")){
		//我的邮件
		$("#searchValHidId").attr("searchType","myEmail");
		searchMyEmailListsByKeyWord($("#dropdownMenu_email").attr("title"),$("#searchFileInputId").val(),1);
	}else if($("#trash").hasClass("active")){
		//我的回收站\
		$("#searchValHidId").attr("searchType","trash");
		documentCenter.searchTrashListsByKeyWord(1);
	}else{
		$("#searchValHidId").attr("searchType","anyoneClass");
		var documentClass = $(".main-left li.active");
		var documentForderClass = $('#file-breadcrumbs .last');
		var folderId = documentForderClass.attr("data-folder-id");
		var idSeq = documentForderClass.attr("data-idseq");
		if(idSeq == undefined || idSeq == null || idSeq == "undefined" || idSeq.length==0){
			folderId = $("#searchSelectId").attr("folderId");
			idSeq = $("#searchSelectId").attr("searchIdseq");
		}
		$("#searchSelectId").attr("searchIdseq",idSeq);
		$("#searchSelectId").attr("folderId",folderId);
		getSearchResult(folderId,documentClass.text(),idSeq,"1","");
	}

}

$("#searchAllModalId").on("click",function(){
	if($("#action-content-panel-searchAll #searchAllResultID").html()!=""){
		$("#action-content-panel-searchAll").slideDown("slow");
	}else if($("#action-content-panel-searchAll #searchResultContent").html()!=""){
		$("#action-content-panel-searchTrash").slideDown("slow");
	}	
	$("#searchAllModalId").hide();
});
function getSearchResult(classId,className,idSeq,page,searchType){//className 就是记日志用的  searchValHidId
	$("#searchAllModalId").hide();
	$("#searchFileInputId").addClass("searching");
	var leftW = $("#classesList").width();
	$("#action-content-panel-searchAll").css("left",leftW+5);
	$("#action-content-panel-searchAll").show();
	var orderField =  $("#fileOrderFieldId").attr("searchField");
	var orderType =  $("#fileOrderTypeId").attr("searchType");
	var queryStr = $("#searchValHidId").val();
	searchType = $("#searchValHidId").attr("searchType"); //allClass 全部分类下检索 anyoneClass 单独分类下检索 myDocument 我的文档下检索
	$("#filePageNowId").attr("searchPage",page);
	var url= $.appClient.generateUrl({ESDocumentCenter:'searchFile'},'x');
	$.ajax({
		url:url,
		type:"post",
		data:{"companyId":g_companyId, "loginUserId":g_userId, "classId":classId,"className":className,type:searchType,"userName":userName, "idSeq":idSeq, "query":queryStr, "page":page, "pageSize":"20",orderField:orderField,orderType:orderType },
		dataType:"json",
		success:function(json){
					var titleWidth = $("#action-content-panel-searchAll").width()*0.3;
					if(titleWidth>260)titleWidth = 260;
					json.width = titleWidth;
					json.searchType = searchType;
					json.keyWord = queryStr;
					$("#action-content-panel-searchAll").html(template("searchAllResult_template",json));
					$("#searchAllTitleDivId").css("width",titleWidth);
					var bodyHeight = document.documentElement.clientHeight;
					$("#searchAllRoll").css("height",bodyHeight-260);
					if ($("#searchAllRoll").find("div.ps-scrollbar-x-rail").length > 0) {
						$("#searchAllRoll").perfectScrollbar('update');
					} else {
						$("#searchAllRoll").perfectScrollbar();
					}
					$("#searchAllResultTitleSpanId").text("检索结果（"+json.count+"）条");
					$(".searchAllClick .searchResultContentName").on("click",function(){
						searchResultClick($(this),searchType);
						return false;
					});
					
					if("1"==orderField){
						if("asc"==orderType){
							$("#searchAllTitleDivId span").removeClass("caret").addClass("caret-desc");
						}else{
							$("#searchAllTitleDivId span").removeClass("caret-desc").addClass("caret");
						}
					}else if("2"==orderField){
						if("asc"==orderType){
							$("#searchAllSizeDivId span").removeClass("caret").addClass("caret-desc");
						}else{
							$("#searchAllSizeDivId span").removeClass("caret-desc").addClass("caret");
						}
					}else if("3"==orderField){
						if("asc"==orderType){
							$("#searchAllTimeId span").removeClass("caret").addClass("caret-desc");
						}else{
							$("#searchAllTimeId span").removeClass("caret-desc").addClass("caret");
						}
					}
					
					$("#searchAllPagingDiv").pagination({
						pages: json.total,
						currentPage: json.page,
						onPageClick: function(pageNumber, event) {
							getSearchResult(classId,className,idSeq,pageNumber,searchType);
						}
					});
					$(".searchAllJumpA").on("click",function(){
						jumpToFolder($(this).attr("root-idseq"),$(this).attr("classId"),$(this).attr("data-idseq"),$(this).attr("data-id"),$(this).attr("isFile"),false);
					});
					
					$(".searchAllSentA").on("click",function(){
					var forderId = $(this).attr("forderId");
					var fileId = $(this).attr("fileId");
					
					var isgroup = $('#receiverusername').attr('isgroup');
					var name = $('#receiverusername').attr("groupName");
					
					var content="确定发送到当前聊天窗口中吗？";
					if(isgroup =='0'){
						content = "确定要将文件发送到用户 【"+name+"】的聊天窗口吗？"
					}else if(isgroup =='1'){
						var groupFlag = $('#receiverusername').attr('groupflag');
						var hexname = hex_md5(groupFlag);
						var obj = $('#newmessage'+hexname) ;
						if(obj.attr("class")=="classnewmessage"){
							content = "确定要将文件发送到分类【"+name+"】的聊天窗口吗？"
						}else if(obj.attr("class")=="newmessage"){
							content = "确定要将文件发送到分组【"+name+"】的聊天窗口吗？"
						}
					}
					var html = [
							'<div id="fileAccessRight" class="alert alert-warning" style="margin:10px 0px -5px 0px;">',
							'	<span class="tips"><strong>请配置【'+name+'】拥有的文件权限</strong></span>',
							'	<div class="options">',
							'		<div class="radio">',
							'		    <label>',
							'		      <input type="radio" name="optionsRadios" value="1" checked> 浏览权限',
							'		    </label>',
							'		</div>',
							'		<div class="radio">',
							'		    <label>',
							'		      <input type="radio" name="optionsRadios" value="3"> 浏览、下载权限',
							'		    </label>',
							'		</div>',
							'	</div>',
							'</div>'
						].join('');
					$.dialog({
						content :content+html,
						quickClose: true,
						okValue : '确定',
						ok : true,
						cancelValue : '取消',
						cancel : true,
						ok : function() {
							var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFileInfo?callback=?';
							var dataInput = {"userName":window.userName,"fileId":fileId, "companyId":g_companyId,"userId":g_userId};
							var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
							jQuery.getJSON(url, ret.data,
								function(data) {
										if(data.file.isDelete=="1"){
											showBottomMsg("对不起，文件已被删除，无法发送！","3");
											return false;
										}else if(data.hasRight == false || data.hasRight == "false"){
											showBottomMsg("对不起，您无此文件的操作权限，请到文件详细信息页进行申请！","3");
											return false;
										}else{
											if($("#receiverusername").text()=="fyBot"){
												showBottomMsg("机器人无法接收您分享的文档哦，请分享给您的其他小伙伴吧！", "3");
											}else{
												var accessRight = $("#fileAccessRight input[name='optionsRadios']:checked").val();
												$.WebIM.sendShareFile(forderId, fileId,accessRight);
											}
										}
							});
						}
					}).show();
				});
				searchResultContentDivHover();
				$("#searchFileInputId").removeClass("searching");
		}
	});
}

function searchResultContentDivHover(){
	$(".searchResultContentDiv").on("mouseover",function(){
		$(this).find(".searchResultContentOperate").css("visibility","visible");
	}).on("mouseout",function(){
		$(this).find(".searchResultContentOperate").css("visibility","hidden");
	});;
}

/**  查询我的关注信息  **/
function searchSubScribeMsgByKeyWord(keyWord,pageNumber){
	isSubSearchScribeTag = pageNumber;
	var pageLimit = 20 ;
	/** 获取关注信息  **/
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/searchSubScribeMsgByUserId?callback=?';
	var dataInput = {'userId':window.userId,"companyId":window.companyid,"pageIndex":pageNumber,"pageLimit":pageLimit,"keyWord":keyWord};
	var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
			function(json) {
				var jsonHeading = '{"type":"searchResult"}';
				if($("#content-heading").find(".navbar-brand").length == 0){
					$("#content-heading").find(".navbar-header").html("<span class='navbar-brand' id = 'mySubScribeBrand' style = 'height:34px;cursor:pointer;'><span class = 'glyphicon glyphicon-chevron-left' style = 'margin-right: 8px;'></span>"+"检索结果</span>").css({"cursor":"pointer","height":"34px"}).attr("title","返回我的关注");
				}else{
					$("#content-heading").find(".navbar-brand").html("<span class = 'glyphicon glyphicon-chevron-left' style = 'margin-right: 8px;'></span>"+"检索结果").css({"cursor":"pointer","height":"34px"}).attr("title","返回我的关注");
				}
				
				$("#subScribeBtn").hide();
				
				$("#contentBody").html(template("mySubscribe_templete",json));
				$("#content-list").perfectScrollbar('update');
				// 分页
				var pageSize =Math.floor((json.subScribeSize+pageLimit-1)/pageLimit);
				$("#pagingDiv").pagination({
					pages: pageSize,
					currentPage: pageNumber,
					onPageClick:function(pageNumber, event) {
						isSubSearchScribeTag = pageNumber;
						searchSubScribeMsgByKeyWord(keyWord,pageNumber);
					}
				});
				$("#searchFileInputId").removeClass("searching");
			});
}

/**  搜索我的邮箱信息 **/
function searchMyEmailListsByKeyWord(emailAddress,keyWord,pageNumber){

	var searchEmialAttachsByKeyWordUrl = $.appClient.generateUrl({ESEMail : 'searchEmialAttachsByKeyWord'}, 'x');
	$.post(searchEmialAttachsByKeyWordUrl,{"userid":g_userId,'companyName':window.companyName,'username':window.userName,"searchKeyWord":keyWord,"email":emailAddress,
		"pageIndex":pageNumber,"pageLimit":20,windowWidth:($(window).width()-210),ip:window.ip}, function(result){
			var json = eval('(' + result + ')');
			var jsonHeading = '{"type":"searchResult"}';
			$("#content-heading").find(".navbar-brand").html("<span id = 'backToEmailLists' class = 'glyphicon glyphicon-chevron-left' style = 'margin-right: 8px;'></span>"+"检索结果").css({"cursor":"pointer","height":"34px"}).attr("title","返回邮箱列表");
			$("#contentBody").html(template("myEmail_templete",json));
			$("#content-list").scrollTop(0);
			$("#content-list").perfectScrollbar('update');
			// 分页
			$("#pagingDiv").pagination({
				pages: json.emailAttechMentPages,
				currentPage: pageNumber,
				onPageClick:function(pageNumber, event) {
					searchEmialAttachsByKeyWord(emailAddress,keyWord,pageNumber);
				}
			});
			$("#searchFileInputId").removeClass("searching");
		});
}

function searchResultClick(searchOne,searchType){
	if(!"1"==$(searchOne).attr("isFile")){
		//如果是点击文件夹 就把data-id 传给classId 跳转到文件下，查询数据
		jumpToFolder($(searchOne).attr("root-idseq"),$(searchOne).attr("data-id"),$(searchOne).attr("data-idseq"),$(searchOne).attr("data-id"),$(searchOne).attr("isFile"),false);
		return false;
	}
	if("anyoneClass"==searchType || "allClass"==searchType || "myDocument"==searchType){
		var html = template('content_panel_template', {});
		$('#main-container').append(html);
		var $panel = $("#action-content-panel");
		var h = window.innerHeight - 58;
		$panel.width("600").height(h);
		$panel.css({top:10});
		$panel.show(300);
	}
	documentCenter.showFileInfo($(searchOne).attr("data-id"),$(searchOne).attr("classId"),searchType);
	return false;
}

function jumpToFolderByFileIdSeq(fileIdSeq,fileId) {
	jumpToFolder("", "", "",fileId,"1",true);
}

/**
 * @param rootidseq   rootidseq是左侧分类的idseq，idseq就是数据父节点的idseq
 * @param isFromChat  wangwenshuo 20160214 添加参数isFromChat  是否来自聊天窗口   聊天窗口打开文件位置按钮打开的一定是公司文档
 */
function jumpToFolder(rootidseq,classid,idseq,fileId,isFile,isFromChat){
	var page = $("#backToList").attr("currentPageNum");
	var searchType = $("#searchValHidId").attr("searchType");
	var  companyId= g_companyId;
	if(searchType=="myDocument" && !isFromChat){ /*true：聊天窗口“打开文件位置”按钮触发，一定是公司文档 */
		companyId="user_"+g_userId;
	}
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFileStatus?callback=?';
	var dataInput = {"companyId":companyId, "fileId":fileId,"isFile":isFile};
	var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
			function(json) {
				json = json.fileInfo;
				if(json.isDelete=="1"){
					showBottomMsg("对不起，文件已被删除，无法为您跳转！", "3");
					return;
				}else{
						rootidseq = json.rootIdseq;
						if(classid == fileId){
							classid = json.id;
							idseq = json.idSeq;
						}else{
							classid = json.classId;
							idseq = json.idSeqSrc;
						}
						$("#action-content-panel-searchAll").hide();
						var docClassObj = $("#classesList li[data-idseq='"+rootidseq+"']");
						var json='{"classId":"'+classid+'","titleText":"'+docClassObj.attr("groupname")+'"}'; //从左侧列表里去选去
						$('#bodyContent_Title').html(template('documentClass_menu_templete',jQuery.parseJSON(json)));
						$("#classesList li").removeClass("active");
						docClassObj.addClass("active");
						
						// xiewenda 添加检索文件跳转定位到对应类滚动条滚动
						$("#privateClassSub").css("display","block");
						$("#publicClassSub").css("display","block");
						$("#classesList").scrollTop(docClassObj.position().top);
						$('#classesList').perfectScrollbar('update');
						
						var viewType = $.cookie('file-view-type_'+window.userId); 
						if (!viewType) {
							viewType = 'icon';
						}
						var sortType = $("#file-sort-type").val();
						var file_view_filter_btn_html = template("file_view_filter_btn_template", {"viewType":viewType,"sortType":sortType});
						// 设置头部
						if(searchType=="myDocument"){
							var jsonHeading = '{"type":"myDocument"}';
						}else{
							var jsonHeading = '{"type":"documentAll"}';
						}
						
						var json = jQuery.parseJSON(jsonHeading);
						json["file_view_filter_btn_html"] = file_view_filter_btn_html;
						
						$("#content-heading").html(template('file_panel_heading_template', json));
						$("#contentBody").html("");
						changeUploadBtn("0");
						$('#mainContentLeft').show();
						$('#mainContentRight').show();
						$("#fileOrderFieldId").val("");
						$("#fileOrderTypeId").val("");
						// 查询文件列表
						documentCenter.getFileList(classid,idseq,"",page);
						closeFileInfo();
				//	}
				}
		});
}

$("body").on("click","#searchAllCloseId",function(){
	$(this).closest(".action-content-panel").hide();
	$("#searchAllResultID").html("");
});
$("body").on("click","#searchAllMinusId",function(){
	$(this).closest(".action-content-panel").slideUp("slow");
	$("#searchAllModalId").show();
});

$("#searchTrashCloseId").on("click",function(){
	$(this).closest(".action-content-panel").hide();
	$("#searchResultContent").html("");
});
$("#searchTrashMinusId").on("click",function(){
	$(this).closest(".action-content-panel").slideUp("slow");
	//$("#action-content-panel-searchAll").slideUp("slow");
	$("#searchAllModalId").show();
});

/**
 * 切换上传按钮的权限
 */
function changeUploadBtn(isShowUpload){
	if("1"==isShowUpload){
//		$("#cantuploadimgid").hide();
		$("#uploadAllBtnDivId").attr("style","position: absolute;top: 121px;width: 79px;height: 35px; ");
		$("#uploadAllBtnDivId").css("right",$("#flyingchat").width()+82+"px");
	}else{
		$("#uploadAllBtnDivId").attr("style","position: absolute;top: 0px;width: 79px;height: 35px; ");
		$("#cantuploadimgid").show();
	}
	/**
	 * 隐藏检索全部窗体
	 */
	$("#action-content-panel-searchAll").hide();
	$("#action-content-panel-searchTrash").hide();
	$("#searchAllResultID").html("");
	$("#searchResultContent").html("");
	$("#searchAllModalId").hide();
}

/**
 * liumingchao  ↓
 */
$('#newClassPanel .createClassBtn').on('click',function(){
	var groupname = $('.createClassName').val();
	if(groupname == ''){
		showBottomMsg("分类名不能为空！",2);
		return false;
	} else if($.trim(groupname) == ''){
		showBottomMsg("分类名不能为空！",2);
		return false;
	}else if($.trim(groupname).length>=30){
		showBottomMsg("分类名支持的最大长度小于30！",2);
		return false;
	}else if(classNameTest.test(groupname) ==false){
		showBottomMsg('分类名不能包含\ / : * \\\\ ? \\" < > |等字符', 2);
		return false;
	}
	var groupuserids = "" ;
	var groupusernames = "" ;
	groupuserids =  window.userId ;
	groupusernames =  window.userName ;
	var groupnames = $.trim(groupname);
	var groupremarks = $('.createClassDescription').val();
	//创建分组！！此方法写在了ESClass里面
	var thisbtn = $(this).button('loading');
	var url = $.appClient.generateUrl({ESDocumentClass:'addClassByNameAndCreateGroup'},'x');
	$.ajax({
		url:url,
		type:'POST',
		data:{className:groupname,companyId: window.companyid,userId:userId,groupuserids:groupuserids,manageruserid:userId,groupname:groupnames,groupremark:groupremarks},
		success:function(data){
			$(thisbtn).button('reset');
			var success = $.parseJSON(data)
			if(success.success == 'true'){
				documentCenter.getClassList(window.userId);
				$("#newClassPanel").hide(); 
				$('.createClassName').val("");
				$('.createClassDescription').val("");
				showBottomMsg("创建分类成功！",1);
				$.WebIM.crateClassGroup(success.flag,groupusernames.replace('@', '\\40'),groupnames);
			}
			
			if(success.success == 'false'){
				if(success.ishave == 'true'){
					showBottomMsg("已经存在 "+groupname+" 分类！",2);
				}else{
					showBottomMsg("创建分类失败！",2);
				}
				
			}
		}
	});
});

/**点击上传企业logo**/
$("#upload_div").on('click',function(){
	campanyinfo(1);
});

/**20151020 xiayongcai 也可修改企业logo*/
$("#companylogo1").on('click',function(){
	if(window.isAdmin == "1"){
		campanyinfo(1);
	}
});
/**liuwei 20150516 **/
//点击企业信息完善
$('#campanyinfo').on('click',function(){
	campanyinfo(0);
});


function campanyinfo(parameter){
	changeUploadBtn("0");
	url =  $.appClient.generateUrl({ESCompanyRegist : 'campanyInfo'},'x');
	$.ajax({
		url:url,
		type: "POST",
		data:{companyId: window.companyid},
		error:function(){
		},
		success:function(data){	
			$.fywindow({
				title:'企业信息完善',
				width: 690,
				height: 420,
				content:data
			});
			selectCampanyInfo(parameter);
		}
	});
}

/**验证输入长度**/
/*function checkInput(obj,len){
	 if(obj.value.replace("/[^/x00-/xFF]/g",'**').length>=len){
		   obj.value=leftUTFString(obj.value,len);
		   showBottomMsg("公司地址长度只能在6-120个字符之间!",3);
		   $("#addresses").css('border-color','#FF0000');
	  }else{
		  $("#addresses").css('border-color','#ccc');
	  }
}

function leftUTFString(str,len) { 
	  if(getStringUTFLength(str)<=len) 
	   return str; 
	   var value = str.substring(0,len); 
	   while(getStringUTFLength(value)>len) { 
	 	  value = value.substring(0,value.length-1); 
	   } 
	return value; 
}*/
/**验证输入长度**/
/**点击企业信息触发**/
function selectCampanyInfo(parameter){
	if(parameter == 1){
		$('#campanyInfo #campanyinfoDDId').hide();
		$('#campanyInfo #logoimagediv').show();
		$('#campanyInfo #campanyinfo_perfect').removeClass("active");
		$("#logoimage").addClass("active");
	}else{
		selectStyle();
	}
	selectStyle();
	/** 点击保存按钮* */
	$('#campanyInfo').on('click','#saveCampanyInfo_perfect',function(){
		var companynamezz = /^[a-zA-Z0-9()\u4E00-\u9FA5_\[\]\_\(\)]+$/ ;
		var companyaddresszz = /^[a-zA-Z0-9()\u4E00-\u9FA5_\[\]\_\(\)\（\）\-\,\，\.\。\:\：]+$/ ;
		var $companyid=$('#campanyInfo #companyid').val();
		var $companyname = $('#campanyInfo #companyname').val();
		var $companyphone = $('#campanyInfo #companyphone').val();
		var $postcode = $('#campanyInfo #postcode').val();
		var $companyfax = $('#campanyInfo #companyfax').val();
		var $addresses = $('#campanyInfo #addresses').val();
	
		var CheckNameZzStr = '<span class="errorStr">'+"- [ ] ( ) "+'</span>';
		var CheckAddressZzStr = '<span class="errorStr">'+"（   ） 。  ， [  ]  ： _  - "+'</span>';
		/** 验证输入是否合法* */
		if(trim($companyname) == ""){
			showBottomMsg("公司名不能为空！",2);
			return false;
			/*
			 * else if(trim($companyname.replace("/[^/x00-/xFF]/g",'**')).length<4){
			 * showBottomMsg("公司名称长度只能在4-50个字符之间!",2); return false; }
			 */ 
		}else if(trim($companyname.replace("/[^/x00-/xFF]/g",'**')).length>50 
					 || trim($companyname.replace("/[^/x00-/xFF]/g",'**')).length < 4){
			showBottomMsg("公司名称长度只能在4-50个字符之间!",2);
			return false;
		} else if(companynamezz.test($companyname) == false){
			showBottomMsg("公司名称只能包含"+ CheckNameZzStr+ "等特殊字符。" ,2);
			return false;
		}
		if(trim($addresses) == ""){
			showBottomMsg("公司地址不能为空！",2);
			return false;
		}else if(trim($addresses.replace("/[^/x00-/xFF]/g",'**')).length < 6){
			showBottomMsg("公司地址长度只能在6-120个字符之间!",2);
			return false;
		}else if(trim($addresses.replace("/[^/x00-/xFF]/g",'**')).length > 120){
			showBottomMsg("公司地址长度只能在6-120个字符之间!",2);
			return false;
			
		//This Math Will Start 
		}else if(trim($addresses) !='' && companyaddresszz.test($addresses) == false){ // 更换正则
			showBottomMsg("公司地址只能包含"+ CheckAddressZzStr+ "等特殊字符。",2);
			return false;
		}
		
		//Top Math Was End (-:
		if(trim($companyphone) != "" && teleZZ.test($companyphone) == false){
			showBottomMsg("电话号码格式不正确！",2);
			return false;
		}
		if(trim($companyfax) != "" && fexZZ.test($companyfax)==false){
			showBottomMsg("传真号码格式不正确！",2);
			return false;
		}
		if(trim($postcode) != "" && postcodeZZ.test($postcode) == false){
			showBottomMsg("邮编格式不正确！",2);
			return false;
		}
		url =  $.appClient.generateUrl({ESCompanyRegist : 'editCampanyInfo'},'x');
		
		$.ajax({
			url:url,
			type: "POST",
			data:{
				companyid:$companyid,companyname:$companyname,addresses:$addresses,companyphone:$companyphone,postcode:$postcode,companyfax:$companyfax
			},
			dataType:"json",
			error:function(){
				showBottomMsg("编辑公司信息失败，请重试",2);
			},
			success:function(datas){
				if(datas.success){
					showBottomMsg("信息保存成功!",1);
					$("#companylogo").attr('title', datas['companyname']);
					document.title=datas['companyname']+"-线上分享系统";
				}else{
					showBottomMsg("企业名称已被注册,请更换企业名称.",2);
				}
			}
		});
		return false;
	});
	
	/** 上传头像* */
	uploadImage();
	$("#cancelCompany").on('click',function(){
		//弹出对话框 确认是否真的删除
		$.dialog({
			title: '注销企业',
			content : '确定要注销企业吗？该动作不可撤销',
			okValue : '确定',
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				$.post($.appClient.generateUrl({ESCompanyRegist:'cancelCompany'},'x'),
						{userId:g_userId,companyId:g_companyId}, function(json){
							var success = $.parseJSON(json);
							if(success.success=='true'){
								showBottomMsg("注销企业成功", 1);
								setTimeout(function(){
									window.location.href = window.logouturl ;
								}, 2000);
							}else{
								showBottomMsg("注销企业失败", 2);
							}
							
						});
			}
		}).showModal();
	});
}

/**点击触发样式**/
function selectStyle(){
	/**点击上传企业logo**/
	$('#campanyInfo #logoimage').unbind('click').on('click',function(){
		$('#campanyInfo #campanyinfoDDId').hide();
		$('#campanyInfo #logoimagediv').show();
		$('#campanyInfo #campanyinfo_perfect').removeClass("active");
		$(this).addClass("active");
		
	});
	/**点击基本信息设置**/
	$('#campanyInfo #campanyinfo_perfect').unbind('click').on('click',function(){
		$('#campanyInfo #campanyinfoDDId').show();
		$('#campanyInfo #logoimagediv').hide();
		$("#campanyInfo #logoimage").removeClass("active");
		$(this).addClass("active");
	});
}

/**上传图片方法**/
function uploadImage(){
	$('#campanyInfo').on('change', ".logo", function(){
		var file = $(this).val(); 
		var filenames = file.split(".") ;
		var extname = filenames[filenames.length-1].toLowerCase() ;
		if(extname=='jpeg' || extname=='jpg' || extname=='png' || extname=='bmp'){
			
		} else {
			showBottomMsg("系统只支持jpeg、jpg、png、bmp格式的logo！", 3);
//			document.getElementById("imgheadinput").outerHTML=document.getElementById("imgheadinput").outerHTML;
			return false ;
		}
		if(window.ActiveXObject) {
			$.ajaxFileUpload({  
				fileElementId: 'campanylogoinput',  
				url: $.appClient.generateUrl({ESCompanyRegist : 'uploadImage1'},'x'), 
				dataType:"text/html",
				beforeSend: function (XMLHttpRequest) {  
					//("loading");  
				},  
				success:function(responseText){
					var success = $.parseJSON(responseText);
					if(success.success ==true){
						$("#companylogo").attr('src',success.logosrc);
						$("#companylogo").show();
						$("#campanylogoimg").attr('src',success.logosrc);
						showBottomMsg("上传企业logo成功！", 1);
						$("#upload_div").hide();
					}else if(success.success == false){
						showBottomMsg(success.msg, 3);
					}
				},
				error:function(){
					showBottomMsg("系统错误!", 3);
				},  
				complete: function (XMLHttpRequest, textStatus) {  
					//("loaded");  
				}  
			});  
		} else {
			$("#campanyInfo #imagesform").ajaxSubmit({
				url: $.appClient.generateUrl({ESCompanyRegist : 'uploadImage1'},'x'),
				type:"post",
				dataType:"text",
				success:function(responseText){
					var success = $.parseJSON(responseText);
					if(success.success == true){
						$("#companylogo").attr('src',success.logosrc);
						$("#companylogo").show();
						$("#campanylogoimg").attr('src',success.logosrc);
						showBottomMsg("上传企业logo成功！", 1);
						$("#upload_div").hide();
					}else if(success.success ==false){
						showBottomMsg(success.msg, 3);
					}
				},
				error:function(){
					showBottomMsg("系统错误!", 3);
				}
			});
		}
		return false;
		
	});
	
}


/*账户设置*/
$('#accountSetting').on('click',function(){
	changeUploadBtn("0");
	url =  $.appClient.generateUrl({ESUserInfo : 'accountAndLog'},'x');
	$.ajax({
		url:url,
		type: "POST",
		data:{userId: window.userId},
		error:function(){
		},
		success:function(data){	
			$.fywindow({
				title:'账户设置',
				width: 800,
				height: 520,
				content:data,
				style:'padding:0px'
			});
			accountSetting();
		}
	});
	
});


/**
 * 管理员安全日志
 * */
$('#safetyLog').on('click',function(){
	url =  $.appClient.generateUrl({ESUserInfo : 'safetyLog'},'x');
	$.ajax({
		url:url,
		type: "POST",
		data:{userId: window.userId},
		error:function(){
		},
		success:function(data){	
			$.fywindow({
				title:'安全日志',
				width: 800,
				height: 520,
				content:data
			});
		}
	});
});

function accountSetting(){
	$("#mainusermsgset").perfectScrollbar();
	/**
	 * 安全日志
	 */
	$("#loginLog").on("click",function(){
		changeUploadBtn("0");
		$("#userSettingDDId").hide();
		$('#acountAndLog #headimagediv').hide();
		$("#loginLogDivId").show();
		$("#password").hide();
		$("#singleSetDivId").hide();
		$("#user_perfect").removeClass("active");
		$("#headimage").removeClass("active");
		$("#changepassword").removeClass("active");
		$("#singleSet").removeClass("active");//删除个性设置
		$("#loginLog").addClass("active");
		getLoginLog(window.userName,"1");
	});
	
	/**
	 * 点击基本设置
	 */
	$('#acountAndLog #user_perfect').unbind('click').on('click',function(){
		$('#acountAndLog #userSettingDDId').show();
		$('#acountAndLog #loginLogDivId').hide();
		$('#acountAndLog #singleSetDivId').hide();
		$('#acountAndLog #headimagediv').hide();
		$('#acountAndLog #password').hide();
		$('#acountAndLog #loginLog').removeClass("active");
		$("#acountAndLog #headimage").removeClass("active");
		$('#acountAndLog #changepassword').removeClass("active");
		$('#acountAndLog #singleSet').removeClass("active");//删除个性设置
		$(this).addClass("active");
		
	});
	/**
	 * 密码修改
	 */
	$('#acountAndLog #changepassword').unbind('click').on('click',function(){
		$('#acountAndLog #userSettingDDId').hide();
		$('#acountAndLog #loginLogDivId').hide();
		$('#acountAndLog #singleSetDivId').hide();
		$('#acountAndLog #headimagediv').hide();
		$('#acountAndLog #password').show();
		$('#acountAndLog #loginLog').removeClass("active");
		$("#acountAndLog #headimage").removeClass("active");
		$('#acountAndLog #user_perfect').removeClass("active");
		$('#acountAndLog #singleSet').removeClass("active");//删除个性设置
		$(this).addClass("active");
		
	});
	/**
	 * 头像更换
	 */
	$('#acountAndLog #headimage').unbind('click').on('click',function(){
		$('#acountAndLog #userSettingDDId').hide();
		$('#acountAndLog #singleSetDivId').hide();
		$('#acountAndLog #loginLogDivId').hide();
		$('#acountAndLog #password').hide();
		$('#acountAndLog #headimagediv').show();
		$('#acountAndLog #loginLog').removeClass("active");
		$("#acountAndLog #changepassword").removeClass("active");
		$('#acountAndLog #user_perfect').removeClass("active");
		$('#acountAndLog #singleSet').removeClass("active");//删除个性设置
		$(this).addClass("active");
		
	});
	/**
	 * 个性设置
	 */
	$('#acountAndLog #singleSet').unbind('click').on('click',function(){
		$('#acountAndLog #userSettingDDId').hide();
		$('#acountAndLog #loginLogDivId').hide();
		$('#acountAndLog #headimagediv').hide();
		$('#acountAndLog #password').hide();
		$('#acountAndLog #singleSetDivId').show();
		$('#acountAndLog #loginLog').removeClass("active");
		$("#acountAndLog #headimage").removeClass("active");
		$('#acountAndLog #changepassword').removeClass("active");
		$('#acountAndLog #user_perfect').removeClass("active");
		$(this).addClass("active");
		
	});
	//绑定按钮事件
	$('#acountAndLog').on('change','input:checkbox[name=isUpRemind]',function(){
		if($(this).is(':checked')){
			$(this).val("1");
		}else{
			$(this).val("0");
		}
	});
	$('#acountAndLog').on('change','input:checkbox[name=isDownRemind]',function(){
		if($(this).is(':checked')){
			$(this).val("1");
		}else{
			$(this).val("0");
		}
	});
	//20151109 xiayongcai 添加左侧分类设置
	$('#acountAndLog').on('change','input:checkbox[name=isOpenSpace]',function(){
		if($(this).is(':checked')){
			$(this).val("1");
		}else{
			$(this).val("0");
		}
	});
	$('#acountAndLog').on('change','input:checkbox[name=isOpenGroup]',function(){
		if($(this).is(':checked')){
			$(this).val("1");
		}else{
			$(this).val("0");
		}
	});
	
	//个性设置按钮
	$('#acountAndLog').on('click','#single_Set_User',function(){
		var isupremind = $('input:checkbox[name=isUpRemind]').val();
		var isdownremind = $('input:checkbox[name=isDownRemind]').val();
		var isupremindVal=$('input:checkbox[name=isUpRemind]').next().val();
		var isdownremindVal = $('input:checkbox[name=isDownRemind]').next().val();
		var isOpenSpace = $('input:checkbox[name=isOpenSpace]').val();
		var isOpenGroup = $('input:checkbox[name=isOpenGroup]').val();
		var isOpenSpaceVal=$('input:checkbox[name=isOpenSpace]').next().val();
		var isOpenGroupVal = $('input:checkbox[name=isOpenGroup]').next().val();
		if(isupremind == isupremindVal && isdownremind == isdownremindVal &&　isOpenSpace　== isOpenSpaceVal && isOpenGroup == isOpenGroupVal){
			showBottomMsg("无修改，无需提交",2);
			return false;
		}
		url =  $.appClient.generateUrl({ESUserInfo : 'do_singleSetUser'},'x');
		$.ajax({
			url:url,
			type: "POST",
			data:{ isUpRemind : isupremind , isDownRemind : isdownremind,userid:window.userId,isOpenSpace:isOpenSpace,isOpenGroup:isOpenGroup},
			dataType:"json",
			success:function(datas){
				if(datas!=null){
					showBottomMsg("保存成功!",1);
					var isBoolean=false;
					if(datas.ISUPREMIND == 0){
						isBoolean=false;
					}else if(datas.ISUPREMIND == 1){
						isBoolean=true; 
					}
					$('input:checkbox[name=isUpRemind]').prop("checked",isBoolean); 
					$('input:checkbox[name=isUpRemind]').val(datas.ISUPREMIND);
					$('input:checkbox[name=isUpRemind]').next().val(datas.ISUPREMIND);
					if(datas.ISDOWREMIND == 0){
						isBoolean=false;
					}else if(datas.ISDOWREMIND == 1){
						isBoolean=true; 
					}
					$('input:checkbox[name=isDownRemind]').prop("checked",isBoolean); 
					$('input:checkbox[name=isDownRemind]').val(datas.ISDOWREMIND);
					$('input:checkbox[name=isDownRemind]').next().val(datas.ISDOWREMIND);
					if(datas.isOpenSpace == 0){
						isBoolean=false;
					}else if(datas.isOpenSpace == 1){
						isBoolean=true; 
					}
					$('input:checkbox[name=isOpenSpace]').prop("checked",isBoolean); 
					$('input:checkbox[name=isOpenSpace]').val(datas.isOpenSpace);
					$('input:checkbox[name=isOpenSpace]').next().val(datas.isOpenSpace);
					if(datas.isOpenGroup == 0){
						isBoolean=false;
					}else if(datas.isOpenGroup == 1){
						isBoolean=true; 
					}
					$('input:checkbox[name=isOpenGroup]').prop("checked",isBoolean); 
					$('input:checkbox[name=isOpenGroup]').val(datas.isOpenGroup);
					$('input:checkbox[name=isOpenGroup]').next().val(datas.isOpenGroup);
					
					window.isOpenSpace=isOpenSpace;
					window.isOpenGroup=isOpenGroup;
					window.ISUPREMIND = isupremind;
			        window.ISDOWREMIND = isdownremind;
				}else{
					showBottomMsg("个性设置失败，请重试!",2);
				}
			},
			error:function(){
				showBottomMsg("个性设置失败，请重试",2);
			}
		});
		return false ;
	});
	
		$('#acountAndLog').on('click','#saveUser_perfect',function(){
			var signature = $('#acountAndLog #signature').val();
			var fullname = $('#acountAndLog #fullname').val();
			var mobilephone = $('#acountAndLog #mobilephone').val();
			var telephone = $('#acountAndLog #telephone').val();
			var email = $('#acountAndLog #email').val();
			var position = $('#acountAndLog #position').val();
			var fax = $('#acountAndLog #fax').val();
			//理论上都可不填写 但是填写就要验证
			if(trim(email)==""){
				showBottomMsg("邮箱不能为空！",2);
				return false;
			}else if(trim(fullname)==""){
				showBottomMsg("姓名不能为空！",2);
				return false;
			}else if(nameZZ.test(fullname)==false){
				showBottomMsg("姓名只包含汉字和字母，请您重新输入！",2);
				return false;
			}
			if(trim(email) != "" && emailaddressZZ.test(email)==false){
				showBottomMsg("邮箱格式不正确,请您输入正确的邮箱,如:XXX@163.com！",2);
				return false;
			}
			if(trim(mobilephone) != "" && mobtelZZ.test(mobilephone)==false){
				showBottomMsg("手机格式不正确,请您输入正确的手机号码,如:186XXXXXXXX！",2);
				return false;
			}
			if(trim(telephone) != "" && teleZZ.test(telephone)==false){
				showBottomMsg("公司电话格式不正确,请您输入正确的电话号码,如:010-5165550X！",2);
				return false;
			}
			if(trim(fax) != "" && fexZZ.test(fax)==false){
				showBottomMsg("传真号码格式不正确,请您输入正确的传真号码,如:010-5895756X！",2);
				return false;
			}
//			if(trim(position).length>50){
//				showBottomMsg("职位长度超出50个字符！",2);
//				return false;
//			}
			url =  $.appClient.generateUrl({ESUserInfo : 'editUserInfo'},'x');
			$.ajax({
				url:url,
				type: "POST",
				data:{
					fullname:fullname,signature:signature,mobilephone:mobilephone,telephone:telephone,email:email,position:position,fax:fax
				},
				dataType:"json",
				error:function(){
//					$('#acountAndLog #user_perfect_alert').html('编辑个人信息失败，请重试!');
//					$('#acountAndLog #user_perfect_alert').css('background-color','red');
//					$('#acountAndLog #user_perfect_alert').slideToggle().delay(2000).hide(0);
					showBottomMsg("编辑个人信息失败，请重试",2);
				},
				success:function(datas){	
					if(datas!=null){
						$('#current_user_name').html(fullname);
						showBottomMsg("信息保存成功!",1);
						/** xiaoxiong 20150519 在修改信息后，自动更新到首页和即时通讯中 **/
						var md5 = hex_md5(window.userName.replace("\\40", "@")) ;
						var obj = $("#itemuser-"+md5);
						obj.attr("itemuserfullname", fullname) ;
						var showName = $.WebIM.showText(fullname, window.userName) ;
						obj.find(".usersname").html(showName) ;
						obj = $("#groupitemuser-"+md5) ;
						if(obj.get(0)){
							obj.attr("itemuserfullname", fullname) ;
							obj.find(".usersname").html(showName) ;
						}
					}else{
						showBottomMsg("编辑个人信息失败，请重试!",2);
					}
					
				}
			});
			return false;
		});
		
	
	var passwordTest = /^\w{6,20}$/;
		/*改密码*/
		$('#changePassword').on('click',function(){
			var oldPassword = $.trim($("#acountAndLog #oldPassword").val());
			var newPassword = $.trim($("#acountAndLog #newPassword").val());
			var repetPassword = $.trim($("#acountAndLog #rePassword").val());
			
			if(oldPassword==''){
//				$("#acountAndLog #oldPassword").addClass('alertError');
				showBottomMsg("原始密码不能为空!",2);
				return false;
			}
			if( newPassword==''){
//				$("#acountAndLog #newPassword").addClass('alertError');
				showBottomMsg("新密码不能为空!",2)
				return false;
			}
			if(passwordTest.test(newPassword) == false){
//				$("#acountAndLog #oldPassword").addClass('alertError');
				showBottomMsg("密码只能由字母，数字，下划线组成，长度应在6-20位之间!",2);
				return false;
			}
			if(newPassword != repetPassword){
//				$("#acountAndLog #rePassword").addClass('alertError');
				showBottomMsg("重复密码与新密码输入不一致!",2)
				return false;
			}
			if(oldPassword == newPassword){
				showBottomMsg("新密码不能与原始密码相同!",2);
				return false;
			}
			var modifyurl = $.appClient.generateUrl({ESUserInfo : 'do_changepassword'}, 'x');
			$.post(modifyurl,{oldPassword:oldPassword,newPassword:newPassword,userId:window.userId}, function(result){
				var isPasswordValid = result.isPasswordValid;//密码是否正确
				var isModifySuccess = result.isModifySuccess;//是否重置成功
				if(isPasswordValid=='true'){//user_password_alert
					if(isModifySuccess=='1'){
//						$('#acountAndLog [name="user_pwd_form"]').find('input').val('')
//						$('#acountAndLog #user_password_alert').html('修成成功!');
//						$('#acountAndLog #user_password_alert').slideToggle().delay(2000).hide(0);
						showBottomMsg("修改成功!",1);
					}else{
//						$('#acountAndLog #user_password_alert').html('修改失败!');
//						$('#acountAndLog #user_password_alert').css('background-color','red');
//						$('#acountAndLog #user_password_alert').slideToggle().delay(2000).hide(0);
						showBottomMsg("修改失败!",2)
					}
				}else{
//					$("#acountAndLog #oldPassword").addClass('alertError');
					showBottomMsg("原始密码不正确!",2)
				}
				
			},'json');
			return false;
		});
		
		
		
		/**
		 * 头像
		 */
		
		var jcropHeadImage ;
		
		$("#imghead").load(function(){
			$("#mainusermsgset").perfectScrollbar('update');
			if($("#imghead").attr("src").indexOf("_h.jpeg")>-1){
				return ;
			}
			jcropHeadImage = $.Jcrop('#imghead',{
				onSelect: updateCoords,
				aspectRatio: 1
			});
			var w = $("#imghead").width();
			var h = $("#imghead").height();
			var rect1;
			var rect2;
			var rect3;
			var rect4;
			if(w>100){
				rect1 = w/2-50 ;
				rect3 = w/2+50 ;
			} else {
				rect1 = 0 ;
				rect3 = w ;
			}
			if(h>100){
				rect2 = h/2-50 ;
				rect4 = h/2+50 ;
			} else {
				rect2 = 0 ;
				rect4 = h ;
			}
			jcropHeadImage.setSelect([rect1, rect2, rect3, rect4]);
		});
		
		$("#imageform").on('change', '.f', function(){
			var file = $(this).val(); 
			var filenames = file.split(".") ;
			var extname = filenames[filenames.length-1].toLowerCase() ;
			if(extname=='jpeg' || extname=='jpg' || extname=='png' || extname=='bmp'){
				
			} else {
				if(file != ''){
				
				showBottomMsg("系统只支持jpeg、jpg、png、bmp格式的头像！", 3);
//				document.getElementById("imgheadinput").outerHTML=document.getElementById("imgheadinput").outerHTML;
				return false ;
				}
			}
			var url = $.appClient.generateUrl({ESUserInfo : 'uploadImage1'},'x') ;
			if(window.ActiveXObject) {
				$.ajaxFileUpload({  
					fileElementId: 'imgheadinput',  
					url: url,  
					dataType:"text/html",
					data:{oldimage:$("#imghead").attr("src")},
					beforeSend: function (XMLHttpRequest) {  
						//("loading");  
					},  
					success:function(responseText){
						var success = $.parseJSON(responseText);
						if(success.success ==true){
							/** xiaoxiong 20150519 在修改完头像后，自动更新到首页和即时通讯中 **/
							$("#imghead").css({width:'auto', height:'auto'}) ; 
//						$("#imghead").attr("src", success.touxiang+"?"+Math.random()) ; 
							$("#imghead").attr("src", success.touxiang) ; 
							$("#imghead").attr("path", success.touxiang) ; 
							$("#imghead").attr("realW", success.realW) ;
							$("#imghead").attr("realH", success.realH) ;
							$(".loginoutbnt").attr("hastempuserportrait", "1");
							$("#imghead_ok").show() ; 
							if($(".jcrop-holder").get(0)){
								jcropHeadImage.destroy() ;
							}
						}else if(success.success ==false){
							showBottomMsg(success.msg, 3);
						}
					},
					error:function(){
						showBottomMsg("系统错误!", 3);
					},  
					complete: function (XMLHttpRequest, textStatus) {  
						//("loaded");  
					}  
				});  
			} else {
				$("#imageform").ajaxSubmit({
					url: url,
					type:"post",
					dataType:"text",
					data:{oldimage:$("#imghead").attr("src")},
					success:function(responseText){
						var success = $.parseJSON(responseText);
						if(success.success ==true){
							/** xiaoxiong 20150519 在修改完头像后，自动更新到首页和即时通讯中 **/
							$("#imghead").css({width:'auto', height:'auto'}) ; 
//							$("#imghead").attr("src", success.touxiang+"?"+Math.random()) ; 
							$("#imghead").attr("src", success.touxiang) ; 
							$("#imghead").attr("path", success.touxiang) ; 
							$("#imghead").attr("realW", success.realW) ;
							$("#imghead").attr("realH", success.realH) ;
							$(".loginoutbnt").attr("hastempuserportrait", "1");
							$("#imghead_ok").show() ; 
							if($(".jcrop-holder").get(0)){
								jcropHeadImage.destroy() ;
							}
						}else if(success.success ==false){
							showBottomMsg(success.msg, 3);
						}
					},
					error:function(){
						showBottomMsg("系统错误!", 3);
					}
				});
			}
			return false;
			
		});
		$("#imghead_ok").on("click", function(){
			var x = $("#x").val();
			var y = $("#y").val();
			var w = $("#w").val();
			var h = $("#h").val();
			if($("#imghead").width()>=700){
				var showW = $("#imghead").width() ;
				var showH = $("#imghead").height() ;
				var realW = $("#imghead").attr("realW")*1 ;
				var realH = $("#imghead").attr("realH")*1 ;
				var xv = realW/showW ; 
				var yv = realH/showH ; 
				x = xv*x ;
				y = xv*y ;
				w = xv*w ;
				h = xv*h ;
			}
			var url = $.appClient.generateUrl({ESUserInfo : 'uploadImageOk'},'x') ;
			$.ajax({
				url: url,
				type:"post",
				data:{x:x,y:y,w:w,h:h,image:$("#imghead").attr("path")},
				success:function(responseText){
					var success = $.parseJSON(responseText);
					if(success.success ==true){
						var md5 = hex_md5(window.userName.replace("\\40", "@")) ;
						var obj = $("#itemuser-"+md5);
						obj.attr("itemuserportrait", success.touxiang) ;
						obj.find(".useritempic").attr("src", success.touxiang) ;
						$("#indexheadimage").attr('src',success.touxiang);  //修改完头像替换掉首页的头像
						$("#imghead_ok").hide() ; 
						jcropHeadImage.destroy() ;
						$("#imghead").attr("src", success.touxiang) ;
						$("#imghead").css({width:"100px", height:"100px"}) ;
						obj = $("#groupitemuser-"+md5) ;
						if(obj.get(0)){
							obj.attr("itemuserportrait", success.touxiang) ;
							obj.find(".useritempic").attr("src", success.touxiang) ;
						}
						showBottomMsg(success.msg, 1);
						$(".loginoutbnt").attr("hastempuserportrait", "");
						$("#imgheaddiv").find('.f').remove();
						$("#imgheaddiv").append('<input id="imgheadinput" type="file" name="myfile" class="f" style="width:82px;height:34px;position:absolute;opacity: 0;z-index:2;"/>');
					} else {
						showBottomMsg(success.msg, 3);
					}
				},
				error:function(){
					showBottomMsg("系统错误!", 3);
				}
			});
			return false ;
		});
}
/**
 * 安全日志的获取信息方法
 * @param username
 * @param page
 */
function getLoginLog(username,page){
/*	jQuery.getJSON(window.onlinefilePath+'/rest/onlinefileuser/getLoginLog?callback=?',
			{"userid":username, "start":page, "limit":"10" },
	function(json) {
		json.startno = (page*1-1)*10+1 ; 
		$("#loginLogMainDivId").html(template("LoginLog_template",json));
		// 分页
		$("#loginLogDivId #pagingDiv").pagination({
			pages: json.totle,
			currentPage: page,
			onPageClick:function(page, event) {
				getLoginLog(username,page);
				return false;
			}
		});
				
	});*/
	var param = {"userid":username, "start":page, "limit":"10" };
	//xiewenda
	$.ajax({
        type: 'POST',
        url: $.appClient.generateUrl({ESUserInfo: 'getLoginLog'},'x'),
        data: param,
        dataType : 'json',
        success: function(json) {
        	json.startno = (page*1-1)*10+1; 
			$("#loginLogMainDivId").html(template("LoginLog_template",json));
			// 分页
			$("#loginLogDivId #pagingDiv").pagination({
				pages: json.totle,
				currentPage: page,
				onPageClick:function(page, event) {
					getLoginLog(username,page);
					return false;
				}
			});
		},
        cache: false
    });
}

/*头像*/
function previewImage(file)
{
  var MAXWIDTH  = 260; 
  var MAXHEIGHT = 180;
  var div = document.getElementById('preview');
  if (file.files && file.files[0])
  {
      div.innerHTML ='<img id=imghead>';
      var img = document.getElementById('imghead');
      img.onload = function(){
        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
        img.width  =  rect.width;
        img.height =  rect.height;
//         img.style.marginLeft = rect.left+'px';
        img.style.marginTop = rect.top+'px';
      }
      var reader = new FileReader();
      reader.onload = function(evt){img.src = evt.target.result;}
      reader.readAsDataURL(file.files[0]);
  }
  else //兼容IE
  {
    var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
    file.select();
    var src = document.selection.createRange().text;
    div.innerHTML = '<img id=imghead>';
    var img = document.getElementById('imghead');
    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
    status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
    div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
  }
}
function clacImgZoomParam( maxWidth, maxHeight, width, height ){
    var param = {top:0, left:0, width:width, height:height};
    if( width>maxWidth || height>maxHeight )
    {
        rateWidth = width / maxWidth;
        rateHeight = height / maxHeight;
        
        if( rateWidth > rateHeight )
        {
            param.width =  maxWidth;
            param.height = Math.round(height / rateWidth);
        }else
        {
            param.width = Math.round(width / rateHeight);
            param.height = maxHeight;
        }
    }
    
    param.left = Math.round((maxWidth - param.width) / 2);
    param.top = Math.round((maxHeight - param.height) / 2);
    return param;
}


function trim(s){
	return $.trim(s); 
}
var nameZZ= /^[\u4e00-\u9fa5a-zA-Z]+$/;
var mobtelZZ =/^1[3|4|5|8][0-9]\d{8}$/;
var lengthZZ= /.{50}|^\s*$/g;
var teleZZ = /^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$/;
var emailaddressZZ = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
var fexZZ =/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/; 
var postcodeZZ = /^[0-9]{6}$/;

//创建分类的取消按钮
$('.createClassCancleBtn').on('click',function(){
	$("#newClassPanel").hide(); 
	$('.createClassName').val("");
	$('.createClassDescription').val("");
	$("#newClassPanel .createClassName").removeAttr("style");	/**20151020 xiayongcai 去除样式*/
});

$('#main-container').on('click','#createClassFile',function(){
	var offset =$(this).offset();
	$('#alertClass').css({top:offset.top+35+"px",left:offset.left-175+"px"});
	if (!$("#alertClass").is(":hidden")) {
		$("#alertClass").hide();
	} else {
		$('#alertClass').show();
		$('#alertClass input').focus();
	}
	return false;
});
$('#alertClass').on('click','#saveClassNoUser',function(){
	saveClassFile($(this));
});

$('#alertClass').on('click','#saveClassNoUserCancel',function(){
	$('#alertClass').hide();
	$('#alertClass input').val('');
});

function resetFolderName(obj){
	var itemId = $(obj).attr('target-id');
	$fileItem = $("#"+itemId);
	var parentFolderId = $fileItem.attr("folderId");
	var folderId = $fileItem.attr("fileId");
//	var folderText = $fileItem.find(".file-name").text();
	var folderText = $fileItem.find(".file-name").attr("fileName");
	$("#resetFolderInputId").val(folderText);
	$("#resetFolderInputId").attr("placeholder",folderText);
	$("#resetFolderInputId").attr("folderId",folderId);
	$("#resetFolderInputId").attr("parentFolderId",parentFolderId);
	$("#resetFolderInputId").attr("target-id",itemId);
	var offset =$(obj).offset();
	$('#resetFolderName').css("top",offset.top-65+"px");
	$('#resetFolderName').css("left",offset.left+"px");
	if($('#resetFolderName').css('display')=='none'){
		$('#resetFolderName').css('display','block');
	}else{
		$('#resetFolderName').css('display','none');
	}
	$("#resetFolderInputId").focus();
}

$("#resetFolderOKId").on("click",function(){
	var $btn = $(this).button('loading');
	var className = $.trim($("#resetFolderInputId").val());
	if($.trim(className).length>0){
		if($.trim(className)==$("#resetFolderInputId").attr("placeholder")){
			showBottomMsg("修改成功","1");
			$btn.button('reset');
		}else{
			var classNameTest = /^[^\s*\/|:<>\\?\"]*$/;
			if($.trim(className).length>50){
				showBottomMsg("文件夹名称支持的最大长度为50！",2);
				$btn.button('reset');
				return false;
			}else if(classNameTest.test(className) ==false){
				showBottomMsg('文件夹名称不能包含\ / : * \\\\ ? \\" < > | 等特殊字符！', 2);
				$btn.button('reset');
				return false;
			}
			var folderId = $("#resetFolderInputId").attr('folderId');
			var oldClassName = $("#resetFolderInputId").attr("placeholder");
			var fatherClassId = $("#resetFolderInputId").attr("parentFolderId");
			 /** 如果是我的文档创建文件夹 那么companyId应该为‘user_’+userId */
			var companyId = documentCenter.getCompanyId();
			$.ajax({
				url:$.appClient.generateUrl({ESDocumentClass:'reClassNameById'},'x'),
				type:'POST',
				data:{className:className,itemId:folderId,oldCLassName:oldClassName,fatherClassId:fatherClassId,companyId:companyId},
				success:function(data){
					$btn.button('reset');
					var data = $.parseJSON(data);
					if(data.success==true || data.success=="true"){
						showBottomMsg("修改成功！","1");
						var itemId = $("#resetFolderInputId").attr('target-id');
						$fileItem = $("#"+itemId);
						var $newObj = $fileItem.find(".file-name");
						var serialNumber = $fileItem.find(".file-name").attr("serialNumber");
						$newObj.text(serialNumber+"-"+className);
						$newObj.attr("fileName",className);
						$newObj.attr("title",serialNumber+"-"+className);
						$fileItem.find("a.file_preview_link").attr("title",serialNumber+"-"+className);
						
						if(!documentCenter.isMyDocument()){
							// 给群组聊天发送提示消息
							$.WebIM.sendFileMsg(folderId, "renameFolder");
						}
						
					}else{
						if(data.ishave==true || data.ishave=="true"){
							showBottomMsg("对不起，已经存在相同名字的文件夹！","3");
						}else{
							showBottomMsg("修改失败,请稍后再试！","3");
						}
					}
				}
			});
			
		}
	}
	$('#resetFolderName').css('display','none');
});
$("#resetFolderCancelId").on("click",function(){
	$('#resetFolderName').css('display','none');
});
//创建文件夹div的隐藏
//点击空白隐藏
$(document).mousedown(function(e){
	  var _con = $('#alertClass');   
	  if(!_con.is(e.target) && _con.has(e.target).length === 0){
		  $('#alertClass').hide();
	  }
	  var _con1 = $('#resetFolderName');   
	  if(!_con1.is(e.target) && _con1.has(e.target).length === 0){
		  $('#resetFolderName').hide();
	  }
});
//点击空白隐藏
$(document).mouseup(function(e){
	var _con = $('#transferGroupAlert');  
	var _con1 = $('#transferGroup');//点击移交分组的的时候这里不做处理 
	if(!_con1.is(e.target) &&!_con.is(e.target) && _con.has(e.target).length === 0){
		$('#transferGroupAlert').css('display','none');
	}
});

//键盘回车创建
$('#alertClass').on('keydown','input',function(event){
	if(event.keyCode==13){
		$('#saveClassNoUser').trigger('click');
	}
});

/**
 * 新建文件夹
 */
function createfilejiapower(flag){
	if(flag == "0"){
		$('#createClassFile').hide();
		$('#groupMembers_').hide();
	}else{
		$('#createClassFile').show();
		$('#groupMembers_').show();
	}
}

var classNameTest = /^[^*\/|:<>\\?\"]*$/;
//新建文件夹
function saveClassFile($btnObj){
	var $btn = $btnObj.button('loading');
	var folderId = $('#file-breadcrumbs .last').attr('data-folder-id');
	var idSeq = $('#file-breadcrumbs .last').attr('data-idseq');
	var className = $('#alertClass input').val();
	if($.trim(className) ==""){
		showBottomMsg("文件夹名称不能为空！",2)
		$btn.button('reset');
		return false;
	}else if($.trim(className).length>50){
		showBottomMsg("文件夹名称支持的最大长度为50！",2);
		$btn.button('reset');
		return false;
	}else if(classNameTest.test(className) ==false){
		showBottomMsg('文件夹名称不能包含\ / : * \\\\ ? \\" < > | 等字符！', 2);
		$btn.button('reset');
		return false;
	}
	className = $.trim(className);
	var url = $.appClient.generateUrl({ESDocumentClass:'addClassByName'},'x');
	
	/** 如果是我的文档创建文件夹 那么companyId应该为‘user_’+userId */
	var companyId = documentCenter.getCompanyId();
	var isMyDocument = documentCenter.isMyDocument();
	$.ajax({
		url:url,
		type:'POST',
		data:{className:className,companyId:companyId,classId:folderId,userId:window.userId,idSeq:idSeq},
		success:function(data){
			$btn.button('reset');
			var json = $.parseJSON(data)
			if(json.success == 'true'){
				$('#alertClass').hide();
				$('#alertClass input').val('');
				documentCenter.getFileList(folderId);
				// 发送群组聊天消息提示    我的文档不需要发送文件夹创建信息
				if(!isMyDocument){
					$.WebIM.sendFileMsg(json.id, "addFolder");
				}
			}
			if(json.success == 'false'){
				if(json.ishave == 'true'){
				//	showBottomMsg("已经存在 "+className+" 文件夹！",2);
					showBottomMsg("已经存在同名的文件夹!",2);
					$('#alertClass input').val("");
				}else{
					showBottomMsg("创建文件夹失败!",2)
				}
			}
			
		}
	});
	$('#saveClassNoUser').unbind("click");
}

function operateClass(isgroupuser){
	var $obj = $(".main-left li.active");
	var flag = $obj.attr('flag');
	var groupname = $obj.attr('groupname');
	var groupid = $obj.attr('data-group-id');
	var classId = $obj.attr('data-class-id');
	var owner = $obj.attr('owner');  //拥有者用友权限
	var remark = $obj.attr('remark');
	$('#classsetting #remarktext').html(remark);
	//只有创建者才有权限去修改这个群组的信息
	if(owner != window.userId && isgroupuser == 'true'){
		$('#deleteGroupClass').css('display','none');
		$('#editGroupClass').css('display','block');
		$('#quiteTheClass').css('display','block');
		$('#groupManener').css('display','none');
		$('#remarktext').parent().parent().css('display','block');
		//xiewenda 20150619 组内成员产看分类信息不能编辑 
		$('#editGroupClass .form-group:last').css('display','none');
		$('#editGroupClass input').attr('readonly',true);
		$('#editGroupClass textarea').attr('readonly',true);
		$('#editGroupClass textarea').attr('placeholder','');
	}else if(owner == window.userId && isgroupuser == 'true'){
		$('#deleteGroupClass').css('display','block');
		$('#editGroupClass').css('display','block');
		$('#quiteTheClass').css('display','none');
		$('#groupManener').css('display','block');
		$('#father').css('display','none');
	}else if(isgroupuser == 'false'){
		var html = '<div class="empty-panel"><i class="glyphicon glyphicon-lock"></i><br><span>无该分组的权限</span></div>';
		$('#contentBody').html(html);
	}
	//删除分类
	$('#classsetting #delete_group_class').unbind('click').on('click',function(){
		$.dialog({
			title: '删除分类',
			content : '确定要删除分类吗?分类删除后无法恢复！',
			okValue : '确定',
			ok : true,
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				var url = $.appClient.generateUrl({ESDocumentClass : 'deleteFileClassById'}, 'x');
				$.post(url, {classId:classId,companyId:window.companyid,userId:window.userId}, function(res) {
					var success = $.parseJSON(res);
					if(success.success == 'true'){
						documentCenter.getClassList(window.userId);
						setTimeout(function(){
							//删除分类后 默认显示第一个分类  如果第一个分类不存在   就进入到个人空间
							//liuwei 增加判断当前用户是否是第一个分类下的，如果是就显示第一个分类的聊天窗口否则显示机器人窗口
							if($('#documentClassList li:first').length>0 && $('#documentClassList li:first').attr('ismember') =='true'){
								$('#documentClassList li:first').trigger('click');
							}else{
								$("#mySubscribe").click();
								$.WebIM.gotoUser('fyBot');
							}
						}, 100);
						//删除分组
//						jQuery.getJSON(window.onlinefilePath+'/rest/chat/deleteGroup?callback=?', {'companyId':window.companyid,'userid':window.userId,'username':window.userName, 'groupflag':flag, "groupid":groupid},
//								function(json) {
//							if(json.isOk){
//								showBottomMsg("名称为【"+groupname+"】的分类已删除！", 1);
//								var content = groupname+"broadcast-dropgroup"+flag ;
//								remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
//								setTimeout(function(){
//									var arg = 'type=delete_group&groups='+flag;
//									remote.jsjac.chat.ofuserservice(arg, true) ;
//								}, 2000);
//							} else {
//								showBottomMsg("删除分类失败！", 3);
//							}
//						});
						
						$.ajax({
							url:$.appClient.generateUrl({ESDefault : 'deleteGroup'},'x'),
							type:'POST',
							data:{companyId:window.companyid,userid:window.userId,username:window.userName,groupflag:flag,groupid:groupid},
							datatype:"json",
							success:function(data){
								var json = $.parseJSON(data)
								if(json.isOk){
									showBottomMsg("名称为【"+groupname+"】的分类已删除！", 1);
									var content = groupname+"broadcast-dropgroup"+flag ;
									remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
									setTimeout(function(){
										var arg = 'type=delete_group&groups='+flag;
										remote.jsjac.chat.ofuserservice(arg, true) ;
									}, 2000);
								} else {
									showBottomMsg("删除分类失败！", 3);
								}
							}
						});
						return false;
					}else if(success.success == "havechildren"){
						showBottomMsg("该分类下存在文件或文件夹，不能直接删除！",2);
					}else if(success.success == "nocreator"){
						showBottomMsg("您无权删除此分类！",3);
					}else if(success.success == "haveuser"){
						showBottomMsg("该分类下存在成员，不能直接删除！",2);
					}
				});
			}
		}).showModal();  
		
	});
	//退出群聊
	$('#livemealong').unbind('click').on('click',function(){
		
		//删除当前分类中的自己
		$.ajax({
			url:$.appClient.generateUrl({ESDefault : 'outGroup'},'x'),
			type:'POST',
			data:{companyId:window.companyid,groupflag:flag,groupid:groupid,userid:window.userId,username:window.userName,fullname:encodeURI(window.fullName, "utf-8")},
			datatype:"json",
			success:function(data){
				var json = $.parseJSON(data)
				if(json.isOk){
				//下面应该是发信息
					var arg = 'type=out_group&groups='+flag+'&username='+window.userName.replace('@', '\\40');
					remote.jsjac.chat.ofuserservice(arg, false) ;
					var content = [groupname, "broadcast-", flag, "<", $("#current_user_name").html(), "退出分类【"+groupname+"】!" ].join("");
					remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
					if($("#receiverusername").attr("receiver") == flag){
						$.WebIM.gotoUser('fyBot') ;
					}
					$("#settingUp").trigger("click");
					showBottomMsg("退出分类成功！",1);
					
					//分类加锁
					var hexname = hex_md5(flag);
					var obj = $('#newmessage'+hexname) ;
					obj.attr("newmessagecount", 0);
					obj.css({display:'none'});
					obj.siblings('span').remove();
					var parent = obj.parent() ;
					parent.attr('ismember', 'false');
					parent.append('<span class="isnotMember glyphicon glyphicon-lock" title="访客"></span>');
					
				} else {
					showBottomMsg("退出分类失败！",3);
				}
			}
		});
	});
	//保存修改分类信息
	$('#classsetting').unbind('click').on('click','#edit_class_perfect',function(){
		var groupname= $.trim($('#classsetting [name="className"]').val());
		var oldName= $.trim($('#classsetting [name="className"]').attr("oldValue"));
		var mark= $.trim($('#classsetting [name="groupremark"]').val());
		var oldmark= $.trim($('#classsetting [name="groupremark"]').attr("oldValue"));
		if(groupname == oldName && mark == oldmark){
			showBottomMsg("您未做任何修改!", 2);
			return false;
		}else if($.trim(groupname).length>=30){
			showBottomMsg("分类名支持的最大长度小于30!",2);
			return false;
		}else if(classNameTest.test(groupname) ==false){
			showBottomMsg('分类名不能包含\ / : * \\\\ ? \\" < > |等字符', 2);
			return false;
		}
		var url = $.appClient.generateUrl({ESDocumentClass:'editClassInfo'},'x');
		$.ajax({
			url:url,
			type:'POST',
			data:{classId:classId,groupName:groupname,companyId:window.companyid,userId:window.userId,groupid:groupid,flag:flag,mark:mark},
			success:function(data){
				var success = $.parseJSON(data)
				if(success.success == 'true'){
					$('#classsetting [name="className"]').attr("oldValue",groupname);
					$('#classsetting [name="groupremark"]').attr("oldValue",mark);
					showBottomMsg("修改成功",1);
					
					$("#ClassNameFromEditClassName").text(groupname); 
		    		if(oldName==$("#flyingchat #receiverusername").text()){
		    			$('#receiverusername').css({"width":"auto"});
		    			$("#flyingchat #receiverusername").text(groupname); 
		    			$("#flyingchat #receiverusername").attr('title',groupname); 
		    			var tempwidth = $("#groupusersspanId").width() ;
						var maxWidth = ($("#mainContentRight").width()-160-tempwidth) ;
						if($('#receiverusername').width()>maxWidth){
							$('#receiverusername').css({"width":maxWidth+"px"});
						} else {
							$('#receiverusername').css({"width":($('#receiverusername').width()+5)+"px"});
						}
		    		}
			    	var $userspan = $("#flyingchatusers #userListspan-0");
			    	if($userspan.attr("text")=="组内成员["+oldName+"]"){
			    		$userspan.text("组内成员["+groupname+"]");
			    		$userspan.attr("text","组内成员["+groupname+"]");
			    	}
			    	
			    	var $activeObj = $("#documentClassList .active");
			    	$activeObj.attr('groupname',groupname).attr('title',groupname).attr('remark',mark); 
			    	$activeObj.find("a").html('<span class="glyphicon glyphicon-list"></span>'+groupname);
			    	
			    	var $starObj = $("#starClassList > li[data-class-id='"+classId+"']");
			    	$starObj.attr('groupname',groupname);
			    	$starObj.attr('title',groupname);
			    	$starObj.attr('remark',mark);
			    	$starObj.find("a").html('<span class="glyphicon glyphicon-list"></span>'+groupname);
					
					$("#mainContentRight .receiveruserstatus").attr('title',mark); 
					$("#mainContentRight .receiveruserstatus").html(mark); 
					$('#classsetting [name="className"]').attr("oldValue",groupname);
				}else if(success.success == 'have'){
					showBottomMsg("分类名重复",2);
				}else{
					showBottomMsg("修改失败",2);
				}
			}
		});
		return false;
	});
	
	
	//移交群组
	$('#transferGroup').on('click',function(){
		//$('#contentBody #transferGroupAlert').remove();
		if($('#transferGroupAlert').css('display')=='block'){
			$('#transferGroupAlert').css('display','none');
			return;
		}
		var groupid = $('.main-left .active').attr('data-group-id');
		var baseurl = $("#flyingchat").attr("baseurl") ; 
		var companyId = window.companyid ;
		var username = window.userName ;
//		jQuery.getJSON(baseurl+'/rest/chat/getCompanyUsersForGroupSet/'+companyId+'/'+username+'?callback=?',{'userid':window.userId,'groupid':groupid},
//				function(json) {
		$.ajax({
			url:$.appClient.generateUrl({ESDefault : 'getCompanyUsersForGroupSet'},'x'),
			type:'POST',
			data:{userid:window.userId,groupid:groupid},
			datatype:"json",
			success:function(data){
			var json = $.parseJSON(data);
			$('#groupManener').append(template("groupManeger_template",json));
			if(json.inUsers.length==1){
				$('#ifNullShow').css('display','block');
			}
			$('#groupManener').css({position:"relative"});
			$("#transferGroupAlert").css({bottom:40+"px",left:150+"px",'z-index':"1001"}).show(600);
			$("#transferGroupAlert #xialatiao").perfectScrollbar();
			$("#xialatiao").perfectScrollbar();
			$('#transferGroupAlert').on('keyup','input',function(){
				var $items = $("#xialatiao li.transfer-user-item");
				if ($(this).val().length > 0) {
					$items.removeClass("active").filter(":contains('"+( $(this).val() )+"')").addClass("active");
				} else {
					$items.addClass("active");
				}
				$("#xialatiao").scrollTop(0);
				$("#xialatiao").perfectScrollbar('update');
			});
			//点击关闭时也关闭移交窗口
			$('#findtransferGroupAlertCloseId').on('click',function(){
				$('#transferGroupAlert').css('display','none');
			});
			//点击某个人进行移交
			$('#transferGroupAlert li').on('click',function(){
				//弹出窗口验证密码
				$('#transferGroupAlert').remove();
				$('#quiteChangeManeger').remove();
				var username = $(this).attr('username');
				var userid = $(this).attr('userid');
				var headimg = $(this).attr('headimg');
				var groupid = $('.main-left .active').attr('data-group-id');
				var classId = $('.main-left .active').attr('data-class-id');
				
				$('#contentBody').append(template("quiteChangeManeger_template",{"username":window.userName,"userid":window.userId,"headimg":window.PORTRAIT}));
				
				$('#quiteChangeManeger').on('click','#confirm_password',function(){
					var password = $('#checkpassword').val();
					//验证密码
					$.ajax({
						url:$.appClient.generateUrl({ESUserInfo : 'checkPasswordIsRight'},'x'),
						type:'POST',
						data:{userid:window.userId,password:password},
						datatype:"json",
						success:function(data){
							var json = $.parseJSON(data);
							if(json.isOk){
								//说明正确，执行移交
								$.ajax({
									url:$.appClient.generateUrl({ESDefault : 'changeGroupAdmin'},'x'),
									type:'POST',
									data:{companyId:window.companyid,username:window.userName,userid:window.userId,tousername:username,touserid:userid,groupid:groupid,classId:classId},
									datatype:"json",
									success:function(data){
										var json1 = $.parseJSON(data);
										if(json1.isOk){
											$('.main-left .active').attr('creator',userid);
											$('.main-left .active').attr('owner',userid);
											showBottomMsg("分类移交成功",1);
											$("#settingUp").trigger("click");
										} else {
											showBottomMsg("分类移交失败",3);
										}
									}
								});
							} else {
								showBottomMsg("对不起，您的密码输入有误！",3);
							}
						}
					});
				});
				//取消
				$('#quiteChangeManeger').on('click','#quxiao',function(){
					$('#quiteChangeManeger').remove();	
				});
			});
//			
//			
//		});
		
			}
		});
	});
	
	
}

//邀请用户
function requestUser(){
	$('#contentHeader_right').unbind('click').click(function(){
		// 设置头部
		var jsonHeading = '{"type":"'+$(this).attr('id')+'"}';
		$("#content-heading").html(template('requestUser_templete'));
		$("#contentBody").html(template("requestUserhtml_templete"));
		//动态添加行
		dynamicAddRow();
	});
}




//获取成员列表	
function getMemberList(owners){
	changeUploadBtn("0");
	closeFileInfo();
	// 设置头部
	var jsonHeading = '{"type":"'+$(this).attr('id')+'"}';
	$("#content-heading").html(template('file_panel_heading_template', jQuery.parseJSON(jsonHeading)));
	$("#contentBody").html("");
	showMainContentLeftFooter(false);

	//分类下用户列表
	var $obj = $(".main-left li.active");
	var baseurl = window.onlinefilePath;
	var companyId = window.companyid;
	var username = window.userName;
	var classId = $obj.attr('data-class-id');
	var groupid =$obj.attr('data-group-id');
	var flag = $obj.attr('flag');
	var owner = $obj.attr('owner');
	var url = baseurl+'/rest/chat/getCompanyUsersForGroupSetAndNotJoin/'+companyId+'/'+username+'?callback=?';
	var dataInput = {'userid':window.userId,'groupid':groupid,'classId':classId,flag:flag,'companyId':window.companyid};
	var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
			function(json) {
				json.owner = owner;
				json.userId = window.userId;
				$('#contentBody').html(template('groupMembers_templete', json));
				//如果当前用户没在当前分组，则不允许邀请
				var ids = "";
				var iscreator = false;
				var nocreator = false;
				$("#myInClassUser div[isIn='1']").each(function(i){
					if($(this).attr("userid") ==window.userId){
						if(window.userId == owner ||window.userId== owners){
							$('#add_user_for_email').css('display','block');
							$('#remove_class_user').attr('disabled',false);
							$('#add_class_user').attr('disabled',false);
							$('#apply_for_group').css('display','none');
							//$("#myInClassUser div[isIn='1']").find('input').css('visibility','visible');
							//$("#myOutClassUser div[isIn='0']").find('input').css('visibility','visible');
							return false
						}else{
							//liuwei20160115 如果没有权限按钮置灰
							$('#add_user_for_email').css('display','block');
							$('#remove_class_user').attr('disabled',true);
							$('#add_class_user').attr('disabled',true);
							$('#apply_for_group').css('display','none');
							
							/*$('#add_user_for_email').css('display','block');
							$('#remove_class_user').css('visibility','hidden');
							$('#add_class_user').css('visibility','visible');
							$('#apply_for_group').css('display','none');*/
							//$("#myInClassUser div[isIn='1']").find('input').css('visibility','hidden');
							//$("#myOutClassUser div[isIn='0']").find('input').css('visibility','visible');
							return false
						}
						
					}else{
						$('#remove_class_user').attr('disabled',true);
						$('#add_class_user').attr('disabled',true);
						$('#apply_for_group').css('display','block');
						
						/*$('#remove_class_user').css('visibility','hidden');
						$('#add_class_user').css('visibility','hidden');
						$('#apply_for_group').css('display','block');*/
					}
				});
				$("#myOutClassUser div[isIn='0']").each(function(i){
					if($(this).attr("userid") ==window.userId){
						$("#myInClassUser div[isIn='1']").find('input').css('visibility','hidden');
						$("#myOutClassUser div[isIn='0']").find('input').css('visibility','hidden');
						
					}
				});
				//如果没有用户在组外，隐藏邀请按钮
				if($("#myOutClassUser div[isIn='0']").length ==0){
					$('#add_class_user').css('display','none');
				}
//				if($("#myInClassUser div[isIn='1']").length ==1 && $("#myInClassUser div[isIn='1']").attr('userid') == window.userId){
//					$('#add_user_for_email').css('display','block');
//					$('#remove_class_user').css('display','none');
//					$('#apply_for_group').css('display','none');
//				}
				
				alertUserInfo();
				
			    //打开组内成员  关闭其他
			    classUserCollapse($("#myInClassUser"),$("#myInClassUserBtn"),1);
			    $("#myInClassUserBtn").on("click",function(){
			    	$("#query_member_one").css('display','block');
			    	$("#imgQuery_member_one").css('display','block');
					$("#query_member_two").css('display','none');
					$("#imgQuery_member_two").css('display','none');
					$("#query_member_three").css('display','none');
					$("#imgQuery_member_three").css('display','none');
			    	classUserCollapse($("#myInClassUser"),$("#myInClassUserBtn"));
			    });
			    $("#myOutClassUserBtn").on("click",function(){
					$("#query_member_one").css('display','none');
					$("#imgQuery_member_one").css('display','none');
					$("#query_member_two").css('display','block');
					$("#imgQuery_member_two").css('display','block');
					$("#query_member_three").css('display','none');
					$("#imgQuery_member_three").css('display','none');
			    	classUserCollapse($("#myOutClassUser"),$("#myOutClassUserBtn"));
			    });
			    $("#noJoinClassUserBtn").on("click",function(){
			    	$("#query_member_one").css('display','none');
			    	$("#imgQuery_member_one").css('display','none');
			    	$("#query_member_two").css('display','none');
					$("#imgQuery_member_two").css('display','none');
					$("#query_member_three").css('display','block');
					$("#imgQuery_member_three").css('display','block');
			    	classUserCollapse($("#noJoinClassUser"),$("#noJoinClassUserBtn"));
			    	
			    });
			    
			    /**
				 * 检索匹配到的组内成员
				 * 1:显示检索成员、隐藏未检索到成员、不调用后台、鼠标监听事件13为回车事件
				 * 2:id:query_member_one
				 * */
				$('#query_member_one').keyup(function(event){
					var outMember=$(this).val();
					if($.trim(outMember) == ''){
						$("#myInClassUser div[class='groupMemmbers_subScibeDel']").css('display','block');
					}
					$("#closeQuery_member_one").css('display','none');
					$("#imgQuery_member_one").css('display','block');
				//	if(outMember.length > 0){
				//		$(".closeQuery_member_one").css('display','block');
				//	}
					if(event.keyCode == 13){  
						//获得该div下所有的值
						$("#myInClassUser div[class='groupMemmbers_subScibeDel']").each(function(i){
							var names=$(this).attr("title");
							if(names.indexOf(outMember) < 0){
								//不包含进行隐藏
								$("#myInClassUser div[class='groupMemmbers_subScibeDel']:eq("+i+")").css('display','none');
							}else{
								$("#myInClassUser div[class='groupMemmbers_subScibeDel']:eq("+i+")").css('display','block');
							}
						});
						if($.trim(outMember) != ''){
							$("#closeQuery_member_one").css('display','block');
							$("#imgQuery_member_one").css('display','none');
							$("#myInClassUser").scrollTop(0);
						}
					}
					 
			    });  
				/**
				 * 检索匹配到的组外成员
				 * 1：显示检索成员、隐藏未检索到成员、不调用后台、鼠标监听事件13为回车事件
				 * 2：id：query_member_two
				 * */
				$('#query_member_two').keyup(function(event){  
					var inMember=$(this).val();
					if($.trim(inMember) == ''){
						$("#myOutClassUser div[class='groupMemmbers_subScribe']").css('display','block');
					}
					$("#closeQuery_member_two").css('display','none');
					$("#imgQuery_member_two").css('display','block');
				//	if(inMember.length > 0){
				//		$(".closeQuery_member_two").css('display','block');
				//	}
					if(event.keyCode == 13){  
						//获得该div下所有的值
						$("#myOutClassUser div[class='groupMemmbers_subScribe']").each(function(i){
							var names=$(this).attr("title");
							if(names.indexOf(inMember) < 0){
								//不包含进行隐藏
								$("#myOutClassUser div[class='groupMemmbers_subScribe']:eq("+i+")").css('display','none');
							}else{
								$("#myOutClassUser div[class='groupMemmbers_subScribe']:eq("+i+")").css('display','block');
							}
						});
						if($.trim(inMember) != ''){
							$("#imgQuery_member_two").css('display','none');
							$("#closeQuery_member_two").css('display','block');
							$("#myInClassUser").scrollTop(0);
						}
					}
			    });
				
				$('#query_member_three').keyup(function(event){  
					var inMember=$(this).val();
					if($.trim(inMember) == ''){
						$("#noJoinClassUser div[class='groupMemmbers_subScribe']").css('display','block');
					}
					$("#closeQuery_member_three").css('display','none');
					$("#imgQuery_member_three").css('display','block');
					//	if(inMember.length > 0){
					//		$(".closeQuery_member_two").css('display','block');
					//	}
					if(event.keyCode == 13){  
						//获得该div下所有的值
						$("#noJoinClassUser div[class='groupMemmbers_subScribe']").each(function(i){
							var names=$(this).attr("useremail");
							if(names.indexOf(inMember) < 0){
								//不包含进行隐藏
								$(this).css('display','none');
							}else{
								$(this).css('display','block');
							}
						});
						if($.trim(inMember) != ''){
							$("#imgQuery_member_three").css('display','none');
							$("#closeQuery_member_three").css('display','block');
							$("#myInClassUser").scrollTop(0);
						}
					}
				});
				/**
				 * 清空事件
				 * */
				$('#closeQuery_member_one').on("click",function(){ 
					$("#query_member_one").val('');
					$("#myInClassUser div[class='groupMemmbers_subScibeDel']").css('display','block');
					$(this).css('display','none');
					$("#imgQuery_member_one").css('display','block');
			    });
				$('#closeQuery_member_two').on("click",function(){
					$("#query_member_two").val('');
					$("#myOutClassUser div[class='groupMemmbers_subScribe']").css('display','block');
					$(this).css('display','none');
					$("#imgQuery_member_two").css('display','block');
				});	
				$('#closeQuery_member_three').on("click",function(){
					$("#query_member_three").val('');
					$("#noJoinClassUser div[class='groupMemmbers_subScribe']").css('display','block');
					$(this).css('display','none');
					$("#imgQuery_member_three").css('display','block');
				});	
				/**
				 * 绑定查询事件
				 * */
				$('#imgQuery_member_one').on("click",function(){ 
					var outMember=$("#query_member_one").val();
					if($.trim(outMember) == ''){
						return;
					}
					$("#myInClassUser div[class='groupMemmbers_subScibeDel']").each(function(i){
						var names=$(this).attr("title");
						if(names.indexOf(outMember) < 0){
							//不包含进行隐藏
							$("#myInClassUser div[class='groupMemmbers_subScibeDel']:eq("+i+")").css('display','none');
						}else{
							$("#myInClassUser div[class='groupMemmbers_subScibeDel']:eq("+i+")").css('display','block');
						}
					});
					$(this).css('display','none');
					$("#closeQuery_member_one").css('display','block');
					$("#myInClassUser").scrollTop(0);
			    });
				$('#imgQuery_member_two').on("click",function(){
					var inMember=$("#query_member_two").val();
					if($.trim(inMember) == ''){
						return;
					}
					$("#myOutClassUser div[class='groupMemmbers_subScribe']").each(function(i){
						var names=$(this).attr("title");
						if(names.indexOf(inMember) < 0){
							//不包含进行隐藏
							$("#myOutClassUser div[class='groupMemmbers_subScribe']:eq("+i+")").css('display','none');
						}else{
							$("#myOutClassUser div[class='groupMemmbers_subScribe']:eq("+i+")").css('display','block');
						}
					});
					
					$(this).css('display','none');
					$("#closeQuery_member_two").css('display','block');
					$("#myInClassUser").scrollTop(0);
				});	
				
				$('#imgQuery_member_three').on("click",function(){
					var inMember=$("#query_member_three").val();
					if($.trim(inMember) == ''){
						return;
					}
					$("#noJoinClassUser div[class='groupMemmbers_subScribe']").each(function(i){
						var names=$(this).attr("useremail");
						if(names.indexOf(inMember) < 0){
							//不包含进行隐藏
							$(this).css('display','none');
						}else{
							$(this).css('display','block');
						}
					});
					
					$(this).css('display','none');
					$("#closeQuery_member_three").css('display','block');
					$("#myInClassUser").scrollTop(0);
				});	
	});

	requestUser();
}

/**  分类右键点击成员按钮 **/
$("#grouptype-context-menu").on("click","#groupMembers",function(){
	var owner = $(this).attr("owner");
	getMemberList(owner);
});

/**点击成员按钮 **/
$("#mainContentLeft").on("click","#groupMembers_",function(){
	getMemberList();
	
//		$.ajax({
//			url: $.appClient.generateUrl({ESDefault:'getCompanyUsersForGroupSetAndNotJoin'},'x'),
//			type:'POST',
//			data:{userid:window.userId,groupid:groupid,classId:classId,flag:flag,companyId:window.companyid},
//			success:function(data) {
//				
//			}
//		});
	
});

//点击邀请成员取消按钮
$('#mainContentLeft').on('click','#add_cancel_user_button',function(){
	getMemberList();
});

/**  成员折叠  **/
function classUserCollapse(target,spanTarget,isFirst){
	//刚进入的时候需要清空状态
	$("#query_member_one").val('');
	$("#query_member_two").val('');
	$("#query_member_three").val('');
	$("#closeQuery_member_one").css('display','none');
	$("#closeQuery_member_two").css('display','none');
	$("#closeQuery_member_three").css('display','none');

	$("#myInClassUser div[class='groupMemmbers_subScibeDel']").css('display','block');
	$("#myOutClassUser div[class='groupMemmbers_subScribe']").css('display','block');
	$("#noJoinClassUser div[class='groupMemmbers_subScribe']").css('display','block');
	//首次进入打开组内成员
	if(isFirst!=1 && target.css("display")=='block'){
		$("#imgQuery_member_two").css('display','none');
		$("#imgQuery_member_one").css('display','none');
		$("#imgQuery_member_three").css('display','none');
		$("#query_member_two").css('display','none');
		$("#query_member_one").css('display','none');
		$("#query_member_three").css('display','none');
		target.css('display','none');
		spanTarget.children("span").attr("class","glyphicon glyphicon-chevron-down");
		return;
	}
	if(isFirst == 1){
		$("#imgQuery_member_two").css('display','none');
		$("#query_member_two").css('display','none');
		$("#imgQuery_member_three").css('display','none');
		$("#query_member_three").css('display','none');
	}
	//再次关闭
	$("#myInClassUser").css('display','none');
	$("#myOutClassUser").css('display','none');
	$("#noJoinClassUser").css('display','none');
	$("#myInClassUserBtn span").attr("class","glyphicon glyphicon-chevron-down");
	$("#myOutClassUserBtn span").attr("class","glyphicon glyphicon-chevron-down");
	$("#noJoinClassUserBtn span").attr("class","glyphicon glyphicon-chevron-down");
	
	target.css('display','block');
	spanTarget.children("span").attr("class","glyphicon glyphicon-chevron-up");
}


//点击成员请出按钮
	$('#contentBody').on('click','#remove_class_user',function(){
		var addgroupuserids = "";
		var groupname = $('.main-left .active').attr("groupname");
		var flag = $('.main-left .active').attr('flag');
		var groupid = $('.main-left .active').attr('data-group-id');
		var data_idseq = $('.main-left .active').attr('data-idseq');
		var checkboxlength = $('#myInClassUser input:checked').length;
		if (checkboxlength == 0) {
			showBottomMsg("请至少选择一个要删除的成员!",2);
			return;
		}
		var deletegroupuserids="";
		var deletegroupuserusernames="" ;
		var deletegroupuserfullnames="" ;
		$('#myInClassUser input:checked').each(
				function(i) {
					var id = $('#myInClassUser input:checked:eq(' + i+ ')').parent().find('input');
					deletegroupuserids += id.val()+',';
					deletegroupuserusernames += id.attr("username").replace('@', '\\40')+',';
					deletegroupuserfullnames += id.attr("fullname")+',';
				});
					deletegroupuserids=deletegroupuserids.substring(0,deletegroupuserids.length-1);
					deletegroupuserusernames=deletegroupuserusernames.substring(0,deletegroupuserusernames.length-1);
					deletegroupuserfullnames=deletegroupuserfullnames.substring(0,deletegroupuserfullnames.length-1);
					var deletes = deletegroupuserids.split(",");
					for(var i=0;i<deletes.length;i++){
						if(deletes[i] == window.userId){
							//$('#myInClassUser [userid="'+deletes[i]+'"]').find('input').css('visibility','hidden');//visibility: hidden
							showBottomMsg("不能删除自己",2);
							return false;
						}
					}
				//	alert("删除的是自己。。。"+data_idseq);
				//	return ;
					$.ajax({
						url:$.appClient.generateUrl({ESDefault : 'resetGroup'},'x'),
						type:'POST',
						data:{companyId:window.companyid,username:window.userName,addgroupuserids:addgroupuserids,deletegroupuserids:deletegroupuserids,groupremark:"", groupname:encodeURI($.trim(groupname), "utf-8"), groupid:groupid, groupflag:flag, manageruserid:window.userId, changeusers:true, changeitems:false, fullname:encodeURI( window.fullName, "utf-8"),data_idseq:data_idseq},
						datatype:"json",
						success:function(data){
							var json = $.parseJSON(data);
							if(json.isOk){
								//$('#groupMembers').trigger('click');
								getMemberList();
								var arg = 'type=reset_group&groups='+flag+'&deletegroupusernames='+deletegroupuserusernames;
								remote.jsjac.chat.ofuserservice(arg, false) ;
								if(deletegroupuserusernames.length > 0){
									var deleteArray = deletegroupuserusernames.split(",");
									for(var d=0;d<deleteArray.length;d++){
										var dcontent = flag+"broadcast--removeuser"+$("#current_user_name").html()+"将您从["+groupname+"]分类中请出！" ;
										remote.jsjac.chat.sendMessage(dcontent, deleteArray[d]+"@"+remote.jsjac.domain);
									}
								}
								var content = groupname+"broadcast-deletegroupuser"+flag+";"+deletegroupuserids+";"+deletegroupuserfullnames ;
								remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
								$.WebIM.reloadGroupUsers(flag) ;
								
								//如果当前聊天窗口是当前操作的群组，就写入成员邀请、请出信息
								var $chatMain = $("#flyingchat .chat-main");
								var receiver = flag+"@broadcast."+remote.jsjac.domain;
								if (receiver == $chatMain.attr("receiver")) {
									var sender = $chatMain.attr("sender");
									var chatMainId = $chatMain.attr("id");
									$.WebIM.writeReceiveMessage(chatMainId, sender, '将 '+deletegroupuserfullnames.replaceAll(",", "、")+' 请出分类！', true);
								}
							} else {
								showBottomMsg("添加分类成员失败",3);
							}
						}
					});
	});

	

	

//点击成员加入

	$('#contentBody').on('click','#add_class_user',function(){
		var deletegroupuserids = "";
		var groupname = $('.main-left .active').attr("groupname");
		
		var flag = $('.main-left .active').attr('flag');
		var groupid = $('.main-left .active').attr('data-group-id');
		var checkboxlength = $('#myOutClassUser input:checked').length;
		if (checkboxlength == 0) {
			showBottomMsg("请至少选择一个要加入的成员!",2);
			return;
		}
		var addgroupuserids="";
		var addgroupusernames="" ;
		var addgroupfullnames="" ;
		$('#myOutClassUser input:checked').each(
				function(i) {
					var id = $('#myOutClassUser input:checked:eq(' + i+ ')').parent().find('input');
					addgroupuserids += id.val()+',';
					addgroupusernames += id.attr("username").replace('@', '\\40')+',';
					addgroupfullnames += id.attr("fullname")+',';
				});
				addgroupuserids=addgroupuserids.substring(0,addgroupuserids.length-1);
				addgroupusernames = addgroupusernames.substring(0,addgroupusernames.length-1);
				addgroupfullnames=addgroupfullnames.substring(0,addgroupfullnames.length-1);
				$.ajax({
					url:$.appClient.generateUrl({ESDefault : 'resetGroup'},'x'),
					type:'POST',
					data:{companyId:window.companyid,username:window.userName,addgroupuserids:addgroupuserids,deletegroupuserids:deletegroupuserids,groupremark:"", groupname:encodeURI($.trim(groupname), "utf-8"), groupid:groupid, groupflag:flag, manageruserid:window.userId, changeusers:true, changeitems:false, fullname:encodeURI( window.fullName, "utf-8")},
					datatype:"json",
					success:function(data){
						var json = $.parseJSON(data);
						if(json.isOk){
							//$('#groupMembers').trigger('click');
							getMemberList();
							var arg = 'type=reset_group&groups='+flag+'&addgroupusernames='+addgroupusernames;
							remote.jsjac.chat.ofuserservice(arg, false) ;
							var content = groupname+"broadcast-addgroup"+flag+";"+$("#current_user_name").html()+"邀请您加入分类【"+groupname+"】！"+addgroupuserids+";"+addgroupfullnames ;
							remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
							$.WebIM.reloadGroupUsers(flag) ;
							
							//如果当前聊天窗口是当前操作的群组，就写入成员邀请、请出信息
							var $chatMain = $("#flyingchat .chat-main");
							var receiver = flag+"@broadcast."+remote.jsjac.domain;
							if (receiver == $chatMain.attr("receiver")) {
								var sender = $chatMain.attr("sender");
								var chatMainId = $chatMain.attr("id");
								$.WebIM.writeReceiveMessage(chatMainId, sender, '邀请 '+addgroupfullnames.replaceAll(",", "、")+' 加入分类！', true);
							}
						} else {
							showBottomMsg("添加分类成员失败",3);
						}
					}
				});
	});

//点击关注成员
function payMember(){
	$('#class_user_list').on('click','#add_user_for_email',function(){
		// 设置头部
		var jsonHeading = '{"type":"'+$(this).attr('id')+'"}';
		$("#content-heading").html(template('requestUser_templete'));
		$("#contentBody").html(template("requestUserhtml_templete"));
		var classId = $('.main-left .active').attr('data-class-id');
		var groupid = $('.main-left .active').attr('data-group-id');
		$.ajax({
			url:$.appClient.generateUrl({ESUserInfo : 'openUrl'},'x'),
			type:'POST',
			data:{classId:classId,groupid:groupid},
			datatype:"json",
			success:function(data){
				var success = $.parseJSON(data)
				if(success.success){
					$("#fe_text").val(success.url);
					 copyUrl();
				}else{
					showBottomMsg('出错了',3);
				}
			}
		});
		//动态添加行
		dynamicAddRow();
		//显示滚动条
		$("#content-list").perfectScrollbar('update');
	});
	
}

//申请加入分类
function addClassified(){
	$('#apply_for_group').on('click',function(){
		var groupid = $('.main-left .active').attr('data-group-id');
		var groupflag = $('.main-left .active').attr('flag');
		applyGroup(groupid,groupflag);
		
	});
}


//用户移除和信息
function alertUserInfo(){
//	$("#class_user_list").perfectScrollbar();
	 $("#content-list .ps-scrollbar-y-rail").css("display","none");
	 $("#myInClassUser").perfectScrollbar();
	 $("#myOutClassUser").perfectScrollbar();
	 $("#noJoinClassUser").perfectScrollbar();
	 payMember();
	 addClassified();
}
//申请加入分类的方法

function applyGroup(groupid,groupflag){
	//此方法之前是为了找管理员所以叫这个名字，现在改为不能重复点击了
	$.ajax({
		url:$.appClient.generateUrl({ESDefault : 'getGroupUserIsAdminInfo'},'x'),
		type:'POST',
		data:{groupid:groupid,groupflag:groupflag},
		datatype:"json",
		success:function(data){
			var json = $.parseJSON(data)
			if(json.isOk){
				$.WebIM.applyIntoGroup(json.username,window.userId,groupflag,"");
			}else{
				showBottomMsg("已发送至该小组管理员，请耐心等待！", 1);
			}
		}
	});
}
function outGroup(groupid,groupname,flag,data_idseq,groupOwner){
	//删除当前分类中的自己
	$.ajax({
		url:$.appClient.generateUrl({ESDefault : 'outGroup'},'x'),
		type:'POST',
		data:{companyId:window.companyid,groupflag:flag,groupid:groupid,userid:window.userId,username:window.userName,fullname:encodeURI(window.fullName, "utf-8"),data_idseq:data_idseq,groupsOwnerId:groupOwner},
		datatype:"json",
		success:function(data){
			var json = $.parseJSON(data)
			if(json.isOk){
			//下面应该是发信息
				var arg = 'type=out_group&groups='+flag+'&username='+window.userName.replace('@', '\\40');
				remote.jsjac.chat.ofuserservice(arg, false) ;
				var content = [groupname, "broadcast-", flag, "<", $("#current_user_name").html(), "退出分类【"+groupname+"】!" ].join("");
				remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
				if($("#receiverusername").attr("receiver") == flag){
					$.WebIM.gotoUser('fyBot') ;
				}
				var classId = $('.main-left li.active').attr('data-class-id');
				var userId = window.userId;
				var status = $('.main-left li.active').attr('isstar');
				//重新加载左侧分类
				documentCenter.getClassList(userId,classId,status);
				//$("#settingUp").trigger("click");
				showBottomMsg("退出分类成功！",1);
			} else {
				showBottomMsg("退出分类失败！",3);
			}
		}
	});

}

////同意加入   20150727 暂停使用
//$('#main-container').on('click','#agreeThisApply',function(){
//	var thisplace = $(this);
//	var creator = $(this).attr('creator');
//	if(creator != window.userId){
//		showBottomMsg("您不是分类管理员，无法进行该操作！", 2);
//		return flase;
//	}
//	var userid = $(this).attr('userid');
//	var groupid = $(this).attr('groupid');
//	//判断该用户是否已是该小组成员
//	var groupname =  $(this).attr("groupname");
//	var groupFlag = $(this).attr('flag');
//	var username = $(this).attr('username');
//	var fullname = $(this).attr('fullname');
//	var addgroupuserids = userid;
//	jQuery.getJSON(window.onlinefilePath+'/rest/chat/getGroupUserByGroupId?callback=?', {'companyId':window.companyid,'username':window.userName,'groupid':groupid,'groupFlag':groupFlag,'applyuserid':userid},
//			function(json) {
//		if(json.all>0){
//			if(!json.isOk){
//				//判断该用户是否已是该小组成员
//				var deletegroupuserids ="";
//				jQuery.getJSON(window.onlinefilePath+'/rest/chat/resetGroup?callback=?', {'companyId':window.companyid, 'username':username, 'addgroupuserids':addgroupuserids, 'deletegroupuserids':deletegroupuserids,groupremark:"", 'groupname':encodeURI($.trim(groupname), "utf-8"), groupid:groupid, groupflag:groupFlag, manageruserid:window.userId, changeusers:true, changeitems:false, "fullname":encodeURI( window.fullName, "utf-8"),'isApplied':'true'},
//						function(json) {
//					if(json.isOk){
//						var arg = 'type=reset_group&groups='+groupFlag+'&addgroupusernames='+username.replace('@', '\\40')+',';
//						$('#groupMembers').trigger('click');
//						remote.jsjac.chat.ofuserservice(arg, false) ;
//						showBottomMsg("您已同意"+fullname+"加入本分类！", 1);
//						//同意之后给用户发送消息通知
//						var dcontent = window.fullName+"接受了您加入该分类的请求,您现在可以在【"+groupname+"】分类发言了;)！" ;
//						var name = username.replace('@', '\\40')
//						remote.jsjac.chat.sendMessage(dcontent,name+"@"+remote.jsjac.domain);
////						$.WebIM.noticeApplyerIntoGroup(userid);
//						$(thisplace).parent().parent().html('您已同意 【'+fullname+'】加入分类');
//						
//						var content = [groupname, "broadcast-", groupFlag, "<", "同意 【"+fullname+"】加入分类【"+groupname+"】！" ].join("");
//						remote.jsjac.chat.sendMessage(content, groupFlag+"@broadcast."+remote.jsjac.domain);
//						$.WebIM.reloadGroupUsers(groupFlag) ;
//					} else {
//						showBottomMsg("添加分类成员失败",3);
//					}
//				});
//			}else{
//				showBottomMsg("改用户已在分类中!", 2);
//			}
//		} else {
//			showBottomMsg("添加分类成员失败",2);
//		}
//	});
//	
//	
//});
//拒绝请求   20150727 暂停使用
//$('#main-container').on('click','#passThisApply',function(){
//	var thisplace = $(this);
//	var userid = $(this).attr('userid');
//	var creator = $(this).attr('creator');
//	if(creator != window.userId){
//		showBottomMsg("您不是分类管理员，无法进行该操作！", 2);
//		return flase;
//	}
//	//判断该用户是否已是该小组成员
//	var groupname =  $(this).attr("groupname");
//	var groupFlag = $(this).attr('flag');
//	var groupid =  $(this).attr('groupid');
//	var username = $(this).attr('username');
//	var fullname = $(this).attr('fullname');
//	var addgroupuserids = userid;
//	jQuery.getJSON(window.onlinefilePath+'/rest/chat/getGroupUserByGroupId?callback=?', {'companyId':window.companyid,'username':window.userName,'groupid':groupid,'groupFlag':groupFlag,'applyuserid':userid},
//			function(json) {
////		if(json.all>0){
//			if(!json.isOk){
//				//不在组中，回复说拒绝
//				var dcontent = window.fullName+"拒绝了您加入【"+groupname+"】分类的申请！" ;
//				var name = username.replace('@', '\\40')
//				remote.jsjac.chat.sendMessage(dcontent,name+"@"+remote.jsjac.domain);
//				showBottomMsg("您已拒绝该成员加入分类!", 1);
////				$("#main-container #passThisApply").parent().html('您已拒绝该成员加入分组');
//				$(thisplace).parent().parent().html('您已拒绝【'+fullname+'】加入【'+groupname+'】分类');
////				alert(thisplace.parent().html());
//			}else{
//				showBottomMsg("该用户已在分类中，请在成员管理中将其请出团队!", 2);
//			}
////		} else {
////			showBottomMsg("添加分类成员失败");
////		}
//	});
//});

//点击空白隐藏
$(document).mouseup(function(e){
	  var _con = $('#ClassUserInfo');   
	  if(!_con.is(e.target) && _con.has(e.target).length === 0){
		  $('#ClassUserInfo').remove();
	  }
});
function dynamicAddRow(){
	$('#request_frame').off("click","#add").on("click","#add",function(){
		var NUM = $('#add').prev().attr("id");
		var name = $('#add').prev().find('input').attr('name');
		var divNum = NUM.substr(NUM.length-1,NUM.length);
		divNum++;
		$("#request_frame #divNum").val(divNum) ;
		var teamhtml = $(this).prev().html();
		teamhtml = '<div id="team_'+divNum+'" ng-repeat="member in invite_members" class="ng-scope">'+teamhtml+'</div>';
		teamhtml = teamhtml.replace(name, "email"+divNum);
		$(this).before(teamhtml);
		//删除按钮显示 
		if($('#request_frame [name^="email"]').length > 1){
			$('#request_frame .delete').css('display','block');
		}
	});
	//删除动态增加的行
	$('#request_frame').off("click",'.delete').on('click','.delete',function(){
		$(this).parent().parent().remove();
		if($('#request_frame [name^="email"]').length == 1){
			$('#request_frame .delete').css('display','none');
		}
	});
	//发送邀请邮件
	$('#request_frame').off("click",'#add_user_button').on('click','#add_user_button',function(){
		var flag = true;
		var index = 0;
		var classId = $('.main-left li.active').attr('data-class-id');
		var groupname = $('.main-left li.active').attr("groupname");
		var groupflag = $('.main-left li.active').attr('flag');
		var groupId = $(".main-left li.active").attr("data-group-id");
		var username = $("#applyUserForm input[name^='email']").each(function(){
			var name =  $(this).attr("name");
			var num = name.substr(name.length-1,name.length);
		    if($.trim($(this).val())==''){
		    	showBottomMsg("请填写邮箱",2);
		 		flag = false;
		 	} else if(emailaddressZZ.test($(this).val())==false){
		 		showBottomMsg($(this).val()+"邮箱格式不正确，请修改，例如:admin@flyingsoft.com",2);
		 		flag = false;
		 	}else if(window.userName == $(this).val()){
		 		showBottomMsg("对不起,不能邀请自己",2);
		 		flag = false;
		 	}else if(emailaddressZZ.test($(this).val())==true){
		 		index++
		 	}
		});	
		var message = $('#request_frame #message_to_email').val();
		var d = $("#applyUserForm").serialize();
		d +="&message="+message ;
		d +="&classId="+classId ;
		d +="&groupId="+groupId ;
		d +="&groupflag="+groupflag ;
		d +="&username="+window.userName ;
		d +="&fullname="+encodeURI( window.fullName, "utf-8") ;
		
		if(!flag){
			return false;
		}

		if(index ==0){
			showBottomMsg("请至少填写一个邮箱",2);
			return false;
		}
		
		$.ajax({
			url:$.appClient.generateUrl({ESUserInfo : 'do_addUser'},'x'),
			type:'POST',
			data:{d:d},
			datatype:"json",
			async: false,
			success:function(data){
				var json = $.parseJSON(data)
				var newUsers = json.newUsers;  //新成员
				var systemUsers = json.systemUsers; //系统成员  但不是该团队
				var companyUsers = json.companyUsers;//公司成员  但在组外
				var classUsers = json.classUsers;  //组内成员
				
				//发送邀请
				var addgroupuserids="";
				var addgroupusernames="" ;
				var addgroupfullnames="" ;
				var isSended = false;
				for(var i=0;companyUsers && i<companyUsers.length;i++){
					addgroupusernames += companyUsers[i].USERNAME.replace('@', '\\40')+',';
					addgroupuserids += companyUsers[i].ID+',';
					addgroupfullnames += companyUsers[i].FULLNAME+',';
					isSended = true;
					
					//如果当前聊天窗口是当前操作的群组，就写入成员邀请、请出信息
					var $chatMain = $("#flyingchat .chat-main");
					var receiver = groupflag+"@broadcast."+remote.jsjac.domain;
					if (receiver == $chatMain.attr("receiver")) {
						var sender = $chatMain.attr("sender");
						var chatMainId = $chatMain.attr("id");
						var usernames = addgroupfullnames;
						usernames=usernames.substring(0,usernames.length-1);
						$.WebIM.writeReceiveMessage(chatMainId, sender, '邀请 '+usernames.replaceAll(",", "、")+' 加入分类！', true);
					}
				}
				for(var i=0;systemUsers && i<systemUsers.length;i++){
					var dcontent = "addtocompany-"+window.fullName+"邀请您加入"+window.companyName+"团队" ;
					var username = systemUsers[i].USERNAME.replace('@', '\\40');
					remote.jsjac.chat.sendMessage(dcontent,username+"@"+remote.jsjac.domain);
					
					addgroupusernames += username+',';
					addgroupuserids += systemUsers[i].ID+',';
					addgroupfullnames += systemUsers[i].FULLNAME+',';
					isSended = true;
				}
				//如果当前聊天窗口是当前操作的群组，就写入成员邀请、请出信息
				var $chatMain = $("#flyingchat .chat-main");
				var receiver = flag+"@broadcast."+remote.jsjac.domain;
				if (receiver == $chatMain.attr("receiver")) {
					var sender = $chatMain.attr("sender");
					var chatMainId = $chatMain.attr("id");
					$.WebIM.writeReceiveMessage(chatMainId, sender, '邀请 '+addgroupfullnames.replaceAll(",", "、")+' 加入分类！', true);
				}
				if(isSended){
					addgroupuserids=addgroupuserids.substring(0,addgroupuserids.length-1);
					addgroupusernames = addgroupusernames.substring(0,addgroupusernames.length-1);
					addgroupfullnames=addgroupfullnames.substring(0,addgroupfullnames.length-1);
					var arg = 'type=reset_group&groups='+groupflag+'&addgroupusernames='+addgroupusernames;
					remote.jsjac.chat.ofuserservice(arg, false) ;
					var content = groupname+"broadcast-addgroup"+groupflag+";"+$("#current_user_name").html()+"邀请您加入分类【"+groupname+"】！"+addgroupuserids+";"+addgroupfullnames ;
					remote.jsjac.chat.sendMessage(content, groupflag+"@broadcast."+remote.jsjac.domain);
					$.WebIM.reloadGroupUsers(groupflag) ;
				}

				showBottomMsg('邀请成功，飞扬小强正在快速的奔跑，逐一发送邀请邮件（消息），请稍等！',2);
			}
			
		});
		//更新聊天窗口的用户界面
		updatechatusers();
	});
	
}

function updatechatusers(){
	var data = {'companyId':window.companyid,'username':window.userName,'userid':window.userId} ;
		var nowGroup = $('.main-left li.active') ;
		data.flag = nowGroup.attr('flag');
		data.classId =  nowGroup.attr('data-class-id');
		data.groupid =  nowGroup.attr('data-group-id');
		var url = window.onlinefilePath+'/rest/chat/getCompanyUsersForGroupSetAndNotJoin/'+data.companyId+'/'+data.username+'?callback=?';
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
	function(json) {
		$('#flyingchatusers').find("div[childdivid='userListItem-nojoin']").remove();
		$('#flyingchatusers').find("#userListItem-nojoin").remove();
		var htmlNoJoin = template('chatusersNojoin_templete',json);
		$('#userListItem-fyBot').before(htmlNoJoin);
	});
}

//复制链接
function copyUrl(){
		var swf = $("#openUrl").attr('swftext');
		var clip = new ZeroClipboard($("#openUrl"), {
			moviePath: swf
		});
		
		clip.on('load', function (client) {
			debugstr("Flash movie loaded and ready.");
		});
		
		clip.on('noFlash', function (client) {
			$(".demo-area").hide();
			debugstr("Your browser has no Flash.");
		});
		
		clip.on('wrongFlash', function (client, args) {
			$(".demo-area").hide();
			debugstr("Flash 10.0.0+ is required but you are running Flash " + args.flashVersion.replace(/,/g, "."));
		});
		
		clip.on('complete', function (client, args) {
		showBottomMsg("已复制到黏贴板","1");
		debugstr("Copied text to clipboard: " + args.text);
		});
}
//一键复制功能  只兼容ie不能用了
function buttonCopyCode(txt){  
    if (window.clipboardData) {  
        window.clipboardData.clearData();  
        window.clipboardData.setData("Text", txt);  
    } else if (navigator.userAgent.indexOf("Opera") != -1) {  
        window.location = txt;  
    } else if (window.netscape) {  
        try {  
            netscape.security.PrivilegeManager  
                    .enablePrivilege("UniversalXPConnect");  
        } catch (e) {  
            alert("你使用的FireFox浏览器,复制功能被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车。\n然后将“signed.applets.codebase_principal_support”双击，设置为“true”");  
            return;  
        }  
        var clip = Components.classes['@mozilla.org/widget/clipboard;1']  
                .createInstance(Components.interfaces.nsIClipboard);  
        if (!clip)  
            return;  
        var trans = Components.classes['@mozilla.org/widget/transferable;1']  
                .createInstance(Components.interfaces.nsITransferable);  
        if (!trans)  
            return;  
        trans.addDataFlavor('text/unicode');  
        var str = new Object();  
        var len = new Object();  
        var str = Components.classes["@mozilla.org/supports-string;1"]  
                .createInstance(Components.interfaces.nsISupportsString);  
        var copytext = txt;  
        str.data = copytext;  
        trans.setTransferData("text/unicode", str, copytext.length * 2);  
        var clipid = Components.interfaces.nsIClipboard;  
        if (!clip)  
            return false;  
        clip.setData(trans, null, clipid.kGlobalClipboard);  
    }  
    else{  
        alert("你的浏览器不支持一键复制功能");  
        return;  
    }  
    alert("复制成功");  
}  

/**
 * liumingchao  ↑
 */
/** fywindow start **/
var fywindow = function (config) {
	config = config || {};
	if (typeof config === 'string' || config.nodeType === 1) {
		config = {content: config, fixed: !_isMobile};
	}
	var json = {};
	if(typeof config.title != 'undefined'){
		json.title = config.title ;
		json.hastitle = 1 ;
	} else {
		json.hastitle = 0 ;
	}
	if(typeof config.content != 'undefined'){
		json.content = config.content ;
	} else {
		json.content = "" ;
	}
	if(typeof config.ok != 'undefined'){
		json.ok = true ;
	} else {
		json.ok = false ;
	}
	if(json.ok){
		if(typeof config.okValue != 'undefined'){
			json.okValue = config.okValue ;
		} else {
			json.okValue = "确定" ;
		}
	}
	if(typeof config.cancel != 'undefined'){
		json.cancel = true ;
	} else {
		json.cancel = false ;
	}
	if(json.cancel){
		if(typeof config.cancelValue != 'undefined'){
			json.cancelValue = config.cancelValue ;
		} else {
			json.cancelValue = "取消" ;
		}
	}
	json.bbar = true ;
	if(!json.ok && !json.cancel){
		json.bbar = false ;
	}
	if(typeof config.width != 'undefined'){
		json.width = config.width ;
	} else {
		json.width = 600 ;
	}
	if(typeof config.height != 'undefined'){
		json.height = config.height ;
	} else {
		json.height = 200 ;
	}
	if(typeof config.style != 'undefined'){
		json.style = config.style ;
	} else {
		json.style = "" ;
	}
	
	var $panel = $("#action-content-panel");
	if ($panel.get(0)) {
		$panel.hide(300);
		$panel.remove();
		if(!$("#art_panel_maskLayer").hasClass("hidden")) {
			$("#art_panel_maskLayer").addClass("hidden")
		}
	}
	// 添加蒙板层
	$("#art_panel_maskLayer").removeClass("hidden");
	$("#searchFileInputId").css("background-color","#ccc");
	var html = template('art_panel_template', json);
	$('#main-container').append(html);
	var $panel = $("#action-content-panel");
	// 设置右侧弹出窗口高度顶满
	var panelH = document.documentElement.clientHeight-65;
	$panel.height(panelH);
	$panel.find(".panel-body").css({"width":json.width, "height":json.height}) ;
	$panel.css({top:10});
	$panel.show(300);
	if(typeof config.ok != 'undefined'){
		$("#action-content-panel").on("click", "#fywindowbbar-ok", function(){
			if(false != config.ok.call(this, window)){
				var $panel = $("#action-content-panel");
				if ($panel.length != 0) {
					$panel.hide(300);
					$panel.remove();
					if(!$("#art_panel_maskLayer").hasClass("hidden")) {
						$("#art_panel_maskLayer").addClass("hidden")
					}
				}
			}
		});
	}
	if(json.cancel){
		$("#action-content-panel").on("click", "#fywindowbbar-cancel", function(){
			var $panel = $("#action-content-panel");
			if ($panel.length != 0) {
				$panel.hide(300);
				$panel.remove();
				if(!$("#art_panel_maskLayer").hasClass("hidden")) {
					$("#art_panel_maskLayer").addClass("hidden")
				}
			}
		});
	}
}
window.fywindow = $.fywindow = fywindow;
/** fywindow end **/

/** fymenu start **/
jQuery.fn.fymenu = function (config) {
	
	if ($("#menu_panel").length > 0 && !config.menuId) {
		$("#menu_panel").remove();
		return;
	}
	config = config || {};
	
	if (typeof config === 'string' || config.nodeType === 1) {
		config = {content: config, fixed: !_isMobile};
	}
	var json = {};
	json.header = config.header;
	json.data = config.data;
	json.menuId = config.menuId || "menu_panel";
	var html = template('menu_template', json);
	$('#main-container').append(html);
//	$('#menu_items_scroller').perfectScrollbar();	
	 var $menu = $("#"+json.menuId);
	 $menu.find("#menu-close").click(function(){
		 $menu.remove();
	 })
	 $menu.on("mouseover","#file_openlevel_set",function(){
				 $(this).find("ul").css("display","block");
			 })
			 .on("mouseout","#file_openlevel_set",function(){
				 $(this).find("ul").css("display","none");
			 });
	
	// 定位
	var tmpH = -55;
    var tmpW = $(this).width() - 210;
    var offset = $(this).offset();  
   
    $menu.css({ top: offset.top + tmpH + "px", left: offset.left + tmpW });
   // alert($("#menu_panel").top);
}
/** fymenu end **/

/**
 * 显示或隐藏 
 */
function showMainContentLeftFooter(status,isfileinfo) {
	var bodyHeight = document.documentElement.clientHeight;
	var contentFooterHeight = 45;
	if(1==isfileinfo){
		$("#content-list4FileInfo").height(bodyHeight-220+contentFooterHeight);
		if (status) {
			$("#content-footer4FileInfo").removeClass('hidden');
		} else {
			$("#content-footer4FileInfo").addClass('hidden');
			if ($("#content-list4FileInfo").find("div.ps-scrollbar-x-rail").length > 0) {
				$("#content-list4FileInfo").perfectScrollbar('update');
			}
		}
	}else{
		if (status) {
			$("#content-footer").removeClass('hidden');
			$("#content-list").height(bodyHeight-220);
		} else {
			$("#content-footer").addClass('hidden');
			$("#content-list").height(bodyHeight-220 + contentFooterHeight);
			if ($("#content-list").find("div.ps-scrollbar-x-rail").length > 0) {
				$("#content-list").perfectScrollbar('update');
			}
		}
	}
}

$("#usersmanager").on("click", function(){
	$.ajax({
	    url : $.appClient.generateUrl({ESUserInfo : 'toUserList'},'x'),
	    success:function(data){
	    	$.fywindow({
		    	title:'用户管理',
				width: 800,
				height: 450,
			    content:data
		    });
		  }
	});
});

$("#about").on("click",function(){
	window.open("/Default/about.html"); 
});

/**
 * 点击版本框显示或隐藏历史版本列表
 */
$("#main-container").on("click", "#contentBody4FileInfo a.showHistoryVersions", function(){
	var $file_preview_history_version = $("#file_preview_history_version");
	if($file_preview_history_version.hasClass("hidden")) {
		$file_preview_history_version.removeClass("hidden");
	} else {
		$file_preview_history_version.addClass("hidden");
	}
	$("#content-list4FileInfo").perfectScrollbar('update');
});

$("#main-container").on("click", "#filecontent a.showHistoryVersions", function(){
	var $file_preview_history_version = $("#filecontent #file_preview_history_version");
	if($file_preview_history_version.hasClass("hidden")) {
		$file_preview_history_version.removeClass("hidden");
	} else {
		$file_preview_history_version.addClass("hidden");
	}
	$("#filecontent").perfectScrollbar('update');
});

/** 分享到 start**/
// 我的文档逻辑变更后   分享到功能重新处理  只有我的文档有分享到功能  wangwenshuo 20151117
function shareToFolderBind() {
	/**
	 * 上传文件，分享到后面的下拉框点击事件
	 */
	$("#shareToMainDivId").on("click",".shareFolderPathBtnId",function(){
//		alert($(this).is(":hidden"));
		if($(this).parent().find(".shareFolderPathDivId").is(":hidden")){ //如果是隐藏状态
			$("#shareToMainDivId .shareFolderPathDivId").hide();
		}
		$(this).parent().find(".shareFolderPathDivId").toggle();
		if(!$(this).parent().find(".shareFolderPathDivId").is(":hidden")){ //如果是隐藏状态 就不发送获取文件夹请求
			$(this).parent().find(".shareFolderCrumbs").jFolderCrumb({data:[{"id":"1", "name":"全部分类", "idSeq":"1."}],clickFunName:"forwardFolderForShare"});
			getForderPathList($(this).parent().find(".share_folder_list"),1);
		}
	});

	/**
	 * 选择好文件夹后的确定事件
	 */
	/*$("#chooseFolder").on("click",function(){*/
	$("#shareToMainDivId").on("click",".chooseShareFolderAId",function(){
		chooseFolderClick($(this).parent().parent());
	});
	
	/** 关闭选择文件夹窗口  **/
	$("#shareToMainDivId").on("click",".closeChooseShareFolderBtnId",function(){
		$(this).parent().parent().find(".shareFolderPathDivId").hide();
	});
	
	/**添加一个分享位置*/
	$("#addSharePath").on("click",function(){
		//数量限制
		/*if($("#shareToMainDivId .sharePathClass").length>4){
			showBottomMsg("最多添加5个分享位置","2");
			return;
		}*/
//		var teamhtml = $(this).parent().prev().children().first().html();
//		teamhtml = '<div class="sharePathClass">'+teamhtml+'</div>';
//		$("#shareToMainDivId").append(teamhtml);
//		var last = $(this).parent().prev().children().last();
//		last.find(".shareFolderPathBtnId").attr("folderId","0");
//		last.find(".shareFolderShowSpanId").text("请选择：");
		var teamhtml = ''+
		'<div class="sharePathClass"  style="margin-top:15px;">'+
			'<span class="shareSpan">分享到：</span> \r\n'+
			 '<button class="shareFolderPathBtnId chooseUploadFolderBtn btn btn-default dropdown-toggle" type="button" aria-expanded="true" folderId="0" folderName="">'+
				'<span class="shareFolderShowSpanId">请选择：</span>'+
				'<span class="caret uploadChooseSpan"></span>'+
			  '</button> \r\n'+
			'<a class="deleteSharePath hidden">删除 </a>'+
			'<div class="shareFolderPathDivId uploadChooseFolderDiv">'+
				'<div class="shareFolderCrumbs btn-group btn-breadcrumb"></div>'+
				'<div class = "share_folder_list forderListsForUpload"></div>'+
				'<button type="button" class="chooseShareFolderAId btn btn-success chooseFolderBtnCls">确定</button>'+
				'<button type="button" class="closeChooseShareFolderBtnId btn btn-default closeChooseFolderBtnCls">取消</button>'+
			'</div>'+
		'</div>';
		$("#shareToMainDivId").append(teamhtml);
		$("#shareToMainDivId .deleteSharePath").removeClass("hidden");
	});
	
		/**删除一个分享位置*/
	$("#shareToMainDivId").on("click",".deleteSharePath",function(){
		$(this).parent().remove();
		return false;
	});
	
	/** 关闭 */
	$("#shareCancelBtnId").on("click", function(){
		closeContentPanel();
	});
	
	/** 确定分享到 */
	$("#shareBtnId").on("click", function(){
		
		var folderIds = ",";
		var folderNames = "";
		var isRepeat = false;
		$("#shareToMainDivId .shareFolderPathBtnId").each(function(){
			var folderId = $(this).attr("folderId")
			if ("0" != folderId) {
				if(folderIds.indexOf(","+folderId+",")>-1){
					isRepeat = true;
				}
				folderIds += folderId+",";
				folderNames += $(this).attr("folderName")+",";
			}
		});
		
		if (folderIds.length<2) {
			showBottomMsg("请选择分享到的路径","3");
			return false;
		}
		if (isRepeat) {
			showBottomMsg("分享路径重复，请重新选择","3");
			return false;
		}
		
		folderIds = folderIds.substring(1,folderIds.length-1);
		folderNames = folderNames.substring(0,folderNames.length-1);
//		var folderId = $("#shareFolderPathBtnId").attr("folderId");
//		var folderName = $("#shareFolderPathBtnId").attr("folderName");
		var itemId = $("#shareToMainDivId").attr("itemId");
		var $fileItem = $("#"+itemId);
		var filetitle = $fileItem.attr("filetitle");
		var classId = $fileItem.attr("folderid");
		var fileId = $fileItem.attr("fileId");
		var fileName = $fileItem.attr("fileName");
		var fileType = $fileItem.attr("fileType");
		var version = $fileItem.attr("version");
		var idSeq = $fileItem.attr("idseq");
		
		var fromCompanyId = "user_"+window.userId;  //实际为用户我的文档文件
		documentCenter.fileShareTo(fromCompanyId,fileId,g_companyId,folderIds,folderNames);
		return false;
	});
}

function chooseFolderClick($ppItem){
	var $folderActiveObj = $ppItem.find("share_folder_list .folder-item.active");
	var folderId = $folderActiveObj.attr("data-id");
	var folderIdSeq = $folderActiveObj.attr("data-idSeq");
	var folderName = $folderActiveObj.attr("data-title");
	if(folderId != undefined && folderId!= "undefined"){
		$ppItem.find(".shareFolderPathBtnId").attr("folderId",folderId);
		$ppItem.find(".shareFolderPathBtnId").attr("idSeq",folderIdSeq);
		$ppItem.find(".shareFolderPathBtnId").attr("folderName",folderName);
		$ppItem.find(".shareFolderShowSpanId").text($folderActiveObj.text());
	}
	$ppItem.find(".shareFolderPathDivId").hide();
	$ppItem.find(".share_folder_list").html("");
}


/**
 * 文件夹但双击事件。
 */
function shareFolderAbindClick($folders){
	$folders.find(".folder-item").on("click",function(){
		$folders.find(".folder-item").removeClass("active");
		$(this).addClass("active");
		var folderId = $(this).attr("data-id");
		var folderIdSeq = $(this).attr("data-idseq");
		var folderName = $(this).attr("data-title");
		if(folderId != undefined && folderId!= "undefined"){
			$folders.parent().parent().find(".shareFolderPathBtnId").attr("folderId",folderId);
			$folders.parent().parent().find(".shareFolderPathBtnId").attr("idSeq",folderIdSeq);
			$folders.parent().parent().find(".shareFolderPathBtnId").attr("folderName",folderName);
			$folders.parent().parent().find(".shareFolderShowSpanId").text($(this).text());
		}
	}).on("dblclick",function(){
		var id = $(this).attr("data-id");
		var fileName = $(this).attr("data-title");
		documentCenter.setupFolderPathForShare(id, "shareFolderCrumbs","forwardFolderForShare",$folders.parent().parent().find(".shareFolderCrumbs"));
		getForderPathList($folders.parent().parent().find(".share_folder_list"),$(this).attr("data-id"));
	});
}

/**
 * 获取有上传权限的文件夹列表
 */
function getForderPathList($folders,folderId){
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFolderById?callback=?';
	var dataInput = {"companyId":g_companyId, "folderId":folderId, "userId":g_userId };
	var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
			function(json) {
				if((folderId==0 || folderId=="0")&&(json.size==0||json.size=="0")){
					$folders.html("<span style='color:#7E7E7E;'>亲，您还没有有权限的分类供您分享，请创建分类或者加入分类。</span>");
				}else{
					if(json.size!="0"){
						$folders.html(template('upload_choose_folder_template', json)).perfectScrollbar();
						shareFolderAbindClick($folders);
					}else{
						showBottomMsg("此文件夹没有子文件夹了！", "2");
					}
				}
	});
	
}

/** 分享到 end **/

/**
 * wangwenshuo 20151020 复制到  start
 */
function copyToFolderAction($fileItem) {
	menuClose(); //手动关闭文件操作 菜单
	
	var fileId = $fileItem.attr("fileid");
	var folderid = $fileItem.attr("folderid");
	var fileName = $fileItem.attr("filetitle");
	var isMyDocument = $fileItem.hasClass("myDocument");
	var file_copy_to_html = template("file_copy_to_template", {"fileId":fileId,"folderid":folderid,"myDocument": isMyDocument?"myDocument":""});
	// 添加蒙板层
	$("#art_panel_maskLayer").removeClass("hidden");
	$("#searchFileInputId").css("background-color","#ccc");
	//显示复制面板
	var html = template('file_copy_panel_template', {});
	$('#main-container').append(html);
	var $panel = $("#copy-action-content-panel");
	var h = window.innerHeight - 58;
	$panel.width("600").height(h);
	$panel.css({top:10});
	$panel.show(300);
	
	//添加内容
	$("#copy-action-content-panel .panel-heading").html(fileName);
	$("#copy-action-content-panel .panel-body").html(file_copy_to_html);
	
	/**
	 * 上传文件，分享到后面的下拉框点击事件
	 */
	$("#copyFolderPathBtnId").on("click",function(){
		$("#copyFolderPathDivId").toggle(100);
		$("#copyFolderCrumbs").jFolderCrumb({data:[{"id":"1", "name":"全部分类", "idSeq":"1."}],clickFunName:"forwardFolderForCopy"});
		getCopyForderPathList(1);
	});
	
	$("#copyOpenlevelId").on("click",function(){
		closeChooseCopyFolderPanl();
	});
	$("#copyOpenlevelUlId li a").on("click",function(){
		$("#copyOpenLeverTitleShowSpanId").text($(this).find("span").text());
		$("#copyOpenlevelId").attr("openlevel",$(this).find("span").attr("openlevel"));
	});
	
	/** 关闭 */
	$("#copyCancelBtnId").on("click", function(){
		closeCopyContentPanel();
	});
	
	/** 确定复制到 */
	$("#copyBtnId").on("click", function(){
		var folderId = $("#copyFolderPathBtnId").attr("folderId");
		var folderName = $("#copyFolderPathBtnId").attr("folderName");
		var fromFolderId = $("#copyToMainDivId").attr("folderid");
		if ("0" == folderId) {
			showBottomMsg("尚未选择目标路径，请选择！","3");
		} else if(folderId==fromFolderId){
			showBottomMsg("不能复制文件到源路径，请更换目标路径！","2");
		} else {
			var fileId = $("#copyToMainDivId").attr("fileId");
			var openlevel = $("#copyOpenlevelId").attr("openlevel");
			documentCenter.copyToFolderPath(fileId, folderName, folderId, openlevel);
		}
	});
	
	return false;
}

function forwardFolderForCopy(thisObj) {
	var $obj = $(thisObj);
	getCopyForderPathList($obj.attr("data-folder-id"));
	return false;
}

/**
 * 获取分类或者文件夹路径
 */
function getCopyForderPathList(folderId, isSetFoderPath){
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFolderById?callback=?';
	var dataInput = {"companyId":g_companyId, "folderId":folderId, "userId":g_userId };
	var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
	function(json) {
		if((folderId==0 || folderId=="0")&&(json.size==0||json.size=="0")){
			$('#copy_folder_list').html("<span style='color:#7E7E7E;'>亲，您还没有有权限的分类供您上传，您可以在“公开级别”选项中选择“私有文档”，待有权限后再分享到分类中。</span>");
		}else{
			if(json.size!="0"){
				$('#copy_folder_list').html(template('upload_choose_folder_template', json));
				if(isSetFoderPath=="1"){
					documentCenter.setupFolderPathForShare(folderId, "copyFolderCrumbs","forwardFolderForCopy");//可以使用share方法
				}
				copyFolderAbindClick();
			}else{
				showBottomMsg("此文件夹没有子文件夹了！", "2");
			}
		}
	});
}

/**
 * 文件夹单击、双击事件。
 */
function copyFolderAbindClick(){
	$("#copy_folder_list .folder-item").on("click",function(){
		$("#copy_folder_list").find(".folder-item").removeClass("active");
		$(this).addClass("active");
		
		var folderId = $(this).attr("data-id");
		var folderIdSeq = $(this).attr("data-idseq");
		var folderName = $(this).attr("data-title");
		if(folderId != undefined && folderId!= "undefined"){
			$("#copyFolderPathBtnId").attr("folderId",folderId);
			$("#copyFolderPathBtnId").attr("idSeq",folderIdSeq);
			$("#copyFolderPathBtnId").attr("folderName",folderName);
			$("#copyFolderShowSpanId").text($(this).text());
		}
	}).on("dblclick",function(){
		var id = $(this).attr("data-id");
		var fileName = $(this).attr("data-title");
		getCopyForderPathList($(this).attr("data-id"),"1");
	});
}

/** 点击分类或者文件夹的选中事件 */
function closeChooseCopyFolderPanl(){
	$("#copyFolderPathDivId").hide(100);
	$('#copy_folder_list').html("");
}
/**  复制到  end**/

/** 文档的视图切换和排序 start */

$("body").on('click','#fileViewFilterBtn',function(){
	var $fileViewFileterSpan = $(this).find("span.glyphicon");
	if ($fileViewFileterSpan.hasClass("glyphicon-th-list")) { // 当前是列表视图，转为图标视图
		$("#iconTypeId").click();
	} else if ($fileViewFileterSpan.hasClass("glyphicon-th-large")) { // 当前是图标视图，转为列表视图
		$("#listTypeId").click();
	}
});


// 设置时间查看关注信息
function setDateSubScribeMsgs(obj,finddateflg){
	var $findDateObj=$("#findDateFlg");
	var dateVal="全部";
	if(finddateflg == '2'){
		dateVal="一周";
	}else if(finddateflg == '1'){
		dateVal="今日";
	}
	$(obj).closest("ul").find("span").removeClass("glyphicon-ok");
	$(obj).find('span').addClass("glyphicon-ok");
	var finDateVal=$findDateObj.html(dateVal);
	var finDateFlg=$findDateObj.attr("findDateFlg",finddateflg);
	getSubScribeMsgsFun(1);
}

function getSubScribeMsgsFun(pageIndex){
	//20151112 获取设置时间段
	var finDateFlg=$("#findDateFlg").attr("findDateFlg");
	$("#content-list").scrollTop(0);
	$("#content-list").perfectScrollbar();
	var pageLimit = 20;
	/** 获取关注信息  **/
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/getSubScribeMsgByUserId?callback=?';
	var dataInput = {'userId':window.userId,'companyId':window.companyid,"pageIndex":pageIndex,"pageLimit":pageLimit,"findDateflg":finDateFlg};
	var ret = addSecurityPart(url, dataInput, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
			function(json) {
		//如果菜单不是我的关注，就不再显示我的关注内容
		if(!$("#mySubscribe").hasClass("active")){ 
			return;
		}
				
		/** lujixiang 20150907 记录当前用户,用于满足关注信息中对分享文件的特殊显示要求  **/
		json.currentUser = window.userName ;
		
		$("#contentBody").html(template("mySubscribe_templete",json));
		$("#content-list").perfectScrollbar('update');
		//分页处理
		var pageSize =Math.floor((json.subScribeSize+pageLimit-1)/pageLimit);
		// 分页
		$("#pagingDiv").pagination({
			pages: pageSize,
			currentPage: pageIndex,
			onPageClick:function(pageNumber, event) {
				isSubScribeTag = pageNumber;
				getSubScribeMsgsFun(pageNumber);
			}
		});
	});
}


// 设置视图类型  icon  list  glyphicon-th-list  glyphicon-th-large
function setViewType(obj, type) {
	var $fileViewFilterUL = $("ul.fileViewFilter");
	var $fileViewFileterSpan = $("#fileViewFilterBtn > span");
	// 选中下拉框的打钩
	$fileViewFilterUL.find('li a.file-view span').removeClass('glyphicon-ok');
	$(obj).find('span').addClass("glyphicon-ok");
	
	if ($(obj).hasClass("myDocument")) {
		// 设置cookie
		$.cookie('myfile-view-type_'+window.userId,type);
		if (type == 'icon') {
			$fileViewFileterSpan.removeClass("glyphicon-th-list");
			$fileViewFileterSpan.addClass("glyphicon-th-large");
		} else if (type == 'list') {
			$fileViewFileterSpan.removeClass("glyphicon-th-large");
			$fileViewFileterSpan.addClass("glyphicon-th-list");
		}
		//documentCenter.getMyFileList($("#filePageNowId").val());
		// 获取当前文件夹路径
		var $currentFolder = $("#file-breadcrumbs li.last");
		var folderId = $currentFolder.attr("data-folder-id");
		var folderIdSeq = $currentFolder.attr("data-idseq");
		if(folderIdSeq.split('.').length < 3){
			folderIdSeq = "" ;
		}
		documentCenter.getFileList(folderId, folderIdSeq,null,$("#filePageNowId").val());
	} else if ($(obj).hasClass("trash")) {
		// 设置cookie
		$.cookie('trash-view-type_'+window.userId,type);
		if (type == 'icon') {
			$fileViewFileterSpan.removeClass("glyphicon-th-list");
			$fileViewFileterSpan.addClass("glyphicon-th-large");
		} else if (type == 'list') {
			$fileViewFileterSpan.removeClass("glyphicon-th-large");
			$fileViewFileterSpan.addClass("glyphicon-th-list");
		}
		documentCenter.getTrashFileList($("#filePageNowId").val());
	} else {
		// 设置cookie
		$.cookie('file-view-type_'+window.userId,type);
		// 获取当前文件夹路径
		var $currentFolder = $("#file-breadcrumbs li.last");
		var folderId = $currentFolder.attr("data-folder-id");
		var folderIdSeq = $currentFolder.attr("data-idseq");
		/** xiaoxiong 20150609 添加是否为第一级分类验证 **/
		if(folderIdSeq.split('.').length == 3){
			folderIdSeq = "" ;
		}
		if (type == 'icon') {
			$fileViewFileterSpan.removeClass("glyphicon-th-list");
			$fileViewFileterSpan.addClass("glyphicon-th-large");
		} else if (type == 'list') {
			$fileViewFileterSpan.removeClass("glyphicon-th-large");
			$fileViewFileterSpan.addClass("glyphicon-th-list");
		}
		documentCenter.getFileList(folderId, folderIdSeq,null,$("#filePageNowId").val());
	}
	
}

/** 文档的视图切换和排序 end */

/** 文件视图的右键菜单 start */

// 初始化菜单事件
function initContextMenu() {
	$('#content-list div.file-item').contextmenu({
		target:'#context-menu', 
		before: function (e, context, target) {
			e.preventDefault();
			// 获取文件的信息
			var item_id = context.attr('id');
			var fileId = context.attr('fileid');
			var isFile = context.attr('isfile');
			var folderId = context.attr('folderId');
			var version = context.attr('version');
			var owner = context.attr('owner');  		//文件拥有者
			var owner_v1 = context.attr('owner_v1'); 	//第一版本权限拥有者 （私密权限）
			var openlevel = context.attr('openlevel');
			var isOwner = (owner == g_userId) ? true : false;
			var isV1Owner = (owner_v1 == g_userId) ? true : false;
			var isV1Version = parseInt(version)==1;
			var isMember = context.attr('isMember');
			var isTrash = context.hasClass("trash");
			var isMyDocument = context.hasClass("myDocument");
			var isSearch = context.hasClass("search");
			var title = isFile=="1"?"文件选项":"文件夹选项";
			var fileType = context.attr('filetype');
			var floderCreatorid = context.attr('creatorid');
			var currentUserId = window.userId;
			/** lujixiang 20150803 获取已被选中的文件夹和文件,批量处理 **/
			var isBatch = false;
			var activeItems = $('#content-list div.file-item.active');
			var size = activeItems.size();
			//标示私密文件权限
			var isDownload=context.attr("isDownload");
			var isLook= context.attr("isLook");
			//var folder_data_idseq = context.attr("idseq");
			//var class_data_idseq = $("#documentClassList li.active").attr("data-idseq");
			//var flag = folder_data_idseq.indexOf(class_data_idseq)==0?true:false;
			var class_owner = $("#documentClassList li.active").attr("owner");
			/** 屏蔽非成员的文件夹操作 和非创建者操作 分类权限最大**/
			if((isMember == 'false' && fileType == 'folder')){
				showBottomMsg("您无权限操作文件夹！", "2");
				return false;	
			}else if(floderCreatorid != currentUserId && currentUserId != class_owner && fileType == 'folder'){
				showBottomMsg("您无权限操作文件夹！", "2");
				return false;
			}
			
			if($(context).hasClass('active')){
				if(size > 1){
					title = '操作选项';
					isBatch = true;
				}
			}else{
				$('#content-list div.file-item').removeClass('active');
				$(context).addClass('active');
			}
			
			var data = template('menu_file_action_items_template', {"isFile":isFile,"targetId":item_id,"isOwner":isOwner,"isV1Owner":isV1Owner,"folderId":folderId,"isMember":isMember,"isTrash":isTrash,'isBatch':isBatch,'openlevel':openlevel,"isMyDocument":isMyDocument,"isDownload":isDownload,"isLook":isLook, "isV1Version":isV1Version});
			
			var html = template('menu_template',{"header":title,"data":data});
		    $("#context-menu").html(html);
		     var $menu = $("#context-menu");
			 $menu.find("#menu-close").click(function(){
				 $(this).parent().parent().remove();
			 })
			 $menu.on("mouseover","#file_openlevel_set",function(){
				 $(this).find("ul").css("display","block");
			 }).on("mouseout","#file_openlevel_set",function(){
				 $(this).find("ul").css("display","none");
			 });
			$("#menu_panel").addClass("dropdown-menu");
			
		},
		onItem: function(context,e) {
			return true;
		}
	});
}
function initRightClickMenu(obj) {
	//如果参数没有意义 就直接调用initContextMenu()方法
	if(!obj){
		initContextMenu();
		return;
	}
	
	/**  此处用于回收站搜索  很多参数不需要  */
	$(obj).find('div.file-item').contextmenu({
		target:'#context-menu', 
		before: function (e, context, target) {
			e.preventDefault();
			
			// 获取文件的信息
			var item_id = context.attr('id');
			var fileId = context.attr('fileid');
			var isFile = context.attr('isfile');
			var folderId = context.attr('folderId');
			var creatorId = context.attr('creatorid');
			var openlevel = context.attr('openlevel');
			var isOwner = (creatorId == g_userId) ? true : false;  //此处用于回收站搜索，故肯定是本人文件
			var isMember = context.attr('isMember');
			var isTrash = context.hasClass("trash");
			var isMyDocument = context.hasClass("myDocument");
			var isSearch = context.hasClass("search");
			var title = isFile=="1"?"文件选项":"文件夹选项";
			/** lujixiang 20150803 获取已被选中的文件夹和文件,批量处理 **/
			var isBatch = false;
			var activeItems = $('#content-list div.file-item.active');
			var size = activeItems.size();
			
			/** 屏蔽非成员的文件夹操作 **/
			if(2 > size && isFile == 0 && isMember == 'false'){
				return false;
			}
			
			if($(context).hasClass('active')){
				
				if(size > 1){
					title = '操作选项';
					isBatch = true;
				}
				
			}else{
				$('#content-list div.file-item').removeClass('active');
				$(context).addClass('active');
			}
			var data = template('menu_file_action_items_template', {"isFile":isFile,"targetId":item_id,"isOwner":isOwner,"folderId":folderId,"isMember":isMember,"isTrash":isTrash,'isBatch':isBatch,"openlevel":openlevel,"isMyDocument":isMyDocument});
			var html = template('menu_template',{"header":title,"data":data});
			$("#context-menu").html(html);
			 var $menu = $("#context-menu");
			 $menu.find("#menu-close").click(function(){
				 $(this).parent().parent().remove();
			 })
			 $menu.on("mouseover","#file_openlevel_set",function(){
				 $(this).find("ul").css("display","block");
			 }).on("mouseout","#file_openlevel_set",function(){
				 $(this).find("ul").css("display","none");
			 });
			
			$("#menu_panel").addClass("dropdown-menu");
		},
		onItem: function(context,e) {
			return true;
		}
	});
}
//初始化群组里成员的菜单事件
function initChatUserMenu() {
	$('#flyingchatusers').find('.itemuser').contextmenu({
		target:'#user-context-menu', 
		before: function (e, context, target) {
			e.preventDefault();
			// 获取文件的信息
			var item_id = context.attr('id');
			var itemuser = context.attr('itemuser');
			var itemuserid = context.attr('itemuserid');
			var itemuserfullname = context.attr('itemuserfullname');
			var isIn = context.attr('isIn')=='0' ? true : false;
			var isOwner = window.userId == itemuserid ? true:false;
			//判断是否是分类创建者liuwei2016/01/06
			var classOwner = $("#documentClassList li").attr('creator');
			var isClassOwner=itemuserid == classOwner? true : false; 
			if(!isOwner && isClassOwner){
			var data = template('menu_user_action_items_template', {"targetId":item_id,"isIn":isIn,"isOwner":isOwner});
			var html = template('menu_template',{"header":"成员操作","data":data});
			$("#user-context-menu").html(html);
			$("#menu_panel").addClass("dropdown-menu");
			}else{
				$("#user-context-menu").html("");
			}
		},
		onItem: function(context,e) {
			return true;	
		}
	});
}

//初始化左侧分类的菜单事件
function initGroupTypeMenu() {
	$('#classesList #publicClassSub').find('li').contextmenu({
		target:'#grouptype-context-menu', 
		before: function (e, context, target) {
			e.preventDefault();
			if(context.attr('lievent')=='false'){ //wangwenshuo 20151104 功能按钮不响应上下文菜单
				return;
			}
			// 获取文件的信息
			var id = context.attr('data-class-id');
			var groupid = context.attr('data-group-id');
			var groupname = context.attr('groupname');
			var ismember = context.attr('ismember');
			var creator = context.attr('creator');
			var owner = context.attr('owner');
			var flag = context.attr('flag');
			var isStar = context.attr('isstar');
			//var isIn = context.attr('isIn')=='0' ? true : false;
			var isOwner = window.userId == creator || window.userId == owner ? true:false;
			var data = template('menu_grouptype_action_items_template', {"isStar":isStar,"targetId":id,"isOwner":isOwner,"ismember":ismember,"flag":flag,"groupid":groupid,"groupname":groupname,"classId":id,"owner":owner});
			var html = template('menu_template',{"header":"分类操作","data":data});
			
			var objli =$(".main-left").find("li");
			$(objli).attr("class","");
			
			//var id = $(this).attr("target-id");
			//var li =$(".main-left").find("li[data-class-id='"+id+"']");
			var li =$("#publicClassSub").find("li[data-class-id='"+id+"']");
			
			$(li).attr("class","active");

			$("#grouptype-context-menu").html(html);
			$("#menu_panel").addClass("dropdown-menu");
		},
		onItem: function(context,e) {
			return true;
		}
	});
	//左侧公司的右击移交事件
	$('#classesList #publicClass').contextmenu({
		target:'#grouptype-context-menu', 
		before: function (e, context, target) {
			e.preventDefault();
			// 获取文件的信息
			var companyid = context.attr('companyid');
			var userid = context.attr('userid');
			var isadmin = context.attr('isadmin');
			var companyname = context.attr('companyname');
			var username = context.attr('username');
			var data = template('menu_company_action_items_template', {"companyid":companyid,"userid":userid,"username":username,"companyname":companyname,"isadmin":isadmin});
			var html = template('menu_template',{"header":"公司操作","data":data});
			$("#grouptype-context-menu").html(html);
			$("#menu_panel").addClass("dropdown-menu");
		},
		onItem: function(context,e) {
			return true;
		}
	});
}

/** 文件视图的右键菜单 end */

/** 文件历史版本 start */
$("body").on('click', '#versions_list .file_list_item', function() {
	var fileId = $(this).attr('fileId');
	var folderId = $(this).attr('folderId');
//	documentCenter.showFileInfo(fileId, folderId, "", true);
});

/** 文件历史版本 end */
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
    } else {
        return this.replace(reallyDo, replaceWith);
    }
}
/**
 * xiewenda 区分大小写 不改变被替换词的高亮
 */
String.prototype.replaceHighlightNoChangeMatchWord = function(replaceWith, ignoreCase) {
	var thisObj = this;
	if(typeof replaceWith != "string") return thisObj;  
	var regReplaceWith = new RegExp(replaceWith, (ignoreCase ? "gi" : "g"));
	if(ignoreCase){
		var arr = thisObj.match(regReplaceWith);
		arr = arr||[];
		var arrNoRepeat = [];
		arr.forEach(function(i){
			if($.inArray(i,arrNoRepeat)==-1){
			 var regi = new RegExp(i, "g"); 
			 thisObj = thisObj.replace(regi,'<em>'+i+'</em>');
			 arrNoRepeat.push(i);
		   }
		}); 
		
	}else{
		thisObj = thisObj.replace(regReplaceWith,'<em>'+replaceWith+'</em>');
	}
	return thisObj;
}


/** 文档中心导航面包屑 start */

function initFolderCrumbs() {
	var $classObj = $(".main-left li.active");
	var folderId = $classObj.attr("data-class-id");
	var folderName = $classObj.attr("title");
	var idSeq = $classObj.attr("data-idseq");
	$("#file-breadcrumbs").jFolderCrumb({data:[{"id":folderId, "name":folderName, "idSeq":idSeq}],clickFunName:"forwardFolder"});
}

/**
 * 文件夹跳转
 * @param thisObj
 */
function forwardFolderForFile(thisObj) {
	var $obj = $(thisObj);
	var folderId = $obj.attr('data-folder-id');
	//documentCenter.getFileList(folderId);
	closeFileInfo();
	showMainContentLeftFooter(true);
	documentCenter.setupFolderPath(folderId,"file-breadcrumbs","forwardFolder");
	documentCenter.getFileList(folderId);
	return false;
}

/**
 * 文件夹跳转
 * @param thisObj
 */
function forwardFolder(thisObj) {
	var $obj = $(thisObj);
	var folderId = $obj.attr('data-folder-id');
	var idseq = $obj.attr('data-idseq');
	documentCenter.getFileList(folderId,idseq);
	closeFileInfo();
	return false;
}


function forwardFolderForShare(thisObj) {
	var $obj = $(thisObj);
	var $folders = $obj.parent().parent().parent().find(".share_folder_list");
	getForderPathList($folders,$obj.attr("data-folder-id"));
	return false;
}

function forwardFolderForUpload(thisObj) {
	var $obj = $(thisObj);
	getForderLstOfUpload($obj.attr("data-folder-id"));
	return false;
}

/** 文档中心导航面包屑 end */

/** 列表视图的排序 start */

function sortList(objThis) {
	var $obj = $(objThis);
	var columnType = $obj.attr("type"); // 列类型
	var $span = $obj.find("span");
	// 替换小三角图标
	var fileOrderFieldId = $obj.attr("ordertype");
	var fileOrderTypeId = "";
	if ($span.hasClass("caret")) { // 向下
		$span.removeClass("caret").addClass("caret-desc");
		fileOrderTypeId = "asc";
	} else { // 向上
		$span.removeClass("caret-desc").addClass("caret");
		fileOrderTypeId = "desc";
	}
	$("#fileOrderFieldId").val(fileOrderFieldId);
	$("#fileOrderTypeId").val(fileOrderTypeId);
	if($("#myDocument").hasClass("active")){
		//documentCenter.getMyFileList($("#filePageNowId").val());
		documentCenter.getFileList($("#fileClassId").val(),null,null,$("#filePageNowId").val());
	} else if ($("#trash").hasClass("active")) {
		if($obj.hasClass("search")){
			documentCenter.searchTrashListsByKeyWord($("#filePageNowId").val());
		}else{
			documentCenter.getTrashFileList($("#filePageNowId").val());
		}
	} else{
		documentCenter.getFileList($("#fileClassId").val(),null,null,$("#filePageNowId").val());
	}
}
/** 推荐企业列表 */
function showcompany(){
		$("#companymanager").toggle();
		if($("#companymanager").is(":hidden")){
			$("#showcompanyss").animate({"height":"45px"},200);
				$("#showcompanyss").html("列表");
		}else{
			$("#showcompanyss").animate({"height":"160px"},200);
				$("#showcompanyss").html("成功推荐企业列表");
		}
}

function searchSortList(objThis , searchType) {
	//searchType  1 检索全部  2 检索当下
	var $obj = $(objThis);
	var $span = $obj.find("span");
	var fileOrderTypeId = "";
	// 替换小三角图标
	if ($span.hasClass("caret")) { // 向下
		$span.removeClass("caret").addClass("caret-desc");
		fileOrderTypeId = "asc";
	} else { // 向上
		$span.removeClass("caret-desc").addClass("caret");
		fileOrderTypeId = "desc";
	}
	$("#fileOrderFieldId").attr("searchField",$obj.attr("searchType"));
    $("#fileOrderTypeId").attr("searchType",fileOrderTypeId);
    if(searchType =="allClass"){
    	getSearchResult("","全部","",$("#filePageNowId").attr("searchPage"),"");
    }else if(searchType=="myDocument"){
    	getSearchResult("","我的文档","",$("#filePageNowId").attr("searchPage"),"myDocument");
    }else if(searchType=="anyoneClass"){
    	getSearchResult($("#searchSelectId").attr("folderId"),$(".main-left li.active").text(),$("#searchSelectId").attr("searchIdseq"),$("#filePageNowId").attr("searchPage"),"");
    }
	
}

/** 列表视图的排序 end */


/**
 * 赞操作
 * @param $obj
 */
function upAction(objThis) {
	var $obj = $(objThis);
	var selected = false;
	//style ,color: #0066FF;
//	if ($obj.hasClass('selected')) {
	if ($obj.attr('ispraise') == "true") {
//		$obj.removeClass('selected');
		$obj.attr('ispraise',"false");
		$obj.attr('style',"");
		selected = false;
		var praiseCount = parseInt($obj.find("span.praisecount").html());
		var sumPraiseCount = parseInt($obj.find("span.sum_praiseCount").html());
		if (praiseCount == 1) {
			$obj.find("span.praisecount").html(0);
			$obj.find("span.sum_praiseCount").html(0);
		} else {
			$obj.find("span.praisecount").html(praiseCount-1);
			$obj.find("span.sum_praiseCount").html(sumPraiseCount-1);
		}
	} else {
//		$obj.addClass('selected');
		selected = true;
		$obj.attr('ispraise',"true");
		$obj.attr('style',"color: #0066FF");
		var praiseCount = parseInt($obj.find("span.praisecount").html());
		var sumPraiseCount = parseInt($obj.find("span.sum_praiseCount").html());
		if (!praiseCount) {
			$obj.find("span.praisecount").html(1);
			$obj.find("span.sum_praiseCount").html(1);
		} else {
			$obj.find("span.praisecount").html(praiseCount+1);
			$obj.find("span.sum_praiseCount").html(sumPraiseCount+1);
		}
	}
	var $file = $obj.closest('.fileinfo');
	documentCenter.praiseFile($file.attr('fileId'), g_userId, selected);
	return false;
}

//详细页面权限申请-
function applyAuthority(id,isApply){
	var $fileInfo=$("#fileinfo_id_"+id);
	var userName = $fileInfo.attr("ownername");
	var fileId = $fileInfo.attr("fileid");
	var fileTitle = $fileInfo.attr("filetitle");
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/applyFileDown?callback=?';
	var data = {'companyId':window.companyid,'username':window.userName,'fileId':fileId,'fileTitle':fileTitle,'applyType':isApply};
	var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
	  function(json) {
		if(json.isOk){
			$.WebIM.applyFileClickFun(userName, fileId, fileTitle,isApply);
		}else{
			showBottomMsg("申请已发送成功，请等待回复...", 2);
		}
	  }
	);
	return false;
}


/** 回收站 start */
$("#privateClassSub").on("click","#trash",function(){
	$('#bodyContent_Title').html(template('userCenter_menu_templete'));
	changeUploadBtn("0");
	closeFileInfo();
	
	var viewType = $.cookie('trash-view-type_'+window.userId);
	if (!viewType) {
		viewType = 'icon';
	}
	var sortType = $("#file-sort-type").val();
	var file_view_filter_btn_html = template("file_view_filter_btn_template", {"viewType":viewType,"sortType":sortType,"documentType":"trash"});
	// 设置头部
	var jsonHeading = '{"type":"'+$(this).attr('id')+'"}';
	var json = jQuery.parseJSON(jsonHeading);
	json["file_view_filter_btn_html"] = file_view_filter_btn_html;
	$("#content-heading").html(template('file_panel_heading_template', json));
	$("#contentBody").html("");
	showMainContentLeftFooter(true);
	documentCenter.getTrashFileList();
});

/**  清空回收站 **/
$("#mainContentBody").on("click","#cleanRecycleBin",function(){
	$.dialog({
		title: '清空回收站',
		content : '确定要彻底删除回收站内所有内容吗？',
		okValue : '确定',
		cancelValue : '取消',
		cancel : true,
		ok : function() {
			$.post($.appClient.generateUrl({ESDocumentCenter:'cleanRecycleBin'},'x'),
					{userId:g_userId}, function(rst){
						if (rst == "success") {
							showBottomMsg("清空回收站成功","1");
							documentCenter.getTrashFileList();
						} else {
							showBottomMsg("清空回收站失败！","3");
						}
					});
		}
	}).showModal();
	return false;
});
/**
 * 文件恢复
 */
function fileRestore(objThis) {
	var itemId = $(objThis).attr("target-id");
	var $itemObj = $("#"+itemId);
	var fileId = $itemObj.attr("fileId");
	var trashId = $itemObj.attr("trashId");
	var isfile = $itemObj.attr("isfile");
	//我的文档文件特殊处理 companyId=user_N
	var isMyDocument = $itemObj.hasClass("myDocument");
	var companyId = window.companyid ;
	if(isMyDocument){
		companyId = "user_"+window.userId ;
	}
	$.dialog({
		title: '恢复'+(isfile=='0'?'文件夹':'文件'),
		content : '确定恢复'+(isfile=='0'?'文件夹':'文件')+'？恢复完成后，请前往原位置查看！',
		okValue : '确定',
		cancelValue : '取消',
		cancel : true,
		ok : function() {
			documentCenter.fileRestore(fileId,trashId,isfile, $(objThis).hasClass("search"),companyId);
		}
	}).showModal();
	return false;
}

/**
 * 文件彻底删除
 */
function fileDestroy(objThis) {
	var itemId = $(objThis).attr("target-id");
	var $itemObj = $("#"+itemId);
	var fileId = $itemObj.attr("fileId");
	var isfile = $itemObj.attr("isfile");
	var isMyDocument = $itemObj.hasClass("myDocument");
	//我的文档文件特殊处理 companyId=user_N
	var companyId = window.companyid ;
	if(isMyDocument){
		companyId = "user_"+window.userId ;
	}
	$.dialog({
			title: '彻底删除文件(文件夹)',
			content : '确定彻底删除文件(文件夹)？删除后无法恢复！',
			okValue : '确定',
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				documentCenter.fileDestroy(fileId,isfile,$(objThis).hasClass("search"),companyId);
			}
		}).showModal();
	return false;
}

/** 回收站 end */

/**
 * 显示文件的所有版本聊天记录
 * longjunhao 20150701 目前需求不明确，暂时不用
 */
function showChatFileLog(objThis) {
	var $item = $(objThis).closest("div.fileinfo");
	var fileId = $item.attr("fileid");
	var fileIdSeq = $item.attr("idseq");
	var fileTitle = $item.attr("filetitle");
	var version = $item.attr("version");
	// 文件的idseq . 文件名 . 文件后缀
	var fileFlag = fileIdSeq.substring(0, fileIdSeq.lastIndexOf("."+fileId+".")) + window.filePathDivide + fileTitle;
	// 在聊天记录框中插入关于该文件的历史聊天信息
	$.WebIM.showChatFileLog(fileFlag);
}

/**
 * 显示文件某个版本的聊天记录
 * longjunhao 20150701 目前需求不明确，暂时不用
 */
function showChatFileLogVersion(objThis) {
	var $item = $(objThis).closest("div.file_list_item");
	var fileId = $item.attr("fileid");
	var fileIdSeq = $item.attr("idseq");
	var fileTitle = $item.attr("filetitle");
	var version = $item.attr("version");
	// 文件的idseq . 文件名 . 文件后缀
	var fileFlag = fileIdSeq.substring(0, fileIdSeq.lastIndexOf("."+fileId+".")) + window.filePathDivide + fileTitle + window.filePathDivide + version;
	// 在聊天记录框中插入关于该文件的历史聊天信息
	$.WebIM.showChatFileLog(fileFlag);
}


/**
 * 标记/取消常用分类
 */
//function starClass() {
//	var $obj = $("#starToolBtn");
//	var classId = $obj.attr("classId");
//	var status = 'true';
//	var isStar = $(".main-left li[data-class-id='"+classId+"']").attr("isStar");
//	if (isStar == "true") {
//		status = "false";
//		star(false);
//	} else {
//		star(true);
//	}
//	documentCenter.starClass(classId,status);
//}

/*function star(isStar) {
	if (typeof isStar === 'string') {
		if (isStar == "true") {
			isStar = true;
		} else if (isStar == "false") {
			isStar = false;
		}
	}
	var $obj = $("#starToolBtn");
	var $picObj = $obj.find("span.glyphicon");
	if (isStar) {
		$picObj.removeClass("glyphicon-star-empty");
		$obj.attr('title','取消常用分类');
		$picObj.addClass("glyphicon-star");
	} else {
		$picObj.removeClass("glyphicon-star");
		$obj.attr('title','设为常用分类');
		$picObj.addClass("glyphicon-star-empty");
	}
}*/

$('#grouptype-context-menu').off('click','#setting-classification').on('click','#setting-classification',function(){
	
	var $obj = $('#setting-classification');
	var classId = $obj.attr("classId");
	var status = 'true';
	var isStar = $(".main-left li[data-class-id='"+classId+"']").attr("isStar");
	
	if (isStar == 'true') {
		status = 'false';
		star(false);
	} else {
		star(true);
	}
	documentCenter.starClass(classId,status);
});

function star(isStar) {
	if (typeof isStar === 'string') {
		if (isStar == "true") {
			isStar = true;
		} else if (isStar == "false") {
			isStar = false;
		}
	}
	var $obj = $("#setting-classification");
	var $picObj = $obj.find("a");
	if (isStar) {
		$picObj.removeClass("glyphicon-star-empty");
		$picObj.addClass("glyphicon-star");
	} else {
		$picObj.removeClass("glyphicon-star");
		$picObj.addClass("glyphicon-star-empty");
	}
}



/**
 * 发表文件评论
 */
function saveFileComment(objThis) {
	var $item = $(objThis).closest("div.fileinfo");
	var filecontent =$(objThis).closest("#filecontent");
	var fileId = $item.attr("fileid");
	var fileIdSeq = $item.attr("idseq");
	var fileTitle = $item.attr("filetitle");
	var version = $item.attr("version");
//	var fileFlag = fileIdSeq.substring(0, fileIdSeq.lastIndexOf("."+fileId+".")) + window.filePathDivide + fileTitle;
	var fileFlag = fileId;
	//if(filecontent.length>0)
	var $contentTextarea = $(objThis).closest("#new_file_comment").find("textarea.form-control");
	var content = $contentTextarea.val();
	if($.trim(content).length<1){
		showBottomMsg("亲,是不是忘记留言啦！", 2);
		return;
	}
	content = content.replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/ /g,'&nbsp;');//替换'<'，'>'
	content = "<p>" + content + "</p>";
	var $quotecomment = $(objThis).closest("#new_file_comment").find("#quote-comment");
	// 判断是否要引用
	if (!$quotecomment.hasClass('hidden')) {
		var $targetItem = $('#'+$quotecomment.attr("target-id"));
		var targetFullName = $targetItem.find('div.comment>a.comment-name').text();
		var targetImg = $targetItem.find('.avatar-face').html(); 
		var targetCreateDate = $targetItem.find('div.comment>.comment-meta>span.comment-date').text();
		var targetComment = $targetItem.find('div.current-comment>pre').html();
		var currentUserName = $('current_user_name').text();
		var targetHtml = 
				'<div class = "fileCommentBorderCls"><span class="comment-name">' +targetImg+targetFullName+ '</span>'+'<span class="comment-date">'+targetCreateDate+'</span>' 
				+ '<p>'+targetComment+'</p></div>';
		content += targetHtml;
	}
	
	$contentTextarea.val("");
	$.post($.appClient.generateUrl({ESDocumentCenter:'newFileComment'},'x'),
			{fileFlag:fileFlag, version:version, content:content, userId:window.userId, fullName:window.fullName, portrait:window.PORTRAIT}, function(rst){
				showBottomMsg("评论成功", 1);
				// 获取最后一页
				var lastPage = '1';
				// 刷新评论
				var versions = "";
				setTimeout(function (){
					
					var versionAction = $('#file_preview_history_version .file_list_item');
						versionAction.each(function(){
							versions += $(this).attr('fileid')+",";
							
					});
					if(filecontent.length>0){
						documentCenter.getFileCommentList(fileFlag,"",lastPage,"",true,"2",versions);
						}else{
						documentCenter.getFileCommentList(fileFlag,"",lastPage,"",true,"",versions);
					}
					},0);
				
				
			});
}

/**
 * 回复文件评论
 */
function replyFileComment(thisObj) {
	var $targetItem = $('#'+$(thisObj).attr('target-id'));
	var filecontent =$(thisObj).closest("#filecontent");
	var targetFullName = $targetItem.find('div.comment>a.comment-name').text();
	var targetCreateDate = $targetItem.find('div.comment span.comment-date').text();
	var targetComment = $targetItem.find('div.current-comment>pre').html();
	var $contentTextarea = $(thisObj).closest("#file_preview_comment").find("textarea.form-control");
	var $quotecomment = $(thisObj).closest("#file_preview_comment").find("#quote-comment");
	$quotecomment.attr("target-id",$(thisObj).attr('target-id'));
	$quotecomment.find("div.title").html("引用 @"+targetFullName+" 的评论：");
	$quotecomment.find("pre").html(targetComment);
	$quotecomment.removeClass("hidden");
	$contentTextarea.focus();
	if(filecontent.length>0){
		$("#filecontent").perfectScrollbar('update');
	}else{
		$("#content-list4FileInfo").scrollTop($("#contentBody4FileInfo").height());
		$("#content-list4FileInfo").perfectScrollbar('update');
	}
	
}

/**
 * 删除文件评论
 */
function deleteFileComment(thisObj) {
	var $item = $(thisObj).closest("div.fileinfo");
	var filecontent = $(thisObj).closest("#filecontent");
	var fileId = $item.attr("fileid");
	var fileIdSeq = $item.attr("idseq");
	var fileTitle = $item.attr("filetitle");
//	var fileFlag = fileIdSeq.substring(0, fileIdSeq.lastIndexOf("."+fileId+".")) + window.filePathDivide + fileTitle;
	var fileFlag = fileId;
	
	var commentId = $(thisObj).attr('target-id');
	commentId = commentId.replace('comment-item-','');
	var searchType = $("#searchValHidId").attr("searchtype");
	if(filecontent.length>0){
		documentCenter.deleteComment(commentId,fileFlag,searchType);
	}else{
		documentCenter.deleteComment(commentId,fileFlag);
	}
	
}

/**
 * 取消引用
 */
function cancelQuoteComment() {
	var $quotePanel = $('#quote-comment');
	$quotePanel.addClass("hidden");
	$quotePanel.attr('targetId','');
	$quotePanel.find('div.title').html("");
	$quotePanel.find('pre').html("");
	// 刷新滚动条
	$("#content-list4FileInfo").perfectScrollbar('update');
}

/**
 * 文件预览-发送到讨论
 */
function send2Share(thisObj) {
	var $fileItem = $(thisObj).closest('div.fileinfo');
	//增加发送到讨论的提示 liumingchao
	var isgroup = $('#receiverusername').attr('isgroup');
	var groupFlag = $('#receiverusername').attr('groupflag');
	var name = $('#receiverusername').html();
	if(name=='fyBot'){
		showBottomMsg("机器人无法接收您分享的文档哦，请分享给您的其他小伙伴吧！","3");
		return;
	}
	var hexname = hex_md5(groupFlag);
	var $obj = $('#newmessage'+hexname) ;
	var content="";
	if(isgroup =='0'){
		content = "确定要将文件发送到用户 【"+name+"】的聊天窗口吗？"
	}else if(isgroup =='1'){
		if($obj.attr("class")=="classnewmessage"){
			content = "确定要将文件发送到分类【"+name+"】的聊天窗口吗？"
		}else if($obj.attr("class")=="newmessage"){
			content = "确定要将文件发送到分组【"+name+"】的聊天窗口吗？"
		}
	}
	
	var html = [
			'<div id="fileAccessRight" class="alert alert-warning" style="margin:10px 0px -5px 0px;">',
			'	<span class="tips"><strong>请配置【'+name+'】拥有的文件权限</strong></span>',
			'	<div class="options">',
			'		<div class="radio">',
			'		    <label>',
			'		      <input type="radio" name="optionsRadios" value="1" checked> 浏览权限',
			'		    </label>',
			'		</div>',
			'		<div class="radio">',
			'		    <label>',
			'		      <input type="radio" name="optionsRadios" value="3"> 浏览、下载权限',
			'		    </label>',
			'		</div>',
			'	</div>',
			'</div>'
		].join('');
	$.dialog({
		content : content+html,
		quickClose: true,
		okValue : '确定',
		cancelValue : '取消',
		cancel : true,
		ok : function() {
			var accessRight = $("#fileAccessRight input[name='optionsRadios']:checked").val();
			$fileItem.attr("accessright",accessRight);
			shareAction($fileItem);
			}
	}).show();
	
	return false;
}
//公开级别设置
function setOpenlevel(thisObj){
	var $fileItem = $(thisObj).closest('div.fileinfo');
	var openlevel = $fileItem.attr("openlevel");
	var targetId = $fileItem.attr("id");
	var data = template('menu_file_action_openlevel_template', {"targetId":targetId,"openlevel":openlevel});
	$(thisObj).fymenu({"data": data,"header":"公开级别设置"});
}

/**
 * wangwenshuo 20151020 复制到 
 */
function send2Copy(thisObj) {
	var $fileItem = $(thisObj).closest('div.fileinfo');
	copyToFolderAction($fileItem);
	return false;
}


//在线浏览
function send2preview(thisObj){
//	var $fileItem = $(thisObj).closest('div.fileinfo');xyc
	var $fileItem = $("#"+thisObj);
	var fileType = $fileItem.attr('filetype');
	var fileSize = $fileItem.attr('size')
	var filePath = $fileItem.attr('filepath');
	var maxFileSize = 52428800;
	var swfHttpUrl = window.onlinefilePath.substring(0, window.onlinefilePath.lastIndexOf("/"));
	var url = $.appClient.generateUrl({ESCFileViewer:'getOnlineViewUrl'},'x'); 
	if(fileSize > maxFileSize){
		showBottomMsg("您选择的文件太大暂不支持在线浏览,请您下载后浏览!",2);
		 return ;
	}
	if('pdf'== fileType || 'tif'== fileType || 'bmp' == fileType ||
			   'docx' == fileType ||'xls' == fileType || 
			   'doc'==fileType || 'xlsx'==fileType ||
			   'ppt'==fileType||'pptx' == fileType||
			   'txt'== fileType || "html" == fileType || "htm" == fileType ||
			   'jpg'== fileType||'png'== fileType|| 
				'gif'== fileType||'jpeg' == fileType){

		$.ajax({
			url:url,
			type:'POST',
			data:{'fileId':filePath,'fileType':fileType},
			success:function(data){
				var pdfUrl = eval('(' + data + ')');
				if('' == pdfUrl){
					showBottomMsg("亲,文件正在获取中稍后再试!",2);
					return ;
				}else{
					//var param = swfHttpUrl+'/fileStoreMainServer/generic/web/viewer.html?file='+pdfUrl;
					var param = "apps/onlinefile/templates/public/generic/web/viewer.html?file="+pdfUrl;
					window.open(param,'_blank');
				}
			}
		});
	}else{
		showBottomMsg("您选择的文件格式暂不支持在线浏览,请您下载后浏览!",2);
		return ;
	}
	
}




/**企业团队注册验证输入长度**/
function checkCompnanyName(obj,len){
	 if(obj.value.replace("/[^/x00-/xFF]/g",'**').length>=len){
		   obj.value=leftUTFString(obj.value,len);
		   showBottomMsg('企业(团队)名称的长度不能超过50个字符!', 2);
		   return false ;
	  }
}

function getStringUTFLength(str) { 
	var value = str.replace("/[^/x00-/xff]/g","  "); 
	return value.length; 
}
function leftUTFString(str,len) { 
	  if(getStringUTFLength(str)<=len) 
	   return str; 
	   var value = str.substring(0,len); 
	   while(getStringUTFLength(value)>len) { 
	 	  value = value.substring(0,value.length-1); 
	   } 
	return value; 
}



/**
 * 发送到讨论操作
 * @param $obj
 */
function shareAction($obj) {
	var folderid = $obj.attr("folderid");
	var fileId = $obj.attr("fileid");
	var accessRight = $obj.attr("accessright");
	/** 如果是我的文档创建文件夹 那么companyId应该为‘user_’+userId */
	var companyId = documentCenter.getCompanyId();
	// 发送一条消息，将文件分享到聊天界面
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFileInfo?callback=?';
	var data = {"userName":window.userName,"fileId":fileId, "companyId":companyId,"userId":g_userId};
	var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
		function(data) {
				if(data.file.isDelete=="1"){
					showBottomMsg("对不起，文件已被删除，无法发送！","3");
					return false;
				}else if(data.hasRight == false || data.hasRight == "false"){
					showBottomMsg("对不起，您无此文件的操作权限，请到文件详细信息页进行申请！","3");
					return false;
				}else{
					if($("#receiverusername").text()=="fyBot"){
					showBottomMsg("机器人无法接收您分享的文档哦，请分享给您的其他小伙伴吧！", "3");
					}else{
						$.WebIM.sendShareFile(folderid, fileId,accessRight);
						closeContentPanel();
					}
				}
	});
}

/**
 * wangwenshuo 20151228 文件申请 打开文件
 * @param {} $obj
 */
function openApplyFile($obj){
	var fileId = $($obj).attr("fileId");
	var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFileInfo?callback=?';
	var data = {"userName":window.userName,"fileId":fileId, "companyId":g_companyId,"userId":g_userId};
	var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
		function(data) {
			if(data.file.isDelete=="1"){
				showBottomMsg("对不起，文件已被删除！","3");
				return false;
			}else if(data.hasRight == false || data.hasRight == "false"){
				showBottomMsg("对不起，您无此文件的操作权限，请到文件详细信息页进行申请！","3");
				return false;
			}else{
				var folderId = data.file.classId;
				var fileIdSeq = data.file.idSeq;
				var isupload = "0";
				if($("#uploadAllBtnDivId").css("top")!="0px"){
					 isupload = "1";
				}
				changeUploadBtn("0");
				var html = template('file_info_template', {"fileIdSeq": fileIdSeq, "myDocument": folderId=='0'?"myDocument":"","isupload":isupload});
				$('#mainContentLeft').hide();
				$('#mainContentLeft4FileInfo').show();
				$('#mainContentLeft4FileInfo').html(html);
				documentCenter.showFileInfo(fileId, folderId, null,null,null,false);
				return false;
			}
	});
}
function showFileContent(obj) {
	var content = $(obj).find("div").html();
	var d = dialog({
		align:'bottom',
	    content: content,
	    width: 500,
	    height: 200,
	    fixed: true,
	    quickClose: true// 点击空白处快速关闭
	});
	d.show(obj);
}


function menuClose(obj) {
	var o = obj || "#menu_panel";
	$(o).remove(); 
}

$("body").on('click','#menu_items li',function(){
	menuClose($(this).closest(".menu"));
});

function gotoIndexPage() {
	window.location.href = window.logouturl ;
}

/** wangwenshuo 20151104 个人空间和公有分类折叠 */
function classCollapse(obj){
	var subObj = $("#"+$(obj).attr("id")+"Sub");
	subObj.slideToggle(200,function(){
		if($(this).is(':hidden')){  //隐藏
			$(obj).find('.glyphicon-chevron-down').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left');
		}else{
			$(obj).find('.glyphicon-chevron-left').removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-down');
		}
	});
	$('#classesList').perfectScrollbar('update');//刷新滚动条
}


/*
$(document).ajaxComplete(function(event, xhr, settings) {  
    if(xhr.getResponseHeader("sessionstatus")=="timeOut"){  
        if(xhr.getResponseHeader("loginPath")){
            alert("会话过期，请重新登陆!");
            window.location.replace(xhr.getResponseHeader("loginPath"));  
        }else{  
            alert("请求超时请重新登陆 !");  
        }  
    }  
}); 
*/
$(document).ajaxComplete(function() {  
    if(window.globalUserStatus == 0){  
     	alert("会话过期，请重新登陆!");
    	window.location.replace(window.logouturl);  
    }  
}); 

/*$.ajaxSetup({
	beforeSend:function(){
		if(window.globalUserStatus == 0){
			showBottomMsg("当前会话已经过期，请<a onclick='gotoIndexPage()'>重新登录</a>。", 3);
			return false ;
		}
	}
});*/
var JPos = {};
(function($){
$.$getAbsPos = function(p)
 {
  var _x = 0;
  var _y = 0;
  while(p.offsetParent){
    _x += p.offsetLeft;
    _y += p.offsetTop;
    p = p.offsetParent;
  }
   
  _x += p.offsetLeft;
  _y += p.offsetTop;
   
  return {x:_x,y:_y};
 };
  
 $.$getMousePos = function(evt){
  var _x,_y;
  evt = evt || window.event;
  if (evt.pageX || evt.pageY)
  {
   _x = evt.pageX;
   _y = evt.pageY;
  }
  else if (evt.clientX || evt.clientY)
  {
   _x = evt.clientX + document.body.scrollLeft - document.body.clientLeft;
   _y = evt.clientY + document.body.scrollTop - document.body.clientTop;
  }
  else
  {
    return $.$getAbsPos(evt.target); 
  }
  return {x:_x,y:_y};
 }
})(JPos);
