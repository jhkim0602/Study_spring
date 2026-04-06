<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week06 Aware Interface</title>
</head>
<body>
<h1>Aware 인터페이스 사용</h1>
<p>BeanNameAware 결과: ${beanName}</p>
<p>ApplicationContext ID: ${contextId}</p>

<h2>현재 컨테이너의 빈 이름</h2>
<c:forEach var="name" items="${beanNames}" varStatus="status">
	<p>${status.count} : ${name}</p>
</c:forEach>

<p><a href="/week06">6주차 메인으로 이동</a></p>
</body>
</html>
