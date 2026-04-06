<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week05 List DI</title>
</head>
<body>
<h1>List 컬렉션 주입</h1>

<h2>Annotation 기반 List</h2>
<c:forEach var="animal" items="${annotationAnimals}">
	<p>${animal.name} / ${animal.sound}</p>
</c:forEach>

<h2>XML 기반 List</h2>
<c:forEach var="animal" items="${xmlAnimals}">
	<p>${animal.name} / ${animal.sound}</p>
</c:forEach>

<p><a href="/week05">5주차 메인으로 이동</a></p>
</body>
</html>
