
//liuwei兼容IE下placeholder
(function($) {
  
  var placeholderfriend = {
    focus: function(s) {
      s = $(s).hide().prev().show().focus();
      var idValue = s.attr("id");
      if (idValue) {
        s.attr("id", idValue.replace("placeholderfriend", ""));
      }
      var clsValue = s.attr("class");
	  if (clsValue) {
        s.attr("class", clsValue.replace("placeholderfriend", ""));
      }
    }
  }

  //判断是否支持placeholder
  function isPlaceholer() {
    var input = document.createElement('input');
    return "placeholder" in input;
  }
  //不支持的代码
  if (!isPlaceholer()) {
    $(function() {

      var form = $(this);
      var elements = form.find("input[type='text'][placeholder]");
      elements.each(function() {
        var s = $(this);
        var pValue = s.attr("placeholder");
		var sValue = s.val();
        if (pValue) {
          if (sValue == '') {
            s.val(pValue);
          }
        }
      });

      elements.focus(function() {
        var s = $(this);
        var pValue = s.attr("placeholder");
		var sValue = s.val();
        if (sValue && pValue) {
          if (sValue == pValue) {
            s.val('');
          }
        }
      });

      elements.blur(function() {
        var s = $(this);
        var pValue = s.attr("placeholder");
		var sValue = s.val();
        if (!sValue) {
          s.val(pValue);
        }
      });
    });
  }
  window.placeholderfriendfocus = placeholderfriend.focus;
})(jQuery);