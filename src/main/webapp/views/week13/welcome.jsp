<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>가입 완료</title>
</head>
<body>
<spring:message code="register.done">
	<spring:argument value="${registerRequest.name}" />
	<spring:argument value="${registerRequest.email}" />
</spring:message>

<p><a href="/week13">13주차 메인으로 이동</a></p>
</body>
</html>
