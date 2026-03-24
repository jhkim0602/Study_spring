# Week 04 Practice

## 주제

4주차 실습에서는 강의자료의 DI 개념을 실제 웹 프로젝트 안에서 확인했다.  
문서는 "새로 만든 결과"만 적는 것이 아니라, 수업 중 누적된 실습 흐름을 기준으로 정리한다.

## 실습 목표

- `Lect_B.week04` 패키지를 주차별 독립 실습 공간으로 사용하기
- Java Config 방식으로 Bean 등록하기
- `@Component`, `@Autowired`, `@Qualifier`, `@Value`를 이용해 DI 확인하기
- 컨트롤러에서 스프링 컨테이너의 Bean을 조회하기
- JSP에서 주입 결과를 직접 화면으로 확인하기

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/com/example/lect8/Lect8Application.java` | `Lect_B.week04` 패키지 스캔 시작점 |
| `src/main/java/Lect_B/week04/AppConfig.java` | Java Config 기반 Bean 등록 |
| `src/main/java/Lect_B/week04/Week04IndexController.java` | `/week04` 메인 진입 컨트롤러 |
| `src/main/java/Lect_B/week04/DIController.java` | 4주차 DI 실습 컨트롤러 |
| `src/main/java/Lect_B/week04/HardWorkUnit.java` | 생성자 주입, `@Qualifier`, `@Value` 실습용 Bean |
| `src/main/java/Lect_B/week04/LombokWorkUnit.java` | Lombok 기반 생성자 주입 Bean |
| `src/main/java/Lect_B/week04/LombokXmlService.java` | XML + Lombok 생성자 주입 실습용 클래스 |
| `src/main/java/Lect_B/week04/WorkUnit.java` | `@Component`로 등록되는 작업 Bean |
| `src/main/java/Lect_B/week04/SmsSender.java` | 문자 발신기 역할 클래스 |
| `src/main/resources/application.properties` | `message.greeting` 설정값 |
| `src/main/resources/xml/week04-beans.xml` | XML 기반 Bean, XML + Lombok 주입 설정 |
| `src/main/webapp/index.jsp` | 루트 인덱스 |
| `src/main/webapp/views/week04/index.jsp` | 4주차 메인 화면 |
| `src/main/webapp/views/week04/annotattionDIView.jsp` | 어노테이션 기반 DI 결과 화면 |
| `src/main/webapp/views/week04/configDIView.jsp` | Java Config 기반 DI 결과 화면 |
| `src/main/webapp/views/week04/lombokDIView.jsp` | Lombok DI 결과 화면 |
| `src/main/webapp/views/week04/beanView.jsp` | XML DI 비교 화면 |
| `src/main/webapp/views/week04/context.jsp` | ApplicationContext 확인 화면 |
| `src/main/webapp/views/week04/modelAndViewView.jsp` | ModelAndView 비교 화면 |

## 1. 실습 구조 만들기

4주차는 3주차와 별개로 관리하기 위해 별도 패키지와 JSP 폴더를 사용한다.

```text
src/main/java/Lect_B/week04/
src/main/webapp/views/week04/
```

의미:

- 주차별 코드가 섞이지 않는다
- 이전 주차와 비교하기 쉽다
- 실습 내용을 누적해서 관리하기 좋다

## 2. `scanBasePackages` 설정

루트 애플리케이션 클래스에서 `Lect_B.week04`를 스캔 대상으로 잡아야 한다.

```java
@SpringBootApplication(scanBasePackages = {"Lect_B.week04"})
public class Lect8Application {
}
```

의미:

- `AppConfig`
- `DIController`
- `WorkUnit`
- `HardWorkUnit`

같은 4주차 클래스를 스프링이 찾아 Bean으로 등록한다.

## 3. Java Config로 Bean 등록

`AppConfig.java`에서는 `@Bean`으로 두 가지 Bean을 등록했다.

```java
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
```

의미:

- `configSms`: `SmsSender` 타입 Bean
- `unit`: `List<String>` 타입 Bean

즉 단일 객체와 컬렉션 객체를 모두 Java Config로 등록하는 예제다.

## 4. 4주차 메인 진입 구성

`/week04` 주소는 별도 컨트롤러에서 처리한다.

```java
@Controller
public class Week04IndexController {

    @GetMapping({"/week04", "/week04/"})
    public String index() {
        return "week04/index";
    }
}
```

의미:

- 4주차 메인 화면을 독립적으로 열 수 있다
- 메인 화면에는 교수님 흐름에 맞춘 3개 실습 링크가 있다

- 어노테이션을 이용한 DI 예제
- config 파일을 이용한 DI 예제
- lombok을 이용한 DI 예제

## 5. `@Component`로 Bean 등록

`WorkUnit`은 어노테이션 방식으로 등록된다.

```java
@Component("week04WorkUnit")
public class WorkUnit {

    public String getUnitName() {
        return "Week04 WorkUnit";
    }
}
```

의미:

- 스프링이 자동으로 Bean 등록
- Bean 이름은 `week04WorkUnit`

## 6. 생성자 주입과 설정값 주입

`HardWorkUnit`은 4주차 DI 핵심 예제다.

```java
@Component
public class HardWorkUnit {

    private final SmsSender autoSms;
    private final WorkUnit workUnit;
    private String msg;

    @Autowired
    public HardWorkUnit(@Qualifier("configSms") SmsSender autoSms,
            @Qualifier("week04WorkUnit") WorkUnit workUnit) {
        this.autoSms = autoSms;
        this.workUnit = workUnit;
    }

    @Value("${message.greeting}")
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```

실습 포인트:

- `SmsSender`는 `configSms` Bean을 이름으로 선택한다
- `WorkUnit`은 `week04WorkUnit` Bean을 주입받는다
- `msg`는 `application.properties`에서 읽는다

즉 하나의 클래스에서

- 생성자 주입
- `@Qualifier`
- `@Value`

를 동시에 확인할 수 있다.

## 6. `annotationDI` 요청 실습

컨트롤러는 `WebApplicationContext`를 주입받아 컨테이너에 접근한다.

```java
@Autowired
private WebApplicationContext context;
```

`annotationDI` 라우트:

```java
@GetMapping({"/annotationDI", "/qualiftingDI", "/week04/annotationDI", "/week04/qualiftingDI"})
public ModelAndView annotationDI() {
    ModelAndView mav = new ModelAndView("week04/annotattionDIView");
    HardWorkUnit work = (HardWorkUnit) context.getBean("hardWorkUnit");
    mav.addObject("obj", work);
    return mav;
}
```

의미:

- 컨테이너에서 `hardWorkUnit` Bean을 가져온다
- JSP로 넘긴다
- JSP에서 `sms`, `workUnit`, `msg`를 확인한다

확인 주소:

- `/annotationDI`
- `/week04/annotationDI`

## 7. `configDI` 요청 실습

사용자가 요청한 코드 흐름을 실제 프로젝트 구조에 맞게 넣은 부분이다.

```java
@GetMapping({"/configDI", "/week04/configDI"})
public ModelAndView configDi(ModelAndView mav) {
    SmsSender sms = context.getBean("configSms", SmsSender.class);
    List<String> unit = context.getBean("unit", List.class);

    mav.addObject("obj1", sms);
    mav.addObject("obj2", unit);
    mav.addObject("sendResult", sms.send("010-1234-5678", "configDI 실습 실행"));
    mav.setViewName("week04/configDIView");
    return mav;
}
```

이 코드에서 한 일:

- `configSms` Bean을 꺼낸다
- `unit` Bean을 꺼낸다
- 두 객체를 JSP로 전달한다
- 실행 결과 문자열도 함께 넘긴다

확인 주소:

- `/configDI`
- `/week04/configDI`

## 8. `lombokDI` 요청 실습

강의자료의 Lombok 흐름을 반영해 현재 실습에서는 두 가지를 함께 보여준다.

### 8-1. 컴포넌트 스캔 + Lombok

```java
@Component
@Getter
public class LombokWorkUnit {

    private final SmsSender configSms;
    private final WorkUnit week04WorkUnit;

    @Value("${message.greeting}")
    private String msg;

    public LombokWorkUnit(SmsSender configSms, WorkUnit week04WorkUnit) {
        this.configSms = configSms;
        this.week04WorkUnit = week04WorkUnit;
    }
}
```

의미:

- Lombok은 getter 같은 반복 코드를 줄여 준다
- 생성자는 Eclipse 실행 안정성을 위해 명시적으로 두었다
- 스프링은 그 생성자에 Bean을 주입한다
- `msg`는 프로퍼티 값으로 채운다

### 8-2. XML + Lombok 생성자 주입

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

이 실습은 교수님이 설명한 다음 내용을 그대로 반영한다.

- `ref bean="xmlSms"`
- `value type="long"`
- Lombok을 활용하는 클래스에 생성자 주입

컨트롤러에서는 `lombokDI` 요청 시 두 결과를 함께 화면에 전달한다.

확인 주소:

- `/lombokDI`
- `/week04/lombokDI`

## 9. XML 기반 DI 비교 실습

PPT의 XML Bean 설정과 Java Config 비교를 실제 화면으로 확인하는 보조 실습도 추가했다.

```java
try (ClassPathXmlApplicationContext xmlContext =
        new ClassPathXmlApplicationContext("xml/week04-beans.xml")) {
    SmsSender xmlSms = xmlContext.getBean("week04XmlSms", SmsSender.class);
    SmsSender configSms = context.getBean("configSms", SmsSender.class);
}
```

확인 주소:

- `/xmlDI`
- `/week04/xmlDI`

이 실습에서 결과 화면에 직접 보이는 항목:

- xml 설정에 의해서 설정된, 인젝션한 객체
- 생성자를 통한 DI된 객체
- 생성자를 통한 DI된 기본 데이터
- Setter를 통해 DI된 객체
- Setter를 통한 DI된 기본 데이터

## 10. ApplicationContext 확인 실습

강의자료 후반부의 `ApplicationContext` 내용을 현재 프로젝트에서도 확인할 수 있게 했다.

```java
HardWorkUnit work = context.getBean("hardWorkUnit", HardWorkUnit.class);
```

확인 주소:

- `/contextDI`
- `/week04/contextDI`

이 화면에서는

- 컨테이너 타입
- 주입된 Bean 타입
- 설정값 주입 결과

를 함께 본다.

## 11. ModelAndView 두 방식 비교

교수님 설명대로 `ModelAndView`는 두 방식 모두 실습에 넣었다.

### 11-1. 직접 생성

```java
ModelAndView mav = new ModelAndView();
```

확인 주소:

- `/modelAndViewNew`
- `/week04/modelAndViewNew`

### 11-2. 매개변수 주입

```java
public ModelAndView modelAndViewParam(ModelAndView mav)
```

확인 주소:

- `/modelAndViewParam`
- `/week04/modelAndViewParam`

이 비교를 통해 교수님이 말한

- `new`로 직접 생성 가능
- 매개변수로 선언하면 스프링이 넣어줌

을 코드와 화면으로 같이 확인할 수 있다.

## 12. JSP에서 확인하는 내용

### `annotattionDIView.jsp`

확인 내용:

- `obj.sms`
- `obj.workUnit`
- `obj.msg`

즉 어노테이션 기반으로 주입된 객체와 설정값을 바로 출력한다.

### `configDIView.jsp`

확인 내용:

- `SmsSender` 객체 타입
- 발신기 이름
- `send()` 실행 결과
- `List<String>` Bean의 값 목록

즉 Java Config 방식으로 등록된 Bean을 웹 화면에서 직접 확인하는 예제다.

### `lombokDIView.jsp`

확인 내용:

- 컴포넌트 스캔 + Lombok 결과
- XML + Lombok 생성자 주입 결과
- `periodTime = 30000`
- `xmlSms`가 실제 주입된 실행 결과

즉 Lombok 실습을 한 가지 방식으로만 끝내지 않고, 강의자료 흐름대로 XML과 연결된 버전까지 확인한다.

## 13. 루트 인덱스와 4주차 화면 연결

루트 `index.jsp`에서 4주차 실습 진입 링크를 추가했다.

```jsp
<a href="/week04">DI 실습</a>
```

또 `views/week04/index.jsp`에서는 4주차 내부 링크를 정리했다.

- `/week04/annotationDI`
- `/week04/configDI`
- `/week04/lombokDI`

즉 루트에서 4주차 실습으로 진입하고, 4주차 내부에서 실습별 화면으로 이동하는 흐름이다.

## 14. 강의자료와 현재 실습의 연결

PPT에서 배운 내용과 현재 실습의 대응은 아래처럼 이해하면 된다.

| 강의자료 개념 | 현재 실습에서 대응되는 부분 |
|---|---|
| Java Config Bean 등록 | `AppConfig`의 `configSms()`, `unit()` |
| 컴포넌트 스캔 | `@Component`가 붙은 `WorkUnit`, `HardWorkUnit` |
| Lombok DI | `LombokWorkUnit`, `LombokXmlService` |
| `@Autowired` | `HardWorkUnit` 생성자, `DIController`의 `context` |
| `@Qualifier` | `HardWorkUnit` 생성자의 `configSms`, `week04WorkUnit` |
| `@Value` | `setMsg()` |
| XML 생성자 주입 | `week04-beans.xml`의 `<ref bean=\"xmlSms\" />`, `<value type=\"long\">30000</value>` |
| 컨테이너에서 Bean 가져오기 | `context.getBean(...)` |
| 뷰로 결과 전달 | `ModelAndView` + JSP |
| ApplicationContext 사용 | `contextDI` |
| ModelAndView 두 방식 | `modelAndViewNew`, `modelAndViewParam` |

즉 현재 프로젝트는 강의자료의 모든 범위를 한 번에 구현한 것은 아니지만,
PPT 핵심 개념을 실습 코드로 확인할 수 있는 중심 예제는 대부분 연결된 상태다.

## 15. 이번 주차에서 기억할 점

- 4주차는 DI 개념을 실제 코드로 연결하기 시작하는 주차다
- `@Bean`과 `@Component`는 Bean 등록 방식이 다르다
- `@Autowired`는 타입 기준 주입이다
- 같은 타입이 여러 개이면 `@Qualifier`가 필요하다
- 설정값은 `@Value`로 따로 주입한다
- XML에서는 `<ref>`로 객체를, `<value type=\"long\">`로 기본 타입 값을 넣을 수 있다
- Lombok은 생성자 주입 코드를 줄여 주며 XML과도 결합할 수 있다
- 컨트롤러는 컨테이너의 Bean을 꺼내 `ModelAndView`로 JSP에 전달할 수 있다
