/**
 * 自定义面包屑导航栏
 * @author longjunhao
 * @param $
 */
;(function($) {
	// Private variables
    var _options = {};
    var _container = {};
    // 所有items
    var items = {};
    // 隐藏的items
    var invisibleItems = {};
    // 显示的items
	var pathItems = {};
	
	var clickFunName = "";
	
    // Public functions
	jQuery.fn.jFolderCrumb = function(options) {
        _options = $.extend({}, $.fn.jFolderCrumb.defaults, options);
        
        return this.each(function() {
            _container = $(this);
            if (jQuery(_container).is(":hidden")) {
            	jQuery(_container).show();
            }
            setupFolderCrumb();
        });
        
    };
    
    function initVar() {
        invisibleItems = {};
    	pathItems = {};
    }
    
    function setupFolderCrumb() {
    	initVar();
    	items = _options.data;
    	clickFunName = _options.clickFunName
    	var folderPathsW = jQuery(_container).outerWidth(true);
    	if (folderPathsW == 0) {
    		folderPathsW = 300;
    	}
    	jQuery(_container).html("");
    	var $panel = $('<ul class="explorer-nav"></ul>').appendTo(jQuery(_container));
    	for (var i=0; i<items.length; i++) {
    		$panel.append('<li class="path-item">'+items[i].name+'</li>');
    	}
    	
    	// 计算宽度
    	var arrayLi = $panel.find("li");
    	var pathItemsW = 0;
    	var pathStartIndex = 0;
    	for (var i=items.length-1; i>=0; i--) {
    		var w = jQuery(arrayLi[i]).outerWidth(true);
    		pathItemsW += w;
    		if (pathItemsW > folderPathsW) {
    			pathStartIndex = i + 1;
    			break;
    		}
    	}
    	if (pathStartIndex > 0) {
    		// 设置隐藏的items
    		for (var i=0; i<pathStartIndex; i++) {
    			invisibleItems[i] = items[i];
    		}
    	}
		// 设置显示的items
		for (var i=pathStartIndex; i<items.length; i++) {
			pathItems[i-pathStartIndex] = items[i];
		}
		
    	var folder_path_html = template('folder_path_template', {"invisibleItems":invisibleItems,"pathItems":pathItems,"total":items.length-pathStartIndex,"clickFunName":clickFunName});
    	jQuery(_container).empty();
    	jQuery(_container).html(folder_path_html);
    	
    }
    
    jQuery(document).on('click','ul.explorer-nav li[data-folder-id]',function(){
		var dataId = $(this).attr("data-folder-id");
		var index = 0;
		for (var i=0; i<items.length; i++) {
			if (dataId == items[i].id) {
				index = i;
				break;
			}
		}
		for (var i=items.length; i>index+1; i--) {
			items.pop();
		}
		_options.data = items;
		setupFolderCrumb();
	});

})(jQuery);


;(function($) {
	/** 滚动条插件 */
    // 创建滚动条
    $.fn.scrollbarCreate = function() {
    	this.perfectScrollbar();
    	this.scrollLeft(300);
    	this.perfectScrollbar('update');
    };
    // 更新滚动条
    $.fn.scrollbarUpdate = function() {
    	this.perfectScrollbar('update');
    };
    
    $.fn.scrollbarTopUpdate = function(height) {
    	if (!height) {
    		height = 0;
    	}
    	this.scrollTop(height);
    	this.perfectScrollbar('update');
    };
    $.fn.showTooltips = function(msg,time){
    	if (msg==''){ return; }
    	this.prepend("<div class='for_fix_ie6_bug' style='position:relative;'><div class='tooltips_main'><div class='tooltips_box'><div class='tooltips'><div class='msg'>"+msg+"</div></div><div class='ov'></div></div></div></div>");
    	this.find(".tooltips_main").fadeIn("slow").animate({ marginTop: "-23px"}, {queue:true, duration:400});
    	try{
    		if(typeof time != "undefined"){
    			this.find('.tooltips_main').delay(time).fadeOut('fast', function(){$(".for_fix_ie6_bug").remove();});
    		}
    	}catch(err){}
    };
    $.fn.hideAllTooltips = function(){
    	this.find('.tooltips_main').fadeOut("slow");
    	this.find('.tooltips_main').remove();
    };
    String.prototype.endWith=function(str){
    	if(str==null||str==""||this.length==0||str.length>this.length)
    	  return false;
    	if(this.substring(this.length-str.length)==str)
    	  return true;
    	else
    	  return false;
    	return true;
    }
    
})(jQuery);