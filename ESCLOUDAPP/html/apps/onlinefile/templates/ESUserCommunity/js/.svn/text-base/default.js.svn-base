//统计评论条数
var userCommunity = {
	baseUrl: window.onlinefilePath,
	/**加载时获取所有帖子信息**/
	getCommunityTypelist:function(page){
		var url = this.baseUrl+'/rest/wechat/getCommunitylist?callback=?';
		var data = {'username':window.userName,'pageid':page};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
				 var pageNo = "";
				 $('#typeofCommnity').hide();
				if(json.alllist !=null &&　json.alllist.length > 0){
					pageNo=json.alllist[0].maxcount;
	    		}else{
	    			pageNo=1;
	    		} 
		    	$('#navListCommnity').html(template('onlineUsersCommnutyList', {'data':json.alllist,'username':window.userName}));
			    //分页
		    	 $("#PaginationCommunity").pagination({
		    		pages:Math.floor((pageNo-1)/8)+1,
		    		currentPage: page/8+1,
		    		onPageClick: function(pageNumber, event) {
		    			//绑定页数，绑定类型
		    			$("#typetoshowCommunity").attr("typeflg","所有帖子")
		    			$("#typetoshowCommunity").attr("pageflg",(pageNumber-1)*8);
		    			$("#typetoshowCommunity").attr("idflg","");
		    			addAllcommunitylist((pageNumber-1)*8);
		    			return false;
		    		}
		    	});
		});
	}
	,
	/**按类型获取发帖标题列表**/
	getCardTitleList:function(lisss,page){
		var url = this.baseUrl + '/rest/wechat/getCommunityTypelist?callback=?';
		var data = {'ctype':lisss,'pageid':page};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
			function(json){
	        $('#userownCommnity').hide();
	        $('#main-community-list').show();
	        $('#typeofCommnity').hide();
	        $('#typetoshowCommunity').show();
	    	$('#navListCommnity').html(template('onlineUsersCommnutyList', {'data':json.alllist,'username':window.userName}));
	    	var pageNo = "";
	    	if(json.alllist !=null &&　json.alllist.length > 0){
	    		pageNo = json.alllist[0].maxcount;
	    	}else{
	    		pageNo = 1;
	    	}
	    	$("#PaginationCommunity").pagination({
    			pages:Math.floor((pageNo-1)/8)+1,
	    		currentPage: page/8+1,
	    		onPageClick: function(pageNumber, event) {
	    			//绑定页数，绑定类型
	    			$("#typetoshowCommunity").attr("pageflg",(pageNumber-1)*8);
	    			$("#typetoshowCommunity").attr("typeflg",$("#"+lisss.trim()).text());
	    			$("#typetoshowCommunity").attr("idflg",lisss);
	    			userCommunity.getCardTitleList(lisss,(pageNumber-1)*8);
	    			return false;
	    		}
	    	});
		});
	}
	,
	/**获取我的用户社区列表**/
	getMyCommunity:function(pageNo){
		$('#main-community-list').show();
		$('#userinfomainCommunity').hide();
		$('#typeofreply').hide();
		$("#typetoshowCommunity").attr("typeflg","我的帖子");
		$('#comm_module_title').html('我的帖子');
		$.getJSON(this.baseUrl+'/rest/wechat/getMyCommunity?callback=?'
			,{'username':window.userName,'pageNo':pageNo}
			,function(json){
				$('#navListCommnity').html(template('myCommnutyList', {'title':json.data,'username':window.userName}));
				$('#typeofCommnity').hide();
				var pageStart = "";
				if(json.data !=null &&　json.data.length > 0){
					pageStart=json.data[0].maxcount;
	    		}else{
	    			pageStart=1;
	    		}
				$("#PaginationCommunity").pagination({
		    		pages:Math.floor((pageStart-1)/8)+1,
		    		currentPage: pageNo/8+1,
		    		onPageClick: function(pageNumber, event) {
		    			//绑定页数，绑定类型
		    			$("#typetoshowCommunity").attr("typeflg","我的帖子")
		    			$("#typetoshowCommunity").attr("pageflg",(pageNumber-1)*8);
		    			$("#typetoshowCommunity").attr("idflg","");
		    			userCommunity.getMyCommunity((pageNumber-1)*8);
		    			return false;
		    		}
		    	});
		});
	}
	,
	/**编辑帖子内容**/
	editComunityContext:function(userid){
		$.getJSON(this.baseUrl + '/rest/wechat/editComunityContext?callback=?'
		,{'userid':userid}
		,function(json){
			$('#commnitytitleids').val(json.data[0].title);
			CKEDITOR.instances.myTextarea.setData(json.data[0].info);
		});
	}
	,
	/**删除用户帖子**/
	deleteCard:function(cardId,pubtype){
		dialog({
			title: '删除确认',
			content: '所有的内容都将被删除,并且不能恢复。 确认要删除该帖子吗?',
			width: 200,
			quickClose: true,
			okValue: '删除',
			ok: function () {
				var url = window.onlinefilePath+'/rest/wechat/deleteCommunitytie?callback=?';
				var data = {'userid':cardId};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
					function(json) {
					if(json.sucess){
						showBottomMsg(json.msg,1);
						userCommunity.addAllcommunitylist(0);
					}else{
						showBottomMsg(json.msg,3);
					}
				});
			},
			cancelValue: '取消',
			cancel: true
		}).show();
		return true;
	}
	,
	/**获取用户评论信息**/
	onlineReply:function(plId){

		// var commentCount;
		var url = window.onlinefilePath+'/rest/wechat/getReplylist?callback=?';
		var data = {'pl_context_id':plId};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
		    	$('#reply_list').html(template('onlineReplyList', {'data':json.replylist,'userName':window.userName,'fullName':window.fullName}));
		    	/**统计评论数**/
		    	$.ajax({
				 type:'POST',
				 url: $.appClient.generateUrl({ESUserCommunity : 'getCommentTotal'},'x'),
				 data:{'cardId':plId},
				 datatype:'json',
				 success:function(data){
				 	  commentCount = JSON.parse(data) ;
					  $('#countNum').text(commentCount+' 次评论');
				 }
		    	});
				var _w = parseInt($('#reply_list').width());
		    	$('#comment-show-info img').each(function(i){
					var img = $(this);
					var realWidth;//真实的宽度
					var realHeight;//真实的高度
					$("<img/>").attr("src", $(img).attr("src")).load(function() {
						realWidth = this.width;
						realHeight = this.height;
						if(realWidth >= _w){
							$(img).css("width","100%").css("height","auto");
						}
						else{
							$(img).css("width",realWidth+'px').css("height",realHeight+'px');
						}
					});
					
				});
		    	if($('#reply_list').html() == ''){
		    		$('.pl_title').hide();
		    	}
		    	$(".a_reply").hide();
		    	$(".a_delete").hide();
		    	$(".hide_error").hide();
		});
	}
	,
	/**获取最新消息提示统计**/
	getCountReplyInfo:function(){
		var url = window.onlinefilePath+'/rest/wechat/getCountReplyInfo?callback=?';
		var data = {'userName':window.userName};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
				$('#myCommunity_callBackInfo').html(template('newCallBackInfo',{'count':json.count}));
		});
		
	}
	,
	/**删除用户评论**/
	deleteComment:function(dataid,contextid){
/*		$("#delCommentlWin").modal("show");*/
			dialog({
				title: '删除评论',
				content: '确认要删除该评论吗?',
				width: 200,
				height:30,
				quickClose: true,
				okValue: '删除',
				ok: function () {
					var url = window.onlinefilePath+'/rest/wechat/deleteComment?callback=?';
					var data = {'pl_id':dataid,'contextid':contextid,'pl_username':window.userName};
					var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
					jQuery.getJSON(url, ret.data,
						function(json) {
						if(json.sucess == true){
							 showBottomMsg(json.msg,1);
							 onlineReply(contextid);
							/*$("#delCommentlWin").modal("hide");
							 $("#replytextarea").val();*/
						}else{
							showBottomMsg(json.msg,3);
						}
					});
				},
				cancelValue: '取消',
				cancel: true
			}).show();	
	
		return true;
	}
	,
	/**获取用户社区板块类型**/
	gettypetolistforcommunity:function(lisss,type,page){
		$("#typetoshowCommunity").attr("pageflg",page);
		$("#typetoshowCommunity").attr("typeflg",type);
		$("#typetoshowCommunity").attr("idflg",lisss);
		if(type != "所有帖子"){
			if(type == "新回复"){
					$("#userreply").removeClass("click_after")
					$("#usertec").removeClass("click_after")
					$("#guanfang").removeClass("click_after")
					$("#allcard").removeClass("click_after");
					$("#newuser").removeClass("click_after");
					this.getCallBackNewMessage(null,page);
			}else if(type == "我的帖子"){
				$("#userreply").removeClass("click_after")
				$("#usertec").removeClass("click_after")
				$("#guanfang").removeClass("click_after")
				$("#allcard").removeClass("click_after");
				$("#newuser").removeClass("click_after");
				this.getMyCommunity(page);
			}else{
			    $("#community_type_select option").each(function(){ //遍历全部option
			        var txt = $(this).text(); //获取option的内容
			       if(type == txt){
			    	   $(this).attr("selected", true);
			       }
			    });
			    position(lisss);
			    /*
				if(type == "新手上路"){
					$("#userreply").removeClass("click_after")
					$("#usertec").removeClass("click_after")
					$("#guanfang").removeClass("click_after")
					$("#allcard").removeClass("click_after");
					$("#newuser").addClass("click_after");
				}
				if(type == "用户反馈"){
					$("#usertec").removeClass("click_after")
					$("#guanfang").removeClass("click_after")
					$("#newuser").removeClass("click_after");
					$("#allcard").removeClass("click_after");
					$("#userreply").addClass("click_after")
					
				}
				if(type == "使用技巧"){
					$("#userreply").removeClass("click_after")
					$("#guanfang").removeClass("click_after")
					$("#newuser").removeClass("click_after");
					$("#allcard").removeClass("click_after");
					$("#usertec").addClass("click_after")
				}
				if(type == "产品公告"){
					$("#userreply").removeClass("click_after")
					$("#newuser").removeClass("click_after");
					$("#usertec").removeClass("click_after")
					$("#allcard").removeClass("click_after");
					$("#guanfang").addClass("click_after")
				}
				*/
					this.getCardTitleList(lisss,page);
			}
			$('#comm_module_title').html(type);
		}else{
			//发布所有帖子，默认为：新手上路
			$("#community_type_select option:first").attr("selected", true);
			this.addAllcommunitylist(page);
		}
		
	}
	,
	/**点击板块每个类型添加样式**/
	addAllcommunitylist:function(page){
		$("#allcard").addClass("click_after");
		$("#guanfang").removeClass("click_after")
		$("#userreply").removeClass("click_after")
		$("#newuser").removeClass("click_after");
		$("#usertec").removeClass("click_after")
		this.tofatiemaincommunity();
		$('#comm_module_title').html('所有帖子');
		$('#typeofCommnity').hide();
		this.getCommunityTypelist(page);
	}
	,
	/**显示主页面数据**/
	tofatiemaincommunity:function(){
		 $('#userownCommnity').hide();
		// $('.nav_module_title_list').show();
		 $('#main-community-list').show();
		 $('#typeofCommnity').show();
		 $('#typetoshowCommunity').show();
	}
	,
	/**用户点赞/取消点赞**/
	praiseCard:function(cardId,status,userId){
		$.post($.appClient.generateUrl({ESUserCommunity:'praiseCard'},'x'),
				{'cardId':cardId, 'userId':userId, 'status':status}, function(rst){
		});
	}
	,
	//获取最新的被评论的贴子
	getCallBackNewMessage:function(flag,page){
		var typeflg=$("#typetoshowCommunity").attr("typeflg");
		if(typeflg == undefined){
			$("#typetoshowCommunity").attr("typeflg","所有帖子");
		}else{
			$("#typetoshowCommunity").attr("typeflg",typeflg);
		}
			$('#userownCommnity').hide();
	        $('#main-community-list').show();
	        $('#typeofreply').hide();
	        $('#userinfomainCommunity').hide();
	        
	        var url = window.onlinefilePath+'/rest/wechat/getCallBackNewMessage?callback=?';
			var data = {'userName':window.userName,'page':page};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
				function(json) {
			//$('#comm_module_title').html('新回复');
			//onclick="js_dismiss_all_message(0)" style="float:right;font-size: xx-small;margin-right:2em;font-weight: 100;">全部标为已读</a>
			$('#comm_module_title').html('新回复<a href="javascript:;"');
			$('#navListCommnity').html(template('communitynNewMessageTemplet',{'datas':json.datas}));
			var pageNo = "";
			$('#typeofCommnity').hide();
			if(json.count !=null &&　json.count > 0){
				pageNo=json.count;
	   		}else{
	   			pageNo=1;
	   		} 
			//分页
	    	 $("#PaginationCommunity").pagination({
	    		pages:Math.floor((pageNo-1)/10)+1,
	    		currentPage: page/10+1,
	    		onPageClick: function(pageNumber, event) {
	    			
	    			//绑定页数，绑定类型
	    	//		$("#typetoshowCommunity").attr("typeflg",typeflg);
	    			$("#typetoshowCommunity").attr("pageflg",(pageNumber-1)*10);
	    			$("#typetoshowCommunity").attr("idflg","");
	    			getCallBackNewMessage(flag,(pageNumber-1)*10);
	    			return false;
	    		}
	    	});
			
			if(flag != 1){
				if(json.count === 0){
					$('#allcard').trigger('click');
				}
			}
		});
	}
}

/**去除空格**/
function trim(s){
	return $.trim(s); 
}

/**点击所有帖子板块时**/
function addAllcommunitylist(page){
	userCommunity.addAllcommunitylist(page);
}

/**获取用户社区板块类型**/
function gettypetolistforcommunity(lisss,type,page){
	//显示发帖后的标题列表
	userCommunity.gettypetolistforcommunity(lisss,type,page);
}

/**获取我的用户社区列表**/
function getMyCommunity(pageNo){
//	$('.nav_module_title_list').show();
 //	$('#main-community-list').show();
 //	$('#userinfomainCommunity').hide();
 //	$('#typeofreply').hide();
	userCommunity.getMyCommunity(pageNo);
}

/**获取评论内容**/
function onlineReply(plId){
	userCommunity.onlineReply(plId);
}
/**删除用户评论**/
function deleteComment(dataid,contextid){
	userCommunity.deleteComment(dataid,contextid);
}

/**点击发贴按钮**/
function toSendPostSection(){
	//光标自动定位到输入框中
//	$('.nav_module_title_list').hide();
	//获取右侧栏，内容设置为空
	$(".abtntobottom").attr("cardId","");
	$('#commnitytitleids').val("");
	CKEDITOR.instances.myTextarea.setData("");
	
	
	$("#typetoshowCommunity li").each(function(){ //遍历全部option
		if($(this).attr("class") == 'click_after'){
			var type=$(this).text();
			if(trim(type) != '所有帖子'){
				$("#community_type_select option").each(function(){ //遍历全部option
			        var txt = $(this).text(); //获取option的内容
			       if(trim(type) == trim(txt)){
			    	   $(this).attr("selected", true);
			       }
			    });
			}
		}
	});
	$('#main-community-list').hide();
	$('#userinfomainCommunity').hide();
	$('#typeofreply').hide();
//	$('#userGridListCommnity').animate({scrollTop: 0/*$(document).height()*/}, 300);
	$('#typeofCommnity').show(); 
	$("#commnitytitleids").focus();
	 return true;
}

/**点击标题按钮显示帖子内容**/

function onlineCommnunityClick(nameid,pubtype,flag){
	var cardId = '';
	cardId = nameid;
	$('#typeofCommnity').hide();
	$('#main-community-list').hide();
	$('#typetoshowCommunity').hide();
	$('#userinfomainCommunity_title').html("");
	$('#userinfomainCommunity_info').html("");
	$('#userownCommnity').show();
	$('#typetoshowCommunity').show();
	$.ajax({
		    data: {'nameid': nameid,'flag':flag},
			type:'POST',
	        url : $.appClient.generateUrl({ESUserCommunity : 'fileinfoCommunity'},'x'),
		    success:function(data){
		    	$('#userownCommnity').html(data);
		    },
		    cache:false
	});
	position (pubtype);
}

function position (pubtype){
	/**点击后自动定位**/
	switch (pubtype) {
		case 'newuser':
			$("#userreply").removeClass("click_after")
			$("#usertec").removeClass("click_after")
			$("#guanfang").removeClass("click_after")
			$("#allcard").removeClass("click_after");
			$("#newuser").addClass("click_after");
			break;
		case 'userreply':
			$("#usertec").removeClass("click_after")
			$("#guanfang").removeClass("click_after")
			$("#newuser").removeClass("click_after");
			$("#allcard").removeClass("click_after");
			$("#userreply").addClass("click_after")
			break;
		case 'usertec':
			$("#userreply").removeClass("click_after")
			$("#guanfang").removeClass("click_after")
			$("#newuser").removeClass("click_after");
			$("#allcard").removeClass("click_after");
			$("#usertec").addClass("click_after")
			break;
		case 'guanfang':
			$("#userreply").removeClass("click_after")
			$("#newuser").removeClass("click_after");
			$("#usertec").removeClass("click_after")
			$("#allcard").removeClass("click_after");
			$("#guanfang").addClass("click_after")
			break;
	}
	
}


/*-------------------验证标题框输入字符的长度---------------------*/
function checkLenTitle(obj,len){  
	  if(obj.value.replace("/[^/x00-/xFF]/g",'**').length>=len){
		   obj.value=leftUTFString(obj.value,len);
		   showBottomMsg('亲,请输入3-70个字符!',2);
		   $("#commnitytitleids").css('border-color','#FF0000');
		   return false ;
	  }else{
		  $("#commnitytitleids").css('border-color','#ccc');
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
/*-------------------验证标题框输入字符的长度---------------------*/

/**点赞**/
function upPraise(objThis,cardId) {
	var $obj = $(objThis);
	var selected = false;
	if ($obj.hasClass('selected')) {
		$obj.removeClass('selected');
		selected = false;
		var praiseCount = parseInt($obj.find("span.praisecount").html());
		if (praiseCount == 1) {
			$obj.find("span.praisecount").html("");
		} else {
			$obj.find("span.praisecount").html(praiseCount-1);
		}
	} else {
		$obj.addClass('selected');
		selected = true;
		var praiseCount = parseInt($obj.find("span.praisecount").html());
		if (!praiseCount) {
			$obj.find("span.praisecount").html("1");
		} else {
			$obj.find("span.praisecount").html(praiseCount+1);
		}
	}
	userCommunity.praiseCard(cardId,selected,window.userId);
	return false;
}


(function ($){
	$(".abtntobottom").attr("cardId","");
	$("#userGridListCommnity").height(window.innerHeight-150).perfectScrollbar();
	//加载用户社区
//	userCommunity.addAllcommunitylist(0);
	//发帖
	$('#communitysubmit').unbind('click').click(function(){
		//获取Fckeditor输入的文本值
	//	var editor = FCKeditorAPI.GetInstance("myTextarea");
	//	var userinfo = editor.GetXHTML(true);
	//	var content = jQuery(editor.EditorDocument.body).text();
		var userinfo = CKEDITOR.instances.myTextarea.getData();
		/*userinfo = userinfo.replace("<br />","");
		userinfo = userinfo.replace("<br>","");*/
		userinfo = trim(userinfo);
		var title = $('#commnitytitleids').val();
		var pubtype = $('#community_type_select').val();
		var realtitle = title;
		if(trim(title) == "" || title == "输入标题"){
			showBottomMsg('亲,请输入标题哦!',3);
			return false;
		}
		if(trim(title).replace("/[^/x00-/xFF]/g",'**').length>=71){
			showBottomMsg('亲,标题最大可输入70个字符!',3);
			return false;
		}
		if(userinfo ==''){
			showBottomMsg('亲,请输入内容哦!',3);
			return false;
		}
		 if(userinfo.length > 2000){
			showBottomMsg('亲,发帖内容太长最大长度为2000字!',3);
			return false;
		} 

		//用户发帖
		 var cardId="";
		 if($(".abtntobottom").attr("cardId") != undefined){
			 cardId=$(".abtntobottom").attr("cardId");
		 }
		$.ajax({
	        url : $.appClient.generateUrl({ESUserCommunity : 'publishCommunityCard'},'x'),
			type:'POST',
		    data: {'username':window.userName,'usertitle':title,'userinfo':userinfo,'realtitle':realtitle,'thtype':pubtype,'cardId':cardId},
	        datatype:"json",
		    success:function(datas){
		    	 var ajaxobj=eval("("+datas+")");  
		    	if(ajaxobj['alllist'] == "OK"){
					showBottomMsg('发布成功',1);
					CKEDITOR.instances.myTextarea.setData('');
				}else{
					showBottomMsg('发布失败',2);
				}
				$('#commnitytitleids').val('');
				//显示模块下发布的内容
				$("#"+pubtype).trigger("click");	
		    },
		    cache:false
		});
	});
	
})(jQuery); 

//获取用户评论的最新信息
function getCallBackNewMessage(flag,page){
	$("#typetoshowCommunity").attr("typeflg","新回复");
	userCommunity.getCallBackNewMessage(flag,page);
	$('#main-community-list').show();
}
//更1条新消息状态
function js_dismiss_message(cardId,plId,page){
	var url = window.onlinefilePath+'/rest/wechat/updateMessageState?callback=?';
	var data = {'cardId':cardId,'plId':plId,'userName':window.userName};
	var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
	jQuery.getJSON(url, ret.data,
			function(json) {
		$('#myCommunity_callBackInfo').html(template('newCallBackInfo',{'count':json.count}));
		userCommunity.getCallBackNewMessage(null,page);
	});
}

//把新消息全部标记为已读标记
/*
function js_dismiss_all_message(page){
		jQuery.getJSON(window.onlinefilePath+'/rest/wechat/updateMessageAllState?callback=?',{'userid':window.userId,'userName':window.userName},
				function(json) {
			$('#myCommunity_callBackInfo').html(template('newCallBackInfo',{'count':json.count}));
			userCommunity.getCallBackNewMessage(null,page);
			$('#allcard').trigger('click');
		});
		//userCommunity.getCountReplyInfo();
	
}
*/

/**liuwei删除用户帖子**/
function deleteCommunityArtical(cardId,pubtype){
	userCommunity.deleteCard(cardId,pubtype);
}

/**帖子详情页面点击返回按钮*/
function backToIndex(){
	var idflg   = $("#typetoshowCommunity").attr("idflg");
	var pageflg = $("#typetoshowCommunity").attr("pageflg");
	var typeflg = $("#typetoshowCommunity").attr("typeflg");
	if(idflg == undefined){
		idflg='';
	}
	if(pageflg == undefined){
		pageflg=0;
	}
	if(typeflg == undefined){
		typeflg='所有帖子';
	}
	gettypetolistforcommunity(idflg,typeflg,pageflg)
}




