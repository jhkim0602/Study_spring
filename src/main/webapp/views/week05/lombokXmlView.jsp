<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week05 Lombok XML</title>
</head>
<body>
<h1>XML + Lombok + prototype 실습</h1>
<p>생성자 주입된 SmsSender: ${obj.sms}</p>
<p>생성자 주입된 periodTime: ${obj.periodTime}</p>
<p>Setter 주입된 msg: ${obj.msg}</p>
<p>Setter 주입된 xmlUnit: ${obj.unit}</p>
<p>실행 결과: ${runResult}</p>

<h2>prototype 빈 직접 조회</h2>
<p>추가 조회 1: ${extraUnit1}</p>
<p>추가 조회 2: ${extraUnit2}</p>

<p><a href="/week05">5주차 메인으로 이동</a></p>
</body>
</html>
