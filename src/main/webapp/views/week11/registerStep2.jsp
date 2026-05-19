<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 - Step2</title>
</head>
<body>
<h2>회원 정보 입력</h2>
<form action="/regist/step3" method="post">
	<p>
		<input type="hidden" name="view" value="registerStep3">
		<label>이메일:<br>
			<input type="text" name="email" id="email" value="${registerRequest.email}"
				onfocus="this.value = '';">
		</label>
	</p>
	<p>
		<label>이름:<br>
			<input type="text" name="name" id="name" value="${registerRequest.name}"
				onfocus="this.value = '';">
		</label>
	</p>
	<p>
		<label>비밀번호:<br>
			<input type="password" name="password" id="password" value="${registerRequest.password}"
				onfocus="this.value = '';">
		</label>
	</p>
	<p>
		<label>비밀번호 확인:<br>
			<input type="password" name="confirmPassword" id="confirmPassword"
				value="${registerRequest.confirmPassword}" onfocus="this.value = '';">
		</label>
	</p>
	<input type="submit" value="가입 완료">
</form>
<p><a href="/week11">11주차 메인으로 이동</a></p>
</body>
</html>
