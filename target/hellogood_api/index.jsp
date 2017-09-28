<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core-rt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>caspian-ws组织用户测试界面</title>
</head>
<body>
	<br>组织用户测试页面（个人用户测试页面
	<a target="_blank" href="<c:url value="index2.jsp	"/>">猛戳这里</a>）
	<br> 统一的登录login
	<form action="<c:url value="/http/auth/login.do"/>" method="post">
		<input type="text" name="account" value="xinmei"> <input
			type="text" name="password" value="123456">
		<button type="submit">提交</button>
	</form>
	<br>统一的退出logout
	<form action="<c:url value="/http/auth/logout.do"/>" method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff">
		<button type="submit">提交</button>
	</form>
	<br> ---------------------------------------------------
	<br>统一的提交（IOS）客户端Device token
	<form action="<c:url value="/http/device/submit.do"/>" method="post">
		<input type="text" name="id" value="31c9ef7761723b"><input type="text"
			name="crypto" value="ffff"><input type="text" name="token"
			value="e0b09638b7e5b6e4c36e6d59901d05b9a943012f6549dadacb8593da95be4ccd">
		<button type="submit">提交</button>
	</form>
	<br> ---------------------------------------------------
	<br> 组织发布消息publishMessage：
	<form action="<c:url value="/http/biz/publishMessage.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text" name="title"
			value="title"><input type="text" name="content"
			value="content"><input type="text" name="groups"
			value="c8ee094801b14f4aa86dd5e736fd16a9"><input type="text"
			name="sendTime" value="0">
		<button type="submit">提交</button>
	</form>
	<br> 组织获取某个子组织的成员列表（分页）pullGroupUserByPage：
	<form action="<c:url value="/http/biz/pullGroupUserByPage.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text" name="groupId"
			value="c8ee094801b14f4aa86dd5e736fd16a9"><input type="text"
			name="pageNum" value="1"><input type="text" name="pageSize"
			value="10">
		<button type="submit">提交</button>
	</form>
	<br> 组织发送P2P消息给个人sendP2pMessage：
	<form action="<c:url value="/http/biz/sendP2pMessage.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text" name="userId"
			value="31c9ef7761723b"><input type="text" name="content"
			value="content">
		<button type="submit">提交</button>
	</form>
	<br> 组织获取个人消息回复getMessageReply：
	<form action="<c:url value="/http/biz/getMessageReply.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text"
			name="messageId" value="159c6e28-adc7-45fc-8d69-9f7191cf5c61">
		<button type="submit">提交</button>
	</form>
	<br> 组织回复个人回复信息sendMessageReply：
	<form action="<c:url value="/http/biz/sendMessageReply.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text"
			name="messageId" value="9d196a2b5d224d7fad5a1b7df2c0aaa7"> <input
			type="text" name="content" value="这个回复只有特定用户可以收到">
		<button type="submit">提交</button>
	</form>
	<br> 组织删除回复信息removeMessageReply：
	<form action="<c:url value="/http/biz/removeMessageReply.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text"
			name="messageId" value="a5aba40728cd4858b8a39d0037b0e45f">
		<button type="submit">提交</button>
	</form>
	<br> 组织回复PUB信息sendMessageReplyAll：
	<form action="<c:url value="/http/biz/sendMessageReplyAll.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text"
			name="messageId" value="3554141d-ea9c-47bf-bae8-332faf2a0ac0">
		<input type="text" name="content" value="这个回复群组所有成员都能收到">
		<button type="submit">提交</button>
	</form>
	<br> 获取PUB消息的回复列表getPubMessageReplyListByPage：
	<form
		action="<c:url value="/http/biz/getPubMessageReplyListByPage.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text"
			name="messageId" value="01cc2d62a9be42b494b376cb2bdb84ca"> <input
			type="text" name="pageNum" value="1"> <input type="text"
			name="pageSize" value="10">
		<button type="submit">提交</button>
	</form>
	<br> 组织获取PUB消息的离线回复列表pullOffLinePubMessageReply：
	<form action="<c:url value="/http/biz/pullOffLinePubMessageReply.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff">
		<button type="submit">提交</button>
	</form>
	<br> 组织获取P2P消息的离线回复列表pullOffLineP2PMessageReply：
	<form action="<c:url value="/http/biz/pullOffLineP2PMessageReply.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff">
		<button type="submit">提交</button>
	</form>
	<br> 获取组织待发送消息列表pullPubMessageOnPending：
	<form action="<c:url value="/http/biz/pullPubMessageOnPending.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text" name="pageNum"
			value="1"> <input type="text" name="pageSize" value="10">
		<button type="submit">提交</button>
	</form>
	<br> 组织删除某个组的待发送消息removePubMessageOnPending：
	<form action="<c:url value="/http/biz/removePubMessageOnPending.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text"
			name="messageId" value="1984d97c-0587-4396-91c4-c166eec59818">
		<button type="submit">提交</button>
	</form>
	<br> 获取组织已完成消息列表pullPubMessageFinished：
	<form action="<c:url value="/http/biz/pullPubMessageFinished.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text" name="pageNum"
			value="1"> <input type="text" name="pageSize" value="10">
		<button type="submit">提交</button>
	</form>
	<br> 组织删除某个成员removeGroupUser：
	<form action="<c:url value="/http/biz/removeGroupUser.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text" name="groupId"
			value="c4ca4238a0b923820dcc509a6f75849b"><input type="text"
			name="userId" value="1100">
		<button type="submit">提交</button>
	</form>
	<br> 组织上传头像uploadAvatar：
	<form action="<c:url value="/http/biz/uploadAvatar.do"/>" method="post"
		enctype="multipart/form-data">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="file" name="avatar">
		<button type="submit">提交</button>
		<img src="<c:url value="/avatar/org.do?id=3"/>">
	</form>
	<br>组织修改资料updateOrgInfo：
	<form action="<c:url value="/http/biz/updateOrgInfo.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"><input type="text" name="orgId"
			value="3"><input type="text" name="name" value="永祥地产"><input
			type="text" name="area" value="地区"><input type="text"
			name="contact" value="联系人"><input type="text" name="email"
			value="niubee@ieee.org"><input type="text" name="desc"
			value="介是特么一个神奇的组织">
		<button type="submit">提交</button>
	</form>
	<br>组织搜索用户searchUser：
	<form action="<c:url value="/http/biz/searchUser.do"/>" method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"><input type="text" name="keyword"
			value="13">
		<button type="submit">提交</button>
	</form>
	<br>组织发出邀请用户加入applyGroupinToUser：
	<form action="<c:url value="/http/biz/applyGroupinToUser.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"><input type="text" name="groupId"
			value="c8ee094801b14f4aa86dd5e736fd16a9"><input type="text"
			name="userId" value="111ddf">
		<button type="submit">提交</button>
	</form>
	<br>获取用户申请的汇总pullApplyGroupSummary：
	<form action="<c:url value="/http/biz/pullApplyGroupSummary.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff">
		<button type="submit">提交</button>
	</form>
	<br>获取用户申请pullApplyGroupFromUser：
	<form action="<c:url value="/http/biz/pullApplyGroupFromUser.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"><input type="text" name="pageNum"
			value="1"><input type="text" name="pageSize" value="10">
		<button type="submit">提交</button>
	</form>
	<br>获取组织下的子组织（申请加入时选择）pullOrgGroupListForSelect：
	<form action="<c:url value="/http/biz/pullOrgGroupListForSelect.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"><input type="text" name="orgId"
			value="1"><input type="text" name="userId"
			value="31c9ef7761723b">
		<button type="submit">提交</button>
	</form>
	<br>同意用户申请加入群组approveUserGroupin：
	<form action="<c:url value="/http/biz/approveUserGroupin.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="a"><input type="text" name="groupId"
			value="c4ca4238a0b923820dcc509a6f75849b"><input type="text"
			name="userId" value="31c9ef7761723b"><input type="text" name="approved"
			value="1">
		<button type="submit">提交</button>
	</form>
	<br>同意用户申请离开群组approveUserGroupout：
	<form action="<c:url value="/http/biz/approveUserGroupout.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="a"><input type="text" name="groupId"
			value="c4ca4238a0b923820dcc509a6f75849b"><input type="text"
			name="userId" value="31c9ef7761723b"><input type="text" name="approved"
			value="1">
		<button type="submit">提交</button>
	</form>
	<br>修改用户在群组的昵称updateGroupUserNickName：
	<form action="<c:url value="/http/biz/updateGroupUserNickName.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="a"><input type="text" name="groupId"
			value="c4ca4238a0b923820dcc509a6f75849b"><input type="text"
			name="userId" value="31c9ef7761723b"><input type="text" name="nickname"
			value="哈哈哈">
		<button type="submit">提交</button>
	</form>
	<br>获取用户管理的群组pullManageGroup：
	<form action="<c:url value="/http/biz/pullManageGroup.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="a">
		<button type="submit">提交</button>
	</form>
	<br>获取成员详细信息pullUserInfo：
	<form action="<c:url value="/http/biz/pullUserInfo.do"/>" method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="a"><input type="text" name="uid"
			value="31c9ef7761723b">
		<button type="submit">提交</button>
	</form>
	<br> 获取组织与个人的会话列表pullTalkListWithUser：
	<form action="<c:url value="/http/biz/pullTalkListWithUser.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf"><input type="text"
			name="crypto" value="ffff"> <input type="text" name="userId"
			value="31c9ef7761723b"> <input type="text" name="pageNum" value="1">
		<input type="text" name="pageSize" value="10">
		<button type="submit">提交</button>
	</form>
	<br> 删除已经发布的PUB消息removePubMessageAlreadySend：
	<form action="<c:url value="/http/biz/removePubMessageAlreadySend.do"/>"
		method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf">
		<input type="text" name="crypto" value="ffff"> 
		<input type="text" name="messageId" value="c29fbd76-0e7f-495a-bdc7-5a3c9a459179">
		<button type="submit">提交</button>
	</form>
	<%-- <br> 验证修改组织信息updateOrgInfoCheck：
	<form action="<c:url value="/http/biz/updateOrgInfoCheck.do"/>" method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf">
		<input type="text" name="crypto" value="ffff">
		<input type="text" name="orgId" value="1">
		<input type="text" name="name" value="新梅文化传播集团">
		<input type="text" name="area" value="迪拜">
		<input type="text" name="phoneNum" value="13313313122">
		<input type="text" name="code" value="1002003004">
		<input type="text" name="contact" value="默罕默德">
		<input type="text" name="email" value="niubee@ieee.org">
		<input type="text" name="desc" value="5月份开始，广州楼市开始出现了降价风潮。但昨日广州市国土房管局发布最新楼市报告显示，5月份广州十区两市一手住宅网签均价，连续三月创新高至15767元/平方米，环比涨324元/平方米，涨幅2.1%；网签成交量也环比略增0.31万平方米，为64.05万平方米，增幅0.49%。">
		<button type="submit">提交</button>
	</form> --%>
	<%-- <br> 提交修改组织信息editOrgInfo：
	<form action="<c:url value="/http/biz/editOrgInfo.do"/>" method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf">
		<input type="text" name="crypto" value="ffff">
		<input type="text" name="orgId" value="1">
		<input type="text" name="name" value="新梅文化传播集团">
		<input type="text" name="area" value="迪拜">
		<input type="text" name="phoneNum" value="13313313122">
		<input type="text" name="code" value="1002003004">
		<input type="text" name="contact" value="默罕默德">
		<input type="text" name="email" value="niubee@ieee.org">
		<input type="text" name="desc" value="5月份开始，广州楼市开始出现了降价风潮。但昨日广州市国土房管局发布最新楼市报告显示，5月份广州十区两市一手住宅网签均价，连续三月创新高至15767元/平方米，环比涨324元/平方米，涨幅2.1%；网签成交量也环比略增0.31万平方米，为64.05万平方米，增幅0.49%。">
		<button type="submit">提交</button>
	</form> --%>
	<br> 组织用户获取组织最新信息getNewOrgInfo：
	<form action="<c:url value="/http/biz/getNewOrgInfo.do"/>" method="post">
		<input type="text" name="id" value="sdfdsfsi7309270r9ewpjlfvjl0o9oilfdgdf">
		<button type="submit">提交</button>
	</form>
</body>
</html>