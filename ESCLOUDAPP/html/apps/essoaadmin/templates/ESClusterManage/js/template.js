
//根据客户端不同屏幕分辩率做到自适应,并将宽高存在window根对象中
(function (){

	window._resize = function (){
		
		var width = $(document).width()*0.96;				// 可见总宽度
		var height = $(document).height()-176 - 77;	// 可见总高度 - 176为平台头部高度
		
			if(navigator.userAgent.indexOf("MSIE 6.0")>0){
			
			width = width-6;	// 6为兼容IE6
				
			}else if(navigator.userAgent.indexOf("MSIE 8.0")>0){
				width = width-4;	// 4为兼容IE8
				height = height-4;
			}
			
			// IE7.0因插件高度未定导致行高和其它浏览器不一致多出4px
			height = navigator.userAgent.indexOf("MSIE 7.0")>0 ? height + 4 : height;
			_size = {width: width, height: height};
	};
	
})();

_resize();

$(document).ready(function(){
	$("#estabs").esTabs("open", {title:"集群设置", content:"#ESClusterManageDiv"});
	$("#estabs").esTabs("onTopToggle", sizeChanged);
	
	// 生成表格
	$("#ESClusterManageTable").flexigrid({
		url: $.appClient.generateUrl({ESClusterManage:'getDataList'},'x'),
		dataType: 'json',
		minwidth: 20,
		colModel : [
			{display: '', name: 'rownum', width: 20, align: 'center'},
			{display: '编辑', name : 'editbtn', width : 60, align: 'center'},
			{display: '集群名称', name : 'name', width : 260, sortable : true, align: 'left', validate:/^\d+$/i, validateMsg:"必须为数字"},
			{display: 'IP地址', name : 'ip', width : 200, sortable : true, align: 'left'},
			{display: '端口号', name : 'port', width : 100, sortable : true, align: 'left'},
			{display: '最大人数', name : 'maxPerson', width : 100, sortable : true, align: 'left'},
			{display: '是否启用', name : 'enable', width : 80, sortable : true, align: 'left'}
		],
		buttons : [
		           {name: '添加', bclass: 'add' ,onpress:addCluster}
			],
		usepager: true,
		title: '数据列表',
		useRp: true,
		rp: 20,
		nomsg:"没有数据",
		showTableToggleBtn: true,
		pagetext: '第',
		itemtext: '页',
		outof: '页 /共',
		width: _size.width,
		height: _size.height,
		pagestat:' 显示 {from} 到 {to}条 / 共{total} 条',
		procmsg:'正在加载数据，请稍候...'
	});
    
    function addCluster(){
		var url=$.appClient.generateUrl({ESClusterManage:'addClusterHtm'},'x');
		$.post(url,function(result){
			$.dialog({
				content:result,
				title:'添加',
				ok:true,
				okVal:'确定',
				cancel:true,
				cancelVal:'取消',
				ok:function()
				{
					var validate = $("#clusterForm").validate();
					if(!validate){
						return false;
					}
					var bol = true;
					$.ajax({
						url : $.appClient.generateUrl({ESClusterManage:'saveCluster'},'x'),
						type : 'post',
						async:false,
						data:{TITLE:$("#createClusterTitleId").val(),IP:$("#createClusterIpId").val(),PORT:$("#createClusterPortId").val(),MAXUSE:$("#createClusterMaxUseId").val(),USED:$("#createClusterUsedId").is(':checked')},
						success : function(result) {
							var resultJson = eval("("+result+")");
							if(resultJson.success=='true'){
								$.dialog.notice({width: 150,content: '保存成功',icon: 'succeed',time: 3});
								$("#ESClusterManageTable").flexOptions({newp:1}).flexReload();
							}else{
								bol = false;
								if(resultJson.msg == 'title'){
									$.dialog.notice({width: 150,content: '此集群名称已被占用',icon: 'error',time: 3});
								}else if(resultJson.msg == 'IPandPORT'){
									$.dialog.notice({width: 150,content: '当前IP下的此端口已经被占用',icon: 'error',time: 3});
								}else{
									$.dialog.notice({width: 150,content: '保存失败',icon: 'error',time: 3});
								}
							}
						}
					});
					if(!bol){
						return false;
					}
				}
			});
		});
    }
	
	
	$("#ESClusterManageTable span[class='editbtn']").die().live('click',function(){
		var clusterId = $(this).attr("clusterId");
		var url=$.appClient.generateUrl({ESClusterManage:'editClusterHtm'},'x');
		$.post(url,{clusterid:clusterId},function(result){
			$.dialog({
				content:result,
				title:'编辑',
				ok:true,
				okVal:'确定',
				cancel:true,
				cancelVal:'取消',
				ok:function()
				{
					var validate = $("#clusterForm").validate();
					if(!validate){
						return false;
					}
					$.ajax({
						url : $.appClient.generateUrl({ESClusterManage:'updateCluster'},'x') ,
						type : 'post',
						data:{ID:clusterId,TITLE:$("#createClusterTitleId").val(),IP:$("#createClusterIpId").val(),PORT:$("#createClusterPortId").val(),MAXUSE:$("#createClusterMaxUseId").val(),USED:$("#createClusterUsedId").is(':checked')},
						success : function(result) {
							var resultJson = eval("("+result+")");
							if(resultJson.success=='true'){
								$.dialog.notice({width: 150,content: '修改成功',icon: 'succeed',time: 3});
								$("#ESClusterManageTable").flexOptions({newp:1}).flexReload();
							}else{
								$.dialog.notice({width: 150,content: '修改失败',icon: 'error',time: 3});
							}
						}
					});
				}
			});
		});
	});
	$("#ESClusterManageTable span[class='delbtn']").die().live('click',function(){
		var clusterId = $(this).attr("clusterId");
		$.dialog({
			content:'确定要删除吗?',
			ok:true,
			okVal:'确定',
			cancel:true,
			cancelVal:'取消',
			ok:function()
			{
				var url=$.appClient.generateUrl({ESClusterManage:'delCluster'},'x');
				$.post(url,{clusterid:clusterId},function(result){
					var resultJson = eval("("+result+")");
					if(resultJson.success=='true'){
						$.dialog.notice({width: 150,content: '删除成功',icon: 'succeed',time: 3});
						$("#ESClusterManageTable").flexOptions({newp:1}).flexReload();
					}else{
						$.dialog.notice({width: 150,content: '删除失败',icon: 'error',time: 3});
					}
				});
			}
		});
	});
	
	
	/**
	 * 去左空格; 
	 * @author ldm
	 */
	function ltrim(s){
		return s.replace( /^\s*/, ""); 
	} 
	/**
	 * 去右空格; 
	 * @author ldm
	 */
	function rtrim(s){
		return s.replace( /\s*$/, ""); 
	} 
	/**
	 * 去左右空格; 
	 * @author ldm
	 */
	function trim(s){
		return rtrim(ltrim(s)); 
	}
	/**
	 * 
	 * 改变浏览器尺寸
	 */ 
	function sizeChanged(){
		if($.browser.msie && $.browser.version==='6.0'){
			$("html").css({overflow:"hidden"});
		}
		var h = $(window).height() - $("#ESClusterManageDiv").position().top;
		var flex = $("#ESClusterManageTable").closest("div.flexigrid");
		var bDiv = flex.find('.bDiv');
	    var contentHeight = bDiv.height();
	    var headflootHeight = flex.height() - contentHeight; 
	    
	    bDiv.height(h - headflootHeight);
		flex.height(h);

		// 修改IE表格宽度兼容
		if($.browser.msie && $.browser.version==='6.0'){
			flex.css({width:"-=3px"});
		}
	};
	sizeChanged();
});
