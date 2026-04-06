<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week05 Scope</title>
</head>
<body>
<h1>빈 스코프 비교</h1>

<h2>singleton</h2>
<p>첫 조회: ${singleton1}</p>
<p>두 번째 조회: ${singleton2}</p>
<p>같은 객체 여부: ${singletonSame}</p>

<h2>prototype</h2>
<p>첫 조회: ${prototype1}</p>
<p>두 번째 조회: ${prototype2}</p>
<p>같은 객체 여부: ${prototypeSame}</p>

<h2>서로 다른 범위 의존 처리</h2>
<p>singleton 안에 직접 주입된 prototype id 1: ${directUnit1}</p>
<p>singleton 안에 직접 주입된 prototype id 2: ${directUnit2}</p>
<p>ObjectFactory로 가져온 prototype 1: ${factoryUnit1}</p>
<p>ObjectFactory로 가져온 prototype 2: ${factoryUnit2}</p>

<h2>request / session</h2>
<p>request scope bean: ${requestTrace}</p>
<p>HTTP session id: ${sessionId}</p>
<p>session scope bean: ${sessionTrace}</p>
<p>현재 세션 접근 횟수: ${sessionAccessCount}</p>

<p><a href="/week05">5주차 메인으로 이동</a></p>
</body>
</html>
