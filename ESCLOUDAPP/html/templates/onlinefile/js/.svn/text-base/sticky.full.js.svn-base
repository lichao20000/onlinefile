// Sticky v1.0 by Daniel Raftery
// http://thrivingkings.com/sticky
//
// http://twitter.com/ThrivingKings

(function( $ )
	{
	
	// Using it without an object
	$.sticky = function(note, options, callback) { return $.fn.sticky(note, options, callback); };
	
	$.fn.sticky = function(note, options, callback) 
		{
		// Default settings
		var position = 'bottom-right'; // top-left, top-right, bottom-left, or bottom-right
		
		var settings =
			{
			'speed'			:	'fast',	 // animations: fast, slow, or integer
			'duplicates'	:	true,  // true or false
			'autoclose'		:	60000  // integer or false
			};
		// Passing in the object instead of specifying a note
		if(!note)
			{ note = this.html(); }
		
		if(options)
			{ $.extend(settings, options); }
		
		// Variables
		var display = true;
		var duplicate = 'no';
		
		// Somewhat of a unique ID
		var uniqID = Math.floor(Math.random()*99999);
		// Handling duplicate notes and IDs
//		$('.sticky-note').each(function()
//			{
//			if($(this).html() == note && $(this).is(':visible'))
//				{ 
//				duplicate = 'yes';
//				if(!settings['duplicates'])
//					{ display = false; }
//				}
//			if($(this).attr('id')==uniqID)
//				{ uniqID = Math.floor(Math.random()*9999999); }
//			});
//预先关闭出现的提示
//		$('.sticky-note').each(function()
//		{
//			$('#' + $(this).attr('rel')).dequeue().fadeOut(settings['speed']);
//		});
		// Make sure the sticky queue exists
		if(!$('#flyingsoftStickyMsgs').length){
			//$('#flyingsoftStickyMsgs').prepend("<span class='glyphicon glyphicon-eye-open' id='closeAll' onclick='closeAll()'></span>");
			$('body').append('<div id="flyingsoftStickyMsgs" class="sticky-queue ' + position + '"><div><span onclick="selectAll()" style="padding-left: 50px;font-size: 12px;">您有<span id="countNumber">0</sapn></span>条消息未读</span><span style="top:3px;left:10px;" class="glyphicon glyphicon-remove" onclick="closeAll()" title="取消"></span></div><div id="flyingsoftStickyMsgss" style="display:none;"></div></div>'); 
			//$('body').append('<div id="flyingsoftStickyMsgs" class="sticky-queue ' + position + '">新消息列表<span class="glyphicon glyphicon-eye-open"></span><div id="flyingsoftStickyMsgss" style="overflow:auto;"></div></div>'); 
		}
		var newMsg = null ;
		// Can it be displayed?
		if(display)
			{
			// Building and inserting sticky note
			var msg = '<div style="display:none;" class="sticky border-' + position + '" id="' + uniqID + '"><img id="close_' + uniqID + '" src="templates/onlinefile/images/close.png" class="sticky-close" rel="' + uniqID + '" title="取消" /><div class="sticky-note" rel="' + uniqID + '">' + note + '</div></div>';
			//$('#flyingsoftStickyMsgs').prepend(msg);
			$('#flyingsoftStickyMsgss').prepend(msg);
			var startTime=0;
		    //获取总数
			$('.sticky-note').each(function(){
				startTime++;	
			});
			if(startTime == 0){
				closeAll();
			}
			$('#countNumber').html(startTime);
			// Smoother animation
			newMsg = $('#' + uniqID) ;
//			var height = newMsg.height();
//			newMsg.css('height', height);
			newMsg.slideDown(settings['speed']);
			display = true;
			}
		
		// Listeners
		newMsg.ready(function()
			{
			// If 'autoclose' is enabled, set a timer to close the sticky
			if(settings['autoclose'])
				{ newMsg.delay(settings['autoclose']).fadeOut(settings['speed'], function(){
					
					$(this).remove();
					    var startTime=0;
						$('.sticky-note').each(function(){
							startTime++;	
						});
						if(startTime == 0){
							closeAll();
						}
						$('#countNumber').html(startTime);
				}); }
			});
		// Closing a sticky
		$('#close_' + uniqID).click(function()
			{ 
			 $('#' + $(this).attr('rel')).dequeue().fadeOut(settings['speed']); 
			});
		
		// Callback data
		var response = 
			{
			'id'		:	uniqID,
			'duplicate'	:	duplicate,
			'displayed'	: 	display,
			'position'	:	position
			}
		
		// Callback function?
		if(callback)
			{ callback(response); }
		else
			{ return(response); }
		
		}
	})( jQuery );

function closeAll(){
	$('#flyingsoftStickyMsgss').dequeue().fadeOut('fast'); 
	$('#flyingsoftStickyMsgs').remove();
}

function selectAll(){
	$('#flyingsoftStickyMsgss').css("overflow","auto");
	$('#flyingsoftStickyMsgss').css("height","200px");
	$('#flyingsoftStickyMsgss').css("display","block");
}