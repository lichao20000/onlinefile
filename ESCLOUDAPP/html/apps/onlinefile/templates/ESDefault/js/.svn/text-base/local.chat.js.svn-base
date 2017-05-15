/***
 * jquery local chat
 **/
;(function ($) {

	if (/1\.(0|1|2)\.(0|1|2)/.test($.fn.jquery) || /^1.1/.test($.fn.jquery)) {
		alert('WebIM requires jQuery v1.2.3 or later!  You are using v' + $.fn.jquery);
		return;
	}
	
	var faceTimed, count = 0;
	
	var fyBotSets = {
		S0: "首先，您的公司电话是什么？如：010-51655505。",
		S1: "您的手机号是什么？如：18551655505。",
		S2: "您的传真地址是什么？如：010-58957560。",
		S3: "您在公司的职位是什么？如：董事长、总裁、部门经理、项目经理、软件工程师等。",
		S4: "谢谢您的配合，已经将输入的相关信息完善到您的个人信息中，如需再次完善，请点击系统右上角的向下箭头，在展现的菜单中点击\"账户设置\"菜单进行完善。"
	};
	
	var fyBotKeyword = "" ;
	
	var defaultPortrait = "apps/onlinefile/templates/ESDefault/images/im/fybot.png" ;
	
	var fyBotSetToField = {
			F0: "telephone",
			F1: "mobilephone",
			F2: "fax",
			F3: "position"
	};
	
	var fyVilidateStr = {
			teleZZ : /^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$/,
			mobtelZZ :/^1[3|4|5|8][0-9]\d{8}$/,
			fexZZ : /^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/,
			positionZZ : /^([\u4e00-\u9fa5]|[a-zA-Z0-9]|[^&\/|\\?.,\'\"><;])+$/ 
	}
		
	var _opts = defaultOptions = {
		version: 1.0,
		chat: "#chat",
		fybot: "fyBot",
		chatEl: function () {
			var $chat = _opts.chat;
			if ((typeof _opts.chat) == "string") {
				$chat = $(_opts.chat);
			} else if ((typeof _opts.chat) == "object") {
				if (!$chat.get(0)) {
					$chat = $($chat);
				}
			} 
			return $chat;
		},
		scrollbarCreate : function(obj) {
			obj.perfectScrollbar();
			obj.scrollLeft(300);
			obj.perfectScrollbar('update');
	    },
		sendMessageDiv: function (receiverId) {
			return $("div[name='sendMessage" + receiverId + "']").get(0);
//			return document.getElementsByName("sendMessage" + receiverId + "")[0];
		},
		receiveMessageDoc: function (receiverId) {
			receiverId = receiverId || "";
			var docs = [];
			$.each($("div[name^='receiveMessage" + receiverId + "']"), function () {
				docs.push($(this));
			});
			return docs;
		},
		sender: "", // 发送者
		receiver: "", // 接收者
		setTitle: function (chatEl) {
			var receiver = this.getReceiver(chatEl);
			receiver = receiver.replace("\\40", "@");
			chatEl.find(".receiverusername").attr("receiver", receiver);
		},
		getReceiver: function (chatEl) {
			var receiver = chatEl.attr("receiver");
			if (~receiver.indexOf("@")) {
				receiver = receiver.split("@")[0];
			}
			return receiver;
		},
		
		datetimeFormat: function (v) {
			if (~~v < 10) {
				return "0" + v;
			}
			return v;
		},
		getDate: function () {
			// 设置当前发送日前
			var date = new Date();
			var datestr = date.getFullYear() + "-" + _opts.datetimeFormat(date.getMonth()*1+1) + "-" + _opts.datetimeFormat(date.getDate());
			return datestr;
		},
		getDatetime: function () {
			// 设置当前发送日前
			var date = new Date();
//			var datetime = date.getFullYear() + "-" + date.getMonth() + "-" + date.getDate();
			var datetime = _opts.datetimeFormat(date.getHours()) 
						+ ":" + _opts.datetimeFormat(date.getMinutes()) 
						+ ":" + _opts.datetimeFormat(date.getSeconds());
			return datetime;
		},
		// longjunhao 20151027 高亮处理
		addHighLight:function (content, keyword) {
			keyword = decodeURI($.trim(keyword), "utf-8");
			var checkStrStart = '<div class="file_title"><a class="file_preview_link link" name="fileName">';
			var checkStrEnd = '</a></div>';
			var index = content.indexOf(checkStrStart);
			// 判断是否是文件框
			if (index > -1) {
				// 获取文件框中的文件标题
				var fileTitle = content.substr(index+checkStrStart.length);
				fileTitle = fileTitle.substr(0, fileTitle.indexOf(checkStrEnd));
				newFileTitle = _opts.replaceNoChangeMatchWords(fileTitle,keyword,true);
				if(newFileTitle==fileTitle) return false;
				content = content.replace(checkStrStart+fileTitle+checkStrEnd, checkStrStart+newFileTitle+checkStrEnd);
			} else {
				var oldContent = content;
				content = _opts.replaceNoChangeMatchWords(content,keyword,true);
				if(content==oldContent) return false;
			}
			return content;
		},
		replaceNoChangeMatchWords : function (replaceBase,replaceWith,ignoreCase){
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
		},
		/***
		 * 发送消息的格式模板					
		 * flag = true 表示当前user是自己，否则就是对方
		 **/ 
		receiveMessageTpl: function (userName, styleTpl, content, flag, date, time, userpic, fromCnName,ID) {
			var reg = /\[[\u4E00-\u9FA5]{1,}\]/gi;
			var arr = content.match(reg);
			var tempArray = [];
			if(arr != null){
				arr.forEach(function(i){
					if($.inArray(i,tempArray)== -1){
						if(faces[i]){
							 var html = '<img height="26" width="26" src="templates/onlinefile/images/expression/f_static_'+faces[i]+'.png">'
							 var regi = /i/gi; 
							 content = content.replace(reg,html);
							 tempArray.push(i);
						}
					   }
				}); 
			}
			var mongoID = ID=="undefined" ? "":ID;
			var userCls = flag ? "im-me" : "im-others";
//			userpic = userpic==null?defaultPortrait:userpic ;
			userpic = $("#itemuser-"+hex_md5(userName.replace("\\40", "@"))).attr("itemuserportrait") ;
			return [
				'<div class="im-item ', userCls, '"><div class="im-message clearfix"><div class="im-user-area"><img width="32" height="32" class="useritempic',(flag?"":" otherusermsg"),'" itemuser="',userName,'" fullname="',fromCnName,'" src="', userpic, '" style="',(userpic==""?"opacity: 0;":""),'"></div><div class="im-message-detail">',
				'<table class="im-message-table" border="0" cellSpacing="0" cellPadding="0">',
				'<tr><td class="lt"></td><td class="tt"></td><td class="rt"></td></tr>',
				'<tr><td class="lm">', (userCls=='im-me'?'':'<span/>'), '</td><td class="mm">',
				'<div class="im-message-title"><p class="im-message-owner"><span class="im-txt-bold">', (fromCnName==null?$("#current_user_name").html():fromCnName), '</span></p><span class="im-send-time">', (time==null?_opts.getDatetime():time), '</span></div>',
				'<div class="im-message-content" msgid="'+mongoID+'" date="'+(date==null?_opts.getDate():date)+' '+(time==null?_opts.getDatetime():time)+'">', content,'</div>',
				'</td><td class="rm">', (userCls=='im-me'?'<span/>':''), '</td></tr>',
				'<tr><td class="lb"></td><td class="bm"></td><td class="rb"></td></tr>',
				'</table>',
				'</div></div></div>'
			].join("");
		},
		// 向接收消息iframe区域写消息
		writeReceiveMessage: function (receiverId, userName, content, flag, date, time, fromCnName) {
			if (content) {
				// 发送消息的样式
				var receiveMessageDoc = _opts.receiveMessageDoc(receiverId);
				$.each(receiveMessageDoc, function () {
					// 向接收信息区域写入发送的数据
					this.append(_opts.receiveMessageTpl(userName, "", content, flag, date, time, null, fromCnName));
					//浏览器自带滚动条滚动条滚到底部方法
//					this.scrollTop($(this)[0].scrollHeight);
					//JQuery插件实现的滚动条滚到底部方法
					$("#receiveMessageDiv").scrollTop($(".im-chat-list").height());
					$("#receiveMessageDiv").perfectScrollbar('update');
				});
			}
		},
		// 发送消息
		sendHandler: function ($chatMain) {
			var doc = $("#flyingchatinput");
			var content = doc.val();
			content = $.trim(content);
			if(content.length>500){
				showBottomMsg("消息内容过长，发送的最大长度为500.", 2);
				return false ;
			}
			content = content.replace(new RegExp("<br>", "gm"), "");
			
			// 查找#文件#
//			content = _opts.findAtFileInContent(content);
			
			// 获取即将发送的内容
			if (content) {
				var sender = $chatMain.attr("sender");
				var receiverId = $chatMain.attr("id");
				var receiver = $chatMain.find("#to").val();
				if (!receiver) {
					// 接收区域写消息
					_opts.writeReceiveMessage(receiverId, "系统", "您还没有选择任何的会话，请先在\"联系人/群组\"中选择会话对象后，再进行此操作！", false);
					return ;
				} else {
					/**
					if(content.indexOf("fyBot -s")>-1){
						_opts.fyBotSearch(doc, content);
						return ;
					} **/
					if($('#receiverusername').attr("receiver") == 'fyBot'){
						var msg = _opts.validateFyBotData(content) ;
						if(msg == ""){
							// 接收区域写消息
							_opts.writeReceiveMessage(receiverId, sender, content, true);
							_opts.fyBotMessageHandler(content, "") ;
							doc.val("");
						} else {
							showBottomMsg(msg, 2);
						}
						return;
					}
					// 接收区域写消息
					_opts.writeReceiveMessage(receiverId, sender, content, true);
				}
				var fileFlag = "";
				var sendcontent = content;
				if($('#receiverusername').attr("isGroup") == "1"){
					// 获取正在浏览的文件id
					try{fileFlag = documentCenter.getCurrentFileFlag();}catch(e){}
					var msg = [$('#receiverusername').attr("groupName"), "broadcast-", $('#receiverusername').attr("groupFlag"), "<",sendcontent ].join("");
					remote.jsjac.chat.sendMessage(msg, receiver);
				} else {
					remote.jsjac.chat.sendMessage(sendcontent, receiver);
				}
				if(_opts.groupToUsers.length>0){
					var users = _opts.groupToUsers.join(",");
					jQuery.getJSON($("#flyingchat").attr("baseurl")+'/rest/chat/saveGroupcallOver?callback=?', {'companyId':$("#flyingchat").attr("companyId"), 'groupflag':$('#receiverusername').attr("groupFlag"), "users":users,'username':window.userName},
							function(json) {
					});
				}
				_opts.saveHistoryMsg($('#flyingchat').attr("username"), $('#receiverusername').attr("receiver"), content, _opts.getDate(), _opts.getDatetime(), $('#receiverusername').attr("isGroup"), "", fileFlag) ;
				// 清空发送区域
				doc.val("");
				_opts.groupToUsers=[];
				_opts.groupToUsersName=[];
			}
		}, 
		initWebIM: function (userJID, receiver) {
			if(receiver == ""){
				_opts.bindEventForUserList();
				_opts.bindEventForGroupUsersList();
				_opts.bindEventForGroupList();
				_opts.toOtherUser();
				
				$("#flyingchatsend").click(function () {
					var $chatMain = $(this).parents(".chat-main");
					_opts.sendHandler($chatMain);
				});
				$('.im-chat-list').on("click", ".msgMore", function(){
					var page = $("#msgMoreButton").attr("page") ;
					var keyWord = $("#msgMoreButton").attr("keyWord") ;
					page = page*1+1 ;
					if(page == 1){
						var skip = $("#msgMoreButton").attr("skip") ;
						_opts.getHistoryMessage($('#receiverusername').attr("receiver"), $('#receiverusername').attr("isGroup"), page, skip,keyWord) ;
					} else {
						_opts.getHistoryMessage($('#receiverusername').attr("receiver"), $('#receiverusername').attr("isGroup"), page, 0,keyWord) ;
					}
					$("#msgMoreButton").attr("page", page) ;
				});
				$('.im-chat-list').on("click", ".returnChat", function(){
					$(".im-chat-list").html('<div id="msgMoreButton" page="0"><div class="msgMore">更多历史消息...</div></div>');
					if($('#receiverusername').attr("receiver") == 'fyBot'){
						_opts.fyBotInitHtml() ;
					}
					_opts.getHistoryMessage($('#receiverusername').attr("receiver"), $('#receiverusername').attr("isGroup"), 1, 0) ;
				});
				
				//获取分类下的用户列表
				$("#groupUsersList").click(function(){
					
					if($(".group-usersdiv").attr("isShow") == "false"){
//						var x = $(this).offset().left - 300;
//						var y = $(this).offset().top + 20;
						var x = 50;
						var y = 46;
						$(".group-usersdiv").css({"display": "block", "left":x, "top":y});
						$('#groupuserListId').perfectScrollbar('update');
						var tempHeight = $(".group-usersdiv").find(".groupuserList").height();
						tempHeight = tempHeight>300?"300px":(tempHeight+"px") ;
						$(".group-usersdiv").animate({height:tempHeight},'slow',function(){
							$(".group-usersdiv").attr("isShow","true");
						}) ;
						$(".group-usersdiv").attr("isShow","runing");
					} else {
						$(".group-usersdiv").animate({height:'0px'},'slow',function(){
							$(".group-usersdiv").css({"display": "none"});
						}) ;
						$(".group-usersdiv").attr("isShow","false");
					}
				});
				
				$("#groupusersspanId").click(function () {
					if($(".group-usersdiv").attr("isShow") == "false"){
//						var x = $(this).offset().left - 300;
//						var y = $(this).offset().top + 20;
						var x = 50;
						var y = 46;
						$(".group-usersdiv").css({"display": "block", "left":x, "top":y});
						$('#groupuserListId').perfectScrollbar('update');
						var tempHeight = $(".group-usersdiv").find(".groupuserList").height();
						tempHeight = tempHeight>300?"300px":(tempHeight+"px") ;
						$(".group-usersdiv").animate({height:tempHeight},'slow',function(){
							$(".group-usersdiv").attr("isShow","true");
						}) ;
						$(".group-usersdiv").attr("isShow","runing");
					} else {
						$(".group-usersdiv").animate({height:'0px'},'slow',function(){
							$(".group-usersdiv").css({"display": "none"});
						}) ;
						$(".group-usersdiv").attr("isShow","false");
					}
				});
				$('#receiverusername').click(function () {
					if($("#receiverusername").attr("isgroup")=="0"){
						return;
					}
					if($(".group-usersdiv").attr("isShow") == "false"){
//						var x = $(this).offset().left - 300;
//						var y = $(this).offset().top + 20;
						var x = 50;
						var y = 46;
						$(".group-usersdiv").css({"display": "block", "left":x, "top":y});
						$('#groupuserListId').perfectScrollbar('update');
						var tempHeight = $(".group-usersdiv").find(".groupuserList").height();
						tempHeight = tempHeight>300?"300px":(tempHeight+"px") ;
						$(".group-usersdiv").animate({height:tempHeight},'slow',function(){
							$(".group-usersdiv").attr("isShow","true");
						}) ;
						$(".group-usersdiv").attr("isShow","runing");
					} else {
						$(".group-usersdiv").animate({height:'0px'},'slow',function(){
							$(".group-usersdiv").css({"display": "none"});
						}) ;
						$(".group-usersdiv").attr("isShow","false");
					}
				});
				$(".chatusersdiv").click(function () {
					var status = $(".chat-usersdiv").attr("isShow")  ;
					if(status == "false"){
						$(".chat-usersdiv").attr("isShow","showing");
						$(".chat-usersdiv").css({"display": "block"});
						$(".chat-usersdiv").animate({height:'400px'},'slow',function(){
							$(".chat-usersdiv").attr("isShow","true");
						}) ;
					} else if(status == "true"){
						$(".chat-usersdiv").attr("isShow","hideing");
						$(".chat-usersdiv").animate({height:'0px'},'slow',function(){
							$(".chat-usersdiv").css({"display": "none"});
							$(".chat-usersdiv").attr("isShow","false");
						}) ;
					}
				});
				
				$(".navbar").click(function () {
					_opts.hidechatdownmenu();
				});
				$(".main-left").click(function () {
					_opts.hidechatdownmenu();
				});
				$("#bodyContent_Title").click(function () {
					_opts.hidechatdownmenu();
				});
				$("#content-heading").click(function () {
					_opts.hidechatdownmenu();
				});
				$("#content-list").click(function () {
					_opts.hidechatdownmenu();
				});
				$("#flyingchat").click(function () {
					_opts.hidechatdownmenu();
				});
				
				/**好友头像点击**/
				$("#bodyContent_Title").on('mouseover','#hailFellow',function(){
					$("#hailFellow").find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/users.png") no-repeat left center');
				}).on('mouseout','#hailFellow',function () {
						$("#hailFellow").find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/users-o.png") no-repeat left center');
				}).on('click','#hailFellow',function(){
					$("#hailFellow").find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/users.png") no-repeat left center');
					$(".chat-usersdiv").attr("isShow","showing");
					$(".chat-usersdiv").css({"display": "block"});
					$(".chat-usersdiv").animate({height:'400px'},'fast',function(){
						$(".chat-usersdiv").attr("isShow","true");
					}) ;
					$("#flyingchatuserlist").css("display", "block") ;
					/*if($('#flyingchatuserlist').attr("view") == "0"){
						$('#flyingchatuserlist').perfectScrollbar();
						$('#flyingchatuserlist').perfectScrollbar('update');
						$('#flyingchatuserlist').attr("view", "1");
					}*/
					$("#flyingchatgrouplist").css("display", "none") ;
					$(".selecticon").css("left", "40px");
				});
				

				/**群聊点击**/
				$("#bodyContent_Title").on('mouseover','#grouPchat',function(){
					$("#grouPchat").find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/groups-o.png") no-repeat left center');
				}).on('mouseout','#grouPchat',function () {
						$("#grouPchat").find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/groups-n.png") no-repeat left center');
				}).on('click','#grouPchat',function(){
						$(".chat-usersdiv").attr("isShow","showing");
						$(".chat-usersdiv").css({"display": "block"});
						$(".chat-usersdiv").animate({height:'400px'},'fast',function(){
							$(".chat-usersdiv").attr("isShow","true");
						}) ;
					$("#flyingchatgrouplist").css("display", "block") ;
					if($('#flyingchatgrouplist').attr("view") == "0"){
						$('#flyingchatgrouplist').perfectScrollbar();
						$('#flyingchatgrouplist').perfectScrollbar('update');
						$('#flyingchatgrouplist').attr("view", "1");
					}
					$("#flyingchatuserlist").css("display", "none") ;
					$(".selecticon").css("left", "130px");
					$("#grouPchat").find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/groups.png") no-repeat left center');
				});
				
				/**点击添加好友按钮**/
				$('#bodyContent_Title').on('click','#addFriends',function(){
					var adduseremailHtml= template("addUserEmail_template");
					var classId = $('.main-left .active').attr('data-class-id');
					var groupid = $('.main-left .active').attr('data-group-id');
					var height = document.documentElement.clientHeight-100;
					$.fywindow({
				    	title: '分享到',
						width: 600,
						height:height,
					    content: adduseremailHtml
				    });
					
					$.ajax({
						url:$.appClient.generateUrl({ESUserInfo : 'openUrl'},'x'),
						type:'POST',
						data:{classId:classId,groupid:groupid},
						datatype:"json",
						success:function(data){
							var success = $.parseJSON(data)
							if(success.success){
								$("#adduseremailBody").find("#fe_text").val(success.url);
							}else{
								showBottomMsg('出错了',3);
							}
						}
					});
					
					//动态添加行
					dynamicAddRow();
					copyUrl();
					var w = $("#adduseremail-list").parent().parent().width();
					var h = $("#adduseremail-list").parent().parent().height()*0.9;
					
					$("#adduseremail-list").css({width:w,height:h});
					//显示滚动条
					$("#adduseremail-list").perfectScrollbar();
					$("#adduseremail-list").perfectScrollbar("update");
					return false;
				});
				//老版本
				/*$("#bodyContent_Title").on("click","#contactgroup",function () {
					var status = $(".chat-usersdiv").attr("isShow");
					if(status == "false"){
						$(".chat-usersdiv").attr("isShow","showing");
						$(".chat-usersdiv").css({"display": "block"});
						$(".chat-usersdiv").animate({height:'400px'},'fast',function(){
							$(".chat-usersdiv").attr("isShow","true");
						}) ;
					} else if(status == "true"){
						$(".chat-usersdiv").attr("isShow","hideing");
						$(".chat-usersdiv").animate({height:'0px'},'fast',function(){
							$(".chat-usersdiv").css({"display": "none"});
							$(".chat-usersdiv").attr("isShow","false");
						}) ;
					}
				});*/
				
				/*$(".items-users").mouseover(function () {
					if($(".chat-usersdiv").attr("activityitem") == "groups"){
						$(this).find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/users.png") no-repeat left center');
					}
				}).mouseout(function () {
					if($(".chat-usersdiv").attr("activityitem") == "groups"){
						$(this).find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/users-o.png") no-repeat left center');
					}
				}).click(function () {
					if($(".chat-usersdiv").attr("activityitem") == "groups"){
						$(".chat-usersdiv").attr("activityitem", "users") ;
						$("#flyingchatuserlist").css("display", "block") ;
						$("#flyingchatgrouplist").css("display", "none") ;
						$(".selecticon").css("left", "40px");
						$(this).find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/users.png") no-repeat left center');
						$(".items-groups").find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/groups-n.png") no-repeat left center');
					}
				});*/
				
				/*$(".items-groups").mouseover(function () {
					if($(".chat-usersdiv").attr("activityitem") == "users"){
						$(this).find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/groups-o.png") no-repeat left center');
					}
				}).mouseout(function () {
					if($(".chat-usersdiv").attr("activityitem") == "users"){
						$(this).find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/groups-n.png") no-repeat left center');
					}
				}).click(function () {
					if($(".chat-usersdiv").attr("activityitem") == "users"){
						$(".chat-usersdiv").attr("activityitem", "groups") ;
						$("#flyingchatgrouplist").css("display", "block") ;
						if($('#flyingchatgrouplist').attr("view") == "0"){
							$('#flyingchatgrouplist').perfectScrollbar();
							$('#flyingchatgrouplist').perfectScrollbar('update');
							$('#flyingchatgrouplist').attr("view", "1");
						}
						$("#flyingchatuserlist").css("display", "none") ;
						$(".selecticon").css("left", "130px");
						$(this).find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/groups.png") no-repeat left center');
						$(".items-users").find("span").css("background", 'url("apps/onlinefile/templates/ESDefault/images/im/users-o.png") no-repeat left center');
					}
				});*/
			/*	$('.chat-usersdiv').on('click','#add_user_for_email',function(){
					
					// 设置头部
					//var jsonHeading = '{"type":"'+$(this).attr('id')+'"}';
					//$("#adduseremail-heading").html(template('requestUser_templete'));
					//$("#adduseremailBody").html(template("requestUserhtml_templete"));
					var adduseremailHtml= template("addUserEmail_template");
					var classId = $('.main-left .active').attr('data-class-id');
					var groupid = $('.main-left .active').attr('data-group-id');
					var height = document.documentElement.clientHeight-100;
					$.fywindow({
				    	title: '分享到',
						width: 600,
						height:height,
					    content: adduseremailHtml
				    });
					
					$.ajax({
						url:$.appClient.generateUrl({ESUserInfo : 'openUrl'},'x'),
						type:'POST',
						data:{classId:classId,groupid:groupid},
						datatype:"json",
						success:function(data){
							var success = $.parseJSON(data)
							if(success.success){
								$("#adduseremailBody").find("#fe_text").val(success.url);
							}else{
								showBottomMsg('出错了',3);
							}
						}
					});
					
					//动态添加行
					dynamicAddRow();
					copyUrl();
					var w = $("#adduseremail-list").parent().parent().width();
					var h = $("#adduseremail-list").parent().parent().height()*0.9;
					
					$("#adduseremail-list").css({width:w,height:h});
					//显示滚动条
					$("#adduseremail-list").perfectScrollbar();
					$("#adduseremail-list").perfectScrollbar("update");
					return false;
				});*/
				
				// 首先取消事件绑定，当一次性发多条消息的情况下会同时绑定多个相同事件
				$(".have-msg, .no-msg, .chat-main").unbind("click");
				$(".have-msg").bind("click", function () {
					$(this).hide();
					$(".no-msg").show();
					$(".chat-main:hidden").show(150);
				});
				
				$(".no-msg").click(function () {
					$(".chat-main:hidden").each(function (i, item) {
						var top = i * 10 + 50;
						var left = i * 20 + 50;
						$(this).show(500).css({top: top, left: left});
					});
				});
				
				/** lujixiang 20150728 注释此段代码,将删除@用户事件放到keydown事件中处理  --start**/
				/**  删除@用户时候一次性全删除！   **/
				/**
				$('#flyingchatinput').bind('input propertychange', function() { 
					var inputVal = $(this).val();
					if(inputVal.indexOf("@")>-1){
						var inputNewVal = inputVal.substring(1,inputVal.length);
						var indexSpace = inputNewVal.lastIndexOf(" ");
						var lastSignIndex = inputNewVal.lastIndexOf("@");
						if(lastSignIndex>0){
							lastSignIndex = lastSignIndex-1;
						}
						var finalValue = ""+inputNewVal.substring(0,lastSignIndex);
						$(this).val(finalValue);
					}
					
					//if(inputVal.substring(0,inputVal.))
				}); 
				**/
				/** lujixiang 20150728 注释此段代码,将删除@用户事件放到keydown事件中处理  --end**/
				 var preKeyCode = 0;
				$('#flyingchatinput').keyup(function (event) {
					var e = event || window.event;
					var keyCode = e.which || e.keyCode;
					if(($('#showGroupUsers').css("display") == "block") && (keyCode==38 || keyCode==40 || keyCode==13)){ //方向上键  方向下键 Enter键
						var num = $('#showGroupUsers li').length;
						var i=0;  //当前选中
						for(i=0;i<num;i++){
							var item = $('#showGroupUsers li:eq('+i+')');
							if(item.hasClass('showGroupUsers4Arrow'))
								break;
						}
						if(keyCode==38){ //方向键上
							if(i==num){//遍历结束  没有class
								$('#showGroupUsers li:last').addClass('showGroupUsers4Arrow');
							}else{
								$('#showGroupUsers li:eq('+i+')').removeClass('showGroupUsers4Arrow');
								if(i==0){
									$('#showGroupUsers li:last').addClass('showGroupUsers4Arrow');
								}else{
									$('#showGroupUsers li:eq('+(i-1)+')').addClass('showGroupUsers4Arrow');
								}
							}
							// 控制滚动条的位置
							if(i==num || i==0){
								$("#showGroupuserListId").scrollTop(40*num);
							}else{
								if(num-i>4){
									var currentScrollTop = $("#showGroupuserListId").scrollTop();
									if(currentScrollTop>40*(i-1)-10){
										$("#showGroupuserListId").scrollTop(40*(i-1)-10); //-10是为了美观
									}
								}
							}
							$("#showGroupuserListId").perfectScrollbar('update');
						}else if(keyCode==40){  //方向键下
							if(i==num){//遍历结束  没有class
								$('#showGroupUsers li:first').addClass('showGroupUsers4Arrow');
							}else{
								$('#showGroupUsers li:eq('+i+')').removeClass('showGroupUsers4Arrow');
								if(i==(num-1)){
									$('#showGroupUsers li:first').addClass('showGroupUsers4Arrow');
								}else{
									$('#showGroupUsers li:eq('+(i+1)+')').addClass('showGroupUsers4Arrow');
								}
							}
							// 控制滚动条的位置
							if(i>=num-1){
								$("#showGroupuserListId").scrollTop(0);
							}else{
								if(i>4){
									var currentScrollTop = $("#showGroupuserListId").scrollTop();
									if(currentScrollTop<40*(i-4)){
										$("#showGroupuserListId").scrollTop(40*(i-4));
									}
								}
							}
							$("#showGroupuserListId").perfectScrollbar('update');
						}else{
							if(i!=num){
								var current = $('#showGroupUsers li:eq('+i+')');
								var itemuser = current.attr('itemuser') ;
								var fullname = current.attr('fullname');
								if(!_opts.contains(_opts.groupToUsers, itemuser)){
									_opts.groupToUsers.push(itemuser) ;
								}
								_opts.groupToUsersName.push(fullname);
								$("#flyingchatinput").focus();
								var val = $("#flyingchatinput").val();
								var str = val.substring(0,val.lastIndexOf("@")+1);
								$("#flyingchatinput").val(str+fullname+" ");
								$('#showGroupUsers').css("display", "none");
								$("#showGroupUsers #showGroupuserListsId").find("li").each(function(){
										 $(this).css("display","block");
									})
							}
						}
						return false;
					}else{
						if($('#showGroupUsers li.showGroupUsers4Arrow').length>0){
							$('#showGroupUsers li.showGroupUsers4Arrow').removeClass('showGroupUsers4Arrow');
						}
						if($('#showGroupUsers').css("display") == "block"){
							var val = $("#flyingchatinput").val();
							var str = val.substring(val.lastIndexOf("@")+1);
							//alert(str);
							$("#showGroupUsers #showGroupuserListsId").find("li").each(function(){
							var fullname = $(this).attr("fullname");
							//alert(fullname);
							if(!fullname || fullname.indexOf(str)==-1){
								 $(this).css("display","none");
							}else{
								 $(this).css("display","block");
							}
							})
						}else{
							$("#showGroupUsers").css("display", "none");
						}
						
					}
					if (keyCode == 13) {
						var $chatMain = $("#"+$(this).attr("jid"));
						_opts.sendHandler($chatMain);
						return false;
					} else if((e.shiftKey && keyCode==50)||(preKeyCode==16 && keyCode==50)){//@的时候
						preKeyCode = keyCode;
						if($("#receiverusername").attr("isgroup")=="1"){
							if($("#showGroupUsers").attr("count")*1>0){
								var tempinputchat = $(this) ;
								tempinputchat.val(tempinputchat.val());
								_opts.showGroupUsers();
								return false ;
							}
						}
					} else if(keyCode == 8){	
						var tempchatinput = $("#flyingchatinput") ;
						var content = tempchatinput.val();
						var lastSignIndex = content.lastIndexOf("@");
						if(lastSignIndex>-1){
							var tempUserName = content.substring(lastSignIndex+1, content.length);
							if(_opts.contains(_opts.groupToUsersName, tempUserName)){
								tempchatinput.val(content.substring(0, lastSignIndex));
								return false;
							}
						}
					}
					preKeyCode = keyCode;
					/**
					else if(e.altKey && (keyCode == 83 || keyCode == 115)){
						var content = $("#flyingchatinput").val();
						content = $.trim(content);
						content = content.replace(new RegExp("<br>", "gm"), "");
						if(content == ""){
							$("#flyingchatinput").val("fyBot -s ");
						} else if(content == "fyBot -s"){
							$("#flyingchatinput").val("fyBot -s ");
						} else {
							$("#flyingchatinput").val("fyBot -s " + content);
						}
						return false;
					} **/
				});
				
			
			   
				/*xiewena  移到上边统一处理
				 * //解决中文输入法不能获取@监听问题
				$('#flyingchatinput').keyup(function (event) {
					var e = event || window.event;
					var keyCode = e.which || e.keyCode;
					if((e.shiftKey && keyCode==50) || (preKeyCode==16 && keyCode==50)){ 
						preKeyCode = keyCode;
						var value = $("#flyingchatinput").val();
						if($("#receiverusername").attr("isgroup")=="1"){
							if($("#showGroupUsers").attr("count")*1>0){
								$("#flyingchatinput").val(value);
								_opts.showGroupUsers();
								return false ;
							}
						}
					}
					
					preKeyCode = keyCode;
				});*/
				/**
				$('#flyingchatinput').keyup(function (event) {
					if($("#receiverusername").attr("isgroup")=="1"){
						if($("#showGroupUsers").attr("count")*1>0){
							var value = $("#flyingchatinput").val();
							if (value.endWith('@')){
								$("#flyingchatinput").val(value.substring(0, value.length-1));
								_opts.showGroupUsers();
								return false ;
							}
						}
					}
				});
				**/
				
			} else {
				var chatEl = $(".chat-main") ;
				chatEl.attr("id", userJID);
				chatEl.attr("sender", _opts.sender);
				chatEl.attr("receiver", receiver);
				chatEl.find(".im-chat-list").attr("name", "receiveMessage" + userJID);
				chatEl.find(".chatSendMessage").attr("name", "sendMessage" + userJID);
				chatEl.find("#flyingchatinput").attr("jid", userJID);	
				_opts.setTitle(chatEl);
			}
			
		},
		hidechatdownmenu : function(){
			if($(".chat-usersdiv").attr("isShow") == "true"){
				$(".chat-usersdiv").animate({height:'0px'},'fast',function(){
					$(".chat-usersdiv").css({"display": "none"});
				});
				$(".chat-usersdiv").attr("isShow","false");
			}
			if($(".group-usersdiv").attr("isShow") == "true"){
				$(".group-usersdiv").animate({height:'0px'},'fast',function(){
					$(".group-usersdiv").css({"display": "none"});
				}) ;
				$(".group-usersdiv").attr("isShow","false");
			}
			$("#showGroupUsers").css("display", "none");
		},
		toOtherUser: function(){
			$(".im-chat-list").on("click", ".otherusermsg", function(){
				if($("#receiverusername").attr("isgroup")=="1"){
					var itemuser = $(this).attr('itemuser') ;
					if(!_opts.contains(_opts.groupToUsers, itemuser)){
						_opts.groupToUsers.push(itemuser) ;
					}
						var fullname = $(this).attr('fullname');
						$("#flyingchatinput").val($("#flyingchatinput").val()+" @" + fullname+" ");
						_opts.groupToUsersName.push(fullname);
					/**}else{
						
						var fullname = $(this).attr('fullname');
						var atname = "@" + fullname ;
						var tempchatinput = $("#flyingchatinput");
						var tempchatinputVal = tempchatinput.val();
						if(tempchatinputVal.indexOf(atname) == -1){
							_opts.groupToUsersName.push(fullname);
							tempchatinput.val(tempchatinputVal +  fullname + " ");
						}
						
					}**/
					$("#flyingchatinput").focus();
				}
			}) ;
			$(".im-chat-list").on("mouseover", ".otherusermsg", function(){
				if($("#receiverusername").attr("isgroup")=="1"){
					$(this).css('cursor', 'pointer') ;
					$(this).attr('title', '@我') ;
				}
			}) ;
			$(".im-chat-list").on("mouseout", ".otherusermsg", function(){
				if($("#receiverusername").attr("isgroup")=="1"){
					$(this).css('cursor', 'default') ;
				}
			}) ;
		},
		
		showGroupUsers: function(){
			$("#showGroupUsers").css("display", "block") ;
			$('#showGroupuserListId').perfectScrollbar('update');
		},
		
		// 建立新聊天窗口
		newWebIM: function (settings) {
			var chatUser = remote.userAddress(settings.receiver);
			var userJID = "u" + hex_md5(chatUser);
			_opts.initWebIM(userJID, chatUser);
			
			$("#" + userJID).find(remote.receiver).val(chatUser);
			$("#" + userJID).show(220);
		},
		/** 组装联系人列表 **/
		reloadUserList: function (isGroup) {
			if(window.companyid == '-1'){
				return false;
			}
			var userListStr = "" ;
			var data = {'companyid':window.companyid,'username':window.userName,'userid':window.userId,fullname:encodeURI($("#current_user_name").html(), "utf-8"),'groupflag':'', 'groupname':''} ;
			var groupitemid =[];
			if(isGroup == 1){
				var nowGroup = $('.main-left li.active') ;
				data.groupflag = nowGroup.attr('flag');
				data.groupname = encodeURI(nowGroup.attr("groupname"), "utf-8");
				data.classId =  nowGroup.attr('data-class-id');
				var groupitemdiv = $("#flyingchatusers").find("div[class='groupitem']");
				//获取当前的群组展开还是关闭的状态
				groupitemdiv.each(function(i){
					var status = $(this).attr("childdivstatus");
					if(status=="open"){
					     groupitemid.push("1");
					}else{
						 groupitemid.push("0");
					}
				});
			}
			var url = window.onlinefilePath+'/rest/chat/getCompanyUsers?callback=?';
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
						//将当前页面群组的展开效果加入到json进行刷新群组页面的状态
						json.groupitemid=groupitemid;
				        var htmlNoJoin = "";
				        var url = window.onlinefilePath+'/rest/chat/getCompanyUsersForGroupSetAndNotJoin/'+data.companyid+'/'+data.username+'?callback=?';
						var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
						jQuery.getJSON(url, ret.data,
								function(json1) {
				        	htmlNoJoin = template('chatusersNojoin_templete',json1);
						$('#flyingchatusers').html(template('chatusers_templete', json)+htmlNoJoin+_opts.buildFyBot());
						_opts.scrollbarCreate($('#flyingchatuserlist'));
						
						if(isGroup == 0){
							_opts.gotoUser('fyBot');
						} else {
							_opts.reloadGroupUsersForClass(json) ;
						}
						var obj = $("#items-users-messcount") ;
						var countObj = $("#items-users-messcount-val") ;
						var usersMessageCounter = json.allMessageCount*1;
						
						
						countObj.val(usersMessageCounter);
						if(usersMessageCounter < 1){
							obj.html(0);
							obj.hide();
						}else {
							usersMessageCounter = (usersMessageCounter < 100 ? usersMessageCounter + '' : '99+');
							obj.html(usersMessageCounter);
							obj.show();
						}
						obj.css({width:(6+(usersMessageCounter+"").length*6)}) ;
						
						
						
						/**   lujixiang 20151124 刷新"群聊"信息数  **/
						var agc = 0 ;
						$(".groupList").find(".newmessage").each(function(){
							var newmessagecount = $(this).attr("newmessagecount");
							if(undefined == newmessagecount || '' == newmessagecount){
								newmessagecount = 0 ;
							}
							agc += newmessagecount * 1;
							// gc += $(this).html()*1;
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
						
						/*
						obj = $("#userallnewmessagecount") ;
						if((usersMessageCounter +$("#items-groups-messcount").html()*1)>98){
							obj.html("99+") ;
						}else{
							obj.html($("#items-users-messcount").html()*1+$("#items-groups-messcount").html()*1) ;
						}
						
						if(obj.html()*1>0 || obj.html() == "99+"){
							obj.css({display:'block',width:(6+(obj.html()+"").length*6)}) ;
						} else {
							if($("#items-groups-messcount").html()==""){
								obj.css({display:'block',width:'12px'}) ;
								obj.html("") ;
							} else {
								obj.css({display:'none'}) ;
							}
							obj.hide();
						}*/
						//reloadCompany();
						//给群组里的用户添加右击事件
						initChatUserMenu();
				    });
				           
			  });
		},
		bindEventForUserList: function () {
			$('#flyingchatusers').on("click", ".groupitem", function(){
				childdivid = $(this).attr("childdivid");
				if($(this).attr("childdivstatus") == "close"){
					$("#"+childdivid).css("display", "block");
					$('#flyingchatuserlist').perfectScrollbar('update');
					$(this).attr("childdivstatus", "open") ;
					$(this).find("span").css("background", "url('apps/onlinefile/templates/ESDefault/images/im/item1.png') no-repeat left center");
				} else {
					$("#"+childdivid).css("display", "none");
					$('#flyingchatuserlist').perfectScrollbar('update');
					$(this).attr("childdivstatus", "close") ;
					$(this).find("span").css("background", "url('apps/onlinefile/templates/ESDefault/images/im/item0.png') no-repeat left center");
				}
			});
			$('#flyingchatusers').on("mouseover", ".groupitem", function(){
				if($(this).attr("childdivstatus")=="close"){
					$(this).find("span").css("background", "url('apps/onlinefile/templates/ESDefault/images/im/item0.png') no-repeat left center");
				}
			});
			$('#flyingchatusers').on("mouseout", ".groupitem", function(){
				if($(this).attr("childdivstatus")=="close"){
					$(this).find("span").css("background", "url('apps/onlinefile/templates/ESDefault/images/im/item2.png') no-repeat left center");
				}
			});
			$('#flyingchatusers').on("click", "li", function(){
				$("#groupusersspanId").css("display", "none") ;
				var realreceiver = $(this).attr('itemuser');
				var receiverusername = $('#receiverusername') ;
				if(receiverusername.attr("receiver") == realreceiver){
					return;
				}
				if($("#flyingchat").attr("username") == realreceiver){
					return;
				}
				var hexname = hex_md5(realreceiver);
				var receiver = realreceiver.replace("@", "\\40");
				receiverusername.attr("isGroup", "0");
				$.WebIM.newWebIM({
					receiver: receiver
				});
				receiverusername.css({"width":"auto"});
				receiverusername.attr("title",$(this).attr("itemuserfullname"));
				receiverusername.attr("groupName",$(this).attr('itemuserfullname'));
				receiverusername.html($(this).attr('itemuserfullname'));
				var maxWidth = ($("#mainContentRight").width()-160) ;
				if(receiverusername.width()>maxWidth){
					receiverusername.css({"width":maxWidth+"px"});
				} else {
					receiverusername.css({"width":(receiverusername.width()+5)+"px"});
				}
//				$(".receiveruserstatus").html($(this).attr('itemstatus'));
				if($(this).attr('itemsignature')){
					$(".receiveruserstatus").html($(this).attr('itemsignature'));
				} else {
					$(".receiveruserstatus").html("");
				}
				$(".im-chat-list").html('<div id="msgMoreButton" page="0"><div class="msgMore">更多历史消息...</div></div>');
				$("#flyingchatinput").val("");
				$(".userpic").find(".useritempic").attr('src', $(this).attr('itemuserportrait'));
				if(receiver == 'fyBot'){
					$(".userpic-status").removeClass("user-online").removeClass("user-offline");
				} else if($(this).attr('itemstatus') == '离线'){
					$(".userpic-status").removeClass("user-online").addClass("user-offline");
				} else {
					$(".userpic-status").removeClass("user-offline").addClass("user-online");
				}
				if(receiver == 'fyBot'){
					_opts.fyBotInitHtml() ;
				}
				if($('#newmessage'+hexname).html()!="0"){
					_opts.getOldNotSeeMessage(realreceiver, 0) ;
				} else {
					_opts.getHistoryMessage(realreceiver, 0, 1, 0) ;
				}
				var obj = $('#newmessage'+hexname) ;
				var countObj = $('#newmessageval'+hexname) ;
				obj.css({display:'none',width:'12px',left:'42px'}) ;
				_opts.updateParentTypeCount(obj.attr("typeid"), -(countObj.val()*1)) ;
				obj.html(0) ;
				countObj.val(0);
				
				removeObjectByClassName('msgGotoUserUrl', realreceiver);
			});
		},
		bindEventForGroupUsersList: function () {
			$('#groupuserListId').on("click", "li", function(){
				var realreceiver = $(this).attr('itemuser');
				if($("#flyingchat").attr("username") == realreceiver){
					return;
				}
				$("#groupusersspanId").css("display", "none") ;
				var hexname = hex_md5(realreceiver);
				var receiver = realreceiver.replace("@", "\\40");
				var receiverusername = $('#receiverusername') ;
				receiverusername.attr("isGroup", "0");
				$.WebIM.newWebIM({
					receiver: receiver
				});
				receiverusername.css({"width":"auto"});
				receiverusername.attr("groupName",$(this).attr('itemuserfullname'));
				receiverusername.html($(this).attr('itemuserfullname'));
				var maxWidth = ($("#mainContentRight").width()-160) ;
				if(receiverusername.width()>maxWidth){
					receiverusername.css({"width":maxWidth+"px"});
				} else {
					receiverusername.css({"width":(receiverusername.width()+5)+"px"});
				}
				
				$(".receiveruserstatus").html($(this).attr('itemsignature')?$(this).attr('itemsignature'):"");
				$(".im-chat-list").html('<div id="msgMoreButton" page="0"><div class="msgMore">更多历史消息...</div></div>');
				$("#flyingchatinput").val("");
				$(".userpic").find(".useritempic").attr('src', $(this).attr('itemuserportrait'));
				if($(this).attr('itemstatus') == '离线'){
					$(".userpic-status").removeClass("user-online").addClass("user-offline");
				} else {
					$(".userpic-status").removeClass("user-offline").addClass("user-online");
				}
				if($('#newmessage'+hexname).html()!="0"){
					_opts.getOldNotSeeMessage(realreceiver, 0) ;
				} else {
					_opts.getHistoryMessage(realreceiver, 0, 1, 0) ;
				}
				$(".group-usersdiv").animate({height:'0px'},'slow',function(){
					$(".group-usersdiv").css({"display": "none"});
				}) ;
				$(".group-usersdiv").attr("isShow","false");
				var obj = $('#newmessage'+hexname) ;
				obj.css({display:'none',width:'12px',left:'42px'}) ;
				_opts.updateParentTypeCount(obj.attr("typeid"), -(obj.html()*1)) ;
				obj.html(0) ;
			});
			$('#showGroupUsers').on("click", "li", function(){
				var itemuser = $(this).attr('itemuser') ;
				// var isContainInputFlag = false;
				/**  修复不能再次@用户的问题  **/
				/**var  inputSplitValues = $("#flyingchatinput").val().split(" ");
				for(var i=0;i<inputSplitValues.length;i++){ 
					if(inputSplitValues[i] == ("@"+$(this).attr('fullname')) ){
						isContainInputFlag = true;
						break;
					}
				} 
				**/
				var fullname = $(this).attr('fullname');
				if(!_opts.contains(_opts.groupToUsers, itemuser)){
					_opts.groupToUsers.push(itemuser) ;
				}
					_opts.groupToUsersName.push(fullname);
					$("#flyingchatinput").focus();
					var val = $("#flyingchatinput").val();
					var str = val.substring(0,val.lastIndexOf("@")+1);
					$("#flyingchatinput").val(str+fullname+" ");
					$('#showGroupUsers').css("display", "none");
					$("#showGroupUsers #showGroupuserListsId").find("li").each(function(){
						 $(this).css("display","block");
					})
				/**} else {
					$('#showGroupUsers').css("display", "none");
					$("#flyingchatinput").focus();
					if(isContainInputFlag){
						$("#flyingchatinput").val($("#flyingchatinput").val());
					}else{
						$("#flyingchatinput").val($("#flyingchatinput").val()+" @"+fullname+" ");
						_opts.groupToUsersName.push(fullname);
					}
				}**/
			});
		},
		
		groupToUsers:[],
		groupToUsersName:[],
		contains : function (array, element) {
		    for (var i = 0; i < array.length; i++) {
		        if (array[i] == element) {
		            return true;
		        }
		    }
		    return false;
		},
		bindEventForGroupList: function () {
			$('#flyingchatgrouplist').on("click", ".groupitem", function(){
				if($(this).attr('isAdd') == "true"){
					_opts.createGroup($("#flyingchat").attr("username"));
				} else {
					_opts.openGroup($(this));
				}
			})
			$('#flyingchatgrouplist').on("mouseover", ".groupitem", function(){
				var obj = $(this) ;
				obj.find(".groupitembuttons").css("display", "block") ;
				obj.find(".groupitemout").css("display", "block") ;
				obj.find(".groupitemset").css("display", "block") ;
				obj.find(".groupitemdrop").css("display", "block") ;
			});
			$('#flyingchatgrouplist').on("mouseout", ".groupitem", function(){
				var obj = $(this) ;
				obj.find(".groupitembuttons").css("display", "none") ;
				obj.find(".groupitemout").css("display", "none") ;
				obj.find(".groupitemset").css("display", "none") ;
				obj.find(".groupitemdrop").css("display", "none") ;
			});
			$('#flyingchatgrouplist').on("click", ".groupitemout", function(){
				var flag = $(this).attr("flag") ;
				var groupname = $(this).attr("groupname") ;
				
				var url = $("#flyingchat").attr("baseurl")+'/rest/chat/outGroup?callback=?';
				var data = {'companyId':$("#flyingchat").attr("companyId"), 'groupflag':flag, "groupid":$(this).attr("groupid"), "userid":$("#flyingchat").attr("userId"), "username":$("#flyingchat").attr("username"), "fullname":encodeURI($("#current_user_name").html(), "utf-8")};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
					if(json.isOk){
						showBottomMsg("退出群组成功！",1);
						_opts.reloadGroupList($("#flyingchat").attr("baseurl"), $("#flyingchat").attr("companyId"), $("#flyingchat").attr("username"));
						var arg = 'type=out_group&groups='+flag+'&username='+window.userName.replace('@', '\\40');
						remote.jsjac.chat.ofuserservice(arg, false) ;
						var content = [groupname, "broadcast-", flag, "<", $("#current_user_name").html(), "退出群组！" ].join("");
						remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
						if($("#receiverusername").attr("receiver") == flag){
							_opts.gotoUser('fyBot') ;
						}
					} else {
						showBottomMsg("退出群组失败！", 3);
					}
				});
				$('#flyingchatgrouplist').perfectScrollbar('update');
				return false;
			});
			$('#flyingchatgrouplist').on("click", ".groupitemset", function(){
				var flag = $(this).attr("flag") ;
				var groupname = $(this).attr("groupname") ;
				var groupid = $(this).attr("groupid") ;
				var remark = $(this).attr("remark") ;
				_opts.editGroup(groupid, flag, groupname, remark) ;
				return false;
			});
			$('#flyingchatgrouplist').on("click", ".groupitemdrop", function(){
				var flag = $(this).attr("flag") ;
				var groupname = $(this).attr("groupname") ;
				
				var url = $("#flyingchat").attr("baseurl")+'/rest/chat/deleteGroup?callback=?';
				var data = {'companyId':$("#flyingchat").attr("companyId"),'userid':window.userId,'username':window.userName, 'groupflag':flag, "groupid":$(this).attr("groupid")};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
					if(json.isOk){
						showBottomMsg("解散群组成功！", 1);
						_opts.reloadGroupList($("#flyingchat").attr("baseurl"), $("#flyingchat").attr("companyId"), $("#flyingchat").attr("username"));
						var content = groupname+"broadcast-dropgroup"+flag ;
						remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
						setTimeout(function(){
							var arg = 'type=delete_group&groups='+flag;
							remote.jsjac.chat.ofuserservice(arg, true) ;
						}, 2000);
					} else {
						showBottomMsg("解散群组失败！", 3);
					}
				});
				$('#flyingchatgrouplist').perfectScrollbar('update');
				return false;
			});
		},
		/** 飞扬机器人默认展现内容 **/
		fyBotInitHtml: function () {
			var fyBot = $('#receiverusername').attr("groupName");
			var username = $("#current_user_name").html();
			var context = [
				'<div id="fybotStartMsg"><p class="fybot1">亲爱的',username,'，您好！</p>',
				'<p class="fybot2">我是', fyBot, '，是您东方飞扬的第一位小伙伴。您可以任性的对我吐露心扉，我不会告诉任何人，只会默默的陪伴在您的左右!</p></div>'
				/**
				我比较的愚蠢，但只要您乐于尝试，就会给您带来别样的感受！</p>',
				'<p class="fybot2">', fyBot, '是一位独特的小伙伴，您可以任性的对我吐露心扉，我不会告诉任何人，只会默默的陪伴在您的左右，当您需要的时候，在下面的聊天室中输入"fyBot -s "(Alt+s)，并在后面添加上要检索的相关词，我即可返回您之前吐露的心语；并且在私聊与群聊下还可以使用此方式检索历史聊天记录。</p></div>',
				**/
			].join("");
			$(context).insertBefore($("#msgMoreButton"));
			$("#receiveMessageDiv").scrollTop($(".im-chat-list").height());
			$("#receiveMessageDiv").perfectScrollbar('update');
		},
		/** 与飞扬机器人开始聊天 **/
		fyBotSessionStart: function () {
			var fyBot = $('#receiverusername').attr("groupName");
			var username = $("#current_user_name").html();
			_opts.fyBotSaveData($('#flyingchat').attr("username"), 'fyBot', "亲爱的"+username+"您好，为了您能够在您的团队中更好的交流，我可以协助您设置一些个人信息。", _opts.getDate(), _opts.getDatetime(), null, "") ;
			var context = _opts.createDateHr(_opts.getDate()) ;
			context += _opts.receiveMessageTpl(fyBot, null, "亲爱的"+username+"您好，为了您能够在您的团队中更好的交流，我可以协助您设置一些个人信息。", false, _opts.getDate(), _opts.getDatetime(), defaultPortrait, _opts.fybot) ;
			context += _opts.receiveMessageTpl(fyBot, null, fyBotSets["S0"] + " <a  id=\"fyBotNext_0\" onclick='$.WebIM.messageClickFun(\"skip_0\")'>跳过</a>", false, _opts.getDate(), _opts.getDatetime(), defaultPortrait, _opts.fybot) ;
			$(".im-chat-list").append(context);
			$("#receiveMessageDiv").scrollTop($(".im-chat-list").height());
			$("#receiveMessageDiv").perfectScrollbar('update');
			$('#receiverusername').attr("fybotstep", "0") ;
			setTimeout(function () {
				_opts.fyBotSaveData($('#flyingchat').attr("username"), 'fyBot', fyBotSets["S0"], _opts.getDate(), _opts.getDatetime(), null, "") ;
			}, 1000);
		},
		/** 飞扬机器人跳到下一个问题的处理方法 **/
		fyBotNext: function (step) {
			$('#fyBotNext_'+step).css("display", "none");
			step = (step*1)+1 ;
			var fyBot = $('#receiverusername').attr("groupName");
			var content = fyBotSets["S"+step] ;
			if(step < 4){
				content += " <a id=\"fyBotNext_"+step+"\" onclick='$.WebIM.messageClickFun(\"skip_"+step+"\")'>跳过</a>" ;
			}
			$('#receiverusername').attr("fybotstep", step) ;
			var context = _opts.receiveMessageTpl(fyBot, null, content, false, _opts.getDate(), _opts.getDatetime(), defaultPortrait, _opts.fybot) ;
			$(".im-chat-list").append(context);
			$("#receiveMessageDiv").scrollTop($(".im-chat-list").height());
			$("#receiveMessageDiv").perfectScrollbar('update');
			$('#receiverusername').attr("fybotstep", step) ;
			_opts.fyBotSaveData($('#flyingchat').attr("username"), 'fyBot', fyBotSets["S"+step], _opts.getDate(), _opts.getDatetime(), null, "") ;
		},
		
		validateFyBotData: function (content) {
			var step = $('#receiverusername').attr("fybotstep") ;
			var msg = "" ;
			if(step == "0"){
				if((fyVilidateStr.teleZZ).test(content)==false){
					msg = "抱歉，您输入的公司电话格式不正确，正确格式如：010-51655505." ;
				}
			} else if(step == "1"){
				if((fyVilidateStr.mobtelZZ).test(content)==false){
					msg = "抱歉，您输入的手机号码格式不正确，正确格式如：18551655505." ;
				}
			} else if(step == "2"){
				if((fyVilidateStr.fexZZ).test(content)==false){
					msg = "抱歉，您输入的传真地址格式不正确，正确格式如：010-58957560." ;
				}
			} else if(step == "3"){
				if(content.length > 50){
					msg = "抱歉，您输入的职位过长，公司职位支持的最大长度为50." ;
				} else if(content.length>0 && (fyVilidateStr.positionZZ).test(content)==false){
					msg = "抱歉，公司职位不能包含&  &#47 | &#92; ? . ' , &quot; &gt; &lt; ;等特殊符号";
				}
			}
			return msg ;
		},
		
		fyBotMessageHandler: function (content, styleTpl) {
			var step = $('#receiverusername').attr("fybotstep") ;
			_opts.fyBotSaveData('fyBot', $('#flyingchat').attr("username"), content, _opts.getDate(), _opts.getDatetime(), fyBotSetToField["F"+step], styleTpl) ;
			$('#fyBotNext_'+step).css("display", "none");
			step = (step*1)+1 ;
			if(step == 5){
				return ;
			}
			var fyBot = $('#receiverusername').attr("groupName");
			var content = fyBotSets["S"+step] ;
			if(step < 4){
				content += " <a id=\"fyBotNext_"+step+"\" onclick='$.WebIM.messageClickFun(\"skip_"+step+"\")'>跳过</a>" ;
			}
			var context = _opts.receiveMessageTpl(fyBot, null, content, false, _opts.getDate(), _opts.getDatetime(), defaultPortrait, _opts.fybot) ;
			$(".im-chat-list").append(context);
			$("#receiveMessageDiv").scrollTop($(".im-chat-list").height());
			$("#receiveMessageDiv").perfectScrollbar('update');
			$('#receiverusername').attr("fybotstep", step) ;
			_opts.fyBotSaveData($('#flyingchat').attr("username"), 'fyBot', fyBotSets["S"+step], _opts.getDate(), _opts.getDatetime(), null, "") ;
		},
		fyBotSaveData: function (userName, from, content, date, time, field, styleTpl) {
			if(content){
				_opts.postSaveHistoryMessage({'companyId':window.companyid, 'username':userName, 'from':from, 'content':content, 'date':date, 'time':time, 'isGroup':0, 'fromCnName':_opts.fybot, 'styleTpl':styleTpl}) ;
				if(field != null){
					$.ajax({
						url:$.appClient.generateUrl({ESUserInfo:'editUserInfo'},'x'),
						type:'POST',
						data:{param : field+"="+content},
						success:function(data){
						}
					});
				}
			}
		},
		fyBotSearch: function (obj, content) {
			/**
			if("fyBot -s" == content){
				showBottomMsg("您没有输入检索词，不能进行检索操作！", 2);
				return ;
			} else {
			}
			**/
			//content = content.substring(8) ;
			_opts.getHistoryMessage($('#receiverusername').attr("receiver"), $('#receiverusername').attr("isGroup"), 1, 0, encodeURI($.trim(content), "utf-8")) ;
			
			obj.val("");
		},
		
		buildFyBot: function () {
			var fyBotStr = "" ;
			fyBotStr += "<div id='userListItem-fyBot' class='groupitemchild' style='display:block;'><ul>" ;
			fyBotStr += "<li id='itemuser-"+hex_md5("fyBot")+"' itemuser='fyBot' itemuserfullname='"+_opts.fybot+"' itemstatus='' itemuserportrait='"+defaultPortrait+"'><table><tr><td rowspan='2'><div id='newmessage"+hex_md5("fyBot")+"' class='newmessage' style='display:none;width:12;left:12px'>0</div><img width='32' height='32' class='useritempic' src='"+defaultPortrait+"'/></td><td><span class='usersname'>"+_opts.fybot+"</span></td></tr><tr><td><span class='userstatus'>飞扬助手</span></td></tr></table></li>" ;
			fyBotStr += "</ul></div>" ;
			return fyBotStr ;
		},
		groupnewmessagestyle: function(newmessagecount, callselfcount){
			var count = newmessagecount*1+callselfcount*1;
			return "display:"+(count==0?"none":"block")+";width:"+(6+(count+"").length*6)+"px;left:"+(48-(count+"").length*6)+"px" ;
		},
		groupnewmessagestyleclass: function(newmessagecount, callselfcount){
			var count = newmessagecount*1+callselfcount*1;
			if(count > 99){
				return "display:"+(count==0?"none":"block")+";width:"+(6+(count+"+").length*6)+"px;" ;
			}else{
				return "display:"+(count==0?"none":"block")+";width:"+(6+(count+"").length*6)+"px;" ;
			}
		},
		groupnewmessageCounter:function(messageCount){
			if(messageCount*1 > 99){
				return "99+";
			}else{
				return messageCount*1;
			}
			
		},
		groupnewmessagevalue: function(newmessagecount,callselfcount){
			var count = newmessagecount*1+callselfcount*1;
			count = count > 99 ? '99+' : count + '' ;
			return count;
		},
		showText: function(text, username){
			return (text.length>10?text.substring(0, 10):text)+(username==window.userName?"(自己)":"") ;
		},
		/** 组装群组列表 **/
		reloadGroupList: function (baseurl, companyId, username) {
			if(window.companyid == '-1'){
				return false;
			}
			
			var url = baseurl+'/rest/chat/getGroupsByUsername/'+companyId+'/'+username+'?callback=?';
			var data = {};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				var groupListStr = '<div class="groupitem" isAdd="true">';
				groupListStr += '<table><tr><td><img width="32" height="32" class="useritempic" src="apps/onlinefile/templates/ESDefault/images/addgroup.png"/></td><td><span >创建群组</span></td></tr></table>';
				groupListStr += '</div>';
				$('#flyingchatgroups').html(groupListStr+template('chatgroups_templete', json));
				_opts.scrollbarCreate($('#flyingchatgrouplist'));
				var gc = 0 ;
				var agc = 0 ;
				var groups = json.groups ;
				if(groups){
					for(var g=0;g<groups.length;g++){
						gc += groups[g].callselfcount*1 ;
						agc += groups[g].newmessagecount*1 ;
					}
					//xiewenda 初始化消息数
					$("#items-groups-messcount").html(0);
					$("#items-groups-messcount-val").val(0);
					_opts.updateGroupParentTypeCount(gc+agc);
					/*if(agc>0){
						if(gc>0){
							_opts.updateGroupParentTypeCount(gc+agc) ;
						} else {
							_opts.updateGroupParentTypeCount(agc) ;
						}
					}*/
				}
			});
		},
		createGroup: function (username) {
			$.ajax({
				type:'POST',
		        url : $.appClient.generateUrl({ESDefault : 'createGroup'},'x'),
		        data: {username:username},
			    success:function(data){
					$.fywindow({
					    	title:'创建群组',
							width: 500,
							height: 420,
						    content:data,
						    okVal:'确定',
						    cancel:true,
							cancelVal:'取消',
							ok: function(){
								var groupname = $("#groupnameid").val();
								if(groupname == ''){
									$("#groupnamemsg").find("span").html("群组名称不能为空！") ;
									$("#groupnamemsg").show() ;
									return false ;
								} else if($.trim(groupname) == ''){
									$("#groupnamemsg").find("span").html("群组名称不能全为空格！") ;
									$("#groupnamemsg").show() ;
									return false ;
								} else if($.trim(groupname).length>20){
									$("#groupnamemsg").find("span").html("群组名称的最大长度为20！") ;
									$("#groupnamemsg").show() ;
									return false ;
								} else {
									$("#groupnamemsg").hide() ;
								}
								var groupremark = $("#groupremarkid").val();
								if($.trim(groupremark).length>100){
									$("#groupremarkmsg").find("span").html("群组描述的最大长度为100！") ;
									$("#groupremarkmsg").show() ;
									return false ;
								} else {
									$("#groupremarkmsg").hide() ;
								}
								var groupuserids = "" ;
								var groupusernames = "" ;
								$('#rightUserlist').find("li").each(function(){
									groupuserids += $(this).attr('userid')+"," ;
									groupusernames += $(this).attr('username').replace('@', '\\40')+"," ;
								});
								groupuserids = groupuserids.substring(0, groupuserids.length-1) ;
								groupusernames = groupusernames.substring(0, groupusernames.length-1) ;
								
								var url = $("#flyingchat").attr("baseurl")+'/rest/chat/createGroup?callback=?';
								var data = {'companyId':$("#flyingchat").attr("companyId"), 'username':username,'userid':window.userId, 'groupuserids':groupuserids, 'manageruserid':window.userId, 'groupname':encodeURI($.trim(groupname), "utf-8"), 'groupremark':encodeURI($("#groupremarkid").val(), "utf-8")};
								var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
								jQuery.getJSON(url, ret.data,
										function(json) {
									if(json.isOk){
										showBottomMsg("创建群组成功！", 1);
										_opts.reloadGroupList($("#flyingchat").attr("baseurl"), $("#flyingchat").attr("companyId"), $("#flyingchat").attr("username"));
										var arg = 'type=add_group&groups='+json.FLAG+'&username='+groupusernames;
										remote.jsjac.chat.ofuserservice(arg, false) ;
										var content = groupname+"broadcast-addgroup"+json.FLAG+";"+$("#current_user_name").html()+"邀请您加入群组！" ;
										remote.jsjac.chat.sendMessage(content, json.FLAG+"@broadcast."+remote.jsjac.domain);
									} else {
										showBottomMsg("创建群组失败！", 3);
									}
								});
						    }
					    });
			    },
			    cache:false
			});
		},
		editGroup: function (groupid, flag, groupname, remark) {
			var username = $("#flyingchat").attr("username");
			$.ajax({
				type:'POST',
				url : $.appClient.generateUrl({ESDefault : 'editGroup'},'x'),
				data: {groupid:groupid, groupname:groupname, remark:remark},
				success:function(data){
					$.fywindow({
				    	title:'编辑群组',
						width: 500,
						height: 420,
					    content:data,
					    okVal:'确定',
					    cancel:true,
						cancelVal:'取消',
						ok: function(){
							var groupname = $("#groupnameid").val();
							if(groupname == ''){
								$("#groupnamemsg").find("span").html("群组名称不能为空！") ;
								$("#groupnamemsg").show() ;
								return false ;
							} else if($.trim(groupname) == ''){
								$("#groupnamemsg").find("span").html("群组名称不能全为空格！") ;
								$("#groupnamemsg").show() ;
								return false ;
							} else if($.trim(groupname).length>20){
								$("#groupnamemsg").find("span").html("群组名称的最大长度为20！") ;
								$("#groupnamemsg").show() ;
								return false ;
							} else {
								$("#groupnamemsg").hide() ;
							}
							var groupremark = $("#groupremarkid").val();
							if($.trim(groupremark).length>100){
								$("#groupremarkmsg").find("span").html("群组描述的最大长度为100！") ;
								$("#groupremarkmsg").show() ;
								return false ;
							} else {
								$("#groupremarkmsg").hide() ;
							}
							var addgroupuserids = "" ;
							var addgroupusernames = "" ;
							var addgroupfullnames = "" ;
							var deletegroupuserids = "" ;
							var deletegroupusernames = "" ;
							var deletegroupfullnames = "" ;
							var oldgroupuserids = $('#oldgroupuserids').val() ;
							var olduserids = oldgroupuserids.split(",");
							$('#rightUserlist').find("li").each(function(){
								var no = $.inArray($(this).attr('userid'), olduserids) ;
								if(no == -1){
									addgroupuserids += $(this).attr('userid')+"," ;
									addgroupusernames += $(this).attr('username').replace('@', '\\40')+"," ;
									addgroupfullnames += $(this).html()+"," ;
								} else {
									olduserids.splice(no,1); 
								}
							});
							for(var i=0;i<olduserids.length;i++){
								deletegroupuserids += olduserids[i]+"," ;
								deletegroupusernames += $("#U_"+olduserids[i]).attr('username').replace('@', '\\40')+"," ;
								deletegroupfullnames += $("#U_"+olduserids[i]).html()+"," ;
							}
							var changeusers = true ;
							if(addgroupuserids=="" && deletegroupuserids==""){
								changeusers = false ;
							} else {
								if(addgroupuserids!=""){
									addgroupuserids = addgroupuserids.substring(0, addgroupuserids.length-1) ;
									addgroupusernames = addgroupusernames.substring(0, addgroupusernames.length-1) ;
									addgroupfullnames = addgroupfullnames.substring(0, addgroupfullnames.length-1) ;
								}
								if(deletegroupuserids!=""){
									deletegroupuserids = deletegroupuserids.substring(0, deletegroupuserids.length-1) ;
									deletegroupusernames = deletegroupusernames.substring(0, deletegroupusernames.length-1) ;
									deletegroupfullnames = deletegroupfullnames.substring(0, deletegroupfullnames.length-1) ;
								}
							}
							var changeitems = true ;
							if($.trim(groupname) == $("#groupnameid").attr("oldvalue") && $.trim($("#groupremarkid").val()) == $("#groupremarkid").attr("oldvalue")){
								changeitems = false ;
							}
							if(!changeusers && !changeitems){
								return true;
							}
							var url = $("#flyingchat").attr("baseurl")+'/rest/chat/resetGroup?callback=?';
							var data = {'companyId':$("#flyingchat").attr("companyId"), 'username':username,'userid':window.userId, 'addgroupuserids':addgroupuserids, 'deletegroupuserids':deletegroupuserids, 'groupname':encodeURI($.trim(groupname), "utf-8"), 'groupremark':encodeURI($("#groupremarkid").val(), "utf-8"), groupid:$("#groupid").val(), groupflag:flag, manageruserid:window.userId, changeusers:changeusers, changeitems:changeitems, "fullname":encodeURI(window.fullName, "utf-8")};
							var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
							jQuery.getJSON(url, ret.data,
									function(json) {
								if(json.isOk){
									showBottomMsg("编辑群组成功！", 1);
									_opts.reloadGroupList($("#flyingchat").attr("baseurl"), $("#flyingchat").attr("companyId"), $("#flyingchat").attr("username"));
									if(changeusers){
										var arg = 'type=reset_group&groups='+flag+'&addgroupusernames='+addgroupusernames+'&deletegroupusernames='+deletegroupusernames;
										remote.jsjac.chat.ofuserservice(arg, false) ;
									}
									if(addgroupuserids.length>0){
										var content = groupname+"broadcast-addgroup"+flag+";"+$("#current_user_name").html()+"邀请您加入群组！"+addgroupuserids+";"+addgroupfullnames ;
										remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
									}
									$('#receiverusername').attr("groupName",$.trim(groupname));
									$('#receiverusername').attr("title", $.trim(groupname));
									$(".receiveruserstatus").html($.trim(groupremark));
									$(".receiveruserstatus").attr("title", $.trim(groupremark));
									_opts.reloadGroupUsers(flag) ;
									if(deletegroupusernames.length > 0){
										var deleteArray = deletegroupusernames.split(",");
										for(var d=0;d<deleteArray.length;d++){
											var dcontent = flag+"broadcast-removeuser"+$("#current_user_name").html()+"将您从["+groupname+"]群组中请出！" ;
											remote.jsjac.chat.sendMessage(dcontent, deleteArray[d]+"@"+remote.jsjac.domain);
										}
										var content = groupname+"broadcast-deletegroupuser"+flag+";"+deletegroupuserids+";"+deletegroupfullnames ;
										remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
									}
								} else {
									showBottomMsg("编辑群组失败！", 3);
								}
							});
					    }
				    });
				},
				cache:false
			});
		},
		openGroup: function (groupObj) {
			var groupFlag = groupObj.attr('flag');
			var id = groupObj.attr('id');
			var useritempic = null;
			useritempic = $(groupObj).find(".useritempic").attr('src');
			
			if(groupFlag == undefined){
				showBottomMsg("未找到群组(或分类)。请确认您是否已退出该群组(或分类)或者已被解散！", 3);
				return false;
			}
			
			if($('#receiverusername').attr("receiver") == groupFlag){
				return;
			}
			$('#receiverusername').attr("isGroup", "1");
			$('#receiverusername').attr("groupFlag", groupObj.attr('flag'));
			$.WebIM.newWebIM({
				receiver: groupFlag+"@broadcast."+remote.jsjac.domain
			});
			if(groupObj.attr("createtime")) {
				_opts.reloadGroupUsers(groupFlag) ;
			}
			_opts.groupToUsers=[];
			_opts.groupToUsersName=[];
			$('#receiverusername').empty();
			$(".receiveruserstatus").empty();
			if(groupObj.attr('groupname')){
				$('#receiverusername').attr("groupName",groupObj.attr('groupname')).text(groupObj.attr('groupname')).attr("title",groupObj.attr('groupname'));
				//$('#receiverusername').attr("title", groupObj.attr('groupname'));
			}
			if(groupObj.attr('remark')){
				$(".receiveruserstatus").html(groupObj.attr('remark'));
				$(".receiveruserstatus").attr("title", groupObj.attr('remark'));
			} else {
				$(".receiveruserstatus").html("");
			}
			$(".im-chat-list").html('<div id="msgMoreButton" page="0"><div class="msgMore">更多历史消息...</div></div>');
			
			$(".userpic").find(".useritempic").attr('src', useritempic);
			
//			if(groupObj.attr('portrait')){
//				$(".userpic").find(".useritempic").attr('src', useritempic);
//				$(".userpic").find(".useritempic").attr('src', groupObj.attr('portrait'));
//			} else {
////				$(".userpic").find(".useritempic").attr('src', "apps/onlinefile/templates/ESDefault/images/im/classgroupicon.png");
//			}
			$(".userpic-status").removeClass("user-offline").removeClass("user-online");
			var hexname = hex_md5(groupObj.attr('flag'));
			var newmessage = $('#newmessage'+hexname) ;
			$('#receiverusername').attr("joindate", groupObj.attr("joindate"));
			$('#receiverusername').attr("jointime", groupObj.attr("jointime"));
			if(newmessage.attr("newmessagecount")!="0"){
				_opts.getOldNotSeeMessage(groupObj.attr('flag'), 1) ;
				
				//wangwenshuo 20151029  点击分类后刷新分类  取消当前分类按消息数排序
				var obj = newmessage.parent();
				if(obj.length>0){
					var isStar = obj.attr("isStar");
					var classId = obj.attr("data-class-id");
					documentCenter.getClassList(window.userId,classId,isStar);
				}
			} else {
				_opts.getHistoryMessage(groupObj.attr('flag'), 1, 1, 0) ;
			}
			if(newmessage.attr("class")=="classnewmessage"){
				newmessage.css({display:'none',width:'12px'}) ;
				newmessage.html(0) ;
			} else {
				newmessage.css({display:'none',width:'12px',left:'42px'}) ;
				newmessage.html(0) ;
				newmessage.attr("newmessagecount", 0);
				_opts.updateGroupParentTypeCountForOpenGroup() ;
			}
			newmessage.attr("newmessagecount","0");
			removeObjectByClassName('msgGotoGroupUrl', groupFlag);
		},
		reloadGroupUsers: function (groupFlag) {
			var url = $("#flyingchat").attr("baseurl")+'/rest/chat/getOneGroupUsers?callback=?';
			var data = {'companyId':$("#flyingchat").attr("companyId"), 'username':$("#flyingchat").attr("username"), 'groupFlag':groupFlag};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				var all = json.all ;
				var online = json.online ;
				$('#groupuserListsId').empty();
				$('#showGroupuserListsId').empty();
				if(all > 0 ) {
					$('#receiverusername').css({"width":"auto"});
					$('#groupuserListsId').html(template('groupuserListsTemplate', json));
					$("#receiverusername").html(json.FULLNAME);
					$("#groupusersspanId").html("("+online+"/"+all+")");
					$("#groupusersspanId").attr("onlinecount", online);
					$("#groupusersspanId").attr("allcount", all);
					$("#groupusersspanId").css({"display": "block"});
					$('#showGroupuserListsId').html(template('groupuserselectlistTemplate', json));
					$('#showGroupUsers').attr("count", all) ;
					_opts.scrollbarCreate($('#showGroupuserListId'));
					var tempwidth = 30+(("("+online+"/"+all+")").length - 5)*8 ;
					var maxWidth = ($("#mainContentRight").width()-160-tempwidth) ;
					if($('#receiverusername').width()>maxWidth){
						$('#receiverusername').css({"width":maxWidth+"px"});
					} else {
						$('#receiverusername').css({"width":($('#receiverusername').width()+5)+"px"});
					}
				} else {
					$("#groupusersspanId").css({"display": "none"});
				}
				_opts.scrollbarCreate($('#groupuserListId'));
			});
		},
		reloadGroupUsersForClass: function (data) {
				var json = {users:data.users[0], username:window.userName} ;
				var all = data.itemallcount[0] ;
				var online = data.itemonlinecount[0] ;
				$('#groupuserListsId').empty();
				$('#showGroupuserListsId').empty();
				if(all > 0 ) {
					$('#receiverusername').css({"width":"auto"});
					$('#groupuserListsId').html(template('groupuserListsTemplate', json));
					$("#groupusersspanId").html("("+online+"/"+all+")");
					$("#groupusersspanId").attr("onlinecount", online);
					$("#groupusersspanId").attr("allcount", all);
					$("#groupusersspanId").css({"display": "block"});
					$('#showGroupuserListsId').html(template('groupuserselectlistTemplate', json));
					$('#showGroupUsers').attr("count", all) ;
					_opts.scrollbarCreate($('#showGroupuserListId'));
					var tempwidth = 30+(("("+online+"/"+all+")").length - 5)*8 ;
					var maxWidth = ($("#mainContentRight").width()-160-tempwidth) ;
					if($('#receiverusername').width()>maxWidth){
						$('#receiverusername').css({"width":maxWidth+"px"});
					} else {
						$('#receiverusername').css({"width":($('#receiverusername').width()+5)+"px"});
					}
				} else {
					$("#groupusersspanId").css({"display": "none"});
				}
				_opts.scrollbarCreate($('#groupuserListId'));
		},
		/** 保存用户在线，但是没有看的消息，为了之后可能正常查看 **/
		saveNotSeeMessage: function (userJID, userName, content, date, time, fromCnName,fromCompanyId) {
			/*liumingchao*/
			if(fromCnName.length>10){
				fromCnName = fromCnName.substring(0,10);
				fromCnName +='...';
			}
			/*liumingchao*/
			/** 判断是否为用户登录广播，如果是的话，不保存到库中 **/
			if(content.indexOf('broadcast-online')>-1){
				userName = content.substring(17);
				if(userName == $('#flyingchat').attr('username')){
					return ;
				}
				var md5 = hex_md5(userName) ;
				var obj = $('#itemuser-'+md5) ;
				if("在线" == obj.attr("itemstatus")){
					return ;
				}
				var itemspan = $("#"+obj.attr('itemspanid')) ;
				var itemonlinecounts = itemspan.attr("itemonlinecounts") ;
				var itemallcounts = itemspan.attr("itemallcounts") ;
				itemspan.html(itemspan.attr("text")+"("+(itemonlinecounts*1+1)+"/"+itemallcounts+")") ;
				itemspan.attr("itemonlinecounts", (itemonlinecounts*1+1)) ;
				if(window.ISUPREMIND == "1"){
					stickytester("<a class='msgOnOffLine' userName='"+userName+"'>"+fromCnName+" 上线了！</a>");
				}
				obj.attr("itemstatus", "在线");
				obj.find(".user-status").removeClass("user-offline").addClass("user-online");
				if($("#atgroupusers-"+md5).get(0)){
					$("#atgroupusers-"+md5).find(".userpic-status").removeClass("user-offline").addClass("user-online");
				}
				if($("#groupitemuser-"+md5).get(0)){
					$("#groupitemuser-"+md5).find(".userpic-status").removeClass("user-offline").addClass("user-online");
					var groupusersspanId = $("#groupusersspanId");
					var itemonlinecounts = groupusersspanId.attr("onlinecount") ;
					var itemallcounts = groupusersspanId.attr("allcount") ;
					groupusersspanId.html("("+(itemonlinecounts*1+1)+"/"+itemallcounts+")") ;
					groupusersspanId.attr("onlinecount", (itemonlinecounts*1+1)) ;
				}
				if(userName == $('#receiverusername').attr("receiver")){
					$("#nowchatuserstatus").removeClass("user-offline").addClass("user-online");
				}
				/** 将新上线的用户移动到自己的上边 **/
				var ulObj = obj.parent() ;
				var lastOnlineUser = ulObj.attr("lastOnlineUser");
				if(lastOnlineUser == ""){
					if(ulObj.find("li:first-child").attr("id") != 'itemuser-'+hex_md5(userName)){
						obj.insertBefore(ulObj.find("li:first-child")) ;
					}
					ulObj.attr("lastOnlineUser", '#itemuser-'+hex_md5(userName)) ;
				} else {
					obj.insertBefore($(lastOnlineUser)) ;
				}
				$('.msgGotoUserUrl').click(function(){_opts.gotoUser($(this).attr('userName'));});
			} else if(content.indexOf('broadcast-offline')>-1){
				userName = content.substring(18);
				if(userName == $('#flyingchat').attr('username')){
					$.dialog({
	    				title:"系统提示",
	    				content : '您的账号已在别处登录，如果不是您本人操作，请尽快修改密码，以保障您的账号信息安全！',
	    				cancelValue:'关闭',
	    				cancel : function() {
	    					   window.open(window.logouturl, "_self");
						}
	    			}).showModal();
//					window.open(window.logouturl+"?passiveLogout=1", "_self");  //退出到首页后 弹出错误信息
					setTimeout(function () {
						  window.open(window.logouturl, "_self");
				      	  }, 3000);
					return;
				}
				if("离线" == $('#itemuser-'+hex_md5(userName)).attr("itemstatus")){
					return ;
				}
				if(userName == $('#flyingchat').attr("isCloseUserName")){
		    		var newpage = $('#nowPage').attr("pagecount")*1 ;
		    		onlineReloadUsers(newpage) ;
			    	$('#flyingchat').attr("isCloseUserName", "");
				}
				if(window.ISDOWREMIND == "1"){
					stickytester("<a class='msgOnOffLine' userName='"+userName+"'>"+fromCnName+" 下线了！</a>");
				}
				var md5 = hex_md5(userName) ;
				var obj = $('#itemuser-'+md5) ;
				obj.attr("itemstatus", "离线");
				obj.find(".user-status").removeClass("user-online").addClass("user-offline");
				if($("#atgroupusers-"+md5).get(0)){
					$("#atgroupusers-"+md5).find(".userpic-status").removeClass("user-online").addClass("user-offline");
				}
				if($("#groupitemuser-"+md5).get(0)){
					$("#groupitemuser-"+md5).find(".userpic-status").removeClass("user-online").addClass("user-offline");
					var groupusersspanId = $("#groupusersspanId");
					var itemonlinecounts = groupusersspanId.attr("onlinecount") ;
					var itemallcounts = groupusersspanId.attr("allcount") ;
					if(itemonlinecounts*1>0){
						groupusersspanId.html("("+(itemonlinecounts*1-1)+"/"+itemallcounts+")") ;
						groupusersspanId.attr("onlinecount", (itemonlinecounts*1-1)) ;
					}
				}
				if(userName == $('#receiverusername').attr("receiver")){
					$("#nowchatuserstatus").removeClass("user-online").addClass("user-offline");
				}
				var itemspan = $("#"+obj.attr('itemspanid')) ;
				var itemonlinecounts = itemspan.attr("itemonlinecounts") ;
				var itemallcounts = itemspan.attr("itemallcounts") ;
				if(itemonlinecounts*1>0){
					itemspan.html(itemspan.attr("text")+"("+(itemonlinecounts*1-1)+"/"+itemallcounts+")") ;
					itemspan.attr("itemonlinecounts", (itemonlinecounts*1-1)) ;
				}
				/** 将新下线的用户移动到自己的下边 **/
				var ulObj = obj.parent() ;
				var lastOnlineUser = ulObj.attr("lastOnlineUser");
				if(itemonlinecounts*1 == 1){
					ulObj.attr("lastOnlineUser", "") ;
				} else {
					if(lastOnlineUser == '#itemuser-'+hex_md5(userName)){
						ulObj.attr("lastOnlineUser", "#"+obj.prev().attr("ID")) ;
					} else {
						obj.insertAfter($(lastOnlineUser)) ;
					}
				}
			} else if(content.indexOf('broadcast-dropgroup')>-1){
				var splitNo = content.indexOf('broadcast-dropgroup') ;
				var groupName = content.substring(0, splitNo) ;
				var groupFlag = content.substring(splitNo+19) ;
				if(userName.replace('\\40', '@') == $('#flyingchat').attr('username')){
					if($("#receiverusername").attr("receiver") == groupFlag){
						_opts.gotoUser('fyBot') ;
					}
				}
				/** lujixiang 20151111 删除群组消息数  --start **/
				
				var hexname = hex_md5(groupFlag);
				var newmessageCount = $('#newmessage'+hexname).html() ;
				if(0 < parseInt(newmessageCount)){
					var items_groups_message = $("#items-groups-messcount");
					var differentCount = parseInt(items_groups_message.html()) - newmessageCount;
					items_groups_message.html(differentCount);
					differentCount == 0 ? items_groups_message.css({display:'none'}) : 1;
					
					var userallnewmessage = $("#userallnewmessagecount");
					var differentAllCount = parseInt(userallnewmessage.html()) - newmessageCount;
					userallnewmessage.html(differentAllCount);
					differentAllCount == 0 ? userallnewmessage.css({display:'none'}) : 1;
				}
				
				
				/** lujixiang 20151111 删除群组消息数  --end **/
				
				_opts.reloadGroupList($("#flyingchat").attr("baseurl"), $("#flyingchat").attr("companyId"), $("#flyingchat").attr("username"));
				// stickytester("群组["+groupName+"]：已解散！");
				stickytester("<a class='msgGotoGroupUrl' groupFlag='"+groupFlag+"'>群组["+groupName+"]：已解散！</a>");
//				var userJID = "u" + hex_md5(groupFlag+"@broadcast."+remote.jsjac.domain);
//				if ($("#" + userJID).get(0)) {
				if($("#receiverusername").attr("receiver") == groupFlag){
					_opts.gotoUser('fyBot') ;
				}
				_opts.messageTip();
			} else if(content.indexOf('broadcast-addgroup')>-1){
				/** 创建群组处理 **/
				if(userName.replace('\\40', '@') == $('#flyingchat').attr('username')){
					return ;
				}
				var splitNo = content.indexOf('broadcast-addgroup') ;
				userName = userName.replace('@', '\\40') ;
				var groupName = content.substring(0, splitNo) ;
				var groupFlag = content.substring(splitNo+18, content.indexOf(";"));
				content = content.substring(content.indexOf(";")+1) ;
				
				//wangwenshuo 20150819   解决接受另一个团队消息 从消息中获取companyId     消息格式：xxxxx<compnayId>dfff
				/*var otherCompanyId=-1;
				if(content.indexOf('<')>-1 && content.lastIndexOf('>')>-1){
					otherCompanyId=content.substring(content.indexOf('<')+1, content.lastIndexOf('>'))
					content = content.substring(0,content.indexOf('<'))+content.substring(content.lastIndexOf('>')+1);
				}*/
				
				if(content.substring(content.length-1) == "！"){
//					stickytester("<a class='msgGotoGroupUrl' groupFlag='"+groupFlag+"'>群组["+groupName+"]："+content+"</a>");
//					$('.msgGotoGroupUrl').click(function(){_opts.gotoGroup(groupFlag)});
				} else {
					var addusers = content.substring(content.indexOf("！")+1);
					content = content.substring(0, content.indexOf("！"));
					var array = addusers.split(";") ;
					var ids = ","+array[0]+"," ;
					var names = array[1] ;
					if(ids.indexOf(","+window.userId+",")>-1){
//						stickytester("<a class='msgGotoGroupUrl' groupFlag='"+groupFlag+"'>群组["+groupName+"]："+content+"</a>");
//						$('.msgGotoGroupUrl').click(function(){_opts.gotoGroup(groupFlag)});
					} else {
						content = fromCnName+" 邀请 "+names.replaceAll(",", "、")+" 加入分类！";
//						stickytester("<a class='msgGotoGroupUrl' groupFlag='"+groupFlag+"'>群组["+groupName+"]："+content+"</a>");
//						$('.msgGotoGroupUrl').click(function(){_opts.gotoGroup(groupFlag)});
					}
				}
				//xiewenda 邀请完成重新加载一下右侧分类树 这里只要解决邀请时间的重新获取
				var obj = $(".main-left li[class='active']");
				var isStar = obj.attr("isStar");
				var classId = obj.attr("data-class-id");
				documentCenter.getClassList(window.userId,classId,isStar);
				
				var userJID = "u" + hex_md5(groupFlag+"@broadcast."+remote.jsjac.domain);
				if (!$("#" + userJID).get(0)) {
					_opts.saveNotSeeGroupMessage(userJID, userName, groupFlag, groupName, content, date, time, true, fromCnName, fromCompanyId) ;
					return;
				}
				_opts.messageTip();
				// 向chat接收信息区域写消息
				remote.jsjac.chat.writeMessage(userJID, userName, content);
				var hexname = hex_md5(groupFlag);
				var obj = $('#newmessage'+hexname) ;
				if(obj.attr("class")=="classnewmessage" && $("#class_user_list").get(0)){
					$('#groupMembers').trigger('click');
				}
				_opts.reloadGroupUsers(groupFlag) ;
			} else if(content.indexOf('broadcast-deletegroupuser')>-1){/** 请出群组 **/
				/** 创建群组处理 **/
				if(userName.replace('\\40', '@') == $('#flyingchat').attr('username')){
					return ;
				}
				var splitNo = content.indexOf('broadcast-deletegroupuser') ;
				userName = userName.replace('@', '\\40') ;
				var groupName = content.substring(0, splitNo) ;
				var groupFlag = content.substring(splitNo+25, content.indexOf(";"));
				content = content.substring(content.indexOf(";")+1) ;
				var array = content.split(";") ;
				var ids = ","+array[0]+"," ;
				var names = array[1] ;
				if(ids.indexOf(","+window.userId+",")>-1){
					return;
				} else {
					content = fromCnName+" 将 "+names.replaceAll(",", "、")+" 请出分类！";
//					stickytester("<a class='msgGotoGroupUrl' groupFlag='"+groupFlag+"'>群组["+groupName+"]："+content+"</a>");
//					$('.msgGotoGroupUrl').click(function(){_opts.gotoGroup(groupFlag)});
				}
				var userJID = "u" + hex_md5(groupFlag+"@broadcast."+remote.jsjac.domain);
				if (!$("#" + userJID).get(0)) {
					_opts.saveNotSeeGroupMessage(userJID, userName, groupFlag, groupName, content, date, time, true, fromCnName,fromCompanyId) ;
					return;
				}
				_opts.messageTip();
				// 向chat接收信息区域写消息
				remote.jsjac.chat.writeMessage(userJID, userName, content);
				var hexname = hex_md5(groupFlag);
				var obj = $('#newmessage'+hexname) ;
				if(obj.attr("class")=="classnewmessage" && $("#class_user_list").get(0)){
					$('#groupMembers').trigger('click');
				}
				_opts.reloadGroupUsers(groupFlag) ;
			} else if(content.indexOf('broadcast-removeuser')>-1){/** 右侧群组 **/
				var splitNo = content.indexOf('broadcast-removeuser') ;
				var groupflag = content.substring(0, splitNo);
				content = content.substring(splitNo+20);
				stickytester(content);
				_opts.messageTip();
				_opts.reloadGroupList(window.onlinefilePath, window.companyid, window.userName);
				if($("#receiverusername").attr("receiver") == groupflag){
					_opts.gotoUser('fyBot') ;
				}
			} else if(content.indexOf('broadcast--removeuser')>-1){/** 左侧分类 **/
				var splitNo = content.indexOf('broadcast--removeuser') ;
				var groupflag = content.substring(0, splitNo);
				content = content.substring(splitNo+21);
				stickytester("<a class='msgGotoGroupUrl' groupFlag='"+groupflag+"'>"+content+"</a>");
				_opts.messageTip();
				
				/** lujixiang 20150804 为分类加锁 **/
				var hexname = hex_md5(groupflag);
				var obj = $('#newmessage'+hexname) ;
				obj.attr("newmessagecount", 0);
				obj.css({display:'none'});
				obj.siblings('span').remove();
				var parent = obj.parent() ;
				parent.attr('ismember', 'false');
				parent.append('<span class="isnotMember glyphicon glyphicon-lock" title="访客"></span>');
				
				if($('.main-left li.active').attr('flag') == groupflag){
					var obj = $('.main-left li.active') ;
					var json='{"titleText":"'+obj.attr("groupname")+'"}';
					$('#bodyContent_Title').html(template('documentClass_menu_templete',jQuery.parseJSON(json)));
					$(".main-left li").removeClass("active");
					obj.addClass("active");
					
					// 设置头部
					var jsonHeading = '{"type":"documentAll"}';
					$("#content-heading").html(template('file_panel_heading_template', jQuery.parseJSON(jsonHeading)));
					$("#contentBody").html("");
					changeUploadBtn("0");
					showMainContentLeftFooter(true);
					// 查询文件列表
					documentCenter.getFirstFileList(obj, obj.attr("data-class-id"));
					//新建文件夹按钮
					createClassFile();
					
					var folderId = obj.attr("data-class-id");
					var folderName = obj.attr("groupname");
					var idSeq = obj.attr("data-idseq");
					createFolderBreadcrumbs(folderId,folderName,idSeq);
					$('#receiveMessageDiv').perfectScrollbar('update');
				}
				if($("#receiverusername").attr("receiver") == groupflag){
					_opts.gotoUser('fyBot') ;
				}
			} else if(content.indexOf('broadcast-status')>-1){
				userName = content.substring(0, content.indexOf('broadcast-status'));
				var status = content.substring(content.indexOf('broadcast-status')+16);
				if(userName == $('#flyingchat').attr('username')){
					return ;
				}
				var obj = $('#itemuser-'+hex_md5(userName)) ;
				var spantitle = "";
				var SIGNATURE = status ;
				if(status.length>14){
					spantitle = status ;
					SIGNATURE = status.substring(0, 14)+"..." ;
				}
				obj.attr("itemsignature", status==""?"&nbsp;":status) ;
				obj.find(".userstatus").html(SIGNATURE==""?"&nbsp;":SIGNATURE);
				obj.find(".userstatus").attr("title", spantitle);
				stickytester("<a class='msgGotoUserUrl' userName='"+userName+"'>"+fromCnName+" 更改了状态！</a>");
				$('.msgGotoUserUrl').click(function(){_opts.gotoUser($(this).attr('userName'))});
			} else if(content.indexOf('broadcast-sharefile')>-1){
				var realUserName = userName.replace('\\40', '@') ;
				if(realUserName == $('#flyingchat').attr('username')){
					return ;
				}
				var groupName = content.substring(0, content.indexOf('broadcast-')) ;
				content = content.substring(content.indexOf('broadcast-')+19);
				var groupFlag = content.substring(0, content.indexOf("<"));
				content = content.substring(content.indexOf("<")+1);
				content = JSON.parse(content);
				content = template('chat_file_share_template', content);
				var userJID = "u" + hex_md5(groupFlag+"@broadcast."+remote.jsjac.domain);
				if (!$("#" + userJID).get(0) || $('#returnChatMain').attr("id")) {
					_opts.saveNotSeeGroupMessage(userJID, userName, groupFlag, groupName, content, date, time, true, fromCnName,fromCompanyId) ;
					return;
				}
				_opts.messageTip();
				// 向chat接收信息区域写消息
				remote.jsjac.chat.writeMessage(userJID, userName, content, date, time, fromCnName);
				if(content.indexOf("退出群组！")>-1){
					var hexname = hex_md5(groupFlag);
					var obj = $('#newmessage'+hexname) ;
					if(obj.attr("class")=="classnewmessage" && $("#class_user_list").get(0)){
						$('#groupMembers').trigger('click');
					}
					_opts.reloadGroupUsers(groupFlag) ;
				}
			} else if(content.indexOf('broadcast-apply')>-1){
				var realUserName = userName.replace('\\40', '@') ;
				if(realUserName == $('#flyingchat').attr('username')){
					return ;
				}
				var groupName = content.substring(0, content.indexOf('broadcast-')) ;
				content = content.substring(content.indexOf('broadcast-')+15);
				var groupFlag = content.substring(0, content.indexOf("<"));
				content = content.substring(content.indexOf("<")+1);
				var hexname = hex_md5(groupFlag);
				var obj = $('#newmessage'+hexname).parent() ;
				if(obj.attr("creator")!=window.userId){
					content = content.substring(0,content.indexOf('分类,您可'))+"分类。</p>" ;
				}
				var userJID = "u" + hex_md5(groupFlag+"@broadcast."+remote.jsjac.domain);
				if (!$("#" + userJID).get(0) || $('#returnChatMain').attr("id")) {
					_opts.saveNotSeeGroupMessage(userJID, userName, groupFlag, groupName, content, date, time, true, fromCnName,fromCompanyId) ;
					return;
				}
				_opts.messageTip();
				// 向chat接收信息区域
				remote.jsjac.chat.writeMessage(userJID, userName, content, date, time, fromCnName);
			} else if(content.indexOf('broadcast-')>-1){
				var realUserName = userName.replace('\\40', '@') ;
				if(realUserName == $('#flyingchat').attr('username')){
					return ;
				}
				var groupName = content.substring(0, content.indexOf('broadcast-')) ;
				content = content.substring(content.indexOf('broadcast-')+10);
				var groupFlag = content.substring(0, content.indexOf("<"));
				content = content.substring(content.indexOf("<")+1);
				var userJID = "u" + hex_md5(groupFlag+"@broadcast."+remote.jsjac.domain);
				
				if(fromCompanyId&&content){
					_opts.saveNotSeeGroupMessage(userJID, userName, groupFlag, groupName, content, date, time, true, fromCnName,fromCompanyId)
					return;
				}
				if (!$("#" + userJID).get(0) || $('#returnChatMain').attr("id")) {
					_opts.saveNotSeeGroupMessage(userJID, userName, groupFlag, groupName, content, date, time, true, fromCnName,fromCompanyId) ;
					return;
				}
				_opts.messageTip();
				// 向chat接收信息区域写消息
				remote.jsjac.chat.writeMessage(userJID, userName, content, date, time, fromCnName);
				if(content.indexOf("退出群组！")>-1){
					var hexname = hex_md5(groupFlag);
					var obj = $('#newmessage'+hexname) ;
					if(obj.attr("class")=="classnewmessage" && $("#class_user_list").get(0)){
						$('#groupMembers').trigger('click');
					}
					_opts.reloadGroupUsers(groupFlag) ;
				}
			} else if (content.indexOf('approveShareCallback-')>-1) { // 同意分享回调
				// 
				
				// 获取callbackDivId
				content = content.substring(content.indexOf('approveShareCallback-')+21);
				var callbackDivId = content.substring(0, content.indexOf("<span "));
				// 动态修改文件的页面的分享状态
				documentCenter.updateFileShareStatus(callbackDivId, 'true');
				
				content = content.substring(content.indexOf("<span "));
				// 消息发送者username
				var realUserName = remote.userAddress(userName);
				var $chatMain = $("#flyingchat .chat-main");
				var sender = $chatMain.attr("receiver");
				if(realUserName == sender){ // 刷新聊天窗口记录
					_opts.messageTip();
					// 向chat接收信息区域写消息
					remote.jsjac.chat.writeMessage(userJID, userName, content, date, time, fromCnName);
				} else { // 弹出信息提示
					var userName = userName.replace('\\40', '@') ;
					var hexname = hex_md5(userName);
					var obj = $('#newmessage'+hexname) ;
					var newmessage = obj.html();
					newmessage = newmessage*1+1 ;
					obj.css({display:'block',width:(6+(newmessage+"").length*6),left:(54-(newmessage+"").length*6)+"px"}) ;
					obj.html(newmessage) ;
					_opts.updateParentTypeCount(obj.attr("typeid"), 1) ;
					if(fromCompanyId&&content){
						_opts.postSaveNotSeeMessage({'companyId':fromCompanyId, 'username':window.userName,'from':userName, 'content':content, 'date':date, 'time':time, 'isGroup':0, 'fromCnName':fromCnName}, false);
						return;
					}
					if(content){
					_opts.postSaveNotSeeMessage({'companyId':window.companyid, 'username':window.userName,'from':userName, 'content':content, 'date':date, 'time':time, 'isGroup':0, 'fromCnName':fromCnName}, false);
					}
					stickytester("<a class='msgGotoUserUrl' userName='"+userName+"'>"+fromCnName+"："+content+"</a>");
					$('.msgGotoUserUrl').click(function(){_opts.gotoUser($(this).attr('userName'))});
					_opts.messageTip();
				}
				
			} else if (content.indexOf('shareFilesCallback-')>-1) {
				// 获取callbackDivId
				content = content.substring(content.indexOf('shareFilesCallback-')+19);
				var callbackFileIds = content.substring(0, content.indexOf("<span "));
				// 动态修改多个文件的页面的分享状态
//				documentCenter.updateFilesShareStatus(callbackFileIds, 'true');
			} else {
				var userName = userName.replace('\\40', '@') ;
				var hexname = hex_md5(userName);
				
				var newMessageCountObj = $('#newmessageval'+hexname) ;
				var newMessageCount = newMessageCountObj.val();
				newMessageCount = ((undefined == newMessageCount || '' == newMessageCount) ? 1 : newMessageCount * 1 + 1) ;
				newMessageCountObj.val(newMessageCount);
				
				var obj = $('#newmessage'+hexname) ;
				var newmessage = '' ;
				newmessage = newMessageCount > 99 ? '99+' : newMessageCount;
				obj.html(newmessage) ;
				obj.css({display:'block',width:(6+(newmessage+"").length*6),left:(54-(newmessage+"").length*6)+"px"}) ;
				
				
				_opts.updateParentTypeCount(obj.attr("typeid"), 1) ;
				if(content.indexOf("sharefile-")>-1){
					content = content.substring(10);
					content = JSON.parse(content);
					content = template('chat_file_share_template', content);
				}
				
				//如果是统一加入群组消息 刷新左侧分类锁
				if(content.indexOf("agreeClassApply-")>-1){ 
				    //分类解锁 
				    var groupFlag = content.substring(0, content.indexOf("agreeClassApply-"));
				    content = content.substring(content.indexOf("agreeClassApply-")+16);
				    var groupHexname = hex_md5(groupFlag);
				    var groupObj = $('#newmessage'+groupHexname) ;
				    if(groupObj.parent().attr('isMember')=="false"){
				    	groupObj.parent().attr('isMember', 'true');
				    	groupObj.siblings('span').remove();
				    }
				}
				
				var notSeeMsgId = '0';
				if(fromCompanyId&&content){
					_opts.postSaveNotSeeMessage({'companyId':fromCompanyId, 'username':window.userName,'from':userName, 'content':content, 'date':date, 'time':time, 'isGroup':0, 'fromCnName':fromCnName}, false);
					return;
				}
				if(content){
					notSeeMsgId = _opts.postSaveNotSeeMessage({'companyId':window.companyid, 'username':window.userName,'from':userName, 'content':content, 'date':date, 'time':time, 'isGroup':0, 'fromCnName':fromCnName}, false);
				}
				
				
				stickytester("<a class='msgGotoUserUrl' userName='"+userName+"' notseemsgid='"+notSeeMsgId+"'>"+fromCnName+"："+content+"</a>");
				$('.msgGotoUserUrl').click(function(){_opts.gotoUser($(this).attr('userName'))});
				_opts.messageTip();
			}
		},
		
		/** lujixiang 20151124 注释,页面业务联系人/群组消息总数展示 --start **/
		/**
		updateuserallnewmessagecount: function(){
			var obj = $("#userallnewmessagecount") ;
			if($("#items-users-messcount").html()=="99+" || $("#items-groups-messcount").html() == "99+" || ($("#items-users-messcount").html()*1+$("#items-groups-messcount").html()*1) >98 ){
				obj.css({display:'block',width:(6+(obj.html()+"").length*6)}) ;
			}else{
			obj.html($("#items-users-messcount").html()*1+$("#items-groups-messcount").html()*1) ;
			if(obj.html()*1>0){
				obj.css({display:'block',width:(6+(obj.html()+"").length*6)}) ;
			} else {
				if($("#items-groups-messcount").html()==""){
					obj.css({display:'block',width:'12px'}) ;
					obj.html("") ;
				} else {
					obj.css({display:'none'}) ;
				}
				obj.hide() ;
			}
			}
		},
		**/
		/** lujixiang 20151124 注释,页面业务联系人/群组消息总数展示 --start **/
		/** 用户私聊消息点击时，自动更改相关消息显示信息 **/
		updateParentTypeCount:function(typeid, value){
			
			/** lujixiang 20151120 注释，不提示分组消息数 **/
			/**
			var parentMc = $("#newMessageCount_"+typeid) ;
			if(parentMc.html() == "99+"){
				if(value < 0 || value == "NaN"){
					parentMc.html(0);
					parentMc.hide();
				}else{
					parentMc.html("99+");
				}
				parentMc.css({display:'block',width:(6+("99+").length*6)}) ;
			}else{
				var parentMcv = parentMc.html()*1+value ;
				if(parentMcv>0){
					parentMc.html(parentMcv);
					parentMc.css({display:'block',width:(6+(parentMcv+"").length*6)}) ;
				} else {
					parentMc.html(0);
					parentMc.hide() ;
				}
			}
			**/
			var obj = $("#items-users-messcount") ;
			var countObj = $("#items-users-messcount-val");
			
			if(value == "NaN"){
				
				obj.html(0) ;
				obj.hide();
				countObj.val(0);
			}
				
			var count = countObj.val();
			count = ((undefined == count || '' == count ) ? value : count * 1 + value );
			countObj.val(count);
			
			if(count < 1){
				obj.html(0) ; 
				obj.hide();
			}else {
				count = (count < 100 ? count : '99+') ;
				obj.html(count);
				obj.css({display:'block',width:(6+(obj.html()+"").length*6)}) ;
			}
		},
		updateGroupParentTypeCount:function(value){
			var groupallcount = $("#items-groups-messcount") ;
			var groupallcountValObj = $("#items-groups-messcount-val") ;
			groupallcountVal = groupallcountValObj.val();
			groupallcountVal = (undefined == groupallcountVal || '' == groupallcountVal ? value : groupallcountVal * 1 + value);
			
			if(groupallcountVal < 1){
				groupallcount.html(0);
				groupallcountValObj.val(0);
				groupallcount.hide();
			}else{
				groupallcountValObj.val(groupallcountVal);
				groupallcountVal = (groupallcountVal > 99 ? '99+' : groupallcountVal + '') ;
				groupallcount.html(groupallcountVal);
				groupallcount.css({display:'block',width:(6+(groupallcountVal+"").length*6)+"px"}) ;
			}
			// _opts.updateuserallnewmessagecount();
		},
		updateGroupParentTypeCountForOpenGroup:function(){
			var agc = 0 ;
			$(".groupList").find(".newmessage").each(function(){
				var newmessagecount = $(this).attr("newmessagecount");
				if(undefined == newmessagecount || '' == newmessagecount){
					newmessagecount = 0 ;
				}
				agc += newmessagecount * 1;
				// gc += $(this).html()*1;
			});
			
			var groupallcountValObj = $("#items-groups-messcount-val") ;
			var groupallcount = $("#items-groups-messcount") ;
			
			if(agc > 0 ){
				groupallcountValObj.val(agc);
				agc = (agc > 99 ? '99+' : agc + '');
				groupallcount.html(agc);
				groupallcount.css({display:'block',width:(6+(agc+"").length*6)+"px"}) ;
			}else{
				groupallcount.css({display:'none'}) ;
				groupallcount.html(0) ;
			}
			
			// _opts.updateuserallnewmessagecount();
		},
		/** 保存用户在线，但是没有看的消息，为了之后可能正常查看 **/
		saveNotSeeGroupMessage: function (userJID, userName, groupFlag, groupName, content, date, time, has, fromCnName, fromCompanyId) {
			// 消息可能来自其他团队
			 if(fromCompanyId&&content){
				_opts.postSaveNotSeeMessage({'companyId':fromCompanyId, 'username':window.userName,'from':userName, 'content':content, 'date':date, 'time':time, 'groupFlag':groupFlag, 'isGroup':1, 'fromCnName':fromCnName}, false) ;
				 return;
			 }
			var hexname = hex_md5(groupFlag);
			var obj = $('#newmessage'+hexname) ;
			if(obj.get(0)){
				// var newmessage = obj.html();
				
				var newmessagecounter = obj.attr("newmessagecount");
				newmessagecounter = (undefined == newmessagecounter || '' == newmessagecounter ? 1 : newmessagecounter * 1 + 1) ;
				obj.attr("newmessagecount", newmessagecounter);
				newmessagecounter = (newmessagecounter > 99 ? '99+' : newmessagecounter + '') ;
				obj.html(newmessagecounter);
				
				
				//xiewenda 没看懂这段代码意思，如果影响了请放开
				/*if(content.indexOf("@"+$("#current_user_name").html())>-1 && newmessage!="99+"){
					newmessage = newmessage*1+1 ; 
				}*/
				//这一步应该是区分发过来的消息 是分类群组下的新消息还是(联系人/群组 )创建的群组下的新消息 
				if(obj.hasClass("classnewmessage")){
					/** 被踢出群组后不显示未读的群组信息 **/
					if(obj.parent().attr('isMember')=="false" && content.indexOf("邀请您")==-1){
						return;
					}
					/** lujixiang 20150803 为分类解锁 **/
					if(obj.parent().attr('isMember')=="false" && content.indexOf("邀请您") > -1){
						obj.parent().attr('isMember', 'true');
						obj.siblings('span').remove();
					}
					
					obj.css({display:'block',width:(6+(newmessagecounter+"").length*6)}) ;
					stickytester("<a class='msgGotoClassUrl' groupFlag='"+groupFlag+"'>分类["+groupName+"]："+content+"</a>");
					$('.msgGotoGroupUrl').click(function(){_opts.gotoGroupForClass(groupFlag)});
					_opts.messageTip();
					
				} else {
					//xiewenda 没看懂这段代码意思，如果影响了请放开
					/*if(content.indexOf("@"+$("#current_user_name").html())>-1){
						_opts.updateGroupParentTypeCount(1) ;
					} else {
						_opts.updateGroupParentTypeCount(0) ;
					}*/
					//xiewenda 群组下的新消息+1
					_opts.updateGroupParentTypeCount(1);
					obj.css({display:'block',width:(6+(newmessagecounter+"").length*6),left:(48-(newmessagecounter+"").length*6)+"px"}) ;
					stickytester("<a class='msgGotoGroupUrl' groupFlag='"+groupFlag+"'>群组["+groupName+"]："+content+"</a>");
					$('.msgGotoGroupUrl').click(function(){_opts.gotoGroup(groupFlag)});
					_opts.messageTip();
				}
				
			} else {
				stickytester("<a class='msgGotoGroupUrl' groupFlag='"+groupFlag+"'>群组["+groupName+"]："+content+"</a>");
				$('.msgGotoGroupUrl').click(function(){_opts.gotoGroup(groupFlag)});
				has = false ;
			}
			if(content){
				_opts.postSaveNotSeeMessage({'companyId':window.companyid, 'username':window.userName,'from':userName, 'content':content, 'date':date, 'time':time, 'groupFlag':groupFlag, 'isGroup':1, 'fromCnName':fromCnName}, !has) ;
			}
		},
		postSaveNotSeeMessage: function(arg, call) {
			var result;
			$.ajax({
				type:'POST',
		        url : $.appClient.generateUrl({ESDefault : 'saveNotSeeMessage'},'x'),
		        data: arg,
		        async:false,//设为同步  获取未读消息ID
			    success:function(data){
			    	if(call){
			    		_opts.reloadGroupList($("#flyingchat").attr("baseurl"), $("#flyingchat").attr("companyId"), $("#flyingchat").attr("username"));
			    	}
			    	result = data;
			    }
			});
			return result;
		},
		postSaveHistoryMessage: function(arg) {
			$.ajax({
				type:'POST',
				url : $.appClient.generateUrl({ESDefault : 'saveHistoryMessage'},'x'),
				data: arg,
				success:function(data){
				}
			});
		},
		
		gotoUser: function(username){
			var receiverusername = $('#receiverusername') ;
			if(receiverusername.attr("receiver") == username){
				return;
			}
			var liObj = $("#itemuser-"+hex_md5(username)) ;
			/** wangwenshuo 20160115 离线后仍可以打开聊天窗口 */
			/*if(liObj.attr('itemstatus') == '离线'){
				return;
			}*/
			var realreceiver = liObj.attr('itemuser');
			var hexname = hex_md5(realreceiver);
			var receiver = realreceiver.replace("@", "\\40");
			receiverusername.attr("isGroup", "0");
			$.WebIM.newWebIM({
				receiver: receiver
			});
			receiverusername.css({"width":"auto"});
			receiverusername.attr("groupName",liObj.attr('itemuserfullname'));
			receiverusername.html(liObj.attr('itemuserfullname'));
			var maxWidth = ($("#mainContentRight").width()-160) ;
			if(receiverusername.width()>maxWidth){
				receiverusername.css({"width":maxWidth+"px"});
			} else {
				receiverusername.css({"width":(receiverusername.width()+5)+"px"});
			}
			
			$(".receiveruserstatus").html(liObj.attr('itemsignature')?liObj.attr('itemsignature'):"");
			$(".im-chat-list").html('<div id="msgMoreButton" page="0"><div class="msgMore">更多历史消息...</div></div>');
			$(".userpic").find(".useritempic").attr('src', liObj.attr('itemuserportrait'));
			if(receiver == 'fyBot'){
				$(".userpic-status").removeClass("user-offline").removeClass("user-online");
			} else if(liObj.attr('itemstatus') == '在线'){
				$(".userpic-status").removeClass("user-offline").addClass("user-online");
			} else {
				$(".userpic-status").removeClass("user-online").addClass("user-offline");
			}
			if(receiver == 'fyBot'){
				_opts.fyBotInitHtml() ;
			}
			if($('#newmessage'+hexname).html()!="0"){
				_opts.getOldNotSeeMessage(realreceiver, 0) ;
			} else {
				_opts.getHistoryMessage(realreceiver, 0, 1, 0) ;
			}
			var obj = $('#newmessage'+hexname) ;
			obj.css({display:'none',width:'12px',left:'42px'}) ;
			_opts.updateParentTypeCount(obj.attr("typeid"), -(obj.html()*1)) ;
			obj.html(0) ;
			$("#groupusersspanId").css("display", "none") ;
//			after(liObj) ;
//			before(liObj) ;
			return false; 
		},
		gotoGroup: function(groupFlag){
			if($('#receiverusername').attr("receiver") == groupFlag){
				return;
			}
			_opts.openGroup($('#flyingchatgrouplist div[flag="'+groupFlag+'"]'));
		},
		gotoGroupForClass: function(groupFlag){
			if($('#receiverusername').attr("receiver") == groupFlag){
				return;
			}
			_opts.openGroup($('.main-left li[flag="'+groupFlag+'"]'));
		},
		createDateHr: function (date) {
			return "<div class='datediv'><hr role='separator' aria-hidden='true'><div class='day_divider_label' aria-label='February ninth, twenty fifteen'>"+date+"</div></div>" ;
		},
		/** 获取一个发送者给自己发送的，且自己还没有看的消息，并展现 **/
		getOldNotSeeMessage: function (receiver, isGroup) {
			var url = $('#flyingchat').attr('baseurl')+'/rest/chat/getOldNotSeeMessage?callback=?';
			var data = {'companyId':$('#flyingchat').attr('companyId'), 'receiver':receiver, 'username':$('#flyingchat').attr('username'), 'isGroup':isGroup};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				var msgs = json.msgs ;
				if(msgs.length>0){
					var preDate = "" ;
					var receiverId = receiver.replace("@", "\\40");
					var userJID = "";
					if(isGroup==1){
						userJID = "u" + hex_md5(receiverId+"@broadcast."+remote.jsjac.domain);
					} else {
						userJID = "u" + hex_md5(receiverId+"@"+remote.jsjac.domain);
					}
					var receiveMessageDoc = _opts.receiveMessageDoc(userJID)[0];
					var messages = "" ;
					for(var i = 0 ; i <msgs.length ;i ++){
						// 发送消息的样式
						if(preDate != msgs[i].DATE){
							messages += _opts.createDateHr(msgs[i].DATE);
							preDate = msgs[i].DATE ;
						}
						var content = decodeURIComponent(msgs[i].CONTENT, "utf-8") ;
						content = content.replace(/%20/g," ");
						// 向接收信息区域写入发送的数据
						messages += _opts.receiveMessageTpl(isGroup==1?msgs[i].FROMUSER:receiver, "", content, false, msgs[i].DATE, msgs[i].TIME, null, isGroup==1?msgs[i].FROMCNNAME:$('#receiverusername').html());
						// 滚动条滚到底部
//						receiveMessageDoc.scrollTop($(receiveMessageDoc)[0].scrollHeight);
					}
					if(preDate != _opts.getDate()){
						messages += _opts.createDateHr(_opts.getDate());
					}
					receiveMessageDoc.append(messages);
					$('#receiveMessageDiv').perfectScrollbar('update');
				}
				$("#msgMoreButton").css("display", "block") ;
				$("#msgMoreButton").attr("skip", msgs.length) ;
			});
		},
		getHistoryMessage: function (receiver, isGroup, page, skip, keyword) {
			
			if(keyword == null){
				fyBotKeyword = 	"";
			} else {
				fyBotKeyword = keyword ;
			}
			var joindate = "" ;
			var jointime = "" ;
			if(isGroup==1){
				joindate = $('#receiverusername').attr("joindate");
				jointime = $('#receiverusername').attr("jointime");
			}
			$imChatList = $(".im-chat-list");
			/** lujixiang 20151209 以post方式获取历史消息 **/
			var url =  $.appClient.generateUrl({ESChat : 'getHistoryMessage'},'x');
			$.ajax({
				url:url,
				type: "POST",
				data:{'companyId':$('#flyingchat').attr('companyId'), 'receiver':receiver, 'username':$('#flyingchat').attr('username'), 'isGroup':isGroup, 'limit':20, page:page, skip:skip, keyword:fyBotKeyword, joindate:joindate, jointime:jointime},
				dataType:"json",
				success:function(json){
					var msgs = json.msgs ;
					var count = msgs.length ;
					if(count>0){
						var preDate = "";
						var messages = "" ;
						var msgCount = msgs.length ;
						for(var i = 0 ; i <msgCount ;i ++){
							// 发送消息的样式
							if(preDate != msgs[i].DATE){
								messages += _opts.createDateHr(msgs[i].DATE);
								preDate = msgs[i].DATE;
							}
							
							var content = decodeURIComponent(msgs[i].CONTENT, "utf-8") ;
							content = content.replace(/%20/g," ");
							var replaceTest = 'actionBtn'+window.userId;
							content = content.replaceAll(replaceTest,'actionBtn');
							var replaceTbackout = 'file-backout-show-'+window.userId;
							content = content.replaceAll(replaceTbackout,'file-backout-show');
							if(fyBotKeyword){
							content = _opts.addHighLight(content, fyBotKeyword);
						    }
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
								messages += _opts.receiveMessageTpl(msgs[i].FROMUSER, "", content, true, msgs[i].DATE, msgs[i].TIME, userpic, isGroup==1?msgs[i].FROMCNNAME:null,msgs[i].ID);
							} else {
								messages += _opts.receiveMessageTpl(msgs[i].FROMUSER, "", content, false, msgs[i].DATE, msgs[i].TIME, userpic, isGroup==1?msgs[i].FROMCNNAME:$('#receiverusername').html(),msgs[i].ID);
							}
						}
						//liuwei不要下面的横杆了
						/*if(preDate != _opts.getDate()){
							messages += _opts.createDateHr(preDate);
						}else{
							messages += _opts.createDateHr(_opts.getDate());
						}*/
						
						if(fyBotKeyword == ""){
							$(messages).insertAfter($("#msgMoreButton"));
							if(page>0){
								if(skip>0){
									$("#receiveMessageDiv").scrollTop(0);
								}else{
									$("#receiveMessageDiv").scrollTop($imChatList.height());
								}
							}
						} else {
							$(messages).insertAfter($("#msgMoreButton"));
							if(page>0){
								if(skip>0){
									$("#receiveMessageDiv").scrollTop(0);
								}else{
									$("#receiveMessageDiv").scrollTop($imChatList.height());
								}
							}
						}
						$('#receiveMessageDiv').perfectScrollbar('update');
					} else {
						if(fyBotKeyword == ""){
							if('fyBot' == $('#receiverusername').attr('receiver'))_opts.fyBotSessionStart() ;
						} else {
							if(page > 0){
								$imChatList.html('<div class="noSearchResult">没有找到符合条件的记录</div><div id="returnChatMain"><div class="returnChat">返回聊天记录界面</div></div>') ;
							}
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
					 window.globalUserStatus=0; //暂时没有好的解决办法先这样,
					 showBottomMsg("请重试",2);
				}
			});
		},
		//向openfire发送创建群组请求
		crateClassGroup: function (flag, groupusernames, groupname) {
			var arg = 'type=add_group&groups='+flag+'&username='+groupusernames;
			remote.jsjac.chat.ofuserservice(arg, false) ;
		},
//		sendmMsg: function (flag, groupusernames, groupname) {
//			_opts.reloadGroupList($("#flyingchat").attr("baseurl"), $("#flyingchat").attr("companyId"), $("#flyingchat").attr("username"));
//			var arg = 'type=add_group&groups='+flag+'&username='+groupusernames;
//			remote.jsjac.chat.ofuserservice(arg, false) ;
//			var content = groupname+"broadcast-addgroup"+flag+";"+$("#current_user_name").html()+"邀请您加入群组！" ;
//			remote.jsjac.chat.sendMessage(content, flag+"@broadcast."+remote.jsjac.domain);
//		},
		
		messageClickFun: function (type) {
			if(type.indexOf("skip_")>-1){
				/** 飞扬机器人问题跳过方法处理 **/
				_opts.fyBotNext(type.substring(5)) ;
			} else if (type == 'approveShare') { // 同意分享文件
				if (arguments.length == 2) {                
					var $action = arguments[1];
					var receiver = $action.attr("data-receiver");
					var receiverFullName = $action.attr("data-receiver-fullname");
					var callbackDivId = $action.attr("data-callback-id");
					var fileId = $action.attr("data-file-id");
					var userId = $action.attr("data-user-id");
					var fileTitle = $action.attr("data-file-title");
					var msgid = $action.parent().parent().attr("msgid");
					var test = $action.html();
					var fromusername = $action.attr("fromusername");
					//同意分享浏览\下载###
					//申请、浏览、下载，分别为三个权限
					var applyLabel = $action.attr("applyLabel");
					//系统消息处 处理文件分享  删除未读本条消息
					var notSeeMsgId = $($action).parent().parent().children(".msgGotoUserUrl").attr('notseemsgid');
					if((msgid =='undefined' || msgid == undefined) && notSeeMsgId != undefined && notSeeMsgId != '0'){
						var url = window.onlinefilePath+'/rest/chat/dropNotSeeMeesage?callback=?';
						var data = {'companyId':window.companyid,'username':window.userName,'notSeeMsgId':notSeeMsgId,'from':fromusername};
						var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
						jQuery.getJSON(url, ret.data,
								function(json) {
							var hexname = hex_md5(fromusername);
							var obj = $('#newmessage'+hexname) ;
							var newmessage = obj.html();
							newmessage = newmessage*1-1 ;
							if(newmessage>0){
								obj.css({display:'block',width:(6+(newmessage+"").length*6),left:(54-(newmessage+"").length*6)+"px"}) ;
								obj.html(newmessage) ;
							}else{
								obj.html(0) ;
								obj.hide();
							}
							_opts.updateParentTypeCount(obj.attr("typeid"),-1) ;
						});
					}
					
					if(msgid =='undefined' || msgid == undefined){
						msgid = $($action).attr('msgid');
					}
					// 判断是不是本人点击
					var postMsg = "";
					var fromUserName = $("#flyingchat").attr("username") ;
					if (receiver != fromUserName.replace("@","\\40")) {
						//判断文件是否删除 liuwei
						var url = window.onlinefilePath+'/rest/onlinefile_filesws/getFileInfo?callback=?';
						var data = {"userName":window.userName,"fileId":fileId, "companyId":window.companyid,"userId":window.userId};
						var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
						jQuery.getJSON(url, ret.data,
							function(data) {
									if(data.file.isDelete=="1"){
										showBottomMsg("对不起，文件已被删除！","3");
										return false;
									}else{
										var content  ="";
										var button = "";
										if(test == '分享'){
											content = "文件【"+fileTitle+"】分享成功";
											button = '我想'+applyLabel+'你分享的文件【<a onclick="openApplyFile(this)" fileId="'+fileId+'">'+fileTitle+'</a>】。 <div><a class="actionBtn'+userId+'" id="applyFile" msgid="'+msgid+'" applyLabel="'+applyLabel+'" data-file-title="'+fileTitle+'" data-receiver="'+receiver+'" data-callback-id="" data-file-id="'+fileId+'" data-user-id="'+userId+'" onclick="$.WebIM.messageClickFun(\'approveShare\', $(this))">取消分享</a></div>';
										//	postMsg = "您申请"+(applyLabel != "申请"?applyLabel:"")+"的文件【"+fileTitle+"】已分享成功";
											//liuwei 添加浏览下载按钮
											if("浏览" == applyLabel){
												data['accessRight'] = 1;
												postMsg = template('chat_file_share_template',data);
											}else if("下载" == applyLabel){
												data['accessRight'] = 3;
												postMsg = template('chat_file_share_template', data);
											}
										}else if(test =='取消分享'){
											content = "文件【"+fileTitle+"】取消分享成功。";
											button = '我想'+applyLabel+'你分享的文件【<a onclick="openApplyFile(this)" fileId="'+fileId+'">'+fileTitle+'</a>】。 <div><a class="actionBtn'+userId+'" id="noApplyFile" msgid="'+msgid+'" applyLabel="'+applyLabel+'" data-file-title="'+fileTitle+'" data-receiver="'+receiver+'" data-callback-id="" data-file-id="'+fileId+'" data-user-id="'+userId+'" onclick="$.WebIM.messageClickFun(\'approveShare\', $(this))">分享</a></div>';
											postMsg = "您申请"+(applyLabel != "申请"?applyLabel:"")+"的文件【"+fileTitle+"】已被取消分享。";
										}
										var name = receiver.replace('@', '\\40');
										remote.jsjac.chat.sendMessage(postMsg,name+"@"+remote.jsjac.domain);
										//改变申请状态
										documentCenter.shareFileToUserWithCallback(fileId, userId, receiver, receiverFullName, callbackDivId, content,msgid,test,button, fromUserName);
										
										//手动改按钮
										$action.parent().parent().html(button);
										
									}
								});
					}else{
						showBottomMsg("对不起，您没有此文件的分享权限。","3");
					}
				}
			} else {
				alert(type);
			}
		},
		doNotmessageClickFun: function (type) {
			if(type.indexOf("skip_")>-1){
				/** 飞扬机器人问题跳过方法处理 **/
				_opts.fyBotNext(type.substring(5)) ;
			} else if (type == 'donotapproveShare') { // 拒绝分享文件
				if (arguments.length == 2) {                
					var $action = arguments[1];
					var receiver = $action.attr("data-receiver");
					var callbackDivId = $action.attr("data-callback-id");
					var fileId = $action.attr("data-file-id");
					var userId = $action.attr("data-user-id");
					var fileTitle = $action.attr("data-file-title");
					var fromusername = $action.attr("fromusername");
					var msgid = $action.parent().parent().attr("msgid");
					var test = $action.html();
					var applyLabel = $action.attr("applyLabel");
					//系统消息处 处理文件分享  删除未读本条消息
					var notSeeMsgId = $($action).parent().parent().children(".msgGotoUserUrl").attr('notseemsgid');
					if((msgid =='undefined' || msgid == undefined) && notSeeMsgId != undefined && notSeeMsgId != '0'){
						
						var url = window.onlinefilePath+'/rest/chat/dropNotSeeMeesage?callback=?';
						var data = {'companyId':window.companyid,'username':window.userName,'notSeeMsgId':notSeeMsgId,'from':fromusername};
						var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
						jQuery.getJSON(url, ret.data,
								function(json) {
							var hexname = hex_md5(fromusername);
							var obj = $('#newmessage'+hexname) ;
							var newmessage = obj.html();
							newmessage = newmessage*1-1 ;
							if(newmessage>0){
								obj.css({display:'block',width:(6+(newmessage+"").length*6),left:(54-(newmessage+"").length*6)+"px"}) ;
								obj.html(newmessage) ;
							}else{
								obj.html(0) ;
								obj.hide();
							}
							_opts.updateParentTypeCount(obj.attr("typeid"),-1) ;
						});
					}
					
					if(msgid =='undefined' || msgid == undefined){
						msgid = $($action).attr('msgid');
					}
					// 判断是不是本人点击
					if (receiver != $("#flyingchat").attr("username").replace("@","\\40")) {
							var content = "拒绝分享文件【"+fileTitle+"】";
							var button = '申请'+applyLabel+'文件【'+fileTitle+'】的请求已被拒绝。';
							var name = fromusername.replace('@', '\\40');
							remote.jsjac.chat.sendMessage(button,name+"@"+remote.jsjac.domain);
						documentCenter.doNotShareFileToUser(fileId, userId, receiver, callbackDivId, content,msgid,test,button,fileTitle,fromusername);
						//手动改按钮
						$action.parent().parent().html(button);
					}else{
						showBottomMsg("对不起，您没有此文件的操作权限。","3");
					}
				}
			} else {
				alert(type);
			}
		},
		
		// 远程发送消息时执行函数
		messageHandler: function (user, content, date, time, fromCnName) {
			var userName = user.split("@")[0];
			var tempUser = user;
			if (~tempUser.indexOf("/")) {
				tempUser = tempUser.substr(0, tempUser.indexOf("/"));
			}
			var userJID = "u" + hex_md5(tempUser);
			//20151016 xiayongcai  addUserFlg添加用户为提示消息并存储消息内容
			// var addUserFlg = true;
			if(content.indexOf('邀请您加入新公司，请刷新页面后点击头像查看') > -1){//
				var fromCompanyId = user.substring(user.lastIndexOf("/")+11);
				userName = userName.replace('\\40','@');
				_opts.saveNotSeeMessage(userJID, userName, content, date, time, fromCnName) ;
				// addUserFlg=false;
				return;
			}
			
			//分类通过邮箱添加成员  不在团队内的系统成员
			if(content.indexOf("addtocompany-")>-1){
				var userName = userName.replace('\\40', '@') ;
				content = content.substring(13);
				window.userCompanySize = window.userCompanySize*1+1;
				reloadCompany();
				stickytester("<a class='msgAddToCompany' userName='msgAddToCompany'>"+fromCnName+"："+content+"</a>");
				$('.msgAddToCompany').click(function(){
					removeObjectByClassName('msgAddToCompany', 'msgAddToCompany');
					$("#userMessagesUl").perfectScrollbar('update');
					$("#userMenusLi .dropdown-toggle").trigger("click");
					return false;
				});
				_opts.messageTip();
				return;
			}
			//if(addUserFlg){
			/** lujixiang 20151120 如果是上线下线匿名消息,直接接受消息，否则判断消息发送者是否与接受者 **/
				//xiewenda 判断发送过来的私聊消息 是否与当前接收用户所在公司相同
			if( content.indexOf('broadcast-online') == -1 && content.indexOf('broadcast-offline') == -1
				&& user.indexOf("/") && !(user.substring(user.lastIndexOf("/")+11)==window.companyid) && content.indexOf('broadcast-sharefile') == -1){
					//这段代码不可以注释掉,注释掉会有问题
					/** lujixiang 20151118 此段注释，不要打开，当消息来自不同公司,切换其他公司时，接受推送的延迟消息  --start**/
					var fromCompanyId = user.substring(user.lastIndexOf("/")+11);
					userName = userName.replace('\\40','@');
					_opts.saveNotSeeMessage(userJID, userName, content, date, time, fromCnName,fromCompanyId) ;
					/** lujixiang 20151118 此段注释，不要打开，当消息来自不同公司,切换其他公司时，接受推送的延迟消息  --end**/
				return;
			}
			//}
			
			// 首次初始webIM
			if (!$("#" + userJID).get(0) || $('#returnChatMain').attr("id")) {
				_opts.saveNotSeeMessage(userJID, userName, content, date, time, fromCnName) ;
				return;
				// 初始IM面板；
				// _opts.initWebIM(userJID, user);
			} else if(content.indexOf('broadcast-')>-1){
				_opts.saveNotSeeMessage(userJID, userName, content, date, time, fromCnName) ;
				return;
			} else if (content.indexOf('approveShareCallback-')>-1) {
				//_opts.saveNotSeeMessage(userJID, userName, content, date, time, fromCnName) ;
				return;
			} else if (content.indexOf('shareFilesCallback-')>-1) {
				_opts.saveNotSeeMessage(userJID, userName, content, date, time, fromCnName) ;
				return;
			}
			if(content.indexOf("sharefile-")>-1){
				content = content.substring(10);
				content = JSON.parse(content);
				content = template('chat_file_share_template', content);
			}
			
			_opts.messageTip();
			// 向chat接收信息区域写消息
			remote.jsjac.chat.writeMessage(userJID, userName, content, date, time, fromCnName);
		},
		
		saveHistoryMsg: function (formuser, touser, content, date, time, isGroup, styleTpl, fileFlag) {
			if(content){
				_opts.postSaveHistoryMessage({'companyId':window.companyid, 'username':touser,'from':formuser, 'content':content, 'date':date, 'time':time, 'isGroup':isGroup, 'fromCnName':$("#current_user_name").html(), 'styleTpl':styleTpl, 'fileFlag':fileFlag}) ;
			}
		},
		
		saveHistorySimpleMsg: function (formuser, touser, content) {
			if(content){
			_opts.saveHistoryMsg(formuser, touser.replace("\\40","@"), content, _opts.getDate(), _opts.getDatetime(), 0, "","") ;
			}
		},
		
		// 发出文件申请
		applyFileClickFun: function(userName, fileId, fileName, callbackDivId) {
			var applyLabel="申请"
			if(callbackDivId !=null && callbackDivId == "isDownload"){
				applyLabel="下载";
				callbackDivId="";
			}else if(callbackDivId !=null && callbackDivId == "isLook"){
				applyLabel="浏览";
				callbackDivId="";
			}
			var $chatMain = $("#flyingchat .chat-main");
			var chatUser = remote.userAddress(userName);
			var userJID = "u" + hex_md5(chatUser);
			var receiverId = userJID;
			var sender = $chatMain.attr("sender");
			var receiver = chatUser;
			
//			var contentText = "<div  style='text-indent: 2em;'>我想下载你分享的文件【"+fileName+"】。</div>";
			var contentText = "<div  style='text-indent: 2em;'>我想"+applyLabel+"你分享的文件【<a onclick='openApplyFile(this)' fileId='"+fileId+"'>"+fileName+"</a>】。</div>";
			var content = contentText + " "
					+"<div><a class='actionBtn"+g_userId+"' id='applyFile' applyLabel='"+applyLabel+"' fromusername='"+window.userName+"' data-file-title='"+fileName+"' data-receiver='"+sender+"' data-receiver-fullname='"+window.fullName+"' data-callback-id='"+callbackDivId+"' data-file-id='"+fileId+"' data-user-id='"+g_userId+"' "
					+"onclick=\"$.WebIM.messageClickFun('approveShare', $(this))\">分享</a>"
					+" <a class='actionBtn"+g_userId+"' id='noApplyFile' applyLabel='"+applyLabel+"' fromusername='"+window.userName+"'  data-file-title='"+fileName+"' data-receiver='"+sender+"' data-receiver-fullname='"+window.fullName+"'  data-callback-id='"+callbackDivId+"' data-file-id='"+fileId+"' data-user-id='"+g_userId+"' "
					+"onclick=\"$.WebIM.doNotmessageClickFun('donotapproveShare', $(this))\">拒绝</a></div>";
			content = $.trim(content);
			content = content.replace(new RegExp("<br>", "gm"), "");
			//插入历史记录的时候返回
			var msgid="";
			/** lujixiang 20151113 将申请文件的消息发送方式改为post，修复乱码  **/
			$.ajax({
				type:'POST',
				url : $.appClient.generateUrl({ESDefault : 'saveHistoryMessageReturnID'},'x'),
				dataType : 'json',
				data: {'companyId':window.companyid,'username':userName,'from':$('#flyingchat').attr("username"),'content':content,'date':_opts.getDate(),'time':_opts.getDatetime(),'isGroup':0,'fromCnName':$("#current_user_name").html(),'styleTpl':"",'fileFlag':""},
				success:function(json){
//			jQuery.getJSON(window.onlinefilePath+'/rest/chat/saveHistoryMessageReturnID?callback=?', {'companyId':window.companyid,'username':userName,'from':$('#flyingchat').attr("username"),'content':content,'date':_opts.getDate(),'time':_opts.getDatetime(),'isGroup':0,'fromCnName':$("#current_user_name").html(),'styleTpl':"",'fileFlag':""},
//					function(json) {
					if(json.isOK){
	//					content.replace(/JUSTDOIT/g, json.msgid);
						var contentText1 = "<div style='text-indent: 2em;'>我想"+applyLabel+"你分享的文件【<a onclick='openApplyFile(this)' fileId='"+fileId+"'>"+fileName+"</a>】。</div>";
						var content1 = contentText1 + " "
								+"<div><a class='actionBtn"+g_userId+"' id='applyFile' applyLabel='"+applyLabel+"' fromusername='"+window.userName+"' MSGID='"+json.msgid+"' data-file-title='"+fileName+"' data-receiver='"+sender+"' data-receiver-fullname='"+window.fullName+"' data-callback-id='"+callbackDivId+"' data-file-id='"+fileId+"' data-user-id='"+g_userId+"' "
								+"onclick=\"$.WebIM.messageClickFun('approveShare', $(this))\">分享</a>"
								+" <a class='actionBtn"+g_userId+"' id='noApplyFile' applyLabel='"+applyLabel+"' fromusername='"+window.userName+"' MSGID='"+json.msgid+"'  data-file-title='"+fileName+"' data-receiver='"+sender+"' data-receiver-fullname='"+window.fullName+"' data-callback-id='"+callbackDivId+"' data-file-id='"+fileId+"' data-user-id='"+g_userId+"' "
								+"onclick=\"$.WebIM.doNotmessageClickFun('donotapproveShare', $(this))\">拒绝</a></div>";
						content1 = $.trim(content1);
						content1 = content1.replace(new RegExp("<br>", "gm"), "");
						// 判断是否在聊天窗口
						if (receiver == $chatMain.attr("receiver")) {
							//  在自己接收区域写提示信息
							//_opts.writeReceiveMessage(receiverId, sender, content, true);
						} else {
							showBottomMsg("发送申请成功！", 1);
						}
						var subfileName = fileName;
						if(subfileName.length>=30){
							subfileName = subfileName.substring(0, 30);
							subfileName+='...';
						}
						var contentText2 = "<div style='text-indent: 2em;'>我想"+applyLabel+"你分享的文件【<a onclick='openApplyFile(this)' fileId='"+fileId+"'>"+subfileName+"</a>】。</div>";
						var content2 = contentText2 + " "
								+"<div><a class='actionBtn"+g_userId+"' id='applyFile' applyLabel='"+applyLabel+"' fromusername='"+window.userName+"' MSGID='"+json.msgid+"' data-file-title='"+fileName+"' data-receiver='"+sender+"' data-receiver-fullname='"+window.fullName+"' data-callback-id='"+callbackDivId+"' data-file-id='"+fileId+"' data-user-id='"+g_userId+"' "
								+"onclick=\"$.WebIM.messageClickFun('approveShare', $(this))\">分享</a>"
								+" <a class='actionBtn"+g_userId+"' id='noApplyFile' applyLabel='"+applyLabel+"'  fromusername='"+window.userName+"' MSGID='"+json.msgid+"'  data-file-title='"+fileName+"' data-receiver='"+sender+"' data-receiver-fullname='"+window.fullName+"' data-callback-id='"+callbackDivId+"' data-file-id='"+fileId+"' data-user-id='"+g_userId+"' "
								+"onclick=\"$.WebIM.doNotmessageClickFun('donotapproveShare', $(this))\">拒绝</a></div>";
						content2 = $.trim(content2);
						content2 = content2.replace(new RegExp("<br>", "gm"), "");
						// 向对方发送申请信息
	//					remote.jsjac.chat.sendMessage(content2, receiver);
						var name = userName.replace('@', '\\40');
						remote.jsjac.chat.sendMessage(content2,name+"@"+remote.jsjac.domain);
					}
				}
			});
			
//			_opts.saveHistoryMsg($('#flyingchat').attr("username"), userName, contentText+content, _opts.getDate(), _opts.getDatetime(), 0, "", "") ;
			
		},
		// 同意分享回调函数
		// 
		
		approveShareCallback: function(receiverName, callbackDivId, content) {
			receiver = remote.userAddress(receiverName);
			content = content.replace(new RegExp("<br>", "gm"), "");
			
			var $chatMain = $("#flyingchat .chat-main");
			// 判断是否在聊天窗口
			if (receiver == $chatMain.attr("receiver")) {
				var receiverId = $chatMain.attr("id");
				var sender = $chatMain.attr("sender");
				//  在自己接收区域写提示信息
				//_opts.writeReceiveMessage(receiverId, sender, content, true);
			} else {
				showBottomMsg("分享文件成功！", 1);
			}
			var touser = receiverName;
			_opts.saveHistoryMsg($('#flyingchat').attr("username"), touser.replace("\\40","@"), content, _opts.getDate(), _opts.getDatetime(), 0, "") ;
			
			//content = ["approveShareCallback-", callbackDivId, content].join("");
			//remote.jsjac.chat.sendMessage(content, receiver);
		},
		/**
		 * 主动分享文件（通过聊天框分享）
		 */
		shareFilesCallback: function(receiver, fileIds) {
			receiver = remote.userAddress(receiver);
			var content = "";
			content = ["shareFilesCallback-", fileIds, content].join("");
			remote.jsjac.chat.sendMessage(content, receiver);
		},
		// 消息提示
		messageTip: function () {
				window.focus();
				if(document.title.indexOf("你来了新消息，请查收！") == -1){
					document.title = document.title+"-你来了新消息，请查收！";
				}
				var borswer = window.navigator.userAgent.toLowerCase();
				if ( borswer.indexOf( "ie" ) >= 0 ){
					document.getElementById('flyingchatsound').innerHTML = "<EMBED src='apps/onlinefile/templates/ESDefault/sound/ding.wav' hidden='true' type='audio/mpeg' controls='console' loop='false' autostart='true'/>";
				} else {
					document.getElementById('flyingchatsound').innerHTML = "<audio id='flyingchatsoundPlay' src='apps/onlinefile/templates/ESDefault/sound/ding.wav' hidden='true'>";
					document.getElementById("flyingchatsoundPlay").play();
				}
		},
		// 在输入框@文件的时候，显示
		atFileTitle: function(title) {
			var $doc = $("#flyingchatinput");
			if ($doc.val().indexOf(title) < 0) {
				$doc.val($doc.val() + "#"+title+"# ");
			}
			$doc.focus();
		},
		// 查找#文件#对应的id, 获取文件id字符串集合
		findAtFileInContent: function(content) {
			var shareList = documentCenter.shareFileList;
			var array = shareList.split(";");
			var ids = "";
			var titles = "";
			for (var i = 0; i < array.length-1; i++) {
				var file = array[i];
				var fileId = file.substring(0, file.indexOf(":"));
				var fileTitle = file.substring(file.indexOf(":") + 1);
				if (content.indexOf("#"+fileTitle+"#") != -1) {
					ids += fileId + ",";
					titles += fileTitle + "&amp;";
				}
			}
			ids = ids.substring(0, ids.length - 1);
			if (titles != "") {
				titles = titles.substring(0, titles.length - 5);
			}
			// 如果是聊天对象是个人，表示把文件分享给他
			if($('#receiverusername').attr("isGroup") == "0"){
				if (ids != "") {
					var receiver = $("#receiverusername").attr("receiver");
					content += "<br><span class='tips'>文件分享</span>";
					documentCenter.shareFilesToUserWithCallback(ids, receiver, titles);
				}
			}
			// 清空分享列表记录
			documentCenter.shareFileList = "";
			return content;
		},
		getHistoryMessage4File: function (receiver, fileFlag, page, skip) {
			skip = skip==null?"0":skip ;
			
			var url = $('#flyingchat').attr('baseurl')+'/rest/chat/getHistoryMessage4File?callback=?';
			var data = {'username':window.userName,'companyId':$('#flyingchat').attr('companyId'), 'receiver':receiver, 'fileFlag':fileFlag, 'limit':20, page:page, skip:skip};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				var msgs = json.msgs ;
				var count = msgs.length ;
				$imChatList = $(".im-chat-list");
				if(count>0){
					var preDate = "" ;
					var messages = "" ;
					var msgCount = msgs.length ;
					for(var i = 0 ; i <msgCount ;i ++){
						// 发送消息的样式
						if(preDate != msgs[i].DATE){
							messages += _opts.createDateHr(msgs[i].DATE);
							preDate = msgs[i].DATE ;
						}
						var content = decodeURIComponent(msgs[i].CONTENT, "utf-8") ;
						content = content.replace(/%20/g," ");
						if(i==msgCount-1){
							$('#receiverusername').attr("fybotstep", "4") ;
						}
						// 用户头像
						var userpic = null ;
						// 向接收信息区域写入发送的数据
						if(msgs[i].FROMUSER == $('#flyingchat').attr('username')){
							messages += _opts.receiveMessageTpl(msgs[i].FROMCNNAME, "", content, true, msgs[i].DATE,  msgs[i].TIME, userpic, msgs[i].FROMCNNAME);
						} else {
							messages += _opts.receiveMessageTpl(msgs[i].FROMCNNAME, "", content, false, msgs[i].DATE, msgs[i].TIME, userpic, msgs[i].FROMCNNAME);
						}
					}
					if(preDate != _opts.getDate()){
						messages += _opts.createDateHr(_opts.getDate());
					}
					$imChatList.html('<div id="msgMoreButton" page="0"><div class="msgMore">更多历史消息...</div></div>');
					$(messages).insertAfter($("#msgMoreButton"));
					if(page == 1){
						$imChatList.append('<div id="returnChatMain"><div class="returnChat">返回聊天记录界面</div></div>') ;
					}
					if(page==1){
						$("#receiveMessageDiv").scrollTop($imChatList.height());
					}
					$('#receiveMessageDiv').perfectScrollbar('update');
				} else {
					if(page == 1){
						$imChatList.html('<div style="float:center;margin-top:10px;margin-bottom:10px;color:rgb(144, 149, 154);">暂时没有聊天记录</div><div id="returnChatMain"><div class="returnChat">返回聊天记录界面</div></div>') ;
					}
					$("#receiveMessageDiv").scrollTop(0);
					$('#receiveMessageDiv').perfectScrollbar('update');
				}
				$("#msgMoreButton").attr("page", page) ;
				if(count<20){
					$("#msgMoreButton").css("display", "none") ;
				}else{
					$("#msgMoreButton").css("display", "block") ;
				}
			});
		},
		/**
		 * 显示群组里针对某个文件的聊天记录
		 */
		showChatFileLog: function(fileFlag) {
			var page = 1;
			_opts.getHistoryMessage4File($('#receiverusername').attr("receiver"), fileFlag, page) ;
		},
		/**
		 * 发送分享文件到聊天框
		 * actionType: send
		 * 			   发送到
		 * accessRight 权限配置  1浏览   3浏览+下载
		 */
		sendShareFile: function(folderId, fileId, accessRight) {
			var $receiverusername = $("#receiverusername");
			var receiverId = $receiverusername.attr("receiver");
			var isGroup = $receiverusername.attr("isGroup");
			var actionType = "shareTo";
			/* 添加判断  是我的文档 还是分类 */
			var companyId = documentCenter.getCompanyId();
			var isMyDocument = documentCenter.isMyDocument();
			
			var rst="";
			//accessRight==0  告诉被分享者分享者有此文件，但没有分享任何权限  （显摆一下）
			if(accessRight!=0){
				rst = documentCenter.shareFileToGroupOrUser(fileId, receiverId, isGroup,accessRight, $receiverusername.html());
			}
			if(rst=="hasShare"){
				showBottomMsg("文件已经分享过！", 2);
				return;
			}else if(rst=="failure"){
				showBottomMsg("分享失败！", 3);
				return;
			}else{
				showBottomMsg("分享成功！", 1);
				var url = $("#flyingchat").attr("baseurl")+'/rest/onlinefile_filesws/getFileInfo?callback=?';
				var data = {"userName":window.userName,"companyId":companyId, "classId":folderId, "fileId":fileId, "userId":g_userId};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
							json["title"] = _opts.getActionTitle("send");
							json["backout"] = "true"; // 显示撤销分享按钮
							json["userId"] = g_userId ;
							/* 添加myDocument、userId 用于个人间的文件分享*/
							json["myDocument"] = isMyDocument?"myDocument":"" ;
							json["accessRight"] = accessRight ;
							json["actionType"] = actionType;
							json["shareToUserName"] = $('#receiverusername').attr("groupName");
							var contenthtml = template('chat_file_share_template', json);
							//显示撤销按钮
							var replaceTbackout = 'file-backout-show-'+g_userId;
							var writeContenthtml = contenthtml.replaceAll(replaceTbackout,'file-backout-show');
							var shareArray = {} ;
							shareArray.file = {} ;
							shareArray.creator = {} ;
							shareArray.file.fileName = json.file.fileName ;
							shareArray.file.type = json.file.type ;
							shareArray.file.id = json.file.id ;
							shareArray.file.isFile = json.file.isFile ;
							shareArray.file.classId = json.file.classId ;
							shareArray.file.userId = json.file.userId ;
							shareArray.file.userName = json.file.userName ;
							shareArray.file.fileId = json.file.fileId ;
							shareArray.file.praiseCount = json.file.praiseCount ;
							shareArray.file.idSeq = json.file.idSeq ;
							shareArray.file.className = json.file.className ;
							shareArray.file.version = json.file.version ;
							shareArray.file.sizeNum = json.file.sizeNum ;
							shareArray.file.groupFlag = json.file.groupFlag;
							shareArray.creator.id = json.creator.id ;
							shareArray.creator.fullName = json.creator.fullName ;
							shareArray.version = json.version ;
							shareArray.title = json.title;
							shareArray.myDocument = json.myDocument;
							shareArray.accessRight = json.accessRight;
							var $chatMain = $("#flyingchat .chat-main");
							var sender = $chatMain.attr("sender");
							var chatMainId = $chatMain.attr("id");
							var receiver = $chatMain.attr("receiver");
							var receiverNow = $chatMain.attr("receiver");
							var groupFlag = json.file.groupFlag;
							// 接收区域写消息
							_opts.writeReceiveMessage(chatMainId, sender, writeContenthtml, true);
							var content = JSON.stringify(shareArray) ;
							//liuwei2016229
							var jsonStr = {file:{fileName:json.file.fileName,type:json.file.type,version:json.file.version,isFile:"1"},creator:{}};
							if(isGroup == "1"){
								_opts.sendFileActionMsg(groupFlag, jsonStr, actionType); 
								var msg = [$receiverusername.attr("groupName"), "broadcast-sharefile", $receiverusername.attr("groupFlag"), "<", content ].join("");
								remote.jsjac.chat.sendMessage(msg, receiver);
							} else {
								_opts.sendFileActionMsg(groupFlag, jsonStr, actionType);
								remote.jsjac.chat.sendMessage("sharefile-"+content, receiver);
							}
							if(_opts.groupToUsers.length>0){
								var users = _opts.groupToUsers.join(",");
								var url = $("#flyingchat").attr("baseurl")+'/rest/chat/saveGroupcallOver?callback=?';
								var data = {'companyId':$("#flyingchat").attr("companyId"), 'groupflag':$receiverusername.attr("groupFlag"), "users":users,'username':window.userName};
								var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
								jQuery.getJSON(url, ret.data,
										function(json) {
								});
							}
							_opts.saveHistoryMsg($('#flyingchat').attr("username"), receiverId, contenthtml, _opts.getDate(), _opts.getDatetime(), isGroup, "", "") ;
						}
				);
			}
			
		},
		/**    发送回复消息给指定人    **/
		sendReplyFileComment: function(fileId,receiverUserName,replyContent,receiverId,fromUserName,fromUserFullName,receiveUserFullName,parentId) {
			var url = $("#flyingchat").attr("baseurl")+'/rest/onlinefile_filesws/getFileInfoById?callback=?';
			var data = { "fileId":fileId,"companyId":g_companyId,"parentId":parentId,"fromUserFullName":encodeURI(fromUserFullName, "utf-8"),"fromUserId":window.userId};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
			        function(json) {
						json["title"] = "<font style = 'color:rgb(48, 160, 201);'>"+fromUserFullName+"</font>回复<font style = 'color:rgb(48, 160, 201);'>"+receiveUserFullName+"</font>：<font style = 'color:rgb(51, 51, 51);'>"+replyContent+"</font>";
						var contenthtml = template('chat_file_comment_template', json);
						/**   如果用户自己@自己那么不给自己发消息了  **/
						if(window.userName != receiverUserName){
							_opts.saveHistoryMsg(fromUserName, receiverUserName, contenthtml, _opts.getDate(), _opts.getDatetime(), "0", "", "") ;
							remote.jsjac.chat.sendMessage(contenthtml,receiverUserName.replace("@", "\\40")+"@"+remote.jsjac.domain);
						}
					}
			);
			
		},
		
		/**
		 * 发送文件操作到聊天框 没有操作的
		 * actionType: cancelShare/deleteFile/deleteFolder
		 * 			   取消分享/删除文件/删除文件夹
		 */
		sendFileMsgNotAction:function(groupFlag, json, actionType) {
			_opts.sendFileActionMsg(groupFlag, json, actionType);
		},
		
		/**
		 * 发送文件操作到聊天框 有操作的
		 * actionType: upload/shareTo/addFolder/renameFolder
		 * 			   上传/分享到/添加文件夹/重命名文件夹
		 */
		sendFileMsg:function(fileId, actionType) {
			var url = $("#flyingchat").attr("baseurl")+'/rest/onlinefile_filesws/getFileInfo?callback=?';
			var data = {"userName":window.userName,"companyId":g_companyId, "classId":"", "fileId":fileId, "userId":g_userId};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
						_opts.sendFileActionMsg(json.file.groupFlag, json, actionType);
					}
			);
		},
		
		/**
		 * 发送对文件操作的群组聊天消息提示
		 * receiverId: 群组的FLAG，例如 c0g1428326458543
		 * actionType: upload/shareTo/addFolder/renameFolder /  cancelShare/deleteFile/deleteFolder
		 * 			   上传/分享到/添加文件夹/重命名文件夹 / 取消分享/删除文件/删除文件夹
		 *  
		 */
		sendFileActionMsg:function(groupFlag, json, actionType) {
			var fileId = json.file.id;
			var receiver = groupFlag+"@broadcast."+remote.jsjac.domain;
			var isGroup = "1";
			json["title"] = _opts.getActionTitle(actionType);
			json["actionType"] = actionType;
			json["shareToUserName"] = $('#receiverusername').attr("groupName");
			var contenthtml = template('chat_file_share_template', json);
			var shareArray = {} ;
			shareArray.file = {} ;
			shareArray.creator = {} ;
			
			shareArray.file.fileName = json.file.fileName ;
			shareArray.file.type = json.file.type ;
			shareArray.file.id = json.file.id ;
			shareArray.file.isFile = json.file.isFile ;
			shareArray.file.classId = json.file.classId ;
			shareArray.file.userId = json.file.userId ;
			shareArray.file.userName = json.file.userName ;
			shareArray.file.fileId = json.file.fileId ;
			shareArray.file.idSeq = json.file.idSeq ;
			shareArray.file.className = json.file.className ;
			shareArray.file.version = json.file.version ;
			shareArray.creator.id = json.creator.id ;
			shareArray.creator.fullName = json.creator.fullName ;
			shareArray.version = json.version ;
			var $chatMain = $("#flyingchat .chat-main");
			var sender = $chatMain.attr("sender");
			var chatMainId = $chatMain.attr("id");
			var receiverNow = $chatMain.attr("receiver");
			if (receiver == receiverNow) {
				// 接收区域写消息
				_opts.writeReceiveMessage(chatMainId, sender, contenthtml, true);
			}
			var tempFileName = shareArray.file.fileName ;
			shareArray.file.fileName = tempFileName.length > 20 ? tempFileName.substring(0, 20) + '...' : tempFileName ;
			var content = JSON.stringify(shareArray) ;
			if(isGroup == "1"){
				var msg = [json.title, "broadcast-sharefile", groupFlag, "<", content ].join("");
				remote.jsjac.chat.sendMessage(msg, receiver);
			} else {
				remote.jsjac.chat.sendMessage("sharefile-"+content, receiver);
			}
			if(_opts.groupToUsers.length>0){
				var users = _opts.groupToUsers.join(",");
				
				var url = $("#flyingchat").attr("baseurl")+'/rest/chat/saveGroupcallOver?callback=?';
				var data = {'companyId':$("#flyingchat").attr("companyId"), 'groupflag':groupFlag, "users":users,'username':window.userName};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
				});
			}
			_opts.saveHistoryMsg($('#flyingchat').attr("username"), groupFlag, contenthtml, _opts.getDate(), _opts.getDatetime(), isGroup, "", "") ;
		},
		
		/**
		 * 获取动作的标题
		 * actionType: send/upload/shareTo/cancelShare/deleteFile/addFolder/renameFolder/deleteFolder
		 * 			   发送/上传/分享到/取消分享/删除文件/添加文件夹/重命名文件夹/删除文件夹
		 */
		getActionTitle:function(actionType) {
			var title = "";
			if (actionType == "send") {
				title = "发送了文件";
			} else if (actionType == "upload") {
				title = "上传了文件";
			} else if (actionType == "shareTo") {
				title = "分享了文件";
			} else if (actionType == "cancelShare") {
				title = "设为了私密文件";
			} else if (actionType == "deleteFile") {
				title = "删除了文件";
			} else if (actionType == "addFolder") {
				title = "添加了文件夹";
			} else if (actionType == "renameFolder") {
				title = "重命名了文件夹";
			} else if (actionType == "deleteFolder") {
				title = "删除了文件夹";
			} else if (actionType == "restoreFile") {
				title = "恢复了文件";
			} else if (actionType == "restoreFolder") {
				title = "恢复了文件夹";
			}else if (actionType == "companyShare") {
				title = "设置公司级公开";
			}else if (actionType == "classShare") {
				title = "设置分类下公开";
			}else if (actionType == "myselfShare") {
				title = "设置私密";
			}
			return title;
		},
		
		
		//申请加入分组
	applyIntoGroup: function(userName, userid,receiverId,callbackDivId) {
		//20151106 xiayongcai 通过表示获取对象参数
		var $obj =$(".main-left").find("li[flag='"+receiverId+"']");
		//var $obj = $('.main-left li.active');
		var groupname = $obj.attr("groupname");
		var groupid = $obj.attr('data-group-id');
		var groupflag = $obj.attr('flag');
		var creator = $obj.attr('creator');
		
		var $chatMain = $("#flyingchat .chat-main");
		var chatUser = remote.userAddress(userName);
		var userJID = "u" + hex_md5(chatUser);
		var receiverId = userJID;
		var sender = $chatMain.attr("sender");
		var receiver = chatUser;
		
		var contentText = "<div  style='text-indent: 2em;'>我想加入您的分类【"+groupname+"】。</div>";
		var content = contentText + " "
				+"<div style='float:right'><a class='actionBtn"+g_userId+"' id='agreeThisApply' username='"+window.userName+"' data-file-title='"+groupname+"' groupid='"+groupid+"' data-receiver='"+sender+"' creator='"+creator+"' data-callback-id='"+callbackDivId+"' groupname='"+groupname+"' flag='"+groupflag+"' fullname='"+window.fullName+"' userid='"+g_userId+"' "
				+"onclick=\"$.WebIM.noticeApplyerIntoGroup('agreeThisApply', $(this))\">同意</a>"
				+" <a class='actionBtn"+g_userId+"' id='passThisApply' username='"+window.userName+"'  groupid='"+groupid+"'  data-file-title='"+groupname+"'  creator='"+creator+"' data-receiver='"+sender+"' data-callback-id='"+callbackDivId+"' groupname='"+groupname+"' flag='"+groupflag+"' fullname='"+window.fullName+"' userid='"+g_userId+"' "
				+"onclick=\"$.WebIM.noticeApplyerIntoGroup('passThisApply', $(this))\">拒绝</a></div>";
		content = $.trim(content);
		content = content.replace(new RegExp("<br>", "gm"), "");
		//插入历史记录的时候返回
		var msgid="";
		/** lujixiang 20151113 将申请文件的消息发送方式改为post，修复乱码  **/
		$.ajax({
			type:'POST',
			url : $.appClient.generateUrl({ESDefault : 'saveHistoryMessageReturnID'},'x'),
			dataType : 'json',
			data: {'companyId':window.companyid,'username':userName,'from':$('#flyingchat').attr("username"),'content':content,'date':_opts.getDate(),'time':_opts.getDatetime(),'isGroup':0,'fromCnName':$("#current_user_name").html(),'styleTpl':"",'fileFlag':""},
			success:function(json){
//		jQuery.getJSON(window.onlinefilePath+'/rest/chat/saveHistoryMessageReturnID?callback=?', {'companyId':window.companyid,'username':userName,'from':$('#flyingchat').attr("username"),'content':content,'date':_opts.getDate(),'time':_opts.getDatetime(),'isGroup':0,'fromCnName':$("#current_user_name").html(),'styleTpl':"",'fileFlag':""},
//				function(json) {
					if(json.isOK){
						var contentText1 = "<div  style='text-indent: 2em;'>我想加入您的分类【"+groupname+"】。</div>";
						var content1 = contentText1 + " "
							+"<div style='float:right'><a class='actionBtn"+g_userId+"' id='agreeThisApply' MSGID='"+json.msgid+"' username='"+window.userName+"'  data-file-title='"+groupname+"' groupid='"+groupid+"'  creator='"+creator+"' data-receiver='"+sender+"' data-callback-id='"+callbackDivId+"' groupname='"+groupname+"' flag='"+groupflag+"' fullname='"+window.fullName+"' userid='"+g_userId+"' "
							+"onclick=\"$.WebIM.noticeApplyerIntoGroup('agreeThisApply', $(this))\">同意</a>"
							+" <a class='actionBtn"+g_userId+"' id='passThisApply' MSGID='"+json.msgid+"'  username='"+window.userName+"'  groupid='"+groupid+"' creator='"+creator+"'  data-file-title='"+groupname+"' data-receiver='"+sender+"' data-callback-id='"+callbackDivId+"' groupname='"+groupname+"' flag='"+groupflag+"' fullname='"+window.fullName+"' userid='"+g_userId+"' "
							+"onclick=\"$.WebIM.noticeApplyerIntoGroup('passThisApply', $(this))\">拒绝</a></div>";
						content1 = $.trim(content1);
						content1 = content1.replace(new RegExp("<br>", "gm"), "");
						// 判断是否在聊天窗口
						if (receiver == $chatMain.attr("receiver")) {
							//  在自己接收区域写提示信息
						} else {
							showBottomMsg("发送申请成功！", 1);
						}
						var contentText2 = "<div  style='text-indent: 2em;'>我想加入您的分类【"+groupname+"】。</div>";
						var content2 = contentText2 + " "
							+"<div style='float:right'><a class='actionBtn"+g_userId+"' id='agreeThisApply' MSGID='"+json.msgid+"' username='"+window.userName+"'  data-file-title='"+groupname+"' groupid='"+groupid+"'  creator='"+creator+"' data-receiver='"+sender+"' data-callback-id='"+callbackDivId+"' groupname='"+groupname+"' flag='"+groupflag+"' fullname='"+window.fullName+"' userid='"+g_userId+"' "
							+"onclick=\"$.WebIM.noticeApplyerIntoGroup('agreeThisApply', $(this))\">同意</a>"
							+" <a class='actionBtn"+g_userId+"' id='passThisApply' MSGID='"+json.msgid+"' username='"+window.userName+"'  groupid='"+groupid+"' creator='"+creator+"'  data-file-title='"+groupname+"' data-receiver='"+sender+"' data-callback-id='"+callbackDivId+"' groupname='"+groupname+"' flag='"+groupflag+"' fullname='"+window.fullName+"' userid='"+g_userId+"' "
							+"onclick=\"$.WebIM.noticeApplyerIntoGroup('passThisApply', $(this))\">拒绝</a></div>";
						content2 = $.trim(content2);
						content2 = content2.replace(new RegExp("<br>", "gm"), "");
						// 向对方发送申请信息
						var name = userName.replace('@', '\\40');
						remote.jsjac.chat.sendMessage(content2,name+"@"+remote.jsjac.domain);
					}
			}
		});
	},
	//同意申请者加入分组后通知申请者
	noticeApplyerIntoGroup: function(type) {
		var button = null;
		var $action = arguments[1];
		var thisplace = $action;
		var creator = $action.attr('creator');
		var groupname =  $action.attr("groupname");
		var userid = $action.attr('userid');
		var groupid = $action.attr('groupid');
		var groupFlag = $action.attr('flag');
		var username =$action.attr('username');
		var fullname = $action.attr('fullname');
		var addgroupuserids = userid;
		var msgid = $action.parent().parent().attr("msgid");
		
		//wangwenshuo 20150824 使用消息提示处理  并且未读消息id存在  删除未读消息
		var notSeeMsgId = $($action).parent().parent().children(".msgGotoUserUrl").attr('notseemsgid');
		if((msgid =='undefined' || msgid == undefined) && notSeeMsgId != undefined && notSeeMsgId != '0'){
			
			var url = window.onlinefilePath+'/rest/chat/dropNotSeeMeesage?callback=?';
			var data = {'companyId':window.companyid,'username':window.userName,'notSeeMsgId':notSeeMsgId,'from':username};
			var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
			jQuery.getJSON(url, ret.data,
					function(json) {
				var hexname = hex_md5(username);
				var obj = $('#newmessage'+hexname) ;
				var newmessage = obj.html();
				newmessage = newmessage*1-1 ;
				if(newmessage>0){
					obj.css({display:'block',width:(6+(newmessage+"").length*6),left:(54-(newmessage+"").length*6)+"px"}) ;
					obj.html(newmessage) ;
				}else{
					obj.html(0) ;
					obj.hide();
				}
				_opts.updateParentTypeCount(obj.attr("typeid"),-1) ;
			});
		}
		
		if (type == 'agreeThisApply') { // 同意分享文件
			if (arguments.length == 2) { 
				if(creator != window.userId){
					showBottomMsg("您不是分类管理员，无法进行该操作！", 2);
					return false;
				}
				if(msgid =='undefined' || msgid == undefined){
					msgid = $($action).attr('msgid');
				}
				var groupContent = "同意加入";
				button = fullname+' 通过申请加入了【'+groupname+'】分类';
				
				//判断该用户是否已是该小组成员
				var url = window.onlinefilePath+'/rest/chat/getGroupUserByGroupId?callback=?';
				var data = {'companyId':window.companyid,'username':window.userName,'groupid':groupid,'groupFlag':groupFlag,'applyuserid':userid};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
					if(json.all>0){
						if(!json.isOk){
							//判断该用户是否已是该小组成员
							var deletegroupuserids ="";
							
							var url = window.onlinefilePath+'/rest/chat/resetGroupByApply?callback=?';
							var data = {'companyId':window.companyid, 'username':username, 'addgroupuserids':addgroupuserids, 'deletegroupuserids':deletegroupuserids,groupremark:"", 'groupname':encodeURI($.trim(groupname), "utf-8"), groupid:groupid, groupflag:groupFlag, manageruserid:window.userId, changeusers:true, changeitems:false, "fullname":encodeURI( window.fullName, "utf-8"),'isApplied':'true','msgid':msgid,'button':button};
							var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
							jQuery.getJSON(url, ret.data,
									function(json) {
								if(json.isOk){
									var arg = 'type=reset_group&groups='+groupFlag+'&addgroupusernames='+username.replace('@', '\\40')+',';
									$('#groupMembers').trigger('click');
									remote.jsjac.chat.ofuserservice(arg, false) ;
									showBottomMsg("您已同意"+fullname+"加入本分类！", 1);
									//同意之后给用户发送消息通知
									var dcontent = groupFlag+"agreeClassApply-"+window.fullName+"接受了您加入该分类的请求,您现在可以在【"+groupname+"】分类发言了;)！" ;
									var name = username.replace('@', '\\40')
									remote.jsjac.chat.sendMessage(dcontent,name+"@"+remote.jsjac.domain);
									$(thisplace).parent().parent().html('您已同意 【'+fullname+'】加入分类');
									
									var content = [groupname, "broadcast-", groupFlag, "<", "同意 【"+fullname+"】加入分类【"+groupname+"】！" ].join("");
									remote.jsjac.chat.sendMessage(content, groupFlag+"@broadcast."+remote.jsjac.domain);
									$.WebIM.reloadGroupUsers(groupFlag) ;
								} else {
									showBottomMsg("添加分类成员失败",3);
								}
							});
						}else{
							showBottomMsg("该用户已在分类中!", 2);
							$(thisplace).parent().parent().html("该用户已在分类中!");
							var content = fullname+' 申请加入了【'+groupname+'】分类(该申请失效)';
							
							var url = window.onlinefilePath+'/rest/chat/modifyChatLog?callback=?';
							var data = {'companyId':window.companyid,'msgid':msgid,'groupid':groupid,'button':content};
							var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
							jQuery.getJSON(url, ret.data,function(json) {});
						}
					} else {
						showBottomMsg("添加分类成员失败",2);
					}
				});
			}
		}else if(type=='passThisApply'){
			if (arguments.length == 2) {
				if(creator != window.userId){
					showBottomMsg("您不是分类管理员，无法进行该操作！", 2);
					return false;
				}
				if(msgid =='undefined'  || msgid == undefined){
					msgid = $($action).attr('msgid');
				}
				var groupContent = "拒绝申请";
				button = fullname+' 通过申请加入了【'+groupname+'】分类被拒绝';
				
				var url = window.onlinefilePath+'/rest/chat/getGroupUserByGroupId?callback=?';
				var data = {'companyId':window.companyid,'username':window.userName,'groupid':groupid,'groupFlag':groupFlag,'applyuserid':userid};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
//					if(json.all>0){
						if(!json.isOk){
							var url = window.onlinefilePath+'/rest/chat/modifyChatLog?callback=?';
							var data = {'companyId':window.companyid,'msgid':msgid,'groupid':groupid,'button':button};
							var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
							jQuery.getJSON(url, ret.data,
									function(json) {
								if(json.isOk){
									//不在组中，回复说拒绝
									var dcontent = window.fullName+"拒绝了您加入【"+groupname+"】分类的申请！" ;
									var name = username.replace('@', '\\40')
									remote.jsjac.chat.sendMessage(dcontent,name+"@"+remote.jsjac.domain);
									showBottomMsg("您已拒绝该成员加入分类!", 1);
									$(thisplace).parent().parent().html('您已拒绝【'+fullname+'】加入【'+groupname+'】分类');
								}
							});
						}else{
							showBottomMsg("该用户已在分类中，请在成员管理中将其请出团队!", 2);
							$(thisplace).parent().parent().html("该用户已在分类中，请在成员管理中将其请出团队!");
							var content = fullname+' 申请加入了【'+groupname+'】分类(该申请失效)';
							
							var url = window.onlinefilePath+'/rest/chat/modifyChatLog?callback=?';
							var data = {'companyId':window.companyid,'msgid':msgid,'groupid':groupid,'button':content};
							var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
							jQuery.getJSON(url, ret.data,function(json) {});
						}
				});
			}
		}
		
	}
};
	
	// 初始化远程聊天程序相关方法
	var initRemoteIM = function (settings) {
		
		// 初始化远程消息
		remote.jsjac.chat.init();
		
		// 设置客户端写入信息方法
		remote.jsjac.chat.writeReceiveMessage = settings.writeReceiveMessage;
		
		// 注册事件
		$(window).bind({
		 	unload: remote.jsjac.chat.unloadHandler,
//		 	error: remote.jsjac.chat.errorHandler,
		 	beforeunload: remote.jsjac.chat.logout
		});
	}
	
	$.extend({
		WebIM: function (opts) {
			opts = opts || {};
			// 覆盖默认配置
			defaultOptions = $.extend(defaultOptions, defaultOptions, opts);
			var settings = $.extend({}, defaultOptions, opts);	
			initRemoteIM(settings);
			
			settings.newWebIM(settings);
			
			$.WebIM.settings = settings;
		}
	});
	
	$.WebIM.settings = $.WebIM.settings || _opts;
	$.WebIM.initWebIM = _opts.initWebIM;
	$.WebIM.newWebIM = _opts.newWebIM;
	$.WebIM.messageHandler = _opts.messageHandler;
	$.WebIM.reloadUserList = _opts.reloadUserList;
	$.WebIM.reloadGroupList = _opts.reloadGroupList;
	$.WebIM.messageClickFun = _opts.messageClickFun;
	$.WebIM.doNotmessageClickFun = _opts.doNotmessageClickFun;
	$.WebIM.applyFileClickFun = _opts.applyFileClickFun;
	$.WebIM.getDate = _opts.getDate();
	$.WebIM.getDatetime = _opts.getDatetime();
	$.WebIM.approveShareCallback = _opts.approveShareCallback;
	$.WebIM.openGroup = _opts.openGroup;
	$.WebIM.atFileTitle = _opts.atFileTitle;
	$.WebIM.shareFilesCallback = _opts.shareFilesCallback;
	$.WebIM.showChatFileLog = _opts.showChatFileLog;
	$.WebIM.sendShareFile = _opts.sendShareFile;
	$.WebIM.sendFileMsg = _opts.sendFileMsg;
	$.WebIM.sendFileMsgNotAction = _opts.sendFileMsgNotAction;
	$.WebIM.applyIntoGroup = _opts.applyIntoGroup;
	$.WebIM.noticeApplyerIntoGroup = _opts.noticeApplyerIntoGroup;
	$.WebIM.crateClassGroup = _opts.crateClassGroup;
	$.WebIM.groupnewmessagestyle = _opts.groupnewmessagestyle;
	$.WebIM.groupnewmessagestyleclass = _opts.groupnewmessagestyleclass;
	$.WebIM.groupnewmessageCounter = _opts.groupnewmessageCounter;
	$.WebIM.groupnewmessagevalue = _opts.groupnewmessagevalue;
	$.WebIM.showText = _opts.showText;
	$.WebIM.reloadGroupUsers = _opts.reloadGroupUsers;
	$.WebIM.sendReplyFileComment = _opts.sendReplyFileComment;
	$.WebIM.gotoUser = _opts.gotoUser;
	$.WebIM.gotoGroup = _opts.gotoGroup;
	$.WebIM.gotoGroupForClass = _opts.gotoGroupForClass;
	$.WebIM.scrollbarCreate = _opts.scrollbarCreate;
	$.WebIM.writeReceiveMessage = _opts.writeReceiveMessage;
	$.WebIM.saveHistorySimpleMsg = _opts.saveHistorySimpleMsg;
	//<a onclick="$.WebIM.messageClickFun('收藏')">收藏</a>
})(jQuery);
