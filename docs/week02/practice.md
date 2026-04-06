# Week 02 Practice

## 주제

Spring Boot 프로젝트에서 JSP 화면과 기본 MVC 구조를 실제 파일 기준으로 살펴보는 단계다.

## 이 실습의 목적

2주차 실습은 "화려한 기능 구현"이 목적이 아니다.  
오히려 아래 질문을 프로젝트 파일로 확인하는 것이 목적이다.

- JSP 파일은 어디에 두는가?
- 뷰 설정은 어디에서 이루어지는가?
- URL과 화면은 왜 자동 연결되지 않는가?

## 현재 프로젝트에서 먼저 볼 파일

- `build.gradle`
- `src/main/resources/application.properties`
- `src/main/webapp/index.jsp`
- `src/main/webapp/views/week02/hello.jsp`
- `src/main/java/com/example/lect8/Lect8Application.java`

## 1. `build.gradle`을 먼저 보는 이유

JSP는 그냥 자바 코드만 있다고 실행되는 것이 아니다.

JSP 관련 의존성이 필요하다.

현재 프로젝트에는 다음 역할의 의존성이 들어 있다.

- `tomcat-embed-jasper`
  - JSP를 컴파일/실행하는 데 필요
- JSTL API / 구현체
  - JSP에서 반복문, 조건문 같은 태그 사용 시 필요

즉 `build.gradle`은

- "이 프로젝트가 어떤 기술을 쓰는가"
- "JSP를 실행할 준비가 되어 있는가"

를 보여주는 첫 번째 파일이다.

## 2. `application.properties`를 보는 이유

현재 프로젝트의 뷰 관련 설정은 여기서 확인할 수 있다.

예:

- `spring.mvc.view.prefix=/views/`
- `spring.mvc.view.suffix=.jsp`

이 뜻은:

- 컨트롤러가 `week02/hello`를 반환하면
- 실제로는 `/views/week02/hello.jsp`를 찾는다는 의미다

즉 이 파일은 "뷰 이름과 실제 파일 경로를 연결하는 힌트"를 준다.

## 3. `src/main/webapp` 구조를 보는 이유

이 폴더는 JSP 화면 파일이 있는 곳이다.

예:

```text
src/main/webapp/
  index.jsp
  views/
    week02/
      hello.jsp
```

읽는 포인트:

- `index.jsp`: 시작 화면
- `views/week02/hello.jsp`: 2주차 실습 화면

## 4. 이 실습에서 중요한 관찰 포인트

### 4-1. 화면 파일이 있다고 URL이 생기지 않는다

초보자는 보통:

- 파일이 있으면 곧 URL도 있다고 생각한다

하지만 웹 프레임워크는 그렇지 않다.

URL을 처리하는 것은 자바 코드 쪽이고,
JSP는 그 결과로 보여주는 화면이다.

### 4-2. 뷰 이름과 JSP 경로는 다르다

컨트롤러가 반환하는 것은 보통:

```java
"week02/hello"
```

같은 뷰 이름이다.

실제 JSP 경로는:

```text
/views/week02/hello.jsp
```

다.

### 4-3. MVC는 파일 구조에도 드러난다

- `java/`: 처리 코드
- `webapp/views/`: 화면 코드
- `resources/`: 설정

즉 폴더 구조 자체가 역할 분리를 보여준다.

## 5. 현재 실습 코드가 보여주는 것

2주차는 일부러 단순하다.  
이 단순함이 중요하다.

왜냐하면 이후 주차에서는 기능이 늘어나지만,
결국 아래 기본 구조는 그대로이기 때문이다.

```text
설정 파일
  -> 요청 처리 코드
  -> 뷰 이름
  -> JSP 화면
```

## 6. 초심자 관점에서 코드 읽는 순서

1. `application.properties`에서 prefix/suffix 확인
2. `webapp/views/week02/hello.jsp` 열어보기
3. 루트 `index.jsp`가 어떤 링크를 제공하는지 보기
4. "이 링크를 누르면 누가 받는가?"를 생각해 보기

이 과정을 통해:

- 화면 파일
- URL 요청
- 자바 처리 코드

가 분리되어 있다는 것을 체감할 수 있다.

## 7. 자주 놓치는 포인트

### JSP는 "화면 파일"이지 "요청 처리 파일"이 아니다

요청 처리의 중심은 Controller다.

### 설정 없이는 화면 경로를 찾기 어렵다

View Resolver 설정이 없다면,
컨트롤러가 돌려준 뷰 이름을 실제 어디로 연결해야 하는지 알기 어렵다.

### 이후 주차도 결국 이 구조 위에 쌓인다

3주차 이후에 Bean, DI, Scope를 배우더라도,
최종적으로는 여전히:

- Controller가 처리하고
- JSP가 보여준다

는 구조는 유지된다.

## 8. 다음 주차로 넘어가기 전 체크리스트

- JSP가 무엇인지 설명할 수 있는가
- Controller가 왜 필요한지 설명할 수 있는가
- View Resolver가 뷰 이름을 실제 경로로 바꾼다는 점을 이해했는가
- `src/main/java`, `src/main/resources`, `src/main/webapp`의 역할을 구분할 수 있는가
