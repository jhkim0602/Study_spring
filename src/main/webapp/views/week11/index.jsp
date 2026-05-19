<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Week11 Index</title>
</head>
<body>
<h1>11주차 요청매핑 · 커맨드객체 · 메시지 다국화 실습</h1>
<p>경로 매핑(@RequestMapping, @PathVariable, RedirectAttributes), 커맨드 객체 자동 바인딩(@ModelAttribute), 리스트/중첩 객체 바인딩, 메시지 다국화(MessageSource, LocaleResolver)를 확인한다.</p>

<ul>
	<li><a href="/regist/step/1">Ex1. 약관 동의(step1) → step2 → step3 흐름</a></li>
	<li><a href="/main">Ex2. 컨트롤러 없이 ViewController로 /main 매핑</a></li>
	<li><a href="/regist/initCommand">Ex4. @ModelAttribute로 커맨드 객체 초기화</a></li>
	<li><a href="/regist/survey">Ex5. 리스트/중첩 객체 커맨드 (설문조사)</a></li>
	<li><a href="/week11/localeResolver">Ex6. 메시지 다국화 선택 화면</a></li>
</ul>

<p><a href="/">루트 인덱스로 이동</a></p>
</body>
</html>
