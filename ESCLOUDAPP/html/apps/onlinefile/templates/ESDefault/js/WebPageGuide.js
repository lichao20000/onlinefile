var prompt_ids = new Array();   //保存需要功能提示的组件ID
var prompt_content = new Array();	//保存功能提示的内容
	prompt_ids.push("#online_left_top");
	prompt_content.push("上线/离线;修改用户信息;修改密码等");
	prompt_ids.push("#cantUpload");
	prompt_content.push("上传文件");
	prompt_ids.push("#flex_menu_btn");
	prompt_content.push("收藏列表，我的文档，所有文档");
	prompt_ids.push(".chatusersdiv");
	prompt_content.push("联系人列表，群组列表；首次登录请选择列表第一位好友“飞扬机器人”完善您的个人信息");
	/*prompt_ids.push("#myemailBtn");
	prompt_content.push("选择您的邮箱");*/
	prompt_ids.push("#emmanager");
	prompt_content.push("添加邮箱,邮箱配置");
	prompt_ids.push(".im-edit-ipt");
	prompt_content.push("聊天输入框,上排工具栏可选");
(function($) {
	var WebPageGuide = function(options) {
		this.settings = {
			// showCloseButton:false,
			source : null
		}
		// this.closeButton='<a href="javascript:void(0);" class="WPGclose"
		// title="关闭新手帮助">×</a>';
		this.stepTemplate = '<div class="WPGstep" seq="" >'
				+'<div class="r1"></div><div class="wh_t"></div><div class="r2"></div>'
				+ '<div class="closeX" style="display:none"></div>'
				+ '<b class="WPGjt" style=""></b>'
				+ '<p style="min-height:40px;height:auto;"><span class="h1 WPGstepNum"></span><span class="h2 WPGstepTitle"></span>'
				+ '<br><span class="WPGstepContent"></span></p>'
				+'<div class="r3"></div><div class="wh_b"></div><div class="r4"></div>'
				+'</div>';

		this.settings = $.extend(this.settings, options);
		this.stepList = [];
		return this;
	}

	WebPageGuide.prototype = {
		newGuidStep : function(source, title, num) {
			var item = {};
			// num = this.stepList.length;
			item.source = source;
			item.title = title;
			// item.content = content;
			item.container = $(this.stepTemplate);
			item.container.find(".WPGstepTitle").html(item.title);
			// item.container.find(".WPGstepContent").html(item.content);
			item.container.attr("seq", num);
			item.container.attr("id", "step" + num);
			item.container.find(".WPGstepNum").html(num + 1+".");
			this.stepList.push(item);
			// 先添加到页面中，否则无法获取container的宽高

			// $(".WPGhelp").append(item.container);
			$("#mainContentContainer").append(item.container);
			var target = $(source);
			var corner = item.container.find(".WPGjt");
			var tleft = target.offset().left;
			var ttop = target.offset().top;
			var twidth = target.width();
			var theight = target.height();
			var cheight = item.container.height();
			var cwidth = item.container.width();
			var cpaddingHeight = parseInt(item.container.css("padding-bottom"))
					+ parseInt(item.container.css("padding-top"));
			var cpaddingWidth = parseInt(item.container.css("padding-left"))
					+ parseInt(item.container.css("padding-right"));
			var cnBorder = 20;
			// 根据target的位置设置提示框的位置
			if (tleft < (document.body.offsetWidth / 2)) {
				if (ttop < (document.body.offsetHeight / 4)) {
					if(source == "#flex_menu_btn"){
						item.container.css({
							top : ttop + theight + cnBorder + 10,
							left : tleft + twidth / 2 - 34
						});
					}else{
						item.container.css({
							top : ttop + theight + cnBorder - 10,
							left : tleft + twidth / 2 - 40
						});
					}
					
					corner.addClass("WPGjt_topleft");
				} else if (ttop > (document.body.offsetHeight * 3 / 4)) {
					if(source == ".im-edit-ipt"){
						item.container.css({
							top : ttop - cheight - cpaddingHeight - cnBorder,
							left : tleft + twidth / 2 - 440
						});
					}else{
						item.container.css({
							top : ttop - cheight - cpaddingHeight - cnBorder,
							left : tleft + twidth / 2 - 40
						});
					}
						
					corner.addClass("WPGjt_bottomleft");
				} else {
					item.container.css({
						top : ttop + (theight - cheight - cpaddingHeight) / 2,
						left : tleft + twidth + cnBorder - 40
					});
					corner.addClass("WPGjt_left");
				}
			} else {
				if (ttop < (document.body.offsetHeight / 4)) {

					
					if (source == ".chatusersdiv") {
						item.container.css({
							top : ttop + theight + cnBorder + 5,
							left : tleft - cwidth / 2 - 30
						});
					} else{
						item.container.css({
							top : ttop + theight + cnBorder,
							left : tleft - cwidth / 2 - 50
						});
					}/*else if (source == "#myem") {
						item.container.css({
							top : ttop + theight + cnBorder - 10,
							left : tleft - cwidth / 2 - 50
						});
					}*/
					corner.addClass("WPGjt_topright");
				} else if (ttop > (document.body.offsetHeight * 3 / 4)) {
					item.container.css({
						top : ttop - cheight - cpaddingHeight - cnBorder,
						left : tleft - cwidth / 2
					});
					corner.addClass("WPGjt_bottomright");
				} else {
					item.container.css({
						top : ttop + (theight - cheight - cpaddingHeight) / 2,
						left : tleft - cwidth - cpaddingWidth - cnBorder - 40
					});
					corner.addClass("WPGjt_right");
				}
			}

			return item;
		},
		startGuide : function() {
			// $(".WPGhelp").css("visibility","visible");
			// $(".WPGhelp").css("display","none");
			// 最后一个按钮内容为完成
			/*if (this.stepList.length > 1) {
				$("#step0").css("display", "block");
			}*/
			// this.stepList[this.stepList.length-1].container.find(".WPGnext").html("完成");
			this.stepList[0].container.show();
		}
	}
	$.extend({
		WebPageGuide : function(options) {
			return new WebPageGuide(options);
		}
	});
	$(document).mousemove(function(e){
		var mX = e.pageX;
		var mY = e.pageY;
		var wp = $('.WPGstep');
		if($(wp).attr('class')){
			if((mY<$(wp).offset().top+$(wp).height() && mY>$(wp).offset().top-30)&&
				(mX>$(wp).offset().left+30 && mX<$(wp).offset().left+30+$(wp).width())){
				$('.closeX').show();
			}else{
				$('.closeX').hide()
			}
		}
	});
	// 绑定下一步事件
	$("#mainContentContainer").on('click', '.closeX', 
		function() {
			var obj = $(this).parents('.WPGstep');
			var step = obj.attr('seq');
			$('.WPGstep').remove();
			var new_s = $.WebPageGuide({
				showCloseButton : true
			});
			if(parseInt(step)<prompt_ids.length-1){
				new_s.newGuidStep(prompt_ids[parseInt(step) + 1],
						prompt_content[parseInt(step) + 1], parseInt(step) + 1);
				new_s.startGuide();
				if(prompt_ids[parseInt(step) + 1]==".im-edit-ipt"){
					$('.im-edit-ipt').focus();
				}
			}
	});
})(jQuery);