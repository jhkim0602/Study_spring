<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입 폼</title>
<style>
	.error { color: #c00; font-size: 0.9em; margin-left: 6px; }
</style>
</head>
<body>
<h2><spring:message code="member.info" /></h2>

<p>현재 폼 action: <code>${formAction}</code></p>

<form:form modelAttribute="registerRequest" method="post" action="${formAction}">
	<p>
		<label><spring:message code="email" />:<br>
			<form:input path="email" />
			<form:errors path="email" cssClass="error" />
		</label>
	</p>
	<p>
		<label><spring:message code="name" />:<br>
			<form:input path="name" />
			<form:errors path="name" cssClass="error" />
		</label>
	</p>
	<p>
		<label><spring:message code="password" />:<br>
			<form:password path="password" />
			<form:errors path="password" cssClass="error" />
		</label>
	</p>
	<p>
		<label><spring:message code="password.confirm" />:<br>
			<input type="password" name="confirmPassword" id="confirmPassword"
				value="${registerRequest.confirmPassword}" onfocus="this.value = '';">
			<form:errors path="confirmPassword" cssClass="error" />
		</label>
	</p>

	<input type="submit" value="<spring:message code='register.btn' />">
</form:form>

<p><a href="/week13">13주차 메인으로 이동</a></p>
</body>
</html>
