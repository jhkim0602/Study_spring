<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week07 Index</title>
</head>
<body>
<h1>7주차 AOP 실습</h1>
<p>핵심 로직과 공통 관심사를 분리하고, Advice가 어느 시점에 실행되는지 확인한다.</p>

<ul>
	<li><a href="/week07/before">Before Advice 성공 예제</a></li>
	<li><a href="/week07/before?role=USER">Before Advice 인증 실패 예제</a></li>
	<li><a href="/week07/after">After Returning / After 성공 예제</a></li>
	<li><a href="/week07/after?orderValue=20&minimumValue=50">After Throwing / After 예외 예제</a></li>
	<li><a href="/week07/around">Around Advice 예제</a></li>
	<li><a href="/week07/pointcut">Annotation Pointcut 예제</a></li>
</ul>

<p><a href="/">루트 인덱스로 이동</a></p>
</body>
</html>
