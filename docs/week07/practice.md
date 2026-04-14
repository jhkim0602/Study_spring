# Week 07 Practice

## 주제

7주차 실습은 AOP의 Advice 실행 시점을 웹 화면에서 확인하도록 구성했다.

원본 실습의 핵심 흐름은 유지한다.

- `@Before`
- `@AfterReturning`
- `@AfterThrowing`
- `@After`
- `@Around`
- `@annotation` Pointcut

다만 현재 프로젝트는 웹 요청 기반이므로,
콘솔 입력이나 랜덤 결과 대신 요청 파라미터로 성공과 실패를 조절하게 바꿨다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/Lect_B/week07/Week07AopService.java` | AOP 적용 대상 서비스 |
| `src/main/java/Lect_B/week07/Week07AdviceAspect.java` | Advice와 Pointcut 정의 |
| `src/main/java/Lect_B/week07/AopEventLog.java` | 화면에 보여 줄 Advice 실행 기록 저장 |
| `src/main/java/Lect_B/week07/TraceAop.java` | `@annotation` Pointcut 확인용 어노테이션 |
| `src/main/java/Lect_B/week07/Week07AopController.java` | 7주차 실습 요청 처리 |
| `src/main/java/Lect_B/week07/Week07IndexController.java` | 7주차 인덱스 요청 처리 |
| `src/main/webapp/views/week07/*.jsp` | 실습 결과 화면 |
| `src/test/java/Lect_B/week07/Week07ContextTests.java` | AOP 적용 테스트 |

## 1. `/week07` 인덱스

`/week07`은 7주차 실습의 시작 화면이다.

확인할 링크:

- Before Advice 성공
- Before Advice 인증 실패
- After Advice 성공
- After Advice 예외
- Around Advice
- Annotation Pointcut

각 링크는 같은 서비스 메서드를 직접 설명하는 대신,
Advice 실행 기록을 화면에 보여 준다.

## 2. Before Advice 실습

URL:

```text
/week07/before
```

대상 메서드:

```java
performSensitiveOperation(String userId, String role, String message, int count)
```

적용 Advice:

```java
@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
```

읽는 법:

- `role=ADMIN`이면 인증 성공 후 대상 메서드가 실행된다.
- `role=USER`이면 `@Before`에서 `SecurityException`이 발생하고 대상 메서드는 실행되지 않는다.
- `JoinPoint`로 메서드 이름과 인자 목록을 기록한다.

실패 예:

```text
/week07/before?role=USER
```

## 3. After Advice 실습

URL:

```text
/week07/after
```

대상 메서드:

```java
placeOrder(double orderValue, double minimumValue)
```

읽는 법:

- `orderValue >= minimumValue`이면 정상 종료다.
- 정상 종료 시 `@AfterReturning`과 `@After`가 실행된다.
- `orderValue < minimumValue`이면 예외가 발생한다.
- 예외 발생 시 `@AfterThrowing`과 `@After`가 실행된다.

성공 예:

```text
/week07/after?orderValue=100&minimumValue=50
```

실패 예:

```text
/week07/after?orderValue=20&minimumValue=50
```

핵심은 `@After`다.

`@After`는 성공과 실패 어느 쪽에서도 실행된다.
그래서 화면에서 성공 예제와 실패 예제를 모두 눌러 비교해야 한다.

## 4. Around Advice 실습

URL:

```text
/week07/around
```

대상 메서드:

```java
check(String userId, String role)
```

적용 Advice:

```java
@Around("execution(* Lect_B.week07.Week07AopService.check(..))")
```

읽는 법:

- 컨트롤러는 기본값으로 `role=admin`을 전달한다.
- `@Around` Advice가 이 값을 `ADMIN`으로 바꾼다.
- 대상 메서드는 변경된 인자를 기준으로 권한을 판단한다.
- 반환값 앞에 `[Around 적용 결과]`가 붙는다.

여기서 중요한 점은 `ProceedingJoinPoint.proceed(args)`다.

이 메서드를 호출해야 대상 서비스 메서드가 실행된다.
그리고 `args`를 넘기면 변경된 인자로 실행할 수 있다.

## 5. Annotation Pointcut 실습

URL:

```text
/week07/pointcut
```

대상 메서드:

```java
@TraceAop
public String annotationTarget(String label)
```

적용 Pointcut:

```java
@Around("@annotation(Lect_B.week07.TraceAop)")
```

읽는 법:

- `@TraceAop`가 붙은 메서드만 Advice 적용 대상이 된다.
- 패키지나 메서드 이름 패턴보다 의도가 직접 드러난다.
- 필요한 메서드에 어노테이션을 붙여 추적 대상을 명시할 수 있다.

이 실습은 `execution` 기반 Pointcut과 비교해서 보면 좋다.

## 6. `AopEventLog`가 필요한 이유

원본 실습은 `System.out.println()`으로 콘솔에 Advice 실행 결과를 찍는다.

하지만 웹 실습에서는 학생이 브라우저 화면을 보고 흐름을 확인해야 한다.
그래서 `AopEventLog`가 Advice 실행 기록을 모아 JSP에 전달한다.

현재 구현은 `ThreadLocal`을 사용한다.

이유:

- 요청마다 로그를 분리하기 위해서
- 테스트에서도 같은 방식으로 Advice 기록을 확인하기 위해서
- singleton Aspect에서 요청별 기록을 안전하게 다루기 위해서

## 7. 테스트에서 확인하는 것

`Week07ContextTests`는 다음을 확인한다.

- 7주차 컨트롤러와 서비스가 스프링 빈으로 로딩되는가
- `Week07AopService`에 AOP 프록시가 적용되는가
- `@Before`가 ADMIN 권한을 허용하는가
- `@Before`가 USER 권한을 막는가
- 성공 주문에서 `@AfterReturning`, `@After`가 기록되는가
- 실패 주문에서 `@AfterThrowing`, `@After`가 기록되는가
- `@Around`가 인자를 변경하고 반환값을 가공하는가
- `@annotation` Pointcut이 `@TraceAop` 메서드를 추적하는가

## 8. 초심자용 실행 순서

1. `/week07` 접속
2. Before 성공 예제 실행
3. Before 실패 예제 실행
4. After 성공 예제 실행
5. After 실패 예제 실행
6. Around 예제에서 `role=admin`이 `ADMIN`으로 바뀌는지 확인
7. Annotation Pointcut 예제 실행

이 순서가 좋은 이유는 Advice의 실행 시점이 점점 강해지기 때문이다.

- `@Before`: 실행 전
- `@AfterReturning`, `@AfterThrowing`, `@After`: 실행 후
- `@Around`: 실행 전후 전체 제어
- `@annotation`: 적용 대상을 고르는 다른 방식

## 9. 이 실습을 끝내면 말할 수 있어야 하는 것

- AOP가 왜 공통 관심사를 분리하는지 설명할 수 있는가
- `@Before`에서 예외를 던지면 대상 메서드가 실행되지 않는다는 점을 설명할 수 있는가
- `@AfterReturning`과 `@AfterThrowing`의 차이를 말할 수 있는가
- `@After`가 성공과 실패 모두에서 실행된다는 점을 설명할 수 있는가
- `@Around`에서 `proceed()`가 왜 중요한지 설명할 수 있는가
- `@annotation` Pointcut이 어떤 장점을 가지는지 설명할 수 있는가

