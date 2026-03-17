# Week 03 Practice

## 주제

스프링 컨테이너가 관리하는 Bean 객체를 XML 방식과 Java 설정 방식으로 만들고, 컨트롤러에서 꺼내서 뷰로 전달하는 실습.

## 실습 목표

- XML 설정 파일로 Bean 등록하기
- Java 설정 클래스로 Bean 등록하기
- 스프링 컨테이너에서 Bean 객체 획득하기
- 컨트롤러에서 `ModelAndView`로 객체를 JSP에 전달하기
- JSP에서 전달받은 객체의 값을 출력해 Bean 관리 여부 확인하기

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/Lect_B/week03/SmsSender.java` | Bean으로 사용할 클래스 |
| `src/main/java/Lect_B/week03/AppConfig.java` | Java 설정 클래스 |
| `src/main/java/Lect_B/week03/ContextController.java` | 요청 처리 컨트롤러 |
| `src/main/java/Lect_B/week03/WorkUnit.java` | `@Component` 실습용 Bean 클래스 |
| `src/main/java/Lect_B/week03/HardWorkUnit.java` | `@Component`와 생성자 기반 DI 실습용 클래스 |
| `src/main/resources/static/xml/Ex1.xml` | XML Bean 설정 파일 |
| `src/main/webapp/views/week03/beanView.jsp` | 결과를 출력하는 JSP |
| `src/main/webapp/index.jsp` | 실습 시작 링크가 있는 첫 화면 |

## 실습 코드 핵심

### 1. XML 방식 Bean 등록

```xml
<bean id="xmlSms" class="Lect_B.week03.SmsSender" />
```

의미:

- Bean 이름: `xmlSms`
- Bean 타입: `Lect_B.week03.SmsSender`

### 2. Java 방식 Bean 등록

```java
@Configuration
public class AppConfig {

    @Bean
    public SmsSender configSms() {
        return new SmsSender("학교실습용 SMS 발신기");
    }

    @Bean
    public SmsSender autoSms() {
        return new SmsSender("자동 주입용 SMS 발신기");
    }
}
```

의미:

- 설정 클래스: `AppConfig`
- Bean 이름: `configSms`, `autoSms`
- Bean 타입: 둘 다 `Lect_B.week03.SmsSender`

### 3. 컨트롤러에서 컨테이너 준비

```java
xmlContext = new XmlWebApplicationContext();
xmlContext.setConfigLocation("classpath:static/xml/Ex1.xml");
xmlContext.refresh();

configContext = new AnnotationConfigWebApplicationContext();
configContext.register(AppConfig.class);
configContext.refresh();
```

의미:

- XML 컨테이너 초기화
- Java 설정 컨테이너 초기화

### 4. Bean 객체 꺼내기

```java
SmsSender xmlSms = (SmsSender) xmlContext.getBean("xmlSms");
SmsSender configSms = (SmsSender) configContext.getBean("configSms");
```

의미:

- 컨테이너에서 Bean 객체를 꺼낸다
- `getBean()` 반환형이 `Object`라서 형변환이 필요하다

### 5. `ModelAndView`로 뷰에 객체 전달

```java
mav.setViewName("week03/beanView");
mav.addObject("xmlSms", xmlSms);
mav.addObject("configSms", configSms);
mav.addObject("xmlSmsType", xmlSms.getClass().getName());
mav.addObject("configSmsType", configSms.getClass().getName());
return mav;
```

의미:

- 이동할 JSP를 지정한다
- JSP에서 사용할 객체와 문자열 데이터를 전달한다

## 요청 처리 흐름

```text
index.jsp
  -> /createBean 요청
  -> ContextController.beanTest()
  -> xmlSms, configSms 획득
  -> ModelAndView에 저장
  -> beanView.jsp 이동
  -> JSP에서 객체 값 출력
```

## 결과 확인

`/createBean` 요청 시 아래 내용을 확인할 수 있다.

- XML 컨테이너 Bean 이름: `xmlSms`
- XML 컨테이너 Bean 타입: `Lect_B.week03.SmsSender`
- XML 컨테이너 객체 내부 값: `기본 SMS 발신기`
- Java 설정 Bean 이름: `configSms`
- Java 설정 Bean 타입: `Lect_B.week03.SmsSender`
- Java 설정 객체 내부 값: `학교실습용 SMS 발신기`

## 실습에서 기억할 점

- XML 방식과 Java 방식은 Bean 등록 방식만 다르다
- 두 방식 모두 결국 스프링 컨테이너가 객체를 관리한다
- 컨트롤러는 `ModelAndView`를 통해 객체를 JSP로 넘길 수 있다

## 추가 실습: `@Component`와 생성자/Getter 만들기

### 1. `WorkUnit` Bean 만들기

파일: `src/main/java/Lect_B/week03/WorkUnit.java`

```java
@Component
public class WorkUnit {
}
```

의미:

- `@Component`가 붙으면 스프링이 이 클래스를 Bean으로 등록한다
- 기본 빈 이름은 클래스명 `WorkUnit`의 첫 글자를 소문자로 바꾼 `workUnit`이다

### 2. `HardWorkUnit` Bean 만들기

파일: `src/main/java/Lect_B/week03/HardWorkUnit.java`

현재 코드:

```java
@Component
public class HardWorkUnit {

    private SmsSender autoSms;
    private WorkUnit workUnit;
    private String msg;

    public HardWorkUnit(WorkUnit workUnit) {
        this.workUnit = workUnit;
    }

    public SmsSender getAutoSms() {
        return autoSms;
    }

    public WorkUnit getWorkUnit() {
        return workUnit;
    }

    public String getMsg() {
        return msg;
    }
}
```

의미:

- `HardWorkUnit`도 스프링 Bean이다
- 생성자의 `WorkUnit workUnit` 매개변수는 나중에 생성자 주입 DI와 연결되는 핵심이다
- getter 메소드는 필드 값을 외부에서 읽을 수 있게 해준다

### 3. Eclipse에서 생성자 만들기

#### 순서

1. `HardWorkUnit.java` 파일을 연다
2. 클래스 본문 안에서 마우스 오른쪽 클릭
3. `Source`
4. `Generate Constructor using Fields...`
5. 목록에서 `workUnit`만 선택
6. `Generate` 클릭

생성 결과:

```java
public HardWorkUnit(WorkUnit workUnit) {
    this.workUnit = workUnit;
}
```

#### 화면 설명

- 첫 번째 화면은 `Source` 메뉴에서 `Generate Constructor using Fields...` 를 고르는 단계다
- 두 번째 화면은 생성자에 넣을 필드를 선택하는 단계다

중요:

- 교수님이 `WorkUnit` 타입의 매개변수를 가지는 생성자를 만들라고 했다면
- `autoSms`, `msg`는 체크 해제하고 `workUnit`만 남겨야 한다

### 4. Eclipse에서 Getter 만들기

#### 순서

1. `HardWorkUnit.java` 파일을 연다
2. 클래스 본문 안에서 마우스 오른쪽 클릭
3. `Source`
4. `Generate Getters and Setters...`
5. `autoSms`, `workUnit`, `msg`를 선택
6. Getter만 필요하면 getter만 생성
7. `Generate` 클릭

생성 결과:

```java
public SmsSender getAutoSms() {
    return autoSms;
}

public WorkUnit getWorkUnit() {
    return workUnit;
}

public String getMsg() {
    return msg;
}
```

### 5. 스크린샷 이름 정리 권장

문서용으로 스크린샷을 저장한다면 아래 이름처럼 맞추는 것을 권장한다.

- `week03-source-generate-constructor-menu.png`
- `week03-generate-constructor-using-fields.png`
- `week03-generate-getters-and-setters.png`

이렇게 해두면 나중에 주차별 문서에 붙일 때 파일 이름만 보고도 어떤 화면인지 바로 알 수 있다.

## 추가 실습: `@Autowired`로 주입하기

### 1. `@Autowired`란?

- 스프링이 필요한 Bean 객체를 자동으로 찾아 넣어주는 어노테이션이다
- 개발자가 `new`로 직접 객체를 만들지 않아도 된다

쉽게 말하면:

- "이 필드나 생성자에 필요한 객체는 스프링이 넣어주세요"

라는 표시다.

### 2. `HardWorkUnit`에 적용한 코드

```java
@Component
public class HardWorkUnit {

    @Autowired
    private SmsSender autoSms;

    private WorkUnit workUnit;
    private String msg;

    @Autowired
    public HardWorkUnit(WorkUnit workUnit) {
        this.workUnit = workUnit;
    }
}
```

의미:

- `@Autowired private SmsSender autoSms;`
  - 스프링이 `SmsSender` 타입 Bean을 찾아 `autoSms`에 넣는다
- `@Autowired public HardWorkUnit(WorkUnit workUnit)`
  - 스프링이 `WorkUnit` Bean을 찾아 생성자 매개변수로 넣는다

### 3. 지금 프로젝트에서 실제로 주입되는 것

- `autoSms` <- `configSms` Bean
- `workUnit` <- `workUnit` Bean

이유:

- `SmsSender` 타입 Bean이 `configSms()`, `autoSms()` 두 개 등록되어 있다
- 그중 `@Qualifier("configSms")`가 붙어 있으므로 `configSms` Bean이 선택된다
- `WorkUnit`은 `@Component`가 붙어 있어 자동 Bean 등록된다

### 4. 왜 `msg`는 주입하지 않는가

- `msg`는 `String` 타입이지만 현재 프로젝트에는 `String` Bean 등록이 없다
- 그래서 지금 단계에서는 단순 멤버 변수로만 둔다
- 나중에 필요하면 `@Value` 같은 방식으로 값을 넣을 수 있다

## 추가 실습: `application.properties` 값 주입

### 1. 먼저 프로퍼티 값 추가

파일: `src/main/resources/application.properties`

```properties
message.greeting=안녕하세요 스프링 DI 실습입니다.
```

의미:

- `message.greeting` 이라는 이름의 설정 값을 등록한 것이다
- 이 값은 Bean이 아니라 문자열 설정값이다

### 2. `setMsg()` 메소드로 값 주입

파일: `src/main/java/Lect_B/week03/HardWorkUnit.java`

```java
@Value("${message.greeting}")
public void setMsg(String msg) {
    this.msg = msg;
}
```

의미:

- 스프링이 `application.properties`에서 `message.greeting` 값을 찾는다
- 그 값을 `setMsg()` 메소드의 매개변수로 넣는다
- 결과적으로 `msg` 필드에 문자열이 저장된다

### 3. 왜 여기서는 `@Autowired`가 아니라 `@Value`인가

- `@Autowired`는 **Bean 객체 주입**에 사용한다
- `@Value`는 **설정값 주입**에 사용한다

즉:

- `SmsSender`, `WorkUnit` 같은 객체는 `@Autowired`
- `message.greeting` 같은 문자열 설정값은 `@Value`

### 4. 지금 `HardWorkUnit`에서 주입되는 것

- `autoSms` <- `@Autowired` 와 `@Qualifier("configSms")` 로 Bean 주입
- `workUnit` <- `@Autowired` 생성자 주입
- `msg` <- `@Value` 로 프로퍼티 값 주입

## 추가 실습: 동일한 타입 Bean 충돌과 `@Qualifier`

### 1. 왜 충돌이 생기는가

현재 `SmsSender` 타입 Bean이 두 개다.

- `configSms`
- `autoSms`

둘 다 타입은 `SmsSender`이다.

그래서 아래처럼 `@Autowired`만 쓰면:

```java
@Autowired
private SmsSender autoSms;
```

스프링 입장에서는 "어느 `SmsSender`를 넣어야 하지?" 라는 문제가 생긴다.

즉, **동일한 타입의 Bean이 둘 이상이면 자동 주입 대상이 모호해질 수 있다.**

### 2. 해결 방법: `@Qualifier`

```java
@Autowired
@Qualifier("configSms")
private SmsSender autoSms;
```

의미:

- `SmsSender` 타입 Bean 중에서
- 이름이 `configSms`인 Bean을 선택해서 주입하라는 뜻이다

### 3. 지금 실습 코드의 의미

`HardWorkUnit`에서:

```java
@Autowired
@Qualifier("configSms")
private SmsSender autoSms;
```

라고 했으므로, 실제 주입 대상은:

- `AppConfig` 클래스의 `configSms()` 메서드가 만든 Bean

이다.

즉 지금은:

- `configSms`도 존재
- `autoSms`도 존재
- 하지만 `HardWorkUnit`에는 `configSms`가 들어간다
