# Spring/JSP Foundation

## 이 문서는 누구를 위한가

이 문서는 다음과 같은 상태의 학습자를 위한 공통 기초 문서다.

- "자바 클래스와 객체가 아직 헷갈린다"
- "빈이 그냥 객체랑 뭐가 다른지 모르겠다"
- "왜 폴더가 이렇게 많은지 모르겠다"
- "브라우저 요청이 어떻게 JSP까지 가는지 흐름이 잘 안 잡힌다"
- "주차별 문서를 읽기 전에 공통 용어를 먼저 잡고 싶다"

즉, 이 문서는 **주차별 문서 전반에 공통으로 깔리는 바닥 개념**을 정리한 문서다.

## 먼저 보면 좋은 문서

- 용어부터 막히면 [glossary.md](glossary.md)
- 실제 파일 위치가 헷갈리면 [project-structure.md](project-structure.md)
- 주차별로 들어가고 싶으면 [README.md](README.md)

## 1. 먼저 전체 그림

이 프로젝트는 단순한 자바 콘솔 프로그램이 아니라 **Spring Boot + JSP 기반 웹 프로젝트**다.

큰 그림은 아래와 같다.

```text
브라우저
  -> URL 요청
  -> 내장 Tomcat 서버
  -> Spring MVC DispatcherServlet
  -> Controller
  -> ModelAndView / 모델 데이터
  -> ViewResolver
  -> JSP
  -> HTML 응답
  -> 브라우저 화면 출력
```

핵심은:

- 브라우저는 URL을 요청한다
- 스프링은 그 요청을 받을 컨트롤러를 찾는다
- 컨트롤러는 데이터를 준비한다
- JSP는 그 데이터를 이용해 화면을 만든다

## 2. 프로젝트 폴더 구조는 왜 이렇게 생겼는가

이 프로젝트의 중요한 폴더는 아래와 같다.

```text
lect_B/
  build.gradle
  src/
    main/
      java/
        com/example/lect8/
        Lect_B/week03/
        Lect_B/week04/
        Lect_B/week05/
        Lect_B/week06/
        Lect_B/week07/
      resources/
        application.properties
        static/xml/
        static/external.properties
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
```

각 위치의 의미:

- `src/main/java`
  - 실제 자바 코드가 들어가는 위치다
  - 컨트롤러, 설정 클래스, 서비스, 실습용 빈 클래스가 여기에 있다
- `src/main/resources`
  - 설정 파일, properties, XML Bean 설정 파일 등이 들어간다
- `src/main/webapp`
  - JSP 파일이 들어간다
- `src/test/java`
  - 테스트 코드가 들어간다
- `docs`
  - 수업 내용을 사람이 읽기 좋은 형태로 정리한 문서다

왜 주차별 패키지를 나눴는가:

- 각 주차의 실습 목적이 다르다
- 이전 주차 코드와 다음 주차 코드를 비교하기 쉽다
- 교수님 수업 흐름을 보존하기 좋다
- 시험 전 복습할 때 "몇 주차 내용이었지?"를 바로 찾을 수 있다

## 3. 자바 기초 용어 먼저 정리

스프링을 이해하려면 자바 용어가 먼저 정리되어야 한다.

### 3-1. 클래스(Class)

클래스는 객체를 만들기 위한 설계도다.

예:

```java
public class SmsSender {
}
```

이 코드는 "이런 형태의 객체를 만들 수 있다"는 정의다.

### 3-2. 객체(Object) / 인스턴스(Instance)

클래스로부터 실제로 만들어진 것을 객체 또는 인스턴스라고 부른다.

```java
SmsSender sender = new SmsSender();
```

여기서:

- `SmsSender`는 클래스
- `sender`는 참조 변수
- `new SmsSender()`로 생성된 실체가 객체다

실무에서는 객체와 인스턴스를 거의 비슷한 뜻으로 많이 쓴다.

### 3-3. 필드(Field)

객체가 내부에 가지고 있는 데이터다.

```java
private String senderName;
```

### 3-4. 메서드(Method)

객체가 할 수 있는 동작이다.

```java
public String send(String phoneNumber, String message) {
    return senderName + message;
}
```

### 3-5. 생성자(Constructor)

객체가 만들어질 때 실행되는 특별한 메서드다.

```java
public SmsSender(String senderName) {
    this.senderName = senderName;
}
```

생성자는 주로:

- 초기값 넣기
- 필수 데이터 받기
- 객체 생성 시 필요한 준비

에 사용된다.

### 3-6. 인터페이스(Interface)

"이 기능을 가진 타입"을 약속하는 규약이다.

예:

```java
public interface Animal {
    String sound();
}
```

장점:

- 구현을 바꿔 끼우기 쉽다
- 결합도를 낮출 수 있다

### 3-7. 상속(Inheritance) / 추상 클래스(Abstract Class)

공통 기능을 부모 클래스에 두고 자식 클래스가 이어받는 방식이다.

예:

- `AbstractCommonService`
- `CommonService`

이런 구조는 "공통 의존성 + 개별 기능"을 분리할 때 자주 쓴다.

### 3-8. 컬렉션(Collection)

여러 객체를 한 번에 담는 자료구조다.

예:

- `List`
- `Map`
- `Set`

스프링은 컬렉션 자체도 빈으로 등록하거나 주입할 수 있다.

## 4. 스프링 기초 용어

### 4-1. 프레임워크(Framework)

프레임워크는 프로그램 구조와 실행 흐름의 큰 틀을 제공하는 도구다.

스프링을 쓰면:

- 객체를 직접 다 만들지 않아도 되고
- 요청 처리 구조를 일정하게 가져갈 수 있고
- 설정, 테스트, 확장이 쉬워진다

### 4-2. 컨테이너(Container)

스프링에서 컨테이너는 객체를 담아두고 관리하는 공간이다.

컨테이너가 하는 일:

- 객체 생성
- 객체 보관
- 의존 관계 연결
- 초기화/종료 관리

### 4-3. 빈(Bean)

빈은 **스프링 컨테이너가 관리하는 객체**다.

중요:

- 모든 빈은 객체다
- 하지만 모든 객체가 빈은 아니다

예:

```java
SmsSender sender1 = new SmsSender();
```

이 객체는 내가 직접 만든 객체다.  
스프링이 관리하지 않으면 그냥 일반 객체다.

반면:

```java
@Component
public class SmsSender {
}
```

또는

```java
@Bean
public SmsSender configSms() {
    return new SmsSender();
}
```

처럼 등록되어 컨테이너가 관리하면 그 객체는 빈이다.

왜 빈이 필요한가:

- 객체 생성 책임을 한곳에 모을 수 있다
- 같은 객체를 여러 곳에서 공유할 수 있다
- 의존 관계를 자동으로 연결할 수 있다
- 생명주기와 범위를 통제할 수 있다

### 4-4. DI(Dependency Injection)

DI는 의존성 주입이다.

뜻:

- 어떤 객체가 다른 객체를 필요로 할 때
- 그 필요한 객체를 외부에서 넣어주는 것

예:

```java
public class MessageService {
    private final SmsSender smsSender;

    public MessageService(SmsSender smsSender) {
        this.smsSender = smsSender;
    }
}
```

여기서 `MessageService`는 `SmsSender`에 의존한다.

스프링이 `SmsSender`를 찾아 생성자에 넣어주면 DI다.

### 4-5. IoC(Inversion of Control)

IoC는 제어의 역전이다.

원래는 개발자가 객체 생성과 흐름을 직접 제어한다.  
하지만 스프링에서는 그 일부를 프레임워크가 가져간다.

즉:

- IoC는 큰 개념
- DI는 IoC를 구현하는 대표적 방법

### 4-6. `ApplicationContext`

스프링 컨테이너를 대표하는 인터페이스다.

많이 하는 일:

- 빈 조회
- 설정 읽기
- 환경 정보 관리

웹 환경에서는 `WebApplicationContext`도 자주 보게 된다.

### 4-7. 설정 클래스(Configuration)

빈을 어떻게 만들지 정하는 자바 설정 클래스다.

```java
@Configuration
public class AppConfig {
}
```

### 4-8. 컴포넌트 스캔(Component Scan)

`@Component`, `@Controller`, `@Service`, `@Repository`가 붙은 클래스를 자동으로 찾아 빈으로 등록하는 기능이다.

### 4-9. 스코프(Scope)

빈이 얼마나 오래 살아 있고, 어떤 범위에서 공유되는지를 뜻한다.

예:

- singleton
- prototype
- request
- session

### 4-10. 라이프사이클(Lifecycle)

빈이 만들어지고 초기화되고 사용되다가 종료되는 전체 생명주기다.

## 5. 웹과 MVC 기초 용어

### 5-1. 클라이언트(Client)

서비스를 요청하는 쪽이다.  
여기서는 보통 브라우저가 클라이언트다.

### 5-2. 서버(Server)

요청을 받아 처리하고 응답하는 쪽이다.

이 프로젝트에서는 스프링 부트가 띄운 톰캣이 웹 서버 역할을 한다.

### 5-3. 요청(Request) / 응답(Response)

- 요청: 브라우저가 서버에 보내는 메시지
- 응답: 서버가 브라우저에 돌려주는 결과

### 5-4. MVC

MVC는 역할 분리 구조다.

- Model: 화면에 전달할 데이터
- View: 사용자에게 보여줄 화면
- Controller: 요청을 받고, 어떤 데이터를 어떤 화면에 보낼지 정하는 역할

왜 MVC가 필요한가:

- HTML/JSP와 자바 로직이 뒤섞이는 것을 줄인다
- 유지보수가 쉬워진다
- 협업이 쉬워진다

### 5-5. JSP

JSP는 서버에서 HTML을 동적으로 만드는 화면 기술이다.

현재 프로젝트에서는 JSP가 View 역할을 한다.

### 5-6. EL / JSTL

- EL(Expression Language): `${obj.name}` 같은 표현으로 JSP에서 값을 쉽게 출력하는 문법
- JSTL: `forEach` 같은 반복/조건 태그 라이브러리

## 6. 이 프로젝트에서 요청이 처리되는 실제 흐름

예를 들어 사용자가 `/week05/scope`를 요청했다고 하자.

```text
브라우저
  -> GET /week05/scope
  -> Spring MVC가 해당 URL을 처리할 Controller 메서드 찾기
  -> Week05PracticeController.scope() 실행
  -> ModelAndView 생성
  -> Model 데이터 담기
  -> View 이름 "week05/scopeView" 반환
  -> ViewResolver가 /views/week05/scopeView.jsp 로 변환
  -> JSP 렌더링
  -> HTML 응답
```

이 흐름이 머리에 들어오면 각 주차의 코드가 훨씬 읽기 쉬워진다.

## 7. 왜 스프링은 객체를 직접 `new` 하지 않게 만들까

핵심 이유는 **결합도 감소**다.

직접 생성 방식:

```java
public class OrderService {
    private final SmsSender smsSender = new SmsSender();
}
```

문제:

- 구현을 바꾸기 어렵다
- 테스트용 가짜 객체를 넣기 어렵다
- 생성 책임과 사용 책임이 섞인다

스프링 방식:

```java
public class OrderService {
    private final SmsSender smsSender;

    public OrderService(SmsSender smsSender) {
        this.smsSender = smsSender;
    }
}
```

장점:

- 어떤 `SmsSender`를 넣을지 바꾸기 쉽다
- 테스트가 쉬워진다
- 객체 관계가 명확해진다

## 8. 문서를 읽는 추천 순서

### 처음 스프링을 보는 경우

1. 이 `foundation.md`
2. `week02/theory.md`
3. `week02/practice.md`
4. `week03/theory.md`
5. `week03/practice.md`
6. `week04` 이후 순서대로

### 빈과 DI가 특히 어려운 경우

1. 이 문서의 `Bean`, `DI`, `ApplicationContext` 부분
2. `week03/theory.md`
3. `week04/theory.md`
4. `week05/theory.md`
5. `week06/theory.md`

### 시험 직전 복습용

1. 각 주차 `README.md`
2. 각 주차 `theory.md`
3. 필요한 주차의 `practice.md`

## 9. 가장 자주 나오는 아주 기초 질문

### Q1. 빈은 그냥 객체 아닌가?

객체는 맞다.  
하지만 **스프링 컨테이너가 관리하느냐 아니냐**가 차이다.

- 내가 `new` 해서 만든 객체: 일반 객체
- 스프링이 생성/보관/주입하는 객체: 빈

### Q2. 컨테이너는 왜 필요한가?

객체가 몇 개 안 되면 직접 관리해도 된다.  
하지만 웹 애플리케이션은 객체 수가 많고 관계가 복잡하다.

컨테이너가 없으면:

- 생성 순서
- 재사용 여부
- 주입 관계
- 종료 시점

을 개발자가 직접 다 관리해야 한다.

### Q3. 컨트롤러도 빈인가?

그렇다.  
`@Controller`가 붙은 클래스도 스프링이 관리하는 빈이다.

### Q4. JSP도 빈인가?

보통은 아니다.  
JSP는 빈이라기보다 View 파일이다.

### Q5. `@Bean`과 `@Component`는 뭐가 다른가?

- `@Component`: 클래스에 직접 붙여 자동 등록
- `@Bean`: 설정 클래스의 메서드로 수동 등록

둘 다 결국 빈을 만든다.  
차이는 등록 방식이다.

## 10. 이 문서 이후에 볼 것

- 용어가 계속 막히면 `glossary.md`
- 현재 프로젝트 파일 배치를 따라가려면 `project-structure.md`
- 2주차: 웹 요청과 JSP/MVC
- 3주차: 빈과 컨테이너, DI의 시작
- 4주차: DI를 실제로 다루는 방법
- 5주차: 빈의 범위, 생명주기, 외부 설정
- 6주차: 5주차 개념을 실습 코드 중심으로 다시 확인
