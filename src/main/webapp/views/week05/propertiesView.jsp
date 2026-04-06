<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week05 Properties</title>
</head>
<body>
<h1>외부 설정 프로퍼티</h1>
<p>요약: ${summary}</p>

<h2>@Value 로 주입한 서버 정보</h2>
<p>server.address: ${config.serverAddress}</p>
<p>server.port: ${config.serverPort}</p>

<h2>@ConfigurationProperties 로 매핑한 메일 설정</h2>
<p>host: ${config.host}</p>
<p>port: ${config.port}</p>
<p>timeoutSeconds: ${config.timeoutSeconds}</p>
<p>username: ${config.credentials.username}</p>
<p>password: ${config.credentials.password}</p>

<h3>defaultRecipients</h3>
<c:forEach var="recipient" items="${config.defaultRecipients}">
	<p>${recipient}</p>
</c:forEach>

<p><a href="/week05">5주차 메인으로 이동</a></p>
</body>
</html>
