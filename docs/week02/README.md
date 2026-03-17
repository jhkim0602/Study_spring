# Week 02

## 주제

Spring Boot 프로젝트에서 JSP 화면과 기본 MVC 구조를 살펴보는 단계.

## 현재 프로젝트에서 확인한 내용

- 메인 클래스: `com.example.lect8.Lect8Application`
- JSP 파일:
  - `src/main/webapp/index.jsp`
  - `src/main/webapp/views/week02/hello.jsp`
- 설정 후보 클래스:
  - `src/main/java/Lect_B_week02/WebConfig.java`

## 공부한 핵심 개념

### 1. Spring Boot와 JSP

- Spring Boot는 기본적으로 템플릿 엔진을 많이 사용하지만 JSP도 설정해서 사용할 수 있다.
- JSP를 쓰려면 Jasper, JSTL 관련 의존성이 필요하다.
- 현재 프로젝트의 `build.gradle`에는 JSP 실행에 필요한 의존성이 들어 있다.

### 2. 화면 파일과 요청 매핑은 별개다

- JSP 파일이 존재한다고 해서 URL로 바로 열리는 것은 아니다.
- 브라우저 요청 URL과 JSP 화면은 컨트롤러 또는 뷰 설정을 통해 연결해야 한다.

### 3. 현재 상태에서 확인한 점

- 서버는 실행되지만 `/`, `/hello`, `/home` 요청은 바로 연결되지 않는다.
- 이유는 컨트롤러와 실제 뷰 연결 설정이 아직 완성되지 않았기 때문이다.

## 실습 전 체크 포인트

- URL을 누가 받을 것인가
- 어떤 뷰 이름을 반환할 것인가
- 실제 JSP 경로가 뷰 리졸버 설정과 맞는가

## 다음에 실습할 것

- 컨트롤러 작성
- 뷰 리졸버 설정 확인
- JSP와 URL 연결
