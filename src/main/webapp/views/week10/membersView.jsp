<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week10 Member List</title>
</head>
<body>
<h1>Member List</h1>
<c:choose>
	<c:when test="${not empty members}">
		<table border="1">
			<thead>
				<tr>
					<th>Id</th>
					<th>Email</th>
					<th>Name</th>
					<th>Password</th>
					<th>RegDate</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="member" items="${members}" varStatus="status">
					<tr>
						<td>${member.id}</td>
						<td>${member.email}</td>
						<td>${member.name}</td>
						<td>${member.password}</td>
						<td>${member.registerDateTime}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<p>No members found.</p>
	</c:otherwise>
</c:choose>

<p><a href="/week10">10주차 메인으로 이동</a></p>
</body>
</html>
