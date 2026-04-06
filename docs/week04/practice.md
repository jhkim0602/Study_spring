# Week 04 Practice

## 주제

4주차 실습은 강의자료의 DI 개념을 현재 웹 프로젝트 안에서 실제로 확인하는 과정이다.

## 이 실습의 목적

4주차 실습은 아래를 "화면으로 보이게" 만드는 것이 목표다.

- 생성자 주입
- `@Qualifier`
- `@Value`
- Java Config 빈 등록
- XML 기반 주입
- Lombok 기반 생성자 주입
- `ApplicationContext` 조회

즉 4주차는 "DI 개념을 코드로 만져 보는 주차"다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/com/example/lect8/Lect8Application.java` | `week04` 패키지 스캔 시작점 |
| `src/main/java/Lect_B/week04/AppConfig.java` | Java Config 기반 빈 등록 |
| `src/main/java/Lect_B/week04/Week04IndexController.java` | `/week04` 메인 진입 |
| `src/main/java/Lect_B/week04/DIController.java` | 4주차 실습 전체 라우트 |
| `src/main/java/Lect_B/week04/HardWorkUnit.java` | 생성자 주입, `@Qualifier`, `@Value` 실습 |
| `src/main/java/Lect_B/week04/LombokWorkUnit.java` | Lombok 기반 생성자 주입 실습 |
| `src/main/java/Lect_B/week04/LombokXmlService.java` | XML + Lombok 조합 실습 |
| `src/main/java/Lect_B/week04/WorkUnit.java` | 컴포넌트 스캔 대상 빈 |
| `src/main/java/Lect_B/week04/SmsSender.java` | DI 대상 클래스 |
| `src/main/resources/xml/week04-beans.xml` | XML 기반 빈 설정 |
| `src/main/webapp/views/week04/*.jsp` | 결과 화면 |

## 1. 왜 `week04` 전용 패키지를 따로 만들었는가

```text
src/main/java/Lect_B/week04/
src/main/webapp/views/week04/
```

의미:

- 주차별 실습 코드를 독립적으로 유지
- 이전 주차와 비교 가능
- 같은 이름의 예제라도 주차별 목적에 맞게 구성 가능

즉 교육용 프로젝트에서 매우 좋은 구조다.

## 2. `AppConfig`는 무엇을 보여주나

`AppConfig`는 Java Config 실습의 중심이다.

예:

```java
@Bean
public SmsSender configSms() {
    return new SmsSender();
}
```

이 메서드는:

- `SmsSender` 객체 하나를 만들고
- `configSms`라는 이름의 빈으로 등록한다

는 뜻이다.

또한 컬렉션도 빈이 될 수 있다는 것을 보여 주기 위해:

```java
@Bean
public List<String> unit() {
    ...
}
```

도 같이 등록한다.

## 3. `HardWorkUnit`은 왜 핵심 예제인가

`HardWorkUnit` 하나에 4주차 핵심 포인트가 거의 다 들어 있다.

### 생성자 주입

```java
public HardWorkUnit(@Qualifier("configSms") SmsSender autoSms,
        @Qualifier("week04WorkUnit") WorkUnit workUnit) {
    ...
}
```

이 코드는:

- 필수 의존성이 생성자에 드러난다
- 어떤 빈이 들어오는지 이름까지 명확하다

### `@Value`

```java
@Value("${message.greeting}")
public void setMsg(String msg) {
    this.msg = msg;
}
```

즉 객체가 외부 설정값도 함께 주입받는다는 것을 보여 준다.

## 4. `DIController`는 무엇을 하는가

`DIController`는 단순히 URL만 연결하는 컨트롤러가 아니라,  
4주차의 여러 DI 방식을 각각 분리해서 보여주는 교육용 컨트롤러다.

주요 라우트:

- `/week04/annotationDI`
- `/week04/configDI`
- `/week04/xmlDI`
- `/week04/lombokDI`
- `/week04/contextDI`

## 5. `annotationDI` 화면은 무엇을 증명하나

`annotationDI`는:

- `@Component`
- 생성자 주입
- `@Qualifier`
- `@Value`

가 실제로 동작했다는 것을 JSP로 보여준다.

즉 "빈이 주입되었다"는 말을 코드뿐 아니라 결과 화면으로 확인하게 한다.

## 6. `configDI` 화면은 무엇을 증명하나

`configDI`는 Java Config로 등록한 빈을:

- 컨테이너에서 꺼내고
- JSP로 넘기고
- 실제 값까지 출력할 수 있음을 보여준다

특히 `List<String>` 빈을 같이 꺼내는 부분은  
"컬렉션도 빈이 될 수 있다"는 점을 직관적으로 이해하게 해 준다.

## 7. `xmlDI`와 `lombokDI`는 왜 필요한가

4주차가 단순히 최신 어노테이션 방식만 가르치는 것은 아니다.

이 두 화면은:

- XML 기반 설정
- Lombok 기반 생성자 주입

도 비교하게 해 준다.

즉 학생은 한 프로젝트 안에서:

- XML
- Java Config
- 컴포넌트 스캔
- Lombok

을 모두 연결해서 볼 수 있다.

## 8. `contextDI`는 왜 넣었는가

`ApplicationContext`나 `WebApplicationContext`는 초보자에게 추상적으로 느껴질 수 있다.

그래서 `contextDI` 화면에서는:

- 컨테이너 타입
- 주입된 빈 타입
- 설정값 주입 결과

를 직접 출력해 보여 준다.

이 예제의 목적은:

> "아, 컨테이너가 진짜 존재하고 실제로 빈을 가지고 있구나"

를 체감시키는 것이다.

## 9. `modelAndViewNew`와 `modelAndViewParam`는 왜 비교하는가

이 부분은 MVC 관점의 보강 실습이다.

- 하나는 `new ModelAndView()`로 직접 생성
- 하나는 메서드 파라미터로 주입받아 사용

이 차이를 통해 학생은:

- 스프링이 어떤 객체를 자동으로 제공할 수 있는지
- 컨트롤러 메서드 시그니처를 어떻게 설계하는지

를 조금 더 감각적으로 익히게 된다.

## 10. 초심자가 4주차 코드를 읽는 순서

1. `Week04IndexController`로 진입점 확인
2. `AppConfig`에서 어떤 빈이 등록되는지 확인
3. `WorkUnit`, `SmsSender`, `HardWorkUnit` 구조 읽기
4. `DIController`에서 어떤 URL이 어떤 예제를 보여주는지 확인
5. JSP에서 어떤 값이 출력되는지 비교

## 11. 이 실습에서 자주 놓치는 포인트

### 포인트 1. `@Autowired`만 중요한 것이 아니다

실제로는:

- 어떤 타입 빈이 있는지
- 이름이 무엇인지
- 여러 개면 어떻게 구분할지

까지 같이 봐야 한다.

### 포인트 2. `@Value`도 DI의 일부다

객체만 주입받는 것이 아니라 설정값도 주입받는다.

### 포인트 3. XML은 과거 유산이 아니라 비교 학습용으로 중요하다

설정 방식의 차이를 이해하면 스프링 구조 이해가 깊어진다.

## 12. 이 실습을 끝내면 말할 수 있어야 하는 것

- 생성자 주입이 왜 좋은가
- `@Qualifier`가 왜 필요한가
- Java Config와 XML은 무엇이 같은 목적을 가지는가
- `@Value`는 어디서 값을 가져오는가
- 컨트롤러가 빈과 설정값을 화면에 어떻게 전달하는가
