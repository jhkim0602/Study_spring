# Week 02 Theory

## 주제

Spring MVC의 가장 기본적인 흐름과 JSP 뷰 처리 이해.

## 핵심 개념

### 1. MVC

- Model: 화면에 전달할 데이터
- View: 사용자에게 보여줄 화면
- Controller: 요청을 받아 처리하는 중간 담당

### 2. JSP

- Java Server Pages
- 서버에서 HTML을 동적으로 만들어 응답하는 기술

### 3. View Resolver

- 컨트롤러가 반환한 뷰 이름을 실제 JSP 파일 경로로 바꿔주는 역할

예:

- 뷰 이름: `week02/hello`
- 실제 경로: `/views/week02/hello.jsp`

### 4. 왜 JSP 파일만 있어서는 안 되는가

- JSP 파일은 단순히 화면 파일일 뿐이다
- 브라우저 요청 URL과 JSP 파일은 자동 연결되지 않는다
- 컨트롤러와 뷰 설정이 함께 있어야 화면이 열린다

## 기억할 문장

"JSP는 화면 파일이고, 컨트롤러는 요청을 처리하며, 뷰 리졸버는 뷰 이름을 실제 JSP 경로로 연결한다."
