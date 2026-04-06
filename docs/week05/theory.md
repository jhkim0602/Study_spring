# Week 05 Theory

## 주제

5주차 PPT `빈 객체 관리`를 기준으로 핵심 개념을 정리한다.

## 목차

- [1. 5주차 큰 흐름](#1-5주차-큰-흐름)
- [2. 빈 스코프](#2-빈-스코프)
- [3. 서로 다른 스코프 빈의 의존 문제](#3-서로-다른-스코프-빈의-의존-문제)
- [4. 라이프사이클 메서드](#4-라이프사이클-메서드)
- [5. Aware 인터페이스](#5-aware-인터페이스)
- [6. 외부 설정 프로퍼티](#6-외부-설정-프로퍼티)
- [7. 슬라이드 도식 해석](#7-슬라이드-도식-해석)
- [8. 시험 대비 핵심 정리](#8-시험-대비-핵심-정리)

## 1. 5주차 큰 흐름

5주차 PPT는 단순히 빈을 등록하는 법이 아니라  
"스프링이 빈을 얼마나 오래, 어떤 범위에서, 어떤 순서로 관리하는가"를 다룬다.

핵심 질문은 아래와 같다.

- 빈은 한 번만 만들어지는가, 매번 새로 만들어지는가
- 웹 요청이나 세션처럼 사용자 맥락에 따라 빈 범위가 달라지는가
- 빈이 생성되고 종료될 때 실행되는 메서드는 무엇인가
- 빈이 자기 이름이나 컨테이너를 직접 알아야 할 때는 어떻게 하는가
- 설정값을 코드 밖으로 빼면 어떻게 주입하는가

## 2. 빈 스코프

PPT 2~6장은 스코프를 설명한다.

### 2-1. singleton

- 기본 스코프
- 스프링 컨테이너 안에 해당 빈 인스턴스가 하나만 존재
- 여러 요청과 여러 객체가 같은 인스턴스를 공유

적합한 경우:

- 공통 서비스
- 유틸리티성 작업
- 상태를 거의 가지지 않는 서비스 객체

### 2-2. prototype

- `getBean()` 할 때마다 새 객체 생성
- 매번 초기 상태가 필요한 객체에 적합

적합한 경우:

- 작업 단위 객체
- 임시 계산 객체
- 요청 시마다 독립 상태가 필요한 객체

### 2-3. request

- HTTP 요청 하나 동안만 살아 있는 빈
- 같은 요청 안에서는 재사용되지만 다음 요청에서는 새로 생성

적합한 경우:

- 요청 단위 로깅
- 요청 추적 ID
- 특정 요청 중간 데이터 저장

### 2-4. session

- 사용자 세션마다 하나씩 유지되는 빈
- 같은 브라우저 세션 안에서는 계속 재사용

적합한 경우:

- 로그인 상태
- 장바구니
- 세션별 사용자 데이터

## 3. 서로 다른 스코프 빈의 의존 문제

PPT 7~8장의 핵심이다.

### 문제 상황

singleton 빈이 prototype 빈을 필드나 생성자로 직접 주입받으면,  
singleton이 생성되는 시점에 prototype도 딱 한 번 주입된다.

즉 prototype인데도 결과적으로 계속 같은 객체를 쓰게 된다.

```java
@Component
public class ScopeSingletonClient {
    private final WorkUnit workUnit;
}
```

이 경우 `workUnit`은 prototype 선언이 되어 있어도  
`ScopeSingletonClient` 안에서는 계속 같은 인스턴스다.

### 해결 방법

PPT에서는 `ObjectFactory` 사용을 제시한다.

```java
@Component
public class ScopeFactoryClient {
    private final ObjectFactory<WorkUnit> workUnitFactory;
}
```

필요할 때마다:

```java
workUnitFactory.getObject()
```

를 호출하면 새 prototype 빈을 받을 수 있다.

## 4. 라이프사이클 메서드

PPT 9~10장은 빈의 생성 이후와 종료 직전 동작을 다룬다.

### 4-1. custom init-method / destroy-method

`@Bean` 설정에서 직접 메서드 이름을 지정한다.

```java
@Bean(initMethod = "customInit", destroyMethod = "customDestroy")
```

장점:

- 초기화와 종료 메서드를 명시적으로 지정할 수 있다

### 4-2. `@PostConstruct`, `@PreDestroy`

- 표준 어노테이션
- 빈 초기화 직후, 종료 직전에 실행된다

### 4-3. `InitializingBean`, `DisposableBean`

인터페이스 메서드를 구현하는 방식이다.

- `afterPropertiesSet()`
- `destroy()`

주로 스프링 라이프사이클과 맞물린 초기화/정리 작업을 코드로 명확히 드러내고 싶을 때 사용한다.

## 5. Aware 인터페이스

PPT 11~12장은 빈이 컨테이너 정보를 직접 알아야 하는 경우를 다룬다.

### 5-1. `BeanNameAware`

- 스프링이 빈 이름을 전달해 준다
- 빈이 자기 이름을 알아야 하는 상황에서 사용

예:

- 로그에 빈 이름 포함
- 동일 클래스의 여러 빈을 이름으로 구분

### 5-2. `ApplicationContextAware`

- 스프링 컨테이너 자체를 전달받는다
- 컨테이너에서 다른 빈을 직접 조회하거나 메타정보를 확인할 때 사용

주의:

- 너무 자주 쓰면 DI 장점을 해칠 수 있다
- 꼭 컨테이너 자체가 필요한 경우에만 제한적으로 사용하는 것이 좋다

## 6. 외부 설정 프로퍼티

PPT 13~14장의 핵심이다.

### 6-1. `@PropertySource`

- 외부 프로퍼티 파일을 명시적으로 로드할 때 사용

### 6-2. `@Value`

- 단일 프로퍼티 값을 바로 필드나 setter에 주입

적합한 경우:

- 서버 주소
- 포트
- 간단한 문자열, 숫자 값

### 6-3. `@ConfigurationProperties`

- 여러 개의 설정을 하나의 객체 구조로 바인딩
- prefix 아래의 하위 키를 필드에 자동 매핑

적합한 경우:

- 메일 설정
- 데이터소스 설정
- API 클라이언트 설정처럼 구조가 있는 값 묶음

## 7. 슬라이드 도식 해석

PPT 안의 이미지도 개념 전달에 중요했다.

### singleton 도식

- 여러 클라이언트가 `memberService`를 요청해도
- 컨테이너 안의 같은 `memberService x01`을 돌려준다

즉 "여러 요청, 하나의 인스턴스"다.

### prototype 도식

- 클라이언트 A, B, C가 같은 이름의 빈을 요청해도
- `prototypeBean x01`, `x02`, `x03`처럼 매번 새 객체가 생긴다

즉 "요청마다 새 인스턴스"다.

### request 도식

- 하나의 컨트롤러/서비스 흐름 안에서도
- 사용자 A 요청에는 A 전용 request 빈
- 사용자 B 요청에는 B 전용 request 빈

이렇게 요청별 격리가 이루어진다.

### 서로 다른 스코프 의존 도식

- singleton이 prototype을 직접 받아버리면
- 그림상으로는 prototype이라도 실제 사용에서는 같은 객체가 재사용된다

그래서 `ObjectFactory`로 늦게 꺼내는 방식이 필요하다.

## 8. 시험 대비 핵심 정리

- singleton은 기본 스코프이며 컨테이너당 하나다.
- prototype은 요청할 때마다 새 객체를 만든다.
- request는 HTTP 요청마다, session은 사용자 세션마다 유지된다.
- singleton이 prototype을 직접 주입받으면 prototype이 사실상 고정된다.
- 이를 해결하려면 `ObjectFactory` 같은 지연 조회 방식이 필요하다.
- 라이프사이클 훅은 `@PostConstruct`, `@PreDestroy`, 인터페이스 방식, custom method 방식이 있다.
- `BeanNameAware`, `ApplicationContextAware`는 컨테이너 메타정보가 필요할 때 사용한다.
- 단일 값은 `@Value`, 구조화된 설정 묶음은 `@ConfigurationProperties`가 적합하다.
