# Assignment 2223002 Theory

## 주제

이 문서는 과제 #1의 요구사항을 하나씩 해석하면서  
"문제를 보면 어떤 생각을 해야 하고, 어떤 구조를 설계해야 하는가"를 정리한 문서다.

## 과제 원문

학생의 성명, 학번 정보를 입력받아 리스트에 저장하고 결과를 출력하는 프로그램을 작성한다.

- C1) 학생 정보는 `StdInfo` 타입 객체에 저장
- C2) 입력받을 학생의 수는 반드시 외부 프로퍼티 파일에 등록
- C3) 학생 정보를 저장하는 `StdInfo` 타입 리스트 및 학생 수는 DI를 이용하여 `StdManager` 클래스의 속성으로 저장
- C4) `StdManager` 클래스는 `ApplicationContextAware`, `InitializingBean` 인터페이스의 구현 클래스로 작성
- C5) 리스트에 추가되는 `StdInfo` 타입 객체는 `StdManager` 클래스에서 획득
- C6) 출력은 `StdManager` 클래스에서 `destroyMethod` 속성에 의해 지정된 메서드를 이용

## 이 과제를 볼 때 먼저 해야 하는 생각

과제 문제를 처음 읽으면 보통 "학생 이름, 학번 입력받으면 되겠네"라고 생각하기 쉽다.  
하지만 이 과제는 단순 입력/출력 문제가 아니다.

문제에서 강조하는 키워드는 다음이다.

- `StdInfo`
- 외부 프로퍼티 파일
- DI
- `StdManager`
- `ApplicationContextAware`
- `InitializingBean`
- `destroyMethod`

즉 이 과제는 기능보다 **스프링 개념을 조건에 맞게 적용하는지**가 핵심이다.

따라서 문제를 보면 먼저:

1. 데이터 객체가 무엇인지
2. 관리용 클래스가 무엇인지
3. 어떤 값이 외부 설정이어야 하는지
4. 어디서 Bean을 꺼내야 하는지
5. 초기화와 종료는 어디서 일어나야 하는지

를 나눠 생각해야 한다.

## 요구사항 분석 표

문제를 읽을 때는 문장을 그대로 외우기보다,  
"이 조건이 어떤 설계를 강제하는가?"를 바로 연결해야 한다.

| 요구사항 | 문제를 보고 바로 떠올려야 할 생각 | 필요한 클래스 / 코드 |
|---|---|---|
| C1. `StdInfo` 타입 객체에 저장 | 학생 1명 = 객체 1개 구조가 필요하다 | `StdInfo`, `studentName`, `studentId` |
| C2. 학생 수는 외부 프로퍼티 파일 | 숫자를 하드코딩하면 안 된다 | `.properties`, `@PropertySource`, `@Value` |
| C3. 리스트와 학생 수를 DI로 저장 | `StdManager`가 직접 만들지 말고 주입받아야 한다 | `List<StdInfo>`, `studentCount`, setter 또는 메서드 인자 주입 |
| C4. `ApplicationContextAware`, `InitializingBean` 구현 | 컨테이너 접근과 초기화 시점을 코드에 녹여야 한다 | `setApplicationContext()`, `afterPropertiesSet()` |
| C5. `StdInfo` 객체는 `StdManager`에서 획득 | `new StdInfo()`보다 `getBean()` 구조가 필요하다 | `applicationContext.getBean(...)` |
| C6. `destroyMethod`로 출력 | 종료 시점 메서드를 설정 클래스에 연결해야 한다 | `@Bean(destroyMethod = "printResult")` |

## 단어를 보면 바로 연결해야 하는 개념

문제에 나온 단어 하나하나를 기능 키워드로 바꾸면 아래처럼 정리된다.

- `타입 객체`
  - 클래스를 만들고, 데이터를 필드로 묶어야 한다는 뜻이다.
- `외부 프로퍼티 파일`
  - 숫자나 설정을 코드에 박아 넣지 말고 분리하라는 뜻이다.
- `DI`
  - 필요한 값이나 객체를 클래스 내부에서 직접 생성하지 말라는 뜻이다.
- `ApplicationContextAware`
  - 스프링 컨테이너 자체를 건네받으라는 뜻이다.
- `InitializingBean`
  - "주입이 다 끝난 뒤"라는 시점을 코드에서 활용하라는 뜻이다.
- `destroyMethod`
  - 종료 시점 로직을 스프링 설정으로 연결하라는 뜻이다.

즉 요구사항은 단순 명령문이 아니라,  
"어떤 스프링 기능을 써서 구조를 짜야 하는가"를 힌트로 준 것이다.

## 1. 요구사항 해석의 출발점

문장:

> 학생의 성명, 학번 정보를 입력받아 리스트에 저장하고 결과를 출력하는 프로그램

이 문장에서 바로 떠올려야 할 것은 아래 3가지다.

### 1-1. 학생 한 명의 데이터를 담는 객체가 필요하다

이름과 학번은 한 학생의 속성이다.  
따라서 이를 묶는 객체가 있어야 한다.

즉:

- 클래스명 후보: `StdInfo`
- 필드: `studentName`, `studentId`

가 자연스럽게 나온다.

### 1-2. 여러 학생을 저장할 리스트가 필요하다

학생이 한 명이 아니라 여러 명 들어오므로:

- `List<StdInfo>`

구조가 필요하다.

### 1-3. 입력, 저장, 출력 전체를 관리하는 클래스가 필요하다

데이터 객체와 별도로:

- 입력을 받고
- 리스트에 추가하고
- 결과를 출력하는

관리용 클래스가 필요하다.

즉:

- 클래스명 후보: `StdManager`

가 자연스럽다.

## 2. C1 해석: `StdInfo` 타입 객체에 저장

조건:

> 학생 정보는 `StdInfo` 타입 객체에 저장

이 조건을 보면 바로:

- `StdInfo`라는 클래스를 만들어야 하고
- 그 안에 이름, 학번 필드를 둬야 하며
- 학생 1명당 객체 1개가 필요하다는 사실

을 떠올려야 한다.

예상 설계:

```java
public class StdInfo {
    private String studentName;
    private String studentId;
}
```

여기서 중요한 점:

- 이름, 학번을 각각 따로 리스트로 관리하는 것이 아니라
- **학생 1명 = 객체 1개**

로 묶는 것이다.

이 방식이 필요한 이유:

- 객체지향적으로 더 자연스럽고
- 리스트에 넣기도 쉽고
- 출력도 편해지기 때문이다

## 3. C2 해석: 학생 수는 외부 프로퍼티 파일

조건:

> 입력받을 학생의 수는 반드시 외부 프로퍼티 파일에 등록

이 조건을 보면 바로 아래를 떠올려야 한다.

- 하드코딩 금지
- 프로퍼티 파일 사용
- `@PropertySource`
- `@Value`

즉 학생 수를 코드에:

```java
int studentCount = 3;
```

처럼 직접 쓰면 안 된다.

대신:

```properties
assignment2223002.student-count=3
```

형태로 외부 파일에 두고 읽어야 한다.

왜 이렇게 하라고 했을까?

- 설정값을 코드와 분리하는 연습을 시키기 위해서다
- 실무에서도 자주 쓰는 방식이기 때문이다

즉 이 조건은 단순히 파일 하나 더 만들라는 뜻이 아니라  
"설정과 코드의 분리"를 이해하라는 의미다.

## 4. C3 해석: 리스트와 학생 수를 DI로 `StdManager` 속성에 저장

조건:

> `StdInfo` 타입 리스트 및 학생 수는 DI를 이용하여 `StdManager` 클래스의 속성으로 저장

이 조건은 굉장히 중요하다.

왜냐하면 이 문장은:

- 리스트를 `StdManager`가 직접 만들지 말고
- 학생 수도 `StdManager` 안에 직접 적지 말고
- 둘 다 외부에서 넣어 주라는 의미

이기 때문이다.

즉 아래처럼 작성하면 과제 의도와 맞지 않는다.

```java
private List<StdInfo> stdInfoList = new ArrayList<>();
private int studentCount = 3;
```

위 방식은 "직접 생성 + 직접 설정"이다.

과제가 원하는 방향은:

```java
public void setStdInfoList(List<StdInfo> stdInfoList) {
    this.stdInfoList = stdInfoList;
}

public void setStudentCount(int studentCount) {
    this.studentCount = studentCount;
}
```

처럼 외부에서 주입받는 구조다.

여기서 알아야 할 핵심 단어:

### DI

DI는 `Dependency Injection`, 의존성 주입이다.

즉 필요한 값을 클래스가 직접 만들지 않고,  
외부 설정 또는 컨테이너가 넣어 주는 방식이다.

이번 과제에서 DI 대상:

- `List<StdInfo>`
- 학생 수 `int`

즉 "객체만 DI되는 게 아니라 리스트나 설정값도 주입 대상"이라는 점을 이해해야 한다.

## 5. C4 해석: `ApplicationContextAware`, `InitializingBean`

조건:

> `StdManager` 클래스는 `ApplicationContextAware`, `InitializingBean` 인터페이스의 구현 클래스로 작성

이 조건은 그냥 외우는 문제가 아니다.  
"왜 하필 이 두 인터페이스를 쓰라고 했는가"를 이해해야 한다.

### 5-1. `ApplicationContextAware`

이 인터페이스를 구현하면 스프링이 컨테이너를 넣어 준다.

핵심 메서드:

```java
public void setApplicationContext(ApplicationContext applicationContext)
```

이걸 쓰는 이유:

- `StdManager`가 나중에 `StdInfo` 빈을 직접 컨테이너에서 꺼내야 하기 때문이다

즉 C5와 연결된다.

### 5-2. `InitializingBean`

이 인터페이스를 구현하면 DI가 끝난 뒤:

```java
public void afterPropertiesSet()
```

메서드가 호출된다.

이걸 쓰는 이유:

- 리스트, 학생 수가 주입된 뒤
- "준비 완료" 상태를 확인하거나 초기 메시지를 출력할 수 있기 때문이다

즉 이 조건은 단순히 인터페이스 2개 붙이라는 뜻이 아니라,

- 컨테이너 접근
- 초기화 시점

을 이해했는지 보는 조건이다.

## 6. C5 해석: `StdInfo` 객체는 `StdManager`에서 획득

조건:

> 리스트에 추가되는 `StdInfo` 타입 객체는 `StdManager` 클래스에서 획득

이 조건은 보통 학생들이 가장 많이 놓치는 부분이다.

잘못 생각하면:

```java
StdInfo stdInfo = new StdInfo();
```

라고 만들기 쉽다.

하지만 문제는 "스프링 과제"다.  
따라서 과제 의도는 `StdManager`가 직접 `new` 하지 않고,
컨테이너에서 `StdInfo`를 받아오는 것이다.

즉 정답 방향은:

```java
StdInfo stdInfo = applicationContext.getBean("stdInfo", StdInfo.class);
```

이 된다.

이 조건을 만족하려면 바로 앞의 C4에서:

- `ApplicationContextAware`

가 왜 필요한지도 연결해서 이해할 수 있어야 한다.

## 7. C6 해석: `destroyMethod`로 출력

조건:

> 출력은 `StdManager` 클래스에서 `destroyMethod` 속성에 의해 지정된 메서드를 이용

이 조건도 매우 중요하다.

보통은 결과를 바로:

```java
stdManager.printResult();
```

처럼 호출하고 싶어진다.

하지만 과제는 그걸 원하지 않는다.

과제가 원하는 구조는:

1. `StdManager`에 결과 출력 메서드를 하나 만들고
2. 설정 클래스에서 그 메서드를 `destroyMethod`로 등록하고
3. 컨텍스트가 종료될 때 자동 실행되게 하는 것

예상 코드:

```java
@Bean(destroyMethod = "printResult")
public StdManager stdManager(...) {
    ...
}
```

이 조건의 의미:

- 스프링 생명주기 개념을 이해하고 있는가
- 종료 시점 메서드 연결을 할 수 있는가

를 보는 것이다.

## 8. 요구사항을 보고 바로 떠올려야 하는 클래스 구조

이 과제를 보고 최종적으로 떠올려야 하는 구조는 아래와 같다.

```text
Assignment2223002Main
  -> 스프링 컨테이너 생성
  -> StdManager bean 획득
  -> 입력 시작

Assignment2223002Config
  -> StdInfo bean 등록
  -> List<StdInfo> bean 등록
  -> StdManager bean 등록
  -> studentCount를 프로퍼티에서 주입
  -> destroyMethod 지정

StdInfo
  -> 학생 이름, 학번 저장

StdManager
  -> 리스트 보관
  -> 학생 수 보관
  -> ApplicationContext 보관
  -> 입력 처리
  -> 결과 출력
```

즉 과제는 사실상 아래 4개를 만들라는 문제다.

- 데이터 클래스
- 관리자 클래스
- 설정 클래스
- 메인 클래스

## 8-1. 함수 단위로는 무엇이 필요할까

클래스 이름만 정하면 아직 절반이다.  
시험에서는 "그 클래스 안에 무슨 함수가 들어가야 하느냐"까지 떠올라야 한다.

### `StdInfo`

- `setStudentName()`
- `setStudentId()`
- `toString()`

즉 데이터 저장과 출력 준비 역할만 있으면 된다.

### `StdManager`

- `setStdInfoList()`
- `setStudentCount()`
- `setApplicationContext()`
- `afterPropertiesSet()`
- `inputStudents()`
- `printResult()`

즉 주입, 초기화, 입력, 출력 흐름이 한 클래스 안에 모인다.

### `Assignment2223002Config`

- `stdInfo()`
- `stdInfoList()`
- `stdManager()`

즉 이 클래스는 "무엇을 빈으로 등록할지"만 담당한다.

### `Assignment2223002Main`

- `main()`

즉 실행 시작점만 담당한다.

## 8-2. 문제를 보고 바로 그릴 수 있어야 하는 연결 그림

```text
properties 파일
  -> studentCount 값 제공

Assignment2223002Config
  -> StdInfo bean 등록(prototype)
  -> List<StdInfo> bean 등록
  -> StdManager bean 등록
     -> 리스트 주입
     -> studentCount 주입
     -> destroyMethod 연결

StdManager
  -> ApplicationContext 전달받음
  -> afterPropertiesSet() 호출
  -> inputStudents()에서 StdInfo bean 반복 획득
  -> 리스트 저장
  -> 종료 시 printResult() 실행
```

## 9. 왜 `StdInfo`는 prototype이어야 하는가

이건 문제에 직접 쓰여 있지는 않지만,
조건을 풀면 자연스럽게 따라나오는 설계 포인트다.

만약 `StdInfo`가 singleton이면:

- 학생 한 명 입력
- 다음 학생 입력

을 해도 같은 객체를 계속 쓰게 된다.

그러면 의도와 다르게 데이터가 덮어써질 수 있다.

따라서 학생 한 명마다 새 객체가 필요하므로:

```java
@Scope("prototype")
```

또는 상수 형태의 prototype 스코프를 쓰는 것이 맞다.

즉 이 과제는 "조건에 안 적혀 있어도 스코프를 스스로 판단해야 하는 문제"이기도 하다.

## 10. 왜 이 과제는 웹이 아니라 콘솔형으로 푸는 것이 자연스러운가

현재 수업 범위 안에서는 이 과제를 굳이 JSP 화면까지 붙일 필요가 없다.

왜냐하면 과제의 핵심 요구사항은:

- Bean 등록
- DI
- 프로퍼티
- Aware
- InitializingBean
- destroyMethod

이지, 웹 화면 구성이 아니기 때문이다.

따라서:

- `AnnotationConfigApplicationContext`
- 콘솔 입력

구조로 푸는 것이 가장 자연스럽다.

즉 요구사항을 보면,
"웹으로 만들까?"보다  
"스프링 컨테이너 기능을 정확히 드러내는 구조가 뭘까?"를 먼저 생각해야 한다.

## 11. 설계 순서 예시

이 과제를 처음부터 푼다고 하면, 아래 순서로 설계하면 된다.

### 1단계. 데이터 객체 설계

- `StdInfo`
- 필드: 이름, 학번
- getter/setter, `toString()`

### 2단계. 설정 파일 설계

- 학생 수를 프로퍼티 파일에 저장

### 3단계. 관리 클래스 설계

- `StdManager`
- 리스트, 학생 수, 컨텍스트 보관
- 입력 메서드
- 출력 메서드

### 4단계. 인터페이스 조건 반영

- `ApplicationContextAware`
- `InitializingBean`

### 5단계. 설정 클래스 작성

- `StdInfo` bean
- `List<StdInfo>` bean
- `StdManager` bean
- `destroyMethod`

### 6단계. 실행 클래스 작성

- 컨테이너 생성
- `StdManager` 획득
- 입력 시작
- 컨텍스트 종료로 결과 출력 유도

이 순서대로 가면 문제를 안정적으로 풀 수 있다.

## 11-1. 시험장에서 바로 쓰는 설계 메모 예시

시험장에서 문제를 받으면 아래처럼 짧게 메모하면 설계가 흔들리지 않는다.

```text
1. 학생 1명 정보 -> StdInfo
2. 여러 명 저장 -> List<StdInfo>
3. 관리 클래스 -> StdManager
4. 학생 수 -> properties
5. 리스트 + 학생 수 -> DI
6. StdInfo 생성 위치 -> StdManager에서 getBean()
7. 종료 출력 -> destroyMethod
```

이 메모만 안정적으로 뽑아낼 수 있으면,  
답안을 클래스/메서드 수준으로 확장하기가 훨씬 쉬워진다.

## 11-2. 최소 답안 뼈대

아래 뼈대는 시험에서 "전체 구조"를 설명할 때 바로 적기 좋은 수준이다.

```java
public class StdInfo {
    private String studentName;
    private String studentId;
}

public class StdManager implements ApplicationContextAware, InitializingBean {
    private List<StdInfo> stdInfoList;
    private int studentCount;
    private ApplicationContext applicationContext;

    public void inputStudents() {
        StdInfo stdInfo = applicationContext.getBean("stdInfo", StdInfo.class);
    }

    public void printResult() {
    }
}

@Configuration
@PropertySource("classpath:static/assignment2223002.properties")
public class Assignment2223002Config {
}
```

이 정도 골격을 먼저 세운 뒤 세부 구현을 붙이면  
문제를 훨씬 안정적으로 풀 수 있다.

## 12. 시험 대비 관점에서 이 과제가 중요한 이유

시험에서 이런 과제가 나오면 보통 단순 구현보다 아래를 본다.

- 요구사항을 클래스 구조로 옮길 수 있는가
- 어떤 Spring 기능을 써야 하는지 판단할 수 있는가
- `new` 대신 Bean 획득 구조를 이해했는가
- 초기화/종료 시점을 이해했는가
- 설정값을 외부 파일로 분리할 수 있는가

즉 이 문제는:

- Bean
- DI
- 외부 설정
- lifecycle
- context 접근

을 한 번에 점검하는 통합형 문제다.

## 13. 시험에 나오면 바로 답해야 하는 질문

### Q1. 왜 `StdManager`가 `ApplicationContextAware`를 구현해야 하나?

`StdManager` 내부에서 `StdInfo` 빈을 직접 가져와야 하기 때문이다.

### Q2. 왜 `StdInfo`는 prototype이어야 하나?

학생 한 명당 새 객체가 필요하기 때문이다.

### Q3. 학생 수를 왜 프로퍼티 파일에 둬야 하나?

조건에서 외부 설정을 요구했고,
설정과 코드를 분리하는 연습이기 때문이다.

### Q4. 왜 결과 출력은 직접 호출하지 않고 `destroyMethod`를 쓰는가?

문제에서 종료 시점 메서드 연결을 요구하기 때문이다.

### Q5. `List<StdInfo>`도 DI 대상이 될 수 있는가?

그렇다. 스프링은 컬렉션도 빈으로 등록하고 주입할 수 있다.

### Q6. 왜 `StdManager`가 모든 일을 하는 것처럼 보이는데도 괜찮은가?

이번 과제는 학습 범위가 Bean, DI, lifecycle 중심이므로  
서비스 계층을 과하게 쪼개기보다 요구사항을 가장 잘 드러내는 단일 관리 클래스로 두는 편이 자연스럽다.

## 14. 기존 주차 문서와 연결

이 과제를 이해하려면 아래 문서와 함께 보면 좋다.

- [3주차 이론](../week03/theory.md)
  - Bean, Container, `getBean()`
- [4주차 이론](../week04/theory.md)
  - DI, `@Value`, Java Config
- [5주차 이론](../week05/theory.md)
  - scope, lifecycle, 외부 프로퍼티
- [6주차 이론](../week06/theory.md)
  - `ApplicationContextAware`, `InitializingBean`

즉 이 과제는 별도 문제이면서도  
3주차~6주차 내용을 실제로 엮어 보는 종합 문제다.

## 15. 최종 정리

이 과제를 잘 푼다는 것은 단순히 학생 정보를 저장하는 프로그램을 만드는 것이 아니다.

정확히는:

- 어떤 클래스가 필요한지 나누고
- 어떤 값이 외부 설정이어야 하는지 판단하고
- 어떤 객체를 Bean으로 관리해야 하는지 정하고
- 어떤 객체를 DI로 넣어야 하는지 설계하고
- 초기화와 종료 시점을 스프링 방식으로 연결하는 것

까지 할 수 있어야 한다는 뜻이다.

즉 이 과제는 작은 프로그램이지만,  
스프링 객체 관리 흐름을 이해했는지 확인하는 좋은 문제다.
