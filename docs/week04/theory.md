# Week 04 Theory

## 주제

4주차 강의자료를 기준으로 스프링의 DI를 이론적으로 정리한다.

## 목차

- [1. 4주차 큰 흐름](#1-4주차-큰-흐름)
- [2. Bean 설정 방법 3가지](#2-bean-설정-방법-3가지)
- [3. 스프링 컨테이너](#3-스프링-컨테이너)
- [4. DI가 필요한 이유](#4-di가-필요한-이유)
- [5. DI 방식 3가지](#5-di-방식-3가지)
- [6. 어노테이션 기반 DI](#6-어노테이션-기반-di)
- [7. Java Config 기반 DI](#7-java-config-기반-di)
- [8. Lombok과 생성자 주입](#8-lombok과-생성자-주입)
- [9. XML 기반 DI](#9-xml-기반-di)
- [10. 자동 의존 관계 설정과 설정 재사용](#10-자동-의존-관계-설정과-설정-재사용)
- [11. 컬렉션 타입 주입](#11-컬렉션-타입-주입)
- [12. ApplicationContext 사용 방식 비교](#12-applicationcontext-사용-방식-비교)
- [13. 시험 대비 핵심 정리](#13-시험-대비-핵심-정리)

## 1. 4주차 큰 흐름

4주차 PPT의 흐름은 단순히 어노테이션 몇 개를 외우는 것이 아니다.

핵심 질문은 이렇다.

- 객체를 스프링이 어떻게 관리하는가
- 객체 사이의 의존 관계를 어떻게 연결하는가
- XML, Java Config, 컴포넌트 스캔 방식이 어떻게 이어지는가
- 웹 프로젝트에서 이 객체들을 어떻게 확인하는가

한 줄로 요약하면:

> 4주차는 "Bean 등록"에서 끝나지 않고 "등록된 Bean을 주입하고 사용하는 것"까지 이해하는 주차다.

## 2. Bean 설정 방법 3가지

PPT에서는 Bean 설정 방법을 3가지로 구분했다.

### 2-1. XML 기반 설정

XML 파일의 `<bean>` 태그로 Bean을 등록하는 방식이다.

```xml
<bean id="xmlSms" class="Lect_B.week03.SmsSender" />
```

특징:

- 코드와 설정이 분리된다
- 설정을 외부 파일에서 한눈에 볼 수 있다
- 설정이 많아지면 XML이 길고 복잡해질 수 있다

### 2-2. Java Config 기반 설정

`@Configuration`, `@Bean`을 이용해 자바 코드로 Bean을 등록하는 방식이다.

```java
@Configuration
public class AppConfig {

    @Bean
    public SmsSender configSms() {
        return new SmsSender();
    }
}
```

특징:

- XML보다 간결하다
- 설정과 코드가 가까이 있어 읽기 쉽다
- 현재 실습 코드도 이 방식을 사용한다

### 2-3. 컴포넌트 기반 설정

`@Component`, `@Controller`, `@Service`, `@Repository`가 붙은 클래스를 자동으로 찾아 등록하는 방식이다.

```java
@Component
public class WorkUnit {
}
```

특징:

- 개발 생산성이 높다
- `@ComponentScan` 또는 부트의 컴포넌트 스캔이 필요하다

## 3. 스프링 컨테이너

PPT에서는 컨테이너를 Bean을 담고 관리하는 공간으로 설명했다.

주요 역할:

- Bean 생성
- Bean 보관
- 의존 관계 연결
- 생명주기 관리

대표 인터페이스:

- `BeanFactory`
- `ApplicationContext`
- `WebApplicationContext`

실습에서는 컨트롤러에서 `WebApplicationContext`를 주입받아 `getBean()`으로 Bean을 확인한다.

## 4. DI가 필요한 이유

객체가 다른 객체를 직접 `new`로 만들면 결합도가 높아진다.

예:

```java
public class Car {
    private Engine engine;

    public Car() {
        this.engine = new Engine();
    }
}
```

문제:

- 구현이 바뀌면 코드를 수정해야 한다
- 테스트가 어렵다
- 객체 관계가 복잡해질수록 관리가 힘들다

그래서 스프링은 필요한 객체를 외부에서 주입한다.

이것이 DI다.

## 5. DI 방식 3가지

PPT에서 정리한 DI 방식은 아래와 같다.

### 5-1. 필드 주입

```java
@Autowired
private SmsSender smsSender;
```

특징:

- 코드가 짧다
- 의존성이 필드 안에 숨어 있어 구조 파악이 어렵다

### 5-2. 생성자 주입

```java
@Autowired
public HardWorkUnit(SmsSender smsSender, WorkUnit workUnit) {
    this.smsSender = smsSender;
    this.workUnit = workUnit;
}
```

특징:

- 필수 의존성이 명확하다
- 실무에서 가장 권장된다

### 5-3. Setter 주입

```java
@Value("${message.greeting}")
public void setMsg(String msg) {
    this.msg = msg;
}
```

특징:

- 선택적인 값 주입에 적합하다
- 실습에서는 설정값 주입 예제로 사용한다

## 6. 어노테이션 기반 DI

PPT에서 가장 강조한 부분 중 하나다.

핵심 어노테이션:

- `@Component`
- `@Controller`
- `@Autowired`
- `@Qualifier`
- `@Value`

### `@Autowired`

기본적으로 타입 기준으로 Bean을 찾아 주입한다.

주의:

- 해당 타입 Bean이 없으면 예외
- 같은 타입 Bean이 여러 개 있어도 예외

### `@Qualifier`

같은 타입 Bean이 2개 이상일 때 이름으로 특정 Bean을 고른다.

```java
@Autowired
public HardWorkUnit(@Qualifier("configSms") SmsSender autoSms,
        @Qualifier("week04WorkUnit") WorkUnit workUnit) {
    this.autoSms = autoSms;
    this.workUnit = workUnit;
}
```

### `@Value`

프로퍼티 값을 주입한다.

```java
@Value("${message.greeting}")
public void setMsg(String msg) {
    this.msg = msg;
}
```

## 7. Java Config 기반 DI

PPT에서는 `@Configuration` 클래스와 `@Bean` 메서드로 설정하는 방식을 따로 다뤘다.

현재 실습 코드도 이 방식을 사용한다.

```java
@Configuration
public class AppConfig {

    @Bean
    public SmsSender configSms() {
        return new SmsSender();
    }

    @Bean
    public List<String> unit() {
        List<String> list = new ArrayList<>();
        list.add("문자열 1");
        list.add("문자열 2");
        return list;
    }
}
```

포인트:

- 메서드 이름이 기본 Bean 이름이 된다
- 반환 타입이 Bean 타입이 된다
- 단순 객체뿐 아니라 `List<String>` 같은 컬렉션도 Bean으로 등록할 수 있다

## 8. Lombok과 생성자 주입

PPT에서는 Lombok의 `@RequiredArgsConstructor`도 소개했다.

핵심:

- `final` 필드를 기반으로 생성자를 자동 생성
- 생성자 주입 코드가 짧아진다
- 스프링 4.3 이후에는 생성자가 하나면 `@Autowired` 생략 가능

현재 실습 코드에서는 두 가지 방식으로 Lombok을 사용한다.

### 8-1. 컴포넌트 스캔 + Lombok

```java
@Component
@Getter
@RequiredArgsConstructor
public class LombokWorkUnit {

    private final SmsSender configSms;
    private final WorkUnit week04WorkUnit;

    @Value("${message.greeting}")
    private String msg;
}
```

의미:

- `final` 필드를 기준으로 생성자가 자동 생성된다
- 스프링은 그 생성자를 이용해 Bean을 주입한다
- Lombok을 사용해 생성자 코드를 직접 쓰지 않아도 된다

### 8-2. XML + Lombok 생성자 주입

강의자료에서 말한 흐름대로 XML에서 인젝션 정보를 쓰고, 클래스는 Lombok으로 생성자를 준비하는 구조도 가능하다.

```java
@Getter
@RequiredArgsConstructor
public class LombokXmlService {

    private final SmsSender smsSender;
    private final long periodTime;
}
```

```xml
<bean id="xmlLombokService" class="Lect_B.week04.LombokXmlService">
    <constructor-arg>
        <ref bean="xmlSms" />
    </constructor-arg>
    <constructor-arg>
        <value type="long">30000</value>
    </constructor-arg>
</bean>
```

의미:

- Lombok이 `SmsSender`, `long` 파라미터 생성자를 만든다
- XML이 그 생성자에 들어갈 값을 제공한다
- `<ref bean="xmlSms" />`는 Bean 객체 주입
- `<value type="long">30000</value>`는 기본값 주입

## 9. XML 기반 DI

PPT에서는 XML에서도 생성자 주입과 Setter 주입을 할 수 있다고 설명했다.

### 생성자 주입

```xml
<bean name="service" class="com.Lect.Service.LombokService">
    <constructor-arg>
        <ref bean="xmlSms" />
    </constructor-arg>
</bean>
```

### Setter 주입

```xml
<bean name="service" class="com.Lect.Service.LombokService">
    <property name="msg">
        <value>롬복을 활용하는 클래스를 사용!!</value>
    </property>
</bean>
```

실습에서는 XML 생성자 주입을 아래처럼 구체적으로 확인했다.

```xml
<bean id="xmlSms" class="Lect_B.week04.SmsSender">
    <constructor-arg value="XML 주입용 SMS 발신기" />
</bean>

<bean id="xmlLombokService" class="Lect_B.week04.LombokXmlService">
    <constructor-arg>
        <ref bean="xmlSms" />
    </constructor-arg>
    <constructor-arg>
        <value type="long">30000</value>
    </constructor-arg>
</bean>
```

이 구조는 강의자료의 다음 두 포인트를 그대로 보여준다.

- 객체 타입은 `<ref>`로 주입
- 기본 타입 값은 `<value type="long">`처럼 타입을 명시해 주입

즉 XML이든 Java Config든 본질은 같다.

- 객체를 만들고
- 의존성을 연결하고
- 컨테이너가 관리한다

## 10. 자동 의존 관계 설정과 설정 재사용

PPT에는 XML의 자동 연결 방식도 나왔다.

- `byName`
- `byType`
- `constructor`

또 부모 Bean을 정의해 공통 설정을 재사용하는 방식도 소개됐다.

핵심 메시지:

- 설정이 많아질수록 중복을 줄이는 전략이 필요하다
- 스프링은 자동 연결과 부모 Bean 재사용 기능을 제공한다

## 11. 컬렉션 타입 주입

4주차 PPT의 후반부에서는 컬렉션 주입도 다뤘다.

- `List`
- `Map`
- `Set`
- `Properties`

실습 코드의 `unit()` Bean은 이 개념의 가장 단순한 버전이다.

```java
@Bean
public List<String> unit() {
    List<String> list = new ArrayList<>();
    list.add("문자열 1");
    list.add("문자열 2");
    return list;
}
```

즉 컬렉션도 스프링 컨테이너가 Bean으로 관리할 수 있다.

## 12. ApplicationContext 사용 방식 비교

PPT 마지막 부분에서는 두 방식을 비교했다.

### `@Autowired`로 주입받은 컨테이너

- 스프링 부트가 만든 표준 컨테이너
- 애플리케이션 전체에서 공유
- 일반적인 애플리케이션 개발에서 권장

### `new AnnotationConfigApplicationContext(...)`

- 개발자가 별도 컨테이너를 직접 생성
- 독립 테스트나 특수 목적에 유용
- 기존 컨텍스트와 별개로 동작할 수 있다

현재 실습은 `WebApplicationContext`를 주입받는 표준 방식에 가깝다.

```java
@Autowired
private WebApplicationContext context;
```

그리고 `contextDI` 실습에서는 이 컨테이너에서 Bean을 직접 꺼내 화면으로 넘긴다.

```java
HardWorkUnit work = context.getBean("hardWorkUnit", HardWorkUnit.class);
```

즉 이론에서 말한 "주입된 컨테이너를 활용하는 방식"을 현재 프로젝트에서 바로 확인할 수 있다.

## 13. 시험 대비 핵심 정리

- Bean 설정 방식은 XML, Java Config, 컴포넌트 스캔 3가지로 정리한다
- 스프링 컨테이너는 Bean 생성, 관리, 의존 관계 연결을 담당한다
- DI 방식은 필드 주입, 생성자 주입, Setter 주입 3가지다
- `@Autowired`는 타입 기준 주입이다
- 같은 타입 Bean이 여러 개면 `@Qualifier`가 필요하다
- `@Value`는 프로퍼티 값을 주입할 때 사용한다
- Java Config에서는 `@Bean` 메서드 이름이 기본 Bean 이름이다
- XML 생성자 주입에서는 `<ref>`로 Bean을, `<value type=\"long\">`로 기본값을 넣을 수 있다
- Lombok은 생성자 주입 코드를 줄여 주며 XML 주입과도 함께 사용할 수 있다
- XML에서도 생성자 주입, Setter 주입, 자동 연결, 부모 Bean 재사용, 컬렉션 주입이 가능하다
- `ApplicationContext`는 컨테이너의 대표 인터페이스다
