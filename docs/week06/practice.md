# Week 06 Practice

## 주제

사용자가 제공한 `6주` 실습 폴더를 현재 `lect_B` 프로젝트 구조에 맞게 재작성한 기록이다.  
이번 주차는 원본 실습 코드가 이미 `scope`, `lifecycle`, `aware`, `external properties`에 집중되어 있어서, 5주차보다 더 교수님 예제 파일명 중심으로 옮겼다.

## 재작성 원칙

- 원본 클래스명 흐름은 유지한다.
- 기존 `week05`와 충돌하지 않도록 `week06` 전용 패키지와 빈 이름을 사용한다.
- 라우트는 `/week06/...` 형태로 통일한다.
- 원본에 없던 lifecycle 화면만 보강해서 콘솔이 아니라 JSP에서도 순서를 확인할 수 있게 했다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/Lect_B/week06/BeanScopeConfig.java` | 스코프 예제용 빈 등록 |
| `src/main/java/Lect_B/week06/BeanScopeController.java` | 6주차 실습 라우트 |
| `src/main/java/Lect_B/week06/InitDestroyUnit.java` | 라이프사이클 훅 실습 |
| `src/main/java/Lect_B/week06/LifeCycleConfig.java` | `initMethod`, `destroyMethod` 설정 |
| `src/main/java/Lect_B/week06/AwareInterfaceImp.java` | `BeanNameAware`, `ApplicationContextAware` 실습 |
| `src/main/java/Lect_B/week06/ExternalConfigComponent.java` | 외부 프로퍼티 매핑 |
| `src/main/java/Lect_B/week06/Week06SmsSender.java` | 스코프 비교용 객체 |
| `src/main/java/Lect_B/week06/Week06WorkUnit.java` | prototype 작업 객체 |
| `src/main/java/Lect_B/week06/Week06DifferentScopeClient.java` | singleton 내부 직접 주입 예제 |
| `src/main/java/Lect_B/week06/Week06ObjectFactoryClient.java` | `ObjectFactory` 해결 예제 |
| `src/main/resources/week06-external.properties` | 6주차 외부 설정 파일 |
| `src/main/webapp/views/week06/*.jsp` | 6주차 화면 |

## 1. 빈 범위 설정 예제

원본 `BeanScopeConfig`의 핵심은 같은 타입 빈을 서로 다른 범위로 등록하는 것이다.

현재 프로젝트에서는 아래 4개 빈을 만들었다.

- `week06ScopeBean0`: singleton
- `week06ScopeBean1`: prototype
- `week06ScopeBean2`: request
- `week06ScopeBean3`: session

`/week06/scopeBean`에서 각 빈을 한 요청 안에서 두 번 조회해 같은 객체인지 비교한다.

정리:

- singleton: 두 번 조회해도 같다
- prototype: 두 번 조회하면 다르다
- request: 같은 요청 안에서는 같다
- session: 같은 세션 안에서는 같다

## 2. 서로 다른 범위 빈 사용

원본 `useDifferentScope` 예제는 singleton이 prototype을 직접 의존할 때 문제를 보여준다.

현재 프로젝트에서는 `Week06DifferentScopeClient`가 singleton이고, 생성 시점에 prototype `Week06WorkUnit` 하나를 주입받는다.

`/week06/useDifferentScope`에서 같은 getter를 두 번 호출해도 같은 `Week06WorkUnit`이 나온다.

이것이 바로:

- prototype으로 선언했지만
- singleton이 이미 받아 둔 객체를 계속 재사용하는 문제

다.

## 3. `ObjectFactory` 사용

원본 `objectFactoryBeanTest` 예제는 위 문제의 해결 방법이다.

`Week06ObjectFactoryClient`는 `ObjectFactory<Week06WorkUnit>`를 주입받고,  
필요할 때마다 `getObject()`를 호출한다.

`/week06/objectFactoryBeanTest`에서 두 번 생성한 객체는 서로 다르다.

즉:

- 직접 주입은 고정
- `ObjectFactory`는 지연 조회

라는 차이를 바로 비교할 수 있다.

## 4. 라이프사이클 메서드

원본 `InitDestroyUnit`은 아래 네 가지를 한 번에 보여준다.

- `@PostConstruct`
- `@PreDestroy`
- `InitializingBean.afterPropertiesSet()`
- `DisposableBean.destroy()`

그리고 `LifeCycleConfig`에서

- `initMethod = "init"`
- `destroyMethod = "cleanup"`

도 같이 설정한다.

현재 프로젝트에서는 `/week06/post-pre`로 열어서

- 초기화 직후 이벤트
- 컨텍스트 종료 후 이벤트

를 화면에서 순서대로 볼 수 있게 바꿨다.

## 5. Aware 인터페이스

원본 `AwareInterfaceImp`는 컨테이너가 빈 이름과 `ApplicationContext`를 주입하는 흐름을 보여준다.

`/week06/awareInterfaceEx`에서 확인 가능한 값:

- `setBeanName()`으로 받은 실제 빈 이름
- `ApplicationContext` ID
- 현재 컨테이너가 가진 빈 이름 목록

즉 이 예제는 "빈이 컨테이너 자체를 직접 알아야 할 때"를 설명한다.

## 6. 외부 설정 프로퍼티

원본 `ExternalConfigComponent`는 `@PropertySource`, `@Value`, `@ConfigurationProperties`를 함께 사용했다.

현재 프로젝트에서는 `week06-external.properties`로 분리했다.

- `@Value`: `week06.server.port`, `week06.server.address`, `week06.message.greeting`
- `@ConfigurationProperties(prefix = "week06.datasource")`: URL, 사용자명, 비밀번호

`/week06/externalConfigEx`에서 모든 값이 화면에 출력된다.

## 7. 이번 주차에서 기억할 점

- 6주차 실습은 bean 생성 자체보다 bean 관리 방식이 핵심이다.
- 스코프와 라이프사이클은 서로 연결된 개념이다.
- singleton과 prototype을 함께 쓸 때는 `ObjectFactory` 같은 지연 조회가 중요하다.
- 외부 설정은 단순 값 주입과 객체 매핑을 나눠서 이해해야 한다.
