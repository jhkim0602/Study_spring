<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week04 DI View</title>
</head>
<body>

<h1>ApplicationContext 기반 DI 확인</h1>

<h2>주입된 컨테이너와 빈</h2>
<p>ApplicationContext 타입: ${contextType}</p>
<p>WorkUnit 타입: ${workUnitType}</p>
<p>WorkUnit 이름: ${workUnitName}</p>
<p>SmsSender 타입: ${senderType}</p>
<p>SmsSender 이름: ${senderName}</p>

<h2>설정값 주입 결과</h2>
<p>message.greeting 값: ${greeting}</p>
<p>실행 결과: ${resultMessage}</p>

<p><a href="/week04">4주차 메인으로 이동</a></p>

</body>
</html>
