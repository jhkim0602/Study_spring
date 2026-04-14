<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week07 Before Advice</title>
</head>
<body>
<h1>Before Advice</h1>
<p><code>@Before</code>는 대상 메서드 실행 전에 인증과 인자 확인을 수행한다.</p>

<h2>요청 값</h2>
<p>userId: ${userId}</p>
<p>role: ${role}</p>
<p>message: ${message}</p>
<p>count: ${count}</p>

<h2>서비스 실행 결과</h2>
<c:choose>
	<c:when test="${success}">
		<p>${result}</p>
	</c:when>
	<c:otherwise>
		<p>${error}</p>
	</c:otherwise>
</c:choose>

<h2>Advice 실행 기록</h2>
<c:forEach var="event" items="${events}">
	<p>${event}</p>
</c:forEach>

<p><a href="/week07/before">ADMIN 권한으로 다시 실행</a></p>
<p><a href="/week07/before?role=USER">USER 권한으로 실패 실행</a></p>
<p><a href="/week07">7주차 메인으로 이동</a></p>
</body>
</html>
