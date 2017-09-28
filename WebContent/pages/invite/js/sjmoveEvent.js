
//window.onload = isTouchDevice;
	//全局变量，触摸开始位置
var startX = 0, startY = 0;
// touchstart事件
function touchSatrtFunc(evt) {
	try {
		var touch = evt.touches[0]; // 获取第一个触点
		var x = Number(touch.pageX); // 页面触点X坐标
		var y = Number(touch.pageY); // 页面触点Y坐标
		// 记录触点初始位置
		startX = x;
		startY = y;
	} catch (e) {
		console.log('touchSatrtFunc：' + e.message);
	}
}

// touchmove事件，这个事件无法获取坐标
function touchMoveFunc(evt) {
	try {
		// evt.preventDefault(); //阻止触摸时浏览器的缩放、滚动条滚动等
		var touch = evt.touches[0]; // 获取第一个触点
		var x = Number(touch.pageX); // 页面触点X坐标
		var y = Number(touch.pageY); // 页面触点Y坐标
		// 判断滑动方向
		var sum = y - startY;
		var pages=$("#pages").val();
		if (sum < -10) {
			if(pages<4)
				window.location.href="page.jsp?pages="+pages+"&type=next&id="+id;
		} else if (sum > 10) {
			if(pages>2)
				window.location.href="page.jsp?pages="+pages+"&type=up&id="+id;
			else if(pages != 1){
				window.location.href="register.jsp?id="+id;
			}
				
				
		}
	} catch (e) {
		console.log('touchMoveFunc：' + e.message);
	}
}
// 绑定事件
function bindEvent() {
	document.getElementById("login_register").addEventListener('touchstart',
			touchSatrtFunc, false);
	document.getElementById("login_register").addEventListener('touchmove',
			touchMoveFunc, false);
}
// 判断是否支持触摸事件
function isTouchDevice() {
	try {
		document.createEvent("TouchEvent");

		bindEvent(); // 绑定事件
	} catch (e) {
		console.log("不支持TouchEvent事件！" + e.message);
	}
}
