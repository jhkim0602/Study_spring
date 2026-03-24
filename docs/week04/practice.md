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
| `src/main/java/Lect_B/week04/DIController.java` | 4주차 DI 실습 컨트롤러 |
| `src/main/java/Lect_B/week04/HardWorkUnit.java` | 생성자 주입, `@Qualifier`, `@Value` 실습용 Bean |
| `src/main/java/Lect_B/week04/WorkUnit.java` | `@Component`로 등록되는 작업 Bean |
| `src/main/java/Lect_B/week04/SmsSender.java` | 문자 발신기 역할 클래스 |
| `src/main/resources/application.properties` | `message.greeting` 설정값 |
| `src/main/webapp/index.jsp` | 루트 인덱스 |
| `src/main/webapp/views/week04/index.jsp` | 4주차 메인 화면 |
| `src/main/webapp/views/week04/annotattionDIView.jsp` | 어노테이션 기반 DI 결과 화면 |
| `src/main/webapp/views/week04/configDIView.jsp` | Java Config 기반 DI 결과 화면 |

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
    List<String> list = new ArrayList();
    list.add("문자열 1");
    list.add("문자열 2");
    return list;
}
```

의미:

- `configSms`: `SmsSender` 타입 Bean
- `unit`: `List<String>` 타입 Bean

즉 단일 객체와 컬렉션 객체를 모두 Java Config로 등록하는 예제다.

## 4. `@Component`로 Bean 등록

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

## 5. 생성자 주입과 설정값 주입

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

## 8. JSP에서 확인하는 내용

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

## 9. 루트 인덱스와 4주차 화면 연결

루트 `index.jsp`에서 4주차 실습 진입 링크를 추가했다.

```jsp
<a href="/annotationDI">DI 실습</a>
```

또 `views/week04/index.jsp`에서는 4주차 내부 링크를 정리했다.

- `/week04/configDI`
- `/week04/annotationDI`

즉 루트에서 4주차 실습으로 진입하고, 4주차 내부에서 실습별 화면으로 이동하는 흐름이다.

## 10. 강의자료와 현재 실습의 연결

PPT에서 배운 내용과 현재 실습의 대응은 아래처럼 이해하면 된다.

| 강의자료 개념 | 현재 실습에서 대응되는 부분 |
|---|---|
| Java Config Bean 등록 | `AppConfig`의 `configSms()`, `unit()` |
| 컴포넌트 스캔 | `@Component`가 붙은 `WorkUnit`, `HardWorkUnit` |
| `@Autowired` | `HardWorkUnit` 생성자, `DIController`의 `context` |
| `@Qualifier` | `HardWorkUnit` 생성자의 `configSms`, `week04WorkUnit` |
| `@Value` | `setMsg()` |
| 컨테이너에서 Bean 가져오기 | `context.getBean(...)` |
| 뷰로 결과 전달 | `ModelAndView` + JSP |

즉 현재 프로젝트는 강의자료의 모든 범위를 한 번에 구현한 것은 아니지만,
PPT 핵심 개념을 실습 코드로 확인할 수 있는 중심 예제는 이미 포함하고 있다.

## 11. 이번 주차에서 기억할 점

- 4주차는 DI 개념을 실제 코드로 연결하기 시작하는 주차다
- `@Bean`과 `@Component`는 Bean 등록 방식이 다르다
- `@Autowired`는 타입 기준 주입이다
- 같은 타입이 여러 개이면 `@Qualifier`가 필요하다
- 설정값은 `@Value`로 따로 주입한다
- 컨트롤러는 컨테이너의 Bean을 꺼내 `ModelAndView`로 JSP에 전달할 수 있다
