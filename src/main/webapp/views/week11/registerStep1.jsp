<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>약관 동의 - Step1</title>
</head>
<body>
<h2>약관</h2>
<p>약관 내용</p>
<form action="/regist/step2" method="post">
	<input type="hidden" name="view" value="registerStep2">
	<label>
		<input type="checkbox" name="agree" value="true"> 약관 동의
	</label>
	<input type="submit" value="다음 단계" />
	<c:if test="${not empty message}">
		<p style="color: blue;">${message}</p>
	</c:if>
</form>
<p><a href="/week11">11주차 메인으로 이동</a></p>
</body>
</html>
