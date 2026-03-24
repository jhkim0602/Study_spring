<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week04 Bean View</title>
</head>
<body>

<h1>XML 기반 DI 확인</h1>

<p>xml 설정에 의해서 설정된, 인젝션한 객체 : ${xmlSms}</p>
<p>생성자를 통한 DI된 객체 : ${xmlDiService.constructorSms}</p>
<p>생성자를 통한 DI된 기본 데이터 : ${xmlDiService.constructorPeriodTime}</p>
<p>Setter를 통해 DI된 객체 : ${xmlDiService.workUnit}</p>
<p>Setter를 통한 DI된 기본 데이터 : ${xmlDiService.msg}</p>

<p><a href="/week04">4주차 메인으로 이동</a></p>

</body>
</html>
