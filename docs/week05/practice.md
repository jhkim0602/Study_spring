# Week 05 Practice

## 주제

5주차 실습은 제공된 실습 코드와 PPT 내용을 현재 프로젝트 구조로 옮기면서,  
빈 관리 개념을 실제 라우트와 JSP 화면으로 확인할 수 있게 만든 기록이다.

## 이 실습의 목적

5주차 실습의 목표는 아래를 "눈으로 확인"하는 것이다.

- 같은 빈을 두 번 꺼냈을 때 정말 같은 객체인가
- prototype은 정말 매번 새로 생성되는가
- singleton 안에 prototype을 넣으면 왜 문제가 생기는가
- 초기화/종료 메서드는 실제로 어떤 순서로 실행되는가
- 외부 설정값이 객체에 실제로 들어오는가

즉 이 실습은 5주차 이론을 결과 화면으로 검증하는 역할을 한다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/Lect_B/week05/Week05IndexController.java` | `/week05` 진입 |
| `src/main/java/Lect_B/week05/Week05PracticeController.java` | 5주차 실습 전체 제어 |
| `src/main/java/Lect_B/week05/SmsSender.java` | 기본 singleton 예제용 클래스 |
| `src/main/java/Lect_B/week05/WorkUnit.java` | prototype 빈 |
| `src/main/java/Lect_B/week05/ScopeSingletonClient.java` | singleton 안에 prototype 직접 주입 예제 |
| `src/main/java/Lect_B/week05/ScopeFactoryClient.java` | `ObjectFactory` 해결 예제 |
| `src/main/java/Lect_B/week05/Week05LifecycleBean.java` | 라이프사이클 예제 |
| `src/main/java/Lect_B/week05/Week05AwareComponent.java` | Aware 인터페이스 예제 |
| `src/main/java/Lect_B/week05/Week05ExternalConfigComponent.java` | 외부 설정 매핑 예제 |
| `src/main/resources/week05-external.properties` | 외부 설정 파일 |
| `src/main/resources/xml/week05-*.xml` | XML 실습 파일 |
| `src/main/webapp/views/week05/*.jsp` | 실습 결과 화면 |

## 1. 왜 `week05`는 예제가 많아졌는가

3~4주차는 주로 DI 그 자체가 주제였다.  
5주차는 그 위에:

- scope
- lifecycle
- aware
- properties

같은 하위 주제가 붙는다.

즉 하나의 큰 개념을 여러 작은 예제로 나눠 확인해야 해서 라우트가 늘어난 것이다.

## 2. `autoDI`, `commonDI`, `list`, `map` 예제는 왜 남겨 두었는가

5주차 실습 원본에는 이미 이전 주차 개념과 이어지는 코드가 섞여 있었다.

이 예제들을 남긴 이유:

- DI 기초를 다시 확인할 수 있고
- 컬렉션 주입처럼 4주차 개념이 5주차에도 이어진다는 것을 보여 주며
- "빈 관리"가 DI와 완전히 별개가 아니라 연결된다는 것을 알 수 있기 때문이다

즉 5주차는 새 개념만 배우는 것이 아니라 이전 개념을 확장하는 주차다.

## 3. `scope` 예제는 무엇을 보여 주는가

`/week05/scope`는 5주차 전체에서 가장 중요한 화면이다.

여기서 한 번에 볼 수 있는 것:

- singleton 두 번 조회 결과
- prototype 두 번 조회 결과
- request scope 결과
- session scope 결과
- singleton 안에 직접 주입된 prototype의 재사용 문제
- `ObjectFactory`로 새 prototype을 얻는 해결책

즉 5주차 이론 거의 전부가 이 한 화면에 압축되어 있다.

## 4. `ScopeSingletonClient`가 중요한 이유

이 클래스는 일부러 "문제가 생기도록" 만든 예제다.

```java
private final WorkUnit workUnit;
```

겉으로는 그냥 생성자 주입처럼 보인다.  
하지만 `WorkUnit`이 prototype이면,
우리가 기대하는 것은 "매번 다른 객체"다.

그런데 singleton 안에 주입되면:

- 생성 시점에 들어간 하나의 `WorkUnit`
- 그 객체를 계속 재사용

하게 된다.

즉 이 예제는 "이론상 prototype"과 "실제 사용상 prototype"이 다를 수 있음을 보여 준다.

## 5. `ScopeFactoryClient`는 왜 해결책인가

`ScopeFactoryClient`는 `ObjectFactory<WorkUnit>`를 사용한다.

의미:

- `WorkUnit`을 미리 받아 두지 않고
- 필요할 때마다 컨테이너에 요청한다

따라서 매 호출 시 새로운 prototype을 얻을 수 있다.

이 예제는 학생이 "왜 ObjectFactory가 존재하는가"를 납득하게 해 준다.

## 6. `lifecycle` 예제는 왜 별도 컨텍스트를 쓰는가

애플리케이션 전체 컨텍스트는 서버가 계속 살아 있는 동안 닫히지 않는다.  
그러면 destroy 관련 메서드를 바로 보기 어렵다.

그래서 실습에서는:

- 로컬 `AnnotationConfigApplicationContext`를 만들고
- 빈을 하나 꺼낸 뒤
- 즉시 `close()` 한다

이렇게 해서 종료 메서드까지 한 번에 확인하게 만든다.

즉 이 실습은 **라이프사이클을 눈에 띄게 보여 주기 위한 실험용 구조**다.

## 7. `aware` 예제는 왜 필요한가

`BeanNameAware`, `ApplicationContextAware`는 평소에 자주 쓰는 문법은 아니다.  
그래서 이론만 보면 왜 필요한지 감이 잘 안 온다.

실습에서 직접 보여 주는 내용:

- 스프링이 전달한 빈 이름
- 컨테이너의 빈 개수
- 컨텍스트로 다른 빈을 직접 조회한 결과

즉 "컨테이너가 빈에게 메타정보를 줄 수 있다"는 것을 체감하게 한다.

## 8. `properties` 예제는 왜 실무 감각과 연결되는가

외부 설정은 수업용 개념처럼 보여도 실무에서 매우 중요하다.

이 실습에서는:

- `@Value`로 단일 값 읽기
- `@ConfigurationProperties`로 구조화된 값 읽기

를 한 번에 비교한다.

학생이 여기서 느껴야 하는 핵심은:

> "설정값은 코드에 박는 것이 아니라 파일로 분리해서 읽는구나"

이다.

## 9. 초심자가 5주차 코드를 읽는 추천 순서

1. `Week05IndexController`와 `/week05/index.jsp`로 전체 메뉴 보기
2. `Week05PracticeController`에서 라우트 목록 확인
3. `scope` 관련 클래스 먼저 읽기
4. `lifecycle`, `aware`, `properties` 순서로 읽기
5. 마지막으로 XML 파일과 보조 예제(`autoDI`, `list`, `map`) 보기

이 순서가 좋은 이유:

가장 핵심인 `scope`부터 잡아야 나머지 예제가 정리되기 때문이다.

## 10. 이 실습에서 자주 놓치는 포인트

### 포인트 1. request/session은 화면에서 한 번만 보면 잘 안 느껴질 수 있다

같은 요청 안에서 다시 조회한 값과,
새 요청 또는 새 브라우저에서의 값을 비교해야 차이가 더 잘 보인다.

### 포인트 2. prototype은 "선언"만 보면 안 된다

어디서 어떻게 조회되는지까지 봐야 실제 동작을 이해할 수 있다.

### 포인트 3. 외부 설정은 단순 문자열 출력이 아니라 설계 방식이다

실무에서 환경별 설정을 바꾸는 기반이 된다.

## 11. 이 실습을 끝내면 말할 수 있어야 하는 것

- 왜 singleton과 prototype이 나뉘는가
- singleton 안에 prototype을 직접 넣으면 왜 문제가 되는가
- ObjectFactory가 왜 필요한가
- lifecycle 메서드는 왜 생성자와 다른가
- `@Value`와 `@ConfigurationProperties`는 각각 언제 쓰는가
