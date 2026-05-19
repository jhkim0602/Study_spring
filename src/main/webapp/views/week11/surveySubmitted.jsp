<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>응답 내용</title>
</head>
<body>
<p>응답 내용:</p>
<ul>
	<c:forEach var="response"
		items="${ansData.responses}" varStatus="status">
		<li>${status.index + 1}번 문항: ${response}</li>
	</c:forEach>
</ul>
<p>응답자 위치: ${ansData.res.location}</p>
<p>응답자 나이: ${ansData.res.age}</p>
<p><a href="/week11">11주차 메인으로 이동</a></p>
</body>
</html>
