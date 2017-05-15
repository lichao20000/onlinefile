/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	//设置语言
	 config.language = 'zh-cn'; 
	 config.toolbar = 'Full';
	 config.autoUpdateElement = true;
	 config.smiley_path = 'apps/onlinefile/templates/ESUserCommunity/ckeditor/plugins/smiley/images/';
	 config.colorButton_colors = '000,800000,8B4513,2F4F4F,008080,000080,4B0082,696969,B22222,A52A2A,DAA520,006400,40E0D0,0000CD,800080,808080,F00,FF8C00,FFD700,008000,0FF,00F,EE82EE,A9A9A9,FFA07A,FFA500,FFFF00,00FF00,AFEEEE,ADD8E6,DDA0DD,D3D3D3,FFF0F5,FAEBD7,FFFFE0,F0FFF0,F0FFFF,F0F8FF,E6E6FA,FFF';
	 config.entities_additional = '#39'; //其中#代替了&
	  //工具栏是否可以被收缩
	 config.toolbarCanCollapse = false;
	 //拖拽改变尺寸
	 config.resize_enabled = false;
	 //拖拽最大宽度
//	 config.resize_maxWidth = 600; 
//	 config.resize_maxHeight = 800; 
	 //工具栏默认是否展开
	 config.toolbarStartupExpanded = true;
	 //是否转换一些难以显示的字符为相应的HTML字符 plugins/entities/plugin.js
	 config.entities_greek = true;
	 //编辑器中回车产生的标签
	 config.enterMode = CKEDITOR.ENTER_BR;
	// 编辑器的z-index值
	 config.baseFloatZIndex = 10000;
	 //设置默认字体大小
	 config.fontSize_defaultLabel = '12';
	 //设置默认字体
	 config.font_defaultLabel = '宋体'; 
	 //把上传图片的英文区域去掉
	 config.image_previewText=' '
	//去掉链接，和 高级功能
	 config.removeDialogTabs = 'image:advanced;image:Link';
	 //字体编辑时的字符集 可以添加常用的中文字符：宋体、楷体、黑体等 
	 config.font_names = '微软雅黑;宋体;新宋体;黑体;隶书;幼圆;楷体_GB2312;仿宋_GB2312;方正舒体;方正姚体;华文隶书;华文新魏;华文行楷;sans-serif;Arial;Comic Sans MS;Courier New;Tahoma;Times New Roman;Verdana' ;
	
	 config.filebrowserBrowseUrl = 'apps/onlinefile/templates/ESUserCommunity/ckfinder/ckfinder.html';
	 config.filebrowserImageBrowseUrl = 'apps/onlinefile/templates/ESUserCommunity/ckfinder/ckfinder.html?type=Images';
	 config.filebrowserFlashBrowseUrl = 'apps/onlinefile/templates/ESUserCommunity/ckfinder/ckfinder.html?type=Flash';	
	 config.filebrowserUploadUrl = 'apps/onlinefile/templates/ESUserCommunity/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files';
	 config.filebrowserImageUploadUrl = 'apps/onlinefile/templates/ESUserCommunity/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Images';
	 config.filebrowserFlashUploadUrl = 'apps/onlinefile/templates/ESUserCommunity/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Flash';
	 
	 config.toolbar_Full = [
		 ['Cut','Copy','Paste','PasteText','PasteFromWord','Undo','Redo'],
		 ['Scayt'],
		 ['Link','Unlink','Anchor'],
		 ['Image','HorizontalRule','Smiley','SpecialChar'], 
		 ['Bold','Italic','Strike','RemoveFormat'],
		 ['Source'],
		 ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'], 
		 ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'], 
		 ['Styles','Format','Font','FontSize']
		 ];
 	//设置快捷键
	  config.keystrokes = [
	                       [ CKEDITOR.ALT + 121 /*F10*/, 'toolbarFocus' ],  //获取焦点
	                        [ CKEDITOR.ALT + 122 /*F11*/, 'elementsPathFocus' ],  //元素焦点
	                       [ CKEDITOR.SHIFT + 121 /*F10*/, 'contextMenu' ],  //文本菜单
	                       [ CKEDITOR.CTRL + 90 /*Z*/, 'undo' ],  //撤销
	                        [ CKEDITOR.CTRL + 89 /*Y*/, 'redo' ],  //重做
	                        [ CKEDITOR.CTRL + CKEDITOR.SHIFT + 90 /*Z*/, 'redo' ],  //
	                        [ CKEDITOR.CTRL + 76 /*L*/, 'link' ],  //链接
	                        [ CKEDITOR.CTRL + 66 /*B*/, 'bold' ],  //粗体
	                        [ CKEDITOR.CTRL + 73 /*I*/, 'italic' ],  //斜体
	                        [ CKEDITOR.CTRL + 85 /*U*/, 'underline' ],  //下划线
	                        [ CKEDITOR.ALT + 109 /*-*/, 'toolbarCollapse' ]
	                    ]
    
    //设置快捷键 可能与浏览器快捷键冲突 plugins/keystrokes/plugin.js.
    config.blockedKeystrokes = [
        CKEDITOR.CTRL + 66 /*B*/,
        CKEDITOR.CTRL + 73 /*I*/,
        CKEDITOR.CTRL + 85 /*U*/
    ]
    
    //设置编辑内元素的背景色的取值 plugins/colorbutton/plugin.js.
    config.colorButton_backStyle = {
        element : 'span',
        styles : { 'background-color' : '#(color)' }
    }
	 //对应的表情图片 plugins/smiley/plugin.js
	 config.smiley_images = [
	 'regular_smile.gif','sad_smile.gif','wink_smile.gif','teeth_smile.gif','confused_smile.gif','tounge_smile.gif',
	 
	 'embaressed_smile.gif','omg_smile.gif','whatchutalkingabout_smile.gif','angry_smile.gif','angel_smile.gif','shades_smile.gif',
	 
	 'devil_smile.gif','cry_smile.gif','lightbulb.gif','thumbs_down.gif','thumbs_up.gif','heart.gif',
	 
	 'broken_heart.gif','kiss.gif','angel_smile.png','envelope.gif',
	 
	 '1.gif','2.gif','3.gif','4.gif','5.gif','6.gif','7.gif','8.gif','9.gif','10.gif','11.gif','12.gif','13.gif','14.gif','15.gif','16.gif',

	'17.gif','18.gif','19.gif','20.gif','21.gif','22.gif','23.gif','24.gif','25.gif','26.gif','27.gif','28.gif','29.gif','30.gif',

	'31.gif','32.gif','33.gif','34.gif','35.gif','36.gif','37.gif','38.gif','39.gif','40.gif','41.gif','42.gif','43.gif','44.gif',

	'45.gif','46.gif',"47.gif",'48.gif','49.gif','50.gif','51.gif','52.gif','53.gif','54.gif','55.gif','56.gif','57.gif','58.gif',

	'59.gif','60.gif','61.gif','62.gif','63.gif','64.gif','65.gif','66.gif','67.gif','68.gif','69.gif','70.gif','71.gif','72.gif',
	
	'73.gif','74.gif','75.gif','76.gif','77.gif','78.gif','79.gif','80.gif','81.gif','82.gif','83.gif','84.gif','85.gif','86.gif',
	
	'87.gif','88.gif','89.gif','90.gif','101.gif','102.gif','103.gif','104.gif','105.gif','106.gif','107.gif','108.gif','109.gif',
	
	'110.gif','111.gif','112.gif','113.gif','114.gif','115.gif','116.gif','117.gif','118.gif','119.gif','120.gif','121.gif','122.gif',
	
	'123.gif','124.gif','125.gif','126.gif','127.gif','128.gif','129.gif','130.gif','131.gif','132.gif'];

};
