/*$(document).ready(function(){
	$(".editbtn").live("click", function(){
//		alert(111);//此处有个问题，父窗口关掉再打开后，点编辑按钮会弹出两个dialog，还没有找到原因
		editCate($(this));
	});
//	
	$("#ids").live("click", function(){
		$("#documentClassTable").find("input[name='id']").prop("checked", this.checked);
	});
	// 生成表格
	$("#documentClassTable").flexigrid({
		url: $.appClient.generateUrl({ESDocumentClass:'getCateList'},'x'),
		dataType: 'json',
//		minwidth: 20,
		//editable: true,
		colModel : [
			{display: '', name: 'rownum', width: 20, align: 'center'},
			{display: '<input type="checkbox" id="ids" name="ids">', name : 'id', width : 40, align: 'center'},
			{display: '编辑', name : 'editbtn', width : 30, align: 'center'},
			{display: '企业编号', name : 'companyId', width : 200, align: 'center',metadata:'companyId'},
			{display: '类别名称', name : 'className', width : 200, align: 'center',metadata:'className'}
			],
//		buttons : [
//		           {name: '添加', bclass: 'add',onpress: addCate},
//		           {name: '删除', bclass: 'delete', onpress: delCate},
//		           {name: '导出', bclass: 'export',onpress: editCate}
//			],
		sortname: "c3",
		sortorder: "asc",
		usepager: true,
		title: '报表列表',
		useRp: true,
		rp: 20,
		nomsg:"没有数据",
		showTableToggleBtn: true,
		pagetext: '第',
		itemtext: '页',
		outof: '页 /共',
		width: 700,
		height: 350,
		pagestat:' 显示 {from} 到 {to}条 / 共{total} 条',
		procmsg:'正在加载数据，请稍候...'

	});
	
	function addCate(name,grid)
    {
		
		
//    	$.ajax({
//    	    url:$.appClient.generateUrl({ESReport:'insert'},'x'),
//    	    success:function(data){
//    	    	dia1 =$.dialog({
////    		    	title:'添加面板',
//    		    	title:'添加报表模版',
//    	    		width: '500px',
//    	    	    height: '250px',
//    	    	   	fixed:true,
//    	    	    resize: false,
//    	    	    padding:0,
//    		    	content:data,
//    		    	init : function() {
//    					$('#addReport').autovalidate();
//    				},
//    		    	button: [
//    		 	            {id:'btnStart', name: '开始上传', disabled: true, callback: function(){return false;}}
//    		 			],
//    		 		cancelVal: '关闭',
//    		 		cancel: true
//    		    });},
//    		    cache:false
//    	});
    };
    function delCate(name,grid)
    {
    	
    };
    function editCate(obj)
    {
    	var tr = obj.closest("tr");
    	var cateId = tr.prop("id").substr(3);
		var className = $("#documentClassTable").flexGetColumnValue(tr,['className']);
    	$.ajax({
		    url : $.appClient.generateUrl({ESDocumentClass : 'edit'},'x'),
		    type:'post',
		    data:{cateId:cateId,className:className},
		    success:function(data){
			     $.dialog({
				    	title:'编辑文件类型',
			    	   	fixed:false,
			    	    resize: false,
			    	    lock : true,
						opacity : 0.1,
				    	content:data,
					    cancelVal: '关闭',
					    cancel: true,
					    okVal:'保存',
					    zIndex:120,
					    ok:true,
					    ok:function()
				    	{   
//					    	if(!$('#documentClassEditForm').validate()){return false;}//校验表单是否合法
						    	var Actionurl = $.appClient.generateUrl({ESDocumentClass : 'editCate'}, 'x');
						    	var roleData = $("#documentClassEditForm").serialize();
//					    		var id=$("#documentClassTable input[name='roleId']").val();
//					        else{
					    		$.post(Actionurl,{data : roleData}, function(res){
					    			if (res == 'true') {
					    				$.dialog.notice({icon : 'succeed',content : '修改成功',title : '3秒后自动关闭',time : 3});
					    				$("#documentClassTable").flexReload();
					    				return;
					    			} else {
					    				$.dialog.notice({icon : 'error',content : '修改失败',title : '3秒后自动关闭',time : 3});
					    				return;
					    			}
					    		});
//						    }
//						 },
//						init: function(){
//							$('#documentClassEditForm').autovalidate();
						}
				    });
			    },
			    cache:false
		});
    	
    	
    };
	
});*/

$(function(){
	
	 
//	 getClassList();
//	 $("#getIndex").click(function(){
//	//	$("#ESDCparentId").val("");	
//		 getClassList();
//
//		});
//	 function getClassList(){
//		 //一上来就滞空
//		 $("#ESDocumentClassSetDiv").empty();
//			var url = $.appClient.generateUrl({ESDocumentClass : "getClassList"}, 'x');
//			$.ajax({
//				url:url,
//				type:"post",
//			//	data:{cateId:cateId,className:className},
//				 success:function(data){
//					var json =  $.parseJSON(data);
//					var temp = 1;
//					$(json).each(function(index,item){
//						var html = '<div class = "storelogomoudle"  value = "'+item.id+'" name ="'+item.id+'" onclick="oneClick(this)" ondblclick="dbClick(this)"><div class = "storelogo"></div><div class = "storeCode"  isclick="0">'+item.name+'</div><input id="putname" type="text" name="" value="'+item.name+'" style="width:50px; display:none;" onblur="saveName(this)" ></div>';
//						$("#ESDocumentClassSetDiv").append(html);
//						if(temp ==1){
//							$("#ESDCparentId").val(item.pid);
//							//alert(item.pid);
//							temp++;
//						}
//					});
//				 }
//				
//			});
//	 }
	//根据pid查
	 
	
});