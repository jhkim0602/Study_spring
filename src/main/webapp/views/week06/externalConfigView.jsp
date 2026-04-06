<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week06 External Config</title>
</head>
<body>
<h1>외부 설정 파일 사용</h1>
<p>Server Port: ${obj.serverPort}</p>
<p>Server Address: ${obj.serverAddress}</p>
<p>Message: ${obj.greeting}</p>
<hr>
<p>Datasource URL: ${obj.url}</p>
<p>Datasource userName: ${obj.userName}</p>
<p>Datasource password: ${obj.password}</p>

<p><a href="/week06">6주차 메인으로 이동</a></p>
</body>
</html>
