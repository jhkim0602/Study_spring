<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week07 XML AOP</title>
</head>
<body>
<h1>XML 기반 AOP 설정</h1>
<p><code>week07-aop.xml</code>의 <code>&lt;aop:config&gt;</code> 설정으로 Advice 실행 순서를 확인한다.</p>

<h2>요청 값</h2>
<p>userId: ${userId}</p>
<p>role: ${role}</p>
<p>orderValue: ${orderValue}</p>
<p>minimumValue: ${minimumValue}</p>

<h2>주문 메서드 실행 결과</h2>
<c:choose>
	<c:when test="${orderSuccess}">
		<p>${orderResult}</p>
	</c:when>
	<c:otherwise>
		<p>${orderError}</p>
	</c:otherwise>
</c:choose>

<h2>권한 메서드 실행 결과</h2>
<p>${permissionResult}</p>

<h2>XML Advice 실행 기록</h2>
<c:forEach var="event" items="${events}">
	<p>${event}</p>
</c:forEach>

<p><a href="/week07/xml">XML AOP 성공 조건으로 다시 실행</a></p>
<p><a href="/week07/xml?orderValue=20&minimumValue=50">XML AOP 예외 조건으로 실행</a></p>
<p><a href="/week07/xml?role=user">XML Around role=user로 실행</a></p>
<p><a href="/week07">7주차 메인으로 이동</a></p>
</body>
</html>
