# Assignment 2223002 Practice

## 주제

이 문서는 `assignment2223002` 과제를 현재 프로젝트 코드 기준으로 해설하는 실습 문서다.

즉 이론 문서가 "문제를 어떻게 해석할 것인가"였다면,  
이 문서는 "실제로 어떤 파일을 만들었고 각 코드가 무슨 역할을 하는가"를 설명한다.

## 관련 파일

| 경로 | 역할 |
|---|---|
| `src/main/java/Lect_B/assignment2223002/StdInfo.java` | 학생 정보 객체 |
| `src/main/java/Lect_B/assignment2223002/StdManager.java` | 입력/저장/출력 관리자 |
| `src/main/java/Lect_B/assignment2223002/Assignment2223002Config.java` | Bean 등록, DI, destroyMethod 설정 |
| `src/main/java/Lect_B/assignment2223002/Assignment2223002Main.java` | 실행용 메인 클래스 |
| `src/main/resources/static/assignment2223002.properties` | 학생 수 외부 설정 파일 |

## 요구사항과 실제 코드 매핑

| 요구사항 | 실제 반영 파일 | 핵심 코드 / 메서드 |
|---|---|---|
| C1. `StdInfo` 타입 객체에 저장 | `StdInfo.java` | `studentName`, `studentId` |
| C2. 학생 수를 외부 프로퍼티에 등록 | `assignment2223002.properties`, `Assignment2223002Config.java` | `assignment2223002.student-count`, `@Value` |
| C3. 리스트와 학생 수를 DI로 저장 | `Assignment2223002Config.java`, `StdManager.java` | `stdInfoList()`, `stdManager(...)`, `setStdInfoList()`, `setStudentCount()` |
| C4. `ApplicationContextAware`, `InitializingBean` 구현 | `StdManager.java` | `setApplicationContext()`, `afterPropertiesSet()` |
| C5. `StdInfo` 객체는 `StdManager`에서 획득 | `StdManager.java` | `applicationContext.getBean("stdInfo", StdInfo.class)` |
| C6. `destroyMethod`로 출력 | `Assignment2223002Config.java`, `StdManager.java` | `@Bean(destroyMethod = "printResult")`, `printResult()` |

## 1. 먼저 전체 흐름부터 보기

이 과제의 실행 흐름은 아래처럼 보면 가장 이해가 쉽다.

```text
메인 클래스 실행
  -> Spring 컨테이너 생성
  -> 설정 클래스 로딩
  -> StdInfo / 리스트 / StdManager 빈 등록
  -> 학생 수 주입
  -> StdManager 초기화 완료
  -> 학생 정보 입력
  -> StdManager가 StdInfo bean을 반복 획득
  -> 리스트에 저장
  -> 컨텍스트 종료
  -> destroyMethod로 결과 출력
```

즉 "입력 프로그램"이면서 동시에  
"Spring 컨테이너 생명주기 프로그램"이다.

## 2. `StdInfo`는 왜 단순해야 하는가

파일:

`src/main/java/Lect_B/assignment2223002/StdInfo.java`

핵심 코드:

```java
private String studentName;
private String studentId;
```

이 클래스는 일부러 단순하게 두는 것이 좋다.

이유:

- 학생 1명의 데이터만 저장하는 역할에 집중해야 하고
- 관리 로직까지 들어가면 `StdManager`와 역할이 겹치기 때문이다

즉 `StdInfo`는 DTO처럼 아주 단순한 구조가 자연스럽다.

### 코드 읽을 때 포인트

- 이름과 학번을 한 객체로 묶었다
- getter/setter로 값 저장과 조회를 한다
- `toString()`으로 출력 형식을 정리했다

### 시험장에서 이 클래스를 설명하는 한 줄

`StdInfo`는 학생 1명의 데이터를 담는 순수 데이터 객체이며, 과제 조건 C1을 만족시키는 가장 기본 클래스다.

## 3. `StdManager`가 과제의 핵심인 이유

파일:

`src/main/java/Lect_B/assignment2223002/StdManager.java`

핵심 선언:

```java
public class StdManager implements ApplicationContextAware, InitializingBean
```

이 한 줄이 과제 핵심이다.

이유:

- 컨테이너를 직접 받아야 하고
- 초기화 완료 시점을 활용해야 하기 때문이다

### 3-1. DI로 들어오는 속성

```java
private List<StdInfo> stdInfoList;
private int studentCount;
private ApplicationContext applicationContext;
```

이 세 속성은 각각 의미가 다르다.

- `stdInfoList`
  - 학생 정보를 저장할 실제 리스트
- `studentCount`
  - 몇 명 입력받을지 결정하는 수
- `applicationContext`
  - `StdInfo` bean을 가져오기 위한 컨테이너 참조

### 3-2. `afterPropertiesSet()`가 왜 필요한가

```java
@Override
public void afterPropertiesSet() {
    System.out.println("[초기화] StdManager 준비 완료");
    System.out.println("[초기화] 입력할 학생 수: " + studentCount);
}
```

이 메서드는 단순 출력용처럼 보이지만 의미가 크다.

보여 주는 것:

- DI가 끝난 뒤 호출된다
- 즉 이 시점에는 학생 수가 이미 주입되어 있다

따라서 이 메서드는:

"초기화 완료 후 상태를 확인하는 자리"

라고 이해하면 좋다.

### 3-3. `StdInfo` bean을 직접 획득하는 부분

```java
StdInfo stdInfo = applicationContext.getBean("stdInfo", StdInfo.class);
```

이 줄은 교수님이 가장 보기 쉬운 체크 포인트 중 하나다.

이 코드가 의미하는 것:

- `StdManager`가 직접 `new StdInfo()`를 하지 않는다
- 컨테이너가 관리하는 `stdInfo` 빈을 가져온다
- 따라서 과제 요구사항 C5를 만족한다

여기서 같이 떠올려야 할 것:

- `StdInfo`가 prototype이어야 한다는 점
- 그래야 반복할 때 매번 다른 객체를 받는다

### 이 메서드 전체를 한 문장으로 설명하면

`inputStudents()`는 입력 횟수만큼 prototype `StdInfo` 빈을 받아 값을 채운 뒤 리스트에 누적하는 메서드다.

## 4. `Assignment2223002Config`를 읽는 법

파일:

`src/main/java/Lect_B/assignment2223002/Assignment2223002Config.java`

이 설정 클래스는 과제 전체를 설계도로 바꿔 놓은 파일이다.

### 4-1. 외부 설정 읽기

```java
@Configuration
@PropertySource("classpath:static/assignment2223002.properties")
```

이 코드가 의미하는 것:

- 설정 클래스다
- 별도 프로퍼티 파일을 읽는다

즉 과제 조건 C2가 시작되는 지점이다.

### 4-2. `StdInfo` bean 등록

```java
@Bean
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public StdInfo stdInfo() {
    return new StdInfo();
}
```

이 코드를 보면 다음을 설명할 수 있어야 한다.

- 왜 `@Bean`인가
- 왜 prototype인가

정답 방향:

- `StdInfo`를 스프링이 관리하게 해야 `StdManager`가 `getBean()`으로 가져올 수 있다
- 학생마다 새 객체가 필요하므로 prototype이 맞다

### 4-3. 리스트 bean 등록

```java
@Bean
public List<StdInfo> stdInfoList() {
    return new ArrayList<>();
}
```

이 부분이 은근히 중요하다.

과제에서 리스트도 DI로 넣으라고 했기 때문에:

- 리스트를 그냥 `StdManager` 안에서 만들지 않고
- 아예 bean으로 등록했다

즉 컬렉션도 스프링 관리 대상이 될 수 있다는 점을 보여 준다.

### 4-4. `StdManager` bean 등록

```java
@Bean(destroyMethod = "printResult")
public StdManager stdManager(List<StdInfo> stdInfoList,
        @Value("${assignment2223002.student-count}") int studentCount) {
    StdManager stdManager = new StdManager();
    stdManager.setStdInfoList(stdInfoList);
    stdManager.setStudentCount(studentCount);
    return stdManager;
}
```

이 메서드는 사실상 과제 정답의 중심이다.

보여 주는 것:

- 리스트가 DI로 들어온다
- 학생 수가 프로퍼티에서 주입된다
- `destroyMethod`가 지정된다

즉 C2, C3, C6를 동시에 만족시키는 코드다.

## 4-5. 이 설정 클래스를 설계도로 읽는 법

이 파일을 읽을 때는 메서드 3개를 각각 객체 하나의 등록 규칙으로 보면 된다.

- `stdInfo()`
  - 학생 1명 객체의 생성 규칙
- `stdInfoList()`
  - 저장소 역할 리스트의 생성 규칙
- `stdManager()`
  - 관리자의 생성 규칙과 의존성 연결 규칙

즉 이 파일은 "실행 로직"이 아니라  
"어떤 객체를 어떤 정책으로 만들고 연결할지"를 적은 설계도다.

## 5. `Assignment2223002Main`가 단순해야 하는 이유

파일:

`src/main/java/Lect_B/assignment2223002/Assignment2223002Main.java`

핵심 코드:

```java
try (AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(Assignment2223002Config.class)) {
    StdManager stdManager = context.getBean("stdManager", StdManager.class);
    stdManager.inputStudents();
}
```

이 메인 클래스는 일부러 단순한 것이 좋다.

왜냐하면:

- 실행 시작
- 컨테이너 생성
- 매니저 bean 획득
- 입력 시작

까지만 맡기고,
나머지 로직은 전부 `StdManager`와 설정 클래스가 처리해야  
역할이 잘 나뉘기 때문이다.

그리고 `try-with-resources`를 쓴 이유는:

- 블록이 끝나면 컨텍스트가 자동 종료되고
- 그 순간 `destroyMethod`가 실행되도록 하기 위해서다

즉 메인 클래스는 "프로그램 시작점" 역할에만 집중한다.

## 6. 프로퍼티 파일이 왜 별도 파일인가

파일:

`src/main/resources/static/assignment2223002.properties`

내용:

```properties
assignment2223002.student-count=3
```

이 한 줄은 단순해 보이지만 의미가 크다.

이걸 별도 파일에 둔 이유:

- 과제 조건 충족
- 설정과 코드 분리
- 코드 수정 없이 학생 수만 바꿀 수 있음

즉 "값 하나인데 굳이 파일로?"가 아니라  
"설정값이므로 파일로"가 스프링다운 사고 방식이다.

## 7. 이 과제를 교수님 앞에서 설명한다면

아래 순서로 설명하면 가장 자연스럽다.

### 설명 순서 1. 문제 요구사항

- 학생 정보는 `StdInfo`
- 관리 클래스는 `StdManager`
- 학생 수는 외부 프로퍼티

### 설명 순서 2. 스프링 적용 포인트

- 리스트와 학생 수를 DI
- `StdManager`는 `ApplicationContextAware`, `InitializingBean` 구현
- `StdInfo`는 prototype bean

### 설명 순서 3. 실행 흐름

- main에서 컨텍스트 생성
- `StdManager` 획득
- 반복 입력
- 종료 시 `printResult()` 자동 실행

이 순서로 말하면
"조건을 이해하고 설계했다"는 인상을 주기 쉽다.

## 8. 시험장에서 서술형으로 쓰는 답안 구조

시험에서 "이 과제를 어떻게 설계할 것인가"를 서술형으로 묻는다면 아래 구조로 답하면 안정적이다.

1. 학생 1명의 정보를 저장하기 위해 `StdInfo` 클래스를 만든다.
2. 여러 학생을 저장하기 위해 `List<StdInfo>`를 사용한다.
3. 입력, 저장, 출력을 담당하는 `StdManager` 클래스를 만든다.
4. 학생 수는 외부 프로퍼티 파일에 저장하고 `@Value`로 주입한다.
5. 리스트와 학생 수는 DI로 `StdManager`에 넣는다.
6. `StdManager`는 `ApplicationContextAware`를 구현해 `StdInfo` 빈을 획득한다.
7. `InitializingBean`을 구현해 초기화 완료 시점을 확인한다.
8. 출력은 `destroyMethod`로 연결한 `printResult()`에서 수행한다.

## 9. 시험 대비 체크리스트

아래 질문에 답할 수 있으면 이 과제를 이해한 것이다.

- 왜 `StdInfo`를 그냥 `new` 하지 않았는가?
- 왜 `StdManager`가 컨텍스트를 알아야 하는가?
- 왜 학생 수를 코드에 직접 적으면 안 되는가?
- 왜 리스트도 DI 대상이 될 수 있는가?
- 왜 출력 메서드를 직접 호출하지 않고 `destroyMethod`로 연결했는가?
- 왜 `StdInfo`는 singleton보다 prototype이 자연스러운가?

## 10. 자주 나오는 실수

### 실수 1. `StdInfo`를 직접 `new` 하는 경우

이러면 과제 조건 C5 의도와 멀어진다.

### 실수 2. 학생 수를 코드에 직접 적는 경우

외부 프로퍼티 조건을 만족하지 못한다.

### 실수 3. 리스트를 `StdManager` 내부에서 직접 만드는 경우

DI 조건 C3를 약하게 만든다.

### 실수 4. 결과를 main에서 직접 출력하는 경우

`destroyMethod` 조건을 만족하지 못한다.

### 실수 5. `StdInfo`를 singleton처럼 쓰는 경우

반복 입력에서 같은 객체를 재사용하게 될 위험이 있다.

## 11. 기존 주차 문서와 연결해서 보기

이 과제는 아래 문서와 같이 보면 더 잘 보인다.

- [3주차 실습](../week03/practice.md)
  - `getBean()`과 컨테이너 조회 감각
- [4주차 실습](../week04/practice.md)
  - DI, `@Value`, Java Config
- [5주차 실습](../week05/practice.md)
  - lifecycle, external properties
- [6주차 실습](../week06/practice.md)
  - `ApplicationContextAware`, `InitializingBean`, 실행 흐름

즉 이 과제는 새로운 별도 세계가 아니라,  
기존 주차 실습을 과제 형태로 묶은 것이다.

## 12. 최종 정리

이 과제의 실전 포인트는 기능보다 구조다.

정리하면:

- `StdInfo`는 데이터 객체
- `StdManager`는 관리 객체
- 설정 클래스는 Bean 등록과 DI 담당
- 메인 클래스는 실행 시작점
- 프로퍼티 파일은 외부 설정 담당

그리고 이 모든 것을 스프링 컨테이너가 연결한다.

즉 과제를 풀 때는 "입력받아 저장"보다  
"어떤 스프링 기능을 어디에 써야 하는가"를 먼저 떠올리는 것이 중요하다.
