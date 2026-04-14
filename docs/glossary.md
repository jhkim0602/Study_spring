# Study Glossary

## 이 문서는 왜 필요한가

수업 자료를 읽다 보면 아래처럼 아주 기본적인 질문에서 막히기 쉽다.

- 클래스와 객체가 뭐가 다른가
- 빈은 그냥 객체 아닌가
- 컨테이너가 왜 필요한가
- `@Autowired`, `@Bean`, `@Qualifier`가 각각 무슨 역할인가
- JSP, Controller, ModelAndView가 어떻게 연결되는가

이 문서는 그런 질문을 **짧게 넘기지 않고, 다음 주차 설명을 읽을 수 있을 정도로 이해시키는 용어 사전**이다.

## 1. 자바 기초 용어

### 클래스(Class)

객체를 만들기 위한 설계도다.

예:

```java
public class SmsSender {
}
```

이 코드는 "이런 형태의 객체를 만들 수 있다"는 정의다.  
클래스만 있다고 해서 실제로 동작하는 대상이 생기는 것은 아니다.

### 객체(Object) / 인스턴스(Instance)

클래스로부터 실제로 메모리에 만들어진 실체다.

```java
SmsSender sender = new SmsSender();
```

여기서 `new SmsSender()`가 만든 것이 객체다.  
실무에서는 객체와 인스턴스를 거의 같은 뜻으로 많이 쓴다.

### 필드(Field)

객체가 내부에 가지고 있는 데이터다.

```java
private String senderName;
```

필드는 "상태"를 나타낸다.

### 메서드(Method)

객체가 할 수 있는 동작이다.

```java
public String send(String phoneNumber, String message) {
    return senderName + ": " + message;
}
```

메서드는 "행동"을 나타낸다.

### 생성자(Constructor)

객체가 생성될 때 호출되는 특별한 메서드다.

```java
public SmsSender(String senderName) {
    this.senderName = senderName;
}
```

생성자는 보통:

- 필수 데이터 받기
- 초기 상태 만들기
- 잘못된 상태의 객체 생성을 막기

를 위해 사용한다.

스프링에서 생성자 주입이 중요한 이유도 여기에 있다.  
"이 객체는 무엇이 있어야 제대로 만들어지는가"를 생성자 시그니처가 드러내기 때문이다.

### 패키지(Package)

클래스를 폴더처럼 묶는 단위다.

예:

- `Lect_B.week03`
- `Lect_B.week04`

패키지를 나누는 이유:

- 이름 충돌 방지
- 관련 코드 묶기
- 기능/주차별 분리

### import

다른 패키지의 클래스를 현재 파일에서 쓰기 위해 가져오는 선언이다.

```java
import org.springframework.stereotype.Controller;
```

### 인터페이스(Interface)

"이런 기능을 제공한다"는 약속이다.

예:

```java
public interface Animal {
    String sound();
}
```

인터페이스를 쓰면 구현을 바꿔 끼우기 쉬워진다.  
스프링이 DI를 다루기 좋은 이유도 인터페이스 기반 설계를 잘 지원하기 때문이다.

### 구현체(Implementation)

인터페이스를 실제로 구현한 클래스다.

예:

- `Animal` 인터페이스
- `Dog`, `Cat` 같은 구현 클래스

### 상속(Inheritance)

부모 클래스의 필드/메서드를 자식 클래스가 물려받는 구조다.

상속은 공통 코드를 재사용할 때 쓰지만,  
무조건 많이 쓰는 것이 좋은 것은 아니다.  
스프링에서는 상속보다 **조합과 DI**가 더 자주 중요하게 다뤄진다.

### 추상 클래스(Abstract Class)

공통 기능은 가지고 있지만, 단독으로 객체를 만들기보다는 자식 클래스가 이어받도록 설계된 클래스다.

### 오버라이드(Override)

부모 메서드나 인터페이스 메서드를 자식 클래스에서 다시 구현하는 것이다.

### 어노테이션(Annotation)

코드에 메타정보를 붙이는 문법이다.

예:

- `@Controller`
- `@Component`
- `@Autowired`

스프링은 이 메타정보를 읽고:

- 빈 등록
- 요청 매핑
- 의존성 주입

같은 작업을 수행한다.

### 의존성(Dependency)

어떤 객체가 일을 하기 위해 다른 객체를 필요로 하는 관계다.

예:

```java
private final SmsSender smsSender;
```

이 경우 현재 클래스는 `SmsSender`에 의존한다.

### 컬렉션(Collection)

여러 데이터를 묶어 다루는 자료구조다.

대표 예:

- `List`: 순서가 있는 목록
- `Map`: 키와 값의 쌍

스프링은 컬렉션 자체도 빈으로 만들고 주입할 수 있다.

## 2. 스프링 핵심 용어

### 프레임워크(Framework)

프로그램 전체 구조와 실행 흐름의 틀을 제공하는 도구다.

라이브러리는 "필요할 때 내가 호출"하는 경우가 많고,  
프레임워크는 "전체 흐름 속에서 프레임워크가 내 코드를 호출"하는 경우가 많다.

이 차이가 바로 IoC와 연결된다.

### Spring Boot

스프링 애플리케이션을 더 쉽게 실행하게 도와주는 도구다.

장점:

- 내장 톰캣 제공
- 기본 설정 자동화
- 의존성 관리 편의

### Bean

스프링 컨테이너가 관리하는 객체다.

가장 중요한 문장:

> 모든 빈은 객체지만, 모든 객체가 빈은 아니다.

예:

```java
SmsSender sender = new SmsSender();
```

이건 그냥 일반 객체다.

반면:

```java
@Bean
public SmsSender configSms() {
    return new SmsSender();
}
```

또는

```java
@Component
public class WorkUnit {
}
```

처럼 스프링에 등록되어 컨테이너가 관리하면 빈이다.

빈이 필요한 이유:

- 생성 책임을 컨테이너에 맡길 수 있고
- 의존성 주입이 쉬워지고
- 생명주기와 범위 관리가 가능해지기 때문이다

### 스프링 컨테이너(Spring Container)

빈을 생성하고, 보관하고, 주입하고, 관리하는 주체다.

컨테이너는 단순한 저장소가 아니다.  
다음 일을 한다.

- 빈 생성
- 의존성 연결
- 초기화 호출
- 종료 처리
- 설정값 반영

### ApplicationContext

대표적인 스프링 컨테이너 인터페이스다.

실무에서 "컨텍스트"라고 부를 때 대부분 이것을 가리킨다.

주요 역할:

- `getBean()`으로 빈 조회
- 환경 정보 접근
- 이벤트 처리
- 메시지 처리

### IoC(Inversion of Control)

제어의 역전이다.

원래는 개발자가:

- 객체를 만들고
- 연결하고
- 실행 흐름도 직접 관리한다

스프링에서는 그 큰 흐름을 프레임워크가 잡는다.  
즉 "누가 주도권을 갖는가"가 바뀐다.

### DI(Dependency Injection)

의존성 주입이다.

어떤 객체가 필요한 다른 객체를 직접 생성하지 않고,  
외부에서 받아 사용하는 방식이다.

직접 생성:

```java
private SmsSender sender = new SmsSender();
```

주입 방식:

```java
private final SmsSender sender;

public WorkUnit(SmsSender sender) {
    this.sender = sender;
}
```

DI의 장점:

- 결합도 감소
- 테스트 용이
- 구현 교체 용이
- 역할 분리

### `@Configuration`

이 클래스가 설정 클래스라는 뜻이다.

즉 "빈을 어떻게 만들지 적어 둔 클래스"라고 이해하면 된다.

### `@Bean`

메서드가 반환한 객체를 빈으로 등록한다.

중요 포인트:

- 빈 이름 기본값은 메서드 이름
- 반환 타입이 빈 타입

### 컴포넌트 스캔(Component Scan)

특정 패키지를 훑어서 `@Component`, `@Controller`, `@Service`, `@Repository`가 붙은 클래스를 자동으로 빈 등록하는 기능이다.

### `@Component`

일반적인 스프링 빈으로 등록할 때 쓰는 기본 어노테이션이다.

### `@Controller`

웹 요청을 처리하는 컨트롤러 빈이라는 뜻이다.

즉 단순한 빈 등록 의미도 있지만,  
Spring MVC가 "이 클래스는 요청 처리용"이라고 이해하게 해 준다.

### `@Service`

비즈니스 로직 계층을 나타낼 때 자주 쓰는 어노테이션이다.

기능상 `@Component`와 비슷하지만, 역할 의도가 더 분명해진다.

### `@Repository`

DB 접근 계층을 나타낼 때 자주 쓰는 어노테이션이다.

역할 표현뿐 아니라 예외 변환 같은 스프링 기능과도 연결될 수 있다.

### `@Autowired`

컨테이너가 적절한 빈을 찾아 주입하게 하는 어노테이션이다.

기본적으로 타입 기준으로 찾는다.

그래서 같은 타입 빈이 여러 개면 문제가 생길 수 있다.

### `@Qualifier`

같은 타입 빈이 여러 개일 때 어떤 빈을 쓸지 이름 등으로 더 구체적으로 지정한다.

예:

```java
public HardWorkUnit(@Qualifier("configSms") SmsSender smsSender) {
    this.smsSender = smsSender;
}
```

### `@Value`

프로퍼티 파일의 값을 한 개씩 읽어 주입할 때 쓴다.

예:

```java
@Value("${week06.message.greeting}")
private String greeting;
```

### `@ConfigurationProperties`

관련된 설정을 묶음 단위로 읽어 객체 필드에 바인딩하는 방식이다.

단일 값 몇 개면 `@Value`도 괜찮지만,  
설정이 많아지면 `@ConfigurationProperties`가 더 구조적이다.

### Scope

빈이 얼마나 오래 살아 있고, 어느 범위에서 공유되는지를 뜻한다.

### singleton

기본 스코프다.  
컨테이너 안에서 하나만 만들어 공유한다.

적합한 경우:

- 공통 서비스
- 상태를 오래 들고 있지 않는 객체

### prototype

조회할 때마다 새 객체를 만든다.

적합한 경우:

- 매번 독립 상태가 필요한 객체
- 작업 단위 객체

중요:

prototype이라고 선언했다고 해서 모든 상황에서 자동으로 "매번 새 객체"처럼 행동하는 것은 아니다.  
어디서, 언제, 어떻게 조회되는지도 봐야 한다.

### request scope

하나의 HTTP 요청 동안만 유지되는 스코프다.

### session scope

하나의 사용자 세션 동안 유지되는 스코프다.

### 라이프사이클(Lifecycle)

빈이 생성되고, 초기화되고, 사용되다가, 종료되는 전체 과정이다.

### `@PostConstruct`

의존성 주입이 끝난 뒤 초기화 작업을 할 때 자주 쓴다.

### `@PreDestroy`

컨테이너가 빈을 종료하기 직전에 정리 작업을 할 때 쓴다.

### `InitializingBean`, `DisposableBean`

초기화/종료 시점에 호출되는 인터페이스 방식 훅이다.

지금은 어노테이션 방식이 더 자주 보이지만,  
수업에서는 라이프사이클을 폭넓게 이해하기 위해 함께 배우는 경우가 많다.

### Aware 인터페이스

컨테이너가 빈에게 자신의 메타정보를 알려 주기 위한 인터페이스 계열이다.

예:

- `BeanNameAware`
- `ApplicationContextAware`

즉 "빈이 자신이 어떤 이름으로 등록되었는지",  
"어떤 컨테이너 안에 있는지" 알 수 있게 한다.

### `ObjectFactory`

필요할 때마다 컨테이너에 객체를 다시 요청하는 도구다.

특히:

- singleton 안에서
- prototype을
- 매번 새로 얻고 싶을 때

중요하다.

## 3. 웹/MVC 용어

### 서버(Server)

브라우저 요청을 받아 처리하고 응답을 보내는 쪽이다.

### 톰캣(Tomcat)

자바 웹 애플리케이션을 실행하는 서버/서블릿 컨테이너다.  
Spring Boot는 내장 톰캣을 함께 실행하는 경우가 많다.

### URL / 경로(Path)

브라우저가 요청하는 주소다.

예:

- `/week05`
- `/week06/scopeBean`

### 요청(Request)

브라우저가 서버에 보내는 데이터와 행위다.

### 응답(Response)

서버가 브라우저에 돌려주는 결과다.

HTML, JSON, 파일 등 다양한 형태가 가능하다.

### DispatcherServlet

Spring MVC의 중심 프론트 컨트롤러다.

역할:

- 요청 받기
- 어떤 컨트롤러가 처리할지 찾기
- 결과를 뷰에 연결하기

즉 MVC 요청 흐름의 교통정리 담당자다.

### Controller

웹 요청을 받아 처리하는 자바 클래스다.

주요 일:

- 요청 받기
- 필요한 빈 호출
- 모델 데이터 준비
- 뷰 이름 결정

### Mapping

어떤 URL을 어떤 메서드가 처리할지 연결하는 규칙이다.

예:

```java
@GetMapping("/week05/scope")
```

### Model

뷰에 전달할 데이터다.

예:

- 문자열
- 숫자
- 객체
- 리스트

### View

사용자에게 보여 줄 화면이다.

이 프로젝트에서는 주로 JSP가 View 역할을 한다.

### JSP

서버에서 데이터를 받아 HTML을 만들어 내는 뷰 기술이다.

JSP는 Controller를 대체하지 않는다.  
JSP는 **화면 표현 담당**이다.

### ViewResolver

컨트롤러가 반환한 뷰 이름을 실제 JSP 경로로 바꿔 주는 도구다.

예:

- 컨트롤러 반환: `week05/index`
- prefix: `/views/`
- suffix: `.jsp`
- 실제 파일: `/views/week05/index.jsp`

### `ModelAndView`

뷰 이름과 모델 데이터를 함께 담는 객체다.

예:

```java
ModelAndView mav = new ModelAndView();
mav.setViewName("week03/beanView");
mav.addObject("configSms", configSms);
```

의미:

- 어디로 보여 줄지
- 무엇을 보여 줄지

를 한 번에 담는다.

## 4. 이 용어들이 주차별로 어떻게 이어지는가

### 2주차

- request
- response
- controller
- view
- JSP
- ViewResolver

즉 웹 요청과 화면의 큰 흐름을 배운다.

### 3주차

- bean
- container
- ApplicationContext
- DI
- `@Bean`
- `@Component`

즉 객체 관리의 출발점을 배운다.

### 4주차

- `@Autowired`
- `@Qualifier`
- `@Value`
- `@Configuration`

즉 DI를 실제 코드로 쓰는 방법을 배운다.

### 5주차

- scope
- singleton
- prototype
- lifecycle
- aware
- external properties

즉 빈을 "어떻게 운영하는가"를 배운다.

### 6주차

- 5주차 개념의 재확인
- 결과 비교 중심 학습
- 실습 코드 패턴 재이해

## 5. 가장 자주 하는 질문

### Q1. 빈은 그냥 객체 아닌가?

그냥 객체일 수는 있지만,  
스프링이 관리하기 시작하면 그 객체를 빈이라고 부른다.

차이는 "누가 관리하느냐"다.

### Q2. 왜 내가 `new` 하면 안 되나?

항상 안 되는 것은 아니다.  
다만 스프링이 관리해야 하는 객체를 직접 `new` 하면:

- 주입
- 생명주기
- 스코프
- 설정 주입

같은 스프링 기능을 제대로 활용하기 어려워진다.

### Q3. Controller도 빈인가?

그렇다.  
`@Controller`가 붙어 컨테이너가 관리하면 컨트롤러도 빈이다.

### Q4. JSP도 빈인가?

아니다.  
JSP는 주로 View 파일로 이해하는 것이 맞다.

### Q5. `@Bean`과 `@Component`는 둘 다 왜 필요한가?

둘 다 빈 등록 방식이다.

- `@Component`: 클래스에 직접 붙여 자동 등록
- `@Bean`: 설정 클래스에서 메서드로 수동 등록

상황에 따라 더 적절한 방식이 다르다.
