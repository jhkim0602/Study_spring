<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week05 Auto DI</title>
</head>
<body>
<h1>필드 / 생성자 / Setter 주입</h1>
<p>필드명 기반으로 연결된 Cat 빈: ${obj.cat}</p>
<p>생성자에서 XML 빈으로 주입된 SmsSender: ${obj.sms}</p>
<p>Setter에서 Qualifier로 주입된 Dog 빈: ${obj.dog}</p>
<p>실행 결과: ${sendResult}</p>
<p><a href="/week05">5주차 메인으로 이동</a></p>
</body>
</html>
