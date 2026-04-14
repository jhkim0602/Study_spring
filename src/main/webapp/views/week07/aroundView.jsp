<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week07 Around Advice</title>
</head>
<body>
<h1>Around Advice</h1>
<p><code>@Around</code>는 대상 메서드 실행 전후를 감싸고, 인자와 반환값을 다룰 수 있다.</p>

<h2>요청 값</h2>
<p>userId: ${userId}</p>
<p>role: ${role}</p>

<h2>서비스 실행 결과</h2>
<p>${result}</p>

<h2>Advice 실행 기록</h2>
<c:forEach var="event" items="${events}">
	<p>${event}</p>
</c:forEach>

<p><a href="/week07/around">role=admin으로 다시 실행</a></p>
<p><a href="/week07/around?role=user">role=user로 실행</a></p>
<p><a href="/week07">7주차 메인으로 이동</a></p>
</body>
</html>
