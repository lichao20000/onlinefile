var documentCenter = {
	baseUrl: window.onlinefilePath,	
	// 记录每次发送消息的时候，分享文件的列表
	shareFileList:"",
	
	/**
	 * 获取正在打开的文件id
	 */
	getCurrentFileId:function() {
		var fileId = $("#mainContentLeft4FileInfo div.fileinfo").attr("fileid");
		return fileId;
	},
	
	/**
	 * 获取正在打开的文件的fileFlag
	 * 文件的idseq . 文件名 . 文件后缀 vx
	 */
	getCurrentFileFlag:function() {
		var $obj = $("#mainContentLeft4FileInfo div.fileinfo");
		var fileId = $obj.attr("fileid");
		var fileIdSeq = $obj.attr("idseq");
		var fileTitle = $obj.attr("filetitle");
		var version = $obj.attr("version");
		var fileFlag = fileIdSeq.substring(0, fileIdSeq.lastIndexOf("."+fileId+".")) + window.filePathDivide + fileTitle + window.filePathDivide + version;
		return fileFlag;
	},
	
	/**
	 * 获取当前文件视图的页数
	 */
	getCurrentPageNum:function() {
		var $currentPageNumber = $("#pagingDiv ul.pagination li.active span.current.pagenumber");
		var page = $currentPageNumber.text();
		return page;
	},
	
	/**
	 * 获取顶级分类的id
	 */
	getClassIdByIdSeq:function(idSeq) {
		var classId = "";
		var array = idSeq.split(".");
		if (array.length > 2) {
			classId = array[1];
		}
		return classId;
	},
	
	/**
	 * 获取左侧分类列表
	 */
	getClassList:function(userId,classId,status) {
		documentCenter.getClassStarList(classId,status);
		var param = {"companyId":g_companyId,"userId":userId, "username":window.userName};
		$.ajax({
			type:'POST',
	        url : $.appClient.generateUrl({ESDocumentCenter : 'getClassList'},'x'),
	        data:param,
	        datatype:"json",
	        async:false,//设为同步 
		    success:function(json){
		    	var json = eval('('+json+')');
		    	$('#documentClassList').html(template('class_item_template', json));
		    	initGroupTypeMenu();
				$(".main-left li[data-class-id='"+classId+"']").attr("isStar",status);
				$(".main-left li[data-class-id='"+classId+"']").addClass("active");
				
		    }
		});
		
	},
	
	/**
	 * 获取用户常用的分类
	 */
	getClassStarList:function(classId,status) {
/*		jQuery.getJSON(this.baseUrl+'/rest/onlinefile_filesws/getClassStarList?callback=?',
				{"companyId":g_companyId,"userId":window.userId, "username":window.userName},
				function(json) {
			$('#starClassList').html(template('changyong_class_item_template', json));
			$('#group-left').perfectScrollbar({"suppressScrollX":true});
			
			if($('#starClassList li').length<6){
				$("#group-left").css("margin-top",$('#starClassList').height()+65);
				$('#starClassList').css("height",($('#starClassList li').length)*50);
			}else{
				$("#group-left").css("margin-top",321);
				$('#starClassList').css("height",250).perfectScrollbar();
			}
			initGroupTypeMenu();
			$(".main-left li[data-class-id='"+classId+"']").attr("isStar",status);
			$(".main-left li[data-class-id='"+classId+"']").addClass("active");
		});*/
	     var param = {"companyId":g_companyId,"userId":window.userId, "username":window.userName};
		//xiewenda 修改
		$.ajax({
			type:'POST',
	        url : $.appClient.generateUrl({ESDocumentCenter : 'getClassStarList'},'x'),
	        data:param,
	        datatype:"json",
	        async:false,//设为同步 
		    success:function(json){
		    	var json = eval('('+json+')');
		    	$('#starClassList').html(template('changyong_class_item_template', json));
				initGroupTypeMenu();
				$(".main-left li[data-class-id='"+classId+"']").attr("isStar",status);
				$(".main-left li[data-class-id='"+classId+"']").addClass("active");
		    }
		});
	},
	
	/**
	 * 登录时，初始化左侧分类列表（常用和普通）
	 * longjunhao 20150928
	 */
	initClassList:function(userId,classId,status) {
		var param = {"companyId":g_companyId,"userId":userId, "username":window.userName};
		$.ajax({
			type:'POST',
	        url : $.appClient.generateUrl({ESDocumentCenter : 'initClassList'},'x'),
	        data:param,
	        datatype:"json",
	        async:false,//设为同步 
		    success:function(json){
		    	var json = eval('('+json+')');
		    	var classes = json.classes;
		    	var starClasses = json.starClasses;
		    	/**
		    	 * 渲染常用分类
		    	 */
		    	$('#starClassList').html(template('changyong_class_item_template', {classes:starClasses}));
		    	
		    	/**
		    	 * 渲染普通分类
		    	 */
		    	$('#documentClassList').html(template('class_item_template', {classes:classes}));
				
				$("#classesList").css("height",$(window).height()-58);
				$('#classesList').perfectScrollbar({"suppressScrollX":true});
				initGroupTypeMenu();
				$(".main-left li[data-class-id='"+classId+"']").attr("isStar",status);
				$(".main-left li[data-class-id='"+classId+"']").addClass("active");
				
		    }
		});
		
	},
	
	/**
	 * longjunhao 20150228 add 加载文档列表
	 * classId:分类ID
	 * userId：用户ID
	 */
	getFileList:function(classId, idseq, userId, page, pageSize) {
		var orderField = $("#fileOrderFieldId").val();
		var orderType = $("#fileOrderTypeId").val();
		$("#fileClassId").val(classId);
		// 获取正在选中的分组分类
		var groupId = $(".main-left li.active").attr("data-group-id");
		if (userId==null) userId="";
		if (idseq==null) idseq="";
		var page = (page == null || page=="") ? 1 : page;
		$("#filePageNowId").val(page);
		var pageSize = (pageSize == null || pageSize == "") ? window.pageSize : pageSize;
		
		/* 添加判断  是我的文档 还是分类 */
		var companyId = documentCenter.getCompanyId();
		var isMyDocument = documentCenter.isMyDocument();
		var url = this.baseUrl+'/rest/onlinefile_filesws/getFileList?callback=?';
        var data = {"userName":window.userName,"companyId":companyId, "loginUserId":g_userId, "groupId":groupId, "classId":classId, "userId":userId, "page":page, "pageSize":pageSize,"idseq":idseq,"orderField":orderField,"orderType":orderType };
        var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			var isMember = json.isMember;
			var viewType = $.cookie('file-view-type_'+window.userId);
			if(isMyDocument){
				viewType = $.cookie('myfile-view-type_'+window.userId)
			}
			if (!viewType) {
				viewType = 'icon';
			}
			if (viewType == 'list') { // 列表视图
				json["viewType"] = "list";
			}
			json["myDocument"] = isMyDocument?"myDocument":"";
			json["orderField"] = orderField;
			json["orderType"] = orderType;
			$('#contentBody').html(template('documentAll_templete', json));
			initContextMenu();
			if ($("#content-list").find("div.ps-scrollbar-x-rail").length > 0) {
				if(window.companyid !="-1"){//xyc 取消滚动条20151120
					$("#content-list").perfectScrollbar('update');
				}
			} else {
				$("#content-list").perfectScrollbar();
			}
			//alert(isMyDocument)
			//if(!isMyDocument){
				if (isMember && $("#mainContentLeft4FileInfo").is(":hidden")) {
					changeUploadBtn("1");
					createfilejiapower("1");
					/** 当自己在当前群组时，才可进入群组聊天框 **/
	//				$.WebIM.openGroup($(this)) ;
	//				$.WebIM.reloadUserList(1) ;
				} else {
					$("#class_visitor").removeClass("hidden");
					createfilejiapower("0");
				}
				if(idseq!=null && idseq.length>0){
					//跳转后，设置下导航条
					json.folderObjsJson.shift();
					$("#file-breadcrumbs").jFolderCrumb({data:json.folderObjsJson,clickFunName:'forwardFolder'});
				}
			//}
			// 分页
			$("#pagingDiv").pagination({
				pages: json.total,
				currentPage: json.page,
				onPageClick: function(pageNumber, event) {
					documentCenter.getFileList(classId, null, "", pageNumber, pageSize);
				}
			});
		});
	},
	getFirstFileList:function(object, classId, idseq, userId, page, pageSize) {
		// 获取正在选中的分组分类
		var groupId = $(".main-left li.active").attr("data-group-id");
		$("#fileClassId").val(classId)
		if (userId==null) userId="";
		if (idseq==null) idseq="";
		var page = (page == null) ? 1 : page;
		$("#filePageNowId").val(page);
		var pageSize = (pageSize == null) ? window.pageSize : pageSize;
		/* 添加判断  是我的文档 还是分类 */
		var companyId = documentCenter.getCompanyId();
		var isMyDocument = documentCenter.isMyDocument();
		
		var url = this.baseUrl+'/rest/onlinefile_filesws/getFileList?callback=?';
		var data = {"userName":window.userName,"companyId":companyId, "loginUserId":g_userId, "groupId":groupId, "classId":classId, "userId":userId, "page":page, "pageSize":pageSize,"idseq":idseq};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
					var isMember = json.isMember;
					var viewType = $.cookie('file-view-type_'+window.userId);
					if(isMyDocument){
						viewType = $.cookie('myfile-view-type_'+window.userId)
					}
					if (!viewType) {
						viewType = 'icon';
					}
					if (viewType == 'list') { // 列表视图
						json["viewType"] = "list";
					}
					json["myDocument"] = isMyDocument?"myDocument":"";
					$('#contentBody').html(template('documentAll_templete', json));
					initContextMenu();
					if(window.companyid !="-1"){
						//xyc 取消滚动条20151120
						$("#content-list").perfectScrollbar('update');
					}
					//重新加载分类消息数 重新加载用户列表↓
					if(!isMyDocument){
						if (isMember) {
							changeUploadBtn("1");
							/** xiaoxiong 20150515 当自己在当前群组时，才可进入群组聊天框 **/
							if($("#peguserpic").attr("class") == "peguserpic-default"){
								$.WebIM.openGroup(object);
								$.WebIM.reloadUserList(1);
							}
							/**文件夹权限**/
							createfilejiapower("1");
						} else {
							$("#class_visitor").removeClass("hidden");
							createfilejiapower("0");
							
							/**   lujixiang 20151125 刷新"好友"信息数  --start   **/
							var chatUserAgc = 0 ;
							$(".userLists").find("input[id^='newmessageval']").each(function(){
								var newmessagecount = $(this).val();
								if(undefined == newmessagecount || '' == newmessagecount){
									newmessagecount = 0 ;
								}
								chatUserAgc += newmessagecount * 1;
							});
							var userallcountValObj = $("#items-users-messcount-val") ;
							var userallcount = $("#items-users-messcount") ;
							
							if(chatUserAgc > 0 ){
								userallcountValObj.val(chatUserAgc);
								chatUserAgc = (chatUserAgc > 99 ? '99+' : chatUserAgc + '');
								userallcount.html(chatUserAgc);
								userallcount.css({display:'block',width:(6+(chatUserAgc+"").length*6)+"px"}) ;
							}else{
								userallcountValObj.val(0);
								userallcount.css({display:'none'}) ;
								userallcount.html(0) ;
							}
							/**   lujixiang 20151125 刷新"好友"信息数  --end   **/
														
							/**   lujixiang 20151125 刷新"群聊"信息数  --start   **/
							var agc = 0 ;
							$(".groupList").find(".newmessage").each(function(){
								var newmessagecount = $(this).attr("newmessagecount");
								if(undefined == newmessagecount || '' == newmessagecount){
									newmessagecount = 0 ;
								}
								agc += newmessagecount * 1;
							});
							
							var groupallcountValObj = $("#items-groups-messcount-val") ;
							var groupallcount = $("#items-groups-messcount") ;
							
							if(agc > 0 ){
								groupallcountValObj.val(agc);
								agc = (agc > 99 ? '99+' : agc + '');
								groupallcount.html(agc);
								groupallcount.css({display:'block',width:(6+(agc+"").length*6)+"px"}) ;
							}else{
								groupallcountValObj.val(0);
								groupallcount.css({display:'none'}) ;
								groupallcount.html(0) ;
							}
							/**   lujixiang 20151125 刷新"群聊"信息数  --end   **/
						}
					}
					if(idseq!=null && idseq.length>0){
						//跳转后，设置下导航条
//						setBreadCrumbs(json.folderObjsJson);
						$("#file-breadcrumbs").jFolderCrumb({data:json.folderObjsJson,clickFunName:'forwardFolder'});
					}
					// 分页
					$("#pagingDiv").pagination({
						pages: json.total,
						currentPage: json.page,
						onPageClick: function(pageNumber, event) {
							documentCenter.getFileList(classId, null, "", pageNumber, pageSize);
						}
					});
				});
		
	},
	/**
	 * 获取我的文件（不包括文件夹，且没有层级关系）
	 */
	getMyFileList:function(page, pageSize) {
		var page = (page == null) ? 1 : page;
		$("#filePageNowId").val(page);
		var pageSize = (pageSize == null) ? window.pageSize : pageSize;
		var orderField = $("#fileOrderFieldId").val();
		var orderType = $("#fileOrderTypeId").val();
		
		var url = this.baseUrl+'/rest/onlinefile_filesws/getMyFileList?callback=?';
		var data = {"userName":window.userName,"companyId":g_companyId, "loginUserId":g_userId, "page":page, "pageSize":pageSize,"orderField":orderField,"orderType":orderType };
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			var viewType = $.cookie('myfile-view-type_'+window.userId);
			if (!viewType) {
				viewType = 'icon';
			}
			if (viewType == 'list') { // 列表视图
				json["viewType"] = "list";
			}
			json["myDocument"] = "myDocument";
			json["orderField"] = orderField;
			json["orderType"] = orderType;
			$('#contentBody').html(template('documentAll_templete', json));
			initContextMenu();
			if(window.companyid !="-1"){//xyc 取消滚动条20151120
				$("#content-list").perfectScrollbar('update');
			}
			// 分页
			$("#pagingDiv").pagination({
				pages: json.total,
				currentPage: json.page,
				onPageClick: function(pageNumber, event) {
					documentCenter.getMyFileList(pageNumber, pageSize);
				}
			});
		});
	},
	
	/**
	 * 获取回收站列表
	 */
	getTrashFileList:function(page, pageSize) {
		var page = (page == null) ? 1 : page;
		$("#filePageNowId").val(page);
		var pageSize = (pageSize == null) ? window.pageSize : pageSize;
		var orderField = $("#fileOrderFieldId").val();
		var orderType = $("#fileOrderTypeId").val();
		
		var url = this.baseUrl+'/rest/onlinefile_filesws/getTrashFileList?callback=?';
		var data = {"userName":window.userName,"companyId":g_companyId, "loginUserId":g_userId, "page":page, "pageSize":pageSize,"orderField":orderField,"orderType":orderType };
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			if(!$("#trash").hasClass("active")){ 
				return;
			}		
			var viewType = $.cookie('trash-view-type_'+window.userId);
			if (viewType == 'list') { // 列表视图
				json["viewType"] = "list";
			}
			json["orderField"] = orderField;
			json["orderType"] = orderType;
			$('#contentBody').html(template('trash_file_list_templete', json));
			initContextMenu();
			if(window.companyid !="-1"){//xyc 取消滚动条20151120
				$("#content-list").perfectScrollbar('update');
			}
			// 分页
			$("#pagingDiv").pagination({
				pages: json.total,
				currentPage: json.page,
				onPageClick: function(pageNumber, event) {
					documentCenter.getTrashFileList(pageNumber, pageSize);
				}
			});
			//回收站没有内容  禁用清空回收站按钮
			if(json.count=='0'){
				$('#cleanRecycleBin').attr('disabled',"true"); //添加disabled属性 
			}
		});
	},
	searchTrashListsByKeyWord:function(page, pageSize) {
		var keyWord=$("#searchValHidId").val();
		var page = (page == null) ? 1 : page;
		$("#filePageNowId").val(page);
		var pageSize = (pageSize == null) ? window.pageSize : pageSize;
		var orderField = $("#fileOrderFieldId").val();
		var orderType = $("#fileOrderTypeId").val();
		
		var url = this.baseUrl+'/rest/onlinefile_filesws/getTrashFileList?callback=?';
		var data = {"userName":window.userName,"companyId":g_companyId, "loginUserId":g_userId, keyWord:keyWord,"page":page, "pageSize":pageSize,"orderField":orderField,"orderType":orderType };
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			//var viewType = $.cookie('trash-view-type_'+window.userId);
			// (viewType == 'list') { // 列表视图
				json["viewType"] = "list";
			//}
			json["orderField"] = orderField;
			json["orderType"] = orderType;
			json["type"] = "search";
			$('#action-content-panel-searchTrash #searchResultContent').html(template('trash_file_list_templete', json));
			$("#searchResultContent").height(document.documentElement.clientHeight-240);
			$("#searchTrashResultTitleSpanId").text("搜索结果（"+json.size+"）条");
			$('#action-content-panel-searchTrash').show();
			initRightClickMenu($("#searchResultContent"));
			//$("#content-list").perfectScrollbar('update');
			// 分页
			$("#searchTrashPagingDiv").pagination({
				pages: json.total,
				currentPage: json.page,
				onPageClick: function(pageNumber, event) {
					documentCenter.searchTrashListsByKeyWord(pageNumber, pageSize);
				}
			});
			$("#searchFileInputId").removeClass("searching");
		});
	},
	searchFile:function(query, page, pageSize) {
		var userId = "";
		// 判断是否处于所有文档
		if ($('#file_list_toggle_user').hasClass('active')) { // 正在所有文档
			userId = $("#selectUserId").val();
		}
		var idSeq = $("#classNavi li.last a").attr("idSeq");
		var classId = $("#selectFileClassId").val();
		var className = $("#classNavi li.last a").html();
		var page = (page == null) ? 1 : page;
		var pageSize = (pageSize == null) ? window.pageSize : pageSize;
		
		var url = this.baseUrl+'/rest/onlinefile_filesws/searchFile?callback=?';
		var data = {"companyId":g_companyId, "loginUserId":g_userId, "classId":classId,"className":className,"createrId":userId,"userName":userName, "idSeq":idSeq, "query":query, "page":page, "pageSize":pageSize };
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			$('#search_results_list').html(template('file_list_item_template', json));
			$('#search_results_paging').html(template('file_list_paging_template', json));
			// 调整高度
			resetSearchFileListHeight();
			$("#search_results_scroller").scrollbarTopUpdate();
		});
	},
	/**
	 * 跳转到文件信息页面
	 * fileid:文件id， folderid：文件所在文件夹id
	 * xiewenda 20151207 对此方法做了删减，真是太乱了
	 * 如果有影响 请见谅
	 * wangwenshuo 20160111 添加参数groupFlag，用于聊天窗口查看文件，当前群组对文件的权限判断  
	 * 
	 * wangwenshuo 20160112 添加参数isMyDoc  标识是companyId是公司id还是user_N
	 * 						聊天窗口查看文档可以确定是公司分类文件
	 */
	showFileInfo:function(fileId,folderId,searchType,showHistoryVersion,groupFlag,isMyDoc) {
		//searchType  ，是全文检索调用的才会传这个值，1代表搜索当前分类，2代表全部检索
		$content_panel_body = $("#contentBody4FileInfo");
		$content_panel_body.height($("#mainContentLeft").height()-110)
		/* 添加判断  是我的文档 还是分类 */
		var companyId = documentCenter.getCompanyId(isMyDoc);
		var isMyDocument = documentCenter.isMyDocument(isMyDoc);
		var url = this.baseUrl+'/rest/onlinefile_filesws/getFileInfo?callback=?';
		var data = {"userName":window.userName,"companyId":companyId, "classId":folderId, "fileId":fileId, "userId":g_userId,"groupFlag":groupFlag};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
					if(json.file.userId==g_userId){
						json.canShare = "true";
					}else{
						json.canShare = "false";
					}
					if (showHistoryVersion) {
						json.showHistoryVersion = 1 ;
					} else {
						json.showHistoryVersion = 0 ;
					}
					json["myDocument"] = isMyDocument?"myDocument":"";
					json["g_userId"] = g_userId;
					var file_preview_info_html = template('file_preview_info_template', json);
					var openlevel = json.file.openLevel;
					//1.详细页面第一层判断 是否有权限
					if (json.hasRight == 'false') {
						var file_no_right_html = template('file_preview_template', {"searchType":searchType,"file_preview_info_html":file_preview_info_html});
						//2.检索或是分类下点开的详细页
						if("1"==searchType){
							$("#searchResultHeaderDivId").hide();
							$("#searchResultMainDivId").hide();
							$("#searchReaultFileInfoBackBtn").show();
							$("#searchResultShowFileInfoId").html("");
							$("#searchResultShowFileInfoId").css("height","100%");
							$("#searchResultShowFileInfoId").html(file_no_right_html);
						}else if("anyoneClass"==searchType || "allClass"==searchType || "myDocument"==searchType){
							// $("#action-content-panel div.panel-heading").html(json.file.fileName + '.' + json.file.type);
							 $("#action-content-panel div.panel-body").html(file_no_right_html);
						}else{
							if (showHistoryVersion) {
								$("#contentBody4FileInfo div.fileinfo").remove();
								$("#contentBody4FileInfo .filecontent").prepend(file_preview_info_html);
							} else {
								$content_panel_body.html(file_no_right_html);
																
							}
						}
						$("#u12_img").css("height","210");//显示文件申请按钮等显示完全
						//xiewenda 3.没有权限不显示评论框
						$("#content-footer_comment").css("display","none");
						
					} else {
						var file_preview_html = template('file_preview_template', {"searchType":searchType,"file_preview_info_html":file_preview_info_html});
						if("1"==searchType){
							$("#searchResultHeaderDivId").hide();
							$("#searchResultMainDivId").hide();
							$("#searchReaultFileInfoBackBtn").show();
							$("#searchResultShowFileInfoId").html("");
							$("#searchResultShowFileInfoId").css("height","100%");
							$("#searchResultShowFileInfoId").html(file_preview_html);
						}else if("anyoneClass"==searchType || "allClass"==searchType || "myDocument"==searchType){
							//$("#action-content-panel div.panel-heading").html(json.file.fileName + '.' + json.file.type);
							$("#action-content-panel div.panel-body").html(file_preview_html);
						}else{
							if (showHistoryVersion) {
								$("#contentBody4FileInfo div.fileinfo").remove();
								$("#contentBody4FileInfo .filecontent").prepend(file_preview_info_html);
							} else {
								$content_panel_body.html(file_preview_html);
							}
							
						}
						//判断文件是否显示版本
						if (json.file.version > 1 || json.file.isLast == 0) {
							// 请求历史版本
							 $.ajax({
						            type: 'POST',
						            url: $.appClient.generateUrl({ESDocumentCenter: 'getFileList4Version'},'x'),
						            data: {userName:window.userName,companyId:companyId,classId:json.file.classId,fileId:fileId,fileName:json.file.fileName,fileType:json.file.type, userId:g_userId},
						            dataType : 'json',
						            async:false,//设为同步 
						            success: function(json) {
						            		json["myDocument"] = isMyDocument?"myDocument":"";
						            		json["openlevel"] = openlevel;
						            		json["g_userId"] = g_userId;
						            		if("anyoneClass"==searchType || "allClass"==searchType || "myDocument"==searchType){
						            			//检索是详细页的的版本添加
						            			$('#filecontent #file_preview_history_version').html(template('file_preview_history_version_template', json));
						            		}else{
						            			//非检索的版本添加
						            			$('#contentBody4FileInfo #file_preview_history_version').html(template('file_preview_history_version_template', json));
						            		}
											if (json.size > 0) {
												$('#versions_scroller').perfectScrollbar();
											}
						            },
						            cache: false
						        });
							
						}

					}
					//最后更新滚动条
					if("anyoneClass"==searchType || "allClass"==searchType || "myDocument"==searchType){
						var h = window.innerHeight - 58;
						$("#filecontent").height(h);
						$("#filecontent").perfectScrollbar();
						$("#filecontent").perfectScrollbar("update");
					}else{
						//刷新页面滚动条
						$("#content-list4FileInfo").perfectScrollbar();
						$("#content-list").perfectScrollbar();
						showMainContentLeftFooter(false,1);
					}
					//文件的评论 也是根据检索类型判断
					documentCenter.getFileCommentList(fileId,null,null,null,false,searchType,"",isMyDoc);
		});
	},
	
	/**
	 * 获取文件的历史版本列表
	 */
	getFileList4Version:function(classId, fileId, fileName, fileType,searchType) {
		$.ajax({
            type: 'POST',
            url: $.appClient.generateUrl({ESDocumentCenter: 'getFileList4Version'},'x'),
            data: {userName:window.userName,companyId:g_companyId,classId:classId,fileId:fileId,fileName:fileName,fileType:fileType, userId:g_userId},
            dataType : 'json',
            async:true,//设为同步 
            success: function(json) {
            	json["g_userId"] = g_userId;
            	if("anyoneClass"==searchType || "allClass"==searchType || "myDocument"==searchType){
            		//json["myDocument"] = isMyDocument?"myDocument":"";
					$('#filecontent #file_preview_history_version').html(template('file_preview_history_version_template', json));
					}else{
					$('#contentBody4FileInfo #file_preview_history_version').html(template('file_preview_history_version_template', json));
					}
					if (json.size > 0) {
						$('#versions_scroller').perfectScrollbar();
					}
            },
            cache: false
        });
	},
	
	/**
	 * 获取文件的评论
	 * wangwenshuo 20160124 添加参数isMyDoc(可选参数)  标识是companyId是公司id还是user_N
	 * 						聊天窗口查看文档可以确定是公司分类文件
	 */
	getFileCommentList:function(fileFlag, version, page, pageSize,isRefresh,type,versions,isMyDoc) {
		version = version == null ? "" : version;
		var page = (page == null || page=="") ? 1 : page;
		var pageSize = (pageSize == null || pageSize == "") ? 20 : pageSize;
		
		$.ajax({
            type: 'POST',
            url: $.appClient.generateUrl({ESDocumentCenter: 'getFileCommentsListByFileId'},'x'),
            data: {userName:window.userName,companyId:g_companyId,userId:g_userId,fileFlag:fileFlag,version:version,page:page, pageSize:pageSize,versions:versions},
            dataType : 'json',
            async:false,//设为同步 
            success: function(json) {
            	var isMyDocument = documentCenter.isMyDocument(isMyDoc);
            	$('#content-list4FileInfo').attr("fileId",fileFlag);
            	if(type != "-1"){
            		var h = document.documentElement.clientHeight-220;
            		$('#content-list4FileInfo').height(h);
            	}
				var file_comment_panel_html = template('file_comment_panel_template',json);
				if("anyoneClass"==type || "allClass"==type){
					$('#filecontent #file_preview_comment').html(file_comment_panel_html);
					$("#filecontent").perfectScrollbar("update");
					return ;
				}else if(isMyDocument){
					//在我的文控中暂时没有评论
				}else{
					$('#contentBody4FileInfo #file_preview_comment').html(file_comment_panel_html);
					
					$("#messageValue").width($("#content-list4FileInfo").width()-150).css("margin-left",32);
					$(".sendCommentBtnContainerCls").css("margin-left",$("#content-list4FileInfo").width()-110);
					
					//表情加载
					$('.emotion').qqFace({
						id : 'facebox',
						assign : 'messageValue',
						path : 'templates/onlinefile/images/expression/', //表情存放的路径
						tip : 'f_static_'
					});
					addFoucs("messageValue");
					$("#content-list4FileInfo").perfectScrollbar();
					$("#content-list4FileInfo").perfectScrollbar('update');
				}
            },
            cache: false
        });
		
	},
	
	/**
	 * 查询成员列表
	 */
	queryMembers:function() {
		var url = this.baseUrl+'/rest/onlinefile_filesws/queryMembers?callback=?';
		var data = {"companyId": 0};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			menu_file_members_data = json;
		});
	},
	/**
	 * 下载文件
	 * fileName : 文件名称.后缀
	 *   wangwenshuo 20151215 添加groupFlag 分享到聊天窗口 权限判断使用
	 */
	downloadFile:function(filePath, fileName ,fileId, groupFlag) {
		//加入fileId，判断下当前文件是否被删除了
		if(fileId==undefined || fileId=="undefined" || fileId.length==0){
			showBottomMsg("对不起，无法验证文件是否仍然存在，无法下载！","3");
			return false;
		}
		var fileServerUrl = this.baseUrl.substring(0, this.baseUrl.lastIndexOf("/"));
		/* 添加判断  是我的文档 还是分类 */
		var companyId = documentCenter.getCompanyId();
		
		var url = this.baseUrl+'/rest/onlinefile_filesws/getFileInfo?callback=?';
		var data = {"userName":window.userName,"fileId":fileId, "companyId":companyId,"userId":g_userId,"groupFlag":groupFlag};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(data) {
					if(data.file.isDelete=="1"){
						showBottomMsg("对不起，文件已被删除，无法下载！","3");
						return false;
					}else if(data.isDownload !="1"){
						if(data.file.openLevel == "3"){
							showBottomMsg("对不起，此文件已经设为私密，您无法下载！","3");
						}else{
							showBottomMsg("对不起，您无此文件的下载权限，请到文件详细信息页进行申请！","3");
						}
						return false;
					}else{
						var downloadURL = fileServerUrl+'/fileStoreMainServer/rest/mainFileServer/downloadFile/'+filePath +'?jsessionid=' + window.jsessionid + '&u=' + window.u + '&token=' + window.token ;
						downloadURL = downloadURL + '&t=' + hex_sha1(downloadURL);
						try{ 
				            var elemIF = document.createElement("iframe");   
				            elemIF.src = downloadURL;   
				            elemIF.style.display = "none";   
				            document.body.appendChild(elemIF);   
				        }catch(e){ 
				        	alert(e);
				        }
				        return;
					}
		});
	},
	/**
	 * 设为私密文件
	 * json 为发送到聊天框的消息数据
	 */
	unshareFile:function(folderId, fileId, unshareFrom, isMyDocument, json, isExist, unshareType, newFileName) {
		// var firstClassId = documentCenter.getClassIdByIdSeq(json.file.idSeq);
		$.post($.appClient.generateUrl({ESDocumentCenter:'unshareFile'},'x'),
				{fileId:fileId, userId:g_userId, companyId:g_companyId, classId:folderId, isExist:isExist, unshareType:unshareType, newFileName:newFileName}, function(rst){
					var rstJson = eval('('+rst+')');
					if (rstJson.success == "true") {
						if(unshareFrom == "fileInfo") { // 刷新文件信息页
							documentCenter.showFileInfo(fileId, folderId);
							changeUploadBtn("0");
						}else{
							var page = documentCenter.getCurrentPageNum();
							// 刷新文档列表
							if (isMyDocument) {
								documentCenter.getMyFileList(page);
							} else {
								documentCenter.getFileList(folderId, "", "", page);
							}
						}
						showBottomMsg("文件设为私密成功！","1");
						$('#class_visitor').hide();
						// 发送设为私密记录
						json.file.className=rstJson.GROUPNAME;
						$.WebIM.sendFileMsgNotAction(rstJson.groupFlag,json,"cancelShare");
					}
				});
	},
	setFileOpenlevel:function(fileId,filetitle,folderId,openlevel,flushType,json) {
		$.ajax({type:"POST",
				url:$.appClient.generateUrl({ESDocumentCenter:'setFileOpenlevel'},'x'),
				data:{fileId:fileId,"username":window.userName, userId:g_userId, companyId:g_companyId, classId:folderId,fileName:filetitle,openlevel:openlevel},
				dataType:'json',
				success:function(rst){
					if (rst.success) {
						var page = documentCenter.getCurrentPageNum();
						if(flushType == "fileinfo") { // 刷新文件信息页
							documentCenter.showFileInfo(fileId, folderId);
							//changeUploadBtn("0");
						}else if(flushType == "fileinfosearch") { // 检索详细页
							var searchType = $("#searchValHidId").attr("searchtype");
							documentCenter.showFileInfo(fileId, folderId,searchType);
						}else if(flushType =="myDocument"){
							documentCenter.getMyFileList(page);
						}else if(flushType =="file-item"){
							documentCenter.getFileList(folderId, "", "", page);
						}
						if(flushType != "fileinfosearch"){
							closeContentPanel();
						}
						var messge = "";
						if(openlevel=="1"){
							messge = "companyShare"
						}else if(openlevel=="2"){
							messge = "classShare"
						}else if(openlevel=="3"){
							messge = "myselfShare"
						}
						showBottomMsg("文件设置公开级别成功！","1");
						//$('#class_visitor').hide();
						// 发送设为级别记录
						json.file.className=rst.GROUPNAME;
						$.WebIM.sendFileMsgNotAction(rst.groupFlag,json,messge);
					}
				}
			});
	},
	/**
	 * 点赞/取消赞文件
	 * selected:true 点赞， false 取消赞
	 */
	praiseFile:function(fileId, userId, status) {
		$.post($.appClient.generateUrl({ESDocumentCenter:'praiseFile'},'x'),
			{fileId:fileId, userId:userId, status:status}, function(rst){
			});
	},
	/**
	 * 删除文件
	 */
	deleteFile:function(folderId, fileId) {
		var companyId = documentCenter.getCompanyId();
		$.post($.appClient.generateUrl({ESDocumentCenter:'deleteFile'},'x'),
				{fileId:fileId, userId:g_userId,isShow:'false',companyId:companyId}, function(rst){
					
					/** lujixiang 20150803 添加删除文件是否成功信息提示 **/
					var json = $.parseJSON(rst);
					if(json.success != 'true'){
						showMsg("文件删除失败", "2");
						return ;
					}else{
						showMsg("文件删除成功", "1");
					}
					
//					if($("#myDocument").hasClass("active")){
//						documentCenter.getMyFileList();
//					}else{
						var page = documentCenter.getCurrentPageNum();
						documentCenter.getFileList(folderId,"","",page);
//					}
				});
	},
	/**
	 * 删除历史文件
	 */
	deleteHistoryFile:function(folderId, fileId) {
		/** lujixiang 20151210 注释,历史版本文件删除时,需要更新版本号 **/
		/**
		$.post($.appClient.generateUrl({ESDocumentCenter:'deleteFile'},'x'),
				{fileId:fileId, userId:g_userId,isShow:'true'}, function(rst){
					var json = eval('('+rst+')');
					var newFileId = "";
					if (json.lastFileId != "") {
						newFileId = json.lastFileId;
					} else {
						newFileId = $('#versions_list>.file_list_item').attr("id");
					}
					documentCenter.showFileInfo(newFileId,folderId);
				});
		**/
		
		/** lujixiang 20151210 修改历史版本文件删除**/
		var companyId = documentCenter.getCompanyId();
		$.post($.appClient.generateUrl({ESDocumentCenter:'deleteFileAndUpdateVersion'},'x'),
				{fileId:fileId, userId:g_userId,isShow:'true',companyId:companyId}, function(rst){
					var json = eval('('+rst+')');
					if(json.success == 'true'){
						showMsg("文件删除成功", "1");
						
						var fileItem = $("#content-list4FileInfo .filecontent .fileinfo");
						//var isLast = fileItem.attr("islast");  //删除的文件是否为最新版本   
						var version = fileItem.attr("version");
						//isLast为1是最新版本
						if(1 == json["isLast"]){
							//如果存在多版本  并且多版本最高版本能正确获取  就显示最高版本  否则返回列表
							if(version*1 >1){
							//	var showFileId = $("#versions_list>.file_list_item:first").attr("fileid");
								var showFileId = $("#versions_list div:first-child").attr("fileid");
								if(showFileId){
									documentCenter.showFileInfo(showFileId,folderId);
									return;
								}
							}
							
							//触发返回按钮事件  返回文件列表页面
							$("#mainContentLeft4FileInfo #backToList").click();
							return;
						}else{
							var showFileId = fileItem.attr("fileid");
							documentCenter.showFileInfo(showFileId,folderId);
						}
						return ;
					}else{
						showMsg(json.msg, "2");
					}
					
		});
	},
	/**
	 * 分享文件to用户
	 */
	shareFileToUser:function(fileId, toUserId) {
		$.post($.appClient.generateUrl({ESDocumentCenter:'shareFileToUser'},'x'),
				{fileId:fileId, toUserId:toUserId}, function(rst){
					if (rst == "hasShare") {
						showMsg("该文件已经分享过!");
					}
				});
	},
	
	/**
	 * 分享文件to联系人群组
	 */
	shareFileToGroupOrUser:function(fileId, receiver, isGroup,accessRight, receiverFullName) {
		
		//如果是我的文档分享到  那么文档是从我的文档分享到公司  需要传companyId和user_N
		if(documentCenter.isMyDocument()){
			var myDocCompanyId = documentCenter.getCompanyId();
		}
		/* wangwenshuo 20151215 改为同步  获取结果*/
		var rst = "";
		$.ajax({
			type:'POST',
	        url : $.appClient.generateUrl({ESDocumentCenter : 'shareFileToGroupOrUser'},'x'),
	        data:{fileId:fileId, receiver:receiver, isGroup:isGroup,companyId:g_companyId,fromCompanyId:myDocCompanyId,accessRight:accessRight, receiverFullName:receiverFullName},
	        datatype:"text",
	        async:false,//设为同步 
		    success:function(json){
				rst = json;
		    }
		});
		return rst;
	},
	
	/**
	 * 分享文件to用户  有回调
	 * 
	 */
	shareFileToUserWithCallback:function(fileId, toUserId, receiver, receiverFullName, callbackDivId, content,msgid,test,button) {
		if(test == '分享'){
			$.post($.appClient.generateUrl({ESDocumentCenter:'shareFileToUser'},'x'),
					{fileId:fileId, toUserId:toUserId,msgid:msgid,test:test,button:button,receiver:receiver, receiverFullName:receiverFullName}, function(rst){
						if (rst == "hasShare") {
							showMsg("该文件已经分享过!");
						}else if(rst == "success"){
							showMsg("分享成功!");
							//加入回调
							$.WebIM.approveShareCallback(receiver, callbackDivId, content);
						}
			});
		}else if(test == '取消分享'){
			$.post($.appClient.generateUrl({ESDocumentCenter:'unShareFileToUser'},'x'),
					{fileId:fileId, toUserId:toUserId,msgid:msgid,test:test,button:button,receiver:receiver, receiverFullName:receiverFullName}, function(rst){
						if (rst == "success") {
							showMsg("取消分享成功!");
							//加入回调
							$.WebIM.approveShareCallback(receiver, callbackDivId, content);
						}
							
			});
		}
		
	},
	/**
	 * 拒绝分享文件to用户
	 * 
	 */
	doNotShareFileToUser:function(fileId, toUserId, receiver, callbackDivId, content,msgid,test,button,fileTitle,fromusername) {
//			$.post($.appClient.generateUrl({ESDocumentCenter:'doNotShareFileToUser'},'x'),
//					{fileId:fileId, toUserId:toUserId,msgid:msgid,test:test,button:button}, function(rst){
//						if (rst == "hasShare") {
//							showMsg("该文件已经分享过!");
//						}else if(rst == "success"){
//							showMsg("分享成功!");
//							//加入回调
////							$.WebIM.approveShareCallback(receiver, callbackDivId, content);
//						}
//					});
			//
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/doNotShareFileToUser?callback=?';
		var data = {'companyId':window.companyid,'fileId':fileId,'toUserId':toUserId,'toUserName':receiver,'msgid':msgid,'button':button};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
					function(json) {
				if(json.isOk){
					showMsg("您已拒绝分享!");
					//发送消息给被拒绝人
//					var dcontent = "拒绝了你申请下载【"+fileTitle+"】文件的请求！" ;
//					var name = fromusername.replace('@', '\\40')
//					remote.jsjac.chat.sendMessage(dcontent,name+"@"+remote.jsjac.domain);
				}else{
					showBottomMsg("拒绝失败，请重试...", 2);
				}
			});
		
	},
	/**
	 * 分享多个文件to一个用户
	 * 目前没有调用的地方
	 */
	shareFilesToUserWithCallback:function(fileIds, receiver, titles) {
		$.post($.appClient.generateUrl({ESDocumentCenter:'shareFilesToUser'},'x'),
				{fileIds:fileIds, toUserName:receiver}, function(rst){
					showMsg("分享文件成功");
					$.WebIM.shareFilesCallback(receiver, fileIds);
				});
	},
	/**
	 * 文件页面的操作下拉菜单click
	 * action：操作id  share-file/edit_file/download_file/delete_file
	 * fileId：文件id
	 */
	fileActionClick:function(action, fileId){
		if (action == 'share_file') {
			documentCenter.shareFile(fileId);
		} else if (action == 'edit_file') {
			documentCenter.openEditFileWin(fileId);
		} else if (action == 'download_file') {
			
		} else if (action == 'delete_file') {
			documentCenter.deleteFile(fileId);
		} else {
			
		}
	},
	/**
	 * 更新文件的页面分享状态
	 * true: 分享
	 * false: 不分享
	 */
	updateFileShareStatus:function(divId, status) {
		var $shareItem = $('#' + divId);
		if ($shareItem.length > 0) {
			$shareItem.attr("hasRight", "1");
			var $item_actions = $shareItem.find('div.file-item-actions');
			$item_actions.html("");
			var actions_html = 
				'<a class="file-up" title="赞" name="up"><i class="iconfa-thumbs-up"></i> <span class="praisecount">'+$shareItem.attr("praiseCount")+'</span></a>' +
				'<a class="file-share" title="分享" name="share"><i class="iconfa-share-alt"></i></a>';
			$item_actions.html(actions_html);
		}
	},
	/**
	 * 更新多个文件的页面分享状态
	 * true: 分享
	 * false: 不分享
	 */
	updateFilesShareStatus:function(fileIds, status) {
		fileIds = fileIds.split(",");
		for (var i in fileIds) {
			var $shareItem = $('#file_id_' + fileIds[i]); 
			if ($shareItem.length > 0) {
				var $item_actions = $shareItem.find('div.file-item-actions');
				$item_actions.html("");
				var actions_html = 
					'<a class="file-up" title="赞" name="up"><i class="iconfa-thumbs-up"></i> <span class="praisecount">'+$shareItem.attr("praiseCount")+'</span></a>' +
					'<a class="file-share" title="分享" name="share"><i class="iconfa-share-alt"></i></a>';
				$item_actions.html(actions_html);
			}
		}
	},
	
	/**
	 * 分享到某个路径下
	 */
	shareToFolderPath:function(fileId, folderName, folderId, openlevel) {
		$.post($.appClient.generateUrl({ESDocumentCenter:'shareToFolderPath'},'x'),
				{fileId:fileId, folderName:folderName, folderId:folderId, openlevel:openlevel}, function(rst){
					if (rst == "success") {
						// 在群组聊天里发送上传文件的消息
						$.WebIM.sendFileMsg(fileId, "shareTo");
						showBottomMsg("分享成功","1");
						// 关闭分享到窗口
						if ($("#shareToMainDivId").hasClass("myDocument")) {
							var page = documentCenter.getCurrentPageNum();
							documentCenter.getMyFileList(page);
						} else {
							// 刷新详细页面
							documentCenter.showFileInfo(fileId, folderId);
						}
						$("#shareCancelBtnId").click();
					} else {
						showBottomMsg("文件分享到失败！","3");
					}
				});
	},
	
	/**
	 * wangwenshuo 20151020 复制到
	 */
	copyToFolderPath:function(fileId, folderName, folderId, openlevel) {
		$.post($.appClient.generateUrl({ESDocumentCenter:'copyToFolderPath'},'x'),
				{fileId:fileId, folderName:folderName, folderId:folderId, openlevel:openlevel}, function(data){
					var json = $.parseJSON(data);
					if (json.success) {
						// 在群组聊天里发送上传文件的消息
						$.WebIM.sendFileMsg(fileId, "upload");
						var msg = "复制成功";
						if(json.isRename=="true") msg += "，文件重命名为："+json.fileName;
						showBottomMsg(msg,"1");
						
						// 如果是我的文档进行复制  复制后刷新我的文档
						if ($("#copyToMainDivId").hasClass("myDocument")) {
							var page = documentCenter.getCurrentPageNum();
							documentCenter.getMyFileList(page);
						}
						$("#copyCancelBtnId").click();
					} else {
						showBottomMsg("文件分享到失败！","3");
					}
				});
	},
	
	/**
	 * 打开文件编辑窗口
	 */
	openEditFileWin:function(fileId) {
		$.ajax({
			url : $.appClient.generateUrl({ESDocumentCenter : 'openEditFileWin' }, 'x'),
			data: {fileId:fileId},
			type : 'post',
			success : function(data) {
				$.dialog({
					title : '文件编辑',
					fixed : false,
					resize : false,
					width:460,
					opacity : 0.1,
					content : data,
					padding : 0,
					okValue : '保存',
					ok : function() {
						var total = 0;
						total = total + $("#editFileInfoOtherPropDiv input").length + $("#editFileInfoOtherPropDiv select").length;
						if(total == 0){
							return true;
						}
						var KeyLst = new Array();
						var valueLst = new Array();
						var validate = true;
						$("#editFileInfoOtherPropDiv input").each(function(){ 
							var validateOne = documentCenter.validate($(this));
							if(validateOne == false){
								KeyLst = new Array();
								valueLst = new Array();
								validate = false;
								return false;
							}
							KeyLst.push($(this).attr("name"));
							valueLst.push ($(this).val());
						  });
						if(!validate){
							return false;
						}
						$("#editFileInfoOtherPropDiv select").each(function(){ 
							KeyLst.push($(this).attr("name"));
							valueLst.push ($(this).val());
						  });
						
						$.ajax({  
						    url: $.appClient.generateUrl({ESDocumentCenter : 'editFileOtherProp' }, 'x'), 
						    data: { "fileId": fileId,"KeyLst":KeyLst,"valueLst":valueLst },  
						    dataType: "json",  
						    type: "POST",  
						    success: function (responseJSON) {  
						    	if(responseJSON==true || responseJSON == 'true'){
						    		showMsg("修改成功！","1");
						    	}else{
						    		showMsg("修改失败！","1");
						    	}
						    }  
						}); 
					},
					cancelValue: '关闭',
				    cancel: true
				}).show();
			}
		});
	},
	validate:function(obj){
		if($(obj).attr("fileType")=="number" && $(obj).val().length>0){
			var reg = /^\d+$/i;
			if($(obj).val().search(reg)==-1){
				$(obj).parent().showTooltips("请填写纯数字。", 5000) ;
				return false;
	    	}
		}else if($(obj).attr("fileType")=="float" && $(obj).val().length>0){
			var length = $(obj).attr("floatlength").split(".");
			if(eval($(obj).val().length)>eval(length[0])){
				$(obj).parent().showTooltips("长度超长了。", 5000) ;
				return false;
			}
			eval("var fregNew = /^\\d+(\\.\\d{0,"+length[1]+"})?$/;");
			if($(obj).val().search(fregNew)==-1){
				$(obj).parent().showTooltips("小数点后最多"+length[1]+"位。", 5000) ;
				return false;
	    	}
		}else{
			return true;
		}
	},
	/**
	 * 设置文件路径的面包屑
	 * folderId:文件夹id
	 * renderId:绑定面包屑的div id
	 */
	setupFolderPath:function(folderId, renderId, clickFunName) {
		/** 如果是我的文档创建文件夹 那么companyId应该为‘user_’+userId */
		var companyId = documentCenter.getCompanyId();
		
		var url = this.baseUrl+'/rest/onlinefile_filesws/getFolderPath?callback=?';
		var data = {"companyId":companyId, "folderId":folderId};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
					json.path.shift();
					var w = $("#content-heading").outerWidth(true) - $("#contentHeader_right").outerWidth(true) - 80;
				$("#"+renderId).width(w).jFolderCrumb({data:json.path,clickFunName:clickFunName});
		});
	},
	setupFolderPathForShare:function(folderId, renderId, clickFunName,renderObj) {
		
		var url = this.baseUrl+'/rest/onlinefile_filesws/getFolderPath?callback=?';
		var data = {"companyId":g_companyId, "folderId":folderId};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
					json.path[0].name="全部分类";
				if(renderObj){
					renderObj.width(400).jFolderCrumb({data:json.path,clickFunName:clickFunName});
					return;
				}
				$("#"+renderId).width(400).jFolderCrumb({data:json.path,clickFunName:clickFunName});
		});
	},
	/**
	 * 回收站文件恢复   isfile：1文件  0文件夹  2批量操作
	 */
	fileRestore:function(fileId,trashId,isfile,isSearch,companyId) {
		$.post($.appClient.generateUrl({ESDocumentCenter:'fileRestore'},'x'),
				{fileId:fileId,trashId:trashId, userId:g_userId,companyId:companyId}, function(rst){
					var msg = isfile=="1"?"文件":isfile=="0"?"文件夹":"批量文件(文件夹)";
					if (rst == "false") {
						showBottomMsg(msg+"恢复失败！","3");
					} else{
						if(rst == "true"){
							showBottomMsg(msg+"恢复成功","1");
						}else{
							showBottomMsg("以下文件(文件夹)恢复失败："+rst,"2");
						}
						/** 发送恢复信息到聊天窗口 **/
						if(companyId.indexOf("user_")==-1){
							var activeItems = $('#content-list div.file-item.active:not(.myDocument)');	// 被选中文件夹和文件
							var groupFlag = $('.main-left .active').attr('flag');
						    activeItems.each(function(){
								var item = $(this);
								var fileName = item.attr("fileName");
								var isfile = item.attr("isfile");
								var fileId = item.attr('fileid');
								if(rst=="true" || (","+rst+",").indexOf(","+fileName+",")==-1){
									if('0' == isfile){
										$.WebIM.sendFileMsg(fileId,"restoreFolder");
									}else{
										$.WebIM.sendFileMsg(fileId,"restoreFile");
									}
								}
							});
						}
						
						// 刷新
						var page = documentCenter.getCurrentPageNum();
						if(isSearch){
							documentCenter.searchTrashListsByKeyWord(page);
						}else{
							documentCenter.getTrashFileList(page);
						}
					}
		});
	},
	
	/**
	 * 回收站文件彻底删除
	 */
	fileDestroy:function(fileId,isfile,isSearch,companyId) {
		$.post($.appClient.generateUrl({ESDocumentCenter:'fileDestroy'},'x'),
		{fileId:fileId, userId:g_userId,companyId:companyId}, function(rst){
			if (rst == "success") {
				showBottomMsg("彻底批量删除文件(文件夹)成功！","1");
				// 刷新
				var page = documentCenter.getCurrentPageNum();
				if(isSearch){
					documentCenter.searchTrashListsByKeyWord(page);
				}else{
					documentCenter.getTrashFileList(page);
				}
			} else {
				showBottomMsg("彻底批量删除文件(文件夹)失败！","3");
			}
		});
	},
	/**
	 * 标记/取消标记为常用分类
	 */
	starClass:function(classId,status) {
		$.post($.appClient.generateUrl({ESDocumentCenter:'starClass'},'x'),
				{classId:classId,userId:g_userId,status:status}, function(rst){
					//documentCenter.getClassStarList(window.userId);
					//xiewenda 下边加载为异步了 所以注释掉 
					//$(".main-left li[data-class-id='"+classId+"']").attr("isStar",status);
					documentCenter.getClassList(window.userId,classId,status);

		});
	},
	/**
	 * 撤销分享文件联系人群组
	 * --------------wangwenshuo 20151127--新增可选参数toCompanyId----------
	 * 分享文档来源为：分类分享  我的文档分享
	 *  	toCompanyId 用于我的文档分享撤销  companyId为目标分类的公司id  fromCompanyId为user_N  谁分享出来的
	 */
	backoutFile:function(fileId, receiver, isGroup, accessRight ,companyId) {
		var data = {fileId:fileId, receiver:receiver, isGroup:isGroup,companyId:g_companyId,accessRight:accessRight}
		if(companyId){
			data["fromCompanyId"]=companyId;
		}
		$.post($.appClient.generateUrl({ESDocumentCenter:'backoutFile'},'x'),
				data, function(rst){
					if(rst=="success"){
						showBottomMsg("撤销成功！","1");
					}else if(rst=="haveBacked"){
						showBottomMsg("该分享已被撤销！","1");
					}else {
						showBottomMsg("撤销失败！","3");
					}
				});
	},
	/**
	 * 删除文件评论
	 */
	deleteComment:function(commentId,fileFlag,type) {
		$.post($.appClient.generateUrl({ESDocumentCenter:'deleteComment'},'x'),
				{commentId:commentId}, function(rst){
					showBottomMsg("删除评论成功！","1");
					// 获取最后一页
					var lastPage = '1';
					// 刷新评论
					documentCenter.getFileCommentList(fileFlag,"",lastPage,"",false,type,null);
//					var versions = "";
//					setTimeout(function (){
//						var versionAction = $('#file_preview_history_version .file_list_item');
//							versionAction.each(function(){
//								versions += $(this).attr('fileid')+",";
//								
//						});
//						documentCenter.getFileCommentList(fileFlag,"",lastPage,"",false,type,versions);
//						},800);
				});
	},
	
	
	/**
	 * 返回companyId   如果是公司返回公司id  如果是我的文档内容 返回“user_N” N为userId 
	 * 
	 */
	getCompanyId:function(isMyDoc){
		
		//传进来了参数  使用参数判断是否是我的文档
		if(isMyDoc!=null && isMyDoc!=undefined){
			if(isMyDoc){
				return "user_"+window.userId;
			}else{
				return window.companyid;
			}
		}
		
		/** 如果是我的文档文件文件夹操作 那么companyId应该为‘user_’+userId */
		var companyId = window.companyid;
		var searchType = $("#searchValHidId").attr("searchType");
		var isMyDocument = $("#myDocument").hasClass("active");
		//三种情况 在我的文档操作文件  检索在我的文档中操作检索文件  选中我的当检索全部操作文件
		if(searchType=="myDoucument" || (isMyDocument && !(searchType=="allClass"))){
			companyId = "user_"+window.userId;
		}
		return companyId;
	},
	/**
	 * 区分文件（夹）操作是我的文档操作  还是分类下操作
	 * wangwenshuo 20160112 添加参数判断 是为了调用方法处写的简单，一致
	 */
	isMyDocument:function(isMyDoc){
		
		//传进来了参数  使用参数判断是否是我的文档
		if(isMyDoc!=null && isMyDoc!=undefined){
			return isMyDoc;
		}
		
		var searchType = $("#searchValHidId").attr("searchType");
		var isMyDocument = $("#myDocument").hasClass("active");
		//三种情况 在我的文档操作文件  检索在我的文档中操作检索文件  选中我的当检索全部操作文件
		if(searchType=="myDoucument" || (isMyDocument && !(searchType=="allClass"))){
			
		}else{
			isMyDocument = false;
		}
		return isMyDocument;
	},
	/**
	 * 我的文档文件分享到分类下
	 * @param fromCompanyId 	来源公司Id，实为“user_N”
	 * @param fileId 			分享文件的fileId
	 * @param g_companyId  		目标公司Id
	 * @param folderId  		目标文件夹Id
	 * @param openlevel 		公开级别
	 */
	fileShareTo:function(fromCompanyId,fromFileId,toCompanyId,toFolderIds,toFolderNames){
		$.post($.appClient.generateUrl({ESDocumentCenter:'fileShareToClass'},'x'),
			{fromCompanyId:fromCompanyId,fromFileId:fromFileId,toCompanyId:toCompanyId,toFolderIds:toFolderIds,toFolderNames:toFolderNames,userId:g_userId}, function(json){
				var json = eval('('+json+')');
				var msg = "分享处理完成";
				if(json.haveSkip){
					msg += "，跳过的分享位置："+json.skipFolderNames;
				}
				if(json.haveFail){
					msg += "，失败的分享位置："+json.failFolderNames;
				}
				if(json.haveSuccess && !json.haveFail){
					msg += "，分享成功！";
					closeContentPanel();
				}
				showBottomMsg(msg+"！","1");
				
				if(json.fileIds.length>0){
					var fileIds = json.fileIds.split(",");
					for (var i in fileIds) {
						// 在群组聊天里发送上传文件的消息
						$.WebIM.sendFileMsg(fileIds[i], "upload");
					}
				}
				
			});
	},
	showDragFileInfo:function(fileId,folderId,isFile,idSeq){
		closeContentPanel();
	    /** 如果是我的文档创建文件夹 那么companyId应该为‘user_’+userId */
		var companyId = documentCenter.getCompanyId();
		
		// 判断文件夹或文件是否被删除
		var url = window.onlinefilePath+'/rest/onlinefile_filesws/checkFileDelete?callback=?';
		var data = {"fileId":fileId, "companyId":companyId};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
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
							var html = template('file_info_template', {"fileIdSeq": idSeq, "myDocument": isMyDocument?"myDocument":"", "currentPageNum":currentPageNum,"isupload":isupload});
							$('#mainContentLeft4FileInfo').show();
							$('#mainContentLeft').hide();
							$('#mainContentLeft4FileInfo').html(html);
							documentCenter.showFileInfo(fileId, folderId);
							documentCenter.setupFolderPath(fileId,"file-details-breadcrumbs","forwardFolderForFile");
						}
					}
		});
	
	},
	/**
	* wangwenshuo 20151216 保存到我的文档
	*/
	fileSaveToMyDoc:function($fileItem){
		var itemId = $fileItem.attr("id");
		var fileId = $fileItem.attr("fileid");
		$.dialog({
		title: '保存到我的文档',
			content : '确定要保存选中文件到我的文档吗？',
			okValue : '确定',
			cancelValue : '取消',
			cancel : true,
			ok : function() {
				//执行保存操作 默认保存到我的文档
				var fromCompanyId = g_companyId;
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
		}).showModal();
	}
}
