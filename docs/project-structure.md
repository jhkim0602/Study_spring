# Project Structure Guide

## 이 문서는 왜 필요한가

스프링을 처음 배우면 코드보다 먼저 폴더 구조에서 막히는 경우가 많다.

- 왜 `java`, `resources`, `webapp`가 나뉘는가
- 왜 JSP는 `views/` 아래에 있는가
- 왜 주차마다 패키지가 따로 있는가
- 왜 어떤 예제는 전역 스캔으로 동작하고, 어떤 예제는 로컬 컨텍스트를 직접 만드는가

이 문서는 **현재 프로젝트 폴더 구조를 실제 파일 기준으로 해설하는 안내서**다.

## 1. 전체 트리 먼저 보기

```text
lect_B/
  build.gradle
  src/
    main/
      java/
        com/example/lect8/
          Lect8Application.java
        Lect_B/
          assignment2223002/
          week03/
          week04/
          week05/
          week06/
          week07/
      resources/
        application.properties
        static/assignment2223002.properties
        static/external.properties
        static/xml/
        xml/
      webapp/
        index.jsp
        views/
          week02/
          week03/
          week04/
          week05/
          week06/
          week07/
    test/
      java/
  docs/
    README.md
    glossary.md
    foundation.md
    project-structure.md
    week02/
    week03/
    week04/
    week05/
    week06/
    week07/
    assignment2223002/
```

이 구조는 아무 이유 없이 복잡한 것이 아니다.  
스프링 웹 프로젝트에서 역할을 분리하기 위해 자연스럽게 생긴 구조다.

## 2. 루트 폴더에서 가장 먼저 봐야 할 것

### `build.gradle`

프로젝트가 어떤 기술을 쓰는지 선언하는 파일이다.

여기에는:

- Spring Boot 의존성
- JSP 실행을 위한 의존성
- 테스트 관련 의존성

같은 정보가 들어 있다.

즉 `build.gradle`은 "이 프로젝트가 무엇으로 구성되는가"를 보여 주는 설계도다.

### `docs/`

코드가 아니라 학습 문서를 두는 공간이다.

왜 코드와 문서를 분리하는가:

- 실행 코드와 설명 텍스트의 책임이 다르기 때문
- 수업 내용 정리를 코드 변경과 분리하기 위해서
- 주차별 복습 경로를 명확하게 만들기 위해서

특히 현재 프로젝트에서는:

- `docs/week02~week07/`에 주차별 문서가 있고
- `docs/assignment2223002/`에 과제 전용 문서가 있다

즉 `docs/`는 단순 메모 폴더가 아니라  
"주차 학습 -> 과제 응용" 흐름까지 연결하는 학습 구조다.

## 3. `src/main/java`는 무엇이 들어가는가

이 폴더에는 실제 자바 코드가 들어간다.

### 3-1. `com/example/lect8/Lect8Application.java`

애플리케이션 시작점이다.

현재 파일:

```java
@SpringBootApplication(scanBasePackages = {"Lect_B.week04", "Lect_B.week05", "Lect_B.week06", "Lect_B.week07"})
```

이 설정이 의미하는 것:

- Spring Boot 애플리케이션 시작
- 컴포넌트 스캔 범위를 `week04~week07`로 제한

왜 이렇게 했는가:

- 주차별 예제를 한 프로젝트에 합치면
- `AppConfig`, `configSms` 같은 이름이 여러 주차에 중복될 수 있고
- 전부 한 번에 스캔하면 빈 충돌이 날 수 있기 때문이다

즉 현재 통합 프로젝트는 **충돌 없이 실행 가능한 주차를 중심으로 전역 스캔**하고 있다.

중요한 점:

- 주차별 패키지를 나눈 것 자체가 문제는 아니다.
- 다만 한 프로젝트 안에 여러 주차 예제를 합치면
- 전역 스캔 범위
- 같은 역할의 설정 클래스
- 외부 설정 파일 위치

를 일관되게 맞추지 않으면 실행 시 혼선이 생길 수 있다.

### 3-2. `Lect_B/week03`

3주차는 Bean, XML 설정, Java Config 비교를 배우는 패키지다.

핵심 파일:

- `AppConfig.java`
- `ContextController.java`
- `SmsSender.java`
- `WorkUnit.java`
- `HardWorkUnit.java`

중요한 해석:

현재 통합 실행에서는 `week03`이 전역 스캔 대상이 아니다.  
이유는 이후 주차와 빈 이름/설정 클래스 이름이 겹칠 수 있기 때문이다.

그래서 3주차 코드는 현재 프로젝트에서:

- 강의용 참조 패키지
- 로컬 컨텍스트 비교 예제

로 읽는 것이 맞다.

즉 3주차는 "통합 서비스 기능"보다  
"Bean 개념을 분리해서 이해하는 실험실"에 가깝다.

### 3-3. `Lect_B/week04`

DI를 실제로 쓰는 주차다.

핵심 파일:

- `DIController.java`
- `AppConfig.java`
- `WorkUnit.java`
- `HardWorkUnit.java`

이 패키지는:

- 컨트롤러
- 설정 클래스
- 주입 대상 클래스

가 함께 있어 "DI 예제를 한 묶음으로" 읽기 좋다.

### 3-4. `Lect_B/week05`

빈 객체 관리 주차다.

핵심 주제:

- scope
- lifecycle
- aware
- external properties

그래서 파일 수도 늘어난다.  
이는 구조가 나빠진 것이 아니라, 배우는 개념 수가 많아졌기 때문이다.

### 3-5. `Lect_B/week06`

5주차 개념을 교수님 실습 코드 흐름 중심으로 다시 확인하는 패키지다.

핵심 파일:

- `BeanScopeConfig.java`
- `BeanScopeController.java`
- `InitDestroyUnit.java`
- `AwareInterfaceImp.java`
- `ExternalConfigComponent.java`

### 3-6. `Lect_B/week07`

AOP를 배우는 패키지다.

핵심 파일:

- `Week07AopService.java`
- `Week07AdviceAspect.java`
- `AopEventLog.java`
- `TraceAop.java`
- `Week07AopController.java`

이 패키지는 핵심 로직과 공통 관심사를 분리해서 읽는 것이 중요하다.

- `Week07AopService`는 핵심 로직을 가진다
- `Week07AdviceAspect`는 인증, 로그, 예외 기록, 실행 시간 측정 같은 공통 기능을 가진다
- JSP 화면은 Advice 실행 기록을 보여 준다

즉 7주차는 "빈을 어떻게 만들고 주입하는가"에서 한 단계 더 나아가,
"빈의 메서드 실행 흐름에 공통 기능을 어떻게 적용하는가"를 확인하는 주차다.

### 3-7. `Lect_B/assignment2223002`

과제 #1 전용 패키지다.

핵심 파일:

- `StdInfo.java`
- `StdManager.java`
- `Assignment2223002Config.java`
- `Assignment2223002Main.java`

이 패키지는 주차 예제와 성격이 조금 다르다.

- `week03~week06`에서 배운 내용을 한 문제에 모아 적용한 패키지이고
- 웹 요청 처리보다 Bean, DI, lifecycle을 드러내는 콘솔형 구조를 쓴다

즉 이 패키지는 "새 개념 주차"가 아니라  
"배운 개념을 과제 문제에 옮긴 통합 응용 패키지"라고 보면 된다.

## 4. 왜 `src/main/resources`가 따로 있는가

이 폴더는 "자바 클래스는 아니지만 애플리케이션 실행에 필요한 자원"을 두는 곳이다.

### `application.properties`

전역 설정 파일이다.

예:

- ViewResolver prefix/suffix
- 기본 메시지

즉 "애플리케이션 동작 규칙"을 적는 곳이다.

### `static/external.properties`

교수님 실습 방식에 맞춘 **공통 외부 설정 파일**이다.

현재 프로젝트는 5주차와 6주차 외부 설정 예제를 이 파일 하나로 통합했다.

왜 하나로 합쳤는가:

- 주차별 `external.properties` 파일이 여러 개 있으면
- 파일 위치와 로딩 경로를 헷갈리기 쉽고
- `@PropertySource` 연결 실수로 `@Value`가 실패할 수 있기 때문이다

현재 방식:

- `src/main/resources/static/external.properties` 하나만 유지
- 5주차: `Week05PropertiesConfig`
- 6주차: `Week06PropertiesConfig`

에서 같은 파일을 읽는다.

### `static/assignment2223002.properties`

과제 #1 전용 설정 파일이다.

현재 값:

- `assignment2223002.student-count`

이 파일이 존재하는 이유:

- 과제 조건에서 학생 수를 외부 프로퍼티 파일에 두라고 했기 때문
- 과제 패키지가 주차별 공통 설정과 역할이 다르기 때문

즉 `external.properties`와 달리,  
이 파일은 학습 예제가 아니라 특정 과제 요구사항을 만족시키는 설정 파일이다.

### `static/xml/`

초기 XML 실습 파일이 들어 있다.

예:

- `Ex1.xml`
- `Ex2.xml`

3주차에서 XML 방식 Bean 등록을 설명할 때 참고한다.

### `xml/`

주차별 XML Bean 설정 파일이 들어 있다.

예:

- `week04-beans.xml`
- `week05-ex1.xml`
- `week05-ex2.xml`

왜 자바 코드가 아닌 `resources`에 두는가:

- XML은 클래스가 아니라 설정 자원
- 클래스패스에서 읽어 오는 대상
- 컴파일 대상 소스와 역할이 다르기 때문

### 외부 설정 참고 사항

예전에는 주차별 외부 설정 파일을 각각 두었지만,
현재 프로젝트는 교수님 실습 방식에 맞춰 `static/external.properties` 하나만 사용한다.

주의:

- 문제의 본질은 "주차별 패키지" 자체가 아니라
- 통합 프로젝트에서 외부 설정 파일과 스캔 범위를 일관되게 맞추지 않은 데 있다

즉 패키지를 주차별로 나누는 것은 괜찮지만,
설정 파일 로딩 경로는 한 방식으로 통일하는 편이 안전하다.

## 5. 왜 JSP는 `src/main/webapp`에 있는가

이 폴더는 웹 화면 파일을 두는 위치다.

핵심 파일:

- `src/main/webapp/index.jsp`
- `src/main/webapp/views/week02/*.jsp`
- `src/main/webapp/views/week03/*.jsp`
- `src/main/webapp/views/week04/*.jsp`
- `src/main/webapp/views/week05/*.jsp`
- `src/main/webapp/views/week06/*.jsp`
- `src/main/webapp/views/week07/*.jsp`

### `index.jsp`

루트 진입 화면이다.  
각 주차 메뉴로 이동하는 링크를 모아 둔다.

### `views/weekXX/`

주차별 JSP 화면 모음이다.

왜 `views/` 아래에 두는가:

- View 파일임을 명확히 하기 위해서
- ViewResolver 설정과 자연스럽게 연결되기 때문이다

현재 `application.properties`에는 보통 다음처럼 설정되어 있다.

```properties
spring.mvc.view.prefix=/views/
spring.mvc.view.suffix=.jsp
```

따라서 컨트롤러가:

```java
return "week05/index";
```

를 반환하면 실제로는:

```text
/views/week05/index.jsp
```

를 찾는다.

## 6. 왜 주차별 패키지와 뷰 폴더를 같이 나눴는가

이 프로젝트는 일반 서비스 개발 프로젝트이면서 동시에 수업용 학습 프로젝트다.

따라서 기능별 분리뿐 아니라 **주차 흐름 보존**도 중요하다.

주차별로 나누는 장점:

- 수업 진행 순서를 그대로 보존할 수 있다
- 같은 개념이 확장되는 지점을 비교하기 쉽다
- 시험 직전에 범위별 복습이 쉽다
- 실습 파일 이름과 PPT 주제를 연결하기 좋다

## 7. 왜 Controller와 Bean 클래스를 나눠 두는가

예를 들어 `week05`를 보면:

- `Week05PracticeController`
- `ScopeSingletonClient`
- `ScopeFactoryClient`
- `Week05ExternalConfigComponent`

같은 파일이 분리되어 있다.

이유:

- Controller는 웹 요청 처리 담당
- Bean 클래스는 비즈니스/실습 로직 담당
- 설정 클래스는 빈 생성 규칙 담당
- JSP는 화면 담당

즉 스프링은 파일 구조에서도 **역할 분리**를 강하게 권장한다.

## 8. 요청 하나가 폴더 구조를 어떻게 지나가는가

예를 들어 `/week05/scope` 요청을 보면:

```text
브라우저
  -> /week05/scope 요청
  -> 내장 Tomcat
  -> DispatcherServlet
  -> Week05PracticeController
  -> ScopeSingletonClient / ScopeFactoryClient / request/session 빈 사용
  -> ModelAndView 또는 모델 데이터 구성
  -> ViewResolver
  -> /views/week05/scopeView.jsp
  -> HTML 응답
```

즉 하나의 요청은:

- `java/`의 컨트롤러와 빈 클래스
- `resources/`의 설정
- `webapp/`의 JSP

를 모두 거친다.

## 9. Eclipse에서 이 구조를 어떻게 읽어야 하는가

초심자는 보통 코드를 파일 하나씩 읽다가 길을 잃는다.  
아래 순서를 추천한다.

### 화면부터 출발하고 싶을 때

1. `src/main/webapp/index.jsp`
2. 주차별 `views/weekXX/index.jsp`
3. 해당 URL을 처리하는 Controller
4. Controller가 사용하는 Bean/Config 클래스

### 개념부터 출발하고 싶을 때

1. `docs/glossary.md`
2. `docs/foundation.md`
3. 해당 주차 `docs/weekXX/README.md`
4. 주차별 `theory.md`, `practice.md`
5. 실제 코드

## 10. 스프링이 이런 구조를 선호하는 이유

핵심 이유는 세 가지다.

### 이유 1. 역할 분리

- 요청 처리
- 객체 관리
- 설정 관리
- 화면 생성

을 한 파일에 섞지 않기 위해서다.

### 이유 2. 유지보수성

파일 역할이 분명하면:

- 어디를 수정해야 할지 찾기 쉽고
- 오류 원인을 좁히기 쉬우며
- 팀 작업에도 유리하다

### 이유 3. 프레임워크 규약과 잘 맞기 때문

스프링은:

- 컴포넌트 스캔
- 클래스패스 리소스 로딩
- ViewResolver

같은 규약 기반 구조를 잘 지원한다.

즉 폴더 구조가 단순 취향이 아니라,  
프레임워크 동작 방식과 맞물려 있다.

## 11. 이 프로젝트를 읽을 때 꼭 기억할 점

- `java`는 실행 로직
- `resources`는 설정/자원
- `webapp`은 JSP 화면
- `docs`는 학습 문서
- `assignment2223002/`는 주차별 학습 내용을 묶은 과제 응용 영역

그리고 주차별 패키지 분리는 단순 정리가 아니라  
강의 흐름, 개념 확장, 빈 충돌 방지까지 고려한 결과다.
