# Week 07 Practice

## 주제

7주차 실습은 제공된 `AdviceAspect`, `ExAOPService`, `ExAOPController` 흐름을 현재 프로젝트 구조에 맞게 재작성한 것이다.

원본 실습은 콘솔 출력과 일부 랜덤 결과에 의존한다.
현재 프로젝트에서는 브라우저 화면에서 Advice 실행 순서를 확인할 수 있도록 JSP 결과 화면으로 재구성했다.

## 이 실습의 목적

7주차 실습의 목표는 AOP 개념을 "눈으로 확인"하는 것이다.

- 대상 메서드 실행 전에 Advice가 먼저 실행되는가
- 정상 종료와 예외 발생에서 실행되는 Advice가 어떻게 다른가
- `@After`는 성공과 실패 모두에서 실행되는가
- `@Around`는 인자와 반환값을 바꿀 수 있는가
- Pointcut 표현식으로 적용 대상을 어떻게 고르는가

즉 이 실습은 7주차 이론을 실제 라우트와 JSP 화면으로 검증하는 역할을 한다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| [`src/main/java/Lect_B/week07/Week07AopService.java`](../../src/main/java/Lect_B/week07/Week07AopService.java) | AOP 적용 대상 서비스 |
| [`src/main/java/Lect_B/week07/Week07AdviceAspect.java`](../../src/main/java/Lect_B/week07/Week07AdviceAspect.java) | Advice와 Pointcut 정의 |
| [`src/main/java/Lect_B/week07/AopEventLog.java`](../../src/main/java/Lect_B/week07/AopEventLog.java) | 화면에 보여 줄 Advice 실행 기록 저장 |
| [`src/main/java/Lect_B/week07/TraceAop.java`](../../src/main/java/Lect_B/week07/TraceAop.java) | `@annotation` Pointcut 확인용 어노테이션 |
| [`src/main/java/Lect_B/week07/Week07AopController.java`](../../src/main/java/Lect_B/week07/Week07AopController.java) | 7주차 실습 라우트 제어 |
| [`src/main/java/Lect_B/week07/Week07IndexController.java`](../../src/main/java/Lect_B/week07/Week07IndexController.java) | `/week07` 진입 |
| [`src/main/webapp/views/week07/*.jsp`](../../src/main/webapp/views/week07) | 실습 결과 화면 |
| [`src/test/java/Lect_B/week07/Week07ContextTests.java`](../../src/test/java/Lect_B/week07/Week07ContextTests.java) | AOP 적용 테스트 |

## 1. 왜 `week07` 전용 패키지를 따로 만들었는가

7주차는 이전 주차와 성격이 다르다.

3~6주차가 주로 빈을 만들고 주입하고 관리하는 내용이었다면,
7주차는 이미 등록된 빈의 메서드 실행 흐름에 공통 기능을 적용한다.

그래서 다음처럼 별도 패키지로 분리했다.

```text
src/main/java/Lect_B/week07/
src/main/webapp/views/week07/
docs/week07/
```

이렇게 나누면:

- 기존 주차 코드와 섞이지 않고
- AOP 적용 대상과 Aspect를 한눈에 볼 수 있고
- `/week07` 화면에서 실습 흐름만 따로 확인할 수 있다

## 2. `Week07AopService`는 무엇을 보여 주는가

`Week07AopService`는 AOP가 적용되는 대상이다.

이 클래스에는 핵심 로직만 둔다.

- `performSensitiveOperation()`: Before Advice 확인
- `placeOrder()`: After Returning, After Throwing, After 확인
- `check()`: Around Advice 확인
- `annotationTarget()`: `@annotation` Pointcut 확인

중요한 점:

서비스 메서드 안에는 Advice 실행 코드가 없다.

즉 서비스는 핵심 로직만 가지고,
공통 기능은 `Week07AdviceAspect`가 처리한다.

## 3. `Week07AdviceAspect`는 왜 핵심 예제인가

7주차에서 가장 중요한 클래스는 `Week07AdviceAspect`다.

이 클래스는 다음을 모두 보여 준다.

- `@Aspect`
- `@Pointcut`
- `@Before`
- `@AfterReturning`
- `@AfterThrowing`
- `@After`
- `@Around`
- `JoinPoint`
- `ProceedingJoinPoint`

즉 7주차 PPT의 핵심 용어가 이 클래스 하나에 모여 있다.

코드를 읽을 때는 "이 메서드가 언제 실행되는가"를 기준으로 보면 된다.

## 4. `/week07/before` 화면은 무엇을 증명하나

`/week07/before`는 Before Advice를 확인하는 화면이다.

기본 요청:

```text
/week07/before
```

실패 요청:

```text
/week07/before?role=USER
```

읽는 법:

- `role=ADMIN`이면 인증 성공 기록이 남고 대상 메서드가 실행된다
- `role=USER`이면 `@Before`에서 예외가 발생한다
- 예외가 발생하면 대상 메서드는 실행되지 않는다

즉 이 화면은 Advice가 핵심 로직 실행 전에 개입할 수 있다는 것을 보여 준다.

## 5. `/week07/after` 화면은 무엇을 비교하나

`/week07/after`는 메서드 실행 후 Advice를 비교하는 화면이다.

성공 요청:

```text
/week07/after?orderValue=100&minimumValue=50
```

실패 요청:

```text
/week07/after?orderValue=20&minimumValue=50
```

성공할 때:

- `@AfterReturning` 실행
- `@After` 실행

실패할 때:

- `@AfterThrowing` 실행
- `@After` 실행

여기서 핵심은 `@After`다.

`@After`는 정상 종료와 예외 발생 모두에서 실행된다.
그래서 성공 예제와 실패 예제를 반드시 같이 비교해야 한다.

## 6. `/week07/around` 화면은 무엇을 보여 주나

`/week07/around`는 Around Advice를 확인하는 화면이다.

기본 요청:

```text
/week07/around
```

기본 요청은 `role=admin`을 전달한다.
서비스 메서드는 `ADMIN`만 권한이 있다고 판단한다.

그런데 화면 결과에서는 접근 권한이 있다고 나온다.

이유:

- `@Around` Advice가 실행 전 인자를 확인한다
- `role` 값을 `admin`에서 `ADMIN`으로 바꾼다
- `proceed(args)`로 변경된 인자를 대상 메서드에 넘긴다
- 반환값 앞에 `[Around 적용 결과]`를 붙인다

이 실습은 `@Around`가 가장 강력한 Advice라는 점을 보여 준다.

## 7. `/week07/pointcut` 화면은 왜 필요한가

`/week07/pointcut`은 `@annotation` Pointcut을 확인하는 화면이다.

대상 메서드:

```java
@TraceAop
public String annotationTarget(String label) {
    return "@TraceAop 대상 메서드 실행: " + label;
}
```

Aspect의 Pointcut:

```java
@Around("@annotation(Lect_B.week07.TraceAop)")
```

이 예제가 필요한 이유:

- `execution`은 패키지, 클래스, 메서드 이름 패턴으로 대상을 고른다
- `@annotation`은 특정 어노테이션이 붙은 메서드를 대상으로 고른다
- 어노테이션 기반 Pointcut은 적용 대상이 코드에 직접 드러난다

즉 이 화면은 Pointcut을 메서드 이름 패턴이 아니라 어노테이션으로도 지정할 수 있음을 보여 준다.

## 8. `AopEventLog`를 왜 만들었는가

원본 실습은 `System.out.println()`으로 콘솔에 결과를 출력한다.

하지만 현재 프로젝트는 JSP 기반 웹 실습이다.
학생이 브라우저 화면에서 Advice 실행 순서를 봐야 한다.

그래서 `AopEventLog`를 만들었다.

역할:

- Advice 실행 기록 저장
- 컨트롤러가 JSP로 전달할 수 있는 목록 제공
- 테스트에서 Advice 실행 여부 검증

`AopEventLog`는 `ThreadLocal`을 사용한다.

요청마다 로그가 섞이지 않도록 하기 위해서다.

## 9. 테스트는 무엇을 확인하나

`Week07ContextTests`는 단순히 빈이 로딩되는지만 보지 않는다.

확인하는 것:

- `Week07AopController`가 로딩되는가
- `Week07AopService`가 AOP 프록시로 감싸졌는가
- `@Before`가 ADMIN 권한을 허용하는가
- `@Before`가 USER 권한을 막는가
- 성공 주문에서 `@AfterReturning`과 `@After`가 기록되는가
- 실패 주문에서 `@AfterThrowing`과 `@After`가 기록되는가
- `@Around`가 인자를 변경하고 반환값을 가공하는가
- `@annotation` Pointcut이 `@TraceAop` 메서드를 추적하는가

특히 `AopUtils.isAopProxy(aopService)` 검증은 중요하다.

서비스가 실제로 AOP 프록시로 감싸졌는지 확인하기 때문이다.

## 10. 초심자가 7주차 코드를 읽는 순서

1. `Week07AopService`에서 핵심 로직 확인
2. `Week07AdviceAspect`에서 어떤 Advice가 있는지 확인
3. `Week07AopController`에서 URL과 서비스 호출 연결 확인
4. `/week07/before`에서 Before Advice 확인
5. `/week07/after`에서 성공/실패 Advice 비교
6. `/week07/around`에서 인자 변경과 반환값 가공 확인
7. `/week07/pointcut`에서 `@annotation` Pointcut 확인

이 순서가 좋은 이유:

먼저 핵심 로직을 보고,
그 다음 공통 기능이 어디서 끼어드는지 보는 편이 AOP를 이해하기 쉽기 때문이다.

## 11. 이 실습에서 자주 놓치는 포인트

### 포인트 1. Advice는 서비스 코드 안에서 직접 호출하지 않는다

AOP의 핵심은 공통 기능을 직접 호출하지 않는 것이다.

서비스 메서드 안에서 `authenticate()`나 `logAfterReturning()`을 직접 부르지 않는다.
스프링 프록시가 Pointcut 조건을 보고 Advice를 실행한다.

### 포인트 2. `@After`와 `@AfterReturning`은 다르다

`@AfterReturning`은 정상 종료 때만 실행된다.
`@After`는 정상 종료와 예외 발생 모두에서 실행된다.

두 Advice를 같은 것으로 보면 안 된다.

### 포인트 3. Pointcut은 너무 넓게 잡으면 위험하다

`execution(* Lect_B..*(..))`처럼 넓은 표현식은 많은 메서드에 Advice를 적용한다.

학습용으로는 편하지만,
실제 프로젝트에서는 패키지, 클래스, 메서드, 어노테이션 조건을 좁혀야 한다.

## 12. 이 실습을 끝내면 말할 수 있어야 하는 것

- AOP가 왜 필요한지 설명할 수 있는가
- 핵심 관심사와 공통 관심사를 구분할 수 있는가
- Advice와 Pointcut의 역할을 설명할 수 있는가
- `@Before`, `@AfterReturning`, `@AfterThrowing`, `@After`, `@Around`의 차이를 말할 수 있는가
- `JoinPoint`와 `ProceedingJoinPoint`의 차이를 설명할 수 있는가
- `execution`, `within`, `args`, `@annotation` Pointcut 표현식을 구분할 수 있는가
