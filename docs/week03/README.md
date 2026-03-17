# Week 03

> 3주차 핵심:  
> 스프링 컨테이너가 관리하는 Bean 객체를 XML 방식과 Java 설정 방식으로 만들고,  
> 컨트롤러에서 꺼내 `ModelAndView`로 뷰에 전달하는 실습

## 목차

- [1. 이번 주차 한눈에 보기](#1-이번-주차-한눈에-보기)
- [2. 왜 이런 실습을 하는가](#2-왜-이런-실습을-하는가)
- [3. 핵심 용어 정리](#3-핵심-용어-정리)
- [4. 프로젝트 파일 구조](#4-프로젝트-파일-구조)
- [5. 현재 실습 코드 정리](#5-현재-실습-코드-정리)
- [6. 요청 처리 흐름](#6-요청-처리-흐름)
- [7. ModelAndView 이해하기](#7-modelandview-이해하기)
- [8. XML 방식과 Java 방식 비교](#8-xml-방식과-java-방식-비교)
- [9. 시험 대비 암기 포인트](#9-시험-대비-암기-포인트)
- [10. 자주 헷갈리는 질문](#10-자주-헷갈리는-질문)

## 1. 이번 주차 한눈에 보기

이번 주차에서 실제로 한 일은 아래와 같다.

1. `SmsSender` 클래스를 준비했다.
2. XML 파일에서 `xmlSms` Bean을 등록했다.
3. Java 설정 클래스에서 `configSms` Bean을 등록했다.
4. 컨트롤러에서 두 컨테이너를 초기화했다.
5. 컨테이너에서 Bean 객체를 꺼냈다.
6. `ModelAndView`에 객체를 담아 JSP로 전달했다.
7. JSP에서 전달받은 객체 값을 출력해 Bean 관리 여부를 확인했다.

## 2. 왜 이런 실습을 하는가

순수 자바에서는 보통 객체를 직접 생성한다.

```java
SmsSender smsSender = new SmsSender();
```

이 방식은 간단하지만 프로젝트가 커질수록 아래 문제가 생긴다.

- 객체를 어디서 만들었는지 찾기 어렵다
- 다른 객체로 교체하기 어렵다
- 객체 생성 규칙이 여러 곳에 흩어진다
- 테스트와 유지보수가 불편해진다

스프링은 이 문제를 해결하기 위해 **객체 생성과 관리**를 컨테이너에게 맡긴다.

즉, 개발자는:

- 어떤 객체를 만들지 정하고
- 어떤 이름으로 등록할지 정하고
- 필요할 때 그 객체를 꺼내서 사용한다

그래서 이번 실습의 본질은:

> "객체를 내가 직접 만들지 않고, 스프링이 관리하게 한 뒤 그 객체를 화면까지 전달해 본다"

이다.

## 3. 핵심 용어 정리

| 용어 | 뜻 | 쉽게 이해하기 |
|---|---|---|
| Spring Container | 객체를 생성하고 관리하는 공간 | 객체 창고 + 관리자 |
| Bean | 스프링 컨테이너가 관리하는 객체 | 컨테이너 안의 객체 |
| XML 설정 | XML 파일로 Bean 등록 | 설정을 문서처럼 작성 |
| Java 설정 | `@Configuration`, `@Bean`으로 Bean 등록 | 설정을 자바 코드로 작성 |
| Controller | 요청을 받아 처리하는 클래스 | 중간 처리 담당 |
| View | 사용자에게 보여줄 화면 | JSP 페이지 |
| Model | View에 넘길 데이터 | 화면용 데이터 |
| ModelAndView | View 이름 + Model 데이터를 함께 담는 객체 | 화면 이름과 전달값을 담는 상자 |
| `getBean()` | 컨테이너에서 Bean을 꺼내는 메서드 | 창고에서 객체 꺼내기 |
| `refresh()` | 컨테이너를 실제 초기화하는 메서드 | 설정 읽고 실제 동작 시작 |
| `register()` | Java 설정 클래스를 컨테이너에 등록 | 어떤 설정을 쓸지 알려줌 |
| classpath | 실행 시 읽을 수 있는 리소스 경로 | 스프링이 찾을 수 있는 경로 |

## 4. 프로젝트 파일 구조

이번 실습과 직접 관련 있는 파일은 아래와 같다.

| 경로 | 역할 |
|---|---|
| `src/main/java/Lect_B/week03/SmsSender.java` | Bean으로 사용할 일반 클래스 |
| `src/main/java/Lect_B/week03/AppConfig.java` | Java 설정 클래스 |
| `src/main/java/Lect_B/week03/ContextController.java` | 요청 처리 컨트롤러 |
| `src/main/resources/static/xml/Ex1.xml` | XML Bean 설정 파일 |
| `src/main/webapp/views/week03/beanView.jsp` | 결과를 출력하는 JSP |
| `src/main/webapp/index.jsp` | 실습 시작 링크가 있는 첫 화면 |

### 폴더 의미

| 폴더 | 의미 |
|---|---|
| `src/main/java` | 자바 코드 |
| `src/main/resources` | 설정 파일, 리소스 |
| `src/main/resources/static` | 정적 리소스용 폴더 |
| `src/main/webapp/views` | JSP 뷰 파일 |

### JSP 뷰 설정

현재 프로젝트는 아래 설정으로 JSP를 찾는다.

```properties
spring.mvc.view.prefix=/views/
spring.mvc.view.suffix=.jsp
```

즉,

```java
mav.setViewName("week03/beanView");
```

라고 쓰면 실제로 찾는 파일은:

```text
/views/week03/beanView.jsp
```

이다.

## 5. 현재 실습 코드 정리

### 5-1. XML 방식 Bean 등록

파일: `src/main/resources/static/xml/Ex1.xml`

```xml
<bean id="xmlSms" class="Lect_B.week03.SmsSender" />
```

의미:

- `id="xmlSms"`: Bean 이름
- `class="Lect_B.week03.SmsSender"`: 생성할 클래스

정리:

- XML 컨테이너는 `xmlSms`라는 이름으로 `SmsSender` 객체를 관리한다.

### 5-2. Java 방식 Bean 등록

파일: `src/main/java/Lect_B/week03/AppConfig.java`

```java
@Configuration
public class AppConfig {

    @Bean
    public SmsSender configSms() {
        return new SmsSender("학교실습용 SMS 발신기");
    }
}
```

의미:

- `@Configuration`: 이 클래스는 설정 클래스다
- `@Bean`: 메서드가 반환하는 객체를 Bean으로 등록한다
- 메서드 이름 `configSms()`가 Bean 이름이 된다

정리:

- Java 설정 컨테이너는 `configSms`라는 이름으로 `SmsSender` 객체를 관리한다.

### 5-3. Bean으로 사용할 클래스

파일: `src/main/java/Lect_B/week03/SmsSender.java`

이 클래스는 실제로 XML과 Java 설정 양쪽에서 모두 사용된다.

즉:

- XML 컨테이너도 `SmsSender`를 만들고
- Java 설정 컨테이너도 `SmsSender`를 만든다

차이는 **만드는 방법과 이름**이다.

## 6. 요청 처리 흐름

### 전체 흐름

```text
index.jsp
  -> /createBean 요청
  -> ContextController.beanTest()
  -> XML 컨테이너에서 xmlSms 획득
  -> Java 컨테이너에서 configSms 획득
  -> ModelAndView에 객체 저장
  -> beanView.jsp로 이동
  -> JSP에서 객체 값 출력
```

### 6-1. 시작 링크

파일: `src/main/webapp/index.jsp`

```jsp
<a href="/createBean">빈객체 생성 예제</a>
```

의미:

- 사용자가 링크를 누르면 `GET /createBean` 요청이 발생한다.

### 6-2. 컨트롤러 생성자에서 컨테이너 준비

파일: `src/main/java/Lect_B/week03/ContextController.java`

```java
xmlContext = new XmlWebApplicationContext();
xmlContext.setConfigLocation("classpath:static/xml/Ex1.xml");
xmlContext.refresh();

configContext = new AnnotationConfigWebApplicationContext();
configContext.register(AppConfig.class);
configContext.refresh();

mav = new ModelAndView();
```

의미:

- `XmlWebApplicationContext` 생성: XML 기반 컨테이너 준비
- `setConfigLocation(...)`: XML 설정 파일 위치 지정
- `refresh()`: XML 컨테이너 실제 초기화
- `AnnotationConfigWebApplicationContext` 생성: Java 설정 기반 컨테이너 준비
- `register(AppConfig.class)`: Java 설정 클래스 등록
- `refresh()`: Java 컨테이너 실제 초기화
- `new ModelAndView()`: 나중에 뷰 이름과 데이터를 담을 객체 준비

### 6-3. 컨트롤러에서 Bean 객체 꺼내기

```java
SmsSender xmlSms = (SmsSender) xmlContext.getBean("xmlSms");
SmsSender configSms = (SmsSender) configContext.getBean("configSms");
```

왜 형변환을 하는가:

- `getBean("이름")` 형태는 기본적으로 `Object`를 반환한다
- 그래서 `SmsSender`로 다시 형변환해야 한다

쉽게 말하면:

- 컨테이너에서 객체를 꺼냈는데 일단 `Object`로 나오므로
- "이건 `SmsSender` 맞아"라고 다시 알려주는 과정이다

### 6-4. `ModelAndView`에 데이터 담기

```java
mav.setViewName("week03/beanView");
mav.addObject("xmlSms", xmlSms);
mav.addObject("configSms", configSms);
mav.addObject("xmlSmsType", xmlSms.getClass().getName());
mav.addObject("configSmsType", configSms.getClass().getName());
return mav;
```

의미:

- `setViewName("week03/beanView")`
  - 이동할 JSP 이름 지정
- `addObject("xmlSms", xmlSms)`
  - `xmlSms` 객체를 JSP에 전달
- `addObject("configSms", configSms)`
  - `configSms` 객체를 JSP에 전달
- `addObject("xmlSmsType", ...)`
  - 문자열 데이터도 같이 전달 가능

## 7. ModelAndView 이해하기

### 7-1. `ModelAndView`는 무엇인가

`ModelAndView`는 이름 그대로 두 부분으로 나뉜다.

- `Model`: 뷰에 전달할 데이터
- `View`: 이동할 화면 이름

즉,

> `ModelAndView`는 "어느 뷰로 갈지"와 "그 뷰에 무엇을 넘길지"를 함께 담는 객체이다.

### 7-2. 왜 필요한가

컨트롤러는 단순히 계산만 하는 곳이 아니라, 처리 결과를 화면에 보여줘야 한다.

그때 필요한 것은 두 가지다.

1. 어떤 JSP를 보여줄지
2. 그 JSP에 어떤 데이터를 넘길지

이 두 가지를 한 번에 담는 것이 `ModelAndView`다.

### 7-3. `addObject()`는 무엇인가

`addObject()`는 모델에 데이터를 넣는 메서드다.

예:

```java
mav.addObject("xmlSms", xmlSms);
```

의미:

- JSP는 `xmlSms`라는 이름으로 이 객체를 받는다.

그래서 JSP에서는:

```jsp
${xmlSms.senderName}
```

처럼 쓸 수 있다.

## 8. XML 방식과 Java 방식 비교

| 구분 | XML 방식 | Java 방식 |
|---|---|---|
| 설정 위치 | XML 파일 | 자바 클래스 |
| 이번 실습 파일 | `Ex1.xml` | `AppConfig.java` |
| Bean 이름 | `xmlSms` | `configSms` |
| Bean 타입 | `Lect_B.week03.SmsSender` | `Lect_B.week03.SmsSender` |
| 장점 | 설정과 코드를 분리해서 보기 좋음 | 타입 체크와 IDE 지원이 좋음 |
| 초보자 관점 | 구조가 낯설 수 있음 | 코드 흐름을 따라가기 쉬움 |

핵심:

- **만드는 대상은 같은 `SmsSender`**
- **등록하는 방식만 다르다**

## 9. 시험 대비 암기 포인트

### 꼭 외울 문장

> Bean은 스프링 컨테이너가 관리하는 객체이다.

> XML 방식은 XML 파일에서 `<bean>` 태그로 Bean을 등록한다.

> Java 방식은 `@Configuration` 클래스 안에서 `@Bean` 메서드로 Bean을 등록한다.

> `ModelAndView`는 뷰 이름과 전달할 데이터를 함께 담는 객체이다.

> `addObject()`는 뷰로 전달할 데이터를 모델에 넣는 메서드이다.

### 한 줄 암기

- `getBean()` = 컨테이너에서 객체 꺼내기
- `setViewName()` = 어느 JSP로 갈지 정하기
- `addObject()` = JSP에 데이터 넘기기
- `refresh()` = 컨테이너 실제 초기화
- `register()` = Java 설정 클래스 등록

## 10. 자주 헷갈리는 질문

### Q1. `xmlSms`와 `configSms`는 무엇이 다른가

둘 다 `SmsSender` 객체다.  
다만 하나는 XML에서 등록했고, 다른 하나는 Java 설정에서 등록했다.

### Q2. 왜 `getBean()` 뒤에 형변환을 하는가

`getBean("이름")`의 기본 반환형이 `Object`이기 때문이다.

### Q3. 왜 JSP에서 `${xmlSms.senderName}` 으로 쓸 수 있는가

컨트롤러에서:

```java
mav.addObject("xmlSms", xmlSms);
```

로 넘겼기 때문이다.

### Q4. 왜 `beanView.jsp` 파일 이름을 코드에 직접 안 쓰는가

프로젝트에 뷰 prefix, suffix 설정이 있기 때문이다.

즉:

```java
mav.setViewName("week03/beanView");
```

라고만 쓰면 스프링이 자동으로:

```text
/views/week03/beanView.jsp
```

를 찾는다.

### Q5. 이번 실습 결과로 무엇을 확인하는가

- XML 컨테이너도 Bean을 관리한다
- Java 설정 컨테이너도 Bean을 관리한다
- 컨트롤러는 그 Bean을 꺼낼 수 있다
- 꺼낸 객체를 뷰에 전달할 수 있다
- JSP는 그 객체의 값을 출력할 수 있다

## 마무리 정리

이번 3주차 실습은 아래 한 문장으로 정리할 수 있다.

> 스프링 컨테이너에 Bean 객체를 등록하고, 컨트롤러에서 그 객체를 꺼내 `ModelAndView`로 JSP에 전달하여 화면에 출력하는 실습
