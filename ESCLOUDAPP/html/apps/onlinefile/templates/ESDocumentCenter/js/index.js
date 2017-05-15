var menu_file_members_data = {};
$(function(){
	documentCenter.queryMembers();
});

$("#header_search_form").keydown(function(e){
	 var e = e || event,
	 keycode = e.which || e.keyCode;
	 if (keycode==13) {
		 $("#header_search_form .icon_search").click();
	 }
});

/**
 * 检索
 */
$("#header_search_form .icon_search").click(function(){
	var query = $('#search_terms').val();
	if (query !== "") {
		// 跳转到检索结果页
		$('#search_results_container').html(template('search_results_container_template', {}));
		$("#search_results_scroller").scrollbarCreate();
		documentCenter.searchFile(query);
		$("#files_tab").removeClass("active");
		$("#search_tab").addClass("active");
		$("#search_terms").blur();
		$("#search_terms").focus();
	}
});

$("#search_terms").keyup(function(){
	if(($('#search_tab').is(':hidden'))){
		if ($(this).val().length > 0) {
			$("#search_clear").removeClass("hidden");
		} else {
			$("#search_clear").addClass("hidden");
		}
	}
	return false;
});

$("#search_clear").click(function(){
	$("#search_terms").val("");
	$("#search_clear").addClass("hidden");
	$("#search_terms").focus();
	if(($('#files_tab').is(':hidden'))){
		$("#search_tab").removeClass("active");
		$("#files_tab").addClass("active");
		$("#file_preview_container").html("");
	}
});

//文件列表页面 ============================================================================================

/** 所有文档 */
$('#file_list_toggle_all').on('click', function(){
	$(this).addClass('active');
	$('#file_list_toggle_user').removeClass('active');
	documentCenter.queryMembers();
	// 设置值
	$('#file_list_toggle_user a').html("我的文档");
	$('#selectUserId').val(g_userId);
	$('#selectUserName').val(g_userName);
	$('#selectFileClassId').val('1');
	// 查询所有
	documentCenter.getFileList("1", "0");
	
	locationClassTitle("1");
	
	uploadBtnCanDo(false);
});

/** 我的文档/指定用户文档 */
$('#file_list_toggle_user').on('click', function(){
	$(this).addClass('active');
	$('#file_list_toggle_all').removeClass('active');
	// 查询指定用户的文档
	var userId = $('#selectUserId').val();
	$('#selectFileClassId').val('1');
	documentCenter.getFileList("1", userId);
	
	locationClassTitle("1");
	
	uploadBtnCanDo(false);
});

/** 选择pane菜单 */
// pane菜单 收藏列表
$('#file_maincontent').on('click', '#list_stars', function(){
	$('#file_maincontent .tab-pane').removeClass("active");
	$("#files_tab").addClass("active");
	
});
// pane菜单 我的文档
$('#file_maincontent').on('click', '#files_user', function(){
	$('#file_maincontent .tab-pane').removeClass("active");
	$("#files_tab").addClass("active");
	
	$('#file_list_toggle_user').click();
});
// pane菜单 所有文档
$('#file_maincontent').on('click', '#files_all', function(){
	$('#file_maincontent .tab-pane').removeClass("active");
	$("#files_tab").addClass("active");
	
	$('#file_list_toggle_all').click();
});

/** 查找用户，弹出用户列表窗口 */
$('#file_list_toggle_users').on('click', function() {
	if ($('#menu_custom').length == 0) {
		if (!$('#file_list_toggle_user').hasClass("active")) {
			$('#file_list_toggle_user').click();
		}
		documentCenter.queryMembers();
		var menu_html = template('menu_template', {});
		$('#file_maincontent').append(menu_html);
		
		var menu_file_member_header_html = template('menu_file_member_header_template', {});
		$('#menu_custom #menu_header').html(menu_file_member_header_html);
		
		var menu_file_member_filter_items_html = template('menu_file_member_filter_items_template', menu_file_members_data);
		$('#menu_custom #menu_items').html(menu_file_member_filter_items_html);
		
		// 计算滚动条
		$('#menu_custom #menu_items_scroller').scrollbarCreate();
		
		// 定位
		var tmpH = $('#file_list_toggle_user').height() + 4;
	    var tmpW = $('#file_list_toggle_user').width() - 18;
	    var offset = $('#file_list_toggle_users').offset();  
	    $("#menu_custom").css({ top: offset.top + tmpH + "px", left: offset.left - tmpW });  
	} else {
		$('#menu_custom').remove();
	}
    return false;
});

/**
 * members 模糊检索
 */
$("#file_maincontent").on('keyup', '#file_member_filter input', function(){
	if ($(this).val().length > 0) {
		$("#file_member_filter a.icon_close").removeClass("hidden");
	} else {
		$("#file_member_filter a.icon_close").addClass("hidden");
	}
	var activeObj = $("#menu_custom li.member_item")
		.removeClass("active")
		.filter(":contains('"+( $(this).val() )+"')")
		.addClass("active");
	if (activeObj.length == 0) {
		$('#file_member_filter_no_matches span').html($(this).val());
		$('#file_member_filter_no_matches').removeClass("hidden");
	} else {
		$('#file_member_filter_no_matches span').html("");
		$('#file_member_filter_no_matches').addClass("hidden");
	}
	$(this).focus();
});

$("#file_maincontent").on('click', '#file_member_filter a.icon_close', function(){
	$('#file_member_filter input').val("");
	$('#file_member_filter input').keyup();
});

$('#file_maincontent').on('click', '#menu_custom', function(){
	return false;
});

/**
 * 点击成员列表的list
 */
$('#file_maincontent').on('click', '#menu_items li.member_item', function(){
	var memberId = $(this).attr("data-member-id");
	var memberName = $(this).find("span.name").html();
	if (memberId == g_userId) {
		memberName = "我的文档";
	}
	if(memberName == "我的文档"){
		uploadBtnCanDo(true);
	}else{
		uploadBtnCanDo(false);
	}
	$('#selectUserId').val(memberId);
	$('#selectUserName').val(memberName);
	$('#file_list_toggle_user a').html(memberName);
	documentCenter.getFileList("1", memberId);
	$("#menu_custom").remove(); 
	locationClassTitle("1");
});


/** 文档列表click事件 */
$('#file_maincontent').on('click', 'div.file_list .file_list_item', function(){
	if ($(this).attr('name') == 'classInfo') { // 点击分类
		// 获取分类id
		var classId = $(this).attr('fileId');
		var className = $(this).attr('fileName');
		var isMember = $(this).attr('isMember');
		var idSeq = $(this).attr('idSeq');
		var userId = $('#selectUserId').val();
		if ($('#file_list_toggle_all').hasClass('active')) { // 正在所有文档
			userId = "0";
		}
		$('#selectFileClassId').val(classId);
		documentCenter.getFileList(classId, userId);
		appendClassTitle(classId, className, isMember, idSeq);
		//判断当前分类下是否有上传的权限
		if(userId == "0" || userId == g_userId){
			//说明是全部文档下
			if("true" == $("#classNavi li.last").attr("isMember")){
				uploadBtnCanDo(true);
			}else{
				uploadBtnCanDo(false);
			}
		} else{
			//说明在别人的
			uploadBtnCanDo(false);
		}
		
		// 如果是一层分类，打开对应分类的分组
		if ($(this).attr('classId') == 1 ) {
			$.WebIM.openGroup($(this));
		}
		
	} else { // 点击文件
		// 判断是否有权限
		if ($(this).attr('hasRight') == '1') {
			// 文档预览
			var fileId = $(this).attr('fileId');
			var classId = $(this).attr('classId');
			documentCenter.showFileInfo(classId, fileId);
		} else {
			showMsg('您没有获得该文件的分享，请向文件所有者发起申请！', '3');
		}
		
	}
});


$('#file_maincontent').on('click', 'div.file_list a[name="userName"]', function(){
	var userId = $(this).attr("data-user-id");
	var userName = $(this).html();
	showMsg("用户" + userName);
	return false; // 解决冒泡事件
});

/**
 * 分享
 */
$('#file_maincontent').on('click', 'div.file_list a[name="share"]', function(){
	var $file = $(this).closest('.file_list_item');
	shareAction($file);
	return false; // 解决冒泡事件
});

/**
 * 赞
 */
$('#file_maincontent').on('click', 'div.file_list a[name="up"]', function(){
	upAction($(this));
	return false; // 解决冒泡事件
});

/**
 * 申请
 */
$('#file_maincontent').on('click', 'div.file_list a[name="apply"]', function(){
	applyAction($(this));
	return false; // 解决冒泡事件
});

/**
 * 下载 - 历史版本
 */
$('div.file_preview_container').on('click', '#versions_list a[name="download"]', function(){
	var $file = $(this).closest('.file_list_item');
	downloadAction($file);
	return false; // 解决冒泡事件
});

/**
 * 分享 - 历史版本
 */
$('div.file_preview_container').on('click', '#versions_list a[name="share"]', function(){
	var $file = $(this).closest('.file_list_item');
	shareAction($file);
	return false; // 解决冒泡事件
});

/**
 * 赞 - 历史版本
 */
$('div.file_preview_container').on('click', '#versions_list a[name="up"]', function(){
	upAction($(this));
	return false; // 解决冒泡事件
});

/**
 * 申请 - 历史版本
 */
$('div.file_preview_container').on('click', '#versions_list a[name="apply"]', function(){
	applyAction($(this));
	return false; // 解决冒泡事件
});

$('#uploadfileMainAId').on('click', function() {
	 $("#addFileMainDivId").show(0); 
   return false;
});

/**
 * 下载操作
 * @param $obj
 */
function downloadAction($obj) {
	var $infoObj = $obj.closest('.file_list_item');
	var filePath = $infoObj.attr('filePath');
	var fileName = $infoObj.attr('fileName');
	var fileType = $infoObj.attr('fileType');
	documentCenter.downloadFile(filePath, fileName + "." + fileType);
}

/**
 * 分享操作
 * @param $obj
 */
function shareAction($obj) {
	// 将点击的文件添加分享记录中
	var classId = $obj.attr("classId");
	var fileId = $obj.attr("fileId");
	var title = $obj.attr("fileName") + "." + $obj.attr("fileType");
	documentCenter.shareFileList = documentCenter.shareFileList + fileId + ":" + title + ";";
//	$.WebIM.atFileTitle(title);
	// 发送一条消息，将文件分享到聊天界面
	$.WebIM.sendShareFile(classId, fileId);
}

/**
 * 赞操作
 * @param $obj
 */
function upAction($obj) {
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
	var $file = $obj.closest('.file_list_item');
	documentCenter.praiseFile($file.attr('fileId'), g_userId, selected);
}

/**
 * 申请操作
 * @param $obj
 */
function applyAction($obj) {
	var $infoObj = $obj.closest('.file_list_item');
	var divId = $infoObj.attr('id');
	var userName = $infoObj.attr('userName');
	var fileId = $infoObj.attr('fileId');
	var fileName = $infoObj.attr('fileName');
	$.WebIM.applyFileClickFun(userName, fileId, fileName, divId);
}

// 分页 ****************************************************************************************************************

$('#file_maincontent').on('click', '.paging a.page_btn', function() {
	var curPage = $('div.paging input[name="page"]').val();
	var pageSize = $('div.paging input[name="pageSize"]').val();
	var total = $('div.paging input[name="total"]').val();
	var num = 0;
	if ($(this).hasClass('previous')) { // 上一页
		if (curPage == 1) { return; }
		num = -1;
	} else { // 下一页
		if (curPage == total) { return; }
		num = 1;
	}
	// 判断
	if($("#search_tab").hasClass("active")) { // 检索结果的翻页
		var query = $('#search_terms').val();
		documentCenter.searchFile(query, (parseInt(curPage) + num), pageSize);
	} else { // 文档中心列表的翻页
		var classId = $('#selectFileClassId').val();
		var userId = $('#selectUserId').val();
		if ($('#file_list_toggle_all').hasClass('active')) {
			userId = "0";
		}
		documentCenter.getFileList(classId, userId, (parseInt(curPage) + num), pageSize);
	}
});

$('#filelist_paging').on('click', '#pages-menu ul li', function(){
	// 获取选中页数
	var page = $(this).attr("data-page");
	var classId = $('#selectFileClassId').val();
	var userId = $('#selectUserId').val();
	if ($('#file_list_toggle_all').hasClass('active')) {
		userId = "0";
	}
	// 刷新列表
	documentCenter.getFileList(classId, userId, page);
	$('#pages-menu').hide();
});

$('#filelist_paging').on('click', 'div.paging span.pages', function() {
	if ($('#pages-menu').css("display")=="none") {
	    var tmpH = -$('#pages-menu').height() - 10;
	    var tmpW = 10;
	    //设置弹出层位置，获取相对位置
	    var offset = $(this).position();
	    $("#pages-menu").css({ top: offset.top + tmpH + "px", left: offset.left + tmpW }); 
	    $("#pages-menu").show(0);
	    $('#pages-menu .perfect-scroller').scrollbarCreate();
	    $('#pages-menu .perfect-scroller').scrollTop($('#pages-menu').height());
	    $('#pages-menu .perfect-scroller').scrollbarUpdate();
	} else {
		$("#pages-menu").hide(0);
	}
    return false;
});

$('#filelist_paging').on('mouseover click', '#pages-menu', function(){
	return false;
});

$('#filelist_paging').on('mouseleave', '#pages-menu', function(){
	$('#pages-menu').hide();
	return false;
});

// 文件信息页面 ============================================================================================

$('div.file_preview_container').on('click', 'a.file_actions', function() {
	if ($('#menu_custom').length == 0) {
		var $filePreviewContainer = documentCenter.getActiveFilePreviewContainer();
		// 获取文件ID
		var fileId = $filePreviewContainer.find("input[name='fileId']").val();
		var menu_html = template('menu_template', {});
		$('#file_maincontent').append(menu_html);
		
		var data = documentCenter.fileActions;
		var actions_item_html = template('menu_file_action_items_template', data);
		$('#menu_custom #menu_items').html(actions_item_html);
		
		// 定位
		var tmpH = 0;
	    var tmpW = $(this).width() + 5;
	    var offset = $(this).offset();
	    $("#menu_custom").css({ top: offset.top + tmpH + "px", left: offset.left + tmpW });  
	} else {
		$("#menu_custom").remove();
	}
    return false;
});

$('div.file_preview_container').on('click', 'a.file_title', function() {
	var $filePreviewContainer = documentCenter.getActiveFilePreviewContainer();
	var title = $filePreviewContainer.find("input[name='fileTitle']").val();
	$.WebIM.atFileTitle(title);
    return false;
});

/**
 * 文件下载
 */
$('div.file_preview_container').on('click', 'a.filetype_button', function() {
	var $filePreviewContainer = documentCenter.getActiveFilePreviewContainer();
	// 获取filePath
	var filePath = $filePreviewContainer.find('input[name="filePath"]').val();
	var fileName = $filePreviewContainer.find('input[name="fileTitle"]').val();
	documentCenter.downloadFile(filePath, fileName);
});


$('#flex_menu_btn').on('click', function() {
	if ($('#menu_custom').length == 0) {
		var menu_html = template('menu_template', {});
		$('#file_maincontent').append(menu_html);
		$('#menu_custom').addClass("popover_menu").addClass("flex_menu");
		$('#menu_custom').prepend('<span class="arrow"></span>');
		$('#menu_custom').prepend('<span class="arrow_shadow"></span>');
		
		var menu_flexpane_header_html = template('menu_flexpane_header_template', {});
		$('#menu_custom #menu_header').html(menu_flexpane_header_html);
		
		var menu_flexpane_items_html = template('menu_flexpane_items_template', {});
		$('#menu_custom #menu_items').html(menu_flexpane_items_html);
		$('#menu_custom #menu_items_scroller').scrollbarCreate();
		
		var menu_flexpane_footer_html = template('menu_flexpane_footer_template', {});
		$('#menu_custom #menu_footer').html(menu_flexpane_footer_html);
		// 定位
		var tmpH = $(this).height() + 20;
	    var tmpW = -$('#menu_custom').width() + 40;
	    var offset = $(this).offset();  
	    $("#menu_custom").css({ top: offset.top + tmpH + "px", left: offset.left + tmpW });  
	} else {
		$('#menu_custom').remove();
	}
	
    return false;
});

/**  菜单action的click事件    **********************************************************************/
// 分享按钮
$('#file_maincontent').on('click', '#share_file', function(){
	$('#menu_custom').remove();
});

//编辑按钮
$('#file_maincontent').on('click', '#edit_file', function(){
	documentCenter.fileActionClick('edit_file', $(this).attr('data-file-id'));
	$('#menu_custom').remove();
});

//下载按钮
$('#file_maincontent').on('click', '#download_file', function(){
	var $filePreviewContainer = documentCenter.getActiveFilePreviewContainer();
	var filePath = $filePreviewContainer.find('input[name="filePath"]').val();
	var fileTitle = $filePreviewContainer.find('input[name="fileTitle"]').val();
	documentCenter.downloadFile(filePath, fileTitle);
	$('#menu_custom').remove();
});

//删除按钮
$('#file_maincontent').on('click', '#delete_file', function(){
	var fileId = $(this).attr('data-file-id');
	$.dialog({
		title : "删除文件",
		content: "您确定要删除该文件吗？",
    	ok: function() {
    		documentCenter.fileActionClick('delete_file', fileId);
    		// 返回文件列表
    		$('#backup_main').click();
    		// 刷新
    		$('#file_list_toggle_all').click();
    		return true;
    	},
    	lock: true,
    	cancelVal:'取消',
    	cancel: true
    });
	$('#menu_custom').remove();
});

//单击空白区域隐藏弹出层  
$(document).click(function(event) {
	if ($(".menu").length > 0) {
		$(".menu").hide(0); 
	}
	if ($('#menu_custom').length > 0) {
		$('#menu_custom').remove();
	}
	if ($('#email_menu_custom').length > 0){
		$('#email_menu_custom').remove();
	}
});  

// 从文件详细页返回
$('div.tab-content').on('click', '#backup_main', function(){
	var fromTabPaneId = $(this).attr("from-tab");
	var $filePreviewContainer = documentCenter.getActiveFilePreviewContainer();
	$filePreviewContainer.addClass('hidden');
	if (fromTabPaneId == "files_tab") {
		$('#file_list_container').removeClass('hidden');
	} else if (fromTabPaneId == "search_tab") {
		$('#search_results_container').removeClass('hidden');
	}
	$filePreviewContainer.html("");
});


/*  liumingchao   文件类型那个添加到主页列表*/

$("#file_list").on('click', 'div.file_list_item .class_tool_menu', function(){
	if ($('#menu_custom').length == 0) {
		
		var menu_html = template('menu_template', {});
		$('#file_maincontent').append(menu_html);
		var last = $('.last a').attr("data-class-id");
		var data = "{targetId:'"+$(this).attr('target-id')+"',last:'"+last+"'}";
		
		var menu_class_action_items_html = template('menu_class_action_items_template', eval("("+data+")"));
		$('#menu_custom #menu_items').html(menu_class_action_items_html);
		
		// 计算滚动条
		$('#menu_custom #menu_items_scroller').scrollbarCreate();
		
		// 定位
		var tmpH = $(this).height() + 10;
	    var tmpW = 0;
	    var offset = $(this).offset();  
	    $("#menu_custom").css({ top: offset.top + tmpH + "px", left: offset.left - tmpW });  
	} else {
		$('#menu_custom').remove();
	}
    return false;
});

$("#file_list").on('hover', '.file_list_item', function() {
	$("#file_list .class_tool_menu").addClass("hidden");
	$(this).find('.class_tool_menu').removeClass("hidden");
});


/** longjunhao 分类导航 start *******************************************************************************************/
// 初始化 记录分类导航json
classObjsJson = {"classObjs":[{"id":"1","name":""}]};
// 添加导航
function appendClassTitle(classId,className,isMember,idSeq) {
	classObjsJson.classObjs[classObjsJson.classObjs.length] = {"id":classId, "name":className, "isMember":isMember, "idSeq":idSeq};
	refreshClassBreadcrumbs();
}
// 定位到
function locationClassTitle(classId) {
	var data = eval(classObjsJson.classObjs);
	var index = 0;
	for(var i=0; i<data.length; i++) {
		if (data[i].id == classId) {
			index = i;
			break;
		}
	}
	for (var i=data.length; i>index+1; i--) {
		data.pop();
	}
	refreshClassBreadcrumbs();
}

//刷新分类导航条数据
function refreshClassBreadcrumbs() {
	var file_class_breadcrumbs_item_html = template('file_class_breadcrumbs_item_template', classObjsJson);
	$('#classNavi').html("");
	$('#classNavi').html(file_class_breadcrumbs_item_html);
	$("#classNavi").jBreadCrumb();
}
$("#classNavi li.last").attr("isMember")
// 导航title的click事件
$("#classNavi").on('click','li',function(){
	var obj = $(this).find("a");
	var classId = obj.attr('data-class-id');
	locationClassTitle(classId);
	
	// 设置值
	var userId = $('#selectUserId').val();
	if ($('#file_list_toggle_all').hasClass('active')) { // 正在所有文档
		userId = "0";
	}
	$('#selectFileClassId').val(classId);
	// 刷新filelist数据
	documentCenter.getFileList(classId, userId);
	
	if ($(this).hasClass("first")) {
		uploadBtnCanDo(false);
	} else {
		if(userId == "0" || userId == g_userId){
			//说明是全部文档下
			if("true" == $("#classNavi li.last").attr("isMember")){
				uploadBtnCanDo(true);
			}else{
				uploadBtnCanDo(false);
			}
		} else{
			//说明在别人的
			uploadBtnCanDo(false);
		}		
	}
});

refreshClassBreadcrumbs();

/** 分类导航 end *******************************************************************************************/

/**
 * 打开文件的页面
 */
$('#chat').on('click', 'div.file_share_item a.file-open', function() {
	var $obj = $(this).closest('div.file_share_item');
	var fileId = $obj.attr('fileId');
	var classId = $obj.attr('classId');
	documentCenter.showFileInfo(classId, fileId);
});

/**
 * 在聊天框的分享中下载
 */
$('#chat').on('click', 'div.file_share_item a.file-download', function() {
	var $obj = $(this).closest('div.file_share_item');
	var filePath = $obj.attr('filePath');
	var fileName = $obj.attr('fileName');
	var fileType = $obj.attr('fileType');
	documentCenter.downloadFile(filePath, fileName + "." + fileType);
});

/**
 * 处理上传按钮的逻辑
 */ 
function uploadBtnCanDo(status) {
	if (status == true) {
		$("#uploadfileMainAId").addClass("uploadfileMainA");
		$("#cantUpload").hide();
	} else {
		$("#cantUpload").show();
		$("#uploadfileMainAId").removeClass("uploadfileMainA");
	}
}

//文件详情页   有定义属性时，鼠标悬浮事件
$('div.file_preview_container').on('mouseover', 'span.file_title', function(){
	if($("#fileDetailShowDivId div").length > 0){
		$("#fileDetailShowDivId").show();
	}else{
		$("#fileDetailShowDivId").hide();
	}
	return false; // 解决冒泡事件
});
$('div.file_preview_container').on('mouseout', 'span.file_title', function(){
	$("#fileDetailShowDivId").hide();
	return false; // 解决冒泡事件
});

$("div.file_preview_container").on('click', '#file_title_container .chatlog', function() {
	var $filePreviewContainer = documentCenter.getActiveFilePreviewContainer();
	var fileId = $filePreviewContainer.find("input[name='fileId']").val();
	// 在聊天记录框中插入关于该文件的历史聊天信息
	$.WebIM.showChatFileLog(fileId);
	return false;
});

