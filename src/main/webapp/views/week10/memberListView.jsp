<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week10 Member Map List</title>
</head>
<body>
<h1>Member List (List&lt;Map&gt; 변환 결과)</h1>
<c:choose>
	<c:when test="${not empty memberList}">
		<table border="1">
			<thead>
				<tr>
					<th>Map Index</th>
					<th>Key</th>
					<th>Name</th>
					<th>Password</th>
					<th>RegDate</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="map" items="${memberList}" varStatus="mapStatus">
					<c:forEach var="entry" items="${map}">
						<tr>
							<td>${mapStatus.index}</td>
							<td>${entry.key}</td>
							<td>${entry.value.name}</td>
							<td>${entry.value.password}</td>
							<td>${entry.value.registerDateTime}</td>
						</tr>
					</c:forEach>
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
