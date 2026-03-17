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
}
```

의미:

- 설정 클래스: `AppConfig`
- Bean 이름: `configSms`
- Bean 타입: `Lect_B.week03.SmsSender`

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
