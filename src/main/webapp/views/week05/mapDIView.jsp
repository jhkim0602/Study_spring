<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week05 Map DI</title>
</head>
<body>
<h1>Map 컬렉션 주입</h1>

<h2>Annotation 기반 Map</h2>
<c:forEach var="entry" items="${annotationAnimalsMap}">
	<p>${entry.key} / ${entry.value.name} / ${entry.value.sound}</p>
</c:forEach>

<h2>XML 기반 Map</h2>
<c:forEach var="entry" items="${xmlAnimalsMap}">
	<p>${entry.key} / ${entry.value.name} / ${entry.value.sound}</p>
</c:forEach>

<p><a href="/week05">5주차 메인으로 이동</a></p>
</body>
</html>
