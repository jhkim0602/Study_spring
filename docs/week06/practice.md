# Week 06 Practice

## 주제

6주차 실습은 제공된 `BeanScopeConfig`, `BeanScopeController`, `InitDestroyUnit`, `AwareInterfaceImp`, `ExternalConfigComponent` 흐름을 현재 프로젝트 구조에 맞게 재작성한 것이다.

## 이 실습의 목적

6주차 실습은 5주차 개념을 다시 반복하는 것이 아니라,

- 교수님 예제 코드 스타일로 다시 보고
- 같은 개념을 다른 파일 구조에서 읽어 보고
- 실제 결과 화면으로 비교하게 만드는

목적을 가진다.

즉 6주차는 "이해 고정"용 실습이다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/Lect_B/week06/BeanScopeConfig.java` | 스코프별 빈 등록 |
| `src/main/java/Lect_B/week06/BeanScopeController.java` | 6주차 라우트 제어 |
| `src/main/java/Lect_B/week06/Week06SmsSender.java` | 스코프 비교용 간단한 객체 |
| `src/main/java/Lect_B/week06/Week06WorkUnit.java` | prototype 작업 객체 |
| `src/main/java/Lect_B/week06/Week06DifferentScopeClient.java` | singleton 내부 직접 주입 예제 |
| `src/main/java/Lect_B/week06/Week06ObjectFactoryClient.java` | `ObjectFactory` 해결 예제 |
| `src/main/java/Lect_B/week06/InitDestroyUnit.java` | 라이프사이클 훅 실습 |
| `src/main/java/Lect_B/week06/LifeCycleConfig.java` | init/destroy 설정 |
| `src/main/java/Lect_B/week06/AwareInterfaceImp.java` | Aware 인터페이스 예제 |
| `src/main/java/Lect_B/week06/ExternalConfigComponent.java` | 외부 프로퍼티 읽기 |
| `src/main/java/Lect_B/week06/Week06PropertiesConfig.java` | 공통 external.properties 로딩 |
| `src/main/resources/static/external.properties` | 5주차, 6주차 공통 설정 파일 |
| `src/main/webapp/views/week06/*.jsp` | 결과 화면 |

## 1. `BeanScopeConfig`는 왜 중요한가

이 클래스는 6주차 전체의 출발점이다.

여기서:

- singleton 빈
- prototype 빈
- request 빈
- session 빈

을 모두 등록한다.

즉 학생은 한 파일 안에서 "같은 타입의 객체라도 범위를 다르게 줄 수 있다"는 것을 확인할 수 있다.

## 2. `/week06/scopeBean` 화면은 어떻게 읽어야 하는가

이 화면은 각 스코프를 한 요청 안에서 두 번 조회한 결과를 보여 준다.

읽는 법:

- singleton: 두 출력이 같아야 정상
- prototype: 두 출력이 달라야 정상
- request: 같은 요청 안에서는 같아야 정상
- session: 같은 세션 안에서는 같아야 정상

즉 여기서는 단순 출력이 아니라  
"같은 객체인지 다른 객체인지"를 보는 것이 핵심이다.

## 3. `/week06/useDifferentScope`는 왜 꼭 봐야 하는가

이 예제는 6주차의 함정 포인트를 보여 준다.

`Week06DifferentScopeClient`는 singleton이고,
`Week06WorkUnit`은 prototype이다.

그런데 생성자에서 직접 주입받으면:

- singleton 생성 시점에 들어온 한 개의 prototype
- 그 객체를 계속 사용

하게 된다.

학생이 이 예제를 보면 느껴야 하는 핵심은:

> "prototype 선언만 보고 안심하면 안 된다"

이다.

## 4. `/week06/objectFactoryBeanTest`는 무엇을 해결하는가

위 문제를 해결하기 위해 `Week06ObjectFactoryClient`가 등장한다.

이 클래스는 prototype을 직접 들고 있지 않고,
필요할 때마다 컨테이너에서 새로 꺼낸다.

따라서:

- 첫 번째 결과
- 두 번째 결과

가 서로 달라진다.

이 예제는 "지연 조회"라는 개념을 아주 잘 보여 준다.

## 5. `/week06/post-pre`는 왜 웹 요청으로 만들었는가

원본 실습은 콘솔 출력 중심이었다.  
현재 프로젝트에서는 화면에서 순서를 확인할 수 있도록 바꿨다.

이렇게 한 이유:

- 학생이 콘솔 로그를 놓쳐도
- 화면에서 초기화/종료 순서를 읽을 수 있게 하기 위해서다

즉 학습 접근성을 높이기 위한 재구성이다.

## 6. `InitDestroyUnit`을 읽을 때 보는 포인트

이 클래스는 한 번에 여러 훅을 보여 준다.

- constructor
- `@PostConstruct`
- `afterPropertiesSet()`
- `init()`
- `@PreDestroy`
- `destroy()`
- `cleanup()`

코드를 읽을 때는 "어떤 메서드가 더 좋다"보다  
"서로 다른 시점에 여러 훅이 존재한다"는 점을 먼저 이해해야 한다.

## 7. `/week06/awareInterfaceEx`는 무엇을 보여 주는가

이 화면은 Aware 인터페이스가 실제로 어떤 데이터를 주는지 확인한다.

출력 내용:

- 빈 이름
- ApplicationContext ID
- 현재 컨테이너의 빈 이름 목록

즉 Aware는 추상 개념이 아니라 실제 데이터를 주는 인터페이스라는 것을 알 수 있다.

## 8. `/week06/externalConfigEx`는 어떻게 읽어야 하는가

이 화면은 두 종류의 설정 읽기를 동시에 보여 준다.

### 단일 값

- 서버 포트
- 주소
- 메시지

### 묶음 값

- datasource URL
- userName
- password

이 비교를 통해 학생은:

- `@Value`
- `@ConfigurationProperties`

의 역할 차이를 자연스럽게 이해할 수 있다.

### 참고 사항

현재 프로젝트는 교수님 실습 방식에 맞춰
주차별 `week06-external.properties` 파일을 따로 두지 않고,
`src/main/resources/static/external.properties` 하나만 사용한다.

6주차에서는:

- `Week06PropertiesConfig`가 공통 설정 파일을 등록하고
- `ExternalConfigComponent`가 `@Value`, `@ConfigurationProperties`로 값을 읽는다

이렇게 분리해야 `@Value("${week06.server.port}")` 같은 코드가 안정적으로 동작한다.

## 9. 초심자용 코드 읽기 순서

1. `BeanScopeConfig`에서 빈 등록 확인
2. `BeanScopeController`에서 어떤 URL이 어떤 예제를 담당하는지 확인
3. `scopeBean`과 `useDifferentScope` 먼저 확인
4. `objectFactoryBeanTest`로 해결책 확인
5. `post-pre`, `awareInterfaceEx`, `externalConfigEx` 순서로 읽기

이 순서가 좋은 이유:

가장 핵심인 "스코프와 의존 문제"를 먼저 잡고,
그 다음 부가 개념을 보는 편이 이해가 빠르기 때문이다.

## 10. 이 실습이 주는 학습 효과

6주차 실습은 학생에게 다음 능력을 키워 준다.

- 출력 결과를 보고 빈 동작을 해석하는 능력
- 같은 개념을 다른 코드 스타일로 읽는 능력
- 설정 파일, 자바 설정, 컨트롤러, JSP를 연결해서 보는 능력

즉 단순히 "예제를 한 번 돌려봄"이 아니라  
스프링 객체 관리 개념을 구조적으로 다시 붙잡게 해 준다.

## 11. 이 실습을 끝내면 말할 수 있어야 하는 것

- 스코프 차이를 결과 출력으로 설명할 수 있는가
- singleton 안 prototype 문제를 실제 예제로 설명할 수 있는가
- ObjectFactory 해결 방식을 설명할 수 있는가
- 라이프사이클 훅들의 실행 순서를 말할 수 있는가
- Aware와 외부 설정 프로퍼티가 왜 필요한지 설명할 수 있는가
