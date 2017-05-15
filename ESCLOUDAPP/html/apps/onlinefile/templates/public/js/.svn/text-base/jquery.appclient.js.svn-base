/*!
 * appClient 1.0.0
 * Date: 2012-09-07 13:00
 */
;
(function($) {
	_baseUrl = getAppPath();
	$.appClient = {
		baseUrl : _baseUrl,
		flexpaperConfig : {
			Scale : 0.6, 
			ZoomTransition : 'easeOut',
			ZoomTime : 0.5, 
			ZoomInterval : 0.2,
			FitPageOnLoad : false,
			FitWidthOnLoad : false, 
			FullScreenAsMaxWindow : false,
			ProgressiveLoading : true,
			MinZoomSize : 0.2,
			MaxZoomSize : 5,
			SearchMatchAll : false,
			InitViewMode : 'Portrait',
			RenderingOrder : 'flash,html',
			 
			ViewModeToolsVisible : true,
			ZoomToolsVisible : true,
			NavToolsVisible : true,
			CursorToolsVisible : true,
			SearchToolsVisible : true,
			localeChain: 'zh_CN'
		},
		generateUrl : function(path, url) {
			this.baseUrl;
			if (typeof url === 'string' && url.length > 0) {
				url = this.baseUrl.substring(0,
						this.baseUrl.lastIndexOf("/") + 1)
						+ url;
			} else {
				url = this.baseUrl;
			}
			var i = 1;
			var urlArray = [];
			urlArray.push(url);
			if ($.isPlainObject(path)) {
				$.each(path, function(k, v) {
					switch (i) {
					case 1:
						urlArray.push("/", k, "/", v);
						break;
					case 2:
						urlArray.push("?", k, "=", v);
						break;
					default:
						urlArray.push("&", k, "=", v);
						break;
					}
					i++;
				});
			}
			return urlArray.join("");
		}
	};

	function getAppPath() {
		var scripts = document.getElementsByTagName('script');
		var me = null;
		for (i in scripts) {
			// 如果通过第三方脚本加载器加载本文件，请保证文件名含有"appclient"字符
			if (scripts[i].src && scripts[i].src.indexOf('appclient') !== -1)
				me = scripts[i];
		}
		// 如果加载的脚本里没有，在获取自己（同步加载可用）
		me = me || script[script.length - 1];
		return me.src.replace(/\\/g, '/').split('baseurl=')[1];
	}
	;
	$.fn.extend({
        "changeTips": function(value) {
            value = $.extend({
                divTip: ""
            },
            value)

            var $this = $(this);
            var indexLi = 0;

            //点击document隐藏下拉层
            $(document).click(function(event) {
                if ($(event.target).attr("class") == value.divTip || $(event.target).is("li")) {
                    var liVal = $(event.target).text();
                    $this.val(liVal);
                    blus();
                } else {
                    blus();
                }
            })

            //隐藏下拉层
            function blus() {
                $(value.divTip).hide();
            }

            //键盘上下执行的函数
            function keychang(up) {
                if (up == "up") {
                    if (indexLi == 1) {
                        indexLi = $(value.divTip).children().length - 1;
                    } else {
                        indexLi--;
                    }
                } else {
                    if (indexLi == $(value.divTip).children().length - 1) {
                        indexLi = 1;
                    } else {
                        indexLi++;
                    }
                }
                $(value.divTip).children().eq(indexLi).addClass("active").siblings().removeClass();
            }

            //值发生改变时
            function valChange() {
                var tex = $this.val(); //输入框的值
                var fronts = ""; //存放含有“@”之前的字符串
                var af = /@/;
                var regMail = new RegExp(tex.substring(tex.indexOf("@"))); //有“@”之后的字符串,注意正则字面量方法，是不能用变量的。所以这里用的是new方式。

                //让提示层显示，并对里面的LI遍历
                if ($this.val() == "") {
                    blus();
                } else {
                    $(value.divTip).show().children().each(function(index) {
                        var valAttr = $(this).attr("email");
                        if (index == 1) {
                            $(this).text(tex).addClass("active").siblings().removeClass();
                        }
                        //索引值大于1的LI元素进处处理
                        if (index > 1) {
                            //当输入的值有“@”的时候
                            if (af.test(tex)) {
                                //如果含有“@”就截取输入框这个符号之前的字符串
                                fronts = tex.substring(tex.indexOf("@"), 0);
                                $(this).text(fronts + valAttr);
                                //判断输入的值“@”之后的值，是否含有和LI的email属性
                                if (regMail.test($(this).attr("email"))) {
                                    $(this).show();
                                } else {
                                    if (index > 1) {
                                        $(this).hide();
                                    }
                                }

                            }
                            //当输入的值没有“@”的时候
                            else {
                                $(this).text(tex + valAttr);
                            }
                        }
                    })
                }
            }

            //输入框值发生改变的时候执行函数，这里的事件用判断处理浏览器兼容性;
            if ($.browser.msie) {
                $(this).bind("propertychange",
                function() {
                    valChange();
                })
            } else {
                $(this).bind("input",
                function() {
                    valChange();
                })
            }

            //鼠标点击和悬停LI
            $(value.divTip).children().hover(function() {
                indexLi = $(this).index(); //获取当前鼠标悬停时的LI索引值;
                if ($(this).index() != 0) {
                    $(this).addClass("active").siblings().removeClass();
                }
            })

            //按键盘的上下移动LI的背景色
            $this.keydown(function(event) {
                if (event.which == 38) { //向上
                    keychang("up")
                } else if (event.which == 40) { //向下
                    keychang()
                } else if (event.which == 13) { //回车
                    var liVal = $(value.divTip).children().eq(indexLi).text();
                    $this.val(liVal);
                    blus();
                }
            })
        }
    });
})(jQuery);