  function utf16to8(str)  {
	    var out, i, len, c;
	 
	    out = "";
	    len = str.length;
	    for(i = 0; i < len; i++){
	        c = str.charCodeAt(i);
	        if ((c >= 0x0001) && (c <= 0x007F)) {
	            out += str.charAt(i);
	        } else if (c > 0x07FF){
	            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
	            out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
	            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
	        } else {
	            out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
	            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
	        }
	    }
	    return out;
  }

  function addSecurityPart(url, data, token, u, jsessionid){
		var start1 = 0;
		var end1 = url.indexOf("callback=?");
		var str = url.substring(start1, end1);
	
		var start2 = end1 + ("callback=?").length;
		var end2 = url.length;
		str = str + url.substring(start2, end2);
	
	    data = addProps(data, token, u, jsessionid);
	
	    var ret = addSha1(str, data);
		return ret;
	}
	
	function addProps(obj, token, u, jsessionid) {
	   obj['token'] =token;
	   obj['u'] =u;
	   obj['JSESSIONID'] =jsessionid;
	   return obj;
	}
	
	function addSha1(str, obj) {
	   var index = 0;
	   for ( var p in obj ){
		  if ( typeof ( obj [ p ]) == " function " ){ 
		      obj [ p ]() ; 
		  } else { // p 为属性名称，obj[p]为对应属性的值 
			 if ( typeof ( obj [ p ]) === 'undefined') {
				 //alert(p + "=" + obj [ p ]);
			 } else {
				 if (p == 't') {
					 
				 } else {
					 if (index == 0)
				     {
			            str += p + "=" + obj [ p ]; 
				     } else {
					    str +=  "&" + p + "=" + obj [ p ]  ; 
					 }
					 index  = index + 1;
				 }
			 }
		  } 
	   }
	   obj['t'] =hex_sha1(utf16to8(str));
	   return {str:str, data:obj};
	}
	
	function writeObj(obj){ 
	   var description = ""; 
	   for(var i in obj){ 
	      var property=obj[i]; 
	      description+=i+" = "+property+"\n"; 
	   } 
	   alert(description); 
	}