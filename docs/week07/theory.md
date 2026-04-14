# Week 07 Theory

## 주제

7주차는 `AOP(Aspect Oriented Programming)`를 다룬다.

AOP는 문제를 하나의 클래스나 메서드 기준으로만 보지 않고,
여러 코드에 공통으로 흩어져 있는 관심사를 기준으로 분리해서 관리하는 프로그래밍 방식이다.

## 이 문서를 읽기 전에

AOP는 처음 보면 용어가 많아서 어렵게 느껴진다.
하지만 핵심 질문은 단순하다.

> "서비스 메서드마다 반복되는 공통 코드를 어디에 둘 것인가?"

예를 들어 여러 서비스 메서드에 다음 코드가 반복된다고 생각해 보자.

```java
System.out.println("메서드 실행 시작");
long start = System.currentTimeMillis();

// 실제 업무 로직

long end = System.currentTimeMillis();
System.out.println("실행 시간: " + (end - start));
```

이런 코드를 모든 메서드에 직접 넣으면:

- 핵심 로직이 지저분해지고
- 같은 코드가 계속 반복되고
- 로깅 방식이 바뀔 때 여러 파일을 고쳐야 하며
- 인증, 예외 처리, 트랜잭션 같은 공통 정책을 일관되게 적용하기 어렵다

AOP는 이런 공통 기능을 별도 클래스로 빼고,
스프링이 메서드 실행 전후에 자동으로 끼워 넣게 만든다.

## 현재 프로젝트에서 먼저 볼 코드

7주차 실습은 다음 파일 구조로 추가했다.

- `src/main/java/Lect_B/week07/Week07IndexController.java`
- `src/main/java/Lect_B/week07/Week07AopController.java`
- `src/main/java/Lect_B/week07/Week07AopService.java`
- `src/main/java/Lect_B/week07/Week07AdviceAspect.java`
- `src/main/java/Lect_B/week07/AopEventLog.java`
- `src/main/java/Lect_B/week07/TraceAop.java`
- `src/main/webapp/views/week07/index.jsp`
- `src/main/webapp/views/week07/beforeView.jsp`
- `src/main/webapp/views/week07/afterView.jsp`
- `src/main/webapp/views/week07/aroundView.jsp`
- `src/main/webapp/views/week07/pointcutView.jsp`

## 목차

- [1. AOP란 무엇인가](#1-aop란-무엇인가)
- [2. 핵심 관심사와 공통 관심사](#2-핵심-관심사와-공통-관심사)
- [3. AOP 주요 구성 요소](#3-aop-주요-구성-요소)
- [4. Advice 종류](#4-advice-종류)
- [5. Pointcut 표현식](#5-pointcut-표현식)
- [6. Aspect 클래스 작성 방식](#6-aspect-클래스-작성-방식)
- [7. JoinPoint 사용](#7-joinpoint-사용)
- [8. Around Advice와 proceed](#8-around-advice와-proceed)
- [9. Spring AOP의 동작 방식](#9-spring-aop의-동작-방식)
- [10. XML 기반 AOP 설정](#10-xml-기반-aop-설정)
- [11. 현재 프로젝트에 적용할 설계](#11-현재-프로젝트에-적용할-설계)
- [12. 자주 헷갈리는 질문](#12-자주-헷갈리는-질문)
- [13. 시험 대비 핵심 정리](#13-시험-대비-핵심-정리)

## 1. AOP란 무엇인가

AOP는 Aspect Oriented Programming의 약자다.
한국어로는 관점 지향 프로그래밍이라고 부른다.

여기서 관점은 "이 코드를 어떤 관심사로 바라볼 것인가"를 뜻한다.

일반적인 객체지향 프로그래밍에서는 보통 다음처럼 클래스를 나눈다.

- 주문 서비스
- 회원 서비스
- 게시글 서비스
- 결제 서비스

이런 분리는 업무 기능 중심이다.

하지만 실제 코드를 작성하다 보면 업무 기능과 별개로 여러 곳에 반복되는 기능이 생긴다.

- 메서드 실행 로그
- 사용자 인증 확인
- 권한 확인
- 예외 로그
- 실행 시간 측정
- 트랜잭션 시작과 종료

이런 기능은 특정 업무 하나에만 속하지 않는다.
여러 업무 기능을 가로질러 반복된다.

AOP는 이런 공통 관심사를 따로 분리해서,
필요한 메서드 실행 지점에 자동으로 적용하게 한다.

## 2. 핵심 관심사와 공통 관심사

AOP를 이해하려면 먼저 관심사를 둘로 나눠야 한다.

### 핵심 관심사

핵심 관심사는 해당 클래스가 원래 해야 하는 업무다.

예:

- 주문 생성
- 회원 가입
- 게시글 작성
- 상품 조회
- 결제 승인

서비스 메서드가 존재하는 직접적인 이유가 핵심 관심사다.

### 공통 관심사

공통 관심사는 여러 핵심 로직에 반복해서 붙는 부가 기능이다.

예:

- 로그 출력
- 인증 검사
- 권한 검사
- 실행 시간 측정
- 예외 기록
- 트랜잭션 처리

공통 관심사는 중요하지만, 핵심 업무 그 자체는 아니다.

### AOP 없이 작성했을 때의 문제

```java
public String placeOrder(String userId, int orderId) {
    System.out.println("[LOG] placeOrder 시작");
    checkLogin(userId);

    String result = "주문 처리: " + orderId;

    System.out.println("[LOG] placeOrder 종료");
    return result;
}
```

이 코드는 동작은 하지만 관심사가 섞여 있다.

- 주문 처리
- 로그
- 인증

이 한 메서드 안에 모두 들어간다.

이런 코드가 많아지면 핵심 로직을 읽기 어렵고,
로그나 인증 정책이 바뀔 때 모든 서비스 메서드를 다시 고쳐야 한다.

### AOP를 적용했을 때의 방향

```java
public String placeOrder(String userId, int orderId) {
    return "주문 처리: " + orderId;
}
```

서비스 메서드는 핵심 로직만 가진다.

로그, 인증, 실행 시간 측정은 Aspect 클래스에서 처리한다.

즉 AOP의 목적은 다음과 같다.

- 핵심 로직을 단순하게 유지
- 공통 기능을 한 곳에 모음
- 공통 정책 변경 시 수정 범위 축소
- 여러 메서드에 일관된 규칙 적용

## 3. AOP 주요 구성 요소

PPT에서 정리한 핵심 용어는 네 가지다.

### 3-1. Advice

Advice는 실제로 실행될 공통 기능 메서드다.

예:

- 메서드 실행 전 인증
- 메서드 실행 후 로그
- 예외 발생 시 예외 메시지 기록
- 실행 시간 측정

코드에서는 보통 `@Before`, `@AfterReturning`, `@AfterThrowing`, `@After`, `@Around` 같은 어노테이션이 붙은 메서드가 Advice다.

### 3-2. JoinPoint

JoinPoint는 Advice를 적용할 수 있는 지점이다.

일반적인 AOP에서는:

- 메서드 호출
- 메서드 실행
- 생성자 호출
- 필드 값 변경
- 예외 발생

같은 지점이 JoinPoint가 될 수 있다.

하지만 Spring AOP에서는 주로 **스프링 빈의 메서드 실행 시점**만 JoinPoint로 사용한다.

이 점이 중요하다.

Spring AOP는 모든 자바 코드 줄 사이에 끼어드는 도구가 아니다.
스프링이 관리하는 빈의 메서드 실행을 프록시로 감싸는 방식이다.

### 3-3. Pointcut

Pointcut은 Advice를 어디에 적용할지 고르는 규칙이다.

예:

```java
@Pointcut("execution(* Lect_B.week07..*Service.*(..))")
private void week07ServiceMethods() {
}
```

이 Pointcut은 다음 뜻이다.

- `Lect_B.week07` 패키지와 하위 패키지에서
- 이름이 `Service`로 끝나는 클래스의
- 모든 메서드 실행을
- Advice 적용 대상으로 삼는다

즉 JoinPoint가 "적용 가능한 위치"라면,
Pointcut은 그중에서 "실제로 적용할 위치를 고르는 조건"이다.

### 3-4. Aspect

Aspect는 Advice와 Pointcut을 하나로 묶은 클래스다.

예:

```java
@Aspect
@Component
public class Week07AdviceAspect {

    @Pointcut("execution(* Lect_B.week07..*Service.*(..))")
    private void week07ServiceMethods() {
    }

    @Before("week07ServiceMethods()")
    public void beforeLog() {
        System.out.println("서비스 메서드 실행 전");
    }
}
```

이 클래스는 다음을 함께 가진다.

- 어디에 적용할지: `week07ServiceMethods()`
- 무엇을 실행할지: `beforeLog()`

그래서 Aspect는 AOP의 실제 모듈 단위라고 볼 수 있다.

## 4. Advice 종류

Advice는 대상 메서드 실행의 어느 시점에 동작하느냐에 따라 나뉜다.

| 종류 | 어노테이션 | XML 태그 | 실행 시점 | 주 사용 목적 |
|---|---|---|---|---|
| Before Advice | `@Before` | `<aop:before>` | 대상 메서드 실행 전 | 인증, 권한 확인, 파라미터 검증 |
| After Returning Advice | `@AfterReturning` | `<aop:after-returning>` | 대상 메서드가 예외 없이 종료된 후 | 반환값 로그, 정상 결과 기록 |
| After Throwing Advice | `@AfterThrowing` | `<aop:after-throwing>` | 대상 메서드에서 예외 발생 후 | 예외 로그, 예외 알림 |
| After Advice | `@After` | `<aop:after>` | 정상/예외와 관계없이 실행 후 | finally 성격의 정리 작업 |
| Around Advice | `@Around` | `<aop:around>` | 대상 메서드 실행 전후 전체 | 실행 시간 측정, 인자 변경, 반환값 가공 |

### 4-1. Before Advice

대상 메서드가 실행되기 전에 동작한다.

```java
@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
public void authenticate(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
}
```

사용하기 좋은 경우:

- 로그인 여부 확인
- 권한 확인
- 입력 파라미터 검증
- 실행 전 로그

주의할 점:

`@Before`는 대상 메서드의 반환값을 알 수 없다.
아직 대상 메서드가 실행되기 전이기 때문이다.

### 4-2. After Returning Advice

대상 메서드가 예외 없이 정상 종료된 뒤 실행된다.

```java
@AfterReturning(pointcut = "week07ServiceMethods()", returning = "result")
public void logAfterReturning(Object result) {
    System.out.println("반환값: " + result);
}
```

사용하기 좋은 경우:

- 정상 반환값 기록
- 성공 로그
- 결과 데이터 확인

주의할 점:

대상 메서드에서 예외가 발생하면 `@AfterReturning`은 실행되지 않는다.

### 4-3. After Throwing Advice

대상 메서드 실행 중 예외가 발생했을 때 실행된다.

```java
@AfterThrowing(pointcut = "week07ServiceMethods()", throwing = "ex")
public void logException(Throwable ex) {
    System.out.println("예외 발생: " + ex.getMessage());
}
```

사용하기 좋은 경우:

- 예외 메시지 기록
- 예외 발생 지점 추적
- 장애 알림

주의할 점:

`@AfterThrowing`은 예외를 "관찰"하는 데 자주 쓰인다.
예외를 삼켜서 정상 결과로 바꾸는 용도는 보통 `@Around`에서 더 명확하게 처리한다.

### 4-4. After Advice

대상 메서드가 끝난 뒤 항상 실행된다.

정상 종료든 예외 발생이든 실행된다는 점에서 자바의 `finally`와 비슷하다.

```java
@After("week07ServiceMethods()")
public void afterFinally(JoinPoint joinPoint) {
    System.out.println("메서드 실행 후 항상 수행");
}
```

사용하기 좋은 경우:

- 후처리 로그
- 임시 상태 정리
- 공통 마무리 작업

주의할 점:

`@After`는 반환값이나 예외 객체를 직접 받는 용도에는 맞지 않다.
반환값이 필요하면 `@AfterReturning`, 예외가 필요하면 `@AfterThrowing`을 사용한다.

### 4-5. Around Advice

대상 메서드 실행 전과 실행 후를 모두 감싸는 Advice다.

```java
@Around("week07ServiceMethods()")
public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    Object result = joinPoint.proceed();

    long end = System.currentTimeMillis();
    System.out.println("실행 시간: " + (end - start) + "ms");

    return result;
}
```

`@Around`는 가장 강력하다.

할 수 있는 일:

- 대상 메서드 실행 전 처리
- 대상 메서드 실행 여부 결정
- 대상 메서드 인자 변경
- 대상 메서드 반환값 변경
- 예외 처리
- 실행 시간 측정

그만큼 주의도 필요하다.

`joinPoint.proceed()`를 호출하지 않으면 대상 메서드는 실행되지 않는다.

## 5. Pointcut 표현식

Pointcut은 Advice 적용 대상을 고르는 규칙이다.

PPT에서는 다음 표현식을 중심으로 다룬다.

- `execution`
- `within`
- `args`
- `@annotation`

## 5-1. execution

`execution`은 메서드 실행을 기준으로 대상을 고른다.

기본 형태:

```text
execution([수식어패턴] 리턴타입패턴 클래스이름패턴.메서드이름패턴(파라미터패턴))
```

예:

```java
@Pointcut("execution(public void set*(..))")
```

의미:

- `public`
- 반환 타입이 `void`
- 메서드 이름이 `set`으로 시작
- 파라미터는 0개 이상

### 자주 쓰는 예

```java
@Pointcut("execution(* Lect_B.week07..*.*(..))")
```

의미:

- `Lect_B.week07` 패키지와 하위 패키지
- 모든 클래스
- 모든 메서드
- 모든 파라미터

```java
@Pointcut("execution(* Lect_B.week07..*Service.*(..))")
```

의미:

- `Lect_B.week07` 패키지와 하위 패키지
- 클래스 이름이 `Service`로 끝나는 대상
- 모든 메서드

```java
@Pointcut("execution(String Lect_B.week07.Week07AopService.check(..))")
```

의미:

- 반환 타입이 `String`
- `Week07AopService`의 `check` 메서드
- 파라미터는 0개 이상

### `*`와 `..`

`*`는 한 구간의 모든 값을 의미한다.

예:

- 모든 반환 타입
- 모든 클래스 이름
- 모든 메서드 이름

`..`는 0개 이상을 의미한다.

예:

- `Lect_B.week07..*`
  - `week07` 패키지와 모든 하위 패키지
- `(..)`
  - 파라미터 0개 이상

## 5-2. within

`within`은 특정 클래스나 패키지 안에 있는 메서드를 고른다.

```java
@Pointcut("within(Lect_B.week07.Week07AopService)")
```

의미:

- `Week07AopService` 클래스 안의 모든 메서드

```java
@Pointcut("within(Lect_B.week07..*)")
```

의미:

- `Lect_B.week07` 패키지와 하위 패키지의 모든 클래스

```java
@Pointcut("within(@org.springframework.stereotype.Service *)")
```

의미:

- `@Service`가 붙은 클래스 안의 모든 메서드

`execution`이 메서드 모양을 자세히 고르는 데 강하다면,
`within`은 클래스나 패키지 범위로 묶을 때 읽기 쉽다.

## 5-3. args

`args`는 메서드에 전달되는 인자 타입을 기준으로 대상을 고른다.

```java
@Pointcut("args(java.lang.String)")
```

의미:

- 인자가 하나이고
- 그 인자가 `String` 타입인 메서드

```java
@Pointcut("args(java.lang.String, ..)")
```

의미:

- 첫 번째 인자가 `String`
- 뒤에는 0개 이상의 인자가 올 수 있음

```java
@Pointcut("args(java.lang.String, int)")
```

의미:

- 첫 번째 인자가 `String`
- 두 번째 인자가 `int`

주의:

`args`는 인자 타입 조건을 기준으로 하므로,
메서드 이름이나 패키지 조건과 함께 쓰는 편이 안전하다.

예:

```java
@Pointcut("execution(* Lect_B.week07..*(..)) && args(java.lang.String, ..)")
```

## 5-4. @annotation

`@annotation`은 특정 어노테이션이 붙은 메서드를 대상으로 삼는다.

```java
@Pointcut("@annotation(Lect_B.week07.TraceAop)")
```

의미:

- `@TraceAop` 어노테이션이 붙은 메서드만 대상

이 방식은 실무에서도 자주 쓰기 좋다.

왜냐하면 Pointcut 표현식에 메서드 이름 패턴을 길게 쓰지 않아도,
개발자가 필요한 메서드에 명시적으로 어노테이션을 붙이면 되기 때문이다.

예:

```java
@TraceAop
public String check(String userId, String role) {
    return "권한 확인 결과";
}
```

이렇게 하면 Aspect는 `@TraceAop`가 붙은 메서드만 골라서 실행 시간 측정이나 로그를 적용할 수 있다.

## 6. Aspect 클래스 작성 방식

어노테이션 기반 AOP에서는 Aspect 클래스를 다음 순서로 작성한다.

### 6-1. 클래스에 `@Aspect`와 `@Component` 붙이기

```java
@Aspect
@Component
public class Week07AdviceAspect {
}
```

의미:

- `@Aspect`
  - 이 클래스는 AOP 설정을 담는 Aspect다
- `@Component`
  - 스프링 빈으로 등록한다

Aspect 클래스도 스프링 빈이어야 스프링이 찾아서 적용할 수 있다.

### 6-2. Pointcut 메서드 작성

```java
@Pointcut("execution(* Lect_B.week07..*Service.*(..))")
private void week07ServiceMethods() {
}
```

Pointcut 메서드는 보통 본문이 비어 있다.
메서드 자체를 실행하려는 것이 아니라,
Pointcut 이름으로 재사용하기 위한 선언이기 때문이다.

주의:

- 반환 타입은 `void`
- 메서드 이름은 Advice 어노테이션에서 사용
- 접근 제어자는 `private`이어도 된다

### 6-3. Advice 메서드 작성

```java
@Before("week07ServiceMethods()")
public void beforeLog(JoinPoint joinPoint) {
    System.out.println(joinPoint.getSignature().getName());
}
```

이제 `week07ServiceMethods()`에 해당하는 대상 메서드가 실행되기 전에
`beforeLog()`가 먼저 실행된다.

## 7. JoinPoint 사용

Advice 메서드는 `JoinPoint`를 받을 수 있다.

단, 받을 때는 첫 번째 파라미터로 두는 것이 원칙이다.

```java
@Before("week07ServiceMethods()")
public void beforeLog(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    Object target = joinPoint.getTarget();
}
```

주요 메서드:

| 메서드 | 의미 |
|---|---|
| `getSignature()` | 호출되는 메서드 정보 |
| `getSignature().getName()` | 메서드 이름 |
| `getSignature().toLongString()` | 긴 형식의 메서드 정보 |
| `getSignature().toShortString()` | 짧은 형식의 메서드 정보 |
| `getTarget()` | 실제 대상 객체 |
| `getArgs()` | 메서드에 전달된 인자 배열 |

예를 들어 사용자가 `/week07/before`를 호출해서
서비스 메서드 `performSensitiveOperation("타겟 메서드 호출", 3)`이 실행된다면,
`JoinPoint`를 통해 다음 정보를 읽을 수 있다.

- 메서드 이름: `performSensitiveOperation`
- 인자: `"타겟 메서드 호출"`, `3`
- 대상 객체: `Week07AopService`

## 8. Around Advice와 proceed

`@Around`에서는 `JoinPoint` 대신 `ProceedingJoinPoint`를 사용한다.

```java
@Around("execution(* Lect_B.week07.Week07AopService.check(..))")
public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();

    args[0] = "ADMIN";

    Object result = joinPoint.proceed(args);

    return "[Around 적용] " + result;
}
```

핵심은 `proceed()`다.

### `proceed()`를 호출하는 경우

대상 메서드가 실제로 실행된다.

```java
Object result = joinPoint.proceed();
```

### `proceed(args)`를 호출하는 경우

변경한 인자 배열로 대상 메서드를 실행한다.

```java
args[0] = "ADMIN";
Object result = joinPoint.proceed(args);
```

### `proceed()`를 호출하지 않는 경우

대상 메서드가 실행되지 않는다.

이 특징은 강력하지만 위험하다.
인증 실패 시 메서드를 막는 용도로 쓸 수 있지만,
실수로 `proceed()`를 빼먹으면 핵심 로직이 아예 동작하지 않는다.

## 9. Spring AOP의 동작 방식

PPT에서는 AOP가 컴파일, 클래스 로딩, 객체 생성 시점 등에 적용될 수 있다고 설명한다.

다만 현재 프로젝트에서 사용할 Spring AOP는 일반적으로 프록시 기반으로 동작한다.

즉 스프링이 대상 빈을 그대로 주입하는 대신,
그 앞에 프록시 객체를 세운다.

현재 프로젝트에서 AOP 실습을 실행하려면 `build.gradle`에 AOP 의존성이 필요하다.

```gradle
implementation 'org.springframework.boot:spring-boot-starter-aspectj'
```

PPT와 제공된 7주차 실습 소스도 이 의존성을 사용한다.
현재 프로젝트가 사용하는 Spring Boot 4 계열에서는 이 스타터로 Spring AOP와 AspectJ 관련 기본 구성을 가져온다.

흐름은 다음처럼 이해하면 된다.

```text
Controller
  -> AOP Proxy
      -> Advice 실행
      -> 실제 Service 메서드 실행
      -> Advice 실행
  -> Controller
```

컨트롤러는 서비스 객체를 호출한다고 생각하지만,
실제로는 프록시가 먼저 호출을 받는다.
프록시는 Pointcut 조건에 맞는 메서드인지 확인하고,
맞으면 Advice를 실행한 뒤 실제 서비스 메서드를 호출한다.

### Spring AOP에서 중요한 제한

Spring AOP는 스프링이 관리하는 빈의 메서드 실행을 중심으로 동작한다.

그래서 다음을 주의해야 한다.

- 스프링 빈이 아닌 객체에는 적용되지 않는다
- 필드 값 변경 같은 JoinPoint는 Spring AOP 대상이 아니다
- 같은 클래스 내부에서 자기 자신의 메서드를 직접 호출하는 경우에는 프록시를 거치지 않을 수 있다
- `private` 메서드에는 일반적인 방식으로 적용하기 어렵다

이 제한을 이해해야 AOP가 "왜 안 걸리지?"라는 상황을 줄일 수 있다.

## 10. XML 기반 AOP 설정

PPT에는 XML 스키마 기반 AOP 설정도 포함되어 있다.

XML 방식에서는 다음 요소를 사용한다.

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
<bean id="advice" class="Lect_B.week07.Week07AdviceAspect" />

<aop:config>
    <aop:aspect id="aspect1" ref="advice">
        <aop:pointcut
            id="pointcut1"
            expression="execution(public * Lect_B.week07.Week07AopService.businessLogic())" />
        <aop:before pointcut-ref="pointcut1" method="beforeLog" />
    </aop:aspect>
</aop:config>
```

현재 프로젝트에서는 어노테이션 기반 구성이 더 자연스럽다.

이유:

- 기존 주차 코드가 `@Controller`, `@Service`, `@Component` 중심이다
- Spring Boot 프로젝트와 잘 맞는다
- Pointcut과 Advice를 한 클래스에서 읽을 수 있다

따라서 7주차 실습 구현은 어노테이션 기반으로 진행하고,
XML 방식은 비교 개념으로 이해하면 충분하다.

## 11. 현재 프로젝트에 적용한 설계

7주차 실습은 원본 실습의 세 흐름을 유지하되,
웹 프로젝트에 맞게 정리한다.

먼저 애플리케이션 시작 클래스의 컴포넌트 스캔 범위에 `Lect_B.week07`을 추가해야 한다.

```java
@SpringBootApplication(scanBasePackages = {
    "Lect_B.week04",
    "Lect_B.week05",
    "Lect_B.week06",
    "Lect_B.week07"
})
```

이 설정이 빠지면 `Week07AopService`, `Week07AdviceAspect`, `Week07AopController`가 스프링 빈으로 등록되지 않는다.

### 11-1. Before 실습

목표:

- 대상 메서드 실행 전에 Advice가 먼저 실행되는지 확인
- `JoinPoint`로 메서드 이름과 인자를 읽기
- 인증 실패 시 대상 메서드를 막을 수 있다는 점 이해

예상 URL:

```text
/week07/before
```

대상 서비스 메서드:

```java
performSensitiveOperation(String userId, String role, String message, int count)
```

적용 Advice:

```java
@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
```

### 11-2. After 실습

목표:

- 정상 종료 시 `@AfterReturning`
- 예외 발생 시 `@AfterThrowing`
- 두 경우 모두 `@After`

가 어떻게 실행되는지 비교한다.

예상 URL:

```text
/week07/after
```

대상 서비스 메서드:

```java
placeOrder(double orderValue, double minimumValue)
```

원본 실습은 `Math.random()`으로 성공과 실패가 갈렸지만,
현재 프로젝트에서는 요청 파라미터로 성공과 실패를 조절하는 편이 좋다.

학습용 예제는 결과가 예측 가능해야 하기 때문이다.

### 11-3. Around 실습

목표:

- 대상 메서드 실행 전후를 감싸는 구조 확인
- `ProceedingJoinPoint` 사용
- 인자 변경 또는 반환값 가공 확인
- 실행 시간 측정 확인

예상 URL:

```text
/week07/around
```

대상 서비스 메서드:

```java
check(String userId, String role)
```

원본 실습은 `Scanner`로 콘솔 입력을 받았지만,
웹 프로젝트에서는 요청 파라미터를 사용해야 한다.

웹 요청 처리 중 콘솔 입력을 기다리면 브라우저 응답이 멈출 수 있기 때문이다.

### 11-4. Annotation Pointcut 실습

목표:

- `@annotation` 표현식 이해
- 특정 어노테이션이 붙은 메서드만 추적

추가할 어노테이션:

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TraceAop {
}
```

적용 Pointcut:

```java
@Pointcut("@annotation(Lect_B.week07.TraceAop)")
```

이 실습은 `execution`처럼 패키지나 메서드 이름 패턴으로 고르는 방식과,
어노테이션으로 명시적으로 고르는 방식을 비교하기 좋다.

## 12. 자주 헷갈리는 질문

### Q1. AOP는 상속과 같은 것인가?

아니다.

상속은 부모 클래스의 기능을 자식 클래스가 물려받는 방식이다.
AOP는 대상 메서드 실행 전후에 공통 기능을 끼워 넣는 방식이다.

### Q2. AOP는 DI와 같은 것인가?

아니다.

DI는 필요한 객체를 외부에서 주입받는 방식이다.
AOP는 메서드 실행 흐름에 공통 기능을 적용하는 방식이다.

다만 둘 다 스프링 컨테이너가 빈을 관리하기 때문에 함께 사용된다.

### Q3. `@After`와 `@AfterReturning`은 무엇이 다른가?

`@AfterReturning`은 대상 메서드가 정상 종료됐을 때만 실행된다.

`@After`는 정상 종료든 예외 발생이든 항상 실행된다.

### Q4. `@AfterThrowing`은 예외를 처리해서 없애 주는가?

기본적으로는 아니다.

`@AfterThrowing`은 예외가 발생했을 때 실행되는 Advice다.
예외 로그를 남길 수는 있지만,
예외를 정상 결과로 바꾸려면 보통 `@Around`에서 `try-catch`로 처리해야 한다.

### Q5. `@Around`가 있으면 다른 Advice는 필요 없는가?

그렇지 않다.

`@Around`는 강력하지만 코드가 복잡해지기 쉽다.

단순히 실행 전 인증이면 `@Before`,
정상 반환값 기록이면 `@AfterReturning`,
예외 기록이면 `@AfterThrowing`을 쓰는 편이 의도가 분명하다.

### Q6. Pointcut은 넓게 잡아도 되는가?

너무 넓게 잡으면 원하지 않는 메서드에도 Advice가 적용된다.

예:

```java
@Pointcut("execution(* Lect_B..*(..))")
```

이렇게 잡으면 프로젝트의 너무 많은 메서드가 대상이 될 수 있다.

학습용으로는 편할 수 있지만,
실제 코드에서는 패키지, 클래스 이름, 어노테이션 등을 조합해서 대상을 좁히는 편이 안전하다.

### Q7. 같은 클래스 안에서 메서드를 호출해도 AOP가 적용되는가?

항상 그렇지는 않다.

Spring AOP는 프록시를 거쳐야 Advice가 적용된다.
같은 클래스 내부에서 `this.someMethod()`처럼 직접 호출하면 프록시를 거치지 않을 수 있다.

이 문제를 자기 호출 문제라고 부른다.

## 13. 시험 대비 핵심 정리

- AOP는 핵심 관심사와 공통 관심사를 분리하는 프로그래밍 방식이다.
- Advice는 실행할 공통 기능이다.
- JoinPoint는 Advice를 적용할 수 있는 위치다.
- Spring AOP에서는 주로 스프링 빈의 메서드 실행이 JoinPoint다.
- Pointcut은 Advice를 적용할 대상을 고르는 규칙이다.
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
- 현재 프로젝트의 7주차 실습은 어노테이션 기반 AOP로 구현하는 것이 적절하다.
