<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core-rt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>caspian-ws个人用户测试界面</title>
</head>
<body>
	<br>个人用户测试页面（组织用户测试页面
	<a target="_blank" href="<c:url value="index.jsp	"/>">猛戳这里</a>）
	<br>统一的登录login：
	<form action="<c:url value="/http/auth/login.do"/>" method="post">
		<input type="text" name="account" value="13888888888"> <input
			type="text" name="password" value="123456">
		<button type="submit">提交</button>
	</form>
	<br>统一的退出logout：
	<form action="<c:url value="/http/auth/logout.do"/>" method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff">
		<button type="submit">提交</button>
	</form>
	<br>
	--------------------------------------------------------------------
	<br>
	--------------------------------------------------------------------
	<br> 个人获取组织发布的信息内容getPubMessage：
	<form action="<c:url value="/http/usr/getPubMessage.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="messageId" value="1984d97c-0587-4396-91c4-c166eec59818">
		<button type="submit">提交</button>
	</form>
	<br> 个人申请加入组织applyGroupin：
	<form action="<c:url value="/http/usr/applyGroupin.do"/>" method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="orgId" value="1"><input type="text" name="desc"
			value="申请加入描述">
		<button type="submit">提交</button>
	</form>
	<br> 个人申请离开组织applyGroupout：
	<form action="<c:url value="/http/usr/applyGroupout.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="groupId" value="1">
		<button type="submit">提交</button>
	</form>
	<br> 个人收取离线的消息内容pullOffLinePubMessage：
	<form action="<c:url value="/http/usr/pullOffLinePubMessage.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff">
		<button type="submit">提交</button>
	</form>
	<br> 个人收取离线的回复内容pullOffLinePubMessageReply：
	<form action="<c:url value="/http/usr/pullOffLinePubMessageReply.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff">
		<button type="submit">提交</button>
	</form>
	<br> 个人发布消息回复sendMessageReply：
	<form action="<c:url value="/http/usr/sendMessageReply.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="messageId" value="37cda926-4056-4e3f-9701-eda39ed4052f"><input
			type="text" name="content" value="contentjjyyjjyy哈哈">
		<button type="submit">提交</button>
	</form>
	<br> 个人删除回复信息removeMessageReply：
	<form action="<c:url value="/http/usr/removeMessageReply.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"> <input type="text"
			name="messageId" value="a5aba40728cd4858b8a39d0037b0e45f">
		<button type="submit">提交</button>
	</form>
	<br> 个人获取组织回复的消息getMessageReply：
	<form action="<c:url value="/http/usr/getMessageReply.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="messageId" value="0a624e89971949ee982e0cb334a46d51">
		<button type="submit">提交</button>
	</form>
	<br> 个人获取某个消息的回复列表getPubMessageReplyListByPage：
	<form
		action="<c:url value="/http/usr/getPubMessageReplyListByPage.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="messageId" value="4bd01389-778f-4fac-95c4-134cea7aa621"><input
			type="text" name="pageNum" value="1"><input type="text"
			name="pageSize" value="10">
		<button type="submit">提交</button>
	</form>
	<br> 个人获取P2P消息内容getP2pMessage：
	<form action="<c:url value="/http/usr/getP2pMessage.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="messageId" value="ff8679a3-bfb7-4266-b44c-e9c568c75d7b">
		<button type="submit">提交</button>
	</form>
	<br> 个人获取离线的组织发来的P2P消息pullOffLineP2pMessage：
	<form action="<c:url value="/http/usr/pullOffLineP2pMessage.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff">
		<button type="submit">提交</button>
	</form>
	<br> 个人修改基本资料updateUserInfo：
	<form action="<c:url value="/http/usr/updateUserInfo.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="account" value="ha"><input type="text" name="name"
			value="张四"><input type="text" name="phone" value="13000111"><input
			type="text" name="email" value="13000111@aa.cc"><input
			type="text" name="region" value="河南省"><input type="text"
			name="city" value="驻马店"><input type="text" name="desc"
			value="么咦嗷诗乄">
		<button type="submit">提交</button>
	</form>
	<br> 个人上传头像uploadAvatar：
	<form action="<c:url value="/http/usr/uploadAvatar.do"/>" method="post"
		enctype="multipart/form-data">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="file"
			name="avatar">
		<button type="submit">提交</button>
		<img src="<c:url value="/avatar/usr.do?id=31c9ef7761723b"/>">
	</form>
	<br> 个人搜索组织searchOrg：
	<form action="<c:url value="/http/usr/searchOrg.do"/>" method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="keyword" value="地产"><input type="text" name="pageNum"
			value="1"><input type="text" name="pageSize" value="10">
		<button type="submit">提交</button>
	</form>
	<br> 检查手机号是否注册过checkPhoneRegistered：
	<form action="<c:url value="/http/usr/checkPhoneRegistered.do"/>"
		method="post">
		<input type="text" name="phone" value="13888888888">
		<button type="submit">提交</button>
	</form>
	<br> 发送验证码sendCaptcha：
	<form action="<c:url value="/http/usr/sendCaptcha.do"/>" method="post">
		<input type="text" name="phone" value="13888888888">
		<button type="submit">提交</button>
	</form>
	<br> 个人用户注册register：
	<form action="<c:url value="/http/usr/register.do"/>" method="post">
		<input type="text" name="phone" value="13888888888"> <input
			type="text" name="captcha" value="999">
		<button type="submit">提交</button>
	</form>
	<br> 个人（已注册）用户验证验证码是否合法checkCaptcha：
	<form action="<c:url value="/http/usr/checkCaptcha.do"/>" method="post">
		<input type="text" name="phone" value="13888888888"><input
			type="text" name="captcha" value="999">
		<button type="submit">提交</button>
	</form>
	<br> 个人用户重置密码password：
	<form action="<c:url value="/http/usr/password.do"/>" method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="phone" value="123"> <input type="text" name="password"
			value="123456">
		<button type="submit">提交</button>
	</form>
	<br> 个人用户注册成功之后设置密码和昵称updatePasswordAndName：
	<form action="<c:url value="/http/usr/updatePasswordAndName.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="phone" value="13888888888"> <input type="text"
			name="password" value="123"> <input type="text" name="name"
			value="张小衫">
		<button type="submit">提交</button>
	</form>
	<br> 个人用户修改密码updatePassword：
	<form action="<c:url value="/http/usr/updatePassword.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="oldpassword" value="123"> <input type="text"
			name="newpassword" value="123456">
		<button type="submit">提交</button>
	</form>
	<br>
	--------------------------------------------------------------------
	<br>
	--------------------------------------------------------------------
	<br>上报地理位置（组织用户、个人 用户）submit：
	<form action="<c:url value="/http/lbs/submit.do"/>" method="post">
		<input type="text" name="id"
			value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input
			type="text" name="crypto" value="31c9ef7761723b"><input
			type="text" name="type" value="0"><input type="text"
			name="phone" value="13000111"> <input type="text"
			name="country" value="中国"> <input type="text" name="region"
			value="河南"> <input type="text" name="city" value="驻马店">
		<input type="text" name="district" value="城关区"> <input
			type="text" name="block" value="XO大酒店"> <input type="text"
			name="latitude" value="23.117055306224895"> <input
			type="text" name="longitude" value="113.2759952545166">
		<button type="submit">提交</button>
	</form>
	<br> 个人用户获取组织信息pullOrgInfo：
	<form action="<c:url value="/http/usr/pullOrgInfo.do"/>" method="post">
		<input type="text" name="id" value="1">
		<button type="submit">提交</button>
	</form>
	<br> 获取自己订阅的群组pullSubscribeGroup：
	<form action="<c:url value="/http/usr/pullSubscribeGroup.do"/>"
		method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input
			type="text" name="crypto" value="ffff">
		<button type="submit">提交</button>
	</form>
	<br> 个人用户（分页）获取组织发来的未处理的邀请pullOrgInviteByPage：
	<form action="<c:url value="/http/usr/pullOrgInviteByPage.do"/>"
		method="post">
		<input type="text" name="id" value="31d72db459c7fa"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="pageNum" value="1"><input type="text" name="pageSize"
			value="10">
		<button type="submit">提交</button>
	</form>
	<br> 个人用户（分页）获取与组织的对话pullTalkListWithGroup：
	<form action="<c:url value="/http/usr/pullTalkListWithGroup.do"/>"
		method="post">
		<input type="text" name="id" value="31d72db459c7fa"><input
			type="text" name="crypto" value="ffff"><input type="text"
			name="orgId" value="4"><input type="text" name="pageNum"
			value="1"><input type="text" name="pageSize" value="10">
		<button type="submit">提交</button>
	</form>
</body>
</html>