$(function(){
	$('#class_add').on('click',function(){
		var classId = $("#selectFileClassId").val();
		if(classId != 1){
			$('#grouplable').hide();
			$('#createGroupUserlist').hide();
		}else{
			$('#grouplable').show();
			$('#createGroupUserlist').show();
		}
		if($('#addClassDiv').css('display') =='none'){
			$('#addClassDiv').show();
		}else{
			$('#addClassDiv').hide();
		}
	});
	//点击创建
	$("#createClass").on('click',function(){
		createClassFile();
	});
	//键盘回车创建
	$('#addClassDiv').on('keydown','#groupnameid',function(event){
		if(event.keyCode==13){
			createClassFile();
		}
	});
	//创建分类的具体方法
	function createClassFile(){
		var companyId = $("#flyingchat").attr("companyId") ;
		var className = $("#groupnameid").val();
		var classId = $("#selectFileClassId").val();
		var userId = $('#selectUserId').val(); //表id
		//alert(classId);
		if(className == ""){
			$("#groupnamedivid").showTooltips("请输入分类名称！", 5000) ;
			return;
		}
		if(classId != 1){//如果不是第一层分类
			var url = $.appClient.generateUrl({ESDocumentClass:'addClassByName'},'x');
			$.ajax({
				url:url,
				type:'POST',
				data:{className:className,companyId:companyId,classId:classId,userId:userId},
				success:function(data){
					var success = $.parseJSON(data)
					if(success.success == 'true'){
						showMsg("创建分类成功！", "1");
						$('#addClassDiv').hide();
						$('#addClassDiv input').val('');
						documentCenter.getFileList(classId, "0");
					}
					if(success.success == 'false'){
						showMsg("创建分类失败！", "2");
					}
					if(success.ishave == 'true'){
						showMsg("已存在该分类名！", "2");
					}
				}
			});
		}else{//如果是第一层分类说明是存在成员的
			var username = $("#flyingchat").attr("username");
			var groupname = $("#groupnameid").val();
			if(groupname == ''){
				$("#groupnamedivid").showTooltips("分类名称不能为空！", 5000) ;
				return false;
			} else if($.trim(groupname) == ''){
				$("#groupnamedivid").showTooltips("分类名称不能全为空格！", 5000) ;
				return false;
			}
			var groupuserids = "" ;
			var groupusernames = "" ;
			var hasSelf = false ;
			$('#addClassDiv').find("input[type='checkbox'][checked]").each(function(){
				if($(this).attr('pass')!="true"){
					groupuserids += $(this).attr('userid')+"," ;
					groupusernames += $(this).attr('username')+"," ;
					if($(this).attr('username') == username){
						hasSelf = true ;
					}
				}
			});
			if(!hasSelf){
				$("#grouplable").showTooltips("您自己必须在新建的分类群组中！", 5000) ;
				return false;
			}
			if(groupuserids == ""){
				$("#grouplable").showTooltips("请给当前群组设置分类群组成员！", 5000) ;
				return false ;
			}
			groupuserids = groupuserids.substring(0, groupuserids.length-1) ;
			groupusernames = groupusernames.substring(0, groupusernames.length-1) ;
			var groupnames = encodeURI($.trim(groupname), "utf-8");
			var groupremarks = encodeURI($("#groupremarkid").val(), "utf-8");
			//创建分组！！此方法写在了ESClass里面
			var url = $.appClient.generateUrl({ESDocumentClass:'addClassByNameAndCreateGroup'},'x');
			$.ajax({
				url:url,
				type:'POST',
				data:{className:className,companyId:companyId,classId:classId,username:username,userId:userId,groupuserids:groupuserids,manageruserid:$("#manageruserid").val(),groupname:groupnames,groupremark:groupremarks},
				success:function(data){
					var success = $.parseJSON(data)
					if(success.success == 'true'){
						$('#addClassDiv').hide();
						$('#addClassDiv input').val('');
						showMsg("创建分类成功！", "1");
						documentCenter.getFileList(classId, "0");
						$.WebIM.sendmMsg(success.flag,groupnames,groupnames);
					}
					if(success.success == 'have'){
						showMsg("已存在该分类名！", "2");
					}
					if(success.success == 'false'){
						showMsg("创建分类失败！", "2");
					}
				}
			});
			
		}
	}
	
	
	$('#createClassOut').on('click',function(){
		
		$('#createClassName').val("");
		$('#addClassDiv').css("display","none");
	});
	$('#file_maincontent').on('blur','.rename_input',function(){
		var classId = $("#selectFileClassId").val();
		var companyId = $("#flyingchat").attr("companyId") ;
		var itemId = $(this).parent().parent().parent().attr('id');
		var className = $('#'+itemId).find('input').val();
		var oldCLassName = $('#'+itemId+'').attr('filename');
		editClassName(classId,companyId,itemId,className,oldCLassName);
		return false;
	});
	//回车事件保存分类名
	$('#file_maincontent').on('keydown','.rename_input',function(event){
		 if(event.keyCode==13){
			var itemId = $(this).parent().parent().parent().attr('id');
			$('#'+itemId+' .file_title a').show();
			$('#'+itemId+' .file_title input').hide();
			
		 }
	});
	//修改分类名的方法
	function editClassName(classId,companyId,itemId,className,oldCLassName){
		//没修改直接返回
		if($.trim(className)  == $('#'+itemId+' .file_title a').html()){
			$('#'+itemId+' .file_title a').show();
			$('#'+itemId+' .file_title input').hide();
			return false;
		}
		if($.trim(className) == ''){
			$('#'+itemId+' .file_title a').show();
			$('#'+itemId+' .file_title input').hide();
			return false;
		}
		var url = $.appClient.generateUrl({ESDocumentClass:'reClassNameById'},'x');
//		alert(className);
		$.ajax({
			url:url,
			type:'POST',
			data:{itemId:itemId,className:className,companyId:companyId,oldCLassName:oldCLassName},
			success:function(data){
				var success = $.parseJSON(data)
				if(success.success == 'true'){
					$('#'+itemId+' .file_title a').show();
					$('#'+itemId+' .file_title input').hide();
					//刷新列表
					documentCenter.getFileList(classId, "0");
					showMsg("修改成功");
				}else{
					showMsg("修改失败");
				}
			}

		});
		$('#menu_custom').remove();return false;
	}
	
	$("#file_list").on('click', 'input.rename_input', function(e){
		e.stopPropagation();
	});
	
	$('#file_maincontent').on('click', '#class_delete', function(){
		var classId = $("#selectFileClassId").val();
		var companyId = $("#flyingchat").attr("companyId") ;
		var itemId = $(this).attr('target-id');
		var userId = $('#selectUserId').val();
		//删除时暂时只能创建者和管理员能删除  进行判断
		var isAdmin = $('#isAdmin').val();
		var oldCLassName = $('#'+itemId).attr('filename');
		$.dialog({
			content : '确定要删除吗？删除后不能恢复！',
			okVal : '确定',
			ok : true,
			cancelVal : '关闭',
			cancel : true,
			ok : function() {
					var url = $.appClient.generateUrl({ESDocumentClass : 'deleteClassById'}, 'x');
					$.post(url, {itemId:itemId,companyId:companyId,userId:userId,isAdmin:isAdmin,oldCLassName:oldCLassName}, function(res) {
						var success = $.parseJSON(res);
						if(success.success == 'true'){
							showMsg("删除分类成功！");
							documentCenter.getFileList(classId, "0");
						}else if(success.success == "havechildren"){
							showMsg("该分类下存在文件或分类！");
						}else if(success.success == "nocreator"){
							showMsg("您无权删除此分类！", "2");
						}
					});
			}
		});
		$('#menu_custom').remove();
	});
	
	$('#file_maincontent').on('click', '#class_rename', function(){
		
		var itemId = $(this).attr('target-id');
		$('#'+itemId+' .file_title a').hide();
		var className = $('#'+itemId+' .file_title a').html();
		$('#'+itemId+' .file_title input').show();
		$('#'+itemId+' .file_title input').val(className);
		$('#'+itemId+' .file_title input').select().focus();	
		
		$('#menu_custom').remove();
	});
	
	
	
	//-----------------------------------------------*成员管理*------------------------------------------------/
	$("#file_maincontent").on('click','#class_user',function(){
		var itemId = $(this).attr('target-id');
		var offset = $('#'+itemId+' a').offset();
		var groupid = $('#'+itemId).attr('groupid');
		var flag = $('#'+itemId).attr('flag');
		var groupName = $('#'+itemId).attr('groupName');
		var url = $.appClient.generateUrl({ESDocumentClass:'editUser'},'x');
		$.ajax({
			url:url,
			type:'POST',
			data:{itemId:itemId,groupid:groupid,flag:flag,groupName:groupName},
			success:function(data){
				var success = $.parseJSON(data)
				$('#classUserDiv #manageruserid').val(success.userId);
				$('#classUserDiv #groupid').val(success.groupid);
				$('#classUserDiv #groupflag').val(success.flag);
				$('#classUserDiv #groupnameid').val(success.groupname);
				$('#classUserDiv #groupnameid').attr("oldvalue",success.groupname);
				
				//当浏览器的高度小于按钮+div框的高度时  改变样式
				if(document.body.offsetHeight < offset.top + 320){
					$('#classUserDiv #menu_custom_class').css({top:offset.top-245+"px",left:offset.left+223+"px"});
					$('#classUserDiv .arrow_shadow').css('top','273px');
					$('#classUserDiv .arrow').css('top','273px');
				}else{
					$('#classUserDiv #menu_custom_class').css({top:offset.top+73+"px",left:offset.left+223+"px"});
					$('#classUserDiv .arrow_shadow').css('top','-5px');
					$('#classUserDiv .arrow').css('top','-5px');
				}
				//
				var baseurl = $("#flyingchat").attr("baseurl") ; 
				var companyId = $("#flyingchat").attr("companyId") ;
				var username = $("#flyingchat").attr("username") ;
				
				var url = baseurl+'/rest/chat/getCompanyUsersForGroupSet/'+companyId+'/'+username+'?callback=?';
				var data = {'userid':window.userId,groupid:groupid};
				var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
				jQuery.getJSON(url, ret.data,
						function(json) {
							$('#createGroupUserlist1').html(template('editgroupusersTemplate', json));
				});
				
				//
				$('#classUserDiv').css('display','block');
			}
		});
		
		$('#menu_custom').remove();
	});
	$('#closeUserClass').on('click',function(){
		$('#classUserDiv').css("display","none");
		$('#classUserDiv #createGroupUserlist1').empty();
	});
	//成员管理修改
	$("#modifuClassUser").on('click',function(){
		var username = $("#flyingchat").attr("username");
		var groupname = $(this).parent().find("#groupnameid").val();
//		if(groupname == ''){
//			$("#groupnamedivid").showTooltips("群组名称不能为空！", 5000) ;
//			return false;
//		} else if($.trim(groupname) == ''){
//			$("#groupnamedivid").showTooltips("群组名称不能全为空格！", 5000) ;
//			return false;
//		}
		var addgroupuserids = "" ;
		var addgroupusernames = "" ;
		var deletegroupuserids = "" ;
		var deletegroupusernames = "" ;
		var hasSelf = false ;
		$('#classUserDiv').find("input[type='checkbox'][checked]").each(function(){
			if($(this).attr('value')!="on"){
				if($(this).attr('username') == username){
					hasSelf = true ;
				}
				if(!$(this).attr('oldvalue')){
					addgroupuserids += $(this).attr('value')+"," ;
					addgroupusernames += $(this).attr('username').replace('@', '\\40')+"," ;
				}
			}
		});
		$('#classUserDiv').find("input[type='checkbox'][oldvalue]").each(function(){
			if(!$(this).attr('checked')){
				deletegroupuserids += $(this).attr('value')+"," ;
				deletegroupusernames += $(this).attr('username').replace('@', '\\40')+"," ;
			}
		});
		if(!hasSelf){
			//$("#grouplable").showTooltips("您自己必须在分类群组中！", 5000) ;
			showMsg("您自己必须在分类群组中！", "2");
			return false;
		}
		var changeusers = true ;
		if(addgroupuserids=="" && deletegroupuserids==""){
			changeusers = false ;
		} else {
			if(addgroupuserids!=""){
				addgroupuserids = addgroupuserids.substring(0, addgroupuserids.length-1) ;
				addgroupusernames = addgroupusernames.substring(0, addgroupusernames.length-1) ;
			}
			if(deletegroupuserids!=""){
				deletegroupuserids = deletegroupuserids.substring(0, deletegroupuserids.length-1) ;
				deletegroupusernames = deletegroupusernames.substring(0, deletegroupusernames.length-1) ;
			}
		}
		var changeitems = true ;
		if(!changeusers && !changeitems){
			$(".aui_state_highlight").showTooltips("您没有做任何的修改，无需保存！", 5000) ;
			return false;
		}
		
		var url = $("#flyingchat").attr("baseurl")+'/rest/chat/resetGroup?callback=?';
		var data = {'companyId':$("#flyingchat").attr("companyId"), 'username':username, 'addgroupuserids':addgroupuserids, 'deletegroupuserids':deletegroupuserids, 'groupname':encodeURI($.trim(groupname), "utf-8"), 'groupremark':encodeURI($("#groupremarkid").val(), "utf-8"), groupid:$("#groupid").val(), groupflag:$("#groupflag").val(), manageruserid:$("#classUserDiv #manageruserid").val(), changeusers:changeusers, changeitems:changeitems, "fullname":encodeURI($("#current_user_name").html(), "utf-8")};
		var ret = addSecurityPart(url, data, window.token, window.u, window.jsessionid);
		jQuery.getJSON(url, ret.data,
				function(json) {
			if(json.isOk){
				showMsg("编辑分类群组成功！", "1");
				 $('#classUserDiv').hide();
				 $('#classUserDiv #createGroupUserlist1').empty();
				if(changeusers){
					var arg = 'type=reset_group&groups='+flag+'&addgroupusernames='+addgroupusernames+'&deletegroupusernames='+deletegroupusernames;
					remote.jsjac.chat.ofuserservice(arg, false) ;
				}
				
			} else {
				showMsg("编辑分类群组失败！", "2");
			}
		});
		
	});
	//点击空白隐藏
	$(document).mouseup(function(e){
		  var _con = $('#addClassDiv');  
		  if(!_con.is(e.target) && _con.has(e.target).length === 0){
			  $('#addClassDiv').hide();
			  $('#addClassDiv input').val('');
		  }
	});
	
	//点击空白隐藏
	$(document).mouseup(function(e){
		  var _con = $('#classUserDiv');   
		  if(!_con.is(e.target) && _con.has(e.target).length === 0){
			  $('#classUserDiv').hide();
			  $('#classUserDiv #createGroupUserlist1').empty();
		  }
	});
	
	/**---------------------------------------------------------*/
	///*
	 //* 
	 //* 全新js
	 //* 
	 //* /
	$('.createClassBtn').on('click',function(){
		var groupname = $('.createClassName').val();
		if(groupname == ''){
			alert('分类名不能为空');
			return false;
		} else if($.trim(groupname) == ''){
			alert('分类名不能为空');
			return false;
		}
		var groupuserids = "" ;
		var groupusernames = "" ;
		groupuserids =  window.userId ;
		groupusernames =  window.userName ;
		var groupnames = encodeURI($.trim(groupname), "utf-8");
		var groupremarks = encodeURI($('.createClassDescription').val(), "utf-8");
		//创建分组！！此方法写在了ESClass里面
		var url = $.appClient.generateUrl({ESDocumentClass:'addClassByNameAndCreateGroup'},'x');
		$.ajax({
			url:url,
			type:'POST',
			data:{className:groupname,companyId: window.companyid,userId:userId,groupuserids:groupuserids,manageruserid:userId,groupname:groupnames,groupremark:groupremarks},
			success:function(data){
				var success = $.parseJSON(data)
				if(success.success == 'true'){
					documentCenter.getClassList(window.userId);
					$("#newClassPanel").hide(); 
					$('.createClassName').val("");
					$('.createClassDescription').val();
					//$.WebIM.sendmMsg(success.flag,groupnames,groupnames);
				}
				if(success.success == 'have'){
					alert('当前分组已存在!');
				}
				if(success.success == 'false'){
					alert('创建失败!');
				}
			}
		});
		
		
		
	});
	$('.createClassCancleBtn').on('click',function(){
		$("#newClassPanel").hide(); 
		$('.createClassName').val("");
		$('.createClassDescription').val();
	});
});

/*账户设置*/
$('#accountSetting').on('click',function(){
	var url = $.appClient.generateUrl({ESUserInfo:'user_perfect'},'x');
	$.ajax({
		url:url,
		type:'POST',
		//data:{className:className,companyId:companyid,userId:userId},
		success:function(data){
			var success = $.parseJSON(data);
			$('#fullname').val(success.FULLNAME);
			$('#mobilephone').val(success.MOBILEPHONE);
			$('#telephone').val(success.TELEOHONE);
			$('#email').val(success.EMAIL);
			$('#position').val(success.POSITION);
			$('#fax').val(success.FAX);
			$('#signature').val(success.SIGNATURE);
		},
		error:function(){
			alert("chcuo;e");
		}
	});
});
/*头像*/
function previewImage(file)
{
  var MAXWIDTH  = 260; 
  var MAXHEIGHT = 180;
  var div = document.getElementById('preview');
  if (file.files && file.files[0])
  {
      div.innerHTML ='<img id=imghead>';
      var img = document.getElementById('imghead');
      img.onload = function(){
        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
        img.width  =  rect.width;
        img.height =  rect.height;
//         img.style.marginLeft = rect.left+'px';
        img.style.marginTop = rect.top+'px';
      }
      var reader = new FileReader();
      reader.onload = function(evt){img.src = evt.target.result;}
      reader.readAsDataURL(file.files[0]);
  }
  else //兼容IE
  {
    var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
    file.select();
    var src = document.selection.createRange().text;
    div.innerHTML = '<img id=imghead>';
    var img = document.getElementById('imghead');
    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
    status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
    div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
  }
}
function clacImgZoomParam( maxWidth, maxHeight, width, height ){
    var param = {top:0, left:0, width:width, height:height};
    if( width>maxWidth || height>maxHeight )
    {
        rateWidth = width / maxWidth;
        rateHeight = height / maxHeight;
        
        if( rateWidth > rateHeight )
        {
            param.width =  maxWidth;
            param.height = Math.round(height / rateWidth);
        }else
        {
            param.width = Math.round(width / rateHeight);
            param.height = maxHeight;
        }
    }
    
    param.left = Math.round((maxWidth - param.width) / 2);
    param.top = Math.round((maxHeight - param.height) / 2);
    return param;
}

$('#uploadA').on('click',function(){
	$('.f').trigger('click');
});

$('.f').on('change',function(){
	$("#imageform").ajaxSubmit({
		url: $.appClient.generateUrl({ESUserInfo : 'uploadImage1'},'x'),
		type:"post",
		dataType:"text",
		success:function(responseText){
			var success = $.parseJSON(responseText);
			if(success.success ==true){
				$("#touxiang").attr('src',success.touxiang);
				$("#indexheadimage").attr('src',success.touxiang);  //修改完头像替换掉首页的头像
				art.dialog.list['uploadhead'].close();
			}else if(success.success ==false){
				showMsg(success.msg, "2");
			}
		},
		error:function(){
			showMsg("系统错误!", "2");
		}
	});return false;
	
});



/**   点击用户个人空间  **/
$("#accountSetting").on("click",function(){
	$('#bodyContent_Title').html(template('account_templete'));
	$('.col-md-4').hide();
	$('.col-md-8').hide();
	$('.frame').show();
});

//去左空格; 
function ltrim(s){
	return s.replace( /^\s*/, ""); 
} 
//去右空格; 
function rtrim(s){ 
	return s.replace( /\s*$/, ""); 
} 
//去左右空格; 
function trim(s){
	return rtrim(ltrim(s)); 
}
var nameZZ= /^[\u4e00-\u9fa5a-zA-Z]+$/;
var mobtelZZ =/^1[3|4|5|8][0-9]\d{8}$/;
var lengthZZ= /.{50}|^\s*$/g;
var teleZZ = /^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$/;
var emailaddressZZ = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
var fexZZ =/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/; 

$('.clearfix').on('click','#saveUser_perfect',function(){
	var signature = $('#signature').val();
	var fullname = $('#fullname').val();
	var mobilephone = $('#mobilephone').val();
	var telephone = $('#telephone').val();
	var email = $('#email').val();
	var position = $('#position').val();
	var fax = $('#fax').val();
	//理论上都可不填写 但是填写就要验证
	if(trim(fullname)==""){
		alert("中文名不能为空");
		return false;
	}else if(nameZZ.test(fullname)==false){
		alert("中文名只能是中文或字母!");
		return false;
	}
	if(trim(email) != "" && emailaddressZZ.test(email)==false){
		alert("邮箱格式不正确!");
		return false;
	}
	if(trim(mobilephone) != "" && mobtelZZ.test(mobilephone)==false){
		alert("手机号码格式不正确!");
		return false;
	}
	if(trim(telephone) != "" && teleZZ.test(telephone)==false){
		alert("公司电话格式不正确");
		return false;
	}
	if(trim(fax) != "" && fexZZ.test(fax)==false){
		alert("传真格式不正确");
		return false;
	}
	url =  $.appClient.generateUrl({ESUserInfo : 'editUserInfo'},'x');
	$.ajax({
		url:url,
		type: "POST",
		data:{
			fullname:fullname,signature:signature,mobilephone:mobilephone,telephone:telephone,email:email,position:position,fax:fax
		},
		dataType:"json",
		error:function(){
			alert("编辑个人信息失败，请重试！");
		},
		success:function(datas){	
			if(datas!=null){
				alert("编辑成功！");
			}else{
				showMsg("编辑失败！", "2");
			}
			
		}
	});return false;
});
/*改密码*/
$('#changePassword').on('click',function(){
	var oldPassword = $("#oldPassword").val();
	var newPassword = $("#newPassword").val();
	var repetPassword = $("#rePassword").val();
	if(oldPassword=='' || newPassword==''){
		alert("密码不能 为空");
		return false;
	}
	if(newPassword != repetPassword){
		alert("两次密码不一致");
		return false;
	}
	var modifyurl = $.appClient.generateUrl({ESUserInfo : 'do_changepassword'}, 'x');
	$.post(modifyurl,{oldPassword:oldPassword,newPassword:newPassword}, function(result){
		var isPasswordValid = result.isPasswordValid;//密码是否正确
		var isModifySuccess = result.isModifySuccess;//是否重置成功
		if(isPasswordValid=='true'){
			if(isModifySuccess=='1'){
				alert("修改成功!");
			}else{
				alert("修改失败!");
			}
		}else{
			alert("密码错误!");
		}
		
	},'json');
	return false;
});

/*创建子文件夹*/
$('#saveClassNoUser').on('click',function(){
	alert(123);
});