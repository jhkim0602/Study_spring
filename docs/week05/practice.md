# Week 05 Practice

## 주제

사용자가 제공한 `5주 2` 실습 폴더를 현재 `lect_B` 프로젝트 구조로 다시 정리하고,  
5주차 PPT 핵심 주제를 실제 라우트와 화면으로 확인할 수 있게 확장한 기록이다.

## 재작성 원칙

- 패키지는 `Lect_B.week05`로 분리해 기존 `week04`와 충돌하지 않게 했다.
- 컨트롤러 이름과 설정 클래스 이름은 주차별로 명확하게 구분했다.
- 원본 실습코드의 예제(`autoDI`, `commonDI`, `list`, `map`, XML + Lombok`)는 유지했다.
- PPT의 핵심 범위인데 원본 코드에 없던 `scope`, `lifecycle`, `aware`, `properties`는 현재 프로젝트 문맥에 맞는 추가 예제로 보강했다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/Lect_B/week05/Week05IndexController.java` | `/week05` 메인 진입 |
| `src/main/java/Lect_B/week05/Week05PracticeController.java` | 5주차 실습 라우트 전체 |
| `src/main/java/Lect_B/week05/SmsSender.java` | singleton 기본 빈 + XML 빈 공용 클래스 |
| `src/main/java/Lect_B/week05/WorkUnit.java` | prototype 범위 빈 |
| `src/main/java/Lect_B/week05/AnimalAutoDI.java` | 필드/생성자/Setter 주입 예제 |
| `src/main/java/Lect_B/week05/AbstractCommonService.java` | 공통 의존성 보관용 추상 클래스 |
| `src/main/java/Lect_B/week05/CommonService.java` | 추상 클래스 상속 실습 |
| `src/main/java/Lect_B/week05/AnimalService.java` | List/Map 컬렉션 주입 예제 |
| `src/main/java/Lect_B/week05/ScopeSingletonClient.java` | singleton 안에 prototype 직접 주입 문제 예제 |
| `src/main/java/Lect_B/week05/ScopeFactoryClient.java` | `ObjectFactory` 해결 예제 |
| `src/main/java/Lect_B/week05/Week05AwareComponent.java` | Aware 인터페이스 실습 |
| `src/main/java/Lect_B/week05/Week05ExternalConfigComponent.java` | 외부 설정 매핑 예제 |
| `src/main/resources/xml/week05-ex1.xml` | XML `SmsSender` 등록 |
| `src/main/resources/xml/week05-ex2.xml` | XML + Lombok + prototype 주입 |
| `src/main/resources/xml/week05-ex3.xml` | XML 기반 List 컬렉션 |
| `src/main/resources/xml/week05-ex4.xml` | XML 기반 Map 컬렉션 |
| `src/main/resources/week05-external.properties` | 5주차 외부 설정 파일 |
| `src/main/webapp/views/week05/*.jsp` | 5주차 결과 화면 |

## 1. 제공된 실습코드 재작성

### 1-1. `autoDI`

원본의 `AnimalAutoDI`는 세 가지 주입 방식을 한 클래스에 모아둔 예제다.

- 필드 주입: `private Animal cat`
- 생성자 주입: `AnimalAutoDI(@Qualifier("xmlSms") SmsSender sms)`
- Setter 주입: `setDog(@Qualifier("dog") Animal dog)`

현재 프로젝트에서는 `/week05/autoDI`로 확인할 수 있게 연결했다.

### 1-2. `commonDI`

원본의 `AbstractCommonService`와 `CommonService`는  
추상 클래스에 공통 의존성을 두고, 하위 클래스에서 추가 상태를 더하는 예제다.

현재 프로젝트에서는 `/week05/commonDI`에서

- 공통 주입된 `Animal`
- 상속된 `defaultValue`
- 하위 클래스의 `periodTime`

을 같이 확인한다.

### 1-3. `lombokXml`

원본 폴더에는 `LombokService`와 `Ex2.xml`이 있었지만 라우트 연결이 빠져 있었다.  
현재 프로젝트에서는 `/week05/lombokXml`로 완성했다.

확인 포인트:

- XML에서 생성자 주입
- XML에서 setter 주입
- `WorkUnit`이 prototype인데, `LombokService`에 주입될 때는 생성 시점의 객체가 들어간다는 점

### 1-4. `list`, `map`

원본의 `AnimalService`, `CollectionDIController`, `Ex3.xml`, `Ex4.xml` 흐름을 재작성했다.

- `/week05/list`: 어노테이션 기반 `List<Animal>`과 XML 기반 `List<Animal>` 비교
- `/week05/map`: 어노테이션 기반 `Map<String, Animal>`과 XML 기반 `Map<String, Animal>` 비교

여기서 `@Order`를 적용해 어노테이션 기반 List 순서도 안정적으로 보여주도록 했다.

## 2. PPT 핵심 범위 확장 실습

원본 실습 폴더만으로는 5주차 PPT의 핵심이 전부 드러나지 않았다.  
그래서 현재 프로젝트 문맥에 맞는 라우트를 추가했다.

### 2-1. `/week05/scope`

이 화면은 PPT 2~8장의 내용을 한 곳에서 확인하게 만든 화면이다.

- singleton: 같은 객체를 계속 재사용
- prototype: 조회할 때마다 새 객체 생성
- request: HTTP 요청마다 새 객체
- session: 사용자 세션마다 유지
- singleton 내부에 prototype을 직접 주입하면 같은 prototype을 계속 씀
- `ObjectFactory`를 쓰면 필요할 때마다 새 prototype을 얻을 수 있음

### 2-2. `/week05/lifecycle`

PPT 9~10장에 맞춰 빈 초기화/종료 흐름을 별도 컨텍스트에서 시뮬레이션했다.

확인 가능한 순서:

1. constructor
2. `@PostConstruct`
3. `afterPropertiesSet()`
4. custom `init-method`
5. `@PreDestroy`
6. `DisposableBean.destroy()`
7. custom `destroy-method`

### 2-3. `/week05/aware`

PPT 11~12장의 `BeanNameAware`, `ApplicationContextAware`를 보여준다.

- 스프링이 전달한 실제 빈 이름
- 컨테이너의 빈 정의 개수
- `ApplicationContext`로 직접 `smsSender`를 조회한 결과

### 2-4. `/week05/properties`

PPT 13~14장에 맞춰 외부 설정 파일을 분리했다.

- `@Value`: `week05.server.*` 같은 단일 값
- `@ConfigurationProperties`: `week05.mail.*` 같은 구조화된 값

## 3. 실습 흐름

```text
/week05
  -> autoDI / commonDI / lombokXml / list / map
  -> scope / lifecycle / aware / properties
  -> 각 JSP에서 주입 결과와 컨테이너 동작 확인
```

## 4. 이번 주차에서 기억할 점

- 5주차는 "빈을 등록한다"보다 "빈을 어떤 범위와 생명주기로 관리하느냐"가 핵심이다.
- prototype은 선언만으로 끝나지 않고, singleton과 함께 쓸 때 주의점이 생긴다.
- 라이프사이클 메서드는 생성자 이후와 종료 직전에 각각 다른 훅을 제공한다.
- 외부 설정은 단일 값 주입과 객체 매핑을 구분해서 보는 것이 중요하다.
