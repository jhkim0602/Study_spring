<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week07 Annotation Pointcut</title>
</head>
<body>
<h1>Annotation Pointcut</h1>
<p><code>@annotation</code> Pointcut은 특정 어노테이션이 붙은 메서드만 대상으로 삼는다.</p>

<h2>요청 값</h2>
<p>label: ${label}</p>

<h2>서비스 실행 결과</h2>
<p>${result}</p>

<h2>Advice 실행 기록</h2>
<c:forEach var="event" items="${events}">
	<p>${event}</p>
</c:forEach>

<p><a href="/week07/pointcut?label=custom-label">다른 label로 실행</a></p>
<p><a href="/week07">7주차 메인으로 이동</a></p>
</body>
</html>
