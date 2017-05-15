
function loadFilePropTbl(){
	var width = 847;
	if(navigator.userAgent.indexOf("MSIE 6.0")>0){
			width = 845;	// 6为兼容IE6
	}
	$("#filePropTable").flexigrid({
		url : false,
		dataType : 'json',
		colModel : [
		        {display: '', name : 'linenumber', width : 20, align: 'center'}, 
				{display:'操作', name:'operate', width:50,align:'center'},
				{display:'字段名', name:'name', width:150,align:'center'},
				{display:'类型', name:'type', width:50, align:'center'},
				{display:'必填项', name:'isnull', width:45,align:'center'},
				{display:'字段长度', name:'length', width:60,align:'center'},
				{display:'小数位数', name:'dotLength', width:50, align:'center'},
				{display:'系统字段', name:'issystem', width:45, align:'center'},
				{display:'描述', name:'description', width:230,editable:true, align:'center'}
		],
		resizable:false,
		rp:5,
		rpOptions: [5, 10, 20],
		usepager: true,
		width:width,
		height:162
	});
}

function boxListQuery(){
	var keyword = $.trim($('input[name="boxListQuery"]').val());
	if(keyword=='请输入关键字') {
		keyword = '';
	}
	$("#filePropTable").flexOptions({newp:1,query:keyword}).flexReload();
}

$(document).keydown(function(event){
	if(event.keyCode == 13) {
		var activeElementName = document.activeElement.name ;
		 if(activeElementName == 'boxListQuery'){
			boxListQuery();
		}else{
			return false;
		}
	}
});

function addFileProp(){
	$.ajax({
	    url:$.appClient.generateUrl({ESFileProperties:'addFileProphtm'},'x'),
	    success:function(html){
	    	$.dialog({
		    	title:'添加',
		    	padding:'0',
			    content:html,
			    cancel:true,
			    cancelVal:'关闭',
			    zIndex:110,
		    	button:[{
					name:"保存",
					callback:function(){
						var validate = fptextValidate(null,0);
						if(validate == false){
							return false;
						}
						var url=$.appClient.generateUrl({ESFileProperties:'addFileProp'},'x');
						var filePropTitle = $("#filePropTitleId").val();
						var filePropType = $("#filePropTypeId").val();
						var filePropIsNull = $("#filePropIsNullId").val();
						var filePropLength = $("#filePropLengthId").val();
						var filePropDotlength = $("#filePropDotlengthId").val();
						var filePropDec = $("#filePropDecId").val();
						$.post(url,{TITLE:filePropTitle,TYPE:filePropType,ISNULL:filePropIsNull,LENGTH:filePropLength,DOTLENGTH:filePropDotlength,DESCRIPTION:filePropDec},function(result){
							var resultJson = eval("("+result+")");
							if(resultJson.success=='true'){
								$.dialog.notice({width: 150,content: '保存成功',icon: 'succeed',time: 3});
								$('#filePropTable').flexReload();
							}else{
								$.dialog.notice({width: 150,content: '保存失败',icon: 'error',time: 3});
							}
						});
						
						return true;
					}
				}]
		    });
	    },
		cache:false
	});
}
function editFileProp(fileId){
	var url = $.appClient.generateUrl({ESFileProperties:'getEditFileProp'},'x');
	$.post(url,{fileId:fileId},function(html){
		$.dialog({
	    	title:'修改属性',
	    	padding:'0',
		    content:html,
		    cancel:true,
		    cancelVal:'关闭',
		    zIndex:110,
	    	button:[{
				name:"修改",
				callback:function(){
					var validate = fptextValidate(null,0);
					if(validate == false){
						return false;
					}
					var url=$.appClient.generateUrl({ESFileProperties:'editFileProp'},'x');
					var filePropTitle = $("#filePropTitleId").val();
					var filePropType = $("#filePropTypeId").val();
					var filePropIsNull = $("#filePropIsNullId").val();
					var filePropLength = $("#filePropLengthId").val();
					var filePropDotlength = $("#filePropDotlengthId").val();
					var filePropDec = $("#filePropDecId").val();
					$.post(url,{ID:fileId,TITLE:filePropTitle,TYPE:filePropType,ISNULL:filePropIsNull,LENGTH:filePropLength,DOTLENGTH:filePropDotlength,DESCRIPTION:filePropDec},function(result){
						var resultJson = eval("("+result+")");
						if(resultJson.success=='true'){
							$.dialog.notice({width: 150,content: '修改成功',icon: 'succeed',time: 3});
							$('#filePropTable').flexReload();
						}else{
							$.dialog.notice({width: 150,content: '修改失败',icon: 'error',time: 3});
						}
					});
					
					return true;
				}
			}]
	    });
	});
}
function delFileProp(fileId){
	$.dialog({
		content:'确定要删除吗?',
		ok:true,
		okVal:'确定',
		cancel:true,
		cancelVal:'取消',
		zIndex:110,
		ok:function()
		{
			var url=$.appClient.generateUrl({ESFileProperties:'delFileProp'},'x');
			$.post(url,{fileId:fileId},function(result){
				var resultJson = eval("("+result+")");
				if(resultJson.success=='true'){
					$.dialog.notice({width: 150,content: '删除成功',icon: 'succeed',time: 3});
					$('#filePropTable').flexReload();
				}else{
					$.dialog.notice({width: 150,content: '删除失败',icon: 'error',time: 3});
				}
			});
		}

	});
}

//验证表单信息
$("#filePropMainId input[type='text']").die();
$("#filePropMainId :text").live("focus",function(){
	$(this).removeClass("borderRed");
});
$("#filePropMainId :text").live("blur",function(){
	fptextValidate(this,1);
});
$("#filePropTypeId").die().live("change",function(){
	var val = $("#filePropTypeId").val();
	if(val=='浮点'){
		$("#filePropDotlengthId").attr("disabled",false);
		$("#filePropDotlengthId").css("background-color","white");
		$("#filePropDotlengthId").attr("value","");
		if($("#filePropDotlengthId").val()==0){
			$("#filePropDotlengthId").addClass("borderRed");
		}
	}else{
		$("#filePropDotlengthId").removeClass("borderRed");
		$("#filePropDotlengthId").attr("disabled",true);
		$("#filePropDotlengthId").css("background-color","#f2f2f5");
		$("#filePropDotlengthId").attr("value",0);
	}
});
function fptextValidate(obj,all){
	var bol = true;
	var isAll = false;
	if(all==0){
		isAll = true;
	}
	if(isAll){
		obj = $("#filePropTitleId");
	}
	if($(obj).attr("id")=="filePropTitleId"){//字段名
		if($(obj).val().length==0 || $(obj).val().length>20){
			$(obj).addClass("borderRed");
			bol = false;
		}
		var reg=/^([\u4e00-\u9fa5]|[a-zA-Z0-9_])+$/;
		if($(obj).val().search(reg)==-1){
			$(obj).addClass("borderRed");
			bol = false;
    	}
	}
	if(isAll){
		obj = $("#filePropLengthId");
	}
	if($(obj).attr("id")=="filePropLengthId"){//字段长度
		if($(obj).val().length==0 || $(obj).val().length>4000){
			$(obj).addClass("borderRed");
			bol = false;
		}
		var reg = /^\d+$/i;
		if($(obj).val().search(reg)==-1){
			$(obj).addClass("borderRed");
			bol = false;
    	}
	}
	if(isAll){
		obj = $("#filePropDotlengthId");
	}
	if($(obj).attr("id")=="filePropDotlengthId"){//小数位数
		if($(obj).val().length==0 || $(obj).val().length>14){
			$(obj).addClass("borderRed");
			bol = false;
		}
		if($("#filePropTypeId").val()=='浮点'){
			if($(obj).val()==0){
				$(obj).addClass("borderRed");
				bol = false;
			}
			var reg =  /^[1-9 ]+$/;
			if($(obj).val().search(reg)==-1){
				$(obj).addClass("borderRed");
				bol = false;
	    	}
		}
	}
	return bol;
}
