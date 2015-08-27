function getContextPath() {
    var pathName = location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}
var path = getContextPath();//location.protocol+"//"+${nowDomainUrl}+
var ctxPath = getContextPath();

/**
 * 将一个表单的数据返回成JSON对象  
 */
$.fn.serializeObject = function() {   
	  var o = {};   
	  var a = this.serializeArray();   
	  $.each(a, function() {   
	    if (o[this.name]) {   
	      if (!o[this.name].push) {   
	        o[this.name] = [ o[this.name] ];   
	      }   
	      o[this.name].push(this.value || '');   
	    } else {   
	      o[this.name] = this.value || '';   
	    }   
	  });   
	  return o;   
};   