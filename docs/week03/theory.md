# Week 03 Theory

## 주제

3주차는 Spring에서 `Bean`, `Container`, `DI`가 무엇인지 처음 제대로 이해하는 주차다.

## 이 문서를 읽기 전에

3주차를 이해하려면 먼저 아래 질문에 답할 수 있어야 한다.

- 클래스와 객체는 무엇이 다른가?
- 생성자는 언제 쓰는가?
- 자바 객체를 직접 `new` 해서 만드는 것이 무슨 뜻인가?

아직 애매하다면 [공통 기초 문서](../foundation.md)의 `자바 기초 용어`, `빈`, `DI` 부분을 먼저 보고 오는 것이 좋다.

## 현재 프로젝트에서 먼저 볼 코드

- `src/main/java/Lect_B/week03/SmsSender.java`
- `src/main/java/Lect_B/week03/AppConfig.java`
- `src/main/java/Lect_B/week03/WorkUnit.java`
- `src/main/java/Lect_B/week03/HardWorkUnit.java`
- `src/main/java/Lect_B/week03/ContextController.java`
- `src/main/resources/static/xml/Ex1.xml`

이 6개 파일을 같이 봐야 3주차 개념이 연결된다.

## 목차

- [1. 3주차의 핵심 질문](#1-3주차의-핵심-질문)
- [2. 빈은 도대체 무엇인가](#2-빈은-도대체-무엇인가)
- [3. 스프링 컨테이너는 왜 필요한가](#3-스프링-컨테이너는-왜-필요한가)
- [4. DI가 필요한 이유](#4-di가-필요한-이유)
- [5. IoC와 DI의 관계](#5-ioc와-di의-관계)
- [6. 빈 등록 방법 3가지](#6-빈-등록-방법-3가지)
- [7. 자주 쓰는 어노테이션](#7-자주-쓰는-어노테이션)
- [8. DI 방식 3가지](#8-di-방식-3가지)
- [9. 컨테이너에서 빈을 꺼내 쓴다는 의미](#9-컨테이너에서-빈을-꺼내-쓴다는-의미)
- [10. 왜 3주차가 중요한가](#10-왜-3주차가-중요한가)
- [11. 자주 헷갈리는 질문](#11-자주-헷갈리는-질문)
- [12. 시험 대비 핵심 정리](#12-시험-대비-핵심-정리)

## 1. 3주차의 핵심 질문

3주차를 한 문장으로 요약하면 이렇다.

> "객체를 개발자가 직접 만들고 연결할 것인가, 아니면 스프링이 관리하게 할 것인가?"

이 질문이 중요한 이유는, 스프링의 거의 모든 기능이 결국:

- 어떤 객체를 만들고
- 그 객체를 어디에 두고
- 다른 객체와 어떻게 연결할지

의 문제로 이어지기 때문이다.

## 2. 빈은 도대체 무엇인가

빈은 **스프링 컨테이너가 관리하는 객체**다.

### 2-1. 일반 객체와 빈의 차이

```java
SmsSender sender = new SmsSender();
```

이 객체는 일반 객체다.

특징:

- 내가 직접 만들었다
- 내가 언제 만들지 정했다
- 내가 직접 참조를 들고 있다

반면 빈은:

- 스프링이 만든다
- 스프링이 보관한다
- 스프링이 다른 객체에 주입할 수 있다

즉:

- 일반 객체 = 자바 객체
- 빈 = 스프링이 관리하는 자바 객체

### 2-2. 실제 코드로 보는 Bean 후보

`src/main/java/Lect_B/week03/SmsSender.java`

```java
public class SmsSender {

    private final String senderName;

    public SmsSender(String senderName) {
        this.senderName = senderName;
    }
}
```

이 클래스는 그 자체만으로는 그냥 자바 클래스다.  
하지만 이 클래스를 XML, `@Bean`, `@Component` 중 하나로 등록하면 빈이 된다.

즉 중요한 것은 "클래스가 무엇인가"보다  
"스프링 컨테이너에 어떻게 등록되었는가"다.

## 3. 스프링 컨테이너는 왜 필요한가

컨테이너는 빈을 관리하는 공간이다.

주요 역할:

- 객체 생성
- 객체 보관
- 의존 관계 연결
- 설정값 반영
- 생명주기 관리

객체가 몇 개 안 될 때는 직접 관리해도 된다.  
하지만 웹 애플리케이션은 객체 수가 많고 관계가 복잡해진다.

그래서 스프링은 이 관리를 컨테이너에 맡긴다.

### 3-1. 실제 코드로 보는 두 종류의 컨테이너

`src/main/java/Lect_B/week03/ContextController.java`

```java
xmlContext = new XmlWebApplicationContext();
xmlContext.setConfigLocation("classpath:static/xml/Ex1.xml");
xmlContext.refresh();

configContext = new AnnotationConfigWebApplicationContext();
configContext.register(AppConfig.class);
configContext.refresh();
```

이 코드는 두 가지 컨테이너를 비교해서 보여 준다.

- `XmlWebApplicationContext`
  - XML 설정 파일을 읽는 컨테이너
- `AnnotationConfigWebApplicationContext`
  - 자바 설정 클래스(`@Configuration`)를 읽는 컨테이너

여기서 `refresh()`는 매우 중요하다.

의미:

- 설정을 실제로 읽고
- 빈을 만들고
- 컨테이너를 사용 가능한 상태로 초기화하라

즉 컨테이너는 단순한 변수나 맵이 아니라  
"설정을 읽어 실제 Bean 세계를 구성하는 실행 주체"다.

## 4. DI가 필요한 이유

DI는 `Dependency Injection`, 즉 의존성 주입이다.

### 4-1. 의존성이란 무엇인가

어떤 객체가 다른 객체를 필요로 하는 관계다.

예:

```java
public class MessageService {
    private SmsSender smsSender;
}
```

`MessageService`는 `SmsSender` 없이는 일을 못 하므로 `SmsSender`에 의존한다.

### 4-2. 직접 생성 방식의 문제

```java
public class MessageService {
    private final SmsSender smsSender = new SmsSender();
}
```

문제점:

- 구현을 바꾸기 어렵다
- 테스트가 어렵다
- 생성 책임과 사용 책임이 섞인다
- 의존성이 클래스 내부에 숨는다

### 4-3. 주입 방식의 장점

```java
public class MessageService {
    private final SmsSender smsSender;

    public MessageService(SmsSender smsSender) {
        this.smsSender = smsSender;
    }
}
```

장점:

- 필요한 의존성이 생성자에 드러난다
- 다른 구현체로 바꾸기 쉽다
- 테스트가 편해진다
- 클래스가 "사용"에 집중할 수 있다

### 4-4. 실제 코드로 보는 3주차 DI

`src/main/java/Lect_B/week03/HardWorkUnit.java`

```java
@Component
public class HardWorkUnit {

    @Autowired
    @Qualifier("configSms")
    private SmsSender autoSms;

    private WorkUnit workUnit;
    private String msg;

    @Autowired
    public HardWorkUnit(WorkUnit workUnit) {
        this.workUnit = workUnit;
    }

    @Value("${message.greeting}")
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```

이 한 클래스 안에 3주차 핵심이 거의 다 들어 있다.

- `@Component`
  - 이 클래스 자체를 빈으로 등록
- `@Autowired`
  - 필요한 빈을 자동 주입
- `@Qualifier("configSms")`
  - 같은 타입 빈 중 특정 이름을 지정
- 생성자
  - `WorkUnit`이 필수 의존성임을 드러냄
- `@Value`
  - 객체가 아니라 설정값 문자열을 주입

즉 DI는 단순히 "객체 하나 넣어 주기"가 아니라,  
"필수 객체, 선택 객체, 설정값"을 구조적으로 연결하는 작업이다.

## 5. IoC와 DI의 관계

### IoC

`Inversion of Control`, 제어의 역전이다.

보통 자바에서는 개발자가 객체 생성과 흐름을 직접 제어한다.  
하지만 스프링에서는 그 제어권의 일부가 프레임워크로 넘어간다.

### DI

DI는 그 IoC를 구현하는 대표적인 방법이다.

정리:

- IoC: 큰 철학/개념
- DI: 실제 구현 방식

쉽게 말하면:

- IoC = "프레임워크가 큰 흐름을 잡는다"
- DI = "그 흐름 안에서 필요한 객체를 외부에서 넣어 준다"

## 6. 빈 등록 방법 3가지

3주차에서 꼭 알아야 하는 포인트는 "빈을 등록하는 방식이 하나가 아니다"라는 점이다.

### 6-1. XML 방식

`src/main/resources/static/xml/Ex1.xml`

```xml
<bean id="xmlSms" class="Lect_B.week03.SmsSender" />
```

의미:

- 빈 이름은 `xmlSms`
- 타입은 `Lect_B.week03.SmsSender`
- 스프링 컨테이너가 이 객체를 관리하라

장점:

- 설정과 코드가 분리된다
- 레거시 시스템과 교육용 비교에 좋다

단점:

- XML이 길어지기 쉽다
- IDE 타입 지원이 자바보다 약하다

### 6-2. Java Config 방식

`src/main/java/Lect_B/week03/AppConfig.java`

```java
@Configuration
public class AppConfig {

    @Bean
    public SmsSender configSms() {
        return new SmsSender("학교실습용 SMS 발신기");
    }
}
```

의미:

- `@Configuration`
  - 이 클래스가 설정 클래스라는 뜻
- `@Bean`
  - 메서드가 반환한 객체를 빈으로 등록
- 빈 이름
  - 기본적으로 메서드 이름 `configSms`

장점:

- 자바 코드라 IDE 지원이 좋다
- 타입 안전성이 좋다
- 현재 실무 스타일과 가깝다

### 6-3. 컴포넌트 스캔 방식

`src/main/java/Lect_B/week03/WorkUnit.java`

```java
@Component
public class WorkUnit {
}
```

의미:

- 패키지 스캔 대상에 이 클래스가 있으면
- 스프링이 자동으로 빈으로 등록한다

장점:

- 설정 코드가 줄어든다
- 개발 속도가 빠르다

단점:

- 자동 등록이 많아지면 출처가 흐릿해질 수 있다

## 7. 자주 쓰는 어노테이션

3주차에서는 어노테이션 이름을 외우는 것보다  
"이 어노테이션이 스프링에게 어떤 명령을 주는가"를 이해해야 한다.

### `@Component`

예제:

```java
@Component
public class WorkUnit {
}
```

역할:

- 일반적인 스프링 빈 등록

### `@Controller`

예제:

```java
@Controller
public class ContextController {
}
```

역할:

- 요청 처리용 클래스라는 뜻
- 동시에 스프링 빈으로 등록

### `@Configuration`

예제:

```java
@Configuration
public class AppConfig {
}
```

역할:

- 설정 클래스라는 뜻
- 이 안의 `@Bean` 메서드를 스프링이 읽는다

### `@Bean`

예제:

```java
@Bean
public SmsSender configSms() {
    return new SmsSender("학교실습용 SMS 발신기");
}
```

역할:

- 메서드 반환 객체를 빈으로 등록

### `@Autowired`

예제:

```java
@Autowired
private SmsSender autoSms;
```

역할:

- 필요한 타입의 빈을 자동으로 연결

### `@Qualifier`

예제:

```java
@Qualifier("configSms")
```

역할:

- 같은 타입 빈이 여러 개일 때
- 어떤 이름의 빈을 쓸지 더 구체적으로 지정

### `@Value`

예제:

```java
@Value("${message.greeting}")
public void setMsg(String msg) {
    this.msg = msg;
}
```

역할:

- 프로퍼티 파일의 설정값을 주입

## 8. DI 방식 3가지

### 8-1. 필드 주입

```java
@Autowired
private SmsSender smsSender;
```

장점:

- 짧다

단점:

- 의존성이 숨는다
- 테스트와 리팩토링이 불편하다

### 8-2. 생성자 주입

```java
@Autowired
public HardWorkUnit(WorkUnit workUnit) {
    this.workUnit = workUnit;
}
```

장점:

- 필수 의존성이 명확하다
- 불변 구조를 만들기 좋다
- 실무에서 가장 권장된다

### 8-3. Setter 주입

```java
@Value("${message.greeting}")
public void setMsg(String msg) {
    this.msg = msg;
}
```

주로:

- 선택적인 의존성
- 외부 설정값

에 자주 쓴다.

핵심:

- "무조건 한 방식만"이 아니라
- **어떤 종류의 값인가**에 따라 쓰임이 달라질 수 있다

## 9. 컨테이너에서 빈을 꺼내 쓴다는 의미

3주차 실습에서는 `getBean()`이 등장한다.

`src/main/java/Lect_B/week03/ContextController.java`

```java
SmsSender xmlSms = xmlContext.getBean("xmlSms", SmsSender.class);
SmsSender configSms = configContext.getBean("configSms", SmsSender.class);
```

이 코드는:

- 내가 `new SmsSender()`를 한 것이 아니라
- 컨테이너가 관리하는 `xmlSms`, `configSms` 빈을 가져오는 것

이다.

즉 `getBean()`은 단순 조회가 아니라
"관리 중인 객체를 받아 사용한다"는 의미다.

그리고 이어서:

```java
mav.setViewName("week03/beanView");
mav.addObject("xmlSms", xmlSms);
mav.addObject("configSms", configSms);
```

를 통해 컨테이너에서 꺼낸 빈을 JSP로 보낸다.

즉 3주차는:

1. 빈 등록
2. 컨테이너 생성
3. 빈 조회
4. 화면 전달

까지를 하나의 흐름으로 보여 준다.

## 10. 왜 3주차가 중요한가

3주차를 이해하면 이후 주차의 거의 모든 개념이 쉬워진다.

- 4주차의 `@Qualifier`
- 4주차의 `@Value`
- 5주차의 scope
- 5주차의 lifecycle
- 6주차의 ObjectFactory

이 전부 "빈을 컨테이너가 관리한다"는 전제를 깔고 있기 때문이다.

즉 3주차는 단순히 Bean이라는 단어를 배우는 주차가 아니라,  
스프링의 세계관을 배우는 주차다.

## 11. 자주 헷갈리는 질문

### Q1. 빈과 객체는 같은 말인가?

완전히 같은 말은 아니다.

- 객체는 자바의 개념
- 빈은 스프링 컨테이너가 관리하는 객체

다.

### Q2. 컨트롤러도 빈인가?

그렇다. `@Controller`가 붙으면 컨트롤러도 빈이다.

### Q3. XML과 Java Config 중 무엇이 더 좋은가?

무조건 하나가 절대적으로 더 좋다고 보기보다는,

- 교육용 비교
- 레거시 프로젝트
- 실무 스타일

에 따라 다르다.  
현재는 Java Config + 어노테이션 방식이 더 많이 쓰인다.

### Q4. `@Autowired`가 다 해 주면 생성자는 왜 배우나?

어떤 의존성이 반드시 필요한지 구조를 드러내기 위해서다.  
실무에서는 생성자 주입이 더 권장된다.

### Q5. `@Value`는 객체 주입이 아닌데 왜 같이 배우나?

스프링은 객체뿐 아니라 설정값도 외부에서 넣어 줄 수 있기 때문이다.  
즉 "주입"의 범위를 넓게 이해해야 한다.

## 12. 시험 대비 핵심 정리

- 빈은 스프링 컨테이너가 관리하는 객체다.
- 컨테이너는 객체 생성, 보관, 주입, 생명주기 관리를 담당한다.
- DI는 필요한 객체를 외부에서 주입받는 방식이다.
- IoC는 큰 개념, DI는 그 구현 방식이다.
- 빈 등록 방식은 XML, Java Config, 컴포넌트 스캔이 있다.
- `@Component`는 자동 등록, `@Bean`은 수동 등록, `@Configuration`은 설정 클래스다.
- `@Autowired`는 주입, `@Qualifier`는 같은 타입 빈 구분, `@Value`는 설정값 주입이다.
