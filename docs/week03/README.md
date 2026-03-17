# Week 03

## 주제

스프링 컨테이너가 관리하는 Bean 객체를 XML 방식과 Java 설정 방식으로 만들고, 컨트롤러에서 꺼내서 뷰로 전달하는 실습.

## 이번 주차 실습 목표

- XML 설정 파일로 Bean 등록하기
- Java 설정 클래스로 Bean 등록하기
- 스프링 컨테이너에서 Bean 객체 획득하기
- 컨트롤러에서 `ModelAndView`로 객체를 JSP에 전달하기
- JSP에서 전달받은 객체의 값을 출력해 Bean 관리 여부 확인하기

## 현재 구현 파일

- `src/main/java/Lect_B/week03/SmsSender.java`
- `src/main/java/Lect_B/week03/AppConfig.java`
- `src/main/java/Lect_B/week03/ContextController.java`
- `src/main/java/Lect_B/week03/JavaConfigMain.java`
- `src/main/java/Lect_B/week03/XmlConfigMain.java`
- `src/main/resources/static/xml/Ex1.xml`
- `src/main/webapp/views/week03/beanView.jsp`
- `src/main/webapp/index.jsp`

## 스프링 폴더 구조 이해

- `src/main/java`
  - 자바 클래스 위치
  - 컨트롤러, 설정 클래스, 일반 클래스가 들어간다
- `src/main/resources`
  - 클래스패스에 올라가는 설정 파일 위치
  - `application.properties` 같은 파일이 들어간다
- `src/main/resources/static`
  - 원래는 정적 리소스 위치
  - 이번 실습에서는 교수님 진행에 맞춰 `static/xml/Ex1.xml`을 사용했다
- `src/main/webapp/views`
  - JSP 파일 위치
  - `beanView.jsp` 같은 화면 파일이 들어간다

현재 프로젝트의 JSP 설정:

- `spring.mvc.view.prefix=/views/`
- `spring.mvc.view.suffix=.jsp`

의미:

- 컨트롤러에서 `mav.setViewName("week03/beanView")` 라고 쓰면
- 실제로는 `/views/week03/beanView.jsp` 파일을 찾는다

## 핵심 개념

### 1. 스프링 컨테이너

- 객체를 생성하고 관리하는 역할을 한다
- 스프링이 관리하는 객체를 Bean이라고 부른다

### 2. Bean

- 컨테이너 안에 등록된 객체
- 필요할 때 `getBean()`으로 꺼내서 사용한다

### 3. XML 설정 방식

- XML 파일 안에 `<bean>` 태그로 객체를 등록한다
- 이번 실습의 XML 파일 경로는 `src/main/resources/static/xml/Ex1.xml`

현재 등록 내용:

```xml
<bean id="xmlSms" class="Lect_B.week03.SmsSender" />
```

의미:

- 빈 이름: `xmlSms`
- 빈 타입: `Lect_B.week03.SmsSender`

### 4. Java 설정 방식

- `@Configuration` 클래스 안에 `@Bean` 메서드로 객체를 등록한다

현재 등록 내용:

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

- 설정 클래스: `AppConfig`
- 빈 이름: `configSms`
- 빈 타입: `Lect_B.week03.SmsSender`

### 5. `ModelAndView`

- 컨트롤러가 뷰 이름과 전달할 데이터를 함께 담아 반환할 때 사용하는 객체
- `View`는 이동할 JSP 이름
- `Model`은 JSP에 전달할 데이터

쉽게 말하면:

- `setViewName()`은 어느 페이지로 갈지 정하는 것
- `addObject()`는 그 페이지로 어떤 데이터를 넘길지 정하는 것

## 현재 실습 코드 흐름

### 1. 인덱스 페이지에서 요청 시작

`index.jsp`:

```jsp
<a href="/createBean">빈객체 생성 예제</a>
```

의미:

- 브라우저가 `GET /createBean` 요청을 보낸다

### 2. 컨트롤러에서 컨테이너 준비

`ContextController` 생성자:

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

- XML 컨테이너 생성 후 XML 설정 파일 읽기
- Java 설정 컨테이너 생성 후 `AppConfig` 등록
- `refresh()`로 컨테이너 실제 초기화
- `ModelAndView` 객체 준비

### 3. 컨트롤러에서 Bean 객체 획득

`beanTest()` 안의 핵심 코드:

```java
SmsSender xmlSms = (SmsSender) xmlContext.getBean("xmlSms");
SmsSender configSms = (SmsSender) configContext.getBean("configSms");
```

의미:

- XML 컨테이너에서 `xmlSms` Bean 객체를 꺼낸다
- Java 설정 컨테이너에서 `configSms` Bean 객체를 꺼낸다
- `getBean()`의 기본 반환형은 `Object`라서 형변환이 필요하다

### 4. 뷰 이름과 객체 전달

```java
mav.setViewName("week03/beanView");
mav.addObject("xmlSms", xmlSms);
mav.addObject("configSms", configSms);
mav.addObject("xmlSmsType", xmlSms.getClass().getName());
mav.addObject("configSmsType", configSms.getClass().getName());
return mav;
```

의미:

- 이동할 JSP는 `beanView.jsp`
- `xmlSms`, `configSms` 객체를 JSP로 전달
- 타입 문자열도 같이 전달
- JSP는 전달받은 객체를 `${xmlSms...}` 형태로 사용 가능

### 5. JSP에서 객체 출력

`beanView.jsp`:

```jsp
<p>빈 타입: ${xmlSmsType}</p>
<p>객체 내부 값: ${xmlSms.senderName}</p>
```

의미:

- 컨트롤러가 넘긴 객체를 JSP에서 꺼내 출력한다
- 화면에 값이 보이면 컨테이너가 Bean을 제대로 관리하고 있음을 확인할 수 있다

## 현재 실습 결과

`/createBean` 요청 시 확인되는 내용:

- XML 컨테이너 Bean 이름: `xmlSms`
- XML 컨테이너 Bean 타입: `Lect_B.week03.SmsSender`
- XML 컨테이너 객체 내부 값: `기본 SMS 발신기`
- Java 설정 컨테이너 Bean 이름: `configSms`
- Java 설정 컨테이너 Bean 타입: `Lect_B.week03.SmsSender`
- Java 설정 컨테이너 객체 내부 값: `학교실습용 SMS 발신기`

## 이번 실습에서 이해해야 할 핵심

- 스프링 컨테이너는 객체를 대신 생성하고 관리한다
- XML 방식과 Java 방식 둘 다 Bean 등록 방법이다
- 컨트롤러는 컨테이너에서 객체를 꺼낼 수 있다
- 컨트롤러는 `ModelAndView`를 사용해 객체를 뷰로 넘길 수 있다
- JSP는 전달받은 객체의 값을 EL 표현식으로 출력할 수 있다

## 내가 헷갈렸던 부분 정리

### `ModelAndView`가 왜 필요한가

- 컨트롤러는 단순히 계산만 하는 곳이 아니라 화면으로 데이터를 넘겨야 한다
- 이때 뷰 이름과 데이터를 한 번에 담기 위해 `ModelAndView`를 사용한다

### `addObject()`는 무엇인가

- JSP에 전달할 데이터를 모델에 넣는 코드다
- 객체도 넣을 수 있고 문자열도 넣을 수 있다

예:

```java
mav.addObject("xmlSms", xmlSms);
```

의미:

- `xmlSms`라는 이름으로 `xmlSms` 객체를 JSP에 전달한다

### 왜 JSP에서는 `${xmlSms.senderName}` 으로 쓰는가

- `addObject("xmlSms", xmlSms)` 로 넘겼기 때문이다
- JSP에서는 `xmlSms`라는 이름으로 객체를 받고 그 안의 `senderName` 값을 읽는다

## 실행 확인 방법

1. Spring Boot 실행
2. 브라우저에서 `http://localhost:8080/createBean` 접속
3. XML 방식과 Java 방식의 Bean 정보가 화면에 출력되는지 확인

## 기억할 문장

"Bean은 스프링 컨테이너가 관리하는 객체다."

"`ModelAndView`는 컨트롤러가 뷰 이름과 전달할 데이터를 함께 담는 객체다."

"`addObject()`는 뷰 페이지에 모델 객체를 전달하는 메서드다."
