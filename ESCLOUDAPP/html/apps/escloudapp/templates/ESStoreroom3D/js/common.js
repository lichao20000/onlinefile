(function(){
	
	    var lv = 0;
	    var lvName = ["库房","排架","列","层"];
	    var lvClass = ["storelogo","shelflogo","collogo","layerlogo"];
		var lvClick = ["storelogomoudle","shelflogomoudle","collogomoudle","layerlogomoudle","boxlogomoudle"];
	    
		$(document).ready(function(){
			$.appClient.generateUrl({
				ESIdentify : "getStoreStructure"
			}, 'x');
		});
		
		/**  现在使用3D功能展现将该层分为4个点击事件 分别绑定不同的class **/
		
		/**  库房点击事件 **/
		/**
		 * update by gaoyide 20140919
		 * 和bug1139问题一样
		 */
		$(".storelogomoudle").die().live('click',function(){
			var parentId = $(this).attr("value");
			var curName = $(this).attr("name");
			var url = $.appClient.generateUrl({ESIdentify:'getRepositoryListImgByParentId'},'x');
			lv++;
			if(lv<4){
				$.get(url,{parentId:parentId},function(result){
					 var bufferHrml = "";
					 var resValue = $.parseJSON(result);
					 var tableContainer = "<table><tr>";
					 for(var i=0; i<resValue.length; i++){  
						 if(resValue[i].name.length>=3){
							 var temp = resValue[i].name.substr(0,2);
							 bufferHrml += "<td><div class = \"shelflogomoudle\" value = '"+resValue[i].id+"' name = '"+resValue[i].name+"' espath = '"+resValue[i].espath+"'><div class = '"+lvClass[lv]+"'></div><div class = 'storeCode' title='"+resValue[i].name+"'>"+lvName[lv]+"["+temp+"...]"+"</div></div></td>";
						 }else{
							 bufferHrml += "<td><div class = \"shelflogomoudle\" value = '"+resValue[i].id+"' name = '"+resValue[i].name+"' espath = '"+resValue[i].espath+"'><div class = '"+lvClass[lv]+"'></div><div class = 'storeCode'>"+lvName[lv]+"["+resValue[i].name+"]"+"</div></div></td>";
						 }
						 //bufferHrml += "<td><div class = \"shelflogomoudle\" value = '"+resValue[i].id+"' name = '"+resValue[i].name+"' espath = '"+resValue[i].espath+"'><div class = '"+lvClass[lv]+"'></div><div class = 'storeCode'>"+lvName[lv]+"["+resValue[i].name+"]"+"</div></div></td>";
					 }
					 tableContainer = tableContainer+bufferHrml+"</tr></table>"
					 $("#storeContainer").html(bufferHrml);
					 $("#storeContainer").css("margin-top",$(document).height()/2-200);
					 $("#storeContainer").css("height",$(document).height() - 280);
				});
				var navContainer = $("#navContainer");
				var tmpHtml = navContainer.html();
				navContainer.html(tmpHtml + "<div class = 'connector'>-></div><div class = 'nav' value = '"+parentId+"' name = '"+lvName[lv-1]+"["+curName+"]"+"'>"+lvName[lv-1]+"["+curName+"]"+"</div>");
			}else{
				 clear();
				 $(this).children(".layerlogo").addClass("activeSelected");
			}
		});
		
		/** 排架点击事件 **/
		$(".shelflogomoudle").die().live('click',function(){
			var parentId = $(this).attr("value");
			var curName = $(this).attr("name");
			var url = $.appClient.generateUrl({ESStoreroom3D:'getRepositoryListForColAndLayer'},'x');
			lv++;
			if(lv<4){
				$.get(url,{parentId:parentId},function(result){
					
					var json = eval('('+result+')');
					var tableHtml = "<table border='0' cellspacing='0' cellpadding='0'><tr>";
					var countCol = 1;
					for (var key in json)
					{
						
						tableHtml += "<td><div class = 'shelfCol'>";
					    for(var i=0; i<json[key].length; i++){ 
					    	var title = json[key][i].gridsurpluswidth==""?"":"剩余宽度"+json[key][i].gridsurpluswidth+"CM";
					    	tableHtml += "<div class = 'shelfLayer' title='"+title+"'>";
					    	/** 内部组装盒 **/
					    	if(json[key][i].boxesCounter > 0){
					    		var boxes = eval('('+json[key][i].boxes+')');
					    		var q = 1;
					    		for (var box in boxes)
								{
					    			tableHtml+="<div class = 'boxCls' title='第"+q+"盒,货架号:"+boxes[box].boxpath+"' repositorypath = '"+boxes[box].repositorypath+"' boxPath = '"+boxes[box].boxpath+"' > </div>";
					    			q ++;
								}
					    	}
					    	tableHtml +="</div><div class = 'shelfDetails'>"+(i+1) + "层" + countCol + "列" + "</div>";
					    }
						tableHtml += "</div></td>";
						countCol++;  
					}
					tableHtml += "</tr></table>";
					 $("#storeContainer").html(tableHtml);
				});
				$("#storeContainer").css("margin-top",0);
				$("#storeContainer").css("height",$(document).height() - 160);
				var navContainer = $("#navContainer");
				var tmpHtml = navContainer.html();
				navContainer.html(tmpHtml + "<div class = 'connector'>-></div><div class = 'nav' value = '"+parentId+"' name = '"+lvName[lv-1]+"["+curName+"]"+"'>"+lvName[lv-1]+"["+curName+"]"+"</div>");
			}else{
				 clear();
				 $(this).children(".layerlogo").addClass("activeSelected");
			}
		});
		
		
		 /**  绑定导航条的绑定事件 **/
		/**
		 * update by gaoyide 20140919
		 * 和bug1139问题一样
		 */
		$(".nav").die().live('click',function(){
			var parentId = $(this).attr("value");
			var curName= $(this).attr("name");
			if(null != curName && curName.indexOf("库房")>=0){
				lv = 1;
			}else if(null != curName && curName.indexOf("排架")>=0){
				lv = 2;
			}else if(null != curName && curName.indexOf("列")>=0){
				lv = 3;
			}else if(null != curName && curName=="首页"){
				lv = 0;
			}
			var url = $.appClient.generateUrl({ESIdentify:'getRepositoryListImgByParentId'},'x');
			$.get(url,{parentId:parentId},function(result){
				 var bufferHrml = "";
				 var resValue = $.parseJSON(result);
				 var tableContainer = "<table><tr>";
				 for(var i=0; i<resValue.length; i++){ 
					 if(lv == 1){
						 if(resValue[i].name.length>=3){
							 var temp = resValue[i].name.substr(0,2);
							 bufferHrml += "<div class = \""+lvClick[lv]+"\" value = '"+resValue[i].id+"' name = '"+resValue[i].name+"' espath = '"+resValue[i].espath+"'><div class = '"+lvClass[lv]+"'></div><div class = 'storeCode'>"+lvName[lv]+"["+temp+"...]"+"</div></div>";
						 }else{
							 bufferHrml += "<td><div class = \""+lvClick[lv]+"\" value = '"+resValue[i].id+"' name = '"+resValue[i].name+"' espath = '"+resValue[i].espath+"'><div class = '"+lvClass[lv]+"'></div><div class = 'storeCode'>"+lvName[lv]+"["+resValue[i].name+"]"+"</div></div></td>";
						 }
						// bufferHrml += "<td><div class = \""+lvClick[lv]+"\" value = '"+resValue[i].id+"' name = '"+resValue[i].name+"' espath = '"+resValue[i].espath+"'><div class = '"+lvClass[lv]+"'></div><div class = 'storeCode'>"+lvName[lv]+"["+resValue[i].name+"]"+"</div></div></td>";
					 }else{
						 if(resValue[i].name.length>=3){
							 var temp = resValue[i].name.substr(0,2);
							 bufferHrml += "<div class = \""+lvClick[lv]+"\" value = '"+resValue[i].id+"' name = '"+resValue[i].name+"' espath = '"+resValue[i].espath+"'><div class = '"+lvClass[lv]+"'></div><div class = 'storeCode'>"+lvName[lv]+"["+temp+"...]"+"</div></div>";
						 }else{
							 bufferHrml += "<div class = \""+lvClick[lv]+"\" value = '"+resValue[i].id+"' name = '"+resValue[i].name+"' espath = '"+resValue[i].espath+"'><div class = '"+lvClass[lv]+"'></div><div class = 'storeCode'>"+lvName[lv]+"["+resValue[i].name+"]"+"</div></div>";
						 }
						 //bufferHrml += "<div class = \""+lvClick[lv]+"\" value = '"+resValue[i].id+"' name = '"+resValue[i].name+"' espath = '"+resValue[i].espath+"'><div class = '"+lvClass[lv]+"'></div><div class = 'storeCode'>"+lvName[lv]+"["+resValue[i].name+"]"+"</div></div>";
					 }
				 }
				 if(lv == 1){
					 bufferHrml = tableContainer + bufferHrml + "</tr></table>"
				 }
				 $("#storeContainer").html(bufferHrml);
			});
			var navContainer = $("#navContainer");
			var tmpHtml = navContainer.html();
			tmpHtml = tmpHtml.substring(0,tmpHtml.lastIndexOf(curName)+curName.length+6);
			navContainer.html(tmpHtml);
			
		});
		
		/** 盒子点击事件用来查看盒子内部的条目数据 **/
		$(".boxCls").die().live('click',function(){
			var boxPath = $(this).attr("boxPath");
			$.ajax({
			    url : $.appClient.generateUrl({
			    ESStoreroom3D : 'showArchiveList',boxPath:boxPath},'x'),
			    success:function(data){
			    	$.dialog({
				    	title:'条目数据',
			    	   	fixed:false,
			    	    resize: false,
			    	    lock : true,
						opacity : 0.1,
				    	okVal:'保存',
					    ok:true,
					    cancelVal: '关闭',
					    cancel: true,
					    content:data,
					    width:500,
					    height:500,
					    ok:function()
				    	{
					    	
				    	}
			    	});
			    }
			});
//			var url = $.appClient.generateUrl({ESStoreroom3D:'getBoxContentsByBoxPath'},'x');
//			$.get(url,{boxPath:boxPath},function(result){
//				
//			});
		});
		
		
		/**  用于选择层时候调整样式  **/
		function clear() {
		    $(".layerlogomoudle").each(function() {
		        if ($(this).children(".layerlogo").hasClass("activeSelected")) {
		            $(this).children(".layerlogo").removeClass("activeSelected");
		        }
		    });
		}

		
})()
