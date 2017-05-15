(function(){
	
	$("#viewOnlineUserButton").live("click", function(){
//		alert("测试11");
		
		$.ajax({
    	    url:$.appClient.generateUrl({ESViewOnlineUser:'index'},'x'),
    	    success:function(data){
	    	    	$.dialog({
	    	    		id:'viewOnlineUserPanel',
	    		    	title:'查看在线用户',
	    	    		width: '500px',
	    	    		height: '250px',
	    	    	   	fixed:true,
	    	    	    resize: false,
	    	    	    padding:0,
	    		    	content:data,
	    		    	init : function() {
	    					$('#viewOnlineUserForm').autovalidate();
	    				},
	    		    	okVal:'确定',
	    			    ok:true,
	    			    
//	    			    okId:'btnStart',
//	    		    	button: [
//	    		    	         //guolanrui 20140830 去掉disabled属性BUG：693
//	     		 	            {id:'btnStarts', name: '确定', callback: function(){return false;}}
//	     		 			],
	    			    cancelVal: '关闭',
	    			    cancel: true
	    		    });
    	    	},
    		    cache:false
    	});
		
	});
	
	
	function test1(){
		alert("测试11");
		
	};
	
	
	
	
})();

