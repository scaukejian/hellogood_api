<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core-rt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="../js/jquery-1.11.2.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script>document.forms[0].submit();</script>
</head>
<body>
	<form action="<c:url value="/ali/doPost.do"/>" method="get">
		<input type="text" name="mobile" value="13888888888">
		<button type="submit">提交</button>
	</form>
 	
</body>
</html>