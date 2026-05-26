<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입력 오류</title>
</head>
<body>
<h2>입력값에 오류가 있습니다.</h2>
<p>
	<spring:message code="validator.fail">
		<spring:argument value="${registerRequest.name}" />
		<spring:argument value="${registerRequest.email}" />
	</spring:message>
</p>
<p><a href="/week13/globalValidator">다시 입력하기</a></p>
<p><a href="/week13">13주차 메인으로 이동</a></p>
</body>
</html>
