<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 - 완료</title>
</head>
<body>
<h3>가입 회원 정보</h3>
<strong>메일 : ${registerRequest.email}</strong><br>
<strong>이름 : ${registerRequest.name}</strong><br>
<strong>암호 : ${registerRequest.password}</strong><br>
<strong>확인 : ${registerRequest.confirmPassword}</strong><br>
<br><h3>회원 가입을 완료했습니다.</h3><br>
<p><a href="<c:url value='/main'/>">[첫 화면 이동]</a></p>
<p><a href="/week11">11주차 메인으로 이동</a></p>
</body>
</html>
