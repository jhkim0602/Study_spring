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
Pointcut 조건에 맞는 메서드 실행 지점에 Advice를 적용한다.

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
- [5. Advice 종류](#5-advice-종류)
- [6. Pointcut 표현식](#6-pointcut-표현식)
- [7. Aspect 클래스와 JoinPoint](#7-aspect-클래스와-joinpoint)
- [8. Around Advice와 proceed](#8-around-advice와-proceed)
- [9. Spring AOP의 프록시 기반 동작](#9-spring-aop의-프록시-기반-동작)
- [10. XML 기반 AOP 설정](#10-xml-기반-aop-설정)
- [11. 현재 프로젝트에서 구현한 실습 구조](#11-현재-프로젝트에서-구현한-실습-구조)
- [12. 자주 헷갈리는 질문](#12-자주-헷갈리는-질문)
- [13. 시험 대비 핵심 정리](#13-시험-대비-핵심-정리)

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

### 4-2. JoinPoint

JoinPoint는 Advice를 적용할 수 있는 지점이다.

Spring AOP에서는 주로 **스프링 빈의 메서드 실행 시점**이 JoinPoint다.

필드 값 변경이나 생성자 호출까지 모두 다루는 일반 AspectJ와 달리,
Spring AOP는 스프링 빈의 메서드 실행을 중심으로 이해하면 된다.

### 4-3. Pointcut

Pointcut은 Advice를 어떤 대상에 적용할지 고르는 규칙이다.

예:

```java
@Pointcut("execution(* Lect_B.week07.Week07AopService.placeOrder(..))")
private void orderOperation() {
}
```

이 Pointcut은 `Week07AopService`의 `placeOrder` 메서드를 대상으로 삼는다.

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

## 5. Advice 종류

Advice는 대상 메서드 실행 시점에 따라 나뉜다.

| 종류 | 어노테이션 | 실행 시점 | 현재 실습 |
|---|---|---|---|
| Before Advice | `@Before` | 대상 메서드 실행 전 | `/week07/before` |
| After Returning Advice | `@AfterReturning` | 정상 종료 후 | `/week07/after` 성공 |
| After Throwing Advice | `@AfterThrowing` | 예외 발생 후 | `/week07/after` 실패 |
| After Advice | `@After` | 정상/예외와 관계없이 실행 후 | `/week07/after` 공통 |
| Around Advice | `@Around` | 대상 메서드 실행 전후 전체 | `/week07/around` |

### 5-1. Before Advice

대상 메서드가 실행되기 전에 실행된다.

현재 실습에서는 권한이 `ADMIN`인지 확인한다.

```java
@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
public void authenticate(JoinPoint joinPoint) {
}
```

권한이 맞지 않으면 예외를 던지고,
대상 메서드는 실행되지 않는다.

### 5-2. After Returning Advice

대상 메서드가 예외 없이 정상 종료된 뒤 실행된다.

```java
@AfterReturning(pointcut = "orderOperation()", returning = "result")
public void logAfterReturning(Object result) {
}
```

반환값을 확인할 수 있으므로 성공 로그에 적합하다.

### 5-3. After Throwing Advice

대상 메서드에서 예외가 발생했을 때 실행된다.

```java
@AfterThrowing(pointcut = "orderOperation()", throwing = "ex")
public void exceptionProcess(Throwable ex) {
}
```

예외 메시지를 기록하는 데 적합하다.

### 5-4. After Advice

대상 메서드 실행 후 항상 실행된다.

정상 종료든 예외 발생이든 실행된다는 점에서 `finally`와 비슷하다.

### 5-5. Around Advice

대상 메서드 실행 전후 전체를 감싼다.

인자를 바꾸거나, 반환값을 가공하거나, 실행 시간을 측정할 수 있다.

단, `proceed()`를 호출해야 대상 메서드가 실제로 실행된다.

## 6. Pointcut 표현식

Pointcut은 Advice 적용 대상을 고르는 규칙이다.

PPT에서는 다음 네 가지 표현식을 중요하게 다룬다.

- `execution`
- `within`
- `args`
- `@annotation`

### 6-1. `execution`

`execution`은 메서드 실행 모양을 기준으로 대상을 고른다.

기본 형태:

```text
execution([수식어패턴] 리턴타입패턴 클래스이름패턴.메서드이름패턴(파라미터패턴))
```

예:

```java
@Pointcut("execution(* Lect_B.week07..*.*(..))")
```

의미:

- `Lect_B.week07` 패키지와 하위 패키지
- 모든 클래스
- 모든 메서드
- 모든 파라미터

현재 실습에서는 특정 서비스 메서드를 정확히 지정한다.

```java
@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
```

### 6-2. `within`

`within`은 특정 클래스나 패키지 안의 메서드를 대상으로 삼는다.

예:

```java
@Pointcut("within(Lect_B.week07..*)")
```

의미:

- `Lect_B.week07` 패키지와 하위 패키지 안의 모든 클래스

`execution`이 메서드 모양을 자세히 고르는 데 강하다면,
`within`은 클래스나 패키지 범위로 묶을 때 읽기 쉽다.

### 6-3. `args`

`args`는 메서드 인자 타입을 기준으로 대상을 고른다.

예:

```java
@Pointcut("args(java.lang.String, ..)")
```

의미:

- 첫 번째 인자가 `String`
- 뒤에는 0개 이상의 인자가 올 수 있음

`args`는 단독으로 쓰면 범위가 넓어질 수 있으므로,
패키지나 메서드 조건과 함께 쓰는 편이 안전하다.

```java
@Pointcut("execution(* Lect_B.week07..*(..)) && args(java.lang.String, ..)")
```

### 6-4. `@annotation`

`@annotation`은 특정 어노테이션이 붙은 메서드를 대상으로 삼는다.

현재 프로젝트에서는 `TraceAop` 어노테이션을 만들었다.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TraceAop {
}
```

그리고 Aspect에서 다음처럼 사용한다.

```java
@Around("@annotation(Lect_B.week07.TraceAop)")
public Object traceAnnotatedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
}
```

이 방식의 장점:

- 패키지나 메서드 이름 패턴에 덜 의존한다
- 적용 대상을 코드에서 명시적으로 표시할 수 있다
- 특정 메서드만 추적할 때 읽기 쉽다

## 7. Aspect 클래스와 JoinPoint

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

## 8. Around Advice와 proceed

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

## 9. Spring AOP의 프록시 기반 동작

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

프록시는 Pointcut 조건에 맞는 메서드인지 확인하고,
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

## 10. XML 기반 AOP 설정

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
- Pointcut과 Advice를 한 클래스에서 함께 읽을 수 있다

## 11. 현재 프로젝트에서 구현한 실습 구조

7주차 실습은 원본 코드의 흐름을 유지하되,
현재 웹 프로젝트 구조에 맞게 다시 구성했다.

### 11-1. Before 실습

URL:

```text
/week07/before
```

확인할 것:

- 대상 메서드 실행 전에 `@Before`가 먼저 실행된다
- `JoinPoint`로 메서드 이름과 인자를 읽는다
- `role=USER`이면 예외가 발생하고 대상 메서드는 실행되지 않는다

### 11-2. After 실습

URL:

```text
/week07/after
```

확인할 것:

- 성공 시 `@AfterReturning`과 `@After`가 실행된다
- 실패 시 `@AfterThrowing`과 `@After`가 실행된다
- `@After`는 정상/예외와 관계없이 실행된다

### 11-3. Around 실습

URL:

```text
/week07/around
```

확인할 것:

- `@Around`가 대상 메서드 실행 전후를 감싼다
- `role=admin`이 `ADMIN`으로 바뀐다
- 반환값 앞에 `[Around 적용 결과]`가 붙는다

### 11-4. Annotation Pointcut 실습

URL:

```text
/week07/pointcut
```

확인할 것:

- `@TraceAop`가 붙은 메서드만 Advice 대상이 된다
- `@annotation` Pointcut으로 특정 어노테이션을 기준으로 대상을 고른다

## 12. 자주 헷갈리는 질문

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

## 13. 시험 대비 핵심 정리

- AOP는 핵심 관심사와 공통 관심사를 분리하는 방식이다.
- Advice는 실행할 공통 기능이다.
- JoinPoint는 Advice를 적용할 수 있는 위치다.
- Spring AOP에서는 주로 스프링 빈의 메서드 실행이 JoinPoint다.
- Pointcut은 Advice 적용 대상을 고르는 규칙이다.
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
