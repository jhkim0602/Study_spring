# Week 06 Theory

## 주제

6주차는 5주차의 `빈 객체 관리` 개념을 실습 코드 관점에서 다시 정리하는 주차다.

## 이 문서를 읽기 전에

6주차는 새로운 거대한 개념을 처음 도입하기보다는,  
5주차 개념을 "다른 코드 형태로 다시 이해하게 하는 역할"이 크다.

즉 6주차 이론은 다음을 목표로 한다.

- 이미 배운 scope를 더 명확하게 만들고
- lifecycle, aware, properties를 다시 묶어 보고
- 실습 파일 이름과 개념을 1:1로 연결한다

## 현재 프로젝트에서 먼저 볼 코드

- `src/main/java/Lect_B/week06/BeanScopeConfig.java`
- `src/main/java/Lect_B/week06/BeanScopeController.java`
- `src/main/java/Lect_B/week06/Week06DifferentScopeClient.java`
- `src/main/java/Lect_B/week06/Week06ObjectFactoryClient.java`
- `src/main/java/Lect_B/week06/InitDestroyUnit.java`
- `src/main/java/Lect_B/week06/LifeCycleConfig.java`
- `src/main/java/Lect_B/week06/AwareInterfaceImp.java`
- `src/main/java/Lect_B/week06/ExternalConfigComponent.java`
- `src/main/resources/static/external.properties`

## 목차

- [1. 6주차를 다시 배우는 이유](#1-6주차를-다시-배우는-이유)
- [2. 스코프 4가지 다시 보기](#2-스코프-4가지-다시-보기)
- [3. 서로 다른 범위 빈 의존 문제 다시 보기](#3-서로-다른-범위-빈-의존-문제-다시-보기)
- [4. 라이프사이클 메서드 다시 보기](#4-라이프사이클-메서드-다시-보기)
- [5. Aware 인터페이스 다시 보기](#5-aware-인터페이스-다시-보기)
- [6. 외부 설정 프로퍼티 다시 보기](#6-외부-설정-프로퍼티-다시-보기)
- [7. 왜 6주차 실습이 중요한가](#7-왜-6주차-실습이-중요한가)
- [8. 시험 대비 핵심 정리](#8-시험-대비-핵심-정리)

## 1. 6주차를 다시 배우는 이유

학생 입장에서는 종종 이런 일이 생긴다.

- 5주차에서 개념 설명은 들었다
- 그런데 실제 코드로 보면 다시 헷갈린다

6주차는 바로 그 지점을 보완한다.

즉 6주차의 의미는:

- 개념 복습
- 실습 패턴 재확인
- 코드와 이론 연결 강화

다.

특히 6주차는 "이론 정의"보다  
"지금 이 줄이 무슨 의미인지"를 코드 결과로 확인하게 해 주는 주차다.

## 2. 스코프 4가지 다시 보기

6주차에서는 스코프를 설명만 하지 않고,
실제로 같은 빈을 두 번 조회했을 때 같은 객체인지 비교한다.

### 실제 설정 코드

`src/main/java/Lect_B/week06/BeanScopeConfig.java`

```java
@Configuration
public class BeanScopeConfig {

    @Bean(name = "week06ScopeBean0")
    public Week06SmsSender singletonBean() {
        return new Week06SmsSender("week06 singleton");
    }

    @Bean(name = "week06ScopeBean1")
    @Scope("prototype")
    public Week06SmsSender prototypeBean() {
        return new Week06SmsSender("week06 prototype");
    }

    @Bean(name = "week06ScopeBean2")
    @RequestScope
    public Week06SmsSender requestBean() {
        return new Week06SmsSender("week06 request");
    }

    @Bean(name = "week06ScopeBean3")
    @SessionScope
    public Week06SmsSender sessionBean() {
        return new Week06SmsSender("week06 session");
    }
}
```

이 코드가 의미하는 것:

- `@Configuration`
  - 설정 클래스
- `@Bean(name = "...")`
  - 각각 다른 이름의 빈 등록
- `@Scope("prototype")`
  - 조회할 때마다 새 객체
- `@RequestScope`
  - 한 HTTP 요청 동안 유지
- `@SessionScope`
  - 한 세션 동안 유지

### singleton

- 하나만 생성
- 계속 재사용

### prototype

- 조회할 때마다 새 객체 생성

### request

- 한 HTTP 요청 동안 유지

### session

- 한 사용자 세션 동안 유지

즉 6주차에서는 scope를 "정의"가 아니라 "출력 비교 대상"으로 이해해야 한다.

### 실제 조회 코드

`src/main/java/Lect_B/week06/BeanScopeController.java`

```java
for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 2; j++) {
        scopeBeanArray[i][j] = context.getBean("week06ScopeBean" + i, Week06SmsSender.class);
    }
    sameFlags[i] = scopeBeanArray[i][0] == scopeBeanArray[i][1];
}
```

이 코드가 왜 중요한가:

- 같은 이름의 빈을 두 번 조회한다
- `==`로 같은 객체인지 비교한다
- 스코프 차이를 실제 결과로 보여 준다

즉 이론을 눈으로 검증하는 코드다.

## 3. 서로 다른 범위 빈 의존 문제 다시 보기

이 부분이 여전히 핵심이다.

singleton이 prototype을 생성자에서 직접 주입받으면,  
그 prototype은 singleton 생성 시점에 한 번 들어간 객체다.

그래서 이후 호출에서도 같은 객체가 나온다.

이건 학생이 처음 보면 매우 이상하게 느끼기 쉽다.

왜냐하면 "prototype이면 당연히 매번 새 객체 아닌가?"라고 생각하기 때문이다.

하지만 중요한 것은:

- prototype 선언
- 실제 사용 위치

를 같이 봐야 한다는 점이다.

### 문제 코드

`src/main/java/Lect_B/week06/Week06DifferentScopeClient.java`

```java
@Component
public class Week06DifferentScopeClient {

    private final Week06WorkUnit workUnit;

    public Week06DifferentScopeClient(Week06WorkUnit workUnit) {
        this.workUnit = workUnit;
    }
}
```

이 코드의 의미:

- 클래스는 기본적으로 singleton
- 생성 시점에 `Week06WorkUnit`을 한 번 받는다
- 이후 같은 참조를 계속 사용한다

즉 prototype 선언만으로는 충분하지 않다.

### 해결 코드

`src/main/java/Lect_B/week06/Week06ObjectFactoryClient.java`

```java
@Component
public class Week06ObjectFactoryClient {

    private final ObjectFactory<Week06WorkUnit> workUnitFactory;

    public Week06ObjectFactoryClient(
            @Qualifier("week06WorkUnit") ObjectFactory<Week06WorkUnit> workUnitFactory) {
        this.workUnitFactory = workUnitFactory;
    }

    public Week06WorkUnit createWorkUnit() {
        return workUnitFactory.getObject();
    }
}
```

핵심:

- `ObjectFactory`를 주입받아 두고
- 필요할 때마다 `getObject()`로 새 prototype을 가져온다

즉 "사용 시점 조회"가 핵심 해결책이다.

## 4. 라이프사이클 메서드 다시 보기

6주차에서는 라이프사이클을 다음 3층으로 이해하면 좋다.

### 실제 코드

`src/main/java/Lect_B/week06/InitDestroyUnit.java`

```java
public class InitDestroyUnit implements InitializingBean, DisposableBean {

    public InitDestroyUnit() { }

    public void init() { }

    public void cleanup() { }

    @PostConstruct
    public void postConstruct() { }

    @Override
    public void afterPropertiesSet() { }

    @PreDestroy
    public void preDestroy() { }

    @Override
    public void destroy() { }
}
```

그리고 설정 클래스:

`src/main/java/Lect_B/week06/LifeCycleConfig.java`

```java
@Configuration
public class LifeCycleConfig {

    @Bean(initMethod = "init", destroyMethod = "cleanup")
    public InitDestroyUnit myBean() {
        return new InitDestroyUnit();
    }
}
```

### 4-1. 생성 직후 단계

- constructor
- `@PostConstruct`

### 4-2. 설정 완료 직후 단계

- `afterPropertiesSet()`
- custom `initMethod`

### 4-3. 종료 직전 단계

- `@PreDestroy`
- `destroy()`
- custom `destroyMethod`

즉 "초기화"도 한 번이 아니고,  
"종료"도 하나의 메서드만 있는 것이 아니다.

## 5. Aware 인터페이스 다시 보기

실제 예제:

`src/main/java/Lect_B/week06/AwareInterfaceImp.java`

```java
@Component
public class AwareInterfaceImp implements BeanNameAware, ApplicationContextAware {

    private ApplicationContext context;
    private String beanName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
```

### `BeanNameAware`

빈 이름을 알려준다.

이것이 중요한 이유:

- 스프링이 빈을 이름으로도 관리한다는 사실을 보여주기 때문이다

### `ApplicationContextAware`

컨테이너 자체를 알려준다.

이것이 중요한 이유:

- 빈은 자신이 어떤 컨테이너 안에 있는지 알 수 있다
- 필요하다면 컨테이너에서 다른 빈도 조회할 수 있다

즉 Aware 인터페이스는:

> "컨테이너가 빈에게 메타정보를 주입할 수 있다"

는 것을 보여 주는 예제다.

## 6. 외부 설정 프로퍼티 다시 보기

6주차 실습에서는 이 부분을 더 직관적으로 본다.

### 설정 파일

`src/main/resources/static/external.properties`

```properties
week06.server.port=8080
week06.server.address=localhost
week06.message.greeting=hello, week06 external properties!

week06.datasource.url=jdbc:mysql://localhost:3306/mydb
week06.datasource.user-name=root
week06.datasource.password=password
```

### 실제 코드

`src/main/java/Lect_B/week06/ExternalConfigComponent.java`

```java
@Component
@ConfigurationProperties(prefix = "week06.datasource")
public class ExternalConfigComponent {

    @Value("${week06.server.port}")
    private String serverPort;

    @Value("${week06.server.address}")
    private String serverAddress;

    @Value("${week06.message.greeting}")
    private String greeting;

    private String url;
    private String userName;
    private String password;
}
```

### `@Value`

한 줄짜리 값 읽기

예:

- 서버 주소
- 포트
- 메시지

### `@ConfigurationProperties`

한 묶음의 설정 읽기

예:

- datasource URL
- 사용자명
- 비밀번호

중요한 차이:

- `@Value`: 점으로 찍은 한 값
- `@ConfigurationProperties`: prefix 아래 전체 묶음

## 7. 왜 6주차 실습이 중요한가

6주차 실습은 학생에게 다음 훈련을 시킨다.

- 같은 개념을 여러 코드 구조에서 읽는 훈련
- 출력 결과를 보고 빈 관리 정책을 해석하는 훈련
- 이론 단어를 실제 파일명과 연결하는 훈련

특히 `BeanScopeController`는:

- `ModelAndView`
- `context.getBean(...)`
- `==` 비교
- `List.of(...)`

같은 자바/스프링 문법이 함께 나온다.

즉 6주차는 "복습"이지만,  
이해를 굳히는 데 매우 중요한 복습이다.

## 8. 시험 대비 핵심 정리

- singleton은 공유용, prototype은 독립 객체용이다.
- request/session은 웹 맥락이 있어야 의미가 뚜렷하다.
- singleton 안에 prototype을 직접 넣으면 새로 생성되지 않을 수 있다.
- 이를 해결하려면 `ObjectFactory` 등 지연 조회 방식을 쓴다.
- 라이프사이클은 생성, 초기화, 종료 각 단계로 나뉜다.
- `BeanNameAware`는 이름, `ApplicationContextAware`는 컨테이너를 전달받는다.
- 외부 설정은 `@Value`와 `@ConfigurationProperties`로 읽는다.
