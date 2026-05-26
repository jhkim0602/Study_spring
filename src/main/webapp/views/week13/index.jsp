<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.net.URLEncoder" %>
<%
	String now = LocalDateTime.now().withNano(0).toString().replace('T', ' ');
	String encodedDateTime = URLEncoder.encode(now, "UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week13 Index</title>
</head>
<body>
<h1>13주차 커맨드 객체 검증 · 타입 변환 실습</h1>
<p><code>&lt;spring:message&gt;</code> / <code>&lt;form:form&gt;</code> 태그, 수동 Validator, JSR-380 <code>@Valid</code>, 글로벌 Validator, <code>@DateTimeFormat</code>/<code>@NumberFormat</code> 타입 변환을 한 번씩 확인한다.</p>

<ul>
	<li><a href="/week13/message">Ex1. &lt;spring:message&gt; + &lt;form:form&gt; 데모 (검증 없음)</a></li>
	<li><a href="/week13/validation">Ex2. 수동 Validator (RegisterRequestValidator)</a></li>
	<li><a href="/week13/register">Ex3. @Valid + JSR-380 어노테이션 검증</a></li>
	<li><a href="/week13/globalValidator">Ex4. 글로벌 범위 Validator (@Validated)</a></li>
	<li><a href="/week13/convert?number=1234.56&price=1234.56&percentage=0.156&dateTime=<%= encodedDateTime %>">Ex5. @DateTimeFormat / @NumberFormat 타입 변환</a></li>
</ul>

<p><a href="/">루트 인덱스로 이동</a></p>
</body>
</html>
