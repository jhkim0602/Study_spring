<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week04 Bean View</title>
</head>
<body>

<h1>XML 기반 DI 확인</h1>

<h2>XML 컨테이너에서 가져온 객체</h2>
<p>빈 이름: ${xmlBeanName}</p>
<p>빈 타입: ${xmlSmsType}</p>
<p>객체 내부 값: ${xmlSms.senderName}</p>
<p>실행 결과: ${xmlMessage}</p>

<h2>Java Config 컨테이너에서 가져온 객체</h2>
<p>빈 이름: ${configBeanName}</p>
<p>빈 타입: ${configSmsType}</p>
<p>객체 내부 값: ${configSms.senderName}</p>
<p>실행 결과: ${configMessage}</p>

<p><a href="/week04">4주차 메인으로 이동</a></p>

</body>
</html>
