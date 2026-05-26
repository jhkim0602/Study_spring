<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Student Info View</title>
</head>
<body>
	<h1>학생 정보 출력</h1>

	<c:choose>
		<c:when test="${not empty stdList}">
			<table border="1">
				<thead>
					<tr>
						<th>성명</th>
						<th>학번</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="std" items="${stdList}" varStatus="status">
						<tr>
							<td>${std.stdName}</td>
							<td>${std.stdNum}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>
			<p>학생 정보가 없음</p>
		</c:otherwise>
	</c:choose>
</body>
</html>
