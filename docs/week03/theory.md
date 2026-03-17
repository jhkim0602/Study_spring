# Week 03 Theory

## 주제

어노테이션을 활용한 DI(Dependency Injection) 이해

## 목차

- [1. 먼저 큰 그림](#1-먼저-큰-그림)
- [2. DI가 필요한 이유](#2-di가-필요한-이유)
- [3. DI와 IoC의 관계](#3-di와-ioc의-관계)
- [4. 어노테이션 기반 DI란 무엇인가](#4-어노테이션-기반-di란-무엇인가)
- [5. 자주 쓰는 어노테이션](#5-자주-쓰는-어노테이션)
- [6. DI 방식 3가지](#6-di-방식-3가지)
- [7. 어노테이션 기반 DI 동작 흐름](#7-어노테이션-기반-di-동작-흐름)
- [8. XML 방식과 비교](#8-xml-방식과-비교)
- [9. 시험 대비 핵심 정리](#9-시험-대비-핵심-정리)
- [10. 자주 헷갈리는 질문](#10-자주-헷갈리는-질문)

## 1. 먼저 큰 그림

DI는 `Dependency Injection`의 줄임말이다.  
한국어로는 보통 **의존 주입**이라고 부른다.

여기서 핵심 단어는 두 개다.

- 의존(Dependency): 어떤 객체가 다른 객체를 필요로 하는 관계
- 주입(Injection): 필요한 객체를 외부에서 넣어주는 것

예를 들어 컨트롤러가 `SmsSender`를 사용해야 한다면, 컨트롤러는 `SmsSender`에 의존한다고 말한다.

```java
public class MessageController {
    private SmsSender smsSender;
}
```

이 상태에서 컨트롤러 안에서 직접:

```java
smsSender = new SmsSender();
```

를 하는 대신, 스프링이 `SmsSender` 객체를 만들어 컨트롤러에 넣어주면 이것이 DI다.

## 2. DI가 필요한 이유

### 직접 생성 방식의 문제

```java
public class MessageController {
    private SmsSender smsSender = new SmsSender();
}
```

이 방식은 처음에는 쉬워 보이지만 점점 문제가 생긴다.

- 객체 생성 코드가 클래스 안에 박혀 있다
- 다른 구현체로 바꾸기 어렵다
- 테스트하기 불편하다
- 객체 관계가 복잡해질수록 관리가 어렵다

### DI를 쓰면 좋아지는 점

- 객체 생성 책임을 스프링이 맡는다
- 클래스는 필요한 객체를 "사용"하는 데 집중할 수 있다
- 구현체 교체가 쉬워진다
- 유지보수와 테스트가 쉬워진다

즉, DI는 **결합도를 낮추고 구조를 유연하게 만드는 방법**이다.

## 3. DI와 IoC의 관계

시험에서 자주 같이 나오는 개념이다.

### IoC

IoC는 `Inversion of Control`, 즉 **제어의 역전**이다.

보통 자바 프로그램에서는 개발자가 객체 생성과 실행 흐름을 직접 제어한다.  
하지만 스프링에서는 이 제어권의 일부를 프레임워크가 가져간다.

### DI

DI는 IoC를 구현하는 대표적인 방법이다.

즉 관계는 이렇게 이해하면 된다.

- IoC: 큰 개념
- DI: IoC를 실제로 구현하는 구체적인 방법

한 줄로 정리하면:

> DI는 스프링이 객체를 생성하고 연결하게 하는 IoC의 대표적인 방식이다.

## 4. 어노테이션 기반 DI란 무엇인가

예전 스프링은 XML로 객체를 등록하고 연결하는 방식이 많았다.

예:

```xml
<bean id="smsSender" class="example.SmsSender" />
```

하지만 최근에는 XML보다 **어노테이션 기반 설정**을 더 많이 사용한다.

어노테이션 기반 DI는 다음과 같은 어노테이션을 사용해서

- 어떤 클래스가 Bean인지 표시하고
- 어디에 주입할지 표시하고
- 어떤 설정 클래스를 사용할지 표시하는 방식이다

즉, 설정을 XML 대신 자바 코드와 어노테이션으로 표현하는 것이다.

## 5. 자주 쓰는 어노테이션

### 5-1. `@Component`

가장 기본적인 Bean 등록 어노테이션이다.

```java
@Component
public class SmsSender {
}
```

의미:

- 이 클래스는 스프링이 Bean으로 등록할 대상이다

### 5-2. `@Controller`

웹 요청을 처리하는 컨트롤러 클래스에 붙인다.

```java
@Controller
public class ContextController {
}
```

의미:

- 이 클래스는 요청을 처리하는 컨트롤러 Bean이다

### 5-3. `@Service`

비즈니스 로직을 담당하는 클래스에 붙인다.

의미:

- 역할상 서비스 계층임을 나타낸다
- 기능적으로는 `@Component`와 같은 계열이다

### 5-4. `@Repository`

DB 접근 계층에 붙인다.

의미:

- 역할상 저장소 계층임을 나타낸다

### 5-5. `@Autowired`

필요한 Bean을 자동으로 주입할 때 사용한다.

```java
@Autowired
private SmsSender smsSender;
```

의미:

- 스프링이 `SmsSender` 타입의 Bean을 찾아 넣어준다

### 5-6. `@Configuration`

설정 클래스를 나타낸다.

```java
@Configuration
public class AppConfig {
}
```

의미:

- 이 클래스는 Bean 설정 정보를 담고 있다

### 5-7. `@Bean`

메서드가 반환하는 객체를 Bean으로 등록한다.

```java
@Bean
public SmsSender configSms() {
    return new SmsSender();
}
```

의미:

- 이 메서드의 반환 객체를 스프링 컨테이너가 관리한다

### 5-8. `@ComponentScan`

`@Component`, `@Controller`, `@Service`, `@Repository`가 붙은 클래스를 찾아 자동 등록한다.

```java
@ComponentScan(basePackages = "example")
```

의미:

- 지정한 패키지를 훑어서 Bean 후보를 찾아 등록한다

## 6. DI 방식 3가지

### 6-1. 생성자 주입

```java
public class MessageController {
    private final SmsSender smsSender;

    public MessageController(SmsSender smsSender) {
        this.smsSender = smsSender;
    }
}
```

장점:

- 객체가 생성될 때 필요한 의존성이 반드시 주입된다
- 불변 객체에 유리하다
- 가장 권장되는 방식이다

### 6-2. Setter 주입

```java
public class MessageController {
    private SmsSender smsSender;

    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }
}
```

장점:

- 선택적 의존성 주입에 사용할 수 있다

특징:

- 예전 XML 설정에서 자주 보였다

### 6-3. 필드 주입

```java
@Autowired
private SmsSender smsSender;
```

장점:

- 코드가 짧다

단점:

- 테스트가 불편하다
- 의존성이 드러나지 않는다

실무에서는 보통 **생성자 주입**을 더 권장한다.

## 7. 어노테이션 기반 DI 동작 흐름

전체 흐름을 순서대로 보면 이렇다.

1. 스프링이 설정 클래스와 컴포넌트 스캔 범위를 읽는다
2. 어노테이션이 붙은 클래스를 찾는다
3. Bean 객체를 생성한다
4. 의존 관계를 확인한다
5. 필요한 Bean을 주입한다
6. 컨테이너에 저장하고 관리한다

즉 개발자는:

- 어떤 클래스가 Bean인지 표시하고
- 어떤 객체가 필요한지 선언하면

스프링이 실제 연결을 담당한다.

## 8. XML 방식과 비교

| 구분 | XML 기반 DI | 어노테이션 기반 DI |
|---|---|---|
| 설정 위치 | XML 파일 | 자바 코드 |
| 가독성 | 설정과 코드가 분리됨 | 코드 흐름을 따라가기 쉬움 |
| IDE 지원 | 상대적으로 약함 | 자동완성, 타입 체크에 유리 |
| 유지보수 | XML과 자바를 함께 봐야 함 | 코드 중심으로 보기 쉬움 |
| 학습 관점 | 구조 이해에 도움 | 실무에서 더 자주 사용 |

핵심 정리:

- XML은 "설정을 문서처럼 분리"하는 방식
- 어노테이션은 "설정을 코드 가까이에 붙이는" 방식

## 9. 시험 대비 핵심 정리

### 꼭 외울 문장

> DI는 객체가 필요로 하는 의존 객체를 외부에서 주입받는 방식이다.

> 스프링은 DI를 통해 객체 생성과 의존 관계 연결을 관리한다.

> `@Component`, `@Controller`, `@Service`, `@Repository`는 Bean 등록 계열 어노테이션이다.

> `@Autowired`는 의존 객체를 자동 주입할 때 사용한다.

> `@Configuration`과 `@Bean`은 자바 기반 설정에서 Bean을 등록할 때 사용한다.

### 한 줄 암기

- DI = 필요한 객체를 외부에서 넣어줌
- IoC = 제어권을 스프링이 가짐
- `@Autowired` = 자동 주입
- `@Bean` = 메서드 반환 객체 등록
- `@Controller` = 웹 요청 처리 Bean

## 10. 자주 헷갈리는 질문

### Q1. `@Bean`도 DI와 관련 있는가

관련 있다.  
`@Bean`은 Bean을 등록하는 역할이고, 등록된 Bean은 이후 DI 대상이 된다.

### Q2. `@Autowired`만 붙이면 무조건 주입되는가

기본적으로는 타입이 맞는 Bean이 있어야 한다.  
같은 타입 Bean이 여러 개면 충돌할 수 있어 `@Qualifier` 같은 추가 설정이 필요할 수 있다.

### Q3. `@Component`와 `@Controller`는 무엇이 다른가

둘 다 Bean 등록 계열이지만 역할이 다르다.

- `@Component`: 일반적인 Bean
- `@Controller`: 웹 요청 처리 컨트롤러

### Q4. XML과 어노테이션 중 무엇을 더 많이 쓰는가

최근에는 보통 어노테이션 기반 방식을 더 많이 쓴다.  
다만 XML 방식도 구조 이해와 기존 프로젝트 유지보수 측면에서 여전히 중요하다.

### Q5. 가장 권장되는 주입 방식은 무엇인가

보통 **생성자 주입**이 가장 권장된다.

이유:

- 의존성이 명확하다
- 불변성 유지에 좋다
- 테스트하기 쉽다

## 마무리 정리

이번 이론의 핵심은 아래 한 문장으로 정리할 수 있다.

> 어노테이션 기반 DI는 스프링이 객체를 생성하고 연결하도록 맡기기 위해, Bean 등록과 주입 정보를 어노테이션으로 표현하는 방식이다.
