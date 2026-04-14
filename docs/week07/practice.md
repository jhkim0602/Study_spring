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
- Advice에 적힌 적용 조건이 어떤 서비스 메서드를 가리키는가

즉 이 실습은 7주차 이론을 실제 라우트와 JSP 화면으로 검증하는 역할을 한다.

Pointcut은 별도 화면의 암기 대상이 아니라,
각 Advice가 어느 서비스 메서드에 붙는지 확인하는 기준으로 읽는다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| [`src/main/java/Lect_B/week07/Week07AopService.java`](../../src/main/java/Lect_B/week07/Week07AopService.java) | AOP 적용 대상 서비스 |
| [`src/main/java/Lect_B/week07/Week07AdviceAspect.java`](../../src/main/java/Lect_B/week07/Week07AdviceAspect.java) | Advice와 적용 조건 정의 |
| [`src/main/java/Lect_B/week07/Week07XmlAopService.java`](../../src/main/java/Lect_B/week07/Week07XmlAopService.java) | XML AOP 적용 대상 서비스 |
| [`src/main/java/Lect_B/week07/Week07XmlAdvice.java`](../../src/main/java/Lect_B/week07/Week07XmlAdvice.java) | XML에서 연결하는 POJO Advice |
| [`src/main/java/Lect_B/week07/Week07XmlAopConfig.java`](../../src/main/java/Lect_B/week07/Week07XmlAopConfig.java) | XML AOP 설정 파일 import |
| [`src/main/resources/xml/week07-aop.xml`](../../src/main/resources/xml/week07-aop.xml) | XML 기반 Aspect, Pointcut, Advice 연결 설정 |
| [`src/main/java/Lect_B/week07/AopEventLog.java`](../../src/main/java/Lect_B/week07/AopEventLog.java) | 화면에 보여 줄 Advice 실행 기록 저장 |
| [`src/main/java/Lect_B/week07/TraceAop.java`](../../src/main/java/Lect_B/week07/TraceAop.java) | 어노테이션 기반 적용 조건 확인용 어노테이션 |
| [`src/main/java/Lect_B/week07/Week07AopController.java`](../../src/main/java/Lect_B/week07/Week07AopController.java) | 7주차 실습 라우트 제어 |
| [`src/main/java/Lect_B/week07/Week07IndexController.java`](../../src/main/java/Lect_B/week07/Week07IndexController.java) | `/week07` 진입 |
| [`src/main/webapp/views/week07/*.jsp`](../../src/main/webapp/views/week07) | 실습 결과 화면 |
| [`src/test/java/Lect_B/week07/Week07ContextTests.java`](../../src/test/java/Lect_B/week07/Week07ContextTests.java) | AOP 적용 테스트 |

원본 7주차 실습 파일과 현재 프로젝트의 대응 관계는 다음과 같다.

| 원본 실습 파일 | 원본에서 확인할 내용 | 현재 프로젝트 반영 |
|---|---|---|
| `build.gradle` | `org.springframework.boot:spring-boot-starter-aspectj` 의존성 | 현재 `build.gradle`에 AOP 의존성 추가 |
| `index.jsp` | `beforeAOP`, `afterAOP`, `aroundAOP` 링크 | 메인 화면과 `/week07` 화면에서 7주차 실습 진입 |
| `AdviceAspect.java` Ex1 | `@Before`와 `execution(...performSensitiveOperation(..))` | `/week07/before`에서 인증 Advice 확인 |
| `AdviceAspect.java` Ex2 | `@Pointcut`, `@AfterReturning`, `@AfterThrowing`, `@After` | `/week07/after`에서 성공/실패/항상 실행 비교 |
| `AdviceAspect.java` Ex2 주석 | `within(...)` 표현식 비교 | 이 문서의 Pointcut 적용 조건 읽기에서 `execution`과 비교 |
| `AdviceAspect.java` Ex3 | `@Around`, `ProceedingJoinPoint`, `proceed(args)` | `/week07/around`에서 인자 변경과 반환값 가공 확인 |
| `ExAOPService.java` | AOP 적용 대상 핵심 로직 | `Week07AopService`로 현재 패키지에 맞게 재구성 |
| `ExAOPController.java` | 컨트롤러가 서비스 메서드를 호출 | `Week07AopController`가 JSP 화면으로 결과 전달 |
| PPT XML AOP 설정 | `<aop:config>`, `<aop:aspect>`, `<aop:pointcut>`, `<aop:before>`, `<aop:after-returning>`, `<aop:after-throwing>`, `<aop:after>`, `<aop:around>` | `week07-aop.xml`과 `/week07/xml`로 실제 동작 확인 |

원본 실습 코드의 Pointcut 문자열에는 `com.week06...` 형식이 섞여 있지만,
현재 통합 프로젝트에서는 실제 패키지인 `Lect_B.week07` 기준으로 고쳐 읽는다.
중요한 것은 패키지 이름 자체가 아니라 `execution`, `within`, `args`, `@annotation`이 대상을 고르는 방식이다.

또한 원본 실습은 학습용 콘솔 코드라서 다음 특징이 있다.

- 인증 성공 여부를 `Math.random()`으로 결정한다
- `@Around`에서 `Scanner`로 콘솔 입력을 받는다
- Advice 실행 결과를 `System.out.println()`으로 확인한다

현재 프로젝트는 웹 실습 흐름에 맞게 다음처럼 바꿨다.

- 랜덤 인증 대신 요청 파라미터 `role`로 성공/실패를 재현한다
- 콘솔 입력 대신 `/week07/around?role=admin` 같은 URL 파라미터를 사용한다
- 콘솔 출력 대신 `AopEventLog`와 JSP 화면으로 Advice 실행 순서를 확인한다
- 강의자료의 XML AOP 설정은 `week07-aop.xml`로 실제 프로젝트에 추가했다

## 1. 왜 `week07` 전용 패키지를 따로 만들었는가

7주차는 이전 주차와 성격이 다르다.

3~6주차가 주로 빈을 만들고 주입하고 관리하는 내용이었다면,
7주차는 이미 등록된 빈의 메서드 실행 흐름에 공통 기능을 적용한다.

그래서 다음처럼 별도 패키지로 분리했다.

```text
src/main/java/Lect_B/week07/
src/main/resources/xml/week07-aop.xml
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

- `performSensitiveOperation()`: 실행 전 인증 확인 대상
- `placeOrder()`: 정상 종료와 예외 발생 비교 대상
- `check()`: 실행 전후 전체 제어 대상
- `annotationTarget()`: `@TraceAop`가 붙은 추적 대상
- `Week07XmlAopService.processXmlOrder()`: XML 기반 After 계열 Advice 대상
- `Week07XmlAopService.checkXmlPermission()`: XML 기반 Around Advice 대상

중요한 점:

서비스 메서드 안에는 Advice 실행 코드가 없다.

즉 서비스는 핵심 로직만 가지고,
공통 기능은 `Week07AdviceAspect`가 처리한다.
XML 실습에서는 같은 역할을 `Week07XmlAdvice`와 `week07-aop.xml`이 나누어 맡는다.

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

코드를 읽을 때는 "이 메서드가 언제 실행되는가"와 "어디에 적용되는가"를 같이 봐야 한다.
XML 기반 비교 실습에서는 같은 질문을 `Week07XmlAdvice`와 `week07-aop.xml`에 던지면 된다.

### Advice와 적용 조건을 같이 읽는 법

예:

```java
@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
public void authenticate(JoinPoint joinPoint) {
}
```

이 코드는 다음처럼 읽는다.

- `@Before`: 대상 메서드 실행 전
- `execution(...)`: 메서드 실행 모양으로 대상 선택
- `performSensitiveOperation(..)`: 실제 적용 대상
- `authenticate()`: 실행할 공통 기능

또 다른 예:

```java
@Around("@annotation(Lect_B.week07.TraceAop)")
public Object traceAnnotatedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
}
```

이 코드는 다음처럼 읽는다.

- `@Around`: 대상 메서드 실행 전후 전체
- `@annotation(...)`: 특정 어노테이션이 붙은 메서드 선택
- `@TraceAop`: 실제 적용 기준

즉 Pointcut은 따로 떼어 암기하는 항목이 아니라,
Advice가 붙는 대상을 알려 주는 부분이다.

### 강의자료 Pointcut 표현식 요소를 현재 실습에서 확인하는 법

강의자료의 Pointcut 주요 표현식 요소는 `execution`, `within`, `args`, `@annotation`이다.
현재 프로젝트는 화면 실습의 안정성을 위해 실제 Advice 적용은 좁은 `execution`과 `@annotation` 중심으로 구성했고,
`within`, `args`는 같은 서비스 메서드를 기준으로 어떻게 읽을 수 있는지 함께 설명한다.

| 표현식 요소 | 기준 | 현재 실습에서 확인하는 방법 |
|---|---|---|
| `execution(...)` | 메서드 실행 모양 | `performSensitiveOperation(..)`, `placeOrder(..)`, `check(..)`에 적용된 Advice 확인 |
| `within(...)` | 클래스 또는 패키지 범위 | 원본 코드의 주석 예시처럼 패키지 전체를 잡을 때 사용 가능하다는 점을 `execution`과 비교 |
| `args(...)` | 전달 인자 타입과 개수 | `performSensitiveOperation(String, String, String, int)`와 `check(String, String)`의 인자 구조로 해석 연습 |
| `@annotation(...)` | 메서드에 붙은 어노테이션 | `/week07/pointcut`에서 `@TraceAop`가 붙은 메서드만 Around 대상이 되는지 확인 |

`execution`은 현재 실습에서 가장 직접적으로 보인다.

```java
@Before("execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))")
```

이 식은 `Week07AopService`의 `performSensitiveOperation(..)` 메서드 실행만 대상으로 삼는다.
`*`는 반환 타입을 가리지 않는다는 뜻이고,
`(..)`는 인자가 0개 이상이어도 된다는 뜻이다.

`within`은 다음처럼 같은 패키지 전체를 대상으로 잡을 때 사용할 수 있다.

```java
@Pointcut("within(Lect_B.week07..*)")
private void week07PackageOperation() {
}
```

이 식은 `Lect_B.week07` 패키지와 하위 패키지 안의 모든 클래스 메서드를 대상으로 삼는다.
하지만 현재 프로젝트에서 이 식을 실제 Advice에 바로 연결하면
`Week07AopService`의 여러 메서드가 한꺼번에 Advice 대상이 될 수 있다.
그래서 실습 코드에서는 `placeOrder(..)`처럼 범위를 좁혀 사용했다.

`args`는 메서드 이름보다 인자 타입에 초점을 둔다.

```java
@Before("args(java.lang.String, ..)")
```

이 식은 첫 번째 인자가 `String`이고 뒤에 인자가 0개 이상 올 수 있는 메서드를 대상으로 삼는다.
현재 서비스에서는 `performSensitiveOperation(String userId, String role, String message, int count)`와
`check(String userId, String role)`가 모두 이 조건에 걸릴 수 있다.
따라서 `args`는 "인자 구조 기준으로 넓게 잡을 때" 유용하지만,
실제 실습에서는 원하지 않는 메서드까지 잡히지 않도록 `execution`으로 좁혔다.

`@annotation`은 현재 프로젝트에서 별도 어노테이션으로 확인한다.

```java
@Around("@annotation(Lect_B.week07.TraceAop)")
public Object traceAnnotatedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
}
```

이 식은 메서드 이름이 아니라 `@TraceAop`가 붙어 있는지를 기준으로 한다.
그래서 `/week07/pointcut` 화면은 Pointcut 자체를 따로 외우는 화면이 아니라,
`@Around` Advice가 `@annotation` 조건으로 적용 대상을 고르는 방식을 확인하는 화면이다.

강의자료의 조합 기호도 같은 방식으로 읽는다.

| 기호 | 실습에서 읽는 법 |
|---|---|
| `*` | 반환 타입, 메서드 이름 일부, 타입 등을 모두 허용 |
| `..` | 하위 패키지 또는 0개 이상의 인자 |
| `+` | 해당 타입과 하위 타입까지 포함 |
| `&&` | 두 조건을 모두 만족해야 함 |
| `&#124;&#124;` | 둘 중 하나를 만족하면 됨 |
| `!` | 해당 조건을 제외 |

예를 들어 다음 식은 `Lect_B.week07` 패키지 안에서 `@TraceAop`가 붙은 메서드만 고른다.

```java
@Pointcut("within(Lect_B.week07..*) && @annotation(Lect_B.week07.TraceAop)")
private void tracedWeek07Operation() {
}
```

현재 프로젝트에서는 같은 의미를 더 단순하게 보여 주기 위해
`@Around("@annotation(Lect_B.week07.TraceAop)")`를 사용했다.

### XML 설정을 코드에서 실제로 읽는 법

강의자료의 XML 기반 AOP는 현재 프로젝트에서 `week07-aop.xml`로 확인한다.

```xml
<aop:config proxy-target-class="true">
    <aop:aspect id="week07XmlAspect" ref="week07XmlAdvice">
        <aop:pointcut id="xmlOrderOperation"
            expression="execution(* Lect_B.week07.Week07XmlAopService.processXmlOrder(..))" />

        <aop:before pointcut-ref="xmlOrderOperation" method="beforeXml" />
        <aop:after-returning pointcut-ref="xmlOrderOperation" method="afterReturningXml" returning="result" />
        <aop:after-throwing pointcut-ref="xmlOrderOperation" method="afterThrowingXml" throwing="ex" />
        <aop:after pointcut-ref="xmlOrderOperation" method="afterXml" />
    </aop:aspect>
</aop:config>
```

이 XML은 다음처럼 읽는다.

- `week07XmlAdvice`: Advice 메서드를 가진 POJO 빈
- `xmlOrderOperation`: `processXmlOrder(..)`를 가리키는 Pointcut 이름
- `<aop:before>`: `beforeXml()`을 대상 메서드 실행 전에 연결
- `<aop:after-returning>`: 정상 종료 후 `afterReturningXml()` 연결
- `<aop:after-throwing>`: 예외 발생 후 `afterThrowingXml()` 연결
- `<aop:after>`: 성공/실패와 관계없이 `afterXml()` 연결
- `returning="result"`: 반환값을 `afterReturningXml()`의 `result` 파라미터로 전달
- `throwing="ex"`: 예외 객체를 `afterThrowingXml()`의 `ex` 파라미터로 전달

Around Advice도 XML에서 연결한다.

```xml
<aop:pointcut id="xmlCheckOperation"
    expression="execution(* Lect_B.week07.Week07XmlAopService.checkXmlPermission(..))" />
<aop:around pointcut-ref="xmlCheckOperation" method="aroundXml" />
```

이 설정은 `checkXmlPermission(..)` 호출을 `aroundXml()`이 감싼다는 뜻이다.
`aroundXml()` 내부에서는 어노테이션 방식과 마찬가지로 `ProceedingJoinPoint.proceed(args)`를 호출해야 대상 메서드가 실행된다.

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
- 화면의 `[Before]` 기록은 `authenticate()` Advice에서 추가한 것이다

즉 이 화면은 Advice가 핵심 로직 실행 전에 개입할 수 있다는 것을 보여 준다.

이때 적용 조건은 `execution(* Lect_B.week07.Week07AopService.performSensitiveOperation(..))`이다.
따라서 이 Advice는 아무 메서드에나 붙는 것이 아니라,
민감 작업 메서드에만 붙는다.

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

세 Advice는 모두 `orderOperation()`을 적용 조건으로 사용한다.
`orderOperation()`은 `placeOrder(..)` 메서드를 가리킨다.

```java
@Pointcut("execution(* Lect_B.week07.Week07AopService.placeOrder(..))")
private void orderOperation() {
}
```

즉 성공/실패/항상 실행의 차이는 Advice 종류에서 생기고,
적용 대상은 같은 주문 메서드다.

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

적용 조건은 `execution(* Lect_B.week07.Week07AopService.check(..))`이다.
따라서 이 Around Advice는 `check(..)` 메서드를 감싸는 예제다.

## 7. 어노테이션 기준 Around 화면은 어떻게 읽어야 하나

`/week07/pointcut`은 "Pointcut만 따로 배우는 화면"이 아니다.

이 화면은 `@Around` Advice가 적용 대상을 고르는 또 다른 방식을 보여 준다.

대상 메서드:

```java
@TraceAop
public String annotationTarget(String label) {
    return "@TraceAop 대상 메서드 실행: " + label;
}
```

Aspect의 Advice:

```java
@Around("@annotation(Lect_B.week07.TraceAop)")
```

읽는 법:

- `@Around`: 대상 메서드 실행 전후 전체
- `@annotation(...)`: 어노테이션이 붙은 메서드를 찾음
- `TraceAop`: 실제 기준 어노테이션
- `annotationTarget()`: 현재 실습에서 해당 어노테이션이 붙은 대상

이 예제는 `execution(...)`과 비교해서 읽어야 한다.

- `execution(...)`: 메서드 이름과 위치를 식으로 지정
- `@annotation(...)`: 메서드에 붙은 어노테이션으로 지정

둘 다 Advice 적용 대상을 고르는 조건이다.
차이는 "어떤 기준으로 고르는가"다.

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

XML 실습에서도 같은 `AopEventLog`를 사용한다.
`Week07XmlAdvice`는 XML에서 빈으로 등록되지만,
생성자 인자로 `aopEventLog` 빈을 주입받아 JSP 화면에 실행 기록을 남긴다.

## 9. `/week07/xml` 화면은 무엇을 보여 주나

`/week07/xml`은 강의자료의 XML 기반 AOP 설정을 실제 웹 화면에서 확인하는 실습이다.

기본 요청:

```text
/week07/xml
```

예외 요청:

```text
/week07/xml?orderValue=20&minimumValue=50
```

권한 요청:

```text
/week07/xml?role=user
```

읽는 법:

- `processXmlOrder(..)`는 `xmlOrderOperation` Pointcut 대상이다
- 성공하면 `[XML Before]`, `[XML AfterReturning]`, `[XML After]`가 기록된다
- 실패하면 `[XML Before]`, `[XML AfterThrowing]`, `[XML After]`가 기록된다
- `checkXmlPermission(..)`은 `xmlCheckOperation` Pointcut 대상이다
- `role=admin` 또는 `role=user`는 XML Around Advice에서 대문자로 바뀐다
- 화면의 실행 기록은 어노테이션 Advice가 아니라 XML로 연결된 `Week07XmlAdvice`가 남긴다

이 화면에서 중요한 비교는 다음이다.

| 비교 대상 | 어노테이션 기반 | XML 기반 |
|---|---|---|
| Advice 클래스 | `Week07AdviceAspect` | `Week07XmlAdvice` |
| 대상 서비스 | `Week07AopService` | `Week07XmlAopService` |
| 연결 방식 | `@Before`, `@Around` 등 | `<aop:before>`, `<aop:around>` 등 |
| 설정 위치 | Java 클래스 | `week07-aop.xml` |
| 화면 | `/week07/before`, `/week07/after`, `/week07/around`, `/week07/pointcut` | `/week07/xml` |

## 10. 테스트는 무엇을 확인하나

`Week07ContextTests`는 단순히 빈이 로딩되는지만 보지 않는다.

확인하는 것:

- `Week07AopController`가 로딩되는가
- `Week07AopService`가 AOP 프록시로 감싸졌는가
- `@Before`가 ADMIN 권한을 허용하는가
- `@Before`가 USER 권한을 막는가
- 성공 주문에서 `@AfterReturning`과 `@After`가 기록되는가
- 실패 주문에서 `@AfterThrowing`과 `@After`가 기록되는가
- `@Around`가 인자를 변경하고 반환값을 가공하는가
- `@annotation` 조건이 `@TraceAop` 메서드를 추적하는가
- XML AOP 서비스가 프록시로 감싸졌는가
- XML `<aop:before>`, `<aop:after-returning>`, `<aop:after-throwing>`, `<aop:after>`, `<aop:around>`가 실제로 실행되는가

특히 `AopUtils.isAopProxy(aopService)` 검증은 중요하다.

서비스가 실제로 AOP 프록시로 감싸졌는지 확인하기 때문이다.
XML 실습에서는 `AopUtils.isAopProxy(xmlAopService)`도 함께 확인한다.

## 11. 초심자가 7주차 코드를 읽는 순서

1. `Week07AopService`에서 핵심 로직 확인
2. `Week07AdviceAspect`에서 Advice 종류 확인
3. 각 Advice의 적용 조건이 어떤 메서드를 가리키는지 확인
4. `Week07AopController`에서 URL과 서비스 호출 연결 확인
5. `/week07/before`에서 Before Advice 확인
6. `/week07/after`에서 성공/실패 Advice 비교
7. `/week07/around`에서 인자 변경과 반환값 가공 확인
8. `/week07/pointcut`에서 어노테이션 기반 적용 조건 확인
9. `week07-aop.xml`에서 XML 기반 Aspect와 Pointcut 연결 확인
10. `/week07/xml`에서 XML Advice 실행 기록 확인

이 순서가 좋은 이유:

먼저 핵심 로직을 보고,
그 다음 공통 기능이 어디서 끼어드는지 보는 편이 AOP를 이해하기 쉽기 때문이다.

## 12. 이 실습에서 자주 놓치는 포인트

### 포인트 1. Advice는 서비스 코드 안에서 직접 호출하지 않는다

AOP의 핵심은 공통 기능을 직접 호출하지 않는 것이다.

서비스 메서드 안에서 `authenticate()`나 `logAfterReturning()`을 직접 부르지 않는다.
스프링 프록시가 적용 조건을 보고 Advice를 실행한다.

### 포인트 2. `@After`와 `@AfterReturning`은 다르다

`@AfterReturning`은 정상 종료 때만 실행된다.
`@After`는 정상 종료와 예외 발생 모두에서 실행된다.

두 Advice를 같은 것으로 보면 안 된다.

### 포인트 3. 적용 조건은 Advice와 같이 읽어야 한다

`execution(* Lect_B..*(..))`처럼 넓은 표현식은 많은 메서드에 Advice를 적용한다.

학습할 때도 실제 프로젝트에서도 중요한 질문은 같다.

> "이 Advice가 정확히 어느 메서드에 붙는가?"

이 질문 없이 Advice 종류만 외우면 AOP 코드를 제대로 읽기 어렵다.

### 포인트 4. XML 기반 AOP도 핵심은 같다

XML 기반 AOP는 설정 위치만 다르다.
핵심은 여전히 다음 질문이다.

> "어떤 Advice가 어느 Pointcut 대상에 언제 실행되는가?"

어노테이션에서는 이 연결을 Java 코드에서 읽고,
XML에서는 `<aop:pointcut>`과 `<aop:before>` 같은 태그에서 읽는다.

## 13. 이 실습을 끝내면 말할 수 있어야 하는 것

- AOP가 왜 필요한지 설명할 수 있는가
- 핵심 관심사와 공통 관심사를 구분할 수 있는가
- Advice와 적용 조건의 역할을 설명할 수 있는가
- `@Before`, `@AfterReturning`, `@AfterThrowing`, `@After`, `@Around`의 차이를 말할 수 있는가
- `<aop:before>`, `<aop:after-returning>`, `<aop:after-throwing>`, `<aop:after>`, `<aop:around>`가 각각 어떤 Advice에 대응하는지 설명할 수 있는가
- `JoinPoint`와 `ProceedingJoinPoint`의 차이를 설명할 수 있는가
- `execution`, `within`, `args`, `@annotation`이 대상을 고르는 기준이라는 점을 설명할 수 있는가
- `*`, `..`, `+`, `&&`, `||`, `!`를 Pointcut 표현식 안에서 읽을 수 있는가
- 원본 실습의 `beforeAOP`, `afterAOP`, `aroundAOP` 흐름이 현재 `/week07/before`, `/week07/after`, `/week07/around`로 어떻게 바뀌었는지 설명할 수 있는가
- 강의자료의 XML AOP 설정이 현재 `week07-aop.xml`과 `/week07/xml`에 어떻게 반영됐는지 설명할 수 있는가
