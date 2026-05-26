<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>타입 변환 결과</title>
</head>
<body>
<h2>@DateTimeFormat / @NumberFormat 결과</h2>

<%-- <fmt:setLocale> : 페이지 범위에 로케일을 설정 / 출력 형식을 결정 --%>
<fmt:setLocale value="en_US" />

<p>
	<%-- groupingUsed="true" : 천 단위 구분 기호(,) 사용 --%>
	<%-- maxFractionDigits="2" : 소수점 이하 최대 2자리까지 표시 --%>
	Number: <fmt:formatNumber value="${Number}" type="number" groupingUsed="true" maxFractionDigits="2" /><br>
	Obj.number: <fmt:formatNumber value="${obj.number}" type="number" groupingUsed="true" maxFractionDigits="2" />
</p>
<p>
	Price (KRW): <fmt:formatNumber value="${Price}" type="currency" currencyCode="KRW" maxFractionDigits="2" /><br>
	Obj.Price (KRW): <fmt:formatNumber value="${obj.price}" type="currency" currencyCode="KRW" maxFractionDigits="2" />
</p>
<p>
	Percent: <fmt:formatNumber value="${Percentage}" type="percent" maxFractionDigits="2" /><br>
	Obj.percent: <fmt:formatNumber value="${obj.percentage}" type="percent" maxFractionDigits="2" />
</p>
<p>
	dateTime: <fmt:formatDate value="${dateTime}" pattern="yyyy-MM-dd HH:mm:ss" /><br>
	Obj.date: <fmt:formatDate value="${obj.date}" pattern="yyyy-MM-dd HH:mm:ss" />
</p>

<p><a href="/week13">13주차 메인으로 이동</a></p>
</body>
</html>
