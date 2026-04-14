# Week 07 Theory

## 주제

7주차는 AOP, 즉 `Aspect Oriented Programming`을 다룬다.

AOP는 여러 코드에 반복해서 등장하는 공통 기능을 핵심 로직과 분리하고,
스프링이 메서드 실행 전후에 자동으로 적용하게 만드는 방식이다.

## 이 문서를 읽기 전에

7주차를 이해하려면 먼저 다음 흐름을 기억해야 한다.

- 3주차: 빈은 스프링이 관리하는 객체다
- 4주차: 컨트롤러는 필요한 빈을 DI로 주입받는다
- 5~6주차: 빈은 scope, lifecycle, properties 같은 운영 정책을 가진다

7주차는 이 흐름 위에서 다음 질문으로 넘어간다.

> "이미 만들어진 스프링 빈의 메서드 실행 전후에 공통 기능을 어떻게 적용할 것인가?"

예를 들어 모든 서비스 메서드에 실행 시간 로그를 넣고 싶다고 하자.
그 코드를 서비스 메서드마다 직접 넣으면 핵심 로직과 로그 코드가 섞인다.

AOP는 이 반복 코드를 Aspect 클래스로 빼고,
Advice가 필요한 메서드에 적용되도록 만든다.

여기서 Pointcut은 따로 떨어진 단원이 아니다.
Pointcut은 항상 "이 Advice가 어느 메서드에 적용되는가"를 설명하는 조건으로 읽어야 한다.

## 현재 프로젝트에서 먼저 볼 코드

- [`Week07AopService.java`](../../src/main/java/Lect_B/week07/Week07AopService.java)
- [`Week07AdviceAspect.java`](../../src/main/java/Lect_B/week07/Week07AdviceAspect.java)
- [`AopEventLog.java`](../../src/main/java/Lect_B/week07/AopEventLog.java)
- [`TraceAop.java`](../../src/main/java/Lect_B/week07/TraceAop.java)
- [`Week07AopController.java`](../../src/main/java/Lect_B/week07/Week07AopController.java)
- [`Week07IndexController.java`](../../src/main/java/Lect_B/week07/Week07IndexController.java)
- [`src/main/webapp/views/week07`](../../src/main/webapp/views/week07)

## 목차

- [1. 7주차의 핵심 질문](#1-7주차의-핵심-질문)
- [2. AOP가 필요한 이유](#2-aop가-필요한-이유)
- [3. 핵심 관심사와 공통 관심사](#3-핵심-관심사와-공통-관심사)
- [4. AOP 주요 구성 요소](#4-aop-주요-구성-요소)
- [5. Advice를 적용 시점과 적용 대상으로 함께 읽기](#5-advice를-적용-시점과-적용-대상으로-함께-읽기)
- [6. Aspect 클래스와 JoinPoint](#6-aspect-클래스와-joinpoint)
- [7. Around Advice와 proceed](#7-around-advice와-proceed)
- [8. Spring AOP의 프록시 기반 동작](#8-spring-aop의-프록시-기반-동작)
- [9. XML 기반 AOP 설정](#9-xml-기반-aop-설정)
- [10. 현재 프로젝트에서 구현한 실습 구조](#10-현재-프로젝트에서-구현한-실습-구조)
- [11. 자주 헷갈리는 질문](#11-자주-헷갈리는-질문)
- [12. 시험 대비 핵심 정리](#12-시험-대비-핵심-정리)

## 1. 7주차의 핵심 질문

7주차를 한 문장으로 요약하면:

> "반복되는 공통 기능을 핵심 로직 밖으로 빼서, 필요한 메서드에 자동으로 적용하는 방법"

이다.

서비스 메서드는 원래 업무에 집중해야 한다.

예를 들어 주문 서비스라면 주문을 처리하는 것이 핵심이다.
그런데 실제 프로젝트에서는 다음 코드도 함께 필요하다.

- 메서드 실행 전 로그인 확인
- 메서드 실행 후 결과 로그 기록
- 예외 발생 시 예외 로그 기록
- 실행 시간 측정

이런 코드를 모든 메서드에 직접 작성하면 코드가 금방 복잡해진다.

AOP는 이 문제를 해결하기 위해 등장한다.

## 2. AOP가 필요한 이유

AOP 없이 코드를 작성하면 핵심 로직과 공통 기능이 섞인다.

```java
public String placeOrder(double orderValue, double minimumValue) {
    System.out.println("주문 처리 시작");

    if (orderValue < minimumValue) {
        System.out.println("예외 발생");
        throw new SecurityException("조건불일치");
    }

    System.out.println("주문 처리 성공");
    return "조건일치";
}
```

이 코드는 주문 처리뿐 아니라 로그와 예외 기록까지 직접 담당한다.

문제는 다음과 같다.

- 핵심 로직이 잘 보이지 않는다
- 같은 로그 코드가 여러 메서드에 반복된다
- 로그 정책이 바뀌면 여러 메서드를 수정해야 한다
- 인증, 예외 처리, 실행 시간 측정 같은 공통 기능을 일관되게 적용하기 어렵다

AOP를 사용하면 서비스 메서드는 핵심 로직만 가진다.

```java
public String placeOrder(double orderValue, double minimumValue) {
    if (orderValue < minimumValue) {
        throw new SecurityException("조건불일치");
    }

    return "조건일치";
}
```

로그와 예외 기록은 Aspect가 담당한다.

중요한 변화는 "로그 코드가 사라졌다"가 아니다.
로그 코드가 서비스에서 사라지고, Aspect라는 공통 기능 위치로 이동했다는 점이다.

## 3. 핵심 관심사와 공통 관심사

AOP를 이해하려면 관심사를 두 종류로 나눠야 한다.

### 핵심 관심사

핵심 관심사는 해당 메서드가 원래 처리해야 하는 업무다.

예:

- 주문 처리
- 회원 가입
- 게시글 작성
- 권한 확인

현재 7주차 실습에서는 `Week07AopService`의 메서드들이 핵심 관심사를 가진다.

```java
public String check(String userId, String role) {
    if (!"ADMIN".equals(role)) {
        return userId + " 사용자는 접근 권한이 없습니다. role=" + role;
    }

    return userId + " 사용자는 접근 권한이 있습니다. role=" + role;
}
```

이 메서드의 핵심 관심사는 권한 판단이다.

### 공통 관심사

공통 관심사는 여러 핵심 로직에 반복해서 붙는 기능이다.

예:

- 로그
- 인증
- 예외 기록
- 실행 시간 측정

현재 7주차 실습에서는 `Week07AdviceAspect`가 공통 관심사를 가진다.

즉 AOP는:

- 핵심 관심사: Service
- 공통 관심사: Aspect

로 역할을 나누는 방식이다.

## 4. AOP 주요 구성 요소

PPT에서 중요한 용어는 네 가지다.

### 4-1. Advice

Advice는 실제로 실행되는 공통 기능 메서드다.

예:

```java
@AfterReturning(pointcut = "orderOperation()", returning = "result")
public void logAfterReturning(Object result) {
    eventLog.add("[AfterReturning] 메서드가 정상적으로 종료됨");
}
```

이 메서드는 주문 메서드가 정상 종료된 뒤 실행되는 공통 기능이다.

여기서 `@AfterReturning`은 실행 시점을 말하고,
`pointcut = "orderOperation()"`은 적용 대상을 말한다.

Advice는 항상 두 질문을 같이 던지며 읽어야 한다.

- 언제 실행되는가?
- 어디에 적용되는가?

### 4-2. JoinPoint

JoinPoint는 Advice를 적용할 수 있는 지점이다.

Spring AOP에서는 주로 **스프링 빈의 메서드 실행 시점**이 JoinPoint다.

필드 값 변경이나 생성자 호출까지 모두 다루는 일반 AspectJ와 달리,
Spring AOP는 스프링 빈의 메서드 실행을 중심으로 이해하면 된다.

현재 프로젝트에서는 `Week07AopService`의 public 메서드 실행이 학습 대상 JoinPoint다.

### 4-3. Pointcut

Pointcut은 Advice를 어떤 대상에 적용할지 고르는 조건이다.

Pointcut은 별도 장으로 외우는 것보다,
Advice 어노테이션 안에서 바로 해석하는 편이 학습 효과가 좋다.

예:

```java
@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
public void authenticate(JoinPoint joinPoint) {
}
```

이 코드는 이렇게 읽는다.

- 실행 시점: `@Before`
- 적용 대상: `Week07AopService.performSensitiveOperation(..)`
- 실행 내용: `authenticate()`

또 다른 예:

```java
@Around("@annotation(Lect_B.week07.TraceAop)")
public Object traceAnnotatedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
}
```

이 코드는 이렇게 읽는다.

- 실행 시점: `@Around`
- 적용 대상: `@TraceAop`가 붙은 메서드
- 실행 내용: 어노테이션 대상 메서드 실행 전후 기록

현재 7주차에서 읽어야 할 Pointcut 표현식은 다음과 같다.

| 표현식 | 읽는 법 | 현재 실습에서의 의미 |
|---|---|---|
| `execution(...)` | 메서드 실행 모양으로 대상 선택 | 특정 서비스 메서드에 Advice 적용 |
| `within(...)` | 클래스나 패키지 범위로 대상 선택 | 같은 패키지 전체를 잡을 때 사용 가능 |
| `args(...)` | 전달 인자 타입으로 대상 선택 | 문자열 인자를 받는 메서드 등을 고를 때 사용 가능 |
| `@annotation(...)` | 특정 어노테이션이 붙은 메서드 선택 | `@TraceAop` 대상 추적 |

`within`과 `args`는 현재 화면 실습의 직접 라우트로 분리하지 않았다.
대신 이론에서 `execution`, `@annotation`과 비교해서 읽는다.
실습을 늘리기보다, 우선 실제로 자주 쓰는 적용 방식이 코드 안에서 어떻게 읽히는지에 집중하기 위해서다.

### 4-4. Aspect

Aspect는 Advice와 Pointcut을 묶은 클래스다.

현재 프로젝트에서는 `Week07AdviceAspect`가 Aspect다.

```java
@Aspect
@Component
public class Week07AdviceAspect {
}
```

`@Aspect`는 AOP 설정 클래스임을 나타내고,
`@Component`는 스프링 빈으로 등록되게 한다.

Aspect를 읽을 때는 다음 순서가 좋다.

1. 이 클래스가 어떤 서비스 메서드에 붙는가
2. 각 Advice가 언제 실행되는가
3. Advice 안에서 어떤 정보를 읽거나 바꾸는가
4. 화면에는 어떤 실행 기록이 출력되는가

## 5. Advice를 적용 시점과 적용 대상으로 함께 읽기

Advice 종류는 대상 메서드 실행 시점에 따라 나뉜다.

하지만 시점만 외우면 AOP를 제대로 읽기 어렵다.
반드시 Pointcut 조건과 함께 읽어야 한다.

| 종류 | 실행 시점 | 적용 대상 읽기 | 현재 실습 |
|---|---|---|---|
| `@Before` | 대상 메서드 실행 전 | `performSensitiveOperation(..)` | `/week07/before` |
| `@AfterReturning` | 정상 종료 후 | `orderOperation()`이 가리키는 `placeOrder(..)` | `/week07/after` 성공 |
| `@AfterThrowing` | 예외 발생 후 | `orderOperation()`이 가리키는 `placeOrder(..)` | `/week07/after` 실패 |
| `@After` | 정상/예외와 관계없이 실행 후 | `orderOperation()`이 가리키는 `placeOrder(..)` | `/week07/after` 공통 |
| `@Around` | 실행 전후 전체 | `check(..)` 또는 `@TraceAop` 메서드 | `/week07/around`, `/week07/pointcut` |

### 5-1. Before Advice

대상 메서드가 실행되기 전에 실행된다.

현재 실습에서는 권한이 `ADMIN`인지 확인한다.

```java
@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
public void authenticate(JoinPoint joinPoint) {
}
```

읽는 법:

- `@Before`: 대상 메서드 실행 전
- `execution(...)`: 어떤 메서드인지 지정
- `performSensitiveOperation(..)`: 민감 작업 메서드

권한이 맞지 않으면 예외를 던지고,
대상 메서드는 실행되지 않는다.

### 5-2. After Returning Advice

대상 메서드가 예외 없이 정상 종료된 뒤 실행된다.

```java
@AfterReturning(pointcut = "orderOperation()", returning = "result")
public void logAfterReturning(Object result) {
}
```

읽는 법:

- `@AfterReturning`: 정상 종료 후
- `orderOperation()`: `placeOrder(..)`를 가리키는 Pointcut 메서드
- `returning = "result"`: 반환값을 `result` 파라미터로 받음

반환값을 확인할 수 있으므로 성공 로그에 적합하다.

### 5-3. After Throwing Advice

대상 메서드에서 예외가 발생했을 때 실행된다.

```java
@AfterThrowing(pointcut = "orderOperation()", throwing = "ex")
public void exceptionProcess(Throwable ex) {
}
```

읽는 법:

- `@AfterThrowing`: 예외 발생 후
- `orderOperation()`: `placeOrder(..)`를 가리킴
- `throwing = "ex"`: 예외 객체를 `ex` 파라미터로 받음

예외 메시지를 기록하는 데 적합하다.

### 5-4. After Advice

대상 메서드 실행 후 항상 실행된다.

```java
@After("orderOperation()")
public void logAfter() {
}
```

읽는 법:

- `@After`: 정상 종료와 예외 발생 모두에서 실행
- `orderOperation()`: `placeOrder(..)`에 적용

정상 종료든 예외 발생이든 실행된다는 점에서 `finally`와 비슷하다.

### 5-5. Around Advice

대상 메서드 실행 전후 전체를 감싼다.

```java
@Around("execution(* Lect_B.week07.Week07AopService.check(..))")
public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
}
```

읽는 법:

- `@Around`: 실행 전후 전체
- `execution(...)`: `check(..)` 메서드에 적용
- `ProceedingJoinPoint`: 대상 메서드 실행을 직접 진행할 수 있음

인자를 바꾸거나, 반환값을 가공하거나, 실행 시간을 측정할 수 있다.

## 6. Aspect 클래스와 JoinPoint

Aspect 클래스는 Pointcut과 Advice를 함께 가진다.

현재 실습의 핵심 클래스는 `Week07AdviceAspect`다.

```java
@Aspect
@Component
public class Week07AdviceAspect {

    private final AopEventLog eventLog;

    public Week07AdviceAspect(AopEventLog eventLog) {
        this.eventLog = eventLog;
    }
}
```

`AopEventLog`를 주입받는 이유는 Advice 실행 기록을 JSP 화면에 보여 주기 위해서다.

### JoinPoint로 얻을 수 있는 정보

Advice 메서드는 `JoinPoint`를 받을 수 있다.

```java
public void authenticate(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
}
```

주요 메서드:

| 메서드 | 의미 |
|---|---|
| `getSignature()` | 호출되는 메서드 정보 |
| `getSignature().getName()` | 메서드 이름 |
| `getTarget()` | 실제 대상 객체 |
| `getArgs()` | 메서드에 전달된 인자 |

즉 JoinPoint는 "지금 어떤 메서드가 어떤 인자로 호출되는가"를 Advice에서 알 수 있게 해 준다.

## 7. Around Advice와 proceed

`@Around`는 `ProceedingJoinPoint`를 사용한다.

```java
@Around("execution(* Lect_B.week07.Week07AopService.check(..))")
public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();
    args[1] = String.valueOf(args[1]).toUpperCase();
    Object result = joinPoint.proceed(args);
    return "[Around 적용 결과] " + result;
}
```

중요한 부분은 `proceed()`다.

- `proceed()`를 호출하면 대상 메서드가 실행된다
- `proceed(args)`를 호출하면 변경된 인자로 대상 메서드가 실행된다
- `proceed()`를 호출하지 않으면 대상 메서드가 실행되지 않는다

현재 실습에서는 `role=admin`으로 요청해도,
Around Advice가 `ADMIN`으로 바꿔 대상 메서드를 실행한다.

`@Around`의 Pointcut 조건도 같이 읽어야 한다.
위 코드는 모든 Around Advice가 아니라, `Week07AopService.check(..)`에만 적용된다.

반면 아래 코드는 적용 기준이 메서드 이름이 아니라 어노테이션이다.

```java
@Around("@annotation(Lect_B.week07.TraceAop)")
public Object traceAnnotatedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
}
```

즉 같은 `@Around`라도 Pointcut 조건에 따라 적용 대상이 달라진다.

## 8. Spring AOP의 프록시 기반 동작

Spring AOP는 일반적으로 프록시 기반으로 동작한다.

흐름은 다음처럼 이해하면 된다.

```text
Controller
  -> AOP Proxy
      -> Advice 실행
      -> Service 메서드 실행
      -> Advice 실행
  -> Controller
```

컨트롤러는 서비스를 직접 호출한다고 생각하지만,
실제로는 스프링이 만든 프록시가 먼저 호출을 받는다.

프록시는 Advice의 Pointcut 조건에 맞는 메서드인지 확인하고,
조건에 맞으면 Advice를 실행한 뒤 실제 서비스 메서드를 호출한다.

### 현재 프로젝트의 AOP 의존성

현재 프로젝트에서는 `build.gradle`에 다음 의존성을 추가했다.

```gradle
implementation 'org.springframework.boot:spring-boot-starter-aspectj'
```

이 의존성이 있어야 `@Aspect`, `@Before`, `@Around` 같은 AOP 기능을 사용할 수 있다.

### Spring AOP에서 주의할 점

- 스프링 빈이 아닌 객체에는 적용되지 않는다
- 필드 값 변경은 Spring AOP의 일반적인 대상이 아니다
- 같은 클래스 내부에서 자기 자신의 메서드를 직접 호출하면 프록시를 거치지 않을 수 있다
- `private` 메서드에는 일반적인 방식으로 적용하기 어렵다

## 9. XML 기반 AOP 설정

PPT에는 XML 스키마 기반 AOP 설정도 포함되어 있다.

XML 방식에서는 다음 태그를 사용한다.

- `<aop:config>`
- `<aop:aspect>`
- `<aop:pointcut>`
- `<aop:before>`
- `<aop:after-returning>`
- `<aop:after-throwing>`
- `<aop:after>`
- `<aop:around>`

예:

```xml
<aop:config>
    <aop:aspect id="aspect1" ref="advice">
        <aop:pointcut
            id="pointcut1"
            expression="execution(public * Lect_B.week07.Week07AopService.businessLogic())" />
        <aop:before pointcut-ref="pointcut1" method="beforeLog" />
    </aop:aspect>
</aop:config>
```

현재 프로젝트에서는 어노테이션 기반 AOP를 사용한다.

이유:

- 기존 주차 코드가 `@Controller`, `@Service`, `@Component` 중심이다
- Spring Boot 프로젝트와 잘 맞는다
- Advice와 적용 조건을 한 클래스에서 함께 읽을 수 있다

## 10. 현재 프로젝트에서 구현한 실습 구조

7주차 실습은 원본 코드의 흐름을 유지하되,
현재 웹 프로젝트 구조에 맞게 다시 구성했다.

### 10-1. Before 실습

URL:

```text
/week07/before
```

확인할 것:

- 대상 메서드 실행 전에 `@Before`가 먼저 실행된다
- `execution(...)` 조건이 `performSensitiveOperation(..)`을 가리킨다
- `JoinPoint`로 메서드 이름과 인자를 읽는다
- `role=USER`이면 예외가 발생하고 대상 메서드는 실행되지 않는다

### 10-2. After 실습

URL:

```text
/week07/after
```

확인할 것:

- `orderOperation()` Pointcut 메서드가 `placeOrder(..)`를 가리킨다
- 성공 시 `@AfterReturning`과 `@After`가 실행된다
- 실패 시 `@AfterThrowing`과 `@After`가 실행된다
- `@After`는 정상/예외와 관계없이 실행된다

### 10-3. Around 실습

URL:

```text
/week07/around
```

확인할 것:

- `@Around`가 대상 메서드 실행 전후를 감싼다
- `execution(...)` 조건이 `check(..)`를 가리킨다
- `role=admin`이 `ADMIN`으로 바뀐다
- 반환값 앞에 `[Around 적용 결과]`가 붙는다

### 10-4. Annotation 기반 적용 대상 실습

URL:

```text
/week07/pointcut
```

확인할 것:

- `@TraceAop`가 붙은 메서드만 Advice 대상이 된다
- `@annotation(...)` 조건으로 특정 어노테이션을 기준으로 대상을 고른다
- 같은 `@Around`라도 `execution(...)`과 `@annotation(...)`은 적용 기준이 다르다

## 11. 자주 헷갈리는 질문

### Q1. AOP는 DI와 같은 것인가?

아니다.

DI는 필요한 객체를 외부에서 주입받는 방식이다.
AOP는 메서드 실행 전후에 공통 기능을 적용하는 방식이다.

둘 다 스프링 컨테이너가 빈을 관리하기 때문에 함께 쓰이는 경우가 많다.

### Q2. `@After`와 `@AfterReturning`은 무엇이 다른가?

`@AfterReturning`은 대상 메서드가 정상 종료됐을 때만 실행된다.

`@After`는 정상 종료든 예외 발생이든 항상 실행된다.

### Q3. `@AfterThrowing`은 예외를 없애 주는가?

기본적으로는 아니다.

`@AfterThrowing`은 예외 발생 후 실행되는 Advice다.
예외를 정상 결과로 바꾸려면 보통 `@Around`에서 직접 처리해야 한다.

### Q4. `@Around`만 쓰면 되는가?

그렇지 않다.

`@Around`는 강력하지만 코드가 복잡해지기 쉽다.
단순히 실행 전 처리만 필요하면 `@Before`가 더 명확하다.

### Q5. Pointcut을 넓게 잡아도 되는가?

너무 넓게 잡으면 원하지 않는 메서드에도 Advice가 적용된다.

예:

```java
@Pointcut("execution(* Lect_B..*(..))")
```

이런 표현식은 학습용으로는 편하지만,
실제 코드에서는 패키지, 클래스, 메서드, 어노테이션 조건을 조합해서 좁히는 편이 안전하다.

### Q6. Pointcut 표현식은 따로 외워야 하는가?

표현식 자체를 외우는 것보다,
Advice 어노테이션 안에서 적용 대상을 읽는 연습이 먼저다.

예:

```java
@After("orderOperation()")
```

이 줄을 보면 `@After`의 실행 시점을 먼저 보고,
`orderOperation()`이 어떤 메서드를 가리키는지 찾아야 한다.

## 12. 시험 대비 핵심 정리

- AOP는 핵심 관심사와 공통 관심사를 분리하는 방식이다.
- Advice는 실행할 공통 기능이다.
- JoinPoint는 Advice를 적용할 수 있는 위치다.
- Spring AOP에서는 주로 스프링 빈의 메서드 실행이 JoinPoint다.
- Pointcut은 Advice 적용 대상을 고르는 조건이다.
- Pointcut은 독립적으로 외우기보다 Advice의 적용 대상을 설명하는 부분으로 읽어야 한다.
- Aspect는 Advice와 Pointcut을 묶은 클래스다.
- `@Before`는 대상 메서드 실행 전에 실행된다.
- `@AfterReturning`은 대상 메서드가 정상 종료된 뒤 실행된다.
- `@AfterThrowing`은 대상 메서드에서 예외가 발생한 뒤 실행된다.
- `@After`는 정상 종료와 예외 발생에 관계없이 실행된다.
- `@Around`는 대상 메서드 실행 전후 전체를 감싼다.
- `@Around`에서는 `ProceedingJoinPoint.proceed()`를 호출해야 대상 메서드가 실행된다.
- `execution`은 메서드 실행 모양을 기준으로 대상을 고른다.
- `within`은 클래스나 패키지 범위를 기준으로 대상을 고른다.
- `args`는 인자 타입을 기준으로 대상을 고른다.
- `@annotation`은 특정 어노테이션이 붙은 메서드를 대상으로 삼는다.
