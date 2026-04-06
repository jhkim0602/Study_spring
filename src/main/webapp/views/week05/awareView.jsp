<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week05 Aware</title>
</head>
<body>
<h1>Aware 인터페이스 확인</h1>
<p>BeanNameAware로 전달된 이름: ${aware.beanName}</p>
<p>ApplicationContext의 빈 정의 개수: ${aware.beanDefinitionCount}</p>
<p>workUnit이 prototype인가: ${aware.workUnitPrototype}</p>
<p>ApplicationContext에서 직접 조회한 smsSender: ${lookupSms}</p>
<p><a href="/week05">5주차 메인으로 이동</a></p>
</body>
</html>
