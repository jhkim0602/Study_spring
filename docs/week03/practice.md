# Week 03 Practice

## 주제

스프링 컨테이너가 관리하는 Bean 객체를 XML 방식과 Java 설정 방식으로 만들고, 컨트롤러에서 꺼내서 JSP로 전달하는 실습이다.

## 이 실습의 의미

3주차 실습은 아래 질문을 실제 프로젝트 파일로 검증하는 과정이다.

- 빈이 정말 만들어지는가?
- XML과 Java Config는 결과적으로 같은 목적을 가지는가?
- 컨테이너에서 빈을 꺼내 화면으로 보낼 수 있는가?

즉 이 실습은 **Bean 개념을 추상 설명에서 실제 출력으로 끌어내리는 단계**다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/Lect_B/week03/SmsSender.java` | Bean으로 사용할 가장 단순한 클래스 |
| `src/main/java/Lect_B/week03/AppConfig.java` | Java Config 방식의 설정 클래스 |
| `src/main/java/Lect_B/week03/ContextController.java` | 요청 처리와 Bean 조회를 담당하는 컨트롤러 |
| `src/main/java/Lect_B/week03/WorkUnit.java` | `@Component`로 자동 등록되는 실습용 빈 |
| `src/main/java/Lect_B/week03/HardWorkUnit.java` | 생성자 주입 실습용 클래스 |
| `src/main/resources/static/xml/Ex1.xml` | XML 방식 Bean 설정 파일 |
| `src/main/webapp/views/week03/beanView.jsp` | 조회 결과를 출력하는 JSP |

## 먼저 알아둘 점: 현재 통합 프로젝트에서의 위치

현재 `Lect8Application`은 `week04~week06` 중심으로 전역 스캔하도록 설정되어 있다.  
그래서 `week03`은 현재 프로젝트에서 "초기 Bean 개념을 보여 주는 분리된 학습 패키지"로 읽는 것이 가장 정확하다.

이렇게 둔 이유:

- 3주차와 4주차 모두 `AppConfig`를 가지고 있고
- `configSms` 같은 빈 이름도 겹칠 수 있으며
- 한 번에 다 스캔하면 충돌 가능성이 생기기 때문이다

즉 이 문서의 포인트는:

- 3주차 예제를 버린 것이 아니라
- **Bean 개념을 충돌 없이 독립적으로 이해하도록 분리해 둔 것**

이라는 점이다.

## 1. `SmsSender`가 왜 좋은 예제인가

`SmsSender`는 일부러 단순하게 만든 클래스다.

이유:

- 구조가 간단해서 Bean 개념에 집중할 수 있다
- 생성자 값 차이로 XML/Java Config 등록 결과를 구분할 수 있다
- 객체 타입과 내부 상태를 화면에 출력하기 좋다

즉 기능이 중요한 게 아니라 **관리 대상 객체의 예시**로 적합하다.

## 2. XML 방식 빈 등록

예:

```xml
<bean id="xmlSms" class="Lect_B.week03.SmsSender" />
```

이 코드가 의미하는 것:

- `xmlSms`라는 이름으로
- `Lect_B.week03.SmsSender` 타입의 객체를
- 스프링 컨테이너에 등록하라

여기서 중요한 것은:

- 클래스만 존재한다고 빈이 되는 것은 아니다
- 컨테이너에 등록되어야 빈이다

## 3. Java Config 방식 빈 등록

예:

```java
@Configuration
public class AppConfig {

    @Bean
    public SmsSender configSms() {
        return new SmsSender("학교실습용 SMS 발신기");
    }
}
```

여기서 스프링이 이해하는 정보:

- 이 클래스는 설정 클래스다
- `configSms()`가 반환한 객체는 빈이다
- 빈 이름은 기본적으로 메서드 이름 `configSms`다

즉 Java 코드이지만 역할은 XML의 `<bean>`과 같다.

## 4. 왜 컨트롤러 안에서 컨텍스트를 직접 만들었는가

실습 코드에는 아래 같은 흐름이 나온다.

```java
xmlContext = new XmlWebApplicationContext();
xmlContext.setConfigLocation("classpath:static/xml/Ex1.xml");
xmlContext.refresh();

configContext = new AnnotationConfigWebApplicationContext();
configContext.register(AppConfig.class);
configContext.refresh();
```

실무에서는 보통 스프링 부트가 컨텍스트를 자동으로 관리한다.  
그런데 여기서는 교육 목적상:

- XML 컨테이너
- Java Config 컨테이너

를 명시적으로 비교하려고 직접 만든 것이다.

즉 "실습을 위한 의도적 단순화"라고 보면 된다.

## 5. `getBean()`은 왜 중요한가

```java
SmsSender xmlSms = xmlContext.getBean("xmlSms", SmsSender.class);
SmsSender configSms = configContext.getBean("configSms", SmsSender.class);
```

이 코드는:

- 컨테이너 안에 등록된 빈을
- 이름과 타입을 기준으로 찾아
- 실제 자바 객체처럼 사용하겠다

는 의미다.

학생이 여기서 느껴야 하는 핵심은:

> "내가 직접 만든 게 아니라 컨테이너가 만든 객체를 가져다 쓰는구나"

이다.

## 6. `ModelAndView`는 왜 쓰는가

컨트롤러는 빈을 조회한 뒤 그것을 JSP로 넘겨야 한다.

이 역할을 하는 대표 도구가 `ModelAndView`다.

예:

```java
mav.setViewName("week03/beanView");
mav.addObject("xmlSms", xmlSms);
mav.addObject("configSms", configSms);
```

의미:

- 어느 JSP로 갈지 정한다
- JSP가 읽을 데이터 이름과 값을 담는다

## 7. 요청 처리 흐름을 한 번에 보면

```text
브라우저
  -> (3주차 예제 기준) /createBean 요청
  -> ContextController.beanTest()
  -> XML 컨텍스트 / Java Config 컨텍스트에서 빈 조회
  -> ModelAndView에 담기
  -> week03/beanView.jsp 렌더링
  -> 브라우저 출력
```

이 흐름을 이해하면 이후 주차도 거의 같은 틀로 읽을 수 있다.

## 8. `@Component` 실습은 왜 추가되는가

3주차 후반부에는 `WorkUnit`, `HardWorkUnit`이 나온다.

이건 "수동 등록(XML, @Bean)"만이 아니라  
"자동 등록(@Component)"도 있다는 것을 보여주기 위한 예제다.

### `WorkUnit`

```java
@Component
public class WorkUnit {
}
```

이 뜻은:

- 이 클래스를 스프링이 스캔해서
- 자동으로 빈으로 등록하라는 의미다

### `HardWorkUnit`

`HardWorkUnit`은 이후 생성자 주입으로 연결될 준비를 보여준다.

즉 3주차 실습은 Bean 생성뿐 아니라  
4주차 DI로 넘어가기 위한 다리 역할도 한다.

## 9. Eclipse에서 생성자와 Getter를 만드는 이유

교수님 실습에서 생성자와 getter를 직접 만들게 하는 이유는,

- 의존성이 어디 들어가는지 눈으로 보게 하고
- 자바 클래스 구조를 익히게 하며
- 스프링이 나중에 이 생성자와 메서드를 어떻게 사용하는지 연결하기 위해서다

## 10. 초심자가 코드 읽을 때 체크해야 할 것

### 체크 1. 이 클래스는 빈인가?

- `@Component`가 있는가?
- `@Bean`으로 등록되었는가?
- XML `<bean>`에 있는가?

### 체크 2. 빈 이름은 무엇인가?

- XML의 `id`
- `@Bean` 메서드 이름
- `@Component` 기본 이름

### 체크 3. 어디서 꺼내는가?

- `getBean()` 호출 지점
- 컨트롤러에서 사용하는 부분

### 체크 4. 화면에는 무엇이 보이는가?

- JSP에서 출력한 객체
- 타입 이름
- 내부 값

## 11. 이 실습이 4주차로 어떻게 이어지는가

3주차에서:

- 빈을 등록하고
- 컨테이너에서 꺼내고
- 화면에 보여주는 것

까지 했다.

4주차에서는 여기에 더해:

- 빈을 자동 주입하고
- 같은 타입 빈을 구분하고
- 설정값도 주입하는 흐름

을 배우게 된다.

## 12. 실습에서 꼭 기억할 점

- XML 방식과 Java Config 방식은 등록 문법만 다르고 목적은 같다.
- 컨테이너가 관리하면 그 객체는 빈이다.
- 컨트롤러는 빈을 조회해서 뷰에 전달할 수 있다.
- `@Component`는 자동 등록의 시작이다.
- 3주차를 이해해야 4주차의 DI 문법이 단순 암기가 아니라 구조로 보인다.
