<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bean View</title>
</head>
<body>

<h1>Bean 생성 실습</h1>

<h2>XML 컨테이너에서 가져온 객체</h2>
<p>빈 이름: xmlSms</p>
<p>빈 타입: ${xmlSmsType}</p>
<p>객체 내부 값: ${xmlSms.senderName}</p>

<h2>Java 설정 컨테이너에서 가져온 객체</h2>
<p>빈 이름: configSms</p>
<p>빈 타입: ${configSmsType}</p>
<p>객체 내부 값: ${configSms.senderName}</p>

</body>
</html>
