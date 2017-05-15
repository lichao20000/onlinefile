// QQ表情插件
(function($){  
	$.fn.qqFace = function(options){
		var defaults = {
			id : 'facebox',
			path : 'face/',
			assign : 'content',
			tip : 'em_'
		};
		var option = $.extend(defaults, options);
		var assign = $('#'+option.assign);
		var id = option.id;
		var path = option.path;
		var tip = option.tip;
		
		if(assign.length<=0){
			//alert('缺少表情赋值对象。');
			return false;
		}
		
		$(this).click(function(e){
			var strFace, labFace;
			if($('#'+id).length<=0){
				strFace = '<div id="'+id+'" style="position:relative;display:none;z-index:1000;cursor:pointer;border-radius: 6px;" class="qqFace">' +
							  '<table border="0" cellspacing="0" cellpadding="0" style = "border: 1px solid #ddd;border-radius: 6px;background-color:#ffffff;"><tr>';
				for(var i=0; i<=106; i++){
					var iUse = "";
					if(i<10){
						iUse = "00" + i;
					}else if(10<=i && i<100){
						iUse = "0" + i;
					}else{
						iUse = i;
					}
					labFace = '['+tip+iUse+']';
					strFace += '<td><img width="25" height="25" src="'+path+tip+iUse+'.png" onclick="insertAtCaret(this);" /></td>';
					if( i % 20 == 19 ) strFace += '</tr><tr>';
					
				}
				strFace += '</tr></table></div>';
			}
			$(this).parent().append(strFace);
			var offset = $(this).position();
			var top = offset.top + $(this).outerHeight();
			$('#'+id).css('bottom',190);
			
//			var type = $("#emotionType").val();
//			if(type == "message"){
//				$('#'+id).css('left',offset.left);
//			}else{
//				$('#'+id).css('left',-80);
//			}
			$('#'+id).css("background-color","#ffffff");
			$('#'+id).show();
			e.stopPropagation();
		});

		$(document).click(function(){
			$('#'+id).hide();
			$('#'+id).remove();
		});
	};

})(jQuery);

jQuery.extend({ 
unselectContents: function(){ 
	if(window.getSelection) 
		window.getSelection().removeAllRanges(); 
	else if(document.selection) 
		document.selection.empty(); 
	} 
}); 
jQuery.fn.extend({ 
	selectContents: function(){ 
		$(this).each(function(i){ 
			var node = this; 
			var selection, range, doc, win; 
			if ((doc = node.ownerDocument) && (win = doc.defaultView) && typeof win.getSelection != 'undefined' && typeof doc.createRange != 'undefined' && (selection = window.getSelection()) && typeof selection.removeAllRanges != 'undefined'){ 
				range = doc.createRange(); 
				range.selectNode(node); 
				if(i == 0){ 
					selection.removeAllRanges(); 
				} 
				selection.addRange(range); 
			} else if (document.body && typeof document.body.createTextRange != 'undefined' && (range = document.body.createTextRange())){ 
				range.moveToElementText(node); 
				range.select(); 
			} 
		}); 
	}, 

});
function insertAtCaret(obj) {
	var divId = "";
	var type = $("#emotionType").val();
	if(type == "message"){
		divId = "messageValue";
	}else{
		divId = "flyingchatinput";
	}
	$("#" + divId).append("<img width='20' height='20' src='" + obj.src + "' attr='imgEnd'>");
	addFoucs(divId);
}
function addFoucs(divId) {
	var html = $("#"+divId).html();
	$("#"+divId).html("");
	var obj = $("#"+divId);
	var range, node;
	if (!obj.hasfocus) {
		obj.focus();
	}
	if (window.getSelection && window.getSelection().getRangeAt) {
		range = window.getSelection().getRangeAt(0);
		range.collapse(false);
		node = range.createContextualFragment(html);
		var c = node.lastChild;
		range.insertNode(node);
		if (c) {
			range.setEndAfter(c);
			range.setStartAfter(c)
		}
		var j = window.getSelection();
		j.removeAllRanges();
		j.addRange(range);

	} else if (document.selection && document.selection.createRange) {
		document.selection.createRange().pasteHTML(text);
	}
}