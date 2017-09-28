/**
 * 常工方法类
 * 
 * @author fb 
 */
window.hellogood = {};
window.hellogood.util= {}
var util = window.hellogood.util;

//判断字符串是否含有特殊字符
//util.containSpecial = function(str) {
//	var reg = /^[^@\/\'\\\"#$%&\^\*]+$/;
//	if (reg.test(str)) {
//		return true;
//	} else {
//		return false;
//	}
//};

//阻止冒泡和浏览器默认行为
//util.stopDefault = function(e) {
//	if (e && e.stopPropagation) {
//		e.stopPropagation();
//	} else {
//		// 否则，我们需要使用IE的方式来取消事件冒泡
//		e.cancelBubble = true;
//	}
//
//	if (e && e.preventDefault) {
//		e.preventDefault();
//	} else {
//		// IE中阻止函数器默认动作的方式
//		e.returnValue = false;
//	}
//	return false;
//};

//把serializeArray获取的数据转换成struts能识别的数据格式
util.toFormJson = function(data) {
	var json = {};

	$.each(data, function() {
		//不过滤空值，过滤后数组对应的下标就乱了
		/*if (!this.value) {
			return;
		}
		
		if ($.isNumeric(this.value) && new Number(this.value) == 0) {
			return;
		}*/

		if (json[this.name]) {
			if ($.isArray(json[this.name])) {
				json[this.name].push(this.value);
			} else {
				var values = [ json[this.name] ];
				values.push(this.value);
				json[this.name] = values;
			}
		} else {
			json[this.name] = this.value;
		}
	});

	return json;
};

util.serializeJson = function($from) {
	return util.toFormJson($from.serializeArray());
};


