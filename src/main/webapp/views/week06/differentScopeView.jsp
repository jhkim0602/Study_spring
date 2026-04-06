<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week06 Different Scope</title>
</head>
<body>
<h1>${title}</h1>

<c:forEach var="unitArray" items="${scopeBeanArray}" varStatus="status">
	<c:forEach var="unit" items="${unitArray}">
		<p>${status.count} : ${unit}</p>
	</c:forEach>
</c:forEach>

<p>같은 객체 여부: ${sameResult}</p>
<p><a href="/week06">6주차 메인으로 이동</a></p>
</body>
</html>
