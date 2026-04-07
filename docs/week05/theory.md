# Week 05 Theory

## 주제

5주차는 빈 객체 관리, 즉 스프링이 빈을 "어떻게 운영하는가"를 이해하는 주차다.

## 이 문서를 읽기 전에

5주차를 제대로 이해하려면 3~4주차의 아래 개념이 선행되어야 한다.

- 빈은 스프링이 관리하는 객체라는 점
- DI로 빈을 다른 클래스에 주입할 수 있다는 점
- `ApplicationContext`가 빈을 가지고 있다는 점

5주차는 그 다음 질문으로 넘어간다.

- 빈은 언제 만들어지는가?
- 얼마나 오래 살아남는가?
- 종료 직전에는 무엇을 할 수 있는가?
- 설정값을 코드 밖에서 읽어올 수 있는가?

## 현재 프로젝트에서 먼저 볼 코드

- `src/main/java/Lect_B/week05/SmsSender.java`
- `src/main/java/Lect_B/week05/WorkUnit.java`
- `src/main/java/Lect_B/week05/RequestTrace.java`
- `src/main/java/Lect_B/week05/SessionTrace.java`
- `src/main/java/Lect_B/week05/ScopeSingletonClient.java`
- `src/main/java/Lect_B/week05/ScopeFactoryClient.java`
- `src/main/java/Lect_B/week05/Week05LifecycleBean.java`
- `src/main/java/Lect_B/week05/Week05AwareComponent.java`
- `src/main/java/Lect_B/week05/Week05ExternalConfigComponent.java`
- `src/main/resources/static/external.properties`

## 목차

- [1. 5주차의 핵심 질문](#1-5주차의-핵심-질문)
- [2. 왜 빈 관리가 중요한가](#2-왜-빈-관리가-중요한가)
- [3. 스코프란 무엇인가](#3-스코프란-무엇인가)
- [4. 스코프 4가지](#4-스코프-4가지)
- [5. 서로 다른 스코프 빈 의존 문제](#5-서로-다른-스코프-빈-의존-문제)
- [6. 라이프사이클 메서드](#6-라이프사이클-메서드)
- [7. Aware 인터페이스](#7-aware-인터페이스)
- [8. 외부 설정 프로퍼티](#8-외부-설정-프로퍼티)
- [9. 슬라이드 도식 해석](#9-슬라이드-도식-해석)
- [10. 자주 헷갈리는 질문](#10-자주-헷갈리는-질문)
- [11. 시험 대비 핵심 정리](#11-시험-대비-핵심-정리)

## 1. 5주차의 핵심 질문

5주차를 한 문장으로 요약하면:

> "스프링이 관리하는 빈은 언제 만들어지고, 얼마나 오래 살아 있고, 어떻게 종료되는가?"

이다.

3~4주차가 "빈을 만들고 주입하는 방법"을 배웠다면,  
5주차는 "그 빈을 어떤 정책으로 운영하는가"를 배우는 주차다.

## 2. 왜 빈 관리가 중요한가

웹 애플리케이션에서는:

- 어떤 객체는 하나만 있으면 충분하고
- 어떤 객체는 요청마다 새로 필요하고
- 어떤 객체는 사용자 세션마다 따로 있어야 하며
- 어떤 객체는 시작/종료 시점에 준비 작업을 해야 한다

그래서 스프링은:

- 생성 시점
- 재사용 범위
- 초기화/종료 훅

을 관리하는 기능을 제공한다.

### 실제 코드로 보는 "공유해도 되는 객체"

`src/main/java/Lect_B/week05/SmsSender.java`

```java
@Component("smsSender")
@Primary
public class SmsSender {

    private final String senderName;
    private final String instanceId;

    public SmsSender() {
        this("5주차 기본 SMS 발신기");
    }
}
```

이 클래스는 기본적으로 singleton으로 관리된다.

- `@Component("smsSender")`
  - 이 이름으로 빈 등록
- `@Primary`
  - 같은 타입 빈이 여러 개일 때 우선 후보로 사용

이런 공통 서비스 객체는 보통 하나를 재사용해도 문제가 적다.

## 3. 스코프란 무엇인가

스코프(scope)는 빈이 **어떤 범위에서 몇 개나 존재할 수 있는가**를 뜻한다.

쉽게 말하면:

- 하나만 만들까?
- 요청마다 만들까?
- 세션마다 만들까?

를 정하는 규칙이다.

스코프는 단순한 문법이 아니라  
"이 객체를 공유해도 되는가?"에 대한 설계 판단이다.

## 4. 스코프 4가지

### 4-1. singleton

- 기본 스코프
- 컨테이너 안에 인스턴스가 하나만 존재
- 여러 곳에서 같은 객체를 공유

실제 예제:

```java
@Component("smsSender")
public class SmsSender {
}
```

적합한 예:

- 서비스 객체
- 유틸리티 객체
- 상태가 거의 없는 공통 컴포넌트

### 4-2. prototype

- 조회할 때마다 새 객체 생성

실제 예제:

`src/main/java/Lect_B/week05/WorkUnit.java`

```java
@Component("workUnit")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkUnit {

    private final String unitId = UUID.randomUUID().toString().substring(0, 8);
}
```

중요 포인트:

- `@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)`
  - 이 빈은 조회할 때마다 새 객체로 다뤄라

적합한 예:

- 작업 단위 객체
- 임시 계산용 객체
- 내부 상태가 분리돼야 하는 객체

### 4-3. request

- HTTP 요청 하나 동안만 존재

실제 예제:

`src/main/java/Lect_B/week05/RequestTrace.java`

```java
@Component
@RequestScope
public class RequestTrace {

    private final String requestId = UUID.randomUUID().toString().substring(0, 8);
}
```

중요 포인트:

- `@RequestScope`
  - 하나의 HTTP 요청 안에서는 같은 객체를 유지

적합한 예:

- 요청 추적 ID
- 요청 중간 로그 객체
- 요청 전용 컨텍스트

### 4-4. session

- 사용자 세션마다 하나 존재

실제 예제:

`src/main/java/Lect_B/week05/SessionTrace.java`

```java
@Component
@SessionScope
public class SessionTrace {

    private final String sessionBeanId = UUID.randomUUID().toString().substring(0, 8);
    private int accessCount;
}
```

중요 포인트:

- `@SessionScope`
  - 같은 브라우저 세션 동안 유지

적합한 예:

- 로그인 정보
- 장바구니
- 사용자별 상태 데이터

## 5. 서로 다른 스코프 빈 의존 문제

5주차에서 가장 중요한 함정은 이것이다.

singleton 빈이 prototype 빈을 직접 주입받으면,  
singleton이 처음 만들어질 때 prototype도 한 번 만들어져 들어간다.

그래서 이후에는 singleton 안에 들어간 같은 prototype을 계속 쓴다.

### 문제 코드

`src/main/java/Lect_B/week05/ScopeSingletonClient.java`

```java
@Component
public class ScopeSingletonClient {

    private final WorkUnit workUnit;

    public ScopeSingletonClient(@Qualifier("workUnit") WorkUnit workUnit) {
        this.workUnit = workUnit;
    }
}
```

이 코드가 의미하는 것:

- `ScopeSingletonClient`는 기본적으로 singleton이다
- 생성 시점에 `workUnit`을 한 번 주입받는다
- 그 뒤에는 같은 `workUnit` 참조를 계속 들고 있다

즉 선언은 prototype이어도,
사용 결과는 "고정된 하나의 객체"처럼 보일 수 있다.

### 해결 코드

`src/main/java/Lect_B/week05/ScopeFactoryClient.java`

```java
@Component
public class ScopeFactoryClient {

    private final ObjectFactory<WorkUnit> workUnitFactory;

    public ScopeFactoryClient(@Qualifier("workUnit") ObjectFactory<WorkUnit> workUnitFactory) {
        this.workUnitFactory = workUnitFactory;
    }

    public WorkUnit createWorkUnit() {
        return workUnitFactory.getObject();
    }
}
```

핵심 차이:

- 직접 주입: 생성 시점 고정
- `ObjectFactory`: 사용 시점 조회

`ObjectFactory.getObject()`는  
"필요한 순간 컨테이너에게 새 prototype 빈을 다시 달라"고 요청하는 것이다.

## 6. 라이프사이클 메서드

빈은 단순히 만들어지고 끝나는 것이 아니다.

일반 흐름:

1. 객체 생성
2. 의존성 주입
3. 초기화
4. 사용
5. 종료 직전 정리

### 실제 코드 예제

`src/main/java/Lect_B/week05/Week05LifecycleBean.java`

```java
public class Week05LifecycleBean implements InitializingBean, DisposableBean {

    public Week05LifecycleBean() { }

    @PostConstruct
    public void postConstruct() { }

    @Override
    public void afterPropertiesSet() { }

    public void customInit() { }

    @PreDestroy
    public void preDestroy() { }

    @Override
    public void destroy() { }

    public void customDestroy() { }
}
```

그리고 설정 클래스:

`src/main/java/Lect_B/week05/Week05LifecycleConfig.java`

```java
@Configuration
public class Week05LifecycleConfig {

    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public Week05LifecycleBean week05LifecycleBean() {
        return new Week05LifecycleBean();
    }
}
```

이 예제가 보여 주는 훅:

- 생성자
  - 객체가 만들어지는 순간
- `@PostConstruct`
  - 의존성 주입이 끝난 뒤 초기화
- `afterPropertiesSet()`
  - `InitializingBean` 인터페이스 방식
- `initMethod`
  - 설정 클래스에서 지정한 커스텀 초기화 메서드
- `@PreDestroy`
  - 종료 직전 호출
- `destroy()`
  - `DisposableBean` 인터페이스 방식
- `destroyMethod`
  - 설정 클래스에서 지정한 커스텀 종료 메서드

즉 생성자만으로는 표현되지 않는 준비/정리 시점을 분리할 수 있다.

## 7. Aware 인터페이스

빈이 컨테이너 정보를 직접 알아야 하는 경우도 있다.

실제 예제:

`src/main/java/Lect_B/week05/Week05AwareComponent.java`

```java
@Component
public class Week05AwareComponent implements BeanNameAware, ApplicationContextAware {

    private String beanName;
    private ApplicationContext applicationContext;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
```

### `BeanNameAware`

빈 이름을 전달받는다.

왜 의미가 있는가:

- 스프링이 타입뿐 아니라 이름으로도 빈을 관리한다는 사실을 보여주기 때문이다

### `ApplicationContextAware`

컨테이너 자체를 전달받는다.

왜 의미가 있는가:

- 빈이 자신이 어떤 컨테이너 안에 있는지 알 수 있다
- 필요하다면 컨테이너에서 다른 빈도 조회할 수 있다

주의:

너무 남용하면 DI의 장점이 줄어든다.  
정말 컨테이너 자체가 필요한 경우에 제한적으로 쓰는 것이 좋다.

## 8. 외부 설정 프로퍼티

실무에서는 설정값을 코드에 직접 박아 두면 유지보수가 힘들다.

예:

- 서버 주소 변경
- 포트 변경
- SMTP 설정 변경
- DB 정보 변경

이런 값은 코드 수정 없이 바꾸고 싶다.  
그래서 프로퍼티 파일을 사용한다.

### 설정 파일 예제

`src/main/resources/static/external.properties`

```properties
week05.server.port=8080
week05.server.address=localhost

week05.mail.host=smtp.gmail.com
week05.mail.port=587
week05.mail.timeout-seconds=30
week05.mail.credentials.username=lectb_week05
```

### `@PropertySource`

`src/main/java/Lect_B/week05/Week05PropertiesConfig.java`

```java
@Configuration
@PropertySource("classpath:static/external.properties")
public class Week05PropertiesConfig {
}
```

역할:

- 외부 프로퍼티 파일을 읽도록 등록

### `@Value`

`src/main/java/Lect_B/week05/Week05ExternalConfigComponent.java`

```java
@Value("${week05.server.port}")
private int serverPort;

@Value("${week05.server.address}")
private String serverAddress;
```

역할:

- 단일 값 주입

### `@ConfigurationProperties`

같은 클래스의 아래 선언:

```java
@Component
@ConfigurationProperties(prefix = "week05.mail")
public class Week05ExternalConfigComponent {
}
```

역할:

- `week05.mail.*` 아래의 여러 설정을 객체 필드로 한 번에 묶어 준다

즉:

- 단일 값 = `@Value`
- 구조화된 설정 묶음 = `@ConfigurationProperties`

로 기억하면 좋다.

## 9. 슬라이드 도식 해석

### singleton 그림

여러 클라이언트 요청이 들어와도 같은 빈 하나를 공유한다.

현재 프로젝트에서는 `SmsSender`가 가장 이해하기 좋은 예다.

### prototype 그림

같은 이름을 요청해도 매번 다른 인스턴스가 생성된다.

현재 프로젝트에서는 `WorkUnit`이 그 예다.

### request 그림

요청 A와 요청 B는 서로 다른 request 빈을 가진다.

현재 프로젝트에서는 `RequestTrace`가 그 예다.

### session 그림

같은 세션 안에서는 같은 객체가 유지되고,
새 세션이면 다른 객체가 된다.

현재 프로젝트에서는 `SessionTrace`가 그 예다.

### 서로 다른 스코프 의존 그림

singleton이 prototype을 직접 품고 있으면 새로 생성되지 않는다.  
그래서 `ObjectFactory`가 필요하다.

## 10. 자주 헷갈리는 질문

### Q1. singleton이 항상 좋은가?

아니다.  
공유해도 되는 객체에는 좋지만, 독립 상태가 필요한 객체에는 맞지 않는다.

### Q2. prototype이면 무조건 새 객체가 보장되나?

`getBean()` 등으로 새로 조회하면 그렇다.  
하지만 singleton 안에 직접 주입받으면 그 시점의 객체가 고정될 수 있다.

### Q3. request/session은 왜 웹에서만 중요하나?

그 범위 자체가 HTTP 요청과 사용자 세션에 의존하기 때문이다.

### Q4. 라이프사이클 메서드는 생성자와 뭐가 다른가?

생성자는 객체 생성 직후다.  
라이프사이클 메서드는 의존성 주입 이후, 또는 종료 직전 같은 더 구체적인 시점에 실행된다.

### Q5. `@Primary`는 왜 붙이나?

같은 타입 빈이 여러 개 있을 때 기본 후보를 정하기 위해서다.  
즉 스프링이 "기본 선택지"를 알 수 있게 해 준다.

## 11. 시험 대비 핵심 정리

- 스코프는 빈의 생존 범위와 재사용 범위를 정한다.
- 기본 스코프는 singleton이다.
- prototype은 조회할 때마다 새 객체를 만든다.
- request는 요청마다, session은 세션마다 유지된다.
- singleton이 prototype을 직접 주입받으면 prototype이 사실상 고정된다.
- 이를 해결하려면 `ObjectFactory` 같은 지연 조회 방식이 필요하다.
- 라이프사이클 훅은 `@PostConstruct`, `@PreDestroy`, 인터페이스 방식, custom method 방식이 있다.
- `BeanNameAware`, `ApplicationContextAware`는 컨테이너 정보가 필요할 때 사용한다.
- `@Value`는 단일 값, `@ConfigurationProperties`는 구조화된 설정 묶음에 적합하다.
