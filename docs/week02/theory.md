# Week 02 Theory

## 주제

2주차는 Spring MVC의 가장 기본적인 흐름, 즉  
"브라우저 요청이 어떻게 컨트롤러와 JSP를 거쳐 화면이 되는가"를 이해하는 주차다.

## 이 문서를 읽기 전에

2주차는 아직 Bean, DI, Scope를 깊게 다루는 문서가 아니다.  
오히려 아래 질문을 정리하는 문서다.

- 브라우저가 URL을 요청하면 누가 받는가?
- 자바 코드는 어디서 실행되는가?
- JSP는 HTML과 무엇이 다른가?
- 컨트롤러는 왜 필요한가?
- 뷰 이름과 JSP 파일 경로는 어떻게 연결되는가?

이 질문들이 정리되어야 3주차 이후의 Bean, DI, Scope가 자연스럽게 이해된다.

## 현재 프로젝트에서 먼저 볼 코드

- `src/main/resources/application.properties`
- `src/main/webapp/index.jsp`
- `src/main/webapp/views/week02/hello.jsp`
- `src/main/java/Lect_B/week04/Week04IndexController.java`

주의할 점:

현재 통합 프로젝트에는 2주차 전용 컨트롤러가 별도로 남아 있지 않다.  
그래서 **2주차 MVC 개념을 설명할 때는 현재 프로젝트에서 가장 단순한 실제 컨트롤러 코드**를 함께 예제로 본다.

## 목차

- [1. 웹 애플리케이션이란 무엇인가](#1-웹-애플리케이션이란-무엇인가)
- [2. 요청과 응답](#2-요청과-응답)
- [3. MVC는 왜 필요한가](#3-mvc는-왜-필요한가)
- [4. 실제 코드로 보는 가장 단순한 Controller](#4-실제-코드로-보는-가장-단순한-controller)
- [5. JSP는 무엇인가](#5-jsp는-무엇인가)
- [6. View Resolver는 왜 필요한가](#6-view-resolver는-왜-필요한가)
- [7. 브라우저부터 JSP까지 실제 흐름](#7-브라우저부터-jsp까지-실제-흐름)
- [8. 2주차에서 먼저 익혀야 할 어노테이션과 문법](#8-2주차에서-먼저-익혀야-할-어노테이션과-문법)
- [9. 왜 JSP 파일만 있다고 URL이 생기지 않는가](#9-왜-jsp-파일만-있다고-url이-생기지-않는가)
- [10. 2주차 개념이 이후 주차와 어떻게 연결되는가](#10-2주차-개념이-이후-주차와-어떻게-연결되는가)
- [11. 자주 헷갈리는 질문](#11-자주-헷갈리는-질문)
- [12. 기억할 핵심 문장](#12-기억할-핵심-문장)

## 1. 웹 애플리케이션이란 무엇인가

우리가 만드는 것은 콘솔 프로그램이 아니라 웹 애플리케이션이다.

웹 애플리케이션의 가장 단순한 구조:

```text
브라우저
  -> URL 요청
  -> 서버
  -> 자바 코드 처리
  -> HTML 응답
  -> 브라우저 화면 출력
```

현재 프로젝트에 대응시키면:

- 브라우저: 사용자의 크롬, 사파리 등
- 서버: Spring Boot가 띄우는 내장 Tomcat
- 자바 코드: Controller와 그 안에서 호출하는 스프링 빈
- 화면: JSP

즉 2주차의 핵심은 "웹 요청 처리 구조"다.

## 2. 요청과 응답

### 요청(Request)

브라우저가 서버에 보내는 메시지다.

예:

- `/`
- `/hello`
- `/week04`
- `/week06/scopeBean`

같은 URL 접근이 모두 요청이다.

### 응답(Response)

서버가 브라우저에 돌려주는 결과다.

예:

- HTML 화면
- JSON 데이터
- 파일 다운로드
- 에러 페이지

2주차에서는 주로 **HTML 화면 응답**을 본다.

## 3. MVC는 왜 필요한가

MVC는 역할을 나누기 위한 구조다.

### Model

화면에 전달할 데이터다.

예:

- 문자 발신기 객체
- 설정값
- 빈 조회 결과

### View

사용자에게 보여 줄 화면이다.  
이 프로젝트에서는 JSP가 View 역할을 한다.

### Controller

요청을 받고,
어떤 데이터를 준비해서,
어떤 화면으로 보낼지 결정하는 역할이다.

왜 MVC가 필요한가:

- 화면 코드와 자바 로직이 덜 섞인다
- 유지보수가 쉬워진다
- 역할이 분리되어 읽기 쉬워진다

## 4. 실제 코드로 보는 가장 단순한 Controller

현재 프로젝트에서 가장 단순한 실제 컨트롤러 예제는 아래와 같다.

```java
@Controller
public class Week04IndexController {

    @GetMapping({"/week04", "/week04/"})
    public String index() {
        return "week04/index";
    }
}
```

이 코드는 `src/main/java/Lect_B/week04/Week04IndexController.java`에 있다.

이 코드를 줄 단위로 해석하면:

- `@Controller`
  - 이 클래스가 스프링 MVC의 요청 처리용 클래스라는 뜻이다
  - 동시에 스프링이 관리하는 빈이 된다
- `@GetMapping({"/week04", "/week04/"})`
  - GET 요청 `/week04`, `/week04/`를 이 메서드가 처리한다는 뜻이다
- `public String index()`
  - 메서드가 문자열을 반환하면 보통 "뷰 이름"으로 해석된다
- `return "week04/index";`
  - 실제 JSP 전체 경로를 적는 것이 아니라 뷰 이름만 반환한다

즉 컨트롤러는:

1. URL 요청을 받고
2. 적절한 메서드를 실행하고
3. 어떤 화면을 보여 줄지 결정한다

는 흐름의 중심이다.

## 5. JSP는 무엇인가

JSP는 `Java Server Pages`의 약자다.

핵심 아이디어:

- HTML 안에 서버 쪽 동적 표현을 넣을 수 있다
- 서버가 JSP를 처리해 최종 HTML로 만들어 브라우저에 보낸다

즉 브라우저가 JSP 파일 그 자체를 받는 것이 아니라,  
서버가 JSP를 처리한 결과인 HTML을 받는다.

현재 2주차 JSP는 아래처럼 아주 단순하다.

```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello</title>
</head>
<body>

<h1>Week02 JSP Test</h1>

</body>
</html>
```

이 코드는 `src/main/webapp/views/week02/hello.jsp`에 있다.

이 JSP가 보여 주는 것은 "JSP도 결국 화면 파일"이라는 점이다.  
아직 EL, JSTL, 모델 데이터 출력이 많지 않을 뿐이다.

## 6. View Resolver는 왜 필요한가

컨트롤러는 보통 JSP 전체 경로를 직접 쓰지 않는다.

예:

```java
return "week04/index";
```

이 문자열은 실제 파일 경로가 아니라 **뷰 이름**이다.

현재 프로젝트 설정:

```properties
spring.mvc.view.prefix=/views/
spring.mvc.view.suffix=.jsp
```

이 코드는 `src/main/resources/application.properties`에 있다.

이 설정의 뜻:

- prefix는 앞에 붙는 경로
- suffix는 뒤에 붙는 확장자

따라서:

- 뷰 이름: `week04/index`
- 실제 JSP 경로: `/views/week04/index.jsp`

가 된다.

여기서 중요한 개념이 **View Resolver**다.

View Resolver는:

- 컨트롤러가 반환한 뷰 이름을 받고
- prefix/suffix를 붙여
- 실제 JSP 파일 경로를 찾는 역할을 한다

즉 View Resolver가 없으면,
"문자열 하나"가 어떤 JSP 파일을 뜻하는지 스프링이 알기 어렵다.

## 7. 브라우저부터 JSP까지 실제 흐름

예를 들어 사용자가 `/week04`로 들어오면 흐름은 다음과 같다.

```text
브라우저
  -> GET /week04 요청
  -> DispatcherServlet이 요청 수신
  -> Week04IndexController.index() 호출
  -> "week04/index" 반환
  -> View Resolver가 "/views/week04/index.jsp"로 해석
  -> JSP 렌더링
  -> HTML 응답
  -> 브라우저 화면 출력
```

2주차에서 꼭 잡아야 할 것은:

- 브라우저는 URL을 요청한다
- Controller는 자바 코드다
- JSP는 화면 파일이다
- View Resolver가 둘 사이를 연결한다

## 8. 2주차에서 먼저 익혀야 할 어노테이션과 문법

### `@Controller`

의미:

- 이 클래스는 웹 요청 처리용이다
- 동시에 스프링 빈으로 등록된다

왜 필요한가:

- DispatcherServlet이 어떤 클래스를 요청 처리 후보로 볼지 알기 위해서다

### `@GetMapping`

의미:

- 특정 GET URL을 어떤 메서드에 연결한다

왜 필요한가:

- URL과 자바 메서드를 매핑해야 브라우저 요청을 처리할 수 있기 때문이다

### `return "뷰이름"`

의미:

- 보통 JSP 전체 경로를 돌려주는 것이 아니라 뷰 이름을 돌려준다

왜 이렇게 하나:

- 컨트롤러가 경로 규칙 전체를 매번 알지 않아도 되게 하려는 것이다
- prefix/suffix 설정과 결합해 일관된 구조를 만들기 쉽다

### `index.jsp` 안의 링크

현재 루트 화면에는 아래와 같은 링크가 있다.

```jsp
<h3>2주차 실습</h3>
<a href="/hello">hello.jsp 이동</a>
```

이 코드는 `src/main/webapp/index.jsp`에 있다.

이 링크의 의미:

- 브라우저가 `/hello`로 GET 요청을 보낸다
- 서버는 그 요청을 처리할 경로를 찾아야 한다

즉 HTML 링크도 결국은 서버 요청의 시작점이다.

## 9. 왜 JSP 파일만 있다고 URL이 생기지 않는가

초보자가 가장 많이 하는 오해가 이것이다.

> "JSP 파일이 있으니까 URL로 바로 열리겠지?"

하지만 보통은 그렇지 않다.

이유:

- JSP는 화면 파일일 뿐이다
- URL과 JSP 파일이 자동 연결되는 것은 아니다
- 누가 어떤 JSP를 보여 줄지 Controller와 View Resolver가 정해야 한다

즉:

- JSP = 화면
- Controller = 요청 처리
- View Resolver = 뷰 경로 연결

이다.

## 10. 2주차 개념이 이후 주차와 어떻게 연결되는가

2주차를 이해하면 이후 주차에서 같은 패턴이 계속 보인다.

- 3주차: 컨트롤러가 컨테이너에서 빈을 조회해 JSP로 보낸다
- 4주차: DI된 객체를 컨트롤러에서 사용한다
- 5주차: scope/lifecycle 결과를 JSP로 출력한다
- 6주차: 스코프 비교 결과를 다시 화면으로 검증한다

즉 이후 주차가 어려워 보여도,
최종 출력 구조는 계속 MVC다.

## 11. 자주 헷갈리는 질문

### Q1. JSP는 자바 클래스인가?

직접 작성할 때는 HTML 기반 템플릿처럼 보이지만,
서버가 내부적으로 처리할 때는 자바 코드로 변환되어 실행된다.

### Q2. HTML만 쓰면 안 되나?

정적인 화면만 필요하면 HTML만으로도 가능하다.  
하지만 서버 데이터에 따라 내용이 달라져야 하면 JSP 같은 서버 뷰 기술이 필요하다.

### Q3. Controller가 데이터를 직접 화면에 출력하면 안 되나?

가능은 하지만 역할이 섞여 구조가 나빠진다.  
그래서 Controller는 처리와 데이터 준비, View는 화면 생성에 집중시키는 것이 좋다.

### Q4. `ModelAndView`는 2주차에도 알아야 하나?

깊게는 3~4주차에서 더 많이 보지만,
2주차부터 "Controller가 화면 이름과 데이터를 넘긴다"는 개념은 알아 두는 것이 좋다.

## 12. 기억할 핵심 문장

> 브라우저 요청은 컨트롤러가 받고, 컨트롤러는 뷰 이름을 반환하며, View Resolver는 그 이름을 실제 JSP 경로로 연결한다.
