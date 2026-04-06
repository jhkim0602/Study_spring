<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week06 Lifecycle</title>
</head>
<body>
<h1>라이프사이클 메서드 순서</h1>

<h2>초기화 직후</h2>
<c:forEach var="event" items="${initEvents}">
	<p>${event}</p>
</c:forEach>

<h2>컨텍스트 종료 후</h2>
<c:forEach var="event" items="${fullEvents}">
	<p>${event}</p>
</c:forEach>

<p><a href="/week06">6주차 메인으로 이동</a></p>
</body>
</html>
