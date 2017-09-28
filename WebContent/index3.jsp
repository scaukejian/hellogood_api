<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core-rt"%>

<html>

<head>
<script type="application/javascript" src="js/jquery-1.11.2.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>caspian-ws个人用户测试界面</title>
<script type="text/javascript">
	$.fn.serializeObject=function(){
		var hasOwnProperty=Object.prototype.hasOwnProperty;
		return this.serializeArray().reduce(function(data,pair){
			if(!hasOwnProperty.call(data,pair.name)){
				data[pair.name]=pair.value;
			}
			return data;
		},{});
	};
	function onClik(url,formName) {
	    var data = $("#"+formName).serializeObject(); 
	     $.ajax({
	         type: "POST",  
	         contentType: "application/json", 
	         url: url, 
	         data: JSON.stringify(data),       
	         dataType: 'json',
	         success: function (result) {
	        	 
	         }
	     });
	     $.post(url, data, "json");
	}
	
	function onGet(deleteUserId,formid,url){
		$("#"+formid).attr("action",url+$("#"+deleteUserId).val()+".do");
		$("#"+formid).submit();
	}
	
	function onGet2Params(formid,id1,id2,url){
		$("#"+formid).attr("action",url+$("#"+id1).val()+"-"+$("#"+id2).val()+".do");
		$("#"+formid).submit();
	}

</script>
</head>
<body>
	
	<br>个人用户测试页面（组织用户测试页面
	<a target="_blank" href="<c:url value="index.jsp"/>">猛戳这里</a>）
	<br>1)	获取验证码：
	<form action="<c:url value="/sms/code.do"/>" method="get">
		<input type="text" name="mobile" value="13888888888">
		发送验证码类型：
		<select name="type">
			<option value="register">注册下发校验码</option>
			<option value="forget">忘记密码下发新密码</option>
		</select>
		<button type="submit">提交</button>
	</form>
	<br>2)	用户注册：
	<form method="post" id="registerForm">
		手机号码：<input type="text" name="mobile" id="mobile">
		密码：<input type="text" name="password" id="password" value="24AC5F7E8E09B6E69B2B04DCFB214401DAD6C5DBDF3020B3066C4CB4A6162F00DA82BA2956E6A5176836833C4D3B5E481427CB5DABD8B505A66431ECC9B966BE374B298CFD8DC033C4BDB2D4E54EFD8381DCA9847C84E3651D41E405B55F38D3BE92033B2F7BD32042CBD33FBF17A4B238F51022B050EBCB5926EAE4AAF25C03">
		验证码：<input type="text" name="smsCode" id="smsCode">
		客户端类型：
		<select name="clientType" id="clientType">
			<option value="IOS">苹果</option>
			<option value="ANDROID">安卓</option>
		</select>
		<button type="button" id="butsubmit" onclick="onClik('/hellogood_api/auth/register.do','registerForm');">提交</button>
	</form>
	<br>3）用户登陆：
	<form method="post" id="loginForm">
		手机号码：<input type="text" name="mobile" id="mobile">
		密码：<input type="text" name="password" id="password" value="AD8D7EA5E7C76BAC3F4C5FAB98808088241EBA5760BDC49991A3FBC8C82ECAA20C4FF3412E42725DBCB59E0FE666ADB8E46853B2E2CB3E07142827646DD941658A33A942E0AF461A49FB8A77418A2AC49ADE21F3FC400EDD9756B65D4F4AB5E434A5A543BE1399F5B196B9F6D2E46405D80A5928B5C130ED0A2A144B6E2DB8E3">
		登陆类型：<select name="clientType" id="clientType">
			<option value="IOS">苹果</option>
			<option value="ANDROID">安卓</option>
		</select>
		<button type="button" id="butsubmit" onclick="onClik('/hellogood_api/auth/login.do','loginForm');">提交</button>
	</form>
	
	<br>4）找回密码：
	<form method="post" id="setNewPasswordForm">
		手机号码：<input type="text" name="mobile" id="mobile">
		密码：<input type="text" name="password" id="password">
		
		<button type="button" id="butsubmit" onclick="onClik('/hellogood_api/auth/changePassword.do','setNewPasswordForm');">提交</button>
	</form>
	
	<br>5)	用户删除(临时测试接口)
	<form action="<c:url value="user/delete/"/>" method="get" id="userDelete">
		userId:<input type="text" name="userId" id="deleteUserId">
		
		<button type="button" onclick="onGet('deleteUserId','userDelete','user/delete/');">提交</button>
	</form>
	
	<br>6)	基础信息查询
	<form action="<c:url value="baseData/all.do"/>" method="get" >
		<button type="submit">提交</button>
	</form>
	
	<br>7)	指定类型基础信息查询
	<form action="<c:url value="baseData/get/"/>" method="get" id="userType">
		<select name="type" id="datatype">
			<option value="area">地区</option>
			<option value="province">省份</option>
			<option value="nation">民族</option>
			<option value="degree">学历</option>
			<option value="industry">行业</option>
			<option value="job">工作职位</option>
			<option value="family">家庭情况</option>
			<option value="asset">资产</option>
			<option value="marry">婚姻</option>
			<option value="ownness">个人状态</option>
			<option value="hotel">酒店</option>
			<option value="message">消息</option>
		</select>
		<button type="button" onclick="onGet('datatype','userType','baseData/get/');">提交</button>
	</form>
	
	<br>8）消息：
	<form method="post" id="addMessage">
		消息类型id：<input type="text" name="type" id="type">
		userId：<input type="text" name="userId" id="userId">
		内容：<input type="text" name="content" id="content">
		<button type="button" id="butsubmit" onclick="onClik('/hellogood_api/message/add.do','addMessage');">提交</button>
	</form>
	
	<br>9)	消息查询
	<form action="<c:url value="message/getMessages/"/>" method="get" id="getMessage">
		userId:<input type="text" name="userId" id="getMessageuserId">		
		<button type="button" onclick="onGet('getMessageuserId','getMessage','message/getMessages/');">提交</button>
	</form>
	
	<br>10)	消息删除
	<form action="<c:url value="message/delete/"/>" method="get" id="deleteMessage">
		messageId:<input type="text" name="messageId" id="deleteMessageId">		
		<button type="button" onclick="onGet('deleteMessageId','deleteMessage','message/delete/');">提交</button>
	</form>
	
	<br>11)	消息设置为已读
	<form action="<c:url value="message/read/"/>" method="get" id="readMessageForm">
		messageId:<input type="text" name="messageId" id="readMessageId">		
		<button type="button" onclick="onGet('readMessageId','readMessageForm','message/read/');">提交</button>
	</form>
	
	<br>12)	人员关注
	<form action="<c:url value="subscribe/add/"/>" method="get" id="addSubscribeForm">
		operatorId:<input type="text" name="operatorId" id="addoperatorId">		
		subscribeUid:<input type="text" name="subscribeUid" id="addsubscribeUid">		
		<button type="button" onclick="onGet2Params('addSubscribeForm','addoperatorId','addsubscribeUid','subscribe/add/');">提交</button>
	</form>
	
	<br>13)	取消关注
	<form action="<c:url value="subscribe/cancel/"/>" method="get" id="cancelSubscribeForm">
		operatorId:<input type="text" name="operatorId" id="canceloperatorId">		
		subscribeUid:<input type="text" name="subscribeUid" id="cancelsubscribeUid">		
		<button type="button" onclick="onGet2Params('cancelSubscribeForm','canceloperatorId','cancelsubscribeUid','subscribe/cancel/');">提交</button>
	</form>
	
	<br>14)	关注人员查询
	<form action="<c:url value="subscribe/getSubscribes/"/>" method="get" id="getSubscribesForm">
		userId:<input type="text" name="userId" id="getSubscribesUserId">		
		<button type="button" onclick="onGet('getSubscribesUserId','getSubscribesForm','subscribe/getSubscribes/');">提交</button>
	</form>
	
	<br>15)	抵用券查询
	<form action="<c:url value="ticket/list/"/>" method="get" id="gettickListForm">
		userId:<input type="text" name="userId" id="getticketListUserId">		
		<button type="button" onclick="onGet('getticketListUserId','gettickListForm','ticket/list/');">提交</button>
	</form>
	
	<br>16)	抵用券使用
	<form action="<c:url value="ticket/use/"/>" method="get" id="getticketUseForm">
		userId:<input type="text" name="userId" id="getticketUseUserId">		
		<button type="button" onclick="onGet('getticketUseUserId','getticketUseForm','ticket/use/');">提交</button>
	</form>
	
	<br>17)	查看相册
	<form action="<c:url value="photo/list/"/>" method="get" id="getphotoListForm">
		userId:<input type="text" name="userId" id="getphotoListUserId">		
		<button type="button" onclick="onGet('getphotoListUserId','getphotoListForm','photo/list/');">提交</button>
	</form>
	
	<br>18)	个人账户
	<form action="<c:url value="account/get/"/>" method="get" id="getaccountForm">
		userId:<input type="text" name="userId" id="getaccountUserId">		
		<button type="button" onclick="onGet('getaccountUserId','getaccountForm','account/get/');">提交</button>
	</form>
	
	<br>19)	形象照设置
	<form action="<c:url value="photo/setHeadFlag/"/>" method="get" id="setHeadFlagForm">
		userId:<input type="text" name="userId" id="setHeadFlaguserId">		
		photoId:<input type="text" name="photoId" id="setHeadFlagphotoId">		
		<button type="button" onclick="onGet2Params('setHeadFlagForm','setHeadFlaguserId','setHeadFlagphotoId','photo/setHeadFlag/');">提交</button>
	</form>
	
	<br>
	<form action="<c:url value="test/reFlashEhcache.do"/>" method="get" id="getaccountForm">
		<font color="red">50)刷新基础数据缓存</font><button type="submit">提交</button>
	</form>
</body>

</html>
