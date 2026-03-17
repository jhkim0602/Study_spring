<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week03 Context</title>
</head>
<body>

<h1>Week03 Context 실습</h1>

<h2>XML 설정 방식</h2>
<p>빈 이름: ${xmlBeanName}</p>
<p>빈 타입: ${xmlBeanType}</p>
<p>발신기 이름: ${xmlSenderName}</p>
<p>실행 결과: ${xmlMessage}</p>

<h2>Java 설정 방식</h2>
<p>빈 이름: ${configBeanName}</p>
<p>빈 타입: ${configBeanType}</p>
<p>발신기 이름: ${configSenderName}</p>
<p>실행 결과: ${configMessage}</p>

</body>
</html>
