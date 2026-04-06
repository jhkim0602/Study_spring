<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week05 Lifecycle</title>
</head>
<body>
<h1>빈 라이프사이클 확인</h1>

<h2>초기화 직후 이벤트</h2>
<c:forEach var="event" items="${initEvents}">
	<p>${event}</p>
</c:forEach>

<h2>컨텍스트 종료 후 전체 이벤트</h2>
<c:forEach var="event" items="${fullEvents}">
	<p>${event}</p>
</c:forEach>

<p><a href="/week05">5주차 메인으로 이동</a></p>
</body>
</html>
