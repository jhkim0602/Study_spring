<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week06 Scope Bean</title>
</head>
<body>
<h1>빈 스코프 비교</h1>

<c:forEach var="smsArray" items="${scopeBeanArray}" varStatus="status">
	<h2>${scopeNames[status.index]}</h2>
	<c:forEach var="sms" items="${smsArray}" varStatus="inner">
		<p>${inner.count} : ${sms}</p>
	</c:forEach>
	<p>같은 객체 여부: ${sameFlags[status.index]}</p>
	<hr>
</c:forEach>

<p><a href="/week06">6주차 메인으로 이동</a></p>
</body>
</html>
