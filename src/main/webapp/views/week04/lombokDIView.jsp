<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week04 Lombok DI View</title>
</head>
<body>

<h1>lombok을 이용한 DI 예제</h1>

<h2>컴포넌트 스캔 + Lombok</h2>
<p>SmsSender 객체: ${obj.configSms}</p>
<p>WorkUnit 객체: ${obj.week04WorkUnit}</p>
<p>설정값(message.greeting): ${obj.msg}</p>
<p>실행 결과: ${sendResult}</p>

<h2>XML + Lombok 생성자 주입</h2>
<p>SmsSender 객체: ${xmlObj.smsSender}</p>
<p>periodTime 값: ${xmlObj.periodTime}</p>
<p>실행 결과: ${xmlSendResult}</p>

<p><a href="/week04">4주차 메인으로 이동</a></p>

</body>
</html>
