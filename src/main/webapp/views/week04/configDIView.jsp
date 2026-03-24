<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week04 Config DI View</title>
</head>
<body>

<h1>config 파일을 이용한 DI 예제</h1>

<h2>SmsSender 빈</h2>
<p>객체 타입: ${obj1Type}</p>
<p>발신기 이름: ${obj1.senderName}</p>
<p>실행 결과: ${sendResult}</p>

<h2>List<String> 빈</h2>
<p>리스트 타입: ${obj2Type}</p>
<ul>
<c:forEach var="item" items="${obj2}">
	<li>${item}</li>
</c:forEach>
</ul>

<p><a href="/week04">4주차 메인으로 이동</a></p>

</body>
</html>
